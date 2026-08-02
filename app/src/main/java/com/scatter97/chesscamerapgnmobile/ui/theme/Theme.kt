package com.scatter97.chesscamerapgnmobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = Amber,
    secondary = Mint,
    tertiary = Sky,
    background = Night,
    surface = Panel,
    surfaceVariant = PanelRaised,
    onPrimary = Night,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
)

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF765A00),
    secondary = androidx.compose.ui.graphics.Color(0xFF006D3B),
    tertiary = androidx.compose.ui.graphics.Color(0xFF005FAE),
)

@Composable
fun KnightboardGoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography(),
        content = content,
    )
}
