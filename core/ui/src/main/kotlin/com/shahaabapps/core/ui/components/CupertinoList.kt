package com.shahaabapps.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing

/**
 * A Parallel section: an optional mono uppercase header, a 1.5dp ink rule,
 * then rows separated by hairlines, all inside the page gutter. There is no
 * card behind it; the rule is what groups the rows.
 */
@Composable
fun CupertinoListSection(
    modifier: Modifier = Modifier,
    header: String? = null,
    footer: String? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = Spacing.listHorizontal)) {
        if (header != null) {
            SectionLabel(header, modifier = Modifier.padding(bottom = Spacing.sectionBottom))
        }
        HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
        Column { content() }
        if (footer != null) {
            Text(
                text = footer,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.colors.secondaryLabel,
                modifier = Modifier.padding(top = Spacing.sm + 2.dp),
            )
        }
    }
}

/** The 11sp mono uppercase label that heads a section or sits above a title. */
@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier, color: Color = AppTheme.colors.secondaryLabel) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = color,
        modifier = modifier.semantics { heading() },
    )
}

/**
 * A row within a [CupertinoListSection]: optional leading glyph, a label,
 * an optional second line, a trailing slot (value, switch, chevron), and the
 * full-width hairline beneath it.
 */
@Composable
fun CupertinoListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    iconColor: Color = AppTheme.colors.ink,
    showDivider: Boolean = true,
    onClick: (() -> Unit)? = null,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    trailing: @Composable (() -> Unit)? = null,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .heightIn(min = Spacing.rowMinHeight)
                .padding(vertical = 10.dp),
        ) {
            if (leadingIcon != null) {
                Icon(leadingIcon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = titleColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppTheme.colors.secondaryLabel,
                    )
                }
            }
            trailing?.invoke()
        }
        if (showDivider) {
            HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
        }
    }
}

/** The chevron on the trailing edge of a row that opens another screen. */
@Composable
fun CupertinoDisclosure(modifier: Modifier = Modifier) {
    Icon(
        Icons.AutoMirrored.Filled.KeyboardArrowRight,
        contentDescription = null,
        tint = AppTheme.colors.secondaryLabel,
        modifier = modifier.size(20.dp),
    )
}

/** Right-aligned secondary value text for a row (e.g. "Asia/Qatar"). */
@Composable
fun CupertinoValue(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = AppTheme.colors.secondaryLabel,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
