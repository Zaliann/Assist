package com.example.assist.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.assist.domain.car.Car


@Entity(tableName = "cars")
class CarEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "brand")
    val brand: String,
    @ColumnInfo(name = "model")
    val model: String,
    @ColumnInfo(name = "year")
    val year: Int,
    @ColumnInfo(name = "mileage")
    val mileage: Int

)

fun CarEntity.toDomain(): Car {
    return Car(
        id = id,
        brand = brand,
        model = model,
        year = year,
        mileage = mileage
    )
}

fun Car.toDb(): CarEntity {
    return CarEntity(
        id = id,
        brand = brand,
        model = model,
        year = year,
        mileage = mileage
    )
}