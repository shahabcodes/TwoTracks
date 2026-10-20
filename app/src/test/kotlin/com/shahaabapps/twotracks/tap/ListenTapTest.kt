package com.shahaabapps.twotracks.tap

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.performSemanticsAction
import com.shahaabapps.core.content.PodcastCue
import com.shahaabapps.core.ui.theme.AppLook
import com.shahaabapps.twotracks.feature.learn.TopicContent
import com.shahaabapps.twotracks.feature.listen.ListenContent
import com.shahaabapps.twotracks.feature.listen.NowPlayingBar
import com.shahaabapps.twotracks.preview.ListenSamples
import org.junit.Test

/** The podcast player, the bar that follows you round the app, and the way in from a topic. */
class ListenTapTest(look: AppLook) : TapTest(look) {

    private fun player(playing: Boolean = true) = show {
        ListenContent(
            ListenSamples.listen(playing = playing), onBack = record("close"), onPlayPause = record("playPause"),
            onBack15 = record("back15"), onForward30 = record("forward30"), onSeek = record1("seek"), onSpeed = record1("speed"),
            onStop = record("stop"), onLine = record1("line"),
        )
    }

    @Test
    fun `the player's transport reaches play, pause, the skips and stop`() {
        player()
        tapAction("Pause")
        expect("playPause")
        tapAction("Back 15 seconds")
        expect("back15")
        tapAction("Forward 30 seconds")
        expect("forward30")
        if (board) tap("Stop") else tapAction("Stop")
        expect("stop")
        tapAction("Close the player")
        expect("close")
    }

    @Test
    fun `a paused player offers play`() {
        player(playing = false)
        tapAction("Play")
        expect("playPause")
    }

    @Test
    fun `the speed steps up from where it is`() {
        player()
        // The sample plays at 1.2: Parallel's chip steps to the next speed, the board picks one outright.
        if (board) tap("1.5×") else tapAction("Change the speed")
        expect("speed(1.5)")
    }

    @Test
    fun `a line of the transcript plays from where it starts`() {
        player()
        if (!board) {
            tapAction("Show the transcript")
            expect()
        } else {
            // The board's player opens on the episode's board; its transcript is a segment away.
            tap("Transcript")
            expect()
        }
        // By its words, since the list keeps itself scrolled to the line being spoken.
        val line = ListenSamples.listen().lines[8]
        tap(PodcastCue.strip(line.text).take(40), substring = true)
        expect("line(8)")
    }

    @Test
    fun `the board and the transcript take turns`() {
        player()
        if (board) {
            // Opens on the board; the transcript and back again are both reachable.
            tap("Transcript")
            expect()
            tap("Board")
            expect()
        } else {
            tapAction("Show the transcript")
            expect()
            tapAction("Show the cover")
            expect()
        }
    }

    @Test
    fun `dragging the scrubber seeks once, where it is let go`() {
        player()
        compose.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress))[0].performSemanticsAction(SemanticsActions.SetProgress) { it(30f) }
        compose.waitForIdle()
        expect("seek(30.0)")
    }

    @Test
    fun `the bar opens the player, pauses and skips on`() {
        show { NowPlayingBar(ListenSamples.nowPlaying(), onOpen = record("open"), onPlayPause = record("playPause"), onForward30 = record("forward30")) }
        tapAction("Open the player")
        expect("open")
        tapAction("Pause")
        expect("playPause")
        tapAction("Forward 30 seconds")
        expect("forward30")
    }

    @Test
    fun `an AZ-204 topic opens its episode`() {
        show {
            TopicContent(
                ListenSamples.topic(), speaker = null, voiceMissing = false, recognizer = null, onBack = record("back"), onOpenTopic = record2("topic"), onOpenQuiz = record2("quiz"),
                onInstallVoice = record("voice"), onChecked = { _, _, _, _ -> calls += "checked" }, onConfidence = { _, _ -> calls += "confidence" },
                onMarkLearned = record("learned"), onUnlearn = record("unlearn"), onMessageShown = {}, onListen = record("listen"),
            )
        }
        tapAction("Listen to this topic")
        expect("listen")
    }
}
