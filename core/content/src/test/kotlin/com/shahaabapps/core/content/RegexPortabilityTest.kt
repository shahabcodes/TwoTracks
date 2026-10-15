package com.shahaabapps.core.content

import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

/**
 * Every pattern the app compiles, held to what Android's regex engine accepts.
 *
 * The JVM takes a bare `{` or `}` as a literal brace, and Android's ICU engine
 * refuses it, so a pattern can pass every unit test here and throw the first
 * time the phone compiles it. The podcast player's timing parser ended in a
 * bare `}` and crashed on first play while all its tests passed. This reads the
 * sources rather than the compiled patterns because the JVM is the wrong judge.
 */
class RegexPortabilityTest {
    private val root = generateSequence(File("").absoluteFile) { it.parentFile }.first { File(it, "settings.gradle.kts").isFile }

    /** A brace that is part of a `{n}`, `{n,}` or `{n,m}` quantifier. */
    private val quantifier = Regex("""\{\d+(,\d*)?\}""")

    @Test
    fun `no pattern in the app has a brace Android would refuse`() {
        val sources = listOf("app/src/main", "core").map { File(root, it) }
            .flatMap { dir -> dir.walkTopDown().filter { it.extension == "kt" && "/src/main/" in it.invariantSeparatorsPath }.toList() }
        val literal = Regex("Regex\\(\"\"\"(.*?)\"\"\"")
        val bad = sources.flatMap { file ->
            literal.findAll(file.readText()).mapNotNull { m ->
                val pattern = m.groupValues[1]
                val bare = quantifier.replace(pattern, "").let { p -> Regex("""(?<!\\)[{}]""").containsMatchIn(p.replace("\\\\", "")) }
                if (bare) "${file.relativeTo(root).invariantSeparatorsPath}: $pattern" else null
            }.toList()
        }
        assertTrue("escape these braces as \\{ and \\}:\n${bad.joinToString("\n")}", bad.isEmpty())
    }
}
