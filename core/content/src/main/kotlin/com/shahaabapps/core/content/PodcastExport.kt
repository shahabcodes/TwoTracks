package com.shahaabapps.core.content

import java.io.File

/**
 * The episodes as JSON, for the render tool in tools/podcast, which reads
 * the scripts from here rather than keeping copies of its own. Written by
 * hand because it is this small and the module has no JSON library.
 */
fun podcastsJson(podcasts: List<Podcast> = Podcasts.all): String = buildString {
    append("[\n")
    podcasts.forEachIndexed { i, p ->
        // The render tool names its files by "topicId": the episode's id, which is the topic's own for a classic episode.
        append("  {\"topicId\": ").append(quote(p.episodeId)).append(", \"topic\": ").append(quote(p.topicId)).append(", \"lines\": [\n")
        p.lines.forEachIndexed { j, l ->
            append("    {\"host\": ").append(quote(l.host.name.lowercase())).append(", \"text\": ").append(quote(l.text)).append("}")
            append(if (j < p.lines.lastIndex) ",\n" else "\n")
        }
        append("  ]}").append(if (i < podcasts.lastIndex) ",\n" else "\n")
    }
    append("]\n")
}

private fun quote(s: String): String = buildString {
    append('"')
    s.forEach { c ->
        when (c) {
            '"' -> append("\\\"")
            '\\' -> append("\\\\")
            '\n' -> append("\\n")
            else -> if (c < ' ') append("\\u%04x".format(c.code)) else append(c)
        }
    }
    append('"')
}

/** `./gradlew :core:content:exportPodcasts` writes tools/podcast/podcasts.json. */
fun main(args: Array<String>) {
    val out = File(args.firstOrNull() ?: "podcasts.json")
    out.parentFile?.mkdirs()
    out.writeText(podcastsJson())
    println("Wrote ${Podcasts.all.size} episodes to ${out.absolutePath}")
}
