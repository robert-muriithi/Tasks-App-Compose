package dev.robert.profile.domain.repository

import dev.robert.profile.domain.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<Profile>
}
