package com.foodday.app.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.foodday.app.data.repository.SessionManager
import com.foodday.app.databinding.ActivityOnboardingBinding
import com.foodday.app.ui.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityOnboardingBinding
    
    @Inject
    lateinit var sessionManager: SessionManager
    
    private val onboardingItems = listOf(
        OnboardingItem(
            "Discover Delicious Food",
            "Explore a variety of cuisines from your favorite local restaurants",
            "onboarding_food"
        ),
        OnboardingItem(
            "Easy Ordering",
            "Order your favorite meals with just a few taps. It's that simple!",
            "onboarding_order"
        ),
        OnboardingItem(
            "Fast Delivery",
            "Get your food delivered hot and fresh to your doorstep in no time",
            "onboarding_delivery"
        ),
        OnboardingItem(
            "Track Your Order",
            "Real-time tracking from kitchen to your door. Never miss a delivery!",
            "onboarding_tracking"
        )
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityOnboardingBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            setupViewPager()
            setupButtons()
        } catch (e: Exception) {
            android.util.Log.e("OnboardingActivity", "Error in onCreate", e)
            finishOnboarding() // Fallback
        }
    }
    
    private fun setupViewPager() {
        val adapter = OnboardingAdapter(onboardingItems)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)
        
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtons(position)
            }
        })
    }
    
    private fun setupButtons() {
        binding.btnSkip.setOnClickListener {
            finishOnboarding()
        }
        
        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < onboardingItems.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                finishOnboarding()
            }
        }
        
        binding.btnGetStarted.setOnClickListener {
            finishOnboarding()
        }
    }
    
    private fun updateButtons(position: Int) {
        val isLastPage = position == onboardingItems.size - 1
        binding.btnNext.visibility = if (isLastPage) android.view.View.GONE else android.view.View.VISIBLE
        binding.btnGetStarted.visibility = if (isLastPage) android.view.View.VISIBLE else android.view.View.GONE
    }
    
    private fun finishOnboarding() {
        sessionManager.setOnboardingCompleted(true)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

data class OnboardingItem(
    val title: String,
    val description: String,
    val imageRes: String
)
