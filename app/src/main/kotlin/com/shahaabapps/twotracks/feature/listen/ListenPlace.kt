package com.shahaabapps.twotracks.feature.listen

import android.content.Context
import androidx.media3.session.CommandButton
import com.shahaabapps.core.content.PodcastStyle
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Where you stopped in each episode, and the speed you listen at. Kept on the
 * phone only: it is a bookmark, not study, and listening is deliberately not
 * counted as hours.
 *
 * The playback service writes it as it plays and the screens read it, so a
 * player reopened tomorrow puts you back where you left off.
 */
@Singleton
class ListenPlace @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("listen", Context.MODE_PRIVATE)

    fun position(topicId: String): Double = prefs.getFloat("at:$topicId", 0f).toDouble()

    fun save(topicId: String, seconds: Double) {
        prefs.edit().putFloat("at:$topicId", seconds.toFloat()).apply()
    }

    fun clear(topicId: String) {
        prefs.edit().remove("at:$topicId").apply()
    }

    var speed: Float
        get() = ListenSpeeds.nearest(prefs.getFloat("speed", 1f))
        set(value) { prefs.edit().putFloat("speed", value).apply() }

    /** The telling last picked, offered first on a topic that has it. */
    var style: PodcastStyle
        get() = PodcastStyle.entries.firstOrNull { it.name == prefs.getString("style", null) } ?: PodcastStyle.CLASSIC
        set(value) { prefs.edit().putString("style", value.name).apply() }
}

/**
 * The speeds on offer. Exactly the ones the system's media controls have an
 * icon for, so the button in the notification always shows the speed you
 * are actually hearing, as the speed chip in the app does.
 */
object ListenSpeeds {
    val ALL = listOf(0.8f, 1.0f, 1.2f, 1.5f, 1.8f, 2.0f)

    fun nearest(speed: Float): Float = ALL.minBy { kotlin.math.abs(it - speed) }

    /** The next speed up, wrapping from the fastest back to the slowest. */
    fun next(speed: Float): Float = ALL[(ALL.indexOf(nearest(speed)) + 1) % ALL.size]

    fun label(speed: Float): String {
        val s = nearest(speed)
        return (if (s % 1f == 0f) s.toInt().toString() else s.toString()) + "×"
    }

    fun icon(speed: Float): Int = when (nearest(speed)) {
        0.8f -> CommandButton.ICON_PLAYBACK_SPEED_0_8
        1.2f -> CommandButton.ICON_PLAYBACK_SPEED_1_2
        1.5f -> CommandButton.ICON_PLAYBACK_SPEED_1_5
        1.8f -> CommandButton.ICON_PLAYBACK_SPEED_1_8
        2.0f -> CommandButton.ICON_PLAYBACK_SPEED_2_0
        else -> CommandButton.ICON_PLAYBACK_SPEED_1_0
    }
}
