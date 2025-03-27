package com.ken.aimockinterview.models.wrapper

import com.ken.aimockinterview.models.QuesAnsModel

data class GeminiQuestionsResp(
    val questions: List<QuesAnsModel> = emptyList(),
)