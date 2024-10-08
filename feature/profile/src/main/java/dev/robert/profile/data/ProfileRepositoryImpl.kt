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
package dev.robert.profile.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.profile.domain.model.Profile
import dev.robert.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl(
    private val database: FirebaseFirestore,
    private val mAuth: FirebaseAuth,
    private val preferences: TodoAppPreferences
) : ProfileRepository {
    override fun getProfileGoogleSignIn(): Flow<Profile> = flow {
        mAuth.currentUser?.let {
            emit(
                Profile(
                    name = it.displayName ?: "",
                    email = it.email ?: "",
                    photoUrl = it.photoUrl.toString(),
                    id = it.uid
                )
            )
        }
    }

    override fun getProfileFirebaseFirestore(): Flow<Result<Profile>> {
        return flow {
            val uid = preferences.userData.firstOrNull()?.id ?: ""
            database.collection(COLLECTION_PATH)
                .document(uid)
                .get()
                .await()
                .toObject(Profile::class.java)?.let { profile ->
                    emit(Result.success(profile))
                }
        }.catch {
            emit(Result.failure(it))
        }
    }

    companion object {
        const val COLLECTION_PATH = "users"
    }
}
