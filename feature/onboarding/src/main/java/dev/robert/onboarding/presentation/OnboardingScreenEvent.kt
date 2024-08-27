package dev.robert.onboarding.presentation

sealed class OnboardingScreenEvent {
    data object OnboardingCompleted : OnboardingScreenEvent()
}
