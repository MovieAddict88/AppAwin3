package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("user_id")
    val userId: Int,
    
    @SerializedName("product_id")
    val productId: Int? = null,
    
    @SerializedName("restaurant_id")
    val restaurantId: Int? = null,
    
    @SerializedName("order_id")
    val orderId: Int? = null,
    
    @SerializedName("rating")
    val rating: Int,
    
    @SerializedName("comment")
    val comment: String,
    
    @SerializedName("images")
    val images: String? = null,
    
    @SerializedName("platform_review")
    val platformReview: Boolean = false,
    
    @SerializedName("status")
    val status: String = "pending",
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    
    @SerializedName("user_name")
    val userName: String? = null,
    
    @SerializedName("user_avatar")
    val userAvatar: String? = null
)

data class ReviewsResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: List<Review>? = null,
    
    @SerializedName("error")
    val error: String? = null
)

data class SubmitReviewRequest(
    val productId: Int? = null,
    val restaurantId: Int? = null,
    val orderId: Int? = null,
    val rating: Int,
    val comment: String
)
