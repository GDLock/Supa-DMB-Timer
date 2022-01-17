package com.example.superdmbtimer.domain.use_cases

import com.example.superdmbtimer.data.DataStorePref
import com.example.superdmbtimer.data.EndPref
import com.example.superdmbtimer.data.StartPref
import com.example.superdmbtimer.domain.model.PersonDate
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
            var start = 0
            var end = 0

            val job1 = launch { start = startPref.value.first()}
            val job2 = launch { end = endPref.value.first()}
            job1.join()
            job2.join()
            PersonDate(start, end)
        }
    }
}