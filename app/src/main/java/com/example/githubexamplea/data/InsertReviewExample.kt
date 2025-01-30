package com.example.githubexamplea.data

import android.content.Context
import com.example.githubexamplea.dao.ReviewDao
import com.example.githubexamplea.database.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InsertReviewExample(private val context: Context) {

    fun insertSampleReviews() {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val reviewDao = ReviewDao(db)

        val reviews = listOf(
            ReviewData(
                userId = "miso",
                clubName = "Kicks & Dreams",
                star = 4.5f,
                review = "축구 경험이 없었지만 너무 재미있고 땀 흘리는게 뿌듯했어요!",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            ReviewData(
                userId = "jun",
                clubName = "Kicks & Dreams",
                star = 4.5f,
                review = "여자 친구들끼리 모여 운동하니까 더 친밀해졌어요.",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            ReviewData(
                userId = "future",
                clubName = "Kicks & Dreams",
                star = 4.5f,
                review = "몸을 움직이니까 건강도 챙길 수 있어서 좋았어요!",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            ReviewData(
                userId = "jun",
                clubName = "Hit & Hustle",
                star = 5.0f,
                review = "야구를 전혀 몰랐지만 여기서 기본부터 배우며 정말 재미를 느꼈어요! 다른 대학생들과 친해지고, 대회에 나가서 승리했을 때는 정말 뿌듯했습니다.",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            ReviewData(
                userId = "future",
                clubName = "Hit & Hustle",
                star = 4.0f,
                review = "목요일 저녁 시간만큼은 스트레스가 사라지는 기분이에요. 팀워크를 통해 책임감도 배웠고, 무엇보다 너무 즐겁습니다.",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            ReviewData(
                userId = "nare",
                clubName = "Hit & Hustle",
                star = 4.0f,
                review = "친구들이랑 같이 야구도 하고 뒤풀이도 하면서 대학생활의 즐거움을 찾았어요. 단순히 스포츠 이상의 추억을 쌓을 수 있는 곳입니다!",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            ReviewData(
                userId = "uuu",
                clubName = "Breathe",
                star = 5.0f,
                review = "초급반에서 고급반으로 건너갈 수 있는 과정이 너무 뿌듯했어요!",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            ReviewData(
                userId = "nare",
                clubName = "Breathe",
                star = 5.0f,
                review = "특별 워크숍에서 배운 명상 기술은 시험 기간에도 큰 도움이 됐어요. 몸과 마음의 건강을 함께 챙길 수 있는 최고의 동아리!",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            ReviewData(
                userId = "future",
                clubName = "Breathe",
                star = 5.0f,
                review = "처음에는 낯설었지만, 요가가 점점 제 생활에 힐링을 주는 존재가 되었어요. 아침 세션으로 하루를 상쾌하게 시작할 수 있어서 너무 좋아요.",
                time = getCurrentTime(),
                date = getCurrentDate()
            )
        )

        db.beginTransaction()
        try {
            for (review in reviews) {
                reviewDao.insertReview(
                    review.userId,
                    review.clubName,
                    review.star,
                    review.review,
                    review.time,
                    review.date
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.KOREA)
        return sdf.format(Date())
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        return sdf.format(Date())
    }

    data class ReviewData(
        val userId: String,
        val clubName: String,
        val star: Float,
        val review: String,
        val time: String,
        val date: String
    )
}
