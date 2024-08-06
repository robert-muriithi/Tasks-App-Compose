package dev.robert.auth.data.repositoy

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.auth.data.model.GoogleUserDto
import dev.robert.auth.domain.mappers.toGoogleUser
import dev.robert.auth.domain.model.GoogleUser
import dev.robert.auth.domain.model.RegisterRequestBody
import dev.robert.auth.domain.repository.AuthenticationRepository
import dev.robert.datastore.data.TodoAppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class AuthenticationRepositoryImpl(
    private val mAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val preferences: TodoAppPreferences
) : AuthenticationRepository {
    override fun login(email: String, password: String): Flow<Result<GoogleUser?>> = flow {
        val user = mAuth.signInWithEmailAndPassword(email, password).await().user
        GoogleUserDto(
            email = user?.email ?: "",
            name = user?.displayName ?: "",
            photoUrl = user?.photoUrl?.toString() ?: "",
            id = user?.uid ?: ""
        ).toGoogleUser().also {
            preferences.saveUserLoggedIn(true)
            emit(Result.success(it))
        }
    }.catch {
        emit(Result.failure(it))
    }

    override suspend fun logout() {
        mAuth.signOut()
        preferences.saveUserLoggedIn(false)
    }

    override suspend fun register(body: RegisterRequestBody): Flow<Result<GoogleUser?>> = flow {
        val user = mAuth.createUserWithEmailAndPassword(body.email, body.password).await().user
        if (user != null) {
            user.sendEmailVerification().await()
            Timber.d("Email verification sent")
            GoogleUserDto(
                email = user.email ?: "",
                name = body.name,
                photoUrl = user.photoUrl?.toString() ?: "",
                id = user.uid
            ).also { storeUserData(it) }.also {
                emit(Result.success(it.toGoogleUser()))
            }
        }
    }.catch {
        emit(Result.failure(it))
    }

    override fun resetPassword(email: String): Flow<Result<Unit>> = flow {
        mAuth.sendPasswordResetEmail(email)
        emit(Result.success(Unit))
    }.catch {
        emit(Result.failure(it))
    }

    private suspend fun storeUserData(user: GoogleUserDto) {
        firestore.collection("users")
            .document(user.id)
            .set(user).await()
    }

    override val userLoggedIn: Boolean
        get() = mAuth.currentUser != null
}
