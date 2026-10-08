package com.shahaabapps.twotracks.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
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
import com.shahaabapps.core.content.WidgetSnapshot
import com.shahaabapps.twotracks.MainActivity

/**
 * Home-screen widgets.
 *
 * Each one reads [WidgetStore] and nothing else: no Firestore, no session, no
 * network. They are deliberately plain — Glance is a restricted subset of
 * Compose and a widget that tries to look like the app's cards ends up
 * looking like a broken version of them. These lean on [GlanceTheme], so they
 * follow the system's light and dark and, on Android 12 and later, the user's
 * wallpaper colours.
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

// ------------------------------------------------------------------ pieces

@Composable
private fun Card(onClick: Intent?, content: @Composable () -> Unit) {
    val base = GlanceModifier
        .fillMaxSize()
        .background(GlanceTheme.colors.widgetBackground)
        .cornerRadius(20.dp)
        .padding(14.dp)
    Column(
        modifier = if (onClick != null) base.clickable(actionStartActivity(onClick)) else base,
        verticalAlignment = Alignment.Vertical.Top,
        content = { content() },
    )
}

@Composable
private fun Title(text: String) = Text(
    text,
    style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.Medium),
)

@Composable
private fun Headline(text: String) = Text(
    text,
    maxLines = 2,
    style = TextStyle(color = GlanceTheme.colors.onSurface, fontSize = 20.sp, fontWeight = FontWeight.Bold),
)

@Composable
private fun Body(text: String, lines: Int = 2) = Text(
    text,
    maxLines = lines,
    style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant, fontSize = 13.sp),
)

/** A row of pips, filled on the left, used for the loop and the shared run. */
@Composable
private fun Pips(filled: Int, total: Int) {
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

// ----------------------------------------------------------------- widgets

/** Today's loop: one topic, the words due, the questions back round. */
class TodayStudyWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val s = WidgetStore.read(context)
        provideContent {
            GlanceTheme {
                Card(openRoute(context, s.nextTopicId.takeIf { it.isNotBlank() }?.let { WidgetRoutes.topic(s.nextCourseId, it) })) {
                    Title("TODAY'S STUDY")
                    Spacer(GlanceModifier.height(4.dp))
                    when {
                        s.stepsTotal == 0 -> {
                            Headline("Nothing queued")
                            Body("Open a course to start the day.")
                        }
                        s.loopClosed -> {
                            Headline("Done for today")
                            Body("Everything due is cleared.")
                        }
                        else -> {
                            Headline("${s.stepsDone} of ${s.stepsTotal} done")
                            Body("${s.minutesLeft} min left" + if (s.nextLabel.isNotBlank()) " · ${s.nextLabel}" else "")
                        }
                    }
                    Spacer(GlanceModifier.height(8.dp))
                    if (s.stepsTotal > 0) Pips(filled = s.stepsDone, total = s.stepsTotal)
                }
            }
        }
    }
}

/** Questions the spaced schedule has brought back round. */
class DailyReviewWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val s = WidgetStore.read(context)
        provideContent {
            GlanceTheme {
                Card(openRoute(context, s.questionsCourseId.takeIf { it.isNotBlank() }?.let { WidgetRoutes.dailyReview(it) })) {
                    Title("DAILY REVIEW")
                    Spacer(GlanceModifier.height(4.dp))
                    if (s.questionsDue > 0) {
                        Headline("${s.questionsDue} due")
                        Body("Questions coming back before they fade.")
                    } else {
                        Headline("All held")
                        Body("Nothing due today.")
                    }
                }
            }
        }
    }
}

/**
 * One word, all day. The only widget here that teaches rather than nags:
 * seeing a word twenty times while unlocking the phone is free exposure.
 */
class WordOfDayWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val s = WidgetStore.read(context)
        provideContent {
            GlanceTheme {
                Card(openRoute(context, s.wordCourseId.takeIf { it.isNotBlank() }?.let { WidgetRoutes.deck(it) })) {
                    Title("WORD OF THE DAY")
                    Spacer(GlanceModifier.height(4.dp))
                    if (s.hasWord) {
                        Headline(s.word)
                        Body(s.wordMeaning, lines = 1)
                        if (s.wordExample.isNotBlank()) {
                            Spacer(GlanceModifier.height(6.dp))
                            Body(s.wordExample, lines = 2)
                        }
                    } else {
                        Headline("No words yet")
                        Body("Read a Words topic and they start appearing here.")
                    }
                }
            }
        }
    }
}

/** The shared run, and whose turn it is. */
class TogetherWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val s = WidgetStore.read(context)
        provideContent {
            GlanceTheme {
                Card(openRoute(context, null)) {
                    Title("TOGETHER")
                    Spacer(GlanceModifier.height(4.dp))
                    val them = s.partnerName.ifBlank { "Your partner" }
                    when (s.togetherState) {
                        3 -> {
                            Headline(if (s.togetherStreak > 1) "${s.togetherStreak} days running" else "Both studied today")
                            Body("You both showed up today.")
                        }
                        1 -> {
                            Headline("Your part is done")
                            Body("$them has not studied yet.")
                        }
                        2 -> {
                            Headline("$them has studied")
                            Body("Your turn, to keep the run going.")
                        }
                        else -> {
                            Headline(if (s.togetherStreak > 0) "${s.togetherStreak} days running" else "Study together")
                            Body(if (s.togetherStreak > 0) "Still alive until the day is out." else "A day counts when you both study.")
                        }
                    }
                    Spacer(GlanceModifier.height(8.dp))
                    if (s.togetherStreak > 0) Pips(filled = minOf(s.togetherStreak, 7), total = 7)
                }
            }
        }
    }
}

// --------------------------------------------------------------- receivers

class TodayStudyWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TodayStudyWidget()
}

class DailyReviewWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DailyReviewWidget()
}

class WordOfDayWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WordOfDayWidget()
}

class TogetherWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TogetherWidget()
}
