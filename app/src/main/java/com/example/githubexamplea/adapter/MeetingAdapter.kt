package com.example.githubexamplea.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubexamplea.R
import com.example.githubexamplea.model.MeetingItem

class MeetingAdapter(private val meetingList: List<MeetingItem>) :
    RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>() {

    // 찜하기 상태 저장을 위한 리스트
    private val favoriteStatus = MutableList(meetingList.size) { false }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meeting, parent, false)
        return MeetingViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meeting = meetingList[position]
        holder.meetingImage.setImageResource(meeting.imageResId)
        holder.meetingTitle.text = meeting.title
        holder.meetingDescription.text = meeting.description

        // 초기 상태 설정
        holder.heartIcon.setImageResource(
            if (favoriteStatus[position]) R.drawable.ic_like_red
            else R.drawable.ic_like_white
        )

        // 찜하기 아이콘 클릭 이벤트 처리
        holder.heartIcon.setOnClickListener {
            favoriteStatus[position] = !favoriteStatus[position]
            holder.heartIcon.setImageResource(
                if (favoriteStatus[position]) R.drawable.ic_like_red
                else R.drawable.ic_like_white
            )
        }
    }

    override fun getItemCount() = meetingList.size

    class MeetingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val meetingImage: ImageView = itemView.findViewById(R.id.meetingImage)
        val heartIcon: ImageView = itemView.findViewById(R.id.heartIcon)
        val meetingTitle: TextView = itemView.findViewById(R.id.meetingTitle)
        val meetingDescription: TextView = itemView.findViewById(R.id.meetingDescription)
    }
}
