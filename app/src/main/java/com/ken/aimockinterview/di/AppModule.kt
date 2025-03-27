package com.ken.aimockinterview.di

import android.app.Application
import android.content.Context
import android.speech.SpeechRecognizer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ken.aimockinterview.repository.AuthRepository
import com.ken.aimockinterview.repository.AuthRepositoryImpl
import com.ken.aimockinterview.repository.GeminiRepository
import com.ken.aimockinterview.repository.TTSRepository
import com.ken.aimockinterview.repository.UserRepository
import com.ken.aimockinterview.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesSessionManager(@ApplicationContext context: Context) = SessionManager(context)

    @Provides
    @Singleton
    fun providesUserRepository(
        firebaseFirestore: FirebaseFirestore,
        sessionManager: SessionManager,
    ) =
        UserRepository(firebaseFirestore, sessionManager)

    @Provides
    @Singleton
    fun providesRepositoryImpl(
        firebaseAuth: FirebaseAuth,
        userRepository: UserRepository,
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, userRepository)
    }

    @Provides
    @Singleton
    fun providesGeminiRepository() = GeminiRepository()

    @Provides
    @Singleton
    fun provideSpeechRecognizer(app: Application): SpeechRecognizer {
        return SpeechRecognizer.createSpeechRecognizer(app)
    }

    @Provides
    @Singleton
    fun provideTTSRepository(@ApplicationContext context: Context) = TTSRepository(context)
}