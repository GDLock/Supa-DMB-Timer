package com.example.superdmbtimer.domain.use_cases

import android.util.Log
import com.example.superdmbtimer.data.DataStorePref
import com.example.superdmbtimer.data.EndPref
import com.example.superdmbtimer.data.StartPref
import com.example.superdmbtimer.domain.model.PersonDate
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GetDatesFlowUseCase @Inject constructor(
    @StartPref private val startPref: DataStorePref<Int>,
    @EndPref private val endPref: DataStorePref<Int>,
) {
    operator fun invoke() =
        combine(startPref.value.onEach { Log.d("Start", it.toString()) }, endPref.value.onEach { Log.d("End", it.toString()) }) { start, end -> PersonDate(start, end) }
}