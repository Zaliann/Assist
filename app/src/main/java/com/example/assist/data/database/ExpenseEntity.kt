package com.example.assist.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.assist.domain.expense.Expense
import com.example.assist.domain.expense.ExpenseTarget
import com.example.assist.domain.maintaince.Part
import dagger.multibindings.IntoMap
import java.time.Instant

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = CarEntity::class,
            parentColumns = ["id"],
            childColumns = ["car_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["car_id"])]
)

class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "car_id")
    val carId: Long,
    @ColumnInfo(name = "part")
    val part: Part?,
    @ColumnInfo(name = "custom_target")
    val customTarget: String?,
    @ColumnInfo(name = "price")
    val price: Int,
    @ColumnInfo(name = "date")
    val date: Instant,
    @ColumnInfo(name = "comment")
    val comment: String
)

fun ExpenseEntity.toDomain(): Expense {
    val target = if (part != null) {
        ExpenseTarget.CarPart(part)
    } else {
        ExpenseTarget.Custom(customTarget.orEmpty())
    }

    return Expense(
        id = id,
        target = target,
        price = price,
        date = date,
        comment = comment
    )
}

fun Expense.toDb(carId: Long): ExpenseEntity {
    var part: Part? = null
    var customTarget: String? = null

    when (target) {

        is ExpenseTarget.CarPart -> part = target.part
        is ExpenseTarget.Custom -> customTarget = target.value
    }

    return ExpenseEntity(
        id = id,
        carId = carId,
        part = part,
        customTarget = customTarget,
        price = price,
        date = date,
        comment = comment
    )
}