package com.shahaabapps.core.content

/** Application architecture: messaging, events, APIs, caching, config and deployment: its five AZ-305 episodes and their boards (Az305Podcasts). */
internal object Az305EpisodesAppArchitecture {
    private val classic = Podcast(
        "sa-app-architecture",
        listOf(
            maya("Hi, and welcome back. Today is application architecture, for AZ-305, the design exam. Our story: Two Tracks goes global. The Android app has become a company, with a backend on Azure, the server code the phones talk to, and users in Europe and Australia. Today we choose how its parts talk to each other."),
            leo("Its parts? I thought an app was one thing."),
            maya("A small app is, for example a to-do list saving to one database. A bigger one is several parts: the Tracks API, a billing worker, a leaderboard. Instead of calling each other and waiting, they pass notes through a go-between. Then one part can be busy, or down, without breaking the rest."),
            leo("Hang on. The API. What's that, in plain words?"),
            maya("An API is the menu of requests your backend accepts, like a waiter's order pad: get my scores, buy a subscription. The phone orders from the menu, and the backend cooks."),
            leo("And the notes they pass. All the same kind of note?"),
            maya("No, and that's the first thing the exam tests. A message is a parcel with a delivery note: the sender cares that it's handled. A paid order is a message. It must be processed once, and never lost."),
            leo("And an event?"),
            maya("An event is a doorbell: a short notice that something happened, whoever hears it. A file arrived. And a stream is a long run of events in time order, like a till roll at a shop, printing every sale all day."),
            leo("Parcel, doorbell, till roll. A service for each?"),
            maya("One for each, plus a very simple queue. A queue is a line of waiting notes, like the queue at a post office. Service Bus carries messages. Event Grid carries single events. Event Hubs takes streams. And Storage queues are the plain, cheap option."),
            leo("Start with Service Bus. When do I pick it?"),
            maya("When you hear orders, payments, in order, exactly once. Picture a paid Two Tracks subscription. It must make one invoice, in order for each user, and nothing may be lost if billing is down for an hour. Service Bus holds the message safely until billing comes back."),
            leo("How does it keep the order?"),
            maya("FIFO, first in, first out: served in the order you arrived, like that post office queue. Service Bus does it with sessions, which are groups that keep one user's messages in line together. And duplicate detection drops a message sent twice, so nobody pays twice."),
            leo("And if a message keeps failing?"),
            maya("It's moved to the dead-letter queue, a side shelf like a lost-property office, so a person can look at it. Nothing just vanishes. And a topic is a queue with several readers, so one order can reach billing and the welcome email both."),
            leo("Now Event Grid. The doorbell."),
            maya("Event Grid pushes each event to whoever subscribed. Say a user uploads a profile photo to Blob Storage, Azure's store for files. Storage tells Event Grid, a blob was created, and Event Grid pushes that to a small function that makes a thumbnail. Nobody polls."),
            leo("Polls?"),
            maya("[chuckle] Asks again and again, is anything new? Like a child on a car trip: are we there yet? An event-driven design reacts instead. So when you hear react when a file arrives, or when an Azure resource changes, think Event Grid."),
            leo("And Event Hubs, the till roll?"),
            maya("Event Hubs takes millions of events a second, and keeps them for a while so they can be read again. That's for telemetry, the little reports every phone sends home: screens opened, taps, errors. Analysts can replay yesterday."),
            leo("Millions a second. How?"),
            maya("It splits the stream into partitions. A partition is like one lane of a motorway: events in a lane keep their order, and many lanes run side by side. Order holds only inside a lane. And if you hear huge simple backlog, lowest cost, that's Storage queues."),
            leo("Okay. So the phone calls the API directly?"),
            maya("Through a front door. API Management, often called APIM, is a gateway: one reception desk in front of all your APIs. Every caller checks in there first. Its policies are the desk's rules, set without code, and they run on every call."),
            leo("What kind of rules?"),
            maya("Rate limits, for example, like a limit of two cakes per customer, so no caller uses everything. It can check each caller's token, a short-lived signed pass like a festival wristband. And callers only know the desk, so the APIs behind it can move without breaking them."),
            leo("Does it come in sizes?"),
            maya("In tiers, which are size and price levels, like economy and business class. Developer is for testing and has no SLA, a service level agreement: Microsoft's written promise of how much of the time it stays up. Basic and Standard serve production. Classic Premium is the one that stretches across several regions."),
            leo("Regions, as in places on the map?"),
            maya("Yes. A region is a group of Azure data centres in one area, like West Europe or Australia East. Two Tracks has users in both, so a gateway standing in several regions means Premium."),
            leo("Next. The leaderboard is slow."),
            maya("Then cache it. A cache is a small, very fast memory for answers you'll need again, like today's timetable on the fridge instead of phoning the station. Azure Managed Redis is Azure's current cache service; the older Azure Cache for Redis is being retired."),
            leo("And how does the API use it?"),
            maya("Cache-aside. Look on the fridge first. On a miss, phone the station, which is the database, then pin the answer up with an expiry time. A query that ran two thousand times a minute now mostly comes from memory."),
            leo("Why the expiry time?"),
            maya("Without one, users keep seeing old scores after the database changes. And a bigger database tier isn't the fix: it pays more to do the same repeated work."),
            leo("Settings next. Where do they live?"),
            maya("In App Configuration, one central store of settings for many apps. It holds feature flags too. A feature flag is a light switch in the settings: turn the new coach on in production without shipping a new version. Labels such as dev and prod keep each environment's values apart."),
            leo("And passwords? Same store?"),
            maya("Never. Secrets go in Key Vault, a locked safe for keys and passwords. App Configuration may hold a Key Vault reference, a pointer that says fetch this from the safe, but never the secret itself."),
            leo("Last piece. Shipping new versions."),
            maya("Infrastructure as code: your Azure resources written down in files kept with the app, like a recipe card, so every environment is cooked the same way. Bicep is the short language for those files. A pipeline is the automatic line that tests and deploys each change: GitHub Actions, or Azure Pipelines."),
            leo("And releasing without downtime?"),
            maya("[pause] Blue-green. Two copies, like two stages in a theatre: the live show and the next one. In App Service, deployment slots do it, on the Standard tier and above. Deploy to the staging slot, let it warm up, then swap. Something wrong? Swap back, in seconds. In Container Apps, revisions, the saved versions of your app, do the same."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If the question says in order, exactly once, the answer is Service Bus. If it says react when a file arrives, Event Grid. Millions of telemetry events, Event Hubs. Rate limits in front of APIs, API Management. No downtime and a rollback in seconds, a staging slot and a swap."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create a Service Bus namespace, the container your queues live in, and a queue with sessions on. Create an App Configuration store and add a feature flag with a prod label. Read the Compare messaging services page and note one requirement word for each service. Then write a three-line Bicep file and run what-if, a dry run, against it."),
            leo("Parcels, doorbells and till rolls. See you next time."),
        ),
        series = PodcastSeries.AZ305,
    )

