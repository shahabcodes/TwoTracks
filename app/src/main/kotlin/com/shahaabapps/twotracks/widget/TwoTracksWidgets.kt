package com.shahaabapps.twotracks.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.StudyKind
import com.shahaabapps.core.content.WidgetSnapshot
import com.shahaabapps.twotracks.MainActivity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Home-screen widgets.
 *
 * Each one reads [WidgetStore] and nothing else: no Firestore, no session, no
 * network. They are deliberately plain — Glance is a restricted subset of
 * Compose and a widget that tries to look like the app's cards ends up
 * looking like a broken version of them. These lean on [GlanceTheme], so they
 * follow the system's light and dark and, on Android 12 and later, the user's
 * wallpaper colours.
 *
 * Two rules hold throughout. A widget never states a number it cannot stand
 * behind: an unwritten or yesterday's snapshot asks for a tap instead of
 * reporting a quiet day. And a widget never ends in a full stop: when there is
 * nothing due it points at the next thing rather than saying so and stopping.
 */

/** Tapping a widget opens the app, and where sensible, the screen it is about. */
internal fun openRoute(context: Context, route: String?): Intent =
    Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        route?.let { putExtra(WidgetRoutes.EXTRA, it) }
        // A distinct action per route so the launcher does not reuse a
        // PendingIntent built for a different destination.
        action = "com.shahaabapps.twotracks.WIDGET_${route ?: "OPEN"}"
    }

object WidgetRoutes {
    const val EXTRA = "tt_widget_route"
    fun topic(courseId: String, topicId: String) = "learn/$courseId/topic/$topicId"
    fun deck(courseId: String) = "learn/$courseId/deck"
    fun dailyReview(courseId: String) = "learn/$courseId/quiz/${Courses.DAILY_MODULE_ID}"
}

/**
 * The route to the next outstanding thing, or null when the snapshot does not
 * name one.
 *
 * Each kind of work lands somewhere different, and only a topic carries a
 * topic id: the words step opens the deck and the questions step opens the
 * daily sitting, both of which the course id alone is enough to name. Reading
 * the kind is what keeps a card saying "Words: 15 waiting" from dropping the
 * reader on Today.
 */
internal fun WidgetSnapshot.nextRoute(): String? {
    if (nextCourseId.isBlank()) return null
    return when (nextKind) {
        StudyKind.TOPIC.name -> nextTopicId.takeIf { it.isNotBlank() }?.let { WidgetRoutes.topic(nextCourseId, it) }
        StudyKind.WORDS.name -> WidgetRoutes.deck(nextCourseId)
        StudyKind.QUESTIONS.name -> WidgetRoutes.dailyReview(nextCourseId)
        else -> null
    }
}

// ------------------------------------------------------------------ pieces

/**
 * Below this a card has room for two lines and no more, so the label, the
 * example sentence and the pips are dropped and the type comes down a size.
 * A launcher hands out whatever height it likes; everything here has to read
 * properly at one cell and at three.
 */
private val COMPACT_BELOW = 92.dp

@Composable
private fun Card(onClick: Intent?, content: @Composable (compact: Boolean) -> Unit) {
    val compact = LocalSize.current.height < COMPACT_BELOW
    val base = GlanceModifier
        .fillMaxSize()
        .background(GlanceTheme.colors.widgetBackground)
        .cornerRadius(20.dp)
        .padding(horizontal = 14.dp, vertical = if (compact) 8.dp else 14.dp)
    // Centred, not top-aligned. A launcher decides the height and often gives
    // more than the content needs; centred text reads as deliberate at any
    // size, where top-aligned text leaves a slab of empty card beneath it.
    Column(
        modifier = if (onClick != null) base.clickable(actionStartActivity(onClick)) else base,
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.Start,
        content = { content(compact) },
    )
}

@Composable
private fun Title(text: String, compact: Boolean) {
    if (compact) return
    Text(
        text,
        style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.Medium),
    )
    Spacer(GlanceModifier.height(4.dp))
}

