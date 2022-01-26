package com.saklayen.scheduler.uii.home

import androidx.lifecycle.ViewModel
import com.saklayen.scheduler.model.App
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    var applicationList = MutableStateFlow(
        mutableListOf(App(
        "xx","yy"
    )))

    private val _navigationActions = Channel<App>(Channel.CONFLATED)
    val navigationActions = _navigationActions.receiveAsFlow()

    private val _getInstalledApps = Channel<Unit>(Channel.CONFLATED)
    val getInstalledApps = _getInstalledApps.receiveAsFlow()


    init {
        getInstalledApps()
    }


    fun onClickAppItem(app: App) {
        _navigationActions.trySend(app)
    }

    private fun getInstalledApps() {
        _getInstalledApps.trySend(Unit)
    }
}
