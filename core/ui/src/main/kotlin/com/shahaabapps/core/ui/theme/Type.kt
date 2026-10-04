package com.shahaabapps.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Typography scaled to the browser app's iOS text sizes:
 * large title 31sp bold, title 22sp bold, headline 17sp semibold,
 * body 17sp, subhead 15sp, footnote 13sp, caption 12sp (uppercase, spaced).
 */
private val CupertinoFontFamily = FontFamily.Default

val CupertinoTypography = Typography(
    // iOS "Large Title" (Browse Safe settings title: 31sp bold)
    displayLarge = TextStyle(
        fontFamily = CupertinoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 31.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.2).sp,
    ),
    // "Title 1"
    headlineLarge = TextStyle(
        fontFamily = CupertinoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
    ),
    // "Title 2"
    headlineMedium = TextStyle(
        fontFamily = CupertinoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    // Nav bar title
    titleLarge = TextStyle(
        fontFamily = CupertinoFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
    ),
    // "Headline"
    titleMedium = TextStyle(
        fontFamily = CupertinoFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
    ),
    // "Body"
    bodyLarge = TextStyle(
        fontFamily = CupertinoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 22.sp,
    ),
    // "Subhead"
    bodyMedium = TextStyle(
        fontFamily = CupertinoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 20.sp,
    ),
    // "Footnote"
    bodySmall = TextStyle(
        fontFamily = CupertinoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    // Section header / caption: 12sp, uppercase + letter-spaced where used
    labelMedium = TextStyle(
        fontFamily = CupertinoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    // Tab bar labels
    labelSmall = TextStyle(
        fontFamily = CupertinoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 12.sp,
    ),
)
