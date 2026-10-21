package com.shahaabapps.twotracks.ui.board

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.BarlowFamily
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.core.ui.theme.JetBrainsFamily
import java.util.Locale

/*
 * The pieces the Departures screens share, drawn as its prototype draws them:
 * condensed capitals for anything a board would print, JetBrains Mono for
 * times, codes and figures, Barlow for what you read, and signal yellow for
 * the one thing to do next.
 */

/** The gutter every board screen keeps: the prototype's 20px. */
val BoardGutter: Dp = 20.dp

private fun caps(text: String) = text.uppercase(Locale.ROOT)

/** A column head, an eyebrow or a field's name: condensed capitals, spaced. */
@Composable
fun BoardLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.secondaryLabel,
    size: TextUnit = 12.sp,
    weight: FontWeight = FontWeight.SemiBold,
    spacing: TextUnit = 0.14.em,
    textAlign: TextAlign? = null,
) {
    Text(
        caps(text), modifier = modifier, color = color, textAlign = textAlign, maxLines = 2,
        style = TextStyle(fontFamily = CondensedFamily, fontWeight = weight, fontSize = size, letterSpacing = spacing, lineHeight = size * 1.25f),
    )
}

/** A time, a code, a gate: JetBrains Mono, as a board prints them. */
@Composable
fun BoardMono(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.secondaryLabel,
    size: TextUnit = 11.sp,
    weight: FontWeight = FontWeight.Medium,
    spacing: TextUnit = 0.08.em,
    textAlign: TextAlign? = null,
) {
    Text(
        text, modifier = modifier, color = color, maxLines = 1, textAlign = textAlign,
        style = TextStyle(fontFamily = JetBrainsFamily, fontWeight = weight, fontSize = size, letterSpacing = spacing),
    )
}

/** A page title: condensed bold capitals, set tight. */
@Composable
fun BoardTitle(text: String, modifier: Modifier = Modifier, size: TextUnit = 44.sp, color: Color = AppTheme.colors.ink, textAlign: TextAlign? = null) {
    Text(
        caps(text), color = color, textAlign = textAlign,
        style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = size, lineHeight = size * 0.95f, letterSpacing = 0.01.em),
        modifier = modifier.semantics { heading() },
    )
}

/** Barlow prose, in the secondary grey unless told otherwise. */
@Composable
fun BoardText(text: String, modifier: Modifier = Modifier, color: Color = AppTheme.colors.secondaryLabel, size: TextUnit = 15.sp, weight: FontWeight = FontWeight.Normal, textAlign: TextAlign? = null, maxLines: Int = Int.MAX_VALUE) {
    Text(
        text, modifier = modifier, color = color, textAlign = textAlign, maxLines = maxLines, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
        style = TextStyle(fontFamily = BarlowFamily, fontWeight = weight, fontSize = size, lineHeight = size * 1.45f),
    )
}

/** The one thing to do next: signal yellow, condensed capitals and an arrow. */
@Composable
fun BoardButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true, arrow: Boolean = true, height: Dp = 52.dp) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth().height(height).clip(shape)
            .background(if (enabled) AppTheme.colors.accent else AppTheme.colors.separator)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
    ) {
        val ink = if (enabled) AppTheme.colors.onSolid else AppTheme.colors.secondaryLabel
        Text(caps(text), color = ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp, letterSpacing = 0.1.em))
        if (arrow) Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = ink, modifier = Modifier.size(18.dp))
    }
}

/** The quieter action: an outline, condensed capitals. */
@Composable
fun BoardOutlineButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, height: Dp = 44.dp, enabled: Boolean = true, color: Color = AppTheme.colors.ink) {
    val shape = RoundedCornerShape(10.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.height(height).clip(shape).border(1.dp, AppTheme.colors.separator, shape)
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick).padding(horizontal = 14.dp),
    ) {
        Text(caps(text), color = if (enabled) color else AppTheme.colors.secondaryLabel, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp, letterSpacing = 0.1.em))
    }
}

/** A text link in the board's yellow: condensed capitals. */
@Composable
fun BoardLink(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, color: Color = AppTheme.colors.accent, size: TextUnit = 13.sp) {
    Text(
        caps(text), color = color,
        style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = size, letterSpacing = 0.1.em),
        modifier = modifier.heightIn(min = 28.dp).clip(RoundedCornerShape(6.dp)).clickable(role = Role.Button, onClick = onClick).padding(vertical = 6.dp),
    )
}

