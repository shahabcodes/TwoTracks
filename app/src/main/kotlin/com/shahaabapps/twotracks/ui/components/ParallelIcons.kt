package com.shahaabapps.twotracks.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
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

/** Glyphs the Departures prototype drew, from its SVG. */
object BoardIcons {

    /** Today: a departures board, three lines of service on it. */
    val Board: ImageVector by lazy {
        ImageVector.Builder(name = "Board", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
            path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
                moveTo(5f, 5f); lineTo(19f, 5f); arcTo(2f, 2f, 0f, false, true, 21f, 7f); lineTo(21f, 17f)
                arcTo(2f, 2f, 0f, false, true, 19f, 19f); lineTo(5f, 19f); arcTo(2f, 2f, 0f, false, true, 3f, 17f)
                lineTo(3f, 7f); arcTo(2f, 2f, 0f, false, true, 5f, 5f); close()
                moveTo(7f, 10f); lineTo(11f, 10f)
                moveTo(7f, 14f); lineTo(14f, 14f)
                moveTo(15f, 10f); lineTo(17f, 10f)
            }
        }.build()
    }

    /** Route: two stations and the line between them. */
    val Route: ImageVector by lazy {
        ImageVector.Builder(name = "Route", defaultWidth = 24.dp, defaultHeight = 24.dp, viewportWidth = 24f, viewportHeight = 24f).apply {
            path(stroke = SolidColor(Color.Black), strokeLineWidth = 1.8f, strokeLineCap = StrokeCap.Round, strokeLineJoin = StrokeJoin.Round) {
                moveTo(8.2f, 18f); arcTo(2.2f, 2.2f, 0f, true, true, 3.8f, 18f); arcTo(2.2f, 2.2f, 0f, true, true, 8.2f, 18f); close()
                moveTo(20.2f, 6f); arcTo(2.2f, 2.2f, 0f, true, true, 15.8f, 6f); arcTo(2.2f, 2.2f, 0f, true, true, 20.2f, 6f); close()
                moveTo(8f, 18f); lineTo(13f, 18f); arcTo(3f, 3f, 0f, false, false, 16f, 15f); lineTo(16f, 8.2f)
            }
        }.build()
    }
}
