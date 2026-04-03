package com.foodday.app.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.foodday.app.R
import com.foodday.app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            // Use post to ensure the view hierarchy is attached before setting up navigation
            binding.root.post {
                try {
                    setupBottomNavigation()
                } catch (e: Exception) {
                    android.util.Log.e("MainActivity", "Error in setupBottomNavigation", e)
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Error in onCreate", e)
        }
    }
    
    private fun setupBottomNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        val navController = navHostFragment?.navController
        
        if (navController != null) {
            binding.bottomNav.setupWithNavController(navController)
        }
        
        // Add badge for cart
        val cartBadge = binding.bottomNav.getOrCreateBadge(R.id.nav_cart)
        cartBadge.isVisible = false
        
        // Handle reselection
        binding.bottomNav.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Scroll to top or perform action
                }
            }
        }
    }
    
    fun updateCartBadge(count: Int) {
        val badge = binding.bottomNav.getOrCreateBadge(R.id.nav_cart)
        if (count > 0) {
            badge.isVisible = true
            badge.number = count
        } else {
            badge.isVisible = false
        }
    }
}
