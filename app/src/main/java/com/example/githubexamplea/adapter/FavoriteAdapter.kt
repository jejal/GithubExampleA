package com.example.githubexamplea.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubexamplea.R
import com.example.githubexamplea.database.DatabaseHelper
import com.example.githubexamplea.model.MeetingItem
import java.io.File

class FavoriteAdapter(private val favoriteList: MutableList<MeetingItem>, private val dbHelper: DatabaseHelper, private val userId: String, private val onItemRemoved: () -> Unit) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meeting, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val meeting = favoriteList[position]

        // Glide를 사용해 이미지 로드
        if (meeting.imagePath.isNotEmpty()) {
            val file = File(meeting.imagePath)
            if (file.exists()) {
                Glide.with(holder.itemView.context)
                    .load(Uri.fromFile(file))
                    .placeholder(R.drawable.img_banner_1)
                    .error(R.drawable.sorbet)
                    .into(holder.meetingImage)
            } else {
                holder.meetingImage.setImageResource(R.drawable.img_banner_1)
            }
        } else {
            holder.meetingImage.setImageResource(R.drawable.img_banner_1)
        }

        holder.meetingTitle.text = meeting.title
        holder.meetingDescription.text = meeting.description

        // 초기 상태는 찜한 상태 (빨간 하트)
        holder.heartIcon.setImageResource(R.drawable.ic_like_red)

        // 하트 아이콘 클릭 시 찜 해제
        holder.heartIcon.setOnClickListener {
            removeFavorite(meeting, position)
        }
    }

    override fun getItemCount() = favoriteList.size

    private fun removeFavorite(meeting: MeetingItem, position: Int) {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM tb_like WHERE id = ? AND club_name = ?", arrayOf(userId, meeting.title))

        // 목록에서 제거 후 UI 업데이트
        favoriteList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, favoriteList.size)

        onItemRemoved() // 찜 개수 업데이트 콜백 실행
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val meetingImage: ImageView = itemView.findViewById(R.id.meetingImage)
        val heartIcon: ImageView = itemView.findViewById(R.id.heartIcon)
        val meetingTitle: TextView = itemView.findViewById(R.id.meetingTitle)
        val meetingDescription: TextView = itemView.findViewById(R.id.meetingDescription)
    }
}
