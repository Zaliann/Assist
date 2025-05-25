package com.example.assist.presentation.expenses

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.assist.presentation.base.StateComponent
import com.example.assist.presentation.models.ExpenseItem

class ExpenseScreen : StateComponent<ExpenseScreen.Action, ExpenseScreen.State>, Tab {
    override val options: TabOptions
        @Composable
        get() = run {
            val painter = rememberVectorPainter(Icons.Filled.Menu)
            remember {
                TabOptions(index = 0u, "Home", painter)
            }
        }

    @Immutable
    data class State(
        val expenses: Map<String, List<ExpenseItem>> = emptyMap(),
        val listState: LazyListState = LazyListState(),
        val editableExpense: ExpenseItem? = null,
    )

    sealed interface Action {
        @JvmInline
        value class Create(val inputState: ExpenseInputState) : Action

        @JvmInline
        value class Edit(val item: ExpenseItem?) : Action

        @JvmInline
        value class FinishEdit(val inputState: ExpenseInputState) : Action

        data object DeleteEditing : Action
    }

    @Composable
    @NonRestartableComposable
    override fun Content(state: State, onAction: (Action) -> Unit) =
        ExpensesScreenContent(state, onAction)

    @Composable
    override fun model() = getViewModel<ExpenseModel>()
}