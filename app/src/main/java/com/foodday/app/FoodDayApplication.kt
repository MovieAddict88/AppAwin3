package com.foodday.app

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodDayApplication : Application() {
    
    companion object {
        private const val TAG = "FoodDayApplication"
        lateinit var instance: FoodDayApplication
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.d(TAG, "Application onCreate called")
        
        // Initialize Firebase manually with fallback
        initializeFirebase()
    }
    
    private fun initializeFirebase() {
        try {
            // Check if Firebase is already initialized
            if (FirebaseApp.getApps(this).isEmpty()) {
                Log.d(TAG, "Firebase not initialized, attempting manual initialization")
                
                // Try to initialize with default config
                try {
                    FirebaseApp.initializeApp(this)
                    Log.d(TAG, "Firebase initialized successfully with default config")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to initialize Firebase with default config", e)
                    
                    // Fallback: Initialize with minimal config to prevent crash
                    try {
                        val options = FirebaseOptions.Builder()
                            .setApplicationId("1:381191062114:android:ff0497c29225114a5f873b")
                            .setApiKey("AIzaSyAx1XjH01YVJD9uMPRKH4y8mbidzNTqaN4")
                            .setProjectId("foody-df40b")
                            .build()
                        FirebaseApp.initializeApp(this, options)
                        Log.d(TAG, "Firebase initialized with fallback config")
                    } catch (e2: Exception) {
                        Log.e(TAG, "Failed to initialize Firebase with fallback config", e2)
                    }
                }
            } else {
                Log.d(TAG, "Firebase already initialized")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Critical error initializing Firebase", e)
            e.printStackTrace()
            // Continue without Firebase - app should still work
        }
    }
}