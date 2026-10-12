package com.shahaabapps.core.content

/** Module 4: Food and shopping. der/die/das, plurals, the accusative, prices and quantities. */
internal object GermanA1M4 {
    private val refs = GermanA1.commonRefs

    private val words = Topic(
        id = "de4-words", title = "Words: food, drink and the supermarket", minutes = 18, references = refs + B.ref("Vocabulary trainer app", GermanA1.GOETHE + "prj/dvt/en/index.html") + B.ref("Practice material: everyday life", GermanA1.GOETHE + "en/spr/ueb/lua.html"),
        blocks = listOf(
            B.warmup(
                "de4-words-w1", "de4-words",
                "\"Two kilos of apples\" in German. Does it need a word for \"of\"?",
                listOf("No, the quantity sits straight in front of the noun", "Yes, von", "Yes, aus"), 0,
                "German is shorter than English here rather than longer.",
            ),
            B.plain("Shopping dialogues are a staple of Hören part 2 and Sprechen part 3. These words get you through a supermarket and a café."),
            B.vocab(
                B.w("brot", "Brot", "bread", "Ich kaufe ein Brot.", "I buy a loaf of bread.", article = "das", plural = "Brote"),
                B.w("wasser", "Wasser", "water", "Ein Wasser, bitte.", "A water, please.", article = "das"),
                B.w("kaffee", "Kaffee", "coffee", "Der Kaffee ist heiß.", "The coffee is hot.", article = "der"),
                B.w("tee", "Tee", "tea", "Sanya trinkt gern Tee.", "Sanya likes drinking tea.", article = "der"),
                B.w("milch", "Milch", "milk", "Die Milch ist im Kühlschrank.", "The milk is in the fridge.", article = "die"),
                B.w("apfel", "Apfel", "apple", "Ein Kilo Äpfel, bitte.", "A kilo of apples, please.", article = "der", plural = "Äpfel"),
                B.w("reis", "Reis", "rice", "Wir essen oft Reis.", "We often eat rice.", article = "der"),
                B.w("huhn", "Hähnchen", "chicken (as food)", "Das Hähnchen ist lecker.", "The chicken is delicious.", article = "das"),
                B.w("gemuese", "Gemüse", "vegetables", "Gemüse ist gesund.", "Vegetables are healthy.", article = "das"),
                B.w("obst", "Obst", "fruit", "Obst gibt es am Markt.", "There is fruit at the market.", article = "das"),
                B.w("essen", "essen", "to eat", "Was isst du gern?", "What do you like eating?"),
                B.w("trinken", "trinken", "to drink", "Ich trinke Kaffee.", "I drink coffee."),
                B.w("kaufen", "kaufen", "to buy", "Ich kaufe Brot und Milch.", "I buy bread and milk."),
                B.w("kosten", "kosten", "to cost", "Was kostet das?", "What does that cost?"),
                B.w("euro", "Euro", "euro", "Das kostet drei Euro fünfzig.", "That costs three euros fifty.", article = "der"),
                B.w("supermarkt", "Supermarkt", "supermarket", "Der Supermarkt ist um die Ecke.", "The supermarket is round the corner.", article = "der", plural = "Supermärkte"),
                B.w("kilo", "Kilo", "kilogram", "Zwei Kilo Reis, bitte.", "Two kilos of rice, please.", article = "das"),
                B.w("flasche", "Flasche", "bottle", "Eine Flasche Wasser.", "A bottle of water.", article = "die", plural = "Flaschen"),
                B.w("lecker", "lecker", "delicious", "Das ist lecker!", "That is delicious!"),
                B.w("rechnung", "Rechnung", "bill", "Die Rechnung, bitte.", "The bill, please.", article = "die", plural = "Rechnungen"),
            ),
            B.h("Prices"),
            B.code("text", """
                1,50 €   ein Euro fünfzig        (the comma is the decimal point)
                3,99 €   drei Euro neunundneunzig
                12,00 €  zwölf Euro
                0,80 €   achtzig Cent
            """),
            B.diagram(
                caption = "Quantities come before the noun with no 'of': ein Kilo Äpfel, eine Flasche Wasser.",
                nodes = listOf(
                    DNode("q", "eine Flasche", 0.3f, 0.5f, w = 0.36f, h = 0.26f, tone = 1, sub = "quantity"),
                    DNode("n", "Wasser", 0.72f, 0.5f, w = 0.3f, h = 0.26f, sub = "noun, no article"),
                ),
                edges = listOf(DEdge("q", "n")),
                aspect = 0.4f,
            ),
            B.check("de4-words-k1", "de4-words", "'drei Euro fünfzig' is", listOf("3.15 €", "3.50 €", "350 €"), 1, "Euro, then cents: 3,50 €."),
            B.check(
                "de4-words-k2", "de4-words",
                "'A bottle of water' is",
                listOf("eine Flasche von Wasser", "eine Flasche Wasser", "eine Flasche aus Wasser"), 1,
                "The quantity sits straight in front of the noun. German has no word for the English of here.",
                whyWrong = mapOf(
                    0 to "von is the obvious guess from English and it is never used this way.",
                    2 to "aus would say the bottle is made of water.",
                ),
            ),
            B.check(
                "de4-words-k3", "de4-words",
                "You want to know a price. You ask",
                listOf("Was kostet das?", "Was ist das?", "Wie ist das?"), 0,
                "kosten is to cost, and Was kostet das? is the question every shop expects.",
                whyWrong = mapOf(
                    1 to "That asks what the thing is.",
                    2 to "That asks what it is like.",
                ),
            ),
            B.glossary("Kasse" to "Checkout.", "Angebot" to "Special offer.", "Tüte" to "Bag."),
            B.recap("Learn food words with their article and plural.", "Prices: Euro then cents, comma as the decimal.", "Quantity + noun with no 'of'."),
        ),
    )

