package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Is the German the app prints as correct actually correct?
 *
 * `einen Kilo Äpfel` shipped inside a model answer. Kilo is das, so the
 * accusative masculine `einen` is impossible, and nothing noticed: a model
 * answer is prose, and the only rules about it were that it exists, that it
 * passes its own keyword marker and that it is long enough. The German itself
 * was never read by anything.
 *
 * The deck already knows what is needed. Every vocabulary card carries its
 * article and most carry a plural, which is a gender table for 420 nouns,
 * written and reviewed as content rather than as a fixture. This walks every
 * sentence the app presents as correct, finds each determiner sitting in front
 * of a noun the deck knows, and asks whether that pairing is possible in any
 * case.
 *
 * What it deliberately does not read: the wrong option of a check, a
 * `mistakes` block, and the prose around them. The course prints bad German on
 * purpose in those places — `kein Zeit` and `ihr Schwester` are there to be
 * recognised as wrong — and a rule that cannot tell teaching from erring would
 * have to be switched off in exactly the places it is most tempting to cheat.
 *
 * It is a narrow rule. It says nothing about adjective endings, verb forms or
 * word order, and it only judges a determiner immediately in front of a noun,
 * so `ein kleines Kind` is skipped. That still comes to about a thousand
 * pairings, and it is the one half of German grammar the content already
 * carries the data to check.
 *
 * The second rule here is about register, because the course says in its own
 * words that mixing the halves of a greeting is the commonest avoidable error
 * in Schreiben part 2. A model that did it would teach the error.
 */
class GermanGrammarTest {

    /** m, f and n are the singular genders; p is the plural. */
    private data class Deck(
        val singular: Map<String, Set<Char>>,
        val plural: Set<String>,
        /** den/dem/des Menschen: a masculine that declines in the singular too. */
        val weak: Set<String>,
    ) {
        fun genders(noun: String): Set<Char> {
            val out = mutableSetOf<Char>()
            singular[noun]?.let { out += it }
            if (noun in plural) out += 'p'
            if (noun in weak) out += 'm'
            return out
        }
    }

    private fun deckOf(words: List<Word>): Deck {
        val singular = mutableMapOf<String, MutableSet<Char>>()
        val plural = mutableSetOf<String>()
        val weak = mutableSetOf<String>()
        words.forEach { w ->
            val gender = when (w.article) {
                "der" -> 'm'
                "die" -> 'f'
                "das" -> 'n'
                else -> return@forEach
            }
            if (' ' in w.de) return@forEach
            singular.getOrPut(w.de) { mutableSetOf() } += gender
            val pl = w.plural
            if (!pl.isNullOrBlank()) {
                plural += pl
                // A masculine whose plural is the stem plus -n or -en takes the
                // same form through the singular: der Kunde, den Kunden.
                if (gender == 'm' && (pl == w.de + "n" || pl == w.de + "en")) weak += pl
            }
        }
        return Deck(singular, plural, weak)
    }

    private val deck = deckOf(Courses.words(GermanA1.course))

    // What each determiner can stand in front of, across every case.
    private val definite = mapOf(
        "der" to setOf('m', 'f', 'p'),   // masculine nominative, feminine dative and genitive, plural genitive
        "die" to setOf('f', 'p'),
        "das" to setOf('n'),
        "den" to setOf('m', 'p'),        // masculine accusative, plural dative
        "dem" to setOf('m', 'n'),
        "des" to setOf('m', 'n'),
    )

    // ein, kein and the possessives all take one set of endings.
    private val einEndings = mapOf(
        "" to setOf('m', 'n'),
        "e" to setOf('f', 'p'),
        "en" to setOf('m', 'p'),
        "em" to setOf('m', 'n'),
        "er" to setOf('f', 'p'),
        "es" to setOf('m', 'n'),
    )
    private val einStems = listOf("ein", "kein", "mein", "dein", "sein", "ihr", "unser", "euer")

