package com.foodday.app.ui.restaurant

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.foodday.app.databinding.ActivityRestaurantDetailsBinding
import com.foodday.app.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantDetailsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRestaurantDetailsBinding
    private val viewModel: ProductViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val restaurantId = intent.getIntExtra("restaurant_id", 0)
        
        binding.btnBack.setOnClickListener { finish() }
        
        // Load restaurant details
    }
}
