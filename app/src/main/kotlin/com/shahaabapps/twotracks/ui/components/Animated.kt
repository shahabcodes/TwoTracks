package com.shahaabapps.twotracks.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AppTheme

/** Scales and fades content in when it first appears (used for Done chips, ticks, badges). */
@Composable
fun PopIn(visible: Boolean = true, content: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(initialScale = 0.5f, animationSpec = spring(dampingRatio = 0.55f, stiffness = 500f)) + fadeIn(tween(150)),
        exit = scaleOut(targetScale = 0.6f, animationSpec = tween(150)) + fadeOut(tween(150)),
    ) { content() }
}

/** Swaps between two states with a bouncy scale, e.g. an empty box and a tick. */
@Composable
fun <T> AnimatedSwap(state: T, content: @Composable (T) -> Unit) {
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            (scaleIn(initialScale = 0.4f, animationSpec = spring(dampingRatio = 0.5f, stiffness = 600f)) + fadeIn(tween(120)))
                .togetherWith(scaleOut(targetScale = 0.4f, animationSpec = tween(120)) + fadeOut(tween(120)))
        },
        label = "swap",
    ) { content(it) }
}

/** A number that rolls up or down when it changes. */
@Composable
fun AnimatedNumber(value: Int, modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.onSurface, style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.titleMedium) {
    AnimatedContent(
        targetState = value,
        transitionSpec = {
            if (targetState > initialState) {
                (slideInVertically { it } + fadeIn()).togetherWith(slideOutVertically { -it } + fadeOut())
            } else {
                (slideInVertically { -it } + fadeIn()).togetherWith(slideOutVertically { it } + fadeOut())
            }
        },
        label = "number",
    ) { Text("$it", color = color, style = style, fontWeight = FontWeight.SemiBold, modifier = modifier) }
}

/** A gentle breathing scale for the "current" marker on the ladder. */
@Composable
fun Modifier.pulse(enabled: Boolean = true): Modifier {
    if (!enabled) return this
    val transition = rememberInfiniteTransition(label = "pulse")
    val scale by transition.animateFloat(
        initialValue = 1f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "pulseScale",
    )
    return this.scale(scale)
}

/**
 * iOS-style swipe to delete: swipe left reveals a red "Delete" tray, releasing
 * past the threshold calls [onDelete]. Wrap each list row.
 */
@Composable
fun SwipeToDelete(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    content: @Composable () -> Unit,
) {
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) { onDelete(); true } else false
        },
        positionalThreshold = { distance -> distance * 0.4f },
    )
    val destructive = AppTheme.colors.destructive
    SwipeToDismissBox(
        state = state,
        modifier = modifier.clip(shape),
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val active = state.targetValue == SwipeToDismissBoxValue.EndToStart
            Box(
                modifier = Modifier.fillMaxSize().background(if (active) destructive else destructive.copy(alpha = 0.7f)).padding(end = 20.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                val iconScale = remember(active) { if (active) 1.15f else 1f }
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = AppTheme.colors.onSolid, modifier = Modifier.scale(iconScale))
                }
            }
        },
        content = { content() },
    )
}
