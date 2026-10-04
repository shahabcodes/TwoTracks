package com.shahaabapps.twotracks.feature.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.components.CupertinoValue
import com.shahaabapps.core.ui.theme.IosColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timeline
import com.shahaabapps.core.ui.components.ThemeSettingsSection
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.ThemeViewModel
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.TimezonePicker

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenAvailability: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val mode by themeViewModel.themeMode.collectAsStateWithLifecycle()
    val palette by themeViewModel.accentPalette.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val clipboard = LocalClipboardManager.current
    var editName by remember { mutableStateOf(false) }
    var confirmReset by remember { mutableStateOf(false) }
    val notifPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }

    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); viewModel.clearMessage() } }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) notifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    val s = state.session
    DetailScaffold(title = "Settings", onBack = onBack) {
        Box {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(bottom = Spacing.xl), verticalArrangement = Arrangement.spacedBy(Spacing.lg)) {
                if (s != null) {
                    CupertinoListSection(header = "Profile") {
                        CupertinoListRow("Name", leadingIcon = Icons.Filled.Person, iconColor = IosColors.Blue, onClick = { editName = true }, trailing = { CupertinoValue(s.me.name); CupertinoDisclosure() })
                        CupertinoListRow("Track", subtitle = viewModel.roleLabel(s.myRole), leadingIcon = Icons.Filled.Timeline, iconColor = IosColors.Indigo)
                        CupertinoListRow("Email", subtitle = state.email, leadingIcon = Icons.Filled.Mail, iconColor = IosColors.Gray)
                        var tz by remember(s.me.timezone) { mutableStateOf(s.me.timezone) }
                        Column(modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm)) {
                            Text("Timezone", style = MaterialTheme.typography.bodyLarge)
                            TimezonePicker(selected = tz, onSelect = { z -> tz = z; viewModel.saveProfile { p -> p.copy(timezone = z) } })
                        }
                    }

                    CupertinoListSection(
                        header = "Partner",
                        footer = if (s.partner == null) "Share this code with your partner. They choose Join in the app and enter it." else "You are both in the same space.",
                    ) {
                        CupertinoListRow(
                            "Invite code", subtitle = s.couple.inviteCode, leadingIcon = Icons.Filled.Link, iconColor = IosColors.Green, showDivider = s.partner != null,
                            trailing = { Chip(text = "Copy", onClick = { clipboard.setText(AnnotatedString(s.couple.inviteCode)) }) },
                        )
                        s.partner?.let { p -> CupertinoListRow(p.name, subtitle = "${viewModel.roleLabel(p.role)}, ${p.timezone}", leadingIcon = Icons.Filled.Favorite, iconColor = IosColors.Pink, showDivider = false) }
                    }

                    CupertinoListSection(
                        header = "Schedule",
                        footer = "Reminders fire at the start of each planned study slot and 15 minutes before a class, in your timezone.",
                    ) {
                        val study = state.availability.blocks.count { it.kind == com.shahaabapps.core.model.BlockKind.STUDY }
                        val cls = state.availability.blocks.count { it.kind == com.shahaabapps.core.model.BlockKind.CLASS }
                        CupertinoListRow(
                            "Weekly template",
                            leadingIcon = Icons.Filled.CalendarMonth, iconColor = IosColors.Orange,
                            subtitle = "$study study slots, $cls class slots, ${state.availability.weeklyStudyMinutes / 60}h ${state.availability.weeklyStudyMinutes % 60}m per week",
                            onClick = onOpenAvailability, trailing = { CupertinoDisclosure() }, showDivider = false,
                        )
                    }

                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        ThemeSettingsSection(mode = mode, palette = palette, onModeChange = themeViewModel::setThemeMode, onPaletteChange = themeViewModel::setAccentPalette)
                    }

                    CupertinoListSection(
                        header = "Guide content",
                        footer = "Restores goals, roadmap, countries, tests, documents and budget from the guide (version ${com.shahaabapps.core.seed.Seed.VERSION}). Logged hours and statuses on your own entries are kept; edits to seeded items are overwritten.",
                    ) {
                        CupertinoListRow(
                            if (state.seedOutdated) "Update to the latest guide content" else "Reset guide content",
                            leadingIcon = Icons.Filled.Refresh, iconColor = IosColors.Red,
                            onClick = { confirmReset = true }, showDivider = false, trailing = { CupertinoDisclosure() },
                        )
                    }
                }
                CupertinoTextButton(text = "Sign out", destructive = true, onClick = viewModel::signOut)
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    if (editName && s != null) {
        var name by remember { mutableStateOf(s.me.name) }
        AlertDialog(
            onDismissRequest = { editName = false },
            confirmButton = { TextButton(onClick = { viewModel.saveProfile { it.copy(name = name.trim()) }; editName = false }, enabled = name.isNotBlank()) { Text("Save") } },
            dismissButton = { TextButton(onClick = { editName = false }) { Text("Cancel") } },
            title = { Text("Your name") },
            text = { CupertinoTextField(value = name, onValueChange = { name = it }, placeholder = "Name", modifier = Modifier.fillMaxWidth()) },
        )
    }
    if (confirmReset) {
        AlertDialog(
            onDismissRequest = { confirmReset = false },
            confirmButton = { TextButton(onClick = { viewModel.resetSeed(); confirmReset = false }) { Text("Reset", color = AppTheme.colors.destructive) } },
            dismissButton = { TextButton(onClick = { confirmReset = false }) { Text("Cancel") } },
            title = { Text("Reset guide content?") },
            text = { Text("Seeded goals, milestones, country to-dos, tests, documents and budget lines go back to the guide defaults. Study logs, mock scores and sessions are kept.") },
        )
    }
}
