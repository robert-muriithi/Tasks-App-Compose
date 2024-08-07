package dev.robert.profile.presentation.screens

sealed class ProfileScreenEvents {
    object LoadProfile : ProfileScreenEvents()
    object EditProfile : ProfileScreenEvents()
    data class ShowError(val message: String) : ProfileScreenEvents()
}
