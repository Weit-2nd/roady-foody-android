package com.weit2nd.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme =
    darkColorScheme(
        primary = Primary,
        onPrimary = White,
        primaryContainer = White,
        onPrimaryContainer = Black,
        inversePrimary = Black,
        secondary = Secondary,
        onSecondary = White,
        secondaryContainer = White,
        onSecondaryContainer = Black,
        tertiary = Tertiary,
        onTertiary = White,
        tertiaryContainer = White,
        onTertiaryContainer = Black,
        background = White,
        onBackground = Black,
        surface = White,
        onSurface = Black,
        surfaceVariant = Gray5,
        onSurfaceVariant = Black,
        error = Error,
        onError = White,
        errorContainer = Gray5,
        onErrorContainer = Black,
        outline = Outline,
        outlineVariant = OutlineVariant,
        scrim = Scrim,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = Primary,
        onPrimary = White,
        primaryContainer = White,
        onPrimaryContainer = Black,
        inversePrimary = Black,
        secondary = Secondary,
        onSecondary = White,
        secondaryContainer = White,
        onSecondaryContainer = Black,
        tertiary = Tertiary,
        onTertiary = White,
        tertiaryContainer = White,
        onTertiaryContainer = Black,
        background = White,
        onBackground = Black,
        surface = White,
        onSurface = Black,
        surfaceVariant = Gray5,
        onSurfaceVariant = Black,
        error = Error,
        onError = White,
        errorContainer = Gray5,
        onErrorContainer = Black,
        outline = Outline,
        outlineVariant = OutlineVariant,
        scrim = Scrim,
    )

@Composable
fun RoadyFoodyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
