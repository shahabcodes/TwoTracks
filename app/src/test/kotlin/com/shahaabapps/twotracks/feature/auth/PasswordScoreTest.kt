package com.shahaabapps.twotracks.feature.auth

import org.junit.Assert.assertEquals
import org.junit.Test

/** The four-segment meter under the sign-up password. */
class PasswordScoreTest {

    @Test
    fun `nothing typed fills nothing`() = assertEquals(0, passwordScore(""))

    @Test
    fun `under six characters is weak whatever it contains`() {
        assertEquals(1, passwordScore("Ab1!"))
        assertEquals(1, passwordScore("abcde"))
    }

    @Test
    fun `length, mixed case and a non-letter each add one`() {
        assertEquals(1, passwordScore("abcdef"))
        assertEquals(2, passwordScore("abcdefghij"))
        assertEquals(3, passwordScore("Abcdefghij"))
        assertEquals(4, passwordScore("Abcdefghi1"))
    }

    @Test
    fun `the score never passes four`() = assertEquals(4, passwordScore("AbcdefghijKLMNOP123!!"))
}