    private val grammar = Topic(
        id = "de4-grammar", title = "Grammar: articles, plurals, the accusative and the two ways to say no", minutes = 26, references = refs + B.ref("Free practice at A1 and A2", GermanA1.GOETHE + "en/spr/ueb/ele.html") + B.ref("Deutsch für dich: free exercises and a community", GermanA1.GOETHE + "prj/dfd/en/home.cfm"),
        blocks = listOf(
            B.warmup(
                "de4-grammar-w1", "de4-grammar",
                "A noun becomes the object of a sentence. Which articles actually change?",
                listOf("Only the masculine ones", "All of them", "None; word order marks the object"), 0,
                "This is the reason the accusative turns out to be less frightening than it sounds.",
            ),
            B.plain("Every German noun has a gender. You cannot guess it reliably, so learn the article as part of the word. Then the accusative changes just one of the three."),
            B.table(
                listOf("Gender", "Definite", "Indefinite", "Negative", "Example"),
                listOf("masculine", "der", "ein", "kein", "der Apfel"),
                listOf("feminine", "die", "eine", "keine", "die Milch"),
                listOf("neuter", "das", "ein", "kein", "das Brot"),
                listOf("plural", "die", "(none)", "keine", "die Äpfel"),
            ),
            B.h("The accusative: the thing you buy, eat, have"),
            B.p("The subject of a sentence is nominative. The direct object (what you buy, eat, see, have) is accusative. Only the masculine changes: der becomes den, ein becomes einen, kein becomes keinen."),
            B.diagram(
                caption = "Subject stays as it is; the masculine object changes der to den and ein to einen.",
                nodes = listOf(
                    DNode("s", "Ich", 0.15f, 0.5f, w = 0.2f, h = 0.26f, tone = 2, sub = "subject"),
                    DNode("v", "kaufe", 0.42f, 0.5f, w = 0.22f, h = 0.26f),
                    DNode("o", "einen Apfel", 0.76f, 0.5f, w = 0.36f, h = 0.26f, tone = 1, sub = "object, masculine: ein → einen"),
                ),
                edges = listOf(DEdge("s", "v"), DEdge("v", "o")),
                aspect = 0.82f,
            ),
            B.table(
                listOf("", "Nominative (subject)", "Accusative (object)"),
                listOf("der Apfel", "Der Apfel ist rot.", "Ich esse den Apfel."),
                listOf("ein Kaffee", "Ein Kaffee kostet 3 €.", "Ich trinke einen Kaffee."),
                listOf("die Milch", "Die Milch ist kalt.", "Ich kaufe die Milch."),
                listOf("das Brot", "Das Brot ist frisch.", "Ich kaufe das Brot."),
            ),
            B.h("Plurals: five patterns"),
            B.table(
                listOf("Ending", "Example", "Tip"),
                listOf("-e", "der Tag, die Tage", "many masculine"),
                listOf("-(e)n", "die Flasche, die Flaschen", "most feminine"),
                listOf("-er (+umlaut)", "das Kind, die Kinder", "many short neuter"),
                listOf("-s", "das Auto, die Autos", "foreign words"),
                listOf("no ending (+umlaut)", "der Apfel, die Äpfel", "masculine in -el, -er, -en"),
            ),
            B.h("Verbs that change their vowel"),
            B.p("A few common verbs change the stem for du and er/sie/es: essen → du isst, er isst; sprechen → du sprichst; nehmen → du nimmst; fahren → du fährst."),
            B.h("nicht or kein: the two ways to say no"),
            B.p("German has two negatives and they do not overlap. kein replaces the article in front of a noun; nicht does everything else. This is the commonest A1 mistake, it is audible the moment you make it, and the fix is one question: am I saying no to a noun?"),
            B.table(
                listOf("What you are negating", "Use", "Example"),
                listOf("a noun with ein, or with no article at all", "kein", "Ich habe keine Zeit."),
                listOf("a noun with der, die, das or a possessive", "nicht", "Das ist nicht mein Buch."),
                listOf("a verb", "nicht", "Ich arbeite heute nicht."),
                listOf("an adjective or an adverb", "nicht", "Der Kaffee ist nicht heiß."),
            ),
            B.p("kein takes exactly the endings ein takes, so it changes in the accusative in the one place ein does: the masculine."),
            B.table(
                listOf("", "Nominative", "Accusative"),
                listOf("masculine", "kein Bruder", "keinen Bruder"),
                listOf("feminine", "keine Schwester", "keine Schwester"),
                listOf("neuter", "kein Kind", "kein Kind"),
                listOf("plural", "keine Eltern", "keine Eltern"),
            ),
            B.h("Where nicht stands"),
            B.bullets(
                "At the end, when it negates the whole sentence: Ich verstehe das nicht.",
                "Before an adjective after sein: Die Wohnung ist nicht teuer.",
                "Before the word it contradicts: Ich komme nicht heute, sondern morgen.",
                "Before the infinitive at the end of a verb bracket: Ich kann heute nicht kommen.",
            ),
            B.gap("de4-g-gap5", "Noun with no article, so kein.", "Ich trinke ___ Kaffee. (der Kaffee)", "keinen"),
            B.gap("de4-g-gap6", "The noun has a possessive, so nicht.", "Das ist ___ mein Mantel.", "nicht"),
            B.gap("de4-g-gap7", "Negating a verb.", "Heute arbeite ich ___.", "nicht"),
            B.check(
                "de4-grammar-k3", "de4-grammar",
                "'I do not have a car' is",
                listOf("Ich habe nicht ein Auto.", "Ich habe kein Auto.", "Ich habe nicht Auto."),
                1,
                "Auto is a noun with ein in front of it, so the negative is kein. nicht never replaces an article.",
                whyWrong = mapOf(
                    0 to "nicht does not stand in front of ein. The two never appear together like this.",
                    2 to "Dropping the article does not make nicht right; a bare noun still takes kein.",
                ),
            ),
            B.check(
                "de4-grammar-k4", "de4-grammar",
                "'That is not my brother' is",
                listOf("Das ist kein Bruder.", "Das ist nicht mein Bruder.", "Das ist keinen Bruder."),
                1,
                "The noun already has a possessive, so there is no article for kein to replace: nicht does it.",
                whyWrong = mapOf(
                    0 to "That says \"that is not a brother\", which is a different sentence. The possessive is what you are contradicting.",
                    2 to "keinen is the masculine accusative, and this noun is the subject after ist, not an object.",
                ),
            ),
            B.check(
                "de4-grammar-k1", "de4-grammar",
                "'Ich nehme ___ Tee.' (der Tee)",
                listOf("den", "der", "dem"), 0,
                "The thing you take is the object, and the masculine is the one article that changes: der becomes den.",
                whyWrong = mapOf(
                    1 to "der is the subject form: Der Tee ist heiß.",
                    2 to "dem is the dative, which comes after prepositions like mit and in.",
                ),
            ),
            B.check(
                "de4-grammar-k2", "de4-grammar",
                "The plural of der Apfel is",
                listOf("die Äpfel", "die Apfeln", "die Apfels"), 0,
                "Masculine nouns in -el, -er and -en usually add no ending and take an umlaut instead.",
                whyWrong = mapOf(
                    1 to "-n is the pattern for most feminine nouns: die Flasche, die Flaschen.",
                    2 to "-s is for words taken from other languages: das Auto, die Autos.",
                ),
            ),
            B.gap("de4-g-gap1", "Accusative article.", "Ich kaufe ___ Apfel. (ein)", "einen"),
            B.gap("de4-g-gap2", "Accusative article.", "Wir trinken ___ Tee. (der)", "den"),
            B.gap("de4-g-gap3", "Conjugate essen.", "Was ___ du gern?", "isst"),
            B.gap("de4-g-gap4", "Negative article.", "Ich habe ___ Zeit. (die Zeit)", "keine"),
            B.reorder("de4-g-re1", "Build the sentence.", "Sanya trinkt einen Kaffee"),
            B.mistakes(
                "Changing feminine or neuter in the accusative. Only der/ein become den/einen.",
                "Using nicht for a noun: 'Ich habe nicht Zeit' is wrong; it is keine Zeit.",
                "Forgetting the vowel change: 'du esst' should be du isst.",
            ),
            B.glossary("Nominativ" to "The subject case.", "Akkusativ" to "The direct-object case.", "Artikel" to "der, die, das and their forms."),
            B.recap("Learn the article with every noun.", "Accusative changes only the masculine: den, einen, keinen.", "kein replaces an article in front of a noun; nicht negates everything else."),
        ),
    )

