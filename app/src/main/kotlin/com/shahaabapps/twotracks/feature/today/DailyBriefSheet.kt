package com.shahaabapps.twotracks.feature.today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Brand
import com.shahaabapps.core.ui.theme.Shapes
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.SheetGrabber

/** What the brief says: built once a day by the Today view model. */
data class DailyBrief(
    val greeting: String,
    val dateLine: String,
    /** The headline: what to do today. */
    val task: String,
    val taskDetail: String,
    /** Why it matters, in order: this goal, this level, the move. */
    val reasons: List<Pair<ImageVector, String>>,
    val streakLine: String,
    val isRestDay: Boolean,
)

/**
 * The morning sheet: gradient header with today's one thing, then three lines
 * on how it moves the plan forward. Shown once a day on open.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyBriefSheet(
    brief: DailyBrief,
    onStart: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AppTheme.colors.groupedBackground,
        shape = Shapes.sheet,
        dragHandle = { SheetGrabber() },
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(bottom = Spacing.md).navigationBarsPadding()) {
            Column(
                modifier = Modifier
                    .padding(horizontal = Spacing.listHorizontal)
                    .fillMaxWidth()
                    .clip(Shapes.contentCard)
                    .background(Brand.accentGradient)
                    .padding(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs),
            ) {
                Text(brief.greeting, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
                Text(brief.task, style = MaterialTheme.typography.displayLarge, color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Filled.Schedule, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Text(brief.taskDetail, style = MaterialTheme.typography.bodyMedium, color = Color.White)
                }
                Text(brief.dateLine, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            }

            Text(
                "Why it matters",
                style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel,
                modifier = Modifier.padding(start = Spacing.listHorizontal + Spacing.sectionStart, top = Spacing.md, bottom = Spacing.sm),
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = Spacing.listHorizontal)
                    .fillMaxWidth()
                    .clip(Shapes.card)
                    .background(AppTheme.colors.card)
                    .padding(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                brief.reasons.forEach { (icon, text) ->
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(AppTheme.colors.accent.copy(alpha = 0.14f)), contentAlignment = Alignment.Center) {
                            Icon(icon, contentDescription = null, tint = AppTheme.colors.accentText, modifier = Modifier.size(16.dp))
                        }
                        Text(text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    }
                }
            }

            Text(
                brief.streakLine,
                style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = Spacing.listHorizontal + Spacing.sectionStart, vertical = Spacing.md),
            )
            Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                CupertinoPrimaryButton(text = if (brief.isRestDay) "Got it" else "Let's go", onClick = onStart)
                CupertinoTextButton(text = "Later", onClick = onDismiss)
            }
        }
    }
}

/** Icons the view model uses when composing reasons. */
object BriefIcons {
    val goal = Icons.Filled.TrendingUp
    val level = Icons.Filled.Flag
    val move = Icons.Filled.Bolt
    val learn = Icons.Filled.MenuBook
}
