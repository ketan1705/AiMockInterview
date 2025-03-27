package com.ken.aimockinterview.repository

import com.google.firebase.auth.AuthResult
import com.ken.aimockinterview.utils.Resource
import kotlinx.coroutines.flow.Flow


interface AuthRepository {

    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(name:String,email: String, password: String): Flow<Resource<AuthResult>>
    fun logoutUser(): Flow<Resource<Boolean>>

}