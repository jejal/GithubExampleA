package com.example.githubexamplea.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.database.DatabaseContract

class UserDao(private val db: SQLiteDatabase) {

    // 사용자 추가 (INSERT)
    fun addUser(
        id: String,
        password: String,
        name: String,
        birthday: String,
        phone: String,
        university: String,
        major: String
    ): Long {
        db.beginTransaction()
        return try {
            val values = ContentValues().apply {
                put(DatabaseContract.UserTable.COLUMN_ID, id)
                put(DatabaseContract.UserTable.COLUMN_PASSWORD, password)
                put(DatabaseContract.UserTable.COLUMN_NAME, name)
                put(DatabaseContract.UserTable.COLUMN_BIRTHDAY, birthday)
                put(DatabaseContract.UserTable.COLUMN_PHONE, phone)
                put(DatabaseContract.UserTable.COLUMN_UNIVERSITY, university)
                put(DatabaseContract.UserTable.COLUMN_MAJOR, major)
            }
            val result = db.insert(DatabaseContract.UserTable.TABLE_NAME, null, values)
            if (result != -1L) {
                db.setTransactionSuccessful()
            }
            result
        } finally {
            db.endTransaction()
        }
    }

    // 사용자 업데이트 (UPDATE)
    fun updateUser(
        id: String,
        newPassword: String,
        newName: String,
        newBirthday: String,
        newPhone: String,
        newUniversity: String,
        newMajor: String
    ): Int {
        val values = ContentValues().apply {
            put(DatabaseContract.UserTable.COLUMN_PASSWORD, newPassword)
            put(DatabaseContract.UserTable.COLUMN_NAME, newName)
            put(DatabaseContract.UserTable.COLUMN_BIRTHDAY, newBirthday)
            put(DatabaseContract.UserTable.COLUMN_PHONE, newPhone)
            put(DatabaseContract.UserTable.COLUMN_UNIVERSITY, newUniversity)
            put(DatabaseContract.UserTable.COLUMN_MAJOR, newMajor)
        }
        return db.update(
            DatabaseContract.UserTable.TABLE_NAME,
            values,
            "${DatabaseContract.UserTable.COLUMN_ID} = ?",
            arrayOf(id)
        )
    }

    // 사용자 삭제 (DELETE)
    fun deleteUser(id: String): Int {
        db.beginTransaction()
        return try {
            val rows = db.delete(
                DatabaseContract.UserTable.TABLE_NAME,
                "${DatabaseContract.UserTable.COLUMN_ID} = ?",
                arrayOf(id)
            )
            db.setTransactionSuccessful()
            rows
        } finally {
            db.endTransaction()
        }
    }

    // 사용자 조회 (SELECT)
    fun getUserById(id: String): Map<String, String>? {
        val cursor = db.query(
            DatabaseContract.UserTable.TABLE_NAME,
            null,
            "${DatabaseContract.UserTable.COLUMN_ID} = ?",
            arrayOf(id),
            null, null, null
        )
        cursor.use { c ->
            return if (c.moveToFirst()) {
                val userMap = mutableMapOf<String, String>()
                userMap[DatabaseContract.UserTable.COLUMN_ID] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_ID))
                userMap[DatabaseContract.UserTable.COLUMN_PASSWORD] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_PASSWORD))
                userMap[DatabaseContract.UserTable.COLUMN_NAME] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_NAME))
                userMap[DatabaseContract.UserTable.COLUMN_BIRTHDAY] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_BIRTHDAY))
                userMap[DatabaseContract.UserTable.COLUMN_PHONE] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_PHONE))
                userMap[DatabaseContract.UserTable.COLUMN_UNIVERSITY] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_UNIVERSITY))
                userMap[DatabaseContract.UserTable.COLUMN_MAJOR] =
                    c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_MAJOR))
                userMap
            } else {
                null
            }
        }
    }
}