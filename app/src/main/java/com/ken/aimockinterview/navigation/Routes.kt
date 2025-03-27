package com.ken.aimockinterview.navigation

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.ken.aimockinterview.components.OnboardingItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

sealed class Routes() {

    @Serializable
    object Splash : Routes()

    @Serializable
    data class OnBoarding(val onboardingItems: List<OnboardingItem>) : Routes()

    @Serializable
    object Login : Routes()

    @Serializable
    object Register : Routes()

    @Serializable
    object Home : Routes()

    @Serializable
    data class Interview(
        val jobRole: String,
        val jobDescription: String,
        val experience: String,
        val questionsList: String,
        val mockId: String,
    ) : Routes()

    @Serializable
    data class Feedback(val mockId: String) : Routes()

}

val onBoardingType = object : NavType<List<OnboardingItem>>(false) {
    override fun get(
        bundle: Bundle,
        key: String,
    ): List<OnboardingItem>? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelableArrayList(key, OnboardingItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelableArrayList(key)
        }
    }

    override fun parseValue(value: String): List<OnboardingItem> {
        return Json.decodeFromString(value)
    }

    override fun put(
        bundle: Bundle,
        key: String,
        value: List<OnboardingItem>,
    ) {
        bundle.putParcelableArrayList(key, ArrayList(value))
    }

    override fun serializeAsValue(value: List<OnboardingItem>): String {
        return Json.encodeToString(value)
    }
}