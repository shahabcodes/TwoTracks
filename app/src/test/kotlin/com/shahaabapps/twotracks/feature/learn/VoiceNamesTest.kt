package com.shahaabapps.twotracks.feature.learn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Reading a voice's gender out of its name, which is the only place Android
 * puts it. The names here are the shapes real engines use.
 */
class VoiceNamesTest {

    @Test
    fun `google names say which they are`() {
        assertEquals(VoiceGender.FEMALE, VoiceNames.genderOf("en-gb-x-gba#female_1-local"))
        assertEquals(VoiceGender.FEMALE, VoiceNames.genderOf("de-de-x-deb#female_2-local"))
        assertEquals(VoiceGender.MALE, VoiceNames.genderOf("en-us-x-iom#male_2-local"))
        assertEquals(VoiceGender.MALE, VoiceNames.genderOf("de-de-x-deg#male_1-network"))
    }

    @Test
    fun `female is not read as male`() {
        // The whole reason this is a function and not a contains() call: the
        // word "male" sits inside the word "female", so a naive check labels
        // every female voice male.
        listOf(
            "en-gb-x-gba#female_1-local",
            "female",
            "FEMALE_2",
            "en-us-x-sfg#female_3-network",
            "fe-male-1",
        ).forEach {
            assertEquals("$it should be female", VoiceGender.FEMALE, VoiceNames.genderOf(it))
        }
    }

    @Test
    fun `a name that does not say is left unknown`() {
        // No guessing from a three-letter code. A voice we cannot label is
        // still offered, still works, and can still be listened to.
        listOf(
            "de-de-x-deb-local",
            "en-gb-x-gbb-local",
            "de-DE-default",
            "en-US-language",
            "cmn-cn-x-ccc-local",
            "",
        ).forEach {
            assertEquals("$it should be unknown", VoiceGender.UNKNOWN, VoiceNames.genderOf(it))
        }
    }

    @Test
    fun `the word has to stand on its own`() {
        // "normale" and "malediction" are not male voices.
        assertEquals(VoiceGender.UNKNOWN, VoiceNames.genderOf("de-de-x-normale-local"))
        assertEquals(VoiceGender.UNKNOWN, VoiceNames.genderOf("malediction"))
        // A digit straight after it is a boundary, which is how engines write it.
        assertEquals(VoiceGender.MALE, VoiceNames.genderOf("en-us-x-male2"))
    }

    @Test
    fun `labels are numbered within their own group`() {
        assertEquals("Female 1", VoiceNames.label(VoiceGender.FEMALE, 1))
        assertEquals("Female 2", VoiceNames.label(VoiceGender.FEMALE, 2))
        assertEquals("Male 1", VoiceNames.label(VoiceGender.MALE, 1))
        assertEquals("Voice 3", VoiceNames.label(VoiceGender.UNKNOWN, 3))
    }

    @Test
    fun `the detail line says where the voice lives and whether it is better`() {
        assertTrue(VoiceNames.detail(networkOnly = true, quality = 300).contains("needs a connection"))
        assertTrue(VoiceNames.detail(networkOnly = false, quality = 300).contains("on the phone"))
        // Voice.QUALITY_NORMAL is 300, so above it is the engine volunteering
        // that this one is better than ordinary.
        assertTrue(VoiceNames.detail(networkOnly = false, quality = 400).contains("higher quality"))
        assertTrue(!VoiceNames.detail(networkOnly = false, quality = 300).contains("higher quality"))
        assertTrue(!VoiceNames.detail(networkOnly = false, quality = 200).contains("higher quality"))
    }
}
