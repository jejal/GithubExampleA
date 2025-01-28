package com.example.githubexamplea.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubexamplea.R
import com.example.githubexamplea.model.MeetingItem

class FavoriteAdapter(private val favoriteList: List<MeetingItem>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val favoriteStatus = MutableList(favoriteList.size) { true }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meeting, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val meeting = favoriteList[position]
        holder.meetingImage.setImageResource(meeting.imageResId)
        holder.meetingTitle.text = meeting.title
        holder.meetingDescription.text = meeting.description

        holder.heartIcon.setImageResource(
            if (favoriteStatus[position]) R.drawable.ic_like_red
            else R.drawable.ic_like_white
        )

        holder.heartIcon.setOnClickListener {
            favoriteStatus[position] = !favoriteStatus[position]
            holder.heartIcon.setImageResource(
                if (favoriteStatus[position]) R.drawable.ic_like_red
                else R.drawable.ic_like_white
            )
        }
    }

    override fun getItemCount() = favoriteList.size

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val meetingImage: ImageView = itemView.findViewById(R.id.meetingImage)
        val heartIcon: ImageView = itemView.findViewById(R.id.heartIcon)
        val meetingTitle: TextView = itemView.findViewById(R.id.meetingTitle)
        val meetingDescription: TextView = itemView.findViewById(R.id.meetingDescription)
    }
}