    private val dialogue = Topic(
        id = "de4-dialogue", title = "Dialogue: at the bakery and the café", minutes = 14, references = refs + B.ref("Practice material: everyday life", GermanA1.GOETHE + "en/spr/ueb/lua.html") + B.ref("Fotogeschichten: everyday life in pictures", GermanA1.GOETHE + "prj/mwd/en/deu/fot.html"),
        blocks = listOf(
            B.warmup(
                "de4-dialogue-w1", "de4-dialogue",
                "At the till you are asked \"Zusammen oder getrennt?\". What is the choice?",
                listOf("Paying together or separately", "Eating in or taking away", "With or without a bag"), 0,
                "It comes when you pay, not when you order.",
            ),
            B.plain("Two short exchanges you will have in your first week in Germany. Note how the shop asks 'Sonst noch etwas?' (anything else?)."),
            B.dialogue("At the bakery",
                B.line("Verkäuferin", "Guten Morgen! Was darf es sein?", "Good morning! What can I get you?"),
                B.line("Usaid", "Ich hätte gern zwei Brötchen und ein Brot.", "I would like two rolls and a loaf."),
                B.line("Verkäuferin", "Gern. Sonst noch etwas?", "Certainly. Anything else?"),
                B.line("Usaid", "Nein, danke. Was kostet das?", "No, thanks. What does that cost?"),
                B.line("Verkäuferin", "Das macht vier Euro zwanzig.", "That comes to four euros twenty."),
                B.line("Usaid", "Hier, bitte. Auf Wiedersehen.", "Here you are. Goodbye."),
            ),
            B.dialogue("In the café",
                B.line("Kellner", "Was möchten Sie trinken?", "What would you like to drink?"),
                B.line("Sanya", "Einen Tee mit Milch, bitte. Und ein Stück Kuchen.", "A tea with milk, please. And a piece of cake."),
                B.line("Kellner", "Kommt sofort.", "Coming right up."),
                B.line("Sanya", "Die Rechnung, bitte.", "The bill, please."),
                B.line("Kellner", "Zusammen oder getrennt?", "Together or separately?"),
                B.line("Sanya", "Zusammen, bitte.", "Together, please."),
            ),
            B.diagram(
                caption = "Ich hätte gern and Ich möchte are the polite ways to order; both take the accusative.",
                nodes = listOf(
                    DNode("a", "Ich hätte gern", 0.28f, 0.3f, w = 0.4f, h = 0.22f, tone = 1),
                    DNode("b", "Ich möchte", 0.28f, 0.7f, w = 0.4f, h = 0.22f, tone = 1),
                    DNode("o", "einen Kaffee / eine Cola / ein Wasser", 0.76f, 0.5f, w = 0.42f, h = 0.3f, sub = "accusative object"),
                ),
                edges = listOf(DEdge("a", "o"), DEdge("b", "o")),
                aspect = 0.5f,
            ),
            B.check("de4-dialogue-k1", "de4-dialogue", "'Sonst noch etwas?' means", listOf("Anything else?", "How much?", "Together?"), 0, "The shop asks whether you want anything else."),
            B.check(
                "de4-dialogue-k2", "de4-dialogue",
                "'Zusammen oder getrennt?' is asked",
                listOf("when you pay", "when you order", "when you sit down"), 0,
                "It is about the bill: one payment between you, or one each.",
                whyWrong = mapOf(
                    1 to "Was darf es sein? is the ordering question.",
                    2 to "Nobody asks it on the way in; there is nothing to divide yet.",
                ),
            ),
            B.check(
                "de4-dialogue-k3", "de4-dialogue",
                "Ordering a coffee politely, you say",
                listOf("Ich hätte gern einen Kaffee.", "Ich habe einen Kaffee.", "Ich bin ein Kaffee."), 0,
                "Ich hätte gern is the polite I would like. Ich möchte does the same job.",
                whyWrong = mapOf(
                    1 to "That says you already have one, which is not an order.",
                    2 to "That says you are a coffee.",
                ),
            ),
            B.glossary("Brötchen" to "Bread roll (das).", "Das macht …" to "That comes to … (a total).", "getrennt" to "Separately."),
            B.recap("Ich hätte gern … / Ich möchte … to order.", "Was kostet das? Das macht …", "Zusammen oder getrennt? when paying."),
        ),
    )

