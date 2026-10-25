package com.shahaabapps.core.content

/** The Cosmos DB change feed: reacting to new and updated items: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesCosmosChangeFeed {
    private val classic = Podcast(
        "ai-cosmos-changefeed",
        listOf(
            maya("Hi, and welcome back. By now Zaiqa is almost whole. The app and the dashboard call the Zaiqa API, which is the one service allowed to write to Azure SQL, where orders and payments live. Cosmos DB takes the order events and the rider pings, and last time it learned to search dishes by meaning."),
            leo("So ordering works and searching works. What is left?"),
            maya("The moment after. The rider hands over the bag and the order is marked delivered. Three things should happen next: the diner gets a ratings prompt, the restaurant gets its payout, and the dish search index, which is the list our search reads, learns this dish sells."),
            leo("Easy. The Zaiqa API does all three before it answers."),
            maya("Then the diner waits while we call a payments service, and if one of the three fails the whole delivery fails with it. [pause] Say you're building a small shop: you would not cancel an order because the thank-you email bounced."),
            leo("Fair. So the API writes the delivery, and something else notices afterwards. How does the something else notice?"),
            maya("That is the question. The old answer is polling: every minute, ask the database what changed. For example, select every order delivered in the last minute. It costs money all night, finds nothing most of the time, and is always a minute late."),
            leo("A minute late on a ratings prompt is fine, honestly."),
            maya("On a payout it is not, and on Friday that is forty thousand queries finding nothing. Cosmos already has the answer written down. Every container has a change feed: an ordered list of the items created or changed, in the order it happened."),
            leo("So instead of asking what is new, I read the list."),
            maya("And you read it from where you stopped. Zaiqa's order worker writes one small item into the order events container when an order is delivered. That write shows up in the change feed a second or two later, and the three jobs read it there."),
            leo("Hold on. The delivery is a row in Azure SQL. Why is there a copy in Cosmos at all? Why not just put a trigger on the SQL table?"),
            maya("Good question. SQL is where money must be correct, not where three other jobs hang off it. A trigger would run inside the transaction that pays the restaurant, holding it open while they work. The order event is a cheap write-once copy anyone may follow."),
            leo("All right. Who reads the feed, then? Do I write a loop?"),
            maya("You could, and then you must remember where you got to and split the work when one machine is not enough. The change feed processor does both. It is a library in the dot NET and Java SDKs, the packs of code Microsoft gives you, and it runs underneath the Azure Functions Cosmos DB trigger."),
            leo("What does it need from me?"),
            maya("Four things. The monitored container, which here is order events. A lease container, usually called leases. A unique instance name for every running copy. And your own code, the delegate, handed a batch of changed items."),
            leo("A lease container. In plain words, please?"),
            maya("A second, tiny container that holds bookmarks, one for each slice of the feed, saying how far that reader got. Picture a shared book with sticky notes in it: handled up to change five hundred and twelve, so after a restart, carry on at five hundred and thirteen."),
            leo("And if I run two copies of the same job?"),
            maya("They point at the same leases with the same processor name, and take their own bookmarks. One sticky note belongs to one copy at a time, so the work splits. The only thing that must differ is the instance name, which is how they tell each other apart."),
            leo("And if the second copy gets its own lease container by mistake?"),
            maya("Then it is a separate reader with its own bookmarks, and every change is handled twice. The opposite mistake is worse: two different jobs sharing one lease container and prefix fight over the same bookmarks, and only one of them fires."),
            leo("So the payout job and the ratings job each get their own leases."),
            maya("Their own lease container, or at least their own prefix. Three jobs, three readers, three sets of bookmarks, all following the same order events."),
            leo("The photo function is Python. Does the processor work there?"),
            maya("No, and that is examined. There is no change feed processor library in Python. A Python app uses the Functions Cosmos DB trigger, which keeps the leases for you, or the pull model, where it reads the feed itself and saves a continuation token, which is a bookmark your own code keeps."),
            leo("And with the Functions trigger, what does my code actually see?"),
            maya("A list of changed items and nothing to arrange. You name the database, the container, the connection and the lease container, and the function signs in with a managed identity, which is a badge Azure gives the app. No key in any setting."),
            leo("Nice. Now tell me what goes wrong, because something always does."),
            maya("Three things. First, delivery is at least once. The bookmark moves only after your batch finishes without an error, so a handler that crashes halfway sees the same change again."),
            leo("So the restaurant gets paid twice."),
            maya("Only if the handler was written carelessly. Add one hundred and eighty rupees to the payout is not safe to run twice. Set this order's payout to one hundred and eighty rupees, keyed by the order id, is safe however often it runs. Write every handler that way."),
            leo("Set, not add. That I can remember. Second thing?"),
            maya("Deletes. Latest version mode is the default, and it shows creates and updates only. Sharma's removes paneer tikka from the menu, the item disappears, and nothing at all appears in the feed. The search index goes on offering a dish nobody can cook."),
            leo("[sigh] That is a horrible bug. How would you even find it?"),
            maya("A diner orders the ghost dish and the kitchen says we stopped making that. The fix is a soft delete: mark the dish deleted with a flag, which is an update, so the feed does show it, then give the item a TTL, a time to live, and Cosmos removes it a week later."),
            leo("And if I really need every delete, as a delete?"),
            maya("Then you read in all versions and deletes mode, which shows every create, update and delete in order, and even says when a TTL did the deleting. It needs continuous backup on the account, and keeps changes only for that window. The heavier choice."),
            leo("Third thing?"),
            maya("The loop. When the search job finishes, it writes its result back onto the dish item. That write is an update, so the feed wakes the same job again, forever. Keep a hash of the dish text, which is a fingerprint: same text, same code. Same fingerprint, skip the work."),
            leo("And anything about starting up?"),
            maya("Yes. The start time counts the first time only. A brand-new reader can start at now, at a date, or at the very beginning, to walk the history once. Once leases exist, the processor carries on from them and ignores that setting."),
            leo("Right. So what do I remember for the exam?"),
            maya("If the reader must see deletes: a soft delete with a TTL, or all versions and deletes mode with continuous backup. If two copies must split the work: same processor name and lease container, different instance name. If an item was edited three times before the reader ran: one entry, the newest."),
            leo("And the Python question?"),
            maya("In Python, the Functions trigger or the pull model with a saved continuation token, because there is no processor library. And if a question worries that a handler might run twice, the answer is a handler safe to repeat, never a stricter feed."),
            leo("And what do I do today?"),
            maya("Today, create a small order events container and a leases container, with the id as the partition key, the drawer Cosmos files each item in. Then write a Python function with a Cosmos DB trigger that logs each changed id, run it locally, and edit an item in Data Explorer."),
            leo("And the horrible one?"),
            maya("Delete an item and watch nothing at all reach the log. Then set a deleted flag instead, and watch it arrive. [chuckle] See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-cosmos-changefeed",
        listOf(
            maya("Welcome to Case Files. Today's case: The Ghost Dish. Zaiqa is our food delivery app. The Kotlin app and the Angular dashboard call the Zaiqa API, which is the one service that writes to Azure SQL, where orders and payments live. Cosmos DB takes the order events and the rider pings."),
            leo("Detective Leo, reporting. What is the complaint?"),
            maya("Three complaints in one week. Diners keep ordering paneer tikka from Sharma's, and the kitchen keeps telling the rider they stopped making it a month ago. A restaurant was paid twice for one order. And a third job ran all night and never fired once."),
            leo("Three crimes, one culprit. I love it. Where do I start?"),
            maya("With what happens when an order is delivered. The rider hands over the bag, the API marks the order delivered in Azure SQL, and three jobs should wake up: the ratings prompt, the restaurant payout, and the dish search index, which is the list our search reads."),
            leo("Then suspect one is obvious. The API forgot to call them."),
            maya("The API never calls them. It writes one small item into the order events container in Cosmos DB and goes back to answering the app. A shop that cancelled your order because a thank-you email bounced would be a bad shop."),
            leo("So how do three jobs find out, if nobody calls them?"),
            maya("Every Cosmos container keeps a change feed: an ordered list of the items created or changed, in the order it happened. Each job reads that list from where it last stopped. No polling, no minute-late query that mostly finds nothing."),
            leo("And where is where it last stopped written down?"),
            maya("In a lease container, which is a tiny second container full of bookmarks. One bookmark per slice of the feed: handled up to change five hundred and twelve, carry on at five hundred and thirteen. That, a monitored container, an instance name and your code are the change feed processor."),
            leo("Noted. Case one, the ghost dish. My suspicion is the search job was switched off."),
            maya("It has run every day. Here is the first clue: it reads in latest version mode, which is the default, and that mode shows creates and updates only."),
            leo("Only creates and updates. So what happens when Sharma's deletes paneer tikka?"),
            maya("Nothing happens. The item vanishes from the container and the feed never mentions it. The search index kept a dish that no longer exists, and went on offering it, for a month."),
            leo("[sigh] A deletion that leaves no evidence. So what is the fix, delete harder?"),
            maya("A soft delete. The dashboard marks the dish deleted with a flag instead of removing it, and a flag is an update, so the feed does show it. Then the item gets a TTL, a time to live, and Cosmos clears it away for real a week later."),
            leo("And if the case demanded real deletes, in the feed, as deletes?"),
            maya("Then you read in all versions and deletes mode, which shows every create, update and delete in order, and even says when a TTL did the deleting. It needs continuous backup on the account and keeps changes only for that window. Heavier, and newer."),
            leo("Case one closed. [chuckle] Case two, a restaurant paid twice. That is fraud, surely."),
            maya("It is arithmetic. The payout handler read a batch, added one hundred and eighty rupees to Sharma's ledger, and then crashed before the batch finished."),
            leo("And when it came back?"),
            maya("The bookmark had not moved, because it moves only when a batch finishes without an error. So the same change arrived again and the handler added one hundred and eighty rupees a second time. Delivery is at least once, always."),
            leo("So the feed is broken."),
            maya("The handler is. Picture a note on a fridge. Add one hundred and eighty is wrong if you read it twice. Set this order's payout to one hundred and eighty, keyed by the order id, is right however often you read it. Every handler must be safe to run twice."),
            leo("Set, not add. Case two closed. Case three, the job that never fired."),
            maya("The ratings job and the payout job were both added last month. Second clue: both were pointed at the same lease container, with the same prefix and the same processor name."),
            leo("Which means they were fighting over the same sticky notes."),
            maya("Exactly. Two copies of one job share the leases and split the work, which is what you want when one copy falls behind. But two different jobs sharing one lease container are mistaken for copies of each other, so only one of them gets the changes."),
            leo("So the ratings job quietly ate the payout job's work. What is the rule?"),
            maya("Same job, several copies: same processor name, same lease container, a different instance name each. Different jobs: a lease container each, or at least a different prefix."),
            leo("Case closed, then. Unless there is a fourth body."),
            maya("There is a near miss. The search job writes its result back onto the dish item, which is an update, so the feed wakes it again, forever. It is saved by a hash of the dish text, a fingerprint where the same text always gives the same code."),
            leo("Same fingerprint, do nothing. Who reads the feed, by the way? Did somebody write a loop?"),
            maya("No. The payout job is dot NET and uses the change feed processor library from the SDK, the pack of code Microsoft gives you. The photo function is Python, and Python has no processor library at all."),
            leo("So Python is locked out?"),
            maya("Python has two doors. The Azure Functions Cosmos DB trigger, which runs the processor for you and hands your code a list of changed items. Or the pull model, where your code reads the feed itself and saves a continuation token, which is a bookmark your code keeps rather than one Cosmos keeps."),
            leo("And when would a detective choose the pull model?"),
            maya("For one-off work. Zaiqa's backfill walked every dish from the beginning once, on a Tuesday afternoon, with a saved token so it could stop and carry on. A start time like that counts the first time only: once leases exist, the processor resumes from them and ignores it."),
            leo("So what does this case teach me for the exam?"),
            maya("Must see deletes: soft delete with a TTL, or all versions and deletes mode with continuous backup. Two copies splitting work: same name, same leases, different instance name. Two different jobs: separate leases. Edited three times before the reader ran: one entry, the newest. Python: trigger or pull model."),
            leo("And what is my homework today?"),
            maya("Create an order events container and a leases container, with the id as the partition key, the drawer Cosmos files each item in. Write a Python function with a Cosmos DB trigger that logs each changed id, run it locally, and edit an item in Data Explorer. Then delete one."),
            leo("And watch nothing arrive in the log. The ghost dish all over again. Case closed."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-cosmos-changefeed",
        listOf(
            maya("Sit down. Did you hear what the Zaiqa team did this week? Five people, five ways to break one thing. You know Zaiqa: the Kotlin app and the Angular dashboard call the Zaiqa API, which is the one service that writes to Azure SQL, where the orders and the payments live."),
            leo("And Cosmos DB holds the order events and the rider pings. What was the one thing?"),
            maya("What happens after an order is delivered. Three jobs: the ratings prompt, the restaurant payout, and the dish search index, which is the list our search reads. None of them is called by the API. The API writes one item into the order events container and walks away."),
            leo("So how do the three jobs hear about it?"),
            maya("Every Cosmos container keeps a change feed: an ordered list of the items created or changed, in the order it happened. Each job reads from where it stopped, and where it stopped lives in a lease container, which is a tiny second container full of bookmarks."),
            leo("Fine. Monday. Who went first?"),
            maya("Tom. Tom built the dish search job and tested it beautifully. Then Sharma's deleted paneer tikka from the menu, and Zaiqa went on recommending paneer tikka for a month."),
            leo("His job broke?"),
            maya("His job never heard. Tom's reader runs in latest version mode, which is the default, and that shows creates and updates only. A deleted item simply vanishes from the container, and the feed says nothing at all."),
            leo("[sigh] So what does Tom do now?"),
            maya("A soft delete. Mark the dish deleted with a flag, which is an update, so the feed does show it, then give the item a TTL, a time to live, and Cosmos clears it away a week later. Or read in all versions and deletes mode instead."),
            leo("What does that one give him?"),
            maya("Every create, update and delete in order, and it even says when a TTL did the deleting. It needs continuous backup on the account and keeps changes only for that window. Heavier, so you choose it on purpose."),
            leo("Tuesday. Please tell me Tuesday is worse."),
            maya("Tuesday is Priya. Her job rewrites a dish's search entry and then saves the result back onto the dish item. That save is an update."),
            leo("Which the feed shows. Which wakes her job. Which saves again."),
            maya("Round and round, all Tuesday, in our busiest container. [chuckle] She fixed it with a hash, which is a fingerprint of the dish text: the same text always gives the same code. Same fingerprint, skip the work."),
            leo("Wednesday?"),
            maya("Dana. Dana added the ratings job, copied Priya's settings, and pointed it at the same lease container, with the same prefix and the same processor name."),
            leo("And they shared the work nicely, like grown-ups?"),
            maya("They were mistaken for two copies of one job, so the leases were shared out and only one of them ever handled anything. Two copies of one job should share: same processor name, same lease container, a different instance name each. Two different jobs need their own leases."),
            leo("Thursday. I sense money."),
            maya("Marcus. The payout handler crashed halfway through a batch, came back, and paid Sharma's twice for one order."),
            leo("The feed sent it twice?"),
            maya("The bookmark moves only when a batch finishes without an error, so yes, the same change came round again. Delivery is at least once and always has been. Add one hundred and eighty rupees is unsafe. Set this order's payout to one hundred and eighty rupees, keyed by the order id, is safe."),
            leo("Set, not add. And Friday? There is always a Friday."),
            maya("The intern. The intern wanted the search index rebuilt from the whole history, so he set the start time to the beginning, restarted everything, and waited."),
            leo("And nothing replayed."),
            maya("Nothing. The start time counts the first time only. Once the leases exist, the processor resumes from them and ignores the setting. He wanted fresh leases, or the pull model and a one-off walk."),
            leo("The pull model? That is new."),
            maya("It is Python's other door. There is no change feed processor library in Python at all, so a Python job uses the Azure Functions Cosmos DB trigger, which keeps the leases for you, or the pull model, where it reads the feed itself and saves a continuation token, which is a bookmark your own code keeps."),
            leo("And the dot NET jobs?"),
            maya("They use the change feed processor library from the SDK, the pack of code Microsoft gives you, and get the leases, the sharing and the bookmarks for nothing."),
            leo("So what do I remember for the exam, apart from avoid these five?"),
            maya("Must see deletes: soft delete with a TTL, or all versions and deletes mode with continuous backup. Two copies splitting work: same processor name, same lease container, different instance name. Edited three times before the reader ran: one entry, the newest. Python: trigger or pull model."),
            leo("And what should I do today, so I am not next week's story?"),
            maya("Create an order events container and a leases container, with the id as the partition key, the drawer Cosmos files each item in. Write a small Python function with a Cosmos DB trigger that logs each changed id, run it locally, and edit an item in Data Explorer."),
            leo("Then delete one, watch nothing arrive, and spare a thought for Tom."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-cosmos-changefeed",
        listOf(
            nova("It's late. The kitchen is shut, your phone says delivered forty minutes ago, and you are still awake with me. Tonight, the thing that happens after that single word. In Zaiqa, our food delivery app, delivered quietly starts three jobs."),
            ivy("Three jobs at this hour. Remind me what Zaiqa already has."),
            nova("The Kotlin app and the Angular dashboard call the Zaiqa API, which is the one service that writes to Azure SQL, where orders and payments must be exactly right. Cosmos DB takes the order events and the rider pings, and last week it learned to search dishes by meaning."),
            ivy("And the three jobs?"),
            nova("The ratings prompt, which asks how the paneer was. The payout, which moves money to the restaurant. And the dish search index, which is the list our search reads. [pause] None of them should keep you standing at the door."),
            ivy("So the API fires all three, then answers?"),
            nova("Then one slow payments call leaves a diner watching a spinner. No. The API writes one small item into the order events container and lets go. Letting go is the whole lesson tonight."),
            ivy("Mm. And who picks it up?"),
            nova("Every Cosmos container keeps a change feed: an ordered list of the items created or changed, in the order it happened. Three readers follow that list, each at its own pace, each from where it stopped."),
            ivy("And where is where it stopped kept? Not in somebody's head, I hope."),
            nova("In a lease container: a small second container full of bookmarks. One bookmark per slice of the feed. Handled up to change five hundred and twelve, so after a restart at two in the morning, carry on at five hundred and thirteen."),
            ivy("Bookmarks. That I like. Who moves them?"),
            nova("The change feed processor. It is a library in the dot NET and Java SDKs, the packs of code Microsoft gives you, and it sits underneath the Azure Functions Cosmos DB trigger too. Give it a monitored container, a lease container, an instance name and your code."),
            ivy("An instance name. Why does it care what I am called?"),
            nova("Because you may run two copies, and they share the leases between them. Same processor name and same lease container means they are one job; the instance name is only how they tell each other apart. [pause] One bookmark belongs to one copy at a time."),
            ivy("And two different jobs?"),
            nova("Each gets its own lease container, or at least its own prefix. Point two jobs at one lease container and they are mistaken for copies of each other, and one of them never fires at all. Somebody always discovers that on a Friday."),
            ivy("The photo function is Python, isn't it. Does the library love Python back?"),
            nova("It does not. There is no change feed processor library in Python. Python gets the Azure Functions Cosmos DB trigger, which keeps the leases quietly for you, or the pull model, where your code reads the feed itself and saves a continuation token, which is a bookmark you keep yourself."),
            ivy("Two ways in. When would you keep the bookmark yourself?"),
            nova("One-off work. Zaiqa walked every old dish once, from the beginning, so the search index had them all. Save the token, stop for lunch, carry on after. For a job that must never sleep, the trigger is kinder."),
            ivy("Now tell me the part that goes wrong at three in the morning."),
            nova("A restaurant paid twice. The bookmark moves only when a batch finishes with no error, so a handler that crashes halfway sees the same change again. Delivery is at least once. Always was."),
            ivy("So the feed owes somebody a refund."),
            nova("The handler does. Add one hundred and eighty rupees is a sentence you must not read twice. Set this order's payout to one hundred and eighty rupees, keyed by the order id, you can read all night. Write every handler so it is safe to run twice."),
            ivy("Set, not add. And the ghost on the menu? You promised me a ghost."),
            nova("Latest version mode. It is the default, and it shows creates and updates only. Sharma's deletes paneer tikka, the item simply vanishes, and the feed says nothing. The search index goes on offering a dish nobody can cook."),
            ivy("That is genuinely eerie."),
            nova("The cure is a soft delete: mark it deleted with a flag, which is an update, so the feed does show it, then give the item a TTL, a time to live, and Cosmos clears it away later. The exam loves that pair."),
            ivy("And if I want real deletes, as deletes?"),
            nova("All versions and deletes mode: every create, update and delete, in order, and it even says when a TTL did it. It wants continuous backup on the account and keeps changes only for that window. Heavier, newer, chosen on purpose."),
            ivy("One more. You said the search job writes back."),
            nova("It does, onto the same dish item, which is an update, which wakes the same job again, forever. A hash of the dish text saves it: a fingerprint where the same text always gives the same code. Same fingerprint, skip."),
            ivy("A loop that feeds itself. [chuckle] Very late-night. And the start time?"),
            nova("It counts once. A brand-new reader can start at now, at a date, or at the very beginning. Once the leases exist, the processor resumes from them and ignores whatever you set."),
            ivy("Before I let you go. What do I remember for the exam?"),
            nova("Must see deletes: soft delete with a TTL, or all versions and deletes mode with continuous backup. Two copies splitting work: same processor name, same lease container, different instance name. Edited three times before the reader ran: one entry, the newest. Python: trigger or pull model."),
            ivy("And what do I do today, before I sleep?"),
            nova("Create an order events container and a leases container, with the id as the partition key, the drawer Cosmos files each item in. Write a small Python function with a Cosmos DB trigger that logs each changed id, run it locally, and edit an item in Data Explorer."),
            ivy("Then delete one, and listen to the silence. Goodnight, you."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-cosmos-changefeed",
        listOf(
            nova("Tonight is date night, darling, and I ordered in. Zaiqa, naturally. The Kotlin app and the Angular dashboard call the Zaiqa API, which is the one service allowed to write to Azure SQL, where the orders and the payments live."),
            rafe("And Cosmos DB keeps the order events and the rider pings. The food is here. What are we doing with the rest of the evening?"),
            nova("The moment after delivered. Three things must happen: the ratings prompt, the restaurant's payout, and the dish search index, which is the list our search reads. None of them should keep me waiting at the door."),
            rafe("So the API does all three while I hold the bag?"),
            nova("No. It writes one small item into the order events container, and lets go. Everything else follows that item. First question, and be careful: how does the payout job find out?"),
            rafe("The change feed. An ordered list of the items created or changed in a container, in the order it happened. It reads that list instead of asking the database what is new every minute."),
            nova("Right answer. [kiss] And it reads from where it stopped. Where is that written down?"),
            rafe("In my head, usually."),
            nova("[laugh] In a lease container, which is a tiny second container full of bookmarks: handled up to change five hundred and twelve, carry on at five hundred and thirteen. Give the change feed processor that, a monitored container, your code and an instance name, and it does the rest."),
            rafe("My turn, love. Two copies of the payout job must split the work. What has to differ between them?"),
            nova("The instance name, and only that. Same processor name, same lease container, so they count as one job sharing bookmarks. One bookmark belongs to one copy at a time."),
            rafe("Right answer. [kiss] And if I gave the ratings job the payout job's lease container?"),
            nova("Then they look like copies of each other, and only one of them ever fires. Different jobs get their own lease container, or at least their own prefix. Now, the photo function is Python. Does the processor library work there?"),
            rafe("Of course it does. It is the same database."),
            nova("No kiss for that one. There is no change feed processor library in Python at all. Python gets the Azure Functions Cosmos DB trigger, or the pull model, where your code reads the feed itself and saves a continuation token, which is a bookmark your code keeps rather than one Cosmos keeps."),
            rafe("Noted, and I will earn it back. Ask me something harder."),
            nova("Sharma's removes paneer tikka from the menu, and the search index must stop offering it. Our reader is in latest version mode. What do you do?"),
            rafe("Latest version mode never shows deletes, only creates and updates. So mark the dish deleted with a flag, which is an update the feed does show, and give the item a TTL, a time to live, so Cosmos clears it away later."),
            nova("A soft delete, exactly right. [kiss] And if you wanted the delete itself?"),
            rafe("All versions and deletes mode. Every create, update and delete, in order, and it says when a TTL did it. It needs continuous backup on the account, and it keeps changes only for that window."),
            nova("Two in a row. [kiss] My turn to be asked, I think."),
            rafe("Then here is the one that cost us money. The payout handler crashed halfway through a batch, came back, and paid Sharma's twice. Whose fault?"),
            nova("The handler's. The bookmark moves only when a batch finishes without an error, so delivery is at least once. Add one hundred and eighty rupees is unsafe. Set this order's payout to one hundred and eighty rupees, keyed by the order id, is safe however often it runs."),
            rafe("Right answer. [kiss] You are very good at being paid exactly once."),
            nova("[chuckle] One more for you. The search job writes its result back onto the dish item. What happens, and what stops it?"),
            rafe("The write is an update, so the feed wakes the same job again, forever. A hash of the dish text stops it: a fingerprint where the same text always gives the same code. Same fingerprint, skip the work."),
            nova("Right answer. [kiss] And one nobody ever gets: the start time."),
            rafe("You can start a brand-new reader at now, at a date, or at the very beginning. And once the leases exist, I think it ignores you."),
            nova("It does. The setting counts the first time only; after that the processor resumes from the leases. Which is why nothing replays when you change it in a panic."),
            rafe("Last one before the food goes cold. What do we remember for the exam?"),
            nova("Must see deletes: soft delete with a TTL, or all versions and deletes mode with continuous backup. Two copies splitting work: same name, same leases, a different instance name. Edited three times before the reader ran: one entry, the newest. Python: trigger or pull model."),
            rafe("And what are we doing with it today, tomorrow being Sunday?"),
            nova("Create an order events container and a leases container, with the id as the partition key, the drawer Cosmos files each item in. Write a small Python function with a Cosmos DB trigger that logs each changed id, run it locally, and edit an item in Data Explorer."),
            rafe("Then delete one, watch nothing happen, and come back to bed. Happy date night, darling."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun feed() = DiagramSpec(
        "An order is delivered, the event lands in the order events container, the jobs read the change feed, and the leases hold the bookmarks.",
        nodes = listOf(
            DNode("api", "Zaiqa API", 0.16f, 0.2f, w = 0.28f, h = 0.2f, tone = 1, sub = "delivered"),
            DNode("events", "order events", 0.5f, 0.2f, w = 0.3f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "change feed"),
            DNode("leases", "leases", 0.84f, 0.2f, w = 0.28f, h = 0.28f, kind = NodeKind.CYLINDER, tone = 2, sub = "bookmarks"),
            DNode("payout", "Payout job", 0.5f, 0.72f, w = 0.34f, h = 0.22f, tone = 1, sub = "Cosmos trigger"),
            DNode("ratings", "Ratings job", 0.16f, 0.72f, w = 0.28f, h = 0.22f, tone = 2, sub = "and search"),
        ),
        edges = listOf(
            DEdge("api", "events", "write"),
            DEdge("events", "payout", "change feed"),
            DEdge("events", "ratings", "same feed"),
            DEdge("payout", "leases", "bookmark", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun modes() = DiagramSpec(
        "Latest version mode gives the newest version of each changed item and no deletes; all versions and deletes mode gives every change.",
        nodes = listOf(
            DNode("latest", "Latest version mode", 0.26f, 0.17f, w = 0.46f, h = 0.24f, tone = 1, sub = "creates and updates"),
            DNode("all", "All versions and deletes", 0.76f, 0.17f, w = 0.44f, h = 0.24f, sub = "needs continuous backup"),
            DNode("one", "One entry", 0.26f, 0.5f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "edited twice: as it is now"),
            DNode("every", "Every change", 0.76f, 0.5f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "deletes included"),
            DNode("soft", "Soft delete + TTL", 0.5f, 0.84f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, tone = 2, sub = "flag it, then let it expire"),
        ),
        edges = listOf(
            DEdge("latest", "one"),
            DEdge("all", "every"),
            DEdge("latest", "soft", "no deletes", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun processor() = DiagramSpec(
        "Two copies of one processor share the leases in one lease container, each with its own instance name, and hand batches to your code.",
        nodes = listOf(
            DNode("events", "order events", 0.2f, 0.2f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "monitored"),
            DNode("leases", "leases", 0.8f, 0.2f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER, tone = 2, sub = "lease container"),
            DNode("c1", "Copy one", 0.25f, 0.62f, w = 0.4f, h = 0.2f, tone = 1, sub = "instance: one"),
            DNode("c2", "Copy two", 0.75f, 0.62f, w = 0.4f, h = 0.2f, tone = 1, sub = "instance: two"),
            DNode("deleg", "Delegate: your code", 0.5f, 0.88f, w = 0.7f, h = 0.16f, kind = NodeKind.NOTE, sub = "pays the restaurant"),
        ),
        edges = listOf(
            DEdge("events", "c1"),
            DEdge("events", "c2"),
            DEdge("c1", "leases", dashed = true),
            DEdge("c2", "leases", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun python() = DiagramSpec(
        "Python has no change feed processor library, so it reads the feed with a Functions trigger or the pull model.",
        nodes = listOf(
            DNode("trigger", "Functions trigger", 0.26f, 0.3f, w = 0.46f, h = 0.3f, tone = 1, sub = "leases handled for you"),
            DNode("pull", "Pull model", 0.76f, 0.3f, w = 0.44f, h = 0.3f, sub = "you keep a token"),
            DNode("nolib", "No processor library", 0.5f, 0.8f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "in Python: two doors"),
        ),
        edges = listOf(
            DEdge("nolib", "trigger"),
            DEdge("nolib", "pull"),
        ),
        aspect = 0.8f,
    )

    private fun loop() = DiagramSpec(
        "Saving the result back onto the dish is an update, so the feed wakes the job again; a hash of the dish text breaks the loop.",
        nodes = listOf(
            DNode("fn", "Search job", 0.26f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "Cosmos trigger"),
            DNode("dish", "The dish", 0.76f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "result saved back"),
            DNode("feed", "Change feed", 0.76f, 0.75f, w = 0.42f, h = 0.22f, sub = "sees an update"),
            DNode("hash", "Check a hash", 0.26f, 0.75f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "text changed? work"),
        ),
        edges = listOf(
            DEdge("fn", "dish", "save"),
            DEdge("dish", "feed", "update"),
            DEdge("feed", "fn", "wakes it"),
            DEdge("hash", "fn", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where the change feed stops: no deletes or in-between edits by default, a start time that counts once, and leases that must not be shared.",
        nodes = listOf(
            DNode("del", "No deletes", 0.26f, 0.2f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "latest version mode"),
            DNode("edits", "No in-between edits", 0.76f, 0.2f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "newest version only"),
            DNode("start", "Start time", 0.26f, 0.7f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "ignored once leases exist"),
            DNode("shared", "Shared lease container", 0.76f, 0.7f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, tone = 2, sub = "only one job fires"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's change feed questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("deletes", "Must see deletes", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "soft delete + TTL"),
            DNode("split", "Split the work", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "unique instance name"),
            DNode("three", "Edited three times", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "one entry, the newest"),
            DNode("py", "Python", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "trigger or pull model"),
            DNode("loops", "Job wakes itself", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "check a hash"),
            DNode("late", "Start time ignored", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "leases already exist"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("deletes", "split", "three", "py", "loops", "late")
    private val today = BoardCard("Today", "Order events and leases, a trigger that logs ids, an edit, then a delete.")

    // The pictures that come back in every telling, one card each.
    private val bookmarkCard = BoardCard("Lease: a bookmark", "Handled up to change 512? After a restart, carry on at 513.")
    private val twiceCard = BoardCard("Safe to run twice", "Set this order's payout to 180: safe. Add 180 to the payout: not safe.")
    private val hashCard = BoardCard("Hash: a fingerprint", "Same text, same code. Change one letter, and it is a different one.")
    private val ghostCard = BoardCard("The ghost dish", "A delete never reaches the feed, so the search index keeps offering it.")
    private val softCard = BoardCard("Soft delete and TTL", "A flag is an update the feed shows; a TTL clears the item later.")
    private val allModeCard = BoardCard("All versions and deletes", "Every create, update and delete, in order. Needs continuous backup.")
    private val atLeastOnceCard = BoardCard("At least once", "The bookmark moves only when a batch finishes with no error.")
    private val startCard = BoardCard("Start time", "It counts once: with leases in place the processor ignores it.")
    private val phraseCard = BoardCard("Listen for the phrase", "Soft delete with a TTL, an instance name, one entry, two doors.")

    private fun classicBoard() = PodcastBoard(
        "ai-cosmos-changefeed",
        listOf(
            // 0: "Hi, and welcome back. By now Zaiqa is almost whole..."
            Scene(
                0, "Three jobs after delivered", feed(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Where we are", "The API owns Azure SQL; Cosmos takes the order events.")),
                    Beat(2, show = setOf("ratings", "payout"), light = setOf("ratings", "payout"), card = BoardCard("After delivered", "Ratings prompt, restaurant payout, dish search index.")),
                    Beat(4, light = setOf("payout"), card = BoardCard("Not inside the API", "One slow payout should not fail the whole delivery.")),
                    Beat(6, card = BoardCard("Polling", "Ask every minute what changed: costly, mostly empty, always late.")),
                    Beat(8, show = setOf("events"), light = setOf("events"), card = BoardCard("Change feed", "An ordered list of the items created or changed, in order.")),
                    Beat(10, light = setOf("events"), pulse = "api" to "events", card = BoardCard("One small item", "The order worker writes an order event; the feed shows it in seconds.")),
                    Beat(12, card = BoardCard("Why not a SQL trigger?", "It would hold the transaction that pays the restaurant open.")),
                ),
            ),
            // 13: "All right. Who reads the feed, then? Do I write a loop?"
            Scene(
                13, "The processor", processor(),
                beats = listOf(
                    Beat(14, show = setOf("events", "leases"), light = setOf("events"), card = BoardCard("Change feed processor", "A dot NET and Java library; it runs under the Functions trigger.")),
                    Beat(16, show = setOf("c1", "deleg"), light = setOf("c1", "deleg"), card = BoardCard("What it needs", "Monitored container, lease container, instance name, your code.")),
                    Beat(18, light = setOf("leases"), pulse = "c1" to "leases", card = bookmarkCard),
                    Beat(20, show = setOf("c2"), light = setOf("c1", "c2"), pulse = "c2" to "leases", card = BoardCard("Two copies", "Same name, same leases, different instance name: the work splits.")),
                    Beat(22, light = setOf("leases"), card = BoardCard("Two different jobs", "Their own lease container, or at least their own prefix.")),
                ),
            ),
            // 25: "The photo function is Python. Does the processor work there?"
            Scene(
                25, "Python's two doors", python(),
                beats = listOf(
                    Beat(26, show = setOf("nolib", "trigger", "pull"), light = setOf("nolib"), card = BoardCard("No library in Python", "The processor library is dot NET and Java only.")),
                    Beat(28, light = setOf("trigger"), pulse = "nolib" to "trigger", card = BoardCard("Functions trigger", "Name the containers; the function signs in with a managed identity.")),
                ),
            ),
            // 29: "Nice. Now tell me what goes wrong, because something always does."
            Scene(
                29, "Paid twice", feed(),
                beats = listOf(
                    Beat(30, show = setOf("api", "events", "payout", "leases"), light = setOf("payout", "leases"), pulse = "payout" to "leases", card = atLeastOnceCard),
                    Beat(32, light = setOf("payout"), card = twiceCard),
                ),
            ),
            // 33: "Set, not add. That I can remember. Second thing?"
            Scene(
                33, "Deletes", modes(),
                beats = listOf(
                    Beat(34, show = setOf("latest", "one"), light = setOf("latest"), card = BoardCard("Latest version mode", "The default: creates and updates only, newest version of each.")),
                    Beat(36, show = setOf("soft"), light = setOf("soft"), pulse = "latest" to "soft", card = softCard),
                    Beat(38, show = setOf("all", "every"), light = setOf("all", "every"), card = allModeCard),
                ),
            ),
            // 39: "Third thing?"
            Scene(
                39, "The loop", loop(),
                beats = listOf(
                    Beat(40, show = setOf("fn", "dish", "feed", "hash"), light = setOf("fn", "dish"), pulse = "dish" to "feed", card = hashCard),
                    Beat(42, light = setOf("hash"), card = startCard),
                ),
            ),
            // 43: "Right. So what do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("deletes", "split", "three"), card = phraseCard),
                    Beat(46, light = setOf("py", "loops", "late"), card = BoardCard("The other three", "Python: trigger or pull model. A job that wakes itself: a hash.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-cosmos-changefeed--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Ghost Dish..."
            Scene(
                0, "The Ghost Dish", feed(),
                beats = listOf(
                    Beat(0, show = setOf("api", "events"), light = setOf("api"), card = BoardCard("The case", "Paneer tikka was deleted a month ago, and Zaiqa still offers it.")),
                    Beat(2, show = setOf("ratings", "payout"), light = setOf("ratings", "payout"), card = BoardCard("Three complaints", "A ghost dish, a restaurant paid twice, a job that never fired.")),
                    Beat(4, light = setOf("api"), card = BoardCard("After delivered", "Ratings prompt, restaurant payout, dish search index.")),
                    Beat(6, pulse = "api" to "events", card = BoardCard("The API walks away", "It writes one order event and goes back to answering the app.")),
                    Beat(8, light = setOf("events"), card = BoardCard("Change feed", "An ordered list of the items created or changed, in order.")),
                    Beat(10, show = setOf("leases"), light = setOf("leases"), pulse = "payout" to "leases", card = bookmarkCard),
                ),
            ),
            // 11: "Noted. Case one, the ghost dish..."
            Scene(
                11, "Clue one: the mode", modes(),
                beats = listOf(
                    Beat(12, show = setOf("latest", "one"), light = setOf("latest"), card = BoardCard("Latest version mode", "The default: creates and updates only, and no deletes at all.")),
                    Beat(14, light = setOf("one"), card = ghostCard),
                    Beat(16, show = setOf("soft"), light = setOf("soft"), pulse = "latest" to "soft", card = softCard),
                    Beat(18, show = setOf("all", "every"), light = setOf("all", "every"), card = allModeCard),
                ),
            ),
            // 19: "Case one closed. Case two, a restaurant paid twice."
            Scene(
                19, "Clue two: paid twice", processor(),
                beats = listOf(
                    Beat(20, show = setOf("events", "c1", "deleg"), light = setOf("c1", "deleg"), card = BoardCard("The payout handler", "Read a batch, added 180 rupees, then crashed before finishing.")),
                    Beat(22, show = setOf("leases"), light = setOf("leases"), pulse = "c1" to "leases", card = atLeastOnceCard),
                    Beat(24, light = setOf("deleg"), card = twiceCard),
                ),
            ),
            // 25: "Set, not add. Case two closed. Case three, the job that never fired."
            Scene(
                25, "Clue three: one lease box", processor(),
                beats = listOf(
                    Beat(26, show = setOf("events", "leases", "c1", "c2"), light = setOf("leases"), card = BoardCard("Two jobs, one lease box", "Ratings and payout shared a lease container, prefix and name.")),
                    Beat(28, light = setOf("c1", "c2"), pulse = "c2" to "leases", card = BoardCard("Mistaken for copies", "Copies share the leases, so only one job ever saw the changes.")),
                    Beat(30, light = setOf("leases"), card = BoardCard("The rule", "Copies: one name, one lease box, own instance name. Jobs: own leases.")),
                ),
            ),
            // 31: "Case closed, then. Unless there is a fourth body."
            Scene(
                31, "The near miss", loop(),
                beats = listOf(
                    Beat(32, show = setOf("fn", "dish", "feed", "hash"), light = setOf("fn", "dish"), pulse = "dish" to "feed", card = hashCard),
                ),
            ),
            // 33: "Same fingerprint, do nothing. Who reads the feed, by the way?"
            Scene(
                33, "Who reads the feed", python(),
                beats = listOf(
                    Beat(34, show = setOf("trigger", "pull", "nolib"), light = setOf("nolib"), card = BoardCard("No library in Python", "Payout is dot NET and uses it; the photo function cannot.")),
                    Beat(36, light = setOf("trigger", "pull"), pulse = "nolib" to "pull", card = BoardCard("Two doors", "The Functions trigger, or the pull model with a continuation token.")),
                    Beat(38, light = setOf("pull"), card = BoardCard("The backfill", "One walk from the beginning; a start time counts the first time only.")),
                ),
            ),
            // 39: "So what does this case teach me for the exam?"
            Scene(
                39, "Case closed", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("deletes", "split", "three", "py"), card = phraseCard),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-cosmos-changefeed--gossip",
        listOf(
            // 0: "Sit down. Did you hear what the Zaiqa team did this week?"
            Scene(
                0, "What the Zaiqa team did", feed(),
                beats = listOf(
                    Beat(0, show = setOf("api", "events"), light = setOf("api"), card = BoardCard("The week", "Five people, five ways to break what happens after delivered.")),
                    Beat(2, show = setOf("ratings", "payout"), light = setOf("ratings", "payout"), pulse = "api" to "events", card = BoardCard("Three jobs", "Ratings prompt, restaurant payout, dish search index.")),
                    Beat(4, show = setOf("leases"), light = setOf("events", "leases"), card = BoardCard("Change feed and leases", "An ordered list of changes, and a tiny box of bookmarks.")),
                ),
            ),
            // 5: "Fine. Monday. Who went first?"
            Scene(
                5, "Monday: Tom", modes(),
                beats = listOf(
                    Beat(6, show = setOf("latest", "one"), light = setOf("latest"), card = BoardCard("Tom's search job", "Tested beautifully, then a dish was deleted and it never heard.")),
                    Beat(8, light = setOf("one"), card = ghostCard),
                    Beat(10, show = setOf("soft"), light = setOf("soft"), pulse = "latest" to "soft", card = softCard),
                    Beat(12, show = setOf("all", "every"), light = setOf("all", "every"), card = allModeCard),
                ),
            ),
            // 13: "Tuesday. Please tell me Tuesday is worse."
            Scene(
                13, "Tuesday: Priya", loop(),
                beats = listOf(
                    Beat(14, show = setOf("fn", "dish"), light = setOf("fn", "dish"), pulse = "fn" to "dish", card = BoardCard("Writing the result back", "Saving onto the same dish is an update, and the feed shows it.")),
                    Beat(16, show = setOf("feed", "hash"), light = setOf("hash"), pulse = "feed" to "fn", card = hashCard),
                ),
            ),
            // 17: "Wednesday?"
            Scene(
                17, "Wednesday: Dana", processor(),
                beats = listOf(
                    Beat(18, show = setOf("events", "leases", "c1", "c2"), light = setOf("leases"), card = BoardCard("One lease box, two jobs", "Dana copied Priya's settings: same prefix, same processor name.")),
                    Beat(20, light = setOf("c1", "c2"), pulse = "c2" to "leases", card = BoardCard("Only one ever fired", "Copies share: same name, same leases, own instance name each.")),
                ),
            ),
            // 21: "Thursday. I sense money."
            Scene(
                21, "Thursday: Marcus", processor(),
                beats = listOf(
                    Beat(22, show = setOf("events", "leases", "c1", "deleg"), light = setOf("deleg"), card = BoardCard("Paid twice", "The handler crashed halfway, and the batch came round again.")),
                    Beat(24, light = setOf("leases"), pulse = "c1" to "leases", card = twiceCard),
                ),
            ),
            // 25: "Set, not add. And Friday? There is always a Friday."
            Scene(
                25, "Friday: the intern", limits(),
                beats = listOf(
                    Beat(26, show = setOf("start", "del"), light = setOf("start"), card = BoardCard("Start from the beginning", "He wanted the whole history replayed, and set the start time.")),
                    Beat(28, light = setOf("start"), card = BoardCard("Nothing replayed", "The start time counts once; with leases, the processor resumes.")),
                ),
            ),
            // 29: "The pull model? That is new."
            Scene(
                29, "Python's other door", python(),
                beats = listOf(
                    Beat(30, show = setOf("nolib", "trigger", "pull"), light = setOf("nolib", "pull"), pulse = "nolib" to "pull", card = BoardCard("No library in Python", "The trigger, or the pull model with a continuation token.")),
                    Beat(32, light = setOf("trigger"), card = BoardCard("The dot NET jobs", "They use the processor library: leases, sharing and bookmarks free.")),
                ),
            ),
            // 33: "So what do I remember for the exam, apart from avoid these five?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("deletes", "split", "three", "py"), card = phraseCard),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-cosmos-changefeed--late-night",
        listOf(
            // 0: "It's late. The kitchen is shut, your phone says delivered..."
            Scene(
                0, "After delivered", feed(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Tonight", "What happens after your order says delivered.")),
                    Beat(2, light = setOf("api"), card = BoardCard("Where we are", "The API owns Azure SQL; Cosmos takes the order events.")),
                    Beat(4, show = setOf("ratings", "payout"), light = setOf("ratings", "payout"), card = BoardCard("Three jobs", "Ratings prompt, restaurant payout, dish search index.")),
                    Beat(6, show = setOf("events"), light = setOf("events"), pulse = "api" to "events", card = BoardCard("Letting go", "One small item into order events, and the API answers the diner.")),
                    Beat(8, light = setOf("events"), card = BoardCard("Change feed", "An ordered list of the items created or changed, in order.")),
                    Beat(10, show = setOf("leases"), light = setOf("leases"), pulse = "payout" to "leases", card = bookmarkCard),
                ),
            ),
            // 11: "Bookmarks. That I like. Who moves them?"
            Scene(
                11, "Who moves the bookmarks", processor(),
                beats = listOf(
                    Beat(12, show = setOf("events", "leases", "c1", "deleg"), light = setOf("c1", "deleg"), card = BoardCard("Change feed processor", "A dot NET and Java library, and under the Functions trigger.")),
                    Beat(14, show = setOf("c2"), light = setOf("c1", "c2"), pulse = "c2" to "leases", card = BoardCard("Two copies", "Same name, same leases, different instance name: the work splits.")),
                    Beat(16, light = setOf("leases"), card = BoardCard("Two different jobs", "Their own lease container, or at least their own prefix.")),
                ),
            ),
            // 17: "The photo function is Python, isn't it."
            Scene(
                17, "Python at this hour", python(),
                beats = listOf(
                    Beat(18, show = setOf("nolib", "trigger", "pull"), light = setOf("nolib", "trigger"), pulse = "nolib" to "trigger", card = BoardCard("No library in Python", "The Functions trigger, or the pull model you steer yourself.")),
                    Beat(20, light = setOf("pull"), pulse = "nolib" to "pull", card = BoardCard("The one-off walk", "Save the continuation token, stop for lunch, carry on after.")),
                ),
            ),
            // 21: "Now tell me the part that goes wrong at three in the morning."
            Scene(
                21, "Three in the morning", processor(),
                beats = listOf(
                    Beat(22, show = setOf("events", "leases", "c1", "deleg"), light = setOf("leases"), pulse = "c1" to "leases", card = atLeastOnceCard),
                    Beat(24, light = setOf("deleg"), card = twiceCard),
                ),
            ),
            // 25: "Set, not add. And the ghost on the menu?"
            Scene(
                25, "The ghost on the menu", modes(),
                beats = listOf(
                    Beat(26, show = setOf("latest", "one"), light = setOf("latest"), card = BoardCard("Latest version mode", "The default: creates and updates only, and no deletes at all.")),
                    Beat(28, show = setOf("soft"), light = setOf("soft"), pulse = "latest" to "soft", card = softCard),
                    Beat(30, show = setOf("all", "every"), light = setOf("all", "every"), card = allModeCard),
                ),
            ),
            // 31: "One more. You said the search job writes back."
            Scene(
                31, "The loop that feeds itself", loop(),
                beats = listOf(
                    Beat(32, show = setOf("fn", "dish", "feed", "hash"), light = setOf("fn", "dish"), pulse = "dish" to "feed", card = hashCard),
                    Beat(34, light = setOf("hash"), card = startCard),
                ),
            ),
            // 35: "Before I let you go. What do I remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("deletes", "split", "three", "py"), card = phraseCard),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-cosmos-changefeed--couples",
        listOf(
            // 0: "Tonight is date night, darling, and I ordered in."
            Scene(
                0, "Date night", feed(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Date night", "The API owns Azure SQL; Cosmos takes the order events.")),
                    Beat(2, show = setOf("ratings", "payout"), light = setOf("ratings", "payout"), card = BoardCard("After delivered", "Ratings prompt, restaurant payout, dish search index.")),
                    Beat(4, show = setOf("events"), light = setOf("events"), pulse = "api" to "events", card = BoardCard("Quiz: who finds out?", "The API writes one item and lets go. How does the payout job hear?")),
                    Beat(5, light = setOf("events"), card = BoardCard("Right answer", "The change feed: an ordered list of the items created or changed.")),
                    Beat(8, show = setOf("leases"), light = setOf("leases"), pulse = "payout" to "leases", card = bookmarkCard),
                ),
            ),
            // 9: "My turn, love. Two copies of the payout job must split the work."
            Scene(
                9, "Knowing your instance", processor(),
                beats = listOf(
                    Beat(9, show = setOf("events", "leases", "c1", "c2"), light = setOf("c1", "c2"), card = BoardCard("Quiz: two copies", "Two copies of the payout job must split the work. What differs?")),
                    Beat(10, light = setOf("leases"), pulse = "c2" to "leases", card = BoardCard("Right answer", "Only the instance name. Same processor name, same lease container.")),
                    Beat(12, light = setOf("leases"), card = BoardCard("Two different jobs", "Their own lease container, or prefix; otherwise one never fires.")),
                ),
            ),
            // 13: "Of course it does. It is the same database."
            Scene(
                13, "No kiss for that one", python(),
                beats = listOf(
                    Beat(13, show = setOf("nolib"), light = setOf("nolib"), card = BoardCard("Quiz: Python", "Does the change feed processor library work in Python?")),
                    Beat(14, show = setOf("trigger", "pull"), light = setOf("trigger", "pull"), pulse = "nolib" to "pull", card = BoardCard("No kiss: there is none", "Python gets the Functions trigger, or the pull model and a token.")),
                ),
            ),
            // 16: "Sharma's removes paneer tikka from the menu."
            Scene(
                16, "The dish that was removed", modes(),
                beats = listOf(
                    Beat(16, show = setOf("latest", "one"), light = setOf("latest"), card = BoardCard("Quiz: the removed dish", "The reader is in latest version mode. How does the search learn?")),
                    Beat(18, show = setOf("soft"), light = setOf("soft"), pulse = "latest" to "soft", card = BoardCard("Right answer: soft delete", "Mark it deleted, which is an update, then let a TTL clear the item.")),
                    Beat(19, show = setOf("all", "every"), light = setOf("all", "every"), card = allModeCard),
                ),
            ),
            // 21: "Then here is the one that cost us money."
            Scene(
                21, "Paid exactly once", processor(),
                beats = listOf(
                    Beat(21, show = setOf("events", "leases", "c1", "deleg"), light = setOf("deleg"), card = BoardCard("Quiz: paid twice", "The handler crashed halfway and paid Sharma's twice. Whose fault?")),
                    Beat(22, light = setOf("leases"), pulse = "c1" to "leases", card = BoardCard("Right answer", "The handler's: delivery is at least once, and the bookmark had not moved.")),
                    Beat(23, light = setOf("deleg"), card = twiceCard),
                ),
            ),
            // 24: "One more for you. The search job writes its result back."
            Scene(
                24, "The job that wakes itself", loop(),
                beats = listOf(
                    Beat(24, show = setOf("fn", "dish"), light = setOf("fn", "dish"), pulse = "fn" to "dish", card = BoardCard("Quiz: writing back", "The search job saves its result onto the dish item. What happens?")),
                    Beat(25, show = setOf("feed", "hash"), light = setOf("hash"), pulse = "feed" to "fn", card = hashCard),
                    Beat(27, card = startCard),
                ),
            ),
            // 29: "Last one before the food goes cold. What do we remember for the exam?"
            Scene(
                29, "For the exam", exam(),
                beats = listOf(
                    Beat(30, show = all6, light = setOf("deletes", "split", "three", "py"), card = phraseCard),
                    Beat(32, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
