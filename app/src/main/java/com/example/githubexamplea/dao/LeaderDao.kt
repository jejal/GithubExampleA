package com.example.githubexamplea.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.database.DatabaseContract

class LeaderDao(private val db: SQLiteDatabase) {

    // 리더 추가
    fun insertLeader(
        clubName: String,
        leaderId: String,
        leaderIntroduction: String,
        clubIntroduction: String,
        leaderCareer: String?
    ): Long {
        db.beginTransaction()
        return try {
            val values = ContentValues().apply {
                put(DatabaseContract.LeaderTable.COLUMN_CLUB_NAME, clubName)
                put(DatabaseContract.LeaderTable.COLUMN_ID, leaderId)
                put(DatabaseContract.LeaderTable.COLUMN_LEADER_INTRO, leaderIntroduction)
                put(DatabaseContract.LeaderTable.COLUMN_CLUB_INTRO, clubIntroduction)
                put(DatabaseContract.LeaderTable.COLUMN_LEADER_CAREER, leaderCareer)
            }
            val result = db.insert(DatabaseContract.LeaderTable.TABLE_NAME, null, values)
            if (result != -1L) {
                db.setTransactionSuccessful()
            }
            result
        } finally {
            db.endTransaction()
        }
    }

    // 리더 정보 업데이트
    fun updateLeader(
        clubName: String,
        newLeaderId: String,
        newLeaderIntro: String,
        newClubIntro: String,
        newLeaderCareer: String?
    ): Int {
        val values = ContentValues().apply {
            put(DatabaseContract.LeaderTable.COLUMN_ID, newLeaderId)
            put(DatabaseContract.LeaderTable.COLUMN_LEADER_INTRO, newLeaderIntro)
            put(DatabaseContract.LeaderTable.COLUMN_CLUB_INTRO, newClubIntro)
            put(DatabaseContract.LeaderTable.COLUMN_LEADER_CAREER, newLeaderCareer)
        }
        return db.update(
            DatabaseContract.LeaderTable.TABLE_NAME,
            values,
            "${DatabaseContract.LeaderTable.COLUMN_CLUB_NAME} = ?",
            arrayOf(clubName)
        )
    }

    // 리더 삭제
    fun deleteLeader(clubName: String): Int {
        db.beginTransaction()
        return try {
            val rows = db.delete(
                DatabaseContract.LeaderTable.TABLE_NAME,
                "${DatabaseContract.LeaderTable.COLUMN_CLUB_NAME} = ?",
                arrayOf(clubName)
            )
            db.setTransactionSuccessful()
            rows
        } finally {
            db.endTransaction()
        }
    }

    // 리더 정보 조회
    fun getLeaderByClubName(clubName: String): Map<String, String>? {
        val cursor = db.query(
            DatabaseContract.LeaderTable.TABLE_NAME,
            null,
            "${DatabaseContract.LeaderTable.COLUMN_CLUB_NAME} = ?",
            arrayOf(clubName),
            null, null, null
        )
        cursor.use { c ->
            return if (c.moveToFirst()) {
                val leaderMap = mutableMapOf<String, String>()
                leaderMap[DatabaseContract.LeaderTable.COLUMN_CLUB_NAME] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.LeaderTable.COLUMN_CLUB_NAME))
                leaderMap[DatabaseContract.LeaderTable.COLUMN_ID] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.LeaderTable.COLUMN_ID))
                leaderMap[DatabaseContract.LeaderTable.COLUMN_LEADER_INTRO] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.LeaderTable.COLUMN_LEADER_INTRO))
                leaderMap[DatabaseContract.LeaderTable.COLUMN_CLUB_INTRO] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.LeaderTable.COLUMN_CLUB_INTRO))
                leaderMap[DatabaseContract.LeaderTable.COLUMN_LEADER_CAREER] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.LeaderTable.COLUMN_LEADER_CAREER))
                leaderMap
            } else {
                null
            }
        }
    }
}
