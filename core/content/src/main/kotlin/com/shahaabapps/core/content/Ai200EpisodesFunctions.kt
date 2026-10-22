package com.shahaabapps.core.content

/** Azure Functions in Python: triggers, bindings and serverless APIs: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesFunctions {
    private val classic = Podcast(
        "ai-functions",
        listOf(
            maya("Hi, and welcome back. Today is Azure Functions. Picture a porch light with a motion sensor. It stays off and costs nothing. Someone walks up, it switches on, does its job, and goes dark. Functions is that, for your code."),
            leo("A porch light for code. So what does it actually do, in plain words?"),
            maya("It runs small pieces of your code when something happens, and you pay only while they run. People call that serverless. There are still servers, but they're Azure's problem, not yours."),
            leo("And what's the something that happens?"),
            maya("That's the trigger, whatever starts a function, like a doorbell. Each function has exactly one. The commonest is an HTTP trigger: your app sends a web request, the same kind of call your Android app already makes, and the function answers it."),
            leo("So a function can be a tiny web API?"),
            maya("Exactly, a serverless API, which is just a set of web addresses your app calls. Say you're building the Two Tracks Coach, a study assistant in an Android app, with a Python backend on Azure. The phone's search request arrives at an HTTP function."),
            leo("What other doorbells are there?"),
            maya("A Service Bus trigger, for a queue, which is a to-do list of jobs. And here's the direction people get backwards: Service Bus never calls your function."),
            leo("Then who does? Something has to wake it."),
            maya("The Functions host, the program Azure runs around your code. It keeps asking the queue, anything for me? It pulls the next message and calls your function with it."),
            leo("So it's like my app polling a server, except Azure writes the loop for me."),
            maya("Exactly. When your function returns without an error, the host marks the message done."),
            leo("And if the job fails halfway?"),
            maya("If your code throws, the host abandons the message: it goes back on the queue for another try. After too many tries, it moves to the dead-letter queue, a side shelf for failed jobs."),
            leo("So I wrap it all in a try and catch and log the error, like I do in my app?"),
            maya("That's the trap. Catch everything and return quietly, and the host thinks it worked. The message is marked done, and the job is lost."),
            leo("Noted. Next doorbell?"),
            maya("An Event Grid trigger, and this one runs the other way. Event Grid pushes each piece of news, like, a file was uploaded, straight to your function, the way a push notification lands on a phone. Functions even handles the little hello Event Grid insists on first."),
            leo("Any more?"),
            maya("A Cosmos DB trigger. Cosmos DB is Azure's database for documents, each one like a filled-in form. It starts when items are created or changed: the host reads the change feed, a running list of every change, in order, and hands your function each new batch."),
            leo("And how does it know where it got up to in that list?"),
            maya("It keeps its place in a lease container. A container is like a table in that database. The lease container is a small extra one that works like a bookmark: it notes how far through the change feed the trigger has read."),
            leo("A bookmark, so it never loses its page. Okay. A function starts. How does it talk to other services?"),
            maya("With bindings. A binding is a connection you declare, instead of code you write, like plumbing that's already fitted. Data arrives as a parameter, or leaves when you set one, with no SDK code."),
            leo("No SDK code? Remind me what an SDK is."),
            maya("A software development kit: the library you'd normally use, like the Service Bus client in your server code. Client, sender, send. With an output binding, sending to a queue is one line: you set a value, and Functions sends it when your function ends."),
            leo("Output binding. So there's an input one too?"),
            maya("Yes. An input binding reads data in, say a document from Cosmos DB, and hands it to the function as a parameter. Data in is input, data out is output, and what starts it is the trigger. For example, the Coach's search request is an HTTP trigger plus an output binding that puts a job on a queue, and it answers at once: accepted."),
            leo("Where do I write all of this? A settings file for every function?"),
            maya("Not any more. In the v2 programming model for Python, the newer way, everything lives in one file, function app dot py. Each function is a normal Python function with decorators on top: a line just above it, starting with the at sign, like a sticky note saying what it's for."),
            leo("So the sticky notes say what starts it, and what it connects to."),
            maya("Exactly. App dot route makes it an HTTP function. App dot service bus queue trigger makes it start on a queue message. The older v1 model had a separate file per function, function dot json. For Python v2, that file doesn't exist."),
            leo("Now the bit I always get wrong. Connections. Where does the password go?"),
            maya("[pause] Here's the trap. In the decorator, connection equals ServiceBusConnection is not a connection string, an address with a password baked in."),
            leo("Then what is it?"),
            maya("The name of an app setting, a named value Azure hands your app while it runs, like an environment variable on your own server. The binding looks the value up by that name."),
            leo("And what's inside the setting? A password?"),
            maya("Ideally, no secret at all. With a managed identity, a badge Azure gives your app so it proves who it is without a password, the setting holds just your Service Bus address. Then give the badge a Service Bus data role, a named permission. The Key Vault episode covers identities."),
            leo("And on my laptop? There's no badge on my laptop."),
            maya("On your laptop, the same settings live in local settings dot json. And that's the second trap: the file is never deployed, never copied up to Azure. So every setting in it must also be created in Azure, as an app setting."),
            leo("So it works on my laptop, and fails in Azure with a missing setting."),
            maya("[sigh] The classic. Core Tools publishes your code, not that file."),
            leo("Core Tools being?"),
            maya("The func command on your laptop. Func init makes a project, func start runs it locally and lists each function's address, and func azure functionapp publish sends the code up."),
            leo("Where does it run in Azure? Do I pick a machine?"),
            maya("You pick a hosting plan, how you rent the machines. For new serverless apps the default is Flex Consumption. It scales to zero: nobody calling, nothing running, nothing paid. And it can join a virtual network, a private network only your own resources share."),
            leo("And if I hate waiting? Doesn't a sleeping app take a moment to wake up?"),
            maya("That pause is a cold start. Flex Consumption lets you keep a few always-ready copies to cut it. If the rule is no cold start at all, always warm, that's the Premium plan, billed per copy even when idle."),
            leo("And if I already rent my own servers?"),
            maya("Then there's Dedicated. If you already pay for an App Service plan, which is servers rented by the month, your functions can run there. But it never scales to zero."),
            leo("Is there an old plan I should avoid?"),
            maya("The legacy Consumption plan. It also scales to zero, but it can't join a virtual network. So if a question says serverless, scale to zero, but inside a virtual network, the answer is Flex Consumption."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If a Python v2 function needs a trigger, it's a decorator in function app dot py, not function dot json. If it works locally but a setting is missing in Azure, add an app setting; the local file isn't deployed. To send a message without SDK code, an output binding. Serverless inside a virtual network, Flex Consumption. Always warm, Premium."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Run func init and add an HTTP function with app dot route. Call it locally after func start. Create a Flex Consumption app and publish to it. Then add an app setting in Azure, and read it in Python with os dot environ, Python's way to read settings."),
            leo("A porch light for my code that never needs feeding. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-functions",
        listOf(
            maya("Welcome to Case Files. Today's case: The Worker That Never Woke. FlashFox is a small Android flashcard app, with a Python backend, the server code the app talks to, on Azure. Its developer, Sam, moved the slow work into Azure Functions. On his laptop, everything ran perfectly. In Azure, the queue filled up with jobs, and nothing ever touched them."),
            leo("Detective Leo, on the case. First, Azure Functions, in plain words?"),
            maya("Small pieces of code that run when something happens, and you pay for the time they run. Picture a porch light with a motion sensor: dark and free until someone walks up. That's what people mean by serverless. The servers are Azure's to look after, not yours."),
            leo("And under the porch light, what is it really?"),
            maya("A function app is one home for several functions, sharing settings and a plan. Inside it runs the Functions host, a program Azure keeps running for you, watching for each function's trigger."),
            leo("Trigger. In plain words?"),
            maya("The thing that starts a function, like a doorbell. Each function has exactly one. When it rings, the host calls your Python function."),
            leo("So FlashFox has two functions?"),
            maya("Two. An HTTP function: the phone sends a web request, asking for a deck to be prepared. It puts a job on a Service Bus queue, a to-do list of jobs, and answers at once, accepted."),
            leo("And the second?"),
            maya("A worker function with a Service Bus trigger. It should wake for each job on that queue."),
            leo("Then my first suspect: the decorators. They work on the laptop, but Azure ignores them."),
            maya("[pause] Wrong suspect. A decorator is a line just above a Python function, starting with the at sign, that labels what it does. In the v2 programming model, every function lives in function app dot py, with its decorators. They work the same on a laptop and in Azure."),
            leo("Suspect two. Functions can't use Service Bus in Azure. Only on laptops."),
            maya("No, the Service Bus trigger is fully supported. Here's the first clue. The host's log, its diary, in Azure says: the setting ServiceBusConnection is missing. The HTTP function needed that same setting to send its jobs, so it was failing too."),
            leo("A setting. But Sam had it. I saw it on his laptop."),
            maya("Where, exactly, Detective? Think about which file."),
            leo("[chuckle] In local settings dot json. The file the project made when he ran func init."),
            maya("And that's the culprit. That file is for your own machine only, and it is never deployed. Core Tools, the func command, publishes the code and leaves that file behind. So every value in it must also exist in Azure as an app setting, a named value Azure hands the app while it runs."),
            leo("So Sam adds ServiceBusConnection as an app setting, and pastes the connection string in. Case closed?"),
            maya("Not so fast. The connection in the decorator is just the name of a setting. What goes inside it matters. A connection string is an address with a password baked in. That's one more secret to leak and to change."),
            leo("So what goes in instead?"),
            maya("An identity-based connection. The function app gets a managed identity, a badge Azure gives it, so it proves who it is with no password."),
            leo("But my code still needs something to connect with. On my own server I'd keep a password in an environment variable."),
            maya("Here the setting holds only the address of Sam's Service Bus. The badge does the proving. Then the badge gets the data roles it needs, sender and receiver, named permissions like job titles, and nothing broader."),
            leo("Second witness?"),
            maya("The testers. Once the worker woke up, some flashcard decks were never built, though the phone said they had been requested. And the queue said every single job was done."),
            leo("Suspect: Service Bus is losing messages."),
            maya("It isn't. Look at the worker. Sam wrapped the whole function in a catch for every error, logged it, and carried on. When the function ends without an error, the host marks the message done. So failed jobs were marked done, and lost."),
            leo("So let the error escape?"),
            maya("Yes. If the function raises, the message is abandoned and tried again. After too many tries it moves to the dead-letter queue, where someone can look at it. The Service Bus episode covers that queue in depth."),
            leo("How does the host know it finished, under the hood?"),
            maya("The host pulls for you. Service Bus never calls your code. The host keeps asking the queue for the next message, takes it with a lock, so no other copy grabs the same job, and calls your function."),
            leo("Like a loop on my server that polls for work, and I only write the body."),
            maya("Exactly. Your function returns, and the host completes the message. It raises, and the host abandons it, letting the lock go."),
            leo("Third clue. I can feel one."),
            maya("[chuckle] Sam's HTTP function had twenty lines of SDK code to send the job. An SDK, a software development kit, is the library you'd normally use for a service. A client, a sender, a send, tidy-up."),
            leo("And there's a shorter way."),
            maya("An output binding. A binding is a connection you declare, not code you write, like plumbing that's already fitted. Add an output decorator for the queue, and set one value."),
            leo("And who actually sends it?"),
            maya("The host, when the function ends. An input binding is the mirror: it reads data in. And the trigger is what starts the run."),
            leo("Last witness: the bill, or the network?"),
            maya("The network. The worker must also read a database inside a virtual network, a private network only Sam's resources share. And he wants it to scale to zero, so nothing runs, and nothing's paid, when it's quiet. He was on the legacy Consumption plan."),
            leo("Dedicated, then. Proper servers."),
            maya("That's where the idea stops. Dedicated runs on an App Service plan, servers rented by the month, and it never scales to zero. Legacy Consumption can't join a virtual network. Flex Consumption does both."),
            leo("And Premium? It sounds expensive."),
            maya("Premium is for always warm. That means no cold start, which is the pause while a sleeping app wakes up. But it's billed even when idle."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. Works locally, setting missing in Azure: local settings aren't deployed, add an app setting. The connection names an app setting, ideally identity-based. Failed messages vanish: stop catching every error. Send without SDK code: an output binding. Scale to zero inside a virtual network: Flex Consumption."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, without the panic. Run func init and add an HTTP function with app dot route. Call it locally after func start. Create a Flex Consumption app and publish to it. Then add an app setting in Azure, and read it in Python with os dot environ, Python's way to read settings."),
            leo("The worker wakes. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-functions",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? Pull up a chair. They moved the Two Tracks Coach's little background jobs into Azure Functions. That's the study assistant in the Android app, with its Python backend, the server code the app talks to, on Azure. And, well. It was a week."),
            leo("[laugh] I heard someone shouted at a laptop. What's Azure Functions, in plain words?"),
            maya("Small pieces of code that run when something happens, and you pay for the time they run. Picture a porch light with a motion sensor: dark and free until someone walks up. It's called serverless, because the servers are Azure's to look after."),
            leo("Does it cost anything while it waits?"),
            maya("On the right plan, nothing. It scales to zero: no copies running, nothing paid, until something starts a function. More on plans on Friday. Spoiler: Priya."),
            leo("And under the porch light?"),
            maya("A function app holds several functions that share settings and a hosting plan. Inside, the Functions host, a program Azure runs for you, watches for each function's trigger, the thing that starts it. When one fires, the host calls your Python function."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. Tom built the embed API, the web address the phone calls to add a note to the smart search. It's an HTTP function: the phone sends a web request, and the function answers."),
            leo("So far, so normal. Like any route on my own server."),
            maya("He wrote it in the v2 programming model, the newer way of writing them: one file, function app dot py, every function labelled with decorators."),
            leo("Decorators, for those of us at the back?"),
            maya("A line just above a Python function, starting with the at sign, that labels what it does. App dot route, and it's an HTTP function. It ran beautifully on his laptop, with func start. Then he published it, and everything that touched Service Bus fell over."),
            leo("Why?"),
            maya("Because the ServiceBusConnection setting lived in local settings dot json, the settings file on his laptop. And that file is never deployed. Core Tools, the func command, publishes the code and leaves it behind. Every value must also be added in Azure, as an app setting, a named value Azure hands the app while it runs."),
            leo("[sigh] Poor Tom. Tuesday?"),
            maya("Tuesday was Dana. Dana fixed Tom's setting. Helpfully. She pasted the whole Service Bus connection string, its address with the password baked in, straight into the decorator, where it says connection equals."),
            leo("Is that not where it goes?"),
            maya("No. That connection is the name of an app setting, not the value. Like an environment variable in your code: you write its name, never the password."),
            leo("So the setting holds the connection string instead?"),
            maya("Better still, no secret at all. Give the function app a managed identity, a badge Azure gives it so it proves who it is with no password. The setting keeps only the Service Bus address, and the badge gets a Service Bus data role."),
            leo("A role being?"),
            maya("A named permission, like a job title. Sender, receiver, nothing broader. Dana took it well. The Key Vault episode has the rest of the identity story."),
            leo("Wednesday?"),
            maya("Wednesday was Marcus. Marcus needed Tom's function to put a job on a queue, a to-do list of jobs. So he wrote twenty lines of SDK code. An SDK, a software development kit, is a service's own library. A client, a sender, a send, cleanup."),
            leo("And that's gossip because?"),
            maya("Because one line would have done it. An output binding. A binding is a connection you declare instead of code you write, like plumbing that's already fitted."),
            leo("Meaning Marcus never touches the Service Bus client at all?"),
            maya("Never. Add the queue output decorator, set a value, and the host sends the message when the function ends. Input bindings read data in; outputs send it out."),
            leo("Thursday, I'm guessing, the intern."),
            maya("[chuckle] The intern. Bless them. They wrote the worker, the function with a Service Bus trigger: the host pulls each job off the queue and hands it to the function. And they were so proud: it never failed. Not once. Wrapped the whole thing in a catch for every error."),
            leo("That sounds responsible."),
            maya("It sounds it. But when the function ends without an error, the message is marked done. So failed jobs were marked done too, and lost. Let the error escape, and the message is abandoned and tried again. After too many tries, it goes to the dead-letter queue, where someone can look at it."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya never makes mistakes, which is why everyone's still talking. She set up the progress refresher: a Cosmos DB trigger, which starts when items in the Coach's database are created or changed. It reads the change feed, a running list of changes. All perfect."),
            leo("So what went wrong?"),
            maya("The plan. She needed to reach a database inside a virtual network, a private network that only the team's resources share. And she wanted it to scale to zero, nothing running and nothing paid when it's quiet. She picked the legacy Consumption plan. It can't join a virtual network."),
            leo("What should she have picked?"),
            maya("Flex Consumption, the default for new serverless apps. Scales to zero, joins a virtual network. Everything she wanted."),
            leo("And the other plans?"),
            maya("Premium is for always warm. That means no cold start, which is the pause while a sleeping app wakes. But it's billed even when idle. Dedicated runs on an App Service plan you already pay for, and never scales to zero."),
            leo("And the Cosmos one keeps a bookmark somewhere, right?"),
            maya("In a lease container, which is like a bookmark: a small extra container where the trigger notes how far through the changes it has read. Priya ticked create if not exists, of course. Priya."),
            leo("So where does Functions stop being the answer?"),
            maya("Where the plan can't follow. If nothing may ever wait for a cold start, scale to zero isn't for you: that's Premium. And the triggers are only the start. What happens on a queue is the Service Bus episode, and the news side is the Event Grid episode."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. Trigger in Python v2: a decorator, not function dot json. Setting missing in Azure: the local file isn't deployed, add an app setting. Connection equals: the name of a setting, ideally identity-based. Send without SDK code: an output binding. Scale to zero inside a virtual network: Flex Consumption."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Run func init and add an HTTP function with app dot route. Call it locally after func start. Create a Flex Consumption app and publish to it. Then add an app setting in Azure, and read it in Python with os dot environ, Python's way to read settings."),
            leo("Settings deployed, errors escaping. Got it, intern. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-functions",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about code that sleeps until it's needed."),
            ivy("Mm. Code that sleeps. My kind of code, at this hour."),
            nova("[chuckle] Picture a porch light with a motion sensor. Dark, costing nothing. Someone walks up, it switches on, does its job, and goes dark again. That's Azure Functions."),
            ivy("Tell me what it really is, under the porch light."),
            nova("Small pieces of code that run when something happens, and you pay for the time they run. It's called serverless, because the servers are Azure's to look after."),
            ivy("So who's awake, if my code is asleep?"),
            nova("The Functions host, a program Azure keeps running inside your function app. It watches for each function's trigger."),
            ivy("Trigger. Say that one slowly."),
            nova("The thing that starts a function. The doorbell. [pause] Each function has exactly one. The commonest is an HTTP trigger. HTTP is how apps talk to servers, so a web request arrives, the kind of call your Android app already makes, and the function answers."),
            ivy("Who's ringing, in our story?"),
            nova("Take the Two Tracks Coach, the study assistant in the Android app, with a Python backend, the server code the app talks to, on Azure. The phone asks for a note to be added to the smart search. An HTTP function takes that request, puts a job on a queue, and answers at once: accepted."),
            ivy("A queue. Tell me."),
            nova("A Service Bus queue, a to-do list of jobs. And a second function, with a Service Bus trigger, wakes for each job on it."),
            ivy("Does the queue knock on its door?"),
            nova("[pause] No, the other way round. The host keeps asking the queue, gently, anything for me? It pulls the next job and hands it to your function. Finish without an error, and the job is marked done."),
            ivy("And when it doesn't finish? Late night, tired code."),
            nova("Then let it fail out loud. If the function raises an error, the message is abandoned and tried again. After too many tries, it moves aside to the dead-letter queue, a side shelf for failed jobs. [pause] But catch every error and carry on quietly, and the job is marked done, and lost."),
            ivy("Any other doorbells?"),
            nova("Two more. An Event Grid trigger works the opposite way: Event Grid pushes each piece of news straight to your function, like, a file was uploaded. And a Cosmos DB trigger wakes when items in Azure's document database are created or changed."),
            ivy("How does it know which changes it's already seen?"),
            nova("It reads the change feed, which is a running list of changes. And it keeps its place in a lease container, a small extra container that works like a bookmark."),
            ivy("A bookmark. Sweet. And how do these little functions talk to anything else?"),
            nova("With bindings. A binding is a connection you declare, instead of code you write. Plumbing that's already fitted: you don't lay the pipes, you just turn the tap. Data comes in as a parameter, or goes out when you set one."),
            ivy("No code at all?"),
            nova("No SDK code. An SDK, a software development kit, is the library you'd normally use for a service, the client you'd set up in your server code."),
            ivy("And instead?"),
            nova("With an output binding, sending a message to the queue is one line. You set a value, and the host sends it when the function ends. An input binding reads data in."),
            ivy("Show me one, for the Coach."),
            nova("The search request. An HTTP trigger, plus a Service Bus output binding. The function sets one value, the job, and answers, accepted. [pause] The host puts the job on the queue as the function ends."),
            ivy("And where do I write all this? Somewhere cosy?"),
            nova("One file. In the v2 programming model, the newer way of writing them, every Python function lives in function app dot py, with decorators on top."),
            ivy("Decorators. Like little name tags?"),
            nova("Little sticky notes: a line just above the function, starting with the at sign. App dot route, for an HTTP function. App dot service bus queue trigger, for the worker."),
            ivy("Now the part that keeps me up. Passwords. Where do they go?"),
            nova("Ideally, nowhere. In the decorator, connection equals ServiceBusConnection is not a connection string, an address with a password baked in. It's the name of an app setting, a named value Azure hands your app while it runs."),
            ivy("And what's inside that setting? Whisper it."),
            nova("[pause] Hardly anything. Give the app a managed identity, a badge Azure gives it, so it proves who it is with no password. Then the setting holds only the Service Bus address. No secret at all."),
            ivy("And on my own laptop, at three in the morning?"),
            nova("There, the settings live in a file called local settings dot json. And here's a soft little warning. That file is never deployed. Core Tools, the func command, publishes your code and leaves it behind. So add every setting in Azure too, or it works on your laptop and fails in the cloud."),
            ivy("Mm. And Core Tools. Remind me what that is."),
            nova("The func command on your laptop. Func init makes a new project. Func start runs it locally, and lists each function's address. And func azure functionapp publish sends the code up to Azure."),
            ivy("Where does it all run, in Azure?"),
            nova("On a hosting plan, how you rent the machines. Flex Consumption is the default for new serverless apps. It scales to zero, so nothing runs and nothing's paid when it's quiet, and it can join a virtual network, a private network only your own resources share."),
            ivy("Now. Where does all this stop working, Nova?"),
            nova("Here. [pause] Scale to zero means a sleeping app takes a moment to wake. That's a cold start. If nothing may ever wait, Premium keeps copies always warm, and bills you even when idle."),
            ivy("And the other plans? The ones I shouldn't trust?"),
            nova("If you already rent an App Service plan, servers by the month, that's Dedicated, and it never sleeps. And the legacy Consumption plan can't join a virtual network at all."),
            ivy("And the queues themselves? The jobs?"),
            nova("That's the Service Bus episode, your next late night. And the news side is the Event Grid episode."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. A trigger in Python v2 is a decorator in function app dot py, not function dot json. A setting missing in Azure means add an app setting. Connection equals names a setting, ideally identity-based. Sending without SDK code is an output binding. And scale to zero inside a virtual network is Flex Consumption."),
            ivy("And something to try today, before I sleep?"),
            nova("Run func init and add an HTTP function with app dot route. Call it locally after func start. [pause] Create a Flex Consumption app and publish to it. Then add an app setting in Azure, and read it in Python with os dot environ, Python's way to read settings."),
            ivy("Code that sleeps until it's called. Like me. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-functions",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about something that only wakes up when you ring."),
            rafe("Sounds like me on a Sunday morning."),
            nova("[laugh] Azure Functions. Picture a porch light with a motion sensor. Dark and free until someone walks up, then on, job done, and dark again."),
            rafe("Romantic lighting. So what is it really, under the porch light?"),
            nova("Small pieces of code that run when something happens, and you pay for the time they run. That's serverless: the servers are Azure's to look after. Inside a function app, the Functions host, a program Azure runs for you, watches for each function's trigger, and calls your Python function when one fires."),
            rafe("Trigger. The thing that gets things started."),
            nova("Exactly. The doorbell. Each function has exactly one. An HTTP trigger starts when a web request comes in, HTTP being how apps talk to servers. A Service Bus trigger starts on a job in a queue, a to-do list of jobs: the host pulls each one off for you."),
            rafe("Picture the Two Tracks Coach, then, the study assistant in the Android app, with a Python backend, the server code the app talks to, on Azure. The phone asks for a note to join the smart search. An HTTP function takes it, puts a job on a queue, and says accepted."),
            nova("And a worker with a Service Bus trigger does the job. So, darling, exam style. In the Python v2 programming model, the newer way to write them, where do you say that a function starts on a Service Bus message?"),
            rafe("In function app dot py, with a decorator. A line just above the function, starting with the at sign, like a little label. App dot service bus queue trigger. No function dot json; that was the old v1 model."),
            nova("Right answer. I do love a man who labels things properly. [kiss]"),
            rafe("Mm. My turn. How does a function talk to other services, without me writing pages of code?"),
            nova("Bindings. A binding is a connection you declare, instead of code you write. Plumbing that's already fitted: you just turn the tap."),
            rafe("So no Service Bus client, no sender, no tidy-up in my code?"),
            nova("None. An input binding brings data in as a parameter. An output binding sends data out when you set a value. The Coach's search request is an HTTP trigger plus a queue output binding."),
            rafe("Then my question. The worker must save a summary to Cosmos DB, our document database, again with no SDK code. What do you add?"),
            nova("An SDK? That's a software development kit, the library you'd normally call. So, no library. I'd add a Cosmos DB input binding."),
            rafe("[sigh] No kiss for that one. Data leaving is an output binding. A Cosmos DB output decorator, set the document, and the host writes it when the function ends. Input is for reading in."),
            nova("Out, not in. I'll remember. Now, settings. In the decorator, connection equals ServiceBusConnection. Is that a connection string, an address with the password baked in?"),
            rafe("No. It's the name of an app setting, a named value Azure hands the app while it runs. And best of all, the setting holds no secret. The app gets a managed identity, a badge Azure gives it so it proves who it is with no password, and the setting holds only the Service Bus address."),
            nova("Right answer. Some things are better left unsaid. [kiss]"),
            rafe("And the badge gets a role? A named permission, like a job title?"),
            nova("A Service Bus data role. Sender and receiver, nothing broader. Never give more than the relationship needs. The Key Vault episode has the rest of the identity story."),
            rafe("Then my question. The app works with func start on the laptop, and fails in Azure: the ServiceBusConnection setting is missing. Why?"),
            nova("Because the settings on your laptop live in local settings dot json, and that file is never deployed. Core Tools, the func command, publishes the code and leaves it behind. Add the value in Azure as an app setting."),
            rafe("Right answer. What happens on the laptop stays on the laptop. [kiss]"),
            nova("Under the hood, though. When the worker throws an error, what happens to the message?"),
            rafe("It's abandoned, and tried again. After too many tries, it goes to the dead-letter queue, a holding pen for failed jobs. But if you catch every error and carry on quietly, the host marks it done, and the job is lost."),
            nova("So let your feelings out, Rafe. And let your errors out too. One more doorbell. A Cosmos DB trigger, when items change. Where does it keep its place?"),
            rafe("In a lease container, a little bookmark container, while it reads the change feed, the running list of changes."),
            nova("Right answer. You never lose your place with me. [kiss]"),
            rafe("And the Event Grid trigger? Quick one, love: does Event Grid push to the function, or does the function go and fetch?"),
            nova("Event Grid pushes, like a notification landing on your phone. It's Service Bus where the host pulls jobs off the queue. And Functions handles the little hello Event Grid wants before it sends anything. Polite. Like you, on our first date."),
            rafe("Now plans. How we rent the machines. Your question, darling. A new function app must scale to zero when idle, but reach a database inside a virtual network. Which plan?"),
            nova("A virtual network being a private network only our own things share. That's Flex Consumption. The default for new serverless apps: scales to zero, joins the network. The legacy Consumption plan can't join one, and Dedicated never scales to zero."),
            rafe("Right answer. Private, and never more than we need. [kiss]"),
            nova("And where does it stop working, Rafe? Every romance has its limits."),
            rafe("At the cold start, the pause while a sleeping app wakes. If nothing may ever wait, it's Premium, always warm, billed even when idle. And for queues in depth, the Service Bus episode; for pushed news, the Event Grid episode."),
            nova("Last question, for the exam. What do we remember?"),
            rafe("A trigger in Python v2 is a decorator. A setting missing in Azure means add an app setting. Connection equals names a setting, ideally identity-based. Data leaving, an output binding. Scale to zero inside a virtual network, Flex Consumption."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Run func init and add an HTTP function with app dot route. Call it locally after func start. Create a Flex Consumption app and publish to it. Then add an app setting in Azure, and read it in Python with os dot environ, Python's way to read settings."),
            rafe("And I'll always wake up when you ring. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun overview() = DiagramSpec(
        "Triggers start the functions declared in function_app.py; input and output bindings move data in and out without SDK code.",
        nodes = listOf(
            DNode("http", "HTTP request", 0.17f, 0.15f, w = 0.3f, h = 0.16f, kind = NodeKind.PILL, tone = 1),
            DNode("sb", "Queue message", 0.5f, 0.15f, w = 0.3f, h = 0.16f, kind = NodeKind.PILL, tone = 1),
            DNode("eg", "Event Grid event", 0.83f, 0.15f, w = 0.3f, h = 0.16f, kind = NodeKind.PILL, tone = 1),
            DNode("app", "function_app.py", 0.5f, 0.48f, w = 0.5f, h = 0.22f, sub = "decorators"),
            DNode("out", "Queue output", 0.2f, 0.82f, w = 0.34f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2),
            DNode("cosmos", "Cosmos DB input", 0.8f, 0.82f, w = 0.34f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2),
        ),
        edges = listOf(
            DEdge("http", "app", "trigger"),
            DEdge("sb", "app"),
            DEdge("eg", "app"),
            DEdge("app", "out", "output"),
            DEdge("cosmos", "app", "input"),
        ),
        aspect = 0.8f,
    )

    private fun embedFlow() = DiagramSpec(
        "The phone calls an HTTP function, which queues a job through an output binding; a queue-triggered worker does it, and failures end in the dead-letter queue.",
        nodes = listOf(
            DNode("phone", "Android app", 0.12f, 0.2f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
            DNode("http", "HTTP function", 0.45f, 0.2f, w = 0.34f, h = 0.2f, tone = 1, sub = "app.route"),
            DNode("queue", "embed-jobs", 0.82f, 0.2f, w = 0.3f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "Service Bus"),
            DNode("worker", "Queue worker", 0.82f, 0.62f, w = 0.3f, h = 0.2f, sub = "queue trigger"),
            DNode("dlq", "Dead-letter queue", 0.45f, 0.62f, w = 0.34f, h = 0.2f, tone = 2, sub = "too many tries"),
        ),
        edges = listOf(
            DEdge("phone", "http", "POST"),
            DEdge("http", "queue", "output binding"),
            DEdge("queue", "worker", "trigger"),
            DEdge("worker", "dlq", "keeps failing", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun triggers() = DiagramSpec(
        "Four triggers the exam names: an HTTP request, a queue message, a pushed event, and changed items read from the change feed with a lease container.",
        nodes = listOf(
            DNode("http", "HTTP", 0.26f, 0.15f, w = 0.44f, h = 0.2f, tone = 1, sub = "a web request"),
            DNode("sb", "Service Bus", 0.76f, 0.15f, w = 0.44f, h = 0.2f, tone = 1, sub = "pulled from a queue"),
            DNode("eg", "Event Grid", 0.26f, 0.48f, w = 0.44f, h = 0.2f, tone = 1, sub = "a pushed event"),
            DNode("cosmos", "Cosmos DB", 0.76f, 0.48f, w = 0.44f, h = 0.2f, tone = 1, sub = "changed items"),
            DNode("one", "One trigger each", 0.26f, 0.84f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("lease", "Lease container", 0.76f, 0.84f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "the bookmark"),
        ),
        edges = listOf(DEdge("cosmos", "lease", "keeps its place")),
        aspect = 0.8f,
    )

    private fun settings() = DiagramSpec(
        "local.settings.json stays on your laptop; in Azure the binding's connection names an app setting, which can be identity-based.",
        nodes = listOf(
            DNode("laptop", "Your laptop", 0.25f, 0.16f, w = 0.44f, h = 0.2f, tone = 1, sub = "func start"),
            DNode("azure", "Function app", 0.75f, 0.16f, w = 0.44f, h = 0.2f, sub = "in Azure"),
            DNode("local", "local.settings.json", 0.25f, 0.5f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "never deployed"),
            DNode("appset", "App setting", 0.75f, 0.5f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "ServiceBusConnection"),
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
        "Flex Consumption is the default serverless plan; Premium stays warm, Dedicated runs on an App Service plan, and legacy Consumption has no virtual network.",
        nodes = listOf(
            DNode("flex", "Flex Consumption", 0.5f, 0.16f, w = 0.7f, h = 0.2f, sub = "scale to zero, virtual network"),
            DNode("premium", "Premium", 0.26f, 0.5f, w = 0.44f, h = 0.2f, tone = 1, sub = "always warm"),
            DNode("dedicated", "Dedicated", 0.76f, 0.5f, w = 0.44f, h = 0.2f, tone = 1, sub = "App Service plan"),
            DNode("legacy", "Legacy Consumption", 0.5f, 0.84f, w = 0.6f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "no virtual network"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Functions questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("v2", "Trigger in Python v2", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a decorator"),
            DNode("missing", "Missing in Azure", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "add an app setting"),
            DNode("send", "Send without SDK", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "output binding"),
            DNode("conn", "connection=", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "names a setting"),
            DNode("vnet", "Zero, in a VNet", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Flex Consumption"),
            DNode("warm", "No cold start", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Premium"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("v2", "missing", "send", "conn", "vnet", "warm")
    private val route = BoardCard("Python", "@app.route(route=\"notes/{noteId}/embed\", methods=[\"POST\"])", code = true)
    private val queueTrigger = BoardCard("Python", "@app.service_bus_queue_trigger(arg_name=\"msg\", queue_name=\"embed-jobs\")", code = true)
    private val setJob = BoardCard("Python", "jobs.set(json.dumps({\"noteId\": note_id}))", code = true)
    private val today = BoardCard("Today", "func init, an HTTP function, func start, a Flex app, an app setting.")

    private fun classicBoard() = PodcastBoard(
        "ai-functions",
        listOf(
            // 0: "Hi, and welcome back. Today is Azure Functions..."
            Scene(
                0, "Code that sleeps", overview(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("Azure Functions", "Small pieces of code that run when something happens.")),
                    Beat(2, card = BoardCard("Serverless", "Azure starts copies when needed and stops them when it's quiet.")),
                    Beat(4, show = setOf("http"), light = setOf("http"), pulse = "http" to "app", card = BoardCard("Trigger", "What starts a function: the doorbell. Exactly one each.")),
                ),
            ),
            // 5: "So a function can be a tiny web API?"
            Scene(
                5, "A serverless API", embedFlow(),
                beats = listOf(
                    Beat(6, show = setOf("phone", "http"), light = setOf("http"), pulse = "phone" to "http", card = BoardCard("The Coach's request", "The phone asks for a note to join the smart search.")),
                    Beat(8, show = setOf("queue", "worker"), light = setOf("worker"), pulse = "queue" to "worker", card = BoardCard("Service Bus trigger", "The host pulls each job off the queue and calls your code.")),
                    Beat(14, show = setOf("dlq"), light = setOf("dlq"), pulse = "worker" to "dlq", card = BoardCard("Errors", "Raise, and the message is retried, then dead-lettered. Never swallow.")),
                ),
            ),
            // 17: "Noted. Next doorbell?"
            Scene(
                17, "More doorbells", triggers(),
                beats = listOf(
                    Beat(18, show = setOf("http", "sb", "eg"), light = setOf("eg"), card = BoardCard("Event Grid trigger", "Wakes for each pushed event; the handshake is handled for you.")),
                    Beat(20, show = setOf("cosmos", "lease"), light = setOf("cosmos", "lease"), pulse = "cosmos" to "lease", card = BoardCard("Cosmos DB trigger", "Reads the change feed; a lease container keeps its place.")),
                    Beat(22, light = setOf("lease"), card = BoardCard("Lease container", "A bookmark: how far through the change feed the trigger has read.")),
                ),
            ),
            // 23: "A bookmark, so it never loses its page..."
            Scene(
                23, "Bindings", overview(),
                beats = listOf(
                    Beat(24, show = setOf("app", "out", "cosmos"), light = setOf("out", "cosmos"), card = BoardCard("Binding", "A declared connection: data in as a parameter, out by setting one.")),
                    Beat(26, light = setOf("out"), pulse = "app" to "out", card = setJob),
                    Beat(28, show = setOf("http"), light = setOf("http", "cosmos"), pulse = "cosmos" to "app", card = BoardCard("In, out, trigger", "Input reads data in, output sends it out, the trigger starts it.")),
                ),
            ),
            // 29: "Where do I write all of this?..."
            Scene(
                29, "The v2 programming model", overview(),
                beats = listOf(
                    Beat(30, show = setOf("app"), light = setOf("app"), card = BoardCard("function_app.py", "One file, a decorator per trigger and binding. No function.json.")),
                    Beat(32, show = setOf("http", "sb", "out"), light = setOf("http", "sb"), pulse = "http" to "app", card = route),
                ),
            ),
            // 33: "Now the bit I always get wrong. Connections..."
            Scene(
                33, "Connections and settings", settings(),
                beats = listOf(
                    Beat(36, show = setOf("azure", "appset"), light = setOf("appset"), pulse = "azure" to "appset", card = BoardCard("Names an app setting", "connection=\"ServiceBusConnection\" is a setting's name, not a value.")),
                    Beat(38, show = setOf("mi"), light = setOf("mi"), pulse = "appset" to "mi", card = BoardCard("Identity-based", "The setting holds only the address; the identity gets a data role.")),
                    Beat(40, show = setOf("laptop", "local"), light = setOf("local"), pulse = "laptop" to "local", card = BoardCard("local.settings.json", "For your machine only. It is never deployed.")),
                    Beat(42, light = setOf("laptop"), pulse = "laptop" to "azure", card = BoardCard("Core Tools", "func init, func start, func azure functionapp publish.")),
                ),
            ),
            // 45: "Where does it run in Azure?..."
            Scene(
                45, "Hosting plans", plans(),
                beats = listOf(
                    Beat(46, show = setOf("flex"), light = setOf("flex"), card = BoardCard("Flex Consumption", "The default: scale to zero, fast scale-out, a virtual network.")),
                    Beat(48, show = setOf("premium", "dedicated"), light = setOf("premium", "dedicated"), card = BoardCard("Premium and Dedicated", "Premium: always warm. Dedicated: an App Service plan, never zero.")),
                    Beat(52, show = setOf("legacy"), light = setOf("legacy"), card = BoardCard("Legacy Consumption", "Scales to zero, but no virtual network.")),
                ),
            ),
            // 53: "So what do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("v2", "missing", "send", "vnet", "warm"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: decorator, setting, binding, plan.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-functions--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Worker That Never Woke..."
            Scene(
                0, "The Worker That Never Woke", embedFlow(),
                beats = listOf(
                    Beat(0, show = setOf("phone", "http", "queue"), light = setOf("queue"), card = BoardCard("The case", "In Azure the queue filled with jobs, and nothing touched them.")),
                    Beat(2, card = BoardCard("Azure Functions", "Code that runs when something happens; you pay for the time.")),
                    Beat(4, show = setOf("worker"), light = setOf("worker"), pulse = "queue" to "worker", card = BoardCard("Under the picture", "The Functions host watches each trigger and calls your function.")),
                    Beat(6, light = setOf("worker"), card = BoardCard("Trigger", "The doorbell that starts a function. Exactly one each.")),
                    Beat(8, light = setOf("http", "queue"), pulse = "http" to "queue", card = BoardCard("Two functions", "An HTTP function queues the job; a queue-triggered worker does it.")),
                ),
            ),
            // 11: "Then my first suspect: the decorators..."
            Scene(
                11, "The suspects", overview(),
                beats = listOf(
                    Beat(11, show = setOf("app"), light = setOf("app"), card = BoardCard("Suspect one: decorators", "Ignored in Azure? No: they work the same everywhere.")),
                    Beat(12, card = queueTrigger),
                    Beat(13, show = setOf("sb"), light = setOf("sb"), pulse = "sb" to "app", card = BoardCard("Suspect two: Service Bus", "Not supported in Azure? It's fully supported.")),
                ),
            ),
            // 14: "No, the Service Bus trigger is fully supported. Here's the first clue..."
            Scene(
                14, "The first clue", settings(),
                beats = listOf(
                    Beat(14, show = setOf("azure", "appset"), light = setOf("appset"), pulse = "azure" to "appset", card = BoardCard("ServiceBusConnection missing", "The host's log in Azure: the setting doesn't exist.")),
                    Beat(17, show = setOf("laptop", "local"), light = setOf("local"), pulse = "laptop" to "local", card = BoardCard("local.settings.json", "Made by func init. For your machine only.")),
                    Beat(18, light = setOf("laptop"), pulse = "laptop" to "azure", card = BoardCard("The culprit", "Core Tools publishes the code, not that file. Add an app setting.")),
                    Beat(20, light = setOf("appset"), card = BoardCard("Not a connection string", "connection= names a setting. A connection string is a secret to leak.")),
                    Beat(22, show = setOf("mi"), light = setOf("mi"), pulse = "appset" to "mi", card = BoardCard("Identity-based", "A managed identity, the address only, sender and receiver roles.")),
                ),
            ),
            // 25: "Second witness?"
            Scene(
                25, "The second witness", embedFlow(),
                beats = listOf(
                    Beat(26, show = setOf("phone", "http", "queue", "worker"), light = setOf("worker"), card = BoardCard("Decks never built", "Yet the queue said every job was done.")),
                    Beat(28, light = setOf("worker"), pulse = "queue" to "worker", card = BoardCard("Catching every error", "Ends without an error, so the message is marked done, and lost.")),
                    Beat(30, show = setOf("dlq"), light = setOf("dlq"), pulse = "worker" to "dlq", card = BoardCard("Let it raise", "Abandoned, retried, then the dead-letter queue.")),
                    Beat(32, light = setOf("queue", "worker"), pulse = "queue" to "worker", card = BoardCard("Under the hood", "The host pulls it with a lock; complete on return, abandon on an error.")),
                ),
            ),
            // 35: "Third clue. I can feel one."
            Scene(
                35, "The third clue", overview(),
                beats = listOf(
                    Beat(36, show = setOf("app", "out"), light = setOf("app"), card = BoardCard("Twenty lines of SDK code", "A client, a sender, a send, tidy-up.")),
                    Beat(38, show = setOf("cosmos"), light = setOf("out"), pulse = "app" to "out", card = BoardCard("Output binding", "Declare it, set one value; the host sends it when the function ends.")),
                ),
            ),
            // 41: "Last witness: the bill, or the network?"
            Scene(
                41, "The last witness", plans(),
                beats = listOf(
                    Beat(42, show = setOf("legacy"), light = setOf("legacy"), card = BoardCard("Legacy Consumption", "Scales to zero, but can't join a virtual network.")),
                    Beat(44, show = setOf("flex", "premium", "dedicated"), light = setOf("flex"), card = BoardCard("Flex Consumption", "Zero and a network. Premium: warm. Dedicated: never zero.")),
                ),
            ),
            // 47: "So, the case summary for the exam?"
            Scene(
                47, "Case closed", exam(),
                beats = listOf(
                    Beat(48, show = all6, light = setOf("missing", "conn", "send", "vnet"), card = BoardCard("Listen for the phrase", "Settings, connections, errors, bindings, plans: one answer each.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-functions--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "The Coach team's week", overview(),
                beats = listOf(
                    Beat(0, show = setOf("app"), light = setOf("app"), card = BoardCard("Azure Functions", "The Coach's little background jobs, moved into functions.")),
                    Beat(2, card = BoardCard("Serverless", "Code that runs when something happens; Azure looks after servers.")),
                    Beat(4, card = BoardCard("Scale to zero", "No copies running, nothing paid, until a trigger fires.")),
                    Beat(6, show = setOf("http", "sb", "eg"), light = setOf("http", "sb", "eg"), pulse = "http" to "app", card = BoardCard("The host", "Watches each function's trigger and calls your Python function.")),
                ),
            ),
            // 7: "Okay. So who went first?"
            Scene(
                7, "Monday: Tom", settings(),
                beats = listOf(
                    Beat(8, show = setOf("laptop"), light = setOf("laptop"), card = BoardCard("The embed API", "An HTTP function in function_app.py, labelled with decorators.")),
                    Beat(12, card = route),
                    Beat(14, show = setOf("local", "azure"), light = setOf("local"), pulse = "laptop" to "local", card = BoardCard("local.settings.json", "Never deployed. Add every value in Azure as an app setting.")),
                ),
            ),
            // 15: "Poor Tom. Tuesday?"
            Scene(
                15, "Tuesday: Dana", settings(),
                beats = listOf(
                    Beat(16, show = setOf("azure", "appset"), light = setOf("appset"), pulse = "azure" to "appset", card = BoardCard("A connection string pasted in", "Dana put the secret where the setting's name should go.")),
                    Beat(20, show = setOf("mi"), light = setOf("mi"), pulse = "appset" to "mi", card = BoardCard("Identity-based", "The setting holds the Service Bus address; the badge needs a role.")),
                    Beat(22, light = setOf("mi"), card = BoardCard("The smallest role", "Service Bus data sender and receiver, nothing broader.")),
                ),
            ),
            // 23: "Wednesday?"
            Scene(
                23, "Wednesday: Marcus", overview(),
                beats = listOf(
                    Beat(24, show = setOf("app"), light = setOf("app"), card = BoardCard("Twenty lines of SDK code", "A client, a sender, a send, cleanup, to put one job on a queue.")),
                    Beat(28, show = setOf("out"), light = setOf("out"), pulse = "app" to "out", card = setJob),
                ),
            ),
            // 29: "Thursday, I'm guessing, the intern."
            Scene(
                29, "Thursday: the intern", embedFlow(),
                beats = listOf(
                    Beat(30, show = setOf("queue", "worker"), light = setOf("worker"), pulse = "queue" to "worker", card = BoardCard("Never failed. Not once.", "Every error caught, logged and ignored.")),
                    Beat(32, show = setOf("dlq"), light = setOf("dlq"), pulse = "worker" to "dlq", card = BoardCard("Let it raise", "Failed jobs were marked done. Raise: retried, then dead-lettered.")),
                ),
            ),
            // 33: "And Friday? There's always a Friday."
            Scene(
                33, "Friday: Priya", plans(),
                beats = listOf(
                    Beat(34, card = BoardCard("Cosmos DB trigger", "Starts on created or changed items, read from the change feed.")),
                    Beat(36, show = setOf("legacy"), light = setOf("legacy"), card = BoardCard("Legacy Consumption", "Scale to zero, but no virtual network.")),
                    Beat(38, show = setOf("flex", "premium", "dedicated"), light = setOf("flex"), card = BoardCard("Flex Consumption", "Scale to zero and a virtual network: the default for new apps.")),
                    Beat(42, card = BoardCard("Lease container", "Where the Cosmos DB trigger keeps its place in the change feed.")),
                ),
            ),
            // 43: "So where does Functions stop being the answer?"
            Scene(
                43, "Where it stops", plans(),
                beats = listOf(
                    Beat(44, show = setOf("flex", "premium"), light = setOf("premium"), card = BoardCard("Cold starts", "If nothing may wait, Premium. Queues and news: other episodes.")),
                ),
            ),
            // 45: "So, the gossip I should remember for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("v2", "missing", "conn", "send", "vnet"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. One colleague, one lesson.")),
                    Beat(48, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-functions--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "Code that sleeps", overview(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "Code that sleeps until it's needed.")),
                    Beat(2, show = setOf("app"), light = setOf("app"), card = BoardCard("Azure Functions", "A porch light with a motion sensor, for your code.")),
                    Beat(4, card = BoardCard("Under the picture", "The Functions host watches each trigger and calls your function.")),
                    Beat(8, show = setOf("http"), light = setOf("http"), pulse = "http" to "app", card = BoardCard("Trigger", "What starts a function: the doorbell. Exactly one each.")),
                ),
            ),
            // 9: "Who's ringing, in our story?"
            Scene(
                9, "Who's ringing", embedFlow(),
                beats = listOf(
                    Beat(10, show = setOf("phone", "http", "queue"), light = setOf("http"), pulse = "phone" to "http", card = BoardCard("The Coach's request", "An HTTP function puts a job on a queue and answers: accepted.")),
                    Beat(12, show = setOf("worker"), light = setOf("worker"), pulse = "queue" to "worker", card = BoardCard("Service Bus trigger", "The host pulls each job off the queue; ends cleanly, marked done.")),
                    Beat(16, show = setOf("dlq"), light = setOf("dlq"), pulse = "worker" to "dlq", card = BoardCard("Fail out loud", "Raise, and it's retried, then dead-lettered. Never swallow errors.")),
                ),
            ),
            // 17: "Any other doorbells?"
            Scene(
                17, "Other doorbells", triggers(),
                beats = listOf(
                    Beat(18, show = setOf("http", "sb", "eg", "cosmos", "lease"), light = setOf("eg", "cosmos"), pulse = "cosmos" to "lease", card = BoardCard("Event Grid and Cosmos DB", "Pushed news; changed items, with a lease container as bookmark.")),
                    Beat(20, light = setOf("lease"), card = BoardCard("Lease container", "A bookmark: how far through the change feed the trigger has read.")),
                ),
            ),
            // 21: "A bookmark. Sweet..."
            Scene(
                21, "Plumbing already fitted", overview(),
                beats = listOf(
                    Beat(22, show = setOf("app", "out", "cosmos"), light = setOf("out", "cosmos"), card = BoardCard("Binding", "A declared connection: data in as a parameter, out by setting one.")),
                    Beat(26, light = setOf("out"), pulse = "app" to "out", card = BoardCard("Output binding", "One line: set a value; the host sends it when the function ends.")),
                    Beat(28, show = setOf("http"), light = setOf("http", "out"), pulse = "http" to "app", card = setJob),
                ),
            ),
            // 29: "And where do I write all this? Somewhere cosy?"
            Scene(
                29, "One cosy file", overview(),
                beats = listOf(
                    Beat(32, show = setOf("app", "http", "sb"), light = setOf("app"), pulse = "http" to "app", card = route),
                ),
            ),
            // 33: "Now the part that keeps me up. Passwords..."
            Scene(
                33, "Passwords", settings(),
                beats = listOf(
                    Beat(34, show = setOf("azure", "appset", "mi"), light = setOf("appset", "mi"), pulse = "appset" to "mi", card = BoardCard("Names an app setting", "With a managed identity, the setting holds only the address.")),
                    Beat(38, show = setOf("laptop", "local"), light = setOf("local"), pulse = "laptop" to "local", card = BoardCard("local.settings.json", "Never deployed. Add every setting in Azure too.")),
                    Beat(40, light = setOf("laptop"), pulse = "laptop" to "azure", card = BoardCard("Core Tools", "func init, func start, func azure functionapp publish.")),
                ),
            ),
            // 41: "Where does it all run, in Azure?"
            Scene(
                41, "Where it runs", plans(),
                beats = listOf(
                    Beat(42, show = setOf("flex"), light = setOf("flex"), card = BoardCard("Flex Consumption", "The default: scale to zero, and a virtual network.")),
                    Beat(44, show = setOf("premium", "dedicated", "legacy"), light = setOf("premium", "legacy"), card = BoardCard("Where it stops", "Cold starts: Premium. Dedicated never sleeps. Legacy: no network.")),
                    Beat(48, card = BoardCard("Your next late night", "Service Bus for the jobs; Event Grid for the news.")),
                ),
            ),
            // 49: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("v2", "missing", "conn", "send", "vnet"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: five little things.")),
                    Beat(52, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-functions--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", overview(),
                beats = listOf(
                    Beat(0, card = BoardCard("Date night", "Something that only wakes up when you ring.")),
                    Beat(2, show = setOf("app"), light = setOf("app"), card = BoardCard("Azure Functions", "A porch light with a motion sensor, for your code.")),
                    Beat(4, card = BoardCard("Under the picture", "The Functions host watches each trigger and calls your function.")),
                    Beat(6, show = setOf("http", "sb"), light = setOf("http", "sb"), pulse = "http" to "app", card = BoardCard("Trigger", "The doorbell: a web request, or a job landing on a queue.")),
                ),
            ),
            // 7: "Picture the Two Tracks Coach, then..."
            Scene(
                7, "Labelled properly", embedFlow(),
                beats = listOf(
                    Beat(7, show = setOf("phone", "http", "queue"), light = setOf("http"), pulse = "http" to "queue", card = BoardCard("The Coach's request", "An HTTP function puts a job on a queue and says: accepted.")),
                    Beat(8, show = setOf("worker"), light = setOf("worker"), pulse = "queue" to "worker", card = BoardCard("Quiz: where's the trigger?", "Python v2: where do you say it starts on a Service Bus message?")),
                    Beat(9, card = queueTrigger),
                ),
            ),
            // 11: "Mm. My turn. How does a function talk to other services?..."
            Scene(
                11, "Plumbing", overview(),
                beats = listOf(
                    Beat(12, show = setOf("app", "out", "cosmos"), light = setOf("out"), pulse = "app" to "out", card = BoardCard("Binding", "Declared, not coded: input brings data in, output sends it out.")),
                    Beat(15, light = setOf("cosmos"), card = BoardCard("Quiz: save to Cosmos DB", "Save a summary to Cosmos DB with no SDK code. What do you add?")),
                    Beat(17, light = setOf("out"), card = BoardCard("No kiss: an output binding", "Data leaving is an output binding. Input is for reading in.")),
                ),
            ),
            // 18: "Out, not in. I'll remember. Now, settings..."
            Scene(
                18, "Better left unsaid", settings(),
                beats = listOf(
                    Beat(18, show = setOf("azure", "appset"), light = setOf("appset"), pulse = "azure" to "appset", card = BoardCard("Quiz: connection=", "Is connection=\"ServiceBusConnection\" a connection string?")),
                    Beat(19, show = setOf("mi"), light = setOf("mi"), pulse = "appset" to "mi", card = BoardCard("Right answer", "It names an app setting; with an identity, it holds no secret.")),
                    Beat(23, show = setOf("laptop", "local"), light = setOf("local"), pulse = "laptop" to "local", card = BoardCard("Quiz: missing in Azure", "Works with func start, but the setting is missing in Azure. Why?")),
                    Beat(24, light = setOf("laptop"), pulse = "laptop" to "azure", card = BoardCard("Right answer", "local.settings.json is never deployed. Add an app setting.")),
                ),
            ),
            // 26: "Under the hood, though. When the worker throws an error..."
            Scene(
                26, "Let it out", embedFlow(),
                beats = listOf(
                    Beat(26, show = setOf("phone", "http", "queue", "worker"), light = setOf("worker"), pulse = "queue" to "worker", card = BoardCard("Quiz: an error", "When the worker throws, what happens to the message?")),
                    Beat(27, show = setOf("dlq"), light = setOf("dlq"), pulse = "worker" to "dlq", card = BoardCard("Retried, then dead-lettered", "Never catch every error: the message would be marked done.")),
                ),
            ),
            // 28: "So let your feelings out, Rafe..."
            Scene(
                28, "Never lose your place", triggers(),
                beats = listOf(
                    Beat(28, show = setOf("cosmos"), light = setOf("cosmos"), card = BoardCard("Quiz: the bookmark", "A Cosmos DB trigger: where does it keep its place?")),
                    Beat(29, show = setOf("lease"), light = setOf("lease"), pulse = "cosmos" to "lease", card = BoardCard("Right answer", "A lease container, while it reads the change feed.")),
                    Beat(32, show = setOf("eg"), light = setOf("eg"), card = BoardCard("Event Grid trigger", "Wakes for pushed news; the hello is handled for you.")),
                ),
            ),
            // 33: "Now plans. How we rent the machines..."
            Scene(
                33, "Private", plans(),
                beats = listOf(
                    Beat(33, show = setOf("legacy", "dedicated"), light = setOf("legacy", "dedicated"), card = BoardCard("Quiz: zero, plus a network", "Scale to zero, but reach a database in a virtual network. Which plan?")),
                    Beat(34, show = setOf("flex"), light = setOf("flex"), card = BoardCard("Right answer", "Flex Consumption: scale to zero, and a virtual network.")),
                    Beat(37, show = setOf("premium"), light = setOf("premium"), card = BoardCard("Every romance has limits", "No cold start allowed: Premium, always warm, billed when idle.")),
                ),
            ),
            // 38: "Last question, for the exam. What do we remember?"
            Scene(
                38, "For the exam", exam(),
                beats = listOf(
                    Beat(39, show = all6, light = setOf("v2", "missing", "conn", "send", "vnet"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
