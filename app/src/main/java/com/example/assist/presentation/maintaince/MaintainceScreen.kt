package com.example.assist.presentation.maintaince

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.example.assist.domain.maintaince.Part
import com.example.assist.presentation.base.StateComponent
import com.example.assist.presentation.models.Replacement

class MaintainceScreen : StateComponent<MaintainceScreen.Action, MaintainceScreen.State>, Tab {
    override val options: TabOptions
        @Composable
        get() = run {
            val painter = rememberVectorPainter(Icons.Filled.Build)
            remember {
                TabOptions(index = 1u, "Maintaince", painter)
            }
        }

    @Immutable
    data class State(
        val mileage: String = "",
        val replacements: List<Replacement> = emptyList(),
        val listState: LazyListState = LazyListState()
    )

    sealed interface Action {
        @JvmInline
        value class Mileage(val value: String) : Action

        data object UpdateMileage : Action

        data class UpdatePart(val part: Part, val price: String) : Action
    }

    @Composable
    @NonRestartableComposable
    override fun Content(state: State, onAction: (Action) -> Unit) =
        MaintenanceScreenContent(state, onAction)

    @Composable
    override fun model() = getViewModel<MaintainceModel>()
}