package com.shahaabapps.core.content

/** Cosmos DB performance: RUs, indexing policies and consistency: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesCosmosPerf {
    private val classic = Podcast(
        "ai-cosmos-perf",
        listOf(
            maya("Hi, and welcome back. Zaiqa is our own version of Zomato, and by now a lot of it exists. The Android app, the Zaiqa API, the service the phone talks to, running in a container. Orders and payments sit in Azure SQL, and the order event feed sits in Cosmos DB."),
            leo("I remember. Last time we put the event feed in Cosmos DB and left the money in Azure SQL. So what breaks today?"),
            maya("Friday, eight in the evening. Dinner rush. The partner dashboard stops showing new orders, and the log fills with 429, the code for too many requests."),
            leo("Too many requests. Is that a crash?"),
            maya("No. It is the database saying you have spent this second's budget, so come back in a moment. Nothing is lost, and the library we call it with quietly tries again a few times."),
            leo("Budget? I pay a monthly bill, surely."),
            maya("Not with Cosmos DB. You buy throughput, meaning how much work the container may do each second. Picture a coin jar refilled every second: four thousand coins a second, and that is all you get."),
            leo("And a coin is?"),
            maya("A request unit, an RU. It is the price tag on one call. Reading one small item by its id, say a kilobyte, costs about one RU. Writes cost more, and queries cost more again."),
            leo("How do I know what my own calls cost? I would just be guessing."),
            maya("You never guess. Every response carries its price in a header called x-ms-request-charge. In our .NET code it is the RequestCharge property on the response. Print it, and the argument stops."),
            leo("For example?"),
            maya("For example, reading one order event by its id costs about one RU. The same events for one restaurant, as a query, cost eight. The same query across every restaurant costs sixty. Same answer, sixty times the price."),
            leo("So the shape of the call matters more than the size of the data."),
            maya("Often, yes. A point read, where you already know the item's id, is the cheapest. A query over one restaurant's events is next. A query across the whole container is the dearest thing you can do."),
            leo("Fine. Friday broke. Do we just buy more coins?"),
            maya("That is the first thing the team did, and it is worth knowing the three ways to buy. Manual, autoscale and serverless."),
            leo("Manual first."),
            maya("Manual is a fixed number you set yourself, from four hundred RU a second upwards. You pay for it all day, whether or not anyone orders. Good when the load is flat, which Zaiqa's is not."),
            leo("And autoscale?"),
            maya("You set only a maximum. Autoscale then floats between a tenth of that maximum and the maximum, following the traffic, and each hour you are billed for the highest it reached. Say you're building anything with a dinner rush: this is the one."),
            leo("And serverless?"),
            maya("No number at all: you pay for each RU you use. Lovely for a test account or a small, rare app. Not for Zaiqa on a Friday, because it is not meant for heavy load that never stops."),
            leo("So the team moved to autoscale."),
            maya("They did. Maximum twenty thousand RU a second. The bill went up, and at eight o'clock the next Friday the 429s came back anyway."),
            leo("[sigh] Twenty thousand, and still told to slow down? How?"),
            maya("Because of partitions. A partition is a drawer. Cosmos DB spreads a container's items across many drawers, and the partition key is the field that picks which drawer an item goes in."),
            leo("And ours is?"),
            maya("The restaurant id. Every event for one restaurant lands in that restaurant's drawer. Which sounded sensible, until Spice Route."),
            leo("Let me guess. Spice Route is popular."),
            maya("On a Friday, Spice Route takes about a third of every order in the city. One drawer, a third of the writes, while hundreds of other drawers sit almost empty."),
            leo("But we bought twenty thousand. Surely the busy drawer can borrow from the quiet ones?"),
            maya("It cannot, and this is the line to remember. The budget is divided evenly between the physical partitions, and everything with one key value lives on exactly one of them. One of those serves at most ten thousand RU a second, whatever you buy."),
            leo("So money cannot fix it."),
            maya("Money cannot fix a bad partition key. Double the maximum and you double the bill, and Spice Route's drawer is throttled exactly as before."),
            leo("How would I even see that? My chart just says 429."),
            maya("In the portal, look at normalised RU consumption, split by partition. A healthy container shows a row of middling bars. Ours showed one bar pinned at a hundred per cent and the rest near zero."),
            leo("So what is the right key?"),
            maya("One with many values, spread evenly, that your queries still filter on. For the event feed we use the order id. There are millions of orders, no single one is hot, and the dashboard reads one order's events at a time."),
            leo("Can I just change the key on the container?"),
            maya("No. A container's partition key is fixed when you create it. You make a new container with the new key and copy the data across. Which is why this is worth getting right on a Tuesday, not a Friday."),
            leo("Right. Anything else making Friday expensive?"),
            maya("The index. An index is like the index at the back of a book: look up a word, see the page, go straight there instead of reading every page."),
            leo("And Cosmos DB builds one for me?"),
            maya("For every field, by default, on every write. Picture a book indexed on every word in it, including the. Our order event carries a free-text note from the customer, extra spicy, no onions, and nobody ever searches it."),
            leo("So stop indexing it."),
            maya("That is the indexing policy: you exclude everything, then include the fields you actually filter on. Excluding that one note took a write from about fourteen RU to nine. On Friday's volume, that is real money."),
            leo("Is there anything the index has to be told to do?"),
            maya("One thing, and the exam loves it. Sorting on two fields at once. Our dashboard lists an order's events by status, then newest first, and that query simply fails with a missing index error."),
            leo("Fails? Not just crawls?"),
            maya("Fails. You add a composite index: one list built on both fields, in the query's own order. Status ascending, then created time descending. Then it runs."),
            leo("Last thing. Is there a setting for how fresh a read is?"),
            maya("Consistency, the rule for how up to date a read must be. There are five levels. Strong always shows the latest write and costs about twice as much per read. Session is the default."),
            leo("And session means?"),
            maya("You always see your own writes, in order. A rider's phone saves a ping and reads it straight back. Another reader might be a moment behind, which for a map of where a rider is, is fine."),
            leo("And the other three?"),
            maya("Bounded staleness lags by at most a set number of versions or seconds. Consistent prefix may lag but never shows things out of order. Eventual is the cheapest, and catches up. Our nightly heat map job reads with eventual."),
            leo("Could that job ask for strong instead?"),
            maya("No, and that is a favourite trick question. A client may ask for a weaker level than the account's default, never a stronger one."),
            leo("So what do I remember for the exam?"),
            maya("If it says peaks at dinner and quiet all night, autoscale. If a sort on two fields fails, a composite index. If writes cost too much and most fields are never filtered, exclude those paths."),
            leo("And the Spice Route problem?"),
            maya("If one partition is throttled while the rest sit idle, the answer is the partition key, not a bigger budget. And a client may only weaken consistency, never strengthen it."),
            leo("And anything to try today?"),
            maya("Print the request charge for a point read, a single-partition query and a cross-partition query. Exclude the long note field and compare the charge of a save before and after. Run a sort on two fields, watch it fail, then add the composite index. And check the account's default consistency in the portal."),
            leo("Measure first, then tune. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-cosmos-perf",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Restaurant That Ate Friday. You know Zaiqa by now, our own version of Zomato. The Android app, the Zaiqa API, the service the phone calls, in a container. Orders and payments in Azure SQL, the order event feed in Cosmos DB."),
            leo("Detective Leo, reporting. What went wrong, and when?"),
            maya("Friday, eight in the evening. The partner dashboard stops showing new orders for twenty-two minutes. No crash, no alert from the servers. Just one number repeating in the log: 429, the code for too many requests."),
            leo("First suspect: the database fell over."),
            maya("It did not. That code is the database saying you have spent this second's budget, come back in a moment. Our library retried a few times and then gave up, which is what the dashboard saw."),
            leo("Budget. So somebody forgot to pay a bill."),
            maya("Closer. With Cosmos DB you buy throughput, meaning how much work the container may do each second. Picture a coin jar refilled every second: spend the coins, and the next call waits."),
            leo("And a coin has a name, I assume."),
            maya("A request unit, an RU. It is the price tag on one call: reading one small item by its id, about a kilobyte, costs roughly one RU. Writes cost more, queries more again."),
            leo("So my second suspect is the code. Somebody wrote a greedy query."),
            maya("Good instinct, and testable. Every response carries its price in a header, x-ms-request-charge. In our .NET code that is the RequestCharge property. The team printed it for a week."),
            leo("And?"),
            maya("Reading one order event by its id: about one RU. The same events for one restaurant, as a query: eight. The same query across every restaurant: sixty. The dashboard was doing the cheap one."),
            leo("[sigh] So the code was innocent. Suspect three: we were simply too small."),
            maya("That is what the team believed. So let us look at how you buy. Manual, autoscale, or serverless."),
            leo("Manual being?"),
            maya("A fixed number you set, from four hundred RU a second, paid for all day whether anyone orders or not. Zaiqa was on manual, at four thousand."),
            leo("And the dinner rush eats four thousand."),
            maya("Easily. So they moved to autoscale: you set only a maximum, and it floats between a tenth of that and the maximum, billed each hour on the highest it reached. Maximum twenty thousand."),
            leo("Case closed, surely."),
            maya("Next Friday, eight o'clock, the same 429s. The bill was five times larger and the dashboard still went dark."),
            leo("[sigh] Then I want the serverless option. Pay per call."),
            maya("Wrong direction. Serverless means no number at all, paying for each RU you use. Lovely for a test account or a small, rare app, and not meant for heavy load that never stops."),
            leo("Then I am out of suspects."),
            maya("Here is the clue you walked past. Cosmos DB splits a container's items across drawers, and each drawer is called a partition. The portal has a chart of normalised RU consumption, split by partition."),
            leo("And the chart said?"),
            maya("One drawer pinned at a hundred per cent. Every other drawer near zero. The field that picks the drawer is the partition key, and ours is the restaurant id."),
            leo("So one restaurant is doing all the eating."),
            maya("Spice Route. On a Friday it takes about a third of every order in the city. One drawer, a third of the writes, hundreds of drawers almost idle."),
            leo("Then the busy drawer borrows from the quiet ones."),
            maya("It cannot, and that is the whole case. The budget is divided evenly between the physical partitions, and everything with one key value lives on exactly one of them. One of those serves at most ten thousand RU a second, whatever you buy."),
            leo("So raising the maximum was never going to work."),
            maya("Money cannot fix a bad partition key. That is the sentence the exam wants."),
            leo("Then we change the key. To what?"),
            maya("Something with many values, spread evenly, that queries still filter on. The order id. Millions of orders, none of them hot, and the dashboard reads one order's events at a time."),
            leo("Change it on Monday morning, then."),
            maya("You cannot change a container's key at all. You create a new container with the new key and copy the data across. Plan it, do not patch it."),
            leo("Case closed?"),
            maya("Two smaller charges first. Start with what Cosmos DB does on every single save."),
            leo("Which is?"),
            maya("An index is like the index at the back of a book: look up a word, see the page, go straight there. Cosmos DB builds one for every field, on every write, unless you say otherwise."),
            leo("Every field. Including the long ones."),
            maya("Our order event carries the customer's note. Extra spicy, no onions. Nobody ever searches it, and every save was paying to index it. The indexing policy excludes everything, then includes the fields you filter on."),
            leo("Worth how much?"),
            maya("A save went from about fourteen RU to nine. On Friday's volume, that is real money. And the second charge: the dashboard's sorted list simply failed."),
            leo("Failed how?"),
            maya("A missing index error. It sorts an order's events by status, then newest first, and a sort on two fields at once needs a composite index: one list built on both fields, in the query's own order."),
            leo("Added, and it runs."),
            maya("It runs. One last witness: a rider complained that their own ping did not show on their own map."),
            leo("That smells like freshness."),
            maya("Consistency, the rule for how up to date a read must be. Five levels. Session is the default and gives you your own writes, in order. That rider's job had been set to eventual, the cheapest, which catches up later."),
            leo("And the fix is strong, surely. Always the latest."),
            maya("Session, not strong. Strong costs about twice as much per read for a guarantee nobody needed. Eventual still suits the nightly heat map job."),
            leo("Could that nightly job ask for strong, just for one night?"),
            maya("No. A client may ask for a weaker level than the account's default, never a stronger one. Another favourite trick."),
            leo("Case closed. So what goes in my notes for the exam?"),
            maya("Peaks at dinner and quiet all night, autoscale. A sort on two fields that fails, a composite index. Writes too dear and most fields never filtered, exclude those paths."),
            leo("And Spice Route?"),
            maya("One partition throttled while the rest sit idle is the partition key, not a bigger budget. And a client may only weaken consistency."),
            leo("And anything to try today?"),
            maya("Print the request charge for a point read, a single-partition query and a cross-partition query. Exclude the long note field and compare the charge of a save before and after. Run a sort on two fields, watch it fail, then add the composite index. And check the account's default consistency in the portal."),
            leo("One restaurant, one drawer, one lesson. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-cosmos-perf",
        listOf(
            maya("Okay, did you hear what happened to the Zaiqa team last Friday? Sit down. You know Zaiqa, our own version of Zomato. The Android app, the Zaiqa API, the service the phone calls, in a container. Orders and payments in Azure SQL, the order event feed in Cosmos DB."),
            leo("I heard the partner dashboard went dark at dinner time. Twenty minutes of nothing."),
            maya("Twenty-two. And the log said one thing over and over: 429, the code for too many requests."),
            leo("Which means what, exactly?"),
            maya("The database telling you that you have spent this second's budget, come back in a moment. It is not a crash. Tom called it an outage in the group chat, and Priya has not let him forget it."),
            leo("[chuckle] Poor Tom. What budget, though?"),
            maya("With Cosmos DB you buy throughput, meaning how much work the container may do each second. Picture a coin jar refilled every second. Spend the coins and the next call waits."),
            leo("And one coin is?"),
            maya("A request unit, an RU. It is the price tag on one call. One small item read by its id, about a kilobyte, is roughly one RU. Writes cost more, queries more again."),
            leo("So who went first?"),
            maya("Tom, on Monday. Tom decided the code was greedy and started rewriting queries, by eye, with no numbers at all."),
            leo("There is a number, surely."),
            maya("There is. Every response carries its price in a header, x-ms-request-charge. In our .NET code it is the RequestCharge property. Priya printed it in an afternoon and the arguing stopped."),
            leo("What did it say?"),
            maya("One order event read by its id, about one RU. One restaurant's events as a query, eight. The same query across every restaurant, sixty. Tom's rewrites had saved almost nothing."),
            leo("Tuesday?"),
            maya("Dana. Dana said we were simply too small, so buy more. Zaiqa was on manual throughput, a fixed four thousand a second, paid for all day whether anyone orders or not."),
            leo("And she raised it."),
            maya("She moved to autoscale, which is actually the right idea: you set only a maximum, and it floats between a tenth of that and the maximum, billed each hour on the highest it reached. Maximum twenty thousand."),
            leo("I feel a but coming."),
            maya("Next Friday, eight o'clock, the same 429s. Five times the bill, same dark dashboard. Dana went very quiet."),
            leo("[sigh] So Marcus suggests serverless."),
            maya("Of course he did. Serverless means no number at all, paying for each RU you use. It is lovely for a test account or a small, rare app, and it is not meant for heavy load that never stops."),
            leo("Thursday, then. Who actually found it?"),
            maya("The intern. Cosmos DB splits a container's items across drawers, and each drawer is called a partition. The intern opened the chart of normalised RU consumption, split by partition, which nobody else had looked at."),
            leo("And?"),
            maya("One bar at a hundred per cent, hundreds of bars near zero. The field that picks the drawer is the partition key, and theirs is the restaurant id."),
            leo("So one restaurant was eating everything."),
            maya("Spice Route. About a third of every order in the city on a Friday night. One drawer taking a third of the writes."),
            leo("Then the hot drawer borrows from the quiet ones, and nobody notices."),
            maya("It cannot. The budget is divided evenly between the physical partitions, and everything with one key value lives on exactly one of them. One of those serves at most ten thousand RU a second, whatever you buy."),
            leo("So Dana's twenty thousand was never going to help."),
            maya("Money cannot fix a bad partition key. The intern put that sentence on the whiteboard and it is still there."),
            leo("[laugh] What key did they move to?"),
            maya("The order id. Millions of values, none of them hot, and the dashboard reads one order's events at a time anyway."),
            leo("A quick change on Friday afternoon, then."),
            maya("Absolutely not, and this is the bit Marcus got wrong. You cannot change a container's partition key. You make a new container with the new key and copy the data across."),
            leo("What else came out of the week?"),
            maya("Two cheaper wins. The first is what Cosmos DB does on every single save."),
            leo("Which is?"),
            maya("It indexes every field. An index is like the index at the back of a book: look up a word, see the page, go straight there. Useful, until you are indexing things nobody searches."),
            leo("Such as?"),
            maya("The customer's note on an order. Extra spicy, no onions. The indexing policy now excludes everything and includes only the fields they filter on, and a save went from about fourteen RU to nine."),
            leo("And the second win?"),
            maya("Priya's. The dashboard's sorted list had been failing for months with a missing index error. It sorts an order's events by status, then newest first."),
            leo("A sort can fail? Not just crawl?"),
            maya("It fails. A sort on two fields at once needs a composite index: one list built on both fields, in the query's own order. Priya added it and the list came back."),
            leo("Anything else, or may I go?"),
            maya("One more. A rider complained that their own ping did not appear on their own map."),
            leo("Ah. Freshness."),
            maya("Consistency, the rule for how up to date a read must be. Five levels. Session is the default and gives you your own writes, in order. Someone had set that job to eventual, the cheapest, which catches up later."),
            leo("And Marcus said use strong."),
            maya("He did, and no. Strong costs about twice as much per read for a guarantee nobody needed. Session was the answer. Eventual still suits the nightly heat map job."),
            leo("Can a job ask for strong anyway, just that once?"),
            maya("No. A client may ask for a weaker level than the account's default, never a stronger one. Marcus knows that now too."),
            leo("So what is the gossip I should remember for the exam?"),
            maya("Peaks at dinner and quiet all night, autoscale. A sort on two fields that fails, a composite index. Writes too dear and most fields never filtered, exclude those paths."),
            leo("And the Spice Route headline?"),
            maya("One partition throttled while the rest sit idle is the partition key, not a bigger budget. And a client may only weaken consistency."),
            leo("And anything to try today, so I am not next week's story?"),
            maya("Print the request charge for a point read, a single-partition query and a cross-partition query. Exclude the long note field and compare the charge of a save before and after. Run a sort on two fields, watch it fail, then add the composite index. And check the account's default consistency in the portal."),
            leo("Measure first, Tom. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-cosmos-perf",
        listOf(
            nova("Hey, you. It's late, the city has eaten, and the kitchens are closing. [pause] Tonight, let's talk about the Friday night that broke Zaiqa."),
            ivy("Zaiqa. Remind me where we left it."),
            nova("Our own version of Zomato. The Android app, the Zaiqa API, the service the phone calls, in a container. Orders and payments in Azure SQL, and the order event feed in Cosmos DB."),
            ivy("And Friday?"),
            nova("Eight in the evening. The partner dashboard stops showing new orders for twenty-two minutes. [pause] And the log says one thing, over and over: 429, the code for too many requests."),
            ivy("Too many requests. That sounds almost polite."),
            nova("It is polite. It's the database saying you've spent this second's budget, come back in a moment. Nothing crashed. Nothing was lost."),
            ivy("A budget by the second. Go on."),
            nova("With Cosmos DB you buy throughput, meaning how much work the container may do each second. [pause] Picture a coin jar, refilled every second. Spend the coins, and the next call waits its turn."),
            ivy("And a coin?"),
            nova("A request unit, an RU. It is the price tag on one call. One small item read by its id, about a kilobyte, is roughly one RU. Writes cost more. Queries more again."),
            ivy("And how do I know what mine cost, at this hour, with nobody to ask?"),
            nova("Every response tells you. There's a header called x-ms-request-charge, and in our .NET code it's the RequestCharge property. [pause] Print it, and you stop guessing."),
            ivy("Give me some numbers, slowly."),
            nova("One order event read by its id: about one RU. One restaurant's events as a query: eight. The same query across every restaurant: sixty. Same answer, sixty times the price."),
            ivy("So the shape of the question matters more than the size of the answer."),
            nova("Mm. That's the night's first lesson. Now, Zaiqa's team thought they had simply bought too little."),
            ivy("And had they?"),
            nova("There are three ways to buy. Manual is a fixed number you set, from four hundred a second, paid for all day. Autoscale, where you set only a maximum. And serverless."),
            ivy("Autoscale sounds like the dinner rush's friend."),
            nova("It is. It floats between a tenth of your maximum and the maximum, following the traffic, and each hour you're billed for the highest it reached. [pause] They set twenty thousand."),
            ivy("And serverless, for the record?"),
            nova("No number at all. You pay for each RU you use. Lovely for a test account or a small, rare app, and not meant for heavy load that never stops."),
            ivy("So, twenty thousand. Did Friday behave?"),
            nova("[long pause] Next Friday, eight o'clock, the same 429s. Five times the bill. The dashboard went dark on time, like a train."),
            ivy("[sigh] I would have wept. So what did they miss?"),
            nova("Cosmos DB splits a container's items across drawers, and each drawer is called a partition. There's a chart in the portal, normalised RU consumption, split by partition, and nobody had opened it."),
            ivy("And when they did?"),
            nova("One bar at a hundred per cent. Hundreds of bars near zero. [pause] The field that picks the drawer is the partition key, and theirs was the restaurant id."),
            ivy("Oh. One restaurant everybody loves."),
            nova("Spice Route. About a third of every order in the city on a Friday. A third of the writes, all into one drawer."),
            ivy("Can the busy drawer borrow from the sleepy ones? It's late, they're not using it."),
            nova("[chuckle] It can't. The budget is divided evenly between the physical partitions, and everything with one key value lives on exactly one of them. One of those serves at most ten thousand RU a second, whatever you buy."),
            ivy("So the twenty thousand was just money."),
            nova("Money cannot fix a bad partition key. [pause] Say that one to yourself on the way to sleep."),
            ivy("And the right key?"),
            nova("Many values, spread evenly, that your queries still filter on. The order id. Millions of them, none of them famous, and the dashboard reads one order's events at a time."),
            ivy("Change it in the morning, then."),
            nova("You can't change a container's key at all. You make a new container with the new key and copy the data across. [pause] Which is why it's worth thinking about tonight, not on a Friday."),
            ivy("Mm. What else was costing them?"),
            nova("What Cosmos DB does on every save. It indexes every field, and an index is like the index at the back of a book: look up a word, see the page, go straight there."),
            ivy("Even the fields nobody looks up."),
            nova("Even the customer's note. Extra spicy, no onions. The indexing policy excludes everything and includes only what you filter on, and a save went from about fourteen RU to nine."),
            ivy("And is there something the index must be told to do?"),
            nova("One thing, and the exam adores it. [pause] The dashboard sorts an order's events by status, then newest first, and that query fails with a missing index error."),
            ivy("Fails. Not sulks."),
            nova("Fails. A sort on two fields at once needs a composite index: one list built on both fields, in the query's own order. Then it runs."),
            ivy("Last one, and then I'm going to bed. Is there a dial for how fresh a read is?"),
            nova("Consistency, the rule for how up to date a read has to be. Five levels. Session is the default: you always see your own writes, in order. [pause] A rider saves a ping and reads it straight back."),
            ivy("And the others, briefly?"),
            nova("Strong always shows the latest write and costs about twice as much per read. Bounded staleness lags by a set limit. Consistent prefix may lag but never shows things out of order. Eventual is the cheapest, and catches up."),
            ivy("Could a cheap nightly job ask for strong, just once?"),
            nova("No. A client may ask for a weaker level than the account's default, never a stronger one. [pause] The account decides how strict the house is."),
            ivy("Before I fall asleep on you. What do I keep for the exam?"),
            nova("Peaks at dinner and quiet all night, autoscale. A sort on two fields that fails, a composite index. Writes too dear and most fields never filtered, exclude those paths."),
            ivy("And Spice Route?"),
            nova("One partition throttled while the rest sit idle is the partition key, not a bigger budget. And a client may only weaken consistency, never strengthen it."),
            ivy("And something to try today, before I sleep?"),
            nova("Print the request charge for a point read, a single-partition query and a cross-partition query. Exclude the long note field and compare the charge of a save before and after. Run a sort on two fields, watch it fail, then add the composite index. And check the account's default consistency in the portal."),
            ivy("One famous restaurant, one tired drawer. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-cosmos-perf",
        listOf(
            nova("Tonight is date night, darling. Candles, something spicy, and the Friday that broke Zaiqa."),
            rafe("Our own version of Zomato. The Android app, the Zaiqa API, the service the phone calls, in a container. Orders and payments in Azure SQL, and the order event feed in Cosmos DB."),
            nova("Mm. And at eight in the evening, the partner dashboard stopped showing new orders for twenty-two minutes. The log said one thing: 429, the code for too many requests."),
            rafe("Turned away at the door all night. I know the feeling."),
            nova("[laugh] It's gentler than that. It's the database saying you've spent this second's budget, come back in a moment."),
            rafe("A budget by the second. Explain it to me slowly, love."),
            nova("With Cosmos DB you buy throughput, meaning how much work the container may do each second. Picture a coin jar refilled every second: spend the coins and the next call waits."),
            rafe("And a coin?"),
            nova("A request unit, an RU. It is the price tag on one call. One small item read by its id, about a kilobyte, is roughly one RU. Writes cost more, queries more again."),
            rafe("Quiz for you, then. I want to know what one call really costs me. Where do I look?"),
            nova("At the header on every response, x-ms-request-charge. In our .NET code it's the RequestCharge property. Never guess the price of anything."),
            rafe("Right answer. Never guess the price of anything. [kiss]"),
            nova("Numbers, since you asked. One order event by its id: about one RU. One restaurant's events as a query: eight. The same query across every restaurant: sixty."),
            rafe("So the shape of the asking matters more than the size of the answer. My turn to be quizzed?"),
            nova("Go on, then. Zaiqa had a fixed four thousand a second, paid for all day. Dinner is a peak and the small hours are nothing. What do we buy instead?"),
            rafe("Autoscale. You set only a maximum, it floats between a tenth of that and the maximum, and each hour you're billed for the highest it reached."),
            nova("Right answer, and smooth with it. [kiss]"),
            rafe("And serverless, for completeness. No number at all, paying for each RU you use. Lovely for a test account or a small, rare app, and not for heavy load that never stops."),
            nova("Exactly. So they set a maximum of twenty thousand. And the next Friday, at eight o'clock, the same 429s and five times the bill."),
            rafe("[sigh] Then something is wrong with the plumbing, not the money."),
            nova("It is. Cosmos DB splits a container's items across drawers, and each drawer is called a partition. The field that picks the drawer is the partition key."),
            rafe("And theirs?"),
            nova("The restaurant id. Which was fine, until Spice Route. About a third of every order in the city on a Friday night, all landing in one drawer."),
            rafe("Quiz, darling. The maximum is twenty thousand and only one drawer is hot. Why doesn't more money fix it?"),
            nova("Because the budget is divided evenly between the physical partitions, and everything with one key value lives on exactly one of them. One of those serves at most ten thousand RU a second, whatever you buy."),
            rafe("Right answer. Money cannot fix a bad partition key. [kiss]"),
            nova("And how would we have seen it coming, without waiting for a dark dashboard?"),
            rafe("The portal's chart of normalised RU consumption, split by partition. One bar at a hundred per cent, the rest near zero."),
            nova("Right answer, and you read the documentation. [kiss]"),
            rafe("So what is the better key?"),
            nova("Many values, spread evenly, that queries still filter on. The order id: millions of them, none of them famous, and the dashboard reads one order's events at a time."),
            rafe("And we change it on the container tonight."),
            nova("We cannot change it at all. You create a new container with the new key and copy the data across. [pause] Some things you choose once, and live with."),
            rafe("Like a surname. What else was costing them?"),
            nova("What Cosmos DB does on every save. It indexes every field, and an index is like the index at the back of a book: look up a word, see the page, go straight there."),
            rafe("Even the customer's note. Extra spicy, no onions."),
            nova("Especially that. The indexing policy excludes everything and includes only the fields you filter on, and one save went from about fourteen RU to nine."),
            rafe("Quiz for you. The dashboard sorts an order's events by status, then newest first, and the query fails with a missing index error. What does it need?"),
            nova("More RU a second. Throw coins at it."),
            rafe("[sigh] No kiss for that one. A sort on two fields at once needs a composite index: one list built on both fields, in the query's own order."),
            nova("Noted. The index first, the coins later."),
            rafe("Last one, and it's yours. A rider saves their own ping and must see it on their own map straight away, as cheaply as possible. Which level of freshness?"),
            nova("Session, the default. Consistency is the rule for how up to date a read has to be, and session gives you your own writes, in order."),
            rafe("Right answer, and the cheap one. [kiss]"),
            nova("Strong would have worked too, and cost about twice as much per read for a promise nobody needed."),
            rafe("And the rest of the five?"),
            nova("Bounded staleness lags by a set limit. Consistent prefix may lag but never shows things out of order. Eventual is the cheapest and catches up, which suits the nightly heat map job."),
            rafe("And could that nightly job ask for strong, just for us?"),
            nova("No. A client may ask for a weaker level than the account's default, never a stronger one. The house decides how strict the house is."),
            rafe("Right answer. Nearly four for four. [kiss]"),
            nova("Last question, for the exam. What do we take away?"),
            rafe("Peaks at dinner and quiet all night, autoscale. A sort on two fields that fails, a composite index. Writes too dear and most fields never filtered, exclude those paths."),
            nova("That's three. And Spice Route?"),
            rafe("One partition throttled while the rest sit idle is the partition key, not a bigger budget. And a client may only weaken consistency, never strengthen it."),
            nova("And something to try today, darling?"),
            rafe("Print the request charge for a point read, a single-partition query and a cross-partition query. Exclude the long note field and compare the charge of a save before and after. Run a sort on two fields, watch it fail, then add the composite index. And check the account's default consistency in the portal."),
            nova("Measured, trimmed and spread evenly. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun budget(app: String, cont: String, rus: String) = DiagramSpec(
        "Each call spends RUs from the container's per-second budget: within it the call succeeds, over it the call gets 429.",
        nodes = listOf(
            DNode("app", app, 0.2f, 0.5f, w = 0.34f, h = 0.22f, tone = 1, sub = ".NET"),
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
            DNode("point", "Point read", 0.17f, 0.66f, w = 0.3f, h = 0.3f, sub = "about 1 RU"),
            DNode("single", "One restaurant", 0.5f, 0.66f, w = 0.3f, h = 0.3f, sub = "one drawer: 8 RU"),
            DNode("cross", "Every restaurant", 0.83f, 0.66f, w = 0.3f, h = 0.3f, tone = 2, sub = "all drawers: 60 RU"),
        ),
        edges = listOf(
            DEdge("header", "point"),
            DEdge("header", "single"),
            DEdge("header", "cross"),
        ),
        aspect = 0.8f,
    )

    private fun drawers() = DiagramSpec(
        "Partitions are drawers: the budget is split between them, so one hot drawer gets 429s however high the maximum goes.",
        nodes = listOf(
            DNode("budget", "Container budget", 0.5f, 0.16f, w = 0.56f, h = 0.2f, tone = 1, sub = "split between drawers"),
            DNode("hot", "Hot drawer", 0.17f, 0.68f, w = 0.3f, h = 0.32f, kind = NodeKind.CYLINDER, tone = 2, sub = "Spice Route"),
            DNode("p2", "Drawer", 0.5f, 0.68f, w = 0.3f, h = 0.32f, kind = NodeKind.CYLINDER, sub = "idle"),
            DNode("p3", "Drawer", 0.83f, 0.68f, w = 0.3f, h = 0.32f, kind = NodeKind.CYLINDER, sub = "idle"),
        ),
        edges = listOf(
            DEdge("budget", "hot", "a third of it"),
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
            DEdge("load", "autoscale", "dinner rush"),
            DEdge("load", "serverless", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun indexing() = DiagramSpec(
        "The indexing policy decides what each save indexes: exclude fields nobody filters on, add a composite index for two-field sorts.",
        nodes = listOf(
            DNode("item", "Order event", 0.25f, 0.16f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "status, time, note"),
            DNode("index", "The index", 0.75f, 0.16f, w = 0.44f, h = 0.2f, tone = 1, sub = "like a book's index"),
            DNode("excl", "Exclude the note", 0.25f, 0.5f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "long, never filtered"),
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
            DNode("strong", "Strong", 0.31f, 0.1f, w = 0.58f, h = 0.16f, tone = 2, sub = "always the latest write"),
            DNode("bounded", "Bounded staleness", 0.31f, 0.3f, w = 0.58f, h = 0.16f, sub = "lags by a set limit"),
            DNode("session", "Session", 0.31f, 0.5f, w = 0.58f, h = 0.16f, tone = 1, sub = "your own writes (default)"),
            DNode("prefix", "Consistent prefix", 0.31f, 0.7f, w = 0.58f, h = 0.16f, sub = "never out of order"),
            DNode("eventual", "Eventual", 0.31f, 0.9f, w = 0.58f, h = 0.16f, sub = "cheapest, catches up"),
            DNode("cost", "Stricter costs more", 0.81f, 0.5f, w = 0.34f, h = 0.4f, kind = NodeKind.NOTE, sub = "strong: about 2x reads"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's performance questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("peak", "Dinner peaks", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "autoscale"),
            DNode("sort", "Sort on two fields", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "composite index"),
            DNode("writes", "Saves cost too much", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "exclude unused paths"),
            DNode("own", "See your own writes", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "session"),
            DNode("many", "One drawer throttled", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "the key, not the budget"),
            DNode("stronger", "Stronger than default?", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "no: only weaker"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("peak", "sort", "writes", "own", "many", "stronger")
    private val chargeCode = BoardCard("Read the charge", "x-ms-request-charge, or RequestCharge on the response in .NET", code = true)
    private val sortCode = BoardCard("The query that fails", "ORDER BY c.status ASC, c.createdAt DESC needs a composite index", code = true)
    private val today = BoardCard("Today", "Print charges, exclude the note, add a composite index, check the default.")
    private val recapCard = BoardCard("Where Zaiqa is now", "App, API in a container, orders in Azure SQL, events in Cosmos DB.")
    private val ruCard = BoardCard("Request unit", "The price tag on one call: about 1 RU for a small item read by id.")
    private val shapesCard = BoardCard("Three shapes of call", "Point read about 1 RU, one restaurant 8, across every restaurant 60.")
    private val buyCard = BoardCard("Three ways to buy", "Manual: a fixed number. Autoscale: a maximum. Serverless: per RU used.")
    private val manualCard = BoardCard("Manual", "A fixed number you set, from 400 RU a second, paid for all day.")
    private val autoCard = BoardCard("Autoscale", "Set only a maximum: it floats between a tenth of it and the maximum.")
    private val serverlessCard = BoardCard("Serverless", "Pay per RU used. For tests and small, rare apps, not steady load.")
    private val stillCard = BoardCard("Still 429 at eight", "Five times the bill, and the dashboard still went dark.")
    private val chartCard = BoardCard("One bar at a hundred", "Normalised RU consumption by partition: the rest near zero.")
    private val betterKeyCard = BoardCard("A better key", "The order id: millions of values, and none of them famous.")
    private val newContainerCard = BoardCard("You cannot change the key", "Make a new container with the new key and copy the data across.")
    private val policyCard = BoardCard("Indexing policy", "Exclude everything, then include the fields you actually filter on.")
    private val examCard = BoardCard("Listen for the phrase", "Autoscale, a composite index, excluded paths, the partition key.")
    private val spiceLessonCard = BoardCard("Spice Route's lesson", "One partition throttled while the rest idle: the key, not the budget.")

    // The shared pictures, one card each.
    private val jarCard = BoardCard("Throughput: a coin jar", "Refilled every second: 4,000 RU a second is 4,000 coins to spend.")
    private val cafeCard = BoardCard("429: come back in a moment", "Too many requests: this second's jar is empty. Wait, then try again.")
    private val drawerCard = BoardCard("Partition: a drawer", "The partition key picks the drawer. Ours was the restaurant id.")
    private val hotCard = BoardCard("A third of the city", "Spice Route takes a third of Friday's orders, all into one drawer.")
    private val splitCard = BoardCard("Money cannot fix the key", "The budget splits evenly, and one key value sits on one partition.")
    private val capCard = BoardCard("Ten thousand RU a second", "That is all one physical partition serves, whatever you buy.")
    private val bookCard = BoardCard("Index: a book's index", "Look up a word, see the page, go straight there: no reading it all.")
    private val everyFieldCard = BoardCard("Every field by default", "Even the customer's note. Each save pays: 14 RU down to 9.")
    private val phoneBookCard = BoardCard("Composite: a phone book", "One list on two fields: surname, then first name. Status, then newest.")
    private val branchesCard = BoardCard("Consistency: shop branches", "One price list in many branches: what may a reader see while it travels?")
    private val strictCard = BoardCard("Strong and bounded", "Strong: the latest write, about 2x reads. Bounded: behind by a set limit.")
    private val sessionCard = BoardCard("Session: your own notebook", "A rider sees their own ping at once; others may be a moment behind.")
    private val looseCard = BoardCard("Prefix and eventual", "Prefix: never out of order. Eventual: cheapest, and catches up later.")
    private val weakerCard = BoardCard("Only weaker", "A client may weaken the account default, never strengthen it.")

    private fun classicBoard() = PodcastBoard(
        "ai-cosmos-perf",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is our own version of Zomato..."
            Scene(
                0, "Friday at eight", budget("Zaiqa API", "order events", "4,000 RU/s"),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = recapCard),
                    Beat(2, show = setOf("cont", "busy"), light = setOf("busy"), pulse = "cont" to "busy", card = BoardCard("Friday, eight o'clock", "The partner dashboard goes dark, and the log fills with 429.")),
                    Beat(4, light = setOf("busy"), card = cafeCard),
                    Beat(6, light = setOf("cont"), card = jarCard),
                    Beat(8, light = setOf("cont"), card = ruCard),
                    Beat(10, show = setOf("ok"), light = setOf("ok"), pulse = "cont" to "ok", card = chargeCode),
                ),
            ),
            // 11: "For example?"
            Scene(
                11, "What a call costs", calls(),
                beats = listOf(
                    Beat(12, show = setOf("header", "point", "single", "cross"), light = setOf("point"), card = shapesCard),
                    Beat(14, light = setOf("cross"), card = BoardCard("Cheapest to dearest", "A point read, then one drawer, then a query across them all.")),
                ),
            ),
            // 15: "Fine. Friday broke. Do we just buy more coins?"
            Scene(
                15, "Three ways to buy", throughput(),
                beats = listOf(
                    Beat(16, show = setOf("manual", "autoscale", "serverless", "load"), light = setOf("manual"), card = buyCard),
                    Beat(18, light = setOf("manual"), pulse = "load" to "manual", card = manualCard),
                    Beat(20, light = setOf("autoscale"), pulse = "load" to "autoscale", card = autoCard),
                    Beat(22, light = setOf("serverless"), pulse = "load" to "serverless", card = serverlessCard),
                    Beat(24, light = setOf("autoscale"), card = stillCard),
                ),
            ),
            // 25: "Twenty thousand, and still told to slow down? How?"
            Scene(
                25, "One hot drawer", drawers(),
                beats = listOf(
                    Beat(26, show = setOf("budget", "hot", "p2", "p3"), light = setOf("budget"), card = drawerCard),
                    Beat(28, light = setOf("hot"), pulse = "budget" to "hot", card = BoardCard("Key: the restaurant id", "Every event for one restaurant lands in that restaurant's drawer.")),
                    Beat(30, light = setOf("hot"), card = hotCard),
                    Beat(32, light = setOf("hot", "p2", "p3"), card = splitCard),
                    Beat(34, light = setOf("hot"), card = capCard),
                    Beat(36, light = setOf("p2", "p3"), card = chartCard),
                    Beat(38, light = setOf("budget"), card = betterKeyCard),
                    Beat(40, light = setOf("budget"), card = newContainerCard),
                ),
            ),
            // 41: "Right. Anything else making Friday expensive?"
            Scene(
                41, "What every save pays for", indexing(),
                beats = listOf(
                    Beat(42, show = setOf("item", "index"), light = setOf("index"), pulse = "item" to "index", card = bookCard),
                    Beat(44, light = setOf("item"), card = everyFieldCard),
                    Beat(46, show = setOf("policy", "excl"), light = setOf("excl"), pulse = "policy" to "excl", card = policyCard),
                    Beat(48, show = setOf("comp"), light = setOf("comp"), pulse = "policy" to "comp", card = sortCode),
                    Beat(50, light = setOf("comp"), card = phoneBookCard),
                ),
            ),
            // 51: "Last thing. Is there a setting for how fresh a read is?"
            Scene(
                51, "How fresh is a read", consistency(),
                beats = listOf(
                    Beat(52, show = setOf("strong", "bounded", "session", "prefix", "eventual", "cost"), light = setOf("strong", "session"), card = branchesCard),
                    Beat(54, light = setOf("session"), card = sessionCard),
                    Beat(56, light = setOf("bounded", "prefix", "eventual"), card = looseCard),
                    Beat(58, light = setOf("strong"), card = weakerCard),
                ),
            ),
            // 59: "So what do I remember for the exam?"
            Scene(
                59, "For the exam", exam(),
                beats = listOf(
                    Beat(60, show = all6, light = setOf("peak", "sort", "writes"), card = examCard),
                    Beat(62, light = setOf("many", "stronger"), card = spiceLessonCard),
                    Beat(64, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-cosmos-perf--case-files",
        listOf(
            // 0: "Welcome to Case Files. Tonight's case: The Restaurant That Ate Friday."
            Scene(
                0, "The case", budget("Zaiqa API", "order events", "4,000 RU/s"),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("The case", "Friday at eight: the dashboard goes dark for twenty-two minutes.")),
                    Beat(2, show = setOf("cont", "busy"), light = setOf("busy"), pulse = "cont" to "busy", card = cafeCard),
                    Beat(6, light = setOf("cont"), card = jarCard),
                    Beat(8, light = setOf("cont"), card = ruCard),
                    Beat(10, show = setOf("ok"), light = setOf("ok"), pulse = "cont" to "ok", card = chargeCode),
                ),
            ),
            // 11: "And?"
            Scene(
                11, "Suspect: a greedy query", calls(),
                beats = listOf(
                    Beat(12, show = setOf("header", "point", "single", "cross"), light = setOf("point", "cross"), card = shapesCard),
                ),
            ),
            // 13: "So the code was innocent. Suspect three: we were simply too small."
            Scene(
                13, "Suspect: too small", throughput(),
                beats = listOf(
                    Beat(14, show = setOf("manual", "autoscale", "serverless", "load"), light = setOf("manual"), card = buyCard),
                    Beat(16, light = setOf("manual"), pulse = "load" to "manual", card = manualCard),
                    Beat(18, light = setOf("autoscale"), pulse = "load" to "autoscale", card = autoCard),
                    Beat(20, light = setOf("autoscale"), card = stillCard),
                    Beat(22, light = setOf("serverless"), pulse = "load" to "serverless", card = serverlessCard),
                ),
            ),
            // 23: "Then I am out of suspects."
            Scene(
                23, "The clue nobody opened", drawers(),
                beats = listOf(
                    Beat(24, show = setOf("budget", "hot", "p2", "p3"), light = setOf("budget"), card = drawerCard),
                    Beat(26, light = setOf("hot"), pulse = "budget" to "hot", card = chartCard),
                    Beat(28, light = setOf("hot"), card = hotCard),
                    Beat(30, light = setOf("hot", "p2", "p3"), card = splitCard),
                    Beat(32, light = setOf("budget"), card = capCard),
                    Beat(34, light = setOf("budget"), card = betterKeyCard),
                    Beat(36, light = setOf("budget"), card = newContainerCard),
                ),
            ),
            // 37: "Case closed?"
            Scene(
                37, "Two smaller charges", indexing(),
                beats = listOf(
                    Beat(40, show = setOf("item", "index"), light = setOf("index"), pulse = "item" to "index", card = bookCard),
                    Beat(42, show = setOf("policy", "excl"), light = setOf("excl"), pulse = "policy" to "excl", card = everyFieldCard),
                    Beat(44, light = setOf("excl"), card = policyCard),
                    Beat(46, show = setOf("comp"), light = setOf("comp"), pulse = "policy" to "comp", card = sortCode),
                ),
            ),
            // 47: "Added, and it runs."
            Scene(
                47, "The rider's own ping", consistency(),
                beats = listOf(
                    Beat(50, show = setOf("strong", "bounded", "session", "prefix", "eventual", "cost"), light = setOf("session"), card = sessionCard),
                    Beat(52, light = setOf("strong"), card = strictCard),
                    Beat(54, light = setOf("eventual"), card = weakerCard),
                ),
            ),
            // 55: "Case closed. So what goes in my notes for the exam?"
            Scene(
                55, "Case closed", exam(),
                beats = listOf(
                    Beat(56, show = all6, light = setOf("peak", "sort", "writes"), card = examCard),
                    Beat(58, light = setOf("many", "stronger"), card = spiceLessonCard),
                    Beat(60, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-cosmos-perf--gossip",
        listOf(
            // 0: "Okay, did you hear what happened to the Zaiqa team last Friday?"
            Scene(
                0, "What the Zaiqa team did", budget("Zaiqa API", "order events", "4,000 RU/s"),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = recapCard),
                    Beat(2, show = setOf("cont", "busy"), light = setOf("busy"), pulse = "cont" to "busy", card = BoardCard("Twenty-two dark minutes", "Friday at eight, and the log said 429 over and over.")),
                    Beat(4, light = setOf("busy"), card = cafeCard),
                    Beat(6, light = setOf("cont"), card = jarCard),
                    Beat(8, light = setOf("cont"), card = ruCard),
                ),
            ),
            // 9: "So who went first?"
            Scene(
                9, "Monday: Tom", calls(),
                beats = listOf(
                    Beat(12, show = setOf("header", "point", "single", "cross"), light = setOf("point"), card = chargeCode),
                    Beat(14, light = setOf("cross"), card = shapesCard),
                ),
            ),
            // 15: "Tuesday?"
            Scene(
                15, "Tuesday: Dana", throughput(),
                beats = listOf(
                    Beat(16, show = setOf("manual", "autoscale", "serverless", "load"), light = setOf("manual"), pulse = "load" to "manual", card = manualCard),
                    Beat(18, light = setOf("autoscale"), pulse = "load" to "autoscale", card = autoCard),
                    Beat(20, light = setOf("autoscale"), card = stillCard),
                    Beat(22, light = setOf("serverless"), pulse = "load" to "serverless", card = serverlessCard),
                ),
            ),
            // 23: "Thursday, then. Who actually found it?"
            Scene(
                23, "Thursday: the intern", drawers(),
                beats = listOf(
                    Beat(24, show = setOf("budget", "hot", "p2", "p3"), light = setOf("budget"), card = drawerCard),
                    Beat(26, light = setOf("hot"), pulse = "budget" to "hot", card = chartCard),
                    Beat(28, light = setOf("hot"), card = hotCard),
                    Beat(30, light = setOf("hot", "p2", "p3"), card = splitCard),
                    Beat(32, light = setOf("budget"), card = capCard),
                    Beat(34, light = setOf("budget"), card = betterKeyCard),
                    Beat(36, light = setOf("budget"), card = newContainerCard),
                ),
            ),
            // 37: "What else came out of the week?"
            Scene(
                37, "Two cheaper wins", indexing(),
                beats = listOf(
                    Beat(40, show = setOf("item", "index"), light = setOf("index"), pulse = "item" to "index", card = bookCard),
                    Beat(42, show = setOf("policy", "excl"), light = setOf("excl"), pulse = "policy" to "excl", card = everyFieldCard),
                    Beat(44, show = setOf("comp"), light = setOf("comp"), card = BoardCard("Priya's missing index", "The sorted list had been failing for months.")),
                    Beat(46, light = setOf("comp"), pulse = "policy" to "comp", card = sortCode),
                ),
            ),
            // 47: "Anything else, or may I go?"
            Scene(
                47, "The rider's own ping", consistency(),
                beats = listOf(
                    Beat(50, show = setOf("strong", "bounded", "session", "prefix", "eventual", "cost"), light = setOf("session"), card = sessionCard),
                    Beat(52, light = setOf("strong"), card = strictCard),
                    Beat(54, light = setOf("eventual"), card = weakerCard),
                ),
            ),
            // 55: "So what is the gossip I should remember for the exam?"
            Scene(
                55, "For the exam", exam(),
                beats = listOf(
                    Beat(56, show = all6, light = setOf("peak", "sort", "writes"), card = examCard),
                    Beat(58, light = setOf("many", "stronger"), card = spiceLessonCard),
                    Beat(60, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-cosmos-perf--late-night",
        listOf(
            // 0: "Hey, you. It's late, the city has eaten, and the kitchens are closing."
            Scene(
                0, "Tonight: a Friday that broke", budget("Zaiqa API", "order events", "4,000 RU/s"),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("Tonight", "The Friday night that broke Zaiqa, and what it really cost.")),
                    Beat(2, show = setOf("cont"), light = setOf("cont"), pulse = "app" to "cont", card = recapCard),
                    Beat(4, show = setOf("busy"), light = setOf("busy"), pulse = "cont" to "busy", card = cafeCard),
                    Beat(8, light = setOf("cont"), card = jarCard),
                    Beat(10, light = setOf("cont"), card = ruCard),
                    Beat(12, show = setOf("ok"), light = setOf("ok"), pulse = "cont" to "ok", card = chargeCode),
                ),
            ),
            // 13: "Give me some numbers, slowly."
            Scene(
                13, "What a call costs", calls(),
                beats = listOf(
                    Beat(14, show = setOf("header", "point", "single", "cross"), light = setOf("point", "cross"), card = shapesCard),
                ),
            ),
            // 17: "Mm. That's the night's first lesson."
            Scene(
                17, "Three ways to buy", throughput(),
                beats = listOf(
                    Beat(18, show = setOf("manual", "autoscale", "serverless", "load"), light = setOf("manual"), card = buyCard),
                    Beat(20, light = setOf("autoscale"), pulse = "load" to "autoscale", card = autoCard),
                    Beat(22, light = setOf("serverless"), pulse = "load" to "serverless", card = serverlessCard),
                    Beat(24, light = setOf("autoscale"), card = stillCard),
                ),
            ),
            // 25: "Then I'd have cried. What did they miss?"
            Scene(
                25, "One drawer, wide awake", drawers(),
                beats = listOf(
                    Beat(26, show = setOf("budget", "hot", "p2", "p3"), light = setOf("budget"), card = drawerCard),
                    Beat(28, light = setOf("hot"), pulse = "budget" to "hot", card = chartCard),
                    Beat(30, light = setOf("hot"), card = hotCard),
                    Beat(32, light = setOf("hot", "p2", "p3"), card = splitCard),
                    Beat(34, light = setOf("budget"), card = capCard),
                    Beat(36, light = setOf("budget"), card = betterKeyCard),
                    Beat(38, light = setOf("budget"), card = newContainerCard),
                ),
            ),
            // 39: "Mm. What else was costing them?"
            Scene(
                39, "Every save pays", indexing(),
                beats = listOf(
                    Beat(40, show = setOf("item", "index"), light = setOf("index"), pulse = "item" to "index", card = bookCard),
                    Beat(42, show = setOf("policy", "excl"), light = setOf("excl"), pulse = "policy" to "excl", card = everyFieldCard),
                    Beat(44, show = setOf("comp"), light = setOf("comp"), card = sortCode),
                    Beat(46, light = setOf("comp"), pulse = "policy" to "comp", card = phoneBookCard),
                ),
            ),
            // 47: "Last one, and then I'm going to bed."
            Scene(
                47, "How fresh is a read", consistency(),
                beats = listOf(
                    Beat(48, show = setOf("strong", "bounded", "session", "prefix", "eventual", "cost"), light = setOf("session"), card = sessionCard),
                    Beat(50, light = setOf("strong", "bounded"), card = strictCard),
                    Beat(52, light = setOf("prefix", "eventual"), card = weakerCard),
                ),
            ),
            // 53: "Before I fall asleep on you. What do I keep for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("peak", "sort", "writes"), card = examCard),
                    Beat(56, light = setOf("many", "stronger"), card = spiceLessonCard),
                    Beat(58, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-cosmos-perf--couples",
        listOf(
            // 0: "Tonight is date night, darling."
            Scene(
                0, "Date night", budget("Zaiqa API", "order events", "RU/s budget"),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("Date night", "Candles, something spicy, and the Friday that broke Zaiqa.")),
                    Beat(1, show = setOf("cont"), light = setOf("cont"), pulse = "app" to "cont", card = recapCard),
                    Beat(2, show = setOf("busy"), light = setOf("busy"), pulse = "cont" to "busy", card = cafeCard),
                    Beat(6, light = setOf("cont"), card = jarCard),
                    Beat(8, light = setOf("cont"), card = ruCard),
                    Beat(10, show = setOf("ok"), light = setOf("ok"), pulse = "cont" to "ok", card = chargeCode),
                ),
            ),
            // 11: "Right answer. Never guess the price of anything."
            Scene(
                11, "Never guess the price", calls(),
                beats = listOf(
                    Beat(12, show = setOf("header", "point", "single", "cross"), light = setOf("point", "cross"), card = shapesCard),
                ),
            ),
            // 13: "So the shape of the asking matters more than the size of the answer."
            Scene(
                13, "Quiz: what do we buy?", throughput(),
                beats = listOf(
                    Beat(14, show = setOf("manual", "autoscale", "serverless", "load"), light = setOf("manual"), card = BoardCard("Quiz: a fixed 4,000 all day", "Dinner is a peak and the small hours nothing. What do we buy?")),
                    Beat(15, light = setOf("autoscale"), pulse = "load" to "autoscale", card = BoardCard("Right answer: autoscale", "A tenth of the maximum up to the maximum, billed on the hour's peak.")),
                    Beat(17, light = setOf("serverless"), pulse = "load" to "serverless", card = serverlessCard),
                    Beat(18, light = setOf("autoscale"), card = stillCard),
                ),
            ),
            // 19: "Then something is wrong with the plumbing, not the money."
            Scene(
                19, "One famous restaurant", drawers(),
                beats = listOf(
                    Beat(20, show = setOf("budget", "hot", "p2", "p3"), light = setOf("budget"), card = drawerCard),
                    Beat(22, light = setOf("hot"), pulse = "budget" to "hot", card = hotCard),
                    Beat(23, light = setOf("hot", "p2", "p3"), card = BoardCard("Quiz: why not more money?", "The maximum is 20,000 and only one drawer is hot.")),
                    Beat(24, light = setOf("hot"), card = splitCard),
                    Beat(25, light = setOf("hot"), card = capCard),
                    Beat(27, light = setOf("p2", "p3"), card = chartCard),
                    Beat(30, light = setOf("budget"), card = betterKeyCard),
                    Beat(32, light = setOf("budget"), card = newContainerCard),
                ),
            ),
            // 33: "Like a surname. What else was costing them?"
            Scene(
                33, "Every save pays", indexing(),
                beats = listOf(
                    Beat(34, show = setOf("item", "index"), light = setOf("index"), pulse = "item" to "index", card = bookCard),
                    Beat(36, show = setOf("policy", "excl"), light = setOf("excl"), pulse = "policy" to "excl", card = everyFieldCard),
                    Beat(37, show = setOf("comp"), light = setOf("comp"), card = BoardCard("Quiz: the sort that fails", "Status, then newest first, and a missing index error.")),
                    Beat(39, light = setOf("comp"), pulse = "policy" to "comp", card = sortCode),
                ),
            ),
            // 41: "Last one, and it's yours. A rider saves their own ping..."
            Scene(
                41, "Quiz: the rider's ping", consistency(),
                beats = listOf(
                    Beat(42, show = setOf("strong", "bounded", "session", "prefix", "eventual", "cost"), light = setOf("session"), card = sessionCard),
                    Beat(44, light = setOf("strong"), card = strictCard),
                    Beat(46, light = setOf("bounded", "prefix", "eventual"), card = looseCard),
                    Beat(48, light = setOf("session"), card = weakerCard),
                ),
            ),
            // 50: "Last question, for the exam. What do we take away?"
            Scene(
                50, "For the exam", exam(),
                beats = listOf(
                    Beat(51, show = all6, light = setOf("peak", "sort", "writes"), card = examCard),
                    Beat(53, light = setOf("many", "stronger"), card = spiceLessonCard),
                    Beat(55, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
