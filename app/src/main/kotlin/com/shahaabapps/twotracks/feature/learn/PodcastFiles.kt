package com.shahaabapps.twotracks.feature.learn

import com.shahaabapps.core.content.PodcastTimeline
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * The AZ-204 episodes on the phone: downloaded once from the podcasts-v1
 * release on first play, and played from the device after that, offline.
 *
 * Each episode is two files, the audio and its timing. A file is written
 * under a temporary name and only renamed into place once it has arrived in
 * full, so a connection that drops halfway can never leave something that
 * looks like a finished episode. An episode counts as downloaded only when
 * both files are there and the timing reads as a timeline.
 */
class PodcastFiles(
    private val dir: File,
    private val fetch: (url: String, into: File) -> Unit = ::httpFetch,
) {
    fun audio(topicId: String): File = File(dir, "$topicId.ogg")
    private fun timingFile(topicId: String): File = File(dir, "$topicId.json")

    /** The timeline, if this episode is fully on the phone. */
    fun timeline(topicId: String): PodcastTimeline? {
        val timing = timingFile(topicId)
        if (!audio(topicId).isFile || !timing.isFile) return null
        return PodcastTimeline.parse(timing.readText())
    }

    fun isDownloaded(topicId: String): Boolean = timeline(topicId) != null

    /**
     * Fetches both files for [topicId], timing first since it is tiny and
     * proves the episode exists before the audio is fetched. Returns the
     * timeline, or throws if either file could not be had; nothing half
     * downloaded is left behind either way.
     */
    fun download(topicId: String): PodcastTimeline {
        dir.mkdirs()
        place("$RELEASE/$topicId.json", timingFile(topicId))
        val timeline = PodcastTimeline.parse(timingFile(topicId).readText())
        if (timeline == null) {
            timingFile(topicId).delete()
            throw IOException("The timing for $topicId is not a timeline")
        }
        place("$RELEASE/$topicId.ogg", audio(topicId))
        return timeline
    }

    /** Removes an episode, for when a newer render replaces it. */
    fun forget(topicId: String) {
        audio(topicId).delete()
        timingFile(topicId).delete()
    }

    private fun place(url: String, target: File) {
        val part = File(target.parentFile, target.name + ".part")
        try {
            fetch(url, part)
            if (!part.isFile || part.length() == 0L) throw IOException("Nothing arrived for $url")
            if (target.exists()) target.delete()
            if (!part.renameTo(target)) throw IOException("Could not move ${part.name} into place")
        } finally {
            part.delete()
        }
    }

    companion object {
        /** Where publish.sh puts the episodes. */
        const val RELEASE = "https://github.com/shahabcodes/TwoTracks/releases/download/podcasts-v1"
    }
}

/**
 * Downloads [url] into [into], following GitHub's redirect from the release
 * page to the storage host the asset actually lives on.
 */
internal fun httpFetch(url: String, into: File) {
    var next = URL(url)
    repeat(5) {
        val c = next.openConnection() as HttpURLConnection
        c.instanceFollowRedirects = false
        c.connectTimeout = 15_000
        c.readTimeout = 30_000
        try {
            when (val code = c.responseCode) {
                in 200..299 -> {
                    c.inputStream.use { input -> into.outputStream().use { input.copyTo(it) } }
                    return
                }
                in 300..399 -> next = URL(next, c.getHeaderField("Location") ?: throw IOException("Redirect with no location from $next"))
                else -> throw IOException("HTTP $code for $next")
            }
        } finally {
            c.disconnect()
        }
    }
    throw IOException("Too many redirects for $url")
}
