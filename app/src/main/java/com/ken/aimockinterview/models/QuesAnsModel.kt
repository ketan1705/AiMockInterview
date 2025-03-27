package com.ken.aimockinterview.models

import kotlinx.serialization.Serializable

@Serializable
data class QuesAnsModel(
    val question: String,
    val answer: String,
)
