package com.shahaabapps.twotracks.feature.learn

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.content.DEdge
import com.shahaabapps.core.content.DHue
import com.shahaabapps.core.content.DNode
import com.shahaabapps.core.content.DiagramSpec
import com.shahaabapps.core.content.NodeKind
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Draws a [DiagramSpec] natively: nodes on a unit grid, edges with arrow
 * heads and optional labels, all in theme colours so it works in dark mode.
 */
@Composable
fun DiagramView(spec: DiagramSpec, modifier: Modifier = Modifier) {
    val colors = DiagramColors(
        accent = AppTheme.colors.accent,
        plate = AppTheme.colors.card,
        fill = AppTheme.colors.fill,
        secondary = AppTheme.colors.secondaryLabel,
        ink = MaterialTheme.colorScheme.onSurface,
        onSolid = AppTheme.colors.onSolid,
        data = AppTheme.colors.dData,
        compute = AppTheme.colors.dCompute,
        flow = AppTheme.colors.dFlow,
        choice = AppTheme.colors.dChoice,
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colors.plate)
            .padding(Spacing.sm),
    ) {
        DiagramCanvas(spec, colors)
        Text(spec.caption, style = MaterialTheme.typography.bodySmall, color = colors.secondary, modifier = Modifier.padding(top = Spacing.xs, start = Spacing.xs, end = Spacing.xs))
    }
}

/**
 * The colours a diagram is drawn in: the theme's on a learning screen, the
 * player's own blues on the podcast board. [plate] is the ground the diagram
 * sits on, which an edge's label is backed with.
 */
data class DiagramColors(
    val accent: Color,
    val plate: Color,
    val fill: Color,
    val secondary: Color,
    val ink: Color,
    val onSolid: Color,
    /** What each [com.shahaabapps.core.content.DHue] is drawn in, the theme's own colours. */
    val data: Color = accent,
    val compute: Color = accent,
    val flow: Color = accent,
    val choice: Color = accent,
)

/**
 * The diagram itself, unframed. [reveal] gives each node how far it has
 * arrived (0 absent, 1 there) and [emphasis] how bright it is (1 lit or
 * normal, less to dim it); an edge takes the fainter of its two ends. A
 * [pulse] is a dot run along the edge between two nodes, [pulseAt] of the way
 * from the first. Left at their defaults it draws exactly as [DiagramView]
 * always has.
 */
