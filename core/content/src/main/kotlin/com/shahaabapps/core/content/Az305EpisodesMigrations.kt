package com.shahaabapps.core.content

/** Migrations: moving to Azure with a plan: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesMigrations {
    private val classic = Podcast(
        "sa-migrations",
        listOf(
            maya("Hi, and welcome back. Today is migrations, for AZ-305, the design exam. Our story: Two Tracks goes global. The company still runs an office system on its own servers, in a small server room: a booking app, its database, and years of recordings. Today we plan the move into Azure."),
            leo("Migration. Is that just a fancy word for moving?"),
            maya("Exactly that. A migration is moving apps and data from your own server room, which people call on-premises, into Azure. Think of moving house. First you list everything you own. Then you decide what each item becomes: move it as it is, repair it, or buy a new one. Then you pack, move and unpack."),
            leo("Is there a guide, or do I just start carrying boxes?"),
            maya("There's a guidebook. The Cloud Adoption Framework, CAF for short, is Microsoft's free guide for moving a whole company to Azure. It runs in phases, like chapters: Strategy, why are we moving? Plan, which apps, in what order? Ready, prepare the ground. And Adopt, move or build."),
            leo("And after the move, the book just ends?"),
            maya("No. Govern, Secure and Manage run alongside for as long as you use Azure, like the house rules once you've moved in. Govern sets the rules, Secure keeps the doors locked, and Manage keeps everything running."),
            leo("Prepare the ground. What does that look like?"),
            maya("A landing zone. It's the prepared ground in Azure: subscriptions, which are Azure's billing accounts, like separate utility accounts, plus networks, rules and sign-in, all set up before anything moves in. Like fitting the locks and the wiring before the furniture arrives."),
            leo("Okay. Then the deciding part. What each item becomes."),
            maya("The Rs. Rehost means nothing changes: the server moves to an Azure virtual machine as it is. People also call it lift and shift."),
            leo("Virtual machine, in plain words?"),
            maya("A computer made of software, rented by the hour in Microsoft's data centre. It acts like a real server, with its own operating system, but you never touch the metal."),
            leo("So my booking app, its Windows server, its settings, all copied across as they are?"),
            maya("All of it, so it's the fastest move, with no code changes. Say your server room has to be empty next month: rehost."),
            leo("And the other Rs?"),
            maya("Replatform: small changes so the app runs on a platform service, where Azure looks after the servers. For example, the booking app moves onto App Service, Azure's home for web apps, and the team stops patching. Patching means installing the operating system's fixes, every month, forever."),
            leo("Platform service. Is that the PaaS I keep hearing about?"),
            maya("Yes. Picture moving house again. IaaS, infrastructure as a service, is renting an empty house: you still fix the boiler yourself. That's a virtual machine, and you patch its operating system."),
            leo("And PaaS is where I just hand over my code?"),
            maya("Your code, and Azure runs the servers under it. PaaS, platform as a service, is a serviced apartment: the landlord fixes things. And SaaS, software as a service, is a hotel: you just turn up."),
            leo("[chuckle] I'd like the hotel, please. And the rest of the Rs?"),
            maya("Refactor: tidy the code for the cloud, with no new features. Rearchitect: change the design, so parts scale separately."),
            leo("Like splitting my one big backend into a bookings service and a payments service?"),
            maya("That's rearchitect. Rebuild: write it again from scratch. Replace: swap it for a bought SaaS product. And two more: retire, switch it off, and retain, leave it where it is."),
            leo("Eight Rs. Any trap in there?"),
            maya("[pause] One. Older material used refactor for the small move onto PaaS that the CAF now calls replatform. So read the requirement words. Minimal code changes onto PaaS is that move, whatever the label says."),
            leo("Now the listing part. How do I know what's even in the server room?"),
            maya("Azure Migrate. It's the free hub for the whole move, and it starts with discovery: finding out what you have. You run the Azure Migrate appliance, a small virtual machine inside your server room that acts like a stocktaker. It watches your servers and reports their size and load to Azure."),
            leo("And then?"),
            maya("An assessment, which is a report per server: is it ready for Azure, what size does it need, and what will it cost a month. A business case compares today's running costs with Azure's. And dependency analysis shows which servers talk to which."),
            leo("Why does that matter?"),
            maya("Because partners must move together. Picture a booking app moved to Azure while its database stays behind. It can't reach its partner, or it's suddenly slow across the internet. So you group servers that talk to each other, and move each group in one wave."),
            leo("Then the database. That's the scary one."),
            maya("Azure Database Migration Service, DMS for short, moves SQL Server databases into Azure. SQL Server is Microsoft's database product. There are two ways. Offline is like moving house with the shop shut: the old database takes no new changes for the whole copy."),
            leo("And online?"),
            maya("Online keeps the shop open. Changes keep flowing to Azure while the copy runs. Downtime, the time users can't use the app, is then only the cutover, the short moment you switch the app across. So if a question says minimal downtime, the answer is an online migration."),
            leo("Where does the database land?"),
            maya("Three choices. SQL Server on an Azure virtual machine, when you need the full product and its operating system. Azure SQL Managed Instance, nearly everything SQL Server does, with no server to patch. And Azure SQL Database, one modern database, fully managed, which DMS moves offline only."),
            leo("The Two Tracks database runs jobs every night. Does that change anything?"),
            maya("It does. Those are SQL Agent jobs, scheduled tasks inside SQL Server. Managed Instance keeps SQL Agent; Azure SQL Database doesn't have it. So, SQL Agent plus a few minutes of downtime means an online migration with DMS, to SQL Managed Instance."),
            leo("Last box. Sixty terabytes of recordings."),
            maya("Files have three tools. AzCopy is a free command-line tool that copies files into Blob storage, Azure's store for files, or into Azure Files, shared folders in Azure. It's scriptable, and its sync command copies only what changed. Right for a modest amount of data on a decent network."),
            leo("And sixty terabytes isn't modest."),
            maya("Not on a slow line. A terabyte is a thousand gigabytes, roughly a thousand films. The office line is 100 megabits a second, and at that speed 60 terabytes takes about eight weeks. So, Azure Data Box: Microsoft posts you a rugged storage box, you fill it, and ship it back. A removal van instead of the post."),
            leo("But people keep adding recordings while the box is in the van."),
            maya("That's Azure Storage Mover: a managed service with a small agent next to your file shares, which sends only new and changed files, and shows progress per share. Data Box carries the bulk; Storage Mover catches up. On a small scale, AzCopy sync does the same."),
            leo("Any warnings before we lift anything?"),
            maya("Two. Don't rehost an app that already has problems: the problems move to Azure with it. And never push tens of terabytes over a slow line when a Data Box would arrive sooner."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If the question says fast, no code changes, the answer is rehost onto a VM. Stop patching with minimal code changes: replatform onto PaaS. Minimal database downtime: an online migration with DMS, to Managed Instance when SQL Agent is there. Tens of terabytes on a slow line: Data Box. Which servers move together: dependency analysis."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create an Azure Migrate project and look at the Servers, databases and web apps page. Create a storage account and a blob container. Copy a local folder into it with azcopy copy. Then change one file, run azcopy sync, and check that only that file is sent."),
            leo("List it, decide it, pack it, move it. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-migrations",
        listOf(
            maya("Welcome to Case Files. Today's case: The Move That Broke Everything. PetPal is a small Android app for booking the vet, with its backend still in an office server room. Last month they moved it all into Azure in one weekend. By Monday, nothing worked."),
            leo("Detective Leo, on the case. My first suspect: Azure itself. Clouds are flaky."),
            maya("[pause] Wrong suspect. Clue one: the booking server already crashed every week, back in the office. They rehosted it, which means they moved it to an Azure virtual machine exactly as it was. A virtual machine is a computer made of software, rented in Microsoft's data centre."),
            leo("So the crashes came along for the ride."),
            maya("Exactly. Rehost, or lift and shift, is the fastest move with no code changes, but the problems move with the app. It fixes nothing."),
            leo("So the memory leak in their booking code just runs on a rented server instead of their own?"),
            maya("Same leak, new address. A shaky app should be repaired first, or replatformed: small changes so it runs on a platform service, where Azure looks after the servers."),
            leo("Platform service. Plain words?"),
            maya("PaaS. Picture renting. IaaS, a virtual machine, is renting an empty house: you still fix the boiler. PaaS, like App Service, is a serviced apartment: the landlord fixes things. And SaaS, a bought product, is a hotel. The more the landlord does, the less patching, installing monthly fixes, for you."),
            leo("Clue two?"),
            maya("The booking app came up in Azure, but every booking timed out. Its report server was still in the office, and the two talk all day."),
            leo("Suspect: the firewall. It's always the firewall."),
            maya("[chuckle] Not this time. Nobody knew they were partners. That's what Azure Migrate is for, the free hub for the whole move. It starts with discovery: the Azure Migrate appliance, a small virtual machine in your server room, acts like a stocktaker and reports each server's size and load."),
            leo("And it would have spotted the partners?"),
            maya("With dependency analysis, yes: it shows which servers talk to which, so partners move together in one wave."),
            leo("How does it know who talks to whom? Does it read their code?"),
            maya("No. Under the picture, the appliance reads each server's settings, its performance and its network connections, and sends that to Azure. It never copies your files. The assessment then says if each server is ready, its size, and its monthly cost."),
            leo("And would it have told them what the move would cost?"),
            maya("A business case does that: it compares today's running costs, the electricity, the hardware, the people, with Azure's. Where Azure Migrate stops: it plans and copies servers, but databases have their own tool, and big piles of files have theirs."),
            leo("Clue three. I can feel it."),
            maya("The database. PetPal's requirement said bookings may stop for ten minutes at most. They used Azure Database Migration Service, DMS, the tool that moves SQL Server databases into Azure, with an offline migration. The booking database was shut for nine hours."),
            leo("Suspect: DMS is slow."),
            maya("DMS did what it was told. Offline is moving house with the shop shut: no new changes until the copy ends. Online keeps the shop open; changes keep flowing to Azure, and downtime is only the cutover, the short moment you switch the app across. Minimal downtime always means online."),
            leo("How does online actually keep up?"),
            maya("It copies a full backup first, then keeps replaying the newer changes from the office database onto the Azure side, until you say switch."),
            leo("Like my app syncing offline edits once the phone gets signal. And where should the database have landed?"),
            maya("Where SQL Agent lives. PetPal's database has SQL Agent jobs, scheduled tasks inside SQL Server. Azure SQL Managed Instance keeps SQL Agent, with no server to patch. Azure SQL Database doesn't, and DMS moves to it offline only."),
            leo("Last clue?"),
            maya("The X-ray images. Seventy terabytes. A terabyte is about a thousand films. They started AzCopy, a free command-line tool that copies files into Blob storage, Azure's store for files, over the office's 100 megabit line. Three weeks later, it was still copying."),
            leo("Suspect: AzCopy has a bug."),
            maya("No bug. Physics. At 100 megabits a second, seventy terabytes takes about nine weeks. AzCopy is right for modest data on a decent line. Past that, Azure Data Box: Microsoft posts a rugged storage box, you fill it and ship it back. The data travels by van, not by wire."),
            leo("But vets keep adding X-rays while the box is on the road."),
            maya("Then Azure Storage Mover catches up: a managed service with an agent beside the file shares that sends only new and changed files. Where Data Box stops, Storage Mover starts. On a small scale, azcopy sync does the same."),
            leo("Is the box safe, rolling around in a van?"),
            maya("The data on it is locked with encryption, scrambled so only the right key reads it, and Microsoft copies it into your storage account when it arrives. The newest boxes hold 120 or 525 terabytes."),
            leo("And what should PetPal have done before any of it?"),
            maya("Followed the Cloud Adoption Framework, Microsoft's free guidebook for a move, in phases: Strategy, Plan, Ready, Adopt. Ready means a landing zone: subscriptions, Azure's billing accounts, plus networks, rules and sign-in, set up before anything moves in. The Networks episode covers how the office reaches Azure."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. Fast, no code changes: rehost, but it won't fix problems. Stop patching, minimal changes: replatform. Minimal database downtime: DMS online, to Managed Instance when SQL Agent is there. Tens of terabytes on a slow line: Data Box, then Storage Mover. And servers that talk move together: dependency analysis."),
            leo("Case closed. Anything to try today?"),
            maya("Rebuild the right plan, small. Create an Azure Migrate project and look at the Servers, databases and web apps page. Create a storage account and a blob container. Copy a local folder into it with azcopy copy. Then change one file, run azcopy sync, and check only that file is sent."),
            leo("No more weekend moves. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-migrations",
        listOf(
            maya("Okay, did you hear what happened at Two Tracks this week? Sit down. Two Tracks goes global, so the old office server room is being emptied into Azure. A migration, which just means moving apps and data from your own server room, called on-premises, into the cloud. And it was a week."),
            leo("[laugh] I heard someone tried to carry a server to the car park. Who went first?"),
            maya("Priya, on Monday, with a plan. She opened the Cloud Adoption Framework, Microsoft's free guidebook for a move. It runs in phases: Strategy, why move? Plan, what, in what order? Ready, prepare the ground. Adopt, move or build. And Govern, Secure and Manage carry on afterwards."),
            leo("Prepare the ground meaning?"),
            maya("A landing zone: subscriptions, which are Azure's billing accounts, plus networks, rules and sign-in, ready before anything moves in. Like fitting the locks and wiring before the furniture arrives. Priya had it done by lunch, which is why everyone's annoyed with her."),
            leo("Is a landing zone a real thing in Azure, or just a word?"),
            maya("Both. It isn't one button. Under the picture, it's real subscriptions with their networks, their policies, which are rules Azure enforces, and who may sign in, often built from Microsoft's ready-made templates. Every app that moves later lands on ground that already follows the rules."),
            leo("And Tuesday?"),
            maya("Tuesday was Tom. Tom took the old report server, the one that crashes every Friday, and rehosted it. Moved it to an Azure virtual machine exactly as it was. A virtual machine is a computer made of software, rented in Microsoft's data centre."),
            leo("And it still crashes every Friday?"),
            maya("Every Friday. Rehost, lift and shift, is fast with no code changes, but the problems move with it. And nobody even used those reports. That server should have been retired, which is one of the Rs: switched off. Retain means leave it where it is."),
            leo("How many Rs are there?"),
            maya("Eight in the CAF. Rehost, nothing changes. Replatform, small changes onto a platform service. Refactor, tidy the code. Rearchitect, change the design so parts scale separately."),
            leo("And the other four? Tom's going to need them."),
            maya("Rebuild from scratch. Replace with a bought product. Plus retire and retain. And older guides said refactor for the small move onto PaaS, so read the requirement words."),
            leo("PaaS. Remind me?"),
            maya("Platform as a service. IaaS, a virtual machine, is renting an empty house: you fix the boiler. PaaS, like App Service, is a serviced apartment: the landlord fixes it. SaaS is a hotel. Priya replatformed the booking app onto App Service, so nobody patches, installs monthly fixes, ever again."),
            leo("Wednesday?"),
            maya("The intern. Bless them. They moved the booking app, and left its database behind in the office. Every page took ten seconds."),
            leo("How were they supposed to know?"),
            maya("Azure Migrate, the free hub for the move. Its appliance, a small virtual machine in the server room, works like a stocktaker. Under the picture, it reads each server's settings, its load and its network connections, and never your files. Dependency analysis then shows who talks to whom, so partners move together."),
            leo("And Thursday? I'm guessing the database."),
            maya("Dana. Dana moved the booking database with Azure Database Migration Service, DMS, the tool for SQL Server databases. Offline. On Thursday morning. The booking app was shut for six hours."),
            leo("Offline being bad?"),
            maya("Offline is moving with the shop shut: no changes until the copy ends. Online keeps the shop open, changes keep flowing from the office to Azure, and downtime is only the cutover, the moment you switch over."),
            leo("And where did the database end up?"),
            maya("It has SQL Agent jobs, scheduled tasks, so Azure SQL Managed Instance, which keeps SQL Agent. Azure SQL Database doesn't, and DMS moves to it offline only."),
            leo("Friday? There's always a Friday."),
            maya("Marcus. Marcus started copying sixty terabytes of recordings with AzCopy, a free command-line tool for copying files into Blob storage, Azure's store for files. Over the office's 100 megabit line. It said eight weeks remaining."),
            leo("[sigh] And the move is due in a month."),
            maya("So Priya ordered an Azure Data Box. Microsoft posts a rugged storage box, you fill it, and ship it back: a removal van instead of the post. And while it's on the road, Azure Storage Mover sends only the new and changed files. Where it stops: Data Box is offline, so the catch-up is always online."),
            leo("How big is one of those boxes?"),
            maya("The newest hold 120 or 525 terabytes. The data is locked with encryption on the box, scrambled so only the right key reads it, and Microsoft copies it into your storage account when it arrives. Marcus cancelled his copy. Eventually."),
            leo("And what's next for them?"),
            maya("Connecting the office to Azure properly, which the Networks episode covers. Marcus is reading it now, apparently."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Fast, no code changes: rehost, but it won't fix a broken app. Stop patching with small changes: replatform. Minimal database downtime: DMS online, to Managed Instance with SQL Agent. Tens of terabytes on a slow line: Data Box, then Storage Mover. Servers that talk: dependency analysis, and move them together."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create an Azure Migrate project and look at the Servers, databases and web apps page. Create a storage account and a blob container. Copy a local folder into it with azcopy copy. Then change one file, run azcopy sync, and check only that file is sent."),
            leo("Retire the Friday server. Got it, Tom. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-migrations",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about moving house. Moving into Azure."),
            ivy("Mm. Packing boxes at midnight. Whose house?"),
            nova("Two Tracks. It's gone global, but there's still a little server room in the office: a booking app, its database, years of recordings. A migration moves apps and data from your own server room, which people call on-premises, into Azure."),
            ivy("Where do you even start a move like that?"),
            nova("With a guidebook. The Cloud Adoption Framework, Microsoft's free guide for moving a company to Azure. Phases, like chapters: Strategy, Plan, Ready, Adopt. And Govern, Secure and Manage stay with you, for as long as you live there."),
            ivy("Ready. That sounds like tidying before guests."),
            nova("[chuckle] Close. Ready means a landing zone: the prepared ground in Azure. Subscriptions, which are Azure's billing accounts, networks, rules and sign-in, all set up before a single box arrives. The locks fitted, the lights wired."),
            ivy("And then you list everything you own."),
            nova("Azure Migrate does the listing. It's the free hub for the move. You run its appliance, a small virtual machine in your server room, a computer made of software, and it walks around quietly like a stocktaker. It reports each server's size and load. [pause] It never copies your files; it reads settings, performance and connections."),
            ivy("And what does it tell you?"),
            nova("An assessment: is each server ready for Azure, what size does it need, what will it cost a month. And dependency analysis: who talks to whom, all night long. Partners move together, in one wave, or one is left calling the other across the internet."),
            ivy("And does it tell you what the new place will cost?"),
            nova("A business case. It sets today's running costs, the power, the hardware, the people, beside Azure's. [pause] Practical, even at this hour."),
            ivy("Then you decide what each thing becomes."),
            nova("The Rs. Rehost: nothing changes, the server moves to an Azure virtual machine as it is. Lift and shift. Fast, no code changes. But it fixes nothing; a troubled app brings its troubles."),
            ivy("And if I'm tired of fixing things myself?"),
            nova("Then replatform. Small changes so the app runs on a platform service. Picture it: a virtual machine, IaaS, is renting an empty house, you still fix the boiler."),
            ivy("And I'm so tired of boilers."),
            nova("[pause] Then a platform service, PaaS, like App Service, is a serviced apartment; the landlord fixes it. And SaaS, a bought product, is a hotel."),
            ivy("And the other Rs?"),
            nova("Refactor, tidy the code. Rearchitect, change the design so parts scale apart. Rebuild from scratch. Replace with a bought product. Retire, switch it off. Retain, leave it be. [pause] And older guides called the small move onto PaaS refactor. So listen for the words: minimal code changes onto PaaS."),
            ivy("Now the database. The one I worry about."),
            nova("Azure Database Migration Service, DMS, moves SQL Server databases, Microsoft's database product, into Azure. Offline is moving with the shop shut, nothing written until the copy is done. Online keeps the shop open. It copies a backup, then keeps replaying the newer changes, until the cutover, the short moment you switch across."),
            ivy("And where does the database go to sleep?"),
            nova("If it has SQL Agent jobs, little scheduled tasks that run at night, then Azure SQL Managed Instance. It keeps SQL Agent, and there's no server to patch. Azure SQL Database doesn't have SQL Agent, and DMS moves to it offline only. And SQL Server on a virtual machine, when you need everything, the operating system too."),
            ivy("And the recordings? Years of them."),
            nova("Sixty terabytes. A terabyte is about a thousand films. AzCopy, a free command-line tool, copies files into Blob storage, Azure's store for files, and its sync copies only what changed. Lovely on a good line. On a 100 megabit office line, sixty terabytes takes about eight weeks."),
            ivy("Too slow, even for us night owls."),
            nova("So, Azure Data Box. Microsoft posts you a rugged storage box. You fill it, and ship it back. [pause] A removal van, driving through the night, instead of the post."),
            ivy("A van full of our secrets, out there in the dark."),
            nova("Locked with encryption, scrambled so only the right key reads it. The newest boxes hold 120 or 525 terabytes, and Microsoft copies them into your storage account when they arrive."),
            ivy("But people keep adding files while it's on the road."),
            nova("That's where Data Box stops. It's offline. So Azure Storage Mover catches up: a managed service with a small agent near your file shares, sending only new and changed files. The Networks episode is your next late night, for how the office reaches Azure."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. Fast, with no code changes, means rehost. Stop patching with small changes means replatform. Minimal database downtime means DMS online, to Managed Instance when SQL Agent is there. Tens of terabytes on a slow line means Data Box. And servers that talk move together, found by dependency analysis."),
            ivy("And something to try today, before I sleep?"),
            nova("Create an Azure Migrate project and look at the Servers, databases and web apps page. Create a storage account and a blob container. [pause] Copy a local folder into it with azcopy copy. Then change one file, run azcopy sync, and check only that file is sent."),
            ivy("Everything packed, nothing lost. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-migrations",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about moving in together. Into Azure."),
            rafe("Moving in. That's a big step. Whose things are we moving?"),
            nova("[laugh] Two Tracks. It's gone global, but its office still has a server room: a booking app, its database, and years of recordings. A migration moves apps and data from your own server room, called on-premises, into Azure."),
            rafe("And before we carry anything, we read the guidebook. The Cloud Adoption Framework, Microsoft's free guide for a move: Strategy, Plan, Ready, Adopt. Ready means a landing zone, subscriptions, Azure's billing accounts, plus networks and rules, set up before the furniture arrives."),
            nova("Look at you, prepared. First question, exam style. The office server must be running in Azure next month, with no code changes at all. Which strategy?"),
            rafe("Rehost. Move it to an Azure virtual machine, a computer made of software, rented in Microsoft's data centre, exactly as it is. Lift and shift."),
            nova("Right answer. You take me as I am. [kiss]"),
            rafe("Mm. Though if I had problems, they'd come along too."),
            nova("They would. But first, the ground we land on. A landing zone. What is it really, under the picture?"),
            rafe("Real subscriptions, with networks, policies, the rules Azure enforces, and sign-in already set up, often from Microsoft's templates. Govern, Secure and Manage carry on after the move, like house rules. Everything that moves in follows them."),
            nova("Mm, house rules. And rehost fixes nothing, remember. So, my next question. The team wants to stop patching servers, installing their monthly fixes, with only minimal code changes. Which strategy?"),
            rafe("Rebuild. Start fresh, from scratch. A whole new me."),
            nova("[sigh] No kiss for that one. Rebuild is a full rewrite. It's replatform: small changes so the app runs on a platform service, PaaS, like App Service."),
            rafe("So I keep my code, and someone else does my chores?"),
            nova("A serviced apartment, darling; the landlord fixes the boiler. A virtual machine, IaaS, is the empty house where you fix it yourself. Older guides said refactor for that move, so listen for the words."),
            rafe("Fine, I'll take the apartment. And the other Rs?"),
            nova("Refactor, tidy the code. Rearchitect, change the design. Rebuild. Replace with a bought SaaS product, the hotel. Retire, switch it off. Retain, keep it where it is. Some things you never move."),
            rafe("Then my question. Before we move a thing, how do we know what's in the server room, and which servers can't live apart?"),
            nova("Azure Migrate, the free hub for the move. Its appliance, a small virtual machine in the server room, is a stocktaker: it reports each server's size and load, and an assessment gives readiness, size and monthly cost. Dependency analysis shows who talks to whom, so partners move together."),
            rafe("Right answer. Nobody gets left behind. [kiss]"),
            nova("And under the picture, the appliance reads settings, performance and connections, never your files. Now, darling, the database. A SQL Server database with SQL Agent jobs, scheduled tasks, must move with only a few minutes of downtime. What do you design?"),
            rafe("Azure Database Migration Service, DMS, online, to Azure SQL Managed Instance. Managed Instance keeps SQL Agent with no server to patch, and online keeps the shop open while the changes flow, until a short cutover."),
            nova("Right answer. Smooth, and nobody notices we've gone. [kiss]"),
            rafe("And offline would be?"),
            nova("Moving with the shop shut: no changes for the whole copy. And Azure SQL Database has no SQL Agent, and DMS moves to it offline only. That's where it stops working. The Relational data episode covers which database to choose."),
            rafe("My question, then. One hundred terabytes of archive files, three weeks, and a 100 megabit line the office also uses. What do you recommend?"),
            nova("AzCopy. The free command-line tool that copies files into Blob storage, Azure's store for files."),
            rafe("Close, but no kiss. AzCopy is right for the data, but that line would take about three months. A terabyte is about a thousand films. It's Azure Data Box: Microsoft posts a rugged storage box, we fill it, and ship it back. A removal van instead of the post."),
            nova("A removal van. How big a van?"),
            rafe("The newest boxes hold 120 or 525 terabytes, locked with encryption, scrambled so only the right key reads them. Microsoft copies them into our storage account when they arrive. Then the box goes home. Some things only stay one night."),
            nova("Mm. Fair. Then one more. While the box is on the road, people keep changing files. How do we catch up?"),
            rafe("Azure Storage Mover. A managed service with an agent near the file shares, sending only new and changed files. Or azcopy sync, on a small scale."),
            nova("Right answer. Only what's changed between us. [kiss]"),
            rafe("Last question, for the exam. What do we remember?"),
            nova("Fast, no code changes: rehost. Stop patching, minimal changes: replatform. Minimal database downtime: DMS online, Managed Instance for SQL Agent. Tens of terabytes on a slow line: Data Box, then Storage Mover. And servers that talk move together, found by dependency analysis."),
            rafe("Five for five. That one earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create an Azure Migrate project and look at the Servers, databases and web apps page. Create a storage account and a blob container. Copy a local folder into it with azcopy copy. Then change one file, run azcopy sync, and check only that file is sent."),
            nova("And you're the one thing I'd never retire. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun overview() = DiagramSpec(
        "Azure Migrate takes stock of the office server room, a strategy is picked per app, and each lands on a VM, a platform service, a managed database or storage.",
        nodes = listOf(
            DNode("office", "Office server room", 0.18f, 0.15f, w = 0.32f, h = 0.2f, tone = 2, sub = "on-premises"),
            DNode("migrate", "Azure Migrate", 0.5f, 0.15f, w = 0.28f, h = 0.2f, tone = 1, sub = "discover, assess"),
            DNode("decide", "Pick an R per app", 0.83f, 0.15f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("iaas", "Azure VMs", 0.18f, 0.55f, w = 0.32f, h = 0.2f, sub = "rehost"),
            DNode("paas", "App Service", 0.5f, 0.55f, w = 0.28f, h = 0.2f, sub = "replatform"),
            DNode("files", "Blob storage", 0.83f, 0.55f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "the recordings"),
            DNode("sql", "SQL Managed Instance", 0.5f, 0.86f, w = 0.5f, h = 0.18f, kind = NodeKind.CYLINDER, sub = "DMS, online"),
        ),
        edges = listOf(
            DEdge("office", "migrate", "appliance"),
            DEdge("migrate", "decide", "report"),
            DEdge("decide", "iaas"),
            DEdge("decide", "paas"),
            DEdge("decide", "files"),
            DEdge("decide", "sql"),
        ),
        aspect = 0.8f,
    )

    private fun caf() = DiagramSpec(
        "The Cloud Adoption Framework runs Strategy, Plan, Ready and Adopt; Ready builds the landing zone, and Govern, Secure and Manage carry on afterwards.",
        nodes = listOf(
            DNode("strat", "Strategy", 0.13f, 0.15f, w = 0.22f, h = 0.2f, sub = "why move?"),
            DNode("plan", "Plan", 0.38f, 0.15f, w = 0.22f, h = 0.2f, sub = "what, when?"),
            DNode("ready", "Ready", 0.63f, 0.15f, w = 0.22f, h = 0.2f, tone = 1, sub = "the ground"),
            DNode("adopt", "Adopt", 0.88f, 0.15f, w = 0.22f, h = 0.2f, sub = "move, build"),
            DNode("lz", "Landing zone", 0.33f, 0.52f, w = 0.6f, h = 0.2f, tone = 1, sub = "subscriptions, networks, rules"),
            DNode("sub", "Subscription", 0.83f, 0.52f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE, sub = "a billing account"),
            DNode("run", "Govern, Secure, Manage: always", 0.5f, 0.86f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("strat", "plan"),
            DEdge("plan", "ready"),
            DEdge("ready", "adopt"),
            DEdge("ready", "lz", "builds"),
            DEdge("lz", "sub"),
        ),
        aspect = 0.8f,
    )

    private fun rs() = DiagramSpec(
        "The Rs: what each app becomes, from rehost with no changes to replace with a bought product; retire and retain make eight.",
        nodes = listOf(
            DNode("rehost", "Rehost", 0.26f, 0.12f, w = 0.46f, h = 0.18f, tone = 1, sub = "a VM, as it is"),
            DNode("replat", "Replatform", 0.76f, 0.12f, w = 0.44f, h = 0.18f, tone = 1, sub = "small changes, PaaS"),
            DNode("refactor", "Refactor", 0.26f, 0.38f, w = 0.46f, h = 0.18f, sub = "tidy the code"),
            DNode("rearch", "Rearchitect", 0.76f, 0.38f, w = 0.44f, h = 0.18f, sub = "change the design"),
            DNode("rebuild", "Rebuild", 0.26f, 0.64f, w = 0.46f, h = 0.18f, sub = "from scratch"),
            DNode("replace", "Replace", 0.76f, 0.64f, w = 0.44f, h = 0.18f, sub = "buy a SaaS product"),
            DNode("rr", "Retire: switch off. Retain: stay.", 0.5f, 0.89f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        aspect = 0.8f,
    )

    private fun hosting() = DiagramSpec(
        "IaaS is renting an empty house, PaaS a serviced apartment, SaaS a hotel: the further right, the less patching is yours.",
        nodes = listOf(
            DNode("iaas", "IaaS: empty house", 0.18f, 0.2f, w = 0.32f, h = 0.24f, sub = "virtual machine"),
            DNode("paas", "PaaS: serviced apartment", 0.5f, 0.2f, w = 0.3f, h = 0.24f, tone = 1, sub = "App Service"),
            DNode("saas", "SaaS: a hotel", 0.83f, 0.2f, w = 0.3f, h = 0.24f, sub = "bought product"),
            DNode("ifix", "You fix the boiler", 0.18f, 0.6f, w = 0.32f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("pfix", "Azure patches", 0.5f, 0.6f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("sfix", "Just turn up", 0.83f, 0.6f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("note", "Less patching to the right", 0.5f, 0.9f, w = 0.9f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("iaas", "ifix"),
            DEdge("paas", "pfix"),
            DEdge("saas", "sfix"),
        ),
        aspect = 0.8f,
    )

    private fun discover() = DiagramSpec(
        "The Azure Migrate appliance watches the office servers and reports to Azure Migrate, which gives an assessment, dependency analysis and a business case.",
        nodes = listOf(
            DNode("servers", "Office servers", 0.18f, 0.15f, w = 0.32f, h = 0.2f, tone = 2),
            DNode("app", "Appliance", 0.5f, 0.15f, w = 0.28f, h = 0.2f, sub = "a stocktaker VM"),
            DNode("migrate", "Azure Migrate", 0.83f, 0.15f, w = 0.3f, h = 0.2f, tone = 1, sub = "the hub"),
            DNode("assess", "Assessment", 0.18f, 0.55f, w = 0.32f, h = 0.22f, kind = NodeKind.NOTE, sub = "ready, size, cost"),
            DNode("deps", "Dependency analysis", 0.5f, 0.55f, w = 0.28f, h = 0.22f, kind = NodeKind.NOTE, sub = "who talks to whom"),
            DNode("case", "Business case", 0.83f, 0.55f, w = 0.3f, h = 0.22f, kind = NodeKind.NOTE, sub = "today vs Azure"),
            DNode("note", "Partners move together, in one wave", 0.5f, 0.88f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("servers", "app", "watches"),
            DEdge("app", "migrate", "reports"),
            DEdge("migrate", "assess"),
            DEdge("migrate", "deps"),
            DEdge("migrate", "case"),
        ),
        aspect = 0.8f,
    )

    private fun database() = DiagramSpec(
        "DMS moves a SQL Server database online to Managed Instance or a VM, keeping downtime to the cutover; Azure SQL Database is offline only.",
        nodes = listOf(
            DNode("src", "SQL Server", 0.18f, 0.15f, w = 0.32f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 2, sub = "on-premises"),
            DNode("dms", "DMS", 0.5f, 0.15f, w = 0.28f, h = 0.22f, tone = 1, sub = "online or offline"),
            DNode("mi", "Managed Instance", 0.83f, 0.15f, w = 0.32f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "SQL Agent kept"),
            DNode("vm", "SQL Server on a VM", 0.25f, 0.6f, w = 0.44f, h = 0.2f, sub = "full product, OS"),
            DNode("sqldb", "Azure SQL Database", 0.75f, 0.6f, w = 0.44f, h = 0.2f, sub = "offline only, no Agent"),
            DNode("note", "Online: downtime is only the cutover", 0.5f, 0.88f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("src", "dms"),
            DEdge("dms", "mi", "online"),
            DEdge("dms", "vm"),
            DEdge("dms", "sqldb", "offline", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun files() = DiagramSpec(
        "AzCopy sends files over the wire; on a slow line, a Data Box carries the bulk by courier and Storage Mover catches up with the changes.",
        nodes = listOf(
            DNode("office", "Office file shares", 0.18f, 0.15f, w = 0.32f, h = 0.22f, tone = 2),
            DNode("azcopy", "AzCopy", 0.5f, 0.15f, w = 0.28f, h = 0.22f, sub = "copy, then sync"),
            DNode("blob", "Blob storage", 0.83f, 0.15f, w = 0.3f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "or Azure Files"),
            DNode("box", "Azure Data Box", 0.3f, 0.58f, w = 0.4f, h = 0.22f, sub = "120 or 525 TB"),
            DNode("mover", "Storage Mover", 0.75f, 0.58f, w = 0.4f, h = 0.22f, tone = 1, sub = "only the changes"),
            DNode("note", "Tens of terabytes, slow line: ship it", 0.5f, 0.88f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("office", "azcopy"),
            DEdge("azcopy", "blob", "the wire"),
            DEdge("office", "box", "fill"),
            DEdge("box", "blob", "courier"),
            DEdge("mover", "blob", "catch-up"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's migration questions, each as the requirement words and the answer they point to.",
        nodes = listOf(
            DNode("rehost", "Fast, no code changes", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "rehost"),
            DNode("replat", "Stop patching", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "replatform"),
            DNode("dms", "Minimal DB downtime", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "DMS online"),
            DNode("mi", "SQL Agent jobs", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Managed Instance"),
            DNode("box", "Tens of TB, slow line", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Data Box"),
            DNode("deps", "Servers that talk", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "dependency analysis"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("rehost", "replat", "dms", "mi", "box", "deps")
    private val phases = setOf("strat", "plan", "ready", "adopt")
    private val houses = setOf("iaas", "paas", "saas", "ifix", "pfix", "sfix", "note")
    private val otherRs = setOf("refactor", "rearch", "rebuild", "replace", "rr")
    private val today = BoardCard("Today", "A Migrate project, a blob container, azcopy copy, then azcopy sync.")
    private val sync = BoardCard("Bash", "azcopy sync \"D:\\recordings\" \"https://ttstore.blob.core.windows.net/recordings\" --recursive", code = true)

    private fun classicBoard() = PodcastBoard(
        "sa-migrations",
        listOf(
            // 0: "Hi, and welcome back. Today is migrations..."
            Scene(
                0, "Two Tracks goes global", overview(),
                beats = listOf(
                    Beat(0, show = setOf("office"), light = setOf("office"), card = BoardCard("Two Tracks goes global", "An office server room: a booking app, its database, recordings.")),
                    Beat(2, show = setOf("migrate", "decide"), light = setOf("office", "migrate"), pulse = "office" to "migrate", card = BoardCard("A migration", "From on-premises into Azure, like moving house.")),
                ),
            ),
            // 3: "Is there a guide, or do I just start carrying boxes?"
            Scene(
                3, "The guidebook", caf(),
                beats = listOf(
                    Beat(4, show = phases, light = phases, card = BoardCard("Cloud Adoption Framework", "Microsoft's guide in phases: Strategy, Plan, Ready, Adopt.")),
                    Beat(6, show = setOf("run"), light = setOf("run"), card = BoardCard("Govern, Secure, Manage", "The house rules, for as long as you live in Azure.")),
                    Beat(8, show = setOf("lz", "sub"), light = setOf("lz", "sub"), pulse = "ready" to "lz", card = BoardCard("Landing zone", "Locks and wiring before the furniture: ready before anything moves.")),
                ),
            ),
            // 9: "Okay. Then the deciding part. What each item becomes."
            Scene(
                9, "What each app becomes", rs(),
                beats = listOf(
                    Beat(10, show = setOf("rehost"), light = setOf("rehost"), card = BoardCard("Rehost", "Lift and shift: the server moves to an Azure VM as it is.")),
                    Beat(12, light = setOf("rehost"), card = BoardCard("A virtual machine", "A computer made of software, rented by the hour.")),
                    Beat(16, show = setOf("replat"), light = setOf("replat"), card = BoardCard("Replatform", "Small changes onto a platform service, so nobody patches.")),
                ),
            ),
            // 17: "Platform service. Is that the PaaS I keep hearing about?"
            Scene(
                17, "House, apartment, hotel", hosting(),
                beats = listOf(
                    Beat(18, show = houses, light = setOf("paas", "pfix"), card = BoardCard("IaaS, PaaS, SaaS", "An empty house, a serviced apartment, a hotel.")),
                ),
            ),
            // 21: "I'd like the hotel, please. And the rest of the Rs?"
            Scene(
                21, "The other Rs", rs(),
                beats = listOf(
                    Beat(22, show = otherRs, light = otherRs, card = BoardCard("Six Rs, and two more", "Refactor, rearchitect, rebuild, replace; retire and retain.")),
                    Beat(26, light = setOf("replat", "refactor"), card = BoardCard("The trap", "Older material says refactor for what the CAF calls replatform.")),
                ),
            ),
            // 27: "Now the listing part. How do I know what's even in the server room?"
            Scene(
                27, "Take stock first", discover(),
                beats = listOf(
                    Beat(28, show = setOf("servers", "app", "migrate"), light = setOf("app"), pulse = "servers" to "app", card = BoardCard("Azure Migrate", "Its appliance, a stocktaker VM, reports each server's size and load.")),
                    Beat(30, show = setOf("assess", "case", "deps"), light = setOf("assess", "case"), pulse = "migrate" to "assess", card = BoardCard("Assessment", "Ready? What size? What cost a month? Plus a business case.")),
                    Beat(32, show = setOf("note"), light = setOf("deps", "note"), pulse = "migrate" to "deps", card = BoardCard("Dependency analysis", "Who talks to whom, so partners move together in one wave.")),
                ),
            ),
            // 33: "Then the database. That's the scary one."
            Scene(
                33, "The database", database(),
                beats = listOf(
                    Beat(34, show = setOf("src", "dms"), light = setOf("dms"), pulse = "src" to "dms", card = BoardCard("DMS", "Moves SQL Server databases into Azure, online or offline.")),
                    Beat(36, show = setOf("note"), light = setOf("note"), card = BoardCard("Online", "The shop stays open; downtime is only the cutover.")),
                    Beat(38, show = setOf("mi", "vm", "sqldb"), light = setOf("mi", "vm", "sqldb"), pulse = "dms" to "vm", card = BoardCard("Three landing spots", "A VM, Managed Instance, or SQL Database (offline only).")),
                    Beat(40, light = setOf("mi"), pulse = "dms" to "mi", card = BoardCard("SQL Agent", "Scheduled jobs. Managed Instance keeps them; SQL Database doesn't.")),
                ),
            ),
            // 41: "Last box. Sixty terabytes of recordings."
            Scene(
                41, "Sixty terabytes", files(),
                beats = listOf(
                    Beat(42, show = setOf("office", "azcopy", "blob"), light = setOf("azcopy"), pulse = "azcopy" to "blob", card = sync),
                    Beat(44, show = setOf("box", "note"), light = setOf("box"), pulse = "office" to "box", card = BoardCard("Azure Data Box", "At 100 Mbps, 60 TB takes eight weeks. Ship a box instead.")),
                    Beat(46, show = setOf("mover"), light = setOf("mover"), pulse = "mover" to "blob", card = BoardCard("Storage Mover", "Sends only new and changed files, after the box has left.")),
                    Beat(48, light = setOf("note"), card = BoardCard("Two warnings", "Don't rehost a broken app. Don't push tens of TB down a slow line.")),
                ),
            ),
            // 49: "So what do I remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = all6, card = BoardCard("Listen for the words", "Each requirement in the question points to one answer.")),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-migrations--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Move That Broke Everything..."
            Scene(
                0, "The Move That Broke Everything", overview(),
                beats = listOf(
                    Beat(0, show = setOf("office", "migrate"), light = setOf("office"), card = BoardCard("The case", "PetPal moved everything in one weekend. By Monday, nothing worked.")),
                    Beat(2, show = setOf("decide", "iaas"), light = setOf("iaas"), pulse = "decide" to "iaas", card = BoardCard("Clue one", "The server that crashed weekly was rehosted on a VM, as it was.")),
                ),
            ),
            // 3: "So the crashes came along for the ride."
            Scene(
                3, "Fix it, or let Azure fix it", hosting(),
                beats = listOf(
                    Beat(4, show = setOf("iaas", "paas"), light = setOf("paas"), card = BoardCard("Replatform", "Small changes onto a platform service; Azure looks after servers.")),
                    Beat(8, show = setOf("saas", "ifix", "pfix", "sfix", "note"), light = setOf("iaas", "paas", "saas"), card = BoardCard("IaaS, PaaS, SaaS", "An empty house, a serviced apartment, a hotel.")),
                ),
            ),
            // 9: "Clue two?"
            Scene(
                9, "Clue two: the partner", discover(),
                beats = listOf(
                    Beat(10, show = setOf("servers"), light = setOf("servers"), card = BoardCard("Clue two", "Bookings timed out: the report server was still in the office.")),
                    Beat(12, show = setOf("app", "migrate"), light = setOf("app"), pulse = "servers" to "app", card = BoardCard("Azure Migrate", "Its appliance, a stocktaker VM, reports each server's size and load.")),
                    Beat(14, show = setOf("deps", "assess", "note"), light = setOf("deps"), pulse = "migrate" to "deps", card = BoardCard("Dependency analysis", "Who talks to whom. It reads connections, never your files.")),
                    Beat(18, show = setOf("case"), light = setOf("case"), pulse = "migrate" to "case", card = BoardCard("Business case", "Today's running costs beside Azure's.")),
                ),
            ),
            // 19: "Clue three. I can feel it."
            Scene(
                19, "Clue three: nine hours", database(),
                beats = listOf(
                    Beat(20, show = setOf("src", "dms"), light = setOf("dms"), pulse = "src" to "dms", card = BoardCard("Clue three", "Offline migration: the booking database shut for nine hours.")),
                    Beat(22, show = setOf("note"), light = setOf("note"), card = BoardCard("Online", "Changes keep flowing; downtime is only the cutover.")),
                    Beat(26, show = setOf("mi", "vm", "sqldb"), light = setOf("mi"), pulse = "dms" to "mi", card = BoardCard("Managed Instance", "Keeps SQL Agent jobs. SQL Database: offline only, no Agent.")),
                ),
            ),
            // 27: "Last clue?"
            Scene(
                27, "The last clue", files(),
                beats = listOf(
                    Beat(28, show = setOf("office", "azcopy", "blob"), light = setOf("azcopy"), pulse = "azcopy" to "blob", card = BoardCard("The last clue", "70 TB through AzCopy on a 100 Mbps line. Weeks, still copying.")),
                    Beat(30, show = setOf("box", "note"), light = setOf("box"), pulse = "office" to "box", card = BoardCard("Azure Data Box", "About nine weeks by wire. Ship a box: the data goes by van.")),
                    Beat(32, show = setOf("mover"), light = setOf("mover"), pulse = "mover" to "blob", card = BoardCard("Storage Mover", "Catches up: only new and changed files.")),
                    Beat(34, light = setOf("box"), pulse = "box" to "blob", card = BoardCard("Locked in transit", "Encrypted on the box; the newest hold 120 or 525 TB.")),
                ),
            ),
            // 35: "And what should PetPal have done before any of it?"
            Scene(
                35, "Before any of it", caf(),
                beats = listOf(
                    Beat(36, show = phases + setOf("lz", "sub", "run"), light = setOf("ready", "lz"), pulse = "ready" to "lz", card = BoardCard("Cloud Adoption Framework", "Strategy, Plan, Ready, Adopt. Ready means a landing zone.")),
                ),
            ),
            // 37: "So, the case summary for the exam?"
            Scene(
                37, "Case closed", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = all6, card = BoardCard("Five rules", "Each requirement in the case points to one answer.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-migrations--gossip",
        listOf(
            // 0: "Okay, did you hear what happened at Two Tracks this week?..."
            Scene(
                0, "The week at Two Tracks", overview(),
                beats = listOf(
                    Beat(0, show = setOf("office", "migrate"), light = setOf("office"), pulse = "office" to "migrate", card = BoardCard("Two Tracks goes global", "The office server room is being emptied into Azure.")),
                ),
            ),
            // 1: "I heard someone tried to carry a server to the car park..."
            Scene(
                1, "Monday: Priya", caf(),
                beats = listOf(
                    Beat(2, show = phases + setOf("run"), light = phases, card = BoardCard("Cloud Adoption Framework", "Strategy, Plan, Ready, Adopt; then Govern, Secure, Manage.")),
                    Beat(4, show = setOf("lz", "sub"), light = setOf("lz"), pulse = "ready" to "lz", card = BoardCard("Landing zone", "Subscriptions, networks, rules and sign-in, ready first.")),
                    Beat(6, light = setOf("lz", "sub"), pulse = "lz" to "sub", card = BoardCard("Under the picture", "Real subscriptions with policies, often from Microsoft templates.")),
                ),
            ),
            // 7: "And Tuesday?"
            Scene(
                7, "Tuesday: Tom", rs(),
                beats = listOf(
                    Beat(8, show = setOf("rehost"), light = setOf("rehost"), card = BoardCard("Rehost", "Moved exactly as it was. The Friday crash moved too.")),
                    Beat(10, show = setOf("rr"), light = setOf("rr"), card = BoardCard("Retire", "Nobody used the reports. Switch it off.")),
                    Beat(12, show = setOf("replat", "refactor", "rearch", "rebuild", "replace"), light = setOf("replat", "refactor"), card = BoardCard("Eight Rs", "Read the requirement words, not the label.")),
                ),
            ),
            // 15: "PaaS. Remind me?"
            Scene(
                15, "Platform as a service", hosting(),
                beats = listOf(
                    Beat(16, show = houses, light = setOf("paas", "pfix"), card = BoardCard("IaaS, PaaS, SaaS", "An empty house, a serviced apartment, a hotel.")),
                ),
            ),
            // 17: "Wednesday?"
            Scene(
                17, "Wednesday: the intern", discover(),
                beats = listOf(
                    Beat(18, show = setOf("servers"), light = setOf("servers"), card = BoardCard("Left behind", "The app moved; its database stayed. Ten seconds a page.")),
                    Beat(20, show = setOf("app", "migrate", "deps", "note"), light = setOf("deps"), pulse = "migrate" to "deps", card = BoardCard("Dependency analysis", "The appliance reads connections, so partners move together.")),
                ),
            ),
            // 21: "And Thursday? I'm guessing the database."
            Scene(
                21, "Thursday: Dana", database(),
                beats = listOf(
                    Beat(22, show = setOf("src", "dms"), light = setOf("dms"), pulse = "src" to "dms", card = BoardCard("Offline, on a Thursday", "The booking app shut for six hours.")),
                    Beat(24, show = setOf("note", "mi", "sqldb"), light = setOf("mi", "note"), pulse = "dms" to "mi", card = BoardCard("Online, to Managed Instance", "Downtime only at the cutover. SQL Agent jobs kept.")),
                ),
            ),
            // 27: "Friday? There's always a Friday."
            Scene(
                27, "Friday: Marcus", files(),
                beats = listOf(
                    Beat(28, show = setOf("office", "azcopy", "blob"), light = setOf("azcopy"), pulse = "azcopy" to "blob", card = BoardCard("Eight weeks remaining", "60 TB through AzCopy on a 100 Mbps line.")),
                    Beat(30, show = setOf("box", "mover", "note"), light = setOf("box", "mover"), pulse = "office" to "box", card = BoardCard("Data Box, then Storage Mover", "The bulk by van; the changes online.")),
                    Beat(32, light = setOf("box"), pulse = "box" to "blob", card = BoardCard("120 or 525 TB", "Encrypted on the box, copied into storage on arrival.")),
                ),
            ),
            // 33: "And what's next for them?"
            Scene(
                33, "What's next", overview(),
                beats = listOf(
                    Beat(34, show = setOf("office", "migrate", "decide", "iaas", "paas", "files", "sql"), light = setOf("office"), card = BoardCard("The Networks episode", "How the office reaches Azure, once the move is done.")),
                ),
            ),
            // 35: "So, the gossip I should remember for the exam?"
            Scene(
                35, "For the exam", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = all6, card = BoardCard("Listen for the words", "Each requirement points to one answer.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-migrations--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "Moving house at night", overview(),
                beats = listOf(
                    Beat(0, show = setOf("office"), light = setOf("office"), card = BoardCard("Tonight", "Moving house: from the office server room into Azure.")),
                    Beat(2, show = setOf("migrate"), light = setOf("migrate"), pulse = "office" to "migrate", card = BoardCard("A migration", "Apps and data from on-premises into Azure.")),
                ),
            ),
            // 3: "Where do you even start a move like that?"
            Scene(
                3, "The guidebook", caf(),
                beats = listOf(
                    Beat(4, show = phases + setOf("run"), light = phases, card = BoardCard("Cloud Adoption Framework", "Strategy, Plan, Ready, Adopt; Govern, Secure, Manage stay.")),
                    Beat(6, show = setOf("lz", "sub"), light = setOf("lz"), pulse = "ready" to "lz", card = BoardCard("Landing zone", "Locks fitted, lights wired, before a single box arrives.")),
                ),
            ),
            // 7: "And then you list everything you own."
            Scene(
                7, "The stocktaker", discover(),
                beats = listOf(
                    Beat(8, show = setOf("servers", "app", "migrate"), light = setOf("app"), pulse = "servers" to "app", card = BoardCard("Azure Migrate", "Its appliance reads settings, performance and connections.")),
                    Beat(10, show = setOf("assess", "deps", "note"), light = setOf("assess", "deps"), pulse = "migrate" to "deps", card = BoardCard("Assessment, dependencies", "Ready, size and cost; and who talks to whom.")),
                    Beat(12, show = setOf("case"), light = setOf("case"), pulse = "migrate" to "case", card = BoardCard("Business case", "Today's running costs beside Azure's.")),
                ),
            ),
            // 13: "Then you decide what each thing becomes."
            Scene(
                13, "What each thing becomes", rs(),
                beats = listOf(
                    Beat(14, show = setOf("rehost"), light = setOf("rehost"), card = BoardCard("Rehost", "Lift and shift. Fast, no code changes, and it fixes nothing.")),
                    Beat(16, show = setOf("replat"), light = setOf("replat"), card = BoardCard("Replatform", "Small changes onto PaaS: the serviced apartment.")),
                    Beat(20, show = otherRs, light = otherRs, card = BoardCard("And the rest", "Refactor, rearchitect, rebuild, replace, retire, retain.")),
                ),
            ),
            // 21: "Now the database. The one I worry about."
            Scene(
                21, "The database", database(),
                beats = listOf(
                    Beat(22, show = setOf("src", "dms", "note"), light = setOf("dms", "note"), pulse = "src" to "dms", card = BoardCard("DMS online", "A backup first, then the newer changes, until the cutover.")),
                    Beat(24, show = setOf("mi", "vm", "sqldb"), light = setOf("mi"), pulse = "dms" to "mi", card = BoardCard("Managed Instance", "Keeps SQL Agent. SQL Database: offline only.")),
                ),
            ),
            // 25: "And the recordings? Years of them."
            Scene(
                25, "The recordings", files(),
                beats = listOf(
                    Beat(26, show = setOf("office", "azcopy", "blob"), light = setOf("azcopy"), pulse = "azcopy" to "blob", card = BoardCard("AzCopy", "Sixty terabytes on 100 Mbps: about eight weeks.")),
                    Beat(28, show = setOf("box", "note"), light = setOf("box"), pulse = "office" to "box", card = BoardCard("Azure Data Box", "A removal van, driving through the night.")),
                    Beat(30, light = setOf("box"), pulse = "box" to "blob", card = BoardCard("Locked in transit", "Encrypted; the newest boxes hold 120 or 525 TB.")),
                    Beat(32, show = setOf("mover"), light = setOf("mover"), pulse = "mover" to "blob", card = BoardCard("Storage Mover", "Catches up with new and changed files.")),
                ),
            ),
            // 33: "Mm. Before you send me off to bed. What do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = all6, card = BoardCard("Five little things", "Each requirement points to one answer.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-migrations--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Moving in together", overview(),
                beats = listOf(
                    Beat(0, show = setOf("office"), light = setOf("office"), card = BoardCard("Tonight", "Moving in together. Into Azure.")),
                    Beat(2, show = setOf("migrate"), light = setOf("migrate"), pulse = "office" to "migrate", card = BoardCard("A migration", "From the office server room, on-premises, into Azure.")),
                ),
            ),
            // 3: "And before we carry anything, we read the guidebook..."
            Scene(
                3, "The guidebook", caf(),
                beats = listOf(
                    Beat(3, show = phases + setOf("lz", "sub", "run"), light = setOf("ready", "lz"), pulse = "ready" to "lz", card = BoardCard("Cloud Adoption Framework", "Strategy, Plan, Ready, Adopt. Ready means a landing zone.")),
                ),
            ),
            // 4: "Look at you, prepared. First question, exam style..."
            Scene(
                4, "Question one", rs(),
                beats = listOf(
                    Beat(5, show = setOf("rehost"), light = setOf("rehost"), card = BoardCard("Rehost", "An Azure VM, exactly as it is. Lift and shift.")),
                ),
            ),
            // 8: "They would. But first, the ground we land on..."
            Scene(
                8, "The ground we land on", caf(),
                beats = listOf(
                    Beat(9, show = setOf("lz", "sub", "run"), light = setOf("lz", "run"), pulse = "lz" to "sub", card = BoardCard("Under the picture", "Subscriptions, networks, policies, sign-in; often from templates.")),
                ),
            ),
            // 10: "Mm, house rules. ... So, my next question..."
            Scene(
                10, "Question two", hosting(),
                beats = listOf(
                    Beat(12, show = houses, light = setOf("paas", "pfix"), card = BoardCard("Replatform", "Small changes onto PaaS: the serviced apartment.")),
                ),
            ),
            // 15: "Fine, I'll take the apartment. And the other Rs?"
            Scene(
                15, "The other Rs", rs(),
                beats = listOf(
                    Beat(16, show = otherRs, light = otherRs, card = BoardCard("The other Rs", "Refactor, rearchitect, rebuild, replace, retire, retain.")),
                ),
            ),
            // 17: "Then my question. Before we move a thing..."
            Scene(
                17, "What's in the room", discover(),
                beats = listOf(
                    Beat(18, show = setOf("servers", "app", "migrate", "assess", "deps", "case", "note"), light = setOf("deps"), pulse = "migrate" to "deps", card = BoardCard("Azure Migrate", "Assessment: ready, size, cost. Dependencies: who talks to whom.")),
                ),
            ),
            // 20: "And under the picture, the appliance reads settings... Now, darling, the database."
            Scene(
                20, "The database", database(),
                beats = listOf(
                    Beat(20, show = setOf("src", "dms"), light = setOf("src"), pulse = "src" to "dms", card = BoardCard("The question", "SQL Agent jobs, and only a few minutes of downtime.")),
                    Beat(21, show = setOf("mi", "note"), light = setOf("mi"), pulse = "dms" to "mi", card = BoardCard("DMS online", "To Managed Instance: SQL Agent kept, a short cutover.")),
                    Beat(24, show = setOf("sqldb", "vm"), light = setOf("sqldb"), pulse = "dms" to "sqldb", card = BoardCard("Offline", "The shop shut. SQL Database: offline only, no SQL Agent.")),
                ),
            ),
            // 25: "My question, then. One hundred terabytes of archive files..."
            Scene(
                25, "A hundred terabytes", files(),
                beats = listOf(
                    Beat(25, show = setOf("office", "azcopy", "blob"), light = setOf("office"), card = BoardCard("The question", "100 TB, three weeks, a busy 100 Mbps line.")),
                    Beat(27, show = setOf("box", "note"), light = setOf("box"), pulse = "office" to "box", card = BoardCard("Azure Data Box", "About three months by wire. Ship it instead.")),
                    Beat(29, light = setOf("box"), pulse = "box" to "blob", card = BoardCard("120 or 525 TB", "Encrypted on the box, copied into storage on arrival.")),
                    Beat(31, show = setOf("mover"), light = setOf("mover"), pulse = "mover" to "blob", card = BoardCard("Storage Mover", "Only new and changed files.")),
                ),
            ),
            // 33: "Last question, for the exam. What do we remember?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = all6, card = BoardCard("Five for five", "Each requirement points to one answer.")),
                    Beat(37, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
