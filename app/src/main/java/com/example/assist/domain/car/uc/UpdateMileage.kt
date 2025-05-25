package com.example.assist.domain.car.uc

import com.example.assist.domain.car.CarRepository
import com.example.assist.domain.car.SelectedCar
import javax.inject.Inject

class UpdateMileage @Inject constructor(
    private val selectedCar: SelectedCar,
    private val repository: CarRepository
) {
    suspend operator fun invoke(mileage: Int) {
        val car = selectedCar.value ?: return
        repository.updateMileage(car.id, mileage)
    }
}