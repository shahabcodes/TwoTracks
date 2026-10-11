package com.shahaabapps.twotracks.feature.settings

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.content.WidgetSnapshot
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.twotracks.widget.WidgetStore
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.widget.PlacedWidget
import com.shahaabapps.twotracks.widget.WidgetConfigActivity
import com.shahaabapps.twotracks.widget.placedWidgets

/**
 * The widgets on the home screen, and a way into each one's settings.
 *
 * A placed widget can already be reconfigured by holding it and tapping the
 * pencil, which is Android's own gesture and is how it is meant to work. It
 * is also invisible: nothing in the app said the settings existed, and
 * nothing says it on the home screen either until you long-press something
 * you have no reason to think is configurable. This is the signpost.
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
            snapshots = list.map { it.config.snapshotKey }.distinct()
                .associateWith { WidgetStore.read(context, it) }
        }
    }

    DetailScaffold(title = "Widgets", onBack = onBack) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(bottom = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            val list = placed
            when {
                list == null -> Unit
                list.isEmpty() -> CupertinoListSection(
                    header = "On your home screen",
                    footer = "Hold an empty part of the home screen, choose Widgets, and look for Two Tracks. " +
                        "It asks what to show as you drop it, and you can change your mind here afterwards.",
                ) {
                    CupertinoListRow("No widgets yet", subtitle = "Nothing of ours is on the home screen", showDivider = false)
                }
                else -> {
                    Text(
                        if (list.size == 1) "One widget" else "${list.size} widgets",
                        style = MaterialTheme.typography.labelMedium,
                        color = AppTheme.colors.secondaryLabel,
                        modifier = Modifier.padding(start = Spacing.sectionStart),
                    )
                    list.forEach { w ->
                        WidgetCard(
                            widget = w,
                            snapshot = snapshots[w.config.snapshotKey] ?: WidgetSnapshot(),
                            onClick = {
                                context.startActivity(
                                    Intent(context, WidgetConfigActivity::class.java)
                                        .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, w.appWidgetId),
                                )
                            },
                        )
                    }
                }
            }
            Text(
                "A widget shows what it was told to show when it was placed. Two of the same kind can follow " +
                    "different people or different courses, and holding one on the home screen and tapping the " +
                    "pencil opens the same settings as tapping it here.",
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.colors.secondaryLabel,
                modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
            )
        }
    }
}

/** One widget: what it looks like, beside what it is set to. */
@Composable
private fun WidgetCard(widget: PlacedWidget, snapshot: WidgetSnapshot, onClick: () -> Unit) {
    CupertinoCard(onClick = onClick, modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.md)) {
            WidgetPreviewThumb(widget.config, snapshot)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    widget.config.content.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    widget.summary(),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.secondaryLabel,
                )
            }
            CupertinoDisclosure()
        }
    }
}

/** "Mine · German A1 · dark", the settings said back in words. */
private fun PlacedWidget.summary(): String {
    val whose = config.owner.label
    val course = config.courseId?.let { id -> Courses.all.firstOrNull { it.id == id }?.title } ?: "Every course"
    val look = buildString {
        append(config.theme.label.lowercase())
        if (config.translucent) append(", translucent")
    }
    return "$whose · $course · $look"
}
