package com.foodday.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodday.app.data.model.*
import com.foodday.app.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    
    private val _notifications = MutableLiveData<Resource<List<NotificationItem>>>()
    val notifications: LiveData<Resource<List<NotificationItem>>> = _notifications
    
    private val _unreadCount = MutableLiveData<Int>()
    val unreadCount: LiveData<Int> = _unreadCount
    
    private val _markAsReadState = MutableLiveData<Resource<Boolean>>()
    val markAsReadState: LiveData<Resource<Boolean>> = _markAsReadState
    
    private val _deleteState = MutableLiveData<Resource<Boolean>>()
    val deleteState: LiveData<Resource<Boolean>> = _deleteState
    
    fun getNotifications() {
        notificationRepository.getNotifications().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _notifications.value = Resource.Success(result.data.first)
                    _unreadCount.value = result.data.second
                }
                is Resource.Error -> {
                    _notifications.value = Resource.Error(result.message)
                }
                is Resource.Loading -> {
                    _notifications.value = Resource.Loading()
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }
    
    fun markAsRead(notificationId: Int? = null) {
        notificationRepository.markAsRead(notificationId).onEach { result ->
            _markAsReadState.value = result
            if (result is Resource.Success) {
                getNotifications()
            }
        }.launchIn(viewModelScope)
    }
    
    fun deleteNotification(notificationId: Int? = null) {
        notificationRepository.deleteNotification(notificationId).onEach { result ->
            _deleteState.value = result
            if (result is Resource.Success) {
                getNotifications()
            }
        }.launchIn(viewModelScope)
    }
    
    fun resetStates() {
        _markAsReadState.value = Resource.Success(false)
        _deleteState.value = Resource.Success(false)
    }
}
