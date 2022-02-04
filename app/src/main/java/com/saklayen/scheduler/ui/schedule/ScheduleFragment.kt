package com.saklayen.scheduler.ui.schedule

import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.saklayen.scheduler.R
import com.saklayen.scheduler.base.ui.BaseFragment
import com.saklayen.scheduler.databinding.FragmentScheduleBinding
import com.saklayen.scheduler.utils.launchAndRepeatWithViewLifecycle
import com.saklayen.scheduler.utils.positiveButton
import com.saklayen.scheduler.utils.showDialog
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

        viewModel.appName.value = args.appName
        viewModel.packageName.value = args.packageName

        viewModel.alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

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
            launch {
                viewModel.message.collect {
                    showDialog {
                        setMessage(it)
                        positiveButton(getString(R.string.ok))
                    }
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