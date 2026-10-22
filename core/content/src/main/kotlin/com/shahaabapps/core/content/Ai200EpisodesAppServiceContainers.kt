package com.shahaabapps.core.content

/** Containers on App Service: settings, secrets and slots: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesAppServiceContainers {
    private val classic = Podcast(
        "ai-appservice-containers",
        listOf(
            maya("Hi, and welcome back. Today, your own container, running on App Service. First, App Service itself: Azure's web hosting. You bring the app, and Azure runs the building: the machines, the updates, the web address. Think of it like renting a furnished flat instead of building a house."),
            leo("A furnished flat. And I just move my app in?"),
            maya("You can move in code, and App Service picks a ready-made Python setup for it. Or you move in your own container image, a packed-up copy of your app and everything it needs, like a lunchbox. That's called a custom container."),
            leo("Remind me what's in the lunchbox. Is the operating system in there?"),
            maya("The slim files of one, called the base image, like a cut-down Debian, plus your code, Python and libraries. Not the kernel, the core that drives the hardware. Your container shares the kernel of the App Service machine it lands on. So a Linux image needs a Linux plan."),
            leo("Got it. And where does App Service find my lunchbox?"),
            maya("In a container registry, a private warehouse for images, such as Azure Container Registry. The registry episode covers it. Say you're building the Two Tracks Coach, a study assistant in an Android app. Its Python backend, the part the phone calls, is the image coach-api, in a registry called ttcoach."),
            leo("And App Service fetches it from there?"),
            maya("Right. Your build pushed it in. App Service downloads it, which is a pull, starts it, and sends web traffic to it."),
            leo("Does App Service need a password to get into the warehouse?"),
            maya("It shouldn't. Give the app a managed identity. That's a badge Azure gives your app, so it can prove who it is with no password stored anywhere."),
            leo("And the badge opens the warehouse door?"),
            maya("Once you give it a role, a named set of permissions. The role for pulling is AcrPull: download images, nothing more."),
            leo("And App Service knows to use the badge?"),
            maya("Only after you flip one switch on the app. Its name is acrUseManagedIdentityCreds, which just means, use the identity's credentials, its proof of who it is, for the registry."),
            leo("[chuckle] Rolls off the tongue. Okay, it's pulled and started. How does my app get its settings, like where the database is?"),
            maya("Through app settings. An app setting is a name and a value you type into App Service, like one named COSMOS ENDPOINT, holding the address of the Coach's database. When the container starts, each one arrives inside it as an environment variable."),
            leo("Environment variable. Plain words?"),
            maya("A named value the program can read when it starts, like a note left on the kitchen table. Python reads them with os.environ. So the same image runs anywhere, and only the notes change. One thing to know: changing a setting restarts the app."),
            leo("Good. Now, my Coach backend listens on port 8000. Is that a problem?"),
            maya("It can be. A port is a numbered door on the machine where your app waits for visitors. App Service knocks on door 80 by default. If your app is waiting behind door 8000, nobody answers, and every request times out."),
            leo("[sigh] So the app's running, and nobody can reach it."),
            maya("Exactly. The fix is one more app setting, called websites port, set to 8000. On screen it's written in capitals, with an underscore. It simply tells App Service which door to knock on."),
            leo("Now secrets. The Coach has a key for Azure OpenAI, the AI service it talks to. Can that be an app setting?"),
            maya("Not a plain one. Anyone who can read the settings can read the key. Keep the key in Key Vault, Azure's safe for secrets. Then make the app setting a Key Vault reference: instead of the key, its value says, fetch this from the safe."),
            leo("And who fetches it?"),
            maya("App Service does, when the app starts, using the app's managed identity again. So the badge needs one more role on the vault: Key Vault Secrets User, which lets it read secrets. The container just sees the real key in an environment variable."),
            leo("And if the badge doesn't have that role?"),
            maya("Then the reference can't be resolved, and the app sees the reference text itself instead of the key. The portal, Azure's website where you manage everything, shows a green tick next to each reference that worked."),
            leo("And when I rotate the key? Replace it with a new one?"),
            maya("Leave the version off the reference, and the app picks up the new one."),
            leo("Okay. Now how do I release a new version without breaking things?"),
            maya("With a deployment slot. A slot is a second live copy of your app, with its own web address, such as staging. Like a rehearsal stage right next to the main stage. Slots need the Standard price level or higher."),
            leo("And the rehearsal goes on stage how?"),
            maya("By a swap. You put the new image in staging, and warm it up with a first request so it's awake. Check it works, then swap."),
            leo("And the users notice nothing?"),
            maya("Nothing. The two slots trade places with no downtime, meaning no moment where the site is down. And if something's wrong, swapping back is your undo button."),
            leo("Do the settings swap too?"),
            maya("Most of them do. But some belong to the stage, not the actor. For example, staging should keep its own test database. So you tick deployment slot setting on that one, and it stays with its slot during a swap. That's a slot setting."),
            leo("Nice. Last piece. I push a new image to the registry. Does App Service notice?"),
            maya("Not by itself. It only pulls again on a restart. For it to happen automatically, turn on continuous deployment."),
            leo("And how does the registry tell App Service there's something new?"),
            maya("With a webhook, which is like a doorbell. On every push, the registry rings the app's deployment address, and the app pulls the new image."),
            leo("And when something goes wrong, how do I see what my container says?"),
            maya("Turn on container logging, then run az webapp log tail on Azure's command line, the text window where you type instructions. You watch the container's output live, start-up lines and all, like a ticker."),
            leo("[laugh] A ticker for my bugs. What are the usual mistakes?"),
            maya("Five. Forgetting websites port when the app isn't on port 80. Keeping the OpenAI key as a plain setting. Giving the badge no Key Vault role, so the reference never resolves."),
            leo("That's three. Keep going."),
            maya("Not marking per-stage settings as slot settings. And pushing a new image while expecting App Service to notice by magic."),
            leo("So what do I remember for the exam?"),
            maya("If the container starts but requests time out on port 8000, the answer is websites port set to 8000. If a key must stay out of code, a Key Vault reference. If staging keeps its own database after a swap, a slot setting."),
            leo("And the registry side?"),
            maya("If production ignores a new image, the registry webhook. And pulling with no password means a managed identity with AcrPull."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create a Linux web app from a public sample image. Add an app setting, and print it from the container's log. Set websites port to match the image, and browse to the site. Then turn on container logging, and watch az webapp log tail while it restarts."),
            leo("A flat, a doorbell and a ticker. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-appservice-containers",
        listOf(
            maya("Welcome to Case Files. Today's case: The Plant That Never Answered. A small Android app called PlantPal reminds people to water their plants. Its backend is a Python container running on App Service. Launch day: the container starts, the logs look happy, and every single request from the phones times out."),
            leo("[chuckle] Detective Leo, on duty. Plants dying of thirst. First, plain words: what's App Service?"),
            maya("Azure's web hosting. You bring the app, and Azure runs the building: the machines, the updates, the web address."),
            leo("And under the picture?"),
            maya("A set of rented machines, called a plan, with a front door that takes web traffic and passes it to your app. PlantPal brings its own container image, a custom container."),
            leo("Container image meaning the lunchbox. Packed-up app, everything it needs. Operating system included?"),
            maya("Its slim files, the base image, yes. Its kernel, the core that drives the hardware, no. The container shares the kernel of the App Service machine. That's why PlantPal's Linux image runs on a Linux plan."),
            leo("Noted for the file. And where does the image come from?"),
            maya("It waits in a container registry, a private warehouse. PlantPal's build pushes it in, and App Service pulls it, meaning downloads it, and starts it. The registry episode tells that side of the story."),
            leo("And how does it get into the warehouse without a password?"),
            maya("With a managed identity, a badge Azure gives the app, plus one switch on the app that says, use the badge for pulls. Its name is acrUseManagedIdentityCreds. A mouthful, but it means no registry password is stored anywhere."),
            leo("Then my first suspect: App Service couldn't get into the warehouse. Missing AcrPull."),
            maya("Good instinct, but no. AcrPull is the role, a set of permissions, that lets the app's managed identity download images. Without it, the image would never arrive, and the container wouldn't start at all. This one started. Here's the real clue: PlantPal's backend listens on port 8000."),
            leo("Port. Like a door number?"),
            maya("Exactly. A numbered door on the machine where the app waits for visitors. App Service's front door knocks on door 80 by default. PlantPal waits behind door 8000. Nobody answers, and every request times out."),
            leo("So the fix is to rebuild the app on port 80."),
            maya("No need. Add an app setting, a name and value you type into App Service, called websites port, set to 8000. On screen it's capitals with an underscore. It tells the front door which door to knock on. The plants got their answers by lunchtime."),
            leo("Case closed, then."),
            maya("Not quite. Second witness: a screenshot of the app's settings went round the office chat. In it, in plain sight, was the key for the weather service PlantPal pays for."),
            leo("Ouch. Why were the settings showing a key?"),
            maya("Because the key was a plain app setting. Each app setting arrives inside the container as an environment variable, a named value the program reads when it starts. Python reads it with os.environ. Handy, but anyone who can see the settings sees the key."),
            leo("So where should the key live?"),
            maya("In Key Vault, Azure's safe for secrets. The app setting becomes a Key Vault reference: its value just says, fetch this from the safe. When the app starts, App Service reads the secret and hands the container the real value. The code doesn't change at all."),
            leo("They did that, and now the app sees a weird line of text where the key should be. Suspect: Key Vault is down."),
            maya("[pause] Key Vault is fine. The clue is the text itself. When a reference can't be resolved, the app gets the reference text instead of the secret."),
            leo("And why couldn't it be resolved?"),
            maya("App Service fetches the secret using the app's managed identity, the badge, and that badge had no role on the vault. It needs Key Vault Secrets User."),
            leo("What's actually happening under the badge?"),
            maya("The managed identity is an account in Entra ID, Microsoft's sign-in system. Azure started the app, so it answers the app's requests for short-lived passes on a private address nobody outside can reach. No password exists to leak."),
            leo("And when the key is rotated, replaced with a new one?"),
            maya("Leave the version off the reference, and the new key is picked up."),
            leo("Third case?"),
            maya("Third case. The team now releases through a deployment slot, a second live copy of the app with its own address, called staging. Like a rehearsal stage next to the main stage."),
            leo("And how does the rehearsal reach the main stage?"),
            maya("By a swap: the two slots trade places, with no downtime, meaning no moment where the site is down. But after one swap, production started writing to the test database."),
            leo("Suspect: someone typed the wrong database name."),
            maya("Nobody typed anything. Settings move with the swap, unless they're marked as a deployment slot setting. The database name wasn't marked, so staging's test database walked onto the main stage with the new image. Mark it as a slot setting, and it stays with its slot."),
            leo("And how did they undo the bad swap?"),
            maya("They swapped back. The slots trade places again, and the old image is live in moments. That's the other gift of slots: rolling back is just one more swap."),
            leo("And the last clue in the file?"),
            maya("A fix was pushed to the registry on Friday, and production kept running the old image all weekend. Continuous deployment was switched on, but the registry had no webhook."),
            leo("Webhook. Plain words?"),
            maya("A doorbell. On every push, the registry rings the app's deployment address, and the app pulls the new image. Without it, App Service only pulls on a restart."),
            leo("How did the team see any of this happening?"),
            maya("Container logging, turned on, and az webapp log tail on Azure's command line, the text window for typed instructions. It streams the container's output live. Start-up lines, errors, all of it."),
            leo("And where does all this stop working?"),
            maya("Slots need the Standard price level or higher, so a Basic plan has none. And the phone never holds a secret at all; it calls the backend, which does. If PlantPal later needs to scale to zero, running nothing and costing nothing when nobody's watering, that's the Container Apps episode."),
            leo("So, the case summary for the exam?"),
            maya("If the container starts but requests time out on port 8000, the answer is websites port set to 8000. If a key must stay out of settings and code, a Key Vault reference, with the Secrets User role."),
            leo("And the swap, and the lost weekend?"),
            maya("If staging keeps its own database after a swap, a slot setting. And if production ignores a pushed image, the registry webhook."),
            leo("Case closed. Anything to try today?"),
            maya("Create a Linux web app from a public sample image. Add an app setting, and print it from the container's log. Set websites port to match the image, and browse to the site. Then turn on container logging, and watch az webapp log tail while it restarts."),
            leo("Water the plants, check the port. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-appservice-containers",
        listOf(
            maya("Okay, did you hear what happened when the Coach team moved to App Service this week? The Two Tracks Coach, the study assistant in the Android app, with its Python backend. Five days, five stories. Get a biscuit."),
            leo("[laugh] I heard there's now a sign on the kitchen door that says, check the port. Start at the start. What's App Service?"),
            maya("Azure's web hosting. You bring the app, Azure runs the building: the machines, the updates, the web address."),
            leo("Meaning what, actually, under the hood?"),
            maya("A plan of rented machines, with a front door that takes web traffic and passes it to your app. The Coach brings its own container image, which App Service calls a custom container."),
            leo("Image meaning the lunchbox. The app packed with everything it needs. The intern asked me if there's a whole Windows inside."),
            maya("Bless. No. There's a slim copy of an operating system's files, the base image, but no kernel, the core that drives the hardware. The container borrows the kernel of the machine it runs on. So the Coach's Linux image needs a Linux plan, which the intern now knows."),
            leo("And the image comes from where?"),
            maya("The team's registry, a private warehouse for images. Their build pushes it in, and App Service pulls it, meaning downloads it. The registry episode has the details."),
            leo("And it gets into the warehouse with a password?"),
            maya("No, with a managed identity, a badge Azure gives the app. The badge holds AcrPull, the role that allows downloading and nothing more."),
            leo("Hang on. What's a role, again?"),
            maya("A named set of permissions. AcrPull is the smallest one that works for pulling. There's also a switch on the app, acrUseManagedIdentityCreds, which just says, use the badge for pulls. Nobody can pronounce it, which is half the office's fun."),
            leo("Okay. Who went first?"),
            maya("Tom. Monday. Tom deployed the backend, the container started, and every request timed out. Tom spent the morning convinced Azure was broken."),
            leo("Was it?"),
            maya("No. The Coach's backend listens on port 8000. A port is a numbered door on the machine where the app waits for visitors. App Service knocks on door 80 unless you say otherwise. Tom hadn't said otherwise."),
            leo("And how do you say otherwise?"),
            maya("With an app setting, a name and value you type into App Service, called websites port, set to 8000. On screen, capitals and an underscore. Hence the sign on the kitchen door."),
            leo("[chuckle] Tuesday?"),
            maya("Dana. Dana added the key for Azure OpenAI, the AI service the Coach talks to, as a plain app setting."),
            leo("What's wrong with that? Settings are handy."),
            maya("Very handy. Every app setting turns up inside the container as an environment variable, a named value the code reads at start-up, with os.environ in Python. Also visible to anyone who opens the settings page, which, on Tuesday, was the whole sales team."),
            leo("Oh no. Where should it have gone?"),
            maya("Into Key Vault, Azure's safe for secrets. The setting becomes a Key Vault reference: its value says, fetch this from the safe. App Service fetches the secret at start-up, and the container sees the real key. Dana fixed it by lunch, and rotated the key, meaning replaced it, by tea."),
            leo("Wednesday. Go."),
            maya("Marcus. Marcus followed Dana's fix exactly, and the app started reading a strange line of text instead of the key. It was the reference itself."),
            leo("Why?"),
            maya("Because App Service reads the secret using the app's managed identity, and Marcus never gave that badge a role on the vault. It needs Key Vault Secrets User."),
            leo("How did he not spot it?"),
            maya("When a reference can't be resolved, the app just gets the reference text. The portal, Azure's website where you manage it all, shows a green tick next to each one that worked. Marcus's had no tick."),
            leo("What's the badge, really, under the picture?"),
            maya("An account in Entra ID, Microsoft's sign-in system. Azure started the app, so Azure answers its requests for short-lived passes, on a private address nobody outside can reach. No password exists to steal. Marcus now checks for green ticks before coffee."),
            leo("Thursday. I sense a swap."),
            maya("The intern. The team uses a deployment slot now: a second live copy of the app with its own address, called staging, like a rehearsal stage."),
            leo("And the rehearsal goes live how?"),
            maya("With a swap. They test the new image in staging, then the two slots trade places, with no downtime, no moment where the site is down. The intern swapped on Thursday."),
            leo("And?"),
            maya("And production started writing learners' notes into the test database. Settings move with the swap, unless they're marked as a deployment slot setting. The database name wasn't marked. Swap back, which is the undo button, mark the database name as a slot setting, and it stays with its slot."),
            leo("Couldn't they have caught it in staging first?"),
            maya("That's the irony. Staging worked perfectly, because it was talking to its own test database. The new image was fine. Only the setting travelled with it. Warm up in staging, check, swap: that part the intern got right."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya pushed a fix to the registry and went home. Production ran the old image all weekend. Priya had switched on continuous deployment, but nobody had added the registry webhook."),
            leo("Webhook, in plain words?"),
            maya("A doorbell. On every push, the registry rings the app's deployment address, and the app pulls the new image. Without it, App Service only pulls on a restart. Priya found it on Monday, by turning on container logging and watching az webapp log tail, which streams the container's output live."),
            leo("Why doesn't App Service just check the registry by itself?"),
            maya("Because it only pulls when it starts. Without the doorbell, a new image sits in the warehouse until someone restarts the app. Priya restarted it by hand on Monday, then added the webhook, so it never happens again."),
            leo("Did anything else go wrong?"),
            maya("Only where all this stops. Slots need the Standard price level or higher, so the cheap Basic plan has none. And the Android app never holds a key; it calls the backend, which does. For a worker that should cost nothing at night, the Container Apps episode is next."),
            leo("So, the gossip I should remember for the exam?"),
            maya("If the container starts but requests time out on 8000, the answer is websites port set to 8000. If a key must stay out of the settings, a Key Vault reference, and Secrets User for the identity."),
            leo("And Thursday and Friday?"),
            maya("If staging keeps its own database after a swap, a slot setting. And if production ignores a pushed image, the registry webhook."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a Linux web app from a public sample image. Add an app setting, and print it from the container's log. Set websites port to match the image, and browse to the site. Then turn on container logging, and watch az webapp log tail while it restarts."),
            leo("Check the port, check the tick. Got it, Tom. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-appservice-containers",
        listOf(
            nova("Hey, you. It's late, the street outside has gone quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, your own container, on App Service."),
            ivy("Your own container. Sounds cosy. What's App Service, Nova?"),
            nova("Azure's web hosting. You bring the app, and Azure runs the building: the machines, the updates, the web address. Like a furnished flat. Under the picture, it's a plan of rented machines, with a front door that takes web traffic and passes it along to your app."),
            ivy("And I move in with my own lunchbox."),
            nova("Your container image, the app packed with everything it needs. That's a custom container."),
            ivy("Tell me honestly. Is there a whole operating system curled up in that lunchbox?"),
            nova("[pause] Just its slim files, the base image: a shell, the system libraries. Never the kernel, the core that talks to the hardware. Your container shares the kernel of the machine it sleeps on. So a Linux image wants a Linux plan."),
            ivy("And where does the lunchbox wait, before it moves in?"),
            nova("Picture the Two Tracks Coach, the study assistant in the Android app, with its Python backend packed as coach-api. Your build pushes it into a container registry, a private warehouse. App Service pulls it from there, downloads it, and starts it."),
            ivy("Pulls it with a password?"),
            nova("[pause] No password. With a managed identity, a badge Azure gives your app. Azure started the app, so it answers the app's requests for short-lived passes on a private address nobody outside can reach."),
            ivy("And the badge opens the warehouse door?"),
            nova("It holds AcrPull, the role that allows downloading images, and nothing more."),
            ivy("And what's a role, exactly?"),
            nova("A named set of permissions. AcrPull is the smallest one that lets the badge download images. Then one switch on the app tells App Service to use the badge for pulls. [pause] Quiet, sensible, no secrets."),
            ivy("So it's in, and it's running. How does it know where its database is?"),
            nova("App settings. A name and a value you type into App Service. When the container starts, each one arrives inside as an environment variable, a named value the code reads, like a note left on the pillow. Python reads it with os.environ. And changing a setting restarts the app."),
            ivy("A note on the pillow. You're sweet tonight."),
            nova("Only a little. Now, a gentle warning. The Coach's backend listens on port 8000. A port is a numbered door, where the app waits for visitors. App Service knocks on door 80, unless you tell it otherwise."),
            ivy("And if nobody tells it?"),
            nova("Then it knocks, and knocks, and nobody answers. Every request times out. [pause] So you add one more app setting, websites port, set to 8000. On screen, capitals and an underscore. It just says, knock on this door."),
            ivy("Now the secrets. Whisper them."),
            nova("Never in the image, never as a plain setting. Keep them in Key Vault, Azure's safe. The app setting becomes a Key Vault reference, a value that says, fetch this from the safe. App Service fetches it at start-up, with the same badge, and the container sees the real value."),
            ivy("And the badge just walks into the safe?"),
            nova("Only if you give it the Key Vault Secrets User role. If you forget, the reference can't be resolved, and the app reads the reference text itself instead of the secret."),
            ivy("And how would I know, at this hour?"),
            nova("Look for the green tick in the portal, Azure's website where you manage it all. [pause] And leave the version off the reference, so a rotated secret, a replaced one, is picked up."),
            ivy("Mm. Now tell me how to change things without anyone noticing."),
            nova("A deployment slot. A second live copy of your app, with its own address, called staging. A rehearsal stage, right beside the main one. You put the new image there, warm it up, check it, [pause] and then swap."),
            ivy("Swap. Slowly."),
            nova("The two slots trade places, with no downtime, not a moment where the site is gone. If something's wrong, you swap back. That's your undo. Slots need the Standard price level or higher, though. Basic has none."),
            ivy("Does everything go with the swap?"),
            nova("Most settings follow it. But some belong to the stage, not the actor. Staging's test database, for example. Tick deployment slot setting on it, and it stays where it is. That's a slot setting."),
            ivy("And the image itself? Does it know which stage it's on?"),
            nova("It doesn't need to. The same image runs in both slots. Only the settings differ, the notes on the pillow. That's the beauty of it. [pause] Build once, and let each stage tell it where it is."),
            ivy("And when you push a new image at midnight? Does App Service wake up for it?"),
            nova("Not on its own. It only pulls again on a restart. Turn on continuous deployment, and give the registry a webhook, a doorbell. Every push, the registry rings the app's address, and the app pulls the new image."),
            ivy("And if I want to watch what it's saying, at this hour?"),
            nova("Turn on container logging, and run az webapp log tail on Azure's command line. The container's output streams in, live. Start-up lines, errors, the lot. Very late-night television."),
            ivy("Give me the whole Coach, then, put together."),
            nova("A Linux plan at the Standard level. The image pulled with the app's badge, holding AcrPull. Websites port at 8000. Secrets as Key Vault references. A staging slot with its own slot settings. And a registry webhook, so every push goes out by itself."),
            ivy("Where does all this stop working?"),
            nova("A phone app never holds a secret; it calls your backend, which does. Slots stop at the Basic price level. And if your worker should cost nothing while it sleeps, App Service always keeps something running. The Container Apps episode is your next late night for that."),
            ivy("[sigh] Mm. Before you send me to bed. What do I remember for the exam?"),
            nova("If the container starts but requests time out on 8000, the answer is websites port set to 8000. If a key must stay out of code, a Key Vault reference. If staging keeps its own database after a swap, a slot setting."),
            ivy("Mm. And the new image at midnight?"),
            nova("If production ignores a pushed image, the registry webhook. And pulling with no password, a managed identity with AcrPull."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a Linux web app from a public sample image. Add an app setting, and print it from the container's log. [pause] Set websites port to match the image, and browse to the site. Then turn on container logging, and watch az webapp log tail while it restarts."),
            ivy("Knock on the right door. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-appservice-containers",
        listOf(
            nova("Tonight is date night, darling. Candlelight, a little music, and you and me, Rafe, talking about moving in together."),
            rafe("Moving in? That's a big step."),
            nova("[laugh] Your container, moving into App Service. Azure's web hosting. You bring the app, Azure runs the building: machines, updates, web address. Under the picture, it's a plan of rented machines with a front door that passes web traffic to your app."),
            rafe("A furnished flat. I can do furnished. What do I bring?"),
            nova("Your own container image, the app packed with everything it needs, like an overnight bag. That's a custom container."),
            rafe("Quiz before we move in. Does the bag carry its own operating system?"),
            nova("Only the slim files of one, the base image. No kernel, the core that drives the hardware; the container shares the host's. So a Linux image needs a Linux plan."),
            rafe("Right answer. You do travel light. [kiss]"),
            nova("Light, but never underdressed."),
            rafe("And where does my bag wait?"),
            nova("In a container registry, a private warehouse. Picture the Two Tracks Coach, the study assistant in the Android app, with its Python backend as coach-api. Your build pushes it in, and App Service pulls it, meaning downloads it, from there."),
            rafe("And it gets into the warehouse how? I'm not sharing my password."),
            nova("No password. A managed identity, a badge Azure gives the app, holding AcrPull: download images, nothing more. Azure answers the badge's requests for short-lived passes on a private address nobody outside can reach."),
            rafe("And what's a role, exactly? I like to know my place."),
            nova("A named set of permissions. AcrPull is the smallest one that lets the badge download images. Then one switch on the app says, use the badge for pulls. It's all about knowing your role."),
            rafe("Then let me test you, darling. Exam style. The Coach container starts, but every request times out. It listens on port 8000. What's missing?"),
            nova("A port is a numbered door where the app waits for visitors, and App Service knocks on door 80 by default. So, an app setting called websites port, set to 8000. On screen, capitals and an underscore."),
            rafe("Right answer. Always knock on the right door. [kiss]"),
            nova("Mm. And what's an app setting, for anyone listening?"),
            rafe("A name and a value you type into App Service. When the container starts, each one arrives inside as an environment variable, a named value the code reads at start-up, with os.environ in Python. Like a note left on the pillow. And changing one restarts the app."),
            nova("A note on the pillow. You do remember things. My turn to quiz you. The Coach's key for Azure OpenAI, the AI service it talks to. Where does it go?"),
            rafe("In Key Vault, Azure's safe for secrets. The app setting becomes a Key Vault reference, a value that says, fetch this from the safe. App Service fetches the secret at start-up, and the container sees the real key. Never a plain setting."),
            nova("Right answer. Some things stay private. [kiss]"),
            rafe("Now a harder one for you. The reference is set up, but the app reads a strange line of text instead of the key. Why?"),
            nova("The badge has no role on the vault, so the reference can't be resolved, and the app gets the reference text itself. Give the managed identity Key Vault Secrets User. And look for the green tick in the portal, Azure's website where you manage it all."),
            rafe("Right answer. You always know what's holding us back. [kiss]"),
            nova("And leave the version off the reference, so when you rotate the key, meaning replace it, the app picks up the new one. Fresh starts are healthy."),
            rafe("Speaking of fresh starts. How do we release a new version without a scene?"),
            nova("A deployment slot. A second live copy of the app, with its own address, called staging. A rehearsal stage."),
            rafe("A rehearsal. And how does it get on stage?"),
            nova("Put the new image there, warm it up, then swap. The two slots trade places with no downtime, not one moment offline. And swapping back is the undo."),
            rafe("Every couple needs an undo. Slots on any plan?"),
            nova("Standard price level or higher. Basic has none. Now, quiz. Staging must keep its own test database, even after a swap. What do we do with the database name?"),
            rafe("Make it a Key Vault reference."),
            nova("[sigh] No kiss for that one. A reference changes where a value comes from, not whether it swaps. Mark it as a deployment slot setting. A slot setting stays with its slot. Some things belong to the room, not to whoever's in it."),
            rafe("Point taken. My question, then. We push a new image to the registry, and production keeps running the old one. Continuous deployment is meant to be on. What do we check?"),
            nova("The webhook, which is like a doorbell: on every push, the registry rings the app's deployment address, and the app pulls the new image. Without it, App Service only pulls on a restart."),
            rafe("Right answer. I'd ring your doorbell any time. [kiss]"),
            nova("[chuckle] Behave. And when things go quiet, turn on container logging and run az webapp log tail. The container's output streams in live. No secrets between us."),
            rafe("So the whole Coach, put together, darling?"),
            nova("A Linux plan at the Standard level. The image pulled with the badge holding AcrPull. Websites port at 8000. Secrets as Key Vault references. A staging slot with slot settings. And a registry webhook. A well-run household."),
            rafe("And where does it all stop working? Every romance has its limits."),
            nova("The phone never holds a secret; it calls the backend, which does. Slots stop at the Basic price level. And App Service always keeps something running, so a worker that should cost nothing at night belongs in the Container Apps episode. The registry episode covers how the image is built."),
            rafe("Last question, for the exam. What do we remember?"),
            nova("If requests time out on port 8000, the answer is websites port set to 8000. If a key must stay out of code, a Key Vault reference. If staging keeps its own database after a swap, a slot setting."),
            rafe("That's three. Don't stop now."),
            nova("If production ignores a pushed image, the registry webhook. And pulling with no password, a managed identity with AcrPull."),
            rafe("Five for five. That earns a long one. [kiss]"),
            nova("Mm. And something to try today, my love?"),
            rafe("Create a Linux web app from a public sample image. Add an app setting, and print it from the container's log. Set websites port to match the image, and browse to the site. Then turn on container logging, and watch az webapp log tail while it restarts."),
            nova("Knock on the right door, darling. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun pull() = DiagramSpec(
        "App Service pulls the custom container from the registry with its managed identity, then starts it.",
        nodes = listOf(
            DNode("acr", "Registry", 0.2f, 0.18f, w = 0.36f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 2, sub = "coach-api image"),
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
            DNode("port", "WEBSITES_PORT", 0.75f, 0.15f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "8000, not 80"),
            DNode("env", "Environment variables", 0.5f, 0.5f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, sub = "read with os.environ"),
            DNode("cont", "Container", 0.25f, 0.85f, w = 0.42f, h = 0.2f, sub = "listens on 8000"),
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
        "Where it stops: the phone holds no secret, slots need Standard, and an always-on host is not scale to zero.",
        nodes = listOf(
            DNode("phone", "The phone holds no secret", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "it calls your backend"),
            DNode("version", "No version on the reference", 0.76f, 0.2f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "rotated keys arrive"),
            DNode("plan", "Slots: Standard or higher", 0.26f, 0.76f, w = 0.46f, h = 0.2f, tone = 2, sub = "Basic has none"),
            DNode("next", "Other episodes", 0.76f, 0.76f, w = 0.42f, h = 0.2f, tone = 1, sub = "ACR, Container Apps"),
        ),
        edges = listOf(DEdge("plan", "next", "next stop")),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's App Service container questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("port", "Times out on 8000", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "WEBSITES_PORT=8000"),
            DNode("secret", "Key out of the code", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Key Vault reference"),
            DNode("slot", "Staging keeps its DB", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "slot setting"),
            DNode("cd", "Push, prod unchanged", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "ACR webhook"),
            DNode("pull", "Pull, no password", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "identity with AcrPull"),
            DNode("tier", "Slots on Basic?", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Standard or higher"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("port", "secret", "slot", "cd", "pull", "tier")
    private val today = BoardCard("Today", "A Linux web app, a setting in the log, WEBSITES_PORT, then az webapp log tail.")
    private val useIdentity = BoardCard("acrUseManagedIdentityCreds", "With AcrPull on the badge, this switch says: use it for pulls.")
    private val tick = BoardCard("Key Vault Secrets User", "The badge needs this role on the vault. Look for the green tick.")
    private val logTail = BoardCard("az webapp log tail", "Container logging on: the container's output streams in live.")

    private fun classicBoard() = PodcastBoard(
        "ai-appservice-containers",
        listOf(
            // 0: "Hi, and welcome back. Today, your own container, running on App Servic..."
            Scene(
                0, "Moving in", pull(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("App Service", "Azure's web hosting: you bring the app, Azure runs the building.")),
                    Beat(2, show = setOf("cont"), light = setOf("cont"), pulse = "app" to "cont", card = BoardCard("Custom container", "Your own image, instead of the built-in Python setup.")),
                    Beat(4, light = setOf("cont"), card = BoardCard("No kernel inside", "A slim OS base, no kernel: it shares the host's. Linux image, Linux plan.")),
                    Beat(6, show = setOf("acr"), light = setOf("acr", "app"), pulse = "acr" to "app", card = BoardCard("Pull from the registry", "coach-api from ttcoach: downloaded, started, sent web traffic.")),
                    Beat(10, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "app", card = BoardCard("Managed identity", "A badge Azure gives your app. No registry password stored.")),
                    Beat(12, light = setOf("mi", "acr"), card = BoardCard("AcrPull", "A role, a named set of permissions: download images, nothing more.")),
                    Beat(14, light = setOf("mi", "acr"), card = useIdentity),
                ),
            ),
            // 15: "Rolls off the tongue. Okay, it's pulled and started. How does my app g..."
            Scene(
                15, "Settings become variables", settings(),
                beats = listOf(
                    Beat(16, show = setOf("app", "env"), light = setOf("app", "env"), pulse = "app" to "env", card = BoardCard("App setting", "A name and a value, like COSMOS_ENDPOINT, typed into App Service.")),
                    Beat(18, show = setOf("cont"), light = setOf("env", "cont"), pulse = "env" to "cont", card = BoardCard("Environment variable", "A note the code reads at start, with os.environ. A change restarts.")),
                    Beat(20, show = setOf("web"), light = setOf("web"), pulse = "web" to "cont", card = BoardCard("Port", "A numbered door. App Service knocks on door 80 by default.")),
                    Beat(22, show = setOf("port"), light = setOf("port"), pulse = "port" to "web", card = BoardCard("WEBSITES_PORT", "Set it to 8000, and App Service knocks on the right door.")),
                ),
            ),
            // 23: "Now secrets. The Coach has a key for Azure OpenAI, the AI service it t..."
            Scene(
                23, "Secrets from the safe", secrets(),
                beats = listOf(
                    Beat(24, show = setOf("kv", "ref"), light = setOf("kv", "ref"), card = BoardCard("Key Vault reference", "Not the key itself: a setting that says, fetch this from the safe.")),
                    Beat(26, show = setOf("app", "mi"), light = setOf("mi"), pulse = "mi" to "kv", card = tick),
                    Beat(28, show = setOf("env"), light = setOf("env"), pulse = "app" to "env", card = BoardCard("No tick, no key", "Unresolved, the app sees the reference text. Leave the version off.")),
                ),
            ),
            // 31: "Okay. Now how do I release a new version without breaking things?"
            Scene(
                31, "Slots and swaps", slots(),
                beats = listOf(
                    Beat(32, show = setOf("prod", "stage"), light = setOf("stage"), card = BoardCard("Deployment slot", "A second live copy with its own address. Standard tier or higher.")),
                    Beat(34, show = setOf("swap"), light = setOf("swap"), pulse = "stage" to "swap", card = BoardCard("Swap", "The slots trade places with no downtime. Swap back to undo.")),
                    Beat(38, show = setOf("slotset", "normal"), light = setOf("slotset"), pulse = "slotset" to "swap", card = BoardCard("Slot setting", "Stays with its slot during a swap, like staging's test database.")),
                ),
            ),
            // 39: "Nice. Last piece. I push a new image to the registry. Does App Service..."
            Scene(
                39, "New images, live", cd(),
                beats = listOf(
                    Beat(42, show = setOf("acr", "hook", "app"), light = setOf("hook"), pulse = "acr" to "hook", card = BoardCard("Continuous deployment", "The registry's webhook rings on every push, and the app pulls.")),
                    Beat(44, show = setOf("logs"), light = setOf("logs"), pulse = "app" to "logs", card = logTail),
                ),
            ),
            // 45: "A ticker for my bugs. What are the usual mistakes?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = setOf("port", "secret"), light = setOf("port", "secret"), card = BoardCard("The usual mistakes", "No port, a plain key, no vault role, no slot setting, no webhook.")),
                    Beat(50, show = setOf("slot", "cd", "pull", "tier"), light = setOf("port", "secret", "slot", "cd", "pull"), card = BoardCard("Listen for the phrase", "Port 8000, a reference, a slot setting, a webhook, AcrPull.")),
                    Beat(54, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-appservice-containers--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Plant That Never Answered. A..."
            Scene(
                0, "The Plant That Never Answered", pull(),
                beats = listOf(
                    Beat(0, show = setOf("app", "cont"), light = setOf("cont"), pulse = "app" to "cont", card = BoardCard("The case", "PlantPal's container starts, and every request times out.")),
                    Beat(4, light = setOf("app"), card = BoardCard("App Service", "Rented machines, a front door for web traffic, your container.")),
                    Beat(6, light = setOf("cont"), card = BoardCard("No kernel inside", "The container shares the host's kernel: a Linux image, a Linux plan.")),
                    Beat(8, show = setOf("acr"), light = setOf("acr"), pulse = "acr" to "app", card = BoardCard("Container registry", "The private warehouse PlantPal's image is pulled from.")),
                    Beat(10, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "app", card = useIdentity),
                ),
            ),
            // 11: "Then my first suspect: App Service couldn't get into the warehouse. Mi..."
            Scene(
                11, "Suspect one", settings(),
                beats = listOf(
                    Beat(12, show = setOf("cont"), light = setOf("cont"), card = BoardCard("AcrPull: cleared", "Without it, the container would never have started at all.")),
                    Beat(14, show = setOf("web"), light = setOf("web"), pulse = "web" to "cont", card = BoardCard("Door 80, door 8000", "App Service knocks on 80. PlantPal waits behind 8000.")),
                    Beat(16, show = setOf("app", "port", "env"), light = setOf("port"), pulse = "port" to "web", card = BoardCard("WEBSITES_PORT", "An app setting set to 8000: knock on this door.")),
                ),
            ),
            // 17: "Case closed, then."
            Scene(
                17, "The leaked screenshot", secrets(),
                beats = listOf(
                    Beat(18, card = BoardCard("The second witness", "A settings screenshot showed the weather service key.")),
                    Beat(20, show = setOf("env"), light = setOf("env"), card = BoardCard("Environment variable", "Each app setting arrives in the container; os.environ reads it.")),
                    Beat(22, show = setOf("kv", "ref"), light = setOf("kv", "ref"), card = BoardCard("Key Vault reference", "@Microsoft.KeyVault(VaultName=plantpal-kv;SecretName=WeatherKey)", code = true)),
                    Beat(24, show = setOf("app", "mi"), light = setOf("mi"), pulse = "mi" to "kv", card = BoardCard("Key Vault Secrets User", "No role, no secret: the app got the reference text instead.")),
                    Beat(28, light = setOf("mi", "app"), pulse = "app" to "mi", card = BoardCard("Under the badge", "An Entra ID account; short-lived passes on a private address.")),
                ),
            ),
            // 31: "Third case?"
            Scene(
                31, "The third case", slots(),
                beats = listOf(
                    Beat(32, show = setOf("prod", "stage", "swap"), light = setOf("swap"), pulse = "stage" to "swap", card = BoardCard("Deployment slot", "Staging: a second live copy. Test there, then swap.")),
                    Beat(34, light = setOf("swap"), card = BoardCard("Swap", "The two slots trade places, with no moment where the site is down.")),
                    Beat(36, show = setOf("slotset", "normal"), light = setOf("slotset", "normal"), pulse = "normal" to "swap", card = BoardCard("Settings travel", "Unless marked as a slot setting. The test database walked on.")),
                    Beat(38, light = setOf("swap", "prod"), pulse = "swap" to "prod", card = BoardCard("Swap back", "Rolling back is just one more swap.")),
                ),
            ),
            // 39: "And the last clue in the file?"
            Scene(
                39, "The last clue", cd(),
                beats = listOf(
                    Beat(40, show = setOf("acr", "hook", "app"), light = setOf("hook"), pulse = "acr" to "hook", card = BoardCard("Webhook", "On every push, the registry rings the app, and the app pulls.")),
                    Beat(44, show = setOf("logs"), light = setOf("logs"), pulse = "app" to "logs", card = logTail),
                ),
            ),
            // 45: "And where does all this stop working?"
            Scene(
                45, "Where it stops", limits(),
                beats = listOf(
                    Beat(46, show = setOf("phone", "version", "plan", "next"), light = setOf("plan", "phone"), pulse = "plan" to "next", card = BoardCard("Where it stops", "Slots need Standard or higher. The phone never holds a secret.")),
                ),
            ),
            // 47: "So, the case summary for the exam?"
            Scene(
                47, "Case closed", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("port", "secret", "slot", "cd"), card = BoardCard("Listen for the phrase", "Port 8000, a reference with Secrets User, a slot setting, a webhook.")),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-appservice-containers--gossip",
        listOf(
            // 0: "Okay, did you hear what happened when the Coach team moved to App Serv..."
            Scene(
                0, "Moving the Coach in", pull(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("The week", "The Coach backend moved to App Service. Five days, five stories.")),
                    Beat(4, show = setOf("cont"), light = setOf("cont"), pulse = "app" to "cont", card = BoardCard("Custom container", "App Service runs the Coach's own image.")),
                    Beat(6, light = setOf("cont"), card = BoardCard("No whole Windows inside", "A slim OS base, no kernel: it borrows the host's. Linux image, Linux plan.")),
                    Beat(8, show = setOf("acr"), light = setOf("acr"), pulse = "acr" to "app", card = BoardCard("Push in, pull out", "The build pushes into the registry; App Service pulls from it.")),
                    Beat(10, show = setOf("mi"), light = setOf("acr", "mi"), pulse = "acr" to "app", card = BoardCard("Pulled with a badge", "A managed identity holding AcrPull. No registry password.")),
                    Beat(12, light = setOf("mi"), pulse = "mi" to "app", card = useIdentity),
                ),
            ),
            // 13: "Okay. Who went first?"
            Scene(
                13, "Monday: Tom", settings(),
                beats = listOf(
                    Beat(14, show = setOf("cont", "web"), light = setOf("cont"), card = BoardCard("Every request timed out", "The container started. Tom blamed Azure.")),
                    Beat(16, light = setOf("web"), pulse = "web" to "cont", card = BoardCard("Door 80, door 8000", "App Service knocks on 80; the Coach waits behind 8000.")),
                    Beat(18, show = setOf("app", "port", "env"), light = setOf("port"), pulse = "port" to "web", card = BoardCard("WEBSITES_PORT", "An app setting set to 8000. Hence the sign on the kitchen door.")),
                ),
            ),
            // 19: "Tuesday?"
            Scene(
                19, "Tuesday: Dana", secrets(),
                beats = listOf(
                    Beat(20, show = setOf("env"), light = setOf("env"), card = BoardCard("A plain app setting", "Every setting becomes an environment variable. Visible to all.")),
                    Beat(24, show = setOf("kv", "ref"), light = setOf("kv", "ref"), card = BoardCard("Key Vault reference", "The setting says: fetch this from the safe.")),
                ),
            ),
            // 25: "Wednesday. Go."
            Scene(
                25, "Wednesday: Marcus", secrets(),
                beats = listOf(
                    Beat(26, show = setOf("ref", "env"), light = setOf("env"), card = BoardCard("The reference text", "The app read the reference itself, not the key.")),
                    Beat(28, show = setOf("kv", "app", "mi"), light = setOf("mi"), pulse = "mi" to "kv", card = tick),
                    Beat(32, light = setOf("mi", "app"), pulse = "app" to "mi", card = BoardCard("Under the badge", "An Entra ID account; short-lived passes on a private address.")),
                ),
            ),
            // 33: "Thursday. I sense a swap."
            Scene(
                33, "Thursday: the intern", slots(),
                beats = listOf(
                    Beat(34, show = setOf("prod", "stage", "swap"), light = setOf("stage"), pulse = "stage" to "swap", card = BoardCard("Deployment slot", "Staging: a second live copy. Test there, then swap.")),
                    Beat(36, light = setOf("swap"), card = BoardCard("Swap", "The two slots trade places, with no moment where the site is down.")),
                    Beat(38, show = setOf("slotset", "normal"), light = setOf("slotset"), pulse = "slotset" to "swap", card = BoardCard("Slot setting", "The database name wasn't marked, so it travelled.")),
                    Beat(40, light = setOf("stage"), card = BoardCard("Staging was fine", "Only the setting travelled. Swapping back is the undo.")),
                ),
            ),
            // 41: "And Friday? There's always a Friday."
            Scene(
                41, "Friday: Priya", cd(),
                beats = listOf(
                    Beat(42, show = setOf("acr", "app"), light = setOf("acr"), card = BoardCard("A weekend on the old image", "Continuous deployment on, but no webhook.")),
                    Beat(44, show = setOf("hook"), light = setOf("hook"), pulse = "acr" to "hook", card = BoardCard("Webhook", "A doorbell: every push rings the app, and it pulls.")),
                    Beat(46, show = setOf("logs"), light = setOf("logs"), pulse = "app" to "logs", card = logTail),
                ),
            ),
            // 47: "Did anything else go wrong?"
            Scene(
                47, "Where it stops", limits(),
                beats = listOf(
                    Beat(48, show = setOf("phone", "version", "plan", "next"), light = setOf("plan", "phone"), pulse = "plan" to "next", card = BoardCard("Where it stops", "Slots need Standard or higher. The phone never holds a key.")),
                ),
            ),
            // 49: "So, the gossip I should remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("port", "secret", "slot", "cd"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer. Nobody wants to be next week's story.")),
                    Beat(54, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-appservice-containers--late-night",
        listOf(
            // 0: "Hey, you. It's late, the street outside has gone quiet, and it's just..."
            Scene(
                0, "Your own container", pull(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("Tonight", "Your own container, on App Service.")),
                    Beat(2, light = setOf("app"), card = BoardCard("App Service", "Azure's web hosting: a furnished flat for your app.")),
                    Beat(4, show = setOf("cont"), light = setOf("cont"), pulse = "app" to "cont", card = BoardCard("Custom container", "Your own image, the app packed with all it needs.")),
                    Beat(6, light = setOf("cont"), card = BoardCard("No kernel inside", "Slim OS files only; it shares the host's kernel. Linux image, Linux plan.")),
                    Beat(8, show = setOf("acr"), light = setOf("acr"), pulse = "acr" to "app", card = BoardCard("Pushed, then pulled", "Your build pushes coach-api in; App Service pulls it and starts it.")),
                    Beat(10, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "app", card = BoardCard("Managed identity", "A badge holding AcrPull. No password.")),
                    Beat(14, light = setOf("mi"), card = useIdentity),
                ),
            ),
            // 15: "So it's in, and it's running. How does it know where its database is?"
            Scene(
                15, "Notes on the pillow", settings(),
                beats = listOf(
                    Beat(16, show = setOf("app", "env", "cont"), light = setOf("env"), pulse = "app" to "env", card = BoardCard("App setting", "Arrives as an environment variable. os.environ reads it.")),
                    Beat(18, show = setOf("web"), light = setOf("web"), pulse = "web" to "cont", card = BoardCard("Port", "A numbered door. App Service knocks on door 80.")),
                    Beat(20, show = setOf("port"), light = setOf("port"), pulse = "port" to "web", card = BoardCard("WEBSITES_PORT", "Set to 8000: knock on this door.")),
                ),
            ),
            // 21: "Now the secrets. Whisper them."
            Scene(
                21, "Secrets, whispered", secrets(),
                beats = listOf(
                    Beat(22, show = setOf("kv", "ref", "app"), light = setOf("ref"), pulse = "ref" to "app", card = BoardCard("Key Vault reference", "Fetch this from the safe, at start-up.")),
                    Beat(24, show = setOf("mi", "env"), light = setOf("mi"), pulse = "mi" to "kv", card = tick),
                ),
            ),
            // 27: "Mm. Now tell me how to change things without anyone noticing."
            Scene(
                27, "The rehearsal stage", slots(),
                beats = listOf(
                    Beat(28, show = setOf("prod", "stage"), light = setOf("stage"), card = BoardCard("Deployment slot", "Staging: a second live copy with its own address.")),
                    Beat(30, show = setOf("swap"), light = setOf("swap"), pulse = "stage" to "swap", card = BoardCard("Swap", "Trade places, no downtime; swap back to undo. Standard or higher.")),
                    Beat(32, show = setOf("slotset", "normal"), light = setOf("slotset"), pulse = "slotset" to "swap", card = BoardCard("Slot setting", "It belongs to the stage, not the actor.")),
                    Beat(34, light = setOf("prod", "stage"), card = BoardCard("Build once", "The same image in both slots; only the settings differ.")),
                ),
            ),
            // 35: "And when you push a new image at midnight? Does App Service wake up fo..."
            Scene(
                35, "At midnight", cd(),
                beats = listOf(
                    Beat(36, show = setOf("acr", "hook", "app"), light = setOf("hook"), pulse = "acr" to "hook", card = BoardCard("Webhook", "Every push rings the app's address, and the app pulls.")),
                    Beat(38, show = setOf("logs"), light = setOf("logs"), pulse = "app" to "logs", card = logTail),
                    Beat(40, light = setOf("app", "hook"), card = BoardCard("The whole Coach", "Standard plan, AcrPull, port 8000, references, slots, webhook.")),
                ),
            ),
            // 41: "Where does all this stop working?"
            Scene(
                41, "Where it stops", limits(),
                beats = listOf(
                    Beat(42, show = setOf("phone", "version", "plan", "next"), light = setOf("phone", "next"), pulse = "plan" to "next", card = BoardCard("Your next late night", "The Container Apps episode: a worker that sleeps for free.")),
                ),
            ),
            // 43: "Mm. Before you send me to bed. What do I remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("port", "secret", "slot", "cd", "pull"), card = BoardCard("Listen for the phrase", "Port 8000, a reference, a slot setting, a webhook, AcrPull.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-appservice-containers--couples",
        listOf(
            // 0: "Tonight is date night, darling. Candlelight, a little music, and you a..."
            Scene(
                0, "Moving in together", pull(),
                beats = listOf(
                    Beat(0, show = setOf("app"), card = BoardCard("Date night", "Moving in together: your container, on App Service.")),
                    Beat(2, light = setOf("app"), card = BoardCard("App Service", "Azure's web hosting: you bring the app, Azure runs the building.")),
                    Beat(4, show = setOf("cont"), light = setOf("cont"), pulse = "app" to "cont", card = BoardCard("Custom container", "The Coach's own image: an overnight bag.")),
                    Beat(5, light = setOf("cont"), card = BoardCard("Quiz: its own OS?", "Slim OS files only, no kernel: it shares the host's. Linux plan.")),
                    Beat(10, show = setOf("acr"), light = setOf("acr"), pulse = "acr" to "app", card = BoardCard("Pushed, then pulled", "Your build pushes coach-api in; App Service pulls it out.")),
                    Beat(12, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "app", card = BoardCard("Managed identity", "A badge holding AcrPull. No password shared.")),
                    Beat(14, light = setOf("mi"), card = BoardCard("Knowing your role", "AcrPull: the smallest role that downloads images.")),
                ),
            ),
            // 15: "Then let me test you, darling. Exam style. The Coach container starts,..."
            Scene(
                15, "The right door", settings(),
                beats = listOf(
                    Beat(15, show = setOf("cont", "web"), light = setOf("web"), pulse = "web" to "cont", card = BoardCard("Quiz: timing out", "The container listens on 8000, and every request times out.")),
                    Beat(16, show = setOf("port"), light = setOf("port"), pulse = "port" to "web", card = BoardCard("Right answer", "WEBSITES_PORT set to 8000.")),
                    Beat(19, show = setOf("app", "env"), light = setOf("env"), pulse = "app" to "env", card = BoardCard("App setting", "A note on the pillow: an environment variable at start-up.")),
                ),
            ),
            // 20: "A note on the pillow. You do remember things. My turn to quiz you. The..."
            Scene(
                20, "Some things stay private", secrets(),
                beats = listOf(
                    Beat(20, show = setOf("kv"), light = setOf("kv"), card = BoardCard("Quiz: the OpenAI key", "Where does the Coach's key go?")),
                    Beat(21, show = setOf("ref", "app"), light = setOf("ref"), pulse = "ref" to "app", card = BoardCard("Key Vault reference", "Fetch this from the safe. Never a plain setting.")),
                    Beat(23, show = setOf("env"), light = setOf("env"), card = BoardCard("Quiz: strange text", "The app reads the reference text, not the key. Why?")),
                    Beat(24, show = setOf("mi"), light = setOf("mi"), pulse = "mi" to "kv", card = tick),
                    Beat(26, light = setOf("kv"), card = BoardCard("Rotate", "Leave the version off, and a replaced key is picked up.")),
                ),
            ),
            // 27: "Speaking of fresh starts. How do we release a new version without a sc..."
            Scene(
                27, "Every couple needs an undo", slots(),
                beats = listOf(
                    Beat(28, show = setOf("prod", "stage", "swap"), light = setOf("swap"), pulse = "stage" to "swap", card = BoardCard("Deployment slot", "Staging: test there, then swap. Swap back to undo.")),
                    Beat(30, light = setOf("swap"), card = BoardCard("Swap", "The two slots trade places, with no moment where the site is down.")),
                    Beat(32, show = setOf("slotset", "normal"), light = setOf("slotset"), card = BoardCard("Quiz: staging's database", "It must stay put after a swap. What do we do?")),
                    Beat(34, light = setOf("slotset"), pulse = "slotset" to "swap", card = BoardCard("No kiss: a slot setting", "A reference changes the source, not whether it swaps.")),
                ),
            ),
            // 35: "Point taken. My question, then. We push a new image to the registry, a..."
            Scene(
                35, "The doorbell", cd(),
                beats = listOf(
                    Beat(35, show = setOf("acr", "app"), light = setOf("acr"), card = BoardCard("Quiz: the old image", "A push, and production still runs the old image. What do we check?")),
                    Beat(36, show = setOf("hook"), light = setOf("hook"), pulse = "acr" to "hook", card = BoardCard("Right answer", "The webhook: every push rings the app, and it pulls.")),
                    Beat(38, show = setOf("logs"), light = setOf("logs"), pulse = "app" to "logs", card = logTail),
                    Beat(40, light = setOf("app", "hook"), card = BoardCard("The whole Coach", "Standard plan, AcrPull, port 8000, references, slots, webhook.")),
                ),
            ),
            // 41: "And where does it all stop working? Every romance has its limits."
            Scene(
                41, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(42, show = setOf("phone", "version", "plan", "next"), light = setOf("phone", "plan"), pulse = "plan" to "next", card = BoardCard("Where it stops", "The phone holds no secret. Slots need Standard or higher.")),
                ),
            ),
            // 43: "Last question, for the exam. What do we remember?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("port", "secret", "slot", "cd", "pull"), card = BoardCard("Listen for the phrase", "Port 8000, a reference, a slot setting, a webhook, AcrPull.")),
                    Beat(49, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
