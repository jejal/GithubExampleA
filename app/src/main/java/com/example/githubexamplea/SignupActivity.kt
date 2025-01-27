package com.example.githubexamplea

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.githubexamplea.database.DatabaseHelper
import android.os.Handler
import android.view.WindowInsetsController
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignupActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var idInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var nameInput: EditText
    private lateinit var birthdayInput: EditText
    private lateinit var contactInput: EditText
    private lateinit var schoolInput: EditText
    private lateinit var majorInput: EditText
    private lateinit var signUpButton: Button
    private lateinit var errorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // DatabaseHelper 초기화
        dbHelper = DatabaseHelper(this)

        // 테이블 존재 여부 확인
        if (!dbHelper.isTableExists()) {
            showErrorMessage("데이터베이스 초기화에 실패했습니다.")
            return
        }

        // 입력란 초기화
        idInput = findViewById(R.id.idInput)
        passwordInput = findViewById(R.id.passwordInput)
        nameInput = findViewById(R.id.nameInput)
        birthdayInput = findViewById(R.id.birthdayInput)
        contactInput = findViewById(R.id.contactInput)
        schoolInput = findViewById(R.id.schoolInput)
        majorInput = findViewById(R.id.majorInput)
        signUpButton = findViewById(R.id.signUpButton)
        errorText = findViewById(R.id.errorText)

        // 가입 정보 * 텍스트 설정
        val joinInfoText = findViewById<TextView>(R.id.joinInfoText)
        val joinSpannable = SpannableString("가입 정보 *")
        joinSpannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.dark_green)),
            joinSpannable.length - 1,
            joinSpannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        joinInfoText.text = joinSpannable

        // 기타 정보 * 텍스트 설정
        val etcInfoText = findViewById<TextView>(R.id.etcInfoText)
        val etcSpannable = SpannableString("기타 정보 *")
        etcSpannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.dark_green)),
            etcSpannable.length - 1,
            etcSpannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        etcInfoText.text = etcSpannable

        // 회원가입 버튼 클릭 리스너 설정
        signUpButton.setOnClickListener {
            if (allFieldsFilled()) {
                val userId = idInput.text.toString()

                // 아이디 중복 체크
                if (dbHelper.isIdExists(userId)) {
                    showErrorMessage("이미 존재하는 아이디입니다.")
                    return@setOnClickListener
                }

                try {
                    // 데이터베이스에 사용자 정보 저장
                    val result = dbHelper.addUser(
                        userId,
                        passwordInput.text.toString(),
                        nameInput.text.toString(),
                        birthdayInput.text.toString(),
                        contactInput.text.toString(),
                        schoolInput.text.toString(),
                        majorInput.text.toString()
                    )

                    if (result != -1L) {
                        // 회원가입 완료 다이얼로그 표시
                        showSignupCompletedDialog()
                    } else {
                        // 데이터베이스 저장 실패
                        showErrorMessage("회원가입에 실패했습니다. 다시 시도해주세요.")
                    }
                } catch (e: Exception) {
                    showErrorMessage("데이터베이스 오류가 발생했습니다: ${e.message}")
                }
            } else {
                showErrorMessage()
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

    private fun allFieldsFilled(): Boolean {
        return idInput.text.isNotEmpty() &&
                passwordInput.text.isNotEmpty() &&
                nameInput.text.isNotEmpty() &&
                birthdayInput.text.isNotEmpty() &&
                contactInput.text.isNotEmpty() &&
                schoolInput.text.isNotEmpty() &&
                majorInput.text.isNotEmpty()
    }

    private fun showErrorMessage(message: String? = null) {
        if (message != null) {
            errorText.text = message
            errorText.visibility = View.VISIBLE
        } else {
            val emptyFields = mutableListOf<String>()

            if (idInput.text.isEmpty()) emptyFields.add("아이디")
            if (passwordInput.text.isEmpty()) emptyFields.add("비밀번호")
            if (nameInput.text.isEmpty()) emptyFields.add("이름")
            if (birthdayInput.text.isEmpty()) emptyFields.add("생년월일")
            if (contactInput.text.isEmpty()) emptyFields.add("연락처")
            if (schoolInput.text.isEmpty()) emptyFields.add("학교")
            if (majorInput.text.isEmpty()) emptyFields.add("학과/전공")

            if (emptyFields.isNotEmpty()) {
                val errorMessage = "${emptyFields.joinToString(", ")}(을)를 입력해 주세요"
                errorText.text = errorMessage
                errorText.visibility = View.VISIBLE
            }
        }
    }

    private fun showSignupCompletedDialog() {
        AlertDialog.Builder(this)
            .setTitle("회원가입 완료")
            .setMessage("잠시후 로그인 화면으로 이동합니다.")
            .setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
                moveToLoginActivity()
            }
            .setCancelable(false)
            .show()

        Handler(Looper.getMainLooper()).postDelayed({
            moveToLoginActivity()
        }, 3000)
    }

    private fun moveToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
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
