package com.shahaabapps.twotracks.feature.study

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.GoalKind
import com.shahaabapps.core.ui.components.CupertinoSwitch
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.LoadingOverlay
import java.util.Locale

@Composable
fun GoalEditScreen(
    goalId: String?,
    onDone: () -> Unit,
    viewModel: GoalEditViewModel = hiltViewModel(),
) {
    val s by viewModel.uiState.collectAsStateWithLifecycle()
    if (s.saving) LoadingOverlay("Saving")
    GoalEditContent(
        s, onCancel = onDone,
        onTitle = viewModel::onTitle, onWhy = viewModel::onWhy, onKind = viewModel::onKind,
        onTargetDate = viewModel::onTargetDate, onPriority = viewModel::onPriority, onRequiredForLevel = viewModel::onRequiredForLevel,
        onComponent = { i, name, hours, target -> viewModel.onComponent(i, name, hours, target) },
        onAddComponent = viewModel::addComponent, onRemoveComponent = viewModel::removeComponent, onPreset = viewModel::applyPreset,
        onSave = { viewModel.save(onDone) }, onDelete = { viewModel.delete(onDone) },
    )
    s.fit?.let { fit ->
        FitSheet(fit = fit, onChoose = { viewModel.choose(it, onDone) }, onDismiss = viewModel::dismissFit)
    }
}

private val kindLabels = listOf(
    GoalKind.LANGUAGE_TEST to "Language test",
    GoalKind.LANGUAGE_LEVEL to "Language level",
    GoalKind.CERTIFICATION to "Certification",
    GoalKind.CUSTOM to "Custom",
)

