package com.example.assist.presentation.models

import androidx.compose.runtime.Immutable
import com.example.assist.domain.expense.Expense
import com.example.assist.domain.expense.ExpenseTarget
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val timeFormatter by lazy {
    DateTimeFormatter
        .ofPattern("dd.MM.yy")
        .withZone(ZoneId.systemDefault())
}

@Immutable
data class ExpenseItem(
    val id: Long,
    val target: String,
    val price: Int,
    val date: String,
    val comment: String
)

fun Expense.toUi() = ExpenseItem(
    id = id,
    target = when (target) {
        is ExpenseTarget.CarPart -> target.part.partName
        is ExpenseTarget.Custom -> target.value
    },
    price = price,
    date = timeFormatter.format(date),
    comment = comment
)