/** "First time flying? Create your space" at the foot of a page. */
@Composable
fun BoardFooter(lead: String, action: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()) {
        if (lead.isNotEmpty()) BoardText(lead, size = 14.sp)
        if (lead.isNotEmpty()) Spacer(Modifier.width(8.dp))
        Text(
            action, color = AppTheme.colors.ink,
            style = TextStyle(fontFamily = BarlowFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, textDecoration = TextDecoration.Underline),
            modifier = Modifier.heightIn(min = 44.dp).clip(RoundedCornerShape(6.dp)).clickable(role = Role.Button, onClick = onClick).padding(vertical = 13.dp),
        )
    }
}

/** The back arrow at the top left, nudged into the gutter as the prototype has it. */
@Composable
fun BoardBack(onClick: () -> Unit, label: String = "Back", modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.offset(x = (-10).dp).size(44.dp).clip(CircleShape).clickable(role = Role.Button, onClickLabel = label, onClick = onClick),
    ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = label, tint = AppTheme.colors.ink) }
}

/** The back arrow, "STEP 2/3", and a yellow segment per step done. */
@Composable
fun BoardSteps(step: Int, total: Int, onBack: (() -> Unit)?, backLabel: String = "Back") {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().height(44.dp)) {
            if (onBack != null) BoardBack(onBack, backLabel)
            Spacer(Modifier.weight(1f))
            BoardMono("STEP $step/$total", spacing = 0.sp)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 8.dp).clearAndSetSemantics {}) {
            repeat(total) { i -> Box(Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp)).background(if (i < step) AppTheme.colors.accent else AppTheme.colors.separator)) }
        }
    }
}

/** A card on the board: a step lighter than the page, rounded. */
@Composable
fun BoardCard(modifier: Modifier = Modifier, radius: Dp = 16.dp, padding: Dp = 16.dp, content: @Composable () -> Unit) {
    Column(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(radius)).background(AppTheme.colors.card).padding(padding)) { content() }
}

/**
 * A field as the board draws one: a dark box with a hairline, its name in
 * condensed capitals above. Emails and codes are set in mono.
 */
@Composable
fun BoardField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    labelTrailing: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    mono: Boolean = false,
    deep: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    placeholder: String = "",
    height: Dp = 50.dp,
    textStyle: TextStyle? = null,
    singleLine: Boolean = true,
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()
    val style = textStyle ?: if (mono) TextStyle(fontFamily = JetBrainsFamily, fontSize = 15.sp, color = AppTheme.colors.ink)
    else TextStyle(fontFamily = BarlowFamily, fontSize = 16.sp, color = AppTheme.colors.ink)
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BoardLabel(label, modifier = Modifier.weight(1f))
            labelTrailing?.invoke()
        }
        val shape = RoundedCornerShape(10.dp)
        val line = when {
            isError -> AppTheme.colors.destructive
            focused -> AppTheme.colors.accent
            else -> AppTheme.colors.separator
        }
        BasicTextField(
            value = value, onValueChange = onValueChange, singleLine = singleLine, textStyle = style,
            cursorBrush = SolidColor(AppTheme.colors.accent),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation, interactionSource = interaction,
            modifier = Modifier.fillMaxWidth().heightIn(min = height).clip(shape)
                .background(if (deep) AppTheme.colors.deep else AppTheme.colors.card)
                .border(1.dp, line, shape).semantics { contentDescription = label },
            decorationBox = { inner ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.heightIn(min = height).padding(start = 14.dp, end = 6.dp)) {
                    Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        if (value.isEmpty() && placeholder.isNotEmpty()) Text(placeholder, style = style.copy(color = AppTheme.colors.secondaryLabel))
                        inner()
                    }
                    trailing?.invoke()
                }
            },
        )
    }
}

/**
 * Characters on flip tiles: each on its own near-black tile with the lower
 * half a shade lighter, as the split-flap board does it. Read out as the
 * whole text, not letter by letter.
 */
@Composable
fun FlipTiles(
    text: String,
    modifier: Modifier = Modifier,
    tileWidth: Dp = 54.dp,
    tileHeight: Dp = 70.dp,
    fontSize: TextUnit = 44.sp,
    color: Color = AppTheme.colors.accent,
    tile: Color = AppTheme.colors.deep,
    gap: Dp = 6.dp,
    fill: Boolean = false,
    separatorColor: Color = AppTheme.colors.secondaryLabel,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(gap), verticalAlignment = Alignment.CenterVertically, modifier = modifier.clearAndSetSemantics { contentDescription = text }) {
        text.forEach { ch ->
            if (ch == ':') {
                Text(":", color = separatorColor, style = TextStyle(fontFamily = JetBrainsFamily, fontWeight = FontWeight.Bold, fontSize = fontSize * 0.9f))
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = (if (fill) Modifier.weight(1f) else Modifier.width(tileWidth)).height(tileHeight).clip(RoundedCornerShape(8.dp)).background(tile)
                        .drawBehind { drawRect(Color.White.copy(alpha = 0.03f), topLeft = Offset(0f, size.height / 2), size = Size(size.width, size.height / 2)) },
                ) { Text(ch.toString(), color = color, style = TextStyle(fontFamily = JetBrainsFamily, fontWeight = FontWeight.Bold, fontSize = fontSize)) }
            }
        }
    }
}

