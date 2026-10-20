package com.shahaabapps.core.content

/**
 * The AI-200 episodes' boards, keyed by episode id; they live beside their
 * scripts, one function each (the JVM's 64 KB limit on a method).
 */
internal object Ai200Boards {
    val all: List<PodcastBoard> by lazy { listOf() }
}
