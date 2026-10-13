package com.shahaabapps.twotracks.feature.settings

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.components.Avatar
import com.shahaabapps.core.ui.components.CupertinoSwitch
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.core.ui.theme.ThemeMode
import com.shahaabapps.twotracks.reminders.ReminderNotifications
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.ui.ThemeViewModel
import com.shahaabapps.twotracks.ui.components.CupertinoDialog
import com.shahaabapps.twotracks.ui.components.DetailScaffold
import com.shahaabapps.twotracks.ui.components.DialogButton
import com.shahaabapps.twotracks.ui.components.StartDateDialog
import com.shahaabapps.twotracks.ui.components.TimezoneDialog
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.LocalGuide
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import com.shahaabapps.twotracks.ui.guide.guideLazyScrollHost
import com.shahaabapps.twotracks.widget.placedWidgets
import java.util.Locale

/**
 * Which item of the list each thing the tour rings sits in.
 *
 * A lazy list has not composed what is off screen, so the tour cannot measure
 * its way there: it scrolls to the item first and the row reports itself once
 * it exists. Keep this in step with the order of the items below.
 */
internal val SETTINGS_GUIDE_ITEMS = mapOf(
    GuideAnchors.SET_NAME to 0,
    GuideAnchors.SET_INVITE to 1,
    GuideAnchors.SET_TEMPLATE to 2,
    GuideAnchors.SET_START_DATE to 2,
    GuideAnchors.SET_FULL_SCREEN to 2,
    GuideAnchors.SET_APPEARANCE to 3,
    GuideAnchors.SET_BIOMETRICS to 3,
    GuideAnchors.SET_VOICES to 3,
    GuideAnchors.SET_WIDGETS to 3,
    GuideAnchors.SET_TOUR to 4,
    GuideAnchors.SET_SEED to 4,
    GuideAnchors.SET_SIGN_OUT to 4,
)

/** Everything the Settings page prints, so a preview can draw it without an account. */
data class SettingsView(
    val name: String = "",
    val photoUrl: String? = null,
    val isTech: Boolean = true,
    /** "The developer · Asia/Qatar" */
    val profileLine: String = "",
    val inviteCode: String = "",
    val partnerName: String? = null,
    val partnerIsTech: Boolean = false,
    val weeklyHours: String = "",
    val startDate: String? = null,
    /** Null where the system has no page for it. */
    val fullScreen: Boolean? = null,
    val theme: ThemeMode = ThemeMode.SYSTEM,
    val look: AppLook = AppLook.PARALLEL,
    val biometricAvailable: Boolean = false,
    val biometricEnabled: Boolean = false,
    val voices: String = "",
    val widgets: String = "",
    val version: String = "",
    val seedOutdated: Boolean = false,
    val tour: Boolean = false,
)

/** "9.4 h a week" */
internal fun weeklyHoursText(minutes: Int): String {
    val h = minutes / 60.0
    return (if (h == Math.floor(h)) "%.0f h".format(Locale.ENGLISH, h) else "%.1f h".format(Locale.ENGLISH, h)) + " a week"
}

/** "None on your home screen", "1 on your home screen". */
internal fun widgetsText(n: Int?): String = when (n) {
    null -> "What each one shows"
    0 -> "None on your home screen"
    else -> "$n on your home screen"
}

