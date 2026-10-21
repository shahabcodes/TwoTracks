package com.shahaabapps.core.content

/** Relational data: choosing Azure SQL and its tiers: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesRelational {
    private val classic = Podcast(
        "sa-relational",
        listOf(
            maya("Hi, and welcome back. Today is relational data on Azure: where a database lives, what power it buys, and how it's protected. Our story is Two Tracks goes global. The little Android study app is a company now, with a backend on Azure, learners in Europe and Australia, and an old office system still to move."),
            leo("Big year for a study app. [chuckle] Start at the start. Relational data, in plain words?"),
            maya("Data kept in tables of rows and columns, like a tidy spreadsheet with strict rules. A learner row, a lesson row, and IDs that point from one table to the other. SQL Server is Microsoft's own database engine: the program that stores such tables and answers questions about them."),
            leo("And this is a design exam. So what am I actually designing?"),
            maya("Choices. The question gives you requirement words, like no patching, or idle at night, and you pick the home, the tier and the protection those words point to. Azure has three homes for SQL Server, from fully managed to fully yours."),
            leo("Fully managed meaning what?"),
            maya("That's PaaS, platform as a service. Think of a hotel room: cleaning and repairs are the hotel's job. Azure patches the server, takes the backups and keeps a spare ready; you only look after your data. Azure SQL Database is that: one database, managed for you."),
            leo("And fully mine?"),
            maya("IaaS, infrastructure as a service. Renting an empty house: you fix the boiler. Here you rent a virtual machine, a pretend computer carved out of a big real one in Azure's building, and run SQL Server on it yourself. You patch the operating system, Windows or Linux, and everything above it."),
            leo("Why would anyone pick the house with the broken boiler?"),
            maya("When the hotel can't give you what you need. For example, a vendor app wants full control of the operating system, or an exact SQL Server version Azure no longer offers as PaaS. Only a VM, a virtual machine, gives you that."),
            leo("And the home in the middle?"),
            maya("SQL Managed Instance. Azure still does the patching, but you get nearly all of SQL Server, close to a hundred percent compatible, inside your own private network in Azure."),
            leo("Who needs nearly all of it?"),
            maya("The Two Tracks office database. It leans on SQL Server extras. SQL Agent, a built-in alarm clock that runs jobs at night. And cross-database queries, which join tables from two databases in one question. Azure SQL Database keeps neither."),
            leo("So the office database goes to Managed Instance."),
            maya("As it is. That's lift and shift: moving house with the furniture as it is, few changes. And if an app already speaks PostgreSQL or MySQL, two popular free database engines, Azure runs those as a managed flexible server, with a cheap Burstable size for quiet databases."),
            leo("Okay, homes done. Now how do I pay for the power?"),
            maya("Azure SQL Database has two purchasing models. A DTU, a database transaction unit, is a set menu: one fixed bundle of processor, memory and disk speed. Simple, but you can't change one part alone."),
            leo("And the other?"),
            maya("vCore, short for virtual core. A core is one brain of a processor. Here you order a la carte: the processors, then the storage, separately. The exam favours vCore. It has the three modern tiers, and it lets you reuse SQL Server licences you already own."),
            leo("Three tiers. Go slowly, I'm a fresher."),
            maya("[pause] First, two words. Latency is the wait for each answer, like the gap between asking a question and hearing the reply. A replica is a live copy of the database, like a mirror; if the main one breaks, Azure switches to it."),
            leo("Got it. Tier one?"),
            maya("General Purpose. Most business apps on a budget. The data sits on fast network storage, it goes up to 4 terabytes, and no readable copy is included. Say your app is a normal school portal: General Purpose."),
            leo("Tier two?"),
            maya("Business Critical. The data sits on fast flash disks inside the server itself, so latency is the lowest. A readable replica is included, and tables can be kept in memory for speed. It's also the dearest."),
            leo("And three, the big one?"),
            maya("Hyperscale. For big or fast-growing databases and heavy reading. Storage grows to 128 terabytes, backups are snapshots, quick photos of the disk, so restores take minutes, and you can add up to 30 named replicas just for reading."),
            leo("So the Australian leaderboard, heading for 30 terabytes?"),
            maya("Hyperscale, with named replicas so the leaderboard pages read from copies. General Purpose and Business Critical both stop at 4 terabytes, so choosing Business Critical for size is a classic mistake."),
            leo("Now the part I care about. Cheap."),
            maya("[chuckle] Two tools. Serverless grows and shrinks itself and bills per second, like a taxi meter instead of owning a car. In General Purpose it can even pause when idle, so you pay only for storage. Picture a progress database, quiet all night, busy after school in Europe."),
            leo("And if it's busy all day, every day?"),
            maya("Then serverless costs more; a fixed size paid by the hour is cheaper. The second tool is an elastic pool: a shared budget of compute for many databases, like a family data plan where each phone uses the allowance at a different time."),
            leo("Like two hundred partner schools, each busy at a different hour."),
            maya("Exactly. Their peaks don't line up, so you never pay for two hundred peaks. And when one database outgrows everything, you split it across databases by a key, such as region, like one filing cabinet per country. That's sharding."),
            leo("Last part. Keeping it safe."),
            maya("GDPR, Europe's privacy law, wants personal data guarded. Start with TDE, transparent data encryption. It scrambles the files on disk, so a stolen disk is useless, and it's on by default. But anyone who queries the database still sees plain values."),
            leo("So it doesn't hide data from the people running the server?"),
            maya("No, and that's the trap. For that, Always Encrypted. The app encrypts chosen columns before sending them, like sealing a letter before the postman takes it. The keys stay with the app, so the server and its admins only see scrambled text."),
            leo("What about support staff who need a peek?"),
            maya("Dynamic data masking. Users without the unmask right see XXXX instead of the value, like a receipt showing only the last four digits. Add auditing, a visitor log of who did what."),
            leo("And keeping strangers off the line?"),
            maya("A private endpoint. An endpoint is the address your app calls, like a phone number. A private one is an internal extension inside your own network, so no traffic crosses the public internet."),
            leo("Right. So what do I remember for the exam?"),
            maya("Five things. If the question says SQL Agent or cross-database queries, Managed Instance. If it says OS access, a VM. Past 4 terabytes, Hyperscale; lowest latency, Business Critical. Idle at night, serverless; many databases with different peaks, an elastic pool. And if admins must not read it, Always Encrypted."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create a serverless General Purpose database with the az sql commands. Run a query in the portal query editor and watch the vCore use in Metrics. Add a mask on a column and query it as a user without unmask rights. Then leave it idle past the auto-pause delay and see its status change to Paused."),
            leo("A database that naps when nobody's looking. My kind of tier. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-relational",
        listOf(
            maya("Welcome to Case Files. Today's case: The Jobs That Stopped at Midnight. Two Tracks, the Android study app, has gone global: a company now, with a backend on Azure and learners in Europe and Australia. Last month they moved the old office database to the cloud. Since then, no nightly reports. Not one."),
            leo("Detective Leo, on the case. Somebody forgot to set an alarm."),
            maya("Closer than you think. First, the scene. The office database holds relational data: tables of rows and columns that point at each other through IDs, like a tidy spreadsheet with strict rules. It runs on SQL Server, Microsoft's database engine."),
            leo("And where did they move it?"),
            maya("To Azure SQL Database. That's PaaS, platform as a service: like a hotel room, where cleaning and repairs are the hotel's job. Azure patches the server, takes backups and keeps a spare ready. You just look after the data."),
            leo("Sounds perfect. My first suspect is the move itself. A bad copy. Data lost on the way."),
            maya("[pause] Wrong suspect. Every row arrived. Clue one: the reports were run by SQL Agent, a built-in alarm clock inside SQL Server that runs jobs at night. Clue two: they joined tables from two databases in one question. That's a cross-database query."),
            leo("And Azure SQL Database doesn't have the alarm clock."),
            maya("Now you're detecting. It runs neither. The office database needed SQL Managed Instance: still patched by Azure, but nearly all of SQL Server, close to a hundred percent compatible, inside your own private network."),
            leo("So the right move was lift and shift?"),
            maya("Lift and shift: moving house with the furniture as it is. What Managed Instance really is: a whole SQL Server, many databases together, that Azure runs for you. That's why the alarm clock and cross-database queries still work."),
            leo("Then why not go all the way? Put it on a virtual machine and keep everything."),
            maya("That's the other trap. A virtual machine is a pretend computer carved out of a big real one in Azure's building. SQL Server on one is IaaS, infrastructure as a service: renting an empty house, where you fix the boiler and patch the operating system. Worth it only when something needs that system, or an exact old SQL Server version."),
            leo("Case one closed. There's a second witness, isn't there?"),
            maya("The leaderboard. Australian learners love it, and it's heading for 30 terabytes. The team bought Business Critical, the fastest tier, to be safe. Last week it hit a wall at 4 terabytes."),
            leo("Suspect: they bought too small a size. Just pay for more."),
            maya("No size of Business Critical goes past 4 terabytes. First, how you pay. A DTU, a database transaction unit, is a set menu: one fixed bundle of processor, memory and disk speed. A vCore, a virtual processor core, is a la carte: processors and storage chosen separately."),
            leo("And the tiers live on the vCore side."),
            maya("Three of them. General Purpose for most apps on a budget. Business Critical for the lowest latency, meaning the shortest wait for each answer, with a readable replica, a live copy, included. And Hyperscale, for big or fast-growing data: storage grows to 128 terabytes."),
            leo("So the leaderboard needed Hyperscale."),
            maya("Hyperscale, with up to 30 named replicas just for reading, so leaderboard pages never touch the main database. Backups are snapshots, quick photos of the disk, so a restore takes minutes. How it works: Hyperscale keeps the storage apart from the processors, so storage grows on its own."),
            leo("Third witness. I can feel a bill coming."),
            maya("[chuckle] The progress database. Someone heard serverless was cheap, and switched it on. Serverless grows and shrinks itself and bills per second, like a taxi meter. In General Purpose it can pause when idle, so you only pay for storage."),
            leo("Suspect: serverless is broken."),
            maya("Serverless was fine. The database wasn't quiet. Europe by day, Australia by their day, so it never idled. A database that's busy around the clock is cheaper on a fixed size, paid by the hour. That's where serverless stops working."),
            leo("And the two hundred partner school databases they made last spring?"),
            maya("Each sized for its own busiest hour, two hundred times over. They needed an elastic pool: one shared budget of compute for many databases, like a family data plan where each phone uses the allowance at a different time. The peaks don't line up, so one pool covers them all."),
            leo("Last witness. Tell me it's not GDPR."),
            maya("It's GDPR, Europe's privacy law. Learner health notes sat in the database, and a database administrator could read them. The team insisted the data was encrypted."),
            leo("Suspect: encryption was switched off."),
            maya("It was on. TDE, transparent data encryption, scrambles the files on disk, so a stolen disk is useless. But anyone who queries the database sees plain values, administrators included. The requirement said admins must not read it. That's Always Encrypted."),
            leo("Which works how?"),
            maya("The app encrypts chosen columns before sending them, like sealing a letter before the postman takes it. The keys stay with the app, so the server only holds scrambled text. For support staff, dynamic data masking shows XXXX instead. And a private endpoint, an address only inside your own network, keeps traffic off the internet."),
            leo("Where does all this stop, though?"),
            maya("At one region, one group of Azure buildings in one part of the world; copies across regions are the high availability episode. And an app that speaks PostgreSQL or MySQL, two free database engines, goes to Azure's flexible server instead. The migrations episode covers moving day."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. SQL Agent or cross-database queries: Managed Instance. Operating system access or a pinned version: a VM. Past 4 terabytes with fast restores: Hyperscale. Idle at night: serverless; many databases with different peaks: an elastic pool. Admins must never see the values: Always Encrypted."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the scene, safely. Create a serverless General Purpose database with the az sql commands. Run a query in the portal query editor and watch the vCore use in Metrics. Add a mask on a column and query it as a user without unmask rights. Leave it idle past the auto-pause delay, and see its status change to Paused."),
            leo("No more jobs stopping at midnight. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-relational",
        listOf(
            maya("Okay, did you hear what the data team did this week? Sit down. Two Tracks has gone global: the little Android study app is a company now, with a backend on Azure and learners in Europe and Australia. This week they moved every database. And it was a week."),
            leo("[laugh] I heard someone booked a hotel for a database. What are we even moving?"),
            maya("Relational data: tables of rows and columns that point at each other through IDs, like a tidy spreadsheet with strict rules. It runs on SQL Server, Microsoft's database engine. And Azure has three homes for it."),
            leo("Three homes. Go on."),
            maya("Azure SQL Database is PaaS, platform as a service: a hotel room, where the hotel does the cleaning and repairs. SQL Server on a virtual machine, a pretend computer rented in Azure's building, is IaaS, infrastructure as a service: an empty house where you fix the boiler. In between, SQL Managed Instance."),
            leo("So who went first?"),
            maya("Tom. Monday. Tom moved the old office database to Azure SQL Database, because least admin sounded lovely. And on Monday night, nothing ran."),
            leo("Nothing?"),
            maya("The office database leaned on SQL Agent, a built-in alarm clock that runs jobs at night, and on cross-database queries, which join tables from two databases at once. Azure SQL Database keeps neither. Managed Instance does: nearly all of SQL Server, close to a hundred percent compatible, still patched by Azure."),
            leo("[sigh] Poor Tom. So Managed Instance is the lift and shift one?"),
            maya("Lift and shift, moving house with the furniture as it is. What it really is: a whole SQL Server, many databases together, run for you inside your own private network. That's why the alarm clock still rings. Tom moved it by Tuesday."),
            leo("Tuesday?"),
            maya("Dana. Dana didn't trust the hotel, so she put the new quiz database on a virtual machine. Total control, she said."),
            leo("Is that bad? Control sounds good."),
            maya("Control means chores. She now patches the operating system every month, and nothing in that database needed it. A VM is for when you truly need the operating system, or an exact old SQL Server version. Dana is quietly moving it back."),
            leo("Wednesday?"),
            maya("Marcus. The Australian leaderboard is heading for 30 terabytes. Marcus picked Business Critical, the fastest tier, because big must mean expensive. It hit a wall at 4 terabytes."),
            leo("Wait. Tiers? Back up a step."),
            maya("Two ways to pay. A DTU, a database transaction unit, is a set menu: one fixed bundle of processor, memory and disk speed. A vCore, a virtual processor core, is a la carte: processors and storage chosen separately. The exam favours vCore, because it has the three modern tiers."),
            leo("Which are?"),
            maya("General Purpose, for most apps on a budget, up to 4 terabytes. Business Critical, for the lowest latency, meaning the shortest wait for each answer, with a readable replica, a live copy, included. And Hyperscale, which grows to 128 terabytes."),
            leo("So Marcus needed Hyperscale."),
            maya("Hyperscale, with up to 30 named replicas just for reading, and snapshot backups, quick photos of the disk, so restores take minutes. Under the hood, Hyperscale keeps storage apart from the processors, so it grows on its own. Priya told him in the lift. Everybody heard."),
            leo("[chuckle] Thursday, I'm guessing, the intern."),
            maya("The intern. Bless them. They switched the progress database to serverless. Serverless grows and shrinks itself and bills per second, like a taxi meter, and in General Purpose it can pause when idle, so you pay only for storage."),
            leo("Sounds clever."),
            maya("For a quiet database. But Europe by day, Australia by their day: it never idled once, and the bill went up. Always busy is cheaper on a fixed size by the hour. Then they made two hundred partner school databases, each sized for its own peak."),
            leo("Two hundred peaks. That's a lot of peak."),
            maya("So Dana showed them the elastic pool: one shared budget of compute for many databases, like a family data plan where each phone uses the allowance at a different time. The schools' busy hours don't line up, so one pool covers them all."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. GDPR, Europe's privacy law, came up in a meeting: learner health notes. Tom said, relax, TDE is on. TDE, transparent data encryption, scrambles the files on disk so a stolen disk is useless."),
            leo("So, safe?"),
            maya("From thieves, yes. From admins, no. Anyone querying the database still sees plain values. Priya switched those columns to Always Encrypted: the app encrypts them before sending, and the keys stay with the app, so the server and its admins only see scrambled text."),
            leo("And the support desk?"),
            maya("Dynamic data masking: they see XXXX, like a receipt with only the last four digits. Plus auditing, a log of who did what, and a private endpoint, an address only inside their own network, so no traffic crosses the public internet."),
            leo("So where does all this stop working?"),
            maya("At one region, one group of Azure buildings in one part of the world; copies across regions are the high availability episode. An app that speaks PostgreSQL or MySQL, two free database engines, gets Azure's flexible server instead. And data that doesn't fit in tables is the Cosmos DB and Storage episode."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. SQL Agent or cross-database queries: Managed Instance. Operating system access: a VM. Past 4 terabytes with fast restores: Hyperscale. Idle at night: serverless; many databases with different peaks: an elastic pool. Admins must not read it: Always Encrypted."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a serverless General Purpose database with the az sql commands. Run a query in the portal query editor and watch the vCore use in Metrics. Add a mask on a column and query it as a user without unmask rights. Then leave it idle past the auto-pause delay and see its status change to Paused."),
            leo("Pick the right home, not the cosiest one. Got it, Tom. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-relational",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about where your data sleeps."),
            ivy("Mm. Somewhere comfortable, I hope. Which data, Nova?"),
            nova("Relational data. Tables of rows and columns that point at each other through IDs, like a tidy spreadsheet with strict rules. Two Tracks, the Android study app, has gone global, with a backend on Azure and learners in Europe and Australia. Its tables run on SQL Server, Microsoft's database engine."),
            ivy("And Azure gives it a home."),
            nova("Three homes. Azure SQL Database is PaaS, platform as a service. Like a hotel room: someone else does the cleaning and repairs, quietly, at night. Azure patches it, backs it up, keeps a spare ready. You only look after the data."),
            ivy("And if I want my own place?"),
            nova("[chuckle] Then a virtual machine, a pretend computer carved from a big real one in Azure's building. That's IaaS, infrastructure as a service. An empty house: total freedom, and you fix the boiler yourself. Worth it only when you need the operating system, or an exact old SQL Server version."),
            ivy("And something in between?"),
            nova("SQL Managed Instance. Nearly all of SQL Server, close to a hundred percent compatible, and Azure still patches it. [pause] It's for the old office database, which leans on SQL Agent, a little alarm clock that runs jobs at night, and on queries that join two databases at once."),
            ivy("Azure SQL Database can't do those?"),
            nova("Not those. So the office database moves to Managed Instance as it is. Lift and shift, moving house with all the furniture. Under the picture, it's a whole SQL Server with many databases, run for you inside your own private network."),
            ivy("Now, the money. How do I pay for all this?"),
            nova("Two ways. A DTU, a database transaction unit, is a set menu: one fixed bundle of processor, memory and disk speed. Or a vCore, a virtual processor core: a la carte, processors and storage chosen separately. The exam likes vCore. It has the modern tiers."),
            ivy("Tiers. Tell me slowly."),
            nova("General Purpose first: most apps, on a budget, up to 4 terabytes. Then Business Critical, for the lowest latency. Latency is the wait between your question and its answer, [pause] like waiting for a reply to a late text."),
            ivy("I know that wait. What else does Business Critical bring?"),
            nova("Fast flash disks inside the server itself, and a readable replica included. A replica is a live copy of the database, like a reflection in a window. If the main one falls, Azure switches to the copy."),
            ivy("And for something really big?"),
            nova("Hyperscale. Storage grows to 128 terabytes, backups are snapshots, quick photos of the disk, so restores take minutes, and up to 30 named replicas share the reading. The Australian leaderboard, heading for 30 terabytes, lives there. Business Critical would stop at 4."),
            ivy("Now. Some databases sleep at night. Like sensible people."),
            nova("Then serverless. It grows and shrinks itself and bills per second, like a taxi meter. In General Purpose, it can pause when idle, so you pay only for storage while it dreams. [pause] Under the hood, Azure watches its work and adds or takes away processor power as needed."),
            ivy("And if it never sleeps?"),
            nova("Then serverless costs more, and a fixed size paid by the hour is cheaper. That's where serverless stops working. And for many small databases, two hundred partner schools, each busy at a different hour, there's the elastic pool."),
            ivy("A pool. At this hour. Go on."),
            nova("A shared budget of compute for many databases. Like a family data plan, where each phone uses the allowance at a different time. Their busy hours don't line up, so you never pay for two hundred peaks."),
            ivy("Now the part that keeps me up. Who can read the learners' health notes?"),
            nova("Ah. GDPR, Europe's privacy law, cares about that too. TDE, transparent data encryption, scrambles the files on disk, so a stolen disk is useless. It's on by default. But anyone who queries the database still sees plain values. Even the administrators."),
            ivy("So how do I keep it from them?"),
            nova("Always Encrypted. The app seals chosen columns before sending them, like a letter sealed before the postman takes it. The keys stay with the app, so the server only holds scrambled text. [pause] And for support staff, dynamic data masking shows XXXX, like a receipt with only the last four digits."),
            ivy("And keeping strangers off the line entirely?"),
            nova("A private endpoint. An endpoint is the address your app calls, like a phone number. A private one is an extension only inside your own network, so nothing crosses the public internet. Add auditing, a log of who did what, and the night stays quiet."),
            ivy("Where does all of this stop working, Nova?"),
            nova("At the edge of one region, one group of Azure buildings in one part of the world. Copies across regions are the high availability episode, your next late night. And an app that speaks PostgreSQL or MySQL, two free engines, gets Azure's flexible server instead."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. SQL Agent or cross-database queries means Managed Instance. Operating system access means a VM. Past 4 terabytes with fast restores means Hyperscale. Idle at night means serverless, and many databases with different peaks, an elastic pool. Admins must never read it means Always Encrypted."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a serverless General Purpose database with the az sql commands. Run a query in the portal query editor and watch the vCore use in Metrics. [pause] Add a mask on a column, query it as a user without unmask rights, then leave it idle past the auto-pause delay and see its status change to Paused."),
            ivy("A database that sleeps when you do. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-relational",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about where our data settles down."),
            rafe("Settling down. On a first date? [laugh] Which data?"),
            nova("Relational data. Tables of rows and columns that point at each other through IDs, like a tidy spreadsheet with strict rules. Two Tracks, the Android study app, has gone global, with a backend on Azure and learners in Europe and Australia. Its tables run on SQL Server, Microsoft's database engine."),
            rafe("And Azure has three homes for it. Hotel, house, or something in between."),
            nova("Exactly. Azure SQL Database is PaaS, platform as a service: a hotel room, where Azure does the cleaning, the patching and the backups. SQL Server on a virtual machine, a pretend computer rented in Azure's building, is IaaS, infrastructure as a service: your own house, where you fix the boiler. In between, SQL Managed Instance."),
            rafe("Then my first question, exam style. The office database uses SQL Agent, a built-in alarm clock for night jobs, and queries that join two databases. It must move with few changes and no patching. Which home?"),
            nova("Managed Instance. Nearly all of SQL Server, close to a hundred percent compatible, and Azure still patches it. Lift and shift, furniture and all."),
            rafe("Right answer. You never forget the little habits. [kiss]"),
            nova("Mm. Under the picture, it's a whole SQL Server, many databases together, run for us inside our own private network. That's why the alarm clock keeps ringing."),
            rafe("My turn to be asked. Go on."),
            nova("A vendor app needs full control of the operating system, and a SQL Server version Azure no longer offers as PaaS. Where does it go?"),
            rafe("Managed Instance again. It's so accommodating."),
            nova("[sigh] No kiss for that one. Azure still owns the operating system there. Only a virtual machine hands you the keys to the whole house, and lets you pick the exact version."),
            rafe("Fine. Teach me how we pay, then."),
            nova("Two ways. A DTU, a database transaction unit, is a set menu: one fixed bundle of processor, memory and disk speed. A vCore, a virtual processor core, is a la carte: processors and storage chosen separately. The exam prefers a la carte. More choice, like you."),
            rafe("And the vCore tiers. Something general, something critical, something huge."),
            nova("General Purpose for most apps, up to 4 terabytes. Business Critical for the lowest latency, meaning the shortest wait for each answer, with a readable replica, a live copy, included. And Hyperscale, which grows to 128 terabytes."),
            rafe("Then here's one. The Australian leaderboard will reach 30 terabytes, serves many readers and needs restores in minutes. Which tier?"),
            nova("Hyperscale. Snapshot backups, quick photos of the disk, so restores take minutes, and up to 30 named replicas for the readers. Business Critical stops at 4 terabytes."),
            rafe("Right answer. Size does matter, sometimes. [kiss]"),
            nova("[chuckle] Behave. Now mine. The progress database is quiet all night and busy after school in Europe. Budget tight. Which compute?"),
            rafe("Serverless. It grows and shrinks itself, bills per second, like a taxi meter, and in General Purpose it pauses when idle, so we only pay for storage while it sleeps."),
            nova("Right answer. Pay only for the hours you're awake. [kiss]"),
            rafe("And where does serverless stop working?"),
            nova("When the database never sleeps. Busy around the clock, it's cheaper on a fixed size paid by the hour. And two hundred partner school databases, each busy at a different hour, want an elastic pool."),
            rafe("A pool party. Explain."),
            nova("A shared budget of compute for many databases. Like a family data plan, each phone using the allowance at a different time. Their peaks don't line up, so you never pay for two hundred peaks at once."),
            rafe("Now the serious one. GDPR, Europe's privacy law. Learner health notes must stay unreadable to the administrators who run the server. Which feature?"),
            nova("Transparent data encryption?"),
            rafe("Close, but no kiss. TDE scrambles the files on disk, so a stolen disk is useless, but anyone who queries still sees plain values. You want Always Encrypted: the app seals the columns before sending them, and the keys stay with the app, so the server only holds scrambled text."),
            nova("Mm. Some secrets you keep even from the staff. And if support should see only the last four digits?"),
            rafe("Dynamic data masking. They see XXXX instead of the value, like a receipt."),
            nova("Right answer. A little mystery keeps things interesting. [kiss]"),
            rafe("And keeping strangers off our line?"),
            nova("A private endpoint. An endpoint is the address your app calls, like a phone number. A private one is an extension inside your own network, so nothing crosses the public internet. And auditing keeps a log of who did what."),
            rafe("Where does it all stop working? Every romance has its limits."),
            nova("At one region, one group of Azure buildings in one part of the world; copies across regions are the high availability episode. And an app that speaks PostgreSQL or MySQL, two free engines, gets Azure's flexible server instead, with read-only copies of its own."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("SQL Agent or cross-database queries: Managed Instance. Operating system access: a VM. Past 4 terabytes, fast restores: Hyperscale. Idle at night: serverless; many databases, different peaks: an elastic pool. Admins must not read it: Always Encrypted."),
            rafe("Five for five. That one earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a serverless General Purpose database with the az sql commands. Run a query in the portal query editor and watch the vCore use in Metrics. Add a mask on a column and query it as a user without unmask rights. Then leave it idle past the auto-pause delay and see its status change to Paused."),
            nova("And you're the one database I'd never let pause. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun homes() = DiagramSpec(
        "Three homes for SQL Server: moving right buys control and costs you more work.",
        nodes = listOf(
            DNode("app", "Two Tracks backend", 0.5f, 0.12f, w = 0.36f, h = 0.18f, kind = NodeKind.USER),
            DNode("db", "Azure SQL Database", 0.17f, 0.5f, w = 0.3f, h = 0.3f, sub = "PaaS: a hotel room"),
            DNode("mi", "SQL Managed Instance", 0.5f, 0.5f, w = 0.3f, h = 0.3f, tone = 1, sub = "SQL Agent, cross-DB"),
            DNode("vm", "SQL Server on a VM", 0.83f, 0.5f, w = 0.3f, h = 0.3f, tone = 2, sub = "IaaS: your own house"),
            DNode("note", "Left: least work. Right: most control, most chores.", 0.5f, 0.86f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("app", "db"),
            DEdge("app", "mi"),
            DEdge("app", "vm"),
        ),
        aspect = 0.8f,
    )

    private fun buying() = DiagramSpec(
        "Pay by DTU, a set menu, or by vCore, a la carte; vCore holds the three modern tiers.",
        nodes = listOf(
            DNode("dtu", "DTU", 0.25f, 0.15f, w = 0.42f, h = 0.2f, tone = 2, sub = "a set menu"),
            DNode("vcore", "vCore", 0.75f, 0.15f, w = 0.42f, h = 0.2f, sub = "a la carte"),
            DNode("gp", "General Purpose", 0.17f, 0.52f, w = 0.3f, h = 0.28f, tone = 1, sub = "most apps, 4 TB"),
            DNode("bc", "Business Critical", 0.5f, 0.52f, w = 0.3f, h = 0.28f, tone = 1, sub = "low latency, replica"),
            DNode("hs", "Hyperscale", 0.83f, 0.52f, w = 0.3f, h = 0.28f, tone = 1, sub = "128 TB, 30 replicas"),
            DNode("note", "Past 4 TB with fast restores: Hyperscale", 0.5f, 0.87f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("vcore", "gp"),
            DEdge("vcore", "bc"),
            DEdge("vcore", "hs"),
        ),
        aspect = 0.8f,
    )

    private fun cost() = DiagramSpec(
        "Serverless suits a database that idles; a busy one wants a fixed size; many databases with different peaks share an elastic pool.",
        nodes = listOf(
            DNode("sl", "Serverless", 0.25f, 0.15f, w = 0.42f, h = 0.22f, sub = "per second, pauses"),
            DNode("prov", "Fixed size", 0.75f, 0.15f, w = 0.42f, h = 0.22f, tone = 2, sub = "busy all day"),
            DNode("pool", "Elastic pool", 0.5f, 0.52f, w = 0.5f, h = 0.22f, tone = 1, sub = "one shared budget"),
            DNode("s1", "School 1", 0.17f, 0.87f, w = 0.28f, h = 0.16f),
            DNode("s2", "School 2", 0.5f, 0.87f, w = 0.28f, h = 0.16f),
            DNode("s3", "School 200", 0.83f, 0.87f, w = 0.28f, h = 0.16f),
        ),
        edges = listOf(
            DEdge("pool", "s1"),
            DEdge("pool", "s2"),
            DEdge("pool", "s3"),
        ),
        aspect = 0.8f,
    )

    private fun protect() = DiagramSpec(
        "TDE guards the files on disk, Always Encrypted hides columns even from admins, masking hides values from staff; a private endpoint keeps traffic inside.",
        nodes = listOf(
            DNode("app", "Two Tracks backend", 0.5f, 0.12f, w = 0.5f, h = 0.18f, tone = 1, sub = "holds the keys"),
            DNode("db", "Database", 0.5f, 0.45f, w = 0.4f, h = 0.22f, kind = NodeKind.CYLINDER),
            DNode("tde", "TDE", 0.17f, 0.82f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "files at rest"),
            DNode("ae", "Always Encrypted", 0.5f, 0.82f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "admins see nothing"),
            DNode("mask", "Masking", 0.83f, 0.82f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "XXXX for staff"),
        ),
        edges = listOf(
            DEdge("app", "db", "private endpoint"),
            DEdge("db", "tde"),
            DEdge("db", "ae"),
            DEdge("db", "mask"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where these homes stop: one region, other engines, and serverless on a database that never idles.",
        nodes = listOf(
            DNode("db", "Azure SQL", 0.25f, 0.17f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "one region"),
            DNode("ha", "High availability", 0.75f, 0.17f, w = 0.42f, h = 0.24f, kind = NodeKind.NOTE, sub = "copies across regions"),
            DNode("pg", "PostgreSQL, MySQL", 0.25f, 0.53f, w = 0.42f, h = 0.24f, tone = 1, sub = "flexible server"),
            DNode("mig", "Migrations", 0.75f, 0.53f, w = 0.42f, h = 0.24f, kind = NodeKind.NOTE, sub = "moving day"),
            DNode("note", "Busy all day? A fixed size beats serverless", 0.5f, 0.87f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("db", "ha", "next episode"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's relational questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("agent", "SQL Agent, cross-DB", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Managed Instance"),
            DNode("os", "OS access", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a VM"),
            DNode("big", "Past 4 TB", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Hyperscale"),
            DNode("idle", "Idle at night", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "serverless"),
            DNode("peaks", "Many DBs, own peaks", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "elastic pool"),
            DNode("admins", "Admins must not read", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Always Encrypted"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("agent", "os", "big", "idle", "peaks", "admins")
    private val allLimits = setOf("db", "ha", "pg", "mig", "note")
    private val schools = setOf("pool", "s1", "s2", "s3")
    private val today = BoardCard("Today", "A serverless database, a query in Metrics, a mask, then Paused.")
    private val relational = BoardCard("Relational data", "Tables of rows and columns that point at each other by ID.")
    private val liftShift = BoardCard("Lift and shift", "Moving house with the furniture as it is: few changes.")
    private val dtuVcore = BoardCard("DTU or vCore", "A set menu of power, or a la carte: processors and storage apart.")
    private val serverless = BoardCard("Serverless", "Grows, shrinks, bills per second; pauses when idle.")
    private val pool = BoardCard("Elastic pool", "A family data plan: many databases, one shared budget.")
    private val alwaysEncrypted = BoardCard("Always Encrypted", "The app seals the columns; keys stay with it. Admins see nothing.")
    private val endpoint = BoardCard("Private endpoint", "An address inside your own network. No public internet.")
    private val edgesCard = BoardCard("Where it stops", "One region; PostgreSQL and MySQL go to flexible server.")

    private fun classicBoard() = PodcastBoard(
        "sa-relational",
        listOf(
            // 0: "Hi, and welcome back. Today is relational data on Azure..."
            Scene(
                0, "Three homes for SQL Server", homes(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("Two Tracks goes global", "A company now: Europe, Australia, an office system to move.")),
                    Beat(2, card = relational),
                    Beat(4, card = BoardCard("A design exam", "Hear the requirement words; pick the home, tier and protection.")),
                    Beat(6, show = setOf("db"), light = setOf("db"), pulse = "app" to "db", card = BoardCard("PaaS: a hotel room", "Azure patches, backs up and keeps a spare. You mind the data.")),
                    Beat(8, show = setOf("vm"), light = setOf("vm"), pulse = "app" to "vm", card = BoardCard("IaaS: your own house", "A rented virtual machine. You patch the operating system.")),
                    Beat(10, light = setOf("vm"), card = BoardCard("Only a VM", "Full OS control, or an exact old SQL Server version.")),
                    Beat(12, show = setOf("mi", "note"), light = setOf("mi"), pulse = "app" to "mi", card = BoardCard("SQL Managed Instance", "Nearly all of SQL Server, still patched by Azure.")),
                    Beat(14, card = BoardCard("SQL Agent", "A built-in alarm clock for night jobs; and cross-database queries.")),
                    Beat(16, card = liftShift),
                ),
            ),
            // 17: "Okay, homes done. Now how do I pay for the power?"
            Scene(
                17, "Paying for power", buying(),
                beats = listOf(
                    Beat(18, show = setOf("dtu"), light = setOf("dtu"), card = BoardCard("DTU", "A set menu: one fixed bundle of processor, memory and disk.")),
                    Beat(20, show = setOf("vcore"), light = setOf("vcore"), card = BoardCard("vCore", "A la carte: processors and storage chosen separately.")),
                    Beat(22, card = BoardCard("Two words", "Latency: the wait for each answer. A replica: a live copy.")),
                    Beat(24, show = setOf("gp"), light = setOf("gp"), pulse = "vcore" to "gp", card = BoardCard("General Purpose", "Most apps on a budget: network storage, up to 4 TB.")),
                    Beat(26, show = setOf("bc"), light = setOf("bc"), pulse = "vcore" to "bc", card = BoardCard("Business Critical", "Lowest latency: flash disks inside, a readable replica.")),
                    Beat(28, show = setOf("hs"), light = setOf("hs"), pulse = "vcore" to "hs", card = BoardCard("Hyperscale", "Up to 128 TB, snapshot backups, up to 30 named replicas.")),
                    Beat(30, show = setOf("note"), light = setOf("hs", "note"), card = BoardCard("The leaderboard", "Heading for 30 TB: Hyperscale. The others stop at 4 TB.")),
                ),
            ),
            // 31: "Now the part I care about. Cheap."
            Scene(
                31, "Cutting the cost", cost(),
                beats = listOf(
                    Beat(32, show = setOf("sl"), light = setOf("sl"), card = serverless),
                    Beat(34, show = schools + "prov", light = setOf("prov", "pool"), card = pool),
                    Beat(36, light = setOf("pool"), pulse = "pool" to "s3", card = BoardCard("Sharding", "Split the data across databases by a key, such as region.")),
                ),
            ),
            // 37: "Last part. Keeping it safe."
            Scene(
                37, "Keeping it safe", protect(),
                beats = listOf(
                    Beat(38, show = setOf("app", "db", "tde"), light = setOf("tde"), card = BoardCard("TDE", "Scrambles the files on disk. On by default. Queries see plain values.")),
                    Beat(40, show = setOf("ae"), light = setOf("ae"), card = alwaysEncrypted),
                    Beat(42, show = setOf("mask"), light = setOf("mask"), card = BoardCard("Dynamic data masking", "XXXX for users without the unmask right; auditing logs who did what.")),
                    Beat(44, light = setOf("app", "db"), pulse = "app" to "db", card = endpoint),
                ),
            ),
            // 45: "Right. So what do I remember for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = all6, card = BoardCard("Listen for the phrase", "Each requirement points to one home, tier or feature.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-relational--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Jobs That Stopped at Midnight..."
            Scene(
                0, "The Jobs That Stopped", homes(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("The case", "No nightly reports since the office database moved.")),
                    Beat(2, card = relational),
                    Beat(4, show = setOf("db"), light = setOf("db"), pulse = "app" to "db", card = BoardCard("Azure SQL Database", "PaaS: a hotel room. Azure patches, backs up, keeps a spare.")),
                    Beat(6, light = setOf("db"), card = BoardCard("Clues one and two", "SQL Agent night jobs, and queries across two databases.")),
                    Beat(8, show = setOf("mi"), light = setOf("mi"), pulse = "app" to "mi", card = BoardCard("SQL Managed Instance", "Nearly all of SQL Server, still patched by Azure.")),
                    Beat(10, card = BoardCard("Lift and shift", "A whole SQL Server, many databases, run for you.")),
                    Beat(12, show = setOf("vm", "note"), light = setOf("vm"), pulse = "app" to "vm", card = BoardCard("A VM?", "IaaS: you patch the OS. Only when something needs it.")),
                ),
            ),
            // 13: "Case one closed. There's a second witness, isn't there?"
            Scene(
                13, "The second witness", buying(),
                beats = listOf(
                    Beat(14, show = setOf("bc"), light = setOf("bc"), card = BoardCard("Business Critical", "Bought for size. It hit a wall at 4 TB.")),
                    Beat(16, show = setOf("dtu", "vcore"), light = setOf("dtu", "vcore"), card = dtuVcore),
                    Beat(18, show = setOf("gp", "hs"), light = setOf("gp", "bc", "hs"), pulse = "vcore" to "bc", card = BoardCard("Three tiers", "Latency: the wait for each answer. A replica: a live copy.")),
                    Beat(20, show = setOf("note"), light = setOf("hs"), pulse = "vcore" to "hs", card = BoardCard("Hyperscale", "128 TB, snapshots, 30 named replicas; storage kept apart.")),
                ),
            ),
            // 21: "Third witness. I can feel a bill coming."
            Scene(
                21, "The third witness", cost(),
                beats = listOf(
                    Beat(22, show = setOf("sl"), light = setOf("sl"), card = serverless),
                    Beat(24, show = setOf("prov"), light = setOf("prov"), card = BoardCard("Never idle", "Busy around the clock: a fixed size by the hour is cheaper.")),
                    Beat(26, show = schools, light = setOf("pool"), pulse = "pool" to "s2", card = BoardCard("Elastic pool", "Two hundred schools, peaks that don't line up, one budget.")),
                ),
            ),
            // 27: "Last witness. Tell me it's not GDPR."
            Scene(
                27, "The last witness", protect(),
                beats = listOf(
                    Beat(28, show = setOf("app", "db"), light = setOf("db"), card = BoardCard("Health notes", "A database administrator could read them.")),
                    Beat(30, show = setOf("tde"), light = setOf("tde"), card = BoardCard("TDE was on", "It guards files at rest. Queries still see plain values.")),
                    Beat(32, show = setOf("ae", "mask"), light = setOf("ae", "mask"), pulse = "app" to "db", card = alwaysEncrypted),
                ),
            ),
            // 33: "Where does all this stop, though?"
            Scene(
                33, "Where it stops", limits(),
                beats = listOf(
                    Beat(34, show = allLimits, light = setOf("db", "pg"), pulse = "db" to "ha", card = edgesCard),
                ),
            ),
            // 35: "So, the case summary for the exam?"
            Scene(
                35, "Case closed", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = all6, card = BoardCard("Listen for the phrase", "Agent, OS, size, idle, peaks, admins: one answer each.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-relational--gossip",
        listOf(
            // 0: "Okay, did you hear what the data team did this week?..."
            Scene(
                0, "The data team's week", homes(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("This week", "Two Tracks moved every database. It was a week.")),
                    Beat(2, card = relational),
                    Beat(4, show = setOf("db", "mi", "vm"), light = setOf("db", "mi", "vm"), card = BoardCard("Three homes", "A hotel room, a managed middle, or your own house.")),
                    Beat(6, light = setOf("db"), pulse = "app" to "db", card = BoardCard("Monday: Tom", "The office database to Azure SQL Database. Nothing ran.")),
                    Beat(8, light = setOf("mi"), pulse = "app" to "mi", card = BoardCard("SQL Agent", "The night-job alarm clock: Managed Instance keeps it.")),
                    Beat(10, card = liftShift),
                    Beat(12, light = setOf("vm"), pulse = "app" to "vm", card = BoardCard("Tuesday: Dana", "The quiz database on a VM, for total control.")),
                    Beat(14, show = setOf("note"), light = setOf("vm", "note"), card = BoardCard("Control means chores", "She patches the OS monthly, and nothing needed it.")),
                ),
            ),
            // 15: "Wednesday?"
            Scene(
                15, "Wednesday: Marcus", buying(),
                beats = listOf(
                    Beat(16, show = setOf("bc"), light = setOf("bc"), card = BoardCard("Business Critical", "Picked for size. A wall at 4 TB.")),
                    Beat(18, show = setOf("dtu", "vcore"), light = setOf("dtu", "vcore"), card = dtuVcore),
                    Beat(20, show = setOf("gp", "hs"), light = setOf("gp", "bc", "hs"), pulse = "vcore" to "gp", card = BoardCard("Three tiers", "Latency: the wait for each answer. A replica: a live copy.")),
                    Beat(22, show = setOf("note"), light = setOf("hs", "note"), pulse = "vcore" to "hs", card = BoardCard("Hyperscale", "128 TB, snapshots, 30 named replicas. Storage kept apart.")),
                ),
            ),
            // 23: "Thursday, I'm guessing, the intern."
            Scene(
                23, "Thursday: the intern", cost(),
                beats = listOf(
                    Beat(24, show = setOf("sl"), light = setOf("sl"), card = serverless),
                    Beat(26, show = setOf("prov"), light = setOf("prov"), card = BoardCard("It never idled", "Always busy: a fixed size by the hour is cheaper.")),
                    Beat(28, show = schools, light = setOf("pool"), pulse = "pool" to "s3", card = pool),
                ),
            ),
            // 29: "And Friday? There's always a Friday."
            Scene(
                29, "Friday: Priya", protect(),
                beats = listOf(
                    Beat(30, show = setOf("app", "db", "tde"), light = setOf("tde"), card = BoardCard("Relax, TDE is on", "It guards files on disk. Not values from admins.")),
                    Beat(32, show = setOf("ae"), light = setOf("ae"), card = alwaysEncrypted),
                    Beat(34, show = setOf("mask"), light = setOf("mask", "app"), pulse = "app" to "db", card = BoardCard("Masking and endpoint", "XXXX for support; a private endpoint keeps it off the internet.")),
                ),
            ),
            // 35: "So where does all this stop working?"
            Scene(
                35, "Where it stops", limits(),
                beats = listOf(
                    Beat(36, show = allLimits, light = setOf("db", "pg"), pulse = "db" to "ha", card = edgesCard),
                ),
            ),
            // 37: "So, the gossip I should remember for the exam?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = all6, card = BoardCard("Listen for the phrase", "One colleague, one lesson, one answer each.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-relational--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Where data sleeps", homes(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Where your data sleeps.")),
                    Beat(2, show = setOf("app"), card = relational),
                    Beat(4, show = setOf("db"), light = setOf("db"), pulse = "app" to "db", card = BoardCard("PaaS: a hotel room", "Azure patches, backs up and keeps a spare, quietly.")),
                    Beat(6, show = setOf("vm"), light = setOf("vm"), pulse = "app" to "vm", card = BoardCard("IaaS: an empty house", "Total freedom; you patch the operating system.")),
                    Beat(8, show = setOf("mi", "note"), light = setOf("mi"), pulse = "app" to "mi", card = BoardCard("SQL Managed Instance", "SQL Agent and cross-database queries still work.")),
                    Beat(10, card = liftShift),
                ),
            ),
            // 11: "Now, the money. How do I pay for all this?"
            Scene(
                11, "Paying for power", buying(),
                beats = listOf(
                    Beat(12, show = setOf("dtu", "vcore"), light = setOf("dtu", "vcore"), card = dtuVcore),
                    Beat(14, show = setOf("gp", "bc"), light = setOf("bc"), pulse = "vcore" to "bc", card = BoardCard("Latency", "The wait between your question and its answer.")),
                    Beat(16, light = setOf("bc"), card = BoardCard("Replica", "A live copy; if the main one falls, Azure switches to it.")),
                    Beat(18, show = setOf("hs", "note"), light = setOf("hs"), pulse = "vcore" to "hs", card = BoardCard("Hyperscale", "128 TB, snapshots, 30 named replicas for the readers.")),
                ),
            ),
            // 19: "Now. Some databases sleep at night."
            Scene(
                19, "Databases that sleep", cost(),
                beats = listOf(
                    Beat(20, show = setOf("sl"), light = setOf("sl"), card = serverless),
                    Beat(22, show = setOf("prov"), light = setOf("prov"), card = BoardCard("If it never sleeps", "A fixed size by the hour is cheaper than serverless.")),
                    Beat(24, show = schools, light = setOf("pool"), pulse = "pool" to "s1", card = pool),
                ),
            ),
            // 25: "Now the part that keeps me up..."
            Scene(
                25, "Who can read it", protect(),
                beats = listOf(
                    Beat(26, show = setOf("app", "db", "tde"), light = setOf("tde"), card = BoardCard("TDE", "Files on disk scrambled. Queries still see plain values.")),
                    Beat(28, show = setOf("ae", "mask"), light = setOf("ae", "mask"), card = alwaysEncrypted),
                    Beat(30, light = setOf("app", "db"), pulse = "app" to "db", card = endpoint),
                ),
            ),
            // 31: "Where does all of this stop working, Nova?"
            Scene(
                31, "Where it stops", limits(),
                beats = listOf(
                    Beat(32, show = allLimits, light = setOf("db", "pg"), pulse = "db" to "ha", card = edgesCard),
                ),
            ),
            // 33: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = all6, card = BoardCard("Listen for the phrase", "Five little things, one answer each.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-relational--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", homes(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Where our data settles down.")),
                    Beat(2, show = setOf("app"), card = relational),
                    Beat(4, show = setOf("db", "vm"), light = setOf("db", "vm"), card = BoardCard("Hotel or house", "PaaS: Azure does the chores. IaaS: you fix the boiler.")),
                    Beat(5, show = setOf("mi"), light = setOf("mi"), card = BoardCard("Quiz: the office database", "SQL Agent, cross-database queries, few changes. Which home?")),
                    Beat(6, pulse = "app" to "mi", light = setOf("mi"), card = BoardCard("Right answer", "Managed Instance: near-full SQL Server, patched by Azure.")),
                    Beat(10, light = setOf("vm"), card = BoardCard("Quiz: the vendor app", "Full OS control and an old SQL Server version. Where?")),
                    Beat(12, show = setOf("note"), light = setOf("vm"), pulse = "app" to "vm", card = BoardCard("No kiss: a VM", "Only a VM hands you the OS and the exact version.")),
                ),
            ),
            // 13: "Fine. Teach me how we pay, then."
            Scene(
                13, "Something huge", buying(),
                beats = listOf(
                    Beat(14, show = setOf("dtu", "vcore"), light = setOf("dtu", "vcore"), card = dtuVcore),
                    Beat(16, show = setOf("gp", "bc", "hs"), light = setOf("gp", "bc", "hs"), card = BoardCard("Three tiers", "Latency: the wait for each answer. A replica: a live copy.")),
                    Beat(17, light = setOf("hs"), card = BoardCard("Quiz: the leaderboard", "30 TB, many readers, restores in minutes. Which tier?")),
                    Beat(18, show = setOf("note"), light = setOf("hs", "note"), pulse = "vcore" to "hs", card = BoardCard("Right answer", "Hyperscale: snapshots and up to 30 named replicas.")),
                ),
            ),
            // 20: "Behave. Now mine. The progress database is quiet all night..."
            Scene(
                20, "Awake hours", cost(),
                beats = listOf(
                    Beat(20, show = setOf("sl"), card = BoardCard("Quiz: quiet at night", "Busy after school, tight budget. Which compute?")),
                    Beat(21, light = setOf("sl"), card = BoardCard("Right answer", "Serverless: bills per second, pauses when idle.")),
                    Beat(24, show = schools + "prov", light = setOf("prov", "pool"), pulse = "pool" to "s1", card = BoardCard("When it never sleeps", "A fixed size. Many databases, different peaks: a pool.")),
                ),
            ),
            // 27: "Now the serious one. GDPR..."
            Scene(
                27, "Secrets from the staff", protect(),
                beats = listOf(
                    Beat(27, show = setOf("app", "db"), light = setOf("db"), card = BoardCard("Quiz: health notes", "Unreadable to the administrators. Which feature?")),
                    Beat(28, show = setOf("tde"), light = setOf("tde"), card = BoardCard("TDE?", "Files on disk only. Close, but no kiss.")),
                    Beat(29, show = setOf("ae"), light = setOf("ae"), card = alwaysEncrypted),
                    Beat(31, show = setOf("mask"), light = setOf("mask"), card = BoardCard("Right answer", "Dynamic data masking: XXXX instead of the value.")),
                    Beat(34, light = setOf("app", "db"), pulse = "app" to "db", card = endpoint),
                ),
            ),
            // 35: "Where does it all stop working? Every romance has its limits."
            Scene(
                35, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(36, show = allLimits, light = setOf("db", "pg"), pulse = "db" to "ha", card = edgesCard),
                ),
            ),
            // 37: "Last question, for the exam. What do we remember?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = all6, card = BoardCard("Listen for the phrase", "Five for five: one answer each.")),
                    Beat(41, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
