package com.example.superdmbtimer.domain.use_cases

import com.example.superdmbtimer.data.DataStorePref
import com.example.superdmbtimer.data.EndPref
import com.example.superdmbtimer.data.StartPref
import com.example.superdmbtimer.domain.model.PersonDate
import javax.inject.Inject

class SetPersonUseCase @Inject constructor(
    private val namePref: DataStorePref<String>,
    @StartPref private val startPref: DataStorePref<Int>,
    @EndPref private val endPref: DataStorePref<Int>
) {
    suspend operator fun invoke(name: String, dates: PersonDate) {
        namePref.setValue(name)
        startPref.setValue(dates.start)
        endPref.setValue(dates.end)
    }
}