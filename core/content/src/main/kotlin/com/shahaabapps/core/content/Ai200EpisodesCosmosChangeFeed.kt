package com.shahaabapps.core.content

/** The Cosmos DB change feed: reacting to new and updated items: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesCosmosChangeFeed {
    private val classic = Podcast(
        "ai-cosmos-changefeed",
        listOf(
            maya("Hi, and welcome back. Today is about reacting when data changes. Say you're building the Two Tracks Coach, a study assistant in an Android app, with a Python backend on Azure. The learners' notes live in Azure Cosmos DB, Microsoft's cloud database, in a container called notes. A container is like one folder for one kind of item."),
            leo("And something has to happen when a note changes?"),
            maya("Yes. Each note has an embedding. That's the list of numbers an AI model writes for a piece of text, so texts that mean similar things get nearby spots, like pins on a map. For example, boiling pasta and cooking spaghetti land close together; paying tax lands far away."),
            leo("So the Coach can search by meaning. And when a note is edited?"),
            maya("Its pin is in the wrong place, so the embedding must be made again. So how does your code find out that a note changed?"),
            leo("Check every note, every minute? [pause] That sounds slow and expensive."),
            maya("It is. Cosmos DB already keeps something better: the change feed. The change feed is a running list of the items in a container that were created or changed, in the order it happened. Your code reads that list and reacts, instead of searching the whole container for what's new."),
            leo("Like the visitors' book at a front desk. Who came in, and when."),
            maya("Lovely. And every container already has one; you don't switch it on. What you choose is the mode: how your reader sees that list. There are two, and the exam loves them both."),
            leo("Go on, then. Mode one."),
            maya("Latest version mode, the default. It shows creates and updates, and for each changed item only its newest version. For example, a note is created, then edited twice, before your code reads the feed. You get one entry: the note as it is now."),
            leo("And deletes?"),
            maya("Not shown. In latest version mode, a deleted item simply vanishes, with no entry at all. That trips a lot of people up."),
            leo("So how would the Coach ever notice a deleted note?"),
            maya("With a soft delete. Instead of deleting the note, you set a flag on it, a yes-or-no field called deleted. That's an update, so the feed shows it. Then you give the note a TTL, time to live: a timer, say one day, after which Cosmos DB removes the item by itself."),
            leo("Mark it, then let it expire. Clever. And mode two?"),
            maya("All versions and deletes mode. It shows every create, every update and every delete, in order. But it needs continuous backup turned on for your Cosmos DB account: Azure keeps a rolling record of your data for a set time, and the feed only reaches back that far."),
            leo("Okay, so there's a list. Who actually reads it?"),
            maya("The change feed processor: a library, ready-made code, for dot NET and Java programs. It remembers where it got to, and shares the work between machines. It also runs underneath the Azure Functions trigger, which we'll come to."),
            leo("How does it remember where it got to?"),
            maya("With leases. A lease is a bookmark. Say the reader has handled the changes up to number 512. The lease says 512. The machine restarts, opens the bookmark, and carries on at 513, instead of starting over. The leases live in a container of their own, the lease container: a box of bookmarks."),
            leo("One bookmark for the whole container?"),
            maya("One per slice. Cosmos DB spreads items across partitions. Picture a filing cabinet: each partition is a drawer, and the partition key, here the learner's id, decides an item's drawer. Each lease covers a slice of drawers. Order is promised within a drawer: Asha's three edits arrive in order, though hers and Ben's may interleave."),
            leo("And if I run two copies of the processor? Do both do everything?"),
            maya("Not if you set it up right. Same processor name, same lease container, and each its own instance name, a unique name per copy. Then each lease is checked out to one copy at a time, like a library book, and the copies split the work. And give the lease container the partition key slash id."),
            leo("And what does my own code actually do?"),
            maya("You write the delegate: your function that handles each batch of changes. For the Coach, rebuild the embedding. And here's the rule. The bookmark only moves after your code finishes a batch without an error. Crash halfway, and the batch comes again. So delivery is at least once: the same change can arrive twice."),
            leo("So my code has to cope with seeing a note twice. How?"),
            maya("Make it safe to run again. Set the word count to 120 is safe to repeat; add 20 to the count is not. One more setting, the start time: where a brand-new processor begins. Now, a date, or the very beginning. Once leases exist, the processor resumes from them and ignores it."),
            leo("I'm writing Python, though. You said dot NET and Java."),
            maya("Right. Python has no change feed processor library. So it has two choices. First, Azure Functions: small pieces of code Azure runs for you when something happens, like a doorbell. A function with a Cosmos DB trigger wakes up when notes change, and is handed the changed items. Leases are handled for you."),
            leo("And the second choice?"),
            maya("The pull model. Your code reads the feed itself, and saves a continuation token: a bookmark it keeps for itself, to carry on later. It suits a one-off job. The Coach used it once, from the beginning, to embed all the old notes."),
            leo("Any traps with the trigger?"),
            maya("A nasty one. The function saves the new embedding back onto the note. But saving is an update, and updates appear in the feed. So the function triggers itself, again and again, forever."),
            leo("[laugh] A function chasing its own tail. How do I stop it?"),
            maya("With a hash: a fingerprint of some data. Same text, same fingerprint; change one letter, and you get a completely different one. Save the fingerprint of the note's text next to the embedding. When the function wakes, it compares: same fingerprint means the text didn't change, so it skips."),
            leo("Give me an example."),
            maya("Asha's note hashes to, say, 7 F 3 A. The function embeds it and saves 7 F 3 A beside it. That save wakes the function, but the text still hashes to 7 F 3 A, so it stops. An edit changes the fingerprint, so it re-embeds. A second function on the same container needs its own lease container, or a different prefix, a name tag on its leases."),
            leo("So what do I remember for the exam?"),
            maya("If the reader must see deletes in latest version mode, the answer is a soft delete with a TTL, or all versions and deletes mode with continuous backup. If two copies must split the work, a unique instance name. If a note changed three times, one entry. If it's Python, a Functions trigger or the pull model."),
            leo("And anything to try today?"),
            maya("Create a notes container and a leases container with partition key slash id. Write a Python function with a Cosmos DB trigger that logs each changed id. Run it locally with func start, the tool that runs functions on your laptop, and edit an item in Data Explorer, the item editor on Azure's website. Then delete an item, see nothing logged, and try a deleted flag instead."),
            leo("A visitors' book that never forgets. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-cosmos-changefeed",
        listOf(
            maya("Welcome to Case Files. Today's case: The Note That Wouldn't Die. Margin Notes is a small Android study app, with a Python backend on Azure. It has a smart search that finds notes by meaning. And notes that students deleted last week keep turning up in that search."),
            leo("Ghost notes. Detective Leo is on the case. How does the search find notes by meaning?"),
            maya("Each note has an embedding. That's the list of numbers an AI model writes for a piece of text, so texts that mean similar things get nearby spots, like pins on a map. Boiling pasta and cooking spaghetti land close; paying tax lands far away. The search looks for pins near the question's pin."),
            leo("So when a note changes, its pin has to move."),
            maya("Exactly, and when a note is deleted, its pin must go. The notes live in Azure Cosmos DB, Microsoft's cloud database, in a container, a folder for one kind of item. And the app hears about changes through the change feed: a running list of the items in a container that were created or changed, in the order it happened."),
            leo("Like a visitors' book. In order, you said?"),
            maya("In order within each partition. Picture a filing cabinet: each partition is a drawer, and the partition key, here the student's id, decides an item's drawer. One student's changes always arrive in order; two students' may interleave. Under the picture, the feed is the container's own record of writes, not a separate copy."),
            leo("Then my first suspect: the change feed is switched off. Nobody turned it on."),
            maya("[pause] Wrong suspect. Every container has a change feed already. You don't switch it on. You choose a mode. And the app reads in latest version mode, the default. Creates and updates, the newest version of each item. And deletes?"),
            leo("Let me guess. Not shown."),
            maya("Not shown. A deleted note simply vanishes from the feed, with no entry at all. So the search never hears about it, and the ghost stays."),
            leo("Then start the reader again from the very beginning. It'll replay everything, deletes included."),
            maya("[sigh] Replaying gives you creates and updates again. Deletes still never appear in this mode. Two real fixes. First, a soft delete: set a flag on the note, a yes-or-no field called deleted. That's an update, so the feed shows it. Then a TTL, time to live, a timer, say a day, after which Cosmos DB removes the item itself."),
            leo("And the second fix?"),
            maya("All versions and deletes mode. It shows every create, update and delete, in order. But it needs continuous backup on the Cosmos DB account, where Azure keeps a rolling record of your data, and the feed only reaches back that far. Margin Notes picked the soft delete."),
            leo("And in latest version mode, does the reader see every edit?"),
            maya("No, only the newest version. Edit a note twice before the reader runs, and it gets one entry: the note as it is now."),
            leo("Ghosts handled. Case closed?"),
            maya("Not yet. Second witness: the finance team. Since launch, the embedding function has run millions of times. More than there are notes."),
            leo("What runs it?"),
            maya("Azure Functions: small pieces of code Azure runs for you when something happens, like a doorbell. This one has a Cosmos DB trigger, so it wakes up when notes change. Underneath, the trigger uses the change feed processor, the library that reads the feed and remembers where it got to."),
            leo("Then my suspect is the trigger. It's broken and firing at random."),
            maya("Not random. Look at what the function does. It makes the new embedding, and saves it back onto the note. And saving is an update."),
            leo("[pause] And updates appear in the feed. So it triggers itself. Forever."),
            maya("Detective Leo, at last. The fix is a hash: a fingerprint of some data. Same text, same fingerprint; change one letter, and it's a completely different one. Save the fingerprint of the note's text next to the embedding."),
            leo("Show me how that stops the loop."),
            maya("A note's text hashes to, say, 7 F 3 A. The function embeds it and saves 7 F 3 A beside it. That save wakes the function again, but the text still hashes to 7 F 3 A, so it skips. A student edits a word, the fingerprint becomes B 2 9 C, and only then does it re-embed."),
            leo("Any more clues in the file?"),
            maya("A third. Someone added a second function to count words per student, on the same notes container. It never fires. Not once."),
            leo("Bad code in the new function?"),
            maya("Bad bookmarks. The processor remembers where it got to with leases. A lease is a bookmark: handled up to change 512, so after a restart, carry on at 513. Leases live in a lease container, a box of bookmarks. Both functions used the same box, with the same prefix, a name tag on the leases. So they fought over one set of bookmarks."),
            leo("So, its own lease container."),
            maya("Or a different prefix. And two copies of one processor are the reverse: same processor name, same lease container, and a unique instance name per copy. Then each lease is checked out to one copy at a time, like a library book, and they split the work."),
            leo("Last thing. The word count is sometimes too high."),
            maya("Because delivery is at least once. The processor moves the bookmark only after your code finishes a batch without error. Crash halfway, and the batch comes again. So handlers must be safe to run twice. Set the count to 120 is safe to repeat; add 20 to the count is not."),
            leo("Any loose ends? Margin Notes is written in Python, isn't it?"),
            maya("It is, and that's where the processor stops. Python has no change feed processor library; that's dot NET and Java. So it's the Functions trigger, or the pull model: your code reads the feed itself and keeps a continuation token, its own bookmark. The Cosmos performance episode covers what each read of the feed costs."),
            leo("So, the case summary for the exam?"),
            maya("If the reader must see deletes in latest version mode, a soft delete with a TTL, or all versions and deletes mode with continuous backup. If a function triggers itself, check a hash before saving. If two copies must split the work, a unique instance name. If it's Python, a Functions trigger or the pull model."),
            leo("Case closed. Anything to try today?"),
            maya("Create a notes container and a leases container with partition key slash id. Write a Python function with a Cosmos DB trigger that logs each changed id. Run it locally with func start, the tool that runs functions on your laptop, and edit an item in Data Explorer, the item editor on Azure's website. Then delete an item, see nothing logged, and try a deleted flag instead."),
            leo("No more ghosts. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-cosmos-changefeed",
        listOf(
            maya("Okay, did you hear about the Coach team's change feed week? Sit down. The Two Tracks Coach, the study assistant in the Android app, with its Python backend on Azure, wanted notes re-embedded the moment they change. And it was a week."),
            leo("[laugh] I heard the word forever in a meeting title. Re-embedded? What does that even mean?"),
            maya("Each note has an embedding. That's the list of numbers an AI model writes for a piece of text, so texts that mean similar things get nearby spots, like pins on a map. Boiling pasta and cooking spaghetti land close; paying tax lands far away. Edit the note, and its pin must move: it's re-embedded."),
            leo("And how do they spot a change?"),
            maya("The notes live in Azure Cosmos DB, Microsoft's cloud database, in a container, a folder for one kind of item. Every container has a change feed, already on: a running list of the items that were created or changed, in the order it happened. Like a visitors' book at a front desk."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. Tom wired the feed to the search, and then deleted a test note. And waited. And waited."),
            leo("Nothing?"),
            maya("Nothing. The default is latest version mode: creates and updates, the newest version of each item. Deletes are not shown. A deleted item just vanishes from the feed. So Tom's test note stayed in the search all day."),
            leo("Poor Tom. What should he have done?"),
            maya("A soft delete. Set a flag on the note, a yes-or-no field called deleted. That's an update, so the feed shows it. Then a TTL, time to live, a timer, say a day, after which Cosmos DB removes the item itself. Or all versions and deletes mode, which shows deletes too, but needs continuous backup, a rolling record Azure keeps of your data."),
            leo("And Tuesday?"),
            maya("Tuesday was Dana. Dana wrote the function. Azure Functions are small pieces of code Azure runs for you when something happens, like a doorbell. Hers had a Cosmos DB trigger, so it woke up whenever a note changed, made the new embedding, and saved it back onto the note."),
            leo("Sounds right. What went wrong?"),
            maya("Saving it back is an update. Updates appear in the feed. So the function woke itself up. Again. And again. By lunch it had run eighty thousand times on twelve notes."),
            leo("[chuckle] That's the forever meeting. How did she fix it?"),
            maya("A hash: a fingerprint of some data. Same text, same fingerprint; change one letter, and it's a completely different one. Dana saves the fingerprint of the note's text next to the embedding."),
            leo("And how does a fingerprint stop the loop?"),
            maya("Say a note hashes to 7 F 3 A. The function embeds it and saves 7 F 3 A beside it. That save wakes it again, but the text still hashes to 7 F 3 A, so it skips. Only a real edit, a new fingerprint like B 2 9 C, means re-embed."),
            leo("Wednesday?"),
            maya("Wednesday, Marcus. Marcus added a second function, counting words per learner, on the same notes container. And his never fired. Not once."),
            leo("Why not?"),
            maya("Leases. Underneath, the trigger uses the change feed processor, the library that reads the feed for you. It remembers where it got to with leases. A lease is a bookmark: handled up to change 512, so after a restart, carry on at 513. They're kept in a lease container, a box of bookmarks."),
            leo("And Marcus's box?"),
            maya("Was Dana's box, with the same prefix, the name tag on the leases. They fought over one set of bookmarks, and Dana's always won. He needed his own lease container, or a different prefix."),
            leo("And if it's two copies of the same function?"),
            maya("The reverse: same processor name, same lease container, and a unique instance name per copy. Each lease is checked out to one copy at a time, like a library book, so they split the work."),
            leo("Thursday. I can feel it."),
            maya("Thursday, the intern. Bless them. The word counts came out too high for some learners, and they blamed the database."),
            leo("Was it the database?"),
            maya("No. Delivery is at least once. The processor hands a batch to your code, and only moves the bookmark once your code returns without an error. One run crashed halfway, so the batch came again. The handler said add 20 to the count, which isn't safe to repeat. Set the count to 120 would have been."),
            leo("And the order of changes? Can that go wrong too?"),
            maya("Order is promised within a partition. Picture a filing cabinet: each partition is a drawer, and the partition key, here the learner's id, decides an item's drawer. One learner's edits arrive in order; two learners' may interleave."),
            leo("And Friday? There's always a Friday."),
            maya("The Friday. Priya. Priya never makes mistakes, so everyone is still talking about it. She wanted the function to embed all the old notes too. So she changed the start time to the beginning, and published it again."),
            leo("Sounds sensible."),
            maya("Except the start time only matters for a brand-new processor. Leases already existed, so it carried on from them and ignored her setting. Nothing replayed. In the end, she wrote a one-off pull model script instead: Python reads the feed itself, from the beginning, and saves a continuation token, its own bookmark."),
            leo("Why not just use the processor in Python?"),
            maya("Because Python has no change feed processor library; that's dot NET and Java. So Python uses the Functions trigger or the pull model. The Cosmos performance episode explains what every read of the feed costs."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If the reader must see deletes in latest version mode, a soft delete with a TTL, or all versions and deletes mode with continuous backup. If two copies must split the work, a unique instance name. If a note changed three times, one entry, the newest. If it's Python, a Functions trigger or the pull model."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a notes container and a leases container with partition key slash id. Write a Python function with a Cosmos DB trigger that logs each changed id. Run it locally with func start, the tool that runs functions on your laptop, and edit an item in Data Explorer, the item editor on Azure's website. Then delete an item, see nothing logged, and try a deleted flag instead."),
            leo("Eighty thousand runs. I'll never forget, Dana. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-cosmos-changefeed",
        listOf(
            nova("Hey, you. It's late, the street is quiet, and it's just the three of us. Me, Ivy, and you, still up. [pause] Tonight, let's talk about noticing when something changes."),
            ivy("Noticing things, at this hour? I notice you, Nova."),
            nova("[chuckle] Sweet. Picture the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. Its notes live in Azure Cosmos DB, Microsoft's cloud database, in a container, a folder for one kind of item. And each note has an embedding."),
            ivy("Remind me. An embedding is..."),
            nova("The list of numbers an AI model writes for a piece of text, so texts that mean similar things get nearby spots, like pins on a map. Boiling pasta and cooking spaghetti, close together. Paying tax, far away. [pause] Edit the note, and its pin must move. So something has to notice the edit."),
            ivy("Something has to be paying attention."),
            nova("And that something is the change feed. A running list of the items in a container that were created or changed, in the order it happened. Your code reads it and reacts, instead of searching everything for what's new."),
            ivy("What is it, really? Under the picture."),
            nova("The container's own record of writes, read in order within each partition. Picture a filing cabinet: each partition is a drawer, and the partition key, here the learner's id, picks an item's drawer. [pause] One learner's edits arrive in order; two learners' may interleave."),
            ivy("And do I have to switch it on?"),
            nova("Never. Every container has one. [pause] You only choose a mode."),
            ivy("Tell me the modes. Slowly."),
            nova("Latest version mode, the default. Creates and updates, and only the newest version of each changed item. Create a note, edit it twice before your code reads, and you get one entry: the note as it is now."),
            ivy("And if I delete one?"),
            nova("It vanishes. No entry at all. That's where latest version mode stops working. [pause] So you use a soft delete. Set a flag on the note, a yes-or-no field called deleted. That's an update, so the feed shows it. Then a TTL, time to live, a timer, say a day, after which Cosmos DB removes the item itself."),
            ivy("Mark it, and let it fade away. Very late night."),
            nova("Or all versions and deletes mode. Every create, update and delete, in order. But it needs continuous backup on the account, a rolling record Azure keeps of your data, and the feed only reaches back that far."),
            ivy("Can a reader start from anywhere it likes?"),
            nova("In latest version mode, from the beginning, a point in time, now, or a saved bookmark. In all versions and deletes mode, only from now or a bookmark. [pause] It's also newer, with narrower support in the libraries, so check its status in the docs."),
            ivy("And who reads all this, while we sleep?"),
            nova("The change feed processor, a library for dot NET and Java programs. It remembers where it got to with leases. A lease is a bookmark: handled up to change 512, so after a restart, carry on at 513. [pause] The leases sleep in their own container, the lease container. A box of bookmarks."),
            ivy("How does the bookmark actually move?"),
            nova("The processor hands a batch of changes to your code, and only after your code finishes without error does it write the new position into the lease. [pause] Crash halfway, and the same batch comes again. Delivery is at least once. So be safe to run twice: set the count to 120, never add 20 to it."),
            ivy("And if two copies of the processor are running?"),
            nova("Same processor name, same lease container, and each its own instance name, a unique name per copy. Then each lease is checked out to one copy at a time, like a library book, and they split the work."),
            ivy("And two different jobs, reading the same notes?"),
            nova("Then each needs its own lease container, or a different prefix, a name tag on its leases. Share one, with the same prefix, and they fight over the same bookmarks. Only one of them gets the changes."),
            ivy("But the Coach is Python."),
            nova("And Python has no change feed processor library. So it uses Azure Functions, small pieces of code Azure runs for you when something happens, like a doorbell. A function with a Cosmos DB trigger wakes when notes change. [pause] Or the pull model: your code reads the feed itself, and keeps a continuation token, its own bookmark."),
            ivy("Any trap waiting in the dark?"),
            nova("One. The function saves the new embedding back onto the note. That's an update. The feed sees it. And the function wakes itself, again, forever. [pause] The fix is a hash: a fingerprint of some data. Same text, same fingerprint; change one letter, and it's a different one."),
            ivy("Show me, gently."),
            nova("The note's text hashes to, say, 7 F 3 A. The function embeds it, and saves 7 F 3 A beside it. That save wakes it, but the text still hashes to 7 F 3 A. [pause] So it rolls over and goes back to sleep. Only a real edit, a new fingerprint, means re-embed."),
            ivy("And if I start a processor from the beginning, to catch up on old notes?"),
            nova("The start time only counts the very first time. Once leases exist, it resumes from them, and ignores the setting. The Cosmos performance episode tells you what each read of the feed costs."),
            ivy("[sigh] Mm. Before I drift off. What do I remember for the exam?"),
            nova("If the reader must see deletes in latest version mode, a soft delete with a TTL, or all versions and deletes mode with continuous backup. If two copies must split the work, a unique instance name. If a note changed three times, one entry. If it's Python, a Functions trigger or the pull model."),
            ivy("And something to try today, before bed?"),
            nova("Create a notes container and a leases container with partition key slash id. Write a Python function with a Cosmos DB trigger that logs each changed id. [pause] Run it locally with func start, the tool that runs functions on your laptop, and edit an item in Data Explorer, the item editor on Azure's website. Then delete an item, see nothing logged, and try a deleted flag instead."),
            ivy("Every change, noticed. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-cosmos-changefeed",
        listOf(
            nova("Tonight is date night, darling. And tonight, Rafe, we're talking about change."),
            rafe("Change? I'm perfect as I am."),
            nova("[laugh] Then you'll love the change feed in Azure Cosmos DB, Microsoft's cloud database. It notices every little change. Picture the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. Its notes live in a container, which is like a folder for one kind of item."),
            rafe("And when a note changes, someone notices. How attentive."),
            nova("Each note has an embedding: the list of numbers an AI model writes for a piece of text, so texts that mean similar things get nearby spots, like pins on a map. Boiling pasta sits close to cooking spaghetti, far from paying tax. Edit the note, and its pin must move."),
            rafe("Like us. Close together. And who notices the edit?"),
            nova("The change feed: a running list of the items in a container that were created or changed, in the order it happened. Like a visitors' book. Every container has one, always on. You only choose the mode."),
            rafe("In order, you said. Always?"),
            nova("Within a partition. Picture a filing cabinet: each partition is a drawer, and the partition key, here the learner's id, picks an item's drawer. One learner's edits arrive in order. Two learners' may interleave, like two conversations at one table."),
            rafe("Then let me test you, darling. Latest version mode, the default. A note is created, then edited twice, before the reader runs. What does the reader get?"),
            nova("One entry: the note as it is now. Latest version mode shows creates and updates, but only the newest version of each changed item."),
            rafe("Right answer. Only the latest version of you matters. [kiss]"),
            nova("Mm. My turn. In latest version mode, a learner deletes a note. What does the feed show?"),
            rafe("A delete entry, with the note's id."),
            nova("No kiss for that one. Nothing at all. A deleted item just vanishes from the feed. So what would you do?"),
            rafe("A soft delete. Set a flag on the note, a yes-or-no field called deleted. That's an update, so the feed shows it. Then a TTL, time to live, a timer after which Cosmos DB removes it. Or all versions and deletes mode, which shows deletes, but needs continuous backup, a rolling record Azure keeps of your data."),
            nova("Right answer. Letting go gently. [kiss]"),
            rafe("Now, who reads the feed? I hope it's someone who remembers where we left off."),
            nova("The change feed processor, a library for dot NET and Java programs. It remembers with leases. A lease is a bookmark: handled up to change 512, so after a restart, carry on at 513. The bookmarks live in the lease container, a box of them."),
            rafe("Someone who remembers where we left off. My kind of reader. How does the bookmark move?"),
            nova("The processor hands a batch of changes to your code, and only after your code finishes without error does it save the new position in the lease. Crash halfway, and the batch comes again. So delivery is at least once. Be safe to run twice: set the count to 120, never add 20 to it."),
            rafe("At least once. Never just once. I like that."),
            nova("Then a question for you. Two copies of a processor should split the work for the same container. What must differ between them?"),
            rafe("The instance name, a unique name per copy. Same processor name, same lease container. Then each lease is checked out to one copy at a time, like a library book."),
            nova("Right answer. Sharing, but never the same bookmark. [kiss]"),
            rafe("And the Coach is Python. Does the processor speak Python?"),
            nova("It doesn't. Python uses Azure Functions, small pieces of code Azure runs for you when something happens, like a doorbell. A function with a Cosmos DB trigger wakes when notes change, with the processor underneath. Or the pull model, where your code reads the feed and keeps a continuation token, its own bookmark."),
            rafe("My question, then. The function makes a new embedding and saves it onto the note. And now it runs forever. Why, and what's the fix?"),
            nova("Saving is an update, and updates appear in the feed, so it wakes itself. The fix is a hash: a fingerprint of some data. Same text, same fingerprint; change one letter, a different one. The text hashes to 7 F 3 A, saved beside the embedding. The next wake still sees 7 F 3 A, so it skips."),
            rafe("Right answer. Some things should only get you excited once. [kiss]"),
            nova("[chuckle] Behave. My question. A processor already has leases, and you change its start time to the beginning. What replays?"),
            rafe("Everything, from the very first note?"),
            nova("No kiss for that one. Nothing replays. The start time only counts the first time; once leases exist, it resumes from them. Now, where does it all stop working, Rafe?"),
            rafe("Deletes in latest version mode, as we said. The in-between edits are never seen. And two functions sharing one lease container with the same prefix, a name tag on the leases, fight, so only one gets the changes. The Cosmos performance episode covers what each read costs."),
            nova("Last question, for the exam. What do we remember?"),
            rafe("If the reader must see deletes in latest version mode, a soft delete with a TTL, or all versions and deletes mode with continuous backup. If two copies must split the work, a unique instance name. If a note changed three times, one entry. If it's Python, a Functions trigger or the pull model."),
            nova("Four for four. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Create a notes container and a leases container with partition key slash id. Write a Python function with a Cosmos DB trigger that logs each changed id. Run it locally with func start, the tool that runs functions on your laptop, and edit an item in Data Explorer, the item editor on Azure's website. Then delete an item, see nothing logged, and try a deleted flag instead."),
            rafe("And I'll notice every change in you. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun feed() = DiagramSpec(
        "A saved note appears in the change feed, a function rebuilds its embedding, and the lease container keeps the bookmark.",
        nodes = listOf(
            DNode("app", "Coach API", 0.16f, 0.2f, w = 0.28f, h = 0.2f, tone = 1, sub = "saves a note"),
            DNode("notes", "notes container", 0.5f, 0.2f, w = 0.3f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "change feed"),
            DNode("leases", "leases", 0.84f, 0.2f, w = 0.28f, h = 0.28f, kind = NodeKind.CYLINDER, tone = 2, sub = "bookmarks"),
            DNode("fn", "Embed function", 0.5f, 0.72f, w = 0.34f, h = 0.22f, tone = 1, sub = "Cosmos DB trigger"),
            DNode("aoai", "Azure OpenAI", 0.16f, 0.72f, w = 0.28f, h = 0.22f, tone = 2, sub = "new embedding"),
        ),
        edges = listOf(
            DEdge("app", "notes", "save"),
            DEdge("notes", "fn", "change feed"),
            DEdge("fn", "aoai", "embed"),
            DEdge("fn", "leases", "bookmark", dashed = true),
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
            DNode("notes", "notes", 0.2f, 0.2f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "monitored"),
            DNode("leases", "leases", 0.8f, 0.2f, w = 0.32f, h = 0.28f, kind = NodeKind.CYLINDER, tone = 2, sub = "lease container"),
            DNode("c1", "Copy one", 0.25f, 0.62f, w = 0.4f, h = 0.2f, tone = 1, sub = "instance: one"),
            DNode("c2", "Copy two", 0.75f, 0.62f, w = 0.4f, h = 0.2f, tone = 1, sub = "instance: two"),
            DNode("deleg", "Delegate: your code", 0.5f, 0.88f, w = 0.7f, h = 0.16f, kind = NodeKind.NOTE, sub = "rebuilds the embedding"),
        ),
        edges = listOf(
            DEdge("notes", "c1"),
            DEdge("notes", "c2"),
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
            DNode("nolib", "No processor library", 0.5f, 0.8f, w = 0.7f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "in Python: two choices"),
        ),
        edges = listOf(
            DEdge("nolib", "trigger"),
            DEdge("nolib", "pull"),
        ),
        aspect = 0.8f,
    )

    private fun loop() = DiagramSpec(
        "Saving the embedding back is an update, so the feed wakes the function again; a hash of the text breaks the loop.",
        nodes = listOf(
            DNode("fn", "Embed function", 0.26f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "Cosmos DB trigger"),
            DNode("note", "The note", 0.76f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "embedding saved back"),
            DNode("feed", "Change feed", 0.76f, 0.75f, w = 0.42f, h = 0.22f, sub = "sees an update"),
            DNode("hash", "Check a hash", 0.26f, 0.75f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "text changed? then embed"),
        ),
        edges = listOf(
            DEdge("fn", "note", "save"),
            DEdge("note", "feed", "update"),
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
            DNode("shared", "Shared lease container", 0.76f, 0.7f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, tone = 2, sub = "only one function fires"),
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
            DNode("loops", "Function loops", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "check a hash"),
            DNode("late", "Start time ignored", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "leases already exist"),
        ),
        aspect = 0.8f,
    )


    private val all6 = setOf("deletes", "split", "three", "py", "loops", "late")
    private val triggerCode = BoardCard("Python", "@app.cosmos_db_trigger(arg_name=\"notes\", lease_container_name=\"leases\", ...)", code = true)
    private val today = BoardCard("Today", "Notes and leases containers, a trigger that logs ids, an edit, then a delete.")

    // The shared pictures, one card each.
    private val embeddingCard = BoardCard("Embedding: pins on a map", "Numbers that place a text on a map of meaning. An edit moves the pin.")
    private val drawerCard = BoardCard("Partition: a drawer", "The learner's id picks the drawer. Changes arrive in order within one drawer.")
    private val leaseCard = BoardCard("Lease: a bookmark", "Handled up to change 512? After a restart, carry on at 513.")
    private val libraryCard = BoardCard("Two copies, one box", "Same name and lease container, own instance name: one lease, one copy, like a book.")
    private val twiceCard = BoardCard("Safe to run twice", "Set the count to 120: safe to repeat. Add 20 to the count: not safe.")
    private val hashCard = BoardCard("Hash: a fingerprint", "Same text, same fingerprint. Change one letter, and it's a different one.")
    private val hashExampleCard = BoardCard("7F3A, still 7F3A: skip", "Saving the embedding leaves the text alone. A real edit: new fingerprint, re-embed.")

    private fun classicBoard() = PodcastBoard(
        "ai-cosmos-changefeed",
        listOf(
            // 0: "Hi, and welcome back. Today is about reacting when data changes..."
            Scene(
                0, "Reacting to changes", feed(),
                beats = listOf(
                    Beat(0, show = setOf("app", "notes"), light = setOf("notes"), pulse = "app" to "notes", card = BoardCard("Container", "Like one folder for one kind of item: here, the learners' notes.")),
                    Beat(2, show = setOf("aoai"), light = setOf("aoai"), card = embeddingCard),
                    Beat(6, show = setOf("fn"), light = setOf("notes", "fn"), pulse = "notes" to "fn", card = BoardCard("Change feed", "A running list of created and changed items, in order.")),
                ),
            ),
            // 8: "Lovely. And every container already has one..."
            Scene(
                8, "Two modes", modes(),
                beats = listOf(
                    Beat(8, show = setOf("latest", "all"), card = BoardCard("Pick a mode", "Every container has a change feed. You choose how to read it.")),
                    Beat(10, show = setOf("one"), light = setOf("latest", "one"), pulse = "latest" to "one", card = BoardCard("Latest version mode", "The default: creates and updates, the newest version of each item.")),
                    Beat(12, light = setOf("latest"), card = BoardCard("No deletes", "A deleted item just vanishes from the feed.")),
                    Beat(14, show = setOf("soft"), light = setOf("soft"), pulse = "latest" to "soft", card = BoardCard("Soft delete + TTL", "A deleted flag is an update the feed shows; a TTL timer removes it later.")),
                    Beat(16, show = setOf("every"), light = setOf("all", "every"), pulse = "all" to "every", card = BoardCard("All versions and deletes", "Every change, deletes too. Needs continuous backup.")),
                ),
            ),
            // 17: "Okay, so there's a list. Who actually reads it?"
            Scene(
                17, "Who reads it", processor(),
                beats = listOf(
                    Beat(18, show = setOf("notes", "c1"), light = setOf("c1"), pulse = "notes" to "c1", card = BoardCard("Change feed processor", "A library for .NET and Java that reads the feed and keeps its place.")),
                    Beat(20, show = setOf("leases"), light = setOf("leases"), pulse = "c1" to "leases", card = leaseCard),
                    Beat(22, light = setOf("notes"), card = drawerCard),
                    Beat(24, show = setOf("c2"), light = setOf("c1", "c2"), pulse = "c2" to "leases", card = libraryCard),
                    Beat(26, show = setOf("deleg"), light = setOf("deleg"), card = BoardCard("At least once", "The bookmark moves after a batch succeeds. A change can come twice.")),
                    Beat(28, light = setOf("deleg"), card = twiceCard),
                ),
            ),
            // 29: "I'm writing Python, though. You said dot NET and Java."
            Scene(
                29, "Python's two ways", python(),
                beats = listOf(
                    Beat(30, show = setOf("nolib", "trigger"), light = setOf("trigger"), pulse = "nolib" to "trigger", card = triggerCode),
                    Beat(32, show = setOf("pull"), light = setOf("pull"), pulse = "nolib" to "pull", card = BoardCard("Continuation token", "The pull model's bookmark, saved by your code. Good for a one-off job.")),
                ),
            ),
            // 33: "Any traps with the trigger?"
            Scene(
                33, "The loop", loop(),
                beats = listOf(
                    Beat(34, show = setOf("fn", "note", "feed"), light = setOf("note", "feed"), pulse = "note" to "feed", card = BoardCard("Saving is an update", "So the function wakes itself, again and again.")),
                    Beat(36, show = setOf("hash"), light = setOf("hash"), pulse = "hash" to "fn", card = hashCard),
                    Beat(38, light = setOf("hash", "fn"), card = hashExampleCard),
                ),
            ),
            // 39: "So what do I remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("deletes", "split", "three", "py"), card = BoardCard("Listen for the phrase", "Deletes: soft delete. Split work: instance name. Python: trigger or pull.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-cosmos-changefeed--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Note That Wouldn't Die..."
            Scene(
                0, "The Note That Wouldn't Die",
                DiagramSpec(
                    "Margin Notes' smart search keeps finding notes that students deleted last week.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.18f, w = 0.38f, h = 0.2f, sub = "Margin Notes"),
                        DNode("search", "Smart search", 0.22f, 0.72f, w = 0.38f, h = 0.2f, tone = 1, sub = "by meaning"),
                        DNode("ghost", "Deleted note", 0.76f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "still in results"),
                        DNode("db", "Cosmos DB", 0.76f, 0.72f, w = 0.4f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "notes container"),
                    ),
                    edges = listOf(
                        DEdge("phone", "search", "asks"),
                        DEdge("search", "ghost", "finds", dashed = true),
                        DEdge("db", "search", "embeddings"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "search", "ghost"), light = setOf("ghost"), pulse = "search" to "ghost", card = BoardCard("The case", "Notes deleted last week still turn up in the smart search.")),
                    Beat(2, show = setOf("db"), light = setOf("db"), pulse = "db" to "search", card = embeddingCard),
                ),
            ),
            // 3: "So when a note changes, its pin has to move."
            Scene(
                3, "The change feed", feed(),
                beats = listOf(
                    Beat(4, show = setOf("app", "notes", "fn"), light = setOf("notes", "fn"), pulse = "notes" to "fn", card = BoardCard("Change feed", "A running list of created and changed items, like a visitors' book.")),
                    Beat(6, light = setOf("notes"), card = drawerCard),
                ),
            ),
            // 7: "Then my first suspect: the change feed is switched off..."
            Scene(
                7, "The suspects", modes(),
                beats = listOf(
                    Beat(7, show = setOf("latest"), light = setOf("latest"), card = BoardCard("Suspect one: switched off", "Every container already has a change feed. You choose a mode.")),
                    Beat(8, card = BoardCard("Latest version mode", "The default: creates and updates, the newest version of each item.")),
                    Beat(10, light = setOf("latest"), card = BoardCard("No deletes", "A deleted note simply vanishes from the feed. The ghost stays.")),
                    Beat(11, card = BoardCard("Suspect: replay from the start", "Replays creates and updates. Deletes still never appear.")),
                    Beat(12, show = setOf("soft"), light = setOf("soft"), pulse = "latest" to "soft", card = BoardCard("Soft delete + TTL", "A deleted flag is an update the feed shows; a TTL timer removes it later.")),
                    Beat(14, show = setOf("all", "every"), light = setOf("all", "every"), pulse = "all" to "every", card = BoardCard("All versions and deletes", "Every change, deletes too. Needs continuous backup.")),
                    Beat(16, show = setOf("one"), light = setOf("one"), pulse = "latest" to "one", card = BoardCard("One entry", "Edited twice before the read: only the note as it is now.")),
                ),
            ),
            // 17: "Ghosts handled. Case closed?"
            Scene(
                17, "The second witness", loop(),
                beats = listOf(
                    Beat(18, show = setOf("fn"), light = setOf("fn"), card = BoardCard("Millions of runs", "More runs than there are notes.")),
                    Beat(20, card = BoardCard("Azure Functions", "Code Azure runs when something happens, like a doorbell.")),
                    Beat(22, show = setOf("note"), light = setOf("note"), pulse = "fn" to "note", card = BoardCard("Saved back", "The new embedding is saved onto the note: that's an update.")),
                    Beat(23, show = setOf("feed"), light = setOf("feed"), pulse = "note" to "feed", card = BoardCard("Saving is an update", "So the feed wakes the function again. Forever.")),
                    Beat(24, show = setOf("hash"), light = setOf("hash"), pulse = "hash" to "fn", card = hashCard),
                    Beat(26, light = setOf("hash", "fn"), card = hashExampleCard),
                ),
            ),
            // 27: "Any more clues in the file?"
            Scene(
                27, "The third clue", processor(),
                beats = listOf(
                    Beat(28, show = setOf("notes", "c1", "c2"), light = setOf("c2"), card = BoardCard("A second function", "Counting words on the same container. It never fires.")),
                    Beat(30, show = setOf("leases"), light = setOf("leases"), pulse = "c1" to "leases", card = leaseCard),
                    Beat(32, light = setOf("c1", "c2"), pulse = "c2" to "leases", card = libraryCard),
                    Beat(34, show = setOf("deleg"), light = setOf("deleg"), card = twiceCard),
                ),
            ),
            // 35: "Any loose ends? Margin Notes is written in Python, isn't it?"
            Scene(
                35, "Loose ends", python(),
                beats = listOf(
                    Beat(36, light = setOf("nolib", "pull"), pulse = "nolib" to "pull", card = BoardCard("Python", "No processor library: the Functions trigger, or the pull model.")),
                ),
            ),
            // 37: "So, the case summary for the exam?"
            Scene(
                37, "Case closed", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("deletes", "loops", "split", "py"), card = BoardCard("Listen for the phrase", "Deletes: soft delete. Loops: a hash. Split work: instance name.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-cosmos-changefeed--gossip",
        listOf(
            // 0: "Okay, did you hear about the Coach team's change feed week?..."
            Scene(
                0, "What the Coach team wanted", feed(),
                beats = listOf(
                    Beat(0, show = setOf("app", "notes"), light = setOf("notes"), card = BoardCard("Re-embed on change", "Notes re-embedded the moment they change.")),
                    Beat(2, show = setOf("aoai"), light = setOf("aoai"), card = embeddingCard),
                    Beat(4, show = setOf("fn"), light = setOf("notes", "fn"), pulse = "notes" to "fn", card = BoardCard("Change feed", "Created and changed items, in order, like a visitors' book.")),
                ),
            ),
            // 5: "Okay. So who went first?"
            Scene(
                5, "Monday: Tom", modes(),
                beats = listOf(
                    Beat(6, show = setOf("latest"), light = setOf("latest"), card = BoardCard("Tom deleted a test note", "And waited. Nothing came.")),
                    Beat(8, card = BoardCard("Latest version mode", "Creates and updates only. A deleted item just vanishes.")),
                    Beat(10, show = setOf("soft", "all", "every"), light = setOf("soft"), pulse = "latest" to "soft", card = BoardCard("Soft delete + TTL", "Or all versions and deletes mode, with continuous backup.")),
                ),
            ),
            // 11: "And Tuesday?"
            Scene(
                11, "Tuesday: Dana", loop(),
                beats = listOf(
                    Beat(12, show = setOf("fn", "note"), light = setOf("fn"), pulse = "fn" to "note", card = BoardCard("Azure Functions", "Code Azure runs when something happens, like a doorbell.")),
                    Beat(14, show = setOf("feed"), light = setOf("feed"), pulse = "note" to "feed", card = BoardCard("80,000 runs, 12 notes", "Saving back is an update, so the function woke itself.")),
                    Beat(16, show = setOf("hash"), light = setOf("hash"), pulse = "hash" to "fn", card = hashCard),
                    Beat(18, light = setOf("hash", "fn"), card = hashExampleCard),
                ),
            ),
            // 19: "Wednesday?"
            Scene(
                19, "Wednesday: Marcus", processor(),
                beats = listOf(
                    Beat(20, show = setOf("notes", "c1", "c2"), light = setOf("c2"), card = BoardCard("A second function", "Counting words on the same container. It never fired.")),
                    Beat(22, show = setOf("leases"), light = setOf("leases"), pulse = "c2" to "leases", card = leaseCard),
                    Beat(24, light = setOf("leases"), card = BoardCard("One box of bookmarks", "Same lease container, same prefix: only one function wins.")),
                    Beat(26, light = setOf("c1", "c2"), pulse = "c1" to "leases", card = libraryCard),
                ),
            ),
            // 27: "Thursday. I can feel it."
            Scene(
                27, "Thursday: the intern", processor(),
                beats = listOf(
                    Beat(28, light = setOf("c1"), card = BoardCard("Counts too high", "They blamed the database.")),
                    Beat(30, light = setOf("deleg"), card = twiceCard),
                    Beat(32, light = setOf("notes"), card = drawerCard),
                ),
            ),
            // 33: "And Friday? There's always a Friday."
            Scene(
                33, "Friday: Priya", python(),
                beats = listOf(
                    Beat(34, light = setOf("trigger"), card = BoardCard("Start time: the beginning", "Leases already existed, so nothing replayed.")),
                    Beat(36, light = setOf("pull"), pulse = "nolib" to "pull", card = BoardCard("A pull model script", "Reads the feed from the beginning and saves a continuation token.")),
                    Beat(38, light = setOf("nolib"), card = BoardCard("No processor in Python", "That's .NET and Java. Python: a Functions trigger or the pull model.")),
                ),
            ),
            // 39: "So, the gossip I should remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("deletes", "split", "three", "py"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Nobody wants 80,000 runs.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-cosmos-changefeed--late-night",
        listOf(
            // 0: "Hey, you. It's late, the street is quiet..."
            Scene(
                0, "Noticing changes", feed(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Noticing when something changes.")),
                    Beat(2, show = setOf("app", "notes"), light = setOf("notes"), pulse = "app" to "notes", card = BoardCard("Container", "A folder for one kind of item: here, the notes.")),
                    Beat(4, show = setOf("aoai"), light = setOf("aoai"), card = embeddingCard),
                    Beat(6, show = setOf("fn"), light = setOf("notes", "fn"), pulse = "notes" to "fn", card = BoardCard("Change feed", "A running list of created and changed items, in order.")),
                    Beat(8, light = setOf("notes"), card = drawerCard),
                ),
            ),
            // 11: "Tell me the modes. Slowly."
            Scene(
                11, "Two modes", modes(),
                beats = listOf(
                    Beat(12, show = setOf("latest", "one"), light = setOf("latest", "one"), pulse = "latest" to "one", card = BoardCard("Latest version mode", "The default: creates and updates, the newest version of each item.")),
                    Beat(14, show = setOf("soft"), light = setOf("soft"), pulse = "latest" to "soft", card = BoardCard("Soft delete + TTL", "Deletes vanish, so flag the note, then let a TTL timer remove it.")),
                    Beat(16, show = setOf("all", "every"), light = setOf("all", "every"), pulse = "all" to "every", card = BoardCard("All versions and deletes", "Every change, deletes too. Needs continuous backup.")),
                    Beat(18, light = setOf("latest", "all"), card = BoardCard("Where it can start", "Latest: beginning, a time, now, a bookmark. All versions: now or a bookmark.")),
                ),
            ),
            // 19: "And who reads all this, while we sleep?"
            Scene(
                19, "Who reads it", processor(),
                beats = listOf(
                    Beat(20, show = setOf("notes", "c1", "leases"), light = setOf("leases"), pulse = "c1" to "leases", card = leaseCard),
                    Beat(22, show = setOf("deleg"), light = setOf("deleg"), card = twiceCard),
                    Beat(24, show = setOf("c2"), light = setOf("c1", "c2"), pulse = "c2" to "leases", card = libraryCard),
                    Beat(26, light = setOf("leases"), card = BoardCard("Two different jobs", "Each its own lease container or prefix, or only one gets changes.")),
                ),
            ),
            // 27: "But the Coach is Python."
            Scene(
                27, "In Python", python(),
                beats = listOf(
                    Beat(28, light = setOf("trigger", "pull"), pulse = "nolib" to "trigger", card = triggerCode),
                ),
            ),
            // 29: "Any trap waiting in the dark?"
            Scene(
                29, "A trap in the dark", loop(),
                beats = listOf(
                    Beat(30, light = setOf("hash"), pulse = "note" to "feed", card = hashCard),
                    Beat(32, light = setOf("hash", "fn"), card = hashExampleCard),
                    Beat(34, light = setOf("fn"), card = BoardCard("Start time", "Counts only the first time. Once leases exist, it resumes from them.")),
                ),
            ),
            // 35: "Mm. Before I drift off. What do I remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("deletes", "split", "three", "py"), card = BoardCard("Listen for the phrase", "Deletes: soft delete. Split work: instance name. Python: trigger or pull.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-cosmos-changefeed--couples",
        listOf(
            // 0: "Tonight is date night, darling. And tonight, Rafe, we're talking about change."
            Scene(
                0, "Date night: change", feed(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Change: the Cosmos DB change feed notices every one.")),
                    Beat(2, show = setOf("app", "notes"), light = setOf("notes"), pulse = "app" to "notes", card = BoardCard("Container", "Like a folder for one kind of item: here, the notes.")),
                    Beat(4, show = setOf("aoai"), light = setOf("aoai"), card = embeddingCard),
                    Beat(6, show = setOf("fn"), light = setOf("notes", "fn"), pulse = "notes" to "fn", card = BoardCard("Change feed", "Created and changed items, in order. Every container has one.")),
                    Beat(8, light = setOf("notes"), card = drawerCard),
                ),
            ),
            // 9: "Then let me test you, darling. Latest version mode..."
            Scene(
                9, "Quiz: the modes", modes(),
                beats = listOf(
                    Beat(9, show = setOf("latest"), light = setOf("latest"), card = BoardCard("Quiz: edited twice", "Created, then edited twice before the reader runs. What arrives?")),
                    Beat(10, show = setOf("one"), light = setOf("one"), pulse = "latest" to "one", card = BoardCard("Right answer: one entry", "Only the newest version of each changed item.")),
                    Beat(12, light = setOf("latest"), card = BoardCard("Quiz: a deleted note", "In latest version mode, what does the feed show?")),
                    Beat(14, card = BoardCard("No kiss: nothing at all", "A deleted item just vanishes from the feed.")),
                    Beat(15, show = setOf("soft", "all", "every"), light = setOf("soft"), pulse = "latest" to "soft", card = BoardCard("Soft delete + TTL", "Or all versions and deletes mode, with continuous backup.")),
                ),
            ),
            // 17: "Now, who reads the feed?..."
            Scene(
                17, "Someone who remembers", processor(),
                beats = listOf(
                    Beat(18, show = setOf("notes", "c1", "leases"), light = setOf("leases"), pulse = "c1" to "leases", card = leaseCard),
                    Beat(20, show = setOf("deleg"), light = setOf("deleg"), card = twiceCard),
                    Beat(22, show = setOf("c2"), light = setOf("c1", "c2"), card = BoardCard("Quiz: split the work", "Two copies, one container. What must differ?")),
                    Beat(23, light = setOf("c2"), pulse = "c2" to "leases", card = libraryCard),
                ),
            ),
            // 25: "And the Coach is Python. Does the processor speak Python?"
            Scene(
                25, "Python", python(),
                beats = listOf(
                    Beat(26, light = setOf("trigger", "pull"), pulse = "nolib" to "trigger", card = BoardCard("No processor in Python", "A Functions trigger, or the pull model with a continuation token.")),
                ),
            ),
            // 27: "My question, then. The function makes a new embedding..."
            Scene(
                27, "Quiz: forever", loop(),
                beats = listOf(
                    Beat(27, light = setOf("fn", "note"), pulse = "fn" to "note", card = BoardCard("Quiz: it runs forever", "The function saves the embedding onto the note. Why the loop?")),
                    Beat(28, light = setOf("hash"), pulse = "hash" to "fn", card = hashExampleCard),
                ),
            ),
            // 30: "Behave. My question. A processor already has leases..."
            Scene(
                30, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(30, light = setOf("start"), card = BoardCard("Quiz: a late start time", "Leases exist; start time set to the beginning. What replays?")),
                    Beat(32, card = BoardCard("No kiss: nothing replays", "The start time only counts the first time.")),
                    Beat(33, light = setOf("del", "edits", "shared"), card = BoardCard("Where it stops", "No deletes, no in-between edits, and shared leases fight.")),
                ),
            ),
            // 34: "Last question, for the exam. What do we remember?"
            Scene(
                34, "For the exam", exam(),
                beats = listOf(
                    Beat(35, show = all6, light = setOf("deletes", "split", "three", "py"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Four for four.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
