package com.example.androidcomposestarterhector.feature.register.presentation.screen.model

import com.example.androidcomposestarterhector.core.ui.components.model.ErrorStatus
import com.example.androidcomposestarterhector.core.ui.components.model.FieldInput

data class RegisterFormState(
    val nameField: FieldInput = FieldInput(value = "", hasInteracted = false),
    val nameError: ErrorStatus = ErrorStatus.noError(),
    val lastnameField: FieldInput = FieldInput(value = "", hasInteracted = false),
    val lastnameError: ErrorStatus = ErrorStatus.noError(),
    val rolField: FieldInput = FieldInput(value = "", hasInteracted = false),
    val rolError: ErrorStatus = ErrorStatus.noError(),
    val emailField: FieldInput = FieldInput(value = "", hasInteracted = false),
    val emailError: ErrorStatus = ErrorStatus.noError(),
    val passwordField: FieldInput = FieldInput(value = "", hasInteracted = false),
    val passwordError: ErrorStatus = ErrorStatus.noError(),
    val confirmPasswordField: FieldInput = FieldInput(value = "", hasInteracted = false),
    val confirmPasswordError: ErrorStatus = ErrorStatus.noError(),
    val isLoadingRegister: Boolean = false
)