/**
 * The goal form, as the prototype draws it: Cancel and the title in a bar,
 * the title in large type over an ink line, why it matters, the kind as
 * pills, the date and priority side by side, then the parts and their hours
 * under an ink rule, and Save held at the foot.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GoalEditContent(
    s: GoalEditUiState,
    onCancel: () -> Unit,
    onTitle: (String) -> Unit,
    onWhy: (String) -> Unit,
    onKind: (GoalKind) -> Unit,
    onTargetDate: (String) -> Unit,
    onPriority: (String) -> Unit,
    onRequiredForLevel: (Boolean) -> Unit,
    onComponent: (Int, String?, String?, String?) -> Unit,
    onAddComponent: () -> Unit,
    onRemoveComponent: (Int) -> Unit,
    onPreset: (List<String>, Int) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
) {
    Scaffold(
        containerColor = AppTheme.colors.groupedBackground,
        bottomBar = {
            Column(modifier = Modifier.background(AppTheme.colors.groupedBackground).navigationBarsPadding().imePadding()) {
                HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                s.error?.let { Text(it, color = AppTheme.colors.destructive, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 10.dp)) }
                val shape = RoundedCornerShape(12.dp)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 12.dp, bottom = 24.dp).fillMaxWidth().height(52.dp).clip(shape)
                        .background(if (s.canSave && !s.saving) AppTheme.colors.accent else AppTheme.colors.separator)
                        .clickable(enabled = s.canSave && !s.saving, role = Role.Button, onClick = onSave),
                ) {
                    Text(if (s.saving) "Saving" else "Save", style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp), color = if (s.canSave && !s.saving) AppTheme.colors.onSolid else AppTheme.colors.secondaryLabel)
                }
            }
        },
    ) { inner ->
        Column(modifier = Modifier.fillMaxSize().padding(inner)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.listHorizontal).padding(top = 6.dp).heightIn(min = 44.dp)) {
                Box(Modifier.weight(1f)) {
                    Text(
                        "Cancel", style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable(role = Role.Button, onClick = onCancel).padding(vertical = 12.dp),
                    )
                }
                Text(
                    if (!s.isNew) "Edit goal" else if (s.levelId != null) "New goal for this level" else "New goal",
                    style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.semantics { heading() },
                )
                Box(Modifier.weight(1f))
            }
            if (!s.loaded) return@Column
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(horizontal = Spacing.listHorizontal).padding(top = 18.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                CupertinoTextField(
                    value = s.title, onValueChange = onTitle, label = "Title", placeholder = "AZ-305 Solutions Architect", strong = true,
                    textStyle = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
                )
                CupertinoTextField(value = s.why, onValueChange = onWhy, label = "Why it matters", placeholder = "What it opens up", singleLine = false)

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Label("Kind")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        kindLabels.forEach { (kind, label) ->
                            val on = s.kind == kind
                            val shape = RoundedCornerShape(19.dp)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.height(38.dp).clip(shape).background(if (on) AppTheme.colors.ink else Color.Transparent)
                                    .border(Spacing.rule, if (on) AppTheme.colors.ink else AppTheme.colors.separator, shape)
                                    .clickable(role = Role.RadioButton) { onKind(kind) }.padding(horizontal = 14.dp),
                            ) { Text(label, style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp), fontWeight = FontWeight.Medium, color = if (on) AppTheme.colors.groupedBackground else AppTheme.colors.ink) }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    val mono = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFamily, fontSize = 15.sp)
                    CupertinoTextField(value = s.targetDateIso, onValueChange = onTargetDate, label = "Target date", placeholder = "2027-03-31", keyboardType = KeyboardType.Number, textStyle = mono, modifier = Modifier.weight(1f))
                    CupertinoTextField(value = s.priority, onValueChange = onPriority, label = "Priority", placeholder = "50", keyboardType = KeyboardType.Number, textStyle = mono, modifier = Modifier.weight(1f))
                }

                if (s.levelId != null && s.isNew) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Required to finish the level", style = MaterialTheme.typography.bodyMedium)
                            Text("Optional goals stay on the level but do not take study slots.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                        }
                        CupertinoSwitch(checked = s.requiredForLevel, onCheckedChange = onRequiredForLevel)
                    }
                }

                Column {
                    HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)) {
                        Text("Parts and hours", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                        Text(totalHours(s.components).uppercase(Locale.ENGLISH), style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 12.sp), color = AppTheme.colors.secondaryLabel)
                    }
                    if (s.components.isEmpty()) {
                        // A new goal starts with nothing in it; these fill it in one tap.
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                            Link("IELTS 4 skills") { onPreset(listOf("Listening", "Reading", "Writing", "Speaking"), 40) }
                            Link("Language") { onPreset(listOf("Vocabulary", "Grammar", "Listening", "Speaking"), 30) }
                            Link("Exam domains") { onPreset(listOf("Domain 1", "Domain 2", "Domain 3", "Practice exams"), 15) }
                        }
                    }
                    val scored = s.kind == GoalKind.LANGUAGE_TEST
                    s.components.forEachIndexed { i, c ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.heightIn(min = 48.dp)) {
                            CupertinoTextField(value = c.name, onValueChange = { onComponent(i, it, null, null) }, placeholder = "Part", showLine = false, textStyle = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                            if (scored) {
                                CupertinoTextField(value = c.targetScore, onValueChange = { onComponent(i, null, null, it) }, placeholder = "band", showLine = false, textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFamily, textAlign = TextAlign.End), modifier = Modifier.width(52.dp))
                            }
                            CupertinoTextField(
                                value = c.hours, onValueChange = { onComponent(i, null, it.filter { ch -> ch.isDigit() || ch == '.' }, null) }, placeholder = "h",
                                keyboardType = KeyboardType.Decimal, showLine = false,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFamily, fontSize = 15.sp, textAlign = TextAlign.End), modifier = Modifier.width(64.dp),
                            )
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(18.dp)).clickable(role = Role.Button, onClickLabel = "Remove ${c.name}") { onRemoveComponent(i) },
                            ) { Icon(Icons.Filled.Close, contentDescription = "Remove ${c.name}", tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(16.dp)) }
                        }
                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    }
                    Box(Modifier.padding(top = 6.dp).heightIn(min = 44.dp), contentAlignment = Alignment.CenterStart) { Link("+ Add a part", onAddComponent) }
                }

                if (s.canDelete) {
                    Text(
                        "Delete this goal", style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp), color = AppTheme.colors.destructive,
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable(role = Role.Button, onClick = onDelete).padding(vertical = 12.dp),
                    )
                }
            }
        }
    }
}

/** "120 h total", from the hours typed so far. */
internal fun totalHours(parts: List<ComponentDraft>): String {
    val total = parts.sumOf { it.hours.toDoubleOrNull() ?: 0.0 }
    val text = if (total == Math.floor(total)) "%.0f".format(Locale.ENGLISH, total) else "%.1f".format(Locale.ENGLISH, total)
    return "$text h total"
}

@Composable
private fun Label(text: String) {
    Text(text.uppercase(Locale.ENGLISH), style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
}

@Composable
private fun Link(text: String, onClick: () -> Unit) {
    Text(
        text, style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp), color = AppTheme.colors.accentText,
        modifier = Modifier.clip(RoundedCornerShape(6.dp)).clickable(role = Role.Button, onClick = onClick).padding(vertical = 8.dp),
    )
}
