package com.example.githubexamplea.data

import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InsertUserExample(private val db: SQLiteDatabase) {

    suspend fun insertMultipleUsers() {
        val userDao = UserDao(db)

        // 사람 정보 리스트에
        val usersToInsert = listOf(
            // id, password, name, birthday, phone, university, major 순
            arrayOf("lee", "0000", "이언", "1990.09.01", "010-1234-5678", "서울여자대학교", "데이터사이언스학과"),
            arrayOf("miso", "1111", "김미소", "1990.09.01", "010-1111-1111", "서울대학교", "경영학과"),
            arrayOf("jun", "2222", "이성준", "1997.09.01", "010-2222-2222", "연세대학교", "체육교육과"),
            arrayOf("future", "3333", "이미래", "1994.03.12", "010-3333-3333", "고려대학교", "수학과"),
            arrayOf("nare", "4444", "박나래", "2002.05.05", "010-4444-4444", "한양대학교", "경제학과"),
            arrayOf("uuu", "5555", "성선우", "2000.11.28", "010-5555-5555", "건국대학교", "화학공학과")
        )

        // 🔹 Coroutine 사용해서 DB 작업 실행 (I/O 스레드에서 실행)
        withContext(Dispatchers.IO) {
            for (userData in usersToInsert) {
                userDao.addUser(
                    id = userData[0],
                    password = userData[1],
                    name = userData[2],
                    birthday = userData[3],
                    phone = userData[4],
                    university = userData[5],
                    major = userData[6]
                )
            }
        }
    }
}