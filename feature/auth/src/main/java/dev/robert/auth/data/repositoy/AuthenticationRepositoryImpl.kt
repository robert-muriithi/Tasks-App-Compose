/*
 * Copyright 2024 Robert Muriithi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.robert.auth.data.repositoy

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.auth.domain.model.GoogleUser
import dev.robert.auth.domain.model.RegisterRequestBody
import dev.robert.auth.domain.repository.AuthenticationRepository
import dev.robert.datastore.data.TodoAppPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class AuthenticationRepositoryImpl(
    private val prefs: TodoAppPreferences,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : AuthenticationRepository {

    override fun loginWithEmailAndPassword(email: String, password: String): Flow<Result<GoogleUser?>> = flow {
        val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user = result.toGoogleUser()
        val userFromFirestore = getUserFromFirestore(user.id)
        prefs.apply {
            saveUserLoggedIn(true)
            saveLoginType(TodoAppPreferences.LOGIN_TYPE_EMAIL)
            userFromFirestore?.let {
                saveUserData(uid = it.id, name = it.name, email = it.email, password = password)
            }
        }
        emit(Result.success(userFromFirestore))
    }.catch { e ->
        emit(Result.failure(e))
    }.flowOn(Dispatchers.IO)

    override suspend fun clearUserData() {
        prefs.saveUserLoggedIn(false)
        prefs.saveLoginType("")
        prefs.saveUserData("", "", "", "")
    }

    override suspend fun registerWithEmailAndPassword(body: RegisterRequestBody): Flow<Result<GoogleUser?>> = flow {
        val result = firebaseAuth.createUserWithEmailAndPassword(body.email, body.password).await()
        val user = result.toGoogleUser().copy(name = body.name)
        user.id.let { id ->
            firestore.collection("users").document(id).set(user).await()
            emit(Result.success(user))
        }
    }.catch {
        emit(Result.failure(it))
    }.flowOn(Dispatchers.IO)

    override fun resetPassword(email: String): Flow<Result<Unit>> {
        return flow {
            firebaseAuth.sendPasswordResetEmail(email).await()
            emit(Result.success(Unit))
        }.catch {
            emit(Result.failure(it))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUserFromFirestore(uid: String): GoogleUser? {
        val document = firestore.collection("users").document(uid).get().await()
        return if (document.exists()) {
            document.toObject(GoogleUser::class.java)
        } else {
            null
        }
    }

    override val userId: Flow<String>
        get() = flow {
            prefs.userData.firstOrNull()?.id?.let { emit(it) }
        }

    override val userLoggedIn: Boolean
        get() = firebaseAuth.currentUser != null

    private fun AuthResult.toGoogleUser(): GoogleUser {
        return GoogleUser(
            email = this.user?.email ?: "",
            name = this.user?.displayName ?: "",
            photoUrl = this.user?.photoUrl.toString(),
            id = this.user?.uid ?: ""
        )
    }
}
