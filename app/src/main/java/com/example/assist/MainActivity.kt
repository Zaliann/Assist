@file:OptIn(ExperimentalVoyagerApi::class)

package com.example.assist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.MutableScatterMap
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.staticCompositionLocalOf
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigatorSaver
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.NavigatorSaver
import cafe.adriel.voyager.transitions.FadeTransition
import com.example.assist.presentation.splash.SplashScreen
import com.example.assist.ui.theme.AssistTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

val LocalRootNavigator = staticCompositionLocalOf<Navigator> {
    error("LocalRootNavigator not provided")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssistTheme {
                CompositionLocalProvider(
                    LocalNavigatorSaver provides RamNavigatorSaver.saver
                ) {
                    Navigator(SplashScreen()) { navigator ->
                        CompositionLocalProvider(
                            LocalRootNavigator provides navigator
                        ) {
                            FadeTransition(navigator) {
                                it.Content()
                            }
                        }
                    }
                }
            }
        }
    }

    private object RamNavigatorSaver {
        private val saverMap = MutableScatterMap<String, List<Screen>>()

        @OptIn(InternalVoyagerApi::class)
        val saver = NavigatorSaver { initial, key, stateHolder, disposeBehavior, parent ->
            Saver(
                save = {
                    val uuid = UUID.randomUUID().toString()
                    saverMap[uuid] = it.items
                    uuid
                },
                restore = { uuid ->
                    Navigator(
                        saverMap.remove(uuid) ?: initial,
                        key,
                        stateHolder,
                        disposeBehavior,
                        parent
                    )
                }
            )
        }
    }
}