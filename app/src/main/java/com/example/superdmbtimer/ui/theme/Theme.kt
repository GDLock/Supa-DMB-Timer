package com.example.superdmbtimer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.superdmbtimer.domain.model.Theme

private val DarkColorPalette = darkColors(
    primary = LightYellow,
    primaryVariant = LightYellow,
    secondary = LightYellow
)

private val LightColorPalette = lightColors(
    primary = Yellow,
    primaryVariant = Yellow,
    secondary = Yellow
)

@Composable
fun SuperDMBTimerTheme(
    theme: Theme = Theme.LIKE_SYSTEM,
    content: @Composable () -> Unit
) {
    val colors = when (theme) {
        Theme.LIKE_SYSTEM -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
        Theme.LIGHT -> LightColorPalette
        Theme.DARK -> DarkColorPalette
    }

    CompositionLocalProvider(
        LocalDimensions provides Dimensions(),
        LocalPadding provides Padding()
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}