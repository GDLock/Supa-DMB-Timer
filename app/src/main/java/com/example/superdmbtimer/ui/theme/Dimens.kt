package com.example.superdmbtimer.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
class Dimensions(
    val spaceLarge: Dp = 30.dp,
    val spaceMedium: Dp = 15.dp,
    val spaceSmall: Dp = 4.dp,
    val spaceMin: Dp = 2.dp,
)

val MaterialTheme.dimensions: Dimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current

internal val LocalDimensions = staticCompositionLocalOf { Dimensions() }