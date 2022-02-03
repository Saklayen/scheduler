package com.saklayen.scheduler.ui.updateschedule

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saklayen.scheduler.database.repositories.ScheduleRepositories
import com.saklayen.scheduler.ui.schedule.ScheduleReceiver
import com.saklayen.scheduler.utils.EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class UpdateScheduleViewModel @Inject constructor(
    val scheduleRepositories: ScheduleRepositories
) : ViewModel() {
    val rowId = MutableStateFlow(Int.MIN_VALUE)
    val requestCode = MutableStateFlow(Int.MIN_VALUE)
    val appName = MutableStateFlow(String.EMPTY)
    val packageName = MutableStateFlow(String.EMPTY)

    private val _showTimePicker = Channel<Unit>(Channel.CONFLATED)
    val showTimePicker = _showTimePicker.receiveAsFlow()

    private val _updateSchedule = Channel<Unit>(Channel.CONFLATED)
    val updateSchedule = _updateSchedule.receiveAsFlow()

    private val _cancelSchedule = Channel<Unit>(Channel.CONFLATED)
    val cancelSchedule = _cancelSchedule.receiveAsFlow()

    private val _message = Channel<String>(Channel.CONFLATED)
    val message = _message.receiveAsFlow()

    val calendar: Calendar = Calendar.getInstance()
    var schedule = MutableStateFlow(String.EMPTY)

    lateinit var alarmManager: AlarmManager

    private val dateFormat = SimpleDateFormat("hh:mm aa", Locale.US)

    fun onClickUpdateSchedule() {
        _showTimePicker.trySend(Unit)
    }

    private fun updateSchedule() {
        _updateSchedule.trySend(Unit)
    }

    suspend fun updateSchedule(context: Context) {
        //check conflict then set schedule
        val isConflicted =
            scheduleRepositories.checkConflict(dateFormat.format(calendar.time).toString())?.first()

        if (isConflicted == true) {
            Timber.d("Conflicted----->")
        } else {

            updateExactSchedule(calendar.timeInMillis, getPendingIntent(context))

            viewModelScope.launch {
                scheduleRepositories.updateSchedule(
                    rowId.value,
                    dateFormat.format(calendar.time).toString(),
                )
            }


        }
    }

    fun cancelSchedule() {
        _cancelSchedule.trySend(Unit)
    }

    suspend fun cancelSchedule(context: Context) {
        alarmManager.cancel(
            getPendingIntent(context)
        )
        viewModelScope.launch {
            scheduleRepositories.deleteSchedule(
                rowId.value
            )
            _message.trySend("Schedule Cancelled")
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
                "Time: --> year" + calendar.get(Calendar.YEAR) + " month:" + calendar.get(
                    Calendar.MONTH
                ) + "day " + calendar.get(Calendar.DAY_OF_MONTH)
            )
            if (calendar.before(Calendar.getInstance())) {
                _message.trySend("Only future time allowed.")
            } else {
                schedule.value = "Scheduled on: " + dateFormat.format(calendar.time).toString()
                updateSchedule()
            }
        }
    }

    private fun updateExactSchedule(timeInMillis: Long, pendingIntent: PendingIntent) {
        updateSchedule(
            timeInMillis,
            pendingIntent
        )
    }

    private fun getPendingIntent(context: Context): PendingIntent {
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


    private fun updateSchedule(timeInMillis: Long, pendingIntent: PendingIntent) {
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