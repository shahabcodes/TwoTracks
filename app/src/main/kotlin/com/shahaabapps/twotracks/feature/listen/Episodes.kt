package com.shahaabapps.twotracks.feature.listen

import android.content.Context
import com.shahaabapps.core.content.Podcast
import com.shahaabapps.core.content.PodcastTimeline
import com.shahaabapps.core.content.Podcasts
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The episodes the app knows: each one's timing ships under assets/podcasts,
 * so its transcript and board work offline from the start, and a topic whose
 * episode has not been rendered yet simply has no Listen card. The sound is
 * not in the app; EpisodeAudio fetches it from the couple's own Firebase
 * Storage on the first play.
 *
 * tools/podcast/install.sh puts a render's timing here and stages its audio
 * for tools/podcast/upload.ps1.
 */
@Singleton
class Episodes @Inject constructor(@ApplicationContext private val context: Context) {
    private val timelines = ConcurrentHashMap<String, Result<PodcastTimeline?>>()

    /** The script, if this episode is in the app. A classic episode's id is its topic's. */
    fun podcast(episodeId: String): Podcast? = Podcasts.episode(episodeId)?.takeIf { timeline(episodeId) != null }

    /** Every telling of a topic the app carries, classic first. */
    fun styles(topicId: String): List<Podcast> = Podcasts.styles(topicId).filter { has(it.episodeId) }

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

        /** The course an episode's topic is in, for opening its player. */
        fun courseOf(episodeId: String): String? {
            val topicId = Podcasts.episode(episodeId)?.topicId ?: episodeId
            return com.shahaabapps.core.content.Courses.all.firstOrNull { c -> c.modules.any { m -> m.topics.any { it.id == topicId } } }?.id
        }
    }
}
