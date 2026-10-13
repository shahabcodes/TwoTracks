package com.shahaabapps.twotracks.ui.guide

import kotlin.math.roundToInt
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
 * explained, an outline round the hole, and a card saying what it is, set
 * beside the hole with a notch pointing at it.
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
        // Keep at it until the ring is actually up. Three tries over a third
        // of a second was not enough: navigating to Settings, composing it
        // and scrolling to a row took longer than that, and a reader tapping
        // at their own pace saw step after step with nothing ringed.
        LaunchedEffect(step) {
            var waited = 0
            while (!guide.revealed && waited < 4_000) {
                guide.revealCurrent()
                kotlinx.coroutines.delay(100)
                waited += 100
            }
        }
        val density = LocalDensity.current
        val pad = with(density) { 8.dp.toPx() }
        val target = guide.target
        // A step with nothing on screen to ring gets a hole of no size, which
        // draws as a plain scrim rather than a ring around the top-left corner.
        val hole = target?.let { Rect(it.left - pad, it.top - pad, it.right + pad, it.bottom + pad) } ?: Rect.Zero
        val animated by animateRectAsState(hole, spring(dampingRatio = 0.85f), label = "guideHole")
        val outline = with(density) { 2.dp.toPx() }
        val corner = with(density) { 14.dp.toPx() }

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
                        cornerRadius = CornerRadius(corner, corner),
                        blendMode = BlendMode.Clear,
                    )
                    // A thin outline, so the hole reads as the thing pointed at
                    // even where the screen behind it is as dark as the scrim.
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.85f),
                        topLeft = Offset(animated.left, animated.top),
                        size = Size(animated.width, animated.height),
                        cornerRadius = CornerRadius(corner, corner),
                        style = Stroke(width = outline),
                    )
                }
            }
            GuideCardLayout(guide = guide, step = step, hole = animated)
        }
    }
}

/**
 * Which side of the hole the card takes, where its top edge goes, and the
 * most height it may have there. [covers] is the one case it sits over the
 * ring: see [placeCard].
 */
internal data class CardPlacement(val below: Boolean, val top: Float, val room: Float, val covers: Boolean = false)

/**
 * Puts a card of [natural] height beside [hole] without touching it.
 *
 * The card goes a [gap] below the hole when it fits there, which is where a
 * thumb is; above it when only that side fits; and otherwise on the roomier
 * side, capped to that side's room, where its words scroll and its buttons
 * stay. [safeTop] and [safeBottom] are the edges inside the system bars and
 * the screen margin: the room is measured between those and the hole, so the
 * card can never be pushed back over the hole by an inset it was not told
 * about. With nothing ringed it sits at the foot of the screen.
 *
 * A ring so tall that neither side has [minRoom] would leave a card too small
 * to hold its Next button, which strands the reader, so then and only then
 * the card goes to the foot at a readable height over the ring. Nothing the
 * tour rings is that tall; the rule is there so it can never get stuck.
 */
internal fun placeCard(hole: Rect, natural: Float, safeTop: Float, safeBottom: Float, gap: Float, minRoom: Float = 0f): CardPlacement {
    if (hole.isEmpty) {
        val room = (safeBottom - safeTop).coerceAtLeast(0f)
        return CardPlacement(below = true, top = safeBottom - natural.coerceAtMost(room), room = room)
    }
    val roomBelow = (safeBottom - (hole.bottom + gap)).coerceAtLeast(0f)
    val roomAbove = ((hole.top - gap) - safeTop).coerceAtLeast(0f)
    if (maxOf(roomBelow, roomAbove) < minRoom && natural > maxOf(roomBelow, roomAbove)) {
        val room = minRoom.coerceAtMost(safeBottom - safeTop).coerceAtLeast(0f)
        return CardPlacement(below = true, top = safeBottom - minOf(natural, room), room = room, covers = true)
    }
    val below = when {
        natural <= roomBelow -> true
        natural <= roomAbove -> false
        else -> roomBelow >= roomAbove
    }
    val room = if (below) roomBelow else roomAbove
    val height = natural.coerceAtMost(room)
    return CardPlacement(below, top = if (below) hole.bottom + gap else hole.top - gap - height, room = room)
}

