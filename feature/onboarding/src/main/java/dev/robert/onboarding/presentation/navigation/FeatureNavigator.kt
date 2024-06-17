package dev.robert.onboarding.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.robert.onboarding.presentation.OnBoarding
import kotlinx.serialization.Serializable

@Serializable
object OnBoardingScreen

@Serializable
object OnBoardingNavGraph

fun NavGraphBuilder.onBoardingNavGraph(
    onCompleteOnBoarding: () -> Unit
) {
    navigation<OnBoardingNavGraph>(
        startDestination = OnBoardingScreen,
    ) {
        onBoardingScreen(
            onCompleteOnBoarding = onCompleteOnBoarding
        )
    }
}

fun NavGraphBuilder.onBoardingScreen(
    onCompleteOnBoarding: () -> Unit,
) {
    composable<OnBoardingScreen> {
        OnBoarding(
            onCompleteOnBoarding = onCompleteOnBoarding,
        )
    }
}
