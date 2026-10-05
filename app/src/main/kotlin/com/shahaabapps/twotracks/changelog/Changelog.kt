package com.shahaabapps.twotracks.changelog

import android.content.Context
import com.shahaabapps.twotracks.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** One version's entry from CHANGELOG.md. */
data class Release(
    val version: String,
    val date: String,
    /** Section heading ("Added", "Changed", "Fixed") to its bullet points. */
    val sections: List<Pair<String, List<String>>>,
) {
    val bullets: List<String> get() = sections.flatMap { it.second }
}

/**
 * Parses the bundled CHANGELOG.md (copied into assets at build time) and
 * remembers which version's "What's new" the user has already seen.
 */
@Singleton
class ChangelogStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val prefs = context.getSharedPreferences("changelog", Context.MODE_PRIVATE)

    val releases: List<Release> by lazy { runCatching { parse(context.assets.open("CHANGELOG.md").bufferedReader().readText()) }.getOrDefault(emptyList()) }

    val currentVersion: String = BuildConfig.VERSION_NAME

    /** The entry for the installed version, or the newest one listed. */
    val latest: Release? get() = releases.firstOrNull { it.version == currentVersion } ?: releases.firstOrNull()

    private val _unseen = MutableStateFlow(computeUnseen())
    /** The release to announce, or null once seen (or on a fresh install). */
    val unseen: StateFlow<Release?> = _unseen

    private fun computeUnseen(): Release? {
        val seen = prefs.getString(KEY_SEEN, null)
        if (seen == null) {
            // First launch ever on this device: nothing is "new" yet, just record the version.
            prefs.edit().putString(KEY_SEEN, currentVersion).apply()
            return null
        }
        return if (seen != currentVersion) latest else null
    }

    fun markSeen() {
        prefs.edit().putString(KEY_SEEN, currentVersion).apply()
        _unseen.value = null
    }

    companion object {
        private const val KEY_SEEN = "seen_version"

        private val heading = Regex("""^## \[([^\]]+)\](?: - (.*))?$""")

        fun parse(markdown: String): List<Release> {
            val releases = mutableListOf<Release>()
            var version: String? = null
            var date = ""
            var sections = mutableListOf<Pair<String, MutableList<String>>>()
            fun flush() {
                val v = version ?: return
                if (v.equals("Unreleased", ignoreCase = true) && sections.all { it.second.isEmpty() }) return
                releases += Release(v, date, sections.filter { it.second.isNotEmpty() }.map { it.first to it.second.toList() })
            }
            markdown.lines().forEach { raw ->
                val line = raw.trimEnd()
                heading.find(line)?.let { m ->
                    flush()
                    version = m.groupValues[1]
                    date = m.groupValues.getOrNull(2).orEmpty()
                    sections = mutableListOf()
                    return@forEach
                }
                if (version == null) return@forEach
                when {
                    line.startsWith("### ") -> sections += line.removePrefix("### ").trim() to mutableListOf()
                    line.startsWith("- ") -> {
                        if (sections.isEmpty()) sections += "Changes" to mutableListOf()
                        sections.last().second += line.removePrefix("- ").trim()
                    }
                    line.startsWith("  ") && sections.isNotEmpty() && sections.last().second.isNotEmpty() -> {
                        val list = sections.last().second
                        list[list.lastIndex] = list.last() + " " + line.trim()
                    }
                }
            }
            flush()
            return releases.filterNot { it.version.equals("Unreleased", ignoreCase = true) }
        }
    }
}
