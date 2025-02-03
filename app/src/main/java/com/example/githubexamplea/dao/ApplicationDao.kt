package com.example.githubexamplea.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.database.DatabaseContract

class ApplicationDao(private val db: SQLiteDatabase) {

    // 지원서 추가
    fun insertApplication(
        userId: String,
        clubName: String,
        date: String,
        time: String
    ): Long {
        val values = ContentValues().apply {
            put(DatabaseContract.ApplicationTable.COLUMN_ID, userId)
            put(DatabaseContract.ApplicationTable.COLUMN_CLUB_NAME, clubName)
            put(DatabaseContract.ApplicationTable.COLUMN_DATE, date)
            put(DatabaseContract.ApplicationTable.COLUMN_TIME, time)
        }
        return db.insertWithOnConflict(
            DatabaseContract.ApplicationTable.TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_IGNORE // 중복 시 무시
        )
    }

    // 지원서 삭제
    fun deleteApplication(userId: String, clubName: String, date: String, time: String): Int {
        return db.delete(
            DatabaseContract.ApplicationTable.TABLE_NAME,
            "${DatabaseContract.ApplicationTable.COLUMN_ID} = ? AND " +
                    "${DatabaseContract.ApplicationTable.COLUMN_CLUB_NAME} = ? AND " +
                    "${DatabaseContract.ApplicationTable.COLUMN_DATE} = ? AND " +
                    "${DatabaseContract.ApplicationTable.COLUMN_TIME} = ?",
            arrayOf(userId, clubName, date, time)
        )
    }

    // 사용자별 지원 내역 조회
    fun getApplicationsByUser(userId: String): List<Map<String, String>> {
        val applications = mutableListOf<Map<String, String>>()
        val cursor = db.query(
            DatabaseContract.ApplicationTable.TABLE_NAME,
            null,
            "${DatabaseContract.ApplicationTable.COLUMN_ID} = ?",
            arrayOf(userId),
            null, null, "${DatabaseContract.ApplicationTable.COLUMN_DATE} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val app = mapOf(
                    "club_name" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ApplicationTable.COLUMN_CLUB_NAME)),
                    "date" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ApplicationTable.COLUMN_DATE)),
                    "time" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ApplicationTable.COLUMN_TIME))
                )
                applications.add(app)
            }
        }
        return applications
    }

    // 클럽별 지원자 목록 조회
    fun getApplicantsByClub(clubName: String): List<Map<String, String>> {
        val applicants = mutableListOf<Map<String, String>>()
        val cursor = db.query(
            DatabaseContract.ApplicationTable.TABLE_NAME,
            null,
            "${DatabaseContract.ApplicationTable.COLUMN_CLUB_NAME} = ?",
            arrayOf(clubName),
            null, null, "${DatabaseContract.ApplicationTable.COLUMN_DATE} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val applicant = mapOf(
                    "user_id" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ApplicationTable.COLUMN_ID)),
                    "date" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ApplicationTable.COLUMN_DATE)),
                    "time" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.ApplicationTable.COLUMN_TIME))
                )
                applicants.add(applicant)
            }
        }
        return applicants
    }
}