@Composable
private fun Headline(text: String, compact: Boolean) = Text(
    text,
    maxLines = if (compact) 1 else 2,
    style = TextStyle(
        color = GlanceTheme.colors.onSurface,
        fontSize = if (compact) 15.sp else 20.sp,
        fontWeight = FontWeight.Bold,
    ),
)

@Composable
private fun Body(text: String, compact: Boolean, lines: Int = 2) = Text(
    text,
    maxLines = if (compact) 1 else lines,
    style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant, fontSize = if (compact) 12.sp else 13.sp),
)

/** A row of pips, filled on the left, used for the loop and the shared run. */
@Composable
private fun Pips(filled: Int, total: Int, compact: Boolean) {
    if (compact) return
    Spacer(GlanceModifier.height(8.dp))
    Row(modifier = GlanceModifier.fillMaxWidth()) {
        repeat(total) { i ->
            Column(
                modifier = GlanceModifier
                    .size(if (i < filled) 10.dp else 8.dp)
                    .cornerRadius(5.dp)
                    .background(if (i < filled) GlanceTheme.colors.primary else GlanceTheme.colors.surfaceVariant),
                content = {},
            )
            Spacer(GlanceModifier.width(6.dp))
        }
    }
}

/**
 * What a widget shows when it has no snapshot it can trust. Said plainly,
 * because a widget that has never been filled in must not be mistaken for a
 * day with nothing to do.
 */
@Composable
private fun NeedsSync(compact: Boolean) {
    Headline("Open TwoTracks", compact)
    Body("Tap to fill the widget in.", compact, lines = 1)
}

// ----------------------------------------------------------------- widgets

/** Today's loop: one topic, the words due, the questions back round. */
class TodayStudyWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val s = WidgetStore.read(context)
        val fresh = s.fresh(LocalDate.now())
        provideContent {
            GlanceTheme {
                Card(openRoute(context, if (fresh) s.nextRoute() else null)) { compact ->
                    Title("TODAY'S STUDY", compact)
                    when {
                        !fresh -> NeedsSync(compact)
                        s.stepsTotal == 0 -> {
                            Headline("Nothing queued", compact)
                            Body("Open a course to start the day.", compact)
                        }
                        s.loopClosed -> {
                            Headline("Done for today", compact)
                            Body("Everything due is cleared.", compact)
                        }
                        else -> {
                            Headline("${s.stepsDone} of ${s.stepsTotal} done", compact)
                            Body("${s.minutesLeft} min left" + if (s.nextLabel.isNotBlank()) " · ${s.nextLabel}" else "", compact)
                        }
                    }
                    if (fresh && s.stepsTotal > 0) Pips(filled = s.stepsDone, total = s.stepsTotal, compact = compact)
                }
            }
        }
    }
}

/** Questions the spaced schedule has brought back round. */
class DailyReviewWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val s = WidgetStore.read(context)
        val fresh = s.fresh(LocalDate.now())
        val due = s.questionsDue > 0 && s.questionsCourseId.isNotBlank()
        // With nothing due this points at the next topic rather than stopping:
        // "nothing to review" is true most of the way through a course, and a
        // widget that only ever says so is a widget worth removing.
        val route = if (!fresh) null else if (due) WidgetRoutes.dailyReview(s.questionsCourseId) else s.nextRoute()
        provideContent {
            GlanceTheme {
                Card(openRoute(context, route)) { compact ->
                    Title("DAILY REVIEW", compact)
                    when {
                        !fresh -> NeedsSync(compact)
                        due -> {
                            Headline("${s.questionsDue} due", compact)
                            Body("Questions coming back before they fade.", compact)
                        }
                        s.nextLabel.isNotBlank() -> {
                            Headline("Nothing due", compact)
                            Body("Next up · ${s.nextLabel}", compact)
                        }
                        else -> {
                            Headline("All held", compact)
                            Body("Nothing has come back round yet.", compact)
                        }
                    }
                }
            }
        }
    }
}

