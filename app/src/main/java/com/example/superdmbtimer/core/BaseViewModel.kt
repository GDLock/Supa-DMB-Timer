package com.example.superdmbtimer.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptionsBuilder
import com.example.superdmbtimer.navigation.NavigationManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Action>(
    private val initial: State,
    private val navigationManager: NavigationManager
) : ViewModel() {
    abstract val state: StateFlow<State>

    protected fun Flow<State>.stateInit(): StateFlow<State> =
        stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), initial)

    abstract fun action(action: Action)

    protected fun back() = viewModelScope.launch { navigationManager.navigateUp() }

    protected fun navigate(
        destination: String,
        arguments: String = "",
        builder: NavOptionsBuilder.() -> Unit = {}
    ) = viewModelScope.launch { navigationManager.navigate(destination, arguments, builder) }
}