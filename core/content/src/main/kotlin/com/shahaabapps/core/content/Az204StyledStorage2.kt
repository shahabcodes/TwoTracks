package com.shahaabapps.core.content

/** The Storage module's Cosmos DB topics as Case Files and Gossip, with their boards (Az204StyledPodcasts). */
internal object Az204StyledStorage2 {
    private val cosmosModelCase = Podcast(
        "az-cosmos-model",
        listOf(
            maya("Welcome to Case Files. Today's case: The Eight O'Clock Crunch. A small plant-watering app for Android reminds people to water their plants, and they tap a button to log it. Its backend runs on App Service and keeps the logs in Cosmos DB. Every morning at eight, when the reminders go out, half the taps fail. By nine, everything is fine again."),
            leo("[chuckle] A crime that clocks in and clocks out. Before I accuse anyone, what is Cosmos DB, in plain words?"),
            maya("A database that stores JSON documents. A JSON document is a little text record, like a contact card: a name, a plant, a time. Cosmos copies them to any region you pick and answers in a few milliseconds. It's organised like a filing cabinet. The account is the cabinet, a database is a drawer, a container is a folder, and items are the cards."),
            leo("And what does the crime scene show? What error do the failing taps get?"),
            maya("A 429. It means too many requests, slow down. In Cosmos you pay in request units, RU for short. Think of arcade tokens. Reading one small item by its id costs about one token, a query costs more, and a write costs about five per kilobyte. The container was given a fixed number of tokens per second."),
            leo("Then it's open and shut. The app ran out of tokens at eight. Suspect one: not enough RU. Raise the limit and go home."),
            maya("[pause] That's what the developer did. They doubled the RU. The next morning at eight, the 429s came back. Fewer, but still there. And the bill had doubled too."),
            leo("Twice the tokens and still failing? Suspicious. Okay, suspect two. The app ignores the 429s. It should wait and try again."),
            maya("True, and it's a real mistake. Ignoring 429s means a tap is simply lost. The fix is to retry after a short wait, with backoff. But retrying only hides the symptom. It doesn't explain why doubling the RU barely helped."),
            leo("Suspect three, then. Something slows every request down. Somebody set the strictest consistency."),
            maya("Wrong suspect again, but let's look. Cosmos keeps several copies of your data, called replicas. A consistency level is the promise about how fresh a read is. Picture a news board copied to many towns. How quickly does every town see the latest news?"),
            leo("And the strictest one is Strong?"),
            maya("Strong means every read sees the very latest write. It costs the most, and using it everywhere roughly doubles the RU of reads. Then bounded staleness, behind by at most a set number of versions or seconds."),
            leo("And at the relaxed end? Where you'd trade freshness for speed?"),
            maya("Session, where you always see your own writes. Consistent prefix, maybe late but never out of order. And eventual, the cheapest, which catches up in any order."),
            leo("So which did the plant app use?"),
            maya("Session, the default. And here's the clue that rules it out. Consistency is about reads. At eight in the morning, it's the writes that fail. Everyone taps watered at once. So the question isn't how many tokens. It's where the writes land."),
            leo("Where they land? Isn't it just one big container?"),
            maya("Under the surface, no. Every container is split by the value of one property on each item, the partition key. Think of a library sorted onto shelves by one label. Items with the same value sit together on one shelf, called a logical partition. One shelf holds at most twenty gigabytes."),
            leo("And what was this container's label?"),
            maya("The country. [pause] And almost every user of the plant app lives in the same country."),
            leo("[sigh] So every eight o'clock tap goes to the same shelf."),
            maya("Exactly. A hot partition. Here's how it really works. Cosmos runs each key value through a hash, a quick scramble into a number, and that number picks a physical partition, a real slice of servers. The RU is shared evenly between the slices. The hot slice used all of its share while the others sat idle. Doubling the RU mostly paid for more idle tokens."),
            leo("That explains the bill. So the fix is easy. Change the key to something better, in the portal."),
            maya("And that's the last wrong turn. The partition key is fixed when the container is created. You can't change it. You create a new container with a better key, and move the items across."),
            leo("Which key, then?"),
            maya("Three things, all at once. Many different values. Writes spread evenly across them. And it's in the question you ask most. The plant app mostly asks, show me my plants and my logs. So the key is the owner, the user id. Thousands of values, and each tap lands on its own user's shelf."),
            leo("What about date? Lots of dates."),
            maya("Date spreads out over time, but today's shelf gets every new write, which is the eight o'clock problem again. And per-user questions would walk every shelf. A question that names the shelf is cheap. One that doesn't has to ask every shelf, which is slow and expensive."),
            leo("And where does this idea stop working? Say one user has a jungle."),
            maya("That's the edge. One logical partition caps at twenty gigabytes, and lives on one slice with its own limit. A key with too few values hits that wall. Past it, pick a key with more values, or combine two properties into one key. And photos of the plants don't belong in Cosmos at all. They go in blobs. The Blob Storage episode covers that."),
            leo("Case nearly closed. Any other evidence in the file?"),
            maya("Two loose ends. The team wants to bring over an old companion website written for MongoDB. You pick the API when you create the account, the language your code speaks to the database, and it can't be changed. Minimal code changes from MongoDB means the MongoDB API. A brand-new app means NoSQL, the exam's default."),
            leo("And the second?"),
            maya("The bill. The app is busy at eight and nearly silent at noon. Provisioned throughput is a fixed RU per second, paid whether used or not, which suits steady, busy traffic."),
            leo("So they were paying for the eight o'clock rush all day long. What else is there?"),
            maya("Autoscale sets a top limit and drops to ten percent when it's quiet. Serverless charges only for what you use, ideal for a small, spiky app. The team moved to autoscale."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If more RU doesn't stop the 429s, suspect a hot partition from a key with few values. A wrong key means a new container, never a change. The default consistency is Session, and late but never out of order is consistent prefix. Unpredictable traffic means serverless or autoscale. And a 429 means retry with backoff, or more RU."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the shelves. Create a free-tier NoSQL account. Create a container with owner as its partition key. Insert two items with different owners in Data Explorer. Then query the items for one owner, and read the RU charge in the query stats. The Cosmos DB in code episode picks up right there."),
            leo("Watering my plants at eight, on my very own shelf. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val cosmosModelGossip = Podcast(
        "az-cosmos-model",
        listOf(
            maya("Did you hear what Sam did with the gym app? Grab a coffee, because this week was all about Cosmos DB."),
            leo("[laugh] I heard the words hot partition in the kitchen and backed away slowly. What's the gym app?"),
            maya("A little Android app for a local gym. Members book classes and log their workouts. A small backend on App Service keeps everything in Cosmos DB, a database that stores JSON documents, little text records like contact cards. It copies them to any region you pick, and answers in milliseconds."),
            leo("And how is it organised? So the gossip makes sense."),
            maya("Like a filing cabinet. The account is the cabinet, a database is a drawer, a container is a folder, and the items are the cards. Now, Sam made the container for the workout logs, and had to pick a partition key."),
            leo("Which is what, in plain words?"),
            maya("Picture a library sorted onto shelves by one label. The partition key is that label, one property on every item. Items with the same value share one shelf, a logical partition, which holds at most twenty gigabytes. Sam picked the gym branch as the label."),
            leo("Sounds sensible. Is that bad?"),
            maya("The gym has three branches. Three shelves for every workout ever logged. And on Monday at six, when everyone at the main branch logs a workout, one shelf takes nearly every write. A hot partition."),
            leo("[sigh] Poor shelf. So who fixed it?"),
            maya("Priya sat down with Sam. Under the picture, Cosmos runs each key value through a hash, a quick scramble into a number, and that number picks a physical partition, a real slice of servers with its own share of the throughput. Three values can never spread across many slices. A good key has many values, spreads writes evenly, and is in the question you ask most."),
            leo("And the gym app's most common question?"),
            maya("Show me my workouts. So the key is the member id. Thousands of values. A question that names the shelf reads one shelf, cheaply. A question that doesn't has to ask every shelf."),
            leo("So Sam just switched the key over. Easy."),
            maya("That was week two. Aisha tried exactly that. She spent an hour in the portal, looking for the setting to change it."),
            leo("[chuckle] And?"),
            maya("There isn't one. The partition key is fixed when the container is created. Priya showed her the real way: create a new container keyed on member id, and copy the items across. And that's the edge of the idea. One shelf caps at twenty gigabytes, so if one member could ever pass that, you'd combine two properties into one key instead."),
            leo("Week three?"),
            maya("Ben. Ben read about consistency levels, and decided the safest choice was the strictest one. Strong, everywhere."),
            leo("Okay, consistency level. Plain words, please."),
            maya("Cosmos keeps copies of the data, called replicas. The consistency level is the promise about how fresh a read is. Picture a news board copied to many towns: how fast does each town see the news? Strong means always the very latest write. Bounded staleness means behind by at most a set number of versions or seconds."),
            leo("And the rest? Ben's argument was that anything weaker than Strong is just wrong data."),
            maya("Not wrong, just possibly a moment late. Session, the default, means you always see your own writes. Consistent prefix, maybe late but never out of order. And eventual catches up in any order, cheapest and fastest. Ben's Strong roughly doubled the RU of every read, and added delay across regions. Dana switched it back to Session."),
            leo("Wait, RU. You keep saying RU."),
            maya("Request units. Think of arcade tokens. Every action costs some. Reading one small item, about a kilobyte, by its id is about one RU. A query costs more, and a write about five per kilobyte. You buy tokens per second, and when you run out, Cosmos answers with a 429. Too many requests, slow down."),
            leo("Let me guess. Somebody ignored those."),
            maya("The intern, bless them. Monday's 429s were in the logs, and the intern thought, it usually works, so ignore it. But the app didn't retry, and bookings were quietly lost. Marcus fixed it kindly: retry after a short wait, with backoff, and raise the limit if it keeps happening."),
            leo("And how does the gym pay for the tokens?"),
            maya("Three ways. Provisioned is a fixed number per second, paid whether used or not, for steady busy load. Autoscale sets a top limit and drops to ten percent when it's quiet. Serverless charges only for what you use, good for small and spiky. The gym is packed at six and empty at two, so Marcus picked autoscale."),
            leo("Anything else happen? I feel like there's more."),
            maya("One quiet win. Dana is moving the gym's old booking website, written for MongoDB, onto Cosmos. You pick the API when you create the account, the language your code speaks to it, and it's fixed for good. Minimal code changes means the MongoDB API, not NoSQL. And the member photos? Not in Cosmos. The Blob Storage episode covers those."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. A key with few values means a hot partition, and a wrong key means a new container. The default consistency is Session, and late but never out of order is consistent prefix. Strong costs the most. Unpredictable traffic means serverless or autoscale. And a 429 means retry with backoff, or add RU."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a free-tier NoSQL account. Create a container with owner as its partition key. Insert two items with different owners in Data Explorer. Then query the items for one owner, and read the RU charge in the query stats. The Cosmos DB in code episode carries on from there."),
            leo("Many values, and never the branch. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    private val cosmosSdkCase = Podcast(
        "az-cosmos-sdk",
        listOf(
            maya("Welcome to Case Files. Today's case: The Ghost Bikes. A small bike rental app for Android shows a map of bikes you can rent. Its backend on App Service keeps the bikes in Cosmos DB. Last week the team retired twenty broken bikes and deleted them. They're still on the map. People keep walking to empty racks."),
            leo("[chuckle] Haunted bicycles. I love it. How does the map get its bikes?"),
            maya("The map doesn't read the bikes container directly. It reads a small summary, a map view, one document per rack. A function keeps that view up to date by watching the change feed."),
            leo("Change feed. Plain words, please."),
            maya("A running list of every insert and update in a container, in order, per partition. Like the activity log on a shared document. Something reads the list and reacts, usually a function with a Cosmos DB trigger, or the change feed processor in the SDK. The Azure Functions episode covers triggers. Here, each change to a bike updates its rack."),
            leo("Then suspect one: the reads are stale. The map shows old data, so the consistency level is too relaxed."),
            maya("[pause] Wrong suspect. Consistency decides how fresh a read is across the copies, and it catches up within moments. These bikes have been ghosts for a week. And bikes rented out yesterday moved on the map just fine. Only the deleted ones are stuck."),
            leo("Only the deleted ones. [pause] Suspect two: the function crashes on deletes."),
            maya("Warmer. The function never crashed. It never heard about them. Inserts and updates are in the change feed. Deletes are not, unless you turn on a preview mode called all versions and deletes."),
            leo("So the function sat there, waiting for news that never came."),
            maya("Exactly. Here's how the feed really works. Cosmos keeps each partition's writes in order, and the reader asks, what changed since my last checkpoint? A deleted item isn't there to be read, so it's not in the answer. The fix is a soft delete. Instead of deleting a bike, mark it retired, which is an update the feed does carry."),
            leo("But then the retired bikes pile up forever."),
            maya("Not with a TTL, time to live. It's a sell-by date, in seconds. Set a default on the container, or a ttl on each item, and Cosmos removes it for you. The retired bike shows up in the feed as an update, the map drops it, and a day later it's gone for good."),
            leo("Ghosts gone. Case closed?"),
            maya("Not yet. Witness two. Since Monday, the new rides function, the one that totals each user's rides, has missed about half the rides. And the map function has missed about half its updates too."),
            leo("Half each. They're sharing something."),
            maya("[laugh] Now you're a detective. They share one lease container. A lease container is a small container, partitioned by id, that works like a bookmark. It stores checkpoints, how far each reader has got, so no change is handled twice. Two different functions sharing it without a prefix look like two copies of one function, so they split the work."),
            leo("So each one only got part of the changes."),
            maya("Right. Give each function its own prefix, a short name tag on its bookmarks, with LeaseContainerPrefix, or its own lease container. And to rebuild the rides totals, set start from beginning, and every existing item comes through once."),
            leo("Any other clues in the file?"),
            maya("The bill. The backend looks up a bike by running a query, find the bike whose id matches, with no partition key. That asks every partition. When you know the id and the partition key, use a point read, ReadItem. It walks straight to one item, for about one RU."),
            leo("Point read, in plain words?"),
            maya("Knowing the shelf and the exact spot on it. Under the picture, it's a direct fetch of one item by its address, with no query engine involved. The SDK mirrors the hierarchy: a Cosmos client for the account, then a database, then a container. And when you must search, say one user's rides this week, include the partition key in the query."),
            leo("And the last clue?"),
            maya("A pre-trigger. Someone wrote JavaScript that runs inside the database before a write, to stamp the rental start time. The stamps were always empty."),
            leo("Let me guess. Triggers don't fire on their own."),
            maya("Correct. You name the trigger in the request options, or it doesn't run. A post-trigger runs after a write, in the same transaction. A user-defined function runs inside a query. And a stored procedure runs as a transaction, all or nothing, like saving a rental and its payment together. But all of them stop at one partition key. That's their edge."),
            leo("What if two staff edit the same bike at once? One marks it broken, one moves it."),
            maya("Then the ETag. Every item carries one, a version stamp that changes on every write. Send it back with an if-match condition, and if someone saved first, your save fails with a 412. Read again, apply your change again. No lock, which is called optimistic concurrency. And patch changes only the fields you name, while upsert writes or replaces."),
            leo("So, the case summary for the exam?"),
            maya("Four rules and two traps. Cheapest read means a point read by id and partition key. Two writers who mustn't overwrite each other means the ETag with if-match. All or nothing means a stored procedure. React to every write means the change feed, with a lease container. The traps: triggers run only when named, and deletes are not in the feed."),
            leo("Case closed. Anything to try today?"),
            maya("Use the free-tier account from the Cosmos DB episode. Insert an item from dot net with CreateItemAsync. Read it back with ReadItemAsync, and print the RequestCharge. Run the same as a query without the partition key, and compare the charge. Then give an item a ttl of sixty, and watch it vanish, like a ghost bike."),
            leo("The only ghost I'll allow. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val cosmosSdkGossip = Podcast(
        "az-cosmos-sdk",
        listOf(
            maya("Did you hear what Dana did to the quiz app's bill? Grab a coffee. This week, Cosmos DB in code made half the office famous."),
            leo("[laugh] I heard the bill grew an extra digit. What's the quiz app?"),
            maya("A little Android trivia app. Players answer a daily quiz, and a backend on App Service keeps players and scores in Cosmos DB, partitioned by player id. The dot net SDK mirrors the hierarchy: a Cosmos client for the account, then a database, then a container."),
            leo("And Dana?"),
            maya("Every time the app opened, Dana's code loaded the player's profile with a query, find the player whose id matches. No partition key. So every query asked every partition. Ten thousand players, opening the app every morning."),
            leo("Isn't a query how you read things?"),
            maya("When you search, yes. But Dana already knew the id and the partition key. Priya swapped it for a point read, ReadItem by id and partition key. Like knowing the shelf and the exact spot on it. About one RU for a small item. Under the picture, it fetches one item straight from its address, with no query engine at all."),
            leo("And when you really do need to search?"),
            maya("Include the partition key, so it stays on one shelf. Say, one player's answers this week. Dana's bill was back to normal by Friday."),
            leo("[chuckle] Good for Dana. Who's next?"),
            maya("Marcus. Marcus wrote a pre-trigger, JavaScript that runs inside the database before a write, to stamp each answer with the server time. Lovely code. It never ran once."),
            leo("Why not?"),
            maya("Because triggers don't fire on their own. You must name the trigger in the request options, or it doesn't run. Aisha spotted it, and explained the rest of the family. A post-trigger runs after a write, in the same transaction, say to update a counter. A user-defined function runs inside a query, to format a value."),
            leo("And stored procedures? I hear about those in meetings."),
            maya("A stored procedure runs inside one partition, as a transaction. All or nothing. The quiz uses one to save a player's answer and their prize coins together. Both land, or neither. That's also its edge: it only works within one partition key, so it can't change two players at once."),
            leo("Week three?"),
            maya("The intern built the leaderboard. A function watches the change feed and updates a top scores document. The change feed is a running list of every insert and update, in order, per partition, like the activity log on a shared document."),
            leo("Sounds clever. What went wrong?"),
            maya("A player deleted their account, and stayed on the leaderboard. Deletes are not in the change feed, unless you turn on a preview mode, all versions and deletes. Ben showed the intern a soft delete. Mark the player deleted, an update the feed does carry, and set a TTL, time to live, a sell-by date in seconds, so Cosmos removes the item later."),
            leo("And how does the leaderboard function know where it got to?"),
            maya("A lease container. A small container, partitioned by id, that works like a bookmark. Under the picture, the function reads each partition's changes since its last checkpoint, then writes the new checkpoint to the lease container. So two copies of the function never handle the same change."),
            leo("I feel like someone broke the bookmark."),
            maya("Sam did. Sam added a second function, one that sends a well done notification, and pointed it at the same lease container. No prefix. The two functions took turns at the bookmarks, and each saw only part of the changes. The fix is a LeaseContainerPrefix, a short name tag for each function's bookmarks, or a lease container each."),
            leo("And Sam's notifications? Did they ever go out?"),
            maya("[sigh] Oh, they went out. Sam set start from beginning to catch up, and every existing item came through once. So every player got a well done for every quiz they had ever played. Replay is great for rebuilding a leaderboard. Less great for notifications."),
            leo("Is the change feed even the right tool for notifications?"),
            maya("For reacting inside your own app, yes. Past that, when other systems need reliable messages they can queue and retry, the function hands each change on to a queue. The Service Bus episode covers queues, and the Azure Functions episode covers the trigger itself."),
            leo("Anything else? Somebody must have lost some data."),
            maya("Nearly. A player had the quiz open on a phone and a tablet, and both saved progress at once. The second save landed on top of the first. Priya added the ETag. Every item carries one, a version stamp that changes on each write. Send it back with if-match, and if the item moved on, the save fails with a 412. Read again, apply again."),
            leo("So nobody locks anything?"),
            maya("No lock. That's optimistic concurrency. Not a stored procedure, not a stronger consistency level, just the stamp. And for the coins counter, she used patch, which changes only the fields you name. Upsert, for the record, writes or replaces the whole item."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. Cheapest read means a point read by id and partition key. Two writers who mustn't overwrite each other means the ETag with if-match. All or nothing means a stored procedure. React to every write means the change feed, with a lease container. And two traps: triggers run only when named, and no deletes in the feed."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Use the free-tier account from the Cosmos DB episode. Insert an item from dot net with CreateItemAsync. Read it back with ReadItemAsync, and print the RequestCharge. Run the same as a query without the partition key, and compare the charge. Then give an item a ttl of sixty, and watch it vanish."),
            leo("Point reads, prefixes, and no replays on a Friday. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    val podcasts: List<Podcast> = listOf(cosmosModelCase, cosmosModelGossip, cosmosSdkCase, cosmosSdkGossip)

    private fun cosmosModelCaseBoard() = PodcastBoard(
        "az-cosmos-model--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Eight O'Clock Crunch..."
            Scene(
                0, "The Eight O'Clock Crunch",
                DiagramSpec(
                    "The plant app's taps go through its backend to Cosmos DB, and at eight in the morning half of them come back as 429s.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("err", "429: slow down", 0.76f, 0.2f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("db", "Cosmos DB", 0.78f, 0.66f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "a tap"),
                        DEdge("app", "db", "write"),
                        DEdge("db", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "db"), pulse = "phone" to "app", card = BoardCard("The case", "Every morning at eight, half the watering taps fail. By nine, all fine.")),
                    Beat(2, light = setOf("db"), pulse = "app" to "db", card = BoardCard("Cosmos DB", "JSON documents in a cabinet: account, database, container, item.")),
                    Beat(4, show = setOf("err"), light = setOf("err"), pulse = "db" to "err", card = BoardCard("Request units (RU)", "Arcade tokens: about 1 for a 1 KB read by id, about 5 a KB to write.")),
                ),
            ),
            // 5: "Then it's open and shut. The app ran out of tokens at eight..."
            Scene(
                5, "The suspects",
                DiagramSpec(
                    "More RU, missing retries and Strong consistency are all checked; none explains why only the eight o'clock writes fail.",
                    nodes = listOf(
                        DNode("ru", "Not enough RU", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "429s stayed"),
                        DNode("retry", "Ignored 429s", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "retry with backoff"),
                        DNode("strong", "Strong everywhere", 0.5f, 0.74f, w = 0.6f, h = 0.22f, kind = NodeKind.NOTE, sub = "it's the writes that fail"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("ru"), light = setOf("ru")),
                    Beat(6, card = BoardCard("Suspect one: not cleared yet", "Doubled RU: fewer 429s, still there, and twice the bill.")),
                    Beat(7, show = setOf("retry"), light = setOf("retry")),
                    Beat(8, card = BoardCard("A real mistake, not the cause", "Retry after a short wait, with backoff. It only hides the symptom.")),
                    Beat(9, show = setOf("strong"), light = setOf("strong")),
                ),
            ),
            // 10: "Wrong suspect again, but let's look. Cosmos keeps several copies..."
            Scene(
                10, "How fresh is a read",
                DiagramSpec(
                    "Five consistency levels, from Strong (freshest, costliest) to Eventual (cheapest, fastest).",
                    nodes = listOf(
                        DNode("towns", "Copies in many towns", 0.5f, 0.09f, w = 0.6f, h = 0.14f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("strong", "Strong", 0.5f, 0.27f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "always the latest; costs most"),
                        DNode("bounded", "Bounded staleness", 0.5f, 0.42f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "behind by a set amount"),
                        DNode("session", "Session", 0.5f, 0.57f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, sub = "your own writes; the default"),
                        DNode("prefix", "Consistent prefix", 0.5f, 0.72f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "never out of order"),
                        DNode("eventual", "Eventual", 0.5f, 0.87f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "catches up; cheapest"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("towns"), light = setOf("towns"), card = BoardCard("Consistency level", "The promise about how fresh a read is, across the replicas.")),
                    Beat(12, show = setOf("strong", "bounded", "session", "prefix", "eventual"), light = setOf("strong"), card = BoardCard("Strong everywhere?", "Roughly doubles the RU of reads, and adds delay across regions.")),
                    Beat(16, light = setOf("session"), card = BoardCard("Session, the default", "Consistency is about reads. At eight, it's the writes that fail.")),
                ),
            ),
            // 17: "Where they land? Isn't it just one big container?"
            Scene(
                17, "Shelves by one label",
                DiagramSpec(
                    "Items with the same partition key value share a logical partition; keyed on country, every tap lands on one shelf.",
                    nodes = listOf(
                        DNode("q", "The eight o'clock taps", 0.5f, 0.1f, w = 0.6f, h = 0.14f, tone = 1),
                        DNode("p1", "Home country", 0.17f, 0.48f, w = 0.3f, h = 0.28f, sub = "every tap"),
                        DNode("p2", "Another country", 0.5f, 0.48f, w = 0.3f, h = 0.28f, tone = 2, sub = "nearly empty"),
                        DNode("p3", "More countries", 0.83f, 0.48f, w = 0.3f, h = 0.28f, tone = 2, sub = "nearly empty"),
                        DNode("cap", "At most 20 GB a shelf", 0.5f, 0.86f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("q", "p1", "all writes"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("p1", "p2", "p3", "cap"), light = setOf("p1"), card = BoardCard("Partition key", "The shelf label. Same value, same shelf: a logical partition.")),
                    Beat(20, show = setOf("q"), light = setOf("q", "p1"), pulse = "q" to "p1", card = BoardCard("The label: country", "Almost every user of the plant app lives in the same country.")),
                    Beat(21, light = setOf("p1"), card = BoardCard("Hot partition", "Every eight o'clock tap lands on the same shelf.")),
                ),
            ),
            // 22: "Exactly. A hot partition. Here's how it really works..."
            Scene(
                22, "Under the shelves",
                DiagramSpec(
                    "A hash of the key value picks a physical partition, and the RU is shared evenly between the physical partitions.",
                    nodes = listOf(
                        DNode("key", "Key value", 0.2f, 0.18f, w = 0.32f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("hash", "Hash", 0.72f, 0.18f, w = 0.3f, h = 0.18f, tone = 1),
                        DNode("pp1", "Slice 1", 0.17f, 0.58f, w = 0.3f, h = 0.24f, sub = "hot, share used up"),
                        DNode("pp2", "Slice 2", 0.5f, 0.58f, w = 0.3f, h = 0.24f, tone = 2, sub = "idle tokens"),
                        DNode("pp3", "Slice 3", 0.83f, 0.58f, w = 0.3f, h = 0.24f, tone = 2, sub = "idle tokens"),
                        DNode("fixed", "Fixed at creation", 0.5f, 0.9f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("key", "hash"),
                        DEdge("hash", "pp1"),
                        DEdge("hash", "pp2"),
                        DEdge("hash", "pp3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("key", "hash", "pp1", "pp2", "pp3"), light = setOf("pp1"), pulse = "hash" to "pp1", card = BoardCard("Under the picture", "A hash of the key picks a physical partition. The RU is shared evenly.")),
                    Beat(24, show = setOf("fixed"), light = setOf("fixed"), card = BoardCard("Change it in the portal?", "No. Create a new container with a better key, and move the items.")),
                ),
            ),
            // 25: "Which key, then?"
            Scene(
                25, "Choosing a key",
                DiagramSpec(
                    "A good key has many values, spreads writes evenly, and is in the query you run most; one shelf still caps at 20 GB.",
                    nodes = listOf(
                        DNode("owner", "Owner", 0.17f, 0.22f, w = 0.3f, h = 0.24f, sub = "the plant app"),
                        DNode("date", "Date", 0.5f, 0.22f, w = 0.3f, h = 0.24f, tone = 1, sub = "today is hot"),
                        DNode("country", "Country", 0.83f, 0.22f, w = 0.3f, h = 0.24f, tone = 2, sub = "never"),
                        DNode("hot", "Hot partition", 0.83f, 0.6f, w = 0.3f, kind = NodeKind.NOTE),
                        DNode("cap", "20 GB a shelf", 0.3f, 0.6f, w = 0.5f, kind = NodeKind.NOTE, sub = "or combine two properties"),
                        DNode("blob", "Photos: Blob Storage", 0.5f, 0.86f, w = 0.6f, kind = NodeKind.PILL),
                    ),
                    edges = listOf(
                        DEdge("country", "hot"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("owner"), light = setOf("owner"), card = BoardCard("Many, even, in the query", "Show me my plants and my logs: the owner id is the key.")),
                    Beat(28, show = setOf("date", "country", "hot"), light = setOf("hot"), pulse = "country" to "hot", card = BoardCard("Date?", "Today's shelf takes every new write: the eight o'clock problem again.")),
                    Beat(30, show = setOf("cap", "blob"), light = setOf("cap", "blob"), card = BoardCard("The edge", "One shelf caps at 20 GB. Plant photos go to blobs instead.")),
                ),
            ),
            // 31: "Case nearly closed. Any other evidence in the file?"
            Scene(
                31, "Pick the API",
                DiagramSpec(
                    "The API is the language your code speaks to Cosmos, chosen once when the account is created.",
                    nodes = listOf(
                        DNode("acct", "Account", 0.5f, 0.12f, w = 0.4f, h = 0.18f, kind = NodeKind.CYLINDER),
                        DNode("nosql", "NoSQL", 0.17f, 0.5f, w = 0.3f, h = 0.22f, sub = "new apps, default"),
                        DNode("mongo", "MongoDB", 0.5f, 0.5f, w = 0.3f, h = 0.22f, tone = 1, sub = "existing Mongo"),
                        DNode("other", "Postgres and others", 0.83f, 0.5f, w = 0.3f, h = 0.22f, tone = 2, sub = "moving apps in"),
                        DNode("fixed", "Fixed at creation", 0.5f, 0.86f, w = 0.5f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("acct", "nosql"),
                        DEdge("acct", "mongo"),
                        DEdge("acct", "other"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("acct")),
                    Beat(32, show = setOf("nosql", "mongo", "other", "fixed"), light = setOf("mongo", "fixed"), pulse = "acct" to "mongo", card = BoardCard("The old MongoDB website", "Minimal code changes: the MongoDB API. A brand-new app: NoSQL.")),
                ),
            ),
            // 33: "And the second?"
            Scene(
                33, "Buying the tokens",
                DiagramSpec(
                    "Provisioned for steady load, autoscale for a ceiling, serverless to pay per use; a 429 means you ran out.",
                    nodes = listOf(
                        DNode("prov", "Provisioned", 0.17f, 0.22f, w = 0.3f, h = 0.24f, tone = 1, sub = "steady, busy"),
                        DNode("auto", "Autoscale", 0.5f, 0.22f, w = 0.3f, h = 0.24f, sub = "down to 10%"),
                        DNode("serv", "Serverless", 0.83f, 0.22f, w = 0.3f, h = 0.24f, tone = 1, sub = "pay per use"),
                        DNode("app", "Plant app", 0.25f, 0.75f, w = 0.3f, h = 0.2f),
                        DNode("err", "429: slow down", 0.72f, 0.75f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "retry with backoff"),
                    ),
                    edges = listOf(
                        DEdge("err", "app", "too many"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(33, show = setOf("app", "err"), light = setOf("err"), pulse = "err" to "app"),
                    Beat(34, show = setOf("prov", "auto", "serv"), light = setOf("auto"), card = BoardCard("Busy at eight, quiet at noon", "Autoscale: a top limit, down to ten percent. Small and spiky: serverless.")),
                ),
            ),
            // 37: "So, the case summary for the exam?"
            Scene(
                37, "Case closed",
                DiagramSpec(
                    "The exam's Cosmos DB model questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("hot", "More RU, still 429s", 0.26f, 0.1f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "hot partition"),
                        DNode("newc", "A wrong key", 0.76f, 0.1f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "a new container"),
                        DNode("session", "Default consistency", 0.26f, 0.34f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "Session"),
                        DNode("prefix", "Late, never out of order", 0.76f, 0.34f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "consistent prefix"),
                        DNode("serv", "Unpredictable traffic", 0.26f, 0.58f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "serverless, autoscale"),
                        DNode("err", "A 429", 0.76f, 0.58f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "retry, or add RU"),
                        DNode("key", "Partition key", 0.5f, 0.86f, w = 0.7f, h = 0.2f, sub = "many values, in the query, fixed"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(38, show = setOf("hot", "newc", "session", "prefix", "serv", "err", "key"), light = setOf("key"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(40, card = BoardCard("Today", "Free tier, a container keyed on owner, two items, one query's RU charge.")),
                ),
            ),
        ),
    )

    private fun cosmosModelGossipBoard() = PodcastBoard(
        "az-cosmos-model--gossip",
        listOf(
            // 0: "Did you hear what Sam did with the gym app?..."
            Scene(
                0, "The gym app",
                DiagramSpec(
                    "The gym's Android app calls a small backend, which keeps bookings and workouts in Cosmos DB as JSON documents.",
                    nodes = listOf(
                        DNode("phone", "Gym app", 0.22f, 0.2f, w = 0.36f, h = 0.18f, sub = "Android"),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("item", "JSON document", 0.76f, 0.2f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, sub = "a contact card"),
                        DNode("db", "Cosmos DB", 0.78f, 0.66f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "book, log"),
                        DEdge("app", "db"),
                        DEdge("db", "item", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "db"), pulse = "phone" to "app"),
                    Beat(2, show = setOf("item"), light = setOf("item"), pulse = "db" to "item", card = BoardCard("Cosmos DB", "Stores JSON documents, copies them to any region, answers in milliseconds.")),
                    Beat(4, light = setOf("db"), card = BoardCard("Cabinet, drawer, folder, card", "Account, database, container, item.")),
                ),
            ),
            // 5: "Which is what, in plain words?"
            Scene(
                5, "Three shelves",
                DiagramSpec(
                    "Keyed on the gym branch, the container has three logical partitions, and Monday's writes pile onto one.",
                    nodes = listOf(
                        DNode("q", "Monday, six o'clock", 0.5f, 0.1f, w = 0.6f, h = 0.14f, tone = 1),
                        DNode("p1", "Main branch", 0.17f, 0.48f, w = 0.3f, h = 0.28f, sub = "every write"),
                        DNode("p2", "Branch two", 0.5f, 0.48f, w = 0.3f, h = 0.28f, tone = 2, sub = "quiet"),
                        DNode("p3", "Branch three", 0.83f, 0.48f, w = 0.3f, h = 0.28f, tone = 2, sub = "quiet"),
                        DNode("cap", "At most 20 GB a shelf", 0.5f, 0.86f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("q", "p1", "all writes"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("p1", "p2", "p3", "cap"), light = setOf("p1"), card = BoardCard("Partition key", "The shelf label. Same value, same shelf: a logical partition.")),
                    Beat(8, show = setOf("q"), light = setOf("q", "p1"), pulse = "q" to "p1", card = BoardCard("Hot partition", "Three values. One shelf takes nearly every write.")),
                ),
            ),
            // 9: "Poor shelf. So who fixed it?"
            Scene(
                9, "What Priya showed Sam",
                DiagramSpec(
                    "A hash of the key value picks a physical partition with its own share of throughput; few values can't spread.",
                    nodes = listOf(
                        DNode("key", "Key value", 0.2f, 0.18f, w = 0.32f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("hash", "Hash", 0.72f, 0.18f, w = 0.3f, h = 0.18f, tone = 1),
                        DNode("pp1", "Slice 1", 0.17f, 0.58f, w = 0.3f, h = 0.24f, sub = "main branch"),
                        DNode("pp2", "Slice 2", 0.5f, 0.58f, w = 0.3f, h = 0.24f, tone = 2, sub = "idle"),
                        DNode("pp3", "Slice 3", 0.83f, 0.58f, w = 0.3f, h = 0.24f, tone = 2, sub = "idle"),
                        DNode("member", "Key on member id", 0.5f, 0.9f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("key", "hash"),
                        DEdge("hash", "pp1"),
                        DEdge("hash", "pp2"),
                        DEdge("hash", "pp3"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("key", "hash", "pp1", "pp2", "pp3"), light = setOf("pp1"), pulse = "hash" to "pp1", card = BoardCard("Under the picture", "A hash of the key picks a physical partition, each with its own share.")),
                    Beat(12, show = setOf("member"), light = setOf("member"), card = BoardCard("Many, even, in the query", "Show me my workouts: the member id, thousands of values.")),
                ),
            ),
            // 13: "So Sam just switched the key over. Easy."
            Scene(
                13, "Week two: Aisha",
                DiagramSpec(
                    "The partition key is fixed when the container is created; a new container with a better key is the only way.",
                    nodes = listOf(
                        DNode("old", "Keyed on branch", 0.24f, 0.3f, w = 0.42f, h = 0.2f, sub = "key fixed"),
                        DNode("new", "New container", 0.76f, 0.3f, w = 0.42f, h = 0.2f, tone = 1, sub = "keyed on member id"),
                        DNode("portal", "A setting to change it?", 0.5f, 0.78f, w = 0.62f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("old", "new", "copy items"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("old", "portal"), light = setOf("portal"), card = BoardCard("An hour in the portal", "Looking for the setting to change the partition key.")),
                    Beat(16, show = setOf("new"), light = setOf("new"), pulse = "old" to "new", card = BoardCard("Fixed at creation", "Create a new container with a better key, and copy the items across.")),
                ),
            ),
            // 17: "Week three?"
            Scene(
                17, "Week three: Ben",
                DiagramSpec(
                    "Five consistency levels, from Strong (freshest, costliest) to Eventual (cheapest, fastest).",
                    nodes = listOf(
                        DNode("towns", "Copies in many towns", 0.5f, 0.09f, w = 0.6f, h = 0.14f, kind = NodeKind.CLOUD, tone = 2),
                        DNode("strong", "Strong", 0.5f, 0.27f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "always the latest; costs most"),
                        DNode("bounded", "Bounded staleness", 0.5f, 0.42f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "behind by a set amount"),
                        DNode("session", "Session", 0.5f, 0.57f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, sub = "your own writes; the default"),
                        DNode("prefix", "Consistent prefix", 0.5f, 0.72f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "never out of order"),
                        DNode("eventual", "Eventual", 0.5f, 0.87f, w = 0.62f, h = 0.13f, kind = NodeKind.PILL, tone = 1, sub = "catches up; cheapest"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("strong"), light = setOf("strong"), card = BoardCard("Strong, everywhere", "The strictest level, for every single read.")),
                    Beat(20, show = setOf("towns", "bounded"), light = setOf("towns"), card = BoardCard("Consistency level", "The promise about how fresh a read is, across the replicas.")),
                    Beat(22, show = setOf("session", "prefix", "eventual"), light = setOf("session"), card = BoardCard("Back to Session", "The default: a member sees their own booking at once. Strong costs most.")),
                ),
            ),
            // 23: "Wait, RU. You keep saying RU."
            Scene(
                23, "The intern and the 429s",
                DiagramSpec(
                    "Every action costs request units; when they run out, Cosmos answers 429 and the app must retry with backoff.",
                    nodes = listOf(
                        DNode("ru", "Request unit (RU)", 0.5f, 0.14f, w = 0.56f, h = 0.18f, kind = NodeKind.PILL, sub = "an arcade token"),
                        DNode("read", "Read by id", 0.17f, 0.45f, w = 0.3f, h = 0.2f, tone = 1, sub = "about 1 RU"),
                        DNode("query", "Query", 0.5f, 0.45f, w = 0.3f, h = 0.2f, tone = 1, sub = "more"),
                        DNode("write", "Write", 0.83f, 0.45f, w = 0.3f, h = 0.2f, tone = 1, sub = "about 5 a KB"),
                        DNode("app", "Gym app", 0.25f, 0.82f, w = 0.3f, h = 0.18f),
                        DNode("err", "429: slow down", 0.72f, 0.82f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, sub = "retry with backoff"),
                    ),
                    edges = listOf(
                        DEdge("ru", "read"),
                        DEdge("ru", "query"),
                        DEdge("ru", "write"),
                        DEdge("err", "app", "too many"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("ru", "read", "query", "write"), light = setOf("ru", "read"), pulse = "ru" to "read", card = BoardCard("Request units", "Arcade tokens. A 1 KB read by id costs about one.")),
                    Beat(26, show = setOf("app", "err"), light = setOf("err"), pulse = "err" to "app", card = BoardCard("Ignored 429s", "Bookings quietly lost. Retry after a short wait, with backoff.")),
                ),
            ),
            // 27: "And how does the gym pay for the tokens?"
            Scene(
                27, "Buying the tokens",
                DiagramSpec(
                    "Provisioned for steady load, autoscale for a ceiling that drops when quiet, serverless to pay per use.",
                    nodes = listOf(
                        DNode("prov", "Provisioned", 0.17f, 0.3f, w = 0.3f, h = 0.24f, tone = 1, sub = "steady, busy"),
                        DNode("auto", "Autoscale", 0.5f, 0.3f, w = 0.3f, h = 0.24f, sub = "down to 10%"),
                        DNode("serv", "Serverless", 0.83f, 0.3f, w = 0.3f, h = 0.24f, tone = 1, sub = "pay per use"),
                        DNode("gym", "Packed at six, empty at two", 0.5f, 0.78f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("prov", "auto", "serv", "gym"), light = setOf("auto", "gym"), card = BoardCard("Marcus picked autoscale", "A top limit, down to ten percent when quiet. Small and spiky: serverless.")),
                ),
            ),
            // 29: "Anything else happen? I feel like there's more."
            Scene(
                29, "Dana's quiet win",
                DiagramSpec(
                    "The API is the language your code speaks to Cosmos, chosen once when the account is created.",
                    nodes = listOf(
                        DNode("acct", "Account", 0.5f, 0.12f, w = 0.4f, h = 0.18f, kind = NodeKind.CYLINDER),
                        DNode("nosql", "NoSQL", 0.17f, 0.5f, w = 0.3f, h = 0.22f, sub = "new apps, default"),
                        DNode("mongo", "MongoDB", 0.5f, 0.5f, w = 0.3f, h = 0.22f, tone = 1, sub = "existing Mongo"),
                        DNode("other", "Postgres and others", 0.83f, 0.5f, w = 0.3f, h = 0.22f, tone = 2, sub = "moving apps in"),
                        DNode("fixed", "Fixed at creation", 0.5f, 0.86f, w = 0.5f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("acct", "nosql"),
                        DEdge("acct", "mongo"),
                        DEdge("acct", "other"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("acct", "nosql", "mongo", "other", "fixed"), light = setOf("mongo", "fixed"), pulse = "acct" to "mongo", card = BoardCard("A MongoDB website", "Minimal code changes: the MongoDB API. Member photos: Blob Storage.")),
                ),
            ),
            // 31: "So, the gossip I should remember for the exam?"
            Scene(
                31, "For the exam",
                DiagramSpec(
                    "The exam's Cosmos DB model questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("hot", "Few key values", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "hot partition"),
                        DNode("newc", "A wrong key", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "a new container"),
                        DNode("session", "Default consistency", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "Session"),
                        DNode("prefix", "Late, never out of order", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "consistent prefix"),
                        DNode("serv", "Unpredictable traffic", 0.26f, 0.78f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "serverless, autoscale"),
                        DNode("err", "A 429", 0.76f, 0.78f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "retry, or add RU"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("hot", "newc", "session", "prefix", "serv", "err"), light = setOf("hot", "newc"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(34, card = BoardCard("Today", "Free tier, a container keyed on owner, two items, one query's RU charge.")),
                ),
            ),
        ),
    )

    private fun cosmosSdkCaseBoard() = PodcastBoard(
        "az-cosmos-sdk--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Ghost Bikes..."
            Scene(
                0, "The Ghost Bikes",
                DiagramSpec(
                    "The bike rental app's map still shows twenty bikes that were deleted from Cosmos DB a week ago.",
                    nodes = listOf(
                        DNode("phone", "Bike map app", 0.22f, 0.2f, w = 0.36f, h = 0.18f, sub = "Android"),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("ghost", "20 deleted bikes", 0.76f, 0.2f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "still on the map"),
                        DNode("db", "Bikes", 0.78f, 0.66f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "app", "the map"),
                        DEdge("app", "db"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "db", "ghost"), light = setOf("ghost"), pulse = "phone" to "app", card = BoardCard("The case", "Twenty retired bikes, deleted last week, still on the map.")),
                ),
            ),
            // 2: "The map doesn't read the bikes container directly..."
            Scene(
                2, "The map view",
                DiagramSpec(
                    "A function reads the change feed of the bikes container and keeps a map view, one document per rack, up to date.",
                    nodes = listOf(
                        DNode("bikes", "Bikes", 0.17f, 0.2f, w = 0.3f, h = 0.26f, kind = NodeKind.CYLINDER),
                        DNode("feed", "Change feed", 0.5f, 0.2f, w = 0.3f, h = 0.2f, kind = NodeKind.PILL, sub = "inserts, updates"),
                        DNode("func", "Function", 0.83f, 0.2f, w = 0.3f, h = 0.2f, sub = "Cosmos DB trigger"),
                        DNode("view", "Map view", 0.83f, 0.64f, w = 0.3f, h = 0.22f, tone = 1, sub = "one doc a rack"),
                    ),
                    edges = listOf(
                        DEdge("bikes", "feed"),
                        DEdge("feed", "func"),
                        DEdge("func", "view", "update"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("bikes", "func", "view"), light = setOf("view"), pulse = "func" to "view", card = BoardCard("The map view", "One summary document per rack, kept up to date by a function.")),
                    Beat(4, show = setOf("feed"), light = setOf("feed"), pulse = "feed" to "func", card = BoardCard("Change feed", "A running list of inserts and updates, like a document's activity log.")),
                ),
            ),
            // 5: "Then suspect one: the reads are stale..."
            Scene(
                5, "The suspects",
                DiagramSpec(
                    "Stale reads and a crashing function are ruled out: only deleted bikes are stuck, because deletes never reach the feed.",
                    nodes = listOf(
                        DNode("stale", "Stale reads", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "catch up in moments"),
                        DNode("crash", "Function crashed", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "it never heard"),
                        DNode("only", "Only deleted bikes", 0.5f, 0.74f, w = 0.66f, h = 0.22f, sub = "rented bikes moved fine"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("stale"), light = setOf("stale")),
                    Beat(6, show = setOf("only"), light = setOf("only"), card = BoardCard("Suspect one: cleared", "Consistency catches up in moments. These ghosts are a week old.")),
                    Beat(7, show = setOf("crash"), light = setOf("crash")),
                    Beat(8, card = BoardCard("No deletes in the feed", "Inserts and updates only, unless all versions and deletes (preview).")),
                ),
            ),
            // 9: "So the function sat there, waiting for news that never came."
            Scene(
                9, "A soft delete",
                DiagramSpec(
                    "Mark the bike retired, an update the feed carries, and let a TTL remove it later.",
                    nodes = listOf(
                        DNode("bike", "Retired bike", 0.2f, 0.3f, w = 0.34f, h = 0.2f),
                        DNode("feed", "Change feed", 0.78f, 0.3f, w = 0.34f, h = 0.2f, kind = NodeKind.PILL, sub = "an update"),
                        DNode("ttl", "TTL", 0.2f, 0.78f, w = 0.34f, h = 0.18f, kind = NodeKind.PILL, sub = "time to live, seconds"),
                        DNode("gone", "Removed", 0.78f, 0.78f, w = 0.3f, h = 0.18f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("bike", "feed", "marked retired"),
                        DEdge("ttl", "gone", "later"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("bike", "feed"), light = setOf("bike"), pulse = "bike" to "feed", card = BoardCard("Under the picture", "The reader asks what changed since its checkpoint. A deleted item isn't there.")),
                    Beat(12, show = setOf("ttl", "gone"), light = setOf("ttl"), pulse = "ttl" to "gone", card = BoardCard("TTL: time to live", "A sell-by date in seconds. The map drops the bike; a day later it's gone.")),
                ),
            ),
            // 14: "Not yet. Witness two..."
            Scene(
                14, "Witness two: half each",
                DiagramSpec(
                    "Two different functions share one lease container without a prefix, so they split the changes between them.",
                    nodes = listOf(
                        DNode("map", "Map function", 0.2f, 0.2f, w = 0.36f, h = 0.2f),
                        DNode("rides", "Rides function", 0.8f, 0.2f, w = 0.36f, h = 0.2f),
                        DNode("lease", "Lease container", 0.5f, 0.6f, w = 0.36f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 1, sub = "the bookmark"),
                        DNode("prefix", "A prefix each", 0.5f, 0.9f, w = 0.5f, h = 0.12f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("map", "lease"),
                        DEdge("rides", "lease"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("map", "rides"), light = setOf("map", "rides"), card = BoardCard("Half each", "Since Monday, both functions miss about half their changes.")),
                    Beat(16, show = setOf("lease"), light = setOf("lease"), pulse = "rides" to "lease", card = BoardCard("Lease container", "Checkpoints, a bookmark, so no change is handled twice.")),
                    Beat(18, show = setOf("prefix"), light = setOf("prefix"), card = BoardCard("LeaseContainerPrefix", "A name tag on each function's bookmarks. Replay: start from beginning.")),
                ),
            ),
            // 19: "Any other clues in the file?"
            Scene(
                19, "The bill",
                DiagramSpec(
                    "A point read goes straight to one item; a query without the partition key asks every partition.",
                    nodes = listOf(
                        DNode("pr", "Point read", 0.22f, 0.18f, w = 0.4f, h = 0.2f, tone = 1, sub = "id + partition key"),
                        DNode("one", "One shelf", 0.22f, 0.62f, w = 0.32f, h = 0.22f, sub = "about 1 RU"),
                        DNode("qx", "Query, no key", 0.72f, 0.18f, w = 0.48f, h = 0.2f, tone = 1, sub = "asks every shelf"),
                        DNode("a", "P1", 0.57f, 0.62f, w = 0.14f, h = 0.18f, tone = 2),
                        DNode("b", "P2", 0.73f, 0.62f, w = 0.14f, h = 0.18f, tone = 2),
                        DNode("c", "P3", 0.89f, 0.62f, w = 0.14f, h = 0.18f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("pr", "one"),
                        DEdge("qx", "a"),
                        DEdge("qx", "b"),
                        DEdge("qx", "c"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("qx", "a", "b", "c"), light = setOf("qx"), pulse = "qx" to "b", card = BoardCard("Finding a bike by query", "The id matches, but no partition key: it asks every partition.")),
                    Beat(22, show = setOf("pr", "one"), light = setOf("pr"), pulse = "pr" to "one", card = BoardCard("Point read", "ReadItem by id and partition key. A direct fetch, about 1 RU.")),
                ),
            ),
            // 23: "And the last clue?"
            Scene(
                23, "The empty stamps",
                DiagramSpec(
                    "Four kinds of server-side JavaScript, each working inside one partition key; triggers run only when named.",
                    nodes = listOf(
                        DNode("sp", "Stored procedure", 0.26f, 0.16f, w = 0.46f, h = 0.22f, sub = "all or nothing"),
                        DNode("pre", "Pre-trigger", 0.76f, 0.16f, w = 0.44f, h = 0.22f, tone = 1, sub = "before a write"),
                        DNode("post", "Post-trigger", 0.26f, 0.5f, w = 0.46f, h = 0.22f, tone = 1, sub = "after a write"),
                        DNode("udf", "User-defined function", 0.76f, 0.5f, w = 0.44f, h = 0.22f, tone = 1, sub = "inside a query"),
                        DNode("named", "Triggers must be named", 0.5f, 0.85f, w = 0.66f, kind = NodeKind.NOTE, sub = "in the request"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("pre"), light = setOf("pre"), card = BoardCard("A pre-trigger", "Stamps the rental start time before a write. Always empty.")),
                    Beat(26, show = setOf("sp", "post", "udf", "named"), light = setOf("named", "sp"), card = BoardCard("Name the trigger", "In the request options, or it never runs. All stop at one partition key.")),
                ),
            ),
            // 27: "What if two staff edit the same bike at once?..."
            Scene(
                27, "Two staff, one bike",
                DiagramSpec(
                    "Each save sends back the ETag it read; if the bike has changed since, the save fails with 412.",
                    nodes = listOf(
                        DNode("u1", "Staff 1", 0.16f, 0.22f, w = 0.22f, h = 0.24f, kind = NodeKind.USER),
                        DNode("u2", "Staff 2", 0.16f, 0.68f, w = 0.22f, h = 0.24f, kind = NodeKind.USER),
                        DNode("item", "Bike 42", 0.74f, 0.3f, w = 0.36f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "ETag: edition 7"),
                        DNode("err", "412: read again", 0.74f, 0.78f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("u1", "item", "if-match 7"),
                        DEdge("u2", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("u1", "u2", "item"), light = setOf("item")),
                    Beat(28, show = setOf("err"), light = setOf("err"), pulse = "u1" to "item", card = BoardCard("ETag with if-match", "Save only if it's still the edition you read; otherwise 412. No lock.")),
                ),
            ),
            // 29: "So, the case summary for the exam?"
            Scene(
                29, "Case closed",
                DiagramSpec(
                    "The exam's Cosmos DB code questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("cheap", "Cheapest read", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "point read"),
                        DNode("race", "Two writers", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "ETag, if-match"),
                        DNode("atomic", "All or nothing", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "stored procedure"),
                        DNode("react", "React to every write", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "change feed, lease"),
                        DNode("traps", "Traps", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "name triggers; no deletes in the feed"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("cheap", "race", "atomic", "react", "traps"), light = setOf("traps"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(32, card = BoardCard("Today", "Insert, point read with RequestCharge, a query without the key, a ttl of 60.")),
                ),
            ),
        ),
    )

    private fun cosmosSdkGossipBoard() = PodcastBoard(
        "az-cosmos-sdk--gossip",
        listOf(
            // 0: "Did you hear what Dana did to the quiz app's bill?..."
            Scene(
                0, "Dana's bill",
                DiagramSpec(
                    "The quiz app's backend keeps players and scores in Cosmos DB; the SDK mirrors the account, database and container.",
                    nodes = listOf(
                        DNode("phone", "Quiz app", 0.22f, 0.2f, w = 0.36f, h = 0.18f, sub = "Android"),
                        DNode("app", "Backend", 0.22f, 0.66f, w = 0.32f, h = 0.2f, sub = "App Service"),
                        DNode("sdk", "Client, database, container", 0.72f, 0.2f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, sub = "the SDK"),
                        DNode("db", "Players", 0.78f, 0.66f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "by player id"),
                    ),
                    edges = listOf(
                        DEdge("phone", "app"),
                        DEdge("app", "db"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "app", "db"), pulse = "phone" to "app", card = BoardCard("Dana's bill", "An extra digit on the quiz app's Cosmos DB bill.")),
                    Beat(2, show = setOf("sdk"), light = setOf("sdk"), pulse = "app" to "db", card = BoardCard("C#", "client.GetContainer(\"quiz\", \"players\")", code = true)),
                ),
            ),
            // 3: "And Dana?"
            Scene(
                3, "Query or point read",
                DiagramSpec(
                    "A point read goes straight to one item; a query without the partition key asks every partition.",
                    nodes = listOf(
                        DNode("pr", "Point read", 0.22f, 0.18f, w = 0.4f, h = 0.2f, tone = 1, sub = "id + partition key"),
                        DNode("one", "One shelf", 0.22f, 0.62f, w = 0.32f, h = 0.22f, sub = "about 1 RU"),
                        DNode("qx", "Query, no key", 0.72f, 0.18f, w = 0.48f, h = 0.2f, tone = 1, sub = "asks every shelf"),
                        DNode("a", "P1", 0.57f, 0.62f, w = 0.14f, h = 0.18f, tone = 2),
                        DNode("b", "P2", 0.73f, 0.62f, w = 0.14f, h = 0.18f, tone = 2),
                        DNode("c", "P3", 0.89f, 0.62f, w = 0.14f, h = 0.18f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("pr", "one"),
                        DEdge("qx", "a"),
                        DEdge("qx", "b"),
                        DEdge("qx", "c"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("qx", "a", "b", "c"), light = setOf("qx"), pulse = "qx" to "a", card = BoardCard("A query for a known id", "No partition key: every query asks every partition.")),
                    Beat(6, show = setOf("pr", "one"), light = setOf("pr"), pulse = "pr" to "one", card = BoardCard("Point read", "ReadItem by id and partition key: a direct fetch, about 1 RU.")),
                    Beat(8, light = setOf("one"), card = BoardCard("Must search? Include the key", "One player's answers this week stay on one shelf.")),
                ),
            ),
            // 9: "Good for Dana. Who's next?"
            Scene(
                9, "Marcus and the trigger",
                DiagramSpec(
                    "Four kinds of server-side JavaScript, each working inside one partition key; triggers run only when named.",
                    nodes = listOf(
                        DNode("sp", "Stored procedure", 0.26f, 0.16f, w = 0.46f, h = 0.22f, sub = "all or nothing"),
                        DNode("pre", "Pre-trigger", 0.76f, 0.16f, w = 0.44f, h = 0.22f, tone = 1, sub = "before a write"),
                        DNode("post", "Post-trigger", 0.26f, 0.5f, w = 0.46f, h = 0.22f, tone = 1, sub = "after a write"),
                        DNode("udf", "User-defined function", 0.76f, 0.5f, w = 0.44f, h = 0.22f, tone = 1, sub = "inside a query"),
                        DNode("named", "Triggers must be named", 0.5f, 0.85f, w = 0.66f, kind = NodeKind.NOTE, sub = "in the request"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("pre"), light = setOf("pre"), card = BoardCard("A pre-trigger", "Stamps each answer with the server time. It never ran once.")),
                    Beat(12, show = setOf("post", "udf", "named"), light = setOf("named"), card = BoardCard("Name the trigger", "In the request options, or it doesn't run.")),
                    Beat(14, show = setOf("sp"), light = setOf("sp"), card = BoardCard("Stored procedure", "Answer and prize coins: both or neither, inside one partition key.")),
                ),
            ),
            // 15: "Week three?"
            Scene(
                15, "The intern's leaderboard",
                DiagramSpec(
                    "A function reads the change feed and updates the top scores; deletes never reach it, so mark players deleted instead.",
                    nodes = listOf(
                        DNode("scores", "Scores", 0.17f, 0.2f, w = 0.3f, h = 0.26f, kind = NodeKind.CYLINDER),
                        DNode("feed", "Change feed", 0.5f, 0.2f, w = 0.3f, h = 0.2f, kind = NodeKind.PILL, sub = "inserts, updates"),
                        DNode("func", "Function", 0.83f, 0.2f, w = 0.3f, h = 0.2f, sub = "Cosmos DB trigger"),
                        DNode("nodel", "No deletes", 0.17f, 0.62f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "soft delete, TTL"),
                        DNode("top", "Top scores", 0.83f, 0.62f, w = 0.3f, h = 0.2f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("scores", "feed"),
                        DEdge("feed", "func"),
                        DEdge("func", "top", "update"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("scores", "feed", "func", "top"), light = setOf("feed"), pulse = "feed" to "func", card = BoardCard("Change feed", "Every insert and update, in order, per partition.")),
                    Beat(18, show = setOf("nodel"), light = setOf("nodel"), card = BoardCard("A deleted player stayed", "No deletes in the feed. Mark deleted, an update, and set a TTL.")),
                ),
            ),
            // 19: "And how does the leaderboard function know where it got to?"
            Scene(
                19, "The bookmark",
                DiagramSpec(
                    "Each function saves checkpoints in the lease container; two functions sharing it need a prefix each.",
                    nodes = listOf(
                        DNode("f1", "Leaderboard", 0.2f, 0.2f, w = 0.36f, h = 0.2f, sub = "function"),
                        DNode("f2", "Notifications", 0.8f, 0.2f, w = 0.36f, h = 0.2f, sub = "Sam's function"),
                        DNode("lease", "Lease container", 0.5f, 0.6f, w = 0.36f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 1, sub = "the bookmark"),
                        DNode("prefix", "A prefix each", 0.5f, 0.9f, w = 0.5f, h = 0.12f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("f1", "lease", "checkpoint"),
                        DEdge("f2", "lease"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("f1", "lease"), light = setOf("lease"), pulse = "f1" to "lease", card = BoardCard("Under the picture", "Read each partition's changes since the checkpoint; save the new one.")),
                    Beat(22, show = setOf("f2", "prefix"), light = setOf("f2", "prefix"), pulse = "f2" to "lease", card = BoardCard("No prefix", "Two functions took turns at the bookmarks. Fix: LeaseContainerPrefix.")),
                    Beat(24, light = setOf("f2"), card = BoardCard("Start from beginning", "Every existing item comes through once. Every player: well done.")),
                ),
            ),
            // 25: "Is the change feed even the right tool for notifications?"
            Scene(
                25, "Where the feed stops",
                DiagramSpec(
                    "The change feed is for your own app reacting to writes; when other systems need reliable messages, hand each change to a queue.",
                    nodes = listOf(
                        DNode("feed", "Change feed", 0.17f, 0.3f, w = 0.3f, h = 0.2f, kind = NodeKind.PILL),
                        DNode("func", "Function", 0.5f, 0.3f, w = 0.28f, h = 0.2f, sub = "Cosmos trigger"),
                        DNode("queue", "Queue", 0.83f, 0.3f, w = 0.28f, h = 0.2f, tone = 1, sub = "Service Bus"),
                        DNode("other", "Other systems", 0.83f, 0.76f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("feed", "func"),
                        DEdge("func", "queue", "hands on"),
                        DEdge("queue", "other"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("feed", "func"), light = setOf("feed"), pulse = "feed" to "func"),
                    Beat(26, show = setOf("queue", "other"), light = setOf("queue"), pulse = "func" to "queue", card = BoardCard("Past the edge", "Other systems need messages? Hand each change to a queue: Service Bus.")),
                ),
            ),
            // 27: "Anything else? Somebody must have lost some data."
            Scene(
                27, "Phone and tablet",
                DiagramSpec(
                    "Each save sends back the ETag it read; if the progress has changed since, the save fails with 412.",
                    nodes = listOf(
                        DNode("u1", "Phone", 0.16f, 0.22f, w = 0.22f, h = 0.24f, kind = NodeKind.USER),
                        DNode("u2", "Tablet", 0.16f, 0.68f, w = 0.22f, h = 0.24f, kind = NodeKind.USER),
                        DNode("item", "Progress", 0.74f, 0.3f, w = 0.36f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "ETag: edition 7"),
                        DNode("err", "412: read again", 0.74f, 0.78f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("u1", "item", "if-match 7"),
                        DEdge("u2", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("u1", "u2", "item", "err"), light = setOf("err"), pulse = "u1" to "item", card = BoardCard("ETag with if-match", "Save only if it's still the edition you read. Otherwise 412.")),
                    Beat(30, light = setOf("item"), card = BoardCard("Optimistic concurrency", "No lock. Patch changes named fields; upsert writes or replaces.")),
                ),
            ),
            // 31: "So, the gossip I should remember for the exam?"
            Scene(
                31, "For the exam",
                DiagramSpec(
                    "The exam's Cosmos DB code questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("cheap", "Cheapest read", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "point read"),
                        DNode("race", "Two writers", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "ETag, if-match"),
                        DNode("atomic", "All or nothing", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "stored procedure"),
                        DNode("react", "React to every write", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "change feed, lease"),
                        DNode("traps", "Traps", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "name triggers; no deletes in the feed"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("cheap", "race", "atomic", "react", "traps"), light = setOf("traps"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(34, card = BoardCard("Today", "Insert, point read with RequestCharge, a query without the key, a ttl of 60.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy {
        listOf(cosmosModelCaseBoard(), cosmosModelGossipBoard(), cosmosSdkCaseBoard(), cosmosSdkGossipBoard())
    }
}
