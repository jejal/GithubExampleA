package com.example.githubexamplea

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.githubexamplea.adapter.ActivityAdapter
import com.example.githubexamplea.adapter.HostAdapter
import com.example.githubexamplea.adapter.RecommendedAdapter
import com.example.githubexamplea.model.ActivityItem
import com.example.githubexamplea.model.HostItem
import com.example.githubexamplea.model.RecommendedItem
import com.example.githubexamplea.utils.SharedPreferencesHelper

class MainActivity : AppCompatActivity() {
    private lateinit var activityAdapter: ActivityAdapter
    private lateinit var newMeetingsAdapter: ActivityAdapter
    private lateinit var deadlineAdapter: ActivityAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter
    private lateinit var hostAdapter: HostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        // 스플래시 스크린 적용
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // 자동 로그인 체크
        if (!SharedPreferencesHelper.isLoggedIn(this)) {
            // 로그인 상태가 아니면 LoginActivity로 이동
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

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

        setupRecyclerViews()
        setupBottomNavigation()
        loadData()

        // 스플래시 스크린 유지 여부 설정 (예: 로딩 조건)
        splashScreen.setKeepOnScreenCondition {
            false  // false가 되면 스플래시 종료
        }
    }

    private fun setupRecyclerViews() {
        // 화면 너비 구하기
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val itemWidth = (screenWidth - 48) / 2 // 패딩값을 고려하여 2등분

        // 액티비티 리사이클러뷰 설정
//        findViewById<RecyclerView>(R.id.rvActivities).apply {
//            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
//            activityAdapter = ActivityAdapter(itemWidth)
//            adapter = activityAdapter
//        }
        findViewById<RecyclerView>(R.id.rvActivities).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            activityAdapter = ActivityAdapter(itemWidth) { activityItem ->
                if (activityItem.image == R.drawable.img_banner_1) {
                    val intent = Intent(this@MainActivity, MeetingActivity::class.java).apply {
                        putExtra("title", activityItem.title)
                        putExtra("description", activityItem.description)
                        putExtra("image", activityItem.image)
                        putExtra("rating", activityItem.rating)
                    }
                    startActivity(intent)
                }
            }
            adapter = activityAdapter
        }

