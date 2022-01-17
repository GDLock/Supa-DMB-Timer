@file:OptIn(ExperimentalAnimationApi::class)

package com.example.superdmbtimer.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink

data class Direction(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLink: List<NavDeepLink> = emptyList()
) {
    companion object {
        const val ANIMATE_TIME = 300
        const val OFFSET = 300
    }

    val enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?) = {
        slideIn(AnimatedContentScope.SlideDirection.Left, OFFSET)
    }

    val exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?) = {
        slideOut(AnimatedContentScope.SlideDirection.Left, -OFFSET)
    }

    val popEnterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?) = {
        slideIn(AnimatedContentScope.SlideDirection.Right, -OFFSET)
    }

    val popExitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?) = {
        slideOut(AnimatedContentScope.SlideDirection.Right, OFFSET)
    }

    private fun AnimatedContentScope<NavBackStackEntry>.slideOut(
        towards: AnimatedContentScope.SlideDirection,
        targetOffset: Int
    ) = slideOutOfContainer(
        towards = towards,
        animationSpec = tween(ANIMATE_TIME),
        targetOffset = { targetOffset }
    ) + fadeOut(tween(ANIMATE_TIME))


    private fun AnimatedContentScope<NavBackStackEntry>.slideIn(
        towards: AnimatedContentScope.SlideDirection,
        initialOffset: Int
    ) = slideIntoContainer(
        towards = towards,
        animationSpec = tween(ANIMATE_TIME),
        initialOffset = { initialOffset }
    ) + fadeIn(tween(ANIMATE_TIME))
}