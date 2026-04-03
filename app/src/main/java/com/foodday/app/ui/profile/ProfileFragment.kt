package com.foodday.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.foodday.app.R
import com.foodday.app.data.model.Resource
import com.foodday.app.databinding.FragmentProfileBinding
import com.foodday.app.ui.auth.LoginActivity
import com.foodday.app.ui.notifications.NotificationsActivity
import com.foodday.app.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding is null")
    
    private val viewModel: AuthViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return _binding!!.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        if (_binding == null) return
        
        setupClickListeners()
        observeViewModel()
        
        viewModel.getProfile()
    }
    
    private fun setupClickListeners() {
        _binding?.btnEditProfile?.setOnClickListener {
            try {
                startActivity(Intent(requireContext(), EditProfileActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        _binding?.menuMyAddresses?.root?.setOnClickListener {
            try {
                startActivity(Intent(requireContext(), AddressesActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        _binding?.menuPaymentMethods?.root?.setOnClickListener {
            // Navigate to payment methods
        }
        
        _binding?.menuOrderHistory?.root?.setOnClickListener {
            // Navigate to order history
        }
        
        _binding?.menuNotifications?.root?.setOnClickListener {
            try {
                startActivity(Intent(requireContext(), NotificationsActivity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        _binding?.menuSettings?.root?.setOnClickListener {
            // Navigate to settings
        }
        
        _binding?.menuHelpSupport?.root?.setOnClickListener {
            // Navigate to help
        }
        
        _binding?.menuAbout?.root?.setOnClickListener {
            // Show about dialog
        }
        
        _binding?.btnLogout?.setOnClickListener {
            showLogoutDialog()
        }
    }
    
    private fun showLogoutDialog() {
        if (!isAdded) return
        try {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.logout_confirm))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.logout()
                    navigateToLogin()
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun navigateToLogin() {
        try {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileState.collect { resource ->
                    if (_binding == null || !isAdded) return@collect
                    try {
                        when (resource) {
                            is Resource.Success -> {
                                val user = resource.data
                                _binding?.tvUserName?.text = user.name
                                _binding?.tvUserEmail?.text = user.email
                                _binding?.tvUserPhone?.text = user.phone ?: "No phone number"
                                
                                user.avatar?.let { avatar ->
                                    _binding?.ivProfileImage?.let { imageView ->
                                        Glide.with(requireContext())
                                            .load(avatar as String?)
                                            .circleCrop()
                                            .placeholder(R.drawable.ic_profile_placeholder)
                                            .error(R.drawable.ic_profile_placeholder)
                                            .into(imageView)
                                    }
                                }
                            }
                            is Resource.Error -> {
                                // Show error
                            }
                            is Resource.Loading -> {
                                // Show loading
                            }
                            else -> {}
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
