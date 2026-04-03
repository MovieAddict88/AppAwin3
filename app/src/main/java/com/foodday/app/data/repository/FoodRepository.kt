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
class FoodRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
) {
    // Categories
    fun getCategories(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getCategories()
            if (response.isSuccessful) {
                response.body()?.let { categoriesResponse ->
                    if (categoriesResponse.success) {
                        emit(Resource.Success(categoriesResponse.categories ?: emptyList()))
                    } else {
                        emit(Resource.Error(categoriesResponse.error ?: "Failed to load categories"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load categories"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    // Restaurants
    fun getRestaurants(
        featured: Boolean? = null,
        search: String? = null,
        limit: Int = 20,
        offset: Int = 0
    ): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getRestaurants(featured, search, limit, offset)
            if (response.isSuccessful) {
                response.body()?.let { restaurantsResponse ->
                    if (restaurantsResponse.success) {
                        emit(Resource.Success(restaurantsResponse.restaurants ?: emptyList()))
                    } else {
                        emit(Resource.Error(restaurantsResponse.error ?: "Failed to load restaurants"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load restaurants"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun getRestaurantById(id: Int): Flow<Resource<Restaurant>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getRestaurantById(id)
            if (response.isSuccessful) {
                response.body()?.let { restaurantsResponse ->
                    if (restaurantsResponse.success) {
                        restaurantsResponse.restaurant?.let {
                            emit(Resource.Success(it))
                        } ?: emit(Resource.Error("Restaurant not found"))
                    } else {
                        emit(Resource.Error(restaurantsResponse.error ?: "Failed to load restaurant"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load restaurant"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    // Products
    fun getProducts(
        categoryId: Int? = null,
        restaurantId: Int? = null,
        search: String? = null,
        featured: Boolean? = null,
        limit: Int = 20,
        offset: Int = 0
    ): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getProducts(categoryId, restaurantId, search, featured, limit, offset)
            if (response.isSuccessful) {
                response.body()?.let { productsResponse ->
                    if (productsResponse.success) {
                        emit(Resource.Success(productsResponse.products ?: emptyList()))
                    } else {
                        emit(Resource.Error(productsResponse.error ?: "Failed to load products"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load products"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun getProductById(id: Int): Flow<Resource<Product>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getProductById(id)
            if (response.isSuccessful) {
                response.body()?.let { productsResponse ->
                    if (productsResponse.success) {
                        productsResponse.product?.let {
                            emit(Resource.Success(it))
                        } ?: emit(Resource.Error("Product not found"))
                    } else {
                        emit(Resource.Error(productsResponse.error ?: "Failed to load product"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load product"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    // Banners
    fun getBanners(type: String? = null): Flow<Resource<List<Banner>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getBanners(type)
            if (response.isSuccessful) {
                response.body()?.let { bannersResponse ->
                    if (bannersResponse.success) {
                        emit(Resource.Success(bannersResponse.banners ?: emptyList()))
                    } else {
                        emit(Resource.Error(bannersResponse.error ?: "Failed to load banners"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load banners"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    // Reviews
    fun getReviews(
        productId: Int? = null,
        restaurantId: Int? = null,
        platform: Boolean = false
    ): Flow<Resource<List<Review>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getReviews(productId, restaurantId, platform)
            if (response.isSuccessful) {
                response.body()?.let { reviewsResponse ->
                    if (reviewsResponse.success) {
                        emit(Resource.Success(reviewsResponse.data ?: emptyList()))
                    } else {
                        emit(Resource.Error(reviewsResponse.error ?: "Failed to load reviews"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load reviews"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    fun submitReview(request: SubmitReviewRequest): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val token = sessionManager.getAuthToken()
            if (token == null) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            
            val response = apiService.submitReview(request)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.success) {
                        emit(Resource.Success(true))
                    } else {
                        emit(Resource.Error(apiResponse.error ?: "Failed to submit review"))
                    }
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to submit review"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
    
    // Settings
    fun getSettings(): Flow<Resource<Setting>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getSettings()
            if (response.isSuccessful) {
                response.body()?.let { setting ->
                    emit(Resource.Success(setting))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load settings"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }.flowOn(Dispatchers.IO)
}
