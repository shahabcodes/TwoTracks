package com.shahaabapps.twotracks.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.BarlowFamily
import com.shahaabapps.core.ui.theme.CondensedFamily
import com.shahaabapps.twotracks.ui.board.BoardBack
import com.shahaabapps.twotracks.ui.board.BoardGutter
import com.shahaabapps.twotracks.ui.board.BoardLabel
import com.shahaabapps.twotracks.ui.board.BoardMono
import com.shahaabapps.twotracks.ui.board.BoardSegments
import com.shahaabapps.twotracks.ui.board.BoardText
import com.shahaabapps.twotracks.ui.board.BoardTitle
import com.shahaabapps.twotracks.ui.guide.GuideAnchors
import com.shahaabapps.twotracks.ui.guide.guideAnchor
import com.shahaabapps.twotracks.ui.guide.guideLazyScrollHost
import java.util.Locale

/** "LINE DEV · ASIA/QATAR", from "The developer · Asia/Qatar". */
internal fun boardProfileLine(isTech: Boolean, profileLine: String): String =
    (listOf("Line ${if (isTech) "dev" else "edu"}") + profileLine.split(" · ").drop(1)).joinToString(" · ").uppercase(Locale.ENGLISH)

/** "9.4H", the weekly hours as the board's value column prints them. */
internal fun boardWeekly(text: String): String = text.removeSuffix(" a week").replace(" h", "H").uppercase(Locale.ENGLISH)

/**
 * Settings on the Departures board, as its prototype draws it: you on a
 * card edged in your colour, then your space, study and the app as ruled
 * rows with their values in mono and switches that say ON or OFF, and the
 * look as the board's segments where the prototype chooses its colour.
 */
