package com.andikas.wang.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.andikas.wang.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()

val WangTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily, letterSpacing = 0.sp),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = displayFontFamily,
        letterSpacing = 0.sp
    ),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily, letterSpacing = 0.sp),
    headlineLarge = baseline.headlineLarge.copy(
        fontFamily = displayFontFamily,
        letterSpacing = 0.sp
    ),
    headlineMedium = baseline.headlineMedium.copy(
        fontFamily = displayFontFamily,
        letterSpacing = 0.sp
    ),
    headlineSmall = baseline.headlineSmall.copy(
        fontFamily = displayFontFamily,
        letterSpacing = 0.sp
    ),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily, letterSpacing = 0.sp),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily, letterSpacing = 0.sp),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily, letterSpacing = 0.sp),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily, letterSpacing = 0.sp),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily, letterSpacing = 0.sp),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily, letterSpacing = 0.sp),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily, letterSpacing = 0.sp),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily, letterSpacing = 0.sp),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily, letterSpacing = 0.sp),
)

