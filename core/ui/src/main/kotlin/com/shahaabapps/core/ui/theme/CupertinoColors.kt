package com.shahaabapps.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * App-specific colours beyond Material3's ColorScheme: the page, hairlines,
 * the translucent fill, the two people's track colours and the status
 * families. Exposed via [LocalCupertinoColors]; read through [AppTheme.colors].
 */
@Immutable
data class CupertinoColors(
    /** The page. White in Parallel; the name is kept from the grouped look. */
    val groupedBackground: Color,
    val card: Color,
    /** Full-strength ink, for the rule that opens a section. */
    val ink: Color,
    val secondaryLabel: Color,
    val separator: Color,
    /** Translucent ink used for text fields, segmented tracks and idle pills. */
    val fill: Color,
    /** What a primary button or chosen chip is filled with: ink in Parallel. */
    val accent: Color,
    /** The colour of a link or a text action, which is not the fill colour. */
    val accentText: Color,
    /**
     * The ink for anything filled solid with the accent, a track or a status
     * colour. White in light, near-black in dark, because the dark palette is
     * pastel by design.
     */
    val onSolid: Color,
    /** The developer's track. */
    val tech: Color,
    /** The teacher's track. */
    val teacher: Color,
    val gradient: List<Color>,
    val destructive: Color,
    val success: Color,
    val blocked: Color,
    val blockedBackground: Color,
    val allowed: Color,
    val allowedBackground: Color,
    val warn: Color,
    /** The darkest surface: the flip tiles behind a time in Departures, the card in Parallel. */
    val deep: Color = card,
    /** An outline that has to be seen on its own: an empty stop, an idle control. */
    val strongBorder: Color = separator,
) {
    /** The illustration gradient as a diagonal brush. */
    val heroBrush: Brush
        get() = Brush.linearGradient(gradient)

    /** A person's track colour: [tech] for the developer, [teacher] otherwise. */
    fun track(isTech: Boolean): Color = if (isTech) tech else teacher
}

internal fun parallelColors(dark: Boolean) = CupertinoColors(
    groupedBackground = if (dark) Palette.PageDark else Palette.PageLight,
    card = if (dark) Palette.CardDark else Palette.CardLight,
    ink = if (dark) Palette.LabelDark else Palette.LabelLight,
    secondaryLabel = if (dark) Palette.SecondaryLabelDark else Palette.SecondaryLabelLight,
    separator = if (dark) Palette.SeparatorDark else Palette.SeparatorLight,
    fill = if (dark) Palette.FillDark else Palette.FillLight,
    accent = if (dark) Palette.AccentDark else Palette.AccentLight,
    accentText = if (dark) Palette.AccentTextDark else Palette.AccentTextLight,
    onSolid = if (dark) Palette.OnSolidDark else Palette.OnSolidLight,
    tech = if (dark) Palette.TechDark else Palette.TechLight,
    teacher = if (dark) Palette.TeacherDark else Palette.TeacherLight,
    gradient = Palette.Gradient,
    destructive = if (dark) Palette.DestructiveDark else Palette.DestructiveLight,
    success = if (dark) Palette.SuccessDark else Palette.SuccessLight,
    blocked = if (dark) Palette.DestructiveDark else Palette.DestructiveLight,
    blockedBackground = if (dark) Palette.BlockedBgDark else Palette.BlockedBgLight,
    allowed = if (dark) Palette.SuccessDark else Palette.SuccessLight,
    allowedBackground = if (dark) Palette.AllowedBgDark else Palette.AllowedBgLight,
    warn = if (dark) Palette.WarnDark else Palette.WarnLight,
)

internal fun departuresColors() = CupertinoColors(
    groupedBackground = DeparturesPalette.Page,
    card = DeparturesPalette.Card,
    ink = DeparturesPalette.Label,
    secondaryLabel = DeparturesPalette.SecondaryLabel,
    separator = DeparturesPalette.Separator,
    fill = DeparturesPalette.Fill,
    accent = DeparturesPalette.Signal,
    accentText = DeparturesPalette.Signal,
    onSolid = DeparturesPalette.OnSignal,
    tech = DeparturesPalette.Signal,
    teacher = DeparturesPalette.Teacher,
    gradient = DeparturesPalette.Gradient,
    destructive = DeparturesPalette.Destructive,
    success = DeparturesPalette.Success,
    blocked = DeparturesPalette.Destructive,
    blockedBackground = DeparturesPalette.DestructiveBg,
    allowed = DeparturesPalette.Success,
    allowedBackground = DeparturesPalette.SuccessBg,
    warn = DeparturesPalette.Warn,
    deep = DeparturesPalette.Deep,
    strongBorder = DeparturesPalette.StrongBorder,
)

val LocalCupertinoColors = staticCompositionLocalOf {
    // Light defaults; always overridden by AppTheme.
    parallelColors(dark = false)
}
