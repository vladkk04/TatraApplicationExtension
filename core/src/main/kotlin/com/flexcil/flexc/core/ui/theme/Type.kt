package com.flexcil.flexc.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private fun appTextStyle(isDarkTheme: Boolean = false) = TextStyle(
    color = if (isDarkTheme) DarkTextColor else LightTextColor,
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp
)

val DarkTypography = Typography(
    displayLarge = appTextStyle(true).copy(fontSize = 48.sp),
    displayMedium = appTextStyle(true).copy(fontSize = 44.sp),
    displaySmall = appTextStyle(true).copy(fontSize = 40.sp),
    headlineLarge = appTextStyle(true).copy(fontSize = 38.sp),
    headlineMedium = appTextStyle(true).copy(fontSize = 44.sp),
    headlineSmall = appTextStyle(true).copy(fontSize = 40.sp),
    titleLarge = appTextStyle(true).copy(fontSize = 36.sp),
    titleMedium = appTextStyle(true).copy(fontSize = 32.sp),
    titleSmall = appTextStyle(true).copy(fontSize = 28.sp),
    bodyLarge = appTextStyle(true).copy(fontSize = 24.sp),
    bodyMedium = appTextStyle(true).copy(fontSize = 20.sp),
    bodySmall = appTextStyle(true).copy(fontSize = 16.sp),
    labelLarge = appTextStyle(true).copy(fontSize = 14.sp),
    labelMedium = appTextStyle(true).copy(fontSize = 12.sp),
    labelSmall = appTextStyle(true).copy(fontSize = 11.sp)
)

val LightTypography = Typography(
    displayLarge = appTextStyle(false).copy(fontSize = 48.sp),
    displayMedium = appTextStyle(false).copy(fontSize = 44.sp),
    displaySmall = appTextStyle(false).copy(fontSize = 40.sp),
    headlineLarge = appTextStyle(false).copy(fontSize = 38.sp),
    headlineMedium = appTextStyle(false).copy(fontSize = 44.sp),
    headlineSmall = appTextStyle(false).copy(fontSize = 40.sp),
    titleLarge = appTextStyle(false).copy(fontSize = 36.sp),
    titleMedium = appTextStyle(false).copy(fontSize = 32.sp),
    titleSmall = appTextStyle(false).copy(fontSize = 28.sp),
    bodyLarge = appTextStyle(false).copy(fontSize = 24.sp),
    bodyMedium = appTextStyle(false).copy(fontSize = 20.sp),
    bodySmall = appTextStyle(false).copy(fontSize = 16.sp),
    labelLarge = appTextStyle(false).copy(fontSize = 14.sp),
    labelMedium = appTextStyle(false).copy(fontSize = 12.sp),
    labelSmall = appTextStyle(false).copy(fontSize = 11.sp),
)