package dev.robert.profile.presentation.screens

import dev.robert.profile.domain.model.Profile

data class ProfileScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val profile: Profile? = null,
    val loginType: String = ""
)
