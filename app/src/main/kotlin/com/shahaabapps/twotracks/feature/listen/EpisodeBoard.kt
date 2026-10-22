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
internal fun EpisodeBoard(
    board: PodcastBoard,
    line: Int?,
    playing: Boolean,
    modifier: Modifier = Modifier,
    palette: BoardPalette = PlayerPalette,
    heading: @Composable (String) -> Unit = { Text(it.uppercase(), color = palette.soft, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.4.sp) },
) {
    val frame = board.frameAt(line)
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // The board travels with its scene: switching telling hands this a new
        // board while the old scene is still fading out, and that scene must be
        // drawn from its own board. Looked up in the new one by number, a scene
        // past the new board's end crashed the app.
        AnimatedContent(
            targetState = board to frame.sceneIndex, label = "scene",
            transitionSpec = { fadeIn(tween(450)) togetherWith fadeOut(tween(300)) },
            modifier = Modifier.fillMaxWidth(),
        ) { (ownBoard, index) ->
            val scene = ownBoard.scenes[index]
            // Only the scene on screen follows the line; one fading out keeps
            // the state it left with.
            val here = if (ownBoard === board && index == frame.sceneIndex) frame else ownBoard.frameAt(scene.fromLine)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                heading(scene.heading)
                SceneDiagram(scene, here.shown, here.lit, here.pulse.takeIf { playing }, palette.diagram)
            }
        }
        Box(Modifier.fillMaxWidth().heightIn(min = 76.dp)) {
            AnimatedContent(
                targetState = frame.card, label = "card",
                transitionSpec = { (fadeIn(tween(350)) + slideInVertically(tween(350)) { it / 3 }) togetherWith fadeOut(tween(200)) },
            ) { card -> if (card != null) Card(card, palette) }
        }
    }
}

@Composable
private fun SceneDiagram(scene: Scene, shown: Set<String>, lit: Set<String>, pulse: Pair<String, String>?, colors: DiagramColors) {
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
        scene.diagram, colors,
        modifier = Modifier.semantics { contentDescription = scene.diagram.caption },
        reveal = { reveal[it]?.value ?: 1f },
        emphasis = { emphasis[it]?.value ?: 1f },
        pulse = pulse, pulseAt = t,
    )
}

/** A term and its meaning, or a line of code set in a monospace face. */
@Composable
private fun Card(card: BoardCard, palette: BoardPalette) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(palette.card).padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        if (card.phrase) {
            // A German phrase: the German is the thing to read and say.
            Text(card.title, color = palette.ink, fontSize = 22.sp, lineHeight = 27.sp, fontWeight = FontWeight.SemiBold)
            Text(card.body, color = palette.soft, fontSize = 15.sp, lineHeight = 20.sp)
            return@Column
        }
        Text(card.title, color = palette.accent, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text(
            card.body, color = palette.ink, fontSize = if (card.code) 14.sp else 16.sp, lineHeight = if (card.code) 19.sp else 21.sp,
            fontWeight = if (card.code) FontWeight.Normal else FontWeight.Medium,
            fontFamily = if (card.code) FontFamily.Monospace else FontFamily.Default,
        )
    }
}

/** How far a node the hosts are not talking about fades back. */
private const val DIMMED = 0.5f

/**
 * The colours a board is drawn in: [ink] for what is read, [soft] for what is
 * under it, [accent] for a card's title, [card] behind a card, and the
 * diagram's own. The Parallel player has its field of blues; the Departures
 * player draws the board in the look's own colours.
 */
internal data class BoardPalette(val ink: Color, val soft: Color, val accent: Color, val card: Color, val diagram: DiagramColors)

// The player's own field: white ink, and a light blue for the solid shapes so
// dark text on them is about 9:1. The plate is the field's middle blue, which
// is what an edge's label sits on.
private val Accent = Color(0xFF8CC8FF)
internal val PlayerPalette = BoardPalette(
    ink = Color.White,
    soft = Color.White.copy(alpha = 0.72f),
    accent = Accent,
    card = Color.White.copy(alpha = 0.1f),
    diagram = DiagramColors(
        accent = Accent,
        plate = Color(0xFF0A3274),
        fill = Color.White.copy(alpha = 0.08f),
        secondary = Color.White.copy(alpha = 0.7f),
        ink = Color.White,
        onSolid = Color(0xFF081E52),
    ),
)
