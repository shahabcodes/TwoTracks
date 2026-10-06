package com.shahaabapps.core.content

/** Module 3: Daily life and time. Telling the time, days, regular verbs in the present, W-questions. */
internal object GermanA1M3 {
    private val refs = GermanA1.commonRefs

    private val words = Topic(
        id = "de3-words", title = "Words: days, times and the daily routine", minutes = 18, references = refs,
        blocks = listOf(
            B.plain("Hören part 1 is full of times and days: appointments, opening hours, trains. These words make those announcements understandable."),
            B.vocab(
                B.w("montag", "Montag", "Monday", "Am Montag arbeite ich.", "On Monday I work.", article = "der"),
                B.w("dienstag", "Dienstag", "Tuesday", "Am Dienstag habe ich Deutschkurs.", "On Tuesday I have German class.", article = "der"),
                B.w("mittwoch", "Mittwoch", "Wednesday", "Mittwoch ist die Mitte der Woche.", "Wednesday is the middle of the week.", article = "der"),
                B.w("donnerstag", "Donnerstag", "Thursday", "Am Donnerstag lerne ich.", "On Thursday I study.", article = "der"),
                B.w("freitag", "Freitag", "Friday", "Freitag ist in Katar frei.", "Friday is off in Qatar.", article = "der"),
                B.w("samstag", "Samstag", "Saturday", "Am Samstag kaufe ich ein.", "On Saturday I shop.", article = "der"),
                B.w("sonntag", "Sonntag", "Sunday", "Am Sonntag arbeite ich wieder.", "On Sunday I work again.", article = "der"),
                B.w("woche", "Woche", "week", "Die Woche hat sieben Tage.", "The week has seven days.", article = "die", plural = "Wochen"),
                B.w("uhr", "Uhr", "o'clock; clock", "Es ist acht Uhr.", "It is eight o'clock.", article = "die", plural = "Uhren"),
                B.w("stunde", "Stunde", "hour", "Ich lerne eine Stunde.", "I study for an hour.", article = "die", plural = "Stunden"),
                B.w("minute", "Minute", "minute", "Zehn Minuten noch.", "Ten more minutes.", article = "die", plural = "Minuten"),
                B.w("morgen", "morgen", "tomorrow (also: der Morgen, morning)", "Bis morgen!", "See you tomorrow!"),
                B.w("heute", "heute", "today", "Heute ist Dienstag.", "Today is Tuesday."),
                B.w("arbeiten", "arbeiten", "to work", "Ich arbeite von sieben bis halb vier.", "I work from seven to half past three."),
                B.w("lernen", "lernen", "to learn, to study", "Wir lernen Deutsch.", "We are learning German."),
                B.w("kommen", "kommen", "to come", "Wann kommst du?", "When are you coming?"),
                B.w("gehen", "gehen", "to go", "Ich gehe um acht ins Büro.", "I go to the office at eight."),
                B.w("machen", "machen", "to do, to make", "Was machst du heute?", "What are you doing today?"),
                B.w("fruehstueck", "Frühstück", "breakfast", "Das Frühstück ist um sieben.", "Breakfast is at seven.", article = "das"),
                B.w("termin", "Termin", "appointment", "Ich habe einen Termin um zehn.", "I have an appointment at ten.", article = "der", plural = "Termine"),
            ),
            B.h("Telling the time"),
            B.table(
                listOf("Clock", "Official (Hören announcements)", "Everyday"),
                listOf("8:00", "acht Uhr", "acht"),
                listOf("8:15", "acht Uhr fünfzehn", "Viertel nach acht"),
                listOf("8:30", "acht Uhr dreißig", "halb neun"),
                listOf("8:45", "acht Uhr fünfundvierzig", "Viertel vor neun"),
                listOf("16:15", "sechzehn Uhr fünfzehn", "Viertel nach vier"),
            ),
            B.diagram(
                caption = "halb points at the coming hour: halb neun is 8:30, half-way to nine.",
                nodes = listOf(
                    DNode("eight", "8:00", 0.15f, 0.5f, w = 0.2f, h = 0.24f, tone = 2),
                    DNode("half", "halb neun", 0.5f, 0.5f, w = 0.28f, h = 0.28f, sub = "8:30"),
                    DNode("nine", "9:00", 0.85f, 0.5f, w = 0.2f, h = 0.24f, tone = 2),
                ),
                edges = listOf(DEdge("eight", "half", dashed = true), DEdge("half", "nine", label = "half-way to")),
                aspect = 0.42f,
            ),
            B.tip("Trains, radio and the exam's recordings use the 24-hour official form: sechzehn Uhr fünfzehn. Friends say Viertel nach vier."),
            B.check("de3-words-k1", "de3-words", "'halb vier' is", listOf("4:30", "3:30", "4:00"), 1, "halb vier is half-way to four: 3:30."),
            B.glossary("Wochentag" to "Weekday.", "Wochenende" to "Weekend.", "Uhrzeit" to "Time of day."),
            B.recap("Days are all der and take am: am Montag.", "halb + next hour; Viertel nach, Viertel vor.", "Announcements use the 24-hour clock."),
        ),
    )

