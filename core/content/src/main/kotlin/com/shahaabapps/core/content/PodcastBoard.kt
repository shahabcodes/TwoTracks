package com.shahaabapps.core.content

/**
 * What the player shows while an episode plays, in place of the cover: a
 * board that builds up as the hosts talk, so a listener who glances at the
 * screen sees the thing being described.
 *
 * It follows the line being spoken rather than the clock, so it needs no
 * timing of its own: the render tool's timeline already says which line is
 * playing, and a re-render that moves every line by a second moves the board
 * with it. Like the episode it says nothing the topic does not teach.
 *
 * An episode is a run of [Scene]s, each from one line until the next scene
 * starts. A scene holds one diagram, drawn as the learning screens draw
 * theirs, and its [Beat]s: at a given line, bring nodes in, light some of
 * them, run a pulse along an edge, or put up a card with a term or a line of
 * code. PodcastBoardTest holds every id, line and order to the script.
 */
data class PodcastBoard(val topicId: String, val scenes: List<Scene>) {

    /** What to draw while [line] is being spoken, or before the first line when null. */
    fun frameAt(line: Int?): BoardFrame {
        val at = line ?: 0
        val sceneIndex = scenes.indexOfLast { it.fromLine <= at }.coerceAtLeast(0)
        val scene = scenes[sceneIndex]
        val past = scene.beats.filter { it.atLine <= at }
        val latest = past.lastOrNull()
        return BoardFrame(
            scene = scene,
            sceneIndex = sceneIndex,
            shown = scene.alwaysShown + past.flatMap { it.show },
            lit = latest?.light.orEmpty(),
            // A pulse runs only while its own line is spoken; a card stays up
            // until another replaces it, so a glance a line late still finds it.
            pulse = latest?.takeIf { it.atLine == at }?.pulse,
            card = past.lastOrNull { it.card != null }?.card,
        )
    }
}

/**
 * One diagram and how it builds. Nodes no beat brings in are there from the
 * scene's first line; the rest arrive with their beat.
 */
data class Scene(
    val fromLine: Int,
    /** A few words over the board: what this part of the episode is about. */
    val heading: String,
    val diagram: DiagramSpec,
    val beats: List<Beat> = emptyList(),
) {
    val alwaysShown: Set<String> get() = diagram.nodes.map { it.id }.toSet() - beats.flatMap { it.show }.toSet()
}

/** What changes on the board when line [atLine] starts. */
data class Beat(
    val atLine: Int,
    /** Nodes that arrive now and stay. */
    val show: Set<String> = emptySet(),
    /** Nodes lit while this beat is the latest; the rest dim. Empty lights nothing. */
    val light: Set<String> = emptySet(),
    /** An edge, from and to, that a pulse runs along while this line is spoken. */
    val pulse: Pair<String, String>? = null,
    val card: BoardCard? = null,
)

/** A term and what it means, or a line of [code], under the diagram. */
data class BoardCard(val title: String, val body: String, val code: Boolean = false)

/** The board at one moment. */
data class BoardFrame(
    val scene: Scene,
    val sceneIndex: Int,
    val shown: Set<String>,
    val lit: Set<String>,
    val pulse: Pair<String, String>?,
    val card: BoardCard?,
)

/** The boards there are. An episode without one shows its cover. */
object PodcastBoards {
    val all: List<PodcastBoard> by lazy { Az204Boards.all }

    fun forTopic(topicId: String): PodcastBoard? = all.firstOrNull { it.topicId == topicId }
}
