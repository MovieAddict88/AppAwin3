package com.foodday.app.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.foodday.app.R
import com.foodday.app.data.repository.SessionManager
import com.foodday.app.ui.auth.LoginActivity
import com.foodday.app.ui.main.MainActivity
import com.foodday.app.ui.onboarding.OnboardingActivity
import com.foodday.app.ui.rider.RiderMainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    
    @Inject
    lateinit var sessionManager: SessionManager
    
    private var isNavigationStarted = false
    private val handler = Handler(Looper.getMainLooper())
    private val navigationRunnable = Runnable { navigateToNextScreen() }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check if sessionManager is injected properly
        if (!::sessionManager.isInitialized) {
            Log.e("SplashActivity", "SessionManager not initialized!")
            // Delay and retry
            Handler(Looper.getMainLooper()).postDelayed({
                if (!isFinishing) {
                    recreate()
                }
            }, 500)
            return
        }
        
        try {
            setContentView(R.layout.activity_splash)
            Log.d("SplashActivity", "Content view set successfully")
        } catch (e: Exception) {
            Log.e("SplashActivity", "Error setting content view", e)
            // Continue anyway - the theme has a windowBackground
        }
        
        // Use Handler instead of coroutine for more reliability
        handler.postDelayed(navigationRunnable, 1500)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(navigationRunnable)
    }
    
    private fun navigateToNextScreen() {
        if (isNavigationStarted || isFinishing || isDestroyed) return
        
        // Double check sessionManager is initialized
        if (!::sessionManager.isInitialized) {
            Log.e("SplashActivity", "SessionManager not initialized in navigateToNextScreen")
            navigateToLogin()
            return
        }
        
        try {
            isNavigationStarted = true
            
            val isOnboardingCompleted = try {
                sessionManager.isOnboardingCompleted()
            } catch (e: Exception) {
                Log.e("SplashActivity", "Error checking onboarding", e)
                false
            }
            
            val isLoggedIn = try {
                sessionManager.isLoggedIn()
            } catch (e: Exception) {
                Log.e("SplashActivity", "Error checking login", e)
                false
            }
            
            Log.d("SplashActivity", "isOnboardingCompleted=$isOnboardingCompleted, isLoggedIn=$isLoggedIn")
            
            when {
                !isOnboardingCompleted -> {
                    Log.d("SplashActivity", "Navigating to Onboarding")
                    startActivitySafe(Intent(this, OnboardingActivity::class.java))
                }
                isLoggedIn -> {
                    val user = try {
                        sessionManager.getUser()
                    } catch (e: Exception) {
                        Log.e("SplashActivity", "Error getting user", e)
                        null
                    }
                    
                    if (user != null) {
                        Log.d("SplashActivity", "Logged in as ${user.role}")
                        if (user.role == "rider") {
                            startActivitySafe(Intent(this, RiderMainActivity::class.java))
                        } else {
                            startActivitySafe(Intent(this, MainActivity::class.java))
                        }
                    } else {
                        Log.d("SplashActivity", "Logged in but user is null")
                        try {
                            sessionManager.clearSession()
                        } catch (e: Exception) {
                            Log.e("SplashActivity", "Error clearing session", e)
                        }
                        startActivitySafe(Intent(this, LoginActivity::class.java))
                    }
                }
                else -> {
                    Log.d("SplashActivity", "Not logged in, navigating to Login")
                    startActivitySafe(Intent(this, LoginActivity::class.java))
                }
            }
            
            // Ensure finish is called on main thread
            handler.post { finishSafe() }
        } catch (e: Exception) {
            Log.e("SplashActivity", "Error navigating to next screen", e)
            e.printStackTrace()
            navigateToLogin()
        }
    }
    
    private fun startActivitySafe(intent: Intent) {
        try {
            if (!isFinishing && !isDestroyed) {
                startActivity(intent)
                Log.d("SplashActivity", "Started activity: ${intent.component}")
            }
        } catch (e: Exception) {
            Log.e("SplashActivity", "Error starting activity", e)
            e.printStackTrace()
            // Try login as fallback
            try {
                if (!isFinishing && !isDestroyed) {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            } catch (e2: Exception) {
                Log.e("SplashActivity", "Error starting login fallback", e2)
            }
        }
    }
    
    private fun finishSafe() {
        try {
            if (!isFinishing && !isDestroyed) {
                finish()
            }
        } catch (e: Exception) {
            Log.e("SplashActivity", "Error finishing activity", e)
        }
    }
    
    private fun navigateToLogin() {
        if (isFinishing || isDestroyed) return
        
        isNavigationStarted = true
        startActivitySafe(Intent(this, LoginActivity::class.java))
        handler.post { finishSafe() }
    }
}
