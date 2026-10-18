package com.shahaabapps.core.content

/**
 * The boards for the German A1 episodes. A German phrase goes up as a card,
 * the German as its title and the English under it, at the line where Jonas
 * says it; the diagrams show the choice behind the words (du or Sie, arriving
 * or leaving). Line numbers are each episode's own, from 0 (GermanPodcasts).
 */
internal object GermanBoards {
    private const val ASPECT = 0.8f

    private fun de1Words() = PodcastBoard(
        "de1-words",
        listOf(
            // 0: "Hi, and welcome to the first German episode..."
            Scene(
                0, "Three voices",
                DiagramSpec(
                    "Maya explains, Leo asks, Jonas says the German, and you say it back in the gap.",
                    nodes = listOf(
                        DNode("maya", "Maya", 0.18f, 0.2f, w = 0.3f, h = 0.16f, kind = NodeKind.PILL, tone = 1, sub = "explains"),
                        DNode("leo", "Leo", 0.18f, 0.5f, w = 0.3f, h = 0.16f, kind = NodeKind.PILL, tone = 1, sub = "asks"),
                        DNode("jonas", "Jonas", 0.5f, 0.82f, w = 0.3f, h = 0.16f, kind = NodeKind.PILL, sub = "says the German"),
                        DNode("you", "You", 0.84f, 0.42f, w = 0.22f, h = 0.2f, kind = NodeKind.USER),
                    ),
                    edges = listOf(DEdge("jonas", "you", "repeat")),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(0, show = setOf("maya", "leo", "you")),
                    Beat(1, show = setOf("jonas"), light = setOf("jonas"), card = BoardCard("Hallo! Ich bin Jonas.", "Hello! I'm Jonas.", phrase = true)),
                    Beat(2, light = setOf("jonas", "you"), pulse = "jonas" to "you", card = BoardCard("In the gap", "Say it out loud. Quietly on the bus is fine.")),
                ),
            ),
            // 3: "Let's start with the polite hello..."
            Scene(
                3, "Formal or informal",
                DiagramSpec(
                    "Formal or informal: the choice decides your greeting, your goodbye and later your verb forms.",
                    nodes = listOf(
                        DNode("you", "You", 0.5f, 0.14f, w = 0.2f, h = 0.16f, kind = NodeKind.USER),
                        DNode("du", "du (informell)", 0.25f, 0.55f, w = 0.38f, h = 0.2f, tone = 1, sub = "friends, family"),
                        DNode("sie", "Sie (formell)", 0.75f, 0.55f, w = 0.38f, h = 0.2f, sub = "strangers, shops, work"),
                        DNode("n1", "Hallo", 0.25f, 0.88f, w = 0.3f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("n2", "Guten Tag", 0.75f, 0.88f, w = 0.3f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(DEdge("you", "du"), DEdge("you", "sie"), DEdge("du", "n1", dashed = true), DEdge("sie", "n2", dashed = true)),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(3, show = setOf("you")),
                    Beat(4, show = setOf("sie", "n2"), light = setOf("n2"), card = BoardCard("Guten Tag.", "Good day: the polite hello.", phrase = true)),
                    Beat(6, light = setOf("sie", "n2"), pulse = "you" to "sie", card = BoardCard("Formell: Sie", "A bank, a shop, the immigration office, a new colleague.")),
                    Beat(7, show = setOf("du", "n1"), light = setOf("n1"), card = BoardCard("Hallo!", "Hello.", phrase = true)),
                    Beat(9, light = setOf("du", "n1"), pulse = "you" to "du", card = BoardCard("Informell: du", "Friends, family, children, your partner.")),
                    Beat(10, light = setOf("du"), card = BoardCard("Hallo Sanya, wie geht's?", "Hi Sanya, how are you?", phrase = true)),
                    Beat(13, card = BoardCard("Mir geht es gut, danke.", "I'm well, thank you.", phrase = true)),
                ),
            ),
            // 14: "What about the time of day?"
            Scene(
                14, "Morning to night",
                DiagramSpec(
                    "Guten Abend greets someone in the evening; Gute Nacht is said on the way out at night.",
                    nodes = listOf(
                        DNode("m", "Guten Morgen", 0.5f, 0.14f, w = 0.5f, h = 0.16f, tone = 1, sub = "good morning"),
                        DNode("a", "Guten Abend", 0.5f, 0.48f, w = 0.5f, h = 0.16f, tone = 1, sub = "arriving in the evening"),
                        DNode("n", "Gute Nacht", 0.5f, 0.82f, w = 0.5f, h = 0.16f, sub = "leaving at night"),
                    ),
                    edges = listOf(DEdge("m", "a"), DEdge("a", "n")),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(16, show = setOf("m", "a", "n"), card = BoardCard("Guten Morgen. Guten Abend.", "Good morning. Good evening. And: Gute Nacht, good night.", phrase = true)),
                    Beat(17, light = setOf("a", "n"), pulse = "a" to "n", card = BoardCard("Arriving or leaving?", "Arrive: Guten Abend. Leave at night: Gute Nacht.")),
                ),
            ),
            // 18: "Now goodbyes..."
            Scene(
                18, "Saying goodbye",
                DiagramSpec(
                    "Goodbyes follow the same split as hello: Tschüss with du, Auf Wiedersehen with Sie.",
                    nodes = listOf(
                        DNode("du", "du", 0.25f, 0.2f, w = 0.3f, h = 0.16f, kind = NodeKind.PILL, tone = 1),
                        DNode("sie", "Sie", 0.75f, 0.2f, w = 0.3f, h = 0.16f, kind = NodeKind.PILL),
                        DNode("t", "Tschüss", 0.25f, 0.62f, w = 0.38f, h = 0.2f, tone = 1, sub = "bye"),
                        DNode("aw", "Auf Wiedersehen", 0.75f, 0.62f, w = 0.42f, h = 0.2f, sub = "goodbye"),
                    ),
                    edges = listOf(DEdge("du", "t"), DEdge("sie", "aw")),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(18, show = setOf("du", "sie")),
                    Beat(19, show = setOf("t"), light = setOf("du", "t"), card = BoardCard("Tschüss, bis morgen!", "Bye, see you tomorrow!", phrase = true)),
                    Beat(22, show = setOf("aw"), light = setOf("sie", "aw"), pulse = "sie" to "aw", card = BoardCard("Auf Wiedersehen, Herr Müller.", "Goodbye, Mr Müller.", phrase = true)),
                ),
            ),
            // 23: "And the little words? Please and thank you?"
            Scene(
                23, "The little words",
                DiagramSpec(
                    "bitte is both please and you're welcome, the reply to danke; Entschuldigung is excuse me and sorry.",
                    nodes = listOf(
                        DNode("danke", "danke", 0.25f, 0.16f, w = 0.34f, h = 0.16f, tone = 1, sub = "thank you"),
                        DNode("bitte", "bitte", 0.75f, 0.16f, w = 0.34f, h = 0.16f, sub = "please; you're welcome"),
                        DNode("ja", "ja", 0.25f, 0.5f, w = 0.3f, h = 0.16f, tone = 1, sub = "yes"),
                        DNode("nein", "nein", 0.75f, 0.5f, w = 0.3f, h = 0.16f, tone = 1, sub = "no"),
                        DNode("ent", "Entschuldigung", 0.5f, 0.84f, w = 0.6f, h = 0.18f, sub = "excuse me; sorry"),
                    ),
                    edges = listOf(DEdge("danke", "bitte", "the reply")),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(24, show = setOf("danke", "bitte"), light = setOf("bitte"), pulse = "danke" to "bitte", card = BoardCard("bitte", "Please, and also you're welcome: the reply to danke.")),
                    Beat(25, card = BoardCard("Danke. Bitte.", "Thank you. You're welcome.", phrase = true)),
                    Beat(27, show = setOf("ja", "nein", "ent"), light = setOf("ja", "nein")),
                    Beat(28, light = setOf("ent"), card = BoardCard("Entschuldigung, wo ist die Toilette?", "Excuse me, where is the toilet?", phrase = true)),
                    Beat(30, light = setOf("ent"), card = BoardCard("Ent · schul · di · gung", "Four small beats, then together.")),
                ),
            ),
            // 32: "So what should I remember for the Goethe exam?"
            Scene(
                32, "For the Goethe exam",
                DiagramSpec(
                    "What greetings mean for the Goethe A1: the speaking part, the listening part, and bitte.",
                    nodes = listOf(
                        DNode("speak", "Speaking", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Guten Tag and Sie"),
                        DNode("hear", "Listening", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "friends or strangers?"),
                        DNode("bitte", "bitte", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "please and you're welcome"),
                    ),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(33, show = setOf("speak", "hear", "bitte"), card = BoardCard("Examiners are strangers", "Introduce yourself with Guten Tag and Sie.")),
                    Beat(35, card = BoardCard("Today", "Greet four people out loud: du or Sie first. End one with Auf Wiedersehen.")),
                    Beat(36, card = BoardCard("Tschüss! Bis bald!", "Bye! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    val all: List<PodcastBoard> by lazy { listOf(de1Words()) }
}
