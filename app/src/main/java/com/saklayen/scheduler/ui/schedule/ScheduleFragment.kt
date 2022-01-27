package com.saklayen.scheduler.ui.schedule

import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.saklayen.scheduler.R
import com.saklayen.scheduler.base.ui.BaseFragment
import com.saklayen.scheduler.databinding.FragmentScheduleBinding
import com.saklayen.scheduler.utils.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>(R.layout.fragment_schedule) {
    private val viewModel: ScheduleViewModel by viewModels()

    override val haveToolbar = true
    override val resToolbarId = R.id.toolbar
    private val args: ScheduleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.appIndex.value = args.appIndex
        viewModel.appName.value = args.appName
        viewModel.packageName.value = args.packageName

        viewModel.alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Handler(Looper.getMainLooper()).postDelayed({
            /*val mainIntent =
                requireActivity().packageManager.getLaunchIntentForPackage(args.packageName)
            startActivity(mainIntent)*/
        }, 3000)

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.showTimePicker.collect {
                    showTimePicker()
                }
            }
            launch {
                viewModel.setSchedule.collect {
                    viewModel.setSchedule(requireContext())
                }
            }
        }
    }

    private fun showTimePicker() {
        TimePickerDialog(
            requireContext(),
            viewModel.timeSetListener,
            viewModel.calendar.get(Calendar.HOUR_OF_DAY),
            viewModel.calendar.get(Calendar.MINUTE),
            false
        ).show()
    }
}