package com.shahaabapps.twotracks.changelog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.shahaabapps.core.ui.components.CupertinoCard
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.StatusChip
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Brand
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.SectionLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * "What's new" after an update: a gradient header with the version, the first
 * few bullets of the newest entry, and View more to the full history.
 */
@Composable
fun WhatsNewDialog(
    release: Release,
    onDismiss: () -> Unit,
    onViewMore: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.clip(Shapes.contentCard).background(AppTheme.colors.card)) {
            Column(
                modifier = Modifier.fillMaxWidth().background(Brand.accentGradient).padding(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.22f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Text("What's new", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                }
                Text("Version ${release.version}" + if (release.date.isNotBlank()) " · ${release.date}" else "", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.9f))
            }
            Column(modifier = Modifier.padding(Spacing.md), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                val shown = release.bullets.take(3)
                shown.forEach { b -> Bullet(b) }
                if (release.bullets.size > shown.size) {
                    Text("and ${release.bullets.size - shown.size} more", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                }
                CupertinoPrimaryButton(text = "Got it", onClick = onDismiss, modifier = Modifier.padding(top = Spacing.sm))
                CupertinoTextButton(text = "View full changelog", onClick = onViewMore)
            }
        }
    }
}

@Composable
private fun Bullet(text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(modifier = Modifier.padding(top = 8.dp, end = 10.dp).size(6.dp).clip(CircleShape).background(AppTheme.colors.accent))
        Text(text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
    }
}

@HiltViewModel
class ChangelogViewModel @Inject constructor(store: ChangelogStore) : ViewModel() {
    val releases: List<Release> = store.releases
    val current: String = store.currentVersion
}

/** The full version history, newest first. */
@Composable
fun ChangelogScreen(onBack: () -> Unit, viewModel: ChangelogViewModel = hiltViewModel()) {
    DetailScaffold(title = "Version history", onBack = onBack) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.md), contentPadding = PaddingValues(bottom = Spacing.xl)) {
            viewModel.releases.forEach { r ->
                item(key = r.version) {
                    CupertinoCard(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Version ${r.version}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            if (r.version == viewModel.current) StatusChip("Installed", AppTheme.colors.success)
                        }
                        if (r.date.isNotBlank()) Text(r.date, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                        r.sections.forEach { (heading, bullets) ->
                            SectionLabel(heading, modifier = Modifier.padding(top = Spacing.sm).padding(start = 0.dp))
                            bullets.forEach { Bullet(it) }
                        }
                    }
                }
            }
            if (viewModel.releases.isEmpty()) item { Text("No changelog bundled with this build.", modifier = Modifier.padding(Spacing.md), color = AppTheme.colors.secondaryLabel) }
        }
    }
}
