package com.example.androidcomposestarterhector.feature.register.domain.usecase

import com.example.androidcomposestarterhector.feature.login.domain.usecase.ValidationResult

class ValidateConfirmPasswordUseCase {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        // Validar si las contraseñas son iguales
        if (password != confirmPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "Las contraseñas no coinciden"
            )
        }
        // Validación exitosa
        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}