package dev.robert.settings.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.robert.design_system.presentation.components.TDButton
import dev.robert.design_system.presentation.theme.Theme as AppTheme
import dev.robert.settings.R
import dev.robert.settings.presentation.components.AboutDialog
import dev.robert.settings.presentation.components.ProfileSection
import dev.robert.settings.presentation.components.Section
import dev.robert.settings.presentation.components.SectionItem
import dev.robert.settings.presentation.components.SettingsActionsOptions
import dev.robert.settings.presentation.components.Theme
import dev.robert.settings.presentation.components.ThemeOption
import dev.robert.settings.presentation.components.ThemeToggleChips
import dev.robert.settings.presentation.components.themeFromValue

@Composable
fun SettingsScreen(
    onNavigateToProfile: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateToChangePassword: () -> Unit = {},
    onNaviagteToAbout: () -> Unit = {},
    onNavigateToRateApp: () -> Unit = {},
    onNavigateToReportIssue: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val theme by viewModel.themeValue.collectAsStateWithLifecycle()

    var showAboutDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(SettingsEvents.LoadSettings)
    }

    SettingsScreenContent(
        uiState = uiState,
        onChangeTheme = {
            viewModel.onEvent(SettingsEvents.ChangeTheme(it))
        },
        theme = theme,
        onNavigateToProfile = onNavigateToProfile,
        onLogout = {
            viewModel.onEvent(SettingsEvents.Logout)
            onNavigateToLogin()
        },
        onNavigateToChangePassword = onNavigateToChangePassword,
        onNaviagteToAbout = {
            showAboutDialog = true
        },
        onClickRateApp = onNavigateToRateApp,
        onClickReportIssue = onNavigateToReportIssue
    )

    if (showAboutDialog) {
        AboutDialog(
            onDismiss = {
                showAboutDialog = false
            },
            onNavigateToGithub = {
                showAboutDialog = false
                onNaviagteToAbout()
            }
        )
    }
}

@Composable
fun SettingsScreenContent(
    uiState: SettingsState,
    onChangeTheme: (AppTheme) -> Unit,
    theme: Int,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    onNavigateToChangePassword: () -> Unit = {},
    onNaviagteToAbout: () -> Unit = {},
    onClickRateApp: () -> Unit = {},
    onClickReportIssue: () -> Unit = {}
) {
    val themeOptions = listOf(
        ThemeOption(Theme.SYSTEM),
        ThemeOption(Theme.LIGHT),
        ThemeOption(Theme.DARK),
        ThemeOption(Theme.MATERIAL_YOU)
    )
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            ProfileSection(
                name = uiState.name,
                email = uiState.email,
                imageUrl = uiState.profileImageUrl,
                onClick = onNavigateToProfile
            )
            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )
            Text(
                text = "Theme",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )
            ThemeToggleChips(
                themeOptions = themeOptions,
                onSelectTheme = { theme ->
                    onChangeTheme(theme.themeFromValue())
                },
                theme = theme,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            SettingsActionsOptions(
                sections = listOf(
                    Section(
                        title = "Account",
                        items = listOf(
                            SectionItem(
                                title = "Profile",
                                subTitle = "View profile",
                                icon = R.drawable.ic_user,
                                action = onNavigateToProfile
                            ),
                            SectionItem(
                                title = "Security",
                                subTitle = "Change your password",
                                icon = R.drawable.ic_lock,
                                action = onNavigateToChangePassword
                            )
                        )
                    ),
                    Section(
                        title = "Help & Support",
                        items = listOf(
                            SectionItem(
                                title = "About",
                                subTitle = "Learn more about the app",
                                icon = R.drawable.ic_chat,
                                action = onNaviagteToAbout
                            ),
                            SectionItem(
                                title = "Rate App",
                                subTitle = "Star the app on Github",
                                icon = R.drawable.ic_rate,
                                action = onClickRateApp
                            ),
                            SectionItem(
                                title = "Report an Issue",
                                subTitle = "Report a bug or issue on Github",
                                icon = R.drawable.ic_exclamation,
                                action = onClickReportIssue
                            )
                        )
                    )
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Version 0.0.1-alpha",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(8.dp)
            )
            TDButton(
                onClick = onLogout,
                text = "Logout",
                enabled = true,
                isLoading = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreenContent(
        modifier = Modifier.fillMaxSize(),
        uiState = SettingsState(
            name = "Robert",
            email = "robert@gmail.com"
        ),
        onChangeTheme = {},
        theme = AppTheme.DARK_THEME.themeValue,
        onNavigateToProfile = {},
    )
}
