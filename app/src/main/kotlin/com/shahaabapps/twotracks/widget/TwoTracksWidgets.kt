package com.shahaabapps.twotracks.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.compose.ui.graphics.Color
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
import androidx.glance.unit.ColorProvider
import com.shahaabapps.core.content.Courses
import com.shahaabapps.core.content.StudyKind
import com.shahaabapps.core.content.WidgetSnapshot
import com.shahaabapps.twotracks.AppActivity
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
    Intent(context, AppActivity::class.java).apply {
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

/** The colours of the card being drawn. See [paletteFor]. */
private val LocalWidgetPalette = compositionLocalOf { paletteFor(WidgetConfig(), Color.White, Color.Black, Color.Gray) }

@Composable
private fun Card(
    onClick: Intent?,
    config: WidgetConfig = WidgetConfig(),
    content: @Composable (compact: Boolean) -> Unit,
) {
    val compact = LocalSize.current.height < COMPACT_BELOW
    // The widget's own look, not the phone's. A home screen is one wallpaper
    // with everything on it, so which of these reads well is a matter of what
    // is behind it, and only the person looking at it knows that.
    val ctx = LocalContext.current
    val palette = paletteFor(
        config,
        themed = GlanceTheme.colors.widgetBackground.getColor(ctx),
        themedStrong = GlanceTheme.colors.onSurface.getColor(ctx),
        themedSoft = GlanceTheme.colors.onSurfaceVariant.getColor(ctx),
    )
    val base = GlanceModifier
        .fillMaxSize()
        .background(palette.background)
        .cornerRadius(config.corners.radius.dp)
        .padding(horizontal = 14.dp, vertical = if (compact) 8.dp else 14.dp)
    // Centred, not top-aligned. A launcher decides the height and often gives
    // more than the content needs; centred text reads as deliberate at any
    // size, where top-aligned text leaves a slab of empty card beneath it.
    CompositionLocalProvider(LocalWidgetPalette provides palette) {
        Column(
            modifier = if (onClick != null) base.clickable(actionStartActivity(onClick)) else base,
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.Start,
            content = { content(compact) },
        )
    }
}

/** The ink for the current card. */
private val strongInk: ColorProvider
    @Composable get() = ColorProvider(LocalWidgetPalette.current.strong)

/** The quieter ink, for labels and second lines. */
private val softInk: ColorProvider
    @Composable get() = ColorProvider(LocalWidgetPalette.current.soft)

@Composable
private fun Title(text: String, compact: Boolean) {
    if (compact) return
    Text(
        text,
        style = TextStyle(color = softInk, fontSize = 11.sp, fontWeight = FontWeight.Medium),
    )
    Spacer(GlanceModifier.height(4.dp))
}

@Composable
private fun Headline(text: String, compact: Boolean) = Text(
    text,
    maxLines = if (compact) 1 else 2,
    style = TextStyle(
        color = strongInk,
        fontSize = if (compact) 15.sp else 20.sp,
        fontWeight = FontWeight.Bold,
    ),
)

@Composable
private fun Body(text: String, compact: Boolean, lines: Int = 2) = Text(
    text,
    maxLines = if (compact) 1 else lines,
    style = TextStyle(color = softInk, fontSize = if (compact) 12.sp else 13.sp),
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
                    .background(
                        ColorProvider(
                            LocalWidgetPalette.current.let { if (i < filled) it.pipOn else it.pipOff },
                        ),
                    ),
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

/**
 * Every widget draws whatever its own settings say.
 *
 * The four providers exist so the launcher's picker can offer four things,
 * but a placed widget is not tied to the one it came from: its settings name
 * the content, so a widget dropped as "Together" can be turned into your
 * partner's German words without being deleted and put back.
 */
private suspend fun GlanceAppWidget.renderConfigured(
    context: Context,
    id: GlanceId,
    fallback: WidgetContent,
) {
    val appWidgetId = runCatching { GlanceAppWidgetManager(context).getAppWidgetId(id) }.getOrNull()
    // Never configured, or the settings could not be read: the provider it
    // was dropped from is the best guess at what was wanted.
    val config = appWidgetId
        ?.let { runCatching { WidgetConfigStore.read(context, it) }.getOrNull() }
        ?: WidgetConfig(content = fallback)
    val s = WidgetStore.read(context, config.snapshotKey)
    provideContent {
        GlanceTheme {
            WidgetBody(config, s, context)
        }
    }
}

/**
 * One face, drawn.
 *
 * What it says is decided in [faceFor], which the preview in Settings reads
 * too: a preview that disagreed with the widget would be worse than no
 * preview, and two copies of these four branches is how that happens.
 */
@Composable
private fun WidgetBody(config: WidgetConfig, s: WidgetSnapshot, context: Context) {
    val face = s.faceFor(config, LocalDate.now())
    Card(openRoute(context, face.route), config) { compact ->
        Title(face.title, compact)
        Headline(face.headline, compact)
        Body(face.body, compact, lines = if (face.extra == null) 2 else 1)
        face.extra?.takeIf { !compact }?.let {
            Spacer(GlanceModifier.height(6.dp))
            Body(it, compact, lines = 2)
        }
        if (config.showPips && face.hasPips) Pips(filled = face.pipsFilled, total = face.pipsTotal, compact = compact)
    }
}

/** Today's loop: one topic, the words due, the questions back round. */
class TodayStudyWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact
    override suspend fun provideGlance(context: Context, id: GlanceId) =
        renderConfigured(context, id, WidgetContent.TODAY_STUDY)
}

/** Questions the spaced schedule has brought back round. */
class DailyReviewWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact
    override suspend fun provideGlance(context: Context, id: GlanceId) =
        renderConfigured(context, id, WidgetContent.DAILY_REVIEW)
}

/**
 * One word, all day. The only widget here that teaches rather than nags:
 * seeing a word twenty times while unlocking the phone is free exposure. It
 * shows a term from a glossary where a course has no vocabulary, so a
 * certification course is not a blank card for good.
 */
class WordOfDayWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact
    override suspend fun provideGlance(context: Context, id: GlanceId) =
        renderConfigured(context, id, WidgetContent.WORD_OF_DAY)
}

/** The shared run, and whose turn it is. */
class TogetherWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Exact
    override suspend fun provideGlance(context: Context, id: GlanceId) =
        renderConfigured(context, id, WidgetContent.TOGETHER)
}

/**
 * The provider that draws a given content, so the configuration screen can
 * redraw a widget the moment its settings change rather than leaving the
 * launcher showing the loading layout until something else happens.
 *
 * Any provider can draw any content, so which one is used here matters only
 * in that it must be the one the launcher has for that widget id; Glance
 * resolves that from the id itself.
 */
internal fun WidgetContent.widget(): GlanceAppWidget = when (this) {
    WidgetContent.TODAY_STUDY -> TodayStudyWidget()
    WidgetContent.DAILY_REVIEW -> DailyReviewWidget()
    WidgetContent.WORD_OF_DAY -> WordOfDayWidget()
    WidgetContent.TOGETHER -> TogetherWidget()
}

/** Redraws one placed widget by its launcher id. */
internal suspend fun GlanceAppWidget.update(context: android.content.Context, appWidgetId: Int) {
    val glanceId = GlanceAppWidgetManager(context).getGlanceIdBy(appWidgetId)
    update(context, glanceId)
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
