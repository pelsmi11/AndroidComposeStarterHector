package com.example.androidcomposestarterhector.feature.login.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcomposestarterhector.core.data.TokenManager
import com.example.androidcomposestarterhector.core.ui.components.model.ErrorStatus
import com.example.androidcomposestarterhector.feature.login.data.repository.LoginRepository
import com.example.androidcomposestarterhector.feature.login.domain.usecase.ValidateEmailUseCase
import com.example.androidcomposestarterhector.feature.login.domain.usecase.ValidatePasswordUseCase
import com.example.androidcomposestarterhector.feature.login.presentation.screen.model.LoginFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val loginRepository: LoginRepository ,
    private val tokenManager: TokenManager
) : ViewModel() {


    private val _formState = MutableStateFlow(LoginFormState())
    val formState: StateFlow<LoginFormState> = _formState

    private val _uiEvent = MutableStateFlow<LoginUiEvent?>(null)
    val uiEvent: StateFlow<LoginUiEvent?> = _uiEvent

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

    fun login() {
        val current = _formState.value

        // 1) Forzamos hasInteracted en ambos campos
        val forcedEmailField = current.emailField.copy(hasInteracted = true)
        val forcedPasswordField = current.passwordField.copy(hasInteracted = true)

        // 2) Re-ejecutamos la validación
        val emailResult = validateEmailUseCase(forcedEmailField.value)
        val passResult = validatePasswordUseCase(forcedPasswordField.value)

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

        // 3) Actualizamos el state con los nuevos 'hasInteracted' y posibles errores
        val newFormState = current.copy(
            emailField = forcedEmailField,
            emailError = newEmailError,
            passwordField = forcedPasswordField,
            passwordError = newPasswordError
        )
        _formState.value = newFormState

        // 4) Verificamos si hay errores: si no, continuamos con “login remoto”
        if (!newEmailError.isError && !newPasswordError.isError) {
            // Activamos loading
            _formState.value = _formState.value.copy(isLoadingAuth = true)
            // Login remoto
            viewModelScope.launch {
                try {
                    val response = loginRepository.login(
                        username = current.emailField.value,
                        password = current.passwordField.value
                    )
                    // Guardar el token en EncryptedSharedPreferences
                    tokenManager.saveToken(response.jwt)
                    // Emite evento de navegación
                    _uiEvent.value = LoginUiEvent.NavigateHome

                } catch (e: Exception) {
                    // Manejar error de red o servidor
                    // Ejemplo: emitir un evento de error
                    _uiEvent.value = LoginUiEvent.ShowError("Error: ${e.message}")
                } finally {
                    // Quitar loading
                    _formState.value = _formState.value.copy(isLoadingAuth = false)
                }
            }
        }
    }
    fun consumeEvent() {
        _uiEvent.value = null
    }
}

sealed class LoginUiEvent {
    object NavigateHome : LoginUiEvent()
    data class ShowError(val message: String) : LoginUiEvent()
    // Podrías agregar otros eventos (MostrarSnackBar, etc.)
}