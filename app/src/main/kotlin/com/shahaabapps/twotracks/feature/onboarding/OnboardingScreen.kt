package com.shahaabapps.twotracks.feature.onboarding

import com.shahaabapps.twotracks.feature.auth.BoardSpaceStep
import com.shahaabapps.twotracks.feature.auth.BoardRoleStep
import com.shahaabapps.twotracks.ui.board.BoardGutter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoSegmentedControl
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.components.SectionLabel
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.MonoFamily
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.LoadingOverlay

/**
 * Steps 2 and 3 of setting up, after the account (step 1): who you are and
 * which track is yours, then a space of your own or your partner's.
 */
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    if (state.isSubmitting) LoadingOverlay("Setting up your space")
    var step by rememberSaveable { mutableIntStateOf(2) }
    val departures = AppTheme.departures

    AnimatedContent(
        targetState = step,
        transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
        label = "step",
        modifier = modifier.fillMaxSize().background(AppTheme.colors.groupedBackground),
    ) { current ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = if (departures) BoardGutter else Spacing.listHorizontal),
        ) {
            if (departures) {
                if (current == 2) {
                    BoardRoleStep(
                        state.name, state.role, state.timezone, viewModel::onNameChange, viewModel::onRoleChange,
                        onContinue = {
                            if (state.mode == OnboardingMode.CHOOSE) viewModel.setMode(OnboardingMode.CREATE)
                            step = 3
                        },
                        onSignOut = viewModel::signOut,
                    )
                } else {
                    BoardSpaceStep(
                        joining = state.mode == OnboardingMode.JOIN, inviteCode = state.inviteCode, error = state.errorMessage,
                        submitting = state.isSubmitting, canSubmit = state.canSubmit,
                        onJoining = { viewModel.setMode(if (it) OnboardingMode.JOIN else OnboardingMode.CREATE) },
                        onCode = viewModel::onInviteCodeChange, onSubmit = viewModel::submit, onBack = { step = 2 },
                    )
                }
            } else if (current == 2) {
                RoleStep(state, viewModel) {
                    if (state.mode == OnboardingMode.CHOOSE) viewModel.setMode(OnboardingMode.CREATE)
                    step = 3
                }
            } else {
                SpaceStep(state, viewModel, onBack = { step = 2 })
            }
        }
    }
}

@Composable
private fun ColumnScope.RoleStep(state: OnboardingUiState, viewModel: OnboardingViewModel, onContinue: () -> Unit) {
    Spacer(Modifier.height(60.dp))
    SectionLabel("Step 2 of 3")
    Title("Which track is yours?")
    Text(
        "The plan, the courses and the countries each of you leads come from this.",
        style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
        modifier = Modifier.padding(top = 10.dp),
    )
    CupertinoTextField(
        value = state.name, onValueChange = viewModel::onNameChange,
        label = "Your first name", modifier = Modifier.padding(top = 24.dp),
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(top = 22.dp).selectableGroup(),
    ) {
        RoleCard(
            title = "The developer",
            detail = "AZ-204 and German. Leads Germany, Ireland and the Netherlands.",
            color = AppTheme.colors.tech,
            selected = state.role == PersonRole.TECH,
            onSelect = { viewModel.onRoleChange(PersonRole.TECH) },
        )
        RoleCard(
            title = "The teacher",
            detail = "IELTS and German. Leads Canada and New Zealand.",
            color = AppTheme.colors.teacher,
            selected = state.role == PersonRole.TEACHER,
            onSelect = { viewModel.onRoleChange(PersonRole.TEACHER) },
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 22.dp).fillMaxWidth(),
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
            Text("Your time zone", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 12.dp))
            Text("Days, streaks and reminders run on this clock. Change it later in Settings.", style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel)
            Text(state.timezone, style = MaterialTheme.typography.bodyMedium.copy(fontFamily = MonoFamily), modifier = Modifier.padding(top = 4.dp, bottom = 12.dp))
            HorizontalDivider(color = AppTheme.colors.separator, thickness = Spacing.hairline)
        }
    }
    Spacer(Modifier.weight(1f).heightIn(min = Spacing.xl))
    CupertinoPrimaryButton(text = "Continue  →", onClick = onContinue, enabled = state.name.isNotBlank())
    Box(Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 28.dp), contentAlignment = Alignment.Center) {
        FooterLink("Not you? Sign out", onClick = viewModel::signOut)
    }
}

