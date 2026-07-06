package com.johnmuindi.foreheadfrenzykenya.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary            = KenyanGreen,
    onPrimary          = KenyanWhite,
    primaryContainer   = KenyanGreenLight,
    onPrimaryContainer = KenyanWhite,
    secondary          = KenyanRed,
    onSecondary        = KenyanWhite,
    secondaryContainer = KenyanRedLight,
    onSecondaryContainer = KenyanWhite,
    tertiary           = MaasaiGold,
    background         = KenyanBlack,
    surface            = DarkSurface,
    surfaceVariant     = DarkSurfaceVariant,
    onSurface          = KenyanWhite,
    onSurfaceVariant   = Color(0xFFB0B0B0),
    error              = KenyanRedLight
)

private val LightColorScheme = lightColorScheme(
    primary            = KenyanGreen,
    onPrimary          = KenyanWhite,
    primaryContainer   = KenyanGreenContainer,
    onPrimaryContainer = KenyanGreen,
    secondary          = KenyanRed,
    onSecondary        = KenyanWhite,
    secondaryContainer = KenyanRedContainer,
    onSecondaryContainer = KenyanRed,
    tertiary           = MaasaiGold,
    background         = KenyanWhite,
    surface            = KenyanWhite,
    onSurface          = KenyanBlack,
    onSurfaceVariant   = Color(0xFF555555),
    error              = KenyanRed
)

@Composable
fun ForeheadFrenzyKenyaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}