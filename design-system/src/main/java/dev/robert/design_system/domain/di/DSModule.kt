package dev.robert.design_system.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.datastore.data.TodoAppPreferences
import dev.robert.design_system.data.ThemeRepositoryImpl
import dev.robert.design_system.domain.ThemeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DSModule {
    @[
    Provides
    Singleton
    ]
    fun provideThemeRepository(prefs: TodoAppPreferences): ThemeRepository = ThemeRepositoryImpl(prefs = prefs)
}
