package dev.robert.auth.data.repositoy

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dev.robert.auth.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class AuthenticationRepositoryImpl(
    private val mAuth: FirebaseAuth
) : AuthenticationRepository {
    override fun login(email: String, password: String): Flow<Result<FirebaseUser?>> =
        callbackFlow {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val user = it.user
                    this.trySend(Result.success(user)).isSuccess
                    close()
                }
                .addOnFailureListener { exception ->
                    this.trySend(Result.failure(exception)).isSuccess
                    close()
                }
        }.catch { throwable ->
            Result.failure<Throwable>(throwable)
        }

    override suspend fun logout() {
    }

    override suspend fun register(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun resetPassword(email: String): Flow<Result<Unit>> = flow {
        mAuth.sendPasswordResetEmail(email)
        emit(Result.success(Unit))
    }.catch {
        emit(Result.failure(it))
    }
}
