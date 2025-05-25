package com.example.assist.presentation.expenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.assist.presentation.models.ExpenseItem

@Composable
fun EditExpenseDialog(
    onDismiss: () -> Unit,
    onConfirm: (ExpenseInputState) -> Unit,
    delete: () -> Unit,
    editableItem: ExpenseItem?
) {
    AnimatedVisibility(
        visible = editableItem != null,
        enter = fadeIn(tween(200)) + scaleIn(tween(200)),
        exit = fadeOut(tween(200)) + scaleOut(tween(200))
    ) {
        var state by remember(editableItem) {
            mutableStateOf(
                ExpenseInputState(
                    sum = editableItem?.price?.toString().orEmpty(),
                    comment = editableItem?.comment.orEmpty()
                )
            )
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Редактирование расхода",
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
                                IconButton(onClick = { state = state.copy(sum = "") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Очистить")
                                }
                            }
                        },
                        keyboardOptions = numberKeyboardOptions,
                        singleLine = true
                    )

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
                TextButton(onClick = { onConfirm(state) }) {
                    Text("Ок")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = delete,
                    colors = ButtonDefaults.textButtonColors().copy(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Удалить")
                }
            }
        )
    }
}