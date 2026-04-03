package com.foodday.app.ui.favorites

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
import com.foodday.app.adapter.ProductAdapter
import com.foodday.app.data.model.Product
import com.foodday.app.data.model.Resource
import com.foodday.app.databinding.FragmentFavoritesBinding
import com.foodday.app.ui.product.ProductDetailsActivity
import com.foodday.app.viewmodel.CartViewModel
import com.foodday.app.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is null")
    
    private val viewModel: FavoriteViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private var productAdapter: ProductAdapter? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return _binding!!.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        if (_binding == null) return
        
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }
    
    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
    }
    
    private fun setupRecyclerView() {
        if (_binding == null || !isAdded) return
        try {
            productAdapter = ProductAdapter(
                onProductClick = { product ->
                    navigateToProductDetails(product)
                },
                onAddToCart = { product ->
                    cartViewModel.addToCart(product.id)
                }
            )
            _binding?.rvFavorites?.layoutManager = GridLayoutManager(requireContext(), 2)
            _binding?.rvFavorites?.adapter = productAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun setupClickListeners() {
        _binding?.btnBrowseMenu?.setOnClickListener {
            // Navigate to home
        }
        
        _binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.getFavorites()
        }
    }
    
    private fun observeViewModel() {
        viewModel.favorites.observe(viewLifecycleOwner) { resource ->
            if (_binding == null || !isAdded) return@observe
            try {
                _binding?.swipeRefresh?.isRefreshing = false
                when (resource) {
                    is Resource.Loading -> {
                        _binding?.progressBar?.isVisible = true
                        _binding?.layoutEmpty?.isVisible = false
                    }
                    is Resource.Success -> {
                        _binding?.progressBar?.isVisible = false
                        val favorites = resource.data

                        if (favorites.isEmpty()) {
                            _binding?.layoutEmpty?.isVisible = true
                            _binding?.rvFavorites?.isVisible = false
                        } else {
                            _binding?.layoutEmpty?.isVisible = false
                            _binding?.rvFavorites?.isVisible = true

                            // Convert favorites to products
                            val products = favorites.map { favorite ->
                                Product(
                                    id = favorite.productId,
                                    restaurantId = 0,
                                    name = favorite.displayName,
                                    imageUrl = favorite.displayImage,
                                    price = favorite.displayPrice ?: 0.0,
                                    hasVariants = favorite.productHasVariants
                                )
                            }
                            productAdapter?.submitList(products)
                        }
                    }
                    is Resource.Error -> {
                        _binding?.progressBar?.isVisible = false
                        if (isAdded) {
                            Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {
                        _binding?.progressBar?.isVisible = false
                    }
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
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        productAdapter = null
    }
}
