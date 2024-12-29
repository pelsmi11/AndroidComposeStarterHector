package com.example.androidcomposestarterhector.feature.register.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidcomposestarterhector.core.ui.components.OutlineFieldWithState
import com.example.androidcomposestarterhector.core.ui.components.SelectFieldWithState
import com.example.androidcomposestarterhector.core.ui.components.model.ErrorStatus
import com.example.androidcomposestarterhector.core.ui.components.model.FieldInput

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {

    var selectedRole by remember { mutableStateOf<String?>(null) } // Aquí se guarda el value (1 o 2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Campo: Nombre
        OutlineFieldWithState(
            modifier = Modifier.fillMaxWidth(),
            label = "Nombre",
            fieldInput = FieldInput("", false),
            errorStatus = ErrorStatus(false, null),
            keyboardOptions = KeyboardOptions.Default,
            onValueChange = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo: Apellido
        OutlineFieldWithState(
            modifier = Modifier.fillMaxWidth(),
            label = "Apellido",
            fieldInput = FieldInput("", false),
            errorStatus = ErrorStatus(false, null),
            keyboardOptions = KeyboardOptions.Default,
            onValueChange = {}
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
            selectedValue = selectedRole,
            errorStatus = ErrorStatus.noError(),
            onValueChange = { selectedRole = it } // Actualiza el valor seleccionado
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo: Correo
        OutlineFieldWithState(
            modifier = Modifier.fillMaxWidth(),
            label = "Correo",
            fieldInput = FieldInput("", false),
            errorStatus = ErrorStatus(false, null),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo: Contraseña
        OutlineFieldWithState(
            modifier = Modifier.fillMaxWidth(),
            label = "Contraseña",
            fieldInput = FieldInput("", false),
            errorStatus = ErrorStatus(false, null),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isPasswordField = true,
            onValueChange = {}
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo: Confirmar Contraseña
        OutlineFieldWithState(
            modifier = Modifier.fillMaxWidth(),
            label = "Confirmar Contraseña",
            fieldInput = FieldInput("", false),
            errorStatus = ErrorStatus(false, null),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isPasswordField = true,
            onValueChange = {}
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    // Aquí podrías llamar a tu ViewModel para crear el usuario
                    // Si OK -> onRegisterSuccess()
                    onRegisterSuccess()
                }) {
                Text("Registrarme")
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

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(
        onRegisterSuccess = {},
        onBackToLogin = {}
    )
}