        // 신규 모임 리사이클러뷰 설정
        findViewById<RecyclerView>(R.id.rvNewMeetings).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            newMeetingsAdapter = ActivityAdapter(itemWidth) { _ -> }
            adapter = newMeetingsAdapter
        }

        // 호스트 프로필 리사이클러뷰 설정
        findViewById<RecyclerView>(R.id.rvHosts).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            hostAdapter = HostAdapter()
            adapter = hostAdapter
        }

        // 마감 임박 리사이클러뷰 설정
        findViewById<RecyclerView>(R.id.rvDeadline).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            deadlineAdapter = ActivityAdapter(itemWidth) { _ -> }
            adapter = deadlineAdapter
        }

        // 추천 모임 리사이클러뷰 설정
        findViewById<RecyclerView>(R.id.rvRecommended).apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2).apply {  // 2행으로 설정
                orientation = GridLayoutManager.HORIZONTAL  // 수평 방향으로 설정
            }
            this.layoutManager = layoutManager
            recommendedAdapter = RecommendedAdapter()
            adapter = recommendedAdapter
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // 선택된 아이콘의 색상 설정
        val colorStateList = resources.getColorStateList(R.color.bottom_nav_selector, null)
        bottomNav.itemIconTintList = colorStateList
        bottomNav.itemTextColor = colorStateList

        // 초기 선택 아이템 설정 (HOME 아이콘을 초록색으로)
        bottomNav.menu.findItem(R.id.nav_home).setIcon(R.drawable.ic_home)

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_home -> {
                    updateBottomNavIcons(R.id.nav_home)
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
                    val intent = Intent(this, MyActivity::class.java)
                    startActivity(intent)
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

    private fun loadData() {
        activityAdapter.submitList(getActivityData())
        newMeetingsAdapter.submitList(getNewMeetingsData())
        hostAdapter.submitList(getHostData())
        deadlineAdapter.submitList(getDeadlineData())
        recommendedAdapter.submitList(getRecommendedData())
    }

    private fun getActivityData(): List<ActivityItem> {
        return listOf(
            ActivityItem(
                image = R.drawable.img_banner_1,
                title = "아침을 깨우는 러닝 모드",
                description = "아침마다 캠퍼스 곳곳을 달리는 이 모임은 단순한 러닝 그 이상! 러닝 초보부터 마라토너까지 모두 환영!",
                rating = "9/10"
            ),
            ActivityItem(
                image = R.drawable.img_banner_1,
                title = "열정을 불태우는 축구 타임",
                description = "매주 쌓아올리는 팀워크! 초보라도 도전 정신을 가진 사람이라면 누구나 환영!",
                rating = "9/11"
            ),
            ActivityItem(
                image = R.drawable.img_banner_1,
                title = "테니스 가보자",
                description = "월 2회 등산 모임",
                rating = "7/12"
            )
        )
    }

    private fun getNewMeetingsData(): List<ActivityItem> {
        return listOf(
            ActivityItem(
                image = R.drawable.img_banner_1,
                title = "마음을 다스리는 요가 타임",
                description = "차분하게 정리하는 시간! 개운한 몸 뿐만 아니라 평안한 마음까지 얻게 되는!",
                rating = "6/10"
            ),
            ActivityItem(
                image = R.drawable.img_banner_1,
                title = "건강한 삶의 시작 수영",
                description = "초보자도 대환영! 기초부터 심화까지 자세하게! 행복한 취미 생활 뿐만 아니라 건강한 삶까지 얻을 수 있는 모임",
                rating = "8/10"
            ),
            ActivityItem(
                image = R.drawable.img_banner_1,
                title = "농구 동호회",
                description = "아침 수영으로 하루를 시작해요",
                rating = "7/10"
            )
        )
    }

    private fun getHostData(): List<HostItem> {
        return listOf(
            HostItem(
                image = R.drawable.profile_person_1,
                name = "김미소",
                intro = "안녕하세요.\nKicks & Dreams의 \n모임장 김미소 입니다.",
                description = "축구를 사랑하는 사람들의 모임",
                infos = listOf(
                    "Kicks & Dreams 모임장",
                    "Happy Soccer 모임장",
                    "나이키 마케터"
                )
            ),
            HostItem(
                image = R.drawable.profile_person_1,
                name = "이하늘",
                intro = "Running Crew의 모임장 \n이하늘 입니다.",
                description = "함께 달리며 건강한 삶을 만들어가요",
                infos = listOf(
                    "Running Crew 모임장",
                    "Marathon Club 운영진",
                    "체육 교육 전공"
                )
            )
        )
    }

    // 마감 임박 데이터 함수 추가
    private fun getDeadlineData(): List<ActivityItem> {
        return listOf(
            ActivityItem(
                image = R.drawable.img_banner_1,
                title = "열정을 불태우는 축구 타임",
                description = "매주 쌓아올리는 팀워크! 초보라도 도전 정신을 가진 사람이라면 누구나 환영!",
                rating = "9/10"
            ),
            ActivityItem(
                image = R.drawable.img_banner_1,
                title = "흥미진진한 야구",
                description = "매주 경기를 통해 흥미진진한 시간을 보낼 수 있는 모임!",
                rating = "8/10"
            ),
            ActivityItem(
                image = R.drawable.img_banner_1,
                title = "마감 임박 모임 3",
                description = "마감 임박 모임 설명 3",
                rating = "8/10"
            )
        )
    }

    private fun getRecommendedData(): List<RecommendedItem> {
        val data = listOf(
            RecommendedItem(
                image = R.drawable.img_banner_1,
                title = "헤이",
                subtitle = "배드민턴 초급자 부터 중급자 까지!"
            ),
            RecommendedItem(
                image = R.drawable.img_banner_1,
                title = "헤이",
                subtitle = "배드민턴 초급자 부터 중급자 까지!"
            ),
            RecommendedItem(
                image = R.drawable.img_banner_1,
                title = "헤이",
                subtitle = "배드민턴 초급자 부터 중급자 까지!"
            ),
            RecommendedItem(
                image = R.drawable.img_banner_1,
                title = "헤이",
                subtitle = "배드민턴 초급자 부터 중급자 까지!"
            ),
            RecommendedItem(
                image = R.drawable.img_banner_1,
                title = "헤이",
                subtitle = "배드민턴 초급자 부터 중급자 까지!"
            ),
            RecommendedItem(
                image = R.drawable.img_banner_1,
                title = "헤이",
                subtitle = "배드민턴 초급자 부터 중급자 까지!"
            )
        )

        // 데이터 크기를 로그로 출력 (디버깅 목적)
        Log.d("MainActivity", "Recommended data size: ${data.size}")

        return data
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
