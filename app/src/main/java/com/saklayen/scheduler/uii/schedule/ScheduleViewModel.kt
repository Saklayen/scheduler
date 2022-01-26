package com.saklayen.scheduler.uii.schedule

import androidx.lifecycle.ViewModel
import com.saklayen.scheduler.utils.EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(): ViewModel() {
    val appName = MutableStateFlow(String.EMPTY)
}