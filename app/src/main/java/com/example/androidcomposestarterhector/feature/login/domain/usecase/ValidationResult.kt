package com.example.androidcomposestarterhector.feature.login.domain.usecase

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String?
)