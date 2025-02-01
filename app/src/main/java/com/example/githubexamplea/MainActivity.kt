package com.example.githubexamplea

import android.content.Intent
import android.net.Uri
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
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.githubexamplea.adapter.ActivityAdapter
import com.example.githubexamplea.adapter.HostAdapter
import com.example.githubexamplea.adapter.RecommendedAdapter
import com.example.githubexamplea.data.InsertApplicationExample
import com.example.githubexamplea.data.InsertClubExample
import com.example.githubexamplea.data.InsertFaqExample
import com.example.githubexamplea.data.InsertLeaderExample
import com.example.githubexamplea.data.InsertReviewExample
import com.example.githubexamplea.data.InsertUserExample
import com.example.githubexamplea.database.DatabaseHelper
import com.example.githubexamplea.model.ActivityItem
import com.example.githubexamplea.model.HostItem
import com.example.githubexamplea.model.RecommendedItem
import com.example.githubexamplea.utils.SharedPreferencesHelper
import java.io.File

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

        // tb_user 데이터 삽입
        val inserter = InsertUserExample(this)
        inserter.insertMultipleUsers()
        // tb_leader 데이터 삽입
        val leaderInserter = InsertLeaderExample(this)
        leaderInserter.insertLeaders()
        // tb_club, tb_club_details 데이터 삽입
        val clubInserter = InsertClubExample(this)
        clubInserter.insertClubsWithDetails()
        // tb_review 데이터 삽입
        val reviewInserter = InsertReviewExample(this)
        reviewInserter.insertSampleReviews()
        // tb_faq 데이터 삽입
        val faqInserter = InsertFaqExample(this)
        faqInserter.insertSampleFaqs()
        // tb_application 데이터 삽입
        val appInserter = InsertApplicationExample(this)
        appInserter.insertSampleApplications()


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
        findViewById<RecyclerView>(R.id.rvActivities).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            activityAdapter = ActivityAdapter(itemWidth) { item ->
                navigateToMeetingActivity(item)
            }
            adapter = activityAdapter
        }

        // 신규 모임 리사이클러뷰 설정
        findViewById<RecyclerView>(R.id.rvNewMeetings).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            newMeetingsAdapter = ActivityAdapter(itemWidth) { item ->
                navigateToMeetingActivity(item)
            }
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
            deadlineAdapter = ActivityAdapter(itemWidth) { item ->
                navigateToMeetingActivity(item)
            }
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

    private fun navigateToMeetingActivity(item: ActivityItem) {
        val allowedClubs = listOf("Kicks & Dreams", "Hit & Hustle", "Breathe")

        if (allowedClubs.contains(item.clubName)) {
            val intent = Intent(this, MeetingActivity::class.java).apply {
                putExtra("club_name", item.clubName)
                putExtra("image", item.image)
            }
            startActivity(intent)
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
        val dbHelper = DatabaseHelper(this)

        activityAdapter.submitList(getActivityData(dbHelper))
        newMeetingsAdapter.submitList(getNewMeetingsData(dbHelper))
        hostAdapter.submitList(getHostData(dbHelper))
        deadlineAdapter.submitList(getDeadlineData(dbHelper))
        recommendedAdapter.submitList(getRecommendedData(dbHelper))
        applyHighRunnerData(dbHelper)
    }

    private fun getActivityData(db: DatabaseHelper): List<ActivityItem> {
        val list = mutableListOf<ActivityItem>()
        val cursor = db.readableDatabase.rawQuery("SELECT club_name, short_title, short_introduction, photo_path FROM tb_club", null)

        while (cursor.moveToNext()) {
            val clubName = cursor.getString(0)
            val title = cursor.getString(1)
            val description = cursor.getString(2)
            val imagePath = cursor.getString(3) ?: ""

            list.add(ActivityItem(image = imagePath, title = title, description = description, rating = "현재 인원: 6", clubName = clubName))
        }

        cursor.close()
        return list
    }

    private fun getNewMeetingsData(db: DatabaseHelper): List<ActivityItem> {
        val list = mutableListOf<ActivityItem>()
        val cursor = db.readableDatabase.rawQuery(
            "SELECT club_name, short_title, short_introduction, photo_path FROM tb_club ORDER BY rowid DESC LIMIT 4", null)

        while (cursor.moveToNext()) {
            val clubName = cursor.getString(0)
            val title = cursor.getString(1)
            val description = cursor.getString(2)
            val imagePath = cursor.getString(3) ?: ""

            list.add(ActivityItem(image = imagePath, title = title, description = description, rating = "현재 인원: 6", clubName = clubName))
        }

        cursor.close()
        return list
    }

    private fun getHostData(db: DatabaseHelper): List<HostItem> {
        val dbInstance = db.readableDatabase
        val hostList = mutableListOf<HostItem>()

        val cursor = dbInstance.rawQuery(
            """
        SELECT leader.club_name, leader.id, user.name, leader.leader_introduction, leader.club_introduction, 
               leader.leader_career, leader.leader_photo_path
        FROM tb_leader AS leader
        JOIN tb_user AS user ON leader.id = user.id
        WHERE leader.id IN ('miso', 'future', 'jun')
        ORDER BY 
            CASE leader.id
                WHEN 'miso' THEN 1
                WHEN 'future' THEN 2
                WHEN 'jun' THEN 3
            END
        """.trimIndent(), null
        )

        while (cursor.moveToNext()) {
            val clubName = cursor.getString(0)
            val id = cursor.getString(1)
            val userName = cursor.getString(2) // 유저의 한국어 이름 가져오기
            val intro = cursor.getString(3)
            val description = cursor.getString(4)
            val career = cursor.getString(5) ?: "정보 없음"
            val imagePath = cursor.getString(6) ?: ""

            val careerList = career.split("/").map { it.trim() }

            hostList.add(
                HostItem(
                    image = imagePath,
                    name = userName, // 한국어 이름을 `name`으로 저장!
                    intro = intro,
                    description = description,
                    infos = careerList
                )
            )
        }
        cursor.close()

        return hostList
    }

    private fun getHighRunnerData(db: DatabaseHelper): InsertClubExample.ClubData? {
        val cursor = db.readableDatabase.rawQuery(
            """
        SELECT club_name, short_title, short_introduction, photo_path
        FROM tb_club
        WHERE club_name = 'High Runner'
        """.trimIndent(), null
        )

        var clubData: InsertClubExample.ClubData? = null

        if (cursor.moveToFirst()) {
            val clubName = cursor.getString(0)
            val shortTitle = cursor.getString(1)
            val shortIntroduction = cursor.getString(2)
            val photoPath = cursor.getString(3) ?: ""

            clubData = InsertClubExample.ClubData(
                clubName = clubName,
                shortTitle = shortTitle,
                shortIntroduction = shortIntroduction,
                photoPath = photoPath
            )
        }
        cursor.close()
        return clubData
    }

    private fun applyHighRunnerData(db: DatabaseHelper) {
        val clubData = getHighRunnerData(db)

        if (clubData != null) {
            val marathonTitle = findViewById<TextView>(R.id.marathonTitle)
            val marathonDescription = findViewById<TextView>(R.id.marathonDescription)
            val marathonImage = findViewById<ImageView>(R.id.marathonImage)

            marathonTitle.text = clubData.shortTitle
            marathonDescription.text = clubData.shortIntroduction

            // Glide를 사용하여 이미지 로드
            if (!clubData.photoPath.isNullOrEmpty()) {
                val file = File(clubData.photoPath)
                if (file.exists()) {
                    Glide.with(this)
                        .load(Uri.fromFile(file))
                        .placeholder(R.drawable.img_banner_1)
                        .error(R.drawable.sorbet)
                        .into(marathonImage)
                } else {
                    marathonImage.setImageResource(R.drawable.img_banner_1)
                }
            } else {
                marathonImage.setImageResource(R.drawable.img_banner_1)
            }
        }
    }

    // 마감 임박 데이터 함수 추가
    private fun getDeadlineData(db: DatabaseHelper): List<ActivityItem> {
        return getActivityData(db) // 신규 모임도 동일한 데이터 사용
    }

    private fun getRecommendedData(db: DatabaseHelper): List<RecommendedItem> {
        val list = mutableListOf<RecommendedItem>()
        val cursor = db.readableDatabase.rawQuery(
            """
        SELECT club_name, short_introduction, photo_path 
        FROM tb_club 
        ORDER BY rowid DESC 
        LIMIT 8 OFFSET 1
        """.trimIndent(), null
        )

        while (cursor.moveToNext()) {
            val title = cursor.getString(0)
            val subtitle = cursor.getString(1)
            val imagePath = cursor.getString(2) ?: ""

            list.add(RecommendedItem(image = imagePath, title = title, subtitle = subtitle))
        }

        cursor.close()
        return list
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
