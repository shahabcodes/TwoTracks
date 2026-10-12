package com.shahaabapps.twotracks.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.content.WidgetSnapshot
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.twotracks.widget.WidgetConfig
import com.shahaabapps.twotracks.widget.faceFor
import com.shahaabapps.twotracks.widget.paletteFor
import java.time.LocalDate

/**
 * The widget, as it will look, drawn in ordinary Compose.
 *
 * Glance cannot be rendered inside the app, so this is a second renderer for
 * the same thing. What it says is not a second opinion: the words, the pips
 * and the colours all come from [faceFor] and [paletteFor], which the widget
 * itself uses, so the only thing duplicated here is the layout.
 *
 * It sits on a backdrop rather than on the page, because half of what these
 * settings decide is how the widget sits against a wallpaper — a translucent
 * card on a white page tells you nothing at all.
 */
@Composable
fun WidgetPreview(
    config: WidgetConfig,
    snapshot: WidgetSnapshot,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    /** False when the caller has already drawn the wallpaper behind several of them. */
    backdrop: Boolean = true,
) {
    val face = snapshot.faceFor(config, LocalDate.now())
    val palette = paletteFor(
        config,
        themed = AppTheme.colors.card,
        themedStrong = MaterialTheme.colorScheme.onSurface,
        themedSoft = AppTheme.colors.secondaryLabel,
    )
    Box(
        modifier = modifier
            .then(if (backdrop) Modifier.clip(RoundedCornerShape(if (compact) 10.dp else 18.dp)).background(Backdrop).padding(if (compact) 8.dp else 14.dp) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(config.corners.radius.dp))
                .background(palette.background)
                .padding(horizontal = if (compact) 10.dp else 14.dp, vertical = if (compact) 8.dp else 14.dp),
        ) {
            Text(
                face.title,
                color = palette.soft,
                fontSize = if (compact) 8.sp else 11.sp,
                lineHeight = if (compact) 11.sp else 14.sp,
                fontWeight = FontWeight.Medium,
            )
            Spacer(Modifier.height(if (compact) 2.dp else 4.dp))
            Text(
                face.headline,
                color = palette.strong,
                fontSize = if (compact) 13.sp else 20.sp,
                lineHeight = if (compact) 16.sp else 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = if (compact) 1 else 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                face.body,
                color = palette.soft,
                fontSize = if (compact) 10.sp else 13.sp,
                lineHeight = if (compact) 13.sp else 17.sp,
                maxLines = if (compact) 1 else 2,
                overflow = TextOverflow.Ellipsis,
            )
            if (!compact) {
                face.extra?.let {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        it,
                        color = palette.soft,
                        fontSize = 13.sp,
                        lineHeight = 17.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            // A short widget drops these, and so does the real one: the
            // widget hides its dots below 92dp of height. A preview that
            // showed them anyway would be promising something.
            if (!compact && config.showPips && face.hasPips) {
                Spacer(Modifier.height(if (compact) 5.dp else 8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 6.dp)) {
                    repeat(face.pipsTotal.coerceAtMost(if (compact) 5 else 12)) { i ->
                        val on = i < face.pipsFilled
                        val d = if (compact) 5.dp else if (on) 10.dp else 8.dp
                        Box(
                            Modifier
                                .size(d)
                                .clip(CircleShape)
                                .background(if (on) palette.pipOn else palette.pipOff),
                        )
                    }
                }
            }
        }
    }
}

/**
 * Something to sit the card on.
 *
 * Not a photograph: a wallpaper of our choosing would flatter one setting
 * and sandbag another. A plain diagonal from dark to light shows a
 * translucent card doing what it does at both ends of the same preview.
 */
internal val Backdrop = Brush.linearGradient(
    listOf(
        androidx.compose.ui.graphics.Color(0xFF2B2F3A),
        androidx.compose.ui.graphics.Color(0xFF6B7280),
        androidx.compose.ui.graphics.Color(0xFFD9DEE7),
    ),
)

/** A small one, for a row in a list. */
@Composable
fun WidgetPreviewThumb(config: WidgetConfig, snapshot: WidgetSnapshot, modifier: Modifier = Modifier) {
    WidgetPreview(config, snapshot, modifier.width(132.dp).height(76.dp), compact = true)
}