@Composable
internal fun BoardSettings(
    v: SettingsView,
    snackbar: SnackbarHostState,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onCopyInvite: () -> Unit,
    onOpenAvailability: () -> Unit,
    onEditStartDate: () -> Unit,
    onOpenFullScreen: () -> Unit,
    onLook: (AppLook) -> Unit,
    onBiometric: (Boolean) -> Unit,
    onOpenVoices: () -> Unit,
    onOpenWidgets: () -> Unit,
    onOpenChangelog: () -> Unit,
    onTour: () -> Unit,
    onReset: () -> Unit,
    onSignOut: () -> Unit,
) {
    val mine = AppTheme.colors.track(v.isTech)
    Scaffold(containerColor = AppTheme.colors.groupedBackground) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            val pageScroll = rememberLazyListState()
            // The same five items as the Parallel page, so the tour's scroll map holds for both.
            LazyColumn(state = pageScroll, modifier = Modifier.guideLazyScrollHost(pageScroll, SETTINGS_GUIDE_ITEMS), contentPadding = PaddingValues(bottom = 40.dp)) {
                item {
                    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(Modifier.height(44.dp), contentAlignment = Alignment.CenterStart) { BoardBack(onBack, "Back to departures") }
                        BoardTitle("Settings", size = 52.sp)
                        Row(
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.padding(top = 8.dp).fillMaxWidth().guideAnchor(GuideAnchors.SET_NAME).clip(RoundedCornerShape(16.dp)).background(AppTheme.colors.card)
                                .drawBehind { drawRect(mine, topLeft = Offset.Zero, size = Size(4.dp.toPx(), size.height)) }
                                .clickable(role = Role.Button, onClick = onEditProfile).padding(start = 18.dp, end = 14.dp, top = 14.dp, bottom = 14.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(10.dp)).background(mine)) {
                                Text(v.name.take(1).uppercase(Locale.ENGLISH), color = AppTheme.colors.onSolid, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp))
                            }
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(v.name, color = AppTheme.colors.ink, style = TextStyle(fontFamily = CondensedFamily, fontWeight = FontWeight.SemiBold, fontSize = 22.sp))
                                BoardMono(boardProfileLine(v.isTech, v.profileLine), spacing = 0.sp)
                            }
                            BoardLabel("Edit", color = AppTheme.colors.accent, size = 15.sp, weight = FontWeight.Bold, spacing = 0.1.em)
                        }
                    }
                }
                item {
                    Group("Your space") {
                        Row("Invite code", "Share it to add a passenger", Modifier.guideAnchor(GuideAnchors.SET_INVITE), onClick = onCopyInvite, onClickLabel = "Copy the invite code") {
                            BoardMono(v.inviteCode, color = AppTheme.colors.accent, size = 13.sp, weight = FontWeight.Bold, spacing = 0.12.em)
                        }
                        val partner = v.partnerName
                        Row("Partner", if (partner != null) "Line ${if (v.partnerIsTech) "DEV" else "EDU"}" else "Share the code to add them") {
                            BoardMono(
                                if (partner != null) "${partner.uppercase(Locale.ENGLISH)} JOINED" else "NOT JOINED YET",
                                color = if (partner != null) AppTheme.colors.track(v.partnerIsTech) else AppTheme.colors.secondaryLabel, size = 12.sp, spacing = 0.sp,
                            )
                        }
                    }
                }
                item {
                    Group("Study") {
                        Row("Weekly hours", "The template the plan fills", Modifier.guideAnchor(GuideAnchors.SET_TEMPLATE), onClick = onOpenAvailability, chevron = true) { Value(boardWeekly(v.weeklyHours)) }
                        Row("Study started", "Day one of the plan", Modifier.guideAnchor(GuideAnchors.SET_START_DATE), onClick = onEditStartDate) { Value(v.startDate?.uppercase(Locale.ENGLISH) ?: "NOT SET") }
                        v.fullScreen?.let { on ->
                            // In the prototype's place for a daily reminder, as on the Parallel page.
                            Row(
                                "Reminders take over the screen", if (on) "A reminder opens in front of whatever you are doing" else "Off. Reminders arrive as a banner.",
                                Modifier.guideAnchor(GuideAnchors.SET_FULL_SCREEN), onClick = onOpenFullScreen,
                            ) { OnOff(on) }
                        }
                    }
                }
                item {
                    Group("App") {
                        Row(
                            "App lock", if (v.biometricAvailable) "Fingerprint, face or PIN when the app opens" else "Set up a screen lock on this phone first",
                            Modifier.guideAnchor(GuideAnchors.SET_BIOMETRICS),
                            onClick = if (v.biometricAvailable) ({ onBiometric(!v.biometricEnabled) }) else null, role = Role.Switch,
                        ) { OnOff(v.biometricEnabled, enabled = v.biometricAvailable) }
                        Row("Voices", "Who reads the courses aloud", Modifier.guideAnchor(GuideAnchors.SET_VOICES), onClick = onOpenVoices, chevron = true) { Value(v.voices.uppercase(Locale.ENGLISH)) }
                        Row("Widgets", "What each one shows", Modifier.guideAnchor(GuideAnchors.SET_WIDGETS), onClick = onOpenWidgets, chevron = true) { Value(v.widgets.uppercase(Locale.ENGLISH)) }
                    }
                    // Where the prototype chooses the board's colour: the board is always dark,
                    // so the choice it offers here is which of the two looks the app is drawn in.
                    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp).guideAnchor(GuideAnchors.SET_APPEARANCE)) {
                        BoardLabel("Look", size = 13.sp, spacing = 0.12.em, modifier = Modifier.padding(bottom = 6.dp).semantics { heading() })
                        Rule()
                        BoardSegments(listOf(AppLook.PARALLEL to "Parallel", AppLook.DEPARTURES to "Departures"), selected = v.look, onSelect = onLook, modifier = Modifier.padding(top = 10.dp))
                        BoardText("Departures is a board at night and is always dark.", size = 12.sp, modifier = Modifier.padding(top = 8.dp))
                    }
                }
                item {
                    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp)) {
                        Rule()
                        Row("What's new", null, onClick = onOpenChangelog, chevron = true) { Value(v.version) }
                        if (v.tour) Row("Show me around", "A short tour", Modifier.guideAnchor(GuideAnchors.SET_TOUR), onClick = onTour, chevron = true) {}
                        Row(if (v.seedOutdated) "Update to the latest guide content" else "Reset guide content", null, Modifier.guideAnchor(GuideAnchors.SET_SEED), onClick = onReset, chevron = true) {}
                        Column(modifier = Modifier.guideAnchor(GuideAnchors.SET_SIGN_OUT)) {
                            Box(
                                contentAlignment = Alignment.CenterStart,
                                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp).clickable(role = Role.Button, onClick = onSignOut),
                            ) { BoardLabel("Sign out", color = AppTheme.colors.destructive, size = 17.sp, weight = FontWeight.Bold, spacing = 0.1.em) }
                            Rule()
                        }
                    }
                }
            }
            SnackbarHost(snackbar, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun Rule() {
    Box(Modifier.fillMaxWidth().height(1.dp).background(AppTheme.colors.separator))
}

@Composable
private fun Group(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(start = BoardGutter, end = BoardGutter, top = 18.dp)) {
        BoardLabel(title, size = 13.sp, spacing = 0.12.em, modifier = Modifier.padding(bottom = 6.dp).semantics { heading() })
        Rule()
        content()
    }
}

@Composable
private fun Row(
    label: String,
    detail: String?,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    chevron: Boolean = false,
    role: Role = Role.Button,
    minHeight: Dp = 54.dp,
    value: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight)
                .then(if (onClick != null) Modifier.clickable(role = role, onClickLabel = onClickLabel, onClick = onClick) else Modifier)
                .padding(vertical = 8.dp),
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(label, color = AppTheme.colors.ink, style = TextStyle(fontFamily = BarlowFamily, fontSize = 15.sp))
                detail?.let { BoardText(it, size = 12.sp) }
            }
            androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                value()
                if (chevron) Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = AppTheme.colors.secondaryLabel, modifier = Modifier.size(16.dp))
            }
        }
        Rule()
    }
}

@Composable
private fun Value(text: String) {
    BoardMono(text, size = 12.sp, spacing = 0.sp)
}

/** The board's switch: a block that says ON in green or OFF in grey. */
@Composable
private fun OnOff(on: Boolean, enabled: Boolean = true) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.width(64.dp).height(30.dp).clip(RoundedCornerShape(6.dp))
            .background(if (on && enabled) AppTheme.colors.success else AppTheme.colors.separator)
            .semantics { stateDescription = if (on) "On" else "Off" },
    ) {
        BoardLabel(
            if (on) "On" else "Off", size = 14.sp, weight = FontWeight.Bold, spacing = 0.1.em,
            color = if (on && enabled) AppTheme.colors.onSolid else if (enabled) AppTheme.colors.secondaryLabel else AppTheme.colors.secondaryLabel.copy(alpha = 0.6f),
        )
    }
}

