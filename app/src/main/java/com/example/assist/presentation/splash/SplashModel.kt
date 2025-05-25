package com.example.assist.presentation.splash

import androidx.lifecycle.ViewModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.assist.data.repository.car.CurrentCar
import com.example.assist.presentation.base.StateHolder
import com.example.assist.presentation.base.StateModel
import com.example.assist.presentation.base.io
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class SplashModel @Inject constructor(
    private val currentCar: CurrentCar,
) : ViewModel(), StateModel<Nothing, Boolean?>, StateHolder<Boolean?> by StateHolder(null) {

    init {
        resolveCurrent()
    }

    override fun onAction(action: Nothing) = Unit

    private fun resolveCurrent() {
        screenModelScope.io {
            val car = withTimeoutOrNull(1000) {
                currentCar.filterNotNull().first()
            }

            update { car != null }
        }
    }
}