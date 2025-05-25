package com.example.assist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.assist.data.converter.Converters
import com.example.assist.data.database.dao.CarDao
import com.example.assist.data.database.dao.ExpenseDao
import com.example.assist.data.database.dao.MaintainceDao
import com.example.assist.data.database.entity.MaintainanceEntity

@Database(
    exportSchema = false,
    version = 1,
    entities = [
        CarEntity::class,
        ExpenseEntity::class,
        MaintainanceEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AssistDatabase : RoomDatabase() {
    abstract val carDao: CarDao
    abstract val expenseDao: ExpenseDao
    abstract val maintainceDao: MaintainceDao
}