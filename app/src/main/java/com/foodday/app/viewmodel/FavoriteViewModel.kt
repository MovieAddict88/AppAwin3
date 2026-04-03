package com.foodday.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodday.app.data.model.*
import com.foodday.app.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    
    private val _favorites = MutableLiveData<Resource<List<Favorite>>>()
    val favorites: LiveData<Resource<List<Favorite>>> = _favorites
    
    private val _addFavoriteState = MutableLiveData<Resource<Boolean>>()
    val addFavoriteState: LiveData<Resource<Boolean>> = _addFavoriteState
    
    private val _removeFavoriteState = MutableLiveData<Resource<Boolean>>()
    val removeFavoriteState: LiveData<Resource<Boolean>> = _removeFavoriteState
    
    fun getFavorites() {
        favoriteRepository.getFavorites().onEach { result ->
            _favorites.value = result
        }.launchIn(viewModelScope)
    }
    
    fun addFavorite(productId: Int) {
        favoriteRepository.addFavorite(productId).onEach { result ->
            _addFavoriteState.value = result
            if (result is Resource.Success) {
                getFavorites()
            }
        }.launchIn(viewModelScope)
    }
    
    fun removeFavorite(productId: Int) {
        favoriteRepository.removeFavorite(productId).onEach { result ->
            _removeFavoriteState.value = result
            if (result is Resource.Success) {
                getFavorites()
            }
        }.launchIn(viewModelScope)
    }
    
    fun resetStates() {
        _addFavoriteState.value = Resource.Success(false)
        _removeFavoriteState.value = Resource.Success(false)
    }
}
