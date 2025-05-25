package com.example.assist.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.assist.presentation.base.StateComponent
import com.example.assist.presentation.cars.CarsListScreen
import com.example.assist.presentation.main.MainScreen

class SplashScreen : StateComponent<Nothing, Boolean?> {
    @Composable
    override fun Content(state: Boolean?, onAction: (Nothing) -> Unit) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            val navigator = LocalNavigator.currentOrThrow

            LaunchedEffect(state) {
                when (state) {
                    true -> navigator.replace(MainScreen())
                    false -> navigator.replace(CarsListScreen())
                    null -> Unit
                }
            }
        }
    }

    @Composable
    override fun model() = getViewModel<SplashModel>()
}