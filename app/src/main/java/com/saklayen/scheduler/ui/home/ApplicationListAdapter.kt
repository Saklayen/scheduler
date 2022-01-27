package com.saklayen.scheduler.ui.home

import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.saklayen.scheduler.databinding.AppItemBinding
import com.saklayen.scheduler.model.App
import com.saklayen.scheduler.utils.addDividerItemDecorator
import com.saklayen.scheduler.utils.clearDecorations
import com.saklayen.scheduler.utils.layoutInflater
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ApplicationListAdapter(val viewModel: HomeViewModel) :
    ListAdapter<App, ApplicationListViewHolder>(
        DiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationListViewHolder {
        return ApplicationListViewHolder(
            AppItemBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ApplicationListViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }
}


@ExperimentalCoroutinesApi
class ApplicationListViewHolder(val binding: AppItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: HomeViewModel, data: App) {
        binding.viewModel = viewModel
        binding.item = data
        binding.executePendingBindings()
    }
}

private class DiffCallback : DiffUtil.ItemCallback<App>() {
    override fun areItemsTheSame(oldItem: App, newItem: App) =
        oldItem.packageName == newItem.packageName

    override fun areContentsTheSame(oldItem: App, newItem: App) =
        oldItem == newItem
}

@ExperimentalCoroutinesApi
@BindingAdapter(value = ["homeViewModel", "applicationList"], requireAll = true)
fun RecyclerView.bindApplicationListAdapter(viewModel: HomeViewModel, data: List<App>?) {
    if (adapter == null) adapter = ApplicationListAdapter(viewModel)
    val value = data ?: emptyList()
    val appListAdapter = adapter as? ApplicationListAdapter
    appListAdapter?.submitList(value)
    clearDecorations()
    if (value.isNotEmpty()) addDividerItemDecorator(RecyclerView.VERTICAL)
}
