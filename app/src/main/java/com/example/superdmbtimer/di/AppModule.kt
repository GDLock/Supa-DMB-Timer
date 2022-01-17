package com.example.superdmbtimer.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.superdmbtimer.core.Timer
import com.example.superdmbtimer.data.DataStorePref
import com.example.superdmbtimer.data.DataStoreUtil
import com.example.superdmbtimer.data.EndPref
import com.example.superdmbtimer.data.StartPref
import com.example.superdmbtimer.domain.model.Theme
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideNamePref(@ApplicationContext context: Context): DataStorePref<String> {
        return DataStorePref.NamePref(
            DataStoreUtil.Base(
                stringPreferencesKey("name"),
                context.dataStore
            )
        )
    }

    @StartPref
    @Singleton
    @Provides
    fun provideStartPref(@ApplicationContext context: Context): DataStorePref<Int> {
        return DataStorePref.StartPref(
            DataStoreUtil.Base(
                intPreferencesKey("start"),
                context.dataStore
            )
        )
    }

    @EndPref
    @Singleton
    @Provides
    fun provideEndPref(@ApplicationContext context: Context): DataStorePref<Int> {
        return DataStorePref.EndPref(
            DataStoreUtil.Base(
                intPreferencesKey("end"),
                context.dataStore
            )
        )
    }

    @Singleton
    @Provides
    fun provideThemePref(@ApplicationContext context: Context): DataStorePref<Theme> {
        return DataStorePref.ThemePref(
            DataStoreUtil.Base(
                stringPreferencesKey("theme"),
                context.dataStore
            )
        )
    }

    @Singleton
    @Provides
    fun provideTimer(): Timer = Timer.Base()

}