package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("restaurant_id")
    val restaurantId: Int,
    
    @SerializedName("category_id")
    val categoryId: Int? = null,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("image")
    val image: String? = null,
    
    @SerializedName("image_url")
    val imageUrl: String? = null,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("sale_price")
    val salePrice: Double? = null,
    
    @SerializedName("stock")
    val stock: Int = 0,
    
    @SerializedName("is_available")
    val isAvailable: Boolean = true,
    
    @SerializedName("is_featured")
    val isFeatured: Boolean = false,
    
    @SerializedName("has_variants")
    val hasVariants: Boolean = false,
    
    @SerializedName("rating")
    val rating: Double = 0.0,
    
    @SerializedName("reviews_count")
    val reviewsCount: Int = 0,
    
    @SerializedName("preparation_time")
    val preparationTime: String? = null,
    
    @SerializedName("status")
    val status: String = "active",
    
    @SerializedName("category_name")
    val categoryName: String? = null,
    
    @SerializedName("restaurant_name")
    val restaurantName: String? = null,
    
    @SerializedName("restaurant_image")
    val restaurantImage: String? = null,
    
    @SerializedName("restaurant_delivery_time")
    val restaurantDeliveryTime: String? = null,
    
    @SerializedName("images")
    val images: List<String>? = null,
    
    @SerializedName("variants")
    val variants: List<ProductVariant>? = null
) {
    val displayPrice: Double
        get() = salePrice ?: price
    
    val hasDiscount: Boolean
        get() = salePrice != null && salePrice < price
    
    val discountPercentage: Int
        get() = if (hasDiscount) {
            (((price - (salePrice ?: price)) / price) * 100).toInt()
        } else 0
}

data class ProductVariant(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("product_id")
    val productId: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("stock")
    val stock: Int = 0,
    
    @SerializedName("is_available")
    val isAvailable: Boolean = true
)

data class ProductsResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("products")
    val products: List<Product>? = null,
    
    @SerializedName("product")
    val product: Product? = null,
    
    @SerializedName("total_count")
    val totalCount: Int = 0,
    
    @SerializedName("limit")
    val limit: Int = 20,
    
    @SerializedName("offset")
    val offset: Int = 0,
    
    @SerializedName("error")
    val error: String? = null
)
