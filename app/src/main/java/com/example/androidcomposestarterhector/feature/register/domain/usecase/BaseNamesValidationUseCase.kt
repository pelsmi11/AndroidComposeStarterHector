package com.example.androidcomposestarterhector.feature.register.domain.usecase

import com.example.androidcomposestarterhector.feature.login.domain.usecase.ValidationResult

abstract class BaseNamesValidationUseCase {
    // Regex genérico para validar nombres o apellidos
    private val nameRegex = Regex(
        pattern = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ'’\\- ]+\$",
        option = RegexOption.IGNORE_CASE
    )

    // Método genérico para validación
    fun validate(input: String, emptyMessage: String, invalidMessage: String): ValidationResult {
        // Validar si está vacío
        if (input.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = emptyMessage
            )
        }

        // Validar si cumple con el patrón
        if (!nameRegex.matches(input)) {
            return ValidationResult(
                successful = false,
                errorMessage = invalidMessage
            )
        }

        // Validación exitosa
        return ValidationResult(successful = true, errorMessage = null)
    }
}