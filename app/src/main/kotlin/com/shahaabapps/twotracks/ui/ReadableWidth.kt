package com.shahaabapps.twotracks.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
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
 * Stops [content] widening past [ReadableWidth] and centres what is left, so
 * the page colour shows either side.
 *
 * This wraps the whole NavHost rather than each screen, so every screen gets
 * it without twenty-three edits. The screens that genuinely want the width —
 * the podcast board, the week grids — opt out through [wideRoutes] in
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
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Box(modifier = Modifier.widthIn(max = ReadableWidth).fillMaxSize()) { content() }
    }
}
