package com.example.githubexamplea

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.githubexamplea.database.DatabaseHelper
import com.example.githubexamplea.utils.SharedPreferencesHelper
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ 자동 로그인 체크 (로그인 정보가 저장되어 있으면 바로 MainActivity 실행)
        if (SharedPreferencesHelper.isLoggedIn(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.idInput)
        val etPassword = findViewById<EditText>(R.id.passwordInput)
        val btnLogin = findViewById<Button>(R.id.loginButton)
        val btnSignUp = findViewById<TextView>(R.id.signUpButton)
        val errorText = findViewById<TextView>(R.id.errorText)

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // ✅ 입력값 검증
            if (username.isEmpty() || password.isEmpty()) {
                errorText.text = "아이디와 비밀번호를 입력해주세요."
                errorText.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // ✅ 비동기 로그인 (Coroutine 사용)
            GlobalScope.launch(Dispatchers.IO) {
                val isValid = dbHelper.validateUser(username, password)

                withContext(Dispatchers.Main) {
                    if (isValid) {
                        errorText.visibility = View.GONE

                        // ✅ 사용자 이름 가져와서 SharedPreferences 저장
                        val userName = dbHelper.getUserName(username)
                        SharedPreferencesHelper.saveUserInfo(this@LoginActivity, username, userName)

                        // ✅ MainActivity 실행
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // 로그인 실패 처리
                        errorText.text = "아이디 또는 비밀번호가 일치하지 않습니다."
                        errorText.visibility = View.VISIBLE
                    }
                }
            }
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
