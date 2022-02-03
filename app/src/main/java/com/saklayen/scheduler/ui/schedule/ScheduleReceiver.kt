package com.saklayen.scheduler.ui.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.saklayen.scheduler.database.repositories.ScheduleRepositories
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ScheduleReceiver : BroadcastReceiver() {
    @Inject
    lateinit var scheduleRepositories: ScheduleRepositories
    private var job: Job? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        val packageName = intent?.getStringExtra("packageName")
        val requestCode = intent?.getIntExtra("requestCode",0)
        job?.cancel()
        job = GlobalScope.launch(Dispatchers.IO) {
            requestCode?.let { scheduleRepositories.updateScheduleStatus(requestCode, true) }
        }
        val mainIntent =
            packageName?.let { context?.packageManager?.getLaunchIntentForPackage(it) }
        context?.startActivity(mainIntent)
    }
}