/** The languages the courses are read aloud in: "German, English". */
internal fun voicesText(): String = Courses.all.filter { it.speechLanguage != null }.map { it.languageName }.distinct().joinToString(", ")

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onOpenAvailability: () -> Unit,
    onOpenVoices: () -> Unit,
    onOpenWidgets: () -> Unit,
    onOpenChangelog: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val mode by themeViewModel.themeMode.collectAsStateWithLifecycle()
    val look by themeViewModel.look.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val clipboard = LocalClipboardManager.current
    var editProfile by remember { mutableStateOf(false) }
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
    var widgetCount by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) { widgetCount = placedWidgets(context).size }
    }

    LaunchedEffect(state.message) { state.message?.let { snackbar.showSnackbar(it); viewModel.clearMessage() } }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) notifPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    val s = state.session
    val fullScreenIntent = remember { ReminderNotifications.fullScreenSettingsIntent(context) }
    val guide = LocalGuide.current
    val view = SettingsView(
        name = s?.me?.name.orEmpty(), photoUrl = s?.me?.photoUrl, isTech = s?.myRole != PersonRole.TEACHER,
        profileLine = s?.let { "${viewModel.roleLabel(it.myRole)} · ${it.me.timezone}" }.orEmpty(),
        inviteCode = s?.couple?.inviteCode.orEmpty(), partnerName = s?.partner?.name, partnerIsTech = s?.partner?.role == PersonRole.TECH,
        weeklyHours = weeklyHoursText(state.availability.weeklyStudyMinutes),
        startDate = s?.me?.startDateIso?.let { Clock.formatDate(it) },
        fullScreen = if (fullScreenIntent != null) fullScreenAllowed else null,
        theme = mode, look = look, biometricAvailable = state.biometricAvailable, biometricEnabled = state.biometricEnabled,
        voices = voicesText(), widgets = widgetsText(widgetCount),
        version = com.shahaabapps.twotracks.BuildConfig.VERSION_NAME, seedOutdated = state.seedOutdated, tour = guide != null,
    )
    SettingsContent(
        view, snackbar, onBack = onBack,
        onEditProfile = { editProfile = true },
        onCopyInvite = { clipboard.setText(AnnotatedString(view.inviteCode)); viewModel.say("Invite code copied") },
        onOpenAvailability = onOpenAvailability, onEditStartDate = { editStartDate = true },
        onOpenFullScreen = { fullScreenIntent?.let { context.startActivity(it) } },
        onTheme = themeViewModel::setThemeMode, onLook = themeViewModel::setLook, onBiometric = viewModel::setBiometricLock,
        onOpenVoices = onOpenVoices, onOpenWidgets = onOpenWidgets, onOpenChangelog = onOpenChangelog,
        onTour = { guide?.start() }, onReset = { confirmReset = true }, onSignOut = viewModel::signOut,
    )

    if (editProfile && s != null) {
        // The prototype's Edit. Name, time zone and password were four rows
        // of their own; they are one tap further in, and the email is said here.
        CupertinoDialog(
            onDismissRequest = { editProfile = false },
            confirmButton = { DialogButton(onClick = { editProfile = false }) { Text("Done") } },
            title = { Text("Your profile") },
            text = {
                Column {
                    ProfileChoice("Name", s.me.name) { editProfile = false; editName = true }
                    ProfileChoice("Time zone", s.me.timezone) { editProfile = false; editTimezone = true }
                    ProfileChoice("Password", "Change it, or get a reset link") { editProfile = false; changePassword = true }
                    Text(
                        "Signed in as ${state.email}. Your track, ${viewModel.roleLabel(s.myRole).lowercase()}, decides your courses and your roadmap.",
                        style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel, modifier = Modifier.padding(top = 12.dp),
                    )
                }
            },
        )
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
            text = {
                Text(
                    "Goals, roadmap, countries, tests, documents and budget go back to the guide (version ${com.shahaabapps.core.seed.Seed.VERSION}). " +
                        "Level 1 starts today and every later date moves with it. Study logs, mock scores and sessions are kept.",
                )
            },
        )
    }
}

@Composable
private fun ProfileChoice(label: String, value: String, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable(role = Role.Button, onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.heightIn(min = 52.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.bodyMedium)
                Text(value, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(16.dp))
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
    }
}

/**
 * Settings, as the prototype draws it: who you are under an ink rule, then
 * your space, your study, the app and about, each a mono heading over plain
 * rows with their value on the right.
 */
