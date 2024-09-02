package dev.robert.settings.presentation.screens

data class SettingsState(
    val email: String = "",
    val name: String = "",
    val profileImageUrl: String = "",
    // val theme: Int = Theme.FOLLOW_SYSTEM.themeValue,
    val isLoading: Boolean = false,
    val selectedTheme: Int = 0
)
