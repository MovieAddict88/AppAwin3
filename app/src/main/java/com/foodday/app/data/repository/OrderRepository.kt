package com.foodday.app.data.repository

import com.foodday.app.data.model.*
import com.foodday.app.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    fun getOrders(status: String? = null): Flow<Resource<List<Order>>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.getOrders("Bearer $token", status)
            if (response.isSuccessful) {
                response.body()?.let { ordersResponse ->
                    if (ordersResponse.success) {
                        emit(Resource.Success(ordersResponse.orders ?: emptyList()))
                    } else {
                        emit(Resource.Error(ordersResponse.error ?: "Failed to load orders"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load orders"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun getOrderById(orderId: Int): Flow<Resource<Order>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.getOrderById("Bearer $token", orderId)
            if (response.isSuccessful) {
                response.body()?.let { ordersResponse ->
                    if (ordersResponse.success) {
                        ordersResponse.order?.let {
                            emit(Resource.Success(it))
                        } ?: emit(Resource.Error("Order not found"))
                    } else {
                        emit(Resource.Error(ordersResponse.error ?: "Failed to load order"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load order"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun placeOrder(
        request: PlaceOrderRequest,
        paymentProofFile: File? = null
    ): Flow<Resource<PlaceOrderResponse>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val orderDataJson = com.google.gson.Gson().toJson(request)
            val orderDataBody = orderDataJson.toRequestBody("application/json".toMediaTypeOrNull())
            
            val paymentProofPart = paymentProofFile?.let { file ->
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("payment_proof", file.name, requestFile)
            }
            
            val response = apiService.placeOrder("Bearer $token", orderDataBody, paymentProofPart)
            if (response.isSuccessful) {
                response.body()?.let { placeOrderResponse ->
                    if (placeOrderResponse.success) {
                        emit(Resource.Success(placeOrderResponse))
                    } else {
                        emit(Resource.Error(placeOrderResponse.error ?: "Failed to place order"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Resource.Error(errorBody ?: "Failed to place order"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun cancelOrder(orderId: Int, reason: String = "Customer cancelled"): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val request = CancelOrderRequest(orderId, "cancel", reason)
            val response = apiService.cancelOrder("Bearer $token", request)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.success) {
                        emit(Resource.Success(true))
                    } else {
                        emit(Resource.Error(apiResponse.message ?: "Failed to cancel order"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to cancel order"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    // Promo Codes
    fun validatePromoCode(code: String): Flow<Resource<ValidatePromoCodeResponse>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.validatePromoCode("Bearer $token", code)
            if (response.isSuccessful) {
                response.body()?.let { validateResponse ->
                    if (validateResponse.success) {
                        emit(Resource.Success(validateResponse))
                    } else {
                        emit(Resource.Error(validateResponse.error ?: "Invalid promo code"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Resource.Error(errorBody ?: "Invalid promo code"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
}
