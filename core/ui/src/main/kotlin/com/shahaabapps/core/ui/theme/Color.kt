package com.shahaabapps.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Palette ported from the Browse Safe browser app: iOS grouped-settings
 * surfaces, hairline outlines, translucent gray fills, and semantic status
 * colours. Accent + hero gradient come from the selected [AccentPalette].
 */
internal object Palette {
    // Light
    val GroupedBackgroundLight = Color(0xFFF2F2F7)   // ios_grouped_bg
    val SurfaceLight = Color(0xFFFFFFFF)             // surface / settings card
    val SurfaceVariantLight = Color(0xFFEEF1F6)      // surface_variant / content card
    val OutlineLight = Color(0xFFDCE0E7)             // hairline
    val FillLight = Color(0x1E787880)                // ios_fill: fields, segment track, idle pills
    val LabelLight = Color(0xFF1A1C1E)
    val SecondaryLabelLight = Color(0xFF5F6368)
    val SeparatorLight = Color(0xFFDCE0E7)

    // Dark
    val GroupedBackgroundDark = Color(0xFF121316)
    val SurfaceDark = Color(0xFF1B1C1F)
    val SurfaceVariantDark = Color(0xFF2A2C31)
    val OutlineDark = Color(0xFF3C4043)
    val FillDark = Color(0x33787880)
    val LabelDark = Color(0xFFE3E3E6)
    val SecondaryLabelDark = Color(0xFF9AA0A6)
    val SeparatorDark = Color(0xFF3C4043)

    // Semantic (palette-independent)
    val DestructiveLight = Color(0xFFCC2A20)         // ios_red, darkened: 5.4:1 on a card, 4.8:1 on the page
    val DestructiveDark = Color(0xFFFF453A)
    val BlockedBgLight = Color(0xFFFFF3F2)
    val BlockedBgDark = Color(0xFF3A1D1A)

    val SuccessLight = Color(0xFF16752F)             // accent_green, darkened: 5.8:1 on a card, 5.2:1 on the page
    val SuccessDark = Color(0xFF81C995)
    val AllowedBgLight = Color(0xFFE4F1EA)
    val AllowedBgDark = Color(0xFF17301F)

    val WarnLight = Color(0xFF9E5A00)                // status_warn: 5.4:1 on a card, 4.8:1 on the page
    val WarnDark = Color(0xFFFBC02D)
}

/**
 * Selectable accent: Qatar maroon by default (the browser app's Qatar country
 * theme), plus its iOS Blue, Pink and Meadow themes. Each carries its hero
 * gradient and a tinted grouped backdrop for light and dark mode.
 */
enum class AccentPalette(
    val accentLight: Color,
    val accentDark: Color,
    val gradient: List<Color>,
    val groupedLight: Color = Palette.GroupedBackgroundLight,
    val fillLight: Color = Palette.FillLight,
    val groupedDark: Color = Palette.GroupedBackgroundDark,
) {
    /** Qatar flag maroon (Browse Safe's Qatar country theme), the default. */
    QATAR(
        accentLight = Color(0xFF8A1538),
        accentDark = Color(0xFFE08AA3),
        gradient = listOf(Color(0xFFB4345F), Color(0xFF8A1538), Color(0xFF5C0D26)),
        groupedLight = Color(0xFFF6EEF1),
        fillLight = Color(0x248A1538),
        groupedDark = Color(0xFF151012),
    ),
    BLUE(
        accentLight = Color(0xFF007AFF),
        accentDark = Color(0xFF0A84FF),
        gradient = listOf(Color(0xFF3B6FF6), Color(0xFF5B63F7), Color(0xFF7C4DFF)),
    ),
    PINK(
        accentLight = Color(0xFFE0338B),
        accentDark = Color(0xFFF48FB1),
        gradient = listOf(Color(0xFFFF8FB6), Color(0xFFE5588E), Color(0xFFC13C72)),
        groupedLight = Color(0xFFFBE4EE),
        fillLight = Color(0x24E0338B),
    ),
    MEADOW(
        accentLight = Color(0xFF1E8E5A),
        accentDark = Color(0xFF7FD1A3),
        gradient = listOf(Color(0xFF14B8A6), Color(0xFF12A57E), Color(0xFF1E8E5A)),
    ),
}

/** iOS system colours used for settings icon tiles. */
object IosColors {
    val Blue = Color(0xFF007AFF)
    val Green = Color(0xFF34C759)
    val Indigo = Color(0xFF5856D6)
    val Orange = Color(0xFFFF9500)
    val Pink = Color(0xFFFF375F)
    val Purple = Color(0xFFAF52DE)
    val Red = Color(0xFFFF3B30)
    val Teal = Color(0xFF30B0C7)
    val Cyan = Color(0xFF00A3A3)
    val Yellow = Color(0xFFFFCC00)
    val Gray = Color(0xFF8E8E93)
}
