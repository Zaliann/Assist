package com.example.assist.data.repository.maintance

import android.util.Log
import com.example.assist.data.database.entity.MaintainanceEntity
import com.example.assist.data.database.dao.MaintainceDao
import com.example.assist.data.database.dao.resolve
import com.example.assist.data.database.entity.toDomain
import com.example.assist.domain.car.Car
import com.example.assist.domain.car.SelectedCar
import com.example.assist.domain.maintaince.MaintainceRepository
import com.example.assist.domain.maintaince.Part
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MaintainedRepositoryImpl @AssistedInject constructor(
    @Assisted private val car: Car,
    private val dao: MaintainceDao
) : MaintainceRepository {
    @AssistedFactory
    interface Factory : MaintainceRepository.Factory {
        override fun invoke(p1: Car): MaintainedRepositoryImpl
    }

    override fun observe() = dao.observe(car.id).map(MaintainanceEntity?::toDomain).onEach {
        Log.d("-tag", "all replacements = $it")
    }

    override suspend fun replace(part: Part) {
        val replacements = dao.resolve(car.id)?.replacements.orEmpty().toMutableMap()
        Log.e("--tag", "existing replacements = $replacements for $car")
        replacements[part] = car.mileage
        Log.e("--tag", "updated = $replacements")
        val maintaince = MaintainanceEntity(car.id, replacements)
        dao.insert(maintaince)
    }
}