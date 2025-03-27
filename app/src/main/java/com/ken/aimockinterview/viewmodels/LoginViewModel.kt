package com.ken.aimockinterview.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ken.aimockinterview.repository.AuthRepository
import com.ken.aimockinterview.repository.UserRepository
import com.ken.aimockinterview.states.AuthState
import com.ken.aimockinterview.utils.Resource
import com.ken.aimockinterview.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _authState = Channel<AuthState>()
    val loginState = _authState.receiveAsFlow()

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        authRepository.loginUser(email, password).collect { result ->
            when (result) {
                is Resource.Success -> {
                    withContext(Dispatchers.IO) {
                        val userId = result.data?.user?.uid
                        userRepository.getUser(userId!!)
//                        getUser(userId!!)
                    }

                    _authState.send(AuthState(isSuccess = "Login Successfully"))
                }

                is Resource.Error -> {
                    _authState.send(AuthState(isError = "${result.message}"))
                }

                is Resource.Loading -> {
                    _authState.send(AuthState(isLoading = true))
                }
            }
        }
    }

    fun getUser(userId: String) {
        viewModelScope.launch {
            userRepository.getUser(userId)
        }
    }

    fun logoutUser() = viewModelScope.launch {
        authRepository.logoutUser().collect { result ->
            when (result) {
                is Resource.Success -> {
                    sessionManager.clearSession()
                    _authState.send(AuthState(isSuccess = "Logout Successfully"))
                }

                is Resource.Error -> {
                    _authState.send(AuthState(isError = "${result.message}"))
                }

                is Resource.Loading -> {
                    _authState.send(AuthState(isLoading = true))
                }
            }
        }
    }
}