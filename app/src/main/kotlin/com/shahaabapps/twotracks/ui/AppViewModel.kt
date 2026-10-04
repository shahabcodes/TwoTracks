package com.shahaabapps.twotracks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.twotracks.reminders.ReminderScheduler
import com.shahaabapps.twotracks.session.SessionState
import com.shahaabapps.twotracks.session.SessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    sessionStore: SessionStore,
    private val reminderScheduler: ReminderScheduler,
) : ViewModel() {

    val state: StateFlow<SessionState> = sessionStore.state

    init {
        // Keep the next-session alarm in sync whenever the session becomes ready.
        state.filterIsInstance<SessionState.Ready>()
            .onEach { reminderScheduler.refresh() }
            .launchIn(viewModelScope)
    }
}