    // Preposition plus article, where the article is not in doubt.
    private val contracted = mapOf(
        "im" to setOf('m', 'n'), "am" to setOf('m', 'n'), "vom" to setOf('m', 'n'),
        "beim" to setOf('m', 'n'), "zum" to setOf('m', 'n'), "zur" to setOf('f'),
        "ins" to setOf('n'), "ans" to setOf('n'), "aufs" to setOf('n'),
        "fürs" to setOf('n'), "durchs" to setOf('n'), "übers" to setOf('n'),
    )

    private fun determiner(token: String): Set<Char>? {
        val low = token.lowercase()
        definite[low]?.let { return it }
        contracted[low]?.let { return it }
        // Bare ihr is also the dative pronoun, as in "ich gebe ihr Blumen", so
        // it is allowed a plural after it as well. ihr Bruder and ihr
        // Schwester are still told apart, which is the pair worth catching.
        if (low == "ihr") return setOf('m', 'n', 'p')
        einStems.forEach { stem ->
            if (low.startsWith(stem)) einEndings[low.removePrefix(stem)]?.let { return it }
        }
        return null
    }

    private val word = Regex("[A-Za-zÄÖÜäöüß]+")

    /** Every determiner-noun pairing in [text] that no case allows. */
    private fun disagreements(text: String): List<String> {
        val out = mutableListOf<String>()
        // A comma or full stop ends the phrase, so nothing is paired across one.
        text.split('.', ',', ';', ':', '!', '?', '(', ')', '„', '“', '"').forEach { phrase ->
            val tokens = word.findAll(phrase).map { it.value }.toList()
            tokens.forEachIndexed { i, token ->
                val allowed = determiner(token) ?: return@forEachIndexed
                val noun = tokens.getOrNull(i + 1) ?: return@forEachIndexed
                if (!noun.first().isUpperCase()) return@forEachIndexed
                val possible = deck.genders(noun)
                if (possible.isEmpty()) return@forEachIndexed
                pairsSeen++
                if ((allowed intersect possible).isEmpty()) out += "$token $noun"
            }
        }
        return out
    }

    private var pairsSeen = 0

    @Test
    fun `it can tell a wrong pairing from a right one`() {
        // The one that shipped, and the correction.
        assertEquals(listOf("einen Kilo"), disagreements("Ich kaufe einen Kilo Äpfel."))
        assertEquals(emptyList<String>(), disagreements("Ich kaufe ein Kilo Äpfel."))
        // Each gender, right and wrong.
        assertEquals(listOf("kein Zeit"), disagreements("Ich habe kein Zeit."))
        assertEquals(emptyList<String>(), disagreements("Ich habe keine Zeit."))
        assertEquals(listOf("das Apfel"), disagreements("Ich nehme das Apfel."))
        assertEquals(emptyList<String>(), disagreements("Ich nehme den Apfel."))
        assertEquals(listOf("ihr Schwester"), disagreements("Das ist ihr Schwester."))
        assertEquals(emptyList<String>(), disagreements("Das ist ihre Schwester."))
        // A feminine in the dative takes der, which is also the masculine
        // nominative: both have to pass.
        assertEquals(emptyList<String>(), disagreements("Ich fahre mit der Bahn."))
        assertEquals(emptyList<String>(), disagreements("Der Bahnhof ist dort."))
        // A plural whose form matches its own singular.
        assertEquals(emptyList<String>(), disagreements("Die Fenster sind offen."))
        assertEquals(emptyList<String>(), disagreements("Das Fenster ist offen."))
        // A weak masculine declines in the singular as well as the plural.
        assertEquals(emptyList<String>(), disagreements("Fahrkarten bekommen Sie am Automaten."))
        // A contraction carries its article with it.
        assertEquals(emptyList<String>(), disagreements("Die Milch ist im Kühlschrank."))
        assertEquals(listOf("zur Bahnhof"), disagreements("Ich gehe zur Bahnhof."))
        // Nothing is paired across a comma, and an unknown noun is left alone.
        assertEquals(emptyList<String>(), disagreements("Das ist der Mann, den Katzenjammer kennt."))
        // ihr is a possessive and a dative pronoun, and both readings pass.
        assertEquals(emptyList<String>(), disagreements("Ich gebe ihr Blumen."))
    }

