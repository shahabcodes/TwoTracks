package com.shahaabapps.core.content

/**
 * The boards for the German A1 episodes. A German phrase goes up as a card,
 * the German as its title and the English under it, at the line where Jonas
 * says it; the diagrams show the choice behind the words (du or Sie, arriving
 * or leaving). Line numbers are each episode's own, from 0 (GermanPodcasts).
 */
internal object GermanBoards {
    private fun de1Words() = PodcastBoard(
        "de1-words",
        listOf(
            // 0: "Hi, and welcome to the first German episode. Today is greetings: hello..."
            Scene(
                0, "Three voices",
                DiagramSpec(
                    "Maya explains, Leo asks, Jonas says the German, and you say it back in the gap.",
                    nodes = listOf(
                        DNode("maya", "Maya", 0.18f, 0.2f, w = 0.3f, kind = NodeKind.PILL, tone = 1, sub = "explains"),
                        DNode("leo", "Leo", 0.18f, 0.5f, w = 0.3f, kind = NodeKind.PILL, tone = 1, sub = "asks"),
                        DNode("jonas", "Jonas", 0.5f, 0.82f, w = 0.3f, kind = NodeKind.PILL, sub = "says the German"),
                        DNode("you", "You", 0.84f, 0.42f, w = 0.22f, h = 0.2f, kind = NodeKind.USER),
                    ),
                    edges = listOf(
                        DEdge("jonas", "you", "repeat"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("maya", "leo", "you")),
                    Beat(1, show = setOf("jonas"), light = setOf("jonas"), card = BoardCard("Hallo! Ich bin Jonas.", "Hello! I'm Jonas.", phrase = true)),
                    Beat(2, light = setOf("jonas", "you"), pulse = "jonas" to "you", card = BoardCard("In the gap", "Say it out loud. Quietly on the bus is fine.")),
                ),
            ),
            // 3: "Quietly on the bus is fine. Saying it is how it sticks. Let's start wi..."
            Scene(
                3, "Formal or informal",
                DiagramSpec(
                    "Formal or informal: the choice decides your greeting, your goodbye and later your verb forms.",
                    nodes = listOf(
                        DNode("you", "You", 0.5f, 0.14f, w = 0.2f, kind = NodeKind.USER),
                        DNode("du", "du (informell)", 0.25f, 0.55f, w = 0.38f, h = 0.2f, tone = 1, sub = "friends, family"),
                        DNode("sie", "Sie (formell)", 0.75f, 0.55f, w = 0.38f, h = 0.2f, sub = "strangers, shops, work"),
                        DNode("n1", "Hallo", 0.25f, 0.88f, w = 0.3f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("n2", "Guten Tag", 0.75f, 0.88f, w = 0.3f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("you", "du"),
                        DEdge("you", "sie"),
                        DEdge("du", "n1", dashed = true),
                        DEdge("sie", "n2", dashed = true),
                    ),
                    aspect = 0.8f,
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
            // 14: "What about the time of day? English has good morning and good evening."
            Scene(
                14, "Morning to night",
                DiagramSpec(
                    "Guten Abend greets someone in the evening; Gute Nacht is said on the way out at night.",
                    nodes = listOf(
                        DNode("m", "Guten Morgen", 0.5f, 0.14f, w = 0.5f, tone = 1, sub = "good morning"),
                        DNode("a", "Guten Abend", 0.5f, 0.48f, w = 0.5f, tone = 1, sub = "arriving in the evening"),
                        DNode("n", "Gute Nacht", 0.5f, 0.82f, w = 0.5f, sub = "leaving at night"),
                    ),
                    edges = listOf(
                        DEdge("m", "a"),
                        DEdge("a", "n"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("m", "a", "n"), card = BoardCard("Guten Morgen. Guten Abend.", "Good morning. Good evening. And: Gute Nacht, good night.", phrase = true)),
                    Beat(17, light = setOf("a", "n"), pulse = "a" to "n", card = BoardCard("Arriving or leaving?", "Arrive: Guten Abend. Leave at night: Gute Nacht.")),
                ),
            ),
            // 18: "Exactly, and that's a mistake people make. «Guten Abend» greets someon..."
            Scene(
                18, "Saying goodbye",
                DiagramSpec(
                    "Goodbyes follow the same split as hello: Tschüss with du, Auf Wiedersehen with Sie.",
                    nodes = listOf(
                        DNode("du", "du", 0.25f, 0.2f, w = 0.3f, kind = NodeKind.PILL, tone = 1),
                        DNode("sie", "Sie", 0.75f, 0.2f, w = 0.3f, kind = NodeKind.PILL),
                        DNode("t", "Tschüss", 0.25f, 0.62f, w = 0.38f, h = 0.2f, tone = 1, sub = "bye"),
                        DNode("aw", "Auf Wiedersehen", 0.75f, 0.62f, w = 0.42f, h = 0.2f, sub = "goodbye"),
                    ),
                    edges = listOf(
                        DEdge("du", "t"),
                        DEdge("sie", "aw"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("du", "sie")),
                    Beat(19, show = setOf("t"), light = setOf("du", "t"), card = BoardCard("Tschüss, bis morgen!", "Bye, see you tomorrow!", phrase = true)),
                    Beat(22, show = setOf("aw"), light = setOf("sie", "aw"), pulse = "sie" to "aw", card = BoardCard("Auf Wiedersehen, Herr Müller.", "Goodbye, Mr Müller.", phrase = true)),
                ),
            ),
            // 23: "Okay. And the little words? Please and thank you?"
            Scene(
                23, "The little words",
                DiagramSpec(
                    "bitte is both please and you're welcome, the reply to danke; Entschuldigung is excuse me and sorry.",
                    nodes = listOf(
                        DNode("danke", "danke", 0.25f, 0.16f, w = 0.34f, tone = 1, sub = "thank you"),
                        DNode("bitte", "bitte", 0.75f, 0.16f, w = 0.34f, sub = "please; you're welcome"),
                        DNode("ja", "ja", 0.25f, 0.5f, w = 0.3f, tone = 1, sub = "yes"),
                        DNode("nein", "nein", 0.75f, 0.5f, w = 0.3f, tone = 1, sub = "no"),
                        DNode("ent", "Entschuldigung", 0.5f, 0.84f, w = 0.6f, h = 0.18f, sub = "excuse me; sorry"),
                    ),
                    edges = listOf(
                        DEdge("danke", "bitte", "the reply"),
                    ),
                    aspect = 0.8f,
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
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("speak", "hear", "bitte"), card = BoardCard("Examiners are strangers", "Introduce yourself with Guten Tag and Sie.")),
                    Beat(35, card = BoardCard("Today", "Greet four people out loud: du or Sie first. End one with Auf Wiedersehen.")),
                    Beat(36, card = BoardCard("Tschüss! Bis bald!", "Bye! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de1Grammar() = PodcastBoard(
        "de1-grammar",
        listOf(
            // 0: "Welcome back. Last time was hello and goodbye. Today is what sits unde..."
            Scene(
                0, "Today's three parts",
                DiagramSpec(
                    "The sounds, the alphabet for spelling, and the verb sein with its one rule: the verb is second.",
                    nodes = listOf(
                        DNode("snd", "Sounds", 0.27f, 0.3f, w = 0.42f, h = 0.2f, tone = 1, sub = "w, v, z, j and more"),
                        DNode("abc", "Alphabet", 0.73f, 0.3f, w = 0.42f, h = 0.2f, tone = 1, sub = "spell your name"),
                        DNode("sein", "sein", 0.27f, 0.7f, w = 0.42f, h = 0.2f, sub = "to be"),
                        DNode("v2", "Verb second", 0.73f, 0.7f, w = 0.42f, h = 0.2f, sub = "the one fixed rule"),
                    ),
                    edges = listOf(
                        DEdge("sein", "v2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("snd", "abc", "sein", "v2"), card = BoardCard("Regular spelling", "Once you know the sounds, you can read any word aloud.")),
                    Beat(1, card = BoardCard("Hallo! Wie geht's?", "Hi! How are you?", phrase = true)),
                    Beat(2, light = setOf("snd")),
                ),
            ),
            // 3: "A few letters make a different sound from English. The big one: the Ge..."
            Scene(
                3, "w and v swap seats",
                DiagramSpec(
                    "German w sounds like English v, and German v sounds like English f.",
                    nodes = listOf(
                        DNode("w", "w", 0.27f, 0.18f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "like English v"),
                        DNode("v", "v", 0.73f, 0.18f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "like English f"),
                        DNode("wa", "das Wasser", 0.27f, 0.55f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 1, sub = "water"),
                        DNode("wie", "wie", 0.27f, 0.84f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 1, sub = "how"),
                        DNode("va", "der Vater", 0.73f, 0.55f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 1, sub = "father"),
                        DNode("vi", "vier", 0.73f, 0.84f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 1, sub = "four"),
                    ),
                    edges = listOf(
                        DEdge("w", "v", "swap"),
                        DEdge("w", "wa", dashed = true),
                        DEdge("v", "va", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("w", "wa", "wie"), light = setOf("w")),
                    Beat(4, light = setOf("wa", "wie"), card = BoardCard("Wasser. Wie.", "Water. How.", phrase = true)),
                    Beat(6, show = setOf("v", "va", "vi"), light = setOf("v"), pulse = "w" to "v", card = BoardCard("Two letters swap seats", "w says v, and v says f.")),
                    Beat(7, light = setOf("va", "vi"), card = BoardCard("Vater. Vier.", "Father. Four.", phrase = true)),
                ),
            ),
            // 8: "Swapping seats. Got it. Any more troublemakers?"
            Scene(
                8, "z, j, ei and ie",
                DiagramSpec(
                    "z is a sharp ts, j is an English y; for ei and ie, say the second letter's English name.",
                    nodes = listOf(
                        DNode("z", "z", 0.27f, 0.22f, w = 0.44f, h = 0.22f, tone = 1, sub = "ts: zehn, die Zeit"),
                        DNode("j", "j", 0.73f, 0.22f, w = 0.44f, h = 0.22f, tone = 1, sub = "y: ja, das Jahr"),
                        DNode("ei", "ei", 0.27f, 0.68f, w = 0.44f, h = 0.22f, sub = "eye: nein, mein"),
                        DNode("ie", "ie", 0.73f, 0.68f, w = 0.44f, h = 0.22f, sub = "ee: Sie, wie"),
                    ),
                    edges = listOf(
                        DEdge("ei", "ie", "second letter", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("z", "j"), light = setOf("z", "j")),
                    Beat(10, card = BoardCard("Zehn. Zeit. Ja. Jahr.", "Ten. Time. Yes. Year.", phrase = true)),
                    Beat(12, show = setOf("ei", "ie"), light = setOf("ei", "ie"), pulse = "ei" to "ie", card = BoardCard("Say the second letter", "ei sounds like eye, ie like ee: nein is nine, Sie is zee.")),
                    Beat(13, card = BoardCard("Nein. Mein. Sie. Wie.", "No. My. You, polite. How.", phrase = true)),
                ),
            ),
            // 14: "And the dots? I've seen dots on some letters."
            Scene(
                14, "Umlaut, ß and ch",
                DiagramSpec(
                    "The Umlaut rounds the lips, ß is a sharp ss, and ch depends on the vowel before it.",
                    nodes = listOf(
                        DNode("um", "ä ö ü", 0.27f, 0.22f, w = 0.44f, h = 0.22f, sub = "Umlaut: round lips"),
                        DNode("ss", "ß", 0.73f, 0.22f, w = 0.44f, h = 0.22f, tone = 1, sub = "sharp ss: die Straße"),
                        DNode("ch1", "ch after e, i", 0.27f, 0.68f, w = 0.44f, h = 0.22f, tone = 1, sub = "soft hiss: ich"),
                        DNode("ch2", "ch after a, o, u", 0.73f, 0.68f, w = 0.44f, h = 0.22f, tone = 1, sub = "throat: das Buch"),
                    ),
                    edges = listOf(
                        DEdge("ch1", "ch2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("um", "ss", "ch1", "ch2"), light = setOf("um"), card = BoardCard("Tschüss", "You already know one Umlaut: round your lips for ü.")),
                    Beat(16, light = setOf("ss", "ch1", "ch2"), card = BoardCard("Schön. Straße. Ich. Buch.", "Beautiful. Street. I. Book.", phrase = true)),
                ),
            ),
            // 17: "My mouth is tired. What's next?"
            Scene(
                17, "Spelling your name",
                DiagramSpec(
                    "Spell with German letter names: i is ee and e is eh, the pair that ruins a surname.",
                    nodes = listOf(
                        DNode("q", "Wie schreibt man das?", 0.5f, 0.15f, w = 0.8f, h = 0.18f, kind = NodeKind.NOTE, sub = "how do you spell that?"),
                        DNode("u", "U", 0.1f, 0.5f, w = 0.16f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "oo"),
                        DNode("s", "S", 0.3f, 0.5f, w = 0.16f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "ess"),
                        DNode("a", "A", 0.5f, 0.5f, w = 0.16f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "ah"),
                        DNode("i", "I", 0.7f, 0.5f, w = 0.16f, h = 0.18f, kind = NodeKind.PILL, sub = "ee"),
                        DNode("d", "D", 0.9f, 0.5f, w = 0.16f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "deh"),
                        DNode("dop", "Doppel", 0.5f, 0.84f, w = 0.5f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "a double letter"),
                    ),
                    edges = listOf(
                        DEdge("u", "s"),
                        DEdge("s", "a"),
                        DEdge("a", "i"),
                        DEdge("i", "d"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("q"), light = setOf("q")),
                    Beat(19, light = setOf("q"), card = BoardCard("Wie schreibt man das?", "How do you spell that?", phrase = true)),
                    Beat(21, show = setOf("dop"), light = setOf("dop"), card = BoardCard("i is ee, e is eh", "German letter names, not English ones.")),
                    Beat(22, show = setOf("u", "s", "a", "i", "d"), card = BoardCard("U, S, A, I, D. Usaid.", "Usaid, spelled with German letter names.", phrase = true)),
                    Beat(23, light = setOf("i"), pulse = "a" to "i"),
                ),
            ),
            // 24: "Very. Now the verb «sein», to be. Changing a verb for the person is ca..."
            Scene(
                24, "sein: to be",
                DiagramSpec(
                    "sein changes for six persons. Keep ihr seid and sie sind apart.",
                    nodes = listOf(
                        DNode("ich", "ich bin", 0.27f, 0.18f, w = 0.44f, h = 0.2f, tone = 1, sub = "I am"),
                        DNode("du", "du bist", 0.27f, 0.5f, w = 0.44f, h = 0.2f, tone = 1, sub = "you are, a friend"),
                        DNode("er", "er, sie, es: ist", 0.27f, 0.82f, w = 0.44f, h = 0.2f, tone = 1, sub = "he, she, it is"),
                        DNode("wir", "wir sind", 0.73f, 0.18f, w = 0.44f, h = 0.2f, tone = 1, sub = "we are"),
                        DNode("ihr", "ihr seid", 0.73f, 0.5f, w = 0.44f, h = 0.2f, sub = "you all are"),
                        DNode("sie", "sie, Sie sind", 0.73f, 0.82f, w = 0.44f, h = 0.2f, sub = "they; you, polite"),
                    ),
                    edges = listOf(
                        DEdge("ihr", "sie", "keep apart"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("ich"), card = BoardCard("konjugieren", "Changing a verb for the person: ich bin, du bist.")),
                    Beat(25, light = setOf("ich"), card = BoardCard("Ich bin Usaid.", "I am Usaid.", phrase = true)),
                    Beat(27, show = setOf("du", "er"), light = setOf("du", "er")),
                    Beat(28, card = BoardCard("Du bist nett. Sie ist Lehrerin.", "You are nice. She is a teacher. No a before a job.", phrase = true)),
                    Beat(30, show = setOf("wir"), light = setOf("wir")),
                    Beat(31, card = BoardCard("Wir sind aus Indien und Pakistan.", "We are from India and Pakistan.", phrase = true)),
                    Beat(33, show = setOf("ihr", "sie"), light = setOf("ihr", "sie")),
                    Beat(34, card = BoardCard("Ich bin, du bist, er ist", "Then wir sind, ihr seid, sie sind. All six persons of sein.", phrase = true)),
                    Beat(35, light = setOf("ihr", "sie"), pulse = "ihr" to "sie"),
                ),
            ),
            // 36: "They are. Keep the pair apart: «ihr seid», «sie sind». And one trap: f..."
            Scene(
                36, "Verb second",
                DiagramSpec(
                    "Whatever takes the first seat, the verb takes the second: Heute bin ich in Doha.",
                    nodes = listOf(
                        DNode("p1", "Position 1", 0.17f, 0.2f, w = 0.3f, h = 0.2f, tone = 2, sub = "Ich"),
                        DNode("p2", "Verb", 0.5f, 0.2f, w = 0.3f, h = 0.2f, sub = "bin"),
                        DNode("p3", "Rest", 0.83f, 0.2f, w = 0.3f, h = 0.2f, tone = 2, sub = "in Doha"),
                        DNode("q1", "Position 1", 0.17f, 0.55f, w = 0.3f, h = 0.2f, tone = 2, sub = "Heute"),
                        DNode("q2", "Verb", 0.5f, 0.55f, w = 0.3f, h = 0.2f, sub = "bin"),
                        DNode("q3", "Rest", 0.83f, 0.55f, w = 0.3f, h = 0.2f, tone = 2, sub = "ich in Doha"),
                        DNode("bad", "Heute ich bin", 0.5f, 0.87f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "verb third: wrong"),
                    ),
                    edges = listOf(
                        DEdge("p1", "p2"),
                        DEdge("p2", "p3"),
                        DEdge("q1", "q2"),
                        DEdge("q2", "q3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("p1", "p2", "p3"), light = setOf("p2"), card = BoardCard("Not: Ich bin gut", "That means good at something. For I'm well: Mir geht es gut.")),
                    Beat(37, show = setOf("q1", "q2", "q3"), light = setOf("q2"), card = BoardCard("Heute bin ich in Doha.", "Today I am in Doha.", phrase = true)),
                    Beat(39, show = setOf("bad"), light = setOf("q1", "q2"), pulse = "q1" to "q2", card = BoardCard("First seat, second seat", "Heute takes the first seat, the verb the second, ich moves behind.")),
                    Beat(40, light = setOf("q2"), card = BoardCard("Heute bin ich müde.", "Today I am tired.", phrase = true)),
                ),
            ),
            // 41: "So what should I remember for the Goethe exam?"
            Scene(
                41, "For the Goethe exam",
                DiagramSpec(
                    "What sounds and sein mean for the Goethe A1: speaking, reading and writing.",
                    nodes = listOf(
                        DNode("spk", "Sprechen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "spell your name"),
                        DNode("les", "Lesen", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "wir sind, ihr seid"),
                        DNode("sch", "Schreiben", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "verb second, every sentence"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(42, show = setOf("spk", "les", "sch"), card = BoardCard("German letter names", "Know Wie schreibt man das? and spell your name: i is ee, e is eh.")),
                    Beat(44, card = BoardCard("Today", "Ten words with w, v, z and j; your street spelled; sein for six; three Heute lines.")),
                    Beat(45, card = BoardCard("Tschüss, bis morgen!", "Bye, see you tomorrow!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de1Dialogue() = PodcastBoard(
        "de1-dialogue",
        listOf(
            // 0: "Welcome back. Today we meet someone for the first time. It's Usaid's f..."
            Scene(
                0, "At the language school",
                DiagramSpec(
                    "A teacher and a new student are strangers, so the first meeting is formal: Guten Tag and Sie.",
                    nodes = listOf(
                        DNode("fw", "Frau Weber", 0.22f, 0.2f, h = 0.2f, kind = NodeKind.USER),
                        DNode("us", "Usaid", 0.78f, 0.2f, h = 0.2f, kind = NodeKind.USER, tone = 1),
                        DNode("sie", "Sie (formell)", 0.5f, 0.58f, w = 0.5f, h = 0.2f, sub = "teacher and new student"),
                        DNode("hei", "Ich heiße ...", 0.5f, 0.87f, w = 0.5f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("fw", "us", "Sie"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("fw", "us")),
                    Beat(1, light = setOf("fw"), card = BoardCard("Ich bin Frau Weber. Wie heißen Sie?", "I am Mrs Weber. What is your name?", phrase = true)),
                    Beat(3, show = setOf("sie"), light = setOf("sie"), pulse = "fw" to "us"),
                    Beat(4, show = setOf("hei"), light = setOf("us", "hei"), card = BoardCard("Ich heiße Usaid Shahab.", "My name is Usaid Shahab. Ich bin Usaid works too.", phrase = true)),
                ),
            ),
            // 6: "Yes, both work: «Ich heiße» or «Ich bin». Next she asks where he's fro..."
            Scene(
                6, "Woher or wo?",
                DiagramSpec(
                    "woher asks where you come from, answered with aus; where you live is answered with in.",
                    nodes = listOf(
                        DNode("woher", "Woher?", 0.27f, 0.2f, w = 0.42f, h = 0.2f, sub = "from where"),
                        DNode("wo", "Wo?", 0.73f, 0.2f, w = 0.42f, h = 0.2f, tone = 1, sub = "where"),
                        DNode("aus", "Ich komme aus Indien", 0.27f, 0.62f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE),
                        DNode("in", "Ich wohne in Doha", 0.73f, 0.62f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("woher", "aus", "aus"),
                        DEdge("wo", "in", "in"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("woher")),
                    Beat(7, light = setOf("woher"), card = BoardCard("Woher kommen Sie, Herr Shahab?", "Where are you from, Mr Shahab?", phrase = true)),
                    Beat(9, show = setOf("wo"), light = setOf("woher"), card = BoardCard("Not where you live", "woher means from where. Where you live is a different question.")),
                    Beat(10, show = setOf("aus", "in"), light = setOf("aus", "in"), card = BoardCard("Ich komme aus Indien,", "I come from India, aber ich wohne in Doha: but I live in Doha.", phrase = true)),
                    Beat(11, light = setOf("aus"), pulse = "woher" to "aus"),
                ),
            ),
            // 12: "That's it. Two small words that carry the whole introduction. Now Frau..."
            Scene(
                12, "sie or Sie",
                DiagramSpec(
                    "sie (she) and Sie (polite you) sound the same; the verb tells them apart.",
                    nodes = listOf(
                        DNode("das", "Das ist Sanya.", 0.5f, 0.15f, w = 0.56f, h = 0.18f, kind = NodeKind.NOTE, tone = 1, sub = "this is Sanya"),
                        DNode("she", "sie kommt", 0.27f, 0.52f, w = 0.44f, h = 0.2f, sub = "she comes"),
                        DNode("you", "Sie kommen", 0.73f, 0.52f, w = 0.44f, h = 0.2f, tone = 1, sub = "you come, polite"),
                        DNode("job", "Sie ist Lehrerin.", 0.5f, 0.86f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "no a before a job"),
                    ),
                    edges = listOf(
                        DEdge("das", "she"),
                        DEdge("she", "you", "same sound", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(13, show = setOf("das"), card = BoardCard("Und das ist?", "And this is...?", phrase = true)),
                    Beat(15, show = setOf("she", "job"), light = setOf("she")),
                    Beat(16, light = setOf("das", "job"), card = BoardCard("Sie kommt aus Pakistan.", "This is Sanya. She comes from Pakistan. She is a teacher.", phrase = true)),
                    Beat(18, show = setOf("you"), light = setOf("she", "you"), pulse = "she" to "you", card = BoardCard("Listen to the verb", "sie kommt: she comes. Sie kommen: you come.")),
                ),
            ),
            // 19: "Freut mich! Willkommen."
            Scene(
                19, "Freut mich, Wie bitte?",
                DiagramSpec(
                    "Freut mich at the start of a first meeting; Wie bitte? when you missed it; Wie schreibt man das? for the spelling.",
                    nodes = listOf(
                        DNode("meet", "First meeting", 0.5f, 0.16f, w = 0.72f, h = 0.2f, tone = 1, sub = "Freut mich! Willkommen."),
                        DNode("miss", "Missed it", 0.5f, 0.5f, w = 0.72f, h = 0.2f, sub = "Wie bitte?"),
                        DNode("spell", "Need the spelling", 0.5f, 0.84f, w = 0.72f, h = 0.2f, tone = 1, sub = "Wie schreibt man das?"),
                    ),
                    edges = listOf(
                        DEdge("meet", "miss"),
                        DEdge("miss", "spell"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(19, show = setOf("meet"), light = setOf("meet"), card = BoardCard("Freut mich! Willkommen.", "Pleased to meet you! Welcome.", phrase = true)),
                    Beat(21, light = setOf("meet"), card = BoardCard("Not on the way out", "Freut mich belongs at the start of a first meeting.")),
                    Beat(22, show = setOf("miss"), light = setOf("miss"), pulse = "meet" to "miss", card = BoardCard("Danke. Wie bitte, wie heißen Sie?", "Thank you. Sorry, what is your name?", phrase = true)),
                    Beat(25, card = BoardCard("Weber. W, E, B, E, R.", "Weber, spelled with the German letter names.", phrase = true)),
                    Beat(27, show = setOf("spell"), light = setOf("spell"), pulse = "miss" to "spell"),
                    Beat(28, card = BoardCard("Wie schreibt man das?", "How do you spell that?", phrase = true)),
                ),
            ),
            // 29: "What about my job? How do I say what I do?"
            Scene(
                29, "The introduction card",
                DiagramSpec(
                    "The Sprechen part 1 card: say one sentence for each word and the first part is done.",
                    nodes = listOf(
                        DNode("name", "der Name", 0.18f, 0.3f, w = 0.3f, h = 0.2f, tone = 1, sub = "Ich heiße"),
                        DNode("alter", "das Alter", 0.5f, 0.3f, w = 0.3f, h = 0.2f, tone = 1, sub = "Jahre alt"),
                        DNode("land", "das Land", 0.82f, 0.3f, w = 0.3f, h = 0.2f, tone = 1, sub = "Ich komme aus"),
                        DNode("ort", "der Wohnort", 0.18f, 0.7f, w = 0.3f, h = 0.2f, tone = 1, sub = "Ich wohne in"),
                        DNode("spr", "die Sprachen", 0.5f, 0.7f, w = 0.3f, h = 0.2f, tone = 1, sub = "Ich spreche"),
                        DNode("beruf", "der Beruf", 0.82f, 0.7f, w = 0.3f, h = 0.2f, sub = "von Beruf"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("beruf"), light = setOf("beruf"), card = BoardCard("von Beruf", "By profession. No ein in front of the job.")),
                    Beat(31, card = BoardCard("Ich bin Softwareentwickler", "...von Beruf: I am a software developer by profession.", phrase = true)),
                    Beat(33, show = setOf("name", "alter", "land", "ort", "spr")),
                    Beat(34, light = setOf("alter", "spr"), card = BoardCard("Ich bin achtundzwanzig Jahre alt.", "I am twenty-eight. And: Ich spreche Englisch und Hindi.", phrase = true)),
                ),
            ),
            // 35: "So what should I remember for the Goethe exam?"
            Scene(
                35, "For the Goethe exam",
                DiagramSpec(
                    "What a first meeting means for the Goethe A1: the speaking part, the listening part, and Wie bitte.",
                    nodes = listOf(
                        DNode("spk", "Sprechen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "name, country, town, job"),
                        DNode("hor", "Hören", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "woher: from where"),
                        DNode("wb", "Wie bitte?", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "rescues the turn"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("spk", "hor", "wb"), card = BoardCard("One sentence each", "Name, country, town and job: the first speaking part.")),
                    Beat(38, card = BoardCard("Today", "Record it: name, country, town, job. Spell your surname. Use Wie bitte? once.")),
                    Beat(39, card = BoardCard("Tschüss! Bis bald!", "Bye! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de1Practice() = PodcastBoard(
        "de1-practice",
        listOf(
            // 0: "Welcome back. Today is practice: you introduce yourself, on paper and ..."
            Scene(
                0, "Three exam tasks",
                DiagramSpec(
                    "The four exam papers. Today rehearses the form, the short message and the introduction.",
                    nodes = listOf(
                        DNode("hor", "Hören", 0.27f, 0.22f, w = 0.42f, h = 0.22f, tone = 2, sub = "20 min"),
                        DNode("les", "Lesen", 0.73f, 0.22f, w = 0.42f, h = 0.22f, tone = 1, sub = "25 min"),
                        DNode("sch", "Schreiben", 0.27f, 0.62f, w = 0.42f, h = 0.22f, sub = "form and message"),
                        DNode("spr", "Sprechen", 0.73f, 0.62f, w = 0.42f, h = 0.22f, sub = "introduction"),
                    ),
                    edges = listOf(
                        DEdge("sch", "spr", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("hor", "les", "sch", "spr"), light = setOf("sch", "spr")),
                    Beat(1, light = setOf("spr"), card = BoardCard("Guten Tag! Wie heißen Sie?", "Good day! What is your name?", phrase = true)),
                ),
            ),
            // 3: "Pretty much. A German form has boxes, and each box has a German name. ..."
            Scene(
                3, "Reading a form",
                DiagramSpec(
                    "Each box on a German form has a name. Read the box, then copy the answer from the situation.",
                    nodes = listOf(
                        DNode("fam", "der Familienname", 0.26f, 0.12f, w = 0.46f, h = 0.18f, tone = 1, sub = "Shahab"),
                        DNode("vor", "der Vorname", 0.74f, 0.12f, w = 0.46f, h = 0.18f, tone = 1, sub = "Usaid"),
                        DNode("land", "das Land", 0.26f, 0.37f, w = 0.46f, h = 0.18f, tone = 1, sub = "Indien"),
                        DNode("ort", "der Wohnort", 0.74f, 0.37f, w = 0.46f, h = 0.18f, sub = "Doha"),
                        DNode("alt", "das Alter", 0.26f, 0.62f, w = 0.46f, h = 0.18f, tone = 1, sub = "28"),
                        DNode("ber", "der Beruf", 0.74f, 0.62f, w = 0.46f, h = 0.18f, tone = 1, sub = "Softwareentwickler"),
                        DNode("sp", "die Sprachen", 0.5f, 0.87f, w = 0.6f, h = 0.18f, tone = 1, sub = "Englisch, Hindi, Deutsch (A1)"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("fam", "vor"), light = setOf("fam", "vor")),
                    Beat(4, card = BoardCard("Familienname: Shahab.", "Surname: Shahab. Vorname: Usaid, the first name.", phrase = true)),
                    Beat(6, show = setOf("land", "ort"), light = setOf("land", "ort")),
                    Beat(7, card = BoardCard("Land: Indien. Wohnort: Doha.", "Country: India. Place of residence: Doha.", phrase = true)),
                    Beat(9, show = setOf("alt", "ber"), light = setOf("ort"), card = BoardCard("Town in Wohnort", "wohnen is to live; the rest is the place. The country goes in Land.")),
                    Beat(10, light = setOf("alt", "ber"), card = BoardCard("Beruf: Softwareentwickler.", "Job: software developer. And Alter: achtundzwanzig, age.", phrase = true)),
                    Beat(12, show = setOf("sp"), light = setOf("sp"), card = BoardCard("Copy, don't invent", "In the form part, the situation already gives you the answers.")),
                    Beat(13, card = BoardCard("Sprachen: Englisch, Hindi, Deutsch.", "Languages: English, Hindi, German.", phrase = true)),
                ),
            ),
            // 14: "Okay, form done. What's the short message?"
            Scene(
                14, "Four sentences",
                DiagramSpec(
                    "A message to a new neighbour: name, country with aus, town with in, job with no article.",
                    nodes = listOf(
                        DNode("n", "Name", 0.5f, 0.12f, w = 0.8f, h = 0.18f, tone = 1, sub = "Ich heiße Sanya."),
                        DNode("c", "Land: aus", 0.5f, 0.37f, w = 0.8f, h = 0.18f, sub = "Ich komme aus Pakistan."),
                        DNode("t", "Wohnort: in", 0.5f, 0.62f, w = 0.8f, h = 0.18f, sub = "Ich wohne in Doha."),
                        DNode("j", "Beruf", 0.5f, 0.87f, w = 0.8f, h = 0.18f, tone = 1, sub = "Ich bin Lehrerin."),
                    ),
                    edges = listOf(
                        DEdge("n", "c"),
                        DEdge("c", "t"),
                        DEdge("t", "j"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("n"), light = setOf("n")),
                    Beat(16, card = BoardCard("Hallo! Ich heiße Sanya.", "Hello! My name is Sanya.", phrase = true)),
                    Beat(18, show = setOf("c"), light = setOf("c")),
                    Beat(19, card = BoardCard("Ich komme aus Pakistan.", "I come from Pakistan.", phrase = true)),
                    Beat(21, show = setOf("t"), light = setOf("c", "t"), pulse = "c" to "t", card = BoardCard("aus or in", "aus for where you come from, in for where you live.")),
                    Beat(22, light = setOf("t"), card = BoardCard("Ich wohne in Doha.", "I live in Doha.", phrase = true)),
                    Beat(24, show = setOf("j"), light = setOf("j"), card = BoardCard("No article for a job", "Ich bin Lehrerin, not eine Lehrerin.")),
                    Beat(25, card = BoardCard("Ich bin Lehrerin von Beruf.", "I am a teacher by profession.", phrase = true)),
                ),
            ),
            // 26: "Four sentences. Is that really enough?"
            Scene(
                26, "Verb second, every time",
                DiagramSpec(
                    "heiße, komme, wohne, bin: the verb is second in every sentence of the message.",
                    nodes = listOf(
                        DNode("p1", "Position 1", 0.17f, 0.22f, w = 0.3f, h = 0.22f, tone = 2, sub = "Ich"),
                        DNode("p2", "Verb", 0.5f, 0.22f, w = 0.3f, h = 0.22f, sub = "komme"),
                        DNode("p3", "Rest", 0.83f, 0.22f, w = 0.3f, h = 0.22f, tone = 2, sub = "aus Indien"),
                        DNode("msg", "Viele Grüße, Usaid", 0.5f, 0.62f, w = 0.64f, h = 0.2f, kind = NodeKind.NOTE, tone = 1, sub = "a friendly ending"),
                    ),
                    edges = listOf(
                        DEdge("p1", "p2"),
                        DEdge("p2", "p3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("p1", "p2", "p3"), light = setOf("p2"), pulse = "p1" to "p2"),
                    Beat(28, show = setOf("msg"), card = BoardCard("Ich komme aus Indien.", "Usaid's message: name, country, town, job, then Viele Grüße.", phrase = true)),
                    Beat(29, light = setOf("msg"), card = BoardCard("Viele Grüße", "Best wishes: a friendly way to end a message.")),
                ),
            ),
            // 30: "Yes, a friendly way to end a message. Now say it out loud: thirty seco..."
            Scene(
                30, "Thirty seconds out loud",
                DiagramSpec(
                    "The speaking part: name, where you are from and live, job and languages, then spell the surname.",
                    nodes = listOf(
                        DNode("s1", "Name", 0.5f, 0.12f, w = 0.8f, h = 0.18f, tone = 1, sub = "Ich heiße ..."),
                        DNode("s2", "Land and Wohnort", 0.5f, 0.37f, w = 0.8f, h = 0.18f, tone = 1, sub = "Ich komme aus ... Ich wohne in ..."),
                        DNode("s3", "Beruf and Sprachen", 0.5f, 0.62f, w = 0.8f, h = 0.18f, sub = "von Beruf. Ich spreche ..."),
                        DNode("s4", "Spell the surname", 0.5f, 0.87f, w = 0.8f, h = 0.18f, sub = "S, H, A, H, A, B"),
                    ),
                    edges = listOf(
                        DEdge("s1", "s2"),
                        DEdge("s2", "s3"),
                        DEdge("s3", "s4"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("s1", "s2", "s3"), light = setOf("s3")),
                    Beat(31, card = BoardCard("Ich spreche Englisch und Hindi.", "I speak English and Hindi.", phrase = true)),
                    Beat(33, show = setOf("s4"), light = setOf("s4"), pulse = "s3" to "s4", card = BoardCard("Heute bin ich", "The verb jumps in right after Heute. Never Heute ich bin.")),
                    Beat(34, card = BoardCard("Shahab. S, H, A, H, A, B.", "Shahab, spelled with the German letter names.", phrase = true)),
                    Beat(36, light = setOf("s4"), card = BoardCard("Until it's automatic", "Slowly. If you hear Wie bitte?, simply say it again.")),
                ),
            ),
            // 37: "So what should I remember for the Goethe exam?"
            Scene(
                37, "For the Goethe exam",
                DiagramSpec(
                    "What introducing yourself means for the Goethe A1: the form, the message, and the speaking part.",
                    nodes = listOf(
                        DNode("f", "Schreiben 1", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "the form: copy"),
                        DNode("m", "Schreiben 2", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "four sentences"),
                        DNode("s", "Sprechen 1", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "introduce, then spell"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("f", "m", "s"), card = BoardCard("Read each box", "Name, Land, Wohnort, Alter, Beruf: copy from the situation.")),
                    Beat(40, card = BoardCard("Today", "Record your four sentences. Check w sounds like v, verb second, surname spelled.")),
                    Beat(41, card = BoardCard("Tschüss! Bis morgen!", "Bye! See you tomorrow!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de2Words() = PodcastBoard(
        "de2-words",
        listOf(
            // 0: "Hi, and welcome back. Today is family, numbers and countries: who is i..."
            Scene(
                0, "Mein or meine",
                DiagramSpec(
                    "The word after my decides the ending: mein before a der word, meine before a die word.",
                    nodes = listOf(
                        DNode("fam", "die Familie", 0.5f, 0.1f, w = 0.5f, tone = 1, sub = "family"),
                        DNode("mann", "der Mann", 0.25f, 0.38f, w = 0.4f, h = 0.18f, tone = 1, sub = "man; husband"),
                        DNode("frau", "die Frau", 0.75f, 0.38f, w = 0.4f, h = 0.18f, tone = 1, sub = "woman; wife; Mrs"),
                        DNode("mein", "mein", 0.25f, 0.66f, w = 0.32f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("meine", "meine", 0.75f, 0.66f, w = 0.32f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("note", "Frau Weber: Mrs Weber", 0.5f, 0.9f, w = 0.7f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("mann", "mein"),
                        DEdge("frau", "meine"),
                        DEdge("fam", "meine", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("fam")),
                    Beat(1, show = setOf("meine"), light = setOf("fam", "meine"), pulse = "fam" to "meine", card = BoardCard("Meine Familie wohnt in Karachi.", "My family lives in Karachi.", phrase = true)),
                    Beat(3, light = setOf("fam", "meine"), card = BoardCard("Learn the article", "die Familie, so meine: my with an extra e.")),
                    Beat(4, show = setOf("mann", "frau"), light = setOf("mann", "frau"), card = BoardCard("Der Mann. Die Frau.", "The man. The woman.", phrase = true)),
                    Beat(6, show = setOf("note"), light = setOf("note"), card = BoardCard("Two meanings each", "Mann: man and husband. Frau: woman, wife, and Mrs.")),
                    Beat(7, show = setOf("mein"), light = setOf("mein", "meine"), card = BoardCard("Mein Mann heißt Usaid.", "My husband is called Usaid. Meine Frau ist Lehrerin: my wife is a teacher.", phrase = true)),
                    Beat(9, light = setOf("mann", "mein"), pulse = "mann" to "mein", card = BoardCard("der: mein. die: meine.", "mein Mann, but meine Frau and meine Familie.")),
                ),
            ),
            // 10: "Meine Mutter kocht gern. Mein Vater ist Ingenieur."
            Scene(
                10, "Parents, siblings, a child",
                DiagramSpec(
                    "Learn each family word with its article; Eltern and Geschwister only come as plurals.",
                    nodes = listOf(
                        DNode("vater", "der Vater", 0.25f, 0.11f, w = 0.42f, h = 0.18f, tone = 1, sub = "father"),
                        DNode("mutter", "die Mutter", 0.75f, 0.11f, w = 0.42f, h = 0.18f, tone = 1, sub = "mother"),
                        DNode("eltern", "die Eltern", 0.5f, 0.37f, w = 0.5f, h = 0.18f, sub = "parents, plural only"),
                        DNode("bruder", "der Bruder", 0.25f, 0.63f, w = 0.42f, h = 0.18f, tone = 1, sub = "brother"),
                        DNode("schw", "die Schwester", 0.75f, 0.63f, w = 0.42f, h = 0.18f, tone = 1, sub = "sister"),
                        DNode("kind", "das Kind", 0.5f, 0.89f, w = 0.42f, h = 0.18f, sub = "child"),
                    ),
                    edges = listOf(
                        DEdge("vater", "eltern"),
                        DEdge("mutter", "eltern"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("vater", "mutter"), light = setOf("vater", "mutter"), card = BoardCard("Meine Mutter kocht gern.", "My mother likes cooking. Mein Vater ist Ingenieur: my father is an engineer.", phrase = true)),
                    Beat(12, show = setOf("eltern"), light = setOf("eltern"), pulse = "vater" to "eltern", card = BoardCard("Plural only", "die Eltern, parents. Geschwister, brothers and sisters together.")),
                    Beat(13, show = setOf("bruder"), light = setOf("bruder"), card = BoardCard("Ich habe einen Bruder.", "I have a brother. And: Meine Eltern wohnen in Indien.", phrase = true)),
                    Beat(15, light = setOf("bruder"), card = BoardCard("einen Bruder", "After ich habe, a der word takes einen. Learn it as one chunk.")),
                    Beat(16, show = setOf("schw"), light = setOf("schw"), card = BoardCard("Sanya hat zwei Schwestern.", "Sanya has two sisters.", phrase = true)),
                    Beat(18, show = setOf("kind"), light = setOf("kind"), card = BoardCard("das Kind", "A das word, whether the child is a boy or a girl.")),
                    Beat(19, light = setOf("kind"), card = BoardCard("Wir haben kein Kind.", "We have no child.", phrase = true)),
                ),
            ),
            // 20: "Not yet, anyway. Now numbers. I've heard they're backwards."
            Scene(
                20, "Zero to nineteen",
                DiagramSpec(
                    "Zero to twelve are their own words; the teens are the unit plus zehn.",
                    nodes = listOf(
                        DNode("own", "0 to 12", 0.5f, 0.15f, w = 0.7f, h = 0.22f, tone = 1, sub = "own words: eins, zwei, elf, zwölf"),
                        DNode("teens", "13 to 19", 0.5f, 0.5f, w = 0.7f, h = 0.22f, sub = "unit plus zehn: dreizehn"),
                        DNode("trap", "sechzehn, siebzehn", 0.5f, 0.84f, w = 0.7f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "six and seven shrink"),
                    ),
                    edges = listOf(
                        DEdge("own", "teens"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("own")),
                    Beat(22, light = setOf("own"), card = BoardCard("Null, eins, zwei, drei, vier", "Zero, one, two, three, four, and on to zehn, ten.", phrase = true)),
                    Beat(24, show = setOf("teens"), light = setOf("teens"), pulse = "own" to "teens", card = BoardCard("Teens add zehn", "drei and zehn: dreizehn, thirteen.")),
                    Beat(25, show = setOf("trap"), light = setOf("trap"), card = BoardCard("Elf, zwölf, dreizehn", "Eleven, twelve, thirteen. Then sechzehn, siebzehn: sixteen, seventeen.", phrase = true)),
                ),
            ),
            // 26: "And the big one. Twenty-one. How does that go?"
            Scene(
                26, "Units first",
                DiagramSpec(
                    "German reads two-digit numbers backwards: the units come first, then und, then the tens.",
                    nodes = listOf(
                        DNode("u", "acht", 0.18f, 0.32f, w = 0.3f, h = 0.22f, sub = "8, units first"),
                        DNode("und", "und", 0.5f, 0.32f, w = 0.22f, h = 0.22f, tone = 2),
                        DNode("t", "zwanzig", 0.82f, 0.32f, w = 0.3f, h = 0.22f, tone = 1, sub = "20, tens last"),
                        DNode("n", "achtundzwanzig = 28", 0.5f, 0.64f, w = 0.76f, kind = NodeKind.NOTE, tone = 2),
                        DNode("age", "Wie alt bist du?", 0.5f, 0.88f, w = 0.6f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("u", "und"),
                        DEdge("und", "t"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("u", "und", "t"), light = setOf("u"), pulse = "u" to "und", card = BoardCard("Four and twenty blackbirds", "Units first, then und, then the tens, all as one word.")),
                    Beat(28, show = setOf("n"), light = setOf("n"), card = BoardCard("Einundzwanzig, achtundzwanzig", "Twenty-one, twenty-eight. And vierunddreißig, thirty-four.", phrase = true)),
                    Beat(30, light = setOf("t"), card = BoardCard("dreißig has ß", "Every other ten ends in zig: zwanzig, vierzig, fünfzig.")),
                    Beat(31, show = setOf("age"), light = setOf("age"), card = BoardCard("Ich bin achtundzwanzig Jahre alt.", "Wie alt bist du? How old are you? I'm twenty-eight years old.", phrase = true)),
                ),
            ),
            // 32: "And my phone number? Do I say it in big chunks?"
            Scene(
                32, "Phone, country, language",
                DiagramSpec(
                    "A phone number goes digit by digit; kommen aus names the country, sprechen the language.",
                    nodes = listOf(
                        DNode("tel", "die Telefonnummer", 0.5f, 0.12f, w = 0.76f, h = 0.18f, sub = "digit by digit"),
                        DNode("aus", "kommen aus", 0.25f, 0.43f, w = 0.44f, h = 0.2f, tone = 1, sub = "the country"),
                        DNode("spr", "sprechen", 0.75f, 0.43f, w = 0.44f, h = 0.2f, tone = 1, sub = "the language"),
                        DNode("form", "On a form", 0.5f, 0.8f, w = 0.86f, h = 0.26f, kind = NodeKind.NOTE, tone = 2, sub = "Staatsangehörigkeit; ledig or verheiratet"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("tel"), light = setOf("tel")),
                    Beat(34, light = setOf("tel"), card = BoardCard("Wie ist Ihre Telefonnummer?", "What is your phone number? Then the digits: null, eins, sieben, drei.", phrase = true)),
                    Beat(36, show = setOf("aus", "spr"), light = setOf("aus", "spr")),
                    Beat(37, light = setOf("aus", "spr"), card = BoardCard("Usaid kommt aus Indien.", "Usaid comes from India. Ich spreche Englisch: I speak English.", phrase = true)),
                    Beat(39, show = setOf("form"), light = setOf("form"), card = BoardCard("Form words", "Staatsangehörigkeit: nationality. ledig: single. verheiratet: married.")),
                    Beat(40, light = setOf("form"), card = BoardCard("Ich bin ledig. Ich bin verheiratet.", "I'm single. I'm married.", phrase = true)),
                ),
            ),
            // 41: "So what should I remember for the Goethe exam?"
            Scene(
                41, "For the Goethe exam",
                DiagramSpec(
                    "What family and numbers mean for the Goethe A1: listening, reading forms, and speaking.",
                    nodes = listOf(
                        DNode("hear", "Listening", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "units first: 45, not 54"),
                        DNode("read", "Reading", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "form words"),
                        DNode("speak", "Speaking", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "the article with the word"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(42, show = setOf("hear", "read", "speak"), card = BoardCard("Units first", "fünfundvierzig is forty-five. Forms: Staatsangehörigkeit, ledig.")),
                    Beat(44, card = BoardCard("Today", "Phone number digit by digit, two ages, count 20 to 30, four family members.")),
                    Beat(45, card = BoardCard("Tschüss! Bis zum nächsten Mal!", "Bye! Until next time!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de2Grammar() = PodcastBoard(
        "de2-grammar",
        listOf(
            // 0: "Welcome back. Today is grammar, the friendly kind: the verb «haben», t..."
            Scene(
                0, "The haben ladder",
                DiagramSpec(
                    "haben has six forms: habe, hast, hat on one side, haben, habt, haben on the other.",
                    nodes = listOf(
                        DNode("ich", "ich habe", 0.25f, 0.16f, w = 0.44f, h = 0.22f, sub = "I have"),
                        DNode("du", "du hast", 0.25f, 0.5f, w = 0.44f, h = 0.22f, sub = "you have, a friend"),
                        DNode("er", "er, sie, es hat", 0.25f, 0.84f, w = 0.44f, h = 0.22f, sub = "he, she, it has"),
                        DNode("wir", "wir haben", 0.75f, 0.16f, w = 0.44f, h = 0.22f, tone = 1, sub = "we have"),
                        DNode("ihr", "ihr habt", 0.75f, 0.5f, w = 0.44f, h = 0.22f, tone = 1, sub = "you all have"),
                        DNode("sie", "sie, Sie haben", 0.75f, 0.84f, w = 0.44f, h = 0.22f, tone = 1, sub = "they; polite you"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(1, show = setOf("ich"), light = setOf("ich"), card = BoardCard("Ich habe einen Bruder.", "I have a brother.", phrase = true)),
                    Beat(3, card = BoardCard("Six steps", "I, you, he or she, we, you all, they.")),
                    Beat(4, show = setOf("du", "er"), light = setOf("du", "er"), card = BoardCard("Ich habe. Du hast. Er hat.", "I have. You have. He has, and sie hat: she has.", phrase = true)),
                    Beat(6, light = setOf("du", "er"), card = BoardCard("Listen for the s", "du hast: s and t. er hat, sie hat: just a t.")),
                    Beat(7, light = setOf("du"), card = BoardCard("Hast du Kinder?", "Do you have children? Sie hat zwei Schwestern: she has two sisters.", phrase = true)),
                    Beat(9, show = setOf("wir", "ihr", "sie"), light = setOf("ihr"), card = BoardCard("ihr habt", "The odd one: ihr, you all, a group of friends.")),
                    Beat(10, light = setOf("wir", "ihr"), card = BoardCard("Habt ihr Hunger?", "Are you all hungry? Wir haben Zeit: we have time.", phrase = true)),
                ),
            ),
            // 11: "And they? Is that another new word?"
            Scene(
                11, "sie or Sie",
                DiagramSpec(
                    "sie and Sie sound the same; the verb tells you who is meant.",
                    nodes = listOf(
                        DNode("sound", "one sound", 0.5f, 0.12f, w = 0.6f, kind = NodeKind.PILL, tone = 2),
                        DNode("she", "sie", 0.18f, 0.48f, w = 0.3f, h = 0.2f, tone = 1, sub = "she"),
                        DNode("they", "sie", 0.5f, 0.48f, w = 0.3f, h = 0.2f, tone = 1, sub = "they"),
                        DNode("pol", "Sie", 0.82f, 0.48f, w = 0.3f, h = 0.2f, sub = "polite you"),
                        DNode("n1", "hat", 0.18f, 0.84f, w = 0.3f, kind = NodeKind.NOTE, tone = 2),
                        DNode("n2", "haben", 0.66f, 0.84f, w = 0.6f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("sound", "she"),
                        DEdge("sound", "they"),
                        DEdge("sound", "pol"),
                        DEdge("she", "n1", dashed = true),
                        DEdge("pol", "n2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("sound")),
                    Beat(12, show = setOf("she", "they", "pol", "n1", "n2"), light = setOf("pol"), pulse = "sound" to "pol", card = BoardCard("The verb gives it away", "she: sie hat. They, and the polite you: haben.")),
                    Beat(13, light = setOf("pol", "n2"), pulse = "pol" to "n2", card = BoardCard("Haben Sie einen Bruder?", "Do you have a brother? And: Sie hat einen Bruder, she has a brother.", phrase = true)),
                ),
            ),
            // 14: "And it? Like, it has?"
            Scene(
                14, "Names to little words",
                DiagramSpec(
                    "Swap a name for er, sie or wir; es stands for a das word like das Kind.",
                    nodes = listOf(
                        DNode("kind", "das Kind", 0.27f, 0.12f, w = 0.46f, h = 0.18f, tone = 1),
                        DNode("es", "es", 0.78f, 0.12f, w = 0.3f, kind = NodeKind.PILL),
                        DNode("usaid", "Usaid", 0.27f, 0.38f, w = 0.46f, h = 0.18f, tone = 1),
                        DNode("er", "er", 0.78f, 0.38f, w = 0.3f, kind = NodeKind.PILL),
                        DNode("sanya", "Sanya", 0.27f, 0.64f, w = 0.46f, h = 0.18f, tone = 1),
                        DNode("sie", "sie", 0.78f, 0.64f, w = 0.3f, kind = NodeKind.PILL),
                        DNode("us", "you and Usaid", 0.27f, 0.89f, w = 0.46f, h = 0.18f, tone = 1),
                        DNode("wir", "wir", 0.78f, 0.89f, w = 0.3f, kind = NodeKind.PILL),
                    ),
                    edges = listOf(
                        DEdge("kind", "es"),
                        DEdge("usaid", "er"),
                        DEdge("sanya", "sie"),
                        DEdge("us", "wir"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("kind", "es"), light = setOf("es")),
                    Beat(15, show = setOf("usaid", "er", "sanya", "sie", "us", "wir"), light = setOf("kind", "es"), pulse = "kind" to "es", card = BoardCard("es for das words", "das Kind is es, even a little boy. Usaid: er. Sanya: sie. You and Usaid: wir.")),
                    Beat(16, light = setOf("usaid", "er"), pulse = "usaid" to "er", card = BoardCard("Usaid hat Zeit. Er hat Zeit.", "Usaid has time. He has time.", phrase = true)),
                ),
            ),
            // 17: "Got it. Now the my and your words."
            Scene(
                17, "mein, dein, Ihr",
                DiagramSpec(
                    "A Possessivartikel matches the noun owned: no ending before der and das, an e before die and plurals.",
                    nodes = listOf(
                        DNode("h1", "der, das", 0.25f, 0.09f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                        DNode("h2", "die, plural", 0.75f, 0.09f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                        DNode("m1", "mein Vater", 0.25f, 0.36f, w = 0.44f, h = 0.18f, sub = "mein Kind"),
                        DNode("m2", "meine Mutter", 0.75f, 0.36f, w = 0.44f, h = 0.18f, sub = "meine Eltern"),
                        DNode("d1", "dein Bruder", 0.25f, 0.62f, w = 0.44f, h = 0.18f, tone = 1, sub = "your, a friend"),
                        DNode("d2", "deine Schwester", 0.75f, 0.62f, w = 0.44f, h = 0.18f, tone = 1, sub = "your, a friend"),
                        DNode("i1", "Ihr Mann", 0.25f, 0.88f, w = 0.44f, h = 0.18f, tone = 1, sub = "your, polite"),
                        DNode("i2", "Ihre Frau", 0.75f, 0.88f, w = 0.44f, h = 0.18f, tone = 1, sub = "your, polite"),
                    ),
                    edges = listOf(
                        DEdge("h1", "m1"),
                        DEdge("h2", "m2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("h1", "h2"), light = setOf("h1", "h2"), card = BoardCard("Possessivartikel", "Whose is it? The word dresses to match the noun, not the owner.")),
                    Beat(19, show = setOf("m1", "m2"), light = setOf("m1", "m2"), pulse = "h2" to "m2", card = BoardCard("Mein Vater. Meine Mutter.", "My father. My mother. Mein Kind, meine Eltern: my child, my parents.", phrase = true)),
                    Beat(21, show = setOf("d1", "d2", "i1", "i2"), light = setOf("d1", "d2", "i1", "i2"), card = BoardCard("dein, deine, Ihr, Ihre", "Swap m for d for a friend. A capital Ihr for the polite you.")),
                    Beat(22, light = setOf("d1", "d2", "i2"), card = BoardCard("Dein Bruder. Deine Schwester.", "Your brother. Your sister. And Ihre Frau: your wife, polite.", phrase = true)),
                ),
            ),
            // 23: "What about his and her? That's where I'd mess up."
            Scene(
                23, "His and her",
                DiagramSpec(
                    "Pick the ending from the owned noun, not the owner: seine because Schwester is die.",
                    nodes = listOf(
                        DNode("o1", "Usaid (he)", 0.22f, 0.14f, w = 0.4f, h = 0.2f, tone = 2, sub = "sein"),
                        DNode("n1", "die Schwester", 0.76f, 0.14f, w = 0.44f, h = 0.2f, sub = "needs an e"),
                        DNode("r1", "seine Schwester", 0.5f, 0.38f, w = 0.56f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("o2", "Sanya (she)", 0.22f, 0.62f, w = 0.4f, h = 0.2f, tone = 2, sub = "ihr"),
                        DNode("n2", "der Bruder", 0.76f, 0.62f, w = 0.44f, h = 0.2f, sub = "no e"),
                        DNode("r2", "ihr Bruder", 0.5f, 0.86f, w = 0.56f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("o1", "r1", dashed = true),
                        DEdge("n1", "r1", "ending"),
                        DEdge("o2", "r2", dashed = true),
                        DEdge("n2", "r2", "ending"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("o1", "n1", "r1", "o2", "n2", "r2"), light = setOf("n1", "r1"), pulse = "n1" to "r1", card = BoardCard("Look at the noun", "Schwester is die, so seine, though Usaid is a he. Bruder is der: ihr Bruder.")),
                    Beat(25, light = setOf("r1", "r2"), pulse = "n2" to "r2", card = BoardCard("Das ist seine Schwester.", "That's his sister. Das ist ihr Bruder: that's her brother.", phrase = true)),
                    Beat(26, card = BoardCard("unser, unsere", "Same pattern: unser Haus, our house. unsere Familie, our family.")),
                ),
            ),
            // 27: "«Unser», same pattern: «unser Haus», «unsere Familie». Now the last pi..."
            Scene(
                27, "Where the verb goes",
                DiagramSpec(
                    "A statement puts the verb second; a yes or no question pulls it to the front; a W-Frage keeps it second.",
                    nodes = listOf(
                        DNode("s1", "Du", 0.18f, 0.14f, w = 0.28f, h = 0.2f, tone = 1),
                        DNode("s2", "hast", 0.5f, 0.14f, w = 0.28f, h = 0.2f, sub = "verb second"),
                        DNode("s3", "Kinder.", 0.82f, 0.14f, w = 0.28f, h = 0.2f, tone = 1),
                        DNode("q1", "Hast", 0.18f, 0.48f, w = 0.28f, h = 0.2f, sub = "verb first"),
                        DNode("q2", "du", 0.5f, 0.48f, w = 0.28f, h = 0.2f, tone = 1),
                        DNode("q3", "Kinder?", 0.82f, 0.48f, w = 0.28f, h = 0.2f, tone = 1),
                        DNode("w1", "Wie alt", 0.18f, 0.82f, w = 0.28f, h = 0.2f, tone = 2, sub = "w word"),
                        DNode("w2", "bist", 0.5f, 0.82f, w = 0.28f, h = 0.2f, sub = "verb second"),
                        DNode("w3", "du?", 0.82f, 0.82f, w = 0.28f, h = 0.2f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("s2", "q1", "to the front"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("s1", "s2", "s3"), light = setOf("s2"), card = BoardCard("Verb second", "In a normal sentence, the verb sits in second place.")),
                    Beat(28, show = setOf("q1", "q2", "q3"), light = setOf("q1"), pulse = "s2" to "q1", card = BoardCard("Hast du Kinder?", "Do you have children? Du hast Kinder: you have children.", phrase = true)),
                    Beat(30, show = setOf("w1", "w2", "w3"), light = setOf("w1", "w2"), card = BoardCard("W-Frage", "A w word first, and the verb stays second: wie, wo, woher.")),
                    Beat(31, light = setOf("w2"), card = BoardCard("Wie alt bist du?", "How old are you? Hast du Geschwister? Do you have siblings?", phrase = true)),
                ),
            ),
            // 32: "So what should I remember for the Goethe exam?"
            Scene(
                32, "For the Goethe exam",
                DiagramSpec(
                    "What haben, pronouns and possessives mean for the Goethe A1: speaking, reading and writing, listening.",
                    nodes = listOf(
                        DNode("speak", "Speaking", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "yes or no: verb first"),
                        DNode("rw", "Reading, writing", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "ending follows the noun"),
                        DNode("hear", "Listening", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "sie or Sie? the verb tells"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("speak", "rw", "hear"), card = BoardCard("Hast du Geschwister?", "A yes or no question starts with the verb. meine Mutter, ihr Bruder.")),
                    Beat(35, card = BoardCard("Today", "mein and dein for four family members, haben in six persons, names to er, sie, wir.")),
                    Beat(36, card = BoardCard("Tschüss! Bis bald!", "Bye! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de2Dialogue() = PodcastBoard(
        "de2-dialogue",
        listOf(
            // 0: "Hi, and welcome back. Today is a real conversation about family. Usaid..."
            Scene(
                0, "In the office kitchen",
                DiagramSpec(
                    "Usaid and his new colleague Lena chat over coffee; in this office, colleagues say du.",
                    nodes = listOf(
                        DNode("lena", "Lena", 0.2f, 0.3f, h = 0.28f, kind = NodeKind.USER, tone = 1),
                        DNode("usaid", "Usaid", 0.8f, 0.3f, h = 0.28f, kind = NodeKind.USER),
                        DNode("note", "Colleagues here: du", 0.5f, 0.78f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "friendly, not Sie"),
                    ),
                    edges = listOf(
                        DEdge("lena", "usaid", "du"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("lena", "usaid")),
                    Beat(2, show = setOf("note"), light = setOf("note"), pulse = "lena" to "usaid", card = BoardCard("Colleagues: du", "This office is friendly, so Lena and Usaid say du.")),
                    Beat(3, light = setOf("lena"), card = BoardCard("Usaid, bist du verheiratet?", "Usaid, are you married?", phrase = true)),
                ),
            ),
            // 4: "That's direct. What does Usaid say? He's getting married soon."
            Scene(
                4, "Not yet",
                DiagramSpec(
                    "noch nicht means true now, and not for much longer: the wedding is in February.",
                    nodes = listOf(
                        DNode("now", "noch nicht", 0.24f, 0.2f, w = 0.42f, h = 0.22f, tone = 1, sub = "now: not yet"),
                        DNode("feb", "im Februar", 0.76f, 0.2f, w = 0.42f, h = 0.22f, sub = "heiraten, to marry"),
                        DNode("vf", "die Verlobte", 0.26f, 0.66f, w = 0.46f, h = 0.22f, sub = "fiancée"),
                        DNode("vm", "Verlobter", 0.76f, 0.66f, w = 0.42f, h = 0.22f, tone = 1, sub = "fiancé"),
                    ),
                    edges = listOf(
                        DEdge("now", "feb", "soon"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("now"), light = setOf("now"), card = BoardCard("noch nicht", "Not yet: kinder than a bare yes or no, and it leaves the story open.")),
                    Beat(6, show = setOf("feb"), light = setOf("feb"), pulse = "now" to "feb", card = BoardCard("Noch nicht. Ich heirate im Februar.", "Not yet. I'm getting married in February.", phrase = true)),
                    Beat(8, show = setOf("vf", "vm"), light = setOf("vf"), card = BoardCard("heiraten", "To marry. die Verlobte: fiancée. Verlobter: fiancé.")),
                    Beat(9, light = setOf("vf"), card = BoardCard("Meine Verlobte heißt Sanya.", "My fiancée is called Sanya.", phrase = true)),
                ),
            ),
            // 10: "Nice. And Lena wants to know more, I bet."
            Scene(
                10, "About Sanya",
                DiagramSpec(
                    "Lena's questions and Usaid's answers: where Sanya is from, how old she is, her job.",
                    nodes = listOf(
                        DNode("q1", "Woher?", 0.22f, 0.15f, w = 0.36f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                        DNode("a1", "aus Pakistan", 0.72f, 0.15f, w = 0.48f, h = 0.2f, sub = "from Pakistan"),
                        DNode("q2", "Wie alt?", 0.22f, 0.5f, w = 0.36f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                        DNode("a2", "vierundzwanzig", 0.72f, 0.5f, w = 0.48f, h = 0.2f, sub = "24: four and twenty"),
                        DNode("q3", "Job?", 0.22f, 0.85f, w = 0.36f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                        DNode("a3", "Lehrerin", 0.72f, 0.85f, w = 0.48f, h = 0.2f, sub = "a woman teacher"),
                    ),
                    edges = listOf(
                        DEdge("q1", "a1"),
                        DEdge("q2", "a2"),
                        DEdge("q3", "a3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("q1"), light = setOf("q1"), card = BoardCard("Woher?", "From where? The her at the end turns where into from where.")),
                    Beat(12, light = setOf("q1"), card = BoardCard("Schön! Woher kommt sie?", "Nice! Where is she from?", phrase = true)),
                    Beat(14, show = setOf("a1", "q2", "a2"), light = setOf("q2", "a2"), pulse = "q2" to "a2", card = BoardCard("Units first", "Twenty-four: vier und zwanzig, four and twenty.")),
                    Beat(15, show = setOf("q3", "a3"), light = setOf("a1", "a2", "a3"), card = BoardCard("Sie kommt aus Pakistan.", "She's from Pakistan. Sie ist vierundzwanzig Jahre alt und Lehrerin: 24, a teacher.", phrase = true)),
                    Beat(17, light = setOf("a3"), card = BoardCard("Lehrerin", "The in at the end makes it a woman: a woman teacher.")),
                ),
            ),
            // 18: "Hast du Geschwister?"
            Scene(
                18, "Hand it back",
                DiagramSpec(
                    "Answer, then hand the same question back with Und du? and the conversation keeps going.",
                    nodes = listOf(
                        DNode("lena", "Lena", 0.2f, 0.18f, h = 0.28f, kind = NodeKind.USER, tone = 1),
                        DNode("usaid", "Usaid", 0.8f, 0.18f, h = 0.28f, kind = NodeKind.USER),
                        DNode("q", "Hast du Geschwister?", 0.5f, 0.48f, w = 0.76f, tone = 1),
                        DNode("ans", "Ja, einen Bruder", 0.5f, 0.68f, w = 0.76f, tone = 1),
                        DNode("back", "Und du?", 0.5f, 0.88f, w = 0.44f, kind = NodeKind.PILL),
                    ),
                    edges = listOf(
                        DEdge("lena", "usaid"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("lena", "usaid", "q"), light = setOf("q"), pulse = "lena" to "usaid", card = BoardCard("Hast du Geschwister?", "Do you have siblings? Brothers and sisters together.", phrase = true)),
                    Beat(21, show = setOf("ans", "back"), light = setOf("back"), pulse = "usaid" to "lena", card = BoardCard("Ja, ich habe einen Bruder.", "Yes, I have a brother. Er wohnt in Indien: he lives in India. Und du? And you?", phrase = true)),
                    Beat(23, light = setOf("back"), card = BoardCard("Und du?", "Two words keep the chat alive, and the speaking exam rewards them.")),
                ),
            ),
            // 24: "Ich habe zwei Schwestern. Meine Eltern wohnen in Hamburg."
            Scene(
                24, "Lena's family",
                DiagramSpec(
                    "meine Eltern and deine Schwestern take an e because they are plural; ages read units first.",
                    nodes = listOf(
                        DNode("s", "zwei Schwestern", 0.26f, 0.12f, w = 0.48f, h = 0.2f, tone = 1, sub = "two sisters"),
                        DNode("e", "meine Eltern", 0.76f, 0.12f, w = 0.44f, h = 0.2f, tone = 1, sub = "in Hamburg"),
                        DNode("d", "deine Schwestern", 0.5f, 0.42f, w = 0.6f, h = 0.18f, sub = "your, plural"),
                        DNode("drei", "drei", 0.18f, 0.78f, w = 0.3f, h = 0.22f, sub = "3, first"),
                        DNode("und", "und", 0.5f, 0.78f, w = 0.22f, h = 0.22f, tone = 2),
                        DNode("zw", "zwanzig", 0.82f, 0.78f, w = 0.3f, h = 0.22f, tone = 1, sub = "20, last"),
                    ),
                    edges = listOf(
                        DEdge("drei", "und"),
                        DEdge("und", "zw"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("s", "e"), light = setOf("s", "e"), card = BoardCard("Ich habe zwei Schwestern.", "I have two sisters. Meine Eltern wohnen in Hamburg: my parents live in Hamburg.", phrase = true)),
                    Beat(26, show = setOf("d"), light = setOf("e", "d"), card = BoardCard("A plural takes meine", "meine Eltern, deine Schwestern: just like a die word.")),
                    Beat(27, light = setOf("d"), card = BoardCard("Wie alt sind deine Schwestern?", "How old are your sisters?", phrase = true)),
                    Beat(29, show = setOf("drei", "und", "zw"), light = setOf("drei"), pulse = "drei" to "und"),
                    Beat(30, light = setOf("drei", "und", "zw"), pulse = "und" to "zw", card = BoardCard("Dreiundzwanzig und dreißig.", "Twenty-three and thirty.", phrase = true)),
                ),
            ),
            // 31: "So I might hear «drei» and think thirteen?"
            Scene(
                31, "Three question words",
                DiagramSpec(
                    "Three question words carry most A1 family talk: age, origin and place.",
                    nodes = listOf(
                        DNode("wie", "Wie alt?", 0.18f, 0.3f, w = 0.3f, h = 0.3f, sub = "age"),
                        DNode("woher", "Woher?", 0.5f, 0.3f, w = 0.3f, h = 0.3f, tone = 1, sub = "origin"),
                        DNode("wo", "Wo?", 0.82f, 0.3f, w = 0.3f, h = 0.3f, tone = 2, sub = "place"),
                        DNode("trap", "dreizehn 13, dreiundzwanzig 23", 0.5f, 0.8f, w = 0.9f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("trap"), light = setOf("trap")),
                    Beat(32, show = setOf("wie", "woher", "wo"), light = setOf("wie", "woher", "wo"), card = BoardCard("und means add", "dreizehn is 13. dreiundzwanzig, with und, is 23.")),
                    Beat(33, light = setOf("wo"), card = BoardCard("Wo wohnen deine Eltern?", "Where do your parents live?", phrase = true)),
                ),
            ),
            // 34: "So what should I remember for the Goethe exam?"
            Scene(
                34, "For the Goethe exam",
                DiagramSpec(
                    "What a family chat means for the Goethe A1: speaking, listening, and the question words.",
                    nodes = listOf(
                        DNode("speak", "Speaking", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "answer, then Und du?"),
                        DNode("hear", "Listening", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "ages: units first"),
                        DNode("qw", "woher or wo?", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "from where, or where"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(35, show = setOf("speak", "hear", "qw"), card = BoardCard("Und du?", "Answer, then hand it back. Hear und: add the tens.")),
                    Beat(37, card = BoardCard("Today", "Both sides: Hast du Geschwister?, two ages, Und du?, where your parents live.")),
                    Beat(38, card = BoardCard("Tschüss! Bis morgen!", "Bye! See you tomorrow!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de2Practice() = PodcastBoard(
        "de2-practice",
        listOf(
            // 0: "Welcome back. Today is practice: a family profile. First we read a sho..."
            Scene(
                0, "Reading part one",
                DiagramSpec(
                    "Lesen part 1: a short text, then true or false lines. Read everything first.",
                    nodes = listOf(
                        DNode("text", "Text", 0.25f, 0.18f, w = 0.42f, h = 0.24f, tone = 2, sub = "5 to 8 lines"),
                        DNode("stmt", "Statement", 0.75f, 0.18f, w = 0.42f, h = 0.24f, sub = "richtig or falsch"),
                        DNode("check", "Check", 0.75f, 0.56f, w = 0.42f, h = 0.24f, sub = "find the line"),
                        DNode("ans", "Answer", 0.25f, 0.56f, w = 0.42f, h = 0.24f, tone = 2, sub = "tick one"),
                        DNode("note", "Read the whole text first", 0.5f, 0.88f, w = 0.86f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("text", "stmt"),
                        DEdge("stmt", "check"),
                        DEdge("check", "ans"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("text")),
                    Beat(2, show = setOf("stmt", "check", "ans", "note"), light = setOf("note"), pulse = "stmt" to "check", card = BoardCard("Richtig oder falsch", "Read the whole text first, then find the line for each statement.")),
                ),
            ),
            // 3: "Ich heiße Sanya und bin vierundzwanzig Jahre alt. Ich komme aus Pakist..."
            Scene(
                3, "Sanya's text",
                DiagramSpec(
                    "Sanya's text in six facts: who she is, her family, Usaid, and their languages.",
                    nodes = listOf(
                        DNode("name", "Sanya, 24", 0.25f, 0.15f, w = 0.44f, h = 0.24f, tone = 1, sub = "aus Pakistan"),
                        DNode("city", "Karachi", 0.75f, 0.15f, w = 0.44f, h = 0.24f, tone = 1, sub = "Lehrerin"),
                        DNode("fam", "Familie: groß", 0.25f, 0.5f, w = 0.44f, h = 0.24f, sub = "2 sisters, 1 brother"),
                        DNode("par", "die Eltern", 0.75f, 0.5f, w = 0.44f, h = 0.24f, sub = "auch in Karachi"),
                        DNode("fian", "Usaid", 0.25f, 0.85f, w = 0.44f, h = 0.24f, tone = 1, sub = "wohnt in Doha"),
                        DNode("lang", "Englisch, Urdu", 0.75f, 0.85f, w = 0.44f, h = 0.24f, tone = 1, sub = "lernen Deutsch"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("name", "city"), light = setOf("name", "city"), card = BoardCard("Ich heiße Sanya.", "I'm Sanya: twenty-four, from Pakistan, I live in Karachi and I'm a teacher.", phrase = true)),
                    Beat(6, show = setOf("fam", "par"), light = setOf("fam", "par"), card = BoardCard("Meine Familie ist groß.", "My family is big. Two sisters and a brother; my parents live in Karachi too.", phrase = true)),
                    Beat(8, light = setOf("par"), card = BoardCard("auch, groß, klein", "auch: also, too. groß: big. klein: small.")),
                    Beat(9, show = setOf("fian", "lang"), light = setOf("fian", "lang"), card = BoardCard("Wir lernen Deutsch.", "We're learning German. Usaid lives in Doha; they speak English and Urdu.", phrase = true)),
                ),
            ),
            // 10: "Okay, I'm ready. Test me."
            Scene(
                10, "Richtig oder falsch",
                DiagramSpec(
                    "Each statement gets its answer from one line of the text, not from memory.",
                    nodes = listOf(
                        DNode("s1", "Sanya hat einen Bruder.", 0.35f, 0.15f, w = 0.66f, h = 0.2f, tone = 1),
                        DNode("r1", "richtig", 0.85f, 0.15f, kind = NodeKind.PILL),
                        DNode("s2", "Usaid wohnt in Karachi.", 0.35f, 0.5f, w = 0.66f, h = 0.2f, tone = 1),
                        DNode("r2", "falsch", 0.85f, 0.5f, kind = NodeKind.PILL),
                        DNode("s3", "Sie sprechen nur Deutsch.", 0.35f, 0.85f, w = 0.66f, h = 0.2f, tone = 1),
                        DNode("r3", "falsch", 0.85f, 0.85f, kind = NodeKind.PILL),
                    ),
                    edges = listOf(
                        DEdge("s1", "r1"),
                        DEdge("s2", "r2"),
                        DEdge("s3", "r3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("s1"), light = setOf("s1"), card = BoardCard("Sanya hat einen Bruder.", "Sanya has a brother. True or false?", phrase = true)),
                    Beat(14, show = setOf("r1"), light = setOf("r1"), pulse = "s1" to "r1", card = BoardCard("Find the line", "zwei Schwestern und einen Bruder: richtig.")),
                    Beat(15, show = setOf("s2"), light = setOf("s2"), card = BoardCard("Usaid wohnt in Karachi.", "Usaid lives in Karachi. True or false?", phrase = true)),
                    Beat(17, show = setOf("r2"), light = setOf("r2"), pulse = "s2" to "r2", card = BoardCard("The word is there, but", "Er wohnt in Doha. Karachi is Sanya's city, so: falsch.")),
                    Beat(18, show = setOf("s3"), light = setOf("s3"), card = BoardCard("Sie sprechen nur Deutsch.", "Sanya and Usaid speak only German. True or false?", phrase = true)),
                    Beat(19, show = setOf("r3"), light = setOf("r3"), pulse = "s3" to "r3", card = BoardCard("nur: only", "They speak English and Urdu and are learning German: falsch.")),
                    Beat(21, card = BoardCard("Zwei Schwestern und einen Bruder", "Ich habe zwei Schwestern und einen Bruder: I have two sisters and a brother.", phrase = true)),
                ),
            ),
            // 22: "And my own profile? Where do I start?"
            Scene(
                22, "Five lines about a family",
                DiagramSpec(
                    "Usaid's model: size first, then who, an age, a job, and where they live.",
                    nodes = listOf(
                        DNode("n1", "Meine Familie ist klein.", 0.5f, 0.1f, w = 0.84f, h = 0.17f, sub = "size first"),
                        DNode("n2", "Ich habe einen Bruder.", 0.5f, 0.3f, w = 0.84f, h = 0.17f, tone = 1, sub = "who, with haben"),
                        DNode("n3", "Er ist 31 Jahre alt.", 0.5f, 0.5f, w = 0.84f, h = 0.17f, tone = 1, sub = "an age"),
                        DNode("n4", "Er ist Ingenieur.", 0.5f, 0.7f, w = 0.84f, h = 0.17f, tone = 1, sub = "a job"),
                        DNode("n5", "Meine Eltern wohnen in Indien.", 0.5f, 0.9f, w = 0.84f, h = 0.17f, tone = 1, sub = "where they live"),
                    ),
                    edges = listOf(
                        DEdge("n1", "n2"),
                        DEdge("n2", "n3"),
                        DEdge("n3", "n4"),
                        DEdge("n4", "n5"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("n1"), light = setOf("n1"), card = BoardCard("Writing: five sentences", "Usaid's model: size, who, an age, a job, where they live.")),
                    Beat(24, light = setOf("n1"), card = BoardCard("Meine Familie ist klein.", "My family is small.", phrase = true)),
                    Beat(26, show = setOf("n2"), light = setOf("n2"), pulse = "n1" to "n2"),
                    Beat(27, show = setOf("n3"), light = setOf("n3"), pulse = "n2" to "n3", card = BoardCard("Er ist einunddreißig Jahre alt.", "Ich habe einen Bruder: I have a brother. He is thirty-one years old.", phrase = true)),
                    Beat(29, show = setOf("n4", "n5"), light = setOf("n4", "n5"), pulse = "n3" to "n4"),
                    Beat(30, light = setOf("n4", "n5"), card = BoardCard("Er ist Ingenieur.", "He's an engineer. Meine Eltern wohnen in Indien: my parents live in India.", phrase = true)),
                ),
            ),
            // 31: "That's five. Can I just copy that with my own family?"
            Scene(
                31, "Lena's turn",
                DiagramSpec(
                    "Same staircase for Lena: a big family, an age said units first, and ihre for her parents.",
                    nodes = listOf(
                        DNode("l1", "Meine Familie ist groß.", 0.5f, 0.1f, w = 0.84f, h = 0.17f, tone = 1, sub = "size"),
                        DNode("l2", "Ich habe zwei Schwestern.", 0.5f, 0.31f, w = 0.84f, h = 0.17f, tone = 1, sub = "who"),
                        DNode("l3", "dreiundzwanzig Jahre alt", 0.5f, 0.52f, w = 0.84f, h = 0.17f, sub = "an age, units first"),
                        DNode("l4", "Ihre Eltern wohnen in Hamburg.", 0.5f, 0.73f, w = 0.84f, h = 0.17f, sub = "her: ihre, plural"),
                        DNode("note", "Write numbers as words", 0.5f, 0.92f, w = 0.84f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("l1", "l2"),
                        DEdge("l2", "l3"),
                        DEdge("l3", "l4"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("l1"), light = setOf("l1"), card = BoardCard("Lena's turn", "A big family: two sisters, twenty-three and thirty, parents in Hamburg.")),
                    Beat(33, show = setOf("l2"), light = setOf("l2"), pulse = "l1" to "l2", card = BoardCard("Meine Familie ist groß.", "My family is big. Ich habe zwei Schwestern: I have two sisters.", phrase = true)),
                    Beat(35, show = setOf("l3", "note"), light = setOf("note"), pulse = "l2" to "l3"),
                    Beat(36, light = setOf("l3"), card = BoardCard("Dreiundzwanzig Jahre alt.", "Eine Schwester ist dreiundzwanzig Jahre alt: one sister is twenty-three.", phrase = true)),
                    Beat(38, show = setOf("l4"), light = setOf("l4"), pulse = "l3" to "l4", card = BoardCard("ihre, with an e", "Eltern is plural, so ihre. The ending follows the thing owned.")),
                    Beat(39, light = setOf("l4"), card = BoardCard("Ihre Eltern wohnen in Hamburg.", "Her parents live in Hamburg.", phrase = true)),
                ),
            ),
            // 40: "So what should I remember for the Goethe exam?"
            Scene(
                40, "For the Goethe exam",
                DiagramSpec(
                    "What a family profile means for the Goethe A1: reading part one, writing, and numbers.",
                    nodes = listOf(
                        DNode("read", "Reading", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "whole text first; nur"),
                        DNode("write", "Writing", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "every point scores"),
                        DNode("num", "Numbers", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "practise them out loud"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(41, show = setOf("read", "write", "num"), card = BoardCard("Find the line", "Read the whole text, then find the line. Cover every writing point.")),
                    Beat(43, card = BoardCard("Today", "Your phone number digit by digit, then in pairs. Your age, your parents' city.")),
                    Beat(44, card = BoardCard("Tschüss! Bis zum nächsten Mal!", "Bye! Until next time!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de3Words() = PodcastBoard(
        "de3-words",
        listOf(
            // 0: "Welcome back. Today is days and times: the seven days of the week, how..."
            Scene(
                0, "The seven days",
                DiagramSpec(
                    "Every day of the week is der, and on a day you say am: am Montag.",
                    nodes = listOf(
                        DNode("mo", "der Montag", 0.26f, 0.12f, w = 0.46f, tone = 1, sub = "Monday"),
                        DNode("di", "der Dienstag", 0.26f, 0.37f, w = 0.46f, sub = "Tuesday"),
                        DNode("mi", "der Mittwoch", 0.26f, 0.62f, w = 0.46f, tone = 1, sub = "Wednesday"),
                        DNode("do", "der Donnerstag", 0.26f, 0.87f, w = 0.46f, sub = "Thursday"),
                        DNode("fr", "der Freitag", 0.76f, 0.12f, w = 0.44f, tone = 1, sub = "Friday"),
                        DNode("sa", "der Samstag", 0.76f, 0.37f, w = 0.44f, tone = 1, sub = "Saturday"),
                        DNode("so", "der Sonntag", 0.76f, 0.62f, w = 0.44f, tone = 1, sub = "Sunday"),
                        DNode("am", "am Montag", 0.76f, 0.87f, w = 0.44f, kind = NodeKind.NOTE, tone = 2, sub = "on Monday"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(1, show = setOf("mo", "di", "mi", "do"), card = BoardCard("Montag, Dienstag, Mittwoch", "Monday, Tuesday, Wednesday, and Donnerstag, Thursday.", phrase = true)),
                    Beat(3, light = setOf("di", "do"), card = BoardCard("Dienstag or Donnerstag?", "Dienstag, the short one, is Tuesday. Donnerstag, the long one, is Thursday.")),
                    Beat(4, show = setOf("fr", "sa", "so"), light = setOf("fr", "sa", "so"), card = BoardCard("Freitag, Samstag, Sonntag.", "Friday, Saturday, Sunday.", phrase = true)),
                    Beat(6, show = setOf("am"), light = setOf("am"), card = BoardCard("Every day is der", "der Montag, der Freitag. On a day: am.")),
                    Beat(7, light = setOf("mo", "am"), card = BoardCard("Am Montag arbeite ich.", "On Monday I work.", phrase = true)),
                ),
            ),
            // 8: "Usaid lives in Doha, though. Isn't the weekend different there?"
            Scene(
                8, "A week in Qatar",
                DiagramSpec(
                    "In Qatar the work week runs Sunday to Thursday; Friday is the day off.",
                    nodes = listOf(
                        DNode("u", "Usaid", 0.5f, 0.16f, w = 0.22f, h = 0.22f, kind = NodeKind.USER),
                        DNode("work", "Sonntag bis Donnerstag", 0.26f, 0.6f, w = 0.46f, h = 0.26f, tone = 1, sub = "Wochentage: work"),
                        DNode("off", "das Wochenende", 0.76f, 0.6f, w = 0.44f, h = 0.26f, sub = "Freitag, Samstag"),
                    ),
                    edges = listOf(
                        DEdge("u", "work"),
                        DEdge("u", "off"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("u")),
                    Beat(9, show = setOf("work", "off"), light = setOf("off"), pulse = "u" to "off", card = BoardCard("das Wochenende", "The weekend. A weekday is a Wochentag.")),
                    Beat(10, light = setOf("work"), pulse = "u" to "work", card = BoardCard("Freitag ist in Katar frei.", "Friday is off in Qatar. Am Sonntag arbeite ich wieder: on Sunday I work again.", phrase = true)),
                ),
            ),
            // 11: "Now the clock. How do I say it's eight o'clock?"
            Scene(
                11, "Telling the time",
                DiagramSpec(
                    "Uhr after the number; Viertel nach after the hour, Viertel vor before the next one.",
                    nodes = listOf(
                        DNode("t0", "acht Uhr", 0.5f, 0.1f, w = 0.6f, h = 0.15f, sub = "8:00"),
                        DNode("t1", "Viertel nach acht", 0.5f, 0.3f, w = 0.6f, h = 0.15f, tone = 1, sub = "8:15"),
                        DNode("t2", "halb neun", 0.5f, 0.5f, w = 0.6f, h = 0.15f, tone = 2, sub = "8:30"),
                        DNode("t3", "Viertel vor neun", 0.5f, 0.7f, w = 0.6f, h = 0.15f, tone = 1, sub = "8:45"),
                        DNode("t4", "neun Uhr", 0.5f, 0.9f, w = 0.6f, h = 0.15f, sub = "9:00"),
                    ),
                    edges = listOf(
                        DEdge("t0", "t1"),
                        DEdge("t1", "t2"),
                        DEdge("t2", "t3"),
                        DEdge("t3", "t4"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("t0", "t4"), light = setOf("t0")),
                    Beat(13, light = setOf("t0"), card = BoardCard("Es ist acht Uhr.", "It is eight o'clock.", phrase = true)),
                    Beat(15, show = setOf("t1", "t3"), light = setOf("t1", "t3")),
                    Beat(16, show = setOf("t2"), light = setOf("t1", "t3"), card = BoardCard("Viertel nach acht. Viertel vor neun.", "Quarter past eight. Quarter to nine.", phrase = true)),
                ),
            ),
            // 17: "Easy. So half past eight is «halb acht»?"
            Scene(
                17, "halb: half-way to",
                DiagramSpec(
                    "halb points at the coming hour: halb neun is 8:30, half-way to nine.",
                    nodes = listOf(
                        DNode("eight", "8:00", 0.15f, 0.4f, w = 0.22f, h = 0.22f, tone = 2),
                        DNode("half", "halb neun", 0.5f, 0.4f, w = 0.32f, h = 0.26f, sub = "8:30"),
                        DNode("nine", "9:00", 0.85f, 0.4f, w = 0.22f, h = 0.22f, tone = 2),
                        DNode("hv", "halb vier", 0.5f, 0.8f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 1, sub = "3:30, not 4:30"),
                    ),
                    edges = listOf(
                        DEdge("eight", "half", dashed = true),
                        DEdge("half", "nine", "half-way to"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("eight", "nine")),
                    Beat(18, show = setOf("half"), light = setOf("half", "nine"), pulse = "half" to "nine", card = BoardCard("halb + the coming hour", "halb neun is 8:30, half-way to nine. Not half past nine.")),
                    Beat(19, light = setOf("half"), card = BoardCard("Es ist halb neun.", "It is half past eight: 8:30.", phrase = true)),
                    Beat(20, show = setOf("hv"), light = setOf("hv"), card = BoardCard("halb vier", "3:30, half-way to four.")),
                ),
            ),
            // 21: "Exactly: half-way to four. Now, there's a second way to tell the time...."
            Scene(
                21, "Two clocks",
                DiagramSpec(
                    "Announcements use the official 24-hour clock; friends use the everyday form. um for times, am for days.",
                    nodes = listOf(
                        DNode("off", "Official", 0.26f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "trains, radio, exam"),
                        DNode("evd", "Everyday", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, tone = 1, sub = "friends"),
                        DNode("o1", "sechzehn Uhr fünfzehn", 0.26f, 0.5f, w = 0.46f, h = 0.22f, sub = "16:15"),
                        DNode("e1", "Viertel nach vier", 0.76f, 0.5f, w = 0.44f, h = 0.22f, tone = 1, sub = "16:15"),
                        DNode("am", "am + day", 0.26f, 0.85f, w = 0.4f, kind = NodeKind.NOTE, tone = 2, sub = "am Montag"),
                        DNode("um", "um + clock time", 0.76f, 0.85f, w = 0.44f, kind = NodeKind.NOTE, tone = 2, sub = "um acht"),
                    ),
                    edges = listOf(
                        DEdge("off", "o1"),
                        DEdge("evd", "e1"),
                        DEdge("o1", "e1", "same moment", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("off", "evd"), light = setOf("off")),
                    Beat(22, show = setOf("o1"), light = setOf("o1"), pulse = "off" to "o1", card = BoardCard("Der Zug fährt um 16 Uhr 15.", "The train leaves at sixteen fifteen: sechzehn Uhr fünfzehn.", phrase = true)),
                    Beat(24, show = setOf("e1", "am", "um"), light = setOf("o1", "e1"), pulse = "o1" to "e1", card = BoardCard("Same moment, two forms", "Announcements: sechzehn Uhr fünfzehn. Friends: Viertel nach vier.")),
                    Beat(25, light = setOf("am", "um"), card = BoardCard("Am Montag um acht.", "On Monday at eight. And Viertel nach vier: quarter past four.", phrase = true)),
                ),
            ),
            // 26: "What about today and tomorrow?"
            Scene(
                26, "Today, tomorrow, routine",
                DiagramSpec(
                    "morgen with a small m is tomorrow; der Morgen is the morning. Routine words keep their article.",
                    nodes = listOf(
                        DNode("heute", "heute", 0.26f, 0.13f, w = 0.44f, h = 0.18f, tone = 1, sub = "today"),
                        DNode("morgen", "morgen", 0.76f, 0.13f, w = 0.44f, h = 0.18f, sub = "tomorrow"),
                        DNode("dm", "der Morgen", 0.76f, 0.43f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "the morning"),
                        DNode("st", "die Stunde", 0.26f, 0.43f, w = 0.44f, h = 0.18f, tone = 1, sub = "hour"),
                        DNode("fr", "das Frühstück", 0.26f, 0.8f, w = 0.44f, h = 0.2f, tone = 1, sub = "breakfast"),
                        DNode("te", "der Termin", 0.76f, 0.8f, w = 0.44f, h = 0.2f, sub = "appointment"),
                    ),
                    edges = listOf(
                        DEdge("morgen", "dm", "capital M", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("heute", "morgen", "dm"), light = setOf("morgen", "dm"), pulse = "morgen" to "dm", card = BoardCard("morgen or der Morgen?", "Small m: tomorrow. Capital M: the morning.")),
                    Beat(28, light = setOf("heute", "morgen"), card = BoardCard("Heute ist Dienstag. Bis morgen!", "Today is Tuesday. See you tomorrow!", phrase = true)),
                    Beat(30, show = setOf("st", "fr", "te"), light = setOf("fr", "te", "st")),
                    Beat(31, light = setOf("fr", "te"), card = BoardCard("Ich habe einen Termin um zehn.", "I have an appointment at ten. Das Frühstück ist um sieben: breakfast is at seven.", phrase = true)),
                ),
            ),
            // 32: "And the verbs for the day? You've got work. What else?"
            Scene(
                32, "Verbs for the day",
                DiagramSpec(
                    "Four everyday verbs to go with a day and a time: lernen, kommen, gehen, machen.",
                    nodes = listOf(
                        DNode("le", "lernen", 0.26f, 0.2f, w = 0.44f, h = 0.2f, tone = 1, sub = "to learn"),
                        DNode("ko", "kommen", 0.76f, 0.2f, w = 0.44f, h = 0.2f, tone = 1, sub = "to come"),
                        DNode("ge", "gehen", 0.26f, 0.5f, w = 0.44f, h = 0.2f, tone = 1, sub = "to go"),
                        DNode("ma", "machen", 0.76f, 0.5f, w = 0.44f, h = 0.2f, tone = 1, sub = "to do, to make"),
                        DNode("you", "You", 0.5f, 0.84f, w = 0.22f, h = 0.22f, kind = NodeKind.USER),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("le", "ko", "ge", "ma")),
                    Beat(34, light = setOf("ma", "ko"), card = BoardCard("Was machst du heute?", "What are you doing today? Wann kommst du: when are you coming?", phrase = true)),
                    Beat(36, show = setOf("you"), light = setOf("you")),
                    Beat(37, light = setOf("ge", "le"), card = BoardCard("Ich gehe um acht ins Büro.", "I go to the office at eight. Ich lerne eine Stunde: I study for an hour.", phrase = true)),
                ),
            ),
            // 38: "So what should I remember for the Goethe exam?"
            Scene(
                38, "For the Goethe exam",
                DiagramSpec(
                    "Times in the Goethe A1: the 24-hour clock in the listening part, halb, and am or um.",
                    nodes = listOf(
                        DNode("hear", "Listening", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "the 24-hour clock"),
                        DNode("halb", "halb neun", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "8:30, not 9:30"),
                        DNode("amum", "am Montag um acht", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "am for days, um for times"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(39, show = setOf("hear", "halb", "amum"), card = BoardCard("Announcements count to 24", "sechzehn Uhr is four in the afternoon.")),
                    Beat(41, card = BoardCard("Today", "Say the time at every clock: five everyday, one official, halb once. Then heute, morgen.")),
                    Beat(42, card = BoardCard("Bis morgen! Tschüss!", "See you tomorrow! Bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de3Grammar() = PodcastBoard(
        "de3-grammar",
        listOf(
            // 0: "Welcome back. Today is grammar: regular verbs in the present tense, wh..."
            Scene(
                0, "Stem plus ending",
                DiagramSpec(
                    "Cut en off the infinitive to get the stem, then add the ending that matches the person.",
                    nodes = listOf(
                        DNode("verb", "lernen", 0.5f, 0.14f, w = 0.44f, h = 0.2f, tone = 1, sub = "to learn"),
                        DNode("stem", "lern-", 0.26f, 0.56f, w = 0.4f, h = 0.22f, sub = "Verbstamm"),
                        DNode("end", "-e", 0.76f, 0.56f, w = 0.36f, h = 0.22f, tone = 1, sub = "Endung"),
                        DNode("res", "ich lerne", 0.5f, 0.88f, w = 0.44f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("verb", "stem"),
                        DEdge("stem", "end", "plus"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("verb")),
                    Beat(2, show = setOf("stem", "end"), light = setOf("stem", "end"), pulse = "verb" to "stem", card = BoardCard("Verbstamm + Endung", "lern- is the stem; the ending changes with the person.")),
                    Beat(3, show = setOf("res"), light = setOf("res"), card = BoardCard("Ich lerne Deutsch.", "I am learning German.", phrase = true)),
                ),
            ),
            // 4: "So «ich» gets an «e» on the end."
            Scene(
                4, "Six persons, six endings",
                DiagramSpec(
                    "The Präsens endings: e, st, t for one person; en, t, en for more than one.",
                    nodes = listOf(
                        DNode("ich", "ich lerne", 0.26f, 0.15f, w = 0.44f, h = 0.2f, tone = 1, sub = "-e"),
                        DNode("du", "du lernst", 0.26f, 0.48f, w = 0.44f, h = 0.2f, tone = 1, sub = "-st"),
                        DNode("er", "er, sie, es lernt", 0.26f, 0.81f, w = 0.44f, h = 0.2f, tone = 1, sub = "-t"),
                        DNode("wir", "wir lernen", 0.76f, 0.15f, w = 0.44f, h = 0.2f, tone = 2, sub = "-en"),
                        DNode("ihr", "ihr lernt", 0.76f, 0.48f, w = 0.44f, h = 0.2f, sub = "-t, the odd one"),
                        DNode("sie", "sie, Sie lernen", 0.76f, 0.81f, w = 0.44f, h = 0.2f, tone = 2, sub = "-en"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("ich"), light = setOf("ich")),
                    Beat(5, show = setOf("du", "er"), light = setOf("du", "er")),
                    Beat(6, light = setOf("er"), card = BoardCard("Sanya lernt jeden Tag Deutsch.", "Sanya studies German every day. And du lernst: you learn.", phrase = true)),
                    Beat(8, show = setOf("wir", "ihr", "sie"), light = setOf("ihr"), card = BoardCard("e, st, t, en, t, en", "Say it like a song. ihr is the odd one: ihr lernt.")),
                    Beat(9, light = setOf("wir", "ihr", "sie"), card = BoardCard("Wir lernen. Ihr lernt. Sie lernen.", "We learn. You all learn. They learn.", phrase = true)),
                ),
            ),
            // 10: "That was too easy. What's the catch?"
            Scene(
                10, "Stems ending in t",
                DiagramSpec(
                    "A stem ending in t or d adds an extra e before st and t, so it can be said.",
                    nodes = listOf(
                        DNode("stem", "arbeit-", 0.5f, 0.14f, w = 0.44f, h = 0.2f, tone = 1, sub = "from arbeiten"),
                        DNode("bad", "du arbeitst", 0.26f, 0.55f, w = 0.44f, h = 0.22f, tone = 2, sub = "unsayable"),
                        DNode("good", "du arbeitest", 0.76f, 0.55f, w = 0.44f, h = 0.22f, sub = "extra e"),
                        DNode("er", "er arbeitet", 0.76f, 0.88f, w = 0.44f, kind = NodeKind.NOTE, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("stem", "bad", dashed = true),
                        DEdge("stem", "good"),
                        DEdge("good", "er"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("stem", "good", "er"), light = setOf("good"), pulse = "stem" to "good"),
                    Beat(12, light = setOf("good", "er"), card = BoardCard("Du arbeitest in Doha.", "You work in Doha. Er arbeitet bis halb vier: he works until half past three.", phrase = true)),
                    Beat(13, show = setOf("bad"), light = setOf("bad"), pulse = "stem" to "bad", card = BoardCard("Not: du arbeitst", "The classic mistake. The extra e makes it sayable.")),
                    Beat(15, light = setOf("good", "er"), card = BoardCard("Du arbeitest. Er arbeitet.", "You work. He works. And ich arbeite: I work.", phrase = true)),
                ),
            ),
            // 16: "Okay. Now questions. How do I ask when?"
            Scene(
                16, "The verb sits in seat two",
                DiagramSpec(
                    "W-question shape: W-word, verb, subject, rest. The verb stays second.",
                    nodes = listOf(
                        DNode("w", "Wann", 0.13f, 0.3f, w = 0.22f, h = 0.22f, tone = 1, sub = "seat 1"),
                        DNode("v", "kommst", 0.38f, 0.3f, w = 0.24f, h = 0.22f, sub = "seat 2"),
                        DNode("s", "du", 0.62f, 0.3f, w = 0.2f, h = 0.22f, tone = 2),
                        DNode("r", "nach Hause?", 0.86f, 0.3f, w = 0.24f, h = 0.22f, tone = 2),
                        DNode("bad", "Wann du kommst?", 0.5f, 0.78f, w = 0.56f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "not German"),
                    ),
                    edges = listOf(
                        DEdge("w", "v"),
                        DEdge("v", "s"),
                        DEdge("s", "r"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("w", "v", "s", "r"), light = setOf("v"), pulse = "w" to "v"),
                    Beat(18, light = setOf("w", "v"), card = BoardCard("Wann kommst du nach Hause?", "When are you coming home?", phrase = true)),
                    Beat(20, show = setOf("bad"), light = setOf("bad"), card = BoardCard("Verb straight after the W-word", "Not: Wann du kommst? The verb jumps to seat two.")),
                    Beat(21, light = setOf("v"), card = BoardCard("Was machst du? Wo wohnst du?", "What do you do? Where do you live?", phrase = true)),
                ),
            ),
            // 22: "«Wo», «woher», «wohin». Three words for where?"
            Scene(
                22, "Where, where from, where to",
                DiagramSpec(
                    "wo has no arrow; woher points towards you, wohin points away. wie viel and warum ask how much and why.",
                    nodes = listOf(
                        DNode("wo", "wo", 0.5f, 0.12f, w = 0.4f, h = 0.18f, tone = 1, sub = "where: no arrow"),
                        DNode("woher", "woher", 0.17f, 0.45f, w = 0.3f, h = 0.2f, sub = "where from"),
                        DNode("you", "You", 0.5f, 0.45f, w = 0.2f, h = 0.22f, kind = NodeKind.USER),
                        DNode("wohin", "wohin", 0.83f, 0.45f, w = 0.3f, h = 0.2f, sub = "where to"),
                        DNode("wv", "wie viel", 0.26f, 0.84f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "how much"),
                        DNode("wa", "warum", 0.76f, 0.84f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "why"),
                    ),
                    edges = listOf(
                        DEdge("woher", "you"),
                        DEdge("you", "wohin"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("wo", "you")),
                    Beat(23, show = setOf("woher", "wohin"), light = setOf("woher", "wohin"), pulse = "woher" to "you", card = BoardCard("Three wheres", "wo: where it is. woher: where from. wohin: where to.")),
                    Beat(24, light = setOf("woher", "wohin"), pulse = "you" to "wohin", card = BoardCard("Woher kommst du? Wohin gehst du?", "Where are you from? Where are you going?", phrase = true)),
                    Beat(26, show = setOf("wv", "wa"), light = setOf("wv", "wa")),
                    Beat(27, light = setOf("wa"), card = BoardCard("Warum lernst du Deutsch?", "Why are you learning German? And Wie viel kostet das: how much does that cost?", phrase = true)),
                ),
            ),
            // 28: "Last thing: time words. «Am», «um», «im». Which is which?"
            Scene(
                28, "um, am, im",
                DiagramSpec(
                    "um for a clock time, am for a day, im for a month; von and bis for a span.",
                    nodes = listOf(
                        DNode("um", "um acht Uhr", 0.5f, 0.12f, w = 0.6f, h = 0.18f, sub = "a pin on the clock"),
                        DNode("am", "am Montag", 0.5f, 0.36f, w = 0.6f, h = 0.18f, tone = 1, sub = "a page in the diary"),
                        DNode("im", "im Februar", 0.5f, 0.6f, w = 0.6f, h = 0.18f, tone = 2, sub = "the month on the wall"),
                        DNode("vb", "von sieben bis halb vier", 0.5f, 0.86f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, tone = 1, sub = "from ... until"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("um", "am", "im", "vb")),
                    Beat(30, light = setOf("um", "am", "im"), card = BoardCard("Um acht Uhr. Am Montag. Im Februar.", "At eight o'clock. On Monday. In February.", phrase = true)),
                ),
            ),
            // 31: "What if I put the time first? «Am Montag», and then what?"
            Scene(
                31, "Time first, verb second",
                DiagramSpec(
                    "Anything can take seat one; the verb keeps seat two and the person moves in behind it.",
                    nodes = listOf(
                        DNode("t", "Am Montag", 0.17f, 0.3f, w = 0.3f, h = 0.22f, tone = 1, sub = "seat 1"),
                        DNode("v", "arbeite", 0.5f, 0.3f, w = 0.28f, h = 0.22f, sub = "seat 2"),
                        DNode("s", "ich", 0.83f, 0.3f, h = 0.22f, tone = 2),
                        DNode("r", "von sieben bis halb vier", 0.5f, 0.72f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("t", "v"),
                        DEdge("v", "s"),
                        DEdge("s", "r", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("t"), light = setOf("t")),
                    Beat(32, show = setOf("v", "s"), light = setOf("v"), pulse = "v" to "s"),
                    Beat(33, show = setOf("r"), light = setOf("t", "v", "s"), card = BoardCard("Am Montag arbeite ich.", "On Monday I work, von sieben bis halb vier: from seven to half past three.", phrase = true)),
                ),
            ),
            // 34: "So what should I remember for the Goethe exam?"
            Scene(
                34, "For the Goethe exam",
                DiagramSpec(
                    "Verbs and questions in the Goethe A1: endings in writing, W-words in listening and speaking, verb second.",
                    nodes = listOf(
                        DNode("wr", "Writing", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "du arbeitest, wir lernen"),
                        DNode("hs", "Listening, speaking", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "woher: a country"),
                        DNode("v2", "Verb second", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "even after am Montag"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(35, show = setOf("wr", "hs", "v2"), card = BoardCard("The W-word says what's wanted", "woher wants a country, wann wants a time.")),
                    Beat(37, card = BoardCard("Today", "Conjugate arbeiten, lernen, machen. Ask and answer four W-questions. Time first once.")),
                    Beat(38, card = BoardCard("Tschüss, bis bald!", "Bye, see you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de3Dialogue() = PodcastBoard(
        "de3-dialogue",
        listOf(
            // 0: "Welcome back. Today is a dialogue: making an appointment on the phone...."
            Scene(
                0, "Calling the Praxis",
                DiagramSpec(
                    "Sanya phones Dr Berger's practice. Listen for the day and the time.",
                    nodes = listOf(
                        DNode("sanya", "Sanya", 0.18f, 0.4f, w = 0.22f, h = 0.24f, kind = NodeKind.USER),
                        DNode("praxis", "die Praxis", 0.74f, 0.4f, w = 0.44f, h = 0.24f, tone = 1, sub = "Dr. Berger"),
                        DNode("listen", "Day + time", 0.5f, 0.84f, w = 0.5f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "what to listen for"),
                    ),
                    edges = listOf(
                        DEdge("sanya", "praxis", "phone"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("sanya", "listen"), light = setOf("listen")),
                    Beat(2, show = setOf("praxis"), light = setOf("praxis"), pulse = "sanya" to "praxis", card = BoardCard("die Praxis", "A doctor's practice.")),
                    Beat(3, light = setOf("praxis"), card = BoardCard("Praxis Dr. Berger, guten Tag.", "Doctor Berger's practice, good day.", phrase = true)),
                ),
            ),
            // 4: "And Sanya? Does she say who she is first?"
            Scene(
                4, "The appointment exchange",
                DiagramSpec(
                    "The appointment exchange: ask, offer, refuse with a reason, counter-offer, accept.",
                    nodes = listOf(
                        DNode("a", "Ich brauche einen Termin", 0.5f, 0.1f, w = 0.6f, h = 0.14f),
                        DNode("b", "Am Dienstag um zehn?", 0.5f, 0.32f, w = 0.6f, h = 0.14f, tone = 1),
                        DNode("c", "Nein, am Dienstag arbeite ich.", 0.5f, 0.54f, w = 0.76f, h = 0.14f, tone = 2),
                        DNode("d", "Geht es am Mittwoch?", 0.5f, 0.76f, w = 0.6f, h = 0.14f, tone = 1),
                        DNode("e", "Ja, das passt.", 0.5f, 0.93f, w = 0.4f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("a", "b"),
                        DEdge("b", "c"),
                        DEdge("c", "d"),
                        DEdge("d", "e"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("a"), light = setOf("a")),
                    Beat(6, light = setOf("a"), card = BoardCard("Ich brauche einen Termin.", "I need an appointment. Open with your name: Sanya Khan hier.", phrase = true)),
                    Beat(8, show = setOf("b"), light = setOf("b"), pulse = "a" to "b"),
                    Beat(9, light = setOf("b"), card = BoardCard("Wann haben Sie Zeit?", "When are you free? Am Dienstag um zehn Uhr: on Tuesday at ten?", phrase = true)),
                    Beat(11, light = setOf("b"), card = BoardCard("die Zeit", "Time. Wann haben Sie Zeit? When are you free?")),
                    Beat(13, show = setOf("c"), light = setOf("c"), pulse = "b" to "c", card = BoardCard("Refuse with a reason", "A bare nein sounds abrupt. The reason makes it polite, and it is marked.")),
                    Beat(14, show = setOf("d", "e"), light = setOf("c"), card = BoardCard("Nein, am Dienstag arbeite ich.", "No, on Tuesday I work.", phrase = true)),
                ),
            ),
            // 15: "Then she suggests another day?"
            Scene(
                15, "Geht es ...?",
                DiagramSpec(
                    "Geht es am Mittwoch? asks if a time is possible. With wie, it asks how someone is.",
                    nodes = listOf(
                        DNode("q", "Geht es am Mittwoch?", 0.5f, 0.18f, w = 0.7f, h = 0.22f, sub = "does Wednesday work?"),
                        DNode("ge", "gehen", 0.26f, 0.62f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, tone = 1, sub = "here: to be possible"),
                        DNode("wie", "Wie geht es?", 0.76f, 0.62f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "how are you?"),
                    ),
                    edges = listOf(
                        DEdge("ge", "q"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("q", "ge"), light = setOf("ge"), pulse = "ge" to "q"),
                    Beat(17, light = setOf("q"), card = BoardCard("Geht es am Mittwoch?", "Does Wednesday work?", phrase = true)),
                    Beat(19, show = setOf("wie"), light = setOf("wie"), card = BoardCard("Wie geht es? is different", "With wie it asks how you are. Without it, it asks if a time works.")),
                ),
            ),
            // 20: "Am Mittwoch um halb drei?"
            Scene(
                20, "halb drei, das passt",
                DiagramSpec(
                    "halb drei is half-way to three: 2:30. Das passt accepts the time.",
                    nodes = listOf(
                        DNode("two", "2:00", 0.15f, 0.32f, w = 0.22f, h = 0.22f, tone = 2),
                        DNode("half", "halb drei", 0.5f, 0.32f, w = 0.32f, h = 0.26f, sub = "2:30"),
                        DNode("three", "3:00", 0.85f, 0.32f, w = 0.22f, h = 0.22f, tone = 2),
                        DNode("passt", "Ja, das passt.", 0.5f, 0.78f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, tone = 1, sub = "that suits"),
                    ),
                    edges = listOf(
                        DEdge("two", "half", dashed = true),
                        DEdge("half", "three", "half-way to"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("two", "half", "three"), light = setOf("half"), card = BoardCard("Am Mittwoch um halb drei?", "On Wednesday at half past two?", phrase = true)),
                    Beat(22, show = setOf("passt"), light = setOf("half", "three"), pulse = "half" to "three", card = BoardCard("halb drei is 2:30", "Half-way to three, not half past three.")),
                    Beat(23, light = setOf("passt"), card = BoardCard("Ja, das passt. Vielen Dank.", "Yes, that suits. Many thanks.", phrase = true)),
                ),
            ),
            // 24: "And now goodbye. «Auf Wiedersehen»?"
            Scene(
                24, "Seeing or hearing",
                DiagramSpec(
                    "sehen is to see and hören is to hear: a phone call ends with Auf Wiederhören.",
                    nodes = listOf(
                        DNode("see", "Auf Wiedersehen", 0.26f, 0.3f, w = 0.44f, h = 0.26f, tone = 2, sub = "in person: sehen"),
                        DNode("hear", "Auf Wiederhören", 0.76f, 0.3f, w = 0.44f, h = 0.26f, sub = "on the phone: hören"),
                        DNode("phone", "das Telefon", 0.76f, 0.78f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("phone", "hear"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("see"), light = setOf("see")),
                    Beat(25, show = setOf("hear", "phone"), light = setOf("hear"), pulse = "phone" to "hear"),
                    Beat(26, light = setOf("hear"), card = BoardCard("Bis Mittwoch, Frau Khan.", "Until Wednesday, Mrs Khan. Auf Wiederhören: goodbye, on the phone.", phrase = true)),
                ),
            ),
            // 27: "Can I hear the middle bit once more? The refusal and the new offer?"
            Scene(
                27, "Refuse, then offer",
                DiagramSpec(
                    "The polite pattern: refuse with a reason, then counter-offer. The practice asks and offers.",
                    nodes = listOf(
                        DNode("r", "Nein, am Dienstag arbeite ich.", 0.5f, 0.22f, w = 0.76f, h = 0.22f, tone = 1, sub = "refuse with a reason"),
                        DNode("o", "Geht es am Mittwoch?", 0.5f, 0.66f, w = 0.7f, h = 0.22f, sub = "counter-offer"),
                    ),
                    edges = listOf(
                        DEdge("r", "o", "then"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("r", "o"), pulse = "r" to "o"),
                    Beat(29, light = setOf("r", "o"), card = BoardCard("Nein, am Dienstag arbeite ich.", "No, on Tuesday I work. Geht es am Mittwoch? Does Wednesday work?", phrase = true)),
                    Beat(32, light = setOf("o"), card = BoardCard("Wann haben Sie Zeit?", "When are you free? Am Mittwoch um halb drei: on Wednesday at half past two?", phrase = true)),
                ),
            ),
            // 33: "So what should I remember for the Goethe exam?"
            Scene(
                33, "For the Goethe exam",
                DiagramSpec(
                    "Appointments in the Goethe A1: day and time when listening, requests in speaking, the phone goodbye.",
                    nodes = listOf(
                        DNode("hear", "Listening", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "halb drei is 2:30"),
                        DNode("speak", "Speaking part 3", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Geht es am Mittwoch?"),
                        DNode("phone", "Auf Wiederhören", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "ends a phone call"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("hear", "speak", "phone"), card = BoardCard("Catch the day and the time", "Ich brauche einen Termin and Geht es ...? do the work in speaking.")),
                    Beat(36, card = BoardCard("Today", "Phone an imaginary surgery: ask, refuse with a reason, counter-offer, Auf Wiederhören.")),
                    Beat(37, card = BoardCard("Auf Wiederhören! Bis bald!", "Goodbye, as on the phone! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de3Practice() = PodcastBoard(
        "de3-practice",
        listOf(
            // 0: "Welcome back. Today is practice: a week in Doha. We'll read a short te..."
            Scene(
                0, "Scan before you read",
                DiagramSpec(
                    "Reading a timetable text: hunt for days and clock times before the questions.",
                    nodes = listOf(
                        DNode("p0", "Days", 0.26f, 0.2f, w = 0.44f, h = 0.26f, sub = "von ... bis"),
                        DNode("p1", "Times", 0.76f, 0.2f, w = 0.44f, h = 0.26f, sub = "um ... Uhr"),
                        DNode("p2", "Verbs", 0.26f, 0.62f, w = 0.44f, h = 0.26f, tone = 2, sub = "arbeitet, lernt"),
                        DNode("p3", "Questions", 0.76f, 0.62f, w = 0.44f, h = 0.26f, tone = 2, sub = "richtig / falsch"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("p3")),
                    Beat(2, show = setOf("p0", "p1", "p2"), light = setOf("p0", "p1"), card = BoardCard("Scan first", "Days and clock times before the questions: they ask when.")),
                    Beat(3, light = setOf("p0"), card = BoardCard("Von Sonntag bis Donnerstag.", "Usaid arbeitet von Sonntag bis Donnerstag: Usaid works Sunday to Thursday.", phrase = true)),
                ),
            ),
            // 4: "«Von» and «bis». From and to."
            Scene(
                4, "Usaid's working day",
                DiagramSpec(
                    "The text gives official times; a question may give the everyday form of the same moment.",
                    nodes = listOf(
                        DNode("n1", "um sieben Uhr", 0.5f, 0.14f, w = 0.64f, h = 0.2f, tone = 1, sub = "er beginnt"),
                        DNode("n2", "fünfzehn Uhr dreißig", 0.5f, 0.48f, w = 0.64f, h = 0.2f, sub = "fertig: halb vier"),
                        DNode("n3", "sechzehn Uhr fünfzehn", 0.5f, 0.82f, w = 0.64f, h = 0.2f, tone = 1, sub = "eine Stunde Deutsch"),
                    ),
                    edges = listOf(
                        DEdge("n1", "n2"),
                        DEdge("n2", "n3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("n1"), light = setOf("n1")),
                    Beat(6, show = setOf("n2"), light = setOf("n2"), pulse = "n1" to "n2", card = BoardCard("Um fünfzehn Uhr dreißig fertig.", "Er ist um fünfzehn Uhr dreißig fertig: he is finished at 15:30.", phrase = true)),
                    Beat(8, light = setOf("n2"), card = BoardCard("fünfzehn Uhr dreißig", "Take twelve off: 3:30 pm. Everyday German: halb vier.")),
                    Beat(9, show = setOf("n3"), light = setOf("n3"), pulse = "n2" to "n3", card = BoardCard("Er lernt eine Stunde Deutsch.", "At sixteen fifteen he studies German for an hour.", phrase = true)),
                ),
            ),
            // 10: "And his weekend?"
            Scene(
                10, "The weekend and nicht",
                DiagramSpec(
                    "nicht flips a statement; einkaufen splits, with ein at the very end.",
                    nodes = listOf(
                        DNode("fr", "am Freitag", 0.26f, 0.2f, w = 0.44f, h = 0.24f, tone = 1, sub = "arbeitet er nicht"),
                        DNode("sa", "am Samstag", 0.76f, 0.2f, w = 0.44f, h = 0.24f, tone = 1, sub = "kauft er ein"),
                        DNode("nicht", "nicht", 0.26f, 0.66f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, tone = 2, sub = "flips the statement"),
                        DNode("ein", "kauft ... ein", 0.76f, 0.66f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, tone = 2, sub = "einkaufen splits"),
                    ),
                    edges = listOf(
                        DEdge("fr", "nicht", dashed = true),
                        DEdge("sa", "ein", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("fr", "sa", "nicht", "ein"), light = setOf("nicht", "ein")),
                    Beat(12, light = setOf("fr", "sa"), card = BoardCard("Am Samstag kauft er ein.", "On Saturday he shops. Am Freitag arbeitet er nicht: on Friday he does not work.", phrase = true)),
                    Beat(13, light = setOf("ein"), pulse = "sa" to "ein"),
                ),
            ),
            // 14: "Sanya's week is the other pattern, Monday to Friday. And in the aftern..."
            Scene(
                14, "Sanya's week",
                DiagramSpec(
                    "Sanya works Monday to Friday and has an English course in the afternoon.",
                    nodes = listOf(
                        DNode("s", "Sanya", 0.5f, 0.16f, w = 0.22f, h = 0.24f, kind = NodeKind.USER),
                        DNode("w", "von Montag bis Freitag", 0.26f, 0.62f, w = 0.46f, h = 0.26f, tone = 1, sub = "arbeitet"),
                        DNode("n", "der Nachmittag", 0.76f, 0.62f, w = 0.44f, h = 0.26f, sub = "Englischkurs"),
                    ),
                    edges = listOf(
                        DEdge("s", "w"),
                        DEdge("s", "n"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("s", "w", "n"), light = setOf("n"), pulse = "s" to "n"),
                    Beat(15, light = setOf("w", "n"), card = BoardCard("Am Nachmittag hat sie einen Kurs.", "Sanya arbeitet von Montag bis Freitag. In the afternoon she has an English course.", phrase = true)),
                ),
            ),
            // 16: "Okay, quiz me."
            Scene(
                16, "Richtig oder falsch",
                DiagramSpec(
                    "Match the time or the day in the question to the text, then answer richtig or falsch.",
                    nodes = listOf(
                        DNode("q1", "Usaid lernt um Viertel nach vier.", 0.5f, 0.15f, w = 0.8f, h = 0.24f, tone = 1, sub = "richtig: sechzehn Uhr fünfzehn"),
                        DNode("q2", "Sanya arbeitet am Samstag.", 0.5f, 0.48f, w = 0.8f, h = 0.24f, tone = 2, sub = "falsch: Montag bis Freitag"),
                        DNode("q3", "Usaid ist um halb vier fertig.", 0.5f, 0.82f, w = 0.8f, h = 0.24f, sub = "richtig: fünfzehn Uhr dreißig"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("q1", "q2"), light = setOf("q1", "q2"), card = BoardCard("richtig oder falsch", "True or false: match the time in the question to the time in the text.")),
                    Beat(19, show = setOf("q3"), light = setOf("q3"), card = BoardCard("Two forms, one moment", "fünfzehn Uhr dreißig in the text, halb vier in the question: richtig.")),
                ),
            ),
            // 21: "Yes. Usaid writes his own week, with «ich». Work comes first, framed b..."
            Scene(
                21, "Writing your week",
                DiagramSpec(
                    "Five sentences about a week. A time phrase can come first; the verb stays second.",
                    nodes = listOf(
                        DNode("t", "Um Viertel nach vier", 0.2f, 0.16f, w = 0.38f, h = 0.26f, tone = 1, sub = "seat 1"),
                        DNode("v", "lerne", 0.5f, 0.16f, w = 0.2f, h = 0.26f, sub = "seat 2"),
                        DNode("s", "ich", 0.71f, 0.16f, w = 0.18f, h = 0.26f, tone = 2),
                        DNode("r", "Deutsch.", 0.9f, 0.16f, w = 0.18f, h = 0.26f, tone = 2),
                        DNode("w1", "von ... bis", 0.26f, 0.52f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "work days"),
                        DNode("w2", "am Freitag", 0.76f, 0.52f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "arbeite ich nicht"),
                        DNode("w3", "am Samstag", 0.26f, 0.82f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "kaufe ich ein"),
                        DNode("w4", "am Abend", 0.76f, 0.82f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "telefoniere ich"),
                    ),
                    edges = listOf(
                        DEdge("t", "v"),
                        DEdge("v", "s"),
                        DEdge("s", "r"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("w1"), light = setOf("w1")),
                    Beat(22, light = setOf("w1"), card = BoardCard("Ich arbeite von sieben bis halb vier", "I work from Sunday to Thursday, from seven to half past three.", phrase = true)),
                    Beat(24, show = setOf("t", "v", "s", "r"), light = setOf("v"), pulse = "t" to "v"),
                    Beat(25, light = setOf("t", "v", "s"), card = BoardCard("Um Viertel nach vier lerne ich ...", "At a quarter past four I study German: Um Viertel nach vier lerne ich Deutsch.", phrase = true)),
                    Beat(27, show = setOf("w2", "w3"), light = setOf("w2", "w3")),
                    Beat(28, light = setOf("w2", "w3"), card = BoardCard("Am Samstag kaufe ich ein.", "On Saturday I go shopping. Am Freitag arbeite ich nicht: on Friday I do not work.", phrase = true)),
                    Beat(31, show = setOf("w4"), light = setOf("w4"), card = BoardCard("Am Abend telefoniere ich mit Sanya.", "In the evening I phone Sanya.", phrase = true)),
                ),
            ),
            // 32: "So what should I remember for the Goethe exam?"
            Scene(
                32, "For the Goethe exam",
                DiagramSpec(
                    "A week in the Goethe A1: scan when reading, move between the two clocks, verb second in writing.",
                    nodes = listOf(
                        DNode("read", "Reading", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "days, times, nicht"),
                        DNode("clock", "Two clocks", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "15:30 is halb vier"),
                        DNode("write", "Writing", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "verb second after am Samstag"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("read", "clock", "write"), card = BoardCard("Scan, convert, verb second", "Days and times first; official and everyday clock; verb in seat two.")),
                    Beat(35, card = BoardCard("Today", "Say the time at your phone five times: halb once, one 24-hour time, your study slot.")),
                    Beat(36, card = BoardCard("Bis Samstag! Tschüss!", "See you on Saturday! Bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de4Words() = PodcastBoard(
        "de4-words",
        listOf(
            // 0: "Hi, and welcome back. Today is food and shopping: the words that get y..."
            Scene(
                0, "Every noun has an article",
                DiagramSpec(
                    "Learn every food word with its article: der, die or das, like a first name and a surname.",
                    nodes = listOf(
                        DNode("hm", "der", 0.17f, 0.1f, h = 0.12f, kind = NodeKind.PILL),
                        DNode("hf", "die", 0.5f, 0.1f, h = 0.12f, kind = NodeKind.PILL),
                        DNode("hn", "das", 0.83f, 0.1f, h = 0.12f, kind = NodeKind.PILL),
                        DNode("m1", "der Kaffee", 0.17f, 0.36f, w = 0.3f, tone = 1, sub = "coffee"),
                        DNode("m2", "der Tee", 0.17f, 0.56f, w = 0.3f, tone = 1, sub = "tea"),
                        DNode("f1", "die Milch", 0.5f, 0.36f, w = 0.3f, tone = 1, sub = "milk"),
                        DNode("n1", "das Brot", 0.83f, 0.36f, w = 0.3f, tone = 1, sub = "bread"),
                        DNode("n2", "das Wasser", 0.83f, 0.56f, w = 0.3f, tone = 1, sub = "water"),
                        DNode("tip", "Learn the article with the word", 0.5f, 0.85f, w = 0.84f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("hm", "m1"),
                        DEdge("hf", "f1"),
                        DEdge("hn", "n1"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("hm", "hf", "hn")),
                    Beat(1, card = BoardCard("Hallo! Heute gehen wir einkaufen.", "Hello! Today we're going shopping.", phrase = true)),
                    Beat(4, show = setOf("n1"), light = setOf("n1"), card = BoardCard("Ich kaufe ein Brot.", "I buy a loaf of bread.", phrase = true)),
                    Beat(6, show = setOf("tip"), light = setOf("hn", "n1"), pulse = "hn" to "n1", card = BoardCard("das Brot, not just Brot", "Half the work of a German noun is der, die or das.")),
                    Beat(7, show = setOf("m1", "m2", "f1", "n2"), light = setOf("m1", "m2", "f1", "n2"), card = BoardCard("Der Kaffee. Die Milch.", "The coffee. The milk. Also: der Tee, das Wasser.", phrase = true)),
                    Beat(10, light = setOf("n2"), card = BoardCard("Ein Wasser, bitte.", "A water, please.", phrase = true)),
                ),
            ),
            // 11: "Now the supermarket. What goes in my trolley?"
            Scene(
                11, "In the trolley",
                DiagramSpec(
                    "Supermarket words with their articles; der Apfel becomes die Äpfel, with a vowel change.",
                    nodes = listOf(
                        DNode("reis", "der Reis", 0.26f, 0.12f, w = 0.44f, tone = 1, sub = "rice"),
                        DNode("huhn", "das Hähnchen", 0.74f, 0.12f, w = 0.44f, tone = 1, sub = "chicken"),
                        DNode("gem", "das Gemüse", 0.26f, 0.34f, w = 0.44f, tone = 1, sub = "vegetables"),
                        DNode("obst", "das Obst", 0.74f, 0.34f, w = 0.44f, tone = 1, sub = "fruit"),
                        DNode("apfel", "der Apfel", 0.26f, 0.72f, w = 0.4f, h = 0.2f, sub = "one apple"),
                        DNode("aepfel", "die Äpfel", 0.74f, 0.72f, w = 0.4f, h = 0.2f, sub = "apples"),
                    ),
                    edges = listOf(
                        DEdge("apfel", "aepfel", "plural"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("reis", "huhn", "gem", "obst", "apfel"), light = setOf("apfel")),
                    Beat(13, show = setOf("aepfel"), light = setOf("apfel", "aepfel"), pulse = "apfel" to "aepfel", card = BoardCard("Der Apfel. Die Äpfel.", "The apple. The apples.", phrase = true)),
                ),
            ),
            // 14: "Okay. So how do I say two kilos of apples? What's the German word for ..."
            Scene(
                14, "No word for of",
                DiagramSpec(
                    "The quantity sits straight in front of the noun, with no von and no article.",
                    nodes = listOf(
                        DNode("q1", "ein Kilo", 0.26f, 0.18f, w = 0.4f, h = 0.2f, tone = 1, sub = "quantity"),
                        DNode("n1", "Äpfel", 0.74f, 0.18f, w = 0.4f, h = 0.2f, sub = "food"),
                        DNode("q2", "eine Flasche", 0.26f, 0.46f, w = 0.4f, h = 0.2f, tone = 1, sub = "quantity"),
                        DNode("n2", "Wasser", 0.74f, 0.46f, w = 0.4f, h = 0.2f, sub = "food"),
                        DNode("von", "no von", 0.5f, 0.8f, w = 0.5f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "nothing in between"),
                    ),
                    edges = listOf(
                        DEdge("q1", "n1"),
                        DEdge("q2", "n2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("q1", "n1"), light = setOf("q1", "n1"), pulse = "q1" to "n1", card = BoardCard("No word for of", "The amount goes straight in front of the food.")),
                    Beat(16, show = setOf("q2", "n2"), light = setOf("q2", "n2"), card = BoardCard("Ein Kilo Äpfel, bitte.", "A kilo of apples, please. And: eine Flasche Wasser, a bottle of water.", phrase = true)),
                    Beat(18, show = setOf("von"), light = setOf("von"), card = BoardCard("Not: eine Flasche von Wasser", "Amount, then food. Nothing in between.")),
                    Beat(19, card = BoardCard("Ich kaufe Brot und Milch.", "I buy bread and milk. Ich trinke Kaffee: I drink coffee.", phrase = true)),
                ),
            ),
            // 20: "And when I'm at the till and I want to know a price?"
            Scene(
                20, "At the Kasse",
                DiagramSpec(
                    "Ask Was kostet das? Prices are euros, then cents, and the comma is the decimal point.",
                    nodes = listOf(
                        DNode("kasse", "die Kasse", 0.5f, 0.1f, w = 0.5f, tone = 1, sub = "checkout"),
                        DNode("frage", "Was kostet das?", 0.5f, 0.33f, w = 0.7f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("p1", "3,50 €", 0.2f, 0.58f, w = 0.34f, kind = NodeKind.NOTE, tone = 2),
                        DNode("w1", "drei Euro fünfzig", 0.69f, 0.58f, w = 0.58f),
                        DNode("p2", "0,80 €", 0.2f, 0.84f, w = 0.34f, kind = NodeKind.NOTE, tone = 2),
                        DNode("w2", "achtzig Cent", 0.69f, 0.84f, w = 0.58f),
                    ),
                    edges = listOf(
                        DEdge("kasse", "frage", dashed = true),
                        DEdge("p1", "w1"),
                        DEdge("p2", "w2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("kasse", "frage"), light = setOf("kasse"), card = BoardCard("die Kasse, die Tüte", "The checkout, and the bag the cashier may ask about.")),
                    Beat(22, light = setOf("frage"), pulse = "kasse" to "frage", card = BoardCard("Was kostet das?", "What does that cost?", phrase = true)),
                    Beat(24, show = setOf("p1", "w1"), light = setOf("p1", "w1"), pulse = "p1" to "w1", card = BoardCard("Euros, then cents", "The comma is the decimal point: 3,50 € is three euros fifty.")),
                    Beat(25, light = setOf("w1"), card = BoardCard("Das kostet drei Euro fünfzig.", "That costs three euros fifty.", phrase = true)),
                    Beat(27, show = setOf("p2", "w2"), light = setOf("p2", "w2"), pulse = "p2" to "w2", card = BoardCard("das Angebot", "A special offer. Under one euro, just say the cents.")),
                    Beat(28, card = BoardCard("Drei Euro neunundneunzig.", "Three euros ninety-nine. And: achtzig Cent, eighty cents.", phrase = true)),
                ),
            ),
            // 29: "Now how do I actually ask for things? Can I say «Ich will», like I wan..."
            Scene(
                29, "Ordering politely",
                DiagramSpec(
                    "Ich will sounds blunt in a shop; Ich hätte gern is the polite I would like.",
                    nodes = listOf(
                        DNode("will", "Ich will", 0.24f, 0.16f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "blunt"),
                        DNode("gern", "Ich hätte gern", 0.74f, 0.16f, w = 0.46f, h = 0.2f, sub = "polite: I would like"),
                        DNode("obj", "ein Brot", 0.74f, 0.46f, w = 0.46f, h = 0.18f, tone = 1, sub = "what you want"),
                        DNode("lecker", "lecker", 0.24f, 0.8f, w = 0.4f, h = 0.18f, tone = 1, sub = "delicious"),
                        DNode("rech", "die Rechnung", 0.74f, 0.8f, w = 0.46f, h = 0.18f, tone = 1, sub = "the bill"),
                    ),
                    edges = listOf(
                        DEdge("gern", "obj"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("will", "gern"), light = setOf("gern")),
                    Beat(31, show = setOf("obj"), light = setOf("gern", "obj"), pulse = "gern" to "obj", card = BoardCard("Ich hätte gern ein Brot.", "I'd like a loaf of bread, and eine Flasche Wasser, a bottle of water.", phrase = true)),
                    Beat(33, show = setOf("lecker", "rech"), light = setOf("lecker", "rech")),
                    Beat(34, card = BoardCard("Das ist lecker!", "That's delicious! Die Rechnung, bitte: the bill, please.", phrase = true)),
                ),
            ),
            // 35: "So what should I remember for the Goethe exam?"
            Scene(
                35, "For the Goethe exam",
                DiagramSpec(
                    "Food words in the Goethe A1: prices in Hören, polite asking in Sprechen, no von in writing.",
                    nodes = listOf(
                        DNode("hear", "Hören", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "euros, then cents"),
                        DNode("speak", "Sprechen", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Ich hätte gern"),
                        DNode("write", "Writing", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "amount + food, no von"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("hear", "speak", "write"), card = BoardCard("Listen for the price", "Euros first, then cents. Ask with Was kostet das?")),
                    Beat(38, card = BoardCard("Today", "A real shopping list, ten items with der, die or das. Say three prices aloud.")),
                    Beat(39, card = BoardCard("Viel Spaß beim Einkaufen!", "Have fun shopping!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de4Grammar() = PodcastBoard(
        "de4-grammar",
        listOf(
            // 0: "Welcome back. Today is grammar, and the big word is «Akkusativ», the a..."
            Scene(
                0, "Three drawers",
                DiagramSpec(
                    "Every noun has a gender: der and das take ein, die takes eine, and the plural takes die with no ein.",
                    nodes = listOf(
                        DNode("dm", "der Apfel", 0.13f, 0.18f, w = 0.22f, h = 0.2f, sub = "masculine"),
                        DNode("df", "die Milch", 0.38f, 0.18f, w = 0.22f, h = 0.2f, sub = "feminine"),
                        DNode("dn", "das Brot", 0.63f, 0.18f, w = 0.22f, h = 0.2f, sub = "neuter"),
                        DNode("dp", "die Äpfel", 0.88f, 0.18f, w = 0.22f, h = 0.2f, sub = "plural"),
                        DNode("im", "ein Apfel", 0.13f, 0.52f, w = 0.22f, h = 0.2f, tone = 1),
                        DNode("if", "eine Flasche", 0.38f, 0.52f, w = 0.22f, h = 0.2f, tone = 1),
                        DNode("in", "ein Brot", 0.63f, 0.52f, w = 0.22f, h = 0.2f, tone = 1),
                        DNode("ip", "no ein", 0.88f, 0.52f, w = 0.22f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                        DNode("tip", "Learn the Artikel with every noun", 0.5f, 0.85f, w = 0.86f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("dm", "im"),
                        DEdge("df", "if"),
                        DEdge("dn", "in"),
                        DEdge("dp", "ip", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("dm", "df", "dn")),
                    Beat(1, card = BoardCard("Hallo! Heute machen wir Grammatik.", "Hello! Today we're doing grammar.", phrase = true)),
                    Beat(3, show = setOf("tip"), light = setOf("tip")),
                    Beat(4, light = setOf("dm", "df", "dn"), card = BoardCard("Der Apfel. Die Milch. Das Brot.", "The apple. The milk. The bread.", phrase = true)),
                    Beat(6, show = setOf("dp", "im", "if", "in", "ip"), light = setOf("im", "if", "in"), pulse = "dm" to "im", card = BoardCard("One a-word per drawer", "der and das take ein, die takes eine; the plural has none.")),
                    Beat(7, light = setOf("dp"), card = BoardCard("Ein Apfel. Eine Flasche.", "An apple. A bottle. Ein Brot, a loaf. Die Äpfel, the apples.", phrase = true)),
                ),
            ),
            // 8: "Fine. Now the scary one. What is the accusative?"
            Scene(
                8, "Only the masculine moves",
                DiagramSpec(
                    "The subject stays as it is; a masculine object changes der to den and ein to einen.",
                    nodes = listOf(
                        DNode("s", "Ich", 0.14f, 0.16f, w = 0.22f, h = 0.22f, tone = 2, sub = "Nominativ"),
                        DNode("v", "esse", 0.41f, 0.16f, w = 0.22f, h = 0.22f, tone = 1, sub = "verb"),
                        DNode("o", "den Apfel", 0.76f, 0.16f, w = 0.4f, h = 0.22f, sub = "Akkusativ"),
                        DNode("c1", "der → den", 0.26f, 0.5f, w = 0.44f, h = 0.2f, sub = "masculine"),
                        DNode("c2", "ein → einen", 0.75f, 0.5f, w = 0.46f, h = 0.2f, sub = "masculine"),
                        DNode("stay", "die, das, plural: no change", 0.5f, 0.83f, w = 0.86f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("s", "v"),
                        DEdge("v", "o"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("s", "v", "o"), light = setOf("s", "o"), pulse = "v" to "o"),
                    Beat(11, show = setOf("c1", "c2"), light = setOf("c1", "c2")),
                    Beat(12, light = setOf("o", "c1"), card = BoardCard("Ich esse den Apfel.", "I eat the apple. As the subject: Der Apfel ist rot, the apple is red.", phrase = true)),
                    Beat(14, light = setOf("c2")),
                    Beat(15, light = setOf("c2"), card = BoardCard("Ich trinke einen Kaffee.", "I drink a coffee.", phrase = true)),
                    Beat(17, show = setOf("stay"), light = setOf("stay")),
                    Beat(18, light = setOf("stay"), card = BoardCard("Ich kaufe die Milch.", "I buy the milk. Ich kaufe das Brot: I buy the bread. No change.", phrase = true)),
                    Beat(21, light = setOf("c1", "c2"), card = BoardCard("Ich nehme den Tee.", "I'll have the tea. Sanya trinkt einen Kaffee: Sanya drinks a coffee.", phrase = true)),
                ),
            ),
            // 22: "Okay, I've got it. Now plurals. Is there one ending, like the English ..."
            Scene(
                22, "Plurals: five patterns",
                DiagramSpec(
                    "Five plural patterns: e for many masculine, n for most feminine, er, s for borrowed words, and an umlaut.",
                    nodes = listOf(
                        DNode("pe", "+ e", 0.14f, 0.1f, w = 0.24f, kind = NodeKind.PILL, tone = 1),
                        DNode("xe", "der Tag, die Tage", 0.64f, 0.1f, w = 0.66f, sub = "many masculine"),
                        DNode("pn", "+ n", 0.14f, 0.3f, w = 0.24f, kind = NodeKind.PILL, tone = 1),
                        DNode("xn", "die Flasche, die Flaschen", 0.64f, 0.3f, w = 0.66f, sub = "most feminine"),
                        DNode("pu", "umlaut", 0.14f, 0.5f, w = 0.24f, kind = NodeKind.PILL, tone = 1),
                        DNode("xu", "der Apfel, die Äpfel", 0.64f, 0.5f, w = 0.66f, sub = "masculine in el"),
                        DNode("ps", "+ s", 0.14f, 0.7f, w = 0.24f, kind = NodeKind.PILL, tone = 1),
                        DNode("xs", "das Auto, die Autos", 0.64f, 0.7f, w = 0.66f, sub = "borrowed words"),
                        DNode("per", "+ er", 0.14f, 0.9f, w = 0.24f, kind = NodeKind.PILL, tone = 1),
                        DNode("xer", "das Kind, die Kinder", 0.64f, 0.9f, w = 0.66f, sub = "short neuter"),
                    ),
                    edges = listOf(
                        DEdge("pe", "xe"),
                        DEdge("pn", "xn"),
                        DEdge("pu", "xu"),
                        DEdge("ps", "xs"),
                        DEdge("per", "xer"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("pe", "xe", "pn", "xn", "pu", "xu"), light = setOf("xe", "xn", "xu")),
                    Beat(24, card = BoardCard("Die Tage. Die Flaschen. Die Äpfel.", "The days. The bottles. The apples.", phrase = true)),
                    Beat(26, show = setOf("ps", "xs", "per", "xer"), light = setOf("xs", "xer"), pulse = "ps" to "xs", card = BoardCard("s only for borrowed words", "das Auto, die Autos. Short neuter words often add er: die Kinder.")),
                ),
            ),
            // 27: "Du isst. Du sprichst. Du nimmst. Du fährst."
            Scene(
                27, "Verbs that change their vowel",
                DiagramSpec(
                    "A few common verbs change their vowel for du and er: essen, du isst.",
                    nodes = listOf(
                        DNode("e", "essen", 0.24f, 0.12f, w = 0.4f, tone = 1, sub = "to eat"),
                        DNode("e2", "du isst", 0.74f, 0.12f, w = 0.44f),
                        DNode("s", "sprechen", 0.24f, 0.36f, w = 0.4f, tone = 1, sub = "to speak"),
                        DNode("s2", "du sprichst", 0.74f, 0.36f, w = 0.44f),
                        DNode("n", "nehmen", 0.24f, 0.6f, w = 0.4f, tone = 1, sub = "to take"),
                        DNode("n2", "du nimmst", 0.74f, 0.6f, w = 0.44f),
                        DNode("f", "fahren", 0.24f, 0.84f, w = 0.4f, tone = 1, sub = "to drive"),
                        DNode("f2", "du fährst", 0.74f, 0.84f, w = 0.44f),
                    ),
                    edges = listOf(
                        DEdge("e", "e2"),
                        DEdge("s", "s2"),
                        DEdge("n", "n2"),
                        DEdge("f", "f2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, light = setOf("e2", "s2", "n2", "f2"), card = BoardCard("Du isst. Du sprichst.", "You eat. You speak. Du nimmst, du fährst: you take, you drive.", phrase = true)),
                    Beat(28, light = setOf("e", "e2"), pulse = "e" to "e2", card = BoardCard("du isst, not du esst", "The vowel changes for du and for er.")),
                ),
            ),
            // 29: "Yes, that's a classic slip: it's «du isst». Now the last big idea, the..."
            Scene(
                29, "nicht or kein",
                DiagramSpec(
                    "One question decides it: saying no to a noun with ein or no article takes kein; nicht does everything else.",
                    nodes = listOf(
                        DNode("q", "A noun with ein, or no article?", 0.5f, 0.12f, w = 0.86f, h = 0.18f),
                        DNode("kein", "kein", 0.25f, 0.44f, w = 0.42f, h = 0.2f, tone = 1, sub = "keine Zeit, kein Auto"),
                        DNode("nicht", "nicht", 0.75f, 0.44f, w = 0.42f, h = 0.2f, tone = 1, sub = "everything else"),
                        DNode("km", "keinen Bruder", 0.25f, 0.8f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "masculine object"),
                        DNode("nn", "nicht mein Buch", 0.75f, 0.8f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "verb, adjective, mein"),
                    ),
                    edges = listOf(
                        DEdge("q", "kein", "yes"),
                        DEdge("q", "nicht", "no"),
                        DEdge("kein", "km", dashed = true),
                        DEdge("nicht", "nn", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("q", "kein", "nicht"), light = setOf("q")),
                    Beat(30, light = setOf("kein"), pulse = "q" to "kein", card = BoardCard("Ich habe keine Zeit.", "I have no time. Ich habe kein Auto: I don't have a car.", phrase = true)),
                    Beat(32, show = setOf("km"), light = setOf("km"), pulse = "kein" to "km"),
                    Beat(33, light = setOf("km"), card = BoardCard("Ich habe keinen Bruder.", "I have no brother. Ich trinke keinen Kaffee: I don't drink coffee.", phrase = true)),
                    Beat(35, light = setOf("nicht"), pulse = "q" to "nicht", card = BoardCard("nicht: everything else", "A verb, an adjective, or a noun with der, die, das or mein.")),
                    Beat(36, light = setOf("nicht"), card = BoardCard("Ich arbeite heute nicht.", "I'm not working today. Der Kaffee ist nicht heiß: the coffee isn't hot.", phrase = true)),
                    Beat(38, show = setOf("nn"), light = setOf("nn"), pulse = "nicht" to "nn", card = BoardCard("Where nicht stands", "At the end for the whole sentence; just before an adjective or mein.")),
                    Beat(39, card = BoardCard("Ich verstehe das nicht.", "I don't understand that. Das ist nicht mein Buch: that's not my book.", phrase = true)),
                ),
            ),
            // 40: "So what should I remember for the Goethe exam?"
            Scene(
                40, "For the Goethe exam",
                DiagramSpec(
                    "Articles in the Goethe A1: check masculine objects in Lesen and Schreiben, and kein in Sprechen.",
                    nodes = listOf(
                        DNode("lw", "Lesen, Schreiben", 0.26f, 0.18f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "einen, den, keinen"),
                        DNode("sp", "Sprechen", 0.76f, 0.18f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "kein before a noun"),
                        DNode("art", "Artikel", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "learn it with every noun"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(41, show = setOf("lw", "sp", "art"), card = BoardCard("Check the masculine", "Feminine and neuter don't move. Only einen, den, keinen.")),
                    Beat(43, card = BoardCard("Today", "Five sentences with a masculine object; say no to three with nicht or kein.")),
                    Beat(44, card = BoardCard("Gut gemacht! Bis zum nächsten Mal.", "Well done! Until next time.", phrase = true)),
                ),
            ),
        ),
    )

    private fun de4Dialogue() = PodcastBoard(
        "de4-dialogue",
        listOf(
            // 0: "Welcome back. Today we go shopping for real: two short scenes you'll h..."
            Scene(
                0, "Two scenes",
                DiagramSpec(
                    "Two everyday scenes: Usaid at the bakery and Sanya in the café. Jonas plays everyone.",
                    nodes = listOf(
                        DNode("bak", "die Bäckerei", 0.26f, 0.24f, w = 0.44f, h = 0.22f, tone = 1, sub = "Usaid buys bread"),
                        DNode("cafe", "das Café", 0.76f, 0.24f, w = 0.44f, h = 0.22f, tone = 1, sub = "Sanya orders tea"),
                        DNode("jonas", "Jonas", 0.5f, 0.72f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "plays everyone"),
                    ),
                    edges = listOf(
                        DEdge("jonas", "bak", dashed = true),
                        DEdge("jonas", "cafe", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("bak", "cafe")),
                    Beat(1, show = setOf("jonas"), light = setOf("jonas"), card = BoardCard("Copy the customer", "Jonas plays the shop and the customer. You say the customer's lines.")),
                ),
            ),
            // 2: "All of them. We start at the bakery, early in the morning. Usaid walks..."
            Scene(
                2, "At the bakery",
                DiagramSpec(
                    "The shop asks two questions: Was darf es sein? to start, and Sonst noch etwas? for anything else.",
                    nodes = listOf(
                        DNode("v1", "Was darf es sein?", 0.27f, 0.12f, w = 0.5f, h = 0.18f, tone = 1, sub = "the shop"),
                        DNode("u1", "Ich hätte gern", 0.76f, 0.34f, w = 0.46f, h = 0.18f, sub = "zwei Brötchen, ein Brot"),
                        DNode("v2", "Sonst noch etwas?", 0.27f, 0.56f, w = 0.5f, h = 0.18f, tone = 1, sub = "anything else?"),
                        DNode("u2", "Nein, danke.", 0.76f, 0.78f, w = 0.46f, h = 0.18f, sub = "Was kostet das?"),
                    ),
                    edges = listOf(
                        DEdge("v1", "u1"),
                        DEdge("u1", "v2"),
                        DEdge("v2", "u2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("v1"), light = setOf("v1"), card = BoardCard("Guten Morgen! Was darf es sein?", "Good morning! What can I get you?", phrase = true)),
                    Beat(5, light = setOf("v1"), card = BoardCard("das Brötchen, die Brötchen", "A bread roll. The plural looks just the same.")),
                    Beat(6, show = setOf("u1"), light = setOf("u1"), pulse = "v1" to "u1", card = BoardCard("Ich hätte gern zwei Brötchen.", "I'd like two bread rolls. Und ein Brot: and a loaf.", phrase = true)),
                    Beat(8, light = setOf("u1"), card = BoardCard("ein Brot, not einen", "Brot is das, so it stays ein. Only masculine words change.")),
                    Beat(9, show = setOf("v2"), light = setOf("v2"), pulse = "u1" to "v2", card = BoardCard("Gern. Sonst noch etwas?", "Certainly. Anything else?", phrase = true)),
                    Beat(12, show = setOf("u2"), light = setOf("u2"), pulse = "v2" to "u2", card = BoardCard("Nein, danke. Was kostet das?", "No, thanks. What does that cost?", phrase = true)),
                ),
            ),
            // 13: "And now comes the scary number."
            Scene(
                13, "Das macht …",
                DiagramSpec(
                    "Das macht gives you the total: euros first, then cents. Then here you are, and goodbye.",
                    nodes = listOf(
                        DNode("tot", "Das macht", 0.5f, 0.14f, w = 0.6f, h = 0.2f, tone = 1, sub = "that comes to"),
                        DNode("price", "vier Euro zwanzig", 0.5f, 0.44f, w = 0.7f, h = 0.2f, sub = "4,20 €"),
                        DNode("bye", "Hier, bitte. Auf Wiedersehen.", 0.5f, 0.78f, w = 0.9f, h = 0.2f, kind = NodeKind.NOTE, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("tot", "price"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("tot"), light = setOf("tot")),
                    Beat(15, show = setOf("price"), light = setOf("price"), pulse = "tot" to "price", card = BoardCard("Das macht vier Euro zwanzig.", "That comes to four euros twenty.", phrase = true)),
                    Beat(18, show = setOf("bye"), light = setOf("bye"), card = BoardCard("Hier, bitte. Auf Wiedersehen.", "Here you are. Goodbye.", phrase = true)),
                ),
            ),
            // 19: "Nice and short. Now the café?"
            Scene(
                19, "In the café",
                DiagramSpec(
                    "Ich hätte gern and Ich möchte are the polite ways to order; both take the accusative.",
                    nodes = listOf(
                        DNode("k", "Was möchten Sie trinken?", 0.5f, 0.1f, w = 0.9f, tone = 1),
                        DNode("a", "Ich hätte gern", 0.26f, 0.38f, w = 0.46f, h = 0.18f),
                        DNode("b", "Ich möchte", 0.26f, 0.64f, w = 0.46f, h = 0.18f),
                        DNode("o", "einen Tee", 0.77f, 0.51f, w = 0.42f, h = 0.22f, tone = 1, sub = "accusative object"),
                        DNode("will", "Ich will", 0.77f, 0.87f, w = 0.42f, kind = NodeKind.NOTE, tone = 2, sub = "rude"),
                    ),
                    edges = listOf(
                        DEdge("a", "o"),
                        DEdge("b", "o"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("k"), light = setOf("k"), card = BoardCard("Was möchten Sie trinken?", "What would you like to drink?", phrase = true)),
                    Beat(23, show = setOf("a", "b", "o"), light = setOf("o"), pulse = "b" to "o"),
                    Beat(24, light = setOf("o"), card = BoardCard("Einen Tee mit Milch, bitte.", "A tea with milk, please. Und ein Stück Kuchen: and a piece of cake.", phrase = true)),
                    Beat(26, show = setOf("will"), light = setOf("will"), card = BoardCard("einen Tee, not ein Tee", "Tee is der, so the order takes einen. And Ich will sounds rude.")),
                    Beat(27, light = setOf("b", "o"), pulse = "b" to "o", card = BoardCard("Ich möchte einen Tee, bitte.", "I'd like a tea, please.", phrase = true)),
                    Beat(30, card = BoardCard("Kommt sofort.", "Coming right up.", phrase = true)),
                ),
            ),
            // 31: "Then she eats the cake, and it's time to pay."
            Scene(
                31, "Paying the bill",
                DiagramSpec(
                    "Zusammen oder getrennt? comes when you pay: one payment for the table, or one each.",
                    nodes = listOf(
                        DNode("r", "Die Rechnung, bitte.", 0.5f, 0.12f, w = 0.8f, h = 0.18f, tone = 1),
                        DNode("q", "Zusammen oder getrennt?", 0.5f, 0.44f, w = 0.9f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("z", "zusammen", 0.25f, 0.78f, w = 0.42f, h = 0.2f, tone = 1, sub = "together"),
                        DNode("g", "getrennt", 0.75f, 0.78f, w = 0.42f, h = 0.2f, tone = 1, sub = "separately"),
                    ),
                    edges = listOf(
                        DEdge("r", "q"),
                        DEdge("q", "z"),
                        DEdge("q", "g"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("r"), light = setOf("r"), card = BoardCard("Die Rechnung, bitte.", "The bill, please.", phrase = true)),
                    Beat(35, show = setOf("q"), light = setOf("q"), pulse = "r" to "q", card = BoardCard("Asked when you pay", "Not when you order: one payment for the table, or one each.")),
                    Beat(36, light = setOf("q"), card = BoardCard("Zusammen oder getrennt?", "Together or separately?", phrase = true)),
                    Beat(39, show = setOf("z"), light = setOf("z"), pulse = "q" to "z", card = BoardCard("Zusammen, bitte.", "Together, please.", phrase = true)),
                    Beat(42, show = setOf("g"), light = setOf("g"), pulse = "q" to "g", card = BoardCard("Getrennt, bitte.", "Separately, please.", phrase = true)),
                ),
            ),
            // 43: "So what should I remember for the Goethe exam?"
            Scene(
                43, "For the Goethe exam",
                DiagramSpec(
                    "Shop dialogues in the Goethe A1: the total in Hören, the shop's questions, and polite orders in Sprechen.",
                    nodes = listOf(
                        DNode("hear", "Hören", 0.26f, 0.18f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "Das macht + price"),
                        DNode("qs", "Shop questions", 0.76f, 0.18f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "Sonst noch etwas?"),
                        DNode("speak", "Sprechen", 0.5f, 0.56f, w = 0.6f, h = 0.22f, kind = NodeKind.NOTE, sub = "Ich möchte einen …"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(44, show = setOf("hear", "qs", "speak"), card = BoardCard("Das macht is the total", "Zusammen oder getrennt? comes at the till, not when you order.")),
                    Beat(46, card = BoardCard("Today", "Order a coffee and a cake out loud, then ask for the bill.")),
                    Beat(47, card = BoardCard("Guten Appetit! Auf Wiedersehen.", "Enjoy your meal! Goodbye.", phrase = true)),
                ),
            ),
        ),
    )

    private fun de4Practice() = PodcastBoard(
        "de4-practice",
        listOf(
            // 0: "Welcome back. Today is practice: reading a supermarket notice, and wri..."
            Scene(
                0, "Read, then write",
                DiagramSpec(
                    "One shopping errand, two exam skills: reading a notice and writing a short note.",
                    nodes = listOf(
                        DNode("err", "Shopping errand", 0.5f, 0.16f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL),
                        DNode("read", "Lesen", 0.25f, 0.6f, w = 0.42f, h = 0.24f, tone = 1, sub = "adverts and notices"),
                        DNode("write", "Schreiben", 0.75f, 0.6f, w = 0.42f, h = 0.24f, tone = 1, sub = "a short message"),
                    ),
                    edges = listOf(
                        DEdge("err", "read"),
                        DEdge("err", "write"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("err", "read", "write"), light = setOf("read", "write")),
                ),
            ),
            // 2: "Again. First, the notice on the door of a Rewe supermarket. It starts ..."
            Scene(
                2, "The Rewe notice",
                DiagramSpec(
                    "A notice of offers: read each price as euros, then cents; the comma is the decimal point.",
                    nodes = listOf(
                        DNode("head", "Angebote diese Woche", 0.5f, 0.08f, w = 0.8f, h = 0.14f),
                        DNode("a1", "Äpfel, 1 kg", 0.3f, 0.3f, w = 0.5f, h = 0.14f, tone = 1),
                        DNode("a2", "1,99 €", 0.78f, 0.3f, w = 0.36f, h = 0.14f, tone = 2),
                        DNode("m1", "Milch, 1 l", 0.3f, 0.5f, w = 0.5f, h = 0.14f, tone = 1),
                        DNode("m2", "0,89 €", 0.78f, 0.5f, w = 0.36f, h = 0.14f, tone = 2),
                        DNode("r1", "Reis, 1 kg", 0.3f, 0.7f, w = 0.5f, h = 0.14f, tone = 1),
                        DNode("r2", "2,20 €", 0.78f, 0.7f, w = 0.36f, h = 0.14f, tone = 2),
                        DNode("sum", "2 kg Reis = 4,40 €", 0.5f, 0.9f, w = 0.7f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("a1", "a2"),
                        DEdge("m1", "m2"),
                        DEdge("r1", "r2"),
                        DEdge("r2", "sum", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("head"), light = setOf("head"), card = BoardCard("das Angebot, die Angebote", "A special offer; more than one are Angebote.")),
                    Beat(3, show = setOf("a1", "a2", "m1", "m2"), light = setOf("a1", "m1"), card = BoardCard("Angebote diese Woche.", "Offers this week. Apples, a kilo: 1,99 €. Milk, a litre: 0,89 €.", phrase = true)),
                    Beat(5, light = setOf("a2", "m2"), pulse = "a1" to "a2", card = BoardCard("The comma is the decimal point", "1,99 € is one euro ninety-nine. Under one euro, just say the cents.")),
                    Beat(6, light = setOf("a2", "m2"), card = BoardCard("Ein Euro neunundneunzig.", "One euro ninety-nine. Neunundachtzig Cent: eighty-nine cents.", phrase = true)),
                    Beat(8, show = setOf("r1", "r2"), light = setOf("r1", "r2")),
                    Beat(9, show = setOf("sum"), light = setOf("sum"), pulse = "r2" to "sum", card = BoardCard("Zwei Kilo Reis kosten …", "… vier Euro vierzig: two kilos of rice cost four euros forty.", phrase = true)),
                ),
            ),
            // 11: "Very gentle maths, though. Now the most important line on the notice: ..."
            Scene(
                11, "Open or closed?",
                DiagramSpec(
                    "geöffnet Monday to Saturday, 7 to 22 Uhr; geschlossen on Sunday, the day the notice leaves out.",
                    nodes = listOf(
                        DNode("mo", "Mo", 0.07f, 0.2f, w = 0.12f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("di", "Di", 0.205f, 0.2f, w = 0.12f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("mi", "Mi", 0.34f, 0.2f, w = 0.12f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("do", "Do", 0.475f, 0.2f, w = 0.12f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("fr", "Fr", 0.61f, 0.2f, w = 0.12f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("sa", "Sa", 0.745f, 0.2f, w = 0.12f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("so", "So", 0.88f, 0.2f, w = 0.12f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                        DNode("auf", "geöffnet", 0.36f, 0.6f, w = 0.62f, h = 0.24f, sub = "7 bis 22 Uhr"),
                        DNode("zu", "geschlossen", 0.84f, 0.6f, w = 0.3f, h = 0.24f, tone = 2, sub = "Sonntag"),
                    ),
                    edges = listOf(
                        DEdge("sa", "auf"),
                        DEdge("so", "zu"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("mo", "di", "mi", "do", "fr", "sa", "so")),
                    Beat(12, show = setOf("auf", "zu"), light = setOf("auf", "zu"), card = BoardCard("Sonntag geschlossen.", "Open Monday to Saturday, 7 to 22 Uhr. Closed on Sunday.", phrase = true)),
                    Beat(14, light = setOf("sa", "so", "zu"), pulse = "so" to "zu", card = BoardCard("Check the day left out", "Saturday at nine in the evening: open. Sunday: geschlossen.")),
                ),
            ),
            // 15: "Okay, I can read the notice. Now the note?"
            Scene(
                15, "Four parts of a note",
                DiagramSpec(
                    "The four points the marker looks for: greeting, message, question and sign-off.",
                    nodes = listOf(
                        DNode("p0", "Greeting", 0.5f, 0.12f, w = 0.8f, h = 0.2f, tone = 1, sub = "Hallo Sanya,"),
                        DNode("p1", "Message", 0.5f, 0.37f, w = 0.8f, h = 0.2f, tone = 1, sub = "when and what: einen Tee"),
                        DNode("p2", "Question", 0.5f, 0.62f, w = 0.8f, h = 0.2f, tone = 1, sub = "Was möchtest du?"),
                        DNode("p3", "Sign-off", 0.5f, 0.87f, w = 0.8f, h = 0.2f, tone = 1, sub = "Bis später, Usaid"),
                    ),
                    edges = listOf(
                        DEdge("p0", "p1"),
                        DEdge("p1", "p2"),
                        DEdge("p2", "p3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("p0", "p1", "p2", "p3"), light = setOf("p0")),
                    Beat(17, light = setOf("p0", "p1"), card = BoardCard("Hallo Sanya, ich gehe um fünf Uhr", "Hi Sanya, I'm going to the supermarket at five o'clock.", phrase = true)),
                    Beat(19, light = setOf("p0"), card = BoardCard("Lower case after the comma", "Hallo Sanya, ich gehe … The comma doesn't end the sentence.")),
                    Beat(20, light = setOf("p1"), pulse = "p0" to "p1", card = BoardCard("Ich kaufe einen Tee und ein Brot.", "I'm buying a tea and a loaf of bread.", phrase = true)),
                    Beat(23, light = setOf("p2"), pulse = "p1" to "p2", card = BoardCard("Was möchtest du?", "What would you like?", phrase = true)),
                    Beat(25, light = setOf("p2"), card = BoardCard("The question is marked", "Skip it and you lose a point. brauchen: to need.")),
                    Beat(26, light = setOf("p2"), card = BoardCard("Brauchst du Reis?", "Do you need rice?", phrase = true)),
                    Beat(29, light = setOf("p3"), pulse = "p2" to "p3", card = BoardCard("Bis später, Usaid.", "See you later, Usaid.", phrase = true)),
                ),
            ),
            // 30: "Can we hear the whole note in one go?"
            Scene(
                30, "Two notes",
                DiagramSpec(
                    "Usaid writes, Sanya writes back: the same four parts, and the masculine object takes einen.",
                    nodes = listOf(
                        DNode("u", "Usaid to Sanya", 0.26f, 0.2f, w = 0.46f, h = 0.26f, kind = NodeKind.NOTE, tone = 1, sub = "einen Tee, ein Brot"),
                        DNode("s", "Sanya to Usaid", 0.76f, 0.2f, w = 0.46f, h = 0.26f, kind = NodeKind.NOTE, tone = 1, sub = "einen Apfel, ein Hähnchen"),
                        DNode("m", "der → einen", 0.5f, 0.66f, w = 0.6f, h = 0.22f, sub = "masculine object"),
                    ),
                    edges = listOf(
                        DEdge("u", "m", dashed = true),
                        DEdge("s", "m", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("u"), light = setOf("u"), card = BoardCard("Usaid's note", "Greeting, when and what, a question, a sign-off: four parts.")),
                    Beat(34, show = setOf("s", "m"), light = setOf("m"), pulse = "s" to "m"),
                    Beat(35, light = setOf("s"), card = BoardCard("Ich kaufe einen Apfel.", "I'm buying an apple. Brauchst du Reis? Do you need rice?", phrase = true)),
                ),
            ),
            // 36: "So what should I remember for the Goethe exam?"
            Scene(
                36, "For the Goethe exam",
                DiagramSpec(
                    "For the Goethe A1: notices in Lesen, the four-part note in Schreiben, and the masculine object.",
                    nodes = listOf(
                        DNode("lesen", "Lesen", 0.26f, 0.18f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "prices, hours, geschlossen"),
                        DNode("schr", "Schreiben", 0.76f, 0.18f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "four parts, with a question"),
                        DNode("masc", "einen Tee", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "masculine object"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(37, show = setOf("lesen", "schr", "masc"), card = BoardCard("Check the day left out", "And a note needs a greeting, the message, a question and a sign-off.")),
                    Beat(39, card = BoardCard("Today", "A real shopping list, ten items with articles. Then order a tea out loud.")),
                    Beat(40, card = BoardCard("Viel Erfolg! Bis später!", "Good luck! See you later!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de5Words() = PodcastBoard(
        "de5-words",
        listOf(
            // 0: "Welcome back. Today is the flat, the furniture and the city: where you..."
            Scene(
                0, "Looking for a flat",
                DiagramSpec(
                    "suchen is the long search; finden is the happy ending; mieten is renting it.",
                    nodes = listOf(
                        DNode("w", "die Wohnung", 0.5f, 0.14f, w = 0.5f, sub = "the flat"),
                        DNode("s", "suchen", 0.5f, 0.42f, w = 0.36f, tone = 1, sub = "to look for"),
                        DNode("f", "finden", 0.25f, 0.76f, w = 0.36f, tone = 1, sub = "to find"),
                        DNode("mi", "mieten", 0.75f, 0.76f, w = 0.36f, tone = 1, sub = "to rent"),
                    ),
                    edges = listOf(
                        DEdge("s", "f"),
                        DEdge("s", "mi", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("w")),
                    Beat(1, show = setOf("s"), light = setOf("w", "s"), card = BoardCard("Wir suchen eine Wohnung.", "We are looking for a flat.", phrase = true)),
                    Beat(3, show = setOf("f", "mi"), light = setOf("f"), pulse = "s" to "f", card = BoardCard("suchen, then finden", "Looking takes months; finding is the end.")),
                ),
            ),
            // 4: "Die Wohnung hat drei Zimmer."
            Scene(
                4, "Rooms: mostly das",
                DiagramSpec(
                    "Most rooms are das; the kitchen is die, and outside the balcony and garden are der.",
                    nodes = listOf(
                        DNode("z", "das Zimmer", 0.25f, 0.1f, w = 0.42f, h = 0.14f, sub = "room"),
                        DNode("sz", "das Schlafzimmer", 0.75f, 0.1f, w = 0.46f, h = 0.14f, tone = 1, sub = "bedroom"),
                        DNode("wz", "das Wohnzimmer", 0.25f, 0.36f, w = 0.42f, h = 0.14f, tone = 1, sub = "living room"),
                        DNode("bad", "das Bad", 0.75f, 0.36f, w = 0.42f, h = 0.14f, tone = 1, sub = "bathroom"),
                        DNode("k", "die Küche", 0.5f, 0.62f, w = 0.42f, h = 0.14f, sub = "kitchen"),
                        DNode("bal", "der Balkon", 0.25f, 0.88f, w = 0.42f, h = 0.14f, tone = 2, sub = "balcony"),
                        DNode("gar", "der Garten", 0.75f, 0.88f, w = 0.42f, h = 0.14f, tone = 2, sub = "garden"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("z"), light = setOf("z"), card = BoardCard("Die Wohnung hat drei Zimmer.", "The flat has three rooms.", phrase = true)),
                    Beat(6, show = setOf("sz", "wz", "bad"), light = setOf("sz", "wz", "bad"), card = BoardCard("Most rooms are das", "das Zimmer, das Schlafzimmer, das Wohnzimmer, das Bad.")),
                    Beat(7, light = setOf("sz", "wz"), card = BoardCard("Das Wohnzimmer ist hell.", "The bedroom is quiet (ruhig). The living room is bright (hell).", phrase = true)),
                    Beat(9, show = setOf("k", "bal", "gar"), light = setOf("k", "bal", "gar")),
                    Beat(10, light = setOf("k"), card = BoardCard("Die Küche ist groß und hell.", "The kitchen is big and bright.", phrase = true)),
                ),
            ),
            // 11: "Now the furniture. What goes in the rooms?"
            Scene(
                11, "Furniture by room",
                DiagramSpec(
                    "Each piece of furniture comes with its article: learn them as one piece.",
                    nodes = listOf(
                        DNode("k", "die Küche", 0.18f, 0.12f, w = 0.32f, kind = NodeKind.PILL),
                        DNode("sz", "Schlafzimmer", 0.5f, 0.12f, w = 0.3f, kind = NodeKind.PILL),
                        DNode("wz", "Wohnzimmer", 0.82f, 0.12f, w = 0.3f, kind = NodeKind.PILL),
                        DNode("t", "der Tisch", 0.18f, 0.45f, w = 0.3f, tone = 1, sub = "table"),
                        DNode("st", "der Stuhl", 0.18f, 0.78f, w = 0.3f, tone = 1, sub = "die Stühle"),
                        DNode("b", "das Bett", 0.5f, 0.45f, w = 0.3f, tone = 1, sub = "bed"),
                        DNode("sch", "der Schrank", 0.5f, 0.78f, w = 0.3f, tone = 1, sub = "wardrobe"),
                        DNode("so", "das Sofa", 0.82f, 0.45f, w = 0.3f, tone = 1, sub = "sofa"),
                        DNode("la", "die Lampe", 0.82f, 0.78f, w = 0.3f, tone = 1, sub = "lamp"),
                    ),
                    edges = listOf(
                        DEdge("k", "t"),
                        DEdge("t", "st"),
                        DEdge("sz", "b"),
                        DEdge("b", "sch"),
                        DEdge("wz", "so"),
                        DEdge("so", "la"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("k", "sz", "wz")),
                    Beat(12, show = setOf("t", "st", "b", "sch", "so", "la")),
                    Beat(13, light = setOf("k", "t", "st"), pulse = "k" to "t", card = BoardCard("Der Tisch steht in der Küche.", "The table is in the kitchen. We need four chairs.", phrase = true)),
                    Beat(15, light = setOf("st"), card = BoardCard("der Stuhl, die Stühle", "One chair, four chairs: the vowel gets dots.")),
                    Beat(16, light = setOf("sz", "b", "sch"), pulse = "sz" to "b", card = BoardCard("Der Schrank ist im Schlafzimmer.", "The wardrobe is in the bedroom. And: Das Bett ist neu, the bed is new.", phrase = true)),
                ),
            ),
            // 17: "Okay, I've found a flat online. But the advert looks like a secret cod..."
            Scene(
                17, "Reading a flat advert",
                DiagramSpec(
                    "A flat advert reads in a fixed order: rooms, size, floor, rent plus bills.",
                    nodes = listOf(
                        DNode("zi", "3 Zi.", 0.27f, 0.12f, w = 0.44f, tone = 1, sub = "drei Zimmer"),
                        DNode("qm", "75 qm", 0.75f, 0.12f, w = 0.42f, tone = 1, sub = "Quadratmeter"),
                        DNode("og", "2. OG", 0.27f, 0.42f, w = 0.44f, tone = 1, sub = "Obergeschoss"),
                        DNode("nk", "650 Euro + NK", 0.75f, 0.42f, w = 0.42f, sub = "plus Nebenkosten"),
                        DNode("eg", "EG", 0.27f, 0.74f, w = 0.44f, kind = NodeKind.NOTE, tone = 2, sub = "Erdgeschoss: ground"),
                        DNode("trap", "Zi. counts", 0.75f, 0.74f, w = 0.42f, kind = NodeKind.NOTE, tone = 2, sub = "rooms you live in"),
                    ),
                    edges = listOf(
                        DEdge("zi", "qm"),
                        DEdge("qm", "og"),
                        DEdge("og", "nk"),
                        DEdge("og", "eg", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("zi", "qm", "og", "nk")),
                    Beat(19, light = setOf("zi", "qm", "og", "nk"), card = BoardCard("Drei Zimmer, 75 Quadratmeter", "Three rooms, 75 square metres, second floor, 650 euros plus bills.", phrase = true)),
                    Beat(20, light = setOf("nk"), card = BoardCard("+ NK", "Nebenkosten: the bills come on top of the rent.")),
                    Beat(21, show = setOf("eg", "trap"), light = setOf("eg", "og"), pulse = "og" to "eg", card = BoardCard("EG, then 1. OG", "The ground floor is EG, so the first OG is one flight up.")),
                    Beat(22, light = setOf("nk"), card = BoardCard("Die Miete ist sechshundert Euro.", "The rent is six hundred euros.", phrase = true)),
                ),
            ),
            // 23: "Adverts always sound lovely. Which words do they use?"
            Scene(
                23, "Advert words",
                DiagramSpec(
                    "Adverts reach for the same few adjectives; no advert prints teuer.",
                    nodes = listOf(
                        DNode("hell", "hell", 0.27f, 0.14f, w = 0.4f, h = 0.18f, sub = "bright"),
                        DNode("ruhig", "ruhig", 0.75f, 0.14f, w = 0.4f, h = 0.18f, sub = "quiet"),
                        DNode("gross", "groß", 0.27f, 0.48f, w = 0.4f, h = 0.18f, tone = 1, sub = "big"),
                        DNode("klein", "klein", 0.75f, 0.48f, w = 0.4f, h = 0.18f, tone = 1, sub = "small"),
                        DNode("teuer", "teuer", 0.27f, 0.82f, w = 0.4f, h = 0.18f, tone = 2, sub = "expensive"),
                        DNode("billig", "billig", 0.75f, 0.82f, w = 0.4f, h = 0.18f, tone = 2, sub = "cheap"),
                    ),
                    edges = listOf(
                        DEdge("gross", "klein", "or"),
                        DEdge("teuer", "billig", "or"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("hell", "ruhig", "gross", "klein", "teuer", "billig"), light = setOf("hell", "ruhig")),
                    Beat(25, light = setOf("hell", "ruhig", "teuer"), card = BoardCard("Hell und ruhig, aber zu teuer.", "The room is bright and quiet, but too expensive.", phrase = true)),
                ),
            ),
            // 26: "Story of my life. Now outside. How do I find the station?"
            Scene(
                26, "In the city",
                DiagramSpec(
                    "Asking the way: listen for links, rechts, geradeaus and a landmark.",
                    nodes = listOf(
                        DNode("you", "You", 0.5f, 0.88f, w = 0.2f, h = 0.18f, kind = NodeKind.USER),
                        DNode("gerade", "geradeaus", 0.5f, 0.5f, w = 0.34f, sub = "straight ahead"),
                        DNode("links", "links", 0.16f, 0.5f, tone = 1, sub = "left"),
                        DNode("rechts", "rechts", 0.84f, 0.5f, tone = 1, sub = "right"),
                        DNode("bhf", "der Bahnhof", 0.5f, 0.12f, w = 0.36f, sub = "station"),
                        DNode("apo", "die Apotheke", 0.16f, 0.2f, w = 0.3f, kind = NodeKind.NOTE, tone = 2, sub = "an der Ecke"),
                        DNode("post", "die Post", 0.84f, 0.2f, w = 0.3f, kind = NodeKind.NOTE, tone = 2, sub = "post office"),
                    ),
                    edges = listOf(
                        DEdge("you", "gerade"),
                        DEdge("gerade", "bhf"),
                        DEdge("gerade", "links"),
                        DEdge("gerade", "rechts"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("you")),
                    Beat(27, show = setOf("bhf", "apo", "post", "links", "rechts", "gerade"), light = setOf("links", "rechts", "gerade")),
                    Beat(28, light = setOf("bhf"), card = BoardCard("Entschuldigung, wo ist der Bahnhof?", "Excuse me, where is the station?", phrase = true)),
                    Beat(31, light = setOf("post", "apo", "gerade"), pulse = "you" to "gerade", card = BoardCard("Die Post ist geradeaus.", "The post office is straight ahead. The pharmacy is round the corner.", phrase = true)),
                    Beat(34, light = setOf("bhf"), pulse = "gerade" to "bhf", card = BoardCard("Ist das weit?", "Is it far? And: Nein, der Bahnhof ist nah. No, the station is near.", phrase = true)),
                ),
            ),
            // 35: "So what should I remember for the Goethe exam?"
            Scene(
                35, "For the Goethe exam",
                DiagramSpec(
                    "What these words mean for the Goethe A1: adverts in Lesen, directions in Hören, articles.",
                    nodes = listOf(
                        DNode("lesen", "Lesen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Zi., qm, OG, + NK"),
                        DNode("hoeren", "Hören", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "links, rechts, geradeaus"),
                        DNode("art", "Rooms with articles", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "most rooms are das"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("lesen", "hoeren", "art"), card = BoardCard("Plus NK", "The advertised rent is not what you pay: bills come on top.")),
                    Beat(38, card = BoardCard("Today", "Read three flat adverts: Zi., OG, NK. Name four things in your room.")),
                    Beat(39, card = BoardCard("Tschüss! Bis bald!", "Bye! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de5Grammar() = PodcastBoard(
        "de5-grammar",
        listOf(
            // 0: "Welcome back. Today is grammar, but two small, friendly tools: «es gib..."
            Scene(
                0, "es gibt + accusative",
                DiagramSpec(
                    "es gibt never changes; the thing that exists is in the accusative, so a masculine takes einen.",
                    nodes = listOf(
                        DNode("eg", "es gibt", 0.5f, 0.1f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, sub = "there is, there are"),
                        DNode("m", "einen Park", 0.2f, 0.42f, w = 0.36f, sub = "der: ein becomes einen"),
                        DNode("f", "eine Küche", 0.5f, 0.66f, w = 0.3f, tone = 1, sub = "die: stays"),
                        DNode("nn", "ein Bad", 0.8f, 0.42f, w = 0.36f, tone = 1, sub = "das: stays"),
                        DNode("p", "drei Zimmer", 0.2f, 0.88f, w = 0.36f, h = 0.14f, tone = 2, sub = "plural: no article"),
                        DNode("k", "keinen Balkon", 0.8f, 0.88f, w = 0.36f, h = 0.14f, tone = 2, sub = "kein like ein"),
                    ),
                    edges = listOf(
                        DEdge("eg", "m"),
                        DEdge("eg", "f"),
                        DEdge("eg", "nn"),
                        DEdge("eg", "p", dashed = true),
                        DEdge("eg", "k", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("eg")),
                    Beat(1, show = setOf("m"), light = setOf("m"), pulse = "eg" to "m", card = BoardCard("Es gibt einen Park.", "There is a park.", phrase = true)),
                    Beat(3, light = setOf("m"), card = BoardCard("Only the masculine changes", "es gibt + accusative: ein Park becomes einen Park.")),
                    Beat(4, show = setOf("f", "nn"), light = setOf("f", "nn"), card = BoardCard("Es gibt eine Küche. Es gibt ein Bad.", "There is a kitchen. There is a bathroom.", phrase = true)),
                    Beat(7, show = setOf("p"), light = setOf("p"), pulse = "eg" to "p", card = BoardCard("Es gibt drei Zimmer.", "There are three rooms.", phrase = true)),
                    Beat(10, show = setOf("k"), light = setOf("k"), pulse = "eg" to "k", card = BoardCard("Es gibt keinen Balkon.", "There is no balcony, but there is a garden (aber es gibt einen Garten).", phrase = true)),
                ),
            ),
            // 11: "Okay, I know what's in the flat. How do I say where things are?"
            Scene(
                11, "Wo? Pick, then switch",
                DiagramSpec(
                    "Wo ist die Lampe? Pick the position word, then switch the article to the dative.",
                    nodes = listOf(
                        DNode("q", "Wo?", 0.5f, 0.12f, w = 0.3f, kind = NodeKind.PILL, sub = "where"),
                        DNode("p", "in, an, auf", 0.5f, 0.44f, w = 0.5f, h = 0.18f, tone = 1, sub = "neben, unter"),
                        DNode("d", "Dativ", 0.5f, 0.78f, w = 0.5f, h = 0.18f, sub = "the article's other coat"),
                    ),
                    edges = listOf(
                        DEdge("q", "p"),
                        DEdge("p", "d"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("q"), light = setOf("q")),
                    Beat(12, show = setOf("p", "d"), light = setOf("p", "d"), pulse = "p" to "d", card = BoardCard("Wo? takes the Dativ", "in, an, auf, neben, unter: the article puts on its dative coat.")),
                    Beat(13, light = setOf("d")),
                ),
            ),
            // 14: "Three to learn. «der» and «das» both become «dem». «die» becomes «der»..."
            Scene(
                14, "The dative coats",
                DiagramSpec(
                    "In the dative der and das become dem, die becomes der, and the plural becomes den plus n.",
                    nodes = listOf(
                        DNode("der", "der Tisch", 0.24f, 0.12f, w = 0.38f, h = 0.15f, tone = 1),
                        DNode("dem1", "auf dem Tisch", 0.76f, 0.12f, w = 0.4f, h = 0.15f),
                        DNode("die", "die Küche", 0.24f, 0.37f, w = 0.38f, h = 0.15f, tone = 1),
                        DNode("der2", "in der Küche", 0.76f, 0.37f, w = 0.4f, h = 0.15f),
                        DNode("das", "das Bett", 0.24f, 0.62f, w = 0.38f, h = 0.15f, tone = 1),
                        DNode("dem2", "unter dem Bett", 0.76f, 0.62f, w = 0.4f, h = 0.15f),
                        DNode("pl", "die Stühle", 0.24f, 0.87f, w = 0.38f, h = 0.15f, tone = 1),
                        DNode("den", "auf den Stühlen", 0.76f, 0.87f, w = 0.4f, h = 0.15f),
                    ),
                    edges = listOf(
                        DEdge("der", "dem1"),
                        DEdge("die", "der2"),
                        DEdge("das", "dem2"),
                        DEdge("pl", "den"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("der", "die", "das", "pl"), light = setOf("der")),
                    Beat(15, show = setOf("dem1"), light = setOf("dem1"), pulse = "der" to "dem1", card = BoardCard("Die Lampe ist auf dem Tisch.", "The lamp is on the table.", phrase = true)),
                    Beat(16, light = setOf("die")),
                    Beat(18, show = setOf("der2"), light = setOf("der2"), pulse = "die" to "der2", card = BoardCard("Wir essen in der Küche.", "We eat in the kitchen.", phrase = true)),
                    Beat(20, light = setOf("das")),
                    Beat(21, show = setOf("dem2"), light = setOf("dem2"), pulse = "das" to "dem2", card = BoardCard("Die Katze schläft unter dem Bett.", "The cat sleeps under the bed.", phrase = true)),
                    Beat(23, light = setOf("pl"), card = BoardCard("den + n", "die Stühle becomes den Stühlen: don't lose the n.")),
                    Beat(24, show = setOf("den"), light = setOf("den"), pulse = "pl" to "den", card = BoardCard("Die Kinder sitzen auf den Stühlen.", "The children are sitting on the chairs.", phrase = true)),
                ),
            ),
            // 25: "Wait. So I'd say «in dem Zentrum»? That's a mouthful."
            Scene(
                25, "Glued together",
                DiagramSpec(
                    "A Kontraktion glues the position word to the article: in dem is im, an dem is am.",
                    nodes = listOf(
                        DNode("indem", "in dem", 0.25f, 0.2f, w = 0.36f, h = 0.18f, tone = 2),
                        DNode("im", "im", 0.75f, 0.2f, w = 0.3f, h = 0.18f, sub = "im Zentrum"),
                        DNode("andem", "an dem", 0.25f, 0.56f, w = 0.36f, h = 0.18f, tone = 2),
                        DNode("am", "am", 0.75f, 0.56f, w = 0.3f, h = 0.18f, sub = "am Bahnhof"),
                        DNode("note", "The exam writes them glued", 0.5f, 0.86f, w = 0.7f, kind = NodeKind.NOTE, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("indem", "im"),
                        DEdge("andem", "am"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("indem"), light = setOf("indem")),
                    Beat(26, show = setOf("im", "andem", "am", "note"), light = setOf("im", "am"), pulse = "indem" to "im", card = BoardCard("Kontraktion", "in dem = im, an dem = am. Learn both forms.")),
                    Beat(27, light = setOf("im", "am"), pulse = "andem" to "am", card = BoardCard("Wir wohnen im Zentrum.", "We live in the centre. And: Der Tisch steht am Fenster, by the window.", phrase = true)),
                ),
            ),
            // 28: "You said «steht». The table stands?"
            Scene(
                28, "stehen or liegen",
                DiagramSpec(
                    "Upright things stehen and flat things liegen; neben, next to, also takes the dative.",
                    nodes = listOf(
                        DNode("st", "stehen", 0.27f, 0.18f, w = 0.4f, h = 0.18f, sub = "upright"),
                        DNode("li", "liegen", 0.75f, 0.18f, w = 0.4f, h = 0.18f, sub = "flat"),
                        DNode("t", "der Tisch", 0.27f, 0.52f, w = 0.4f, tone = 1, sub = "der Stuhl"),
                        DNode("b", "das Buch", 0.75f, 0.52f, w = 0.4f, tone = 1, sub = "das Handy"),
                        DNode("ne", "neben der Post", 0.5f, 0.84f, w = 0.5f, kind = NodeKind.NOTE, tone = 2, sub = "next to: dative"),
                    ),
                    edges = listOf(
                        DEdge("st", "t"),
                        DEdge("li", "b"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("st", "t"), light = setOf("st")),
                    Beat(29, show = setOf("li", "b"), light = setOf("st", "li"), pulse = "li" to "b"),
                    Beat(30, show = setOf("ne"), light = setOf("b", "ne"), card = BoardCard("Das Buch liegt auf dem Tisch.", "The book is lying on the table. And: Die Bank ist neben der Post.", phrase = true)),
                ),
            ),
            // 31: "And that other question you promised? Where to?"
            Scene(
                31, "Wo? or Wohin?",
                DiagramSpec(
                    "Wo? is where something is, with the dative; Wohin? is where to, movement, with the accusative.",
                    nodes = listOf(
                        DNode("wo", "Wo?", 0.25f, 0.16f, w = 0.36f, h = 0.18f, kind = NodeKind.PILL, sub = "no movement"),
                        DNode("wohin", "Wohin?", 0.75f, 0.16f, w = 0.36f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "movement"),
                        DNode("im", "im Kino", 0.25f, 0.52f, w = 0.36f, h = 0.18f, sub = "dative"),
                        DNode("ins", "ins Kino", 0.75f, 0.52f, w = 0.36f, h = 0.18f, tone = 1, sub = "accusative"),
                    ),
                    edges = listOf(
                        DEdge("wo", "im"),
                        DEdge("wohin", "ins"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("wo", "wohin"), light = setOf("wohin")),
                    Beat(32, show = setOf("im", "ins"), light = setOf("im", "ins"), pulse = "wohin" to "ins"),
                    Beat(33, light = setOf("im", "ins"), card = BoardCard("Ich bin im Kino. Ich gehe ins Kino.", "I'm at the cinema. I'm going to the cinema.", phrase = true)),
                ),
            ),
            // 34: "So what should I remember for the Goethe exam?"
            Scene(
                34, "For the Goethe exam",
                DiagramSpec(
                    "What es gibt and Wo? mean for the Goethe A1: reading, writing and the dative.",
                    nodes = listOf(
                        DNode("lesen", "Lesen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "im = in dem, am = an dem"),
                        DNode("schreiben", "Schreiben", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "es gibt einen Park"),
                        DNode("wo", "Wo? means Dativ", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "dem, der, dem, den"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(35, show = setOf("lesen", "schreiben", "wo"), card = BoardCard("Two tools", "es gibt + einen; Wo? + dem, der, dem, den.")),
                    Beat(37, card = BoardCard("Today", "Walk round your home: five es gibt, five dative sentences, im and am.")),
                    Beat(38, card = BoardCard("Bis zum nächsten Mal! Tschüss!", "Until next time! Bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de5Dialogue() = PodcastBoard(
        "de5-dialogue",
        listOf(
            // 0: "Welcome back. Today is two short conversations, the kind you hear in t..."
            Scene(
                0, "Two short dialogues",
                DiagramSpec(
                    "Two Hören-style conversations: a phone call with the Vermieter, and asking a Passant the way.",
                    nodes = listOf(
                        DNode("usaid", "Usaid", 0.14f, 0.24f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
                        DNode("weber", "Herr Weber", 0.7f, 0.24f, w = 0.44f, h = 0.18f, tone = 1, sub = "der Vermieter"),
                        DNode("sanya", "Sanya", 0.14f, 0.72f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
                        DNode("pass", "der Passant", 0.7f, 0.72f, w = 0.44f, h = 0.18f, tone = 1, sub = "passer-by"),
                    ),
                    edges = listOf(
                        DEdge("usaid", "weber", "phone call"),
                        DEdge("sanya", "pass", "the way", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("usaid", "sanya", "weber", "pass")),
                    Beat(2, light = setOf("weber"), pulse = "usaid" to "weber", card = BoardCard("der Vermieter", "The landlord: he answers with his family name.")),
                    Beat(3, light = setOf("weber"), card = BoardCard("Weber, guten Tag.", "Weber, good afternoon.", phrase = true)),
                ),
            ),
            // 4: "Just his surname? No hello, this is?"
            Scene(
                4, "Why you're calling",
                DiagramSpec(
                    "anrufen splits: rufe goes second, an jumps to the end, and the reason sits in the middle.",
                    nodes = listOf(
                        DNode("ich", "Ich rufe", 0.2f, 0.2f, w = 0.32f, h = 0.18f, sub = "verb second"),
                        DNode("weg", "wegen der Wohnung", 0.5f, 0.52f, w = 0.5f, h = 0.18f, tone = 1, sub = "the reason"),
                        DNode("an", "an.", 0.8f, 0.2f, w = 0.3f, h = 0.18f, sub = "at the end"),
                        DNode("frei", "noch frei?", 0.5f, 0.84f, w = 0.4f, kind = NodeKind.NOTE, tone = 2, sub = "still free?"),
                    ),
                    edges = listOf(
                        DEdge("ich", "weg"),
                        DEdge("weg", "an"),
                        DEdge("weg", "frei", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("weg"), light = setOf("weg"), card = BoardCard("wegen", "About, because of: wegen der Wohnung, about the flat.")),
                    Beat(6, show = setOf("ich", "an"), light = setOf("ich", "an"), card = BoardCard("Ich rufe wegen der Wohnung an.", "I'm calling about the flat.", phrase = true)),
                    Beat(8, light = setOf("ich", "an"), pulse = "ich" to "weg", card = BoardCard("anrufen splits", "rufe second, an at the end: a sandwich with the reason inside.")),
                    Beat(9, show = setOf("frei"), light = setOf("frei"), pulse = "weg" to "frei", card = BoardCard("Ist die Wohnung noch frei?", "Is the flat still free?", phrase = true)),
                ),
            ),
            // 10: "And what does Herr Weber say?"
            Scene(
                10, "What the flat has",
                DiagramSpec(
                    "Listen for what there is and what there isn't, and for the floor.",
                    nodes = listOf(
                        DNode("zi", "drei Zimmer", 0.27f, 0.12f, w = 0.42f, tone = 1),
                        DNode("k", "eine Küche", 0.75f, 0.12f, w = 0.42f, tone = 1),
                        DNode("bad", "ein Bad", 0.27f, 0.4f, w = 0.42f, tone = 1),
                        DNode("bal", "keinen Balkon", 0.75f, 0.4f, w = 0.42f, tone = 2, sub = "es gibt keinen"),
                        DNode("gar", "einen Garten", 0.27f, 0.68f, w = 0.42f, sub = "klein"),
                        DNode("st", "im zweiten Stock", 0.5f, 0.9f, w = 0.56f, kind = NodeKind.NOTE, sub = "two flights up"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("zi", "k", "bad"), light = setOf("zi"), card = BoardCard("Sie hat drei Zimmer.", "It has three rooms, a kitchen and a bathroom.", phrase = true)),
                    Beat(15, show = setOf("bal"), light = setOf("bal"), card = BoardCard("Gibt es einen Balkon?", "Is there a balcony?", phrase = true)),
                    Beat(17, show = setOf("gar", "st"), light = setOf("gar", "st")),
                    Beat(18, light = setOf("st"), card = BoardCard("Die Wohnung ist im zweiten Stock.", "The flat is on the second floor. And: es gibt einen kleinen Garten.", phrase = true)),
                ),
            ),
            // 19: "Okay, the big question: the rent. Do I say «Wie viel ist die Miete»?"
            Scene(
                19, "Rent and a viewing",
                DiagramSpec(
                    "Rent is hoch, plus Nebenkosten on top; then ask to see it and propose a day.",
                    nodes = listOf(
                        DNode("q", "Wie hoch?", 0.25f, 0.12f, w = 0.4f, kind = NodeKind.PILL, sub = "not wie viel"),
                        DNode("r", "650 Euro", 0.25f, 0.44f, w = 0.4f, tone = 1, sub = "die Miete"),
                        DNode("nk", "+ Nebenkosten", 0.25f, 0.78f, w = 0.4f, h = 0.18f, sub = "you pay more"),
                        DNode("see", "Kann ich sehen?", 0.75f, 0.12f, w = 0.4f, kind = NodeKind.PILL, tone = 1, sub = "ask to see it"),
                        DNode("sa", "Samstag, elf Uhr", 0.75f, 0.44f, w = 0.4f, tone = 1, sub = "propose a day"),
                        DNode("ad", "Gartenstraße 12", 0.75f, 0.78f, w = 0.4f, h = 0.18f, tone = 2, sub = "die Adresse"),
                    ),
                    edges = listOf(
                        DEdge("q", "r"),
                        DEdge("r", "nk"),
                        DEdge("see", "sa"),
                        DEdge("sa", "ad"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(19, show = setOf("q"), light = setOf("q")),
                    Beat(21, light = setOf("q"), card = BoardCard("Wie hoch ist die Miete?", "How much is the rent?", phrase = true)),
                    Beat(24, show = setOf("r", "nk"), light = setOf("r", "nk"), pulse = "r" to "nk", card = BoardCard("650 Euro, plus Nebenkosten.", "Six hundred and fifty euros, plus bills.", phrase = true)),
                    Beat(26, light = setOf("nk"), card = BoardCard("plus Nebenkosten", "Heating, water and rubbish come on top of every figure.")),
                    Beat(27, show = setOf("see"), light = setOf("see"), card = BoardCard("Kann ich die Wohnung sehen?", "Can I see the flat? Perhaps on Saturday? (Vielleicht am Samstag?)", phrase = true)),
                    Beat(30, show = setOf("sa", "ad"), light = setOf("sa", "ad"), pulse = "see" to "sa", card = BoardCard("Ja, Samstag um elf Uhr.", "Yes, Saturday at eleven. The address is Gartenstraße twelve.", phrase = true)),
                ),
            ),
            // 31: "Now the street. Is Sanya lost?"
            Scene(
                31, "Asking the way",
                DiagramSpec(
                    "Asking the way: listen for geradeaus, the second street links, and a landmark.",
                    nodes = listOf(
                        DNode("you", "Sanya", 0.5f, 0.88f, w = 0.2f, h = 0.18f, kind = NodeKind.USER),
                        DNode("ger", "geradeaus", 0.5f, 0.56f, w = 0.34f, tone = 1, sub = "straight on"),
                        DNode("li", "zweite Straße links", 0.24f, 0.3f, w = 0.44f, tone = 1, sub = "second left"),
                        DNode("bhf", "der Bahnhof", 0.24f, 0.08f, w = 0.4f, h = 0.14f),
                        DNode("post", "neben der Post", 0.72f, 0.08f, w = 0.48f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("fuss", "fünf Minuten zu Fuß", 0.76f, 0.3f, w = 0.44f, kind = NodeKind.NOTE, tone = 2, sub = "not far"),
                    ),
                    edges = listOf(
                        DEdge("you", "ger"),
                        DEdge("ger", "li"),
                        DEdge("li", "bhf"),
                        DEdge("bhf", "post", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("you")),
                    Beat(32, light = setOf("you"), card = BoardCard("der Passant", "A passer-by: one fixed opening, one fixed question.")),
                    Beat(33, show = setOf("bhf"), light = setOf("bhf"), card = BoardCard("Entschuldigung, wo ist der Bahnhof?", "Excuse me, where is the station?", phrase = true)),
                    Beat(35, show = setOf("ger"), light = setOf("ger"), pulse = "you" to "ger"),
                    Beat(36, show = setOf("li"), light = setOf("li"), pulse = "ger" to "li", card = BoardCard("Geradeaus, die zweite Straße links.", "Go straight on here and then the second street on the left.", phrase = true)),
                    Beat(39, show = setOf("post"), light = setOf("bhf", "post"), pulse = "bhf" to "post", card = BoardCard("Der Bahnhof ist neben der Post.", "The station is next to the post office.", phrase = true)),
                    Beat(42, show = setOf("fuss"), light = setOf("fuss"), card = BoardCard("Etwa fünf Minuten zu Fuß.", "Is it far? No, about five minutes on foot.", phrase = true)),
                    Beat(45, card = BoardCard("Vielen Dank! Gern geschehen.", "Thank you very much! You're welcome.", phrase = true)),
                ),
            ),
            // 46: "So what should I remember for the Goethe exam?"
            Scene(
                46, "For the Goethe exam",
                DiagramSpec(
                    "What the two dialogues mean for the Goethe A1: Hören and Sprechen.",
                    nodes = listOf(
                        DNode("rent", "Hören: the rent", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "plus Nebenkosten"),
                        DNode("floor", "Hören: the floor", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "im zweiten Stock"),
                        DNode("dir", "Hören: directions", 0.26f, 0.56f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "links, rechts, geradeaus"),
                        DNode("spr", "Sprechen", 0.76f, 0.56f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Wie hoch ist die Miete?"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(47, show = setOf("rent", "floor", "dir", "spr"), card = BoardCard("Listen for the plus", "The figure, then plus Nebenkosten: you pay more.")),
                    Beat(49, card = BoardCard("Today", "Phone about a flat out loud: why you call, rooms, rent, a day to see it.")),
                    Beat(50, card = BoardCard("Tschüss, bis bald!", "Bye, see you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de5Practice() = PodcastBoard(
        "de5-practice",
        listOf(
            // 0: "Welcome back. Today is practice: reading flat adverts, and writing abo..."
            Scene(
                0, "The situation first",
                DiagramSpec(
                    "Lesen part 2: read the situation first and underline the conditions before you look at any advert.",
                    nodes = listOf(
                        DNode("s", "Situation", 0.5f, 0.14f, w = 0.5f, h = 0.18f, kind = NodeKind.PILL, sub = "read it first"),
                        DNode("c1", "max 600 Euro", 0.27f, 0.5f, w = 0.42f, h = 0.18f, tone = 1, sub = "condition one"),
                        DNode("c2", "near the station", 0.75f, 0.5f, w = 0.42f, h = 0.18f, tone = 1, sub = "in der Nähe vom Bahnhof"),
                        DNode("p", "Welche Anzeige passt?", 0.5f, 0.84f, w = 0.6f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("s", "c1"),
                        DEdge("s", "c2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("s")),
                    Beat(2, show = setOf("c1", "c2"), light = setOf("c1", "c2"), pulse = "s" to "c1"),
                    Beat(3, show = setOf("p"), light = setOf("p"), card = BoardCard("Welche Anzeige passt?", "Which advert fits? (Sie suchen eine Wohnung für maximal 600 Euro.)", phrase = true)),
                    Beat(5, light = setOf("p"), card = BoardCard("die Anzeige, passt", "The advert; fits or suits.")),
                ),
            ),
            // 6: "Zwei-Zimmer-Wohnung, hell und ruhig, fünfhundertachtzig Euro plus Nebe..."
            Scene(
                6, "Check each advert",
                DiagramSpec(
                    "Check each advert against every condition: A fits both, B fails the price.",
                    nodes = listOf(
                        DNode("c1", "max 600 Euro", 0.27f, 0.12f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("c2", "near the station", 0.75f, 0.12f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("a", "Anzeige A", 0.27f, 0.48f, w = 0.42f, h = 0.2f, sub = "580 Euro, 5 Min."),
                        DNode("b", "Anzeige B", 0.75f, 0.48f, w = 0.42f, h = 0.2f, tone = 2, sub = "950 Euro, Zentrum"),
                        DNode("d", "The decoy fails one", 0.5f, 0.84f, w = 0.6f, kind = NodeKind.NOTE, tone = 2, sub = "check every condition"),
                    ),
                    edges = listOf(
                        DEdge("c1", "a"),
                        DEdge("c2", "a"),
                        DEdge("c1", "b", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("c1", "c2", "a"), light = setOf("a"), card = BoardCard("Zwei-Zimmer-Wohnung, 580 Euro", "Two-room flat, bright and quiet, 580 euros plus bills, five minutes to the station.", phrase = true)),
                    Beat(7, light = setOf("c1", "c2", "a"), pulse = "c1" to "a"),
                    Beat(9, show = setOf("b"), light = setOf("b"), card = BoardCard("Drei-Zimmer-Wohnung im Zentrum", "Three-room flat in the centre, with a balcony, 950 euros plus bills.", phrase = true)),
                    Beat(10, light = setOf("c1", "b"), pulse = "c1" to "b"),
                    Beat(11, show = setOf("d"), light = setOf("a", "d"), card = BoardCard("A passt", "The decoy fits most conditions and fails just one.")),
                ),
            ),
            // 12: "Keine Haustiere."
            Scene(
                12, "The small print",
                DiagramSpec(
                    "Adverts hide small print: pets, and the date the flat is free.",
                    nodes = listOf(
                        DNode("a", "Anzeige A", 0.27f, 0.16f, w = 0.42f, h = 0.18f),
                        DNode("b", "Anzeige B", 0.75f, 0.16f, w = 0.42f, h = 0.18f, tone = 2),
                        DNode("ka", "Keine Haustiere", 0.27f, 0.52f, w = 0.42f, h = 0.18f, tone = 1, sub = "no pets"),
                        DNode("hw", "Haustiere willkommen", 0.75f, 0.52f, w = 0.44f, h = 0.18f, tone = 1, sub = "pets welcome"),
                        DNode("nov", "ab 1. November", 0.75f, 0.84f, w = 0.44f, kind = NodeKind.NOTE, tone = 2, sub = "free from then"),
                    ),
                    edges = listOf(
                        DEdge("a", "ka"),
                        DEdge("b", "hw"),
                        DEdge("hw", "nov", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("a", "ka"), light = setOf("ka"), pulse = "a" to "ka", card = BoardCard("Keine Haustiere.", "No pets.", phrase = true)),
                    Beat(13, light = setOf("ka")),
                    Beat(15, show = setOf("b", "hw", "nov"), light = setOf("hw", "nov"), pulse = "b" to "hw", card = BoardCard("Ab dem ersten November.", "From the first of November. And: Haustiere willkommen, pets welcome.", phrase = true)),
                ),
            ),
            // 16: "So B loves cats but costs a fortune. Okay, now the writing?"
            Scene(
                16, "Schreiben: four points",
                DiagramSpec(
                    "A note about your new flat: where, how many rooms, what you like, and an invitation.",
                    nodes = listOf(
                        DNode("open", "Liebe Sanya,", 0.5f, 0.08f, w = 0.5f, h = 0.12f, kind = NodeKind.PILL, tone = 2),
                        DNode("wo", "Where", 0.27f, 0.3f, w = 0.42f, h = 0.18f, sub = "im Zentrum"),
                        DNode("zi", "Rooms", 0.75f, 0.3f, w = 0.42f, h = 0.18f, sub = "drei Zimmer"),
                        DNode("like", "What you like", 0.27f, 0.62f, w = 0.42f, h = 0.18f, sub = "groß und hell"),
                        DNode("inv", "Invitation", 0.75f, 0.62f, w = 0.42f, h = 0.18f, sub = "Kommst du?"),
                        DNode("close", "Viele Grüße", 0.5f, 0.9f, w = 0.5f, h = 0.12f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("open", "close", "same register", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("open", "wo", "zi", "like", "inv"), light = setOf("open")),
                    Beat(18, light = setOf("wo"), card = BoardCard("Sie ist im Zentrum.", "I have a new flat! It's in the centre, in Gartenstraße.", phrase = true)),
                    Beat(19, light = setOf("wo"), card = BoardCard("im = in dem", "das Zentrum: in dem Zentrum glues into im Zentrum.")),
                    Beat(21, light = setOf("zi"), card = BoardCard("Die Wohnung hat drei Zimmer.", "The flat has three rooms, a kitchen and a bathroom.", phrase = true)),
                    Beat(24, light = setOf("like"), card = BoardCard("Das Wohnzimmer ist groß und hell.", "The living room is big and bright. Es gibt auch einen kleinen Balkon.", phrase = true)),
                    Beat(26, show = setOf("close"), light = setOf("open", "close"), pulse = "open" to "close"),
                    Beat(27, light = setOf("inv", "close"), card = BoardCard("Kommst du am Samstag?", "Are you coming on Saturday? Viele Grüße, Usaid.", phrase = true)),
                    Beat(28, light = setOf("wo"), card = BoardCard("About thirty words", "All four points. The one people forget is where.")),
                ),
            ),
            // 29: "About thirty words, with all four points. The one people forget is whe..."
            Scene(
                29, "Sprechen: a word card",
                DiagramSpec(
                    "Sprechen part 2: the word card becomes a question, and the answer is a full sentence.",
                    nodes = listOf(
                        DNode("t", "Thema: Wohnen", 0.5f, 0.1f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                        DNode("card", "Zimmer", 0.2f, 0.42f, w = 0.32f, h = 0.2f, kind = NodeKind.NOTE, sub = "word card"),
                        DNode("q", "Wie viele Zimmer?", 0.72f, 0.42f, w = 0.48f, h = 0.2f, tone = 1, sub = "your question"),
                        DNode("a", "Meine Wohnung hat", 0.5f, 0.8f, w = 0.6f, h = 0.2f, sub = "a full sentence"),
                    ),
                    edges = listOf(
                        DEdge("t", "card"),
                        DEdge("card", "q"),
                        DEdge("q", "a"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("t", "card"), light = setOf("card"), pulse = "t" to "card"),
                    Beat(30, show = setOf("q"), light = setOf("q"), pulse = "card" to "q", card = BoardCard("Wie viele Zimmer hat deine Wohnung?", "How many rooms does your flat have?", phrase = true)),
                    Beat(33, show = setOf("a"), light = setOf("a"), pulse = "q" to "a", card = BoardCard("Meine Wohnung hat zwei Zimmer.", "My flat has two rooms and a balcony (und einen Balkon).", phrase = true)),
                    Beat(36, light = setOf("a"), card = BoardCard("Ich wohne neben dem Bahnhof.", "I live in the centre (im Zentrum), next to the station.", phrase = true)),
                ),
            ),
            // 37: "So what should I remember for the Goethe exam?"
            Scene(
                37, "For the Goethe exam",
                DiagramSpec(
                    "What practice means for the Goethe A1: Lesen part 2, Schreiben part 2, Sprechen part 2.",
                    nodes = listOf(
                        DNode("lesen", "Lesen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "conditions first"),
                        DNode("schreiben", "Schreiben", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "all four points"),
                        DNode("sprechen", "Sprechen", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "card, question, full answer"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("lesen", "schreiben", "sprechen"), card = BoardCard("The decoy", "It fits all the conditions but one.")),
                    Beat(40, card = BoardCard("Today", "Describe your home room by room; write an advert for it in shorthand.")),
                    Beat(41, card = BoardCard("Viel Spaß und tschüss!", "Have fun, and bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de6Words() = PodcastBoard(
        "de6-words",
        listOf(
            // 0: "Hi, and welcome back. Today is work and school: the words for jobs, th..."
            Scene(
                0, "What's your job?",
                DiagramSpec(
                    "Was sind Sie von Beruf? Answer with ich bin and the job, with no article in between.",
                    nodes = listOf(
                        DNode("q", "Was sind Sie von Beruf?", 0.5f, 0.14f, w = 0.8f, kind = NodeKind.PILL, sub = "what do you do?"),
                        DNode("ich", "Ich bin", 0.2f, 0.5f, w = 0.3f, tone = 1),
                        DNode("job", "Informatiker", 0.7f, 0.5f, w = 0.42f, sub = "no article"),
                        DNode("ein", "ein / der", 0.7f, 0.84f, w = 0.36f, kind = NodeKind.NOTE, tone = 2, sub = "not after sein"),
                    ),
                    edges = listOf(
                        DEdge("q", "job", dashed = true),
                        DEdge("ich", "job"),
                        DEdge("ein", "job", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("q")),
                    Beat(1, light = setOf("q"), card = BoardCard("Was sind Sie von Beruf?", "What do you do for a living?", phrase = true)),
                    Beat(3, light = setOf("q"), card = BoardCard("von Beruf", "By profession. Der Beruf is the job.")),
                    Beat(4, show = setOf("ich", "job"), light = setOf("job"), pulse = "ich" to "job", card = BoardCard("Ich bin Informatiker.", "I'm a software developer.", phrase = true)),
                    Beat(6, show = setOf("ein"), light = setOf("ein"), pulse = "ein" to "job", card = BoardCard("No article after sein", "Ich bin Informatiker, not ein Informatiker.")),
                ),
            ),
            // 7: "Sanya ist Lehrerin."
            Scene(
                7, "Add -in for a woman",
                DiagramSpec(
                    "der Lehrer becomes die Lehrerin; der Arzt becomes die Ärztin, with an umlaut too.",
                    nodes = listOf(
                        DNode("lm", "der Lehrer", 0.25f, 0.2f, w = 0.42f, tone = 1, sub = "the man"),
                        DNode("lf", "die Lehrerin", 0.75f, 0.2f, w = 0.42f, sub = "the woman"),
                        DNode("am", "der Arzt", 0.25f, 0.55f, w = 0.42f, tone = 1, sub = "the man"),
                        DNode("af", "die Ärztin", 0.75f, 0.55f, w = 0.42f, sub = "umlaut and in"),
                        DNode("als", "arbeiten als", 0.5f, 0.86f, w = 0.5f, kind = NodeKind.NOTE, tone = 2, sub = "to work as"),
                    ),
                    edges = listOf(
                        DEdge("lm", "lf", "+ in"),
                        DEdge("am", "af", "+ in"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("lf"), light = setOf("lf"), card = BoardCard("Sanya ist Lehrerin.", "Sanya is a teacher.", phrase = true)),
                    Beat(9, show = setOf("lm"), light = setOf("lf"), pulse = "lm" to "lf", card = BoardCard("The -in ending", "The female form of a job. Leave it off for a woman and people notice.")),
                    Beat(10, show = setOf("am", "af"), light = setOf("af"), pulse = "am" to "af", card = BoardCard("Die Ärztin ist jung.", "The doctor is young. And: Mein Bruder ist Arzt.", phrase = true)),
                    Beat(12, show = setOf("als"), light = setOf("als")),
                    Beat(13, light = setOf("als", "lf"), card = BoardCard("Ich arbeite als Lehrerin.", "I work as a teacher.", phrase = true)),
                ),
            ),
            // 15: "Usaid works at a company, in an office. «die Firma» is the company, «d..."
            Scene(
                15, "At the office",
                DiagramSpec(
                    "Usaid's working day: die Firma, das Büro, der Kunde, die Aufgabe, and der Chef.",
                    nodes = listOf(
                        DNode("firma", "die Firma", 0.25f, 0.14f, w = 0.42f, tone = 1, sub = "the company"),
                        DNode("buero", "das Büro", 0.75f, 0.14f, w = 0.42f, tone = 1, sub = "the office"),
                        DNode("kunde", "der Kunde", 0.25f, 0.46f, w = 0.42f, tone = 1, sub = "the customer"),
                        DNode("aufg", "die Aufgabe", 0.75f, 0.46f, w = 0.42f, tone = 1, sub = "the task"),
                        DNode("chef", "der Chef", 0.3f, 0.8f, w = 0.42f, h = 0.18f, sub = "the boss"),
                        DNode("koch", "der Koch", 0.76f, 0.8f, w = 0.36f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "the cook"),
                    ),
                    edges = listOf(
                        DEdge("chef", "koch", "not", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("firma", "buero"), light = setOf("firma", "buero")),
                    Beat(16, light = setOf("buero"), card = BoardCard("Ich arbeite im Büro.", "I work in the office. And: Die Firma ist in Doha.", phrase = true)),
                    Beat(18, show = setOf("kunde", "aufg"), light = setOf("kunde", "aufg")),
                    Beat(19, light = setOf("kunde"), card = BoardCard("Ich telefoniere mit Kunden.", "I phone customers. And: Ich schreibe viele E-Mails.", phrase = true)),
                    Beat(21, show = setOf("chef", "koch"), light = setOf("chef"), pulse = "chef" to "koch", card = BoardCard("Chef is the boss", "A cook is der Koch.")),
                    Beat(22, light = setOf("chef"), card = BoardCard("Mein Chef heißt Herr Braun.", "My boss is called Mr Braun. Meine Kollegen sind freundlich.", phrase = true)),
                ),
            ),
            // 24: "Sanya has «die Klasse», her class, and «der Unterricht», the lessons. ..."
            Scene(
                24, "Sanya's school",
                DiagramSpec(
                    "Sanya's school day: die Klasse, der Unterricht, die Schüler and die Hausaufgabe.",
                    nodes = listOf(
                        DNode("sanya", "Sanya", 0.5f, 0.14f, w = 0.36f, kind = NodeKind.PILL, sub = "die Lehrerin"),
                        DNode("klasse", "die Klasse", 0.25f, 0.48f, w = 0.42f, tone = 1, sub = "the class"),
                        DNode("unt", "der Unterricht", 0.75f, 0.48f, w = 0.44f, tone = 1, sub = "the lessons"),
                        DNode("sch", "die Schüler", 0.25f, 0.82f, w = 0.42f, tone = 1, sub = "the pupils"),
                        DNode("hausa", "die Hausaufgabe", 0.75f, 0.82f, w = 0.44f, tone = 1, sub = "the homework"),
                    ),
                    edges = listOf(
                        DEdge("sanya", "klasse"),
                        DEdge("sanya", "unt"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("sanya", "klasse", "unt", "sch", "hausa"), light = setOf("klasse", "unt")),
                    Beat(25, light = setOf("unt", "hausa"), card = BoardCard("Der Unterricht beginnt um acht.", "Lessons start at eight. Die Hausaufgaben sind leicht: the homework is easy.", phrase = true)),
                    Beat(28, light = setOf("sanya", "sch"), card = BoardCard("Die Lehrerin erklärt die Grammatik.", "The teacher explains the grammar. Die Schüler fragen viel.", phrase = true)),
                ),
            ),
            // 30: "German has one word for that moment: «der Feierabend», the end of the ..."
            Scene(
                30, "When work stops",
                DiagramSpec(
                    "Feierabend is the end of the working day, not a party; Urlaub is the holiday.",
                    nodes = listOf(
                        DNode("arbeit", "die Arbeit", 0.5f, 0.12f, w = 0.5f, tone = 1, sub = "nine to five"),
                        DNode("feier", "der Feierabend", 0.5f, 0.44f, w = 0.56f, sub = "work stops, go home"),
                        DNode("urlaub", "der Urlaub", 0.5f, 0.76f, w = 0.5f, tone = 1, sub = "the holiday"),
                    ),
                    edges = listOf(
                        DEdge("arbeit", "feier"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("arbeit", "feier"), light = setOf("feier"), pulse = "arbeit" to "feier"),
                    Beat(31, light = setOf("feier"), card = BoardCard("Schönen Feierabend!", "Enjoy your evening! Um fünf ist Feierabend.", phrase = true)),
                    Beat(33, show = setOf("urlaub"), light = setOf("urlaub"), card = BoardCard("müde, fertig", "Tired, finished: two words for the end of a long day.")),
                    Beat(34, light = setOf("urlaub"), card = BoardCard("Im August habe ich Urlaub.", "In August I'm on holiday.", phrase = true)),
                ),
            ),
            // 35: "So what should I remember for the Goethe exam?"
            Scene(
                35, "For the Goethe exam",
                DiagramSpec(
                    "What job words mean for the Goethe A1: the speaking part, the -in form and the reading part.",
                    nodes = listOf(
                        DNode("spr", "Sprechen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Ich bin + job"),
                        DNode("in", "the -in form", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "for a woman"),
                        DNode("les", "Lesen", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "office and school notices"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("spr", "in", "les"), card = BoardCard("No article after sein", "Ich bin Informatiker. Sanya ist Lehrerin.")),
                    Beat(38, card = BoardCard("Today", "Say your job with von Beruf, then your partner's. Use -in once. Name your workplace.")),
                    Beat(39, card = BoardCard("Tschüss! Schönen Feierabend!", "Bye! Enjoy your evening!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de6Grammar() = PodcastBoard(
        "de6-grammar",
        listOf(
            // 0: "Welcome back. Today is grammar, and it's the most useful grammar in A1..."
            Scene(
                0, "Three modal verbs",
                DiagramSpec(
                    "Three verbs for what you can do, must do and would like to do.",
                    nodes = listOf(
                        DNode("k", "können", 0.5f, 0.15f, w = 0.5f, h = 0.18f, sub = "can"),
                        DNode("m", "müssen", 0.5f, 0.48f, w = 0.5f, h = 0.18f, tone = 1, sub = "must, have to"),
                        DNode("mo", "möchten", 0.5f, 0.81f, w = 0.5f, h = 0.18f, tone = 1, sub = "would like"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("k", "m", "mo")),
                    Beat(1, light = setOf("k"), card = BoardCard("Ich kann schwimmen.", "I can swim.", phrase = true)),
                    Beat(3, light = setOf("k", "m", "mo"), card = BoardCard("Modalverb", "A modal brings a second verb, the Infinitiv, to the end.")),
                ),
            ),
            // 4: "Ich muss heute lange arbeiten."
            Scene(
                4, "The verb bracket",
                DiagramSpec(
                    "The modal sits in second place; the infinitive goes last, unchanged. The rest sits in between.",
                    nodes = listOf(
                        DNode("a", "Ich", 0.09f, 0.3f, w = 0.16f, h = 0.18f, tone = 2),
                        DNode("b", "muss", 0.29f, 0.3f, w = 0.22f, h = 0.18f, sub = "second"),
                        DNode("c", "heute lange", 0.56f, 0.3f, w = 0.3f, h = 0.18f, tone = 2, sub = "the middle"),
                        DNode("d", "arbeiten", 0.86f, 0.3f, h = 0.18f, sub = "last"),
                        DNode("a2", "Sanya", 0.11f, 0.72f, w = 0.2f, h = 0.18f, tone = 2),
                        DNode("b2", "muss", 0.32f, 0.72f, w = 0.2f, h = 0.18f),
                        DNode("c2", "um sieben", 0.56f, 0.72f, h = 0.18f, tone = 2),
                        DNode("d2", "aufstehen", 0.86f, 0.72f, h = 0.18f),
                    ),
                    edges = listOf(
                        DEdge("b", "d", "bracket", dashed = true),
                        DEdge("b2", "d2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("a", "b", "c", "d"), light = setOf("b", "d"), card = BoardCard("Ich muss heute lange arbeiten.", "I have to work late today.", phrase = true)),
                    Beat(6, light = setOf("b", "d"), pulse = "b" to "d", card = BoardCard("Satzklammer", "Two bookends: the modal second, the infinitive last.")),
                    Beat(7, light = setOf("c", "d"), card = BoardCard("Not: Ich muss arbeiten heute", "heute goes inside the bracket; arbeiten stays last.")),
                    Beat(9, show = setOf("a2", "b2", "c2", "d2"), light = setOf("b2", "d2"), pulse = "b2" to "d2", card = BoardCard("Sanya muss um sieben aufstehen.", "Sanya has to get up at seven.", phrase = true)),
                ),
            ),
            // 11: "Here's the good news. For «ich», and for «er» or «sie», the form is th..."
            Scene(
                11, "ich kann, du kannst",
                DiagramSpec(
                    "ich and er have the same form with no ending; du adds -st; können and müssen drop the umlaut there.",
                    nodes = listOf(
                        DNode("ich", "ich kann", 0.25f, 0.14f, w = 0.42f, sub = "no ending"),
                        DNode("du", "du kannst", 0.75f, 0.14f, w = 0.42f, tone = 1, sub = "adds st"),
                        DNode("er", "er kann", 0.25f, 0.44f, w = 0.42f, sub = "same as ich"),
                        DNode("wir", "wir können", 0.75f, 0.44f, w = 0.42f, tone = 1, sub = "umlaut back"),
                        DNode("mu", "ich muss, wir müssen", 0.5f, 0.74f, w = 0.8f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("mo", "ich möchte, wir möchten", 0.5f, 0.92f, w = 0.8f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("ich", "er", "same"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("ich", "du", "er", "wir"), light = setOf("ich", "er"), pulse = "ich" to "er"),
                    Beat(12, light = setOf("du", "wir"), card = BoardCard("Ich kann. Du kannst. Wir können.", "I can. You can. We can. And: Er kann.", phrase = true)),
                    Beat(14, show = setOf("mu", "mo"), light = setOf("mu", "mo")),
                ),
            ),
            // 15: "Kannst du mir helfen?"
            Scene(
                15, "Questions and nicht",
                DiagramSpec(
                    "In a question the modal comes first; nicht sits inside the bracket, just before the infinitive.",
                    nodes = listOf(
                        DNode("q1", "Kannst", 0.16f, 0.26f, w = 0.28f, h = 0.18f, sub = "first"),
                        DNode("q2", "du mir", 0.5f, 0.26f, w = 0.3f, h = 0.18f, tone = 2),
                        DNode("q3", "helfen?", 0.84f, 0.26f, w = 0.28f, h = 0.18f, sub = "last"),
                        DNode("n1", "Ich kann", 0.16f, 0.7f, w = 0.28f, h = 0.18f),
                        DNode("n2", "heute nicht", 0.5f, 0.7f, w = 0.32f, h = 0.18f, tone = 1, sub = "inside"),
                        DNode("n3", "kommen", 0.84f, 0.7f, w = 0.28f, h = 0.18f, sub = "last"),
                    ),
                    edges = listOf(
                        DEdge("q1", "q3", dashed = true),
                        DEdge("n1", "n3", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("q1", "q2", "q3"), light = setOf("q1", "q3"), pulse = "q1" to "q3", card = BoardCard("Kannst du mir helfen?", "Can you help me?", phrase = true)),
                    Beat(18, show = setOf("n1", "n2", "n3"), light = setOf("n2"), card = BoardCard("Ich kann heute nicht kommen.", "I can't come today.", phrase = true)),
                ),
            ),
            // 20: "It is. «möchten» is the polite wish, would like. With a second verb, t..."
            Scene(
                20, "Would like, want, may",
                DiagramSpec(
                    "möchten is a polite wish, wollen a strong want, dürfen permission.",
                    nodes = listOf(
                        DNode("mo", "möchten", 0.5f, 0.14f, w = 0.56f, h = 0.18f, sub = "polite wish"),
                        DNode("wo", "wollen", 0.25f, 0.52f, w = 0.42f, h = 0.18f, tone = 1, sub = "strong want"),
                        DNode("du", "dürfen", 0.75f, 0.52f, w = 0.42f, h = 0.18f, tone = 1, sub = "may"),
                        DNode("thing", "Ich möchte einen Kaffee.", 0.5f, 0.86f, w = 0.84f, kind = NodeKind.NOTE, tone = 2, sub = "a thing: no second verb"),
                    ),
                    edges = listOf(
                        DEdge("mo", "thing", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("mo"), light = setOf("mo")),
                    Beat(21, show = setOf("thing"), light = setOf("thing"), pulse = "mo" to "thing", card = BoardCard("Ich möchte einen Kaffee trinken.", "I'd like to drink a coffee. Or just: Ich möchte einen Kaffee.", phrase = true)),
                    Beat(23, show = setOf("wo", "du"), light = setOf("wo", "du")),
                    Beat(24, light = setOf("du"), card = BoardCard("Hier darf man nicht rauchen.", "You may not smoke here. And: Wir wollen nach Deutschland fahren.", phrase = true)),
                ),
            ),
            // 26: "Yes, the «Possessivartikel», the words for my, your, his. They behave ..."
            Scene(
                26, "My, his, your",
                DiagramSpec(
                    "Possessives take the same endings as ein: mein Chef, meine Firma, meinen Chef.",
                    nodes = listOf(
                        DNode("ich", "ich: mein", 0.25f, 0.12f, w = 0.42f, h = 0.14f, tone = 1, sub = "my"),
                        DNode("er", "er: sein", 0.75f, 0.12f, w = 0.42f, h = 0.14f, tone = 1, sub = "his"),
                        DNode("sie", "sie: ihr", 0.25f, 0.4f, w = 0.42f, h = 0.14f, tone = 1, sub = "her"),
                        DNode("wir", "wir: unser", 0.75f, 0.4f, w = 0.42f, h = 0.14f, tone = 1, sub = "our"),
                        DNode("Sie", "Sie: Ihr", 0.5f, 0.66f, w = 0.5f, h = 0.14f, sub = "your, formal"),
                        DNode("acc", "meinen Chef", 0.5f, 0.88f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2, sub = "der Chef after the verb"),
                    ),
                    edges = listOf(
                        DEdge("ich", "acc", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("ich", "acc"), light = setOf("ich"), pulse = "ich" to "acc", card = BoardCard("Like ein", "mein Chef, meine Firma, mein Büro; Ich frage meinen Chef.")),
                    Beat(27, light = setOf("acc"), card = BoardCard("Ich frage meinen Chef.", "I'll ask my boss. And: Mein Bruder ist Arzt.", phrase = true)),
                    Beat(29, show = setOf("er", "sie", "wir", "Sie"), light = setOf("Sie")),
                    Beat(30, light = setOf("er", "Sie"), card = BoardCard("Wie ist Ihre Telefonnummer?", "What is your phone number? And: Sein Büro ist groß.", phrase = true)),
                ),
            ),
            // 31: "So what should I remember for the Goethe exam?"
            Scene(
                31, "For the Goethe exam",
                DiagramSpec(
                    "What modals mean for the Goethe A1: the writing part, the speaking part and the formal your.",
                    nodes = listOf(
                        DNode("sch", "Schreiben", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "modal 2nd, verb last"),
                        DNode("spr", "Sprechen", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Kannst du, Ich möchte"),
                        DNode("les", "Lesen", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "Ihre: the formal your"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("sch", "spr", "les"), card = BoardCard("Check the bracket", "Modal second, infinitive last and unchanged.")),
                    Beat(34, card = BoardCard("Today", "Five sentences with können, five with müssen, three with möchten. Verb last?")),
                    Beat(35, card = BoardCard("Viel Spaß! Bis zum nächsten Mal!", "Have fun! Until next time!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de6Dialogue() = PodcastBoard(
        "de6-dialogue",
        listOf(
            // 0: "Welcome back. Today is two conversations about work. First a party, wh..."
            Scene(
                0, "Two conversations",
                DiagramSpec(
                    "Today: what you do, at a party; and a voicemail when you are ill.",
                    nodes = listOf(
                        DNode("party", "At a party", 0.5f, 0.2f, w = 0.6f, h = 0.2f, tone = 1, sub = "what do you do?"),
                        DNode("vm", "A voicemail", 0.5f, 0.6f, w = 0.6f, h = 0.2f, tone = 1, sub = "I'm ill today"),
                    ),
                    edges = listOf(
                        DEdge("party", "vm", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, light = setOf("party", "vm")),
                ),
            ),
            // 2: "At the party. Lena meets Usaid and Sanya, and she asks the friendly «d..."
            Scene(
                2, "What do you do?",
                DiagramSpec(
                    "Was machst du beruflich? asks what you do: the job first, then the place.",
                    nodes = listOf(
                        DNode("q", "Was machst du beruflich?", 0.5f, 0.12f, w = 0.84f, kind = NodeKind.PILL, sub = "for a living?"),
                        DNode("usaid", "Informatiker", 0.25f, 0.46f, w = 0.44f, h = 0.18f, sub = "Usaid"),
                        DNode("firma", "bei einer Firma", 0.25f, 0.82f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "the place, second"),
                        DNode("sanya", "Lehrerin", 0.75f, 0.46f, w = 0.44f, h = 0.18f, sub = "Sanya: unterrichtet"),
                        DNode("schule", "an einer Schule", 0.75f, 0.82f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "dreißig Schüler"),
                    ),
                    edges = listOf(
                        DEdge("q", "usaid"),
                        DEdge("q", "sanya"),
                        DEdge("usaid", "firma", dashed = true),
                        DEdge("sanya", "schule", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("q"), light = setOf("q")),
                    Beat(3, light = setOf("q"), card = BoardCard("Was machst du beruflich?", "What do you do for a living?", phrase = true)),
                    Beat(5, show = setOf("usaid"), light = setOf("usaid"), pulse = "q" to "usaid", card = BoardCard("What, not where", "Give the job first, then the place.")),
                    Beat(6, show = setOf("firma"), light = setOf("usaid", "firma"), card = BoardCard("Ich bin Informatiker.", "I'm a software developer. Ich arbeite bei einer Firma in Doha.", phrase = true)),
                    Beat(8, show = setOf("sanya"), light = setOf("sanya"), pulse = "q" to "sanya", card = BoardCard("unterrichten", "To teach. The teacher unterrichtet; the pupils lernen.")),
                    Beat(9, show = setOf("schule"), light = setOf("sanya", "schule"), card = BoardCard("Ich unterrichte Englisch.", "I teach English, an einer Schule in Lahore: at a school.", phrase = true)),
                    Beat(12, light = setOf("schule"), card = BoardCard("Meine Klasse hat dreißig Schüler.", "My class has thirty pupils. Not dreizehn, thirteen.", phrase = true)),
                ),
            ),
            // 14: "A modal question: do you have to work a lot? And Sanya answers with «m..."
            Scene(
                14, "Do you work a lot?",
                DiagramSpec(
                    "müssen and können: the modal second, the other verb last.",
                    nodes = listOf(
                        DNode("q1", "Musst du", 0.25f, 0.14f, w = 0.42f),
                        DNode("q2", "viel arbeiten?", 0.75f, 0.14f, w = 0.44f, tone = 1),
                        DNode("m1", "ich muss", 0.18f, 0.5f, w = 0.32f, sub = "second"),
                        DNode("m2", "um sieben", 0.5f, 0.5f, w = 0.28f, tone = 2),
                        DNode("m3", "anfangen", 0.82f, 0.5f, w = 0.32f, sub = "last"),
                        DNode("k1", "ich kann", 0.18f, 0.84f, w = 0.32f, sub = "second"),
                        DNode("k2", "um zwei", 0.5f, 0.84f, w = 0.28f, tone = 2),
                        DNode("k3", "nach Hause gehen", 0.82f, 0.84f, w = 0.34f, sub = "last"),
                    ),
                    edges = listOf(
                        DEdge("m1", "m3", dashed = true),
                        DEdge("k1", "k3", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14),
                    Beat(15, show = setOf("q1", "q2"), light = setOf("q1", "q2"), card = BoardCard("Musst du viel arbeiten?", "Do you have to work a lot?", phrase = true)),
                    Beat(18, show = setOf("m1", "m2", "m3", "k1", "k2", "k3"), light = setOf("m1", "m3", "k1", "k3"), card = BoardCard("Ich muss um sieben anfangen.", "I have to start at seven. Aber ich kann um zwei nach Hause gehen.", phrase = true)),
                ),
            ),
            // 20: "The morning you wake up ill. Usaid calls his manager, Frau Braun, and ..."
            Scene(
                20, "The sick-day voicemail",
                DiagramSpec(
                    "A voicemail has four parts: who you are, what is wrong, what should happen, when you are back.",
                    nodes = listOf(
                        DNode("a", "Hier ist Usaid.", 0.36f, 0.11f, w = 0.6f, tone = 1, sub = "1. who you are"),
                        DNode("b", "Ich bin krank.", 0.36f, 0.35f, w = 0.6f, tone = 1, sub = "2. what is wrong"),
                        DNode("c", "Herr Khan kann ...", 0.36f, 0.59f, w = 0.6f, tone = 1, sub = "3. what should happen"),
                        DNode("d", "Ich bin morgen wieder da.", 0.36f, 0.84f, w = 0.66f, h = 0.2f, sub = "4. when you are back"),
                        DNode("leider", "leider", 0.85f, 0.35f, kind = NodeKind.NOTE, tone = 2, sub = "sorry, short"),
                    ),
                    edges = listOf(
                        DEdge("a", "b"),
                        DEdge("b", "c"),
                        DEdge("c", "d"),
                        DEdge("leider", "b", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("a", "b", "c", "d")),
                    Beat(21, light = setOf("a"), card = BoardCard("Hier ist Usaid Shahab.", "This is Usaid Shahab. Guten Morgen, Frau Braun.", phrase = true)),
                    Beat(23, show = setOf("leider"), light = setOf("b", "leider"), pulse = "leider" to "b"),
                    Beat(24, light = setOf("b"), card = BoardCard("Ich bin krank.", "I'm ill. Ich kann heute leider nicht ins Büro kommen.", phrase = true)),
                    Beat(26, light = setOf("c"), pulse = "b" to "c", card = BoardCard("das Meeting, die Besprechung", "Two words for a meeting at work.")),
                    Beat(27, light = setOf("c"), card = BoardCard("Ich muss zum Arzt gehen.", "I have to go to the doctor. Herr Khan can do the meeting.", phrase = true)),
                    Beat(29, light = setOf("d"), pulse = "c" to "d", card = BoardCard("The part people drop", "When you are back. The exam marks it.")),
                    Beat(30, light = setOf("d"), card = BoardCard("Ich bin morgen wieder da.", "I'll be back tomorrow.", phrase = true)),
                ),
            ),
            // 32: "On the phone it's «Auf Wiederhören». «sehen» is to see, «hören» is to ..."
            Scene(
                32, "Hanging up",
                DiagramSpec(
                    "Auf Wiedersehen face to face; Auf Wiederhören on the phone.",
                    nodes = listOf(
                        DNode("sehen", "Auf Wiedersehen", 0.5f, 0.2f, w = 0.66f, h = 0.2f, tone = 1, sub = "face to face: see"),
                        DNode("hoeren", "Auf Wiederhören", 0.5f, 0.6f, w = 0.66f, h = 0.2f, sub = "on the phone: hear"),
                    ),
                    edges = listOf(
                        DEdge("sehen", "hoeren", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, light = setOf("hoeren"), pulse = "sehen" to "hoeren"),
                    Beat(33, light = setOf("hoeren"), card = BoardCard("Vielen Dank und auf Wiederhören.", "Many thanks, and goodbye. Sie können mich auf dem Handy anrufen.", phrase = true)),
                ),
            ),
            // 35: "An announcement at the language school. It starts with «Achtung», atte..."
            Scene(
                35, "Achtung, eine Durchsage",
                DiagramSpec(
                    "In an announcement, listen for the day, the time and the room.",
                    nodes = listOf(
                        DNode("di", "Dienstag", 0.5f, 0.12f, w = 0.5f, tone = 2, sub = "fällt aus"),
                        DNode("do", "Donnerstag", 0.18f, 0.56f, w = 0.32f, h = 0.2f, sub = "the day"),
                        DNode("uhr", "achtzehn Uhr", 0.52f, 0.56f, w = 0.32f, h = 0.2f, sub = "the time"),
                        DNode("raum", "Raum zwölf", 0.84f, 0.56f, w = 0.3f, h = 0.2f, sub = "the room"),
                    ),
                    edges = listOf(
                        DEdge("di", "uhr", "wieder", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(35, show = setOf("do", "uhr", "raum"), light = setOf("do", "uhr", "raum")),
                    Beat(36, show = setOf("di"), light = setOf("di"), card = BoardCard("Der Deutschkurs fällt aus.", "The German course is cancelled. Achtung, eine Durchsage: attention.", phrase = true)),
                    Beat(38, light = setOf("do", "uhr", "raum"), pulse = "di" to "uhr", card = BoardCard("Am Donnerstag um achtzehn Uhr", "On Thursday at six in the evening, in Raum zwölf: room twelve.", phrase = true)),
                ),
            ),
            // 39: "So what should I remember for the Goethe exam?"
            Scene(
                39, "For the Goethe exam",
                DiagramSpec(
                    "What these conversations mean for the Goethe A1: speaking, listening and announcements.",
                    nodes = listOf(
                        DNode("spr", "Sprechen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "job first, then place"),
                        DNode("hoe", "Hören", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "four parts: when back?"),
                        DNode("ann", "Durchsage", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "day, time, room"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(40, show = setOf("spr", "hoe", "ann"), card = BoardCard("Listen for all four", "Who, what is wrong, what should happen, when back.")),
                    Beat(42, card = BoardCard("Today", "Leave the sick-day voicemail out loud. Check all four parts are in it.")),
                    Beat(43, card = BoardCard("Vielen Dank und auf Wiederhören!", "Many thanks, and goodbye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de6Practice() = PodcastBoard(
        "de6-practice",
        listOf(
            // 0: "Welcome back. Today is practice: three exam tasks from the world of wo..."
            Scene(
                0, "Three exam tasks",
                DiagramSpec(
                    "Today: a notice to read, an email to write, and your introduction to say.",
                    nodes = listOf(
                        DNode("les", "Lesen", 0.5f, 0.14f, w = 0.56f, h = 0.18f, tone = 1, sub = "der Aushang"),
                        DNode("sch", "Schreiben", 0.5f, 0.48f, w = 0.56f, h = 0.18f, tone = 1, sub = "die E-Mail"),
                        DNode("spr", "Sprechen", 0.5f, 0.82f, w = 0.56f, h = 0.18f, tone = 1, sub = "part one"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, light = setOf("les", "sch", "spr")),
                ),
            ),
            // 2: "Exactly. In German it's «der Aushang», a notice on a board. In the rea..."
            Scene(
                2, "Der Aushang",
                DiagramSpec(
                    "Read a notice for the small words: ab is from, bis is by, wieder is again.",
                    nodes = listOf(
                        DNode("fr", "am Freitag", 0.5f, 0.1f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("ab", "ab dreizehn Uhr", 0.25f, 0.38f, w = 0.46f, h = 0.18f, sub = "from one, not all day"),
                        DNode("bis", "bis zwölf Uhr", 0.75f, 0.38f, w = 0.44f, h = 0.18f, tone = 1, sub = "by twelve"),
                        DNode("wie", "am Montag wieder", 0.25f, 0.72f, w = 0.46f, h = 0.18f, tone = 1, sub = "again at eight"),
                        DNode("braun", "Frau Braun", 0.75f, 0.72f, w = 0.44f, h = 0.18f, tone = 2, sub = "call with questions"),
                    ),
                    edges = listOf(
                        DEdge("fr", "ab"),
                        DEdge("fr", "bis"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("fr"), light = setOf("fr"), card = BoardCard("der Aushang", "A notice on a board. Decide richtig or falsch.")),
                    Beat(3, show = setOf("ab"), light = setOf("ab"), pulse = "fr" to "ab", card = BoardCard("Ab dreizehn Uhr geschlossen.", "Closed from one o'clock: am Freitag ist das Büro ab dreizehn Uhr geschlossen.", phrase = true)),
                    Beat(5, light = setOf("ab"), card = BoardCard("Not all day", "ab dreizehn Uhr: from one o'clock. Falsch!")),
                    Beat(6, show = setOf("bis"), light = setOf("bis"), pulse = "fr" to "bis", card = BoardCard("E-Mails bis zwölf Uhr beantworten.", "Please answer emails by twelve o'clock.", phrase = true)),
                    Beat(9, show = setOf("wie", "braun"), light = setOf("wie", "braun"), card = BoardCard("Am Montag beginnt die Arbeit wieder.", "On Monday work starts again, um acht Uhr. Wer Fragen hat: call Frau Braun.", phrase = true)),
                ),
            ),
            // 11: "And the numbers. Now writing. Usaid is ill and can't come in tomorrow,..."
            Scene(
                11, "An email to the boss",
                DiagramSpec(
                    "A formal email: opening, the problem, the reason, when you are back, a question, the sign-off.",
                    nodes = listOf(
                        DNode("a", "Sehr geehrte Frau Braun,", 0.38f, 0.08f, w = 0.72f, h = 0.13f),
                        DNode("b", "... leider nicht kommen.", 0.38f, 0.24f, w = 0.72f, h = 0.13f, tone = 1),
                        DNode("c", "Ich bin krank ...", 0.38f, 0.4f, w = 0.72f, h = 0.13f, tone = 1),
                        DNode("d", "... bin ich wieder da.", 0.38f, 0.56f, w = 0.72f, h = 0.13f, tone = 1),
                        DNode("e", "Können wir ...?", 0.38f, 0.72f, w = 0.72f, h = 0.13f, tone = 1),
                        DNode("f", "Mit freundlichen Grüßen", 0.38f, 0.9f, w = 0.72f, h = 0.15f),
                        DNode("m", "geehrter", 0.87f, 0.24f, w = 0.24f, kind = NodeKind.NOTE, tone = 2, sub = "for a man"),
                    ),
                    edges = listOf(
                        DEdge("a", "f", "match", dashed = true),
                        DEdge("m", "a", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("a"), light = setOf("a")),
                    Beat(12, light = setOf("a"), card = BoardCard("Sehr geehrte Frau Braun,", "Dear Ms Braun: geehrte for a woman.", phrase = true)),
                    Beat(14, show = setOf("m"), light = setOf("m"), pulse = "m" to "a", card = BoardCard("Sehr geehrter Herr Braun", "A man takes geehrter: one letter, the first word the marker reads.")),
                    Beat(15, show = setOf("b"), light = setOf("b"), card = BoardCard("Ich kann morgen leider nicht kommen.", "Unfortunately I can't come tomorrow: ins Büro, to the office.", phrase = true)),
                    Beat(18, show = setOf("c"), light = setOf("c"), card = BoardCard("Ich muss zum Arzt gehen.", "I have to go to the doctor. Ich bin krank: I am ill.", phrase = true)),
                    Beat(21, show = setOf("d", "e"), light = setOf("d", "e"), card = BoardCard("Am Donnerstag bin ich wieder da.", "I'll be back on Thursday. Then a question about the meeting.", phrase = true)),
                    Beat(23, show = setOf("f"), light = setOf("a", "f"), pulse = "a" to "f", card = BoardCard("Match the halves", "Sehr geehrte at the top, Mit freundlichen Grüßen at the bottom.")),
                    Beat(24, light = setOf("f"), card = BoardCard("Mit freundlichen Grüßen", "Yours sincerely: the formal sign-off.", phrase = true)),
                ),
            ),
            // 26: "Part one is your introduction. You get a card with seven words: «Name»..."
            Scene(
                26, "Your introduction",
                DiagramSpec(
                    "Sprechen part one: seven words on the card, one sentence for each.",
                    nodes = listOf(
                        DNode("name", "der Name", 0.25f, 0.1f, w = 0.42f, h = 0.14f, tone = 1),
                        DNode("alter", "das Alter", 0.75f, 0.1f, w = 0.42f, h = 0.14f, tone = 1),
                        DNode("land", "das Land", 0.25f, 0.32f, w = 0.42f, h = 0.14f, tone = 1),
                        DNode("wohn", "der Wohnort", 0.75f, 0.32f, w = 0.42f, h = 0.14f, tone = 1),
                        DNode("spr", "die Sprachen", 0.25f, 0.54f, w = 0.42f, h = 0.14f, tone = 1),
                        DNode("beruf", "der Beruf", 0.75f, 0.54f, w = 0.42f, h = 0.14f, tone = 1),
                        DNode("hobby", "das Hobby", 0.5f, 0.76f, w = 0.42f, h = 0.14f, tone = 1),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("name", "alter", "land", "wohn", "spr", "beruf", "hobby")),
                    Beat(27, light = setOf("name", "alter", "land"), card = BoardCard("Ich heiße Usaid.", "My name is Usaid. Ich bin dreißig Jahre alt und komme aus Pakistan.", phrase = true)),
                    Beat(30, light = setOf("wohn", "beruf"), card = BoardCard("Ich arbeite als Informatiker.", "I work as a software developer. Ich wohne in Doha: I live in Doha.", phrase = true)),
                    Beat(33, light = setOf("spr", "hobby"), card = BoardCard("Mein Hobby ist Lesen.", "My hobby is reading. Ich spreche Urdu, Englisch und ein bisschen Deutsch.", phrase = true)),
                ),
            ),
            // 35: "Then spell it letter by letter, with the German alphabet. If they ask ..."
            Scene(
                35, "Spell it, say it",
                DiagramSpec(
                    "Spell your name, give your number in pairs, and say where you work.",
                    nodes = listOf(
                        DNode("abc", "Spell your name", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "German alphabet"),
                        DNode("num", "Phone number", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "pairs of digits"),
                        DNode("arzt", "die Ärztin", 0.25f, 0.52f, w = 0.42f, tone = 1),
                        DNode("praxis", "die Praxis", 0.75f, 0.52f, w = 0.42f, sub = "the surgery"),
                        DNode("lehr", "die Lehrerin", 0.25f, 0.82f, w = 0.42f, tone = 1),
                        DNode("schule", "die Schule", 0.75f, 0.82f, w = 0.42f, sub = "the school"),
                    ),
                    edges = listOf(
                        DEdge("arzt", "praxis"),
                        DEdge("lehr", "schule"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(35, show = setOf("abc", "num", "arzt", "praxis", "lehr", "schule"), light = setOf("abc", "num")),
                    Beat(36, light = setOf("praxis", "schule"), pulse = "arzt" to "praxis", card = BoardCard("Die Ärztin arbeitet in der Praxis.", "The doctor works at the surgery. Die Lehrerin: in der Schule.", phrase = true)),
                ),
            ),
            // 37: "So what should I remember for the Goethe exam?"
            Scene(
                37, "For the Goethe exam",
                DiagramSpec(
                    "What today's practice means for the Goethe A1: reading, writing and speaking.",
                    nodes = listOf(
                        DNode("les", "Lesen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "ab, bis, wieder"),
                        DNode("sch", "Schreiben", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "match the halves"),
                        DNode("spr", "Sprechen", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "seven sentences ready"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("les", "sch", "spr"), card = BoardCard("Say when you're back", "It is a content point of its own in the email.")),
                    Beat(40, card = BoardCard("Today", "Record your introduction. Spell your surname. Number in pairs. Three job sentences.")),
                    Beat(41, card = BoardCard("Tschüss, bis bald!", "Bye, see you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de7Words() = PodcastBoard(
        "de7-words",
        listOf(
            // 0: "Welcome back. Today is travel words: trains, tickets, the airport, and..."
            Scene(
                0, "Getting around",
                DiagramSpec(
                    "Learn each word with its article. After mit, der and das become dem, and die becomes der.",
                    nodes = listOf(
                        DNode("hder", "der", 0.17f, 0.1f, h = 0.12f, kind = NodeKind.PILL),
                        DNode("hdie", "die", 0.5f, 0.1f, h = 0.12f, kind = NodeKind.PILL),
                        DNode("hdas", "das", 0.83f, 0.1f, h = 0.12f, kind = NodeKind.PILL),
                        DNode("zug", "der Zug", 0.17f, 0.36f, w = 0.3f, h = 0.18f, tone = 1, sub = "train"),
                        DNode("bus", "der Bus", 0.17f, 0.6f, w = 0.3f, h = 0.18f, tone = 1, sub = "bus"),
                        DNode("ubahn", "die U-Bahn", 0.5f, 0.36f, w = 0.3f, h = 0.18f, tone = 1, sub = "underground"),
                        DNode("strab", "die Straßenbahn", 0.5f, 0.6f, w = 0.3f, h = 0.18f, tone = 1, sub = "tram"),
                        DNode("auto", "das Auto", 0.83f, 0.36f, w = 0.3f, h = 0.18f, tone = 1, sub = "car"),
                        DNode("rad", "das Fahrrad", 0.83f, 0.6f, w = 0.3f, h = 0.18f, tone = 1, sub = "bicycle"),
                        DNode("mit", "mit dem Zug, mit der U-Bahn", 0.5f, 0.87f, w = 0.84f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("hder", "zug"),
                        DEdge("hdie", "ubahn"),
                        DEdge("hdas", "auto"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("hder", "hdie", "hdas")),
                    Beat(1, show = setOf("zug"), light = setOf("zug"), card = BoardCard("Der Zug nach Berlin fährt um neun.", "The train to Berlin leaves at nine.", phrase = true)),
                    Beat(3, show = setOf("bus", "ubahn", "strab"), light = setOf("ubahn", "strab"), pulse = "hdie" to "ubahn", card = BoardCard("A name tag on every word", "der Zug, der Bus, die U-Bahn, die Straßenbahn.")),
                    Beat(4, light = setOf("ubahn"), card = BoardCard("Ich nehme die U-Bahn.", "I take the underground.", phrase = true)),
                    Beat(6, show = setOf("auto", "rad", "mit"), light = setOf("auto", "rad", "mit"), pulse = "hdas" to "auto"),
                    Beat(7, light = setOf("rad", "mit"), card = BoardCard("Ich fahre mit dem Fahrrad.", "I go by bike.", phrase = true)),
                ),
            ),
            // 8: "What about flying? Sanya's family is in Doha. Do I still say «fahren»?"
            Scene(
                8, "Ground or air",
                DiagramSpec(
                    "You fahren in anything on the ground and fliegen in a plane.",
                    nodes = listOf(
                        DNode("you", "You", 0.5f, 0.14f, w = 0.2f, h = 0.18f, kind = NodeKind.USER),
                        DNode("fahren", "fahren", 0.25f, 0.5f, w = 0.4f, h = 0.2f, tone = 1, sub = "train, bus, car"),
                        DNode("fliegen", "fliegen", 0.75f, 0.5f, w = 0.4f, h = 0.2f, sub = "a plane"),
                        DNode("fz", "das Flugzeug", 0.75f, 0.84f, w = 0.4f, kind = NodeKind.NOTE, tone = 2, sub = "the plane"),
                        DNode("fh", "der Flughafen", 0.25f, 0.84f, w = 0.4f, kind = NodeKind.NOTE, tone = 2, sub = "the airport"),
                    ),
                    edges = listOf(
                        DEdge("you", "fahren"),
                        DEdge("you", "fliegen"),
                        DEdge("fliegen", "fz", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("you")),
                    Beat(9, show = setOf("fahren", "fliegen", "fz", "fh"), light = setOf("fliegen"), pulse = "you" to "fliegen", card = BoardCard("fahren or fliegen?", "On the ground: fahren. In a plane: fliegen.")),
                    Beat(10, light = setOf("fliegen", "fh"), card = BoardCard("Ich fliege nach Doha.", "I fly to Doha. And: Der Flughafen ist weit, the airport is far.", phrase = true)),
                ),
            ),
            // 11: "Okay. Now I'm at the station, and I need a ticket."
            Scene(
                11, "At the ticket counter",
                DiagramSpec(
                    "At der Schalter, einfach is a single, one way, and hin und zurück is a return.",
                    nodes = listOf(
                        DNode("schalter", "der Schalter", 0.5f, 0.12f, w = 0.5f, sub = "ticket counter"),
                        DNode("q", "Einfach oder hin und zurück?", 0.5f, 0.4f, w = 0.84f, kind = NodeKind.NOTE, tone = 1),
                        DNode("einfach", "einfach", 0.25f, 0.68f, w = 0.42f, h = 0.18f, tone = 1, sub = "single, not easy"),
                        DNode("hin", "hin und zurück", 0.75f, 0.68f, w = 0.42f, h = 0.18f, tone = 1, sub = "there and back"),
                        DNode("fk", "die Fahrkarte", 0.5f, 0.91f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("schalter", "q"),
                        DEdge("q", "einfach"),
                        DEdge("q", "hin"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("schalter", "fk"), light = setOf("schalter")),
                    Beat(13, show = setOf("q"), light = setOf("q"), pulse = "schalter" to "q", card = BoardCard("Einfach oder hin und zurück?", "Single or return?", phrase = true)),
                    Beat(15, show = setOf("einfach", "hin"), light = setOf("einfach"), card = BoardCard("einfach is not easy here", "At the counter it means one way, a single.")),
                    Beat(16, light = setOf("hin", "fk"), pulse = "q" to "hin", card = BoardCard("Eine Fahrkarte nach München, bitte.", "A ticket to Munich, please. One return.", phrase = true)),
                ),
            ),
            // 17: "Great. And then where's my train?"
            Scene(
                17, "Reading the board",
                DiagramSpec(
                    "On der Fahrplan, Abfahrt and Ankunft sit side by side: ab is away, an is at.",
                    nodes = listOf(
                        DNode("plan", "der Fahrplan", 0.5f, 0.12f, w = 0.5f, sub = "the timetable"),
                        DNode("ab", "die Abfahrt", 0.25f, 0.44f, w = 0.42f, h = 0.2f, tone = 1, sub = "departure: ab, away"),
                        DNode("an", "die Ankunft", 0.75f, 0.44f, w = 0.42f, h = 0.2f, tone = 1, sub = "arrival: an, at"),
                        DNode("gleis", "das Gleis", 0.25f, 0.8f, w = 0.42f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "the track number"),
                        DNode("steig", "der Bahnsteig", 0.75f, 0.8f, w = 0.42f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "where you wait"),
                    ),
                    edges = listOf(
                        DEdge("plan", "ab"),
                        DEdge("plan", "an"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("plan", "ab", "an", "gleis", "steig"), light = setOf("ab", "an"), pulse = "plan" to "an", card = BoardCard("The front piece is the whole word", "Abfahrt is departure, Ankunft is arrival.")),
                    Beat(19, light = setOf("gleis"), card = BoardCard("Der Zug fährt von Gleis drei.", "The train leaves from platform three.", phrase = true)),
                ),
            ),
            // 20: "I keep hearing «fahren» inside other words."
            Scene(
                20, "Travel verbs",
                DiagramSpec(
                    "In a sentence the front piece jumps to the end: fährt in second place, ab last.",
                    nodes = listOf(
                        DNode("verbs", "abfahren, ankommen, umsteigen", 0.5f, 0.1f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("s1", "Der Zug", 0.14f, 0.4f, w = 0.24f, tone = 1),
                        DNode("s2", "fährt", 0.39f, 0.4f, w = 0.22f),
                        DNode("s3", "um acht", 0.63f, 0.4f, w = 0.22f, tone = 2),
                        DNode("s4", "ab.", 0.87f, 0.4f, w = 0.2f),
                        DNode("delay", "die Verspätung", 0.27f, 0.8f, w = 0.46f, h = 0.2f, tone = 1, sub = "the delay, added on"),
                        DNode("puenkt", "pünktlich", 0.77f, 0.8f, w = 0.4f, h = 0.2f, tone = 1, sub = "on time"),
                    ),
                    edges = listOf(
                        DEdge("s2", "s4", "one verb", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("verbs"), light = setOf("verbs"), card = BoardCard("On, off, swap", "einsteigen, aussteigen, umsteigen.")),
                    Beat(22, show = setOf("s1", "s2", "s3", "s4"), light = setOf("s2", "s4"), pulse = "s2" to "s4", card = BoardCard("Der Zug fährt um acht ab.", "The train departs at eight. And: Wir kommen um zwölf an.", phrase = true)),
                    Beat(24, show = setOf("delay", "puenkt"), light = setOf("delay")),
                    Beat(25, light = setOf("delay"), card = BoardCard("Zwanzig Minuten Verspätung.", "The train is twenty minutes late.", phrase = true)),
                ),
            ),
            // 26: "Now times. The announcement says «siebzehn Uhr». Is that seven?"
            Scene(
                26, "The clock",
                DiagramSpec(
                    "Announcements use the 24-hour clock. halb goes to the next hour: halb zehn is 9:30.",
                    nodes = listOf(
                        DNode("sieb", "siebzehn Uhr", 0.27f, 0.14f, w = 0.46f, h = 0.2f, sub = "5 pm, announcements"),
                        DNode("vz", "vierzehn Uhr dreißig", 0.75f, 0.14f, w = 0.44f, h = 0.2f, tone = 1, sub = "2:30 pm"),
                        DNode("a", "9:30", 0.14f, 0.5f, w = 0.24f, h = 0.2f, tone = 1),
                        DNode("b", "halb zehn", 0.47f, 0.5f, w = 0.34f, h = 0.2f, sub = "half way to ten"),
                        DNode("c", "not halb neun", 0.82f, 0.5f, w = 0.32f, h = 0.2f, tone = 2, sub = "that is 8:30"),
                        DNode("vn", "Viertel nach neun", 0.27f, 0.84f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "9:15"),
                        DNode("vv", "Viertel vor zehn", 0.75f, 0.84f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "9:45"),
                    ),
                    edges = listOf(
                        DEdge("a", "b"),
                        DEdge("b", "c", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("sieb"), light = setOf("sieb"), card = BoardCard("No a.m. or p.m.", "The hour, then Uhr, then the minutes.")),
                    Beat(28, show = setOf("vz"), light = setOf("vz"), card = BoardCard("Vierzehn Uhr dreißig.", "Two thirty in the afternoon. Achtzehn Uhr fünfzig: 6:50 pm.", phrase = true)),
                    Beat(30, show = setOf("a", "b", "c"), light = setOf("b"), pulse = "a" to "b", card = BoardCard("halb goes to the next hour", "halb zehn is 9:30, not 10:30.")),
                    Beat(31, show = setOf("vn", "vv"), light = setOf("vn", "vv"), card = BoardCard("Es ist halb zehn.", "It's half past nine. Viertel nach neun, Viertel vor zehn.", phrase = true)),
                ),
            ),
            // 32: "So what should I remember for the Goethe exam?"
            Scene(
                32, "For the Goethe exam",
                DiagramSpec(
                    "What travel words mean for the Goethe A1: listening, reading, and the ticket counter.",
                    nodes = listOf(
                        DNode("hear", "Hören", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "24-hour; halb goes on"),
                        DNode("read", "Lesen", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Abfahrt or Ankunft?"),
                        DNode("einf", "einfach", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "a single, not easy"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("hear", "read", "einf"), card = BoardCard("Read the front piece", "ab is away, an is at. halb zehn is 9:30.")),
                    Beat(35, card = BoardCard("Today", "Read one real journey: Abfahrt, Ankunft, umsteigen? Then ask for the ticket aloud.")),
                    Beat(36, card = BoardCard("Gute Reise! Bis bald!", "Have a good trip! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de7Grammar() = PodcastBoard(
        "de7-grammar",
        listOf(
            // 0: "Welcome back. Today is the grammar of travel: verbs that split in two,..."
            Scene(
                0, "Verbs that split",
                DiagramSpec(
                    "A trennbares Verb: the stem fährt takes second place and the prefix ab waits at the end.",
                    nodes = listOf(
                        DNode("tv", "trennbares Verb", 0.3f, 0.14f, w = 0.52f, h = 0.18f, kind = NodeKind.PILL, sub = "separable verb"),
                        DNode("pre", "Präfix: ab", 0.78f, 0.14f, w = 0.38f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "the front piece"),
                        DNode("s1", "Der Zug", 0.13f, 0.52f, w = 0.24f, h = 0.18f, tone = 1, sub = "1"),
                        DNode("s2", "fährt", 0.38f, 0.52f, w = 0.22f, h = 0.18f, sub = "2"),
                        DNode("s3", "um neun Uhr", 0.63f, 0.52f, w = 0.24f, h = 0.18f, tone = 2, sub = "middle"),
                        DNode("s4", "ab.", 0.87f, 0.52f, w = 0.2f, h = 0.18f, sub = "end"),
                        DNode("inf", "abfahren", 0.5f, 0.86f, w = 0.5f, kind = NodeKind.NOTE, tone = 2, sub = "to depart"),
                    ),
                    edges = listOf(
                        DEdge("s2", "s4", "one verb", dashed = true),
                        DEdge("inf", "s2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("tv")),
                    Beat(2, show = setOf("pre", "inf", "s1", "s2", "s3", "s4"), light = setOf("s2", "s4"), pulse = "s2" to "s4", card = BoardCard("Engine and last carriage", "The verb in second place, the prefix at the very end.")),
                    Beat(3, light = setOf("s2", "s4"), card = BoardCard("Der Zug fährt um neun Uhr ab.", "The train departs at nine.", phrase = true)),
                ),
            ),
            // 4: "So «fährt» is second, and «ab» is the very last word? Even with other ..."
            Scene(
                4, "Prefix at the end",
                DiagramSpec(
                    "Every separable verb works the same way: the prefix waits at the end of the sentence.",
                    nodes = listOf(
                        DNode("i1", "ankommen", 0.2f, 0.14f, w = 0.36f, h = 0.18f, tone = 1, sub = "to arrive"),
                        DNode("e1", "Wir kommen um zwölf Uhr an.", 0.7f, 0.14f, w = 0.56f, h = 0.18f),
                        DNode("i2", "umsteigen", 0.2f, 0.39f, w = 0.36f, h = 0.18f, tone = 1, sub = "to change trains"),
                        DNode("e2", "Ich steige in Köln um.", 0.7f, 0.39f, w = 0.56f, h = 0.18f),
                        DNode("i3", "aufstehen", 0.2f, 0.64f, w = 0.36f, h = 0.18f, tone = 1, sub = "to get up"),
                        DNode("e3", "Ich stehe um sechs auf.", 0.7f, 0.64f, w = 0.56f, h = 0.18f),
                        DNode("pre", "ab, an, auf, aus, ein, mit, um, zurück", 0.5f, 0.89f, w = 0.96f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("i1", "e1"),
                        DEdge("i2", "e2"),
                        DEdge("i3", "e3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("i1"), light = setOf("i1")),
                    Beat(6, show = setOf("e1"), light = setOf("e1"), pulse = "i1" to "e1", card = BoardCard("Wir kommen um zwölf Uhr an.", "We arrive at twelve.", phrase = true)),
                    Beat(8, show = setOf("i2"), light = setOf("i2")),
                    Beat(9, show = setOf("e2"), light = setOf("e2"), pulse = "i2" to "e2", card = BoardCard("Ich steige in Köln um.", "I change trains in Cologne.", phrase = true)),
                    Beat(11, show = setOf("i3", "pre"), light = setOf("pre"), card = BoardCard("Everyday split verbs", "aufstehen, einkaufen, mitkommen, fernsehen.")),
                    Beat(12, show = setOf("e3"), light = setOf("e3"), pulse = "i3" to "e3", card = BoardCard("Ich stehe um sechs auf.", "I get up at six. And: Kommst du mit? Are you coming along?", phrase = true)),
                ),
            ),
            // 13: "Does the verb ever stay in one piece?"
            Scene(
                13, "With a helper verb",
                DiagramSpec(
                    "After müssen or möchten the verb is whole again, at the end. The stress is on the prefix.",
                    nodes = listOf(
                        DNode("m1", "Ich", 0.12f, 0.16f, w = 0.2f, h = 0.18f, tone = 1),
                        DNode("m2", "muss", 0.34f, 0.16f, w = 0.2f, h = 0.18f, sub = "2"),
                        DNode("m3", "in Köln", 0.57f, 0.16f, w = 0.22f, h = 0.18f, tone = 2),
                        DNode("m4", "umsteigen.", 0.84f, 0.16f, w = 0.3f, h = 0.18f, sub = "whole"),
                        DNode("stress", "ANkommen", 0.27f, 0.56f, w = 0.46f, h = 0.2f, tone = 1, sub = "stress the prefix"),
                        DNode("stay", "be-, er-, ver-", 0.77f, 0.56f, w = 0.42f, h = 0.2f, tone = 2, sub = "stay put: ich verstehe"),
                        DNode("q", "Wann kommst du an?", 0.5f, 0.87f, w = 0.7f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("m2", "m4", "helper, verb", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("m1", "m2", "m3", "m4"), light = setOf("m2", "m4"), pulse = "m2" to "m4", card = BoardCard("Coupled back together", "The helper in second place, the whole verb at the end.")),
                    Beat(15, light = setOf("m4"), card = BoardCard("Ich muss in Köln umsteigen.", "I have to change in Cologne.", phrase = true)),
                    Beat(17, show = setOf("stress", "stay"), light = setOf("stress")),
                    Beat(18, show = setOf("q"), light = setOf("q"), card = BoardCard("Wann kommst du an?", "When are you arriving?", phrase = true)),
                ),
            ),
            // 19: "Okay. Now the where-to words. How do I say I'm going to Berlin?"
            Scene(
                19, "Wohin? nach or zu",
                DiagramSpec(
                    "Wohin? nach for cities and countries with no article; zu for people and places with one.",
                    nodes = listOf(
                        DNode("wohin", "Wohin?", 0.5f, 0.12f, w = 0.34f, kind = NodeKind.PILL, sub = "where to?"),
                        DNode("nach", "nach", 0.25f, 0.44f, w = 0.42f, h = 0.2f, tone = 1, sub = "cities, countries"),
                        DNode("zu", "zu", 0.75f, 0.44f, w = 0.42f, h = 0.2f, tone = 1, sub = "people, with an article"),
                        DNode("n1", "nach Berlin, nach Hause", 0.25f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                        DNode("z1", "zum Arzt, zur Schule", 0.75f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("wohin", "nach"),
                        DEdge("wohin", "zu"),
                        DEdge("nach", "n1", dashed = true),
                        DEdge("zu", "z1", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("wohin", "nach", "n1"), light = setOf("nach"), pulse = "wohin" to "nach"),
                    Beat(21, light = setOf("n1"), card = BoardCard("Wir fahren nach Berlin.", "We're going to Berlin. And: Ich gehe nach Hause, I'm going home.", phrase = true)),
                    Beat(23, show = setOf("zu", "z1"), light = setOf("zu"), pulse = "wohin" to "zu", card = BoardCard("zum = zu dem, zur = zu der", "der Arzt: zum Arzt. die Schule: zur Schule.")),
                    Beat(24, light = setOf("z1"), card = BoardCard("Ich gehe zum Arzt.", "I'm going to the doctor. Sanya geht zur Schule.", phrase = true)),
                ),
            ),
            // 25: "And going into a building? Like the cinema?"
            Scene(
                25, "in and mit",
                DiagramSpec(
                    "in das shrinks to ins. mit always takes the dative: der and das become dem, die becomes der.",
                    nodes = listOf(
                        DNode("ins", "ins Kino", 0.5f, 0.1f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("mit", "mit", 0.5f, 0.36f, w = 0.3f, h = 0.18f, sub = "by, how you travel"),
                        DNode("dz", "der Zug", 0.17f, 0.66f, w = 0.3f, tone = 2, sub = "mit dem Zug"),
                        DNode("dt", "das Taxi", 0.5f, 0.66f, w = 0.3f, tone = 2, sub = "mit dem Taxi"),
                        DNode("du", "die U-Bahn", 0.83f, 0.66f, w = 0.3f, tone = 1, sub = "mit der U-Bahn"),
                        DNode("never", "never mit den Zug", 0.5f, 0.9f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("mit", "dz"),
                        DEdge("mit", "dt"),
                        DEdge("mit", "du"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("ins", "mit", "dz", "dt", "du"), light = setOf("mit"), card = BoardCard("mit reshapes the article", "der and das become dem; die becomes der.")),
                    Beat(27, light = setOf("dz", "du"), pulse = "mit" to "dz", card = BoardCard("Sanya fährt mit dem Zug.", "Sanya goes by train. And: Ich fahre mit der U-Bahn.", phrase = true)),
                    Beat(28, light = setOf("du"), pulse = "mit" to "du"),
                    Beat(29, show = setOf("never"), light = setOf("never")),
                    Beat(30, light = setOf("mit", "dz"), card = BoardCard("Mit dem Zug nach Hamburg.", "We go to Hamburg by train and change in Hanover: steigen ... um.", phrase = true)),
                ),
            ),
            // 31: "So what should I remember for the Goethe exam?"
            Scene(
                31, "For the Goethe exam",
                DiagramSpec(
                    "Split verbs and Wohin? in the Goethe A1: listening, reading and writing.",
                    nodes = listOf(
                        DNode("hear", "Hören", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "wait for the prefix"),
                        DNode("read", "Lesen", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "nach marks the town"),
                        DNode("write", "Schreiben", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "mit dem Zug, never mit den"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("hear", "read", "write"), card = BoardCard("Wait for the end", "fährt ... ab is departs; kommt ... an is arrives.")),
                    Beat(34, card = BoardCard("Today", "Five sentences with abfahren, ankommen, umsteigen; again with müssen.")),
                    Beat(35, card = BoardCard("Tschüss! Bis bald!", "Bye! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de7Dialogue() = PodcastBoard(
        "de7-dialogue",
        listOf(
            // 0: "Welcome back. Today is a dialogue at the station: Usaid buys a ticket ..."
            Scene(
                0, "At the ticket counter",
                DiagramSpec(
                    "Usaid buys a ticket to Hamburg from a stranger, so it's Guten Tag and Sie, and möchte.",
                    nodes = listOf(
                        DNode("usaid", "Usaid", 0.18f, 0.24f, w = 0.24f, h = 0.22f, kind = NodeKind.USER),
                        DNode("seller", "Seller", 0.82f, 0.24f, w = 0.24f, h = 0.22f, kind = NodeKind.USER, tone = 1),
                        DNode("schalter", "der Schalter", 0.5f, 0.24f, w = 0.36f, h = 0.18f, tone = 2, sub = "the counter"),
                        DNode("sie", "Guten Tag, Sie", 0.5f, 0.58f, w = 0.5f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "a stranger"),
                        DNode("hh", "nach Hamburg", 0.5f, 0.86f, w = 0.5f, kind = NodeKind.NOTE, tone = 2, sub = "for the weekend"),
                    ),
                    edges = listOf(
                        DEdge("usaid", "schalter"),
                        DEdge("schalter", "seller"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("usaid", "schalter", "seller")),
                    Beat(2, show = setOf("sie", "hh"), light = setOf("sie"), pulse = "usaid" to "schalter"),
                    Beat(3, light = setOf("hh"), card = BoardCard("Eine Fahrkarte nach Hamburg, bitte.", "Good afternoon. I would like a ticket to Hamburg, please.", phrase = true)),
                ),
            ),
            // 4: "And the ticket seller asks something back?"
            Scene(
                4, "Single or return",
                DiagramSpec(
                    "einfach is one way; hin und zurück is there and back. Usaid comes back on Sunday.",
                    nodes = listOf(
                        DNode("q", "Einfach oder hin und zurück?", 0.5f, 0.14f, w = 0.86f, kind = NodeKind.NOTE, tone = 1),
                        DNode("einfach", "einfach", 0.25f, 0.48f, w = 0.42f, h = 0.2f, tone = 2, sub = "one way"),
                        DNode("hin", "hin und zurück", 0.75f, 0.48f, w = 0.42f, h = 0.2f, sub = "return"),
                        DNode("sun", "am Sonntag zurück", 0.75f, 0.84f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "back on Sunday"),
                    ),
                    edges = listOf(
                        DEdge("q", "einfach"),
                        DEdge("q", "hin"),
                        DEdge("hin", "sun", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("q"), light = setOf("q"), card = BoardCard("Einfach oder hin und zurück?", "Single or return?", phrase = true)),
                    Beat(7, show = setOf("einfach", "hin"), light = setOf("einfach")),
                    Beat(8, show = setOf("sun"), light = setOf("hin"), pulse = "q" to "hin"),
                    Beat(9, light = setOf("hin", "sun"), card = BoardCard("Hin und zurück.", "Return. I travel today and come back on Sunday.", phrase = true)),
                ),
            ),
            // 10: "Then she tells him the price and the train?"
            Scene(
                10, "Price, time, platform",
                DiagramSpec(
                    "Three details in one breath: the price, the time and the platform. Repeat the platform back.",
                    nodes = listOf(
                        DNode("price", "89 Euro", 0.5f, 0.14f, w = 0.44f, h = 0.18f, tone = 2, sub = "neunundachtzig"),
                        DNode("time", "14:15", 0.5f, 0.46f, w = 0.44f, h = 0.18f, tone = 1, sub = "vierzehn Uhr fünfzehn"),
                        DNode("gleis", "Gleis 7", 0.5f, 0.78f, w = 0.44f, h = 0.18f, sub = "say it back"),
                    ),
                    edges = listOf(
                        DEdge("price", "time"),
                        DEdge("time", "gleis"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("price", "time", "gleis"), light = setOf("time", "gleis")),
                    Beat(12, light = setOf("price", "time", "gleis"), card = BoardCard("Gleis sieben, vierzehn Uhr fünfzehn", "The next train leaves at 14:15 from platform 7. That's 89 euros.", phrase = true)),
                    Beat(14, light = setOf("gleis"), pulse = "time" to "gleis", card = BoardCard("A quarter past two", "Repeat the platform number back: Gleis sieben.")),
                ),
            ),
            // 15: "Muss ich umsteigen?"
            Scene(
                15, "The two follow-ups",
                DiagramSpec(
                    "Two questions decide the journey: do I change, and when do I arrive? On, off and swap.",
                    nodes = listOf(
                        DNode("start", "Abfahrt 14:15", 0.2f, 0.16f, w = 0.36f, h = 0.2f, tone = 1, sub = "Gleis 7"),
                        DNode("hann", "Hannover", 0.5f, 0.44f, w = 0.36f, h = 0.2f, sub = "umsteigen, 10 min"),
                        DNode("hh", "Hamburg", 0.8f, 0.72f, w = 0.36f, h = 0.2f, tone = 1, sub = "an 18:40"),
                        DNode("verbs", "ein: on, aus: off, um: swap", 0.32f, 0.9f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("start", "hann"),
                        DEdge("hann", "hh"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("start"), card = BoardCard("Muss ich umsteigen?", "Do I have to change trains?", phrase = true)),
                    Beat(17, show = setOf("hann", "verbs"), light = setOf("hann", "verbs"), pulse = "start" to "hann"),
                    Beat(18, light = setOf("hann"), card = BoardCard("Ja, in Hannover.", "Yes, in Hanover. You have ten minutes there.", phrase = true)),
                    Beat(21, show = setOf("hh"), light = setOf("hh"), pulse = "hann" to "hh", card = BoardCard("Wann komme ich in Hamburg an?", "And when do I arrive in Hamburg?", phrase = true)),
                    Beat(24, light = setOf("hh"), card = BoardCard("Um achtzehn Uhr vierzig. Gute Reise!", "At 18:40. Have a good trip!", phrase = true)),
                ),
            ),
            // 25: "Now the platform. The speakers crackle, and I panic."
            Scene(
                25, "On the platform",
                DiagramSpec(
                    "Read the statement first, decide which detail it tests, and listen only for that. A delay is added on.",
                    nodes = listOf(
                        DNode("read", "Read the statement", 0.27f, 0.14f, w = 0.46f, h = 0.18f, tone = 1, sub = "before the audio"),
                        DNode("detail", "Which detail?", 0.27f, 0.44f, w = 0.46f, h = 0.18f, sub = "time, platform, place"),
                        DNode("listen", "Listen for it", 0.27f, 0.74f, w = 0.46f, h = 0.18f, tone = 2, sub = "heard once only"),
                        DNode("plan", "Abfahrt 10:32", 0.77f, 0.14f, w = 0.42f, h = 0.18f, tone = 2, sub = "on the board"),
                        DNode("delay", "+ 20 Minuten", 0.77f, 0.44f, w = 0.42f, h = 0.18f, tone = 1, sub = "Verspätung"),
                        DNode("real", "really 10:52", 0.77f, 0.74f, w = 0.42f, h = 0.18f, sub = "not pünktlich"),
                    ),
                    edges = listOf(
                        DEdge("read", "detail"),
                        DEdge("detail", "listen"),
                        DEdge("plan", "delay"),
                        DEdge("delay", "real"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("read", "detail", "listen"), light = setOf("detail"), pulse = "read" to "detail"),
                    Beat(27, show = setOf("plan"), light = setOf("plan"), card = BoardCard("Der ICE nach München hat Verspätung.", "Platform 3: the ICE to Munich is twenty minutes late today.", phrase = true)),
                    Beat(29, show = setOf("delay", "real"), light = setOf("real"), pulse = "delay" to "real", card = BoardCard("The delay is added on", "10:32 plus twenty minutes is 10:52. pünktlich? Falsch.")),
                    Beat(30, light = setOf("delay"), card = BoardCard("Der Zug hat Verspätung.", "The train is late.", phrase = true)),
                ),
            ),
            // 31: "What's the second announcement?"
            Scene(
                31, "Listen for nicht",
                DiagramSpec(
                    "When an announcement corrects itself, the number before nicht is the real one.",
                    nodes = listOf(
                        DNode("rz", "der Regionalzug", 0.5f, 0.12f, w = 0.56f, kind = NodeKind.PILL, tone = 1, sub = "regional train, Bonn"),
                        DNode("g5", "Gleis fünf", 0.25f, 0.42f, w = 0.42f, h = 0.18f, sub = "the real one"),
                        DNode("g2", "nicht Gleis zwei", 0.75f, 0.42f, w = 0.42f, h = 0.18f, tone = 2, sub = "crossed out"),
                        DNode("fg", "Liebe Fahrgäste", 0.5f, 0.66f, w = 0.56f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("schl", "schließen", 0.25f, 0.88f, w = 0.42f, kind = NodeKind.NOTE, tone = 2, sub = "to close"),
                        DNode("gleich", "gleich", 0.75f, 0.88f, w = 0.42f, kind = NodeKind.NOTE, tone = 2, sub = "in a moment"),
                    ),
                    edges = listOf(
                        DEdge("rz", "g5"),
                        DEdge("rz", "g2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("rz", "g5", "g2"), light = setOf("rz")),
                    Beat(33, light = setOf("g5"), pulse = "rz" to "g5", card = BoardCard("Heute von Gleis fünf.", "The regional train to Bonn leaves from platform 5 today, not 2.", phrase = true)),
                    Beat(35, show = setOf("fg", "schl", "gleich"), light = setOf("fg", "schl", "gleich")),
                    Beat(36, light = setOf("gleich"), card = BoardCard("Der Zug fährt gleich ab.", "Please board and close the doors. The train is about to depart.", phrase = true)),
                ),
            ),
            // 37: "So what should I remember for the Goethe exam?"
            Scene(
                37, "For the Goethe exam",
                DiagramSpec(
                    "The station in the Goethe A1: announcements in Hören part 2, and a question for Sprechen.",
                    nodes = listOf(
                        DNode("hear", "Hören part 2", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "once only: read first"),
                        DNode("catch", "Catch", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Gleis, time, nicht"),
                        DNode("speak", "Sprechen", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "Muss ich umsteigen?"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("hear", "catch", "speak"), card = BoardCard("One detail per statement", "Platform, time, Verspätung, and the word nicht.")),
                    Beat(40, card = BoardCard("Today", "Buy a ticket aloud: where to, single or return, umsteigen?, arrival, platform back.")),
                    Beat(41, card = BoardCard("Gute Reise! Tschüss!", "Have a good trip! Bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de7Practice() = PodcastBoard(
        "de7-practice",
        listOf(
            // 0: "Welcome back. Today is practice: we read a timetable, write a short me..."
            Scene(
                0, "Reading a timetable",
                DiagramSpec(
                    "Each row is a Verbindung: departure, arrival, changes, price. Compare the rows against the situation.",
                    nodes = listOf(
                        DNode("head", "Köln to Berlin, Samstag", 0.5f, 0.08f, w = 0.9f, h = 0.12f, kind = NodeKind.PILL),
                        DNode("a1", "Ab 8:15", 0.12f, 0.32f, w = 0.2f, h = 0.18f, tone = 1),
                        DNode("b1", "An 12:40", 0.36f, 0.32f, w = 0.2f, h = 0.18f, tone = 1),
                        DNode("c1", "Hannover", 0.62f, 0.32f, h = 0.18f, tone = 2, sub = "umsteigen"),
                        DNode("d1", "59 €", 0.88f, 0.32f, w = 0.18f, h = 0.18f, tone = 2),
                        DNode("a2", "Ab 9:00", 0.12f, 0.57f, w = 0.2f, h = 0.18f, tone = 1),
                        DNode("b2", "An 12:10", 0.36f, 0.57f, w = 0.2f, h = 0.18f, tone = 1),
                        DNode("c2", "direkt", 0.62f, 0.57f, h = 0.18f, sub = "no change"),
                        DNode("d2", "89 €", 0.88f, 0.57f, w = 0.18f, h = 0.18f, tone = 2),
                        DNode("a3", "Ab 10:30", 0.12f, 0.82f, w = 0.2f, h = 0.18f, tone = 1),
                        DNode("b3", "An 15:05", 0.36f, 0.82f, w = 0.2f, h = 0.18f, tone = 1),
                        DNode("c3", "Hamburg", 0.62f, 0.82f, h = 0.18f, tone = 2, sub = "umsteigen"),
                        DNode("d3", "39 €", 0.88f, 0.82f, w = 0.18f, h = 0.18f),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("head")),
                    Beat(2, show = setOf("a1", "b1", "c1", "d1", "a2", "b2", "c2", "d2", "a3", "b3", "c3", "d3"), light = setOf("a2", "b2"), card = BoardCard("Verbindung", "A connection: Abfahrt, Ankunft, changes, price.")),
                    Beat(3, light = setOf("a2", "b2", "c2"), card = BoardCard("Verbindung zwei: direkt.", "Connection two: departure 9:00, arrival 12:10, direct.", phrase = true)),
                    Beat(5, light = setOf("c1", "c2", "c3")),
                    Beat(6, light = setOf("c2"), card = BoardCard("Welche Verbindung? Verbindung zwei.", "You don't want to change. Which connection? Connection two.", phrase = true)),
                    Beat(8, light = setOf("d3", "c3"), card = BoardCard("Not on price alone", "Read the situation first, then check each row against it.")),
                    Beat(9, light = setOf("d3"), card = BoardCard("Neununddreißig Euro.", "The ticket costs 39 euros.", phrase = true)),
                ),
            ),
            // 10: "Now the writing. What do I write?"
            Scene(
                10, "Where, when and how",
                DiagramSpec(
                    "The verb wants second place: when Am Freitag comes first, fahre comes next and ich moves behind it.",
                    nodes = listOf(
                        DNode("p1", "Where and when", 0.27f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 1, sub = "am for a day, nach for a town"),
                        DNode("p2", "How", 0.75f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 1, sub = "mit dem Zug"),
                        DNode("w1", "Am Freitag", 0.15f, 0.52f, w = 0.28f, h = 0.18f, tone = 2, sub = "1"),
                        DNode("w2", "fahre", 0.4f, 0.52f, w = 0.2f, h = 0.18f, sub = "2"),
                        DNode("w3", "ich", 0.6f, 0.52f, w = 0.18f, h = 0.18f, tone = 1),
                        DNode("w4", "nach Hamburg.", 0.84f, 0.52f, w = 0.3f, h = 0.18f, tone = 1),
                        DNode("an", "komme um zwölf Uhr an", 0.5f, 0.86f, w = 0.7f, kind = NodeKind.NOTE, tone = 2, sub = "the split verb"),
                    ),
                    edges = listOf(
                        DEdge("w1", "w2"),
                        DEdge("p1", "p2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("p1"), light = setOf("p1"), card = BoardCard("Four points", "Where, when and how, where you sleep, a request.")),
                    Beat(12, show = setOf("w1", "w2", "w3", "w4"), light = setOf("w4"), card = BoardCard("Am Freitag fahre ich nach Hamburg.", "On Friday I'm going to Hamburg.", phrase = true)),
                    Beat(14, show = setOf("p2"), light = setOf("w2"), pulse = "w1" to "w2", card = BoardCard("Verb in second place", "Am Freitag first, then the verb fahre, then ich.")),
                    Beat(15, show = setOf("an"), light = setOf("p2", "an"), pulse = "p1" to "p2", card = BoardCard("Ich fahre mit dem Zug um acht Uhr.", "I'm going by train at eight, and arrive at twelve.", phrase = true)),
                ),
            ),
            // 16: "Then where he sleeps."
            Scene(
                16, "Sleep and a request",
                DiagramSpec(
                    "The last two points: where you sleep, and a request as a question. Hallo Sanya goes with Viele Grüße.",
                    nodes = listOf(
                        DNode("hallo", "Hallo Sanya,", 0.5f, 0.1f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("p3", "Where you sleep", 0.27f, 0.38f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, tone = 1, sub = "im Hotel, übernachten"),
                        DNode("p4", "A request", 0.75f, 0.38f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 1, sub = "Kannst du ...?"),
                        DNode("abholen", "abholen", 0.75f, 0.66f, w = 0.44f, h = 0.18f, sub = "to pick up, whole at the end"),
                        DNode("gruss", "Viele Grüße", 0.5f, 0.9f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("p4", "abholen"),
                        DEdge("p3", "p4"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("p3"), light = setOf("p3")),
                    Beat(18, light = setOf("p3"), card = BoardCard("Ich schlafe im Hotel am Bahnhof.", "I'm sleeping at the hotel by the station.", phrase = true)),
                    Beat(20, show = setOf("p4", "abholen"), light = setOf("p4", "abholen"), pulse = "p4" to "abholen"),
                    Beat(21, light = setOf("abholen"), card = BoardCard("Kannst du mich am Bahnhof abholen?", "Can you pick me up at the station?", phrase = true)),
                    Beat(23, show = setOf("hallo", "gruss"), light = setOf("hallo", "gruss"), card = BoardCard("One register", "A friend: Hallo Sanya at the top, Viele Grüße at the bottom.")),
                ),
            ),
            // 24: "Fahren wir am Samstag mit dem Zug nach Berlin?"
            Scene(
                24, "Planning together",
                DiagramSpec(
                    "Sprechen part 3: a picture card, a request to your partner, and an answer: Ja, gern or Nein, leider nicht.",
                    nodes = listOf(
                        DNode("t", "a ticket", 0.17f, 0.12f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                        DNode("c", "a clock", 0.5f, 0.12f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                        DNode("b", "a bicycle", 0.83f, 0.12f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 2),
                        DNode("req", "Kannst du ...?", 0.5f, 0.42f, w = 0.5f, h = 0.2f, sub = "the request"),
                        DNode("yes", "Ja, gern.", 0.25f, 0.78f, w = 0.42f, h = 0.2f, tone = 1, sub = "plus a question back"),
                        DNode("no", "Nein, leider nicht.", 0.75f, 0.78f, w = 0.42f, h = 0.2f, tone = 1, sub = "plus a reason"),
                    ),
                    edges = listOf(
                        DEdge("req", "yes"),
                        DEdge("req", "no"),
                        DEdge("c", "req", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("t", "c", "b", "req"), light = setOf("req"), card = BoardCard("Am Samstag mit dem Zug nach Berlin?", "Shall we go to Berlin by train on Saturday?", phrase = true)),
                    Beat(26, show = setOf("yes"), light = setOf("yes"), pulse = "req" to "yes"),
                    Beat(27, light = setOf("yes"), card = BoardCard("Ja, gern. Wann kommst du an?", "Yes, gladly. When do you arrive?", phrase = true)),
                    Beat(29, show = setOf("no"), light = setOf("no"), pulse = "req" to "no", card = BoardCard("Not a bare order", "Hol mich ab! with no bitte sounds rude. Ask with Kannst du.")),
                    Beat(30, light = setOf("no"), card = BoardCard("Nein, leider nicht.", "No, unfortunately not. Ich muss am Samstag arbeiten: I have to work.", phrase = true)),
                ),
            ),
            // 31: "Story of my life. And times? My partner says «halb sieben»."
            Scene(
                31, "Times from your day",
                DiagramSpec(
                    "halb goes to the next hour: halb sieben is 6:30. nach is past, vor is to.",
                    nodes = listOf(
                        DNode("h", "halb sieben", 0.5f, 0.16f, w = 0.6f, h = 0.2f, sub = "6:30, half way to seven"),
                        DNode("vn", "Viertel nach drei", 0.5f, 0.48f, w = 0.6f, h = 0.2f, tone = 1, sub = "3:15"),
                        DNode("zv", "zehn vor neun", 0.5f, 0.8f, w = 0.6f, h = 0.2f, tone = 1, sub = "8:50"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("h")),
                    Beat(32, show = setOf("vn", "zv"), light = setOf("h"), card = BoardCard("halb goes to the next hour", "halb sieben is 6:30, not 7:30.")),
                    Beat(33, light = setOf("vn", "zv"), card = BoardCard("Halb sieben. Viertel nach drei.", "Half past six. Quarter past three. And: zehn vor neun, ten to nine.", phrase = true)),
                ),
            ),
            // 34: "So what should I remember for the Goethe exam?"
            Scene(
                34, "For the Goethe exam",
                DiagramSpec(
                    "A trip in the Goethe A1: a timetable to read, a message to write, requests to make.",
                    nodes = listOf(
                        DNode("read", "Lesen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "rows against the situation"),
                        DNode("write", "Schreiben", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "all four points"),
                        DNode("speak", "Sprechen", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "Kannst du ...? Ja, gern."),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(35, show = setOf("read", "write", "speak"), card = BoardCard("Not just the price", "Check every row against the situation; cover all four points.")),
                    Beat(37, card = BoardCard("Today", "Plan a real weekend trip: a timetable aloud, a four-point message, three requests.")),
                    Beat(38, card = BoardCard("Tschüss und gute Reise!", "Bye, and have a good trip!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de8Words() = PodcastBoard(
        "de8-words",
        listOf(
            // 0: "Hi, and welcome back. Today is the body, feeling ill and the doctor. B..."
            Scene(
                0, "What hurts?",
                DiagramSpec(
                    "Mein Kopf tut weh: the body part is the subject, and tut weh says it hurts.",
                    nodes = listOf(
                        DNode("you", "You", 0.5f, 0.18f, w = 0.22f, h = 0.2f, kind = NodeKind.USER),
                        DNode("kopf", "der Kopf", 0.27f, 0.62f, w = 0.4f, h = 0.2f, tone = 1, sub = "head"),
                        DNode("weh", "tut weh", 0.75f, 0.62f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "hurts"),
                    ),
                    edges = listOf(
                        DEdge("kopf", "weh"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("you")),
                    Beat(1, show = setOf("kopf", "weh"), light = setOf("kopf", "weh"), pulse = "kopf" to "weh", card = BoardCard("Mein Kopf tut weh.", "My head hurts.", phrase = true)),
                ),
            ),
            // 3: "It works like English: my head hurts. «der Kopf» is the head, and «tut..."
            Scene(
                3, "Body parts and articles",
                DiagramSpec(
                    "Learn every body part with its article: most are der, a few are das or die.",
                    nodes = listOf(
                        DNode("der", "der", 0.18f, 0.1f, w = 0.3f, h = 0.12f, kind = NodeKind.PILL),
                        DNode("das", "das", 0.5f, 0.1f, w = 0.28f, h = 0.12f, kind = NodeKind.PILL),
                        DNode("die", "die", 0.82f, 0.1f, w = 0.3f, h = 0.12f, kind = NodeKind.PILL),
                        DNode("d1", "der Kopf", 0.18f, 0.3f, w = 0.32f, h = 0.14f, tone = 1, sub = "head"),
                        DNode("d2", "der Mund", 0.18f, 0.46f, w = 0.32f, h = 0.14f, tone = 1, sub = "mouth"),
                        DNode("d3", "der Hals", 0.18f, 0.62f, w = 0.32f, h = 0.14f, tone = 1, sub = "throat"),
                        DNode("d4", "der Bauch", 0.18f, 0.78f, w = 0.32f, h = 0.14f, tone = 1, sub = "stomach"),
                        DNode("d5", "der Rücken", 0.18f, 0.93f, w = 0.32f, h = 0.14f, tone = 1, sub = "back"),
                        DNode("s1", "das Auge", 0.5f, 0.3f, w = 0.3f, h = 0.14f, tone = 1, sub = "eye"),
                        DNode("s2", "das Ohr", 0.5f, 0.46f, w = 0.3f, h = 0.14f, tone = 1, sub = "ear"),
                        DNode("s3", "das Bein", 0.5f, 0.62f, w = 0.3f, h = 0.14f, tone = 1, sub = "leg"),
                        DNode("s4", "das Herz", 0.5f, 0.78f, w = 0.3f, h = 0.14f, tone = 1, sub = "heart"),
                        DNode("e1", "die Nase", 0.82f, 0.3f, w = 0.3f, h = 0.14f, tone = 1, sub = "nose"),
                        DNode("e2", "die Hand", 0.82f, 0.46f, w = 0.3f, h = 0.14f, tone = 1, sub = "hand"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("der", "das", "die")),
                    Beat(4, show = setOf("d1", "s1", "s2", "e1", "d2"), light = setOf("d1", "s1", "s2", "e1", "d2"), card = BoardCard("der Kopf, das Auge, das Ohr", "the head, the eye, the ear (and: die Nase, der Mund)", phrase = true)),
                    Beat(6, show = setOf("d3", "d4", "d5", "s3", "s4", "e2"), light = setOf("der", "das"), card = BoardCard("Learn the article too", "Most of the body is der. Watch das Auge, das Ohr, das Bein, das Herz.")),
                    Beat(7, light = setOf("d3", "d4", "d5", "s3"), card = BoardCard("der Hals, der Bauch, der Rücken", "the throat, the stomach, the back (and: das Bein, der Fuß)", phrase = true)),
                ),
            ),
            // 8: "Okay. And the second way to say something hurts?"
            Scene(
                8, "Body part + Schmerzen",
                DiagramSpec(
                    "Glue the body part in front of Schmerzen, and say you have it: haben, never sein.",
                    nodes = listOf(
                        DNode("k", "der Kopf", 0.22f, 0.16f, w = 0.38f, h = 0.18f, tone = 1, sub = "head"),
                        DNode("s", "die Schmerzen", 0.75f, 0.16f, w = 0.44f, h = 0.18f, tone = 1, sub = "pain"),
                        DNode("ks", "Kopfschmerzen", 0.5f, 0.5f, w = 0.6f, h = 0.2f, sub = "headache"),
                        DNode("h", "Ich habe …", 0.5f, 0.84f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, sub = "haben, never sein"),
                    ),
                    edges = listOf(
                        DEdge("k", "ks"),
                        DEdge("s", "ks"),
                        DEdge("ks", "h", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8),
                    Beat(9, show = setOf("k", "s", "ks"), light = setOf("ks"), pulse = "k" to "ks"),
                    Beat(10, show = setOf("h"), light = setOf("ks", "h"), card = BoardCard("Ich habe Kopfschmerzen.", "I have a headache.", phrase = true)),
                    Beat(12, light = setOf("h"), pulse = "ks" to "h", card = BoardCard("A noun takes haben", "Bauchschmerzen, Zahnschmerzen: the body part comes first.")),
                    Beat(13, card = BoardCard("Ich habe Zahnschmerzen.", "I have toothache. Mein Bauch tut auch weh: my stomach hurts too.", phrase = true)),
                ),
            ),
            // 14: "Poor Usaid. And if he has a cold?"
            Scene(
                14, "Ill, and since when",
                DiagramSpec(
                    "Name the illness with haben; say how long with seit and the present tense.",
                    nodes = listOf(
                        DNode("e", "die Erkältung", 0.27f, 0.12f, w = 0.44f, tone = 1, sub = "cold"),
                        DNode("f", "das Fieber", 0.75f, 0.12f, w = 0.4f, tone = 1, sub = "fever"),
                        DNode("h", "der Husten", 0.27f, 0.34f, w = 0.44f, tone = 1, sub = "cough"),
                        DNode("g", "die Grippe", 0.75f, 0.34f, w = 0.4f, tone = 1, sub = "flu"),
                        DNode("s", "seit zwei Tagen", 0.27f, 0.62f, w = 0.44f, h = 0.18f, sub = "since then"),
                        DNode("p", "Ich habe …", 0.75f, 0.62f, w = 0.4f, h = 0.18f, sub = "present tense"),
                        DNode("m", "Mir geht es schlecht", 0.5f, 0.9f, w = 0.7f, kind = NodeKind.NOTE, tone = 2, sub = "in general, then the symptom"),
                    ),
                    edges = listOf(
                        DEdge("s", "p"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14),
                    Beat(15, show = setOf("e", "f", "h", "g")),
                    Beat(16, light = setOf("e", "f"), card = BoardCard("Ich habe eine Erkältung und Fieber.", "I have a cold and a fever.", phrase = true)),
                    Beat(18, show = setOf("s", "p"), light = setOf("s", "p"), pulse = "s" to "p"),
                    Beat(19, light = setOf("s"), card = BoardCard("Ich habe seit zwei Tagen Fieber.", "I have had a fever for two days.", phrase = true)),
                    Beat(21, show = setOf("m"), light = setOf("m"), card = BoardCard("Not just the verdict", "Say what hurts too: the pharmacy needs the symptom.")),
                    Beat(22, light = setOf("m"), card = BoardCard("Mir geht es schlecht.", "I feel unwell. Ich habe Halsschmerzen: I have a sore throat.", phrase = true)),
                ),
            ),
            // 23: "Now the admin. Where do I actually go?"
            Scene(
                23, "The admin of being ill",
                DiagramSpec(
                    "The Rezept goes to the Apotheke for medicine; the Krankmeldung goes to work or school.",
                    nodes = listOf(
                        DNode("pr", "die Praxis", 0.27f, 0.1f, w = 0.44f, tone = 1, sub = "Sprechstunde"),
                        DNode("za", "der Zahnarzt", 0.75f, 0.1f, w = 0.42f, tone = 1, sub = "dentist"),
                        DNode("rz", "das Rezept", 0.27f, 0.38f, w = 0.44f, sub = "prescription"),
                        DNode("km", "die Krankmeldung", 0.75f, 0.38f, w = 0.46f, sub = "sick note"),
                        DNode("ap", "die Apotheke", 0.27f, 0.66f, w = 0.44f, tone = 1, sub = "pharmacy"),
                        DNode("ar", "die Arbeit", 0.75f, 0.66f, w = 0.42f, tone = 1, sub = "work or school"),
                        DNode("gb", "Gute Besserung!", 0.5f, 0.9f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2, sub = "get well soon"),
                    ),
                    edges = listOf(
                        DEdge("pr", "rz"),
                        DEdge("rz", "ap"),
                        DEdge("pr", "km"),
                        DEdge("km", "ar"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23),
                    Beat(24, show = setOf("pr", "za"), light = setOf("pr", "za")),
                    Beat(26, show = setOf("rz", "km", "ap", "ar"), light = setOf("rz", "km"), pulse = "rz" to "ap"),
                    Beat(27, light = setOf("km", "ar"), pulse = "km" to "ar", card = BoardCard("Ich brauche eine Krankmeldung.", "I need a sick note (für die Arbeit: for work).", phrase = true)),
                    Beat(30, light = setOf("rz", "ap"), pulse = "rz" to "ap", card = BoardCard("Hier ist das Rezept.", "Here is the prescription. Zwei Tabletten am Tag: two a day.", phrase = true)),
                    Beat(33, show = setOf("gb"), light = setOf("gb"), card = BoardCard("Gute Besserung, Sanya!", "Get well soon, Sanya!", phrase = true)),
                ),
            ),
            // 34: "So what should I remember for the Goethe exam?"
            Scene(
                34, "For the Goethe exam",
                DiagramSpec(
                    "What the body words mean for the Goethe A1: listening, speaking, and the two papers.",
                    nodes = listOf(
                        DNode("hear", "Listening", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "the body part before schmerzen"),
                        DNode("speak", "Speaking", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "say it both ways"),
                        DNode("paper", "Rezept or Krankmeldung", 0.5f, 0.56f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "pharmacy or work"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(35, show = setOf("hear", "speak", "paper"), card = BoardCard("Kopf, Bauch or Zahn?", "The body part in front of schmerzen is the whole answer.")),
                    Beat(37, card = BoardCard("Today", "Three body parts, said both ways. One symptom with seit. Rezept or Krankmeldung?")),
                    Beat(38, card = BoardCard("Bleib gesund! Tschüss!", "Stay healthy! Bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de8Grammar() = PodcastBoard(
        "de8-grammar",
        listOf(
            // 0: "Welcome back. Today is the «Imperativ», the command form: how a doctor..."
            Scene(
                0, "Verb first, then Sie",
                DiagramSpec(
                    "The Sie command is the yes or no question with a full stop: verb first, Sie second.",
                    nodes = listOf(
                        DNode("v", "1 verb", 0.27f, 0.12f, w = 0.36f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("s", "2 Sie", 0.73f, 0.12f, w = 0.36f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("q", "Trinken Sie viel Wasser?", 0.5f, 0.44f, w = 0.8f, h = 0.2f, tone = 2, sub = "a question"),
                        DNode("c", "Trinken Sie viel Wasser!", 0.5f, 0.8f, w = 0.8f, h = 0.2f, sub = "an instruction"),
                    ),
                    edges = listOf(
                        DEdge("q", "c", "full stop"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("v", "s")),
                    Beat(1, show = setOf("c"), light = setOf("c"), card = BoardCard("Trinken Sie viel Wasser!", "Drink a lot of water!", phrase = true)),
                    Beat(2, show = setOf("q"), light = setOf("q")),
                    Beat(3, light = setOf("v", "s", "c"), pulse = "q" to "c", card = BoardCard("Imperativ with Sie", "Like the question, without the question: verb first, Sie second.")),
                    Beat(4, light = setOf("c"), card = BoardCard("Nehmen Sie die Tabletten!", "Take the tablets! Bleiben Sie im Bett: stay in bed!", phrase = true)),
                ),
            ),
            // 5: "Can I drop the «Sie»? It's shorter."
            Scene(
                5, "Sie stays, bitte softens",
                DiagramSpec(
                    "Sie never drops out of the polite command, and bitte turns an order into a request.",
                    nodes = listOf(
                        DNode("no", "Nehmen die Tabletten", 0.5f, 0.14f, w = 0.8f, h = 0.18f, tone = 2, sub = "Sie missing: wrong"),
                        DNode("yes", "Nehmen Sie die Tabletten", 0.5f, 0.46f, w = 0.8f, h = 0.18f, sub = "Sie stays"),
                        DNode("b", "Nehmen Sie bitte Platz", 0.5f, 0.8f, w = 0.8f, h = 0.18f, kind = NodeKind.NOTE, tone = 1, sub = "bitte: softer"),
                    ),
                    edges = listOf(
                        DEdge("no", "yes", "add Sie"),
                        DEdge("yes", "b", "add bitte"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("no"), light = setOf("no")),
                    Beat(6, show = setOf("yes", "b"), light = setOf("yes", "b"), pulse = "yes" to "b"),
                    Beat(7, light = setOf("b"), card = BoardCard("Kommen Sie herein!", "Come in! Nehmen Sie bitte Platz: please take a seat.", phrase = true)),
                ),
            ),
            // 9: "In, inside. Now the friendly command, for one friend, with «du». Here'..."
            Scene(
                9, "The du and ihr commands",
                DiagramSpec(
                    "du: the du form with -st cut off; the vowel change stays, the umlaut drops. ihr: the ihr form without ihr.",
                    nodes = listOf(
                        DNode("a1", "du trinkst", 0.2f, 0.12f, w = 0.36f, tone = 1),
                        DNode("b1", "Trink!", 0.8f, 0.12f, w = 0.34f, sub = "cut -st"),
                        DNode("a2", "du nimmst", 0.2f, 0.37f, w = 0.36f, tone = 1),
                        DNode("b2", "Nimm!", 0.8f, 0.37f, w = 0.34f, sub = "vowel stays"),
                        DNode("a3", "du schläfst", 0.2f, 0.62f, w = 0.36f, tone = 1),
                        DNode("b3", "Schlaf!", 0.8f, 0.62f, w = 0.34f, sub = "umlaut drops"),
                        DNode("a4", "ihr nehmt", 0.2f, 0.87f, w = 0.36f, tone = 1),
                        DNode("b4", "Nehmt!", 0.8f, 0.87f, w = 0.34f, sub = "drop ihr"),
                    ),
                    edges = listOf(
                        DEdge("a1", "b1"),
                        DEdge("a2", "b2"),
                        DEdge("a3", "b3"),
                        DEdge("a4", "b4"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("a1", "b1"), light = setOf("a1", "b1"), pulse = "a1" to "b1"),
                    Beat(10, light = setOf("b1"), card = BoardCard("Trink viel! Bleib im Bett!", "Drink a lot! Stay in bed!", phrase = true)),
                    Beat(12, show = setOf("a2", "b2"), light = setOf("a2", "b2"), pulse = "a2" to "b2"),
                    Beat(13, light = setOf("b2"), card = BoardCard("Nimm die Tabletten!", "Take the tablets!", phrase = true)),
                    Beat(15, show = setOf("a3", "b3"), light = setOf("a3", "b3"), pulse = "a3" to "b3", card = BoardCard("Not schläf", "The umlaut drops: du schläfst gives schlaf, du fährst gives fahr.")),
                    Beat(16, light = setOf("b3"), card = BoardCard("Schlaf gut!", "Sleep well!", phrase = true)),
                    Beat(18, show = setOf("a4", "b4"), light = setOf("a4", "b4"), pulse = "a4" to "b4"),
                    Beat(19, light = setOf("b4"), card = BoardCard("Schlaft gut, Kinder!", "Sleep well, children!", phrase = true)),
                ),
            ),
            // 20: "And «sein»? That one's always strange."
            Scene(
                20, "sein, haben, anrufen",
                DiagramSpec(
                    "sein is irregular; haben is regular; a split verb sends its little piece to the end.",
                    nodes = listOf(
                        DNode("s", "sein", 0.2f, 0.16f, w = 0.32f, h = 0.2f, kind = NodeKind.PILL, tone = 1),
                        DNode("s2", "Seien Sie / sei / seid", 0.68f, 0.16f, w = 0.56f, h = 0.2f),
                        DNode("h", "haben", 0.2f, 0.5f, w = 0.32f, h = 0.2f, kind = NodeKind.PILL, tone = 1),
                        DNode("h2", "Haben Sie Geduld!", 0.68f, 0.5f, w = 0.56f, h = 0.2f, sub = "die Geduld: patience"),
                        DNode("a", "anrufen", 0.2f, 0.84f, w = 0.32f, h = 0.2f, kind = NodeKind.PILL, tone = 1),
                        DNode("a2", "Rufen Sie … an!", 0.68f, 0.84f, w = 0.56f, h = 0.2f, sub = "an goes to the end"),
                    ),
                    edges = listOf(
                        DEdge("s", "s2"),
                        DEdge("h", "h2"),
                        DEdge("a", "a2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("s"), light = setOf("s")),
                    Beat(21, show = setOf("s2", "h", "h2"), light = setOf("s2", "h2"), pulse = "h" to "h2"),
                    Beat(22, card = BoardCard("Sei ruhig! Haben Sie Geduld!", "Be quiet! Be patient!", phrase = true)),
                    Beat(23, show = setOf("a"), light = setOf("a")),
                    Beat(24, show = setOf("a2"), light = setOf("a2"), pulse = "a" to "a2"),
                    Beat(25, light = setOf("a2"), card = BoardCard("Rufen Sie morgen in der Praxis an!", "Call the surgery tomorrow!", phrase = true)),
                ),
            ),
            // 26: "Okay, commands done. You said there was a pattern from how are you?"
            Scene(
                26, "Mir geht es",
                DiagramSpec(
                    "Mir geht es gut uses a Dativpronomen: the to-me form. Ihnen is the formal one, with a capital.",
                    nodes = listOf(
                        DNode("n1", "ich", 0.27f, 0.09f, w = 0.36f, h = 0.13f, tone = 2),
                        DNode("d1", "mir", 0.73f, 0.09f, w = 0.36f, h = 0.13f),
                        DNode("n2", "du", 0.27f, 0.25f, w = 0.36f, h = 0.13f, tone = 2),
                        DNode("d2", "dir", 0.73f, 0.25f, w = 0.36f, h = 0.13f),
                        DNode("n3", "er", 0.27f, 0.41f, w = 0.36f, h = 0.13f, tone = 2),
                        DNode("d3", "ihm", 0.73f, 0.41f, w = 0.36f, h = 0.13f),
                        DNode("n4", "sie", 0.27f, 0.57f, w = 0.36f, h = 0.13f, tone = 2),
                        DNode("d4", "ihr", 0.73f, 0.57f, w = 0.36f, h = 0.13f),
                        DNode("n5", "Sie", 0.27f, 0.73f, w = 0.36f, h = 0.13f, tone = 2),
                        DNode("d5", "Ihnen", 0.73f, 0.73f, w = 0.36f, h = 0.13f),
                        DNode("n6", "wir", 0.27f, 0.9f, w = 0.36f, h = 0.13f, tone = 2),
                        DNode("d6", "uns", 0.73f, 0.9f, w = 0.36f, h = 0.13f),
                    ),
                    edges = listOf(
                        DEdge("n1", "d1"),
                        DEdge("n2", "d2"),
                        DEdge("n3", "d3"),
                        DEdge("n4", "d4"),
                        DEdge("n5", "d5"),
                        DEdge("n6", "d6"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26),
                    Beat(27, show = setOf("n1", "d1", "n2", "d2", "n3", "d3", "n4", "d4", "n5", "d5", "n6", "d6"), light = setOf("d1"), pulse = "n1" to "d1"),
                    Beat(28, light = setOf("d1", "d2"), card = BoardCard("Wie geht es dir?", "How are you? Mir geht es nicht gut: I'm not well.", phrase = true)),
                    Beat(30, light = setOf("d5"), pulse = "n5" to "d5", card = BoardCard("Not Ich geht es gut", "Always Mir geht es gut. Formal: Wie geht es Ihnen?")),
                    Beat(31, light = setOf("d5"), card = BoardCard("Was fehlt Ihnen?", "What's wrong? Kann ich Ihnen helfen: can I help you?", phrase = true)),
                    Beat(34, light = setOf("d3", "d4"), card = BoardCard("Ihm tut der Rücken weh.", "His back hurts. Geht es ihr besser: is she better?", phrase = true)),
                ),
            ),
            // 35: "So what should I remember for the Goethe exam?"
            Scene(
                35, "For the Goethe exam",
                DiagramSpec(
                    "What the Imperativ means for the Goethe A1: instructions to hear, requests to make, du forms to build.",
                    nodes = listOf(
                        DNode("hear", "Listening", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "verb + Sie = instruction"),
                        DNode("speak", "Speaking", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "command + bitte"),
                        DNode("du", "du command", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "cut -st, vowel stays, umlaut drops"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("hear", "speak", "du"), card = BoardCard("Bleiben Sie zu Hause.", "An instruction, not a question: stay at home.")),
                    Beat(38, card = BoardCard("Today", "Five Sie instructions, the same five with du, three with mir geht es, two with bitte.")),
                    Beat(39, card = BoardCard("Bis bald, und schlaf gut!", "See you soon, and sleep well!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de8Dialogue() = PodcastBoard(
        "de8-dialogue",
        listOf(
            // 0: "Welcome back. Today is two conversations you'll really have: the phone..."
            Scene(
                0, "Booking by phone",
                DiagramSpec(
                    "The call: the surgery's name, then Sanya: her name, I need an appointment, and since when.",
                    nodes = listOf(
                        DNode("san", "Sanya", 0.16f, 0.18f, w = 0.24f, h = 0.2f, kind = NodeKind.USER),
                        DNode("pr", "die Praxis", 0.72f, 0.18f, w = 0.44f, h = 0.18f, tone = 1, sub = "Doktor Müller"),
                        DNode("t", "der Termin", 0.5f, 0.52f, w = 0.5f, h = 0.18f, sub = "the appointment"),
                        DNode("s", "seit zwei Tagen", 0.5f, 0.84f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, sub = "present tense: ich habe"),
                    ),
                    edges = listOf(
                        DEdge("san", "pr", "phone"),
                        DEdge("pr", "t"),
                        DEdge("t", "s", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("san")),
                    Beat(1, show = setOf("pr"), light = setOf("pr"), pulse = "san" to "pr", card = BoardCard("Praxis Doktor Müller, guten Tag.", "Doctor Müller's surgery, good morning.", phrase = true)),
                    Beat(3, show = setOf("t"), light = setOf("t")),
                    Beat(4, light = setOf("t"), card = BoardCard("Ich brauche einen Termin.", "I need an appointment.", phrase = true)),
                    Beat(6, show = setOf("s"), light = setOf("s"), pulse = "t" to "s"),
                    Beat(7, light = setOf("s"), card = BoardCard("Ich habe seit zwei Tagen Fieber.", "I've had a fever for two days (und Halsschmerzen: and a sore throat).", phrase = true)),
                    Beat(9, light = setOf("s"), card = BoardCard("Not ich hatte", "With seit, German keeps the present tense: ich habe.")),
                ),
            ),
            // 10: "Können Sie heute um fünfzehn Uhr dreißig kommen?"
            Scene(
                10, "A time and the card",
                DiagramSpec(
                    "The surgery offers a time; Sanya says Das passt and asks what to bring: the Versichertenkarte.",
                    nodes = listOf(
                        DNode("clock", "fünfzehn Uhr dreißig", 0.5f, 0.14f, w = 0.7f, h = 0.18f, tone = 1, sub = "half past three"),
                        DNode("ok", "Das passt.", 0.27f, 0.48f, w = 0.44f, h = 0.18f, sub = "that works"),
                        DNode("q", "Was muss ich mitbringen?", 0.75f, 0.48f, w = 0.44f, h = 0.22f),
                        DNode("vk", "die Versichertenkarte", 0.5f, 0.84f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, sub = "bringen … mit"),
                    ),
                    edges = listOf(
                        DEdge("clock", "ok"),
                        DEdge("ok", "q"),
                        DEdge("q", "vk"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("clock"), light = setOf("clock"), card = BoardCard("Können Sie heute um 15.30 kommen?", "Can you come today at half past three?", phrase = true)),
                    Beat(12, show = setOf("ok"), light = setOf("ok"), pulse = "clock" to "ok"),
                    Beat(13, show = setOf("q"), light = setOf("q"), pulse = "ok" to "q", card = BoardCard("Was muss ich mitbringen?", "What do I have to bring? Ja, das passt: yes, that works.", phrase = true)),
                    Beat(15, show = setOf("vk"), light = setOf("vk"), pulse = "q" to "vk"),
                    Beat(16, light = setOf("vk"), card = BoardCard("Bitte bringen Sie Ihre Karte mit.", "Please bring your (insurance) card: die Versichertenkarte.", phrase = true)),
                ),
            ),
            // 17: "Booked. So now we're in the waiting room?"
            Scene(
                17, "With the doctor",
                DiagramSpec(
                    "The visit in four steps: what is wrong, since when, the diagnosis, the instructions.",
                    nodes = listOf(
                        DNode("a", "Was fehlt Ihnen?", 0.5f, 0.12f, w = 0.7f, tone = 1, sub = "what's wrong?"),
                        DNode("b", "Ich habe … seit …", 0.5f, 0.37f, w = 0.7f, sub = "the patient"),
                        DNode("c", "Das ist …", 0.5f, 0.62f, w = 0.7f, tone = 2, sub = "the diagnosis"),
                        DNode("d", "Bleiben Sie …! Nehmen Sie …!", 0.5f, 0.87f, w = 0.8f, tone = 1, sub = "the instructions"),
                    ),
                    edges = listOf(
                        DEdge("a", "b"),
                        DEdge("b", "c"),
                        DEdge("c", "d"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17),
                    Beat(18, show = setOf("a"), light = setOf("a")),
                    Beat(19, light = setOf("a"), card = BoardCard("Was fehlt Ihnen?", "What's wrong? Not: what is missing.", phrase = true)),
                    Beat(21, show = setOf("b"), light = setOf("b"), pulse = "a" to "b"),
                    Beat(22, light = setOf("b"), card = BoardCard("Mein Hals tut weh.", "My throat hurts. Ich kann nicht gut schlafen: I can't sleep well.", phrase = true)),
                    Beat(24, show = setOf("c"), light = setOf("c"), pulse = "b" to "c"),
                    Beat(25, light = setOf("c"), card = BoardCard("Öffnen Sie bitte den Mund.", "Open your mouth, please. Das ist eine Erkältung: that's a cold.", phrase = true)),
                ),
            ),
            // 27: "Here come the instructions, and they're all «Sie» commands: verb first..."
            Scene(
                27, "The doctor's instructions",
                DiagramSpec(
                    "Every instruction at the surgery is a Sie command: verb first, Sie second, and not a question.",
                    nodes = listOf(
                        DNode("rule", "verb + Sie", 0.5f, 0.12f, w = 0.6f, kind = NodeKind.PILL, sub = "an instruction"),
                        DNode("b", "Bleiben Sie", 0.2f, 0.5f, w = 0.34f, h = 0.22f, tone = 1, sub = "drei Tage zu Hause"),
                        DNode("t", "Trinken Sie", 0.5f, 0.5f, h = 0.22f, tone = 1, sub = "viel Tee"),
                        DNode("n", "Nehmen Sie", 0.82f, 0.5f, w = 0.32f, h = 0.22f, tone = 1, sub = "zweimal am Tag"),
                        DNode("how", "How many days? How often?", 0.5f, 0.86f, w = 0.8f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("rule", "b"),
                        DEdge("rule", "t"),
                        DEdge("rule", "n"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("rule"), light = setOf("rule")),
                    Beat(28, show = setOf("b", "t"), light = setOf("b", "t"), pulse = "rule" to "b", card = BoardCard("Bleiben Sie drei Tage zu Hause.", "Stay at home for three days. Trinken Sie viel Tee: drink a lot of tea.", phrase = true)),
                    Beat(30, show = setOf("how"), light = setOf("how")),
                    Beat(31, show = setOf("n"), light = setOf("n"), pulse = "rule" to "n", card = BoardCard("Nehmen Sie diese Tabletten.", "Take these tablets, zweimal am Tag: twice a day.", phrase = true)),
                ),
            ),
            // 32: "Sanya goes to the language school. Does she need a note for missing cl..."
            Scene(
                32, "A sick note and a wish",
                DiagramSpec(
                    "Ask for the Krankmeldung for work or school; the doctor ends with Gute Besserung.",
                    nodes = listOf(
                        DNode("km", "die Krankmeldung", 0.5f, 0.14f, w = 0.7f, h = 0.2f, sub = "sick note"),
                        DNode("sch", "die Schule", 0.27f, 0.5f, w = 0.44f, h = 0.18f, tone = 1, sub = "school"),
                        DNode("fr", "bis Freitag", 0.75f, 0.5f, w = 0.42f, h = 0.18f, tone = 1, sub = "until Friday"),
                        DNode("gb", "Gute Besserung!", 0.5f, 0.84f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, sub = "get well soon"),
                    ),
                    edges = listOf(
                        DEdge("km", "sch", "for"),
                        DEdge("km", "fr"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32),
                    Beat(33, show = setOf("km"), light = setOf("km")),
                    Beat(34, show = setOf("sch"), light = setOf("km", "sch"), pulse = "km" to "sch", card = BoardCard("Brauche ich eine Krankmeldung?", "Do I need a sick note (für die Schule: for school)?", phrase = true)),
                    Beat(36, show = setOf("fr"), light = setOf("fr"), pulse = "km" to "fr"),
                    Beat(37, show = setOf("gb"), light = setOf("gb"), card = BoardCard("Gute Besserung!", "Get well soon! Hier ist die Krankmeldung bis Freitag.", phrase = true)),
                ),
            ),
            // 38: "So what should I remember for the Goethe exam?"
            Scene(
                38, "For the Goethe exam",
                DiagramSpec(
                    "What the doctor's dialogues mean for the Goethe A1: times and how often, instructions, and seit.",
                    nodes = listOf(
                        DNode("hear", "Listening", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "day, time, how often"),
                        DNode("inst", "verb + Sie", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "an instruction"),
                        DNode("speak", "Speaking", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "seit + present tense"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(39, show = setOf("hear", "inst", "speak"), card = BoardCard("zweimal am Tag", "Times and how often are what the listening part tests.")),
                    Beat(41, card = BoardCard("Today", "To the mirror: what hurts, how long with seit, what to do, a Krankmeldung?")),
                    Beat(42, card = BoardCard("Tschüss! Bis bald!", "Bye! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de8Practice() = PodcastBoard(
        "de8-practice",
        listOf(
            // 0: "Welcome back. Today is practice, in three exam parts: reading a surger..."
            Scene(
                0, "Surgery hours",
                DiagramSpec(
                    "Read every line with its small words: auch adds hours, geschlossen takes them away.",
                    nodes = listOf(
                        DNode("h", "Sprechzeiten", 0.5f, 0.08f, w = 0.6f, h = 0.12f, kind = NodeKind.PILL),
                        DNode("m", "Montag bis Freitag", 0.5f, 0.27f, w = 0.86f, tone = 1, sub = "8 bis 12 Uhr"),
                        DNode("a", "Montag und Donnerstag", 0.5f, 0.47f, w = 0.86f, tone = 1, sub = "auch 15 bis 18 Uhr"),
                        DNode("w", "Mittwochnachmittag", 0.5f, 0.67f, w = 0.86f, tone = 2, sub = "geschlossen"),
                        DNode("t", "Termine", 0.27f, 0.89f, w = 0.44f, kind = NodeKind.NOTE, sub = "telefonisch"),
                        DNode("n", "der Notfall", 0.75f, 0.89f, w = 0.42f, kind = NodeKind.NOTE, sub = "112"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0),
                    Beat(2, show = setOf("h"), light = setOf("h")),
                    Beat(3, show = setOf("m"), light = setOf("m"), card = BoardCard("Montag bis Freitag, acht bis zwölf", "Monday to Friday, eight to twelve.", phrase = true)),
                    Beat(6, show = setOf("a", "w"), light = setOf("a", "w"), card = BoardCard("Mittwochnachmittag geschlossen.", "Wednesday afternoon closed. Montag und Donnerstag auch: also Mon and Thu.", phrase = true)),
                    Beat(8, light = setOf("w"), card = BoardCard("Richtig oder falsch?", "Wednesday afternoon: falsch, it's closed. Thursday afternoon: richtig, auch.")),
                    Beat(9, show = setOf("t", "n"), light = setOf("t", "n"), card = BoardCard("Termine bitte telefonisch.", "Appointments by phone, please. Im Notfall: in an emergency, 112.", phrase = true)),
                ),
            ),
            // 11: "Yes, «der Notfall», and one one two is the number. Now the second read..."
            Scene(
                11, "The medicine leaflet",
                DiagramSpec(
                    "The Packungsbeilage: read the dose with the age and the limit beside it.",
                    nodes = listOf(
                        DNode("pb", "die Packungsbeilage", 0.5f, 0.1f, w = 0.8f, h = 0.14f, kind = NodeKind.PILL, sub = "medicine leaflet"),
                        DNode("d", "dreimal täglich", 0.27f, 0.38f, w = 0.44f, h = 0.2f, tone = 1, sub = "nach dem Essen"),
                        DNode("k", "Kinder unter 6", 0.75f, 0.38f, w = 0.44f, h = 0.2f, tone = 2, sub = "nicht geeignet"),
                        DNode("x", "max. 7 Tage", 0.27f, 0.7f, w = 0.44f, h = 0.2f, tone = 1, sub = "nicht mehr als"),
                        DNode("f", "Bei Fieber", 0.75f, 0.7f, w = 0.44f, h = 0.2f, tone = 1, sub = "Arzt fragen"),
                    ),
                    edges = listOf(
                        DEdge("pb", "d"),
                        DEdge("pb", "k"),
                        DEdge("pb", "x"),
                        DEdge("pb", "f"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("pb"), light = setOf("pb")),
                    Beat(12, show = setOf("d", "k"), light = setOf("d", "k"), pulse = "pb" to "d", card = BoardCard("Dreimal täglich nach dem Essen.", "Three times daily after meals. Kinder unter sechs: not suitable.", phrase = true)),
                    Beat(14, light = setOf("k"), pulse = "pb" to "k", card = BoardCard("A child of four?", "Falsch: under six is not suitable. Read the age beside the dose.")),
                    Beat(15, show = setOf("x", "f"), light = setOf("x", "f"), card = BoardCard("Nicht mehr als sieben Tage nehmen.", "Don't take it for more than seven days. Bei Fieber: ask a doctor.", phrase = true)),
                ),
            ),
            // 16: "Okay, now writing. Sanya's ill, and I can't visit her today."
            Scene(
                16, "A get-well message",
                DiagramSpec(
                    "Four points: ask how she is, say why you can't come, offer something, end with the wish.",
                    nodes = listOf(
                        DNode("p1", "Wie geht es dir?", 0.5f, 0.11f, w = 0.8f, tone = 1, sub = "1 ask"),
                        DNode("p2", "leider nicht kommen", 0.5f, 0.37f, w = 0.8f, tone = 1, sub = "2 why: Ich muss arbeiten."),
                        DNode("p3", "Soll ich dir … bringen?", 0.5f, 0.63f, w = 0.8f, tone = 1, sub = "3 offer for tomorrow"),
                        DNode("p4", "Gute Besserung!", 0.5f, 0.89f, w = 0.8f, sub = "4 the wish"),
                    ),
                    edges = listOf(
                        DEdge("p1", "p2"),
                        DEdge("p2", "p3"),
                        DEdge("p3", "p4"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16),
                    Beat(17, show = setOf("p1"), light = setOf("p1")),
                    Beat(18, light = setOf("p1"), card = BoardCard("Liebe Sanya, wie geht es dir?", "Dear Sanya, how are you?", phrase = true)),
                    Beat(20, show = setOf("p2"), light = setOf("p2"), pulse = "p1" to "p2"),
                    Beat(21, light = setOf("p2"), card = BoardCard("Ich kann heute leider nicht kommen.", "Unfortunately I can't come today. Ich muss arbeiten: I have to work.", phrase = true)),
                    Beat(23, show = setOf("p3"), light = setOf("p3"), pulse = "p2" to "p3"),
                    Beat(24, light = setOf("p3"), card = BoardCard("Soll ich dir morgen Suppe bringen?", "Shall I bring you soup tomorrow?", phrase = true)),
                    Beat(26, show = setOf("p4"), light = setOf("p4"), pulse = "p3" to "p4"),
                    Beat(27, light = setOf("p4"), card = BoardCard("Gute Besserung!", "Get well soon! Bleib im Bett und schlaf viel: stay in bed, sleep a lot.", phrase = true)),
                ),
            ),
            // 28: "Now speaking. What's a request in the exam?"
            Scene(
                28, "Picture card to request",
                DiagramSpec(
                    "Sprechen: a picture card, a request with bitte, and a friendly answer.",
                    nodes = listOf(
                        DNode("pic", "a glass of water", 0.5f, 0.12f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "the picture card"),
                        DNode("q", "Kannst du mir bitte … geben?", 0.5f, 0.48f, w = 0.86f, h = 0.2f, sub = "the request"),
                        DNode("a", "Ja, gern. Hier, bitte.", 0.5f, 0.84f, w = 0.7f, h = 0.18f, tone = 1, sub = "the answer"),
                    ),
                    edges = listOf(
                        DEdge("pic", "q"),
                        DEdge("q", "a"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28),
                    Beat(29, show = setOf("pic", "q"), light = setOf("pic", "q"), pulse = "pic" to "q"),
                    Beat(30, light = setOf("q"), card = BoardCard("Kannst du mir bitte Wasser geben?", "Can you give me (a glass of) water, please?", phrase = true)),
                    Beat(32, show = setOf("a"), light = setOf("a"), pulse = "q" to "a"),
                    Beat(33, light = setOf("a"), card = BoardCard("Ja, gern. Hier, bitte.", "Yes, gladly. Here you are.", phrase = true)),
                ),
            ),
            // 35: "A «Sie» or «du» command, always with «bitte», so it's a request, not a..."
            Scene(
                35, "Command plus bitte",
                DiagramSpec(
                    "A command without bitte is an order; with bitte it is a request.",
                    nodes = listOf(
                        DNode("o", "command alone", 0.27f, 0.16f, w = 0.44f, h = 0.2f, tone = 2, sub = "an order"),
                        DNode("r", "command + bitte", 0.75f, 0.16f, w = 0.44f, h = 0.2f, sub = "a request"),
                        DNode("s", "Öffnen Sie bitte …!", 0.27f, 0.62f, w = 0.44f, h = 0.22f, tone = 1, sub = "Sie: the window"),
                        DNode("d", "Ruf bitte … an!", 0.75f, 0.62f, w = 0.44f, h = 0.22f, tone = 1, sub = "du: the doctor"),
                    ),
                    edges = listOf(
                        DEdge("o", "r", "add bitte"),
                        DEdge("r", "s"),
                        DEdge("r", "d"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(35, show = setOf("o", "r"), light = setOf("r"), pulse = "o" to "r"),
                    Beat(36, show = setOf("s", "d"), light = setOf("s", "d"), card = BoardCard("Öffnen Sie bitte das Fenster!", "Please open the window! Ruf bitte den Arzt an: please call the doctor!", phrase = true)),
                ),
            ),
            // 37: "So what should I remember for the Goethe exam?"
            Scene(
                37, "For the Goethe exam",
                DiagramSpec(
                    "What this practice means for the Goethe A1: reading, writing and speaking.",
                    nodes = listOf(
                        DNode("read", "Reading", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "bis, auch, nicht, unter"),
                        DNode("wr", "Writing", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "four points + the wish"),
                        DNode("sp", "Speaking", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "bitte, then Ja, gern"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("read", "wr", "sp"), card = BoardCard("Small words, big meaning", "geschlossen, auch, nicht, unter: they decide richtig or falsch.")),
                    Beat(40, card = BoardCard("Today", "Phone call with seit; three Sie and three du instructions; a message; a leaflet.")),
                    Beat(41, card = BoardCard("Bis bald! Bleib gesund!", "See you soon! Stay healthy!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de9Words() = PodcastBoard(
        "de9-words",
        listOf(
            // 0: "Hi, and welcome back. Today is free time and weather: hobbies, sport, ..."
            Scene(
                0, "Hobbies",
                DiagramSpec(
                    "A hobby is often just a verb. With a noun, say what you do with it: Wir spielen Fußball.",
                    nodes = listOf(
                        DNode("fz", "die Freizeit", 0.5f, 0.14f, w = 0.5f, kind = NodeKind.PILL, tone = 1, sub = "free time"),
                        DNode("verb", "a verb", 0.25f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "schwimmen, lesen, kochen"),
                        DNode("noun", "spielen + noun", 0.75f, 0.5f, w = 0.42f, h = 0.2f, sub = "der Fußball"),
                        DNode("hobby", "das Hobby", 0.5f, 0.86f, w = 0.44f, kind = NodeKind.NOTE, tone = 2, sub = "Mein Hobby ist ..."),
                    ),
                    edges = listOf(
                        DEdge("fz", "verb"),
                        DEdge("fz", "noun"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("fz")),
                    Beat(1, light = setOf("fz"), card = BoardCard("Was machst du in deiner Freizeit?", "What do you do in your free time?", phrase = true)),
                    Beat(3, show = setOf("verb", "hobby"), light = setOf("verb"), pulse = "fz" to "verb"),
                    Beat(4, light = setOf("hobby"), card = BoardCard("Mein Hobby ist Fußball.", "My hobby is football.", phrase = true)),
                    Beat(6, show = setOf("noun"), light = setOf("noun"), pulse = "fz" to "noun"),
                    Beat(7, light = setOf("noun"), card = BoardCard("Wir spielen Fußball.", "We play football.", phrase = true)),
                ),
            ),
            // 8: "And if I just like swimming? How do I say I like it?"
            Scene(
                8, "Like, prefer, like most",
                DiagramSpec(
                    "Likes in three steps: gern, lieber, am liebsten. The verb stays the same; the little word changes.",
                    nodes = listOf(
                        DNode("c", "am liebsten", 0.5f, 0.16f, w = 0.5f, h = 0.18f, sub = "most of all"),
                        DNode("b", "lieber", 0.5f, 0.5f, w = 0.5f, h = 0.18f, tone = 1, sub = "I prefer"),
                        DNode("a", "gern", 0.5f, 0.84f, w = 0.5f, h = 0.18f, tone = 2, sub = "I like"),
                    ),
                    edges = listOf(
                        DEdge("a", "b"),
                        DEdge("b", "c"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("a"), light = setOf("a"), card = BoardCard("Verb, then gern", "Ich schwimme gern: I like swimming. gern comes after the verb.")),
                    Beat(10, light = setOf("a"), card = BoardCard("Ich schwimme gern. Ich lese gern.", "I like swimming. I like reading.", phrase = true)),
                    Beat(12, show = setOf("b", "c"), light = setOf("b"), pulse = "a" to "b"),
                    Beat(13, light = setOf("b"), card = BoardCard("Ich koche lieber.", "I prefer cooking (and Ich lese gern: I like reading).", phrase = true)),
                    Beat(15, light = setOf("c"), pulse = "b" to "c"),
                    Beat(16, light = setOf("c"), card = BoardCard("Am liebsten tanze ich.", "Most of all I like dancing.", phrase = true)),
                ),
            ),
            // 17: "Wait, why is «ich» after the verb there?"
            Scene(
                17, "Verb second",
                DiagramSpec(
                    "When am liebsten opens the sentence, the verb keeps second place and ich moves behind it.",
                    nodes = listOf(
                        DNode("p1", "Am liebsten", 0.21f, 0.28f, w = 0.36f, h = 0.2f, tone = 2, sub = "first place"),
                        DNode("p2", "tanze", 0.53f, 0.28f, w = 0.22f, h = 0.2f, sub = "verb second"),
                        DNode("p3", "ich", 0.81f, 0.28f, w = 0.28f, h = 0.2f, tone = 1, sub = "then ich"),
                        DNode("f1", "Fahrrad fahren", 0.26f, 0.76f, w = 0.46f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "to cycle"),
                        DNode("f2", "spazieren gehen", 0.75f, 0.76f, w = 0.46f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "to go for a walk"),
                    ),
                    edges = listOf(
                        DEdge("p1", "p2"),
                        DEdge("p2", "p3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("p1", "p2", "p3")),
                    Beat(18, show = setOf("f1", "f2"), light = setOf("p2"), pulse = "p1" to "p2", card = BoardCard("The verb wants second place", "Am liebsten takes first place, so ich moves behind the verb.")),
                    Beat(19, light = setOf("f1", "f2"), card = BoardCard("Ich fahre gern Fahrrad.", "I like cycling. And: Gehen wir spazieren? Shall we go for a walk?", phrase = true)),
                ),
            ),
            // 20: "And if someone asks me about a film I watched?"
            Scene(
                20, "Funny or boring?",
                DiagramSpec(
                    "Three words for a film or a book. langweilig starts with lang, long, but it means boring.",
                    nodes = listOf(
                        DNode("lu", "lustig", 0.5f, 0.14f, w = 0.5f, tone = 1, sub = "funny"),
                        DNode("in", "interessant", 0.5f, 0.42f, w = 0.5f, tone = 1, sub = "interesting"),
                        DNode("la", "langweilig", 0.5f, 0.7f, w = 0.5f, sub = "boring, not long!"),
                        DNode("film", "der Film", 0.22f, 0.92f, w = 0.34f, h = 0.13f, kind = NodeKind.NOTE, tone = 2),
                        DNode("buch", "das Buch", 0.78f, 0.92f, w = 0.34f, h = 0.13f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("lu", "in", "la"), light = setOf("la"), card = BoardCard("langweilig: the lang trap", "It means boring. A two-minute video can be langweilig too.")),
                    Beat(22, show = setOf("film", "buch"), light = setOf("film", "buch"), card = BoardCard("Der Film ist langweilig.", "The film is boring. Das Buch ist interessant: the book is interesting.", phrase = true)),
                ),
            ),
            // 23: "Good to know before I call a long film boring by accident. What about ..."
            Scene(
                23, "The weather",
                DiagramSpec(
                    "Es ist plus a word for warm, hot and cold; the rain verb stands alone: Es regnet.",
                    nodes = listOf(
                        DNode("w", "das Wetter", 0.5f, 0.13f, w = 0.5f, h = 0.18f, kind = NodeKind.PILL, sub = "Wie ist das Wetter?"),
                        DNode("sonne", "die Sonne", 0.25f, 0.47f, w = 0.42f, h = 0.2f, tone = 1, sub = "heiß, warm, sonnig"),
                        DNode("regen", "der Regen", 0.75f, 0.47f, w = 0.42f, h = 0.2f, tone = 1, sub = "Es regnet."),
                        DNode("grad", "Grad", 0.5f, 0.83f, w = 0.5f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "dreißig Grad"),
                    ),
                    edges = listOf(
                        DEdge("w", "sonne"),
                        DEdge("w", "regen"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("w"), light = setOf("w")),
                    Beat(25, light = setOf("w"), card = BoardCard("Wie ist das Wetter?", "What is the weather like?", phrase = true)),
                    Beat(27, show = setOf("sonne"), light = setOf("sonne"), pulse = "w" to "sonne", card = BoardCard("sonnig / regnerisch", "Sunny / rainy. Es ist heiß, warm, kalt: it is hot, warm, cold.")),
                    Beat(28, light = setOf("sonne"), card = BoardCard("In Doha ist es sehr heiß.", "In Doha it is very hot. Die Sonne scheint: the sun is shining.", phrase = true)),
                    Beat(30, show = setOf("regen", "grad"), light = setOf("regen"), pulse = "w" to "regen", card = BoardCard("Es regnet, never Es ist regnen", "The weather verb stands alone. For degrees: the number, then Grad.")),
                    Beat(31, light = setOf("regen", "grad"), card = BoardCard("Es regnet. Es sind dreißig Grad.", "It is raining. It is thirty degrees.", phrase = true)),
                ),
            ),
            // 32: "And the seasons? Are they der, die or das?"
            Scene(
                32, "Seasons and clubs",
                DiagramSpec(
                    "All four seasons are der and take im. Sport often happens in a Verein; a Sunday trip is an Ausflug.",
                    nodes = listOf(
                        DNode("fr", "der Frühling", 0.25f, 0.12f, w = 0.44f, h = 0.18f, tone = 1, sub = "spring"),
                        DNode("so", "der Sommer", 0.75f, 0.12f, w = 0.44f, h = 0.18f, tone = 1, sub = "summer"),
                        DNode("he", "der Herbst", 0.75f, 0.42f, w = 0.44f, h = 0.18f, tone = 1, sub = "autumn"),
                        DNode("wi", "der Winter", 0.25f, 0.42f, w = 0.44f, h = 0.18f, tone = 1, sub = "winter"),
                        DNode("ve", "der Verein", 0.25f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "club"),
                        DNode("au", "der Ausflug", 0.75f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "day trip"),
                    ),
                    edges = listOf(
                        DEdge("fr", "so"),
                        DEdge("so", "he"),
                        DEdge("he", "wi"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("fr", "so", "he", "wi"), card = BoardCard("im + season or month", "im Frühling, im Sommer, im Herbst, im Winter, im Januar.")),
                    Beat(34, light = setOf("wi", "he"), pulse = "he" to "wi", card = BoardCard("Im Winter ist es kalt.", "In winter it is cold. Im Herbst regnet es oft: in autumn it often rains.", phrase = true)),
                    Beat(36, show = setOf("ve", "au"), light = setOf("ve")),
                    Beat(37, light = setOf("au"), card = BoardCard("Am Sonntag machen wir einen Ausflug.", "On Sunday we are going on a day trip. We hike in the mountains.", phrase = true)),
                ),
            ),
            // 38: "So what should I remember for the Goethe exam?"
            Scene(
                38, "For the Goethe exam",
                DiagramSpec(
                    "Free time and weather in the Goethe A1: the Hobby card, the weather in Hören, and the ladder.",
                    nodes = listOf(
                        DNode("speak", "Sprechen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Ich schwimme gern."),
                        DNode("hear", "Hören", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Regen, Sonne, Grad"),
                        DNode("ladder", "gern, lieber, am liebsten", 0.5f, 0.56f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "which step?"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(39, show = setOf("speak", "hear", "ladder"), card = BoardCard("Hobby is on the card", "Have a full sentence ready: Ich schwimme gern.")),
                    Beat(41, card = BoardCard("Today", "Three likes and one preference out loud, then today's weather in one sentence.")),
                    Beat(42, card = BoardCard("Viel Spaß in der Freizeit! Tschüss!", "Have fun in your free time! Bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de9Grammar() = PodcastBoard(
        "de9-grammar",
        listOf(
            // 0: "Hi, and welcome back. Today is grammar, in three small pieces: how to ..."
            Scene(
                0, "gern or mögen",
                DiagramSpec(
                    "gern goes after a verb and likes doing it; mögen goes with a noun and likes the thing.",
                    nodes = listOf(
                        DNode("gern", "verb + gern", 0.25f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "liking an activity"),
                        DNode("mag", "mögen + noun", 0.75f, 0.2f, w = 0.44f, h = 0.22f, sub = "liking a thing"),
                        DNode("conj", "ich mag, du magst", 0.5f, 0.62f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "er mag, wir mögen, ihr mögt"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("gern"), light = setOf("gern")),
                    Beat(3, light = setOf("gern"), card = BoardCard("Ich schwimme gern.", "I like swimming.", phrase = true)),
                    Beat(5, show = setOf("mag", "conj"), light = setOf("mag"), card = BoardCard("gern or mögen?", "gern likes doing something; mögen likes a thing or a person.")),
                    Beat(6, light = setOf("mag", "conj"), card = BoardCard("Ich mag Tee. Ich mag meine Kollegen.", "I like tea. I like my colleagues.", phrase = true)),
                    Beat(8, light = setOf("gern", "mag"), card = BoardCard("Not: Ich mag schwimmen", "An activity takes gern: Ich schwimme gern.")),
                    Beat(9, light = setOf("mag"), card = BoardCard("Ich mag Fußball.", "I like football. Ich spiele gern Fußball: I like playing football.", phrase = true)),
                ),
            ),
            // 10: "And «möchten»? It sounds a lot like «mögen»."
            Scene(
                10, "Wanting and not liking",
                DiagramSpec(
                    "möchten is wanting something now; nicht gern and kein say you do not like it.",
                    nodes = listOf(
                        DNode("mo", "möchten", 0.5f, 0.18f, w = 0.5f, h = 0.2f, sub = "wanting now"),
                        DNode("ng", "nicht gern", 0.25f, 0.6f, w = 0.44f, h = 0.22f, tone = 1, sub = "disliking an activity"),
                        DNode("kein", "kein + noun", 0.75f, 0.6f, w = 0.44f, h = 0.22f, tone = 1, sub = "disliking a thing"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("mo"), light = setOf("mo")),
                    Beat(12, light = setOf("mo"), card = BoardCard("Ich möchte einen Tee.", "I'd like a tea.", phrase = true)),
                    Beat(14, show = setOf("ng", "kein"), light = setOf("ng", "kein")),
                    Beat(15, light = setOf("kein"), card = BoardCard("Ich mag keinen Fisch.", "I don't like fish. Ich tanze nicht gern: I don't like dancing.", phrase = true)),
                ),
            ),
            // 16: "Okay, piece two. Where do the time words go?"
            Scene(
                16, "The verb keeps seat two",
                DiagramSpec(
                    "If a time word opens the sentence, the verb still comes second and the subject moves after it.",
                    nodes = listOf(
                        DNode("s1", "Am Samstag", 0.15f, 0.3f, w = 0.28f, h = 0.22f, tone = 2, sub = "seat one"),
                        DNode("s2", "spiele", 0.41f, 0.3f, w = 0.22f, h = 0.22f, sub = "seat two"),
                        DNode("s3", "ich", 0.62f, 0.3f, w = 0.18f, h = 0.22f, tone = 1, sub = "subject"),
                        DNode("s4", "Fußball.", 0.85f, 0.3f, h = 0.22f, tone = 1),
                        DNode("alt", "Ich spiele am Samstag Fußball.", 0.5f, 0.74f, w = 0.9f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "or: subject first"),
                    ),
                    edges = listOf(
                        DEdge("s1", "s2"),
                        DEdge("s2", "s3"),
                        DEdge("s3", "s4"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("s2"), light = setOf("s2"), card = BoardCard("Zeitangabe", "A time word: heute, morgen, am Samstag, im Sommer.")),
                    Beat(18, show = setOf("alt"), light = setOf("alt"), card = BoardCard("Ich spiele am Samstag Fußball.", "I play football on Saturday.", phrase = true)),
                    Beat(20, show = setOf("s1", "s3", "s4"), light = setOf("s1", "s2"), pulse = "s1" to "s2"),
                    Beat(21, light = setOf("s2", "s3"), pulse = "s2" to "s3", card = BoardCard("Am Samstag spiele ich Fußball.", "On Saturday I play football.", phrase = true)),
                    Beat(23, light = setOf("s2"), card = BoardCard("Never: Am Samstag ich spiele", "Only one thing fits in front of the verb.")),
                    Beat(24, card = BoardCard("Heute gehen wir ins Kino.", "Today we're going to the cinema. Im Herbst regnet es oft: in autumn it often rains.", phrase = true)),
                ),
            ),
            // 25: "What if I have when, how and where, all in one sentence?"
            Scene(
                25, "Time, manner, place",
                DiagramSpec(
                    "When, how, where: time before manner before place. Frequency words sit after the verb.",
                    nodes = listOf(
                        DNode("v", "Ich fahre", 0.5f, 0.14f, w = 0.4f, kind = NodeKind.PILL),
                        DNode("t", "am Sonntag", 0.17f, 0.48f, w = 0.3f, h = 0.22f, tone = 1, sub = "time: when"),
                        DNode("m", "mit dem Bus", 0.5f, 0.48f, w = 0.3f, h = 0.22f, tone = 1, sub = "manner: how"),
                        DNode("p", "nach Köln", 0.83f, 0.48f, w = 0.3f, h = 0.22f, tone = 1, sub = "place: where"),
                        DNode("f", "immer, oft, manchmal, nie", 0.5f, 0.84f, w = 0.8f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "Häufigkeit: how often"),
                    ),
                    edges = listOf(
                        DEdge("t", "m"),
                        DEdge("m", "p"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("v", "t", "m", "p"), light = setOf("t", "m", "p"), pulse = "t" to "m", card = BoardCard("Time, manner, place", "When, how, where. English puts the place first: unlearn it.")),
                    Beat(27, light = setOf("v"), pulse = "m" to "p", card = BoardCard("am Sonntag · mit dem Bus · nach Köln", "Ich fahre ... : I'm going to Cologne by bus on Sunday.", phrase = true)),
                    Beat(29, show = setOf("f"), light = setOf("f"), card = BoardCard("Häufigkeit", "immer, oft, manchmal, nie: always, often, sometimes, never.")),
                    Beat(30, light = setOf("f"), card = BoardCard("Wir gehen oft spazieren.", "We often go for a walk. Manchmal koche ich: sometimes I cook.", phrase = true)),
                ),
            ),
            // 31: "And piece three, the past. Is it scary?"
            Scene(
                31, "The Perfekt bracket",
                DiagramSpec(
                    "The Perfekt bracket: haben in position two, the Partizip at the very end.",
                    nodes = listOf(
                        DNode("a", "Ich", 0.09f, 0.3f, w = 0.14f, h = 0.2f, tone = 1),
                        DNode("b", "habe", 0.29f, 0.3f, w = 0.2f, h = 0.2f),
                        DNode("c", "gestern Fußball", 0.58f, 0.3f, w = 0.32f, h = 0.2f, tone = 2),
                        DNode("d", "gespielt.", 0.87f, 0.3f, w = 0.24f, h = 0.2f),
                        DNode("rec", "ge + stem + t", 0.5f, 0.76f, w = 0.6f, h = 0.22f, kind = NodeKind.NOTE, tone = 1, sub = "the Partizip"),
                    ),
                    edges = listOf(
                        DEdge("b", "d", "bracket", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("a", "b", "c", "d"), light = setOf("b", "d"), pulse = "b" to "d", card = BoardCard("The Perfekt", "haben in seat two, the Partizip at the very end.")),
                    Beat(33, light = setOf("d"), card = BoardCard("Ich habe gestern Fußball gespielt.", "Yesterday I played football.", phrase = true)),
                    Beat(35, show = setOf("rec"), light = setOf("rec"), card = BoardCard("ge + stem + t", "spielen: gespielt. kochen: gekocht. hören: gehört. arbeiten: gearbeitet.")),
                    Beat(36, light = setOf("rec"), card = BoardCard("Sanya hat Reis gekocht.", "Sanya cooked rice. Ich habe Musik gehört: I listened to music.", phrase = true)),
                    Beat(38, light = setOf("d")),
                    Beat(39, light = setOf("b", "d"), card = BoardCard("Was hast du am Wochenende gemacht?", "What did you do at the weekend?", phrase = true)),
                ),
            ),
            // 40: "So what should I remember for the Goethe exam?"
            Scene(
                40, "For the Goethe exam",
                DiagramSpec(
                    "This grammar in the Goethe A1: verb second when you speak and write, gern or mögen, and the Partizip in Hören.",
                    nodes = listOf(
                        DNode("sw", "Sprechen, Schreiben", 0.5f, 0.14f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "verb second, even after heute"),
                        DNode("gm", "gern or mögen", 0.5f, 0.44f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "a verb or a thing?"),
                        DNode("h", "Hören", 0.5f, 0.74f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "wait for the last word"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(41, show = setOf("sw", "gm", "h"), card = BoardCard("Verb second, always", "Even after am Samstag or heute: examiners notice.")),
                    Beat(43, card = BoardCard("Today", "Five sentences starting with a time phrase, verb second; two again in the Perfekt.")),
                    Beat(44, card = BoardCard("Bis bald! Schönes Wochenende!", "See you soon! Have a nice weekend!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de9Dialogue() = PodcastBoard(
        "de9-dialogue",
        listOf(
            // 0: "Hi, and welcome back. Today is a dialogue: two friends making weekend ..."
            Scene(
                0, "Weekend plans",
                DiagramSpec(
                    "Sanya's weekend: each plan starts with the day, then the verb, then ich or wir.",
                    nodes = listOf(
                        DNode("lena", "Lena", 0.22f, 0.12f, w = 0.32f, h = 0.15f, kind = NodeKind.PILL, tone = 1),
                        DNode("sanya", "Sanya", 0.78f, 0.12f, w = 0.32f, h = 0.15f, kind = NodeKind.PILL),
                        DNode("sam", "am Samstag", 0.25f, 0.5f, w = 0.44f, h = 0.22f, tone = 1, sub = "schwimmen mit Usaid"),
                        DNode("son", "am Sonntag", 0.75f, 0.5f, w = 0.44f, h = 0.22f, tone = 1, sub = "zusammen kochen"),
                        DNode("we", "das Wochenende", 0.5f, 0.85f, w = 0.56f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("lena", "sanya"),
                        DEdge("sanya", "sam"),
                        DEdge("sanya", "son"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("lena", "sanya"), light = setOf("lena")),
                    Beat(3, light = setOf("lena"), pulse = "lena" to "sanya", card = BoardCard("Was machst du am Wochenende, Sanya?", "What are you doing at the weekend, Sanya?", phrase = true)),
                    Beat(5, show = setOf("we"), light = setOf("we"), card = BoardCard("am = an dem", "On the: am Wochenende, am Samstag, am Sonntag.")),
                    Beat(6, show = setOf("sam"), light = setOf("sam"), pulse = "sanya" to "sam", card = BoardCard("Am Samstag gehe ich schwimmen.", "On Saturday I'm going swimming (mit Usaid: with Usaid).", phrase = true)),
                    Beat(8, light = setOf("sam"), card = BoardCard("Day, verb, then ich", "Am Samstag takes first place; the verb gehe stays second.")),
                    Beat(9, show = setOf("son"), light = setOf("son"), pulse = "sanya" to "son", card = BoardCard("Und am Sonntag kochen wir zusammen.", "And on Sunday we cook together.", phrase = true)),
                ),
            ),
            // 10: "And «zusammen» is together?"
            Scene(
                10, "Like, prefer, like most",
                DiagramSpec(
                    "Answer with more than ja: Sanya likes swimming, reads most of all; Lena prefers tennis.",
                    nodes = listOf(
                        DNode("q", "Schwimmst du gern?", 0.5f, 0.12f, w = 0.62f, kind = NodeKind.PILL),
                        DNode("a1", "sehr gern", 0.25f, 0.46f, w = 0.44f, h = 0.22f, tone = 1, sub = "Sanya: schwimmen"),
                        DNode("a2", "am liebsten", 0.75f, 0.46f, w = 0.44f, h = 0.22f, sub = "Sanya: lesen"),
                        DNode("a3", "lieber", 0.5f, 0.82f, w = 0.5f, h = 0.22f, tone = 1, sub = "Lena: Tennis"),
                    ),
                    edges = listOf(
                        DEdge("q", "a1"),
                        DEdge("q", "a2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("q"), light = setOf("q")),
                    Beat(12, light = setOf("q"), card = BoardCard("Schwimmst du gern?", "Do you like swimming?", phrase = true)),
                    Beat(14, show = setOf("a1"), light = setOf("a1"), pulse = "q" to "a1", card = BoardCard("Not a bare ja", "Add one more sentence: it is free marks.")),
                    Beat(15, show = setOf("a2"), light = setOf("a2"), pulse = "q" to "a2", card = BoardCard("Aber am liebsten lese ich.", "Yes, very much. But most of all I like reading. And you?", phrase = true)),
                    Beat(17, show = setOf("a3"), light = setOf("a3")),
                    Beat(18, light = setOf("a3"), card = BoardCard("Ich spiele lieber Tennis.", "I prefer playing tennis. Are you coming along on Sunday? At three?", phrase = true)),
                ),
            ),
            // 19: "«Kommst du mit»: are you coming along? What's the «mit» doing at the e..."
            Scene(
                19, "Yes, if...",
                DiagramSpec(
                    "An invitation, a yes, and a condition: wenn das Wetter gut ist.",
                    nodes = listOf(
                        DNode("inv", "Kommst du mit?", 0.5f, 0.14f, w = 0.6f, h = 0.2f, tone = 1, sub = "are you coming along?"),
                        DNode("yes", "Ja, gern!", 0.5f, 0.46f, w = 0.5f, h = 0.18f, sub = "yes, gladly"),
                        DNode("wenn", "Wenn das Wetter gut ist.", 0.5f, 0.8f, w = 0.8f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "if the weather is good"),
                    ),
                    edges = listOf(
                        DEdge("inv", "yes"),
                        DEdge("yes", "wenn", "if", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(19, show = setOf("inv"), light = setOf("inv")),
                    Beat(21, show = setOf("yes", "wenn"), light = setOf("yes", "wenn"), pulse = "yes" to "wenn", card = BoardCard("Ja, gern! Wenn das Wetter gut ist.", "Yes, gladly! If the weather is good.", phrase = true)),
                    Beat(23, light = setOf("wenn"), card = BoardCard("wenn = if", "ist goes to the very end. Copy it as a block for now.")),
                ),
            ),
            // 24: "Und nun das Wetter. Heute ist es sonnig und warm, bis achtundzwanzig G..."
            Scene(
                24, "The weather report",
                DiagramSpec(
                    "A weather report has a fixed shape: today, this evening, tomorrow, the weekend. Day word first, then the number.",
                    nodes = listOf(
                        DNode("h", "heute", 0.5f, 0.12f, w = 0.62f, h = 0.2f, tone = 1, sub = "sonnig, bis 28 Grad"),
                        DNode("ab", "am Abend", 0.5f, 0.37f, w = 0.62f, h = 0.2f, tone = 2, sub = "Wolken"),
                        DNode("mo", "morgen", 0.5f, 0.62f, w = 0.62f, h = 0.2f, sub = "Regen, nur 15 Grad"),
                        DNode("we", "das Wochenende", 0.5f, 0.87f, w = 0.62f, h = 0.2f, tone = 1, sub = "wieder schön"),
                    ),
                    edges = listOf(
                        DEdge("h", "ab"),
                        DEdge("ab", "mo"),
                        DEdge("mo", "we"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("h"), light = setOf("h"), card = BoardCard("Heute ist es sonnig und warm.", "Today it is sunny and warm, up to twenty-eight degrees.", phrase = true)),
                    Beat(26, light = setOf("h"), card = BoardCard("bis 28 Grad", "Up to 28 degrees: the top of the day.")),
                    Beat(27, show = setOf("ab"), light = setOf("ab"), pulse = "h" to "ab", card = BoardCard("Am Abend kommen Wolken.", "In the evening clouds arrive.", phrase = true)),
                    Beat(29, show = setOf("mo"), light = setOf("mo"), pulse = "ab" to "mo", card = BoardCard("nur = only", "Only fifteen degrees: colder than today.")),
                    Beat(30, light = setOf("mo"), card = BoardCard("Morgen regnet es den ganzen Tag.", "Tomorrow it rains all day, only fifteen degrees.", phrase = true)),
                    Beat(33, show = setOf("we"), light = setOf("we"), pulse = "mo" to "we", card = BoardCard("Am Wochenende wird es wieder schön.", "At the weekend it gets nice again.", phrase = true)),
                ),
            ),
            // 34: "Phew. And what if it rains on a big outdoor event?"
            Scene(
                34, "Bei Regen",
                DiagramSpec(
                    "bei Regen is a condition, not a place: in case of rain, the festival moves to the Stadthalle.",
                    nodes = listOf(
                        DNode("z", "Zuhörer", 0.5f, 0.1f, w = 0.44f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("fest", "das Stadtfest", 0.5f, 0.4f, w = 0.56f, h = 0.2f, kind = NodeKind.PILL, sub = "Samstag, vierzehn Uhr"),
                        DNode("park", "im Park", 0.25f, 0.78f, w = 0.44f, h = 0.22f, tone = 1, sub = "good weather"),
                        DNode("halle", "in der Stadthalle", 0.75f, 0.78f, w = 0.44f, h = 0.22f, sub = "bei Regen"),
                    ),
                    edges = listOf(
                        DEdge("z", "fest"),
                        DEdge("fest", "park"),
                        DEdge("fest", "halle", "bei Regen", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(35, show = setOf("z", "fest"), light = setOf("z"), card = BoardCard("Zuhörer", "Listeners: the radio talks to them.")),
                    Beat(36, show = setOf("park"), light = setOf("fest", "park"), pulse = "fest" to "park", card = BoardCard("Das Stadtfest beginnt im Park.", "The town festival on Saturday starts at two in the afternoon in the park.", phrase = true)),
                    Beat(38, show = setOf("halle"), light = setOf("halle"), pulse = "fest" to "halle", card = BoardCard("bei Regen", "Not a place: in case of rain. Stadthalle: the town hall for events.")),
                    Beat(39, light = setOf("halle"), card = BoardCard("Bei Regen: in der Stadthalle.", "Bei Regen ist das Fest in der Stadthalle: if it rains, it is in the town hall.", phrase = true)),
                ),
            ),
            // 40: "So what should I remember for the Goethe exam?"
            Scene(
                40, "For the Goethe exam",
                DiagramSpec(
                    "Weekend plans and weather in the Goethe A1: the radio report, the rain condition, a full answer.",
                    nodes = listOf(
                        DNode("hear", "Hören", 0.5f, 0.14f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "day word first, then the number"),
                        DNode("bei", "bei Regen", 0.5f, 0.44f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "a condition, not a place"),
                        DNode("speak", "Sprechen", 0.5f, 0.74f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "add a reason, not a bare ja"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(41, show = setOf("hear", "bei", "speak"), card = BoardCard("Careful with numbers", "Times and dates sound like temperatures too.")),
                    Beat(43, card = BoardCard("Today", "Plan a weekend out loud: a day and a time, gern, lieber, and a bei Regen plan.")),
                    Beat(44, card = BoardCard("Bis Samstag! Bei Regen im Café!", "See you Saturday! If it rains, at the café!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de9Practice() = PodcastBoard(
        "de9-practice",
        listOf(
            // 0: "Hi, and welcome back. Today is practice, exam style: an email to read,..."
            Scene(
                0, "Lesen: Tom's email",
                DiagramSpec(
                    "Lesen part one: each statement is checked against one line of Tom's email. One word can flip it.",
                    nodes = listOf(
                        DNode("mail", "die E-Mail von Tom", 0.5f, 0.1f, w = 0.62f, h = 0.15f, kind = NodeKind.PILL),
                        DNode("hobby", "das Hobby", 0.25f, 0.42f, w = 0.44f, h = 0.22f, tone = 1, sub = "Gitarre"),
                        DNode("abend", "jeden Abend", 0.75f, 0.42f, w = 0.44f, h = 0.22f, sub = "eine Stunde üben"),
                        DNode("park", "am Samstag", 0.25f, 0.78f, w = 0.44f, h = 0.22f, tone = 1, sub = "im Park, vier Uhr"),
                        DNode("regen", "bei Regen", 0.75f, 0.78f, w = 0.44f, h = 0.22f, tone = 1, sub = "bei mir zu Hause"),
                    ),
                    edges = listOf(
                        DEdge("mail", "hobby"),
                        DEdge("mail", "abend"),
                        DEdge("hobby", "park"),
                        DEdge("abend", "regen"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("mail"), light = setOf("mail"), card = BoardCard("Richtig oder falsch?", "A short personal text, then statements: true or false.")),
                    Beat(3, show = setOf("hobby"), light = setOf("hobby"), pulse = "mail" to "hobby", card = BoardCard("Ich spiele Gitarre!", "Hello Usaid, how are you? I have a new hobby now: I play the guitar!", phrase = true)),
                    Beat(5, show = setOf("abend"), light = setOf("abend"), pulse = "mail" to "abend", card = BoardCard("üben", "To practise.")),
                    Beat(6, light = setOf("abend"), card = BoardCard("Ich übe jeden Abend eine Stunde.", "I practise for an hour every evening.", phrase = true)),
                    Beat(8, light = setOf("abend"), card = BoardCard("jeden Abend, not jeden Morgen", "One word flips the statement: Falsch.")),
                    Beat(9, show = setOf("park"), light = setOf("park"), card = BoardCard("Kommst du? Bring Sanya mit!", "On Saturday we're playing in the park, at four. Are you coming? Bring Sanya along!", phrase = true)),
                    Beat(11, show = setOf("regen"), light = setOf("regen"), card = BoardCard("bei mir", "At my place. And bei Regen: in case of rain.")),
                    Beat(12, light = setOf("regen"), card = BoardCard("Bei Regen: bei mir zu Hause.", "Bei Regen treffen wir uns bei mir zu Hause: if it rains, we meet at my place.", phrase = true)),
                    Beat(14, light = setOf("regen"), card = BoardCard("Richtig", "Check each statement against one line of the text.")),
                ),
            ),
            // 15: "Okay. And then I write back?"
            Scene(
                15, "Schreiben: four points",
                DiagramSpec(
                    "A reply to an invitation has four points, in order: thanks, yes and who, a question, the rain plan.",
                    nodes = listOf(
                        DNode("n1", "1. Danke", 0.5f, 0.12f, w = 0.8f, h = 0.2f, tone = 1, sub = "thank him"),
                        DNode("n2", "2. Ja, und mit wem", 0.5f, 0.37f, w = 0.8f, h = 0.2f, tone = 1, sub = "yes, and who comes"),
                        DNode("n3", "3. eine Frage", 0.5f, 0.62f, w = 0.8f, h = 0.2f, tone = 1, sub = "what should I bring?"),
                        DNode("n4", "4. bei Regen", 0.5f, 0.87f, w = 0.8f, h = 0.2f, sub = "the rain plan"),
                    ),
                    edges = listOf(
                        DEdge("n1", "n2"),
                        DEdge("n2", "n3"),
                        DEdge("n3", "n4"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("n1"), light = setOf("n1")),
                    Beat(17, light = setOf("n1"), card = BoardCard("Vielen Dank für die Einladung.", "Thank you very much for the invitation.", phrase = true)),
                    Beat(19, show = setOf("n2"), light = setOf("n2"), pulse = "n1" to "n2"),
                    Beat(20, light = setOf("n2"), card = BoardCard("Ich komme gern.", "I'd love to come. Und ich bringe Sanya mit: and I'll bring Sanya along.", phrase = true)),
                    Beat(22, show = setOf("n3"), light = setOf("n3"), pulse = "n2" to "n3", card = BoardCard("Getränke", "Drinks. A question gives Tom something to answer.")),
                    Beat(23, light = setOf("n3"), card = BoardCard("Was soll ich mitbringen?", "What should I bring? Maybe drinks?", phrase = true)),
                    Beat(25, show = setOf("n4"), light = setOf("n4"), pulse = "n3" to "n4", card = BoardCard("Don't skip the rain plan", "Tom offered one, so answering it is a content point.")),
                    Beat(26, light = setOf("n4"), card = BoardCard("Bei Regen kommen wir zu dir.", "If it rains, we'll come to you.", phrase = true)),
                ),
            ),
            // 27: "What if Sanya writes her own reply, and she'd rather go to the café?"
            Scene(
                27, "Sanya's reply",
                DiagramSpec(
                    "Sanya's reply keeps the same four points: she comes with a friend, asks about cake, and picks the café.",
                    nodes = listOf(
                        DNode("who", "mit einer Freundin", 0.5f, 0.16f, w = 0.8f, h = 0.22f, tone = 1, sub = "who comes"),
                        DNode("q", "Soll ich Kuchen mitbringen?", 0.5f, 0.5f, w = 0.8f, h = 0.22f, tone = 1, sub = "the question"),
                        DNode("rain", "Bei Regen ins Café", 0.5f, 0.84f, w = 0.8f, h = 0.22f, sub = "the rain plan"),
                    ),
                    edges = listOf(
                        DEdge("who", "q"),
                        DEdge("q", "rain"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("who"), light = setOf("who")),
                    Beat(29, show = setOf("q", "rain"), light = setOf("q", "rain"), pulse = "q" to "rain", card = BoardCard("Soll ich Kuchen mitbringen?", "Should I bring cake? Bei Regen gehen wir ins Café: if it rains, let's go to the café.", phrase = true)),
                ),
            ),
            // 30: "Okay. And the speaking part, the word card game?"
            Scene(
                30, "Sprechen: the word card",
                DiagramSpec(
                    "Sprechen part two: the card gives a topic and a word. Make a question; your partner answers in a sentence.",
                    nodes = listOf(
                        DNode("card", "Thema: Freizeit", 0.5f, 0.13f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, tone = 1, sub = "Wort: Sport"),
                        DNode("q", "Welchen Sport machst du?", 0.26f, 0.52f, w = 0.48f, h = 0.26f),
                        DNode("a", "Ich spiele Fußball.", 0.75f, 0.52f, w = 0.46f, h = 0.26f, tone = 1, sub = "a full sentence"),
                        DNode("wet", "Wort: Wetter", 0.5f, 0.87f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "Wie ist das Wetter heute?"),
                    ),
                    edges = listOf(
                        DEdge("card", "q"),
                        DEdge("q", "a"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("card"), light = setOf("card"), card = BoardCard("A topic and a word", "Make a question from the word; your partner answers in a sentence.")),
                    Beat(32, show = setOf("q"), light = setOf("q"), pulse = "card" to "q", card = BoardCard("Welchen Sport machst du?", "Which sport do you do?", phrase = true)),
                    Beat(34, light = setOf("q"), card = BoardCard("Not just: Sport?", "A single word is not a question. Build a full one.")),
                    Beat(35, show = setOf("a"), light = setOf("a"), pulse = "q" to "a", card = BoardCard("Ich spiele Fußball.", "I play football. Und ich schwimme gern: and I like swimming.", phrase = true)),
                    Beat(37, show = setOf("wet"), light = setOf("wet")),
                    Beat(38, light = setOf("wet"), card = BoardCard("Es ist sehr heiß, 35 Grad.", "What is the weather like today in Doha? Very hot, thirty-five degrees.", phrase = true)),
                ),
            ),
            // 39: "So what should I remember for the Goethe exam?"
            Scene(
                39, "For the Goethe exam",
                DiagramSpec(
                    "This practice in the Goethe A1: one word in Lesen, four points in Schreiben, full questions in Sprechen.",
                    nodes = listOf(
                        DNode("les", "Lesen", 0.5f, 0.14f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "one word can flip it"),
                        DNode("sch", "Schreiben", 0.5f, 0.44f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "four points"),
                        DNode("spr", "Sprechen", 0.5f, 0.74f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "full question, full answer"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(40, show = setOf("les", "sch", "spr"), card = BoardCard("jeden Abend, not jeden Morgen", "Check each statement against one line of the text.")),
                    Beat(42, card = BoardCard("Today", "Your weekend: three time-word sentences, today's weather, a four-point reply.")),
                    Beat(43, card = BoardCard("Viele Grüße und bis Samstag!", "Best wishes, and see you on Saturday!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de10Words() = PodcastBoard(
        "de10-words",
        listOf(
            // 0: "Welcome back. Today is the «Vergangenheit», the past: words like yeste..."
            Scene(
                0, "Verb second, verb last",
                DiagramSpec(
                    "A past sentence: the helper sits second, the past form of the verb goes to the very end.",
                    nodes = listOf(
                        DNode("a", "Gestern", 0.5f, 0.12f, w = 0.5f, tone = 1, sub = "first place"),
                        DNode("b", "habe", 0.5f, 0.37f, w = 0.5f, sub = "the helper, second"),
                        DNode("c", "ich", 0.5f, 0.62f, w = 0.5f, tone = 2),
                        DNode("d", "gearbeitet", 0.5f, 0.87f, w = 0.5f, sub = "past form, last"),
                    ),
                    edges = listOf(
                        DEdge("a", "b"),
                        DEdge("b", "c"),
                        DEdge("c", "d"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, card = BoardCard("Vergangenheit", "The past: time words, and the verbs of a day in their past forms.")),
                    Beat(1, show = setOf("a", "b", "c", "d"), card = BoardCard("Gestern habe ich gearbeitet.", "Yesterday I worked.", phrase = true)),
                    Beat(3, light = setOf("b", "d"), pulse = "a" to "b", card = BoardCard("Second and last", "The helper comes second; the past form waits at the end.")),
                ),
            ),
            // 4: "And the day before yesterday? English needs four words for that."
            Scene(
                4, "Further back in time",
                DiagramSpec(
                    "From long ago to yesterday: früher, letztes Jahr, letzte Woche, vorgestern, gestern.",
                    nodes = listOf(
                        DNode("fr", "früher", 0.5f, 0.1f, w = 0.56f, h = 0.15f, tone = 2, sub = "in the past"),
                        DNode("jahr", "letztes Jahr", 0.5f, 0.3f, w = 0.56f, h = 0.15f, tone = 1, sub = "das Jahr"),
                        DNode("woche", "letzte Woche", 0.5f, 0.5f, w = 0.56f, h = 0.15f, tone = 1, sub = "die Woche"),
                        DNode("vor", "vorgestern", 0.5f, 0.7f, w = 0.56f, h = 0.15f, tone = 1, sub = "day before yesterday"),
                        DNode("ge", "gestern", 0.5f, 0.9f, w = 0.56f, h = 0.15f, sub = "yesterday"),
                    ),
                    edges = listOf(
                        DEdge("fr", "jahr"),
                        DEdge("jahr", "woche"),
                        DEdge("woche", "vor"),
                        DEdge("vor", "ge"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("vor", "ge"), light = setOf("vor"), pulse = "vor" to "ge"),
                    Beat(6, light = setOf("vor"), card = BoardCard("Vorgestern war Sonntag.", "The day before yesterday was Sunday.", phrase = true)),
                    Beat(8, show = setOf("woche", "jahr"), light = setOf("woche", "jahr"), card = BoardCard("letzte / letztes", "die Woche: letzte Woche. das Jahr: letztes Jahr.")),
                    Beat(9, light = setOf("woche"), card = BoardCard("Letzte Woche war ich krank.", "Last week I was ill. Last year we were in Lahore.", phrase = true)),
                    Beat(11, show = setOf("fr"), light = setOf("fr"), card = BoardCard("war and hatte", "Was and had: short and simple. Früher: earlier, in the past.")),
                    Beat(12, light = setOf("fr"), card = BoardCard("Früher habe ich in Karachi gewohnt.", "I used to live in Karachi.", phrase = true)),
                ),
            ),
            // 13: "Okay. Now already and not yet. My mum always asks if I've eaten."
            Scene(
                13, "Already or not yet",
                DiagramSpec(
                    "schon is already, noch nicht is not yet; frühstücken has the past form gefrühstückt.",
                    nodes = listOf(
                        DNode("schon", "schon", 0.26f, 0.3f, w = 0.42f, h = 0.22f, sub = "already"),
                        DNode("noch", "noch nicht", 0.74f, 0.3f, w = 0.42f, h = 0.22f, tone = 1, sub = "not yet"),
                        DNode("fr", "frühstücken", 0.5f, 0.75f, w = 0.6f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "gefrühstückt"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("schon", "noch", "fr"), light = setOf("schon", "noch")),
                    Beat(15, light = setOf("fr"), card = BoardCard("Ich habe schon gegessen.", "I have already eaten. I haven't had breakfast yet.", phrase = true)),
                ),
            ),
            // 16: "How do I tell a whole day? First this, then that?"
            Scene(
                16, "A day in order",
                DiagramSpec(
                    "A day in the past, in order: zuerst, dann, danach, am Abend. Each step is one past sentence.",
                    nodes = listOf(
                        DNode("z", "zuerst", 0.5f, 0.12f, w = 0.5f, tone = 1, sub = "first"),
                        DNode("d", "dann", 0.5f, 0.37f, w = 0.5f, tone = 1, sub = "then"),
                        DNode("da", "danach", 0.5f, 0.62f, w = 0.5f, tone = 1, sub = "after that"),
                        DNode("ab", "am Abend", 0.5f, 0.87f, w = 0.5f, sub = "in the evening"),
                    ),
                    edges = listOf(
                        DEdge("z", "d"),
                        DEdge("d", "da"),
                        DEdge("da", "ab"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("z", "d", "da", "ab")),
                    Beat(18, light = setOf("z", "d"), pulse = "z" to "d", card = BoardCard("Zuerst bin ich aufgestanden.", "First I got up. Then I had breakfast.", phrase = true)),
                ),
            ),
            // 19: "Hold on. «bin ich aufgestanden», but «habe ich gefrühstückt». Why two ..."
            Scene(
                19, "haben or sein?",
                DiagramSpec(
                    "Most verbs take haben; verbs that move you from place to place, or change you, take sein.",
                    nodes = listOf(
                        DNode("v", "The verb", 0.5f, 0.1f, w = 0.36f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("h", "haben", 0.26f, 0.4f, w = 0.42f, h = 0.18f, tone = 1, sub = "most verbs"),
                        DNode("s", "sein", 0.74f, 0.4f, w = 0.42f, h = 0.18f, sub = "moving, changing"),
                        DNode("hn", "gegessen, getrunken, gesehen", 0.26f, 0.75f, w = 0.44f, h = 0.26f, kind = NodeKind.NOTE, tone = 2),
                        DNode("sn", "gefahren, geflogen, geblieben", 0.74f, 0.75f, w = 0.44f, h = 0.26f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("v", "h"),
                        DEdge("v", "s"),
                        DEdge("h", "hn", dashed = true),
                        DEdge("s", "sn", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(19, show = setOf("v", "h", "s")),
                    Beat(20, light = setOf("s"), pulse = "v" to "s", card = BoardCard("sein: the suitcase verbs", "From one place to another, or getting up: sein. Most others: haben.")),
                    Beat(21, light = setOf("s"), card = BoardCard("Danach bin ich zur Arbeit gefahren.", "After that I drove to work. In the evening I cooked.", phrase = true)),
                    Beat(23, show = setOf("hn"), light = setOf("h", "hn"), pulse = "h" to "hn", card = BoardCard("Learn three things", "The verb, its past form, its helper: essen, gegessen, haben.")),
                    Beat(24, light = setOf("h"), card = BoardCard("Wir haben einen Film gesehen.", "I drank tea. We watched a film.", phrase = true)),
                    Beat(26, show = setOf("sn"), light = setOf("s", "sn"), pulse = "s" to "sn", card = BoardCard("bleiben takes sein", "Nothing moves, but it is still sein: Wir sind zu Hause geblieben.")),
                    Beat(27, light = setOf("sn"), card = BoardCard("Ich bin nach Doha geflogen.", "I flew to Doha. We stayed at home.", phrase = true)),
                ),
            ),
            // 28: "Is there any pattern, or is it all memory?"
            Scene(
                28, "Where the ge- goes",
                DiagramSpec(
                    "No ge- for verbs in be- or -ieren; einkaufen puts the ge in the middle.",
                    nodes = listOf(
                        DNode("b", "besucht", 0.5f, 0.15f, w = 0.6f, h = 0.2f, tone = 1, sub = "besuchen: no ge-"),
                        DNode("t", "telefoniert", 0.5f, 0.5f, w = 0.6f, h = 0.2f, tone = 1, sub = "telefonieren: no ge-"),
                        DNode("e", "eingekauft", 0.5f, 0.85f, w = 0.6f, h = 0.2f, sub = "einkaufen: ge in the middle"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("b", "t", "e"), light = setOf("b", "t")),
                    Beat(30, light = setOf("b", "e"), card = BoardCard("Ich habe meine Eltern besucht.", "I visited my parents. We did the shopping.", phrase = true)),
                ),
            ),
            // 31: "So what should I remember for the Goethe exam?"
            Scene(
                31, "For the Goethe exam",
                DiagramSpec(
                    "Past words in the Goethe A1: telling yesterday in Sprechen, trips in Lesen, and every verb with its helper.",
                    nodes = listOf(
                        DNode("sp", "Sprechen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "zuerst, dann, danach"),
                        DNode("le", "Lesen", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "gestern, letzte Woche"),
                        DNode("hv", "Every verb", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "with its helper"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("sp", "le", "hv"), card = BoardCard("gefahren needs sein", "A past form is only useful once you know its helper.")),
                    Beat(34, card = BoardCard("Today", "Yesterday in five sentences: zuerst, dann, danach. One with sein, three with haben.")),
                    Beat(35, card = BoardCard("Tschüss! Bis morgen!", "Bye! See you tomorrow!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de10Grammar() = PodcastBoard(
        "de10-grammar",
        listOf(
            // 0: "Welcome back. Today is grammar: how German talks about the past. Spoke..."
            Scene(
                0, "The Perfekt sandwich",
                DiagramSpec(
                    "The Perfekt: the helper sits second, the participle goes to the very end.",
                    nodes = listOf(
                        DNode("q1", "Was", 0.5f, 0.12f, w = 0.5f, tone = 2),
                        DNode("q2", "hast", 0.5f, 0.37f, w = 0.5f, sub = "Hilfsverb, second"),
                        DNode("q3", "du gestern", 0.5f, 0.62f, w = 0.5f, tone = 2, sub = "the middle"),
                        DNode("q4", "gemacht", 0.5f, 0.87f, w = 0.5f, sub = "participle, last"),
                    ),
                    edges = listOf(
                        DEdge("q1", "q2"),
                        DEdge("q2", "q3"),
                        DEdge("q3", "q4"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("q2", "q4"), card = BoardCard("Perfekt", "The spoken past: haben or sein, plus a participle at the end.")),
                    Beat(2, light = setOf("q2"), card = BoardCard("Hilfsverb", "The helper: haben or sein, in the usual verb place.")),
                    Beat(3, show = setOf("q1", "q3"), card = BoardCard("Was hast du gestern gemacht?", "What did you do yesterday?", phrase = true)),
                    Beat(4, light = setOf("q2", "q4")),
                ),
            ),
            // 5: "Exactly. And the answer has the same shape: the helper second, everyth..."
            Scene(
                5, "Not the English order",
                DiagramSpec(
                    "In English eaten sits next to have; in German the participle waits at the end.",
                    nodes = listOf(
                        DNode("ok", "Ich habe Pizza gegessen.", 0.5f, 0.3f, w = 0.8f, h = 0.24f, sub = "participle last"),
                        DNode("bad", "Ich habe gegessen Pizza.", 0.5f, 0.72f, w = 0.8f, h = 0.24f, tone = 2, sub = "the English order: wrong"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("ok"), light = setOf("ok"), card = BoardCard("Ich habe Pizza gegessen.", "I ate pizza.", phrase = true)),
                    Beat(8, show = setOf("bad"), light = setOf("bad"), card = BoardCard("Not: habe gegessen Pizza", "The participle waits until after the pizza.")),
                ),
            ),
            // 9: "Yes, how do I choose?"
            Scene(
                9, "haben or sein?",
                DiagramSpec(
                    "sein for movement from A to B and for change of state; haben for everything else.",
                    nodes = listOf(
                        DNode("v", "The verb", 0.5f, 0.09f, w = 0.36f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("h", "haben", 0.26f, 0.34f, w = 0.42f, h = 0.18f, tone = 1, sub = "everything else"),
                        DNode("s", "sein", 0.74f, 0.34f, w = 0.42f, h = 0.18f, sub = "A to B, or change"),
                        DNode("hn", "essen, kaufen, lesen", 0.26f, 0.62f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                        DNode("sn", "gehen, fahren, fliegen", 0.74f, 0.62f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                        DNode("ex", "bleiben, passieren", 0.74f, 0.88f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "exceptions: sein"),
                    ),
                    edges = listOf(
                        DEdge("v", "h"),
                        DEdge("v", "s"),
                        DEdge("h", "hn", dashed = true),
                        DEdge("s", "sn", dashed = true),
                        DEdge("sn", "ex", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("v", "h", "s")),
                    Beat(10, show = setOf("hn", "sn"), light = setOf("s"), pulse = "v" to "s", card = BoardCard("From A to B: sein", "Moving from one place to another, or a change like getting up.")),
                    Beat(11, light = setOf("sn"), card = BoardCard("Ich bin ins Kino gegangen.", "I went to the cinema.", phrase = true)),
                    Beat(14, light = setOf("s", "sn"), pulse = "s" to "sn", card = BoardCard("Wir sind nach Köln gefahren.", "We went to Cologne. Sanya flew to Doha.", phrase = true)),
                    Beat(16, show = setOf("ex"), light = setOf("ex"), pulse = "sn" to "ex"),
                    Beat(17, light = setOf("ex"), card = BoardCard("Was ist passiert?", "We stayed at home. What happened?", phrase = true)),
                ),
            ),
            // 18: "Okay. And how do I build the participle itself?"
            Scene(
                18, "Building the participle",
                DiagramSpec(
                    "Regular ge-t, irregular ge-en, separable with ge inside, and no ge- for be-, ver-, er-, -ieren.",
                    nodes = listOf(
                        DNode("reg", "ge + stem + t", 0.5f, 0.11f, w = 0.7f, h = 0.18f, tone = 1, sub = "gemacht, gekauft"),
                        DNode("irr", "ge + stem + en", 0.5f, 0.37f, w = 0.7f, h = 0.18f, tone = 1, sub = "getrunken, geschrieben"),
                        DNode("sep", "front piece + ge", 0.5f, 0.63f, w = 0.7f, h = 0.18f, sub = "eingekauft, aufgestanden"),
                        DNode("no", "no ge-", 0.5f, 0.89f, w = 0.7f, h = 0.18f, tone = 2, sub = "besucht, verstanden, telefoniert"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(19, show = setOf("reg", "irr"), light = setOf("reg", "irr")),
                    Beat(20, light = setOf("reg", "irr"), card = BoardCard("Ich habe Brot gekauft.", "I bought bread. I wrote an email.", phrase = true)),
                    Beat(22, show = setOf("sep", "no"), light = setOf("sep", "no"), card = BoardCard("be-, ver-, er-, -ieren", "No ge- at all: besucht, verstanden, telefoniert.")),
                    Beat(23, light = setOf("no"), card = BoardCard("Sanya hat ihre Eltern besucht.", "Sanya visited her parents. I understood everything.", phrase = true)),
                ),
            ),
            // 24: "Great. So how do I say I was ill? «Ich bin krank gewesen»?"
            Scene(
                24, "war and hatte",
                DiagramSpec(
                    "For was and had, German skips the Perfekt and uses the Präteritum: war and hatte.",
                    nodes = listOf(
                        DNode("sein", "sein", 0.26f, 0.12f, w = 0.4f, kind = NodeKind.PILL, tone = 1),
                        DNode("haben", "haben", 0.74f, 0.12f, w = 0.4f, kind = NodeKind.PILL, tone = 1),
                        DNode("war", "war", 0.26f, 0.44f, w = 0.4f, h = 0.18f, sub = "was"),
                        DNode("hatte", "hatte", 0.74f, 0.44f, w = 0.4f, h = 0.18f, sub = "had"),
                        DNode("wn", "ich war, du warst, wir waren", 0.26f, 0.8f, w = 0.44f, h = 0.26f, kind = NodeKind.NOTE, tone = 2),
                        DNode("hn", "ich hatte, du hattest, wir hatten", 0.74f, 0.8f, w = 0.44f, h = 0.26f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("sein", "war"),
                        DEdge("haben", "hatte"),
                        DEdge("war", "wn", dashed = true),
                        DEdge("hatte", "hn", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("sein", "haben")),
                    Beat(25, show = setOf("war", "hatte"), light = setOf("war", "hatte"), pulse = "sein" to "war", card = BoardCard("Präteritum", "The simple past. At A1, only war and hatte.")),
                    Beat(26, light = setOf("war"), card = BoardCard("Gestern war ich krank.", "Yesterday I was ill. We had no time.", phrase = true)),
                    Beat(28, show = setOf("wn", "hn"), light = setOf("wn", "hn")),
                    Beat(29, light = setOf("wn", "hn"), card = BoardCard("Ich war, du warst, wir waren.", "I was, you were, we were. And: ich hatte, du hattest, wir hatten.", phrase = true)),
                ),
            ),
            // 30: "So what should I remember for the Goethe exam?"
            Scene(
                30, "For the Goethe exam",
                DiagramSpec(
                    "The past in the Goethe A1: wait for the participle, choose the helper, and use war and hatte.",
                    nodes = listOf(
                        DNode("hl", "Hören, Lesen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "wait for the end"),
                        DNode("ss", "Sprechen, Schreiben", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "movement takes sein"),
                        DNode("wh", "war and hatte", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "not the Perfekt"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("hl", "ss", "wh"), card = BoardCard("Wait for the participle", "It comes last, and it tells you what happened.")),
                    Beat(33, card = BoardCard("Today", "Twenty participles with their helper: ten haben, five sein. Participle last.")),
                    Beat(34, card = BoardCard("Das war's für heute. Tschüss!", "That's it for today. Bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de10Dialogue() = PodcastBoard(
        "de10-dialogue",
        listOf(
            // 0: "Welcome back. Today is a dialogue, or really two. First, Monday mornin..."
            Scene(
                0, "Monday at work",
                DiagramSpec(
                    "Lena and Usaid are friends at work, so it is du: wie war dein Wochenende?",
                    nodes = listOf(
                        DNode("lena", "Lena", 0.22f, 0.28f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("usaid", "Usaid", 0.78f, 0.28f, h = 0.26f, kind = NodeKind.USER),
                        DNode("q", "Wie war dein Wochenende?", 0.5f, 0.75f, w = 0.8f, h = 0.22f, sub = "das Wochenende"),
                    ),
                    edges = listOf(
                        DEdge("lena", "usaid", "du"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("lena", "usaid")),
                    Beat(1, show = setOf("q"), light = setOf("q"), pulse = "lena" to "usaid", card = BoardCard("Wie war dein Wochenende?", "How was your weekend?", phrase = true)),
                    Beat(3, light = setOf("lena", "usaid"), card = BoardCard("war: was", "The Monday question. dein, because they say du.")),
                ),
            ),
            // 4: "Am Samstag sind Sanya und ich nach Köln gefahren."
            Scene(
                4, "Saturday in Köln",
                DiagramSpec(
                    "One Saturday, two helpers: sein for going somewhere, haben for seeing something.",
                    nodes = listOf(
                        DNode("sat", "Am Samstag", 0.5f, 0.1f, w = 0.46f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("koeln", "nach Köln", 0.26f, 0.42f, w = 0.44f, h = 0.2f, sub = "sind gefahren"),
                        DNode("dom", "der Dom", 0.74f, 0.42f, w = 0.44f, h = 0.2f, tone = 1, sub = "haben gesehen"),
                        DNode("rhein", "der Rhein", 0.5f, 0.8f, w = 0.6f, h = 0.2f, sub = "sind spazieren gegangen"),
                    ),
                    edges = listOf(
                        DEdge("sat", "koeln"),
                        DEdge("koeln", "dom"),
                        DEdge("koeln", "rhein"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("sat", "koeln"), light = setOf("koeln"), card = BoardCard("... nach Köln gefahren.", "On Saturday Sanya and I went to Cologne.", phrase = true)),
                    Beat(6, light = setOf("koeln"), pulse = "sat" to "koeln", card = BoardCard("From A to B: sein", "sind comes second, after Am Samstag; gefahren waits at the end.")),
                    Beat(7, show = setOf("dom", "rhein"), light = setOf("dom", "rhein"), card = BoardCard("Wir haben den Dom gesehen.", "We saw the cathedral and went for a walk by the Rhine.", phrase = true)),
                    Beat(9, light = setOf("dom", "rhein"), pulse = "koeln" to "rhein", card = BoardCard("The helper follows the verb", "Seeing: haben. Walking somewhere: sein. Not the person.")),
                ),
            ),
            // 10: "Und am Sonntag?"
            Scene(
                10, "Sunday, and Lena",
                DiagramSpec(
                    "Usaid's Sunday is all haben; Lena was ill, with war, and stayed home, with sein.",
                    nodes = listOf(
                        DNode("sun", "Am Sonntag", 0.5f, 0.1f, w = 0.46f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("sl", "lange geschlafen", 0.26f, 0.4f, w = 0.44f, h = 0.2f, tone = 1, sub = "haben"),
                        DNode("fi", "der Film", 0.74f, 0.4f, w = 0.44f, h = 0.2f, tone = 1, sub = "haben gesehen"),
                        DNode("lena", "Ich war krank.", 0.26f, 0.78f, w = 0.44f, h = 0.22f, sub = "war, not bin gewesen"),
                        DNode("bl", "zu Hause geblieben", 0.74f, 0.78f, w = 0.44f, h = 0.22f, tone = 2, sub = "bleiben takes sein"),
                    ),
                    edges = listOf(
                        DEdge("sun", "sl"),
                        DEdge("sl", "fi"),
                        DEdge("lena", "bl"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("sun"), card = BoardCard("Und am Sonntag?", "And on Sunday?", phrase = true)),
                    Beat(12, show = setOf("sl"), light = setOf("sl"), pulse = "sun" to "sl"),
                    Beat(13, light = setOf("sl"), card = BoardCard("Ich habe lange geschlafen.", "On Sunday I slept for a long time.", phrase = true)),
                    Beat(16, show = setOf("fi"), light = setOf("fi"), pulse = "sl" to "fi", card = BoardCard("Und du?", "Then we cooked together and watched a film. And you?", phrase = true)),
                    Beat(18, show = setOf("lena"), light = setOf("lena")),
                    Beat(19, show = setOf("bl"), light = setOf("lena", "bl"), card = BoardCard("Ich war krank.", "I was ill. I stayed at home and drank a lot of tea.", phrase = true)),
                    Beat(20, light = setOf("bl"), pulse = "lena" to "bl", card = BoardCard("bleiben: sein", "Nobody moves, but it is still sein: ich bin geblieben.")),
                ),
            ),
            // 21: "Good ear. «bleiben», to stay, is the exception: it takes «sein». Now t..."
            Scene(
                21, "Sorry I am late",
                DiagramSpec(
                    "The excuse: sorry first, then the reason. The train had a delay, then the bus was missed.",
                    nodes = listOf(
                        DNode("chef", "der Chef", 0.22f, 0.16f, w = 0.3f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("usaid", "Usaid", 0.78f, 0.16f, w = 0.3f, h = 0.26f, kind = NodeKind.USER),
                        DNode("clock", "halb zehn", 0.5f, 0.45f, w = 0.36f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("zug", "der Zug", 0.26f, 0.78f, w = 0.44f, h = 0.22f, tone = 1, sub = "hatte Verspätung"),
                        DNode("bus", "der Bus", 0.74f, 0.78f, w = 0.44f, h = 0.22f, sub = "verpasst: no ge-"),
                    ),
                    edges = listOf(
                        DEdge("zug", "bus", "dann"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("chef", "usaid")),
                    Beat(22, show = setOf("clock"), light = setOf("clock"), card = BoardCard("Es ist schon halb zehn.", "Good morning, Mr Shahab. It's already half past nine.", phrase = true)),
                    Beat(24, light = setOf("usaid"), card = BoardCard("Es tut mir leid", "I'm sorry. First the apology, then the reason.")),
                    Beat(25, show = setOf("zug"), light = setOf("zug"), card = BoardCard("Der Zug hatte Verspätung.", "I'm sorry. The train was thirty minutes late.", phrase = true)),
                    Beat(27, show = setOf("bus"), light = setOf("bus"), card = BoardCard("verpassen, verpasst", "To miss. ver- means no ge-. Not passed!")),
                    Beat(28, light = setOf("zug", "bus"), pulse = "zug" to "bus", card = BoardCard("Ich habe den Bus verpasst.", "And then I missed the bus.", phrase = true)),
                ),
            ),
            // 29: "Is the boss angry?"
            Scene(
                29, "Kein Problem",
                DiagramSpec(
                    "The boss says Sie; Usaid read the email and already replied.",
                    nodes = listOf(
                        DNode("sie", "Sie", 0.5f, 0.13f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "formal: the boss"),
                        DNode("mail", "die E-Mail", 0.5f, 0.47f, w = 0.56f, h = 0.2f, tone = 1, sub = "gelesen?"),
                        DNode("rep", "schon geantwortet", 0.5f, 0.82f, w = 0.56f, h = 0.2f, sub = "already replied"),
                    ),
                    edges = listOf(
                        DEdge("sie", "mail"),
                        DEdge("mail", "rep"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("sie"), light = setOf("sie")),
                    Beat(31, show = setOf("mail"), light = setOf("mail"), pulse = "sie" to "mail", card = BoardCard("Kein Problem.", "No problem. Have you read the email from Ms Braun?", phrase = true)),
                    Beat(33, show = setOf("rep"), light = setOf("rep"), pulse = "mail" to "rep"),
                    Beat(34, light = setOf("rep"), card = BoardCard("Ja, ich habe sie gelesen.", "Yes, I read it last night and have already replied.", phrase = true)),
                ),
            ),
            // 35: "One more thing. How do I give a reason, like because I was ill?"
            Scene(
                35, "weil: verb to the end",
                DiagramSpec(
                    "After weil, the verb goes to the very end of its part of the sentence: weil ich krank war.",
                    nodes = listOf(
                        DNode("a", "Ich war nicht da,", 0.5f, 0.12f, w = 0.6f, tone = 2),
                        DNode("b", "weil", 0.5f, 0.37f, w = 0.6f, tone = 1, sub = "because"),
                        DNode("c", "ich krank", 0.5f, 0.62f, w = 0.6f, tone = 2),
                        DNode("d", "war", 0.5f, 0.87f, w = 0.6f, sub = "the verb, last"),
                    ),
                    edges = listOf(
                        DEdge("a", "b"),
                        DEdge("b", "c"),
                        DEdge("c", "d"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("a", "b", "c", "d"), light = setOf("b", "d")),
                    Beat(37, light = setOf("d"), card = BoardCard("... weil ich krank war.", "I wasn't there because I was ill.", phrase = true)),
                ),
            ),
            // 38: "So what should I remember for the Goethe exam?"
            Scene(
                38, "For the Goethe exam",
                DiagramSpec(
                    "This dialogue in the Goethe A1: excuses in Hören, your weekend in Sprechen, and ich war krank.",
                    nodes = listOf(
                        DNode("h", "Hören", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "listen for the reason"),
                        DNode("s", "Sprechen", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "time word each"),
                        DNode("w", "ich war krank", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "not bin krank gewesen"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(39, show = setOf("h", "s", "w"), card = BoardCard("Verspätung, verpasst", "The reason is what the Hören question asks about.")),
                    Beat(41, card = BoardCard("Today", "Wie war dein Wochenende? Five past sentences, ordered with zuerst, dann, danach.")),
                    Beat(42, card = BoardCard("Schönes Wochenende! Tschüss!", "Have a nice weekend! Bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de10Practice() = PodcastBoard(
        "de10-practice",
        listOf(
            // 0: "Welcome back. Today is practice, with three exam tasks in one: reading..."
            Scene(
                0, "Three tasks, one past",
                DiagramSpec(
                    "Reading a postcard, writing one, and telling yesterday: all three are in the past.",
                    nodes = listOf(
                        DNode("le", "Lesen", 0.5f, 0.15f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 1, sub = "a holiday postcard"),
                        DNode("sc", "Schreiben", 0.5f, 0.5f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 1, sub = "your own postcard"),
                        DNode("sp", "Sprechen", 0.5f, 0.85f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 1, sub = "your yesterday"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("le", "sc", "sp")),
                    Beat(1, light = setOf("le")),
                ),
            ),
            // 2: "In the Goethe exam, they do. Lesen part one is often a short personal ..."
            Scene(
                2, "Sanya's postcard",
                DiagramSpec(
                    "Sanya's card from Hamburg: the greeting, how they came, and what they did.",
                    nodes = listOf(
                        DNode("gr", "Grüße aus Hamburg", 0.5f, 0.13f, w = 0.7f, h = 0.2f, tone = 1, sub = "Liebe Lena"),
                        DNode("zug", "der Zug", 0.5f, 0.47f, w = 0.7f, h = 0.2f, sub = "Freitag: gekommen"),
                        DNode("ha", "der Hafen, das Schiff", 0.5f, 0.82f, w = 0.7f, h = 0.22f, tone = 1, sub = "Samstag: gesehen, gefahren"),
                    ),
                    edges = listOf(
                        DEdge("gr", "zug"),
                        DEdge("zug", "ha"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("gr"), light = setOf("gr"), card = BoardCard("Liebe Lena, viele Grüße aus Hamburg!", "Dear Lena, greetings from Hamburg!", phrase = true)),
                    Beat(5, light = setOf("gr"), card = BoardCard("Liebe or Lieber", "Dear: Liebe Lena, Lieber Tom.")),
                    Beat(6, show = setOf("zug"), light = setOf("zug"), pulse = "gr" to "zug", card = BoardCard("Mit dem Zug gekommen.", "We came by train on Friday.", phrase = true)),
                    Beat(8, light = setOf("zug"), card = BoardCard("By plane? Falsch.", "Check the transport, the weather and the days against the text.")),
                    Beat(9, show = setOf("ha"), light = setOf("ha"), pulse = "zug" to "ha", card = BoardCard("Wir haben den Hafen gesehen.", "On Saturday we saw the harbour and went on a boat.", phrase = true)),
                    Beat(11, light = setOf("ha"), card = BoardCard("Two helpers", "Seeing the harbour: haben. Going by ship: sein.")),
                ),
            ),
            // 12: "Das Wetter war leider schlecht, es hat viel geregnet."
            Scene(
                12, "Weather and the way back",
                DiagramSpec(
                    "The weather with war, yesterday in the museum, and the way back in the present.",
                    nodes = listOf(
                        DNode("we", "das Wetter", 0.5f, 0.13f, w = 0.7f, h = 0.2f, sub = "leider schlecht"),
                        DNode("mu", "das Museum", 0.5f, 0.47f, w = 0.7f, h = 0.2f, tone = 1, sub = "gestern"),
                        DNode("zu", "zurück nach Doha", 0.5f, 0.82f, w = 0.7f, h = 0.2f, tone = 1, sub = "Montag: still to come"),
                    ),
                    edges = listOf(
                        DEdge("we", "mu"),
                        DEdge("mu", "zu"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("we"), light = setOf("we"), card = BoardCard("Das Wetter war leider schlecht.", "Unfortunately the weather was bad; it rained a lot.", phrase = true)),
                    Beat(14, show = setOf("mu", "zu"), light = setOf("we"), card = BoardCard("Good weather? Falsch.", "leider schlecht: unfortunately bad.")),
                    Beat(15, light = setOf("mu", "zu"), pulse = "mu" to "zu", card = BoardCard("Am Montag fliegen wir zurück.", "Yesterday we were at the museum. On Monday we fly back to Doha.", phrase = true)),
                    Beat(16, light = setOf("zu"), card = BoardCard("Coming back: present", "It hasn't happened yet, so fliegen stays in the present.")),
                ),
            ),
            // 17: "Good catch. Flying back hasn't happened yet, so it's the present. And ..."
            Scene(
                17, "Your postcard: four points",
                DiagramSpec(
                    "A postcard about a trip: where you were, two things you did, the weather, when you come back.",
                    nodes = listOf(
                        DNode("a", "Ich war in ...", 0.5f, 0.11f, w = 0.64f, h = 0.17f, tone = 1, sub = "where you were"),
                        DNode("b", "Ich habe ... / bin ...", 0.5f, 0.37f, w = 0.64f, h = 0.17f, sub = "two things you did"),
                        DNode("c", "Das Wetter war ...", 0.5f, 0.63f, w = 0.64f, h = 0.17f, tone = 2, sub = "the weather"),
                        DNode("d", "Am ... komme ich zurück.", 0.5f, 0.89f, w = 0.64f, h = 0.17f, sub = "when you come back"),
                    ),
                    edges = listOf(
                        DEdge("a", "b"),
                        DEdge("b", "c"),
                        DEdge("c", "d"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("a", "b", "c", "d"), card = BoardCard("Four points", "Where you were, two things you did, the weather, when you come back.")),
                    Beat(18, light = setOf("a"), card = BoardCard("Ich war drei Tage hier.", "Hi Lena, greetings from Berlin! I was here for three days.", phrase = true)),
                    Beat(21, light = setOf("b"), pulse = "a" to "b", card = BoardCard("Brandenburger Tor gesehen", "I saw the Brandenburg Gate and went for lots of walks.", phrase = true)),
                    Beat(23, light = setOf("c"), pulse = "b" to "c"),
                    Beat(24, light = setOf("c", "d"), pulse = "c" to "d", card = BoardCard("Das Wetter war schön und warm.", "The weather was nice and warm. On Sunday I'm coming back to Doha.", phrase = true)),
                ),
            ),
            // 25: "That's four points. And the speaking part? What does the examiner ask?"
            Scene(
                25, "Your yesterday",
                DiagramSpec(
                    "Was haben Sie gestern gemacht? Three sentences, each with a time word.",
                    nodes = listOf(
                        DNode("q", "Was haben Sie gestern gemacht?", 0.5f, 0.1f, w = 0.9f, kind = NodeKind.NOTE, tone = 1),
                        DNode("g", "gestern", 0.26f, 0.44f, w = 0.44f, h = 0.22f, sub = "aufgestanden: sein"),
                        DNode("d", "dann", 0.74f, 0.44f, w = 0.44f, h = 0.22f, tone = 1, sub = "gearbeitet: haben"),
                        DNode("ab", "am Abend", 0.26f, 0.8f, w = 0.44f, h = 0.22f, tone = 1, sub = "gelernt: haben"),
                        DNode("j", "letztes Jahr", 0.74f, 0.8f, w = 0.44f, h = 0.22f, sub = "geflogen: sein"),
                    ),
                    edges = listOf(
                        DEdge("g", "d"),
                        DEdge("d", "ab"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("q"), light = setOf("q")),
                    Beat(27, light = setOf("q"), card = BoardCard("Was haben Sie gestern gemacht?", "What did you do yesterday?", phrase = true)),
                    Beat(30, show = setOf("g"), light = setOf("g"), card = BoardCard("Um sieben Uhr aufgestanden.", "Yesterday I got up at seven.", phrase = true)),
                    Beat(33, show = setOf("d", "ab"), light = setOf("d", "ab"), pulse = "d" to "ab", card = BoardCard("Am Abend habe ich Deutsch gelernt.", "Then I worked, and in the evening I studied German.", phrase = true)),
                    Beat(36, show = setOf("j"), light = setOf("j"), card = BoardCard("Nach Pakistan geflogen.", "Last year I flew to Pakistan.", phrase = true)),
                ),
            ),
            // 37: "So what should I remember for the Goethe exam?"
            Scene(
                37, "For the Goethe exam",
                DiagramSpec(
                    "This practice in the Goethe A1: check the text in Lesen, four points in Schreiben, three sentences in Sprechen.",
                    nodes = listOf(
                        DNode("le", "Lesen", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "transport, weather, days"),
                        DNode("sc", "Schreiben", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "four points"),
                        DNode("sp", "Sprechen", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "three sentences, time words"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("le", "sc", "sp"), card = BoardCard("Don't forget the way back", "When you come back is nearly always one of the four points.")),
                    Beat(40, card = BoardCard("Today", "Five sentences about yesterday, a four-point postcard, and Wie war dein Tag?")),
                    Beat(41, card = BoardCard("Viele Grüße und bis bald!", "Best wishes, and see you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de11Forms() = PodcastBoard(
        "de11-forms",
        listOf(
            // 0: "Welcome back. Today is the Goethe exam's «Schreiben», part one: a form..."
            Scene(
                0, "Schreiben, part one",
                DiagramSpec(
                    "The answers are already in the situation text: you copy them into the five gaps of the form.",
                    nodes = listOf(
                        DNode("sit", "Situation", 0.5f, 0.14f, w = 0.5f, h = 0.18f, kind = NodeKind.NOTE, tone = 1, sub = "the short text"),
                        DNode("form", "das Formular", 0.5f, 0.5f, w = 0.5f, h = 0.18f, sub = "five gaps"),
                        DNode("you", "You", 0.5f, 0.84f, w = 0.22f, h = 0.2f, kind = NodeKind.USER),
                    ),
                    edges = listOf(
                        DEdge("sit", "form", "copy"),
                        DEdge("you", "form", "fill in"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("sit", "form"), light = setOf("form")),
                    Beat(1, light = setOf("sit"), pulse = "sit" to "form", card = BoardCard("Copy, don't invent", "Almost every answer is already in the situation text.")),
                    Beat(3, show = setOf("you"), light = setOf("form"), pulse = "you" to "form", card = BoardCard("Füllen Sie das Formular aus.", "Fill in the form.", phrase = true)),
                ),
            ),
            // 4: "«Formular» is the form. Is it der, die or das?"
            Scene(
                4, "Names and places",
                DiagramSpec(
                    "Familienname is the surname, Vorname the first name; Geburtsort is where you were born, Wohnort where you live.",
                    nodes = listOf(
                        DNode("fn", "der Familienname", 0.25f, 0.14f, w = 0.44f, h = 0.2f, tone = 1, sub = "surname: Khan"),
                        DNode("vn", "der Vorname", 0.75f, 0.14f, w = 0.44f, h = 0.2f, tone = 1, sub = "first name: Sanya"),
                        DNode("sanya", "Sanya", 0.5f, 0.48f, w = 0.22f, h = 0.2f, kind = NodeKind.USER),
                        DNode("go", "der Geburtsort", 0.25f, 0.84f, w = 0.44f, h = 0.2f, sub = "born: Lahore"),
                        DNode("wo", "der Wohnort", 0.75f, 0.84f, w = 0.44f, h = 0.2f, sub = "lives now: Köln"),
                    ),
                    edges = listOf(
                        DEdge("sanya", "go", "born"),
                        DEdge("sanya", "wo", "lives"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, card = BoardCard("das Formular", "ausfüllen: füllen near the front, aus at the very end.")),
                    Beat(6, show = setOf("fn", "vn"), light = setOf("fn", "vn"), card = BoardCard("Familienname: Khan. Vorname: Sanya.", "Surname: Khan. First name: Sanya.", phrase = true)),
                    Beat(8, light = setOf("fn"), card = BoardCard("Don't swap them", "Familienname is the surname; Vorname comes before.")),
                    Beat(9, show = setOf("sanya", "go", "wo"), light = setOf("go", "wo"), card = BoardCard("Geburtsort: Lahore. Wohnort: Köln.", "Place of birth: Lahore. Place of residence: Cologne.", phrase = true)),
                    Beat(11, light = setOf("go"), pulse = "sanya" to "go", card = BoardCard("Geburt is birth", "Geburtsort: where you were born. Wohnort: where you live now.")),
                ),
            ),
            // 12: "Der fünfte Mai neunzehnhundertachtundneunzig."
            Scene(
                12, "Dates in digits",
                DiagramSpec(
                    "The text says the date in words; the form wants digits, day first, with dots. Said aloud, it is an ordinal.",
                    nodes = listOf(
                        DNode("said", "der fünfte Mai 1998", 0.5f, 0.12f, w = 0.7f, h = 0.18f, tone = 1, sub = "said out loud"),
                        DNode("wr", "05.05.1998", 0.5f, 0.4f, w = 0.7f, h = 0.18f, sub = "on the form: day.month.year"),
                        DNode("o1", "1. der erste", 0.25f, 0.66f, w = 0.44f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("o2", "2. der zweite", 0.75f, 0.66f, w = 0.44f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("o3", "3. der dritte", 0.25f, 0.86f, w = 0.44f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                        DNode("o7", "7. der siebte", 0.75f, 0.86f, w = 0.44f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("said", "wr", "on the form"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("said"), light = setOf("said"), card = BoardCard("Der fünfte Mai 1998.", "The fifth of May, nineteen ninety-eight.", phrase = true)),
                    Beat(14, show = setOf("wr"), light = setOf("wr"), pulse = "said" to "wr", card = BoardCard("Day, month, year, with dots", "Words in the text, digits on the form: 05.05.1998.")),
                    Beat(15, show = setOf("o1", "o2", "o3", "o7"), card = BoardCard("Der erste, der zweite, der dritte.", "The first, the second, the third. And der siebte, the seventh.", phrase = true)),
                    Beat(17, light = setOf("o1", "o3", "o7"), card = BoardCard("The odd ones: erste, dritte, siebte", "Others add -te up to 19 (der zwölfte), -ste from 20 (der zwanzigste).")),
                ),
            ),
            // 18: "Staatsangehörigkeit: pakistanisch."
            Scene(
                18, "Nationality and address",
                DiagramSpec(
                    "Staatsangehörigkeit wants the adjective with a small letter, not the country.",
                    nodes = listOf(
                        DNode("country", "Pakistan", 0.25f, 0.14f, w = 0.4f, h = 0.2f, tone = 2, sub = "the country"),
                        DNode("adj", "pakistanisch", 0.75f, 0.14f, w = 0.42f, h = 0.2f, sub = "the nationality"),
                        DNode("staat", "die Staatsangehörigkeit", 0.5f, 0.42f, w = 0.8f, h = 0.14f, kind = NodeKind.NOTE, tone = 1),
                        DNode("street", "Straße, Hausnummer", 0.5f, 0.65f, w = 0.8f, h = 0.18f, tone = 1, sub = "Schulstraße 8"),
                        DNode("plz", "die PLZ, Wohnort", 0.5f, 0.87f, w = 0.8f, h = 0.18f, tone = 1, sub = "50667 Köln"),
                    ),
                    edges = listOf(
                        DEdge("country", "adj", "adjective"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("staat", "adj"), light = setOf("adj"), card = BoardCard("Staatsangehörigkeit: pakistanisch.", "Nationality: Pakistani.", phrase = true)),
                    Beat(20, show = setOf("country", "street", "plz"), light = setOf("adj"), pulse = "country" to "adj", card = BoardCard("Adjective, small letter", "pakistanisch, deutsch, indisch, katarisch.")),
                    Beat(21, light = setOf("street", "plz"), card = BoardCard("Schulstraße 8, 50667 Köln", "8 School Street, postcode 50667, Cologne.", phrase = true)),
                ),
            ),
            // 22: "And the forms with little squares? I've seen those."
            Scene(
                22, "Tick, don't write",
                DiagramSpec(
                    "A choice field says bitte ankreuzen: put a cross in one box instead of writing an answer.",
                    nodes = listOf(
                        DNode("kz", "Kurszeit (bitte ankreuzen)", 0.5f, 0.14f, w = 0.8f, kind = NodeKind.NOTE, tone = 1),
                        DNode("vm", "vormittags", 0.25f, 0.5f, w = 0.4f, h = 0.2f, tone = 2, sub = "mornings"),
                        DNode("ab", "abends", 0.75f, 0.5f, w = 0.4f, h = 0.2f, sub = "evenings: ticked"),
                        DNode("you", "Sanya", 0.5f, 0.84f, w = 0.22f, h = 0.2f, kind = NodeKind.USER),
                    ),
                    edges = listOf(
                        DEdge("you", "ab", "X"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("kz"), light = setOf("kz")),
                    Beat(23, show = setOf("vm", "ab", "you"), light = setOf("ab"), pulse = "you" to "ab", card = BoardCard("ankreuzen: tick", "Writing beside an empty box scores nothing.")),
                    Beat(24, light = setOf("vm", "ab"), card = BoardCard("Vormittags oder abends?", "Mornings or evenings? Please tick.", phrase = true)),
                ),
            ),
            // 25: "Is it always a language course, or could it be something else?"
            Scene(
                25, "Hotel and trip forms",
                DiagramSpec(
                    "Other forms the exam likes: a hotel or a trip, with rooms, nights, payment, and the date and signature.",
                    nodes = listOf(
                        DNode("dz", "das Doppelzimmer", 0.25f, 0.14f, w = 0.46f, h = 0.2f, tone = 1, sub = "double room"),
                        DNode("nacht", "die Nacht, die Nächte", 0.75f, 0.14f, w = 0.46f, h = 0.2f, tone = 1, sub = "night, nights"),
                        DNode("zahl", "die Zahlung", 0.25f, 0.48f, w = 0.46f, h = 0.2f, tone = 1, sub = "bar oder Kreditkarte"),
                        DNode("fs", "der Familienstand", 0.75f, 0.48f, w = 0.46f, h = 0.2f, tone = 1, sub = "ledig, verheiratet"),
                        DNode("dat", "das Datum", 0.25f, 0.84f, w = 0.46f, h = 0.2f, sub = "date"),
                        DNode("us", "die Unterschrift", 0.75f, 0.84f, w = 0.46f, h = 0.2f, sub = "signature"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("dz", "nacht", "zahl", "fs"), card = BoardCard("A hotel or a trip", "Anzahl, Nacht or Nächte, Zahlung, Familienstand.")),
                    Beat(27, light = setOf("dz", "nacht", "zahl"), card = BoardCard("Ein Doppelzimmer für zwei Nächte.", "A double room for two nights. I pay by credit card.", phrase = true)),
                    Beat(29, show = setOf("dat", "us"), light = setOf("dat", "us"), card = BoardCard("Never leave a gap", "Every gap carries a point: a guess from the text beats nothing.")),
                ),
            ),
            // 30: "So what should I remember for the Goethe exam?"
            Scene(
                30, "For the Goethe exam",
                DiagramSpec(
                    "Schreiben part 1: learn the field names, copy from the text, and use the two formats.",
                    nodes = listOf(
                        DNode("fields", "Field names", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "they are the whole task"),
                        DNode("copy", "Copy, don't invent", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Geburtsort, not Wohnort"),
                        DNode("fmt", "Two formats", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "05.05.1998 and pakistanisch"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("fields", "copy", "fmt"), card = BoardCard("Schreiben, part one", "Field names as words; every answer from the text; dates with dots.")),
                    Beat(33, card = BoardCard("Today", "Fill in a real German form: date with dots, nationality as an adjective, tick.")),
                    Beat(34, card = BoardCard("Viel Erfolg beim Formular! Tschüss!", "Good luck with the form! Bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de11Messages() = PodcastBoard(
        "de11-messages",
        listOf(
            // 0: "Welcome back. Today is «Schreiben», part two: a short message. You get..."
            Scene(
                0, "The five-line recipe",
                DiagramSpec(
                    "Five lines every time: the greeting, the three content points from the task, the sign-off.",
                    nodes = listOf(
                        DNode("a", "Anrede", 0.5f, 0.1f, w = 0.8f, tone = 1, sub = "the greeting"),
                        DNode("p1", "Punkt 1", 0.5f, 0.3f, w = 0.5f, h = 0.14f),
                        DNode("p2", "Punkt 2", 0.5f, 0.5f, w = 0.5f, h = 0.14f),
                        DNode("p3", "Punkt 3", 0.5f, 0.7f, w = 0.5f, h = 0.14f),
                        DNode("g", "Gruß", 0.5f, 0.9f, w = 0.8f, tone = 1, sub = "the sign-off, then your name"),
                    ),
                    edges = listOf(
                        DEdge("a", "p1"),
                        DEdge("p1", "p2"),
                        DEdge("p2", "p3"),
                        DEdge("p3", "g"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("p1", "p2", "p3"), light = setOf("p1", "p2", "p3"), card = BoardCard("About thirty words", "Three content points carry most of the ten marks.")),
                    Beat(2, show = setOf("a", "g"), light = setOf("a", "g"), pulse = "p3" to "g", card = BoardCard("Five lines", "Anrede, point, point, point, Gruß.")),
                ),
            ),
            // 3: "Liebe Lena, lieber Tom."
            Scene(
                3, "du or Sie",
                DiagramSpec(
                    "Greeting and sign-off come as a pair: Liebe with Viele Grüße for du, Sehr geehrte with Mit freundlichen Grüßen for Sie.",
                    nodes = listOf(
                        DNode("du", "du", 0.25f, 0.1f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "a friend"),
                        DNode("sie", "Sie", 0.75f, 0.1f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, sub = "teacher, office"),
                        DNode("liebe", "Liebe Lena,", 0.25f, 0.34f, w = 0.46f, tone = 1, sub = "a woman"),
                        DNode("sg", "Sehr geehrte Frau Braun,", 0.75f, 0.34f, w = 0.48f, sub = "a woman"),
                        DNode("lieber", "Lieber Tom,", 0.25f, 0.58f, w = 0.46f, tone = 1, sub = "a man"),
                        DNode("sgr", "Sehr geehrter Herr Weber,", 0.75f, 0.58f, w = 0.48f, sub = "a man"),
                        DNode("vg", "Viele Grüße", 0.25f, 0.86f, w = 0.46f, kind = NodeKind.NOTE, tone = 1, sub = "or Liebe Grüße"),
                        DNode("mfg", "Mit freundlichen Grüßen", 0.75f, 0.86f, w = 0.48f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("du", "liebe"),
                        DEdge("sie", "sg"),
                        DEdge("lieber", "vg", dashed = true),
                        DEdge("sgr", "mfg", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("du", "liebe", "lieber"), light = setOf("liebe", "lieber"), card = BoardCard("Liebe Lena, lieber Tom.", "Dear Lena, dear Tom.", phrase = true)),
                    Beat(5, show = setOf("sie"), light = setOf("liebe", "lieber"), card = BoardCard("The ending follows the person", "Liebe for a woman, Lieber for a man.")),
                    Beat(6, show = setOf("sg", "sgr"), light = setOf("sg", "sgr"), pulse = "sie" to "sg", card = BoardCard("Sehr geehrte Frau Braun.", "Dear Ms Braun. And: Sehr geehrter Herr Weber, Dear Mr Weber.", phrase = true)),
                    Beat(8, light = setOf("liebe"), card = BoardCard("Comma, then a small letter", "A new line is not a new sentence.")),
                    Beat(9, light = setOf("liebe"), card = BoardCard("Liebe Lena, ich komme gern.", "Dear Lena, I'd love to come.", phrase = true)),
                    Beat(11, show = setOf("vg", "mfg"), light = setOf("vg", "mfg"), pulse = "lieber" to "vg", card = BoardCard("Match the two halves", "No full stop after the sign-off; your name on the next line.")),
                    Beat(12, light = setOf("vg", "mfg"), pulse = "sgr" to "mfg", card = BoardCard("Viele Grüße, Usaid.", "Best wishes, Usaid. Formal: Mit freundlichen Grüßen.", phrase = true)),
                ),
            ),
            // 14: "Then you lose the mark for register, however good your German is. Now ..."
            Scene(
                14, "Three content points",
                DiagramSpec(
                    "Each Inhaltspunkt is marked on its own, so one line per point, in the task's order.",
                    nodes = listOf(
                        DNode("task", "Task: no class on Saturday", 0.5f, 0.1f, w = 0.84f, h = 0.14f, kind = NodeKind.NOTE, tone = 1),
                        DNode("p1", "Punkt 1: warum nicht?", 0.5f, 0.34f, w = 0.7f, h = 0.18f, sub = "weil ich arbeiten muss"),
                        DNode("p2", "Punkt 2: zu Hause", 0.5f, 0.6f, w = 0.7f, h = 0.18f, sub = "Wörter, Hausaufgaben"),
                        DNode("p3", "Punkt 3: wann?", 0.5f, 0.86f, w = 0.7f, h = 0.18f, sub = "nächsten Samstag"),
                    ),
                    edges = listOf(
                        DEdge("task", "p1"),
                        DEdge("p1", "p2"),
                        DEdge("p2", "p3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("task"), light = setOf("task"), card = BoardCard("Each point is marked on its own", "Skip one, and that one's marks are gone.")),
                    Beat(16, show = setOf("p1"), light = setOf("p1"), pulse = "task" to "p1"),
                    Beat(17, light = setOf("p1"), card = BoardCard("…weil ich arbeiten muss.", "Unfortunately I can't come to class on Saturday, because I have to work.", phrase = true)),
                    Beat(19, light = setOf("p1"), card = BoardCard("weil sends the verb to the end", "A reason with weil is also an easy way to reach thirty words.")),
                    Beat(20, show = setOf("p2"), light = setOf("p2"), pulse = "p1" to "p2", card = BoardCard("Ich lerne zu Hause die Wörter.", "At home I learn the words and do the homework.", phrase = true)),
                    Beat(22, show = setOf("p3"), light = setOf("p3"), pulse = "p2" to "p3"),
                    Beat(23, light = setOf("p3"), card = BoardCard("Nächsten Samstag komme ich wieder.", "I'll be back next Saturday.", phrase = true)),
                ),
            ),
            // 24: "That's it? Then «Mit freundlichen Grüßen» and my name?"
            Scene(
                24, "A friend's birthday",
                DiagramSpec(
                    "The same five lines with du: Lieber Tom, the three points, then Bis bald and your name.",
                    nodes = listOf(
                        DNode("lt", "Lieber Tom,", 0.5f, 0.1f, w = 0.7f, h = 0.14f, tone = 1),
                        DNode("g1", "herzlichen Glückwunsch", 0.5f, 0.31f, w = 0.7f, sub = "Punkt 1"),
                        DNode("g2", "ein Buch über Fußball", 0.5f, 0.52f, w = 0.7f, sub = "Punkt 2: the gift"),
                        DNode("g3", "am Samstag um sechs", 0.5f, 0.73f, w = 0.7f, sub = "Punkt 3: when"),
                        DNode("bb", "Bis bald, Usaid", 0.5f, 0.91f, w = 0.7f, h = 0.14f, kind = NodeKind.NOTE, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("lt", "g1"),
                        DEdge("g1", "g2"),
                        DEdge("g2", "g3"),
                        DEdge("g3", "bb"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, card = BoardCard("Five lines, thirty words", "Greeting, three points, sign-off: done.")),
                    Beat(26, show = setOf("lt", "g1"), light = setOf("g1"), pulse = "lt" to "g1", card = BoardCard("Lieber Tom, herzlichen Glückwunsch!", "Dear Tom, happy birthday!", phrase = true)),
                    Beat(28, light = setOf("lt", "g1"), card = BoardCard("The comma rule again", "herzlichen stays small after Lieber Tom,")),
                    Beat(29, show = setOf("g2", "g3", "bb"), light = setOf("g2", "g3"), card = BoardCard("Ich schenke dir ein Buch.", "I'm giving you a book about football. I'm coming on Saturday at six.", phrase = true)),
                ),
            ),
            // 30: "Are there sentences I can reuse in almost any task?"
            Scene(
                30, "Sentences for any task",
                DiagramSpec(
                    "Stock sentences that cover almost every task; a question is the easiest third point.",
                    nodes = listOf(
                        DNode("du", "Kannst du mir bitte …?", 0.25f, 0.16f, w = 0.46f, h = 0.22f, tone = 1, sub = "asking a friend"),
                        DNode("sie", "Können Sie mir bitte …?", 0.75f, 0.16f, w = 0.46f, h = 0.22f, sub = "asking formally"),
                        DNode("sorry", "Es tut mir leid", 0.25f, 0.5f, w = 0.46f, h = 0.22f, tone = 1, sub = "sorry"),
                        DNode("thx", "Vielen Dank für …", 0.75f, 0.5f, w = 0.46f, h = 0.22f, tone = 1, sub = "thanks"),
                        DNode("q", "Soll ich etwas mitbringen?", 0.5f, 0.84f, w = 0.7f, h = 0.2f, sub = "a question"),
                    ),
                    edges = listOf(
                        DEdge("thx", "q"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("du", "sie", "sorry", "thx"), card = BoardCard("Reuse them", "Ask, say sorry, say thanks; a question is the easiest third point.")),
                    Beat(32, light = setOf("sie"), card = BoardCard("Können Sie mir bitte … schicken?", "Can you please send me the homework?", phrase = true)),
                    Beat(34, light = setOf("thx"), card = BoardCard("An invitation", "Thank her, say when you're coming, ask a question.")),
                    Beat(35, show = setOf("q"), light = setOf("thx", "q"), pulse = "thx" to "q", card = BoardCard("Vielen Dank für die Einladung.", "Thank you for the invitation. Should I bring anything?", phrase = true)),
                ),
            ),
            // 36: "So what should I remember for the Goethe exam?"
            Scene(
                36, "For the Goethe exam",
                DiagramSpec(
                    "Schreiben part 2: five lines, one register, and all three content points.",
                    nodes = listOf(
                        DNode("five", "Five lines", 0.26f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "about thirty words"),
                        DNode("reg", "One register", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "greeting matches sign-off"),
                        DNode("pts", "All three points", 0.5f, 0.56f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "each one is marked"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(37, show = setOf("five", "reg", "pts"), card = BoardCard("Schreiben, part two", "Liebe with Viele Grüße; Sehr geehrte with Mit freundlichen Grüßen.")),
                    Beat(39, card = BoardCard("Today", "One message in twenty minutes: five lines, thirty words, every point there.")),
                    Beat(40, card = BoardCard("Viele Grüße und bis bald!", "Best wishes, and see you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de11Reading() = PodcastBoard(
        "de11-reading",
        listOf(
            // 0: "Welcome back. Today is «Lesen», the reading paper. Twenty-five minutes..."
            Scene(
                0, "Lesen in three parts",
                DiagramSpec(
                    "The reading paper: twenty-five minutes, fifteen points, and three parts, each asking one question.",
                    nodes = listOf(
                        DNode("clock", "25 minutes, 15 points", 0.5f, 0.12f, w = 0.8f, kind = NodeKind.NOTE, tone = 1),
                        DNode("t1", "Teil 1", 0.18f, 0.56f, w = 0.3f, h = 0.36f, tone = 1, sub = "two texts: Richtig / Falsch"),
                        DNode("t2", "Teil 2", 0.5f, 0.56f, w = 0.3f, h = 0.36f, sub = "adverts: a or b?"),
                        DNode("t3", "Teil 3", 0.82f, 0.56f, w = 0.3f, h = 0.36f, tone = 2, sub = "signs: Richtig / Falsch"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("clock", "t1", "t2", "t3"), light = setOf("clock"), card = BoardCard("Lesen", "Twenty-five minutes, fifteen points, three parts.")),
                    Beat(1, light = setOf("t1", "t2", "t3"), card = BoardCard("The method", "Each part asks one question. Answer that one.")),
                ),
            ),
            // 2: "That's the first mistake. In «Teil eins», part one, you get two short ..."
            Scene(
                2, "Teil 1: statements first",
                DiagramSpec(
                    "Read the statements first; each one maps to one sentence, and day words and negations flip the answer.",
                    nodes = listOf(
                        DNode("st", "1. Read the statements", 0.5f, 0.12f, w = 0.74f, tone = 1),
                        DNode("tx", "2. Find the one sentence", 0.5f, 0.37f, w = 0.74f, tone = 1),
                        DNode("ans", "Richtig / Falsch", 0.5f, 0.62f, w = 0.56f),
                        DNode("flip", "Flip words", 0.5f, 0.86f, w = 0.84f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "nicht, kein, Samstag or Sonntag, ich or wir"),
                    ),
                    edges = listOf(
                        DEdge("st", "tx"),
                        DEdge("tx", "ans"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("st", "tx", "ans"), light = setOf("st"), pulse = "st" to "tx", card = BoardCard("Statements first", "Each statement maps to one sentence of the text.")),
                    Beat(3, light = setOf("tx"), card = BoardCard("Hallo Tom, am Samstag ziehen wir um!", "Hi Tom, we're moving on Saturday!", phrase = true)),
                    Beat(5, card = BoardCard("umziehen splits", "ziehen in second place, um at the very end.")),
                    Beat(6, light = setOf("ans"), pulse = "tx" to "ans", card = BoardCard("Usaid zieht am Sonntag um.", "Falsch: the email says Samstag.")),
                    Beat(7, show = setOf("flip"), light = setOf("flip"), card = BoardCard("Not mentioned at all?", "At A1, that is Falsch.")),
                    Beat(8, light = setOf("tx"), card = BoardCard("Bring bitte dein Auto mit.", "Can you help us? Please bring your car.", phrase = true)),
                    Beat(9, light = setOf("ans"), card = BoardCard("Every richtig", "needs a line in the text you can point at.")),
                ),
            ),
            // 10: "«Richtig», and you can point at the exact line. That's the test: every..."
            Scene(
                10, "Teil 2: all conditions",
                DiagramSpec(
                    "The right advert meets every condition; the decoy matches the topic but fails one detail.",
                    nodes = listOf(
                        DNode("sit", "Situation", 0.5f, 0.12f, w = 0.84f, h = 0.2f, kind = NodeKind.NOTE, tone = 1, sub = "Sonntag, Kinder, schwimmen"),
                        DNode("a", "Anzeige a", 0.25f, 0.45f, w = 0.44f, h = 0.2f, tone = 2, sub = "Schwimmbad Nord"),
                        DNode("b", "Anzeige b", 0.75f, 0.45f, w = 0.44f, h = 0.2f, sub = "Freizeitbad Süd"),
                        DNode("ax", "Sonntag geschlossen", 0.25f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "fails the day"),
                        DNode("bok", "täglich, Kinderbecken", 0.75f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "every condition holds"),
                    ),
                    edges = listOf(
                        DEdge("sit", "a"),
                        DEdge("sit", "b"),
                        DEdge("a", "ax", dashed = true),
                        DEdge("b", "bok"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("a", "b"), light = setOf("a", "b"), card = BoardCard("die Anzeige", "Five situations, two adverts each: pick the one that fits.")),
                    Beat(11, light = setOf("a", "b"), card = BoardCard("Welche Anzeige passt?", "Which advert fits?", phrase = true)),
                    Beat(13, card = BoardCard("The decoy", "Matches the topic, fails one detail: the day, the price, the city.")),
                    Beat(14, show = setOf("sit"), light = setOf("sit"), card = BoardCard("…mit Ihren Kindern schwimmen gehen.", "You want to go swimming with your children on Sunday.", phrase = true)),
                    Beat(15, light = setOf("sit"), card = BoardCard("Underline the conditions", "What, when, how much, where.")),
                    Beat(16, light = setOf("a"), pulse = "sit" to "a"),
                    Beat(17, show = setOf("ax"), light = setOf("ax"), pulse = "a" to "ax", card = BoardCard("Sonntag geschlossen.", "Closed on Sunday.", phrase = true)),
                    Beat(19, light = setOf("b"), pulse = "sit" to "b", card = BoardCard("One miss and it's out", "Right topic, wrong day.")),
                    Beat(20, show = setOf("bok"), light = setOf("bok"), pulse = "b" to "bok", card = BoardCard("Täglich neun bis zwanzig Uhr.", "Open daily 9 to 20. Children's pool. Family ticket 15 euros.", phrase = true)),
                ),
            ),
            // 22: "It's b: every condition holds. Now «Teil drei»: five signs and notices..."
            Scene(
                22, "Teil 3: signs",
                DiagramSpec(
                    "Learn the sign words; nur, nicht, kein, geschlossen and verboten each flip the answer.",
                    nodes = listOf(
                        DNode("ein", "der Eingang", 0.25f, 0.1f, w = 0.44f, h = 0.18f, tone = 1, sub = "entrance"),
                        DNode("aus", "der Ausgang", 0.75f, 0.1f, w = 0.44f, h = 0.18f, tone = 1, sub = "exit"),
                        DNode("dr", "drücken", 0.25f, 0.36f, w = 0.44f, h = 0.18f, tone = 1, sub = "push"),
                        DNode("zi", "ziehen", 0.75f, 0.36f, w = 0.44f, h = 0.18f, tone = 1, sub = "pull"),
                        DNode("verb", "Rauchen verboten", 0.25f, 0.62f, w = 0.44f, h = 0.18f, sub = "no smoking"),
                        DNode("erl", "Hunde erlaubt", 0.75f, 0.62f, w = 0.44f, h = 0.18f, sub = "dogs allowed"),
                        DNode("nur", "nur für Kunden", 0.25f, 0.88f, w = 0.44f, h = 0.18f, sub = "customers only"),
                        DNode("kas", "die Kasse", 0.75f, 0.88f, w = 0.44f, h = 0.18f, tone = 1, sub = "till"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, card = BoardCard("das Schild", "Five signs or notices, again Richtig or Falsch.")),
                    Beat(23, show = setOf("ein", "aus", "dr", "zi", "kas"), light = setOf("ein", "aus", "kas"), card = BoardCard("Eingang, Ausgang, Kasse.", "Entrance, exit, till. And: drücken, ziehen, push, pull.", phrase = true)),
                    Beat(24, light = setOf("dr", "zi")),
                    Beat(26, show = setOf("verb", "erl", "nur"), light = setOf("verb", "erl", "nur"), card = BoardCard("Rauchen verboten. Hunde erlaubt.", "No smoking. Dogs allowed. Parking for customers only.", phrase = true)),
                    Beat(28, light = setOf("nur"), card = BoardCard("nur flips it", "So do nicht, kein, geschlossen and verboten.")),
                ),
            ),
            // 29: "Öffnungszeiten: Samstag zehn bis vierzehn Uhr."
            Scene(
                29, "Numbers and rewordings",
                DiagramSpec(
                    "Check the numbers against the sign, and expect the statement to say the same thing in other words.",
                    nodes = listOf(
                        DNode("sign", "die Bibliothek", 0.25f, 0.14f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 1, sub = "Samstag 10 bis 14 Uhr"),
                        DNode("st1", "Samstag, 15 Uhr?", 0.75f, 0.14f, w = 0.44f, h = 0.22f, tone = 2, sub = "Falsch: it shuts at 14"),
                        DNode("rew", "Essen verboten", 0.25f, 0.5f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 1, sub = "on the sign"),
                        DNode("st2", "nicht essen", 0.75f, 0.5f, w = 0.44f, h = 0.22f, sub = "Richtig: a rewording"),
                        DNode("ausw", "der Bibliotheksausweis", 0.5f, 0.84f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "library card, to borrow books"),
                    ),
                    edges = listOf(
                        DEdge("sign", "st1"),
                        DEdge("rew", "st2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("sign"), light = setOf("sign"), card = BoardCard("Öffnungszeiten: Samstag 10–14 Uhr.", "Opening hours: Saturday ten to two.", phrase = true)),
                    Beat(30, show = setOf("st1"), light = setOf("st1"), pulse = "sign" to "st1"),
                    Beat(31, show = setOf("rew", "st2", "ausw"), light = setOf("st2"), pulse = "rew" to "st2", card = BoardCard("Same idea, other words", "Essen und Trinken verboten: man darf nicht essen.")),
                    Beat(32, light = setOf("st2"), card = BoardCard("…in der Bibliothek nicht essen.", "You may not eat in the library.", phrase = true)),
                ),
            ),
            // 33: "So what should I remember for the Goethe exam?"
            Scene(
                33, "For the Goethe exam",
                DiagramSpec(
                    "Lesen: statements first in Teil 1, every condition in Teil 2, sign words and numbers in Teil 3.",
                    nodes = listOf(
                        DNode("t1", "Teil 1", 0.26f, 0.18f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "statements first, flip words"),
                        DNode("t2", "Teil 2", 0.76f, 0.18f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "every condition must hold"),
                        DNode("t3", "Teil 3", 0.5f, 0.56f, w = 0.6f, h = 0.22f, kind = NodeKind.NOTE, sub = "signs, rewordings, numbers"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("t1", "t2", "t3"), card = BoardCard("Lesen", "The decoy fails one detail; nur, nicht, kein flip the answer.")),
                    Beat(36, card = BoardCard("Today", "A whole Lesen paper in 25 minutes, then a reason for each miss.")),
                    Beat(37, card = BoardCard("Viel Glück beim Lesen! Tschüss!", "Good luck with the reading! Bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de12Overview() = PodcastBoard(
        "de12-overview",
        listOf(
            // 0: "Welcome to module twelve, the last stop before the exam. Today is the ..."
            Scene(
                0, "Four papers",
                DiagramSpec(
                    "The Goethe A1 is four papers: three written in one sitting, and speaking in a group.",
                    nodes = listOf(
                        DNode("h", "Hören", 0.27f, 0.2f, w = 0.42f, h = 0.22f, tone = 1, sub = "about 20 minutes"),
                        DNode("l", "Lesen", 0.73f, 0.2f, w = 0.42f, h = 0.22f, tone = 1, sub = "25 minutes"),
                        DNode("s", "Schreiben", 0.27f, 0.5f, w = 0.42f, h = 0.22f, tone = 1, sub = "20 minutes"),
                        DNode("p", "Sprechen", 0.73f, 0.5f, w = 0.42f, h = 0.22f, sub = "15 minutes, in a group"),
                        DNode("c", "Prüfungsteilnehmer", 0.5f, 0.84f, w = 0.76f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "candidate"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, card = BoardCard("Module twelve", "The exam itself: four papers, 100 points, and what 60 looks like.")),
                    Beat(3, show = setOf("h", "l", "s", "p"), card = BoardCard("Hören, Lesen, Schreiben, Sprechen.", "Listening, reading, writing, speaking.", phrase = true)),
                    Beat(5, show = setOf("c"), light = setOf("p", "c"), card = BoardCard("Prüfungsteilnehmer", "Candidate. Speaking is up to four of you, with two examiners.")),
                ),
            ),
            // 6: "Of course it is. And the points?"
            Scene(
                6, "Sixty of a hundred",
                DiagramSpec(
                    "Each paper is scaled to 25. You need 60 in total, so a strong paper can carry a weak one.",
                    nodes = listOf(
                        DNode("a", "Hören", 0.14f, 0.32f, w = 0.22f, h = 0.32f, kind = NodeKind.CYLINDER, tone = 1, sub = "25"),
                        DNode("b", "Lesen", 0.38f, 0.32f, w = 0.22f, h = 0.32f, kind = NodeKind.CYLINDER, tone = 1, sub = "25"),
                        DNode("c", "Schreiben", 0.62f, 0.32f, w = 0.22f, h = 0.32f, kind = NodeKind.CYLINDER, tone = 1, sub = "25"),
                        DNode("d", "Sprechen", 0.86f, 0.32f, w = 0.22f, h = 0.32f, kind = NodeKind.CYLINDER, tone = 1, sub = "25"),
                        DNode("t", "10 + 20 + 18 + 14 = 62", 0.5f, 0.8f, w = 0.8f, h = 0.22f, kind = NodeKind.NOTE, sub = "a pass: no minimum per paper"),
                    ),
                    edges = listOf(
                        DEdge("a", "t"),
                        DEdge("b", "t"),
                        DEdge("c", "t"),
                        DEdge("d", "t"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("a", "b", "c", "d"), card = BoardCard("Four jars of 25", "Pass at 60. Gut from 80, sehr gut from 90.")),
                    Beat(9, show = setOf("t"), light = setOf("a", "t"), pulse = "a" to "t", card = BoardCard("No minimum per paper", "10 in Hören, and still 62 in total: a pass.")),
                    Beat(10, light = setOf("a"), card = BoardCard("Revise the weak one", "The total passes, so the weak paper earns more points per hour.")),
                ),
            ),
            // 11: "The opposite. The total is what passes, so an hour on your weak paper ..."
            Scene(
                11, "The answer sheet",
                DiagramSpec(
                    "Only der Antwortbogen is marked. In Hören, move your answers across in the pauses.",
                    nodes = listOf(
                        DNode("you", "You", 0.16f, 0.5f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
                        DNode("book", "question booklet", 0.58f, 0.22f, w = 0.5f, h = 0.22f, tone = 2, sub = "not marked"),
                        DNode("sheet", "der Antwortbogen", 0.58f, 0.74f, w = 0.5f, h = 0.22f, sub = "the only thing marked"),
                    ),
                    edges = listOf(
                        DEdge("book", "sheet", "in the pauses"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("you"), card = BoardCard("On the day", "Passport, phones off and away. Hören first, with a pencil.")),
                    Beat(12, show = setOf("sheet"), light = setOf("sheet"), card = BoardCard("Schreiben Sie auf den Antwortbogen.", "Write on the answer sheet.", phrase = true)),
                    Beat(14, show = setOf("book"), light = setOf("sheet"), pulse = "book" to "sheet", card = BoardCard("Only the sheet is marked", "The booklet is working paper. Transfer your answers in the pauses.")),
                ),
            ),
            // 15: "Got it. What else will the paper say to me?"
            Scene(
                15, "The instructions",
                DiagramSpec(
                    "Every part opens with a polite instruction: the task, an example, then you mark the answer.",
                    nodes = listOf(
                        DNode("a", "die Aufgabe", 0.5f, 0.12f, w = 0.56f, h = 0.2f, tone = 1, sub = "the task"),
                        DNode("b", "das Beispiel", 0.5f, 0.37f, w = 0.56f, h = 0.2f, tone = 1, sub = "the example"),
                        DNode("c", "die Lösung", 0.5f, 0.62f, w = 0.56f, h = 0.2f, sub = "the answer"),
                        DNode("d", "ankreuzen", 0.5f, 0.87f, w = 0.56f, h = 0.2f, sub = "tick: richtig or falsch"),
                    ),
                    edges = listOf(
                        DEdge("a", "b"),
                        DEdge("b", "c"),
                        DEdge("c", "d"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("a"), light = setOf("a"), card = BoardCard("Lesen Sie die Aufgabe.", "Read the task.", phrase = true)),
                    Beat(20, show = setOf("b"), light = setOf("b"), pulse = "a" to "b", card = BoardCard("Hier ist ein Beispiel.", "Here is an example.", phrase = true)),
                    Beat(23, show = setOf("c"), light = setOf("c"), pulse = "b" to "c", card = BoardCard("Markieren Sie die Lösung.", "Mark the answer.", phrase = true)),
                    Beat(26, show = setOf("d"), light = setOf("d"), pulse = "c" to "d", card = BoardCard("Kreuzen Sie an: Richtig oder Falsch.", "Tick: true or false.", phrase = true)),
                ),
            ),
            // 27: "What about «Schreiben»?"
            Scene(
                27, "Writing, listening, speaking",
                DiagramSpec(
                    "What each paper asks of you, and the certificate at the end.",
                    nodes = listOf(
                        DNode("s", "Schreiben", 0.5f, 0.12f, w = 0.7f, h = 0.2f, tone = 1, sub = "a form, then about 30 words"),
                        DNode("h", "Hören", 0.5f, 0.37f, w = 0.7f, h = 0.2f, tone = 1, sub = "most parts played twice"),
                        DNode("p", "Sprechen", 0.5f, 0.62f, w = 0.7f, h = 0.2f, tone = 1, sub = "a group, two examiners"),
                        DNode("z", "das Zertifikat", 0.5f, 0.87f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, sub = "by post, about a week later"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("s"), light = setOf("s"), card = BoardCard("Ergänzen Sie das Formular.", "Complete the form.", phrase = true)),
                    Beat(32, show = setOf("h"), light = setOf("h"), card = BoardCard("Sie hören den Text zweimal.", "You hear the text twice.", phrase = true)),
                    Beat(35, show = setOf("p"), light = setOf("p"), card = BoardCard("Die Prüferin stellt Fragen.", "The examiner asks questions.", phrase = true)),
                    Beat(37, show = setOf("z"), light = setOf("z"), card = BoardCard("Zertifikat", "The certificate, with your grade. Sixty is a pass.")),
                ),
            ),
            // 38: "So what should I remember for the Goethe exam?"
            Scene(
                38, "For the Goethe exam",
                DiagramSpec(
                    "The overview for the Goethe A1: the points, the answer sheet, and what to bring.",
                    nodes = listOf(
                        DNode("n1", "60 of 100", 0.5f, 0.16f, w = 0.8f, h = 0.22f, kind = NodeKind.NOTE, sub = "no minimum: revise the weakest"),
                        DNode("n2", "der Antwortbogen", 0.5f, 0.46f, w = 0.8f, h = 0.22f, kind = NodeKind.NOTE, sub = "move answers in the pauses"),
                        DNode("n3", "Passport and pencil", 0.5f, 0.76f, w = 0.8f, h = 0.22f, kind = NodeKind.NOTE, sub = "phones off and away"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(39, show = setOf("n1", "n2", "n3"), card = BoardCard("Four papers of 25", "Sixty in total passes. A strong paper carries a weak one.")),
                    Beat(41, card = BoardCard("Today", "Four papers and their minutes; what 60 means; your exam centre; the ID you need.")),
                    Beat(42, card = BoardCard("Viel Glück! Bis bald!", "Good luck! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de12Listening() = PodcastBoard(
        "de12-listening",
        listOf(
            // 0: "Welcome back. Today is «Hören», the listening paper, and its three par..."
            Scene(
                0, "Read first, then listen",
                DiagramSpec(
                    "Before each recording: read the question, pick the detail type, and listen for just that.",
                    nodes = listOf(
                        DNode("q", "Wann kommt der Zug?", 0.5f, 0.14f, w = 0.7f, h = 0.2f, tone = 1, sub = "read the question"),
                        DNode("d", "a time", 0.5f, 0.46f, w = 0.5f, h = 0.2f, sub = "the detail type"),
                        DNode("l", "Uhr, halb, Viertel", 0.5f, 0.8f, w = 0.7f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "and numbers: listen for these"),
                    ),
                    edges = listOf(
                        DEdge("q", "d"),
                        DEdge("d", "l"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, card = BoardCard("Hören", "Points are lost to panic, not to German. Today: a method.")),
                    Beat(3, show = setOf("q"), light = setOf("q"), card = BoardCard("Wann kommt der Zug?", "When does the train come?", phrase = true)),
                    Beat(5, show = setOf("d", "l"), light = setOf("l"), pulse = "d" to "l", card = BoardCard("Listen for one thing", "When: a time. Where: a place. How much: a price.")),
                ),
            ),
            // 6: "Okay. And the three parts?"
            Scene(
                6, "Teil eins: the correction",
                DiagramSpec(
                    "Speakers correct themselves on purpose. The first number is taken back; the second one is the answer.",
                    nodes = listOf(
                        DNode("t", "Teil eins", 0.5f, 0.14f, w = 0.7f, h = 0.2f, kind = NodeKind.PILL, tone = 1, sub = "six dialogues, played twice"),
                        DNode("a", "um zehn nach acht", 0.5f, 0.46f, w = 0.66f, h = 0.2f, tone = 2, sub = "taken back"),
                        DNode("b", "um halb neun", 0.5f, 0.8f, w = 0.66f, h = 0.2f, sub = "the answer"),
                    ),
                    edges = listOf(
                        DEdge("a", "b", "nein"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(7, show = setOf("t"), light = setOf("t"), card = BoardCard("First gist, then detail", "First hearing: a likely answer. Second hearing: check the detail.")),
                    Beat(8, show = setOf("a", "b"), light = setOf("b"), card = BoardCard("Nein, am Sonntag erst um halb neun.", "At ten past eight. No, on Sunday not until half past eight.", phrase = true)),
                    Beat(10, light = setOf("b"), pulse = "a" to "b", card = BoardCard("The second number counts", "Never tick the first number you hear. Wait for the correction.")),
                ),
            ),
            // 11: "Sneaky. What's the other trick?"
            Scene(
                11, "The word that pulls you",
                DiagramSpec(
                    "A wrong option's word is nearly always said. Hearing it does not make it the answer.",
                    nodes = listOf(
                        DNode("q", "When do they meet?", 0.5f, 0.16f, w = 0.7f, h = 0.2f, tone = 1),
                        DNode("sa", "Samstag", 0.26f, 0.62f, w = 0.42f, h = 0.22f, tone = 2, sub = "mentioned"),
                        DNode("so", "Sonntag um drei", 0.74f, 0.62f, w = 0.42f, h = 0.22f, sub = "the answer"),
                    ),
                    edges = listOf(
                        DEdge("q", "sa", dashed = true),
                        DEdge("q", "so"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("q")),
                    Beat(13, show = setOf("sa", "so"), light = setOf("so"), pulse = "q" to "so", card = BoardCard("Aber Sonntag um drei im Park?", "I can't on Saturday, I'm working. But Sunday at three in the park?", phrase = true)),
                    Beat(14, light = setOf("sa"), card = BoardCard("Heard is not right", "Ask: does it answer the question?")),
                ),
            ),
            // 15: "Right. Hearing a word doesn't make its option right. Ask yourself: doe..."
            Scene(
                15, "Teil zwei: once only",
                DiagramSpec(
                    "Announcements are heard once. nicht … sondern flips the answer: what comes after sondern is true.",
                    nodes = listOf(
                        DNode("t", "die Durchsage", 0.5f, 0.14f, w = 0.7f, h = 0.2f, kind = NodeKind.PILL, tone = 1, sub = "four, played once"),
                        DNode("n", "nicht Gleis zwei", 0.26f, 0.56f, w = 0.44f, h = 0.22f, tone = 2, sub = "ruled out"),
                        DNode("s", "sondern Gleis sechs", 0.74f, 0.56f, w = 0.44f, h = 0.22f, sub = "the answer"),
                        DNode("r", "richtig or falsch?", 0.5f, 0.86f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("n", "s"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("t", "r"), light = setOf("t"), card = BoardCard("Teil zwei", "Four public announcements, heard once only. Richtig or falsch.")),
                    Beat(17, show = setOf("n", "s"), card = BoardCard("nicht … sondern", "Not this, but that. What comes after sondern is the answer.")),
                    Beat(18, light = setOf("s"), pulse = "n" to "s", card = BoardCard("Sondern von Gleis sechs.", "The train leaves today not from platform two, but from platform six.", phrase = true)),
                    Beat(20, light = setOf("r"), card = BoardCard("Falsch", "Platform two was said, and the statement is still false.")),
                    Beat(21, light = setOf("s"), card = BoardCard("Nicht um neun, sondern um zehn.", "Not at nine, but at ten.", phrase = true)),
                ),
            ),
            // 22: "What other words do announcements love?"
            Scene(
                22, "Words announcements love",
                DiagramSpec(
                    "Two small words that decide a richtig or falsch: gelten and bereits.",
                    nodes = listOf(
                        DNode("g", "gelten", 0.26f, 0.3f, w = 0.42f, h = 0.24f, tone = 1, sub = "to be valid"),
                        DNode("b", "bereits", 0.74f, 0.3f, w = 0.42f, h = 0.24f, tone = 1, sub = "already"),
                        DNode("a", "das Angebot", 0.26f, 0.72f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "the offer"),
                        DNode("k", "die Kasse", 0.74f, 0.72f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "the ticket desk"),
                    ),
                    edges = listOf(
                        DEdge("g", "a", dashed = true),
                        DEdge("b", "k", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("g"), light = setOf("g")),
                    Beat(24, show = setOf("a"), light = setOf("g", "a"), card = BoardCard("Das Angebot gilt nur heute.", "The offer is valid today only.", phrase = true)),
                    Beat(26, show = setOf("b"), light = setOf("b")),
                    Beat(27, show = setOf("k"), light = setOf("b", "k"), card = BoardCard("Die Kasse ist bereits geschlossen.", "The ticket desk is already closed.", phrase = true)),
                ),
            ),
            // 28: "And «Teil drei»?"
            Scene(
                28, "Teil drei: messages",
                DiagramSpec(
                    "Five phone messages, played twice. Note the day or time the first time; check it the second.",
                    nodes = listOf(
                        DNode("t", "Teil drei", 0.5f, 0.12f, w = 0.7f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "five messages, twice"),
                        DNode("di", "Dienstag", 0.26f, 0.42f, w = 0.42f, h = 0.2f, tone = 2, sub = "nicht"),
                        DNode("mi", "Mittwoch um neun", 0.74f, 0.42f, w = 0.42f, h = 0.2f, sub = "sondern"),
                        DNode("z", "zurückrufen", 0.26f, 0.8f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "to call back"),
                        DNode("sp", "später", 0.74f, 0.8f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "later"),
                    ),
                    edges = listOf(
                        DEdge("di", "mi"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("t"), light = setOf("t")),
                    Beat(30, show = setOf("di", "mi"), light = setOf("mi"), pulse = "di" to "mi", card = BoardCard("Sondern am Mittwoch um neun.", "Your appointment is not on Tuesday at nine, but on Wednesday at nine.", phrase = true)),
                    Beat(33, show = setOf("z"), light = setOf("z"), card = BoardCard("Bitte rufen Sie zurück.", "Please call back.", phrase = true)),
                    Beat(36, show = setOf("sp"), light = setOf("sp"), card = BoardCard("Eine Stunde später, in Raum vier.", "The Monday course starts an hour later, at seven in the evening, in room four.", phrase = true)),
                ),
            ),
            // 37: "So what should I remember for the Goethe exam?"
            Scene(
                37, "For the Goethe exam",
                DiagramSpec(
                    "Hören for the Goethe A1: read first, the second thing wins, and transfer in the pauses.",
                    nodes = listOf(
                        DNode("n1", "Read first", 0.5f, 0.16f, w = 0.8f, h = 0.22f, kind = NodeKind.NOTE, sub = "a time, a place or a price?"),
                        DNode("n2", "The second thing wins", 0.5f, 0.46f, w = 0.8f, h = 0.22f, kind = NodeKind.NOTE, sub = "corrections, nicht … sondern"),
                        DNode("n3", "der Antwortbogen", 0.5f, 0.76f, w = 0.8f, h = 0.22f, kind = NodeKind.NOTE, sub = "transfer in the pauses"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("n1", "n2", "n3"), card = BoardCard("Three rules for Hören", "Read first. The second thing is the answer. Only the answer sheet is marked.")),
                    Beat(40, card = BoardCard("Today", "One paper, no pausing: read first, Teil zwei once, the correction, transfer.")),
                    Beat(41, card = BoardCard("Gut gemacht! Bis bald!", "Well done! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de12Speaking() = PodcastBoard(
        "de12-speaking",
        listOf(
            // 0: "Welcome back. Today is «Sprechen», the speaking paper. It's the one pe..."
            Scene(
                0, "Three fixed parts",
                DiagramSpec(
                    "Three parts, three fixed shapes. Part 1 is yours alone; parts 2 and 3 go round the group with cards.",
                    nodes = listOf(
                        DNode("p1", "sich vorstellen", 0.5f, 0.14f, w = 0.76f, h = 0.22f, tone = 1, sub = "part 1: introduce yourself"),
                        DNode("p2", "die Wortkarte", 0.5f, 0.46f, w = 0.76f, h = 0.22f, tone = 1, sub = "part 2: ask and answer"),
                        DNode("p3", "die Bildkarte", 0.5f, 0.78f, w = 0.76f, h = 0.22f, tone = 1, sub = "part 3: requests"),
                    ),
                    edges = listOf(
                        DEdge("p1", "p2"),
                        DEdge("p2", "p3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("p1", "p2", "p3"), card = BoardCard("Sprechen", "Fifteen minutes, up to four candidates, two examiners. The parts never change.")),
                ),
            ),
            // 2: "Fifteen minutes, in a group of up to four candidates, with two examine..."
            Scene(
                2, "Part one: the card",
                DiagramSpec(
                    "The card for part 1: seven words, one sentence each, then spelling or a number.",
                    nodes = listOf(
                        DNode("name", "Name", 0.26f, 0.1f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("alter", "Alter", 0.74f, 0.1f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("land", "Land", 0.26f, 0.32f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("wohnort", "Wohnort", 0.74f, 0.32f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("sprachen", "Sprachen", 0.26f, 0.54f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("beruf", "Beruf", 0.74f, 0.54f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                        DNode("hobby", "Hobby", 0.5f, 0.76f, w = 0.42f, kind = NodeKind.PILL, tone = 1),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, card = BoardCard("sich vorstellen", "Introduce yourself: one sentence for each word on the card.")),
                    Beat(3, light = setOf("name", "alter"), card = BoardCard("Ich heiße Sanya Khan.", "My name is Sanya Khan. I am twenty-eight years old.", phrase = true)),
                    Beat(6, light = setOf("land", "wohnort"), card = BoardCard("Ich komme aus Pakistan.", "I come from Pakistan and live in Lahore.", phrase = true)),
                    Beat(9, light = setOf("sprachen"), card = BoardCard("Ich spreche ein bisschen Deutsch.", "I speak Urdu, English and a little German.", phrase = true)),
                    Beat(12, light = setOf("beruf", "hobby"), card = BoardCard("Ich bin Lehrerin von Beruf.", "I am a teacher by profession. My hobby is reading.", phrase = true)),
                ),
            ),
            // 13: "Is that it for part one?"
            Scene(
                13, "Spell it, say it",
                DiagramSpec(
                    "Then spell a name or say a number. V and W swap round in German, and so do their names.",
                    nodes = listOf(
                        DNode("v", "V", 0.26f, 0.14f, w = 0.4f, h = 0.2f, sub = "Vau"),
                        DNode("w", "W", 0.74f, 0.14f, w = 0.4f, h = 0.2f, sub = "Weh"),
                        DNode("j", "J", 0.26f, 0.42f, w = 0.4f, h = 0.2f, tone = 1, sub = "Jott"),
                        DNode("z", "Z", 0.74f, 0.42f, w = 0.4f, h = 0.2f, tone = 1, sub = "Zett"),
                        DNode("k", "buchstabieren", 0.5f, 0.76f, w = 0.7f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "to spell: K, H, A, N"),
                    ),
                    edges = listOf(
                        DEdge("v", "w", "swap", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("k"), light = setOf("k")),
                    Beat(15, light = setOf("k"), card = BoardCard("Ich buchstabiere: K, H, A, N.", "I spell: K, H, A, N.", phrase = true)),
                    Beat(17, show = setOf("v", "w", "j", "z"), light = setOf("v", "w"), pulse = "v" to "w", card = BoardCard("V and W swap", "V is said Vau, W is said Weh. J is Jott, Z is Zett.")),
                    Beat(18, card = BoardCard("Meine Telefonnummer ist …", "My phone number is 0176 445566: digit by digit.", phrase = true)),
                ),
            ),
            // 19: "Okay, part two. What's on the cards there?"
            Scene(
                19, "Part two: the word card",
                DiagramSpec(
                    "Part 2: turn the card word into a full question, then answer in a full sentence.",
                    nodes = listOf(
                        DNode("card", "das Frühstück", 0.5f, 0.12f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "die Wortkarte"),
                        DNode("q", "a full question", 0.5f, 0.45f, w = 0.6f, h = 0.2f, sub = "for the next person"),
                        DNode("a", "a full answer", 0.5f, 0.78f, w = 0.6f, h = 0.2f, tone = 1, sub = "never one word"),
                    ),
                    edges = listOf(
                        DEdge("card", "q"),
                        DEdge("q", "a"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("card"), light = setOf("card")),
                    Beat(21, show = setOf("q"), light = setOf("q"), pulse = "card" to "q", card = BoardCard("Was isst du zum Frühstück?", "What do you eat for breakfast?", phrase = true)),
                    Beat(23, light = setOf("q"), card = BoardCard("Not just the card word", "Make a full question with it, and answer in a full sentence.")),
                    Beat(24, show = setOf("a"), light = setOf("a"), pulse = "q" to "a", card = BoardCard("Ich esse Brot mit Käse.", "I eat bread with cheese and drink tea.", phrase = true)),
                    Beat(27, light = setOf("q"), card = BoardCard("Wann stehst du auf?", "When do you get up? Card word: aufstehen. The auf goes to the end.", phrase = true)),
                    Beat(30, light = setOf("a"), card = BoardCard("Ich stehe um sechs Uhr auf.", "I get up at six and have breakfast at seven.", phrase = true)),
                ),
            ),
            // 31: "Then part three, with pictures?"
            Scene(
                31, "Part three: the picture card",
                DiagramSpec(
                    "Part 3: a picture card. Make a polite request with bitte, then react to the one you are given.",
                    nodes = listOf(
                        DNode("pic", "das Fenster", 0.5f, 0.12f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "die Bildkarte"),
                        DNode("du", "Kannst du bitte …?", 0.26f, 0.46f, w = 0.44f, h = 0.22f, tone = 1, sub = "someone in the group"),
                        DNode("sie", "Können Sie bitte …?", 0.74f, 0.46f, w = 0.44f, h = 0.22f, sub = "the examiner"),
                        DNode("re", "reagieren", 0.5f, 0.8f, w = 0.6f, h = 0.22f, sub = "Ja, gern. Hier, bitte."),
                    ),
                    edges = listOf(
                        DEdge("pic", "du"),
                        DEdge("pic", "sie"),
                        DEdge("du", "re", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("pic", "du", "sie"), light = setOf("du")),
                    Beat(33, light = setOf("du"), pulse = "pic" to "du", card = BoardCard("Kannst du bitte das Fenster öffnen?", "Can you open the window, please?", phrase = true)),
                    Beat(35, show = setOf("re"), light = setOf("re"), pulse = "du" to "re"),
                    Beat(36, light = setOf("re"), card = BoardCard("Ja, natürlich, gern.", "Yes, of course, gladly.", phrase = true)),
                    Beat(39, light = setOf("du", "re"), card = BoardCard("Hast du einen Stift für mich?", "Do you have a pen for me? No, unfortunately not. Please ask Tom.", phrase = true)),
                ),
            ),
            // 40: "And what if I just don't understand the examiner?"
            Scene(
                40, "Never freeze",
                DiagramSpec(
                    "Missed what the examiner said? Asking for a repeat costs nothing; silence costs the point.",
                    nodes = listOf(
                        DNode("s", "silence", 0.26f, 0.4f, w = 0.42f, h = 0.26f, tone = 2, sub = "costs the point"),
                        DNode("w", "Wie bitte?", 0.74f, 0.4f, w = 0.42f, h = 0.26f, sub = "costs nothing"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(41, light = setOf("w")),
                    Beat(42, light = setOf("w"), card = BoardCard("Wie bitte? Noch einmal, bitte.", "Pardon? Once more, please.", phrase = true)),
                ),
            ),
            // 43: "So what should I remember for the Goethe exam?"
            Scene(
                43, "For the Goethe exam",
                DiagramSpec(
                    "Sprechen for the Goethe A1: part 1 automatic, part 2 full sentences, part 3 bitte.",
                    nodes = listOf(
                        DNode("n1", "Part 1", 0.5f, 0.12f, w = 0.8f, h = 0.2f, kind = NodeKind.NOTE, sub = "seven sentences, spell, a number"),
                        DNode("n2", "Part 2", 0.5f, 0.37f, w = 0.8f, h = 0.2f, kind = NodeKind.NOTE, sub = "full question, full answer"),
                        DNode("n3", "Part 3", 0.5f, 0.62f, w = 0.8f, h = 0.2f, kind = NodeKind.NOTE, sub = "a request with bitte; Ja, gern"),
                        DNode("n4", "Wie bitte?", 0.5f, 0.87f, w = 0.8f, h = 0.2f, kind = NodeKind.NOTE, sub = "never silence"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(44, show = setOf("n1", "n2", "n3", "n4"), card = BoardCard("Rehearse until automatic", "The three parts never change, so every answer can be ready.")),
                    Beat(46, card = BoardCard("Today", "Record all three parts: seven card words, spelling, three questions, three requests.")),
                    Beat(47, card = BoardCard("Viel Glück in der Prüfung! Tschüss!", "Good luck in the exam! Bye!", phrase = true)),
                ),
            ),
        ),
    )

    private fun de12Words() = PodcastBoard(
        "de12-words",
        listOf(
            // 0: "Welcome to the last word episode. The official Goethe A1 word list, th..."
            Scene(
                0, "The rest of the list",
                DiagramSpec(
                    "Eleven themed modules plus this sweep. Together they cover the official A1 Wortliste.",
                    nodes = listOf(
                        DNode("a", "Modules 1 to 11", 0.27f, 0.3f, w = 0.44f, h = 0.24f, tone = 1, sub = "themed words"),
                        DNode("b", "this topic", 0.73f, 0.3f, w = 0.44f, h = 0.24f, sub = "the rest, about 400"),
                        DNode("c", "die Wortliste", 0.5f, 0.78f, w = 0.7f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "about 650 words"),
                    ),
                    edges = listOf(
                        DEdge("a", "c"),
                        DEdge("b", "c"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("a", "b", "c"), light = setOf("b"), pulse = "b" to "c", card = BoardCard("Wortliste", "The official Goethe A1 word list. Today: highlights of the rest, theme by theme.")),
                ),
            ),
            // 2: "With the smallest words, because they carry whole questions. A questio..."
            Scene(
                2, "Question words",
                DiagramSpec(
                    "A Fragewort carries a whole question. The wo family: wo, woher, wohin.",
                    nodes = listOf(
                        DNode("wer", "wer", 0.26f, 0.11f, w = 0.42f, h = 0.18f, tone = 1, sub = "who"),
                        DNode("was", "was", 0.26f, 0.36f, w = 0.42f, h = 0.18f, tone = 1, sub = "what"),
                        DNode("wann", "wann", 0.26f, 0.61f, w = 0.42f, h = 0.18f, tone = 1, sub = "when"),
                        DNode("wie", "wie / warum", 0.26f, 0.86f, w = 0.42f, h = 0.18f, tone = 1, sub = "how / why"),
                        DNode("wo", "wo", 0.74f, 0.11f, w = 0.42f, h = 0.18f, sub = "where"),
                        DNode("woher", "woher", 0.74f, 0.36f, w = 0.42f, h = 0.18f, sub = "where from"),
                        DNode("wohin", "wohin", 0.74f, 0.61f, w = 0.42f, h = 0.18f, sub = "where to"),
                    ),
                    edges = listOf(
                        DEdge("wo", "woher", dashed = true),
                        DEdge("woher", "wohin", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("wer", "was", "wo"), card = BoardCard("Fragewort", "Question word: wer, was, wo, wann, wie, warum.")),
                    Beat(3, light = setOf("wer", "was", "wo"), card = BoardCard("Wer ist das? Was machst du?", "Who is that? What are you doing? Where do you live?", phrase = true)),
                    Beat(5, show = setOf("woher", "wohin"), light = setOf("wo", "woher", "wohin"), pulse = "wo" to "woher"),
                    Beat(6, light = setOf("woher", "wohin"), card = BoardCard("Woher kommst du? Wohin fährst du?", "Where do you come from? Where are you going?", phrase = true)),
                    Beat(8, show = setOf("wann", "wie"), light = setOf("wann", "wie")),
                    Beat(9, light = setOf("wann", "wie"), card = BoardCard("Wann beginnt der Kurs?", "When does the course start? Why are you learning German?", phrase = true)),
                ),
            ),
            // 10: "Okay, small words done. What's next?"
            Scene(
                10, "die Farbe",
                DiagramSpec(
                    "welcher, welche, welches change with the noun: die Farbe, so welche Farbe.",
                    nodes = listOf(
                        DNode("f", "die Farbe", 0.5f, 0.14f, w = 0.6f, h = 0.2f, sub = "colour"),
                        DNode("w", "welcher, welche, welches", 0.5f, 0.46f, w = 0.86f, h = 0.22f, tone = 1, sub = "which: changes with the noun"),
                        DNode("c", "rot, blau, grün, gelb", 0.5f, 0.8f, w = 0.86f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "schwarz, weiß"),
                    ),
                    edges = listOf(
                        DEdge("f", "w"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("f"), light = setOf("f")),
                    Beat(12, light = setOf("f"), card = BoardCard("Welche Farbe hat das Auto?", "What colour is the car?", phrase = true)),
                    Beat(14, show = setOf("w", "c"), light = setOf("w"), pulse = "f" to "w", card = BoardCard("die Farbe: welche Farbe", "welcher, welche, welches follow der, die, das.")),
                    Beat(15, light = setOf("c"), card = BoardCard("Der Apfel ist rot.", "The apple is red. The sky is blue.", phrase = true)),
                ),
            ),
            // 16: "Easy enough. What else?"
            Scene(
                16, "Opposites in pairs",
                DiagramSpec(
                    "The list loves opposites, so learn them in pairs. zu means closed, and also too.",
                    nodes = listOf(
                        DNode("r", "richtig / falsch", 0.5f, 0.16f, w = 0.76f, h = 0.22f, tone = 1, sub = "right / wrong"),
                        DNode("v", "voll / leer", 0.5f, 0.46f, w = 0.76f, h = 0.22f, tone = 1, sub = "full / empty"),
                        DNode("o", "offen / zu", 0.5f, 0.76f, w = 0.76f, h = 0.22f, tone = 1, sub = "open / closed"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("r", "v", "o")),
                    Beat(18, light = setOf("v"), card = BoardCard("Der Bus ist voll.", "The bus is full. Die Flasche ist leer: the bottle is empty.", phrase = true)),
                    Beat(21, light = setOf("o"), card = BoardCard("Die Tür ist zu. Es ist zu teuer.", "The door is closed. It is too expensive.", phrase = true)),
                ),
            ),
            // 22: "One little word, three jobs. What about things? Clothes and stuff?"
            Scene(
                22, "Nouns with their article",
                DiagramSpec(
                    "Learn every noun with its article and plural: the exam checks both.",
                    nodes = listOf(
                        DNode("hm", "der", 0.17f, 0.09f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("hf", "die", 0.5f, 0.09f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("hn", "das", 0.83f, 0.09f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("mantel", "der Mantel", 0.17f, 0.32f, w = 0.3f, h = 0.18f, tone = 1, sub = "coat"),
                        DNode("schl", "der Schlüssel", 0.17f, 0.56f, w = 0.3f, h = 0.18f, tone = 1, sub = "key"),
                        DNode("hose", "die Hose", 0.5f, 0.32f, w = 0.3f, h = 0.18f, tone = 1, sub = "trousers"),
                        DNode("jacke", "die Jacke", 0.5f, 0.56f, w = 0.3f, h = 0.18f, tone = 1, sub = "jacket"),
                        DNode("brille", "die Brille", 0.5f, 0.8f, w = 0.3f, h = 0.18f, tone = 1, sub = "glasses"),
                        DNode("hemd", "das Hemd", 0.83f, 0.32f, w = 0.3f, h = 0.18f, tone = 1, sub = "shirt"),
                        DNode("handy", "das Handy", 0.83f, 0.56f, w = 0.3f, h = 0.18f, tone = 1, sub = "mobile"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("hm", "hf", "hn", "mantel", "hose", "jacke", "hemd"), light = setOf("hm", "hf", "hn"), card = BoardCard("Say the article", "Article and plural, out loud, as if they are part of the word.")),
                    Beat(24, light = setOf("hemd", "hose"), card = BoardCard("Das Hemd ist weiß.", "The shirt is white. The trousers are too long.", phrase = true)),
                    Beat(26, show = setOf("schl", "handy", "brille"), light = setOf("schl", "handy", "brille")),
                    Beat(27, light = setOf("schl", "brille"), card = BoardCard("Wo ist mein Schlüssel?", "Where is my key? Usaid wears glasses: eine Brille.", phrase = true)),
                ),
            ),
            // 28: "What about people? Forms always ask about people."
            Scene(
                28, "Forms and family",
                DiagramSpec(
                    "The words a form uses about people, and the family words the list adds.",
                    nodes = listOf(
                        DNode("fs", "der Familienstand", 0.5f, 0.14f, w = 0.8f, h = 0.2f, tone = 1, sub = "marital status"),
                        DNode("mw", "männlich / weiblich", 0.5f, 0.44f, w = 0.8f, h = 0.2f, tone = 1, sub = "male / female: tick one"),
                        DNode("ef", "die Ehefrau", 0.26f, 0.78f, w = 0.42f, h = 0.2f, sub = "wife"),
                        DNode("em", "der Ehemann", 0.74f, 0.78f, w = 0.42f, h = 0.2f, sub = "husband"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("fs", "mw", "ef", "em"), light = setOf("fs", "mw")),
                    Beat(30, light = setOf("ef"), card = BoardCard("Das ist meine Ehefrau Sanya.", "This is my wife Sanya.", phrase = true)),
                ),
            ),
            // 31: "And then the verbs?"
            Scene(
                31, "Verbs and phrases",
                DiagramSpec(
                    "Verbs the exam keeps using, and phrases for the first week. wissen is a fact; kennen is a person or place.",
                    nodes = listOf(
                        DNode("wh", "wiederholen", 0.5f, 0.11f, w = 0.7f, h = 0.18f, sub = "to repeat"),
                        DNode("wi", "wissen", 0.26f, 0.37f, w = 0.42f, h = 0.2f, tone = 1, sub = "a fact"),
                        DNode("ke", "kennen", 0.74f, 0.37f, w = 0.42f, h = 0.2f, tone = 1, sub = "a person, a place"),
                        DNode("be", "bedeuten", 0.5f, 0.63f, w = 0.7f, h = 0.18f, tone = 1, sub = "to mean"),
                        DNode("ph", "Alles in Ordnung?", 0.5f, 0.88f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "the first week"),
                    ),
                    edges = listOf(
                        DEdge("wi", "ke", "or"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("wh"), light = setOf("wh")),
                    Beat(33, light = setOf("wh"), card = BoardCard("Können Sie das bitte wiederholen?", "Could you repeat that, please?", phrase = true)),
                    Beat(35, show = setOf("wi", "ke"), light = setOf("wi", "ke"), pulse = "wi" to "ke"),
                    Beat(36, light = setOf("wi", "ke"), card = BoardCard("Ich weiß es nicht.", "I don't know. Kennst du Frau Braun? Do you know Mrs Braun?", phrase = true)),
                    Beat(38, show = setOf("be"), light = setOf("be")),
                    Beat(39, light = setOf("be"), card = BoardCard("Was bedeutet dieses Wort?", "What does this word mean?", phrase = true)),
                    Beat(41, show = setOf("ph"), light = setOf("ph")),
                    Beat(42, light = setOf("ph"), card = BoardCard("Alles in Ordnung? Viel Glück!", "Is everything all right? Good luck! Welcome: Herzlich willkommen!", phrase = true)),
                ),
            ),
            // 43: "So what should I remember for the Goethe exam?"
            Scene(
                43, "For the Goethe exam",
                DiagramSpec(
                    "The word list for the Goethe A1: question words, articles, and asking again.",
                    nodes = listOf(
                        DNode("n1", "wer, wo, wann, welche", 0.5f, 0.16f, w = 0.84f, h = 0.22f, kind = NodeKind.NOTE, sub = "Hören, Lesen: which detail"),
                        DNode("n2", "der, die, das", 0.5f, 0.46f, w = 0.84f, h = 0.22f, kind = NodeKind.NOTE, sub = "Schreiben: article and plural"),
                        DNode("n3", "Wie bitte?", 0.5f, 0.76f, w = 0.84f, h = 0.22f, kind = NodeKind.NOTE, sub = "Sprechen: ask them to repeat"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(44, show = setOf("n1", "n2", "n3"), card = BoardCard("Small words, big points", "Question words point to the detail; every noun comes with its article.")),
                    Beat(46, card = BoardCard("Today", "Every due card, aloud, honestly marked, with the article for every noun.")),
                    Beat(47, card = BoardCard("Viel Spaß! Bis bald!", "Have fun! See you soon!", phrase = true)),
                ),
            ),
        ),
    )

    val all: List<PodcastBoard> by lazy { listOf(de1Words(), de1Grammar(), de1Dialogue(), de1Practice(), de2Words(), de2Grammar(), de2Dialogue(), de2Practice(), de3Words(), de3Grammar(), de3Dialogue(), de3Practice(), de4Words(), de4Grammar(), de4Dialogue(), de4Practice(), de5Words(), de5Grammar(), de5Dialogue(), de5Practice(), de6Words(), de6Grammar(), de6Dialogue(), de6Practice(), de7Words(), de7Grammar(), de7Dialogue(), de7Practice(), de8Words(), de8Grammar(), de8Dialogue(), de8Practice(), de9Words(), de9Grammar(), de9Dialogue(), de9Practice(), de10Words(), de10Grammar(), de10Dialogue(), de10Practice(), de11Forms(), de11Messages(), de11Reading(), de12Overview(), de12Listening(), de12Speaking(), de12Words()) }
}
