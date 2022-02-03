package com.saklayen.scheduler.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.NavDestination
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference

/**
 * Sets up a [BottomNavigationView] for use with a [NavController]. This will call
 * [android.view.MenuItem.onNavDestinationSelected] when a menu item is selected.
 *
 * The selected item in the NavigationView will automatically be updated when the destination
 * changes.
 */

fun BottomNavigationView.setupNavController(navController: NavController) {
    setupWithNavController(this, navController)
}

/**
 * Sets up a [BottomNavigationView] for use with a [NavController]. This will call
 * [.onNavDestinationSelected] when a menu item is selected. The
 * selected item in the BottomNavigationView will automatically be updated when the destination
 * changes.
 *
 * @param bottomNavigationView The BottomNavigationView that should be kept in sync with
 * changes to the NavController.
 * @param navController The NavController that supplies the primary menu.
 * Navigation actions on this NavController will be reflected in the
 * selected item in the BottomNavigationView.
 */
fun setupWithNavController(
    bottomNavigationView: BottomNavigationView,
    navController: NavController
) {
    bottomNavigationView.setOnNavigationItemSelectedListener { item ->
        if (item.itemId != navController.currentDestination?.id)
            NavigationUI.onNavDestinationSelected(item, navController)
        else false

    }
    val weakReference = WeakReference(bottomNavigationView)
    navController.addOnDestinationChangedListener(
        object : OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination, arguments: Bundle?
            ) {
                val view = weakReference.get()
                if (view == null) {
                    navController.removeOnDestinationChangedListener(this)
                    return
                }
                val menu = view.menu
                var h = 0
                val size = menu.size()
                while (h < size) {
                    val item = menu.getItem(h)
                    if (matchDestination(destination, item.itemId)) {
                        item.isChecked = true
                    }
                    h++
                }
            }
        })
}


/**
 * Determines whether the given `destId` matches the NavDestination. This handles
 * both the default case (the destination's id matches the given id) and the nested case where
 * the given id is a parent/grandparent/etc of the destination.
 */
fun matchDestination(
    destination: NavDestination,
    @IdRes destId: Int
): Boolean {
    var currentDestination: NavDestination? = destination
    while (currentDestination!!.id != destId && currentDestination.parent != null) {
        currentDestination = currentDestination.parent
    }
    return currentDestination.id == destId
}