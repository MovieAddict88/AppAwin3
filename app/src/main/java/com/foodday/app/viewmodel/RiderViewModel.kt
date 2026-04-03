package com.foodday.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodday.app.data.model.*
import com.foodday.app.data.repository.RiderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RiderViewModel @Inject constructor(
    private val riderRepository: RiderRepository
) : ViewModel() {
    
    private val _orders = MutableLiveData<Resource<List<RiderOrder>>>()
    val orders: LiveData<Resource<List<RiderOrder>>> = _orders
    
    private val _orderDetail = MutableLiveData<Resource<RiderOrder>>()
    val orderDetail: LiveData<Resource<RiderOrder>> = _orderDetail
    
    private val _updateStatusState = MutableLiveData<Resource<Boolean>>()
    val updateStatusState: LiveData<Resource<Boolean>> = _updateStatusState
    
    private val _updateLocationState = MutableLiveData<Resource<Boolean>>()
    val updateLocationState: LiveData<Resource<Boolean>> = _updateLocationState
    
    private val _riderLocation = MutableLiveData<Resource<Map<String, Any>>>()
    val riderLocation: LiveData<Resource<Map<String, Any>>> = _riderLocation
    
    fun getOrders(status: String? = null) {
        riderRepository.getRiderOrders(status).onEach { result ->
            _orders.value = result
        }.launchIn(viewModelScope)
    }
    
    fun getOrderById(orderId: Int) {
        riderRepository.getRiderOrderById(orderId).onEach { result ->
            _orderDetail.value = result
        }.launchIn(viewModelScope)
    }
    
    fun updateOrderStatus(orderId: Int, status: String) {
        riderRepository.updateOrderStatus(orderId, status).onEach { result ->
            _updateStatusState.value = result
        }.launchIn(viewModelScope)
    }
    
    fun updateLocation(latitude: Double, longitude: Double) {
        riderRepository.updateRiderLocation(latitude, longitude).onEach { result ->
            _updateLocationState.value = result
        }.launchIn(viewModelScope)
    }
    
    fun getRiderLocation(riderId: Int) {
        riderRepository.getRiderLocation(riderId).onEach { result ->
            _riderLocation.value = result
        }.launchIn(viewModelScope)
    }
    
    fun resetStates() {
        _updateStatusState.value = Resource.Success(false)
        _updateLocationState.value = Resource.Success(false)
    }
}
