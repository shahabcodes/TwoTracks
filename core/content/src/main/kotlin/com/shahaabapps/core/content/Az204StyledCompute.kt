package com.shahaabapps.core.content

/** The Compute module's App Service, slots and Functions topics as Case Files and Gossip, with their boards (Az204StyledPodcasts). */
internal object Az204StyledCompute {
    private val appServiceCase = Podcast(
        "az-app-service",
        listOf(
            maya("Welcome to Case Files. Today's case: The Six O'Clock Stall. A small climbing gym sells wall slots through an Android app. The app talks to a small backend API, hosted on Azure App Service. On Monday at six in the morning, the first members to book stare at a spinning wheel for half a minute. Then a few bookings fail outright."),
            leo("[chuckle] A climbing gym, and the app falls off the wall. Detective Leo reporting. First, what is App Service, in plain words?"),
            maya("Azure's just run my web app service. You hand it your code, or a container, and it gives you a web address, HTTPS, scaling and patching. You never log into a server. Picture renting a shelf in a well-run bakery. You bring the cake, your app, and the bakery owns the ovens, the cleaning and the front door."),
            leo("Okay. So what changed before Monday? Every crime has a before."),
            maya("Good habit. Over the weekend, the developer did two things to save money. They moved the backend from a Windows plan to a new Linux plan, on the Free tier. And they took the database password out of a settings file in the code, and typed it into Azure instead."),
            leo("Then my first suspect is the database. It's down, or it's slow first thing in the morning."),
            maya("[pause] Wrong suspect. The gym's admin site uses the same database, and it answered instantly all morning. And notice the pattern. Only the first booking after the quiet night stalls. Everything after it is quick."),
            leo("Suspect two, then. The machine is too small. Make it bigger and it'll be faster."),
            maya("That's the exam's favourite distractor. A bigger worker makes a running app faster. It doesn't help an app that isn't running. And that's the clue. When an app sits idle, the platform unloads it to save resources. The first request has to start it up again, and that member waits."),
            leo("So the app was asleep. How do you keep it awake?"),
            maya("A setting called Always On. Under the picture, it's simple. App Service sends your app a small request every few minutes, so it never counts as idle and never gets unloaded. But Always On is offered from the Basic tier upwards. On Free, you can't switch it on. The weekend's saving switched it off."),
            leo("A tier is a price level, right? Like phone contracts?"),
            maya("Exactly. Free and Shared are for trying things out, on shared hardware, with no scaling. Basic gives you your own workers and custom domains, scaled by hand. Standard adds auto scale, deployment slots and daily backups. Premium adds more power and private networking, and Isolated gives you a whole environment of your own."),
            leo("Wait, where does the tier live? On the app?"),
            maya("On the App Service plan. Every app has two parts. The plan is the shelf: the region, Windows or Linux, the tier, and how many workers you get. Under the picture, those workers are virtual machines that Azure runs and patches for you. The web app is the cake: your code, its app settings, its domain and its slots."),
            leo("So the admin site and the booking API could sit on one plan?"),
            maya("They could, and it's cheaper. But they'd share the plan's workers. If the booking API needs five workers on a busy Saturday, the admin site gets five as well. Two apps that must scale differently need two plans."),
            leo("Fine. Basic tier, Always On, the morning stall is solved. But you said some bookings failed outright. That's a different crime."),
            maya("It is. The log said the database password was empty. Not wrong. Empty. The code asked for a setting, and got nothing back."),
            leo("Aha. Then the password should never have left the settings file. Put it back in the code?"),
            maya("Please don't. A secret in a file you commit can be read by anyone who sees the code. Moving it out was the right call. It now lives in an app setting: a name and a value that App Service hands your app as an environment variable when it starts. Better still is a Key Vault reference, and the Key Vault episode covers that."),
            leo("Then why is it empty? It's right there in the portal. I can see it."),
            maya("Look closely at the name. The code asks for it with capital letters. The developer typed it all in lower case. On the old Windows plan, that still worked. On Linux, a setting's name is read exactly as typed, capital letters and all. To the code, that setting doesn't exist."),
            leo("[sigh] Undone by a capital letter. So I fix the name and save. Right now, at quarter past six, in the morning rush?"),
            maya("Hold that thought. Saving an app setting restarts the app. Under the hood, settings are handed over only as the app starts, so a new value means a fresh start. Every member booking at that moment gets a hiccup. Pick a quiet minute, or better still, test it on a copy first."),
            leo("A copy? Like a second version of the app?"),
            maya("A deployment slot. A second live copy of your app on the same plan, with its own address, for testing before you swap it in. But slots start at Standard, not Basic. If a question says the cheapest tier with staging slots, it's Standard. The Deployment slots and scaling episode covers the swap itself."),
            leo("And how would a detective have seen all this sooner?"),
            maya("The logs. App Service keeps application logs, which is whatever your code prints, plus web server logs and failed request traces. Log stream shows them live. Logging to the file system switches itself off after twelve hours, so it never fills the disk. For anything long-term, Application Insights, which has its own episode."),
            leo("Is App Service always the right home, though? For everything the gym does?"),
            maya("That's the edge. It's for web apps and APIs that run all day. A job that should run only when something happens, like resizing a member's photo, fits Azure Functions better. And the phone app itself never lives on App Service, and never holds that database password. It calls the API, and the API holds it."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If two apps must scale differently, two plans. If it says the cheapest tier with slots or auto scale, Standard. If a secret sits in the code, an app setting or a Key Vault reference. If the first request after idle is slow, Always On, from Basic up. And a setting that worked on Windows but reads empty on Linux means match the case."),
            leo("And if a question says I changed a setting and the app restarted?"),
            maya("That's expected, not a bug. Settings are read as the app starts. [pause] Case closed."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case. Create a web app with the .NET 8 runtime, on Linux, on the Free tier. Add an app setting called Hello, with the value World, and save. Open Log stream and confirm the restart lines. Browse the URL to see the default page. Then delete the resource group when you're done."),
            leo("No climbers were harmed. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val appServiceGossip = Podcast(
        "az-app-service",
        listOf(
            maya("Did you hear what Sam did on the plant care team? Sam was building a small plant watering app for Android, with a backend on Azure App Service. And Sam put the database password in the backend's settings file, right in the code. Then committed it."),
            leo("[laugh] I heard there was a meeting about it. Before the drama, though, what's App Service, in plain words?"),
            maya("Azure's just run my web app service. You hand it your code, or a container, and you get a web address, HTTPS, scaling and patching, with no server to look after. Picture renting a shelf in a very well-run bakery. You bring the cake, and the bakery owns the ovens and the front door."),
            leo("So what did Sam do wrong, exactly? The password has to live somewhere."),
            maya("Anyone who can see the code can read a password in a committed file. Aisha sat with Sam and moved it into an app setting instead. That's a name and a value you add in the portal, and App Service hands it to your app as an environment variable when it starts. No code change, nothing in the repository."),
            leo("Environment variable meaning what?"),
            maya("A labelled value the computer gives a program as it starts, like a note left on your desk each morning. In .NET it just turns up in your configuration. And Aisha told Sam the even better version: a Key Vault reference, so the password sits in a vault. The Key Vault episode tells that story."),
            leo("Okay, Sam's sorted. Who's next?"),
            maya("Ben. Ben wanted to test the new watering schedule screen on a staging copy before it went live. So he went looking for deployment slots, on the Basic tier. And couldn't find them anywhere."),
            leo("A deployment slot being what?"),
            maya("A second live copy of your app, on the same plan, with its own address. You test the new version there, then swap it in. But slots start at Standard. So does auto scale. Basic gives you your own workers and custom domains, but you scale by hand, and there are no slots."),
            leo("So Ben needed to move up a tier. What are the tiers again?"),
            maya("Free and Shared for trying things out, on shared hardware. Basic. Standard, where most production apps live, with slots, auto scale and daily backups. Premium for more power and private networking. Isolated, a whole environment of your own, for strict compliance. Ben moved to Standard that afternoon."),
            leo("Hold on, did Ben change the tier on the app?"),
            maya("On the plan, which is the bit people mix up. Every app has two parts. The App Service plan is the shelf: region, Windows or Linux, tier, and how many workers. Under the picture, those workers are virtual machines Azure runs and patches for you. The web app is the cake: your code, settings, domain and slots."),
            leo("Can two apps share a shelf?"),
            maya("They can. Ben's team put a tiny admin site on the same plan, to save money. Fine, until spring, when the watering app needed six workers. The admin site got six as well, because apps on one plan share its workers. Two apps that must scale differently need two plans."),
            leo("[chuckle] An admin site with six workers for one admin. Next?"),
            maya("Dana. The watering app sends its reminders at quarter past four every afternoon. At quarter past four exactly, Dana changed an app setting in the portal, and saved."),
            leo("And?"),
            maya("And the app restarted, right as the reminders were going out. Saving an app setting always restarts the app. Under the hood, settings are only handed over as the app starts, so a new value needs a fresh start. A few hundred plants waited ten minutes for a drink. Dana now changes settings at quiet times."),
            leo("Poor plants. Was that the worst of it?"),
            maya("Not quite. Tom moved the backend from Windows to Linux. Afterwards, one setting read as empty. On Windows it had worked for a year."),
            leo("Let me guess. Something about Linux?"),
            maya("On Linux, a setting's name is read exactly as typed, capital letters and all. Tom had typed the name in lower case, and the code asked for it with capitals. To the code, it simply wasn't there. Priya spotted it in about a minute, from the log stream."),
            leo("Log stream?"),
            maya("App Service keeps application logs, which is whatever your code prints, plus web server logs and failed request traces. Log stream shows them live. Logging to the file system turns itself off after twelve hours, so it never fills the disk. For the long term, there's Application Insights, which has its own episode."),
            leo("And the intern? There's always an intern."),
            maya("The intern noticed that the first request each morning was slow, after a quiet night. So they asked for a bigger worker. Priya said no, switch on Always On instead."),
            leo("Why wouldn't a bigger worker help?"),
            maya("A bigger worker makes a running app faster. It doesn't stop an idle one being unloaded, and then having to start again. Always On keeps the app loaded by sending it a small request every few minutes, so it never counts as idle. It's offered from Basic upwards, never on Free."),
            leo("Does the phone app live on App Service too?"),
            maya("No, and that's the edge. App Service hosts web apps and APIs that run all day. The phone app runs on the phone, calls the API, and never holds the password. And code that should run only when something happens, like a photo of a sad plant arriving, belongs in Azure Functions."),
            leo("So, the office lessons for the exam?"),
            maya("Four things. If two apps must scale differently, two plans. If it asks for the cheapest tier with slots or auto scale, Standard. If a secret sits in the code, move it to an app setting. And if the first request after idle is slow, Always On. Oh, and a saved setting that restarts the app is expected, not a bug."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a web app with the .NET 8 runtime, on Linux, on the Free tier. Add an app setting called Hello, with the value World, and save. Open Log stream and confirm the restart lines. Browse the URL to see the default page, and delete the resource group when done."),
            leo("Hello World, at a quiet time. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    private val slotsCase = Podcast(
        "az-slots-scaling",
        listOf(
            maya("Welcome to Case Files. Today's case: The Phantom Bikes. A small bike rental company runs an Android app, with a backend on Azure App Service. On Friday evening they released a new version. On Saturday morning, riders saw bikes on the map that don't exist. And some riders lost their booking halfway through paying."),
            leo("[chuckle] Ghost bikes. Detective Leo is pedalling. Where do we start?"),
            maya("With how they release. The backend has a deployment slot called staging. A slot is a second live copy of your app, with its own web address, on the same plan. They deploy the new version there, test it, then swap it into production."),
            leo("Swap, meaning they copy the files across? Then my first suspect is a bad copy. Some files got mixed up."),
            maya("[pause] Wrong suspect, and a common one. A swap copies no files at all. It changes the routing. Azure first warms up the staging slot by sending it requests, then switches which workers answer the production address. The staging workers, already warm, start serving production. The old version moves over to staging."),
            leo("So it's like moving a signpost, not moving the shops."),
            maya("Exactly. Which is why you can swap back in seconds, with no redeploy. Now, the clue. The phantom bikes come from a test database. Staging is wired to a test database full of made-up bikes. After the swap, production was reading from it."),
            leo("So the swap took the test connection string with it? Why would it do that?"),
            maya("Because most settings travel with the code. General app settings and connection strings move in a swap. Unless you mark one as a deployment slot setting. Then it's a sticky setting, and it stays with its slot. Production keeps the real database, staging keeps the test one."),
            leo("But the developer told me they did mark it sticky. I saw the lock icon myself."),
            maya("When did they mark it, detective?"),
            leo("[sigh] Saturday morning. After the swap."),
            maya("That cracks the first half. Mark it before. Marked afterwards, the first swap has already carried the staging value across. And some things always stay put, by the way: custom domain names, scale settings, IP restrictions and Always On."),
            leo("Okay, phantom bikes explained. What about the lost bookings? Same culprit?"),
            maya("Different culprit. Saturday was sunny, so traffic tripled, and auto scale added workers. A worker is one machine running a copy of your app. Each booking took the same work as before, there were just more of them, so scale out, meaning more workers, was the right move."),
            leo("Then my suspect is auto scale itself. It's broken. Turn it off."),
            maya("Auto scale did its job. The backend kept each rider's half-finished booking in its own memory. And each worker has its own memory. A rider's first request hit worker one, the next hit worker two, which had never heard of them. Booking gone."),
            leo("So keep the booking somewhere every worker can see?"),
            maya("Yes. Somewhere shared, like a cache or the database. The Redis cache episode covers the shared kind. That's the edge of scale out: it only works when the app is stateless, or shares its state."),
            leo("And scale up? When would that be the answer instead?"),
            maya("When each request got heavier. Scale up means a bigger worker, with more processor and memory, usually a higher pricing tier, and a short restart. If the bike app started drawing a route map on every request, a bigger worker. More riders doing the same thing, more workers."),
            leo("Was there a third clue? There's always a third clue."),
            maya("There was. The worker count kept bouncing all afternoon. Three, four, three, four. Their scale rule said add a worker above sixty percent processor, and remove one below sixty."),
            leo("Same number both ways. So it flaps, like a door in the wind?"),
            maya("Exactly. A scale rule is a condition that adds or removes workers. Leave a gap, say out at seventy and in at thirty, and set a cool-down, the least time between two scale actions. Plus a minimum and a maximum. Auto scale starts at the Standard tier."),
            leo("Does the rule go on the app?"),
            maya("On the plan, because the plan owns the workers. Under the hood, Azure Monitor watches the plan's numbers and changes how many workers it has. And when you know the rush is coming, like every sunny weekend morning, a schedule rule adds workers before it starts, cheaper than reacting after."),
            leo("One more question. Could they have caught the phantom bikes before everyone saw them?"),
            maya("Two ways. Traffic percent sends, say, ten percent of real riders to staging, and a cookie keeps each one there. If failed requests stay flat in Application Insights, set it back to zero and swap. And auto swap swaps as soon as a deployment to the slot finishes. It waits for the warm-up, so nobody meets a cold slot."),
            leo("And where does all this stop working?"),
            maya("Slots belong to App Service, from Standard up. On Basic there are none, and you scale by hand. And everything we said lives in one region. Sending riders worldwide to their nearest copy is a job for Front Door, which has its own episode. The App Service episode covers plans and tiers."),
            leo("So, the case summary for the exam?"),
            maya("Four rules. If production must keep its own database after a swap, mark that connection string as a slot setting, before the swap. If it must swap as soon as a deployment finishes, auto swap. If traffic doubles with the same work per request, scale out. And if the rule keeps flapping, add a cool-down and a gap between the thresholds."),
            leo("And what does a swap really change?"),
            maya("The routing, not the files. The warmed staging copy answers production, and you can swap back. Case closed."),
            leo("Case closed. Anything to try today?"),
            maya("You'll need a Standard plan, which the free credit covers for an hour. Add a slot named staging to a web app. Change the app title in staging only. Swap, and confirm production shows the new title. Then swap back, and delete the plan."),
            leo("Swap, check, swap back. No phantoms. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val slotsGossip = Podcast(
        "az-slots-scaling",
        listOf(
            maya("Did you hear what happened on quiz night? Nadia's team runs a trivia quiz app for Android, with a backend on Azure App Service. Thursday night, two thousand players, and half of them got sent back to the start screen, mid-quiz."),
            leo("[laugh] I heard about the start screen. What happened?"),
            maya("Nadia saw the crowd coming and scaled out. That means more workers behind the same web address, on the same pricing tier. A worker is one machine running a copy of your app. Good call. But the backend kept each player's progress in its own memory."),
            leo("And each worker has its own memory, I'm guessing."),
            maya("Exactly. A player's answer to question three hit worker one, and question four hit worker two, which had never seen them. Back to the start. Omar fixed it by keeping progress in a shared cache, so every worker sees the same thing. The Redis cache episode covers that bit."),
            leo("So scale out needs the app to keep its state somewhere shared. What's scale up, then?"),
            maya("A bigger worker. More processor and memory, usually a higher tier, and a short restart. If each request got heavier, scale up. If there are just more of the same requests, like quiz night, scale out. Say a photo filter app starts editing huge images on every request: that's a bigger worker."),
            leo("Got it. Who's next?"),
            maya("Ben. Ben set up auto scale for quiz night, which you get from the Standard tier. His scale rule said add a worker above fifty percent processor, and remove one below fifty."),
            leo("Same number both ways. [pause] That sounds bad."),
            maya("It flapped all night. Out, in, out, in, like a door in the wind. A scale rule is a condition that adds or removes workers. Priya told him to leave a gap, out at seventy and in at thirty, and to set a cool-down, the least time between two scale actions. And a minimum and a maximum, always."),
            leo("Did Ben put the rule on the app?"),
            maya("He tried. Rules live on the plan, because the plan owns the workers. Under the hood, Azure Monitor watches the plan's numbers and changes its worker count. And Priya's best tip: quiz night is every Thursday at eight. A schedule rule adds workers at half past seven, before the rush, cheaper than reacting after."),
            leo("Sensible Priya. Now, I heard something about questions from the test database?"),
            maya("[chuckle] That was Omar. The team ships with a deployment slot. A slot is a second live copy of the app, with its own address. Staging is the rehearsal room, production is the stage. Omar deployed the new version to staging, tested it, and swapped."),
            leo("Swapped meaning what, really?"),
            maya("A swap copies no files. Azure warms up staging by sending it requests, then switches the routing, so the warmed staging workers answer the production address. The old version moves to staging. Under the picture, it's a signpost moving, not the shops."),
            leo("Then how did test questions reach real players?"),
            maya("Staging pointed at a test database full of silly questions, like what colour is the colour blue. Most settings travel with the code in a swap, connection strings included. So the test database came along. Unless you mark the setting as a deployment slot setting. Then it's a sticky setting, and it stays with its slot."),
            leo("So Omar never marked it as sticky?"),
            maya("He marked it. After the swap. By then, the first swap had already carried the staging value across. Mark it before. And domain names, scale settings, IP restrictions and Always On stay with the slot anyway."),
            leo("[sigh] Poor Omar. Did he at least swap back?"),
            maya("In seconds. That's the beauty of it. The old version sits in staging, so you swap again, with no redeploy. Old code to staging, new code to production, and back again with one click. And then there's the intern."),
            leo("Oh no. What did the intern do?"),
            maya("The intern deployed to staging and hit swap straight away, before the warm-up had finished. Players met a cold copy for the first minute. Priya showed them auto swap. It swaps automatically as soon as a deployment to the slot finishes, and it waits for the warm-up first."),
            leo("And can you try a new version on only some players?"),
            maya("Traffic percent. Set it to ten on staging, and one player in ten lands on the new version, with a cookie that keeps them there. If failed requests stay flat in Application Insights, set it back to zero and swap. Aisha does that every release now."),
            leo("Does all this work on any App Service tier?"),
            maya("That's the edge. Slots and auto scale begin at Standard. On Basic there are no slots, and scaling is by hand. And all of this is one region. If the quiz goes worldwide and players need their nearest copy, Front Door does that routing, and it has its own episode. The App Service episode covers tiers and plans."),
            leo("So, the gossip to remember for the exam?"),
            maya("Four things. If production must keep its own database after a swap, mark it as a slot setting, before swapping. If it must swap the moment a deployment finishes, auto swap. If traffic doubles with the same work per request, scale out. And if the rule keeps flapping, add a cool-down and a gap."),
            leo("And what does a swap really change?"),
            maya("The routing, not the files. And auto scale rules sit on the plan, not the app, because the plan owns the workers."),
            leo("And something to try today, so I'm not on next week's list?"),
            maya("You'll need a Standard plan, which the free credit covers for an hour. Add a slot named staging to a web app. Change the app title in staging only. Swap, and confirm production shows the new title. Swap back, then delete the plan."),
            leo("Quiz question: what moves in a swap? The routing. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    private val functionsCase = Podcast(
        "az-functions",
        listOf(
            maya("Welcome to Case Files. Today's case: The Reminder That Never Rang. A small habit tracker app for Android nudges its users every morning at seven: drink water, go for a walk. Behind it sits an Azure function on a timer. For three days now, nobody has been nudged. Not one reminder."),
            leo("[chuckle] A habit tracker that's lost the habit. Detective Leo is on it. First, what's a function, in plain words?"),
            maya("A small piece of code that runs only when something happens. You pay per run, and there's no server to manage. When nothing happens, nothing runs. Under the picture, Azure keeps a host, a small runtime that listens for events and starts your code when one arrives."),
            leo("And what decides when it runs?"),
            maya("Its trigger. A trigger is the one event that starts the function, like a doorbell. Every function has exactly one. HTTP for a web request, a timer for a schedule, a queue message, a new blob, a Cosmos DB change, or messages from Event Grid, Event Hubs or Service Bus. Here, it's a timer."),
            leo("Then my first suspect is the schedule. Someone wrote it wrong. Isn't the format weird?"),
            maya("Good instinct, so we check. The timer uses NCRONTAB, a CRON format with six fields, not the usual five. Seconds, minutes, hours, day, month, weekday. Seven every morning is zero, zero, seven, then three stars. [pause] And that's exactly what's in the code. Suspect cleared."),
            leo("Okay. Suspect two. It's on the Consumption plan, it took too long, and it got killed."),
            maya("Closer, because that did happen here once, just not to this function. A month ago, the app's weekly report function ran for twenty minutes on Consumption. Consumption stops a single run at ten minutes, five by default. So the report died every Sunday."),
            leo("What is Consumption, exactly?"),
            maya("The pay per run plan. It scales to zero when idle, so you pay nothing while it's quiet. The price is a cold start, a delay while Azure loads your function after a quiet spell, like starting a cold car. Plus that ten-minute cap, and no private network."),
            leo("So how did they fix the long report?"),
            maya("That's the clue. To save money, they moved the whole function app onto the App Service plan their backend already used. That's the Dedicated plan. No ten-minute cap there, and the cost is the plan, not the runs. The report started working. And three days later, the reminders stopped."),
            leo("Wait. So the move broke the timer? How?"),
            maya("On Dedicated, the host goes to sleep when it's idle, unless Always On is switched on. Between reminders, the app sat idle for hours, so it slept. A sleeping host can't hear the alarm clock. Turn on Always On, and the seven o'clock timer fires again."),
            leo("[laugh] Asleep on the job, literally. Case closed, then?"),
            maya("Not yet. The developer's first panic fix was to change the schedule to every five minutes, just to see something happen. They put the star slash five at the very start."),
            leo("Which is the seconds field. So, every five seconds?"),
            maya("Every five seconds. Thousands of nudges a day, once Always On came back. Every five minutes is zero in the seconds field, then star slash five in the minutes. Count the fields. Six, and the first is seconds."),
            leo("Ouch. Were there other clues in the file?"),
            maya("One more, in the repository. The local settings file was committed. It holds settings for running on your laptop, and it's never deployed, but this one held the storage account's connection string. Anyone reading the repository could use it."),
            leo("So where should those settings live in Azure?"),
            maya("In app settings, under the same keys. Secrets, better still, in Key Vault, and the Key Vault episode covers that. The host file is different. It sets things for the whole function app, like logging and time-outs, and it is deployed. One storage setting is required, because the runtime keeps its timer locks in a storage account."),
            leo("A function app being the whole box, not one function?"),
            maya("Right. A function app is the box you deploy. It holds many functions sharing one plan, runtime and settings. Here, the morning reminder, the weekly report, and a queue-triggered sender. Each still has its own single trigger."),
            leo("If the reminder reads users from a database and writes messages to a queue, isn't that three triggers?"),
            maya("One trigger, the timer. The read is an input binding, and the write is an output binding. A binding is a ready-made connection to another service, so you write no connection code. You return a value, and the output binding drops it on the queue."),
            leo("Like plugs instead of wiring. And where does all this stop working?"),
            maya("At the edges of the plans. Must never cold start, or needs a private network? Premium, which keeps instances pre-warmed. A run longer than ten minutes rules out Consumption. And a workflow that waits for hours or days, say a thirty-day habit challenge, belongs in Durable Functions. The Durable Functions episode covers that."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If it asks how many triggers, one. If it must not have a cold start, or needs a private network, Premium. If it's the cheapest for a few runs a day, Consumption. If a job runs for thirty minutes, rule out Consumption. And a timer on Dedicated that never fires means Always On."),
            leo("And a question that shows a schedule?"),
            maya("Count the fields. Six, seconds first, that's NCRONTAB. Case closed."),
            leo("Case closed. Anything to try today?"),
            maya("Create a Consumption function app in the portal. Add an HTTP trigger function from the template, and call it from the browser with your name. Then add a timer function that runs every minute, and watch Monitor show the runs. Delete the resource group when you're done."),
            leo("One reminder a minute. I'll try to keep the habit. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val functionsGossip = Podcast(
        "az-functions",
        listOf(
            maya("Did you hear what Lena did with the dog walking app? It's a little Android app where walkers post a photo of each dog after the walk, so owners know they're happy. Lena wrote a timer function for the evening summary, and the summary went out at all the wrong times."),
            leo("[laugh] Owners getting summaries at three in the morning. Before the gossip, what's a function?"),
            maya("A small piece of code that runs only when something happens. You pay per run, with no server to manage. Under the picture, Azure runs a host, a small runtime that listens for events and starts your code when one arrives. When nothing happens, nothing runs. A walker uploads a photo at noon, the function wakes, does its job, and stops."),
            leo("And what was wrong with Lena's schedule?"),
            maya("Azure's timer uses NCRONTAB, a CRON format with six fields, not five, and the first is seconds. Seconds, minutes, hours, day, month, weekday. Lena wrote five, like most tutorials, so the schedule didn't mean what she thought. Aisha counted them for her: six, seconds first."),
            leo("So every five minutes would be what?"),
            maya("Zero in the seconds, then star slash five in the minutes, then stars. Put the star slash five first and it's every five seconds. Which, by the way, is exactly what the intern did on Tuesday."),
            leo("[chuckle] Of course. What else happened?"),
            maya("Raj. The monthly invoice function for dog owners runs for about twenty minutes. Raj put it on the Consumption plan, and it was stopped at ten minutes, every single month. Half the owners got an invoice, and Raj got a lot of emails."),
            leo("What's the Consumption plan?"),
            maya("Pay per run. It scales to zero, so a quiet app costs almost nothing. The catch is a cold start, a delay while Azure loads your function after a quiet spell, like a cold car on a winter morning. And a single run is capped at ten minutes, five by default, with no private network."),
            leo("So what did Raj do?"),
            maya("Priya gave him two choices. Premium, which allows sixty minutes by default, or longer if you set it. Or Durable Functions, which splits long work into steps that can wait and pick up again. The Durable Functions episode tells that story."),
            leo("And Premium is just Consumption, but more?"),
            maya("Premium keeps instances pre-warmed, so there's no cold start, and it can join a private network. But you pay even when it's idle, for at least one instance. The booking function, which must answer an owner instantly, went on Premium. Flex Consumption sits in between: still pay per run, with a few always-ready instances to soften cold starts."),
            leo("Okay. Who's next?"),
            maya("The intern, again. They committed the local settings file to the repository. That file is for running functions on your laptop, it's never deployed, and theirs held the storage connection string."),
            leo("[sigh] Where should those settings live?"),
            maya("In Azure, the same keys live in app settings. Secrets, better still, in Key Vault, which has its own episode. Ben replaced the storage key that afternoon. And the host file is the other one. It sets things for the whole function app, like logging and time-outs, and that one is deployed."),
            leo("Function app meaning the whole box?"),
            maya("Right. A function app is the box you deploy: many functions sharing one plan, runtime and settings. The dog app has three. The photo one, the evening summary, and the sender. Each has its own single trigger."),
            leo("The photo one. Tell me about that."),
            maya("When a walker uploads a photo, it lands in Blob Storage, and a blob trigger starts a function that makes a thumbnail. A trigger is the one event that starts a function. Tom wanted the same function to start on a timer as well. Two triggers. Not allowed. Every function has exactly one."),
            leo("But it reads the photo and writes the thumbnail. Aren't those triggers too?"),
            maya("That's what Tom asked. No, those are bindings. A binding is a ready-made connection to another service, so there's no connection code. An input binding hands you data, an output binding writes what you return. The photo arrives by trigger, and the thumbnail leaves by an output binding."),
            leo("And the last bit of gossip?"),
            maya("Chris. To save money, Chris moved the evening summary onto the App Service plan the main backend already uses. That's the Dedicated plan, where the cost is the plan, not the runs. And the summary stopped firing."),
            leo("Let me guess. The host fell asleep?"),
            maya("On Dedicated, the host sleeps when it's idle, unless Always On is switched on. A sleeping host can't hear the timer. Chris switched on Always On, and the evening summary came back. Under the hood, Always On keeps sending the app a small request, so it never counts as idle."),
            leo("Does the phone app run functions itself?"),
            maya("No, that's the edge. Functions run in Azure. The phone uploads the photo, or calls an HTTP trigger, and the function does the rest. And for an API that's busy all day, a plain App Service backend is often simpler. The App Service episode covers that."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. If it asks how many triggers, one. If it must not have a cold start, or needs a private network, Premium. If it's the cheapest for a few runs a day, Consumption. And if a job runs for thirty minutes, rule out Consumption. A schedule question? Count the fields: six, seconds first."),
            leo("And something to try today, so I'm not Tom?"),
            maya("Create a Consumption function app in the portal. Add an HTTP trigger function from the template, and call it from the browser with your name. Add a timer function that runs every minute, and watch Monitor show the runs. Then delete the resource group."),
            leo("One trigger, six fields. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    val podcasts: List<Podcast> = listOf(appServiceCase, appServiceGossip, slotsCase, slotsGossip, functionsCase, functionsGossip)

    private fun appServiceCaseBoard() = PodcastBoard(
        "az-app-service--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Six O'Clock Stall..."
            Scene(
                0, "The Six O'Clock Stall",
                DiagramSpec(
                    "The gym's Android app calls its booking API on App Service; at six on Monday the first bookings stall, then some fail.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("api", "Booking API", 0.22f, 0.66f, w = 0.34f, h = 0.2f, sub = "App Service"),
                        DNode("err", "Stalls, then fails", 0.76f, 0.2f, w = 0.42f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("svc", "App Service", 0.76f, 0.66f, w = 0.4f, h = 0.3f, kind = NodeKind.CLOUD, sub = "URL, HTTPS"),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "book"),
                        DEdge("api", "err", dashed = true),
                        DEdge("api", "svc", "runs on"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "err"), light = setOf("err"), pulse = "phone" to "api", card = BoardCard("The case", "Monday, six a.m.: the first bookings stall, then a few fail.")),
                    Beat(2, show = setOf("svc"), light = setOf("svc"), pulse = "api" to "svc", card = BoardCard("App Service", "Hand it your code; get a URL, HTTPS, scaling and patching. No server.")),
                    Beat(4, light = setOf("api"), card = BoardCard("The weekend before", "Moved to a Linux plan on Free; the password moved out of the code.")),
                ),
            ),
            // 5: "Then my first suspect is the database..."
            Scene(
                5, "The suspects",
                DiagramSpec(
                    "Neither suspect fits: the database answered the admin site, and a bigger worker cannot wake a sleeping app.",
                    nodes = listOf(
                        DNode("db", "Database down?", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "admin site was fine"),
                        DNode("big", "A bigger worker?", 0.76f, 0.2f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "doesn't wake it"),
                        DNode("first", "Only the first request", 0.5f, 0.74f, w = 0.66f, h = 0.22f, sub = "after a quiet night"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("db"), light = setOf("db")),
                    Beat(6, card = BoardCard("Suspect one: cleared", "The admin site used the same database and answered instantly.")),
                    Beat(7, show = setOf("big"), light = setOf("big")),
                    Beat(8, show = setOf("first"), light = setOf("first"), card = BoardCard("Suspect two: cleared", "Bigger makes a running app faster. It doesn't wake a sleeping one.")),
                ),
            ),
            // 9: "So the app was asleep. How do you keep it awake?"
            Scene(
                9, "Always On",
                DiagramSpec(
                    "An idle app is unloaded and must start again; Always On, from Basic up, keeps it loaded.",
                    nodes = listOf(
                        DNode("user", "First member", 0.14f, 0.3f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("app", "Sleeping API", 0.72f, 0.3f, w = 0.4f, h = 0.2f, tone = 2),
                        DNode("on", "Always On", 0.72f, 0.78f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "Basic and above"),
                        DNode("free", "Free: not offered", 0.26f, 0.78f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("user", "app", "slow start"),
                        DEdge("on", "app", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(9, show = setOf("user", "app"), pulse = "user" to "app"),
                    Beat(10, show = setOf("on", "free"), light = setOf("on", "free"), pulse = "on" to "app", card = BoardCard("Always On", "A small request every few minutes, so the app is never unloaded.")),
                ),
            ),
            // 11: "A tier is a price level, right?..."
            Scene(
                11, "Pricing tiers",
                DiagramSpec(
                    "The pricing tiers from Free to Isolated; Always On starts at Basic, slots and auto scale at Standard.",
                    nodes = listOf(
                        DNode("free", "Free, Shared", 0.3f, 0.1f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 2, sub = "trying things out"),
                        DNode("basic", "Basic", 0.3f, 0.3f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "scale by hand"),
                        DNode("std", "Standard", 0.3f, 0.5f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, sub = "most real apps"),
                        DNode("prem", "Premium", 0.3f, 0.7f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "more power"),
                        DNode("iso", "Isolated", 0.3f, 0.9f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 2, sub = "your own"),
                        DNode("on", "Always On", 0.8f, 0.3f, w = 0.36f, h = 0.16f, kind = NodeKind.NOTE, sub = "from here"),
                    ),
                    edges = listOf(
                        DEdge("on", "basic"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("free", "basic", "std", "prem", "iso", "on"), light = setOf("basic", "on"), pulse = "on" to "basic", card = BoardCard("A tier is a price level", "Basic: own workers, Always On. Standard: auto scale, slots, backups.")),
                ),
            ),
            // 13: "Wait, where does the tier live? On the app?"
            Scene(
                13, "The shelf and the cake",
                DiagramSpec(
                    "Every app has two parts: the App Service plan underneath, and the web app that runs on it.",
                    nodes = listOf(
                        DNode("app", "Web app", 0.5f, 0.16f, w = 0.44f, h = 0.2f, sub = "the cake: your code"),
                        DNode("w1", "Worker", 0.2f, 0.5f, w = 0.24f, h = 0.14f, tone = 2),
                        DNode("w2", "Worker", 0.5f, 0.5f, w = 0.24f, h = 0.14f, tone = 2),
                        DNode("w3", "Worker", 0.8f, 0.5f, w = 0.24f, h = 0.14f, tone = 2),
                        DNode("plan", "App Service plan", 0.5f, 0.84f, w = 0.9f, h = 0.2f, kind = NodeKind.PILL, tone = 1, sub = "region, OS, tier, workers"),
                    ),
                    edges = listOf(
                        DEdge("app", "w2"),
                        DEdge("plan", "w2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("plan", "w1", "w2", "w3", "app"), light = setOf("plan"), pulse = "plan" to "w2", card = BoardCard("App Service plan", "The shelf: region, OS, tier, workers. The workers are VMs Azure runs.")),
                    Beat(16, light = setOf("plan", "app"), pulse = "app" to "w2", card = BoardCard("Apps on one plan share workers", "Two apps that must scale differently need two plans.")),
                ),
            ),
            // 17: "Fine. Basic tier, Always On, the morning stall is solved..."
            Scene(
                17, "The empty setting",
                DiagramSpec(
                    "An app setting is handed to the app as an environment variable at start-up; on Linux its name must match exactly.",
                    nodes = listOf(
                        DNode("set", "App setting", 0.24f, 0.22f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "typed in lower case"),
                        DNode("env", "Environment variable", 0.5f, 0.58f, w = 0.5f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                        DNode("app", "Booking API", 0.76f, 0.22f, w = 0.38f, h = 0.2f, sub = "asks with capitals"),
                        DNode("restart", "Save means restart", 0.5f, 0.88f, w = 0.5f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("set", "env"),
                        DEdge("env", "app", "at start-up"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("app"), light = setOf("app"), card = BoardCard("Empty, not wrong", "The code asked for a setting and got nothing back.")),
                    Beat(20, show = setOf("set", "env"), light = setOf("set", "env"), pulse = "env" to "app", card = BoardCard("App setting", "A name and a value, handed over as an environment variable at start-up.")),
                    Beat(22, light = setOf("set", "app"), card = BoardCard("Linux reads names exactly", "Lower case in the portal, capitals in the code: not the same setting.")),
                    Beat(24, show = setOf("restart"), light = setOf("restart"), card = BoardCard("Saving restarts the app", "Settings are handed over at start-up. Pick a quiet minute.")),
                ),
            ),
            // 25: "A copy? Like a second version of the app?"
            Scene(
                25, "A copy to test on",
                DiagramSpec(
                    "A deployment slot is a second live copy on the same plan; slots begin at the Standard tier.",
                    nodes = listOf(
                        DNode("prod", "Production", 0.7f, 0.26f, w = 0.44f, h = 0.2f),
                        DNode("stage", "Staging slot", 0.7f, 0.74f, w = 0.44f, h = 0.2f, tone = 1),
                        DNode("std", "Standard and up", 0.22f, 0.5f, w = 0.38f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("prod", "stage", "swap", dashed = true, bidirectional = true),
                        DEdge("std", "stage", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("prod")),
                    Beat(26, show = setOf("stage", "std"), light = setOf("stage", "std"), pulse = "stage" to "prod", card = BoardCard("Deployment slot", "A second live copy on the same plan. Cheapest tier with slots: Standard.")),
                ),
            ),
            // 27: "And how would a detective have seen all this sooner?"
            Scene(
                27, "The logs",
                DiagramSpec(
                    "App Service keeps application logs, web server logs and failed request traces; log stream shows them live.",
                    nodes = listOf(
                        DNode("app", "Booking API", 0.22f, 0.3f, w = 0.36f, h = 0.2f),
                        DNode("logs", "Logs", 0.76f, 0.3f, w = 0.32f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("stream", "Log stream", 0.5f, 0.8f, w = 0.4f, kind = NodeKind.PILL, sub = "watch live"),
                    ),
                    edges = listOf(
                        DEdge("app", "logs", "prints"),
                        DEdge("logs", "stream"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("app")),
                    Beat(28, show = setOf("logs", "stream"), light = setOf("logs", "stream"), pulse = "app" to "logs", card = BoardCard("File system logging", "Switches itself off after twelve hours. Long-term: Application Insights.")),
                ),
            ),
            // 29: "Is App Service always the right home, though?..."
            Scene(
                29, "Where it stops",
                DiagramSpec(
                    "App Service hosts APIs that run all day; event-driven jobs suit Functions, and the phone only calls the API.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.24f, 0.2f, w = 0.4f, h = 0.2f, sub = "no password"),
                        DNode("api", "Booking API", 0.24f, 0.7f, w = 0.36f, h = 0.2f, sub = "App Service"),
                        DNode("fn", "Azure Functions", 0.76f, 0.2f, w = 0.42f, h = 0.2f, tone = 1, sub = "on an event"),
                        DNode("db", "Database", 0.78f, 0.7f, w = 0.3f, h = 0.28f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "calls"),
                        DEdge("api", "db", "holds the password"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("phone", "api", "db"), pulse = "phone" to "api"),
                    Beat(30, show = setOf("fn"), light = setOf("fn", "phone"), pulse = "api" to "db", card = BoardCard("Past the edge", "On an event: Functions. The phone never holds the password; the API does.")),
                ),
            ),
            // 31: "So, the case summary for the exam?"
            Scene(
                31, "Case closed",
                DiagramSpec(
                    "The exam's App Service questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("scale", "Scale differently", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "two plans"),
                        DNode("slots", "Cheapest with slots", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Standard"),
                        DNode("secret", "Secret in the code", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "app setting"),
                        DNode("idle", "Slow after idle", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Always On"),
                        DNode("linux", "Empty on Linux", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "match the case"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("scale", "slots", "secret", "idle", "linux"), light = setOf("linux"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(34, card = BoardCard("Saved a setting?", "Expected: settings are read at start-up, so the app restarts.")),
                    Beat(36, card = BoardCard("Today", "Free Linux web app, Hello=World, watch Log stream, browse, delete.")),
                ),
            ),
        ),
    )

    private fun appServiceGossipBoard() = PodcastBoard(
        "az-app-service--gossip",
        listOf(
            // 0: "Did you hear what Sam did on the plant care team?..."
            Scene(
                0, "What Sam did",
                DiagramSpec(
                    "Sam's database password sat in the backend's settings file, and the file was committed for anyone to read.",
                    nodes = listOf(
                        DNode("code", "Settings file", 0.24f, 0.3f, w = 0.42f, h = 0.2f, sub = "the password inside"),
                        DNode("repo", "Committed", 0.76f, 0.3f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE),
                        DNode("readers", "Anyone with the code", 0.7f, 0.78f, w = 0.5f, h = 0.18f, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("code", "repo", "committed"),
                        DEdge("readers", "repo", "read"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("code", "repo"), light = setOf("code"), pulse = "code" to "repo", card = BoardCard("The password in the code", "A plant watering app's settings file, committed.")),
                    Beat(2, card = BoardCard("App Service", "Your code in; a URL, HTTPS, scaling and patching out. No server.")),
                    Beat(4, show = setOf("readers"), light = setOf("readers"), pulse = "readers" to "repo", card = BoardCard("Anyone can read it", "Aisha moved it into an app setting instead.")),
                ),
            ),
            // 5: "Environment variable meaning what?"
            Scene(
                5, "App settings",
                DiagramSpec(
                    "An app setting is given to the app as an environment variable at start-up, with no code change.",
                    nodes = listOf(
                        DNode("set", "App setting", 0.24f, 0.22f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "a name and a value"),
                        DNode("env", "Environment variable", 0.5f, 0.58f, w = 0.5f, h = 0.18f, kind = NodeKind.PILL, tone = 1),
                        DNode("app", "Watering app", 0.76f, 0.22f, w = 0.38f, h = 0.2f),
                        DNode("kv", "Key Vault reference", 0.5f, 0.88f, w = 0.56f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("set", "env"),
                        DEdge("env", "app", "at start-up"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("set", "env", "app"), light = setOf("env"), pulse = "env" to "app", card = BoardCard("Environment variable", "A labelled value given to the app as it starts, like a note on your desk.")),
                    Beat(6, show = setOf("kv"), light = setOf("kv"), card = BoardCard("Even better", "A Key Vault reference: the password sits in the vault.")),
                ),
            ),
            // 8: "Ben. Ben wanted to test the new watering schedule screen..."
            Scene(
                8, "Ben's missing slots",
                DiagramSpec(
                    "The pricing tiers from Free to Isolated; slots and auto scale begin at Standard, not Basic.",
                    nodes = listOf(
                        DNode("free", "Free, Shared", 0.3f, 0.1f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 2, sub = "trying things out"),
                        DNode("basic", "Basic", 0.3f, 0.3f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "scale by hand"),
                        DNode("std", "Standard", 0.3f, 0.5f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, sub = "most real apps"),
                        DNode("prem", "Premium", 0.3f, 0.7f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "more power"),
                        DNode("iso", "Isolated", 0.3f, 0.9f, w = 0.5f, h = 0.14f, kind = NodeKind.PILL, tone = 2, sub = "your own"),
                        DNode("slots", "Slots, auto scale", 0.8f, 0.5f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "start here"),
                    ),
                    edges = listOf(
                        DEdge("slots", "std"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("free", "basic", "std"), light = setOf("basic"), card = BoardCard("Slots on Basic?", "Not there. Basic gives your own workers, scaled by hand.")),
                    Beat(10, show = setOf("slots"), light = setOf("std", "slots"), pulse = "slots" to "std", card = BoardCard("Deployment slot", "A second live copy with its own address, to test and then swap in.")),
                    Beat(12, show = setOf("prem", "iso"), light = setOf("std"), card = BoardCard("Ben moved to Standard", "Where most production apps live: slots, auto scale, daily backups.")),
                ),
            ),
            // 13: "Hold on, did Ben change the tier on the app?"
            Scene(
                13, "The shelf and the cake",
                DiagramSpec(
                    "Every app has two parts: the App Service plan underneath, and the web app that runs on it.",
                    nodes = listOf(
                        DNode("app", "Web app", 0.5f, 0.16f, w = 0.44f, h = 0.2f, sub = "the cake: your code"),
                        DNode("w1", "Worker", 0.2f, 0.5f, w = 0.24f, h = 0.14f, tone = 2),
                        DNode("w2", "Worker", 0.5f, 0.5f, w = 0.24f, h = 0.14f, tone = 2),
                        DNode("w3", "Worker", 0.8f, 0.5f, w = 0.24f, h = 0.14f, tone = 2),
                        DNode("plan", "App Service plan", 0.5f, 0.84f, w = 0.9f, h = 0.2f, kind = NodeKind.PILL, tone = 1, sub = "region, OS, tier, workers"),
                    ),
                    edges = listOf(
                        DEdge("app", "w2"),
                        DEdge("plan", "w2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("plan", "w1", "w2", "w3", "app"), light = setOf("plan"), pulse = "plan" to "w2", card = BoardCard("The tier lives on the plan", "Region, OS, tier, workers. The workers are VMs Azure runs for you.")),
                ),
            ),
            // 15: "Can two apps share a shelf?"
            Scene(
                15, "Sharing a plan",
                DiagramSpec(
                    "Apps on one plan share its workers, so apps that must scale differently need separate plans.",
                    nodes = listOf(
                        DNode("store", "Watering app", 0.25f, 0.18f, w = 0.4f, h = 0.2f),
                        DNode("admin", "Admin site", 0.75f, 0.18f, w = 0.36f, h = 0.2f, tone = 1),
                        DNode("plan", "One plan", 0.5f, 0.56f, w = 0.6f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "six shared workers"),
                        DNode("two", "Grow differently? Two plans", 0.5f, 0.88f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("store", "plan"),
                        DEdge("admin", "plan"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(15, show = setOf("store", "admin")),
                    Beat(16, show = setOf("plan", "two"), light = setOf("admin", "two"), pulse = "admin" to "plan", card = BoardCard("Six workers for one admin", "Apps on one plan share its workers. Scale differently? Two plans.")),
                ),
            ),
            // 18: "Dana. The watering app sends its reminders at quarter past four..."
            Scene(
                18, "Dana's quarter past four",
                DiagramSpec(
                    "Saving an app setting restarts the app, because settings are handed over only as it starts.",
                    nodes = listOf(
                        DNode("set", "Setting saved", 0.24f, 0.26f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE),
                        DNode("app", "Watering app", 0.76f, 0.26f, w = 0.4f, h = 0.2f, sub = "restarts"),
                        DNode("remind", "4:15 reminders", 0.5f, 0.78f, w = 0.5f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("set", "app", "restart"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("remind", "set"), light = setOf("remind"), card = BoardCard("Quarter past four", "Dana saved a setting right as the reminders went out.")),
                    Beat(20, show = setOf("app"), light = setOf("app"), pulse = "set" to "app", card = BoardCard("Saving restarts the app", "Settings are handed over at start-up. Change them at quiet times.")),
                ),
            ),
            // 22: "Not quite. Tom moved the backend from Windows to Linux..."
            Scene(
                22, "Tom's move to Linux",
                DiagramSpec(
                    "On Linux a setting's name is read exactly as typed, so lower case in the portal misses capitals in the code.",
                    nodes = listOf(
                        DNode("portal", "In the portal", 0.26f, 0.26f, w = 0.42f, h = 0.2f, kind = NodeKind.NOTE, sub = "lower case"),
                        DNode("code", "In the code", 0.76f, 0.26f, w = 0.4f, h = 0.2f, sub = "capitals"),
                        DNode("stream", "Log stream", 0.5f, 0.78f, w = 0.4f, kind = NodeKind.PILL, sub = "watch live"),
                    ),
                    edges = listOf(
                        DEdge("portal", "code", "no match", dashed = true),
                        DEdge("code", "stream"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("portal", "code")),
                    Beat(24, light = setOf("portal", "code"), pulse = "portal" to "code", card = BoardCard("Linux reads names exactly", "Capital letters and all. Match the case.")),
                    Beat(26, show = setOf("stream"), light = setOf("stream"), pulse = "code" to "stream", card = BoardCard("Logs", "File system logging stops after twelve hours. Long-term: App Insights.")),
                ),
            ),
            // 28: "The intern noticed that the first request each morning was slow..."
            Scene(
                28, "The intern's slow mornings",
                DiagramSpec(
                    "An idle app is unloaded and must start again; Always On, from Basic up, keeps it loaded.",
                    nodes = listOf(
                        DNode("user", "First visitor", 0.14f, 0.3f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("app", "Sleeping app", 0.72f, 0.3f, w = 0.4f, h = 0.2f, tone = 2),
                        DNode("on", "Always On", 0.72f, 0.78f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "Basic and above"),
                        DNode("big", "Bigger worker? No", 0.26f, 0.78f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("user", "app", "slow start"),
                        DEdge("on", "app", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("user", "app", "big"), light = setOf("big"), pulse = "user" to "app", card = BoardCard("A bigger worker?", "Priya said no: switch on Always On.")),
                    Beat(30, show = setOf("on"), light = setOf("on"), pulse = "on" to "app", card = BoardCard("Always On", "Keeps the app loaded with a small request now and then. Basic and up.")),
                ),
            ),
            // 31: "Does the phone app live on App Service too?"
            Scene(
                31, "Where it stops",
                DiagramSpec(
                    "App Service hosts APIs that run all day; the phone only calls the API, and event-driven code suits Functions.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.24f, 0.2f, w = 0.4f, h = 0.2f, sub = "no password"),
                        DNode("api", "Watering API", 0.24f, 0.7f, w = 0.38f, h = 0.2f, sub = "App Service"),
                        DNode("fn", "Azure Functions", 0.76f, 0.2f, w = 0.42f, h = 0.2f, tone = 1, sub = "on an event"),
                        DNode("db", "Database", 0.78f, 0.7f, w = 0.3f, h = 0.28f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "calls"),
                        DEdge("api", "db", "the password"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("phone", "api", "db", "fn"), light = setOf("fn", "phone"), pulse = "phone" to "api", card = BoardCard("Past the edge", "The phone calls the API and holds no password. On an event: Functions.")),
                ),
            ),
            // 33: "So, the office lessons for the exam?"
            Scene(
                33, "For the exam",
                DiagramSpec(
                    "The exam's App Service questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("scale", "Scale differently", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "two plans"),
                        DNode("slots", "Cheapest with slots", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Standard"),
                        DNode("secret", "Secret in the code", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "app setting"),
                        DNode("idle", "Slow after idle", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Always On"),
                        DNode("restart", "Setting changed", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "the app restarts"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("scale", "slots", "secret", "idle", "restart"), light = setOf("restart"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(36, card = BoardCard("Today", "Free Linux web app, Hello=World, watch Log stream, browse, delete.")),
                ),
            ),
        ),
    )

    private fun slotsCaseBoard() = PodcastBoard(
        "az-slots-scaling--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Phantom Bikes..."
            Scene(
                0, "The Phantom Bikes",
                DiagramSpec(
                    "After Friday's release, the bike rental app shows bikes that don't exist, and some bookings vanish mid-payment.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("api", "Rental API", 0.22f, 0.66f, w = 0.34f, h = 0.2f, sub = "App Service"),
                        DNode("ghost", "Phantom bikes", 0.76f, 0.2f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("lost", "Lost bookings", 0.76f, 0.66f, w = 0.4f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "rent"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "ghost", "lost"), light = setOf("ghost", "lost"), pulse = "phone" to "api", card = BoardCard("The case", "After Friday's release: phantom bikes and lost bookings.")),
                ),
            ),
            // 2: "With how they release. The backend has a deployment slot called staging..."
            Scene(
                2, "Slot and swap",
                DiagramSpec(
                    "A swap warms staging, then switches which slot answers the production address; the old version goes to staging.",
                    nodes = listOf(
                        DNode("users", "Riders", 0.14f, 0.5f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("prod", "Production", 0.66f, 0.2f, w = 0.48f, h = 0.2f, sub = "v1 before, v2 after"),
                        DNode("stage", "Staging slot", 0.66f, 0.8f, w = 0.48f, h = 0.2f, tone = 1, sub = "v2 before, v1 after"),
                    ),
                    edges = listOf(
                        DEdge("users", "prod", "your address"),
                        DEdge("prod", "stage", "swap", dashed = true, bidirectional = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(2, show = setOf("users", "prod", "stage"), light = setOf("stage"), card = BoardCard("Deployment slot", "A second live copy with its own address, on the same plan.")),
                    Beat(3, card = BoardCard("Suspect one", "A bad copy of the files?")),
                    Beat(4, light = setOf("prod", "stage"), pulse = "stage" to "prod", card = BoardCard("A swap moves no files", "Warm staging, then switch which workers answer production.")),
                ),
            ),
            // 6: "Exactly. Which is why you can swap back in seconds..."
            Scene(
                6, "What moves, what sticks",
                DiagramSpec(
                    "Settings move with the code in a swap unless marked as deployment slot settings, which stay with their slot.",
                    nodes = listOf(
                        DNode("prod", "Production", 0.26f, 0.2f, w = 0.44f, h = 0.2f),
                        DNode("stage", "Staging slot", 0.26f, 0.72f, w = 0.44f, h = 0.2f, tone = 1),
                        DNode("realdb", "Real database", 0.78f, 0.2f, w = 0.36f, h = 0.26f, kind = NodeKind.CYLINDER),
                        DNode("testdb", "Test database", 0.78f, 0.72f, w = 0.36f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("prod", "realdb", "sticky"),
                        DEdge("stage", "testdb"),
                        DEdge("prod", "testdb", "after the swap", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("prod", "stage", "testdb"), light = setOf("testdb"), pulse = "prod" to "testdb", card = BoardCard("The clue", "The phantom bikes come from staging's test database.")),
                    Beat(8, show = setOf("realdb"), light = setOf("realdb", "prod"), pulse = "prod" to "realdb", card = BoardCard("Sticky setting", "A deployment slot setting stays with its slot during a swap.")),
                    Beat(12, light = setOf("prod", "stage"), card = BoardCard("Mark it before the swap", "After is too late: the first swap already carried the staging value.")),
                ),
            ),
            // 13: "Okay, phantom bikes explained. What about the lost bookings?..."
            Scene(
                13, "Lost bookings",
                DiagramSpec(
                    "With several workers, keep a half-finished booking in a shared cache or database, not in one worker's memory.",
                    nodes = listOf(
                        DNode("user", "Rider", 0.14f, 0.4f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("w1", "Worker 1", 0.54f, 0.18f, w = 0.3f),
                        DNode("w2", "Worker 2", 0.54f, 0.62f, w = 0.3f),
                        DNode("cache", "Shared cache", 0.86f, 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("user", "w1"),
                        DEdge("user", "w2"),
                        DEdge("w1", "cache"),
                        DEdge("w2", "cache"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("user", "w1", "w2"), pulse = "user" to "w1", card = BoardCard("Scale out", "More workers, same tier: right for more of the same work.")),
                    Beat(16, light = setOf("w2"), pulse = "user" to "w2", card = BoardCard("Each worker has its own memory", "Worker two had never heard of the rider. Booking gone.")),
                    Beat(18, show = setOf("cache"), light = setOf("cache"), pulse = "w2" to "cache", card = BoardCard("Keep state shared", "A cache or the database. The Redis cache episode: the shared kind.")),
                ),
            ),
            // 19: "And scale up? When would that be the answer instead?"
            Scene(
                19, "Up or out",
                DiagramSpec(
                    "Scale up makes each worker bigger; scale out adds more workers behind the same address.",
                    nodes = listOf(
                        DNode("small", "Small worker", 0.22f, 0.2f, w = 0.34f, tone = 2),
                        DNode("big", "Big worker", 0.22f, 0.66f, w = 0.4f, h = 0.28f, sub = "scale up"),
                        DNode("one", "Worker", 0.74f, 0.2f, tone = 2),
                        DNode("o1", "Worker", 0.6f, 0.6f, w = 0.24f),
                        DNode("o2", "Worker", 0.87f, 0.6f, w = 0.24f),
                        DNode("out", "scale out", 0.74f, 0.88f, w = 0.3f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("small", "big", dashed = true),
                        DEdge("one", "o1", dashed = true),
                        DEdge("one", "o2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("small", "big", "one", "o1", "o2", "out"), light = setOf("big"), pulse = "small" to "big", card = BoardCard("Scale up", "A bigger worker for heavier requests: higher tier, short restart.")),
                ),
            ),
            // 21: "Was there a third clue? There's always a third clue."
            Scene(
                21, "The flapping",
                DiagramSpec(
                    "Auto scale rules sit on the plan, with a gap, a cool-down, a minimum and a maximum so they do not flap.",
                    nodes = listOf(
                        DNode("rule", "Scale rule", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "60 both ways"),
                        DNode("plan", "App Service plan", 0.5f, 0.56f, w = 0.8f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "owns the workers"),
                        DNode("cool", "Cool-down", 0.76f, 0.2f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "time between"),
                        DNode("gap", "Out at 70, in at 30", 0.5f, 0.88f, w = 0.6f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("rule", "plan"),
                        DEdge("cool", "plan", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(22, show = setOf("rule"), light = setOf("rule"), card = BoardCard("Three, four, three, four", "Add above sixty, remove below sixty: it flaps.")),
                    Beat(24, show = setOf("plan", "cool", "gap"), light = setOf("gap", "cool"), card = BoardCard("Stop the flapping", "Leave a gap, set a cool-down, a minimum and a maximum.")),
                    Beat(26, light = setOf("plan"), pulse = "rule" to "plan", card = BoardCard("Rules live on the plan", "The plan owns the workers. A known rush? A schedule rule.")),
                ),
            ),
            // 27: "One more question. Could they have caught the phantom bikes...?"
            Scene(
                27, "Catch it sooner",
                DiagramSpec(
                    "Traffic percent sends a share of users to staging; auto swap swaps once a deployment to the slot finishes.",
                    nodes = listOf(
                        DNode("deploy", "Deploy done", 0.2f, 0.2f, w = 0.34f, kind = NodeKind.PILL),
                        DNode("stage", "Staging slot", 0.7f, 0.2f, w = 0.44f, h = 0.2f, tone = 1),
                        DNode("users", "Riders", 0.14f, 0.7f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("prod", "Production", 0.7f, 0.7f, w = 0.44f, h = 0.2f, sub = "9 in 10 riders"),
                    ),
                    edges = listOf(
                        DEdge("deploy", "stage", "auto swap"),
                        DEdge("users", "prod"),
                        DEdge("users", "stage", "1 in 10", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("users", "prod"), pulse = "users" to "prod"),
                    Beat(28, show = setOf("deploy", "stage"), light = setOf("stage", "deploy"), pulse = "users" to "stage", card = BoardCard("Traffic percent, auto swap", "Try it on some riders; or swap when a deploy finishes, after warm-up.")),
                ),
            ),
            // 29: "And where does all this stop working?"
            Scene(
                29, "Past the edge",
                DiagramSpec(
                    "Slots and auto scale need Standard; reaching riders worldwide at their nearest copy is Front Door's job.",
                    nodes = listOf(
                        DNode("basic", "Basic: no slots", 0.26f, 0.22f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                        DNode("std", "Standard and up", 0.76f, 0.22f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "slots, auto scale"),
                        DNode("fd", "Front Door", 0.5f, 0.74f, w = 0.5f, h = 0.3f, kind = NodeKind.CLOUD, sub = "many regions"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("basic", "std", "fd"), light = setOf("std", "fd"), card = BoardCard("Past the edge", "Slots from Standard. The nearest copy worldwide: Front Door.")),
                ),
            ),
            // 31: "So, the case summary for the exam?"
            Scene(
                31, "Case closed",
                DiagramSpec(
                    "The exam's slots and scaling questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("db", "Keep prod's database", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "slot setting"),
                        DNode("auto", "Swap when deployed", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "auto swap"),
                        DNode("out", "Traffic doubles", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "scale out"),
                        DNode("flap", "Rule keeps flapping", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "cool-down"),
                        DNode("swap", "What a swap changes", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "routing, not files"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("db", "auto", "out", "flap"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(34, show = setOf("swap"), light = setOf("swap"), card = BoardCard("A swap changes routing", "The warmed staging copy answers production. Swap back any time.")),
                    Beat(36, card = BoardCard("Today", "Add a staging slot, change the title there, swap, check, swap back, delete.")),
                ),
            ),
        ),
    )

    private fun slotsGossipBoard() = PodcastBoard(
        "az-slots-scaling--gossip",
        listOf(
            // 0: "Did you hear what happened on quiz night?..."
            Scene(
                0, "Quiz night",
                DiagramSpec(
                    "Scaled out, each worker kept its own players' progress, so players bounced between workers and lost it.",
                    nodes = listOf(
                        DNode("users", "Players", 0.14f, 0.4f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("w1", "Worker 1", 0.54f, 0.18f, w = 0.3f),
                        DNode("w2", "Worker 2", 0.54f, 0.62f, w = 0.3f),
                        DNode("cache", "Shared cache", 0.86f, 0.4f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("users", "w1"),
                        DEdge("users", "w2"),
                        DEdge("w1", "cache"),
                        DEdge("w2", "cache"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("users", "w1"), light = setOf("users"), pulse = "users" to "w1", card = BoardCard("Thursday quiz night", "Two thousand players; half sent back to the start screen.")),
                    Beat(2, show = setOf("w2"), light = setOf("w1", "w2"), pulse = "users" to "w2", card = BoardCard("Scale out", "More workers behind the same address, on the same tier.")),
                    Beat(4, show = setOf("cache"), light = setOf("cache"), pulse = "w2" to "cache", card = BoardCard("Each worker has its own memory", "Omar moved the players' progress to a shared cache.")),
                ),
            ),
            // 5: "So scale out needs the app to keep its state somewhere shared..."
            Scene(
                5, "Up or out",
                DiagramSpec(
                    "Scale up makes each worker bigger; scale out adds more workers behind the same address.",
                    nodes = listOf(
                        DNode("small", "Small worker", 0.22f, 0.2f, w = 0.34f, tone = 2),
                        DNode("big", "Big worker", 0.22f, 0.66f, w = 0.4f, h = 0.28f, sub = "scale up"),
                        DNode("one", "Worker", 0.74f, 0.2f, tone = 2),
                        DNode("o1", "Worker", 0.6f, 0.6f, w = 0.24f),
                        DNode("o2", "Worker", 0.87f, 0.6f, w = 0.24f),
                        DNode("out", "scale out", 0.74f, 0.88f, w = 0.3f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("small", "big", dashed = true),
                        DEdge("one", "o1", dashed = true),
                        DEdge("one", "o2", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("small", "big", "one", "o1", "o2", "out"), pulse = "small" to "big", card = BoardCard("Heavier, or just more?", "Heavier requests: scale up. More of the same, like quiz night: scale out.")),
                ),
            ),
            // 8: "Ben. Ben set up auto scale for quiz night..."
            Scene(
                8, "Ben's flapping rule",
                DiagramSpec(
                    "Auto scale rules sit on the plan, with a gap, a cool-down, a minimum and a maximum so they do not flap.",
                    nodes = listOf(
                        DNode("rule", "Scale rule", 0.26f, 0.2f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "50 both ways"),
                        DNode("plan", "App Service plan", 0.5f, 0.56f, w = 0.8f, h = 0.18f, kind = NodeKind.PILL, tone = 1, sub = "owns the workers"),
                        DNode("cool", "Cool-down", 0.76f, 0.2f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "time between"),
                        DNode("gap", "Out at 70, in at 30", 0.5f, 0.88f, w = 0.6f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("rule", "plan"),
                        DEdge("cool", "plan", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("rule"), light = setOf("rule"), card = BoardCard("Fifty both ways", "Add above fifty, remove below fifty. It flapped all night.")),
                    Beat(10, show = setOf("cool", "gap"), light = setOf("cool", "gap"), card = BoardCard("Leave a gap", "Out at seventy, in at thirty, a cool-down, a minimum and a maximum.")),
                    Beat(12, show = setOf("plan"), light = setOf("plan"), pulse = "rule" to "plan", card = BoardCard("Rules live on the plan", "Quiz night is every Thursday: a schedule rule adds workers first.")),
                ),
            ),
            // 14: "That was Omar. The team ships with a deployment slot..."
            Scene(
                14, "Omar's swap",
                DiagramSpec(
                    "A swap warms staging, then switches which slot answers the production address; the old version goes to staging.",
                    nodes = listOf(
                        DNode("users", "Players", 0.14f, 0.5f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("prod", "Production", 0.66f, 0.2f, w = 0.48f, h = 0.2f, sub = "the stage"),
                        DNode("stage", "Staging slot", 0.66f, 0.8f, w = 0.48f, h = 0.2f, tone = 1, sub = "the rehearsal room"),
                    ),
                    edges = listOf(
                        DEdge("users", "prod", "your address"),
                        DEdge("prod", "stage", "swap", dashed = true, bidirectional = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("users", "prod", "stage"), light = setOf("stage"), card = BoardCard("Deployment slot", "A second live copy: staging rehearses, production is the stage.")),
                    Beat(16, light = setOf("prod", "stage"), pulse = "stage" to "prod", card = BoardCard("A swap moves no files", "Warm staging, then switch which workers answer production.")),
                ),
            ),
            // 17: "Then how did test questions reach real players?"
            Scene(
                17, "Test questions, live",
                DiagramSpec(
                    "Settings move with the code in a swap unless marked as deployment slot settings, which stay with their slot.",
                    nodes = listOf(
                        DNode("prod", "Production", 0.26f, 0.2f, w = 0.44f, h = 0.2f),
                        DNode("stage", "Staging slot", 0.26f, 0.72f, w = 0.44f, h = 0.2f, tone = 1),
                        DNode("realdb", "Real questions", 0.78f, 0.2f, w = 0.38f, h = 0.26f, kind = NodeKind.CYLINDER),
                        DNode("testdb", "Test questions", 0.78f, 0.72f, w = 0.38f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("prod", "realdb", "sticky"),
                        DEdge("stage", "testdb"),
                        DEdge("prod", "testdb", "after the swap", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("prod", "stage", "testdb"), light = setOf("testdb"), pulse = "prod" to "testdb", card = BoardCard("Settings travel with the code", "Unless marked as a slot setting. Then it's sticky and stays.")),
                    Beat(20, show = setOf("realdb"), light = setOf("realdb", "prod"), pulse = "prod" to "realdb", card = BoardCard("Mark it before the swap", "After is too late: the first swap already carried the value.")),
                    Beat(22, light = setOf("stage"), card = BoardCard("Swap back", "The old version sits in staging. Seconds, and no redeploy.")),
                ),
            ),
            // 23: "Oh no. What did the intern do?"
            Scene(
                23, "The intern and auto swap",
                DiagramSpec(
                    "Auto swap swaps once a deployment to the slot finishes, after warm-up; Traffic percent sends some users to staging.",
                    nodes = listOf(
                        DNode("deploy", "Deploy done", 0.2f, 0.2f, w = 0.34f, kind = NodeKind.PILL),
                        DNode("stage", "Staging slot", 0.7f, 0.2f, w = 0.44f, h = 0.2f, tone = 1),
                        DNode("users", "Players", 0.14f, 0.7f, w = 0.2f, h = 0.26f, kind = NodeKind.USER),
                        DNode("prod", "Production", 0.7f, 0.7f, w = 0.44f, h = 0.2f, sub = "9 in 10 players"),
                    ),
                    edges = listOf(
                        DEdge("deploy", "stage", "auto swap"),
                        DEdge("users", "prod"),
                        DEdge("users", "stage", "1 in 10", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("deploy", "stage"), light = setOf("deploy"), pulse = "deploy" to "stage", card = BoardCard("Auto swap", "Swaps when a deployment to the slot finishes, after the warm-up.")),
                    Beat(26, show = setOf("users", "prod"), light = setOf("stage"), pulse = "users" to "stage", card = BoardCard("Traffic percent", "One player in ten on staging; a cookie keeps them there.")),
                ),
            ),
            // 27: "Does all this work on any App Service tier?"
            Scene(
                27, "Past the edge",
                DiagramSpec(
                    "Slots and auto scale need Standard; reaching players worldwide at their nearest copy is Front Door's job.",
                    nodes = listOf(
                        DNode("basic", "Basic: no slots", 0.26f, 0.22f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2),
                        DNode("std", "Standard and up", 0.76f, 0.22f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "slots, auto scale"),
                        DNode("fd", "Front Door", 0.5f, 0.74f, w = 0.5f, h = 0.3f, kind = NodeKind.CLOUD, sub = "many regions"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("basic", "std", "fd"), light = setOf("std", "fd"), card = BoardCard("Past the edge", "Slots from Standard. The nearest copy worldwide: Front Door.")),
                ),
            ),
            // 29: "So, the gossip to remember for the exam?"
            Scene(
                29, "For the exam",
                DiagramSpec(
                    "The exam's slots and scaling questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("db", "Keep prod's database", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "slot setting"),
                        DNode("auto", "Swap when deployed", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "auto swap"),
                        DNode("out", "Traffic doubles", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "scale out"),
                        DNode("flap", "Rule keeps flapping", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "cool-down"),
                        DNode("swap", "What a swap changes", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "routing, not files"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("db", "auto", "out", "flap"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(32, show = setOf("swap"), light = setOf("swap"), card = BoardCard("A swap changes routing", "Not the files. And auto scale rules sit on the plan.")),
                    Beat(34, card = BoardCard("Today", "Add a staging slot, change the title there, swap, check, swap back, delete.")),
                ),
            ),
        ),
    )

    private fun functionsCaseBoard() = PodcastBoard(
        "az-functions--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Reminder That Never Rang..."
            Scene(
                0, "The Reminder That Never Rang",
                DiagramSpec(
                    "A timer should start the habit tracker's reminder function at seven each morning; for three days, nothing.",
                    nodes = listOf(
                        DNode("timer", "Timer, 7 a.m.", 0.22f, 0.2f, w = 0.36f, kind = NodeKind.PILL),
                        DNode("fn", "Reminder function", 0.74f, 0.2f, w = 0.44f, h = 0.2f),
                        DNode("host", "The host", 0.24f, 0.72f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "listens for events"),
                        DNode("phone", "Android app", 0.74f, 0.72f, w = 0.4f, h = 0.2f, sub = "no nudge"),
                    ),
                    edges = listOf(
                        DEdge("timer", "fn", "trigger"),
                        DEdge("fn", "phone", "nudge", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("timer", "fn", "phone"), light = setOf("phone"), card = BoardCard("The case", "Three days, and not one morning nudge.")),
                    Beat(2, show = setOf("host"), light = setOf("host", "fn"), card = BoardCard("A function", "Small code that runs when something happens. A host starts it.")),
                    Beat(4, light = setOf("timer"), pulse = "timer" to "fn", card = BoardCard("Exactly one trigger", "The one event that starts a function. Here: a timer.")),
                ),
            ),
            // 5: "Then my first suspect is the schedule..."
            Scene(
                5, "Suspect one: the schedule",
                DiagramSpec(
                    "NCRONTAB has six fields, and the first is seconds, not minutes.",
                    nodes = listOf(
                        DNode("sec", "sec", 0.09f, 0.4f, w = 0.15f, h = 0.2f),
                        DNode("min", "min", 0.25f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("hour", "hour", 0.41f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("day", "day", 0.57f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("month", "mon", 0.73f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("week", "wday", 0.9f, 0.4f, w = 0.16f, h = 0.2f, tone = 1),
                        DNode("ok", "Suspect cleared", 0.5f, 0.82f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("sec", "min", "hour", "day", "month", "week")),
                    Beat(6, show = setOf("ok"), light = setOf("hour", "ok"), card = BoardCard("Seven every morning", "0 0 7 * * *", code = true)),
                ),
            ),
            // 7: "Okay. Suspect two. It's on the Consumption plan..."
            Scene(
                7, "Suspect two: Consumption",
                DiagramSpec(
                    "Consumption scales to zero and bills per run, at the price of cold starts and a ten-minute cap.",
                    nodes = listOf(
                        DNode("plan", "Consumption", 0.5f, 0.16f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "pay per run"),
                        DNode("zero", "Scales to zero", 0.26f, 0.5f, w = 0.44f, h = 0.18f, tone = 1),
                        DNode("cold", "Cold start", 0.76f, 0.5f, w = 0.4f, h = 0.18f, tone = 1, sub = "a cold car"),
                        DNode("cap", "Ten minutes max", 0.5f, 0.84f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("plan", "zero"),
                        DEdge("plan", "cold"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("plan", "cap"), light = setOf("cap"), card = BoardCard("The weekly report", "Twenty minutes on Consumption: stopped at ten, every Sunday.")),
                    Beat(10, show = setOf("zero", "cold"), light = setOf("cold"), pulse = "plan" to "cold", card = BoardCard("Cold start", "A delay while Azure loads the function after a quiet spell.")),
                ),
            ),
            // 11: "So how did they fix the long report?"
            Scene(
                11, "The move to Dedicated",
                DiagramSpec(
                    "On a Dedicated App Service plan the host sleeps when idle, so a timer needs Always On.",
                    nodes = listOf(
                        DNode("ded", "Dedicated", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "App Service plan"),
                        DNode("fa", "Function app", 0.76f, 0.2f, w = 0.4f, h = 0.22f),
                        DNode("sleep", "Host asleep", 0.26f, 0.72f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "idle for hours"),
                        DNode("on", "Always On", 0.76f, 0.72f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "keeps it awake"),
                    ),
                    edges = listOf(
                        DEdge("fa", "ded", "moved"),
                        DEdge("on", "fa", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("ded", "fa"), light = setOf("ded"), pulse = "fa" to "ded", card = BoardCard("The clue", "For the long report, the app moved onto the backend's plan.")),
                    Beat(14, show = setOf("sleep", "on"), light = setOf("sleep", "on"), pulse = "on" to "fa", card = BoardCard("Always On", "On Dedicated, a timer needs it. A sleeping host hears no alarm.")),
                ),
            ),
            // 16: "Not yet. The developer's first panic fix..."
            Scene(
                16, "Every five seconds",
                DiagramSpec(
                    "A step in the first field repeats every few seconds; every five minutes puts the step in the second field.",
                    nodes = listOf(
                        DNode("sec", "sec", 0.09f, 0.4f, w = 0.15f, h = 0.2f, tone = 2),
                        DNode("min", "min", 0.25f, 0.4f, w = 0.15f, h = 0.2f),
                        DNode("hour", "hour", 0.41f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("day", "day", 0.57f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("month", "mon", 0.73f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("week", "wday", 0.9f, 0.4f, w = 0.16f, h = 0.2f, tone = 1),
                        DNode("trap", "Seconds come first", 0.5f, 0.82f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("sec", "min", "hour", "day", "month", "week"), light = setOf("sec"), card = BoardCard("The panic fix", "*/5 * * * * *", code = true)),
                    Beat(18, show = setOf("trap"), light = setOf("min", "trap"), card = BoardCard("Every five minutes", "0 */5 * * * *", code = true)),
                ),
            ),
            // 19: "Ouch. Were there other clues in the file?"
            Scene(
                19, "The committed file",
                DiagramSpec(
                    "The local settings file is for your laptop and never committed; in Azure the same keys live in app settings.",
                    nodes = listOf(
                        DNode("host", "host.json", 0.26f, 0.3f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "whole function app"),
                        DNode("local", "local.settings.json", 0.74f, 0.3f, w = 0.48f, h = 0.22f, kind = NodeKind.NOTE, tone = 1, sub = "laptop only"),
                        DNode("never", "Never commit it", 0.74f, 0.78f, w = 0.48f, h = 0.18f, kind = NodeKind.PILL, tone = 2),
                        DNode("appset", "App settings", 0.26f, 0.78f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "in Azure"),
                    ),
                    edges = listOf(
                        DEdge("local", "never", dashed = true),
                        DEdge("local", "appset", "same keys", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("local", "never"), light = setOf("local", "never"), card = BoardCard("In the repository", "The local settings file, with the storage connection string.")),
                    Beat(22, show = setOf("host", "appset"), light = setOf("appset", "host"), pulse = "local" to "appset", card = BoardCard("In Azure", "Same keys in app settings; secrets in Key Vault. host.json is deployed.")),
                ),
            ),
            // 23: "A function app being the whole box, not one function?"
            Scene(
                23, "One box, one trigger each",
                DiagramSpec(
                    "A function app holds many functions sharing a plan, runtime and settings; each has its own single trigger.",
                    nodes = listOf(
                        DNode("timer", "Timer, 7 a.m.", 0.2f, 0.16f, w = 0.34f, kind = NodeKind.PILL),
                        DNode("sweep", "Reminder", 0.7f, 0.16f, w = 0.4f, h = 0.2f),
                        DNode("queue", "Queue", 0.7f, 0.5f, w = 0.3f, h = 0.22f, kind = NodeKind.CYLINDER),
                        DNode("send", "Sender", 0.24f, 0.5f, w = 0.4f, h = 0.2f),
                        DNode("app", "One function app", 0.5f, 0.86f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, sub = "same plan, runtime, settings"),
                    ),
                    edges = listOf(
                        DEdge("timer", "sweep", "trigger"),
                        DEdge("sweep", "queue", "output"),
                        DEdge("queue", "send", "trigger"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("timer", "sweep", "queue", "send", "app"), light = setOf("app"), pulse = "queue" to "send", card = BoardCard("Function app", "The box you deploy: many functions sharing plan, runtime and settings.")),
                ),
            ),
            // 25: "If the reminder reads users from a database and writes messages to a queue..."
            Scene(
                25, "Bindings",
                DiagramSpec(
                    "One trigger starts the function; input bindings bring data in and output bindings send results out.",
                    nodes = listOf(
                        DNode("trig", "Timer", 0.16f, 0.5f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("fn", "Reminder", 0.5f, 0.5f, w = 0.32f, h = 0.24f),
                        DNode("in", "Input binding", 0.5f, 0.13f, w = 0.4f, h = 0.18f, tone = 1, sub = "read the users"),
                        DNode("out", "Output binding", 0.84f, 0.84f, w = 0.3f, h = 0.24f, tone = 1, sub = "write to a queue"),
                    ),
                    edges = listOf(
                        DEdge("trig", "fn"),
                        DEdge("in", "fn"),
                        DEdge("fn", "out"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("trig", "fn"), pulse = "trig" to "fn"),
                    Beat(26, show = setOf("in", "out"), light = setOf("in", "out"), pulse = "fn" to "out", card = BoardCard("Binding", "A ready-made connection: no connection code. Still one trigger.")),
                ),
            ),
            // 27: "Like plugs instead of wiring. And where does all this stop working?"
            Scene(
                27, "Past the edge",
                DiagramSpec(
                    "No cold start or a private network points to Premium; work that waits for days belongs in Durable Functions.",
                    nodes = listOf(
                        DNode("prem", "Premium", 0.26f, 0.22f, w = 0.46f, h = 0.22f, sub = "pre-warmed"),
                        DNode("long", "Over ten minutes", 0.76f, 0.22f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "not Consumption"),
                        DNode("dur", "Durable Functions", 0.5f, 0.74f, w = 0.56f, h = 0.22f, tone = 1, sub = "waits for days"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(28, show = setOf("prem", "long", "dur"), light = setOf("prem", "dur"), card = BoardCard("Past the edge", "No cold start: Premium. A thirty-day challenge: Durable Functions.")),
                ),
            ),
            // 29: "So, the case summary for the exam?"
            Scene(
                29, "Case closed",
                DiagramSpec(
                    "The exam's Functions questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("one", "How many triggers", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "exactly one"),
                        DNode("cold", "No cold start", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Premium"),
                        DNode("cheap", "Few runs a day", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "Consumption"),
                        DNode("long", "Runs 30 minutes", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "not Consumption"),
                        DNode("timer", "Timer never fires", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "Always On"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("one", "cold", "cheap", "long", "timer"), light = setOf("timer"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(32, card = BoardCard("Count the fields", "Six, and the first is seconds: NCRONTAB.")),
                    Beat(34, card = BoardCard("Today", "Consumption app, an HTTP trigger, then a timer every minute. Watch Monitor.")),
                ),
            ),
        ),
    )

    private fun functionsGossipBoard() = PodcastBoard(
        "az-functions--gossip",
        listOf(
            // 0: "Did you hear what Lena did with the dog walking app?..."
            Scene(
                0, "What Lena did",
                DiagramSpec(
                    "NCRONTAB has six fields, and the first is seconds; a five-field schedule means something else.",
                    nodes = listOf(
                        DNode("sec", "sec", 0.09f, 0.4f, w = 0.15f, h = 0.2f),
                        DNode("min", "min", 0.25f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("hour", "hour", 0.41f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("day", "day", 0.57f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("month", "mon", 0.73f, 0.4f, w = 0.15f, h = 0.2f, tone = 1),
                        DNode("week", "wday", 0.9f, 0.4f, w = 0.16f, h = 0.2f, tone = 1),
                        DNode("trap", "Five fields? Wrong", 0.5f, 0.82f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("trap"), light = setOf("trap"), card = BoardCard("Lena's evening summary", "A five-field schedule, and summaries at all the wrong times.")),
                    Beat(2, card = BoardCard("A function", "Small code that runs when something happens. A host starts it.")),
                    Beat(4, show = setOf("sec", "min", "hour", "day", "month", "week"), light = setOf("sec"), card = BoardCard("NCRONTAB", "Six fields: seconds, minutes, hours, day, month, weekday.")),
                    Beat(6, light = setOf("min"), card = BoardCard("Every five minutes", "0 */5 * * * *", code = true)),
                ),
            ),
            // 7: "Of course. What else happened?"
            Scene(
                7, "Raj's long invoices",
                DiagramSpec(
                    "Consumption scales to zero and bills per run, at the price of cold starts and a ten-minute cap.",
                    nodes = listOf(
                        DNode("plan", "Consumption", 0.5f, 0.16f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "pay per run"),
                        DNode("zero", "Scales to zero", 0.26f, 0.5f, w = 0.44f, h = 0.18f, tone = 1),
                        DNode("cold", "Cold start", 0.76f, 0.5f, w = 0.4f, h = 0.18f, tone = 1, sub = "a cold car"),
                        DNode("cap", "Ten minutes max", 0.5f, 0.84f, w = 0.6f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("plan", "zero"),
                        DEdge("plan", "cold"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(8, show = setOf("plan", "cap"), light = setOf("cap"), card = BoardCard("Twenty minutes on Consumption", "Stopped at ten minutes, every single month.")),
                    Beat(10, show = setOf("zero", "cold"), light = setOf("cold"), pulse = "plan" to "cold", card = BoardCard("Cold start", "A delay while Azure loads the function after a quiet spell.")),
                ),
            ),
            // 11: "So what did Raj do?"
            Scene(
                11, "Premium or Durable",
                DiagramSpec(
                    "Premium pre-warms, runs longer and joins private networks; Durable Functions splits long work into steps.",
                    nodes = listOf(
                        DNode("prem", "Premium", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "pre-warmed"),
                        DNode("dur", "Durable Functions", 0.76f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "steps that wait"),
                        DNode("book", "Booking function", 0.26f, 0.7f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "answers instantly"),
                    ),
                    edges = listOf(
                        DEdge("book", "prem"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(12, show = setOf("prem", "dur"), light = setOf("prem", "dur"), card = BoardCard("Raj's two choices", "Premium: sixty minutes by default. Or Durable Functions.")),
                    Beat(14, show = setOf("book"), light = setOf("prem", "book"), pulse = "book" to "prem", card = BoardCard("Premium", "No cold start, private network, but you pay while it's idle.")),
                ),
            ),
            // 16: "The intern, again. They committed the local settings file..."
            Scene(
                16, "The intern's file",
                DiagramSpec(
                    "The local settings file is for your laptop and never committed; in Azure the same keys live in app settings.",
                    nodes = listOf(
                        DNode("host", "host.json", 0.26f, 0.3f, w = 0.42f, h = 0.22f, kind = NodeKind.NOTE, sub = "whole function app"),
                        DNode("local", "local.settings.json", 0.74f, 0.3f, w = 0.48f, h = 0.22f, kind = NodeKind.NOTE, tone = 1, sub = "laptop only"),
                        DNode("never", "Never commit it", 0.74f, 0.78f, w = 0.48f, h = 0.18f, kind = NodeKind.PILL, tone = 2),
                        DNode("appset", "App settings", 0.26f, 0.78f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "in Azure"),
                    ),
                    edges = listOf(
                        DEdge("local", "never", dashed = true),
                        DEdge("local", "appset", "same keys", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("local", "never"), light = setOf("local", "never"), card = BoardCard("Committed by the intern", "The local settings file, with the storage connection string.")),
                    Beat(18, show = setOf("host", "appset"), light = setOf("appset", "host"), pulse = "local" to "appset", card = BoardCard("In Azure", "Same keys in app settings; secrets in Key Vault. host.json is deployed.")),
                ),
            ),
            // 19: "Function app meaning the whole box?"
            Scene(
                19, "The dog app's box",
                DiagramSpec(
                    "One function app holds the photo, summary and sender functions; each has exactly one trigger.",
                    nodes = listOf(
                        DNode("blob", "Blob trigger", 0.2f, 0.16f, w = 0.34f, kind = NodeKind.PILL),
                        DNode("thumb", "Photo function", 0.7f, 0.16f, w = 0.42f, h = 0.2f),
                        DNode("summary", "Summary", 0.24f, 0.5f, w = 0.4f, h = 0.2f),
                        DNode("send", "Sender", 0.72f, 0.5f, w = 0.4f, h = 0.2f),
                        DNode("app", "One function app", 0.5f, 0.86f, w = 0.7f, h = 0.18f, kind = NodeKind.NOTE, sub = "same plan, runtime, settings"),
                    ),
                    edges = listOf(
                        DEdge("blob", "thumb", "trigger"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("thumb", "summary", "send", "app"), light = setOf("app"), card = BoardCard("Function app", "The box you deploy. Each function has its own single trigger.")),
                    Beat(22, show = setOf("blob"), light = setOf("blob", "thumb"), pulse = "blob" to "thumb", card = BoardCard("Tom's two triggers", "Not allowed. Every function has exactly one.")),
                ),
            ),
            // 23: "But it reads the photo and writes the thumbnail..."
            Scene(
                23, "Bindings",
                DiagramSpec(
                    "One trigger starts the function; input bindings bring data in and output bindings send results out.",
                    nodes = listOf(
                        DNode("trig", "Trigger", 0.16f, 0.5f, h = 0.18f, kind = NodeKind.PILL),
                        DNode("fn", "Photo function", 0.5f, 0.5f, w = 0.32f, h = 0.24f),
                        DNode("in", "Input binding", 0.5f, 0.13f, w = 0.4f, h = 0.18f, tone = 1, sub = "hands data in"),
                        DNode("out", "Output binding", 0.84f, 0.84f, w = 0.3f, h = 0.24f, tone = 1, sub = "the thumbnail"),
                    ),
                    edges = listOf(
                        DEdge("trig", "fn"),
                        DEdge("in", "fn"),
                        DEdge("fn", "out"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("trig", "fn"), pulse = "trig" to "fn"),
                    Beat(24, show = setOf("in", "out"), light = setOf("in", "out"), pulse = "fn" to "out", card = BoardCard("Binding", "A ready-made connection to a service. No connection code to write.")),
                ),
            ),
            // 25: "And the last bit of gossip?"
            Scene(
                25, "Chris and the sleeping host",
                DiagramSpec(
                    "On a Dedicated App Service plan the host sleeps when idle, so a timer needs Always On.",
                    nodes = listOf(
                        DNode("ded", "Dedicated", 0.26f, 0.2f, w = 0.44f, h = 0.22f, sub = "App Service plan"),
                        DNode("fa", "Evening summary", 0.76f, 0.2f, w = 0.42f, h = 0.22f),
                        DNode("sleep", "Host asleep", 0.26f, 0.72f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "idle all day"),
                        DNode("on", "Always On", 0.76f, 0.72f, w = 0.4f, h = 0.18f, kind = NodeKind.PILL, sub = "keeps it awake"),
                    ),
                    edges = listOf(
                        DEdge("fa", "ded", "moved"),
                        DEdge("on", "fa", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("ded", "fa"), light = setOf("ded"), pulse = "fa" to "ded", card = BoardCard("To save money", "The summary moved onto the backend's App Service plan.")),
                    Beat(28, show = setOf("sleep", "on"), light = setOf("sleep", "on"), pulse = "on" to "fa", card = BoardCard("Always On", "On Dedicated, a sleeping host can't hear the timer.")),
                ),
            ),
            // 29: "Does the phone app run functions itself?"
            Scene(
                29, "Past the edge",
                DiagramSpec(
                    "Functions run in Azure; the phone uploads or calls an HTTP trigger, and an all-day API may suit App Service.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.24f, 0.22f, w = 0.4f, h = 0.2f, sub = "uploads, calls"),
                        DNode("fn", "Function", 0.76f, 0.22f, w = 0.36f, h = 0.2f, sub = "in Azure"),
                        DNode("svc", "App Service", 0.5f, 0.74f, w = 0.5f, h = 0.3f, kind = NodeKind.CLOUD, sub = "busy all day"),
                    ),
                    edges = listOf(
                        DEdge("phone", "fn", "HTTP or blob"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("phone", "fn", "svc"), light = setOf("fn", "svc"), pulse = "phone" to "fn", card = BoardCard("Past the edge", "The phone uploads or calls; the function runs in Azure.")),
                ),
            ),
            // 31: "So, the gossip I should remember for the exam?"
            Scene(
                31, "For the exam",
                DiagramSpec(
                    "The exam's Functions questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("one", "How many triggers", 0.26f, 0.13f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "exactly one"),
                        DNode("cold", "No cold start", 0.76f, 0.13f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "Premium"),
                        DNode("cheap", "Few runs a day", 0.26f, 0.45f, w = 0.46f, h = 0.18f, kind = NodeKind.NOTE, sub = "Consumption"),
                        DNode("long", "Runs 30 minutes", 0.76f, 0.45f, w = 0.44f, h = 0.18f, kind = NodeKind.NOTE, sub = "not Consumption"),
                        DNode("cron", "A schedule question", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "six fields, seconds first"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(32, show = setOf("one", "cold", "cheap", "long", "cron"), light = setOf("cron"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(34, card = BoardCard("Today", "Consumption app, an HTTP trigger, then a timer every minute. Watch Monitor.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy {
        listOf(
            appServiceCaseBoard(), appServiceGossipBoard(),
            slotsCaseBoard(), slotsGossipBoard(),
            functionsCaseBoard(), functionsGossipBoard(),
        )
    }
}
