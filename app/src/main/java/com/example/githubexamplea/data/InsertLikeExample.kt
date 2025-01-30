package com.example.githubexamplea.data

import android.content.Context
import com.example.githubexamplea.dao.LikeDao
import com.example.githubexamplea.database.DatabaseHelper

class InsertLikeExample(private val context: Context) {

    fun insertLikes() {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val likeDao = LikeDao(db)

        // 예시 좋아요 데이터: (사용자 ID, 클럽 이름)
        val likesToInsert = listOf(
            arrayOf("miso", "Kicks & Dreams"),
            arrayOf("lsj", "Hit & Hustle"),
            arrayOf("lmr", "Breathe"),
            arrayOf("pnr", "Kicks & Dreams")
        )

        db.beginTransaction()
        try {
            for (likeData in likesToInsert) {
                likeDao.insertLike(
                    userId = likeData[0],
                    clubName = likeData[1]
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }
}
