package com.shahaabapps.core.content

/** The Scrum Master: a true leader who serves: its five PSM I episodes and their boards (Psm1Podcasts). */
internal object Psm1EpisodesScrumMaster {
    private val classic = Podcast(
        "psm-scrum-master",
        listOf(
            maya("Hi, and welcome back. Today: the Scrum Master. Picture a small team building an Android running app: five Developers who build it, one Product Owner who decides what it needs most, and one Scrum Master. Today we find out what that last person actually does all day."),
            leo("Honestly? I always assumed the Scrum Master is a project manager with a new name. Hands out tickets, chases deadlines."),
            maya("That's the most common guess, and the exam is built to catch it. [pause] The Scrum Guide, the short free document that defines Scrum, says the Scrum Master is accountable for establishing Scrum as the Guide defines it, and for the Scrum Team's effectiveness."),
            leo("Accountable. What does that mean here, in plain words?"),
            maya("It means answerable for a result. Since 2020 the Guide talks about accountabilities instead of roles. So the Scrum Master answers for two things: the team really doing Scrum, and the team getting better at it. Not for the features in the app."),
            leo("So who hands out the tickets?"),
            maya("Nobody. The team is self-managing, which means the team itself decides who does what, when and how. The Developers pull in the work themselves. A Scrum Master who assigns tasks is quietly taking that away from them."),
            leo("Then what's left for the Scrum Master? It sounds like a job with nothing in it."),
            maya("[chuckle] Quite a lot. The Guide calls the Scrum Master a true leader who serves the Scrum Team and the larger organisation. Think of a good football coach: never on the pitch scoring, but the reason the team plays better every season."),
            leo("Serves. Like a waiter?"),
            maya("Serves as in helps them grow, not fetches the coffee. The Guide lists three groups the Scrum Master serves: the Scrum Team, the Product Owner, and the organisation. Let's take the team first."),
            leo("Go on."),
            maya("Coaching the team members in self-management and cross-functionality. Cross-functional means the team holds every skill it needs, Android, backend, testing, design, so it never waits on another department. And helping them focus on high-value Increments that meet the Definition of Done."),
            leo("Two more words. Increment, and Definition of Done."),
            maya("An Increment is a usable piece of the product, a real step forward; for us, a build of the app that works. The Definition of Done is the team's quality checklist: for example, code reviewed, tests passing, runs on the oldest Android version we support. Work that misses it isn't part of the Increment."),
            leo("Got it. What else for the team?"),
            maya("Causing the removal of impediments. An impediment is anything blocking the team's progress that the team can't easily clear itself. Say the Developers wait three days every Sprint for another department to sign off the release build. That's an impediment."),
            leo("And the Scrum Master fixes it personally?"),
            maya("Listen to the wording: causing the removal. Sometimes they remove it themselves; often they coach the team to remove it, or go to whoever in the company can. And a small problem the Developers can solve, like a flaky test, is theirs to solve. Self-managing, remember."),
            leo("Fair. And the last one for the team?"),
            maya("Ensuring that all Scrum events take place and are positive, productive and kept within the timebox. A timebox is a maximum length, like a parking meter: the Daily Scrum gets fifteen minutes, Sprint Planning up to eight hours for a one-month Sprint."),
            leo("So the Scrum Master runs every meeting."),
            maya("Ensures, not runs. Take the Daily Scrum: it's the Developers' event. The Scrum Master makes sure it happens and teaches them to keep it inside fifteen minutes, but doesn't have to attend, and never turns it into a status report to themselves."),
            leo("Okay. Now the Product Owner. How does the Scrum Master serve them?"),
            maya("By helping find techniques for defining the Product Goal, the long-term objective for the product, and for managing the Product Backlog, the ordered list of everything the product might need. By helping the team understand why clear, concise backlog items matter. And by helping establish empirical product planning."),
            leo("Empirical product planning. Unpack that, please."),
            maya("Empirical means deciding from what you've actually seen, not from a guess made months ago. So instead of a fixed plan for the year, you ship, watch what users do, and replan. One more: facilitating stakeholder collaboration, as requested or needed. Stakeholders are people outside the team with a stake in the product, like marketing."),
            leo("Facilitating. Plain words?"),
            maya("Making a conversation easy and useful without taking a side or making the decision. Like a good host at a dinner party who makes sure everyone is heard. The Product Owner still decides what goes in the backlog, and in what order."),
            leo("And the third group, the organisation?"),
            maya("Leading, training and coaching the organisation in its Scrum adoption. Planning and advising Scrum implementations. Helping employees and stakeholders understand an empirical approach to complex work. And removing barriers between stakeholders and Scrum Teams."),
            leo("Give me an example of that last one."),
            maya("Say you're building the app's new payment screen, and the sales team keeps messaging requests straight to individual Developers. The Scrum Master helps sales see that requests go to the Product Owner, and invites them to the Sprint Review, where they see the real product and give feedback. The barrier comes down; the shortcut goes away."),
            leo("Now my real doubt. Can the Scrum Master also write code? I'm a developer; I'd go mad just coaching."),
            maya("Yes. The Guide doesn't forbid it: one person can be the Scrum Master and a Developer. If they're working on items in the Sprint Backlog, they join the Daily Scrum as a Developer. The catch is keeping both hats honest, so the coaching doesn't vanish under the coding."),
            leo("And how many Scrum Masters does a team get?"),
            maya("One. A Scrum Team has one Scrum Master, one Product Owner, and Developers. Not two Scrum Masters sharing the job, and not a committee."),
            leo("Hmm. And where does their authority stop? Can the Scrum Master cancel a Sprint, say?"),
            maya("No. Only the Product Owner can cancel a Sprint. The Scrum Master doesn't order the backlog, doesn't choose the work, and doesn't decide how the Developers build it. Their power is influence: teaching, coaching, asking the question that helps the team see. The Product Owner episode covers the decisions that belong to them."),
            leo("So what do I remember for the exam?"),
            maya("If an answer has the Scrum Master assigning tasks or choosing the work, it's wrong: the team is self-managing. Accountable for the Scrum Team's effectiveness: the Scrum Master. Impediments: they cause the removal, and the team solves its own small ones. And a true leader who serves the team, the Product Owner and the organisation."),
            leo("And something to try today?"),
            maya("Read the Scrum Master section of the Scrum Guide; it's under a page. Then list three impediments your team hit in the last month, and note whether each was removed by the team, by a leader, or not at all. And spot one moment when someone was assigned work instead of the team deciding."),
            leo("A true leader who serves. See you next time."),
        ),
        series = PodcastSeries.PSM1,
    )

