package com.example.assist.domain.maintaince

import android.util.Log
import com.example.assist.domain.car.SelectedCar
import com.example.assist.domain.execute
import com.example.assist.domain.expense.Expense
import com.example.assist.domain.expense.ExpenseRepository
import com.example.assist.domain.expense.ExpenseTarget
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class Maintaince @Inject constructor(
    selectedCar: SelectedCar,
    maintainceRepositoryFactory: MaintainceRepository.Factory,
    expenseRepositoryFactory: ExpenseRepository.Factory
) {
    private val repositories = selectedCar.filterNotNull().map {
        Repositories(maintainceRepositoryFactory(it), expenseRepositoryFactory(it), it.mileage)
    }

    suspend fun update(part: Part, price: Int) = repositories.execute {
        coroutineScope {
            launch { maintaince.replace(part) }
            launch { expense.add(Expense(0, ExpenseTarget.CarPart(part), price)) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observe() = repositories.transformLatest {
        val currentMileage = it.currentMileage
        Log.d("--tag", "currentMileage = $currentMileage")
        val mapped = it.maintaince.observe().map { partsMap ->
            Log.d("--tag", "partsMap = $partsMap")
            Part.entries.map { part ->
                val replacedAt = partsMap[part]
                val remaining = if (replacedAt != null) {
                    val diff = currentMileage - replacedAt
                    Log.d("--tag", "diff = $diff, resource = ${part.mileageResource}")
                    (part.mileageResource - diff).coerceAtLeast(0).also {
                        Log.d("--tag", "remaining = $it")
                    }
                } else {
                    -1
                }

                PartReplacement(part, remaining)
            }
        }
        emitAll(mapped)
    }

    private class Repositories(
        val maintaince: MaintainceRepository,
        val expense: ExpenseRepository,
        val currentMileage: Int
    )
}