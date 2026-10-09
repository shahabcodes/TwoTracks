package com.shahaabapps.core.common.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.speechDataStore: DataStore<Preferences> by preferencesDataStore(name = "speech_prefs")

/**
 * Which voice to read each language in.
 *
 * Kept per language rather than once for the app: the voice that reads German
 * has nothing to do with the one that reads English, and a person may well
 * want a man for one and a woman for the other. The value is the engine's own
 * voice name, which is the only thing the system will accept back.
 *
 * A name that is no longer installed is simply not found when it is applied,
 * and the engine's default is used, so an uninstalled voice costs a different
 * voice rather than silence.
 */
@Singleton
class SpeechPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    /** The chosen voice for a language tag such as "de-DE", or null for the engine's default. */
    fun voiceName(languageTag: String): Flow<String?> = context.speechDataStore.data
        .map { prefs -> prefs[key(languageTag)]?.takeIf { it.isNotBlank() } }

    suspend fun setVoiceName(languageTag: String, voiceName: String?) {
        context.speechDataStore.edit { prefs ->
            if (voiceName == null) prefs.remove(key(languageTag)) else prefs[key(languageTag)] = voiceName
        }
    }

    /**
     * What a voice turned out to be, once somebody listened to it.
     *
     * Stored against the engine's own voice name rather than the language, so
     * a voice labelled while picking a German reader is still labelled when it
     * appears in the English list, and so the label survives the voice moving
     * position as others are installed.
     *
     * The value is "GENDER" or "GENDER|CHARACTER", both from the app's own
     * enums; anything unrecognised reads as unlabelled.
     */
    fun voiceLabels(): Flow<Map<String, String>> = context.speechDataStore.data
        .map { prefs ->
            prefs.asMap()
                .filterKeys { it.name.startsWith(LABEL_PREFIX) }
                .map { (k, v) -> k.name.removePrefix(LABEL_PREFIX) to v.toString() }
                .toMap()
        }

    suspend fun setVoiceLabel(voiceName: String, value: String?) {
        context.speechDataStore.edit { prefs ->
            val k = stringPreferencesKey(LABEL_PREFIX + voiceName)
            if (value == null) prefs.remove(k) else prefs[k] = value
        }
    }

    private fun key(languageTag: String) = stringPreferencesKey("voice_" + languageTag.lowercase())

    private companion object {
        const val LABEL_PREFIX = "label_"
    }
}
