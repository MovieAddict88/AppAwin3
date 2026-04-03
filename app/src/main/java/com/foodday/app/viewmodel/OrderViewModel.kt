package com.foodday.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodday.app.data.model.*
import com.foodday.app.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {
    
    private val _orders = MutableLiveData<Resource<List<Order>>>()
    val orders: LiveData<Resource<List<Order>>> = _orders
    
    private val _orderDetail = MutableLiveData<Resource<Order>>()
    val orderDetail: LiveData<Resource<Order>> = _orderDetail
    
    private val _placeOrderState = MutableLiveData<Resource<PlaceOrderResponse>>()
    val placeOrderState: LiveData<Resource<PlaceOrderResponse>> = _placeOrderState
    
    private val _cancelOrderState = MutableLiveData<Resource<Boolean>>()
    val cancelOrderState: LiveData<Resource<Boolean>> = _cancelOrderState
    
    private val _validatePromoState = MutableLiveData<Resource<ValidatePromoCodeResponse>>()
    val validatePromoState: LiveData<Resource<ValidatePromoCodeResponse>> = _validatePromoState
    
    private val _currentPromoCode = MutableLiveData<PromoCode?>()
    val currentPromoCode: LiveData<PromoCode?> = _currentPromoCode
    
    private val _discountAmount = MutableLiveData<Double>(0.0)
    val discountAmount: LiveData<Double> = _discountAmount
    
    fun getOrders(status: String? = null) {
        orderRepository.getOrders(status).onEach { result ->
            _orders.value = result
        }.launchIn(viewModelScope)
    }
    
    fun getOrderById(orderId: Int) {
        orderRepository.getOrderById(orderId).onEach { result ->
            _orderDetail.value = result
        }.launchIn(viewModelScope)
    }
    
    fun placeOrder(
        deliveryAddress: String,
        customerPhone: String,
        deliveryLatitude: Double? = null,
        deliveryLongitude: Double? = null,
        paymentMethod: String = "cod",
        paymentReference: String? = null,
        paymentAmountSent: String? = null,
        paymentSenderName: String? = null,
        notes: String? = null,
        promoCode: String? = null,
        paymentProofFile: File? = null
    ) {
        val request = PlaceOrderRequest(
            deliveryAddress = deliveryAddress,
            customerPhone = customerPhone,
            deliveryLatitude = deliveryLatitude,
            deliveryLongitude = deliveryLongitude,
            paymentMethod = paymentMethod,
            paymentReference = paymentReference,
            paymentAmountSent = paymentAmountSent,
            paymentSenderName = paymentSenderName,
            notes = notes,
            promoCode = promoCode
        )
        orderRepository.placeOrder(request, paymentProofFile).onEach { result ->
            _placeOrderState.value = result
        }.launchIn(viewModelScope)
    }
    
    fun cancelOrder(orderId: Int, reason: String = "Customer cancelled") {
        orderRepository.cancelOrder(orderId, reason).onEach { result ->
            _cancelOrderState.value = result
        }.launchIn(viewModelScope)
    }
    
    fun validatePromoCode(code: String) {
        orderRepository.validatePromoCode(code).onEach { result ->
            _validatePromoState.value = result
            if (result is Resource.Success) {
                _currentPromoCode.value = result.data.promo
                _discountAmount.value = result.data.discountAmount ?: 0.0
            }
        }.launchIn(viewModelScope)
    }
    
    fun clearPromoCode() {
        _currentPromoCode.value = null
        _discountAmount.value = 0.0
    }
    
    fun resetPlaceOrderState() {
        _placeOrderState.value = Resource.Success(PlaceOrderResponse(false))
    }
    
    fun resetCancelOrderState() {
        _cancelOrderState.value = Resource.Success(false)
    }
}
