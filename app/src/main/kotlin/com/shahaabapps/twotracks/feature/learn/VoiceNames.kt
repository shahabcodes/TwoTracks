package com.shahaabapps.twotracks.feature.learn

/** Which voice it is, as far as anyone is willing to say. */
enum class VoiceGender { FEMALE, MALE, UNKNOWN }

/** What a voice sounds like, in one word, chosen by the person listening to it. */
enum class VoiceCharacter(val label: String) {
    WARM("warm"),
    BRIGHT("bright"),
    DEEP("deep"),
    CRISP("crisp"),
    SOFT("soft"),
    ;

    companion object {
        fun from(name: String?): VoiceCharacter? = entries.firstOrNull { it.name == name }
    }
}

/**
 * Reading what a text-to-speech voice is, and remembering what it turned out
 * to be.
 *
 * Android's [android.speech.tts.Voice] has a name, a locale, a quality, a
 * latency and a feature list, and **no gender**. There is no API that will
 * tell you. Some engines spell it out in the name — Samsung's and a few others
 * produce things like `en-gb-x-gba#female_1-local` — and where the name says,
 * [genderOf] reads it.
 *
 * Google's engine, which is what is actually on most phones, does not. Its
 * voices are named `en-us-x-iob-local`, `de-de-x-nfh-local`: a three-letter
 * code that identifies the voice and says nothing about it. No amount of
 * parsing turns that into "male" or "warm", and guessing it would be worse
 * than leaving it blank, because a voice labelled wrongly is chosen wrongly.
 *
 * So the one thing that can actually answer it is the ear of the person
 * choosing. A voice is heard the moment it is tapped; the label is theirs to
 * set once, and it is remembered against the engine's own voice name so it
 * survives a reinstall and holds across both languages that voice reads.
 */
object VoiceNames {

    // "male" sits inside "female", so the boundaries are the whole trick here.
    private val FEMALE = Regex("(?<![a-z])fe-?male(?![a-z])")
    private val MALE = Regex("(?<![a-z])male(?![a-z])")

    /** The feature an engine sets on a voice whose data is not on the phone. */
    const val NOT_INSTALLED = "notInstalled"

    fun genderOf(voiceName: String): VoiceGender {
        val n = voiceName.lowercase()
        return when {
            FEMALE.containsMatchIn(n) -> VoiceGender.FEMALE
            MALE.containsMatchIn(n) -> VoiceGender.MALE
            else -> VoiceGender.UNKNOWN
        }
    }

    /**
     * The part of the name that identifies the voice itself, with the delivery
     * route dropped.
     *
     * `en-us-x-iob-local` and `en-us-x-iob-network` are one voice fetched two
     * ways, and listing both is how the picker ended up twice as long as it
     * needed to be. This is what collapses them.
     */
    fun variantOf(voiceName: String): String {
        val n = voiceName.lowercase().removeSuffix("-local").removeSuffix("-network")
        return n.substringAfterLast("-x-", n)
    }

    /**
     * The country the voice speaks for, upper case, or empty when the name
     * does not carry one. English alone ships GB, US, AU, IN and NG voices and
     * they do not belong in one undivided list.
     */
    fun regionOf(voiceName: String): String =
        voiceName.split('-').getOrNull(1)?.takeIf { it.length == 2 }?.uppercase().orEmpty()

    /**
     * The legacy per-locale entry an engine lists beside its real voices
     * (`en-US-language`). It is the engine's default wearing a different hat,
     * and the picker already offers that as its first row.
     */
    fun isLegacyLocaleVoice(voiceName: String): Boolean = voiceName.endsWith("-language", ignoreCase = true)

    /**
     * What to call a voice nobody has labelled yet. Letters rather than
     * numbers, and taken from the voice's own code, so a voice keeps its name
     * when another is installed beside it — a numbered list renumbers itself
     * and the voice you chose last week is suddenly a different one.
     */
    fun placeholderLabel(voiceName: String): String {
        val v = variantOf(voiceName)
        val letter = v.lastOrNull { it.isLetter() }?.uppercaseChar() ?: 'A'
        return "Voice $letter"
    }

    /** How a voice reads in the list, given whatever is known about it. */
    fun label(gender: VoiceGender, character: VoiceCharacter?, voiceName: String): String {
        val who = when (gender) {
            VoiceGender.FEMALE -> "Female"
            VoiceGender.MALE -> "Male"
            VoiceGender.UNKNOWN -> null
        }
        return when {
            who != null && character != null -> "$who · ${character.label}"
            who != null -> who
            character != null -> "${placeholderLabel(voiceName)} · ${character.label}"
            else -> placeholderLabel(voiceName)
        }
    }

    /**
     * A short line about where the voice comes from. A network voice is often
     * the better one and is silent on a plane, which is worth knowing before
     * choosing it for a course you study on the train.
     */
    fun detail(networkOnly: Boolean, quality: Int): String {
        val where = if (networkOnly) "needs a connection" else "on the phone"
        // Voice.QUALITY_NORMAL is 300; anything above it is the engine saying
        // this one is better than ordinary.
        val good = if (quality > 300) "higher quality · " else ""
        return good + where
    }
}
