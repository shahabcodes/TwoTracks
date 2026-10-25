package com.shahaabapps.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * The Parallel palette. Ink on a white page, separated by rules rather than
 * cards, and colour kept for the two people: the developer's track is blue and
 * the teacher's is orange. Everything else is ink, its secondary grey or a
 * status colour.
 *
 * Every pair the app draws is measured in [ContrastTest]; nothing here is by
 * eye.
 */
internal object Palette {
    // Light
    val PageLight = Color(0xFFFFFFFF)
    val CardLight = Color(0xFFFFFFFF)
    val SurfaceVariantLight = Color(0xFFF6F7F8)
    val LabelLight = Color(0xFF111214)                // ink
    val SecondaryLabelLight = Color(0xFF5A5E66)
    val SeparatorLight = Color(0xFFE6E7EA)            // hairline between rows
    val FillLight = Color(0x0F111214)                 // ink at 6%: fields, tracks, idle chips
    val AccentLight = Color(0xFF111214)               // solid buttons and chosen chips are ink
    val AccentTextLight = Color(0xFF2347D6)           // links and actions: 7.1:1 on the page
    val TechLight = Color(0xFF2347D6)                 // the developer's track
    val TeacherLight = Color(0xFFBB440A)              // the teacher's track: the prototype's C2470A
                                                      // read 4.43:1 on the fill, so a shade darker

    // Dark: the same page turned over, never pure black.
    val PageDark = Color(0xFF111214)
    val CardDark = Color(0xFF17181B)
    val SurfaceVariantDark = Color(0xFF1E2024)
    val LabelDark = Color(0xFFECEDEF)
    val SecondaryLabelDark = Color(0xFFA0A4AB)
    val SeparatorDark = Color(0xFF2C2E33)
    val FillDark = Color(0x1FECEDEF)
    val AccentDark = Color(0xFFECEDEF)
    val AccentTextDark = Color(0xFF8FA8FF)
    val TechDark = Color(0xFF8FA8FF)
    val TeacherDark = Color(0xFFFF9A5C)

    // Status
    val DestructiveLight = Color(0xFFB3241B)          // 6.6:1 on the page
    val DestructiveDark = Color(0xFFFF6B5E)
    val BlockedBgLight = Color(0xFFFDF1F0)
    val BlockedBgDark = Color(0xFF3A1D1A)

    val SuccessLight = Color(0xFF16752F)              // 5.8:1 on the page
    val SuccessDark = Color(0xFF81C995)
    val AllowedBgLight = Color(0xFFEEF6F0)
    val AllowedBgDark = Color(0xFF17301F)

    val WarnLight = Color(0xFF9E5A00)                 // 5.4:1 on the page
    val WarnDark = Color(0xFFFBC02D)

    // Diagrams. Four hues that must stay apart at a glance, because in a
    // diagram the colour carries the meaning: blue is somewhere data rests,
    // green something that runs your code, amber something in motion, violet
    // a way the story can go. They are their own colours and not the track or
    // status ones, which already mean a person or an outcome elsewhere in the
    // app; borrowing those would have the picture saying something it does
    // not mean. Each is drawn as a wash behind ordinary ink with a firmer rim,
    // so what matters is that the hues differ, not their contrast with text.
    val DataLight = Color(0xFF1B62C9)
    val DataDark = Color(0xFF7FB0FF)
    val ComputeLight = Color(0xFF13795B)
    val ComputeDark = Color(0xFF5FD0A8)
    val FlowLight = Color(0xFFA85A00)
    val FlowDark = Color(0xFFFFB053)
    val ChoiceLight = Color(0xFF7A3BB5)
    val ChoiceDark = Color(0xFFC79BF0)

    /** Ink for a solid fill; see [CupertinoColors.onSolid]. */
    val OnSolidLight = Color(0xFFFFFFFF)
    val OnSolidDark = Color(0xFF111214)

    /** The one gradient left: ink deepening, for the few drawn illustrations. */
    val Gradient = listOf(Color(0xFF3A3D44), Color(0xFF111214))
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

/**
 * The Departures palette: a departures board at night. One dark page, cards a
 * step lighter, a near-black for the flip tiles, and one signal colour, yellow,
 * for what is boarding now and for the developer's line. The teacher's line is
 * blue, and on time, delayed and cancelled read green, orange and red, as a
 * board does. There is no light version: the board is the look.
 */
internal object DeparturesPalette {
    val Page = Color(0xFF131417)
    val Card = Color(0xFF1C1E23)
    /** The flip tiles behind a time. */
    val Deep = Color(0xFF0B0C0E)
    val Label = Color(0xFFF2F2EE)
    val SecondaryLabel = Color(0xFFA3A7AF)
    val Separator = Color(0xFF2C2F36)
    /** The outline of an empty stop or an idle control. */
    val StrongBorder = Color(0xFF4A4E57)
    val Fill = Color(0x1AF2F2EE)
    val Signal = Color(0xFFF5C518)
    val OnSignal = Color(0xFF131417)
    val Teacher = Color(0xFF4FA3FF)
    val Success = Color(0xFF7CE38B)
    val Warn = Color(0xFFFF9F5A)
    val Destructive = Color(0xFFFF8A7F)
    val SuccessBg = Color(0xFF16261A)
    val DestructiveBg = Color(0xFF2A1715)
    val Gradient = listOf(Color(0xFF22252B), Color(0xFF0B0C0E))
}