    private val caseFiles = Podcast(
        "sa-app-architecture",
        listOf(
            maya("Welcome to Case Files. Today's case: The Customer Who Paid Twice. TrailMate is a small Android hiking app that went global, with a backend on Azure, the server code the phones talk to. Last month, some walkers were charged twice for one subscription, and a few got their invoices in the wrong order."),
            leo("Detective Leo, on the case. My first suspect: the bank."),
            maya("[pause] Wrong suspect. The bank charged exactly what it was asked to. Clue one: when someone pays, the TrailMate API, the menu of requests the backend accepts, like a waiter's order pad, sends a note to the billing worker. And that note travelled through Event Grid."),
            leo("Event Grid being, in plain words?"),
            maya("A doorbell service. It carries events, short notices that something happened, and pushes each one to whoever subscribed. Great for a file arrived. But a payment isn't a doorbell. It's a parcel with a delivery note: a message, and the sender cares that it's handled once."),
            leo("And Event Grid doesn't care?"),
            maya("It doesn't promise order, and it doesn't drop duplicates. If it isn't sure a delivery landed, it tries again, so billing heard the same payment twice. The design failed the requirement: processed exactly once, in order, never lost."),
            leo("So which service carries parcels?"),
            maya("Service Bus. It's a message broker, a post office between the parts. Messages wait safely in a queue, a line of waiting notes, even if billing is down for an hour. Under the picture, it's a store that holds each message until a reader confirms it's done, and only then deletes it."),
            leo("And the order? And the twice?"),
            maya("Sessions keep order: a session is a group, so one walker's messages stay in line together, first in, first out. Duplicate detection drops a message sent twice within a time window. And a message that keeps failing moves to the dead-letter queue, a lost-property shelf a person checks."),
            leo("Case closed, then?"),
            maya("Not yet. Clue two: the Azure bill. TrailMate sends telemetry, the little reports every phone sends home, screens and taps and errors, thousands a second. Someone sent all of it through Service Bus queues."),
            leo("Suspect: too many phones."),
            maya("[chuckle] Suspect: the wrong service. Telemetry is a stream, a long run of events in time order, like a till roll. It wants Event Hubs, which takes millions of events a second and keeps them so analysts can replay them. Service Bus charges for care a stream never uses."),
            leo("How does Event Hubs take millions?"),
            maya("It splits the stream into partitions. A partition is like one lane of a motorway: order holds inside a lane, and many lanes run side by side. Where it stops working: there's no order across lanes, and no dead-lettering. Payments still belong on Service Bus."),
            leo("Clue three?"),
            maya("The leaderboard. Walkers said it showed last week's winners. TrailMate had added a cache, a small fast memory for answers you'll need again, like a timetable pinned on the fridge. Azure Managed Redis, which keeps data in memory."),
            leo("Suspect: the cache is broken."),
            maya("The cache is fine. The pattern is cache-aside: look on the fridge first; on a miss, phone the station, the database, then pin the answer up with an expiry time. TrailMate pinned answers with no expiry time. So the old ones never came down."),
            leo("Clue four. I can feel it."),
            maya("The settings. TrailMate keeps its settings in App Configuration, one central store for many apps, with feature flags, light switches for features. And there, as a plain value, sat the database password."),
            leo("Suspect: whoever can read the settings. Which is everyone."),
            maya("Now you're detecting. Secrets go in Key Vault, a locked safe. App Configuration holds only a Key Vault reference, a pointer that says fetch it from the safe, and the app opens the safe with its managed identity, a badge Azure gives the app. The Authorizing access episode covers the safe."),
            leo("And the last clue?"),
            maya("The fix itself went out badly. They deployed straight to production, the live app. The new version crashed, the app was down, and rolling back meant deploying the old version again."),
            leo("What should they have done?"),
            maya("Blue-green: two copies, like two stages in a theatre. In App Service, a deployment slot is that second stage, on the Standard tier and above. The pipeline, the automatic line that ships each change, deploys to the staging slot. It warms up, then a swap makes it live. Swap back, and you've rolled back, in seconds."),
            leo("And the front door? Did they have one?"),
            maya("They do now. API Management, a gateway: one reception desk in front of all the APIs, with policies, the desk's rules, like a rate limit, two cakes per customer. It checks each caller's token, a short-lived signed pass like a festival wristband. Where it stops: it's a front door, not a queue. It never holds parcels."),
            leo("Last suspect, then. Which size of desk did they buy?"),
            maya("[pause] The Developer tier. A tier is a size and price level, like economy or business class, and Developer is for testing, with no SLA, a service level agreement, Microsoft's written promise of how much of the time it stays up. Production wants Basic or Standard. And if the desk must stand in several regions, Azure's areas like West Europe and Australia East, classic Premium."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. In order, exactly once, never lost: Service Bus. React when a file arrives: Event Grid. Millions of telemetry events: Event Hubs. Repeat reads: Managed Redis with cache-aside and an expiry time. No downtime, rollback in seconds: a staging slot and a swap."),
            leo("Case closed. Anything to try today?"),
            maya("Rebuild the fix. Create a Service Bus namespace, the container your queues live in, and a queue with sessions on. Create an App Configuration store and add a feature flag with a prod label. Read the Compare messaging services page and note one requirement word for each service. Then run what-if on a three-line Bicep file."),
            leo("No more paying twice. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "sa-app-architecture",
        listOf(
            maya("Okay, did you hear what happened at Two Tracks this week? Sit down. Two Tracks goes global: the Android app is a company now, with a backend on Azure, the server code the phones talk to. And this week the team rebuilt how its parts talk. It was a week."),
            leo("[laugh] I heard there was a whiteboard incident. Who went first?"),
            maya("Tom. Monday. Tom wired the paid subscriptions through Event Grid. Event Grid is a doorbell service: it pushes events, short notices that something happened, to whoever subscribed."),
            leo("And that's wrong because?"),
            maya("A payment isn't a doorbell. It's a parcel with a delivery note, a message: it must be handled exactly once, in order, never lost. Event Grid keeps no order and drops no duplicates. Two invoices for one walker, by Monday lunch."),
            leo("So what should carry the payments?"),
            maya("Service Bus, the office post room. Messages wait in a queue, a line of notes like the post office queue, until billing says done. Sessions keep one user's messages in order, first in, first out. Duplicate detection drops repeats. And failures go to the dead-letter queue, the lost-property shelf."),
            leo("And Event Grid? Is it useless, then?"),
            maya("Not at all. Priya used it the right way. When a walker uploads a profile photo, Blob Storage, Azure's store for files, tells Event Grid a blob was created, and Event Grid pushes that to a function that makes a thumbnail. Under the picture, Event Grid keeps a list of subscribers and calls each one, retrying if they miss it. Nobody polls, asking again and again, anything new?"),
            leo("Poor Tom. Tuesday?"),
            maya("Tuesday was Dana. Dana took the telemetry, the little reports every phone sends home, taps and screens and errors, and pushed it all through Service Bus. Thousands of reports a second, through the careful post room."),
            leo("Expensive?"),
            maya("Very. Telemetry is a stream, events in time order, like a till roll. That's Event Hubs: millions of events a second, kept so analysts can replay them. It splits the stream into partitions; a partition is like one lane of a motorway, with order inside each lane."),
            leo("So where does Event Hubs stop working?"),
            maya("Order across lanes, and per-message care. No dead-letter shelf, no duplicate check. So payments stay on Service Bus. And a huge, simple backlog at the lowest price? Storage queues, plain and cheap, each message up to 64 kilobytes."),
            leo("Wednesday?"),
            maya("Marcus. Marcus fixed the slow leaderboard by asking for a bigger database tier. A tier is a size and price level, like economy and business class."),
            leo("Bigger is faster, surely."),
            maya("[sigh] Bigger pays more to do the same repeated work. Priya added a cache instead: a small, very fast memory, like the timetable on the fridge instead of phoning the station. Azure Managed Redis, the current one; the older Azure Cache for Redis is being retired."),
            leo("And it just works?"),
            maya("With the cache-aside pattern. Read the cache first. On a miss, read the database, then store the answer in the cache with an expiry time. Under the picture: Redis keeps data in memory instead of on disk, and memory answers in about a millisecond."),
            leo("Thursday, I'm guessing, the intern."),
            maya("The intern. Bless them. They put the database password in App Configuration, as a plain setting. App Configuration is one central store of settings and feature flags for many apps. A feature flag is a light switch: turn the new coach feature on without a new release."),
            leo("And the password problem?"),
            maya("Anyone who can read the settings can read it. Secrets go in Key Vault, a locked safe, and App Configuration holds only a Key Vault reference: a pointer, fetch it from the safe. The Authorizing access episode has the rest."),
            leo("And Friday? There's always a Friday."),
            maya("Friday was Priya, putting a gate in front of it all: API Management. It's a gateway, one reception desk in front of every API, the menu of requests a backend accepts. Its policies, the desk's rules, run on every call: rate limits, and checking each caller's token, a short-lived pass like a festival wristband."),
            leo("And which size did she buy?"),
            maya("Developer, at first. Then someone noticed it has no SLA, a service level agreement, Microsoft's written promise of uptime. So, Standard for production. And Premium, the classic one, only if the gateway must stand in several regions, Azure's areas like West Europe and Australia East."),
            leo("And then everyone went home?"),
            maya("Tom released the fix straight to production. On a Friday. It crashed, and the rollback was a full redeploy. So now there's a pipeline, an automatic line that tests and deploys every change, with GitHub Actions, and Bicep files describing every resource, like recipe cards."),
            leo("And the crash next time?"),
            maya("A deployment slot. In App Service, on Standard and above, the pipeline deploys to the staging slot, it warms up, then a swap makes it production. That's blue-green: two stages in a theatre. Swap back to roll back, in seconds. The Choosing compute episode has more on App Service."),
            leo("So, the gossip I should remember for the exam?"),
            maya("In order, exactly once: Service Bus. React when a blob is created: Event Grid. Millions of telemetry events: Event Hubs. Same query over and over: Managed Redis with cache-aside. Settings and feature flags: App Configuration, secrets in Key Vault. And no downtime: a slot and a swap."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a Service Bus namespace, the container that holds your queues, and a queue with sessions on. Create an App Configuration store with a feature flag and a prod label. Read the Compare messaging services page, noting one requirement word per service. Then write a three-line Bicep file and run what-if."),
            leo("Never on a Friday. Got it, Tom. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "sa-app-architecture",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about how the parts of an app pass notes to each other."),
            ivy("Mm. Passing notes after dark. Whose app?"),
            nova("Two Tracks. It's gone global: the Android app is a company now, with a backend on Azure, the server code the phones talk to, and users in Europe and Australia. Its parts don't call each other and wait. They leave notes with a go-between, so one part can sleep without breaking the rest."),
            ivy("And these notes. Are they all the same?"),
            nova("Three kinds. A message is a parcel with a delivery note: someone cares that it's handled. An event is a doorbell: a short notice that something happened. And a stream is a long run of events, like a till roll, printing all night."),
            ivy("Start with the parcel. The one that matters."),
            nova("A paid subscription. It must make one invoice, in order for each user, and nothing may be lost if billing is asleep for an hour. That's Service Bus. A post room that holds each message in a queue, a line of waiting notes, until a reader says done."),
            ivy("In order, you said. How does it keep its promises?"),
            nova("Sessions. A session is a group, so one user's messages stay in line together: first in, first out. Duplicate detection drops a note sent twice. [pause] And a message that keeps failing goes to the dead-letter queue, a quiet shelf, like lost property, where a person finds it in the morning."),
            ivy("And the doorbell?"),
            nova("Event Grid. A user uploads a profile photo, and Blob Storage, Azure's store for files, rings: a blob was created. Event Grid pushes that to whoever subscribed, say a small function that makes a thumbnail. Nobody has to keep asking, is there anything new? Nobody polls."),
            ivy("I hate being asked if there's anything new."),
            nova("[chuckle] I know. Under the picture, Event Grid keeps a list of subscriptions and filters, and calls each matching subscriber, trying again if they don't answer. But it keeps no order and drops no duplicates. So never payments."),
            ivy("And the till roll?"),
            nova("Event Hubs. Telemetry, the little reports every phone sends home, screens and taps and errors, millions a second. Event Hubs takes them all and keeps them, so analysts can replay yesterday."),
            ivy("Millions. How does it breathe?"),
            nova("Partitions. A partition is like one lane of a motorway at night: order holds inside a lane, and many lanes run side by side. That's where it stops working, too. No order across lanes. And for a huge, simple backlog at the lowest price, there's a fourth: Storage queues."),
            ivy("Now the phone. Does it knock on the API's door directly?"),
            nova("The API being the menu of requests the backend accepts, like a waiter's order pad. And no. There's a front desk. API Management is a gateway, one reception desk in front of every API. Its policies are the desk's rules, like a limit of two cakes per customer."),
            ivy("And who gets past the desk?"),
            nova("Whoever shows a valid token, a short-lived signed pass, like a festival wristband. And the APIs behind the desk can move, and nobody outside notices. [pause] If the desk must stand in several regions, Azure's areas like West Europe and Australia East, that's the classic Premium tier, the top price level."),
            ivy("Mm. And the leaderboard, at this hour?"),
            nova("Cached. A cache is a small, very fast memory, like the timetable on the fridge. Azure Managed Redis. The pattern is cache-aside: look on the fridge first, and only on a miss, ask the database, then pin the answer up with an expiry time, so it never goes stale."),
            ivy("Why is it so fast? What's really in there?"),
            nova("Redis keeps its data in memory, not on a disk, so an answer comes back in about a millisecond. [pause] And if you meet the older Azure Cache for Redis in a question, it's being retired; new designs choose Azure Managed Redis. A bigger database instead? That only pays more for the same repeated work."),
            ivy("And the settings? Where does an app keep its little preferences?"),
            nova("App Configuration, one central store for many apps. Feature flags live there, light switches for features, so you can turn the new coach on without a release. [pause] But secrets, never. They go in Key Vault, the locked safe, and the settings hold only a Key Vault reference, a pointer to the safe."),
            ivy("Last thing. How does a new version arrive, without waking anyone?"),
            nova("A pipeline, an automatic line that tests and deploys each change, from GitHub Actions or Azure Pipelines. The resources themselves are written in Bicep files, like recipe cards, so every environment comes out the same. That's infrastructure as code."),
            ivy("And if the new version is a disaster?"),
            nova("Blue-green. Two stages in a theatre. In App Service, a deployment slot is the second stage, on Standard and above. The new version warms up in staging, then a swap makes it live. [long pause] Something wrong? Swap back. Seconds. The Choosing compute episode is your next late night, for where these apps run."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. In order, exactly once, means Service Bus. React when a file arrives means Event Grid. Millions of telemetry events means Event Hubs. Rate limits in front of APIs means API Management. And no downtime, with a rollback in seconds, means a staging slot and a swap."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a Service Bus namespace, the container that holds your queues, and a queue with sessions on. Create an App Configuration store and add a feature flag with a prod label. [pause] Read the Compare messaging services page and note one requirement word for each. Then run what-if on a three-line Bicep file."),
            ivy("Parcels, doorbells, and a till roll printing all night. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "sa-app-architecture",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about how to pass notes without getting caught."),
            rafe("I'm very good at passing notes. Whose app tonight?"),
            nova("[laugh] Two Tracks. It went global: the Android app is a company now, with a backend on Azure, the server code the phones talk to. Its parts pass notes through a go-between, so one part can be busy without breaking the rest."),
            rafe("And the notes come in three flavours. A message is a parcel with a delivery note. An event is a doorbell, a short notice that something happened. A stream is a till roll, events in time order, all day."),
            nova("Exactly. So, first question, exam style. Each paid subscription must be processed exactly once, in order, and never lost. Which service carries it?"),
            rafe("Service Bus. A post room that holds each message in a queue, a line of waiting notes, until the reader says done. Sessions keep one user's messages in order, and duplicate detection drops repeats."),
            nova("Right answer. Faithful, and never twice. [kiss]"),
            rafe("Mm. And what if a message keeps failing? Asking for a friend."),
            nova("It goes to the dead-letter queue, a shelf like lost property, where a person looks at it. And a topic is a queue with several readers, so billing and the welcome email both get the order. Some notes are for more than one admirer."),
            rafe("Then my question. A user uploads a profile photo to Blob Storage, Azure's store for files. A thumbnail must be made, and nobody should keep asking is there anything new. Which service?"),
            nova("Event Grid. Storage rings the doorbell, a blob was created, and Event Grid pushes it to a function that makes the thumbnail. Nobody polls."),
            rafe("Right answer. You never have to ask twice. [kiss]"),
            nova("Under the picture, Event Grid keeps the list of who subscribed, with filters, and calls each one, retrying if they don't pick up. But it keeps no order. So never for payments. Next. Every phone sends telemetry, little reports home, every second, and analysts must replay yesterday. Which service?"),
            rafe("Service Bus again. It's never let me down."),
            nova("No kiss for that one. Service Bus removes each message once it's handled, so there's nothing to replay. It's Event Hubs: millions of events a second, kept for replay. It splits the stream into partitions; a partition is like one lane of a motorway, with order only inside its lane."),
            rafe("Lanes. I can stay in my lane. And the cheapest, simplest queue of all?"),
            nova("Storage queues. Plain, cheap, huge backlogs. Now, the phone calls our API, the menu of requests the backend accepts. Who stands at the door?"),
            rafe("API Management. A gateway, one reception desk in front of every API. Its policies are house rules, like rate limits, two cakes per customer. It checks each caller's token, a short-lived signed pass like a festival wristband. A pass that doesn't last the night."),
            nova("And it stops being enough where?"),
            rafe("It's a front door, not a post room. It never holds messages. And the Developer tier, the testing price level, has no SLA, a service level agreement, Microsoft's written promise of uptime. Classic Premium stretches across several regions, Azure's areas like West Europe and Australia East."),
            nova("Mm. Then my question, darling. The leaderboard query hits the database two thousand times a minute with the same answer. What cuts the load with the least change?"),
            rafe("A bigger database tier. Go big."),
            nova("[sigh] No kiss for that one. Bigger pays more for the same repeated work. Azure Managed Redis, a cache, a fast memory like the timetable on the fridge, with cache-aside: look in the cache first, and only on a miss, ask the database, then keep the answer with an expiry time."),
            rafe("And what's really inside Redis, under the fridge picture?"),
            nova("Data kept in memory instead of on a disk, so an answer comes back in about a millisecond. And the older Azure Cache for Redis is being retired, so new designs choose Azure Managed Redis. Quick, and it remembers just enough."),
            rafe("An expiry time, though. So nothing goes stale between us. Fine. My question. Where do settings and feature flags live, for many apps?"),
            nova("App Configuration. One central store, with labels like dev and prod. A feature flag is a light switch, so the new coach can go live without a release. And the database password stays out of it: secrets go in Key Vault, with only a Key Vault reference in the settings."),
            rafe("Right answer. Some things stay locked up. [kiss]"),
            nova("Last one, love. The Choosing compute episode covers where our apps live; this is how they arrive. Releases must have no downtime, and a rollback in seconds. What do you recommend?"),
            rafe("A deployment slot. In App Service, on Standard and above, the pipeline, the automatic line that ships each change, deploys to the staging slot. It warms up, then we swap. Blue-green, two stages in a theatre. Swap back, and it's undone. Bicep files describe the resources, like recipe cards."),
            nova("Right answer. Smooth, and nobody notices the change. [kiss]"),
            rafe("Then for the exam. What do we remember?"),
            nova("In order, exactly once: Service Bus. React when a file arrives: Event Grid. Millions of telemetry events: Event Hubs. Rate limits in front of APIs: API Management. Same query again and again: Managed Redis with cache-aside. No downtime: a slot and a swap."),
            rafe("Six for six. That one earns a long one. [kiss]"),
            nova("Mm. And something to try today, darling?"),
            rafe("Create a Service Bus namespace, the container that holds the queues, and a queue with sessions on. Create an App Configuration store with a feature flag and a prod label. Read the Compare messaging services page for one requirement word each. Then run what-if on a three-line Bicep file."),
            nova("And you're the one message I'd never dead-letter. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AZ305,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun arch() = DiagramSpec(
        "The phone calls through API Management; the API reads a cache and settings, queues orders on Service Bus, and phones stream telemetry into Event Hubs.",
        nodes = listOf(
            DNode("phone", "Android app", 0.12f, 0.14f, w = 0.2f, h = 0.18f, kind = NodeKind.USER),
            DNode("apim", "API Management", 0.46f, 0.14f, w = 0.34f, h = 0.18f, sub = "the front desk"),
            DNode("api", "Tracks API", 0.83f, 0.14f, w = 0.3f, h = 0.18f, tone = 1, sub = "the backend"),
            DNode("eh", "Event Hubs", 0.18f, 0.47f, w = 0.32f, h = 0.18f, tone = 2, sub = "telemetry"),
            DNode("redis", "Managed Redis", 0.52f, 0.47f, w = 0.3f, h = 0.18f, kind = NodeKind.CYLINDER, sub = "cache-aside"),
            DNode("sb", "Service Bus", 0.84f, 0.47f, w = 0.3f, h = 0.18f, sub = "orders queue"),
            DNode("cfg", "App Configuration", 0.28f, 0.8f, w = 0.44f, h = 0.18f, tone = 2, sub = "+ Key Vault refs"),
            DNode("fn", "Billing worker", 0.8f, 0.8f, w = 0.34f, h = 0.18f, tone = 1, sub = "a function"),
        ),
        edges = listOf(
            DEdge("phone", "apim", "HTTPS"),
            DEdge("apim", "api"),
            DEdge("phone", "eh", "events"),
            DEdge("api", "redis", "read"),
            DEdge("api", "sb", "order"),
            DEdge("sb", "fn", "trigger"),
            DEdge("cfg", "api", "settings", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun messages() = DiagramSpec(
        "A message is a parcel, an event a doorbell, a stream a till roll; each has its service, and Storage queues are the plain, cheap option.",
        nodes = listOf(
            DNode("msg", "Message", 0.18f, 0.14f, w = 0.3f, h = 0.18f, kind = NodeKind.NOTE, sub = "a parcel"),
            DNode("evt", "Event", 0.5f, 0.14f, w = 0.3f, h = 0.18f, kind = NodeKind.NOTE, sub = "a doorbell"),
            DNode("str", "Stream", 0.82f, 0.14f, w = 0.3f, h = 0.18f, kind = NodeKind.NOTE, sub = "a till roll"),
            DNode("sb", "Service Bus", 0.18f, 0.48f, w = 0.3f, h = 0.2f, tone = 1, sub = "in order, once"),
            DNode("eg", "Event Grid", 0.5f, 0.48f, w = 0.3f, h = 0.2f, tone = 1, sub = "push to subscribers"),
            DNode("eh", "Event Hubs", 0.82f, 0.48f, w = 0.3f, h = 0.2f, tone = 1, sub = "millions, replay"),
            DNode("sq", "Storage queues", 0.5f, 0.84f, w = 0.5f, h = 0.18f, tone = 2, sub = "simple, cheapest"),
        ),
        edges = listOf(
            DEdge("msg", "sb"),
            DEdge("evt", "eg"),
            DEdge("str", "eh"),
        ),
        aspect = 0.8f,
    )

    private fun serviceBus() = DiagramSpec(
        "The API sends each order to a Service Bus queue; sessions keep order, duplicates are dropped, and failing messages go to the dead-letter queue.",
        nodes = listOf(
            DNode("api", "Tracks API", 0.15f, 0.2f, w = 0.26f, h = 0.2f, tone = 1, sub = "sender"),
            DNode("queue", "Orders queue", 0.5f, 0.2f, w = 0.34f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "sessions, FIFO"),
            DNode("fn", "Billing worker", 0.85f, 0.2f, w = 0.26f, h = 0.2f, tone = 1),
            DNode("topic", "Topic", 0.15f, 0.55f, w = 0.26f, h = 0.18f, sub = "several readers"),
            DNode("dlq", "Dead-letter queue", 0.5f, 0.55f, w = 0.42f, h = 0.18f, tone = 2, sub = "lost property"),
            DNode("dup", "Duplicate detection", 0.85f, 0.55f, w = 0.26f, h = 0.18f, kind = NodeKind.NOTE),
            DNode("note", "Exactly once, in order, never lost", 0.5f, 0.87f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("api", "queue", "send"),
            DEdge("queue", "fn", "receive"),
            DEdge("queue", "dlq", "keeps failing"),
            DEdge("dup", "queue", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun events() = DiagramSpec(
        "Blob Storage tells Event Grid a blob was created and Event Grid pushes it to a function; phones stream telemetry into Event Hubs, split into partitions.",
        nodes = listOf(
            DNode("blob", "Blob Storage", 0.18f, 0.15f, w = 0.32f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "photo uploaded"),
            DNode("eg", "Event Grid", 0.5f, 0.15f, w = 0.26f, h = 0.2f, tone = 2, sub = "pushes"),
            DNode("thumb", "Thumbnail function", 0.83f, 0.15f, w = 0.3f, h = 0.2f, tone = 1),
            DNode("phones", "Phones", 0.12f, 0.55f, w = 0.2f, h = 0.18f, kind = NodeKind.USER),
            DNode("eh", "Event Hubs", 0.5f, 0.55f, w = 0.3f, h = 0.2f, tone = 2, sub = "kept for replay"),
            DNode("lanes", "Partitions", 0.83f, 0.55f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE, sub = "lanes, order inside"),
            DNode("note", "Nobody polls: Event Grid pushes", 0.5f, 0.87f, w = 0.9f, h = 0.14f, kind = NodeKind.NOTE),
        ),
        edges = listOf(
            DEdge("blob", "eg", "BlobCreated"),
            DEdge("eg", "thumb", "push"),
            DEdge("phones", "eh", "telemetry"),
            DEdge("eh", "lanes"),
        ),
        aspect = 0.8f,
    )

    private fun gateway() = DiagramSpec(
        "Every call checks in at API Management first; its policies run on each call, and the tier decides the SLA and how many regions it stands in.",
        nodes = listOf(
            DNode("phone", "Android app", 0.12f, 0.2f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
            DNode("apim", "API Management", 0.5f, 0.2f, w = 0.36f, h = 0.2f, sub = "the front desk"),
            DNode("api", "Tracks API", 0.85f, 0.2f, w = 0.26f, h = 0.2f, tone = 1),
            DNode("pol", "Policies", 0.2f, 0.55f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "rate limits, tokens"),
            DNode("tiers", "Tiers", 0.7f, 0.55f, w = 0.52f, h = 0.2f, kind = NodeKind.NOTE, sub = "Developer: no SLA"),
            DNode("prem", "Classic Premium: several regions", 0.5f, 0.86f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("phone", "apim", "HTTPS"),
            DEdge("apim", "api"),
            DEdge("apim", "pol", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun cache() = DiagramSpec(
        "Cache-aside: the API looks in Managed Redis first, goes to the database only on a miss, then fills the cache with an expiry time.",
        nodes = listOf(
            DNode("api", "Tracks API", 0.18f, 0.35f, w = 0.3f, h = 0.22f, tone = 1, sub = "leaderboard"),
            DNode("redis", "Managed Redis", 0.75f, 0.15f, w = 0.4f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "1. look here first"),
            DNode("db", "Database", 0.75f, 0.55f, w = 0.4f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "2. only on a miss"),
            DNode("note", "Pin the answer with an expiry time", 0.5f, 0.87f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("api", "redis", "read"),
            DEdge("api", "db", "on a miss"),
            DEdge("db", "redis", "fill", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun config() = DiagramSpec(
        "App Configuration holds settings and feature flags for many apps; secrets stay in Key Vault, with only a reference in the settings.",
        nodes = listOf(
            DNode("api", "Tracks API", 0.5f, 0.15f, w = 0.4f, h = 0.2f, tone = 1),
            DNode("cfg", "App Configuration", 0.24f, 0.5f, w = 0.44f, h = 0.22f, sub = "settings, flags"),
            DNode("kv", "Key Vault", 0.76f, 0.5f, w = 0.4f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "secrets only"),
            DNode("flag", "Feature flag: a light switch", 0.24f, 0.85f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE),
            DNode("label", "Labels: dev and prod", 0.76f, 0.85f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("cfg", "api", "settings"),
            DEdge("kv", "api", "secret"),
            DEdge("cfg", "kv", "reference", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun deploy() = DiagramSpec(
        "Bicep files describe the resources; the pipeline deploys to the staging slot, which warms up, and a swap makes it production. Swap back to roll back.",
        nodes = listOf(
            DNode("code", "Bicep files", 0.18f, 0.15f, w = 0.3f, h = 0.2f, kind = NodeKind.NOTE, sub = "recipe cards"),
            DNode("pipe", "Pipeline", 0.5f, 0.15f, w = 0.3f, h = 0.2f, tone = 2, sub = "GitHub Actions"),
            DNode("stage", "Staging slot", 0.3f, 0.55f, w = 0.36f, h = 0.22f, sub = "new version"),
            DNode("prod", "Production slot", 0.75f, 0.55f, w = 0.36f, h = 0.22f, tone = 1, sub = "live"),
            DNode("note", "Swap back to roll back, in seconds", 0.5f, 0.87f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("code", "pipe"),
            DEdge("pipe", "stage", "deploy"),
            DEdge("stage", "prod", "swap", bidirectional = true),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's application architecture questions, each as the requirement words and the service they point to.",
        nodes = listOf(
            DNode("sb", "In order, exactly once", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Service Bus"),
            DNode("eg", "A file arrives", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Event Grid"),
            DNode("eh", "Millions of events", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Event Hubs"),
            DNode("apim", "Rate limits on APIs", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "API Management"),
            DNode("redis", "Same query again", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Redis, cache-aside"),
            DNode("slot", "No downtime", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a slot and a swap"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("sb", "eg", "eh", "apim", "redis", "slot")
    private val today = BoardCard("Today", "A Service Bus queue, a feature flag, the compare page, a what-if run.")
    private val bicep = BoardCard("Bicep", "resource plan 'Microsoft.Web/serverfarms@2023-12-01' = { sku: { name: 'S1' } }", code = true)

    private fun classicBoard() = PodcastBoard(
        "sa-app-architecture",
        listOf(
            // 0: "Hi, and welcome back. Today is application architecture..."
            Scene(
                0, "Two Tracks goes global", arch(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), card = BoardCard("Two Tracks goes global", "An Android app grown into a company, with a backend on Azure.")),
                    Beat(2, show = setOf("sb", "fn", "redis", "eh"), light = setOf("sb", "fn"), pulse = "sb" to "fn", card = BoardCard("Parts that pass notes", "A go-between lets one part be busy or down without breaking the rest.")),
                    Beat(4, light = setOf("api"), card = BoardCard("An API", "The menu of requests a backend accepts, like a waiter's order pad.")),
                ),
            ),
            // 5: "And the notes they pass. All the same kind of note?"
            Scene(
                5, "Parcel, doorbell, till roll", messages(),
                beats = listOf(
                    Beat(6, show = setOf("msg"), light = setOf("msg"), card = BoardCard("A message", "A parcel with a delivery note: someone cares that it's handled.")),
                    Beat(8, show = setOf("evt", "str"), light = setOf("evt", "str"), card = BoardCard("An event, a stream", "A doorbell: something happened. A till roll: events in order.")),
                    Beat(10, show = setOf("sb", "eg", "eh", "sq"), light = setOf("sb", "eg", "eh", "sq"), pulse = "msg" to "sb", card = BoardCard("A queue", "A line of waiting notes, like the queue at a post office.")),
                ),
            ),
            // 11: "Start with Service Bus. When do I pick it?"
            Scene(
                11, "Service Bus", serviceBus(),
                beats = listOf(
                    Beat(12, show = setOf("api", "queue", "fn", "note"), light = setOf("queue"), pulse = "api" to "queue", card = BoardCard("In order, exactly once", "A paid order waits safely until billing comes back.")),
                    Beat(14, show = setOf("dup"), light = setOf("queue", "dup"), pulse = "dup" to "queue", card = BoardCard("Sessions and duplicates", "Sessions keep one user's messages in line; repeats are dropped.")),
                    Beat(16, show = setOf("dlq", "topic"), light = setOf("dlq", "topic"), pulse = "queue" to "dlq", card = BoardCard("Dead-letter queue", "A lost-property shelf for messages that keep failing.")),
                ),
            ),
            // 17: "Now Event Grid. The doorbell."
            Scene(
                17, "Doorbells and till rolls", events(),
                beats = listOf(
                    Beat(18, show = setOf("blob", "eg", "thumb"), light = setOf("eg"), pulse = "blob" to "eg", card = BoardCard("Event Grid", "Pushes each event to whoever subscribed: a blob was created.")),
                    Beat(20, show = setOf("note"), light = setOf("note"), pulse = "eg" to "thumb", card = BoardCard("Nobody polls", "Event-driven parts react, instead of asking again and again.")),
                    Beat(22, show = setOf("phones", "eh"), light = setOf("eh"), pulse = "phones" to "eh", card = BoardCard("Event Hubs", "Millions of telemetry events a second, kept for replay.")),
                    Beat(24, show = setOf("lanes"), light = setOf("lanes"), pulse = "eh" to "lanes", card = BoardCard("Partitions", "Lanes of a motorway: order holds only inside a lane.")),
                ),
            ),
            // 25: "Okay. So the phone calls the API directly?"
            Scene(
                25, "The front desk", gateway(),
                beats = listOf(
                    Beat(26, show = setOf("phone", "apim", "api"), light = setOf("apim"), pulse = "phone" to "apim", card = BoardCard("API Management", "A gateway: one reception desk in front of all your APIs.")),
                    Beat(28, show = setOf("pol"), light = setOf("pol"), pulse = "apim" to "pol", card = BoardCard("Policies", "Rules on every call: rate limits, checking each caller's token.")),
                    Beat(30, show = setOf("tiers"), light = setOf("tiers"), card = BoardCard("Tiers and the SLA", "Developer: testing, no SLA. Basic and Standard: production.")),
                    Beat(32, show = setOf("prem"), light = setOf("prem"), card = BoardCard("A region", "A group of Azure data centres in one area, like West Europe.")),
                ),
            ),
            // 33: "Next. The leaderboard is slow."
            Scene(
                33, "The timetable on the fridge", cache(),
                beats = listOf(
                    Beat(34, show = setOf("api", "redis"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("A cache", "A fast memory for repeat answers: Azure Managed Redis.")),
                    Beat(36, show = setOf("db"), light = setOf("db"), pulse = "api" to "db", card = BoardCard("Cache-aside", "Cache first; on a miss, the database, then fill the cache.")),
                    Beat(38, show = setOf("note"), light = setOf("note"), pulse = "db" to "redis", card = BoardCard("An expiry time", "Without one, users see old scores. A bigger tier is not the fix.")),
                ),
            ),
            // 39: "Settings next. Where do they live?"
            Scene(
                39, "Settings and secrets", config(),
                beats = listOf(
                    Beat(40, show = setOf("api", "cfg", "flag", "label"), light = setOf("cfg", "flag"), pulse = "cfg" to "api", card = BoardCard("App Configuration", "One store of settings and feature flags for many apps.")),
                    Beat(42, show = setOf("kv"), light = setOf("kv"), pulse = "cfg" to "kv", card = BoardCard("Key Vault reference", "A pointer to the safe. Never a secret's value in the settings.")),
                ),
            ),
            // 43: "Last piece. Shipping new versions."
            Scene(
                43, "Shipping safely", deploy(),
                beats = listOf(
                    Beat(44, show = setOf("code", "pipe"), light = setOf("code", "pipe"), pulse = "code" to "pipe", card = bicep),
                    Beat(46, show = setOf("stage", "prod", "note"), light = setOf("stage", "prod"), pulse = "stage" to "prod", card = BoardCard("Blue-green", "Deploy to staging, warm up, swap. Swap back to roll back.")),
                ),
            ),
            // 47: "So what do I remember for the exam?"
            Scene(
                47, "For the exam", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = all6, card = BoardCard("Listen for the words", "Each requirement in the question points to one service.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "sa-app-architecture--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Customer Who Paid Twice..."
            Scene(
                0, "The Customer Who Paid Twice", serviceBus(),
                beats = listOf(
                    Beat(0, show = setOf("api", "fn"), light = setOf("fn"), card = BoardCard("The case", "Walkers charged twice; invoices in the wrong order.")),
                    Beat(2, light = setOf("api"), card = BoardCard("Clue one", "The payment note travelled through Event Grid.")),
                ),
            ),
            // 3: "Event Grid being, in plain words?"
            Scene(
                3, "Doorbell or parcel?", messages(),
                beats = listOf(
                    Beat(4, show = setOf("evt", "eg"), light = setOf("eg"), pulse = "evt" to "eg", card = BoardCard("Event Grid", "A doorbell: pushes short notices to whoever subscribed.")),
                    Beat(6, show = setOf("msg"), light = setOf("msg"), card = BoardCard("The requirement", "Exactly once, in order, never lost: that's a message.")),
                ),
            ),
            // 7: "So which service carries parcels?"
            Scene(
                7, "The post office", serviceBus(),
                beats = listOf(
                    Beat(8, show = setOf("api", "queue", "fn"), light = setOf("queue"), pulse = "api" to "queue", card = BoardCard("Service Bus", "Messages wait in a queue until a reader confirms they're done.")),
                    Beat(10, show = setOf("dup", "dlq", "note"), light = setOf("dup", "dlq"), pulse = "queue" to "dlq", card = BoardCard("Sessions, duplicates, dead-letter", "Order per user, repeats dropped, failures on a side shelf.")),
                ),
            ),
            // 11: "Case closed, then?"
            Scene(
                11, "Clue two: the bill", events(),
                beats = listOf(
                    Beat(12, show = setOf("phones", "eh"), light = setOf("phones"), card = BoardCard("Clue two", "Telemetry, thousands a second, sent through Service Bus.")),
                    Beat(14, light = setOf("eh"), pulse = "phones" to "eh", card = BoardCard("Event Hubs", "A till roll: millions of events a second, kept for replay.")),
                    Beat(16, show = setOf("lanes"), light = setOf("lanes"), pulse = "eh" to "lanes", card = BoardCard("Partitions", "Lanes: order only inside one. No dead-letter shelf.")),
                ),
            ),
            // 17: "Clue three?"
            Scene(
                17, "Clue three: old winners", cache(),
                beats = listOf(
                    Beat(18, show = setOf("api", "redis"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("A cache", "Azure Managed Redis: a fast memory for repeat answers.")),
                    Beat(20, show = setOf("db", "note"), light = setOf("note"), pulse = "api" to "db", card = BoardCard("No expiry time", "Cache-aside needs one, or old answers never come down.")),
                ),
            ),
            // 21: "Clue four. I can feel it."
            Scene(
                21, "Clue four: the settings", config(),
                beats = listOf(
                    Beat(22, show = setOf("api", "cfg", "flag", "label"), light = setOf("cfg"), card = BoardCard("A password in plain sight", "Stored as a plain value in App Configuration.")),
                    Beat(24, show = setOf("kv"), light = setOf("kv"), pulse = "cfg" to "kv", card = BoardCard("Key Vault reference", "The settings point to the safe; the app opens it with its badge.")),
                ),
            ),
            // 25: "And the last clue?"
            Scene(
                25, "The last clue", deploy(),
                beats = listOf(
                    Beat(26, show = setOf("prod"), light = setOf("prod"), card = BoardCard("Straight to production", "The new version crashed; rolling back meant a redeploy.")),
                    Beat(28, show = setOf("code", "pipe", "stage", "note"), light = setOf("stage"), pulse = "stage" to "prod", card = BoardCard("A deployment slot", "Staging warms up, then a swap. Swap back to roll back.")),
                ),
            ),
            // 29: "And the front door? Did they have one?"
            Scene(
                29, "The front door", gateway(),
                beats = listOf(
                    Beat(30, show = setOf("phone", "apim", "api", "pol"), light = setOf("apim", "pol"), pulse = "phone" to "apim", card = BoardCard("API Management", "A gateway with policies: rate limits, token checks.")),
                    Beat(32, show = setOf("tiers", "prem"), light = setOf("tiers"), card = BoardCard("The Developer tier", "No SLA: for testing. Several regions: classic Premium.")),
                ),
            ),
            // 33: "So, the case summary for the exam?"
            Scene(
                33, "Case closed", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = setOf("sb", "eg", "eh", "redis", "slot"), card = BoardCard("Five rules", "Each requirement in the case points to one service.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "sa-app-architecture--gossip",
        listOf(
            // 0: "Okay, did you hear what happened at Two Tracks this week?..."
            Scene(
                0, "The week at Two Tracks", arch(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "sb", "fn", "redis", "eh"), light = setOf("api"), card = BoardCard("Two Tracks goes global", "This week the team rebuilt how the app's parts talk.")),
                ),
            ),
            // 1: "I heard there was a whiteboard incident. Who went first?"
            Scene(
                1, "Monday: Tom", messages(),
                beats = listOf(
                    Beat(2, show = setOf("evt", "eg"), light = setOf("eg"), pulse = "evt" to "eg", card = BoardCard("Event Grid", "A doorbell: pushes short notices to whoever subscribed.")),
                    Beat(4, show = setOf("msg"), light = setOf("msg"), card = BoardCard("A payment is a parcel", "Exactly once, in order, never lost. Two invoices by lunch.")),
                ),
            ),
            // 5: "So what should carry the payments?"
            Scene(
                5, "The post room", serviceBus(),
                beats = listOf(
                    Beat(6, show = setOf("api", "queue", "fn", "dup", "dlq"), light = setOf("queue"), pulse = "api" to "queue", card = BoardCard("Service Bus", "Sessions keep order, repeats dropped, failures dead-lettered.")),
                ),
            ),
            // 7: "And Event Grid? Is it useless, then?"
            Scene(
                7, "Doorbells and till rolls", events(),
                beats = listOf(
                    Beat(8, show = setOf("blob", "eg", "thumb", "note"), light = setOf("eg"), pulse = "blob" to "eg", card = BoardCard("A blob was created", "Event Grid pushes it to a thumbnail function. Nobody polls.")),
                    Beat(10, show = setOf("phones", "eh"), light = setOf("phones"), card = BoardCard("Tuesday: Dana", "Telemetry pushed through the careful post room.")),
                    Beat(12, light = setOf("eh"), pulse = "phones" to "eh", card = BoardCard("Event Hubs", "A till roll: millions of events a second, kept for replay.")),
                    Beat(14, show = setOf("lanes"), light = setOf("lanes"), pulse = "eh" to "lanes", card = BoardCard("Where it stops", "No order across lanes, no dead-letter. Cheapest: Storage queues.")),
                ),
            ),
            // 15: "Wednesday?"
            Scene(
                15, "Wednesday: Marcus", cache(),
                beats = listOf(
                    Beat(16, show = setOf("api", "db"), light = setOf("db"), pulse = "api" to "db", card = BoardCard("A bigger tier", "It pays more to do the same repeated work.")),
                    Beat(18, show = setOf("redis"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("A cache", "Azure Managed Redis: the timetable on the fridge.")),
                    Beat(20, show = setOf("note"), light = setOf("note"), pulse = "db" to "redis", card = BoardCard("Cache-aside", "Cache first; on a miss, the database, then fill with an expiry.")),
                ),
            ),
            // 21: "Thursday, I'm guessing, the intern."
            Scene(
                21, "Thursday: the intern", config(),
                beats = listOf(
                    Beat(22, show = setOf("api", "cfg", "flag", "label"), light = setOf("cfg"), card = BoardCard("A password as a setting", "Anyone who reads the settings reads the password.")),
                    Beat(24, show = setOf("kv"), light = setOf("kv"), pulse = "cfg" to "kv", card = BoardCard("Key Vault reference", "Settings point to the safe. Secrets live only in Key Vault.")),
                ),
            ),
            // 25: "And Friday? There's always a Friday."
            Scene(
                25, "Friday: Priya", gateway(),
                beats = listOf(
                    Beat(26, show = setOf("phone", "apim", "api", "pol"), light = setOf("apim", "pol"), pulse = "phone" to "apim", card = BoardCard("API Management", "One reception desk: rate limits and token checks on every call.")),
                    Beat(28, show = setOf("tiers", "prem"), light = setOf("tiers", "prem"), card = BoardCard("The Developer tier", "No SLA, so Standard for production; classic Premium for regions.")),
                ),
            ),
            // 29: "And then everyone went home?"
            Scene(
                29, "Friday, later: Tom", deploy(),
                beats = listOf(
                    Beat(30, show = setOf("prod", "code", "pipe"), light = setOf("prod"), card = BoardCard("Straight to production", "On a Friday. A crash, and a full redeploy to roll back.")),
                    Beat(32, show = setOf("stage", "note"), light = setOf("stage"), pulse = "stage" to "prod", card = BoardCard("A deployment slot", "Deploy to staging, then swap. Swap back in seconds.")),
                ),
            ),
            // 33: "So, the gossip I should remember for the exam?"
            Scene(
                33, "For the exam", exam(),
                beats = listOf(
                    Beat(34, show = all6, light = all6, card = BoardCard("Listen for the words", "Each requirement points to one service.")),
                    Beat(36, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "sa-app-architecture--late-night",
        listOf(
            // 0: "Hey, you. It's late..."
            Scene(
                0, "Passing notes after dark", arch(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("api"), card = BoardCard("Tonight", "How an app's parts pass notes through a go-between.")),
                    Beat(2, show = setOf("sb", "fn", "eh", "redis"), light = setOf("sb", "eh"), pulse = "api" to "sb", card = BoardCard("Two Tracks goes global", "Parts leave notes, so one can sleep without breaking the rest.")),
                ),
            ),
            // 3: "And these notes. Are they all the same?"
            Scene(
                3, "Three kinds of note", messages(),
                beats = listOf(
                    Beat(4, show = setOf("msg", "evt", "str"), light = setOf("msg", "evt", "str"), card = BoardCard("Parcel, doorbell, till roll", "A message, an event, a stream.")),
                ),
            ),
            // 5: "Start with the parcel. The one that matters."
            Scene(
                5, "The parcel", serviceBus(),
                beats = listOf(
                    Beat(6, show = setOf("api", "queue", "fn", "note"), light = setOf("queue"), pulse = "api" to "queue", card = BoardCard("Service Bus", "Holds each message in a queue until a reader says done.")),
                    Beat(8, show = setOf("dup", "dlq"), light = setOf("dup", "dlq"), pulse = "queue" to "dlq", card = BoardCard("Sessions and dead-letter", "Order per user; failing messages wait on a quiet shelf.")),
                ),
            ),
            // 9: "And the doorbell?"
            Scene(
                9, "Doorbell and till roll", events(),
                beats = listOf(
                    Beat(10, show = setOf("blob", "eg", "thumb"), light = setOf("eg"), pulse = "blob" to "eg", card = BoardCard("Event Grid", "A blob was created: pushed to a thumbnail function.")),
                    Beat(12, show = setOf("note"), light = setOf("eg"), pulse = "eg" to "thumb", card = BoardCard("Under the picture", "Subscribers called one by one, with retries. No order kept.")),
                    Beat(14, show = setOf("phones", "eh"), light = setOf("eh"), pulse = "phones" to "eh", card = BoardCard("Event Hubs", "Telemetry, millions a second, kept for replay.")),
                    Beat(16, show = setOf("lanes"), light = setOf("lanes"), pulse = "eh" to "lanes", card = BoardCard("Partitions", "Lanes of a motorway: order holds only inside one.")),
                ),
            ),
            // 17: "Now the phone. Does it knock on the API's door directly?"
            Scene(
                17, "The front desk", gateway(),
                beats = listOf(
                    Beat(18, show = setOf("phone", "apim", "api", "pol"), light = setOf("apim"), pulse = "phone" to "apim", card = BoardCard("API Management", "A gateway in front of every API, with policies.")),
                    Beat(20, show = setOf("tiers", "prem"), light = setOf("prem"), card = BoardCard("A token", "A short-lived signed pass. Several regions: classic Premium.")),
                ),
            ),
            // 21: "Mm. And the leaderboard, at this hour?"
            Scene(
                21, "The fridge", cache(),
                beats = listOf(
                    Beat(22, show = setOf("api", "redis", "db", "note"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("Cache-aside", "Cache first; on a miss, the database, then an expiry time.")),
                    Beat(24, light = setOf("redis"), pulse = "db" to "redis", card = BoardCard("In memory", "About a millisecond. Azure Cache for Redis is being retired.")),
                ),
            ),
            // 25: "And the settings? Where does an app keep its little preferences?"
            Scene(
                25, "Little preferences", config(),
                beats = listOf(
                    Beat(26, show = setOf("api", "cfg", "kv", "flag", "label"), light = setOf("cfg", "kv"), pulse = "cfg" to "kv", card = BoardCard("App Configuration", "Settings and feature flags; secrets only by Key Vault reference.")),
                ),
            ),
            // 27: "Last thing. How does a new version arrive, without waking anyone?"
            Scene(
                27, "Arriving quietly", deploy(),
                beats = listOf(
                    Beat(28, show = setOf("code", "pipe"), light = setOf("code", "pipe"), pulse = "code" to "pipe", card = BoardCard("Infrastructure as code", "Bicep files and a pipeline: every environment the same.")),
                    Beat(30, show = setOf("stage", "prod", "note"), light = setOf("stage", "prod"), pulse = "stage" to "prod", card = BoardCard("A deployment slot", "Warm up in staging, then swap. Swap back in seconds.")),
                ),
            ),
            // 31: "Mm. Before you send me off to bed. What do I remember for the exam?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = setOf("sb", "eg", "eh", "apim", "slot"), card = BoardCard("Five little things", "Each requirement points to one service.")),
                    Beat(34, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "sa-app-architecture--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", arch(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), light = setOf("phone"), card = BoardCard("Tonight", "How to pass notes without getting caught.")),
                    Beat(2, show = setOf("sb", "eh", "redis", "fn"), light = setOf("api", "sb"), pulse = "api" to "sb", card = BoardCard("Two Tracks goes global", "Its parts pass notes through a go-between.")),
                ),
            ),
            // 3: "And the notes come in three flavours..."
            Scene(
                3, "Three flavours", messages(),
                beats = listOf(
                    Beat(3, show = setOf("msg", "evt", "str"), light = setOf("msg", "evt", "str"), card = BoardCard("Parcel, doorbell, till roll", "A message, an event, a stream.")),
                    Beat(5, show = setOf("sb"), light = setOf("sb"), pulse = "msg" to "sb", card = BoardCard("Exactly once, in order", "Service Bus: a post room, with sessions and duplicate detection.")),
                ),
            ),
            // 7: "Mm. And what if a message keeps failing? Asking for a friend."
            Scene(
                7, "Notes that keep failing", serviceBus(),
                beats = listOf(
                    Beat(8, show = setOf("api", "queue", "fn", "dlq", "topic"), light = setOf("dlq", "topic"), pulse = "queue" to "dlq", card = BoardCard("Dead-letter queue", "A lost-property shelf. A topic: one message, several readers.")),
                ),
            ),
            // 9: "Then my question. A user uploads a profile photo..."
            Scene(
                9, "Doorbells and lanes", events(),
                beats = listOf(
                    Beat(10, show = setOf("blob", "eg", "thumb"), light = setOf("eg"), pulse = "blob" to "eg", card = BoardCard("Event Grid", "A blob was created: pushed to a function that makes a thumbnail.")),
                    Beat(12, show = setOf("note"), light = setOf("eg", "note"), pulse = "eg" to "thumb", card = BoardCard("Under the picture", "Subscribers called one by one, with retries. Never payments.")),
                    Beat(14, show = setOf("phones", "eh", "lanes"), light = setOf("eh", "lanes"), pulse = "phones" to "eh", card = BoardCard("Event Hubs", "Telemetry kept for replay, split into partitions: lanes.")),
                ),
            ),
            // 16: "Storage queues. Plain, cheap, huge backlogs. Now, the phone..."
            Scene(
                16, "Who stands at the door", gateway(),
                beats = listOf(
                    Beat(17, show = setOf("phone", "apim", "api", "pol"), light = setOf("apim", "pol"), pulse = "phone" to "apim", card = BoardCard("API Management", "A gateway with policies: rate limits and token checks.")),
                    Beat(19, show = setOf("tiers", "prem"), light = setOf("tiers", "prem"), card = BoardCard("Where it stops", "Not a post room. Developer: no SLA. Several regions: Premium.")),
                ),
            ),
            // 20: "Mm. Then my question, darling. The leaderboard query..."
            Scene(
                20, "Two thousand a minute", cache(),
                beats = listOf(
                    Beat(20, show = setOf("api", "db"), light = setOf("db"), pulse = "api" to "db", card = BoardCard("The question", "The same leaderboard query, two thousand times a minute.")),
                    Beat(22, show = setOf("redis", "note"), light = setOf("redis"), pulse = "api" to "redis", card = BoardCard("Cache-aside", "Azure Managed Redis first; the database only on a miss.")),
                    Beat(24, light = setOf("redis"), card = BoardCard("In memory", "About a millisecond. Azure Cache for Redis is being retired.")),
                ),
            ),
            // 25: "An expiry time, though... My question. Where do settings..."
            Scene(
                25, "Settings and secrets", config(),
                beats = listOf(
                    Beat(26, show = setOf("api", "cfg", "kv", "flag", "label"), light = setOf("cfg", "kv"), pulse = "cfg" to "kv", card = BoardCard("App Configuration", "Settings and flags; secrets by Key Vault reference only.")),
                ),
            ),
            // 28: "Last one, love. ... Releases must have no downtime..."
            Scene(
                28, "No downtime", deploy(),
                beats = listOf(
                    Beat(29, show = setOf("code", "pipe", "stage", "prod", "note"), light = setOf("stage", "prod"), pulse = "stage" to "prod", card = BoardCard("A deployment slot", "Warm up in staging, then swap; swap back to undo.")),
                ),
            ),
            // 31: "Then for the exam. What do we remember?"
            Scene(
                31, "For the exam", exam(),
                beats = listOf(
                    Beat(32, show = all6, light = all6, card = BoardCard("Six for six", "Each requirement points to one service.")),
                    Beat(35, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
