package com.example.assist.data.converter

import com.example.assist.domain.maintaince.Part

private const val separator = " "

fun Map<Part, Int>.serialize() = entries.joinToString(separator) { (part, mileage) ->
    "${part.ordinal},${mileage}"
}

fun String.deserialize() = split(separator).associate { replacementString ->
    val (partOrdinal, mileageReplacement) = replacementString
        .split(",")
        .map(String::toInt)

    Part.entries[partOrdinal] to mileageReplacement
}