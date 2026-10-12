package com.shahaabapps.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes as M3Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

/** User-selectable light/dark preference. */
enum class ThemeMode { SYSTEM, LIGHT, DARK }

private val lightColors = lightColorScheme(
    primary = Palette.AccentLight,
    onPrimary = Palette.OnSolidLight,
    secondary = Palette.AccentTextLight,
    error = Palette.DestructiveLight,
    background = Palette.PageLight,
    onBackground = Palette.LabelLight,
    surface = Palette.CardLight,
    onSurface = Palette.LabelLight,
    surfaceVariant = Palette.SurfaceVariantLight,
    onSurfaceVariant = Palette.SecondaryLabelLight,
    outline = Palette.SeparatorLight,
    outlineVariant = Palette.SeparatorLight,
)

private val darkColors = darkColorScheme(
    primary = Palette.AccentDark,
    onPrimary = Palette.OnSolidDark,
    secondary = Palette.AccentTextDark,
    error = Palette.DestructiveDark,
    background = Palette.PageDark,
    onBackground = Palette.LabelDark,
    surface = Palette.CardDark,
    onSurface = Palette.LabelDark,
    surfaceVariant = Palette.SurfaceVariantDark,
    onSurfaceVariant = Palette.SecondaryLabelDark,
    outline = Palette.SeparatorDark,
    outlineVariant = Palette.SeparatorDark,
)

/**
 * Material3 shapes at Parallel's radii: 12dp buttons and fields, 16dp cards,
 * 28dp sheets.
 */
private val ParallelM3Shapes = M3Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

/** Root theme: the Parallel palette in light or dark, and its type. */
@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    content: @Composable () -> Unit,
) {
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    CompositionLocalProvider(LocalCupertinoColors provides parallelColors(dark)) {
        MaterialTheme(
            colorScheme = if (dark) darkColors else lightColors,
            typography = CupertinoTypography,
            shapes = ParallelM3Shapes,
            content = content,
        )
    }
}

/** Convenience accessor for the current [CupertinoColors]. */
object AppTheme {
    val colors: CupertinoColors
        @Composable get() = LocalCupertinoColors.current
}
