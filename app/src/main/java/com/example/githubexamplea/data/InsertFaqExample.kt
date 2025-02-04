package com.example.githubexamplea.data

import android.database.sqlite.SQLiteDatabase
import com.example.githubexamplea.dao.FaqDao
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InsertFaqExample(private val db: SQLiteDatabase) {

    suspend fun insertSampleFaqs() {
        val faqDao = FaqDao(db)

        // 초기 faq 데이터
        val faqs = listOf(
            FaqData("uuu", "Kicks & Dreams", "축구를 처음 해봐도 괜찮을까요?", "물론입니다! 초보자를 위한 기본 트레이닝이 준비되어 있습니다.", getCurrentTime(), getCurrentDate()),
            FaqData("jun", "Kicks & Dreams", "참가비 외에 추가 비용이 있나요?", "추가 비용은 없으며, 개인 준비물만 챙겨주시면 됩니다.", getCurrentTime(), getCurrentDate()),
            FaqData("future", "Kicks & Dreams", "시간 맞추기 어려운 경우 어떻게 하나요?", "모든 일정 참여가 필수는 아니며, 가능한 일정에만 참여하셔도 됩니다.", getCurrentTime(), getCurrentDate()),
            FaqData("miso", "Hit & Hustle", "초보자도 참여할 수 있나요??", "네! 초보자부터 경험자까지 모두 환영합니다. 기본기 훈련부터 차근차근 배우실 수 있어요.", getCurrentTime(), getCurrentDate()),
            FaqData("future", "Hit & Hustle", "장비를 개인적으로 준비해야 하나요?", "글러브와 운동복은 개인 준비가 필요합니다. 배트와 보호 장비 등은 제공합니다.", getCurrentTime(), getCurrentDate()),
            FaqData("jun", "Breathe", "요가를 한 번도 해본 적이 없어도 괜찮을까요?", "물론입니다! 예약 시, 초보자를 위한 기본 수업에 참여해 주시면 됩니다.", getCurrentTime(), getCurrentDate()),
            FaqData("nare", "Breathe", "요가 매트를 제공해 주나요?", "개인 매트를 준비해 주셔야 하지만, 현장에서 대여도 가능합니다. (대여비 별도)", getCurrentTime(), getCurrentDate()),
            FaqData("lee", "Breathe", "몸이 뻣뻣한 사람도 참여할 수 있나요?", "요가는 유연성이 아니라 지속적인 연습을 통해 몸의 균형을 찾는 활동입니다. 누구나 가능해요!", getCurrentTime(), getCurrentDate())
        )

        // 코루틴을 사용하여 I/O 작업 (데이터베이스 작업) 실행
        withContext(Dispatchers.IO) {
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
        }
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.KOREA)
        return sdf.format(Date())
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
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
