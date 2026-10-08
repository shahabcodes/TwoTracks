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
    val measurer = rememberTextMeasurer()
    val accent = AppTheme.colors.accent
    val card = AppTheme.colors.card
    val fill = AppTheme.colors.fill
    val secondary = AppTheme.colors.secondaryLabel
    val ink = MaterialTheme.colorScheme.onSurface
    val labelStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
    val subStyle = MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center, fontSize = 10.sp)
    val edgeStyle = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = secondary)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(card)
            .padding(Spacing.sm),
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(1f / spec.aspect)) {
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
            // Edges first so nodes sit on top.
            spec.edges.forEach { e ->
                val a = byId[e.from] ?: return@forEach
                val b = byId[e.to] ?: return@forEach
                drawEdge(rectOf(a), rectOf(b), e, secondary, card, measurer, edgeStyle)
            }
            spec.nodes.forEach { n ->
                val r = rectOf(n)
                val tone = when (n.tone) { 0 -> accent; 1 -> accent.copy(alpha = 0.55f); else -> secondary.copy(alpha = 0.35f) }
                val textColor = if (n.tone == 0 && n.kind != NodeKind.NOTE) Color.White else ink
                when (n.kind) {
                    NodeKind.BOX -> drawRoundRect(tone, r.topLeft, r.size, CornerRadius(12.dp.toPx()))
                    NodeKind.PILL -> drawRoundRect(tone, r.topLeft, r.size, CornerRadius(r.height / 2f))
                    NodeKind.NOTE -> {
                        drawRoundRect(fill, r.topLeft, r.size, CornerRadius(8.dp.toPx()))
                        drawRoundRect(secondary.copy(alpha = 0.5f), r.topLeft, r.size, CornerRadius(8.dp.toPx()), style = Stroke(width = 1.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f))))
                    }
                    NodeKind.CYLINDER -> {
                        val ry = r.height * 0.14f
                        drawRoundRect(tone, Offset(r.left, r.top + ry), Size(r.width, r.height - ry * 2), CornerRadius(ry * 2.2f, ry))
                        drawOval(tone, Offset(r.left, r.top), Size(r.width, ry * 2))
                        drawOval(Color.White.copy(alpha = 0.35f), Offset(r.left, r.top), Size(r.width, ry * 2), style = Stroke(width = 1.5f))
                        drawOval(tone, Offset(r.left, r.bottom - ry * 2), Size(r.width, ry * 2))
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
                    else -> 0.90f to 0.86f
                }
                val boxW = r.width * wFrac
                // A USER's label sits under the figure, so its height is the
                // room left beneath the shape, not room inside it.
                val boxH = if (n.kind == NodeKind.USER) {
                    (size.height - r.bottom - 2f).coerceAtLeast(0f)
                } else {
                    r.height * hFrac
                }

                // The label gets first call on the height; the sub-label takes
                // what is left, and is dropped rather than pushing the label out.
                val labelH = if (n.sub != null) boxH * 0.62f else boxH
                val label = fitText(measurer, n.label, labelStyle.copy(color = textColor), boxW, labelH)
                val subH = boxH - label.size.height
                val sub = n.sub
                    ?.takeIf { subH >= 8.sp.toPx() }
                    ?.let { fitText(measurer, it, subStyle.copy(color = textColor.copy(alpha = 0.85f)), boxW, subH) }

                val totalH = label.size.height + (sub?.size?.height ?: 0)
                val top = if (n.kind == NodeKind.USER) r.bottom + 2f else r.center.y - totalH / 2f
                var y = top
                drawClamped(label, r.center.x, y)
                y += label.size.height
                if (sub != null) drawClamped(sub, r.center.x, y)
            }
        }
        Text(spec.caption, style = MaterialTheme.typography.bodySmall, color = secondary, modifier = Modifier.padding(top = Spacing.xs, start = Spacing.xs, end = Spacing.xs))
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
