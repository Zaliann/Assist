package com.example.assist.data.repository.expenses

import com.example.assist.data.database.ExpenseEntity
import com.example.assist.data.database.dao.ExpenseDao
import com.example.assist.data.database.toDb
import com.example.assist.data.database.toDomain
import com.example.assist.domain.car.Car
import com.example.assist.domain.expense.Expense
import com.example.assist.domain.expense.ExpenseRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map
import java.time.Instant

class ExpenseRepositoryImpl @AssistedInject constructor(
    @Assisted private val car: Car,
    private val dao: ExpenseDao,
) : ExpenseRepository {
    @AssistedFactory
    interface Factory : ExpenseRepository.Factory {
        override fun invoke(p1: Car): ExpenseRepositoryImpl
    }

    override fun observe() = dao.observe(car.id).map { list -> list.map(ExpenseEntity::toDomain) }

    override suspend fun add(expense: Expense) {
        val entity = expense.toDb(car.id)
        dao.insert(entity)
    }

    override suspend fun delete(id: Long) {
        dao.delete(id)
    }

    override suspend fun getIn(range: ClosedRange<Instant>): List<Expense> {
        val start = range.start.toEpochMilli()
        val end = range.endInclusive.toEpochMilli()
        return dao.fromRange(car.id, start, end).map(ExpenseEntity::toDomain)
    }

    override suspend fun edit(id: Long, price: Int, comment: String) {
        dao.edit(id, price, comment)
    }
}