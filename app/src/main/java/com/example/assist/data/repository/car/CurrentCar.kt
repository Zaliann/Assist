package com.example.assist.data.repository.car

import android.util.Log
import com.example.assist.data.database.dao.CarDao
import com.example.assist.data.database.entity.toDomain
import com.example.assist.data.database.toDomain
import com.example.assist.data.store.Store
import com.example.assist.domain.car.Car
import com.example.assist.domain.car.SelectedCar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentCar @Inject constructor(
    private val dao: CarDao,
    private val selectedCarId: Store<Long>,
    private val scope: CoroutineScope
) : SelectedCar, StateFlow<Car?> by dao.resolveSelected(scope, selectedCarId)

@OptIn(ExperimentalCoroutinesApi::class)
private fun CarDao.resolveSelected(scope: CoroutineScope, store: Store<Long>) =
    store.data
        .filterNotNull()
        .onEach { Log.d("--tag", "selected car id = $it") }
        .flatMapLatest { observe(it) }
        .map { it?.toDomain() }
        .onEach { Log.d("--tag", "selected car = $it") }
        .stateIn(scope, SharingStarted.Lazily, null)