package com.shahaabapps.twotracks.feature.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shahaabapps.core.ui.components.CupertinoDisclosure
import com.shahaabapps.core.ui.components.CupertinoListRow
import com.shahaabapps.core.ui.components.CupertinoListSection
import com.shahaabapps.core.ui.components.CupertinoScaffold
import com.shahaabapps.core.ui.theme.IosColors
import com.shahaabapps.core.ui.theme.Spacing
import com.shahaabapps.twotracks.ui.Routes

/** The iOS Settings-style hub: coloured icon tiles, labels and chevrons in grouped cards. */
@Composable
fun MoreScreen(onNavigate: (String) -> Unit) {
    CupertinoScaffold(title = "More") {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(bottom = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.lg),
        ) {
            CupertinoListSection(header = "The plan") {
                CupertinoListRow("Roadmap", subtitle = "Quarter by quarter to the Sep 2027 checkpoint", leadingIcon = Icons.Filled.Map, iconColor = IosColors.Indigo, onClick = { onNavigate(Routes.ROADMAP) }, trailing = { CupertinoDisclosure() })
                CupertinoListRow("Countries", subtitle = "Six tracks, who leads, what to do", leadingIcon = Icons.Filled.Public, iconColor = IosColors.Cyan, onClick = { onNavigate(Routes.COUNTRIES) }, trailing = { CupertinoDisclosure() })
                CupertinoListRow("Tests and scores", subtitle = "Official results against targets", leadingIcon = Icons.Filled.Verified, iconColor = IosColors.Green, onClick = { onNavigate(Routes.TESTS) }, trailing = { CupertinoDisclosure() })
                CupertinoListRow("Documents", subtitle = "Checklist per person", leadingIcon = Icons.Filled.Description, iconColor = IosColors.Orange, onClick = { onNavigate(Routes.DOCUMENTS) }, trailing = { CupertinoDisclosure() })
                CupertinoListRow("Budget", subtitle = "Planned versus spent", leadingIcon = Icons.Filled.AccountBalanceWallet, iconColor = IosColors.Pink, showDivider = false, onClick = { onNavigate(Routes.BUDGET) }, trailing = { CupertinoDisclosure() })
            }
            CupertinoListSection(header = "App") {
                CupertinoListRow("Settings", subtitle = "Profile, schedule, reminders, partner invite", leadingIcon = Icons.Filled.Settings, iconColor = IosColors.Gray, showDivider = false, onClick = { onNavigate(Routes.SETTINGS) }, trailing = { CupertinoDisclosure() })
            }
            CupertinoListSection(footer = "Rules in the guide were checked on 28 September 2026. Re-check official sites before paying fees or resigning.") {
                CupertinoListRow("Guide", subtitle = "Moving Abroad as a Couple, September 2026", leadingIcon = Icons.Filled.Flag, iconColor = IosColors.Blue, showDivider = false)
            }
        }
    }
}
