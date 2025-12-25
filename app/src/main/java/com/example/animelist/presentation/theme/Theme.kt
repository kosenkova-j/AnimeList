// presentation/theme/Theme.kt
package com.example.animelist.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = CoralAccent,
    onPrimary = Color.White,
    primaryContainer = PeachLight,
    onPrimaryContainer = BrownText,

    secondary = PeachDark,
    onSecondary = Color.White,
    secondaryContainer = PeachContainer,
    onSecondaryContainer = BrownText,

    background = CreamWhite,
    onBackground = BrownText,

    surface = Color.White,
    onSurface = BrownText,
    surfaceVariant = PeachContainer,
    onSurfaceVariant = WarmGray,

    outline = PeachDark,
    outlineVariant = PeachLight
)

private val DarkColorScheme = darkColorScheme(
    primary = PeachLight,
    onPrimary = BrownText,
    primaryContainer = DarkCard,
    onPrimaryContainer = PeachLight,

    secondary = PeachDark,
    onSecondary = Color.White,
    secondaryContainer = DarkCard,
    onSecondaryContainer = PeachLight,

    background = DarkBackground,
    onBackground = Color.White,

    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkCard,
    onSurfaceVariant = WarmGray,

    outline = PeachDark,
    outlineVariant = DarkCard
)

@Composable
fun AnimeListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}