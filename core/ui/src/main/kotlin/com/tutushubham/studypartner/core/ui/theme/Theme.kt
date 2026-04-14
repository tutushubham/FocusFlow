package com.tutushubham.studypartner.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors =
    lightColorScheme(
        primary = FocusPrimary,
        onPrimary = FocusOnPrimary,
        primaryContainer = Color(0xFFD8E4FF),
        onPrimaryContainer = Color(0xFF001B3D),
        secondary = Color(0xFF535F70),
        onSecondary = Color.White,
        background = FocusBackground,
        onBackground = Color(0xFF1A1C1E),
        surface = FocusSurface,
        onSurface = Color(0xFF1A1C1E),
        surfaceVariant = Color(0xFFE1E4EA),
        onSurfaceVariant = Color(0xFF43474E),
        outline = FocusOutlineVariant,
    )

private val DarkColors = darkColorScheme()

@Composable
fun FocusFlowTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = FocusTypography,
        shapes = FocusShapes,
        content = content,
    )
}
