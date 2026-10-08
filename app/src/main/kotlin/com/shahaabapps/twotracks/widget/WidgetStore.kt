package com.shahaabapps.twotracks.widget

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shahaabapps.core.content.WidgetSnapshot
import kotlinx.coroutines.flow.first

private val Context.widgetDataStore by preferencesDataStore(name = "widget_snapshot")

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

    private val STEPS_DONE = intPreferencesKey("steps_done")
    private val STEPS_TOTAL = intPreferencesKey("steps_total")
    private val MINUTES_LEFT = intPreferencesKey("minutes_left")
    private val NEXT_LABEL = stringPreferencesKey("next_label")
    private val NEXT_COURSE = stringPreferencesKey("next_course")
    private val NEXT_TOPIC = stringPreferencesKey("next_topic")
    private val QUESTIONS_DUE = intPreferencesKey("questions_due")
    private val QUESTIONS_COURSE = stringPreferencesKey("questions_course")
    private val WORD = stringPreferencesKey("word")
    private val WORD_MEANING = stringPreferencesKey("word_meaning")
    private val WORD_EXAMPLE = stringPreferencesKey("word_example")
    private val WORD_COURSE = stringPreferencesKey("word_course")
    private val TOGETHER_STREAK = intPreferencesKey("together_streak")
    private val PARTNER_NAME = stringPreferencesKey("partner_name")
    private val TOGETHER_STATE = intPreferencesKey("together_state")
    private val DATE_ISO = stringPreferencesKey("date_iso")

    suspend fun write(context: Context, s: WidgetSnapshot) {
        context.widgetDataStore.edit { p ->
            p[STEPS_DONE] = s.stepsDone
            p[STEPS_TOTAL] = s.stepsTotal
            p[MINUTES_LEFT] = s.minutesLeft
            p[NEXT_LABEL] = s.nextLabel
            p[NEXT_COURSE] = s.nextCourseId
            p[NEXT_TOPIC] = s.nextTopicId
            p[QUESTIONS_DUE] = s.questionsDue
            p[QUESTIONS_COURSE] = s.questionsCourseId
            p[WORD] = s.word
            p[WORD_MEANING] = s.wordMeaning
            p[WORD_EXAMPLE] = s.wordExample
            p[WORD_COURSE] = s.wordCourseId
            p[TOGETHER_STREAK] = s.togetherStreak
            p[PARTNER_NAME] = s.partnerName
            p[TOGETHER_STATE] = s.togetherState
            p[DATE_ISO] = s.dateIso
        }
    }

    suspend fun read(context: Context): WidgetSnapshot = runCatching {
        context.widgetDataStore.data.first().toSnapshot()
    }.getOrDefault(WidgetSnapshot())

    private fun Preferences.toSnapshot() = WidgetSnapshot(
        stepsDone = this[STEPS_DONE] ?: 0,
        stepsTotal = this[STEPS_TOTAL] ?: 0,
        minutesLeft = this[MINUTES_LEFT] ?: 0,
        nextLabel = this[NEXT_LABEL].orEmpty(),
        nextCourseId = this[NEXT_COURSE].orEmpty(),
        nextTopicId = this[NEXT_TOPIC].orEmpty(),
        questionsDue = this[QUESTIONS_DUE] ?: 0,
        questionsCourseId = this[QUESTIONS_COURSE].orEmpty(),
        word = this[WORD].orEmpty(),
        wordMeaning = this[WORD_MEANING].orEmpty(),
        wordExample = this[WORD_EXAMPLE].orEmpty(),
        wordCourseId = this[WORD_COURSE].orEmpty(),
        togetherStreak = this[TOGETHER_STREAK] ?: 0,
        partnerName = this[PARTNER_NAME].orEmpty(),
        togetherState = this[TOGETHER_STATE] ?: 0,
        dateIso = this[DATE_ISO].orEmpty(),
    )
}
