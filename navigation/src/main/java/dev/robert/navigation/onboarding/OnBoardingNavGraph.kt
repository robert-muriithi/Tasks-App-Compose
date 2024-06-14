package dev.robert.navigation.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import dev.robert.navigation.auth.AuthNavGraph
import dev.robert.onboarding.presentation.navigation.OnBoardingScreen
import dev.robert.onboarding.presentation.navigation.onBoardingScreen
import kotlinx.serialization.Serializable

@Serializable
object OnBoardingNavGraph

fun NavGraphBuilder.onBoardingNavGraph(
    navController: NavController
) {
    navigation<OnBoardingNavGraph>(
        startDestination = OnBoardingScreen,
    ) {
        onBoardingScreen(
            onCompleteOnBoarding = {
                navController.navigate(
                    AuthNavGraph,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(navController.graph.id, true)
                        .build(),
                )
            },
        )
    }
}
