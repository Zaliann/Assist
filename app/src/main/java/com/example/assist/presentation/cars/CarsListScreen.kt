package com.example.assist.presentation.cars

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.NonRestartableComposable
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.Navigator
import com.example.assist.domain.car.Car
import com.example.assist.presentation.base.StateComponent

class CarsListScreen : StateComponent<CarsListScreen.Action, CarsListScreen.State> {
    @Immutable
    data class State(
        val cars: List<Car> = emptyList(),
        val listState: LazyListState = LazyListState(),
        val selected: Car? = null
    )

    sealed interface Action {
        data class Select(val id: Long, val navigator: Navigator) : Action

        @JvmInline
        value class Delete(val id: Long) : Action

        @JvmInline
        value class Add(val inputState: CarInputState) : Action
    }

    @Composable
    @NonRestartableComposable
    override fun Content(state: State, onAction: (Action) -> Unit) =
        CarsListScreenContent(state, onAction)

    @Composable
    override fun model() = getViewModel<CarsListModel>()
}