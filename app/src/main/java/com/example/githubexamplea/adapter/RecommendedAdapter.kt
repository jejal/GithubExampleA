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

    // 찜 상태 저장 리스트
    private val favoriteStatus = mutableListOf<Boolean>()

    override fun submitList(list: List<RecommendedItem>?) {
        super.submitList(list)
        favoriteStatus.clear()
        list?.let { favoriteStatus.addAll(List(it.size) { false }) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.recommendedImage)
        private val titleText: TextView = view.findViewById(R.id.titleText)
        private val subtitleText: TextView = view.findViewById(R.id.subtitleText)
        private val heartIcon: ImageView = view.findViewById(R.id.heartIcon)

        fun bind(item: RecommendedItem, position: Int) {
            imageView.setImageResource(item.image)
            titleText.text = item.title
            subtitleText.text = item.subtitle

            // 찜 여부에 따른 아이콘 설정
            heartIcon.setImageResource(
                if (favoriteStatus[position]) R.drawable.ic_like_red
                else R.drawable.ic_like_white
            )

            // 찜하기 버튼 클릭 이벤트 설정
            heartIcon.setOnClickListener {
                favoriteStatus[position] = !favoriteStatus[position]
                heartIcon.setImageResource(
                    if (favoriteStatus[position]) R.drawable.ic_like_red
                    else R.drawable.ic_like_white
                )
            }
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
