package com.shahaabapps.twotracks.widget

import androidx.compose.ui.graphics.Color
import com.shahaabapps.core.content.WidgetSnapshot
import java.time.LocalDate

/**
 * What a widget has to say, worked out once and drawn twice.
 *
 * The widget itself is Glance and the preview in Settings is ordinary
 * Compose, and the two cannot share a composable. They can share this: the
 * words, the pips and where a tap goes are decided here, so a preview that
 * disagrees with the widget is a bug in one renderer rather than a second
 * opinion about what the widget means.
 *
 * @param extra a second, quieter line, used only by the word of the day and
 *   dropped when the widget is too short for it.
 */
data class WidgetFace(
    val title: String,
    val headline: String,
    val body: String,
    val extra: String? = null,
    val pipsFilled: Int = 0,
    val pipsTotal: Int = 0,
    val route: String? = null,
) {
    val hasPips: Boolean get() = pipsTotal > 0
}

/** What this widget shows today, given its settings. */
fun WidgetSnapshot.faceFor(config: WidgetConfig, today: LocalDate): WidgetFace {
    val fresh = fresh(today)
    return when (config.content) {
        WidgetContent.TODAY_STUDY -> todayFace(fresh)
        WidgetContent.DAILY_REVIEW -> reviewFace(fresh)
        WidgetContent.WORD_OF_DAY -> wordFace()
        WidgetContent.TOGETHER -> togetherFace(fresh)
    }
}

/**
 * What a widget shows when it has no snapshot it can trust. Said plainly,
 * because a widget that has never been filled in must not be mistaken for a
 * day with nothing to do.
 */
private fun needsSync(title: String) = WidgetFace(
    title = title,
    headline = "Open TwoTracks",
    body = "Tap to fill the widget in.",
)

private fun WidgetSnapshot.todayFace(fresh: Boolean): WidgetFace {
    val title = "TODAY'S STUDY"
    if (!fresh) return needsSync(title)
    val pips = if (stepsTotal > 0) stepsDone to stepsTotal else 0 to 0
    val route = nextRoute()
    return when {
        stepsTotal == 0 -> WidgetFace(title, "Nothing queued", "Open a course to start the day.", route = route)
        loopClosed -> WidgetFace(title, "Done for today", "Everything due is cleared.", pipsFilled = pips.first, pipsTotal = pips.second, route = route)
        else -> WidgetFace(
            title,
            "$stepsDone of $stepsTotal done",
            "$minutesLeft min left" + if (nextLabel.isNotBlank()) " · $nextLabel" else "",
            pipsFilled = pips.first, pipsTotal = pips.second, route = route,
        )
    }
}

private fun WidgetSnapshot.reviewFace(fresh: Boolean): WidgetFace {
    val title = "DAILY REVIEW"
    if (!fresh) return needsSync(title)
    val due = questionsDue > 0 && questionsCourseId.isNotBlank()
    // With nothing due this points at the next topic rather than stopping:
    // "nothing to review" is true most of the way through a course, and a
    // widget that only ever says so is worth removing.
    val route = if (due) WidgetRoutes.dailyReview(questionsCourseId) else nextRoute()
    return when {
        due -> WidgetFace(title, "$questionsDue due", "Questions coming back before they fade.", route = route)
        nextLabel.isNotBlank() -> WidgetFace(title, "Nothing due", "Next up · $nextLabel", route = route)
        else -> WidgetFace(title, "All held", "Nothing has come back round yet.", route = route)
    }
}

private fun WidgetSnapshot.wordFace(): WidgetFace {
    // A word from yesterday is still a word worth meeting, so this one does
    // not need a snapshot written today, only one written at all.
    val title = if (wordIsTerm) "TERM OF THE DAY" else "WORD OF THE DAY"
    val route = when {
        !hasWord || wordCourseId.isBlank() -> null
        wordIsTerm && wordTopicId.isNotBlank() -> WidgetRoutes.topic(wordCourseId, wordTopicId)
        wordIsTerm -> null
        else -> WidgetRoutes.deck(wordCourseId)
    }
    return when {
        hasWord -> WidgetFace(title, word, wordMeaning, extra = wordExample.takeIf { it.isNotBlank() }, route = route)
        !synced -> needsSync(title)
        else -> WidgetFace(title, "Nothing yet", "Read a topic and its terms appear here.")
    }
}

