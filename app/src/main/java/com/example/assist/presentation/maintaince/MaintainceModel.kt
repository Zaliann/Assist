package com.example.assist.presentation.maintaince

import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.assist.domain.car.SelectedCar
import com.example.assist.domain.car.uc.UpdateMileage
import com.example.assist.domain.maintaince.Maintaince
import com.example.assist.domain.maintaince.Part
import com.example.assist.domain.maintaince.PartReplacement
import com.example.assist.presentation.base.StateHolder
import com.example.assist.presentation.base.StateModel
import com.example.assist.presentation.base.io
import com.example.assist.presentation.models.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MaintainceModel @Inject constructor(
    private val updateMileage: UpdateMileage,
    private val maintaince: Maintaince,
    private val selectedCar: SelectedCar
) : ViewModel(),
    StateModel<MaintainceScreen.Action, MaintainceScreen.State>,
    StateHolder<MaintainceScreen.State> by StateHolder(MaintainceScreen.State()) {

    init {
        observeMileage()
        observeMaintaince()
    }

    override fun onAction(action: MaintainceScreen.Action) {
        when (action) {
            is MaintainceScreen.Action.Mileage -> update { it.copy(mileage = action.value) }
            MaintainceScreen.Action.UpdateMileage -> update()
            is MaintainceScreen.Action.UpdatePart -> updatePart(action.part, action.price)
        }
    }

    private fun update() {
        state.value.mileage
            .runCatching { toInt() }
            .onSuccess { mileage -> screenModelScope.io { updateMileage(mileage) } }
    }

    private fun updatePart(part: Part, price: String) {
        price
            .runCatching { toInt() }
            .onSuccess { screenModelScope.io { maintaince.update(part, it) } }
    }

    private fun observeMileage() {
        selectedCar
            .filterNotNull()
            .map { it.mileage }
            .distinctUntilChanged()
            .onEach { mileage -> update { it.copy(mileage = mileage.toString()) } }
            .launchIn(screenModelScope)
    }

    private fun observeMaintaince() {
        maintaince
            .observe()
            .onEach { replacements ->
                val ui = replacements.map(PartReplacement::toUi)
                update { it.copy(replacements = ui) }
            }
            .launchIn(screenModelScope)
    }
}