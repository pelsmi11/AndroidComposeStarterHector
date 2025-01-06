package com.example.androidcomposestarterhector.feature.register.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidcomposestarterhector.core.ui.components.OutlineFieldWithState
import com.example.androidcomposestarterhector.core.ui.components.SelectFieldWithState
import com.example.androidcomposestarterhector.core.ui.components.model.ErrorStatus
import com.example.androidcomposestarterhector.core.ui.components.model.FieldInput

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {

    val formState by viewModel.formState.collectAsState()
    val uiEvent by viewModel.uiEvent.collectAsState()

    // Obtenemos el FocusManager local
    val focusManager = LocalFocusManager.current

    // Para manejar el snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Observamos si hay un evento de navegación
    LaunchedEffect(uiEvent) {
        when (val event = uiEvent) {
            is RegisterUiEvent.NavigateHome -> {
                onRegisterSuccess()
                // Importante: resetear el uiEvent para no navegar otra vez si se recompone
                viewModel.consumeEvent()
            }

            null -> Unit
            is RegisterUiEvent.ShowError -> {
                val message = event.message
                snackbarHostState.showSnackbar(message)
                viewModel.consumeEvent()
            }
        }
    }

    var selectedRole by remember { mutableStateOf<String?>(null) } // Aquí se guarda el value (1 o 2)

    // El Scaffold con SnackbarHost
    Scaffold(
        // O en material3: use `SnackbarHostState` manual + "Scaffold(...) { innerPadding -> ... }"
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Campo: Nombre
            OutlineFieldWithState(
                modifier = Modifier.fillMaxWidth(),
                label = "Nombre",
                fieldInput = formState.nameField,
                errorStatus = formState.nameError,
                keyboardOptions = KeyboardOptions.Default,
                onValueChange = {
                    viewModel.onNameChange(it)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo: Apellido
            OutlineFieldWithState(
                modifier = Modifier.fillMaxWidth(),
                label = "Apellido",
                fieldInput = formState.lastnameField,
                errorStatus = formState.lastnameError,
                keyboardOptions = KeyboardOptions.Default,
                onValueChange = {
                    viewModel.onLastNameChange(it)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Select para Rol
            SelectFieldWithState(
                modifier = Modifier.fillMaxWidth(),
                label = "Rol",
                options = listOf(
                    "1" to "Administrador",
                    "2" to "Coordinador"
                ),
                selectedValue = formState.rolField.value,
                errorStatus = formState.rolError,
                onValueChange = {
                    viewModel.onRolChange(it)
                } // Actualiza el valor seleccionado
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo: Correo
            OutlineFieldWithState(
                modifier = Modifier.fillMaxWidth(),
                label = "Correo",
                fieldInput = formState.emailField,
                errorStatus = formState.emailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = {
                    viewModel.onEmailChange(it)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo: Contraseña
            OutlineFieldWithState(
                modifier = Modifier.fillMaxWidth(),
                label = "Contraseña",
                fieldInput = formState.passwordField,
                errorStatus = formState.passwordError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isPasswordField = true,
                onValueChange = {
                    viewModel.onPasswordChange(it)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo: Confirmar Contraseña
            OutlineFieldWithState(
                modifier = Modifier.fillMaxWidth(),
                label = "Confirmar Contraseña",
                fieldInput = formState.confirmPasswordField,
                errorStatus = formState.confirmPasswordError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isPasswordField = true,
                onValueChange = {
                    viewModel.onConfirmPasswordChange(it)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Button(
                    modifier = Modifier.weight(1f),
                    enabled = !formState.isLoadingRegister,
                    onClick = {
                        // Aquí podrías llamar a tu ViewModel para crear el usuario
                        // Si OK -> onRegisterSuccess()
                        focusManager.clearFocus(force = true)
                        viewModel.handleRegister()
                    }) {
                    if (formState.isLoadingRegister) {
                        // Mostrar un indicador
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Validando...")
                    } else {
                        Text("Registrarme")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onBackToLogin()
                    }) {
                    Text("Volver a Login")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onRegisterSuccess = {},
        onBackToLogin = {}
    )
}