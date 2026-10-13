package com.shahaabapps.twotracks.feature.settings

import androidx.compose.ui.unit.em
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardOutlineButton
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardBack
import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.WidgetSnapshot
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.widget.PlacedWidget
import com.shahaabapps.twotracks.widget.WidgetConfigActivity
import com.shahaabapps.twotracks.widget.WidgetContent
import com.shahaabapps.twotracks.widget.WidgetStore
import com.shahaabapps.twotracks.widget.placedWidgets
import java.util.Locale

/**
 * The widgets on the home screen, and a way into each one's settings.
 *
 * A placed widget can already be reconfigured by holding it and tapping the
 * pencil, which is Android's own gesture. It is also invisible: nothing in
 * the app said the settings existed. This is the signpost.
 *
 * It lists what is actually placed rather than what could be, because a
 * widget's settings belong to one square on one home screen: two Together
 * widgets can show different people.
 */
@Composable
fun WidgetsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var placed by remember { mutableStateOf<List<PlacedWidget>?>(null) }
    var snapshots by remember { mutableStateOf<Map<String, WidgetSnapshot>>(emptyMap()) }

    // Re-read on resume: the way out of here is the configuration screen, and
    // coming back to a row that still shows what it used to reads as a change
    // that did not take.
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            val list = placedWidgets(context)
            placed = list
            snapshots = list.map { it.config.snapshotKey }.distinct().associateWith { WidgetStore.read(context, it) }
        }
    }
    WidgetsContent(placed, snapshots, onBack = onBack, onChange = { w ->
        context.startActivity(Intent(context, WidgetConfigActivity::class.java).putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, w.appWidgetId))
    })
}

/**
 * As the prototype draws it: what is on the home screen, drawn on a
 * wallpaper two to a row, then each with what it is set to and Change, then
 * the kinds there are and how to add one.
 */
@Composable
fun WidgetsContent(placed: List<PlacedWidget>?, snapshots: Map<String, WidgetSnapshot>, onBack: () -> Unit, onChange: (PlacedWidget) -> Unit) {
    if (AppTheme.departures) { BoardWidgets(placed, snapshots, onBack, onChange); return }
    DetailScaffold(title = "Widgets", onBack = onBack, backLabel = "Back to settings") {
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(bottom = 40.dp)) {
            Text(
                "A widget shows what it was told to show when you placed it. Two of the same kind can follow different people.",
                style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
            )
            val list = placed
            if (list != null) {
                Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 22.dp)) {
                    Heading("On your home screen · ${list.size}")
                    if (list.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(top = 6.dp).fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Backdrop).padding(16.dp),
                        ) {
                            list.chunked(2).forEach { pair ->
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    pair.forEach { w ->
                                        WidgetPreview(
                                            w.config, snapshots[w.config.snapshotKey] ?: WidgetSnapshot(), backdrop = false,
                                            modifier = Modifier.weight(1f).aspectRatio(1f).clickable(role = Role.Button, onClickLabel = "Change") { onChange(w) },
                                        )
                                    }
                                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
                Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 18.dp)) {
                    HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                    if (list.isEmpty()) {
                        Text("Nothing of ours is on the home screen yet.", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(vertical = 16.dp))
                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    }
                    list.forEach { w ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp)) {
                            Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(w.config.content.label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                Text(w.summary(), style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
                            }
                            val shape = RoundedCornerShape(18.dp)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.height(36.dp).clip(shape).border(Spacing.rule, AppTheme.colors.separator, shape)
                                    .clickable(role = Role.Button) { onChange(w) }.padding(horizontal = 14.dp),
                            ) { Text("Change", style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp), color = AppTheme.colors.ink) }
                        }
                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    }
                }
            }
            Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 26.dp)) {
                Heading("Kinds you can add")
                HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                WidgetContent.entries.forEach { k ->
                    Column(modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp).padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)) {
                        Text(k.label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                        Text(k.blurb, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
                    }
                    HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                }
                Text(
                    "Hold an empty part of the home screen, choose Widgets, and look for Two Tracks. It asks what to show as you drop it. Holding one and tapping the pencil opens the same settings as Change.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 12.dp),
                )
            }
        }
    }
}

