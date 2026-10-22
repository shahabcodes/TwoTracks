package com.shahaabapps.core.content

/** The Connect module's AZ-204 topics, API Management and Event Grid, as Case Files and Gossip, with their boards (Az204StyledPodcasts). */
internal object Az204StyledConnect {
    private val apimCase = Podcast(
        "az-apim",
        listOf(
            maya("Welcome to Case Files. Today's case: The Phantom Bookings. Iron Oak, a small gym, has an Android app for booking classes. Its backend runs on App Service, and in front of it sits Azure API Management. Bookings open every Monday at seven. This Monday, every spin class was full by one minute past, booked by accounts nobody at the gym had ever seen."),
            leo("[chuckle] Ghosts on exercise bikes. Before I start accusing anyone, what is API Management, in plain words?"),
            maya("One front door for all your APIs. An API is the set of addresses your backend answers on. The front door has a doorman, called the gateway, who takes every call and runs your rules. Those rules are called policies. There's also a developer portal, a website where partners read the documentation and try calls."),
            leo("And under the picture? What is the doorman, really?"),
            maya("A proxy that Azure runs for you. The phone calls the gateway's address, never the backend's. The gateway runs your policies on the request, forwards it to the backend, then runs more policies on the reply before handing it back. So every rule lives in one place, not copied into every API."),
            leo("Then my first suspect is easy. Too many calls, too fast. The gym needs a quota."),
            maya("[pause] Right worry, wrong tool. A quota is a long allowance, say ten thousand calls a month, and past it the answer is 403, Forbidden: you've had your share. A short burst is a rate limit, say sixty calls a minute, and past it the answer is 429, Too Many Requests: slow down."),
            leo("Fine, a rate limit then. That stops the ghosts."),
            maya("It slows them down. It doesn't ask who they are. And here's the clue. The gym already had a rate limit, and a rule that checks every caller's sign-in token. It's called validate JWT, and it sat at the global level, for every API. Yet ghost calls with no token at all were sailing straight through."),
            leo("How do we know that? Fingerprints?"),
            maya("Better. The trace. In the portal's test console, the trace shows every policy that ran on a call, in order. And on the book class operation, the token check simply isn't in the list. It never ran. Read the trace before you suspect anything else."),
            leo("So somebody switched it off?"),
            maya("Nobody touched it. On Friday, a developer added a stricter rate limit to just one operation, booking a class. An operation is one address and method. Policies sit at four levels: global, product, API and operation. And a lower level has to include a base element, which means, run the wider level's rules here too."),
            leo("[sigh] And they left it out."),
            maya("They left it out. So on that one operation, the global token check silently stopped running. It saved without a single complaint, which is what makes it so easy to miss. Put base back in, and every caller has to show a token again."),
            leo("Where does the phone get the token from, though?"),
            maya("The member signs in on the phone, and the app gets a token from Microsoft's identity platform. The MSAL episode covers that side. The gateway only checks the token's signature and its audience, meaning who the token was made for."),
            leo("So is the subscription key the same thing? The app sends one of those too."),
            maya("A different handshake. A product is a bundle of APIs with its own terms, like a phone plan. A subscription gives a caller a pair of keys to one product, and the subscription key goes in a header. The key says which app or partner is calling. The token says which member. Neither one proves anything to the backend."),
            leo("Case closed, then?"),
            maya("Not yet. Ghost bookings kept arriving after base was fixed. And this time, the gateway had no record of them at all."),
            leo("So they never came through the front door. [pause] They came through the back."),
            maya("Exactly. The backend's own address was still open to the internet. Someone found it and called it directly, skipping every policy. The fix is to lock the backend so it only accepts the gateway: an IP restriction, a private network, or a client certificate. And the gateway signs in to the backend with a managed identity, so no password is stored."),
            leo("A private network. Easy. Done."),
            maya("That's the twist. They chose the Consumption tier: serverless, pay per call, ready in minutes. But Consumption has no private network and no developer portal, and the gym's partners want the portal."),
            leo("So which tier gives them both, without paying for the biggest one?"),
            maya("Basic and Standard have the portal, and Standard v2 adds network integration, so the gateway can reach a private backend. Premium adds several regions and full private networks. Developer tier is for testing only."),
            leo("And if the gym chain wants the doorman in its own server room?"),
            maya("The self-hosted gateway. It's the gateway packed as a container, run on your own machines or in another cloud. That's Premium, or Developer for testing. And know where API Management stops. One global entry point that sends web traffic to the nearest region and caches files at the edge is Front Door. The Front Door and CDN episode covers it."),
            leo("One more. Could the rate limit count per member, not per whole app?"),
            maya("Yes, with rate limit by key. The key is a policy expression, a small piece of C sharp inside the policy, with a context object that hands you the request, the caller and the subscription. Count by the caller's address, by subscription, or by any value you can read from the request."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If it says limit bursts, or 429, pick rate limit. If it says a monthly or daily allowance, or 403, pick quota. If parent policies stopped running at one level, look for the missing base. If callers can skip the gateway, lock down the backend. And a gateway on-premises or in another cloud is the self-hosted gateway, on Premium."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the crime scene. Create a Consumption API Management instance, and import the conference sample API. Add a rate limit of three calls per sixty seconds. Then call four times from the test console, and read the 429 and the trace."),
            leo("Getting told to slow down, on purpose. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val apimGossip = Podcast(
        "az-apim",
        listOf(
            maya("Okay, did you hear what Aisha did to the bike rental app? Grab a coffee. This week was all about API Management, and nearly everyone in the office got a turn."),
            leo("[laugh] I heard a hotel was told it couldn't have any more bikes. Start from the top. What did Aisha do?"),
            maya("Aisha runs the backend for the bike rental app, the Android one where you unlock a bike with your phone. Partners, like a hotel booking site, call the same API to show free bikes. She wanted to stop them hammering it at rush hour. So she set a quota, a thousand calls a month."),
            leo("That sounds sensible?"),
            maya("On day three, the hotel hit a thousand, and got 403, Forbidden, for the rest of the month. What Aisha wanted was a rate limit. A rate limit smooths short bursts, say sixty calls a minute, and answers 429, Too Many Requests, which means slow down. A quota is a long allowance, and past it the answer is, you've had your share."),
            leo("Slow down, versus go away. Who fixed it?"),
            maya("Priya. And she started with the thing itself. API Management is one front door for all your APIs. Callers never talk to your servers directly. The doorman is the gateway, which takes every call and runs your rules, called policies."),
            leo("And what is the doorman really, under the picture?"),
            maya("A proxy that Azure runs for you. A call comes in, the gateway runs the inbound policies, forwards the call to your backend, then runs the outbound policies on the reply, and on-error if anything breaks. Policies are short statements in XML, a tag-based text format. Priya put a rate limit in the inbound section, and gave the hotel a fair monthly quota."),
            leo("A monthly plan. Is that a product thing?"),
            maya("It is. A product is a bundle of APIs with its own terms, like a phone plan. The bike team has a Free product anyone can try, and a Partner product that needs approval."),
            leo("And how does the hotel prove it's on the Partner plan?"),
            maya("With a subscription. It gives a caller two keys to one product. The caller sends the subscription key in a header, and the gateway knows who's calling. Two keys, so one can be swapped while the other works."),
            leo("Okay. What's week two?"),
            maya("Sam. Sam wanted a stricter limit on just one operation, unlocking a bike. An operation is one address and method. Policies sit at four levels: global, product, API and operation. Sam wrote a fresh policy for unlock, and left out one tiny line called base. Unlock had been getting hammered by a script, so Sam was in a hurry."),
            leo("Which does what?"),
            maya("Base means, run the wider level's rules here too. Without it, the global rules silently stopped running on unlock. Including the token check, validate JWT, which makes sure the rider really signed in. For two days, unlock never asked who you were."),
            leo("[sigh] Free bikes for everyone. How did anyone notice?"),
            maya("Priya again. She opened the test console and read the trace, which lists every policy that ran on a call. The token check wasn't there. She put base back. Then she showed Sam a policy expression, a little C sharp inside the policy, with a context object, so the limit could count per rider instead of per app."),
            leo("Week three?"),
            maya("Ben. Ben's backend sat behind the gateway, all neatly wrapped in policies. But its own address was still open to the internet. A curious developer found it and called it directly. No key, no token, no rate limit. They just walked around the front door. It took a full day before anyone noticed."),
            leo("So the front door is only as good as the back door."),
            maya("That's what Dana told him. The subscription key proves who's calling to the gateway, not to the backend. So you lock the backend to accept only the gateway: an IP restriction, a private network, or a client certificate. And the gateway signs in with a managed identity, a badge from Azure, so no password is stored. The Key Vault episode explains those badges."),
            leo("And the intern? There's always an intern."),
            maya("[chuckle] Bless them. The intern set the whole thing up on the Consumption tier. Serverless, pay per call, ready in minutes. Lovely. Then the partners asked for the developer portal, the website with docs and a try-it page. And Ben wanted his private network. Consumption has neither."),
            leo("So what should they have picked?"),
            maya("Developer tier is for testing, with no SLA. Basic and Standard are for production, and Standard v2 adds network integration. Premium brings several regions, private networks and the self-hosted gateway. That's the gateway as a container you run in your own building or another cloud, say at the bike depots with their own servers."),
            leo("Is API Management the answer to everything, then?"),
            maya("No, and the exam likes that. It manages APIs: keys, limits, tokens, reshaping requests. It doesn't run your code, and it isn't a global entry point for a website. Sending visitors to the nearest region and caching files at the edge is Front Door, in the Front Door and CDN episode. And a mobile team waiting on a backend can use mock response, a pretend reply."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. Limit bursts, or 429, is rate limit. A monthly or daily allowance, or 403, is quota. Parent policies stopped running means a missing base. Callers skipping the gateway means lock down the backend. And a gateway on-premises or in another cloud is the self-hosted gateway, on Premium."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a Consumption API Management instance, and import the conference sample API. Add a rate limit of three calls per sixty seconds. Then call four times from the test console, and read the 429 and the trace."),
            leo("Slow down, not go away. Got it, Aisha. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    private val eventGridCase = Podcast(
        "az-eventgrid",
        listOf(
            maya("Welcome to Case Files. Today's case: The Echoing Fern. A small plant care app for Android, called Leafy. You photograph your plant, the photo goes to Blob Storage, and a few seconds later your phone says something like, your fern looks thirsty. This week, users started getting every message twice. Some got it three times."),
            leo("[chuckle] A fern with an echo. Who's the messenger in this story?"),
            maya("Azure Event Grid. Think of a postman for news. An event is a short note that says, this happened: a photo was uploaded. Event Grid carries that note, the moment it happens, to whoever cares. That's push. Nobody has to keep asking storage, anything new? That endless asking is called polling."),
            leo("And what is the postman really, under the picture?"),
            maya("An event is a small JSON document: an id, a subject that's like the item's path, an event type, a time, and a little data. Event Grid is a routing service for those documents. Sources post them to a topic, an address that works like a noticeboard, and Event Grid passes each one on to whoever subscribed."),
            leo("Then here's my first suspect. Users are tapping upload twice. Impatient gardeners."),
            maya("[pause] Check the evidence. Both messages came from events with the same event id. Two taps would make two uploads, so two different events. This is one event, delivered twice. The event id is our fingerprint."),
            leo("Then Event Grid is broken. It's supposed to deliver once."),
            maya("That's the mistake the whole case turns on. Event Grid promises at least once, not exactly once. Here's how delivery works. It sends the event to the handler and waits for a success reply. No reply in time, or an error, and it tries again, waiting longer each time, for up to twenty-four hours. You can tune the attempts and an event's lifetime."),
            leo("So the handler didn't answer in time?"),
            maya("Right. The handler is a Function that looks at the photo and sends the message. Last week someone added a slower plant health check. The Function sent the message, then took too long to say done. Event Grid saw no success, and delivered again. Every retry sent another message. The Azure Functions episode covers writing handlers like that one."),
            leo("So we make the Function faster."),
            maya("Faster helps, but it's not the cure. Any handler can see a repeat, so it has to be idempotent. Doing it twice gives the same result as doing it once, like pressing a lift button twice. The Function now stores each event id it has handled, and if it sees one again, it does nothing."),
            leo("[sigh] Fine. Case closed?"),
            maya("Not quite. The second clue is the bill. The Function runs far more often than people upload photos. Read the log, and it's waking up for every file in the storage account: thumbnails, backups, little text files of plant notes."),
            leo("But the code checks the file name first and stops. I've seen it."),
            maya("And that's the second mistake. Filtering inside the handler means each unwanted event is still delivered, still wakes the Function, still costs. The fix belongs on the event subscription, the route from a topic to one handler. Give it a subject filter: the subject begins with the photos container's path, and ends with dot JPG. Everything else is never delivered."),
            leo("And the topic here is the storage account itself?"),
            maya("Its system topic, one Azure makes for an Azure service's events, like BlobCreated. Leafy also has a custom topic of its own. When a user taps, I watered it, the backend publishes a plant watered event there, and a separate handler schedules the next reminder."),
            leo("Any more witnesses?"),
            maya("One. A garden shop partner wanted those plant watered events, to send discount codes. They pointed a subscription at their webhook, which is just a web address that accepts the notes. Not one event ever arrived."),
            leo("A broken webhook?"),
            maya("A too polite webhook. Before Event Grid trusts an address, it does a validation handshake. It sends a validation code, and the webhook must send that same code back. Theirs just replied 200, okay, without the code, so the subscription was never validated."),
            leo("So the garden shop has to write that echo themselves?"),
            maya("Or use a Function with an Event Grid trigger, which does the handshake for you. Or someone opens the validation link Event Grid sends, within ten minutes."),
            leo("And notes that never get through, even after a whole day of trying?"),
            maya("By default, they're dropped. Configure dead-lettering, and they're saved as files in a storage container instead, a lost-property box you can read later. Leafy now dead-letters, so a corrupted photo that keeps crashing the Function is kept, not lost."),
            leo("Last suspect. The smart flower pots. Can they send their soil readings through Event Grid too?"),
            maya("That's where Event Grid stops. Readings every second from thousands of pots is high-volume telemetry, a stream, and that's Event Hubs. Event Grid is for single things that happened, one at a time. And an order that must be charged exactly right is a job for Service Bus. The Event Hubs and Service Bus episodes pick it up from there."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If it says react when something happens, pick Event Grid. If only certain files or folders should fire, a subject filter, begins with or ends with. If undelivered events must be kept, dead-lettering to a storage container. If a handler sees duplicates, make it idempotent. And if a webhook never gets events, it must answer the validation handshake."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the crime scene. Create a storage account and an Event Grid trigger Function. Add a system topic subscription for BlobCreated. Upload a file, and read the event JSON in the Function log. Then add a subject suffix filter for PNG, upload a text file, and watch it get skipped."),
            leo("A witness who never shows up. My favourite kind. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val eventGridGossip = Podcast(
        "az-eventgrid",
        listOf(
            maya("Did you hear what Dana did to the quiz app leaderboard? Every player who finished a quiz on Tuesday got double points. Grab a coffee. It was an Event Grid week."),
            leo("[laugh] I heard someone topped the leaderboard after playing twice. What actually happened?"),
            maya("So, the quiz app. Android, with a small backend on Azure. When a player finishes a quiz, the backend posts a short note: quiz finished, this player, this score. That note is an event. And Azure Event Grid carries it, the moment it happens, to the code that adds the points."),
            leo("A note, carried. In plain words, Event Grid is what?"),
            maya("A postman for news. Nobody has to keep asking, anything new? That asking is polling, and Event Grid replaces it with push. Under the picture, an event is a small JSON document, with an id, a subject, an event type, a time and a bit of data. Event Grid is a routing service that passes those documents from where they happen to whoever subscribed."),
            leo("So where did Dana go wrong?"),
            maya("Dana assumed every event arrives exactly once. But Event Grid promises at least once. It posts the event to your handler and waits for a success reply. No success, and it retries, waiting longer each time, for up to twenty-four hours. On Tuesday the points handler was slow, timed out, and got plenty of events twice. You can tune the attempts and an event's lifetime."),
            leo("[sigh] And added the points twice."),
            maya("Every time. Aisha fixed it. She made the handler idempotent. That's a big word for, doing it twice gives the same result as doing it once. Pressing the lift button twice still calls one lift. The handler now records each event id, and skips any id it has already handled."),
            leo("Nice. And where do these quiz notes get posted, exactly?"),
            maya("To a topic, an address that works like a noticeboard. Quiz finished goes to a custom topic, one the app publishes to itself. There's also a system topic, one Azure makes for an Azure service. The app's storage account has one, so when a player uploads an avatar, Blob Storage posts a BlobCreated event. You pay per operation, and the first hundred thousand a month are free."),
            leo("And who reads the noticeboard?"),
            maya("Each event subscription is a route from a topic to one handler, the code or service that reacts. A Function, a Logic App, a queue, or a webhook, which is just a web address that accepts the notes. Which brings us to week two. Ben."),
            leo("What did Ben do?"),
            maya("Ben built a webhook in his own API, to email players a well done. He made it answer 200, okay, to anything at all, then created the subscription. Ben was proud of it for about an hour. The subscription never got validated, and not one event arrived."),
            leo("Why not? It said okay."),
            maya("Okay isn't enough. Before Event Grid trusts a web address, it does a validation handshake. It sends a validation code, and the webhook has to send that same code back. Like confirming your email for a newsletter."),
            leo("So Ben's webhook said okay, but never repeated the code back."),
            maya("Exactly. Sam showed Ben the fix: echo the code. Or use a Function with an Event Grid trigger, which does the handshake for you. Or open the validation link it sends, within ten minutes."),
            leo("Week three?"),
            maya("The intern, bless them. The avatar resizer was waking up for every file in the storage account. Backups, logs, everything. The intern's fix was a check at the top of the Function: if it's not a picture, stop. It ran thousands of times a day, mostly to say, not mine."),
            leo("That sounds reasonable, though?"),
            maya("It works, but each unwanted event is still delivered, still wakes the Function, still costs. Priya moved it onto the subscription. A subject filter: the subject begins with the avatars container's path, and ends with dot PNG. Everything else never leaves Event Grid. You can filter by event type too, or on fields in the data."),
            leo("And the Friday? There's always a Friday."),
            maya("[chuckle] Marcus. Marcus wanted every tap in every quiz, thousands a second, sent through Event Grid for analytics. Priya stopped him at the whiteboard. That's high-volume telemetry, a stream, and that's Event Hubs. Event Grid is for single things that happened. Jobs that must each be handled with care, like paying out a prize, are Service Bus. The Event Hubs episode covers the stream side."),
            leo("Did anything get lost for good in all this?"),
            maya("Almost. During Ben's week, some events failed for a whole day and were simply dropped. Now the team configures dead-lettering. Events that still can't be delivered after the retries are saved as files in a storage container, a lost-property box you can read later, instead of vanishing. The Blob Storage episode covers those containers."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. React when something happens is Event Grid. Only certain files or folders should fire is a subject filter, begins with or ends with. Undelivered events must be kept is dead-lettering to a storage container. A handler seeing duplicates must be idempotent. And a webhook must answer the validation handshake."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a storage account and an Event Grid trigger Function. Add a system topic subscription for BlobCreated. Upload a file, and read the event JSON in the Function log. Then add a subject suffix filter for PNG, upload a text file, and watch it get skipped."),
            leo("Twice is the same as once. Got it, Dana. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    val podcasts: List<Podcast> = listOf(apimCase, apimGossip, eventGridCase, eventGridGossip)

    private fun apimCaseBoard() = PodcastBoard(
        "az-apim--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Phantom Bookings..."
            Scene(
                0, "The Phantom Bookings",
                DiagramSpec(
                    "The gym's Android app calls API Management, whose gateway runs the policies and forwards each call to the backend.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.17f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("gw", "API Management", 0.5f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("be", "Backend", 0.83f, 0.3f, w = 0.3f, h = 0.2f, tone = 1),
                        DNode("ghost", "Spin full by 7:01", 0.5f, 0.78f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("phone", "gw", "book"),
                        DEdge("gw", "be"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "gw", "be", "ghost"), light = setOf("ghost"), pulse = "phone" to "gw", card = BoardCard("The case", "Monday, seven o'clock: every spin class booked by unknown accounts.")),
                    Beat(2, light = setOf("gw"), card = BoardCard("Gateway", "The doorman: takes every call and runs your rules, called policies.")),
                    Beat(4, light = setOf("gw", "be"), pulse = "gw" to "be", card = BoardCard("Under the picture", "A proxy Azure runs: policies on the way in, the backend, policies out.")),
                ),
            ),
            // 5: "Then my first suspect is easy. Too many calls, too fast..."
            Scene(
                5, "The wrong tool",
                DiagramSpec(
                    "A rate limit stops short bursts with 429; a quota is a long allowance, and past it the answer is 403.",
                    nodes = listOf(
                        DNode("rl", "Rate limit", 0.26f, 0.22f, w = 0.44f, h = 0.24f, sub = "short window"),
                        DNode("q", "Quota", 0.76f, 0.22f, w = 0.44f, h = 0.24f, sub = "long allowance"),
                        DNode("r429", "429 Too Many Requests", 0.26f, 0.72f, w = 0.46f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                        DNode("r403", "403 Forbidden", 0.76f, 0.72f, w = 0.44f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("rl", "r429", "slow down"),
                        DEdge("q", "r403", "your share"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("q"), light = setOf("q")),
                    Beat(6, show = setOf("r403", "rl", "r429"), light = setOf("rl", "r429"), pulse = "rl" to "r429", card = BoardCard("Suspect one: wrong tool", "Bursts are a rate limit, 429. A quota is a monthly allowance, 403.")),
                    Beat(7, light = setOf("rl"), card = BoardCard("It only slows them", "A rate limit never asks who the caller is.")),
                ),
            ),
            // 8: "It slows them down. It doesn't ask who they are. And here's the clue..."
            Scene(
                8, "The trace",
                DiagramSpec(
                    "The global token check never ran on the book class operation, because its policy left out the base element.",
                    nodes = listOf(
                        DNode("global", "Global", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "validate JWT"),
                        DNode("op", "Book class", 0.76f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "operation level"),
                        DNode("base", "base left out", 0.5f, 0.72f, w = 0.66f, h = 0.22f, kind = NodeKind.NOTE, sub = "global rules skipped"),
                    ),
                    edges = listOf(
                        DEdge("global", "op", "skipped", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("global"), light = setOf("global"), card = BoardCard("validate JWT", "Checks every caller's sign-in token. Set at the global level.")),
                    Beat(10, show = setOf("op"), light = setOf("op"), card = BoardCard("The trace", "Every policy that ran, in order. The token check isn't there.")),
                    Beat(12, show = setOf("base"), light = setOf("base"), pulse = "global" to "op", card = BoardCard("XML", "<inbound><base /><rate-limit calls=\"10\" renewal-period=\"60\" /></inbound>", code = true)),
                    Beat(14, light = setOf("global", "op"), card = BoardCard("Put base back", "It saved without complaint. With base, every caller shows a token.")),
                ),
            ),
            // 15: "Where does the phone get the token from, though?"
            Scene(
                15, "Token and key",
                DiagramSpec(
                    "The token says which member is calling; the subscription key says which app or partner. Both are shown to the gateway.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.17f, 0.5f, w = 0.3f, h = 0.2f),
                        DNode("token", "Sign-in token", 0.56f, 0.2f, w = 0.4f, h = 0.2f, kind = NodeKind.PILL, sub = "which member"),
                        DNode("key", "Subscription key", 0.56f, 0.8f, w = 0.4f, h = 0.2f, kind = NodeKind.PILL, sub = "which app"),
                        DNode("gw", "Gateway", 0.86f, 0.5f, w = 0.26f, h = 0.2f),
                    ),
                    edges = listOf(
                        DEdge("phone", "token"),
                        DEdge("token", "gw"),
                        DEdge("phone", "key"),
                        DEdge("key", "gw"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("phone", "token", "gw")),
                    Beat(16, light = setOf("token"), pulse = "token" to "gw", card = BoardCard("A token from the phone", "The member signs in with MSAL; the gateway checks signature and audience.")),
                    Beat(18, show = setOf("key"), light = setOf("key"), pulse = "key" to "gw", card = BoardCard("Product and key", "A product bundles APIs; its subscription key says which app is calling.")),
                ),
            ),
            // 19: "Case closed, then?"
            Scene(
                19, "The back door",
                DiagramSpec(
                    "The backend accepts only the gateway, which signs in to it with a managed identity; nobody can walk round.",
                    nodes = listOf(
                        DNode("ghost", "Someone", 0.12f, 0.3f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 2),
                        DNode("gw", "Gateway", 0.48f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("be", "Backend", 0.84f, 0.3f, w = 0.28f, h = 0.2f, tone = 1),
                        DNode("id", "Managed identity", 0.6f, 0.8f, w = 0.46f, h = 0.18f, kind = NodeKind.PILL, sub = "the gateway's badge"),
                    ),
                    edges = listOf(
                        DEdge("ghost", "be", "direct", dashed = true),
                        DEdge("gw", "be", "only path"),
                        DEdge("id", "be", "sign in"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(19, show = setOf("gw", "be")),
                    Beat(20, light = setOf("gw"), card = BoardCard("No record at the gateway", "The bookings never came through the front door.")),
                    Beat(21, show = setOf("ghost"), light = setOf("ghost", "be"), pulse = "ghost" to "be"),
                    Beat(22, show = setOf("id"), light = setOf("be", "id"), pulse = "gw" to "be", card = BoardCard("Lock the backend", "Accept only the gateway: IP restriction, private network or certificate.")),
                ),
            ),
            // 23: "A private network. Easy. Done."
            Scene(
                23, "The twist: tiers",
                DiagramSpec(
                    "Consumption is pay per call with no portal or private network; Premium adds regions, networks and the self-hosted gateway.",
                    nodes = listOf(
                        DNode("cons", "Consumption", 0.26f, 0.14f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, sub = "pay per call"),
                        DNode("dev", "Developer", 0.76f, 0.14f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, tone = 2, sub = "testing only"),
                        DNode("std", "Basic, Standard", 0.26f, 0.47f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, tone = 1, sub = "production"),
                        DNode("prem", "Premium", 0.76f, 0.47f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, sub = "regions, networks"),
                        DNode("sh", "Self-hosted gateway", 0.76f, 0.84f, w = 0.44f, h = 0.2f, sub = "on your machines"),
                    ),
                    edges = listOf(
                        DEdge("prem", "sh"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("cons", "dev", "std", "prem"), light = setOf("cons"), card = BoardCard("Consumption", "Serverless, pay per call. No developer portal, no private network.")),
                    Beat(27, show = setOf("sh"), light = setOf("prem", "sh"), pulse = "prem" to "sh"),
                    Beat(28, light = setOf("sh"), card = BoardCard("Self-hosted gateway", "A container on-premises or in another cloud. Past APIM: Front Door.")),
                ),
            ),
            // 29: "One more. Could the rate limit count per member, not per whole app?"
            Scene(
                29, "Count by key",
                DiagramSpec(
                    "Rate limit by key counts per caller address, per subscription, or by any value a policy expression reads.",
                    nodes = listOf(
                        DNode("rl", "Rate limit by key", 0.5f, 0.2f, w = 0.5f, h = 0.2f),
                        DNode("a1", "Address", 0.18f, 0.75f, w = 0.3f, h = 0.2f, tone = 1),
                        DNode("a2", "Subscription", 0.5f, 0.75f, w = 0.3f, h = 0.2f, tone = 1),
                        DNode("a3", "Request value", 0.82f, 0.75f, w = 0.3f, h = 0.2f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("rl", "a1"),
                        DEdge("rl", "a2"),
                        DEdge("rl", "a3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("rl"), light = setOf("rl")),
                    Beat(30, show = setOf("a1", "a2", "a3"), light = setOf("rl", "a2"), pulse = "rl" to "a2", card = BoardCard("Policy expression", "counter-key=\"@(context.Subscription.Id)\"", code = true)),
                ),
            ),
            // 31: "So, the case summary for the exam?"
            Scene(
                31, "Case closed",
                DiagramSpec(
                    "The exam's API Management questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("burst", "Bursts, or 429", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "rate limit"),
                        DNode("allow", "Allowance, or 403", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "quota"),
                        DNode("base", "Parent rules skipped", 0.26f, 0.46f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "missing base"),
                        DNode("skip", "Callers skip gateway", 0.76f, 0.46f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "lock the backend"),
                        DNode("onprem", "Gateway on-premises", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "self-hosted, Premium"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("burst", "allow", "base", "skip", "onprem"), light = setOf("base", "skip"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one policy, fix or tier.")),
                    Beat(34, card = BoardCard("Today", "Consumption tier, import the sample, rate limit 3 per 60, read the 429.")),
                ),
            ),
        ),
    )

    private fun apimGossipBoard() = PodcastBoard(
        "az-apim--gossip",
        listOf(
            // 0: "Okay, did you hear what Aisha did to the bike rental app?..."
            Scene(
                0, "What Aisha did",
                DiagramSpec(
                    "Aisha set a monthly quota where she wanted a rate limit: past a quota the answer is 403, past a rate limit 429.",
                    nodes = listOf(
                        DNode("q", "Quota", 0.26f, 0.22f, w = 0.44f, h = 0.24f, sub = "1,000 a month"),
                        DNode("rl", "Rate limit", 0.76f, 0.22f, w = 0.44f, h = 0.24f, sub = "60 a minute"),
                        DNode("r403", "403 Forbidden", 0.26f, 0.72f, w = 0.44f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                        DNode("r429", "429 Too Many Requests", 0.76f, 0.72f, w = 0.46f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("q", "r403", "go away"),
                        DEdge("rl", "r429", "slow down"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("q")),
                    Beat(2, light = setOf("q"), card = BoardCard("Aisha's quota", "1,000 calls a month, to stop partners hammering the bike API.")),
                    Beat(4, show = setOf("r403", "rl", "r429"), light = setOf("rl", "r429"), pulse = "rl" to "r429", card = BoardCard("Rate limit", "Short bursts, 429: slow down. A quota is a long allowance, 403.")),
                ),
            ),
            // 5: "Slow down, versus go away. Who fixed it?"
            Scene(
                5, "One front door",
                DiagramSpec(
                    "API Management stands in front of the bike API: its gateway takes every call and runs the policies.",
                    nodes = listOf(
                        DNode("p1", "Hotel partner", 0.12f, 0.25f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("p2", "Rider app", 0.12f, 0.68f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                        DNode("gw", "API Management", 0.48f, 0.46f, w = 0.34f, h = 0.28f, sub = "the gateway"),
                        DNode("be", "Bike API", 0.85f, 0.46f, w = 0.28f, h = 0.2f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("p1", "gw"),
                        DEdge("p2", "gw"),
                        DEdge("gw", "be"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("p1", "p2", "gw", "be"), light = setOf("gw"), pulse = "p1" to "gw", card = BoardCard("Gateway", "The doorman: takes every call and runs your rules, called policies.")),
                    Beat(8, pulse = "gw" to "be", card = BoardCard("Under the picture", "A proxy Azure runs: inbound, the backend, outbound, on-error.")),
                ),
            ),
            // 9: "A monthly plan. Is that a product thing?"
            Scene(
                9, "Products and keys",
                DiagramSpec(
                    "A product bundles APIs with its own terms; a subscription gives a caller keys, sent to the gateway.",
                    nodes = listOf(
                        DNode("free", "Free product", 0.26f, 0.18f, w = 0.44f, h = 0.22f, tone = 1, sub = "open to anyone"),
                        DNode("partner", "Partner product", 0.76f, 0.18f, w = 0.44f, h = 0.22f, tone = 1, sub = "needs approval"),
                        DNode("key", "Subscription key", 0.5f, 0.54f, w = 0.5f, kind = NodeKind.PILL),
                        DNode("gw", "Gateway", 0.5f, 0.86f, w = 0.36f, h = 0.18f),
                    ),
                    edges = listOf(
                        DEdge("partner", "key", "gives", dashed = true),
                        DEdge("key", "gw", "in a header"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("free", "partner", "key", "gw"), light = setOf("key"), pulse = "key" to "gw", card = BoardCard("Product", "A bundle of APIs with its own terms, like a phone plan.")),
                ),
            ),
            // 13: "Okay. What's week two?"
            Scene(
                13, "Week two: Sam",
                DiagramSpec(
                    "Sam's unlock policy left out base, so the global token check stopped running on that one operation.",
                    nodes = listOf(
                        DNode("global", "Global", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "validate JWT"),
                        DNode("op", "Unlock bike", 0.76f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "operation level"),
                        DNode("base", "base left out", 0.5f, 0.72f, w = 0.66f, h = 0.22f, kind = NodeKind.NOTE, sub = "global rules skipped"),
                    ),
                    edges = listOf(
                        DEdge("global", "op", "skipped", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("global", "op"), light = setOf("op"), card = BoardCard("Four levels", "Global, product, API, operation. Sam wrote a fresh one for unlock.")),
                    Beat(16, show = setOf("base"), light = setOf("base"), pulse = "global" to "op", card = BoardCard("base", "Run the wider level's rules here too. Without it: no token check.")),
                    Beat(18, light = setOf("global", "op"), card = BoardCard("The trace, then the fix", "Base back in; a policy expression counts per rider, not per app.")),
                ),
            ),
            // 19: "Week three?"
            Scene(
                19, "Week three: Ben",
                DiagramSpec(
                    "Ben's backend was still open to the internet; it should accept only the gateway, which signs in with a managed identity.",
                    nodes = listOf(
                        DNode("ghost", "Someone", 0.12f, 0.3f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 2),
                        DNode("gw", "Gateway", 0.48f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("be", "Ben's backend", 0.84f, 0.3f, w = 0.28f, h = 0.2f, tone = 1),
                        DNode("id", "Managed identity", 0.6f, 0.8f, w = 0.46f, h = 0.18f, kind = NodeKind.PILL, sub = "the gateway's badge"),
                    ),
                    edges = listOf(
                        DEdge("ghost", "be", "direct", dashed = true),
                        DEdge("gw", "be", "only path"),
                        DEdge("id", "be", "sign in"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("ghost", "gw", "be"), light = setOf("ghost", "be"), pulse = "ghost" to "be", card = BoardCard("Round the front door", "No key, no token, no rate limit. The backend's address was open.")),
                    Beat(22, show = setOf("id"), light = setOf("be", "id"), pulse = "gw" to "be", card = BoardCard("Lock the backend", "Accept only the gateway: IP restriction, private network or certificate.")),
                ),
            ),
            // 23: "And the intern? There's always an intern."
            Scene(
                23, "The intern's tier",
                DiagramSpec(
                    "Consumption is pay per call with no portal or private network; Premium adds regions, networks and the self-hosted gateway.",
                    nodes = listOf(
                        DNode("cons", "Consumption", 0.26f, 0.14f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, sub = "pay per call"),
                        DNode("dev", "Developer", 0.76f, 0.14f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, tone = 2, sub = "testing, no SLA"),
                        DNode("std", "Basic, Standard", 0.26f, 0.47f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, tone = 1, sub = "production"),
                        DNode("prem", "Premium", 0.76f, 0.47f, w = 0.44f, h = 0.22f, kind = NodeKind.PILL, sub = "regions, networks"),
                        DNode("sh", "Self-hosted gateway", 0.76f, 0.84f, w = 0.44f, h = 0.2f, sub = "on your machines"),
                    ),
                    edges = listOf(
                        DEdge("prem", "sh"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("cons"), light = setOf("cons"), card = BoardCard("Consumption", "Serverless and quick, but no developer portal and no private network.")),
                    Beat(26, show = setOf("dev", "std", "prem", "sh"), light = setOf("prem", "sh"), pulse = "prem" to "sh", card = BoardCard("Self-hosted gateway", "The gateway as a container, in your building or another cloud. Premium.")),
                ),
            ),
            // 27: "Is API Management the answer to everything, then?"
            Scene(
                27, "Where it stops",
                DiagramSpec(
                    "API Management manages APIs; a global entry point that caches at the edge is Front Door, and mock response fakes a reply.",
                    nodes = listOf(
                        DNode("apim", "API Management", 0.26f, 0.25f, w = 0.44f, h = 0.24f, sub = "keys, limits, tokens"),
                        DNode("fd", "Front Door", 0.76f, 0.25f, w = 0.44f, h = 0.24f, tone = 1, sub = "nearest region"),
                        DNode("mock", "Mock response", 0.5f, 0.75f, w = 0.6f, h = 0.22f, kind = NodeKind.NOTE, sub = "a pretend reply"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("apim"), light = setOf("apim")),
                    Beat(28, show = setOf("fd", "mock"), light = setOf("fd"), card = BoardCard("Past the edge", "Nearest region and an edge cache: Front Door, not API Management.")),
                ),
            ),
            // 29: "So, the gossip I should remember for the exam?"
            Scene(
                29, "For the exam",
                DiagramSpec(
                    "The exam's API Management questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("burst", "Bursts, or 429", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "rate limit"),
                        DNode("allow", "Allowance, or 403", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "quota"),
                        DNode("base", "Parent rules skipped", 0.26f, 0.46f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "missing base"),
                        DNode("skip", "Callers skip gateway", 0.76f, 0.46f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "lock the backend"),
                        DNode("onprem", "Gateway on-premises", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "self-hosted, Premium"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("burst", "allow", "base", "skip", "onprem"), light = setOf("burst", "allow"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one policy, fix or tier.")),
                    Beat(32, card = BoardCard("Today", "Consumption tier, import the sample, rate limit 3 per 60, read the 429.")),
                ),
            ),
        ),
    )

    private fun eventGridCaseBoard() = PodcastBoard(
        "az-eventgrid--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Echoing Fern..."
            Scene(
                0, "The Echoing Fern",
                DiagramSpec(
                    "Leafy's photo goes to Blob Storage, a Function checks it and messages the phone, and every message arrives twice.",
                    nodes = listOf(
                        DNode("phone", "Leafy app", 0.2f, 0.22f, w = 0.34f, h = 0.2f),
                        DNode("store", "Blob Storage", 0.78f, 0.22f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("fn", "Function", 0.78f, 0.72f, w = 0.34f, h = 0.2f, tone = 1),
                        DNode("note", "Every message twice", 0.24f, 0.72f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("phone", "store", "photo"),
                        DEdge("store", "fn", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "store", "fn", "note"), light = setOf("note"), pulse = "phone" to "store", card = BoardCard("The case", "Your fern looks thirsty. Twice. Sometimes three times.")),
                    Beat(2, light = setOf("store", "fn"), pulse = "store" to "fn", card = BoardCard("Event Grid", "A postman for news: it pushes each this-happened note. No polling.")),
                ),
            ),
            // 3: "And what is the postman really, under the picture?"
            Scene(
                3, "Under the picture",
                DiagramSpec(
                    "An event is a small JSON document; sources post it to a topic, and Event Grid passes it on to subscribers.",
                    nodes = listOf(
                        DNode("store", "Storage", 0.15f, 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("grid", "Event Grid", 0.5f, 0.4f, w = 0.36f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("fn", "Function", 0.85f, 0.4f, w = 0.28f, h = 0.22f),
                        DNode("doc", "id, subject, type, time, data", 0.5f, 0.84f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("store", "grid", "event"),
                        DEdge("grid", "fn", "push"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("store", "grid", "fn")),
                    Beat(4, show = setOf("doc"), light = setOf("doc", "grid"), pulse = "store" to "grid", card = BoardCard("An event", "{ id, subject, eventType, eventTime, data }", code = true)),
                ),
            ),
            // 5: "Then here's my first suspect. Users are tapping upload twice..."
            Scene(
                5, "The suspects",
                DiagramSpec(
                    "Two taps would make two events; the same event id twice means one event, delivered twice.",
                    nodes = listOf(
                        DNode("taps", "Two taps?", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "two events"),
                        DNode("once", "Exactly once?", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "at least once"),
                        DNode("same", "Same event id", 0.5f, 0.74f, w = 0.66f, h = 0.22f, sub = "one event, twice"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("taps"), light = setOf("taps")),
                    Beat(6, show = setOf("same"), light = setOf("same"), card = BoardCard("Suspect one: cleared", "Two taps make two uploads, so two different events.")),
                    Beat(7, show = setOf("once"), light = setOf("once")),
                    Beat(8, card = BoardCard("Suspect two: cleared", "Event Grid promises at least once, not exactly once.")),
                ),
            ),
            // 9: "So the handler didn't answer in time?"
            Scene(
                9, "Retries and repeats",
                DiagramSpec(
                    "Event Grid retries for up to 24 hours, so a handler may see an event twice and must be idempotent.",
                    nodes = listOf(
                        DNode("grid", "Event Grid", 0.2f, 0.3f, w = 0.36f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("handler", "Slow Function", 0.78f, 0.3f, w = 0.36f, h = 0.22f),
                        DNode("idem", "Idempotent", 0.5f, 0.8f, w = 0.5f, h = 0.22f, kind = NodeKind.NOTE, sub = "twice = once"),
                    ),
                    edges = listOf(
                        DEdge("grid", "handler", "retry up to 24h"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("grid", "handler")),
                    Beat(10, pulse = "grid" to "handler", card = BoardCard("No success reply", "So Event Grid delivered again. Each retry sent another message.")),
                    Beat(12, show = setOf("idem"), light = setOf("idem", "handler"), card = BoardCard("Idempotent", "Twice gives the same result as once. Store each event id handled.")),
                ),
            ),
            // 14: "Not quite. The second clue is the bill..."
            Scene(
                14, "The second clue: the bill",
                DiagramSpec(
                    "A subject filter on the subscription lets JPG files in the photos folder through and stops the rest.",
                    nodes = listOf(
                        DNode("jpg", "fern.jpg", 0.2f, 0.14f, w = 0.3f, tone = 1),
                        DNode("txt", "notes.txt", 0.8f, 0.14f, w = 0.3f, tone = 2),
                        DNode("filter", "Subject filter", 0.5f, 0.5f, w = 0.56f, h = 0.24f, kind = NodeKind.NOTE, sub = "begins: photos, ends: .jpg"),
                        DNode("fn", "Function", 0.5f, 0.87f, w = 0.42f, h = 0.18f),
                    ),
                    edges = listOf(
                        DEdge("jpg", "filter"),
                        DEdge("txt", "filter", "never sent", dashed = true),
                        DEdge("filter", "fn"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("txt", "fn"), light = setOf("txt"), card = BoardCard("Every file wakes it", "Thumbnails, backups, plant notes: each one delivered and paid for.")),
                    Beat(16, show = setOf("jpg", "filter"), light = setOf("filter", "jpg", "fn"), pulse = "filter" to "fn", card = BoardCard("Subject filter", "On the event subscription: begins with the photos path, ends with .jpg.")),
                ),
            ),
            // 17: "And the topic here is the storage account itself?"
            Scene(
                17, "Two topics",
                DiagramSpec(
                    "Sources post events to a topic: a system topic for an Azure service, a custom topic for your own app.",
                    nodes = listOf(
                        DNode("src1", "Storage account", 0.22f, 0.25f, w = 0.4f, h = 0.22f, tone = 1, sub = "system topic"),
                        DNode("src2", "Leafy backend", 0.22f, 0.72f, w = 0.4f, h = 0.22f, tone = 1, sub = "custom topic"),
                        DNode("topic", "Topic", 0.75f, 0.48f, w = 0.42f, h = 0.36f, kind = NodeKind.CLOUD, sub = "a noticeboard"),
                    ),
                    edges = listOf(
                        DEdge("src1", "topic", "BlobCreated"),
                        DEdge("src2", "topic", "plant watered"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(17, show = setOf("topic", "src1"), light = setOf("src1")),
                    Beat(18, show = setOf("src2"), light = setOf("src1", "src2"), pulse = "src2" to "topic", card = BoardCard("System or custom", "Azure makes system topics. Leafy's backend publishes to a custom topic.")),
                ),
            ),
            // 19: "Any more witnesses?"
            Scene(
                19, "The silent webhook",
                DiagramSpec(
                    "Before trusting a webhook, Event Grid sends a validation code that the webhook must send back.",
                    nodes = listOf(
                        DNode("grid", "Event Grid", 0.2f, 0.35f, w = 0.36f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("hook", "Shop webhook", 0.78f, 0.35f, w = 0.36f, h = 0.22f),
                        DNode("fn", "Function trigger", 0.5f, 0.82f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, sub = "does it for you"),
                    ),
                    edges = listOf(
                        DEdge("grid", "hook", "validation code", bidirectional = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("grid", "hook"), light = setOf("hook"), card = BoardCard("Not one event arrived", "The garden shop pointed a subscription at its webhook.")),
                    Beat(22, show = setOf("fn"), light = setOf("hook", "fn"), pulse = "grid" to "hook", card = BoardCard("Validation handshake", "Send the code back; a bare 200 isn't enough. A Function trigger does it.")),
                ),
            ),
            // 25: "And notes that never get through, even after a whole day of trying?"
            Scene(
                25, "Lost property",
                DiagramSpec(
                    "Events that still cannot be delivered after the retries are dead-lettered to a storage container.",
                    nodes = listOf(
                        DNode("grid", "Event Grid", 0.2f, 0.3f, w = 0.36f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("handler", "Function", 0.78f, 0.3f, w = 0.36f, h = 0.24f, tone = 2, sub = "keeps crashing"),
                        DNode("dl", "Dead-letter", 0.5f, 0.78f, w = 0.44f, h = 0.34f, kind = NodeKind.CYLINDER, sub = "a storage container"),
                    ),
                    edges = listOf(
                        DEdge("grid", "handler", "failed", dashed = true),
                        DEdge("grid", "dl", "after retries"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("grid", "handler"), pulse = "grid" to "handler"),
                    Beat(26, show = setOf("dl"), light = setOf("dl"), pulse = "grid" to "dl", card = BoardCard("Dead-letter", "Without it, dropped. With it, kept as files, like a lost-property box.")),
                ),
            ),
            // 27: "Last suspect. The smart flower pots..."
            Scene(
                27, "Where it stops",
                DiagramSpec(
                    "Readings every second are a stream for Event Hubs; Event Grid reacts to single events; Service Bus handles careful jobs.",
                    nodes = listOf(
                        DNode("pots", "Smart pots", 0.5f, 0.14f, w = 0.44f, h = 0.2f, sub = "readings every second"),
                        DNode("eg", "Event Grid", 0.18f, 0.62f, w = 0.32f, h = 0.28f, sub = "single events"),
                        DNode("eh", "Event Hubs", 0.5f, 0.62f, w = 0.3f, h = 0.28f, tone = 1, sub = "a stream"),
                        DNode("sb", "Service Bus", 0.82f, 0.62f, w = 0.32f, h = 0.28f, tone = 1, sub = "careful jobs"),
                    ),
                    edges = listOf(
                        DEdge("pots", "eh"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("pots"), light = setOf("pots")),
                    Beat(28, show = setOf("eg", "eh", "sb"), light = setOf("eh"), pulse = "pots" to "eh", card = BoardCard("High-volume telemetry", "That's Event Hubs. Orders that must be right: Service Bus.")),
                ),
            ),
            // 29: "So, the case summary for the exam?"
            Scene(
                29, "Case closed",
                DiagramSpec(
                    "The exam's Event Grid questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("react", "Something happened", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Event Grid"),
                        DNode("some", "Only some files", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "subject filter"),
                        DNode("keep", "Keep undelivered", 0.26f, 0.46f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "dead-letter"),
                        DNode("dup", "Duplicates", 0.76f, 0.46f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "idempotent"),
                        DNode("hook", "Webhook gets nothing", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "validation handshake"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("react", "some", "keep", "dup", "hook"), light = setOf("dup"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(32, card = BoardCard("Today", "Trigger Function, BlobCreated, read the JSON; a .png filter skips a .txt.")),
                ),
            ),
        ),
    )

    private fun eventGridGossipBoard() = PodcastBoard(
        "az-eventgrid--gossip",
        listOf(
            // 0: "Did you hear what Dana did to the quiz app leaderboard?..."
            Scene(
                0, "Double points",
                DiagramSpec(
                    "The quiz backend posts a quiz finished event, and Event Grid pushes it to the handler that adds the points.",
                    nodes = listOf(
                        DNode("app", "Quiz backend", 0.17f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("grid", "Event Grid", 0.5f, 0.3f, w = 0.32f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("handler", "Points handler", 0.84f, 0.3f, w = 0.3f, h = 0.2f, tone = 1),
                        DNode("note", "Points added twice", 0.5f, 0.8f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("app", "grid", "quiz finished"),
                        DEdge("grid", "handler", "push"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("handler", "note"), light = setOf("note"), card = BoardCard("What Dana did", "Everyone who finished a quiz on Tuesday got double points.")),
                    Beat(2, show = setOf("app", "grid"), light = setOf("app"), pulse = "app" to "grid", card = BoardCard("An event", "A short note: quiz finished, this player, this score.")),
                    Beat(4, light = setOf("grid"), pulse = "grid" to "handler", card = BoardCard("Under the picture", "A small JSON document, routed from where it happened to subscribers.")),
                ),
            ),
            // 5: "So where did Dana go wrong?"
            Scene(
                5, "At least once",
                DiagramSpec(
                    "Event Grid retries for up to 24 hours, so a handler may see an event twice and must be idempotent.",
                    nodes = listOf(
                        DNode("grid", "Event Grid", 0.2f, 0.3f, w = 0.36f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("handler", "Points handler", 0.78f, 0.3f, w = 0.36f, h = 0.22f),
                        DNode("idem", "Idempotent", 0.5f, 0.8f, w = 0.5f, h = 0.22f, kind = NodeKind.NOTE, sub = "twice = once"),
                    ),
                    edges = listOf(
                        DEdge("grid", "handler", "retry up to 24h"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("grid", "handler"), pulse = "grid" to "handler", card = BoardCard("At least once", "No success reply, so it retries for up to 24 hours. Twice is normal.")),
                    Beat(8, show = setOf("idem"), light = setOf("idem", "handler"), card = BoardCard("Idempotent", "Aisha's fix: record each event id, and skip one already handled.")),
                ),
            ),
            // 9: "Nice. And where do these quiz notes get posted, exactly?"
            Scene(
                9, "Noticeboards",
                DiagramSpec(
                    "Sources post events to a topic: a system topic for an Azure service, a custom topic for your own app.",
                    nodes = listOf(
                        DNode("src1", "Storage account", 0.22f, 0.25f, w = 0.4f, h = 0.22f, tone = 1, sub = "system topic"),
                        DNode("src2", "Quiz backend", 0.22f, 0.72f, w = 0.4f, h = 0.22f, tone = 1, sub = "custom topic"),
                        DNode("topic", "Topic", 0.75f, 0.48f, w = 0.42f, h = 0.36f, kind = NodeKind.CLOUD, sub = "a noticeboard"),
                    ),
                    edges = listOf(
                        DEdge("src1", "topic", "BlobCreated"),
                        DEdge("src2", "topic", "quiz finished"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("topic", "src1", "src2"), light = setOf("src1", "src2"), pulse = "src2" to "topic", card = BoardCard("System or custom", "Azure makes system topics. The quiz backend publishes to a custom one.")),
                ),
            ),
            // 11: "And who reads the noticeboard?"
            Scene(
                11, "Routes to handlers",
                DiagramSpec(
                    "An event subscription is a route from a topic to one handler: a Function, a webhook or a queue.",
                    nodes = listOf(
                        DNode("topic", "Topic", 0.17f, 0.5f, w = 0.3f, h = 0.3f, kind = NodeKind.CLOUD),
                        DNode("sub", "Event subscription", 0.5f, 0.5f, w = 0.32f, h = 0.2f, kind = NodeKind.PILL),
                        DNode("h1", "Function", 0.84f, 0.18f, w = 0.3f, tone = 1),
                        DNode("h2", "Webhook", 0.84f, 0.5f, w = 0.3f, tone = 1),
                        DNode("h3", "Queue", 0.84f, 0.82f, w = 0.3f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("topic", "sub"),
                        DEdge("sub", "h1"),
                        DEdge("sub", "h2"),
                        DEdge("sub", "h3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("topic")),
                    Beat(12, show = setOf("sub", "h1", "h2", "h3"), light = setOf("sub"), pulse = "sub" to "h2", card = BoardCard("Event subscription", "A route from a topic to one handler that reacts.")),
                ),
            ),
            // 13: "What did Ben do?"
            Scene(
                13, "Week two: Ben",
                DiagramSpec(
                    "Before trusting a webhook, Event Grid sends a validation code that the webhook must send back.",
                    nodes = listOf(
                        DNode("grid", "Event Grid", 0.2f, 0.35f, w = 0.36f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("hook", "Ben's webhook", 0.78f, 0.35f, w = 0.36f, h = 0.22f),
                        DNode("fn", "Function trigger", 0.5f, 0.82f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, sub = "does it for you"),
                    ),
                    edges = listOf(
                        DEdge("grid", "hook", "validation code", bidirectional = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("grid", "hook"), light = setOf("hook"), card = BoardCard("200 to anything", "The subscription never got validated. Not one event arrived.")),
                    Beat(16, show = setOf("fn"), light = setOf("hook", "fn"), pulse = "grid" to "hook", card = BoardCard("Validation handshake", "Send the code back, like confirming an email. A Function trigger does it.")),
                ),
            ),
            // 19: "Week three?"
            Scene(
                19, "Week three: the intern",
                DiagramSpec(
                    "A subject filter on the subscription lets PNG avatars through and stops the rest before delivery.",
                    nodes = listOf(
                        DNode("png", "avatar.png", 0.2f, 0.14f, w = 0.3f, tone = 1),
                        DNode("txt", "backup.log", 0.8f, 0.14f, w = 0.3f, tone = 2),
                        DNode("filter", "Subject filter", 0.5f, 0.5f, w = 0.56f, h = 0.24f, kind = NodeKind.NOTE, sub = "begins: avatars, ends: .png"),
                        DNode("fn", "Resizer Function", 0.5f, 0.87f, w = 0.42f, h = 0.18f),
                    ),
                    edges = listOf(
                        DEdge("png", "filter"),
                        DEdge("txt", "filter", "never sent", dashed = true),
                        DEdge("filter", "fn"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("txt", "fn"), light = setOf("txt", "fn"), card = BoardCard("A check in the Function", "Still delivered, still wakes the Function, still costs.")),
                    Beat(22, show = setOf("png", "filter"), light = setOf("filter", "png"), pulse = "filter" to "fn", card = BoardCard("Subject filter", "Priya's fix, on the subscription: begins with avatars, ends with .png.")),
                ),
            ),
            // 23: "And the Friday? There's always a Friday."
            Scene(
                23, "Marcus's Friday",
                DiagramSpec(
                    "Thousands of taps a second are a stream for Event Hubs; Event Grid reacts to single events; Service Bus handles careful jobs.",
                    nodes = listOf(
                        DNode("taps", "Every quiz tap", 0.5f, 0.14f, w = 0.44f, h = 0.2f, sub = "thousands a second"),
                        DNode("eg", "Event Grid", 0.18f, 0.62f, w = 0.32f, h = 0.28f, sub = "single events"),
                        DNode("eh", "Event Hubs", 0.5f, 0.62f, w = 0.3f, h = 0.28f, tone = 1, sub = "a stream"),
                        DNode("sb", "Service Bus", 0.82f, 0.62f, w = 0.32f, h = 0.28f, tone = 1, sub = "careful jobs"),
                    ),
                    edges = listOf(
                        DEdge("taps", "eh"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("taps", "eg", "eh", "sb"), light = setOf("eh"), pulse = "taps" to "eh", card = BoardCard("High-volume telemetry", "That's Event Hubs. A prize payout to get right: Service Bus.")),
                ),
            ),
            // 25: "Did anything get lost for good in all this?"
            Scene(
                25, "Lost property",
                DiagramSpec(
                    "Events that still cannot be delivered after the retries are dead-lettered to a storage container.",
                    nodes = listOf(
                        DNode("grid", "Event Grid", 0.2f, 0.3f, w = 0.36f, h = 0.32f, kind = NodeKind.CLOUD),
                        DNode("handler", "Your handler", 0.78f, 0.3f, w = 0.36f, h = 0.24f, tone = 2, sub = "not answering"),
                        DNode("dl", "Dead-letter", 0.5f, 0.78f, w = 0.44f, h = 0.34f, kind = NodeKind.CYLINDER, sub = "a storage container"),
                    ),
                    edges = listOf(
                        DEdge("grid", "handler", "failed", dashed = true),
                        DEdge("grid", "dl", "after retries"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("grid", "handler", "dl"), light = setOf("dl"), pulse = "grid" to "dl", card = BoardCard("Dead-letter", "Instead of being dropped, kept as files, like a lost-property box.")),
                ),
            ),
            // 27: "So, the gossip I should remember for the exam?"
            Scene(
                27, "For the exam",
                DiagramSpec(
                    "The exam's Event Grid questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("react", "Something happened", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Event Grid"),
                        DNode("some", "Only some files", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "subject filter"),
                        DNode("keep", "Keep undelivered", 0.26f, 0.46f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "dead-letter"),
                        DNode("dup", "Duplicates", 0.76f, 0.46f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "idempotent"),
                        DNode("hook", "Webhook", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "validation handshake"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("react", "some", "keep", "dup", "hook"), light = setOf("dup"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(30, card = BoardCard("Today", "Trigger Function, BlobCreated, read the JSON; a .png filter skips a .txt.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy {
        listOf(apimCaseBoard(), apimGossipBoard(), eventGridCaseBoard(), eventGridGossipBoard())
    }
}
