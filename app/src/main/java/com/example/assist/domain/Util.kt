@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.assist.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest

suspend fun <T, R> Flow<T>.execute(block: suspend T.() -> R) = mapLatest { it.block() }.first()