@Composable
fun DiagramCanvas(
    spec: DiagramSpec,
    colors: DiagramColors,
    modifier: Modifier = Modifier,
    reveal: (String) -> Float = { 1f },
    emphasis: (String) -> Float = { 1f },
    pulse: Pair<String, String>? = null,
    pulseAt: Float = 0f,
) {
    val measurer = rememberTextMeasurer()
    val accent = colors.accent
    val card = colors.plate
    val fill = colors.fill
    val secondary = colors.secondary
    val ink = colors.ink
    val onSolid = colors.onSolid
    // Sized for a card you read, not a label you squint at. fitText shrinks
    // these to fit a small node, so a generous starting point costs nothing
    // where there is no room and buys legibility where there is.
    val labelStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
    val subStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center, color = secondary)
    val edgeStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = secondary)
    Canvas(modifier = modifier.fillMaxWidth().aspectRatio(1f / spec.aspect)) {
            val byId = spec.nodes.associateBy { it.id }
            fun rectOf(n: DNode): Rect {
                val w = (n.w * size.width).coerceAtMost(size.width)
                val h = (n.h * size.height).coerceAtMost(size.height)
                // Nudged back inside rather than drawn off the edge. A spec that
                // places a node past the boundary is a content bug, and the
                // content test catches those; this stops it looking broken
                // meanwhile.
                val left = (n.x * size.width - w / 2f).coerceIn(0f, size.width - w)
                val top = (n.y * size.height - h / 2f).coerceIn(0f, size.height - h)
                return Rect(Offset(left, top), Size(w, h))
            }
            fun alphaOf(id: String) = (reveal(id) * emphasis(id)).coerceIn(0f, 1f)
            // Edges first so nodes sit on top.
            spec.edges.forEach { e ->
                val a = byId[e.from] ?: return@forEach
                val b = byId[e.to] ?: return@forEach
                val alpha = minOf(alphaOf(e.from), alphaOf(e.to))
                if (alpha <= 0.01f) return@forEach
                drawEdge(rectOf(a), rectOf(b), e, secondary.copy(alpha = secondary.alpha * alpha), card, measurer, edgeStyle.copy(color = secondary.copy(alpha = secondary.alpha * alpha)))
            }
            pulse?.let { (from, to) ->
                val a = byId[from] ?: return@let
                val b = byId[to] ?: return@let
                val start = edgePoint(rectOf(a), rectOf(b).center)
                val end = edgePoint(rectOf(b), rectOf(a).center)
                val t = pulseAt.coerceIn(0f, 1f)
                val at = Offset(start.x + (end.x - start.x) * t, start.y + (end.y - start.y) * t)
                // A dot with a soft halo, brightest mid-way so it seems to leave
                // one node and arrive at the other rather than blink in place.
                val glow = kotlin.math.sin(t * Math.PI).toFloat().coerceAtLeast(0.25f)
                drawCircle(accent.copy(alpha = 0.28f * glow), 9.dp.toPx(), at)
                drawCircle(accent.copy(alpha = glow), 4.5.dp.toPx(), at)
            }
            spec.nodes.forEach { n ->
                val alpha = alphaOf(n.id)
                if (alpha <= 0.01f) return@forEach
                // Arriving nodes grow the last few per cent into place.
                val grow = 0.88f + 0.12f * reveal(n.id).coerceIn(0f, 1f)
                val full = rectOf(n)
                val r = Rect(
                    full.center.x - full.width * grow / 2f, full.center.y - full.height * grow / 2f,
                    full.center.x + full.width * grow / 2f, full.center.y + full.height * grow / 2f,
                )
                fun Color.faded() = copy(alpha = this.alpha * alpha)
                // Tinted cards rather than solid blocks. A diagram is read, not
                // admired, and a column of black rectangles with reversed-out
                // type is heavy going; a wash of colour inside a firmer edge
                // separates the steps just as well and leaves every word in
                // ordinary ink. Tone says how much a node matters: 0 is the
                // thing being taught, 1 supports it, 2 is an aside.
                val hue = when (n.hue) {
                    DHue.NEUTRAL -> accent
                    DHue.DATA -> colors.data
                    DHue.COMPUTE -> colors.compute
                    DHue.FLOW -> colors.flow
                    DHue.CHOICE -> colors.choice
                }
                val tone = when (n.tone) {
                    0 -> hue.copy(alpha = 0.16f)
                    1 -> hue.copy(alpha = 0.09f)
                    else -> secondary.copy(alpha = 0.10f)
                }.faded()
                val rim = when (n.tone) {
                    0 -> hue.copy(alpha = 0.55f)
                    1 -> hue.copy(alpha = 0.30f)
                    else -> secondary.copy(alpha = 0.30f)
                }.faded()
                val textColor = ink.faded()
                val radius = 18.dp.toPx()
                val rimWidth = 1.5.dp.toPx()
                when (n.kind) {
                    NodeKind.BOX -> {
                        drawRoundRect(tone, r.topLeft, r.size, CornerRadius(radius))
                        drawRoundRect(rim, r.topLeft, r.size, CornerRadius(radius), style = Stroke(width = rimWidth))
                    }
                    NodeKind.PILL -> {
                        drawRoundRect(tone, r.topLeft, r.size, CornerRadius(r.height / 2f))
                        drawRoundRect(rim, r.topLeft, r.size, CornerRadius(r.height / 2f), style = Stroke(width = rimWidth))
                    }
                    NodeKind.NOTE -> {
                        drawRoundRect(fill.faded(), r.topLeft, r.size, CornerRadius(radius))
                        drawRoundRect(secondary.copy(alpha = 0.5f).faded(), r.topLeft, r.size, CornerRadius(radius), style = Stroke(width = rimWidth, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))))
                    }
                    NodeKind.CYLINDER -> {
                        // A wide, shallow cylinder reads as a squashed ellipse,
                        // so the end caps shrink as the shape gets wider: a
                        // full-width store stays a card with a lid rather than
                        // turning into a running track.
                        val ry = (r.height * 0.14f).coerceAtMost(r.width * 0.045f)
                        drawRoundRect(tone, Offset(r.left, r.top + ry), Size(r.width, r.height - ry * 2), CornerRadius(ry * 2.2f, ry))
                        drawOval(tone, Offset(r.left, r.top), Size(r.width, ry * 2))
                        drawOval(rim, Offset(r.left, r.top), Size(r.width, ry * 2), style = Stroke(width = rimWidth))
                        drawOval(tone, Offset(r.left, r.bottom - ry * 2), Size(r.width, ry * 2))
                        drawRoundRect(rim, Offset(r.left, r.top + ry), Size(r.width, r.height - ry * 2), CornerRadius(ry * 2.2f, ry), style = Stroke(width = rimWidth))
                    }
                    NodeKind.CLOUD -> {
                        val p = Path().apply {
                            addOval(Rect(r.left, r.top + r.height * 0.3f, r.left + r.width * 0.5f, r.bottom))
                            addOval(Rect(r.left + r.width * 0.25f, r.top, r.left + r.width * 0.8f, r.bottom - r.height * 0.1f))
                            addOval(Rect(r.left + r.width * 0.5f, r.top + r.height * 0.25f, r.right, r.bottom))
                        }
                        drawPath(p, tone)
                    }
                    NodeKind.USER -> {
                        val headR = r.height * 0.22f
                        drawCircle(tone, headR, Offset(r.center.x, r.top + headR + 2f))
                        drawRoundRect(tone, Offset(r.center.x - headR * 1.5f, r.top + headR * 2.3f), Size(headR * 3f, r.height - headR * 2.3f), CornerRadius(headR))
                    }
                }
                // How much of the shape the text may actually use. A box is
                // nearly all usable; a cylinder's body is inset by its end
                // caps, and a cloud is only solid near its middle, so text
                // laid out to the bounding box would sit on nothing.
                val (wFrac, hFrac) = when (n.kind) {
                    NodeKind.CYLINDER -> 0.84f to 0.68f
                    NodeKind.CLOUD -> 0.60f to 0.50f
                    NodeKind.USER -> 1.35f to 0.52f
                    // Room to breathe inside the card, so the words are not
                    // pressed against its edge.
                    else -> 0.84f to 0.74f
                }
                // Text is laid out on the node's full size, so a node growing
                // into place does not re-fit its type on every frame.
                val boxW = full.width * wFrac
                // A USER's label sits under the figure, so its height is the
                // room left beneath the shape, not room inside it.
                val boxH = if (n.kind == NodeKind.USER) {
                    (size.height - full.bottom - 2f).coerceAtLeast(0f)
                } else {
                    full.height * hFrac
                }

                // The label gets first call on the height; the sub-label takes
                // what is left, and is dropped rather than pushing the label out.
                val labelH = if (n.sub != null) boxH * 0.62f else boxH
                val label = fitText(measurer, n.label, labelStyle.copy(color = textColor), boxW, labelH)
                val subH = boxH - label.size.height
                val sub = n.sub
                    ?.takeIf { subH >= 8.sp.toPx() }
                    // Grey under the title, as a caption is: it explains the
                    // step without competing with its name.
                    ?.let { fitText(measurer, it, subStyle.copy(color = secondary.copy(alpha = secondary.alpha * alpha)), boxW, subH) }

                val totalH = label.size.height + (sub?.size?.height ?: 0)
                val top = if (n.kind == NodeKind.USER) full.bottom + 2f else full.center.y - totalH / 2f
                var y = top
                drawClamped(label, full.center.x, y)
                y += label.size.height
                if (sub != null) drawClamped(sub, full.center.x, y)
            }
    }
}

