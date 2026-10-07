package com.shahaabapps.core.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SpeechMatchTest {

    @Test
    fun `saying the sentence back scores every word`() {
        val r = SpeechMatch.of("Ich bin krank und kann heute nicht kommen.", "ich bin krank und kann heute nicht kommen")
        assertEquals(100, r.percent)
        assertTrue(r.good)
        assertTrue(r.words.all { it.matched })
    }

    @Test
    fun `punctuation and capitals never decide the answer`() {
        val r = SpeechMatch.of("Was machst du beruflich?", "Was machst du beruflich")
        assertEquals(100, r.percent)
        val s = SpeechMatch.of("Entschuldigung, wo ist der Bahnhof?", "entschuldigung wo ist der bahnhof")
        assertEquals(100, s.percent)
    }

    @Test
    fun `sharp s and ss are the same word`() {
        assertEquals(100, SpeechMatch.of("Die Straße ist groß.", "die strasse ist gross").percent)
    }

    @Test
    fun `a dropped word costs only that word`() {
        val r = SpeechMatch.of("Wie hoch ist die Miete?", "wie hoch ist Miete")
        assertEquals(80, r.percent)
        assertTrue("four of five is still good enough", r.good)
        assertEquals(listOf("die"), r.words.filterNot { it.matched }.map { it.word })
    }

    @Test
    fun `a different sentence scores nothing and is not good`() {
        // The bug this came from: one card's transcript shown against another's target.
        val r = SpeechMatch.of("Ich bin krank und kann heute nicht kommen.", "was machst du beruflich")
        assertEquals(0, r.percent)
        assertFalse(r.good)
        assertTrue(r.words.none { it.matched })
    }

    @Test
    fun `extra words around the sentence do not cost anything`() {
        val r = SpeechMatch.of("Wie hoch ist die Miete?", "also wie hoch ist die Miete bitte")
        assertEquals(100, r.percent)
    }

    @Test
    fun `a word repeated in the target counts wherever it appears once`() {
        val r = SpeechMatch.of("Ja ja, das ist das Haus.", "ja das ist das haus")
        assertEquals(100, r.percent)
    }

    @Test
    fun `nothing heard scores nothing rather than dividing by zero`() {
        val r = SpeechMatch.of("Wie hoch ist die Miete?", "")
        assertEquals(0, r.percent)
        assertFalse(r.good)
        assertEquals(5, r.words.size)
    }

    @Test
    fun `an empty target is not good, and does not divide by zero`() {
        val r = SpeechMatch.of("", "anything at all")
        assertEquals(0, r.percent)
        assertFalse(r.good)
        assertTrue(r.words.isEmpty())
    }

    @Test
    fun `every shipped speaking exercise scores full marks against its own target`() {
        // Guards the normalisation against content: a target the marker mangles
        // could never be matched, however well it was spoken.
        Courses.all.flatMap { it.allTopics }
            .flatMap { it.blocks }
            .filterIsInstance<Block.Exercise>()
            .map { it.exercise }
            .filterIsInstance<Ex.Speak>()
            .forEach { ex ->
                val r = SpeechMatch.of(ex.target, ex.target)
                assertEquals("${ex.id} should match itself", 100, r.percent)
            }
    }
}