/**
 * Measures the card at its natural height, decides where it goes with
 * [placeCard], and draws it there with a notch pointing at the hole. The
 * natural height is handed to the tour too, so that when it scrolls a row
 * into view it leaves the card room beside it.
 */
@Composable
private fun GuideCardLayout(guide: GuideState, step: GuideStep, hole: Rect) {
    val density = LocalDensity.current
    val bars = WindowInsets.safeDrawing
    Layout(
        content = {
            GuideCard(guide, step)
            Notch()
        },
        modifier = Modifier.fillMaxSize(),
    ) { measurables, constraints ->
        val width = constraints.maxWidth
        val height = constraints.maxHeight
        val margin = Spacing.md.roundToPx()
        val gap = 14.dp.toPx()
        val cardWidth = (width - 2 * margin).coerceAtLeast(0)
        val natural = measurables[0].maxIntrinsicHeight(cardWidth).toFloat()
        guide.cardHeight = natural
        guide.cardGap = gap
        val safeTop = bars.getTop(density) + margin.toFloat()
        val safeBottom = height - bars.getBottom(density) - margin.toFloat()
        val place = placeCard(hole, natural, safeTop, safeBottom, gap, minRoom = 160.dp.toPx())
        val card = measurables[0].measure(Constraints(minWidth = cardWidth, maxWidth = cardWidth, maxHeight = place.room.roundToInt().coerceAtLeast(0)))
        val notch = measurables[1].measure(Constraints())
        val cardTop = if (hole.isEmpty || place.covers) (safeBottom - card.height).roundToInt() else if (place.below) place.top.roundToInt() else (hole.top - gap - card.height).roundToInt()
        val inset = 24.dp.roundToPx()
        val notchX = (hole.center.x - notch.width / 2f).roundToInt().coerceIn(margin + inset, (width - margin - inset - notch.width).coerceAtLeast(margin + inset))
        layout(width, height) {
            card.place(margin, cardTop)
            if (!hole.isEmpty && !place.covers) {
                if (place.below) notch.place(notchX, cardTop - notch.height) else notch.placeWithLayer(notchX, cardTop + card.height) { rotationZ = 180f }
            }
        }
    }
}

/** The notch on the card's edge, pointing up at the hole; turned over when the card is above it. */
@Composable
private fun Notch() {
    Canvas(Modifier.size(width = 18.dp, height = 9.dp)) {
        val path = Path().apply {
            moveTo(0f, size.height)
            lineTo(size.width / 2f, 0f)
            lineTo(size.width, size.height)
            close()
        }
        drawPath(path, GuideSurface)
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
 * The buttons are one row held at the foot, and only the words above them
 * scroll, so a card given little room says less rather than losing Next.
 */
@Composable
private fun GuideCard(guide: GuideState, step: GuideStep) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(GuideSurface)
            .padding(start = Spacing.lg, end = Spacing.lg, top = Spacing.md, bottom = Spacing.sm),
    ) {
        Column(
            modifier = Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Text("${guide.position} OF ${guide.total}", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.6f))
                Box(Modifier.weight(1f).height(3.dp).clip(RoundedCornerShape(2.dp)).background(Color.White.copy(alpha = 0.18f))) {
                    Box(Modifier.fillMaxWidth(guide.position.toFloat() / guide.total.coerceAtLeast(1)).height(3.dp).background(Color.White.copy(alpha = 0.7f)))
                }
            }
            Text(step.title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
            Text(step.body, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.82f))
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(top = Spacing.sm)) {
            TextButton(onClick = guide::stop) { Text("Skip", color = Color.White.copy(alpha = 0.75f)) }
            Spacer(Modifier.weight(1f))
            if (guide.position > 1) {
                TextButton(onClick = guide::back) { Text("Back", color = Color.White.copy(alpha = 0.85f)) }
            }
            Button(
                onClick = guide::next,
                modifier = Modifier.height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = GuideSurface),
            ) {
                Text(if (guide.isLast) "Done" else "Next", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
            }
        }
    }
}

/** Near-black, so the narrator is never mistaken for the app. */
private val GuideSurface = Color(0xFF1C1C1E)
