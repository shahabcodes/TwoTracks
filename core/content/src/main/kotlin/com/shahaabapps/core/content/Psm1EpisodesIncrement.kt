package com.shahaabapps.core.content

/** The Increment and the Definition of Done: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesIncrement {
    private val classic = Podcast(
        "psm-increment",
        listOf(
            maya("Hi, and welcome back. Today: the Increment, and its commitment, the Definition of Done. Our team builds a running app for Android with a small backend. And on every team I've known, the most argued-about word is done."),
            leo("Oh, yes. It's done, it just needs testing. I've said that. More than once."),
            maya("[laugh] That's the exact sentence this topic retires. The Scrum Guide calls an Increment a concrete stepping stone toward the Product Goal. A real, working piece of the product."),
            leo("Stepping stone. In plain words?"),
            maya("Picture crossing a river on stones. Each Increment is one more stone you can actually stand on. It's additive: it adds to every Increment before it, and it's thoroughly verified, so they all work together. The new version holds everything the last one had, plus the new piece, and the old pieces still work."),
            leo("And it has to be usable?"),
            maya("To provide value, it must be usable. That means a runner could really use it, if you shipped it. Not a design on a slide. Not code on a branch nobody merged."),
            leo("One Increment per Sprint, then?"),
            maya("A favourite exam question. One or more. Several Increments may be created within a Sprint. The sum of them is presented at the Sprint Review, so people inspect real, working software."),
            leo("And can we ship before the Review? We push to the Play Store whenever something's ready."),
            maya("Yes. An Increment may be delivered to stakeholders before the Sprint ends. The Guide says the Sprint Review should never be a gate to releasing value. For example, a crash fix that's Done on Tuesday can reach the Play Store on Tuesday."),
            leo("Right. Now, who decides when something is Done?"),
            maya("The Definition of Done. It's the Increment's commitment: a formal description of the state of the Increment when it meets the quality measures the product needs. Picture a pilot's pre-flight checklist. The pilot doesn't feel ready. They tick every line."),
            leo("Give me ours."),
            maya("Say you're building the running app. Code reviewed by another developer. Automated tests pass. Works on the oldest Android version we support. No new crashes in the test build. Accessibility checked. Merged, with a signed release build ready. Every item meets all of it, or it isn't Done."),
            leo("And the moment it ticks every box?"),
            maya("The Guide's idea: the moment a Product Backlog item meets the Definition of Done, an Increment is born. Not at the Review. Not when somebody nods. At that moment."),
            leo("What about almost done? Ninety percent?"),
            maya("There's no such thing. Work that doesn't meet the Definition of Done isn't part of an Increment. It can't be released, or even presented at the Sprint Review. It goes back to the Product Backlog, for the Product Owner to consider again."),
            leo("That feels harsh. Why not show the progress?"),
            maya("Transparency. The Definition of Done gives everyone a shared understanding of what was completed. If done means one thing to you and another to marketing, they announce a feature that isn't ready. Under the picture, that's empiricism: you inspect what really exists, not what's promised."),
            leo("Where does the Definition of Done come from? Do we write it?"),
            maya("If the organisation has one as a standard, every Scrum Team must follow it as a minimum. If it doesn't, the Scrum Team creates one that fits the product. And the Developers are required to conform to it."),
            leo("A minimum. So we can add to it?"),
            maya("Yes. A team can be stricter than the company standard, never looser. Picture this: the company says every app passes a security scan. Your team adds: works with no signal, because it's a running app."),
            leo("And three teams building the same app?"),
            maya("They must agree on, and all follow, the same Definition of Done. Their Increments have to work together, so they need one meaning of done."),
            leo("Can the definition change over time?"),
            maya("It can. At the Sprint Retrospective the team plans ways to improve quality and effectiveness, and the Definition of Done is one of the things they can improve. Teams often add to it as they mature."),
            leo("Who's accountable for quality? The testers?"),
            maya("There are no separate testers in Scrum; everyone doing the work is a Developer. The Developers instil quality by sticking to the Definition of Done, and the whole Scrum Team is accountable for a valuable, useful Increment every Sprint. Quality doesn't drop during the Sprint."),
            leo("And the Product Owner accepting stories at the Review? We do that."),
            maya("[pause] Not in the Scrum Guide. The Definition of Done decides what is done. The Review is a working session about progress, not a sign-off gate. The Sprint Review episode covers what happens there instead."),
            leo("So what do I remember for the exam?"),
            maya("An item that doesn't meet the Definition of Done: not part of the Increment, back to the Product Backlog. Increments per Sprint: one or more. Release before the Review: allowed; the Review is not a gate. An organisation's standard: the minimum for every team. Several teams on one product: one shared Definition of Done."),
            leo("And anything to try today?"),
            maya("Write a Definition of Done for your own app, seven lines at most. Check the last feature you finished against it, honestly. Then release one small Increment in the middle of a Sprint, say to the Play Store's internal testing track."),
            leo("Done means done. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-increment",
        listOf(
            maya("Welcome to Case Files. Today's case: The Crash on Old Phones. A team builds a running app for Android with a small backend. A new route-sharing feature was shown at the Sprint Review, declared done, and released. Within an hour, it crashed on every phone more than four years old."),
            leo("[sigh] Detective Leo. First suspect: the tester. Somebody didn't test."),
            maya("There was no tester to blame. In Scrum, everyone who does the work is a Developer; there are no separate sub-teams. So, exhibit one: I asked four Developers what done meant. I got four answers."),
            leo("Four?"),
            maya("Coded. Coded and reviewed. Passing tests on my phone. And, my favourite, it's done, it just needs testing. Nobody had a Definition of Done."),
            leo("Definition of Done. In plain words?"),
            maya("It's the Increment's commitment: a formal description of the state of the Increment when it meets the quality measures the product needs. Picture a pilot's pre-flight checklist. Nobody takes off because they feel ready. They tick every line."),
            leo("And the Increment is?"),
            maya("A concrete stepping stone toward the Product Goal. A working piece of the product, additive to all the ones before it, and verified so they all work together. To provide value, it must be usable."),
            leo("Second suspect, then. The Product Owner accepted it at the Review. It's their fault."),
            maya("[pause] Wrong suspect, but a real clue. Exhibit two: the Review notes say, Product Owner accepts the story. The Scrum Guide has no such step. The Definition of Done decides what's done. The Review is a working session, not a sign-off gate."),
            leo("So what should have stopped the release?"),
            maya("A Definition of Done with one line: works on the oldest Android version we support. The moment an item meets every line, an Increment is born. Route sharing never met it. So it was never part of an Increment."),
            leo("And if it isn't part of the Increment?"),
            maya("It can't be released, or even presented at the Sprint Review. It goes back to the Product Backlog, for the Product Owner to consider again. No ninety percent done. Done, or not."),
            leo("Exhibit three?"),
            maya("The company's own rulebook. Turns out the organisation has a standard Definition of Done for every app: code review, automated tests, a security scan. The team had never read it."),
            leo("So they had a definition, and didn't know?"),
            maya("They did. If the organisation has one as a standard, every Scrum Team must follow it as a minimum. A team can add stricter lines for its product. It can never drop below the standard."),
            leo("Third suspect. They needed a hardening Sprint, to test everything before release."),
            maya("Tempting, and wrong. Scrum has no hardening Sprint, and no release Sprint. Every Sprint produces a Done, usable Increment. A hardening Sprint is a confession that the Definition of Done is too weak."),
            leo("Where else did it break?"),
            maya("Exhibit four. The backend team, a second Scrum Team on the same product, had its own looser definition. Their part of route sharing was done by their rules, broken by everyone else's."),
            leo("One product, two definitions."),
            maya("Exactly. Several Scrum Teams on one product must agree on, and follow, the same Definition of Done. Their Increments have to fit together."),
            leo("The manager wanted to skip the security scan this once, to hit the launch date. Allowed?"),
            maya("No. During the Sprint, quality does not decrease, and the organisation's standard is the minimum. If the date matters most, the team talks with the Product Owner about doing less. Never about doing it worse."),
            leo("Is usable the same as released, by the way?"),
            maya("Not quite. Usable means it could be released: working, tested, there. Whether and when to release it is a separate choice. The Definition of Done makes sure that choice is always open."),
            leo("Could they have released the fix fast, before the next Review?"),
            maya("Yes. An Increment may be delivered to stakeholders before the end of the Sprint. The Review is never a gate to releasing value. Their crash fix, once Done, went to the Play Store the same afternoon."),
            leo("And how do they stop it happening again?"),
            maya("At the Sprint Retrospective they improved the Definition of Done, which is one of the things a Retrospective can change. They added: tested on the oldest supported phone. The Developers instil quality by sticking to it; the whole Scrum Team is accountable for a valuable, useful Increment."),
            leo("Why does a shared meaning of done matter so much?"),
            maya("Transparency. It gives everyone a shared understanding of what was completed. Under the picture, it's empiricism: you inspect what truly exists, and decide from that. The Sprint Review episode shows what they inspect."),
            leo("So, the case summary for the exam?"),
            maya("An item that doesn't meet the Definition of Done: not part of the Increment, back to the Product Backlog. Increments per Sprint: one or more. Release before the Review: allowed; the Review is not a gate. An organisation's standard: the minimum for every team. Several teams on one product: one shared Definition of Done."),
            leo("Case closed. Anything to try today?"),
            maya("Write a Definition of Done for your own app, seven lines at most. Check the last feature you finished against it, honestly. Then release one small Increment in the middle of a Sprint, say to the Play Store's internal testing track."),
            leo("Done, or not done. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-increment",
        listOf(
            maya("Okay, did you hear what the running app team called done this week? Android app, small backend, and five people with five meanings of one little word."),
            leo("[laugh] I heard the phrase it just needs testing. Start me off. What's an Increment?"),
            maya("The Scrum Guide calls it a concrete stepping stone toward the Product Goal. Picture stones across a river: each Increment is one more you can stand on. It's additive, adding to every Increment before it; it's thoroughly verified so they all work together; and it must be usable."),
            leo("And done means?"),
            maya("Whatever the Definition of Done says. It's the Increment's commitment: a formal description of the quality the Increment must meet. Like a pre-flight checklist. Which brings us to Monday."),
            leo("Who was Monday?"),
            maya("Tom. He moved his offline maps card to Done, then said, it just needs testing. Tests were on the Definition of Done. So it wasn't Done. Work that doesn't meet it isn't part of an Increment."),
            leo("What happens to it, then?"),
            maya("If it isn't Done by the end of the Sprint, it can't be released or presented at the Sprint Review. It goes back to the Product Backlog, for the Product Owner to consider again."),
            leo("Poor Tom. Tuesday?"),
            maya("Dana, the Product Owner. A crash fix was Done on Tuesday. Reviewed, tested, merged, the lot. And Dana said: hold it, we'll release it after the Sprint Review."),
            leo("Sounds tidy."),
            maya("Sounds tidy, and it's a myth. An Increment may be delivered to stakeholders before the end of the Sprint. The Guide says the Sprint Review should never be a gate to releasing value. Runners crashed for eight extra days."),
            leo("[chuckle] Wednesday?"),
            maya("Marcus and the backend team. The company has a standard Definition of Done: code review, automated tests, a security scan. Marcus wrote a shorter one for his team. No security scan. Faster, he said."),
            leo("Can't a team choose its own?"),
            maya("Only above the line. If the organisation has a standard, every Scrum Team must follow it as a minimum. Teams can add stricter checks for their product, never drop any. If there's no standard, the Scrum Team creates one."),
            leo("Marcus said it was launch week. Doesn't that count?"),
            maya("Still no. The Guide says quality does not decrease during the Sprint. If time is short, you negotiate scope with the Product Owner. Do less, never worse."),
            leo("Thursday? Someone good, please."),
            maya("Priya. The Android, iPhone and backend teams all build one product, and each had its own done. Priya got them in one room and they agreed a single Definition of Done. That's the Guide: several teams on one product share the same one."),
            leo("Why does that matter so much?"),
            maya("Because their Increments have to work together. And because of transparency: one shared understanding of what was completed. Under the picture, it's empiricism. You inspect what truly exists, then decide."),
            leo("And Friday? There's always a Friday."),
            maya("The intern. At the Sprint Review, the intern demoed a half-built feature and called it ninety percent done. Then asked the Product Owner to accept the story."),
            leo("Two crimes."),
            maya("Two. Undone work isn't presented at the Review. And there's no accepting stories in the Scrum Guide. The Definition of Done decides what's done; the Review is a working session about progress. The Sprint Review episode has the rest."),
            leo("How many Increments did they make, in the end?"),
            maya("Three, in one Sprint. The crash fix, a faster start screen, and route sharing. That's allowed. Several Increments may be created in a Sprint, and their sum is presented at the Review."),
            leo("Did they release all three?"),
            maya("Two went to the Play Store mid-Sprint. Route sharing waited for a marketing launch. That's fine: usable means it could be released, and whether and when is a separate choice. Done keeps the choice open."),
            leo("And who checks quality? Did they hire testers?"),
            maya("Scrum has no separate testers. Everyone doing the work is a Developer. The Developers instil quality by sticking to the Definition of Done, and the whole Scrum Team is accountable for a valuable, useful Increment every Sprint."),
            leo("Did the definition ever change?"),
            maya("At the Retrospective. They added a line: tested on the oldest supported phone. Improving the Definition of Done is one of the things a Sprint Retrospective can do."),
            leo("And where does the definition live? In a drawer?"),
            maya("Somewhere everyone can see it. It's there for transparency: the team, the Product Owner and the stakeholders all share one meaning of done."),
            leo("So, the gossip I should remember for the exam?"),
            maya("An item that doesn't meet the Definition of Done: not part of the Increment, back to the Product Backlog. Increments per Sprint: one or more. Release before the Review: allowed; the Review is not a gate. An organisation's standard: the minimum for every team. Several teams on one product: one shared Definition of Done."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Write a Definition of Done for your own app, seven lines at most. Check the last feature you finished against it, honestly. Then release one small Increment in the middle of a Sprint, say to the Play Store's internal testing track."),
            leo("It just needs testing, Tom. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-increment",
        listOf(
            nova("Hey, you. It's late. The build is running, and you're waiting for a green tick. [pause] Tonight we talk about the word on everyone's lips. Done."),
            ivy("Mm. Done. My favourite word, and the one I trust least."),
            nova("[chuckle] Then let's earn it. In Scrum, done is held by the Definition of Done, and what it produces is the Increment. The Scrum Guide calls an Increment a concrete stepping stone toward the Product Goal."),
            ivy("A stepping stone. Walk me across."),
            nova("Picture a river at night, and stones across it. Each Increment is one more stone you can stand on. It's additive, adding to every one before it, and it's thoroughly verified, [pause] so they all hold together. And it must be usable. A runner could really use it, if you shipped it."),
            ivy("Only one stone per Sprint?"),
            nova("One or more. Several Increments may be created within a Sprint. Their sum is presented at the Sprint Review."),
            ivy("And if one is ready on a Tuesday night?"),
            nova("Then it can go. An Increment may be delivered to stakeholders before the Sprint ends. The Guide says the Sprint Review should never be a gate to releasing value. A crash fix, Done on Tuesday, can reach the Play Store on Tuesday."),
            ivy("Is usable the same as released?"),
            nova("Not quite. Usable means it could be released, tonight if you wanted. Whether and when is a separate choice. [pause] Done keeps that choice open."),
            ivy("Tell me about the Definition of Done. Slowly."),
            nova("It's the Increment's commitment. A formal description of the state of the Increment when it meets the quality the product needs. [pause] Like a pilot's checklist before a night flight. Nobody takes off because they feel ready. They tick every line."),
            ivy("Read me ours."),
            nova("For the running app: reviewed by another developer. Automated tests pass. Works on the oldest Android version we support. No new crashes in the test build. Accessibility checked. Merged, with a signed release build ready. All of it, or it isn't Done."),
            ivy("And the moment every line is ticked?"),
            nova("An Increment is born. That's the Guide's own idea: the moment a Product Backlog item meets the Definition of Done. Not at the Review. Not when someone nods. Right then."),
            ivy("And if it's almost there? Ninety percent, at midnight?"),
            nova("[pause] Then it isn't part of an Increment. It can't be released, or even shown at the Sprint Review. It goes back to the Product Backlog, for the Product Owner to consider again. There's no almost."),
            ivy("Why so strict, so late?"),
            nova("Transparency. The Definition of Done gives everyone a shared understanding of what was completed. Under the picture, it's empiricism: you inspect what truly exists, and decide from that. Not from a promise made at midnight."),
            ivy("Who writes the checklist?"),
            nova("If the organisation has a Definition of Done as a standard, every Scrum Team follows it as a minimum. If not, the Scrum Team creates one that fits the product. The Developers must conform to it."),
            ivy("Can I make mine stricter?"),
            nova("Stricter, yes. Looser, never. The company says every app passes a security scan; your team adds, works with no signal. Because runners run where there's no signal."),
            ivy("And when the deadline whispers, skip a line, just this once?"),
            nova("[pause] Don't listen. During the Sprint, quality does not decrease. If time runs short, you talk with the Product Owner about doing less. Never about doing it worse."),
            ivy("And three teams on one app?"),
            nova("One shared Definition of Done. They agree on it together, and all follow it, so their Increments fit."),
            ivy("Where do they keep the checklist?"),
            nova("Where everyone can see it. The team, the Product Owner, the stakeholders. One meaning of done, in the open. That's transparency."),
            ivy("Who keeps the quality, then? A test team in the dark?"),
            nova("No sub-teams in Scrum. Everyone doing the work is a Developer, and the Developers instil quality by sticking to the Definition of Done. The whole Scrum Team is accountable for a valuable, useful Increment every Sprint. At the Retrospective, they can improve the definition itself."),
            ivy("And the Product Owner, accepting stories at the Review?"),
            nova("Not in the Guide. The definition decides what's done. The Review is a working session, not a gate. The Sprint Review episode is another late night."),
            ivy("[sigh] Mm. Before I sleep. What do I remember for the exam?"),
            nova("An item that doesn't meet the Definition of Done: not part of the Increment, back to the Product Backlog. Increments per Sprint: one or more. Release before the Review: allowed; the Review is not a gate. An organisation's standard: the minimum for every team. Several teams on one product: one shared Definition of Done."),
            ivy("And something to try today, before I close my eyes?"),
            nova("Write a Definition of Done for your own app, seven lines at most. [pause] Check the last feature you finished against it, honestly. Then release one small Increment in the middle of a Sprint, say to the Play Store's internal testing track."),
            ivy("Done, really done. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-increment",
        listOf(
            nova("Tonight is date night, darling, and I want to know: when are you done?"),
            rafe("Done with dinner? Done with you? Never."),
            nova("[laugh] Done with work. In Scrum, done has a definition. Literally. The Definition of Done. And what it gives you is an Increment."),
            rafe("Increment. Tell me what it is, so I can make one."),
            nova("The Scrum Guide calls it a concrete stepping stone toward the Product Goal. Picture stones across a river. Each one is additive, adding to all the ones before; it's thoroughly verified so they all hold together; and it must be usable. For our running app, a version a runner could really use."),
            rafe("Quiz me. I'm ready to be verified."),
            nova("Exam style. How many Increments can a team create in one Sprint?"),
            rafe("One or more. Several Increments may be created within a Sprint, and their sum is presented at the Sprint Review."),
            nova("Right answer. Never just the one. [kiss]"),
            rafe("My turn. The Increment's commitment. What is it, and what does it describe?"),
            nova("The Definition of Done. A formal description of the state of the Increment when it meets the quality measures the product needs. Like a pilot's pre-flight checklist: you tick every line, you don't just feel ready."),
            rafe("Right answer. You always tick every box. [kiss]"),
            nova("Ours: reviewed by another developer, automated tests pass, works on the oldest Android version we support, no new crashes, accessibility checked, merged, a signed release build ready. And the moment an item meets all of it, an Increment is born."),
            rafe("Then here's one for you. Last day of the Sprint, an item is ninety percent done. We show it at the Review, and call it nearly there."),
            nova("No kiss for that one, love. Work that doesn't meet the Definition of Done isn't part of an Increment. It can't be released, or even presented at the Review. It goes back to the Product Backlog, for the Product Owner to consider again."),
            rafe("Strict. I like it. Why so strict?"),
            nova("Transparency. A shared understanding of what was completed. Under the picture, it's empiricism: inspect what truly exists, then decide. My turn. A crash fix is Done on Tuesday. Must it wait for the Sprint Review to be released?"),
            rafe("No. An Increment may be delivered to stakeholders before the end of the Sprint. The Sprint Review is never a gate to releasing value."),
            nova("Right answer. Why wait, when it's ready? [kiss]"),
            rafe("And is usable the same as released?"),
            nova("Not quite. Usable means it could be released. Whether and when to release is a separate choice; done keeps that choice open."),
            rafe("Then let me ask. Who writes the Definition of Done?"),
            nova("If the organisation has one as a standard, every Scrum Team follows it as a minimum. If not, the Scrum Team creates one for the product. And a team may be stricter than the standard, never looser."),
            rafe("Stricter than the standard. That's my type."),
            nova("[chuckle] Then exam style. Three Scrum Teams work on one product. How many Definitions of Done?"),
            rafe("Three, one each. Every team is different."),
            nova("No kiss. One. Several teams on one product must agree on, and all follow, the same Definition of Done, so their Increments fit together."),
            rafe("Then quiz me again. I can take it."),
            nova("Exam style. Launch week. A manager asks the Developers to skip one line of the Definition of Done, just this once, to make the date. What do you say?"),
            rafe("No. Quality does not decrease during the Sprint, and an organisation's standard is the minimum. If time is short, we negotiate scope with the Product Owner. Do less, never worse."),
            nova("Right answer. Never cutting corners on me. [kiss]"),
            rafe("One definition, like one of you. And who checks quality? Do we hire testers?"),
            nova("No sub-teams in Scrum; everyone doing the work is a Developer. The Developers instil quality by sticking to the Definition of Done. And the whole Scrum Team is accountable for a valuable, useful Increment every Sprint."),
            rafe("Last one from me. Does the Product Owner accept stories at the Review, as a gate?"),
            nova("Not in the Scrum Guide. The Definition of Done decides what's done; the Review is a working session. The Sprint Review episode covers it. And at the Retrospective, the team can improve the Definition of Done itself."),
            rafe("Right answer. Always improving. [kiss]"),
            nova("Last question, for the exam. What do we remember?"),
            rafe("An item that doesn't meet the Definition of Done: not part of the Increment, back to the Product Backlog. Increments per Sprint: one or more. Release before the Review: allowed; the Review is not a gate. An organisation's standard: the minimum for every team. Several teams on one product: one shared Definition of Done."),
            nova("Five for five, and six kisses tonight, darling. That earns a long one. [kiss] And something to try today?"),
            rafe("Write a Definition of Done for your own app, seven lines at most. Check the last feature you finished against it, honestly. Then release one small Increment in the middle of a Sprint, say to the Play Store's internal testing track."),
            nova("Done, darling. Really done. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun stones() = DiagramSpec(
        "Each Increment is a stepping stone toward the Product Goal: additive, verified, and usable.",
        nodes = listOf(
            DNode("goal", "Product Goal", 0.5f, 0.13f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 1),
            DNode("i1", "Increment 1", 0.17f, 0.5f, w = 0.3f, h = 0.2f, tone = 1),
            DNode("i2", "Increment 2", 0.5f, 0.5f, w = 0.3f, h = 0.2f, tone = 1),
            DNode("i3", "Increment 3", 0.83f, 0.5f, w = 0.3f, h = 0.2f, tone = 1),
            DNode("note", "Additive, verified, usable", 0.5f, 0.86f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("i1", "i2", "adds to"),
            DEdge("i2", "i3", "adds to"),
            DEdge("i3", "goal", "toward"),
        ),
        aspect = 0.8f,
    )

    private fun dod() = DiagramSpec(
        "An item that meets the Definition of Done becomes an Increment, releasable at once; one that doesn't goes back to the Product Backlog.",
        nodes = listOf(
            DNode("item", "Product Backlog item", 0.25f, 0.15f, w = 0.44f, h = 0.2f),
            DNode("dod", "Definition of Done", 0.75f, 0.15f, w = 0.44f, h = 0.2f, tone = 1, sub = "every line ticked"),
            DNode("inc", "Increment", 0.25f, 0.52f, w = 0.44f, h = 0.2f, tone = 1, sub = "born right then"),
            DNode("back", "Product Backlog", 0.75f, 0.52f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2, sub = "if not Done"),
            DNode("review", "Sprint Review", 0.25f, 0.86f, w = 0.44f, h = 0.2f, sub = "the sum, inspected"),
            DNode("store", "Play Store", 0.75f, 0.86f, w = 0.44f, h = 0.2f, sub = "any time it's Done"),
        ),
        edges = listOf(
            DEdge("item", "dod", "checked"),
            DEdge("dod", "inc", "meets it"),
            DEdge("dod", "back", "doesn't"),
            DEdge("inc", "review"),
            DEdge("inc", "store", "release"),
        ),
        aspect = 0.8f,
    )

    private fun standard() = DiagramSpec(
        "An organisation's Definition of Done is the minimum; a team may add stricter lines, and teams on one product share one.",
        nodes = listOf(
            DNode("org", "Organisation standard", 0.5f, 0.14f, w = 0.8f, h = 0.2f, tone = 2, sub = "the minimum"),
            DNode("team", "One shared definition", 0.5f, 0.47f, w = 0.7f, h = 0.2f, tone = 1, sub = "stricter, never looser"),
            DNode("t1", "Android team", 0.17f, 0.82f, w = 0.3f, h = 0.2f),
            DNode("t2", "iPhone team", 0.5f, 0.82f, w = 0.3f, h = 0.2f),
            DNode("t3", "Backend team", 0.83f, 0.82f, w = 0.3f, h = 0.2f),
        ),
        edges = listOf(
            DEdge("org", "team", "add to"),
            DEdge("team", "t1"),
            DEdge("team", "t2"),
            DEdge("team", "t3"),
        ),
        aspect = 0.8f,
    )

    private fun quality() = DiagramSpec(
        "The Developers instil quality through the Definition of Done; the Scrum Team is accountable, and the Retrospective improves it.",
        nodes = listOf(
            DNode("devs", "Developers", 0.25f, 0.18f, w = 0.42f, h = 0.22f, tone = 1, sub = "instil quality"),
            DNode("team", "Scrum Team", 0.75f, 0.18f, w = 0.42f, h = 0.22f, sub = "accountable"),
            DNode("retro", "Retrospective", 0.25f, 0.62f, w = 0.42f, h = 0.22f, sub = "improves the definition"),
            DNode("noq", "No testers sub-team", 0.75f, 0.62f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("retro", "devs", "improve"),
            DEdge("devs", "team"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Increment questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("notdone", "Not Done at the end", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "back to the backlog"),
            DNode("many", "Increments per Sprint", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "one or more"),
            DNode("early", "Release before Review", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "allowed, no gate"),
            DNode("org", "Organisation standard", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "minimum for all"),
            DNode("teams", "Several teams", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "one shared definition"),
            DNode("accept", "Accepting stories", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "not in the Guide"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("notdone", "many", "early", "org", "teams", "accept")
    private val incs = setOf("i1", "i2", "i3")
    private val trio = setOf("t1", "t2", "t3")
    private val takeaways = BoardCard("Listen for the phrase", "Not Done: back to the backlog. One or more a Sprint. Release early. One shared definition.")
    private val today = BoardCard("Today", "Write your Definition of Done, test a feature against it, ship early.")
    private val increment = BoardCard("Increment", "A concrete stepping stone toward the Product Goal.")
    private val additive = BoardCard("Additive and verified", "Adds to every earlier Increment; all of them work together.")
    private val usableCard = BoardCard("Usable", "A runner could really use it. Not a slide, not an unmerged branch.")
    private val oneOrMore = BoardCard("One or more", "Several Increments a Sprint; their sum is shown at the Review.")
    private val releaseEarly = BoardCard("Release early", "Delivered before the Sprint ends: the Review is never a gate.")
    private val dodCard = BoardCard("Definition of Done", "The Increment's commitment: a formal description of quality.")
    private val ours = BoardCard("Ours", "Reviewed, tested, oldest Android, no new crashes, merged, signed.")
    private val born = BoardCard("An Increment is born", "The moment an item meets every line. Not at the Review.")
    private val notDone = BoardCard("Not Done", "Not released, not presented. Back to the Product Backlog.")
    private val transparency = BoardCard("Transparency", "One shared meaning of done. Inspect what truly exists.")
    private val orgCard = BoardCard("Organisation standard", "Every Scrum Team follows it as a minimum; else the team writes one.")
    private val stricter = BoardCard("Stricter, never looser", "Company: a security scan. The team adds: works with no signal.")
    private val severalTeams = BoardCard("Several teams", "One product, one shared Definition of Done.")
    private val doLess = BoardCard("Do less, never worse", "Quality does not decrease during the Sprint. Negotiate scope.")
    private val usableReleased = BoardCard("Usable, not yet released", "It could be released. Whether and when is a separate choice.")
    private val whoKeeps = BoardCard("Who keeps quality", "The Developers, by the definition. No testers' sub-team.")
    private val retro = BoardCard("Retrospective", "The team can improve the Definition of Done.")
    private val accepting = BoardCard("Accepting stories?", "Not in the Guide. The definition decides; the Review is no gate.")

    private fun classicBoard() = PodcastBoard(
        "psm-increment",
        listOf(
            // 0: "Hi, and welcome back. Today: the Increment..."
            Scene(
                0, "Stepping stones", stones(),
                beats = listOf(
                    Beat(0, card = BoardCard("Done", "The most argued-about word on any team.")),
                    Beat(2, show = setOf("i1"), light = setOf("i1"), card = increment),
                    Beat(4, show = setOf("i2", "i3"), light = incs, pulse = "i1" to "i2", card = additive),
                    Beat(6, show = setOf("note"), light = setOf("note"), card = usableCard),
                    Beat(8, light = incs, pulse = "i3" to "goal", card = oneOrMore),
                ),
            ),
            // 9: "And can we ship before the Review?"
            Scene(
                9, "The Definition of Done", dod(),
                beats = listOf(
                    Beat(10, show = setOf("inc", "store"), light = setOf("store"), pulse = "inc" to "store", card = releaseEarly),
                    Beat(12, show = setOf("item", "dod"), light = setOf("dod"), pulse = "item" to "dod", card = dodCard),
                    Beat(14, card = ours),
                    Beat(16, light = setOf("inc"), pulse = "dod" to "inc", card = born),
                    Beat(18, show = setOf("back"), light = setOf("back"), pulse = "dod" to "back", card = notDone),
                    Beat(20, card = transparency),
                ),
            ),
            // 21: "Where does the Definition of Done come from?"
            Scene(
                21, "Where it comes from", standard(),
                beats = listOf(
                    Beat(22, show = setOf("org"), light = setOf("org"), card = orgCard),
                    Beat(24, show = setOf("team"), light = setOf("team"), pulse = "org" to "team", card = stricter),
                    Beat(26, show = trio, light = trio, pulse = "team" to "t1", card = severalTeams),
                ),
            ),
            // 27: "Can the definition change over time?"
            Scene(
                27, "Keeping quality", quality(),
                beats = listOf(
                    Beat(28, light = setOf("retro"), pulse = "retro" to "devs", card = retro),
                    Beat(30, light = setOf("devs", "team", "noq"), card = whoKeeps),
                    Beat(32, card = accepting),
                ),
            ),
            // 33: "So what do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("notdone", "many", "early", "org", "teams"), card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-increment--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Crash on Old Phones..."
            Scene(
                0, "The Crash on Old Phones", dod(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Shown, called done, released, and crashing on older phones.")),
                    Beat(2, card = BoardCard("No tester to blame", "Everyone doing the work is a Developer. No sub-teams.")),
                    Beat(4, card = BoardCard("Four meanings of done", "Coded. Reviewed. Tests on my phone. Just needs testing.")),
                    Beat(6, show = setOf("item", "dod"), light = setOf("dod"), pulse = "item" to "dod", card = dodCard),
                ),
            ),
            // 7: "And the Increment is?"
            Scene(
                7, "The Increment", stones(),
                beats = listOf(
                    Beat(8, show = incs + "note", light = incs, pulse = "i1" to "i2", card = BoardCard("Increment", "A stepping stone: additive, verified, and usable.")),
                ),
            ),
            // 9: "Second suspect, then. The Product Owner accepted it at the Review."
            Scene(
                9, "Exhibit two: the Review", dod(),
                beats = listOf(
                    Beat(10, show = setOf("item", "dod", "review"), light = setOf("review"), card = accepting),
                    Beat(12, show = setOf("inc"), light = setOf("inc"), pulse = "dod" to "inc", card = born),
                    Beat(14, show = setOf("back"), light = setOf("back"), pulse = "dod" to "back", card = notDone),
                ),
            ),
            // 15: "Exhibit three?"
            Scene(
                15, "Exhibit three: the rulebook", standard(),
                beats = listOf(
                    Beat(16, show = setOf("org"), light = setOf("org"), card = BoardCard("The company's rulebook", "Review, automated tests, a security scan. Nobody had read it.")),
                    Beat(18, show = setOf("team"), light = setOf("team"), pulse = "org" to "team", card = stricter),
                    Beat(20, card = BoardCard("A hardening Sprint?", "No such thing. Every Sprint makes a Done, usable Increment.")),
                    Beat(22, show = trio, light = setOf("t3"), pulse = "team" to "t3", card = BoardCard("Exhibit four", "The backend team's looser definition. One product, two dones.")),
                    Beat(24, light = trio, card = severalTeams),
                    Beat(26, light = setOf("org"), card = doLess),
                ),
            ),
            // 27: "Is usable the same as released, by the way?"
            Scene(
                27, "Usable and released", dod(),
                beats = listOf(
                    Beat(28, light = setOf("inc"), card = usableReleased),
                    Beat(30, light = setOf("store"), pulse = "inc" to "store", card = releaseEarly),
                ),
            ),
            // 31: "And how do they stop it happening again?"
            Scene(
                31, "Never again", quality(),
                beats = listOf(
                    Beat(32, light = setOf("retro"), pulse = "retro" to "devs", card = BoardCard("Retrospective", "They added: tested on the oldest supported phone.")),
                    Beat(34, light = setOf("devs", "team"), card = transparency),
                ),
            ),
            // 35: "So, the case summary for the exam?"
            Scene(
                35, "Case closed", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("notdone", "many", "early", "org", "teams"), card = takeaways),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "psm-increment--gossip",
        listOf(
            // 0: "Okay, did you hear what the running app team called done this week?"
            Scene(
                0, "Five meanings of done", stones(),
                beats = listOf(
                    Beat(0, card = BoardCard("The running app team", "Five people, five meanings of one little word.")),
                    Beat(2, show = incs + "note", light = incs, pulse = "i1" to "i2", card = additive),
                ),
            ),
            // 3: "And done means?"
            Scene(
                3, "Monday and Tuesday", dod(),
                beats = listOf(
                    Beat(4, show = setOf("item", "dod"), light = setOf("dod"), pulse = "item" to "dod", card = dodCard),
                    Beat(6, card = BoardCard("Monday: Tom", "Moved to Done, then: it just needs testing. Not Done.")),
                    Beat(8, show = setOf("back"), light = setOf("back"), pulse = "dod" to "back", card = notDone),
                    Beat(10, show = setOf("inc"), light = setOf("inc"), pulse = "dod" to "inc", card = BoardCard("Tuesday: Dana", "A Done crash fix, held back until after the Review.")),
                    Beat(12, show = setOf("store"), light = setOf("store"), pulse = "inc" to "store", card = releaseEarly),
                ),
            ),
            // 13: "Wednesday?"
            Scene(
                13, "Wednesday and Thursday", standard(),
                beats = listOf(
                    Beat(14, show = setOf("org"), light = setOf("org"), card = BoardCard("Wednesday: Marcus", "A shorter definition for the backend. No security scan.")),
                    Beat(16, show = setOf("team"), light = setOf("team"), pulse = "org" to "team", card = BoardCard("Above the line", "The standard is the minimum: add stricter checks, never fewer.")),
                    Beat(18, card = doLess),
                    Beat(20, show = trio, light = trio, pulse = "team" to "t1", card = BoardCard("Thursday: Priya", "Three teams, one product: one shared Definition of Done.")),
                    Beat(22, card = transparency),
                ),
            ),
            // 23: "And Friday? There's always a Friday."
            Scene(
                23, "Friday: the intern", dod(),
                beats = listOf(
                    Beat(24, light = setOf("review"), card = BoardCard("Friday: the intern", "Ninety percent done, demoed, then: please accept the story.")),
                    Beat(26, light = setOf("back"), card = BoardCard("Two crimes", "Undone work isn't presented. Accepting stories isn't in the Guide.")),
                    Beat(28, light = setOf("inc"), pulse = "inc" to "review", card = oneOrMore),
                    Beat(30, light = setOf("store"), pulse = "inc" to "store", card = usableReleased),
                ),
            ),
            // 31: "And who checks quality? Did they hire testers?"
            Scene(
                31, "Quality", quality(),
                beats = listOf(
                    Beat(32, light = setOf("devs", "team", "noq"), card = whoKeeps),
                    Beat(34, light = setOf("retro"), pulse = "retro" to "devs", card = retro),
                    Beat(36, card = BoardCard("In the open", "Everyone shares one meaning of done: that's transparency.")),
                ),
            ),
            // 37: "So, the gossip I should remember for the exam?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("notdone", "many", "early", "org", "teams"), card = takeaways),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "psm-increment--late-night",
        listOf(
            // 0: "Hey, you. It's late. The build is running..."
            Scene(
                0, "Done", stones(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The word on everyone's lips: done.")),
                    Beat(2, show = setOf("i1"), light = setOf("i1"), card = increment),
                    Beat(4, show = setOf("i2", "i3", "note"), light = incs, pulse = "i1" to "i2", card = additive),
                    Beat(6, light = incs, pulse = "i3" to "goal", card = oneOrMore),
                ),
            ),
            // 7: "And if one is ready on a Tuesday night?"
            Scene(
                7, "Ready on a Tuesday", dod(),
                beats = listOf(
                    Beat(8, show = setOf("inc", "store"), light = setOf("store"), pulse = "inc" to "store", card = releaseEarly),
                    Beat(10, light = setOf("inc"), card = usableReleased),
                    Beat(12, show = setOf("item", "dod"), light = setOf("dod"), pulse = "item" to "dod", card = dodCard),
                    Beat(14, card = ours),
                    Beat(16, light = setOf("inc"), pulse = "dod" to "inc", card = born),
                    Beat(18, show = setOf("back"), light = setOf("back"), pulse = "dod" to "back", card = notDone),
                    Beat(20, card = transparency),
                ),
            ),
            // 21: "Who writes the checklist?"
            Scene(
                21, "Who writes the checklist", standard(),
                beats = listOf(
                    Beat(22, show = setOf("org"), light = setOf("org"), card = orgCard),
                    Beat(24, show = setOf("team"), light = setOf("team"), pulse = "org" to "team", card = stricter),
                    Beat(26, card = doLess),
                    Beat(28, show = trio, light = trio, pulse = "team" to "t1", card = severalTeams),
                    Beat(30, card = BoardCard("In the open", "The team, the Product Owner, the stakeholders: one meaning of done.")),
                ),
            ),
            // 31: "Who keeps the quality, then?"
            Scene(
                31, "Who keeps the quality", quality(),
                beats = listOf(
                    Beat(32, light = setOf("devs", "team", "noq"), card = whoKeeps),
                    Beat(34, light = setOf("retro"), pulse = "retro" to "devs", card = accepting),
                ),
            ),
            // 35: "Before I sleep. What do I remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("notdone", "many", "early", "org", "teams"), card = takeaways),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-increment--couples",
        listOf(
            // 0: "Tonight is date night, darling, and I want to know: when are you done?"
            Scene(
                0, "When are you done?", stones(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "When are you done?")),
                    Beat(4, show = incs + "note", light = incs, pulse = "i1" to "i2", card = increment),
                    Beat(6, card = BoardCard("Quiz: how many?", "Increments in one Sprint?")),
                    Beat(7, pulse = "i3" to "goal", card = oneOrMore),
                ),
            ),
            // 9: "My turn. The Increment's commitment."
            Scene(
                9, "Quiz: the commitment", dod(),
                beats = listOf(
                    Beat(9, card = BoardCard("Quiz: the commitment", "The Increment's, and what does it describe?")),
                    Beat(10, show = setOf("item", "dod"), light = setOf("dod"), pulse = "item" to "dod", card = dodCard),
                    Beat(12, show = setOf("inc"), light = setOf("inc"), pulse = "dod" to "inc", card = born),
                    Beat(13, card = BoardCard("Quiz: ninety percent", "Show it at the Review as nearly there?")),
                    Beat(14, show = setOf("back"), light = setOf("back"), pulse = "dod" to "back", card = notDone),
                    Beat(16, show = setOf("store"), light = setOf("store"), card = BoardCard("Quiz: wait for the Review?", "A crash fix, Done on Tuesday.")),
                    Beat(17, pulse = "inc" to "store", card = releaseEarly),
                    Beat(20, light = setOf("inc"), card = usableReleased),
                ),
            ),
            // 21: "Then let me ask. Who writes the Definition of Done?"
            Scene(
                21, "Who writes it", standard(),
                beats = listOf(
                    Beat(22, show = setOf("org", "team"), light = setOf("org", "team"), pulse = "org" to "team", card = orgCard),
                    Beat(24, card = BoardCard("Quiz: three teams", "One product. How many Definitions of Done?")),
                    Beat(26, show = trio, light = trio, pulse = "team" to "t1", card = BoardCard("No kiss: just one", "Several teams on one product share one definition.")),
                    Beat(28, light = setOf("org"), card = BoardCard("Quiz: skip a line?", "Launch week, just this once?")),
                    Beat(29, card = doLess),
                ),
            ),
            // 31: "One definition, like one of you. And who checks quality?"
            Scene(
                31, "Who checks quality", quality(),
                beats = listOf(
                    Beat(32, light = setOf("devs", "team", "noq"), card = whoKeeps),
                    Beat(33, card = BoardCard("Quiz: accepting stories?", "Does the Product Owner sign off at the Review?")),
                    Beat(34, light = setOf("retro"), pulse = "retro" to "devs", card = BoardCard("Not a gate", "The definition decides. A Retrospective can improve it.")),
                ),
            ),
            // 36: "Last question, for the exam. What do we remember?"
            Scene(
                36, "For the exam", exam(),
                beats = listOf(
                    Beat(37, show = all6, light = setOf("notdone", "many", "early", "org", "teams"), card = takeaways),
                    Beat(39, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
