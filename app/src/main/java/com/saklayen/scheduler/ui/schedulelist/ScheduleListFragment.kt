package com.saklayen.scheduler.ui.schedulelist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.saklayen.scheduler.R
import com.saklayen.scheduler.base.ui.BaseFragment
import com.saklayen.scheduler.databinding.FragmentScheduleListBinding
import com.saklayen.scheduler.utils.launchAndRepeatWithViewLifecycle
import com.saklayen.scheduler.utils.navigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleListFragment :
    BaseFragment<FragmentScheduleListBinding>(R.layout.fragment_schedule_list) {
    override val haveToolbar = true
    override val resToolbarId = R.id.toolbar
    private val viewModel: ScheduleListViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.navigationActions.collect {
                    navigate(
                        ScheduleListFragmentDirections.navigateToUpdateScheduleFragment(it)
                    )
                }
            }
        }
    }
}