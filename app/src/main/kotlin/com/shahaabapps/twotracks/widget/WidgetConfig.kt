package com.shahaabapps.twotracks.widget

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

/** What a widget shows. Any placed widget can be pointed at any of these. */
enum class WidgetContent(val label: String, val blurb: String) {
    TODAY_STUDY("Today's study", "The day's topic, words and questions, ticked off as you go"),
    DAILY_REVIEW("Daily review", "Questions coming back round before they fade"),
    WORD_OF_DAY("Word of the day", "One word from the deck, with its meaning"),
    TOGETHER("Together", "Days in a row you have both studied"),
}

/** Whose numbers it shows. */
enum class WidgetOwner(val label: String) { ME("Mine"), PARTNER("My partner's") }

/** Which theme it draws in, regardless of what the phone is doing. */
enum class WidgetTheme(val label: String) { FOLLOW("Follow the app"), LIGHT("Light"), DARK("Dark") }

/**
 * One placed widget's settings.
 *
 * Kept per widget rather than per provider, so two copies of the same widget
 * can show different things — your German words beside their IELTS review is
 * the case this exists for.
 *
 * @param courseId null for every course at once, which is the default and
 *   what the widgets did before they could be configured.
 */
data class WidgetConfig(
    val content: WidgetContent = WidgetContent.TODAY_STUDY,
    val owner: WidgetOwner = WidgetOwner.ME,
    val courseId: String? = null,
    val theme: WidgetTheme = WidgetTheme.FOLLOW,
    val translucent: Boolean = false,
) {
    /** The snapshot this widget reads: one is kept per person and course. */
    val snapshotKey: String get() = snapshotKey(owner, courseId)

    companion object {
        fun snapshotKey(owner: WidgetOwner, courseId: String?): String =
            "${owner.name}|${courseId ?: "all"}"
    }
}

/**
 * Where each widget's settings live.
 *
 * Keyed by the launcher's own widget id, which is the only identifier that
 * survives the widget being moved, resized or the app being reinstalled. The
 * ids are handed out by the system and reused, so a widget's settings are
 * cleared when it is removed, or the next widget to take that id inherits
 * someone else's choices.
 */
object WidgetConfigStore {

    suspend fun read(context: Context, appWidgetId: Int): WidgetConfig {
        val prefs = runCatching { context.widgetDataStore.data.first() }.getOrNull() ?: return WidgetConfig()
        fun str(name: String) = prefs[stringPreferencesKey(key(appWidgetId, name))]
        return WidgetConfig(
            content = str("content")?.let { v -> WidgetContent.entries.firstOrNull { it.name == v } } ?: WidgetConfig().content,
            owner = str("owner")?.let { v -> WidgetOwner.entries.firstOrNull { it.name == v } } ?: WidgetOwner.ME,
            courseId = str("course")?.takeIf { it.isNotBlank() },
            theme = str("theme")?.let { v -> WidgetTheme.entries.firstOrNull { it.name == v } } ?: WidgetTheme.FOLLOW,
            translucent = prefs[booleanPreferencesKey(key(appWidgetId, "translucent"))] ?: false,
        )
    }

    suspend fun write(context: Context, appWidgetId: Int, config: WidgetConfig) {
        context.widgetDataStore.edit { p ->
            p[stringPreferencesKey(key(appWidgetId, "content"))] = config.content.name
            p[stringPreferencesKey(key(appWidgetId, "owner"))] = config.owner.name
            p[stringPreferencesKey(key(appWidgetId, "course"))] = config.courseId.orEmpty()
            p[stringPreferencesKey(key(appWidgetId, "theme"))] = config.theme.name
            p[booleanPreferencesKey(key(appWidgetId, "translucent"))] = config.translucent
        }
    }

    suspend fun clear(context: Context, appWidgetId: Int) {
        context.widgetDataStore.edit { p ->
            listOf("content", "owner", "course", "theme").forEach {
                p.remove(stringPreferencesKey(key(appWidgetId, it)))
            }
            p.remove(booleanPreferencesKey(key(appWidgetId, "translucent")))
        }
    }

    private fun key(appWidgetId: Int, name: String) = "w${appWidgetId}_$name"
}

/** One widget actually on the home screen, with the settings it is running. */
data class PlacedWidget(val appWidgetId: Int, val config: WidgetConfig)

/**
 * Every widget of ours the launcher is currently showing.
 *
 * Asked of each provider in turn, because a placed widget belongs to the one
 * it was dropped from even though its settings decide what it draws. Ordered
 * by id so the list does not reshuffle between visits.
 */
suspend fun placedWidgets(context: android.content.Context): List<PlacedWidget> = runCatching {
    val manager = androidx.glance.appwidget.GlanceAppWidgetManager(context)
    val providers = listOf(
        TodayStudyWidget::class.java,
        DailyReviewWidget::class.java,
        WordOfDayWidget::class.java,
        TogetherWidget::class.java,
    )
    providers
        .flatMap { runCatching { manager.getGlanceIds(it) }.getOrDefault(emptyList()) }
        .mapNotNull { glanceId -> runCatching { manager.getAppWidgetId(glanceId) }.getOrNull() }
        .distinct()
        .sorted()
        .map { id -> PlacedWidget(id, WidgetConfigStore.read(context, id)) }
}.getOrDefault(emptyList())
