package com.foodday.app.data.model

import com.google.gson.annotations.SerializedName

data class NotificationItem(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("user_id")
    val userId: Int? = null,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("type")
    val type: String = "system",
    
    @SerializedName("is_read")
    val isRead: Boolean = false,
    
    @SerializedName("data")
    val data: String? = null,
    
    @SerializedName("created_at")
    val createdAt: String
) {
    val iconRes: Int
        get() = when (type) {
            "order" -> android.R.drawable.ic_menu_agenda
            "promotion" -> android.R.drawable.ic_menu_share
            "delivery" -> android.R.drawable.ic_menu_mylocation
            else -> android.R.drawable.ic_menu_info_details
        }
}

data class NotificationsResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("notifications")
    val notifications: List<NotificationItem>? = null,
    
    @SerializedName("unread_count")
    val unreadCount: Int = 0,
    
    @SerializedName("error")
    val error: String? = null
)
