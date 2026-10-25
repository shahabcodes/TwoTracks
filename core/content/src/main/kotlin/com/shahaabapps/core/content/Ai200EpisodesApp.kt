package com.shahaabapps.core.content

/** Zaiqa, the app we build all course: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesApp {
    private val classic = Podcast(
        "ai-app",
        listOf(
            maya("Hi, and welcome to the first episode of this course. Everything here we learn by building one app, across ten topics. The app is Zaiqa: a food delivery app, our own version of the one you already order dinner from. Today is the map."),
            leo("Zaiqa. Our own version. So are we copying how the real one works inside?"),
            maya("We cannot, and this is the only episode where I will say it: the real company's internals are not public. We are building our own system that does the same job. Everything after this is a design we choose."),
            leo("Fair enough. I write Android apps and I have never shipped anything to the cloud. Start from the screen I know."),
            maya("Then you already own the first piece. The Zaiqa app is Kotlin and Jetpack Compose: browse, search, order, track the rider. There is a second screen too, the partner dashboard, where a restaurant edits its menu. That one is Angular."),
            leo("Two front doors. What do they both talk to?"),
            maya("The Zaiqa API. An API is just a program other programs call: your app asks a question over the internet, and it answers. Ours is written in dot NET 9 with ASP dot NET Core, and it runs on Azure Container Apps."),
            leo("And it is the only thing my phone calls?"),
            maya("Yes, and that matters. Every rule about money and menus lives in one program, so there is one place to fix a bug. Picture a small online shop where the phone talks straight to the database: the price rules then live in every app version ever installed."),
            leo("[chuckle] I have shipped that shop. All right. Where does the data actually sit?"),
            maya("Four places, and they are not rivals. [pause] Learn them as four sentences, not four product names. SQL holds what must be correct. Cosmos holds what must be fast and huge. Redis holds what is true right now. Blob holds files."),
            leo("Start with the first one. SQL, as in a normal relational database?"),
            maya("Azure SQL Database, the managed SQL Server that Azure runs for you. It is Zaiqa's source of truth: the one store whose copy of a fact is the one that counts. Restaurants, menus, users, orders, order lines, payments."),
            leo("Why there, and not somewhere cheaper?"),
            maya("Because of transactions. A transaction is several changes that either all happen or none of them do. Your order writes one row in Orders, two in OrderLines and one in Payments, together. Half of that is not a smaller order. It is a bug with your money inside it."),
            leo("And the rest of it, the stuff flying around?"),
            maya("That is Cosmos DB for NoSQL. In there go the order event feed, the rider's ping history, and the dish vectors we search with. Enormous write rates, spread around the world."),
            leo("A dish vector?"),
            maya("A vector is a list of numbers. A model reads a dish description and writes numbers that capture what it means, so dal makhani lands near a creamy black lentil curry. For example, that is how we answer the vegan question."),
            leo("Why not just add a table of pings to SQL? It is right there."),
            maya("Thirty thousand rider pings a minute, into the database your payments depend on, and nobody ever queries last Tuesday's dot on a map. You would pay for it twice, and get nothing back. Cosmos also gives us a change feed."),
            leo("Change feed. Plain words?"),
            maya("An ordered list of what changed, which other work can read later. Order delivered goes into the feed, and the ratings prompt and the restaurant's payout pick it up from there. The change feed has its own episode at the end."),
            leo("Right. And Redis?"),
            maya("Azure Managed Redis, and it is a cache: a fast copy of something cheap to lose and quick to rebuild. The hot restaurant list, the rider's current position on your map, and rate limiting, which is counting how often one phone calls us."),
            leo("The rider's position is not in the database at all?"),
            maya("The one on your screen comes from Redis, read thousands of times a second. The history goes to Cosmos. And if Redis restarts and forgets, nothing that matters is lost: your order is still in SQL."),
            leo("And the photos."),
            maya("Blob Storage. A blob is a file kept in storage, found by its address rather than by a row. A four megabyte dish photo is a file. SQL keeps the dish row and the photo's address."),
            leo("There is a fifth store in my notes. Postgres?"),
            maya("PostgreSQL holds the payouts ledger, and it is our second place to keep vectors, on purpose: the exam likes asking when you would keep them there instead of in Cosmos. That argument has episodes of its own."),
            leo("Back to my dinner, then. I tap pay. What actually happens?"),
            maya("Say you're building this yourself, step by step. The app calls the API. The API writes the order, its lines and the payment into Azure SQL in one transaction, puts one message on a queue, and answers your phone. Under a second."),
            leo("A queue being?"),
            maya("A line of messages one service leaves for another to pick up later. Ours is Azure Service Bus. The message says: this order is paid, send it to the kitchen. The order worker, a small dot NET program, reads that line."),
            leo("Why not call the restaurant's system straight from the API and be done?"),
            maya("Because then your phone waits for their system. At eight on a Friday it is busy, and your spinner spins. With the queue the slow part is somebody else's problem, and the worker can try again."),
            leo("And the photo of the dish I was looking at?"),
            maya("A restaurant uploads it to Blob Storage from the dashboard. Event Grid notices and tells the photo function, a small Python program, which makes the thumbnail and writes the dish vector into Cosmos. Nobody waits for that."),
            leo("There is also a taste service in my notes."),
            maya("Python with FastAPI, also on Container Apps. It is the part that understands: something like dal makhani, but vegan. It reads those dish vectors."),
            leo("What about signing in, and all the passwords between these parts?"),
            maya("Microsoft Entra is the sign-in service, and it signs people in with a one-time code. Between our own services there are no passwords at all: each one gets a managed identity, a badge Azure gives it, so it can prove who it is with no stored secret."),
            leo("There must be some secret. We take payments."),
            maya("One: the payment provider's key, and it lives in Key Vault, a safe that Azure runs. The surge fee on a rainy Friday lives in App Configuration, so nobody redeploys the API to change a number."),
            leo("Is this not a lot of boxes for one dinner? Could it not all be one program?"),
            maya("It could, on day one. The reason it is not: the API must answer in milliseconds, the worker may retry for ten minutes, and the photo job is slow and lumpy. Together, the slowest sets the pace. For example, one stuck photo should never make an order fail."),
            leo("All right. Say I am asked which store holds what. How do I tell?"),
            maya("Listen for the phrase. Several tables must change together, or a monthly report for finance: [pause] that is relational, so Azure SQL. Millions of writes, global, or a feed of what changed: Cosmos DB."),
            leo("And the other two?"),
            maya("Read thousands of times a second and worthless a minute later is Redis. A four megabyte file is Blob Storage, with only the address in the row. And anything that must still add up after a crash goes with the orders, in SQL."),
            leo("Before we finish, what else should I hold on to for the exam?"),
            maya("Three things. The API owns Azure SQL, and nothing else writes to it. Everything that reacts to an order reads a queue or a change feed. And every episode after this changes one part of this map."),
            leo("And what should I try today?"),
            maya("Nothing here needs an Azure account. Draw Zaiqa's parts from memory and name what each one is built with. Write the four stores and one sentence each for why we chose them. Then pick three things Zaiqa saves and say which store each belongs in."),
            leo("And trace one order from my phone to the restaurant, naming every part it passes. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-app",
        listOf(
            maya("Welcome to Case Files. Our first case, and our first look at the app we build all course: Zaiqa, a food delivery app, our own version of the one you order dinner from. On Friday at eight, a customer paid for dal makhani. The kitchen never saw the order."),
            leo("Detective Leo, reporting. Before I open the file, I should admit something. I write Android apps, and I have never shipped a line of anything to the cloud."),
            maya("Perfect. A detective who has to ask. And one thing I will say once, here only: the real company's internals are not public, so Zaiqa is our own system doing the same job, not a description of theirs."),
            leo("Understood. Then walk me round the building. Who is in this case?"),
            maya("Six parts. The Zaiqa app in Kotlin and Jetpack Compose, the partner dashboard in Angular where a restaurant edits its menu, the Zaiqa API, the taste service, the order worker and the photo function. The API is where everything meets."),
            leo("The API. In plain words, for the file?"),
            maya("An API is a program other programs call: the phone asks a question over the internet, and the API answers. Ours is dot NET 9, and it is the only part allowed to write to the main database. Remember that line. It closes the case later."),
            leo("First suspect, then. The payment failed, and the customer was never really charged."),
            maya("Checked. The money is there, and so is the order. Azure SQL Database has the order row, its two order lines and the payment, written in one transaction: several changes that either all happen or none of them do."),
            leo("So SQL is where the truth lives."),
            maya("It is the source of truth: the one store whose copy of a fact is the one that counts. Restaurants, menus, users, orders, order lines, payments. If it has to be correct, it is in there."),
            leo("Suspect two. The database was busy, and the kitchen's message got lost in the crowd."),
            maya("Look at what else was being written that evening. Somebody had added a table of rider pings, and it was taking thirty thousand rows a minute from every rider on the road."),
            leo("Thirty thousand a minute. In the same database as the payments."),
            maya("And nobody ever queries last Tuesday's dot on a map. Pings belong in Cosmos DB for NoSQL, which we use for the order event feed, the rider ping history and the dish vectors."),
            leo("Dish vectors? In plain words?"),
            maya("A vector is a list of numbers. A model reads a dish description and writes numbers that capture its meaning, so dal makhani sits near a creamy black lentil curry. That is how we answer: something like dal makhani, but vegan."),
            leo("Noted. But the pings only made things slow. My order still is not in the kitchen."),
            maya("Then follow it. The API writes to SQL, puts one message on a queue, and answers the phone. A queue is a line of messages one service leaves for another to pick up later. Ours is Azure Service Bus."),
            leo("And was the message on the queue?"),
            maya("It was. Timed one second past eight. [pause] Which moves our suspect along to whatever was supposed to read it."),
            leo("The order worker. The small program that takes the message and tells the restaurant."),
            maya("A small dot NET worker, yes. And here is the clue. Its log that evening is one line, over and over: a database error. The worker was writing to Azure SQL itself."),
            leo("Writing to SQL. But you said the API is the only part allowed to."),
            maya("Somebody made it a second writer last month, to save one call. The worker tried to set an order status the API had already changed, failed, and dropped the message instead of sending it on."),
            leo("[sigh] So the rules about money now live in two programs, and the two disagree."),
            maya("That is the whole case. One writer to SQL. Everything else reacts, through a queue or through a change feed, which is an ordered list of what changed that other work can read."),
            leo("What else did the customer see? They said the rider never moved."),
            maya("A side case, and an easy one. The dot on your map comes from Azure Managed Redis, a cache: a fast copy of something cheap to lose and quick to rebuild. No order was ever sent, so no rider was ever assigned."),
            leo("And what else does Redis hold?"),
            maya("The hot restaurant list, and rate limiting, which is counting how often one phone calls us. Picture a shop that keeps today's bestsellers on a card by the till. That is Redis. The till's books are still SQL."),
            leo("Last complaint in the file. The dish photo was missing from the menu."),
            maya("That one closes itself. The restaurant uploaded a photo from the dashboard, and the dashboard put it in a database column instead of Blob Storage. A blob is a file kept in storage, found by its address rather than by a row."),
            leo("Four megabytes of curry inside the orders database."),
            maya("Which every backup and every restore then drags along behind it. The photo belongs in Blob Storage. Event Grid tells the photo function it landed, and the function makes the thumbnail and writes the dish vector into Cosmos."),
            leo("So what was actually fixed?"),
            maya("Four things. The worker stopped writing to SQL and went back to messages. The pings moved to Cosmos. The photo moved to Blob Storage. And nobody makes a phone wait for a restaurant to answer. That is what the queue is for."),
            leo("And the parts I have not met yet?"),
            maya("The taste service is Python with FastAPI, and it is the part that answers the vegan question. Microsoft Entra is the sign-in service, with a one-time code to your phone. Between our own services nobody holds a password: each has a managed identity, a badge Azure gives it."),
            leo("No passwords. What about the payment provider's key?"),
            maya("In Key Vault, a safe that Azure runs for you. The surge fee on a rainy Friday lives in App Configuration, so nobody redeploys the API to change a number. PostgreSQL keeps the payouts ledger, and it has episodes of its own."),
            leo("Then what does this case teach me for the exam?"),
            maya("Listen for the phrase. Several tables must change together, or a monthly report for finance: Azure SQL. Millions of writes, global, or a feed of what changed: Cosmos DB."),
            leo("And the other two stores?"),
            maya("Read thousands of times a second and worthless a minute later: Redis. A four megabyte file: Blob Storage, with only the address in the row. And one writer to the database, with everything else reacting."),
            leo("Case closed. What do I try today?"),
            maya("Nothing that needs an Azure account. Draw Zaiqa's parts from memory and name what each one is built with, then write the four stores and one sentence each for why we chose them."),
            leo("And trace one order from the phone to the restaurant, naming every part it passes. Case closed. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-app",
        listOf(
            maya("Sit down, because did you hear what the Zaiqa team did this week? Zaiqa is the food delivery app we build across this whole course, our own version of the one you order dinner from. Five people, five mistakes, and one map of the app."),
            leo("[laugh] Five? Start at the beginning. I write Android apps and I have never shipped to the cloud, so pretend I know nothing above the screen."),
            maya("Then you know the first piece already: the Zaiqa app, Kotlin and Jetpack Compose. There is a partner dashboard too, in Angular, where a restaurant edits its menu and watches orders arrive. Both of them call one thing."),
            leo("Which is?"),
            maya("The Zaiqa API. An API is a program other programs call: the phone asks over the internet, and the API answers. Ours is dot NET 9, and it is the only part allowed to write to the main database. Hold on to that, because Monday."),
            leo("Monday. Go."),
            maya("Tom. Tom put the rider pings in Azure SQL Database. Thirty thousand rows a minute, in the same database as every order and every payment."),
            leo("Why is that database so precious?"),
            maya("It is the source of truth: the one store whose copy of a fact is the one that counts. Restaurants, menus, users, orders, order lines, payments. And it can write them in one transaction, which is several changes that either all happen or none of them do."),
            leo("And the pings do not need any of that."),
            maya("Nobody ever queries last Tuesday's dot on a map. They belong in Cosmos DB for NoSQL, with the order event feed and the rider ping history: enormous write rates, spread around the world, and a change feed."),
            leo("Two new words there. Change feed?"),
            maya("An ordered list of what changed in Cosmos, which other work can read later. Order delivered goes in, and the ratings prompt and the payout pick it up. Tom moved the pings on Tuesday and apologised to the finance team."),
            leo("And what else lives in Cosmos?"),
            maya("The dish vectors. A vector is a list of numbers. A model reads a dish description and writes numbers that capture what it means, so dal makhani lands near a creamy black lentil curry. That is how the taste service answers: something like dal makhani, but vegan."),
            leo("Right. Tuesday?"),
            maya("Dana. Dana kept the order total only in Azure Managed Redis, because it was faster."),
            leo("What is Redis, in plain words?"),
            maya("A cache: a fast copy of something cheap to lose and quick to rebuild. The hot restaurant list, the rider's current position on your map, and rate limiting, which is counting how often one phone calls us."),
            leo("Cheap to lose. And she kept money in it."),
            maya("Redis restarted on Tuesday night, and nobody could say what four hundred people had been charged. The totals were rebuilt from the payment provider by hand. [sigh] Dana does not talk about Tuesday."),
            leo("Wednesday, then."),
            maya("Marcus. A restaurant uploaded a four megabyte photo of a new dish, and Marcus saved the file into a column in the orders database."),
            leo("Where should a photo go?"),
            maya("Blob Storage. A blob is a file kept in storage, found by its address rather than by a row. SQL keeps the dish row and the address, never the file. Marcus found out when the nightly backup took two hours."),
            leo("[chuckle] A photo album in the payments database. Thursday?"),
            maya("The intern, and this is the big one. The order worker, a small dot NET program that takes a paid order and sends it to the restaurant, was given its own connection and told to write to SQL directly."),
            leo("I thought only the API writes."),
            maya("That is the rule, and the intern broke it to save one call. Within a day the rules about money lived in two programs, the two disagreed, and orders quietly failed on a Friday night."),
            leo("So how is the worker meant to hear about an order?"),
            maya("Through a queue: a line of messages one service leaves for another to pick up later. The API writes to SQL, drops one message on Azure Service Bus, and answers the phone. The worker reads that queue."),
            leo("And Friday? Please tell me Friday was quiet."),
            maya("Priya. Priya made the app wait for the restaurant's own system to confirm the order before the screen moved on."),
            leo("That sounds polite."),
            maya("It is polite until eight on a Friday, when their system is busy and your spinner spins for forty seconds. Say you're building a small shop: never make a customer wait on somebody else's server. Put it on the queue, and let the worker retry."),
            leo("Did anything go right this week?"),
            maya("The quiet parts. A restaurant uploads a photo, Event Grid tells the photo function, a small Python program, and it makes the thumbnail and writes the dish vector into Cosmos. Nobody waits for any of it."),
            leo("And the rest of the cast?"),
            maya("The taste service is Python with FastAPI. Microsoft Entra is the sign-in service that signs customers in with a one-time code. Between our own services nobody holds a password: each has a managed identity, a badge Azure gives it."),
            leo("No passwords at all? We take payments."),
            maya("One key, the payment provider's, and it sits in Key Vault, a safe that Azure runs. The surge fee on a rainy Friday lives in App Configuration, so nobody redeploys the API to change a number. PostgreSQL keeps the payouts ledger."),
            leo("All right. If the exam asks me which store holds what, how do I tell?"),
            maya("Listen for the phrase. Several tables must change together, or a monthly report for finance: Azure SQL. Millions of writes, global, or a feed of what changed: Cosmos DB."),
            leo("And the other two?"),
            maya("Read thousands of times a second and worthless a minute later: Redis. A four megabyte file: Blob Storage, with only the address in the row. And one writer to the database. Everybody else reacts."),
            leo("So what do I try today, so I am not next week's story?"),
            maya("Nothing that needs an Azure account. Draw Zaiqa's parts from memory and name what each one is built with, then write the four stores and one sentence each for why we chose them."),
            leo("And trace one order from the phone to the restaurant, naming every part it passes. Poor Dana. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-app",
        listOf(
            nova("It is late, and you are still up, so let us start something together. [pause] Tonight is the first night of a long build. We are going to make a food delivery app, our own version of the one you order dinner from, and we are calling it Zaiqa."),
            ivy("Zaiqa. Ours. And I should say, for anyone listening on a walk: I have never put anything in the cloud. I write phone apps."),
            nova("Then you already own the first piece. [pause] The Zaiqa app, Kotlin and Jetpack Compose: browse, search, order, and watch the rider crawl towards you. That is your end of it."),
            ivy("And the other end?"),
            nova("A second screen, the partner dashboard, in Angular, where a restaurant edits its menu and watches orders land. Both of them talk to one thing, and only one thing."),
            ivy("Which is?"),
            nova("The Zaiqa API. An API is just a program other programs call: your phone asks a question over the internet, and it answers. Ours is dot NET 9, and it is the only part allowed to write to the main database."),
            ivy("Only one? That sounds strict, for this hour."),
            nova("Strict is kind, later. [pause] One writer means one place where the rules about money live. Two writers means two stories about what you were charged, and at two in the morning somebody has to choose between them."),
            ivy("Go on, then. Where does my dinner actually get written down?"),
            nova("Azure SQL Database. It is Zaiqa's source of truth: the one store whose copy of a fact is the one that counts. Restaurants, menus, users, orders, order lines, payments."),
            ivy("And why that one, and not something newer?"),
            nova("Because of the transaction: several changes that either all happen or none of them do. Your order writes a row in Orders, two in OrderLines and one in Payments, together. [pause] Half an order is not a smaller dinner. It is a bug with your money inside it."),
            ivy("Fair. What else is awake at this hour?"),
            nova("Cosmos DB for NoSQL. It takes the order event feed, the rider's ping history, and the dish vectors. Enormous write rates, spread around the world, and a change feed."),
            ivy("You said two new things there. In plain words, slowly."),
            nova("A vector is a list of numbers. A model reads a dish and writes numbers that capture what it means, so dal makhani lands near a creamy black lentil curry. [pause] And a change feed is an ordered list of what changed, which other work can read later."),
            ivy("So delivered goes into the feed, and something else picks it up."),
            nova("The ratings prompt, and the payout to the restaurant. Neither of them has to ask the API's permission. [pause] We give the change feed a whole night of its own, at the end of the course."),
            ivy("And the little dot of the rider, creeping along my map?"),
            nova("That is Azure Managed Redis, and it is a cache: a fast copy of something cheap to lose and quick to rebuild. The rider's position right now, the hot restaurant list, and rate limiting, which is counting how often one phone calls us."),
            ivy("Cheap to lose. I like things that are honest about themselves."),
            nova("[chuckle] It is. If Redis restarts and forgets, your order is still in SQL and nothing that matters is gone. Only what was true a minute ago, and a minute ago has passed anyway."),
            ivy("And the photographs? The ones that make me order at midnight."),
            nova("Blob Storage. A blob is a file kept in storage, found by its address rather than by a row. Four megabytes of curry is a file. SQL keeps the dish row and the address, and never the photo."),
            ivy("Say the four of them back to me, in one breath."),
            nova("SQL holds what must be correct. Cosmos holds what must be fast and huge. Redis holds what is true right now. Blob holds files. [pause] Four sentences, and most of the exam's data questions are one of them."),
            ivy("Four sentences. I can carry that to bed. Now, you promised me an order."),
            nova("Eight on a Friday, and you tap pay. The app calls the API. The API writes the order, its lines and the payment into SQL in one transaction, puts one message on a queue, and answers your phone. Under a second."),
            ivy("A queue, at this hour, sounds restful."),
            nova("A line of messages one service leaves for another to pick up later. Ours is Azure Service Bus. The message says: this one is paid, send it to the kitchen. [pause] The order worker, a small dot NET program, reads that line."),
            ivy("Why not telephone the kitchen directly and have done with it?"),
            nova("Because then you wait for their system. At eight on a Friday their system is busy, and your spinner spins while you stare at it. The queue hands the slow part to somebody else, and the worker can try again."),
            ivy("And the quiet work nobody sees?"),
            nova("A restaurant uploads a dish photo. Event Grid notices and tells the photo function, a small Python program, which makes the thumbnail and writes the dish vector into Cosmos. And the taste service, Python with FastAPI, is the part that answers: something like dal makhani, but vegan."),
            ivy("Nobody waiting. Lovely. And the doors? Who holds keys?"),
            nova("Microsoft Entra is the sign-in service, and it signs you in with a one-time code to your phone. Between our own services there are no passwords at all: each one has a managed identity, a badge Azure gives it to prove who it is."),
            ivy("No passwords anywhere?"),
            nova("One. The payment provider's key, asleep in Key Vault, a safe that Azure runs. And the surge fee on a rainy Friday lives in App Configuration, so nobody redeploys the API to change a number. [pause] PostgreSQL keeps the payouts ledger, for another night."),
            ivy("Where does tonight's picture stop being true?"),
            nova("Here. Everything we drew is our own design, not a description of any real company, whose internals are not public. And nothing tonight is the whole of any service: each one gets its own episode, and its own argument."),
            ivy("Before I give in, then. What do I keep for the exam?"),
            nova("Listen for the phrase. Several tables changing together, or a monthly report for finance: Azure SQL. Millions of writes, global, or a feed of what changed: Cosmos DB."),
            ivy("And my little dot, and my photographs?"),
            nova("Read thousands of times a second and worthless a minute later: Redis. A four megabyte file: Blob Storage, with only the address in the row. [pause] And one writer to the database, with everything else reacting."),
            ivy("And something to try today, before I sleep?"),
            nova("Nothing that needs an Azure account. Draw Zaiqa's parts from memory and name what each one is built with, then write the four stores and one sentence each for why we chose them."),
            ivy("And trace one order from my phone to the restaurant, naming every part it passes. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-app",
        listOf(
            nova("Date night, darling. Candles, something warm, and ten courses ahead of us. [pause] Tonight we start building Zaiqa: our own food delivery app, our own version of the one we order from every Friday."),
            rafe("Our own version. Not a copy of theirs?"),
            nova("We could not be. Their internals are not public, love, so this is our design, start to finish. And I will say that once tonight and never again."),
            rafe("Our design. I like the sound of that. Where do we start? I only know the phone."),
            nova("You are the first course. The Zaiqa app, Kotlin and Jetpack Compose: browse, search, order, watch the rider come to you. And beside you, the partner dashboard in Angular, where a restaurant keeps its menu."),
            rafe("And we both talk to the same one, I hope."),
            nova("Only one, and only ever one. The Zaiqa API, in dot NET 9. An API is a program other programs call: you ask over the internet, it answers. A question for you already: why should it be the only thing that writes to our database?"),
            rafe("Because the rules about money should live in one place. Two writers, two stories about what I was charged, and one of the two is wrong."),
            nova("Right answer, and beautifully said. [kiss]"),
            rafe("Keep going, then. Where does my order actually go?"),
            nova("Azure SQL Database, our source of truth: the one store whose copy of a fact is the one that counts. Restaurants, menus, users, orders, order lines, payments. Everything that has to be correct."),
            rafe("And it writes them all together, or not at all."),
            nova("That is the transaction: several changes that either all happen or none of them do. One row in Orders, two in OrderLines, one in Payments. All in, or nothing. [pause] I do approve of commitment."),
            rafe("[chuckle] So do I. Exam question for you, darling. We take thirty thousand rider pings a minute, and later work has to react to each one. Where do they go?"),
            nova("Cosmos DB for NoSQL, and its change feed starts the later work. A change feed is an ordered list of what changed, which other work reads. Pings in SQL would cost a fortune and buy us nothing."),
            rafe("Right answer, and the reason with it. [kiss]"),
            nova("Cosmos also keeps the order event feed and the dish vectors. Your turn: what is a vector?"),
            rafe("A list of numbers. A model reads a dish and writes numbers that capture what it means, so dal makhani lands near a creamy black lentil curry. It is how we answer: something like dal makhani, but vegan."),
            nova("Right answer. Clever is very attractive. [kiss]"),
            rafe("And the little dot of our rider, crawling across the map?"),
            nova("Azure Managed Redis, a cache: a fast copy of something cheap to lose and quick to rebuild. The rider's position right now, the hot restaurant list, and rate limiting, which is counting how often one phone calls us."),
            rafe("Cheap to lose. A question, then. Could we keep the order total in Redis, since it is so fast?"),
            nova("We could, and we would lose it on the next restart, with nobody able to say what four hundred people had been charged. A total is money, so it lives in SQL."),
            rafe("Right answer, and no shortcuts. [kiss]"),
            nova("Your turn again, handsome. A restaurant sends us a four megabyte photo of a new dish. Where does the file live, and what does SQL keep?"),
            rafe("Put it in the orders database, as a column. Then it travels with the dish."),
            nova("[sigh] No kiss for that one. Every backup and every restore then drags a photo album behind it. The file goes in Blob Storage, where a blob is a file found by its address rather than by a row, and SQL keeps the dish row and that address."),
            rafe("Noted, and deserved. Say the four of them for me, in one breath."),
            nova("SQL holds what must be correct. Cosmos holds what must be fast and huge. Redis holds what is true right now. Blob holds files. [pause] Four sentences, and most of the exam's data questions are one of them."),
            rafe("Then take me through one order. Friday, eight, I tap pay."),
            nova("The app calls the API. The API writes the order, its lines and the payment into SQL in one transaction, puts one message on a queue, and answers your phone. Under a second, and you are free."),
            rafe("A queue being a line of messages one service leaves for another to pick up later. Azure Service Bus, in our case. The order worker reads it and tells the restaurant."),
            nova("Right answer, and you read ahead. [kiss]"),
            rafe("Why not call the kitchen straight from the API, and be honest about it?"),
            nova("Because then you wait for their system, and at eight on a Friday their system is busy. The queue takes the slow part away, and the worker can try again if it fails. Nobody should be kept waiting, love."),
            rafe("Quite. And the photograph that made me order in the first place?"),
            nova("The restaurant uploads it, Event Grid notices and tells the photo function, a small Python program, and it makes the thumbnail and writes the dish vector into Cosmos. The taste service, Python with FastAPI, answers the vegan question."),
            rafe("And the keys to all this? I do like to know who holds a key."),
            nova("Nobody does. Microsoft Entra is the sign-in service, and it signs you in with a one-time code. Between our own services there are no passwords: each one has a managed identity, a badge Azure gives it to prove who it is."),
            rafe("One key, surely. We take payments."),
            nova("One, and it is locked in Key Vault, a safe that Azure runs. The surge fee on a rainy Friday lives in App Configuration, so nobody redeploys the API to change a number. PostgreSQL keeps the payouts ledger, for another date."),
            rafe("Last question of the night, for the exam. Which phrases point where?"),
            nova("Several tables changing together, or a monthly report for finance: Azure SQL. Millions of writes, global, or a feed of what changed: Cosmos DB. Read every second and worthless a minute later: Redis."),
            rafe("And a four megabyte file?"),
            nova("Blob Storage, with only the address in the row. And one writer to the database, with everything else reacting through a queue or a change feed."),
            rafe("Four for four. That earns a long one. [kiss]"),
            nova("And something to try today, my love?"),
            rafe("Nothing that needs an Azure account. Draw Zaiqa's parts from memory and name what each one is built with, then write the four stores and one sentence each for why we chose them."),
            nova("And trace one order from your phone to the restaurant, naming every part it passes. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun zaiqaMap() = DiagramSpec(
        "One order's journey: the app and the dashboard call the API, the API owns Azure SQL, and the queue, the worker and the photo function fill Cosmos, Redis and Blob.",
        nodes = listOf(
            DNode("app", "Zaiqa app", 0.17f, 0.10f, w = 0.28f, h = 0.14f, sub = "Kotlin"),
            DNode("dash", "Partner dash", 0.83f, 0.10f, w = 0.28f, h = 0.14f, sub = "Angular"),
            DNode("api", "Zaiqa API", 0.5f, 0.31f, w = 0.28f, h = 0.14f, tone = 1, sub = "dot NET 9"),
            DNode("sql", "Azure SQL", 0.17f, 0.52f, w = 0.28f, h = 0.16f, kind = NodeKind.CYLINDER, sub = "source of truth"),
            DNode("redis", "Redis", 0.5f, 0.52f, w = 0.28f, h = 0.16f, kind = NodeKind.CYLINDER, tone = 2, sub = "right now"),
            DNode("blob", "Blob Storage", 0.83f, 0.52f, w = 0.28f, h = 0.16f, kind = NodeKind.CYLINDER, tone = 2, sub = "dish photos"),
            DNode("queue", "Orders queue", 0.17f, 0.73f, w = 0.28f, h = 0.14f, sub = "Service Bus"),
            DNode("worker", "Order worker", 0.5f, 0.73f, w = 0.28f, h = 0.14f, tone = 1),
            DNode("photo", "Photo function", 0.83f, 0.73f, w = 0.28f, h = 0.14f, sub = "Python"),
            DNode("cosmos", "Cosmos DB", 0.5f, 0.92f, w = 0.28f, h = 0.16f, kind = NodeKind.CYLINDER, sub = "events, vectors"),
        ),
        edges = listOf(
            DEdge("app", "api", label = "order"), DEdge("dash", "api", label = "menus"),
            DEdge("api", "sql", label = "read write"), DEdge("api", "redis", label = "hot list"),
            DEdge("api", "blob", label = "photo url"), DEdge("api", "queue", label = "paid"),
            DEdge("queue", "worker", label = "message"), DEdge("worker", "cosmos", label = "events"),
            DEdge("photo", "blob", dashed = true), DEdge("photo", "cosmos", label = "vectors"),
        ),
        aspect = 0.9f,
    )

    private fun stores() = DiagramSpec(
        "Four stores, four sentences: SQL what must be correct, Cosmos what must be fast and huge, Redis what is true right now, Blob the files.",
        nodes = listOf(
            DNode("rule", "Four sentences", 0.5f, 0.12f, w = 0.56f, h = 0.16f, kind = NodeKind.NOTE, sub = "not four products"),
            DNode("sql", "Azure SQL", 0.25f, 0.42f, w = 0.44f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "must be correct"),
            DNode("cosmos", "Cosmos DB", 0.75f, 0.42f, w = 0.44f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 1, sub = "fast and huge"),
            DNode("redis", "Redis", 0.25f, 0.78f, w = 0.44f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2, sub = "true right now"),
            DNode("blob", "Blob Storage", 0.75f, 0.78f, w = 0.44f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2, sub = "files"),
        ),
        aspect = 0.8f,
    )

    private fun order() = DiagramSpec(
        "One order: the app calls the API, the API writes SQL in one transaction and drops a message on the queue, and the worker tells the restaurant.",
        nodes = listOf(
            DNode("you", "You", 0.1f, 0.12f, w = 0.16f, h = 0.16f, kind = NodeKind.USER),
            DNode("app", "Zaiqa app", 0.45f, 0.12f, w = 0.4f, h = 0.16f, sub = "tap pay"),
            DNode("api", "Zaiqa API", 0.45f, 0.37f, w = 0.4f, h = 0.16f, tone = 1, sub = "dot NET 9"),
            DNode("sql", "Azure SQL", 0.82f, 0.37f, w = 0.32f, h = 0.18f, kind = NodeKind.CYLINDER, sub = "one transaction"),
            DNode("queue", "Orders queue", 0.45f, 0.62f, w = 0.44f, h = 0.16f, sub = "Service Bus"),
            DNode("worker", "Order worker", 0.3f, 0.87f, w = 0.42f, h = 0.16f, tone = 1, sub = "dot NET"),
            DNode("kitchen", "The restaurant", 0.78f, 0.87f, w = 0.4f, h = 0.16f, kind = NodeKind.NOTE, sub = "cooks it"),
        ),
        edges = listOf(
            DEdge("you", "app"),
            DEdge("app", "api", label = "order"),
            DEdge("api", "sql", label = "write"),
            DEdge("api", "queue", label = "paid"),
            DEdge("queue", "worker", label = "message"),
            DEdge("worker", "kitchen", label = "sends"),
        ),
        aspect = 0.8f,
    )

    private fun pipeline() = DiagramSpec(
        "The work nobody waits for: a photo lands in Blob Storage, Event Grid starts the photo function, and the dish vector goes into Cosmos for the taste service.",
        nodes = listOf(
            DNode("photo", "Dish photo", 0.2f, 0.14f, w = 0.36f, h = 0.18f, kind = NodeKind.NOTE, sub = "uploaded"),
            DNode("blob", "Blob Storage", 0.72f, 0.14f, w = 0.44f, h = 0.18f, kind = NodeKind.CYLINDER, sub = "the file"),
            DNode("grid", "Event Grid", 0.2f, 0.45f, w = 0.36f, h = 0.18f, sub = "it landed"),
            DNode("fn", "Photo function", 0.72f, 0.45f, w = 0.44f, h = 0.18f, tone = 1, sub = "Python"),
            DNode("taste", "Taste service", 0.2f, 0.78f, w = 0.36f, h = 0.2f, sub = "FastAPI"),
            DNode("cosmos", "Cosmos DB", 0.72f, 0.78f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "dish vectors"),
        ),
        edges = listOf(
            DEdge("photo", "blob", label = "upload"),
            DEdge("blob", "grid", label = "tells"),
            DEdge("grid", "fn", label = "starts"),
            DEdge("fn", "cosmos", label = "vector"),
            DEdge("taste", "cosmos", label = "reads"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's store questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("tables", "Tables change together", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Azure SQL"),
            DNode("writes", "Millions of writes", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Cosmos DB"),
            DNode("second", "Read every second", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Redis, a cache"),
            DNode("file", "A four megabyte file", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Blob Storage"),
            DNode("wait", "The app must not wait", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a queue"),
            DNode("nopass", "No password inside", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a badge"),
        ),
        aspect = 0.8f,
    )

    private val today = BoardCard("Today", "Draw the parts from memory; the four stores; trace one order to the restaurant.")
    private val phrases = BoardCard("Listen for the phrase", "Tables together: SQL. Millions of writes or a feed of changes: Cosmos.")
    private val otherTwo = BoardCard("The other two", "Read every second, cheap to lose: Redis. A four megabyte file: Blob.")

    private fun classicBoard() = PodcastBoard(
        "ai-app",
        listOf(
            // 0: "Hi, and welcome to the first episode of this course..."
            Scene(
                0, "Meet Zaiqa", zaiqaMap(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("Zaiqa", "The food delivery app we build across ten topics. Today is the map.")),
                    Beat(2, light = setOf("app"), card = BoardCard("Our own design", "The real company's internals are not public. This system is ours.")),
                    Beat(4, show = setOf("dash"), light = setOf("app", "dash"), card = BoardCard("Two front doors", "The Kotlin app, and the Angular dashboard a restaurant keeps its menu in.")),
                    Beat(6, show = setOf("api"), light = setOf("api"), pulse = "app" to "api", card = BoardCard("Zaiqa API", "A program other programs call. Dot NET 9 on Azure Container Apps.")),
                    Beat(8, show = setOf("sql", "redis", "blob", "queue", "worker", "photo", "cosmos"), light = setOf("api"), card = BoardCard("One writer", "Only the API writes to the database, so the money rules live in one place.")),
                ),
            ),
            // 10: "Four places, and they are not rivals..."
            Scene(
                10, "Four stores", stores(),
                beats = listOf(
                    Beat(10, light = setOf("sql", "cosmos", "redis", "blob"), card = BoardCard("Four sentences", "SQL correct. Cosmos fast and huge. Redis right now. Blob files.")),
                    Beat(12, light = setOf("sql"), card = BoardCard("Azure SQL", "The source of truth: restaurants, menus, users, orders, payments.")),
                    Beat(14, light = setOf("sql"), card = BoardCard("Transaction", "Several changes that either all happen or none of them do.")),
                    Beat(16, light = setOf("cosmos"), card = BoardCard("Cosmos DB", "Order events, rider pings and dish vectors: huge writes, worldwide.")),
                    Beat(18, light = setOf("cosmos"), card = BoardCard("Dish vector", "A list of numbers that captures what a dish means.")),
                    Beat(22, light = setOf("cosmos"), card = BoardCard("Change feed", "An ordered list of what changed, which other work can read later.")),
                    Beat(24, light = setOf("redis"), card = BoardCard("Redis", "A cache: the hot list, the rider's position, rate limiting.")),
                    Beat(28, light = setOf("blob"), card = BoardCard("Blob Storage", "A file found by its address. SQL keeps the row and the address.")),
                    Beat(30, light = setOf("sql", "cosmos"), card = BoardCard("PostgreSQL too", "The payouts ledger, and the other place to keep vectors.")),
                ),
            ),
            // 31: "Back to my dinner, then. I tap pay..."
            Scene(
                31, "One order", order(),
                beats = listOf(
                    Beat(32, show = setOf("you", "app", "api", "sql"), light = setOf("api", "sql"), pulse = "api" to "sql", card = BoardCard("Tap pay", "The order, its lines and the payment, written in one transaction.")),
                    Beat(34, show = setOf("queue", "worker"), light = setOf("queue"), pulse = "api" to "queue", card = BoardCard("Service Bus queue", "A line of messages one service leaves for another to pick up later.")),
                    Beat(36, show = setOf("kitchen"), light = setOf("worker", "kitchen"), pulse = "worker" to "kitchen", card = BoardCard("Why a queue", "Your phone never waits for the restaurant's own system to answer.")),
                ),
            ),
            // 37: "And the photo of the dish I was looking at?"
            Scene(
                37, "The work nobody waits for", pipeline(),
                beats = listOf(
                    Beat(38, light = setOf("grid", "fn"), pulse = "grid" to "fn", card = BoardCard("Event Grid", "The photo lands, and the function makes the thumbnail and the vector.")),
                    Beat(40, light = setOf("taste"), pulse = "taste" to "cosmos", card = BoardCard("Taste service", "Python with FastAPI: something like dal makhani, but vegan.")),
                    Beat(42, light = setOf("fn"), card = BoardCard("No passwords inside", "Entra signs people in; every service carries a managed identity badge.")),
                    Beat(44, light = setOf("cosmos"), card = BoardCard("Key Vault, App Configuration", "The payment key in a safe; the surge fee as a setting.")),
                ),
            ),
            // 45: "Is this not a lot of boxes for one dinner?"
            Scene(
                45, "Why so many parts", zaiqaMap(),
                beats = listOf(
                    Beat(46, light = setOf("api", "worker", "photo"), card = BoardCard("Why not one program?", "The API answers in milliseconds; the photo job is slow and lumpy.")),
                ),
            ),
            // 47: "All right. Say I am asked which store holds what."
            Scene(
                47, "For the exam", exam(),
                beats = listOf(
                    Beat(48, light = setOf("tables", "writes"), card = phrases),
                    Beat(50, light = setOf("second", "file"), card = otherTwo),
                    Beat(52, light = setOf("wait", "nopass"), card = BoardCard("One writer", "Everything else reacts through a queue or a change feed.")),
                    Beat(54, card = today),
                ),
            ),
        ),
    )

    private fun theCase() = DiagramSpec(
        "The case: the order is in SQL and the message is on the queue, but the worker dropped it and the kitchen saw nothing.",
        nodes = listOf(
            DNode("you", "Customer", 0.12f, 0.14f, w = 0.18f, h = 0.18f, kind = NodeKind.USER),
            DNode("app", "Zaiqa app", 0.5f, 0.14f, w = 0.4f, h = 0.18f, sub = "paid at eight"),
            DNode("api", "Zaiqa API", 0.5f, 0.44f, w = 0.4f, h = 0.18f, tone = 1, sub = "dot NET 9"),
            DNode("sql", "Azure SQL", 0.85f, 0.44f, w = 0.26f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "order is here"),
            DNode("queue", "Orders queue", 0.5f, 0.72f, w = 0.44f, h = 0.16f, sub = "message is here"),
            DNode("worker", "Order worker", 0.28f, 0.93f, w = 0.42f, h = 0.14f, tone = 1, sub = "dropped it"),
            DNode("kitchen", "The kitchen", 0.76f, 0.93f, w = 0.36f, h = 0.14f, kind = NodeKind.NOTE, tone = 2, sub = "saw nothing"),
        ),
        edges = listOf(
            DEdge("you", "app"),
            DEdge("app", "api", label = "order"),
            DEdge("api", "sql", label = "written"),
            DEdge("api", "queue", label = "paid"),
            DEdge("queue", "worker", label = "message"),
            DEdge("worker", "kitchen", label = "never sent", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-app--case-files",
        listOf(
            // 0: "Welcome to Case Files. Our first case..."
            Scene(
                0, "The order that vanished", theCase(),
                beats = listOf(
                    Beat(0, show = setOf("you", "app"), light = setOf("app"), card = BoardCard("The case", "Friday at eight: paid for dal makhani, and the kitchen never saw it.")),
                    Beat(4, show = setOf("api", "sql", "queue", "worker", "kitchen"), light = setOf("api"), card = BoardCard("The cast", "App, dashboard, API, taste service, order worker, photo function.")),
                    Beat(6, light = setOf("api"), card = BoardCard("Zaiqa API", "A program other programs call. The only part that writes to SQL.")),
                    Beat(8, light = setOf("sql"), pulse = "api" to "sql", card = BoardCard("The order is there", "One transaction wrote the order, its two lines and the payment.")),
                ),
            ),
            // 10: "It is the source of truth..."
            Scene(
                10, "Suspect two: the database", stores(),
                beats = listOf(
                    Beat(10, light = setOf("sql"), card = BoardCard("Source of truth", "Orders, order lines, payments, menus, users: what must be correct.")),
                    Beat(12, light = setOf("sql"), card = BoardCard("Clue: thirty thousand pings", "A rider ping table in the payments database, a minute at a time.")),
                    Beat(14, light = setOf("cosmos"), card = BoardCard("Cosmos DB", "Order events, rider pings and dish vectors: huge writes, worldwide.")),
                    Beat(16, light = setOf("cosmos"), card = BoardCard("Dish vector", "A list of numbers that captures what a dish means.")),
                ),
            ),
            // 18: "Then follow it. The API writes to SQL..."
            Scene(
                18, "Following the order", order(),
                beats = listOf(
                    Beat(18, show = setOf("you", "app", "api", "sql", "queue"), light = setOf("queue"), pulse = "api" to "queue", card = BoardCard("The queue", "A line of messages one service leaves for another to pick up later.")),
                    Beat(20, light = setOf("queue"), card = BoardCard("The message was there", "Timed one second past eight. So whatever reads it is next.")),
                    Beat(22, show = setOf("worker", "kitchen"), light = setOf("worker"), pulse = "queue" to "worker", card = BoardCard("The order worker", "A small dot NET program that sends the paid order to the kitchen.")),
                    Beat(24, light = setOf("worker", "sql"), card = BoardCard("The culprit", "The worker had been made a second writer to SQL, and it failed.")),
                    Beat(26, light = setOf("api", "sql"), pulse = "api" to "sql", card = BoardCard("One writer", "The API owns SQL. Everything else reacts through a queue or a feed.")),
                ),
            ),
            // 27: "What else did the customer see?"
            Scene(
                27, "The side cases", stores(),
                beats = listOf(
                    Beat(28, light = setOf("redis"), card = BoardCard("Redis", "A cache: the rider's dot, the hot list, rate limiting.")),
                    Beat(30, light = setOf("redis"), card = BoardCard("Cheap to lose", "A fast copy, quick to rebuild. The order itself is still in SQL.")),
                    Beat(32, light = setOf("blob"), card = BoardCard("Blob Storage", "A file found by its address. SQL keeps the row and the address.")),
                ),
            ),
            // 34: "Which every backup and every restore then drags along..."
            Scene(
                34, "What was fixed", pipeline(),
                beats = listOf(
                    Beat(34, light = setOf("grid", "fn"), pulse = "grid" to "fn", card = BoardCard("Event Grid", "The photo lands, and the function makes the thumbnail and the vector.")),
                    Beat(36, light = setOf("fn", "cosmos"), pulse = "fn" to "cosmos", card = BoardCard("Four fixes", "Pings to Cosmos, photo to Blob, worker back to messages, no waiting.")),
                    Beat(38, light = setOf("taste"), pulse = "taste" to "cosmos", card = BoardCard("The rest of the cast", "Taste service in FastAPI; Entra sign-in; managed identity badges.")),
                    Beat(40, light = setOf("blob"), card = BoardCard("Key Vault, App Configuration", "The payment key in a safe; the surge fee as a setting.")),
                ),
            ),
            // 41: "Then what does this case teach me for the exam?"
            Scene(
                41, "Case closed", exam(),
                beats = listOf(
                    Beat(42, light = setOf("tables", "writes"), card = phrases),
                    Beat(44, light = setOf("second", "file", "wait"), card = otherTwo),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-app--gossip",
        listOf(
            // 0: "Sit down, because did you hear what the Zaiqa team did this week?"
            Scene(
                0, "The Zaiqa team's week", zaiqaMap(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("Five mistakes", "Five people, five mistakes, and one map of the app we build.")),
                    Beat(2, show = setOf("dash"), light = setOf("app", "dash"), card = BoardCard("Two front doors", "The Kotlin app, and the Angular dashboard a restaurant keeps its menu in.")),
                    Beat(4, show = setOf("api", "sql", "redis", "blob", "queue", "worker", "photo", "cosmos"), light = setOf("api"), pulse = "app" to "api", card = BoardCard("Zaiqa API", "A program other programs call. The only part that writes to SQL.")),
                    Beat(6, light = setOf("sql"), card = BoardCard("Monday: Tom", "Thirty thousand rider pings a minute into the payments database.")),
                    Beat(8, light = setOf("sql"), card = BoardCard("Transaction", "Several changes that either all happen or none of them do.")),
                ),
            ),
            // 10: "Nobody ever queries last Tuesday's dot on a map."
            Scene(
                10, "Where the pings belong", stores(),
                beats = listOf(
                    Beat(10, light = setOf("cosmos"), card = BoardCard("Cosmos DB", "Order events, rider pings and dish vectors: huge writes, worldwide.")),
                    Beat(12, light = setOf("cosmos"), card = BoardCard("Change feed", "An ordered list of what changed, which other work can read later.")),
                    Beat(14, light = setOf("cosmos"), card = BoardCard("Dish vector", "A list of numbers that captures what a dish means.")),
                ),
            ),
            // 16: "Dana. Dana kept the order total only in Azure Managed Redis."
            Scene(
                16, "Tuesday and Wednesday", stores(),
                beats = listOf(
                    Beat(16, light = setOf("redis"), card = BoardCard("Tuesday: Dana", "The order total kept only in Redis, because it was faster.")),
                    Beat(18, light = setOf("redis"), card = BoardCard("Redis", "A cache: the hot list, the rider's position, rate limiting.")),
                    Beat(20, light = setOf("sql"), card = BoardCard("Money is not a cache", "A restart, and nobody could say what four hundred people had paid.")),
                    Beat(22, light = setOf("blob"), card = BoardCard("Wednesday: Marcus", "A four megabyte dish photo saved into a database column.")),
                    Beat(24, light = setOf("blob"), card = BoardCard("Blob Storage", "A file found by its address. SQL keeps the row and the address.")),
                ),
            ),
            // 26: "The intern, and this is the big one."
            Scene(
                26, "Thursday and Friday", order(),
                beats = listOf(
                    Beat(26, show = setOf("you", "app", "api", "sql", "queue", "worker", "kitchen"), light = setOf("worker", "sql"), card = BoardCard("Thursday: the intern", "The worker made a second writer to SQL, to save one call.")),
                    Beat(30, light = setOf("queue"), pulse = "api" to "queue", card = BoardCard("The queue", "The API writes, drops one message, and answers the phone.")),
                    Beat(32, light = setOf("kitchen"), card = BoardCard("Friday: Priya", "The app made to wait for the restaurant's own system to confirm.")),
                    Beat(34, light = setOf("worker", "kitchen"), pulse = "worker" to "kitchen", card = BoardCard("Never make the phone wait", "Put it on the queue, and let the worker try again.")),
                ),
            ),
            // 36: "The quiet parts. A restaurant uploads a photo..."
            Scene(
                36, "What went right", pipeline(),
                beats = listOf(
                    Beat(36, light = setOf("grid", "fn"), pulse = "grid" to "fn", card = BoardCard("Event Grid", "The photo lands, and the function makes the thumbnail and the vector.")),
                    Beat(38, light = setOf("taste"), pulse = "taste" to "cosmos", card = BoardCard("No passwords inside", "Entra signs customers in; every service carries a managed identity.")),
                    Beat(40, light = setOf("blob"), card = BoardCard("Key Vault, App Configuration", "The payment key in a safe; the surge fee as a setting.")),
                ),
            ),
            // 41: "All right. If the exam asks me which store holds what..."
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, light = setOf("tables", "writes"), card = phrases),
                    Beat(44, light = setOf("second", "file", "wait"), card = otherTwo),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-app--late-night",
        listOf(
            // 0: "It is late, and you are still up..."
            Scene(
                0, "Tonight, a long build", zaiqaMap(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("Tonight", "We start building Zaiqa, our own food delivery app, over ten topics.")),
                    Beat(4, show = setOf("dash"), light = setOf("app", "dash"), card = BoardCard("Two front doors", "The Kotlin app, and the Angular dashboard a restaurant keeps its menu in.")),
                    Beat(6, show = setOf("api", "sql", "redis", "blob", "queue", "worker", "photo", "cosmos"), light = setOf("api"), pulse = "app" to "api", card = BoardCard("Zaiqa API", "A program other programs call. Dot NET 9, and the only writer to SQL.")),
                    Beat(8, light = setOf("api", "sql"), pulse = "api" to "sql", card = BoardCard("One writer", "Two writers means two stories about what you were charged.")),
                ),
            ),
            // 10: "Azure SQL Database. It is Zaiqa's source of truth..."
            Scene(
                10, "The four of them", stores(),
                beats = listOf(
                    Beat(10, light = setOf("sql"), card = BoardCard("Source of truth", "Orders, order lines, payments, menus, users: what must be correct.")),
                    Beat(12, light = setOf("sql"), card = BoardCard("Transaction", "Several changes that either all happen or none of them do.")),
                    Beat(14, light = setOf("cosmos"), card = BoardCard("Cosmos DB", "Order events, rider pings and dish vectors: huge writes, worldwide.")),
                    Beat(16, light = setOf("cosmos"), card = BoardCard("Vector, and change feed", "A list of numbers for a dish; an ordered list of what changed.")),
                    Beat(18, light = setOf("cosmos"), card = BoardCard("Who reads the feed", "The ratings prompt, and the payout to the restaurant.")),
                    Beat(20, light = setOf("redis"), card = BoardCard("Redis", "A cache: the rider's dot, the hot list, rate limiting.")),
                    Beat(24, light = setOf("blob"), card = BoardCard("Blob Storage", "A file found by its address. SQL keeps the row and the address.")),
                    Beat(26, light = setOf("sql", "cosmos", "redis", "blob"), card = BoardCard("Four sentences", "SQL correct. Cosmos fast and huge. Redis right now. Blob files.")),
                ),
            ),
            // 27: "Four sentences. Now, you promised me an order."
            Scene(
                27, "One order", order(),
                beats = listOf(
                    Beat(28, show = setOf("you", "app", "api", "sql", "queue", "worker", "kitchen"), light = setOf("api", "sql"), pulse = "api" to "sql", card = BoardCard("Tap pay", "The order, its lines and the payment, written in one transaction.")),
                    Beat(30, light = setOf("queue"), pulse = "api" to "queue", card = BoardCard("Service Bus queue", "A line of messages one service leaves for another to pick up later.")),
                    Beat(32, light = setOf("worker", "kitchen"), pulse = "worker" to "kitchen", card = BoardCard("Why a queue", "Your phone never waits for the restaurant's own system to answer.")),
                ),
            ),
            // 34: "A restaurant uploads a dish photo..."
            Scene(
                34, "The quiet work", pipeline(),
                beats = listOf(
                    Beat(34, light = setOf("grid", "fn"), pulse = "grid" to "fn", card = BoardCard("Event Grid", "The photo lands, and the function makes the thumbnail and the vector.")),
                    Beat(36, light = setOf("taste"), pulse = "taste" to "cosmos", card = BoardCard("No passwords inside", "Entra signs you in; every service carries a managed identity badge.")),
                    Beat(38, light = setOf("cosmos"), card = BoardCard("Key Vault, App Configuration", "The payment key in a safe; the surge fee as a setting.")),
                ),
            ),
            // 40: "Where does tonight's picture stop being true?"
            Scene(
                40, "Where it stops", zaiqaMap(),
                beats = listOf(
                    Beat(40, light = setOf("api"), card = BoardCard("Where it stops", "Our own design, and each part gets a whole episode of its own.")),
                ),
            ),
            // 41: "Before I give in, then. What do I keep for the exam?"
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, light = setOf("tables", "writes"), card = phrases),
                    Beat(44, light = setOf("second", "file", "wait"), card = otherTwo),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-app--couples",
        listOf(
            // 0: "Date night, darling..."
            Scene(
                0, "Date night", zaiqaMap(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("Date night", "Ten courses ahead: we build Zaiqa, our own food delivery app.")),
                    Beat(4, show = setOf("dash"), light = setOf("app", "dash"), card = BoardCard("Two front doors", "The Kotlin app, and the Angular dashboard a restaurant keeps its menu in.")),
                    Beat(6, show = setOf("api", "sql", "redis", "blob", "queue", "worker", "photo", "cosmos"), light = setOf("api"), pulse = "app" to "api", card = BoardCard("Quiz: one writer?", "Why should the API be the only thing that writes to our database?")),
                    Beat(8, light = setOf("api", "sql"), pulse = "api" to "sql", card = BoardCard("Right answer", "One place for the money rules. Two writers, two stories.")),
                ),
            ),
            // 10: "Azure SQL Database, our source of truth..."
            Scene(
                10, "Quizzing each other", stores(),
                beats = listOf(
                    Beat(10, light = setOf("sql"), card = BoardCard("Source of truth", "Orders, order lines, payments, menus, users: what must be correct.")),
                    Beat(12, light = setOf("sql"), card = BoardCard("Transaction", "Several changes that either all happen or none of them do.")),
                    Beat(14, light = setOf("cosmos"), card = BoardCard("Right answer: Cosmos", "Thirty thousand pings a minute, and a change feed to start the rest.")),
                    Beat(16, light = setOf("cosmos"), card = BoardCard("Quiz: what is a vector?", "A list of numbers that captures what a dish means.")),
                    Beat(20, light = setOf("redis"), card = BoardCard("Redis", "A cache: the rider's dot, the hot list, rate limiting.")),
                    Beat(22, light = setOf("sql"), card = BoardCard("Right answer: money is SQL", "A restart would lose what four hundred people had paid.")),
                    Beat(24, light = setOf("blob"), card = BoardCard("Quiz: a four megabyte photo", "Where does the file live, and what does SQL keep?")),
                    Beat(26, light = setOf("blob"), card = BoardCard("No kiss for that one", "The file goes in Blob Storage; SQL keeps the row and the address.")),
                    Beat(28, light = setOf("sql", "cosmos", "redis", "blob"), card = BoardCard("Four sentences", "SQL correct. Cosmos fast and huge. Redis right now. Blob files.")),
                ),
            ),
            // 29: "Then take me through one order. Friday, eight, I tap pay."
            Scene(
                29, "One order", order(),
                beats = listOf(
                    Beat(30, show = setOf("you", "app", "api", "sql", "queue", "worker", "kitchen"), light = setOf("api", "sql"), pulse = "api" to "sql", card = BoardCard("Tap pay", "The order, its lines and the payment, written in one transaction.")),
                    Beat(32, light = setOf("queue", "worker"), pulse = "queue" to "worker", card = BoardCard("Right answer", "A queue, Service Bus, and the worker tells the restaurant.")),
                    Beat(34, light = setOf("worker", "kitchen"), pulse = "worker" to "kitchen", card = BoardCard("Why a queue", "Nobody should wait for somebody else's busy system.")),
                ),
            ),
            // 36: "The restaurant uploads it, Event Grid notices..."
            Scene(
                36, "The quiet work", pipeline(),
                beats = listOf(
                    Beat(36, light = setOf("grid", "fn"), pulse = "grid" to "fn", card = BoardCard("Event Grid", "The photo lands, and the function makes the thumbnail and the vector.")),
                    Beat(38, light = setOf("taste"), pulse = "taste" to "cosmos", card = BoardCard("No passwords inside", "Entra signs you in; every service carries a managed identity badge.")),
                    Beat(40, light = setOf("cosmos"), card = BoardCard("Key Vault, App Configuration", "The payment key in a safe; the surge fee as a setting.")),
                ),
            ),
            // 41: "Last question of the night, for the exam."
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, light = setOf("tables", "writes"), card = phrases),
                    Beat(44, light = setOf("second", "file", "wait"), card = otherTwo),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
