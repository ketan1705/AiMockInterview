package com.ken.aimockinterview.models.wrapper

import com.ken.aimockinterview.models.UserAnswerResponse

data class UserResponseWrapper(
    val responses: List<UserAnswerResponse> = emptyList(),
)