/**
 * The smallest type a diagram is allowed to shrink to. Below this it is a grey
 * smudge, and the content test is what keeps labels from needing it.
 */
private val MIN_DIAGRAM_SP = 7.5f

/**
 * Lays out text so it cannot leave the shape it belongs to.
 *
 * The box is fixed by the spec and the words are whatever the author wrote, so
 * one of the two has to give. Shrinking the type is the only choice that loses
 * nothing, so it steps down from the diagram's own size to [MIN_DIAGRAM_SP].
 * Only if even that will not fit does it clip to the lines there is room for
 * and ellipsise — which is a content bug, not a layout one, and the thing the
 * geometry test exists to prevent.
 *
 * Before this, the width was constrained and the height was not, so a label
 * that wrapped to four lines in a two-line box was simply centred and drawn
 * straight over the shape's edges and whatever was next to it.
 */
private fun DrawScope.fitText(
    measurer: androidx.compose.ui.text.TextMeasurer,
    text: String,
    style: TextStyle,
    maxWidth: Float,
    maxHeight: Float,
): TextLayoutResult {
    val constraints = androidx.compose.ui.unit.Constraints(maxWidth = maxWidth.toInt().coerceAtLeast(1))
    // The style's own line height is absolute, so shrinking the font without
    // it leaves every line as tall as before and the shrinking does almost
    // nothing. Both have to come down together.
    fun at(sp: Float) = style.copy(fontSize = sp.sp, lineHeight = (sp * 1.3f).sp)
    var sp = style.fontSize.value.takeIf { it.isFinite() && it > MIN_DIAGRAM_SP } ?: 12f
    while (sp > MIN_DIAGRAM_SP) {
        val laid = measurer.measure(text, at(sp), constraints = constraints)
        if (laid.size.height <= maxHeight) return laid
        sp -= 1f
    }
    val floor = at(MIN_DIAGRAM_SP)
    val lineHeight = measurer.measure("Mg", floor).size.height.coerceAtLeast(1)
    val lines = (maxHeight / lineHeight).toInt().coerceAtLeast(1)
    return measurer.measure(
        text,
        floor,
        overflow = TextOverflow.Ellipsis,
        maxLines = lines,
        constraints = constraints,
    )
}

