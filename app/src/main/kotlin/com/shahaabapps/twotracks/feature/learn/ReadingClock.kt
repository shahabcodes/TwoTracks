package com.shahaabapps.twotracks.feature.learn

/**
 * How long this reading has taken, counting only while the lesson is in front
 * of somebody.
 *
 * A clock started when the screen opens and read when the lesson is marked
 * learned counts the walk to the kitchen, the phone in a pocket and the night
 * the app was left open. That is not reading time, and until now it was the
 * number going into the hours: [resumed] and [paused] are what make the
 * difference, and a lesson left and come back to adds its two sittings rather
 * than the gap between them.
 *
 * It takes the clock as an argument rather than reading it, because a timer
 * that can only be tested by waiting is a timer nobody tests.
 */
internal class ReadingClock(private val now: () -> Long = System::currentTimeMillis) {
    private var since: Long? = null
    private var banked = 0L
    private var running = 0L

    /** The lesson is on screen: the clock runs. */
    fun resumed() {
        if (since == null) {
            since = now()
            running = 0L
        }
    }

    /** The lesson is not: what has run so far is kept, and nothing accrues. */
    fun paused() {
        if (since == null) return
        bank()
        since = null
    }

    /** Whole seconds of reading, running or paused. */
    fun seconds(): Int = (millis() / 1_000L).toInt()

    /** Whole minutes, for the hours, which round the way a kitchen clock does. */
    fun minutes(): Int = (millis() / 60_000L).toInt()

    /**
     * Reading so far, in milliseconds.
     *
     * This is the wall clock, which can be put back — by hand, or by the
     * network correcting it — in the middle of a sitting. Reading already
     * done is not taken away for that: when the segment on the clock comes
     * out shorter than it was a moment ago, what had run is banked and the
     * segment starts again from the new time. The cost is the few seconds
     * either side of the jump; the alternative is a lesson that reads as
     * having taken no time at all.
     */
    private fun millis(): Long {
        val started = since ?: return banked + running
        val run = now() - started
        if (run < running) {
            bank()
            since = now()
        } else {
            running = run
        }
        return banked + running
    }

    private fun bank() {
        val started = since
        if (started != null) running = maxOf(running, (now() - started).coerceAtLeast(0L))
        banked += running
        running = 0L
    }
}

/**
 * A length of reading in words: "20 min 30 sec".
 *
 * Seconds are shown because a lesson is a thing you can read in two minutes
 * or forty, and "20 min" for twenty and a half is a rounding the reader can
 * see is a rounding. They stop being shown past an hour, where they are noise
 * against the hours and minutes beside them.
 */
internal fun readingWords(seconds: Int): String {
    if (seconds <= 0) return "no time at all"
    val hours = seconds / 3_600
    val minutes = (seconds % 3_600) / 60
    val secs = seconds % 60
    return when {
        hours > 0 && minutes > 0 -> "$hours hr $minutes min"
        hours > 0 -> "$hours hr"
        minutes > 0 && secs > 0 -> "$minutes min $secs sec"
        minutes > 0 -> "$minutes min"
        else -> "$secs sec"
    }
}

/**
 * The running clock in the header: "4:12", and "1:04:12" once it has been an
 * hour.
 *
 * Digits rather than words, because this one changes every second and is read
 * at a glance. Minutes are not padded and seconds always are, which is how
 * every clock that counts up is written.
 */
internal fun readingClockFace(seconds: Int): String {
    val safe = seconds.coerceAtLeast(0)
    val hours = safe / 3_600
    val minutes = (safe % 3_600) / 60
    val secs = safe % 60
    return if (hours > 0) "$hours:%02d:%02d".format(minutes, secs) else "$minutes:%02d".format(secs)
}

/**
 * What the lesson says about the last time it was read.
 *
 * Addressed to the reader and in the past, because that is what it is: a
 * thing they did, not a target to beat. Null when there is nothing to say —
 * a lesson never finished, or finished before the clock existed — which is
 * the common case and draws no line at all.
 */
internal fun lastReadingLine(seconds: Int): String? =
    if (seconds <= 0) null else "You read this in ${readingWords(seconds)} last time."
