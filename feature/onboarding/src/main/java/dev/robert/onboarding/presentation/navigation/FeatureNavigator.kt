package dev.robert.onboarding.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.robert.onboarding.presentation.OnBoarding
import kotlinx.serialization.Serializable

@Serializable
object OnBoardingScreen

fun NavGraphBuilder.onBoardingScreen(
    onCompleteOnBoarding: () -> Unit,
) {
    composable<OnBoardingScreen> {
        OnBoarding(
            onCompleteOnBoarding = onCompleteOnBoarding,
        )
    }
}