/**
 * Draws text centred on [centreX], then pulls it back inside the canvas if it
 * would hang off an edge. Off-centre reads as a near miss; half a word past
 * the border reads as broken.
 */
private fun DrawScope.drawClamped(laid: TextLayoutResult, centreX: Float, y: Float) {
    val x = (centreX - laid.size.width / 2f).coerceIn(0f, (size.width - laid.size.width).coerceAtLeast(0f))
    val top = y.coerceIn(0f, (size.height - laid.size.height).coerceAtLeast(0f))
    drawText(laid, topLeft = Offset(x, top))
}

private fun DrawScope.drawEdge(a: Rect, b: Rect, e: DEdge, color: Color, plate: Color, measurer: androidx.compose.ui.text.TextMeasurer, style: TextStyle) {
    val start = edgePoint(a, b.center)
    val end = edgePoint(b, a.center)
    val stroke = Stroke(width = 2.5f, cap = StrokeCap.Round, pathEffect = if (e.dashed) PathEffect.dashPathEffect(floatArrayOf(10f, 8f)) else null)
    drawLine(color, start, end, strokeWidth = stroke.width, cap = StrokeCap.Round, pathEffect = stroke.pathEffect)
    arrowHead(end, start, color)
    if (e.bidirectional) arrowHead(start, end, color)
    e.label?.let { text ->
        // Measured with no constraints at all before, so a long label ran out
        // of the canvas in one unbroken line. It now wraps inside a share of
        // the width and is pulled back inside the edges.
        val t = measurer.measure(
            text,
            style,
            constraints = androidx.compose.ui.unit.Constraints(maxWidth = (size.width * 0.34f).toInt().coerceAtLeast(1)),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        val mid = Offset((start.x + end.x) / 2f, (start.y + end.y) / 2f)
        val pad = 5f
        val x = (mid.x - t.size.width / 2f).coerceIn(pad, (size.width - t.size.width - pad).coerceAtLeast(pad))
        val y = (mid.y - t.size.height / 2f).coerceIn(pad, (size.height - t.size.height - pad).coerceAtLeast(pad))
        // A real plate, in the card's own colour. The old one was drawn at
        // alpha 0.001, so the line ran straight through the words.
        drawRoundRect(plate, Offset(x - pad, y - 2f), Size(t.size.width + pad * 2, t.size.height + 4f), CornerRadius(6f))
        drawText(t, topLeft = Offset(x, y))
    }
}

/** The point where a line from the rect centre toward [toward] leaves the rect. */
private fun edgePoint(r: Rect, toward: Offset): Offset {
    val dx = toward.x - r.center.x
    val dy = toward.y - r.center.y
    if (dx == 0f && dy == 0f) return r.center
    val sx = if (dx != 0f) (r.width / 2f) / kotlin.math.abs(dx) else Float.MAX_VALUE
    val sy = if (dy != 0f) (r.height / 2f) / kotlin.math.abs(dy) else Float.MAX_VALUE
    val s = minOf(sx, sy)
    return Offset(r.center.x + dx * s, r.center.y + dy * s)
}

private fun DrawScope.arrowHead(tip: Offset, from: Offset, color: Color) {
    val angle = atan2(tip.y - from.y, tip.x - from.x)
    val len = 11f
    val spread = 0.5f
    val p = Path().apply {
        moveTo(tip.x, tip.y)
        lineTo(tip.x - len * cos(angle - spread), tip.y - len * sin(angle - spread))
        lineTo(tip.x - len * cos(angle + spread), tip.y - len * sin(angle + spread))
        close()
    }
    drawPath(p, color)
}

@Suppress("unused")
private fun dist(a: Offset, b: Offset) = sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y))

@Suppress("unused")
private fun TextLayoutResult.sizeOf(): IntSize = size