    private val practice = Topic(
        id = "de4-practice", title = "Practice: a shopping list and a note", minutes = 16, references = refs + B.ref("A1 practice set 2 (PDF)", GermanA1.GOETHE + "pro/relaunch/prf/materialien/A1_sd1/sd_1_uebungssatz02.pdf") + B.ref("Practice material: everyday life", GermanA1.GOETHE + "en/spr/ueb/lua.html"),
        blocks = listOf(
            B.warmup(
                "de4-practice-w1", "de4-practice",
                "A shop notice says geschlossen. What have you learnt?",
                listOf("It is closed", "It is open all day", "It has moved"), 0,
                "One word that settles half the questions in the reading paper.",
            ),
            B.plain("Lesen part 2 shows adverts and notices; Schreiben part 2 is a short message. Practise both with a shopping errand."),
            B.diagram(
                caption = "Schreiben part 2: the four content points the marker looks for.",
                nodes = listOf(DNode("p0", "Greeting", 0.14f, 0.5f, w = 0.22f, h = 0.34f, tone = 0, sub = "Hallo Sanya,"), DNode("p1", "Message", 0.38f, 0.5f, w = 0.22f, h = 0.34f, tone = 0, sub = "what and when"), DNode("p2", "Question", 0.62f, 0.5f, w = 0.22f, h = 0.34f, tone = 0, sub = "Was möchtest du?"), DNode("p3", "Sign-off", 0.86f, 0.5f, w = 0.22f, h = 0.34f, tone = 0, sub = "Bis später, Usaid")),
                aspect = 0.65f,
            ),
            B.p("Angebote diese Woche bei Rewe: Äpfel 1 kg 1,99 €. Milch 1 l 0,89 €. Brot 2,49 €. Hähnchen 1 kg 6,50 €. Reis 1 kg 2,20 €. Geöffnet Montag bis Samstag von 7 bis 22 Uhr. Sonntag geschlossen."),
            B.check("de4-practice-k1", "de4-practice", "Richtig oder falsch: Der Supermarkt ist am Sonntag offen.", listOf("Richtig", "Falsch"), 1, "Sonntag geschlossen = closed on Sunday. Falsch."),
            B.check("de4-practice-k2", "de4-practice", "Zwei Kilo Reis kosten", listOf("2,20 €", "4,40 €", "6,50 €"), 1, "2 × 2,20 € = 4,40 €."),
            B.check(
                "de4-practice-k3", "de4-practice",
                "You can shop at this Rewe at nine on a Saturday evening.",
                listOf("Richtig", "Falsch"), 0,
                "Montag bis Samstag von 7 bis 22 Uhr, so Saturday at 21:00 is inside the hours. Only Sunday is shut.",
                whyWrong = mapOf(
                    1 to "Saturday is in the range. Sonntag geschlossen is the one day that is not, and it is the line a notice question usually turns on.",
                ),
            ),
            B.match("de4-p-match1", "Match article and noun.", "der" to "Apfel", "die" to "Milch", "das" to "Brot", "die (plural)" to "Flaschen"),
            B.gap("de4-p-gap1", "Accusative.", "Kaufst du ___ Brot? (ein)", "ein", hint = "Brot is das: no change."),
            B.gap("de4-p-gap2", "Accusative.", "Ich nehme ___ Apfel. (der)", "den"),
            B.gap("de4-p-gap3", "Plural.", "Ich kaufe drei ___. (Flasche)", "Flaschen"),
            B.worked("""
                Worked example. Usaid writes the note. Greeting with a comma: Hallo Sanya, and the next word stays
                lower case. Then when, with um: ich gehe um fünf Uhr in den Supermarkt. Then what, and the masculine
                object is the one that changes: Ich kaufe einen Tee und ein Brot. Then a question, so she has
                something to answer: Was möchtest du? Last the sign-off: Bis später, Usaid.
            """),
            B.faded(
                "de4-practice-f1",
                "Sanya writes back, same four parts. She is going to the market at seven and buying apples and chicken.",
                listOf(
                    "Greeting with a comma: Hallo Usaid,",
                    "When, with um: ich gehe um sieben Uhr auf den Markt.",
                    "What. Apfel is der, and being the object is what moves it: Ich kaufe ___ Apfel und ein Hähnchen.",
                    "A question, then the sign-off: Brauchst du Reis? Bis später, Sanya.",
                ),
                2, listOf("den"),
                "The masculine is the only article the accusative moves, so it is the only one worth checking before you send a note.",
            ),
            B.writing("de4-p-write", "Write a short note to your partner (Schreiben part 2 style): say you are going shopping, what you are buying, and ask what they want. Use a greeting and a sign-off.",
                """
                Hallo Sanya, ich gehe um fünf Uhr in den Supermarkt. Ich kaufe Brot, Milch und ein Kilo Äpfel. Was möchtest du? Brauchst du Tee? Bis später, Usaid
                """,
                "Greeting", "When you go", "Two things you buy (accusative)", "A question to your partner", "Sign-off", minWords = 25),
            B.tryIt("Try it today", "Write your real shopping list in German with articles: der Reis, die Milch, das Brot.",
                "Ten items with articles", "Said the prices aloud", "Used einen for a masculine item", "Ordered a tea in German, out loud",
            ),
            B.glossary("Angebot" to "Offer.", "geöffnet / geschlossen" to "Open / closed.", "brauchen" to "To need."),
            B.recap("Notices: look for prices, hours and the word geschlossen.", "A note needs a greeting, the message, a question, a sign-off.", "Accusative shows in masculine objects only."),
        ),
    )

