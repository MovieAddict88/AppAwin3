package com.foodday.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodday.app.adapter.BannerAdapter
import com.foodday.app.adapter.CategoryAdapter
import com.foodday.app.adapter.ProductAdapter
import com.foodday.app.adapter.RestaurantAdapter
import com.foodday.app.data.model.Product
import com.foodday.app.data.model.Resource
import com.foodday.app.databinding.FragmentHomeBinding
import com.foodday.app.ui.product.ProductDetailsActivity
import com.foodday.app.ui.restaurant.RestaurantDetailsActivity
import com.foodday.app.ui.search.SearchActivity
import com.foodday.app.viewmodel.CartViewModel
import com.foodday.app.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is null")
    
    private val viewModel: HomeViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    
    private var bannerAdapter: BannerAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var productAdapter: ProductAdapter? = null
    private var restaurantAdapter: RestaurantAdapter? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding!!.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        if (_binding == null) return
        
        setupAdapters()
        setupClickListeners()
        observeViewModel()
        
        viewModel.loadHomeData()
        cartViewModel.getCart()
    }
    
    private fun setupAdapters() {
        if (_binding == null || !isAdded) return
        try {
            // Banner Adapter
            bannerAdapter = BannerAdapter {
                // Handle banner click
            }
            _binding?.viewPagerBanners?.adapter = bannerAdapter
            _binding?.dotsIndicator?.attachTo(_binding?.viewPagerBanners!!)
            
            // Category Adapter
            categoryAdapter = CategoryAdapter { category ->
                try {
                    val intent = Intent(requireContext(), SearchActivity::class.java).apply {
                        putExtra("category_id", category.id)
                        putExtra("category_name", category.name)
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            _binding?.rvCategories?.layoutManager = 
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            _binding?.rvCategories?.adapter = categoryAdapter
            
            // Product Adapter
            productAdapter = ProductAdapter(
                onProductClick = { product ->
                    navigateToProductDetails(product)
                },
                onAddToCart = { product ->
                    addToCart(product)
                }
            )
            _binding?.rvPopular?.layoutManager = 
                GridLayoutManager(requireContext(), 2)
            _binding?.rvPopular?.adapter = productAdapter
            
            // Restaurant Adapter
            restaurantAdapter = RestaurantAdapter { restaurant ->
                try {
                    val intent = Intent(requireContext(), RestaurantDetailsActivity::class.java).apply {
                        putExtra("restaurant_id", restaurant.id)
                    }
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            _binding?.rvRestaurants?.layoutManager = 
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            _binding?.rvRestaurants?.adapter = restaurantAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun setupClickListeners() {
        _binding?.cvSearch?.setOnClickListener {
            try {
                startActivity(Intent(requireContext(), SearchActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        _binding?.tvSeeAllCategories?.setOnClickListener {
            // Navigate to all categories
        }
        
        _binding?.tvSeeAllRestaurants?.setOnClickListener {
            // Navigate to all restaurants
        }
        
        _binding?.tvSeeAllPopular?.setOnClickListener {
            try {
                startActivity(Intent(requireContext(), SearchActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        _binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.loadHomeData()
        }
    }
    
    private fun observeViewModel() {
        viewModel.banners.observe(viewLifecycleOwner) { resource ->
            if (_binding == null || !isAdded) return@observe
            try {
                when (resource) {
                    is Resource.Success -> {
                        bannerAdapter?.submitList(resource.data)
                        _binding?.shimmerBanners?.isVisible = false
                    }
                    is Resource.Error -> {
                        _binding?.shimmerBanners?.isVisible = false
                    }
                    is Resource.Loading -> {
                        _binding?.shimmerBanners?.isVisible = true
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) { resource ->
            if (_binding == null || !isAdded) return@observe
            try {
                when (resource) {
                    is Resource.Success -> {
                        categoryAdapter?.submitList(resource.data)
                        _binding?.shimmerCategories?.isVisible = false
                    }
                    is Resource.Error -> {
                        _binding?.shimmerCategories?.isVisible = false
                    }
                    is Resource.Loading -> {
                        _binding?.shimmerCategories?.isVisible = true
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModel.featuredProducts.observe(viewLifecycleOwner) { resource ->
            if (_binding == null || !isAdded) return@observe
            try {
                when (resource) {
                    is Resource.Success -> {
                        productAdapter?.submitList(resource.data)
                        _binding?.shimmerProducts?.isVisible = false
                    }
                    is Resource.Error -> {
                        _binding?.shimmerProducts?.isVisible = false
                    }
                    is Resource.Loading -> {
                        _binding?.shimmerProducts?.isVisible = true
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewModel.featuredRestaurants.observe(viewLifecycleOwner) { resource ->
            if (_binding == null || !isAdded) return@observe
            try {
                when (resource) {
                    is Resource.Success -> {
                        restaurantAdapter?.submitList(resource.data)
                        _binding?.shimmerRestaurants?.isVisible = false
                    }
                    is Resource.Error -> {
                        _binding?.shimmerRestaurants?.isVisible = false
                    }
                    is Resource.Loading -> {
                        _binding?.shimmerRestaurants?.isVisible = true
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        cartViewModel.addToCartState.observe(viewLifecycleOwner) { state ->
            if (_binding == null || !isAdded) return@observe
            try {
                when (state) {
                    is Resource.Success -> {
                        if (state.data) {
                            Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun navigateToProductDetails(product: Product) {
        try {
            val intent = Intent(requireContext(), ProductDetailsActivity::class.java).apply {
                putExtra("product_id", product.id)
            }
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun addToCart(product: Product) {
        try {
            cartViewModel.addToCart(product.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        bannerAdapter = null
        categoryAdapter = null
        productAdapter = null
        restaurantAdapter = null
    }
}
