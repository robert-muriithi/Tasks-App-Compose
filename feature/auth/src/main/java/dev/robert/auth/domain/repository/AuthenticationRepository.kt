package dev.robert.auth.domain.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun login(email: String, password: String): Flow<Result<FirebaseUser?>>
    suspend fun logout()
    suspend fun register(email: String, password: String)

    fun resetPassword(email: String): Flow<Result<Unit>>
}
