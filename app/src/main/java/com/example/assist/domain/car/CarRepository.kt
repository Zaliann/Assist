package com.example.assist.domain.car

import kotlinx.coroutines.flow.Flow

interface CarRepository {
    fun observe(): Flow<List<Car>>

    suspend fun put(car: Car)

    suspend fun select(id: Long)

    suspend fun delete(id: Long)

    suspend fun updateMileage(id: Long, mileage: Int)
}