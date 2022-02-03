package com.saklayen.scheduler.ui.schedule

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saklayen.scheduler.database.model.Schedule
import com.saklayen.scheduler.database.repositories.ScheduleRepositories
import com.saklayen.scheduler.utils.EMPTY
import com.saklayen.scheduler.utils.getRandomInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@DelicateCoroutinesApi
@HiltViewModel
class ScheduleViewModel @Inject constructor(val scheduleRepositories: ScheduleRepositories) :
    ViewModel() {
    val requestCode = MutableStateFlow(Int.MIN_VALUE)
    val appName = MutableStateFlow(String.EMPTY)
    val packageName = MutableStateFlow(String.EMPTY)

    private val _showTimePicker = Channel<Unit>(Channel.CONFLATED)
    val showTimePicker = _showTimePicker.receiveAsFlow()

    private val _setSchedule = Channel<Unit>(Channel.CONFLATED)
    val setSchedule = _setSchedule.receiveAsFlow()

    val calendar: Calendar = Calendar.getInstance()
    var schedule = MutableStateFlow(String.EMPTY)

    private val _message = Channel<String>(Channel.CONFLATED)
    val message = _message.receiveAsFlow()

    lateinit var alarmManager: AlarmManager

    private val dateFormat = SimpleDateFormat("hh:mm aa")
    private val dateFormatTest = SimpleDateFormat("yyyy-MMM-dd hh:mm aa")

    fun onClickSetSchedule() {
        _showTimePicker.trySend(Unit)
    }

    private fun setSchedule() {
        _setSchedule.trySend(Unit)
    }

    suspend fun setSchedule(context: Context) {
        //check conflict then set schedule

        viewModelScope.launch {
            val isConflicted =
                scheduleRepositories.checkConflict(dateFormat.format(calendar.time).toString())
                    ?.first()

            if (isConflicted == true) {
                _message.trySend("Time Conflicts")
            } else {

                setExactSchedule(calendar.timeInMillis, getPendingIntent(context))
                scheduleRepositories.insertSchedule(
                    Schedule(
                        0,
                        dateFormat.format(calendar.time).toString(),
                        appName.value,
                        packageName.value,
                        requestCode.value,
                        requestCode.value.toString(),
                        false
                    )
                )

            }
        }
    }

    val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        viewModelScope.launch {

            calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                hourOfDay,
                minute,
                0
            )
            Timber.d(
                "%s%s", "Timed--> " + "Scheduled on: ", dateFormatTest.format(calendar.time).toString()
            )
            Timber.d(
                "Time: --> year" + calendar.get(Calendar.YEAR) + " month:" + calendar.get(
                    Calendar.MONTH
                ) + "day " + calendar.get(Calendar.DAY_OF_MONTH)
            )
            if (calendar.before(Calendar.getInstance())) {
                _message.trySend("Only future time allowed.")
            } else {
                schedule.value = "Scheduled on: " + dateFormat.format(calendar.time).toString()
                setSchedule()
            }
        }
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        requestCode.value = getRandomInt()
        val myIntent = Intent(context, ScheduleReceiver::class.java)
        myIntent.putExtra("packageName", packageName.value)
        myIntent.putExtra("requestCode", requestCode.value)
        return PendingIntent.getBroadcast(
            context,
            requestCode.value,
            myIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun setExactSchedule(timeInMillis: Long, pendingIntent: PendingIntent) {
        setSchedule(
            timeInMillis,
            pendingIntent
        )
    }

    private fun setSchedule(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
        }
    }

}