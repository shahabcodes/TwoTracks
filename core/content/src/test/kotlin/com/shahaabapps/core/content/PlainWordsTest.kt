package com.shahaabapps.core.content

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The fresher test, as far as a test can hold it: in the AI-200 and AZ-305
 * episodes, a hard word is never left bare the first time a listener hears
 * it. Each episode stands alone, since a listener may hear only one telling.
 *
 * It cannot tell a good explanation from a poor one; tools/podcast/WRITING.md
 * says what a good one is ("the shared pictures"). It catches the bare
 * mention, and a line that stacks new words on top of each other.
 */
class PlainWordsTest {
    private val checked = setOf(PodcastSeries.AI200, PodcastSeries.AZ305, PodcastSeries.PSM1)
    private val episodes = Podcasts.all.filter { it.series in checked }

    /** Hard words, by the name the failure reports. */
    private val hard = linkedMapOf(
        "vector" to "vectors?", "embedding" to "embeddings?", "dimension" to "dimensions?",
        "euclidean" to "euclidean", "cosine" to "cosine", "dot product" to "dot products?", "inner product" to "inner products?",
        "node" to "nodes?", "graph" to "graphs?", "cluster" to "clusters?", "partition" to "partitions?",
        "index" to "index(es)?|indexes|indices", "replica" to "replicas?", "token" to "tokens?", "hash" to "hash(es)?",
        "quantized" to "quantiz\\w*", "latency" to "latency", "throughput" to "throughput", "schema" to "schemas?",
        "endpoint" to "endpoints?", "namespace" to "namespaces?", "idempotent" to "idempotent", "payload" to "payloads?",
        "SDK" to "SDKs?", "API" to "APIs?", "recall" to "recall", "HNSW" to "HNSW", "IVFFlat" to "IVFFlat", "diskANN" to "diskANN",
        "RAG" to "RAG", "RU" to "RUs?|request units?", "429" to "429", "KQL" to "KQL", "span" to "spans?", "telemetry" to "telemetry",
        "lease" to "leases?", "TTL" to "TTL", "webhook" to "webhooks?", "middleware" to "middleware", "sentinel" to "sentinel",
        "manifest" to "manifests?", "pod" to "pods?", "ingress" to "ingress", "revision" to "revisions?", "probe" to "probes?",
        "PgBouncer" to "PgBouncer", "pooling" to "pooling", "jsonb" to "jsonb", "B-tree" to "B-trees?", "GIN" to "GIN",
        "managed identity" to "managed identit(y|ies)", "RBAC" to "RBAC", "consistency" to "consistency", "ACR" to "ACR",
        "KEDA" to "KEDA", "AKS" to "AKS", "OpenTelemetry" to "OpenTelemetry", "Entra" to "Entra",
        "flat" to "flat", "approximate" to "approximate", "nearest neighbour" to "nearest neighbou?rs?",
    ).mapValues { (name, pattern) ->
        // Acronyms keep their case; words match in any case.
        val acronym = name.any { it.isUpperCase() } && name.none { it.isLowerCase() }
        Regex("""\b($pattern)\b""", if (acronym) emptySet() else setOf(RegexOption.IGNORE_CASE))
    }

    /** The words a line uses to explain something. Generous on purpose: the bar is an explanation nearby, not a perfect one. */
    private val explains = Regex(
        """\b(means?|meaning|is a|is an|is the|is just|are just|are the|is like|like a|like an|like the|think of|picture|imagine|in plain words|plain words|which is|that is|that's|it's a|it's an|it's the|it's just|called|stands for|for example|short for|a kind of|a sort of|a way to|works like|as if)\b""",
        RegexOption.IGNORE_CASE,
    )

    private fun text(l: PodcastLine) = PodcastCue.strip(l.text)

    @Test
    fun `there are episodes to hold to it`() {
        assertTrue(episodes.isNotEmpty())
    }

    @Test
    fun `a hard word is explained where a listener first hears it`() {
        val bare = mutableListOf<String>()
        episodes.forEach { p ->
            hard.forEach { (name, word) ->
                val first = p.lines.indexOfFirst { word.containsMatchIn(text(it)) }
                if (first < 0) return@forEach
                val nearby = listOfNotNull(p.lines[first], p.lines.getOrNull(first + 1)).joinToString(" ") { text(it) }
                // "A managed identity, a badge Azure gives your app": the word, then what it is, straight after.
                val apposition = Regex("""(${word.pattern})(\s+[\w-]+){0,2}\s*[,:]\s+(an?|the|one|its|your|our|that|this|\w+'s)\b""", word.options)
                if (!explains.containsMatchIn(nearby) && !apposition.containsMatchIn(text(p.lines[first]))) bare +="${p.episodeId} line $first: \"$name\" in \"${text(p.lines[first]).take(90)}\""
            }
        }
        assertTrue("${bare.size} hard words first heard with no explanation nearby:\n" + bare.joinToString("\n"), bare.isEmpty())
    }

    @Test
    fun `a line brings in at most two new hard words`() {
        val stacked = mutableListOf<String>()
        episodes.forEach { p ->
            val seen = mutableSetOf<String>()
            p.lines.forEachIndexed { i, l ->
                val fresh = hard.filter { (name, word) -> name !in seen && word.containsMatchIn(text(l)) }.keys
                if (fresh.size > 2) stacked += "${p.episodeId} line $i brings in ${fresh.joinToString()}"
                seen += fresh
            }
        }
        assertTrue("${stacked.size} lines stack new words:\n" + stacked.joinToString("\n"), stacked.isEmpty())
    }
}
