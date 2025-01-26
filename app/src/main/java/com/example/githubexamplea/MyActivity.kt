package com.example.githubexamplea

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubexamplea.adapter.MeetingAdapter
import com.example.githubexamplea.adapter.FavoriteAdapter
import com.example.githubexamplea.model.MeetingItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

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

        val actionBar = supportActionBar
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setCustomView(R.layout.custom_actionbar)

        setupBottomNavigation()
        setupRecyclerViews()
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // 선택된 아이콘의 색상 설정
        val colorStateList = resources.getColorStateList(R.color.bottom_nav_selector, null)
        bottomNav.itemIconTintList = colorStateList
        bottomNav.itemTextColor = colorStateList

        // 초기 선택 아이템 설정 (MY 아이콘을 초록색으로)
        bottomNav.selectedItemId = R.id.nav_profile

        // UI가 완전히 렌더링된 후 아이콘 강제 변경
        bottomNav.post {
            updateBottomNavIcons(R.id.nav_profile)
        }

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    updateBottomNavIcons(R.id.nav_home)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()  // MyActivity 종료
                    true
                }
                R.id.nav_list -> {
                    updateBottomNavIcons(R.id.nav_list)
                    true
                }
                R.id.nav_chat -> {
                    updateBottomNavIcons(R.id.nav_chat)
                    true
                }
                R.id.nav_favorite -> {
                    updateBottomNavIcons(R.id.nav_favorite)
                    true
                }
                R.id.nav_profile -> {
                    updateBottomNavIcons(R.id.nav_profile)
                    true
                }
                else -> false
            }
        }
    }

    // 네비게이션 아이콘 업데이트 함수
    private fun updateBottomNavIcons(selectedItemId: Int) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        val iconMap = mapOf(
            R.id.nav_home to Pair(R.drawable.ic_home, R.drawable.ic_home_blank),
            R.id.nav_list to Pair(R.drawable.ic_menu_blank, R.drawable.ic_menu_blank),
            R.id.nav_chat to Pair(R.drawable.ic_like_blank, R.drawable.ic_like_blank),
            R.id.nav_favorite to Pair(R.drawable.ic_chat_blank, R.drawable.ic_chat_blank),
            R.id.nav_profile to Pair(R.drawable.ic_my, R.drawable.ic_my_blank)
        )

        // 모든 아이콘을 초기 상태 (비활성)로 변경
        for ((id, icons) in iconMap) {
            bottomNav.menu.findItem(id).setIcon(icons.second) // 비활성 아이콘
        }

        // 선택된 아이템을 활성화 상태로 변경
        bottomNav.menu.findItem(selectedItemId).setIcon(iconMap[selectedItemId]?.first ?: return)
    }

    private fun setupRecyclerViews() {
        // 신청한 모임 데이터 설정
        val appliedMeetingsList = listOf(
            MeetingItem(R.drawable.img_banner_1, "수영", "초급자부터 중급자까지!"),
            MeetingItem(R.drawable.img_banner_1, "테니스", "전문가와 함께하는 수업"),
            MeetingItem(R.drawable.img_banner_1, "탁구", "재미있는 탁구 모임"),
            MeetingItem(R.drawable.img_banner_1, "축구", "열정을 불태우는 축구!")
        )

        // 찜한 모임 데이터 설정
        val favoriteMeetingsList = listOf(
            MeetingItem(R.drawable.img_banner_1, "수영", "즐거운 수영 모임"),
            MeetingItem(R.drawable.img_banner_1, "테니스", "건강한 아침 테니스"),
            MeetingItem(R.drawable.img_banner_1, "탁구", "탁구 마스터 도전"),
            MeetingItem(R.drawable.img_banner_1, "축구", "매주 토요일 정기 모임")
        )

        // 신청한 모임 RecyclerView 설정
        findViewById<RecyclerView>(R.id.rvAppliedMeetings).apply {
            layoutManager = LinearLayoutManager(this@MyActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = MeetingAdapter(appliedMeetingsList)
        }

        // 찜한 모임 RecyclerView 설정
        findViewById<RecyclerView>(R.id.rvFavoriteMeetings).apply {
            layoutManager = LinearLayoutManager(this@MyActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = FavoriteAdapter(favoriteMeetingsList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun applyWindowInsetsToRootView() {
        val rootView = findViewById<View>(android.R.id.content) // 최상위 레이아웃 가져오기
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // 기존 패딩 대신 하단 마진을 적용
            view.setPadding(
                systemBarsInsets.left,   // 왼쪽 패딩
                systemBarsInsets.top,    // 상태바 높이
                systemBarsInsets.right,  // 오른쪽 패딩
                0  // 하단 패딩 대신 0으로 설정
            )

            // 네비게이션 바 높이를 bottomNav에 적용
            findViewById<BottomNavigationView>(R.id.bottomNav).apply {
                setPadding(0, 0, 0, systemBarsInsets.bottom)
            }
            insets
        }
    }
}
