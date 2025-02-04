package com.example.githubexamplea

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.githubexamplea.api.OpenWeatherApiClient
import com.example.githubexamplea.api.model.ForecastItem
import com.example.githubexamplea.database.DatabaseHelper
import com.example.githubexamplea.utils.SharedPreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MeetingActivity : AppCompatActivity() {
    private var isFavorite = false  // 찜 상태 추적
    private var location: String = ""
    private var needs: String = ""

    // DB에서 특정 clubName("Run & Run", "Climb City") 데이터 가져오기
    fun getClubData(db: DatabaseHelper, clubName: String): Triple<String, String, String?> {
        val cursor = db.readableDatabase.rawQuery(
            """
        SELECT short_title, short_introduction, photo_path
        FROM tb_club
        WHERE club_name = ?
        """.trimIndent(), arrayOf(clubName)
        )

        var title = "제목 없음"
        var intro = "소개 정보가 없습니다."
        var photoPath: String? = null

        if (cursor.moveToFirst()) {
            title = cursor.getString(0) ?: title
            intro = cursor.getString(1) ?: intro
            photoPath = cursor.getString(2) ?: null
        }
        cursor.close()

        return Triple(title, intro, photoPath)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)

        val clubName = intent.getStringExtra("club_name") ?: "모임 이름 없음"
        val imagePath = intent.getStringExtra("image") ?: ""

        val titleTextView = findViewById<TextView>(R.id.tv_event_title)
        val imageView = findViewById<ImageView>(R.id.image_event)
        val clubInfoTextView = findViewById<TextView>(R.id.club_info)
        val clubInfoTextView2 = findViewById<TextView>(R.id.club_info2)
        val clubIntroTextView = findViewById<TextView>(R.id.club_intro)
        val proTitle1 = findViewById<TextView>(R.id.club_protitle1)
        val program1 = findViewById<TextView>(R.id.club_program1)
        val proTitle2 = findViewById<TextView>(R.id.club_protitle2)
        val program2 = findViewById<TextView>(R.id.club_program2)
        val proTitle3 = findViewById<TextView>(R.id.club_protitle3)
        val program3 = findViewById<TextView>(R.id.club_program3)
        val question1 = findViewById<TextView>(R.id.club_question1)
        val answer1 = findViewById<TextView>(R.id.club_answer1)
        val question2 = findViewById<TextView>(R.id.club_question2)
        val answer2 = findViewById<TextView>(R.id.club_answer2)
        val question3 = findViewById<TextView>(R.id.club_question3)
        val answer3 = findViewById<TextView>(R.id.club_answer3)
        val review1 = findViewById<TextView>(R.id.club_review1)
        val review2 = findViewById<TextView>(R.id.club_review2)
        val review3 = findViewById<TextView>(R.id.club_review3)
        val hostNameTextView = findViewById<TextView>(R.id.club_hostName)
        val hostIntroTextView = findViewById<TextView>(R.id.club_hostIntro)
        val hostImageView = findViewById<ImageView>(R.id.club_hostImage)
        val image1View = findViewById<ImageView>(R.id.image1)
        val image1TitleView = findViewById<TextView>(R.id.image1_title)
        val image1IntroView = findViewById<TextView>(R.id.image1_intro)
        val peopleCount1 = findViewById<TextView>(R.id.people_count1)
        val image2View = findViewById<ImageView>(R.id.image2)
        val image2TitleView = findViewById<TextView>(R.id.image2_title)
        val image2IntroView = findViewById<TextView>(R.id.image2_intro)
        val peopleCount2 = findViewById<TextView>(R.id.people_count2)

        // 날씨 컨테이너
        val weatherContainer = findViewById<LinearLayout>(R.id.weather_forecast_container)
        fetchWeatherForecast(weatherContainer)

        // FAQ 초기화 (중복 방지)
        question1.text = ""
        answer1.text = ""
        question2.text = ""
        answer2.text = ""
        question3.text = ""
        answer3.text = ""

        question1.visibility = View.GONE
        answer1.visibility = View.GONE
        question2.visibility = View.GONE
        answer2.visibility = View.GONE
        question3.visibility = View.GONE
        answer3.visibility = View.GONE

        titleTextView.text = clubName

        if (!imagePath.isNullOrEmpty()) {
            val file = File(imagePath)
            if (file.exists()) {
                Glide.with(this)
                    .load(Uri.fromFile(file))
                    .placeholder(R.drawable.img_banner_1)
                    .error(R.drawable.sorbet)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.img_banner_1)
            }
        } else {
            imageView.setImageResource(R.drawable.img_banner_1)
        }

        // DB에서 해당 club_name에 대한 데이터를 조회
        val dbHelper = DatabaseHelper(this)
        val db = dbHelper.readableDatabase
        val userId = SharedPreferencesHelper.getUserId(this) ?: return
        // 해당 클럽 신청 인원수 불러오기
        updateParticipantCount(clubName)

        // 모임 정보
        val cursor = db.rawQuery(
            """
            SELECT date, time, location, needs, cost
            FROM tb_club
            WHERE club_name = ?
            """.trimIndent(), arrayOf(clubName)
        )

        if (cursor.moveToFirst()) {
            val date = cursor.getString(0) ?: ""
            val time = cursor.getString(1) ?: ""
            location = cursor.getString(2) ?: "장소 정보 없음"
            needs = cursor.getString(3) ?: "준비물 정보 없음"
            val cost = cursor.getString(4) ?: ""

            // club_info 텍스트뷰 설정
            val clubInfoText = """
                📅 $date $time
                📍 $location
                ✔ $needs
                💰 $cost
            """.trimIndent()

            clubInfoTextView.text = clubInfoText
            clubInfoTextView2.text = clubInfoText
        }
        cursor.close()

        val introCursor = db.rawQuery(
            """
            SELECT club_introduction
            FROM tb_club_details
            WHERE club_name = ?
            """.trimIndent(), arrayOf(clubName)
        )

        if (introCursor.moveToFirst()) {
            val clubIntroduction = introCursor.getString(0) ?: "소개 정보가 없습니다."
            clubIntroTextView.text = clubIntroduction
        }
        introCursor.close()

        // 모임 디테일 정보
        val detailsCursor = db.rawQuery(
            """
        SELECT club_introduction, program_1, program_2, program_3
        FROM tb_club_details
        WHERE club_name = ?
        """.trimIndent(), arrayOf(clubName)
        )

        if (detailsCursor.moveToFirst()) {
            val clubIntroduction = detailsCursor.getString(0) ?: "소개 정보가 없습니다."
            clubIntroTextView.text = clubIntroduction

            val rawProgram1 = detailsCursor.getString(1)
            val rawProgram2 = detailsCursor.getString(2)
            val rawProgram3 = detailsCursor.getString(3)

            if (!rawProgram1.isNullOrEmpty()) {
                val programParts = rawProgram1.split("\n", limit = 2)
                proTitle1.text = programParts[0] // 첫 번째 줄을 제목으로
                program1.text = programParts.getOrNull(1) ?: "" // 나머지 내용

            } else {
                proTitle1.visibility = View.GONE
                program1.visibility = View.GONE
            }

            if (!rawProgram2.isNullOrEmpty()) {
                val programParts = rawProgram2.split("\n", limit = 2)
                proTitle2.text = programParts[0]
                program2.text = programParts.getOrNull(1) ?: ""
            } else {
                proTitle2.visibility = View.GONE
                program2.visibility = View.GONE
            }

            if (!rawProgram3.isNullOrEmpty()) {
                val programParts = rawProgram3.split("\n", limit = 2)
                proTitle3.text = programParts[0]
                program3.text = programParts.getOrNull(1) ?: ""
            } else {
                proTitle3.visibility = View.GONE
                program3.visibility = View.GONE
            }
        }
        detailsCursor.close()

        // 모임 faq
        val faqCursor = db.rawQuery(
            """
        SELECT DISTINCT question, answer
        FROM tb_faq
        WHERE club_name = ?
        ORDER BY date DESC, time DESC
        LIMIT 3
        """.trimIndent(), arrayOf(clubName)
        )

        val faqList = mutableListOf<Pair<String, String>>()

        while (faqCursor.moveToNext()) {
            val question = faqCursor.getString(0) ?: ""
            val answer = faqCursor.getString(1) ?: ""
            faqList.add(question to answer)
        }
        faqCursor.close()

        if (faqList.isNotEmpty()) {
            if (faqList.size > 0) {
                question1.text = "Q. ${faqList[0].first}"
                answer1.text = "A. ${faqList[0].second}"
                question1.visibility = View.VISIBLE
                answer1.visibility = View.VISIBLE
            }
            if (faqList.size > 1) {
                question2.text = "Q. ${faqList[1].first}"
                answer2.text = "A. ${faqList[1].second}"
                question2.visibility = View.VISIBLE
                answer2.visibility = View.VISIBLE
            }
            if (faqList.size > 2) {
                question3.text = "Q. ${faqList[2].first}"
                answer3.text = "A. ${faqList[2].second}"
                question3.visibility = View.VISIBLE
                answer3.visibility = View.VISIBLE
            }
        }

        // 모임 리뷰
        val reviewCursor = db.rawQuery(
            """
        SELECT review
        FROM tb_review
        WHERE club_name = ?
        ORDER BY date DESC, time DESC
        LIMIT 3
        """.trimIndent(), arrayOf(clubName)
        )

        val reviewList = mutableListOf<String>()

        while (reviewCursor.moveToNext()) {
            val review = reviewCursor.getString(0) ?: ""
            reviewList.add(review)
        }
        reviewCursor.close()

        if (reviewList.size > 0) review1.text = reviewList[0]
        if (reviewList.size > 1) review2.text = reviewList[1]
        if (reviewList.size > 2) review3.text = reviewList[2]

        // 모임장 소개
        val hostCursor = db.rawQuery(
            """
        SELECT leader.id, leader.leader_introduction, leader.leader_photo_path, user.name
        FROM tb_leader AS leader
        JOIN tb_user AS user ON leader.id = user.id
        WHERE leader.club_name = ?
        """.trimIndent(), arrayOf(clubName)
        )

        if (hostCursor.moveToFirst()) {
            val leaderId = hostCursor.getString(0) ?: ""
            val leaderIntro = hostCursor.getString(1) ?: "소개 정보가 없습니다."
            val leaderPhotoPath = hostCursor.getString(2) ?: ""
            val leaderName = hostCursor.getString(3) ?: "모임장 정보 없음"

            hostNameTextView.text = leaderName
            hostIntroTextView.text = leaderIntro

            if (leaderPhotoPath.isNotEmpty()) {
                val file = File(leaderPhotoPath)
                if (file.exists()) {
                    Glide.with(this)
                        .load(Uri.fromFile(file))
                        .placeholder(R.drawable.img_banner_1)
                        .error(R.drawable.sorbet)
                        .into(hostImageView)
                } else {
                    hostImageView.setImageResource(R.drawable.img_banner_1)
                }
            } else {
                hostImageView.setImageResource(R.drawable.img_banner_1)
            }
        }
        hostCursor.close()

        // 유사한 모임
        val dbHelper2 = DatabaseHelper(this)

        val (runTitle, runIntro, runPhotoPath) = getClubData(dbHelper2, "Run & Run")
        val runParticipants = getParticipantCount(dbHelper2, "Run & Run")

        val (climbTitle, climbIntro, climbPhotoPath) = getClubData(dbHelper2, "Climb City")
        val climbParticipants = getParticipantCount(dbHelper2, "Climb City")

        image1TitleView.text = runTitle
        image1IntroView.text = runIntro
        peopleCount1.text = "현재 인원: $runParticipants"

        if (!runPhotoPath.isNullOrEmpty()) {
            val file = File(runPhotoPath)
            if (file.exists()) {
                Glide.with(this)
                    .load(Uri.fromFile(file))
                    .placeholder(R.drawable.img_banner_1)
                    .error(R.drawable.sorbet)
                    .into(image1View)
            } else {
                image1View.setImageResource(R.drawable.img_banner_1)
            }
        } else {
            image1View.setImageResource(R.drawable.img_banner_1)
        }

        image2TitleView.text = climbTitle
        image2IntroView.text = climbIntro
        peopleCount2.text = "현재 인원: $climbParticipants"

        if (!climbPhotoPath.isNullOrEmpty()) {
            val file = File(climbPhotoPath)
            if (file.exists()) {
                Glide.with(this)
                    .load(Uri.fromFile(file))
                    .placeholder(R.drawable.img_banner_1)
                    .error(R.drawable.sorbet)
                    .into(image2View)
            } else {
                image2View.setImageResource(R.drawable.img_banner_1)
            }
        } else {
            image2View.setImageResource(R.drawable.img_banner_1)
        }


        // 찜하기 버튼 가져오기
        val btnFavorite = findViewById<ImageButton>(R.id.btn_favorite)

        // 현재 사용자가 해당 클럽을 찜한 상태인지 조회
        isFavorite = checkFavoriteStatus(dbHelper, userId, clubName)

        // 찜한 상태를 UI에 반영
        btnFavorite.setImageResource(if (isFavorite) R.drawable.ic_like_red else R.drawable.ic_like_blank)

        // 찜 버튼 클릭 이벤트
        btnFavorite.setOnClickListener {
            isFavorite = !isFavorite

            // UI 업데이트
            btnFavorite.setImageResource(if (isFavorite) R.drawable.ic_like_red else R.drawable.ic_like_blank)

            // DB 업데이트 (찜 추가/삭제)
            updateFavoriteState(dbHelper, userId, clubName, isFavorite)
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
            val intent = Intent(this, CalendarActivity::class.java).apply {
                putExtra("TITLE", "신청하기")
                putExtra("club_name", clubName)
                putExtra("location", location)
                putExtra("needs", needs)
            }
            startActivity(intent)
        }
    }

    // 화면이 다시 보일 때 최신 신청 인원 업데이트
    override fun onResume() {
        super.onResume()
        val titleTextView = findViewById<TextView>(R.id.tv_event_title)
        val clubName = titleTextView.text.toString()

        updateParticipantCount(clubName)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // 찜하기 상태 체크
    private fun checkFavoriteStatus(dbHelper: DatabaseHelper, userId: String, clubName: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT 1 FROM tb_like WHERE id = ? AND club_name = ? LIMIT 1",
            arrayOf(userId, clubName)
        )

        val isLiked = cursor.moveToFirst() // 값이 있으면 true (찜한 상태)
        cursor.close()
        return isLiked
    }

    // 찜하기 상태 업데이트
    private fun updateFavoriteState(dbHelper: DatabaseHelper, userId: String, clubName: String, isLiked: Boolean) {
        val db = dbHelper.writableDatabase
        if (isLiked) {
            db.execSQL("INSERT OR IGNORE INTO tb_like (id, club_name) VALUES (?, ?)", arrayOf(userId, clubName))
        } else {
            db.execSQL("DELETE FROM tb_like WHERE id = ? AND club_name = ?", arrayOf(userId, clubName))
        }
    }

    // 신청 인원수 카운트
    private fun getParticipantCount(db: DatabaseHelper, clubName: String): Int {
        val cursor = db.readableDatabase.rawQuery(
            """
        SELECT COUNT(*) FROM tb_application WHERE club_name = ?
        """.trimIndent(), arrayOf(clubName)
        )

        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }

    // 신청 인원수 업데이트
    private fun updateParticipantCount(clubName: String) {
        val dbHelper = DatabaseHelper(this) // 매번 새 DB 인스턴스 생성 (최신 데이터 반영)
        val participantsTextView = findViewById<TextView>(R.id.tv_participants)

        // 최신 신청 인원 가져오기
        val participantCount = getParticipantCount(dbHelper, clubName)

        // UI 업데이트
        participantsTextView.text = "${participantCount}명 신청"
    }

    // OpenWeather 날씨 API 호출
    private fun fetchWeatherForecast(weatherContainer: LinearLayout) {
        val apiKey = BuildConfig.OPENWEATHER_API_KEY
        val cityName = "Seoul"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiResponse = OpenWeatherApiClient.service.getWeatherForecast(cityName, apiKey)

                if (!apiResponse.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MeetingActivity, "API 요청 실패 (Error: ${apiResponse.code()})", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val forecastData = apiResponse.body()
                if (forecastData != null) {
                    val forecastList = forecastData.list

                    val dailyForecastMap = mutableMapOf<String, ForecastItem>()
                    for (item in forecastList) {
                        val date = item.dateTime.split(" ")[0]  // YYYY-MM-DD 형식
                        dailyForecastMap[date] = item
                    }

                    val sortedKeys = dailyForecastMap.keys.sorted().take(4)

                    val daysLabel = listOf("오늘", "내일", "모레", "글피")

                    withContext(Dispatchers.Main) {
                        weatherContainer.removeAllViews()

                        for ((index, date) in sortedKeys.withIndex()) {
                            val forecast = dailyForecastMap[date] ?: continue

                            val forecastView = LayoutInflater.from(this@MeetingActivity)
                                .inflate(R.layout.item_weather_forecast, weatherContainer, false)

                            val weatherIcon = forecastView.findViewById<ImageView>(R.id.weather_icon)
                            val weatherDay = forecastView.findViewById<TextView>(R.id.weather_day)
                            val weatherDate = forecastView.findViewById<TextView>(R.id.weather_date)
                            val weatherTemp = forecastView.findViewById<TextView>(R.id.weather_temp)
                            val weatherFeelsLike = forecastView.findViewById<TextView>(R.id.weather_feels_like)
                            val weatherWind = forecastView.findViewById<TextView>(R.id.weather_wind)
                            val weatherHumidity = forecastView.findViewById<TextView>(R.id.weather_humidity)

                            val dateParts = date.split("-")
                            val formattedDate = "${dateParts[1]}.${dateParts[2]}" // MM.DD 형식 변환

                            weatherDay.text = daysLabel[index]  // 오늘, 내일, 모레, 글피
                            weatherDate.text = formattedDate

                            weatherTemp.text = "${forecast.main.temperature}°C"
                            weatherFeelsLike.text = "${forecast.main.feelsLike}°C"
                            weatherWind.text = "${forecast.wind.speed} m/s"
                            weatherHumidity.text = "${forecast.main.humidity}%"

                            val weatherDesc = forecast.weather[0].description
                            val iconRes = when (weatherDesc) {
                                "맑음" -> R.drawable.ic_sun
                                "구름조금", "구름많음" -> R.drawable.ic_cloud
                                "흐림", "매우흐림" -> R.drawable.ic_cloud2
                                "비", "약한비", "강한비", "매우강한비", "극심한비", "소나기", "이슬비", "약한이슬비", "강한이슬비" -> R.drawable.ic_rain
                                "약한눈", "눈", "강한눈", "진눈깨비", "약한진눈깨비", "강한진눈깨비", "소나기눈" -> R.drawable.ic_snow
                                else -> R.drawable.ic_cloud2
                            }
                            weatherIcon.setImageResource(iconRes)

                            weatherContainer.addView(forecastView)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MeetingActivity, "날씨 정보 불러오기 실패", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
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