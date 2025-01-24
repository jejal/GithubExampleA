package com.example.githubexamplea.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubexamplea.R
import com.example.githubexamplea.model.ActivityItem

class ActivityAdapter(
    private val itemWidth: Int,
    private val onItemClick: (ActivityItem) -> Unit  // 클릭 이벤트 추가
) : ListAdapter<ActivityItem, ActivityAdapter.ViewHolder>(ActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity_card, parent, false)
        view.layoutParams.width = itemWidth
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.activityImage)
        private val titleText: TextView = view.findViewById(R.id.titleText)
        private val descriptionText: TextView = view.findViewById(R.id.descriptionText)
        private val ratingText: TextView = view.findViewById(R.id.ratingText)

        fun bind(item: ActivityItem, onItemClick: (ActivityItem) -> Unit) {
            imageView.setImageResource(item.image)
            titleText.text = item.title
            descriptionText.text = item.description
            ratingText.text = item.rating

            // 클릭 이벤트 추가
            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}

class ActivityDiffCallback : DiffUtil.ItemCallback<ActivityItem>() {
    override fun areItemsTheSame(oldItem: ActivityItem, newItem: ActivityItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: ActivityItem, newItem: ActivityItem): Boolean {
        return oldItem == newItem
    }
}
