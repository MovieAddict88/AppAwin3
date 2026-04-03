package com.foodday.app.ui.product

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.foodday.app.R
import com.foodday.app.data.model.Resource
import com.foodday.app.databinding.ActivityProductDetailsBinding
import com.foodday.app.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProductDetailsBinding
    private val viewModel: ProductViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val productId = intent.getIntExtra("product_id", 0)
        
        binding.btnBack.setOnClickListener { finish() }
        
        viewModel.getProductById(productId)
        observeViewModel()
    }
    
    private fun observeViewModel() {
        viewModel.productDetail.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val product = resource.data
                    binding.apply {
                        tvProductName.text = product.name
                        tvDescription.text = product.description
                        tvPrice.text = getString(R.string.price_format, product.displayPrice.toString())
                        
                        Glide.with(ivProduct)
                            .load(product.imageUrl)
                            .into(ivProduct)
                    }
                }
                is Resource.Error -> {
                    // Handle error
                }
                is Resource.Loading -> {
                    // Show loading
                }
                else -> {}
            }
        }
    }
}
