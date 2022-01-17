package com.example.superdmbtimer.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

interface DataStoreUtil<T> {
    fun get(default: T): Flow<T>
    suspend fun set(value: T)

    class Base<T>(
        private val key: Preferences.Key<T>,
        private val pref: DataStore<Preferences>
    ) : DataStoreUtil<T> {
        override fun get(default: T) = pref.data.map { it[key] ?: default }

        override suspend fun set(value: T) {
            pref.edit { it[key] = value }
        }
    }

    class Test<T> : DataStoreUtil<T> {
        private val t = MutableSharedFlow<T>()

        override fun get(default: T): Flow<T> = t

        override suspend fun set(value: T) = t.emit(value)
    }
}