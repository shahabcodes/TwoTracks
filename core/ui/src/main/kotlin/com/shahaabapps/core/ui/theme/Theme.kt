package com.shahaabapps.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes as M3Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** User-selectable light/dark preference. */
enum class ThemeMode { SYSTEM, LIGHT, DARK }

private fun lightColors(accent: Color, grouped: Color) = lightColorScheme(
    primary = accent,
    onPrimary = Color.White,
    secondary = accent,
    error = Palette.DestructiveLight,
    background = grouped,
    onBackground = Palette.LabelLight,
    surface = Palette.SurfaceLight,
    onSurface = Palette.LabelLight,
    surfaceVariant = Palette.SurfaceVariantLight,
    onSurfaceVariant = Palette.SecondaryLabelLight,
    outline = Palette.OutlineLight,
)

private fun darkColors(accent: Color, grouped: Color) = darkColorScheme(
    primary = accent,
    onPrimary = Color(0xFF0B1220),
    secondary = accent,
    error = Palette.DestructiveDark,
    background = grouped,
    onBackground = Palette.LabelDark,
    surface = Palette.SurfaceDark,
    onSurface = Palette.LabelDark,
    surfaceVariant = Palette.SurfaceVariantDark,
    onSurfaceVariant = Palette.SecondaryLabelDark,
    outline = Palette.OutlineDark,
)

private fun cupertinoColors(dark: Boolean, palette: AccentPalette) = CupertinoColors(
    groupedBackground = if (dark) palette.groupedDark else palette.groupedLight,
    card = if (dark) Palette.SurfaceDark else Palette.SurfaceLight,
    secondaryLabel = if (dark) Palette.SecondaryLabelDark else Palette.SecondaryLabelLight,
    separator = if (dark) Palette.SeparatorDark else Palette.SeparatorLight,
    fill = if (dark) Palette.FillDark else palette.fillLight,
    accent = if (dark) palette.accentDark else palette.accentLight,
    accentText = if (dark) palette.accentTextDark else palette.accentTextLight,
    onSolid = if (dark) Palette.OnSolidDark else Palette.OnSolidLight,
    gradient = palette.gradient,
    destructive = if (dark) Palette.DestructiveDark else Palette.DestructiveLight,
    success = if (dark) Palette.SuccessDark else Palette.SuccessLight,
    blocked = if (dark) Palette.DestructiveDark else Palette.DestructiveLight,
    blockedBackground = if (dark) Palette.BlockedBgDark else Palette.BlockedBgLight,
    allowed = if (dark) Palette.SuccessDark else Palette.SuccessLight,
    allowedBackground = if (dark) Palette.AllowedBgDark else Palette.AllowedBgLight,
    warn = if (dark) Palette.WarnDark else Palette.WarnLight,
)

/**
 * Material3 shapes mapped to the browser app's radii: dialogs 14dp
 * (extraLarge), sheets and content cards 16dp (large), settings cards 12dp
 * (medium), fields and pills 10dp (small).
 */
private val CupertinoM3Shapes = M3Shapes(
    extraSmall = RoundedCornerShape(7.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(14.dp),
)

/**
 * Root theme: Material3 scheme keyed to the chosen [accentPalette], plus the
 * app's [CupertinoColors].
 */
@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    accentPalette: AccentPalette = AccentPalette.QATAR,
    content: @Composable () -> Unit,
) {
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val accent = if (dark) accentPalette.accentDark else accentPalette.accentLight

    CompositionLocalProvider(LocalCupertinoColors provides cupertinoColors(dark, accentPalette)) {
        MaterialTheme(
            colorScheme = if (dark) darkColors(accent, accentPalette.groupedDark) else lightColors(accent, accentPalette.groupedLight),
            typography = CupertinoTypography,
            shapes = CupertinoM3Shapes,
            content = content,
        )
    }
}

/** Convenience accessor for the current [CupertinoColors]. */
object AppTheme {
    val colors: CupertinoColors
        @Composable get() = LocalCupertinoColors.current
}
