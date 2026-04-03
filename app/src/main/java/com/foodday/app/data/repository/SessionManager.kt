package com.foodday.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.foodday.app.data.model.User
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    private val gson = Gson()
    
    companion object {
        private const val TAG = "SessionManager"
        private const val PREF_NAME = "FoodDayPrefs"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER = "user"
        private const val KEY_FIREBASE_TOKEN = "firebase_token"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
    
    fun saveAuthToken(token: String) {
        try {
            prefs.edit().putString(KEY_AUTH_TOKEN, token).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving auth token", e)
        }
    }
    
    fun getAuthToken(): String? {
        return try {
            prefs.getString(KEY_AUTH_TOKEN, null)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting auth token", e)
            null
        }
    }
    
    fun saveUser(user: User) {
        try {
            val userJson = gson.toJson(user)
            prefs.edit().putString(KEY_USER, userJson).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user", e)
        }
    }
    
    fun getUser(): User? {
        return try {
            val userJson = prefs.getString(KEY_USER, null)
            if (userJson != null) {
                try {
                    gson.fromJson(userJson, User::class.java)
                } catch (e: JsonSyntaxException) {
                    Log.e(TAG, "Error parsing user JSON", e)
                    // Clear corrupted data
                    prefs.edit().remove(KEY_USER).apply()
                    null
                }
            } else null
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user", e)
            null
        }
    }
    
    fun saveFirebaseToken(token: String) {
        try {
            prefs.edit().putString(KEY_FIREBASE_TOKEN, token).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving firebase token", e)
        }
    }
    
    fun getFirebaseToken(): String? {
        return try {
            prefs.getString(KEY_FIREBASE_TOKEN, null)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting firebase token", e)
            null
        }
    }
    
    fun setOnboardingCompleted(completed: Boolean) {
        try {
            prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error saving onboarding state", e)
        }
    }
    
    fun isOnboardingCompleted(): Boolean {
        return try {
            prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting onboarding state", e)
            false
        }
    }
    
    fun isLoggedIn(): Boolean {
        return try {
            getAuthToken() != null
        } catch (e: Exception) {
            Log.e(TAG, "Error checking login state", e)
            false
        }
    }
    
    fun clearSession() {
        try {
            prefs.edit().clear().apply()
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing session", e)
        }
    }
}
