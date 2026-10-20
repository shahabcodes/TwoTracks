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
data class Podcast(
    val topicId: String,
    val lines: List<PodcastLine>,
    val series: PodcastSeries = PodcastSeries.AZ204,
    val style: PodcastStyle = PodcastStyle.CLASSIC,
) {
    /**
     * What the episode is known by: its audio and timing files, the player's
     * media id, the place you left it. A classic episode is its topic's id, as
     * every episode was before there were styles, so nothing saved changes.
     */
    val episodeId: String get() = if (style == PodcastStyle.CLASSIC) topicId else "$topicId--${style.slug}"

    /** The words as heard, without cues. */
    val spokenWords: Int get() = lines.sumOf { PodcastCue.strip(it.text).split(Regex("\\s+")).count { w -> w.isNotBlank() } }

    /** About how long it runs, at the hosts' easy pace of 150 words a minute. */
    val minutes: Int get() = ((spokenWords + 75) / 150).coerceAtLeast(1)
}

/** One line of an episode. A German line from [Host.JONAS] carries its English [translation]. */
data class PodcastLine(val host: Host, val text: String, val translation: String? = null)

/**
 * The voices. [MAYA] explains and [LEO] asks, in English; [JONAS] is the
 * German episodes' native speaker, and says every German word and sentence,
 * so the German a learner copies is never said with an English accent.
 */
enum class Host(val displayName: String) { MAYA("Maya"), LEO("Leo"), JONAS("Jonas") }

/** Which show an episode belongs to: what the player calls it and who is on it. */
enum class PodcastSeries(val label: String, val hosts: String) {
    AZ204("AZ-204", "Maya & Leo"),
    GERMAN_A1("German A1", "Maya, Leo & Jonas"),
}

/**
 * The same topic told another way, for a listener to pick from. [CLASSIC] is
 * the plain explanation every topic has; the others tell it as a story and
 * are there for some topics, beside it, never instead of it.
 */
enum class PodcastStyle(val label: String, val slug: String) {
    CLASSIC("Classic", ""),

    /** A mystery: a broken app, the clues, the wrong suspects, and the service that solves it. */
    CASE_FILES("Case Files", "case-files"),

    /** The office gossip: what a made-up colleague got wrong, and what everyone learnt from it. */
    GOSSIP("Gossip", "gossip"),
}

/** The cues a line may carry, and how to take them out for the transcript. */
object PodcastCue {
    /** Sounds the voice makes. */
    val SOUNDS = setOf("laugh", "chuckle", "sigh")

    /** Silences. */
    val PAUSES = setOf("pause", "long pause")

    /**
     * Repeat after me, on a line of Jonas's: he says it slowly, leaves you the
     * time to say it yourself, then says it at normal speed.
     */
    const val REPEAT = "repeat"

    val ALL = SOUNDS + PAUSES + REPEAT

    private val cue = Regex("""\[([a-z ]+)]""")

    /** Every cue in [text], in order. */
    fun cues(text: String): List<String> = cue.findAll(text).map { it.groupValues[1] }.toList()

    /**
     * A German word or phrase inside Maya's or Leo's English, written «so».
     * The render tool says it with German sounds in the host's own voice, so
     * "Guten Abend" is not read as English; the transcript shows it plainly.
     */
    val GERMAN = Regex("«([^»]+)»")

    /** [text] with its cues and German marks removed, and the spaces they leave tidied. */
    fun strip(text: String): String = text.replace(cue, " ").replace(GERMAN, "$1").replace(Regex("\\s+"), " ").replace(Regex(" ([,.!?])"), "$1").trim()
}

/** The episodes there are. A topic without one simply has no player. */
object Podcasts {
    val all: List<Podcast> by lazy { Az204Podcasts.all + Az204StyledPodcasts.all + GermanPodcasts.all }

    /** A topic's classic episode, the one every topic with episodes has. */
    fun forTopic(topicId: String): Podcast? = all.firstOrNull { it.topicId == topicId && it.style == PodcastStyle.CLASSIC }

    /** Every telling of a topic, classic first, in the order the styles are listed. */
    fun styles(topicId: String): List<Podcast> = all.filter { it.topicId == topicId }.sortedBy { it.style.ordinal }

    /** The episode by its id: a topic's id for its classic episode, "topic--style" for another. */
    fun episode(episodeId: String): Podcast? = all.firstOrNull { it.episodeId == episodeId }
}

internal fun maya(text: String) = PodcastLine(Host.MAYA, text.trimIndent().replace("\n", " "))
internal fun leo(text: String) = PodcastLine(Host.LEO, text.trimIndent().replace("\n", " "))
internal fun jonas(de: String, en: String) = PodcastLine(Host.JONAS, de.trimIndent().replace("\n", " "), en.trimIndent().replace("\n", " "))
