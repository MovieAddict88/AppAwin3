package com.foodday.app.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.foodday.app.databinding.ActivityAddressesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddressesActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAddressesBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnBack.setOnClickListener { finish() }
    }
}
