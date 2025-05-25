package com.example.assist.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.assist.data.database.entity.MaintainanceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface MaintainceDao {
    @Query("SELECT * FROM maintainance WHERE car_id = :id")
    fun observe(id: Long): Flow<MaintainanceEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MaintainanceEntity)
}

suspend fun MaintainceDao.resolve(id: Long) = observe(id).first()