package com.example.assist.presentation.expenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach

val expenseTypesDefault = listOf(
    "Топливо",
    "Мойка",
    "Парковка",
    "Штрафы",
    "Налоги",
    "Страхование"
)

val numberKeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

@Immutable
data class ExpenseInputState(
    val sum: String = "",
    val type: String = "",
    val comment: String = ""
)

@Composable
fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onConfirm: (ExpenseInputState) -> Unit,
    visible: Boolean = false,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(200)) + scaleIn(tween(200)),
        exit = fadeOut(tween(200)) + scaleOut(tween(200))
    ) {
        var state by remember { mutableStateOf(ExpenseInputState()) }
        var isCorrectSum: Boolean = true

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Добавление расхода",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = state.sum,
                        onValueChange = {
                            state = state.copy(sum = it.filter { c -> c.isDigit() })
                        },
                        label = { Text("Сумма") },
                        trailingIcon = {
                            if (state.sum.isNotEmpty()) {
                                if (state.sum.toLong() !in 1 .. 1_000_000_000) {
                                    isCorrectSum = false
                                    Text(
                                        text = "Введите корректную сумму",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(start = 16.dp, end = 4.dp)
                                    )
                                } else {
                                    isCorrectSum = true
                                    IconButton(onClick = { state = state.copy(sum = "") }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Очистить")
                                    }
                                }
                            }
                        },
                        keyboardOptions = numberKeyboardOptions,
                        singleLine = true
                    )

                    Box {
                        var dropdownExpanded by remember { mutableStateOf(false) }

                        OutlinedTextField(
                            value = state.type,
                            onValueChange = {},
                            label = { Text("Тип расхода") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { dropdownExpanded = true }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            expenseTypesDefault.fastForEach { item ->
                                key(item) {
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            state = state.copy(type = item)
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = state.comment,
                        onValueChange = { state = state.copy(comment = it) },
                        label = { Text("Комментарий") },
                        trailingIcon = {
                            if (state.comment.isNotEmpty()) {
                                IconButton(onClick = { state = state.copy(comment = "") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Очистить")
                                }
                            }
                        },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { if (isCorrectSum) onConfirm(state) }) {
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
fun ExpensesScreenWithDialogPreview() {
    MaterialTheme {
        var visible by remember { mutableStateOf(true) }
        AddExpenseDialog(
            onDismiss = { visible = false },
            onConfirm = { visible = false },
            visible = visible
        )
    }
}