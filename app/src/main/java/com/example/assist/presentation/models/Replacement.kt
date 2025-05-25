package com.example.assist.presentation.models

import androidx.compose.runtime.Immutable
import com.example.assist.domain.maintaince.Part
import com.example.assist.domain.maintaince.PartReplacement

@Immutable
data class Replacement(
    val part: Part,
    val remaining: Int
)

fun PartReplacement.toUi() = Replacement(part, remaining)