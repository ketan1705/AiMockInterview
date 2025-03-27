package com.ken.aimockinterview.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ken.aimockinterview.repository.AuthRepository
import com.ken.aimockinterview.states.AuthState
import com.ken.aimockinterview.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) :
    ViewModel() {

    private val _authState = Channel<AuthState>()
    val registerState = _authState.receiveAsFlow()

    fun registerUser(name: String, email: String, password: String) = viewModelScope.launch {
        authRepository.registerUser(name, email, password).collect { result ->
            when (result) {
                is Resource.Success -> {
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
}