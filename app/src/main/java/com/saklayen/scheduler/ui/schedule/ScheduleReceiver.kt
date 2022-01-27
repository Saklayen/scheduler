package com.saklayen.scheduler.ui.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScheduleReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val packageName = intent?.getStringExtra("packageName")
        val mainIntent =
            packageName?.let { context?.packageManager?.getLaunchIntentForPackage(it) }
        context?.startActivity(mainIntent)
    }
}