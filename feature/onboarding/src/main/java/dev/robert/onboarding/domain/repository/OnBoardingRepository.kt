package dev.robert.onboarding.domain.repository

import kotlinx.coroutines.flow.Flow

interface OnBoardingRepository {
    val isOnboarded: Flow<Boolean>
    suspend fun setOnboarded(isOnboarded: Boolean)
}