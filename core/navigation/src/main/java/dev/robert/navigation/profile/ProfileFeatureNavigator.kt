package dev.robert.navigation.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.robert.profile.presentation.screens.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
object ProfileNavGraph

@Serializable
object ProfileScreen

fun NavGraphBuilder.profileNavGraph() {
    navigation<ProfileNavGraph>(
        startDestination = ProfileScreen,
    ) {
        profileScreen()
    }
}

fun NavGraphBuilder.profileScreen() {
    composable<ProfileScreen> {
        ProfileScreen()
    }
}