    val module = Module(
        id = "de-m4", number = 4, title = "Food and shopping", summary = "der, die, das, plurals, the accusative, prices and quantities.",
        examWeight = "A1", componentId = "listening", topics = listOf(words, grammar, dialogue, practice),
        quiz = Quiz(
            id = "de-m4-test", title = "Module 4 test", passPercent = 60,
            questions = listOf(
                B.q("de4-q1", "de4-words", "Hören: 'Das macht sieben Euro neunzig.' How much?", listOf("7,19 €", "7,90 €", "79 €"), setOf(1), "sieben Euro neunzig = 7,90 €.",
                whyWrong = mapOf(
                    0 to "neunzehn would be nineteen. neunzig is ninety, and the cents follow the euros with no word between them.",
                    2 to "That would be neunundsiebzig Euro.",
                ),
            ),
                B.q("de4-q2", "de4-grammar", "Lesen: 'Ich trinke ___ Kaffee.'", listOf("ein", "einen", "eine"), setOf(1), "Kaffee is der; accusative einen.",
                whyWrong = mapOf(
                    0 to "ein is the subject form. Here the coffee is being drunk, so it is the object.",
                    2 to "eine belongs to die-nouns. Kaffee is der.",
                ),
            ),
                B.q("de4-q3", "de4-grammar", "Which stays the same in the accusative?", listOf("der", "ein (masculine)", "das"), setOf(2), "Only the masculine changes.",
                whyWrong = mapOf(
                    0 to "der is precisely the one that changes, to den.",
                    1 to "einen is that change. The masculine is the only one that moves at all.",
                ),
            ),
                B.q("de4-q4", "de4-grammar", "Plural of Apfel", listOf("Apfels", "Äpfel", "Apfeln"), setOf(1), "Umlaut, no ending: die Äpfel.",
                whyWrong = mapOf(
                    0 to "German does not build plurals with -s, apart from a handful of borrowed words.",
                    2 to "-n is a dative plural ending rather than a plural.",
                ),
            ),
                B.q("de4-q5", "de4-grammar", "'I have no time' is", listOf("Ich habe nicht Zeit.", "Ich habe keine Zeit.", "Ich habe kein Zeit."), setOf(1), "Zeit is feminine, so the negative article is keine. nicht is for verbs and adjectives, not nouns.",
                whyWrong = mapOf(
                    0 to "nicht negates verbs and adjectives. A noun takes kein.",
                    2 to "The right word with the wrong ending: Zeit is die, so keine.",
                ),
            ),
                B.q("de4-q6", "de4-dialogue", "To order politely you say", listOf("Ich will …", "Ich hätte gern …", "Gib mir …"), setOf(1), "Ich hätte gern or Ich möchte.",
                whyWrong = mapOf(
                    0 to "wollen is blunter in German than I want is in English.",
                    2 to "That is give me, which is for a friend at best.",
                ),
            ),
                B.q("de4-q7", "de4-dialogue", "'Zusammen oder getrennt?' is asked when", listOf("ordering", "paying", "leaving"), setOf(1), "The waiter asks how to split the bill.",
                whyWrong = mapOf(
                    0 to "It comes at the end of the meal, when the bill is being settled.",
                    2 to "By the time you are leaving it has been paid.",
                ),
            ),
                B.q("de4-q8", "de4-practice", "Richtig oder falsch: 'geöffnet von 7 bis 22 Uhr' means open until 10 pm.", listOf("Richtig", "Falsch"), setOf(0), "22 Uhr is 10 pm. Richtig.",
                whyWrong = mapOf(
                    1 to "22 Uhr is ten at night.",
                ),
            ),
                B.q("de4-q9", "de4-words", "'eine Flasche Wasser': what is missing before Wasser?", listOf("von", "nothing", "des"), setOf(1), "Quantity + noun, no 'of'.",
                whyWrong = mapOf(
                    0 to "von is of in other senses, and a quantity does not take it.",
                    2 to "des is a genitive, which A1 does not need.",
                ),
            ),
                B.q("de4-q10", "de4-grammar", "'Was ___ du gern?' (essen)", listOf("esst", "isst", "essen"), setOf(1), "Vowel change: du isst.",
                whyWrong = mapOf(
                    0 to "esst is ihr.",
                    2 to "essen is wir and sie, and the infinitive.",
                ),
            ),
                B.q("de4-q11", "de4-words", "Which is neuter?", listOf("Apfel", "Milch", "Brot"), setOf(2), "Brot is neuter: das Brot. Apfel is der and Milch is die.",
                whyWrong = mapOf(
                    0 to "der Apfel.",
                    1 to "die Milch.",
                ),
            ),
                B.q("de4-q12", "de4-practice", "Schreiben: a note needs", listOf("only the message", "greeting, message, sign-off", "a long introduction"), setOf(1), "The exam marks greeting and sign-off as content points.",
                whyWrong = mapOf(
                    0 to "The greeting and the sign-off are content points. Leaving them off loses marks before the message is read.",
                    2 to "There are about thirty words to play with, so a history will not fit.",
                ),
            ),
            ),
        ),
    )
}
