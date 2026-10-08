package com.shahaabapps.core.content

/** Module 2: Me and my family. haben, pronouns, numbers to 100, countries and languages. */
internal object GermanA1M2 {
    private val refs = GermanA1.commonRefs

    private val words = Topic(
        id = "de2-words", title = "Words: family, numbers and countries", minutes = 18, references = refs,
        blocks = listOf(
            B.warmup(
                "de2-words-w1", "de2-words",
                "German says the number 21 as…",
                listOf("one-and-twenty", "twenty-one, as in English", "two-tens-one"), 0,
                "English used to do this too: four and twenty blackbirds.",
            ),
            B.plain("The A1 exam loves family, age and origin. These words cover all three, plus the numbers you need for ages, prices and phone numbers."),
            B.vocab(
                B.w("familie", "Familie", "family", "Meine Familie wohnt in Karachi.", "My family lives in Karachi.", article = "die", plural = "Familien"),
                B.w("mann", "Mann", "man; husband", "Mein Mann heißt Usaid.", "My husband is called Usaid.", article = "der", plural = "Männer"),
                B.w("frau", "Frau", "woman; wife; Mrs", "Meine Frau ist Lehrerin.", "My wife is a teacher.", article = "die", plural = "Frauen"),
                B.w("kind", "Kind", "child", "Wir haben kein Kind.", "We have no child.", article = "das", plural = "Kinder"),
                B.w("mutter", "Mutter", "mother", "Meine Mutter kocht gern.", "My mother likes cooking.", article = "die", plural = "Mütter"),
                B.w("vater", "Vater", "father", "Mein Vater ist Ingenieur.", "My father is an engineer.", article = "der", plural = "Väter"),
                B.w("eltern", "Eltern", "parents", "Meine Eltern wohnen in Indien.", "My parents live in India.", article = "die"),
                B.w("bruder", "Bruder", "brother", "Ich habe einen Bruder.", "I have a brother.", article = "der", plural = "Brüder"),
                B.w("schwester", "Schwester", "sister", "Sanya hat zwei Schwestern.", "Sanya has two sisters.", article = "die", plural = "Schwestern"),
                B.w("verheiratet", "verheiratet", "married", "Wir sind bald verheiratet.", "We are soon married."),
                B.w("alt", "alt", "old", "Wie alt bist du?", "How old are you?"),
                B.w("jahre", "Jahr", "year", "Ich bin 28 Jahre alt.", "I am 28 years old.", article = "das", plural = "Jahre"),
                B.w("land", "Land", "country", "Deutschland ist ein schönes Land.", "Germany is a beautiful country.", article = "das", plural = "Länder"),
                B.w("sprache", "Sprache", "language", "Deutsch ist eine schwere Sprache.", "German is a hard language.", article = "die", plural = "Sprachen"),
                B.w("sprechen", "sprechen", "to speak", "Ich spreche Englisch und ein bisschen Deutsch.", "I speak English and a little German."),
                B.w("deutschland", "Deutschland", "Germany", "Wir ziehen nach Deutschland.", "We are moving to Germany."),
                B.w("indien", "Indien", "India", "Usaid kommt aus Indien.", "Usaid comes from India."),
                B.w("pakistan", "Pakistan", "Pakistan", "Sanya kommt aus Pakistan.", "Sanya comes from Pakistan."),
                B.w("katar", "Katar", "Qatar", "Wir wohnen in Katar.", "We live in Qatar."),
                B.w("telefonnummer", "Telefonnummer", "phone number", "Wie ist Ihre Telefonnummer?", "What is your phone number?", article = "die", plural = "Telefonnummern"),
            ),
            B.h("Numbers 0 to 100"),
            B.code("text", """
                0 null   1 eins   2 zwei   3 drei   4 vier   5 fünf   6 sechs   7 sieben   8 acht   9 neun   10 zehn
                11 elf   12 zwölf   13 dreizehn   14 vierzehn   15 fünfzehn   16 sechzehn   17 siebzehn   18 achtzehn   19 neunzehn
                20 zwanzig   30 dreißig   40 vierzig   50 fünfzig   60 sechzig   70 siebzig   80 achtzig   90 neunzig   100 hundert
                21 einundzwanzig (one-and-twenty)   28 achtundzwanzig   45 fünfundvierzig   99 neunundneunzig
            """),
            B.diagram(
                caption = "German reads two-digit numbers backwards: the units come first, then 'und', then the tens.",
                nodes = listOf(
                    DNode("u", "acht", 0.2f, 0.4f, w = 0.24f, h = 0.24f, sub = "8, units first"),
                    DNode("und", "und", 0.5f, 0.4f, w = 0.18f, h = 0.24f, tone = 2),
                    DNode("t", "zwanzig", 0.8f, 0.4f, w = 0.28f, h = 0.24f, tone = 1, sub = "20, tens last"),
                    DNode("n", "achtundzwanzig = 28", 0.5f, 0.84f, w = 0.6f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("u", "und"), DEdge("und", "t")),
                aspect = 0.5f,
            ),
            B.tip("sechzehn and siebzehn drop the s and en: not sechszehn. dreißig has ß, every other ten has z."),
            B.check("de2-words-k1", "de2-words", "How do you say 34?", listOf("dreißigundvier", "vierunddreißig", "dreivierzig"), 1, "Units first: vier und dreißig."),
            B.glossary("Geschwister" to "Siblings.", "ledig" to "Single (unmarried).", "Staatsangehörigkeit" to "Nationality, a form word."),
            B.recap("Family words come with der, die, das; learn the article with the word.", "Two-digit numbers go units-und-tens.", "Ich spreche … names your languages."),
        ),
    )

    private val grammar = Topic(
        id = "de2-grammar", title = "Grammar: haben, pronouns and possessives", minutes = 20, references = refs,
        blocks = listOf(
            B.warmup(
                "de2-grammar-w1", "de2-grammar",
                "To turn a German statement into a yes/no question, what moves?",
                listOf("The verb goes first", "A question word goes at the start", "Nothing; only the tone changes"), 0,
                "A statement puts the verb second. A yes/no question does something simpler than that.",
            ),
            B.plain("haben (to have) is the second verb you cannot do without. With the pronouns and mein/dein, you can describe your whole family."),
            B.table(
                listOf("Person", "haben", "Example"),
                listOf("ich", "habe", "Ich habe einen Bruder."),
                listOf("du", "hast", "Hast du Kinder?"),
                listOf("er / sie / es", "hat", "Sie hat zwei Schwestern."),
                listOf("wir", "haben", "Wir haben Zeit."),
                listOf("ihr", "habt", "Habt ihr Hunger?"),
                listOf("sie / Sie", "haben", "Haben Sie eine Telefonnummer?"),
            ),
            B.h("Pronouns"),
            B.table(
                listOf("German", "English", "Note"),
                listOf("ich", "I", ""),
                listOf("du", "you", "one person, informal"),
                listOf("er / sie / es", "he / she / it", "es for das-nouns like das Kind"),
                listOf("wir", "we", ""),
                listOf("ihr", "you", "several people, informal"),
                listOf("sie", "they", "lower case"),
                listOf("Sie", "you", "formal, always capital"),
            ),
            B.h("mein, dein, Ihr: whose?"),
            B.p("Possessives take the gender of the thing owned. mein Vater (der), meine Mutter (die), mein Kind (das), meine Eltern (plural)."),
            B.table(
                listOf("", "der / das", "die / plural"),
                listOf("my", "mein Vater, mein Kind", "meine Mutter, meine Eltern"),
                listOf("your (du)", "dein Bruder", "deine Schwester"),
                listOf("his", "sein Bruder", "seine Schwester"),
                listOf("her", "ihr Bruder", "ihre Schwester"),
                listOf("our", "unser Haus", "unsere Familie"),
                listOf("your (Sie)", "Ihr Mann", "Ihre Frau"),
            ),
            B.diagram(
                caption = "Pick the ending from the owned noun, not the owner: sein + e because Schwester is die.",
                nodes = listOf(
                    DNode("owner", "Usaid (he)", 0.18f, 0.5f, w = 0.28f, h = 0.22f, tone = 2, sub = "sein-"),
                    DNode("noun", "Schwester (die)", 0.82f, 0.5f, w = 0.32f, h = 0.22f, sub = "needs -e"),
                    DNode("res", "seine Schwester", 0.5f, 0.86f, w = 0.4f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("owner", "res", dashed = true), DEdge("noun", "res", label = "ending")),
                aspect = 0.5f,
            ),
            B.h("Yes/no questions: verb first"),
            B.p("Turn a statement into a question by putting the verb first: Du hast Kinder. → Hast du Kinder? W-questions keep the verb second: Wie alt bist du?"),
            B.gap("de2-g-gap1", "Fill the gap with haben.", "Ich ___ zwei Schwestern.", "habe"),
            B.gap("de2-g-gap2", "Fill the gap with the possessive.", "Das ist ___ Mutter. (my)", "meine"),
            B.reorder("de2-g-re1", "Make a yes/no question.", "Hast du Geschwister"),
            B.mistakes(
                "Choosing the possessive ending from the owner: 'ihr Schwester' for 'her sister' is wrong; it is ihre Schwester.",
                "Writing sie (they) with a capital. Only formal Sie is capitalised.",
                "Keeping the verb second in a yes/no question. Hast du … ? starts with the verb.",
            ),
            B.glossary("Possessivartikel" to "A word like mein or dein that shows ownership.", "Ja/Nein-Frage" to "A yes/no question; the verb comes first.", "W-Frage" to "A question starting with a w-word; verb second."),
            B.recap("haben: habe, hast, hat, haben, habt, haben.", "mein/dein/sein/ihr take -e before die-nouns and plurals.", "Yes/no question: verb first. W-question: verb second."),
        ),
    )

    private val dialogue = Topic(
        id = "de2-dialogue", title = "Dialogue: talking about your family", minutes = 14, references = refs,
        blocks = listOf(
            B.warmup(
                "de2-dialogue-w1", "de2-dialogue",
                "Someone tells you about their family and then says \"Und du?\". What is that doing?",
                listOf("Handing the same question back to you", "Asking whether you agree", "Asking you to repeat it"), 0,
                "Two words that keep a conversation alive, and the speaking exam rewards them.",
            ),
            B.plain("A conversation with a new colleague. Listen, repeat each line, then check the English."),
            B.dialogue("In the office kitchen",
                B.line("Lena", "Usaid, bist du verheiratet?", "Usaid, are you married?"),
                B.line("Usaid", "Noch nicht. Ich heirate im Februar. Meine Verlobte heißt Sanya.", "Not yet. I am getting married in February. My fiancée is called Sanya."),
                B.line("Lena", "Schön! Woher kommt sie?", "Nice! Where is she from?"),
                B.line("Usaid", "Sie kommt aus Pakistan. Sie ist 24 Jahre alt und Lehrerin.", "She is from Pakistan. She is 24 years old and a teacher."),
                B.line("Lena", "Hast du Geschwister?", "Do you have siblings?"),
                B.line("Usaid", "Ja, ich habe einen Bruder. Er wohnt in Indien. Und du?", "Yes, I have a brother. He lives in India. And you?"),
                B.line("Lena", "Ich habe zwei Schwestern. Meine Eltern wohnen in Hamburg.", "I have two sisters. My parents live in Hamburg."),
                B.line("Usaid", "Wie alt sind deine Schwestern?", "How old are your sisters?"),
                B.line("Lena", "Dreiundzwanzig und dreißig.", "Twenty-three and thirty."),
            ),
            B.diagram(
                caption = "Three question words carry most A1 family talk.",
                nodes = listOf(
                    DNode("wie", "Wie alt …?", 0.2f, 0.5f, w = 0.28f, h = 0.3f, sub = "age"),
                    DNode("woher", "Woher …?", 0.5f, 0.5f, w = 0.28f, h = 0.3f, tone = 1, sub = "origin"),
                    DNode("wo", "Wo …?", 0.8f, 0.5f, w = 0.28f, h = 0.3f, tone = 2, sub = "place"),
                ),
                aspect = 0.4f,
            ),
            B.check("de2-dialogue-k1", "de2-dialogue", "'Hast du Geschwister?' asks about", listOf("children", "siblings", "parents"), 1, "Geschwister are brothers and sisters together."),
            B.glossary("Verlobte" to "Fiancée (die); Verlobter for the man.", "heiraten" to "To marry.", "noch nicht" to "Not yet."),
            B.recap("Bist du verheiratet? Hast du Geschwister? Wie alt …?", "Und du? hands the question back.", "Say ages with the backwards numbers."),
        ),
    )

    private val practice = Topic(
        id = "de2-practice", title = "Practice: a family profile", minutes = 16, references = refs,
        blocks = listOf(
            B.warmup(
                "de2-practice-w1", "de2-practice",
                "\"My sister\" in German. Does mein change?",
                listOf("Yes, it follows the noun it owns and becomes meine", "No, mein never changes", "Only in writing"), 0,
                "The ending follows the thing owned rather than the person owning it.",
            ),
            B.plain("Lesen part 1 in the exam is a short text with true/false statements. Read Sanya's text, then answer."),
            B.diagram(
                caption = "Lesen part 1: a short text, then true or false lines. Read everything first.",
                nodes = listOf(DNode("p0", "Text", 0.14f, 0.5f, w = 0.22f, h = 0.34f, tone = 2, sub = "5 to 8 lines"), DNode("p1", "Statement", 0.38f, 0.5f, w = 0.22f, h = 0.34f, tone = 0, sub = "richtig / falsch"), DNode("p2", "Check", 0.62f, 0.5f, w = 0.22f, h = 0.34f, tone = 0, sub = "find the line"), DNode("p3", "Answer", 0.86f, 0.5f, w = 0.22f, h = 0.34f, tone = 2, sub = "tick one")),
                aspect = 0.65f,
            ),
            B.p("Ich heiße Sanya und bin 24 Jahre alt. Ich komme aus Pakistan und wohne in Karachi. Ich bin Lehrerin. Meine Familie ist groß: Ich habe zwei Schwestern und einen Bruder. Meine Eltern wohnen auch in Karachi. Mein Verlobter heißt Usaid. Er wohnt in Doha und ist Softwareentwickler. Wir sprechen Englisch und Urdu, und wir lernen Deutsch."),
            B.check("de2-practice-k1", "de2-practice", "Richtig oder falsch: Sanya hat einen Bruder.", listOf("Richtig", "Falsch"), 0, "The text says 'einen Bruder'. Richtig."),
            B.check("de2-practice-k2", "de2-practice", "Richtig oder falsch: Usaid wohnt in Karachi.", listOf("Richtig", "Falsch"), 1, "Usaid wohnt in Doha. Falsch."),
            B.match("de2-p-match1", "Match the number.", "achtundzwanzig" to "28", "vierundzwanzig" to "24", "dreißig" to "30", "siebzehn" to "17"),
            B.gap("de2-p-gap1", "Complete with haben.", "Sanya ___ zwei Schwestern.", "hat"),
            B.gap("de2-p-gap2", "Complete with the possessive (her).", "___ Eltern wohnen in Karachi.", "Ihre", hint = "Capital at the start of the sentence."),
            B.writing("de2-p-write", "Write 5 sentences about your family: who is in it, their ages or jobs, where they live.",
                """
                Meine Familie ist klein. Ich habe einen Bruder. Er ist 31 Jahre alt und Ingenieur. Meine Eltern wohnen in Indien. Meine Verlobte Sanya ist Lehrerin und kommt aus Pakistan.
                """,
                "At least two family members", "One age", "One job", "One place", minWords = 20),
            B.tryIt("Try it today", "Say your phone number in German, digit by digit, then in pairs (neunundneunzig, …).",
                "Said it digit by digit", "Said it in pairs", "Said your age", "Said your parents' city",
            ),
            B.glossary("groß / klein" to "Big / small.", "auch" to "Also.", "lernen" to "To learn."),
            B.recap("Read the whole text before the true/false lines.", "Possessive ending follows the owned noun.", "Numbers: practise them aloud, they are everywhere in the exam."),
        ),
    )

    val module = Module(
        id = "de-m2", number = 2, title = "Me and my family", summary = "haben, pronouns, possessives, numbers to 100, countries and languages.",
        examWeight = "A1", componentId = "vocabulary", topics = listOf(words, grammar, dialogue, practice),
        quiz = Quiz(
            id = "de-m2-test", title = "Module 2 test", passPercent = 60,
            questions = listOf(
                B.q("de2-q1", "de2-words", "Hören: 'Ich bin fünfundvierzig Jahre alt.' How old?", listOf("54", "45", "15"), setOf(1), "fünf-und-vierzig: 5 and 40 = 45.",
                whyWrong = mapOf(
                    0 to "The trap exactly: German says the units first, so this is five-and-forty rather than fifty-four.",
                    2 to "fünfzehn would be fifteen. The und is the signal that two numbers are being added.",
                ),
            ),
                B.q("de2-q2", "de2-words", "Which article goes with Kind?", listOf("der", "die", "das"), setOf(2), "Kind is neuter: das Kind, plural die Kinder.",
                whyWrong = mapOf(
                    0 to "A child is not der, which is the guess an English ear makes from thinking of a boy.",
                    1 to "die Kinder is the plural. In the singular it is das.",
                ),
            ),
                B.q("de2-q3", "de2-grammar", "Lesen: 'Wir ___ zwei Kinder.'", listOf("habe", "habt", "haben"), setOf(2), "wir takes haben: Wir haben zwei Kinder.",
                whyWrong = mapOf(
                    0 to "habe is ich.",
                    1 to "habt is ihr, the plural you.",
                ),
            ),
                B.q("de2-q4", "de2-grammar", "'Her brother' is", listOf("ihr Bruder", "ihre Bruder", "sein Bruder"), setOf(0), "Bruder is der, so no -e: ihr Bruder.",
                whyWrong = mapOf(
                    1 to "The -e goes on before die-nouns and plurals. Bruder is der, so it stays bare.",
                    2 to "sein is his. ihr is hers.",
                ),
            ),
                B.q("de2-q5", "de2-grammar", "Which is a correct yes/no question?", listOf("Du hast Zeit?", "Hast du Zeit?", "Zeit du hast?"), setOf(1), "Verb first: Hast du Zeit?",
                whyWrong = mapOf(
                    0 to "A statement with a question mark. It works in speech and is not what the paper is testing.",
                    2 to "Nothing in German puts the verb last in a main clause.",
                ),
            ),
                B.q("de2-q6", "de2-dialogue", "'Noch nicht' means", listOf("not yet", "never", "also"), setOf(0), "noch nicht = not yet.",
                whyWrong = mapOf(
                    1 to "nie is never. noch nicht leaves the door open.",
                    2 to "auch is also.",
                ),
            ),
                B.q("de2-q7", "de2-dialogue", "Hören: 'Woher kommt deine Frau?' asks", listOf("where your wife lives", "where your wife is from", "how old your wife is"), setOf(1), "Woher asks about origin, from where. Wo asks where you live.",
                whyWrong = mapOf(
                    0 to "Wo wohnt … asks where she lives. woher asks where she started.",
                    2 to "Wie alt … asks the age.",
                ),
            ),
                B.q("de2-q8", "de2-practice", "Richtig oder falsch: 'Meine Eltern wohnen auch in Karachi' means the parents live elsewhere.", listOf("Richtig", "Falsch"), setOf(1), "auch = also; they live in Karachi too. Falsch.",
                whyWrong = mapOf(
                    0 to "auch means too, so they live in Karachi as well rather than somewhere else.",
                ),
            ),
                B.q("de2-q9", "de2-words", "Schreiben: write 17.", listOf("siebenzehn", "siebzehn", "siebzig"), setOf(1), "siebzehn drops the -en; siebzig is 70.",
                whyWrong = mapOf(
                    0 to "sieben loses its -en inside the teen: siebzehn.",
                    2 to "siebzig is seventy. One syllable apart, and the listening paper knows it.",
                ),
            ),
                B.q("de2-q10", "de2-grammar", "Which pronoun is formal 'you'?", listOf("ihr", "sie", "Sie"), setOf(2), "Capital Sie is formal you.",
                whyWrong = mapOf(
                    0 to "ihr is the plural you among friends.",
                    1 to "Lower-case sie is she or they. Only the capital makes it formal, which is invisible when spoken.",
                ),
            ),
                B.q("de2-q11", "de2-words", "'Ich spreche Urdu und Englisch.' is about", listOf("countries", "languages", "jobs"), setOf(1), "sprechen with a language name says which languages you speak.",
                whyWrong = mapOf(
                    0 to "Ich komme aus … is the sentence for countries.",
                    2 to "Ich bin … von Beruf is the one for jobs.",
                ),
            ),
                B.q("de2-q12", "de2-practice", "Lesen: on a form, 'verheiratet' is a", listOf("job", "marital status", "language"), setOf(1), "Familienstand: ledig, verheiratet.",
                whyWrong = mapOf(
                    0 to "Beruf is the field for the job.",
                    2 to "Sprachen is the field for languages.",
                ),
            ),
            ),
        ),
    )
}
