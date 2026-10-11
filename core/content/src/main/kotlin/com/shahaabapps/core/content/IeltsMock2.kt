package com.shahaabapps.core.content

/** Second pool of IELTS mock questions, so a retake draws a different paper. */
internal object IeltsMock2 {
    private const val LIBRARY = "Membership of the city library is free to anyone living, working or studying in the district. Visitors from outside may join for an annual fee of 20 pounds. Members may borrow up to twelve items at a time, for three weeks, and may renew twice online unless another reader has reserved the item. Fines of 20 pence a day apply to overdue books but not to audio materials. The branch at Hill Road closes on Wednesday afternoons."

    private const val SLEEP = "For most of the twentieth century, researchers assumed that an unbroken eight-hour sleep was the human norm. Historical records suggest otherwise. Diaries, court papers and medical texts from before the industrial era refer casually to a first and a second sleep, separated by an hour or more of wakefulness in which people prayed, talked or visited neighbours. The historian who assembled this evidence argues that the consolidated night emerged only with cheap artificial lighting, which pushed bedtimes later and compressed the night. Sleep scientists remain divided: some regard segmented sleep as the natural pattern to which people revert in the absence of electric light, while others point out that the historical sources describe a practice rather than a physiological need."

    val questions = listOf(
        // Listening
        B.q("im41", "ie2-details", "Listening Part 1: What is the caller's postcode?", listOf("BN1 4GH", "BN4 1GH", "BM1 4GH"), setOf(0), "B-N-one, four-G-H is read out in the usual two groups.", listen = "My postcode is B N one, four G H.",
            whyWrong = mapOf(
                1 to "The one comes before the four. B-N-one, then four-G-H, in the two groups they are read in.",
                2 to "The second letter is N, not M. The two sound alike and only one was said.",
            ),
        ),
        B.q("im42", "ie2-traps", "Listening Part 1: How many people will attend?", listOf("eight", "eighteen", "eighty"), setOf(1), "Eighteen, corrected from eight.", listen = "There'll be eight of us. Sorry, eighteen. I forgot the second group.",
            whyWrong = mapOf(
                0 to "Eight is the figure before the correction.",
                2 to "Eighty is neither figure. It is what you write if you catch only the ending.",
            ),
        ),
        B.q("im43", "ie2-parts", "Listening Part 1: Which service does the caller want?", listOf("a repair", "a replacement", "a refund"), setOf(0), "She asks for it to be repaired rather than replaced.", listen = "I don't want a new one, and I'd rather not have my money back. I'd just like it repaired if that's possible.",
            whyWrong = mapOf(
                1 to "“I don’t want a new one” rules this out in the first clause.",
                2 to "“I’d rather not have my money back” rules this out in the second.",
            ),
        ),
        B.q("im44", "ie2-details", "Listening Part 2: What time does the last tour leave?", listOf("3.30", "4.15", "4.30"), setOf(2), "Half past four is the last departure.", listen = "Tours leave every forty-five minutes, and the last one of the day sets off at half past four.",
            whyWrong = mapOf(
                0 to "Half past three is not mentioned at all.",
                1 to "Quarter past four would be a tour, but not the last one. The question asks for the last.",
            ),
        ),
        B.q("im45", "ie2-traps", "Listening Part 2: What is not permitted in the building?", listOf("photography", "food and drink", "large bags"), setOf(1), "Photography is allowed without flash; food and drink are not allowed at all.", listen = "You're welcome to take photographs, provided you turn the flash off. Large bags can be left at the desk. Food and drink, though, are not permitted anywhere inside.",
            whyWrong = mapOf(
                0 to "Photography is allowed, with the flash off. A condition is not a prohibition.",
                2 to "Large bags are allowed too, once they are left at the desk.",
            ),
        ),
        B.q("im46", "ie2-parts", "Listening Part 3: What do the students disagree about?", listOf("the topic", "the length of the presentation", "who speaks first"), setOf(1), "One wants fifteen minutes, the other twenty; they agree on topic and order.", listen = "Student A: Fifteen minutes should be plenty. Student B: I really think we need twenty to cover the data. Student A: Well, let's ask the tutor. At least we agree on the topic, and you're happy to start.",
            whyWrong = mapOf(
                0 to "“At least we agree on the topic” settles that one.",
                2 to "“You’re happy to start” settles the order as well.",
            ),
        ),
        B.q("im47", "ie2-parts", "Listening Part 3: What does the tutor suggest they add?", listOf("more statistics", "a case study", "a longer introduction"), setOf(1), "A single case study to anchor the argument.", listen = "Tutor: The argument is sound, but it's abstract. I'd add one case study, something concrete the audience can hold on to.",
            whyWrong = mapOf(
                0 to "The tutor’s complaint is that it is abstract, which more statistics would not fix.",
                2 to "The introduction is not mentioned.",
            ),
        ),
        B.q("im48", "ie2-details", "Listening Part 4: Coral reefs cover what share of the ocean floor?", listOf("less than one percent", "about ten percent", "roughly a quarter"), setOf(0), "Less than one percent, yet they support a quarter of marine species.", listen = "Coral reefs occupy less than one percent of the ocean floor, and yet they support something close to a quarter of all marine species.",
            whyWrong = mapOf(
                1 to "Ten percent is not a figure given.",
                2 to "A quarter is the share of marine species, not the share of the floor. The two numbers are in the same sentence on purpose.",
            ),
        ),
        B.q("im49", "ie2-details", "Listening Part 4: What does the speaker say is the greatest threat to reefs?", listOf("fishing", "rising sea temperature", "tourism"), setOf(1), "Warming water causes the bleaching described as the greatest threat.", listen = "Overfishing and tourism both do damage, but the greatest threat by some distance is rising sea temperature, which triggers bleaching.",
            whyWrong = mapOf(
                0 to "Fishing does damage and is named, which is what makes it tempting. It is not the greatest threat.",
                2 to "Tourism is named in the same breath, and for the same reason.",
            ),
        ),
        B.q("im50", "ie2-traps", "Listening Part 4: How long can a bleached reef take to recover?", listOf("a year", "a decade or more", "it never recovers"), setOf(1), "Ten to fifteen years if conditions allow, so a decade or more.", listen = "A bleached reef is not necessarily dead. Given cool water and clean conditions it can recover, although that usually takes ten to fifteen years.",
            whyWrong = mapOf(
                0 to "A year is far short of the ten to fifteen given.",
                2 to "“Not necessarily dead” is the first thing said about it.",
            ),
        ),
        // Reading: library notice, General Training style
        B.q("im51", "ie3-timing", "Reading: TRUE, FALSE or NOT GIVEN: Library membership is free for people who work in the district.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(0), "Free to anyone living, working or studying in the district.", scenario = LIBRARY,
            whyWrong = mapOf(
                1 to "Working in the district is one of the three ways named.",
                2 to "It is addressed in the first sentence.",
            ),
        ),
        B.q("im52", "ie3-timing", "Reading: TRUE, FALSE or NOT GIVEN: Members may borrow twenty items at once.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(1), "The limit is twelve items, so twenty contradicts the text.", scenario = LIBRARY,
            whyWrong = mapOf(
                0 to "The limit is twelve. Twenty is not a number the notice uses.",
                2 to "A limit is given, which is what makes this a contradiction.",
            ),
        ),
        B.q("im53", "ie3-timing", "Reading: TRUE, FALSE or NOT GIVEN: Fines apply to overdue audio materials.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(1), "Fines apply to books but not to audio materials.", scenario = LIBRARY,
            whyWrong = mapOf(
                0 to "Fines are named, but “not to audio materials” is the second half of the sentence.",
                2 to "Audio materials are mentioned, so this is settled rather than absent.",
            ),
        ),
        B.q("im54", "ie3-timing", "Reading: How many times may a member renew an item online?", listOf("once", "twice", "three times"), setOf(1), "May renew twice, unless the item is reserved.", scenario = LIBRARY,
            whyWrong = mapOf(
                0 to "Once is less than the notice allows.",
                2 to "Three is more. Twice is the figure, and only if nobody has reserved it.",
            ),
        ),
        B.q("im55", "ie3-timing", "Reading: TRUE, FALSE or NOT GIVEN: The Hill Road branch is open all day on Wednesday.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(1), "It closes on Wednesday afternoons.", scenario = LIBRARY,
            whyWrong = mapOf(
                0 to "Wednesday afternoons are when it closes.",
                2 to "The last sentence is about exactly this.",
            ),
        ),
        // Reading: academic passage
        B.q("im56", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: Before industrialisation, people commonly slept in two periods.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(0), "The historical sources refer casually to a first and a second sleep.", scenario = SLEEP,
            whyWrong = mapOf(
                1 to "The sources “refer casually to a first and a second sleep”, which is this statement.",
                2 to "It is given, and at length.",
            ),
        ),
        B.q("im57", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: All sleep scientists accept that segmented sleep is the natural human pattern.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(1), "The passage says scientists remain divided, which contradicts 'all'.", scenario = SLEEP,
            whyWrong = mapOf(
                0 to "“Sleep scientists remain divided” is incompatible with “all”.",
                2 to "The division is described, so there is something to contradict.",
            ),
        ),
        B.q("im58", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: Segmented sleep made people more productive during the day.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(2), "Daytime productivity is never mentioned.", scenario = SLEEP,
            whyWrong = mapOf(
                0 to "Nothing is said about the daytime at all. The waking hour is described as prayer, talk and visits, not as its effect the next day.",
                1 to "FALSE would need the passage to deny it. It never raises it.",
            ),
        ),
        B.q("im59", "ie3-headings", "Reading: According to the historian, what ended the two-part night?", listOf("longer working hours", "cheap artificial lighting", "changes in architecture"), setOf(1), "Cheap artificial lighting pushed bedtimes later and compressed the night.", scenario = SLEEP,
            whyWrong = mapOf(
                0 to "Working hours are not mentioned.",
                2 to "Nor is architecture. The cause given is cheap light.",
            ),
        ),
        B.q("im60", "ie3-headings", "Reading: Which heading best fits the passage?", listOf("How electricity was invented", "A forgotten way of sleeping, and the argument about it", "Why eight hours of sleep is essential"), setOf(1), "The passage describes the historical practice and the unresolved debate around it.", scenario = SLEEP,
            whyWrong = mapOf(
                0 to "Electricity appears in one clause, as a cause, not as the subject.",
                2 to "The passage questions the eight-hour night rather than arguing for it.",
            ),
        ),
    )
}
