package com.example.githubexamplea

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowInsetsController
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubexamplea.adapter.MeetingAdapter
import com.example.githubexamplea.adapter.FavoriteAdapter
import com.example.githubexamplea.database.DatabaseHelper
import com.example.githubexamplea.model.MeetingItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.githubexamplea.utils.SharedPreferencesHelper

class MyActivity : AppCompatActivity() {
    private lateinit var totalLikeTextView: TextView
    private lateinit var totalApplyTextView: TextView
    private lateinit var textUnivMajor: TextView
    private lateinit var  totalReviewTextView: TextView

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

        val textUserName = findViewById<TextView>(R.id.textUserName) // 사용자 이름 표시
        val btnLogout = findViewById<TextView>(R.id.btnLogout) // 로그아웃 버튼

        // 저장된 사용자 이름 가져와서 표시
        textUserName.text = SharedPreferencesHelper.getUserName(this)

        btnLogout.setOnClickListener {
            // 사용자 정보 삭제 후 로그인 화면으로 이동
            SharedPreferencesHelper.logout(this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        totalLikeTextView = findViewById(R.id.totalLike)
        totalApplyTextView = findViewById(R.id.totalApply)
        totalReviewTextView = findViewById(R.id.totalReview)
        updateClubCounts()

        textUnivMajor = findViewById(R.id.textUnivMajor)
        updateUserUniversityAndMajor()
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
        val appliedMeetingsList = getAppliedMeetings().toMutableList()
        val favoriteMeetingsList = getFavoriteMeetings().toMutableList()
        val dbHelper = DatabaseHelper(this)
        val userId = SharedPreferencesHelper.getUserId(this) ?: "guest"

        // 신청한 모임 RecyclerView 설정
        findViewById<RecyclerView>(R.id.rvAppliedMeetings).apply {
            layoutManager = LinearLayoutManager(this@MyActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = MeetingAdapter(this@MyActivity, appliedMeetingsList) {
                updateClubCounts() // 신청 취소 시 숫자 즉시 업데이트
            }
        }

        // 찜한 모임 RecyclerView 설정
        findViewById<RecyclerView>(R.id.rvFavoriteMeetings).apply {
            layoutManager = LinearLayoutManager(this@MyActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = FavoriteAdapter(favoriteMeetingsList, dbHelper, userId) {
                updateClubCounts() // 찜 취소 시 숫자 즉시 업데이트
            }
        }
    }

    // 신청한 모임 목록
    private fun getAppliedMeetings(): List<MeetingItem> {
        val meetingList = mutableListOf<MeetingItem>()
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val userId = SharedPreferencesHelper.getUserId(this)

        val cursor = db.rawQuery(
            """
            SELECT c.photo_path, a.club_name, c.short_introduction, a.date, a.time
            FROM tb_application AS a
            JOIN tb_club AS c ON a.club_name = c.club_name
            WHERE a.id = ?
            """.trimIndent(), arrayOf(userId)
        )

        while (cursor.moveToNext()) {
            val imagePath = cursor.getString(0) ?: ""
            val title = cursor.getString(1)
            val description = cursor.getString(2)
            val date = cursor.getString(3)
            val time = cursor.getString(4)

            meetingList.add(MeetingItem(imagePath, title, description, date, time))
        }

        cursor.close()
        return meetingList
    }

    // 찜한 모임 목록
    private fun getFavoriteMeetings(): List<MeetingItem> {
        val favoriteList = mutableListOf<MeetingItem>()
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val userId = SharedPreferencesHelper.getUserId(this)

        val cursor = db.rawQuery(
            """
        SELECT c.photo_path, l.club_name, c.short_introduction
        FROM tb_like AS l
        JOIN tb_club AS c ON l.club_name = c.club_name
        WHERE l.id = ?
        """.trimIndent(), arrayOf(userId)
        )

        while (cursor.moveToNext()) {
            val imagePath = cursor.getString(0) ?: ""  // 내부 저장소의 클럽 이미지 경로
            val title = cursor.getString(1)
            val description = cursor.getString(2)

            favoriteList.add(MeetingItem(imagePath, title, description, "", "")) // 날짜, 시간 필요 없음
        }

        cursor.close()
        return favoriteList
    }

    private fun updateUserUniversityAndMajor() {
        val dbHelper = DatabaseHelper(this)
        val userId = SharedPreferencesHelper.getUserId(this) ?: return

        val cursor = dbHelper.readableDatabase.rawQuery(
            """
            SELECT university, major
            FROM tb_user
            WHERE id = ?
            """.trimIndent(), arrayOf(userId)
        )

        if (cursor.moveToFirst()) {
            val university = cursor.getString(0) ?: "대학 정보 없음"
            val major = cursor.getString(1) ?: "전공 정보 없음"

            // 대학과 전공을 "대학교 / 전공" 형태로 표시 (슬래시 양옆 공백 유지)
            textUnivMajor.text = "$university / $major"
        }

        cursor.close()
    }

    private fun updateClubCounts() {
        val dbHelper = DatabaseHelper(this)
        val userId = SharedPreferencesHelper.getUserId(this) ?: return

        val likeCursor = dbHelper.readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM tb_like WHERE id = ?", arrayOf(userId)
        )
        if (likeCursor.moveToFirst()) {
            val likeCount = likeCursor.getInt(0)
            totalLikeTextView.text = likeCount.toString()
        }
        likeCursor.close()

        val applyCursor = dbHelper.readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM tb_application WHERE id = ?", arrayOf(userId)
        )
        if (applyCursor.moveToFirst()) {
            val applyCount = applyCursor.getInt(0)
            totalApplyTextView.text = applyCount.toString()
        }
        applyCursor.close()

        val reviewCursor = dbHelper.readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM tb_review WHERE id = ?", arrayOf(userId)
        )
        if (reviewCursor.moveToFirst()) {
            val reviewCount = reviewCursor.getInt(0)
            totalReviewTextView.text = reviewCount.toString()
        }
        reviewCursor.close()
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
