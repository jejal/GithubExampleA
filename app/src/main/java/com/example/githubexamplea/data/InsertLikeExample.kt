package com.example.githubexamplea.data

import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.dao.LikeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InsertLikeExample(private val db: SQLiteDatabase) {

    suspend fun insertLikes() {
        val likeDao = LikeDao(db)

        // 초기 좋아요 데이터: (사용자 ID, 클럽 이름)
        val likesToInsert = listOf(
            arrayOf("lee", "Kicks & Dreams"),
            arrayOf("lee", "Hit & Hustle"),
            arrayOf("lee", "High Runner"),
            arrayOf("lee", "Climb City"),
            arrayOf("lee", "Swing"),
            arrayOf("lee", "Dolphin"),
            arrayOf("lee", "Breathe"),
            arrayOf("miso", "Kicks & Dreams"),
            arrayOf("miso", "Run & Run"),
            arrayOf("miso", "High Runner"),
            arrayOf("miso", "Ping & Pong"),
            arrayOf("miso", "Swing"),
            arrayOf("miso", "Basket Team"),
            arrayOf("jun", "Kicks & Dreams"),
            arrayOf("jun", "Hit & Hustle"),
            arrayOf("jun", "Run & Run"),
            arrayOf("future", "Kicks & Dreams"),
            arrayOf("future", "Hit & Hustle"),
            arrayOf("future", "Run & Run"),
            arrayOf("future", "High Runner"),
            arrayOf("future", "Climb City"),
            arrayOf("future", "Ping & Pong"),
            arrayOf("future", "Swing"),
            arrayOf("future", "Breathe"),
            arrayOf("nare", "Kicks & Dreams"),
            arrayOf("nare", "Hit & Hustle"),
            arrayOf("nare", "Run & Run"),
            arrayOf("nare", "High Runner"),
            arrayOf("nare", "Climb City"),
            arrayOf("uuu", "Kicks & Dreams"),
            arrayOf("uuu", "Hit & Hustle"),
            arrayOf("uuu", "Ping & Pong"),
            arrayOf("uuu", "Basket Team")
        )

        withContext(Dispatchers.IO) {
            for (likeData in likesToInsert) {
                likeDao.insertLike(
                    userId = likeData[0],
                    clubName = likeData[1]
                )
            }
        }
    }
}
