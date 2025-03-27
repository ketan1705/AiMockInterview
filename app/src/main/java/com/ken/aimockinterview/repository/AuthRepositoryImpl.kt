package com.ken.aimockinterview.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.ken.aimockinterview.models.User
import com.ken.aimockinterview.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository,
) : AuthRepository {

    override fun loginUser(
        email: String,
        password: String,
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch { exception ->
            val errorMessage = getAuthErrorMessage(exception)
            emit(Resource.Error(errorMessage))
        }
    }

    override fun registerUser(
        name: String,
        email: String,
        password: String,
    ): Flow<Resource<AuthResult>> {

        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("User ID is null")
            val user = User(userId = userId, email = email, name = name)
            val firestoreResult = userRepository.saveUser(user)
            if (firestoreResult is Resource.Error)
                throw Exception(firestoreResult.message)
            emit(Resource.Success(result))
        }.catch { exception ->
            val errorMessage = getAuthErrorMessage(exception)
            emit(Resource.Error(errorMessage))
//            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun logoutUser(): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            firebaseAuth.currentUser?.let {
                firebaseAuth.signOut()
            }
            emit(Resource.Success(true))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    private fun getAuthErrorMessage(exception: Throwable): String {
        return when (exception) {
            is FirebaseAuthInvalidUserException -> "No User Found."
            is FirebaseAuthInvalidCredentialsException -> "Invalid email or password."
            is FirebaseAuthUserCollisionException -> "This email is already in use."
//            is FirebaseAuthWeakPasswordException -> "Password should be at least 6 characters long."
            is FirebaseAuthRecentLoginRequiredException -> "Please log in again to proceed."
            is FirebaseAuthException -> exception.message ?: "Authentication failed."
            else -> "Something went wrong. Please try again."
        }
    }
}