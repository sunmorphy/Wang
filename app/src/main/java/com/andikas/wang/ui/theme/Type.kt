package com.andikas.wang.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.andikas.wang.R

val bodyFontFamily = FontFamily(
    Font(R.font.manrope_regular)
)

val displayFontFamily = FontFamily(
    Font(R.font.roboto_variable)
)

// Default Material 3 typography values
val baseline = Typography()

val WangTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily), // size: 57, line height: 64
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily), // size: 45, line height: 52
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily), // size: 36, line height: 44
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily), // size: 32, line height: 40
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily), // size: 28, line height: 36
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily), // size: 24, line height: 32
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily), // size: 22, line height: 28
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily), // size: 16, line height: 24
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily), // size: 14, line height: 20
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily), // size: 16, line height: 24
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily), // size: 14, line height: 20
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily), // size: 12, line height: 16
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily), // size: 14, line height: 20
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily), // size: 12, line height: 16
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily), // size: 11, line height: 16
)