    private val caseFiles = Podcast(
        "psm-scrum-master",
        listOf(
            maya("Welcome to Case Files. Today's case: The Coach Who Became the Boss. A team builds an Android running app with a small backend. Six months ago they hired a Scrum Master. Since then the Developers have stopped speaking up, the Daily Scrum runs forty minutes, and the last two releases to the Play Store slipped."),
            leo("[chuckle] Detective Leo, reporting. First, the basics. What's a Scrum Master supposed to be?"),
            maya("The Scrum Guide, the short free document that defines Scrum, says the Scrum Master is accountable for establishing Scrum as the Guide defines it, and for the Scrum Team's effectiveness. Accountable means answerable for the result. Since 2020 the Guide speaks of accountabilities, not roles."),
            leo("Effectiveness. So, the team working well."),
            maya("And getting better at it. The Guide calls the Scrum Master a true leader who serves the Scrum Team and the larger organisation. Keep that phrase in your notebook. Now, the first exhibit: the task board."),
            leo("What's on it?"),
            maya("Every task has a name on it, and every name was written by the Scrum Master. Each morning he hands out the day's work."),
            leo("First suspect, then: he's not handing out enough. The Developers are idle."),
            maya("[pause] Wrong suspect. Handing out work at all is the crime. The team is meant to be self-managing, which means the team decides who does what, when and how. The Developers make their own plan for the Sprint. Once someone else does it for them, they stop thinking and start waiting."),
            leo("So that's why they went quiet."),
            maya("That's clue one. Second exhibit: the Daily Scrum. Fifteen minutes is its timebox, and a timebox is a maximum length, like a parking meter. Here it runs forty, and every Developer stands facing the Scrum Master, reporting what they did yesterday."),
            leo("Second suspect: he's not strict enough. He should cut people off."),
            maya("Closer, but no. The Daily Scrum is the Developers' event, to inspect progress toward the Sprint Goal and adapt their plan. It isn't a status report to anyone. The Scrum Master ensures it happens and stays inside fifteen minutes, and he needn't even attend. Here he made himself the audience."),
            leo("Under the picture, then. What does the job really look like?"),
            maya("The Guide lists three groups the Scrum Master serves. For the team: coaching self-management and cross-functionality, meaning the team holds every skill it needs. Helping them focus on high-value Increments, usable pieces of the product, that meet the Definition of Done, the team's quality checklist."),
            leo("And the other two items for the team?"),
            maya("Causing the removal of impediments, things blocking progress that the team can't easily clear. And ensuring all Scrum events happen, and are positive, productive and inside their timebox. Third exhibit: the impediments list."),
            leo("Let me guess. Empty, because he removes everything?"),
            maya("Close. He fixed everything himself, even a broken test any Developer could have fixed in an hour. The Guide says causing the removal. Big blockers, like a security team that takes a week to sign off each build, he should chase. Small ones, the team should own."),
            leo("And how does he serve the Product Owner?"),
            maya("By helping with techniques for the Product Goal, the long-term objective for the product, and for the Product Backlog, the ordered list of everything the product might need. By helping the team understand why clear backlog items matter, and by helping establish empirical product planning: deciding from what you actually observe."),
            leo("Did our suspect do that?"),
            maya("Fourth exhibit. He re-ordered the Product Backlog himself one weekend, because he thought the Product Owner had it wrong. That's where the Scrum Master's authority stops. Ordering the backlog is the Product Owner's call. He can coach, show data, ask questions. He can't decide."),
            leo("What else can't he do?"),
            maya("He can't cancel a Sprint; only the Product Owner can. He doesn't choose the work, and he doesn't decide how the Developers build it. His power is influence, not command. The Product Owner episode covers the decisions that belong to them."),
            leo("And the organisation, the third group?"),
            maya("Leading, training and coaching it in adopting Scrum. Planning and advising Scrum implementations. Helping people understand an empirical approach to complex work. And removing barriers between stakeholders, people outside the team with a stake in the product, and Scrum Teams."),
            leo("Was there a barrier here?"),
            maya("A big one. Marketing never saw the app until release day, so they complained afterwards. The fix: invite them to the Sprint Review, see the working Increment, give feedback then. Facilitating that, making the conversation easy without taking sides, is the Scrum Master's job. The Sprint Review episode tells the rest."),
            leo("One last doubt. Could he code as well? Would that have helped?"),
            maya("He could. One person can be Scrum Master and a Developer, and if he works on Sprint Backlog items he joins the Daily Scrum as a Developer, not as its chair. A team has exactly one Scrum Master, though. Not two, and not a committee."),
            leo("So, the case summary for the exam?"),
            maya("If an answer has the Scrum Master assigning tasks or choosing the work, it's wrong: the team is self-managing. Accountable for the Scrum Team's effectiveness: the Scrum Master. Impediments: they cause the removal, and the team solves its own small ones. And a true leader who serves the team, the Product Owner and the organisation."),
            leo("Case closed. Anything to try today?"),
            maya("Read the Scrum Master section of the Scrum Guide; it's under a page. Then list three impediments your team hit in the last month, and note whether each was removed by the team, by a leader, or not at all. And spot one moment when someone was assigned work instead of the team deciding."),
            leo("From boss back to coach. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "psm-scrum-master",
        listOf(
            maya("Okay, did you hear what happened on the app team this week? Three people tried their hand at being the Scrum Master. And honestly, the stories."),
            leo("[laugh] I heard there was a whiteboard incident. But start me off. What's a Scrum Master meant to do?"),
            maya("The Scrum Guide, the short free document that defines Scrum, says the Scrum Master is accountable for establishing Scrum as the Guide defines it, and for the Scrum Team's effectiveness. Accountable means answerable for the result. The Guide calls them a true leader who serves the Scrum Team and the larger organisation."),
            leo("Serves. Noted. So, Monday?"),
            maya("Monday was Tom. Tom took over as Scrum Master and, first thing, he wrote names on every task on the board. Who builds the login screen, who fixes the backend, who writes the tests."),
            leo("Sounds organised."),
            maya("Sounds organised, and it's the exam's favourite wrong answer. The team is self-managing: the team decides who does what, when and how. The Developers make their own plan. When Tom assigned the work, nobody felt it was theirs anymore."),
            leo("Poor Tom. Tuesday?"),
            maya("Tuesday was Dana and the Daily Scrum. Its timebox is fifteen minutes, a timebox being a maximum length, like a parking meter. Dana stood at the front with a notebook and had every Developer report to her, one by one. Forty minutes."),
            leo("[sigh] A status meeting in disguise."),
            maya("Exactly. The Daily Scrum is the Developers' event, to inspect progress toward the Sprint Goal and adapt their plan. The Scrum Master ensures it happens and stays inside fifteen minutes. She doesn't have to attend, and it's never a report to her."),
            leo("Wednesday. Go."),
            maya("Wednesday was Marcus, and impediments. An impediment is anything blocking the team's progress that it can't easily clear itself. Marcus decided to remove all of them, personally. He even fixed a failing unit test at midnight."),
            leo("That sounds heroic, though."),
            maya("It sounds heroic. But the Guide says the Scrum Master causes the removal of impediments. Big ones, like a release that waits three days for another department's sign-off, yes, chase those. A failing test is the Developers' to fix. Taking it away stops them learning."),
            leo("So what is the Scrum Master meant to do for the team, really?"),
            maya("Coach self-management and cross-functionality, meaning the team holds every skill it needs. Help it focus on high-value Increments, usable pieces of the app, that meet the Definition of Done, the team's quality checklist. Cause the removal of impediments. And make sure every event happens, is positive and productive, and stays inside its timebox."),
            leo("Thursday?"),
            maya("Thursday was Priya, the Product Owner. She asked Marcus to re-order her Product Backlog, the ordered list of everything the app might need, because he had opinions."),
            leo("And he did it?"),
            maya("He did it. Wrong. Ordering the backlog is the Product Owner's call. The Scrum Master serves the Product Owner with techniques: for the Product Goal, the long-term objective for the app, for managing the backlog, and for empirical product planning, which means deciding from what you actually observe."),
            leo("Where else does the Scrum Master's power stop?"),
            maya("They can't cancel a Sprint; only the Product Owner can. They don't choose the work or how it's built. Their power is influence: coaching, teaching, facilitating, which means making a conversation easy and useful without deciding for anyone."),
            leo("And Friday? There's always a Friday."),
            maya("The intern. The intern drew an org chart with the Scrum Master at the top and the Developers underneath. [pause] The Guide says no hierarchies inside the Scrum Team. One Scrum Master, one Product Owner, Developers, all one team."),
            leo("Did anyone get it right this week?"),
            maya("Dana, on Friday afternoon. Marketing had been sending feature requests straight to Developers in chat. She helped marketing take them to the Product Owner, and invited them to the Sprint Review to see the real app. That's serving the organisation: removing barriers between stakeholders and the team."),
            leo("What else does the organisation get?"),
            maya("Leading, training and coaching it in adopting Scrum, planning and advising Scrum implementations, and helping everyone understand an empirical approach to complex work. The Product Owner episode covers the decisions on the other side of the line."),
            leo("One more doubt. Can a Scrum Master also code? Tom misses coding."),
            maya("He can. One person can be Scrum Master and a Developer. When he works on Sprint Backlog items, he joins the Daily Scrum as a Developer. One Scrum Master per team, though. Not two."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If an answer has the Scrum Master assigning tasks or choosing the work, it's wrong: the team is self-managing. Accountable for the Scrum Team's effectiveness: the Scrum Master. Impediments: they cause the removal, and the team solves its own small ones. And a true leader who serves the team, the Product Owner and the organisation."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Read the Scrum Master section of the Scrum Guide; it's under a page. Then list three impediments your team hit in the last month, and note whether each was removed by the team, by a leader, or not at all. And spot one moment when someone was assigned work instead of the team deciding."),
            leo("Coach, Tom. Not boss. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "psm-scrum-master",
        listOf(
            nova("Hey, you. It's late, and it's just us. Me, Ivy, and you, still up, still learning. [pause] Tonight: the Scrum Master. The one person on the team who works hardest at being needed less."),
            ivy("Mm. Sounds like my last relationship, Nova."),
            nova("[chuckle] Let's keep this one healthier. Picture a small team building an Android app: Developers who build it, a Product Owner who decides what matters most, and one Scrum Master."),
            ivy("And the Scrum Master is the boss. Isn't that how it goes?"),
            nova("That's the guess the exam is waiting for. [pause] The Scrum Guide, the short free document that defines Scrum, says the Scrum Master is accountable for establishing Scrum as the Guide defines it, and for the Scrum Team's effectiveness. Accountable means answerable for a result."),
            ivy("Answerable. Not in charge."),
            nova("Not in charge. The Guide calls them a true leader who serves the Scrum Team and the larger organisation. Think of a good coach: never the one scoring, but the reason the team plays better every season."),
            ivy("So who tells the Developers what to do, at this hour?"),
            nova("Nobody tells them. The team is self-managing: it decides who does what, when and how. If the Scrum Master starts assigning tasks, the team quietly stops owning its work. [pause] So they don't."),
            ivy("What do they do, then? Slowly. I'm listening."),
            nova("For the team, three things, and a fourth. Coach self-management and cross-functionality, which means the team holds every skill it needs: Android, backend, testing, design. And help them focus on high-value Increments that meet the Definition of Done."),
            ivy("Increment. Definition of Done. Tuck me in with those."),
            nova("An Increment is a usable piece of the product, a build of the app that really works. The Definition of Done is the team's quality checklist, like code reviewed and tests passing. If a piece of work misses it, it isn't part of the Increment. Not tonight, not ever."),
            ivy("And the third?"),
            nova("Causing the removal of impediments. An impediment is anything blocking progress that the team can't easily clear itself, like a release that waits three days for another department's signature. Notice the word causing. [pause] Sometimes they clear it; often they help the team clear it."),
            ivy("And small things? A broken test at midnight?"),
            nova("Belongs to the Developers. Fixing it for them steals the lesson. And the fourth: ensuring every Scrum event takes place, and is positive, productive and inside its timebox. A timebox is a maximum length, like the last train home. The Daily Scrum gets fifteen minutes."),
            ivy("So the Scrum Master runs the Daily Scrum."),
            nova("Ensures, not runs. It's the Developers' event, to inspect progress toward the Sprint Goal. The Scrum Master needn't even be there. And it's never a status report to them."),
            ivy("Who else do they look after?"),
            nova("The Product Owner. With techniques for the Product Goal, the long-term objective for the product, and for the Product Backlog, the ordered list of everything the product might need. With empirical product planning: deciding from what you actually observe. And facilitating stakeholder collaboration, as requested or needed."),
            ivy("Facilitating. Say it softer."),
            nova("[pause] Making a conversation easy and useful, without taking a side or deciding for anyone. Like a host at a late dinner who makes sure every quiet guest is heard. Stakeholders are people outside the team with a stake in the product."),
            ivy("And the third group?"),
            nova("The whole organisation. Leading, training and coaching its Scrum adoption. Planning and advising Scrum implementations. Helping people understand an empirical approach to complex work. And removing barriers between stakeholders and Scrum Teams, like sales messaging Developers directly instead of going to the Product Owner."),
            ivy("Where does their power end?"),
            nova("They can't cancel a Sprint; only the Product Owner can. They don't order the backlog, choose the work, or decide how it's built. Their power is influence. The Product Owner episode is your next late night."),
            ivy("Can a Scrum Master write code too? Some of us can't stop."),
            nova("They can. One person can be Scrum Master and a Developer, and then they join the Daily Scrum as a Developer, not as its chair. [pause] Just keep the coaching alive under all that code. But one Scrum Master a team. Not two, and never a committee."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("If an answer has the Scrum Master assigning tasks or choosing the work, it's wrong: the team is self-managing. Accountable for the Scrum Team's effectiveness: the Scrum Master. Impediments: they cause the removal, and the team solves its own small ones. And a true leader who serves the team, the Product Owner and the organisation."),
            ivy("And something to try today, before I sleep?"),
            nova("Read the Scrum Master section of the Scrum Guide; it's under a page. [pause] Then list three impediments your team hit in the last month, and note whether each was removed by the team, by a leader, or not at all. And spot one moment when someone was assigned work instead of the team deciding."),
            ivy("A leader who serves. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "psm-scrum-master",
        listOf(
            nova("Tonight is date night, darling. Candles, and a question about who's really in charge around here."),
            rafe("Clearly me."),
            nova("[laugh] Spoken like someone who's never met a Scrum Master. Picture a small team building an Android app: Developers who build it, a Product Owner who decides what matters most, and a Scrum Master. The Scrum Guide, the short free document that defines Scrum, makes the Scrum Master accountable for two things."),
            rafe("Go on. I love accountability."),
            nova("For establishing Scrum as the Guide defines it, and for the Scrum Team's effectiveness. Accountable means answerable for the result. The Guide calls them a true leader who serves the Scrum Team and the larger organisation."),
            rafe("A leader who serves. Now that's my type."),
            nova("Then a quiz, exam style. The Developers are slow to start each morning. The Scrum Master begins assigning each Developer their tasks for the day. Good idea?"),
            rafe("No. The team is self-managing: it decides who does what, when and how. Assigning tasks takes that away. The Scrum Master should coach them to plan it themselves."),
            nova("Right answer. You never tell me what to do. [kiss]"),
            rafe("I wouldn't dare. My turn. Who is the Daily Scrum for?"),
            nova("The Developers. Fifteen minutes, to inspect progress toward the Sprint Goal and adapt their plan. And the Scrum Master is the one who chairs it and takes the updates."),
            rafe("No kiss for that one. The Scrum Master ensures it happens and stays inside its timebox, a maximum length, like a table booked for one sitting. But they needn't attend, and it's never a status report to them."),
            nova("Mm. Fair. Then tell me what the Scrum Master does for the team."),
            rafe("Coaches self-management and cross-functionality, meaning the team holds every skill it needs. Helps it focus on high-value Increments, usable pieces of the app, that meet the Definition of Done, the team's quality checklist. And makes sure every event is positive, productive and timeboxed."),
            nova("And the one everyone gets half right?"),
            rafe("Impediments: anything blocking progress that the team can't easily clear itself. The Guide says the Scrum Master causes their removal."),
            nova("Quiz. A Developer finds a broken test in her own code. Should the Scrum Master fix it for her?"),
            rafe("No. That's the Developers' to solve. Causing removal means chasing the big blockers, like a release waiting days for another department, and coaching the team on the rest."),
            nova("Right answer. You know when to step back, and when to step in. [kiss]"),
            rafe("Now the Product Owner. How does the Scrum Master serve them, darling?"),
            nova("With techniques for the Product Goal, the long-term objective for the product, and for the Product Backlog, the ordered list of everything the product might need. With empirical product planning, which means deciding from what you actually see. And facilitating stakeholder collaboration as requested or needed."),
            rafe("Facilitating. Like setting the mood?"),
            nova("[chuckle] Making a conversation easy and useful without taking a side. Stakeholders are people outside the team with a stake in the product. My question. The Scrum Master thinks the backlog is in the wrong order. May they re-order it?"),
            rafe("No. Ordering the Product Backlog belongs to the Product Owner. The Scrum Master can coach, show the data, ask questions. Influence, not command."),
            nova("Right answer. Influence works on me every time. [kiss]"),
            rafe("And the organisation? The third group they serve."),
            nova("Leading, training and coaching its Scrum adoption. Planning and advising Scrum implementations. Helping people understand an empirical approach. And removing barriers between stakeholders and Scrum Teams, like sales messaging Developers directly instead of the Product Owner. The fix there: send sales to the Product Owner, and invite them to the Sprint Review to see the real app."),
            rafe("Last quiz. Who can cancel a Sprint?"),
            nova("Only the Product Owner, and only if the Sprint Goal becomes obsolete. Never the Scrum Master. The Sprint episode has the rest."),
            rafe("Right answer. And our Sprint isn't over yet. [kiss]"),
            nova("One for you. Can the Scrum Master also be a Developer?"),
            rafe("Yes. One person can hold both, and then they join the Daily Scrum as a Developer, not as its chair. The trick is keeping the coaching alive under all that coding. But only one Scrum Master per team. Not two sharing the job, and not a committee."),
            nova("Then, for the exam. What do we remember?"),
            rafe("If an answer has the Scrum Master assigning tasks or choosing the work, it's wrong: the team is self-managing. Accountable for the Scrum Team's effectiveness: the Scrum Master. Impediments: they cause the removal, and the team solves its own small ones. And a true leader who serves the team, the Product Owner and the organisation."),
            nova("Four for four. That earns a long one. [kiss]"),
            rafe("Mm. And something to try today?"),
            nova("Read the Scrum Master section of the Scrum Guide; it's under a page. Then list three impediments your team hit in the last month, and note whether each was removed by the team, by a leader, or not at all. And spot one moment when someone was assigned work instead of the team deciding."),
            rafe("A leader who serves, and a partner who listens. See you next time."),
        ),
        series = PodcastSeries.PSM1,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun team() = DiagramSpec(
        "One Scrum Team: one Scrum Master, one Product Owner and the Developers, with no hierarchy inside it.",
        nodes = listOf(
            DNode("sm", "Scrum Master", 0.5f, 0.15f, w = 0.5f, h = 0.2f, tone = 1, sub = "the team's effectiveness"),
            DNode("po", "Product Owner", 0.25f, 0.5f, w = 0.42f, h = 0.2f, sub = "the product's value"),
            DNode("dev", "Developers", 0.75f, 0.5f, w = 0.42f, h = 0.2f, sub = "the plan and the work"),
            DNode("note", "One team, no sub-teams, no hierarchy", 0.5f, 0.85f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("sm", "po", "serves"),
            DEdge("sm", "dev", "serves"),
        ),
        aspect = 0.8f,
    )

    private fun serves() = DiagramSpec(
        "The Scrum Master is a true leader who serves three groups: the Scrum Team, the Product Owner and the organisation.",
        nodes = listOf(
            DNode("sm", "Scrum Master", 0.5f, 0.15f, w = 0.5f, h = 0.2f, tone = 1, sub = "a true leader who serves"),
            DNode("team", "Scrum Team", 0.17f, 0.7f, w = 0.3f, h = 0.24f, sub = "coaching"),
            DNode("po", "Product Owner", 0.5f, 0.7f, w = 0.3f, h = 0.24f, sub = "techniques"),
            DNode("org", "Organisation", 0.83f, 0.7f, w = 0.3f, h = 0.24f, tone = 2, sub = "adoption"),
        ),
        edges = listOf(
            DEdge("sm", "team"),
            DEdge("sm", "po"),
            DEdge("sm", "org"),
        ),
        aspect = 0.8f,
    )

    private fun forTeam() = DiagramSpec(
        "How the Scrum Master serves the Scrum Team: coaching, focus on Done Increments, impediments, and events in their timebox.",
        nodes = listOf(
            DNode("coach", "Coach", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "self-management"),
            DNode("focus", "Done Increments", 0.75f, 0.18f, w = 0.42f, h = 0.2f, sub = "high value, usable"),
            DNode("imped", "Impediments", 0.25f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "cause their removal"),
            DNode("events", "Events", 0.75f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "kept in the timebox"),
        ),
        edges = listOf(
            DEdge("coach", "focus"),
            DEdge("imped", "events"),
        ),
        aspect = 0.8f,
    )

    private fun forPo() = DiagramSpec(
        "How the Scrum Master serves the Product Owner: techniques for the goal and the backlog, empirical planning, collaboration.",
        nodes = listOf(
            DNode("goal", "Product Goal", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "ways to define it"),
            DNode("backlog", "Product Backlog", 0.75f, 0.18f, w = 0.42f, h = 0.2f, sub = "clear, concise items"),
            DNode("empirical", "Empirical planning", 0.25f, 0.76f, w = 0.42f, h = 0.2f, sub = "decide from what you see"),
            DNode("stake", "Stakeholders", 0.75f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "facilitate, as needed"),
        ),
        edges = listOf(
            DEdge("goal", "backlog"),
            DEdge("empirical", "stake"),
        ),
        aspect = 0.8f,
    )

    private fun forOrg() = DiagramSpec(
        "How the Scrum Master serves the organisation: training, advising, an empirical approach, and removing barriers.",
        nodes = listOf(
            DNode("train", "Lead and train", 0.25f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "Scrum adoption"),
            DNode("advise", "Plan and advise", 0.75f, 0.18f, w = 0.42f, h = 0.2f, sub = "implementations"),
            DNode("empirical", "Empirical approach", 0.25f, 0.76f, w = 0.42f, h = 0.2f, sub = "for complex work"),
            DNode("barriers", "Remove barriers", 0.75f, 0.76f, w = 0.42f, h = 0.2f, tone = 2, sub = "stakeholders and teams"),
        ),
        edges = listOf(
            DEdge("train", "advise"),
            DEdge("empirical", "barriers"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "What the Scrum Master does not do: their power is influence, not command, and each decision has its owner.",
        nodes = listOf(
            DNode("assign", "Assign tasks", 0.25f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "no: the team decides"),
            DNode("order", "Order the backlog", 0.75f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Product Owner"),
            DNode("cancel", "Cancel a Sprint", 0.25f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Product Owner only"),
            DNode("how", "How to build it", 0.75f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "the Developers"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Scrum Master questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("assign", "Assigns the work?", 0.26f, 0.2f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "wrong: self-managing"),
            DNode("eff", "Team's effectiveness", 0.76f, 0.2f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "the Scrum Master"),
            DNode("imped", "Impediments", 0.26f, 0.74f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "causes the removal"),
            DNode("serve", "A leader who serves", 0.76f, 0.74f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "team, owner, organisation"),
        ),
        aspect = 0.8f,
    )

    private val all4 = setOf("assign", "eff", "imped", "serve")
    private val today = BoardCard("Today", "Read the section; three impediments and who removed them; one assignment.")
    private val takeaways = BoardCard("Listen for the phrase", "Assigns tasks: wrong. Effectiveness: Scrum Master. Impediments: causes removal.")
    private val selfManaging = BoardCard("Self-managing", "The team decides who does what, when and how.")
    private val accountable = BoardCard("Accountable for", "Scrum as the Guide defines it, and the team's effectiveness.")
    private val bothHats = BoardCard("Scrum Master and Developer", "Allowed. They join the Daily Scrum as a Developer.")

    private fun classicBoard() = PodcastBoard(
        "psm-scrum-master",
        listOf(
            // 0: "Hi, and welcome back. Today: the Scrum Master..."
            Scene(
                0, "Who is the Scrum Master?", team(),
                beats = listOf(
                    Beat(0, card = BoardCard("The Scrum Master", "One of three accountabilities on a Scrum Team.")),
                    Beat(1, light = setOf("sm"), card = BoardCard("A project manager?", "The most common guess, and the exam's favourite trap.")),
                    Beat(2, card = accountable),
                    Beat(4, card = BoardCard("Accountability", "Answerable for a result. Since 2020, not called a role.")),
                    Beat(6, light = setOf("dev"), card = selfManaging),
                ),
            ),
            // 7: "Then what's left for the Scrum Master?"
            Scene(
                7, "A true leader who serves", serves(),
                beats = listOf(
                    Beat(8, show = setOf("sm"), light = setOf("sm"), card = BoardCard("A true leader who serves", "Like a coach: never scoring, but the reason the team improves.")),
                    Beat(10, show = setOf("team", "po", "org"), light = setOf("team", "po", "org"), pulse = "sm" to "team", card = BoardCard("Three groups", "The Scrum Team, the Product Owner, the organisation.")),
                ),
            ),
            // 11: "Go on."
            Scene(
                11, "Serving the team", forTeam(),
                beats = listOf(
                    Beat(12, show = setOf("coach", "focus"), light = setOf("coach"), card = BoardCard("Cross-functional", "Every skill the team needs is inside the team.")),
                    Beat(14, light = setOf("focus"), pulse = "coach" to "focus", card = BoardCard("Increment, Definition of Done", "A usable step forward; the team's quality checklist.")),
                    Beat(16, show = setOf("imped"), light = setOf("imped"), card = BoardCard("Impediment", "A blocker the team can't easily clear itself.")),
                    Beat(18, card = BoardCard("Causing the removal", "Chase the big blockers; the Developers solve the small ones.")),
                    Beat(20, show = setOf("events"), light = setOf("events"), card = BoardCard("Timebox", "A maximum length. Daily Scrum 15 minutes; Planning up to 8 hours.")),
                    Beat(22, card = BoardCard("Ensures, not runs", "The Daily Scrum is the Developers'. Never a status report.")),
                ),
            ),
            // 23: "Okay. Now the Product Owner."
            Scene(
                23, "Serving the Product Owner", forPo(),
                beats = listOf(
                    Beat(24, show = setOf("goal", "backlog"), light = setOf("goal", "backlog"), pulse = "goal" to "backlog", card = BoardCard("Product Goal and Backlog", "The long-term objective, and the ordered list of work.")),
                    Beat(26, show = setOf("empirical", "stake"), light = setOf("empirical"), card = BoardCard("Empirical planning", "Decide from what you've seen: ship, watch users, replan.")),
                    Beat(28, light = setOf("stake"), card = BoardCard("Facilitating", "Making a talk easy and useful, without making the decision.")),
                ),
            ),
            // 29: "And the third group, the organisation?"
            Scene(
                29, "Serving the organisation", forOrg(),
                beats = listOf(
                    Beat(30, show = setOf("train", "advise", "empirical", "barriers"), light = setOf("train", "advise"), card = BoardCard("Scrum adoption", "Lead, train and coach; plan and advise implementations.")),
                    Beat(32, light = setOf("barriers"), card = BoardCard("Removing barriers", "Sales asks the Product Owner, and comes to the Sprint Review.")),
                ),
            ),
            // 33: "Now my real doubt. Can the Scrum Master also write code?"
            Scene(
                33, "Where the power stops", limits(),
                beats = listOf(
                    Beat(34, card = bothHats),
                    Beat(36, card = BoardCard("One Scrum Master", "Per Scrum Team. Not two, and not a committee.")),
                    Beat(38, show = setOf("assign", "order", "cancel", "how"), light = setOf("order", "cancel"), card = BoardCard("Influence, not command", "Product Owner orders and cancels; Developers decide how.")),
                ),
            ),
            // 39: "So what do I remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all4, light = all4, card = takeaways),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "psm-scrum-master--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Coach Who Became the Boss..."
            Scene(
                0, "The Coach Who Became the Boss", team(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "Quiet Developers, a 40-minute Daily Scrum, late releases.")),
                    Beat(2, light = setOf("sm"), card = accountable),
                    Beat(4, card = BoardCard("A true leader who serves", "Keep that phrase in your notebook.")),
                ),
            ),
            // 5: "What's on it?"
            Scene(
                5, "Exhibit one: the task board", limits(),
                beats = listOf(
                    Beat(6, show = setOf("assign"), light = setOf("assign"), card = BoardCard("A name on every task", "Written by the Scrum Master, each morning.")),
                    Beat(8, card = selfManaging),
                ),
            ),
            // 10: "That's clue one. Second exhibit: the Daily Scrum."
            Scene(
                10, "Exhibit two: the Daily Scrum", forTeam(),
                beats = listOf(
                    Beat(10, show = setOf("events"), light = setOf("events"), card = BoardCard("Forty minutes", "The timebox is 15. Every Developer reports to the Scrum Master.")),
                    Beat(12, card = BoardCard("The Developers' event", "Inspect progress toward the Sprint Goal. Not a status report.")),
                    Beat(14, show = setOf("coach", "focus"), light = setOf("coach", "focus"), pulse = "coach" to "focus", card = BoardCard("Serving the team", "Coach self-management; focus on Done Increments.")),
                    Beat(16, show = setOf("imped"), light = setOf("imped"), card = BoardCard("Exhibit three: impediments", "Blockers the team can't easily clear itself.")),
                    Beat(18, card = BoardCard("Causing the removal", "He fixed a test any Developer could fix. Chase the big ones.")),
                ),
            ),
            // 19: "And how does he serve the Product Owner?"
            Scene(
                19, "Serving the Product Owner", forPo(),
                beats = listOf(
                    Beat(20, show = setOf("goal", "backlog", "empirical", "stake"), light = setOf("goal", "backlog", "empirical"), card = BoardCard("Techniques", "For the Product Goal, the backlog, and empirical planning.")),
                ),
            ),
            // 21: "Did our suspect do that?"
            Scene(
                21, "Exhibit four: the backlog", limits(),
                beats = listOf(
                    Beat(22, show = setOf("order"), light = setOf("order"), card = BoardCard("He re-ordered the backlog", "The Product Owner's call. He can coach, not decide.")),
                    Beat(24, show = setOf("cancel", "how"), light = setOf("cancel", "how"), card = BoardCard("Influence, not command", "No cancelling Sprints, no choosing the work or the how.")),
                ),
            ),
            // 25: "And the organisation, the third group?"
            Scene(
                25, "Serving the organisation", forOrg(),
                beats = listOf(
                    Beat(26, show = setOf("train", "advise", "empirical", "barriers"), light = setOf("train", "advise", "empirical"), card = BoardCard("Scrum adoption", "Lead, train and coach; plan and advise; empirical work.")),
                    Beat(28, light = setOf("barriers"), card = BoardCard("Marketing at the Review", "See the working Increment and give feedback before release.")),
                ),
            ),
            // 29: "One last doubt. Could he code as well?"
            Scene(
                29, "Two hats", team(),
                beats = listOf(
                    Beat(30, light = setOf("sm", "dev"), card = bothHats),
                ),
            ),
            // 31: "So, the case summary for the exam?"
            Scene(
                31, "Case closed", exam(),
                beats = listOf(
                    Beat(32, show = all4, light = all4, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "psm-scrum-master--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the app team this week?..."
            Scene(
                0, "A week of Scrum Masters", serves(),
                beats = listOf(
                    Beat(0, card = BoardCard("Three tries", "Three people tried being the Scrum Master this week.")),
                    Beat(2, show = setOf("sm"), light = setOf("sm"), card = accountable),
                ),
            ),
            // 3: "Serves. Noted. So, Monday?"
            Scene(
                3, "Monday: Tom", limits(),
                beats = listOf(
                    Beat(4, show = setOf("assign"), light = setOf("assign"), card = BoardCard("A name on every task", "Tom assigned the login screen, the backend, the tests.")),
                    Beat(6, card = selfManaging),
                ),
            ),
            // 7: "Poor Tom. Tuesday?"
            Scene(
                7, "Tuesday and Wednesday", forTeam(),
                beats = listOf(
                    Beat(8, show = setOf("events"), light = setOf("events"), card = BoardCard("Forty minutes", "A 15-minute timebox, run as a report to Dana.")),
                    Beat(10, card = BoardCard("The Developers' event", "The Scrum Master ensures it happens; needn't attend.")),
                    Beat(12, show = setOf("imped"), light = setOf("imped"), card = BoardCard("Marcus fixed everything", "Every impediment, himself, even a failing test.")),
                    Beat(14, card = BoardCard("Causing the removal", "Chase the big blockers; the Developers fix their own tests.")),
                    Beat(16, show = setOf("coach", "focus"), light = setOf("coach", "focus", "imped", "events"), card = BoardCard("Serving the team", "Coach, focus on Done Increments, impediments, events.")),
                ),
            ),
            // 17: "Thursday?"
            Scene(
                17, "Thursday: Priya", forPo(),
                beats = listOf(
                    Beat(18, show = setOf("backlog"), light = setOf("backlog"), card = BoardCard("Re-ordered by Marcus", "Ordering the backlog is the Product Owner's call.")),
                    Beat(20, show = setOf("goal", "empirical", "stake"), light = setOf("goal", "empirical"), card = BoardCard("Serving the Product Owner", "Techniques for the goal, the backlog, empirical planning.")),
                ),
            ),
            // 21: "Where else does the Scrum Master's power stop?"
            Scene(
                21, "Where the power stops", limits(),
                beats = listOf(
                    Beat(22, show = setOf("order", "cancel", "how"), light = setOf("cancel", "how"), card = BoardCard("Influence, not command", "Coaching, teaching, facilitating; never deciding for them.")),
                ),
            ),
            // 23: "And Friday? There's always a Friday."
            Scene(
                23, "Friday: the intern", team(),
                beats = listOf(
                    Beat(24, light = setOf("note"), card = BoardCard("No hierarchy", "One Scrum Master, one Product Owner, Developers: one team.")),
                ),
            ),
            // 25: "Did anyone get it right this week?"
            Scene(
                25, "Dana gets it right", forOrg(),
                beats = listOf(
                    Beat(26, show = setOf("barriers"), light = setOf("barriers"), card = BoardCard("Removing barriers", "Marketing goes to the Product Owner, and to the Sprint Review.")),
                    Beat(28, show = setOf("train", "advise", "empirical"), light = setOf("train", "advise", "empirical"), card = BoardCard("Serving the organisation", "Lead, train and coach; plan and advise; an empirical approach.")),
                ),
            ),
            // 29: "One more doubt. Can a Scrum Master also code?"
            Scene(
                29, "Can Tom code?", team(),
                beats = listOf(
                    Beat(30, light = setOf("sm", "dev"), card = bothHats),
                ),
            ),
            // 31: "So, the gossip I should remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all4, light = all4, card = takeaways),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "psm-scrum-master--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "Needed a little less", team(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The Scrum Master: working hardest at being needed less.")),
                    Beat(3, light = setOf("sm"), card = BoardCard("The boss?", "The guess the exam is waiting for.")),
                    Beat(4, card = accountable),
                ),
            ),
            // 6: "Not in charge. The Guide calls them a true leader who serves..."
            Scene(
                6, "A leader who serves", serves(),
                beats = listOf(
                    Beat(6, show = setOf("sm"), light = setOf("sm"), card = BoardCard("A true leader who serves", "Like a coach: never scoring, but the reason the team improves.")),
                    Beat(8, show = setOf("team", "po", "org"), light = setOf("team"), pulse = "sm" to "team", card = selfManaging),
                ),
            ),
            // 10: "For the team, three things, and a fourth."
            Scene(
                10, "Serving the team", forTeam(),
                beats = listOf(
                    Beat(10, show = setOf("coach", "focus"), light = setOf("coach"), card = BoardCard("Cross-functional", "Every skill the team needs is inside the team.")),
                    Beat(12, light = setOf("focus"), pulse = "coach" to "focus", card = BoardCard("Increment, Definition of Done", "A usable step forward; the team's quality checklist.")),
                    Beat(14, show = setOf("imped"), light = setOf("imped"), card = BoardCard("Causing the removal", "Sometimes they clear it; often they help the team clear it.")),
                    Beat(16, show = setOf("events"), light = setOf("events"), card = BoardCard("Timebox", "A maximum length, like the last train. Daily Scrum: 15 minutes.")),
                    Beat(18, card = BoardCard("Ensures, not runs", "The Developers' event. Never a status report.")),
                ),
            ),
            // 19: "Who else do they look after?"
            Scene(
                19, "Serving the Product Owner", forPo(),
                beats = listOf(
                    Beat(20, show = setOf("goal", "backlog", "empirical"), light = setOf("goal", "backlog", "empirical"), card = BoardCard("Techniques", "For the Product Goal, the backlog, and empirical planning.")),
                    Beat(22, show = setOf("stake"), light = setOf("stake"), card = BoardCard("Facilitating", "Making a talk easy and useful, without deciding for anyone.")),
                ),
            ),
            // 23: "And the third group?"
            Scene(
                23, "Serving the organisation", forOrg(),
                beats = listOf(
                    Beat(24, show = setOf("train", "advise", "empirical", "barriers"), light = setOf("train", "barriers"), card = BoardCard("Adoption and barriers", "Train and coach; sales go to the Product Owner, not Developers.")),
                ),
            ),
            // 25: "Where does their power end?"
            Scene(
                25, "Where the power ends", limits(),
                beats = listOf(
                    Beat(26, show = setOf("assign", "order", "cancel", "how"), light = setOf("order", "cancel"), card = BoardCard("Influence, not command", "Product Owner orders and cancels; Developers decide how.")),
                    Beat(28, card = bothHats),
                ),
            ),
            // 29: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                29, "For the exam", exam(),
                beats = listOf(
                    Beat(30, show = all4, light = all4, card = takeaways),
                    Beat(32, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "psm-scrum-master--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Who's in charge?", team(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Who is really in charge around here?")),
                    Beat(2, light = setOf("sm"), card = accountable),
                    Beat(4, card = BoardCard("A true leader who serves", "The Scrum Team and the larger organisation.")),
                ),
            ),
            // 6: "Then a quiz, exam style..."
            Scene(
                6, "Quiz: assigning tasks", limits(),
                beats = listOf(
                    Beat(6, show = setOf("assign"), light = setOf("assign"), card = BoardCard("Quiz: assign the tasks?", "Developers slow each morning. Should the Scrum Master assign?")),
                    Beat(7, card = selfManaging),
                ),
            ),
            // 9: "I wouldn't dare. My turn. Who is the Daily Scrum for?"
            Scene(
                9, "Quiz: the Daily Scrum", forTeam(),
                beats = listOf(
                    Beat(9, show = setOf("events"), light = setOf("events"), card = BoardCard("Quiz: whose event?", "The Daily Scrum, fifteen minutes.")),
                    Beat(11, card = BoardCard("No kiss for that one", "The Scrum Master ensures it happens; needn't attend.")),
                    Beat(13, show = setOf("coach", "focus"), light = setOf("coach", "focus"), pulse = "coach" to "focus", card = BoardCard("Serving the team", "Coach self-management; focus on Done Increments.")),
                    Beat(15, show = setOf("imped"), light = setOf("imped"), card = BoardCard("Impediments", "The Scrum Master causes their removal.")),
                    Beat(17, card = BoardCard("The broken test", "The Developers' to solve. Chase the big blockers.")),
                ),
            ),
            // 19: "Now the Product Owner. How does the Scrum Master serve them, darling?"
            Scene(
                19, "Serving the Product Owner", forPo(),
                beats = listOf(
                    Beat(20, show = setOf("goal", "backlog", "empirical", "stake"), light = setOf("goal", "backlog", "empirical"), card = BoardCard("Techniques", "For the Product Goal, the backlog, and empirical planning.")),
                    Beat(22, light = setOf("stake"), card = BoardCard("Facilitating", "Easy, useful talk, without taking a side.")),
                    Beat(23, light = setOf("backlog"), card = BoardCard("Quiz: re-order it?", "No. The Product Owner orders the Product Backlog.")),
                ),
            ),
            // 25: "And the organisation? The third group they serve."
            Scene(
                25, "Serving the organisation", forOrg(),
                beats = listOf(
                    Beat(26, show = setOf("train", "advise", "empirical", "barriers"), light = setOf("barriers"), card = BoardCard("Removing barriers", "Sales to the Product Owner, and to the Sprint Review.")),
                ),
            ),
            // 27: "Last quiz. Who can cancel a Sprint?"
            Scene(
                27, "Quiz: cancel a Sprint", limits(),
                beats = listOf(
                    Beat(28, show = setOf("cancel"), light = setOf("cancel"), card = BoardCard("Only the Product Owner", "And only if the Sprint Goal becomes obsolete.")),
                    Beat(31, show = setOf("assign", "order", "how"), card = BoardCard("Scrum Master and Developer", "Allowed. One Scrum Master per team.")),
                ),
            ),
            // 32: "Then, for the exam. What do we remember?"
            Scene(
                32, "For the exam", exam(),
                beats = listOf(
                    Beat(33, show = all4, light = all4, card = takeaways),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
