package com.shahaabapps.twotracks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.datastore.ThemePreferences
import com.shahaabapps.core.ui.theme.AccentPalette
import com.shahaabapps.core.ui.theme.ThemeMode
import com.shahaabapps.twotracks.feature.auth.CompanionKind
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themePreferences: ThemePreferences,
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = themePreferences.themeModeName
        .map { runCatching { ThemeMode.valueOf(it) }.getOrDefault(ThemeMode.LIGHT) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemeMode.LIGHT)

    val accentPalette: StateFlow<AccentPalette> = themePreferences.accentPaletteName
        .map { runCatching { AccentPalette.valueOf(it) }.getOrDefault(AccentPalette.QATAR) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AccentPalette.QATAR)

    val companion: StateFlow<CompanionKind> = themePreferences.companionName
        .map { runCatching { CompanionKind.valueOf(it) }.getOrDefault(CompanionKind.BUDDY) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CompanionKind.BUDDY)

    fun setCompanion(kind: CompanionKind) {
        viewModelScope.launch { themePreferences.setCompanionName(kind.name) }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { themePreferences.setThemeModeName(mode.name) }
    }

    fun setAccentPalette(palette: AccentPalette) {
        viewModelScope.launch { themePreferences.setAccentPaletteName(palette.name) }
    }
}
