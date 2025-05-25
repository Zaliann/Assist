package com.example.assist.domain.expense.analytics

import com.example.assist.data.repository.car.CurrentCar
import com.example.assist.domain.execute
import com.example.assist.domain.expense.ExpenseRepository
import com.example.assist.domain.expense.ExpenseTarget
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseSum @Inject constructor(
    private val currentCar: CurrentCar,
    private val repositoryFactory: ExpenseRepository.Factory
) {
    private val repository by lazy { currentCar.filterNotNull().map(repositoryFactory) }

    suspend operator fun invoke(filter: AnalyticsFilter): Long = repository.execute {
        getIn(filter.period).filter { it.target matches filter.selection }.sumOf { it.price.toLong() }
    }

    private infix fun ExpenseTarget.matches(selection: AnalyticsSelection) = when (selection) {
        AnalyticsSelection.All -> true
        AnalyticsSelection.Maintaince -> this is ExpenseTarget.CarPart
        is AnalyticsSelection.Target -> this is ExpenseTarget.Custom && value == selection.value.value
    }
}