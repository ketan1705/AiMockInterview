package com.ken.aimockinterview.states

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = "",
)
