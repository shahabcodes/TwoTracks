package com.shahaabapps.core.content

/** The five Scrum values: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesValues {
    private val classic = Podcast(
        "psm-values",
        listOf(
            maya("Hi, and welcome back. Today: the five Scrum values. Commitment, focus, openness, respect and courage. They sound like a poster in a corridor, but the P S M one assessment asks about them, and they're what keep the rest of Scrum honest."),
            leo("I'll be honest, values make me roll my eyes. Every company has a poster. Why should a developer care?"),
            maya("Because the Scrum Guide says Scrum only works as well as people live these five. Last episode was the three pillars: transparency, inspection and adaptation. Picture a team where people are afraid of bad news. They hide it, the board stops telling the truth, and the pillars fall over. The values are what hold them up."),
            leo("Okay. List them again, slowly, so they stick."),
            maya("Commitment, focus, openness, respect, courage. If you want a hook: can five ordinary robots code? C, F, O, R, C. [pause] Commitment, focus, openness, respect, courage."),
            leo("[laugh] Can five ordinary robots code. Fine, that'll stick. Commitment first. Committed to what? Finishing every ticket I'm given?"),
            maya("Not to a pile of tickets. The Scrum Guide says the Scrum Team commits to achieving its goals and to supporting each other. The goals are things like the Sprint Goal, the single objective for this Sprint, and the Product Goal, the longer-term one."),
            leo("What's the difference, in practice? Goal or tickets, I still have to ship."),
            maya("Picture a team whose Sprint Goal is: users can sign in with their fingerprint. Halfway through, one piece turns out much harder. Committed to the goal, they talk to the Product Owner, swap the work, and still reach the goal. Committed to every ticket, they'd cut corners on quality or work all weekend."),
            leo("Product Owner, the one person who decides what's most valuable, right?"),
            maya("Right, the one accountable for the value of the product. And the other half of commitment is supporting each other. The backend developer is stuck on a crash; the Android developer pairs with her instead of polishing his own screen. The whole team succeeds or fails together."),
            leo("Focus next. Headphones on, notifications off?"),
            maya("Bigger than that. The Guide says the team's primary focus is on the work of the Sprint, to make the best possible progress toward its goals. For example: a sales manager messages one developer directly, asking for one small change. Focus means it goes to the Product Owner and the Product Backlog, not quietly into this Sprint."),
            leo("Doesn't that make us look unhelpful?"),
            maya("It feels that way, but the request isn't lost. It's on the Product Backlog, the ordered to-do list for the whole product, and the Product Owner decides where it goes. A Sprint is a month or less, so the wait is short. And the team doesn't quietly drift away from the goal it committed to."),
            leo("Openness. Open about what? My salary?"),
            maya("[chuckle] About the work and its challenges. The Guide says the Scrum Team and its stakeholders are open about both. Stakeholders are people outside the team with a stake in the product: users, sales, management. Say the update to the payment library breaks checkout. You say so at today's Daily Scrum, not at the end of the Sprint."),
            leo("And the stakeholders have to be open too?"),
            maya("Both directions, yes. If marketing knows the launch date has moved, they say so. Openness is what feeds transparency, the first pillar. A team that's open about the bad news has a board you can trust."),
            leo("Respect. Everyone's nice to each other?"),
            maya("More precise than nice. The Guide says team members respect each other as capable, independent people, and are respected that way by the people they work with. You don't rewrite the junior developer's code at midnight; you review it with her. And a manager who trusts the team to decide how to do its work is showing respect too."),
            leo("And courage? Sounds dramatic for an app team."),
            maya("It's quiet courage, mostly. The Guide says the team has the courage to do the right thing and to work on tough problems. Saying no to releasing a build that doesn't meet the Definition of Done, the shared quality bar, even when a stakeholder wants it on Friday. Admitting at the Retrospective that you broke the build. Taking on the scary sign-in rewrite."),
            leo("So how do the values actually connect to the pillars?"),
            maya("The Guide says that when the team and the people it works with live these values, the pillars of transparency, inspection and adaptation come to life, and that builds trust. One way to picture it: openness and courage make the truth visible; respect and focus let you inspect it calmly; commitment to the goal makes you adapt."),
            leo("But you can't put respect in a ticket. Where do values stop working?"),
            maya("They can't be forced by a rule, and the Guide doesn't try. It says people learn and explore the values as they work with Scrum's events and artifacts. And it says your decisions, your steps and the way you use Scrum should strengthen the values, not weaken or undermine them. Coaching that is part of the Scrum Master's job, in its own episode."),
            leo("What does undermining look like, on a real team?"),
            maya("For example, a manager who uses the Daily Scrum to blame people for slow tickets. People stop being open, the board gets polished instead of honest, and transparency dies. Or a bonus for the most tickets closed: focus moves from the goal to the count, and nobody helps anybody."),
            leo("Any traps the exam likes?"),
            maya("The impostor in a list. Transparency is a pillar, not a value. Trust, collaboration, honesty and quality are good things, but they're not among the five. And commitment is to the goals and to each other, never to a fixed amount of work."),
            leo("So what do I remember for the exam?"),
            maya("If a question asks for the Scrum values, it's commitment, focus, openness, respect and courage; transparency is a pillar, not a value. Commitment is to the goals and to supporting each other. Openness covers the team and its stakeholders, about the work and its challenges. Respect means capable, independent people. And living the values brings the pillars to life and builds trust."),
            leo("And anything to try today?"),
            maya("Watch your team for one Sprint, and write down one moment where someone showed courage, and one where it was missing. Notice one piece of bad news that took more than a day to surface, and ask which value would have surfaced it sooner. Then read the Scrum Values section of the Scrum Guide; it's a few lines long."),
            leo("Can five ordinary robots code. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-values",
        listOf(
            maya("Welcome to Case Files. Today's case: The Sprint That Said Fine. An Android team held a Daily Scrum every morning for two weeks. Every morning, every developer said: fine, no problems. Then at the Sprint Review, the payment screen didn't work, and the Sprint Goal was missed."),
            leo("[sigh] Detective Leo. Fine, every day, then a crater. My first suspect: lazy developers."),
            maya("Wrong suspect. They worked hard, and late. The exam would look somewhere else: the five Scrum values. Commitment, focus, openness, respect and courage. The Scrum Guide says Scrum only works as well as people live them."),
            leo("Values. Isn't that the poster by the coffee machine?"),
            maya("The poster is the problem; living them is the point. Think back to the three pillars: transparency, inspection and adaptation. When the values go, people stop telling the truth, and the pillars fall over. Let's take the values one at a time, like witnesses. First clue: the payment bug."),
            leo("What about it?"),
            maya("Tom found it on day three. A library update broke the payment flow. He said nothing for a week, hoping to fix it quietly. Every morning: fine."),
            leo("So, openness?"),
            maya("Openness. The Guide says the Scrum Team and its stakeholders are open about the work and its challenges. Stakeholders are the people outside the team with a stake in the product: users, sales, management. Under the picture, openness is what feeds transparency. A bug nobody mentions isn't on any board."),
            leo("Then my second suspect: the Scrum Master. They should have checked every ticket and assigned the fix."),
            maya("[pause] Also wrong, and a classic trap. The Scrum Master doesn't assign work; the team is self-managing, it decides who does what. The real question is why Tom stayed quiet. Second clue: who came to the Daily Scrum."),
            leo("Who came?"),
            maya("The department head. Every morning, he stood at the back and asked why tickets were slow, by name. So people learnt to say fine."),
            leo("So that's respect?"),
            maya("Respect, and courage. The Guide says team members respect each other as capable, independent people, and are respected that way by the people they work with. Being blamed by name isn't that. And courage means doing the right thing and working on tough problems. Saying I'm stuck, out loud, takes courage, and the room has to make that safe."),
            leo("Third clue?"),
            maya("The board. Halfway through the Sprint, a sales manager messaged Priya directly and asked for a new banner on the home screen. Priya built it. Two days gone."),
            leo("Focus, then."),
            maya("Focus. The Guide says the team's primary focus is on the work of the Sprint, to make the best progress toward its goals. Priya's request belonged with the Product Owner, the one person accountable for the product's value, on the Product Backlog, the ordered list of everything the product might need."),
            leo("And commitment? Everyone stayed late. That's committed."),
            maya("Committed to what, though? Fourth clue: each developer guarded their own tickets. Nobody helped Tom. The Guide says the team commits to achieving its goals and to supporting each other. The goal here was the Sprint Goal, the single objective of the Sprint. Late nights on your own tickets aren't commitment to the goal."),
            leo("So with commitment to the goal, what would have happened?"),
            maya("On day three, Tom says the payment flow is broken. Two developers pair with him. They talk to the Product Owner, drop the banner, adjust the work, and still reach the goal. Same people, same skills, different values."),
            leo("And the department head at the back of the room?"),
            maya("Respect runs both ways: the team is respected as capable, independent people by those it works with. The Daily Scrum is for the Developers. His place to see progress is the Sprint Review, where stakeholders look at the work with the team."),
            leo("Can't we just write a rule? Thou shalt be open."),
            maya("That's where it stops working. You can't force a value with a rule. The Guide says people learn and explore the values as they work with Scrum's events and artifacts, and that decisions and the way Scrum is used should strengthen the values, not undermine them. Blaming people at the Daily Scrum undermines them. The Scrum Master episode covers coaching the room."),
            leo("And when the values are lived?"),
            maya("The Guide says the pillars come to life, and that builds trust. Trust is the result, by the way, not one of the five. The exam likes to slip trust, collaboration or transparency into a list of values."),
            leo("So, the case summary for the exam?"),
            maya("The Scrum values are commitment, focus, openness, respect and courage; transparency is a pillar, not a value. Commitment is to the goals and to supporting each other. Openness covers the team and its stakeholders, about the work and its challenges. Respect means capable, independent people. Living the values brings the pillars to life and builds trust."),
            leo("Case closed. And to try today?"),
            maya("Watch your team for one Sprint, and write down one moment where someone showed courage, and one where it was missing. Notice one piece of bad news that took more than a day to surface, and ask which value would have surfaced it sooner. Then read the Scrum Values section of the Scrum Guide; it's a few lines long."),
            leo("Say it at the Daily Scrum, not at the Review. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-values",
        listOf(
            maya("Okay, did you hear what happened on the app team this week? The Scrum Master put the five Scrum values on the wall. Commitment, focus, openness, respect, courage. And then, one by one, everybody broke one."),
            leo("[laugh] I heard. Start with why there's a list on the wall at all."),
            maya("The Scrum Guide says Scrum works only as well as people live those five. They hold up the three pillars: transparency, inspection and adaptation. If people are scared or distracted, the truth stops reaching the board. A hook to remember them: can five ordinary robots code. C, F, O, R, C."),
            leo("Isn't this a bit soft, for an exam about rules?"),
            maya("The exam asks for them directly, and sideways too: a story where someone hides a problem is really a question about openness and transparency. Under the picture, the values are simply how people behave when things go wrong, so the information Scrum runs on stays honest."),
            leo("Can five ordinary robots code. Okay. Monday?"),
            maya("Monday was Tom. Tom found a bug in the payment flow after a library update. And he said nothing. For a week. Every Daily Scrum: fine, no problems."),
            leo("Classic Tom. Which value?"),
            maya("Openness. The Guide says the Scrum Team and its stakeholders are open about the work and its challenges. Stakeholders are people outside the team with a stake in the product: users, sales, the bosses. Under the picture, openness is what keeps transparency true. Tom's silence made the board lie."),
            leo("Tuesday?"),
            maya("Tuesday was Priya. A sales manager messaged her directly for a new banner on the home screen. Priya, being lovely, just built it. Two days, gone from the Sprint."),
            leo("Helpful, though."),
            maya("Helpful to one person. The value is focus: the Guide says the team's primary focus is on the work of the Sprint, to make the best progress toward its goals. The banner belonged with the Product Owner, the one person accountable for the product's value, on the Product Backlog, the ordered list for the whole product. Not slipped in quietly."),
            leo("Wednesday. I bet it's Marcus."),
            maya("It's Marcus. Marcus didn't like the intern's code for the settings screen. So at midnight he rewrote all of it, without a word."),
            leo("[chuckle] Oof. Respect?"),
            maya("Respect. The Guide says team members respect each other as capable, independent people, and are respected that way by the people they work with. You review the intern's code with her; you don't erase it in the dark. And respect runs both ways: managers respect the team's right to decide how it works."),
            leo("Thursday?"),
            maya("Thursday was Dana. A stakeholder wanted the release in the Play Store by Friday. The build didn't meet the Definition of Done, the shared quality bar every piece of work must meet. Dana knew. She shipped it anyway, because saying no felt too awkward."),
            leo("And Friday it crashed."),
            maya("Friday it crashed. The value is courage: the Guide says the team has the courage to do the right thing and to work on tough problems. Saying this isn't done, not yet, to someone senior is exactly that courage."),
            leo("So what should Dana have said?"),
            maya("Something like: it isn't done yet, here's what's missing. Work that doesn't meet the Definition of Done isn't released; it goes back to the Product Backlog for later. The Increment episode has the details."),
            leo("And Friday's culprit?"),
            maya("The department head. He announced a bonus for whoever closes the most tickets. By lunch, nobody was helping anybody."),
            leo("That's commitment, isn't it? People committed to their tickets."),
            maya("That's the trap. The Guide says the team commits to achieving its goals and to supporting each other. The goals are the Sprint Goal, the single objective of the Sprint, and the Product Goal. A ticket race kills both halves. And the Guide says decisions and the way Scrum is used should strengthen the values, not undermine them."),
            leo("So can the Scrum Master just make a rule? No more Toms?"),
            maya("That's where values stop working. You can't force them with a rule. The Guide says people learn and explore the values as they work with Scrum's events and artifacts. A Scrum Master coaches and makes the room safe; the Scrum Master episode has more. When the values are lived, the pillars come to life, and that builds trust."),
            leo("And trust is a value too?"),
            maya("Trust is the result, not one of the five. The exam's favourite trick: a list with an impostor. Trust, collaboration, honesty, or transparency, which is a pillar."),
            leo("So, the gossip I should remember for the exam?"),
            maya("The Scrum values are commitment, focus, openness, respect and courage; transparency is a pillar, not a value. Commitment is to the goals and to supporting each other. Openness covers the team and its stakeholders, about the work and its challenges. Respect means capable, independent people. Living the values brings the pillars to life and builds trust."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Watch your team for one Sprint, and write down one moment where someone showed courage, and one where it was missing. Notice one piece of bad news that took more than a day to surface, and ask which value would have surfaced it sooner. Then read the Scrum Values section of the Scrum Guide; it's a few lines long."),
            leo("Say it out loud, Tom. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-values",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning for P S M one. [pause] Tonight: five small words that decide whether a team tells the truth."),
            ivy("Five words, this late at night. I can manage five. Go on, Nova."),
            nova("The five Scrum values. Commitment, focus, openness, respect, courage. [pause] The Scrum Guide says Scrum only works as well as people live them."),
            ivy("Why so much weight on five words?"),
            nova("Because they hold up the pillars. Transparency, inspection, adaptation, from the empiricism episode. If people are afraid, they hide bad news. The board stops telling the truth. And then everything you inspect is a story."),
            ivy("Under the poster, then. What are they really?"),
            nova("Behaviour. [pause] The way people act when things go wrong. The pillars need honest information, and people only give it when they feel safe, focused and respected."),
            ivy("Help me keep them in my head, at this hour."),
            nova("[chuckle] Can five ordinary robots code. C, F, O, R, C. Commitment, focus, openness, respect, courage."),
            ivy("Commitment first, then. Committed to what? Every ticket on the board?"),
            nova("Not the tickets. The Guide says the Scrum Team commits to achieving its goals, and to supporting each other. [pause] The goals are the Sprint Goal, the single objective for this Sprint, and the Product Goal, the longer one."),
            ivy("Show me the difference."),
            nova("Picture a team whose Sprint Goal is fingerprint sign-in for their Android app. Halfway through, one piece turns out much harder. Committed to the goal, they talk to the Product Owner, the one person accountable for the product's value, and change the work. Committed to tickets, they cut quality, or lose their weekend."),
            ivy("And supporting each other?"),
            nova("When the backend developer is stuck on a crash, the Android developer pairs with her, instead of polishing his own screen. The team rises or falls together."),
            ivy("Even if my own work slips?"),
            nova("Even then. The whole Scrum Team is accountable for a valuable, useful Increment every Sprint, not one person's tickets. [pause] Nobody wins alone."),
            ivy("Focus next. Like now, with the lights low?"),
            nova("[laugh] Something like that. The Guide says the primary focus is on the work of the Sprint, to make the best possible progress toward the goals. A sales manager messages you for one small change. Focus sends it to the Product Owner, onto the Product Backlog, the ordered list for the whole product. [pause] Not quietly into tonight's work."),
            ivy("Openness. That sounds intimate."),
            nova("It's about the work. The Guide says the Scrum Team and its stakeholders are open about the work and its challenges. Stakeholders are people outside the team with a stake in the product. The payment flow broke on day three? You say it at the next Daily Scrum. Not a week later."),
            ivy("And respect?"),
            nova("Team members respect each other as capable, independent people, and are respected that way by the people they work with. You review a junior's code with her. You don't quietly rewrite it at midnight. [pause] Midnight has better uses."),
            ivy("And courage. The last one."),
            nova("Courage to do the right thing, and to work on tough problems. Saying no to a release that doesn't meet the Definition of Done, the shared quality bar, even when someone senior wants it by Friday. Admitting you broke the build. [pause] Quiet courage, mostly."),
            ivy("And the build that isn't done. Where does it go?"),
            nova("Not to the Play Store. Work that doesn't meet the Definition of Done goes back to the Product Backlog, for later. [pause] The Increment episode tells that story."),
            ivy("And when a team really lives all five?"),
            nova("The Guide says the pillars come to life, and that builds trust. Openness and courage put the truth on the board. Respect and focus let you look at it calmly. And commitment to the goal makes you change course when you need to."),
            ivy("Can't a manager just order people to be open?"),
            nova("That's where it stops working. A value can't be forced with a rule. The Guide says people learn and explore the values as they work with Scrum's events and artifacts. And decisions should strengthen the values, not undermine them. A manager blaming people at the Daily Scrum undermines all five. The Scrum Master episode is about coaching that room."),
            ivy("Any trap the exam sets, in the dark?"),
            nova("A list with an impostor. Transparency is a pillar, not a value. Trust is the result, not a value. Collaboration and honesty are lovely, [pause] but they're not among the five."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("The Scrum values are commitment, focus, openness, respect and courage; transparency is a pillar, not a value. Commitment is to the goals and to supporting each other. Openness covers the team and its stakeholders, about the work and its challenges. Respect means capable, independent people. Living the values brings the pillars to life and builds trust."),
            ivy("And something to try today, before I sleep?"),
            nova("Watch your team for one Sprint, and write down one moment where someone showed courage, and one where it was missing. [pause] Notice one piece of bad news that took more than a day to surface, and ask which value would have surfaced it sooner. Then read the Scrum Values section of the Scrum Guide; it's a few lines long."),
            ivy("Five small words. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-values",
        listOf(
            nova("Tonight is date night, darling. And tonight we talk about values."),
            rafe("Our values? Already? We've only just ordered."),
            nova("[laugh] Scrum's values, love. Five of them. The Scrum Guide says Scrum only works as well as people live them, because they hold up the three pillars: transparency, inspection and adaptation."),
            rafe("Hold up the pillars. I like a strong foundation. Test me."),
            nova("Exam question, then. Name the five Scrum values."),
            rafe("Commitment, focus, openness, respect and courage. Can five ordinary robots code: C, F, O, R, C."),
            nova("Right answer, and a robot joke. [kiss]"),
            rafe("Commitment first. I'm very committed, you know."),
            nova("To what, though? The Guide says the Scrum Team commits to achieving its goals and to supporting each other. The goals are the Sprint Goal, the single objective for the Sprint, and the Product Goal, the longer-term one. Not to finishing every ticket."),
            rafe("Show me the difference on our app."),
            nova("Picture a Sprint Goal: fingerprint sign-in for our Android app. Halfway through, one piece turns out much harder. Committed to the goal, the team talks to the Product Owner, the one person accountable for the product's value, swaps the work, and still reaches the goal. Committed to tickets, they cut quality."),
            rafe("Then my question. A teammate is stuck on a crash, and you could keep polishing your own screen instead. What does commitment say?"),
            nova("Help her. Supporting each other is half of commitment. The team succeeds or fails together."),
            rafe("Even if my own screen slips?"),
            nova("Even then. The whole Scrum Team is accountable for a valuable, useful Increment every Sprint, not one person's tickets."),
            rafe("Right answer. Supportive, too. [kiss]"),
            nova("Focus next. The Guide says the primary focus is on the work of the Sprint, to make the best possible progress toward the goals. So, quiz. A sales manager messages a developer directly for one small change mid-Sprint. What should happen?"),
            rafe("Easy. Just do it, it's small, and it keeps sales happy."),
            nova("No kiss for that one. Focus sends it to the Product Owner, onto the Product Backlog, the ordered list for the whole product. The Product Owner decides where it goes. It isn't lost, and the Sprint stays pointed at its goal."),
            rafe("Fair. I'll keep my focus on you, then. Openness?"),
            nova("The Guide says the Scrum Team and its stakeholders are open about the work and its challenges. Stakeholders are people outside the team with a stake in the product: users, sales, management. If the payment flow breaks on day three, you say so at the next Daily Scrum."),
            rafe("Not a week later, hoping to fix it quietly."),
            nova("Never. Openness is what keeps transparency true. Respect, now. Team members respect each other as capable, independent people, and are respected that way by the people they work with."),
            rafe("So I don't rewrite the junior's code at midnight."),
            nova("You review it with her. Midnight is for other things. My turn to ask. A stakeholder wants the release in the Play Store on Friday, but the build doesn't meet the Definition of Done, the shared quality bar. Which value says no?"),
            rafe("Courage. The courage to do the right thing and to work on tough problems, even when someone senior is waiting."),
            nova("Right answer. Brave, and handsome. [kiss]"),
            rafe("And the unfinished build? Where does it go?"),
            nova("Back to the Product Backlog. Work that doesn't meet the Definition of Done isn't released. The Increment episode is all about that."),
            rafe("Now a trap. Is transparency one of the five values?"),
            nova("No. Transparency is a pillar. Trust isn't one either; trust is what you get. The Guide says when the values are lived, the pillars come to life and that builds trust. Collaboration and honesty aren't on the list either."),
            rafe("Right answer. Trust earned. [kiss]"),
            nova("And where do values stop working?"),
            rafe("When someone tries to force them with a rule. The Guide says people learn and explore the values as they work with Scrum's events and artifacts, and decisions should strengthen the values, not undermine them. A manager blaming people at the Daily Scrum undermines all five. The Scrum Master episode covers coaching that."),
            nova("And how do you tell the values are working?"),
            rafe("People say I'm stuck at the Daily Scrum without fear, and the board stays honest. Under the picture, the values are just how people behave when things go wrong."),
            nova("Then the last question, for the exam. What do we remember?"),
            rafe("The Scrum values are commitment, focus, openness, respect and courage; transparency is a pillar, not a value. Commitment is to the goals and to supporting each other. Openness covers the team and its stakeholders, about the work and its challenges. Respect means capable, independent people. Living the values brings the pillars to life and builds trust."),
            nova("All five, darling. That earns a long one. [kiss]"),
            rafe("Mm. And something to try today?"),
            nova("Watch your team for one Sprint, and write down one moment where someone showed courage, and one where it was missing. Notice one piece of bad news that took more than a day to surface, and ask which value would have surfaced it sooner. Then read the Scrum Values section of the Scrum Guide; it's a few lines long."),
            rafe("Courage, then. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun fiveValues() = DiagramSpec(
        "The five Scrum values; lived by the team, they hold up transparency, inspection and adaptation.",
        nodes = listOf(
            DNode("commit", "Commitment", 0.18f, 0.16f, w = 0.3f, h = 0.2f, tone = 1, sub = "to the goals"),
            DNode("focus", "Focus", 0.5f, 0.16f, w = 0.3f, h = 0.2f, tone = 1, sub = "the Sprint's work"),
            DNode("open", "Openness", 0.82f, 0.16f, w = 0.3f, h = 0.2f, tone = 1, sub = "about the work"),
            DNode("respect", "Respect", 0.32f, 0.5f, w = 0.3f, h = 0.2f, tone = 1, sub = "capable people"),
            DNode("courage", "Courage", 0.68f, 0.5f, w = 0.3f, h = 0.2f, tone = 1, sub = "the right thing"),
            DNode("pillars", "The three pillars", 0.5f, 0.85f, w = 0.9f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "transparency, inspection, adaptation"),
        ),
        edges = listOf(
            DEdge("respect", "pillars"),
            DEdge("courage", "pillars"),
            DEdge("open", "pillars", "feeds"),
        ),
        aspect = 0.8f,
    )

    private fun commitment() = DiagramSpec(
        "Commitment is to the goal and to each other: the work can change with the Product Owner, the goal stays.",
        nodes = listOf(
            DNode("goal", "Sprint Goal", 0.5f, 0.15f, w = 0.6f, h = 0.2f, tone = 1, sub = "the one objective"),
            DNode("work", "The work", 0.25f, 0.52f, w = 0.42f, h = 0.2f, sub = "can change"),
            DNode("po", "Product Owner", 0.75f, 0.52f, w = 0.42f, h = 0.2f, sub = "agrees the swap"),
            DNode("team", "Support each other", 0.5f, 0.86f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("work", "goal", "toward"),
            DEdge("po", "work", "renegotiate"),
            DEdge("team", "goal"),
        ),
        aspect = 0.8f,
    )

    private fun focus() = DiagramSpec(
        "A request in the middle of a Sprint goes to the Product Owner and the Product Backlog, not quietly to a developer.",
        nodes = listOf(
            DNode("sales", "Sales manager", 0.25f, 0.15f, w = 0.42f, h = 0.2f, tone = 2, sub = "one small change"),
            DNode("dev", "A developer", 0.75f, 0.15f, w = 0.42f, h = 0.2f, tone = 2),
            DNode("po", "Product Owner", 0.25f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "decides where"),
            DNode("pb", "Product Backlog", 0.25f, 0.85f, w = 0.42f, h = 0.2f, sub = "ordered list"),
            DNode("sprint", "This Sprint", 0.75f, 0.68f, w = 0.42f, h = 0.24f, sub = "stays on its goal"),
        ),
        edges = listOf(
            DEdge("sales", "dev", "not quietly", dashed = true),
            DEdge("sales", "po", "ask"),
            DEdge("po", "pb", "orders"),
        ),
        aspect = 0.8f,
    )

    private fun behaviour() = DiagramSpec(
        "Openness, respect and courage, each with what it looks like on an Android team.",
        nodes = listOf(
            DNode("o1", "Openness", 0.25f, 0.16f, w = 0.42f, h = 0.2f, tone = 1),
            DNode("o2", "Say it at the Daily Scrum", 0.75f, 0.16f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("r1", "Respect", 0.25f, 0.5f, w = 0.42f, h = 0.2f, tone = 1),
            DNode("r2", "Review it, don't rewrite it", 0.75f, 0.5f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("c1", "Courage", 0.25f, 0.84f, w = 0.42f, h = 0.2f, tone = 1),
            DNode("c2", "Not done? Don't ship it", 0.75f, 0.84f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("o1", "o2"),
            DEdge("r1", "r2"),
            DEdge("c1", "c2"),
        ),
        aspect = 0.8f,
    )

    private fun trust() = DiagramSpec(
        "When the values are lived, the pillars come to life, and that builds trust. Trust is the result, not a value.",
        nodes = listOf(
            DNode("vals", "Five values, lived", 0.5f, 0.15f, w = 0.8f, h = 0.2f, tone = 1, sub = "not forced by a rule"),
            DNode("pil", "The pillars come to life", 0.5f, 0.5f, w = 0.8f, h = 0.2f, sub = "transparency, inspection, adaptation"),
            DNode("trust", "Trust", 0.5f, 0.85f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "the result"),
        ),
        edges = listOf(
            DEdge("vals", "pil"),
            DEdge("pil", "trust", "builds"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's values questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("five", "The five values", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "C, F, O, R, C"),
            DNode("transp", "Transparency?", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a pillar, not a value"),
            DNode("commit", "Commitment to", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "the goals, each other"),
            DNode("open", "Openness", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "team and stakeholders"),
            DNode("respect", "Respect", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "capable, independent"),
            DNode("trust", "Values lived", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "pillars live, trust grows"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("five", "transp", "commit", "open", "respect", "trust")
    private val values5 = setOf("commit", "focus", "open", "respect", "courage")
    private val focusRest = setOf("po", "pb", "sprint")
    private val takeaways = BoardCard("Listen for the phrase", "Commitment, focus, openness, respect, courage. Transparency is a pillar.")
    private val today = BoardCard("Today", "Courage seen and missed; slow bad news; which value; read Scrum Values.")

    private fun classicBoard() = PodcastBoard(
        "psm-values",
        listOf(
            // 0: "Hi, and welcome back. Today: the five Scrum values..."
            Scene(
                0, "Five small words", fiveValues(),
                beats = listOf(
                    Beat(0, card = BoardCard("The five Scrum values", "Commitment, focus, openness, respect, courage.")),
                    Beat(2, show = setOf("pillars"), light = setOf("pillars"), card = BoardCard("Why a developer cares", "Hide bad news and the board lies: the pillars fall over.")),
                    Beat(4, show = values5, light = values5, card = BoardCard("Can five ordinary robots code", "Commitment, Focus, Openness, Respect, Courage.")),
                ),
            ),
            // 5: "Commitment first. Committed to what?"
            Scene(
                5, "Commitment", commitment(),
                beats = listOf(
                    Beat(6, show = setOf("goal"), light = setOf("goal"), card = BoardCard("Commitment", "To the team's goals, and to supporting each other.")),
                    Beat(8, show = setOf("work", "po"), light = setOf("work", "po"), pulse = "po" to "work", card = BoardCard("Goal, not tickets", "Harder than planned? Swap the work with the Product Owner.")),
                    Beat(10, show = setOf("team"), light = setOf("team"), pulse = "team" to "goal", card = BoardCard("Support each other", "Pair on the crash instead of polishing your own screen.")),
                ),
            ),
            // 11: "Focus next. Headphones on, notifications off?"
            Scene(
                11, "Focus", focus(),
                beats = listOf(
                    Beat(12, show = setOf("sales", "dev"), light = setOf("sales", "dev"), pulse = "sales" to "dev", card = BoardCard("Focus", "On the work of the Sprint, toward its goals.")),
                    Beat(14, show = focusRest, light = setOf("po", "pb"), pulse = "po" to "pb", card = BoardCard("Not lost", "On the Product Backlog; the Product Owner decides where.")),
                ),
            ),
            // 15: "Openness. Open about what? My salary?"
            Scene(
                15, "Openness, respect, courage", behaviour(),
                beats = listOf(
                    Beat(16, show = setOf("o1", "o2"), light = setOf("o1", "o2"), pulse = "o1" to "o2", card = BoardCard("Openness", "Team and stakeholders, open about the work and its challenges.")),
                    Beat(18, light = setOf("o1"), card = BoardCard("It feeds transparency", "A team open about bad news has a board you can trust.")),
                    Beat(20, show = setOf("r1", "r2"), light = setOf("r1", "r2"), pulse = "r1" to "r2", card = BoardCard("Respect", "Capable, independent people, and respected as such.")),
                    Beat(22, show = setOf("c1", "c2"), light = setOf("c1", "c2"), pulse = "c1" to "c2", card = BoardCard("Courage", "Do the right thing, and work on tough problems.")),
                ),
            ),
            // 23: "So how do the values actually connect to the pillars?"
            Scene(
                23, "Values and the pillars", trust(),
                beats = listOf(
                    Beat(24, light = setOf("vals", "pil", "trust"), pulse = "vals" to "pil", card = BoardCard("The pillars come to life", "Values lived by the team and those around it build trust.")),
                    Beat(26, light = setOf("vals"), card = BoardCard("Can't be forced", "Learnt through the events and artifacts; never undermine them.")),
                    Beat(28, card = BoardCard("Undermining", "Blame at the Daily Scrum; a bonus for tickets closed.")),
                ),
            ),
            // 29: "Any traps the exam likes?"
            Scene(
                29, "The impostor", fiveValues(),
                beats = listOf(
                    Beat(30, light = setOf("pillars"), card = BoardCard("Not a value", "Transparency is a pillar. Trust is the result. Neither is one of five.")),
                ),
            ),
            // 31: "So what do I remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = all6, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-values--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Sprint That Said Fine..."
            Scene(
                0, "The Sprint That Said Fine", fiveValues(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Fine every morning; the Sprint Goal missed at the Review.")),
                    Beat(2, show = values5, light = values5, card = BoardCard("Suspect cleared: laziness", "They worked hard. Look at the five values instead.")),
                    Beat(4, show = setOf("pillars"), light = setOf("pillars"), pulse = "open" to "pillars", card = BoardCard("Values hold the pillars", "Lose them and people stop telling the truth.")),
                ),
            ),
            // 5: "What about it?"
            Scene(
                5, "Clue one: the payment bug", behaviour(),
                beats = listOf(
                    Beat(6, show = setOf("o1"), light = setOf("o1"), card = BoardCard("Silent for a week", "Tom found the bug on day three, and said fine.")),
                    Beat(8, show = setOf("o2"), light = setOf("o1", "o2"), pulse = "o1" to "o2", card = BoardCard("Openness", "Team and stakeholders, open about work and challenges.")),
                ),
            ),
            // 9: "Then my second suspect: the Scrum Master."
            Scene(
                9, "Clue two: who came", behaviour(),
                beats = listOf(
                    Beat(10, card = BoardCard("Suspect cleared: Scrum Master", "The Scrum Master doesn't assign work; the team self-manages.")),
                    Beat(12, card = BoardCard("The department head", "At the back every morning, asking by name why tickets were slow.")),
                    Beat(14, show = setOf("r1", "r2", "c1", "c2"), light = setOf("r1", "c1"), pulse = "r1" to "r2", card = BoardCard("Respect and courage", "Capable people, in a room where saying I'm stuck is safe.")),
                ),
            ),
            // 15: "Third clue?"
            Scene(
                15, "Clue three: the banner", focus(),
                beats = listOf(
                    Beat(16, show = setOf("sales", "dev"), light = setOf("sales", "dev"), pulse = "sales" to "dev", card = BoardCard("A banner, two days", "Asked for directly, built quietly, mid-Sprint.")),
                    Beat(18, show = focusRest, light = setOf("po", "pb"), pulse = "sales" to "po", card = BoardCard("Focus", "The Sprint's work first; requests go to the Product Owner.")),
                ),
            ),
            // 19: "And commitment? Everyone stayed late."
            Scene(
                19, "Clue four: lone tickets", commitment(),
                beats = listOf(
                    Beat(20, show = setOf("goal"), light = setOf("goal"), card = BoardCard("Committed to what?", "The goals and each other, not your own tickets.")),
                    Beat(22, show = setOf("work", "po", "team"), light = setOf("team", "po"), pulse = "po" to "work", card = BoardCard("Same people, other values", "Pair on the bug, drop the banner, still reach the goal.")),
                    Beat(24, card = BoardCard("Respect, both ways", "The Daily Scrum is the Developers'. His place: the Sprint Review.")),
                ),
            ),
            // 25: "Can't we just write a rule? Thou shalt be open."
            Scene(
                25, "No rule for values", trust(),
                beats = listOf(
                    Beat(26, light = setOf("vals"), card = BoardCard("Can't be forced", "Learnt through events and artifacts; blame undermines them.")),
                    Beat(28, light = setOf("pil", "trust"), pulse = "pil" to "trust", card = BoardCard("Trust is the result", "Not a value. Nor are collaboration or transparency.")),
                ),
            ),
            // 29: "So, the case summary for the exam?"
            Scene(
                29, "Case closed", exam(),
                beats = listOf(
                    Beat(30, show = all6, light = all6, card = takeaways),
                    Beat(32, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "psm-values--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the app team this week?..."
            Scene(
                0, "Five values on the wall", fiveValues(),
                beats = listOf(
                    Beat(0, show = values5, light = values5, card = BoardCard("On the wall", "Commitment, focus, openness, respect, courage.")),
                    Beat(2, show = setOf("pillars"), light = setOf("pillars"), pulse = "open" to "pillars", card = BoardCard("Why a list", "They hold up transparency, inspection and adaptation.")),
                    Beat(4, light = values5, card = BoardCard("Under the picture", "How people behave when things go wrong.")),
                ),
            ),
            // 5: "Can five ordinary robots code. Okay. Monday?"
            Scene(
                5, "Monday: Tom", behaviour(),
                beats = listOf(
                    Beat(6, show = setOf("o1"), light = setOf("o1"), card = BoardCard("A week of fine", "A payment bug, found on day three and kept quiet.")),
                    Beat(8, show = setOf("o2"), light = setOf("o1", "o2"), pulse = "o1" to "o2", card = BoardCard("Openness", "Team and stakeholders, open about work and challenges.")),
                ),
            ),
            // 9: "Tuesday?"
            Scene(
                9, "Tuesday: Priya", focus(),
                beats = listOf(
                    Beat(10, show = setOf("sales", "dev"), light = setOf("sales", "dev"), pulse = "sales" to "dev", card = BoardCard("A banner, quietly", "Two days gone from the Sprint.")),
                    Beat(12, show = focusRest, light = setOf("po", "pb"), pulse = "sales" to "po", card = BoardCard("Focus", "Requests go to the Product Owner and the Product Backlog.")),
                ),
            ),
            // 13: "Wednesday. I bet it's Marcus."
            Scene(
                13, "Wednesday: Marcus", behaviour(),
                beats = listOf(
                    Beat(14, show = setOf("r1"), light = setOf("r1"), card = BoardCard("Rewritten at midnight", "The intern's code, without a word.")),
                    Beat(16, show = setOf("r2"), light = setOf("r1", "r2"), pulse = "r1" to "r2", card = BoardCard("Respect", "Capable, independent people. Review it with her.")),
                ),
            ),
            // 17: "Thursday?"
            Scene(
                17, "Thursday: Dana", behaviour(),
                beats = listOf(
                    Beat(18, show = setOf("c1"), light = setOf("c1"), card = BoardCard("Shipped anyway", "Not done, but saying no felt too awkward.")),
                    Beat(20, show = setOf("c2"), light = setOf("c1", "c2"), pulse = "c1" to "c2", card = BoardCard("Courage", "Do the right thing, and work on tough problems.")),
                    Beat(22, card = BoardCard("Not done? Back it goes", "Short of the Definition of Done: back to the Product Backlog.")),
                ),
            ),
            // 23: "And Friday's culprit?"
            Scene(
                23, "Friday: a bonus per ticket", commitment(),
                beats = listOf(
                    Beat(24, show = setOf("goal"), light = setOf("goal"), card = BoardCard("A ticket race", "By lunch, nobody was helping anybody.")),
                    Beat(26, show = setOf("work", "po", "team"), light = setOf("team", "goal"), pulse = "team" to "goal", card = BoardCard("Commitment", "To the goals and to each other. A ticket race kills both.")),
                ),
            ),
            // 27: "So can the Scrum Master just make a rule?"
            Scene(
                27, "No rule for values", trust(),
                beats = listOf(
                    Beat(28, light = setOf("vals", "pil"), pulse = "vals" to "pil", card = BoardCard("Can't be forced", "Learnt through events and artifacts; lived, they build trust.")),
                    Beat(30, light = setOf("trust"), pulse = "pil" to "trust", card = BoardCard("Trust is the result", "Not a value. Nor collaboration, honesty or transparency.")),
                ),
            ),
            // 31: "So, the gossip I should remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = all6, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "psm-values--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "Five small words", fiveValues(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Five small words that decide whether a team tells the truth.")),
                    Beat(2, show = values5, light = values5, card = BoardCard("The five values", "Commitment, focus, openness, respect, courage.")),
                    Beat(4, show = setOf("pillars"), light = setOf("pillars"), pulse = "open" to "pillars", card = BoardCard("They hold up the pillars", "Afraid people hide bad news; the board stops telling the truth.")),
                    Beat(6, light = values5, card = BoardCard("Under the poster", "Behaviour: how people act when things go wrong.")),
                    Beat(8, card = BoardCard("Can five ordinary robots code", "C, F, O, R, C.")),
                ),
            ),
            // 9: "Commitment first, then."
            Scene(
                9, "Commitment", commitment(),
                beats = listOf(
                    Beat(10, show = setOf("goal"), light = setOf("goal"), card = BoardCard("Commitment", "To the goals, and to supporting each other.")),
                    Beat(12, show = setOf("work", "po"), light = setOf("work", "po"), pulse = "po" to "work", card = BoardCard("Goal, not tickets", "Harder than planned? Change the work with the Product Owner.")),
                    Beat(14, show = setOf("team"), light = setOf("team"), pulse = "team" to "goal", card = BoardCard("Support each other", "Pair on the crash; the team rises or falls together.")),
                    Beat(16, card = BoardCard("Nobody wins alone", "The whole Scrum Team is accountable for a useful Increment.")),
                ),
            ),
            // 17: "Focus next. Like now, with the lights low?"
            Scene(
                17, "Focus", focus(),
                beats = listOf(
                    Beat(18, show = setOf("sales", "dev") + focusRest, light = setOf("sales", "po"), pulse = "sales" to "po", card = BoardCard("Focus", "The Sprint's work; requests go to the Product Owner.")),
                ),
            ),
            // 19: "Openness. That sounds intimate."
            Scene(
                19, "Open, respectful, brave", behaviour(),
                beats = listOf(
                    Beat(20, show = setOf("o1", "o2"), light = setOf("o1", "o2"), pulse = "o1" to "o2", card = BoardCard("Openness", "Team and stakeholders, open about the work and its challenges.")),
                    Beat(22, show = setOf("r1", "r2"), light = setOf("r1", "r2"), pulse = "r1" to "r2", card = BoardCard("Respect", "Capable, independent people. Review, don't rewrite.")),
                    Beat(24, show = setOf("c1", "c2"), light = setOf("c1", "c2"), pulse = "c1" to "c2", card = BoardCard("Courage", "Do the right thing, and work on tough problems.")),
                    Beat(26, card = BoardCard("Not done", "Back to the Product Backlog, not to the Play Store.")),
                ),
            ),
            // 27: "And when a team really lives all five?"
            Scene(
                27, "Trust", trust(),
                beats = listOf(
                    Beat(28, light = setOf("vals", "pil"), pulse = "vals" to "pil", card = BoardCard("The pillars come to life", "And that builds trust.")),
                    Beat(30, light = setOf("vals"), card = BoardCard("Can't be forced", "Learnt through events and artifacts; blame undermines them.")),
                    Beat(32, light = setOf("trust"), pulse = "pil" to "trust", card = BoardCard("The impostors", "Transparency is a pillar; trust is the result.")),
                ),
            ),
            // 33: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = all6, card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-values--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Our values", fiveValues(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Tonight: values. Scrum's, love.")),
                    Beat(2, show = setOf("pillars"), light = setOf("pillars"), card = BoardCard("Why values", "They hold up the three pillars.")),
                    Beat(4, card = BoardCard("Quiz", "Name the five Scrum values.")),
                    Beat(5, show = values5, light = values5, pulse = "open" to "pillars", card = BoardCard("Can five ordinary robots code", "Commitment, focus, openness, respect, courage.")),
                ),
            ),
            // 7: "Commitment first. I'm very committed, you know."
            Scene(
                7, "Commitment", commitment(),
                beats = listOf(
                    Beat(8, show = setOf("goal"), light = setOf("goal"), card = BoardCard("Committed to what?", "The goals, and each other. Not every ticket.")),
                    Beat(10, show = setOf("work", "po"), light = setOf("work", "po"), pulse = "po" to "work", card = BoardCard("Goal, not tickets", "Harder than planned? Swap the work with the Product Owner.")),
                    Beat(11, card = BoardCard("Quiz: a stuck teammate", "Keep polishing your screen, or help?")),
                    Beat(12, show = setOf("team"), light = setOf("team"), pulse = "team" to "goal", card = BoardCard("Help her", "Supporting each other is half of commitment.")),
                    Beat(14, card = BoardCard("Nobody wins alone", "The whole Scrum Team is accountable for a useful Increment.")),
                ),
            ),
            // 16: "Focus next..."
            Scene(
                16, "Quiz: focus", focus(),
                beats = listOf(
                    Beat(16, show = setOf("sales", "dev"), light = setOf("sales", "dev"), pulse = "sales" to "dev", card = BoardCard("Quiz: one small change", "A sales manager messages a developer mid-Sprint.")),
                    Beat(18, show = focusRest, light = setOf("po", "pb"), pulse = "sales" to "po", card = BoardCard("No kiss: focus", "To the Product Owner and the Product Backlog, not quietly in.")),
                ),
            ),
            // 19: "Fair. I'll keep my focus on you, then. Openness?"
            Scene(
                19, "Open, respectful, brave", behaviour(),
                beats = listOf(
                    Beat(20, show = setOf("o1", "o2"), light = setOf("o1", "o2"), pulse = "o1" to "o2", card = BoardCard("Openness", "Team and stakeholders, open about the work and its challenges.")),
                    Beat(22, show = setOf("r1", "r2"), light = setOf("r1", "r2"), pulse = "r1" to "r2", card = BoardCard("Respect", "Capable, independent people. Review, don't rewrite.")),
                    Beat(24, show = setOf("c1"), light = setOf("c1"), card = BoardCard("Quiz: release on Friday?", "The build isn't done. Which value says no?")),
                    Beat(25, show = setOf("c2"), light = setOf("c1", "c2"), pulse = "c1" to "c2", card = BoardCard("Courage", "Do the right thing, even with someone senior waiting.")),
                    Beat(28, card = BoardCard("Not done", "Back to the Product Backlog, never released.")),
                ),
            ),
            // 29: "Now a trap. Is transparency one of the five values?"
            Scene(
                29, "Quiz: the impostor", trust(),
                beats = listOf(
                    Beat(29, card = BoardCard("Quiz", "Is transparency one of the five values?")),
                    Beat(30, light = setOf("pil", "trust"), pulse = "pil" to "trust", card = BoardCard("No: a pillar", "Trust is the result. Collaboration and honesty aren't listed.")),
                    Beat(33, light = setOf("vals"), card = BoardCard("Can't be forced", "Learnt through events and artifacts; blame undermines them.")),
                    Beat(35, light = setOf("vals", "pil"), pulse = "vals" to "pil", card = BoardCard("When it works", "People say I'm stuck without fear; the board stays honest.")),
                ),
            ),
            // 36: "Then the last question, for the exam."
            Scene(
                36, "For the exam", exam(),
                beats = listOf(
                    Beat(37, show = all6, light = all6, card = takeaways),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
