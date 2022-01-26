package com.saklayen.scheduler.uii.schedule

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.saklayen.scheduler.R
import com.saklayen.scheduler.base.ui.BaseFragment
import com.saklayen.scheduler.databinding.FragmentScheduleBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

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

        Handler(Looper.getMainLooper()).postDelayed({
            /* Create an Intent that will start the Menu-Activity. */
            val mainIntent = requireActivity().packageManager.getLaunchIntentForPackage(args.packageName)
            startActivity(mainIntent)
        }, 3000)
    }
}