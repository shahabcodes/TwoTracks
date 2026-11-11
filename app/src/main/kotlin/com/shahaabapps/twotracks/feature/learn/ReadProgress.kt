package com.shahaabapps.twotracks.feature.learn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.layout
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
    // Pitch from the measured items' own tops, which quietly includes the
    // gap between them; with one item on screen there is no pitch to take,
    // so that item's height stands in for it.
    val pitch = if (last.index > first.index) {
        (last.offset - first.offset).toFloat() / (last.index - first.index)
    } else {
        first.size.toFloat()
    }
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

/** How long the readout stays after the scroll stops. */
internal const val PROGRESS_LINGER_MS = 1_400L

/**
 * The percentage read, shown while the lesson is being scrolled.
 *
 * It comes while the thumb is moving and goes a moment after it stops,
 * because that is the only time the question is being asked. A permanent
 * badge over the text would be one more thing on the page for every minute
 * of reading, to answer something wanted in the second after a long swipe:
 * how much of this is left.
 *
 * A lesson that fits on one screen never shows it at all. "100%" over a page
 * that was never scrolled reads as an achievement when nothing has happened.
 */
@Composable
internal fun BoxScope.ReadProgress(listState: LazyListState) {
    val percent by remember(listState) {
        derivedStateOf {
            val info = listState.layoutInfo
            readPercent(
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
        modifier = Modifier.align(Alignment.TopCenter),
    ) {
        ReadProgressPill(percent)
    }
}

/** The readout itself: the number, and a track filled to it. */
@Composable
private fun ReadProgressPill(percent: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
        modifier = Modifier
            .padding(top = Spacing.sm)
            .clip(RoundedCornerShape(20.dp))
            .background(AppTheme.colors.ink.copy(alpha = 0.82f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
            // One label for the whole thing: a screen reader has no use for
            // a bar it cannot see, and "42" on its own means nothing.
            .clearAndSetSemantics { contentDescription = "$percent percent of this topic read" },
    ) {
        Text(
            "$percent%",
            style = MaterialTheme.typography.labelMedium.copy(
                fontFamily = MonoFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = AppTheme.colors.groupedBackground,
        )
        Box(
            modifier = Modifier
                .width(44.dp)
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(AppTheme.colors.groupedBackground.copy(alpha = 0.3f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .layout { measurable, constraints ->
                        // Measured against the whole track and then cut to
                        // the fraction, so the fill and the track agree at
                        // every width and every text scale.
                        val placeable = measurable.measure(constraints)
                        val width = Math.round(placeable.width * percent.coerceIn(0, 100) / 100f)
                        layout(width, placeable.height) { placeable.place(0, 0) }
                    }
                    .background(AppTheme.colors.groupedBackground.copy(alpha = 0.9f)),
            )
        }
    }
}
