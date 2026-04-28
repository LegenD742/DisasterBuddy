package com.devanshdroid.disasterbuddy.core.utils


import android.content.Context
import android.content.SharedPreferences
import com.devanshdroid.disasterbuddy.core.utils.Constants.KEY_FCM_TOKEN
import com.devanshdroid.disasterbuddy.core.utils.Constants.KEY_JWT_TOKEN
import com.devanshdroid.disasterbuddy.core.utils.Constants.KEY_USER_ID
import com.devanshdroid.disasterbuddy.core.utils.Constants.KEY_USER_NAME
import com.devanshdroid.disasterbuddy.core.utils.Constants.KEY_USER_ROLE
import com.devanshdroid.disasterbuddy.core.utils.Constants.PREF_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var jwtToken: String?
        get() = prefs.getString(KEY_JWT_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_JWT_TOKEN, value).apply()

    var userRole: String?
        get() = prefs.getString(KEY_USER_ROLE, null)
        set(value) = prefs.edit().putString(KEY_USER_ROLE, value).apply()

    var userId: String?
        get() = prefs.getString(KEY_USER_ID, null)
        set(value) = prefs.edit().putString(KEY_USER_ID, value).apply()

    var userName: String?
        get() = prefs.getString(KEY_USER_NAME, null)
        set(value) = prefs.edit().putString(KEY_USER_NAME, value).apply()

    var fcmToken: String?
        get() = prefs.getString(KEY_FCM_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_FCM_TOKEN, value).apply()

    fun isLoggedIn(): Boolean = jwtToken != null

    fun isCitizen(): Boolean = userRole == Constants.ROLE_CITIZEN

    fun isVolunteer(): Boolean = userRole == Constants.ROLE_VOLUNTEER

    fun clearAll() = prefs.edit().clear().apply()
}