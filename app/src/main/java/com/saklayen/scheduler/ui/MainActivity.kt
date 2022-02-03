package com.saklayen.scheduler.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.saklayen.scheduler.R
import com.saklayen.scheduler.base.ui.BaseActivity
import com.saklayen.scheduler.databinding.ActivityMainBinding
import com.saklayen.scheduler.utils.findNavControllerByFragmentContainerView
import com.saklayen.scheduler.utils.setupNavController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val mNavController by lazy { findNavControllerByFragmentContainerView(R.id.nav_host) }
    companion object{
        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.homeFragment,
            R.id.scheduleListFragment,
            R.id.completedSchedulesFragment
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bottomNav.setupNavController(mNavController)
        mNavController.addOnDestinationChangedListener { _, destination, _ ->
            val selectId = destination.id
            val isTopLevelDestination = TOP_LEVEL_DESTINATIONS.contains(selectId)
            binding.bottomNav.visibility =
                if (isTopLevelDestination) View.VISIBLE else View.GONE
        }

        binding.bottomNav.setupWithNavController(mNavController)

    }

    override fun registerToolbarWithNavigation(toolbar: MaterialToolbar) {
        Timber.d("registerToolbarWithNavigation")
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        toolbar.setupWithNavController(mNavController, appBarConfiguration)
    }
}