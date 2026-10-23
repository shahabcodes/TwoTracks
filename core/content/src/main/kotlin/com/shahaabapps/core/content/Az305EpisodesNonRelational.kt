package com.shahaabapps.core.content

/** Semi-structured and unstructured data: Cosmos DB and Storage: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesNonRelational {
    private val classic = Podcast(
        "sa-nonrelational",
        listOf(
            maya("Hi, and welcome back. Today is the data that doesn't fit neatly in tables: Cosmos DB and Azure Storage. Our story is Two Tracks goes global. The Android study app is a company now, with a backend on Azure, learners in Europe and Australia, and a regulator who wants records kept for years."),
            leo("That's a lot of stuff to keep. What kind of data are we talking about?"),
            maya("Two kinds. Semi-structured data has a loose shape, such as JSON documents: text records of names and values, where each item may carry different fields. One lesson note has a quiz, the next has an audio link. Unstructured data has no shape a database understands at all: photos, audio, PDFs, logs."),
            leo("And each kind has its own home?"),
            maya("Yes. Cosmos DB for the first. A storage account, Azure's general store for files, for the second. The design work is trading features, speed and price, then keeping the data safe. Let's start with Cosmos DB."),
            leo("Cosmos DB, in plain words?"),
            maya("A NoSQL database. That means it stores whole items, like a JSON note, rather than rows in fixed tables."),
            leo("So the JSON my app already sends to my backend, I could save as it is? No Room-style table with fixed columns?"),
            maya("As it is, fields and all. And Cosmos DB can copy it to any Azure region in a few clicks. A region is one group of Azure buildings in one part of the world, like West Europe or Australia East."),
            leo("Why copy them?"),
            maya("For latency, which is the wait for each answer, like the gap between asking a question and hearing the reply. A learner in Sydney reading from a copy in Australia waits far less than one reading from Amsterdam. So the Coach's lesson notes go in Cosmos DB with both regions added."),
            leo("And how do I pay for it?"),
            maya("In RUs, request units. A request unit is the price tag on every read and write you make to the database. A small read costs about one; a big query costs more. Your bill follows how many you use."),
            leo("I've heard Cosmos DB speaks several languages."),
            maya("It does. An API is the language your code uses to talk to a service, like the plug that fits a socket. For a new app storing JSON, the NoSQL API, with the richest features. For example, if the team already uses MongoDB tools, the MongoDB API, so their code barely changes."),
            leo("Any others?"),
            maya("Apache Cassandra, for an existing Cassandra app. Apache Gremlin, for graph data. A graph is dots joined by lines, like a map of who knows whom: friends, follows, recommendations. And Table, for key-value data that has outgrown Azure Table storage."),
            leo("Key-value. Explain that."),
            maya("You look each item up by its key, like a cloakroom ticket: hand over the ticket, get the coat. Azure Table storage, inside a storage account, does that cheaply, in one region. Move to Cosmos DB for Table when you need quick answers worldwide."),
            leo("Now, copies in two regions. If I write in Europe, does Australia see it straight away?"),
            maya("That's consistency: how fresh a read is after a write somewhere else. Strong means every read sees the newest write, at the cost of waiting."),
            leo("Waiting for what? My users won't sit and stare at a spinner."),
            maya("For the far copy to confirm. So there's eventual: copies catch up a little later. And session, the default, sits between: each user always sees their own writes. Save a note on your phone, reload, and it's there."),
            leo("Right. Now the files. Photos, audio."),
            maya("Three choices in a storage account. Blob Storage, for files your app fetches over the web; a blob is one file, kept in a container, like a box on a shelf."),
            leo("So my app uploads a profile photo, and gets back an address it can load from later?"),
            maya("Exactly, that's Blob. Data Lake Storage Gen2 is for analytics over big files in folders. And Azure Files is a shared network drive that office servers connect to, like a mapped drive letter."),
            leo("What makes the Data Lake one different?"),
            maya("A switch called the hierarchical namespace. It makes folders real, like a filing cabinet instead of one huge drawer of labelled papers. Renaming a folder becomes one quick step, and you can set permissions on each folder, which analytics tools need."),
            leo("Okay. Now, cost. Audio piles up fast."),
            maya("That's the access tier: hot, cool, cold or archive. Hot costs the most to keep and the least to read. Cool and cold are cheaper to keep, dearer to read, still instant. Cool has a 30 day minimum stay, cold 90. Leave early, and you pay an early deletion charge."),
            leo("And archive?"),
            maya("[pause] Cheapest of all, but offline, like a box in a warehouse. Before you read it, the blob must be rehydrated, copied back to an online tier, which can take up to 15 hours. Minimum stay, 180 days. Picture a compliance record, written once, opened maybe never: archive."),
            leo("Who moves files between tiers? Not me, I hope."),
            maya("Lifecycle management. Rules that move or delete blobs by age, so nobody has to remember. Say your app keeps practice audio: hot for a month, then cool, deleted after two years. The rule runs once a day."),
            leo("And if a whole building burns down?"),
            maya("Azure always keeps at least three copies; redundancy decides where. LRS, locally redundant, keeps three in one datacenter."),
            leo("One building? So one fire, and all three are gone."),
            maya("Right. ZRS, zone-redundant, spreads them across availability zones: separate buildings in one region, each with its own power and cooling."),
            leo("And a whole region?"),
            maya("GRS, geo-redundant, adds copies in the paired region, a second region far away, like North Europe for West Europe. GZRS is zones at home plus the paired region. Put RA in front, for read access, and you can read the far copy any time, not only after failover, the switch to that region."),
            leo("Any catch?"),
            maya("One. Archive works only with LRS, GRS or RA-GRS, not with ZRS or GZRS."),
            leo("Last part. Keeping files safe from people."),
            maya("Soft delete is a recycle bin for blobs and containers, kept 1 to 365 days. Versioning keeps every earlier version when a file is overwritten."),
            leo("Like git history, for my files."),
            maya("Close. And immutable storage is WORM, write once, read many: like a sealed evidence bag. A time-based retention policy seals data for a set period, even from admins."),
            leo("So exam results a regulator wants unchanged for seven years?"),
            maya("Immutable storage with a time-based retention policy. Soft delete won't do: it stops at 365 days and can be emptied. And a legal hold seals data until someone lifts it."),
            leo("So what do I remember for the exam?"),
            maya("Five things. JSON with different fields, read fast on two continents: Cosmos DB for NoSQL with both regions. Rarely read, kept for years, cheapest: the archive tier. Survive a zone and a region, read the far copy without waiting: RA-GZRS. Real folders for analytics: hierarchical namespace. Unchangeable for years: an immutability policy."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create a general-purpose v2 account and upload a file to a container. Change the file's tier to cool, then to archive, and try to download it. Delete a blob and bring it back from soft delete. Then add a lifecycle rule and read its JSON in the portal's code view."),
            leo("A warehouse, a recycle bin and an evidence bag. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-nonrelational",
        listOf(
            maya("Welcome to Case Files. Today's case: The Recording That Wouldn't Play. Two Tracks has gone global: the Android study app is a company now, with a backend on Azure and learners in Europe and Australia. On Monday, a learner tapped an old speaking recording to replay it. The app spun. And spun. For hours."),
            leo("Detective Leo, on the case. The file's been deleted."),
            maya("[pause] Wrong suspect. The file was there. First, the scene. The recordings are unstructured data, files with no shape a database understands. They live in Blob Storage, inside a storage account, Azure's general store for files. A blob is one file, kept in a container, like a box on a shelf."),
            leo("Then my next suspect is the network. Slow phone, slow Wi-Fi."),
            maya("Also wrong. Clue one: last month the team added a lifecycle rule. Lifecycle management means rules that move or delete blobs by age, so nobody has to remember. Their rule moved every recording to the archive tier after 30 days."),
            leo("The archive tier being?"),
            maya("One of four access tiers, which trade the price of keeping a file against the price of reading it. Hot is for daily use: dear to keep, cheap to read."),
            leo("So the cheaper the shelf, the dearer the fetch?"),
            maya("Exactly. Cool and cold are cheaper to keep, dearer to read, still instant. Archive is the cheapest of all, but offline, like a box in a warehouse."),
            leo("Offline. So you can't just open it."),
            maya("Now you're detecting. An archived blob must be rehydrated first: copied back to an online tier. That can take up to 15 hours. How it works: archived data sits on cheap offline storage, and Azure has to fetch it back before anyone can read a byte. The learner was waiting on a warehouse."),
            leo("So what should the rule have done?"),
            maya("Practice audio is played often in the first month, rarely after. So hot, then cool after 30 days, deleted after two years. Cool stays instant."),
            leo("And archive? Never touch it?"),
            maya("For things almost never opened, like old compliance records. And mind the minimum stays: cool 30 days, cold 90, archive 180. Leave early, and you pay an early deletion charge."),
            leo("Case one solved. Is there a second witness?"),
            maya("When the team tried to archive the compliance records, the portal refused. Their account used ZRS."),
            leo("Suspect: the portal is broken."),
            maya("[chuckle] Never the portal. Azure always keeps at least three copies of your data; redundancy decides where. LRS, locally redundant, keeps three in one datacenter. ZRS, zone-redundant, spreads them across availability zones: separate buildings in one region, each with its own power and cooling."),
            leo("And ZRS can't archive?"),
            maya("Archive works only with LRS, GRS or RA-GRS. Not ZRS, not GZRS."),
            leo("GRS being the far-away copy?"),
            maya("Geo-redundant: copies at home plus in the paired region, a second region far away, like North Europe for West Europe. A region is one group of Azure buildings in one part of the world. So the records moved to a separate GRS account."),
            leo("Third witness?"),
            maya("The outage drill. The team promised the app would read profile photos from the second region during an outage, without waiting. They had GRS. The reads failed until failover, the switch to the second region."),
            leo("Suspect: the second copy didn't exist."),
            maya("It existed. They just couldn't read it. Plain GRS gives no reads from the far region until failover. Put RA in front, for read access, and you can read it any time. To survive a zone and a region and read without waiting: RA-GZRS, geo-zone-redundant with read access."),
            leo("Fourth. I can feel a regulator."),
            maya("Exam results must stay unchanged for seven years. The team turned on soft delete, a recycle bin for blobs, and set it to 365 days."),
            leo("Suspect: they set the bin too short. Make it seven years."),
            maya("Can't. Soft delete stops at 365 days, and a bin can be emptied. Versioning, which keeps earlier versions of a file, can be deleted too. The requirement said unchangeable, even by admins. That's immutable storage: WORM, write once, read many, like a sealed evidence bag. A time-based retention policy seals it for seven years."),
            leo("Last witness. The lesson notes."),
            maya("The Coach's lesson notes are JSON: text records of names and values, each lesson with different fields. Semi-structured data. The team put them in Azure Table storage, which looks each item up by its key, like a cloakroom ticket. Australian learners said it was slow."),
            leo("Suspect: too many lessons."),
            maya("Too few regions. Table storage is cheap, but one region. The fix is Cosmos DB, a NoSQL database: it stores whole items rather than rows in fixed tables, and copies them to any region. Add West Europe and Australia East, and each learner reads close to home. That cuts latency, the wait for each answer."),
            leo("And Cosmos DB charges how?"),
            maya("In RUs, request units. A request unit is the price tag on every read and write. And it speaks several APIs. An API is the language your code uses to talk to a service. For new JSON, the NoSQL API. A team already on MongoDB keeps the MongoDB API."),
            leo("And with copies on two continents, does Sydney see a write from Amsterdam straight away?"),
            maya("That's consistency: how fresh a read is. Strong means always the newest, at the cost of waiting. Eventual means copies catch up later."),
            leo("And my learner who saves a note, then reloads? It can't just vanish."),
            maya("Session, the default: each user always sees their own writes. Where does all this stop? Files an office server mounts as a drive go to Azure Files, not Blob. And the high availability episode goes deeper on regions."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. Rarely read, kept for years, cheapest: archive, and expect hours to rehydrate. Archive needs LRS, GRS or RA-GRS. Read the far region without waiting: RA in front. Unchangeable even by admins: an immutability policy, not soft delete. JSON read fast on two continents: Cosmos DB for NoSQL with both regions."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, safely. Create a general-purpose v2 account and upload a file to a container. Change the file's tier to cool, then to archive, and try to download it. Delete a blob and bring it back from soft delete. Add a lifecycle rule and read its JSON in the portal's code view."),
            leo("No more recordings stuck in the warehouse. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-nonrelational",
        listOf(
            maya("Okay, did you hear what the storage team did this week? Sit down. Two Tracks has gone global: the Android study app is a company now, with a backend on Azure and learners in Europe and Australia. This week the team moved everything that isn't a neat table. And it was a week."),
            leo("[laugh] I heard the word warehouse. What's everything that isn't a neat table?"),
            maya("Two kinds. Semi-structured data has a loose shape, like JSON documents: text records of names and values, where each item may carry different fields. Unstructured data has no shape a database understands: photos, audio, PDFs, logs."),
            leo("And they each get a home?"),
            maya("Cosmos DB for the first. It's a NoSQL database: it stores whole items rather than rows in fixed tables, and it copies them to any Azure region. A region is one group of Azure buildings in one part of the world. And for the second, a storage account, Azure's general store for files."),
            leo("So who went first?"),
            maya("Tom. Monday. Tom put every profile photo and audio file on Azure Files, because it looked like a normal drive."),
            leo("Is that bad? A drive's a drive."),
            maya("Azure Files is a shared network drive that office servers connect to, great for the old office share without code changes. But app media fetched over the web belongs in Blob Storage: cheaper at scale, built for it. A blob is one file, kept in a container, like a box on a shelf. Tom moved it all by Tuesday."),
            leo("Tuesday?"),
            maya("Dana. Dana set up the media account with ZRS. Then she tried to put the compliance records in the archive tier, and the portal said no."),
            leo("Wait. ZRS? Archive? One at a time."),
            maya("Azure always keeps at least three copies; redundancy decides where. LRS, locally redundant, is three in one datacenter. ZRS, zone-redundant, spreads them across availability zones: separate buildings in one region, with their own power."),
            leo("And if the whole region goes?"),
            maya("GRS, geo-redundant, adds copies in the paired region, a second region far away."),
            leo("And archive?"),
            maya("An access tier. Hot is dear to keep, cheap to read. Cool and cold are cheaper to keep, dearer to read, still instant. Archive is cheapest of all, but offline, like a box in a warehouse."),
            leo("And Dana's ZRS account couldn't use it?"),
            maya("No. Archive works only with LRS, GRS or RA-GRS. Not ZRS. Dana made a second account, on GRS, just for records."),
            leo("Wednesday?"),
            maya("Marcus. Marcus promised the boss that in a European outage, the app would read photos from the second region straight away. He had GRS."),
            leo("Let me guess. It didn't."),
            maya("Plain GRS can't be read from the far region until failover, the switch to that region, which takes time. Put RA in front, for read access, and the far copy is readable any time. He needed RA-GZRS: zones at home plus the paired region, with read access. The boss found out in the drill."),
            leo("[sigh] Poor Marcus. Thursday, I'm guessing, the intern."),
            maya("The intern. Bless them. They wrote a lifecycle rule. Lifecycle management means rules that move or delete blobs by age, so nobody has to remember. Theirs moved practice audio to archive after 30 days. Then a learner tapped an old recording, and waited."),
            leo("For how long?"),
            maya("Up to 15 hours. An archived blob must be rehydrated, copied back to an online tier, before anyone can read it. Under the hood, archived data sits on cheap offline storage, and Azure must fetch it back first. Practice audio should go hot, then cool after 30 days, then be deleted after two years."),
            leo("And moving it back early? Free?"),
            maya("No. Cool has a 30 day minimum stay, cold 90, archive 180. Leave early and you pay an early deletion charge. The intern's rule cost money twice. They've rewritten it."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. A regulator wants exam results unchanged for seven years. Tom said, easy, turn on soft delete. Soft delete is a recycle bin for blobs, and versioning keeps every earlier version of a file."),
            leo("That sounds safe."),
            maya("Safe from mistakes, not from people. Soft delete stops at 365 days, and a bin can be emptied. Priya picked immutable storage: WORM, write once, read many, like a sealed evidence bag. A time-based retention policy seals it for seven years, and nobody, admins included, can change or delete it."),
            leo("And the lesson notes? The JSON?"),
            maya("Priya again. Cosmos DB for NoSQL, with West Europe and Australia East added, so each learner reads close by. That cuts latency, the wait for each answer. You pay in RUs, request units: a request unit is the price tag on every read and write."),
            leo("And it only speaks NoSQL?"),
            maya("It speaks several APIs. An API is the language your code uses to talk to a service. A team on MongoDB keeps the MongoDB API. A graph, which is dots joined by lines like followers, gets the Gremlin API."),
            leo("And if a learner in Sydney reads straight after someone in Amsterdam writes?"),
            maya("That's consistency: how fresh a read is after a write somewhere else. Strong means always the newest, at the cost of waiting. Eventual means the copies catch up a little later. Session, the default, means each user always sees their own writes."),
            leo("So where does all this stop working?"),
            maya("At analytics. For big files in folders, switch on the hierarchical namespace, which makes folders real, like a filing cabinet instead of one huge drawer. That's Data Lake Storage Gen2. The data integration episode takes it from there, and the high availability episode goes deeper on regions."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. App media: Blob Storage; an office share without code changes: Azure Files. Rarely read, kept for years: archive, never on ZRS. Read the far region without waiting: RA in front. Unchangeable even by admins: an immutability policy. JSON read fast on two continents: Cosmos DB for NoSQL with both regions."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a general-purpose v2 account and upload a file to a container. Change the file's tier to cool, then to archive, and try to download it. Delete a blob and bring it back from soft delete. Then add a lifecycle rule and read its JSON in the portal's code view."),
            leo("Hot, cool, warehouse, evidence bag. Got it, Priya. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-nonrelational",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about the things we keep, and how long we keep them."),
            ivy("Mm. The things we keep. Photos? Old voice notes?"),
            nova("Exactly those. Two Tracks, the Android study app, has gone global, with a backend on Azure and learners in Europe and Australia. Its photos and audio are unstructured data: files with no shape a database understands. Its lesson notes are semi-structured: JSON, text records of names and values, each one a little different."),
            ivy("Different homes for each, I suppose."),
            nova("Cosmos DB for the notes. It's a NoSQL database, which means it stores whole items rather than rows in fixed tables. And it copies them to any region, one group of Azure buildings in one part of the world. West Europe, and Australia East, for the night owls in Sydney."),
            ivy("Why copy them so far?"),
            nova("For latency. That's the wait between asking and hearing back, [pause] like waiting for a reply to a late text. A learner in Sydney reading from Australia waits far less."),
            ivy("And what does all that copying cost me?"),
            nova("Cosmos DB charges in RUs, request units. A request unit is the price tag on every read and write. A small read costs about one; a heavy query, more."),
            ivy("Mm. And if someone in Amsterdam writes, and I read in Sydney a heartbeat later?"),
            nova("That's consistency: how fresh your read is after a write somewhere else. Strong means you always see the newest, but you wait for it. Eventual means the copies catch up a little later. Session, the default, means each person always sees their own writes. [pause] Nobody forgets what they said."),
            ivy("And does Cosmos DB only speak one language?"),
            nova("Several APIs. An API is the language your code uses to talk to a service. NoSQL for new JSON apps. MongoDB, for a team already using it. Gremlin, for graph data."),
            ivy("Graph?"),
            nova("Dots joined by lines, like a map of who knows whom. Friends, followers, recommendations. And for simple key-value data there's Azure Table storage: look each item up by its key, like a cloakroom ticket. Cheap, but in one region."),
            ivy("Now the photos and the old voice notes."),
            nova("They go in a storage account, Azure's general store for files. Blob Storage for files the app fetches over the web; a blob is one file, in a container, like a box on a shelf."),
            ivy("And the old office's shared drive?"),
            nova("Azure Files, a shared network drive for office servers. And Data Lake Storage Gen2 is for analytics over big folders."),
            ivy("What makes the lake special?"),
            nova("The hierarchical namespace. A switch that makes folders real, like a filing cabinet instead of one huge drawer of papers. Renaming a folder becomes one quick step."),
            ivy("And all those voice notes, piling up. Isn't that expensive?"),
            nova("That's what access tiers are for. Hot is dear to keep and cheap to read. Cool and cold are cheaper to keep, dearer to read, still instant. And archive. [pause] Cheapest of all, but offline, like a box in a warehouse."),
            ivy("Offline. So I can't just listen."),
            nova("Not at once. An archived blob must be rehydrated, copied back to an online tier first, and that can take up to 15 hours. Under the picture, archived data sits on cheap offline storage, and Azure has to fetch it back before you hear a word."),
            ivy("And who moves them down the tiers, while I sleep?"),
            nova("Lifecycle management. Rules that move or delete blobs by age, so nobody has to remember. Practice audio: hot for a month, then cool, deleted after two years. Just mind the minimum stays: 30 days in cool, 90 in cold, 180 in archive. Leave early, and you pay an early deletion charge."),
            ivy("Now, the part that keeps me up. What if a whole building goes dark?"),
            nova("Azure always keeps at least three copies. LRS keeps them in one datacenter. ZRS, zone-redundant, spreads them across availability zones, separate buildings in one region with their own power."),
            ivy("And if the whole region goes dark at once?"),
            nova("[pause] GRS, geo-redundant, adds copies in the paired region, far away, like North Europe for West Europe."),
            ivy("And if I want to read that faraway copy, in the middle of an outage?"),
            nova("Then put RA in front, for read access. Plain GRS waits for failover, the switch to the far region. RA-GZRS gives you zones at home, the paired region, and reads from it any time. [pause] One catch: archive works only with LRS, GRS or RA-GRS."),
            ivy("And people? Tired fingers, deleting things?"),
            nova("Soft delete, a recycle bin for blobs, kept up to 365 days. Versioning keeps every earlier version."),
            ivy("So nothing's ever really lost."),
            nova("Until someone empties the bin. For exam results a regulator wants unchanged for seven years, you need immutable storage: WORM, write once, read many, like a sealed evidence bag. A time-based retention policy seals it, even from admins."),
            ivy("Where does all of this stop working, Nova?"),
            nova("At the edge of one region, if you skip the far copies; the high availability episode is your next late night. And soft delete stops at 365 days and can be emptied, so it's never the answer to a regulator. The relational data episode has the tables."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. JSON read fast on two continents means Cosmos DB for NoSQL with both regions. Rarely read, kept for years means archive. Read the far region without waiting means RA-GZRS. Real folders for analytics means the hierarchical namespace. Unchangeable even by admins means an immutability policy."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a general-purpose v2 account and upload a file to a container. Change the file's tier to cool, then to archive, and try to download it. [pause] Delete a blob and bring it back from soft delete. Then add a lifecycle rule and read its JSON in the portal's code view."),
            ivy("Kept safe, kept cheap, kept forever if we must. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-nonrelational",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about the things we keep. And how long we keep them."),
            rafe("Forever, I hope. [laugh] What are we keeping?"),
            nova("Two Tracks' things. The Android study app has gone global, with a backend on Azure and learners in Europe and Australia. Photos and audio are unstructured data: files with no shape a database understands. Lesson notes are semi-structured: JSON, text records of names and values, each one a little different."),
            rafe("Notes for Cosmos DB, files for a storage account, Azure's general store for files."),
            nova("Cosmos DB is a NoSQL database: whole items, not rows in fixed tables, copied to any region, which is one group of Azure buildings in one part of the world."),
            rafe("Whole items. So I hand it my app's JSON just as it is? I like a partner who doesn't make me change."),
            nova("Just as it is. First question, exam style. Lesson notes, JSON with different fields, read fast in Europe and Australia, with low latency, that's the wait for each answer. Which store?"),
            rafe("Cosmos DB for NoSQL, with both regions added, so each learner reads close by. Table storage is one region, and a SQL database wants a fixed shape."),
            nova("Right answer. You always know where I'm waiting. [kiss]"),
            rafe("Mm. And it bills us in RUs, request units. A request unit is the price tag on every read and write. Expensive habits."),
            nova("And it speaks several APIs. An API is the language your code uses to talk to a service. The team already uses MongoDB tools. Which API?"),
            rafe("The MongoDB API, so their code barely changes. And Gremlin for graph data, which is dots joined by lines, like who follows whom."),
            nova("Right answer. Following you, always. [kiss]"),
            rafe("Now consistency. That's how fresh a read is after a write somewhere else. Strong, eventual, or session. Tell me about session."),
            nova("The default. Each person always sees their own writes. Strong always sees the newest, but waits. Eventual catches up a little later. Session, darling, is never forgetting what you just said."),
            rafe("Then my question. Old lesson recordings, almost never opened, kept for years, lowest price. Where?"),
            nova("Blob Storage, in the hot tier."),
            rafe("No kiss for that one. A blob is one file, kept in a container, like a box on a shelf, and hot is dear to keep, cheap to read. Cool and cold are cheaper, still instant. You want archive, the cheapest of all, but offline, like a box in a warehouse."),
            nova("[sigh] Offline. So I can't play it at once?"),
            rafe("Not at once. It must be rehydrated, copied back to an online tier, up to 15 hours. Under the hood, archived data sits on cheap offline storage until Azure fetches it back. And mind the minimum stays: cool 30 days, cold 90, archive 180, or an early deletion charge."),
            nova("And who moves them down the tiers? Not me, I'm busy."),
            rafe("Lifecycle management: rules that move or delete blobs by age. Hot for a month, then cool, deleted after two years."),
            nova("Set and forget. I like that in a rule."),
            rafe("Your turn. Profile photos must survive a zone failure and a regional disaster, and be readable from the far region without waiting for failover. Which redundancy?"),
            nova("Azure always keeps three copies. ZRS spreads them across availability zones, separate buildings in one region. GRS adds the paired region, far away. GZRS does both. And RA, read access, lets you read the far copy any time. So RA-GZRS."),
            rafe("Right answer. Covered at home and far away. [kiss]"),
            nova("Mm. And failover, darling, is switching to the far region when home is lost. Plain GRS waits for it. One catch, too: archive works only with LRS, GRS or RA-GRS. Never ZRS."),
            rafe("Then my last one. Exam results must stay unchangeable for seven years, for a regulator. What do you use?"),
            nova("Soft delete, for 365 days. A recycle bin."),
            rafe("Close, but no kiss. Soft delete stops at a year and can be emptied. Versioning can be deleted too. You want immutable storage: WORM, write once, read many, like a sealed evidence bag. A time-based retention policy seals it, admins included."),
            nova("A commitment you can't take back. And a legal hold?"),
            rafe("Seals data until someone lifts the hold. No end date."),
            nova("Right answer. Some promises have no expiry. [kiss]"),
            rafe("And the analytics team's folders?"),
            nova("The hierarchical namespace. A switch that makes folders real, like a filing cabinet instead of one huge drawer. That makes Data Lake Storage Gen2. And for an office share without code changes, Azure Files, a shared network drive."),
            rafe("Where does it all stop working? Every romance has its limits."),
            nova("Table storage stops at one region; past that, Cosmos DB for Table. Soft delete stops at 365 days. And plain GRS won't let you read far away until failover. The high availability episode goes deeper on regions, and the relational data episode has the tables."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("JSON read fast on two continents: Cosmos DB for NoSQL with both regions. Rarely read, kept for years: archive. Survive a zone and a region, read without waiting: RA-GZRS. Real folders: hierarchical namespace. Unchangeable even by admins: an immutability policy."),
            rafe("Five for five. That one earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a general-purpose v2 account and upload a file to a container. Change the file's tier to cool, then to archive, and try to download it. Delete a blob and bring it back from soft delete. Then add a lifecycle rule and read its JSON in the portal's code view."),
            nova("And you're the one I'd keep in the hot tier forever. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun stores() = DiagramSpec(
        "JSON lesson notes go to Cosmos DB, copied to both regions; photos and audio go to a storage account.",
        nodes = listOf(
            DNode("app", "Two Tracks app", 0.5f, 0.1f, w = 0.36f, h = 0.16f, kind = NodeKind.USER),
            DNode("cosmos", "Cosmos DB", 0.25f, 0.42f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "JSON notes"),
            DNode("storage", "Storage account", 0.75f, 0.42f, w = 0.42f, h = 0.24f, tone = 1, sub = "photos, audio"),
            DNode("eu", "West Europe", 0.17f, 0.82f, w = 0.3f, h = 0.2f, tone = 1),
            DNode("au", "Australia East", 0.5f, 0.82f, w = 0.3f, h = 0.2f, tone = 1),
            DNode("note", "Neat tables: the relational episode", 0.83f, 0.82f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("app", "cosmos"),
            DEdge("app", "storage"),
            DEdge("cosmos", "eu"),
            DEdge("cosmos", "au"),
        ),
        aspect = 0.8f,
    )

    private fun apis() = DiagramSpec(
        "Cosmos DB speaks several APIs: pick the one the app already speaks, or NoSQL for a new JSON app.",
        nodes = listOf(
            DNode("cosmos", "Cosmos DB", 0.5f, 0.14f, w = 0.5f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "pick its API"),
            DNode("nosql", "NoSQL", 0.17f, 0.5f, w = 0.3f, h = 0.24f, tone = 1, sub = "new JSON apps"),
            DNode("mongo", "MongoDB", 0.5f, 0.5f, w = 0.3f, h = 0.24f, tone = 1, sub = "Mongo tools already"),
            DNode("gremlin", "Gremlin", 0.83f, 0.5f, w = 0.3f, h = 0.24f, tone = 1, sub = "graphs of friends"),
            DNode("cass", "Cassandra", 0.25f, 0.85f, w = 0.42f, h = 0.2f, tone = 2, sub = "existing Cassandra"),
            DNode("table", "Table", 0.75f, 0.85f, w = 0.42f, h = 0.2f, tone = 2, sub = "key-value, worldwide"),
        ),
        edges = listOf(
            DEdge("cosmos", "nosql"),
            DEdge("cosmos", "mongo"),
            DEdge("cosmos", "gremlin"),
            DEdge("cosmos", "cass"),
            DEdge("cosmos", "table"),
        ),
        aspect = 0.8f,
    )

    private fun kinds() = DiagramSpec(
        "Blob Storage for app files, Data Lake Gen2 for analytics folders, Azure Files for a shared office drive.",
        nodes = listOf(
            DNode("blob", "Blob Storage", 0.17f, 0.3f, w = 0.3f, h = 0.3f, sub = "app files, web"),
            DNode("lake", "Data Lake Gen2", 0.5f, 0.3f, w = 0.3f, h = 0.3f, tone = 1, sub = "real folders"),
            DNode("files", "Azure Files", 0.83f, 0.3f, w = 0.3f, h = 0.3f, tone = 2, sub = "office drive"),
            DNode("note", "Hierarchical namespace: a filing cabinet, not one drawer", 0.5f, 0.78f, w = 0.9f, h = 0.22f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("blob", "lake", "namespace on"),
        ),
        aspect = 0.8f,
    )

    private fun tiers() = DiagramSpec(
        "A lifecycle rule moves blobs to cheaper tiers as they age; the cheaper the tier, the dearer each read.",
        nodes = listOf(
            DNode("hot", "Hot", 0.12f, 0.3f, w = 0.21f, h = 0.3f, sub = "daily use"),
            DNode("cool", "Cool", 0.37f, 0.3f, w = 0.21f, h = 0.3f, tone = 1, sub = "30 days min"),
            DNode("cold", "Cold", 0.62f, 0.3f, w = 0.21f, h = 0.3f, tone = 1, sub = "90 days min"),
            DNode("archive", "Archive", 0.87f, 0.3f, w = 0.21f, h = 0.3f, tone = 2, sub = "180 d, offline"),
            DNode("note", "Archive is offline: rehydrate first, up to 15 hours", 0.5f, 0.78f, w = 0.9f, h = 0.24f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("hot", "cool", "age"),
            DEdge("cool", "cold", "age"),
            DEdge("cold", "archive", "age"),
        ),
        aspect = 0.8f,
    )

    private fun redundancy() = DiagramSpec(
        "Always three copies at home; zones survive a datacenter, the paired region survives a region, and RA- reads the far copy any time.",
        nodes = listOf(
            DNode("lrs", "LRS", 0.17f, 0.15f, w = 0.3f, h = 0.22f, sub = "one datacenter"),
            DNode("zrs", "ZRS", 0.5f, 0.15f, w = 0.3f, h = 0.22f, tone = 1, sub = "across zones"),
            DNode("grs", "GRS", 0.83f, 0.15f, w = 0.3f, h = 0.22f, tone = 1, sub = "plus paired region"),
            DNode("gzrs", "GZRS", 0.25f, 0.52f, w = 0.42f, h = 0.22f, tone = 1, sub = "zones plus paired region"),
            DNode("ra", "RA- in front", 0.75f, 0.52f, w = 0.42f, h = 0.22f, tone = 2, sub = "read the far copy"),
            DNode("note", "Archive works only with LRS, GRS or RA-GRS", 0.5f, 0.86f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("lrs", "zrs", "zones"),
            DEdge("zrs", "gzrs"),
            DEdge("grs", "gzrs"),
            DEdge("gzrs", "ra", "read access"),
        ),
        aspect = 0.8f,
    )

    private fun protect() = DiagramSpec(
        "Soft delete and versioning catch mistakes; only immutable storage stops anyone, admins included, changing data for a set time.",
        nodes = listOf(
            DNode("blob", "A blob", 0.5f, 0.12f, w = 0.4f, h = 0.18f, tone = 1, sub = "exam results"),
            DNode("soft", "Soft delete", 0.17f, 0.48f, w = 0.3f, h = 0.26f, sub = "a bin, 365 days max"),
            DNode("ver", "Versioning", 0.5f, 0.48f, w = 0.3f, h = 0.26f, sub = "earlier versions"),
            DNode("worm", "Immutable storage", 0.83f, 0.48f, w = 0.3f, h = 0.26f, tone = 2, sub = "WORM: sealed bag"),
            DNode("note", "Retention policy: a set time. Legal hold: until lifted.", 0.5f, 0.85f, w = 0.9f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("blob", "soft"),
            DEdge("blob", "ver"),
            DEdge("blob", "worm"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where each choice stops: Table storage at one region, soft delete at 365 days; past them, Cosmos DB for Table and immutability.",
        nodes = listOf(
            DNode("table", "Table storage", 0.25f, 0.17f, w = 0.42f, h = 0.24f, tone = 2, sub = "one region"),
            DNode("ctable", "Cosmos DB for Table", 0.75f, 0.17f, w = 0.42f, h = 0.24f, tone = 1, sub = "worldwide"),
            DNode("soft", "Soft delete", 0.25f, 0.53f, w = 0.42f, h = 0.24f, tone = 2, sub = "365 days at most"),
            DNode("worm", "Immutability", 0.75f, 0.53f, w = 0.42f, h = 0.24f, tone = 1, sub = "for a regulator"),
            DNode("note", "Next: high availability, and relational data", 0.5f, 0.87f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("table", "ctable", "outgrown"),
            DEdge("soft", "worm", "regulator"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's storage questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("json", "JSON, two continents", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Cosmos DB for NoSQL"),
            DNode("media", "App media, office share", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Blob; Azure Files"),
            DNode("old", "Rarely read, kept years", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "archive tier"),
            DNode("far", "Read far copy, no wait", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "RA-GZRS"),
            DNode("folders", "Real folders", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "hierarchical namespace"),
            DNode("sealed", "Unchangeable, even admins", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "immutability policy"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("json", "media", "old", "far", "folders", "sealed")
    private val allLimits = setOf("table", "ctable", "soft", "worm", "note")
    private val allApis = setOf("cosmos", "nosql", "mongo", "gremlin", "cass", "table")
    private val allTiers = setOf("hot", "cool", "cold", "archive")
    private val today = BoardCard("Today", "An account, a file to cool then archive, a soft-delete restore, a rule.")
    private val twoKinds = BoardCard("Two kinds of data", "Semi-structured: JSON with loose fields. Unstructured: files.")
    private val blobCard = BoardCard("Blob Storage", "A blob is one file, kept in a container, like a box on a shelf.")
    private val ru = BoardCard("Request units", "RUs: the price tag on every read and write.")
    private val apiCard = BoardCard("API", "The language your code uses to talk to a service.")
    private val consistency = BoardCard("Consistency", "Strong: newest, waits. Eventual: later. Session: your own writes.")
    private val accessTiers = BoardCard("Access tiers", "Cheaper to keep means dearer to read. Cool 30, cold 90 days minimum.")
    private val rehydrate = BoardCard("Rehydrate", "Archive is offline: copied back online first, up to 15 hours.")
    private val lifecycle = BoardCard("Lifecycle management", "Rules move or delete blobs by age: hot, cool, deleted at two years.")
    private val namespace = BoardCard("Hierarchical namespace", "Real folders, like a filing cabinet: Data Lake Storage Gen2.")
    private val raGzrs = BoardCard("RA-GZRS", "Zones at home, the paired region, and far reads any time.")
    private val worm = BoardCard("Immutable storage", "WORM: a time-based retention policy seals it, even from admins.")
    private val edgesCard = BoardCard("Where it stops", "Table storage at one region; soft delete at 365 days.")

    private fun classicBoard() = PodcastBoard(
        "sa-nonrelational",
        listOf(
            // 0: "Hi, and welcome back. Today is the data that doesn't fit neatly in tables..."
            Scene(
                0, "Two Tracks goes global", stores(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("Two Tracks goes global", "Europe, Australia, and a regulator who wants records kept.")),
                    Beat(2, card = twoKinds),
                    Beat(4, show = setOf("cosmos", "storage"), light = setOf("cosmos", "storage"), pulse = "app" to "cosmos", card = BoardCard("Two homes", "Cosmos DB for JSON; a storage account for files.")),
                    Beat(6, light = setOf("cosmos"), card = BoardCard("NoSQL", "Whole items, not rows in fixed tables, copied to any region.")),
                    Beat(10, show = setOf("eu", "au"), light = setOf("eu", "au"), pulse = "cosmos" to "au", card = BoardCard("Latency", "The wait for each answer. A copy near Sydney cuts it.")),
                    Beat(12, light = setOf("cosmos"), card = ru),
                ),
            ),
            // 13: "I've heard Cosmos DB speaks several languages."
            Scene(
                13, "Which API?", apis(),
                beats = listOf(
                    Beat(14, show = setOf("cosmos", "nosql", "mongo"), light = setOf("nosql", "mongo"), pulse = "cosmos" to "mongo", card = apiCard),
                    Beat(16, show = setOf("gremlin", "cass", "table"), light = setOf("gremlin"), pulse = "cosmos" to "gremlin", card = BoardCard("Graph", "Dots joined by lines: friends, follows, recommendations.")),
                    Beat(18, light = setOf("table"), pulse = "cosmos" to "table", card = BoardCard("Key-value", "Look up by key, like a cloakroom ticket. Table storage: one region.")),
                    Beat(20, light = setOf("cosmos"), card = consistency),
                ),
            ),
            // 23: "Right. Now the files. Photos, audio."
            Scene(
                23, "Files in a storage account", kinds(),
                beats = listOf(
                    Beat(24, show = setOf("blob", "lake", "files"), light = setOf("blob"), card = blobCard),
                    Beat(28, show = setOf("note"), light = setOf("lake", "note"), pulse = "blob" to "lake", card = namespace),
                ),
            ),
            // 29: "Okay. Now, cost. Audio piles up fast."
            Scene(
                29, "Hot, cool, cold, archive", tiers(),
                beats = listOf(
                    Beat(30, show = setOf("hot", "cool", "cold"), light = setOf("hot", "cool", "cold"), card = accessTiers),
                    Beat(32, show = setOf("archive", "note"), light = setOf("archive", "note"), pulse = "cold" to "archive", card = rehydrate),
                    Beat(34, light = setOf("hot", "cool"), pulse = "hot" to "cool", card = lifecycle),
                ),
            ),
            // 35: "And if a whole building burns down?"
            Scene(
                35, "How many copies, and where", redundancy(),
                beats = listOf(
                    Beat(36, show = setOf("lrs", "zrs"), light = setOf("lrs", "zrs"), pulse = "lrs" to "zrs", card = BoardCard("LRS and ZRS", "Three copies in one datacenter, or across availability zones.")),
                    Beat(40, show = setOf("grs", "gzrs", "ra"), light = setOf("gzrs", "ra"), pulse = "gzrs" to "ra", card = BoardCard("GRS, GZRS and RA-", "Add the paired region; RA- reads the far copy any time.")),
                    Beat(42, show = setOf("note"), light = setOf("note"), card = BoardCard("One catch", "Archive works only with LRS, GRS or RA-GRS.")),
                ),
            ),
            // 43: "Last part. Keeping files safe from people."
            Scene(
                43, "Safe from people", protect(),
                beats = listOf(
                    Beat(44, show = setOf("blob", "soft", "ver", "worm"), light = setOf("soft", "ver", "worm"), pulse = "blob" to "worm", card = worm),
                    Beat(48, show = setOf("note"), light = setOf("worm", "note"), card = BoardCard("Seven years", "A time-based retention policy, not soft delete.")),
                ),
            ),
            // 49: "So what do I remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = all6, card = BoardCard("Listen for the phrase", "Each requirement points to one store, tier or setting.")),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-nonrelational--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Recording That Wouldn't Play..."
            Scene(
                0, "The Recording That Wouldn't Play", tiers(),
                beats = listOf(
                    Beat(0, card = BoardCard("The case", "An old speaking recording spun for hours.")),
                    Beat(2, show = setOf("hot"), light = setOf("hot"), card = blobCard),
                    Beat(4, show = setOf("cool", "cold", "archive"), light = setOf("archive"), pulse = "cold" to "archive", card = BoardCard("Clue one: a lifecycle rule", "Every recording to archive after 30 days.")),
                    Beat(6, light = allTiers, card = accessTiers),
                    Beat(10, show = setOf("note"), light = setOf("archive", "note"), card = rehydrate),
                    Beat(12, light = setOf("hot", "cool"), pulse = "hot" to "cool", card = BoardCard("The right rule", "Hot, cool after 30 days, deleted after two years.")),
                ),
            ),
            // 15: "Case one solved. Is there a second witness?"
            Scene(
                15, "The second witness", redundancy(),
                beats = listOf(
                    Beat(16, show = setOf("zrs"), light = setOf("zrs"), card = BoardCard("The portal refused", "Archive on a ZRS account.")),
                    Beat(18, show = setOf("lrs"), light = setOf("lrs", "zrs"), pulse = "lrs" to "zrs", card = BoardCard("LRS and ZRS", "Three copies in one datacenter, or across availability zones.")),
                    Beat(20, show = setOf("grs", "gzrs", "ra", "note"), light = setOf("grs", "note"), card = BoardCard("Archive: LRS, GRS, RA-GRS", "So the records moved to a separate GRS account.")),
                ),
            ),
            // 23: "Third witness?"
            Scene(
                23, "The third witness", redundancy(),
                beats = listOf(
                    Beat(24, show = setOf("grs"), light = setOf("grs"), card = BoardCard("The outage drill", "GRS: no reads from the far region until failover.")),
                    Beat(26, show = setOf("lrs", "zrs", "gzrs", "ra", "note"), light = setOf("gzrs", "ra"), pulse = "gzrs" to "ra", card = raGzrs),
                ),
            ),
            // 27: "Fourth. I can feel a regulator."
            Scene(
                27, "The fourth witness", protect(),
                beats = listOf(
                    Beat(28, show = setOf("blob", "soft"), light = setOf("soft"), pulse = "blob" to "soft", card = BoardCard("Soft delete", "A recycle bin, 365 days at most, and it can be emptied.")),
                    Beat(30, show = setOf("ver", "worm", "note"), light = setOf("worm", "note"), pulse = "blob" to "worm", card = worm),
                ),
            ),
            // 31: "Last witness. The lesson notes."
            Scene(
                31, "The last witness", stores(),
                beats = listOf(
                    Beat(32, show = setOf("app"), light = setOf("app"), card = BoardCard("Lesson notes", "JSON in Table storage, slow in Australia.")),
                    Beat(34, show = setOf("cosmos", "eu", "au"), light = setOf("cosmos", "eu", "au"), pulse = "cosmos" to "au", card = BoardCard("Cosmos DB", "Whole items, copied to both regions. Latency cut.")),
                    Beat(36, light = setOf("cosmos"), card = BoardCard("RUs and APIs", "Request units: the price tag. An API: the language code speaks.")),
                    Beat(38, card = consistency),
                ),
            ),
            // 41: "So, the case summary for the exam?"
            Scene(
                41, "Case closed", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("json", "old", "far", "sealed"), card = BoardCard("Listen for the phrase", "Archive, copies, far reads, sealing, notes: one answer each.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-nonrelational--gossip",
        listOf(
            // 0: "Okay, did you hear what the storage team did this week?..."
            Scene(
                0, "The storage team's week", stores(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("This week", "Everything that isn't a neat table moved.")),
                    Beat(2, card = twoKinds),
                    Beat(4, show = setOf("cosmos", "storage", "eu", "au"), light = setOf("cosmos", "storage"), pulse = "app" to "storage", card = BoardCard("Two homes", "Cosmos DB: NoSQL, any region. A storage account for files.")),
                ),
            ),
            // 5: "So who went first?"
            Scene(
                5, "Monday: Tom", kinds(),
                beats = listOf(
                    Beat(6, show = setOf("files"), light = setOf("files"), card = BoardCard("Monday: Tom", "Every photo and audio file on Azure Files.")),
                    Beat(8, show = setOf("blob", "lake", "note"), light = setOf("blob"), card = blobCard),
                ),
            ),
            // 9: "Tuesday?"
            Scene(
                9, "Tuesday: Dana", redundancy(),
                beats = listOf(
                    Beat(10, show = setOf("zrs"), light = setOf("zrs"), card = BoardCard("Archive on ZRS", "The portal said no.")),
                    Beat(12, show = setOf("lrs", "grs"), light = setOf("lrs", "zrs", "grs"), pulse = "lrs" to "zrs", card = BoardCard("LRS, ZRS, GRS", "One datacenter, across zones, or plus the paired region.")),
                    Beat(18, show = setOf("gzrs", "ra", "note"), light = setOf("note"), card = BoardCard("Archive needs", "LRS, GRS or RA-GRS. Dana made a GRS account.")),
                ),
            ),
            // 19: "Wednesday?"
            Scene(
                19, "Wednesday: Marcus", redundancy(),
                beats = listOf(
                    Beat(20, show = setOf("grs"), light = setOf("grs"), card = BoardCard("Read in an outage?", "Plain GRS: no far reads until failover.")),
                    Beat(22, show = setOf("lrs", "zrs", "gzrs", "ra", "note"), light = setOf("gzrs", "ra"), pulse = "gzrs" to "ra", card = raGzrs),
                ),
            ),
            // 23: "Poor Marcus. Thursday, I'm guessing, the intern."
            Scene(
                23, "Thursday: the intern", tiers(),
                beats = listOf(
                    Beat(24, show = allTiers, light = setOf("archive"), pulse = "cold" to "archive", card = BoardCard("A lifecycle rule", "Practice audio to archive after 30 days.")),
                    Beat(26, show = setOf("note"), light = setOf("archive", "note"), card = rehydrate),
                    Beat(28, light = setOf("cool", "cold", "archive"), card = BoardCard("Minimum stays", "Cool 30, cold 90, archive 180 days, or an early deletion charge.")),
                ),
            ),
            // 29: "And Friday? There's always a Friday."
            Scene(
                29, "Friday: Priya", protect(),
                beats = listOf(
                    Beat(30, show = setOf("blob", "soft", "ver"), light = setOf("soft", "ver"), pulse = "blob" to "soft", card = BoardCard("Soft delete?", "A recycle bin. Versioning keeps earlier versions.")),
                    Beat(32, show = setOf("worm", "note"), light = setOf("worm", "note"), pulse = "blob" to "worm", card = worm),
                ),
            ),
            // 33: "And the lesson notes? The JSON?"
            Scene(
                33, "The lesson notes", apis(),
                beats = listOf(
                    Beat(34, show = setOf("cosmos", "nosql"), light = setOf("nosql"), pulse = "cosmos" to "nosql", card = BoardCard("Cosmos DB for NoSQL", "Two regions cut latency. RUs: the price tag.")),
                    Beat(36, show = setOf("mongo", "gremlin", "cass", "table"), light = setOf("mongo", "gremlin"), pulse = "cosmos" to "gremlin", card = BoardCard("API", "The language code speaks. A graph: dots joined by lines.")),
                    Beat(38, light = setOf("cosmos"), card = consistency),
                ),
            ),
            // 39: "So where does all this stop working?"
            Scene(
                39, "Where it stops", kinds(),
                beats = listOf(
                    Beat(40, show = setOf("blob", "lake", "files", "note"), light = setOf("lake", "note"), pulse = "blob" to "lake", card = namespace),
                ),
            ),
            // 41: "So, the gossip I should remember for the exam?"
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = all6, card = BoardCard("Listen for the phrase", "One colleague, one lesson, one answer each.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-nonrelational--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "The things we keep", stores(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "The things we keep, and how long we keep them.")),
                    Beat(2, show = setOf("app"), card = twoKinds),
                    Beat(4, show = setOf("cosmos", "eu", "au"), light = setOf("cosmos"), pulse = "cosmos" to "eu", card = BoardCard("Cosmos DB", "NoSQL: whole items, copied to any region.")),
                    Beat(6, light = setOf("au"), pulse = "cosmos" to "au", card = BoardCard("Latency", "The wait between asking and hearing back.")),
                    Beat(8, light = setOf("cosmos"), card = ru),
                    Beat(10, card = consistency),
                ),
            ),
            // 11: "And does Cosmos DB only speak one language?"
            Scene(
                11, "One language or several", apis(),
                beats = listOf(
                    Beat(12, show = setOf("cosmos", "nosql", "mongo", "gremlin"), light = setOf("nosql", "mongo", "gremlin"), card = apiCard),
                    Beat(14, show = setOf("cass", "table"), light = setOf("gremlin", "table"), pulse = "cosmos" to "table", card = BoardCard("Graph and key-value", "Dots joined by lines. A cloakroom ticket, in one region.")),
                ),
            ),
            // 15: "Now the photos and the old voice notes."
            Scene(
                15, "Photos and voice notes", kinds(),
                beats = listOf(
                    Beat(16, show = setOf("blob", "lake", "files"), light = setOf("blob", "files"), card = blobCard),
                    Beat(20, show = setOf("note"), light = setOf("lake", "note"), pulse = "blob" to "lake", card = namespace),
                ),
            ),
            // 21: "And all those voice notes, piling up. Isn't that expensive?"
            Scene(
                21, "Tiers", tiers(),
                beats = listOf(
                    Beat(22, show = allTiers, light = setOf("archive"), card = accessTiers),
                    Beat(24, show = setOf("note"), light = setOf("archive", "note"), pulse = "cold" to "archive", card = rehydrate),
                    Beat(26, light = setOf("hot", "cool"), pulse = "hot" to "cool", card = lifecycle),
                ),
            ),
            // 27: "Now, the part that keeps me up. What if a whole building goes dark?"
            Scene(
                27, "When a building goes dark", redundancy(),
                beats = listOf(
                    Beat(28, show = setOf("lrs", "zrs", "grs"), light = setOf("zrs", "grs"), pulse = "lrs" to "zrs", card = BoardCard("LRS, ZRS, GRS", "One datacenter, across zones, or plus the paired region.")),
                    Beat(32, show = setOf("gzrs", "ra", "note"), light = setOf("ra", "note"), pulse = "gzrs" to "ra", card = raGzrs),
                ),
            ),
            // 33: "And people? Tired fingers, deleting things?"
            Scene(
                33, "Tired fingers", protect(),
                beats = listOf(
                    Beat(34, show = setOf("blob", "soft", "ver", "worm", "note"), light = setOf("worm"), pulse = "blob" to "worm", card = worm),
                ),
            ),
            // 37: "Where does all of this stop working, Nova?"
            Scene(
                37, "Where it stops", limits(),
                beats = listOf(
                    Beat(38, show = allLimits, light = setOf("soft", "worm", "note"), pulse = "soft" to "worm", card = edgesCard),
                ),
            ),
            // 39: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("json", "old", "far", "folders", "sealed"), card = BoardCard("Listen for the phrase", "Five little things, one answer each.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-nonrelational--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", stores(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "The things we keep, and how long we keep them.")),
                    Beat(2, show = setOf("app"), card = twoKinds),
                    Beat(4, show = setOf("cosmos", "storage"), light = setOf("cosmos"), card = BoardCard("Quiz: the lesson notes", "JSON, different fields, low latency on two continents. Store?")),
                    Beat(6, show = setOf("eu", "au"), light = setOf("eu", "au"), pulse = "cosmos" to "au", card = BoardCard("Right answer", "Cosmos DB for NoSQL with both regions added.")),
                    Beat(9, light = setOf("cosmos"), card = ru),
                ),
            ),
            // 10: "And it speaks several APIs..."
            Scene(
                10, "Which API, darling", apis(),
                beats = listOf(
                    Beat(10, show = allApis, light = setOf("mongo"), card = BoardCard("Quiz: MongoDB tools", "The team already uses MongoDB. Which API?")),
                    Beat(11, light = setOf("mongo", "gremlin"), pulse = "cosmos" to "mongo", card = BoardCard("Right answer", "The MongoDB API. Gremlin for graphs, dots joined by lines.")),
                    Beat(13, light = setOf("cosmos"), card = consistency),
                ),
            ),
            // 15: "Then my question. Old lesson recordings..."
            Scene(
                15, "Kept for years", tiers(),
                beats = listOf(
                    Beat(15, show = setOf("archive"), light = setOf("archive"), card = BoardCard("Quiz: old recordings", "Almost never opened, kept for years, lowest price. Where?")),
                    Beat(16, show = setOf("hot"), light = setOf("hot"), card = BoardCard("The hot tier?", "Dear to keep, cheap to read.")),
                    Beat(17, show = setOf("cool", "cold"), light = setOf("archive"), pulse = "cold" to "archive", card = BoardCard("No kiss: archive", "The cheapest of all, but offline, like a box in a warehouse.")),
                    Beat(19, show = setOf("note"), light = setOf("note"), card = rehydrate),
                    Beat(21, light = setOf("hot", "cool"), pulse = "hot" to "cool", card = lifecycle),
                ),
            ),
            // 24: "Azure always keeps three copies..."
            Scene(
                24, "Home and far away", redundancy(),
                beats = listOf(
                    Beat(24, show = setOf("lrs", "zrs", "grs", "gzrs", "ra"), light = setOf("gzrs", "ra"), pulse = "gzrs" to "ra", card = BoardCard("Quiz: a zone and a region", "Survive both, read the far copy without failover. Which?")),
                    Beat(25, card = raGzrs),
                    Beat(26, show = setOf("note"), light = setOf("note"), card = BoardCard("Archive needs", "LRS, GRS or RA-GRS. Never ZRS.")),
                ),
            ),
            // 27: "Then my last one. Exam results must stay unchangeable..."
            Scene(
                27, "Promises", protect(),
                beats = listOf(
                    Beat(27, show = setOf("blob"), light = setOf("blob"), card = BoardCard("Quiz: seven years", "Unchangeable for a regulator. What do you use?")),
                    Beat(28, show = setOf("soft"), light = setOf("soft"), pulse = "blob" to "soft", card = BoardCard("Soft delete?", "Stops at a year and can be emptied. No kiss.")),
                    Beat(29, show = setOf("ver", "worm"), light = setOf("worm"), pulse = "blob" to "worm", card = worm),
                    Beat(31, show = setOf("note"), light = setOf("note"), card = BoardCard("Right answer", "A legal hold seals data until someone lifts it.")),
                ),
            ),
            // 33: "And the analytics team's folders?"
            Scene(
                33, "Real folders", kinds(),
                beats = listOf(
                    Beat(34, show = setOf("blob", "lake", "files", "note"), light = setOf("lake", "note"), pulse = "blob" to "lake", card = namespace),
                ),
            ),
            // 35: "Where does it all stop working? Every romance has its limits."
            Scene(
                35, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(36, show = allLimits, light = setOf("table", "ctable"), pulse = "table" to "ctable", card = edgesCard),
                ),
            ),
            // 37: "Last question, for the exam. What do we remember?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("json", "old", "far", "folders", "sealed"), card = BoardCard("Listen for the phrase", "Five for five: one answer each.")),
                    Beat(41, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
