package com.example.githubexamplea.database

import kotlinx.coroutines.*
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.githubexamplea.data.InsertApplicationExample
import com.example.githubexamplea.data.InsertClubExample
import com.example.githubexamplea.data.InsertFaqExample
import com.example.githubexamplea.data.InsertLeaderExample
import com.example.githubexamplea.data.InsertLikeExample
import com.example.githubexamplea.data.InsertReviewExample
import com.example.githubexamplea.data.InsertUserExample

class DatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "Actify.db"
        private const val DATABASE_VERSION = 12
        private const val TAG = "DatabaseHelper"

        // 기존 tb_user 테이블 관련 상수
        private const val TABLE_USERS = "tb_user"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_BIRTHDAY = "birthday"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_UNIVERSITY = "university"
        private const val COLUMN_MAJOR = "major"

        // 새로운 테이블 이름 상수
        private const val TABLE_LEADER = "tb_leader"
        private const val TABLE_LIKE = "tb_like"
        private const val TABLE_CLUB = "tb_club"
        private const val TABLE_APPLICATION = "tb_application"
        private const val TABLE_CLUB_DETAILS = "tb_club_details"
        private const val TABLE_REVIEW = "tb_review"
        private const val TABLE_FAQ = "tb_faq"
    }

    override fun onCreate(db: SQLiteDatabase) {
        createTbUser(db)
        createTbLeader(db)
        createTbLike(db)
        createTbClub(db)
        createTbApplication(db)
        createTbClubDetails(db)
        createTbReview(db)
        createTbFaq(db)

        val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val isDatabaseInitialized = sharedPreferences.getBoolean("isDatabaseInitialized", false)

        if (!isDatabaseInitialized) {
            // 🔹 CoroutineScope을 사용해서 비동기 실행
            CoroutineScope(Dispatchers.IO).launch {
                insertInitialData(db, context)

                // 초기화 완료 저장
                withContext(Dispatchers.Main) {
                    sharedPreferences.edit().putBoolean("isDatabaseInitialized", true).apply()
                }
            }
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LEADER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LIKE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLUB")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPLICATION")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLUB_DETAILS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REVIEW")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAQ")
        onCreate(db)
    }

    private fun createTbUser(db: SQLiteDatabase) {
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
    }

    private fun createTbLeader(db: SQLiteDatabase) {
        val createTbLeader = """
            CREATE TABLE $TABLE_LEADER (
                club_name TEXT PRIMARY KEY,
                id TEXT NOT NULL,
                leader_introduction TEXT NOT NULL,
                club_introduction TEXT NOT NULL,
                leader_career TEXT,
                leader_photo_path TEXT
            )
        """.trimIndent()
        db.execSQL(createTbLeader)
    }

    private fun createTbLike(db: SQLiteDatabase) {
        val createTbLike = """
            CREATE TABLE $TABLE_LIKE (
                id TEXT NOT NULL,
                club_name TEXT NOT NULL,
                PRIMARY KEY (id, club_name)
            )
        """.trimIndent()
        db.execSQL(createTbLike)
    }

    private fun createTbClub(db: SQLiteDatabase) {
        val createTbClub = """
            CREATE TABLE $TABLE_CLUB (
                club_name TEXT PRIMARY KEY,
                short_title TEXT NOT NULL,
                short_introduction TEXT NOT NULL,
                date TEXT,
                time TEXT,
                location TEXT,
                needs TEXT,
                cost TEXT,
                photo_path TEXT
            )
        """.trimIndent()
        db.execSQL(createTbClub)
    }

    private fun createTbApplication(db: SQLiteDatabase) {
        val createTbApplication = """
            CREATE TABLE $TABLE_APPLICATION (
                id TEXT NOT NULL,
                club_name TEXT NOT NULL,
                date TEXT NOT NULL,
                time TEXT NOT NULL,
                PRIMARY KEY (id, club_name, date, time)
            )
        """.trimIndent()
        db.execSQL(createTbApplication)
    }

    private fun createTbClubDetails(db: SQLiteDatabase) {
        val createTbClubDetails = """
            CREATE TABLE $TABLE_CLUB_DETAILS (
                club_name TEXT PRIMARY KEY,
                club_introduction TEXT,
                program_1 TEXT,
                program_2 TEXT,
                program_3 TEXT
            )
        """.trimIndent()
        db.execSQL(createTbClubDetails)
    }

    private fun createTbReview(db: SQLiteDatabase) {
        val createTbReview = """
            CREATE TABLE $TABLE_REVIEW (
                id TEXT NOT NULL,
                club_name TEXT NOT NULL,
                star REAL NOT NULL,
                review TEXT NOT NULL,
                time TEXT NOT NULL DEFAULT (strftime('%H:%M:%S', 'now', 'localtime')),
                date TEXT NOT NULL DEFAULT (strftime('%Y-%m-%d', 'now', 'localtime'))
            )
        """.trimIndent()
        db.execSQL(createTbReview)
    }

    private fun createTbFaq(db: SQLiteDatabase) {
        val createTbFaq = """
            CREATE TABLE $TABLE_FAQ (
                id TEXT NOT NULL,
                club_name TEXT NOT NULL,
                question TEXT NOT NULL,
                answer TEXT NOT NULL,
                time TEXT NOT NULL,
                date TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTbFaq)
    }

    private suspend fun insertInitialData(db: SQLiteDatabase, context: Context) {
        Log.d(TAG, "초기 데이터 삽입 시작")

        try {
            withContext(Dispatchers.IO) {  // 🔹 IO 스레드에서 실행 (DB 최적화)
                InsertUserExample(db).insertMultipleUsers()
                InsertLikeExample(db).insertLikes()
                InsertApplicationExample(db).insertSampleApplications()
                InsertReviewExample(db).insertSampleReviews()
                InsertFaqExample(db).insertSampleFaqs()

                // 🆕 추가할 부분: tb_leader와 tb_club 데이터도 함께 삽입!
                InsertLeaderExample(db, context).insertLeaders()
                InsertClubExample(db, context).insertClubsWithDetails()
            }
        } catch (e: Exception) {
            Log.e(TAG, "초기 데이터 삽입 중 오류 발생: ${e.message}")
        }

        Log.d(TAG, "초기 데이터 삽입 완료")
    }

    // ✅ 안전한 사용자 인증 (Coroutine + Retry 적용)
    suspend fun validateUser(id: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            var attempt = 0
            val maxAttempts = 5  // 최대 5회 재시도

            while (attempt < maxAttempts) {
                try {
                    val db = readableDatabase
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
                    return@withContext isValid  // ✅ 성공하면 즉시 반환

                } catch (e: Exception) {
                    attempt++
                    delay(200L)  // 🔥 200ms 대기 후 재시도
                }
            }
            false  // 🔴 5번 재시도 후에도 실패하면 false 반환
        }
    }

    // ✅ 사용자 정보 가져오기
    suspend fun getUserName(id: String): String {
        return withContext(Dispatchers.IO) {
            val db = readableDatabase
            val cursor = db.query(
                TABLE_USERS,
                arrayOf(COLUMN_NAME),
                "$COLUMN_ID = ?",
                arrayOf(id),
                null, null, null
            )
            val name = if (cursor.moveToFirst()) cursor.getString(0) else "사용자"
            cursor.close()
            name
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
}
