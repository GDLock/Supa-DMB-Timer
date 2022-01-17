@file:OptIn(ExperimentalAnimationApi::class)

package com.example.superdmbtimer.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import com.example.superdmbtimer.navigation.Destinations.home
import com.example.superdmbtimer.navigation.Destinations.person
import com.example.superdmbtimer.navigation.Destinations.settings
import com.example.superdmbtimer.navigation.Direction
import com.example.superdmbtimer.navigation.NavigationCommand
import com.example.superdmbtimer.navigation.NavigationManager
import com.example.superdmbtimer.presentation.home.HomeScreen
import com.example.superdmbtimer.presentation.person.PersonScreen
import com.example.superdmbtimer.presentation.settings.SettingsScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun AnimatedNavGraph(navigationManager: NavigationManager, startRoute: String) {

    val navController = rememberAnimatedNavController()

    LaunchedEffect(true) {
        navigationManager.commands.collect {
            when (it) {
                NavigationCommand.Back -> navController.navigateUp()
                is NavigationCommand.To -> navController.navigate(it.route, it.navOptions)
            }
        }
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        destination(home) { HomeScreen() }
        destination(settings) { SettingsScreen() }
        destination(person) { PersonScreen() }
    }
}

fun NavGraphBuilder.destination(
    navCommand: Direction,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) = composable(
    navCommand.route,
    navCommand.arguments,
    navCommand.deepLink,
    popEnterTransition = navCommand.popEnterTransition,
    popExitTransition = navCommand.popExitTransition,
    exitTransition = navCommand.exitTransition,
    enterTransition = navCommand.enterTransition,
    content = content
)