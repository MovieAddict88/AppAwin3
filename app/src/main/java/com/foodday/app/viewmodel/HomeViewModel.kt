package com.foodday.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodday.app.data.model.*
import com.foodday.app.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {
    
    private val _categories = MutableLiveData<Resource<List<Category>>>()
    val categories: LiveData<Resource<List<Category>>> = _categories
    
    private val _featuredRestaurants = MutableLiveData<Resource<List<Restaurant>>>()
    val featuredRestaurants: LiveData<Resource<List<Restaurant>>> = _featuredRestaurants
    
    private val _featuredProducts = MutableLiveData<Resource<List<Product>>>()
    val featuredProducts: LiveData<Resource<List<Product>>> = _featuredProducts
    
    private val _banners = MutableLiveData<Resource<List<Banner>>>()
    val banners: LiveData<Resource<List<Banner>>> = _banners
    
    private val _settings = MutableLiveData<Resource<Setting>>()
    val settings: LiveData<Resource<Setting>> = _settings
    
    fun loadHomeData() {
        loadCategories()
        loadFeaturedRestaurants()
        loadFeaturedProducts()
        loadBanners()
        loadSettings()
    }
    
    fun loadCategories() {
        foodRepository.getCategories().onEach { result ->
            _categories.value = result
        }.launchIn(viewModelScope)
    }
    
    fun loadFeaturedRestaurants() {
        foodRepository.getRestaurants(featured = true, limit = 10).onEach { result ->
            _featuredRestaurants.value = result
        }.launchIn(viewModelScope)
    }
    
    fun loadFeaturedProducts() {
        foodRepository.getProducts(featured = true, limit = 10).onEach { result ->
            _featuredProducts.value = result
        }.launchIn(viewModelScope)
    }
    
    fun loadBanners() {
        foodRepository.getBanners("home").onEach { result ->
            _banners.value = result
        }.launchIn(viewModelScope)
    }
    
    fun loadSettings() {
        foodRepository.getSettings().onEach { result ->
            _settings.value = result
        }.launchIn(viewModelScope)
    }
}
