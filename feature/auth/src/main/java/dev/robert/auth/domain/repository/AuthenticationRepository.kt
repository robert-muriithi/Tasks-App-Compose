package dev.robert.auth.domain.repository

import dev.robert.auth.domain.model.GoogleUser
import dev.robert.auth.domain.model.RegisterRequestBody
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun login(email: String, password: String): Flow<Result<GoogleUser?>>
    suspend fun logout()
    suspend fun register(body: RegisterRequestBody): Flow<Result<GoogleUser?>>

    fun resetPassword(email: String): Flow<Result<Unit>>
    val userLoggedIn: Boolean
}
