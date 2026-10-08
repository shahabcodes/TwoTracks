package com.shahaabapps.core.content

/**
 * Marking the one step a faded example leaves out.
 *
 * Forgiving on purpose: the point is whether the learner knows which thing
 * goes there, not whether they typed it with the same capitals and
 * punctuation as the author. "Staging.", "the staging slot" and "staging" all
 * count. It reuses [WritingMarker.normalise], so the rules are the same ones
 * the writing tasks are marked by.
 */
fun Block.Faded.accepts(typed: String): Boolean {
    val t = " " + WritingMarker.normalise(typed) + " "
    return answers.any { a ->
        val n = WritingMarker.normalise(a)
        // Whole words, so "cool" does not answer a step that wanted "cooldown".
        n.isNotEmpty() && t.contains(" $n ")
    }
}

/** The step's text with the blank filled in, for the reveal. */
fun Block.Faded.solved(): String = steps[blank].replace("___", answers.first())
