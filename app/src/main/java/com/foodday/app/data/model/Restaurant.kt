package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class Restaurant(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("image")
    val image: String? = null,
    
    @SerializedName("image_url")
    val imageUrl: String? = null,
    
    @SerializedName("banner")
    val banner: String? = null,
    
    @SerializedName("banner_url")
    val bannerUrl: String? = null,
    
    @SerializedName("address")
    val address: String? = null,
    
    @SerializedName("latitude")
    val latitude: Double? = null,
    
    @SerializedName("longitude")
    val longitude: Double? = null,
    
    @SerializedName("phone")
    val phone: String? = null,
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("delivery_time")
    val deliveryTime: String? = null,
    
    @SerializedName("delivery_fee")
    val deliveryFee: Double = 0.0,
    
    @SerializedName("min_order")
    val minOrder: Double = 0.0,
    
    @SerializedName("rating")
    val rating: Double = 0.0,
    
    @SerializedName("reviews_count")
    val reviewsCount: Int = 0,
    
    @SerializedName("status")
    val status: String = "active",
    
    @SerializedName("is_featured")
    val isFeatured: Boolean = false,
    
    @SerializedName("opening_time")
    val openingTime: String? = null,
    
    @SerializedName("closing_time")
    val closingTime: String? = null,
    
    @SerializedName("products_count")
    val productsCount: Int = 0
)

data class RestaurantsResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("restaurants")
    val restaurants: List<Restaurant>? = null,
    
    @SerializedName("restaurant")
    val restaurant: Restaurant? = null,
    
    @SerializedName("error")
    val error: String? = null
)
