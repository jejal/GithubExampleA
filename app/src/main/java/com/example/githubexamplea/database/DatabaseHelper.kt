package com.example.githubexamplea.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "Actify.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "tb_user"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)

        // 테스트용 계정 추가
        val insertUser = "INSERT INTO $TABLE_USERS ($COLUMN_ID, $COLUMN_PASSWORD) VALUES (?, ?)"
        db.execSQL(insertUser, arrayOf("daye", "1234"))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun validateUser(id: String, password: String): Boolean {
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
        return isValid
    }
}