private fun WidgetSnapshot.togetherFace(fresh: Boolean): WidgetFace {
    val title = "TOGETHER"
    if (!fresh) return needsSync(title)
    val them = partnerName.ifBlank { "Your partner" }
    val pips = if (togetherStreak > 0) minOf(togetherStreak, 7) to 7 else 0 to 0
    val (headline, body) = when {
        togetherState == 3 ->
            (if (togetherStreak > 1) "$togetherStreak days running" else "Both studied today") to "You both showed up today."
        togetherState == 1 -> "Your part is done" to "$them has not studied yet."
        togetherState == 2 -> "$them has studied" to "Your turn, to keep the run going."
        else -> (if (togetherStreak > 0) "$togetherStreak days running" else "Study together") to
            (if (togetherStreak > 0) "Still alive until the day is out." else "A day counts when you both study.")
    }
    return WidgetFace(title, headline, body, pipsFilled = pips.first, pipsTotal = pips.second)
}

// ------------------------------------------------------------------ colour

/**
 * The colours one widget draws in, decided once for both renderers.
 *
 * A forced look has to change the ink with the card: keeping the theme's
 * text over a near-black background drew dark on dark and the headline all
 * but vanished.
 */
data class WidgetPalette(
    val background: Color,
    val strong: Color,
    val soft: Color,
    val pipOn: Color,
    val pipOff: Color,
)

private val ON_DARK = Triple(Color.White, Color(0xFFB0B0B5), Color(0xFF48484A))
private val ON_LIGHT = Triple(Color(0xFF1C1C1E), Color(0xFF6E6E73), Color(0xFFD1D1D6))

/**
 * A translucent card is part wallpaper, so it lands between the two themes
 * and the quiet grey that reads on a solid card does not survive it: white at
 * 55 per cent over a dark wallpaper measured about 1.5:1. The wash is heavier
 * and its second line darker, which holds both ways round.
 */
private const val WASH_ALPHA = 0.8f
private val ON_LIGHT_WASH = Color(0xFF48484A)
private val ON_DARK_WASH = Color(0xFFD1D1D6)

/**
 * @param themed the background the phone's own theme would give, used when
 *   the widget is set to follow it.
 * @param themedStrong, themedSoft the same, for ink.
 */
fun paletteFor(
    config: WidgetConfig,
    themed: Color,
    themedStrong: Color,
    themedSoft: Color,
): WidgetPalette {
    val base = when (config.theme) {
        WidgetTheme.LIGHT -> Color.White
        WidgetTheme.DARK -> Color(0xFF111214)
        WidgetTheme.FOLLOW -> themed
    }
    val background = if (config.translucent) base.copy(alpha = WASH_ALPHA) else base
    val accent = config.accent.color
    val (strong, soft, pipOff) = when (config.theme) {
        WidgetTheme.LIGHT -> Triple(ON_LIGHT.first, if (config.translucent) ON_LIGHT_WASH else ON_LIGHT.second, ON_LIGHT.third)
        WidgetTheme.DARK -> Triple(ON_DARK.first, if (config.translucent) ON_DARK_WASH else ON_DARK.second, ON_DARK.third)
        WidgetTheme.FOLLOW -> Triple(themedStrong, themedSoft, themedSoft.copy(alpha = 0.35f))
    }
    return WidgetPalette(
        background = background,
        strong = strong,
        soft = soft,
        pipOn = accent ?: strong,
        // A chosen colour has to be visible before anything is done, or the
        // first day of a course shows a row of grey and the choice looks
        // like it did nothing. The empty ones are the same colour, faded.
        pipOff = accent?.copy(alpha = 0.3f) ?: pipOff,
    )
}
