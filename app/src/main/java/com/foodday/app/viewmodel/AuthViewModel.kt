package com.foodday.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodday.app.data.model.*
import com.foodday.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _loginState = MutableStateFlow<Resource<AuthResponse>>(Resource.Idle())
    val loginState: StateFlow<Resource<AuthResponse>> = _loginState.asStateFlow()
    
    private val _registerState = MutableStateFlow<Resource<AuthResponse>>(Resource.Idle())
    val registerState: StateFlow<Resource<AuthResponse>> = _registerState.asStateFlow()
    
    private val _profileState = MutableStateFlow<Resource<User>>(Resource.Idle())
    val profileState: StateFlow<Resource<User>> = _profileState.asStateFlow()
    
    private val _updateProfileState = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val updateProfileState: StateFlow<Resource<Boolean>> = _updateProfileState.asStateFlow()
    
    private val _forgotPasswordState = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val forgotPasswordState: StateFlow<Resource<Boolean>> = _forgotPasswordState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    init {
        _currentUser.value = authRepository.getCurrentUser()
    }
    
    fun login(email: String, password: String) {
        val request = LoginRequest(email, password)
        authRepository.login(request).onEach { result ->
            _loginState.value = result
            if (result is Resource.Success) {
                _currentUser.value = result.data.user
            }
        }.launchIn(viewModelScope)
    }
    
    fun register(
        name: String,
        email: String,
        password: String,
        phone: String? = null,
        address: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        promoCode: String? = null
    ) {
        val request = RegisterRequest(name, email, password, phone, address, latitude, longitude, promoCode)
        authRepository.register(request).onEach { result ->
            _registerState.value = result
            if (result is Resource.Success) {
                _currentUser.value = result.data.user
            }
        }.launchIn(viewModelScope)
    }
    
    fun getProfile() {
        authRepository.getProfile().onEach { result ->
            _profileState.value = result
            if (result is Resource.Success) {
                _currentUser.value = result.data
            }
        }.launchIn(viewModelScope)
    }
    
    fun updateProfile(
        name: String? = null,
        phone: String? = null,
        address: String? = null,
        latitude: Double? = null,
        longitude: Double? = null
    ) {
        val request = UpdateProfileRequest(name, phone, address, latitude, longitude)
        authRepository.updateProfile(request).onEach { result ->
            _updateProfileState.value = result
        }.launchIn(viewModelScope)
    }
    
    fun forgotPassword(email: String) {
        authRepository.forgotPassword(email).onEach { result ->
            _forgotPasswordState.value = result
        }.launchIn(viewModelScope)
    }
    
    fun logout() {
        authRepository.logout()
        _currentUser.value = null
    }
    
    fun isLoggedIn(): Boolean = authRepository.isLoggedIn()
    
    fun resetStates() {
        _loginState.value = Resource.Idle()
        _registerState.value = Resource.Idle()
        _updateProfileState.value = Resource.Idle()
        _forgotPasswordState.value = Resource.Idle()
    }
}
