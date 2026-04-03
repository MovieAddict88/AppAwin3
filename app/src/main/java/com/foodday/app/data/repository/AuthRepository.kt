package com.foodday.app.data.repository

import com.foodday.app.data.model.*
import com.foodday.app.data.remote.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    private val gson = Gson()

    private fun <T> parseError(response: Response<T>): String {
        return try {
            val errorBody = response.errorBody()?.string()
            val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
            errorResponse.message ?: errorResponse.error
        } catch (e: Exception) {
            "An unexpected error occurred"
        }
    }

    fun register(request: RegisterRequest): Flow<Resource<AuthResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.register(request)
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    if (authResponse.success) {
                        authResponse.token?.let { token ->
                            sessionManager.saveAuthToken(token)
                        }
                        authResponse.user?.let { user ->
                            sessionManager.saveUser(user)
                        }
                        emit(Resource.Success(authResponse))
                    } else {
                        emit(Resource.Error(authResponse.message ?: authResponse.error ?: "Registration failed"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error(parseError(response)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun login(request: LoginRequest): Flow<Resource<AuthResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.login(request)
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    if (authResponse.success) {
                        authResponse.token?.let { token ->
                            sessionManager.saveAuthToken(token)
                        }
                        authResponse.user?.let { user ->
                            sessionManager.saveUser(user)
                        }
                        emit(Resource.Success(authResponse))
                    } else {
                        emit(Resource.Error(authResponse.message ?: authResponse.error ?: "Login failed"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error(parseError(response)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun getProfile(): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.getProfile("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    if (authResponse.success) {
                        authResponse.user?.let { user ->
                            sessionManager.saveUser(user)
                            emit(Resource.Success(user))
                        } ?: emit(Resource.Error("User data not found"))
                    } else {
                        emit(Resource.Error(authResponse.message ?: authResponse.error ?: "Failed to get profile"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error(parseError(response)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun updateProfile(request: UpdateProfileRequest): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.updateProfile("Bearer $token", request)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.success) {
                        emit(Resource.Success(true))
                    } else {
                        emit(Resource.Error(apiResponse.message ?: apiResponse.error ?: "Update failed"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error(parseError(response)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun forgotPassword(email: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.forgotPassword(mapOf("email" to email))
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.success) {
                        emit(Resource.Success(true))
                    } else {
                        emit(Resource.Error(apiResponse.message ?: apiResponse.error ?: "Request failed"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error(parseError(response)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun logout() {
        sessionManager.clearSession()
    }
    
    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()
    
    fun getCurrentUser(): User? = sessionManager.getUser()
    
    fun getAuthToken(): String? = sessionManager.getAuthToken()
}
