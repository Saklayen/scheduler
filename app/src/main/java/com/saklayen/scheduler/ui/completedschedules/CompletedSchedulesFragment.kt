package com.saklayen.scheduler.ui.completedschedules

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.saklayen.scheduler.R
import com.saklayen.scheduler.base.ui.BaseFragment
import com.saklayen.scheduler.databinding.FragmentCompletedSchedulesBinding
import com.saklayen.scheduler.ui.schedulelist.ScheduleListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletedSchedulesFragment :
    BaseFragment<FragmentCompletedSchedulesBinding>(R.layout.fragment_completed_schedules) {
    private val viewModel: ScheduleListViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
    }

}