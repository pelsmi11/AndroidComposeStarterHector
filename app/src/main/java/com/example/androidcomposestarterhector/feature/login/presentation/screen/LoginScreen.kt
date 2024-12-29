package com.example.androidcomposestarterhector.feature.login.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    // Observamos si hay un evento de navegación
    LaunchedEffect(uiEvent) {
        when (uiEvent) {
            is LoginUiEvent.NavigateHome -> {
                onNavigateToHome()
                // Importante: resetear el uiEvent para no navegar otra vez si se recompone
                viewModel.consumeEvent()
            }
            null -> Unit
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // E-MAIL
        OutlineFieldWithState(
            modifier = Modifier.fillMaxWidth(),
            label = "Correo",
            fieldInput = formState.emailField,
            errorStatus = formState.emailError,
            keyboardOptions = KeyboardOptions.Default,
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
                    // SOLO llamamos viewModel.login()
                    viewModel.login()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Iniciar Sesión")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { onNavigateToRegister() }, modifier = Modifier.weight(1f)) {
                Text("Crear Cuenta")
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