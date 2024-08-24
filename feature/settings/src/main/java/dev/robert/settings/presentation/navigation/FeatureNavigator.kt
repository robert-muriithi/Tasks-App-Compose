package dev.robert.settings.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import dev.robert.settings.presentation.screens.SettingsScreen
import dev.robert.settings.presentation.screens.change_password.ChangePasswordScreen
import kotlinx.serialization.Serializable

@Serializable
object SettingsNavGraph

@Serializable
object SettingsScreen

@Serializable
object ChangePasswordScreen

fun NavGraphBuilder.settingsNavGraph(
    onNavigateToProfile: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    onNavigateToRateApp: () -> Unit,
    onNavigateToReportIssue: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    navigation<SettingsNavGraph>(
        startDestination = SettingsScreen,
    ) {
        composable<SettingsScreen> {
            SettingsScreen(
                onNavigateToProfile = onNavigateToProfile,
                onNavigateToChangePassword = onNavigateToChangePassword,
                onNavigateToRateApp = onNavigateToRateApp,
                onNavigateToReportIssue = onNavigateToReportIssue,
                onNaviagteToAbout = onNavigateToAbout,
                onNavigateToLogin = onNavigateToLogin
            )
        }
        composable<ChangePasswordScreen> {
            ChangePasswordScreen()
        }
    }
}
