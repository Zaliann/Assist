package com.example.assist.presentation.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.assist.domain.expense.Expense
import com.example.assist.domain.expense.ExpenseTarget
import com.example.assist.domain.expense.Expenses
import com.example.assist.presentation.base.StateHolder
import com.example.assist.presentation.base.StateModel
import com.example.assist.presentation.base.io
import com.example.assist.presentation.models.ExpenseItem
import com.example.assist.presentation.models.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ExpenseModel @Inject constructor(
    private val expenses: Expenses
) : ViewModel(),
    StateModel<ExpenseScreen.Action, ExpenseScreen.State>,
    StateHolder<ExpenseScreen.State> by StateHolder(ExpenseScreen.State()) {

    init {
        observeList()
    }

    override fun onAction(action: ExpenseScreen.Action) {
        when (action) {
            is ExpenseScreen.Action.Create -> add(action.inputState)
            is ExpenseScreen.Action.Edit -> update { it.copy(editableExpense = action.item) }
            is ExpenseScreen.Action.FinishEdit -> finishEdit(action.inputState)
            ExpenseScreen.Action.DeleteEditing -> deleteEditing()
        }
    }

    private fun deleteEditing() {
        val editing = state.value.editableExpense ?: return
        update { it.copy(editableExpense = null) }
        screenModelScope.io {
            delay(300)
            expenses.delete(editing.id)
        }
    }

    private fun finishEdit(inputState: ExpenseInputState) {
        val editing = state.value.editableExpense ?: return
        update { it.copy(editableExpense = null) }

        inputState
            .runCatching { sum.toInt() to comment }
            .onSuccess { (price, comment) ->
                screenModelScope.io {
                    delay(300)
                    expenses.edit(editing.id, price, comment)
                }
            }
    }

    private fun observeList() {
        expenses
            .observe()
            .onEach { expenses ->
                val items = expenses.map(Expense::toUi).groupBy(ExpenseItem::date)
                update { it.copy(expenses = items) }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun add(inputState: ExpenseInputState) {
        inputState
            .runCatching {
                Expense(
                    id = 0,
                    target = ExpenseTarget.Custom(inputState.type),
                    price = inputState.sum.toInt(),
                    date = Instant.now(),
                    comment = inputState.comment
                )
            }
            .onSuccess { expense ->
                viewModelScope.io {
                    delay(300)
                    expenses.add(expense)
                }
            }
    }

}