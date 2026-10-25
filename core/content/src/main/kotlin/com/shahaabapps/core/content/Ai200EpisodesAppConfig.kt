package com.shahaabapps.core.content

/** App Configuration: settings and feature flags in one place: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesAppConfig {
    private val classic = Podcast(
        "ai-appconfig",
        listOf(
            maya("Hi, and welcome back. Zaiqa's API, the .NET service behind the app, runs in Container Apps, orders sit in Azure SQL, the order feed and the rider pings are in Cosmos, and Redis holds what is true right now. Today: two small values that have to change while all of that keeps running."),
            leo("Which two?"),
            maya("The surge fee, the few rupees we add to delivery when it rains. And a switch for the new search, the one the Taste service answers. Neither is worth a release at eight on a Friday."),
            leo("Mine would live in a settings file next to the code. Is that so bad?"),
            maya("For one copy of one service, it is fine. But at dinner rush Azure runs six copies of the API side by side, plus the Taste service and the order worker. They all need the same surge fee."),
            leo("So I edit the file, build again, and restart everything."),
            maya("And you miss one copy, and that copy charges the old fee. [sigh] Two diners in the same street pay different money for the same rain."),
            leo("Right. So where should the surge fee live?"),
            maya("In Azure App Configuration. Picture a noticeboard in the staff room that every service reads. You create one store for Zaiqa, write the fee on it once, and every copy of every service follows."),
            leo("What is actually written on it?"),
            maya("Keys, each with a value. A key is a setting's name. For example, Zaiqa SurgeFee, written with a colon between the two words, so keys group together like files in a folder."),
            leo("And the value is the number."),
            maya("Yes, and it is stored as text. Twenty, meaning twenty rupees, and your code turns it into a number. The Taste service reads the same key and gets the same twenty."),
            leo("But I want to try a mad surge fee without charging real diners."),
            maya("Then you use a label. A label is a sticky tag on a key, so one key holds a different value for each place the app runs: dev, your testing copy, and prod, the real one diners use."),
            leo("So the setting called SurgeFee is two hundred with the dev label, and twenty with the prod label."),
            maya("Exactly. And a key can have no label at all, which makes it the shared default. A service loads the no-label keys first, then its own label on top, and where a key is in both, the later one wins."),
            leo("Fine. Can the payment provider's key go on the board too? It is just another setting."),
            maya("No, and that is the first hard rule. App Configuration is for settings that are not secrets. The payment key stays in Key Vault, the locked safe we gave it last episode."),
            leo("Then how does the API find it, if it is not on the board?"),
            maya("The board holds a Key Vault reference: a key whose value is the secret's address, not the secret. Picture a note on the noticeboard saying, the payment key is in the safe, in the drawer called PaymentKey."),
            leo("And who opens the safe?"),
            maya("The API does, with its own managed identity, the badge Azure gives your app so it signs in with no password in the code. The same badge that pulls our images. It needs one permission on the board and one on the safe."),
            leo("Named what?"),
            maya("Roles. To read the board, App Configuration Data Reader. To read the safe, Key Vault Secrets User. Give the badge the first and forget the second, and here is the part that bites."),
            leo("The payment setting comes back empty."),
            maya("No. The whole load fails and the API does not start. The provider follows every reference while it is loading, one of them is refused, it raises an error, and your service never comes up."),
            leo("[pause] So a missing role on the safe is not a broken setting. It is an outage."),
            maya("It is. And the logs say forbidden from the vault, while every plain key loaded perfectly. That pairing is an exam favourite."),
            leo("Noted. And if I want to change values, not only read them?"),
            maya("That is App Configuration Data Owner: read, write, and take snapshots. The services only read, so they get Data Reader. You, editing the surge fee, get Data Owner."),
            leo("How does the code read the board at all?"),
            maya("With the configuration provider, a ready-made library from Microsoft. There is one for .NET and one for Python, and they behave the same way. You call load once, when the service starts."),
            leo("Like reading my Android preferences at launch. And what comes back?"),
            maya("Something that works like a dictionary. Ask for SurgeFee, get twenty. It has already followed the Key Vault references, so the payment key is simply there."),
            leo("Now. It rains. I change the fee on the board. When does the running API notice?"),
            maya("Only when it checks. Nothing pushes anything to it. The provider has a refresh call, and you make it at the start of every request the service handles."),
            leo("Every request? So every diner's tap costs a call to Azure as well?"),
            maya("No. Refresh quietly does nothing until a set time has passed, thirty seconds by default. Only then does it really look. And if you never call refresh, your change never arrives at all."),
            leo("Does it then check every key? We have dozens."),
            maya("That is why you pick a sentinel key: one key the service watches, like a guard on watch. Say it holds twelve. You change it to thirteen, and that is the signal to reload everything."),
            leo("So I change the surge fee, then the sentinel."),
            maya("The sentinel last, always. Then every copy reloads once, with the new fee already in place, and nobody ever sees half a change. No restart, no release."),
            leo("[pause] And the new search switch?"),
            maya("That is a feature flag: an on and off switch stored on the same board. The code for the new search is already shipped, hidden behind a flag called NewSearch. Turn it on, and the search appears."),
            leo("Not for everyone at once, I hope."),
            maya("No. You add a filter, which is a rule on the flag. On for ten percent of diners, or on only between two and five in the morning, or on for named users and groups. That last one is called targeting."),
            leo("And the code asks the flag what, exactly?"),
            maya("One question, through the feature management library: is this on for this diner? If yes, the search box calls the Taste service's new path. If no, the old one."),
            leo("Last thing. We pushed a bad fee once. Can I freeze a set I trust?"),
            maya("That is a snapshot: a named copy of chosen settings that can never be changed once it is taken. Before a release the team takes one, say release 1.4, and that release loads it by name."),
            leo("So an edit the next morning cannot touch it."),
            maya("It cannot. Going back means loading an older one. [pause] And the four mistakes: the secret written on the board as plain text, watching every key instead of a sentinel, never calling refresh, and a role on the board with none on the safe."),
            leo("So what do I remember for the exam?"),
            maya("If the question says reload when one key changes, a sentinel key. If changed values never arrive, nobody is calling refresh. If a Key Vault reference is refused and the app will not start, the badge is missing Key Vault Secrets User."),
            leo("Keep going."),
            maya("If the service only reads settings, Data Reader; if it writes or takes snapshots, Data Owner. If a feature must reach ten percent of users with no new release, a feature flag with a filter. And if a release must use exactly what was tested, a snapshot."),
            leo("Good. And something to try today?"),
            maya("Create a store and give yourself App Configuration Data Owner. Add one key with a dev label and one with a prod label. Load them in Python with the provider and print both. Then watch a sentinel, change a value and the sentinel, and see refresh pick it up."),
            leo("One board, one change, every kitchen follows. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-appconfig",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Release That Would Not Start. Half past seven on a Friday, the Zaiqa API, the .NET service behind the app, goes out with one new setting, and every copy dies before it serves a single order."),
            leo("Detective Leo, reporting. One new setting. Which one?"),
            maya("The surge fee, the few rupees we add to delivery when it rains. Product wanted it changeable without a release, so we moved it off the settings file and onto Azure App Configuration."),
            leo("Which is what, in plain words?"),
            maya("One shared noticeboard in the staff room. A store you create once, that every copy of every Zaiqa service reads. Keys, each with a value: Zaiqa SurgeFee is twenty, meaning twenty rupees."),
            leo("Then my first suspect is the fee itself. Somebody typed twenty thousand."),
            maya("A fair suspect, and wrong. A silly number makes expensive chips, not a dead service. This one never finished starting. [pause] Look at what else went onto the board that afternoon."),
            leo("Reading the evidence. The setting called SurgeFee, with a tag on it. The one called NewSearch. And the one called PaymentKey."),
            maya("Stop there. That tag is a label: a sticky tag on a key, so one key holds a different value for each place the app runs, dev for testing and prod for real diners. That one is innocent."),
            leo("Then the payment key. Somebody put the payment provider's secret on a shared noticeboard?"),
            maya("No, and that is the next wrong turn. What is on the board is a Key Vault reference: a key whose value is the secret's address, not the secret. A note saying, it is in the safe, in the drawer called PaymentKey."),
            leo("So the secret never left Key Vault. Then where is my crime?"),
            maya("In who may open the safe. The API follows that note itself, with its managed identity, the badge Azure gives your app so it signs in with no password in the code."),
            leo("And the badge was given nothing?"),
            maya("It was given App Configuration Data Reader on the store, which is exactly right for reading settings. Nobody gave it Key Vault Secrets User on the vault."),
            leo("Then one setting comes back empty, surely. Why is the whole service down?"),
            maya("Because the reference is followed while the settings are loading, not later. One refusal, and the load raises an error. It does not skip that key and carry on. The service never reaches its first line of work."),
            leo("[sigh] An outage from a missing role. What did the logs actually say?"),
            maya("Forbidden, from the vault, at start-up, with every plain key loading perfectly. That pairing is the whole clue: plain keys fine, one note refused, nothing running."),
            leo("Case closed, then. Two roles, not one."),
            maya("Two roles. And while we are here, the other three mistakes the team nearly made that week."),
            leo("Go on. I will take notes."),
            maya("One: writing the payment secret on the board as plain text, which is how this started. Two: never calling refresh."),
            leo("Refresh. Explain that one properly."),
            maya("Nothing pushes a change to a running service. You make a refresh call at the start of every request, and it quietly does nothing until thirty seconds have passed. Skip it, and your new fee never arrives."),
            leo("And the third?"),
            maya("Watching every key. Instead you pick a sentinel key: one key the service watches, like a guard on watch. Hold it at twelve, move it to thirteen, and every copy reloads the lot."),
            leo("Order of operations?"),
            maya("Fee first, sentinel last. Then nobody ever reads half a change. [pause] And the new search went out the same week, behind a feature flag."),
            leo("A flag being?"),
            maya("An on and off switch stored on the same board, with the code already shipped and hidden behind it. Add a filter, a rule on the flag, and it is on for ten percent of diners, or for named users and groups, which is called targeting."),
            leo("And if someone edits the board the day after a release?"),
            maya("Then you want a snapshot: a named copy of chosen settings that cannot be changed once it is taken. Release 1.4 loads its own by name and ignores whatever happened since."),
            leo("Where does all this stop working? Every case has an edge."),
            maya("Three edges. Secrets never go on the board, only notes pointing at them. The Android app never reads the store at all; it asks the API, which does. And a copy with no traffic refreshes on its next request, not before."),
            leo("And if I want more on the safe itself?"),
            maya("The Key Vault episode, the one just before this, is the safe from the inside."),
            leo("Then give me the exam take-aways and I will close the file."),
            maya("If a Key Vault reference is refused and the app will not start, the badge is missing Key Vault Secrets User. If changed values never arrive, nobody calls refresh. If the question says reload when one key changes, a sentinel key."),
            leo("Three. Two more."),
            maya("If the service only reads, App Configuration Data Reader; writing values or taking snapshots is Data Owner. And if a feature must reach ten percent of users with no new release, a feature flag with a filter."),
            leo("Case closed. And what do we try today?"),
            maya("Create a store and give yourself App Configuration Data Owner. Add one key with a dev label and one with a prod label. Load them in Python with the provider and print both. Then watch a sentinel, change a value and the sentinel, and see refresh pick it up."),
            leo("Plain keys loaded, one note refused, nothing running. Filed. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-appconfig",
        listOf(
            maya("Okay, did you hear what the Zaiqa team did to the settings this week? Sit down. They took the surge fee and the new search switch off the settings files and put them on Azure App Configuration, and it was quite a week."),
            leo("I heard the word forbidden shouted from three desks at once. Start at the beginning. Why move anything?"),
            maya("Because the surge fee, the few rupees we add to delivery when it rains, lived in a file beside the code. Six copies of the Zaiqa API, the .NET service behind the app, plus the Taste service, and all of them had to agree."),
            leo("And changing it meant a release."),
            maya("A release at eight on a Friday, in the rain. So: one store, which is a shared noticeboard every service reads. Keys with values, Zaiqa SurgeFee is twenty, and nobody rebuilds anything."),
            leo("Lovely. Now the carnage. Who went first?"),
            maya("Tom. Monday. Tom put the payment provider's secret on the board as a plain value, because, quote, it is just another setting."),
            leo("[chuckle] And?"),
            maya("Security found it within the hour. The board is for settings that are not secrets. What belongs there is a Key Vault reference: a key whose value is the secret's address, not the secret itself."),
            leo("A note saying which drawer of the safe. Fine. Tuesday?"),
            maya("Dana. Dana shipped the reference properly, gave the API's managed identity, the badge Azure gives your app, App Configuration Data Reader on the store, and went to lunch."),
            leo("That sounds correct to me."),
            maya("Half correct. Nobody gave that badge Key Vault Secrets User on the vault. And the whole service refused to start."),
            leo("Not one empty setting? The whole thing?"),
            maya("The whole thing. References are followed while the settings load, so one refusal raises an error and the load fails. Plain keys all fine, one note refused, nothing running."),
            leo("[sigh] Poor Dana. Wednesday."),
            maya("Priya, and this one is only expensive. Priya made the service watch every key she had loaded, all forty of them, on every refresh."),
            leo("Refresh being?"),
            maya("A call you make at the start of each request. Nothing pushes a change to a running service, so it has to look for itself, and it only really looks once thirty seconds have passed."),
            leo("So that is forty looks from Priya, every thirty seconds, times six copies."),
            maya("Exactly. What she wanted was a sentinel key: one key the service watches, like a guard on watch. Change your values, then move the sentinel from twelve to thirteen, and everything reloads once."),
            leo("Values first, sentinel last. Thursday?"),
            maya("Marcus. Marcus never called refresh at all. He changed the fee, watched the board, and told the whole floor that App Configuration was broken."),
            leo("[chuckle] It was doing exactly nothing, perfectly."),
            maya("Perfectly. And a tag caught him out the week before, so let us do tags. A label is a sticky tag on a key, so one key holds a different value for each place the app runs."),
            leo("Dev for testing, prod for real diners."),
            maya("And a key with no label at all is the shared default. The service loads the no-label keys first, then its own label on top, and where a key is in both, the later one wins."),
            leo("Right. And the intern? There is always an intern."),
            maya("The intern was the hero, actually. The new search went out behind a feature flag: an on and off switch stored on the same board, with the code already shipped and hidden behind it."),
            leo("Turned on for everybody at five on a Friday?"),
            maya("No. The intern added a filter, which is a rule on the flag, and it went on for ten percent of diners. You can also pick named users and groups, which is called targeting, or a time window."),
            leo("And the week ended how?"),
            maya("With a snapshot, which is a named copy of chosen settings that can never be changed once it is taken. They took one called release 1.4, so that release loads what was tested, whatever anyone edits afterwards."),
            leo("Three more things I will be asked. Where does the board stop?"),
            maya("Secrets never live on it. The Android app never reads it; it asks the API, which does. And a copy that has been quiet all evening sees your change on its next request."),
            leo("Good. And for the exam, the short version?"),
            maya("If a Key Vault reference is refused and the app will not start, Key Vault Secrets User is missing. If changes never arrive, nobody calls refresh. If one key should trigger a reload, a sentinel key."),
            leo("Two more."),
            maya("Read-only is App Configuration Data Reader; writing values and taking snapshots is Data Owner. And ten percent of users with no release is a feature flag with a filter. The Key Vault episode has the safe itself."),
            leo("And what are we trying today?"),
            maya("Create a store and give yourself App Configuration Data Owner. Add one key with a dev label and one with a prod label. Load them in Python with the provider and print both. Then watch a sentinel, change a value and the sentinel, and see refresh pick it up."),
            leo("Tom, Dana, Priya and Marcus, all in one week. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-appconfig",
        listOf(
            nova("Hey, you. It is late, the rain is doing that thing on the window, and it is just the three of us. Me, Ivy, and you, still awake. [pause] Tonight: the two little values that change while Zaiqa keeps running."),
            ivy("Two values. Go on, slowly."),
            nova("The surge fee, the few rupees we add to delivery when it rains. And a switch for the new search. [pause] Neither of them is worth waking anybody up for a release."),
            ivy("And where do they live now? Beside the code, I assume."),
            nova("They did. In a settings file, copied into every service. Six copies of the Zaiqa API, the .NET service behind the app, the Taste service, the order worker. All of them had to be told."),
            ivy("And you would miss one."),
            nova("Somebody always misses one. [pause] So tonight they come off the file and onto Azure App Configuration: one store, a noticeboard in the staff room that every service reads."),
            ivy("And written on it?"),
            nova("Keys, each with a value. Zaiqa SurgeFee is twenty, meaning twenty rupees. The API reads twenty. The Taste service reads twenty. Nobody disagrees, ever again."),
            ivy("Mm. And when you want to be reckless in testing?"),
            nova("Then you put a label on it. A label is a sticky tag on a key, so one key holds a different value for each place the app runs: dev for your testing copy, prod for real diners."),
            ivy("And a key with no tag at all?"),
            nova("That one is the shared default. A service loads the no-label keys first, then its own label on top, and where both have a key, the later one wins. [pause] Quiet and tidy."),
            ivy("Now the part I always want at this hour. The secret."),
            nova("Not on the board, you. The board is for settings that are not secrets. The payment provider's key stays in Key Vault, the safe, exactly where we left it last episode."),
            ivy("Then how does the API reach it in the dark?"),
            nova("The board holds a Key Vault reference: a key whose value is the secret's address, not the secret. A note saying, it is in the safe, in the drawer called PaymentKey."),
            ivy("And the API opens that drawer itself."),
            nova("With its managed identity, the badge Azure gives your app so it signs in with no password in the code. Two permissions: App Configuration Data Reader on the board, Key Vault Secrets User on the safe."),
            ivy("And if somebody forgets the second one?"),
            nova("Then nothing starts. [pause] Not one quiet missing setting. The reference is followed while the settings are loading, the refusal raises an error, and the service never gets up at all."),
            ivy("That is a horrible way to find out."),
            nova("It is. Plain keys perfect, one note refused, the whole thing down. Remember that pairing. The exam is fond of it."),
            ivy("Noted. Now. It starts raining at eleven, and you change the fee. Does anybody notice?"),
            nova("Only if they look. Nothing pushes. You call refresh at the start of each request, and it does nothing at all until thirty seconds have passed. Never call it, and your change never lands."),
            ivy("And it checks everything each time?"),
            nova("Only one thing, if you are kind to it. A sentinel key: one key the service watches, like a guard on watch. It sits at twelve. You move it to thirteen, and every copy reloads the lot at once."),
            ivy("Values first, guard last."),
            nova("Always that way round. [pause] Then nobody ever reads half a change."),
            ivy("And the new search? Is it on yet?"),
            nova("For some of you. It is behind a feature flag, an on and off switch stored on the same board, with the code already shipped and hidden behind it."),
            ivy("On for whom, exactly?"),
            nova("You add a filter, a rule on the flag: ten percent of diners, or a time window, or named users and groups, which is called targeting. The library that answers, is this on for this diner, is feature management."),
            ivy("And who does the reading, inside the app?"),
            nova("The configuration provider, a ready-made library from Microsoft, one for .NET and one for Python. You call load once when the service starts, and it hands back something like a dictionary."),
            ivy("Last one, and then I will let you sleep. The bad edit."),
            nova("A snapshot. A named copy of chosen settings that can never be changed once it is taken. Release 1.4 loads its own by name, whatever anybody types on the board the next morning."),
            ivy("And the edges? Where does the noticeboard stop?"),
            nova("The Android app never reads it; it asks the API, which does. And a copy nobody has called all evening gets your change on its next request, not before. [pause] The Key Vault episode is the safe itself."),
            ivy("Then the exam. What are you leaving me with?"),
            nova("Reference refused and the app will not start: Key Vault Secrets User. Changes never arriving: nobody calls refresh. Reload when one key changes: a sentinel key. The service only reads: Data Reader. Ten percent with no release: a flag with a filter."),
            ivy("And the thing to try, before the tea goes cold?"),
            nova("Today: create a store and give yourself App Configuration Data Owner. Add one key with a dev label and one with a prod label. Load them in Python with the provider and print both. Then watch a sentinel, change a value and the sentinel, and see refresh pick it up."),
            ivy("One board, one small change, and everyone follows. Goodnight, you. Sleep well."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-appconfig",
        listOf(
            nova("Tonight is date night, darling. Rain on the window, something with chilli in it, and you and me talking about the two little values Zaiqa changes without telling anybody."),
            rafe("Rain. So that is the surge fee, the few rupees you add to delivery when it pours, and the riders get soaked and want paying for it."),
            nova("That one, and a switch for the new search. Both used to live in a settings file beside the code, copied into six copies of the Zaiqa API, the .NET service behind the app, and into the Taste service."),
            rafe("And you edited one and forgot the rest. Careless, love."),
            nova("Which is why they are on a noticeboard now. Azure App Configuration: one store, every service reads it, keys with values. Zaiqa SurgeFee is twenty, meaning twenty rupees, and the Taste service reads the very same twenty. So quiz me, and be strict."),
            rafe("Strict it is. One key, two environments, and I do not want two stores. What do you reach for?"),
            nova("A label: a sticky tag on a key, so one key holds a different value for each place the app runs, dev for testing and prod for real diners. And a key with no label is the shared default, loaded first."),
            rafe("Right answer, and you got the order right too. [kiss]"),
            nova("Mm. My turn. The payment provider's key. On the board, or not?"),
            rafe("Never. The board is for settings that are not secrets. The board gets a Key Vault reference, a key whose value is the secret's address, and the safe keeps the secret itself."),
            nova("Right answer. An address is not an invitation. [kiss]"),
            rafe("Then who opens the safe, darling?"),
            nova("The API does, with its managed identity, the badge Azure gives your app so it signs in with no password. App Configuration Data Reader on the board, Key Vault Secrets User on the safe."),
            rafe("Both, yes. Now the cruel one, exam style. The badge has Data Reader, nobody granted the vault role, and the release goes out. What does the diner see?"),
            nova("Nothing at all, because nothing started. The reference is followed while the settings load, so the refusal raises an error and the whole load fails. Not one empty setting: no service."),
            rafe("Right answer, and most people say empty setting. [kiss]"),
            nova("I have been caught by it. [pause] Now, it starts raining at eleven and I change the fee. How does a running copy hear about it?"),
            rafe("It does not, unless it asks. You call refresh at the start of each request, and it sits still until thirty seconds have passed. No refresh, no news."),
            nova("Right answer. Nobody tells you anything unless you ask. [kiss]"),
            rafe("And you would not have it check forty keys at a time."),
            nova("No. A sentinel key: one key the service watches, like a guard on watch. Change your values, then move it from twelve to thirteen, and every copy reloads everything at once. Nobody ever reads half a change."),
            rafe("Values first, guard last. Very proper. And the reading, in code?"),
            nova("The configuration provider, a ready-made library from Microsoft, one for .NET and one for Python. Called once when the service starts, and it hands you something like a dictionary."),
            rafe("Then my next question. The new search is ready, and I want it for a tenth of the diners tonight. No release."),
            nova("A feature flag with a filter. The flag is an on and off switch on the same board, the code already shipped behind it, and the filter is the rule: ten percent, or a time window, or named users and groups."),
            rafe("Targeting, that last one. Right answer, and greedy with the detail. [kiss]"),
            nova("I learn from the best. One more for you, and it is the one people lose. A release must run exactly the settings that were tested, even if somebody edits the board at breakfast."),
            rafe("A snapshot. A named copy of chosen settings that can never be changed once it is taken. Release 1.4 loads its own, by name."),
            nova("Right answer. Some things should stay exactly as they were. [kiss]"),
            rafe("And where does our lovely noticeboard stop working?"),
            nova("The Android app never reads it; it asks the API, which does. A copy nobody has called all evening hears you on its next request, not a moment sooner, because refresh only runs when a request comes in. And the Key Vault episode is the safe from the inside."),
            rafe("Last one, for the exam. Leave me five."),
            nova("Reference refused and nothing starts: Key Vault Secrets User. Values never arriving: call refresh. Reload on one key: a sentinel key. The service only reads: Data Reader, and Data Owner to write or take a snapshot. Ten percent, no release: a flag with a filter."),
            rafe("Five for five. And something to try today, darling?"),
            nova("Create a store and give yourself App Configuration Data Owner. Add one key with a dev label and one with a prod label. Load them in Python with the provider and print both. Then watch a sentinel, change a value and the sentinel, and see refresh pick it up."),
            rafe("And I will be your sentinel, watching for the smallest change. Goodnight, everyone."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun board() = DiagramSpec(
        "One App Configuration store that every copy of every Zaiqa service reads, instead of a settings file each.",
        nodes = listOf(
            DNode("phone", "Zaiqa app", 0.24f, 0.18f, w = 0.4f, h = 0.2f, sub = "the diner"),
            DNode("api", "Zaiqa API", 0.76f, 0.18f, w = 0.4f, h = 0.2f, tone = 1, sub = "six copies"),
            DNode("file", "Settings file", 0.24f, 0.74f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "edit and restart"),
            DNode("store", "App Configuration", 0.76f, 0.74f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "one store"),
        ),
        edges = listOf(
            DEdge("phone", "api", "asks"),
            DEdge("api", "file", dashed = true),
            DEdge("api", "store", "reads"),
        ),
        aspect = 0.8f,
    )

    private fun keys() = DiagramSpec(
        "One key, a value per label: the service loads the no-label default first, then its own label, and the later one wins.",
        nodes = listOf(
            DNode("key", "Zaiqa SurgeFee", 0.5f, 0.14f, w = 0.64f, h = 0.2f, kind = NodeKind.PILL, tone = 1, sub = "a setting's name"),
            DNode("dev", "dev label", 0.24f, 0.5f, w = 0.4f, h = 0.2f, sub = "for testing"),
            DNode("prod", "prod label", 0.76f, 0.5f, w = 0.4f, h = 0.2f, sub = "for real diners"),
            DNode("def", "No label", 0.5f, 0.85f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, sub = "the shared default"),
        ),
        edges = listOf(
            DEdge("key", "dev"),
            DEdge("key", "prod"),
            DEdge("key", "def"),
        ),
        aspect = 0.8f,
    )

    private fun vault() = DiagramSpec(
        "The store holds a Key Vault reference; the API follows it with its own badge, so it needs a role on both.",
        nodes = listOf(
            DNode("store", "App Configuration", 0.24f, 0.17f, w = 0.42f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "settings only"),
            DNode("ref", "Key Vault reference", 0.24f, 0.52f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "an address"),
            DNode("vault", "Key Vault", 0.76f, 0.52f, w = 0.4f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 2, sub = "the safe"),
            DNode("api", "Zaiqa API", 0.5f, 0.86f, w = 0.5f, h = 0.2f, tone = 1, sub = "managed identity"),
        ),
        edges = listOf(
            DEdge("store", "ref"),
            DEdge("ref", "vault", "points to"),
            DEdge("api", "store", "Data Reader"),
            DEdge("api", "vault", "Secrets User"),
        ),
        aspect = 0.8f,
    )

    private fun refresh() = DiagramSpec(
        "Refresh checks one sentinel key once the interval has passed; when it changes, every key reloads at once.",
        nodes = listOf(
            DNode("api", "Zaiqa API", 0.24f, 0.17f, w = 0.42f, h = 0.2f, tone = 1, sub = "refresh per request"),
            DNode("store", "App Configuration", 0.76f, 0.17f, w = 0.42f, h = 0.28f, kind = NodeKind.CYLINDER),
            DNode("sentinel", "Sentinel key", 0.76f, 0.56f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, tone = 2, sub = "12 becomes 13"),
            DNode("reload", "Reload everything", 0.24f, 0.56f, w = 0.42f, h = 0.2f, sub = "all keys at once"),
            DNode("order", "Edit the values, then the sentinel last", 0.5f, 0.87f, w = 0.84f, h = 0.16f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("api", "store", "every 30 s at most"),
            DEdge("store", "sentinel", "watched"),
            DEdge("sentinel", "reload"),
        ),
        aspect = 0.8f,
    )

    private fun flags() = DiagramSpec(
        "A feature flag with a filter switches the new search on for some diners; a snapshot freezes a release's settings.",
        nodes = listOf(
            DNode("flag", "Feature flag", 0.24f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.PILL, sub = "NewSearch"),
            DNode("filter", "Filter", 0.24f, 0.62f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "10 percent of diners"),
            DNode("snap", "Snapshot", 0.76f, 0.2f, w = 0.42f, h = 0.22f, tone = 2, sub = "named and frozen"),
            DNode("release", "Release", 0.76f, 0.62f, w = 0.42f, h = 0.22f, tone = 1, sub = "loads it by name"),
        ),
        edges = listOf(
            DEdge("flag", "filter"),
            DEdge("snap", "release"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where the board stops: secrets go to Key Vault, the phone asks the API, and a quiet copy refreshes on its next request.",
        nodes = listOf(
            DNode("secrets", "Secrets", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "Key Vault, not the board"),
            DNode("kv", "Key Vault episode", 0.76f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "the safe itself"),
            DNode("phone", "Zaiqa app", 0.26f, 0.76f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "asks the API instead"),
            DNode("quiet", "Quiet copy", 0.76f, 0.76f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "sees it next request"),
        ),
        edges = listOf(DEdge("secrets", "kv", "more in")),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's App Configuration questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("sentinel", "Reload on one key", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "sentinel key"),
            DNode("refresh", "Changes never arrive", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "call refresh"),
            DNode("ref", "Nothing starts at all", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Key Vault Secrets User"),
            DNode("snap", "Exactly what was tested", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "snapshot"),
            DNode("flag", "10 percent, no release", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "flag with a filter"),
            DNode("reader", "The service only reads", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Data Reader"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("sentinel", "refresh", "ref", "snap", "flag", "reader")
    private val examCard = BoardCard("For the exam", "The phrase in the question, and the answer it points to.")
    private val today = BoardCard("Today", "A store, a dev and a prod label, load in Python, then a sentinel refresh.")

    private fun classicBoard() = PodcastBoard(
        "ai-appconfig",
        listOf(
            // 0: "Zaiqa's API runs in Container Apps... two small values that have to change"
            Scene(
                0, "Where settings live", board(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), pulse = "phone" to "api", card = BoardCard("Where we are", "The API in Container Apps, orders in SQL, the feed in Cosmos.")),
                    Beat(4, show = setOf("file"), light = setOf("api", "file"), pulse = "api" to "file", card = BoardCard("Six copies, one file each", "Edit, rebuild, restart every one. Miss one and it charges the old fee.")),
                    Beat(8, show = setOf("store"), light = setOf("store"), pulse = "api" to "store", card = BoardCard("App Configuration", "One store every service reads, so you change a value once.")),
                ),
            ),
            // 10: "Keys, each with a value..."
            Scene(
                10, "Keys and labels", keys(),
                beats = listOf(
                    Beat(10, show = setOf("key"), light = setOf("key"), card = BoardCard("Key and value", "Zaiqa SurgeFee is twenty: twenty rupees added when it rains.")),
                    Beat(14, show = setOf("dev", "prod"), light = setOf("dev", "prod"), card = BoardCard("Label", "A sticky tag, so one key has a value per place the app runs.")),
                    Beat(16, show = setOf("def"), light = setOf("def"), card = BoardCard("No label is the default", "Load the no-label keys first, then your label. The later one wins.")),
                ),
            ),
            // 18: "App Configuration is for settings that are not secrets."
            Scene(
                18, "Secrets stay in the safe", vault(),
                beats = listOf(
                    Beat(18, show = setOf("store", "vault"), light = setOf("store"), card = BoardCard("Not on the board", "Settings only. The payment key stays in Key Vault, the safe.")),
                    Beat(20, show = setOf("ref"), light = setOf("ref"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "A key whose value is the secret's address, not the secret.")),
                    Beat(22, show = setOf("api"), light = setOf("api"), pulse = "api" to "store", card = BoardCard("Managed identity", "The badge Azure gives your app, so no password lives in the code.")),
                    Beat(24, light = setOf("api", "vault"), pulse = "api" to "vault", card = BoardCard("Two roles", "Data Reader on the board, Key Vault Secrets User on the safe.")),
                    Beat(26, light = setOf("ref", "vault"), card = BoardCard("Nothing starts", "A refused reference fails the whole load: no empty setting, no service.")),
                    Beat(30, light = setOf("store"), card = BoardCard("Data Owner", "Reading is Data Reader. Writing values and snapshots is Data Owner.")),
                ),
            ),
            // 32: "With the configuration provider..."
            Scene(
                32, "Reading and refreshing", refresh(),
                beats = listOf(
                    Beat(32, show = setOf("api", "store"), light = setOf("api"), card = BoardCard("Configuration provider", "A library from Microsoft. Call load once, when the service starts.")),
                    Beat(36, light = setOf("api", "store"), pulse = "api" to "store", card = BoardCard("Refresh", "Called every request, it looks only once thirty seconds have passed.")),
                    Beat(40, show = setOf("sentinel"), light = setOf("sentinel"), pulse = "store" to "sentinel", card = BoardCard("Sentinel key", "The one key each copy watches. Twelve becomes thirteen.")),
                    Beat(42, show = setOf("reload", "order"), light = setOf("reload"), pulse = "sentinel" to "reload", card = BoardCard("The sentinel last", "Then every copy reloads once and nobody reads half a change.")),
                ),
            ),
            // 44: "That is a feature flag..."
            Scene(
                44, "Flags and snapshots", flags(),
                beats = listOf(
                    Beat(44, show = setOf("flag"), light = setOf("flag"), card = BoardCard("Feature flag", "An on and off switch on the board. NewSearch is already shipped.")),
                    Beat(46, show = setOf("filter"), light = setOf("filter"), pulse = "flag" to "filter", card = BoardCard("Filter", "Ten percent of diners, a time window, or named users and groups.")),
                    Beat(50, show = setOf("snap", "release"), light = setOf("snap", "release"), pulse = "snap" to "release", card = BoardCard("Snapshot", "A named set that cannot change. Release 1.4 loads it by name.")),
                ),
            ),
            // 54: "If the question says reload when one key changes..."
            Scene(
                54, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("sentinel", "refresh", "ref"), card = examCard),
                    Beat(56, light = setOf("reader", "flag", "snap")),
                    Beat(58, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-appconfig--case-files",
        listOf(
            Scene(
                0, "The case", board(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), card = BoardCard("The case", "Half past seven, one new setting, and every copy dies before an order.")),
                    Beat(2, show = setOf("file", "store"), light = setOf("store"), pulse = "api" to "store", card = BoardCard("One store, not a file each", "The surge fee moves off the settings file onto the noticeboard.")),
                    Beat(4, light = setOf("store"), card = BoardCard("Keys and values", "Zaiqa SurgeFee is twenty: twenty rupees, read by every service.")),
                ),
            ),
            Scene(
                8, "Suspect one: the label", keys(),
                beats = listOf(
                    Beat(8, show = setOf("key", "dev", "prod", "def"), light = setOf("dev", "prod"), card = BoardCard("Label", "A sticky tag, so one key has a value per place the app runs.")),
                ),
            ),
            Scene(
                10, "Suspect two: the secret", vault(),
                beats = listOf(
                    Beat(10, show = setOf("store", "ref", "vault"), light = setOf("ref"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "A note on the board: the address of the secret, not the secret.")),
                    Beat(12, show = setOf("api"), light = setOf("api"), card = BoardCard("Managed identity", "The badge Azure gives your app, so no password lives in the code.")),
                    Beat(14, light = setOf("api", "store"), pulse = "api" to "store", card = BoardCard("Data Reader, and nothing else", "Right for the board. Nobody granted a role on the vault.")),
                    Beat(16, light = setOf("api", "vault"), pulse = "api" to "vault", card = BoardCard("Nothing starts", "A refused reference fails the whole load: no empty setting, no service.")),
                    Beat(18, light = setOf("vault"), card = BoardCard("The clue", "Plain keys fine, one note refused, nothing running. Two roles, not one.")),
                ),
            ),
            Scene(
                22, "The other mistakes", refresh(),
                beats = listOf(
                    Beat(22, show = setOf("api", "store"), light = setOf("api"), card = BoardCard("Never calling refresh", "Nothing pushes a change to a running service. It has to look.")),
                    Beat(24, light = setOf("api", "store"), pulse = "api" to "store", card = BoardCard("Refresh", "Called every request, it looks only once thirty seconds have passed.")),
                    Beat(26, show = setOf("sentinel"), light = setOf("sentinel"), pulse = "store" to "sentinel", card = BoardCard("Sentinel key", "One key watched, not forty. Twelve becomes thirteen.")),
                    Beat(28, show = setOf("reload", "order"), light = setOf("reload"), pulse = "sentinel" to "reload", card = BoardCard("Fee first, sentinel last", "Then every copy reloads once and nobody reads half a change.")),
                ),
            ),
            Scene(
                30, "Flags and snapshots", flags(),
                beats = listOf(
                    Beat(30, show = setOf("flag", "filter"), light = setOf("flag", "filter"), pulse = "flag" to "filter", card = BoardCard("Feature flag with a filter", "Shipped hidden, then on for ten percent of diners.")),
                    Beat(32, show = setOf("snap", "release"), light = setOf("snap", "release"), pulse = "snap" to "release", card = BoardCard("Snapshot", "A named set that cannot change. Release 1.4 loads it by name.")),
                ),
            ),
            Scene(
                33, "Where it stops", limits(),
                beats = listOf(
                    Beat(34, light = setOf("secrets", "phone", "quiet"), card = BoardCard("Three edges", "No secrets, the phone asks the API, a quiet copy waits for a request.")),
                    Beat(36, light = setOf("kv"), pulse = "secrets" to "kv", card = BoardCard("Next door", "The Key Vault episode is the safe from the inside.")),
                ),
            ),
            Scene(
                38, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("ref", "refresh", "sentinel"), card = examCard),
                    Beat(40, light = setOf("reader", "flag", "snap")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-appconfig--gossip",
        listOf(
            Scene(
                0, "Off the settings file", board(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), card = BoardCard("What they did", "The surge fee and the search switch came off the settings files.")),
                    Beat(2, show = setOf("file"), light = setOf("file"), pulse = "api" to "file", card = BoardCard("A file per copy", "Six copies of the API, the Taste service, and all had to agree.")),
                    Beat(4, show = setOf("store"), light = setOf("store"), pulse = "api" to "store", card = BoardCard("One store", "Keys with values. Zaiqa SurgeFee is twenty, and nobody rebuilds.")),
                ),
            ),
            Scene(
                6, "Tom, Monday", vault(),
                beats = listOf(
                    Beat(6, show = setOf("store", "vault"), light = setOf("store"), card = BoardCard("Tom's plain value", "The payment secret typed onto the board. Security found it.")),
                    Beat(8, show = setOf("ref"), light = setOf("ref"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "A key whose value is the secret's address, not the secret.")),
                    Beat(10, show = setOf("api"), light = setOf("api"), pulse = "api" to "store", card = BoardCard("Dana's badge", "A managed identity with App Configuration Data Reader. Then lunch.")),
                    Beat(12, light = setOf("api", "vault"), pulse = "api" to "vault", card = BoardCard("The missing role", "Nobody granted Key Vault Secrets User on the vault.")),
                    Beat(14, light = setOf("ref", "vault"), card = BoardCard("Nothing starts", "A refused reference fails the whole load: no empty setting, no service.")),
                ),
            ),
            Scene(
                16, "Priya and Marcus", refresh(),
                beats = listOf(
                    Beat(16, show = setOf("api", "store"), light = setOf("store"), card = BoardCard("Watching forty keys", "Every key, every refresh, times six copies. Only expensive.")),
                    Beat(18, light = setOf("api", "store"), pulse = "api" to "store", card = BoardCard("Refresh", "Called every request, it looks only once thirty seconds have passed.")),
                    Beat(20, show = setOf("sentinel", "reload", "order"), light = setOf("sentinel"), pulse = "store" to "sentinel", card = BoardCard("Sentinel key", "One key watched. Values first, twelve to thirteen last.")),
                    Beat(22, light = setOf("reload"), pulse = "sentinel" to "reload", card = BoardCard("Marcus never called it", "He changed the fee and said the service was broken.")),
                ),
            ),
            Scene(
                24, "Labels", keys(),
                beats = listOf(
                    Beat(24, show = setOf("key", "dev", "prod"), light = setOf("dev", "prod"), card = BoardCard("Label", "A sticky tag, so one key has a value per place the app runs.")),
                    Beat(26, show = setOf("def"), light = setOf("def"), card = BoardCard("No label is the default", "Load the no-label keys first, then your label. The later one wins.")),
                ),
            ),
            Scene(
                28, "The intern's good week", flags(),
                beats = listOf(
                    Beat(28, show = setOf("flag"), light = setOf("flag"), card = BoardCard("Feature flag", "An on and off switch on the board. NewSearch shipped hidden.")),
                    Beat(30, show = setOf("filter"), light = setOf("filter"), pulse = "flag" to "filter", card = BoardCard("Filter", "Ten percent of diners, a time window, or named users and groups.")),
                    Beat(32, show = setOf("snap", "release"), light = setOf("snap", "release"), pulse = "snap" to "release", card = BoardCard("Snapshot", "A named set that cannot change. Release 1.4 loads it by name.")),
                ),
            ),
            Scene(
                33, "Where it stops", limits(),
                beats = listOf(
                    Beat(34, light = setOf("secrets", "phone", "quiet"), card = BoardCard("Three edges", "No secrets, the phone asks the API, a quiet copy waits for a request.")),
                ),
            ),
            Scene(
                36, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("ref", "refresh", "sentinel"), card = examCard),
                    Beat(38, light = setOf("reader", "flag", "snap")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-appconfig--late-night",
        listOf(
            Scene(
                0, "Two values tonight", board(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), card = BoardCard("Tonight", "The surge fee, and a switch for the new search. Neither needs a release.")),
                    Beat(4, show = setOf("file"), light = setOf("file"), pulse = "api" to "file", card = BoardCard("A file per copy", "Six copies of the API, the Taste service, the order worker.")),
                    Beat(6, show = setOf("store"), light = setOf("store"), pulse = "api" to "store", card = BoardCard("One store", "A noticeboard in the staff room that every service reads.")),
                ),
            ),
            Scene(
                8, "Keys and labels", keys(),
                beats = listOf(
                    Beat(8, show = setOf("key"), light = setOf("key"), card = BoardCard("Key and value", "Zaiqa SurgeFee is twenty. Everyone reads the same twenty.")),
                    Beat(10, show = setOf("dev", "prod"), light = setOf("dev", "prod"), card = BoardCard("Label", "A sticky tag, so one key has a value per place the app runs.")),
                    Beat(12, show = setOf("def"), light = setOf("def"), card = BoardCard("No label is the default", "Loaded first, then your own label on top. The later one wins.")),
                ),
            ),
            Scene(
                14, "The secret stays in the safe", vault(),
                beats = listOf(
                    Beat(14, show = setOf("store", "vault"), light = setOf("vault"), card = BoardCard("Not on the board", "Settings only. The payment key stays in Key Vault, the safe.")),
                    Beat(16, show = setOf("ref"), light = setOf("ref"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "A key whose value is the secret's address, not the secret.")),
                    Beat(18, show = setOf("api"), light = setOf("api"), pulse = "api" to "store", card = BoardCard("Two roles", "Data Reader on the board, Key Vault Secrets User on the safe.")),
                    Beat(20, light = setOf("api", "vault"), pulse = "api" to "vault", card = BoardCard("Nothing starts", "A refused reference fails the whole load: no empty setting, no service.")),
                    Beat(22, light = setOf("ref", "vault"), card = BoardCard("The pairing to remember", "Plain keys perfect, one note refused, the whole thing down.")),
                ),
            ),
            Scene(
                24, "Refresh and the sentinel", refresh(),
                beats = listOf(
                    Beat(24, show = setOf("api", "store"), light = setOf("api"), pulse = "api" to "store", card = BoardCard("Refresh", "Called every request, it looks only once thirty seconds have passed.")),
                    Beat(26, show = setOf("sentinel"), light = setOf("sentinel"), pulse = "store" to "sentinel", card = BoardCard("Sentinel key", "One key watched. It sits at twelve; you move it to thirteen.")),
                    Beat(28, show = setOf("reload", "order"), light = setOf("reload"), pulse = "sentinel" to "reload", card = BoardCard("Values first, guard last", "Then every copy reloads once and nobody reads half a change.")),
                ),
            ),
            Scene(
                30, "Flags and snapshots", flags(),
                beats = listOf(
                    Beat(30, show = setOf("flag"), light = setOf("flag"), card = BoardCard("Feature flag", "An on and off switch on the board. NewSearch shipped hidden.")),
                    Beat(32, show = setOf("filter"), light = setOf("filter"), pulse = "flag" to "filter", card = BoardCard("Filter", "Ten percent of diners, a time window, or named users and groups.")),
                    Beat(36, show = setOf("snap", "release"), light = setOf("snap", "release"), pulse = "snap" to "release", card = BoardCard("Snapshot", "A named set that cannot change. Release 1.4 loads it by name.")),
                ),
            ),
            Scene(
                37, "Where it stops", limits(),
                beats = listOf(
                    Beat(38, light = setOf("phone", "quiet", "kv"), card = BoardCard("The edges", "The phone asks the API. A quiet copy hears you on its next request.")),
                ),
            ),
            Scene(
                40, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("ref", "refresh", "sentinel"), card = examCard),
                    Beat(42, light = setOf("reader", "flag", "snap"), card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-appconfig--couples",
        listOf(
            Scene(
                0, "Date night settings", board(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), card = BoardCard("Tonight", "The surge fee, and a switch for the new search.")),
                    Beat(2, show = setOf("file"), light = setOf("file"), pulse = "api" to "file", card = BoardCard("A file per copy", "Copied into six copies of the API, and into the Taste service.")),
                    Beat(4, show = setOf("store"), light = setOf("store"), pulse = "api" to "store", card = BoardCard("One store", "App Configuration: every service reads it, keys with values.")),
                ),
            ),
            Scene(
                6, "One key, two places", keys(),
                beats = listOf(
                    Beat(6, show = setOf("key", "dev", "prod", "def"), light = setOf("dev", "prod"), card = BoardCard("Label", "A sticky tag per place the app runs. No label is the default.")),
                ),
            ),
            Scene(
                8, "The safe", vault(),
                beats = listOf(
                    Beat(8, show = setOf("store", "vault"), light = setOf("store"), card = BoardCard("Not on the board", "Settings only. The payment key stays in Key Vault, the safe.")),
                    Beat(9, show = setOf("ref"), light = setOf("ref"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "A key whose value is the secret's address, not the secret.")),
                    Beat(12, show = setOf("api"), light = setOf("api"), pulse = "api" to "store", card = BoardCard("Two roles", "Data Reader on the board, Key Vault Secrets User on the safe.")),
                    Beat(14, light = setOf("api", "vault"), pulse = "api" to "vault", card = BoardCard("Nothing starts", "A refused reference fails the whole load: no empty setting, no service.")),
                ),
            ),
            Scene(
                17, "Refresh and the sentinel", refresh(),
                beats = listOf(
                    Beat(17, show = setOf("api", "store"), light = setOf("api"), pulse = "api" to "store", card = BoardCard("Refresh", "Called every request, it looks only once thirty seconds have passed.")),
                    Beat(20, show = setOf("sentinel", "reload", "order"), light = setOf("sentinel"), pulse = "store" to "sentinel", card = BoardCard("Sentinel key", "One key watched. Values first, twelve to thirteen last.")),
                    Beat(22, light = setOf("reload"), pulse = "sentinel" to "reload", card = BoardCard("Configuration provider", "A library from Microsoft. Call load once, when the service starts.")),
                ),
            ),
            Scene(
                24, "Flags and snapshots", flags(),
                beats = listOf(
                    Beat(24, show = setOf("flag", "filter"), light = setOf("flag", "filter"), pulse = "flag" to "filter", card = BoardCard("Feature flag with a filter", "Shipped hidden, then on for a tenth of the diners.")),
                    Beat(27, show = setOf("snap", "release"), light = setOf("snap", "release"), pulse = "snap" to "release", card = BoardCard("Snapshot", "A named set that cannot change. Release 1.4 loads it by name.")),
                ),
            ),
            Scene(
                29, "Where it stops", limits(),
                beats = listOf(
                    Beat(30, light = setOf("phone", "quiet", "kv"), card = BoardCard("The edges", "The phone asks the API. A quiet copy hears you on its next request.")),
                ),
            ),
            Scene(
                32, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = setOf("ref", "refresh", "sentinel"), card = examCard),
                    Beat(34, light = setOf("reader", "flag", "snap"), card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
