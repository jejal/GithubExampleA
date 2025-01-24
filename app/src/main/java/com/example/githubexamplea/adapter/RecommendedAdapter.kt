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
import com.example.githubexamplea.model.RecommendedItem

class RecommendedAdapter : ListAdapter<RecommendedItem, RecommendedAdapter.ViewHolder>(RecommendedDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.recommendedImage)
        private val titleText: TextView = view.findViewById(R.id.titleText)
        private val subtitleText: TextView = view.findViewById(R.id.subtitleText)

        fun bind(item: RecommendedItem) {
            imageView.setImageResource(item.image)
            titleText.text = item.title
            subtitleText.text = item.subtitle
        }
    }
}

class RecommendedDiffCallback : DiffUtil.ItemCallback<RecommendedItem>() {
    override fun areItemsTheSame(oldItem: RecommendedItem, newItem: RecommendedItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: RecommendedItem, newItem: RecommendedItem): Boolean {
        return oldItem == newItem
    }
}
