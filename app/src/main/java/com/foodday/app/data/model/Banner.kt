package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("image")
    val image: String,
    
    @SerializedName("image_url")
    val imageUrl: String? = null,
    
    @SerializedName("link")
    val link: String? = null,
    
    @SerializedName("link_url")
    val linkUrl: String? = null,
    
    @SerializedName("type")
    val type: String = "promo",
    
    @SerializedName("status")
    val status: String = "active",
    
    @SerializedName("sort_order")
    val sortOrder: Int = 0
)

data class BannersResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("banners")
    val banners: List<Banner>? = null,
    
    @SerializedName("error")
    val error: String? = null
)
