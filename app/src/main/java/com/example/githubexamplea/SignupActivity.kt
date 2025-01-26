package com.example.githubexamplea

import android.app.AlertDialog
import android.content.Intent
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
                // 여기에 회원가입 로직을 구현합니다.
                // 예: 입력된 정보의 유효성 검사, 데이터베이스에 사용자 정보 저장 등

                // 회원가입 완료 다이얼로그 표시
                showSignupCompletedDialog()
            } else {
                showErrorMessage()
            }
        }
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

    private fun showErrorMessage() {
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
}