@Composable
fun SettingsContent(
    v: SettingsView,
    snackbar: SnackbarHostState,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onCopyInvite: () -> Unit,
    onOpenAvailability: () -> Unit,
    onEditStartDate: () -> Unit,
    onOpenFullScreen: () -> Unit,
    onTheme: (ThemeMode) -> Unit,
    onLook: (AppLook) -> Unit,
    onBiometric: (Boolean) -> Unit,
    onOpenVoices: () -> Unit,
    onOpenWidgets: () -> Unit,
    onOpenChangelog: () -> Unit,
    onTour: () -> Unit,
    onReset: () -> Unit,
    onSignOut: () -> Unit,
) {
    if (AppTheme.departures) {
        BoardSettings(
            v, snackbar, onBack, onEditProfile, onCopyInvite, onOpenAvailability, onEditStartDate, onOpenFullScreen,
            onLook, onBiometric, onOpenVoices, onOpenWidgets, onOpenChangelog, onTour, onReset, onSignOut,
        )
        return
    }
    DetailScaffold(title = "Settings", onBack = onBack, backLabel = "Back to Today") {
        Box {
            val pageScroll = rememberLazyListState()
            // The tour scrolls this list's own state, not one it was handed
            // and this list never used.
            LazyColumn(state = pageScroll, modifier = Modifier.guideLazyScrollHost(pageScroll, SETTINGS_GUIDE_ITEMS), contentPadding = PaddingValues(bottom = 40.dp)) {
                item {
                    Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 10.dp)) {
                        HorizontalDivider(color = AppTheme.colors.ink, thickness = Spacing.rule)
                        Row(
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxWidth().guideAnchor(GuideAnchors.SET_NAME).clickable(role = Role.Button, onClick = onEditProfile).padding(vertical = 16.dp),
                        ) {
                            Avatar(name = v.name, photoUrl = v.photoUrl, size = 48.dp, color = AppTheme.colors.track(v.isTech))
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(v.name, style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp), fontWeight = FontWeight.SemiBold)
                                Text(v.profileLine, style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
                            }
                            Text("Edit", style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp), color = AppTheme.colors.accentText)
                        }
                        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                    }
                }
                item {
                    Section("Your space") {
                        Line("Invite code", Modifier.guideAnchor(GuideAnchors.SET_INVITE), onClick = onCopyInvite, onClickLabel = "Copy the invite code") {
                            Text(v.inviteCode, style = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFamily, fontSize = 15.sp, letterSpacing = 1.8.sp))
                        }
                        Line("Partner") {
                            if (v.partnerName != null) {
                                Box(Modifier.size(8.dp).clip(CircleShape).background(AppTheme.colors.track(v.partnerIsTech)))
                                Value("${v.partnerName} joined")
                            } else {
                                Value("Not joined yet. Share the code.")
                            }
                        }
                    }
                }
                item {
                    Section("Study") {
                        Line("Weekly hours", Modifier.guideAnchor(GuideAnchors.SET_TEMPLATE), onClick = onOpenAvailability, chevron = true) { Value(v.weeklyHours) }
                        Line("Study started", Modifier.guideAnchor(GuideAnchors.SET_START_DATE), onClick = onEditStartDate) { Value(v.startDate ?: "Not set") }
                        v.fullScreen?.let { on ->
                            // In the prototype's place for a daily reminder: the app's
                            // reminders follow the study blocks, and this is the one
                            // thing about them the system lets you change.
                            Line(
                                "Reminders take over the screen", Modifier.guideAnchor(GuideAnchors.SET_FULL_SCREEN), onClick = onOpenFullScreen, chevron = true,
                                detail = if (on) "A reminder opens in front of whatever you are doing" else "Off. Reminders arrive as a banner.",
                            ) { Value(if (on) "On" else "Off") }
                        }
                    }
                }
                item {
                    Section("App") {
                        // Which of the two designs the app is drawn in. Departures is
                        // a board at night and has no light version, so the light and
                        // dark choice below only applies to Parallel.
                        Line("Look", detail = (if (v.look == AppLook.DEPARTURES) "A departures board, always dark" else "Light and editorial") + ". The home-screen icon follows once you leave the app.", minHeight = 64.dp) { LookChoice(v.look, onLook) }
                        if (v.look == AppLook.PARALLEL) {
                            Line("Appearance", Modifier.guideAnchor(GuideAnchors.SET_APPEARANCE), minHeight = 64.dp) { ThemeChoice(v.theme, onTheme) }
                        } else {
                            Line("Appearance", Modifier.guideAnchor(GuideAnchors.SET_APPEARANCE), detail = "The board is always dark") { Value("Dark") }
                        }
                        Line(
                            "App lock", Modifier.guideAnchor(GuideAnchors.SET_BIOMETRICS),
                            detail = if (v.biometricAvailable) "Fingerprint, face or PIN when the app opens" else "Set up a screen lock on this phone first",
                        ) { CupertinoSwitch(checked = v.biometricEnabled, onCheckedChange = onBiometric, enabled = v.biometricAvailable) }
                        Line("Voices", Modifier.guideAnchor(GuideAnchors.SET_VOICES), onClick = onOpenVoices, chevron = true) { Value(v.voices) }
                        Line("Widgets", Modifier.guideAnchor(GuideAnchors.SET_WIDGETS), onClick = onOpenWidgets, chevron = true) { Value(v.widgets) }
                    }
                }
                item {
                    Section("About") {
                        Line("What's new", onClick = onOpenChangelog, chevron = true) {
                            Text(v.version, style = MaterialTheme.typography.bodySmall.copy(fontFamily = MonoFamily, fontSize = 13.sp), color = AppTheme.colors.secondaryLabel)
                        }
                        if (v.tour) Line("Show me around", Modifier.guideAnchor(GuideAnchors.SET_TOUR), onClick = onTour, chevron = true) { Value("A short tour") }
                        Line(
                            if (v.seedOutdated) "Update to the latest guide content" else "Reset guide content", Modifier.guideAnchor(GuideAnchors.SET_SEED),
                            onClick = onReset, chevron = true,
                        ) {}
                        Column(modifier = Modifier.guideAnchor(GuideAnchors.SET_SIGN_OUT)) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).clickable(role = Role.Button, onClick = onSignOut)) {
                                Text("Sign out", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = AppTheme.colors.destructive)
                            }
                            HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
                        }
                    }
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(start = Spacing.listHorizontal, end = Spacing.listHorizontal, top = 22.dp)) {
        Text(
            title.uppercase(Locale.ENGLISH), style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp), color = AppTheme.colors.secondaryLabel,
            modifier = Modifier.padding(bottom = 4.dp).semantics { heading() },
        )
        content()
    }
}