    private val grammar = Topic(
        id = "de3-grammar", title = "Grammar: regular verbs and W-questions", minutes = 20, references = refs,
        blocks = listOf(
            B.plain("Most German verbs follow one pattern in the present tense. Learn the endings once and you can use hundreds of verbs."),
            B.table(
                listOf("Person", "Ending", "lernen", "arbeiten"),
                listOf("ich", "-e", "lerne", "arbeite"),
                listOf("du", "-st", "lernst", "arbeitest"),
                listOf("er / sie / es", "-t", "lernt", "arbeitet"),
                listOf("wir", "-en", "lernen", "arbeiten"),
                listOf("ihr", "-t", "lernt", "arbeitet"),
                listOf("sie / Sie", "-en", "lernen", "arbeiten"),
            ),
            B.remember("Stems ending in -t or -d (arbeit-, find-) add an -e before -st and -t so you can say them: du arbeitest, er findet."),
            B.h("The W-questions"),
            B.table(
                listOf("Word", "Asks", "Example"),
                listOf("Wer", "who", "Wer ist das?"),
                listOf("Was", "what", "Was machst du?"),
                listOf("Wo", "where", "Wo wohnst du?"),
                listOf("Woher", "from where", "Woher kommst du?"),
                listOf("Wohin", "to where", "Wohin gehst du?"),
                listOf("Wann", "when", "Wann kommst du?"),
                listOf("Wie", "how", "Wie geht's?"),
                listOf("Wie viel / Wie viele", "how much / how many", "Wie viel kostet das?"),
                listOf("Warum", "why", "Warum lernst du Deutsch?"),
            ),
            B.diagram(
                caption = "W-question shape: w-word, verb, subject, rest. The verb stays second.",
                nodes = listOf(
                    DNode("w", "Wann", 0.14f, 0.5f, w = 0.22f, h = 0.26f, tone = 1),
                    DNode("v", "kommst", 0.38f, 0.5f, w = 0.22f, h = 0.26f),
                    DNode("s", "du", 0.62f, 0.5f, w = 0.18f, h = 0.26f, tone = 2),
                    DNode("r", "nach Hause?", 0.86f, 0.5f, w = 0.24f, h = 0.26f, tone = 2),
                ),
                edges = listOf(DEdge("w", "v"), DEdge("v", "s"), DEdge("s", "r")),
                aspect = 0.4f,
            ),
            B.h("Time expressions"),
            B.bullets("um + clock time: um acht Uhr", "am + day or part of day: am Montag, am Morgen, am Abend", "von … bis …: von sieben bis halb vier", "im + month: im Februar"),
            B.gap("de3-g-gap1", "Conjugate lernen.", "Sanya ___ jeden Tag Deutsch.", "lernt"),
            B.gap("de3-g-gap2", "Conjugate arbeiten.", "Du ___ bis halb vier.", "arbeitest"),
            B.gap("de3-g-gap3", "Preposition for a clock time.", "Der Kurs beginnt ___ sechzehn Uhr.", "um"),
            B.reorder("de3-g-re1", "Build the W-question.", "Wann beginnt der Kurs"),
            B.mistakes(
                "du arbeitst without the -e. It is du arbeitest.",
                "Using 'am' with a clock time. am Montag, but um acht Uhr.",
                "Mixing wo and wohin: Wo wohnst du? (where) versus Wohin gehst du? (to where).",
            ),
            B.glossary("Verbstamm" to "The verb stem: lern- from lernen.", "Endung" to "The ending added to the stem.", "Präsens" to "Present tense."),
            B.recap("Endings: -e, -st, -t, -en, -t, -en.", "W-word first, verb second.", "um for times, am for days, im for months."),
        ),
    )

