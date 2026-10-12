package com.shahaabapps.twotracks.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/** Glyphs the prototype drew that Material has no match for. */
object ParallelIcons {

    /** Today: the two tracks as rails, each with its marker at a different height. */
    val Tracks: ImageVector by lazy {
        ImageVector.Builder(name = "Tracks", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
            path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round) {
                moveTo(8f, 3f); lineTo(8f, 21f)
                moveTo(16f, 3f); lineTo(16f, 21f)
            }
            path(fill = SolidColor(Color.Black)) {
                moveTo(10.2f, 9f); arcTo(2.2f, 2.2f, 0f, true, true, 5.8f, 9f); arcTo(2.2f, 2.2f, 0f, true, true, 10.2f, 9f); close()
                moveTo(18.2f, 15f); arcTo(2.2f, 2.2f, 0f, true, true, 13.8f, 15f); arcTo(2.2f, 2.2f, 0f, true, true, 18.2f, 15f); close()
            }
        }.build()
    }
}
