package com.ken.aimockinterview.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ken.aimockinterview.models.InterviewDetails
import com.ken.aimockinterview.models.QuesAnsModel
import com.ken.aimockinterview.models.ResumeData
import com.ken.aimockinterview.models.User
import com.ken.aimockinterview.models.UserAnswerResponse
import com.ken.aimockinterview.models.UserRatingFeedback
import com.ken.aimockinterview.repository.GeminiRepository
import com.ken.aimockinterview.repository.UserRepository
import com.ken.aimockinterview.utils.Constants
import com.ken.aimockinterview.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GeminiViewModel @Inject constructor(
    private val repository: GeminiRepository,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
) :
    ViewModel() {

    private val _questionsList = MutableStateFlow<List<QuesAnsModel>>(emptyList())
    val questionList: StateFlow<List<QuesAnsModel>>
        get() = _questionsList

    val userFeedback: StateFlow<List<UserAnswerResponse>>
        get() = userRepository.userFeedback

    val userRatingFeedback: StateFlow<UserRatingFeedback?>
        get() = repository.userRatingFeedback

    val resumeData: StateFlow<ResumeData?>
        get() = repository.resumeData

    private val userData: StateFlow<User>
        get() = sessionManager.userData

    private val mockId: StateFlow<String>
        get() = sessionManager.mockId

    init {
        viewModelScope.launch {
            sessionManager.observeUserData()
            sessionManager.observeMockId()
            Log.d(Constants.TAG, "GEMINI User ID: ${userData.value.userId}")
            Log.d(Constants.TAG, "GEMINI Mock ID: ${mockId.value}")
        }
    }

    // gemini repository methods
    fun getQuestions(jobRole: String, jobDescription: String, experienceYears: String) {

        viewModelScope.launch {
            val result = repository.getQuestions(jobRole, jobDescription, experienceYears)
            _questionsList.emit(result)
            val jsonMockResp = Gson().toJson(result)
            launch(Dispatchers.IO) {
                Log.d(Constants.TAG, "getQuestions USER ID: ${userData.value.userId}")
                if (userData.value.userId.isNotEmpty()) {
                    val interviewDetails = InterviewDetails(
                        jsonMockResp = jsonMockResp,
                        jobRole = jobRole,
                        jobDescription = jobDescription,
                        jobExperience = experienceYears,
                        createBy = userData.value.userId,
                    )
                    userRepository.saveMockInterview(userData.value.userId, interviewDetails)
                    sessionManager.observeMockId()
                }
            }
        }
    }

    // gemini repository methods
    fun getFeedback(question: String, userAnswer: String, userAnsResp: UserAnswerResponse) {
        viewModelScope.launch {
            repository.getFeedback(question, userAnswer)
            withContext(Dispatchers.IO) {
                addUserResponse(userAnsResp)
            }
        }
    }

    // gemini repository methods
    fun extractUserResume(resumeDetails: String) {
        viewModelScope.launch {
            repository.extractUserResume(resumeDetails)
        }
    }

    // user repository methods
    fun addUserResponse(userAnsResp: UserAnswerResponse) {
        viewModelScope.launch {
            if (mockId.value.isNotEmpty() && userData.value.userId.isNotEmpty() && userRatingFeedback.value != null) {
                userAnsResp.feedback = userRatingFeedback.value?.feedback ?: ""
                userAnsResp.rating = userRatingFeedback.value?.rating ?: ""
                userAnsResp.mockId = userAnsResp.mockId.ifEmpty { mockId.value }
                userAnsResp.userId = userData.value.userId
                userRepository.addUserResponse(mockId.value, userAnsResp)
            }
        }
    }

    // user repository methods
    fun getUserResponse(mockID: String = "") {
        viewModelScope.launch {
            userRepository.getUserResponse(mockID.ifEmpty { mockId.value })
        }
    }

    fun saveMockId(mockId: String) {
        viewModelScope.launch {
            sessionManager.saveMockId(mockId = mockId)
        }
    }

}
