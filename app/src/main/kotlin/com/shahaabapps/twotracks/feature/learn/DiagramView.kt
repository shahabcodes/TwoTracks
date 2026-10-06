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
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
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
                val w = n.w * size.width
                val h = n.h * size.height
                return Rect(Offset(n.x * size.width - w / 2f, n.y * size.height - h / 2f), Size(w, h))
            }
            // Edges first so nodes sit on top.
            spec.edges.forEach { e ->
                val a = byId[e.from] ?: return@forEach
                val b = byId[e.to] ?: return@forEach
                drawEdge(rectOf(a), rectOf(b), e, secondary, measurer, edgeStyle)
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
                val label = measurer.measure(n.label, labelStyle.copy(color = textColor), constraints = androidx.compose.ui.unit.Constraints(maxWidth = (r.width * 0.92f).toInt().coerceAtLeast(1)))
                val sub = n.sub?.let { measurer.measure(it, subStyle.copy(color = textColor.copy(alpha = 0.85f)), constraints = androidx.compose.ui.unit.Constraints(maxWidth = (r.width * 0.92f).toInt().coerceAtLeast(1))) }
                val totalH = label.size.height + (sub?.size?.height ?: 0)
                var y = if (n.kind == NodeKind.USER) r.bottom + 2f else r.center.y - totalH / 2f
                if (n.kind == NodeKind.USER) {
                    drawText(label.copy(layoutInput = label.layoutInput), topLeft = Offset(r.center.x - label.size.width / 2f, y))
                } else {
                    drawText(label, topLeft = Offset(r.center.x - label.size.width / 2f, y))
                    y += label.size.height
                    if (sub != null) drawText(sub, topLeft = Offset(r.center.x - sub.size.width / 2f, y))
                }
            }
        }
        Text(spec.caption, style = MaterialTheme.typography.bodySmall, color = secondary, modifier = Modifier.padding(top = Spacing.xs, start = Spacing.xs, end = Spacing.xs))
    }
}

private fun DrawScope.drawEdge(a: Rect, b: Rect, e: DEdge, color: Color, measurer: androidx.compose.ui.text.TextMeasurer, style: TextStyle) {
    val start = edgePoint(a, b.center)
    val end = edgePoint(b, a.center)
    val stroke = Stroke(width = 2.5f, cap = StrokeCap.Round, pathEffect = if (e.dashed) PathEffect.dashPathEffect(floatArrayOf(10f, 8f)) else null)
    drawLine(color, start, end, strokeWidth = stroke.width, cap = StrokeCap.Round, pathEffect = stroke.pathEffect)
    arrowHead(end, start, color)
    if (e.bidirectional) arrowHead(start, end, color)
    e.label?.let { text ->
        val t = measurer.measure(text, style)
        val mid = Offset((start.x + end.x) / 2f, (start.y + end.y) / 2f)
        val pad = 6f
        drawRoundRect(Color.White.copy(alpha = 0.001f), Offset(mid.x - t.size.width / 2f - pad, mid.y - t.size.height / 2f - 2f), Size(t.size.width + pad * 2, t.size.height + 4f), CornerRadius(6f))
        drawIntoCanvas { c ->
            val paint = android.graphics.Paint().apply { this.color = android.graphics.Color.TRANSPARENT }
            c.nativeCanvas.drawRect(0f, 0f, 0f, 0f, paint)
        }
        drawText(t, topLeft = Offset(mid.x - t.size.width / 2f, mid.y - t.size.height / 2f))
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
