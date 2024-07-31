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
        mAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
        prefs: TodoAppPreferences
    ): AuthenticationRepository = AuthenticationRepositoryImpl(mAuth = mAuth, firestore = firestore, preferences = prefs)
}
