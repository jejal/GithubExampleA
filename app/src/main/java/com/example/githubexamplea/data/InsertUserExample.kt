package com.example.githubexamplea.insert

import android.content.Context
import com.example.githubexamplea.database.DatabaseHelper
import com.example.githubexamplea.dao.UserDao

class InsertUserExample(private val context: Context) {

    fun insertMultipleUsers() {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val userDao = UserDao(db)

        // 사람 정보 리스트에
        val usersToInsert = listOf(
            // id, password, name, birthday, phone, university, major 순
            arrayOf("actify", "actify", "액티파이", "1990.09.01", "010-1234-1234", "서울여자대학교", "데이터사이언스학과"),
            arrayOf("miso", "1111", "김미소", "1990.09.01", "010-1111-1111", "서울대학교", "경영학과"),
            arrayOf("jun", "1111", "이성준", "1997.09.01", "010-2222-2222", "연세대학교", "체육교육과"),
            arrayOf("future", "2222", "이미래", "1994.03.12", "010-3333-3333", "고려대학교", "수학과"),
            arrayOf("nare", "3333", "박나래", "2002.05.05", "010-4444-4444", "한양대학교", "경제학과"),
            arrayOf("uuu", "3333", "성선우", "2000.11.28", "010-5555-5555", "건국대학교", "화학공학과")
        )

        try {
            // 각 사용자 정보를 반복문으로 추가
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
        } finally {
            db.close()
        }
    }
}