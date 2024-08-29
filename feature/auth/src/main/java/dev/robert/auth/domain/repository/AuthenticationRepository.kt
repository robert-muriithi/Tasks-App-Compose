package dev.robert.auth.domain.repository

import dev.robert.auth.domain.model.GoogleUser
import dev.robert.auth.domain.model.RegisterRequestBody
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    fun loginWithEmailAndPassword(email: String, password: String): Flow<Result<GoogleUser?>>
    suspend fun clearUserData()
    suspend fun registerWithEmailAndPassword(body: RegisterRequestBody): Flow<Result<GoogleUser?>>
    fun resetPassword(email: String): Flow<Result<Unit>>
    val userLoggedIn: Boolean
    suspend fun getUserFromFirestore(uid: String): GoogleUser?
    val userId: Flow<String>
    // val getUser: Flow<GoogleUser?>
}
