package com.example.githubexamplea.data

import android.content.Context
import com.example.githubexamplea.dao.FaqDao
import com.example.githubexamplea.database.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InsertFaqExample(private val context: Context) {

    fun insertSampleFaqs() {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val faqDao = FaqDao(db)

        val faqs = listOf(
            FaqData(
                userId = "jun",
                clubName = "Kicks & Dreams",
                question = "축구를 처음 해봐도 괜찮을까요?",
                answer = "물론입니다! 초보자를 위한 기본 트레이닝이 준비되어 있습니다.",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            FaqData(
                userId = "future",
                clubName = "Kicks & Dreams",
                question = "참가비 외에 추가 비용이 있나요?",
                answer = "추가 비용은 없으며, 개인 준비물만 챙겨주시면 됩니다.",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            FaqData(
                userId = "nare",
                clubName = "Kicks & Dreams",
                question = "시간 맞추기 어려운 경우 어떻게 하나요?",
                answer = "모든 일정 참여가 필수는 아니며, 가능한 일정에만 참여하셔도 됩니다.",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            FaqData(
                userId = "uuu",
                clubName = "Hit & Hustle",
                question = "초보자도 참여할 수 있나요??",
                answer = "네! 초보자부터 경험자까지 모두 환영합니다. 기본기 훈련부터 차근차근 배우실 수 있어요.",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            FaqData(
                userId = "nare",
                clubName = "Hit & Hustle",
                question = "장비를 개인적으로 준비해야 하나요?",
                answer = "글러브와 운동복은 개인 준비가 필요합니다. 배트와 보호 장비 등은 제공합니다.",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            FaqData(
                userId = "future",
                clubName = "Breathe",
                question = "요가를 한 번도 해본 적이 없어도 괜찮을까요?",
                answer = "물론입니다! 예약 시, 초보자를 위한 기본 수업에 참여해 주시면 됩니다.",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            FaqData(
                userId = "jun",
                clubName = "Breathe",
                question = "요가 매트를 제공해 주나요?",
                answer = "개인 매트를 준비해 주셔야 하지만, 현장에서 대여도 가능합니다. (대여비 별도)",
                time = getCurrentTime(),
                date = getCurrentDate()
            ),
            FaqData(
                userId = "miso",
                clubName = "Breathe",
                question = "몸이 뻣뻣한 사람도 참여할 수 있나요?",
                answer = "요가는 유연성이 아니라 지속적인 연습을 통해 몸의 균형을 찾는 활동입니다. 누구나 가능해요! 대학생들이면 거의 다 가지고 있는 거북목, 굳은 허리 등 해결하러 오세요!",
                time = getCurrentTime(),
                date = getCurrentDate()
            )
        )

        db.beginTransaction()
        try {
            for (faq in faqs) {
                faqDao.insertFaq(
                    faq.userId,
                    faq.clubName,
                    faq.question,
                    faq.answer,
                    faq.time,
                    faq.date
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

    data class FaqData(
        val userId: String,
        val clubName: String,
        val question: String,
        val answer: String,
        val time: String,
        val date: String
    )
}
