package com.shahaabapps.core.content

/** Cosmos DB performance: RUs, indexing policies and consistency: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesCosmosPerf {
    private val classic = Podcast(
        "ai-cosmos-perf",
        listOf(
            maya("Hi, and welcome back. Today is about making a database cheaper and faster. The database is Azure Cosmos DB, Microsoft's cloud database. It keeps your app's data as lots of small records, and each record is called an item."),
            leo("Cheaper and faster. Two of my favourite words. Where do we start?"),
            maya("With an app. Say you're building the Two Tracks Coach, a study assistant in an Android app, with a small Python backend on Azure. The learners' notes live in Cosmos DB, in a container called notes. A container is like one folder that holds one kind of item."),
            leo("And using that folder costs money?"),
            maya("Every single call does. The price is counted in request units, RUs for short. A request unit is the price tag on one call to the database. Reading one small item, about a page of text, when you know its id, costs about 1 RU. Saving costs more, and searching costs more again."),
            leo("So how do I pay? Coins in a slot?"),
            maya("[chuckle] Close. You buy a budget per second, called throughput. Throughput is how much work the database will do for you each second. Give the notes container 1,000 RUs per second, and the app may spend up to 1,000 RUs in any one second."),
            leo("And if the app asks for 1,500 in one second?"),
            maya("Then some calls are turned away with 429. That's a status code, a short number the database sends back. 429 means too many requests: this second's budget is spent. The reply also says how many milliseconds to wait before trying again."),
            leo("So my app just falls over?"),
            maya("Usually not. The SDK, the Python library your code uses to talk to Cosmos DB, waits and retries a 429 for you a few times. A few 429s under load are normal. Lots of them mean you need a bigger budget, a cheaper query, meaning a cheaper search, or a better partition key."),
            leo("Partition key. What's that, in plain words?"),
            maya("It's the label that decides which shelf an item sits on. The Coach uses the learner's id, so each learner's notes sit together. The budget is shared out between shelves. If one shelf gets nearly all the traffic, that one is told to slow down while the others sit idle."),
            leo("Okay. And how do I know what each call costs? Do I guess?"),
            maya("Never guess. Every reply carries a header, a small label stuck on the response, called the request charge. It says how many RUs that call used. Print it on real calls. The charge grows with the item's size, with how much of it is indexed, and with how strict the read is."),
            leo("Which calls are cheapest?"),
            maya("A point read: fetching one item when you know its id and its partition key, like taking a book from a shelf you already know. Next is a single-partition query, a search on one shelf. The most expensive is a cross-partition query, which has to search every shelf."),
            leo("Got it. And is there only one way to buy the budget?"),
            maya("Three ways. Manual is a fixed number of RUs per second, from 400 up, and you change it yourself. It suits steady load. Serverless has no budget to set at all: you pay for each RU you use. It suits testing, or a small app that's busy now and then, not heavy load all day."),
            leo("And the third?"),
            maya("Autoscale. You set only a maximum. The budget then moves between ten percent of that maximum and the maximum, following the load, and you're billed for the highest point reached each hour. For example, the Coach is quiet all day, then learners log in at nine and 429s appear. Autoscale to 4,000 covers that peak and drops back to 400."),
            leo("Nice. So that's buying. How do I make each call cheaper?"),
            maya("Two settings. The first is the indexing policy. An index is like the index at the back of a book: it lets the database find items without reading every one. The indexing policy says which fields of an item go into that index."),
            leo("And what goes in by default?"),
            maya("Every field of every item. That makes any filter fast, but every save pays to update the index for every field. The Coach's notes have a long text field that nobody ever filters on, and every save was paying to index it."),
            leo("So I tell it to skip the text."),
            maya("Exactly. You exclude that path. A path is just where a field sits in the item, here slash text. The team excluded it, and saving a note dropped from about 14 RUs to 9. Measured with the request charge, not guessed."),
            leo("What else goes in the policy?"),
            maya("A composite index. Picture a small app screen that lists a learner's notes sorted by topic, then newest first. That sort is an ORDER BY, the part of a query that puts results in order, here on two fields at once. And it fails with a missing index error."),
            leo("Why? Every field is already indexed."),
            maya("Each on its own, yes. A sort on two fields needs a composite index: one index over several fields together, in the same order and direction as the query. So, topic ascending, then created date descending. Add it, and the same query works."),
            leo("You said the second setting was about how strict a read is."),
            maya("That's consistency. Cosmos DB keeps several copies of your data, and a save takes a moment to reach them all. Consistency decides what a reader may see in that moment. There are five levels, from strictest to loosest."),
            leo("Five. Of course there are. [pause] Go on."),
            maya("Strong always shows the latest save, and costs about twice as much per read. Bounded staleness may lag, but only by a set number of versions or a set time. Session, the default, always shows you your own saves, in order. Consistent prefix never shows things out of order, but may lag. Eventual is cheapest, and catches up eventually."),
            leo("Which one do I want?"),
            maya("Session, for most apps. A learner who saves a note sees it at once; other learners may see it a moment later. You set the default on the account, the top-level Cosmos DB resource. A client, the object your code connects with, can then ask for a weaker level, never a stronger one. The Coach's nightly report uses an eventual client, because it can lag and costs less."),
            leo("The usual mistakes?"),
            maya("Guessing RU costs instead of reading the request charge. Indexing long text nobody filters on. Picking strong by habit and paying double for reads. Treating every 429 as an outage, when the SDK already retried. And expecting a client to ask for a stronger level than the account default."),
            leo("So what do I remember for the exam?"),
            maya("If the question says 429s at a morning peak and quiet the rest of the day, the answer is autoscale. If an ORDER BY on two fields fails, a composite index. If saves cost too much and fields are never filtered, exclude those paths. If users must see their own saves and nothing stricter, session. And a client can only weaken consistency."),
            leo("And anything to try today?"),
            maya("Print the request charge for a point read, a single-partition query and a cross-partition query. Exclude the text field from the index, and compare the charge of an upsert, a save, before and after. Run an ORDER BY on two fields, see it fail, then add the composite index. And check the account's default consistency in the portal, Azure's website."),
            leo("Cheaper, faster, and measured. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-cosmos-perf",
        listOf(
            maya("Welcome to Case Files. Today's case: The Monday Morning Meltdown. Revise Owl is a small Android flashcard app, with a Python backend on Azure. Its data lives in Azure Cosmos DB, Microsoft's cloud database. Every Monday at nine, the app freezes, errors pile up, and by ten it's fine again."),
            leo("Detective Leo, reporting. A crime that clocks in at nine. What do the errors say?"),
            maya("They're all 429. That's a status code, a short number the database sends back. 429 means too many requests. To read it, you need one idea first: in Cosmos DB, every call has a price, counted in request units, RUs."),
            leo("A price on every call? What is an RU, in plain words?"),
            maya("The price tag on one call. Reading one small item, about a page of text, by its id, costs about 1 RU. Saving costs more; searching more again. Under the price tag, an RU rolls the processor time, memory and disk reads a call uses into one number."),
            leo("And Revise Owl pays how?"),
            maya("With throughput: a budget of RUs per second. The cards container has a fixed 1,000 RUs per second. A container is like a folder holding one kind of item. In any second, if the calls need more than 1,000 RUs, the extra ones get 429, with a note saying how many milliseconds to wait."),
            leo("Then my first suspect is obvious. Cosmos DB is down. It's an outage."),
            maya("[pause] Wrong suspect. The SDK, the Python library the backend uses, waits and retries a 429 by itself a few times. A few under load are normal. Lots of them, every Monday at nine, mean the budget is too small for that hour, and fine for the rest of the week."),
            leo("Then buy a bigger budget. Set it to 4,000, all week."),
            maya("And pay peak prices for every quiet hour. Here's the fix. Autoscale. You set a maximum, say 4,000, and the budget moves between ten percent of it and the maximum, following the load. You're billed for the highest point reached each hour. Monday at nine, it rises. By ten, it settles back to 400."),
            leo("How does it decide when to rise?"),
            maya("It watches how much of the budget is being used and scales up straight away, no restart, no warning. There's a third way too: serverless, with no budget at all, where you pay for each RU used. Good for testing or a tiny app, not for heavy load every day."),
            leo("Case closed, then. Autoscale. Lunch?"),
            maya("Not yet. A second witness: the finance team. Saving a card costs about 14 RUs. They expected half that. Where do you look?"),
            leo("I'd guess the cards are too big."),
            maya("Never guess. Every reply carries a header, a small label on the response, called the request charge, saying exactly what that call cost. The team printed it, and the cost was in the index. An index is like the index at the back of a book: it lets the database find items without reading every one."),
            leo("And an index makes things slower?"),
            maya("It makes searches faster and saves dearer. By default, Cosmos DB indexes every field of every item, so each save updates the index for every field. Revise Owl's cards carry a long text field nobody filters on. The indexing policy, the container's rule for what goes in the index, can exclude that path."),
            leo("Then my suspect is the index itself. Turn indexing off completely."),
            maya("[sigh] And every search has to read every card, which is far worse. Exclude only what nobody filters on. A path is where a field sits, here slash text. The team excluded it, and saving dropped from about 14 RUs to 9, measured with the request charge."),
            leo("Any more clues in the file?"),
            maya("One more. The deck screen lists cards sorted by deck, then by due date, and that query, the search behind it, fails with a missing index error. The sort is an ORDER BY, the part of a query that puts results in order, on two fields at once."),
            leo("But every field is indexed. You just said so."),
            maya("Each one on its own. A sort on two fields needs a composite index: one index over several fields together, in the same order and direction as the query. Deck ascending, due date ascending. Under the hood, the database keeps the cards already sorted that way, so it just reads them in order."),
            leo("And the last suspect. A teammate set the account, the top-level Cosmos DB resource, to strong consistency, to make things more reliable."),
            maya("Ah, consistency. Cosmos DB keeps several copies of your data, and a save takes a moment to reach them all. Consistency decides what a reader may see in that moment. Strong always shows the latest save, and costs about twice as much per read. That made Monday worse."),
            leo("So what should it be?"),
            maya("Session, the default. A user always sees their own saves, in order. Under the hood, the SDK carries a session token, a small receipt for your last save, and reads come from a copy that has caught up to it. Looser levels cost less: consistent prefix may lag but never shows things out of order, and eventual catches up eventually."),
            leo("And can the code ask for something stricter than the account's setting?"),
            maya("No. A client, the object your code connects with, may ask for a weaker level than the account's default, never a stronger one."),
            leo("Any loose ends?"),
            maya("One, and it's where autoscale stops working. The budget is shared out between shelves, chosen by the partition key, the label that decides where an item sits. If one shelf gets nearly all the traffic, that shelf still gets 429s, however high the maximum. The fix there is a better partition key."),
            leo("And which calls are cheapest, for the record?"),
            maya("A point read: one item fetched by its id and partition key, like a book from a known shelf. Then a single-partition query, a search on one shelf. Dearest is a cross-partition query, across every shelf. The Cosmos SDK episode covers those in Python, and the vector search episode adds vector indexes to the indexing policy."),
            leo("So, the case summary for the exam?"),
            maya("If 429s come at a morning peak and the rest of the day is quiet, autoscale. If an ORDER BY on two fields fails, a composite index. If saves cost too much and a field is never filtered, exclude its path. If users must see their own saves and nothing stricter, session. And a client can only weaken consistency."),
            leo("Case closed. Anything to try today?"),
            maya("Print the request charge for a point read, a single-partition query and a cross-partition query. Exclude the text field from the index, and compare the charge of an upsert, a save, before and after. Run an ORDER BY on two fields, see it fail, then add the composite index. And check the account's default consistency in the portal, Azure's website."),
            leo("Mondays at nine. Solved at last. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-cosmos-perf",
        listOf(
            maya("Okay, did you hear about the Coach team's database bill this week? Grab a coffee. The Two Tracks Coach, the study assistant in the Android app, with its Python backend on Azure, keeps its notes in Azure Cosmos DB, Microsoft's cloud database. And this week, everyone found a new way to pay for it."),
            leo("[laugh] I heard finance sent an email with three question marks. How do you even pay for a database?"),
            maya("Per call. Every call has a price, counted in request units, RUs. An RU is the price tag on one call. Reading one small item by its id, about a page of text, costs about 1 RU. Under the picture, an RU rolls processor time, memory and disk reads into one number."),
            leo("And you pay for each RU as you go?"),
            maya("Mostly you buy a budget per second, called throughput. The notes container, which is like a folder of one kind of item, had 1,000 RUs per second. Go over it in a second, and calls get 429: a status code meaning too many requests. The reply even says how many milliseconds to wait."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. Tom was asked what a note search costs, and he guessed. Said two RUs, very confident. It was twelve."),
            leo("How was he supposed to know?"),
            maya("Every reply carries a header, a small label on the response, called the request charge. It says exactly how many RUs that call used. You print it on real calls. Tom does now. It's on a sticky note on his screen."),
            leo("[chuckle] What else is on the sticky note?"),
            maya("Which calls are cheapest. A point read, one item fetched by its id and partition key, the label that picks its shelf. Then a single-partition query, a search on one shelf. Dearest, a cross-partition query, which searches every shelf."),
            leo("Very tidy. And Tuesday?"),
            maya("Tuesday was Dana. Dana noticed that saving one note cost about 14 RUs. The cause was the index. An index is like the index at the back of a book: it finds items without reading every one. And by default, Cosmos DB indexes every field of every item."),
            leo("So every save updates the index for every field."),
            maya("Including the long text of the note, which nobody ever filters on. Dana changed the indexing policy, the container's rule for what goes in the index, and excluded that path. A path is where a field sits, here slash text. Saves dropped to 9 RUs. Dana got a cake."),
            leo("Good for Dana. Wednesday?"),
            maya("Wednesday, Marcus. Marcus set the account, the top-level Cosmos DB resource, to strong consistency, because strong sounds safe. Consistency is how fresh a read must be. Cosmos DB keeps several copies of your data, and a save takes a moment to reach them all."),
            leo("And strong means?"),
            maya("Every reader always sees the latest save. Lovely, and about twice the cost per read. The default is session: you always see your own saves, in order, and others may see them a moment later. Under the hood, the SDK, the library the code uses, carries a session token, a little receipt for your last save."),
            leo("So what did Marcus actually need?"),
            maya("Session. A learner who saves a note must see it at once, nothing stricter. Bounded staleness lags by a set limit at most. Consistent prefix may lag, but never out of order. Eventual is cheapest, and catches up eventually. Marcus put it back to session before anyone in finance noticed. Almost."),
            leo("Thursday. I can feel it."),
            maya("Thursday, the intern. Bless them. At nine every morning, learners log in, and the logs fill with 429s. The intern opened an outage ticket, red, urgent, everyone tagged."),
            leo("Was it an outage?"),
            maya("No. The SDK had already waited and retried those calls. A few 429s under load are normal. Lots, at one peak, mean the budget is too small for that hour. So the team moved to autoscale. You set a maximum, 4,000 here, and the budget moves between ten percent of it and the maximum, following the load."),
            leo("And they pay for the maximum all day?"),
            maya("No, for the highest point reached each hour. Quiet hours cost a tenth. The other two ways to buy are manual, a fixed budget you change yourself, and serverless, where you pay per RU used, for testing or tiny apps, not heavy daily load."),
            leo("And Friday? There's always a Friday."),
            maya("The Friday. Priya. Priya never makes mistakes, which is why it's still the talk of the kitchen. She was writing the nightly report, and asked her client, the object her code connects with, for strong consistency, while the account default was session."),
            leo("Does that work?"),
            maya("No. A client can ask for a weaker level than the account default, never a stronger one. And the report didn't even need fresh data. So Priya flipped it to an eventual client, the cheapest level, which may lag but catches up. Cheaper report, same numbers by morning."),
            leo("Did anything else come up?"),
            maya("One thing, and it's where autoscale stops working. Each learner's notes share a shelf, because the partition key is the learner's id, and the budget is shared between shelves. If one shelf gets most of the traffic, it gets 429s however high the maximum goes. The fix there is a better partition key."),
            leo("And the sorted notes list? I heard someone shouting at it."),
            maya("That was Tom again. A list sorted by topic, then newest first, is an ORDER BY on two fields, the part of a query, a database search, that sorts. It failed with a missing index error. It needs a composite index: one index over both fields, in the query's order and direction. The Cosmos SDK episode has the query side of that."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If 429s come at a morning peak and the day is quiet, autoscale. If an ORDER BY on two fields fails, a composite index. If saves cost too much and a field is never filtered, exclude its path. If users must see their own saves and nothing stricter, session. And a client can only weaken consistency."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Print the request charge for a point read, a single-partition query and a cross-partition query. Exclude the text field from the index, and compare the charge of an upsert, a save, before and after. Run an ORDER BY on two fields, see it fail, then add the composite index. And check the account's default consistency in the portal, Azure's website."),
            leo("Measure first. Got it, Tom. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-cosmos-perf",
        listOf(
            nova("Hey, you. It's late, the kettle's on, and it's just the three of us. Me, Ivy, and you, still up. [pause] Tonight, let's talk about what a database charges you, and how to make it charge you less."),
            ivy("Money, at this hour? Nova, you know how to keep me awake."),
            nova("Only the gentle kind. Picture the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. Its notes live in Azure Cosmos DB, Microsoft's cloud database. And in Cosmos DB, every call has a price."),
            ivy("Every call. Even a little read, late at night?"),
            nova("Even that. The price is counted in request units, RUs. An RU is the price tag on one call. Reading one small item, about a page of text, by its id, costs about 1 RU. [pause] Saving costs more. Searching costs more again."),
            ivy("And what is an RU, really? Under the price tag."),
            nova("One number that rolls up the processor time, memory and disk reads a call uses. So you don't count those three yourself. You just read one price."),
            ivy("And how do I pay it?"),
            nova("With throughput: a budget of RUs per second. The notes container, a folder for one kind of item, might get 1,000 RUs per second. Ask for more than that in one second, and some calls get 429. A status code, a short number back from the database, meaning too many requests."),
            ivy("Turned away at the door. How rude."),
            nova("Politely, though. The reply says how many milliseconds to wait. And the SDK, the Python library your code uses, waits and retries for you, a few times. A few 429s are normal. [pause] Lots of them mean the budget is too small, or something else is wrong."),
            ivy("How do I know what each call costs? Can I just ask?"),
            nova("You can. Every reply carries a header, a small label on the response, called the request charge. It tells you the RUs that call used. Print it on real calls, and never guess."),
            ivy("Mm. And which calls are cheapest?"),
            nova("A point read. You fetch one item by its id and its partition key, the label that decides which shelf it sits on. Then a single-partition query, a search on one shelf. And the dearest, a cross-partition query, which searches every shelf."),
            ivy("And the budget. Is it always a fixed number?"),
            nova("Three ways. Manual: a fixed number, from 400 up, that you change yourself. Serverless: no budget, you pay per RU used, for testing or a tiny app. [pause] And autoscale. You set a maximum, and the budget moves between ten percent of it and the maximum, following the load. Billed for the highest point each hour."),
            ivy("So when learners all log in at nine..."),
            nova("Autoscale rises to meet them, and settles back down when they've gone. That's the exam's answer for a morning peak and a quiet day. [pause] But it stops working when one shelf gets all the traffic. The budget is shared between shelves, so a hot one still gets 429s. Then you need a better partition key."),
            ivy("Tell me how to make each call cheaper. Slowly."),
            nova("The indexing policy. An index is like the index at the back of a book. It finds items without reading every one. And the indexing policy is the container's rule for which fields go in it. By default, every field of every item."),
            ivy("Every field. That sounds expensive."),
            nova("For saves, it is. Each save updates the index for every field. The Coach's notes have a long text field nobody filters on. So you exclude that path, where the field sits, slash text. [pause] Saving a note went from about 14 RUs to 9."),
            ivy("And sorting? I like things in order."),
            nova("I know you do. Sort notes by topic, then newest first, and that's an ORDER BY on two fields. The part of a query that sorts. It fails with a missing index error, until you add a composite index: one index over both fields, in the query's own order and direction."),
            ivy("And the last setting? You promised me consistency."),
            nova("I did. Cosmos DB keeps several copies of your data, and a save takes a moment to reach them all. Consistency decides what a reader sees in that moment. Five levels. Strong, always the latest, at about twice the read cost. Bounded staleness, lagging by a set limit. Session, which we'll come to. Consistent prefix, never out of order. And eventual, the cheapest."),
            ivy("Which one stays with me?"),
            nova("Session, the default. You always see your own saves, in order. Under the hood, the SDK carries a session token, a little receipt for your last save, and your reads come from a copy that has caught up to it. [pause] A client, the object your code connects with, may ask for weaker than the default you set on the account, your top-level Cosmos DB resource. Never stronger."),
            ivy("And the nightly report, while we sleep?"),
            nova("An eventual client. It can lag a little, and it costs less. The Cosmos SDK episode shows how to set that client up in Python. And the vector search episode adds vector indexes to this same indexing policy."),
            ivy("[sigh] Mm. Before I drift off. What do I remember for the exam?"),
            nova("If 429s come at a morning peak and the day is quiet, autoscale. If an ORDER BY on two fields fails, a composite index. If saves cost too much and a field is never filtered, exclude its path. If you must see your own saves and nothing stricter, session. And a client can only weaken consistency."),
            ivy("And something to try today, before bed?"),
            nova("Print the request charge for a point read, a single-partition query and a cross-partition query. Exclude the text field from the index, and compare the charge of an upsert, a save, before and after. [pause] Run an ORDER BY on two fields, see it fail, then add the composite index. And check the account's default consistency in the portal, Azure's website."),
            ivy("Cheaper by morning. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-cosmos-perf",
        listOf(
            nova("Tonight is date night, darling. And tonight, Rafe, we're talking about budgets."),
            rafe("Romance at its finest. Is this about the restaurant bill?"),
            nova("[laugh] It's about Azure Cosmos DB, Microsoft's cloud database, where every call has a price. Picture the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. Its notes live in Cosmos DB, and every read and every save costs something."),
            rafe("Everything worth having costs something. What's the currency?"),
            nova("Request units, RUs. An RU is the price tag on one call. Reading one small item, about a page of text, by its id, costs about 1 RU. Under the picture, an RU rolls up the processor time, memory and disk reads a call uses, into one number."),
            rafe("And we pay how? I hope not by the minute."),
            nova("By the second. You buy throughput, a budget of RUs per second, for a container, which is like a folder for one kind of item. Spend more than that in a second, and calls get 429, a status code meaning too many requests."),
            rafe("Turned away for being too keen. I know the feeling."),
            nova("And the SDK, the Python library the code uses, waits the milliseconds the reply asks for and tries again, a few times. Persistent, like you."),
            rafe("So, a quiz, darling. The Coach is quiet all day, but at nine every morning learners log in, and 429s appear. Which way to buy the budget?"),
            nova("Autoscale. You set a maximum, and the budget moves between ten percent of it and the maximum, following the load. Billed for the highest point each hour."),
            rafe("Right answer. You rise to the occasion. [kiss]"),
            nova("Mm. And the other two? Manual is a fixed budget, from 400 up, that you change yourself. Serverless has no budget at all; you pay per RU used, for testing or a tiny app, not heavy load every day."),
            rafe("And how do I know what a call really cost me? I don't like surprises on the bill."),
            nova("Every reply carries a header, a small label on the response, called the request charge, with the RUs that call used. Print it on real calls. Never guess."),
            rafe("Which calls are cheapest, then?"),
            nova("A point read: one item by its id and its partition key, the label that decides which shelf it sits on. Then a single-partition query, a search on one shelf. The dearest, a cross-partition query, searches every shelf. Some people just can't commit to one shelf."),
            rafe("[chuckle] Not me. Now, the index. Tell me something I don't know."),
            nova("An index is like the index at the back of a book. It finds items without reading every one. The indexing policy is the container's rule for what goes in it. By default, every field of every item, so every save updates the index for every field."),
            rafe("My turn to ask. Saving a note costs about 14 RUs, and the note's long text field is never filtered on. What do we do?"),
            nova("Exclude that path from the indexing policy. A path is where a field sits, here slash text. Saves dropped to about 9 RUs."),
            rafe("Right answer. Some things are better left out. [kiss]"),
            nova("My question. A list sorted by topic, then newest first. An ORDER BY on two fields, the part of a query that sorts. It fails with a missing index error. Why?"),
            rafe("Because every field is indexed on its own. So, turn indexing off?"),
            nova("No kiss for that one. Then every search reads every note. A sort on two fields needs a composite index: one index over both fields, in the query's order and direction."),
            rafe("Fine. Now, consistency. Something I'm famous for."),
            nova("Cosmos DB keeps several copies of your data, and a save takes a moment to reach them all. Consistency decides what a reader may see in that moment. Five levels, strictest first. Strong always shows the latest save, at about twice the read cost. Bounded staleness lags by a set limit at most. Session we'll come to. Consistent prefix never shows things out of order. Eventual is cheapest."),
            rafe("Then a quiz. A learner must always see the note they just saved, and nothing stricter is needed. Which level?"),
            nova("Session, the default. You always see your own saves, in order. The SDK carries a session token, a little receipt for your last save, and your reads come from a copy that has caught up to it."),
            rafe("Right answer. What happens in our session stays in our session. [kiss]"),
            nova("Now you. The account, the top-level Cosmos DB resource, defaults to session. Can a client, the object your code connects with, ask for strong?"),
            rafe("No. A client may ask for weaker than the default, never stronger. The nightly report uses eventual, the cheapest, because it can lag."),
            nova("Right answer. You know your limits. [kiss]"),
            rafe("And where does autoscale stop working? Every romance has its limits."),
            nova("When one shelf gets all the traffic. The budget is shared between shelves, so a hot one still gets 429s, however high the maximum. Then you need a better partition key. The Cosmos SDK episode covers point reads and queries, and the vector search episode adds vector indexes to this same policy."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("If 429s come at a morning peak and the day is quiet, autoscale. If an ORDER BY on two fields fails, a composite index. If saves cost too much and a field is never filtered, exclude its path. If you must see your own saves, session. And a client can only weaken consistency."),
            rafe("Five for five. That one earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Print the request charge for a point read, a single-partition query and a cross-partition query. Exclude the text field from the index, and compare the charge of an upsert, a save, before and after. Run an ORDER BY on two fields, see it fail, then add the composite index. And check the account's default consistency in the portal, Azure's website."),
            nova("Worth every request unit. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun budget(app: String, cont: String, rus: String) = DiagramSpec(
        "Each call spends RUs from the container's per-second budget: within it the call succeeds, over it the call gets 429.",
        nodes = listOf(
            DNode("app", app, 0.2f, 0.5f, w = 0.34f, h = 0.22f, tone = 1, sub = "Python"),
            DNode("cont", cont, 0.55f, 0.5f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER, sub = rus),
            DNode("ok", "200 OK", 0.86f, 0.2f, w = 0.26f, h = 0.2f, kind = NodeKind.PILL, sub = "charge in header"),
            DNode("busy", "429", 0.86f, 0.8f, w = 0.26f, h = 0.2f, kind = NodeKind.PILL, tone = 2, sub = "wait, then retry"),
        ),
        edges = listOf(
            DEdge("app", "cont", "call"),
            DEdge("cont", "ok", "in budget"),
            DEdge("cont", "busy", "over budget"),
        ),
        aspect = 0.8f,
    )

    private fun calls() = DiagramSpec(
        "The request charge says what each call cost: a point read is cheapest, a cross-partition query the dearest.",
        nodes = listOf(
            DNode("header", "Request charge", 0.5f, 0.15f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "the RUs each call used"),
            DNode("point", "Point read", 0.17f, 0.66f, w = 0.3f, h = 0.3f, sub = "cheapest"),
            DNode("single", "Single-partition", 0.5f, 0.66f, w = 0.3f, h = 0.3f, sub = "one shelf"),
            DNode("cross", "Cross-partition", 0.83f, 0.66f, w = 0.3f, h = 0.3f, tone = 2, sub = "every shelf: dearest"),
        ),
        edges = listOf(
            DEdge("header", "point"),
            DEdge("header", "single"),
            DEdge("header", "cross"),
        ),
        aspect = 0.8f,
    )

    private fun shelves() = DiagramSpec(
        "The budget is shared between shelves, so one hot shelf gets 429s however high the maximum goes.",
        nodes = listOf(
            DNode("budget", "Container budget", 0.5f, 0.16f, w = 0.56f, h = 0.2f, tone = 1, sub = "shared between shelves"),
            DNode("hot", "Hot shelf", 0.17f, 0.68f, w = 0.3f, h = 0.32f, kind = NodeKind.CYLINDER, tone = 2, sub = "429s"),
            DNode("p2", "Shelf", 0.5f, 0.68f, w = 0.3f, h = 0.32f, kind = NodeKind.CYLINDER, sub = "idle"),
            DNode("p3", "Shelf", 0.83f, 0.68f, w = 0.3f, h = 0.32f, kind = NodeKind.CYLINDER, sub = "idle"),
        ),
        edges = listOf(
            DEdge("budget", "hot", "most traffic"),
            DEdge("budget", "p2"),
            DEdge("budget", "p3"),
        ),
        aspect = 0.8f,
    )

    private fun throughput() = DiagramSpec(
        "Three ways to buy throughput: manual for steady load, autoscale for peaks, serverless for tests and small apps.",
        nodes = listOf(
            DNode("manual", "Manual", 0.17f, 0.3f, w = 0.3f, h = 0.3f, sub = "fixed, from 400"),
            DNode("autoscale", "Autoscale", 0.5f, 0.3f, w = 0.3f, h = 0.3f, tone = 1, sub = "10% of max to max"),
            DNode("serverless", "Serverless", 0.83f, 0.3f, w = 0.3f, h = 0.3f, sub = "pay per RU"),
            DNode("load", "The load", 0.5f, 0.8f, w = 0.56f, h = 0.2f, kind = NodeKind.PILL, sub = "steady, peaks or rare"),
        ),
        edges = listOf(
            DEdge("load", "manual", dashed = true),
            DEdge("load", "autoscale", "9 a.m. peak"),
            DEdge("load", "serverless", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun indexing() = DiagramSpec(
        "The indexing policy decides what each save indexes: exclude fields nobody filters on, add a composite index for two-field sorts.",
        nodes = listOf(
            DNode("item", "A note item", 0.25f, 0.16f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "title, topic, text"),
            DNode("index", "The index", 0.75f, 0.16f, w = 0.44f, h = 0.2f, tone = 1, sub = "like a book's index"),
            DNode("excl", "Exclude /text", 0.25f, 0.5f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "long, never filtered"),
            DNode("comp", "Composite index", 0.75f, 0.5f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "sort on two fields"),
            DNode("policy", "Indexing policy", 0.5f, 0.84f, w = 0.7f, h = 0.18f, tone = 2, sub = "what goes in the index"),
        ),
        edges = listOf(
            DEdge("item", "index", "every save"),
            DEdge("policy", "excl"),
            DEdge("policy", "comp"),
        ),
        aspect = 0.8f,
    )

    private fun consistency() = DiagramSpec(
        "Five consistency levels from strictest to loosest; stricter reads cost more, and session is the default.",
        nodes = listOf(
            DNode("strong", "Strong", 0.31f, 0.1f, w = 0.58f, h = 0.16f, tone = 2, sub = "always the latest save"),
            DNode("bounded", "Bounded staleness", 0.31f, 0.3f, w = 0.58f, h = 0.16f, sub = "lags by a set limit"),
            DNode("session", "Session", 0.31f, 0.5f, w = 0.58f, h = 0.16f, tone = 1, sub = "your own saves (default)"),
            DNode("prefix", "Consistent prefix", 0.31f, 0.7f, w = 0.58f, h = 0.16f, sub = "never out of order"),
            DNode("eventual", "Eventual", 0.31f, 0.9f, w = 0.58f, h = 0.16f, sub = "cheapest, catches up"),
            DNode("cost", "Stricter costs more", 0.81f, 0.5f, w = 0.34f, h = 0.4f, kind = NodeKind.NOTE, sub = "strong: about 2x reads"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's performance questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("peak", "Morning peaks", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "autoscale"),
            DNode("sort", "Sort on two fields", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "composite index"),
            DNode("writes", "Saves cost too much", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "exclude unused paths"),
            DNode("own", "See your own saves", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "session"),
            DNode("many", "Lots of 429s", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "budget, query or key"),
            DNode("stronger", "Stronger than default?", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "no: only weaker"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("peak", "sort", "writes", "own", "many", "stronger")
    private val chargeCode = BoardCard("Python", "container.client_connection.last_response_headers[\"x-ms-request-charge\"]", code = true)
    private val sortCode = BoardCard("Query", "SELECT * FROM c WHERE c.userId = @u ORDER BY c.topic ASC, c.createdAt DESC", code = true)
    private val today = BoardCard("Today", "Print charges, exclude /text, add a composite index, check the default.")

    private fun classicBoard() = PodcastBoard(
        "ai-cosmos-perf",
        listOf(
            // 0: "Hi, and welcome back. Today is about making a database cheaper and faster..."
            Scene(
                0, "Every call has a price", budget("Coach API", "notes container", "1,000 RU/s"),
                beats = listOf(
                    Beat(0, show = setOf("app", "cont"), card = BoardCard("Azure Cosmos DB", "Microsoft's cloud database: your app's data as small records, called items.")),
                    Beat(2, light = setOf("cont"), card = BoardCard("Container", "Like one folder holding one kind of item: here, the notes.")),
                    Beat(4, light = setOf("app", "cont"), pulse = "app" to "cont", card = BoardCard("Request unit (RU)", "The price tag on a call: about 1 RU to read a small item by its id.")),
                    Beat(6, light = setOf("cont"), card = BoardCard("Throughput", "A budget of RUs per second: 1,000 RU/s here.")),
                    Beat(8, show = setOf("busy"), light = setOf("busy"), pulse = "cont" to "busy", card = BoardCard("429", "Too many requests: this second's budget is spent. Wait, then retry.")),
                    Beat(10, show = setOf("ok"), light = setOf("ok", "busy"), card = BoardCard("The SDK retries", "A few 429s under load are normal. Lots mean something must change.")),
                ),
            ),
            // 11: "Partition key. What's that, in plain words?"
            Scene(
                11, "Shelves", shelves(),
                beats = listOf(
                    Beat(12, light = setOf("hot"), pulse = "budget" to "hot", card = BoardCard("Partition key", "The label that picks an item's shelf: here, the learner's id.")),
                ),
            ),
            // 13: "Okay. And how do I know what each call costs? Do I guess?"
            Scene(
                13, "What did it cost?", calls(),
                beats = listOf(
                    Beat(14, show = setOf("header"), light = setOf("header"), card = chargeCode),
                    Beat(16, show = setOf("point", "single", "cross"), light = setOf("point"), pulse = "header" to "point", card = BoardCard("Point read", "One item by its id and partition key: the cheapest call.")),
                ),
            ),
            // 17: "Got it. And is there only one way to buy the budget?"
            Scene(
                17, "Three ways to buy", throughput(),
                beats = listOf(
                    Beat(18, show = setOf("manual", "serverless"), light = setOf("manual", "serverless"), card = BoardCard("Manual and serverless", "Manual: fixed, from 400. Serverless: pay per RU, for tests and small apps.")),
                    Beat(20, show = setOf("autoscale", "load"), light = setOf("autoscale"), pulse = "load" to "autoscale", card = BoardCard("Autoscale", "Set a maximum: it moves between 10% of it and the max, billed per hour.")),
                ),
            ),
            // 21: "Nice. So that's buying. How do I make each call cheaper?"
            Scene(
                21, "The indexing policy", indexing(),
                beats = listOf(
                    Beat(22, show = setOf("item", "index", "policy"), light = setOf("index", "policy"), card = BoardCard("Index", "Like a book's index: find items without reading every one.")),
                    Beat(24, light = setOf("item", "index"), pulse = "item" to "index", card = BoardCard("Every field by default", "Fast filters, but every save updates the index for every field.")),
                    Beat(26, show = setOf("excl"), light = setOf("excl"), pulse = "policy" to "excl", card = BoardCard("Exclude /text", "Saving a note: about 14 RUs down to 9, measured.")),
                    Beat(28, show = setOf("comp"), light = setOf("comp"), pulse = "policy" to "comp", card = sortCode),
                    Beat(30, light = setOf("comp"), card = BoardCard("Composite index", "One index over several fields, in the query's order and direction.")),
                ),
            ),
            // 31: "You said the second setting was about how strict a read is."
            Scene(
                31, "Consistency", consistency(),
                beats = listOf(
                    Beat(32, light = setOf("cost"), card = BoardCard("Consistency", "What a reader may see while a save reaches every copy.")),
                    Beat(34, light = setOf("strong", "eventual"), card = BoardCard("Strictest to loosest", "Strong costs about twice per read; eventual is cheapest.")),
                    Beat(36, light = setOf("session"), card = BoardCard("Session (default)", "You see your own saves. A client may only weaken it.")),
                    Beat(38, light = setOf("strong"), card = BoardCard("The usual mistakes", "Guessing costs, indexing long text, strong by habit, panicking at 429.")),
                ),
            ),
            // 39: "So what do I remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("peak", "sort", "own"), card = BoardCard("Listen for the phrase", "Peak: autoscale. Two-field sort: composite. Own saves: session.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-cosmos-perf--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Monday Morning Meltdown..."
            Scene(
                0, "The Monday Morning Meltdown",
                DiagramSpec(
                    "Revise Owl freezes every Monday at nine: its calls to Cosmos DB come back as 429.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.18f, w = 0.38f, h = 0.2f, sub = "Revise Owl"),
                        DNode("api", "Python backend", 0.22f, 0.72f, w = 0.38f, h = 0.2f, sub = "on Azure"),
                        DNode("err", "429", 0.76f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.PILL, tone = 2, sub = "Mondays at nine"),
                        DNode("db", "Cosmos DB", 0.76f, 0.72f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "cards container"),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "calls"),
                        DEdge("api", "db", "requests"),
                        DEdge("db", "err", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("phone"), pulse = "phone" to "api", card = BoardCard("The case", "Every Monday at nine the app freezes; by ten it's fine again.")),
                    Beat(2, show = setOf("db", "err"), light = setOf("err"), pulse = "db" to "err", card = BoardCard("429", "Too many requests: the budget for that second is spent.")),
                    Beat(4, light = setOf("db"), pulse = "api" to "db", card = BoardCard("Request unit (RU)", "The price tag on one call: about 1 RU for a small read by id.")),
                ),
            ),
            // 5: "And Revise Owl pays how?"
            Scene(
                5, "The budget", budget("Revise Owl API", "cards container", "1,000 RU/s"),
                beats = listOf(
                    Beat(6, show = setOf("app", "cont"), light = setOf("cont"), pulse = "app" to "cont", card = BoardCard("Throughput", "A budget of RUs per second: a fixed 1,000 here.")),
                    Beat(7, show = setOf("busy"), light = setOf("busy"), pulse = "cont" to "busy", card = BoardCard("Suspect one: an outage", "Cosmos DB is down?")),
                    Beat(8, show = setOf("ok"), light = setOf("ok", "busy"), card = BoardCard("Cleared", "The SDK retries 429s. Lots at one hour: the budget is too small then.")),
                ),
            ),
            // 9: "Then buy a bigger budget. Set it to 4,000, all week."
            Scene(
                9, "The fix", throughput(),
                beats = listOf(
                    Beat(9, show = setOf("manual"), light = setOf("manual"), card = BoardCard("Suspect: 4,000 all week", "Peak prices for every quiet hour.")),
                    Beat(10, show = setOf("autoscale", "load"), light = setOf("autoscale"), pulse = "load" to "autoscale", card = BoardCard("Autoscale", "Max 4,000: up at nine, back to 400 by ten. Billed per hour's peak.")),
                    Beat(12, show = setOf("serverless"), light = setOf("serverless"), card = BoardCard("Serverless", "Pay per RU used: tests or a tiny app, not heavy daily load.")),
                ),
            ),
            // 13: "Case closed, then. Autoscale. Lunch?"
            Scene(
                13, "The second witness", indexing(),
                beats = listOf(
                    Beat(14, show = setOf("item"), light = setOf("item"), card = BoardCard("14 RUs a save", "Finance expected half that.")),
                    Beat(16, show = setOf("index"), light = setOf("index"), pulse = "item" to "index", card = chargeCode),
                    Beat(18, show = setOf("policy"), light = setOf("index", "policy"), card = BoardCard("Every field by default", "Each save updates the index for every field, long text included.")),
                    Beat(19, card = BoardCard("Suspect: indexing off", "Then every search reads every card. Far worse.")),
                    Beat(20, show = setOf("excl"), light = setOf("excl"), pulse = "policy" to "excl", card = BoardCard("Exclude /text", "Saving dropped from about 14 RUs to 9.")),
                ),
            ),
            // 21: "Any more clues in the file?"
            Scene(
                21, "The sorted deck", indexing(),
                beats = listOf(
                    Beat(22, light = setOf("item"), card = BoardCard("Query", "SELECT * FROM c ORDER BY c.deck ASC, c.dueAt ASC", code = true)),
                    Beat(24, show = setOf("comp"), light = setOf("comp"), pulse = "policy" to "comp", card = BoardCard("Composite index", "Deck ascending, due date ascending: the query's own order.")),
                ),
            ),
            // 25: "And the last suspect. A teammate set the account... to strong..."
            Scene(
                25, "The last suspect", consistency(),
                beats = listOf(
                    Beat(25, light = setOf("strong"), card = BoardCard("Suspect: strong", "Always the latest save, at about twice the read cost.")),
                    Beat(28, light = setOf("session"), card = BoardCard("Session (default)", "Your own saves, in order. A session token marks your last save.")),
                    Beat(30, light = setOf("session", "eventual"), card = BoardCard("Only weaker", "A client may ask for weaker than the account default, never stronger.")),
                ),
            ),
            // 31: "Any loose ends?"
            Scene(
                31, "Loose ends", shelves(),
                beats = listOf(
                    Beat(32, light = setOf("hot"), pulse = "budget" to "hot", card = BoardCard("A hot shelf", "One partition gets the traffic: 429s whatever the maximum.")),
                ),
            ),
            // 33: "And which calls are cheapest, for the record?"
            Scene(
                33, "Cheapest calls", calls(),
                beats = listOf(
                    Beat(34, light = setOf("point"), pulse = "header" to "point", card = BoardCard("Other episodes", "Cosmos SDK: reads and queries. Vector search: vector indexes.")),
                ),
            ),
            // 35: "So, the case summary for the exam?"
            Scene(
                35, "Case closed", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("peak", "writes", "sort"), card = BoardCard("Listen for the phrase", "Peak: autoscale. Pricey saves: exclude. Two-field sort: composite.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-cosmos-perf--gossip",
        listOf(
            // 0: "Okay, did you hear about the Coach team's database bill this week?..."
            Scene(
                0, "The Coach's bill", budget("Coach API", "notes container", "1,000 RU/s"),
                beats = listOf(
                    Beat(0, show = setOf("app", "cont"), card = BoardCard("Azure Cosmos DB", "Microsoft's cloud database, where every call has a price.")),
                    Beat(2, light = setOf("app", "cont"), pulse = "app" to "cont", card = BoardCard("Request unit (RU)", "The price tag on one call: about 1 RU for a small read by id.")),
                    Beat(4, show = setOf("ok", "busy"), light = setOf("busy"), pulse = "cont" to "busy", card = BoardCard("Throughput and 429", "A budget of RUs per second. Go over it, and calls get 429.")),
                ),
            ),
            // 5: "Okay. So who went first?"
            Scene(
                5, "Monday: Tom", calls(),
                beats = listOf(
                    Beat(6, show = setOf("header"), light = setOf("header"), card = BoardCard("Tom guessed 2 RUs", "It was 12.")),
                    Beat(8, card = chargeCode),
                    Beat(10, show = setOf("point", "single", "cross"), light = setOf("point"), pulse = "header" to "point", card = BoardCard("Cheapest first", "Point read, then one shelf, then every shelf.")),
                ),
            ),
            // 11: "Very tidy. And Tuesday?"
            Scene(
                11, "Tuesday: Dana", indexing(),
                beats = listOf(
                    Beat(12, show = setOf("item", "index"), light = setOf("item", "index"), pulse = "item" to "index", card = BoardCard("14 RUs a save", "Every field indexed by default, the long text too.")),
                    Beat(14, show = setOf("policy", "excl"), light = setOf("excl"), pulse = "policy" to "excl", card = BoardCard("Exclude /text", "Saves dropped to 9 RUs. Dana got a cake.")),
                ),
            ),
            // 15: "Good for Dana. Wednesday?"
            Scene(
                15, "Wednesday: Marcus", consistency(),
                beats = listOf(
                    Beat(16, light = setOf("cost"), card = BoardCard("Consistency", "How fresh a read must be while a save reaches every copy.")),
                    Beat(18, light = setOf("strong", "session"), card = BoardCard("Strong or session", "Strong: always the latest, double the reads. Session: your own saves.")),
                    Beat(20, light = setOf("bounded", "prefix", "eventual"), card = BoardCard("The other three", "Bounded staleness, consistent prefix, eventual: looser and cheaper.")),
                ),
            ),
            // 21: "Thursday. I can feel it."
            Scene(
                21, "Thursday: the intern", throughput(),
                beats = listOf(
                    Beat(22, show = setOf("load"), light = setOf("load"), card = BoardCard("An outage ticket", "429s at nine every morning. The SDK had already retried.")),
                    Beat(24, show = setOf("autoscale"), light = setOf("autoscale"), pulse = "load" to "autoscale", card = BoardCard("Autoscale", "Set a maximum of 4,000: it moves between 10% of it and the max.")),
                    Beat(26, show = setOf("manual", "serverless"), light = setOf("manual", "serverless"), card = BoardCard("Manual and serverless", "Fixed, from 400; or pay per RU, for tests and tiny apps.")),
                ),
            ),
            // 27: "And Friday? There's always a Friday."
            Scene(
                27, "Friday: Priya", consistency(),
                beats = listOf(
                    Beat(28, light = setOf("strong", "session"), card = BoardCard("Strong from the client?", "The account default was session.")),
                    Beat(30, light = setOf("eventual"), card = BoardCard("Only weaker", "A client may weaken the default, never strengthen it. Report: eventual.")),
                ),
            ),
            // 31: "Did anything else come up?"
            Scene(
                31, "Where autoscale stops", shelves(),
                beats = listOf(
                    Beat(32, light = setOf("hot"), pulse = "budget" to "hot", card = BoardCard("A hot shelf", "One partition gets the traffic: 429s whatever the maximum.")),
                ),
            ),
            // 33: "And the sorted notes list? I heard someone shouting at it."
            Scene(
                33, "Tom again", indexing(),
                beats = listOf(
                    Beat(34, show = setOf("comp"), light = setOf("comp"), pulse = "policy" to "comp", card = sortCode),
                ),
            ),
            // 35: "So, the gossip I should remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("peak", "own", "stronger"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Measure first, like Tom does now.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-cosmos-perf--late-night",
        listOf(
            // 0: "Hey, you. It's late, the kettle's on..."
            Scene(
                0, "What a call costs", budget("Coach API", "notes container", "1,000 RU/s"),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "What a database charges you, and how to make it charge less.")),
                    Beat(2, show = setOf("app", "cont"), light = setOf("cont"), card = BoardCard("Azure Cosmos DB", "Microsoft's cloud database, where every call has a price.")),
                    Beat(4, light = setOf("app", "cont"), pulse = "app" to "cont", card = BoardCard("Request unit (RU)", "The price tag on one call: about 1 RU for a small read by id.")),
                    Beat(6, card = BoardCard("Under the price tag", "Processor time, memory and disk reads, rolled into one number.")),
                    Beat(8, show = setOf("busy"), light = setOf("busy"), pulse = "cont" to "busy", card = BoardCard("Throughput and 429", "A budget of RUs per second. Ask for more, and calls get 429.")),
                    Beat(10, show = setOf("ok"), light = setOf("ok", "busy"), card = BoardCard("Turned away, politely", "Wait the milliseconds it asks; the SDK retries a few times.")),
                ),
            ),
            // 11: "How do I know what each call costs? Can I just ask?"
            Scene(
                11, "What it cost", calls(),
                beats = listOf(
                    Beat(12, show = setOf("header"), light = setOf("header"), card = chargeCode),
                    Beat(14, show = setOf("point", "single", "cross"), light = setOf("point"), pulse = "header" to "point", card = BoardCard("Point read", "One item by its id and partition key: the cheapest call.")),
                ),
            ),
            // 15: "And the budget. Is it always a fixed number?"
            Scene(
                15, "Buying the budget", throughput(),
                beats = listOf(
                    Beat(16, show = setOf("manual", "autoscale", "serverless"), light = setOf("autoscale"), card = BoardCard("Manual, serverless, autoscale", "Autoscale: between 10% of a maximum and the max, billed per hour.")),
                    Beat(18, show = setOf("load"), light = setOf("autoscale", "load"), pulse = "load" to "autoscale", card = BoardCard("The nine o'clock rush", "Autoscale rises to meet it. A hot shelf still needs a better key.")),
                ),
            ),
            // 19: "Tell me how to make each call cheaper. Slowly."
            Scene(
                19, "The indexing policy", indexing(),
                beats = listOf(
                    Beat(20, show = setOf("item", "index", "policy"), light = setOf("index", "policy"), card = BoardCard("Index", "Like a book's index: find items without reading every one.")),
                    Beat(22, show = setOf("excl"), light = setOf("excl"), pulse = "policy" to "excl", card = BoardCard("Exclude /text", "Saving a note went from about 14 RUs to 9.")),
                    Beat(24, show = setOf("comp"), light = setOf("comp"), pulse = "policy" to "comp", card = BoardCard("Composite index", "One index over both fields, for an ORDER BY on two fields.")),
                ),
            ),
            // 25: "And the last setting? You promised me consistency."
            Scene(
                25, "Consistency", consistency(),
                beats = listOf(
                    Beat(26, light = setOf("strong", "eventual"), card = BoardCard("Five levels", "Strong costs about twice per read; eventual is cheapest.")),
                    Beat(28, light = setOf("session"), card = BoardCard("Session (default)", "Your own saves, in order. A client may only weaken it.")),
                    Beat(30, light = setOf("eventual"), card = BoardCard("The nightly report: eventual", "It can lag a little, and costs less.")),
                ),
            ),
            // 31: "Mm. Before I drift off. What do I remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = setOf("peak", "sort", "own"), card = BoardCard("Listen for the phrase", "Peak: autoscale. Two-field sort: composite. Own saves: session.")),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-cosmos-perf--couples",
        listOf(
            // 0: "Tonight is date night, darling. And tonight, Rafe, we're talking about budgets."
            Scene(
                0, "Date night budgets", budget("Coach API", "notes container", "RU/s budget"),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Budgets: what every call to Cosmos DB costs.")),
                    Beat(2, show = setOf("app", "cont"), light = setOf("cont"), card = BoardCard("Azure Cosmos DB", "Microsoft's cloud database, where every call has a price.")),
                    Beat(4, light = setOf("app", "cont"), pulse = "app" to "cont", card = BoardCard("Request unit (RU)", "The price tag on one call: about 1 RU for a small read by id.")),
                    Beat(6, show = setOf("busy"), light = setOf("busy"), pulse = "cont" to "busy", card = BoardCard("Throughput and 429", "A budget of RUs per second. Spend more, and calls get 429.")),
                    Beat(8, show = setOf("ok"), light = setOf("ok", "busy"), card = BoardCard("The SDK retries", "It waits the milliseconds the reply asks for, and tries again.")),
                ),
            ),
            // 9: "So, a quiz, darling. The Coach is quiet all day..."
            Scene(
                9, "Quiz: the nine o'clock rush", throughput(),
                beats = listOf(
                    Beat(9, show = setOf("load"), light = setOf("load"), card = BoardCard("Quiz", "Quiet all day, 429s at nine. Which way to buy?")),
                    Beat(10, show = setOf("autoscale"), light = setOf("autoscale"), pulse = "load" to "autoscale", card = BoardCard("Right answer: autoscale", "Between 10% of a maximum and the max, billed per hour's peak.")),
                    Beat(12, show = setOf("manual", "serverless"), light = setOf("manual", "serverless"), card = BoardCard("Manual and serverless", "Fixed, from 400; or pay per RU, for tests and tiny apps.")),
                ),
            ),
            // 13: "And how do I know what a call really cost me?..."
            Scene(
                13, "What it really cost", calls(),
                beats = listOf(
                    Beat(14, show = setOf("header"), light = setOf("header"), card = chargeCode),
                    Beat(16, show = setOf("point", "single", "cross"), light = setOf("point"), pulse = "header" to "point", card = BoardCard("Point read", "One item by its id and partition key: the cheapest call.")),
                ),
            ),
            // 17: "Not me. Now, the index. Tell me something I don't know."
            Scene(
                17, "The index", indexing(),
                beats = listOf(
                    Beat(18, show = setOf("item", "index"), light = setOf("index"), pulse = "item" to "index", card = BoardCard("Index", "Like a book's index. By default every field, on every save.")),
                    Beat(19, show = setOf("policy"), light = setOf("item"), card = BoardCard("Quiz: 14 RUs a save", "The long text field is never filtered on. What do we do?")),
                    Beat(20, show = setOf("excl"), light = setOf("excl"), pulse = "policy" to "excl", card = BoardCard("Right answer: exclude /text", "Saves dropped to about 9 RUs.")),
                    Beat(22, light = setOf("item"), card = BoardCard("Quiz: a two-field sort", "ORDER BY topic, then newest first, fails. Why?")),
                    Beat(24, show = setOf("comp"), light = setOf("comp"), pulse = "policy" to "comp", card = BoardCard("No kiss: composite index", "Indexing off would make every search read every note.")),
                ),
            ),
            // 25: "Fine. Now, consistency. Something I'm famous for."
            Scene(
                25, "Consistency", consistency(),
                beats = listOf(
                    Beat(26, light = setOf("strong", "eventual"), card = BoardCard("Five levels", "Strong costs about twice per read; eventual is cheapest.")),
                    Beat(27, light = setOf("cost"), card = BoardCard("Quiz: see your own saves", "Nothing stricter is needed. Which level?")),
                    Beat(28, light = setOf("session"), card = BoardCard("Right answer: session", "The default. A session token marks your last save.")),
                    Beat(30, light = setOf("strong", "session"), card = BoardCard("Quiz: strong from the client?", "The account defaults to session.")),
                    Beat(31, light = setOf("eventual"), card = BoardCard("Right answer: only weaker", "Never stronger. The nightly report uses eventual.")),
                ),
            ),
            // 33: "And where does autoscale stop working?..."
            Scene(
                33, "Every romance has limits", shelves(),
                beats = listOf(
                    Beat(34, light = setOf("hot"), pulse = "budget" to "hot", card = BoardCard("A hot shelf", "One partition gets the traffic: 429s whatever the maximum.")),
                ),
            ),
            // 35: "Last question, for the exam. What do we remember?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("peak", "writes", "own"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Five for five.")),
                    Beat(39, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
