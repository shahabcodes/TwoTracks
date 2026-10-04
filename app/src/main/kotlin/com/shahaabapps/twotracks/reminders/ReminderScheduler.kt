package com.shahaabapps.twotracks.reminders

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.shahaabapps.core.model.SessionStatus
import com.shahaabapps.core.planner.Dates
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Schedules one exact alarm for the next planned session start (in the user's
 * own timezone). The receiver posts the notification and asks for the next one.
 * Also schedules a post-class prompt 5 minutes after a CLASS session ends.
 */
@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
    private val sessionPlanning: SessionPlanning,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    fun refresh() {
        scope.launch { scheduleNext() }
    }

    suspend fun scheduleNext() {
        val session = sessionStore.current() ?: return
        val me = session.me
        val zone = Clock.zone(me)
        val now = Clock.now(me)
        val today = now.toLocalDate()
        val sessions = withTimeoutOrNull(5_000) {
            sessionPlanning.ensurePlanned(today)
            sessionPlanning.sessionsBetween(today, today.plusDays(7)).first()
        }.orEmpty().filter { it.owner == me.role && it.status == SessionStatus.PLANNED }
        // The shared goals flow may be idle (no screen collecting); wait briefly for a real value.
        val goals = withTimeoutOrNull(5_000) { dataStore.goals.first { it.isNotEmpty() } }.orEmpty()

        data class Candidate(val atMillis: Long, val title: String, val text: String)

        val candidates = sessions.flatMap { s ->
            val date = Dates.parseOrNull(s.dateIso) ?: return@flatMap emptyList()
            val goal = goals.firstOrNull { it.id == s.goalId }
            val component = goal?.components?.firstOrNull { it.id == s.componentId }
            val what = listOfNotNull(goal?.title, component?.name).joinToString(": ")
            val start = date.atTime(LocalTime.of(s.startMinutes / 60, s.startMinutes % 60)).atZone(zone)
            val end = date.atTime(LocalTime.of(s.endMinutes / 60, s.endMinutes % 60)).atZone(zone).plusMinutes(5)
            buildList {
                if (s.isClass) {
                    add(Candidate(start.minusMinutes(15).toInstant().toEpochMilli(), "Class in 15 minutes", what))
                    add(Candidate(end.toInstant().toEpochMilli(), "How did class go?", "Log what today's $what class covered."))
                } else {
                    add(Candidate(start.toInstant().toEpochMilli(), "Time to study", "$what (${s.durationMinutes} min). Tap to start the timer."))
                }
            }
        }
        val next = candidates.filter { it.atMillis > now.toInstant().toEpochMilli() }.minByOrNull { it.atMillis }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pending = pendingIntent(next?.title.orEmpty(), next?.text.orEmpty())
        alarmManager.cancel(pending)
        if (next == null) return
        val canExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()
        if (canExact) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next.atMillis, pending)
        } else {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next.atMillis, pending)
        }
    }

    private fun pendingIntent(title: String, text: String): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java)
            .putExtra(ReminderReceiver.EXTRA_TITLE, title)
            .putExtra(ReminderReceiver.EXTRA_TEXT, text)
        return PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private companion object {
        const val REQUEST_CODE = 42
    }
}
