package com.shahaabapps.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.ui.R

/**
 * Parallel's two typefaces, both bundled under the SIL Open Font Licence
 * (assets/licenses). Schibsted Grotesk is a variable font, so each weight is
 * the same file read at a different `wght`; IBM Plex Mono carries the small
 * uppercase labels, times and figures.
 */
val SansFamily = FontFamily(
    sans(FontWeight.Normal),
    sans(FontWeight.Medium),
    sans(FontWeight.SemiBold),
    sans(FontWeight.Bold),
)

/**
 * One weight of the variable file. The `wght` setting has to be passed by
 * name: the shorter Font(resId, weight) overload only labels the entry with a
 * weight and draws the file's default instance, so every weight came out as
 * Regular.
 */
@OptIn(ExperimentalTextApi::class)
private fun sans(weight: FontWeight) = Font(
    R.font.schibsted_grotesk,
    weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
)

val MonoFamily = FontFamily(
    Font(R.font.ibm_plex_mono_regular, FontWeight.Normal),
    Font(R.font.ibm_plex_mono_medium, FontWeight.Medium),
)

/**
 * The scale from the prototype: a 34sp semibold page title drawn slightly
 * tight, 15 to 16sp body, and an 11sp mono label for section headers and
 * eyebrows, letter-spaced and uppercased where it is used.
 */
val CupertinoTypography = Typography(
    // Page title
    displayLarge = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 34.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.85).sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.5).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.3).sp,
    ),
    titleLarge = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.2).sp,
    ),
    titleMedium = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 21.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    // Buttons
    labelLarge = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
    ),
    // Section headers and eyebrows: mono, spaced, uppercased where used
    labelMedium = TextStyle(
        fontFamily = MonoFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.9.sp,
    ),
    // Tab bar labels
    labelSmall = TextStyle(
        fontFamily = SansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
    ),
)

/**
 * Departures' three typefaces, bundled under the SIL Open Font Licence
 * (assets/licenses): Barlow for what you read, Barlow Condensed for the
 * board's headings, labels and buttons, and JetBrains Mono for times, gates
 * and figures. JetBrains Mono is a variable font, read at the two weights the
 * prototype uses.
 */
val BarlowFamily = FontFamily(
    Font(R.font.barlow_regular, FontWeight.Normal),
    Font(R.font.barlow_medium, FontWeight.Medium),
    Font(R.font.barlow_semibold, FontWeight.SemiBold),
)

val CondensedFamily = FontFamily(
    Font(R.font.barlow_condensed_medium, FontWeight.Medium),
    Font(R.font.barlow_condensed_semibold, FontWeight.SemiBold),
    Font(R.font.barlow_condensed_bold, FontWeight.Bold),
)

@OptIn(ExperimentalTextApi::class)
private fun jetbrains(weight: FontWeight) = Font(
    R.font.jetbrains_mono,
    weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
)

val JetBrainsFamily = FontFamily(
    jetbrains(FontWeight.Normal),
    jetbrains(FontWeight.Medium),
    jetbrains(FontWeight.Bold),
)

/**
 * The board's scale: a 52sp condensed page title, set in capitals where it is
 * drawn, Barlow at 15 to 16sp for reading, and condensed, letter-spaced labels
 * for column heads, eyebrows, buttons and tabs.
 */
val DeparturesTypography = Typography(
    displayLarge = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 52.sp, lineHeight = 50.sp, letterSpacing = 0.5.sp),
    headlineLarge = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 34.sp, letterSpacing = 0.3.sp),
    headlineMedium = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 26.sp, lineHeight = 30.sp, letterSpacing = 0.2.sp),
    titleLarge = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 26.sp),
    titleMedium = TextStyle(fontFamily = BarlowFamily, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, lineHeight = 22.sp),
    bodyLarge = TextStyle(fontFamily = BarlowFamily, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontFamily = BarlowFamily, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 21.sp),
    bodySmall = TextStyle(fontFamily = BarlowFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.sp),
    // Buttons: condensed capitals, as "BOARD NOW"
    labelLarge = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 22.sp, letterSpacing = 1.4.sp),
    // Column heads and eyebrows
    labelMedium = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 16.sp, letterSpacing = 1.4.sp),
    // Tabs
    labelSmall = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, lineHeight = 16.sp, letterSpacing = 1.0.sp),
)
