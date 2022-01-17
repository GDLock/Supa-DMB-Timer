package com.example.superdmbtimer.domain.use_cases

import com.example.superdmbtimer.data.DataStorePref
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetNameUseCase @Inject constructor(
    private val namePref: DataStorePref<String>
) {
    suspend operator fun invoke() = namePref.value.first()
}