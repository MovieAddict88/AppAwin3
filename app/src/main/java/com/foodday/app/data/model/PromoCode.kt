package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class PromoCode(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("code")
    val code: String,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("discount_type")
    val discountType: String,
    
    @SerializedName("discount_value")
    val discountValue: Double,
    
    @SerializedName("min_order")
    val minOrder: Double = 0.0,
    
    @SerializedName("max_discount")
    val maxDiscount: Double? = null,
    
    @SerializedName("usage_limit")
    val usageLimit: Int? = null,
    
    @SerializedName("used_count")
    val usedCount: Int = 0,
    
    @SerializedName("start_date")
    val startDate: String? = null,
    
    @SerializedName("end_date")
    val endDate: String? = null,
    
    @SerializedName("status")
    val status: String = "active",
    
    @SerializedName("is_global")
    val isGlobal: Boolean = false,
    
    @SerializedName("is_for_registration")
    val isForRegistration: Boolean = false
) {
    val isValid: Boolean
        get() = status == "active" && (usageLimit == null || usedCount < usageLimit)
    
    val formattedDiscount: String
        get() = when (discountType) {
            "percentage" -> "${discountValue.toInt()}% OFF"
            "fixed" -> "₱${discountValue.toInt()} OFF"
            else -> "${discountValue.toInt()} OFF"
        }
}

data class ValidatePromoCodeResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("promo")
    val promo: PromoCode? = null,
    
    @SerializedName("discount_amount")
    val discountAmount: Double? = null,
    
    @SerializedName("error")
    val error: String? = null,
    
    @SerializedName("min_order")
    val minOrder: Double? = null,
    
    @SerializedName("current_applicable_subtotal")
    val currentApplicableSubtotal: Double? = null
)
