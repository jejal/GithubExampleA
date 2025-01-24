package com.example.githubexamplea

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.example.githubexamplea.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 12(API 31) 이상에서는 WindowInsetsController로 상태바 설정
            val windowInsetsController = window.insetsController
            windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        } else {
            // Lollipop(API 21) 이상에서는 기존 방식 사용
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        }

        // 최상위 레이아웃에 WindowInsets 적용
        applyWindowInsetsToRootView()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back) // 커스텀 아이콘 적용
        }

        //달력
        //val tvDate = findViewById<TextView>(R.id.tvDate)
        val calendarView = findViewById<MaterialCalendarView>(R.id.calendarView)

        // 현재 날짜를 "yyyy.MM" 형식으로 표시
        //val currentDate = Calendar.getInstance().time
        //val dateFormat = SimpleDateFormat("yyyy.MM", Locale.getDefault())
        //tvDate.text = dateFormat.format(currentDate)

        // 공휴일 및 토요일 색상 적용
        calendarView.addDecorators(
            HolidayDecorator(),
            SaturdayDecorator(),
            SundayDecorator()
        )

        // 캘린더의 월 제목 포맷을 변경 (January 2025 -> 2025.01)
        calendarView.setTitleFormatter { day ->
            val formattedMonth = String.format("%04d.%02d", day.year, day.month)
            formattedMonth
        }

        // 시간 선택 색상 변화
        val timeOption1 = findViewById<Button>(R.id.timeOption1)
        val timeOption2 = findViewById<Button>(R.id.timeOption2)

        val timeButtons = listOf(timeOption1, timeOption2)

        for (button in timeButtons) {
            button.setOnClickListener {
                timeButtons.forEach { it.isSelected = false }
                button.isSelected = true
                button.setTextColor(Color.WHITE)
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()  // 현재 액티비티 종료하고 이전으로 돌아가기
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class HolidayDecorator : DayViewDecorator {
        private val holidays = setOf(
            CalendarDay.from(2025, 1, 1),
            CalendarDay.from(2025, 1, 28),
            CalendarDay.from(2025, 1, 29),
            CalendarDay.from(2025, 1, 30),
            CalendarDay.from(2025, 3, 1),
            CalendarDay.from(2025, 3, 3),
            CalendarDay.from(2025, 5, 5),
            CalendarDay.from(2025, 5, 6),
            CalendarDay.from(2025, 8, 15),
            CalendarDay.from(2025, 10, 3),
            CalendarDay.from(2025, 10, 9),
            CalendarDay.from(2025, 12, 25)
        )

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return holidays.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.RED)) // 공휴일을 빨간색으로
        }
    }

    class SaturdayDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            val calendar = Calendar.getInstance()
            calendar.set(day.year, day.month - 1, day.day)
            return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.BLUE)) // 토요일을 파란색으로
        }
    }

    class SundayDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            val calendar = Calendar.getInstance()
            calendar.set(day.year, day.month -1, day.day)
            return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.RED)) // 일요일을 빨간색으로
        }
    }

    private fun applyWindowInsetsToRootView() {
        val rootView = findViewById<View>(android.R.id.content) // 최상위 레이아웃 가져오기
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBarsInsets.left,   // 왼쪽 패딩
                systemBarsInsets.top,    // 상태바 높이
                systemBarsInsets.right,  // 오른쪽 패딩
                systemBarsInsets.bottom  // 하단 네비게이션 바 높이
            )
            insets
        }
    }
}
