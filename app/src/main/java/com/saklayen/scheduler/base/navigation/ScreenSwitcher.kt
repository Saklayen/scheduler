package com.saklayen.scheduler.base.navigation

interface ScreenSwitcher<S : Screen> {
    fun open(mScreen: S)

    fun goBack()
}
