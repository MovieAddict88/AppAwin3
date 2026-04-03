package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class Setting(
    @SerializedName("site_name")
    val siteName: String = "Food Day",
    
    @SerializedName("site_tagline")
    val siteTagline: String = "Order Food, Anytime, Anywhere",
    
    @SerializedName("support_email")
    val supportEmail: String = "support@foodday.ph",
    
    @SerializedName("support_phone")
    val supportPhone: String = "+63 2 8123 4567",
    
    @SerializedName("currency")
    val currency: String = "PHP",
    
    @SerializedName("currency_symbol")
    val currencySymbol: String = "₱",
    
    @SerializedName("delivery_fee_type")
    val deliveryFeeType: String = "fixed",
    
    @SerializedName("base_delivery_fee")
    val baseDeliveryFee: Double = 50.0,
    
    @SerializedName("distance_delivery_fee")
    val distanceDeliveryFee: Double = 10.0,
    
    @SerializedName("base_distance_km")
    val baseDistanceKm: Double = 1.0,
    
    @SerializedName("free_delivery_minimum")
    val freeDeliveryMinimum: Double = 500.0,
    
    @SerializedName("max_delivery_distance")
    val maxDeliveryDistance: Double = 10.0,
    
    @SerializedName("service_fee")
    val serviceFee: Double = 15.0,
    
    @SerializedName("service_fee_type")
    val serviceFeeType: String = "fixed",
    
    @SerializedName("gcash_name")
    val gcashName: String? = null,
    
    @SerializedName("gcash_number")
    val gcashNumber: String? = null,
    
    @SerializedName("paymaya_name")
    val paymayaName: String? = null,
    
    @SerializedName("paymaya_number")
    val paymayaNumber: String? = null,
    
    @SerializedName("facebook_url")
    val facebookUrl: String? = null,
    
    @SerializedName("tiktok_url")
    val tiktokUrl: String? = null
) {
    val hasGcash: Boolean
        get() = !gcashNumber.isNullOrBlank()
    
    val hasPaymaya: Boolean
        get() = !paymayaNumber.isNullOrBlank()
}
