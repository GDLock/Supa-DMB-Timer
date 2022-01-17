package com.example.superdmbtimer.domain.use_cases

import com.example.superdmbtimer.data.DataStorePref
import javax.inject.Inject

class GetNameFlowUseCase @Inject constructor(
    private val namePref: DataStorePref<String>
) {
    operator fun invoke() = namePref.value
}