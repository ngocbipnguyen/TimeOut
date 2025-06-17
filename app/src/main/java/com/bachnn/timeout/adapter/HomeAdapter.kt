package com.bachnn.timeout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bachnn.timeout.data.model.AppInfo
import com.bachnn.timeout.databinding.AppInfoItemBinding

class HomeAdapter(private val clickListener: (AppInfo) -> Unit, private val clickBoxActive:(AppInfo, Boolean) -> Unit) :
    ListAdapter<AppInfo, HomeAdapter.ViewHolder>(AppInfoDiffCallback()) {
    class AppInfoDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean =
            oldItem.packageName == newItem.packageName

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean =
            oldItem == newItem
    }

    class ViewHolder(private val binding: AppInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(appInfo: AppInfo, clickListener: (AppInfo) -> Unit, clickBoxActive: (AppInfo, Boolean) -> Unit) {
            val drawable =
                binding.root.context.packageManager.getApplicationIcon(appInfo.packageName)
            binding.appInfoIcon.setImageDrawable(drawable)
            binding.nameApp.text = appInfo.label
            binding.packageApp.text = appInfo.packageName
            binding.timeUsed.text = appInfo.formatTimestamp()
            // Remove previous listener to avoid multiple callbacks
            binding.activeBox.isChecked = appInfo.active
            binding.appFrame.setOnClickListener {
                clickListener(appInfo)
            }

            binding.activeBox.setOnCheckedChangeListener { _, isChecked ->
                clickBoxActive(appInfo, isChecked)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AppInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, clickBoxActive)
    }

    fun updateTimestamp(packageName: String, newTimestamp: Long) {
        val currentList = currentList.toMutableList()
        val index = currentList.indexOfFirst { it.packageName == packageName }
        if (index != -1) {
            currentList[index] = currentList[index].copy(timestamp = newTimestamp)
            submitList(currentList)
        }
    }

    fun updateItem(updatedAppInfo: AppInfo) {
        val currentList = currentList.toMutableList()
        val index = currentList.indexOfFirst { it.packageName == updatedAppInfo.packageName }
        if (index != -1) {
            currentList[index] = updatedAppInfo
            submitList(currentList)
        }
    }

    fun updateItems(updatedAppInfos: List<AppInfo>) {
        val currentList = currentList.toMutableList()
        updatedAppInfos.forEach { updatedAppInfo ->
            val index = currentList.indexOfFirst { it.packageName == updatedAppInfo.packageName }
            if (index != -1) {
                currentList[index] = updatedAppInfo
            }
        }
        submitList(currentList)
    }
}