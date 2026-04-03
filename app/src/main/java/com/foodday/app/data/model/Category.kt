package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("icon")
    val icon: String? = null,
    
    @SerializedName("image")
    val image: String? = null,
    
    @SerializedName("image_url")
    val imageUrl: String? = null,
    
    @SerializedName("status")
    val status: String = "active",
    
    @SerializedName("sort_order")
    val sortOrder: Int = 0
)

data class CategoriesResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("categories")
    val categories: List<Category>? = null,
    
    @SerializedName("error")
    val error: String? = null
)
