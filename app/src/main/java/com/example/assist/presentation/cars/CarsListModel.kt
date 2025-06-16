package com.example.assist.presentation.cars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.assist.domain.car.Car
import com.example.assist.domain.car.CarRepository
import com.example.assist.domain.car.SelectedCar
import com.example.assist.presentation.base.StateHolder
import com.example.assist.presentation.base.StateModel
import com.example.assist.presentation.base.io
import com.example.assist.presentation.main.MainScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsListModel @Inject constructor(
    private val selectedCar: SelectedCar,
    private val repository: CarRepository
) : ViewModel(),
    StateModel<CarsListScreen.Action, CarsListScreen.State>,
    StateHolder<CarsListScreen.State> by StateHolder(CarsListScreen.State()) {

    init {
        observeCars()
        observeSelected()
    }

    override fun onAction(action: CarsListScreen.Action) {
        when (action) {
            is CarsListScreen.Action.Delete -> screenModelScope.io {
                repository.delete(action.id)
            }

            is CarsListScreen.Action.Select -> screenModelScope.io {
                repository.select(action.id)
                action.navigator.push(MainScreen())
            }
            is CarsListScreen.Action.Add -> add(action.inputState)
        }
    }

    private fun observeCars() {
        repository
            .observe()
            .onEach { cars -> update { it.copy(cars = cars) } }
            .flowOn(Dispatchers.IO)
            .launchIn(screenModelScope)
    }

    private fun observeSelected() {
        selectedCar
            .filterNotNull()
            .onEach { selected -> update { it.copy(selected = selected) } }
            .flowOn(Dispatchers.IO)
            .launchIn(screenModelScope)
    }

    private fun add(inputState: CarInputState) {
        inputState
            .runCatching {
                Car(
                    id = 0,
                    brand = inputState.brand,
                    model = inputState.model,
                    year = inputState.year.toInt(),
                    mileage = inputState.mileage.toInt()
                )
            }
            .onSuccess { car ->
                viewModelScope.io {
                    delay(300)
                    repository.put(car)
                }
            }
    }

}