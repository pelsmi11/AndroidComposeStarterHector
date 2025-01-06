package com.example.androidcomposestarterhector.feature.register.domain.usecase

import com.example.androidcomposestarterhector.feature.login.domain.usecase.ValidationResult

class ValidateLastnameUseCase : BaseNamesValidationUseCase() {
    operator fun invoke(lastname: String): ValidationResult {
        return validate(
            input = lastname,
            emptyMessage = "El apellido está vacío",
            invalidMessage = "El apellido solo puede contener letras, espacios, tildes, guiones y apóstrofes"
        )
    }
}