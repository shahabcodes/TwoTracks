package com.shahaabapps.core.content

/** Module 11: Writing to people. The Schreiben paper: forms (part 1) and short messages (part 2), exactly as the exam asks. */
internal object GermanA1M11 {
    private val refs = GermanA1.commonRefs

    private val forms = Topic(
        id = "de11-forms", title = "Schreiben part 1: filling in a form", minutes = 18, references = refs,
        blocks = listOf(
            B.warmup(
                "de11-forms-w1", "de11-forms",
                "Schreiben part 1 is a form to fill in. Where do the answers come from?",
                listOf("The situation text above it", "Your own real details", "Whatever plausibly fits each field"), 0,
                "Nothing has to be invented, and inventing is how the marks go.",
            ),
            B.plain("Schreiben part 1 gives you a short situation and a form with five gaps. Most of the answers are in the text; you copy them into the right field. It is worth five points and takes three minutes when you know the field names."),
            B.diagram(
                caption = "The form fields the exam uses, in the order they usually appear. Learn them as vocabulary.",
                nodes = listOf(
                    DNode("a", "Familienname / Vorname", 0.25f, 0.2f, w = 0.44f, h = 0.2f, tone = 1),
                    DNode("b", "Geburtsdatum / Geburtsort", 0.75f, 0.2f, w = 0.44f, h = 0.2f),
                    DNode("c", "Straße, Hausnummer / PLZ, Wohnort", 0.25f, 0.5f, w = 0.44f, h = 0.2f, tone = 2),
                    DNode("d", "Telefon / E-Mail", 0.75f, 0.5f, w = 0.44f, h = 0.2f),
                    DNode("e", "Staatsangehörigkeit / Beruf", 0.25f, 0.8f, w = 0.44f, h = 0.2f, tone = 1),
                    DNode("f", "Datum / Unterschrift", 0.75f, 0.8f, w = 0.44f, h = 0.2f, tone = 2),
                ),
                aspect = 0.6f,
            ),
            B.vocab(
                B.w("m11-familienname", "Familienname", "surname", "Familienname: Shahab", "Surname: Shahab", article = "der", plural = "Familiennamen"),
                B.w("m11-vorname", "Vorname", "first name", "Vorname: Usaid", "First name: Usaid", article = "der", plural = "Vornamen"),
                B.w("m11-geburtsdatum", "Geburtsdatum", "date of birth", "Geburtsdatum: 12.03.1996", "Date of birth: 12 March 1996", article = "das"),
                B.w("m11-geburtsort", "Geburtsort", "place of birth", "Geburtsort: Karachi", "Place of birth: Karachi", article = "der"),
                B.w("m11-strasse-hausnummer", "Straße, Hausnummer", "street, house number", "Gartenstraße 12", "12 Garden Street"),
                B.w("m11-plz", "Postleitzahl (PLZ)", "postcode", "PLZ: 50667", "Postcode: 50667", article = "die"),
                B.w("m11-wohnort", "Wohnort", "place of residence", "Wohnort: Köln", "Town: Cologne", article = "der"),
                B.w("m11-staatsangehoerigkeit", "Staatsangehörigkeit", "nationality", "Staatsangehörigkeit: pakistanisch", "Nationality: Pakistani", article = "die"),
                B.w("m11-unterschrift", "Unterschrift", "signature", "Datum, Unterschrift", "Date, signature", article = "die"),
                B.w("m11-datum", "Datum", "date", "Datum: 6.10.2026", "Date: 6 October 2026", article = "das"),
                B.w("m11-anmeldung", "Anmeldung", "registration", "Anmeldung für den Deutschkurs", "Registration for the German course", article = "die", plural = "Anmeldungen"),
                B.w("m11-formular", "Formular", "form", "Bitte das Formular ausfüllen.", "Please fill in the form.", article = "das", plural = "Formulare"),
                B.w("m11-ausfuellen", "ausfüllen", "to fill in", "Füllen Sie das Formular aus.", "Fill in the form."),
                B.w("m11-ankreuzen", "ankreuzen", "to tick", "Bitte ankreuzen: ja oder nein.", "Please tick: yes or no."),
                B.w("m11-abfahrt-ankunft", "Abfahrt / Ankunft", "departure / arrival (on forms)", "Abfahrt: 10.10., Ankunft: 12.10.", "Departure 10 Oct, arrival 12 Oct"),
                B.w("m11-anzahl", "Anzahl", "number of", "Anzahl Personen: 2", "Number of people: 2", article = "die"),
                B.w("m11-zahlung", "Zahlung", "payment", "Zahlung: mit Karte", "Payment: by card", article = "die"),
                B.w("m11-kreditkarte", "Kreditkarte", "credit card", "Ich zahle mit Kreditkarte.", "I pay by credit card.", article = "die", plural = "Kreditkarten"),
                B.w("m11-bar", "bar", "in cash", "Zahlen Sie bar?", "Are you paying cash?"),
                B.w("m11-einzelzimmer", "Einzelzimmer / Doppelzimmer", "single / double room", "Ein Doppelzimmer für zwei Nächte.", "A double room for two nights.", article = "das"),
                B.w("m11-geburtstag", "Geburtstag", "birthday", "Wann hast du Geburtstag?", "When is your birthday?", article = "der", plural = "Geburtstage"),
                B.w("m11-ledig", "ledig / verheiratet", "single / married", "Familienstand: ledig", "Marital status: single"),
            ),
            B.h("Dates and the ordinal numbers"),
            B.p("Forms want dates as numbers: 12.03.1996 is the twelfth of March. Said aloud: der zwölfte März. In a letter: am 12. März. Ordinals: add -te up to 19 (erste, zweite, dritte, vierte, siebte are irregular) and -ste from 20."),
            B.table(
                listOf("Written", "Said"),
                listOf("1.", "der erste"), listOf("2.", "der zweite"), listOf("3.", "der dritte"), listOf("7.", "der siebte"),
                listOf("12.", "der zwölfte"), listOf("20.", "der zwanzigste"), listOf("31.", "der einunddreißigste"),
            ),
            B.h("A worked example"),
            B.p("Situation: Ihre Freundin Sanya Khan möchte sich für einen Deutschkurs anmelden. Sie ist am 5. Mai 1998 in Lahore geboren, wohnt in der Schulstraße 8 in 50667 Köln, ist Lehrerin und möchte den Kurs am Abend besuchen. Ihre Telefonnummer ist 0176 445566."),
            B.table(
                listOf("Feld", "Antwort"),
                listOf("Familienname", "Khan"),
                listOf("Vorname", "Sanya"),
                listOf("Geburtsdatum", "05.05.1998"),
                listOf("Geburtsort", "Lahore"),
                listOf("Straße, Hausnummer", "Schulstraße 8"),
                listOf("PLZ, Wohnort", "50667 Köln"),
                listOf("Beruf", "Lehrerin"),
                listOf("Kurszeit (bitte ankreuzen)", "☐ vormittags ☒ abends"),
                listOf("Telefon", "0176 445566"),
            ),
            B.gap("de11-f-gap1", "Which field?", "Khan goes into the field ___. (Familienname / Vorname)", "Familienname"),
            B.gap("de11-f-gap2", "Which field?", "Lahore goes into the field ___. (Wohnort / Geburtsort)", "Geburtsort"),
            B.gap("de11-f-gap3", "Date format.", "5 May 1998 as a German date: ___", "05.05.1998", "5.5.1998"),
            B.check("de11-forms-k1", "de11-forms", "The form asks for 'Staatsangehörigkeit'. Sanya is from Pakistan. You write", listOf("Pakistan", "pakistanisch", "Lahore"), 1, "Nationality is the adjective form: pakistanisch (deutsch, indisch, katarisch)."),
            B.mistakes(
                "Swapping Vorname and Familienname.",
                "Writing the English date order: 05/05/1998 is accepted, but 05.05.1998 is the German way.",
                "Writing Wohnort where it says Geburtsort.",
                "Leaving a gap blank; a guess from the text is better than nothing.",
            ),
            B.glossary("Familienstand" to "Marital status: ledig, verheiratet.", "vormittags / nachmittags / abends" to "Mornings / afternoons / evenings.", "Nacht / Nächte" to "Night / nights, on hotel forms."),
            B.recap("Learn the field names: they are the whole of part 1.", "Dates as 05.05.1998; nationalities as adjectives.", "Every answer is in the situation text; copy it carefully."),
        ),
    )

