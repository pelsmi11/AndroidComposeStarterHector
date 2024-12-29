package com.example.androidcomposestarterhector.feature.login.domain.usecase

class ValidateEmailUseCase {
    // Regex que valida emails con:
    // - Letras y dígitos
    // - Símbolos especiales (. + _ -)
    // - Dominio con al menos 2 letras (p.ej. .com, .mx, .ar)
    private val emailRegex = Regex(
        pattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
        option = RegexOption.IGNORE_CASE
    )

    operator fun invoke(email: String): ValidationResult {
        // 1) Verificar si está vacío
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "El correo está vacío 🙃"
            )
        }

        // 2) Verificar si cumple con el patrón
        if (!emailRegex.matches(email)) {
            return ValidationResult(
                successful = false,
                errorMessage = "El correo no es válido 🤔"
            )
        }

        // 3) Si pasa ambos checks => es válido
        return ValidationResult(true, null)
    }
}