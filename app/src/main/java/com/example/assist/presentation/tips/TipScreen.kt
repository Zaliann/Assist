package com.example.assist.presentation.tips

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions


class TipScreen : Tab {
    override val options: TabOptions
        @Composable
        get() = run {
        val painter = rememberVectorPainter(Icons.Filled.Info)
        remember {
            TabOptions(index = 2u, "Tip", painter)
        }
    }

    @Composable
    override fun Content(){
        TipScreenContent()
    }

}