    private val messages = Topic(
        id = "de11-messages", title = "Schreiben part 2: the short message that scores full marks", minutes = 26, references = refs,
        blocks = listOf(
            B.warmup(
                "de11-messages-w1", "de11-messages",
                "Schreiben part 2 wants a short message. How long?",
                listOf("About thirty words, in five lines", "About a hundred words", "As long as you like"), 0,
                "It is scored on the parts it covers rather than its length, and there are three to cover.",
            ),
            B.plain("Schreiben part 2 gives you a situation and three content points. You write about 30 words: a greeting, the three points, a sign-off. Ten points are available and the three content points are most of them. This topic is the recipe."),
            B.diagram(
                caption = "The recipe: five lines, every time. The three middle lines are the three content points from the task.",
                nodes = listOf(
                    DNode("a", "Anrede: Liebe Lena, / Lieber Tom, / Sehr geehrte Frau Braun,", 0.5f, 0.1f, w = 0.9f, h = 0.14f, tone = 1),
                    DNode("b", "Punkt 1", 0.5f, 0.3f, w = 0.5f, h = 0.14f),
                    DNode("c", "Punkt 2", 0.5f, 0.5f, w = 0.5f, h = 0.14f),
                    DNode("d", "Punkt 3 (often a question)", 0.5f, 0.7f, w = 0.5f, h = 0.14f),
                    DNode("e", "Gruß: Viele Grüße, / Bis bald, / Mit freundlichen Grüßen, + Name", 0.5f, 0.9f, w = 0.9f, h = 0.14f, tone = 1),
                ),
                edges = listOf(DEdge("a", "b"), DEdge("b", "c"), DEdge("c", "d"), DEdge("d", "e")),
                aspect = 0.67f,
            ),
            B.h("du or Sie?"),
            B.table(
                listOf("", "Friend (du)", "Office, teacher, landlord (Sie)"),
                listOf("Greeting", "Liebe Lena, / Lieber Tom, / Hallo Sanya,", "Sehr geehrte Frau Braun, / Sehr geehrter Herr Weber, / Sehr geehrte Damen und Herren,"),
                listOf("You", "du, dich, dir, dein", "Sie, Ihnen, Ihr"),
                listOf("Sign-off", "Viele Grüße / Liebe Grüße / Bis bald / Dein Usaid", "Mit freundlichen Grüßen / Freundliche Grüße"),
            ),
            B.remember("After the greeting comes a comma and the next line starts with a small letter: Liebe Lena, ich komme … The sign-off has no full stop and your name goes on the next line."),
            B.h("Sentences that cover almost every task"),
            B.table(
                listOf("Task says", "You write"),
                listOf("Sagen Sie, warum Sie schreiben", "Ich schreibe dir, weil … / Ich habe eine Frage."),
                listOf("Entschuldigen Sie sich", "Es tut mir leid, aber ich kann nicht kommen."),
                listOf("Sagen Sie, wann / wo", "Am Samstag um 15 Uhr. / Wir treffen uns am Bahnhof."),
                listOf("Bitten Sie um …", "Kannst du mir bitte … schicken? / Können Sie mir bitte … geben?"),
                listOf("Fragen Sie nach …", "Wann …? Wo …? Was kostet …? Haben Sie …?"),
                listOf("Machen Sie einen Vorschlag", "Wir können … / Möchtest du …?"),
                listOf("Bedanken Sie sich", "Vielen Dank für …"),
                listOf("Laden Sie ein", "Ich möchte dich zu … einladen. Kommst du?"),
            ),
            B.vocab(
                B.w("m11-einladen", "einladen", "to invite", "Ich lade dich ein.", "I invite you."),
                B.w("m11-einladung", "Einladung", "invitation", "Danke für die Einladung!", "Thanks for the invitation!", article = "die", plural = "Einladungen"),
                B.w("m11-vorschlag", "Vorschlag", "suggestion", "Ich habe einen Vorschlag.", "I have a suggestion.", article = "der", plural = "Vorschläge"),
                B.w("m11-absagen", "absagen", "to cancel", "Ich muss leider absagen.", "I have to cancel, unfortunately."),
                B.w("m11-schicken", "schicken", "to send", "Kannst du mir die Adresse schicken?", "Can you send me the address?"),
                B.w("m11-antwort", "Antwort", "answer, reply", "Danke für deine Antwort.", "Thanks for your reply.", article = "die", plural = "Antworten"),
                B.w("m11-frage", "Frage", "question", "Ich habe eine Frage.", "I have a question.", article = "die", plural = "Fragen"),
                B.w("m11-liebe-gruesse", "Liebe Grüße", "Best wishes (informal)", "Liebe Grüße, Sanya", "Best wishes, Sanya"),
                B.w("m11-mfg", "Mit freundlichen Grüßen", "Yours sincerely", "Mit freundlichen Grüßen, U. Shahab", "Yours sincerely, U. Shahab"),
                B.w("m11-sehr-geehrte", "Sehr geehrte / Sehr geehrter", "Dear (formal)", "Sehr geehrte Frau Braun,", "Dear Ms Braun,"),
                B.w("m11-lieber", "Liebe / Lieber", "Dear (informal)", "Lieber Tom,", "Dear Tom,"),
                B.w("m11-treffen-sich", "sich treffen", "to meet up", "Wir treffen uns um acht.", "We meet at eight."),
            ),
            B.h("Three worked examples, then yours"),
            B.p("Task: Sie können am Samstag nicht zum Deutschkurs kommen. Schreiben Sie an die Kursleiterin Frau Braun: warum Sie nicht kommen können, was Sie zu Hause machen, wann Sie wieder kommen."),
            B.code("text", """
                Sehr geehrte Frau Braun,
                ich kann am Samstag leider nicht zum Kurs kommen, weil ich arbeiten muss.
                Ich lerne zu Hause die Wörter und mache die Hausaufgaben.
                Am nächsten Samstag komme ich wieder.
                Mit freundlichen Grüßen
                Usaid Shahab
            """),
            B.p("Task: Ihr Freund Tom hat Geburtstag. Schreiben Sie: Glückwunsch, was Sie schenken, wann Sie kommen."),
            B.code("text", """
                Lieber Tom,
                herzlichen Glückwunsch zum Geburtstag!
                Ich schenke dir ein Buch über Fußball.
                Ich komme am Samstag um sechs Uhr zu deiner Party.
                Bis bald
                Usaid
            """),
            B.marked("de11-m-write1", "Task: Sie möchten mit einer Freundin ins Kino gehen. Schreiben Sie: welcher Film, wann und wo Sie sich treffen, eine Frage.",
                """
                Liebe Lena, ich möchte am Freitag ins Kino gehen. Der Film heißt Die Reise und beginnt um 20 Uhr. Wir treffen uns um 19 Uhr vor dem Kino am Bahnhof. Kommst du mit? Viele Grüße, Sanya
                """,
                "Which film" to listOf("film", "heißt", "heisst"),
                "When and where you meet" to listOf("treffen uns", "um ", "vor dem", "am bahnhof", "im kino"),
                "A question" to listOf("?"),
                minWords = 30),
            B.marked("de11-m-write2", "Task: Sie haben eine Wohnung gesehen und möchten sie mieten. Schreiben Sie an den Vermieter Herrn Weber: welche Wohnung, ab wann, eine Frage (formal).",
                """
                Sehr geehrter Herr Weber, ich habe die Wohnung in der Gartenstraße 12 gesehen und möchte sie gern mieten. Ich kann ab dem 1. November einziehen. Wie hoch sind die Nebenkosten? Mit freundlichen Grüßen, Usaid Shahab
                """,
                "Which flat" to listOf("wohnung", "straße", "strasse"),
                "From when (ab …)" to listOf("ab ", "ab dem", "einziehen"),
                "A formal question" to listOf("?"),
                minWords = 30),
            B.check("de11-messages-k1", "de11-messages", "Your message to the course teacher should start with", listOf("Hallo Frau Braun,", "Sehr geehrte Frau Braun,", "Liebe Braun,"), 1, "A teacher or office gets the formal greeting and Sie."),
            B.mistakes(
                "Skipping a content point; each one is marked separately.",
                "Mixing du and Sie in one message.",
                "No greeting or no sign-off; both are checked.",
                "Fewer than 30 words; add a reason with weil or a time.",
            ),
            B.glossary("Anrede" to "The greeting line.", "Gruß / Grußformel" to "The sign-off line.", "Inhaltspunkt" to "Content point; the task gives three.", "Glückwunsch" to "Congratulations: Herzlichen Glückwunsch!"),
            B.recap("Five lines: greeting, point, point, point, sign-off; about 30 words.", "du with Liebe/Lieber and Viele Grüße; Sie with Sehr geehrte/r and Mit freundlichen Grüßen.", "Reuse the stock sentences; a question is the easiest third point."),
        ),
    )

