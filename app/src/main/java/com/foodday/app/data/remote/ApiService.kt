package com.foodday.app.data.remote

import com.foodday.app.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // Auth
    @POST("auth.php?action=register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("auth.php?action=login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @GET("auth.php")
    suspend fun getProfile(): Response<AuthResponse>
    
    @PUT("auth.php")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest
    ): Response<ApiResponse<*>>
    
    @POST("auth.php?action=forgot-password")
    suspend fun forgotPassword(@Body email: Map<String, String>): Response<ApiResponse<*>>
    
    // Categories
    @GET("categories.php")
    suspend fun getCategories(): Response<CategoriesResponse>
    
    // Restaurants
    @GET("restaurants.php")
    suspend fun getRestaurants(
        @Query("featured") featured: Boolean? = null,
        @Query("search") search: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<RestaurantsResponse>
    
    @GET("restaurants.php")
    suspend fun getRestaurantById(@Query("id") id: Int): Response<RestaurantsResponse>
    
    // Products
    @GET("products.php")
    suspend fun getProducts(
        @Query("category_id") categoryId: Int? = null,
        @Query("restaurant_id") restaurantId: Int? = null,
        @Query("search") search: String? = null,
        @Query("featured") featured: Boolean? = null,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<ProductsResponse>
    
    @GET("products.php")
    suspend fun getProductById(@Query("id") id: Int): Response<ProductsResponse>
    
    // Cart
    @GET("cart.php")
    suspend fun getCart(
        @Query("lat") lat: Double? = null,
        @Query("lng") lng: Double? = null
    ): Response<CartResponse>
    
    @POST("cart.php")
    suspend fun addToCart(
        @Body request: AddToCartRequest
    ): Response<ApiResponse<*>>
    
    @PUT("cart.php")
    suspend fun updateCartItem(
        @Body request: UpdateCartRequest
    ): Response<ApiResponse<*>>
    
    @DELETE("cart.php")
    suspend fun removeCartItem(
        @Query("id") id: Int
    ): Response<ApiResponse<*>>
    
    @DELETE("cart.php")
    suspend fun clearCart(): Response<ApiResponse<*>>
    
    // Orders
    @GET("orders.php")
    suspend fun getOrders(
        @Query("status") status: String? = null
    ): Response<OrdersResponse>
    
    @GET("orders.php")
    suspend fun getOrderById(
        @Query("id") id: Int
    ): Response<OrdersResponse>
    
    @Multipart
    @POST("orders.php")
    suspend fun placeOrder(
        @Part("order_data") orderData: RequestBody,
        @Part paymentProof: MultipartBody.Part? = null
    ): Response<PlaceOrderResponse>
    
    @PUT("orders.php")
    suspend fun cancelOrder(
        @Body request: CancelOrderRequest
    ): Response<ApiResponse<*>>
    
    // Favorites
    @GET("favorites.php")
    suspend fun getFavorites(): Response<FavoritesResponse>
    
    @POST("favorites.php")
    suspend fun addFavorite(
        @Body request: AddFavoriteRequest
    ): Response<ApiResponse<*>>
    
    @DELETE("favorites.php")
    suspend fun removeFavorite(
        @Query("product_id") productId: Int
    ): Response<ApiResponse<*>>
    
    // Notifications
    @GET("notifications.php")
    suspend fun getNotifications(): Response<NotificationsResponse>
    
    @PUT("notifications.php")
    suspend fun markNotificationAsRead(
        @Query("id") id: Int? = null
    ): Response<ApiResponse<*>>
    
    @DELETE("notifications.php")
    suspend fun deleteNotification(
        @Query("id") id: Int? = null
    ): Response<ApiResponse<*>>
    
    // Banners
    @GET("banners.php")
    suspend fun getBanners(@Query("type") type: String? = null): Response<BannersResponse>
    
    // Promo Codes
    @GET("promo-codes.php?action=validate")
    suspend fun validatePromoCode(
        @Query("code") code: String
    ): Response<ValidatePromoCodeResponse>
    
    // Reviews
    @GET("reviews.php")
    suspend fun getReviews(
        @Query("product_id") productId: Int? = null,
        @Query("restaurant_id") restaurantId: Int? = null,
        @Query("platform") platform: Boolean = false
    ): Response<ReviewsResponse>
    
    @POST("reviews.php")
    suspend fun submitReview(
        @Body request: SubmitReviewRequest
    ): Response<ApiResponse<*>>
    
    // Settings
    @GET("settings.php")
    suspend fun getSettings(): Response<Setting>
    
    // Rider
    @GET("rider-orders.php")
    suspend fun getRiderOrders(
        @Query("status") status: String? = null
    ): Response<RiderOrdersResponse>
    
    @GET("rider-orders.php")
    suspend fun getRiderOrderById(
        @Query("id") id: Int
    ): Response<RiderOrdersResponse>
    
    @PUT("rider-orders.php")
    suspend fun updateRiderOrderStatus(
        @Body request: UpdateOrderStatusRequest
    ): Response<ApiResponse<*>>
    
    @PUT("rider-orders.php")
    suspend fun updateRiderLocation(
        @Body request: UpdateRiderLocationRequest
    ): Response<ApiResponse<*>>
    
    @GET("rider-location.php")
    suspend fun getRiderLocation(@Query("rider_id") riderId: Int): Response<Map<String, Any>>
}
