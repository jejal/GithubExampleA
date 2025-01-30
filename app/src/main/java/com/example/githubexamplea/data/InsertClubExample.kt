package com.example.githubexamplea.data

import android.content.Context
import com.example.githubexamplea.dao.ClubDao
import com.example.githubexamplea.dao.ClubDetailsDao
import com.example.githubexamplea.database.DatabaseHelper

class InsertClubExample(private val context: Context) {
    fun insertClubsWithDetails() {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        val clubDao = ClubDao(db)
        val clubDetailsDao = ClubDetailsDao(db)

        val clubs = listOf(
            ClubData(
                clubName = "Kicks & Dreams",
                shortTitle = "열정을 불태우는 축구 타임",
                shortIntroduction = "매주 쌓아올리는 팀워크! 초보라도 도전 정신을 가진 사람이라면 누구나 환영!",
                date = "매주 수요일",
                time = "저녁 7시 ~ 9시",
                location = "서울여자대학교 운동장",
                needs = "운동화(축구화), 운동복, 물병, 보호대(선택)",
                cost = "월 10,000원",
                details = ClubDetailsData(
                    clubName = "Kicks & Dreams",
                    clubIntroduction = "킥스 앤 드림스는 축구를 사랑하는 대학생 여성들이 모여 즐거움과 열정을 나누는 곳입니다. 초보자도 환영하며, 기본 드릴과 기술부터 시작해 경기 전략까지 함께 배우고 연습합니다. 매주 팀원들과 함께 땀흘리며 팀워크와 유대감을 쌓아보세요! 정기적으로 다른 동아리와의 친선 경기 및 자체 리그도 진행되니 도전 정신을 가진 여러분을 기다립니다. 축구 실력보다 중요한 건 즐길 준비입니다!",
                    program_1 = "연습 프로그램\n" +
                            "드리블, 패스, 슛 등 기본 축구 기술 트레이닝\n" +
                            "미니 게임 및 포지션 연습으로 실전 감각 익히기",
                    program_2 = "경기 프로그램\n" +
                            "자체 리그 : 동아리 내 팀 간 리그전 진행\n" +
                            "외부 친선 경기 : 다른 대학 클럽 또는 지역 풋살 팀과 교류 경기",
                    program_3 = "추가 활동\n" +
                            "축구 퀴즈나 소규모 이벤트로 팀워크 강화\n" +
                            "시즌 종료 후 시상식(MVP, 베스트 팀플레이어 등)"
                )
            ),
            ClubData(
                clubName = "Hit & Hustle",
                shortTitle = "흥미진진한 야구",
                shortIntroduction = "매주 경기를 통해 흥미진진한 시간을 보낼 수 있는 모임!",
                date = "매주 목요일",
                time = "오후 5시",
                location = "한강공원",
                needs = "개인 글러브 및 운동복 (기본 장비 제공)",
                cost = "월 20,000원",
                details = ClubDetailsData(
                    clubName = "Hit & Hustle",
                    clubIntroduction = "Hit & Hustle은 야구를 사랑하는 대학생들이 모여 팀워크와 경쟁의 즐거움을 경험할 수 있어요. 초보부터 경험자까지 모두 환영! 실력을 키우는 동시에 친구들과의 소통을 통해 협동심과 리더십을 배울 수 있어요. 함께 하실래요?",
                    program_1 = "포지션별 트레이닝\n" +
                            "초보자도 쉽게 배울 수 있도록 포지션별 트레이닝 제공",
                    program_2 = "경기 전략 워크숍\n" +
                            "팀 단위로 전략을 분석하고 경기력을 높이는 시간",
                    program_3 = ""
                )
            ),
            ClubData(
                clubName = "Breathe",
                shortTitle = "마음을 다스리는 요가 타임",
                shortIntroduction = "차분하게 정리하는 시간! 개운한 몸뿐만 아니라 평안한 마음까지 얻게 되는!",
                date = "월, 목, 토",
                time = "20:00-22:00",
                location = "실내 체육관",
                needs = "농구화, 운동복",
                cost = "월 35,000원",
                details = ClubDetailsData(
                    clubName = "Breathe",
                    clubIntroduction = "Breathe는 바쁜 대학 생활 속에서 신체와 마음의 균형을 찾아갑니다. 초보자도 쉽게 따라 할 수 있는 커리큘럼부터 고급 레벨의 요가까지, 모든 참가자가 자신의 페이스에 맞춰 성장할 수 있습니다. 건강과 힐링을 동시에 누리고 싶다면 지금 같이 숨쉬러 가실래요?",
                    program_1 = "레벨별 커리큘럼\n"
                            +"초보자용 기초 수업부터 고급 수업까지 맞춤형 강의 (월요일, 수요일로 구분)\n",
                    program_2 = "명상과 다도 시간\n" +
                            "요가 후 마음의 안정과 집중력을 키우는 시간\n",
                    program_3 = "특별 세션\n" +
                            "힐링 요가, 일출 요가 등 한 달에 한 번 자연 속 야외 활동\n"
                )
            )
        )

        db.beginTransaction()
        try {
            for (club in clubs) {
                // tb_club에 데이터 삽입
                clubDao.insertClub(
                    club.clubName,
                    club.shortTitle,
                    club.shortIntroduction,
                    club.date,
                    club.time,
                    club.location,
                    club.needs,
                    club.cost
                )

                // tb_club_details에 데이터 삽입
                clubDetailsDao.insertClubDetails(
                    club.clubName,
                    club.details.clubIntroduction,
                    club.details.program_1,
                    club.details.program_2,
                    club.details.program_3
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    data class ClubData(
        val clubName: String,
        val shortTitle: String,
        val shortIntroduction: String,
        val date: String,
        val time: String,
        val location: String,
        val needs: String,
        val cost: String,
        val details: ClubDetailsData
    )

    data class ClubDetailsData(
        val clubName: String,
        val clubIntroduction: String,
        val program_1: String,
        val program_2: String,
        val program_3: String
    )
}
