package com.shahaabapps.core.content

/** Azure Functions in Python: triggers, bindings and serverless APIs: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesFunctions {
    private val classic = Podcast(
        "ai-functions",
        listOf(
            maya("Hi, and welcome back. Zaiqa's API, the .NET service the phone talks to, lives in Container Apps. Orders sit in Azure SQL, and dish photos are files in Blob Storage. Today, the piece we keep mentioning and never open: the photo function."),
            leo("The thing that makes the thumbnails. Why is that not just another container, like everything else?"),
            maya("Because of the shape of its work. A restaurant adds one dish, then nothing for an hour, then forty photos at once. For example, a chain putting ten branches on Zaiqa in a morning. A container sitting idle all night still costs money."),
            leo("And this does not?"),
            maya("On the right plan, no. Azure Functions runs small pieces of code when something happens, and you pay for the time they run. Picture a porch light with a motion sensor: dark and free until somebody walks up."),
            leo("So what happens when the restaurant presses upload?"),
            maya("The partner dashboard puts the file in Blob Storage. Event Grid notices and pushes the news to our function. Nobody waits: the restaurant's screen says saved straight away."),
            leo("And the function does what, in order?"),
            maya("Two jobs. It makes the thumbnail, a small copy of the photo for the menu list. Then it writes the dish vector into Cosmos, and a vector is just a list of numbers that captures what a dish is like, so the taste service can find something near it."),
            leo("Right. So how does the function know a photo arrived? Does it keep checking the storage account?"),
            maya("No, and that is the trigger: the thing that starts a function, like a doorbell. Each function has exactly one. Ours is an Event Grid trigger, so Event Grid pushes each piece of news to the function, the way a notification lands on your phone."),
            leo("And then it reads the photo itself?"),
            maya("It could, with the storage library. We use a binding instead. A binding is a connection you declare rather than code you write, like plumbing that is already fitted: the photo arrives as a parameter, and the Cosmos record leaves when we set one."),
            leo("Two kinds, then. Which way round?"),
            maya("Data coming in is an input binding, data going out is an output binding, and the thing that starts it all is the trigger. So ours is an Event Grid trigger, a blob input binding for the photo, and a Cosmos output binding for the dish record."),
            leo("And no library code at all?"),
            maya("None. No SDK code, and an SDK is a software development kit, a service's own library: a client, a credential, a call, a tidy-up. One line above the function does the same job."),
            leo("Which line?"),
            maya("A decorator: a line just above a Python function, starting with the at sign, like a sticky note saying what it is for. In the v2 programming model, the newer way of writing these, every function lives in one file, function app dot py, with its sticky notes on top."),
            leo("And the way before that?"),
            maya("A folder for each function, with a settings file called function dot json beside the code. That is the v1 model. For Python v2 that file does not exist, so a question that shows you one in a v2 project is wrong."),
            leo("Good. Now the part I always get wrong. Where does the storage password go?"),
            maya("[pause] Nowhere, ideally. In the decorator, the connection is not a connection string, an address with a password baked in. It is the name of an app setting, a named value Azure hands your app while it runs."),
            leo("And the setting holds the password instead?"),
            maya("Not for us. The function app has a managed identity, a badge Azure gives it so it proves who it is with no password. The setting holds only the storage address, with blob service URI on the end of its name, and the badge gets a read role on the photos."),
            leo("The same badge the API carries for Cosmos. And on my laptop?"),
            maya("There the same settings live in local settings dot json, and here is the trap: that file is never deployed. Core Tools, the func command, publishes your code and leaves it behind. Every value in it must exist in Azure too, as an app setting."),
            leo("So it works on my laptop and dies in Azure with a missing setting."),
            maya("[sigh] Every team does that once. Func init makes the project, func start runs it on your machine, and func azure functionapp publish sends the code up."),
            leo("Where does it run up there? Do I pick a machine?"),
            maya("You pick a hosting plan, which is how you rent the machines. For a new app like ours the default is Flex Consumption. It scales to zero, so nothing runs and nothing is paid at four in the morning, and it can join a virtual network: a private network only our own resources share."),
            leo("And the older Consumption plan?"),
            maya("Still there, and the exam still asks about it. It also scales to zero, but it cannot join a virtual network, and a function on it must finish inside ten minutes."),
            leo("What if I hate waiting? A sleeping app must take a moment to wake up."),
            maya("That pause is a cold start. Flex Consumption can keep a few copies always ready, which cuts it. If the rule is no cold start at all, that is the Premium plan: always warm, billed per copy even when idle, and thirty minutes a run by default instead of ten."),
            leo("Does a cold start matter for a thumbnail?"),
            maya("Not in the least. Nobody is staring at one. It would matter for something a diner waits on, which is why the surge fee lives in the API and not in a function."),
            leo("And if I already pay for servers?"),
            maya("Then there is the App Service plan, servers rented by the month. If you already run one, for example for an internal tool, your functions can share it. It never scales to zero, and that is the reason you would choose it."),
            leo("Now a real one from last week. The partner team wanted a web address that rebuilds every thumbnail for a restaurant. It timed out."),
            maya("It always will. An HTTP function has about two hundred and thirty seconds to answer, and that ceiling belongs to the platform in front of it, not to a setting you can raise. Two hundred photos never finish in four minutes."),
            leo("So what do you do instead?"),
            maya("Answer at once and work afterwards. The function takes the request, starts the job and replies accepted, with an address the dashboard can check for progress. The two hundred thumbnails then happen in the background, at their own speed."),
            leo("And if the job has steps? Resize, describe, write, then tell the dashboard it is finished."),
            maya("Then you reach for Durable Functions, an extension where one function is the recipe and the others are the steps. The recipe calls each step, fans two hundred thumbnails out at once, waits for them all, and carries on if the machine restarts underneath it."),
            leo("Carries on how?"),
            maya("It writes its progress to storage and replays the recipe, so a restart picks up where it stopped. [pause] That is the one thing a plain function cannot do: it dies with its machine."),
            leo("So what do I remember for the exam?"),
            maya("Five things. A trigger in the v2 programming model is a decorator in function app dot py, never function dot json. A setting that works locally and is missing in Azure means the local file was not deployed, so add an app setting. Writing to a store with no SDK code is an output binding."),
            leo("And the plans?"),
            maya("Scale to zero inside a virtual network is Flex Consumption. No cold start at all is Premium. And a long job behind a web address is never an HTTP function waiting: it is accepted now, with Durable or a queue behind it."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Run func init and add an HTTP function with app dot route. Call it locally after func start. Create a Flex Consumption app and publish to it. Then add an app setting in Azure, and read it in Python with os dot environ."),
            leo("A porch light for Zaiqa's photos. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-functions",
        listOf(
            maya("Welcome to Case Files. Today's case: The Dish That Never Appeared. A Mumbai restaurant put eleven new dishes on Zaiqa on Monday, with a photo for each. By Tuesday the menu showed all eleven, and every one of them had a grey box where the picture should be."),
            leo("Detective Leo, reporting. Somebody has lost eleven photographs."),
            maya("Nobody lost anything. All eleven files are in Blob Storage, full size. What is missing is the thumbnail, the small copy the menu list shows, and the dish record in Cosmos."),
            leo("Made by what, exactly?"),
            maya("The photo function: a small Python program on Azure Functions, which runs your code when something happens and charges you for the time it runs. It makes the thumbnail and writes the dish vector into Cosmos, where a vector is just a list of numbers that captures what a dish is like."),
            leo("First suspect, then. Event Grid. It never told the function."),
            maya("[pause] Wrong suspect, and I can prove it. Event Grid pushes each piece of news to its handlers, and it keeps count. Its own numbers show eleven events delivered, and every delivery answered with a success."),
            leo("So the function ran and did nothing? That is worse."),
            maya("It is, and it is our second clue, but hold it. Look at Monday morning first, before the code was touched. The host's log says one thing, eleven times over: the setting PhotoStorage does not exist."),
            leo("The host being?"),
            maya("The Functions host, the program Azure runs around your code. It watches each function's trigger, the thing that starts it, and calls your Python function when that fires. Ours is an Event Grid trigger."),
            leo("And PhotoStorage?"),
            maya("The name of an app setting, a named value Azure hands the app while it runs. The function's blob input binding points at it, and a binding is a connection you declare instead of code you write, like plumbing already fitted."),
            leo("Instead of the storage library, you mean."),
            maya("Instead of any of it. No SDK code, and an SDK is a software development kit, a service's own library: a client, a credential, a call, a tidy-up. The photo arrives as a parameter instead."),
            leo("Then somebody deleted the setting."),
            maya("Nobody deleted it. It had never been created. It lived in local settings dot json on a laptop, and that file is never deployed: Core Tools, the func command, publishes the code and leaves it behind."),
            leo("[sigh] So it worked on the laptop."),
            maya("Beautifully. Every value in that file has to exist in Azure as well, as an app setting."),
            leo("Suspect three. Somebody pasted the storage password into the setting and mistyped it."),
            maya("There is no password to paste. The setting holds the storage address, with blob service URI on the end of its name, and the function app has a managed identity: a badge Azure gives it so it proves who it is with no password."),
            leo("The same badge the Zaiqa API, the .NET service the phone talks to, uses for Cosmos. Fine. Monday afternoon, the setting is added. And still grey boxes."),
            maya("Still grey boxes. And now the second clue earns its keep: eleven events, eleven successes, no thumbnails. What does that tell you, Detective?"),
            leo("That it ran, finished happily, and did no work."),
            maya("And why would it finish happily?"),
            leo("[chuckle] Because somebody caught every error and wrote it politely to the log."),
            maya("Exactly that. A try and an except around the whole function, one tidy log line, and a clean return. Event Grid is told success, so it never retries, and nobody is ever woken up."),
            leo("And if the error escapes?"),
            maya("The run is marked failed, Event Grid retries it for up to a day, and the failures show up where somebody looks. Let it raise. Catch only what you can actually handle."),
            leo("What was the error underneath all that politeness?"),
            maya("The imaging library was missing from the requirements file, so every run threw on its very first line. Eleven tidy log entries nobody read, and eleven grey boxes."),
            leo("Case nearly closed. But there is a second file on your desk."),
            maya("There is. The dashboard has a rebuild all thumbnails button, which calls a web address on that same function app. For this restaurant, with two hundred dishes, it came back with an error after about four minutes."),
            leo("Suspect: it timed out, and somebody must raise the limit."),
            maya("You cannot. An HTTP function has about two hundred and thirty seconds to answer, and that ceiling belongs to the platform in front of it, not to a setting."),
            leo("Then how is a long job ever done behind a web address?"),
            maya("You answer at once and work afterwards. The function takes the request, starts the job, and replies accepted with an address the dashboard can check for progress."),
            leo("And the work itself? Two hundred photos, four steps each."),
            maya("Durable Functions, an extension where one function is the recipe and the others are the steps. The recipe fans two hundred thumbnails out, waits for them all, and carries on if the machine restarts underneath it."),
            leo("A plain function cannot do that?"),
            maya("A plain function dies with its machine. The recipe writes its progress to storage and is replayed, so a restart picks up where it stopped instead of starting again."),
            leo("Last file. Is it the bill, or the network?"),
            maya("The plan, which is both, and the exam loves it. The photo app was on the older Consumption plan: it scales to zero, which we want, but it cannot join a virtual network, and nothing on it may run past ten minutes."),
            leo("And we need the private network because?"),
            maya("Because the rebuild job reads the payouts ledger, which nothing reaches from the open internet. So: Flex Consumption. It scales to zero, joins a virtual network, and can keep a few copies always ready."),
            leo("Always ready meaning what, exactly?"),
            maya("No waiting for a cold start, which is the pause while a sleeping app wakes. If nothing may ever wait, that is Premium: always warm, billed even when idle, thirty minutes a run by default. Or an App Service plan, if you already rent the servers."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. A trigger in the v2 programming model is a decorator in function app dot py, not function dot json. A setting that works locally and is missing in Azure means add an app setting. Writing to a store with no SDK code is an output binding."),
            leo("And the two from the second file?"),
            maya("Scale to zero inside a virtual network is Flex Consumption, and no cold start at all is Premium. A long job behind a web address is accepted now, with Durable or a queue behind it."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate it, calmly. Run func init and add an HTTP function with app dot route. Call it locally after func start. Create a Flex Consumption app and publish to it. Then add an app setting in Azure, and read it in Python with os dot environ."),
            leo("Eleven thumbnails, and not one tidy except. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-functions",
        listOf(
            maya("Okay, did you hear what happened to Zaiqa's photo function this week? Sit down. The little Python program that makes the thumbnails had five days, and five different people found five different ways to break it."),
            leo("[laugh] I heard the words grey box shouted across the office. Start at the beginning. What does it actually do?"),
            maya("A restaurant uploads a dish photo from the partner dashboard. The file lands in Blob Storage, Event Grid pushes the news to the function, and the function makes the thumbnail: the small copy the menu list shows."),
            leo("And the search side? The taste service needs something out of it."),
            maya("The same run writes the dish vector into Cosmos, and a vector is just a list of numbers that captures what a dish is like. Nobody waits for any of it. Right. Monday."),
            leo("Monday is always Tom."),
            maya("Monday was Tom. Tom wrote the whole thing on his laptop, ran it with func start, watched eleven thumbnails appear, and published it. In Azure every single run failed: the setting PhotoStorage does not exist."),
            leo("Had he forgotten to add it?"),
            maya("He had added it, to local settings dot json. Which is never deployed. Core Tools, the func command, publishes the code and leaves that file behind, so every value in it has to exist in Azure too, as an app setting."),
            leo("[sigh] Poor Tom. Tuesday?"),
            maya("Dana. Dana fixed Tom's setting, helpfully, by pasting the storage account's whole connection string into it: an address with the password baked in, sitting in a setting half the company can read."),
            leo("Is that not what the setting is for?"),
            maya("Not here. The function app has a managed identity, a badge Azure gives it so it proves who it is with no password. The setting holds only the storage address, with blob service URI on the end of its name, and the badge gets a read role on the photos."),
            leo("So Dana's password was never needed at all."),
            maya("It was not. It is rotated now, and we do not speak of it. Wednesday was Marcus."),
            leo("What did Marcus do?"),
            maya("Marcus added the Cosmos write. Thirty lines of it: a client, a credential, the container, the call, the tidy-up. An SDK is a service's own library, a software development kit, and he used every inch of it."),
            leo("And there is a shorter way, or this would not be gossip."),
            maya("One decorator. A Cosmos output binding, where a binding is a connection you declare instead of code you write, like plumbing already fitted. Set one value, and the host writes the record when the function ends."),
            leo("A decorator being the sticky note above the function?"),
            maya("That is it: a line starting with the at sign, just above a Python function. In the v2 programming model, the newer way, every function sits in function app dot py with its trigger decorator on top, and there is no function dot json at all."),
            leo("Thursday. Let me guess. The intern."),
            maya("[chuckle] The intern. Bless them. They wrapped the entire function in a try and an except, logged the error beautifully, and returned as though nothing had happened. Every run a success, for ever."),
            leo("That sounds responsible."),
            maya("It sounds it. But Event Grid is told the run succeeded, so it never retries, and nothing ever appears as failed. Eleven dishes went out with grey boxes while the log filled with tidy little messages nobody read."),
            leo("And if the intern had let it raise?"),
            maya("The run is marked failed, Event Grid retries it for up to a day, and somebody gets told. Catch what you can actually handle, and nothing else."),
            leo("And Friday? There is always a Friday."),
            maya("Friday was Priya, and Priya does not make mistakes, which is why this one is delicious. She built the rebuild all thumbnails button for the dashboard: a web address on the same function app, two hundred dishes behind it."),
            leo("And it timed out."),
            maya("After about four minutes. An HTTP function has roughly two hundred and thirty seconds to answer, and that ceiling is the platform in front of it, not a setting anyone can raise."),
            leo("What should she have built?"),
            maya("What she shipped on Monday instead. The web address takes the request, starts the job, and replies accepted straight away, with an address the dashboard can check for progress."),
            leo("And the two hundred photos themselves?"),
            maya("Durable Functions: an extension where one function is the recipe and the others are the steps. It fans two hundred thumbnails out, waits for them all, and carries on if the machine restarts underneath it."),
            leo("That is four people. You promised five."),
            maya("The fifth was a meeting, which is how the photo app ended up on the older Consumption plan. It scales to zero, which we want, but it cannot join a virtual network, and nothing on it may run past ten minutes."),
            leo("And the rebuild job needs the private network, I suppose."),
            maya("It reads the payouts ledger, so yes. We moved to Flex Consumption, the default for new serverless apps: scales to zero, joins a virtual network, and keeps a few copies always ready to cut the cold start, which is the pause while a sleeping app wakes."),
            leo("And if a cold start is never allowed?"),
            maya("Then Premium: always warm, billed per copy even when idle, thirty minutes a run by default. Or an App Service plan, if you already rent the servers. Neither of those scales to zero."),
            leo("Does a cold start even matter for photos?"),
            maya("Not at all. Nobody stares at a thumbnail. It matters for what a diner waits on, which is why the surge fee lives in the Zaiqa API, the .NET service the phone talks to, and not in a function."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. A trigger in the v2 programming model is a decorator, never function dot json. A setting that works locally and is missing in Azure means the local file was not deployed. Writing to a store with no SDK code is an output binding."),
            leo("And Priya's two?"),
            maya("Scale to zero inside a virtual network is Flex Consumption, and no cold start at all is Premium. A long job behind a web address is accepted now, with Durable behind it."),
            leo("And something to try today, so I am not next week's story?"),
            maya("Run func init and add an HTTP function with app dot route. Call it locally after func start. Create a Flex Consumption app and publish to it. Then add an app setting in Azure, and read it in Python with os dot environ."),
            leo("Five people, five lessons, no grey boxes. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-functions",
        listOf(
            nova("Hey, you. It is late, the kitchen has closed, and the riders have gone home. [pause] Tonight, the quietest thing in Zaiqa: the little program that only wakes when a photograph lands."),
            ivy("A program that sleeps all day. My kind of colleague. Go on, Nova."),
            nova("Zaiqa's API, the .NET service the phone talks to, lives in Container Apps and never sleeps. Orders are in Azure SQL, dish photos are files in Blob Storage. [pause] Tonight is about the one part that is allowed to switch itself off."),
            ivy("The photo function. Tell me what it does, slowly."),
            nova("A restaurant uploads a dish photo from the partner dashboard. The file lands in Blob Storage, and a small Python program wakes up and makes the thumbnail: the little copy the menu list shows."),
            ivy("And anything else, while it is up?"),
            nova("It writes the dish vector into Cosmos, and a vector is just a list of numbers that captures what a dish is like, so the taste service can find something near it. [pause] Then it goes back to sleep."),
            ivy("And it costs nothing while it sleeps?"),
            nova("On the right plan, nothing at all. Azure Functions runs small pieces of code when something happens, and you pay for the time they run. A porch light with a motion sensor: dark and free until somebody walks up."),
            ivy("Mm. And who wakes it? Does something knock?"),
            nova("That is the trigger: the thing that starts a function, like a doorbell. Each function has exactly one. [pause] Ours is an Event Grid trigger, and Event Grid pushes each piece of news straight to the function, the way a notification lands on your phone."),
            ivy("And once it is awake, how does it reach the photo?"),
            nova("With a binding, which is a connection you declare instead of code you write. Plumbing already fitted: you do not lay the pipes, you turn the tap. The photo arrives as a parameter."),
            ivy("And the dish record going out?"),
            nova("The mirror of it. Data coming in is an input binding, data going out is an output binding, and the thing that starts the run is the trigger. Ours: an Event Grid trigger, a blob input binding, a Cosmos output binding."),
            ivy("No library code at all? At this hour I would like that to be true."),
            nova("It is true. No SDK code, and an SDK is a software development kit, a service's own library: a client, a credential, a call, a tidy-up. One line above the function does all of it."),
            ivy("One little line. Describe it to me."),
            nova("A decorator: a line just above a Python function, starting with the at sign, like a sticky note saying what it is for. In the v2 programming model, the newer way, every function lives in one file called function app dot py."),
            ivy("And the old way? There is always an old way."),
            nova("A folder for each function, with a settings file called function dot json beside the code. [pause] That is the v1 model, and in Python v2 that file simply does not exist."),
            ivy("Now the thing that keeps me awake. Passwords."),
            nova("None here, and I say that gently. In the decorator, the connection is not a connection string, an address with a password baked in. It is the name of an app setting: a named value Azure hands your app while it runs."),
            ivy("And inside the setting? Whisper it."),
            nova("Hardly anything. The app has a managed identity, a badge Azure gives it so it proves who it is with no password. The setting holds only the storage address, with blob service URI on the end of its name."),
            ivy("And on my own laptop, at two in the morning?"),
            nova("There the settings live in local settings dot json, and here is the soft little warning. [pause] That file is never deployed. Core Tools, the func command, publishes your code and leaves it behind, so every value must exist in Azure too."),
            ivy("Or it works at home and fails in the cloud."),
            nova("Every team learns that once. Func init makes the project, func start runs it on your machine, and func azure functionapp publish sends the code up."),
            ivy("And where does it live up there?"),
            nova("On a hosting plan, which is how you rent the machines. Flex Consumption is the default for a new app: it scales to zero, so nothing runs and nothing is paid at four in the morning, and it can join a virtual network, a private network only our own resources share."),
            ivy("And the older one, the one in all the questions?"),
            nova("The Consumption plan. It scales to zero as well, but it cannot join a virtual network, and nothing on it may run past ten minutes."),
            ivy("So what does a sleeping app cost me, in time?"),
            nova("A moment to wake. That pause is a cold start. [pause] Flex Consumption can keep a few copies always ready, and if nothing may ever wait, Premium keeps them all warm, bills you even when idle, and gives each run thirty minutes by default."),
            ivy("Does a cold start matter for a photograph?"),
            nova("Not at all. Nobody is watching a thumbnail arrive. It matters for what a diner waits on, which is why the surge fee lives in the API. And there is the App Service plan, if you already rent servers by the month."),
            ivy("Mm. Now tell me where all this stops working."),
            nova("Here. The dashboard wanted one web address that rebuilds two hundred thumbnails. An HTTP function has about two hundred and thirty seconds to answer, and that ceiling is the platform in front of it, not a setting you can raise."),
            ivy("So it gave up after four minutes."),
            nova("It did. The answer is to reply at once: take the request, start the job, say accepted, and hand back an address the dashboard can check for progress."),
            ivy("And something has to do two hundred photos, patiently, in the dark."),
            nova("Durable Functions. An extension where one function is the recipe and the others are the steps: it fans two hundred thumbnails out, waits for them all, and carries on if the machine restarts underneath it."),
            ivy("It remembers where it was. [sigh] I would like that."),
            nova("It writes its progress to storage and replays the recipe, so a restart picks up where it stopped. A plain function cannot: it dies with its machine."),
            ivy("Before you send me to bed. What do I remember for the exam?"),
            nova("Five small things. A trigger in the v2 programming model is a decorator in function app dot py, never function dot json. A setting missing in Azure means the local file was not deployed, so add an app setting. Writing with no SDK code is an output binding."),
            ivy("And the two about machines?"),
            nova("Scale to zero inside a virtual network is Flex Consumption. No cold start at all is Premium. And a long job behind a web address is accepted now, with Durable or a queue behind it."),
            ivy("And something to try today, before I sleep?"),
            nova("Run func init and add an HTTP function with app dot route. Call it locally after func start. [pause] Create a Flex Consumption app and publish to it. Then add an app setting in Azure, and read it in Python with os dot environ."),
            ivy("A light that only comes on for photographs. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-functions",
        listOf(
            nova("Tonight is date night, darling, and I have picked the laziest thing in Zaiqa to talk about. Something that sleeps all day and only wakes when somebody rings."),
            rafe("You have described me on a Sunday. Which part of Zaiqa is that?"),
            nova("The photo function. A restaurant uploads a dish photo from the partner dashboard, the file lands in Blob Storage, and a small Python program wakes up, makes the thumbnail, and goes back to sleep."),
            rafe("The thumbnail being the little copy on the menu list. And the taste service? It wants something too."),
            nova("The same run writes the dish vector into Cosmos, and a vector is just a list of numbers that captures what a dish is like. Now, exam style, love. What wakes it up, and what is that called?"),
            rafe("Event Grid pushes the news, and the thing that starts a function is its trigger. The doorbell. Exactly one each, and ours is an Event Grid trigger."),
            nova("Right answer, and quick with it. I do like a man who answers the door. [kiss]"),
            rafe("My turn. In the v2 programming model, the newer way of writing these, where do you say that a function starts when a photo lands?"),
            nova("In function app dot py, with a decorator: a line just above the function, starting with the at sign, like a little label. No function dot json anywhere; that belonged to the v1 model."),
            rafe("Right answer. Everything properly labelled, which is more than I manage. [kiss]"),
            nova("Then a harder one, and be careful. The function must write that dish record to Cosmos with no SDK code at all. An SDK is a software development kit, a service's own library. What do you add?"),
            rafe("A Cosmos input binding. A binding is a connection you declare instead of code you write, after all."),
            nova("[sigh] No kiss for that one. Data leaving is an output binding. You add a Cosmos output decorator, set the record, and the host writes it when the function ends. Input is for reading in."),
            rafe("Out, not in. Noted, and I will earn it back. The photo itself arrives through a blob input binding, so there is no storage library either."),
            nova("Better. The plumbing is already fitted; we only turn the taps."),
            rafe("Then here is mine, and it is a real one. The app runs perfectly with func start on my laptop, and in Azure every run fails: the setting PhotoStorage does not exist. Why, darling?"),
            nova("Because on your laptop the settings live in local settings dot json, and that file is never deployed. Core Tools, the func command, publishes the code and leaves it behind. Add the value in Azure, as an app setting."),
            rafe("Right answer. What happens on the laptop stays on the laptop. [kiss]"),
            nova("And the setting itself. Is it a connection string, an address with the password baked in?"),
            rafe("Never. The app has a managed identity, a badge Azure gives it so it proves who it is with no password. The setting holds only the storage address, with blob service URI on the end of its name."),
            nova("Right answer. No secrets between us. [kiss]"),
            rafe("And the badge gets a role, I hope. A named permission, nothing broader."),
            nova("A read role on the photos, and a write role on the one Cosmos container. Never more than the relationship needs. Now, plans, which is how we rent the machines."),
            rafe("Ask me properly, then."),
            nova("Exam style. A new serverless app must scale to zero when nobody is uploading, and it must reach the payouts ledger inside a virtual network. Which plan?"),
            rafe("A virtual network being a private network only our own things share. That is Flex Consumption: the default for new apps, scales to zero, joins the network. The older Consumption plan cannot join one, and an App Service plan never scales to zero."),
            nova("Right answer, all three. Private, and never more than we need. [kiss]"),
            rafe("And where does the romance end? There is always a limit."),
            nova("At the cold start, which is the pause while a sleeping app wakes. Flex Consumption keeps a few copies always ready; if nothing may ever wait, it is Premium, always warm, billed even when idle, and thirty minutes a run by default."),
            rafe("Nobody waits on a thumbnail, so we let ours sleep. Last question from me, love, and it caught the partner team last week."),
            nova("Go on."),
            rafe("One web address rebuilds two hundred thumbnails for a restaurant. It fails after about four minutes. What is the limit, and what do we build instead?"),
            nova("About two hundred and thirty seconds for an HTTP function to answer, and it is the platform in front of it, not a setting we can raise. So reply accepted at once, and do the work behind it with Durable Functions."),
            rafe("Right answer. Say yes quickly, then take your time. [kiss]"),
            nova("Durable being the extension where one function is the recipe and the others are the steps. It fans two hundred thumbnails out, waits for them all, and carries on if the machine restarts underneath it."),
            rafe("Because it writes its progress down and replays it. Unlike a plain function, which dies with its machine."),
            nova("Exactly. So, for the exam, five things. What do we keep?"),
            rafe("A trigger in the v2 programming model is a decorator, never function dot json. A missing setting in Azure means the local file was not deployed. Data leaving with no SDK code is an output binding."),
            nova("Two more, and then pudding."),
            rafe("Scale to zero inside a virtual network is Flex Consumption. No cold start at all is Premium. And a long job behind a web address is accepted now, with Durable behind it."),
            nova("That is six, and every one of them right. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Run func init and add an HTTP function with app dot route. Call it locally after func start. Create a Flex Consumption app and publish to it. Then add an app setting in Azure, and read it in Python with os dot environ."),
            rafe("Asleep until you ring. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun photoFlow() = DiagramSpec(
        "A restaurant uploads a dish photo, Event Grid pushes the news, and the photo function writes the thumbnail and the dish vector.",
        nodes = listOf(
            DNode("dash", "Dashboard", 0.12f, 0.2f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
            DNode("blob", "Blob Storage", 0.45f, 0.2f, w = 0.34f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "dish photos"),
            DNode("grid", "Event Grid", 0.82f, 0.2f, w = 0.3f, h = 0.22f, tone = 1, sub = "photo uploaded"),
            DNode("fn", "Photo function", 0.82f, 0.62f, w = 0.3f, h = 0.2f, sub = "Python"),
            DNode("cosmos", "Cosmos DB", 0.45f, 0.62f, w = 0.34f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2, sub = "dish vector"),
        ),
        edges = listOf(
            DEdge("dash", "blob", "upload"),
            DEdge("blob", "grid", "event"),
            DEdge("grid", "fn", "pushes"),
            DEdge("fn", "cosmos", "output"),
            DEdge("fn", "blob", "thumbnail", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun overview() = DiagramSpec(
        "Triggers start the functions declared in function_app.py; input and output bindings move data in and out without SDK code.",
        nodes = listOf(
            DNode("http", "HTTP request", 0.17f, 0.15f, w = 0.3f, h = 0.16f, kind = NodeKind.PILL, tone = 1),
            DNode("eg", "Photo uploaded", 0.5f, 0.15f, w = 0.3f, h = 0.16f, kind = NodeKind.PILL, tone = 1),
            DNode("sb", "Queue message", 0.83f, 0.15f, w = 0.3f, h = 0.16f, kind = NodeKind.PILL, tone = 1),
            DNode("app", "function_app.py", 0.5f, 0.48f, w = 0.5f, h = 0.22f, sub = "decorators"),
            DNode("blob", "Blob input", 0.2f, 0.82f, w = 0.34f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2),
            DNode("cosmos", "Cosmos output", 0.8f, 0.82f, w = 0.34f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2),
        ),
        edges = listOf(
            DEdge("eg", "app", "trigger"),
            DEdge("http", "app"),
            DEdge("sb", "app"),
            DEdge("blob", "app", "input"),
            DEdge("app", "cosmos", "output"),
        ),
        aspect = 0.8f,
    )

    private fun triggers() = DiagramSpec(
        "Four triggers the exam names: a pushed event, a web request, a job on a queue and a schedule, one to a function.",
        nodes = listOf(
            DNode("eg", "Event Grid", 0.26f, 0.15f, w = 0.44f, h = 0.2f, tone = 1, sub = "a pushed event"),
            DNode("http", "HTTP", 0.76f, 0.15f, w = 0.44f, h = 0.2f, tone = 1, sub = "a web request"),
            DNode("sb", "Service Bus", 0.26f, 0.48f, w = 0.44f, h = 0.2f, tone = 1, sub = "a job on a queue"),
            DNode("timer", "Timer", 0.76f, 0.48f, w = 0.44f, h = 0.2f, tone = 1, sub = "on a schedule"),
            DNode("one", "One trigger each", 0.5f, 0.84f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE),
        ),
        aspect = 0.8f,
    )

    private fun settings() = DiagramSpec(
        "local.settings.json stays on your laptop; in Azure the binding's connection names an app setting, which can be identity-based.",
        nodes = listOf(
            DNode("laptop", "Your laptop", 0.25f, 0.16f, w = 0.44f, h = 0.2f, tone = 1, sub = "func start"),
            DNode("azure", "Function app", 0.75f, 0.16f, w = 0.44f, h = 0.2f, sub = "in Azure"),
            DNode("local", "local.settings.json", 0.25f, 0.5f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "never deployed"),
            DNode("appset", "App setting", 0.75f, 0.5f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "PhotoStorage"),
            DNode("mi", "Managed identity", 0.75f, 0.84f, w = 0.44f, h = 0.2f, tone = 2, sub = "no secret stored"),
        ),
        edges = listOf(
            DEdge("laptop", "local"),
            DEdge("laptop", "azure", "publish code"),
            DEdge("azure", "appset"),
            DEdge("appset", "mi", "identity-based"),
        ),
        aspect = 0.8f,
    )

    private fun plans() = DiagramSpec(
        "Flex Consumption is the default serverless plan; Premium stays warm, an App Service plan never sleeps, and Consumption has no virtual network.",
        nodes = listOf(
            DNode("flex", "Flex Consumption", 0.5f, 0.16f, w = 0.7f, h = 0.2f, sub = "scale to zero, virtual network"),
            DNode("premium", "Premium", 0.26f, 0.5f, w = 0.44f, h = 0.2f, tone = 1, sub = "always warm, 30 minutes"),
            DNode("appsvc", "App Service plan", 0.76f, 0.5f, w = 0.44f, h = 0.2f, tone = 1, sub = "never scales to zero"),
            DNode("legacy", "Consumption", 0.5f, 0.84f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "no virtual network"),
        ),
        aspect = 0.8f,
    )

    private fun durable() = DiagramSpec(
        "A long job answers at once and runs behind the request: the recipe fans out the steps and writes down where it got to.",
        nodes = listOf(
            DNode("http", "HTTP request", 0.2f, 0.16f, w = 0.36f, h = 0.2f, tone = 1, sub = "rebuild all"),
            DNode("accept", "Accepted at once", 0.72f, 0.16f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "230 second limit"),
            DNode("recipe", "The recipe", 0.5f, 0.5f, w = 0.5f, h = 0.2f, sub = "one function"),
            DNode("steps", "Two hundred steps", 0.26f, 0.84f, w = 0.46f, h = 0.2f, tone = 2, sub = "thumbnails"),
            DNode("state", "Progress saved", 0.76f, 0.84f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "survives a restart"),
        ),
        edges = listOf(
            DEdge("http", "accept", "reply"),
            DEdge("http", "recipe", "starts"),
            DEdge("recipe", "steps", "fans out"),
            DEdge("recipe", "state"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Functions questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("v2", "Trigger in Python v2", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a decorator"),
            DNode("missing", "Missing in Azure", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "add an app setting"),
            DNode("send", "Write with no SDK", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "an output binding"),
            DNode("durable", "Too long for HTTP", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "accept now, Durable"),
            DNode("vnet", "Zero, in a VNet", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Flex Consumption"),
            DNode("warm", "No cold start", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Premium"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("v2", "missing", "send", "durable", "vnet", "warm")
    private val egTrigger = BoardCard("Python", "@app.event_grid_trigger(arg_name=\"event\")", code = true)
    private val blobIn = BoardCard("Python", "@app.blob_input(arg_name=\"photo\", path=\"dishes\", connection=\"PhotoStorage\")", code = true)
    private val today = BoardCard("Today", "func init, an HTTP function, func start, a Flex app, an app setting.")
    private val plansCard = BoardCard("And the plans", "Zero in a network: Flex. Never cold: Premium. Long job: Durable.")
    private val raiseCard = BoardCard("Let it raise", "Marked failed, retried for up to a day, and somebody is told.")
    private val restartCard = BoardCard("Survives a restart", "Progress written down, the recipe replayed, not restarted.")
    private val limitCard = BoardCard("230 seconds", "The platform in front of the function, not a setting to raise.")
    private val acceptCard = BoardCard("Accept now, work after", "Reply accepted, with an address to check for progress.")
    private val durableCard = BoardCard("Durable Functions", "One function is the recipe, the others the steps.")
    private val legacyCard = BoardCard("Consumption", "Scales to zero, but no virtual network, and ten minutes a run.")

    private fun classicBoard() = PodcastBoard(
        "ai-functions",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa's API ... the photo function."
            Scene(
                0, "The last piece", photoFlow(),
                beats = listOf(
                    Beat(0, show = setOf("dash", "blob"), light = setOf("blob"), pulse = "dash" to "blob", card = BoardCard("Where we are", "The API in Container Apps, orders in SQL, photos in Blob Storage.")),
                    Beat(4, show = setOf("fn"), light = setOf("fn"), card = BoardCard("Azure Functions", "Code that runs when something happens; you pay for the time.")),
                    Beat(6, show = setOf("grid"), light = setOf("grid"), pulse = "blob" to "grid", card = BoardCard("Nobody waits", "The photo lands, Event Grid pushes the news, the screen says saved.")),
                    Beat(8, show = setOf("cosmos"), light = setOf("fn", "cosmos"), pulse = "fn" to "cosmos", card = BoardCard("Two jobs", "The thumbnail for the menu list, and the dish vector into Cosmos.")),
                ),
            ),
            // 9: "Right. So how does the function know a photo arrived?"
            Scene(
                9, "The doorbell", triggers(),
                beats = listOf(
                    Beat(10, show = setOf("eg", "http", "sb", "timer"), light = setOf("eg"), card = BoardCard("Trigger", "What starts a function: the doorbell. Ours is an Event Grid trigger.")),
                    Beat(11, show = setOf("one"), light = setOf("one"), card = BoardCard("One each", "A function has exactly one trigger, whatever else it connects to.")),
                ),
            ),
            // 12: "It could, with the storage library. We use a binding instead."
            Scene(
                12, "Bindings", overview(),
                beats = listOf(
                    Beat(12, show = setOf("app", "blob"), light = setOf("blob"), pulse = "blob" to "app", card = BoardCard("Binding", "A declared connection: the photo arrives as a parameter.")),
                    Beat(14, show = setOf("eg", "cosmos"), light = setOf("eg", "cosmos"), pulse = "app" to "cosmos", card = BoardCard("In, out, trigger", "Input reads data in, output sends it out, the trigger starts it.")),
                    Beat(16, show = setOf("http", "sb"), light = setOf("app"), card = BoardCard("No SDK code", "No client, no credential, no call: one line above the function.")),
                ),
            ),
            // 17: "Which line?"
            Scene(
                17, "The v2 programming model", overview(),
                beats = listOf(
                    Beat(18, light = setOf("app"), card = BoardCard("function_app.py", "One file, a decorator per trigger and binding. No function.json.")),
                    Beat(20, light = setOf("eg", "app"), pulse = "eg" to "app", card = egTrigger),
                ),
            ),
            // 21: "Now the part I always get wrong. Where does the storage password go?"
            Scene(
                21, "Connections and settings", settings(),
                beats = listOf(
                    Beat(22, show = setOf("azure", "appset"), light = setOf("appset"), pulse = "azure" to "appset", card = BoardCard("Names an app setting", "The connection names a setting, never a connection string.")),
                    Beat(24, show = setOf("mi"), light = setOf("mi"), pulse = "appset" to "mi", card = BoardCard("Identity-based", "The setting holds only the address; the badge does the proving.")),
                    Beat(26, show = setOf("laptop", "local"), light = setOf("local"), pulse = "laptop" to "local", card = BoardCard("local.settings.json", "For your machine only. It is never deployed.")),
                    Beat(28, light = setOf("laptop"), pulse = "laptop" to "azure", card = BoardCard("Core Tools", "func init, func start, func azure functionapp publish.")),
                ),
            ),
            // 29: "Where does it run up there? Do I pick a machine?"
            Scene(
                29, "Hosting plans", plans(),
                beats = listOf(
                    Beat(30, show = setOf("flex"), light = setOf("flex"), card = BoardCard("Flex Consumption", "The default: scale to zero, and a virtual network.")),
                    Beat(32, show = setOf("legacy"), light = setOf("legacy"), card = legacyCard),
                    Beat(34, show = setOf("premium"), light = setOf("premium"), card = BoardCard("Cold start", "The pause while a sleeping app wakes. Premium stays warm.")),
                    Beat(38, show = setOf("appsvc"), light = setOf("appsvc"), card = BoardCard("App Service plan", "Servers you already rent. It never scales to zero.")),
                ),
            ),
            // 39: "The partner team wanted a web address that rebuilds every thumbnail."
            Scene(
                39, "A job too long for HTTP", durable(),
                beats = listOf(
                    Beat(40, show = setOf("http", "accept"), light = setOf("accept"), pulse = "http" to "accept", card = limitCard),
                    Beat(42, show = setOf("recipe"), light = setOf("recipe"), pulse = "http" to "recipe", card = acceptCard),
                    Beat(44, show = setOf("steps"), light = setOf("steps"), pulse = "recipe" to "steps", card = durableCard),
                    Beat(46, show = setOf("state"), light = setOf("state"), pulse = "recipe" to "state", card = restartCard),
                ),
            ),
            // 47: "So what do I remember for the exam?"
            Scene(
                47, "For the exam", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("v2", "missing", "send"), card = BoardCard("Listen for the phrase", "Each phrase points to one answer: decorator, setting, binding.")),
                    Beat(50, light = setOf("vnet", "warm", "durable"), card = plansCard),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-functions--case-files",
        listOf(
            // 0: "Today's case: The Dish That Never Appeared."
            Scene(
                0, "The Dish That Never Appeared", photoFlow(),
                beats = listOf(
                    Beat(0, show = setOf("dash", "blob"), light = setOf("blob"), pulse = "dash" to "blob", card = BoardCard("The case", "Eleven dishes went live with a grey box where the photo should be.")),
                    Beat(2, show = setOf("cosmos"), light = setOf("cosmos"), card = BoardCard("What is missing", "The files are in Blob Storage. No thumbnail, and no dish record.")),
                    Beat(4, show = setOf("fn"), light = setOf("fn"), card = BoardCard("The photo function", "Python on Azure Functions: the thumbnail and the dish vector.")),
                    Beat(6, show = setOf("grid"), light = setOf("grid"), pulse = "grid" to "fn", card = BoardCard("Not Event Grid", "Eleven events delivered, and every one answered with a success.")),
                ),
            ),
            // 8: "Look at Monday morning first... the setting PhotoStorage does not exist."
            Scene(
                8, "The first clue", settings(),
                beats = listOf(
                    Beat(8, show = setOf("azure", "appset"), light = setOf("appset"), pulse = "azure" to "appset", card = BoardCard("PhotoStorage missing", "The host's log, eleven times over: the setting does not exist.")),
                    Beat(12, light = setOf("appset"), card = blobIn),
                    Beat(16, show = setOf("laptop", "local"), light = setOf("local"), pulse = "laptop" to "local", card = BoardCard("local.settings.json", "It lived on a laptop, and that file is never deployed.")),
                    Beat(18, light = setOf("laptop"), pulse = "laptop" to "azure", card = BoardCard("Core Tools", "Publishes the code and leaves the settings file behind.")),
                    Beat(20, show = setOf("mi"), light = setOf("mi"), pulse = "appset" to "mi", card = BoardCard("No password to paste", "A managed identity; the setting holds only the address.")),
                ),
            ),
            // 22: "Eleven events, eleven successes, no thumbnails."
            Scene(
                22, "The second clue", photoFlow(),
                beats = listOf(
                    Beat(22, show = setOf("grid", "fn"), light = setOf("fn"), pulse = "grid" to "fn", card = BoardCard("Eleven successes", "It ran, finished happily, and did no work at all.")),
                    Beat(26, light = setOf("fn"), card = BoardCard("The tidy except", "Catch everything, return cleanly, and the run counts as a success.")),
                    Beat(28, light = setOf("grid"), pulse = "grid" to "fn", card = raiseCard),
                ),
            ),
            // 31: "But there is a second file on your desk."
            Scene(
                31, "The second file", durable(),
                beats = listOf(
                    Beat(32, show = setOf("http"), light = setOf("http"), card = BoardCard("Rebuild all thumbnails", "One web address, two hundred dishes behind it.")),
                    Beat(34, show = setOf("accept"), light = setOf("accept"), pulse = "http" to "accept", card = limitCard),
                    Beat(36, show = setOf("recipe"), light = setOf("recipe"), pulse = "http" to "recipe", card = acceptCard),
                    Beat(38, show = setOf("steps"), light = setOf("steps"), pulse = "recipe" to "steps", card = durableCard),
                    Beat(40, show = setOf("state"), light = setOf("state"), pulse = "recipe" to "state", card = restartCard),
                ),
            ),
            // 41: "Last file. Is it the bill, or the network?"
            Scene(
                41, "The last witness", plans(),
                beats = listOf(
                    Beat(42, show = setOf("legacy"), light = setOf("legacy"), card = legacyCard),
                    Beat(44, show = setOf("flex"), light = setOf("flex"), card = BoardCard("Flex Consumption", "Zero, a virtual network, and a few copies always ready.")),
                    Beat(46, show = setOf("premium", "appsvc"), light = setOf("premium"), card = BoardCard("Premium", "Always warm: no cold start, billed when idle, thirty minutes.")),
                ),
            ),
            // 47: "So, the case summary for the exam?"
            Scene(
                47, "Case closed", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("v2", "missing", "send"), card = BoardCard("Listen for the phrase", "Settings, connections, bindings, plans: one answer each.")),
                    Beat(50, light = setOf("vnet", "warm", "durable"), card = plansCard),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-functions--gossip",
        listOf(
            // 0: "Did you hear what happened to Zaiqa's photo function this week?"
            Scene(
                0, "Five days, five people", photoFlow(),
                beats = listOf(
                    Beat(0, show = setOf("dash", "blob", "fn"), light = setOf("fn"), pulse = "dash" to "blob", card = BoardCard("The photo function", "The little program that makes the thumbnails, broken five ways.")),
                    Beat(2, show = setOf("grid"), light = setOf("grid"), pulse = "grid" to "fn", card = BoardCard("What it does", "A photo lands, Event Grid pushes the news, the thumbnail is made.")),
                    Beat(4, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "fn" to "cosmos", card = BoardCard("And the search side", "The same run writes the dish vector into Cosmos.")),
                ),
            ),
            // 5: "Monday is always Tom."
            Scene(
                5, "Monday: Tom", settings(),
                beats = listOf(
                    Beat(6, show = setOf("laptop", "local"), light = setOf("laptop"), pulse = "laptop" to "local", card = BoardCard("Worked on the laptop", "Eleven thumbnails with func start, then every run failed in Azure.")),
                    Beat(8, show = setOf("azure", "appset"), light = setOf("local"), pulse = "laptop" to "azure", card = BoardCard("local.settings.json", "Never deployed. Every value must exist in Azure as an app setting.")),
                ),
            ),
            // 9: "Poor Tom. Tuesday?"
            Scene(
                9, "Tuesday: Dana", settings(),
                beats = listOf(
                    Beat(10, show = setOf("azure", "appset"), light = setOf("appset"), pulse = "azure" to "appset", card = BoardCard("A connection string pasted in", "The secret went where the setting's name should go.")),
                    Beat(12, show = setOf("mi"), light = setOf("mi"), pulse = "appset" to "mi", card = BoardCard("Identity-based", "The setting holds only the address; the badge does the proving.")),
                ),
            ),
            // 14: "Wednesday was Marcus."
            Scene(
                14, "Wednesday: Marcus", overview(),
                beats = listOf(
                    Beat(16, show = setOf("app"), light = setOf("app"), card = BoardCard("Thirty lines of SDK code", "A client, a credential, the container, the call, the tidy-up.")),
                    Beat(18, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "app" to "cosmos", card = BoardCard("Output binding", "Set one value; the host writes the record when the function ends.")),
                    Beat(20, show = setOf("eg", "http", "sb", "blob"), light = setOf("app"), card = BoardCard("function_app.py", "One file, a decorator per trigger. No function.json at all.")),
                ),
            ),
            // 21: "Thursday. Let me guess. The intern."
            Scene(
                21, "Thursday: the intern", photoFlow(),
                beats = listOf(
                    Beat(22, show = setOf("fn"), light = setOf("fn"), card = BoardCard("Never failed. Not once.", "Every error caught, logged beautifully, and returned as a success.")),
                    Beat(24, show = setOf("grid"), light = setOf("grid"), pulse = "grid" to "fn", card = BoardCard("So it never retried", "Eleven dishes kept their grey boxes, and nothing looked wrong.")),
                    Beat(26, light = setOf("grid"), card = raiseCard),
                ),
            ),
            // 27: "And Friday? There is always a Friday."
            Scene(
                27, "Friday: Priya", durable(),
                beats = listOf(
                    Beat(28, show = setOf("http"), light = setOf("http"), card = BoardCard("Rebuild all thumbnails", "One web address, two hundred dishes behind it.")),
                    Beat(30, show = setOf("accept"), light = setOf("accept"), pulse = "http" to "accept", card = limitCard),
                    Beat(32, show = setOf("recipe"), light = setOf("recipe"), pulse = "http" to "recipe", card = acceptCard),
                    Beat(34, show = setOf("steps", "state"), light = setOf("steps"), pulse = "recipe" to "steps", card = durableCard),
                ),
            ),
            // 35: "That is four people. You promised five."
            Scene(
                35, "The fifth: a meeting", plans(),
                beats = listOf(
                    Beat(36, show = setOf("legacy"), light = setOf("legacy"), card = legacyCard),
                    Beat(38, show = setOf("flex"), light = setOf("flex"), card = BoardCard("Flex Consumption", "Zero, a virtual network, and a few copies always ready.")),
                    Beat(40, show = setOf("premium", "appsvc"), light = setOf("premium"), card = BoardCard("Premium", "Always warm, billed when idle, thirty minutes a run by default.")),
                ),
            ),
            // 43: "So, the gossip I should remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("v2", "missing", "send"), card = BoardCard("Listen for the phrase", "One colleague, one lesson, and one answer each.")),
                    Beat(46, light = setOf("vnet", "warm", "durable"), card = plansCard),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-functions--late-night",
        listOf(
            // 0: "It is late... the quietest thing in Zaiqa."
            Scene(
                0, "The quietest thing", photoFlow(),
                beats = listOf(
                    Beat(0, show = setOf("dash", "blob"), light = setOf("blob"), card = BoardCard("Tonight", "The one part of Zaiqa allowed to switch itself off.")),
                    Beat(4, show = setOf("fn", "grid"), light = setOf("fn"), pulse = "dash" to "blob", card = BoardCard("The photo function", "A photo lands, it wakes, makes the thumbnail, and sleeps again.")),
                    Beat(6, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "fn" to "cosmos", card = BoardCard("And the dish vector", "A list of numbers that captures what a dish is like.")),
                    Beat(8, light = setOf("fn"), card = BoardCard("Azure Functions", "Code that runs when something happens; you pay for the time.")),
                ),
            ),
            // 9: "And who wakes it? Does something knock?"
            Scene(
                9, "Who wakes it", triggers(),
                beats = listOf(
                    Beat(10, show = setOf("eg", "http", "sb", "timer"), light = setOf("eg"), card = BoardCard("Trigger", "What starts a function: the doorbell. Ours is an Event Grid trigger.")),
                    Beat(11, show = setOf("one"), light = setOf("one"), card = BoardCard("One each", "A function has exactly one trigger, whatever else it connects to.")),
                ),
            ),
            // 12: "With a binding, which is a connection you declare..."
            Scene(
                12, "Plumbing already fitted", overview(),
                beats = listOf(
                    Beat(12, show = setOf("app", "blob"), light = setOf("blob"), pulse = "blob" to "app", card = BoardCard("Binding", "A declared connection: the photo arrives as a parameter.")),
                    Beat(14, show = setOf("eg", "cosmos"), light = setOf("cosmos"), pulse = "app" to "cosmos", card = BoardCard("In, out, trigger", "Input reads data in, output sends it out, the trigger starts it.")),
                    Beat(16, show = setOf("http", "sb"), light = setOf("app"), card = BoardCard("No SDK code", "No client, no credential, no call: one line above the function.")),
                    Beat(18, light = setOf("eg", "app"), pulse = "eg" to "app", card = egTrigger),
                ),
            ),
            // 21: "Now the thing that keeps me awake. Passwords."
            Scene(
                21, "Passwords", settings(),
                beats = listOf(
                    Beat(22, show = setOf("azure", "appset"), light = setOf("appset"), pulse = "azure" to "appset", card = BoardCard("Names an app setting", "The connection names a setting, never a connection string.")),
                    Beat(24, show = setOf("mi"), light = setOf("mi"), pulse = "appset" to "mi", card = BoardCard("Identity-based", "The setting holds only the address; the badge does the proving.")),
                    Beat(26, show = setOf("laptop", "local"), light = setOf("local"), pulse = "laptop" to "local", card = BoardCard("local.settings.json", "For your machine only. It is never deployed.")),
                    Beat(28, light = setOf("laptop"), pulse = "laptop" to "azure", card = BoardCard("Core Tools", "func init, func start, func azure functionapp publish.")),
                ),
            ),
            // 29: "And where does it live up there?"
            Scene(
                29, "Where it lives", plans(),
                beats = listOf(
                    Beat(30, show = setOf("flex"), light = setOf("flex"), card = BoardCard("Flex Consumption", "The default: scale to zero, and a virtual network.")),
                    Beat(32, show = setOf("legacy"), light = setOf("legacy"), card = legacyCard),
                    Beat(34, show = setOf("premium"), light = setOf("premium"), card = BoardCard("Cold start", "The pause while a sleeping app wakes. Premium stays warm.")),
                    Beat(36, show = setOf("appsvc"), light = setOf("appsvc"), card = BoardCard("App Service plan", "Servers you already rent by the month. It never scales to zero.")),
                ),
            ),
            // 37: "Now tell me where all this stops working."
            Scene(
                37, "Where it stops", durable(),
                beats = listOf(
                    Beat(38, show = setOf("http", "accept"), light = setOf("accept"), pulse = "http" to "accept", card = limitCard),
                    Beat(40, show = setOf("recipe"), light = setOf("recipe"), pulse = "http" to "recipe", card = acceptCard),
                    Beat(42, show = setOf("steps"), light = setOf("steps"), pulse = "recipe" to "steps", card = durableCard),
                    Beat(44, show = setOf("state"), light = setOf("state"), pulse = "recipe" to "state", card = restartCard),
                ),
            ),
            // 45: "Before you send me to bed. What do I remember for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("v2", "missing", "send"), card = BoardCard("Listen for the phrase", "Five small things, each phrase pointing to one answer.")),
                    Beat(48, light = setOf("vnet", "warm", "durable"), card = plansCard),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-functions--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", photoFlow(),
                beats = listOf(
                    Beat(0, show = setOf("dash", "blob"), light = setOf("blob"), pulse = "dash" to "blob", card = BoardCard("Date night", "The laziest thing in Zaiqa: it sleeps until somebody rings.")),
                    Beat(2, show = setOf("fn", "grid"), light = setOf("fn"), pulse = "grid" to "fn", card = BoardCard("The photo function", "A photo lands, it wakes, makes the thumbnail, and sleeps again.")),
                    Beat(4, show = setOf("cosmos"), light = setOf("cosmos"), pulse = "fn" to "cosmos", card = BoardCard("And the dish vector", "A list of numbers that captures what a dish is like.")),
                    Beat(5, light = setOf("grid"), card = BoardCard("Quiz: what wakes it?", "Event Grid pushes the news. What is that thing called?")),
                ),
            ),
            // 7: "In the v2 programming model, where do you say that a function starts?"
            Scene(
                7, "Labelled properly", overview(),
                beats = listOf(
                    Beat(7, show = setOf("app", "eg"), light = setOf("app"), pulse = "eg" to "app", card = BoardCard("Quiz: where is the trigger?", "In the v2 model, where do you say a photo starts the function?")),
                    Beat(8, card = egTrigger),
                    Beat(10, show = setOf("cosmos", "blob"), light = setOf("cosmos"), card = BoardCard("Quiz: write to Cosmos", "The dish record, with no SDK code. Which binding do you add?")),
                    Beat(12, light = setOf("cosmos"), pulse = "app" to "cosmos", card = BoardCard("No kiss: an output binding", "Data leaving is an output binding. Input is for reading in.")),
                    Beat(13, show = setOf("http", "sb"), light = setOf("blob"), pulse = "blob" to "app", card = BoardCard("The photo comes in", "A blob input binding, so there is no storage library either.")),
                ),
            ),
            // 15: "The app runs perfectly with func start, and in Azure every run fails."
            Scene(
                15, "What stays on the laptop", settings(),
                beats = listOf(
                    Beat(15, show = setOf("laptop", "azure"), light = setOf("laptop"), pulse = "laptop" to "azure", card = BoardCard("Quiz: missing in Azure", "It works with func start and fails in Azure. Why, darling?")),
                    Beat(16, show = setOf("local"), light = setOf("local"), pulse = "laptop" to "local", card = BoardCard("Right answer", "local.settings.json is never deployed. Add an app setting.")),
                    Beat(18, show = setOf("appset"), light = setOf("appset"), pulse = "azure" to "appset", card = BoardCard("Quiz: a connection string?", "Is the setting an address with the password baked in?")),
                    Beat(19, show = setOf("mi"), light = setOf("mi"), pulse = "appset" to "mi", card = BoardCard("Right answer", "A managed identity; the setting holds only the address.")),
                ),
            ),
            // 23: "Ask me properly, then."
            Scene(
                23, "How we rent the machines", plans(),
                beats = listOf(
                    Beat(24, show = setOf("flex", "legacy", "appsvc"), light = setOf("legacy", "appsvc"), card = BoardCard("Quiz: zero, plus a network", "Scale to zero and reach a private network. Which plan?")),
                    Beat(25, light = setOf("flex"), card = BoardCard("Right answer", "Flex Consumption: scale to zero, and a virtual network.")),
                    Beat(28, show = setOf("premium"), light = setOf("premium"), card = BoardCard("Where the romance ends", "No cold start allowed: Premium, always warm, billed when idle.")),
                ),
            ),
            // 29: "Last question from me, love..."
            Scene(
                29, "Say yes quickly", durable(),
                beats = listOf(
                    Beat(31, show = setOf("http", "accept"), light = setOf("http"), pulse = "http" to "accept", card = BoardCard("Quiz: it fails at four minutes", "Two hundred thumbnails behind one web address. What is the limit?")),
                    Beat(32, light = setOf("accept"), card = limitCard),
                    Beat(34, show = setOf("recipe", "steps"), light = setOf("recipe"), pulse = "recipe" to "steps", card = durableCard),
                    Beat(35, show = setOf("state"), light = setOf("state"), pulse = "recipe" to "state", card = restartCard),
                ),
            ),
            // 36: "So, for the exam, five things. What do we keep?"
            Scene(
                36, "For the exam", exam(),
                beats = listOf(
                    Beat(37, show = all6, light = setOf("v2", "missing", "send"), card = BoardCard("What we keep", "Decorator, app setting, output binding: one phrase, one answer.")),
                    Beat(39, light = setOf("vnet", "warm", "durable"), card = plansCard),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
