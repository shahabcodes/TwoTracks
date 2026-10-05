package com.shahaabapps.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * App-specific colours beyond Material3's ColorScheme: the grouped backdrop,
 * card surface, hairline separators, the translucent field fill, the current
 * accent + its hero gradient, and semantic status families.
 * Exposed via [LocalCupertinoColors]; read through [AppTheme.colors].
 */
@Immutable
data class CupertinoColors(
    val groupedBackground: Color,
    val card: Color,
    val secondaryLabel: Color,
    val separator: Color,
    /** Translucent gray used for text fields, segmented tracks and idle pills. */
    val fill: Color,
    val accent: Color,
    val gradient: List<Color>,
    val destructive: Color,
    val success: Color,
    val blocked: Color,
    val blockedBackground: Color,
    val allowed: Color,
    val allowedBackground: Color,
    val warn: Color,
) {
    /** The accent hero gradient as a diagonal brush. */
    val heroBrush: Brush
        get() = Brush.linearGradient(gradient)
}

val LocalCupertinoColors = staticCompositionLocalOf {
    // Light Qatar defaults; always overridden by AppTheme.
    CupertinoColors(
        groupedBackground = Palette.GroupedBackgroundLight,
        card = Palette.SurfaceLight,
        secondaryLabel = Palette.SecondaryLabelLight,
        separator = Palette.SeparatorLight,
        fill = Palette.FillLight,
        accent = AccentPalette.QATAR.accentLight,
        gradient = AccentPalette.QATAR.gradient,
        destructive = Palette.DestructiveLight,
        success = Palette.SuccessLight,
        blocked = Palette.DestructiveLight,
        blockedBackground = Palette.BlockedBgLight,
        allowed = Palette.SuccessLight,
        allowedBackground = Palette.AllowedBgLight,
        warn = Palette.WarnLight,
    )
}
