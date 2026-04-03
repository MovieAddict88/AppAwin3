package com.foodday.app.ui.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foodday.app.databinding.ActivityTrackOrderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackOrderActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityTrackOrderBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnBack.setOnClickListener { finish() }
    }
}
