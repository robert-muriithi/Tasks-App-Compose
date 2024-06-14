package dev.robert.onboarding.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.onboarding.data.repository.OnBoardingRepositoryImpl
import dev.robert.onboarding.domain.repository.OnBoardingRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {
    @[
    Provides
    Singleton
    ]
    fun bindOnBoardingRepository(preferences: TodoAppPreferences): OnBoardingRepository = OnBoardingRepositoryImpl(prefs = preferences)
}
