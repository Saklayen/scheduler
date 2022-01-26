package com.saklayen.scheduler.base.ui

import com.google.android.material.appbar.MaterialToolbar
import com.saklayen.scheduler.base.navigation.ActivityScreenSwitcher

interface NavigationHost {
    fun registerToolbarWithNavigation(toolbar: MaterialToolbar)

    fun activityScreenSwitcher(): ActivityScreenSwitcher
}
