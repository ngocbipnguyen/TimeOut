package com.bachnn.timeout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bachnn.timeout.data.model.AppInfo
import com.bachnn.timeout.databinding.AppInfoItemBinding

class HomeAdapter(private val clickListener: (AppInfo) -> Unit) :
    ListAdapter<AppInfo, HomeAdapter.ViewHolder>(AppInfoDiffCallback()) {
    class AppInfoDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean =
            oldItem.packageName == newItem.packageName

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean =
            oldItem == newItem
    }

    class ViewHolder(private val binding: AppInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(appInfo: AppInfo, clickListener: (AppInfo) -> Unit) {
            val drawable =
                binding.root.context.packageManager.getApplicationIcon(appInfo.packageName)
            binding.appInfoIcon.setImageDrawable(drawable)
            binding.nameApp.text = appInfo.label
            binding.packageApp.text = appInfo.packageName
            binding.timeUsed.text = "0"
            binding.appFrame.setOnClickListener {
                clickListener(appInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AppInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

}