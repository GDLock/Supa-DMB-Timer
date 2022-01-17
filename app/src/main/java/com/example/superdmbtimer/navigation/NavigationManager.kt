package com.example.superdmbtimer.navigation

import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {

    private val _commands = Channel<NavigationCommand>()
    val commands = _commands.receiveAsFlow()

    suspend fun navigateUp() = _commands.send(NavigationCommand.Back)

    suspend fun navigate(
        destination: String,
        arguments: String = "",
        builder: NavOptionsBuilder.() -> Unit = {}
    ) {
        _commands.send(
            NavigationCommand.To(
                route = destination + arguments,
                navOptions = navOptions(builder)
            )
        )
    }
}

sealed class NavigationCommand {
    object Back : NavigationCommand()
    data class To(val route: String, val navOptions: NavOptions?) : NavigationCommand()
}