    private val reading = Topic(
        id = "de11-reading", title = "Lesen: the three parts and how to beat each one", minutes = 22, references = refs,
        blocks = listOf(
            B.warmup(
                "de11-reading-w1", "de11-reading",
                "In Lesen Teil 2, how many of the conditions does the right advert meet?",
                listOf("All of them", "Most of them", "The first one"), 0,
                "Which is exactly what the decoy is designed to fail.",
            ),
            B.plain("Lesen is 25 minutes and 15 points: two short texts with Richtig/Falsch (part 1), five adverts to match to situations (part 2), five notices with Richtig/Falsch (part 3). You have seen all three in the modules; this topic is the method."),
            B.diagram(
                caption = "The three parts and the single question each one really asks.",
                nodes = listOf(
                    DNode("a", "Teil 1: two emails or notes", 0.17f, 0.5f, w = 0.3f, h = 0.5f, tone = 1, sub = "Does the text say this? Richtig / Falsch"),
                    DNode("b", "Teil 2: five situations, two adverts each", 0.5f, 0.5f, w = 0.3f, h = 0.5f, sub = "Which advert fits ALL conditions? a / b"),
                    DNode("c", "Teil 3: five signs or notices", 0.83f, 0.5f, w = 0.3f, h = 0.5f, tone = 2, sub = "Is the statement true here? Richtig / Falsch"),
                ),
                aspect = 0.62f,
            ),
            B.h("Teil 1: Richtig or Falsch on a personal text"),
            B.bullets(
                "Read the statements first, then the text. Each statement maps to one sentence.",
                "Watch for negations (nicht, kein), the opposite day or time, and the opposite person (ich versus wir).",
                "If the text does not mention it at all, the answer is Falsch at A1.",
            ),
            B.p("Text: Hallo Tom, am Samstag ziehen wir um! Die neue Wohnung ist in der Bahnhofstraße, im dritten Stock. Kannst du uns helfen? Wir fangen um 9 Uhr an. Am Abend gibt es Pizza für alle. Bring bitte dein Auto mit. Viele Grüße, Usaid"),
            B.check("de11-reading-k1", "de11-reading", "Richtig oder falsch: Usaid zieht am Sonntag um.", listOf("Richtig", "Falsch"), 1, "am Samstag, not Sunday. Falsch."),
            B.check("de11-reading-k2", "de11-reading", "Richtig oder falsch: Tom soll sein Auto mitbringen.", listOf("Richtig", "Falsch"), 0, "Bring bitte dein Auto mit. Richtig."),
            B.h("Teil 2: matching situations to adverts"),
            B.bullets(
                "Underline the two or three conditions in the situation: what, when, how much, where.",
                "Check advert a against every condition; one miss and it is out.",
                "The wrong advert usually matches the topic but fails one detail: closed on Sunday, too expensive, wrong city.",
            ),
            B.p("Situation: Sie möchten am Sonntag mit Ihren Kindern schwimmen gehen. Anzeige a: Schwimmbad Nord, Montag bis Samstag 7 bis 21 Uhr, Sonntag geschlossen. Anzeige b: Freizeitbad Süd, täglich 9 bis 20 Uhr, Kinderbecken, Familienkarte 15 Euro."),
            B.check("de11-reading-k3", "de11-reading", "Welche Anzeige passt?", listOf("Anzeige a", "Anzeige b", "keine"), 1, "a is closed on Sunday; b is open daily and has a children's pool."),
            B.h("Teil 3: notices and signs"),
            B.bullets(
                "The statement is often a rewording: geöffnet becomes 'man kann … kaufen'; geschlossen becomes 'man kann nicht …'.",
                "Numbers are the trap: 8 bis 12 Uhr versus 'nach 12 Uhr'.",
                "Signs: Eingang, Ausgang, Kasse, Drücken, Ziehen, Rauchen verboten, Hunde erlaubt, Parken nur für Kunden.",
            ),
            B.vocab(
                B.w("m11-eingang", "Eingang / Ausgang", "entrance / exit", "Der Eingang ist links.", "The entrance is on the left.", article = "der"),
                B.w("m11-verboten", "verboten", "forbidden", "Rauchen verboten!", "No smoking!"),
                B.w("m11-erlaubt", "erlaubt", "allowed", "Hunde erlaubt.", "Dogs allowed."),
                B.w("m11-druecken", "drücken / ziehen", "push / pull", "Bitte drücken.", "Please push."),
                B.w("m11-kasse", "Kasse", "checkout, till", "Bitte an der Kasse zahlen.", "Please pay at the till.", article = "die", plural = "Kassen"),
                B.w("m11-umziehen", "umziehen", "to move house", "Wir ziehen am Samstag um.", "We move on Saturday."),
                B.w("m11-oeffnungszeiten", "Öffnungszeiten", "opening hours", "Die Öffnungszeiten stehen an der Tür.", "The opening hours are on the door.", article = "die"),
                B.w("m11-nur", "nur", "only", "Parken nur für Kunden.", "Parking for customers only."),
            ),
            B.p("Schild: Bibliothek. Öffnungszeiten: Montag bis Freitag 10 bis 18 Uhr, Samstag 10 bis 14 Uhr. Bücher ausleihen nur mit Bibliotheksausweis. Essen und Trinken verboten."),
            B.check("de11-reading-k4", "de11-reading", "Richtig oder falsch: Am Samstag kann man um 15 Uhr Bücher ausleihen.", listOf("Richtig", "Falsch"), 1, "Saturday closes at 14 Uhr. Falsch."),
            B.check("de11-reading-k5", "de11-reading", "Richtig oder falsch: Man darf in der Bibliothek nicht essen.", listOf("Richtig", "Falsch"), 0, "Essen und Trinken verboten. Richtig."),
            B.mistakes(
                "Reading the text first and the statements last; do it the other way round.",
                "Choosing the advert that matches the topic but not the day or the price.",
                "Missing nur, nicht, kein, geschlossen, verboten; each one flips the answer.",
            ),
            B.glossary("Teil" to "Part of a paper.", "Anzeige" to "Advert.", "Schild" to "Sign (das).", "Bibliotheksausweis" to "Library card."),
            B.recap("Teil 1: statements first; negation and day words flip the answer.", "Teil 2: all conditions must hold; the decoy fails one detail.", "Teil 3: rewordings and numbers; learn the sign vocabulary."),
        ),
    )

