package com.shahaabapps.core.content

/** The Security module's Microsoft Graph and App Configuration topics as Case Files and Gossip, with their boards (Az204StyledPodcasts). */
internal object Az204StyledSecurity3 {
    private val graphCase = Podcast(
        "az-graph",
        listOf(
            maya("Welcome to Case Files. Today's case: The Hundred Club. A small gym chain runs an Android app called Liftlog. Its backend, on Azure App Service, shows members every trainer on staff, with a photo. The staff list comes from the company's Microsoft 365. On Monday the gym hired its hundred and first trainer. And she isn't in the app. Nor is anyone hired after her."),
            leo("[chuckle] A vanishing trainer. Detective Leo reporting. Before I start accusing people, where does the list come from, exactly?"),
            maya("From Microsoft Graph. Think of it as one front door to Microsoft 365. Users, email, calendars, files and Teams, all behind one web address. Your app knocks on one door and asks, with ordinary web requests, like GET to read something. That's what a REST API means."),
            leo("And under the picture? Is Graph one giant database that holds all of that?"),
            maya("No, and that's worth knowing. Graph is a gateway. Behind it sit the real services: the directory in Microsoft Entra ID for users, Exchange for mail, OneDrive and SharePoint for files. Graph checks your request and passes it to the right one, so you learn one address instead of five."),
            leo("Fine. Suspect one. The backend is showing Graph the wrong token."),
            maya("[pause] Good thought, wrong suspect. Every request carries an access token, a ticket with the venue printed on it. That venue is called the audience, and for Graph it must say Graph. A ticket made for the gym's own API gets turned away at the door. But turned away means nothing comes back. Here a hundred trainers came back."),
            leo("Suspect two, then. Someone used the beta endpoint. I hear beta things are flaky."),
            maya("They did use beta, I checked the file. That's a real mistake. Beta is the preview version, and it can change under you, so production uses version one point oh. But beta doesn't cut a list at a round hundred. Look at the number again. Exactly one hundred."),
            leo("A round number. [pause] Oh. Like a page. One page of a phone book."),
            maya("That's the clue. Big results come in pages, and for users a page holds a hundred by default. Each page that has more carries a next link, the full web address of the following page. The backend read page one, ignored the next link, and reported partial results. That's the culprit."),
            leo("How does the next link actually work? Is Graph keeping my place somewhere?"),
            maya("Near enough. The next link has a marker built into it, saying where the last page stopped. You call it exactly as it is, and you get the next page, with a new link. Keep going until a page arrives without one. That's how you know you've reached the end."),
            leo("So the fix is a loop that follows the link. Can the code libraries do that for me?"),
            maya("Yes. The Graph SDKs, ready-made code libraries, have a page helper that follows the links for you. The backend uses the SDK with DefaultAzureCredential, so it reaches Graph with its managed identity and no password in the code. The Key Vault episode covers that badge."),
            leo("Trainers restored. Case closed?"),
            maya("Second witness. The app also shows a little badge with the number of trainers. That count was failing too. The developer asked Graph to count the users, and got an error back."),
            leo("Let me guess. Something about a header."),
            maya("[laugh] You're learning. Count, search and some filters on users and groups need one extra header, ConsistencyLevel: eventual. A header is a small note sent along with the request. Without it, those directory queries fail. It's a favourite exam detail."),
            leo("While we're tidying, the list was slow too. Every trainer's whole profile came back. Can I ask for less?"),
            maya("With OData query parameters, little add-ons at the end of a request that shape the answer. Select picks the fields, so just the name and job title. Filter is a where clause, say only the trainers department."),
            leo("And the order on screen? Members want the trainers alphabetically."),
            maya("Orderby sorts, and top sets the page size. Expand pulls in something related, like a group's members."),
            leo("Shape first, then page through. Got it. Any more clues in the file?"),
            maya("One more, from Tuesday. The app now shows each trainer's next free slot, from their calendar. That's a hundred and forty calls. The developer sent them one by one, and Graph answered with a 429."),
            leo("Which means slow down?"),
            maya("Exactly. You're being throttled. The answer carries a Retry-After header saying how long to wait, so honour it. The SDKs retry for you. And to cut the round trips, use a batch: one envelope holding many requests, each with its own id and its own answer. But one batch carries at most twenty."),
            leo("So a hundred and forty calls is seven batches. And thirty would be two, not one."),
            maya("Two. A batch with more than twenty is rejected. And a batch saves round trips, not work. Graph still counts every request inside it, so a big enough burst can still be throttled."),
            leo("Where does Graph stop being the answer? Could the gym keep its own bookings there?"),
            maya("That's the edge. Graph is for Microsoft 365 and Entra data: people, mail, calendars, files. The gym's bookings live in its own database, like Cosmos DB. And creating Azure resources, like a storage account, is Azure Resource Manager, not Graph. Those are classic distractors."),
            leo("And the Android app itself? Could the phone call Graph directly?"),
            maya("It can, for the signed-in user's own data, like their own calendar. The phone signs the user in and gets a token whose audience is Graph, with scopes like Calendars Read. The MSAL episode covers that side. To find the scope a call needs, use Graph Explorer, a web page where you try calls and read the permissions tab."),
            leo("Then give me the case summary for the exam."),
            maya("Four rules. If a call returns only part of a list, follow the next link. If you need many calls in few round trips, batch them, twenty per batch. If the query uses count or search, add ConsistencyLevel: eventual. If you get a 429, wait for Retry-After. And the token's audience is Graph, on version one point oh, never beta."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case in Graph Explorer, no setup needed. Get yourself, then your top three messages, selecting only the subject. Then list users with a count, adding the ConsistencyLevel header. And look at the permissions tab for each call."),
            leo("A crime with no victims. My favourite kind. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val graphGossip = Podcast(
        "az-graph",
        listOf(
            maya("Okay, did you hear what Sam did on the habit tracker team? Grab a tea. This week was all about Microsoft Graph, and four different people made the news."),
            leo("[laugh] I heard there's a whiteboard with names on it. Start with Sam. What did Sam do?"),
            maya("Sam's building a small habit tracker for Android. Its backend on App Service puts your habits into your Outlook calendar, so the reminders show up next to your meetings. To reach the calendar, it calls Microsoft Graph."),
            leo("Which is what, in plain words?"),
            maya("One front door to Microsoft 365. Users, mail, calendars, files and Teams, all behind one web address. It's a REST API, so you ask with ordinary web requests, like GET to read something. Under the picture, Graph is a gateway: it checks your request and hands it to the service that really holds the data, like Exchange for mail, or Entra ID for users."),
            leo("So what went wrong for Sam?"),
            maya("Sam already had an access token, the one the app uses for its own API. So he sent that same token to Graph. And Graph turned him away, every single call."),
            leo("Why? A token's a token."),
            maya("That's what Sam said. Priya explained it at lunch. A token is a ticket with the venue printed on it, called the audience. One token, one audience. A ticket for the habit tracker's API says habit tracker. Graph only accepts tickets that say Graph. So the app needs a second token, made for Graph."),
            leo("[chuckle] Poor Sam, at the wrong concert. How does the app get the right ticket?"),
            maya("It asks the Microsoft identity platform for a token with Graph as the audience, and the scopes it needs, like Calendars Read. On the phone, MSAL does that, and the MSAL episode covers it. Priya also showed Sam Graph Explorer, a web page to try calls, where the permissions tab tells you which scope each call needs."),
            leo("Okay. Who's next on the whiteboard?"),
            maya("Aisha. Aisha found a lovely new property on the beta endpoint and shipped it to production. Three weeks later it changed shape, and her app's settings screen went blank."),
            leo("Ouch. What's beta, exactly?"),
            maya("The preview version of Graph. It's where new features appear first, so it's tempting. But things there can change or vanish without warning. Production apps use version one point oh, which stays stable. Aisha moved back to one point oh the same afternoon, and now she tries beta only in test apps."),
            leo("Reasonable. And the third name?"),
            maya("Ben. Ben runs the office's staff directory app. His manager asked how many people work here, and Ben's report said exactly one hundred. There are two hundred and twelve."),
            leo("[pause] Exactly one hundred. That sounds like a page, not a headcount."),
            maya("You'd have caught it. Big results come in pages. Each page that has more carries a next link, the full web address of the following page, with a marker inside saying where the last page stopped. Ben's code read page one and stopped, and reported partial results to his manager as the whole truth. Follow the next link until a page arrives without one."),
            leo("Can't I just make the page bigger instead?"),
            maya("Top asks for a bigger page, but it's a hint, not a promise. Graph may still page. Following the next link is the only reliable way through. And the Graph SDKs, which are ready-made code libraries, have a helper that follows the links for you."),
            leo("Fine. And the last one? I heard it was the intern."),
            maya("Bless them. The intern added a little total to Ben's directory, using count, and a search box, using search. Both failed with an error nobody understood."),
            leo("Let me guess. A missing header."),
            maya("Exactly. Count, search and some filters on users and groups need a header, ConsistencyLevel: eventual. A header is a small note sent along with the request. Priya added it, and both lit up. It's a favourite exam detail, so the intern will never forget it."),
            leo("While we're on the directory, it was slow, wasn't it?"),
            maya("Very. Ben fetched every field of every person. Priya showed him OData query parameters, little add-ons at the end of a request. Select picks fields, like name and email."),
            leo("So the directory only asks for what's on screen. Can it narrow down the people too?"),
            maya("Filter is a where clause, like one department. Orderby sorts. Expand pulls in something related, like a group's members. The answer shrank to a fraction."),
            leo("Any drama with batches?"),
            maya("A small one. Dana needed thirty calls for a dashboard, so she put all thirty in one batch. A batch is one envelope with many requests inside, each with its own id and its own answer. But one batch carries at most twenty, so hers was rejected. Two batches fixed it."),
            leo("And if Graph decides we're asking too much?"),
            maya("You get a 429, which means slow down, you're being throttled. Honour the Retry-After header it sends. The SDKs retry for you. And a batch saves round trips, not work, because Graph still counts each request inside it."),
            leo("Is there anything Graph can't do? The intern wanted it to create a storage account."),
            maya("[laugh] Not Graph. Graph is for Microsoft 365 and Entra data: people, mail, calendars, files. Creating Azure resources is Azure Resource Manager. And Sam's own habit data lives in his own database, not in Graph."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. If a call returns only part of a list, follow the next link. Many calls in few round trips means a batch, twenty per batch, so thirty is two. Count or search means ConsistencyLevel: eventual. And a 429 means wait for Retry-After. The token's audience is always Graph, on version one point oh."),
            leo("And something to try today, so I'm not on the whiteboard?"),
            maya("Open Graph Explorer, no setup needed. Get yourself, then your top three messages, selecting only the subject. Then list users with a count, adding the ConsistencyLevel header. And look at the permissions tab for each call."),
            leo("One header, two batches, no whiteboard. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    private val appConfigCase = Podcast(
        "az-appconfig",
        listOf(
            maya("Welcome to Case Files. Today's case: The Price That Wouldn't Move. A small bike rental runs an Android app called Spokes. Riders unlock bikes with their phones, and a backend on Azure App Service works out the price. On Friday evening, the owner raised the hourly price for the weekend. On Saturday morning, the app was still charging the old price."),
            leo("[chuckle] Cheap bikes for everyone. Detective Leo on the case. Where did the owner change the price?"),
            maya("In Azure App Configuration. Think of it as a shared notice board for settings. Limits, prices, web addresses, on-off switches. All the apps read the one board. Change a value once, and every copy of every app picks it up. Each setting is a key, which is its name, and a value."),
            leo("And under the picture? What is it really?"),
            maya("A key-value store, run by Azure, that your apps read over HTTPS. Each entry is a key and a value, with an optional label, and a content type that says how to read the value, like JSON. A library in the app, the provider, loads the keys at start-up into the app's ordinary settings, so the code reads them like any other setting."),
            leo("Then suspect one is easy. The app can't reach the board. Wrong permissions."),
            maya("[pause] Good instinct, wrong suspect. The backend reads the store with its managed identity, which holds the App Configuration Data Reader role. If that were broken, it couldn't have loaded anything at start-up. It loaded the old price perfectly. Reading works. Noticing a change doesn't."),
            leo("Suspect two. They only told it to watch one key. It should watch every key, to be safe."),
            maya("That's the mistake the topic warns about, and it wouldn't help here anyway. Watching every key means one request per key, on every check. Hundreds. The right pattern is a sentinel key, a guard on watch. The app watches just that one, and when it changes, it reloads every other key in one go."),
            leo("So did the owner forget to bump the sentinel?"),
            maya("No, I checked. Price changed, sentinel bumped from six to seven, refresh interval thirty seconds. Everything in the store was right. So the clue is in the app. [pause] Leo, who does the checking, after those thirty seconds?"),
            leo("A timer? Something ticking away in the background?"),
            maya("That's what the developer believed too. But there's no timer. The checking is done by the middleware, a small piece of code that runs on each incoming request. After the interval, the next request asks the store whether the sentinel changed. If it did, every key reloads."),
            leo("And the Spokes backend didn't have that middleware. So it never asked."),
            maya("That's the culprit. The developer registered the sentinel and set the interval, but never added the App Configuration middleware. Without it, the refresh never triggers. One line in the start-up code, and the new price arrived on the next unlock."),
            leo("So it's not instant, even then?"),
            maya("No, and that's the edge of the idea. A change arrives after the interval, on the next request. Fine for a price. If something must change in the same second, it doesn't belong in a setting. And the app code itself doesn't change. It reads the values just as it always did."),
            leo("Fine. Case closed?"),
            maya("Two more witnesses. While checking the store, I found the card payment key sitting there as a plain value."),
            leo("[sigh] On the shared notice board."),
            maya("Exactly. The board is for settings that are not secrets. A secret goes in Key Vault, and the store holds a Key Vault reference, a pointer to it. The app follows the pointer with its own identity, and the secret stays in the vault. The Key Vault episode covers that side."),
            leo("And the second witness?"),
            maya("The test backend was charging live prices. Same key, but testers want cheap rides. That's what a label is for, a variant of one key, usually per environment. The dev label says one euro an hour, prod says four. Each app loads its own label with a label filter. The test app simply had no filter."),
            leo("And the Android app itself? Could it read the board directly?"),
            maya("Not with a connection string inside it. Anything in a phone app can be taken apart. The phone asks the backend, and the backend reads the store with its identity. And the developer had also left a connection string in the backend's settings. With a managed identity already there, grant Data Reader instead."),
            leo("What about the new e-bikes? I heard they launched this week."),
            maya("With a feature flag, an on-off switch for one part of the app, flipped from the board with no new release. And a feature filter decides the flag's answer while the app runs. They showed e-bikes to ten percent of riders first. That's a percentage filter."),
            leo("And the summer weekend discount? Or letting the staff try it first?"),
            maya("A weekend is a time window filter. Staff first is targeting, with a group. You can write custom filters too. In code, the feature manager answers whether a flag is enabled. And before launch they took a snapshot, a frozen, named set of keys, exactly as they were for that release."),
            leo("So, the case summary for the exam?"),
            maya("Four rules. If settings must update without a restart, watch one sentinel key, and add the middleware. If one key needs different values per environment, use a label. If a value is a secret, store a Key Vault reference. And if a feature goes to a share of users, that's a percentage filter, not a label and not a sentinel."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case. Create a free store, with two labelled values for one key. Add a feature flag, and toggle it. In a console app, load with Select and a label filter, and print the value. Then change the value in the portal, and add a sentinel refresh. The free tier is enough."),
            leo("A price that moves on cue. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val appConfigGossip = Podcast(
        "az-appconfig",
        listOf(
            maya("Okay, did you hear what Dana did with the quiz app? Pull up a chair. This week the whole office learned about Azure App Configuration, mostly the hard way."),
            leo("[laugh] I heard the word sentinel shouted across the kitchen. Start with Dana."),
            maya("Dana's team runs a small quiz app for Android, with a backend on App Service. They moved all its settings into App Configuration. Question time limits, the daily quiz count, even the colour of the buttons. The idea was good. Before, each copy of the backend had its own settings file, and the copies kept drifting apart."),
            leo("Which is what, in plain words?"),
            maya("A shared notice board for settings. Every app reads the one board, so you change a value once and every copy picks it up. Under the picture, it's a key-value store run by Azure. Each setting is a key, its name, and a value, with an optional label and content type. A library in your app loads them into its ordinary settings at start-up."),
            leo("Sounds sensible. So what did Dana do?"),
            maya("Dana put everything on the board. Including the password for the scoring database, as a plain value."),
            leo("[sigh] On the board everyone reads?"),
            maya("That's what Priya said, gently. The board is for settings that aren't secrets. A secret goes in Key Vault, and the board holds a Key Vault reference, a pointer to it. The app follows the pointer with its own identity, and the password never leaves the vault. The Key Vault episode tells that half."),
            leo("Okay. Who's next?"),
            maya("Ben. Ben wanted changes to arrive without restarting the app. Good goal. So he registered every one of the two hundred keys for refresh."),
            leo("Is that bad? It sounds thorough."),
            maya("Every check became a request per key. They were on the Free tier, one store and a thousand requests a day, and Ben used the day up before lunch. Production stores belong on Standard anyway. Priya showed him the sentinel key, a guard on watch. The app watches one key. When you've finished changing settings, you bump it, and every other key reloads in one go."),
            leo("One request instead of two hundred. And the third?"),
            maya("The intern. The intern registered the sentinel perfectly, set the refresh interval, bumped the key, and waited. And waited. Nothing changed, all afternoon."),
            leo("[pause] Because something has to do the checking."),
            maya("Right. There's no background timer. The middleware does it, a small piece of code that runs on each incoming request. After the interval, the next request asks the store whether the sentinel moved, and if it did, everything reloads. The intern had never added the middleware, so the refresh never triggered."),
            leo("And once it was added?"),
            maya("The new value arrived on the next request after the interval. Not instantly, and that's the edge of it. Something that must change in the same second isn't a setting. And the app code didn't change at all. It reads values as it always did. The intern now has a sticky note on the monitor that just says, middleware."),
            leo("Who else? I heard Aisha's name."),
            maya("Aisha connected the backend to the store with a connection string, pasted into its settings. But the backend already had a managed identity, the badge Azure gives your app. Priya removed the string and gave the identity the App Configuration Data Reader role. Nothing left to leak."),
            leo("And how does the Android app read the store?"),
            maya("It doesn't, directly. A connection string in a phone app can be pulled out by anyone who downloads it. The phone asks the backend, and the backend reads the store with its identity. If the phone needs a setting, like the question time limit, the backend simply sends it along with the quiz."),
            leo("Any happy stories this week?"),
            maya("Sam's. Sam needed the test app to allow fifty quizzes a day, while the live app keeps fifteen. Same key, two labels. A label is a variant of one key, usually per environment, like dev and prod."),
            leo("How does each app know which label is its own?"),
            maya("It loads its own label with a label filter. And for a value with structure, like a list of quiz topics, the content type says to read it as JSON."),
            leo("Nice. And the new quiz mode everyone's talking about?"),
            maya("That's a feature flag, an on-off switch for part of the app, flipped from the board with no new release. Nora wanted it on for a fifth of players, and first tried a label. But a label is per environment, not a rollout. The answer was a feature filter, a percentage filter, which decides the flag while the app runs."),
            leo("What other filters are there?"),
            maya("A time window, between two times, like a weekend tournament. Targeting, for named users and groups, like the beta testers. Or a custom one. In code, the feature manager says whether a flag is enabled. And before the release, Sam took a snapshot, a frozen, named set of keys that can't change afterwards."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. Settings that must update without a restart mean one sentinel key and the middleware. One key with different values per environment means a label. A secret means a Key Vault reference, never a plain value. And a feature for a share of users means a percentage filter. The app reads the store with its identity and Data Reader."),
            leo("And something to try today, so I'm not the next story?"),
            maya("The free tier is enough. Create a free store, with two labelled values for one key. Add a feature flag, and toggle it. In a console app, load with Select and a label filter, and print the value. Then change the value in the portal, and add a sentinel refresh."),
            leo("One sentinel, and nobody shouting. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    val podcasts: List<Podcast> = listOf(graphCase, graphGossip, appConfigCase, appConfigGossip)

    private fun graphCaseBoard() = PodcastBoard(
        "az-graph--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Hundred Club..."
            Scene(
                0, "The Hundred Club",
                DiagramSpec(
                    "The gym's Android app asks its backend for the trainers, and the backend asks Microsoft Graph, which passes the request on.",
                    nodes = listOf(
                        DNode("phone", "Liftlog app", 0.22f, 0.15f, w = 0.36f, h = 0.16f),
                        DNode("missing", "Trainer 101?", 0.74f, 0.15f, w = 0.4f, h = 0.16f, kind = NodeKind.NOTE),
                        DNode("app", "Backend", 0.22f, 0.5f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("graph", "Microsoft Graph", 0.72f, 0.5f, w = 0.44f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("svc", "Entra ID, Exchange, OneDrive", 0.5f, 0.88f, w = 0.8f, h = 0.16f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "trainers"),
                        DEdge("app", "graph", "GET"),
                        DEdge("graph", "svc", "passes on"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "missing"), light = setOf("missing"), pulse = "phone" to "app", card = BoardCard("The case", "Trainer 101 was hired on Monday. She isn't in the app.")),
                    Beat(2, show = setOf("graph"), light = setOf("graph"), pulse = "app" to "graph", card = BoardCard("Microsoft Graph", "One REST API, one web address, in front of Microsoft 365.")),
                    Beat(4, show = setOf("svc"), light = setOf("svc"), pulse = "graph" to "svc", card = BoardCard("Under the picture", "A gateway: it checks the request and passes it to the real service.")),
                ),
            ),
            // 5: "Fine. Suspect one. The backend is showing Graph the wrong token."
            Scene(
                5, "The suspects",
                DiagramSpec(
                    "A wrong token gets nothing back and beta changes shape; a list cut at exactly one hundred points somewhere else.",
                    nodes = listOf(
                        DNode("tok", "Wrong token", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "nothing comes back"),
                        DNode("beta", "Beta endpoint", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "changes, not cuts"),
                        DNode("round", "Exactly 100", 0.5f, 0.74f, w = 0.5f, h = 0.22f, sub = "a round number"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("tok"), light = setOf("tok")),
                    Beat(6, card = BoardCard("Suspect one: cleared", "A wrong audience is turned away. Here a hundred came back.")),
                    Beat(7, show = setOf("beta"), light = setOf("beta")),
                    Beat(8, show = setOf("round"), light = setOf("round"), card = BoardCard("Suspect two: a real slip", "Production uses version one point oh. But beta doesn't stop at 100.")),
                ),
            ),
            // 9: "A round number. Oh. Like a page..."
            Scene(
                9, "Page after page",
                DiagramSpec(
                    "Big results come in pages; follow each page's next link until a page has none, or let the SDK follow them.",
                    nodes = listOf(
                        DNode("p1", "Page 1", 0.2f, 0.3f, w = 0.3f, h = 0.18f, sub = "100 users"),
                        DNode("p2", "Page 2", 0.8f, 0.3f, w = 0.3f, h = 0.18f),
                        DNode("p3", "Last page", 0.8f, 0.75f, w = 0.3f, h = 0.18f, sub = "no next link"),
                        DNode("sdk", "Graph SDK", 0.22f, 0.75f, w = 0.34f, h = 0.2f, tone = 1, sub = "page helper"),
                    ),
                    edges = listOf(
                        DEdge("p1", "p2", "next link"),
                        DEdge("p2", "p3", "next link"),
                        DEdge("sdk", "p1", "follows"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("p1"), light = setOf("p1"), card = BoardCard("Exactly one hundred", "One page of users. The backend never asked for page two.")),
                    Beat(10, show = setOf("p2", "p3"), light = setOf("p1", "p2"), pulse = "p1" to "p2", card = BoardCard("Next link", "The full web address of the following page. Partial results otherwise.")),
                    Beat(12, light = setOf("p3"), pulse = "p2" to "p3", card = BoardCard("The end of the list", "A page arrives without a next link. Only then stop.")),
                    Beat(14, show = setOf("sdk"), light = setOf("sdk"), pulse = "sdk" to "p1", card = BoardCard("Graph SDK", "A page helper follows the links; DefaultAzureCredential, no password.")),
                ),
            ),
            // 15: "Trainers restored. Case closed?"
            Scene(
                15, "One extra header",
                DiagramSpec(
                    "Count, search and some filters on users and groups only work with the ConsistencyLevel eventual header.",
                    nodes = listOf(
                        DNode("query", "Count the trainers", 0.24f, 0.3f, w = 0.4f, h = 0.22f),
                        DNode("header", "ConsistencyLevel: eventual", 0.4f, 0.8f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "a note sent along"),
                        DNode("graph", "Microsoft Graph", 0.8f, 0.3f, w = 0.36f, h = 0.22f, kind = NodeKind.CLOUD),
                    ),
                    edges = listOf(
                        DEdge("query", "graph"),
                        DEdge("header", "query", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("query", "graph"), light = setOf("query"), card = BoardCard("The second witness", "The trainer count came back with an error.")),
                    Beat(18, show = setOf("header"), light = setOf("header"), pulse = "query" to "graph", card = BoardCard("Count or search?", "Add the header ConsistencyLevel: eventual, or the query fails.")),
                ),
            ),
            // 19: "While we're tidying, the list was slow too..."
            Scene(
                19, "Ask for less",
                DiagramSpec(
                    "OData query parameters at the end of a request shape the answer before it comes back.",
                    nodes = listOf(
                        DNode("req", "GET the trainers", 0.5f, 0.13f, w = 0.6f, h = 0.18f),
                        DNode("select", "select", 0.24f, 0.45f, w = 0.4f, kind = NodeKind.PILL, sub = "which fields"),
                        DNode("filter", "filter", 0.76f, 0.45f, w = 0.4f, kind = NodeKind.PILL, sub = "a where clause"),
                        DNode("orderby", "orderby", 0.24f, 0.72f, w = 0.4f, kind = NodeKind.PILL, tone = 1, sub = "sort"),
                        DNode("top", "top", 0.76f, 0.72f, w = 0.4f, kind = NodeKind.PILL, tone = 1, sub = "page size"),
                        DNode("more", "expand, count, search", 0.5f, 0.93f, w = 0.66f, h = 0.12f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("req", "select"),
                        DEdge("req", "filter"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(19, show = setOf("req")),
                    Beat(20, show = setOf("select", "filter", "orderby", "top", "more"), light = setOf("select", "filter"), pulse = "req" to "select", card = BoardCard("OData query parameters", "Select fields, filter rows, orderby, top. Expand adds related things.")),
                ),
            ),
            // 23: "Shape first, then page through. Got it. Any more clues in the file?"
            Scene(
                23, "Too many calls",
                DiagramSpec(
                    "Calls sent one by one get a 429 with Retry-After; a batch carries up to twenty requests in one call.",
                    nodes = listOf(
                        DNode("app", "Backend", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("graph", "Microsoft Graph", 0.8f, 0.3f, w = 0.36f, h = 0.22f, kind = NodeKind.CLOUD),
                        DNode("batch", "One batch", 0.4f, 0.75f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "up to 20 inside"),
                        DNode("err", "429", 0.82f, 0.75f, w = 0.32f, h = 0.18f, kind = NodeKind.NOTE, sub = "Retry-After"),
                    ),
                    edges = listOf(
                        DEdge("app", "graph", "140 calls"),
                        DEdge("graph", "err", dashed = true),
                        DEdge("app", "batch", "packs"),
                        DEdge("batch", "graph", "one call"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("app", "graph"), light = setOf("app"), pulse = "app" to "graph", card = BoardCard("140 calls, one by one", "A next free slot for every trainer, from their calendar.")),
                    Beat(25, show = setOf("err"), light = setOf("err"), pulse = "graph" to "err", card = BoardCard("429: slow down", "You're throttled. Honour Retry-After; the SDKs retry for you.")),
                    Beat(26, show = setOf("batch"), light = setOf("batch"), pulse = "batch" to "graph", card = BoardCard("Batch", "One envelope, many requests, each with its own id. At most twenty.")),
                    Beat(27, card = BoardCard("140 calls is seven batches", "And thirty is two. More than twenty in one batch is rejected.")),
                    Beat(28, light = setOf("graph"), card = BoardCard("Round trips, not work", "Graph still counts every request inside, so a burst can be throttled.")),
                ),
            ),
            // 29: "Where does Graph stop being the answer?..."
            Scene(
                29, "Where Graph stops",
                DiagramSpec(
                    "Graph serves Microsoft 365 and Entra data; your own data is your database, and Azure resources are Resource Manager.",
                    nodes = listOf(
                        DNode("graph", "Microsoft Graph", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "365 and Entra data"),
                        DNode("db", "Cosmos DB", 0.78f, 0.22f, w = 0.34f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1),
                        DNode("phone", "Android app", 0.26f, 0.72f, w = 0.4f, h = 0.2f, sub = "MSAL, Graph token"),
                        DNode("arm", "Resource Manager", 0.78f, 0.72f, w = 0.4f, h = 0.2f, tone = 2, sub = "creates resources"),
                    ),
                    edges = listOf(
                        DEdge("phone", "graph", "own calendar"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("graph", "db", "arm"), light = setOf("db", "arm"), card = BoardCard("Not Graph", "Own bookings: your database. A storage account: Resource Manager.")),
                    Beat(32, show = setOf("phone"), light = setOf("phone", "graph"), pulse = "phone" to "graph", card = BoardCard("The phone side", "Signs in with MSAL; Graph Explorer shows the scope each call needs.")),
                ),
            ),
            // 33: "Then give me the case summary for the exam."
            Scene(
                33, "Case closed",
                DiagramSpec(
                    "The exam's Graph questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("page", "Part of a list", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "next link"),
                        DNode("batch", "Many calls", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "batch, 20 each"),
                        DNode("cons", "Count or search", 0.5f, 0.45f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, sub = "ConsistencyLevel: eventual"),
                        DNode("retry", "A 429", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "wait for Retry-After"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("page", "batch", "cons", "retry"), light = setOf("page"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Audience Graph, version one point oh.")),
                    Beat(36, card = BoardCard("Today", "Graph Explorer: me, three messages, users with a count, permissions.")),
                ),
            ),
        ),
    )

    private fun graphGossipBoard() = PodcastBoard(
        "az-graph--gossip",
        listOf(
            // 0: "Okay, did you hear what Sam did on the habit tracker team?..."
            Scene(
                0, "Sam's habit tracker",
                DiagramSpec(
                    "Sam's backend puts habits in the user's calendar through Microsoft Graph, a gateway to the services behind it.",
                    nodes = listOf(
                        DNode("phone", "Habit tracker", 0.22f, 0.2f, w = 0.38f, h = 0.18f),
                        DNode("graph", "Microsoft Graph", 0.74f, 0.2f, w = 0.44f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("cal", "Outlook calendar", 0.76f, 0.66f, w = 0.38f, h = 0.2f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("phone", "app"),
                        DEdge("app", "graph", "GET"),
                        DEdge("graph", "cal", "passes on"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app"), pulse = "phone" to "app"),
                    Beat(2, show = setOf("graph", "cal"), light = setOf("cal"), pulse = "app" to "graph", card = BoardCard("Habits in the calendar", "Reminders next to your meetings, through Microsoft Graph.")),
                    Beat(4, light = setOf("graph"), pulse = "graph" to "cal", card = BoardCard("Under the picture", "A gateway: it hands each request to the service that holds the data.")),
                ),
            ),
            // 5: "So what went wrong for Sam?"
            Scene(
                5, "One token, one audience",
                DiagramSpec(
                    "Each access token has one audience; Graph only accepts a token made for Graph.",
                    nodes = listOf(
                        DNode("app", "Backend", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("token", "Access token", 0.5f, 0.72f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "audience: Graph"),
                        DNode("graph", "Microsoft Graph", 0.8f, 0.3f, w = 0.36f, h = 0.22f, kind = NodeKind.CLOUD),
                        DNode("own", "Habit API token", 0.8f, 0.92f, w = 0.36f, h = 0.14f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("app", "token", "sends"),
                        DEdge("token", "graph", "accepted"),
                        DEdge("own", "graph", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("app", "graph", "own"), light = setOf("own"), pulse = "own" to "graph", card = BoardCard("Sam's token", "Made for the habit tracker's own API. Graph turned every call away.")),
                    Beat(8, show = setOf("token"), light = setOf("token"), pulse = "token" to "graph", card = BoardCard("One token, one audience", "Graph only accepts a ticket that says Graph. Get a second token.")),
                    Beat(10, light = setOf("app"), card = BoardCard("Graph Explorer", "Try calls in the browser; the permissions tab shows each scope.")),
                ),
            ),
            // 11: "Okay. Who's next on the whiteboard?"
            Scene(
                11, "Aisha's beta",
                DiagramSpec(
                    "The beta endpoint is a preview that can change; production apps call version one point oh.",
                    nodes = listOf(
                        DNode("beta", "Beta endpoint", 0.26f, 0.3f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "can change"),
                        DNode("v1", "Version 1.0", 0.76f, 0.3f, w = 0.4f, h = 0.2f, tone = 1, sub = "stable"),
                        DNode("app", "Aisha's app", 0.5f, 0.78f, w = 0.4f, h = 0.2f),
                    ),
                    edges = listOf(
                        DEdge("app", "beta", dashed = true),
                        DEdge("app", "v1", "production"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("beta", "app"), light = setOf("beta"), pulse = "app" to "beta", card = BoardCard("Shipped on beta", "Three weeks later the property changed shape. A blank screen.")),
                    Beat(14, show = setOf("v1"), light = setOf("v1"), pulse = "app" to "v1", card = BoardCard("Production: one point oh", "Beta only in test apps.")),
                ),
            ),
            // 15: "Reasonable. And the third name?"
            Scene(
                15, "Ben's hundred",
                DiagramSpec(
                    "Big results come in pages; follow each page's next link until a page has none.",
                    nodes = listOf(
                        DNode("p1", "Page 1", 0.2f, 0.3f, w = 0.3f, h = 0.18f, sub = "100 people"),
                        DNode("p2", "Page 2", 0.8f, 0.3f, w = 0.3f, h = 0.18f),
                        DNode("p3", "Last page", 0.8f, 0.75f, w = 0.3f, h = 0.18f, sub = "no next link"),
                        DNode("sdk", "Graph SDK", 0.22f, 0.75f, w = 0.34f, h = 0.2f, tone = 1, sub = "follows links"),
                    ),
                    edges = listOf(
                        DEdge("p1", "p2", "next link"),
                        DEdge("p2", "p3", "next link"),
                        DEdge("sdk", "p1"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("p1"), light = setOf("p1"), card = BoardCard("Exactly one hundred", "Ben's report counted one page of staff. There are 212.")),
                    Beat(18, show = setOf("p2", "p3"), light = setOf("p2", "p3"), pulse = "p1" to "p2", card = BoardCard("Next link", "The full address of the next page. Follow it until there is none.")),
                    Beat(20, show = setOf("sdk"), light = setOf("sdk"), pulse = "sdk" to "p1", card = BoardCard("Top is a hint", "Graph may still page. The SDKs follow the links for you.")),
                ),
            ),
            // 21: "Fine. And the last one? I heard it was the intern."
            Scene(
                21, "The intern's count",
                DiagramSpec(
                    "Count, search and some filters on users and groups only work with the ConsistencyLevel eventual header.",
                    nodes = listOf(
                        DNode("query", "Count and search", 0.24f, 0.3f, w = 0.4f, h = 0.22f),
                        DNode("header", "ConsistencyLevel: eventual", 0.4f, 0.8f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "a note sent along"),
                        DNode("graph", "Microsoft Graph", 0.8f, 0.3f, w = 0.36f, h = 0.22f, kind = NodeKind.CLOUD),
                    ),
                    edges = listOf(
                        DEdge("query", "graph"),
                        DEdge("header", "query", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("query", "graph"), light = setOf("query"), card = BoardCard("The intern's total", "Count and search failed with an error nobody understood.")),
                    Beat(24, show = setOf("header"), light = setOf("header"), pulse = "query" to "graph", card = BoardCard("Count or search?", "Add the header ConsistencyLevel: eventual, or the query fails.")),
                    Beat(26, light = setOf("query"), card = BoardCard("OData query parameters", "Select, filter, orderby, expand: Ben's answer shrank to a fraction.")),
                ),
            ),
            // 29: "Any drama with batches?"
            Scene(
                29, "Dana's thirty",
                DiagramSpec(
                    "A batch carries up to twenty requests in one call; a 429 means wait for Retry-After.",
                    nodes = listOf(
                        DNode("app", "Dashboard", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("graph", "Microsoft Graph", 0.8f, 0.3f, w = 0.36f, h = 0.22f, kind = NodeKind.CLOUD),
                        DNode("batch", "One batch", 0.4f, 0.75f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "up to 20 inside"),
                        DNode("err", "429", 0.82f, 0.75f, w = 0.32f, h = 0.18f, kind = NodeKind.NOTE, sub = "Retry-After"),
                    ),
                    edges = listOf(
                        DEdge("app", "batch", "packs"),
                        DEdge("batch", "graph", "one call"),
                        DEdge("graph", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("app", "batch", "graph"), light = setOf("batch"), pulse = "batch" to "graph", card = BoardCard("Thirty in one batch", "Rejected: a batch carries at most twenty. Two batches fixed it.")),
                    Beat(32, show = setOf("err"), light = setOf("err"), pulse = "graph" to "err", card = BoardCard("429: slow down", "Honour Retry-After. A batch saves round trips, not work.")),
                    Beat(34, light = setOf("graph"), card = BoardCard("Not Graph", "Azure resources: Resource Manager. Your own data: your database.")),
                ),
            ),
            // 35: "So, the gossip I should remember for the exam?"
            Scene(
                35, "For the exam",
                DiagramSpec(
                    "The exam's Graph questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("page", "Part of a list", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "next link"),
                        DNode("batch", "Many calls", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "batch, 20 each"),
                        DNode("cons", "Count or search", 0.5f, 0.45f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, sub = "ConsistencyLevel: eventual"),
                        DNode("retry", "A 429", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "wait for Retry-After"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(36, show = setOf("page", "batch", "cons", "retry"), light = setOf("batch"), card = BoardCard("Listen for the phrase", "Thirty calls is two batches. Audience Graph, version one point oh.")),
                    Beat(38, card = BoardCard("Today", "Graph Explorer: me, three messages, users with a count, permissions.")),
                ),
            ),
        ),
    )

    private fun appConfigCaseBoard() = PodcastBoard(
        "az-appconfig--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Price That Wouldn't Move..."
            Scene(
                0, "The price that wouldn't move",
                DiagramSpec(
                    "The bike rental's backend reads its price from App Configuration, but a changed price never reached it.",
                    nodes = listOf(
                        DNode("phone", "Spokes app", 0.22f, 0.15f, w = 0.36f, h = 0.16f),
                        DNode("price", "Old price on Saturday", 0.72f, 0.15f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("app", "Backend", 0.22f, 0.58f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("store", "App Configuration", 0.74f, 0.6f, w = 0.42f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "unlock"),
                        DEdge("store", "app", "settings"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "price"), light = setOf("price"), pulse = "phone" to "app", card = BoardCard("The case", "Friday: the price went up. Saturday: still the old price.")),
                    Beat(2, show = setOf("store"), light = setOf("store"), pulse = "store" to "app", card = BoardCard("App Configuration", "A shared notice board for settings. Change once, every app sees it.")),
                    Beat(4, light = setOf("app", "store"), card = BoardCard("Under the picture", "A key-value store. The provider loads keys into the app's settings.")),
                ),
            ),
            // 5: "Then suspect one is easy. The app can't reach the board..."
            Scene(
                5, "The suspects",
                DiagramSpec(
                    "Reading works and the store is right; watching every key would only add requests. The answer is one sentinel key.",
                    nodes = listOf(
                        DNode("perm", "No permission", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "nothing would load"),
                        DNode("every", "Watch every key", 0.76f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "hundreds of requests"),
                        DNode("sent", "Sentinel key", 0.5f, 0.74f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "bumped 6 to 7"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("perm"), light = setOf("perm")),
                    Beat(6, card = BoardCard("Suspect one: cleared", "It loaded the old price with Data Reader. Reading works.")),
                    Beat(7, show = setOf("every"), light = setOf("every")),
                    Beat(8, show = setOf("sent"), light = setOf("sent"), card = BoardCard("Suspect two: a mistake", "Watch one sentinel key; it reloads every other key in one go.")),
                    Beat(10, card = BoardCard("The store is right", "Price changed, sentinel bumped, refresh interval thirty seconds.")),
                ),
            ),
            // 11: "A timer? Something ticking away in the background?"
            Scene(
                11, "Who does the checking",
                DiagramSpec(
                    "The middleware checks the sentinel on a request after the interval; when it changes, every other key reloads.",
                    nodes = listOf(
                        DNode("sent", "Sentinel key", 0.26f, 0.18f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "the guard"),
                        DNode("mw", "Middleware", 0.78f, 0.18f, w = 0.36f, h = 0.18f, sub = "on each request"),
                        DNode("others", "All other keys", 0.26f, 0.62f, w = 0.42f, h = 0.2f, tone = 2),
                        DNode("app", "Spokes backend", 0.78f, 0.62f, w = 0.36f, h = 0.2f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("mw", "sent", "checks"),
                        DEdge("others", "app", "reloaded"),
                        DEdge("mw", "app"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("sent", "app")),
                    Beat(12, show = setOf("mw"), light = setOf("mw"), pulse = "mw" to "sent", card = BoardCard("Middleware", "Runs on each request. After the interval, it checks the sentinel.")),
                    Beat(14, show = setOf("others"), light = setOf("others", "app"), pulse = "others" to "app", card = BoardCard("The culprit", "No middleware, no refresh. One line in start-up fixed it.")),
                    Beat(16, light = setOf("app"), card = BoardCard("Not instant", "On the next request after the interval. Code reads values as usual.")),
                ),
            ),
            // 17: "Fine. Case closed?"
            Scene(
                17, "No secrets on the board",
                DiagramSpec(
                    "Secrets stay in Key Vault; the store only holds a Key Vault reference that points to them.",
                    nodes = listOf(
                        DNode("store", "App Configuration", 0.26f, 0.25f, w = 0.44f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("ref", "Key Vault reference", 0.26f, 0.75f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a pointer"),
                        DNode("vault", "Key Vault", 0.8f, 0.75f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("store", "ref"),
                        DEdge("ref", "vault", "points to", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("store"), light = setOf("store"), card = BoardCard("A witness", "The card payment key, sitting on the board as a plain value.")),
                    Beat(20, show = setOf("ref", "vault"), light = setOf("ref"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "A pointer. The secret stays in the vault: the Key Vault episode.")),
                ),
            ),
            // 21: "And the second witness?"
            Scene(
                21, "One key, two labels",
                DiagramSpec(
                    "A label is a variant of one key per environment; each app loads its own label with a label filter.",
                    nodes = listOf(
                        DNode("key", "HourlyPrice", 0.5f, 0.15f, w = 0.4f),
                        DNode("dev", "label dev: 1", 0.24f, 0.48f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("prod", "label prod: 4", 0.76f, 0.48f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("devapp", "Test app", 0.24f, 0.8f, w = 0.3f, h = 0.14f, tone = 1),
                        DNode("liveapp", "Live app", 0.76f, 0.8f, w = 0.3f, h = 0.14f),
                    ),
                    edges = listOf(
                        DEdge("key", "dev"),
                        DEdge("key", "prod"),
                        DEdge("dev", "devapp"),
                        DEdge("prod", "liveapp"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(21, show = setOf("key")),
                    Beat(22, show = setOf("dev", "prod", "devapp", "liveapp"), light = setOf("dev", "devapp"), pulse = "dev" to "devapp", card = BoardCard("Label", "Same key, a value per environment. The test app had no label filter.")),
                ),
            ),
            // 23: "And the Android app itself? Could it read the board directly?"
            Scene(
                23, "The phone's way in",
                DiagramSpec(
                    "The phone asks the backend; the backend reads the store with its managed identity and the Data Reader role.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.24f, 0.22f, w = 0.4f, h = 0.2f),
                        DNode("cs", "Connection string?", 0.76f, 0.22f, w = 0.42f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                        DNode("app", "Backend", 0.24f, 0.74f, w = 0.32f, h = 0.2f, sub = "managed identity"),
                        DNode("store", "App Configuration", 0.76f, 0.74f, w = 0.42f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "asks"),
                        DEdge("app", "store", "Data Reader"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("phone", "cs"), light = setOf("cs"), card = BoardCard("In the phone?", "Anything inside a phone app can be taken apart.")),
                    Beat(24, show = setOf("app", "store"), light = setOf("app", "store"), pulse = "app" to "store", card = BoardCard("App Configuration Data Reader", "The backend's identity gets the role. No connection string anywhere.")),
                ),
            ),
            // 25: "What about the new e-bikes? I heard they launched this week."
            Scene(
                25, "E-bikes, a few at a time",
                DiagramSpec(
                    "A feature filter decides a flag's answer while the app runs: by percentage, time window or targeting.",
                    nodes = listOf(
                        DNode("flag", "Feature flag", 0.5f, 0.14f, w = 0.4f, kind = NodeKind.PILL, sub = "EBikes"),
                        DNode("pct", "Percentage", 0.17f, 0.55f, w = 0.3f, h = 0.2f, tone = 1, sub = "10% of riders"),
                        DNode("time", "Time window", 0.5f, 0.55f, w = 0.3f, h = 0.2f, tone = 1, sub = "the weekend"),
                        DNode("tgt", "Targeting", 0.83f, 0.55f, w = 0.3f, h = 0.2f, tone = 1, sub = "staff group"),
                        DNode("snap", "Snapshot: frozen keys", 0.5f, 0.88f, w = 0.56f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("flag", "pct"),
                        DEdge("flag", "time"),
                        DEdge("flag", "tgt"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("flag", "pct"), light = setOf("flag", "pct"), pulse = "flag" to "pct", card = BoardCard("Feature flag", "An on-off switch on the board. Ten percent of riders: percentage.")),
                    Beat(28, show = setOf("time", "tgt", "snap"), light = setOf("time", "tgt"), card = BoardCard("Feature filters", "Weekend: time window. Staff first: targeting. Snapshot: frozen keys.")),
                ),
            ),
            // 29: "So, the case summary for the exam?"
            Scene(
                29, "Case closed",
                DiagramSpec(
                    "The exam's App Configuration questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("restart", "No restart", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "sentinel, middleware"),
                        DNode("env", "Per environment", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "a label"),
                        DNode("secret", "A secret", 0.5f, 0.45f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, sub = "Key Vault reference"),
                        DNode("share", "A share of users", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "percentage filter"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("restart", "env", "secret", "share"), light = setOf("restart"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Not a label, not a sentinel: a filter.")),
                    Beat(32, card = BoardCard("Today", "A free store, two labels, a flag, a console app, then a sentinel.")),
                ),
            ),
        ),
    )

    private fun appConfigGossipBoard() = PodcastBoard(
        "az-appconfig--gossip",
        listOf(
            // 0: "Okay, did you hear what Dana did with the quiz app?..."
            Scene(
                0, "Dana's quiz app",
                DiagramSpec(
                    "The quiz app's backend reads its settings from App Configuration, a store of keys and values.",
                    nodes = listOf(
                        DNode("phone", "Quiz app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("kv", "Key = value", 0.74f, 0.2f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("store", "App Configuration", 0.74f, 0.66f, w = 0.42f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app"),
                        DEdge("store", "app", "settings"),
                        DEdge("kv", "store"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone")),
                    Beat(2, show = setOf("app", "store"), light = setOf("store"), pulse = "store" to "app", card = BoardCard("Everything on the board", "Time limits, the daily quiz count, even the button colour.")),
                    Beat(4, show = setOf("kv"), light = setOf("kv"), pulse = "kv" to "store", card = BoardCard("Under the picture", "A key-value store. A library loads keys into the app's settings.")),
                ),
            ),
            // 5: "Sounds sensible. So what did Dana do?"
            Scene(
                5, "No secrets on the board",
                DiagramSpec(
                    "Secrets stay in Key Vault; the store only holds a Key Vault reference that points to them.",
                    nodes = listOf(
                        DNode("store", "App Configuration", 0.26f, 0.25f, w = 0.44f, h = 0.28f, kind = NodeKind.CYLINDER),
                        DNode("ref", "Key Vault reference", 0.26f, 0.75f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a pointer"),
                        DNode("vault", "Key Vault", 0.8f, 0.75f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("store", "ref"),
                        DEdge("ref", "vault", "points to", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("store"), light = setOf("store"), card = BoardCard("Dana's password", "The scoring database password, on the board as a plain value.")),
                    Beat(8, show = setOf("ref", "vault"), light = setOf("ref"), pulse = "ref" to "vault", card = BoardCard("Key Vault reference", "A pointer. The password never leaves the vault.")),
                ),
            ),
            // 9: "Okay. Who's next?"
            Scene(
                9, "Ben's two hundred keys",
                DiagramSpec(
                    "Watching every key costs a request per key; one sentinel key triggers a reload of all the others.",
                    nodes = listOf(
                        DNode("every", "Every key watched", 0.26f, 0.2f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "a request per key"),
                        DNode("quota", "Free tier", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "1000 requests a day"),
                        DNode("sent", "Sentinel key", 0.5f, 0.74f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "the guard"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("every"), light = setOf("every"), card = BoardCard("Two hundred keys", "Ben registered every one for refresh. Each check: 200 requests.")),
                    Beat(12, show = setOf("quota", "sent"), light = setOf("sent"), card = BoardCard("Sentinel key", "Bump one key and every other key reloads. One request, not hundreds.")),
                ),
            ),
            // 13: "One request instead of two hundred. And the third?"
            Scene(
                13, "The intern waited",
                DiagramSpec(
                    "The middleware checks the sentinel on a request after the interval; when it changes, every other key reloads.",
                    nodes = listOf(
                        DNode("sent", "Sentinel key", 0.26f, 0.18f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "the guard"),
                        DNode("mw", "Middleware", 0.78f, 0.18f, w = 0.36f, h = 0.18f, sub = "on each request"),
                        DNode("others", "All other keys", 0.26f, 0.62f, w = 0.42f, h = 0.2f, tone = 2),
                        DNode("app", "Quiz backend", 0.78f, 0.62f, w = 0.36f, h = 0.2f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("mw", "sent", "checks"),
                        DEdge("others", "app", "reloaded"),
                        DEdge("mw", "app"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("sent", "others", "app"), light = setOf("sent"), card = BoardCard("Everything right?", "Sentinel registered, interval set, key bumped. Nothing changed.")),
                    Beat(16, show = setOf("mw"), light = setOf("mw"), pulse = "mw" to "sent", card = BoardCard("No middleware, no refresh", "It runs on each request, and checks the sentinel after the interval.")),
                    Beat(18, light = setOf("app"), pulse = "others" to "app", card = BoardCard("Not instant", "On the next request after the interval. App code reads as usual.")),
                ),
            ),
            // 19: "Who else? I heard Aisha's name."
            Scene(
                19, "Aisha's connection string",
                DiagramSpec(
                    "The backend reads the store with its managed identity and the Data Reader role; the phone only asks the backend.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.24f, 0.22f, w = 0.4f, h = 0.2f),
                        DNode("cs", "Connection string", 0.76f, 0.22f, w = 0.42f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                        DNode("app", "Backend", 0.24f, 0.74f, w = 0.32f, h = 0.2f, sub = "managed identity"),
                        DNode("store", "App Configuration", 0.76f, 0.74f, w = 0.42f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "asks"),
                        DEdge("app", "store", "Data Reader"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(19, show = setOf("cs", "app", "store"), light = setOf("cs"), card = BoardCard("A connection string", "Pasted into settings, though the backend had a managed identity.")),
                    Beat(20, light = setOf("app", "store"), pulse = "app" to "store", card = BoardCard("App Configuration Data Reader", "Priya gave the identity the role. Nothing left to leak.")),
                    Beat(22, show = setOf("phone"), light = setOf("phone", "app"), pulse = "phone" to "app", card = BoardCard("The phone asks the backend", "Never a connection string inside the Android app.")),
                ),
            ),
            // 23: "Any happy stories this week?"
            Scene(
                23, "Sam's two labels",
                DiagramSpec(
                    "A label is a variant of one key per environment; each app loads its own label with a label filter.",
                    nodes = listOf(
                        DNode("key", "DailyQuizzes", 0.5f, 0.15f, w = 0.4f),
                        DNode("dev", "label dev: 50", 0.24f, 0.48f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                        DNode("prod", "label prod: 15", 0.76f, 0.48f, w = 0.4f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("devapp", "Test app", 0.24f, 0.8f, w = 0.3f, h = 0.14f, tone = 1),
                        DNode("liveapp", "Live app", 0.76f, 0.8f, w = 0.3f, h = 0.14f),
                    ),
                    edges = listOf(
                        DEdge("key", "dev"),
                        DEdge("key", "prod"),
                        DEdge("dev", "devapp"),
                        DEdge("prod", "liveapp"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("key")),
                    Beat(24, show = setOf("dev", "prod", "devapp", "liveapp"), light = setOf("dev", "prod"), pulse = "prod" to "liveapp", card = BoardCard("Label", "Same key, a value per environment. Each app loads its own label.")),
                ),
            ),
            // 27: "Nice. And the new quiz mode everyone's talking about?"
            Scene(
                27, "Nora's fifth",
                DiagramSpec(
                    "A feature filter decides a flag's answer while the app runs: by percentage, time window or targeting.",
                    nodes = listOf(
                        DNode("flag", "Feature flag", 0.5f, 0.14f, w = 0.4f, kind = NodeKind.PILL, sub = "QuizMode"),
                        DNode("pct", "Percentage", 0.17f, 0.55f, w = 0.3f, h = 0.2f, tone = 1, sub = "20% of players"),
                        DNode("time", "Time window", 0.5f, 0.55f, w = 0.3f, h = 0.2f, tone = 1, sub = "a tournament"),
                        DNode("tgt", "Targeting", 0.83f, 0.55f, w = 0.3f, h = 0.2f, tone = 1, sub = "beta testers"),
                        DNode("snap", "Snapshot: frozen keys", 0.5f, 0.88f, w = 0.56f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("flag", "pct"),
                        DEdge("flag", "time"),
                        DEdge("flag", "tgt"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("flag", "pct"), light = setOf("pct"), pulse = "flag" to "pct", card = BoardCard("Not a label: a percentage", "A fifth of players is a percentage filter, decided while the app runs.")),
                    Beat(30, show = setOf("time", "tgt", "snap"), light = setOf("time", "tgt"), card = BoardCard("Feature filters", "Weekend: time window. Beta testers: targeting. Snapshot: frozen keys.")),
                ),
            ),
            // 31: "So, the gossip I should remember for the exam?"
            Scene(
                31, "For the exam",
                DiagramSpec(
                    "The exam's App Configuration questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("restart", "No restart", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "sentinel, middleware"),
                        DNode("env", "Per environment", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "a label"),
                        DNode("secret", "A secret", 0.5f, 0.45f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, sub = "Key Vault reference"),
                        DNode("share", "A share of users", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "percentage filter"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("restart", "env", "secret", "share"), light = setOf("share"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. The identity reads with Data Reader.")),
                    Beat(34, card = BoardCard("Today", "A free store, two labels, a flag, a console app, then a sentinel.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(graphCaseBoard(), graphGossipBoard(), appConfigCaseBoard(), appConfigGossipBoard()) }
}
