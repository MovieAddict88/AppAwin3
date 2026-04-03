package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("error")
    val error: String? = null
)

data class ErrorResponse(
    @SerializedName("error")
    val error: String,
    
    @SerializedName("message")
    val message: String? = null
)

sealed class Resource<T> {
    class Idle<T> : Resource<T>()
    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String, val code: Int? = null) : Resource<T>()
    
    fun isIdle() = this is Idle
    fun isLoading() = this is Loading
    fun isSuccess() = this is Success
    fun isError() = this is Error
}
