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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
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

    fun start() {
        scope.launch {
            combine(
                sessionStore.state,
                dataStore.topicProgress,
                dataStore.quizAttempts,
                dataStore.flashcards,
                dataStore.logs,
            ) { state, progress, attempts, cards, logs ->
                val session = (state as? SessionState.Ready)?.session ?: return@combine WidgetSnapshot()
                val me = session.myRole
                val them = if (me == PersonRole.TECH) PersonRole.TEACHER else PersonRole.TECH
                val today = Clock.today(session.me)
                val byCourse = Courses.all.associate { it.id to CourseProgress.of(me, it.id, progress, attempts) }
                val mineDates = logs.filter { it.owner == me }.map { it.dateIso }
                val theirDates = logs.filter { it.owner == them }.map { it.dateIso }
                WidgetSnapshot.of(
                    owner = me,
                    courses = Courses.all,
                    progressByCourse = byCourse,
                    topicProgress = progress,
                    attempts = attempts,
                    flashcards = cards,
                    togetherStreak = Streaks.together(mineDates, theirDates, today),
                    togetherState = togetherCode(Streaks.togetherToday(mineDates, theirDates, today)),
                    partnerName = session.partner?.name.orEmpty(),
                    today = today,
                )
            }.distinctUntilChanged().collect { snapshot ->
                runCatching {
                    WidgetStore.write(context, snapshot)
                    widgets.forEach { it.updateAll(context) }
                }
            }
        }
    }
}
