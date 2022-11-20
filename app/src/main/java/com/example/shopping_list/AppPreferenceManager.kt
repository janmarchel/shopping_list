package com.example.shopping_list

import android.content.Context
import android.content.SharedPreferences


class AppPreferenceManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)
    }

    var darkModeState: Boolean
        get() = sharedPreferences.getBoolean("darkMode", false)
        set(enable) {
            val editor = sharedPreferences.edit()
            editor.putBoolean("darkMode", enable)
            editor.apply()
        }
}