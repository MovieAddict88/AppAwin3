package com.foodday.app.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.foodday.app.adapter.CartItemAdapter
import com.foodday.app.data.model.CartResponse
import com.foodday.app.data.model.Resource
import com.foodday.app.databinding.FragmentCartBinding
import com.foodday.app.ui.main.MainActivity
import com.foodday.app.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class CartFragment : Fragment() {
    
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is null")
    
    private val viewModel: CartViewModel by viewModels()
    private var cartItemAdapter: CartItemAdapter? = null
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
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
        viewModel.getCart()
    }
    
    private fun setupRecyclerView() {
        if (_binding == null || !isAdded) return
        cartItemAdapter = CartItemAdapter(
            onQuantityChange = { cartItem, quantity ->
                viewModel.updateCartItem(cartItem.id, quantity)
            },
            onRemove = { cartItem ->
                viewModel.removeCartItem(cartItem.id)
            }
        )
        _binding?.rvCartItems?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        _binding?.rvCartItems?.adapter = cartItemAdapter
    }
    
    private fun setupClickListeners() {
        _binding?.btnBrowseMenu?.setOnClickListener {
            // Navigate to home
        }
        
        _binding?.btnCheckout?.setOnClickListener {
            try {
                startActivity(Intent(requireContext(), CheckoutActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        _binding?.btnClearCart?.setOnClickListener {
            showClearCartDialog()
        }
    }
    
    private fun showClearCartDialog() {
        if (!isAdded) return
        try {
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Clear Cart")
                .setMessage("Are you sure you want to clear your cart?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.clearCart()
                }
                .setNegativeButton("No", null)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun observeViewModel() {
        viewModel.cartState.observe(viewLifecycleOwner) { resource ->
            if (_binding == null || !isAdded) return@observe
            try {
                when (resource) {
                    is Resource.Loading -> {
                        _binding?.progressBar?.isVisible = true
                    }
                    is Resource.Success -> {
                        _binding?.progressBar?.isVisible = false
                        val cartResponse = resource.data

                        if (cartResponse.items.isNullOrEmpty()) {
                            showEmptyCart()
                        } else {
                            showCart(cartResponse)
                        }

                        // Update badge in main activity
                        (activity as? MainActivity)?.updateCartBadge(cartResponse.count)
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
    }
    
    private fun showEmptyCart() {
        if (_binding == null) return
        _binding?.layoutEmpty?.isVisible = true
        _binding?.layoutCart?.isVisible = false
        _binding?.layoutCheckout?.isVisible = false
    }
    
    private fun showCart(cartResponse: CartResponse) {
        if (_binding == null) return
        _binding?.layoutEmpty?.isVisible = false
        _binding?.layoutCart?.isVisible = true
        _binding?.layoutCheckout?.isVisible = true
        
        cartResponse.items?.let {
            cartItemAdapter?.submitList(it)
        }
        
        // Update totals
        _binding?.tvSubtotal?.text = currencyFormat.format(cartResponse.subtotal)
        _binding?.tvServiceFee?.text = currencyFormat.format(cartResponse.serviceFee)
        
        cartResponse.deliveryFee?.let { fee ->
            _binding?.tvDeliveryFee?.text = if (cartResponse.freeDeliveryApplied) "Free" else currencyFormat.format(fee)
        } ?: run {
            _binding?.tvDeliveryFee?.text = "Calculated at checkout"
        }
        
        val total = cartResponse.subtotal + cartResponse.serviceFee + (cartResponse.deliveryFee ?: 0.0)
        _binding?.tvTotal?.text = currencyFormat.format(total)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cartItemAdapter = null
    }
}
