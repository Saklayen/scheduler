package com.saklayen.scheduler.ui.schedulelist

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.saklayen.scheduler.database.model.Schedule
import com.saklayen.scheduler.databinding.ScheduledAppItemBinding
import com.saklayen.scheduler.utils.addDividerItemDecorator
import com.saklayen.scheduler.utils.clearDecorations
import com.saklayen.scheduler.utils.layoutInflater
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ScheduledApplicationListAdapter(val viewModel: ScheduleListViewModel) :
    ListAdapter<Schedule, ApplicationListViewHolder>(
        DiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationListViewHolder {
        return ApplicationListViewHolder(
            ScheduledAppItemBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ApplicationListViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }
}


@ExperimentalCoroutinesApi
class ApplicationListViewHolder(val binding: ScheduledAppItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: ScheduleListViewModel, data: Schedule) {
        binding.viewModel = viewModel
        binding.item = data
        binding.executePendingBindings()
    }
}

private class DiffCallback : DiffUtil.ItemCallback<Schedule>() {
    override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule) =
        oldItem.packageName == newItem.packageName

    override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule) =
        oldItem == newItem
}

@ExperimentalCoroutinesApi
@BindingAdapter(value = ["scheduleListViewModel", "applicationList"], requireAll = true)
fun RecyclerView.bindScheduledApplicationListAdapter(
    viewModel: ScheduleListViewModel,
    data: List<Schedule>?
) {
    if (adapter == null) adapter = ScheduledApplicationListAdapter(viewModel)
    val value = data ?: emptyList()
    val appListAdapter = adapter as? ScheduledApplicationListAdapter
    appListAdapter?.submitList(value)
    clearDecorations()
    if (value.isNotEmpty()) addDividerItemDecorator(RecyclerView.VERTICAL)
}
