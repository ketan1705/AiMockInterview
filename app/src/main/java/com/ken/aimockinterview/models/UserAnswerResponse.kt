package com.ken.aimockinterview.models

data class UserAnswerResponse(
    val question: String = "",
    val correctAns: String = "",
    val userAns: String = "",
    var mockId: String = "",
    var feedback: String = "",
    var rating: String = "",
    var userId: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val questionId: String = "",
)
