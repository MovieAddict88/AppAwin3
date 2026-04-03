package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("order_number")
    val orderNumber: String,
    
    @SerializedName("user_id")
    val userId: Int,
    
    @SerializedName("restaurant_id")
    val restaurantId: Int? = null,
    
    @SerializedName("rider_id")
    val riderId: Int? = null,
    
    @SerializedName("subtotal")
    val subtotal: Double,
    
    @SerializedName("delivery_fee")
    val deliveryFee: Double = 0.0,
    
    @SerializedName("tax")
    val tax: Double = 0.0,
    
    @SerializedName("discount")
    val discount: Double = 0.0,
    
    @SerializedName("total")
    val total: Double,
    
    @SerializedName("is_downpayment")
    val isDownpayment: Boolean = false,
    
    @SerializedName("downpayment_amount")
    val downpaymentAmount: Double = 0.0,
    
    @SerializedName("remaining_balance")
    val remainingBalance: Double = 0.0,
    
    @SerializedName("payment_method")
    val paymentMethod: String,
    
    @SerializedName("payment_status")
    val paymentStatus: String = "pending",
    
    @SerializedName("payment_proof")
    val paymentProof: String? = null,
    
    @SerializedName("payment_reference")
    val paymentReference: String? = null,
    
    @SerializedName("payment_amount_sent")
    val paymentAmountSent: Double? = null,
    
    @SerializedName("payment_sender_name")
    val paymentSenderName: String? = null,
    
    @SerializedName("order_status")
    val orderStatus: String = "pending",
    
    @SerializedName("delivery_address")
    val deliveryAddress: String,
    
    @SerializedName("delivery_latitude")
    val deliveryLatitude: Double? = null,
    
    @SerializedName("delivery_longitude")
    val deliveryLongitude: Double? = null,
    
    @SerializedName("delivery_distance_km")
    val deliveryDistanceKm: Double? = null,
    
    @SerializedName("delivery_fee_type")
    val deliveryFeeType: String? = null,
    
    @SerializedName("service_fee")
    val serviceFee: Double = 0.0,
    
    @SerializedName("service_fee_type")
    val serviceFeeType: String = "fixed",
    
    @SerializedName("customer_name")
    val customerName: String? = null,
    
    @SerializedName("customer_phone")
    val customerPhone: String? = null,
    
    @SerializedName("notes")
    val notes: String? = null,
    
    @SerializedName("promo_code")
    val promoCode: String? = null,
    
    @SerializedName("estimated_delivery_time")
    val estimatedDeliveryTime: String? = null,
    
    @SerializedName("delivered_at")
    val deliveredAt: String? = null,
    
    @SerializedName("cancelled_at")
    val cancelledAt: String? = null,
    
    @SerializedName("cancellation_reason")
    val cancellationReason: String? = null,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    
    @SerializedName("restaurant_name")
    val restaurantName: String? = null,
    
    @SerializedName("restaurant_phone")
    val restaurantPhone: String? = null,
    
    @SerializedName("rider_name")
    val riderName: String? = null,
    
    @SerializedName("rider_phone")
    val riderPhone: String? = null,
    
    @SerializedName("items")
    val items: List<OrderItem>? = null,
    
    @SerializedName("status")
    val status: String? = null
) {
    val displayStatus: String
        get() = status ?: orderStatus
    
    val isActive: Boolean
        get() = displayStatus in listOf("pending", "confirmed", "preparing", "ready", "on_delivery")
    
    val isDelivered: Boolean
        get() = displayStatus == "delivered"
    
    val isCancelled: Boolean
        get() = displayStatus == "cancelled"
}

data class OrderItem(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("order_id")
    val orderId: Int,
    
    @SerializedName("product_id")
    val productId: Int? = null,
    
    @SerializedName("product_variant_id")
    val productVariantId: Int? = null,
    
    @SerializedName("product_name")
    val productName: String,
    
    @SerializedName("variant_name")
    val variantName: String? = null,
    
    @SerializedName("quantity")
    val quantity: Int,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("subtotal")
    val subtotal: Double
)

data class OrdersResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("orders")
    val orders: List<Order>? = null,
    
    @SerializedName("order")
    val order: Order? = null,
    
    @SerializedName("error")
    val error: String? = null
)

data class PlaceOrderResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("order_id")
    val orderId: Int? = null,
    
    @SerializedName("order_number")
    val orderNumber: String? = null,
    
    @SerializedName("error")
    val error: String? = null
)

data class PlaceOrderRequest(
    val deliveryAddress: String,
    val phone: String? = null,
    val customerPhone: String? = null,
    val deliveryLatitude: Double? = null,
    val deliveryLongitude: Double? = null,
    val paymentMethod: String = "cod",
    val paymentReference: String? = null,
    val paymentAmountSent: String? = null,
    val paymentSenderName: String? = null,
    val notes: String? = null,
    val promoCode: String? = null
)

data class CancelOrderRequest(
    val orderId: Int,
    val action: String = "cancel",
    val reason: String = "Customer cancelled"
)

data class RiderOrder(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("order_number")
    val orderNumber: String,
    
    @SerializedName("restaurant_name")
    val restaurantName: String? = null,
    
    @SerializedName("restaurant_address")
    val restaurantAddress: String? = null,
    
    @SerializedName("restaurant_phone")
    val restaurantPhone: String? = null,
    
    @SerializedName("restaurant_latitude")
    val restaurantLatitude: Double? = null,
    
    @SerializedName("restaurant_longitude")
    val restaurantLongitude: Double? = null,
    
    @SerializedName("customer_name")
    val customerName: String? = null,
    
    @SerializedName("customer_phone")
    val customerPhone: String? = null,
    
    @SerializedName("delivery_address")
    val deliveryAddress: String,
    
    @SerializedName("delivery_latitude")
    val deliveryLatitude: Double? = null,
    
    @SerializedName("delivery_longitude")
    val deliveryLongitude: Double? = null,
    
    @SerializedName("total")
    val total: Double,
    
    @SerializedName("payment_method")
    val paymentMethod: String,
    
    @SerializedName("order_status")
    val orderStatus: String,
    
    @SerializedName("items")
    val items: List<OrderItem>? = null
)

data class RiderOrdersResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("orders")
    val orders: List<RiderOrder>? = null,
    
    @SerializedName("order")
    val order: RiderOrder? = null,
    
    @SerializedName("error")
    val error: String? = null
)

data class UpdateRiderLocationRequest(
    val latitude: Double,
    val longitude: Double,
    val action: String = "update_location"
)

data class UpdateOrderStatusRequest(
    val orderId: Int,
    val action: String = "update_status",
    val status: String
)
