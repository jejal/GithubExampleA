package com.example.githubexamplea.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubexamplea.R
import com.example.githubexamplea.model.HostItem
import java.io.File
import com.bumptech.glide.Glide

class HostAdapter : ListAdapter<HostItem, HostAdapter.ViewHolder>(HostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_host_profile, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hostItem = getItem(position)
        Log.d("RecyclerViewDebug", "onBindViewHolder 호출됨 - position: $position, name: ${hostItem.name}")
        holder.bind(hostItem)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val hostImage: ImageView = view.findViewById(R.id.hostImage)
        private val hostName: TextView = view.findViewById(R.id.hostName)
        private val hostIntro: TextView = view.findViewById(R.id.hostIntro)
        private val hostDescription: TextView = view.findViewById(R.id.hostDescription)
        private val hostInfo1: TextView = view.findViewById(R.id.hostInfo1)
        private val hostInfo2: TextView = view.findViewById(R.id.hostInfo2)
        private val hostInfo3: TextView = view.findViewById(R.id.hostInfo3)

        fun bind(item: HostItem) {
            hostName.text = item.name
            // `\n` 적용을 위해 setText() 사용
            hostIntro.text = item.intro.replace("\\n", "\n")
            hostDescription.text = item.description.replace("\\n", "\n")

            val info1 = item.infos.getOrNull(0) ?: ""
            val info2 = item.infos.getOrNull(1) ?: ""
            val info3 = item.infos.getOrNull(2) ?: ""

            // 값이 있을 때만 `•` 출력하도록 변경
            hostInfo1.text = if (info1.isNotEmpty()) "• $info1" else ""
            hostInfo2.text = if (info2.isNotEmpty()) "• $info2" else ""
            hostInfo3.text = if (info3.isNotEmpty()) "• $info3" else ""

            Log.d("GlideDebug", "로드할 이미지 경로: ${item.image}") // Glide에서 받는 경로 확인

            if (!item.image.isNullOrEmpty()) {
                val file = File(item.image)
                if (file.exists()) { // Glide에서 사용할 수 있는지 확인
                    Log.d("GlideDebug", "파일 존재 확인됨: ${item.image}")

                    Glide.with(itemView.context)
                        .load(Uri.fromFile(file)) // `Uri.fromFile()`을 사용하여 Glide에서 읽도록 설정
                        .placeholder(R.drawable.img_banner_1)
                        .error(R.drawable.sorbet)
                        .into(hostImage)

                } else {
                    Log.e("GlideError", "파일이 존재하지 않음: ${item.image}") // Glide가 파일을 못 찾을 때 로그 추가
                    hostImage.setImageResource(R.drawable.img_banner_1)
                }
            } else {
                Log.e("GlideError", "SQLite에서 가져온 imagePath가 NULL 또는 EMPTY") // SQLite에서 데이터가 비어 있는 경우 로그 추가
                hostImage.setImageResource(R.drawable.img_banner_1)
            }

        }
    }
}

class HostDiffCallback : DiffUtil.ItemCallback<HostItem>() {
    override fun areItemsTheSame(oldItem: HostItem, newItem: HostItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: HostItem, newItem: HostItem): Boolean {
        return oldItem == newItem
    }
}
