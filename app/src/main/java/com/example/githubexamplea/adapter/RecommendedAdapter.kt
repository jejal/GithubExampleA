package com.example.githubexamplea.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubexamplea.R
import com.example.githubexamplea.database.DatabaseHelper
import com.example.githubexamplea.model.RecommendedItem
import com.example.githubexamplea.utils.SharedPreferencesHelper
import java.io.File

class RecommendedAdapter(private val context: Context) :
    ListAdapter<RecommendedItem, RecommendedAdapter.ViewHolder>(RecommendedDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recommendedItem = getItem(position)
        holder.bind(recommendedItem)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.recommendedImage)
        private val titleText: TextView = view.findViewById(R.id.titleText)
        private val subtitleText: TextView = view.findViewById(R.id.subtitleText)
        private val heartIcon: ImageView = view.findViewById(R.id.heartIcon)

        fun bind(item: RecommendedItem) {
            titleText.text = item.title
            subtitleText.text = item.subtitle

            // 이미지가 있는 경우 Glide로 로드, 없으면 기본 이미지 설정
            if (!item.image.isNullOrEmpty()) {
                val file = File(item.image)
                if (file.exists()) {
                    Glide.with(itemView.context)
                        .load(Uri.fromFile(file))
                        .placeholder(R.drawable.img_banner_1)
                        .error(R.drawable.sorbet)
                        .into(imageView)
                } else {
                    imageView.setImageResource(R.drawable.img_banner_1)
                }
            } else {
                imageView.setImageResource(R.drawable.img_banner_1)
            }

            updateFavoriteIcon(item.isFavorite)

            // 찜하기 버튼 클릭 시 상태 변경
            heartIcon.setOnClickListener {
                toggleFavoriteStatus(item)
            }
        }

        private fun toggleFavoriteStatus(item: RecommendedItem) {
            val dbHelper = DatabaseHelper(context)
            val db = dbHelper.writableDatabase
            val userId = SharedPreferencesHelper.getUserId(context) ?: return

            // 즐겨찾기 상태 토글
            item.isFavorite = !item.isFavorite

            if (item.isFavorite) {
                // 즐겨찾기 추가 (중복 방지)
                db.execSQL("INSERT OR IGNORE INTO tb_like (id, club_name) VALUES (?, ?)", arrayOf(userId, item.title))
            } else {
                // 즐겨찾기 제거
                db.execSQL("DELETE FROM tb_like WHERE id = ? AND club_name = ?", arrayOf(userId, item.title))
            }

            updateFavoriteIcon(item.isFavorite)
        }

        // 즐겨찾기 상태에 따라 하트 아이콘 변경
        private fun updateFavoriteIcon(isFavorite: Boolean) {
            heartIcon.setImageResource(if (isFavorite) R.drawable.ic_like_red else R.drawable.ic_like_white)
        }
    }
}

// RecyclerView의 데이터 변경을 효율적으로 처리하기 위한 DiffUtil 콜백
class RecommendedDiffCallback : DiffUtil.ItemCallback<RecommendedItem>() {
    override fun areItemsTheSame(oldItem: RecommendedItem, newItem: RecommendedItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: RecommendedItem, newItem: RecommendedItem): Boolean {
        return oldItem == newItem
    }
}
