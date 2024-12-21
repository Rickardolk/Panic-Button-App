package com.example.panicbutton.model

import android.content.Context

object Preference {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_ONBOARDING_SHOW = "onboarding_show"

    fun isOnboardingShow(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_ONBOARDING_SHOW, false)
    }

    fun setOnboardingShow(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_ONBOARDING_SHOW, true)
            .apply()
    }

    //simpan session login
    fun saveLoginSession(context: Context, email: String, role: String) {
        val preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        preferences.edit()
            .putString("user_email", email)
            .putString("user_role", role) // save role user atau admin
            .putBoolean("is_logged_in", true)
            .apply()
    }

    //cek user login
    fun isUserLoggedIn(context: Context): Boolean {
        val preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return preferences.getBoolean("is_logged_in", false)
    }

    //user biasa atau admin
    fun getUserRole(context: Context): String {
        val preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return preferences.getString("user_role", "user") ?: "user"
    }

    //fun logout dan hapus session login
    fun logout(context: Context) {
        val preferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return preferences.edit()
            .clear()
            .apply()
    }
}