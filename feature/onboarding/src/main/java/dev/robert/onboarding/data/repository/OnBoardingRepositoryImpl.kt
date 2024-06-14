package dev.robert.onboarding.data.repository

import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.onboarding.domain.repository.OnBoardingRepository
import kotlinx.coroutines.flow.Flow

class OnBoardingRepositoryImpl(
    private val prefs: TodoAppPreferences
) : OnBoardingRepository {
    override val isOnboarded: Flow<Boolean>
        get() = prefs.onboardingCompleted

    override suspend fun setOnboarded(isOnboarded: Boolean) {
        prefs.saveOnboardingCompleted(isOnboarded)
    }
}
