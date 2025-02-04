package com.example.githubexamplea.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubexamplea.R
import com.example.githubexamplea.model.ActivityItem
import java.io.File

class ActivityAdapter(
    private val itemWidth: Int,
    private val onItemClick: (ActivityItem) -> Unit  // 클릭 이벤트 추가
) : ListAdapter<ActivityItem, ActivityAdapter.ViewHolder>(ActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity_card, parent, false)
        view.layoutParams.width = itemWidth
        return ViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(view: View, private val onItemClick: (ActivityItem) -> Unit) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.activityImage)
        private val titleText: TextView = view.findViewById(R.id.titleText)
        private val descriptionText: TextView = view.findViewById(R.id.descriptionText)
        private val ratingText: TextView = view.findViewById(R.id.ratingText)

        fun bind(item: ActivityItem) {
            titleText.text = item.title
            descriptionText.text = item.description
            ratingText.text = item.rating

            // 이미지 파일 경로 확인 후 Glide로 로드
            if (!item.image.isNullOrEmpty()) {
                val file = File(item.image)
                if (file.exists()) {
                    Log.d("GlideDebug", "파일 존재 확인됨: ${item.image}")

                    Glide.with(itemView.context)
                        .load(Uri.fromFile(file)) // 내부 저장소 파일을 로드
                        .placeholder(R.drawable.img_banner_1) // 로딩 중 표시할 이미지
                        .error(R.drawable.sorbet) // 오류 발생 시 기본 이미지
                        .into(imageView)

                } else {
                    Log.e("GlideError", "파일이 존재하지 않음: ${item.image}")
                    imageView.setImageResource(R.drawable.img_banner_1)
                }
            } else {
                Log.e("GlideError", "SQLite에서 가져온 imagePath가 NULL 또는 EMPTY")
                imageView.setImageResource(R.drawable.img_banner_1)
            }

            // 클릭 이벤트 추가
            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}

// RecyclerView의 데이터 변경을 효율적으로 처리하기 위한 DiffUtil 콜백
class ActivityDiffCallback : DiffUtil.ItemCallback<ActivityItem>() {
    override fun areItemsTheSame(oldItem: ActivityItem, newItem: ActivityItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: ActivityItem, newItem: ActivityItem): Boolean {
        return oldItem == newItem
    }
}
