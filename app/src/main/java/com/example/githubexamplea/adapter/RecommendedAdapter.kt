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

            heartIcon.setOnClickListener {
                toggleFavoriteStatus(item)
            }
        }

        private fun toggleFavoriteStatus(item: RecommendedItem) {
            val dbHelper = DatabaseHelper(context)
            val db = dbHelper.writableDatabase
            val userId = SharedPreferencesHelper.getUserId(context) ?: return

            item.isFavorite = !item.isFavorite

            if (item.isFavorite) {
                db.execSQL("INSERT OR IGNORE INTO tb_like (id, club_name) VALUES (?, ?)", arrayOf(userId, item.title))
            } else {
                db.execSQL("DELETE FROM tb_like WHERE id = ? AND club_name = ?", arrayOf(userId, item.title))
            }

            updateFavoriteIcon(item.isFavorite)
        }

        private fun updateFavoriteIcon(isFavorite: Boolean) {
            heartIcon.setImageResource(if (isFavorite) R.drawable.ic_like_red else R.drawable.ic_like_white)
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
