package com.foodday.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodday.app.data.model.*
import com.foodday.app.data.repository.FoodRepository
import com.foodday.app.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    
    private val _products = MutableLiveData<Resource<List<Product>>>()
    val products: LiveData<Resource<List<Product>>> = _products
    
    private val _productDetail = MutableLiveData<Resource<Product>>()
    val productDetail: LiveData<Resource<Product>> = _productDetail
    
    private val _restaurants = MutableLiveData<Resource<List<Restaurant>>>()
    val restaurants: LiveData<Resource<List<Restaurant>>> = _restaurants
    
    private val _restaurantDetail = MutableLiveData<Resource<Restaurant>>()
    val restaurantDetail: LiveData<Resource<Restaurant>> = _restaurantDetail
    
    private val _reviews = MutableLiveData<Resource<List<Review>>>()
    val reviews: LiveData<Resource<List<Review>>> = _reviews
    
    private val _submitReviewState = MutableLiveData<Resource<Boolean>>()
    val submitReviewState: LiveData<Resource<Boolean>> = _submitReviewState
    
    private val _addFavoriteState = MutableLiveData<Resource<Boolean>>()
    val addFavoriteState: LiveData<Resource<Boolean>> = _addFavoriteState
    
    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery
    
    fun getProducts(
        categoryId: Int? = null,
        restaurantId: Int? = null,
        search: String? = null,
        featured: Boolean? = null
    ) {
        _searchQuery.value = search
        foodRepository.getProducts(categoryId, restaurantId, search, featured).onEach { result ->
            _products.value = result
        }.launchIn(viewModelScope)
    }
    
    fun getProductById(productId: Int) {
        foodRepository.getProductById(productId).onEach { result ->
            _productDetail.value = result
        }.launchIn(viewModelScope)
    }
    
    fun getRestaurants(featured: Boolean? = null, search: String? = null) {
        foodRepository.getRestaurants(featured, search).onEach { result ->
            _restaurants.value = result
        }.launchIn(viewModelScope)
    }
    
    fun getRestaurantById(restaurantId: Int) {
        foodRepository.getRestaurantById(restaurantId).onEach { result ->
            _restaurantDetail.value = result
        }.launchIn(viewModelScope)
    }
    
    fun getReviews(productId: Int? = null, restaurantId: Int? = null) {
        foodRepository.getReviews(productId, restaurantId).onEach { result ->
            _reviews.value = result
        }.launchIn(viewModelScope)
    }
    
    fun submitReview(
        rating: Int,
        comment: String,
        productId: Int? = null,
        restaurantId: Int? = null,
        orderId: Int? = null
    ) {
        val request = SubmitReviewRequest(productId, restaurantId, orderId, rating, comment)
        foodRepository.submitReview(request).onEach { result ->
            _submitReviewState.value = result
        }.launchIn(viewModelScope)
    }
    
    fun addToFavorites(productId: Int) {
        favoriteRepository.addFavorite(productId).onEach { result ->
            _addFavoriteState.value = result
        }.launchIn(viewModelScope)
    }
    
    fun resetAddFavoriteState() {
        _addFavoriteState.value = Resource.Success(false)
    }
    
    fun resetSubmitReviewState() {
        _submitReviewState.value = Resource.Success(false)
    }
}
