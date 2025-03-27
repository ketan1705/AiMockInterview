package com.ken.aimockinterview.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ken.aimockinterview.models.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_session")

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {
    private val dataStore = context.dataStore

    companion object {

        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val MOCK_ID = stringPreferencesKey("mock_id")
    }


    private val _userData = MutableStateFlow(User("", "", ""))
    val userData: StateFlow<User> = _userData

    private val _mockId = MutableStateFlow("")
    val mockId: StateFlow<String> = _mockId

    init {
        // Load user data into StateFlow when the SessionManager is initialized
        observeUserData()
        observeMockId()
    }

    suspend fun saveUser(
        userId: String,
        userName: String,
        userEmail: String,
    ) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USER_NAME] = userName
            preferences[USER_EMAIL] = userEmail
        }
    }

    suspend fun saveMockId(
        mockId: String,
    ) {
        dataStore.edit { preferences ->
            preferences[MOCK_ID] = mockId
        }
    }


     fun observeUserData() {
        dataStore.data.map { preferences ->
            User(
                userId = preferences[USER_ID] ?: "",
                name = preferences[USER_NAME] ?: "",
                email = preferences[USER_EMAIL] ?: ""
            )
        }.onEach { user ->
            _userData.value = user
        }.launchIn(GlobalScope) // Consider replacing with app-wide CoroutineScope
    }

     fun observeMockId() {
        dataStore.data.map { preferences ->
            preferences[MOCK_ID] ?: ""
        }.onEach { id ->
            _mockId.value = id
        }.launchIn(GlobalScope)
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}