@Composable
private fun ColumnScope.SpaceStep(state: OnboardingUiState, viewModel: OnboardingViewModel, onBack: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(top = 8.dp)
            .offset(x = (-10).dp)
            .size(44.dp)
            .clip(CircleShape)
            .clickable(role = Role.Button, onClickLabel = "Back", onClick = onBack),
    ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AppTheme.colors.ink)
    }
    SectionLabel("Step 3 of 3", modifier = Modifier.padding(top = 18.dp))
    Title("One space for the two of you")
    CupertinoSegmentedControl(
        options = listOf(OnboardingMode.CREATE, OnboardingMode.JOIN),
        selected = if (state.mode == OnboardingMode.JOIN) OnboardingMode.JOIN else OnboardingMode.CREATE,
        label = { if (it == OnboardingMode.CREATE) "Start a new one" else "Join your partner's" },
        onSelect = viewModel::setMode,
        modifier = Modifier.padding(top = 22.dp),
    )
    if (state.mode == OnboardingMode.JOIN) {
        Text(
            "Type the six-letter code from your partner's phone. You join their space as the other track.",
            style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
            modifier = Modifier.padding(top = 22.dp),
        )
        CupertinoTextField(
            value = state.inviteCode, onValueChange = viewModel::onInviteCodeChange,
            label = "Invite code", keyboardType = KeyboardType.Ascii,
            modifier = Modifier.padding(top = 14.dp),
        )
    } else {
        Text(
            "Your space comes with both plans already in it: the levels, the courses, the six countries and the paperwork. Once it is open, Settings shows a six-letter code to send your partner.",
            style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel,
            modifier = Modifier.padding(top = 22.dp),
        )
    }
    state.errorMessage?.let {
        Column(Modifier.padding(top = 18.dp)) {
            HorizontalDivider(color = AppTheme.colors.destructive, thickness = Spacing.rule)
            Text(it, color = AppTheme.colors.destructive, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 10.dp))
        }
    }
    Spacer(Modifier.weight(1f).heightIn(min = Spacing.xl))
    CupertinoPrimaryButton(
        text = when {
            state.isSubmitting -> "Setting up"
            state.mode == OnboardingMode.JOIN -> "Join the space  →"
            else -> "Open the space  →"
        },
        onClick = viewModel::submit,
        enabled = state.canSubmit,
        modifier = Modifier.padding(bottom = 36.dp),
    )
}

@Composable
private fun Title(text: String) {
    Text(
        text, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 8.dp).semantics { heading() },
    )
}

/** A track to choose: its colour as a bar down the side, what it studies and leads, and a radio. */
@Composable
private fun RoleCard(title: String, detail: String, color: Color, selected: Boolean, onSelect: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(shape)
            .border(if (selected) 2.dp else Spacing.rule, if (selected) AppTheme.colors.ink else AppTheme.colors.separator, shape)
            .selectable(selected = selected, role = Role.RadioButton, onClick = onSelect)
            .padding(18.dp),
    ) {
        Box(Modifier.width(4.dp).fillMaxHeight().background(color))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(detail, style = MaterialTheme.typography.bodyMedium, color = AppTheme.colors.secondaryLabel)
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(22.dp).clip(CircleShape).border(2.dp, AppTheme.colors.ink, CircleShape),
        ) {
            if (selected) Box(Modifier.size(10.dp).clip(CircleShape).background(AppTheme.colors.ink))
        }
    }
}

@Composable
private fun FooterLink(text: String, onClick: () -> Unit) {
    Text(
        text,
        style = MaterialTheme.typography.labelLarge.copy(textDecoration = TextDecoration.Underline),
        color = AppTheme.colors.ink,
        modifier = Modifier.clip(CircleShape).clickable(role = Role.Button, onClick = onClick).padding(horizontal = 8.dp, vertical = 12.dp),
    )
}
