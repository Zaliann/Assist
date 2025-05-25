package com.example.assist.data.repository.car

import com.example.assist.data.database.dao.CarDao
import com.example.assist.data.database.CarEntity
import com.example.assist.data.database.entity.toDb
import com.example.assist.data.database.entity.toDomain
import com.example.assist.data.database.toDomain
import com.example.assist.data.database.toDb
import com.example.assist.data.store.Store
import com.example.assist.domain.car.Car
import com.example.assist.domain.car.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val dao: CarDao,
    private val selectedCarId: Store<Long>
) : CarRepository {
    override fun observe(): Flow<List<Car>> = dao.observeAll().map { list ->
        list.map(CarEntity::toDomain)
    }

    override suspend fun put(car: Car) {
        dao.insert(car.toDb())
    }

    override suspend fun select(id: Long) {
        selectedCarId.put(id)
    }

    override suspend fun delete(id: Long) {
        dao.delete(id)
    }

    override suspend fun updateMileage(id: Long, mileage: Int) {
        dao.updateMileage(id, mileage)
    }
}