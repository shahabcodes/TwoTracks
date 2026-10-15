package com.shahaabapps.twotracks.feature.listen

import android.content.Context
import android.net.Uri
import com.shahaabapps.core.content.Podcast
import com.shahaabapps.core.content.PodcastTimeline
import com.shahaabapps.core.content.Podcasts
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The AZ-204 episodes, which ship inside the app: an Opus file and its timing
 * for each topic, under assets/podcasts. Nothing is downloaded, so an episode
 * plays offline from the first time, and a topic whose episode has not been
 * rendered yet simply has no Listen card.
 *
 * tools/podcast/install.sh is what puts a render here.
 */
@Singleton
class Episodes @Inject constructor(@ApplicationContext private val context: Context) {
    private val timelines = ConcurrentHashMap<String, Result<PodcastTimeline?>>()

    /** The script, if this topic has an episode in the app. */
    fun podcast(topicId: String): Podcast? = Podcasts.forTopic(topicId)?.takeIf { timeline(topicId) != null }

    /** When each line is spoken, read once from the asset beside the audio. */
    fun timeline(topicId: String): PodcastTimeline? = timelines.getOrPut(topicId) {
        runCatching {
            try {
                context.assets.open("$DIR/$topicId.json").bufferedReader().use { PodcastTimeline.parse(it.readText()) }
            } catch (e: IOException) {
                null
            }
        }
    }.getOrNull()

    fun has(topicId: String): Boolean = podcast(topicId) != null

    companion object {
        const val DIR = "podcasts"
        fun uri(topicId: String): Uri = Uri.parse("asset:///$DIR/$topicId.ogg")
    }
}
