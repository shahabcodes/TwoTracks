package com.shahaabapps.twotracks.feature.settings

import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import com.shahaabapps.twotracks.ui.components.SegmentedChoice
import com.shahaabapps.twotracks.feature.auth.MascotState
import com.shahaabapps.twotracks.feature.auth.CompanionKind
import com.shahaabapps.twotracks.feature.auth.Companion
import com.shahaabapps.twotracks.ui.components.DialogButton
import com.shahaabapps.twotracks.reminders.ReminderNotifications
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.LocalGuide
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import com.shahaabapps.twotracks.ui.guide.guideScrollHost
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.StartDateDialog
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoSwitch
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.components.CupertinoValue
import com.shahaabapps.core.ui.theme.IosColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timeline
import com.shahaabapps.core.ui.components.ThemeSettingsSection
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.ThemeViewModel
import com.shahaabapps.twotracks.ui.components.Chip
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.TimezoneDialog

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenAvailability: () -> Unit,
    onOpenChangelog: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val mode by themeViewModel.themeMode.collectAsStateWithLifecycle()
    val palette by themeViewModel.accentPalette.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val clipboard = LocalClipboardManager.current
    var editName by remember { mutableStateOf(false) }
    var editTimezone by remember { mutableStateOf(false) }
    var editStartDate by remember { mutableStateOf(false) }
    var confirmReset by remember { mutableStateOf(false) }
    var changePassword by remember { mutableStateOf(false) }
    val notifPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { }
    val context = LocalContext.current
    // Re-read on resume: the switch is thrown in the system's own settings and
    // the app comes back to a row that would otherwise still read "Off".
    var fullScreenAllowed by remember { mutableStateOf(ReminderNotifications.canTakeOverScreen(context)) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) fullScreenAllowed = ReminderNotifications.canTakeOverScreen(context)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); viewModel.clearMessage() } }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) notifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    val s = state.session
    DetailScaffold(title = "Settings", onBack = onBack) {
        Box {
            val pageScroll = rememberScrollState()
                Column(modifier = Modifier.guideScrollHost(pageScroll).verticalScroll(pageScroll).padding(bottom = Spacing.xl), verticalArrangement = Arrangement.spacedBy(Spacing.md)) {
                if (s != null) {
                    CupertinoListSection(header = "Profile") {
                        CupertinoListRow("Name", modifier = Modifier.guideAnchor(GuideAnchors.SET_NAME), leadingIcon = Icons.Filled.Person, iconColor = IosColors.Blue, onClick = { editName = true }, trailing = { CupertinoValue(s.me.name); CupertinoDisclosure() })
                        CupertinoListRow("Track", modifier = Modifier.guideAnchor(GuideAnchors.SET_TRACK), subtitle = viewModel.roleLabel(s.myRole), leadingIcon = Icons.Filled.Timeline, iconColor = IosColors.Indigo)
                        CupertinoListRow("Email", subtitle = state.email, leadingIcon = Icons.Filled.Mail, iconColor = IosColors.Gray)
                        CupertinoListRow("Password", modifier = Modifier.guideAnchor(GuideAnchors.SET_PASSWORD), subtitle = "Change it here or get a reset link by email", leadingIcon = Icons.Filled.Lock, iconColor = IosColors.Orange, onClick = { changePassword = true }, trailing = { CupertinoDisclosure() })
                        CupertinoListRow(
                            "Timezone",
                            modifier = Modifier.guideAnchor(GuideAnchors.SET_TIMEZONE),
                            leadingIcon = Icons.Filled.Public, iconColor = IosColors.Teal, showDivider = false,
                            onClick = { editTimezone = true },
                            trailing = { CupertinoValue(s.me.timezone); CupertinoDisclosure() },
                        )
                    }

                    CupertinoListSection(
                        header = "Partner",
                        footer = if (s.partner == null) "Share this code with your partner. They choose Join in the app and enter it." else "You are both in the same space.",
                    ) {
                        CupertinoListRow(
                            "Invite code",
                            modifier = Modifier.guideAnchor(GuideAnchors.SET_INVITE),
                            subtitle = s.couple.inviteCode, leadingIcon = Icons.Filled.Link, iconColor = IosColors.Green, showDivider = s.partner != null,
                            trailing = { Chip(text = "Copy", onClick = { clipboard.setText(AnnotatedString(s.couple.inviteCode)) }) },
                        )
                        s.partner?.let { p -> CupertinoListRow(p.name, subtitle = "${viewModel.roleLabel(p.role)}, ${p.timezone}", leadingIcon = Icons.Filled.Favorite, iconColor = IosColors.Pink, showDivider = false) }
                    }

                    CupertinoListSection(
                        header = "Schedule",
                        footer = "Reminders fire at the start of each planned study slot and 15 minutes before a class, in your timezone. " +
                            "Nothing before your start date is counted as a day you missed.",
                    ) {
                        val study = state.availability.blocks.count { it.kind == com.shahaabapps.core.model.BlockKind.STUDY }
                        val cls = state.availability.blocks.count { it.kind == com.shahaabapps.core.model.BlockKind.CLASS }
                        CupertinoListRow(
                            "Weekly template",
                            modifier = Modifier.guideAnchor(GuideAnchors.SET_TEMPLATE),
                            leadingIcon = Icons.Filled.CalendarMonth, iconColor = IosColors.Orange,
                            subtitle = "$study study slots, $cls class slots, ${state.availability.weeklyStudyMinutes / 60}h ${state.availability.weeklyStudyMinutes % 60}m per week",
                            onClick = onOpenAvailability, trailing = { CupertinoDisclosure() },
                        )
                        CupertinoListRow(
                            "Start date",
                            modifier = Modifier.guideAnchor(GuideAnchors.SET_START_DATE),
                            leadingIcon = Icons.Filled.PlayCircleOutline, iconColor = IosColors.Green,
                            subtitle = if (s.me.startDateIso == null) "Not set. The plan's own first day is used." else "The day your study counts from",
                            onClick = { editStartDate = true },
                            trailing = { CupertinoValue(s.me.startDateIso ?: "Not set"); CupertinoDisclosure() },
                        )
                        // Only worth offering where the system can be asked. Below
                        // Android 14 declaring the permission is enough and there
                        // is no page to send anyone to.
                        val fullScreenIntent = remember { ReminderNotifications.fullScreenSettingsIntent(context) }
                        if (fullScreenIntent != null) {
                            CupertinoListRow(
                                "Reminders take over the screen",
                                modifier = Modifier.guideAnchor(GuideAnchors.SET_FULL_SCREEN),
                                leadingIcon = Icons.Filled.Alarm, iconColor = IosColors.Red, showDivider = false,
                                subtitle = if (fullScreenAllowed) {
                                    "A reminder opens in front of whatever you are doing"
                                } else {
                                    "Off. Reminders arrive as a banner instead."
                                },
                                onClick = { context.startActivity(fullScreenIntent) },
                                trailing = { CupertinoValue(if (fullScreenAllowed) "On" else "Off"); CupertinoDisclosure() },
                            )
                        }
                    }

                    CupertinoListSection(
                        header = "Privacy",
                        footer = if (state.biometricAvailable) "Asks for your fingerprint, face or device PIN when the app opens and after it has been in the background for 30 seconds."
                        else "Set up a fingerprint, face unlock or screen lock on this phone to use this.",
                    ) {
                        CupertinoListRow(
                            "Unlock with biometrics",
                            modifier = Modifier.guideAnchor(GuideAnchors.SET_BIOMETRICS),
                            leadingIcon = Icons.Filled.Fingerprint, iconColor = IosColors.Green, showDivider = false,
                            trailing = { CupertinoSwitch(checked = state.biometricEnabled, onCheckedChange = viewModel::setBiometricLock, enabled = state.biometricAvailable) },
                        )
                    }

                    Column(modifier = Modifier.padding(horizontal = Spacing.listHorizontal)) {
                        ThemeSettingsSection(mode = mode, palette = palette, onModeChange = themeViewModel::setThemeMode, onPaletteChange = themeViewModel::setAccentPalette)
                        val companion by themeViewModel.companion.collectAsStateWithLifecycle()
                        Text("SIGN-IN COMPANION", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = Spacing.md, start = Spacing.sectionStart).guideAnchor(GuideAnchors.SET_COMPANION))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Spacing.md), modifier = Modifier.padding(top = Spacing.sm)) {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                                SegmentedChoice(options = listOf(CompanionKind.BUDDY, CompanionKind.PLANE), selected = companion, label = { it.label }, onSelect = themeViewModel::setCompanion)
                                SegmentedChoice(options = listOf(CompanionKind.SANYA, CompanionKind.USAID), selected = companion, label = { it.label }, onSelect = themeViewModel::setCompanion)
                            }
                            Box(modifier = Modifier.size(width = 120.dp, height = 150.dp), contentAlignment = Alignment.Center) {
                                Companion(kind = companion, state = MascotState(mood = 0.8f), modifier = Modifier.fillMaxSize())
                            }
                        }
                        Text("Lives on the sign-in screen and reacts to what you type.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = Spacing.xs, start = Spacing.sectionStart))
                    }

                    CupertinoListSection(
                        header = "Guide content",
                        footer = "Restores goals, roadmap, countries, tests, documents and budget from the guide (version ${com.shahaabapps.core.seed.Seed.VERSION}). Level 1 starts today and every later date moves with it. Logged hours and statuses on your own entries are kept; edits to seeded items are overwritten.",
                    ) {
                        CupertinoListRow(
                            if (state.seedOutdated) "Update to the latest guide content" else "Reset guide content",
                            modifier = Modifier.guideAnchor(GuideAnchors.SET_SEED),
                            leadingIcon = Icons.Filled.Refresh, iconColor = IosColors.Red,
                            onClick = { confirmReset = true }, showDivider = false, trailing = { CupertinoDisclosure() },
                        )
                    }
                }
                // The voice each language is read in. Its own view model, so
                // the list is loaded once per visit rather than on every
                // recomposition of this screen.
                Box(modifier = Modifier.guideAnchor(GuideAnchors.SET_VOICES)) { VoiceSettingsSections(hiltViewModel()) }
                CupertinoListSection(header = "About") {
                    val guide = LocalGuide.current
                    if (guide != null) {
                        CupertinoListRow(
                            "Show me around",
                            modifier = Modifier.guideAnchor(GuideAnchors.SET_TOUR),
                            subtitle = "A short tour of the app, over the real screens",
                            leadingIcon = Icons.Filled.Explore, iconColor = IosColors.Teal,
                            onClick = guide::start,
                            trailing = { CupertinoDisclosure() },
                        )
                    }
                    CupertinoListRow(
                        "Version history", subtitle = "Version ${com.shahaabapps.twotracks.BuildConfig.VERSION_NAME}", leadingIcon = Icons.Filled.History, iconColor = IosColors.Indigo,
                        showDivider = false, onClick = onOpenChangelog, trailing = { CupertinoDisclosure() },
                    )
                }
                Box(modifier = Modifier.guideAnchor(GuideAnchors.SET_SIGN_OUT)) { CupertinoTextButton(text = "Sign out", destructive = true, onClick = viewModel::signOut) }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }

    if (editName && s != null) {
        var name by remember { mutableStateOf(s.me.name) }
        CupertinoDialog(
            onDismissRequest = { editName = false },
            confirmButton = { DialogButton(onClick = { viewModel.saveProfile { it.copy(name = name.trim()) }; editName = false }, enabled = name.isNotBlank()) { Text("Save") } },
            dismissButton = { DialogButton(onClick = { editName = false }) { Text("Cancel") } },
            title = { Text("Your name") },
            text = { CupertinoTextField(value = name, onValueChange = { name = it }, placeholder = "Name", modifier = Modifier.fillMaxWidth()) },
        )
    }
    if (editTimezone && s != null) {
        TimezoneDialog(selected = s.me.timezone, onSelect = { z -> viewModel.saveProfile { p -> p.copy(timezone = z) } }, onDismiss = { editTimezone = false })
    }
    if (editStartDate && s != null) {
        StartDateDialog(
            selectedIso = s.me.startDateIso,
            onPick = { iso -> viewModel.saveProfile { p -> p.copy(startDateIso = iso) } },
            onDismiss = { editStartDate = false },
        )
    }
    if (changePassword) {
        var current by remember { mutableStateOf("") }
        var new by remember { mutableStateOf("") }
        var again by remember { mutableStateOf("") }
        val mismatch = again.isNotEmpty() && new != again
        CupertinoDialog(
            onDismissRequest = { changePassword = false },
            confirmButton = {
                DialogButton(
                    onClick = { viewModel.changePassword(current, new); changePassword = false },
                    enabled = current.isNotEmpty() && new.length >= 6 && new == again,
                ) { Text("Change") }
            },
            dismissButton = { DialogButton(onClick = { changePassword = false }) { Text("Cancel") } },
            title = { Text("Change password") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    CupertinoTextField(value = current, onValueChange = { current = it }, placeholder = "Current password", keyboardType = KeyboardType.Password, isPassword = true, modifier = Modifier.fillMaxWidth())
                    CupertinoTextField(value = new, onValueChange = { new = it }, placeholder = "New password (6+ characters)", keyboardType = KeyboardType.Password, isPassword = true, modifier = Modifier.fillMaxWidth())
                    CupertinoTextField(value = again, onValueChange = { again = it }, placeholder = "New password again", keyboardType = KeyboardType.Password, isPassword = true, modifier = Modifier.fillMaxWidth())
                    if (mismatch) Text("The two new passwords differ.", color = AppTheme.colors.destructive, style = MaterialTheme.typography.bodySmall)
                    Text("Forgot the current one? Get a reset link at ${state.email} instead.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                    CupertinoTextButton(text = "Email me a reset link", onClick = { viewModel.sendPasswordReset(); changePassword = false })
                }
            },
        )
    }
    if (confirmReset) {
        CupertinoDialog(
            onDismissRequest = { confirmReset = false },
            confirmButton = { DialogButton(onClick = { viewModel.resetSeed(); confirmReset = false }, destructive = true) { Text("Reset") } },
            dismissButton = { DialogButton(onClick = { confirmReset = false }) { Text("Cancel") } },
            title = { Text("Reset guide content?") },
            text = { Text("Seeded goals, milestones, country to-dos, tests, documents and budget lines go back to the guide defaults. Study logs, mock scores and sessions are kept.") },
        )
    }
}
