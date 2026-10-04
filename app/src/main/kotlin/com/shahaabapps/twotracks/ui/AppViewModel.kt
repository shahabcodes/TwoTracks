package com.shahaabapps.twotracks.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahaabapps.core.data.couple.CoupleRepository
import com.shahaabapps.core.seed.Seed
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
    private val coupleRepository: CoupleRepository,
) : ViewModel() {

    val state: StateFlow<SessionState> = sessionStore.state
    private var upgrading = false

    init {
        state.filterIsInstance<SessionState.Ready>()
            .onEach { ready ->
                // Bring older spaces up to the current guide content (levels etc.); progress is preserved.
                if (ready.session.couple.seededVersion < Seed.VERSION && !upgrading) {
                    upgrading = true
                    coupleRepository.resetSeedContent(ready.session.coupleId)
                }
                // Keep the next-session alarm in sync whenever the session becomes ready.
                reminderScheduler.refresh()
            }
            .launchIn(viewModelScope)
    }
}
