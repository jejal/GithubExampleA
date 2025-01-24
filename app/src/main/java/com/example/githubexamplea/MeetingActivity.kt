package com.example.githubexamplea

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MeetingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 스플래시 스크린 적용
        //val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)

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

        // ActionBar가 있는지 확인하고 제목 변경
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)  // 뒤로가기 버튼 활성화
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)  // 커스텀 아이콘 적용
        }

        val btnSelectDate = findViewById<Button>(R.id.btn_apply)
        btnSelectDate.setOnClickListener {
            // CalendarActivity로 이동
            val intent = Intent(this, CalendarActivity::class.java)
            intent.putExtra("TITLE", "신청하기")  // "신청하기" 값을 전달
            startActivity(intent)
        }

        // 스플래시 스크린 유지 여부 설정 (예: 로딩 조건)
        //splashScreen.setKeepOnScreenCondition {
            //false  // false가 되면 스플래시 종료
        //}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
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