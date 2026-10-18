package com.shahaabapps.twotracks.feature.listen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.content.BoardCard
import com.shahaabapps.core.content.PodcastBoard
import com.shahaabapps.core.content.Scene
import com.shahaabapps.twotracks.feature.learn.DiagramCanvas
import com.shahaabapps.twotracks.feature.learn.DiagramColors

/**
 * The episode's board, in the cover's place: the scene's heading, its
 * diagram building up as the hosts reach each part, and a card for the term
 * or the line of code just said.
 *
 * It is driven by the line being spoken and nothing else, so seeking, the
 * transcript and the speed all move it for free. Each scene fades into the
 * next; inside a scene nodes fade and grow in as their beat arrives, what is
 * not being talked about dims, and a pulse runs along an edge while its line
 * plays. Paused, the pulse rests and the board holds.
 */
@Composable
internal fun EpisodeBoard(board: PodcastBoard, line: Int?, playing: Boolean, modifier: Modifier = Modifier) {
    val frame = board.frameAt(line)
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        AnimatedContent(
            targetState = frame.sceneIndex, label = "scene",
            transitionSpec = { fadeIn(tween(450)) togetherWith fadeOut(tween(300)) },
            modifier = Modifier.fillMaxWidth(),
        ) { index ->
            val scene = board.scenes[index]
            // Only the scene on screen follows the line; one fading out keeps
            // the state it left with.
            val here = if (index == frame.sceneIndex) frame else board.frameAt(scene.fromLine)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    scene.heading.uppercase(), color = Soft, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.4.sp,
                )
                SceneDiagram(scene, here.shown, here.lit, here.pulse.takeIf { playing })
            }
        }
        Box(Modifier.fillMaxWidth().heightIn(min = 76.dp)) {
            AnimatedContent(
                targetState = frame.card, label = "card",
                transitionSpec = { (fadeIn(tween(350)) + slideInVertically(tween(350)) { it / 3 }) togetherWith fadeOut(tween(200)) },
            ) { card -> if (card != null) Card(card) }
        }
    }
}

@Composable
private fun SceneDiagram(scene: Scene, shown: Set<String>, lit: Set<String>, pulse: Pair<String, String>?) {
    val ids = scene.diagram.nodes.map { it.id }
    // One animation per node, in the diagram's own order so the calls line up
    // from one frame to the next.
    val reveal: Map<String, State<Float>> = ids.associateWith { id ->
        animateFloatAsState(if (id in shown) 1f else 0f, tween(520), label = "reveal $id")
    }
    val emphasis: Map<String, State<Float>> = ids.associateWith { id ->
        animateFloatAsState(if (lit.isEmpty() || id in lit) 1f else DIMMED, tween(400), label = "light $id")
    }
    val travel = rememberInfiniteTransition(label = "pulse")
    val t by travel.animateFloat(0f, 1f, infiniteRepeatable(tween(1300, easing = LinearEasing), RepeatMode.Restart), label = "pulse at")
    DiagramCanvas(
        scene.diagram, BoardColors,
        modifier = Modifier.semantics { contentDescription = scene.diagram.caption },
        reveal = { reveal[it]?.value ?: 1f },
        emphasis = { emphasis[it]?.value ?: 1f },
        pulse = pulse, pulseAt = t,
    )
}

/** A term and its meaning, or a line of code set in a monospace face. */
@Composable
private fun Card(card: BoardCard) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.1f)).padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(card.title, color = Accent, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text(
            card.body, color = Ink, fontSize = if (card.code) 14.sp else 16.sp, lineHeight = if (card.code) 19.sp else 21.sp,
            fontWeight = if (card.code) FontWeight.Normal else FontWeight.Medium,
            fontFamily = if (card.code) FontFamily.Monospace else FontFamily.Default,
        )
    }
}

/** How far a node the hosts are not talking about fades back. */
private const val DIMMED = 0.5f

// The player's own field: white ink, and a light blue for the solid shapes so
// dark text on them is about 9:1. The plate is the field's middle blue, which
// is what an edge's label sits on.
private val Ink = Color.White
private val Soft = Color.White.copy(alpha = 0.72f)
private val Accent = Color(0xFF8CC8FF)
private val BoardColors = DiagramColors(
    accent = Accent,
    plate = Color(0xFF0A3274),
    fill = Color.White.copy(alpha = 0.08f),
    secondary = Color.White.copy(alpha = 0.7f),
    ink = Ink,
    onSolid = Color(0xFF081E52),
)
