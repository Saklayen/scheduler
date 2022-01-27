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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
                    viewModel.applicationList.value = getInstalledApps(false)

                }
            }
        }
    }

    suspend fun getInstalledApps(getSysPackages: Boolean): ArrayList<App> {
        val res = ArrayList<App>()
        val packs = requireActivity().packageManager.getInstalledPackages(0)
        for (i in 0 until packs.size) {
            val p: PackageInfo = packs[i]
            if ((!getSysPackages) && (p.versionName == null)) {
                continue
            }
            val appInfo = App(
                i,
                p.applicationInfo.loadLabel(requireActivity().packageManager).toString(),
                p.packageName
                //p.applicationInfo.loadIcon(requireActivity().packageManager)
            )

            res.add(appInfo)
        }
        return res

    }

}