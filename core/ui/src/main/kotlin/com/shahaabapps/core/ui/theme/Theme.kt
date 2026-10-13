package com.shahaabapps.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes as M3Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

/** User-selectable light/dark preference. Departures ignores it: the board is dark. */
enum class ThemeMode { SYSTEM, LIGHT, DARK }

/**
 * Which of the two designs the app is drawn in. Both read the same state and
 * do the same things; each screen has a layout for each, taken from its
 * prototype.
 */
enum class AppLook { PARALLEL, DEPARTURES }

/** The three roles a look's typefaces play, for code shared by both looks. */
@Immutable
data class AppFonts(val sans: FontFamily, val display: FontFamily, val mono: FontFamily)

private val ParallelFonts = AppFonts(sans = SansFamily, display = SansFamily, mono = MonoFamily)
private val DeparturesFonts = AppFonts(sans = BarlowFamily, display = CondensedFamily, mono = JetBrainsFamily)

val LocalAppLook = staticCompositionLocalOf { AppLook.PARALLEL }
val LocalAppFonts = staticCompositionLocalOf { ParallelFonts }

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

private val departuresScheme = darkColorScheme(
    primary = DeparturesPalette.Signal,
    onPrimary = DeparturesPalette.OnSignal,
    secondary = DeparturesPalette.Signal,
    error = DeparturesPalette.Destructive,
    background = DeparturesPalette.Page,
    onBackground = DeparturesPalette.Label,
    surface = DeparturesPalette.Card,
    onSurface = DeparturesPalette.Label,
    surfaceVariant = DeparturesPalette.Card,
    onSurfaceVariant = DeparturesPalette.SecondaryLabel,
    outline = DeparturesPalette.Separator,
    outlineVariant = DeparturesPalette.Separator,
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

/** Whether [mode] comes out dark under [look]. Departures always does. */
@Composable
fun isDark(mode: ThemeMode, look: AppLook): Boolean = look == AppLook.DEPARTURES || when (mode) {
    ThemeMode.SYSTEM -> isSystemInDarkTheme()
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
}

/** Root theme: Parallel in light or dark, or the Departures board, with each one's type. */
@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    look: AppLook = AppLook.PARALLEL,
    content: @Composable () -> Unit,
) {
    val dark = isDark(themeMode, look)
    val departures = look == AppLook.DEPARTURES
    CompositionLocalProvider(
        LocalCupertinoColors provides if (departures) departuresColors() else parallelColors(dark),
        LocalAppLook provides look,
        LocalAppFonts provides if (departures) DeparturesFonts else ParallelFonts,
    ) {
        MaterialTheme(
            colorScheme = when { departures -> departuresScheme; dark -> darkColors; else -> lightColors },
            typography = if (departures) DeparturesTypography else CupertinoTypography,
            shapes = ParallelM3Shapes,
            content = content,
        )
    }
}

/** Convenience accessor for the current [CupertinoColors]. */
object AppTheme {
    val colors: CupertinoColors
        @Composable get() = LocalCupertinoColors.current

    val look: AppLook
        @Composable get() = LocalAppLook.current

    /** True while drawing the Departures board. */
    val departures: Boolean
        @Composable get() = LocalAppLook.current == AppLook.DEPARTURES

    val fonts: AppFonts
        @Composable get() = LocalAppFonts.current
}

/**
 * Text as the current look sets a title or a button: unchanged in Parallel,
 * in capitals on the Departures board, which writes its headings, buttons
 * and tabs as a board does.
 */
@Composable
fun displayCase(text: String): String = if (LocalAppLook.current == AppLook.DEPARTURES) text.uppercase(java.util.Locale.ROOT) else text
