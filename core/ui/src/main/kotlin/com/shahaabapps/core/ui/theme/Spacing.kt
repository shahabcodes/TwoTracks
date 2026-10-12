package com.shahaabapps.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/** Spacing tokens from the Parallel prototype: a 24dp gutter and 56dp rows. */
object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 20.dp
    val xl = 32.dp

    /** The page gutter. */
    val listHorizontal = 24.dp
    val rowMinHeight = 56.dp

    /** Hairlines run the full width of a section; nothing is inset. */
    val dividerInsetWithIcon = 0.dp
    val dividerInset = 0.dp

    /** Section header: 22dp above, 6dp below its label, flush with the gutter. */
    val sectionTop = 22.dp
    val sectionBottom = 6.dp
    val sectionStart = 0.dp

    /** The ink rule that opens a section, and the hairline between its rows. */
    val rule = 1.5.dp
    val hairline = 1.dp
}

object Shapes {
    /** A boxed list, where one is still wanted. */
    val card = RoundedCornerShape(16.dp)
    /** An outlined content card. */
    val contentCard = RoundedCornerShape(16.dp)
    /** Filled and outlined buttons. */
    val button = RoundedCornerShape(12.dp)
    /** Text fields and pills. */
    val field = RoundedCornerShape(12.dp)
    /** Segmented control track. */
    val segment = RoundedCornerShape(10.dp)
    /** Segmented control thumb and chips. */
    val segmentThumb = RoundedCornerShape(8.dp)
    /** Settings icon tile. */
    val iconTile = RoundedCornerShape(7.dp)
    /** Bottom sheets (top corners). */
    val sheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    /** Alert dialogs. */
    val dialog = RoundedCornerShape(20.dp)
    /** The page header is square: Parallel has no banner edge. */
    val hero = RoundedCornerShape(0.dp)
}
