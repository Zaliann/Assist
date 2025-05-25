package com.example.assist.presentation.analytics

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.example.assist.LocalRootNavigator
import com.example.assist.domain.expense.ExpenseTarget
import com.example.assist.domain.expense.analytics.AnalyticsSelection
import com.example.assist.presentation.cars.CarsListScreen
import com.example.assist.presentation.expenses.expenseTypesDefault
import com.example.assist.presentation.expenses.numberKeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreenContent(
    state: AnalyticsScreen.State,
    onAction: (AnalyticsScreen.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        val navigator = LocalRootNavigator.current

        TopAppBar(
            title = {
                Text(
                    text = "Аналитика",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                IconButton(onClick = { navigator.replace(CarsListScreen()) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
            }
        )

        Text(
            text = "Сумма расходов за выбранный период по выбранным категориям:",
            fontSize = 20.sp
        )


        Spacer(Modifier.height(12.dp))


        OutlinedTextField(
            value = state.from.value,
            onValueChange = { onAction(AnalyticsScreen.Action.From(it)) },
            label = { Text("Начальная дата") },
            keyboardOptions = numberKeyboardOptions,
            singleLine = true,
            isError = state.from is InputState.Error
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.to.value,
            onValueChange = { onAction(AnalyticsScreen.Action.To(it)) },
            label = { Text("Конечная дата") },
            keyboardOptions = numberKeyboardOptions,
            singleLine = true,
            isError = state.to is InputState.Error
        )

        Spacer(Modifier.height(12.dp))

        Box {
            var dropdownExpanded by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = when (state.selection) {
                    AnalyticsSelection.All -> "Все"
                    AnalyticsSelection.Maintaince -> "ТО"
                    is AnalyticsSelection.Target -> state.selection.value.value
                },
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
                DropdownMenuItem(
                    text = { Text("Все") },
                    onClick = {
                        onAction(
                            AnalyticsScreen.Action.SelectionChoose(
                                AnalyticsSelection.All
                            )
                        )
                        dropdownExpanded = false
                    }
                )

                DropdownMenuItem(
                    text = { Text("ТО") },
                    onClick = {
                        onAction(
                            AnalyticsScreen.Action.SelectionChoose(
                                AnalyticsSelection.Maintaince
                            )
                        )
                        dropdownExpanded = false
                    }
                )

                expenseTypesDefault.fastForEach { item ->
                    key(item) {
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                onAction(
                                    AnalyticsScreen.Action.SelectionChoose(
                                        AnalyticsSelection.Target(
                                            ExpenseTarget.Custom(item)
                                        )
                                    )
                                )
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        AnimatedContent(state.sum, label = "") { sum ->
            Text(text = "Итого: " + sum.toString(), fontSize = 24.sp,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ))
        }

        Spacer(Modifier.height(12.dp))

        TextButton(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = { onAction(AnalyticsScreen.Action.Compute) }) {
            Text(
                text = "Найти сумму расходов",
                fontSize = 24.sp,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }

}