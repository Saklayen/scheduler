package com.saklayen.scheduler.ui.schedule

import android.app.AlarmManager
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saklayen.scheduler.utils.EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import android.app.PendingIntent
import android.content.Context

import android.content.Intent




@HiltViewModel
class ScheduleViewModel @Inject constructor() : ViewModel() {
    val appIndex = MutableStateFlow(Int.MIN_VALUE)
    val appName = MutableStateFlow(String.EMPTY)
    val packageName = MutableStateFlow(String.EMPTY)

    private val _showTimePicker = Channel<Unit>(Channel.CONFLATED)
    val showTimePicker = _showTimePicker.receiveAsFlow()

    private val _setSchedule = Channel<Unit>(Channel.CONFLATED)
    val setSchedule = _setSchedule.receiveAsFlow()

    val calendar: Calendar = Calendar.getInstance()
    var schedule = MutableStateFlow(String.EMPTY)

    lateinit var alarmManager: AlarmManager

    fun onClickSetSchedule() {
        _showTimePicker.trySend(Unit)
    }

    private fun setSchedule(){
        _setSchedule.trySend(Unit)
    }

    fun setSchedule(context: Context){
        val myIntent = Intent(context, ScheduleReceiver::class.java)
        myIntent.putExtra("packageName", packageName.value)
        val pendingIntent = PendingIntent.getBroadcast(context, appIndex.value, myIntent, appIndex.value)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)
    }

    val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        viewModelScope.launch {
            calendar.set(0, 0, 0, hourOfDay, minute)
            val dateFormat = SimpleDateFormat("hh:mm aa", Locale.US)
            schedule.value = "Scheduled on: "+ dateFormat.format(calendar.time).toString()
            setSchedule()
        }
    }
}