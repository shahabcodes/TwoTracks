package com.shahaabapps.twotracks.ui.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.common.datastore.GuidePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Whether the tour is owed to whoever is signed in.
 *
 * The flag is set when a sign-in succeeds and cleared when the tour ends,
 * however it ends: finished, skipped, or the person simply signing out again.
 * Reopening the app does not sign anyone in, so it does not bring the tour
 * back.
 */
@HiltViewModel
class GuideViewModel @Inject constructor(
    private val guidePreferences: GuidePreferences,
) : ViewModel() {

    val pending: StateFlow<Boolean> = guidePreferences.pending
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun markSeen() {
        viewModelScope.launch { guidePreferences.setPending(false) }
    }
}
