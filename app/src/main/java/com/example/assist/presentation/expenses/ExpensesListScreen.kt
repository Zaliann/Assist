package com.example.assist.presentation.expenses

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.assist.LocalRootNavigator
import com.example.assist.presentation.cars.CarsListScreen
import com.example.assist.presentation.models.ExpenseItem

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreenContent(state: ExpenseScreen.State, onAction: (ExpenseScreen.Action) -> Unit) {
    val navigator = LocalRootNavigator.current
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface).padding(bottom = 80.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()

        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Расходы",
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                state = state.listState
            ) {
                state.expenses.forEach { (date, expenses) ->
                    stickyHeader(key = date, contentType = "header") {
                        DateHeader(date = date)
                    }

                    items(expenses, key = { it.id }, contentType = { "expense" }) { item ->
                        ExpenseItemCard(
                            modifier = Modifier.animateItem(),
                            expense = item,
                            onClick = { onAction(ExpenseScreen.Action.Edit(item)) }
                        )
                    }
                }
            }
        }

        var dialogVisible by remember { mutableStateOf(false) }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 48.dp, end = 24.dp)
                .zIndex(1f),
            onClick = { dialogVisible = true }
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
        }

        AddExpenseDialog(
            onDismiss = { dialogVisible = false },
            onConfirm = {
                onAction(ExpenseScreen.Action.Create(it))
                dialogVisible = false
            },
            visible = dialogVisible
        )

        EditExpenseDialog(
            onDismiss = { onAction(ExpenseScreen.Action.Edit(null)) },
            onConfirm = { onAction(ExpenseScreen.Action.FinishEdit(it)) },
            delete = { onAction(ExpenseScreen.Action.DeleteEditing) },
            editableItem = state.editableExpense
        )
    }
}

@Composable
fun ExpenseItemCard(
    modifier: Modifier,
    expense: ExpenseItem,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Сумма: ${expense.price}",
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = expense.target,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = expense.date,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(16.dp))

                val shortenedComment = expense.comment
                    .split(" ")
                    .take(5)
                    .joinToString(" ")
                    .let { if (expense.comment.split(" ").size > 5) "$it..." else it }

                Text(
                    text = shortenedComment,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DateHeader(date: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.4f))
        Text(
            text = date,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}



@Preview
@Composable
fun ExpensesScreenContentPreview() {
    ExpensesScreenContent(
        state = ExpenseScreen.State(
            expenses = mapOf("2023-10-01" to listOf(ExpenseItem(1L, "Покупка", 1000, "25-07-2003" , "Тестовый комментарий"))),
            listState = rememberLazyListState(),
            editableExpense = null
        ),
        onAction = {}
    )
}