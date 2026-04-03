package com.foodday.app.ui.review

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foodday.app.databinding.ActivityReviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityReviewBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnBack.setOnClickListener { finish() }
    }
}
