package com.example.assist.presentation.analytics

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.assist.domain.expense.analytics.AnalyticsFilter
import com.example.assist.domain.expense.analytics.ExpenseSum
import com.example.assist.presentation.base.StateHolder
import com.example.assist.presentation.base.StateModel
import com.example.assist.presentation.base.io
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class AnalyticsModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val expenseSum: ExpenseSum
) : ViewModel(),
    StateModel<AnalyticsScreen.Action, AnalyticsScreen.State>,
    StateHolder<AnalyticsScreen.State> by StateHolder(AnalyticsScreen.State()) {

    override fun onAction(action: AnalyticsScreen.Action) {
        when (action) {
            is AnalyticsScreen.Action.From -> update {
                it.copy(from = InputState.Success(action.value))
            }
            is AnalyticsScreen.Action.To -> update {
                it.copy(to = InputState.Success(action.value))
            }
            is AnalyticsScreen.Action.SelectionChoose -> update {
                it.copy(selection = action.value)
            }

            AnalyticsScreen.Action.Compute -> compute()
        }
    }

    private fun compute() {
        val (from, to) = state.value.run { from.value to to.value }

        val parsedFrom = parseDate(from)
        val parsedTo = parseDate(to)

        when {
            parsedFrom.isFailure && parsedTo.isFailure -> update {
                it.copy(
                    from = InputState.Error(from),
                    to = InputState.Error(to)
                )
            }
            parsedFrom.isFailure -> update {
                it.copy(from = InputState.Error(from))
            }
            parsedTo.isFailure -> update {
                it.copy(to = InputState.Error(to))
            }
        }

        if (parsedFrom.isFailure || parsedTo.isFailure) {
            Toast.makeText(context,"Введите дату в формате \"дд.мм.гггг\"", Toast.LENGTH_SHORT).show()
            return
        }

        val range = parsedFrom.getOrThrow()..parsedTo.getOrThrow()
        val filter = AnalyticsFilter(range, state.value.selection)

        screenModelScope.io {
            val sum = expenseSum(filter)

            update { it.copy(sum = sum) }
        }
    }

    private fun parseDate(date: String): Result<Instant> = kotlin.runCatching {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        LocalDate
            .parse(date, formatter)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
    }
}