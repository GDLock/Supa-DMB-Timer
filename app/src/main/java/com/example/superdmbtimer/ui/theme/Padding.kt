package com.example.superdmbtimer.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Stable
class Padding(
    val general: PaddingValues = PaddingValues(28.dp, 15.dp),
    val topBar: PaddingValues = PaddingValues(8.dp, 4.dp)
)

val MaterialTheme.padding: Padding
    @Composable
    @ReadOnlyComposable
    get() = LocalPadding.current

internal val LocalPadding = staticCompositionLocalOf { Padding() }