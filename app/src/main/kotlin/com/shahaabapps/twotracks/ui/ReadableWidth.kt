package com.shahaabapps.twotracks.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * How wide a column of prose is allowed to get.
 *
 * The app is one phone column, and handed a tablet it simply stretched: on a
 * 10-inch tablet held upright (800dp) a lesson ran about 130 characters to the
 * line, which is roughly twice a comfortable measure and makes the eye lose
 * its place on the way back. 720dp lands a lesson paragraph at 90 to 100
 * characters, which is wide enough that nothing already laid out for a phone —
 * a row of three answer buttons, a five-column table — has to be re-thought,
 * and narrow enough to read.
 */
val ReadableWidth: Dp = 720.dp

/**
 * The most page colour allowed to show either side of the column.
 *
 * The cap on its own was a rule about one orientation pretending to be a rule
 * about all of them. Upright, 800dp less the 720dp cap is 40dp either side,
 * which reads as a margin. Turned on its side the same tablet is 1280dp, and
 * the same cap leaves 280dp either side — a phone screen in a frame, with the
 * work pushed into the middle of a tablet that was turned precisely to get
 * more room. So the margin is what is held constant and the column widens past
 * the readable measure, rather than the gap growing without limit. 40dp is
 * chosen so that a tablet held upright is laid out exactly as it was.
 */
val MaxGutter: Dp = 40.dp

/**
 * Holds [content] to a readable column with no more than [MaxGutter] of page
 * colour either side, and centres what is left.
 *
 * This wraps the whole NavHost rather than each screen, so every screen gets
 * it without twenty-three edits. The screens that genuinely want the width —
 * the podcast board, the file viewer — opt out through [wideRoutes] in
 * MainNavGraph, which is why [wide] is a parameter rather than a constant: the
 * cap is the default and a screen says it does not want it, not the other way
 * round.
 */
@Composable
fun ReadableColumn(wide: Boolean = false, content: @Composable () -> Unit) {
    if (wide) {
        content()
        return
    }
    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        // Narrower than the measure and there is nothing to cap: a phone keeps
        // its whole screen, edge to edge, exactly as before.
        val column = maxOf(ReadableWidth, maxWidth - MaxGutter * 2).coerceAtMost(maxWidth)
        Box(modifier = Modifier.width(column).fillMaxSize()) { content() }
    }
}
