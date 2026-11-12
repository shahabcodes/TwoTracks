package com.shahaabapps.twotracks.feature.learn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import kotlinx.coroutines.delay

/**
 * One item of the lesson as it was measured: which item it is, where its top
 * is in the viewport's own coordinates, and how tall it came out.
 *
 * The same three numbers Compose reports for a visible item, named so the
 * arithmetic below can be tested without a laid-out list.
 */
internal data class Seen(val index: Int, val offset: Int, val size: Int)

/**
 * How far down a lesson the reader has got, 0 to 1.
 *
 * A lesson is a list of blocks of wildly different heights — a title, a
 * table, a paragraph, a quick check — and only the handful on screen have
 * ever been measured. So this cannot be a true fraction of the whole text;
 * it is an estimate, and the honest way to make one is to take the pitch of
 * the items that have been measured and assume the rest are like them. That
 * is right to within a paragraph in the middle of a lesson, and the two
 * places where being wrong would be noticed are both exact: nothing
 * scrolled is nothing read, and the last block fully on screen is the end
 * of the lesson.
 *
 * It answers where the reader is, not the furthest they have ever been. A
 * number that stayed at 80% while you scrolled back to re-read the middle
 * would not be telling you anything about the screen in front of you.
 */
internal fun readFraction(totalItems: Int, visible: List<Seen>, viewportStart: Int, viewportEnd: Int): Float {
    if (totalItems <= 0 || visible.isEmpty()) return 0f
    val viewport = viewportEnd - viewportStart
    if (viewport <= 0) return 0f
    val first = visible.first()
    val last = visible.last()
    // The foot of the lesson is the one point worth being exact about: the
    // last block is fully on screen, so there is nothing below it, whatever
    // the estimate would otherwise say.
    if (last.index >= totalItems - 1 && last.offset + last.size <= viewportEnd) return 1f
    val pitch = pitchOf(first, last)
    if (pitch <= 0f) return 0f
    val scrollable = pitch * totalItems - viewport
    // Short enough to fit: it is all in front of the reader at once.
    if (scrollable <= 0f) return 1f
    val scrolled = pitch * first.index + (viewportStart - first.offset)
    return (scrolled / scrollable).coerceIn(0f, 1f)
}

/** [readFraction] as a percentage, which is the only form the screen shows. */
internal fun readPercent(totalItems: Int, visible: List<Seen>, viewportStart: Int, viewportEnd: Int): Int =
    Math.round(readFraction(totalItems, visible, viewportStart, viewportEnd) * 100f)

/**
 * Pitch from the measured items' own tops, which quietly includes the gap
 * between them. With one item on screen there is no pitch to take, so that
 * item's own height stands in for it.
 */
private fun pitchOf(first: Seen, last: Seen): Float =
    if (last.index > first.index) {
        (last.offset - first.offset).toFloat() / (last.index - first.index)
    } else {
        first.size.toFloat()
    }

/**
 * How much of the whole lesson one screenful is, 0 to 1.
 *
 * This is what sets the length of the thumb, and so it is the part of the
 * rail that says how long the lesson is: a short topic gets a thumb filling
 * most of the rail, a long one a stub. Same estimate as [readFraction] and
 * the same caveat — the unmeasured blocks are assumed to be like the
 * measured ones — and it answers 1 whenever there is nothing to scroll,
 * which is the honest answer for a lesson that is all on screen.
 */
internal fun viewportShare(totalItems: Int, visible: List<Seen>, viewportStart: Int, viewportEnd: Int): Float {
    if (totalItems <= 0 || visible.isEmpty()) return 1f
    val viewport = (viewportEnd - viewportStart).toFloat()
    if (viewport <= 0f) return 1f
    val pitch = pitchOf(visible.first(), visible.last())
    if (pitch <= 0f) return 1f
    val content = pitch * totalItems
    return if (content <= viewport) 1f else (viewport / content).coerceIn(0f, 1f)
}

/** Where the thumb sits on the rail and how long it is, both in pixels. */
internal data class Rail(val thumbTop: Float, val thumbLength: Float)

/**
 * The rail's geometry.
 *
 * The thumb is as long a part of the rail as the screen is of the lesson,
 * and it travels the rail as the reader travels the lesson — so its top edge
 * is nowhere near the read fraction, and its *bottom* edge is: at 100% the
 * thumb is resting on the foot of the rail, with the whole rail above it
 * filled. That is what makes one shape say both things at once, where the
 * reader is and how much is behind them.
 *
 * A thumb is never shorter than [minThumb], because a forty-screen lesson
 * would otherwise give it a couple of pixels and nothing to grab the eye.
 */
internal fun railFor(railLength: Float, share: Float, read: Float, minThumb: Float): Rail {
    if (railLength <= 0f) return Rail(0f, 0f)
    val wanted = railLength * share.coerceIn(0f, 1f)
    val length = wanted.coerceAtLeast(minThumb.coerceAtMost(railLength)).coerceAtMost(railLength)
    return Rail(thumbTop = (railLength - length) * read.coerceIn(0f, 1f), thumbLength = length)
}