@Composable
private fun Heading(text: String) {
    Text(text.uppercase(Locale.ENGLISH), style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp), color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(bottom = 4.dp).semantics { heading() })
}

/** "Mine · German A1 · dark", the settings said back in words. */
internal fun PlacedWidget.summary(): String {
    val whose = config.owner.label
    val course = config.courseId?.let { id -> Courses.all.firstOrNull { it.id == id }?.title } ?: "Every course"
    val look = buildString {
        append(config.theme.label.lowercase())
        if (config.translucent) append(", translucent")
    }
    return "$whose · $course · $look"
}

/**
 * Widgets on the Departures board, as its prototype draws them: what is on
 * the home screen on its wallpaper, each with what it is set to and CHANGE,
 * and the kinds there are. The previews are the widgets themselves, which
 * keep their own look whatever the app is drawn in.
 */
@Composable
private fun BoardWidgets(placed: List<PlacedWidget>?, snapshots: Map<String, WidgetSnapshot>, onBack: () -> Unit, onChange: (PlacedWidget) -> Unit) {
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        Column(modifier = Modifier.fillMaxSize().padding(inner).verticalScroll(rememberScrollState()).padding(bottom = 40.dp)) {
            Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to settings") }
                BoardTitle("Widgets", size = 52.sp)
                BoardText("A small board on your home screen. It shows what it was told to show when you placed it, and two of the same kind can follow different people.")
            }
            val list = placed
            if (list != null) {
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 16.dp)) {
                    BoardLabel("On your home screen · ${list.size}", size = 13.sp, spacing = 0.12.em, modifier = Modifier.padding(bottom = 8.dp).semantics { heading() })
                    if (list.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Backdrop).padding(14.dp),
                        ) {
                            list.chunked(2).forEach { pair ->
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    pair.forEach { w ->
                                        WidgetPreview(
                                            w.config, snapshots[w.config.snapshotKey] ?: WidgetSnapshot(), backdrop = false,
                                            modifier = Modifier.weight(1f).aspectRatio(1f).clickable(role = Role.Button, onClickLabel = "Change") { onChange(w) },
                                        )
                                    }
                                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
                Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 16.dp)) {
                    if (list.isEmpty()) {
                        BoardText("Nothing of ours is on the home screen yet.", modifier = Modifier.padding(vertical = 16.dp))
                        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                    }
                    list.forEach { w ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth().heightIn(min = 60.dp)) {
                            Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                BoardText(w.config.content.label, color = AppTheme.colors.ink, weight = FontWeight.Medium)
                                BoardMono(w.summary().uppercase(Locale.ENGLISH), spacing = 0.sp)
                            }
                            BoardOutlineButton("Change", onClick = { onChange(w) }, height = 36.dp)
                        }
                        Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                    }
                }
            }
            Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 20.dp)) {
                BoardLabel("Kinds you can add", size = 13.sp, spacing = 0.12.em, modifier = Modifier.padding(bottom = 6.dp).semantics { heading() })
                Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                WidgetContent.entries.forEach { k ->
                    Column(modifier = Modifier.fillMaxWidth().heightIn(min = 58.dp).padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)) {
                        BoardText(k.label, color = AppTheme.colors.ink, weight = FontWeight.Medium)
                        BoardText(k.blurb, size = 12.sp)
                    }
                    Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
                }
                BoardText(
                    "Hold an empty part of the home screen, choose Widgets, and look for Two Tracks. It asks what to show as you drop it. Holding one and tapping the pencil opens the same settings as Change.",
                    size = 12.sp, modifier = Modifier.padding(top = 12.dp),
                )
            }
        }
    }
}
