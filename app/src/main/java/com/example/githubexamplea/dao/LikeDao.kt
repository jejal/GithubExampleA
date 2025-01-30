package com.example.githubexamplea.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.database.DatabaseContract

class LikeDao(private val db: SQLiteDatabase) {

    // 좋아요 추가
    fun insertLike(userId: String, clubName: String): Long {
        val values = ContentValues().apply {
            put(DatabaseContract.LikeTable.COLUMN_ID, userId)
            put(DatabaseContract.LikeTable.COLUMN_CLUB_NAME, clubName)
        }
        return db.insert(
            DatabaseContract.LikeTable.TABLE_NAME,
            null,
            values
        )
    }

    // 좋아요 삭제 (특정 사용자의 특정 클럽 좋아요 취소)
    fun deleteLike(userId: String, clubName: String): Int {
        return db.delete(
            DatabaseContract.LikeTable.TABLE_NAME,
            "${DatabaseContract.LikeTable.COLUMN_ID} = ? AND " +
                    "${DatabaseContract.LikeTable.COLUMN_CLUB_NAME} = ?",
            arrayOf(userId, clubName)
        )
    }

    // 사용자별 좋아요 목록 조회
    fun getLikesByUser(userId: String): List<String> {
        val likedClubs = mutableListOf<String>()
        val cursor = db.query(
            DatabaseContract.LikeTable.TABLE_NAME,
            arrayOf(DatabaseContract.LikeTable.COLUMN_CLUB_NAME),
            "${DatabaseContract.LikeTable.COLUMN_ID} = ?",
            arrayOf(userId),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                likedClubs.add(
                    it.getString(
                        it.getColumnIndexOrThrow(DatabaseContract.LikeTable.COLUMN_CLUB_NAME)
                    )
                )
            }
        }
        return likedClubs
    }

    // 클럽별 좋아요한 사용자 목록 조회
    fun getLikesByClub(clubName: String): List<String> {
        val likedUsers = mutableListOf<String>()
        val cursor = db.query(
            DatabaseContract.LikeTable.TABLE_NAME,
            arrayOf(DatabaseContract.LikeTable.COLUMN_ID),
            "${DatabaseContract.LikeTable.COLUMN_CLUB_NAME} = ?",
            arrayOf(clubName),
            null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                likedUsers.add(
                    it.getString(
                        it.getColumnIndexOrThrow(DatabaseContract.LikeTable.COLUMN_ID)
                    )
                )
            }
        }
        return likedUsers
    }

    // 특정 좋아요 존재 여부 확인
    fun isLiked(userId: String, clubName: String): Boolean {
        val cursor = db.query(
            DatabaseContract.LikeTable.TABLE_NAME,
            null,
            "${DatabaseContract.LikeTable.COLUMN_ID} = ? AND " +
                    "${DatabaseContract.LikeTable.COLUMN_CLUB_NAME} = ?",
            arrayOf(userId, clubName),
            null, null, null
        )
        return cursor.use { it.count > 0 }
    }
}
