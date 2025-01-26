package com.example.githubexamplea

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.githubexamplea.database.DatabaseHelper
import com.example.githubexamplea.R

class SignupActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

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
    }
}