/** How long the rail stays after the scroll stops. */
internal const val PROGRESS_LINGER_MS = 1_400L

/** The width of the rail, and the least of it the thumb may be. */
private val RAIL_WIDTH = 5.dp
private val RAIL_MIN_THUMB = 32.dp

/**
 * The scroll rail: where in the lesson this screen is, and how much of the
 * lesson is behind it.
 *
 * It comes while the thumb is moving and goes a moment after it stops,
 * because that is the only time the question is being asked. A rail standing
 * permanently down the edge of every lesson would be one more thing on the
 * page for every minute of reading, to answer something wanted in the second
 * after a long swipe: how much of this is left.
 *
 * A lesson that fits on one screen never shows it at all. A full rail over a
 * page that was never scrolled reads as an achievement when nothing has
 * happened.
 */
@Composable
internal fun BoxScope.ReadProgress(listState: LazyListState) {
    val read by remember(listState) {
        derivedStateOf {
            val info = listState.layoutInfo
            readFraction(
                totalItems = info.totalItemsCount,
                visible = info.visibleItemsInfo.map { Seen(it.index, it.offset, it.size) },
                viewportStart = info.viewportStartOffset,
                viewportEnd = info.viewportEndOffset,
            )
        }
    }
    val share by remember(listState) {
        derivedStateOf {
            val info = listState.layoutInfo
            viewportShare(
                totalItems = info.totalItemsCount,
                visible = info.visibleItemsInfo.map { Seen(it.index, it.offset, it.size) },
                viewportStart = info.viewportStartOffset,
                viewportEnd = info.viewportEndOffset,
            )
        }
    }
    val scrollable by remember(listState) {
        derivedStateOf { listState.canScrollForward || listState.canScrollBackward }
    }
    var showing by remember { mutableStateOf(false) }
    LaunchedEffect(listState.isScrollInProgress, scrollable) {
        if (!scrollable) {
            showing = false
        } else if (listState.isScrollInProgress) {
            showing = true
        } else if (showing) {
            delay(PROGRESS_LINGER_MS)
            showing = false
        }
    }
    AnimatedVisibility(
        visible = showing,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier.align(Alignment.CenterEnd),
    ) {
        ReadRail(read = read, share = share)
    }
}

/**
 * The rail itself: a track down the edge, the part of the lesson already
 * behind the reader filled in, the thumb, and the percentage riding beside
 * it.
 */
@Composable
private fun ReadRail(read: Float, share: Float) {
    val ink = AppTheme.colors.ink
    // The thumb is where the finger put it, so it is never animated; the
    // fill behind it is, which is what turns a jumped-to position into a
    // visible amount read.
    val filled by animateFloatAsState(targetValue = read, animationSpec = tween(220), label = "read")
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = Spacing.lg, horizontal = 6.dp),
    ) {
        val density = LocalDensity.current
        val railPx = constraints.maxHeight.toFloat()
        val minThumb = with(density) { RAIL_MIN_THUMB.toPx() }
        val rail = railFor(railPx, share, read, minThumb)
        val shown = railFor(railPx, share, filled, minThumb)
        val percent = Math.round(read.coerceIn(0f, 1f) * 100f)
        val thumbTop = with(density) { rail.thumbTop.toDp() }
        val thumbLength = with(density) { rail.thumbLength.toDp() }
        // Everything above the thumb's foot is read, which is the whole rail
        // once the thumb is resting on the bottom of it.
        val readTo = with(density) { (shown.thumbTop + shown.thumbLength).toDp() }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .width(RAIL_WIDTH)
                .fillMaxHeight()
                .clip(RoundedCornerShape(RAIL_WIDTH / 2))
                .background(ink.copy(alpha = 0.10f))
                // One label for the whole thing: a screen reader has no use
                // for a bar it cannot see, and "42" on its own means nothing.
                .clearAndSetSemantics { contentDescription = "$percent percent of this topic read" },
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .width(RAIL_WIDTH)
                .height(readTo)
                .clip(RoundedCornerShape(RAIL_WIDTH / 2))
                .background(ink.copy(alpha = 0.42f)),
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = thumbTop)
                .width(RAIL_WIDTH)
                .height(thumbLength)
                .clip(RoundedCornerShape(RAIL_WIDTH / 2))
                .background(ink.copy(alpha = 0.92f)),
        )
        // The number rides with the thumb rather than sitting in a corner,
        // so the two are read as one thing. It is kept on the rail at both
        // ends, where the thumb runs out of room before the label does.
        val labelHeight = with(density) { 22.dp.toPx() }
        val labelTop = (rail.thumbTop + rail.thumbLength / 2f - labelHeight / 2f)
            .coerceIn(0f, (railPx - labelHeight).coerceAtLeast(0f))
        Text(
            "$percent%",
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = MonoFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = AppTheme.colors.groupedBackground,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = with(density) { labelTop.toDp() }, x = -(RAIL_WIDTH + 6.dp))
                .clip(RoundedCornerShape(11.dp))
                .background(ink.copy(alpha = 0.82f))
                .padding(horizontal = 7.dp, vertical = 3.dp)
                .clearAndSetSemantics { },
        )
    }
}
