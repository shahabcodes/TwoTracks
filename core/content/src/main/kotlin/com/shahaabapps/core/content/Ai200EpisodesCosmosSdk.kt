package com.shahaabapps.core.content

/** Cosmos DB for NoSQL in Python: connect and query: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesCosmosSdk {
    private val classic = Podcast(
        "ai-cosmos-sdk",
        listOf(
            maya("Hi, and welcome back. Zaiqa is up and running. Our API, the .NET service the phone talks to, lives in Container Apps, and we can read the logs when a release will not start. Today: where the data goes."),
            leo("I thought that was settled. Orders are in Azure SQL."),
            maya("And they stay there. Restaurants, menus, users, orders, payments: all in Azure SQL, because money needs real transactions. Today is about two things SQL is bad at."),
            leo("Which two?"),
            maya("The order event feed, every small status change: paid, accepted, cooking, picked up, delivered. And rider pings, each rider's position, every three seconds, all evening. For example, five thousand riders at eight on a Friday."),
            leo("Why not just add a table? A pings table with a time column."),
            maya("On a quiet Tuesday that works. But that is six million rows an hour, nobody ever updates one, and nobody joins them to payments. You would be paying for transactions and foreign keys you never use."),
            leo("Fair. So where do they go?"),
            maya("Azure Cosmos DB for NoSQL. It swallows enormous write rates and does not mind records that differ from each other. The line to remember: SQL holds what must be correct, Cosmos holds what must be fast and huge."),
            leo("Records that differ how?"),
            maya("A Cosmos record is JSON, a plain-text format of labelled values, like a filled-in form. A paid event carries an amount. A picked-up event carries a rider name instead. Neither has to match the other."),
            leo("And one of those records is called?"),
            maya("An item. One event is one item. Items sit in a container, containers sit in a database, and the database sits in an account. Picture a building, a room in it, and a filing cabinet in the room."),
            leo("So what cabinets does Zaiqa get?"),
            maya("Two today. One called order events, one called rider pings. Both in a database called zaiqa, inside one Cosmos account."),
            leo("And the catch?"),
            maya("Every container must declare a partition key when you create it. That is one property whose value decides which slice of the storage an item lives in. Think of the cabinet as drawers, and the key picks the drawer."),
            leo("Which property, for the events?"),
            maya("The order id. Every event for one order lands in the same drawer, so the tracking screen opens one drawer and reads the lot. For the pings, the rider id."),
            leo("And a bad choice would be?"),
            maya("Something with few values. Say you're building the pings keyed on city, and you serve four cities. Four drawers take everything, one of them is Mumbai on a Friday, and that drawer is on fire while the other three nap."),
            leo("So: many different values, and the thing most reads filter on."),
            maya("That is the whole rule. And every call costs request units, or RUs. An RU is the price tag on one operation, and the next episode is all about them. Calls that stay in one drawer are the cheap ones."),
            leo("Right. Now, our order side is .NET. Why are we writing Python?"),
            maya("Because Zaiqa's Taste service is Python, the one that answers, something like dal makhani but vegan. It owns these two containers, and reads them for the tracking screen. The library is a package called azure-cosmos."),
            leo("And the password goes where?"),
            maya("Nowhere. Cosmos will hand out account keys, master passwords for the whole account, and we are not using one. We use DefaultAzureCredential, from a second package, azure-identity."),
            leo("What does that thing actually do?"),
            maya("It goes looking for a sign-in. On your laptop it finds your az login. In Container Apps it finds the service's managed identity, the badge Azure gives your app, the same badge that pulls our images."),
            leo("Same badge, another door. How much code?"),
            maya("Three lines. Make a CosmosClient with the account address and that credential, ask it for the database, ask that for the container. Make it once when the service starts, like one Retrofit instance in your Android app."),
            leo("Once, not per request. Then I can do anything?"),
            maya("Only what your role allows, and Cosmos splits roles in two. A data-plane role covers the data itself: reading items, writing items, running queries. Ours is called Cosmos DB Built-in Data Contributor."),
            leo("And the other half?"),
            maya("The control plane: creating the account, the database, the container. Those are different roles entirely, and most apps should never hold them."),
            leo("Why would I care, if I only read and write?"),
            maya("Because of a forbidden error that catches everybody. A tidy start-up line that says create database if not exists, running with a data-plane role, fails with 403, and the message says nothing about roles."),
            leo("[sigh] And the fix?"),
            maya("Make the database and the containers ahead of time, with the az command line or a template, and let the code only open them."),
            leo("Good. Now let me write a ping."),
            maya("Call upsert item. It inserts the item, or replaces the one already there with that id and key. One call, no checking first, no two code paths."),
            leo("And reading one back?"),
            maya("If you know both halves of the item's identity, the id and the partition key value, call read item. That is a point read: Cosmos walks straight to the one item, with no query engine. About one RU for a small item."),
            leo("Why both halves?"),
            maya("Because the identity is the pair. The same id can sit in two drawers and mean two different things. Hand Cosmos the id alone and it does not know which drawer to open."),
            leo("And when I do not know the id?"),
            maya("Then you query. Cosmos speaks a SQL-like language: select, from, where. Ask for one order's events in time order, and pass the partition key with it. That makes it a single-partition query: one drawer, cheap."),
            leo("And if a diner types into the search box, that text goes in the where clause?"),
            maya("Never directly. Pass it as a parameter, a named blank in the query text that is filled in separately. Otherwise a diner types a quotation mark and a scrap of query, and their words become part of your command. That is injection."),
            leo("Blanks on a form. What if I cannot scope it to one drawer?"),
            maya("Then it is a cross-partition query. It fans out to every partition, gathers the answers and costs far more. Our nightly report on how long orders took is one, run once, off peak. The tracking screen never is."),
            leo("And a long answer? A rider's whole evening is thousands of pings."),
            maya("You page it. Ask for pages of fifty, and each page comes back with a continuation token: a bookmark you cannot read, but Cosmos can. The app hands it back later, and the query carries on where it stopped."),
            leo("Better than remembering the last id."),
            maya("Much better. An id cannot say how far a fan-out had got across every partition. And one more call: patch item, which changes named fields in place without sending the whole item back."),
            leo("Where would Zaiqa use that?"),
            maya("The order's live summary. Replace the status, add one to a counter. [pause] Two small instructions on the wire instead of a whole document."),
            leo("One word I keep seeing: consistency."),
            maya("A consistency level means how fresh a read has to be, and fresher costs more RUs and more waiting. The default is Session, which promises you always read your own writes. The next episode gives it properly."),
            leo("Fair enough. So what do I remember for the exam?"),
            maya("If you know the id and the partition key value, a point read: the cheapest call there is. If start-up gets forbidden while creating a database, that is a data-plane role, so make it with az instead. If user text goes into a query, parameters, never a pasted string."),
            leo("Keep going."),
            maya("If the next page must come later, in a new request, that is the continuation token. If the filter is not the partition key, it is a cross-partition query, and it costs more. And if a key has four values, pick one with many."),
            leo("And anything to try today?"),
            maya("Create a serverless account, a database and a container partitioned on the user id. Give yourself Built-in Data Contributor and sign in with az login. Upsert three notes for two users, then point-read one. Then run a parameterized single-partition query and a cross-partition one, and compare."),
            leo("SQL for what must be correct, Cosmos for what must be fast and huge. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-cosmos-sdk",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Rider Who Froze. Eight on a Friday, and on thousands of Zaiqa phones the little scooter on the tracking map stopped dead outside the restaurant, while the food was already at the door."),
            leo("Detective Leo, reporting. Zaiqa's orders live in Azure SQL, and that part was fine, was it not? Receipts went out, payments took."),
            maya("Perfect, all evening. Orders, order lines, payments: Azure SQL did its job. The frozen scooter is a different store. Two months ago we put two things into Azure Cosmos DB for NoSQL."),
            leo("Which two, and why not just a table?"),
            maya("The order event feed, every status change: paid, accepted, cooking, picked up. And rider pings, each rider's position every three seconds. Five thousand riders on a Friday is six million rows an hour that nobody updates and nobody joins to payments."),
            leo("So SQL holds what must be correct, Cosmos holds what must be fast and huge. Describe the scene for me."),
            maya("A Cosmos record is JSON, a plain-text format of labelled values, like a filled-in form, and one record is called an item. Items sit in a container, containers in a database, the database in an account."),
            leo("Evidence bag one. What did the tracking screen actually ask for?"),
            maya("One rider's last position. And here is the first clue. It asked with a query that filtered on the rider id, and it did not pass the partition key."),
            leo("Partition key. Plain words, for the record."),
            maya("One property you choose when you make a container, whose value decides which slice of the storage an item lives in. Think of the container as a filing cabinet, and the key as picking the drawer."),
            leo("Then surely filtering on the rider id is the same thing?"),
            maya("Only if the rider id is the key, and only if you say so. Clue two: that container's key was city. Four cities, four drawers, and Mumbai's drawer taking most of a Friday night."),
            leo("[sigh] So one drawer on fire and three asleep."),
            maya("And because the filter was not the key, every read became a cross-partition query: it fans out to every partition, gathers the answers, and costs far more than one drawer would."),
            leo("Costs. In money, or in time?"),
            maya("Both. Every call spends request units, or RUs. An RU is the price tag on one operation. A fan-out at dinner rush drained the budget, the calls slowed, and the little scooter stopped moving."),
            leo("Then the fix is a bigger budget."),
            maya("That is the junior's answer, and it buys you a month. The real fix is two things: key the pings on the rider id, so one rider is one drawer, and pass that key on every read."),
            leo("Keys with many values, and reads that stay in one drawer. What about the event feed? Same illness?"),
            maya("Nearly. The feed is keyed on the order id, which is right. But the tracking screen fetched one known event with a query that said where the id equals, and that is the third clue."),
            leo("What should it have said?"),
            maya("Nothing at all. When you know both halves of an item's identity, the id and the partition key value, you call read item. That is a point read: Cosmos goes straight to the one item, no query engine, about one RU."),
            leo("Both halves. Why?"),
            maya("Because the identity is the pair. The same id can sit in two drawers and mean two different things. Give Cosmos the id alone and it does not know which drawer to open."),
            leo("Noted. Now, the next file. There is an incident from the week the service first went live, and I want it closed too."),
            maya("The service would not start at all. Its start-up code said create database if not exists, and every attempt came back forbidden."),
            leo("Someone forgot the password."),
            maya("There is no password, and that is the point. The Taste service, our Python one, signs in with DefaultAzureCredential: on a laptop that is your az login, and in Container Apps it is the service's managed identity, the badge Azure gives your app."),
            leo("So the badge was missing."),
            maya("The badge was there, holding a data-plane role: Cosmos DB Built-in Data Contributor. That covers the data itself, reading and writing items and running queries. Making a database is the control plane, a different set of roles."),
            leo("[chuckle] So the role could do the work, but not build the furniture."),
            maya("Exactly. We made the database and the containers once, with the az command line, and the start-up code now only opens them. It started first time."),
            leo("Case nearly closed. Anything still open?"),
            maya("Two things. The search box, where a diner types, something like dal makhani. Our first version pasted that text straight into the query."),
            leo("And a diner with a sense of humour types a quotation mark and a scrap of query."),
            maya("And their words become part of our command. That is injection. Now it goes in as a parameter, a named blank in the query text that is filled in separately."),
            leo("And the second?"),
            maya("A rider's whole evening is thousands of pings, and the first version asked for all of them at once. Now it asks for pages of fifty, and each page comes with a continuation token: a bookmark you cannot read, but Cosmos can."),
            leo("Which the app hands back for the next page."),
            maya("Later, in a brand new request, and the query carries on exactly where it stopped. An id could not do that: it cannot say how far a fan-out had got across every partition."),
            leo("And writing? Does the rider app check before it saves?"),
            maya("No. It calls upsert item, which inserts the item, or replaces the one already there with that id and key. And for a small change, patch item, which edits named fields without sending the whole item."),
            leo("For example?"),
            maya("The order's live summary. Replace the status, add one to a counter. Two small instructions on the wire instead of a whole document."),
            leo("One last thing on the report: consistency."),
            maya("A consistency level means how fresh a read must be, and fresher costs more RUs and more waiting. Session is the default, and it promises you always read your own writes. The performance episode takes it apart properly."),
            leo("So what goes in the summary for the exam?"),
            maya("If you know the id and the partition key value, a point read, the cheapest call there is. If a filter is not the partition key, it is a cross-partition query, and it costs more. If a key has four values, pick one with many."),
            leo("And the forbidden one?"),
            maya("Forbidden while creating a database means a data-plane role: make it with az instead. User text in a query means parameters. And a page fetched later, in a new request, means the continuation token."),
            leo("Case closed. Anything to try today?"),
            maya("Create a serverless account, a database and a container partitioned on the user id. Give yourself Built-in Data Contributor and sign in with az login. Upsert three notes for two users, then point-read one. Then run a parameterized single-partition query and a cross-partition one, and compare."),
            leo("One drawer, one call, no fan-out. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-cosmos-sdk",
        listOf(
            maya("Okay, did you hear what the Zaiqa team did to the tracking screen this week? Sit down. They finally moved the rider pings and the order event feed out of Azure SQL and into Cosmos DB, and it was quite a week."),
            leo("I heard the words fan-out shouted across the office. Start at the beginning. Why move anything? Orders are in Azure SQL and nobody complains."),
            maya("And orders stay there. Restaurants, menus, users, orders, payments: all Azure SQL, because money needs real transactions. The trouble was the other two. Every status change, and each rider's position every three seconds."),
            leo("How much is that, really?"),
            maya("Five thousand riders on a Friday is six million rows an hour. Nobody updates one, nobody joins them to payments. Priya put it best: SQL holds what must be correct, Cosmos holds what must be fast and huge."),
            leo("Fine. So who went first?"),
            maya("Tom. Monday. Tom added a pings table to Azure SQL anyway, with a time column, because, quote, it is just a table."),
            leo("[chuckle] And by Friday?"),
            maya("The nightly backup took four hours and the finance report timed out. Tom took it well. Mostly. Azure Cosmos DB for NoSQL swallows that write rate without blinking."),
            leo("Because it stores what, exactly?"),
            maya("Records of JSON, a plain-text format of labelled values, like a filled-in form. One record is an item. Items sit in a container, containers in a database, the database in an account."),
            leo("Right. Tuesday."),
            maya("Dana. Dana wrote the Taste service's start-up code, and it said create database if not exists. Every start came back forbidden, and Dana spent a morning convinced the password was wrong."),
            leo("Was it?"),
            maya("There is no password. The service signs in with DefaultAzureCredential: your az login on a laptop, and in Container Apps the service's managed identity, the badge Azure gives your app."),
            leo("So what was forbidden?"),
            maya("The badge held a data-plane role, Cosmos DB Built-in Data Contributor. That covers the data itself: reading items, writing items, running queries. Creating a database is the control plane, a different set of roles."),
            leo("So what should Dana have done?"),
            maya("What she did by lunch. Made the database and the two containers with the az command line, once, and left the code to open them."),
            leo("Wednesday. I sense something worse."),
            maya("Marcus. Marcus created the pings container, and had to choose its partition key: one property whose value decides which slice of the storage an item lives in. Picture a filing cabinet, and the key picks the drawer."),
            leo("And Marcus picked?"),
            maya("City. Four cities, four drawers, and Mumbai's drawer taking most of a Friday evening. One drawer on fire, three asleep."),
            leo("What should it have been?"),
            maya("The rider id. Thousands of values, and it is what every read filters on. One rider, one drawer. The event feed got the order id, which Priya had already got right."),
            leo("Thursday. Go on."),
            maya("The intern. The new search box lets a diner type, something like dal makhani but vegan, and the intern pasted that text straight into the query."),
            leo("[laugh] And someone typed a quotation mark."),
            maya("A tester did, with a scrap of query after it, and their words became part of our command. That is injection. It goes in as a parameter now, a named blank in the query text, filled in separately."),
            leo("Friday. Please tell me Friday was quiet."),
            maya("Friday was Priya, and it was the expensive one. The tracking screen fetched one known event with a query that said where the id equals, without the key."),
            leo("And that costs more than what?"),
            maya("Than a point read. When you know both halves of an item's identity, the id and the partition key value, you call read item, and Cosmos goes straight to the one item. About one RU, and an RU is the price tag on one operation."),
            leo("And why does Cosmos want both halves?"),
            maya("Because the identity is the pair. The same id can sit in two drawers and mean two different things. Hand Cosmos the id alone and it does not know which drawer to open."),
            leo("And what did Priya's query actually cost?"),
            maya("Without the key it was a cross-partition query: it fans out to every partition, gathers the answers, and costs far more. At eight on a Friday the bill climbed and the screen slowed to a crawl."),
            leo("Was that the fan-out I heard shouted about?"),
            maya("That was it. And one more from Priya's week. The rider history screen asked for an entire evening of pings in one go."),
            leo("Thousands of them."),
            maya("Now it asks for pages of fifty, and each page comes with a continuation token: a bookmark you cannot read, but Cosmos can. The app hands it back later, in a new request, and the query carries on where it stopped."),
            leo("Did anything actually go right?"),
            maya("Plenty. Writing a ping is upsert item: it inserts the item, or replaces the one already there with that id and key. And the live order summary uses patch item, which changes named fields without sending the whole item."),
            leo("For example?"),
            maya("Replace the status, add one to a counter. Two small instructions on the wire instead of a whole document. [pause] Dana's work, and nobody has had to touch it."),
            leo("And the argument I heard in the kitchen about consistency?"),
            maya("A consistency level means how fresh a read must be, and fresher costs more RUs and more waiting. Session is the default, and it promises you read your own writes. That argument is next episode's problem."),
            leo("So what is the gossip I should remember for the exam?"),
            maya("If you know the id and the partition key value, a point read, the cheapest call there is. If the filter is not the partition key, it is a cross-partition query, and it costs more. And a key with four values is Marcus's mistake: pick one with many."),
            leo("And Dana's, and the intern's?"),
            maya("Forbidden while creating a database means a data-plane role, so make it with az instead. User text in a query means parameters. And a page fetched later, in a new request, means the continuation token."),
            leo("And something to try today, so I am not next week's story?"),
            maya("Create a serverless account, a database and a container partitioned on the user id. Give yourself Built-in Data Contributor and sign in with az login. Upsert three notes for two users, then point-read one. Then run a parameterized single-partition query and a cross-partition one, and compare."),
            leo("No fan-outs at dinner rush. Got it, Priya. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-cosmos-sdk",
        listOf(
            nova("Hey, you. It is late, the tea has gone cold, and it is just the three of us. Me, Ivy, and you, still up. [pause] Tonight: the part of Zaiqa that never sleeps."),
            ivy("The riders. Of course it is the riders. Go on, Nova."),
            nova("Zaiqa's orders already live in Azure SQL, and they are staying there. Restaurants, menus, users, orders, payments. Money needs real transactions, and SQL is very good at that. [pause] Tonight is about two things it is bad at."),
            ivy("Two things. Tell me slowly."),
            nova("The order event feed, every small status change: paid, accepted, cooking, picked up. And the rider pings, each rider's position, every three seconds, all evening long."),
            ivy("And that is a lot of rows."),
            nova("Five thousand riders on a Friday is six million an hour. Nobody ever updates one. Nobody joins one to a payment. [pause] So they go somewhere else: Azure Cosmos DB for NoSQL."),
            ivy("Which holds what, exactly?"),
            nova("Records of JSON, a plain-text format of labelled values, like a filled-in form. One record is an item. Items live in a container, containers in a database, the database in an account. A cabinet, a room, a building."),
            ivy("And the line I am supposed to remember?"),
            nova("SQL holds what must be correct. Cosmos holds what must be fast and huge. [pause] Say it to yourself on the way to bed."),
            ivy("Mm. And where inside the cabinet does one little ping land?"),
            nova("In a drawer. Every container declares a partition key: that is one property whose value decides which slice of the storage an item lives in. For the pings, the rider id."),
            ivy("So one rider, one drawer. And if you choose badly?"),
            nova("Pick something with four values, like the city, and four drawers take everything. Mumbai's burns all evening while three others nap. [pause] Many values, and the thing your reads filter on. That is the rule."),
            ivy("Noted. Now, who does the talking? Your backend is .NET, is it not?"),
            nova("The order side is, and it keeps its orders in SQL. But Zaiqa's Taste service is Python, the one that answers, something like dal makhani but vegan, and it owns these containers. The package is azure-cosmos."),
            ivy("And the password. There is always a password at this hour."),
            nova("Not tonight. Cosmos will hand out account keys, master passwords for the whole account, and we do not take one. We use DefaultAzureCredential instead."),
            ivy("Which finds what?"),
            nova("A sign-in, wherever it is standing. On your laptop, your az login. In Container Apps, the service's managed identity: the badge Azure gives your app, with no secret to lose."),
            ivy("And once it is through the door, can it do as it pleases?"),
            nova("[pause] Only what its role allows. A data-plane role covers the data itself: reading items, writing items, running queries. Ours is Cosmos DB Built-in Data Contributor."),
            ivy("And the things it cannot do?"),
            nova("Make an account, a database or a container. Those belong to the control plane, and they are different roles. Which is why a tidy start-up line that says create database if not exists comes back forbidden."),
            ivy("[sigh] At two in the morning, I imagine."),
            nova("Always. Make the database and the containers once with the az command line, and let the code only open them. [pause] Then it starts first time."),
            ivy("Right. Now save me a round trip."),
            nova("Upsert item. It inserts the item, or replaces the one already there with that id and key. One call, no checking first."),
            ivy("And bringing one back?"),
            nova("If you know both halves of the item's identity, the id and the partition key value, that's read item. A point read: Cosmos walks straight to the one item, no query engine at all, about one request unit."),
            ivy("A request unit being?"),
            nova("The price tag on one operation. Everything you do spends RUs, and the next late night is all about them. [pause] Cheap calls stay in one drawer."),
            ivy("And when I do not know the id?"),
            nova("You query. Cosmos speaks a SQL-like language: select, from, where. Ask for one order's events in time order and pass the partition key along, and it is a single-partition query. One drawer, cheap."),
            ivy("And if I forget to pass it?"),
            nova("Then it is a cross-partition query. It fans out to every partition, gathers the answers, and costs far more. [pause] Fine for the nightly report. Not at eight on a Friday."),
            ivy("And the search box, where someone types their heart's desire?"),
            nova("That text never goes into the query text. It goes in as a parameter, a named blank filled in separately. Otherwise a quotation mark and a scrap of query turn their words into your command. That is injection."),
            ivy("Sneaky. Now, a rider's whole evening is thousands of pings. Do I get them all at once?"),
            nova("You page them. Fifty at a time, and each page comes back with a continuation token: a bookmark you cannot read, but Cosmos can. Hand it back later, in a new request, and the query carries on exactly where it stopped."),
            ivy("Better than remembering the last one I saw."),
            nova("Much. An id cannot say how far a fan-out had got across every partition. [pause] And there is patch item, which changes named fields without sending the whole item back: replace the status, add one to a counter."),
            ivy("Put the whole thing together for me, softly."),
            nova("One Cosmos account. A container of order events keyed on the order id, a container of rider pings keyed on the rider id. The Python service signs in with its badge, point-reads what it knows, queries one drawer when it does not, and pages the long answers."),
            ivy("And where does it stop working?"),
            nova("Where correctness lives. A refund across three tables is a SQL job, not a Cosmos one. [pause] And a consistency level, meaning how fresh a read must be, costs more RUs the fresher you want it. Session is the default: you always read your own writes."),
            ivy("Mm. Before I drift off. What do I remember for the exam?"),
            nova("Id and partition key known: a point read, the cheapest call there is. A filter that is not the partition key: a cross-partition query, and it costs more. A key with four values: pick one with many."),
            ivy("And the two that catch people?"),
            nova("Forbidden while creating a database is a data-plane role, so make it with az instead. User text in a query goes in as parameters. And the next page, later, in a new request, is the continuation token."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a serverless account, a database and a container partitioned on the user id. Give yourself Built-in Data Contributor and sign in with az login. [pause] Upsert three notes for two users, then point-read one, then run a parameterized single-partition query and a cross-partition one, and compare."),
            ivy("Correct over here, fast and huge over there. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-cosmos-sdk",
        listOf(
            nova("Tonight is date night, darling. Candles, something with chilli in it, and you and me talking about where Zaiqa puts the things it cannot keep in a tidy table."),
            rafe("You spoil me. Orders are in Azure SQL, are they not? Restaurants, menus, users, payments. All very correct."),
            nova("Very correct, and staying there. Money needs real transactions across several tables, love. But two things do not fit: the order event feed, and the rider pings."),
            rafe("Pings. Every three seconds, all evening."),
            nova("Five thousand riders on a Friday is six million records an hour. Nobody updates one, nobody joins it to a payment. So quiz me, and be strict: where do they go?"),
            rafe("Azure Cosmos DB for NoSQL. Enormous write rates, spread across the world, and happy with records that do not match each other."),
            nova("Right answer. Correct over there, fast and huge over here. [kiss]"),
            rafe("And a Cosmos record is?"),
            nova("JSON, a plain-text format of labelled values, like a filled-in form. One record is an item. Items live in a container, containers in a database, the database in an account."),
            rafe("A cabinet in a room in a building. And every cabinet has drawers."),
            nova("It does, and you choose how they are divided. Every container declares a partition key: that is one property whose value decides which slice of the storage an item lives in."),
            rafe("Then my turn to be strict. Exam style, darling. The rider pings container is keyed on city, we serve four cities, and on Friday one drawer takes almost everything. What went wrong, and what fixes it?"),
            nova("Four values is far too few, so Mumbai's drawer burns while three nap. Key it on the rider id instead: thousands of values, and it is what every read filters on. One rider, one drawer."),
            rafe("Right answer, and quick with it. [kiss]"),
            nova("Mm. The event feed is keyed on the order id, for the same reason. Now, who does the talking? Not the .NET side tonight."),
            rafe("The Taste service. Our Python one, the one that answers, something like dal makhani but vegan."),
            nova("That one. Its package is azure-cosmos. And here is a gentle question: how does it get in? Cosmos will give out account keys, master passwords for the whole account."),
            rafe("We do not take keys. DefaultAzureCredential: your az login on a laptop, and in Container Apps the service's managed identity, the badge Azure gives your app."),
            nova("Right answer. No secrets between us. [kiss]"),
            rafe("Now I will be difficult. The service starts up, says create database if not exists, and gets forbidden. Whose fault?"),
            nova("Nobody's, really. The badge holds a data-plane role, Cosmos DB Built-in Data Contributor, which covers the data itself: reading items, writing items, running queries. Making a database is the control plane, a different set of roles."),
            rafe("And the fix, before the starter arrives?"),
            nova("Make the database and the containers once with the az command line, and let the code only open them."),
            rafe("Right answer. Build the house before you move in. [kiss]"),
            nova("Your turn again. Writing a ping: which call, and why do you not check first?"),
            rafe("Upsert item. It inserts the item, or replaces the one already there with that id and key. One call, one code path."),
            nova("Mm, decisive. And reading one back, when you know the id and the key?"),
            rafe("A query, with where the id equals."),
            nova("[sigh] No kiss for that one. When you know both halves of the identity, you call read item. That is a point read: Cosmos goes straight to the one item, no query engine, about one request unit. An RU is the price tag on one operation."),
            rafe("Both halves. Because the same id can sit in two drawers."),
            nova("And mean two different things. Give Cosmos the id alone and it does not know which drawer to open."),
            rafe("Let me earn that one back. You query when you do not know the id, Cosmos speaks a SQL-like language, and you pass the partition key so it stays a single-partition query. One drawer, cheap."),
            nova("Right answer, and a lovely recovery. [kiss]"),
            rafe("And without the key?"),
            nova("A cross-partition query. It fans out to every partition, gathers the answers, and costs far more. Fine for the nightly report on how long orders took. Never on the tracking screen at eight on a Friday."),
            rafe("And the search box, where a diner types whatever they like?"),
            nova("Never into the query text, darling. It goes in as a parameter, a named blank filled in separately. Otherwise a quotation mark and a scrap of query turn their words into our command. That is injection."),
            rafe("Last one from me, and it is a real exam question. A rider's evening is thousands of pings. The screen shows fifty, and the next fifty must come later, in a brand new request. What does the app keep?"),
            nova("The continuation token: a bookmark you cannot read, but Cosmos can. Hand it back and the query carries on exactly where it stopped. An id cannot say how far a fan-out had got across every partition."),
            rafe("Right answer. Always knowing where we left off. [kiss]"),
            nova("And one more call for free: patch item, which changes named fields without sending the whole item. Replace the status, add one to a counter."),
            rafe("Two small instructions instead of the whole document. Very economical. And the thing people argue about, consistency?"),
            nova("A consistency level means how fresh a read must be, and fresher costs more RUs and more waiting. Session is the default, and it promises you always read your own writes. The next date covers it properly."),
            rafe("Then the whole household, put together."),
            nova("Orders and payments in Azure SQL. Order events keyed on the order id, rider pings keyed on the rider id, both in Cosmos. The Python service signs in with its badge, point-reads what it knows, keeps to one drawer when it can, and pages the long answers."),
            rafe("And what do we remember for the exam, love?"),
            nova("Id and partition key known: a point read. A filter that is not the partition key: a cross-partition query, and it costs more. A key with four values: pick one with many."),
            rafe("That is three. The two that caught us?"),
            nova("Forbidden while creating a database is a data-plane role, so make it with az. And user text in a query goes in as parameters, never pasted."),
            rafe("Five for five. That earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a serverless account, a database and a container partitioned on the user id. Give yourself Built-in Data Contributor and sign in with az login. Upsert three notes for two users, then point-read one, then run a parameterized single-partition query and a cross-partition one, and compare."),
            nova("Correct in one place, fast and huge in the other. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun stores() = DiagramSpec(
        "Orders and payments stay in Azure SQL; the order event feed and the rider pings go into Cosmos DB.",
        nodes = listOf(
            DNode("sql", "Azure SQL", 0.26f, 0.18f, w = 0.46f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 1, sub = "orders, payments"),
            DNode("cosmos", "Cosmos DB", 0.76f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2, sub = "fast and huge"),
            DNode("events", "Order events", 0.26f, 0.76f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "paid, cooking, picked up"),
            DNode("pings", "Rider pings", 0.76f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "every three seconds"),
        ),
        edges = listOf(
            DEdge("cosmos", "events", "container"),
            DEdge("cosmos", "pings", "container"),
        ),
        aspect = 0.8f,
    )

    private fun frozen() = DiagramSpec(
        "The tracking screen asked the Taste service for one rider's position, and the read fanned out to every drawer.",
        nodes = listOf(
            DNode("phone", "Zaiqa app", 0.22f, 0.18f, w = 0.38f, h = 0.2f, sub = "tracking map"),
            DNode("sql", "Azure SQL", 0.76f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 1, sub = "orders, payments"),
            DNode("taste", "Taste service", 0.22f, 0.72f, w = 0.38f, h = 0.2f, sub = "Python, reads"),
            DNode("pings", "Rider pings", 0.76f, 0.72f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 2, sub = "keyed on city"),
        ),
        edges = listOf(
            DEdge("phone", "taste", "asks"),
            DEdge("taste", "pings", "fans out"),
            DEdge("phone", "sql", "orders: fine", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun hierarchy() = DiagramSpec(
        "The Taste service signs in, then works down from account to database to container, which holds the items.",
        nodes = listOf(
            DNode("app", "Taste service", 0.22f, 0.18f, w = 0.42f, h = 0.2f, tone = 1, sub = "azure-cosmos"),
            DNode("acct", "Account", 0.72f, 0.14f, w = 0.46f, h = 0.18f, tone = 2, sub = "the building"),
            DNode("db", "Database: zaiqa", 0.72f, 0.42f, w = 0.46f, h = 0.18f, sub = "the room"),
            DNode("cont", "Container: events", 0.72f, 0.74f, w = 0.46f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "key: order id"),
            DNode("item", "Item", 0.22f, 0.74f, w = 0.38f, h = 0.24f, kind = NodeKind.NOTE, sub = "one event as JSON"),
        ),
        edges = listOf(
            DEdge("app", "acct", "signs in"),
            DEdge("acct", "db"),
            DEdge("db", "cont"),
            DEdge("cont", "item", "holds"),
        ),
        aspect = 0.8f,
    )

    private fun drawers() = DiagramSpec(
        "The partition key's value picks the drawer an item lives in: keyed on the order id, one order is one drawer.",
        nodes = listOf(
            DNode("key", "Partition key", 0.5f, 0.16f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "order id"),
            DNode("d1", "Drawer 8-8-1", 0.17f, 0.72f, w = 0.3f, h = 0.26f, sub = "one order"),
            DNode("d2", "Drawer 8-8-2", 0.5f, 0.72f, w = 0.3f, h = 0.26f, sub = "one order"),
            DNode("d3", "Drawer 8-8-3", 0.83f, 0.72f, w = 0.3f, h = 0.26f, sub = "one order"),
        ),
        edges = listOf(
            DEdge("key", "d1"),
            DEdge("key", "d2"),
            DEdge("key", "d3"),
        ),
        aspect = 0.8f,
    )

    private fun signin() = DiagramSpec(
        "No account key: DefaultAzureCredential uses the managed identity, Azure gives it a short pass, Cosmos checks its role.",
        nodes = listOf(
            DNode("app", "Taste service", 0.26f, 0.16f, w = 0.46f, h = 0.2f, tone = 1, sub = "DefaultAzureCredential"),
            DNode("entra", "Azure sign-in", 0.76f, 0.16f, w = 0.42f, h = 0.2f, tone = 2, sub = "a short-lived pass"),
            DNode("mi", "Managed identity", 0.26f, 0.5f, w = 0.46f, h = 0.2f, sub = "a badge, no password"),
            DNode("cosmos", "Cosmos DB", 0.76f, 0.5f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "checks the role"),
            DNode("key", "Account key", 0.5f, 0.86f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "a master password: not used"),
        ),
        edges = listOf(
            DEdge("app", "mi", "uses"),
            DEdge("mi", "entra", "pass"),
            DEdge("app", "cosmos", "with the pass"),
        ),
        aspect = 0.8f,
    )

    private fun roles() = DiagramSpec(
        "A data-plane role reads and writes items but cannot create databases or containers; make those ahead of time.",
        nodes = listOf(
            DNode("dp", "Data-plane role", 0.26f, 0.18f, w = 0.46f, h = 0.2f, tone = 1, sub = "Data Contributor"),
            DNode("items", "Items", 0.76f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "read and write"),
            DNode("create", "Create database", 0.26f, 0.76f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "403, forbidden"),
            DNode("cli", "az or a template", 0.76f, 0.76f, w = 0.42f, h = 0.2f, sub = "ahead of time"),
        ),
        edges = listOf(
            DEdge("dp", "items", "allowed"),
            DEdge("dp", "create", "refused", dashed = true),
            DEdge("cli", "create", "does it"),
        ),
        aspect = 0.8f,
    )

    private fun reads() = DiagramSpec(
        "A point read goes straight to one item; a query in one drawer is cheaper than one that fans out to every partition.",
        nodes = listOf(
            DNode("cont", "Container", 0.5f, 0.14f, w = 0.5f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "order events"),
            DNode("pr", "Point read", 0.26f, 0.5f, w = 0.46f, h = 0.2f, sub = "id and key, one RU"),
            DNode("q1", "Single-partition", 0.76f, 0.5f, w = 0.44f, h = 0.2f, sub = "one drawer"),
            DNode("qx", "Cross-partition query", 0.5f, 0.85f, w = 0.6f, h = 0.2f, tone = 2, sub = "every drawer, more RUs"),
        ),
        edges = listOf(
            DEdge("cont", "pr"),
            DEdge("cont", "q1"),
            DEdge("cont", "qx"),
        ),
        aspect = 0.8f,
    )

    private fun query() = DiagramSpec(
        "A diner's text goes into a query as a parameter, filled in separately, never pasted into the query text.",
        nodes = listOf(
            DNode("search", "Search box", 0.26f, 0.16f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "the diner's text"),
            DNode("fstr", "Pasted in", 0.76f, 0.16f, w = 0.42f, h = 0.2f, tone = 2, sub = "injection"),
            DNode("params", "Parameters", 0.26f, 0.76f, w = 0.46f, h = 0.2f, tone = 1, sub = "filled in separately"),
            DNode("sql", "Query text", 0.76f, 0.76f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "stays the same"),
        ),
        edges = listOf(
            DEdge("search", "params"),
            DEdge("search", "fstr", dashed = true),
            DEdge("params", "sql"),
        ),
        aspect = 0.8f,
    )

    private fun paging() = DiagramSpec(
        "Each page comes with a continuation token; the app hands it back later and the query resumes where it stopped.",
        nodes = listOf(
            DNode("page1", "Page 1", 0.26f, 0.18f, w = 0.46f, h = 0.2f, sub = "fifty pings"),
            DNode("token", "Continuation token", 0.76f, 0.18f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "a bookmark"),
            DNode("app", "The app", 0.76f, 0.76f, w = 0.44f, h = 0.2f, kind = NodeKind.USER),
            DNode("page2", "Page 2, later", 0.26f, 0.76f, w = 0.46f, h = 0.2f, sub = "resumes there"),
        ),
        edges = listOf(
            DEdge("page1", "token"),
            DEdge("token", "app", "kept"),
            DEdge("app", "page2", "hands it back"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Cosmos DB SDK questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("pr", "Id and key known", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a point read"),
            DNode("role", "403 creating a database", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "az or a template"),
            DNode("prm", "Diner text in a query", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "parameters"),
            DNode("tok", "Next page, later", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "continuation token"),
            DNode("cross", "Filter off the key", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "cross-partition"),
            DNode("pk", "Four key values", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "pick the rider id"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("pr", "role", "prm", "tok", "cross", "pk")
    private val today = BoardCard("Today", "Serverless account, Data Contributor, upsert three, point-read, two queries.")
    private val pointCard = BoardCard("Point read", "Id plus partition key: straight to one item, about one RU.")
    private val upsertCard = BoardCard("Upsert item", "Inserts the item, or replaces the one with that id and key.")
    private val paramCard = BoardCard("Parameters", "A named blank in the query text, filled in separately.")
    private val halvesCard = BoardCard("Both halves", "The same id can sit in two drawers and mean two things.")
    private val patchCard = BoardCard("Patch item", "Changes named fields: replace the status, add one to a counter.")

    private fun classicBoard() = PodcastBoard(
        "ai-cosmos-sdk",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is up and running..."
            Scene(
                0, "Orders are already in SQL", stores(),
                beats = listOf(
                    Beat(0, show = setOf("sql"), light = setOf("sql"), card = BoardCard("Today", "Orders stay in Azure SQL. Today: what goes in Cosmos, and how we write it.")),
                    Beat(2, light = setOf("sql"), card = BoardCard("Azure SQL", "Restaurants, menus, users, orders, payments: money needs transactions.")),
                    Beat(4, show = setOf("events", "pings"), light = setOf("events", "pings"), card = BoardCard("What SQL is bad at", "The order event feed, and each rider's position every three seconds.")),
                    Beat(6, light = setOf("pings"), card = BoardCard("Six million rows an hour", "Nobody updates one, and nobody joins them to payments.")),
                    Beat(8, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "cosmos" to "pings", card = BoardCard("Cosmos DB for NoSQL", "Enormous write rates, worldwide, and records that differ from each other.")),
                ),
            ),
            // 9: "Records that differ how?"
            Scene(
                9, "What Cosmos holds", hierarchy(),
                beats = listOf(
                    Beat(10, show = setOf("item"), light = setOf("item"), card = BoardCard("JSON item", "A plain-text form of labelled values. One event is one item.")),
                    Beat(12, show = setOf("app", "acct", "db", "cont"), light = setOf("acct", "db", "cont"), pulse = "db" to "cont", card = BoardCard("Account, database, container", "A building, a room in it, and a filing cabinet in the room.")),
                    Beat(14, light = setOf("cont"), pulse = "cont" to "item", card = BoardCard("Zaiqa's two containers", "Order events, and rider pings, in a database called zaiqa.")),
                ),
            ),
            // 16: "Every container must declare a partition key..."
            Scene(
                16, "One drawer per order", drawers(),
                beats = listOf(
                    Beat(16, show = setOf("key", "d1"), light = setOf("key", "d1"), pulse = "key" to "d1", card = BoardCard("Partition key", "One property whose value picks the drawer an item lives in.")),
                    Beat(18, show = setOf("d2", "d3"), light = setOf("d1"), card = BoardCard("Keyed on the order id", "Every event for one order lands in one drawer. Pings: the rider id.")),
                    Beat(20, light = setOf("key"), card = BoardCard("A bad key", "City: four values, so one drawer burns and three nap.")),
                    Beat(22, light = setOf("d1"), card = BoardCard("Request units", "RUs: the price tag on one call. One drawer is the cheap one.")),
                ),
            ),
            // 23: "Right. Now, our order side is .NET..."
            Scene(
                23, "Signing in", signin(),
                beats = listOf(
                    Beat(24, show = setOf("app"), light = setOf("app"), card = BoardCard("The Taste service", "Zaiqa's Python service owns these containers. Package: azure-cosmos.")),
                    Beat(26, show = setOf("key"), light = setOf("key"), card = BoardCard("No account key", "A master password for the whole account. We do not use one.")),
                    Beat(28, show = setOf("mi", "entra"), light = setOf("mi"), pulse = "app" to "mi", card = BoardCard("Managed identity", "az login on a laptop; in Azure, the badge Azure gives your app.")),
                    Beat(30, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "app" to "cosmos", card = BoardCard("Three lines", "The client once, then the database client, then the container client.")),
                ),
            ),
            // 31: "Once, not per request. Then I can do anything?"
            Scene(
                31, "What the role allows", roles(),
                beats = listOf(
                    Beat(32, show = setOf("dp", "items"), light = setOf("dp", "items"), pulse = "dp" to "items", card = BoardCard("Data-plane role", "Built-in Data Contributor: read items, write items, run queries.")),
                    Beat(34, show = setOf("create"), light = setOf("create"), card = BoardCard("The control plane", "Making the account, the database, the container. Different roles.")),
                    Beat(36, light = setOf("create"), pulse = "dp" to "create", card = BoardCard("Forbidden at start-up", "Create database if not exists, with a data-plane role, fails.")),
                    Beat(38, show = setOf("cli"), light = setOf("cli"), pulse = "cli" to "create", card = BoardCard("The fix", "Make the database and the containers with az, ahead of time.")),
                ),
            ),
            // 39: "Good. Now let me write a ping."
            Scene(
                39, "Save and read", reads(),
                beats = listOf(
                    Beat(40, show = setOf("cont"), light = setOf("cont"), card = upsertCard),
                    Beat(42, show = setOf("pr"), light = setOf("pr"), pulse = "cont" to "pr", card = pointCard),
                    Beat(44, light = setOf("pr"), card = halvesCard),
                    Beat(46, show = setOf("q1"), light = setOf("q1"), pulse = "cont" to "q1", card = BoardCard("Single-partition query", "Select, from, where, plus the partition key. One drawer, cheap.")),
                ),
            ),
            // 47: "And if a diner types into the search box..."
            Scene(
                47, "Never paste it in", query(),
                beats = listOf(
                    Beat(47, show = setOf("search", "fstr", "params", "sql"), light = setOf("search", "fstr"), pulse = "search" to "fstr", card = BoardCard("The search box", "A diner types: something like dal makhani, but vegan.")),
                    Beat(48, light = setOf("params"), pulse = "search" to "params", card = paramCard),
                ),
            ),
            // 50: "Then it is a cross-partition query..."
            Scene(
                50, "One drawer, or every drawer", reads(),
                beats = listOf(
                    Beat(50, show = setOf("cont", "qx"), light = setOf("qx"), pulse = "cont" to "qx", card = BoardCard("Cross-partition query", "No partition key: it fans out to every drawer and costs far more.")),
                ),
            ),
            // 51: "And a long answer? A rider's whole evening..."
            Scene(
                51, "A bookmark", paging(),
                beats = listOf(
                    Beat(52, show = setOf("page1", "token"), light = setOf("token"), pulse = "page1" to "token", card = BoardCard("Continuation token", "A bookmark with each page of fifty. Cosmos reads it; you cannot.")),
                    Beat(54, show = setOf("app", "page2"), light = setOf("page2"), pulse = "app" to "page2", card = BoardCard("Later, in a new request", "The query carries on exactly where it stopped.")),
                    Beat(56, light = setOf("page1"), card = patchCard),
                    Beat(58, light = setOf("page2"), card = BoardCard("Consistency, next time", "How fresh a read must be. Session: you read your own writes.")),
                ),
            ),
            // 59: "Fair enough. So what do I remember for the exam?"
            Scene(
                59, "For the exam", exam(),
                beats = listOf(
                    Beat(60, show = all6, light = setOf("pr", "role", "prm"), card = BoardCard("Listen for the phrase", "A point read, a data-plane role, parameters: one answer each.")),
                    Beat(62, light = setOf("tok", "cross", "pk"), card = BoardCard("And three more", "The continuation token, cross-partition, a key with many values.")),
                    Beat(64, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-cosmos-sdk--case-files",
        listOf(
            // 0: "Welcome to Case Files. Tonight's case: The Rider Who Froze..."
            Scene(
                0, "The Rider Who Froze", frozen(),
                beats = listOf(
                    Beat(0, show = setOf("phone"), light = setOf("phone"), card = BoardCard("The case", "The scooter on the tracking map froze while the food was at the door.")),
                    Beat(2, show = setOf("sql"), light = setOf("sql"), pulse = "phone" to "sql", card = BoardCard("Azure SQL was fine", "Orders, order lines, payments: correct all evening.")),
                    Beat(4, show = setOf("pings"), light = setOf("pings"), card = BoardCard("Six million rows an hour", "A rider's position every three seconds, and nobody updating one.")),
                    Beat(6, show = setOf("taste"), light = setOf("taste"), pulse = "phone" to "taste", card = BoardCard("JSON items", "A plain-text form of labelled values. Items sit in a container.")),
                ),
            ),
            // 8: "One rider's last position. And here is the first clue..."
            Scene(
                8, "Clue one: no key passed", drawers(),
                beats = listOf(
                    Beat(8, show = setOf("key"), light = setOf("key"), card = BoardCard("Clue one", "The query filtered on the rider id, without the partition key.")),
                    Beat(10, show = setOf("d1"), light = setOf("d1"), pulse = "key" to "d1", card = BoardCard("Partition key", "One property whose value picks the drawer an item lives in.")),
                    Beat(12, show = setOf("d2", "d3"), light = setOf("d2", "d3"), card = BoardCard("Clue two: keyed on city", "Four cities, four drawers, and Mumbai's taking a Friday night.")),
                    Beat(14, light = setOf("d1", "d2", "d3"), card = BoardCard("Cross-partition query", "The filter was not the key, so every read fanned out.")),
                ),
            ),
            // 16: "Both. Every call spends request units..."
            Scene(
                16, "What it cost", reads(),
                beats = listOf(
                    Beat(16, show = setOf("cont", "qx"), light = setOf("qx"), pulse = "cont" to "qx", card = BoardCard("Request units", "RUs: the price tag on one call. A fan-out drains them.")),
                    Beat(18, light = setOf("qx"), card = BoardCard("The real fix", "Key the pings on the rider id, and pass the key on every read.")),
                    Beat(20, light = setOf("cont"), card = BoardCard("The feed is keyed right", "Order id. But it fetched a known event with a query.")),
                    Beat(22, show = setOf("pr"), light = setOf("pr"), pulse = "cont" to "pr", card = pointCard),
                ),
            ),
            // 25: "Noted. Now, the next file..."
            Scene(
                25, "The week it would not start", signin(),
                beats = listOf(
                    Beat(26, show = setOf("app"), light = setOf("app"), card = BoardCard("It would not start", "Create database if not exists came back forbidden, every time.")),
                    Beat(28, show = setOf("mi", "entra", "key"), light = setOf("mi"), pulse = "app" to "mi", card = BoardCard("No password", "DefaultAzureCredential: az login, or the service's own badge.")),
                    Beat(30, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "app" to "cosmos", card = BoardCard("Data-plane role", "Data Contributor: items and queries, not making a database.")),
                    Beat(32, light = setOf("cosmos"), card = BoardCard("The fix", "Make the database and containers with az, once, ahead of time.")),
                ),
            ),
            // 33: "Case nearly closed. Anything still open?"
            Scene(
                33, "Still open", query(),
                beats = listOf(
                    Beat(34, show = setOf("search", "fstr", "params", "sql"), light = setOf("search", "fstr"), pulse = "search" to "fstr", card = BoardCard("The search box", "Dal makhani: the first version pasted it into the query text.")),
                    Beat(36, light = setOf("params"), pulse = "search" to "params", card = paramCard),
                ),
            ),
            // 37: "And the second?"
            Scene(
                37, "Pages, and writes", paging(),
                beats = listOf(
                    Beat(38, show = setOf("page1", "token"), light = setOf("token"), pulse = "page1" to "token", card = BoardCard("Continuation token", "A bookmark with each page of fifty. Cosmos reads it; you cannot.")),
                    Beat(40, show = setOf("app", "page2"), light = setOf("page2"), pulse = "app" to "page2", card = BoardCard("Later, in a new request", "The query carries on exactly where it stopped.")),
                    Beat(42, light = setOf("page1"), card = upsertCard),
                    Beat(44, light = setOf("page2"), card = patchCard),
                ),
            ),
            // 45: "One last thing on the report: consistency."
            Scene(
                45, "Case closed", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("pr"), card = BoardCard("Consistency, next time", "How fresh a read must be. Session: you read your own writes.")),
                    Beat(48, light = setOf("pr", "cross", "pk"), card = BoardCard("Listen for the phrase", "A point read, a cross-partition query, a key with many values.")),
                    Beat(50, light = setOf("role", "prm", "tok"), card = BoardCard("And three more", "A data-plane role, parameters, and the continuation token.")),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-cosmos-sdk--gossip",
        listOf(
            // 0: "Okay, did you hear what the Zaiqa team did this week?"
            Scene(
                0, "What the Zaiqa team did", stores(),
                beats = listOf(
                    Beat(0, show = setOf("sql", "events", "pings"), light = setOf("events", "pings"), card = BoardCard("The week", "The rider pings and the order event feed moved out of Azure SQL.")),
                    Beat(2, light = setOf("sql"), card = BoardCard("Orders stay in SQL", "Restaurants, menus, users, orders, payments: real transactions.")),
                    Beat(4, light = setOf("pings"), card = BoardCard("Six million rows an hour", "Nobody updates one, and nobody joins them to payments.")),
                    Beat(6, light = setOf("sql"), card = BoardCard("Monday: Tom", "Added a pings table to Azure SQL anyway. It is just a table.")),
                    Beat(8, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "cosmos" to "pings", card = BoardCard("Cosmos DB for NoSQL", "Swallows that write rate without blinking.")),
                ),
            ),
            // 9: "Because it stores what, exactly?"
            Scene(
                9, "Items and containers", hierarchy(),
                beats = listOf(
                    Beat(10, show = setOf("acct", "db", "cont", "item"), light = setOf("item", "cont"), pulse = "cont" to "item", card = BoardCard("JSON items", "A plain-text form of labelled values, in a container, in a database.")),
                ),
            ),
            // 11: "Right. Tuesday."
            Scene(
                11, "Tuesday: Dana", signin(),
                beats = listOf(
                    Beat(12, show = setOf("app"), light = setOf("app"), card = BoardCard("Tuesday: Dana", "Create database if not exists, and every start came back forbidden.")),
                    Beat(14, show = setOf("mi", "entra", "key"), light = setOf("mi"), pulse = "app" to "mi", card = BoardCard("No password", "DefaultAzureCredential: az login, or the service's own badge.")),
                    Beat(16, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "app" to "cosmos", card = BoardCard("Data-plane role", "Data Contributor: items and queries. Making a database is not.")),
                    Beat(18, light = setOf("cosmos"), card = BoardCard("By lunch", "The database and the two containers made once with az.")),
                ),
            ),
            // 19: "Wednesday. I sense something worse."
            Scene(
                19, "Wednesday: Marcus", drawers(),
                beats = listOf(
                    Beat(20, show = setOf("key", "d1"), light = setOf("key"), pulse = "key" to "d1", card = BoardCard("Wednesday: Marcus", "He chose the pings container's partition key.")),
                    Beat(22, show = setOf("d2", "d3"), light = setOf("d2", "d3"), card = BoardCard("He picked city", "Four cities, four drawers. Mumbai's burns, three sleep.")),
                    Beat(24, light = setOf("d1"), card = BoardCard("The rider id", "Thousands of values, and what every read filters on.")),
                ),
            ),
            // 25: "Thursday. Go on."
            Scene(
                25, "Thursday: the intern", query(),
                beats = listOf(
                    Beat(26, show = setOf("search", "fstr", "params", "sql"), light = setOf("search", "fstr"), pulse = "search" to "fstr", card = BoardCard("Thursday: the intern", "Pasted the diner's search text straight into the query.")),
                    Beat(28, light = setOf("params"), pulse = "search" to "params", card = paramCard),
                ),
            ),
            // 29: "Friday. Please tell me Friday was quiet."
            Scene(
                29, "Friday: Priya", reads(),
                beats = listOf(
                    Beat(30, show = setOf("cont"), light = setOf("cont"), card = BoardCard("Friday: Priya", "Fetched one known event with a query saying where the id equals.")),
                    Beat(32, show = setOf("pr"), light = setOf("pr"), pulse = "cont" to "pr", card = pointCard),
                    Beat(34, light = setOf("pr"), card = halvesCard),
                    Beat(36, show = setOf("qx"), light = setOf("qx"), pulse = "cont" to "qx", card = BoardCard("Cross-partition query", "Without the key it fans out to every drawer and costs more.")),
                ),
            ),
            // 37: "Was that the fan-out I heard shouted about?"
            Scene(
                37, "The long answer", paging(),
                beats = listOf(
                    Beat(38, show = setOf("page1", "token"), light = setOf("page1"), card = BoardCard("An entire evening", "Thousands of pings asked for in one go.")),
                    Beat(40, light = setOf("token"), pulse = "page1" to "token", card = BoardCard("Continuation token", "A bookmark with each page of fifty. Hand it back later.")),
                    Beat(42, show = setOf("app", "page2"), light = setOf("page2"), pulse = "app" to "page2", card = upsertCard),
                    Beat(44, light = setOf("page2"), card = patchCard),
                ),
            ),
            // 45: "And the argument I heard in the kitchen about consistency?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("pr"), card = BoardCard("Consistency, next time", "How fresh a read must be. Session: you read your own writes.")),
                    Beat(48, light = setOf("pr", "cross", "pk"), card = BoardCard("Listen for the phrase", "A point read, a cross-partition query, a key with many values.")),
                    Beat(50, light = setOf("role", "prm", "tok"), card = BoardCard("And three more", "A data-plane role, parameters, and the continuation token.")),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-cosmos-sdk--late-night",
        listOf(
            // 0: "Hey, you. It is late, the tea has gone cold..."
            Scene(
                0, "The part that never sleeps", stores(),
                beats = listOf(
                    Beat(0, show = setOf("sql"), light = setOf("sql"), card = BoardCard("Tonight", "The part of Zaiqa that never sleeps.")),
                    Beat(2, light = setOf("sql"), card = BoardCard("Orders stay in SQL", "Restaurants, menus, users, orders, payments. Real transactions.")),
                    Beat(4, show = setOf("events", "pings"), light = setOf("events", "pings"), card = BoardCard("Two things SQL is bad at", "The order event feed, and a rider's position every three seconds.")),
                    Beat(6, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "cosmos" to "pings", card = BoardCard("Cosmos DB for NoSQL", "Six million an hour, nobody updating one, nobody joining it.")),
                ),
            ),
            // 8: "Which holds what, exactly?"
            Scene(
                8, "A cabinet, a room, a building", hierarchy(),
                beats = listOf(
                    Beat(8, show = setOf("item", "cont", "db", "acct"), light = setOf("item"), pulse = "cont" to "item", card = BoardCard("JSON items", "A plain-text form of labelled values. One record is one item.")),
                    Beat(10, light = setOf("cont"), card = BoardCard("The line to remember", "SQL holds what must be correct. Cosmos, what must be fast and huge.")),
                ),
            ),
            // 11: "Mm. And where inside the cabinet does one little ping land?"
            Scene(
                11, "Which drawer", drawers(),
                beats = listOf(
                    Beat(12, show = setOf("key", "d1"), light = setOf("key", "d1"), pulse = "key" to "d1", card = BoardCard("Partition key", "One property whose value picks the drawer. Pings: the rider id.")),
                    Beat(14, show = setOf("d2", "d3"), light = setOf("d2", "d3"), card = BoardCard("A bad key", "City: four values, so one drawer burns and three nap.")),
                ),
            ),
            // 15: "Noted. Now, who does the talking?"
            Scene(
                15, "Getting in", signin(),
                beats = listOf(
                    Beat(16, show = setOf("app"), light = setOf("app"), card = BoardCard("The Taste service", "Zaiqa's Python service owns these containers. Package: azure-cosmos.")),
                    Beat(18, show = setOf("key"), light = setOf("key"), card = BoardCard("No account key", "A master password for the whole account. We do not take one.")),
                    Beat(20, show = setOf("mi", "entra"), light = setOf("mi"), pulse = "app" to "mi", card = BoardCard("Managed identity", "az login on a laptop; in Azure, the badge Azure gives your app.")),
                    Beat(22, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "app" to "cosmos", card = BoardCard("Only what its role allows", "Built-in Data Contributor: items and queries.")),
                ),
            ),
            // 23: "And the things it cannot do?"
            Scene(
                23, "What it cannot do", roles(),
                beats = listOf(
                    Beat(24, show = setOf("dp", "items", "create", "cli"), light = setOf("create"), pulse = "dp" to "create", card = BoardCard("Forbidden", "Create database if not exists, with a data-plane role, fails.")),
                    Beat(26, light = setOf("cli"), pulse = "cli" to "create", card = BoardCard("The fix", "Make the database and the containers with az, once.")),
                ),
            ),
            // 27: "Right. Save me a ping, then."
            Scene(
                27, "Save and read", reads(),
                beats = listOf(
                    Beat(28, show = setOf("cont"), light = setOf("cont"), card = upsertCard),
                    Beat(30, show = setOf("pr"), light = setOf("pr"), pulse = "cont" to "pr", card = pointCard),
                    Beat(32, light = setOf("pr"), card = BoardCard("Request units", "The price tag on one call. Cheap calls stay in one drawer.")),
                    Beat(34, show = setOf("q1"), light = setOf("q1"), pulse = "cont" to "q1", card = BoardCard("Single-partition query", "Select, from, where, plus the partition key. One drawer.")),
                    Beat(36, show = setOf("qx"), light = setOf("qx"), pulse = "cont" to "qx", card = BoardCard("Cross-partition query", "Forget the key and it fans out to every drawer. Far more RUs.")),
                ),
            ),
            // 37: "And the search box, where someone types their heart's desire?"
            Scene(
                37, "Never into the query text", query(),
                beats = listOf(
                    Beat(38, show = setOf("search", "fstr", "params", "sql"), light = setOf("params"), pulse = "search" to "params", card = BoardCard("Parameters", "A named blank filled in separately. Otherwise, injection.")),
                ),
            ),
            // 39: "Sneaky. Now, a rider's whole evening is thousands of pings."
            Scene(
                39, "A bookmark", paging(),
                beats = listOf(
                    Beat(40, show = setOf("page1", "token"), light = setOf("token"), pulse = "page1" to "token", card = BoardCard("Continuation token", "A bookmark with each page of fifty. Cosmos reads it; you cannot.")),
                    Beat(42, show = setOf("app", "page2"), light = setOf("page2"), pulse = "app" to "page2", card = patchCard),
                    Beat(44, light = setOf("page1"), card = BoardCard("The whole thing", "Events on the order id, pings on the rider id, one badge.")),
                    Beat(46, light = setOf("page2"), card = BoardCard("Where it stops", "A refund across three tables is a SQL job, not a Cosmos one.")),
                ),
            ),
            // 47: "Mm. Before I drift off. What do I remember for the exam?"
            Scene(
                47, "For the exam", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("pr", "cross", "pk"), card = BoardCard("Listen for the phrase", "A point read, a cross-partition query, a key with many values.")),
                    Beat(50, light = setOf("role", "prm", "tok"), card = BoardCard("And three more", "A data-plane role, parameters, and the continuation token.")),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-cosmos-sdk--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", stores(),
                beats = listOf(
                    Beat(0, show = setOf("sql"), light = setOf("sql"), card = BoardCard("Date night", "Where Zaiqa puts what it cannot keep in a tidy table.")),
                    Beat(2, show = setOf("events", "pings"), light = setOf("sql"), card = BoardCard("Orders stay in SQL", "Restaurants, menus, users, payments. Real transactions.")),
                    Beat(4, light = setOf("pings"), card = BoardCard("Six million an hour", "Nobody updates one, and nobody joins it to a payment.")),
                    Beat(6, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "cosmos" to "pings", card = BoardCard("Right answer", "Cosmos DB for NoSQL: enormous write rates, records that differ.")),
                ),
            ),
            // 7: "And a Cosmos record is?"
            Scene(
                7, "A cabinet in a room", hierarchy(),
                beats = listOf(
                    Beat(8, show = setOf("item", "cont", "db", "acct"), light = setOf("item"), pulse = "cont" to "item", card = BoardCard("JSON items", "A plain-text form of labelled values. One record is one item.")),
                ),
            ),
            // 9: "A cabinet in a room in a building..."
            Scene(
                9, "Choosing the drawers", drawers(),
                beats = listOf(
                    Beat(10, show = setOf("key", "d1"), light = setOf("key"), pulse = "key" to "d1", card = BoardCard("Partition key", "One property whose value picks the drawer an item lives in.")),
                    Beat(12, show = setOf("d2", "d3"), light = setOf("d1", "d2", "d3"), card = BoardCard("Right answer", "Four values is too few. Key the pings on the rider id.")),
                ),
            ),
            // 14: "Mm. The event feed is keyed on the order id..."
            Scene(
                14, "How does it get in", signin(),
                beats = listOf(
                    Beat(16, show = setOf("app", "key"), light = setOf("key"), card = BoardCard("Account keys", "A master password for the whole account. We do not take one.")),
                    Beat(18, show = setOf("mi", "entra", "cosmos"), light = setOf("mi"), pulse = "app" to "mi", card = BoardCard("Right answer", "DefaultAzureCredential: az login, or the service's own badge.")),
                ),
            ),
            // 19: "Now I will be difficult..."
            Scene(
                19, "Whose fault", roles(),
                beats = listOf(
                    Beat(20, show = setOf("dp", "items", "create"), light = setOf("dp", "create"), pulse = "dp" to "create", card = BoardCard("Forbidden", "A data-plane role covers items and queries, not a new database.")),
                    Beat(22, show = setOf("cli"), light = setOf("cli"), pulse = "cli" to "create", card = BoardCard("Right answer", "Make the database and the containers with az, before you move in.")),
                ),
            ),
            // 24: "Your turn again. Writing a ping..."
            Scene(
                24, "Which call, and why", reads(),
                beats = listOf(
                    Beat(24, show = setOf("cont"), light = setOf("cont"), card = upsertCard),
                    Beat(26, light = setOf("cont"), card = BoardCard("Quiz: reading one back", "You know the id and the partition key value. Which call?")),
                    Beat(28, show = setOf("pr"), light = setOf("pr"), pulse = "cont" to "pr", card = BoardCard("No kiss: a point read", "Straight to one item, no query engine, about one RU.")),
                    Beat(30, light = setOf("pr"), card = halvesCard),
                    Beat(32, show = setOf("q1"), light = setOf("q1"), pulse = "cont" to "q1", card = BoardCard("Right answer", "Pass the partition key: a single-partition query. One drawer.")),
                    Beat(34, show = setOf("qx"), light = setOf("qx"), pulse = "cont" to "qx", card = BoardCard("Cross-partition query", "Without the key it fans out to every drawer. Costs far more.")),
                ),
            ),
            // 35: "And the search box, where a diner types whatever they like?"
            Scene(
                35, "Never into the query text", query(),
                beats = listOf(
                    Beat(36, show = setOf("search", "fstr", "params", "sql"), light = setOf("params"), pulse = "search" to "params", card = BoardCard("Parameters", "A named blank filled in separately. Otherwise, injection.")),
                ),
            ),
            // 37: "Last one from me, and it is a real exam question..."
            Scene(
                37, "What the app keeps", paging(),
                beats = listOf(
                    Beat(38, show = setOf("page1", "token", "app", "page2"), light = setOf("token"), pulse = "page1" to "token", card = BoardCard("Continuation token", "A bookmark the app hands back for the next fifty, later.")),
                    Beat(40, light = setOf("page2"), pulse = "app" to "page2", card = patchCard),
                ),
            ),
            // 41: "Two small instructions instead of the whole document..."
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("pr"), card = BoardCard("Consistency, next date", "How fresh a read must be. Session: you read your own writes.")),
                    Beat(44, light = setOf("pk"), card = BoardCard("The whole household", "Events on the order id, pings on the rider id, one badge.")),
                    Beat(46, light = setOf("pr", "cross", "pk"), card = BoardCard("Listen for the phrase", "A point read, a cross-partition query, a key with many values.")),
                    Beat(48, light = setOf("role", "prm"), card = BoardCard("And two more", "A data-plane role, and parameters for a diner's text.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
