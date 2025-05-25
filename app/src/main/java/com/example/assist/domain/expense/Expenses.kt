package com.example.assist.domain.expense

import com.example.assist.domain.car.SelectedCar
import com.example.assist.domain.execute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class Expenses @Inject constructor(
    selectedCar: SelectedCar,
    expenseRepositoryFactory: ExpenseRepository.Factory
) {
    private val repository = selectedCar.filterNotNull().map(expenseRepositoryFactory)

    fun observe(): Flow<List<Expense>> = repository.flatMapLatest { it.observe() }

    suspend fun add(expense: Expense) = repository.execute { add(expense) }

    suspend fun edit(id: Long, price: Int, comment: String) =
        repository.execute { edit(id, price, comment) }

    suspend fun delete(id: Long) = repository.execute { delete(id) }
}