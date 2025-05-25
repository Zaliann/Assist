package com.example.assist.domain.expense

import com.example.assist.domain.car.Car
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface ExpenseRepository {
    interface Factory : (Car) -> ExpenseRepository

    fun observe(): Flow<List<Expense>>

    suspend fun add(expense: Expense)

    suspend fun edit(id: Long, price: Int, comment: String)

    suspend fun delete(id: Long)

    suspend fun getIn(range: ClosedRange<Instant>): List<Expense>
}