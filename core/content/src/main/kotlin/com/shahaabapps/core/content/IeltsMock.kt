package com.shahaabapps.core.content

/**
 * IELTS mock: the Listening and Reading papers in exam shape, 40 questions,
 * timed at 90 minutes, no feedback until the end. Pass is set at 75 percent,
 * which is 30 of 40 and the band 7 threshold in Listening. Writing and
 * Speaking are rehearsed with the marker in modules 4 to 7.
 */
internal object IeltsMock {
    val quiz = Quiz(
        id = "ie-mock", title = "IELTS mock: Listening and Reading", passPercent = 75, timeLimitMinutes = 90, instantFeedback = false, drawCount = 40,
        questions = B.spread(listOf(
            // ---------------------------------------------------------------- Listening Part 1
            B.q("im1", "ie2-parts", "Listening Part 1: What is the caller's surname?", listOf("Haywood", "Heywood", "Hayward"), setOf(2), "Spelt H-A-Y-W-A-R-D in the recording.", listen = "Receptionist: Could I take your name please? Caller: It's Hayward. That's H-A-Y-W-A-R-D.",
                whyWrong = mapOf(
                    0 to "Haywood ends in -wood. The letters given finish A-R-D.",
                    1 to "Heywood changes the vowel as well. Follow the letters as they are said, not the name you expect.",
                ),
            ),
            B.q("im2", "ie2-details", "Listening Part 1: What is the membership number?", listOf("4017", "4070", "4701"), setOf(1), "Four oh seven oh is 4070.", listen = "Your membership number is four, oh, seven, oh. I'll repeat that: four zero seven zero.",
                whyWrong = mapOf(
                    0 to "The last two digits are seven then oh. This reverses them.",
                    2 to "This moves the oh into the middle. Digits dictated one at a time have to be written as they arrive.",
                ),
            ),
            B.q("im3", "ie2-traps", "Listening Part 1: How much does the annual membership cost?", listOf("40 pounds", "45 pounds", "55 pounds"), setOf(2), "The speaker corrects forty-five to fifty-five for the annual rate.", listen = "The monthly rate is forty-five pounds. Sorry, that's the monthly one. The annual membership works out at fifty-five pounds.",
                whyWrong = mapOf(
                    0 to "Forty is never said. It is the figure you write if you half-hear forty-five.",
                    1 to "Forty-five is the monthly rate, and the speaker corrects herself. The question asks for the annual one.",
                ),
            ),
            B.q("im4", "ie2-parts", "Listening Part 1: Which day does the caller choose?", listOf("Tuesday", "Wednesday", "Thursday"), setOf(1), "Tuesday is full and Thursday is too late, so Wednesday is chosen.", listen = "Receptionist: We have space on Tuesday. Caller: Tuesday's difficult. Receptionist: Then Wednesday or Thursday. Caller: Thursday would be too late for me, so let's say Wednesday.",
                whyWrong = mapOf(
                    0 to "Tuesday is offered and refused: “Tuesday's difficult”.",
                    2 to "Thursday is offered too, and ruled out as too late.",
                ),
            ),
            B.q("im5", "ie2-details", "Listening Part 1: What time does the session start?", listOf("6.15", "6.50", "7.15"), setOf(0), "Quarter past six is 6.15.", listen = "The evening session starts at quarter past six and runs for ninety minutes.",
                whyWrong = mapOf(
                    1 to "Quarter past is fifteen minutes, not fifty. 6.50 would be ten to seven.",
                    2 to "The hour is six. Quarter past six, not quarter past seven.",
                ),
            ),
            B.q("im6", "ie2-parts", "Listening Part 1: What must the caller bring?", listOf("a passport", "a photograph", "a towel"), setOf(1), "A recent photograph for the card; a towel is provided.", listen = "Please bring a recent photograph for your membership card. Towels are provided, so there's no need for your own.",
                whyWrong = mapOf(
                    0 to "No passport is mentioned. It is the document you expect to need rather than the one asked for.",
                    2 to "Towels are mentioned, and mentioned in order to say they are provided.",
                ),
            ),
            // ---------------------------------------------------------------- Listening Part 2
            B.q("im7", "ie2-details", "Listening Part 2: Where is the café?", listOf("next to the main entrance", "opposite the library", "at the end of the corridor"), setOf(1), "Directly opposite the library; the entrance is the reference point only.", listen = "As you come in through the main entrance, the library is on your left, and the café is directly opposite it.",
                whyWrong = mapOf(
                    0 to "The entrance is where you are standing, not where the café is.",
                    2 to "No corridor appears here. That is a direction from another question set.",
                ),
            ),
            B.q("im8", "ie2-traps", "Listening Part 2: The gallery is currently", listOf("open as usual", "closed for refurbishment", "open at weekends only"), setOf(1), "Closed for refurbishment until spring.", listen = "I should mention that the upper gallery is closed at the moment for refurbishment, and won't reopen until the spring.",
                whyWrong = mapOf(
                    0 to "The whole sentence exists to say otherwise.",
                    2 to "No weekend opening is offered. It is closed until spring, not open part of the time.",
                ),
            ),
            B.q("im9", "ie2-parts", "Listening Part 2: What is free for visitors?", listOf("parking", "the guided tour", "the audio guide"), setOf(2), "Audio guides are free; parking is charged and the tour costs five pounds.", listen = "Audio guides are available free of charge at the desk. The guided tour is five pounds, and parking in the visitor car park costs three pounds for the day.",
                whyWrong = mapOf(
                    0 to "Parking is three pounds for the day.",
                    1 to "The tour is five pounds. All three are named and only one of them is free.",
                ),
            ),
            B.q("im10", "ie2-details", "Listening Part 2: How long does the guided tour last?", listOf("45 minutes", "an hour and a quarter", "two hours"), setOf(1), "An hour and a quarter is 75 minutes.", listen = "The guided tour leaves at eleven and lasts an hour and a quarter, so you'll be back by quarter past twelve.",
                whyWrong = mapOf(
                    0 to "Forty-five minutes is three quarters of an hour, not an hour and a quarter.",
                    2 to "Eleven until quarter past twelve is an hour and a quarter.",
                ),
            ),
            // ---------------------------------------------------------------- Listening Part 3
            B.q("im11", "ie2-traps", "Listening Part 3: What do the students decide to change about their project?", listOf("the topic", "the method of collecting data", "the deadline"), setOf(1), "They keep the topic and move from interviews to an online survey.", listen = "Tutor: Are you still happy with the topic? Student A: Yes, we want to keep that. It's the interviews we're worried about. Student B: We think an online survey would reach far more people in the time we have. Tutor: That sounds sensible.",
                whyWrong = mapOf(
                    0 to "The topic is raised and kept: “we want to keep that”.",
                    2 to "The deadline comes up later and is not what they change.",
                ),
            ),
            B.q("im12", "ie2-parts", "Listening Part 3: What does the tutor warn them about?", listOf("response rates", "the word limit", "the cost"), setOf(0), "The tutor warns that online surveys often get few replies.", listen = "Tutor: One thing to watch with online surveys is the response rate. Students often send out hundreds and get back a handful.",
                whyWrong = mapOf(
                    1 to "No word limit appears in the warning.",
                    2 to "Cost is not raised. The online survey is the cheap option, which is part of why they chose it.",
                ),
            ),
            B.q("im13", "ie2-parts", "Listening Part 3: Who will write the literature review?", listOf("Student A alone", "Student B alone", "both students together"), setOf(1), "Student B offers to do it while A designs the survey.", listen = "Student A: I'll design the survey questions, then. Student B: In that case I'll take on the literature review by myself, and you can check it afterwards.",
                whyWrong = mapOf(
                    0 to "Student A takes the survey questions. The review is the other job.",
                    2 to "B says “by myself”, and A only checks it afterwards.",
                ),
            ),
            B.q("im14", "ie2-traps", "Listening Part 3: When is the draft due?", listOf("the end of this month", "the middle of next month", "the end of next month"), setOf(1), "The fifteenth of next month is the middle.", listen = "Tutor: I'd like a draft by the fifteenth of next month. Not the end of the month, because I need time to comment before the final deadline.",
                whyWrong = mapOf(
                    0 to "This month is never mentioned. The date given is next month’s fifteenth.",
                    2 to "The tutor rules the end of the month out in so many words: she needs time to comment first.",
                ),
            ),
            // ---------------------------------------------------------------- Listening Part 4
            B.q("im15", "ie2-details", "Listening Part 4: Urban beekeeping has grown because cities offer", listOf("fewer predators", "a greater variety of flowers", "cheaper equipment"), setOf(1), "Gardens and parks provide a wider variety of flowers.", listen = "The surprising success of urban beekeeping comes down to forage. Gardens, parks and even roadside verges offer a far greater variety of flowering plants than the monocultures that dominate the countryside.",
                whyWrong = mapOf(
                    0 to "Predators are not mentioned. The reason given is forage.",
                    2 to "Equipment comes up later, and has nothing to do with why the hives do well.",
                ),
            ),
            B.q("im16", "ie2-details", "Listening Part 4: City hives benefit from a longer season because cities are", listOf("warmer", "drier", "quieter"), setOf(0), "The urban heat island keeps cities a degree or two warmer.", listen = "Cities are also a degree or two warmer than the surrounding countryside, an effect known as the urban heat island, and this extends the foraging season at both ends.",
                whyWrong = mapOf(
                    1 to "Rainfall is not mentioned. A heat island is about temperature.",
                    2 to "Noise is not mentioned either, and would not lengthen a season.",
                ),
            ),
            B.q("im17", "ie2-details", "Listening Part 4: The main threat to urban hives is", listOf("pesticides", "overcrowding of hives", "traffic pollution"), setOf(1), "Too many hives in one area compete for limited forage.", listen = "The principal risk now is not pesticide but overcrowding: when too many hives are placed in one district, the colonies compete for a finite amount of forage and all of them suffer.",
                whyWrong = mapOf(
                    0 to "Pesticide is named only to say it is no longer the principal risk.",
                    2 to "Traffic pollution is not raised at all.",
                ),
            ),
            B.q("im18", "ie2-details", "Listening Part 4: Honey from city hives was found to be", listOf("lower in sugar", "more varied in flavour", "slower to crystallise"), setOf(1), "Greater plant variety produces more varied flavour.", listen = "Analysis of city honey shows a far broader range of pollen types, which translates into noticeably more varied flavours from one district to the next.",
                whyWrong = mapOf(
                    0 to "Sugar is not measured. What was analysed was pollen.",
                    2 to "Crystallisation is not mentioned.",
                ),
            ),
            B.q("im19", "ie2-details", "Listening Part 4: The speaker recommends that new beekeepers first", listOf("buy two hives", "take a course with a local association", "contact the council"), setOf(1), "A course with a local association comes before any purchase.", listen = "My advice to anyone starting out is simple: before you buy a single piece of equipment, take a course with your local beekeeping association.",
                whyWrong = mapOf(
                    0 to "“Before you buy a single piece of equipment” puts the course first.",
                    2 to "The council is not mentioned.",
                ),
            ),
            B.q("im20", "ie2-traps", "Listening Part 4: How many colonies does the speaker suggest per square kilometre?", listOf("two", "ten", "twenty"), setOf(1), "Ten, corrected from an earlier figure.", listen = "Some guidance suggests twenty colonies per square kilometre. In my experience that is far too many, and ten is a more realistic ceiling.",
                whyWrong = mapOf(
                    0 to "Two is not a figure the speaker gives.",
                    2 to "Twenty is the guidance he rejects as far too many.",
                ),
            ),
            // ---------------------------------------------------------------- Reading Passage 1
            B.q("im21", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: The Eden Project was built on former industrial land.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(0), "The passage says it was built in a disused china clay pit, which is former industrial land.",
                scenario = "The Eden Project opened in Cornwall in 2001, built in a disused china clay pit that had been worked for more than a century. Its two groups of linked domes, known as biomes, house plants from humid tropical and Mediterranean climates. The structures are covered not in glass but in panels of a light plastic film, which weighs less than one percent of the equivalent glass and allows the supporting steel frame to be correspondingly slender. Visitor numbers exceeded one million in the first year, far beyond the forecasts, although attendance has since settled at a lower level.",
                whyWrong = mapOf(
                    1 to "A clay pit worked for a century is industrial land. FALSE would need the passage to deny it.",
                    2 to "The passage does say where it was built, so this is not a gap.",
                ),
            ),
            B.q("im22", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: The biomes are covered in glass panels.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(1), "The passage states they are covered in plastic film, not glass. That is a contradiction.",
                scenario = "The Eden Project opened in Cornwall in 2001, built in a disused china clay pit that had been worked for more than a century. Its two groups of linked domes, known as biomes, house plants from humid tropical and Mediterranean climates. The structures are covered not in glass but in panels of a light plastic film, which weighs less than one percent of the equivalent glass and allows the supporting steel frame to be correspondingly slender. Visitor numbers exceeded one million in the first year, far beyond the forecasts, although attendance has since settled at a lower level.",
                whyWrong = mapOf(
                    0 to "“Not in glass but in panels of a light plastic film” is a flat denial.",
                    2 to "Glass is addressed, which is what makes this FALSE rather than NOT GIVEN.",
                ),
            ),
            B.q("im23", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: The project cost more to build than was originally planned.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(2), "Construction cost is never mentioned, only visitor numbers against forecasts.",
                scenario = "The Eden Project opened in Cornwall in 2001, built in a disused china clay pit that had been worked for more than a century. Its two groups of linked domes, known as biomes, house plants from humid tropical and Mediterranean climates. The structures are covered not in glass but in panels of a light plastic film, which weighs less than one percent of the equivalent glass and allows the supporting steel frame to be correspondingly slender. Visitor numbers exceeded one million in the first year, far beyond the forecasts, although attendance has since settled at a lower level.",
                whyWrong = mapOf(
                    0 to "No cost appears anywhere. Exceeding a forecast of visitors is not exceeding a budget.",
                    1 to "FALSE would need the passage to say the cost was on plan. It says nothing about cost at all.",
                ),
            ),
            B.q("im24", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: First-year attendance was higher than expected.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(0), "'Exceeded one million, far beyond the forecasts' states exactly this.",
                scenario = "The Eden Project opened in Cornwall in 2001, built in a disused china clay pit that had been worked for more than a century. Its two groups of linked domes, known as biomes, house plants from humid tropical and Mediterranean climates. The structures are covered not in glass but in panels of a light plastic film, which weighs less than one percent of the equivalent glass and allows the supporting steel frame to be correspondingly slender. Visitor numbers exceeded one million in the first year, far beyond the forecasts, although attendance has since settled at a lower level.",
                whyWrong = mapOf(
                    1 to "“Far beyond the forecasts” is the opposite of a denial.",
                    2 to "It is given, in those words.",
                ),
            ),
            B.q("im25", "ie3-headings", "Reading: Complete the summary with ONE WORD: The plastic panels weigh under one percent of the equivalent ___, which allows a slimmer steel frame.", listOf("film", "glass", "clay"), setOf(1), "The comparison in the passage is with glass.",
                scenario = "The Eden Project opened in Cornwall in 2001, built in a disused china clay pit that had been worked for more than a century. Its two groups of linked domes, known as biomes, house plants from humid tropical and Mediterranean climates. The structures are covered not in glass but in panels of a light plastic film, which weighs less than one percent of the equivalent glass and allows the supporting steel frame to be correspondingly slender. Visitor numbers exceeded one million in the first year, far beyond the forecasts, although attendance has since settled at a lower level.",
                whyWrong = mapOf(
                    0 to "The film is the thing being weighed, not what it is weighed against.",
                    2 to "The clay pit is where it was built. The comparison is with glass.",
                ),
            ),
            // ---------------------------------------------------------------- Reading Passage 2
            B.q("im26", "ie3-headings", "Reading: Which heading best fits the paragraph?", listOf("The cost of night work", "An unexpected effect of artificial light", "How street lights are powered"), setOf(1), "The paragraph's whole point is the unforeseen consequence for insects and the birds that eat them.",
                scenario = "When councils replaced orange sodium street lamps with white light-emitting diodes, the stated aims were lower bills and better visibility. Both were achieved. What had not been anticipated was the effect on insects. Moth numbers along newly lit verges fell by roughly half within three years, and the insect-eating birds that depend on them declined in step. Ecologists now argue that the colour of the light, rather than its brightness, is the critical factor.",
                whyWrong = mapOf(
                    0 to "Cost appears only as a stated aim that was met. The paragraph is not about it.",
                    2 to "How the lamps are powered is never discussed.",
                ),
            ),
            B.q("im27", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: The switch to LEDs reduced electricity bills.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(0), "'Both were achieved' refers to lower bills and better visibility.",
                scenario = "When councils replaced orange sodium street lamps with white light-emitting diodes, the stated aims were lower bills and better visibility. Both were achieved. What had not been anticipated was the effect on insects. Moth numbers along newly lit verges fell by roughly half within three years, and the insect-eating birds that depend on them declined in step. Ecologists now argue that the colour of the light, rather than its brightness, is the critical factor.",
                whyWrong = mapOf(
                    1 to "“Both were achieved” covers the bills.",
                    2 to "It is given, by that sentence.",
                ),
            ),
            B.q("im28", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: Ecologists believe brightness matters more than colour.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(1), "The passage says the opposite: colour rather than brightness is critical.",
                scenario = "When councils replaced orange sodium street lamps with white light-emitting diodes, the stated aims were lower bills and better visibility. Both were achieved. What had not been anticipated was the effect on insects. Moth numbers along newly lit verges fell by roughly half within three years, and the insect-eating birds that depend on them declined in step. Ecologists now argue that the colour of the light, rather than its brightness, is the critical factor.",
                whyWrong = mapOf(
                    0 to "The passage says colour rather than brightness, which is this statement reversed.",
                    2 to "It is addressed directly, so there is something here to contradict.",
                ),
            ),
            B.q("im29", "ie3-headings", "Reading: Complete the summary with ONE WORD: Moth numbers fell by about ___ within three years.", listOf("half", "third", "quarter"), setOf(0), "'Fell by roughly half' is stated directly.",
                scenario = "When councils replaced orange sodium street lamps with white light-emitting diodes, the stated aims were lower bills and better visibility. Both were achieved. What had not been anticipated was the effect on insects. Moth numbers along newly lit verges fell by roughly half within three years, and the insect-eating birds that depend on them declined in step. Ecologists now argue that the colour of the light, rather than its brightness, is the critical factor.",
                whyWrong = mapOf(
                    1 to "A third is not a figure in the passage.",
                    2 to "Nor is a quarter. “Roughly half” is stated.",
                ),
            ),
            B.q("im30", "ie3-headings", "Reading: Why did bird numbers fall, according to the passage?", listOf("The birds avoided the light", "Their insect food declined", "Their nesting sites were removed"), setOf(1), "The birds 'depend on' the moths and declined in step with them.",
                scenario = "When councils replaced orange sodium street lamps with white light-emitting diodes, the stated aims were lower bills and better visibility. Both were achieved. What had not been anticipated was the effect on insects. Moth numbers along newly lit verges fell by roughly half within three years, and the insect-eating birds that depend on them declined in step. Ecologists now argue that the colour of the light, rather than its brightness, is the critical factor.",
                whyWrong = mapOf(
                    0 to "Avoidance is not mentioned. The birds declined with the moths, not away from the lamps.",
                    2 to "Nesting sites are not raised.",
                ),
            ),
            // ---------------------------------------------------------------- Reading Passage 3
            B.q("im31", "ie3-headings", "Reading: Which heading best fits the paragraph?", listOf("A practice that returned for new reasons", "The history of woollen cloth", "Why machines replaced hand work"), setOf(0), "The paragraph describes an old practice revived for modern environmental reasons.",
                scenario = "Hedgelaying, the craft of partly cutting and bending a hedge so that it thickens from the base, had almost disappeared from the British countryside by the 1970s, displaced by wire fencing and mechanical flails. Its revival over the past two decades owes little to nostalgia. A laid hedge supports far more nesting birds than a flailed one, holds soil on sloping ground and, unlike a fence, continues to function as a stock barrier for a century or more. Grant schemes now pay for the work, and the number of trained layers has risen accordingly, although the craft remains slow: an experienced worker lays perhaps ten metres in a day.",
                whyWrong = mapOf(
                    1 to "Wool is not mentioned. The hedge here is a stock barrier, not a source of cloth.",
                    2 to "Machines appear as what displaced the craft, not as what the paragraph is about.",
                ),
            ),
            B.q("im32", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: Hedgelaying was revived mainly because people were nostalgic.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(1), "The passage says the revival 'owes little to nostalgia'.",
                scenario = "Hedgelaying, the craft of partly cutting and bending a hedge so that it thickens from the base, had almost disappeared from the British countryside by the 1970s, displaced by wire fencing and mechanical flails. Its revival over the past two decades owes little to nostalgia. A laid hedge supports far more nesting birds than a flailed one, holds soil on sloping ground and, unlike a fence, continues to function as a stock barrier for a century or more. Grant schemes now pay for the work, and the number of trained layers has risen accordingly, although the craft remains slow: an experienced worker lays perhaps ten metres in a day.",
                whyWrong = mapOf(
                    0 to "“Owes little to nostalgia” says the opposite.",
                    2 to "Nostalgia is named, which is why this is a contradiction rather than a gap.",
                ),
            ),
            B.q("im33", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: A laid hedge lasts longer than a wire fence.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(0), "'Unlike a fence, continues to function as a stock barrier for a century or more' states the comparison.",
                scenario = "Hedgelaying, the craft of partly cutting and bending a hedge so that it thickens from the base, had almost disappeared from the British countryside by the 1970s, displaced by wire fencing and mechanical flails. Its revival over the past two decades owes little to nostalgia. A laid hedge supports far more nesting birds than a flailed one, holds soil on sloping ground and, unlike a fence, continues to function as a stock barrier for a century or more. Grant schemes now pay for the work, and the number of trained layers has risen accordingly, although the craft remains slow: an experienced worker lays perhaps ten metres in a day.",
                whyWrong = mapOf(
                    1 to "The comparison with a fence is made, and it favours the hedge.",
                    2 to "“Unlike a fence” is the passage making exactly this comparison.",
                ),
            ),
            B.q("im34", "ie3-tfng", "Reading: TRUE, FALSE or NOT GIVEN: Hedgelaying is cheaper than erecting a wire fence.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(2), "Relative cost is never compared; only grants and speed are mentioned.",
                scenario = "Hedgelaying, the craft of partly cutting and bending a hedge so that it thickens from the base, had almost disappeared from the British countryside by the 1970s, displaced by wire fencing and mechanical flails. Its revival over the past two decades owes little to nostalgia. A laid hedge supports far more nesting birds than a flailed one, holds soil on sloping ground and, unlike a fence, continues to function as a stock barrier for a century or more. Grant schemes now pay for the work, and the number of trained layers has risen accordingly, although the craft remains slow: an experienced worker lays perhaps ten metres in a day.",
                whyWrong = mapOf(
                    0 to "Grants pay for the work, which is not the same as the work being cheaper.",
                    1 to "Nothing is said about relative cost in either direction.",
                ),
            ),
            B.q("im35", "ie3-headings", "Reading: Complete the sentence with ONE WORD AND/OR A NUMBER: An experienced worker lays about ___ metres per day.", listOf("two", "ten", "a hundred"), setOf(1), "'Perhaps ten metres in a day' is stated at the end.",
                scenario = "Hedgelaying, the craft of partly cutting and bending a hedge so that it thickens from the base, had almost disappeared from the British countryside by the 1970s, displaced by wire fencing and mechanical flails. Its revival over the past two decades owes little to nostalgia. A laid hedge supports far more nesting birds than a flailed one, holds soil on sloping ground and, unlike a fence, continues to function as a stock barrier for a century or more. Grant schemes now pay for the work, and the number of trained layers has risen accordingly, although the craft remains slow: an experienced worker lays perhaps ten metres in a day.",
                whyWrong = mapOf(
                    0 to "Two is not given.",
                    2 to "A hundred metres a day would not be described as slow.",
                ),
            ),
            B.q("im36", "ie3-headings", "Reading: What displaced hedgelaying by the 1970s?", listOf("grant schemes", "wire fencing and mechanical flails", "a shortage of hedges"), setOf(1), "Both are named as the cause of its disappearance.",
                scenario = "Hedgelaying, the craft of partly cutting and bending a hedge so that it thickens from the base, had almost disappeared from the British countryside by the 1970s, displaced by wire fencing and mechanical flails. Its revival over the past two decades owes little to nostalgia. A laid hedge supports far more nesting birds than a flailed one, holds soil on sloping ground and, unlike a fence, continues to function as a stock barrier for a century or more. Grant schemes now pay for the work, and the number of trained layers has risen accordingly, although the craft remains slow: an experienced worker lays perhaps ten metres in a day.",
                whyWrong = mapOf(
                    0 to "Grants belong to the revival, not to the decline.",
                    2 to "No shortage of hedges is mentioned.",
                ),
            ),
            // ---------------------------------------------------------------- Reading: General Training style notices
            B.q("im37", "ie3-timing", "Reading: Staff handbook. TRUE, FALSE or NOT GIVEN: Employees may carry unused leave into the next year without permission.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(1), "Carry-over requires written approval from a line manager, so 'without permission' is contradicted.",
                scenario = "Annual leave. Full-time staff are entitled to 25 days of paid leave per year, in addition to public holidays. Requests must be submitted at least two weeks in advance through the staff portal. Up to five days may be carried over into the following year, but only with the written approval of your line manager. Leave may not be taken during the first month of employment.",
                whyWrong = mapOf(
                    0 to "Carry-over exists, but “only with the written approval of your line manager”.",
                    2 to "The handbook addresses it, so there is something to contradict.",
                ),
            ),
            B.q("im38", "ie3-timing", "Reading: Staff handbook. How much notice must a leave request give?", listOf("one week", "two weeks", "one month"), setOf(1), "'At least two weeks in advance' is stated.",
                scenario = "Annual leave. Full-time staff are entitled to 25 days of paid leave per year, in addition to public holidays. Requests must be submitted at least two weeks in advance through the staff portal. Up to five days may be carried over into the following year, but only with the written approval of your line manager. Leave may not be taken during the first month of employment.",
                whyWrong = mapOf(
                    0 to "One week is not the figure given.",
                    2 to "A month is how long you wait before taking any leave, not the notice a request needs.",
                ),
            ),
            B.q("im39", "ie3-timing", "Reading: Staff handbook. TRUE, FALSE or NOT GIVEN: Public holidays count towards the 25 days.", listOf("TRUE", "FALSE", "NOT GIVEN"), setOf(1), "The leave is 'in addition to public holidays', so they do not count towards it.",
                scenario = "Annual leave. Full-time staff are entitled to 25 days of paid leave per year, in addition to public holidays. Requests must be submitted at least two weeks in advance through the staff portal. Up to five days may be carried over into the following year, but only with the written approval of your line manager. Leave may not be taken during the first month of employment.",
                whyWrong = mapOf(
                    0 to "“In addition to public holidays” puts them outside the 25.",
                    2 to "It is addressed, in that phrase.",
                ),
            ),
            B.q("im40", "ie3-timing", "Reading: Staff handbook. A new employee wants leave in week three of their job. They", listOf("may apply as normal", "may not take leave yet", "need two weeks' notice only"), setOf(1), "Leave may not be taken during the first month of employment.",
                scenario = "Annual leave. Full-time staff are entitled to 25 days of paid leave per year, in addition to public holidays. Requests must be submitted at least two weeks in advance through the staff portal. Up to five days may be carried over into the following year, but only with the written approval of your line manager. Leave may not be taken during the first month of employment.",
                whyWrong = mapOf(
                    0 to "The first-month rule overrides the normal process.",
                    2 to "Notice is not the obstacle here. The first month is.",
                ),
            ),
        ) + IeltsMock2.questions),
    )
}
