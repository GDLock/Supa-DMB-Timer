package com.example.superdmbtimer.presentation.home

import com.example.superdmbtimer.core.BaseViewModel
import com.example.superdmbtimer.core.Timer
import com.example.superdmbtimer.navigation.Destinations
import com.example.superdmbtimer.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    navigationManager: NavigationManager,
    timer: Timer,
    homeUseCases: HomeUseCases
) : BaseViewModel<HomeViewState, HomeAction>(HomeViewState(), navigationManager) {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val timerFlow = homeUseCases.getDatesFlowUseCase().flatMapLatest { timer(it.end) }

    override val state = combine(
        homeUseCases.getNameFlowUseCase(),
        homeUseCases.getDatesFlowUseCase(),
        timerFlow
    ) { name, dates, remain ->
        HomeViewState(name, dates.start, dates.end, remain)
    }.stateInit()

    override fun action(action: HomeAction) {
        when (action) {
            HomeAction.Navigate -> navigate(Destinations.settings.route)
        }
    }
}