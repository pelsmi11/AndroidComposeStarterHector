package com.example.androidcomposestarterhector.feature.register.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcomposestarterhector.core.ui.components.model.ErrorStatus
import com.example.androidcomposestarterhector.feature.login.domain.usecase.ValidateEmailUseCase
import com.example.androidcomposestarterhector.feature.login.domain.usecase.ValidatePasswordUseCase
import com.example.androidcomposestarterhector.feature.register.domain.usecase.ValidateConfirmPasswordUseCase
import com.example.androidcomposestarterhector.feature.register.domain.usecase.ValidateLastnameUseCase
import com.example.androidcomposestarterhector.feature.register.domain.usecase.ValidateNameUseCase
import com.example.androidcomposestarterhector.feature.register.domain.usecase.ValidateRolUseCase
import com.example.androidcomposestarterhector.feature.register.presentation.screen.model.RegisterFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateLastNameUseCase: ValidateLastnameUseCase,
    private val validateRolUseCase: ValidateRolUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase
) : ViewModel() {


    private val _formState = MutableStateFlow(RegisterFormState())
    val formState: StateFlow<RegisterFormState> = _formState

    private val _uiEvent = MutableStateFlow<RegisterUiEvent?>(null)
    val uiEvent: StateFlow<RegisterUiEvent?> = _uiEvent

    fun onNameChange(newValue: String) {
        val current = _formState.value
        val updatedNameField = current.nameField.copy(
            value = newValue,
            hasInteracted = true
        )
        val validationResult = validateNameUseCase(newValue)
        val updatedNameError = if (validationResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(validationResult.errorMessage ?: "Error desconocido")
        }
        _formState.value = current.copy(
            nameField = updatedNameField,
            nameError = updatedNameError
        )
    }

    fun onLastNameChange(newValue: String) {
        val current = _formState.value
        val updatedLastNameField = current.lastnameField.copy(
            value = newValue,
            hasInteracted = true
        )
        val validationResult = validateLastNameUseCase(newValue)
        val updatedLastNameError = if (validationResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(validationResult.errorMessage ?: "Error desconocido")
        }
        _formState.value = current.copy(
            lastnameField = updatedLastNameField,
            lastnameError = updatedLastNameError
        )
    }

    fun onRolChange(newValue: String) {
        val current = _formState.value
        val updatedRolField = current.rolField.copy(
            value = newValue,
            hasInteracted = true
        )
        val validationResult = validateRolUseCase(newValue)
        val updatedRolError = if (validationResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(validationResult.errorMessage ?: "Error desconocido")
        }
        _formState.value = current.copy(
            rolField = updatedRolField,
            rolError = updatedRolError
        )
    }

    fun onEmailChange(newValue: String) {
        val current = _formState.value
        // Marcamos que el usuario ya interactuó
        val updatedEmailField = current.emailField.copy(
            value = newValue,
            hasInteracted = true
        )
        // Validamos con use case
        val validationResult = validateEmailUseCase(newValue)
        val updatedEmailError = if (validationResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(validationResult.errorMessage ?: "Error desconocido")
        }

        _formState.value = current.copy(
            emailField = updatedEmailField,
            emailError = updatedEmailError
        )
    }

    fun onPasswordChange(newValue: String) {
        val current = _formState.value
        val updatedPasswordField = current.passwordField.copy(
            value = newValue,
            hasInteracted = true
        )
        val validationResult = validatePasswordUseCase(newValue)
        val updatedPasswordError = if (validationResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(validationResult.errorMessage ?: "Error desconocido")
        }

        _formState.value = current.copy(
            passwordField = updatedPasswordField,
            passwordError = updatedPasswordError
        )
    }

    fun onConfirmPasswordChange(newValue: String) {
        val current = _formState.value

        // Actualizamos el campo de Confirmar Contraseña
        val updatedConfirmPasswordField = current.confirmPasswordField.copy(
            value = newValue,
            hasInteracted = true
        )

        // Validamos la confirmación de contraseña usando el use case
        val validationResult = validateConfirmPasswordUseCase(
            password = current.passwordField.value,
            confirmPassword = newValue
        )

        // Actualizamos el estado del error basado en el resultado de la validación
        val updatedConfirmPasswordError = if (validationResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(validationResult.errorMessage ?: "Error desconocido")
        }

        // Actualizamos el estado del formulario
        _formState.value = current.copy(
            confirmPasswordField = updatedConfirmPasswordField,
            confirmPasswordError = updatedConfirmPasswordError
        )
    }

    fun handleRegister() {
        val current = _formState.value

        // 1) Forzamos hasInteracted en ambos campos
        val forcedNameField = current.nameField.copy(hasInteracted = true)
        val forcedLastNameField = current.lastnameField.copy(hasInteracted = true)
        val forcedRolField = current.rolField.copy(hasInteracted = true)
        val forcedEmailField = current.emailField.copy(hasInteracted = true)
        val forcedPasswordField = current.passwordField.copy(hasInteracted = true)
        val forcedConfirmPasswordField = current.confirmPasswordField.copy(hasInteracted = true)

        // 2) Re-ejecutamos la validación
        val nameResult = validateNameUseCase(forcedNameField.value)
        val lastNameResult = validateLastNameUseCase(forcedLastNameField.value)
        val rolResult = validateRolUseCase(forcedRolField.value)
        val emailResult = validateEmailUseCase(forcedEmailField.value)
        val passResult = validatePasswordUseCase(forcedPasswordField.value)
        val confirmPassResult = validateConfirmPasswordUseCase(
            password = forcedPasswordField.value,
            confirmPassword = forcedConfirmPasswordField.value
        )

        val newNameError = if (nameResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(nameResult.errorMessage ?: "Error desconocido")
        }

        val newLastNameError = if (lastNameResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(lastNameResult.errorMessage ?: "Error desconocido")
        }

        val newRolError = if (rolResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(rolResult.errorMessage ?: "Error desconocido")
        }

        val newEmailError = if (emailResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(emailResult.errorMessage ?: "Error desconocido")
        }

        val newPasswordError = if (passResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(passResult.errorMessage ?: "Error desconocido")
        }

        val newConfirmPasswordError = if (confirmPassResult.successful) {
            ErrorStatus.noError()
        } else {
            ErrorStatus.error(confirmPassResult.errorMessage ?: "Error desconocido")
        }

        // 3) Actualizamos el state con los nuevos 'hasInteracted' y posibles errores
        val newFormState = current.copy(
            nameField = forcedNameField,
            nameError = newNameError,
            lastnameField = forcedLastNameField,
            lastnameError = newLastNameError,
            rolField = forcedRolField,
            emailField = forcedEmailField,
            emailError = newEmailError,
            passwordField = forcedPasswordField,
            passwordError = newPasswordError,
            confirmPasswordField = forcedConfirmPasswordField,
            confirmPasswordError = newConfirmPasswordError
        )
        _formState.value = newFormState

        // 4) Verificamos si hay errores: si no, continuamos con “register remoto”
        if (!newEmailError.isError && !newPasswordError.isError) {
            // Activamos loading
            _formState.value = _formState.value.copy(isLoadingRegister = true)
            // Login remoto
            viewModelScope.launch {
                println("Formulario enviado")
            }
        }

    }

    fun consumeEvent() {
        _uiEvent.value = null
    }
}

sealed class RegisterUiEvent {
    object NavigateHome : RegisterUiEvent()
    data class ShowError(val message: String) : RegisterUiEvent()
    // Podrías agregar otros eventos (MostrarSnackBar, etc.)
}