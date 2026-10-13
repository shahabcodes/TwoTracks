package com.shahaabapps.twotracks.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.content.WidgetSnapshot
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.twotracks.feature.settings.WidgetPreview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.theme.AppTheme as AppThemeRoot
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.ThemeViewModel
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * What a widget shows, asked when it is dropped on the home screen.
 *
 * The launcher starts this before the widget exists and waits for a result:
 * cancelling leaves nothing placed, which is why the result is set to
 * cancelled up front and only replaced once something is chosen.
 *
 * It is also reachable afterwards from the widget itself, so a widget can be
 * changed without being deleted and put back.
 */
@AndroidEntryPoint
class WidgetConfigActivity : ComponentActivity() {

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID,
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        // Backing out must leave no widget behind.
        setResult(Activity.RESULT_CANCELED, Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId))
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) { finish(); return }

        setContent {
            val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()
            val look by themeViewModel.look.collectAsStateWithLifecycle()
            AppThemeRoot(themeMode = themeMode, look = look) {
                WidgetConfigScreen(
                    appWidgetId = appWidgetId,
                    existing = intent?.action != AppWidgetManager.ACTION_APPWIDGET_CONFIGURE,
                    onDone = { config ->
                        lifecycleScope.launch {
                            WidgetConfigStore.write(this@WidgetConfigActivity, appWidgetId, config)
                            // Draw it before handing back, or the launcher
                            // shows the loading layout until something else
                            // happens to refresh it.
                            runCatching { config.content.widget().update(this@WidgetConfigActivity, appWidgetId) }
                            setResult(
                                Activity.RESULT_OK,
                                Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId),
                            )
                            finish()
                        }
                    },
                    onCancel = { finish() },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WidgetConfigScreen(
    appWidgetId: Int,
    onDone: (WidgetConfig) -> Unit,
    onCancel: () -> Unit,
    existing: Boolean,
) {
    val context = LocalContext.current
    var config by remember { mutableStateOf(WidgetConfig()) }
    var loaded by remember { mutableStateOf(false) }
    // An existing widget opens on its own settings rather than the defaults.
    LaunchedEffect(appWidgetId) {
        config = WidgetConfigStore.read(context, appWidgetId)
        loaded = true
    }

    // The preview shows real numbers, not a mock-up: changing whose progress
    // it follows is also how you find out whether they have studied today.
    var snapshot by remember { mutableStateOf(WidgetSnapshot()) }
    LaunchedEffect(config.snapshotKey) {
        snapshot = WidgetStore.read(context, config.snapshotKey)
    }

    DetailScaffold(title = if (existing) "This widget" else "Widget", onBack = onCancel) {
        if (!loaded) return@DetailScaffold
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            WidgetPreview(
                config = config,
                snapshot = snapshot,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.listHorizontal)
                    .height(188.dp),
            )

            CupertinoListSection(header = "Shows", footer = config.content.blurb) {
                WidgetContent.entries.forEachIndexed { i, c ->
                    CupertinoListRow(
                        c.label,
                        showDivider = i < WidgetContent.entries.lastIndex,
                        onClick = { config = config.copy(content = c) },
                        trailing = { if (config.content == c) Chosen() },
                    )
                }
            }

            CupertinoListSection(header = "Whose", footer = "A widget can watch your partner's progress instead of your own.") {
                WidgetOwner.entries.forEachIndexed { i, o ->
                    CupertinoListRow(
                        o.label,
                        showDivider = i < WidgetOwner.entries.lastIndex,
                        onClick = { config = config.copy(owner = o) },
                        trailing = { if (config.owner == o) Chosen() },
                    )
                }
            }

            CupertinoListSection(
                header = "Course",
                footer = "Every course at once, or hold this widget to one of them.",
            ) {
                CupertinoListRow(
                    "Every course",
                    onClick = { config = config.copy(courseId = null) },
                    trailing = { if (config.courseId == null) Chosen() },
                )
                Courses.all.forEachIndexed { i, course ->
                    CupertinoListRow(
                        course.title,
                        showDivider = i < Courses.all.lastIndex,
                        onClick = { config = config.copy(courseId = course.id) },
                        trailing = { if (config.courseId == course.id) Chosen() },
                    )
                }
            }

            CupertinoListSection(
                header = "Look",
                footer = "A translucent widget takes the colour of the wallpaper behind it. " +
                    "Round corners match most launchers; square ones match the rest. The dots count " +
                    "what is done out of what is due, and a widget too short for them leaves them out " +
                    "— the preview above is drawn at a comfortable height.",
            ) {
                // Without the width the card shrinks to its widest chip row
                // and sits narrower than every section above it.
                Column(
                    modifier = Modifier.fillMaxWidth().padding(Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                ) {
                    Picker("Theme", WidgetTheme.entries, config.theme, { it.label }) { config = config.copy(theme = it) }
                    Picker(
                        "Background",
                        listOf(false, true),
                        config.translucent,
                        { if (it) "Translucent" else "Solid" },
                    ) { config = config.copy(translucent = it) }
                    Picker("Corners", WidgetCorners.entries, config.corners, { it.label }) { config = config.copy(corners = it) }
                    Picker("Dots", listOf(true, false), config.showPips, { if (it) "Show" else "Hide" }) {
                        config = config.copy(showPips = it)
                    }
                    // Only the dots take a colour, so the choice disappears
                    // with them rather than sitting there doing nothing. The
                    // words keep the ink the theme gives them: an accent
                    // readable on a white card is not readable on a black one,
                    // and a widget has no contrast test watching it.
                    if (config.showPips) {
                        Text("Dot colour", style = MaterialTheme.typography.labelMedium, color = AppThemeRoot.colors.secondaryLabel)
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                            WidgetAccent.entries.forEach { a ->
                                AccentChip(a, chosen = config.accent == a) { config = config.copy(accent = a) }
                            }
                        }
                    }
                }
            }

            CupertinoPrimaryButton(
                text = if (existing) "Save" else "Add it",
                onClick = { onDone(config) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.listHorizontal),
            )
        }
    }
}

/** A labelled row of chips, one of which is chosen. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> Picker(
    label: String,
    options: List<T>,
    chosen: T,
    text: (T) -> String,
    onPick: (T) -> Unit,
) {
    Text(label, style = MaterialTheme.typography.labelMedium, color = AppThemeRoot.colors.secondaryLabel)
    FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
        options.forEach { option ->
            Chip(
                text = if (option == chosen) "${text(option)} ✓" else text(option),
                onClick = { onPick(option) },
            )
        }
    }
}

/** A colour you can see, rather than the name of one. */
@Composable
private fun AccentChip(accent: WidgetAccent, chosen: Boolean, onPick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clip(Shapes.field)
            .background(if (chosen) AppThemeRoot.colors.accent.copy(alpha = 0.18f) else AppThemeRoot.colors.fill)
            .clickable(onClick = onPick)
            .padding(horizontal = 10.dp, vertical = 7.dp),
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(accent.color ?: AppThemeRoot.colors.secondaryLabel),
        )
        Text(
            if (chosen) "${accent.label} ✓" else accent.label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = AppThemeRoot.colors.accentText,
        )
    }
}

@Composable
private fun Chosen() {
    Text("✓", color = AppThemeRoot.colors.accentText, style = MaterialTheme.typography.titleMedium)
}
