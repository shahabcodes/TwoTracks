package com.shahaabapps.twotracks.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
            val accentPalette by themeViewModel.accentPalette.collectAsStateWithLifecycle()
            AppThemeRoot(themeMode = themeMode, accentPalette = accentPalette) {
                WidgetConfigScreen(
                    appWidgetId = appWidgetId,
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
) {
    val context = LocalContext.current
    var config by remember { mutableStateOf(WidgetConfig()) }
    var loaded by remember { mutableStateOf(false) }
    // An existing widget opens on its own settings rather than the defaults.
    androidx.compose.runtime.LaunchedEffect(appWidgetId) {
        config = WidgetConfigStore.read(context, appWidgetId)
        loaded = true
    }

    DetailScaffold(title = "Widget", onBack = onCancel) {
        if (!loaded) return@DetailScaffold
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
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

            CupertinoListSection(header = "Look", footer = "A translucent widget takes the colour of the wallpaper behind it.") {
                // Without the width the card shrinks to its widest chip row
                // and sits narrower than every section above it.
                Column(
                    modifier = Modifier.fillMaxWidth().padding(Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                ) {
                    Text("Theme", style = MaterialTheme.typography.labelMedium, color = AppThemeRoot.colors.secondaryLabel)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        WidgetTheme.entries.forEach { t ->
                            Chip(
                                text = if (config.theme == t) "${t.label} ✓" else t.label,
                                onClick = { config = config.copy(theme = t) },
                            )
                        }
                    }
                    Text("Background", style = MaterialTheme.typography.labelMedium, color = AppThemeRoot.colors.secondaryLabel)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                        listOf(false to "Solid", true to "Translucent").forEach { (value, label) ->
                            Chip(
                                text = if (config.translucent == value) "$label ✓" else label,
                                onClick = { config = config.copy(translucent = value) },
                            )
                        }
                    }
                }
            }

            CupertinoPrimaryButton(
                text = "Add it",
                onClick = { onDone(config) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.listHorizontal),
            )
        }
    }
}

@Composable
private fun Chosen() {
    Text("✓", color = AppThemeRoot.colors.accent, style = MaterialTheme.typography.titleMedium)
}
