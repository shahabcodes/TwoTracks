package com.shahaabapps.twotracks.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.updateAll
import com.shahaabapps.core.content.CourseProgress
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.WidgetSnapshot
import com.shahaabapps.core.model.PersonRole
import com.shahaabapps.core.planner.Streaks
import com.shahaabapps.twotracks.session.Clock
import com.shahaabapps.twotracks.session.CoupleDataStore
import com.shahaabapps.twotracks.session.SessionState
import com.shahaabapps.twotracks.session.SessionStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Keeps the widgets' snapshot in step with the app.
 *
 * It watches the same collections the Learn and Today screens watch, rebuilds
 * [WidgetSnapshot] whenever they move, and asks the launcher to redraw. The
 * widgets themselves never compute anything, so they cannot disagree with the
 * screens: there is one derivation and they both read its output.
 *
 * Everything here is best-effort. A widget that fails to update shows an
 * older number; a widget that throws shows the launcher's error box until the
 * phone restarts, so nothing in this file is allowed to propagate.
 */
@Singleton
class WidgetRefresher @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionStore: SessionStore,
    private val dataStore: CoupleDataStore,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val widgets: List<GlanceAppWidget> =
        listOf(TodayStudyWidget(), DailyReviewWidget(), WordOfDayWidget(), TogetherWidget())

    /**
     * The snapshot is flat, so who-owes-today travels as a number. Spelled out
     * rather than taken from ordinal, so reordering the enum cannot silently
     * swap "your turn" for "their turn" on someone's home screen.
     */
    private fun togetherCode(t: Streaks.Together): Int = when (t) {
        Streaks.Together.NEITHER -> 0
        Streaks.Together.ONLY_ME -> 1
        Streaks.Together.ONLY_THEM -> 2
        Streaks.Together.BOTH -> 3
    }

    /** The last time a snapshot was written, so four receivers waking at once do the work once. */
    private val lastPushedAt = AtomicLong(0)

    fun start() {
        scope.launch { snapshots().collect { push(it) } }
    }

    /**
     * Recompute once, now. Called when the launcher adds a widget or asks one
     * to update, which is the only chance a widget has to fill itself in when
     * the app has not been opened.
     */
    suspend fun refreshNow() {
        runCatching {
            if (System.currentTimeMillis() - lastPushedAt.get() < MIN_GAP_MS) return
            withTimeoutOrNull(WAIT_FOR_DATA_MS) { snapshots().first() }?.let { push(it) }
        }
    }

    /**
     * Snapshots as the couple's data moves. Nothing is emitted until there is
     * a session to build one from: an empty snapshot written over a good one
     * leaves every widget reading as a quiet day, and it survives until the
     * app is next opened.
     */
    private fun snapshots(): Flow<Map<String, WidgetSnapshot>> =
        combine(
            sessionStore.state,
            dataStore.topicProgress,
            dataStore.quizAttempts,
            dataStore.flashcards,
            dataStore.logs,
        ) { state, progress, attempts, cards, logs ->
            @Suppress("UNUSED_EXPRESSION")
            val session = (state as? SessionState.Ready)?.session
                ?: return@combine null as Map<String, WidgetSnapshot>?
            val me = session.myRole
            val them = if (me == PersonRole.TECH) PersonRole.TEACHER else PersonRole.TECH
            val today = Clock.today(session.me)
            val mineDates = logs.filter { it.owner == me }.map { it.dateIso }
            val theirDates = logs.filter { it.owner == them }.map { it.dateIso }
            val streak = Streaks.together(mineDates, theirDates, today)
            val togetherState = togetherCode(Streaks.togetherToday(mineDates, theirDates, today))

            // One per person and course, because a widget is configured
            // before anyone asks the app anything: the launcher renders it
            // with no session, so whatever it might need has to be sitting
            // there already. Four courses and two people is eight small
            // flat values, which is cheaper than being clever about it.
            buildMap<String, WidgetSnapshot> {
                listOf(WidgetOwner.ME to me, WidgetOwner.PARTNER to them).forEach { (which, role) ->
                    (listOf(null) + Courses.all.map { it.id }).forEach { courseId ->
                        val courses = Courses.all.filter { courseId == null || it.id == courseId }
                        val byCourse = courses.associate { it.id to CourseProgress.of(role, it.id, progress, attempts) }
                        put(
                            WidgetConfig.snapshotKey(which, courseId),
                            WidgetSnapshot.of(
                                owner = role,
                                courses = courses,
                                progressByCourse = byCourse,
                                topicProgress = progress,
                                attempts = attempts,
                                flashcards = cards,
                                togetherStreak = streak,
                                togetherState = togetherState,
                                // Whose name the Together card nudges: always
                                // the other one from the reader's side.
                                partnerName = if (which == WidgetOwner.ME) session.partner?.name.orEmpty() else session.me.name,
                                today = today,
                            ),
                        )
                    }
                }
            }
        }.filterNotNull().distinctUntilChanged()

    private suspend fun push(snapshots: Map<String, WidgetSnapshot>) {
        runCatching {
            snapshots.forEach { (key, snapshot) -> WidgetStore.write(context, key, snapshot) }
            lastPushedAt.set(System.currentTimeMillis())
            widgets.forEach { it.updateAll(context) }
        }
    }

    private companion object {
        /** How long a cold start may spend waiting for Firestore before giving up. */
        const val WAIT_FOR_DATA_MS = 20_000L
        /** Four receivers wake on one broadcast; only the first needs to recompute. */
        const val MIN_GAP_MS = 60_000L
    }
}
