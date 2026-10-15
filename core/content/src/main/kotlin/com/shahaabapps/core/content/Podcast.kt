package com.shahaabapps.core.content

/**
 * A short episode for one topic, talked through by two fictional hosts: Maya,
 * who explains, and Leo, who asks what a learner would ask. It is a way back
 * into a topic already read, on a walk or a commute, not a replacement for
 * reading it: it says nothing the topic does not teach.
 *
 * The words are the transcript the app shows beside the player and the
 * script the audio is rendered from, so there is one text and it cannot
 * drift from what is heard. A line may carry cues for the voice, written in
 * square brackets where they fall: [laugh], [chuckle] and [sigh] are sounds
 * the voice makes, [pause] and [long pause] are silence. Pronunciation is not
 * written into the text; the render tool keeps its own list, so the
 * transcript reads as English rather than as instructions to a synthesiser.
 */
data class Podcast(val topicId: String, val lines: List<PodcastLine>) {
    /** The words as heard, without cues. */
    val spokenWords: Int get() = lines.sumOf { PodcastCue.strip(it.text).split(Regex("\\s+")).count { w -> w.isNotBlank() } }

    /** About how long it runs, at the hosts' easy pace of 150 words a minute. */
    val minutes: Int get() = ((spokenWords + 75) / 150).coerceAtLeast(1)
}

data class PodcastLine(val host: Host, val text: String)

/** The two voices. [MAYA] explains; [LEO] asks. */
enum class Host(val displayName: String) { MAYA("Maya"), LEO("Leo") }

/** The cues a line may carry, and how to take them out for the transcript. */
object PodcastCue {
    /** Sounds the voice makes. */
    val SOUNDS = setOf("laugh", "chuckle", "sigh")

    /** Silences. */
    val PAUSES = setOf("pause", "long pause")

    val ALL = SOUNDS + PAUSES

    private val cue = Regex("""\[([a-z ]+)]""")

    /** Every cue in [text], in order. */
    fun cues(text: String): List<String> = cue.findAll(text).map { it.groupValues[1] }.toList()

    /** [text] with its cues removed and the spaces they leave tidied. */
    fun strip(text: String): String = text.replace(cue, " ").replace(Regex("\\s+"), " ").replace(Regex(" ([,.!?])"), "$1").trim()
}

/** The episodes there are. A topic without one simply has no player. */
object Podcasts {
    val all: List<Podcast> by lazy { Az204Podcasts.all }

    fun forTopic(topicId: String): Podcast? = all.firstOrNull { it.topicId == topicId }
}

internal fun maya(text: String) = PodcastLine(Host.MAYA, text.trimIndent().replace("\n", " "))
internal fun leo(text: String) = PodcastLine(Host.LEO, text.trimIndent().replace("\n", " "))