    private val dialogue = Topic(
        id = "de3-dialogue", title = "Dialogue: making an appointment", minutes = 14, references = refs,
        blocks = listOf(
            B.plain("Appointments appear in Hören and in Sprechen part 3 (making requests). Listen for the day and the time."),
            B.dialogue("Calling the doctor's practice",
                B.line("Praxis", "Praxis Dr. Berger, guten Tag.", "Dr Berger's practice, good day."),
                B.line("Sanya", "Guten Tag, Sanya Khan hier. Ich brauche einen Termin.", "Good day, Sanya Khan speaking. I need an appointment."),
                B.line("Praxis", "Wann haben Sie Zeit? Am Dienstag um zehn Uhr?", "When are you free? Tuesday at ten?"),
                B.line("Sanya", "Nein, am Dienstag arbeite ich. Geht es am Mittwoch?", "No, on Tuesday I work. Does Wednesday work?"),
                B.line("Praxis", "Am Mittwoch um halb drei?", "Wednesday at half past two?"),
                B.line("Sanya", "Ja, das passt. Vielen Dank.", "Yes, that suits. Many thanks."),
                B.line("Praxis", "Bis Mittwoch, Frau Khan. Auf Wiederhören.", "Until Wednesday, Mrs Khan. Goodbye (on the phone)."),
            ),
            B.diagram(
                caption = "The appointment exchange: ask, offer, refuse with a reason, counter-offer, accept.",
                nodes = listOf(
                    DNode("a", "Ich brauche einen Termin", 0.5f, 0.1f, w = 0.6f, h = 0.14f),
                    DNode("b", "Am Dienstag um zehn?", 0.5f, 0.32f, w = 0.6f, h = 0.14f, tone = 1),
                    DNode("c", "Nein, da arbeite ich. Geht es am Mittwoch?", 0.5f, 0.54f, w = 0.76f, h = 0.14f, tone = 2),
                    DNode("d", "Am Mittwoch um halb drei?", 0.5f, 0.76f, w = 0.6f, h = 0.14f, tone = 1),
                    DNode("e", "Ja, das passt.", 0.5f, 0.94f, w = 0.4f, h = 0.1f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c"), DEdge("c", "d"), DEdge("d", "e")),
                aspect = 0.8f,
            ),
            B.check("de3-dialogue-k1", "de3-dialogue", "'Auf Wiederhören' is used", listOf("in person", "on the phone", "in a letter"), 1, "hören = to hear; it is the phone goodbye. In person it is Auf Wiedersehen."),
            B.glossary("Praxis" to "A doctor's practice.", "Das passt." to "That suits / works.", "Geht es …?" to "Does … work? (asking if a time is possible)"),
            B.recap("Ich brauche einen Termin. Wann haben Sie Zeit?", "Refuse politely with a reason, then counter-offer.", "Auf Wiederhören on the phone."),
        ),
    )

    private val practice = Topic(
        id = "de3-practice", title = "Practice: a week in Doha", minutes = 16, references = refs,
        blocks = listOf(
            B.plain("Read Usaid's week, then answer. Watch the times: official form in the text."),
            B.diagram(
                caption = "Reading a timetable text: hunt for days and clock times before the questions.",
                nodes = listOf(DNode("p0", "Days", 0.14f, 0.5f, w = 0.22f, h = 0.34f, tone = 0, sub = "von … bis"), DNode("p1", "Times", 0.38f, 0.5f, w = 0.22f, h = 0.34f, tone = 0, sub = "um … Uhr"), DNode("p2", "Verbs", 0.62f, 0.5f, w = 0.22f, h = 0.34f, tone = 2, sub = "arbeitet, lernt"), DNode("p3", "Questions", 0.86f, 0.5f, w = 0.22f, h = 0.34f, tone = 2, sub = "richtig / falsch")),
                aspect = 0.4f,
            ),
            B.p("Usaid arbeitet von Sonntag bis Donnerstag. Er beginnt um sieben Uhr und ist um fünfzehn Uhr dreißig fertig. Um sechzehn Uhr fünfzehn lernt er eine Stunde Deutsch. Am Freitag und am Samstag arbeitet er nicht. Am Samstag kauft er ein und telefoniert mit Sanya. Sanya arbeitet von Montag bis Freitag von acht bis vierzehn Uhr. Am Nachmittag hat sie von sechzehn bis achtzehn Uhr einen Englischkurs."),
            B.check("de3-practice-k1", "de3-practice", "Richtig oder falsch: Usaid lernt Deutsch um Viertel nach vier.", listOf("Richtig", "Falsch"), 0, "sechzehn Uhr fünfzehn = 16:15 = Viertel nach vier. Richtig."),
            B.check("de3-practice-k2", "de3-practice", "Richtig oder falsch: Sanya arbeitet am Samstag.", listOf("Richtig", "Falsch"), 1, "Montag bis Freitag. Falsch."),
            B.match("de3-p-match1", "Match the time.", "halb vier" to "15:30", "Viertel nach vier" to "16:15", "acht Uhr" to "08:00", "Viertel vor neun" to "08:45"),
            B.gap("de3-p-gap1", "Preposition for a day.", "___ Samstag kauft Usaid ein.", "Am"),
            B.gap("de3-p-gap2", "Conjugate telefonieren.", "Er ___ mit Sanya.", "telefoniert"),
            B.writing("de3-p-write", "Write 5 sentences about your week: when you work, when you study German, what you do at the weekend.",
                """
                Ich arbeite von Sonntag bis Donnerstag von sieben bis halb vier. Um Viertel nach vier lerne ich Deutsch. Am Freitag arbeite ich nicht. Am Samstag kaufe ich ein. Am Abend telefoniere ich mit Sanya.
                """,
                "Work days", "One clock time", "Study time", "Weekend activity", minWords = 20),
            B.tryIt("Try it today", "Say the time out loud every time you look at your phone today, in the official form.",
                "Said at least five times aloud", "Used 'halb' once correctly", "Said one 24-hour time", "Said your study slot: um Viertel nach vier",
            ),
            B.glossary("fertig" to "Finished.", "einkaufen" to "To shop (separable: kauft … ein).", "Nachmittag" to "Afternoon."),
            B.recap("Scan the text for days and times before the questions.", "um for times, am for days.", "Regular verbs: stem plus ending."),
        ),
    )

    val module = Module(
        id = "de-m3", number = 3, title = "Daily life and time", summary = "Telling the time, days, regular verbs in the present, W-questions.",
        examWeight = "A1", componentId = "grammar", topics = listOf(words, grammar, dialogue, practice),
        quiz = Quiz(
            id = "de-m3-test", title = "Module 3 test", passPercent = 60,
            questions = listOf(
                B.q("de3-q1", "de3-words", "Hören: 'Der Zug fährt um siebzehn Uhr dreißig.' When?", listOf("7:30", "17:30", "17:13"), setOf(1), "siebzehn Uhr dreißig = 17:30."),
                B.q("de3-q2", "de3-words", "'halb acht' is", listOf("8:30", "7:30", "8:00"), setOf(1), "halb eight is half-way to eight: 7:30."),
                B.q("de3-q3", "de3-grammar", "Lesen: 'Du ___ sehr viel.' (arbeiten)", listOf("arbeitst", "arbeitest", "arbeitet"), setOf(1), "Stem in -t takes -est for du."),
                B.q("de3-q4", "de3-grammar", "Which asks 'to where'?", listOf("Wo", "Woher", "Wohin"), setOf(2), "Wohin asks for a destination, to where. Wo asks where something is."),
                B.q("de3-q5", "de3-grammar", "Which preposition: '___ Montag'?", listOf("um", "am", "im"), setOf(1), "Days take am: am Montag. Clock times take um, months take im."),
                B.q("de3-q6", "de3-dialogue", "'Geht es am Mittwoch?' means", listOf("Is Wednesday possible?", "Where is it on Wednesday?", "It goes on Wednesday."), setOf(0), "Geht es …? asks whether a time works."),
                B.q("de3-q7", "de3-dialogue", "On the phone you end with", listOf("Auf Wiedersehen", "Auf Wiederhören", "Gute Nacht"), setOf(1), "Wiederhören for the phone."),
                B.q("de3-q8", "de3-practice", "Richtig oder falsch: 'Er ist um fünfzehn Uhr dreißig fertig' = he finishes at 3:30 pm.", listOf("Richtig", "Falsch"), setOf(0), "15:30 is 3:30 pm. Richtig."),
                B.q("de3-q9", "de3-words", "Which day is 'Thursday'?", listOf("Dienstag", "Donnerstag", "Freitag"), setOf(1), "Donnerstag is Thursday; Dienstag is Tuesday and Freitag is Friday."),
                B.q("de3-q10", "de3-grammar", "Schreiben: 'Wir ___ Deutsch.' (lernen)", listOf("lerne", "lernt", "lernen"), setOf(2), "wir takes the -en ending: wir lernen."),
                B.q("de3-q11", "de3-grammar", "Which W-question is correctly built?", listOf("Wann du kommst?", "Wann kommst du?", "Du kommst wann?"), setOf(1), "W-word, verb, subject."),
                B.q("de3-q12", "de3-words", "'Ich habe einen Termin' means I have", listOf("a ticket", "an appointment", "a timetable"), setOf(1), "Termin = appointment."),
            ),
        ),
    )
}
