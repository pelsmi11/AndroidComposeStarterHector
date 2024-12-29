package com.example.androidcomposestarterhector.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CustomInputField(
    label: String,
    text: String,
    onValueChange: (String) -> Unit,
    error: String?,           // Mensaje de error o null
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
) {
//    // Podríamos personalizar la transformación si es un campo password
//    val transformation = if (isPassword) {
//        // Ojo: Podrías usar PasswordVisualTransformation(), pero el user
//        // de ProAndroidDev usa un custom. Aquí simplificado:
//        VisualTransformation.Password
//    } else {
//        VisualTransformation.None
//    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = error != null,
//            visualTransformation = transformation
        )
        if (error != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}