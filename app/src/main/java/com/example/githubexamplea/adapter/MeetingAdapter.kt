package com.example.githubexamplea.adapter

import android.content.Context
import android.database.sqlite.SQLiteDatabase
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
import com.example.githubexamplea.utils.SharedPreferencesHelper
import java.io.File

class MeetingAdapter(
    private val context: Context,
    private val meetingList: MutableList<MeetingItem>,
    private val onItemRemoved: () -> Unit
) : RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meeting, parent, false)
        return MeetingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meeting = meetingList[position]

        // 이미지 로드
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
        holder.meetingDate.text = "📅 ${meeting.date}"
        holder.meetingTime.text = "⏰ ${meeting.time.replace("\n", " ")}"

        // 아이콘을 "신청 취소"로 변경
        holder.cancelIcon.setImageResource(R.drawable.ic_cancel)

        // ❌ 신청 취소 버튼 (ic_cancel 아이콘)
        holder.cancelIcon.setOnClickListener {
            removeItem(position, meeting)
        }
    }

    override fun getItemCount() = meetingList.size

    class MeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val meetingImage: ImageView = itemView.findViewById(R.id.meetingImage)
        val cancelIcon: ImageView = itemView.findViewById(R.id.heartIcon) // 찜하기 대신 신청 취소
        val meetingTitle: TextView = itemView.findViewById(R.id.meetingTitle)
        val meetingDescription: TextView = itemView.findViewById(R.id.meetingDescription)
        val meetingDate: TextView = itemView.findViewById(R.id.meetingDate)
        val meetingTime: TextView = itemView.findViewById(R.id.meetingTime)
    }

    // 🔹 아이템 삭제 함수 (DB와 UI 반영)
    private fun removeItem(position: Int, meeting: MeetingItem) {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val userId = SharedPreferencesHelper.getUserId(context)

        db.execSQL(
            "DELETE FROM tb_application WHERE id = ? AND club_name = ? AND date = ? AND time = ?",
            arrayOf(userId, meeting.title, meeting.date, meeting.time)
        )

        meetingList.removeAt(position) // 리스트에서 제거
        notifyItemRemoved(position)   // 해당 위치의 UI 업데이트
        notifyItemRangeChanged(position, meetingList.size) // 인덱스 정리

        onItemRemoved()  // 신청 개수 업데이트 콜백 실행
    }
}
