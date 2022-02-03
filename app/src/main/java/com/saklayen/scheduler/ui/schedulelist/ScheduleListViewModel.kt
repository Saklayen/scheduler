package com.saklayen.scheduler.ui.schedulelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saklayen.scheduler.database.model.Schedule
import com.saklayen.scheduler.database.repositories.ScheduleRepositories
import com.saklayen.scheduler.domain.Result
import com.saklayen.scheduler.utils.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ScheduleListViewModel @Inject constructor(val scheduleRepositories: ScheduleRepositories): ViewModel() {
    private val _navigationActions = Channel<Schedule>(Channel.CONFLATED)
    val navigationActions = _navigationActions.receiveAsFlow()

    private val fetchScheduledApplicationList =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val scheduledApplicationList = fetchScheduledApplicationList.flatMapLatest {
        scheduleRepositories.getScheduledApps(isStarted = false)!!
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = null
    )

    private val fetchCompletedScheduleList =
        MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val completedScheduleList = fetchCompletedScheduleList.flatMapLatest {
        scheduleRepositories.getScheduledApps(isStarted = true)!!
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = null
    )


    init {
        viewModelScope.launch {
            getCompletedScheduleApp()
        }
        viewModelScope.launch {
            getScheduledApps()
        }
    }
    private fun getCompletedScheduleApp(){
        fetchCompletedScheduleList.tryEmit(Unit)
    }
    private fun getScheduledApps(){
        fetchScheduledApplicationList.tryEmit(Unit)
    }

    fun onClickAppItem(schedule: Schedule){
        //navigate to schedule fragment for modification
        _navigationActions.trySend(schedule)

    }
}