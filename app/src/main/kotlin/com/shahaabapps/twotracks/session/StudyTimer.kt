package com.shahaabapps.twotracks.session

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** A running study timer for one goal component. Start time is persisted so it survives process death. */
data class TimerState(
    val goalId: String,
    val componentId: String,
    val sessionId: String?,
    val startedAtMillis: Long,
) {
    fun elapsedMinutes(now: Long = System.currentTimeMillis()): Int = ((now - startedAtMillis) / 60_000L).toInt()
}

@Singleton
class StudyTimer @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences("study_timer", Context.MODE_PRIVATE)

    private val _state = MutableStateFlow(load())
    val state: StateFlow<TimerState?> = _state

    fun start(goalId: String, componentId: String, sessionId: String?) {
        val s = TimerState(goalId, componentId, sessionId, System.currentTimeMillis())
        prefs.edit()
            .putString("goalId", goalId).putString("componentId", componentId)
            .putString("sessionId", sessionId).putLong("startedAt", s.startedAtMillis)
            .apply()
        _state.value = s
    }

    /** Stops and returns the elapsed state (caller logs it). */
    fun stop(): TimerState? {
        val s = _state.value
        prefs.edit().clear().apply()
        _state.value = null
        return s
    }

    private fun load(): TimerState? {
        val goalId = prefs.getString("goalId", null) ?: return null
        val componentId = prefs.getString("componentId", null) ?: return null
        val startedAt = prefs.getLong("startedAt", 0L)
        if (startedAt <= 0L) return null
        return TimerState(goalId, componentId, prefs.getString("sessionId", null), startedAt)
    }
}
