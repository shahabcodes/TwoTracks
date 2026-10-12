package com.shahaabapps.twotracks.changelog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

private val dayMonth = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)

/** "12 Oct" from the changelog's "2026-10-12", or the text as it is. */
internal fun shortDate(iso: String): String = runCatching { LocalDate.parse(iso.trim()).format(dayMonth) }.getOrDefault(iso)

/**
 * "What's new" after an update, in the page's own type: which version this
 * is, its lines under an ink rule, and Got it.
 */
@Composable
fun WhatsNewDialog(
    release: Release,
    onDismiss: () -> Unit,
    onViewMore: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.clip(Shapes.contentCard).background(AppTheme.colors.groupedBackground).padding(24.dp)) {
            Text(
                "Version ${release.version}" + if (release.date.isNotBlank()) " · ${shortDate(release.date)}" else "",
                style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel,
            )
            Text("What's new", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 6.dp, bottom = 14.dp))
            HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
            val shown = release.bullets.take(3)
            shown.forEach { b ->
                Text(b, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 12.dp))
                HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
            }
            if (release.bullets.size > shown.size) {
                Text("and ${release.bullets.size - shown.size} more", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 8.dp))
            }
            CupertinoPrimaryButton(text = "Got it", onClick = onDismiss, modifier = Modifier.padding(top = 18.dp))
            CupertinoTextButton(text = "Every version", onClick = onViewMore)
        }
    }
}

@HiltViewModel
class ChangelogViewModel @Inject constructor(store: ChangelogStore) : ViewModel() {
    val releases: List<Release> = store.releases
    val current: String = store.currentVersion
}

@Composable
fun ChangelogScreen(onBack: () -> Unit, viewModel: ChangelogViewModel = hiltViewModel()) {
    ChangelogContent(viewModel.releases, viewModel.current, onBack)
}

/**
 * Every version, newest first, as the prototype draws it: the version and
 * its date in a narrow column, the line that says what changed beside it.
 */
@Composable
fun ChangelogContent(releases: List<Release>, current: String, onBack: () -> Unit) {
    DetailScaffold(title = "What's new", onBack = onBack, backLabel = "Back to settings", eyebrow = "You have $current") {
        LazyColumn(contentPadding = PaddingValues(bottom = 40.dp)) {
            item { HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 12.dp)) }
            items(releases, key = { it.version }) { r ->
                Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(vertical = 14.dp)) {
                        Column(modifier = Modifier.width(64.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                r.version, style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 13.sp), fontWeight = FontWeight.Medium,
                                color = if (r.version == current) AppTheme.colors.accentText else AppTheme.colors.ink,
                            )
                            if (r.date.isNotBlank()) Text(shortDate(r.date), style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = AppTheme.colors.secondaryLabel)
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            r.bullets.forEach { Text(it, style = MaterialTheme.typography.bodyMedium) }
                        }
                    }
                    HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                }
            }
            if (releases.isEmpty()) item { Text("No changelog bundled with this build.", modifier = Modifier.padding(Spacing.listHorizontal), color = AppTheme.colors.secondaryLabel) }
        }
    }
}
