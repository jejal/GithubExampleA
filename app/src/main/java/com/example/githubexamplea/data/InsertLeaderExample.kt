package com.example.githubexamplea.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.githubexamplea.dao.LeaderDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class InsertLeaderExample(private val db: SQLiteDatabase, private val context: Context) {

    suspend fun insertLeaders() = withContext(Dispatchers.IO) {
        val leaderDao = LeaderDao(db)

        Log.d("DB_INSERT", "리더 데이터 삽입 시작")

        // 리더 정보 리스트 (club_name, id, leader_introduction, club_introduction, leader_career, leader_photo)
        val leaders = listOf(
            arrayOf("Kicks & Dreams", "miso", "안녕하세요. \nKicks & Dreams의 \n모임장 김미소 입니다.", "축구를 사랑하는 사람들의 모임", "Kicks & Dreams 모임장/Happy Soccer 모임장/나이키 마케터", "leader_miso.jpg"),
            arrayOf("Breathe", "future", "안녕하세요. \nBreathe의 \n모임장 이미래 입니다.", "차분하게 정리하는 시간을 가져봐요", "Breathe 모임장/Marathon Club 운영진/체육 교육 전공", "leader_future.jpg"),
            arrayOf("Hit & Hustle", "jun", "안녕하세요. \nHit & Hustle의 \n모임장 이성준 입니다.", "야구에 진심인 사람들의 모임", "Hit & Hustle의 모임장/연속 3번 믿을만한 모임장 선정!", "leader_jun.jpg")
        )

        try {
            for (leaderData in leaders) {
                val imagePath = copyImageToInternalStorage(context, leaderData[5]) // 🔹 suspend 함수로 변경됨
                val result = leaderDao.insertLeader(
                    clubName = leaderData[0],
                    leaderId = leaderData[1],
                    leaderIntroduction = leaderData[2],
                    clubIntroduction = leaderData[3],
                    leaderCareer = leaderData[4],
                    leaderPhotoPath = imagePath
                )
                Log.d("DB_INSERT", "삽입된 데이터 ID: ${leaderData[1]}, result=$result")
            }
        } catch (e: Exception) {
            Log.e("DB_INSERT", "리더 데이터 삽입 중 오류 발생: ${e.message}")
        }
    }

    private suspend fun copyImageToInternalStorage(context: Context, fileName: String): String =
        withContext(Dispatchers.IO) {
            val directory = File(context.filesDir, "user_images")
            if (!directory.exists()) directory.mkdirs() // 디렉토리 없으면 생성

            val file = File(directory, fileName)

            return@withContext try {
                val inputStream = context.assets.open("images/$fileName")
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                Log.d("FileCopyDebug", "이미지 복사 성공: ${file.absolutePath}")
                file.absolutePath
            } catch (e: Exception) {
                Log.e("FileCopyDebug", "이미지 복사 실패: ${e.message}")
                ""
            }
        }
}
