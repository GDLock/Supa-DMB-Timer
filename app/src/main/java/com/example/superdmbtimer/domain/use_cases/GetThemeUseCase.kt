package com.example.superdmbtimer.domain.use_cases

import com.example.superdmbtimer.data.DataStorePref
import com.example.superdmbtimer.domain.model.Theme
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val themePref: DataStorePref<Theme>
) {
    operator fun invoke() = themePref.value
}