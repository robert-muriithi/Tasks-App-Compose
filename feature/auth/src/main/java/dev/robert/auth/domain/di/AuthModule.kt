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
package dev.robert.auth.domain.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.auth.data.repositoy.AuthenticationRepositoryImpl
import dev.robert.auth.domain.repository.AuthenticationRepository
import dev.robert.datastore.data.TodoAppPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @[
    Provides
    Singleton
    ]
    fun getInstance() = FirebaseAuth.getInstance()

    @[
    Provides
    Singleton
    ]
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    @[
    Provides
    Singleton
    ]
    fun provideAuthRepository(
        prefs: TodoAppPreferences,
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthenticationRepository = AuthenticationRepositoryImpl(prefs = prefs, firestore = firestore, firebaseAuth = auth)
}
