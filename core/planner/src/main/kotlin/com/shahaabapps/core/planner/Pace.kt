package com.shahaabapps.core.planner

/**
 * How long a question took against how long it ought to have taken.
 *
 * The app has no way to ask someone how sure they were without asking them,
 * and a question that interrupts to ask gets answered dishonestly or not at
 * all. The clock is the honest version of the same question: an answer given
 * in four seconds on a paragraph-long scenario was believed rather than
 * worked out, and one that took two minutes was worked out rather than known.
 *
 * Both readings matter, and they matter in opposite directions. A quick wrong
 * answer is a wrong belief — the person does not know they are wrong, so
 * nothing will correct it except being asked again. A slow right answer is
 * not yet knowledge — it was reconstructed, and stretching the gap before the
 * next sighting is exactly the wrong move.
 */
object Pace {

    enum class Verdict {
        /** Answered far faster than reading it allows: taken on belief. */
        QUICK,

        /** Around the time it should take. */
        STEADY,

        /** Worked out rather than known. */
        SLOW,

        /** No clock for this one: an old attempt, or the paper timed out first. */
        UNKNOWN,
    }

    /** Roughly 200 words a minute, which is ordinary silent reading. */
    private const val WORDS_PER_SECOND = 3.3

    /** Reading the prompt is not answering it. */
    private const val DECIDE_SECONDS = 10

    /** Each option past the first two is another thing to weigh. */
    private const val PER_EXTRA_OPTION_SECONDS = 5

    /** Nothing is expected to take less than this, however short it reads. */
    private const val FLOOR_SECONDS = 12

    /** Under a third of the expectation is faster than the words can be read. */
    private const val QUICK_FRACTION = 3

    /** Over twice the expectation is working it out. */
    private const val SLOW_MULTIPLE = 2

    /**
     * How long [prompt], its [scenario] and its [options] ought to take: the
     * reading, plus a moment to decide, plus a little for every option past
     * the second.
     *
     * Deliberately crude. It is a yardstick for "four seconds is not reading
     * this", not a model of anybody's reading speed, and the two verdicts sit
     * a long way either side of it so that being wrong by a third changes
     * nothing.
     */
    fun expectedSeconds(prompt: String, scenario: String? = null, options: List<String> = emptyList()): Int {
        val words = (listOfNotNull(scenario, prompt) + options).sumOf { it.words() }
        val reading = (words / WORDS_PER_SECOND).toInt()
        val weighing = (options.size - 2).coerceAtLeast(0) * PER_EXTRA_OPTION_SECONDS
        return (reading + DECIDE_SECONDS + weighing).coerceAtLeast(FLOOR_SECONDS)
    }

    private fun String.words(): Int = split(' ', '\n', '\t').count { it.isNotBlank() }

    /** Where [taken] falls against [expected]. Either being absent means [Verdict.UNKNOWN]. */
    fun verdict(taken: Int?, expected: Int?): Verdict {
        if (taken == null || expected == null || taken <= 0 || expected <= 0) return Verdict.UNKNOWN
        return when {
            taken * QUICK_FRACTION < expected -> Verdict.QUICK
            taken > expected * SLOW_MULTIPLE -> Verdict.SLOW
            else -> Verdict.STEADY
        }
    }
}
