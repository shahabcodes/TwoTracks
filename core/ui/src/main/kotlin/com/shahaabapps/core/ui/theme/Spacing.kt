package com.shahaabapps.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/** Spacing and shape tokens tuned to the browser app's iOS metrics. */
object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 20.dp
    val xl = 32.dp

    /** Standard leading/trailing inset for grouped lists and cards. */
    val listHorizontal = 16.dp
    val rowMinHeight = 46.dp

    /** Divider inset so hairlines start at the label, past a 29dp icon tile (12 + 29 + 12). */
    val dividerInsetWithIcon = 53.dp
    val dividerInset = 16.dp

    /** Section header: 18dp above, 8dp below, 4dp extra start inset. */
    val sectionTop = 18.dp
    val sectionBottom = 8.dp
    val sectionStart = 4.dp
}

object Shapes {
    /** iOS settings card (bg_settings_card). */
    val card = RoundedCornerShape(12.dp)
    /** Content card with more presence (bg_card). */
    val contentCard = RoundedCornerShape(16.dp)
    /** Filled buttons. */
    val button = RoundedCornerShape(12.dp)
    /** Text fields and pills (bg_pill). */
    val field = RoundedCornerShape(10.dp)
    /** Segmented control track. */
    val segment = RoundedCornerShape(12.dp)
    /** Segmented control thumb and seg chips. */
    val segmentThumb = RoundedCornerShape(9.dp)
    /** Settings icon tile. */
    val iconTile = RoundedCornerShape(7.dp)
    /** Bottom sheets (top corners). */
    val sheet = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    /** Alert dialogs. */
    val dialog = RoundedCornerShape(14.dp)
    /** Hero banner bottom edge. */
    val hero = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
}
