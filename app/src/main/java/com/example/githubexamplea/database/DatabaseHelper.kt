package com.example.githubexamplea.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "Actify.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "tb_user"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_BIRTHDAY = "birthday"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_UNIVERSITY = "university"
        private const val COLUMN_MAJOR = "major"
        private const val TAG = "DatabaseHelper" // 로그 태그 추가
    }

    override fun onCreate(db: SQLiteDatabase) {
        try {
            val createTable = """
                CREATE TABLE $TABLE_USERS (
                    $COLUMN_ID TEXT PRIMARY KEY,
                    $COLUMN_PASSWORD TEXT NOT NULL,
                    $COLUMN_NAME TEXT NOT NULL,
                    $COLUMN_BIRTHDAY TEXT NOT NULL,
                    $COLUMN_PHONE TEXT NOT NULL,
                    $COLUMN_UNIVERSITY TEXT NOT NULL,
                    $COLUMN_MAJOR TEXT NOT NULL
                )
            """.trimIndent()
            db.execSQL(createTable)
            Log.d(TAG, "테이블 생성 성공")
        } catch (e: Exception) {
            Log.e(TAG, "테이블 생성 실패: ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        try {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
            onCreate(db)
            Log.d(TAG, "테이블 업그레이드 성공")
        } catch (e: Exception) {
            Log.e(TAG, "테이블 업그레이드 실패: ${e.message}")
        }
    }

    fun isTableExists(): Boolean {
        return try {
            val db = this.readableDatabase
            val cursor = db.rawQuery(
                "SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='$TABLE_USERS'",
                null
            )
            cursor.moveToFirst()
            val count = cursor.getInt(0)
            cursor.close()

            val exists = count > 0
            Log.d(TAG, "테이블 존재 여부: $exists")
            exists
        } catch (e: Exception) {
            Log.e(TAG, "테이블 존재 여부 확인 실패: ${e.message}")
            false
        }
    }

    fun isIdExists(id: String): Boolean {
        return try {
            val db = this.readableDatabase
            val cursor = db.query(
                TABLE_USERS,
                arrayOf(COLUMN_ID),
                "$COLUMN_ID = ?",
                arrayOf(id),
                null,
                null,
                null
            )
            val exists = cursor.count > 0
            cursor.close()
            Log.d(TAG, "아이디 중복 체크: $exists")
            exists
        } catch (e: Exception) {
            Log.e(TAG, "아이디 중복 체크 실패: ${e.message}")
            false
        }
    }

    fun addUser(id: String, password: String, name: String, birthday: String, contact: String, school: String, major: String): Long {
        val db = this.writableDatabase
        db.beginTransaction()

        try {
            val values = ContentValues().apply {
                put(COLUMN_ID, id)
                put(COLUMN_PASSWORD, password)
                put(COLUMN_NAME, name)
                put(COLUMN_BIRTHDAY, birthday)
                put(COLUMN_PHONE, contact)
                put(COLUMN_UNIVERSITY, school)
                put(COLUMN_MAJOR, major)
            }

            val result = db.insert(TABLE_USERS, null, values)
            if (result != -1L) {
                db.setTransactionSuccessful()
                Log.d(TAG, "사용자 추가 성공: $id")
            } else {
                Log.e(TAG, "사용자 추가 실패: $id")
            }
            return result

        } catch (e: Exception) {
            Log.e(TAG, "사용자 추가 중 오류 발생: ${e.message}")
            return -1
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun validateUser(id: String, password: String): Boolean {
        return try {
            val db = this.readableDatabase
            val cursor = db.query(
                TABLE_USERS,
                arrayOf(COLUMN_ID),
                "$COLUMN_ID = ? AND $COLUMN_PASSWORD = ?",
                arrayOf(id, password),
                null,
                null,
                null
            )
            val isValid = cursor.count > 0
            cursor.close()
            Log.d(TAG, "사용자 인증 결과: $isValid")
            isValid
        } catch (e: Exception) {
            Log.e(TAG, "사용자 인증 실패: ${e.message}")
            false
        }
    }

    fun getUserName(id: String): String {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_NAME),
            "$COLUMN_ID = ?",
            arrayOf(id),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            val name = cursor.getString(0)
            cursor.close()
            name
        } else {
            cursor.close()
            "사용자"
        }
    }
}
