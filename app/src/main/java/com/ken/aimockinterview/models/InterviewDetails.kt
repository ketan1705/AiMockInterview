package com.ken.aimockinterview.models

import java.util.UUID

data class InterviewDetails(
    var jobRole: String = "",
    var jobDescription: String = "",
    var jobExperience: String = "",
    var createBy: String = "",
    var createAt: Long = System.currentTimeMillis(),
    var mockId: String = UUID.randomUUID().toString(),
    var jsonMockResp: String = "",
)
/*
data class InterviewDetails(

    val jsonMockResp: String,
    val jobRole: String,
    val jobDescription: String,
    val jobExperience: String,
    val createBy: String,
    val createAt: Long = System.currentTimeMillis(),
    val mockId: String = UUID.randomUUID().toString(),
//    val id: String = "${createBy}_${createAt}",
)
*/
