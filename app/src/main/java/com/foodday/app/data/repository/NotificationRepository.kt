package com.foodday.app.data.repository

import com.foodday.app.data.model.*
import com.foodday.app.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    fun getNotifications(): Flow<Resource<Pair<List<NotificationItem>, Int>>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.getNotifications("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { notificationsResponse ->
                    if (notificationsResponse.success) {
                        emit(Resource.Success(
                            Pair(
                                notificationsResponse.notifications ?: emptyList(),
                                notificationsResponse.unreadCount
                            )
                        ))
                    } else {
                        emit(Resource.Error(notificationsResponse.error ?: "Failed to load notifications"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load notifications"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun markAsRead(notificationId: Int? = null): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.markNotificationAsRead("Bearer $token", notificationId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.success) {
                        emit(Resource.Success(true))
                    } else {
                        emit(Resource.Error(apiResponse.message ?: "Failed to mark as read"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to mark as read"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun deleteNotification(notificationId: Int? = null): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.deleteNotification("Bearer $token", notificationId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.success) {
                        emit(Resource.Success(true))
                    } else {
                        emit(Resource.Error(apiResponse.message ?: "Failed to delete notification"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to delete notification"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
}
