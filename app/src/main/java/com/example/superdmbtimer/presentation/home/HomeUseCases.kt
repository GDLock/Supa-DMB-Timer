package com.example.superdmbtimer.presentation.home

import com.example.superdmbtimer.domain.use_cases.GetDatesFlowUseCase
import com.example.superdmbtimer.domain.use_cases.GetNameFlowUseCase
import javax.inject.Inject

data class HomeUseCases @Inject constructor(
    val getNameFlowUseCase: GetNameFlowUseCase,
    val getDatesFlowUseCase: GetDatesFlowUseCase,
)