/** A notice in a status colour: a tinted box with its edge, a condensed heading and a line. */
@Composable
fun BoardNotice(title: String, text: String, color: Color, background: Color, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(14.dp)
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth().clip(shape).background(background).border(1.5.dp, color, shape).padding(14.dp),
    ) {
        BoardLabel(title, color = color, size = 17.sp, weight = FontWeight.Bold, spacing = 0.08.em)
        if (text.isNotBlank()) BoardText(text, color = AppTheme.colors.ink.copy(alpha = 0.92f), size = 14.sp)
    }
}

/**
 * The perforation across a boarding pass: a dashed line with a notch cut
 * from each edge in the page colour.
 */
@Composable
fun Perforation(modifier: Modifier = Modifier) {
    val page = AppTheme.colors.groupedBackground
    val dash = AppTheme.colors.separator
    Box(
        modifier.fillMaxWidth().height(20.dp).drawBehind {
            val y = size.height / 2
            drawLine(dash, Offset(16.dp.toPx(), y), Offset(size.width - 16.dp.toPx(), y), strokeWidth = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 5.dp.toPx())))
            drawCircle(page, radius = 10.dp.toPx(), center = Offset(-2.dp.toPx(), y))
            drawCircle(page, radius = 10.dp.toPx(), center = Offset(size.width + 2.dp.toPx(), y))
        },
    )
}

/**
 * A segmented choice on a card: the chosen one in the light ink with dark
 * text, or in [colorOf] its option, as whose-line tabs take each person's colour.
 */
@Composable
fun <T> BoardSegments(
    options: List<Pair<T, String>>, selected: T, onSelect: (T) -> Unit, modifier: Modifier = Modifier, colorOf: ((T) -> Color)? = null,
    /** Each segment as wide as its label, the row scrolling when they outgrow it, so a two-word label never wraps. */
    scroll: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(AppTheme.colors.card)
            .then(if (scroll) Modifier.horizontalScroll(rememberScrollState()) else Modifier).padding(4.dp),
    ) {
        options.forEach { (value, label) ->
            val on = value == selected
            Box(
                contentAlignment = Alignment.Center,
                modifier = (if (scroll) Modifier else Modifier.weight(1f)).height(44.dp).clip(RoundedCornerShape(9.dp)).background(if (on) colorOf?.invoke(value) ?: AppTheme.colors.ink else Color.Transparent)
                    .semantics { this.selected = on }.clickable(role = Role.Tab) { onSelect(value) },
            ) {
                Text(
                    caps(label), color = if (on) AppTheme.colors.groupedBackground else AppTheme.colors.secondaryLabel, maxLines = 1, softWrap = false,
                    style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 0.08.em),
                    modifier = if (scroll) Modifier.padding(horizontal = 14.dp) else Modifier,
                )
            }
        }
    }
}

/** A figure on a card: the number in condensed bold, what it counts in capitals under it. */
@Composable
fun BoardStat(value: String, label: String, modifier: Modifier = Modifier, color: Color = AppTheme.colors.ink, size: TextUnit = 24.sp) {
    Column(modifier.clip(RoundedCornerShape(12.dp)).background(AppTheme.colors.card).padding(horizontal = 12.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(value, color = color, maxLines = 1, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = size, lineHeight = size * 1.1f))
        BoardLabel(label, size = 12.sp, spacing = 0.1.em)
    }
}

/** A row in a ruled list: hairline below, at least [minHeight] tall. */
@Composable
fun BoardRow(modifier: Modifier = Modifier, minHeight: Dp = 56.dp, onClick: (() -> Unit)? = null, content: @Composable RowScope.() -> Unit) {
    Column(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight).then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier),
            content = content,
        )
        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
    }
}

/** A small square in a person's colour, as the board marks whose a row is. */
@Composable
fun WhoMark(color: Color, size: Dp = 8.dp) {
    Box(Modifier.size(size).clip(RoundedCornerShape(2.dp)).background(color))
}

/** A thin vertical bar in a person's colour, filling the height of its row. */
@Composable
fun RowScope.LineMark(color: Color, width: Dp = 3.dp, height: Dp? = null) {
    Box(Modifier.width(width).then(if (height != null) Modifier.height(height) else Modifier.fillMaxHeight()).background(color))
}
