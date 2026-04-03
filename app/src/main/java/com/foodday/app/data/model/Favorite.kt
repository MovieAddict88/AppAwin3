package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class Favorite(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("user_id")
    val userId: Int,
    
    @SerializedName("product_id")
    val productId: Int,
    
    @SerializedName("restaurant_id")
    val restaurantId: Int? = null,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("product_name")
    val productName: String? = null,
    
    @SerializedName("image")
    val image: String? = null,
    
    @SerializedName("product_image")
    val productImage: String? = null,
    
    @SerializedName("price")
    val price: Double? = null,
    
    @SerializedName("product_price")
    val productPrice: Double? = null,
    
    @SerializedName("rating")
    val rating: Double? = null,
    
    @SerializedName("has_variants")
    val hasVariants: Boolean = false,
    
    @SerializedName("product_has_variants")
    val productHasVariants: Boolean = false
) {
    val displayName: String
        get() = productName ?: name ?: "Product"
    
    val displayImage: String?
        get() = productImage ?: image
    
    val displayPrice: Double?
        get() = productPrice ?: price
}

data class FavoritesResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("favorites")
    val favorites: List<Favorite>? = null,
    
    @SerializedName("error")
    val error: String? = null
)

data class AddFavoriteRequest(
    val productId: Int
)
