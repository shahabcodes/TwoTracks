package com.shahaabapps.core.content

/** Containers on App Service: settings, secrets and slots: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesAppServiceContainers {
    private val classic = Podcast(
        "ai-appservice-containers",
        listOf(
            maya("Hi, and welcome back. Zaiqa is the food delivery app we build all course. Last time we packed the Zaiqa API, the dot NET 9 backend every screen calls, into an image and pushed it to the registry. Today we try to run it."),
            leo("The image is on the shelf doing nothing. Where does it actually run?"),
            maya("We try App Service first. App Service is Azure's managed web hosting: you bring the app, and Azure runs the building. The machines, the patching, the web address, the certificate."),
            leo("Like renting a furnished flat instead of building a house."),
            maya("That is the picture. Usually you hand it your code and it picks a ready-made dot NET setup. It can also run your own image instead, and that is called a custom container. Ours already holds dot NET 9 and Entity Framework Core."),
            leo("The image sits in a private registry. How does App Service get in? Do I paste a registry password into a box?"),
            maya("You do not. You give the app a managed identity, an account Azure creates and looks after for it, like a staff badge the building issues. Then you grant that badge the AcrPull role on the registry: download images, nothing else."),
            leo("And App Service knows to use the badge?"),
            maya("Only after one switch. In the app's configuration there is a setting called acrUseManagedIdentityCreds. Turn it on, and every pull uses the badge. Miss it, and the pull fails while you swear the role is right."),
            leo("Fine. It pulled, it started. How does my code learn the Azure SQL connection? Zaiqa's orders and payments live there."),
            maya("Through app settings. An app setting is a name and a value you type into App Service, and when the container starts, every one of them is handed over as an ordinary environment variable. The code reads them like any other."),
            leo("So one image runs in test and in production, with different settings."),
            maya("That is the whole point. And one warning worth tattooing: adding, editing or removing an app setting restarts the app, so every instance's container starts again."),
            leo("There is also a Connection strings box in the portal. A prettier app setting?"),
            maya("Nearly, with one trap. A connection string reaches the container with a prefix glued to the front of its name, a different prefix for each type: one for Azure SQL, one for PostgreSQL, one for custom."),
            leo("So my code looks for ZaiqaDb and finds nothing."),
            maya("Dot NET knows the prefixes and copes. For example, our Python taste service would miss it completely. Which is why most teams keep everything in plain app settings instead."),
            leo("And the payment provider key? I am not typing that into the portal."),
            maya("Please do not. That key lives in Key Vault, Azure's safe for secrets. In App Service you make the setting a Key Vault reference: the value is not the key, it is a note saying fetch this secret from that safe."),
            leo("And who opens the safe?"),
            maya("The same badge, once it holds the Key Vault Secrets User role. App Service resolves the reference at start-up, so the container only sees the finished value. Forget the role and nothing resolves: the app gets the reference text itself. Look for the green tick in the portal."),
            leo("Right. We browse to the site, and it just hangs."),
            maya("[chuckle] The classic first afternoon. Our container listens on port 8080, which is what dot NET uses inside a container now. App Service knocks on port 80."),
            leo("A port being a numbered door on the machine?"),
            maya("One door among many, each with its own number. The fix is a single app setting, the websites port setting, set to 8080. Traffic goes to the right door, and the site answers."),
            leo("What else bites on the first deploy?"),
            maya("The clock. App Service waits about 230 seconds for the container to answer after it starts. Our backend checks its Azure SQL connection on the way up, and on a cold machine that can run long."),
            leo("And then it gives up?"),
            maya("It reports that the container did not respond to pings, and restarts it. You raise the wait with the container start time limit setting, as far as 1800 seconds. Treat a slow start as a smell, though, not a fix."),
            leo("Say one instance goes bad at dinner time. Does App Service notice?"),
            maya("If you tell it where to look. Set a health check path, say slash healthz, and App Service pings that path on every instance. One that keeps failing is taken out of rotation and eventually replaced."),
            leo("Out of rotation meaning no customer reaches it."),
            maya("No new requests, yes. One catch the exam likes: it only helps with two or more instances. With one instance there is nowhere else to send the traffic."),
            leo("Releases next. How do I put a new build live without a scary minute?"),
            maya("With a deployment slot. A slot is a second live copy of the app, usually called staging, with its own web address. You deploy the new image there, warm it up, click around, then swap."),
            leo("And the swap is instant?"),
            maya("To a customer, yes. App Service puts production's settings onto staging first and waits until it is warm, then switches the routing. Production never takes a cold start, and swapping back is your rollback."),
            leo("Wait. If production's settings go to staging, does staging's test database land in production?"),
            maya("That is the trap, and the answer is a slot setting. Tick deployment slot setting on a value and it stays with its slot through a swap. Our staging slot keeps its own copy of the Zaiqa database, so nobody tests against real orders."),
            leo("Picture a one-person recipe app. Do they get slots too?"),
            maya("Only on Standard tier or higher. Basic has no slots at all, and the exam loves that detail."),
            leo("Now, I push a new image to the registry. Does App Service notice?"),
            maya("Not by itself. Turn on continuous deployment, and App Service hands the registry a webhook, a short message the registry sends on every push to the tag it watches. The app hears it, pulls, and restarts."),
            leo("[sigh] And when it all goes wrong, where do I look?"),
            maya("At what the container printed. Turn on container logging to the file system with az webapp log config, then watch it live with az webapp log tail. Azure's start-up lines and your own dot NET logging come through together."),
            leo("So the backend runs. Why does the course not stop here?"),
            maya("Because Zaiqa is not one web app. The taste service is a second container, the order worker is a third, and the worker has no web address at all. It just reads the orders queue and gets on with it."),
            leo("And App Service wants a web app."),
            maya("App Service is built around one web app, with small helper containers beside it. Its plan is a rented machine, always on, and it scales on things like processor use or a timetable, not on how many requests are in flight."),
            leo("And Zaiqa is asleep at four and on fire at eight on Friday."),
            maya("[pause] Which is next episode. Container Apps scales on requests in flight and on the depth of the orders queue, and goes to zero when nobody is ordering. Each release is a revision, a numbered version that can take a slice of the traffic."),
            leo("So what do I remember for the exam?"),
            maya("Five phrases. Container never answers and it listens on 8080: the websites port setting. A key that must stay in the safe: a Key Vault reference, resolved by the app's identity. Staging must keep its own database across a swap: a slot setting."),
            leo("And the last two?"),
            maya("Pull with no password: a managed identity with AcrPull, and that credentials switch turned on. Killed before a slow container answers: raise the container start time limit, as far as 1800 seconds."),
            leo("And what should I try today?"),
            maya("Create a Linux web app from a public sample image. Add an app setting and find it printed in the container's log. Set the websites port setting to match the image and browse to the site. Then turn on container logging and watch az webapp log tail while it restarts."),
            leo("One door number, and a tail of logs. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-appservice-containers",
        listOf(
            maya("Welcome to Case Files. Today's case: The Orders That Went To The Wrong Database. Zaiqa, the food delivery app we are building, released a new version of the Zaiqa API, the dot NET 9 backend behind every screen. By nine on Friday, four hundred real orders sat in a test database."),
            leo("Detective Leo, reporting. Four hundred dinners in the wrong drawer. Where was the backend running?"),
            maya("On App Service, Azure's managed web hosting: Azure runs the machines, the patching and the web address, and you bring the app. The team had moved the image we shelved in the registry last time onto it."),
            leo("Moved the image. So App Service runs our own container, not its own dot NET setup?"),
            maya("That is a custom container: your own image, pulled and started instead of a ready-made stack. Ours carries dot NET 9 and Entity Framework Core already."),
            leo("First suspect, then. Someone typed the test database into production by hand."),
            maya("The log of changes says nobody touched it. Here is the first clue: the release went out through a deployment slot. A slot is a second live copy of the app, called staging, with its own web address."),
            leo("And then you swap, and staging becomes production."),
            maya("Exactly. The slots trade places. App Service puts production's settings onto staging first, waits until it is warm, then switches the routing, so no customer waits for a cold start."),
            leo("Then the settings travelled. Why did the database not travel back?"),
            maya("Because one setting had been marked a slot setting, and one had not. A slot setting stays with its slot through a swap, which is exactly what you want for staging's test database."),
            leo("So they marked the wrong one."),
            maya("They marked the connection to the message queue, and forgot the database. The database setting travelled into production with the new image, and production began writing real orders into the test copy."),
            leo("[sigh] Case closed?"),
            maya("Not yet. There is a second file on this desk from the same release, and it explains why nobody noticed for an hour."),
            leo("Go on."),
            maya("When the new image first went to staging, the site did not answer at all. The container listens on port 8080, which is what dot NET uses inside a container now, and App Service knocks on port 80."),
            leo("A port being a numbered door on the machine. So the knocking went to an empty room."),
            maya("One app setting fixes it: the websites port setting, set to 8080. An app setting is a name and a value you type in, handed to the container as an ordinary environment variable when it starts."),
            leo("And they had been staring at the registry instead."),
            maya("Which brings suspect three, cleared quickly. The app pulls with a managed identity, an account Azure creates and looks after for the app, like a staff badge. That badge holds the AcrPull role: download images, nothing else."),
            leo("And there is a switch for it, if I remember."),
            maya("acrUseManagedIdentityCreds, in the app's configuration. It was on. Had it been off, the pull itself would have failed, and no container would ever have started."),
            leo("So the pull was innocent. What about the hour of silence after the fix?"),
            maya("Clue four. The container was killed twice while starting. App Service waits about 230 seconds for a container to answer, and this one checks its Azure SQL connection on the way up."),
            leo("On a cold machine, longer than 230 seconds."),
            maya("And then it reports that the container did not respond to pings. The team raised the container start time limit setting, as far as 1800 seconds, which bought the time. A slow start is still a smell."),
            leo("Next suspect. The payment key was sitting in a settings box, I bet."),
            maya("Not here, happily. The payment provider key lives in Key Vault, Azure's safe for secrets, and the setting is a Key Vault reference: a note saying fetch this secret from that safe, not the secret itself."),
            leo("And the badge opens the safe."),
            maya("With the Key Vault Secrets User role, yes. Forget the role and nothing resolves, and the app uses the reference text as its value. The portal shows a green tick beside one that resolved."),
            leo("So why did only some customers see broken orders?"),
            maya("Last clue, and my favourite. The app runs three instances, and one of them had been restarting all evening. Nobody had set a health check path."),
            leo("And a health check does what, exactly?"),
            maya("You give App Service a path, say slash healthz, and it pings that path on every instance. One that keeps failing is taken out of rotation and eventually replaced. It only helps with two instances or more."),
            leo("So with the check on, the bad instance stops getting customers."),
            maya("Yes. And one thing the team did get right: continuous deployment. App Service hands the registry a webhook, a short message the registry sends on every push to the watched tag, and the app pulls and restarts."),
            leo("And how did they finally see any of this?"),
            maya("Container logging. Turn it on to the file system with az webapp log config, then watch live with az webapp log tail. Azure's start-up lines and our own dot NET logging arrive in one stream."),
            leo("Case closed, then?"),
            maya("Closed, with one note for the file. This app should not have been on App Service at all. Zaiqa is three containers, and the order worker has no web address: it only reads the orders queue."),
            leo("And App Service is built around a web app."),
            maya("One web app with small helpers beside it, on a rented machine that is always on, scaling on processor use or a timetable. Container Apps, next episode, scales on requests in flight and on queue depth, and can go to zero."),
            leo("Where does the slot trick stop working, by the way?"),
            maya("On Basic. Slots need Standard tier or higher, and that detail turns up in exam questions. And the phone never holds the payment key: it calls our backend, and even that reads the vault."),
            leo("So the case summary for the exam?"),
            maya("Staging must keep its own database across a swap: mark it a slot setting. The container never answers and listens on 8080: the websites port setting. A key that must stay in the safe: a Key Vault reference."),
            leo("And the quieter two?"),
            maya("Killed before a slow container answers: raise the container start time limit. Pull with no password: a managed identity holding AcrPull, with that credentials switch on."),
            leo("And anything to try today?"),
            maya("Create a Linux web app from a public sample image. Add an app setting and find it printed in the container's log. Set the websites port setting to match the image and browse to the site. Then turn on container logging and watch az webapp log tail while it restarts."),
            leo("Right drawer, right door. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-appservice-containers",
        listOf(
            maya("Sit down. Did you hear what happened on the Zaiqa team this week? They finally tried to run the Zaiqa API, the dot NET 9 backend behind every screen of our food delivery app, on App Service. Five days, five disasters."),
            leo("Five? Start on Monday."),
            maya("Monday was Tom. App Service is Azure's managed web hosting: it runs the machines and the web address, and it can run your own image instead of its ready-made dot NET setup. That is a custom container."),
            leo("And Tom's image?"),
            maya("Pulled beautifully. The app has a managed identity, an account Azure creates and keeps for it, like a staff badge, and the badge holds AcrPull: download images, nothing else. Tom even remembered the switch, acrUseManagedIdentityCreds."),
            leo("So Monday was a success story. Disappointing."),
            maya("[chuckle] Then he browsed to the site, and it hung for twenty minutes. Our container listens on port 8080, the port dot NET uses in a container now. App Service knocks on port 80."),
            leo("A port being a numbered door, and nobody behind that one."),
            maya("One app setting fixes it: the websites port setting, set to 8080. An app setting is a name and a value you type into App Service, handed to the container as an ordinary environment variable at start."),
            leo("Tuesday. Who?"),
            maya("Dana, and the payment provider key. She pasted it straight into an app setting, where anyone who can read the app's configuration can read the key."),
            leo("You hear that, and you wince."),
            maya("The key belongs in Key Vault, Azure's safe for secrets. The setting becomes a Key Vault reference: not the key, a note saying fetch this secret from that safe. App Service resolves it at start-up with the same badge."),
            leo("And the badge needs permission on the safe, presumably."),
            maya("The Key Vault Secrets User role. Dana's first try had no role, so nothing resolved, and the app happily used the reference text itself as the key. The portal shows a green tick when it resolves."),
            leo("Wednesday."),
            maya("Marcus, and the clock. His release kept dying on the way up. App Service waits about 230 seconds for a container to answer, and ours checks its Azure SQL connection while it starts."),
            leo("And on a cold machine that is slow."),
            maya("It reported that the container did not respond to pings, over and over. He raised the container start time limit, as far as 1800 seconds. Which works, and which he now tells everyone is a smell, not a fix."),
            leo("Thursday, and I sense something expensive."),
            maya("Thursday was Priya, and the swap. She released through a deployment slot: a second live copy of the app called staging, with its own web address. You warm it up, then swap, and the slots trade places."),
            leo("No downtime. I will admit that is nice."),
            maya("It is: App Service puts production's settings onto staging and waits until it is warm before switching the routing. But most settings travel with the swap, and Priya had not marked the database."),
            leo("So production woke up pointing at the test database."),
            maya("For about forty minutes. The fix is one tick: a slot setting stays with its slot through a swap. Staging keeps its test copy of the Zaiqa database, production keeps the real one."),
            leo("And Friday? Please tell me it was the intern."),
            maya("The intern pushed a new image and announced the release in the channel. Nothing changed. App Service only pulls when you tell it to, or when continuous deployment is on."),
            leo("Which does what?"),
            maya("App Service hands the registry a webhook, a short message the registry sends on every push to the tag it watches. The app hears it, pulls the new image and restarts. Without it, the old image keeps serving dinner."),
            leo("And nobody noticed for how long?"),
            maya("Two hours, because nobody had turned on container logging. You switch it on to the file system with az webapp log config, then watch az webapp log tail and see every start-up line as it happens."),
            leo("Did anything break for customers?"),
            maya("One instance of three kept restarting all evening, and a third of the orders failed. There was no health check path set."),
            leo("Which would have done what?"),
            maya("You give App Service a path, say slash healthz, and it pings that path on every instance. One that keeps failing is taken out of rotation and eventually replaced. It needs two instances or more to help."),
            leo("So after all that, is Zaiqa staying on App Service?"),
            maya("No, and that is the real gossip. Zaiqa is three containers: the backend, the taste service and the order worker, and the worker has no web address at all. It only reads the orders queue."),
            leo("And App Service wants a web app."),
            maya("One web app with small helpers beside it, on a rented machine that is always on, scaling on processor use or a timetable. Zaiqa is asleep at four and on fire at eight on Friday."),
            leo("So where do they go?"),
            maya("Container Apps, next episode. It scales on requests in flight and on the depth of the orders queue, down to zero, and each release becomes a revision, a numbered version that can take a slice of the traffic."),
            leo("One more thing. Did anyone try slots on a cheap plan?"),
            maya("Dana did. Slots need Standard tier or higher; Basic has none, and the exam enjoys asking that."),
            leo("So what do I remember for the exam?"),
            maya("Container never answers and it listens on 8080: the websites port setting. A key that must stay out of the configuration: a Key Vault reference, resolved by the app's identity. Staging keeps its own database across a swap: a slot setting."),
            leo("And the rest of the week?"),
            maya("A push that changes nothing: the registry webhook and continuous deployment. A container killed while starting: the container start time limit, up to 1800 seconds. A pull with no password: the identity with AcrPull."),
            leo("And what should I try today?"),
            maya("Create a Linux web app from a public sample image. Add an app setting and find it printed in the container's log. Set the websites port setting to match the image and browse to the site. Then turn on container logging and watch az webapp log tail while it restarts."),
            leo("Five days, five lessons. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-appservice-containers",
        listOf(
            nova("It is late, you are still up, and so are we. Tonight: the night the Zaiqa API, the dot NET 9 backend behind every screen of our food delivery app, moved out of the registry and went looking for a home."),
            ivy("Went looking. I like a story with a limp in it."),
            nova("[pause] We start at App Service: Azure's managed web hosting. You bring the app, Azure runs the building. The machines, the patching, the web address, the certificate on it."),
            ivy("And our app arrives as an image, not as code."),
            nova("Which App Service is happy with. Hand it your own image and it runs that instead of a ready-made dot NET setup. That is a custom container, and ours already carries dot NET 9 and Entity Framework Core."),
            ivy("And how does it reach into the registry at that hour? A password taped to the door?"),
            nova("Nothing so sad. The app gets a managed identity, an account Azure makes and looks after for it, like a staff badge the building issues. The badge holds AcrPull: download images, nothing else."),
            ivy("And someone has to tell the door to accept badges."),
            nova("One switch in the app's configuration, acrUseManagedIdentityCreds. On, and every pull uses the badge. Off, and you will spend an hour certain the role is wrong."),
            ivy("[pause] So it pulled. Did it answer?"),
            nova("Not a word. Our container listens on port 8080, because that is what dot NET uses inside a container now, and App Service was knocking politely on port 80."),
            ivy("A port being a numbered door. Knocking on the wrong one all evening."),
            nova("One app setting ends it: the websites port setting, 8080. An app setting is a name and a value you type into App Service, and every one is handed to the container as an ordinary environment variable when it starts."),
            ivy("So the same image can live two different lives."),
            nova("Test and production, same image, different settings. Just know that adding or editing one restarts the app, so every instance's container starts again."),
            ivy("And the Azure SQL connection? Zaiqa's orders live there, you told me."),
            nova("There is a connection strings box too, with one trap in it: the value reaches the container with a prefix glued to the front of its name, a different prefix per type. Dot NET copes; most other stacks do not."),
            ivy("And the payment key? Say something comforting."),
            nova("It never touches a settings box. It sits in Key Vault, Azure's safe for secrets, and the setting is a Key Vault reference: a note saying fetch this secret from that safe. The same badge opens it, with the Key Vault Secrets User role."),
            ivy("And if the badge has no role?"),
            nova("[pause] Then the app reads the note out loud as if it were the key. The portal puts a green tick beside a reference that resolved, and that tick is worth a glance at one in the morning."),
            ivy("The container came up, then. Slowly?"),
            nova("Twice it did not come up at all. App Service waits about 230 seconds for an answer, and ours was checking its database connection on the way up."),
            ivy("And it just gives up on you."),
            nova("It reports that the container did not respond to pings. You raise the container start time limit, as far as 1800 seconds. But a start that slow is telling you something."),
            ivy("What about the one bad instance that ruins everyone's dinner?"),
            nova("You give App Service a health check path, say slash healthz. It pings that path on every instance, and one that keeps failing is taken out of rotation and eventually replaced. It needs two instances or more."),
            ivy("Now the part I like. Shipping at night."),
            nova("A deployment slot. A second live copy of the app, called staging, with its own web address. You put the new image there, warm it up, walk around it, and then swap."),
            ivy("And the swap is the romantic bit."),
            nova("[pause] It is. App Service puts production's settings onto staging, waits until it is warm, then switches the routing. Nobody waits for a cold start, and swapping back is your way home."),
            ivy("Settings travel, then. All of them?"),
            nova("All but the ones you pin. Tick deployment slot setting on a value and it is a slot setting: it stays with its slot through a swap. Staging keeps the test database, production keeps the real one."),
            ivy("And slots on the cheapest plan?"),
            nova("Standard tier or higher. Basic has none at all, which the exam remembers more often than you will."),
            ivy("Someone pushes a new image while we sleep. Does anything happen?"),
            nova("Only with continuous deployment on. Then App Service hands the registry a webhook, a short message the registry sends on every push to the watched tag, and the app pulls and restarts by itself."),
            ivy("And when it all goes quietly wrong at three in the morning?"),
            nova("Container logging. Switch it on to the file system with az webapp log config, then watch az webapp log tail. Azure's start-up lines and our own dot NET logging, in one stream, live."),
            ivy("So Zaiqa lives happily on App Service forever."),
            nova("[pause] No. Zaiqa is three containers, and the order worker has no web address at all; it just reads the orders queue. App Service is shaped around one web app with small helpers beside it."),
            ivy("And it is always on, I suppose."),
            nova("A rented machine, scaling on processor use or a timetable. Container Apps, next time, scales on requests in flight and on queue depth, and goes to zero when nobody is ordering. Each release is a revision, a numbered version that can take a slice of traffic."),
            ivy("Before I fall asleep on you. What do I remember for the exam?"),
            nova("Container never answers and it listens on 8080: the websites port setting. A key that must stay in the safe: a Key Vault reference. Staging keeps its own database across a swap: a slot setting."),
            ivy("Two more, and then bed."),
            nova("A pull with no password: a managed identity holding AcrPull, with that credentials switch on. A container killed while starting: the container start time limit, up to 1800 seconds."),
            ivy("And what do I try today, before the kettle goes cold?"),
            nova("Create a Linux web app from a public sample image. Add an app setting and find it printed in the container's log. Set the websites port setting to match the image and browse to the site. Then turn on container logging and watch az webapp log tail while it restarts."),
            ivy("Right door, warm kettle. Goodnight, you."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-appservice-containers",
        listOf(
            nova("Tonight is date night, darling, and I have brought work to the table. The Zaiqa API, the dot NET 9 backend behind every screen of our food delivery app, is finally leaving the registry."),
            rafe("Moving in somewhere. How serious of it."),
            nova("We are trying App Service first: Azure's managed web hosting. It runs the machines, the patching and the web address, and you bring the app. Think of a furnished flat."),
            rafe("And we arrive with our own image, not its ready-made dot NET setup."),
            nova("That is a custom container, and ours already carries dot NET 9 and Entity Framework Core. First question, love. How does App Service get into our private registry with no password?"),
            rafe("A managed identity, an account Azure makes and keeps for the app, like a staff badge. With the AcrPull role, so it can download images and nothing else."),
            nova("And the switch nobody remembers?"),
            rafe("acrUseManagedIdentityCreds, in the app's configuration. On, and every pull uses the badge."),
            nova("Perfect, both halves. [kiss]"),
            rafe("My turn. The site is up and answers nothing at all. Our container listens on port 8080. Why?"),
            nova("Because App Service knocks on port 80 unless you tell it otherwise, and a port is just a numbered door. One app setting fixes it: the websites port setting, set to 8080."),
            rafe("And an app setting is?"),
            nova("A name and a value you type into App Service, handed to the container as an ordinary environment variable when it starts. Change one and the app restarts."),
            rafe("Flawless. Your turn to be dangerous."),
            nova("Then here it is. Where does the payment provider key live, and how does the container ever see it?"),
            rafe("In an app setting, surely, as long as only we can read the configuration."),
            nova("No kiss for that one. It lives in Key Vault, Azure's safe for secrets, and the setting is a Key Vault reference: not the key, a note saying fetch this secret from that safe."),
            rafe("And the badge opens the safe."),
            nova("With the Key Vault Secrets User role. Without it nothing resolves, and the app uses the note itself as the key. Look for the green tick in the portal."),
            rafe("Noted, and humbled. Next: our container takes four minutes to start, because it checks Azure SQL on the way up, and App Service kills it. Why, and what do I change?"),
            nova("It waits about 230 seconds, then reports that the container did not respond to pings. You raise the container start time limit, as far as 1800 seconds, and then you go and make the start faster."),
            rafe("Both halves again. [kiss]"),
            nova("My turn. One instance of three has been failing all evening and customers keep landing on it. What have we not set?"),
            rafe("A health check path. Slash healthz, and App Service pings it on every instance; one that keeps failing is taken out of rotation and eventually replaced."),
            nova("And the catch?"),
            rafe("It only helps with two instances or more. With one, there is nowhere else to send anybody."),
            nova("That is the catch exactly. [kiss]"),
            rafe("Now the part I have been waiting for. Release night, and nobody waits for a cold start."),
            nova("A deployment slot, darling: a second live copy of the app, called staging, with its own web address. New image there, warm it up, then swap, and the slots trade places."),
            rafe("Warm it up how?"),
            nova("App Service puts production's settings onto staging first and waits until it answers, then switches the routing. Which brings my question: what stops staging's test database following the swap into production?"),
            rafe("A slot setting. Tick deployment slot setting on the value and it stays with its slot."),
            nova("Right answer. [kiss]"),
            rafe("And slots on a cheap plan?"),
            nova("Standard tier or higher; Basic has none. Now you: we push a new image to the registry and production does not change. What did we forget?"),
            rafe("Continuous deployment. App Service hands the registry a webhook, a short message the registry sends on every push to the watched tag, and the app pulls and restarts."),
            nova("Exactly right. [kiss] And where do we sit and watch the container talk to us?"),
            rafe("Container logging to the file system with az webapp log config, then az webapp log tail, live."),
            nova("Six out of seven. [kiss] And yet we are not staying here."),
            rafe("Why not? It works, and the flat is furnished."),
            nova("Because Zaiqa is three containers, and the order worker has no web address at all; it only reads the orders queue. App Service is shaped around one web app with small helpers beside it."),
            rafe("And the plan is a rented machine, always on."),
            nova("Scaling on processor use or a timetable. Container Apps, next date, scales on requests in flight and on the depth of that queue, and goes to zero when nobody is ordering. Each release is a revision, a numbered version that can take a slice of traffic."),
            rafe("Then before the candles go out: what do we remember for the exam?"),
            nova("Container never answers and it listens on 8080: the websites port setting. A key that must stay in the safe: a Key Vault reference. Staging keeps its own database across a swap: a slot setting."),
            rafe("And the two you quizzed me on first?"),
            nova("A pull with no password: a managed identity holding AcrPull, with that credentials switch on. A container killed while starting: the container start time limit, up to 1800 seconds."),
            rafe("And what shall we try today, before the wine runs out?"),
            nova("Create a Linux web app from a public sample image. Add an app setting and find it printed in the container's log. Set the websites port setting to match the image and browse to the site. Then turn on container logging and watch az webapp log tail while it restarts."),
            rafe("One door number, one safe, one slot. Happy date night, love."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun pull() = DiagramSpec(
        "App Service pulls the custom container from the registry with its managed identity, then starts it.",
        nodes = listOf(
            DNode("acr", "Registry", 0.2f, 0.18f, w = 0.36f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 2, sub = "zaiqa-api image"),
            DNode("mi", "Managed identity", 0.74f, 0.18f, w = 0.46f, h = 0.2f, tone = 1, sub = "holds AcrPull"),
            DNode("app", "App Service", 0.5f, 0.55f, w = 0.5f, h = 0.2f, tone = 1, sub = "pulls and starts it"),
            DNode("cont", "Your container", 0.5f, 0.86f, w = 0.5f, h = 0.18f, sub = "a custom container"),
        ),
        edges = listOf(
            DEdge("acr", "app", "pull"),
            DEdge("mi", "app", "used for pulls"),
            DEdge("app", "cont", "starts"),
        ),
        aspect = 0.8f,
    )

    private fun settings() = DiagramSpec(
        "App settings arrive in the container as environment variables; WEBSITES_PORT says which door traffic knocks on.",
        nodes = listOf(
            DNode("app", "App settings", 0.25f, 0.15f, w = 0.42f, h = 0.2f, tone = 1, sub = "names and values"),
            DNode("port", "WEBSITES_PORT", 0.75f, 0.15f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "8080, not 80"),
            DNode("env", "Environment variables", 0.5f, 0.5f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "read at start-up"),
            DNode("cont", "Container", 0.25f, 0.85f, w = 0.42f, h = 0.2f, sub = "listens on 8080"),
            DNode("web", "Web traffic", 0.75f, 0.85f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "door 80 by default"),
        ),
        edges = listOf(
            DEdge("app", "env", "at start"),
            DEdge("env", "cont"),
            DEdge("port", "web", "which door"),
            DEdge("web", "cont", "knocks"),
        ),
        aspect = 0.8f,
    )

    private fun secrets() = DiagramSpec(
        "A Key Vault reference is resolved at start-up with the app's identity, which needs Key Vault Secrets User.",
        nodes = listOf(
            DNode("kv", "Key Vault", 0.2f, 0.2f, w = 0.34f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 2, sub = "the secret"),
            DNode("ref", "Key Vault reference", 0.7f, 0.2f, w = 0.5f, h = 0.2f, kind = NodeKind.NOTE, sub = "fetch this from the safe"),
            DNode("mi", "Managed identity", 0.25f, 0.58f, w = 0.44f, h = 0.2f, tone = 1, sub = "Secrets User role"),
            DNode("app", "App Service", 0.75f, 0.58f, w = 0.42f, h = 0.2f, tone = 1, sub = "resolves at start"),
            DNode("env", "Environment variable", 0.5f, 0.88f, w = 0.56f, h = 0.18f, kind = NodeKind.PILL, sub = "the real value"),
        ),
        edges = listOf(
            DEdge("ref", "app", "the setting"),
            DEdge("app", "mi", "uses"),
            DEdge("mi", "kv", "reads"),
            DEdge("app", "env"),
        ),
        aspect = 0.8f,
    )

    private fun slots() = DiagramSpec(
        "Deploy to staging, then swap: the slots trade places; normal settings travel, slot settings stay put.",
        nodes = listOf(
            DNode("prod", "Production slot", 0.25f, 0.18f, w = 0.44f, h = 0.22f, tone = 1, sub = "the main stage"),
            DNode("stage", "Staging slot", 0.75f, 0.18f, w = 0.44f, h = 0.22f, sub = "its own address"),
            DNode("swap", "Swap", 0.5f, 0.52f, w = 0.36f, h = 0.18f, kind = NodeKind.PILL, sub = "no downtime"),
            DNode("slotset", "Slot setting", 0.25f, 0.85f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "stays: the database"),
            DNode("normal", "Normal setting", 0.75f, 0.85f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "moves with the swap"),
        ),
        edges = listOf(
            DEdge("stage", "swap", "new image"),
            DEdge("swap", "prod", "trade places"),
            DEdge("slotset", "swap", "stays put", dashed = true),
            DEdge("normal", "swap", "travels"),
        ),
        aspect = 0.8f,
    )

    private fun cd() = DiagramSpec(
        "Continuous deployment: the registry's webhook rings on every push and the app pulls; log tail shows the output.",
        nodes = listOf(
            DNode("acr", "Registry", 0.22f, 0.2f, w = 0.38f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 2, sub = "new image pushed"),
            DNode("hook", "Webhook", 0.75f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "rings on every push"),
            DNode("app", "App Service", 0.75f, 0.62f, w = 0.42f, h = 0.2f, tone = 1, sub = "pulls the new image"),
            DNode("logs", "Log stream", 0.25f, 0.62f, w = 0.42f, h = 0.2f, sub = "az webapp log tail"),
        ),
        edges = listOf(
            DEdge("acr", "hook", "push"),
            DEdge("hook", "app", "calls"),
            DEdge("app", "logs", "output"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where it stops: slots need Standard, the plan is always on, and Zaiqa's worker has no web address at all.",
        nodes = listOf(
            DNode("worker", "The worker has no site", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "it reads the queue"),
            DNode("always", "The plan is always on", 0.76f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "no scale to zero"),
            DNode("plan", "Slots: Standard or higher", 0.26f, 0.76f, w = 0.46f, h = 0.2f, tone = 2, sub = "Basic has none"),
            DNode("next", "Next episode", 0.76f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = "Container Apps"),
        ),
        edges = listOf(DEdge("plan", "next", "next stop"), DEdge("worker", "next", "fits there")),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's App Service container questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("port", "Times out on 8080", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "WEBSITES_PORT=8080"),
            DNode("secret", "Key out of the code", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Key Vault reference"),
            DNode("slot", "Staging keeps its DB", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "slot setting"),
            DNode("start", "Killed at start-up", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "start time limit"),
            DNode("pull", "Pull, no password", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "identity with AcrPull"),
            DNode("cd", "Push, prod unchanged", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "the registry webhook"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("port", "secret", "slot", "start", "pull", "cd")
    private val today = BoardCard("Today", "A Linux web app, a setting in the log, WEBSITES_PORT, then az webapp log tail.")
    private val useIdentity = BoardCard("acrUseManagedIdentityCreds", "With AcrPull on the badge, this switch says: use it for pulls.")
    private val tick = BoardCard("Key Vault Secrets User", "The badge needs this role on the vault. Look for the green tick.")
    private val logTail = BoardCard("az webapp log tail", "Container logging on: the container's output streams in live.")
    private val startLimit = BoardCard("230 seconds, then killed", "Raise the container start time limit, as far as 1800 seconds.")
    private val health = BoardCard("Health check path", "Slash healthz, pinged on every instance. Two instances or more.")
    private val why = BoardCard("Why we move on", "Three containers, one with no web address, and a plan always on.")

    private fun classicBoard() = PodcastBoard(
        "ai-appservice-containers",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is the food delivery app we build all cou..."
            Scene(
                0, "Moving in", pull(),
                beats = listOf(
                    Beat(0, card = BoardCard("Where it runs", "Last time: the image on the shelf. Today: somewhere to run it.")),
                    Beat(2, show = setOf("app"), light = setOf("app"), card = BoardCard("App Service", "Azure's managed web hosting: you bring the app, Azure runs the building.")),
                    Beat(4, show = setOf("cont"), light = setOf("cont"), pulse = "app" to "cont", card = BoardCard("Custom container", "Your own image, run instead of the ready-made dot NET setup.")),
                    Beat(6, show = setOf("acr", "mi"), light = setOf("mi"), pulse = "mi" to "app", card = BoardCard("Managed identity", "A badge Azure gives the app. AcrPull: download images, nothing else.")),
                    Beat(8, light = setOf("acr", "mi"), pulse = "acr" to "app", card = useIdentity),
                ),
            ),
            // 9: "Fine. It pulled, it started. How does my code learn the Azure SQL con..."
            Scene(
                9, "Settings become variables", settings(),
                beats = listOf(
                    Beat(10, show = setOf("app", "env"), light = setOf("app", "env"), pulse = "app" to "env", card = BoardCard("App setting", "A name and a value, handed over as an environment variable at start.")),
                    Beat(12, show = setOf("cont"), light = setOf("env", "cont"), pulse = "env" to "cont", card = BoardCard("A change restarts", "Add, edit or remove one, and every instance's container starts again.")),
                    Beat(14, card = BoardCard("Connection strings", "Arrive with a prefix glued to the name, one per type. Dot NET copes.")),
                    Beat(16, card = BoardCard("Why app settings win", "Other stacks miss the prefixed name, so most teams use plain settings.")),
                ),
            ),
            // 17: "And the payment provider key? I am not typing that into the portal."
            Scene(
                17, "Secrets from the safe", secrets(),
                beats = listOf(
                    Beat(18, show = setOf("kv", "ref"), light = setOf("kv", "ref"), card = BoardCard("Key Vault reference", "Not the key: a note saying fetch this secret from the safe.")),
                    Beat(20, show = setOf("app", "mi", "env"), light = setOf("mi"), pulse = "mi" to "kv", card = tick),
                ),
            ),
            // 21: "Right. We browse to the site, and it just hangs."
            Scene(
                21, "The door number", settings(),
                beats = listOf(
                    Beat(22, show = setOf("cont", "web"), light = setOf("web", "cont"), pulse = "web" to "cont", card = BoardCard("Door 80, door 8080", "App Service knocks on 80. Our container waits behind 8080.")),
                    Beat(24, show = setOf("port"), light = setOf("port"), pulse = "port" to "web", card = BoardCard("WEBSITES_PORT", "One app setting, set to 8080, and traffic reaches the container.")),
                ),
            ),
            // 25: "What else bites on the first deploy?"
            Scene(
                25, "Start-up and health", pull(),
                beats = listOf(
                    Beat(26, show = setOf("app", "cont"), light = setOf("cont"), pulse = "app" to "cont", card = startLimit),
                    Beat(28, light = setOf("cont"), card = BoardCard("Did not respond to pings", "Killed on the way up. Raise the limit, then make the start faster.")),
                    Beat(30, light = setOf("app"), card = health),
                    Beat(32, card = BoardCard("Two instances or more", "With one instance there is nowhere else to send the traffic.")),
                ),
            ),
            // 33: "Releases next. How do I put a new build live without a scary minute?"
            Scene(
                33, "Slots and swaps", slots(),
                beats = listOf(
                    Beat(34, show = setOf("prod", "stage"), light = setOf("stage"), card = BoardCard("Deployment slot", "A second live copy with its own address. Standard tier or higher.")),
                    Beat(36, show = setOf("swap"), light = setOf("swap"), pulse = "stage" to "swap", card = BoardCard("Swap", "Production's settings go on staging, it warms up, then routing switches.")),
                    Beat(38, show = setOf("slotset", "normal"), light = setOf("slotset"), pulse = "slotset" to "swap", card = BoardCard("Slot setting", "Stays with its slot through a swap: staging keeps its test database.")),
                    Beat(40, light = setOf("prod"), card = BoardCard("Basic has no slots", "Slots need Standard tier or higher. A favourite exam detail.")),
                ),
            ),
            // 41: "Now, I push a new image to the registry. Does App Service notice?"
            Scene(
                41, "New images, live", cd(),
                beats = listOf(
                    Beat(42, show = setOf("acr", "hook", "app"), light = setOf("hook"), pulse = "acr" to "hook", card = BoardCard("Continuous deployment", "The registry's webhook rings on every push, and the app pulls.")),
                    Beat(44, show = setOf("logs"), light = setOf("logs"), pulse = "app" to "logs", card = logTail),
                ),
            ),
            // 45: "So the backend runs. Why does the course not stop here?"
            Scene(
                45, "Why we move on", limits(),
                beats = listOf(
                    Beat(46, show = setOf("worker"), light = setOf("worker"), card = BoardCard("Three containers", "The taste service, and an order worker with no web address at all.")),
                    Beat(48, show = setOf("always"), light = setOf("always"), card = BoardCard("The plan is always on", "A rented machine, scaling on processor use or a timetable.")),
                    Beat(50, show = setOf("plan", "next"), light = setOf("next"), pulse = "worker" to "next", card = why),
                ),
            ),
            // 51: "So what do I remember for the exam?"
            Scene(
                51, "For the exam", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("port", "secret", "slot"), card = BoardCard("Listen for the phrase", "Port 8080, a Key Vault reference, a slot setting: one answer each.")),
                    Beat(54, light = setOf("pull", "start"), card = BoardCard("And the quiet two", "The identity with AcrPull, and the container start time limit.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-appservice-containers--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Orders That Went To The Wron..."
            Scene(
                0, "The case", slots(),
                beats = listOf(
                    Beat(0, show = setOf("prod"), light = setOf("prod"), card = BoardCard("The case", "Four hundred real orders landed in the test database on Friday.")),
                    Beat(2, card = BoardCard("App Service", "Azure's managed web hosting, running our own image from the registry.")),
                    Beat(4, card = BoardCard("Custom container", "Your own image, pulled and started instead of a ready-made stack.")),
                    Beat(6, show = setOf("stage"), light = setOf("stage"), card = BoardCard("Deployment slot", "A second live copy called staging, with its own web address.")),
                    Beat(8, show = setOf("swap"), light = setOf("swap"), pulse = "stage" to "swap", card = BoardCard("Swap", "Production's settings go on staging, it warms up, then routing switches.")),
                    Beat(10, show = setOf("slotset", "normal"), light = setOf("slotset"), pulse = "slotset" to "swap", card = BoardCard("Slot setting", "Stays with its slot through a swap. The database was not marked.")),
                    Beat(12, light = setOf("normal"), pulse = "normal" to "swap", card = BoardCard("What travelled", "The unmarked database setting rode the swap into production.")),
                ),
            ),
            // 14: "Not yet. There is a second file on this desk from the same release..."
            Scene(
                14, "The silent site", settings(),
                beats = listOf(
                    Beat(16, show = setOf("cont", "web"), light = setOf("web", "cont"), pulse = "web" to "cont", card = BoardCard("Door 80, door 8080", "App Service knocks on 80; the container waits behind 8080.")),
                    Beat(18, show = setOf("app", "port", "env"), light = setOf("port"), pulse = "port" to "web", card = BoardCard("WEBSITES_PORT", "One app setting, handed over as an environment variable at start.")),
                ),
            ),
            // 19: "And they had been staring at the registry instead."
            Scene(
                19, "Suspect three: the pull", pull(),
                beats = listOf(
                    Beat(20, show = setOf("acr", "mi", "app"), light = setOf("mi"), pulse = "mi" to "app", card = BoardCard("Managed identity", "A badge holding AcrPull: download images, nothing else.")),
                    Beat(22, light = setOf("acr"), pulse = "acr" to "app", card = useIdentity),
                    Beat(24, show = setOf("cont"), light = setOf("cont"), pulse = "app" to "cont", card = startLimit),
                    Beat(26, light = setOf("cont"), card = BoardCard("Did not respond to pings", "Killed twice while it checked its Azure SQL connection.")),
                ),
            ),
            // 27: "Next suspect. The payment key was sitting in a settings box, I bet."
            Scene(
                27, "The key in the safe", secrets(),
                beats = listOf(
                    Beat(28, show = setOf("kv", "ref", "app"), light = setOf("ref"), pulse = "ref" to "app", card = BoardCard("Key Vault reference", "Not the key: a note saying fetch this secret from the safe.")),
                    Beat(30, show = setOf("mi", "env"), light = setOf("mi"), pulse = "mi" to "kv", card = tick),
                ),
            ),
            // 31: "So why did only some customers see broken orders?"
            Scene(
                31, "The failing instance", pull(),
                beats = listOf(
                    Beat(32, show = setOf("app", "cont"), light = setOf("cont"), card = BoardCard("One instance of three", "Restarting all evening, and customers kept landing on it.")),
                    Beat(34, light = setOf("app"), pulse = "app" to "cont", card = health),
                ),
            ),
            // 36: "Yes. And one thing the team did get right: continuous deployment."
            Scene(
                36, "What they got right", cd(),
                beats = listOf(
                    Beat(36, show = setOf("acr", "hook", "app"), light = setOf("hook"), pulse = "acr" to "hook", card = BoardCard("Continuous deployment", "The registry's webhook rings on every push, and the app pulls.")),
                    Beat(38, show = setOf("logs"), light = setOf("logs"), pulse = "app" to "logs", card = logTail),
                ),
            ),
            // 39: "Case closed, then?"
            Scene(
                39, "A note for the file", limits(),
                beats = listOf(
                    Beat(40, show = setOf("worker"), light = setOf("worker"), card = BoardCard("Three containers", "The order worker has no web address: it reads the orders queue.")),
                    Beat(42, show = setOf("always", "next"), light = setOf("always"), pulse = "worker" to "next", card = why),
                    Beat(44, show = setOf("plan"), light = setOf("plan"), card = BoardCard("Basic has no slots", "Slots need Standard tier or higher. The exam asks that one.")),
                ),
            ),
            // 45: "So the case summary for the exam?"
            Scene(
                45, "Case closed", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("slot", "port", "secret"), card = BoardCard("Listen for the phrase", "A slot setting, the port setting, a Key Vault reference.")),
                    Beat(48, light = setOf("start", "pull"), card = BoardCard("And the quiet two", "The start time limit, and the identity holding AcrPull.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-appservice-containers--gossip",
        listOf(
            // 0: "Sit down. Did you hear what happened on the Zaiqa team this week?"
            Scene(
                0, "Monday: Tom", pull(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("The week", "The Zaiqa backend tried to live on App Service. Five days, five lessons.")),
                    Beat(2, show = setOf("cont"), light = setOf("cont"), pulse = "app" to "cont", card = BoardCard("Custom container", "Your own image instead of the ready-made dot NET setup.")),
                    Beat(4, show = setOf("acr", "mi"), light = setOf("mi"), pulse = "mi" to "app", card = BoardCard("Managed identity", "A badge holding AcrPull: download images, nothing else.")),
                    Beat(6, light = setOf("acr"), pulse = "acr" to "app", card = useIdentity),
                ),
            ),
            // 7: "A port being a numbered door, and nobody behind that one."
            Scene(
                7, "The wrong door", settings(),
                beats = listOf(
                    Beat(8, show = setOf("app", "port", "env", "cont", "web"), light = setOf("port", "web"), pulse = "port" to "web", card = BoardCard("WEBSITES_PORT", "App Service knocks on 80; set it to 8080 and the site answers.")),
                ),
            ),
            // 9: "Tuesday. Who?"
            Scene(
                9, "Tuesday: Dana", secrets(),
                beats = listOf(
                    Beat(10, show = setOf("app", "env"), light = setOf("env"), pulse = "app" to "env", card = BoardCard("The key in a setting", "Anyone who can read the configuration can read the key.")),
                    Beat(12, show = setOf("kv", "ref"), light = setOf("ref"), pulse = "ref" to "app", card = BoardCard("Key Vault reference", "Not the key: a note saying fetch this secret from the safe.")),
                    Beat(14, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "kv", card = tick),
                ),
            ),
            // 15: "Wednesday."
            Scene(
                15, "Wednesday: Marcus", pull(),
                beats = listOf(
                    Beat(16, show = setOf("app", "cont"), light = setOf("cont"), pulse = "app" to "cont", card = startLimit),
                    Beat(18, light = setOf("cont"), card = BoardCard("Did not respond to pings", "Over and over, while it checked its Azure SQL connection.")),
                ),
            ),
            // 19: "Thursday, and I sense something expensive."
            Scene(
                19, "Thursday: Priya", slots(),
                beats = listOf(
                    Beat(20, show = setOf("prod", "stage", "swap"), light = setOf("stage"), pulse = "stage" to "swap", card = BoardCard("Deployment slot", "Staging, with its own address. Warm it up, then swap.")),
                    Beat(22, light = setOf("swap"), pulse = "swap" to "prod", card = BoardCard("Swap", "Production's settings go on staging, it warms up, then routing switches.")),
                    Beat(24, show = setOf("slotset", "normal"), light = setOf("slotset"), pulse = "slotset" to "swap", card = BoardCard("Slot setting", "Tick it, and the value stays with its slot through a swap.")),
                ),
            ),
            // 25: "And Friday? Please tell me it was the intern."
            Scene(
                25, "Friday: the intern", cd(),
                beats = listOf(
                    Beat(26, show = setOf("acr", "app"), light = setOf("acr"), card = BoardCard("A push, and nothing", "App Service pulls when told to, or when continuous deployment is on.")),
                    Beat(28, show = setOf("hook"), light = setOf("hook"), pulse = "acr" to "hook", card = BoardCard("Webhook", "A short message the registry sends on every push to the watched tag.")),
                    Beat(30, show = setOf("logs"), light = setOf("logs"), pulse = "app" to "logs", card = logTail),
                ),
            ),
            // 31: "Did anything break for customers?"
            Scene(
                31, "The failing instance", pull(),
                beats = listOf(
                    Beat(32, show = setOf("app", "cont"), light = setOf("cont"), card = BoardCard("One instance of three", "Restarting all evening, and a third of the orders failed.")),
                    Beat(34, light = setOf("app"), pulse = "app" to "cont", card = health),
                ),
            ),
            // 35: "So after all that, is Zaiqa staying on App Service?"
            Scene(
                35, "Why they move on", limits(),
                beats = listOf(
                    Beat(36, show = setOf("worker"), light = setOf("worker"), card = BoardCard("Three containers", "The order worker has no web address: it reads the orders queue.")),
                    Beat(38, show = setOf("always"), light = setOf("always"), card = BoardCard("The plan is always on", "A rented machine, scaling on processor use or a timetable.")),
                    Beat(40, show = setOf("next"), light = setOf("next"), pulse = "worker" to "next", card = why),
                    Beat(42, show = setOf("plan"), light = setOf("plan"), card = BoardCard("Basic has no slots", "Slots need Standard tier or higher. The exam enjoys asking.")),
                ),
            ),
            // 43: "So what do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("port", "secret", "slot"), card = BoardCard("Listen for the phrase", "The port setting, a Key Vault reference, a slot setting.")),
                    Beat(46, light = setOf("cd", "start", "pull"), card = BoardCard("And the rest of the week", "The registry webhook, the start time limit, the identity with AcrPull.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-appservice-containers--late-night",
        listOf(
            // 0: "It is late, you are still up, and so are we. Tonight..."
            Scene(
                0, "Tonight", pull(),
                beats = listOf(
                    Beat(0, show = setOf("acr"), light = setOf("acr"), card = BoardCard("Tonight", "The image leaves the registry and goes looking for a home.")),
                    Beat(2, show = setOf("app"), light = setOf("app"), pulse = "acr" to "app", card = BoardCard("App Service", "Managed web hosting: the machines, the address, the certificate.")),
                    Beat(4, show = setOf("cont"), light = setOf("cont"), pulse = "app" to "cont", card = BoardCard("Custom container", "Your own image instead of a ready-made dot NET setup.")),
                    Beat(6, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "app", card = BoardCard("Managed identity", "A badge holding AcrPull: download images, nothing else.")),
                    Beat(8, light = setOf("mi", "acr"), card = useIdentity),
                ),
            ),
            // 9: "So it pulled. Did it answer?"
            Scene(
                9, "The wrong door", settings(),
                beats = listOf(
                    Beat(10, show = setOf("cont", "web"), light = setOf("web", "cont"), pulse = "web" to "cont", card = BoardCard("Door 80, door 8080", "Knocking politely on the wrong door all evening.")),
                    Beat(12, show = setOf("app", "port", "env"), light = setOf("port"), pulse = "port" to "web", card = BoardCard("WEBSITES_PORT", "One app setting, 8080, and the knocking finds someone in.")),
                    Beat(14, light = setOf("env"), pulse = "app" to "env", card = BoardCard("A change restarts", "Add or edit one, and every instance's container starts again.")),
                    Beat(16, card = BoardCard("Connection strings", "Arrive with a prefix glued to the name. Dot NET copes; others do not.")),
                ),
            ),
            // 17: "And the payment key? Say something comforting."
            Scene(
                17, "The safe", secrets(),
                beats = listOf(
                    Beat(18, show = setOf("kv", "ref", "app", "mi"), light = setOf("ref"), pulse = "ref" to "app", card = BoardCard("Key Vault reference", "A note saying fetch this secret from the safe, not the secret.")),
                    Beat(20, light = setOf("mi"), pulse = "mi" to "kv", card = tick),
                ),
            ),
            // 21: "The container came up, then. Slowly?"
            Scene(
                21, "Slow starts", pull(),
                beats = listOf(
                    Beat(22, show = setOf("app", "cont"), light = setOf("cont"), pulse = "app" to "cont", card = startLimit),
                    Beat(24, light = setOf("cont"), card = BoardCard("Did not respond to pings", "Twice it never came up: it was checking its database on the way.")),
                    Beat(26, light = setOf("app"), card = health),
                ),
            ),
            // 27: "Now the part I like. Shipping at night."
            Scene(
                27, "Shipping at night", slots(),
                beats = listOf(
                    Beat(28, show = setOf("prod", "stage", "swap"), light = setOf("stage"), pulse = "stage" to "swap", card = BoardCard("Deployment slot", "A second live copy called staging, with its own web address.")),
                    Beat(30, light = setOf("swap"), pulse = "swap" to "prod", card = BoardCard("Swap", "Production's settings go on staging, it warms up, then routing switches.")),
                    Beat(32, show = setOf("slotset", "normal"), light = setOf("slotset"), pulse = "slotset" to "swap", card = BoardCard("Slot setting", "Pinned to its slot: staging keeps the test database.")),
                    Beat(34, light = setOf("prod"), card = BoardCard("Basic has no slots", "Slots need Standard tier or higher, which the exam remembers.")),
                ),
            ),
            // 35: "Someone pushes a new image while we sleep. Does anything happen?"
            Scene(
                35, "While you sleep", cd(),
                beats = listOf(
                    Beat(36, show = setOf("acr", "hook", "app"), light = setOf("hook"), pulse = "acr" to "hook", card = BoardCard("Continuous deployment", "The registry's webhook rings on every push, and the app pulls.")),
                    Beat(38, show = setOf("logs"), light = setOf("logs"), pulse = "app" to "logs", card = logTail),
                ),
            ),
            // 39: "So Zaiqa lives happily on App Service forever."
            Scene(
                39, "Where it stops", limits(),
                beats = listOf(
                    Beat(40, show = setOf("worker"), light = setOf("worker"), card = BoardCard("Three containers", "The order worker has no web address: it reads the orders queue.")),
                    Beat(42, show = setOf("always", "next", "plan"), light = setOf("always", "next"), pulse = "worker" to "next", card = why),
                ),
            ),
            // 43: "Before I fall asleep on you. What do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("port", "secret", "slot"), card = BoardCard("Listen for the phrase", "The port setting, a Key Vault reference, a slot setting.")),
                    Beat(46, light = setOf("pull", "start"), card = BoardCard("Two more, then bed", "The identity with AcrPull, and the container start time limit.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-appservice-containers--couples",
        listOf(
            // 0: "Tonight is date night, darling, and I have brought work to the table."
            Scene(
                0, "Date night", pull(),
                beats = listOf(
                    Beat(0, show = setOf("acr"), light = setOf("acr"), card = BoardCard("Date night", "The backend image is finally leaving the registry.")),
                    Beat(2, show = setOf("app"), light = setOf("app"), pulse = "acr" to "app", card = BoardCard("App Service", "Managed web hosting: a furnished flat, and you bring the app.")),
                    Beat(4, show = setOf("cont"), light = setOf("cont"), pulse = "app" to "cont", card = BoardCard("Quiz: getting in", "How does App Service reach a private registry with no password?")),
                    Beat(5, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "app", card = BoardCard("Right answer", "A managed identity holding AcrPull: images, and nothing else.")),
                    Beat(7, light = setOf("mi"), card = useIdentity),
                ),
            ),
            // 9: "My turn. The site is up and answers nothing at all."
            Scene(
                9, "The door that answers", settings(),
                beats = listOf(
                    Beat(9, show = setOf("cont", "web"), light = setOf("web"), card = BoardCard("Quiz: nothing answers", "The container listens on 8080 and the site says nothing. Why?")),
                    Beat(10, show = setOf("port"), light = setOf("port"), pulse = "port" to "web", card = BoardCard("Right answer", "App Service knocks on 80 until the port setting says 8080.")),
                    Beat(12, show = setOf("app", "env"), light = setOf("env"), pulse = "app" to "env", card = BoardCard("App setting", "A name and a value, handed over as an environment variable.")),
                ),
            ),
            // 14: "Then here it is. Where does the payment provider key live?"
            Scene(
                14, "The dangerous one", secrets(),
                beats = listOf(
                    Beat(14, show = setOf("app", "env"), light = setOf("app"), card = BoardCard("Quiz: the payment key", "Where does it live, and how does the container ever see it?")),
                    Beat(16, show = setOf("kv", "ref"), light = setOf("ref"), pulse = "ref" to "app", card = BoardCard("No kiss: Key Vault", "A Key Vault reference: a note saying fetch this from the safe.")),
                    Beat(18, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "kv", card = tick),
                ),
            ),
            // 19: "Noted, and humbled. Next: our container takes four minutes to start."
            Scene(
                19, "Slow to start", pull(),
                beats = listOf(
                    Beat(19, show = setOf("app", "cont"), light = setOf("cont"), card = BoardCard("Quiz: four minutes", "App Service kills the container on the way up. Why, and what next?")),
                    Beat(20, light = setOf("app"), pulse = "app" to "cont", card = startLimit),
                    Beat(22, light = setOf("app", "cont"), card = BoardCard("Right answer", "It waits about 230 seconds, then reports it never answered.")),
                    Beat(23, card = BoardCard("Quiz: one bad instance", "Customers keep landing on it. What have we not set?")),
                    Beat(25, card = health),
                ),
            ),
            // 27: "Now the part I have been waiting for. Release night."
            Scene(
                27, "Release night", slots(),
                beats = listOf(
                    Beat(28, show = setOf("prod", "stage", "swap"), light = setOf("stage"), pulse = "stage" to "swap", card = BoardCard("Deployment slot", "Staging, with its own address. Warm it up, then swap.")),
                    Beat(30, show = setOf("normal"), light = setOf("swap"), pulse = "normal" to "swap", card = BoardCard("Quiz: the test database", "What stops it following the swap into production?")),
                    Beat(31, show = setOf("slotset"), light = setOf("slotset"), pulse = "slotset" to "swap", card = BoardCard("Right answer", "A slot setting stays with its slot through the swap.")),
                ),
            ),
            // 34: "Standard tier or higher; Basic has none. Now you: we push a new image."
            Scene(
                34, "The push that did nothing", cd(),
                beats = listOf(
                    Beat(34, show = setOf("acr", "app"), light = setOf("acr"), card = BoardCard("Quiz: nothing changed", "A new image is pushed and production carries on. What is missing?")),
                    Beat(35, show = setOf("hook"), light = setOf("hook"), pulse = "acr" to "hook", card = BoardCard("Right answer", "Continuous deployment: the registry's webhook rings on each push.")),
                    Beat(37, show = setOf("logs"), light = setOf("logs"), pulse = "app" to "logs", card = logTail),
                ),
            ),
            // 38: "Six out of seven. And yet we are not staying here."
            Scene(
                38, "Why we move on", limits(),
                beats = listOf(
                    Beat(40, show = setOf("worker"), light = setOf("worker"), card = BoardCard("Three containers", "The order worker has no web address: it reads the orders queue.")),
                    Beat(42, show = setOf("always", "next", "plan"), light = setOf("always", "next"), pulse = "worker" to "next", card = why),
                ),
            ),
            // 43: "Then before the candles go out: what do we remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("port", "secret", "slot"), card = BoardCard("Listen for the phrase", "The port setting, a Key Vault reference, a slot setting.")),
                    Beat(46, light = setOf("pull", "start"), card = BoardCard("And the two from before", "The identity with AcrPull, and the container start time limit.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
