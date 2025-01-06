package com.example.androidcomposestarterhector.feature.register.domain.usecase

import com.example.androidcomposestarterhector.feature.login.domain.usecase.ValidationResult

class ValidateNameUseCase : BaseNamesValidationUseCase() {
    operator fun invoke(name: String): ValidationResult {
        return validate(
            input = name,
            emptyMessage = "El nombre está vacío",
            invalidMessage = "El nombre solo puede contener letras, espacios, tildes, guiones y apóstrofes"
        )
    }
}