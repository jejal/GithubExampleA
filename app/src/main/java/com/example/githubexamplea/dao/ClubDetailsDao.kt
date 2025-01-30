package com.example.githubexamplea.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.database.DatabaseContract

class ClubDetailsDao(private val db: SQLiteDatabase) {

    // 클럽 상세 정보 추가
    fun insertClubDetails(
        clubName: String,
        clubIntroduction: String,
        program_1: String,
        program_2: String,
        program_3: String
    ): Long {
        val values = ContentValues().apply {
            put(DatabaseContract.ClubDetailsTable.COLUMN_CLUB_NAME, clubName)
            put(DatabaseContract.ClubDetailsTable.COLUMN_CLUB_INTRODUCTION, clubIntroduction)
            put(DatabaseContract.ClubDetailsTable.COLUMN_PROGRAM_1, program_1)
            put(DatabaseContract.ClubDetailsTable.COLUMN_PROGRAM_2, program_2)
            put(DatabaseContract.ClubDetailsTable.COLUMN_PROGRAM_3, program_3)
        }
        return db.insert(DatabaseContract.ClubDetailsTable.TABLE_NAME, null, values) // 트랜잭션 제거

    }

    // 기타 필요한 메서드 (조회/수정/삭제) 추가 가능
}
