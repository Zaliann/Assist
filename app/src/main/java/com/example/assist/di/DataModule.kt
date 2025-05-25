package com.example.assist.di

import android.content.Context
import androidx.room.Room
import com.example.assist.data.database.AssistDatabase
import com.example.assist.data.repository.car.CarRepositoryImpl
import com.example.assist.data.repository.car.CurrentCar
import com.example.assist.data.repository.expenses.ExpenseRepositoryImpl
import com.example.assist.data.repository.maintance.MaintainedRepositoryImpl
import com.example.assist.data.store.preferencesStore
import com.example.assist.domain.car.CarRepository
import com.example.assist.domain.car.SelectedCar
import com.example.assist.domain.expense.ExpenseRepository
import com.example.assist.domain.maintaince.MaintainceRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun selectedCar(impl: CurrentCar): SelectedCar

    @Binds
    fun carRepository(impl: CarRepositoryImpl): CarRepository

    @Binds
    fun expenseRepository(factory: ExpenseRepositoryImpl.Factory): ExpenseRepository.Factory

    @Binds
    fun maintainceRepository(factory: MaintainedRepositoryImpl.Factory): MaintainceRepository.Factory

    companion object {
        @Provides
        @Singleton
        fun scope() = CoroutineScope(Dispatchers.IO + SupervisorJob())

        @Provides
        @Singleton
        fun carIdStore(@ApplicationContext context: Context) = preferencesStore(
            name = "selected_car_id",
            context = context,
            serialize = Long::toString,
            deserialize = { it.toLong() }
        )

        @Provides
        @Singleton
        fun database(@ApplicationContext context: Context) = Room
            .databaseBuilder(
                context,
                AssistDatabase::class.java,
                AssistDatabase::class.simpleName.orEmpty()
            )
            .fallbackToDestructiveMigration(true)
            .build()

        @Provides
        @Singleton
        fun carDao(database: AssistDatabase) = database.carDao

        @Provides
        @Singleton
        fun expenseDao(database: AssistDatabase) = database.expenseDao

        @Provides
        @Singleton
        fun maintainceDao(database: AssistDatabase) = database.maintainceDao
    }
}