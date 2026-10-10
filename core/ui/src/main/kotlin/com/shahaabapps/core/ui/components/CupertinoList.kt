package com.shahaabapps.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing

/**
 * A grouped, inset-rounded list section in the iOS Settings style, with an
 * optional uppercase header and footer caption (Browse Safe SettingsSectionTitle).
 */
@Composable
fun CupertinoListSection(
    modifier: Modifier = Modifier,
    header: String? = null,
    footer: String? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (header != null) {
            Text(
                text = header.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = AppTheme.colors.secondaryLabel,
                modifier = Modifier.padding(
                    start = Spacing.listHorizontal + Spacing.sectionStart,
                    end = Spacing.listHorizontal,
                    bottom = Spacing.sectionBottom,
                ),
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = Spacing.listHorizontal)
                .clip(Shapes.card)
                .background(AppTheme.colors.card),
            content = { content() },
        )
        if (footer != null) {
            Text(
                text = footer,
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.colors.secondaryLabel,
                modifier = Modifier.padding(
                    start = Spacing.listHorizontal + Spacing.sectionStart,
                    end = Spacing.listHorizontal,
                    top = Spacing.sectionBottom,
                ),
            )
        }
    }
}

/**
 * A single row within a [CupertinoListSection], modelled on the browser app's
 * HubRow: optional 29dp coloured icon tile, 17sp label, optional subtitle,
 * trailing slot (value text, switch, chevron), and a hairline divider inset
 * to the label's start.
 */
@Composable
fun CupertinoListRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leadingIcon: ImageVector? = null,
    iconColor: Color = AppTheme.colors.accentText,
    showDivider: Boolean = true,
    onClick: (() -> Unit)? = null,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    trailing: @Composable (() -> Unit)? = null,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
                .heightIn(min = Spacing.rowMinHeight)
                .padding(start = 12.dp, end = 14.dp, top = 8.dp, bottom = 8.dp),
        ) {
            if (leadingIcon != null) {
                IconTile(icon = leadingIcon, color = iconColor)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f).padding(start = if (leadingIcon == null) 4.dp else 0.dp),
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
            HorizontalDivider(
                color = AppTheme.colors.separator,
                thickness = 0.5.dp,
                modifier = Modifier.padding(start = if (leadingIcon != null) Spacing.dividerInsetWithIcon else Spacing.dividerInset),
            )
        }
    }
}

/** The iOS "›" disclosure glyph used on the trailing edge of a pushing row. */
@Composable
fun CupertinoDisclosure(modifier: Modifier = Modifier) {
    Text(
        text = "›",
        color = AppTheme.colors.secondaryLabel,
        fontSize = 22.sp,
        modifier = modifier.padding(start = 4.dp),
    )
}

/** Right-aligned secondary value text for a row (e.g. "Asia/Qatar"). */
@Composable
fun CupertinoValue(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = AppTheme.colors.secondaryLabel,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
