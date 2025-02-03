package com.example.githubexamplea.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.database.DatabaseContract
import kotlinx.coroutines.*

class UserDao(private val db: SQLiteDatabase) {

    // ✅ 사용자 추가 (INSERT) - Coroutine + Retry 적용
    suspend fun addUser(
        id: String,
        password: String,
        name: String,
        birthday: String,
        phone: String,
        university: String,
        major: String
    ): Long {
        return withContext(Dispatchers.IO) {
            var attempt = 0
            val maxAttempts = 5  // 최대 5회 재시도

            while (attempt < maxAttempts) {
                try {
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
                    if (result != -1L) return@withContext result // 성공 시 반환

                } catch (e: Exception) {
                    attempt++
                    delay(200L)  // 🔥 200ms 대기 후 재시도
                }
            }
            -1L // 실패 시 -1 반환
        }
    }

    // ✅ 사용자 조회 (SELECT) - Coroutine + Retry 적용
    suspend fun getUserById(id: String): Map<String, String>? {
        return withContext(Dispatchers.IO) {
            var attempt = 0
            val maxAttempts = 5  // 최대 5회 재시도

            while (attempt < maxAttempts) {
                try {
                    val cursor = db.query(
                        DatabaseContract.UserTable.TABLE_NAME,
                        null,
                        "${DatabaseContract.UserTable.COLUMN_ID} = ?",
                        arrayOf(id),
                        null, null, null
                    )

                    return@withContext cursor.use { c ->
                        if (c.moveToFirst()) {
                            mapOf(
                                DatabaseContract.UserTable.COLUMN_ID to c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_ID)),
                                DatabaseContract.UserTable.COLUMN_PASSWORD to c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_PASSWORD)),
                                DatabaseContract.UserTable.COLUMN_NAME to c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_NAME)),
                                DatabaseContract.UserTable.COLUMN_BIRTHDAY to c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_BIRTHDAY)),
                                DatabaseContract.UserTable.COLUMN_PHONE to c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_PHONE)),
                                DatabaseContract.UserTable.COLUMN_UNIVERSITY to c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_UNIVERSITY)),
                                DatabaseContract.UserTable.COLUMN_MAJOR to c.getString(c.getColumnIndexOrThrow(DatabaseContract.UserTable.COLUMN_MAJOR))
                            )
                        } else null
                    }

                } catch (e: Exception) {
                    attempt++
                    delay(200L)  // 🔥 200ms 대기 후 재시도
                }
            }
            null // 실패 시 null 반환
        }
    }

    // ✅ 사용자 업데이트 (UPDATE) - 기존 방식 유지
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

    // ✅ 사용자 삭제 (DELETE) - 기존 방식 유지
    fun deleteUser(id: String): Int {
        return db.delete(
            DatabaseContract.UserTable.TABLE_NAME,
            "${DatabaseContract.UserTable.COLUMN_ID} = ?",
            arrayOf(id)
        )
    }
}
