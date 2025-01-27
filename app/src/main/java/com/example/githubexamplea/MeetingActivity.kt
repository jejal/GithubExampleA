package com.example.githubexamplea

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MeetingActivity : AppCompatActivity() {
    private var isFavorite = false  // 찜 상태 추적

    override fun onCreate(savedInstanceState: Bundle?) {
        // 스플래시 스크린 적용
        //val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)

        // 찜하기 버튼 가져오기
        val btnFavorite = findViewById<ImageButton>(R.id.btn_favorite)

        // 초기 찜 상태 설정 (기본적으로 빈 하트)
        btnFavorite.setImageResource(if (isFavorite) R.drawable.ic_like_red else R.drawable.ic_like_blank)

        btnFavorite.setOnClickListener {
            // 상태 토글
            isFavorite = !isFavorite

            // 상태에 따라 아이콘 변경
            btnFavorite.setImageResource(
                if (isFavorite) R.drawable.ic_like_red
                else R.drawable.ic_like_blank
            )

            // 찜하기 상태 저장 로직 (추가 가능)
            saveFavoriteState(isFavorite)
        }

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

    private fun saveFavoriteState(isFav: Boolean) {
        // SharedPreferences를 사용하여 상태 저장 (앱 종료 후에도 상태 유지하려면 사용)
        val sharedPref = getSharedPreferences("favorite_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("meeting_favorite", isFav)
            apply()
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