package dev.robert.auth.presentation.screens.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.auth.presentation.utils.EmailValidator
import dev.robert.auth.presentation.utils.NameValidator
import dev.robert.auth.presentation.utils.PasswordMatchValidator
import dev.robert.auth.presentation.utils.PasswordValidator

@Module
@InstallIn(SingletonComponent::class)
object ValidatorsModule {
    @[Provides] fun bindPasswordMatchValidator(): PasswordMatchValidator = PasswordMatchValidator()
    @[Provides] fun bindPasswordValidator(): PasswordValidator = PasswordValidator()
    @[Provides]
    fun provideEmailValidator(): EmailValidator = EmailValidator()
    @[Provides]
    fun provideNameValidator(): NameValidator = NameValidator()
}
