package dev.robert.onboarding.domain.model

import androidx.annotation.DrawableRes

data class OnboardingItem(
    val title: String,
    val description: String,
    @DrawableRes val image: Int,
)
