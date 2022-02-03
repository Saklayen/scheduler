package com.saklayen.scheduler.ui.updateschedule

import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.saklayen.scheduler.R
import com.saklayen.scheduler.base.ui.BaseFragment
import com.saklayen.scheduler.databinding.FragmentUpdateScheduleBinding
import com.saklayen.scheduler.utils.launchAndRepeatWithViewLifecycle
import com.saklayen.scheduler.utils.navigateUp
import com.saklayen.scheduler.utils.positiveButton
import com.saklayen.scheduler.utils.showDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class UpdateScheduleFragment : BaseFragment<FragmentUpdateScheduleBinding>(R.layout.fragment_update_schedule) {
    private val viewModel: UpdateScheduleViewModel by viewModels()

    override val haveToolbar = true
    override val resToolbarId = R.id.toolbar
    private val args: UpdateScheduleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.rowId.value = args.schedule.rowid
        viewModel.requestCode.value = Integer.parseInt(args.schedule.requestCode)
        viewModel.appName.value = args.schedule.appName
        viewModel.packageName.value = args.schedule.packageName
        viewModel.schedule.value = args.schedule.time

        viewModel.alarmManager =
            requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        launchAndRepeatWithViewLifecycle {
            launch {
                viewModel.showTimePicker.collect {
                    showTimePicker()
                }
            }
            launch {
                viewModel.updateSchedule.collect {
                    viewModel.updateSchedule(requireContext())
                }
            }
            launch {
                viewModel.cancelSchedule.collect {
                    viewModel.cancelSchedule(requireContext())
                }
            }
            launch {
                viewModel.message.collect {
                    showDialog {
                        setMessage(it)
                        positiveButton(getString(R.string.ok)){
                            navigateUp()
                        }
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