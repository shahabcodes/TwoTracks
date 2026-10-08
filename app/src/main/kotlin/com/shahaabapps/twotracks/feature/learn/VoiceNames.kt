package com.shahaabapps.twotracks.feature.learn

/** Which voice it is, as far as the engine is willing to say. */
enum class VoiceGender { FEMALE, MALE, UNKNOWN }

/**
 * Reading what a text-to-speech voice is from its name.
 *
 * Android's [android.speech.tts.Voice] has a name, a locale, a quality and a
 * latency, and **no gender**. There is no API that will tell you. What there
 * is, is a naming convention: Google's engine names voices like
 * `en-gb-x-gba#female_1-local`, and several others spell it out somewhere in
 * the name too.
 *
 * So this reads the name where the name says, and answers [VoiceGender.UNKNOWN]
 * where it does not, rather than guessing from a three-letter code. A voice we
 * cannot label is still offered and still works; it is listed by number, and
 * the point of being able to hear one before choosing is that the label was
 * never the thing that mattered.
 */
object VoiceNames {

    // "male" sits inside "female", so the boundaries are the whole trick here.
    private val FEMALE = Regex("(?<![a-z])fe-?male(?![a-z])")
    private val MALE = Regex("(?<![a-z])male(?![a-z])")

    fun genderOf(voiceName: String): VoiceGender {
        val n = voiceName.lowercase()
        return when {
            FEMALE.containsMatchIn(n) -> VoiceGender.FEMALE
            MALE.containsMatchIn(n) -> VoiceGender.MALE
            else -> VoiceGender.UNKNOWN
        }
    }

    /**
     * What to call it in a list. [position] counts from 1 within its own
     * group, so two female voices read as "Female 1" and "Female 2" rather
     * than by the engine's name, which is unreadable.
     */
    fun label(gender: VoiceGender, position: Int): String = when (gender) {
        VoiceGender.FEMALE -> "Female $position"
        VoiceGender.MALE -> "Male $position"
        VoiceGender.UNKNOWN -> "Voice $position"
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
