package com.example.androidcomposestarterhector.feature.register.domain.usecase

import com.example.androidcomposestarterhector.feature.login.domain.usecase.ValidationResult

class ValidateRolUseCase {
    operator fun invoke(rol: String?): ValidationResult {
        // 1. Validar si el rol es nulo o está vacío
        if (rol.isNullOrBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El rol no puede estar vacío"
            )
        }

        // 2. Validar si el rol tiene un valor válido
        val validRoles = listOf("1", "2") // Lista de roles válidos (puedes ajustar según necesidad)
        if (!validRoles.contains(rol)) {
            return ValidationResult(
                successful = false,
                errorMessage = "El rol seleccionado no es válido"
            )
        }

        // 3. Retornar validación exitosa si pasa las reglas
        return ValidationResult(successful = true, errorMessage = null)
    }
}