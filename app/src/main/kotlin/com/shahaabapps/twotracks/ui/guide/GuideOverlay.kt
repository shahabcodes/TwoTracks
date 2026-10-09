package com.shahaabapps.twotracks.ui.guide

import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.animation.AnimatedVisibility
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.Chip

/**
 * Reports this composable's position to the tour, so a step can ring it.
 *
 * Bounds are taken in window space because the ring is drawn by an overlay
 * that sits above the whole scaffold, including the tab bar, and the two have
 * no coordinate space in common otherwise.
 */
@Composable
fun Modifier.guideAnchor(key: String): Modifier {
    val guide = LocalGuide.current ?: return this
    return onGloballyPositioned { guide.put(key, it.boundsInWindow()) }
}

/**
 * The tour drawn over everything: a scrim with a hole over the thing being
 * explained, and a card saying what it is.
 *
 * Taps anywhere on the scrim are swallowed. The point of a step is that one
 * thing is being explained, and letting a stray tap land on the screen behind
 * would move the app out from under the words.
 */
@Composable
fun GuideOverlay(guide: GuideState) {
    AnimatedVisibility(visible = guide.running, enter = fadeIn(), exit = fadeOut()) {
        val step = guide.step ?: return@AnimatedVisibility
        val density = LocalDensity.current
        val pad = with(density) { 8.dp.toPx() }
        val target = guide.target
        // A step with nothing on screen to ring gets a hole of no size, which
        // draws as a plain scrim rather than a ring around the top-left corner.
        val hole = target?.let { Rect(it.left - pad, it.top - pad, it.right + pad, it.bottom + pad) } ?: Rect.Zero
        val animated by animateRectAsState(hole, spring(dampingRatio = 0.85f), label = "guideHole")

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
        ) {
            // alpha just under 1 forces an offscreen layer, which is what lets
            // BlendMode.Clear cut the hole instead of painting black.
            Canvas(modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.995f)) {
                drawRect(Color.Black.copy(alpha = 0.72f))
                if (!animated.isEmpty) {
                    drawRoundRect(
                        color = Color.Transparent,
                        topLeft = Offset(animated.left, animated.top),
                        size = Size(animated.width, animated.height),
                        cornerRadius = CornerRadius(18f, 18f),
                        blendMode = BlendMode.Clear,
                    )
                }
            }

            GuideCard(guide = guide, step = step, holeBottom = animated.bottom)
        }
    }
}

/**
 * The words. It sits at the bottom unless the hole is down there, in which
 * case it moves to the top, because a card covering the thing it is pointing
 * at explains nothing.
 */
@Composable
private fun GuideCard(
    guide: GuideState,
    step: GuideStep,
    holeBottom: Float,
) {
    val density = LocalDensity.current
    val screenBottomThird = with(density) { 420.dp.toPx() }
    val atTop = holeBottom > 0f && holeBottom > screenBottomThird
    Box(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        Column(
            modifier = Modifier
                .align(if (atTop) Alignment.TopCenter else Alignment.BottomCenter)
                .padding(Spacing.lg)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(AppTheme.colors.card)
                .padding(Spacing.lg),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                "STEP ${guide.position} OF ${guide.total}",
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.colors.secondaryLabel,
            )
            Text(
                step.title,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            )
            Text(step.body, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
            Spacer(Modifier.height(Spacing.sm))
            CupertinoPrimaryButton(
                text = if (guide.isLast) "Done" else "Next",
                onClick = guide::next,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm), modifier = Modifier.fillMaxWidth()) {
                if (guide.position > 1) {
                    Chip(text = "Back", onClick = guide::back)
                }
                Chip(text = "Skip the tour", onClick = { guide.stop() })
            }
        }
    }
}
