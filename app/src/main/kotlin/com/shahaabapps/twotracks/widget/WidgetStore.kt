package com.shahaabapps.twotracks.widget

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shahaabapps.core.content.WidgetSnapshot
import kotlinx.coroutines.flow.first

// Shared with WidgetConfigStore: one file holds both the snapshots the
// widgets read and the settings of each placed widget.
internal val Context.widgetDataStore by preferencesDataStore(name = "widget_snapshot")

/**
 * The widgets' own copy of what the app knows.
 *
 * A widget is rendered by the launcher, in a process with no signed-in
 * session and often no network, and it has a few milliseconds to do it. So it
 * never reads Firestore; the app writes this flat snapshot whenever it
 * recomputes the real thing and the widgets read only this.
 *
 * Every read falls back to an empty snapshot rather than throwing, because a
 * widget that crashes shows the launcher's error box until the phone is
 * restarted, which is far worse than a widget showing nothing.
 */
object WidgetStore {

    private fun STEPS_DONE(k: String) = intPreferencesKey("$k.steps_done")
    private fun STEPS_TOTAL(k: String) = intPreferencesKey("$k.steps_total")
    private fun MINUTES_LEFT(k: String) = intPreferencesKey("$k.minutes_left")
    private fun NEXT_LABEL(k: String) = stringPreferencesKey("$k.next_label")
    private fun NEXT_COURSE(k: String) = stringPreferencesKey("$k.next_course")
    private fun NEXT_TOPIC(k: String) = stringPreferencesKey("$k.next_topic")
    private fun NEXT_KIND(k: String) = stringPreferencesKey("$k.next_kind")
    private fun QUESTIONS_DUE(k: String) = intPreferencesKey("$k.questions_due")
    private fun QUESTIONS_COURSE(k: String) = stringPreferencesKey("$k.questions_course")
    private fun WORD(k: String) = stringPreferencesKey("$k.word")
    private fun WORD_MEANING(k: String) = stringPreferencesKey("$k.word_meaning")
    private fun WORD_EXAMPLE(k: String) = stringPreferencesKey("$k.word_example")
    private fun WORD_COURSE(k: String) = stringPreferencesKey("$k.word_course")
    private fun WORD_TOPIC(k: String) = stringPreferencesKey("$k.word_topic")
    private fun WORD_IS_TERM(k: String) = booleanPreferencesKey("$k.word_is_term")
    private fun TOGETHER_STREAK(k: String) = intPreferencesKey("$k.together_streak")
    private fun PARTNER_NAME(k: String) = stringPreferencesKey("$k.partner_name")
    private fun TOGETHER_STATE(k: String) = intPreferencesKey("$k.together_state")
    private fun DATE_ISO(k: String) = stringPreferencesKey("$k.date_iso")

    /**
     * One snapshot per person and course, so a widget set to the partner's
     * German can read its own without the app recomputing anything at the
     * moment the launcher asks.
     */
    suspend fun write(context: Context, key: String, s: WidgetSnapshot) {
        context.widgetDataStore.edit { p ->
            p[STEPS_DONE(key)] = s.stepsDone
            p[STEPS_TOTAL(key)] = s.stepsTotal
            p[MINUTES_LEFT(key)] = s.minutesLeft
            p[NEXT_LABEL(key)] = s.nextLabel
            p[NEXT_COURSE(key)] = s.nextCourseId
            p[NEXT_TOPIC(key)] = s.nextTopicId
            p[NEXT_KIND(key)] = s.nextKind
            p[QUESTIONS_DUE(key)] = s.questionsDue
            p[QUESTIONS_COURSE(key)] = s.questionsCourseId
            p[WORD(key)] = s.word
            p[WORD_MEANING(key)] = s.wordMeaning
            p[WORD_EXAMPLE(key)] = s.wordExample
            p[WORD_COURSE(key)] = s.wordCourseId
            p[WORD_TOPIC(key)] = s.wordTopicId
            p[WORD_IS_TERM(key)] = s.wordIsTerm
            p[TOGETHER_STREAK(key)] = s.togetherStreak
            p[PARTNER_NAME(key)] = s.partnerName
            p[TOGETHER_STATE(key)] = s.togetherState
            p[DATE_ISO(key)] = s.dateIso
        }
    }

    suspend fun read(context: Context, key: String = DEFAULT_KEY): WidgetSnapshot = runCatching {
        context.widgetDataStore.data.first().toSnapshot(key)
    }.getOrDefault(WidgetSnapshot())

    /** What an unconfigured widget reads: the signed-in person, every course. */
    const val DEFAULT_KEY = "ME|all"

    private fun Preferences.toSnapshot(key: String) = WidgetSnapshot(
        stepsDone = this[STEPS_DONE(key)] ?: 0,
        stepsTotal = this[STEPS_TOTAL(key)] ?: 0,
        minutesLeft = this[MINUTES_LEFT(key)] ?: 0,
        nextLabel = this[NEXT_LABEL(key)].orEmpty(),
        nextCourseId = this[NEXT_COURSE(key)].orEmpty(),
        nextTopicId = this[NEXT_TOPIC(key)].orEmpty(),
        nextKind = this[NEXT_KIND(key)].orEmpty(),
        questionsDue = this[QUESTIONS_DUE(key)] ?: 0,
        questionsCourseId = this[QUESTIONS_COURSE(key)].orEmpty(),
        word = this[WORD(key)].orEmpty(),
        wordMeaning = this[WORD_MEANING(key)].orEmpty(),
        wordExample = this[WORD_EXAMPLE(key)].orEmpty(),
        wordCourseId = this[WORD_COURSE(key)].orEmpty(),
        wordTopicId = this[WORD_TOPIC(key)].orEmpty(),
        wordIsTerm = this[WORD_IS_TERM(key)] ?: false,
        togetherStreak = this[TOGETHER_STREAK(key)] ?: 0,
        partnerName = this[PARTNER_NAME(key)].orEmpty(),
        togetherState = this[TOGETHER_STATE(key)] ?: 0,
        dateIso = this[DATE_ISO(key)].orEmpty(),
    )
}
