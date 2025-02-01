package com.example.githubexamplea.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.database.DatabaseContract

class ClubDao(private val db: SQLiteDatabase) {

    // 클럽 추가
    fun insertClub(
        clubName: String,
        shortTitle: String,
        shortIntroduction: String,
        date: String?,
        time: String?,
        location: String?,
        needs: String?,
        cost: String?,
        photoPath: String?
    ): Long {
        val values = ContentValues().apply {
            put(DatabaseContract.ClubTable.COLUMN_CLUB_NAME, clubName)
            put(DatabaseContract.ClubTable.COLUMN_SHORT_TITLE, shortTitle)
            put(DatabaseContract.ClubTable.COLUMN_SHORT_INTRODUCTION, shortIntroduction)
            put(DatabaseContract.ClubTable.COLUMN_DATE, date)
            put(DatabaseContract.ClubTable.COLUMN_TIME, time)
            put(DatabaseContract.ClubTable.COLUMN_LOCATION, location)
            put(DatabaseContract.ClubTable.COLUMN_NEEDS, needs)
            put(DatabaseContract.ClubTable.COLUMN_COST, cost)
            put(DatabaseContract.ClubTable.COLUMN_PHOTO_PATH, photoPath)
        }
        return db.insert(DatabaseContract.ClubTable.TABLE_NAME, null, values)
    }

    // 클럽 정보 업데이트
    fun updateClub(
        clubName: String,
        shortTitle: String,
        shortIntroduction: String,
        newDate: String?,
        newTime: String?,
        newLocation: String?,
        newNeeds: String?,
        newCost: String?,
        newPhotoPath: String?
    ): Int {
        val values = ContentValues().apply {
            put(DatabaseContract.ClubTable.COLUMN_SHORT_TITLE, shortTitle)
            put(DatabaseContract.ClubTable.COLUMN_SHORT_INTRODUCTION, shortIntroduction)
            put(DatabaseContract.ClubTable.COLUMN_DATE, newDate)
            put(DatabaseContract.ClubTable.COLUMN_TIME, newTime)
            put(DatabaseContract.ClubTable.COLUMN_LOCATION, newLocation)
            put(DatabaseContract.ClubTable.COLUMN_NEEDS, newNeeds)
            put(DatabaseContract.ClubTable.COLUMN_COST, newCost)
            put(DatabaseContract.ClubTable.COLUMN_PHOTO_PATH, newPhotoPath)
        }
        return db.update(
            DatabaseContract.ClubTable.TABLE_NAME,
            values,
            "${DatabaseContract.ClubTable.COLUMN_CLUB_NAME} = ?",
            arrayOf(clubName)
        )
    }

    // 클럽 삭제
    fun deleteClub(clubName: String): Int {
        db.beginTransaction()
        return try {
            val rows = db.delete(
                DatabaseContract.ClubTable.TABLE_NAME,
                "${DatabaseContract.ClubTable.COLUMN_CLUB_NAME} = ?",
                arrayOf(clubName)
            )
            db.setTransactionSuccessful()
            rows
        } finally {
            db.endTransaction()
        }
    }

    // 특정 클럽 조회
    fun getClubByName(clubName: String): Map<String, String>? {
        val cursor = db.query(
            DatabaseContract.ClubTable.TABLE_NAME,
            null,
            "${DatabaseContract.ClubTable.COLUMN_CLUB_NAME} = ?",
            arrayOf(clubName),
            null, null, null
        )
        cursor.use { c ->
            return if (c.moveToFirst()) {
                val clubMap = mutableMapOf<String, String>()
                clubMap[DatabaseContract.ClubTable.COLUMN_CLUB_NAME] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_CLUB_NAME))
                clubMap[DatabaseContract.ClubTable.COLUMN_SHORT_TITLE] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_SHORT_TITLE))
                clubMap[DatabaseContract.ClubTable.COLUMN_SHORT_INTRODUCTION] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_SHORT_INTRODUCTION))
                clubMap[DatabaseContract.ClubTable.COLUMN_DATE] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_DATE))
                clubMap[DatabaseContract.ClubTable.COLUMN_TIME] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_TIME))
                clubMap[DatabaseContract.ClubTable.COLUMN_LOCATION] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_LOCATION))
                clubMap[DatabaseContract.ClubTable.COLUMN_NEEDS] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_NEEDS))
                clubMap[DatabaseContract.ClubTable.COLUMN_COST] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_COST))
                clubMap[DatabaseContract.ClubTable.COLUMN_PHOTO_PATH] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_PHOTO_PATH))
                clubMap
            } else {
                null
            }
        }
    }

    // 모든 클럽 조회
    fun getAllClubs(): List<Map<String, String>> {
        val list = mutableListOf<Map<String, String>>()
        val cursor = db.query(
            DatabaseContract.ClubTable.TABLE_NAME,
            null,
            null, null, null, null, null
        )
        cursor.use { c ->
            while (c.moveToNext()) {
                val clubMap = mutableMapOf<String, String>()
                clubMap[DatabaseContract.ClubTable.COLUMN_CLUB_NAME] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_CLUB_NAME))
                clubMap[DatabaseContract.ClubTable.COLUMN_SHORT_TITLE] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_SHORT_TITLE))
                clubMap[DatabaseContract.ClubTable.COLUMN_SHORT_INTRODUCTION] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_SHORT_INTRODUCTION))
                clubMap[DatabaseContract.ClubTable.COLUMN_DATE] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_DATE))
                clubMap[DatabaseContract.ClubTable.COLUMN_TIME] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_TIME))
                clubMap[DatabaseContract.ClubTable.COLUMN_LOCATION] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_LOCATION))
                clubMap[DatabaseContract.ClubTable.COLUMN_NEEDS] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_NEEDS))
                clubMap[DatabaseContract.ClubTable.COLUMN_COST] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_COST))
                clubMap[DatabaseContract.ClubTable.COLUMN_PHOTO_PATH] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.ClubTable.COLUMN_PHOTO_PATH))
                list.add(clubMap)
            }
        }
        return list
    }
}