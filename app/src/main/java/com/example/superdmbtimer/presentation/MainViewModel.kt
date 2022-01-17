package com.example.superdmbtimer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superdmbtimer.data.DataStorePref
import com.example.superdmbtimer.domain.model.Theme
import com.example.superdmbtimer.domain.use_cases.GetNameFlowUseCase
import com.example.superdmbtimer.domain.use_cases.GetThemeUseCase
import com.example.superdmbtimer.navigation.Destinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getThemeUseCase: GetThemeUseCase,
    getNameFlowUseCase: GetNameFlowUseCase,
) : ViewModel() {

    val state = combine(
        getThemeUseCase(),
        getNameFlowUseCase().map {
            if (it.isBlank()) Destinations.person.route
            else Destinations.home.route
        }
    ) { theme, startRoute ->
        MainViewState(theme, startRoute, true)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MainViewState()
    )
}

data class MainViewState(
    val theme: Theme = Theme.LIKE_SYSTEM,
    val startRoute: String = "",
    val isLoaded: Boolean = false
)