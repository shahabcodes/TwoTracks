package com.shahaabapps.twotracks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AppTheme

/**
 * The Parallel mark: the two tracks as two thin bars side by side, the
 * teacher's set a little lower, in each person's colour. Decorative, so it is
 * hidden from screen readers.
 */
@Composable
fun TwoTracksMark(modifier: Modifier = Modifier, height: Dp = 96.dp) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = modifier.clearAndSetSemantics {}) {
        Box(Modifier.width(4.dp).height(height).background(AppTheme.colors.tech))
        Box(Modifier.padding(top = height * 0.19f).width(4.dp).height(height).background(AppTheme.colors.teacher))
    }
}
