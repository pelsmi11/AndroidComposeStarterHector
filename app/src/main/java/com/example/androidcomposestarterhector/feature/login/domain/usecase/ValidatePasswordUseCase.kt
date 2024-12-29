package com.example.androidcomposestarterhector.feature.login.domain.usecase

class ValidatePasswordUseCase {
    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña está vacía"
            )
        }

        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe tener al menos 8 caracteres"
            )
        }
        // Podrías agregar más reglas (longitud mínima, etc.)
        return ValidationResult(true, null)
    }
}