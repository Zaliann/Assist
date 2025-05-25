package com.example.assist.domain.maintaince

import com.example.assist.domain.car.Car
import kotlinx.coroutines.flow.Flow

interface MaintainceRepository {
    interface Factory : (Car) -> MaintainceRepository

    fun observe(): Flow<Map<Part, Int>>

    suspend fun replace(part: Part)
}