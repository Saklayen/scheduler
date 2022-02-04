package com.saklayen.scheduler.ui.home

import android.content.Context
import android.content.pm.PackageInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saklayen.scheduler.model.App
import com.saklayen.scheduler.utils.WhileViewSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    val fetchInstalledApplicationList =
        MutableSharedFlow<Context>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val installedApplicationList = fetchInstalledApplicationList.flatMapLatest {
        getInstalledApps(it, false)
    }.stateIn(
        scope = viewModelScope,
        started = WhileViewSubscribed,
        initialValue = null
    )
    private val _navigationActions = Channel<App>(Channel.CONFLATED)
    val navigationActions = _navigationActions.receiveAsFlow()

    private val _getInstalledApps = Channel<Unit>(Channel.CONFLATED)
    val getInstalledApps = _getInstalledApps.receiveAsFlow()


    init {
        viewModelScope.launch {
            getInstalledApps()
        }
    }


    fun onClickAppItem(app: App) {
        _navigationActions.trySend(app)
    }

    private fun getInstalledApps() {
        _getInstalledApps.trySend(Unit)
    }

    private fun getInstalledApps(context: Context, getSysPackages: Boolean): StateFlow<List<App>> {

        val res = mutableListOf<App>()

        val packs = context.packageManager.getInstalledPackages(0)
        for (i in 0 until packs.size) {
            val packageInfo: PackageInfo = packs[i]
            if ((!getSysPackages) && (packageInfo.versionName == null)) {
                continue
            }
            val appInfo = App(
                packageInfo.applicationInfo.loadLabel(context.packageManager).toString(),
                packageInfo.packageName,
                packageInfo.applicationInfo.loadIcon(context.packageManager)
            )
            res.add(appInfo)
        }
        return MutableStateFlow(res)

    }
}
