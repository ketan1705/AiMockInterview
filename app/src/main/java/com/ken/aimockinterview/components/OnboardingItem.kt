package com.ken.aimockinterview.components

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Parcelize
@Serializable
data class OnboardingItem(
    val image: Int,
    val title: String,
    val description: String,
) : Parcelable