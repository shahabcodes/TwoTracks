package com.shahaabapps.core.content

/** Module 1: Sounds and greetings. Alphabet, pronunciation, hello and goodbye, introducing yourself. */
internal object GermanA1M1 {
    private val refs = GermanA1.commonRefs

    private val words = Topic(
        id = "de1-words", title = "Words: hello, goodbye and the basics", minutes = 15, references = refs,
        blocks = listOf(
            B.plain("""
                A1 is about surviving everyday situations. This first set of words lets you greet someone, be polite,
                and say yes, no and thank you. Tap a card to see the English, and the speaker to hear it.
            """),
            B.vocab(
                B.w("hallo", "Hallo", "hello", "Hallo, ich bin Usaid.", "Hello, I am Usaid."),
                B.w("guten-morgen", "Guten Morgen", "good morning", "Guten Morgen, Frau Khan!", "Good morning, Mrs Khan!"),
                B.w("guten-tag", "Guten Tag", "good day (formal hello)", "Guten Tag, wie geht es Ihnen?", "Good day, how are you?"),
                B.w("guten-abend", "Guten Abend", "good evening", "Guten Abend zusammen.", "Good evening, everyone."),
                B.w("gute-nacht", "Gute Nacht", "good night", "Gute Nacht und schlaf gut.", "Good night and sleep well."),
                B.w("tschuess", "Tschüss", "bye", "Tschüss, bis morgen!", "Bye, see you tomorrow!"),
                B.w("auf-wiedersehen", "Auf Wiedersehen", "goodbye (formal)", "Auf Wiedersehen, Herr Müller.", "Goodbye, Mr Müller."),
                B.w("bitte", "bitte", "please; you're welcome", "Ein Wasser, bitte.", "A water, please."),
                B.w("danke", "danke", "thank you", "Danke schön!", "Thank you very much!"),
                B.w("ja", "ja", "yes", "Ja, gern.", "Yes, gladly."),
                B.w("nein", "nein", "no", "Nein, danke.", "No, thank you."),
                B.w("entschuldigung", "Entschuldigung", "excuse me; sorry", "Entschuldigung, wo ist die Toilette?", "Excuse me, where is the toilet?"),
                B.w("wie-gehts", "Wie geht's?", "how are you? (informal)", "Hallo Sanya, wie geht's?", "Hi Sanya, how are you?"),
                B.w("gut", "gut", "good, well", "Mir geht es gut.", "I am well."),
                B.w("bis-bald", "Bis bald", "see you soon", "Bis bald, Sanya!", "See you soon, Sanya!"),
            ),
            B.diagram(
                caption = "Formal or informal: the choice decides your greeting, your goodbye and later your verb forms.",
                nodes = listOf(
                    DNode("you", "You", 0.5f, 0.14f, w = 0.2f, h = 0.16f, kind = NodeKind.USER),
                    DNode("du", "du (informal)", 0.25f, 0.55f, w = 0.36f, h = 0.2f, tone = 1, sub = "friends, family, children"),
                    DNode("sie", "Sie (formal)", 0.75f, 0.55f, w = 0.36f, h = 0.2f, sub = "strangers, officials, work"),
                    DNode("n1", "Hallo · Tschüss", 0.25f, 0.88f, w = 0.36f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                    DNode("n2", "Guten Tag · Auf Wiedersehen", 0.75f, 0.88f, w = 0.4f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("you", "du"), DEdge("you", "sie"), DEdge("du", "n1", dashed = true), DEdge("sie", "n2", dashed = true)),
                aspect = 0.7f,
            ),
            B.remember("""
                Germans switch between du and Sie. At the Ausländerbehörde, in a shop, or with a new colleague, start with
                Sie and Guten Tag. Friends and your partner get du and Hallo.
            """),
            B.check("de1-words-k1", "de1-words", "You enter a bank in Berlin. Which greeting fits?",
                listOf("Hallo!", "Guten Tag!", "Tschüss!"), 1, "A bank is formal: Guten Tag. Hallo is for friends and Tschüss is a goodbye."),
            B.glossary("Wortschatz" to "Vocabulary, literally a word treasure.", "formell" to "Formal, with Sie.", "informell" to "Informal, with du."),
            B.recap("Guten Tag and Auf Wiedersehen for Sie; Hallo and Tschüss for du.", "bitte means please and you're welcome.", "Every word here is now in your deck."),
        ),
    )

    private val grammar = Topic(
        id = "de1-grammar", title = "Grammar: sounds, the alphabet and I am", minutes = 18, references = refs + B.ref("Goethe: German alphabet and pronunciation practice", GermanA1.GOETHE + "en/spr/ueb.html"),
        blocks = listOf(
            B.plain("""
                German spelling is regular: once you know the sounds, you can read any word aloud. This topic gives you
                the sounds that differ from English, the alphabet for spelling your name, and the verb sein (to be).
            """),
            B.h("Sounds that trip English speakers"),
            B.table(
                listOf("Letters", "Sound", "Example"),
                listOf("w", "like English v", "Wasser, wie"),
                listOf("v", "like English f", "Vater, vier"),
                listOf("z", "ts", "zehn, Zeit"),
                listOf("j", "like English y", "ja, Jahr"),
                listOf("ei", "like English eye", "nein, mein"),
                listOf("ie", "long ee", "Sie, wie"),
                listOf("ch after a, o, u", "throat sound", "Buch, auch"),
                listOf("ch after e, i", "soft hiss", "ich, nicht"),
                listOf("ä ö ü", "a with lips rounded", "Mädchen, schön, Tschüss"),
                listOf("ß", "sharp ss", "Straße, heißen"),
            ),
            B.tip("ei and ie: say the second letter's English name. ei is like 'eye' (i), ie is like 'ee' (e). Nein is 'nine', Sie is 'zee'."),
            B.h("The alphabet, for spelling your name"),
            B.p("In the exam and at any counter you will be asked 'Wie schreibt man das?' (how do you spell that?). Letters are said as in the table; a double letter is 'Doppel'."),
            B.code("text", """
                a ah   b beh  c tseh  d deh   e eh    f eff  g geh   h hah
                i ee   j yot  k kah   l ell   m emm   n enn  o oh    p peh
                q koo  r err  s ess   t teh   u oo    v fow  w veh   x iks
                y üpsilon      z tset  ä äh   ö öh    ü üh   ß ess-tset
                Usaid:  oo – ess – ah – ee – deh      Sanya: ess – ah – enn – üpsilon – ah
            """),
            B.h("sein: to be"),
            B.table(
                listOf("Person", "sein", "Example"),
                listOf("ich", "bin", "Ich bin Usaid."),
                listOf("du", "bist", "Du bist nett."),
                listOf("er / sie / es", "ist", "Sie ist Lehrerin."),
                listOf("wir", "sind", "Wir sind aus Indien und Pakistan."),
                listOf("ihr", "seid", "Ihr seid Freunde."),
                listOf("sie / Sie", "sind", "Sie sind Herr Müller?"),
            ),
            B.diagram(
                caption = "The word order in a plain statement: the verb is always in second position.",
                nodes = listOf(
                    DNode("p1", "Position 1", 0.17f, 0.3f, w = 0.3f, h = 0.2f, tone = 2, sub = "Ich"),
                    DNode("p2", "Position 2: verb", 0.5f, 0.3f, w = 0.3f, h = 0.2f, sub = "bin"),
                    DNode("p3", "Rest", 0.83f, 0.3f, w = 0.3f, h = 0.2f, tone = 2, sub = "Usaid"),
                    DNode("q1", "Position 1", 0.17f, 0.75f, w = 0.3f, h = 0.2f, tone = 2, sub = "Heute"),
                    DNode("q2", "verb", 0.5f, 0.75f, w = 0.3f, h = 0.2f, sub = "bin"),
                    DNode("q3", "Rest", 0.83f, 0.75f, w = 0.3f, h = 0.2f, tone = 2, sub = "ich in Doha"),
                ),
                edges = listOf(DEdge("p1", "p2"), DEdge("p2", "p3"), DEdge("q1", "q2"), DEdge("q2", "q3")),
                aspect = 0.5f,
            ),
            B.remember("Verb second. Whatever you put first (Ich, Heute, Morgen), the conjugated verb comes next: Heute bin ich in Doha."),
            B.gap("de1-g-gap1", "Fill the gap with the right form of sein.", "Wir ___ aus Pakistan.", "sind"),
            B.gap("de1-g-gap2", "Fill the gap with the right form of sein.", "Du ___ müde.", "bist"),
            B.reorder("de1-g-re1", "Put the words in order (verb second).", "Heute bin ich müde"),
            B.mistakes(
                "Saying 'Ich bin gut' for 'I am well'. It means 'I am good (at it)'. Use 'Mir geht es gut'.",
                "Reading w as English w. Wasser is 'vasser'.",
                "Putting the verb third: 'Heute ich bin müde' is wrong; the verb must be second.",
            ),
            B.glossary("sein" to "The verb 'to be'.", "konjugieren" to "To change a verb for the person: ich bin, du bist.", "Umlaut" to "The two dots on ä, ö, ü that change the sound."),
            B.recap("w sounds like v, ei like eye, ie like ee.", "sein: bin, bist, ist, sind, seid, sind.", "The verb is always second in a statement."),
        ),
    )

    private val dialogue = Topic(
        id = "de1-dialogue", title = "Dialogue: meeting someone for the first time", minutes = 14, references = refs,
        blocks = listOf(
            B.plain("Listen to each line, then read it aloud. Tap a line to see the English. This is the exact shape of the exam's Sprechen part 1: introduce yourself."),
            B.dialogue("At the language school",
                B.line("Lehrerin", "Guten Tag! Ich bin Frau Weber. Wie heißen Sie?", "Good day! I am Mrs Weber. What is your name?"),
                B.line("Usaid", "Guten Tag. Ich heiße Usaid Shahab.", "Good day. My name is Usaid Shahab."),
                B.line("Lehrerin", "Woher kommen Sie, Herr Shahab?", "Where are you from, Mr Shahab?"),
                B.line("Usaid", "Ich komme aus Indien, aber ich wohne in Doha.", "I come from India, but I live in Doha."),
                B.line("Lehrerin", "Und das ist …?", "And this is …?"),
                B.line("Usaid", "Das ist Sanya. Sie kommt aus Pakistan. Sie ist Lehrerin.", "This is Sanya. She comes from Pakistan. She is a teacher."),
                B.line("Lehrerin", "Freut mich! Willkommen.", "Pleased to meet you! Welcome."),
                B.line("Sanya", "Danke. Wie bitte, wie heißen Sie?", "Thank you. Sorry, what is your name?"),
                B.line("Lehrerin", "Weber. W-E-B-E-R.", "Weber. W-E-B-E-R."),
            ),
            B.h("The sentences you will reuse"),
            B.bullets(
                "Ich heiße … / Ich bin … (my name is)",
                "Ich komme aus … (I come from) · Ich wohne in … (I live in)",
                "Ich bin … von Beruf (I am a … by profession): Softwareentwickler, Lehrerin",
                "Wie bitte? (sorry, say again) · Wie schreibt man das? (how do you spell that?)",
            ),
            B.diagram(
                caption = "The Sprechen part 1 card: say one thing per line and you have passed the first part.",
                nodes = listOf(
                    DNode("n", "Name", 0.2f, 0.25f, w = 0.3f, h = 0.18f, sub = "Ich heiße …"),
                    DNode("a", "Alter", 0.5f, 0.25f, w = 0.3f, h = 0.18f, sub = "Ich bin 28 Jahre alt."),
                    DNode("l", "Land", 0.8f, 0.25f, w = 0.3f, h = 0.18f, sub = "Ich komme aus …"),
                    DNode("w", "Wohnort", 0.2f, 0.72f, w = 0.3f, h = 0.18f, sub = "Ich wohne in …"),
                    DNode("s", "Sprachen", 0.5f, 0.72f, w = 0.3f, h = 0.18f, sub = "Ich spreche …"),
                    DNode("b", "Beruf", 0.8f, 0.72f, w = 0.3f, h = 0.18f, sub = "Ich bin … von Beruf."),
                ),
                aspect = 0.55f,
            ),
            B.check("de1-dialogue-k1", "de1-dialogue", "Someone says a name too fast. What do you say?",
                listOf("Tschüss!", "Wie bitte?", "Danke schön!"), 1, "Wie bitte? asks them to repeat. It is polite in every situation."),
            B.glossary("Freut mich" to "Pleased to meet you.", "von Beruf" to "By profession.", "Willkommen" to "Welcome."),
            B.recap("Ich heiße, ich komme aus, ich wohne in: the three sentences of every introduction.", "Wie bitte? gets a repeat.", "Spell your name with the German letter names."),
        ),
    )

    private val practice = Topic(
        id = "de1-practice", title = "Practice: introduce yourself in writing and speech", minutes = 16, references = refs,
        blocks = listOf(
            B.plain("The exam's Schreiben part 1 is a form; part 2 is a short message. Sprechen part 1 is the introduction. Practise all three here."),
            B.diagram(
                caption = "The four exam papers. This topic rehearses the three in colour.",
                nodes = listOf(DNode("p0", "Hören", 0.14f, 0.5f, w = 0.22f, h = 0.34f, tone = 2, sub = "20 min"), DNode("p1", "Lesen", 0.38f, 0.5f, w = 0.22f, h = 0.34f, tone = 0, sub = "25 min"), DNode("p2", "Schreiben", 0.62f, 0.5f, w = 0.22f, h = 0.34f, tone = 0, sub = "20 min"), DNode("p3", "Sprechen", 0.86f, 0.5f, w = 0.22f, h = 0.34f, tone = 0, sub = "15 min")),
                aspect = 0.4f,
            ),
            B.h("Reading: a form"),
            B.table(
                listOf("Feld", "Antwort"),
                listOf("Familienname", "Shahab"),
                listOf("Vorname", "Usaid"),
                listOf("Land", "Indien"),
                listOf("Wohnort", "Doha"),
                listOf("Alter", "28"),
                listOf("Beruf", "Softwareentwickler"),
                listOf("Sprachen", "Englisch, Hindi, Deutsch (A1)"),
            ),
            B.check("de1-practice-k1", "de1-practice", "Which field asks for the city you live in?",
                listOf("Land", "Wohnort", "Vorname"), 1, "Wohnort is the place of residence. Land is the country, Vorname the first name."),
            B.match("de1-p-match1", "Match the question to the answer.",
                "Wie heißen Sie?" to "Ich heiße Sanya.", "Woher kommen Sie?" to "Aus Pakistan.", "Wo wohnen Sie?" to "In Karachi.", "Was sind Sie von Beruf?" to "Ich bin Lehrerin."),
            B.gap("de1-p-gap1", "Complete the sentence.", "Ich ___ in Doha.", "wohne"),
            B.gap("de1-p-gap2", "Complete the sentence.", "Sanya ___ aus Pakistan.", "kommt"),
            B.writing("de1-p-write", "Write 4 short sentences to introduce yourself to a new neighbour: name, country, city, job.",
                """
                Hallo! Ich heiße Usaid Shahab. Ich komme aus Indien. Ich wohne in Doha. Ich bin Softwareentwickler von Beruf. Viele Grüße, Usaid
                """,
                "Name", "Country (aus …)", "City (in …)", "Job", minWords = 15),
            B.steps("Speak it: thirty seconds of introduction",
                "Say your name: Ich heiße …" to "One sentence, slow and clear.",
                "Say where you are from and where you live." to "Ich komme aus … Ich wohne in …",
                "Say your job and your languages." to "Ich bin … von Beruf. Ich spreche …",
                "Spell your surname with the German letters." to "Ess – hah – ah – hah – ah – beh.",
            ),
            B.tryIt("Try it today", "Record yourself on your phone reading the four sentences. Listen once and mark what you would fix.",
                "Recorded the introduction", "w sounded like v", "Verb came second in every sentence", "Spelled the surname",
            ),
            B.glossary("Familienname" to "Surname.", "Vorname" to "First name.", "Wohnort" to "Place of residence."),
            B.recap("A form asks Name, Land, Wohnort, Alter, Beruf.", "Four sentences make a complete introduction.", "Spell your name out loud until it is automatic."),
        ),
    )

    val module = Module(
        id = "de-m1", number = 1, title = "Sounds and greetings", summary = "The alphabet, pronunciation, hello and goodbye, introducing yourself.",
        examWeight = "A1", componentId = "speaking", topics = listOf(words, grammar, dialogue, practice),
        quiz = Quiz(
            id = "de-m1-test", title = "Module 1 test", passPercent = 60,
            questions = listOf(
                B.q("de1-q1", "de1-words", "Hören: you hear 'Guten Abend, Frau Khan.' What time of day is it?", listOf("Morning", "Evening", "Night"), setOf(1), "Guten Abend is the evening greeting; Guten Morgen is morning and Gute Nacht is said when leaving at night."),
                B.q("de1-q2", "de1-words", "Which goodbye is formal?", listOf("Tschüss", "Bis bald", "Auf Wiedersehen"), setOf(2), "Auf Wiedersehen goes with Sie. Tschüss and Bis bald are informal."),
                B.q("de1-q3", "de1-grammar", "Lesen: 'Wir ___ aus Indien.' Choose the verb.", listOf("bin", "sind", "ist"), setOf(1), "wir always takes sind: Wir sind aus Indien."),
                B.q("de1-q4", "de1-grammar", "Which sentence has the verb in the right place?", listOf("Heute ich bin in Doha.", "Heute bin ich in Doha.", "Ich heute bin in Doha."), setOf(1), "The conjugated verb is second: Heute bin ich in Doha."),
                B.q("de1-q5", "de1-grammar", "How is 'w' pronounced in Wasser?", listOf("Like English w", "Like English v", "Like English f"), setOf(1), "German w sounds like English v. German v sounds like f."),
                B.q("de1-q6", "de1-dialogue", "Hören: 'Woher kommen Sie?' What is being asked?", listOf("Where do you live?", "Where are you from?", "What is your job?"), setOf(1), "Woher = from where. Wo wohnen Sie? asks where you live."),
                B.q("de1-q7", "de1-dialogue", "You did not catch a word. What do you say?", listOf("Wie bitte?", "Gute Nacht.", "Freut mich."), setOf(0), "Wie bitte? asks for a repeat."),
                B.q("de1-q8", "de1-practice", "Lesen: on a form, 'Wohnort' means", listOf("surname", "country", "place of residence"), setOf(2), "Wohnort is where you live; Land is the country."),
                B.q("de1-q9", "de1-practice", "Schreiben: which sentence says your profession?", listOf("Ich bin 28 Jahre alt.", "Ich bin Lehrerin von Beruf.", "Ich wohne in Karachi."), setOf(1), "von Beruf marks the profession."),
                B.q("de1-q10", "de1-words", "Someone hands you a coffee. You say:", listOf("Bitte.", "Danke.", "Nein."), setOf(1), "Danke is thank you. Bitte would be the reply to it."),
                B.q("de1-q11", "de1-grammar", "Spell 'Sie' with German letter names.", listOf("ess – ee – eh", "ess – ie – eh", "zet – ee – eh"), setOf(0), "S is ess, i is ee, e is eh."),
                B.q("de1-q12", "de1-dialogue", "Sprechen: the introduction card asks for Alter. You say:", listOf("Ich bin 28 Jahre alt.", "Ich komme aus Indien.", "Ich spreche Englisch."), setOf(0), "Alter means age, so you answer with Ich bin … Jahre alt."),
            ),
        ),
    )
}
