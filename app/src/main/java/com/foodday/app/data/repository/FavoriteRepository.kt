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
class FavoriteRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    fun getFavorites(): Flow<Resource<List<Favorite>>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.getFavorites("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { favoritesResponse ->
                    if (favoritesResponse.success) {
                        emit(Resource.Success(favoritesResponse.favorites ?: emptyList()))
                    } else {
                        emit(Resource.Error(favoritesResponse.error ?: "Failed to load favorites"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load favorites"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun addFavorite(productId: Int): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val request = AddFavoriteRequest(productId)
            val response = apiService.addFavorite("Bearer $token", request)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.success) {
                        emit(Resource.Success(true))
                    } else {
                        emit(Resource.Error(apiResponse.message ?: "Failed to add favorite"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                val errorBody = response.errorBody()?.string()
                emit(Resource.Error(errorBody ?: "Failed to add favorite"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun removeFavorite(productId: Int): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.removeFavorite("Bearer $token", productId)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.success) {
                        emit(Resource.Success(true))
                    } else {
                        emit(Resource.Error(apiResponse.message ?: "Failed to remove favorite"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to remove favorite"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
}
