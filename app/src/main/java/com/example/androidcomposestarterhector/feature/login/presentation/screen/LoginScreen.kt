package com.example.androidcomposestarterhector.feature.login.presentation.screen

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

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val formState by viewModel.formState.collectAsState()
    val uiEvent by viewModel.uiEvent.collectAsState()

    // 1) Obtenemos el FocusManager local
    val focusManager = LocalFocusManager.current

    // Para manejar el snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Observamos si hay un evento de navegación
    LaunchedEffect(uiEvent) {
        when (val event = uiEvent) {
            is LoginUiEvent.NavigateHome -> {
                onNavigateToHome()
                // Importante: resetear el uiEvent para no navegar otra vez si se recompone
                viewModel.consumeEvent()
            }

            null -> Unit
            is LoginUiEvent.ShowError -> {
                val message = event.message
                snackbarHostState.showSnackbar(message)
                viewModel.consumeEvent()
            }
        }
    }

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
        // Contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // E-MAIL
            OutlineFieldWithState(
                modifier = Modifier.fillMaxWidth(),
                label = "Correo",
                fieldInput = formState.emailField,
                errorStatus = formState.emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email // Esto habilita un teclado específico para correos electrónicos
                ),
                onValueChange = {
                    viewModel.onEmailChange(it)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // PASSWORD
            OutlineFieldWithState(
                modifier = Modifier.fillMaxWidth(),
                label = "Contraseña",
                fieldInput = formState.passwordField,
                errorStatus = formState.passwordError,
                keyboardOptions = KeyboardOptions.Default,
                isPasswordField = true,
                onValueChange = {
                    viewModel.onPasswordChange(it)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                // BOTÓN LOGIN
                Button(
                    onClick = {
                        // 2) Al hacer click, limpiamos el foco
                        focusManager.clearFocus(force = true)
                        viewModel.login()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !formState.isLoadingAuth // Deshabilitar si está cargando, si gustas
                ) {
                    if (formState.isLoadingAuth) {
                        // Mostrar un indicador
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Validando...")
                    } else {
                        Text(text = "Iniciar Sesión")
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = { onNavigateToRegister() }, modifier = Modifier.weight(1f)) {
                    Text("Crear Cuenta")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onNavigateToHome = {},
        onNavigateToRegister = {}
    )
}