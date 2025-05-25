package com.example.assist.domain.car

data class Car(
    val id: Long,
    val brand: String,
    val model: String,
    val year: Int,
    val mileage: Int
)