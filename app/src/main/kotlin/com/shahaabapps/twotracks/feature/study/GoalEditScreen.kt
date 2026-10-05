package com.shahaabapps.twotracks.feature.study

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoSwitch
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.SectionLabel
import com.shahaabapps.twotracks.ui.components.SegmentedChoice

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalEditScreen(
    goalId: String?,
    onDone: () -> Unit,
    viewModel: GoalEditViewModel = hiltViewModel(),
) {
    val s by viewModel.uiState.collectAsStateWithLifecycle()

    DetailScaffold(title = if (!s.isNew) "Edit goal" else if (s.levelId != null) "New goal for this level" else "New goal", onBack = onDone) {
        if (!s.loaded) return@DetailScaffold
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Spacing.listHorizontal).padding(bottom = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            CupertinoTextField(value = s.title, onValueChange = viewModel::onTitle, placeholder = "Title, e.g. AZ-305 Solutions Architect")
            CupertinoTextField(value = s.subtitle, onValueChange = viewModel::onSubtitle, placeholder = "Short line, e.g. Tier 2, fee USD 165")
            CupertinoTextField(value = s.why, onValueChange = viewModel::onWhy, placeholder = "Why it helps (optional)", singleLine = false)

            SectionLabel("Type", modifier = Modifier.padding(horizontal = 0.dp))
            SegmentedChoice(
                options = GoalKind.entries, selected = s.kind,
                label = { when (it) { GoalKind.LANGUAGE_TEST -> "Lang test"; GoalKind.LANGUAGE_LEVEL -> "Lang level"; GoalKind.CERTIFICATION -> "Cert"; GoalKind.CUSTOM -> "Other" } },
                onSelect = viewModel::onKind,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Target date (yyyy-mm-dd)", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    CupertinoTextField(value = s.targetDateIso, onValueChange = viewModel::onTargetDate, placeholder = "2027-03-31", keyboardType = KeyboardType.Number)
                }
                Column(modifier = Modifier.width(110.dp)) {
                    Text("Priority", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    CupertinoTextField(value = s.priority, onValueChange = viewModel::onPriority, placeholder = "50", keyboardType = KeyboardType.Number)
                }
            }
            Text("Lower priority number is scheduled first.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)


            if (s.levelId != null && s.isNew) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Required to finish the level")
                        Text("Optional goals stay on the level but do not take study slots.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    }
                    CupertinoSwitch(checked = s.requiredForLevel, onCheckedChange = viewModel::onRequiredForLevel)
                }
            }

            SectionLabel("Skills and hour budgets", modifier = Modifier.padding(horizontal = 0.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(Spacing.xs), verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Chip("IELTS 4 skills", onClick = { viewModel.applyPreset(listOf("Listening", "Reading", "Writing", "Speaking"), 40) })
                Chip("Language", onClick = { viewModel.applyPreset(listOf("Vocabulary", "Grammar", "Listening", "Speaking"), 30) })
                Chip("Exam domains", onClick = { viewModel.applyPreset(listOf("Domain 1", "Domain 2", "Domain 3", "Practice exams"), 15) })
            }
            s.components.forEachIndexed { i, c ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                    CupertinoTextField(value = c.name, onValueChange = { viewModel.onComponent(i, name = it) }, placeholder = "Skill", modifier = Modifier.weight(1f))
                    CupertinoTextField(value = c.hours, onValueChange = { viewModel.onComponent(i, hours = it.filter { ch -> ch.isDigit() || ch == '.' }) }, placeholder = "hours", keyboardType = KeyboardType.Decimal, modifier = Modifier.width(80.dp))
                    CupertinoTextField(value = c.targetScore, onValueChange = { viewModel.onComponent(i, target = it) }, placeholder = "target", modifier = Modifier.width(80.dp))
                    IconButton(onClick = { viewModel.removeComponent(i) }) { Icon(Icons.Filled.Close, "Remove", tint = AppTheme.colors.secondaryLabel) }
                }
            }
            CupertinoTextButton(text = "+ Add skill", onClick = viewModel::addComponent)

            s.error?.let { Text(it, color = AppTheme.colors.destructive, style = MaterialTheme.typography.bodySmall) }
            CupertinoPrimaryButton(text = if (s.saving) "Saving" else "Save", enabled = s.canSave, onClick = { viewModel.save(onDone) }, modifier = Modifier.fillMaxWidth())
            if (s.canDelete) CupertinoTextButton(text = "Delete goal", destructive = true, onClick = { viewModel.delete(onDone) })
        }
    }

    s.fit?.let { fit ->
        FitSheet(fit = fit, onChoose = { viewModel.choose(it, onDone) }, onDismiss = viewModel::dismissFit)
    }
}
