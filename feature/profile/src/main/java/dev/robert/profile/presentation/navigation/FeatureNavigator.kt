package dev.robert.profile.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.robert.profile.presentation.screens.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
object ProfileNavGraph

@Serializable
object ProfileScreen

fun NavGraphBuilder.profileNavGraph(
    onNavigateUp: () -> Unit,
) {
    navigation<ProfileNavGraph>(
        startDestination = ProfileScreen,
    ) {
        profileScreen(
            onNavigateBack = onNavigateUp,
        )
    }
}

fun NavGraphBuilder.profileScreen(
    onNavigateBack: () -> Unit,
) {
    composable<ProfileScreen> {
        ProfileScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}
