package com.flexcil.flexc.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = TatraPrimaryBlue,
    onPrimary = TatraOnPrimary,

    tertiary = TatraTertiaryGreen, // Використовуємо для графіків та сум

    background = TatraDarkBackground,
    onBackground = TatraDarkTextPrimary,

    surface = TatraDarkSurface,
    onSurface = TatraDarkTextPrimary,

    surfaceVariant = TatraDarkSurfaceVariant,
    onSurfaceVariant = TatraDarkTextSecondary, // Ідеально підходить для сірого тексту

    outline = TatraDarkSurfaceVariant, // Для рамок та розділювачів
    error = TatraError
)

@Composable
fun ApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}