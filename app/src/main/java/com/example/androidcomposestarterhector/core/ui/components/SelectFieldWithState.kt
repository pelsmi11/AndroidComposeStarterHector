package com.example.androidcomposestarterhector.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.androidcomposestarterhector.core.ui.components.model.ErrorStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFieldWithState(
    modifier: Modifier = Modifier,
    label: String,
    options: List<Pair<String, String>>, // Lista de opciones (value, label)
    selectedValue: String?, // El valor seleccionado
    errorStatus: ErrorStatus,
    onValueChange: (String) -> Unit // Retorna el valor seleccionado
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it } // Maneja la apertura/cierre del menú
    ) {
        // TextField dentro del componente
        OutlinedTextField(
            modifier = modifier
                .menuAnchor() // Asegura que el menú esté alineado con el TextField
                .fillMaxWidth(),
            value = options.firstOrNull { it.first == selectedValue }?.second ?: "", // Mostrar label del valor seleccionado
            onValueChange = {}, // No editable manualmente
            label = { Text(label, style = MaterialTheme.typography.bodyMedium) },
            readOnly = true, // Solo lectura
            isError = errorStatus.isError,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            singleLine = true
        )

        // Dropdown para las opciones
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.second, // El label de la opción
                            textAlign = TextAlign.Start
                        )
                    },
                    onClick = {
                        expanded = false
                        onValueChange(option.first) // Devuelve el value (1 para admin, 2 para coord)
                    }
                )
            }
        }
    }
}