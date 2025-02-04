package com.example.githubexamplea.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesHelper {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"

    // SharedPreferences 객체 반환
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // 사용자 정보를 SharedPreferences에 저장
    fun saveUserInfo(context: Context, userId: String, userName: String) {
        val prefs = getPrefs(context)
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            apply()
        }
    }

    // 저장된 사용자 ID 가져오기 (없으면 null 반환)
    fun getUserId(context: Context): String? {
        return getPrefs(context).getString(KEY_USER_ID, null)
    }

    // 저장된 사용자 이름 가져오기 (없으면 null 반환)
    fun getUserName(context: Context): String? {
        return getPrefs(context).getString(KEY_USER_NAME, null)
    }

    // 로그인 상태 확인 (기본값: false)
    fun isLoggedIn(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // 로그아웃 (저장된 모든 데이터 삭제)
    fun logout(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().apply {
            clear()
            apply()
        }
    }
}