@Composable
private fun Line(
    label: String,
    modifier: Modifier = Modifier,
    detail: String? = null,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    chevron: Boolean = false,
    minHeight: androidx.compose.ui.unit.Dp = 56.dp,
    value: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight)
                .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClickLabel = onClickLabel, onClick = onClick) else Modifier)
                .padding(vertical = 8.dp),
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(label, style = MaterialTheme.typography.bodyMedium)
                detail?.let { Text(it, style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp), color = AppTheme.colors.secondaryLabel) }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                value()
                if (chevron) Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(16.dp))
            }
        }
        HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
    }
}

@Composable
private fun Value(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp), color = AppTheme.colors.secondaryLabel, maxLines = 1)
}

@Composable
private fun LookChoice(selected: AppLook, onSelect: (AppLook) -> Unit) {
    Segments(listOf(AppLook.PARALLEL to "Parallel", AppLook.DEPARTURES to "Departures"), selected, onSelect)
}

@Composable
private fun <T> Segments(options: List<Pair<T, String>>, selected: T, onSelect: (T) -> Unit) {
    val shape = RoundedCornerShape(10.dp)
    Row(modifier = Modifier.clip(shape).border(Spacing.rule, AppTheme.colors.separator, shape)) {
        options.forEach { (value, label) ->
            val on = selected == value
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(36.dp).background(if (on) AppTheme.colors.accent else Color.Transparent)
                    .clickable(role = Role.RadioButton) { onSelect(value) }.padding(horizontal = 12.dp),
            ) { Text(label, style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp), color = if (on) AppTheme.colors.onSolid else AppTheme.colors.ink) }
        }
    }
}

@Composable
private fun ThemeChoice(selected: ThemeMode, onSelect: (ThemeMode) -> Unit) {
    Segments(listOf(ThemeMode.LIGHT to "Light", ThemeMode.DARK to "Dark", ThemeMode.SYSTEM to "System"), selected, onSelect)
}
