package com.example.githubexamplea.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.database.DatabaseContract

class FaqDao(private val db: SQLiteDatabase) {

    // FAQ 추가
    fun insertFaq(
        userId: String,
        clubName: String,
        question: String,
        answer: String,
        time: String,
        date: String
    ): Long {
        val values = ContentValues().apply {
            put(DatabaseContract.FaqTable.COLUMN_ID, userId)
            put(DatabaseContract.FaqTable.COLUMN_CLUB_NAME, clubName)
            put(DatabaseContract.FaqTable.COLUMN_QUESTION, question)
            put(DatabaseContract.FaqTable.COLUMN_ANSWER, answer)
            put(DatabaseContract.FaqTable.COLUMN_TIME, time)
            put(DatabaseContract.FaqTable.COLUMN_DATE, date)
        }
        return db.insert(DatabaseContract.FaqTable.TABLE_NAME, null, values)
    }

    // FAQ 삭제
    fun deleteFaq(userId: String, clubName: String, date: String): Int {
        return db.delete(
            DatabaseContract.FaqTable.TABLE_NAME,
            "${DatabaseContract.FaqTable.COLUMN_ID} = ? AND " +
                    "${DatabaseContract.FaqTable.COLUMN_CLUB_NAME} = ? AND " +
                    "${DatabaseContract.FaqTable.COLUMN_DATE} = ?",
            arrayOf(userId, clubName, date)
        )
    }

    // 클럽별 FAQ 조회
    fun getFaqsByClub(clubName: String): List<Map<String, String>> {
        val faqs = mutableListOf<Map<String, String>>()
        val cursor = db.query(
            DatabaseContract.FaqTable.TABLE_NAME,
            null,
            "${DatabaseContract.FaqTable.COLUMN_CLUB_NAME} = ?",
            arrayOf(clubName),
            null, null, "${DatabaseContract.FaqTable.COLUMN_DATE} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val faq = mapOf(
                    "user_id" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.FaqTable.COLUMN_ID)),
                    "question" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.FaqTable.COLUMN_QUESTION)),
                    "answer" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.FaqTable.COLUMN_ANSWER)),
                    "time" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.FaqTable.COLUMN_TIME)),
                    "date" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.FaqTable.COLUMN_DATE))
                )
                faqs.add(faq)
            }
        }
        return faqs
    }

    // 전체 FAQ 조회
    fun getAllFaqs(): List<Map<String, String>> {
        val faqs = mutableListOf<Map<String, String>>()
        val cursor = db.query(
            DatabaseContract.FaqTable.TABLE_NAME,
            null,
            null, null, null, null, "${DatabaseContract.FaqTable.COLUMN_DATE} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val faq = mapOf(
                    "user_id" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.FaqTable.COLUMN_ID)),
                    "club_name" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.FaqTable.COLUMN_CLUB_NAME)),
                    "question" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.FaqTable.COLUMN_QUESTION)),
                    "answer" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.FaqTable.COLUMN_ANSWER)),
                    "time" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.FaqTable.COLUMN_TIME)),
                    "date" to it.getString(it.getColumnIndexOrThrow(DatabaseContract.FaqTable.COLUMN_DATE))
                )
                faqs.add(faq)
            }
        }
        return faqs
    }
}
