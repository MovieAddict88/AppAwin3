package com.foodday.app.ui.orders

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.foodday.app.adapter.OrderAdapter
import com.foodday.app.data.model.Order
import com.foodday.app.data.model.Resource
import com.foodday.app.databinding.FragmentOrdersBinding
import com.foodday.app.ui.order.OrderDetailsActivity
import com.foodday.app.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {
    
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is null")
    
    private val viewModel: OrderViewModel by viewModels()
    private var orderAdapter: OrderAdapter? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return _binding!!.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        if (_binding == null) return
        
        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
        
        viewModel.getOrders()
    }
    
    private fun setupRecyclerView() {
        if (_binding == null || !isAdded) return
        try {
            orderAdapter = OrderAdapter { order ->
                navigateToOrderDetails(order)
            }
            _binding?.rvOrders?.layoutManager = LinearLayoutManager(requireContext())
            _binding?.rvOrders?.adapter = orderAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun setupClickListeners() {
        _binding?.btnBrowseMenu?.setOnClickListener {
            // Navigate to home
        }
        
        _binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.getOrders()
        }
    }
    
    private fun observeViewModel() {
        viewModel.orders.observe(viewLifecycleOwner) { resource ->
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
                        if (resource.data.isEmpty()) {
                            _binding?.layoutEmpty?.isVisible = true
                            _binding?.rvOrders?.isVisible = false
                        } else {
                            _binding?.layoutEmpty?.isVisible = false
                            _binding?.rvOrders?.isVisible = true
                            orderAdapter?.submitList(resource.data)
                        }
                    }
                    is Resource.Error -> {
                        _binding?.progressBar?.isVisible = false
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
    
    private fun navigateToOrderDetails(order: Order) {
        try {
            val intent = Intent(requireContext(), OrderDetailsActivity::class.java).apply {
                putExtra("order_id", order.id)
            }
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        orderAdapter = null
    }
}
