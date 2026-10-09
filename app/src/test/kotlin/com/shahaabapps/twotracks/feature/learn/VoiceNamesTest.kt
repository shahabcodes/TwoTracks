package com.shahaabapps.twotracks.feature.learn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Reading a text-to-speech voice's name: the gender where an engine states
 * it, and otherwise what can honestly be told from the string.
 *
 * The names here are the shapes real engines use, and the long list at the end
 * is verbatim what Google's engine reported on an Android 16 device — because
 * that list is the whole reason the picker needed fixing.
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
    fun `the detail line says where the voice lives and whether it is better`() {
        assertTrue(VoiceNames.detail(networkOnly = true, quality = 300).contains("needs a connection"))
        assertTrue(VoiceNames.detail(networkOnly = false, quality = 300).contains("on the phone"))
        // Voice.QUALITY_NORMAL is 300, so above it is the engine volunteering
        // that this one is better than ordinary.
        assertTrue(VoiceNames.detail(networkOnly = false, quality = 400).contains("higher quality"))
        assertTrue(!VoiceNames.detail(networkOnly = false, quality = 300).contains("higher quality"))
        assertTrue(!VoiceNames.detail(networkOnly = false, quality = 200).contains("higher quality"))
    }

    @Test
    fun `a voice fetched two ways is one voice`() {
        // This pairing is what doubled the list.
        assertEquals(
            VoiceNames.variantOf("en-us-x-iob-local"),
            VoiceNames.variantOf("en-us-x-iob-network"),
        )
        assertEquals("iob", VoiceNames.variantOf("en-us-x-iob-local"))
        assertEquals("nfh", VoiceNames.variantOf("de-de-x-nfh-network"))
    }

    @Test
    fun `two countries' voices are not the same voice`() {
        // Both end in "a", and collapsing on the variant alone would merge them.
        assertEquals("GB", VoiceNames.regionOf("en-gb-x-gba-local"))
        assertEquals("US", VoiceNames.regionOf("en-us-x-iob-local"))
        assertEquals("DE", VoiceNames.regionOf("de-de-x-dea-local"))
    }

    @Test
    fun `the engine's own default is not listed twice`() {
        assertTrue(VoiceNames.isLegacyLocaleVoice("en-US-language"))
        assertTrue(VoiceNames.isLegacyLocaleVoice("de-DE-language"))
        assertFalse(VoiceNames.isLegacyLocaleVoice("en-us-x-iob-local"))
    }

    @Test
    fun `an unlabelled voice keeps its name when others are installed`() {
        // A numbered list renumbers itself, so the voice chosen last week
        // becomes a different one the moment a voice is added above it. The
        // placeholder is taken from the voice's own code instead.
        val before = VoiceNames.placeholderLabel("en-us-x-iom-local")
        val after = VoiceNames.placeholderLabel("en-us-x-iom-network")
        assertEquals(before, after)
        assertEquals("Voice M", before)
    }

    @Test
    fun `a label says what was heard`() {
        assertEquals("Female · warm", VoiceNames.label(VoiceGender.FEMALE, VoiceCharacter.WARM, "en-us-x-iob-local"))
        assertEquals("Male", VoiceNames.label(VoiceGender.MALE, null, "en-us-x-iob-local"))
        assertEquals("Voice B", VoiceNames.label(VoiceGender.UNKNOWN, null, "en-us-x-iob-local"))
        // A character without a gender still beats a bare placeholder.
        assertEquals("Voice B · crisp", VoiceNames.label(VoiceGender.UNKNOWN, VoiceCharacter.CRISP, "en-us-x-iob-local"))
    }

    @Test
    fun `the real list collapses to something a person can read`() {
        // Every English voice the device actually reported, verbatim.
        val reported = listOf(
            "en-AU-language", "en-GB-language", "en-IN-language", "en-NG-language", "en-US-language",
            "en-au-x-aua-local", "en-au-x-aub-local", "en-au-x-auc-local", "en-au-x-aud-local",
            "en-gb-x-gba-local", "en-gb-x-gbb-local", "en-gb-x-gbc-local", "en-gb-x-gbd-local",
            "en-gb-x-gbg-local", "en-gb-x-rjs-local",
            "en-in-x-ena-local", "en-in-x-enc-local", "en-in-x-end-local", "en-in-x-ene-local",
            "en-ng-x-tfn-local",
            "en-us-x-iob-local", "en-us-x-iog-local", "en-us-x-iol-local", "en-us-x-iom-local",
            "en-us-x-sfg-local", "en-us-x-tpc-local", "en-us-x-tpd-local", "en-us-x-tpf-local",
        ) + listOf(
            "en-au-x-aua-network", "en-gb-x-gba-network", "en-us-x-iob-network", "en-us-x-iom-network",
        )
        // The ones the device marked notInstalled: every AU, IN and NG voice.
        val notInstalled = reported.filter { it.contains("-au-") || it.contains("-in-") || it.contains("-ng-") || it.startsWith("en-AU") || it.startsWith("en-IN") || it.startsWith("en-NG") }.toSet()

        val listed = reported
            .filterNot { it in notInstalled }
            .filterNot { VoiceNames.isLegacyLocaleVoice(it) }
            .distinctBy { VoiceNames.regionOf(it) + "/" + VoiceNames.variantOf(it) }

        // Six British and eight American, down from thirty-two rows.
        assertEquals(14, listed.size)
        assertEquals(6, listed.count { VoiceNames.regionOf(it) == "GB" })
        assertEquals(8, listed.count { VoiceNames.regionOf(it) == "US" })
        assertTrue(listed.none { VoiceNames.isLegacyLocaleVoice(it) })
    }
}
