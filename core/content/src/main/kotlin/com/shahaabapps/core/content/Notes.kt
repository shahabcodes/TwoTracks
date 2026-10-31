package com.shahaabapps.core.content

/**
 * The important-notes sheet for one topic: the page you read on the way into
 * the exam room, and the page you read while the episode plays.
 *
 * It is not a second topic. The topic teaches, at length, with checks and a
 * worked example; this is what a reader who has already read it wants back
 * in thirty seconds — the handful of definitions the exam leans on, one
 * picture of how the pieces sit together, one table that settles the choice
 * the exam likes to ask about, and the commands with the moment each one is
 * the right command.
 *
 * It is built from the same [Block] types the topic uses, and drawn by the
 * same renderer, so nothing can be written here that no screen can show —
 * which is the rule that cost this codebase 44 invisible option notes.
 *
 * One sheet per topic, which is also one per podcast episode: an episode is
 * keyed on its topic, so the sheet a listener opens from the player is the
 * sheet a reader opens from the topic.
 */
data class TopicNotes(
    val topicId: String,
    /** What the topic is, in one sentence, for the top of the sheet. */
    val headline: String,
    val blocks: List<Block>,
) {
    val definitions: List<Term> get() = blocks.filterIsInstance<Block.Glossary>().flatMap { it.terms }
    val commands: List<CommandNote> get() = blocks.filterIsInstance<Block.Commands>().flatMap { it.items }
    val pictures: Int get() = blocks.count { it is Block.Diagram }
    val tables: Int get() = blocks.count { it is Block.Table || it is Block.Compare }
}

/**
 * The sheets there are. A topic without one simply has no notes link, the
 * same way a topic without an episode has no player.
 */
object Notes {
    val all: List<TopicNotes> by lazy { Ai200Notes.all + Ai200Notes2.all + Ai200Notes3.all + Az204Notes.all + Az204Notes2.all }

    fun forTopic(topicId: String): TopicNotes? = all.firstOrNull { it.topicId == topicId }

    /** Whether this topic has a sheet, for a screen deciding whether to offer one. */
    fun has(topicId: String): Boolean = forTopic(topicId) != null
}

/**
 * Shorthand for a sheet, so every sheet in the app is laid out in the same
 * order: what it is, the words, the picture, the choice, the commands, the
 * traps. A reader who has opened one knows where to look in all of them.
 */
internal fun notes(
    topicId: String,
    headline: String,
    vararg blocks: Block,
) = TopicNotes(topicId, headline.trimIndent().replace("\n", " ").trim(), blocks.toList())
