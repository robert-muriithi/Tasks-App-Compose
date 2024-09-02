package dev.robert.profile.domain.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.profile.data.ProfileRepositoryImpl
import dev.robert.profile.domain.repository.ProfileRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @[
    Provides
    Singleton
    ]
    fun provideProfileRepository(
        database: FirebaseFirestore,
        mAuth: FirebaseAuth,
        preferences: TodoAppPreferences
    ): ProfileRepository = ProfileRepositoryImpl(database, mAuth, preferences)
}
