package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("avatar")
    val avatar: String? = null,
    
    @SerializedName("address")
    val address: String? = null,
    
    @SerializedName("latitude")
    val latitude: Double? = null,
    
    @SerializedName("longitude")
    val longitude: Double? = null,
    
    @SerializedName("role")
    val role: String = "customer",
    
    @SerializedName("status")
    val status: String = "active"
)

data class AuthResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("token")
    val token: String? = null,
    
    @SerializedName("user")
    val user: User? = null,
    
    @SerializedName("error")
    val error: String? = null
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val phone: String? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val promoCode: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class UpdateProfileRequest(
    val name: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
