package com.example.githubexamplea.data

import android.content.Context
import com.example.githubexamplea.database.DatabaseHelper
import com.example.githubexamplea.dao.LeaderDao

class InsertLeaderExample(private val context: Context) {

    fun insertLeaders() {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val leaderDao = LeaderDao(db)

        // 리더 정보 리스트 (club_name, id, leader_introduction, club_introduction, leader_career)
        val leaders = listOf(
            arrayOf("Kicks & Dreams", "miso", "안녕하세요. Kicks & Dreams의 모임장 김미소 입니다.", "축구를 사랑하는 사람들의 모임", "Kicks & Dreams 모임장/Happy Soccer 모임장/나이키 마케터"),
            arrayOf("Running Crew", "future", "안녕하세요. Running Crew 모임장 이미래 입니다.", "함께 달리며 건강한 삶을 만들어가요", "Running Crew 모임장/Marathon Club 운영진/체육 교육 전공"),
            arrayOf("수영에 빠지다", "jun", "안녕하세요. 수영에 빠지다의 모임장 이성준 입니다.", "수영에 진심인 사람들의 모임", "수영에 빠지다의 모임장/연속 3번 믿을만한 모임장 선정!")
        )

        try {
            for (leaderData in leaders) {
                leaderDao.insertLeader(
                    clubName = leaderData[0],
                    leaderId = leaderData[1],
                    leaderIntroduction = leaderData[2],
                    clubIntroduction = leaderData[3],
                    leaderCareer = leaderData[4]
                )
            }
        } finally {
            db.close()
        }
    }
}
