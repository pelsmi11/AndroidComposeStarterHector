package com.example.androidcomposestarterhector.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.androidcomposestarterhector.core.ui.components.model.ErrorStatus
import com.example.androidcomposestarterhector.core.ui.components.model.FieldInput
import com.example.androidcomposestarterhector.core.ui.components.model.IconResource

@Composable
fun OutlineFieldWithState(
    modifier: Modifier = Modifier,
    label: String,
    fieldInput: FieldInput,
    errorStatus: ErrorStatus,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isPasswordField: Boolean = false,
    leadingIconResource: IconResource? = null,
    // Podrías agregar trailingIconResource si quieres
    onValueChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = fieldInput.value,
        onValueChange = {
            // notifica a quien lo use
            onValueChange(it)
        },
        label = {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        leadingIcon = leadingIconResource?.let { iconRes ->
            {
                Icon(
                    painter = iconRes.asPainterResource(),
                    contentDescription = null
                )
            }
        },
        isError = fieldInput.hasInteracted && errorStatus.isError,
        supportingText = {
            if (fieldInput.hasInteracted && errorStatus.isError) {
                errorStatus.errorMsg?.let { msg ->
                    Text(
                        text = msg,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        trailingIcon = if (isPasswordField) {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    // Usa tus propios drawables o ImageVector
                    // Aquí simplificado con un Info icon:
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        } else if (fieldInput.hasInteracted && errorStatus.isError) {
            {
                Icon(imageVector = Icons.Filled.Info, contentDescription = "Error")
            }
        } else {
            null
        },
        visualTransformation = if (isPasswordField && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        }
    )
}