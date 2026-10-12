package com.shahaabapps.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * A status pill in the Parallel style: mono uppercase text in [color], on a
 * 6% tint of it, inside a 1.5dp outline of it. At 8% the teacher's orange
 * measured 4.47:1, under the body-text floor; ContrastTest holds the 6%.
 */
@Composable
fun StatusChip(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = color,
        maxLines = 1,
        modifier = modifier
            .heightIn(min = 28.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.06f))
            .border(1.5.dp, color, CircleShape)
            .wrapContentSize(Alignment.Center)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    )
}
