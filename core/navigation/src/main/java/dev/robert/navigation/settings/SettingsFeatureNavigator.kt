/*
 * Copyright 2024 Robert Muriithi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.robert.navigation.settings

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
