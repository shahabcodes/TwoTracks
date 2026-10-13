package com.shahaabapps.core.ui.components

import com.shahaabapps.core.ui.theme.displayCase
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing

/**
 * A top-level screen: an optional mono [eyebrow] above a 34sp page title,
 * flush with the gutter on the white page, and an optional trailing action
 * slot.
 */
@Composable
fun CupertinoScaffold(
    title: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = AppTheme.colors.groupedBackground,
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = Spacing.md, bottom = Spacing.sm),
            ) {
                if (eyebrow != null) SectionLabel(eyebrow, modifier = Modifier.padding(bottom = 6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = displayCase(title),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.weight(1f).semantics { heading() },
                    )
                    actions()
                }
            }
            content(PaddingValues(0.dp))
        }
    }
}
