package com.foodday.app.ui.rider

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foodday.app.databinding.ActivityRiderMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RiderMainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRiderMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiderMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
