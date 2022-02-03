package com.saklayen.scheduler.ui.home

import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.saklayen.scheduler.R
import com.saklayen.scheduler.base.ui.BaseFragment
import com.saklayen.scheduler.databinding.FragmentHomeBinding
import com.saklayen.scheduler.model.App
import com.saklayen.scheduler.utils.launchAndRepeatWithViewLifecycle
import com.saklayen.scheduler.utils.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.navigationActions.collect {
                    navigate(
                        HomeFragmentDirections.navigateToScheduleFragment(
                            it.appIndex,
                            it.appName,
                            it.packageName
                        )
                    )
                }
            }

            launch {
                viewModel.getInstalledApps.collect {
                    viewModel.fetchInstalledApplicationList.tryEmit(requireContext())

                }
            }
        }
    }

}