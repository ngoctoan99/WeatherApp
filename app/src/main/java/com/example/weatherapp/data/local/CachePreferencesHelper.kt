package com.example.weatherapp.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

open class CachePreferencesHelper @Inject constructor(context: Context) {

    companion object {
        private const val PREF_PACKAGE_NAME = "com.example.weatherapp"
        private const val PREF_DATA_WEATHER = "data_language"


    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    var dataWeather: String
        get() = preferences.getString(PREF_DATA_WEATHER, "").toString()
        set(token) = preferences.edit().putString(PREF_DATA_WEATHER, token).apply()

    fun clearCache() {
        preferences.edit().clear().apply()
    }

    fun SharedPreferences.Editor.putDouble(key: String, double: Double) =
        putLong(key, java.lang.Double.doubleToRawLongBits(double))

    fun SharedPreferences.getDouble(key: String, default: Double) =
        java.lang.Double.longBitsToDouble(getLong(key, java.lang.Double.doubleToRawLongBits(default)))
}
