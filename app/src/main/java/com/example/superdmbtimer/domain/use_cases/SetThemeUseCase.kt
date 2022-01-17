package com.example.superdmbtimer.domain.use_cases

import com.example.superdmbtimer.data.DataStorePref
import com.example.superdmbtimer.domain.model.Theme
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val themePref: DataStorePref<Theme>
) {
    suspend operator fun invoke(value: Theme) = themePref.setValue(value)
}