/**
 * One word, all day. The only widget here that teaches rather than nags:
 * seeing a word twenty times while unlocking the phone is free exposure. It
 * shows a term from a glossary where a course has no vocabulary, so a
 * certification course is not a blank card for good.
 */
class WordOfDayWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val s = WidgetStore.read(context)
        // A word from yesterday is still a word worth meeting, so this one
        // does not need a snapshot written today, only one written at all.
        val route = when {
            !s.hasWord || s.wordCourseId.isBlank() -> null
            s.wordIsTerm && s.wordTopicId.isNotBlank() -> WidgetRoutes.topic(s.wordCourseId, s.wordTopicId)
            s.wordIsTerm -> null
            else -> WidgetRoutes.deck(s.wordCourseId)
        }
        provideContent {
            GlanceTheme {
                Card(openRoute(context, route)) { compact ->
                    Title(if (s.wordIsTerm) "TERM OF THE DAY" else "WORD OF THE DAY", compact)
                    when {
                        s.hasWord -> {
                            Headline(s.word, compact)
                            Body(s.wordMeaning, compact, lines = 1)
                            if (s.wordExample.isNotBlank() && !compact) {
                                Spacer(GlanceModifier.height(6.dp))
                                Body(s.wordExample, compact, lines = 2)
                            }
                        }
                        !s.synced -> NeedsSync(compact)
                        else -> {
                            Headline("Nothing yet", compact)
                            Body("Read a topic and its terms appear here.", compact)
                        }
                    }
                }
            }
        }
    }
}

/** The shared run, and whose turn it is. */
class TogetherWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val s = WidgetStore.read(context)
        val fresh = s.fresh(LocalDate.now())
        provideContent {
            GlanceTheme {
                Card(openRoute(context, null)) { compact ->
                    Title("TOGETHER", compact)
                    val them = s.partnerName.ifBlank { "Your partner" }
                    when {
                        !fresh -> NeedsSync(compact)
                        s.togetherState == 3 -> {
                            Headline(if (s.togetherStreak > 1) "${s.togetherStreak} days running" else "Both studied today", compact)
                            Body("You both showed up today.", compact)
                        }
                        s.togetherState == 1 -> {
                            Headline("Your part is done", compact)
                            Body("$them has not studied yet.", compact)
                        }
                        s.togetherState == 2 -> {
                            Headline("$them has studied", compact)
                            Body("Your turn, to keep the run going.", compact)
                        }
                        else -> {
                            Headline(if (s.togetherStreak > 0) "${s.togetherStreak} days running" else "Study together", compact)
                            Body(
                                if (s.togetherStreak > 0) "Still alive until the day is out." else "A day counts when you both study.",
                                compact,
                            )
                        }
                    }
                    if (fresh && s.togetherStreak > 0) {
                        Pips(filled = minOf(s.togetherStreak, 7), total = 7, compact = compact)
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------------- receivers

@EntryPoint
@InstallIn(SingletonComponent::class)
internal interface WidgetEntryPoint {
    fun widgetRefresher(): WidgetRefresher
}

private val refreshScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

/**
 * Receivers that recompute, not just redraw.
 *
 * [GlanceAppWidgetReceiver.onUpdate] re-renders from the stored snapshot, which
 * for a widget added while the app was closed means rendering nothing. This is
 * the only moment a widget gets to ask for fresh data, so it asks: on being
 * added, and on each update the launcher schedules.
 */
abstract class RefreshingWidgetReceiver : GlanceAppWidgetReceiver() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val refresher = runCatching {
            EntryPointAccessors.fromApplication(
                context.applicationContext,
                WidgetEntryPoint::class.java,
            ).widgetRefresher()
        }.getOrNull() ?: return
        refreshScope.launch { refresher.refreshNow() }
    }
}

class TodayStudyWidgetReceiver : RefreshingWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TodayStudyWidget()
}

class DailyReviewWidgetReceiver : RefreshingWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DailyReviewWidget()
}

class WordOfDayWidgetReceiver : RefreshingWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WordOfDayWidget()
}

class TogetherWidgetReceiver : RefreshingWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TogetherWidget()
}
