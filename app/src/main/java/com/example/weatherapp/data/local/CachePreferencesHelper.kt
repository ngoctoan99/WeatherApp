package com.example.weatherapp.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

open class CachePreferencesHelper @Inject constructor(context: Context) {

    companion object {
        private const val PREF_PACKAGE_NAME = "com.wallet.oscar.preferences"
        private const val PREF_KEY_LANGUAGE_APP = "language_app"
        private const val PREF_KEY_ACCESS_TOKEN = "accessToken"
        private const val PREF_KEY_FCM_TOKEN = "fcm_token"
        private const val PREF_KEY_THE_FIRST_OPEN_APP = "the_first_open_app"
        private const val PREF_CODE_LANGUAGE = "code_language"


    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)
    var fcmToken: String
        get() = preferences.getString(PREF_KEY_FCM_TOKEN, "").toString()
        set(token) = preferences.edit().putString(PREF_KEY_FCM_TOKEN, token).apply()


    var languageApp: String
        get() = preferences.getString(PREF_KEY_LANGUAGE_APP, "").toString()
        set(token) = preferences.edit().putString(PREF_KEY_LANGUAGE_APP, token).apply()

    var languageCode: String
        get() = preferences.getString(PREF_CODE_LANGUAGE, "en").toString()
        set(code) = preferences.edit().putString(PREF_CODE_LANGUAGE, code).apply()


    var accessToken: String
        get() = preferences.getString(PREF_KEY_ACCESS_TOKEN, "").toString()
        set(token) = preferences.edit().putString(PREF_KEY_ACCESS_TOKEN, token).apply()

    var theFirstOpenApp: Boolean
        get() = preferences.getBoolean(PREF_KEY_THE_FIRST_OPEN_APP, true)
        set(isDarkMode) = preferences.edit().putBoolean(PREF_KEY_THE_FIRST_OPEN_APP, isDarkMode)
            .apply()

    fun clearCache() {
        preferences.edit().clear().apply()
    }

    fun clearCacheForLogout() {

        //   fcmToken=""
        accessToken = ""
    }

    fun SharedPreferences.Editor.putDouble(key: String, double: Double) =
        putLong(key, java.lang.Double.doubleToRawLongBits(double))

    fun SharedPreferences.getDouble(key: String, default: Double) =
        java.lang.Double.longBitsToDouble(getLong(key, java.lang.Double.doubleToRawLongBits(default)))
}
