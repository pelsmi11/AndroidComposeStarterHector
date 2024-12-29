package com.example.androidcomposestarterhector.feature.login.domain.di

import com.example.androidcomposestarterhector.feature.login.domain.usecase.ValidateEmailUseCase
import com.example.androidcomposestarterhector.feature.login.domain.usecase.ValidatePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LoginDomainModule {

    @Provides
    fun provideValidateEmailUseCase() = ValidateEmailUseCase()

    @Provides
    fun provideValidatePasswordUseCase() = ValidatePasswordUseCase()
}