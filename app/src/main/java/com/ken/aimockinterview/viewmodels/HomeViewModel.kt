package com.ken.aimockinterview.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ken.aimockinterview.models.InterviewDetails
import com.ken.aimockinterview.models.User
import com.ken.aimockinterview.repository.UserRepository
import com.ken.aimockinterview.utils.Constants
import com.ken.aimockinterview.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {


    val interviewList: StateFlow<List<InterviewDetails>>
        get() = userRepository.interviewList

    val userData: StateFlow<User>
        get() = sessionManager.userData

    init {
        viewModelScope.launch {
            sessionManager.observeUserData()
            Log.d(Constants.TAG, "User ID: ${userData.value.userId}")
            sessionManager.userData.collect { user ->
                getMockInterview()
            }
        }
    }

    fun getMockInterview() {
        viewModelScope.launch {
            userRepository.getMockInterview(
                userId = userData.value.userId
            )
        }
    }
}