    @Test
    fun `a German letter model opens and closes in one register`() {
        // Sehr geehrte … pairs with Mit freundlichen Grüßen; a first name pairs
        // with Viele Grüße. The course calls mixing them the commonest
        // avoidable error in the task, so a model may not make it.
        val formalOpen = listOf("Sehr geehrte")
        val informalOpen = listOf("Liebe ", "Lieber ", "Hallo ")
        val formalClose = listOf("freundlichen Grüßen", "Freundliche Grüße")
        val informalClose = listOf(
            "Viele Grüße", "Liebe Grüße", "Herzliche Grüße", "Bis bald",
            "Bis später", "Alles Gute", "Gute Besserung", "Dein ", "Deine ",
        )

        val letters = GermanA1.course.allTopics
            .flatMap { t -> t.blocks.filterIsInstance<Block.Exercise>().map { it.exercise } }
            .filterIsInstance<Ex.Writing>()
            .filter { e -> (formalOpen + informalOpen).any { it in e.model } }
        assertTrue("expected German letter models, saw ${letters.size}", letters.size >= 9)

        letters.forEach { e ->
            val formal = formalOpen.any { it in e.model }
            val opened = if (formal) "formally" else "informally"
            val closes = if (formal) formalClose else informalClose
            assertTrue(
                "de/${e.id} opens $opened and closes the other way: ${e.model.trim()}",
                closes.any { it in e.model },
            )
        }
    }

    @Test
    fun `the deck is a gender table worth trusting`() {
        assertTrue("expected 350 or more nouns with a gender, saw ${deck.singular.size}", deck.singular.size >= 350)
        assertTrue("expected plural forms, saw ${deck.plural.size}", deck.plural.size >= 250)
    }

    @Test
    fun `every German sentence the app presents as correct agrees`() {
        val bad = mutableListOf<String>()
        fun scan(where: String, text: String) {
            disagreements(text).forEach { bad += "$where: $it  in  \"${text.take(90)}\"" }
        }

        Courses.words(GermanA1.course).forEach { scan("word/${it.id}", it.example) }
        GermanA1.course.allTopics.forEach { t ->
            t.blocks.forEach { b ->
                when (b) {
                    is Block.Dialogue -> b.lines.forEach { scan("dialogue/${t.id}", it.de) }
                    // The option the reader is being told is right. The wrong
                    // ones are wrong on purpose and are not read here.
                    is Block.Check -> scan("check/${b.question.id}", b.question.correct.joinToString(" ") { i -> b.question.options[i] })
                    is Block.Table -> b.rows.forEach { row -> row.forEach { cell -> scan("table/${t.id}", cell) } }
                    is Block.Faded -> b.steps.forEach { scan("faded/${b.id}", it) }
                    is Block.Exercise -> when (val e = b.exercise) {
                        is Ex.Speak -> scan("speak/${e.id}", e.target)
                        is Ex.Listen -> scan("listen/${e.id}", e.audio)
                        is Ex.Writing -> scan("model/${e.id}", e.model)
                        is Ex.Reorder -> scan("reorder/${e.id}", e.answer)
                        // The hole filled with its own answer is the sentence
                        // the exercise is teaching.
                        is Ex.Gap -> scan("gap/${e.id}", e.sentence.replace("___", e.answers.first()))
                        else -> Unit
                    }
                    else -> Unit
                }
            }
        }

        // Both the module tests and the paper sittings, correct option only.
        (GermanA1.course.modules.mapNotNull { it.quiz } + listOfNotNull(GermanA1.course.mockExam))
            .flatMap { it.questions }
            .forEach { q -> scan("question/${q.id}", q.correct.joinToString(" ") { i -> q.options[i] }) }

        assertTrue("fewer pairings than expected, saw $pairsSeen", pairsSeen >= 900)
        assertTrue("German that does not agree:\n" + bad.joinToString("\n"), bad.isEmpty())
    }
}
