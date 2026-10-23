package com.shahaabapps.core.content

/** The PSM I assessment, 80 questions, 60 minutes, 85 percent: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesAssessment {
    private val classic = Podcast(
        "psm-assessment",
        listOf(
            maya("Hi, and welcome back. Today: the sitting itself. The P S M one assessment, Professional Scrum Master one, from Scrum.org. Eighty questions, sixty minutes, and you pass at eighty-five percent. Everything we've covered so far leads to this hour."),
            leo("Eighty-five. That sounds brutal. My uni pass mark was forty."),
            maya("[chuckle] It's high on purpose. Eighty-five percent of eighty is sixty-eight. So you can get twelve wrong, and not one more. The good news: every question comes from one short document."),
            leo("The Scrum Guide."),
            maya("The Scrum Guide, the November 2020 edition, by Ken Schwaber and Jeff Sutherland. There's no newer official edition. It's short enough to read in an evening. And on the exam it's the only source of truth, even when your office does things differently. The traps episode covers those habits."),
            leo("If it all comes from the Guide, what does it test most?"),
            maya("The numbers, who does what, and the commitments. The timeboxes for a one-month Sprint: Sprint Planning up to eight hours, the Daily Scrum fifteen minutes, the Sprint Review up to four hours, the Retrospective up to three. With shorter Sprints, Planning, Review and Retrospective are usually shorter, but the Daily Scrum is always fifteen minutes. And a Scrum Team is typically ten people or fewer."),
            leo("And the commitments?"),
            maya("Each artifact has one. The Product Backlog has the Product Goal. The Sprint Backlog has the Sprint Goal. The Increment has the Definition of Done. Learn those three pairs cold. Then the three pillars, transparency, inspection and adaptation, and the five values: commitment, focus, openness, respect and courage."),
            leo("What do the questions look like?"),
            maya("Three kinds. Multiple choice, where you pick one answer. Multiple answer, where more than one option is right. And true or false. It's taken online, it costs two hundred US dollars an attempt, and once you pass, the certification doesn't need renewing."),
            leo("Sixty minutes for eighty questions. Do the maths for me."),
            maya("Forty-five seconds a question, on average. Picture a five-kilometre run: you don't sprint the first lap and walk the last. You hold an even pace. A handy checkpoint: about twenty questions every fifteen minutes."),
            leo("And when one question eats three minutes?"),
            maya("That's the real danger. Most people who fail on time didn't lack knowledge; they lost five minutes on two questions. Choose your best answer, and move on. A question left behind costs one mark. Running out of time can cost ten."),
            leo("What about those multiple answer ones? They scare me."),
            maya("Treat every option as its own true or false. Hold each one up against the Guide, separately. For example: which are Scrum events? The Sprint, true. Sprint Planning, true. Refinement, false; it's an ongoing activity. Release planning, false; not in the Guide."),
            leo("Any words to watch for?"),
            maya("[pause] Lots. Best: several answers may be partly true, so pick the one the Guide supports most directly. Must, should and may: must is a rule, may is a choice. Not and never: a question asking which is not true flips everything. Read every word."),
            leo("Give me a sneaky one."),
            maya("The Product Owner must attend the Daily Scrum. True or false? False. It's the Developers' event. The word must is doing all the work there. Swap must for may, and it's a different question."),
            leo("And the scenario questions? The long ones about a team in trouble?"),
            maya("Find the accountability first. Who owns this? The Product Owner owns the order of the Product Backlog. The Developers own the Sprint Backlog and how the work is done. The Scrum Master owns how well Scrum is understood and used. The right answer usually respects that, plus self-management and learning from what actually happened."),
            leo("How do I practise? Is there a sample exam?"),
            maya("Scrum.org offers a free practice assessment called Scrum Open. Take it many times. But don't memorise its answers: the real questions are different. For each one you miss, find the sentence in the Guide that settles it. That's how understanding sticks."),
            leo("And this app?"),
            maya("Each module here ends with a test, and the course has a mock assessment of eighty questions. Take that one with a sixty-minute timer, in one sitting, so the pace becomes a habit."),
            leo("What score should I be getting before I book the real one?"),
            maya("The Guide doesn't say, of course; that's advice, not a rule. A good sign is scoring well above eighty-five on practice runs, again and again, under time. If you're scraping eighty-five in practice, the real one is a coin toss."),
            leo("Anything for the day itself?"),
            maya("It's online, so a quiet room, a steady internet connection, and your phone out of reach. Say you're building a release that evening: don't. Give the hour its own space."),
            leo("And if I fail?"),
            maya("You pay for another attempt, and you'll know exactly where you were weak. Go back to those topics, the Product Backlog episode, the Sprint episode, whichever it was. A miss is information. That's empiricism, after all."),
            leo("So what do I remember for the exam?"),
            maya("Eighty questions, sixty minutes, eighty-five percent: that's sixty-eight right, twelve wrong at most. The pace: about forty-five seconds a question. The source: the Scrum Guide, November 2020, only. Practice: Scrum Open, for understanding, not memory. And read the small words: best, must, not."),
            leo("And anything to try today?"),
            maya("Take Scrum Open today with a timer running, and note your score and the time you reach the halfway question. For each wrong answer, find the sentence in the Scrum Guide that settles it. Then write down the pace checkpoints you'll use on the day."),
            leo("Even pace, every word. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-assessment",
        listOf(
            maya("Welcome to Case Files. Today's case: The Clock That Ran Out. Priya, an Android developer on a running app team, knows Scrum well. She sat the P S M one assessment, Professional Scrum Master one. At question seventy-one, the hour ended. She scored eighty-one percent."),
            leo("[sigh] Detective Leo. First suspect: she didn't study enough."),
            maya("Wrong suspect. Look at her answers. She reached seventy questions and got sixty-five of them right: better than nine in ten of what she read. But the pass mark is eighty-five percent of eighty, which is sixty-eight. She was three short, with ten questions never read."),
            leo("So it was the clock."),
            maya("The clock was the weapon. Sixty minutes for eighty questions is forty-five seconds a question, on average. Picture a five-kilometre run: hold an even pace, don't sprint the first lap. A useful checkpoint is twenty questions every fifteen minutes."),
            leo("Where did her time go?"),
            maya("Exhibit one: her scrap paper. Question nine, six minutes. A multiple answer question: which of these are Scrum events? She went round and round."),
            leo("How should she have done it?"),
            maya("Treat each option as its own true or false, against the Guide. The Sprint: true. Sprint Planning: true. Refinement: false, it's an ongoing activity, not an event. Release planning: false, not in the Guide. Thirty seconds, done."),
            leo("Second suspect: the questions are badly written. Too tricky."),
            maya("[pause] Fair, but tricky in known ways. Exhibit two: a question she got wrong. The Scrum Master must facilitate the Daily Scrum. She said true. The Guide says the Daily Scrum is for the Developers; the Scrum Master makes sure it happens. The word must was the trap."),
            leo("What other words should she have caught?"),
            maya("Best: several answers may be partly true, so pick the one the Guide supports most directly. Must, should and may: a rule, a recommendation, a choice. And not: a question asking which is not true flips everything."),
            leo("Third suspect: she learnt from the wrong source."),
            maya("Now you're warm. Exhibit three: her notes. Copied from an old blog. Development Team, the three Daily Scrum questions, roles. That's the older wording. The assessment uses the Scrum Guide, November 2020 edition: Developers, accountabilities, self-managing. It's the only source of truth."),
            leo("Did her notes get the numbers right, at least?"),
            maya("Half. The Daily Scrum at fifteen minutes, right. The Sprint Review at two hours, wrong. For a one-month Sprint: Sprint Planning up to eight hours, the Sprint Review up to four, the Retrospective up to three. Shorter Sprints, usually shorter events; the Daily Scrum stays fifteen minutes."),
            leo("And the commitments?"),
            maya("Right on those. Product Backlog and Product Goal. Sprint Backlog and Sprint Goal. Increment and Definition of Done. Three pairs, learnt cold. And the pillars and values: transparency, inspection, adaptation; commitment, focus, openness, respect, courage."),
            leo("Did she practise at all?"),
            maya("Exhibit four. Scrum Open, the free practice assessment from Scrum.org. She took it eleven times and scored a hundred. Then she noticed she remembered the answers, not the reasons. The real questions are different."),
            leo("So what's the right way to use Scrum Open?"),
            maya("For every question missed, or guessed, find the sentence in the Guide that settles it. And take it timed. Practice that ignores the clock trains the wrong habit."),
            leo("How many times had she read the Guide itself?"),
            maya("Twice. Not enough. It's short enough to read in an evening; read it until nothing in it surprises you. Every question comes from it, so every reading pays."),
            leo("What about the long scenario questions? A team in trouble, four options."),
            maya("Find the accountability first. The Product Owner owns the order of the Product Backlog. The Developers own the Sprint Backlog and how the work gets done. The Scrum Master owns how well Scrum is understood and used. Then pick the answer that respects self-management and empiricism, learning from what happened."),
            leo("Anything about the day itself?"),
            maya("Exhibit five. She sat it on a train, on her phone's data. It's an online test; a quiet room and a steady connection matter. She lost two minutes to a dropped signal. The facts, so you're ready: three kinds of question, multiple choice, multiple answer, true or false; two hundred US dollars an attempt; and the certification never needs renewing."),
            leo("And now? Does she try again?"),
            maya("She does, with another attempt paid. Her ten unread questions told her nothing about her knowledge, everything about her pace. That's empiricism: inspect what happened, adapt. The traps episode covers the myths she fixed in her notes."),
            leo("So, the case summary for the exam?"),
            maya("Eighty questions, sixty minutes, eighty-five percent: that's sixty-eight right, twelve wrong at most. The pace: about forty-five seconds a question. The source: the Scrum Guide, November 2020, only. Practice: Scrum Open, for understanding, not memory. And read the small words: best, must, not."),
            leo("Case closed. Anything to try today?"),
            maya("Take Scrum Open today with a timer running, and note your score and the time you reach the halfway question. For each wrong answer, find the sentence in the Scrum Guide that settles it. Then write down the pace checkpoints you'll use on the day."),
            leo("Mind the clock. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-assessment",
        listOf(
            maya("Okay, did you hear? The whole running app team booked P S M one, Professional Scrum Master one, for the same week. Five people, five ways to sit one exam."),
            leo("[laugh] I heard someone did it on a train. Start with the basics."),
            maya("Eighty questions, sixty minutes, and you pass at eighty-five percent. Three kinds of question: multiple choice, multiple answer, and true or false. It's online, two hundred US dollars an attempt, and the certification never needs renewing."),
            leo("Eighty-five percent of eighty is?"),
            maya("Sixty-eight. So twelve wrong, at most. Which brings us to Monday, and the intern, who'd worked out the pass mark as sixty right. Studied for a gentler exam than the real one."),
            leo("Ouch. Tuesday?"),
            maya("Tom. Tom took Scrum Open, the free practice assessment from Scrum.org, fourteen times, until he knew every answer by heart. Then the real questions were different. Memory, not understanding."),
            leo("How should he have used it?"),
            maya("For each question missed, or guessed, find the sentence in the Scrum Guide that settles it. The Guide's November 2020 edition is the only source. Every question comes from it."),
            leo("[chuckle] Wednesday?"),
            maya("Marcus studied from a blog. An old one. Development Team, roles, the three Daily Scrum questions. The 2020 Guide says Developers, accountabilities, self-managing, and the three questions are gone. He lost marks on words alone."),
            leo("What else did his blog get wrong?"),
            maya("The timeboxes. For a one-month Sprint: Sprint Planning up to eight hours, the Daily Scrum fifteen minutes, the Sprint Review up to four hours, the Retrospective up to three. His blog said the Review was two. In shorter Sprints, Planning, Review and Retrospective are usually shorter; the Daily Scrum is always fifteen minutes."),
            leo("Thursday? The train."),
            maya("Dana. She sat it on the train home, on her phone's data. It's an online test; she lost three minutes to tunnels. A quiet room and a steady connection, please. And not on a release evening, either."),
            leo("Did anyone do it well?"),
            maya("Friday. Priya. She'd done the maths: sixty minutes for eighty questions is forty-five seconds a question. Picture a five-kilometre run at an even pace. She checked the clock at question twenty: fifteen minutes gone. Right on time."),
            leo("What did she do when she got stuck?"),
            maya("Picked her best answer and moved on. One question left behind costs a mark. Ten minutes lost can cost ten. Tom spent seven minutes on one question, for the record."),
            leo("Of course he did. What about the multiple answer ones?"),
            maya("Priya's trick: treat each option as its own true or false. For example: which are Scrum events? The Sprint: true. Sprint Planning: true. Refinement: false, it's an ongoing activity. Release planning: false, it's not in the Guide."),
            leo("And the tricky wording?"),
            maya("She read every word. Best means the option the Guide supports most directly. Must is a rule; may is a choice. And watch for not. The Product Owner must attend the Daily Scrum: false. It's the Developers' event."),
            leo("And the long scenario ones?"),
            maya("Find the accountability. The Product Owner orders the Product Backlog. The Developers own the Sprint Backlog and how the work is done. The Scrum Master owns how well Scrum is understood and used. The answer that respects self-management usually wins."),
            leo("Was there a cram list going round the office?"),
            maya("Priya's. Three artifacts, three commitments: Product Backlog and Product Goal, Sprint Backlog and Sprint Goal, Increment and Definition of Done. Three pillars: transparency, inspection, adaptation. Five values: commitment, focus, openness, respect, courage. And a Scrum Team is typically ten or fewer."),
            leo("And how many times had she read the Guide?"),
            maya("[chuckle] Five, she says. It's short enough for an evening. By the fifth time nothing in it surprised her, and that's the point."),
            leo("So, who passed?"),
            maya("Priya, with ninety-four. Dana passed by one question. Tom and Marcus pay for another attempt. And they'll know where they were weak, which is empiricism: inspect, then adapt. The traps episode has every myth Marcus fell for."),
            leo("And the intern?"),
            maya("Hasn't booked yet. Taking this app's mock assessment, eighty questions, with a sixty-minute timer, until the pace is a habit."),
            leo("Any advice from Priya for the intern?"),
            maya("Take the practice timed, every time. Check the clock at question twenty. And when two answers both sound fine, it's a best answer question: pick the one the Guide says most directly. The other is usually a good practice the Guide never mentions."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Eighty questions, sixty minutes, eighty-five percent: that's sixty-eight right, twelve wrong at most. The pace: about forty-five seconds a question. The source: the Scrum Guide, November 2020, only. Practice: Scrum Open, for understanding, not memory. And read the small words: best, must, not."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Take Scrum Open today with a timer running, and note your score and the time you reach the halfway question. For each wrong answer, find the sentence in the Scrum Guide that settles it. Then write down the pace checkpoints you'll use on the day."),
            leo("Not on the train, Dana. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-assessment",
        listOf(
            nova("Hey, you. It's late, and maybe your exam is tomorrow. [pause] Tonight, we walk through the hour itself. P S M one, Professional Scrum Master one. Slowly, so you sleep well."),
            ivy("Mm. Tell me the numbers first. I like to know what I'm walking into."),
            nova("Eighty questions. Sixty minutes. The pass mark is eighty-five percent. [pause] That's sixty-eight right answers, so you may get twelve wrong, and not one more."),
            ivy("Twelve. That's not many."),
            nova("It's enough, if you know the Guide. Every question comes from the Scrum Guide, the November 2020 edition. Not your office, not a blog. It's the only source of truth, and it's short enough to read in an evening. Perhaps this one."),
            ivy("What does the Guide ask about most?"),
            nova("The numbers, and who does what. For a one-month Sprint: Sprint Planning up to eight hours. The Daily Scrum, fifteen minutes. The Sprint Review, up to four. The Retrospective, up to three. [pause] Shorter Sprints usually mean shorter Planning, Review and Retrospective. The Daily Scrum stays fifteen minutes."),
            ivy("Say the commitments to me, like a lullaby."),
            nova("The Product Backlog, and its Product Goal. The Sprint Backlog, and its Sprint Goal. The Increment, and its Definition of Done. [pause] Three pairs. Learn them until they hum."),
            ivy("And the pillars? The values?"),
            nova("Three pillars: transparency, inspection, adaptation. Five values: commitment, focus, openness, respect, courage. And a Scrum Team is typically ten people or fewer."),
            ivy("What kinds of questions wait for me?"),
            nova("Three kinds. Multiple choice, one answer. Multiple answer, more than one. And true or false. It's taken online. It costs two hundred US dollars an attempt. And once you pass, it never needs renewing."),
            ivy("And the time. Sixty minutes feels so short tonight."),
            nova("Forty-five seconds a question, on average. Picture a slow night run, five kilometres, at an even pace. No sprinting the first lap. [pause] A checkpoint: around twenty questions every fifteen minutes."),
            ivy("And if one question holds me too long?"),
            nova("Choose your best answer, and let it go. A question left behind costs one mark. Running out of time can cost ten. Most people who fail on time didn't lack knowledge. They lingered."),
            ivy("And if two answers both sound right?"),
            nova("Then it's a best answer question. Pick the one the Guide says most directly, in its own words. The other is usually a good practice the Guide never mentions."),
            ivy("The multiple answer ones. Whisper me the trick."),
            nova("Treat every option as its own true or false. [pause] Which are Scrum events? The Sprint, true. Sprint Planning, true. Refinement, false, an ongoing activity. Release planning, false, not in the Guide. One at a time."),
            ivy("And the words that hide in the questions?"),
            nova("Best: the option the Guide supports most directly. Must, should and may: a rule, a recommendation, a choice. And not, which flips everything. The Product Owner must attend the Daily Scrum: false. It's the Developers' event. Read every word, even at the end of the hour."),
            ivy("And the long stories? A team in trouble, four ways out?"),
            nova("Find the accountability first. The Product Owner orders the Product Backlog. The Developers own the Sprint Backlog and how the work is done. The Scrum Master owns how well Scrum is understood and used. The right answer usually respects self-management, and learning from what happened."),
            ivy("How do I practise, in the nights before?"),
            nova("Scrum Open, the free practice assessment from Scrum.org. Take it timed. But never memorise its answers; the real questions are different. For each one you miss, find the sentence in the Guide that settles it. And this course has a mock assessment of eighty questions. Give it the full sixty minutes."),
            ivy("What score should I see before I'm ready?"),
            nova("That's advice, not a rule. Well above eighty-five, again and again, under the clock. [pause] Scraping eighty-five in practice makes the real one a coin toss."),
            ivy("And tomorrow? Where should I be?"),
            nova("Somewhere quiet, with a steady internet connection, your phone out of reach. It's online, so the room is yours to choose. Choose a calm one."),
            ivy("Should I cram tonight?"),
            nova("[pause] Read the Guide once more, slowly, then sleep. It's short. A rested mind reads every word; a tired one skims past the must and the not."),
            ivy("And if I don't pass?"),
            nova("Then you pay for another attempt, and you know exactly what to study. A miss is information. That's empiricism: inspect, adapt. The traps episode is a good late night for that."),
            ivy("[sigh] Mm. Last thing. What do I remember for the exam?"),
            nova("Eighty questions, sixty minutes, eighty-five percent: that's sixty-eight right, twelve wrong at most. The pace: about forty-five seconds a question. The source: the Scrum Guide, November 2020, only. Practice: Scrum Open, for understanding, not memory. And read the small words: best, must, not."),
            ivy("And something to try today, before I close my eyes?"),
            nova("Take Scrum Open today with a timer running, and note your score and the time you reach the halfway question. [pause] For each wrong answer, find the sentence in the Scrum Guide that settles it. Then write down the pace checkpoints you'll use on the day."),
            ivy("Even pace, every word. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-assessment",
        listOf(
            nova("Tonight is date night, darling, and I've booked us something special. P S M one. Professional Scrum Master one."),
            rafe("An exam? You've booked us an exam? You really know how to spoil me."),
            nova("[laugh] A practice round first. Quick-fire questions about the sitting itself. Every right answer, a kiss. Every wrong one, a little lecture. Ready?"),
            rafe("Always. Hit me."),
            nova("How many questions, and how long?"),
            rafe("Eighty questions, sixty minutes."),
            nova("Right answer. Short and intense, just how you like it. [kiss]"),
            rafe("My turn. What's the pass mark?"),
            nova("Eighty-five percent. So sixty-eight right answers out of eighty. Twelve wrong, at most."),
            rafe("Right answer. High standards. I'd expect nothing less. [kiss]"),
            nova("And the questions come in which kinds?"),
            rafe("Multiple choice, multiple answer, and true or false. It's taken online, two hundred US dollars an attempt, and once you pass, it never needs renewing."),
            nova("A commitment that lasts. I like that. Now, exam style: where does every question come from?"),
            rafe("The Scrum Guide. Whatever edition is newest on the blogs."),
            nova("No kiss for that one. It's the November 2020 edition, by Ken Schwaber and Jeff Sutherland. There's no newer official one, and old blogs still say Development Team and roles. The Guide is the only source of truth; the traps episode covers the myths."),
            rafe("Then give me one to win back. The pace."),
            nova("Sixty minutes, eighty questions. How long per question, on average?"),
            rafe("Forty-five seconds. Like a five-kilometre run at an even pace. About twenty questions every fifteen minutes."),
            nova("Right answer. Steady, never rushed. [kiss]"),
            rafe("Then a Guide question, for luck. The Sprint Review's timebox, for a one-month Sprint?"),
            nova("Up to four hours. Sprint Planning up to eight, the Retrospective up to three, the Daily Scrum fifteen minutes. Shorter Sprints usually mean shorter Planning, Review and Retrospective, but the Daily Scrum stays fifteen minutes."),
            rafe("Right answer. You always know how long to linger. [kiss]"),
            nova("My turn. Three artifacts, three commitments. Pair them up."),
            rafe("Product Backlog and Product Goal. Sprint Backlog and Sprint Goal. Increment and Definition of Done."),
            nova("Right answer. A perfect match, like us. [kiss]"),
            rafe("And if one question takes forever?"),
            nova("Pick your best answer and move on. One question left behind costs a mark; running out of time can cost ten. Don't linger, darling. Not on a question, anyway."),
            rafe("Then quiz me on the scary ones. Multiple answer, where more than one option is right."),
            nova("Which of these are Scrum events: the Sprint, Sprint Planning, refinement, release planning?"),
            rafe("Treat each one as its own true or false. The Sprint: true. Sprint Planning: true. Refinement: false, an ongoing activity. Release planning: false, not in the Guide."),
            nova("Right answer. You took them one at a time. [kiss]"),
            rafe("My turn. True or false: the Product Owner must attend the Daily Scrum."),
            nova("False. The Daily Scrum is for the Developers. The word must is the trap. Best, must, may, not: read every small word."),
            rafe("Right answer. You never miss the small things. [kiss]"),
            nova("And when two answers both sound right?"),
            rafe("It's a best answer question. Pick the one the Guide says most directly. The other is usually a good practice the Guide never mentions."),
            nova("And the long scenario questions? A team in trouble?"),
            rafe("Find the accountability. The Product Owner orders the Product Backlog. The Developers own the Sprint Backlog and how the work is done. The Scrum Master owns how well Scrum is understood and used. Then pick the answer that respects self-management."),
            nova("Lovely. And how do we practise?"),
            rafe("Scrum Open, the free practice assessment from Scrum.org. Memorise every answer, and you're set."),
            nova("No kiss. The real questions are different. Use it to understand: for each miss, find the sentence in the Guide that settles it. And take this course's eighty-question mock assessment with a sixty-minute timer. Aim to score well above eighty-five, again and again, under the clock. That's advice, not a rule, but it's good advice."),
            rafe("And the night of the exam? Candles?"),
            nova("A quiet room, a steady internet connection, phone out of reach. Candles optional. And if it goes wrong, another attempt, and you'll know exactly what to study. That's empiricism: inspect what happened, then adapt."),
            rafe("And the night before? Do we cram?"),
            nova("Read the Guide once more, slowly, then sleep. Three pillars: transparency, inspection, adaptation. Five values: commitment, focus, openness, respect, courage. A rested mind reads every must and every not. Then lights out, darling."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("Eighty questions, sixty minutes, eighty-five percent: that's sixty-eight right, twelve wrong at most. The pace: about forty-five seconds a question. The source: the Scrum Guide, November 2020, only. Practice: Scrum Open, for understanding, not memory. And read the small words: best, must, not."),
            rafe("Perfect score, darling. That earns a long one. [kiss] And something to try today?"),
            nova("Take Scrum Open today with a timer running, and note your score and the time you reach the halfway question. For each wrong answer, find the sentence in the Scrum Guide that settles it. Then write down the pace checkpoints you'll use on the day."),
            rafe("Even pace, and every word. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun numbers() = DiagramSpec(
        "The PSM I assessment: 80 questions in 60 minutes, passed at 85 percent, which is about 45 seconds a question.",
        nodes = listOf(
            DNode("q", "80 questions", 0.25f, 0.18f, w = 0.42f, h = 0.24f, tone = 1),
            DNode("t", "60 minutes", 0.75f, 0.18f, w = 0.42f, h = 0.24f, tone = 1),
            DNode("p", "85 percent", 0.25f, 0.52f, w = 0.42f, h = 0.24f, sub = "68 right"),
            DNode("pace", "45 seconds", 0.75f, 0.52f, w = 0.42f, h = 0.24f, sub = "per question"),
            DNode("note", "Twelve wrong, at most", 0.5f, 0.85f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        aspect = 0.8f,
    )

    private fun source() = DiagramSpec(
        "Every question comes from the Scrum Guide (2020): its timeboxes, its three commitments, its pillars and values.",
        nodes = listOf(
            DNode("guide", "Scrum Guide, Nov 2020", 0.5f, 0.15f, w = 0.7f, h = 0.2f, tone = 1, sub = "the only source"),
            DNode("nums", "Timeboxes", 0.17f, 0.6f, w = 0.3f, h = 0.26f, sub = "8h, 15m, 4h, 3h"),
            DNode("pairs", "Three commitments", 0.5f, 0.6f, w = 0.3f, h = 0.26f, sub = "goal, goal, done"),
            DNode("pillars", "Pillars and values", 0.83f, 0.6f, w = 0.3f, h = 0.26f, sub = "three and five"),
            DNode("note", "Shorter Sprints: other events usually shorter; Daily Scrum stays 15m", 0.5f, 0.9f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("guide", "nums"),
            DEdge("guide", "pairs"),
            DEdge("guide", "pillars"),
        ),
        aspect = 0.8f,
    )

    private fun kinds() = DiagramSpec(
        "Three kinds of question, and the small words that decide them.",
        nodes = listOf(
            DNode("mc", "Multiple choice", 0.25f, 0.2f, w = 0.42f, h = 0.24f, sub = "one answer"),
            DNode("ma", "Multiple answer", 0.75f, 0.2f, w = 0.42f, h = 0.24f, sub = "each option true or false"),
            DNode("tf", "True or false", 0.25f, 0.62f, w = 0.42f, h = 0.24f),
            DNode("words", "Small words", 0.75f, 0.62f, w = 0.42f, h = 0.24f, tone = 1, sub = "best, must, not"),
        ),
        aspect = 0.8f,
    )

    private fun scenario() = DiagramSpec(
        "In a scenario question, find whose accountability it is before choosing an answer.",
        nodes = listOf(
            DNode("q", "A scenario question", 0.5f, 0.13f, w = 0.8f, h = 0.18f, kind = NodeKind.NOTE),
            DNode("po", "Product Owner", 0.17f, 0.6f, w = 0.3f, h = 0.3f, sub = "orders the backlog"),
            DNode("devs", "Developers", 0.5f, 0.6f, w = 0.3f, h = 0.3f, tone = 1, sub = "Sprint Backlog, how"),
            DNode("sm", "Scrum Master", 0.83f, 0.6f, w = 0.3f, h = 0.3f, sub = "Scrum understood, used"),
        ),
        edges = listOf(
            DEdge("q", "po"),
            DEdge("q", "devs"),
            DEdge("q", "sm"),
        ),
        aspect = 0.8f,
    )

    private fun practice() = DiagramSpec(
        "Practise with Scrum Open and the mock, trace every miss to the Guide, then sit the real one somewhere quiet.",
        nodes = listOf(
            DNode("open", "Scrum Open", 0.25f, 0.18f, w = 0.42f, h = 0.24f, sub = "free, timed, for reasons"),
            DNode("guide", "The Guide", 0.75f, 0.18f, w = 0.42f, h = 0.24f, tone = 1, sub = "find the sentence"),
            DNode("mock", "Mock assessment", 0.25f, 0.58f, w = 0.42f, h = 0.24f, sub = "80 questions, 60 minutes"),
            DNode("real", "The real one", 0.75f, 0.58f, w = 0.42f, h = 0.24f, tone = 1, sub = "online, quiet room"),
            DNode("note", "Score well above 85, again and again", 0.5f, 0.88f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("open", "guide", "each miss"),
            DEdge("mock", "real", "then"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The assessment's essentials, each as the phrase to remember and what it means for the hour.",
        nodes = listOf(
            DNode("num", "80, 60, 85 percent", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "68 right"),
            DNode("pace", "The pace", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "45 seconds each"),
            DNode("src", "The source", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Scrum Guide 2020"),
            DNode("open", "Scrum Open", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "understand, don't memorise"),
            DNode("words", "Small words", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "best, must, not"),
            DNode("fail", "If you fail", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "inspect and adapt"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("num", "pace", "src", "open", "words", "fail")
    private val roles3 = setOf("po", "devs", "sm")
    private val types3 = setOf("mc", "ma", "tf")
    private val takeaways = BoardCard("Remember", "80, 60, 85 percent: 68 right. 45 seconds each. The 2020 Guide only. Best, must, not.")
    private val today = BoardCard("Today", "Scrum Open on a timer, each miss traced to the Guide, pace checkpoints.")
    private val numbersCard = BoardCard("PSM I", "80 questions, 60 minutes, pass at 85 percent.")
    private val twelve = BoardCard("Twelve wrong, at most", "85 percent of 80 is 68 right answers.")
    private val guideCard = BoardCard("The Scrum Guide", "November 2020, the only source of truth. Short: one evening.")
    private val timeboxes = BoardCard("Timeboxes", "One-month Sprint: Planning 8h, Daily 15 min, Review 4h, Retro 3h.")
    private val commitments = BoardCard("Commitments", "Product Goal, Sprint Goal, Definition of Done. And pillars, values.")
    private val threeKinds = BoardCard("Three kinds", "Multiple choice, multiple answer, true or false. Online.")
    private val paceCard = BoardCard("Forty-five seconds", "An even pace, like a 5 km run: about 20 questions per 15 minutes.")
    private val linger = BoardCard("Don't linger", "Best answer, move on. Lost time costs more than one mark.")
    private val eachOption = BoardCard("Multiple answer", "Each option its own true or false, held against the Guide.")
    private val smallWords = BoardCard("Small words", "Best, must, should, may, not. Read every word.")
    private val twoRight = BoardCard("Two answers sound right?", "Best answer: the one the Guide says most directly.")
    private val accountability = BoardCard("Find the accountability", "Who owns this? Then respect self-management and empiricism.")
    private val openCard = BoardCard("Scrum Open", "Free practice. Don't memorise: trace each miss to the Guide.")
    private val mockCard = BoardCard("Mock assessment", "Eighty questions, sixty minutes, one sitting.")
    private val readyCard = BoardCard("Ready?", "Advice, not a rule: well above 85, again and again, timed.")
    private val dayCard = BoardCard("On the day", "A quiet room, a steady connection, phone out of reach.")
    private val failCard = BoardCard("If you fail", "Another attempt, and you know what to study. Inspect, adapt.")

    private fun classicBoard() = PodcastBoard(
        "psm-assessment",
        listOf(
            // 0: "Hi, and welcome back. Today: the sitting itself..."
            Scene(
                0, "The numbers", numbers(),
                beats = listOf(
                    Beat(0, show = setOf("q", "t", "p"), light = setOf("q", "t", "p"), card = numbersCard),
                    Beat(2, show = setOf("note"), light = setOf("p", "note"), card = twelve),
                ),
            ),
            // 3: "The Scrum Guide."
            Scene(
                3, "One source", source(),
                beats = listOf(
                    Beat(4, light = setOf("guide"), card = guideCard),
                    Beat(6, show = setOf("nums", "note"), light = setOf("nums"), pulse = "guide" to "nums", card = timeboxes),
                    Beat(8, show = setOf("pairs", "pillars"), light = setOf("pairs", "pillars"), pulse = "guide" to "pairs", card = commitments),
                ),
            ),
            // 9: "What do the questions look like?"
            Scene(
                9, "Kinds and pace", kinds(),
                beats = listOf(
                    Beat(10, light = types3, card = threeKinds),
                    Beat(12, card = paceCard),
                    Beat(14, card = linger),
                    Beat(16, light = setOf("ma"), card = eachOption),
                    Beat(18, light = setOf("words"), card = smallWords),
                    Beat(20, card = BoardCard("Must?", "The Product Owner must attend the Daily Scrum: false.")),
                ),
            ),
            // 21: "And the scenario questions?"
            Scene(
                21, "Scenario questions", scenario(),
                beats = listOf(
                    Beat(22, show = roles3, light = roles3, pulse = "q" to "devs", card = accountability),
                ),
            ),
            // 23: "How do I practise? Is there a sample exam?"
            Scene(
                23, "Practice", practice(),
                beats = listOf(
                    Beat(24, light = setOf("open", "guide"), pulse = "open" to "guide", card = openCard),
                    Beat(26, light = setOf("mock"), pulse = "mock" to "real", card = mockCard),
                    Beat(28, light = setOf("note"), card = readyCard),
                    Beat(30, light = setOf("real"), card = dayCard),
                    Beat(32, card = failCard),
                ),
            ),
            // 33: "So what do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = all6, card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-assessment--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Clock That Ran Out..."
            Scene(
                0, "The Clock That Ran Out", numbers(),
                beats = listOf(
                    Beat(0, show = setOf("q", "t"), light = setOf("t"), card = BoardCard("The case", "At question 71 the hour ended. Score: 81 percent.")),
                    Beat(2, show = setOf("p", "note"), light = setOf("p", "note"), card = BoardCard("Three short", "65 right of 70 read. The pass mark is 68. Ten never read.")),
                    Beat(4, show = setOf("pace"), light = setOf("pace"), card = paceCard),
                ),
            ),
            // 5: "Where did her time go?"
            Scene(
                5, "Exhibits one and two", kinds(),
                beats = listOf(
                    Beat(6, light = setOf("ma"), card = BoardCard("Exhibit one", "Six minutes on one multiple answer question.")),
                    Beat(8, card = BoardCard("Each option alone", "Sprint, Planning: yes. Refinement, release planning: no.")),
                    Beat(10, light = setOf("words"), card = BoardCard("Exhibit two", "The Scrum Master must facilitate the Daily Scrum? False.")),
                    Beat(12, card = smallWords),
                ),
            ),
            // 13: "Third suspect: she learnt from the wrong source."
            Scene(
                13, "Exhibit three: the notes", source(),
                beats = listOf(
                    Beat(14, light = setOf("guide"), card = BoardCard("Exhibit three", "Old blog notes: Development Team, roles, three questions.")),
                    Beat(16, show = setOf("nums"), light = setOf("nums"), pulse = "guide" to "nums", card = timeboxes),
                    Beat(18, show = setOf("pairs", "pillars", "note"), light = setOf("pairs", "pillars"), pulse = "guide" to "pairs", card = commitments),
                ),
            ),
            // 19: "Did she practise at all?"
            Scene(
                19, "Exhibit four: practice", practice(),
                beats = listOf(
                    Beat(20, light = setOf("open"), card = BoardCard("Exhibit four", "Scrum Open eleven times: answers remembered, not reasons.")),
                    Beat(22, light = setOf("open", "guide"), pulse = "open" to "guide", card = openCard),
                    Beat(24, light = setOf("guide"), card = BoardCard("Read the Guide", "Until nothing in it surprises you.")),
                ),
            ),
            // 25: "What about the long scenario questions?"
            Scene(
                25, "Scenario questions", scenario(),
                beats = listOf(
                    Beat(26, show = roles3, light = roles3, pulse = "q" to "devs", card = accountability),
                ),
            ),
            // 27: "Anything about the day itself?"
            Scene(
                27, "Exhibit five: the train", practice(),
                beats = listOf(
                    Beat(28, light = setOf("real"), card = BoardCard("Exhibit five", "Sat it on a train, on phone data. Two minutes lost.")),
                    Beat(30, light = setOf("mock"), pulse = "mock" to "real", card = BoardCard("Inspect, adapt", "Ten unread questions: a pacing problem, not knowledge.")),
                ),
            ),
            // 31: "So, the case summary for the exam?"
            Scene(
                31, "Case closed", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = all6, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "psm-assessment--gossip",
        listOf(
            // 0: "Okay, did you hear? The whole running app team booked PSM I..."
            Scene(
                0, "Five ways to sit it", numbers(),
                beats = listOf(
                    Beat(0, card = BoardCard("The running app team", "Five people, one exam, five ways to sit it.")),
                    Beat(2, show = setOf("q", "t", "p"), light = setOf("q", "t", "p"), card = numbersCard),
                    Beat(4, show = setOf("note"), light = setOf("p", "note"), card = BoardCard("Monday: the intern", "Thought the pass mark was 60 right. It's 68.")),
                ),
            ),
            // 5: "Ouch. Tuesday?"
            Scene(
                5, "Tuesday: Tom", practice(),
                beats = listOf(
                    Beat(6, light = setOf("open"), card = BoardCard("Tuesday: Tom", "Scrum Open fourteen times, every answer by heart.")),
                    Beat(8, light = setOf("open", "guide"), pulse = "open" to "guide", card = openCard),
                ),
            ),
            // 9: "Wednesday?"
            Scene(
                9, "Wednesday: Marcus", source(),
                beats = listOf(
                    Beat(10, light = setOf("guide"), card = BoardCard("Wednesday: Marcus", "An old blog: Development Team, roles, the three questions.")),
                    Beat(12, show = setOf("nums", "note"), light = setOf("nums"), pulse = "guide" to "nums", card = timeboxes),
                ),
            ),
            // 13: "Thursday? The train."
            Scene(
                13, "Dana and Priya", numbers(),
                beats = listOf(
                    Beat(14, light = setOf("t"), card = BoardCard("Thursday: Dana", "On a train, on phone data. Three minutes lost to tunnels.")),
                    Beat(16, light = setOf("pace"), card = BoardCard("Friday: Priya", "45 seconds a question; right on time at question 20.")),
                    Beat(18, card = linger),
                ),
            ),
            // 19: "Of course he did. What about the multiple answer ones?"
            Scene(
                19, "Priya's tricks", kinds(),
                beats = listOf(
                    Beat(20, light = setOf("ma"), card = eachOption),
                    Beat(22, light = setOf("words"), card = smallWords),
                ),
            ),
            // 23: "And the long scenario ones?"
            Scene(
                23, "Scenario questions", scenario(),
                beats = listOf(
                    Beat(24, show = roles3, light = roles3, pulse = "q" to "devs", card = accountability),
                ),
            ),
            // 25: "Was there a cram list going round the office?"
            Scene(
                25, "Priya's cram list", source(),
                beats = listOf(
                    Beat(26, light = setOf("pairs", "pillars"), pulse = "guide" to "pairs", card = BoardCard("Priya's cram list", "Three commitments, three pillars, five values, ten or fewer.")),
                    Beat(28, light = setOf("guide"), card = BoardCard("Five readings", "Short enough for an evening; by the fifth, no surprises.")),
                ),
            ),
            // 29: "So, who passed?"
            Scene(
                29, "Who passed", practice(),
                beats = listOf(
                    Beat(30, light = setOf("real"), card = BoardCard("Results", "Priya 94. Dana by one question. Tom and Marcus try again.")),
                    Beat(32, light = setOf("mock"), pulse = "mock" to "real", card = BoardCard("The intern", "This app's mock: 80 questions, a 60-minute timer.")),
                    Beat(34, light = setOf("note"), card = twoRight),
                ),
            ),
            // 35: "So, the gossip I should remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = all6, card = takeaways),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "psm-assessment--late-night",
        listOf(
            // 0: "Hey, you. It's late, and maybe your exam is tomorrow..."
            Scene(
                0, "The hour itself", numbers(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The hour itself, slowly, so you sleep well.")),
                    Beat(2, show = setOf("q", "t", "p", "note"), light = setOf("p", "note"), card = twelve),
                    Beat(4, card = guideCard),
                ),
            ),
            // 5: "What does the Guide ask about most?"
            Scene(
                5, "What it asks most", source(),
                beats = listOf(
                    Beat(6, show = setOf("nums", "note"), light = setOf("nums"), pulse = "guide" to "nums", card = timeboxes),
                    Beat(8, show = setOf("pairs"), light = setOf("pairs"), pulse = "guide" to "pairs", card = BoardCard("Three pairs", "Product Goal, Sprint Goal, Definition of Done.")),
                    Beat(10, show = setOf("pillars"), light = setOf("pillars"), pulse = "guide" to "pillars", card = BoardCard("Pillars and values", "Transparency, inspection, adaptation. Commitment, focus, openness, respect, courage.")),
                ),
            ),
            // 11: "What kinds of questions wait for me?"
            Scene(
                11, "Kinds and pace", kinds(),
                beats = listOf(
                    Beat(12, light = types3, card = threeKinds),
                    Beat(14, card = paceCard),
                    Beat(16, card = linger),
                    Beat(18, card = twoRight),
                    Beat(20, light = setOf("ma"), card = eachOption),
                    Beat(22, light = setOf("words"), card = smallWords),
                ),
            ),
            // 23: "And the long stories?"
            Scene(
                23, "Long stories", scenario(),
                beats = listOf(
                    Beat(24, show = roles3, light = roles3, pulse = "q" to "devs", card = accountability),
                ),
            ),
            // 25: "How do I practise, in the nights before?"
            Scene(
                25, "Practice and rest", practice(),
                beats = listOf(
                    Beat(26, light = setOf("open", "guide"), pulse = "open" to "guide", card = openCard),
                    Beat(28, light = setOf("note"), card = readyCard),
                    Beat(30, light = setOf("real"), card = dayCard),
                    Beat(32, light = setOf("guide"), card = BoardCard("Tonight", "Read the Guide once more, slowly. Then sleep.")),
                    Beat(34, light = setOf("mock"), pulse = "mock" to "real", card = failCard),
                ),
            ),
            // 35: "Last thing. What do I remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = all6, card = takeaways),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-assessment--couples",
        listOf(
            // 0: "Tonight is date night, darling, and I've booked us something special."
            Scene(
                0, "Date night: an exam", numbers(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "A booking for two: PSM I.")),
                    Beat(4, show = setOf("q", "t"), light = setOf("q", "t"), card = BoardCard("Quiz: how many, how long?", "Eighty questions, sixty minutes.")),
                    Beat(7, show = setOf("p", "note"), light = setOf("p", "note"), card = BoardCard("Quiz: the pass mark", "85 percent: 68 right, 12 wrong at most.")),
                    Beat(10, card = threeKinds),
                    Beat(12, card = BoardCard("Quiz: the source", "Where does every question come from?")),
                    Beat(14, card = BoardCard("No kiss", "The Scrum Guide, November 2020. No newer official edition.")),
                    Beat(16, show = setOf("pace"), light = setOf("pace"), card = BoardCard("Quiz: the pace", "How long per question, on average?")),
                    Beat(17, card = paceCard),
                ),
            ),
            // 19: "Then a Guide question, for luck."
            Scene(
                19, "Guide questions", source(),
                beats = listOf(
                    Beat(19, card = BoardCard("Quiz: the Review's timebox", "For a one-month Sprint?")),
                    Beat(20, show = setOf("nums", "note"), light = setOf("nums"), pulse = "guide" to "nums", card = timeboxes),
                    Beat(22, show = setOf("pairs"), light = setOf("pairs"), pulse = "guide" to "pairs", card = BoardCard("Quiz: pair them up", "Three artifacts, three commitments.")),
                    Beat(23, show = setOf("pillars"), card = BoardCard("A perfect match", "Product Goal, Sprint Goal, Definition of Done.")),
                ),
            ),
            // 25: "And if one question takes forever?"
            Scene(
                25, "The scary ones", kinds(),
                beats = listOf(
                    Beat(26, card = linger),
                    Beat(28, light = setOf("ma"), card = BoardCard("Quiz: multiple answer", "Sprint, Planning, refinement, release planning: events?")),
                    Beat(29, card = eachOption),
                    Beat(31, light = setOf("words"), card = BoardCard("Quiz: must attend?", "The Product Owner must attend the Daily Scrum?")),
                    Beat(32, card = BoardCard("False", "It's the Developers' event. Read every small word.")),
                    Beat(34, card = twoRight),
                ),
            ),
            // 36: "And the long scenario questions? A team in trouble?"
            Scene(
                36, "Who owns it", scenario(),
                beats = listOf(
                    Beat(37, show = roles3, light = roles3, pulse = "q" to "devs", card = accountability),
                ),
            ),
            // 38: "Lovely. And how do we practise?"
            Scene(
                38, "Practice", practice(),
                beats = listOf(
                    Beat(39, light = setOf("open"), card = BoardCard("Memorise it all?", "No kiss: the real questions are different.")),
                    Beat(40, light = setOf("open", "guide"), pulse = "open" to "guide", card = openCard),
                    Beat(42, light = setOf("real"), card = BoardCard("The night of", "A quiet room, a steady connection, phone away. Candles optional.")),
                    Beat(44, light = setOf("guide"), card = BoardCard("The night before", "Read the Guide once more; pillars and values; then sleep.")),
                ),
            ),
            // 45: "Last question, for the exam. What do we remember?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = all6, card = takeaways),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
