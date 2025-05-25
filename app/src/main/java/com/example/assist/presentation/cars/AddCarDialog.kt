package com.example.assist.presentation.cars

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate

val numberKeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

@Immutable
data class CarInputState(
    val brand: String = "",
    val model: String = "",
    val year: String = "",
    val mileage: String = ""
)

@Composable
fun AddCarDialog(
    onDismiss: () -> Unit,
    onConfirm: (CarInputState) -> Unit,
    visible: Boolean = false,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)) + scaleIn(tween(200)),
        exit = fadeOut(tween(200)) + scaleOut(tween(200))
    ) {
        var state by remember { mutableStateOf(CarInputState()) }
        var isCorrectMileage: Boolean = true
        var isCorrectYear: Boolean = true

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Добавление ТС",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = state.brand,
                        onValueChange = { state = state.copy(brand = it) },
                        label = { Text("Марка ТС") },
                        trailingIcon = {
                            if (state.brand.isNotEmpty()) {
                                IconButton(onClick = { state = state.copy(brand = "") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Очистить")
                                }
                            }
                        },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.model,
                        onValueChange = { state = state.copy(model = it) },
                        label = { Text("Модель ТС") },
                        trailingIcon = {
                            if (state.model.isNotEmpty()) {
                                IconButton(onClick = { state = state.copy(model = "") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Очистить")
                                }
                            }
                        },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = state.year,
                        onValueChange = {
                            state = state.copy(year = it.filter { c -> c.isDigit() })
                        },
                        label = { Text("Год производства") },
                        trailingIcon = {
                            if (state.year.isNotEmpty()) {
                                if (state.year.toLong() !in 1..LocalDate.now().year) {
                                    isCorrectYear = false
                                    Text(
                                        text = "Введите корректный год",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(start = 16.dp, end = 4.dp)
                                    )
                                } else {
                                    isCorrectYear = true
                                    IconButton(onClick = { state = state.copy(year = "") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Очистить")
                                    }
                                }

                            }



                        },
                        keyboardOptions = numberKeyboardOptions,
                        singleLine = true
                    )
                    val yearPattern = Regex("^[0-9]{0,4}$")
                    OutlinedTextField(
                        value = state.mileage,
                        onValueChange = {
                            state = state.copy(mileage = it.filter { c -> c.isDigit() })
                        },
                        label = { Text("Пробег") },
                        trailingIcon = {
                            if (state.mileage.isNotEmpty()) {
                                if(state.mileage.toLong() !in 0 .. 1_000_000_000) {
                                    isCorrectMileage = false
                                    Text(
                                        text = "Введите корректный пробег",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(start = 16.dp, end = 4.dp)
                                    )
                                } else {
                                    isCorrectMileage = true
                                    IconButton(onClick = { state = state.copy(mileage = "") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Очистить")
                                    }
                                }

                            }

                        },
                        keyboardOptions = numberKeyboardOptions,
                        singleLine = true
                    )



                }
            },
            confirmButton = {
                TextButton(onClick = { if (isCorrectYear && isCorrectMileage) onConfirm(state) }) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Preview
@Composable
fun CarListScreenWithDialogPreview() {
    MaterialTheme {
        var visible by remember { mutableStateOf(true) }
        AddCarDialog(
            onDismiss = { visible = false },
            onConfirm = { visible = false },
            visible = visible
        )
    }
}