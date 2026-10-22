package com.shahaabapps.core.content

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The words for which way things move, said the right way round in every
 * episode. A listener heard "pull" where "push" was meant; the renders were
 * checked against the scripts, but nothing checked the scripts themselves.
 * These are the reversals that can be caught by their words alone: an image
 * is pushed into a registry and pulled out of it, Event Grid pushes to its
 * handlers, and Service Bus is read by its receivers rather than calling them.
 */
class DirectionTest {
    private val wrong = listOf(
        Regex("""\bpull(s|ed|ing)?\b[^.?!]{0,40}\b(in)?to (the |your |a |an |our )?(private )?(registry|acr|container registry)\b""") to "an image is pushed into a registry, not pulled",
        Regex("""\bpush(es|ed|ing)?\b[^.?!]{0,40}\bfrom (the |your |a |an |our )?(private )?(registry|acr|container registry)\b""") to "an image is pulled from a registry, not pushed",
        Regex("""\bevent grid (polls|pulls)\b""") to "Event Grid pushes to its handlers",
        Regex("""\bservice bus (pushes|calls your|posts to)\b""") to "Service Bus receivers pull; it never calls your code",
    )

    @Test
    fun `push and pull are the right way round in every episode`() {
        val found = Podcasts.all.flatMap { p ->
            p.lines.mapIndexedNotNull { i, line ->
                val text = PodcastCue.strip(line.text).lowercase()
                wrong.firstOrNull { (rule, _) -> rule.containsMatchIn(text) }?.let { (_, why) -> "${p.episodeId} line $i ($why): ${line.text}" }
            }
        }
        assertTrue(found.joinToString("\n"), found.isEmpty())
    }

    @Test
    fun `the rules catch the reversals they are for`() {
        val samples = listOf(
            "Then you pull the image into the registry.",
            "App Service pushes the image from ACR.",
            "Event Grid polls the queue.",
            "Service Bus pushes each message to your worker.",
        )
        samples.forEach { s -> assertTrue(s, wrong.any { (rule, _) -> rule.containsMatchIn(s.lowercase()) }) }
        val right = listOf(
            "Your pipeline pushes the image into the registry.",
            "App Service pulls the image from ACR.",
            "Event Grid pushes, and nobody polls.",
        )
        right.forEach { s -> assertTrue(s, wrong.none { (rule, _) -> rule.containsMatchIn(s.lowercase()) }) }
    }
}
