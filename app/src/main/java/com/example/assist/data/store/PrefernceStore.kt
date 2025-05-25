package com.example.assist.data.store

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T> preferencesStore(
    context: Context,
    name: String,
    serialize: (T) -> String,
    deserialize: (String) -> T
): Store<T> = object : Store<T> {
    private val key = stringPreferencesKey("${name}_key")
    private val Context.dataStore by preferencesDataStore(name)

    override val data: Flow<T?> = context.dataStore.data.map { value ->
        value[key]?.let(deserialize)
    }

    override suspend fun clear() {
        context.dataStore.edit { value -> value.remove(key) }
    }

    override suspend fun put(item: T) {
        context.dataStore.edit { value -> value[key] = serialize(item) }
    }
}