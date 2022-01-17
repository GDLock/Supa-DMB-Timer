package com.example.superdmbtimer.data

import com.example.superdmbtimer.domain.model.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Qualifier

interface DataStorePref<T> {
    val value: Flow<T>
    suspend fun setValue(value: T)

    class NamePref(private val dataStoreUtil: DataStoreUtil<String>) : DataStorePref<String> {
        override val value = dataStoreUtil.get("")
        override suspend fun setValue(value: String) = dataStoreUtil.set(value)
    }

    class StartPref(private val dataStoreUtil: DataStoreUtil<Int>) : DataStorePref<Int> {
        override val value = dataStoreUtil.get(-1)
        override suspend fun setValue(value: Int) = dataStoreUtil.set(value)
    }

    class EndPref(private val dataStoreUtil: DataStoreUtil<Int>) : DataStorePref<Int> {
        override val value = dataStoreUtil.get(-1)
        override suspend fun setValue(value: Int) = dataStoreUtil.set(value)
    }

    class ThemePref(private val dataStoreUtil: DataStoreUtil<String>) : DataStorePref<Theme> {
        override val value = dataStoreUtil.get(Theme.LIKE_SYSTEM.name).map { Theme.valueOf(it) }
        override suspend fun setValue(value: Theme) = dataStoreUtil.set(value.name)
    }
}

@Qualifier annotation class StartPref

@Qualifier annotation class EndPref




