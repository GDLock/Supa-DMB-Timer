package com.example.superdmbtimer.domain.use_cases

import com.example.superdmbtimer.data.DataStorePref
import com.example.superdmbtimer.data.EndPref
import com.example.superdmbtimer.data.StartPref
import com.example.superdmbtimer.domain.model.PersonDate
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class GetDatesUseCase @Inject constructor(
    @StartPref private val startPref: DataStorePref<Int>,
    @EndPref private val endPref: DataStorePref<Int>,
) {
    suspend operator fun invoke(): PersonDate {
        return coroutineScope {
            val start = async { startPref.value.first()}
            val end = async{ endPref.value.first()}
            PersonDate(start.await(), end.await())
        }
    }
}