    val module = Module(
        id = "de-m11", number = 11, title = "Writing to people", summary = "Schreiben part 1 forms, part 2 messages with the five-line recipe, and the Lesen method.",
        examWeight = "A1", componentId = "writing", topics = listOf(forms, messages, reading),
        quiz = Quiz(
            id = "de-m11-test", title = "Module 11 test", passPercent = 60,
            questions = listOf(
                B.q("de11-q1", "de11-forms", "On a form, 'Familienname' is", listOf("first name", "surname", "family size"), setOf(1), "Familienname is the surname; Vorname is the first name.",
                whyWrong = mapOf(
                    0 to "Vorname is the first name, the one that comes before.",
                    2 to "Nothing on an A1 form asks the size of your family.",
                ),
            ),
                B.q("de11-q2", "de11-forms", "Born in Lahore, living in Köln: what goes in 'Wohnort'?", listOf("Lahore", "Köln", "Pakistan"), setOf(1), "Wohnort is where you live now.",
                whyWrong = mapOf(
                    0 to "Lahore is the Geburtsort, where you were born.",
                    2 to "Pakistan would answer Land or Staatsangehörigkeit.",
                ),
            ),
                B.q("de11-q3", "de11-forms", "The German way to write 3 August 1995 is", listOf("03.08.1995", "08.03.1995", "1995.08.03"), setOf(0), "Day, month, year with dots.",
                whyWrong = mapOf(
                    1 to "That is the American order, month first.",
                    2 to "Year first is used in some systems and not on a German form.",
                ),
            ),
                B.q("de11-q4", "de11-forms", "'Staatsangehörigkeit' for someone from Pakistan is", listOf("Pakistan", "pakistanisch", "Pakistani"), setOf(1), "Nationality is written as the adjective.",
                whyWrong = mapOf(
                    0 to "Pakistan is the country. The field asks for the nationality.",
                    2 to "Pakistani is the English noun. German uses the adjective, lower case.",
                ),
            ),
                B.q("de11-q5", "de11-messages", "A message to your landlord starts with", listOf("Hallo Herr Weber,", "Sehr geehrter Herr Weber,", "Lieber Weber,"), setOf(1), "Formal: Sehr geehrter Herr … with Sie.",
                whyWrong = mapOf(
                    0 to "Hallo with a surname is an odd mixture, and too casual for a landlord.",
                    2 to "Lieber goes with a first name, and a surname on its own is wrong either way.",
                ),
            ),
                B.q("de11-q6", "de11-messages", "A message to a friend ends with", listOf("Mit freundlichen Grüßen", "Viele Grüße", "Hochachtungsvoll"), setOf(1), "Viele Grüße or Liebe Grüße for du.",
                whyWrong = mapOf(
                    0 to "Mit freundlichen Grüßen is the formal one, for strangers and offices.",
                    2 to "Hochachtungsvoll is formal to the point of being old-fashioned.",
                ),
            ),
                B.q("de11-q7", "de11-messages", "The task has three content points. You cover two well and skip one. What happens?", listOf("Full marks anyway", "You lose the points for the missing one", "The whole task scores zero"), setOf(1), "Each point is marked on its own; the missing one costs its points.",
                whyWrong = mapOf(
                    0 to "Two out of three points is two thirds of the content marks.",
                    2 to "Nothing scores zero for one missing point; the rest still counts.",
                ),
            ),
                B.q("de11-q8", "de11-messages", "'Kannst du mir bitte die Adresse schicken?' is", listOf("a request", "an apology", "an invitation"), setOf(0), "Kannst du … bitte …? asks for something.",
                whyWrong = mapOf(
                    1 to "An apology would be Es tut mir leid.",
                    2 to "An invitation would offer something rather than ask for it.",
                ),
            ),
                B.q("de11-q9", "de11-messages", "After 'Liebe Lena,' the next line starts", listOf("with a capital letter", "with a small letter", "with your name"), setOf(1), "After the comma the sentence continues: Liebe Lena, ich …",
                whyWrong = mapOf(
                    0 to "A comma, not a full stop, so the sentence carries on in lower case.",
                    2 to "Your name goes at the end, with the sign-off.",
                ),
            ),
                B.q("de11-q10", "de11-reading", "Lesen Teil 2: the advert matches the topic but is closed on the day you need. It is", listOf("still the right answer", "the wrong answer", "half right"), setOf(1), "All conditions must hold.",
                whyWrong = mapOf(
                    0 to "Matching the topic is one condition of several, and all of them have to hold.",
                    2 to "There is no half right: the answer is one advert.",
                ),
            ),
                B.q("de11-q11", "de11-reading", "Lesen: 'Parken nur für Kunden.' Richtig oder falsch: Anyone may park here.", listOf("Richtig", "Falsch"), setOf(1), "nur für Kunden: customers only. Falsch.",
                whyWrong = mapOf(
                    0 to "nur für Kunden is customers only, so not anyone.",
                ),
            ),
                B.q("de11-q12", "de11-reading", "'Rauchen verboten' means", listOf("smoking area", "no smoking", "smoking allowed"), setOf(1), "verboten is forbidden.",
                whyWrong = mapOf(
                    0 to "A smoking area would be a Raucherbereich.",
                    2 to "verboten is the opposite of allowed.",
                ),
            ),
                B.q("de11-q13", "de11-reading", "Lesen: 'Samstag 10 bis 14 Uhr.' Richtig oder falsch: You can go in at 15 Uhr on Saturday.", listOf("Richtig", "Falsch"), setOf(1), "Closes at 14 Uhr. Falsch.",
                whyWrong = mapOf(
                    0 to "bis 14 Uhr means it shuts at two, an hour before.",
                ),
            ),
                B.q("de11-q14", "de11-forms", "'Bitte ankreuzen' tells you to", listOf("sign", "tick a box", "write the date"), setOf(1), "ankreuzen is to put a cross in a box.",
                whyWrong = mapOf(
                    0 to "Signing is unterschreiben.",
                    2 to "A date would be asked for by its own field.",
                ),
            ),
            ),
        ),
    )
}
