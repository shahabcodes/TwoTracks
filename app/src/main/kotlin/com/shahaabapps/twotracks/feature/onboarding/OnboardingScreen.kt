package com.shahaabapps.twotracks.feature.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoPrimaryButton
import com.shahaabapps.core.ui.components.CupertinoScaffold
import com.shahaabapps.core.ui.components.CupertinoTextButton
import com.shahaabapps.core.ui.components.CupertinoTextField
import com.shahaabapps.core.ui.theme.AppTheme
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.components.SegmentedChoice

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CupertinoScaffold(title = "Your space", modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.md),
        ) {
            when (state.mode) {
                OnboardingMode.CHOOSE -> {
                    Text(
                        text = "One of you creates the shared space, the other joins it with the 6-letter invite code.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppTheme.colors.secondaryLabel,
                        modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
                    )
                    CupertinoListSection {
                        CupertinoListRow(
                            title = "Create a new space",
                            subtitle = "Seeds the full plan from the guide",
                            leadingIcon = Icons.Filled.AddCircle,
                            onClick = { viewModel.setMode(OnboardingMode.CREATE) },
                            trailing = { CupertinoDisclosure() },
                        )
                        CupertinoListRow(
                            title = "Join with an invite code",
                            subtitle = "Your partner already created one",
                            leadingIcon = Icons.Filled.Link,
                            showDivider = false,
                            onClick = { viewModel.setMode(OnboardingMode.JOIN) },
                            trailing = { CupertinoDisclosure() },
                        )
                    }
                    CupertinoTextButton(text = "Sign out", onClick = viewModel::signOut, destructive = true)
                }
                else -> {
                    Column(
                        modifier = Modifier.padding(horizontal = Spacing.listHorizontal),
                        verticalArrangement = Arrangement.spacedBy(Spacing.md),
                    ) {
                        if (state.mode == OnboardingMode.JOIN) {
                            CupertinoTextField(
                                value = state.inviteCode, onValueChange = viewModel::onInviteCodeChange,
                                placeholder = "Invite code (6 letters)", keyboardType = KeyboardType.Ascii,
                            )
                        }
                        CupertinoTextField(value = state.name, onValueChange = viewModel::onNameChange, placeholder = "Your first name")

                        Text("I AM THE", style = MaterialTheme.typography.labelMedium, color = AppTheme.colors.secondaryLabel)
                        SegmentedChoice(
                            options = PersonRole.entries,
                            selected = state.role,
                            label = { if (it == PersonRole.TECH) "Developer" else "Teacher" },
                            onSelect = viewModel::onRoleChange,
                        )
                        Text(
                            "Timezone ${state.timezone}. Change it later in Settings if needed.",
                            style = MaterialTheme.typography.bodySmall, color = AppTheme.colors.secondaryLabel,
                        )

                        state.errorMessage?.let { Text(it, color = AppTheme.colors.destructive, style = MaterialTheme.typography.bodySmall) }

                        CupertinoPrimaryButton(
                            text = when {
                                state.isSubmitting -> "Setting up"
                                state.mode == OnboardingMode.CREATE -> "Create space"
                                else -> "Join space"
                            },
                            onClick = viewModel::submit,
                            enabled = state.canSubmit,
                        )
                        CupertinoTextButton(text = "Back", onClick = { viewModel.setMode(OnboardingMode.CHOOSE) })
                    }
                }
            }
        }
    }
}
