package com.shahaabapps.core.content

/**
 * German A1 mock exam: the Hören and Lesen papers in exam shape, 30 questions,
 * timed like the real sitting (about 20 + 25 minutes), no feedback until the
 * end, pass at 60 percent like the real exam. Schreiben and Sprechen are
 * rehearsed in Module 11 and Module 12 with the marker and the recogniser.
 */
internal object GermanA1Mock {
    val quiz = Quiz(
        id = "de-mock", title = "Goethe A1 mock: Hören and Lesen", passPercent = 60, timeLimitMinutes = 45, instantFeedback = false, drawCount = 30,
        questions = B.spread(listOf(
            // Hören Teil 1: dialogues, three options
            B.q("dm1", "de12-listening", "Hören Teil 1: Wann kommt Sanya an?", listOf("um 14:15", "um 14:50", "um 15:15"), setOf(1), "Vierzehn Uhr fünfzig: the second time given replaces the first.", listen = "Usaid: Wann kommst du morgen an? Sanya: Um Viertel nach zwei. Nein, Moment, der Zug ist später, um vierzehn Uhr fünfzig. Usaid: Gut, ich hole dich ab.",
                whyWrong = mapOf(
                    0 to "Viertel nach zwei is the time she says first, then takes back with “Nein, Moment”.",
                    2 to "The hour stays vierzehn. fünfzehn Uhr fünfzehn would be quarter past three.",
                ),
            ),
            B.q("dm2", "de12-listening", "Hören Teil 1: Was kauft die Frau?", listOf("ein Kilo Äpfel und Brot", "zwei Kilo Äpfel", "Brot und Milch"), setOf(0), "Ein Kilo Äpfel und ein Brot; the milk is sold out.", listen = "Verkäufer: Was darf es sein? Frau: Ein Kilo Äpfel und ein Brot, bitte. Haben Sie auch Milch? Verkäufer: Nein, die Milch ist leider aus. Frau: Dann nur die Äpfel und das Brot.",
                whyWrong = mapOf(
                    1 to "One kilo, not two, and the bread is bought as well.",
                    2 to "The milk is “leider aus”, sold out, which is why she settles for the other two.",
                ),
            ),
            B.q("dm3", "de7-dialogue", "Hören Teil 1: Von welchem Gleis fährt der Zug?", listOf("Gleis 3", "Gleis 8", "Gleis 13"), setOf(1), "Gleis acht; Gleis drei is where the other train goes.", listen = "Mann: Entschuldigung, der Zug nach Berlin, von welchem Gleis? Frau: Der Zug nach Berlin fährt von Gleis acht. Gleis drei ist der Zug nach Hamburg.",
                whyWrong = mapOf(
                    0 to "Gleis drei is the Hamburg train. Both numbers are said, one each.",
                    2 to "dreizehn is thirteen. acht was said, and drei belongs to the other train.",
                ),
            ),
            B.q("dm4", "de8-dialogue", "Hören Teil 1: Was fehlt dem Mann?", listOf("Er hat Kopfschmerzen.", "Er hat Bauchschmerzen.", "Er hat Fieber."), setOf(1), "Bauchschmerzen; he says no fever.", listen = "Ärztin: Guten Tag, was fehlt Ihnen? Mann: Ich habe seit gestern Bauchschmerzen. Ärztin: Haben Sie Fieber? Mann: Nein, kein Fieber.",
                whyWrong = mapOf(
                    0 to "No headache is mentioned.",
                    2 to "“Nein, kein Fieber” is the answer to exactly that question.",
                ),
            ),
            B.q("dm5", "de5-dialogue", "Hören Teil 1: Wo ist die Apotheke?", listOf("neben der Bank", "neben der Post", "am Bahnhof"), setOf(1), "Rechts and then neben der Post.", listen = "Frau: Entschuldigung, wo ist hier eine Apotheke? Mann: Gehen Sie die erste Straße rechts, die Apotheke ist neben der Post. Frau: Danke schön.",
                whyWrong = mapOf(
                    0 to "No bank appears.",
                    2 to "No station either. The landmark given is the Post.",
                ),
            ),
            B.q("dm6", "de9-dialogue", "Hören Teil 1: Was machen sie am Sonntag?", listOf("schwimmen", "ins Kino gehen", "kochen"), setOf(1), "Sunday is the cinema; swimming is Saturday.", listen = "Lena: Gehen wir am Samstag schwimmen? Tom: Ja, gern. Und am Sonntag? Lena: Am Sonntag gehen wir ins Kino, um sieben. Tom: Super.",
                whyWrong = mapOf(
                    0 to "Swimming is Samstag, in the first line.",
                    2 to "No cooking is mentioned.",
                ),
            ),
            // Hören Teil 2: announcements, Richtig/Falsch
            B.q("dm7", "de12-listening", "Hören Teil 2: Richtig oder falsch: Der Supermarkt schließt um 20 Uhr.", listOf("Richtig", "Falsch"), setOf(1), "The shop closes at 21 Uhr today, not 20.", listen = "Liebe Kundinnen und Kunden, unser Markt schließt heute um einundzwanzig Uhr. Bitte gehen Sie zur Kasse.",
                whyWrong = mapOf(
                    0 to "einundzwanzig is twenty-one, so 21 Uhr. 20 would be zwanzig.",
                ),
            ),
            B.q("dm8", "de7-dialogue", "Hören Teil 2: Richtig oder falsch: Der Flug nach Doha hat Verspätung.", listOf("Richtig", "Falsch"), setOf(0), "vierzig Minuten Verspätung. Richtig.", listen = "Achtung, Passagiere des Fluges QR 70 nach Doha: Der Flug hat heute vierzig Minuten Verspätung. Das Boarding beginnt um einundzwanzig Uhr.",
                whyWrong = mapOf(
                    1 to "“vierzig Minuten Verspätung” is the delay, stated outright.",
                ),
            ),
            B.q("dm9", "de12-listening", "Hören Teil 2: Richtig oder falsch: Das Konzert ist im Park.", listOf("Richtig", "Falsch"), setOf(1), "nicht im Park, sondern in der Stadthalle. Falsch.", listen = "Liebe Zuhörer, das Konzert am Freitag ist wegen Regen nicht im Park, sondern in der Stadthalle. Beginn ist um zwanzig Uhr.",
                whyWrong = mapOf(
                    0 to "“nicht im Park, sondern in der Stadthalle” moves it. The Park is named only to say it is not there.",
                ),
            ),
            B.q("dm10", "de6-dialogue", "Hören Teil 2: Richtig oder falsch: Der Deutschkurs am Montag fällt aus.", listOf("Richtig", "Falsch"), setOf(0), "Der Kurs am Montag fällt aus. Richtig.", listen = "Eine Durchsage der Sprachschule: Der Deutschkurs am Montag fällt aus. Der nächste Kurs ist am Mittwoch um achtzehn Uhr.",
                whyWrong = mapOf(
                    1 to "“fällt aus” is the phrase for a cancelled class, and a replacement date follows.",
                ),
            ),
            // Hören Teil 3: phone messages, three options
            B.q("dm11", "de8-dialogue", "Hören Teil 3: Wann ist der Termin?", listOf("Dienstag um 10", "Donnerstag um 10", "Donnerstag um 11"), setOf(1), "Donnerstag um zehn, after the correction.", listen = "Guten Tag, hier ist die Praxis Dr. Müller. Ihr Termin am Dienstag geht leider nicht. Können Sie am Donnerstag um zehn Uhr kommen? Bitte rufen Sie zurück.",
                whyWrong = mapOf(
                    0 to "Dienstag is the appointment that “geht leider nicht”.",
                    2 to "The hour offered is zehn, not elf.",
                ),
            ),
            B.q("dm12", "de10-dialogue", "Hören Teil 3: Warum kommt Lena nicht?", listOf("Sie ist krank.", "Sie muss arbeiten.", "Der Zug hat Verspätung."), setOf(1), "Sie muss arbeiten; she is not ill.", listen = "Hallo Sanya, hier ist Lena. Ich kann heute Abend leider nicht kommen, ich muss bis neun arbeiten. Wir sehen uns am Samstag. Tschüss.",
                whyWrong = mapOf(
                    0 to "Lena is not ill here. She gives work as the reason.",
                    2 to "No train is mentioned.",
                ),
            ),
            B.q("dm13", "de5-dialogue", "Hören Teil 3: Wie hoch ist die Miete?", listOf("550 Euro", "650 Euro", "560 Euro"), setOf(1), "sechshundertfünfzig is six hundred and fifty; listen for the hundreds first.", listen = "Guten Tag, Herr Shahab, hier Weber wegen der Wohnung. Die Miete ist sechshundertfünfzig Euro plus Nebenkosten. Sie können die Wohnung am Samstag um elf sehen.",
                whyWrong = mapOf(
                    0 to "fünfhundertfünfzig would be 550. The hundreds said were sechshundert.",
                    2 to "560 mishears the fünfzig as the tens of a different hundred. Catch the hundreds first.",
                ),
            ),
            B.q("dm14", "de7-dialogue", "Hören Teil 3: Wo treffen sie sich?", listOf("am Bahnhof", "vor dem Kino", "im Café"), setOf(1), "nicht am Bahnhof, sondern vor dem Kino.", listen = "Hallo Tom, hier Usaid. Wir treffen uns heute nicht am Bahnhof, sondern vor dem Kino, um halb acht. Bis später.",
                whyWrong = mapOf(
                    0 to "The Bahnhof is named only to say “nicht am Bahnhof”.",
                    2 to "No café is mentioned.",
                ),
            ),
            B.q("dm15", "de9-dialogue", "Hören Teil 3: Wie ist das Wetter am Samstag?", listOf("Regen", "Sonne und 25 Grad", "Schnee"), setOf(1), "Samstag sonnig, fünfundzwanzig Grad; the rain is Sunday.", listen = "Das Wetter am Wochenende: Am Samstag scheint die Sonne, fünfundzwanzig Grad. Am Sonntag regnet es.",
                whyWrong = mapOf(
                    0 to "Rain is Sunday, in the second sentence.",
                    2 to "No snow appears in this forecast.",
                ),
            ),
            // Lesen Teil 1: two texts, Richtig/Falsch
            B.q("dm16", "de11-reading", "Lesen Teil 1: Richtig oder falsch: Usaid zieht am Sonntag um.", listOf("Richtig", "Falsch"), setOf(1), "Am Samstag, not Sunday. Falsch.", scenario = "Hallo Tom, am Samstag ziehen wir um! Die neue Wohnung ist in der Bahnhofstraße, im dritten Stock. Kannst du uns helfen? Wir fangen um 9 Uhr an. Am Abend gibt es Pizza für alle. Bring bitte dein Auto mit. Viele Grüße, Usaid",
                whyWrong = mapOf(
                    0 to "The note says Samstag. Sonntag is the day after.",
                ),
            ),
            B.q("dm17", "de11-reading", "Lesen Teil 1: Richtig oder falsch: Die Wohnung ist im dritten Stock.", listOf("Richtig", "Falsch"), setOf(0), "im dritten Stock. Richtig.", scenario = "Hallo Tom, am Samstag ziehen wir um! Die neue Wohnung ist in der Bahnhofstraße, im dritten Stock. Kannst du uns helfen? Wir fangen um 9 Uhr an. Am Abend gibt es Pizza für alle. Bring bitte dein Auto mit. Viele Grüße, Usaid",
                whyWrong = mapOf(
                    1 to "“im dritten Stock” is in the first line of the note.",
                ),
            ),
            B.q("dm18", "de11-reading", "Lesen Teil 1: Richtig oder falsch: Tom soll etwas zu essen mitbringen.", listOf("Richtig", "Falsch"), setOf(1), "He should bring his car; the pizza is provided. Falsch.", scenario = "Hallo Tom, am Samstag ziehen wir um! Die neue Wohnung ist in der Bahnhofstraße, im dritten Stock. Kannst du uns helfen? Wir fangen um 9 Uhr an. Am Abend gibt es Pizza für alle. Bring bitte dein Auto mit. Viele Grüße, Usaid",
                whyWrong = mapOf(
                    0 to "He is asked to bring his car. “Pizza für alle” says the food is already arranged.",
                ),
            ),
            B.q("dm19", "de10-practice", "Lesen Teil 1: Richtig oder falsch: Sanya ist mit dem Zug nach Hamburg gekommen.", listOf("Richtig", "Falsch"), setOf(0), "mit dem Zug gekommen. Richtig.", scenario = "Liebe Lena, viele Grüße aus Hamburg! Wir sind am Freitag mit dem Zug gekommen. Am Samstag haben wir den Hafen gesehen. Das Wetter war leider schlecht. Am Montag fliegen wir zurück nach Doha. Bis bald, Sanya",
                whyWrong = mapOf(
                    1 to "“mit dem Zug gekommen” is how they arrived; the flight is the way home.",
                ),
            ),
            B.q("dm20", "de10-practice", "Lesen Teil 1: Richtig oder falsch: Das Wetter in Hamburg war gut.", listOf("Richtig", "Falsch"), setOf(1), "leider schlecht. Falsch.", scenario = "Liebe Lena, viele Grüße aus Hamburg! Wir sind am Freitag mit dem Zug gekommen. Am Samstag haben wir den Hafen gesehen. Das Wetter war leider schlecht. Am Montag fliegen wir zurück nach Doha. Bis bald, Sanya",
                whyWrong = mapOf(
                    0 to "“leider schlecht” is the opposite.",
                ),
            ),
            // Lesen Teil 2: situations and adverts
            B.q("dm21", "de11-reading", "Lesen Teil 2: Sie möchten am Sonntag mit Kindern schwimmen gehen. Welche Anzeige passt?", listOf("Anzeige a", "Anzeige b", "keine"), setOf(1), "a is closed on Sunday; b is open daily with a children's pool.", scenario = "a: Schwimmbad Nord, Montag bis Samstag 7 bis 21 Uhr, Sonntag geschlossen.\nb: Freizeitbad Süd, täglich 9 bis 20 Uhr, Kinderbecken, Familienkarte 15 Euro.",
                whyWrong = mapOf(
                    0 to "Anzeige a is “Sonntag geschlossen”.",
                    2 to "Anzeige b is open daily and has a Kinderbecken, so one of them does fit.",
                ),
            ),
            B.q("dm22", "de5-practice", "Lesen Teil 2: Sie suchen eine Wohnung für maximal 600 Euro in der Nähe vom Bahnhof. Welche Anzeige passt?", listOf("Anzeige a", "Anzeige b", "keine"), setOf(0), "a is 580 euros and five minutes from the station; b costs 950.", scenario = "a: 2-Zimmer-Wohnung, 55 qm, 580 Euro + NK, 5 Minuten zum Bahnhof.\nb: 3-Zimmer-Wohnung im Zentrum, Balkon, 950 Euro + NK.",
                whyWrong = mapOf(
                    1 to "950 Euro is over the 600 limit, whatever else it offers.",
                    2 to "Anzeige a is 580 and five minutes from the station, so one of them does fit.",
                ),
            ),
            B.q("dm23", "de7-practice", "Lesen Teil 2: Sie wollen am Samstag nach Berlin, ohne umsteigen. Welche Verbindung?", listOf("Verbindung a", "Verbindung b", "keine"), setOf(1), "Only connection b is marked direkt, so no change of train.", scenario = "a: Abfahrt 8:15, Ankunft 12:40, umsteigen in Hannover, 59 Euro.\nb: Abfahrt 9:00, Ankunft 12:10, direkt, 89 Euro.",
                whyWrong = mapOf(
                    0 to "Verbindung a changes in Hannover, which is what “umsteigen” means.",
                    2 to "Verbindung b is marked direkt, so one of them does fit.",
                ),
            ),
            B.q("dm24", "de6-practice", "Lesen Teil 2: Sie möchten abends Deutsch lernen. Welcher Kurs passt?", listOf("Kurs a", "Kurs b", "keiner"), setOf(0), "a is in the evening; b is in the morning.", scenario = "a: Deutsch A1, Montag und Mittwoch 18 bis 20 Uhr, Sprachschule Mitte.\nb: Deutsch A1 Intensiv, Montag bis Freitag 9 bis 12 Uhr.",
                whyWrong = mapOf(
                    1 to "Kurs b runs 9 bis 12 Uhr, which is the morning.",
                    2 to "Kurs a is 18 bis 20 Uhr, so one of them does fit.",
                ),
            ),
            B.q("dm25", "de4-practice", "Lesen Teil 2: Sie möchten am Sonntag Brot kaufen. Welche Anzeige passt?", listOf("Anzeige a", "Anzeige b", "keine"), setOf(1), "a is closed on Sunday; b opens Sunday morning.", scenario = "a: Bäckerei Schmidt, Montag bis Samstag 6 bis 18 Uhr, Sonntag geschlossen.\nb: Bäckerei am Markt, täglich geöffnet, Sonntag 7 bis 11 Uhr.",
                whyWrong = mapOf(
                    0 to "Anzeige a is “Sonntag geschlossen”.",
                    2 to "Anzeige b opens Sunday morning, so one of them does fit.",
                ),
            ),
            // Lesen Teil 3: notices, Richtig/Falsch
            B.q("dm26", "de11-reading", "Lesen Teil 3: Richtig oder falsch: Man darf hier parken, wenn man kein Kunde ist.", listOf("Richtig", "Falsch"), setOf(1), "nur für Kunden. Falsch.", scenario = "Schild am Supermarkt: Parken nur für Kunden. Maximal 2 Stunden.",
                whyWrong = mapOf(
                    0 to "“nur für Kunden” is the whole of the sign.",
                ),
            ),
            B.q("dm27", "de8-practice", "Lesen Teil 3: Richtig oder falsch: Am Mittwochnachmittag ist die Praxis offen.", listOf("Richtig", "Falsch"), setOf(1), "Mittwochnachmittag geschlossen. Falsch.", scenario = "Praxis Dr. Müller. Sprechzeiten: Montag bis Freitag 8 bis 12 Uhr. Montag und Donnerstag auch 15 bis 18 Uhr. Mittwochnachmittag geschlossen.",
                whyWrong = mapOf(
                    0 to "The last line names Mittwochnachmittag as the closed one. The afternoon hours are Montag and Donnerstag.",
                ),
            ),
            B.q("dm28", "de6-practice", "Lesen Teil 3: Richtig oder falsch: Man kann Frau Braun anrufen.", listOf("Richtig", "Falsch"), setOf(0), "Wer Fragen hat, kann Frau Braun anrufen. Richtig.", scenario = "Aushang: Am Freitag ist das Büro ab 13 Uhr geschlossen. Wer Fragen hat, kann Frau Braun anrufen: 0221 334455.",
                whyWrong = mapOf(
                    1 to "The notice gives her name and a number precisely so that people can ring.",
                ),
            ),
            B.q("dm29", "de11-reading", "Lesen Teil 3: Richtig oder falsch: In der Bibliothek darf man essen.", listOf("Richtig", "Falsch"), setOf(1), "Essen und Trinken verboten. Falsch.", scenario = "Bibliothek. Öffnungszeiten: Montag bis Freitag 10 bis 18 Uhr. Essen und Trinken verboten.",
                whyWrong = mapOf(
                    0 to "“Essen und Trinken verboten” is the second line.",
                ),
            ),
            B.q("dm30", "de7-dialogue", "Lesen Teil 3: Richtig oder falsch: Der Zug nach Bonn fährt heute von Gleis 5.", listOf("Richtig", "Falsch"), setOf(0), "heute von Gleis 5. Richtig.", scenario = "Anzeige am Bahnhof: Regionalzug nach Bonn, 10:32, heute Gleis 5 (nicht Gleis 2).",
                whyWrong = mapOf(
                    1 to "“heute Gleis 5” is the notice, with Gleis 2 named as what it is not.",
                ),
            ),
        ) + GermanA1Mock2.questions),
    )
}
