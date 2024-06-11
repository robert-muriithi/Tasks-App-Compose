package dev.robert.auth.presentation.screens.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.auth.presentation.utils.EmailValidator
import dev.robert.auth.presentation.utils.PasswordMatchValidator
import dev.robert.auth.presentation.utils.PasswordValidator

@Module
@InstallIn(SingletonComponent::class)
abstract class ValidatorsModule {
    @[Binds] abstract fun bindPasswordMatchValidator(validator: PasswordMatchValidator): PasswordMatchValidator
    @[Binds] abstract fun bindEmailValidator(validator: EmailValidator): EmailValidator
    @[Binds] abstract fun bindPasswordValidator(validator: PasswordValidator): PasswordValidator
}
