package com.shahaabapps.twotracks.ui.guide

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.Spacing

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
    // Dropped when the composable leaves, so a step cannot ring where a
    // control used to be on the screen before.
    DisposableEffect(key) { onDispose { guide.remove(key) } }
    return onGloballyPositioned { guide.put(key, it.boundsInWindow()) }
}

/**
 * Offers this scrolling screen to the tour, so a step can bring a row below
 * the fold into view before ringing it. Only for screens that scroll as one
 * piece; a lazy list has nothing composed down there to scroll to.
 */
/**
 * Offers a lazy screen to the tour, with the item each anchor lives in. The
 * tour scrolls to the item and the anchor reports its bounds once it has been
 * composed, which on a lazy list is the only order that works.
 */
@Composable
fun Modifier.guideLazyScrollHost(state: LazyListState, items: Map<String, Int>): Modifier {
    val guide = LocalGuide.current ?: return this
    DisposableEffect(state) { onDispose { guide.clearLazyHost(state) } }
    return onGloballyPositioned { guide.setLazyHost(state, items, it.boundsInWindow()) }
}

@Composable
fun Modifier.guideScrollHost(state: ScrollState): Modifier {
    val guide = LocalGuide.current ?: return this
    DisposableEffect(state) { onDispose { guide.clearScrollHost(state) } }
    return onGloballyPositioned { guide.setScrollHost(state, it.boundsInWindow()) }
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
        // Scroll the thing being explained into view first, then ring it.
        // A step that has just navigated arrives before the new screen has
        // been laid out and reported where anything is, so this waits a frame
        // or two rather than scrolling to a position nobody has measured yet.
        LaunchedEffect(step) {
            repeat(3) {
                guide.revealCurrent()
                kotlinx.coroutines.delay(120)
            }
        }
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

            GuideCard(guide = guide, step = step, hole = animated)
        }
    }
}

/**
 * The words.
 *
 * Deliberately not dressed like the app. Everything behind the scrim is dimmed
 * except the thing being ringed, which stays at full strength — so a card in
 * the app's own colours, carrying the app's own accent button, reads as one
 * more thing on the screen to press, and the ringed control and the Next
 * button compete. This one is near-black with a white button: plainly the
 * narrator, not the app.
 *
 * It takes whichever side of the ring has more room and is capped to that
 * room, so it cannot grow over the thing it is pointing at.
 */
@Composable
private fun BoxScope.GuideCard(guide: GuideState, step: GuideStep, hole: Rect) {
    val density = LocalDensity.current
    var boxHeight by remember { mutableFloatStateOf(0f) }
    val gap = with(density) { Spacing.md.toPx() }

    val above = if (hole.isEmpty) 0f else hole.top
    val below = if (hole.isEmpty || boxHeight == 0f) 0f else boxHeight - hole.bottom
    // With nothing ringed there is no thing to avoid, so it sits low, where a
    // thumb can reach it.
    val atTop = !hole.isEmpty && above > below
    val room = if (hole.isEmpty || boxHeight == 0f) boxHeight else if (atTop) above else below
    val maxHeight = with(density) { (room - gap).coerceAtLeast(0f).toDp() }

    Column(
        modifier = Modifier
            .align(if (atTop) Alignment.TopCenter else Alignment.BottomCenter)
            .onGloballyPositioned { boxHeight = it.parentLayoutCoordinates?.size?.height?.toFloat() ?: boxHeight }
            .systemBarsPadding()
            .padding(Spacing.md)
            .fillMaxWidth()
            .heightIn(max = if (maxHeight > 0.dp) maxHeight else 420.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(GuideSurface)
            .verticalScroll(rememberScrollState())
            .padding(Spacing.lg),
        verticalArrangement = Arrangement.spacedBy(Spacing.xs),
    ) {
        Text(
            "STEP ${guide.position} OF ${guide.total}",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.6f),
        )
        Text(
            step.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
        )
        Text(step.body, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.82f))
        Spacer(Modifier.height(Spacing.sm))
        Button(
            onClick = guide::next,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = GuideSurface),
        ) {
            Text(
                if (guide.isLast) "Done" else "Next",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Spacing.md), modifier = Modifier.fillMaxWidth()) {
            if (guide.position > 1) {
                TextButton(onClick = guide::back) { Text("Back", color = Color.White.copy(alpha = 0.8f)) }
            }
            TextButton(onClick = guide::stop) { Text("Skip", color = Color.White.copy(alpha = 0.8f)) }
        }
    }
}

/** Near-black, so the narrator is never mistaken for the app. */
private val GuideSurface = Color(0xFF1C1C1E)
