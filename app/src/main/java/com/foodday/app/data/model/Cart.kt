package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("user_id")
    val userId: Int,
    
    @SerializedName("product_id")
    val productId: Int,
    
    @SerializedName("product_variant_id")
    val productVariantId: Int? = null,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("product_name")
    val productName: String,
    
    @SerializedName("display_name")
    val displayName: String? = null,
    
    @SerializedName("image")
    val image: String? = null,
    
    @SerializedName("image_url")
    val imageUrl: String? = null,
    
    @SerializedName("product_price")
    val productPrice: Double? = null,
    
    @SerializedName("product_sale_price")
    val productSalePrice: Double? = null,
    
    @SerializedName("variant_name")
    val variantName: String? = null,
    
    @SerializedName("variant_price")
    val variantPrice: Double? = null,
    
    @SerializedName("restaurant_id")
    val restaurantId: Int,
    
    @SerializedName("restaurant_name")
    val restaurantName: String,
    
    @SerializedName("price")
    val price: Double = 0.0,
    
    @SerializedName("subtotal")
    val subtotal: Double = 0.0
) {
    val displayPrice: Double
        get() = variantPrice ?: productSalePrice ?: productPrice ?: price
}

data class CartResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("items")
    val items: List<CartItem>? = null,
    
    @SerializedName("subtotal")
    val subtotal: Double = 0.0,
    
    @SerializedName("service_fee")
    val serviceFee: Double = 0.0,
    
    @SerializedName("service_fee_type")
    val serviceFeeType: String = "fixed",
    
    @SerializedName("delivery_fee")
    val deliveryFee: Double? = null,
    
    @SerializedName("delivery_fee_type")
    val deliveryFeeType: String = "fixed",
    
    @SerializedName("delivery_distance_km")
    val deliveryDistanceKm: Double? = null,
    
    @SerializedName("delivery_billable_distance_km")
    val deliveryBillableDistanceKm: Double = 0.0,
    
    @SerializedName("delivery_base_fee")
    val deliveryBaseFee: Double? = null,
    
    @SerializedName("delivery_per_km_fee")
    val deliveryPerKmFee: Double? = null,
    
    @SerializedName("delivery_included_distance_km")
    val deliveryIncludedDistanceKm: Double? = null,
    
    @SerializedName("free_delivery_applied")
    val freeDeliveryApplied: Boolean = false,
    
    @SerializedName("delivery_fee_error")
    val deliveryFeeError: String? = null,
    
    @SerializedName("count")
    val count: Int = 0,
    
    @SerializedName("restaurant_id")
    val restaurantId: Int? = null,
    
    @SerializedName("multiple_restaurants")
    val multipleRestaurants: Boolean = false,
    
    @SerializedName("registration_promo")
    val registrationPromo: PromoCode? = null,
    
    @SerializedName("error")
    val error: String? = null
)

data class AddToCartRequest(
    val productId: Int,
    val quantity: Int = 1,
    val productVariantId: Int? = null
)

data class UpdateCartRequest(
    val cartId: Int,
    val quantity: Int
)
