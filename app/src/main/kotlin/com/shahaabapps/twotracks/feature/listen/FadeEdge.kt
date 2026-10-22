package com.shahaabapps.twotracks.feature.listen

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp

/**
 * Fades the last [height] of what this draws to nothing, whatever lies
 * behind it: the board scrolling under the pinned subtitles reads as going
 * on below rather than being cut off.
 */
internal fun Modifier.fadeBottom(height: Dp): Modifier = graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        val fade = (height.toPx() / size.height).coerceIn(0f, 1f)
        drawRect(Brush.verticalGradient(0f to Color.Black, (1f - fade) to Color.Black, 1f to Color.Transparent), blendMode = BlendMode.DstIn)
    }
