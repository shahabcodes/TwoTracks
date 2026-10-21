package com.shahaabapps.core.content

/** High availability: zones, regions and replicas: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesHighAvailability {
    private val classic = Podcast(
        "sa-high-availability",
        listOf(
            maya("Hi, and welcome back. Today is high availability, a design topic, so we'll practise choosing. The story: Two Tracks goes global. The little Android app is now a company, with its backend on Azure, users in Europe and Australia, and a promise that the app stays up."),
            leo("High availability. Sounds like me on a Saturday. What does it mean, in plain words?"),
            maya("[chuckle] It means the app keeps running when a part breaks, with nobody having to step in. HA for short. The trick is always the same: more than one copy, kept in places that don't fail together. Like keeping spare keys in two pockets, then two bags, then two houses."),
            leo("And each step costs more, I'm guessing."),
            maya("Exactly, and each survives a bigger accident. The exam describes the accident; you pick the cheapest step that survives it. First, one word you'll hear everywhere: SLA."),
            leo("SLA. What's that?"),
            maya("A service level agreement. It's Microsoft's uptime promise for one service, as a percentage. Picture a train company promising that 9,999 trains in 10,000 run. That's 99.99 percent."),
            leo("Okay. So where do the copies go? Start with the cheapest pocket."),
            maya("First, a VM, a virtual machine: a computer you rent by the hour in Azure's datacenter. A datacenter is a big building full of those computers, standing in racks, which are tall shelves of servers."),
            leo("And the cheapest pocket puts my VMs on different shelves?"),
            maya("That's an availability set. It spreads VMs over fault domains, racks with their own power and network switch, so one rack failing takes one VM, not all. And over update domains, groups Azure patches one at a time. With two VMs or more, the SLA is 99.95 percent."),
            leo("So what if the whole building catches fire?"),
            maya("[pause] Then the availability set burns with it. That's the trap. For a building you need availability zones. A region is an area of the world where Azure has datacenters, like West Europe. Many regions have three zones."),
            leo("And a zone is, what, one building?"),
            maya("A zone is one or more datacenters with their own power, cooling and network, a few kilometres from the others. Like houses on different streets. Two VMs or more across zones get 99.99 percent, and Microsoft recommends zones over sets wherever the region has them."),
            leo("And if the whole region goes down? A really bad day for West Europe?"),
            maya("Then you need a second region, like North Europe: two houses in two cities. Azure links some regions as a region pair, partners for example for copied storage."),
            leo("Racks, buildings, regions. Got it. Now, my app uses two services. What's my SLA?"),
            maya("Take the Two Tracks API. An API is the door your phone app knocks on to ask for data. Say it runs on App Service, Azure's web hosting, at 99.95 percent, and calls SQL Database at 99.99. Every request needs both. That's a chain, and the SLA of a chain is called the composite SLA."),
            leo("So I take the lower one? Or the average?"),
            maya("Neither. You multiply. Small numbers first. Picture two services that are each up 9 days in 10. That's 90 percent, or 0.9."),
            leo("Okay. Two services, each up 0.9."),
            maya("In a chain, both must be up at once. 0.9 times 0.9 is 0.81. So the chain is up 81 percent of the time, lower than either part."),
            leo("Ouch. Adding a service made it worse."),
            maya("Every link is one more thing that can break. Now flip it: two copies side by side, so either can answer. The pair only fails when both fail. Each is down 0.1 of the time, and 0.1 times 0.1 is 0.01. So the pair is up 99 percent."),
            leo("From 90 to 99, just by doubling up. And the real numbers?"),
            maya("0.9995 times 0.9999 is about 0.9994, so the composite SLA is about 99.94 percent. A chain multiplies you down; copies side by side lift you up."),
            leo("So one big VM is out?"),
            maya("A classic trap. A single VM on Premium SSD, a fast disk, has an SLA of 99.9 percent, but there's no second copy at all. If the question says survive a failure, one VM never qualifies."),
            leo("So how do I actually run my API across zones?"),
            maya("With a virtual machine scale set, a group of identical VMs that Azure adds or removes as load changes, spread over zones 1, 2 and 3. In front goes a zone-redundant load balancer. That's a traffic spreader with a desk in every zone, like a receptionist in each building."),
            leo("And if I'm on App Service, not VMs?"),
            maya("App Service has a zone-redundant switch too. And for two regions, a global entry point such as Azure Front Door sends users to the healthy region. The networks episode compares those."),
            leo("Now the database. That's where the data actually lives."),
            maya("SQL Database already keeps spare copies inside one datacenter. Each one is a replica, which is just an identical copy, like a photocopy. Turn on zone redundancy and the replicas sit in different zones. On Business Critical, the fastest tier, meaning its top price level, that's 99.995 percent."),
            leo("And when a whole region fails?"),
            maya("You copy the database to another region, in one of two ways. Active geo-replication gives one database readable copies elsewhere, and you fail over by hand. Fail over means switching the app to the copy. Afterwards the app must use a new server name."),
            leo("That sounds painful with five databases."),
            maya("Very. So the other way is a failover group. Several databases fail over together, and the app keeps one connection string, the address it uses to reach the database. The group's listener is a fixed name, like a forwarding phone number that always rings the live copy."),
            leo("So, say your app has five databases: failover group."),
            maya("Exactly. Now the progress store. Two Tracks keeps workout progress in Azure Cosmos DB, a database that copies itself to every region you add. Picture a notice board copied to several offices."),
            leo("Users in Europe and Australia both log workouts. Can both offices pin up notes?"),
            maya("Yes, with multi-region writes. Each user writes to the nearby region, which cuts latency, the wait while data crosses the world. And the SLA reaches 99.999 percent. The price is consistency."),
            leo("Consistency, in plain words?"),
            maya("How fresh a read is promised to be. Strong means every read sees the very latest write, but it can't be combined with multi-region writes, because every region would have to agree first. Session, the default, means each user always sees their own writes. That's the one here."),
            leo("And the photos and audio? Where do those go?"),
            maya("In Azure Storage, Azure's file store, where you choose the redundancy: how many copies, and where. LRS keeps three copies in one datacenter. ZRS keeps three across zones, so a zone can fail with no data loss. GRS adds the paired region. GZRS is both."),
            leo("And the RA in front of some of them?"),
            maya("Read access. With RA-GRS or RA-GZRS you can read the far copy even before a failover; plain GRS hides it until then. And region copies are sent a moment later, so a regional failover can lose a little data."),
            leo("So what do I remember for the exam?"),
            maya("If the question says survive a rack failure, cheapest, the answer is an availability set. Survive a datacenter: availability zones. Services in a chain: multiply their SLAs for the composite SLA. Many databases failing over with one connection string: a failover group. Writes in Europe and Australia at the top SLA: Cosmos DB multi-region writes with session consistency."),
            leo("And anything to try today?"),
            maya("Create a Flexible scale set across zones 1, 2 and 3, and check each VM's zone in the portal. Look up the SLA for App Service and SQL Database and work out their composite SLA. Then open a storage account's Redundancy page and see which options your region offers."),
            leo("Spare keys in two houses, and multiply the chain. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-high-availability",
        listOf(
            maya("Welcome to Case Files. Today's case: The Promise That Burned Down. Two Tracks has gone global: the Android app's backend runs on Azure, users are in Europe and Australia, and the company promised the app stays up. Then one night a building in West Europe lost power, and the app went dark."),
            leo("[chuckle] Detective Leo, reporting. Before the suspects: what exactly did they promise?"),
            maya("An SLA, a service level agreement. That's an uptime promise as a percentage, like a train company promising 9,999 trains in 10,000 run. Two Tracks promised 99.99 percent, and Microsoft gives an SLA like that for each of its services."),
            leo("And what broke? One building?"),
            maya("One datacenter, a big building full of rented computers in racks, tall shelves of servers. The API ran there. The API is the door the phone app knocks on for data. It ran on two VMs, virtual machines, which are computers you rent by the hour."),
            leo("Two VMs and still dark. First suspect: the VMs were too small."),
            maya("[pause] Wrong suspect. Size changes speed, not survival. The clue is where the two VMs sat: in an availability set. That spreads VMs over fault domains, racks with their own power and network switch, and update domains, groups Azure patches one at a time."),
            leo("So one rack dies, the other VM lives. Sounds safe."),
            maya("Safe from a rack. But both racks stood in the same building. When the building lost power, both VMs went with it. An availability set never leaves one datacenter."),
            leo("Then what survives a building?"),
            maya("Availability zones. A region is an area of the world with Azure datacenters, like West Europe, and many regions have three zones. A zone is one or more datacenters with their own power, cooling and network. Like keeping spare keys in a house on another street."),
            leo("What does a zone really look like?"),
            maya("Under the picture, separate buildings a few kilometres apart, joined by fast private cables, so copies can keep in step. Two VMs or more across zones get 99.99 percent, against 99.95 in a set. And Microsoft recommends zones wherever a region has them."),
            leo("So they moved to zones. How?"),
            maya("With a virtual machine scale set, a group of identical VMs Azure adds or removes as load changes, spread over zones 1, 2 and 3. In front, a zone-redundant load balancer, a traffic spreader with a desk in every zone."),
            leo("How does it know a zone is dead?"),
            maya("It checks each VM's health every few seconds, like a receptionist ringing each office. No answer, no more visitors sent there. That's the whole mechanism; nobody has to press a button."),
            leo("Case solved, then."),
            maya("Not quite. The second clue was in the promise itself. The API runs on App Service, Azure's web hosting, at 99.95 percent, and calls SQL Database at 99.99. The team averaged them and wrote 99.97 in the contract."),
            leo("An average sounds fair to me."),
            maya("It isn't. Both must be up at once, so they're a chain, and a chain's composite SLA is the SLAs multiplied. Small numbers first: two services, each up 9 days in 10, so 0.9. In a chain, 0.9 times 0.9 is 0.81. Lower than either part."),
            leo("And if they stand side by side, two copies?"),
            maya("Then the pair fails only when both fail. Each is down 0.1, and 0.1 times 0.1 is 0.01, so the pair is up 99 percent. For the real chain, 0.9995 times 0.9999 is about 0.9994. 99.94, not 99.97. The contract was wrong."),
            leo("Third suspect, then. The database."),
            maya("Good nose. SQL Database already keeps a replica, an identical copy, like a photocopy, and zone redundancy puts those copies in different zones. But for a whole region going down, the team had set up active geo-replication on each of five databases."),
            leo("What's wrong with that?"),
            maya("It copies one database to another region, and you fail over, which means switch to the copy, one database at a time, by hand. And the app must then use a new server name. Five databases, five new names, at three in the morning."),
            leo("[sigh] Five changes under pressure. The fix?"),
            maya("A failover group. All the databases fail over together, and the app keeps one connection string, the address it uses to reach the database. The group's listener is a fixed name, like a forwarding phone number that always rings the live copy."),
            leo("Any more crimes?"),
            maya("Two. The progress store is Azure Cosmos DB, a database that copies itself to every region you add, like a notice board copied to several offices. The team turned on multi-region writes, so users in Europe and Australia both write nearby."),
            leo("Less waiting for Australia. Good. So what went wrong?"),
            maya("That waiting is called latency, the time data takes to cross the world. The trouble was consistency, which is how fresh a read is promised to be. They asked for strong consistency, where every read sees the latest write, and Azure refused."),
            leo("Why refuse?"),
            maya("Because strong would make every region agree before any write counted, and multi-region writes can't wait for that. Session, the default, means each user always sees their own writes. That's the fit, and the SLA reaches 99.999 percent."),
            leo("And the last crime?"),
            maya("Media storage. They chose GRS, geo-redundant storage: three copies here, plus copies in the paired region, a partner region Azure links. During the outage they tried to read the far copy, and couldn't."),
            leo("Because it stays hidden until a failover."),
            maya("Exactly. Reading it any time needs RA-GRS, read access, or RA-GZRS. GZRS is three zones here plus the paired region, the strongest. And ZRS alone, three copies across zones, survives a zone with no data loss."),
            leo("Where does all this stop working? There's always a catch."),
            maya("Two places. Copies to another region are sent a moment after each write, so a regional failover can lose a little data. And copies are not backups: delete a table by mistake, and every copy deletes it too. The backup and disaster recovery episode covers that."),
            leo("So, the case summary for the exam?"),
            maya("If the question says survive a rack, cheapest, the answer is an availability set. Survive a datacenter: availability zones. Services in a chain: multiply, for the composite SLA. Many databases failing over with one connection string: a failover group. Writes in Europe and Australia at 99.999: Cosmos DB multi-region writes with session, never strong."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate it safely. Create a Flexible scale set across zones 1, 2 and 3, and check each VM's zone in the portal. Look up the SLA for App Service and SQL Database and work out their composite SLA. Then open a storage account's Redundancy page, and see which options your region offers."),
            leo("Two buildings, not two shelves. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-high-availability",
        listOf(
            maya("Okay, did you hear what happened at Two Tracks this week? The app's gone global, users in Europe and Australia, and the company promised the app would stay up. So everyone's been building for high availability. And let's just say, it was a week."),
            leo("[laugh] I heard there was a whiteboard incident. Start with the basics. High availability means?"),
            maya("The app keeps running when a part breaks, with nobody stepping in. HA for short. The trick: more than one copy, in places that don't fail together. Keys in two pockets, then two bags, then two houses. Each step costs more and survives more."),
            leo("And the promise they made?"),
            maya("An SLA, a service level agreement: an uptime promise as a percentage. Picture a train company promising 9,999 trains in 10,000 run. That's 99.99 percent. Microsoft gives one for each service."),
            leo("Okay. Who went first?"),
            maya("Tom. Monday. The API, the door the phone app knocks on for data, runs on VMs, virtual machines, computers you rent by the hour. Tom had to survive a datacenter outage. A datacenter is one big building of servers in racks, tall shelves."),
            leo("And Tom picked?"),
            maya("An availability set. It spreads VMs over fault domains, racks with their own power and network switch, and over update domains, groups patched one at a time. Lovely for a rack. Useless for a building, because every rack stands in the same one."),
            leo("[sigh] Poor Tom. What should he have picked?"),
            maya("Availability zones. A region is an area of the world with Azure datacenters, like West Europe, and many have three zones. A zone is one or more datacenters with their own power, cooling and network. Under the picture, separate buildings kilometres apart, joined by fast private cables."),
            leo("And the SLA difference?"),
            maya("Two VMs or more in a set: 99.95. Across zones: 99.99. So Tom rebuilt it as a virtual machine scale set, a group of identical VMs that grows and shrinks with load, over zones 1, 2 and 3, with a zone-redundant load balancer in front."),
            leo("A load balancer being?"),
            maya("A traffic spreader, like a receptionist with a desk in every building. It checks each VM's health every few seconds and stops sending visitors to one that doesn't answer. That's how a dead zone gets skipped, with nobody pressing a button."),
            leo("Tuesday?"),
            maya("Dana. Dana wrote the uptime promise for the contract. The API runs on App Service, Azure's web hosting, at 99.95, and calls SQL Database at 99.99. Dana averaged them and wrote 99.97."),
            leo("Sounds reasonable."),
            maya("That's what Dana said. But both must be up at once, so they're a chain, and the chain's composite SLA is the SLAs multiplied. Small numbers: two services each up 9 days in 10, so 0.9. 0.9 times 0.9 is 0.81. The chain is worse than either part."),
            leo("And with two copies side by side?"),
            maya("Then it fails only when both fail. Each is down 0.1, and 0.1 times 0.1 is 0.01, so the pair is up 99 percent. Dana's real chain: 0.9995 times 0.9999, about 99.94. Legal had a word."),
            leo("[chuckle] Wednesday?"),
            maya("Priya, on the databases. Five SQL databases had to move to North Europe together if West Europe went down. SQL Database already keeps a replica, an identical copy, in other zones once you turn on zone redundancy. But a whole region needs more."),
            leo("And Priya chose?"),
            maya("Active geo-replication on each one. That gives one database a readable copy in another region, and you fail over, which means switch to the copy, by hand. One at a time. And each time, the app needs a new server name."),
            leo("Five names to change during an outage. Nightmare."),
            maya("So she switched to a failover group. All five fail over together, and the app keeps one connection string, the address it uses to reach the database. The listener is a fixed name, like a forwarding phone number."),
            leo("Thursday."),
            maya("Marcus. The progress store is Cosmos DB, a database that copies itself to every region you add, like a notice board copied to several offices. Marcus turned on multi-region writes, so Europe and Australia both write nearby. The SLA hits 99.999."),
            leo("Sounds like a win."),
            maya("Then he asked for strong consistency. Consistency means how fresh a read is promised to be; strong means every read sees the latest write. Azure said no. Strong would make every region agree before a write counts, so it can't go with multi-region writes."),
            leo("So what did he use?"),
            maya("Session, the default: each user always sees their own writes. And writing nearby cuts latency, which is the wait while data crosses the world. Australia was very happy."),
            leo("Friday. There's always a Friday."),
            maya("The intern. Bless them. Media files went on GRS, geo-redundant storage: three copies here plus copies in the paired region, a partner region Azure links. During a drill, the intern tried to read the far copy. Nothing."),
            leo("Hidden until a failover?"),
            maya("Exactly. Reading it any time needs RA-GRS or RA-GZRS, RA for read access. GZRS is zones here plus the paired region. And ZRS alone, three copies across zones, survives a zone with no data loss."),
            leo("Did anything catch everyone out?"),
            maya("One thing. Copies to another region are sent a moment after each write, so a regional failover can lose a little data. And a copy isn't a backup: delete a table and every copy deletes it. The backup and disaster recovery episode has that drama."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If the question says survive a rack failure, cheapest, the answer is an availability set. Survive a datacenter: availability zones. Services in a chain: multiply their SLAs for the composite SLA. Many databases failing over with one connection string: a failover group. Writes in Europe and Australia at the top SLA: Cosmos DB multi-region writes with session consistency."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a Flexible scale set across zones 1, 2 and 3, and check each VM's zone in the portal. Look up the SLA for App Service and SQL Database and work out their composite SLA. Then open a storage account's Redundancy page and see which options your region offers."),
            leo("Zones, not sets, and multiply. Got it, Dana. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-high-availability",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just us. Me, Ivy, and you, still up, still learning. [pause] Tonight: staying up when things break."),
            ivy("Mm. Staying up when things break. That's my whole night, Nova."),
            nova("[chuckle] Then you'll love this. It's called high availability, HA for short. It means your app keeps running when a part fails, with nobody waking up to fix it. The trick is more than one copy, in places that don't fail together."),
            ivy("Like a spare key."),
            nova("Like spare keys in two pockets, then two bags, then two houses. Each step costs a little more, [pause] and survives a bigger accident. Take Two Tracks. The Android app has gone global, users in Europe and Australia, and the company has made a promise."),
            ivy("A promise. How romantic. What kind?"),
            nova("An SLA, a service level agreement. It's an uptime promise as a percentage, like a night train promising 9,999 trips in 10,000 arrive. 99.99 percent. Microsoft makes one for each of its services."),
            ivy("And the app itself. Where does it live?"),
            nova("The API, the door the phone app knocks on for data, runs on VMs. A VM is a virtual machine, a computer you rent by the hour, sitting in a datacenter. That's a big building full of servers in racks, [pause] tall shelves humming in the dark."),
            ivy("So my first pocket is?"),
            nova("An availability set. It spreads your VMs over fault domains, racks with their own power and network switch, and update domains, groups Azure patches one at a time. One rack dies, the other VM keeps humming. 99.95 percent with two or more."),
            ivy("And if the whole building goes dark?"),
            nova("Then the set goes with it. [pause] That's where it stops working. For a building you need availability zones. A region is an area of the world with Azure datacenters, like West Europe, and many have three zones."),
            ivy("Tell me what a zone really is."),
            nova("One or more datacenters with their own power, cooling and network. Under the picture, separate buildings kilometres apart, joined by fast private cables. VMs across zones get 99.99 percent, and Microsoft recommends zones wherever a region has them."),
            ivy("How do I spread my VMs over them?"),
            nova("With a virtual machine scale set, a group of identical VMs that grows and shrinks with load, across zones 1, 2 and 3. And in front, a zone-redundant load balancer, a traffic spreader with a desk in every zone."),
            ivy("And how does it know a zone has gone quiet?"),
            nova("It checks each VM's health every few seconds, like a night porter knocking on doors. No answer, no more guests sent there. [pause] Nobody has to wake up."),
            ivy("Now, that promise. If my API uses two services, what do I promise?"),
            nova("Say the API runs on App Service, Azure's web hosting, at 99.95, and it calls SQL Database at 99.99. Both must be up at once. That's a chain, and its uptime is called the composite SLA. You multiply."),
            ivy("Gently. Small numbers, please. It's late."),
            nova("Two services, each up 9 nights in 10. That's 0.9. In a chain, 0.9 times 0.9 is 0.81. [pause] 81 percent, worse than either one alone."),
            ivy("And if I keep two copies side by side?"),
            nova("Then you're only down when both are down. Each is down 0.1 of the time, and 0.1 times 0.1 is 0.01. So the pair is up 99 percent. The real chain, 0.9995 times 0.9999, is about 99.94."),
            ivy("Chains pull you down; pairs lift you up. Noted. And my data?"),
            nova("SQL Database keeps a replica, an identical copy, like a photocopy, inside its datacenter. Turn on zone redundancy and the replicas sleep in different zones. For a whole region, you copy to another region, like North Europe."),
            ivy("How?"),
            nova("Two ways. Active geo-replication gives one database readable copies elsewhere, and you fail over, which means switch to the copy, by hand. But the app must then use a new server name."),
            ivy("And if I have five databases?"),
            nova("A failover group. They all fail over together, and the app keeps one connection string, the address it uses to find the database. The listener is a fixed name, [pause] like a forwarding number that always rings whoever's awake."),
            ivy("And the progress store? Users in Europe and Australia, both writing at night."),
            nova("Cosmos DB, a database that copies itself to every region you add, like a notice board in several offices. With multi-region writes, each user writes nearby. That cuts latency, which is the wait while data crosses the world. And the SLA reaches 99.999."),
            ivy("Too good to be true. What's the price?"),
            nova("Consistency. That means how fresh a read is promised to be. Strong, where every read sees the latest write, can't go with multi-region writes, because every region would have to agree first. Session, the default, means you always see your own writes."),
            ivy("And my photos?"),
            nova("Azure Storage, with a redundancy choice. LRS, three copies in one building. ZRS, three across zones, no data loss if a zone fails. GZRS adds the paired region, a partner region Azure links. And RA, read access, lets you read the far copy even before a failover."),
            ivy("Where does all this stop working?"),
            nova("Copies to another region travel a moment after each write, so a regional failover can lose a little. [pause] And a copy is not a backup: delete something by mistake, and every copy forgets it too. The backup and disaster recovery episode is your next late night."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("If the question says survive a rack failure, cheapest, the answer is an availability set. Survive a datacenter: availability zones. Services in a chain: multiply their SLAs for the composite SLA. Many databases failing over with one connection string: a failover group. Writes in Europe and Australia at the top SLA: Cosmos DB multi-region writes with session consistency."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a Flexible scale set across zones 1, 2 and 3, and check each VM's zone in the portal. [pause] Look up the SLA for App Service and SQL Database and work out their composite SLA. Then open a storage account's Redundancy page and see which options your region offers."),
            ivy("Spare keys in two houses. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-high-availability",
        listOf(
            nova("Tonight is date night, darling. Candles, wine, and you and me, Rafe, talking about the one thing every relationship needs."),
            rafe("Trust?"),
            nova("[laugh] Availability. High availability, HA for short: staying up when something breaks, with nobody rushing in to fix it."),
            rafe("Staying up all night. That I can promise."),
            nova("Careful with promises. In Azure a promise is an SLA, a service level agreement: uptime as a percentage, like a train company promising 9,999 trains in 10,000 run. That's 99.99 percent."),
            rafe("And our story tonight?"),
            nova("Two Tracks goes global. The Android app's backend lives on Azure, with users in Europe and Australia. The API, the door the phone app knocks on for data, runs on VMs, virtual machines, computers you rent by the hour in a datacenter."),
            rafe("A datacenter being a big building full of servers in racks, tall shelves. I did my homework."),
            nova("Mm, I like a prepared man. So, a quiz, exam style. You must survive a rack failing, as cheaply as possible. What do you pick?"),
            rafe("An availability set. It spreads VMs over fault domains, racks with their own power and network switch, and update domains, groups Azure patches one at a time."),
            nova("Right answer. Never keep all your eggs on one shelf. [kiss]"),
            rafe("My turn. Now the whole building loses power. What survives?"),
            nova("Availability zones. A region is an area of the world with Azure datacenters, like West Europe, and many regions have three zones. A zone is one or more datacenters with their own power, cooling and network. Two VMs or more across zones get 99.99 percent."),
            rafe("Right answer. I'd follow you to another building. [kiss]"),
            nova("But what does a zone really look like?"),
            rafe("Separate buildings kilometres apart, joined by fast private cables. You spread a virtual machine scale set, a group of identical VMs that grows with load, over zones 1, 2 and 3, behind a zone-redundant load balancer."),
            nova("And the load balancer is a traffic spreader with a desk in every zone. It checks each VM's health every few seconds, and stops sending anyone to a VM that doesn't answer."),
            rafe("Like me, ignoring my phone on date night."),
            nova("[chuckle] Next question. The API runs on App Service, Azure's web hosting, at 99.95, and calls SQL Database at 99.99. Both must be up. What's the composite SLA?"),
            rafe("Easy. Average them. 99.97."),
            nova("No kiss for that one. They're a chain, so you multiply. Small numbers: two services each up 9 days in 10, so 0.9. 0.9 times 0.9 is 0.81. A chain is weaker than either partner."),
            rafe("And two of us side by side?"),
            nova("Then we only fail when both fail. Each is down 0.1; 0.1 times 0.1 is 0.01. So together, 99 percent. The real chain, 0.9995 times 0.9999, is about 99.94."),
            rafe("Together we're stronger. I knew it. Now, five SQL databases must move to North Europe together, and the app keeps its connection string, the address it uses to find them. What do you use?"),
            nova("A failover group. They fail over together, which means switch to the copies, and the listener is a fixed name, like a forwarding number, so nothing changes in the app."),
            rafe("Right answer. Some of us keep the same number for life. [kiss]"),
            nova("And the other way, active geo-replication?"),
            rafe("One database, readable copies elsewhere, failed over by hand, and a new server name afterwards. Fine for one, messy for five. And inside the region, zone redundancy puts each replica, an identical copy, in a different zone."),
            nova("Mm. Next, the progress store. Cosmos DB, a database that copies itself to every region you add, like a notice board in several offices. Users in Europe and Australia must both write nearby, at the top SLA. What do you set?"),
            rafe("Multi-region writes. Each user writes close by, which is less latency, the wait while data crosses the world. 99.999 percent. And strong consistency, so we're always in sync."),
            nova("Half right. Consistency means how fresh a read is promised to be. Strong, where every read sees the latest write, can't go with multi-region writes, because every region would have to agree before anything counts. So?"),
            rafe("So we pick session, the default. Each user always sees their own writes."),
            nova("Right answer, second try. I always see your writes. [kiss]"),
            rafe("And the photos? Storage."),
            nova("LRS keeps three copies in one building. ZRS three across zones, no data loss if one goes. GRS adds the paired region, a partner region Azure links. GZRS does both. And RA, read access, lets you read the far copy before a failover."),
            rafe("One more, darling. A single VM on Premium SSD, a fast disk, has its own SLA, 99.9 percent. Does it survive a failure?"),
            nova("No. There's no second copy at all, so one VM never counts as highly available. A relationship needs two, Rafe."),
            rafe("Right answer, and a lovely one. [kiss]"),
            nova("And where does all this romance stop working? Copies to another region arrive a moment after each write, so a regional failover can lose a little. And a copy isn't a backup: delete a table, and every copy forgets it. The backup and disaster recovery episode picks up there."),
            rafe("Heartbreaking. Last question, for the exam. What do we remember?"),
            nova("If the question says survive a rack failure, cheapest, the answer is an availability set. Survive a datacenter: availability zones. Services in a chain: multiply their SLAs for the composite SLA. Many databases failing over with one connection string: a failover group. Writes in Europe and Australia at the top SLA: Cosmos DB multi-region writes with session consistency."),
            rafe("Five for five, darling. That earns a long one. [kiss]"),
            nova("Mm. And something to try today?"),
            rafe("Create a Flexible scale set across zones 1, 2 and 3, and check each VM's zone in the portal. Look up the SLA for App Service and SQL Database and work out their composite SLA. Then open a storage account's Redundancy page and see which options your region offers."),
            nova("Two copies of you, never in the same building. Perfect night. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun ladder() = DiagramSpec(
        "Copies kept in places that don't fail together: racks in one building, zones in one region, or a second region.",
        nodes = listOf(
            DNode("api", "Two Tracks API", 0.5f, 0.12f, w = 0.6f, h = 0.16f, kind = NodeKind.NOTE),
            DNode("set", "Availability set", 0.17f, 0.48f, w = 0.3f, h = 0.26f, sub = "racks"),
            DNode("zones", "Availability zones", 0.5f, 0.48f, w = 0.3f, h = 0.26f, tone = 1, sub = "buildings"),
            DNode("region", "Second region", 0.83f, 0.48f, w = 0.3f, h = 0.26f, tone = 2, sub = "far away"),
            DNode("cost", "Each step costs more and survives more", 0.5f, 0.86f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("api", "set"),
            DEdge("api", "zones"),
            DEdge("api", "region"),
        ),
        aspect = 0.8f,
    )

    private fun sla() = DiagramSpec(
        "In a chain both services must be up, so you multiply; side by side both must fail, so the pair is up more.",
        nodes = listOf(
            DNode("a", "Service A", 0.25f, 0.15f, w = 0.42f, h = 0.18f, sub = "up 0.9"),
            DNode("b", "Service B", 0.75f, 0.15f, w = 0.42f, h = 0.18f, sub = "up 0.9"),
            DNode("chain", "In a chain", 0.25f, 0.5f, w = 0.42f, h = 0.2f, tone = 2, sub = "0.9 x 0.9 = 0.81"),
            DNode("pair", "Side by side", 0.75f, 0.5f, w = 0.42f, h = 0.2f, tone = 1, sub = "1 - 0.01 = 0.99"),
            DNode("real", "App Service, then SQL", 0.5f, 0.85f, w = 0.8f, h = 0.18f, kind = NodeKind.NOTE, sub = "0.9995 x 0.9999 = about 99.94%"),
        ),
        edges = listOf(
            DEdge("a", "chain"),
            DEdge("b", "chain"),
            DEdge("a", "pair"),
            DEdge("b", "pair"),
            DEdge("chain", "real", "real numbers"),
        ),
        aspect = 0.8f,
    )

    private fun zones() = DiagramSpec(
        "A scale set spreads identical VMs over three zones, and a zone-redundant load balancer skips a dead one.",
        nodes = listOf(
            DNode("users", "Users", 0.5f, 0.1f, w = 0.2f, h = 0.14f, kind = NodeKind.USER),
            DNode("lb", "Zone-redundant load balancer", 0.5f, 0.34f, w = 0.8f, h = 0.14f, tone = 1),
            DNode("z1", "Zone 1", 0.17f, 0.62f, w = 0.3f, h = 0.22f, sub = "VMs"),
            DNode("z2", "Zone 2", 0.5f, 0.62f, w = 0.3f, h = 0.22f, sub = "VMs"),
            DNode("z3", "Zone 3", 0.83f, 0.62f, w = 0.3f, h = 0.22f, sub = "VMs"),
            DNode("note", "One scale set across zones 1, 2, 3", 0.5f, 0.9f, w = 0.9f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("users", "lb"),
            DEdge("lb", "z1"),
            DEdge("lb", "z2"),
            DEdge("lb", "z3"),
        ),
        aspect = 0.8f,
    )

    private fun sql() = DiagramSpec(
        "A failover group moves all the databases to North Europe together; the listener name the API uses never changes.",
        nodes = listOf(
            DNode("api", "Two Tracks API", 0.25f, 0.16f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "one connection string"),
            DNode("listener", "Listener", 0.75f, 0.16f, w = 0.42f, h = 0.2f, tone = 1, sub = "a fixed name"),
            DNode("primary", "West Europe", 0.25f, 0.76f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "primary"),
            DNode("secondary", "North Europe", 0.75f, 0.76f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2, sub = "readable copy"),
        ),
        edges = listOf(
            DEdge("api", "listener", "connects"),
            DEdge("listener", "primary"),
            DEdge("primary", "secondary", "geo copy", dashed = true),
            DEdge("listener", "secondary", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun cosmos() = DiagramSpec(
        "With multi-region writes each user writes to the nearby region; session consistency shows you your own writes.",
        nodes = listOf(
            DNode("ueu", "Europe", 0.18f, 0.14f, w = 0.2f, h = 0.16f, kind = NodeKind.USER),
            DNode("uau", "Australia", 0.82f, 0.14f, w = 0.2f, h = 0.16f, kind = NodeKind.USER),
            DNode("eu", "West Europe", 0.25f, 0.52f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "writes here"),
            DNode("au", "Australia East", 0.75f, 0.52f, w = 0.42f, h = 0.24f, kind = NodeKind.CYLINDER, sub = "writes here"),
            DNode("note", "Session: you see your own writes", 0.5f, 0.87f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("ueu", "eu"),
            DEdge("uau", "au"),
            DEdge("eu", "au", "copies", bidirectional = true),
        ),
        aspect = 0.8f,
    )

    private fun storage() = DiagramSpec(
        "Storage redundancy: three copies in one building or across zones, and the G versions add the paired region.",
        nodes = listOf(
            DNode("lrs", "LRS", 0.25f, 0.16f, w = 0.42f, h = 0.2f, sub = "3 copies, 1 building"),
            DNode("zrs", "ZRS", 0.75f, 0.16f, w = 0.42f, h = 0.2f, sub = "3 copies, 3 zones"),
            DNode("grs", "GRS", 0.25f, 0.58f, w = 0.42f, h = 0.2f, tone = 2, sub = "+ paired region"),
            DNode("gzrs", "GZRS", 0.75f, 0.58f, w = 0.42f, h = 0.2f, tone = 1, sub = "zones + region"),
            DNode("ra", "RA- means read the far copy any time", 0.5f, 0.88f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("lrs", "grs", "add a region"),
            DEdge("zrs", "gzrs", "add a region"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Region copies arrive a moment late, and a copy is not a backup: a mistake is copied everywhere.",
        nodes = listOf(
            DNode("lag", "Region copies lag", 0.25f, 0.2f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "a little data lost"),
            DNode("del", "Not a backup", 0.75f, 0.2f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "deletes are copied"),
            DNode("bk", "Backup and DR episode", 0.5f, 0.74f, w = 0.7f, h = 0.22f, tone = 1, sub = "the next piece"),
        ),
        edges = listOf(
            DEdge("lag", "bk"),
            DEdge("del", "bk"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's high availability questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("rack", "Survive a rack", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "availability set"),
            DNode("dc", "Survive a building", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "availability zones"),
            DNode("chain", "Services in a chain", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "multiply the SLAs"),
            DNode("fog", "Many DBs, one name", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "failover group"),
            DNode("write", "Writes in EU and AU", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "multi-region, session"),
            DNode("read", "Read the far copy", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "RA-GRS or RA-GZRS"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("rack", "dc", "chain", "fog", "write", "read")
    private val zoneSet = setOf("lb", "z1", "z2", "z3")
    private val today = BoardCard("Today", "A scale set over three zones, a composite SLA, the Redundancy page.")
    private val takeaways = BoardCard("Listen for the phrase", "Rack: set. Building: zones. Chain: multiply. Many databases: failover group.")

    private fun classicBoard() = PodcastBoard(
        "sa-high-availability",
        listOf(
            // 0: "Hi, and welcome back. Today is high availability..."
            Scene(
                0, "Racks, buildings, regions", ladder(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Two Tracks goes global", "Users in Europe and Australia, and a promise that the app stays up.")),
                    Beat(2, card = BoardCard("High availability", "Keeps running when a part breaks: copies that don't fail together.")),
                    Beat(6, card = BoardCard("SLA", "An uptime promise: 99.99% is 9,999 trains in 10,000 running.")),
                    Beat(8, card = BoardCard("VM and datacenter", "A computer rented by the hour, in a big building of server racks.")),
                    Beat(10, show = setOf("set"), light = setOf("set"), pulse = "api" to "set", card = BoardCard("Availability set", "VMs over racks and patch groups in one building. 99.95%.")),
                    Beat(12, show = setOf("zones"), light = setOf("zones"), pulse = "api" to "zones", card = BoardCard("Region", "An area of the world with Azure datacenters, like West Europe.")),
                    Beat(14, card = BoardCard("Availability zone", "Datacenters with their own power, cooling and network. 99.99%.")),
                    Beat(16, show = setOf("region"), light = setOf("region"), pulse = "api" to "region", card = BoardCard("Second region", "North Europe: survives a whole region going down.")),
                ),
            ),
            // 17: "Racks, buildings, regions. Got it. Now, my app uses two services..."
            Scene(
                17, "The composite SLA", sla(),
                beats = listOf(
                    Beat(18, show = setOf("a", "b"), light = setOf("a", "b"), card = BoardCard("Composite SLA", "Services in a chain must all be up, so multiply their SLAs.")),
                    Beat(20, card = BoardCard("Small numbers", "Two services, each up 9 days in 10: 0.9.")),
                    Beat(22, show = setOf("chain"), light = setOf("chain"), pulse = "a" to "chain", card = BoardCard("In a chain", "0.9 x 0.9 = 0.81: lower than either part.")),
                    Beat(24, show = setOf("pair"), light = setOf("pair"), pulse = "b" to "pair", card = BoardCard("Side by side", "Down only when both are: 0.1 x 0.1 = 0.01, so up 0.99.")),
                    Beat(26, show = setOf("real"), light = setOf("real"), pulse = "chain" to "real", card = BoardCard("The real chain", "App Service 99.95% x SQL Database 99.99% = about 99.94%.")),
                    Beat(28, light = setOf("chain"), card = BoardCard("Trap: one VM", "Premium SSD gives 99.9%, but there is no second copy at all.")),
                ),
            ),
            // 29: "So how do I actually run my API across zones?"
            Scene(
                29, "Compute across zones", zones(),
                beats = listOf(
                    Beat(30, show = zoneSet, light = setOf("lb"), pulse = "users" to "lb", card = BoardCard("Zone-redundant load balancer", "A traffic spreader with a desk in every zone.")),
                    Beat(32, light = setOf("z1", "z2", "z3"), card = BoardCard("Two regions", "A global entry point such as Front Door picks the healthy region.")),
                ),
            ),
            // 33: "Now the database. That's where the data actually lives."
            Scene(
                33, "SQL in two regions", sql(),
                beats = listOf(
                    Beat(34, show = setOf("primary"), light = setOf("primary"), card = BoardCard("Replica", "An identical copy. Zone redundancy puts them in different zones.")),
                    Beat(36, show = setOf("secondary"), light = setOf("primary", "secondary"), pulse = "primary" to "secondary", card = BoardCard("Active geo-replication", "One database, readable copies elsewhere, failed over by hand.")),
                    Beat(38, show = setOf("api", "listener"), light = setOf("listener"), pulse = "api" to "listener", card = BoardCard("Failover group", "Databases fail over together; the listener name stays the same.")),
                ),
            ),
            // 40: "Exactly. Now the progress store..."
            Scene(
                40, "Cosmos DB in two regions", cosmos(),
                beats = listOf(
                    Beat(40, show = setOf("eu"), light = setOf("eu"), pulse = "ueu" to "eu", card = BoardCard("Cosmos DB", "A database copied to every region you add, like a notice board.")),
                    Beat(42, show = setOf("au"), light = setOf("eu", "au"), pulse = "eu" to "au", card = BoardCard("Multi-region writes", "Each user writes nearby: less latency, and a 99.999% SLA.")),
                    Beat(44, show = setOf("note"), light = setOf("note"), card = BoardCard("Consistency", "How fresh a read is. Strong can't go with multi-region writes.")),
                ),
            ),
            // 45: "And the photos and audio? Where do those go?"
            Scene(
                45, "Storage redundancy", storage(),
                beats = listOf(
                    Beat(46, show = setOf("lrs", "zrs", "grs", "gzrs"), light = setOf("zrs", "gzrs"), pulse = "zrs" to "gzrs", card = BoardCard("ZRS and GZRS", "ZRS survives a zone with no data loss; GZRS adds the paired region.")),
                    Beat(48, show = setOf("ra"), light = setOf("ra"), card = BoardCard("RA-GRS and RA-GZRS", "Read the far copy any time. Region copies lag a moment.")),
                ),
            ),
            // 49: "So what do I remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("rack", "dc", "chain", "fog", "write"), card = takeaways),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-high-availability--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Promise That Burned Down..."
            Scene(
                0, "The Promise That Burned Down", ladder(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("The case", "A West Europe building lost power, and the app went dark.")),
                    Beat(2, card = BoardCard("SLA", "An uptime promise as a percentage. They promised 99.99%.")),
                    Beat(4, card = BoardCard("Datacenter", "A big building of rented computers standing in racks.")),
                    Beat(6, show = setOf("set"), light = setOf("set"), pulse = "api" to "set", card = BoardCard("Suspect cleared: VM size", "Size changes speed, not survival. The VMs sat in a set.")),
                    Beat(8, card = BoardCard("Availability set", "Racks in one building. The building went, and both VMs with it.")),
                    Beat(10, show = setOf("zones"), light = setOf("zones"), pulse = "api" to "zones", card = BoardCard("Availability zones", "Separate datacenters in one region, each with its own power.")),
                    Beat(12, show = setOf("region"), light = setOf("zones"), card = BoardCard("Under the picture", "Buildings kilometres apart, joined by fast private cables. 99.99%.")),
                ),
            ),
            // 13: "So they moved to zones. How?"
            Scene(
                13, "The zone fix", zones(),
                beats = listOf(
                    Beat(14, show = zoneSet, light = setOf("lb"), pulse = "users" to "lb", card = BoardCard("Scale set across zones", "Identical VMs over zones 1, 2 and 3, behind a zone-redundant balancer.")),
                    Beat(16, light = setOf("z1", "z2", "z3"), card = BoardCard("How a dead zone is skipped", "Health checks every few seconds: no answer, no traffic.")),
                ),
            ),
            // 17: "Case solved, then."
            Scene(
                17, "The averaged promise", sla(),
                beats = listOf(
                    Beat(18, show = setOf("a", "b"), light = setOf("a", "b"), card = BoardCard("Clue: 99.97% in the contract", "App Service 99.95% and SQL 99.99%, averaged. Wrong.")),
                    Beat(20, show = setOf("chain"), light = setOf("chain"), pulse = "a" to "chain", card = BoardCard("Multiply", "Each up 0.9: in a chain, 0.9 x 0.9 = 0.81.")),
                    Beat(22, show = setOf("pair", "real"), light = setOf("pair", "real"), pulse = "chain" to "real", card = BoardCard("The real chain", "A pair side by side: 0.99. The chain: about 99.94%, not 99.97%.")),
                ),
            ),
            // 23: "Third suspect, then. The database."
            Scene(
                23, "Five databases", sql(),
                beats = listOf(
                    Beat(24, show = setOf("primary"), light = setOf("primary"), card = BoardCard("Replica", "An identical copy; zone redundancy spreads them over zones.")),
                    Beat(26, show = setOf("secondary"), light = setOf("secondary"), pulse = "primary" to "secondary", card = BoardCard("Suspect: geo-replication", "One database at a time, by hand, then a new server name.")),
                    Beat(28, show = setOf("api", "listener"), light = setOf("listener"), pulse = "api" to "listener", card = BoardCard("Failover group", "All fail over together; the listener name never changes.")),
                ),
            ),
            // 29: "Any more crimes?"
            Scene(
                29, "Strong, refused", cosmos(),
                beats = listOf(
                    Beat(30, show = setOf("eu", "au"), light = setOf("eu", "au"), pulse = "eu" to "au", card = BoardCard("Multi-region writes", "Users in Europe and Australia both write nearby.")),
                    Beat(32, card = BoardCard("Latency and consistency", "Latency: the wait. Consistency: how fresh a read is promised.")),
                    Beat(34, show = setOf("note"), light = setOf("note"), card = BoardCard("Session, not strong", "Strong needs every region to agree first. Session fits: 99.999%.")),
                ),
            ),
            // 35: "And the last crime?"
            Scene(
                35, "The hidden copy", storage(),
                beats = listOf(
                    Beat(36, show = setOf("lrs", "grs"), light = setOf("grs"), pulse = "lrs" to "grs", card = BoardCard("GRS", "Copies in the paired region, hidden until a failover.")),
                    Beat(38, show = setOf("zrs", "gzrs", "ra"), light = setOf("gzrs", "ra"), pulse = "zrs" to "gzrs", card = BoardCard("RA-GRS, RA-GZRS and ZRS", "Read access any time; ZRS survives a zone with no data loss.")),
                ),
            ),
            // 39: "Where does all this stop working?"
            Scene(
                39, "Where it stops", limits(),
                beats = listOf(
                    Beat(40, light = setOf("lag", "del"), pulse = "del" to "bk", card = BoardCard("Not a backup", "Region copies lag; a deleted table is deleted in every copy.")),
                ),
            ),
            // 41: "So, the case summary for the exam?"
            Scene(
                41, "Case closed", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("rack", "dc", "chain", "fog", "write"), card = takeaways),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-high-availability--gossip",
        listOf(
            // 0: "Okay, did you hear what happened at Two Tracks this week?..."
            Scene(
                0, "Monday: Tom's set", ladder(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Gone global", "Users in Europe and Australia, and a promise the app stays up.")),
                    Beat(2, card = BoardCard("High availability", "Keeps running when a part breaks: copies that don't fail together.")),
                    Beat(4, card = BoardCard("SLA", "An uptime promise: 99.99% is 9,999 trains in 10,000.")),
                    Beat(6, card = BoardCard("VM and datacenter", "A rented computer, in a big building of server racks.")),
                    Beat(8, show = setOf("set"), light = setOf("set"), pulse = "api" to "set", card = BoardCard("Tom: an availability set", "Racks in one building. Fine for a rack, useless for a building.")),
                    Beat(10, show = setOf("zones", "region"), light = setOf("zones"), pulse = "api" to "zones", card = BoardCard("Availability zones", "Separate datacenters in a region, joined by fast private cables.")),
                ),
            ),
            // 12: "Two VMs or more in a set: 99.95. Across zones: 99.99..."
            Scene(
                12, "Tom's rebuild", zones(),
                beats = listOf(
                    Beat(12, show = zoneSet, light = setOf("z1", "z2", "z3"), card = BoardCard("Scale set across zones", "A group of identical VMs over zones 1, 2 and 3. 99.99%.")),
                    Beat(14, light = setOf("lb"), pulse = "users" to "lb", card = BoardCard("Load balancer", "A traffic spreader; health checks skip a VM that doesn't answer.")),
                ),
            ),
            // 15: "Tuesday?"
            Scene(
                15, "Tuesday: Dana's average", sla(),
                beats = listOf(
                    Beat(16, show = setOf("a", "b"), light = setOf("a", "b"), card = BoardCard("Dana wrote 99.97%", "App Service 99.95% and SQL 99.99%, averaged. Wrong.")),
                    Beat(18, show = setOf("chain"), light = setOf("chain"), pulse = "a" to "chain", card = BoardCard("Composite SLA: multiply", "Each up 0.9: in a chain, 0.9 x 0.9 = 0.81.")),
                    Beat(20, show = setOf("pair", "real"), light = setOf("pair", "real"), pulse = "chain" to "real", card = BoardCard("Side by side, and for real", "A pair: 0.99. The real chain: about 99.94%.")),
                ),
            ),
            // 21: "Wednesday?"
            Scene(
                21, "Wednesday: Priya", sql(),
                beats = listOf(
                    Beat(22, show = setOf("primary"), light = setOf("primary"), card = BoardCard("Replica", "An identical copy, in other zones with zone redundancy.")),
                    Beat(24, show = setOf("secondary"), light = setOf("secondary"), pulse = "primary" to "secondary", card = BoardCard("Active geo-replication", "One database at a time, by hand, and a new server name.")),
                    Beat(26, show = setOf("api", "listener"), light = setOf("listener"), pulse = "api" to "listener", card = BoardCard("Failover group", "All five fail over together; one connection string.")),
                ),
            ),
            // 27: "Thursday."
            Scene(
                27, "Thursday: Marcus", cosmos(),
                beats = listOf(
                    Beat(28, show = setOf("eu", "au"), light = setOf("eu", "au"), pulse = "eu" to "au", card = BoardCard("Multi-region writes", "Europe and Australia both write nearby. 99.999%.")),
                    Beat(30, card = BoardCard("Strong consistency: refused", "Every region would have to agree before a write counts.")),
                    Beat(32, show = setOf("note"), light = setOf("note"), card = BoardCard("Session", "Each user sees their own writes; nearby writes cut latency.")),
                ),
            ),
            // 33: "Friday. There's always a Friday."
            Scene(
                33, "Friday: the intern", storage(),
                beats = listOf(
                    Beat(34, show = setOf("lrs", "grs"), light = setOf("grs"), pulse = "lrs" to "grs", card = BoardCard("GRS", "Copies in the paired region, hidden until a failover.")),
                    Beat(36, show = setOf("zrs", "gzrs", "ra"), light = setOf("gzrs", "ra"), pulse = "zrs" to "gzrs", card = BoardCard("RA- to read", "RA-GRS or RA-GZRS to read any time; ZRS survives a zone.")),
                ),
            ),
            // 37: "Did anything catch everyone out?"
            Scene(
                37, "The catch", limits(),
                beats = listOf(
                    Beat(38, light = setOf("lag", "del"), pulse = "del" to "bk", card = BoardCard("Not a backup", "Region copies lag; a deleted table is deleted in every copy.")),
                ),
            ),
            // 39: "So, the gossip I should remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("rack", "dc", "chain", "fog", "write"), card = takeaways),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-high-availability--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "Staying up", ladder(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Tonight", "Staying up when things break: high availability.")),
                    Beat(2, card = BoardCard("High availability", "Keeps running when a part fails, with nobody waking up to fix it.")),
                    Beat(6, card = BoardCard("SLA", "An uptime promise: 99.99% is 9,999 trips in 10,000.")),
                    Beat(8, card = BoardCard("VM and datacenter", "A rented computer, in a big building of server racks.")),
                    Beat(10, show = setOf("set"), light = setOf("set"), pulse = "api" to "set", card = BoardCard("Availability set", "Racks with their own power, in one building. 99.95%.")),
                    Beat(12, show = setOf("zones", "region"), light = setOf("zones"), pulse = "api" to "zones", card = BoardCard("Availability zones", "A set stops at the building. Zones survive one going dark.")),
                    Beat(14, card = BoardCard("What a zone really is", "Buildings kilometres apart, joined by fast private cables. 99.99%.")),
                ),
            ),
            // 15: "How do I spread my VMs over them?"
            Scene(
                15, "Across the zones", zones(),
                beats = listOf(
                    Beat(16, show = zoneSet, light = setOf("lb"), pulse = "users" to "lb", card = BoardCard("Scale set and balancer", "Identical VMs over three zones, a traffic spreader in front.")),
                    Beat(18, light = setOf("z1", "z2", "z3"), card = BoardCard("How it knows", "Health checks every few seconds; no answer, no more traffic.")),
                ),
            ),
            // 19: "Now, that promise..."
            Scene(
                19, "The composite SLA", sla(),
                beats = listOf(
                    Beat(20, show = setOf("a", "b"), light = setOf("a", "b"), card = BoardCard("Composite SLA", "Services in a chain must all be up, so multiply.")),
                    Beat(22, show = setOf("chain"), light = setOf("chain"), pulse = "a" to "chain", card = BoardCard("In a chain", "Each up 0.9: 0.9 x 0.9 = 0.81, worse than either.")),
                    Beat(24, show = setOf("pair", "real"), light = setOf("pair", "real"), pulse = "chain" to "real", card = BoardCard("Side by side", "Pair: 0.99. The real chain: about 99.94%.")),
                ),
            ),
            // 25: "Chains pull you down; pairs lift you up. Noted. And my data?"
            Scene(
                25, "Your data", sql(),
                beats = listOf(
                    Beat(26, show = setOf("primary"), light = setOf("primary"), card = BoardCard("Replica", "An identical copy; zone redundancy puts them in different zones.")),
                    Beat(28, show = setOf("secondary"), light = setOf("secondary"), pulse = "primary" to "secondary", card = BoardCard("Active geo-replication", "One database, copies elsewhere, failed over by hand.")),
                    Beat(30, show = setOf("api", "listener"), light = setOf("listener"), pulse = "api" to "listener", card = BoardCard("Failover group", "All fail over together; the listener is a fixed name.")),
                ),
            ),
            // 31: "And the progress store?..."
            Scene(
                31, "Writing at night", cosmos(),
                beats = listOf(
                    Beat(32, show = setOf("eu", "au"), light = setOf("eu", "au"), pulse = "eu" to "au", card = BoardCard("Multi-region writes", "Each user writes nearby: less latency, 99.999%.")),
                    Beat(34, show = setOf("note"), light = setOf("note"), card = BoardCard("Consistency", "How fresh a read is. Session, never strong, with these writes.")),
                ),
            ),
            // 35: "And my photos?"
            Scene(
                35, "Your photos", storage(),
                beats = listOf(
                    Beat(36, show = setOf("lrs", "zrs", "grs", "gzrs", "ra"), light = setOf("zrs", "gzrs", "ra"), pulse = "zrs" to "gzrs", card = BoardCard("Storage redundancy", "ZRS for a zone; GZRS adds the paired region; RA- to read it.")),
                ),
            ),
            // 37: "Where does all this stop working?"
            Scene(
                37, "Where it stops", limits(),
                beats = listOf(
                    Beat(38, light = setOf("lag", "del"), pulse = "del" to "bk", card = BoardCard("Not a backup", "Region copies lag; a mistake is copied everywhere.")),
                ),
            ),
            // 39: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("rack", "dc", "chain", "fog", "write"), card = takeaways),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-high-availability--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", ladder(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Date night", "Staying up when something breaks: high availability.")),
                    Beat(4, card = BoardCard("SLA", "An uptime promise as a percentage: 99.99%.")),
                    Beat(6, card = BoardCard("The API", "The door the phone app knocks on, running on rented VMs.")),
                    Beat(8, card = BoardCard("Quiz: survive a rack", "As cheaply as possible. What do you pick?")),
                    Beat(9, show = setOf("set"), light = setOf("set"), pulse = "api" to "set", card = BoardCard("Availability set", "VMs over racks and patch groups in one building.")),
                    Beat(11, card = BoardCard("Quiz: the building goes", "The whole datacenter loses power. What survives?")),
                    Beat(12, show = setOf("zones", "region"), light = setOf("zones"), pulse = "api" to "zones", card = BoardCard("Availability zones", "Datacenters with their own power, cooling and network. 99.99%.")),
                ),
            ),
            // 14: "But what does a zone really look like?"
            Scene(
                14, "Inside the zones", zones(),
                beats = listOf(
                    Beat(15, show = zoneSet, light = setOf("z1", "z2", "z3"), card = BoardCard("Scale set across zones", "Buildings apart, fast cables between, identical VMs in each.")),
                    Beat(16, light = setOf("lb"), pulse = "users" to "lb", card = BoardCard("Load balancer", "Health checks every few seconds; a silent VM gets no traffic.")),
                ),
            ),
            // 18: "Next question. The API runs on App Service..."
            Scene(
                18, "The composite SLA", sla(),
                beats = listOf(
                    Beat(18, show = setOf("a", "b"), light = setOf("a", "b"), card = BoardCard("Quiz: composite SLA", "App Service 99.95% calls SQL 99.99%. Both must be up.")),
                    Beat(19, card = BoardCard("No kiss: the average", "A chain never averages. It multiplies.")),
                    Beat(20, show = setOf("chain"), light = setOf("chain"), pulse = "a" to "chain", card = BoardCard("In a chain", "Each up 0.9: 0.9 x 0.9 = 0.81.")),
                    Beat(22, show = setOf("pair", "real"), light = setOf("pair", "real"), pulse = "chain" to "real", card = BoardCard("Side by side", "Pair: 0.99. The real chain: about 99.94%.")),
                ),
            ),
            // 23: "Together we're stronger. I knew it. Now, five SQL databases..."
            Scene(
                23, "Five databases", sql(),
                beats = listOf(
                    Beat(23, show = setOf("primary", "secondary"), light = setOf("primary", "secondary"), card = BoardCard("Quiz: five databases", "Move to North Europe together, same connection string.")),
                    Beat(24, show = setOf("api", "listener"), light = setOf("listener"), pulse = "api" to "listener", card = BoardCard("Failover group", "They fail over together; the listener name never changes.")),
                    Beat(27, light = setOf("secondary"), pulse = "primary" to "secondary", card = BoardCard("Active geo-replication", "One database, failed over by hand, then a new server name.")),
                ),
            ),
            // 28: "Mm. Next, the progress store..."
            Scene(
                28, "Europe and Australia", cosmos(),
                beats = listOf(
                    Beat(28, show = setOf("eu", "au"), light = setOf("eu", "au"), card = BoardCard("Quiz: write in both places", "Users in Europe and Australia, at the top SLA.")),
                    Beat(29, pulse = "eu" to "au", card = BoardCard("Multi-region writes", "Each user writes nearby: less latency, 99.999%.")),
                    Beat(30, show = setOf("note"), light = setOf("note"), card = BoardCard("Not strong consistency", "Strong needs every region to agree first.")),
                    Beat(32, card = BoardCard("Session", "Each user always sees their own writes.")),
                ),
            ),
            // 33: "And the photos? Storage."
            Scene(
                33, "Storage, and one VM", storage(),
                beats = listOf(
                    Beat(34, show = setOf("lrs", "zrs", "grs", "gzrs", "ra"), light = setOf("zrs", "gzrs", "ra"), pulse = "zrs" to "gzrs", card = BoardCard("Storage redundancy", "ZRS for a zone; GZRS adds the paired region; RA- to read it.")),
                    Beat(35, light = setOf("lrs"), card = BoardCard("Quiz: one VM", "Premium SSD gives 99.9%, but no second copy: not highly available.")),
                ),
            ),
            // 38: "And where does all this romance stop working?..."
            Scene(
                38, "Where it stops", limits(),
                beats = listOf(
                    Beat(38, light = setOf("lag", "del"), pulse = "del" to "bk", card = BoardCard("Not a backup", "Region copies lag; a deleted table is deleted in every copy.")),
                ),
            ),
            // 39: "Heartbreaking. Last question, for the exam."
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("rack", "dc", "chain", "fog", "write"), card = takeaways),
                    Beat(43, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
