package com.ken.aimockinterview.repository

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.ken.aimockinterview.models.InterviewDetails
import com.ken.aimockinterview.models.User
import com.ken.aimockinterview.models.UserAnswerResponse
import com.ken.aimockinterview.models.wrapper.UserResponseWrapper
import com.ken.aimockinterview.utils.Constants
import com.ken.aimockinterview.utils.Resource
import com.ken.aimockinterview.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val sessionManager: SessionManager,
) {

    private var _interviewList = MutableStateFlow<List<InterviewDetails>>(emptyList())
    val interviewList: StateFlow<List<InterviewDetails>>
        get() = _interviewList

    private val _userFeedback = MutableStateFlow<List<UserAnswerResponse>>(emptyList())
    val userFeedback: StateFlow<List<UserAnswerResponse>>
        get() = _userFeedback

    suspend fun getUser(userId: String) {
        try {
            val userSnapShot = firebaseFirestore
                .collection(Constants.USERS)
                .document(userId)
                .get().await()
            if (userSnapShot.exists()) {
                val user = userSnapShot.toObject(User::class.java)
                user?.let {
                    sessionManager.saveUser(it.userId, it.name, it.email)
                }
            } else {
                throw Exception("User Not Found")
            }
        } catch (e: Exception) {
            Log.d(Constants.TAG, "User Data: ${e.message}")
            throw e
        }
    }

    suspend fun saveUser(user: User): Resource<Boolean> {
        return try {
            firebaseFirestore.collection(Constants.USERS).document(user.userId).set(user).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown Error")
        }
    }

    suspend fun saveMockInterview(userId: String, interviewDetails: InterviewDetails) {
        try {
            firebaseFirestore.collection(Constants.USERS)
                .document(userId)
                .collection(Constants.MOCK_INTERVIEW)
                .document(interviewDetails.mockId)
                .set(interviewDetails)
                .await()

            sessionManager.saveMockId(interviewDetails.mockId)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getMockInterview(userId: String) {
        try {
            val interviewSnapShot = firebaseFirestore
                .collection(Constants.USERS)
                .document(userId)
                .collection(Constants.MOCK_INTERVIEW)
                .get().await()

            val userResponsesSnapshot = firebaseFirestore
                .collection(Constants.USER_RESPONSES)
                .get().await()

            val existingMock = userResponsesSnapshot.documents
                .mapNotNull {
                    it.id
                }.toSet()

            val interviews = interviewSnapShot.documents.mapNotNull { interview ->
                interview
                    .toObject(InterviewDetails::class.java)?.takeIf {
                        it.mockId in existingMock
                    }
            }
            _interviewList.value = interviews

        } catch (e: Exception) {
            Log.d(Constants.TAG, "Interview Data: ${e.message}")
        }
    }


    suspend fun addUserResponse(mockId: String, userAnsResp: UserAnswerResponse) {
        try {
            val document = firebaseFirestore
                .collection(Constants.USER_RESPONSES)
                .document(mockId)
            document.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    document.update(Constants.RESPONSES, FieldValue.arrayUnion(userAnsResp))
                } else {
                    val newData = hashMapOf("responses" to listOf(userAnsResp))
                    document.set(newData)
                }
            }.await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getUserResponse(mockId: String) {
        try {
            val snapshot = firebaseFirestore.collection(Constants.USER_RESPONSES)
                .document(mockId)
                .get()
                .await()
            if (snapshot.exists()) {
                val result = snapshot.toObject(UserResponseWrapper::class.java)
                Log.d(Constants.TAG, "User Response list: ${result?.responses}")
                result?.let { response ->
                    _userFeedback.emit(response.responses)
                }
            }

        } catch (e: Exception) {
            throw e
        }
    }
}