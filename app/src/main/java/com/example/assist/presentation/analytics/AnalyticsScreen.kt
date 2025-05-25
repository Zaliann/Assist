package com.example.assist.presentation.analytics

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
import com.example.assist.domain.expense.analytics.AnalyticsSelection
import com.example.assist.presentation.base.StateComponent

class AnalyticsScreen : StateComponent<AnalyticsScreen.Action, AnalyticsScreen.State>, Tab {
    override val options: TabOptions
        @Composable
        get() = run {
            val painter = rememberVectorPainter(Icons.Filled.Menu)
            remember {
                TabOptions(index = 4u, "Analytics", painter)
            }
        }

    @Immutable
    data class State(
        val from: InputState = InputState.Success(""),
        val to: InputState = InputState.Success(""),
        val selection: AnalyticsSelection = AnalyticsSelection.All,
        val sum: Long = 0L
    )

    sealed interface Action {
        @JvmInline
        value class From(val value: String) : Action

        @JvmInline
        value class To(val value: String) : Action

        @JvmInline
        value class SelectionChoose(val value: AnalyticsSelection) : Action

        data object Compute : Action
    }

    @Composable
    @NonRestartableComposable
    override fun Content(state: State, onAction: (Action) -> Unit) =
        AnalyticsScreenContent(state, onAction)

    @Composable
    override fun model() = getViewModel<AnalyticsModel>()
}

sealed interface InputState {
    val value: String

    @JvmInline
    value class Success(override val value: String) : InputState

    @JvmInline
    value class Error(override val value: String) : InputState
}