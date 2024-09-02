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
