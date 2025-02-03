package com.example.githubexamplea

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete)

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

        // 전달된 날짜 및 시간 정보 가져오기
        val selectedDate = intent.getStringExtra("SELECTED_DATE") ?: "날짜 선택 안됨"
        val selectedTime = intent.getStringExtra("SELECTED_TIME")?.replace("\n", " ") ?: "선택된 시간 없음"
        val location = intent.getStringExtra("location") ?: "장소 정보 없음"
        val needs = intent.getStringExtra("needs") ?: "준비물 정보 없음"

        // TextView에 설정
        val dateTextView = findViewById<TextView>(R.id.tvSelectedDate)
        val timeTextView = findViewById<TextView>(R.id.tvSelectedTime)
        val locationTextView = findViewById<TextView>(R.id.tvLocation)
        val needsTextView = findViewById<TextView>(R.id.tvNeeds)
        dateTextView.text = "$selectedDate"
        timeTextView.text = "$selectedTime"
        locationTextView.text = "$location"
        needsTextView.text = "$needs"

        val btnConfirm = findViewById<Button>(R.id.btn_confirm)
        btnConfirm.setOnClickListener {
            // MainActivity로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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