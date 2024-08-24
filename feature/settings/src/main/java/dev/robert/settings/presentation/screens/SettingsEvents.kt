package dev.robert.settings.presentation.screens

import dev.robert.design_system.presentation.theme.Theme

sealed class SettingsEvents {
    data object LoadSettings : SettingsEvents()
    data class ChangeTheme(val theme: Theme) : SettingsEvents()
    data object Logout : SettingsEvents()
}
