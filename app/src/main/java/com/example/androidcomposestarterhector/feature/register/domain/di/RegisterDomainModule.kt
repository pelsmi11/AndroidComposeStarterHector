package com.example.androidcomposestarterhector.feature.register.domain.di

import com.example.androidcomposestarterhector.feature.register.domain.usecase.ValidateConfirmPasswordUseCase
import com.example.androidcomposestarterhector.feature.register.domain.usecase.ValidateLastnameUseCase
import com.example.androidcomposestarterhector.feature.register.domain.usecase.ValidateNameUseCase
import com.example.androidcomposestarterhector.feature.register.domain.usecase.ValidateRolUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RegisterDomainModule {
    @Provides
    fun provideValidateNameUseCase() = ValidateNameUseCase()

    @Provides
    fun provideValidateLastNameUseCase() = ValidateLastnameUseCase()

    @Provides
    fun provideValidateRoleUseCase() = ValidateRolUseCase()

    @Provides
    fun provideValidateConfirmPasswordUseCase() = ValidateConfirmPasswordUseCase()
}