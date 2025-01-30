package com.example.githubexamplea.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.database.DatabaseContract

class ReviewDao(private val db: SQLiteDatabase) {

    // 리뷰 추가
    fun insertReview(
        userId: String,
        clubName: String,
        star: Float,
        reviewText: String,
        time: String,
        date: String
    ): Long {
        val values = ContentValues().apply {
            put(DatabaseContract.ReviewTable.COLUMN_ID, userId)
            put(DatabaseContract.ReviewTable.COLUMN_CLUB_NAME, clubName)
            put(DatabaseContract.ReviewTable.COLUMN_STAR, star)
            put(DatabaseContract.ReviewTable.COLUMN_REVIEW, reviewText)
            put(DatabaseContract.ReviewTable.COLUMN_TIME, time)
            put(DatabaseContract.ReviewTable.COLUMN_DATE, date)
        }
        return db.insert(DatabaseContract.ReviewTable.TABLE_NAME, null, values)
    }

    // 리뷰 삭제
    fun deleteReview(userId: String, clubName: String, date: String): Int {
        return db.delete(
            DatabaseContract.ReviewTable.TABLE_NAME,
            "${DatabaseContract.ReviewTable.COLUMN_ID} = ? AND " +
                    "${DatabaseContract.ReviewTable.COLUMN_CLUB_NAME} = ? AND " +
                    "${DatabaseContract.ReviewTable.COLUMN_DATE} = ?",
            arrayOf(userId, clubName, date)
        )
    }

    // 클럽별 리뷰 조회
    fun getReviewsByClub(clubName: String): List<Map<String, Any>> {
        val reviews = mutableListOf<Map<String, Any>>()
        val cursor = db.query(
            DatabaseContract.ReviewTable.TABLE_NAME,
            null,
            "${DatabaseContract.ReviewTable.COLUMN_CLUB_NAME} = ?",
            arrayOf(clubName),
            null, null, "${DatabaseContract.ReviewTable.COLUMN_DATE} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val review = mapOf(
                    "user_id" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ReviewTable.COLUMN_ID)),
                    "star" to it.getFloat(it.getColumnIndexOrThrow(DatabaseContract.ReviewTable.COLUMN_STAR)),
                    "review" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ReviewTable.COLUMN_REVIEW)),
                    "time" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ReviewTable.COLUMN_TIME)),
                    "date" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ReviewTable.COLUMN_DATE))
                )
                reviews.add(review)
            }
        }
        return reviews
    }

    // 사용자별 리뷰 조회
    fun getReviewsByUser(userId: String): List<Map<String, Any>> {
        val reviews = mutableListOf<Map<String, Any>>()
        val cursor = db.query(
            DatabaseContract.ReviewTable.TABLE_NAME,
            null,
            "${DatabaseContract.ReviewTable.COLUMN_ID} = ?",
            arrayOf(userId),
            null, null, "${DatabaseContract.ReviewTable.COLUMN_DATE} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val review = mapOf(
                    "club_name" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ReviewTable.COLUMN_CLUB_NAME)),
                    "star" to it.getFloat(it.getColumnIndexOrThrow(DatabaseContract.ReviewTable.COLUMN_STAR)),
                    "review" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ReviewTable.COLUMN_REVIEW)),
                    "time" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ReviewTable.COLUMN_TIME)),
                    "date" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ReviewTable.COLUMN_DATE))
                )
                reviews.add(review)
            }
        }
        return reviews
    }
}
