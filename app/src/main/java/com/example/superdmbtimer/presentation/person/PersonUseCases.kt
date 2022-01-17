package com.example.superdmbtimer.presentation.person

import com.example.superdmbtimer.domain.use_cases.GetDatesUseCase
import com.example.superdmbtimer.domain.use_cases.GetNameUseCase
import com.example.superdmbtimer.domain.use_cases.SetPersonUseCase
import javax.inject.Inject

data class PersonUseCases @Inject constructor(
    val getDatesUseCase: GetDatesUseCase,
    val getNameUseCase: GetNameUseCase,
    val setPersonUseCase: SetPersonUseCase
)