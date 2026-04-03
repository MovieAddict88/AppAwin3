package com.foodday.app.data.repository

import com.foodday.app.data.model.*
import com.foodday.app.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RiderRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    fun getRiderOrders(status: String? = null): Flow<Resource<List<RiderOrder>>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.getRiderOrders(status)
            if (response.isSuccessful) {
                response.body()?.let { riderOrdersResponse ->
                    if (riderOrdersResponse.success) {
                        emit(Resource.Success(riderOrdersResponse.orders ?: emptyList()))
                    } else {
                        emit(Resource.Error(riderOrdersResponse.error ?: "Failed to load orders"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load orders"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun getRiderOrderById(orderId: Int): Flow<Resource<RiderOrder>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.getRiderOrderById(orderId)
            if (response.isSuccessful) {
                response.body()?.let { riderOrdersResponse ->
                    if (riderOrdersResponse.success) {
                        riderOrdersResponse.order?.let {
                            emit(Resource.Success(it))
                        } ?: emit(Resource.Error("Order not found"))
                    } else {
                        emit(Resource.Error(riderOrdersResponse.error ?: "Failed to load order"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load order"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun updateOrderStatus(orderId: Int, status: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val request = UpdateOrderStatusRequest(orderId, "update_status", status)
            val response = apiService.updateRiderOrderStatus(request)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.success) {
                        emit(Resource.Success(true))
                    } else {
                        emit(Resource.Error(apiResponse.message ?: "Failed to update status"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to update status"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun updateRiderLocation(latitude: Double, longitude: Double): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val request = UpdateRiderLocationRequest(latitude, longitude)
            val response = apiService.updateRiderLocation(request)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.success) {
                        emit(Resource.Success(true))
                    } else {
                        emit(Resource.Error(apiResponse.message ?: "Failed to update location"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to update location"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun getRiderLocation(riderId: Int): Flow<Resource<Map<String, Any>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getRiderLocation(riderId)
            if (response.isSuccessful) {
                response.body()?.let { locationResponse ->
                    if (locationResponse["success"] == true) {
                        emit(Resource.Success(locationResponse))
                    } else {
                        emit(Resource.Error(locationResponse["message"] as? String ?: "Failed to get location"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to get location"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
}
