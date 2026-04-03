package com.foodday.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodday.app.data.model.*
import com.foodday.app.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {
    
    private val _cartState = MutableLiveData<Resource<CartResponse>>()
    val cartState: LiveData<Resource<CartResponse>> = _cartState
    
    private val _addToCartState = MutableLiveData<Resource<Boolean>>()
    val addToCartState: LiveData<Resource<Boolean>> = _addToCartState
    
    private val _updateCartState = MutableLiveData<Resource<Boolean>>()
    val updateCartState: LiveData<Resource<Boolean>> = _updateCartState
    
    private val _removeCartState = MutableLiveData<Resource<Boolean>>()
    val removeCartState: LiveData<Resource<Boolean>> = _removeCartState
    
    private val _clearCartState = MutableLiveData<Resource<Boolean>>()
    val clearCartState: LiveData<Resource<Boolean>> = _clearCartState
    
    private val _cartItemCount = MutableLiveData<Int>()
    val cartItemCount: LiveData<Int> = _cartItemCount
    
    fun getCart(lat: Double? = null, lng: Double? = null) {
        cartRepository.getCart(lat, lng).onEach { result ->
            _cartState.value = result
            if (result is Resource.Success) {
                _cartItemCount.value = result.data.count
            }
        }.launchIn(viewModelScope)
    }
    
    fun addToCart(productId: Int, quantity: Int = 1, productVariantId: Int? = null) {
        val request = AddToCartRequest(productId, quantity, productVariantId)
        cartRepository.addToCart(request).onEach { result ->
            _addToCartState.value = result
            if (result is Resource.Success) {
                getCart()
            }
        }.launchIn(viewModelScope)
    }
    
    fun updateCartItem(cartId: Int, quantity: Int) {
        val request = UpdateCartRequest(cartId, quantity)
        cartRepository.updateCartItem(request).onEach { result ->
            _updateCartState.value = result
            if (result is Resource.Success) {
                getCart()
            }
        }.launchIn(viewModelScope)
    }
    
    fun removeCartItem(cartId: Int) {
        cartRepository.removeCartItem(cartId).onEach { result ->
            _removeCartState.value = result
            if (result is Resource.Success) {
                getCart()
            }
        }.launchIn(viewModelScope)
    }
    
    fun clearCart() {
        cartRepository.clearCart().onEach { result ->
            _clearCartState.value = result
            if (result is Resource.Success) {
                getCart()
            }
        }.launchIn(viewModelScope)
    }
    
    fun calculateTotal(cartResponse: CartResponse?): Double {
        cartResponse ?: return 0.0
        val subtotal = cartResponse.subtotal
        val deliveryFee = cartResponse.deliveryFee ?: 0.0
        val serviceFee = cartResponse.serviceFee
        return subtotal + deliveryFee + serviceFee
    }
    
    fun resetAddToCartState() {
        _addToCartState.value = Resource.Success(false)
    }
}
