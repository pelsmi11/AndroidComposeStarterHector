package com.example.androidcomposestarterhector.feature.login.presentation.screen.model

import com.example.androidcomposestarterhector.core.ui.components.model.ErrorStatus
import com.example.androidcomposestarterhector.core.ui.components.model.FieldInput

data class LoginFormState(
    val emailField: FieldInput = FieldInput(value = "", hasInteracted = false),
    val emailError: ErrorStatus = ErrorStatus.noError(),
    val passwordField: FieldInput = FieldInput(value = "", hasInteracted = false),
    val passwordError: ErrorStatus = ErrorStatus.noError(),
)