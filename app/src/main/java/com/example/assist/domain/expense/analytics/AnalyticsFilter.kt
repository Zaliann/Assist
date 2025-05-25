package com.example.assist.domain.expense.analytics

import com.example.assist.domain.expense.ExpenseTarget
import java.time.Instant

class AnalyticsFilter(
    val period: ClosedRange<Instant>,
    val selection: AnalyticsSelection
)

sealed interface AnalyticsSelection {
    data object All : AnalyticsSelection

    data object Maintaince : AnalyticsSelection

    data class Target(val value: ExpenseTarget.Custom) : AnalyticsSelection
}