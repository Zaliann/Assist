package com.example.assist.domain.expense

import com.example.assist.domain.maintaince.Part
import java.time.Instant

class Expense(
    val id: Long,
    val target: ExpenseTarget,
    val price: Int,
    val date: Instant = Instant.now(),
    val comment: String = ""
)

sealed interface ExpenseTarget {
    //new
//    @JvmInline
//    value class CarPart(val part: String) : ExpenseTarget

    @JvmInline
    value class CarPart(val part: Part) : ExpenseTarget

    @JvmInline
    value class Custom(val value: String) : ExpenseTarget
}