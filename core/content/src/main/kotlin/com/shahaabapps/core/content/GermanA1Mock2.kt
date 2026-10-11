package com.shahaabapps.core.content

/** Second pool of German A1 mock questions, so a retake draws a different paper. */
internal object GermanA1Mock2 {
    val questions = listOf(
        // Hören Teil 1
        B.q("dm31", "de3-dialogue", "Hören Teil 1: Wann beginnt der Kurs?", listOf("um 17 Uhr", "um 18 Uhr", "um 19 Uhr"), setOf(1), "achtzehn Uhr is six in the evening; the other times are not said.", listen = "Der Deutschkurs beginnt am Montag um achtzehn Uhr und dauert zwei Stunden.",
            whyWrong = mapOf(
                0 to "siebzehn is seventeen. achtzehn was said, and the two sound alike at speed.",
                2 to "neunzehn is nineteen. The course runs two hours from eighteen, so nineteen is in the middle of it.",
            ),
        ),
        B.q("dm32", "de4-dialogue", "Hören Teil 1: Was bestellt die Frau?", listOf("einen Kaffee", "einen Tee mit Milch", "ein Wasser"), setOf(1), "Einen Tee mit Milch; the coffee is refused.", listen = "Kellner: Möchten Sie einen Kaffee? Frau: Nein danke, einen Tee mit Milch, bitte.",
            whyWrong = mapOf(
                0 to "The coffee is what the waiter offers, and “Nein danke” turns it down.",
                2 to "No water is mentioned.",
            ),
        ),
        B.q("dm33", "de2-dialogue", "Hören Teil 1: Woher kommt der Mann?", listOf("aus Indien", "aus Pakistan", "aus Katar"), setOf(1), "Ich komme aus Pakistan; Katar is where he lives now.", listen = "Ich komme aus Pakistan, aber ich wohne seit drei Jahren in Katar.",
            whyWrong = mapOf(
                0 to "India is never said. It is the country you might assume rather than the one given.",
                2 to "Katar is where he lives now, after “aber ich wohne”. The question asks woher, where from.",
            ),
        ),
        B.q("dm34", "de6-dialogue", "Hören Teil 1: Was ist die Frau von Beruf?", listOf("Ärztin", "Verkäuferin", "Lehrerin"), setOf(0), "Ich bin Ärztin; the school is where her sister works.", listen = "Ich bin Ärztin und arbeite im Krankenhaus. Meine Schwester ist Lehrerin an einer Schule.",
            whyWrong = mapOf(
                1 to "No shop assistant appears.",
                2 to "Lehrerin is her sister. Two jobs are named and only one of them is hers.",
            ),
        ),
        B.q("dm35", "de8-dialogue", "Hören Teil 1: Was soll der Patient nehmen?", listOf("Tabletten", "einen Tee", "nichts"), setOf(0), "Nehmen Sie diese Tabletten; tea is extra advice, not the prescription.", listen = "Nehmen Sie diese Tabletten zweimal am Tag und trinken Sie viel Tee.",
            whyWrong = mapOf(
                1 to "Tea is advice alongside the tablets, not the thing prescribed.",
                2 to "Something is prescribed, in the first two words.",
            ),
        ),
        B.q("dm36", "de9-dialogue", "Hören Teil 1: Was macht Tom am Samstag?", listOf("schwimmen", "Fußball spielen", "lernen"), setOf(1), "Fußball on Saturday; swimming is Sunday.", listen = "Am Samstag spiele ich Fußball mit Freunden, und am Sonntag gehe ich schwimmen.",
            whyWrong = mapOf(
                0 to "Swimming is Sunday, in the second half of the sentence.",
                2 to "No studying is mentioned.",
            ),
        ),
        // Hören Teil 2
        B.q("dm37", "de12-listening", "Hören Teil 2: Richtig oder falsch: Die Bibliothek ist am Samstag geschlossen.", listOf("Richtig", "Falsch"), setOf(1), "Saturday opening is 10 to 14 Uhr, so it is open. Falsch.", listen = "Die Bibliothek ist von Montag bis Freitag von zehn bis achtzehn Uhr geöffnet, am Samstag von zehn bis vierzehn Uhr.",
            whyWrong = mapOf(
                0 to "Saturday is named with its own hours, zehn bis vierzehn. Being open for less of the day is not being closed.",
            ),
        ),
        B.q("dm38", "de7-dialogue", "Hören Teil 2: Richtig oder falsch: Der Bus fährt heute nicht.", listOf("Richtig", "Falsch"), setOf(0), "Der Bus fällt heute aus means it is cancelled. Richtig.", listen = "Achtung: Der Bus nach Bonn fällt heute aus. Bitte nehmen Sie die Straßenbahn Linie drei.",
            whyWrong = mapOf(
                1 to "“fällt aus” is the phrase for a cancelled service, and the tram is offered instead.",
            ),
        ),
        B.q("dm39", "de12-listening", "Hören Teil 2: Richtig oder falsch: Das Schwimmbad kostet für Kinder fünf Euro.", listOf("Richtig", "Falsch"), setOf(1), "Children pay three euros; five is the adult price. Falsch.", listen = "Der Eintritt ins Schwimmbad kostet fünf Euro für Erwachsene und drei Euro für Kinder.",
            whyWrong = mapOf(
                0 to "Fünf Euro is the adult price. The two figures are in the same sentence, one each.",
            ),
        ),
        B.q("dm40", "de5-dialogue", "Hören Teil 2: Richtig oder falsch: Die Wohnung hat einen Balkon.", listOf("Richtig", "Falsch"), setOf(1), "Es gibt keinen Balkon, aber einen Garten. Falsch.", listen = "Die Wohnung hat drei Zimmer. Es gibt keinen Balkon, aber einen kleinen Garten.",
            whyWrong = mapOf(
                0 to "“keinen Balkon” is the denial; the Garten is what there is instead.",
            ),
        ),
        // Hören Teil 3
        B.q("dm41", "de10-dialogue", "Hören Teil 3: Warum kommt Lena nicht zum Essen?", listOf("Sie ist krank.", "Sie muss arbeiten.", "Sie hat keine Zeit am Samstag."), setOf(0), "Ich bin krank; she is ill.", listen = "Hallo, hier ist Lena. Ich kann heute leider nicht zum Essen kommen, ich bin krank. Vielleicht nächste Woche.",
            whyWrong = mapOf(
                1 to "No work is mentioned.",
                2 to "The call is about today, and the reason given is illness, not the diary.",
            ),
        ),
        B.q("dm42", "de8-dialogue", "Hören Teil 3: Wohin soll Herr Khan gehen?", listOf("in die Praxis", "in die Apotheke", "ins Krankenhaus"), setOf(1), "Gehen Sie in die Apotheke; the prescription is ready there.", listen = "Guten Tag, Herr Khan. Ihr Rezept ist fertig. Bitte gehen Sie in die Apotheke neben der Post.",
            whyWrong = mapOf(
                0 to "The Praxis is who is calling.",
                2 to "No hospital is mentioned. A Rezept is collected from an Apotheke.",
            ),
        ),
        B.q("dm43", "de7-dialogue", "Hören Teil 3: Wann fährt der nächste Zug?", listOf("um 14:20", "um 14:40", "um 15:20"), setOf(1), "vierzehn Uhr vierzig is 14:40.", listen = "Der nächste Zug nach Hamburg fährt um vierzehn Uhr vierzig von Gleis neun.",
            whyWrong = mapOf(
                0 to "vierzehn Uhr zwanzig would be 14:20. The minutes said were vierzig.",
                2 to "The hour is vierzehn, not fünfzehn.",
            ),
        ),
        B.q("dm44", "de6-dialogue", "Hören Teil 3: Was soll Frau Khan mitbringen?", listOf("ihren Pass", "ein Foto", "ihre Versichertenkarte"), setOf(2), "Bringen Sie Ihre Versichertenkarte mit.", listen = "Guten Tag, hier ist die Praxis. Bitte bringen Sie zu Ihrem Termin Ihre Versichertenkarte mit.",
            whyWrong = mapOf(
                0 to "No passport is asked for.",
                1 to "No photograph either. The Versichertenkarte is the one named.",
            ),
        ),
        B.q("dm45", "de9-dialogue", "Hören Teil 3: Wie ist das Wetter am Montag?", listOf("Sonne", "Regen", "Schnee"), setOf(2), "Schnee am Montag; rain is Sunday.", listen = "Am Sonntag regnet es den ganzen Tag. Am Montag kommt Schnee, nur minus zwei Grad.",
            whyWrong = mapOf(
                0 to "No sun appears in the forecast.",
                1 to "Rain is Sunday. Monday is the snow.",
            ),
        ),
        // Lesen
        B.q("dm46", "de11-reading", "Lesen: Richtig oder falsch: Der Kurs ist am Dienstag.", listOf("Richtig", "Falsch"), setOf(1), "The notice says Mittwoch, Wednesday. Falsch.", scenario = "Aushang: Deutschkurs A1. Jeden Mittwoch von 18 bis 20 Uhr, Raum 4. Anmeldung im Büro. Kosten: 60 Euro für zehn Termine.",
            whyWrong = mapOf(
                0 to "Mittwoch is Wednesday. Dienstag is the day before.",
            ),
        ),
        B.q("dm47", "de11-reading", "Lesen: Was kostet der Kurs?", listOf("18 Euro", "60 Euro", "10 Euro"), setOf(1), "60 Euro für zehn Termine.", scenario = "Aushang: Deutschkurs A1. Jeden Mittwoch von 18 bis 20 Uhr, Raum 4. Anmeldung im Büro. Kosten: 60 Euro für zehn Termine.",
            whyWrong = mapOf(
                0 to "18 is the hour the class starts, not a price.",
                2 to "Ten is the number of sessions the 60 Euro covers.",
            ),
        ),
        B.q("dm48", "de11-reading", "Lesen Teil 2: Sie möchten am Abend Sport machen. Welche Anzeige passt?", listOf("Anzeige a", "Anzeige b", "keine"), setOf(1), "a closes at 17 Uhr; b is open until 22 Uhr.", scenario = "a: Sportzentrum Nord, Montag bis Freitag 8 bis 17 Uhr.\nb: Fitnessstudio Süd, täglich 7 bis 22 Uhr, auch am Wochenende.",
            whyWrong = mapOf(
                0 to "Anzeige a closes at 17 Uhr, which is not the evening.",
                2 to "Anzeige b is open until 22 Uhr, so one of them does fit.",
            ),
        ),
        B.q("dm49", "de11-reading", "Lesen Teil 3: Richtig oder falsch: Man darf hier mit dem Fahrrad fahren.", listOf("Richtig", "Falsch"), setOf(1), "Fahrradfahren verboten means cycling is forbidden. Falsch.", scenario = "Schild im Park: Fahrradfahren verboten. Hunde bitte an der Leine. Geöffnet von 7 bis 21 Uhr.",
            whyWrong = mapOf(
                0 to "“verboten” is the word that settles it.",
            ),
        ),
        B.q("dm50", "de11-reading", "Lesen Teil 3: Bis wann ist der Park geöffnet?", listOf("bis 7 Uhr", "bis 21 Uhr", "bis 24 Uhr"), setOf(1), "Geöffnet von 7 bis 21 Uhr.", scenario = "Schild im Park: Fahrradfahren verboten. Hunde bitte an der Leine. Geöffnet von 7 bis 21 Uhr.",
            whyWrong = mapOf(
                0 to "7 Uhr is when it opens, not when it closes.",
                2 to "Midnight is not on the sign.",
            ),
        ),
    )
}
