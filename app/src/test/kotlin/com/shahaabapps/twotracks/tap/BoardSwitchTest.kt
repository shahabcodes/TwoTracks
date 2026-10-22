package com.shahaabapps.twotracks.tap

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.shahaabapps.core.content.PodcastBoards
import com.shahaabapps.core.content.Podcasts
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.twotracks.feature.listen.EpisodeBoard
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Switching telling with a style pill hands the board a different episode's
 * board while the old scene is still fading out. The outgoing scene must be
 * drawn from its own board: looked up in the new one, a scene past the new
 * board's end crashed the app.
 */
class BoardSwitchTest(look: AppLook) : TapTest(look) {

    @Test
    fun `switching to a telling with fewer scenes, deep into the episode, does not crash`() {
        // A topic whose tellings have different numbers of scenes: from late in
        // the longer one to the start of the shorter one.
        val pair = Podcasts.all.mapNotNull { p -> PodcastBoards.forEpisode(p.episodeId)?.let { p to it } }
            .groupBy { it.first.topicId }.values
            .mapNotNull { tellings ->
                val long = tellings.maxByOrNull { it.second.scenes.size } ?: return@mapNotNull null
                val short = tellings.minByOrNull { it.second.scenes.size } ?: return@mapNotNull null
                if (long.second.scenes.size > short.second.scenes.size) long.second to short.second else null
            }.firstOrNull()
        assertNotNull("no topic has tellings with different scene counts", pair)
        val (long, short) = pair!!
        var shown by mutableStateOf(long)
        var line by mutableStateOf<Int?>(long.scenes.last().fromLine)
        show { EpisodeBoard(shown, line, playing = true) }
        compose.mainClock.autoAdvance = false
        shown = short
        line = 0
        // Through the cross-fade, frame by frame, where the outgoing scene is still drawn.
        repeat(30) { compose.mainClock.advanceTimeByFrame() }
        compose.mainClock.autoAdvance = true
        compose.waitForIdle()
    }
}
