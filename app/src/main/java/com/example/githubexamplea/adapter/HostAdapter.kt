package com.example.githubexamplea.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubexamplea.R
import com.example.githubexamplea.model.HostItem

class HostAdapter : ListAdapter<HostItem, HostAdapter.ViewHolder>(HostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_host_profile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val hostImage: ImageView = view.findViewById(R.id.hostImage)
        private val hostName: TextView = view.findViewById(R.id.hostName)
        private val hostIntro: TextView = view.findViewById(R.id.hostIntro)
        private val hostDescription: TextView = view.findViewById(R.id.hostDescription)
        private val hostInfo1: TextView = view.findViewById(R.id.hostInfo1)
        private val hostInfo2: TextView = view.findViewById(R.id.hostInfo2)
        private val hostInfo3: TextView = view.findViewById(R.id.hostInfo3)

        fun bind(item: HostItem) {
            hostImage.setImageResource(item.image)
            hostName.text = item.name
            hostIntro.text = item.intro
            hostDescription.text = item.description
            hostInfo1.text = "• ${item.infos[0]}"
            hostInfo2.text = "• ${item.infos[1]}"
            hostInfo3.text = "• ${item.infos[2]}"
        }
    }
}

class HostDiffCallback : DiffUtil.ItemCallback<HostItem>() {
    override fun areItemsTheSame(oldItem: HostItem, newItem: HostItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: HostItem, newItem: HostItem): Boolean {
        return oldItem == newItem
    }
}
