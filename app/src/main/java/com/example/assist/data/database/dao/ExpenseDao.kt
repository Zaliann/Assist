package com.example.assist.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.assist.data.database.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM EXPENSES WHERE car_id = :carId ORDER BY date DESC")
    fun observe(carId: Long) : Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE expenses SET price = :price, comment = :comment WHERE id = :id")
    suspend fun edit(id: Long, price: Int, comment: String)

    @Query("SELECT * FROM EXPENSES WHERE car_id = :carId AND date >= :start AND date <= :end")
    suspend fun fromRange(carId: Long, start: Long, end: Long): List<ExpenseEntity>
}