package com.shahaabapps.core.content

/** Service Bus for AI back ends: queues, topics and dead letters: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesServiceBus {
    private val classic = Podcast(
        "ai-servicebus",
        listOf(
            maya("Hi, and welcome back. Zaiqa is in good shape. Our API, the .NET service the phone talks to, runs in Container Apps. Orders and payments sit in Azure SQL, the event feed sits in Cosmos DB. Today we build the one piece we have been pretending exists."),
            leo("Pretending? Which piece?"),
            maya("The orders queue. Since the first episode the API has taken a payment and then sent the order to the restaurant. It has been writing that message into nothing."),
            leo("Why not just call the restaurant's tablet straight from the API?"),
            maya("Because the tablet is in a kitchen. For example, the wifi drops at eight on a Friday, or they are mid-rush with the app closed. The diner watches a spinner while we wait for a kitchen."),
            leo("So the API should not wait."),
            maya("Right. It takes the payment, writes the order row in Azure SQL, drops a message saying order four four seven one is paid, and answers the phone at once."),
            leo("And who delivers it to the restaurant?"),
            maya("Something else, later. The message goes to Azure Service Bus, into a queue. A queue is one line of messages, and each one is handled by exactly one receiver."),
            leo("One receiver only. Why does that matter?"),
            maya("Picture a bakery with one line and three cashiers. Add a cashier and the line moves faster, but nobody is served twice. Ours is called orders."),
            leo("And where does a queue live?"),
            maya("In a namespace: your own post office building, with its own address and your queues inside it. Basic tier has queues only. Standard and Premium add topics, which we will come to."),
            leo("Right. Now the question I actually want answered. Who picks the message up, and where does that program live?"),
            maya("It is called a worker. Zaiqa has one in the design already: the order worker, a small .NET program whose only job is to read that queue. It is your code, and you host it."),
            leo("That is what I mean. In the old world I would put it on IIS, next to the website. Do we do that?"),
            maya("No, and this is the trap. [pause] An IIS application pool starts when a request arrives, and with none for twenty minutes it is idle-timed out and shut down. Your worker never gets a request."),
            leo("So IIS would quietly kill it."),
            maya("Mid-job, at three in the morning, and nothing looks broken. IIS is built for things that answer calls. A worker does not answer calls."),
            leo("Then where does it go?"),
            maya("On a plain server, a Windows Service: it starts with the machine and runs forever, with no web server involved. In Azure you have four good homes."),
            leo("Name them."),
            maya("An Azure Function with a Service Bus trigger, so the platform runs your code once per message. Container Apps with no ingress, which means no public door, scaling on how many messages are waiting."),
            leo("That is two."),
            maya("An App Service WebJob, which rides along with a web app you already have. Or a Kubernetes deployment, if your team already runs one."),
            leo("And the queue itself? Do I deploy that somewhere?"),
            maya("No. The queue is a managed service. You create the namespace and the queue, and Microsoft runs the machines, the storage and the copies. The only thing you deploy is the worker."),
            leo("Then say it plainly. What is the difference between my API and my worker?"),
            maya("Four things. An API is called, and answers while the caller waits. A worker is driven by messages already in the queue, and nobody waits for it."),
            leo("That is one. Two?"),
            maya("An API needs a public door on the internet. A worker has no door at all. It dials out to Service Bus and pulls."),
            leo("Three?"),
            maya("An API scales on requests a second. A worker scales on how deep the queue is. A thousand orders waiting, start more copies of the worker."),
            leo("And four?"),
            maya("What a failure looks like. If the API is down, the diner sees an error now. If the worker is down, nothing visibly breaks: the messages pile up and get handled later. [pause] That is the point of a queue."),
            leo("That is the clearest thing I have heard this month. So the worker takes a message. Is it gone from the queue?"),
            maya("Not yet. The default receive mode is PeekLock. Think of a library hold: the book is put aside for you and hidden from everyone else, but it is not yours until you check it out."),
            leo("And then I say I am done?"),
            maya("You settle it, one of four ways. Complete deletes it. Abandon gives the lock back for another try. Dead-letter moves it aside with a reason. Defer sets it aside to fetch later by its number."),
            leo("And the other mode?"),
            maya("ReceiveAndDelete. The message is gone the moment it is handed over. Fine for something you can afford to lose. Never for a paid order."),
            leo("How long does the lock last?"),
            maya("That is the lock duration, and the default is one minute. Five minutes is the most you can set."),
            leo("Our restaurant tablet takes three."),
            maya("Then the lock runs out while you are still working. The message becomes visible again, another worker takes it, and the kitchen cooks twice. Renew the lock as the work runs, or raise the lock duration."),
            leo("And a message nobody can ever handle? Say the restaurant closed an hour ago."),
            maya("Every try raises its delivery count. Past the max delivery count, ten by default, Service Bus moves it to the dead-letter queue: a side queue for mail that could not be delivered."),
            leo("Who reads that?"),
            maya("Nobody, unless you do. [sigh] Zaiqa runs a script each morning that opens the dead-letter queue, prints the reason on each message, and resends what a fix has cured."),
            leo("Now, one restaurant's orders. Do they arrive in order?"),
            maya("Not by default. Several workers pull from one line, so two orders for the same kitchen can cross. If that matters, you turn on sessions."),
            leo("What is a session?"),
            maya("A group of messages sharing one session id, delivered in order to one receiver at a time. Put the restaurant id in there, and that kitchen gets a single file of its own."),
            leo("And if our payment service retries and sends the same order twice?"),
            maya("Duplicate detection. On Standard and Premium you switch it on, give every message an id of your own, and Service Bus drops a second one with the same id inside a window you choose."),
            leo("What about a pre-order? A diner books dinner at eight, in the morning."),
            maya("A scheduled message. You hand it over now with a time on it, and it stays invisible until then. No timer in your worker, no table of pending jobs."),
            leo("And the topics you keep promising?"),
            maya("Now. A queue gives each message to one receiver; a topic gives every subscription its own copy. Say you're building the delivered-order side: ratings, loyalty points and the finance report each need every order."),
            leo("So that is a topic with three subscriptions."),
            maya("Yes, not one queue. And a subscription can filter: a rule tests a label, city equals Mumbai. One catch, a new subscription has a default rule accepting everything, so remove it."),
            leo("Last one. We are on Standard. When would Zaiqa go Premium?"),
            maya("Two reasons. Premium gives a private endpoint, a door onto your own private network instead of the public internet. And it raises the biggest message to a hundred megabytes."),
            leo("Not that we would send a hundred megabytes."),
            maya("We would not. A dish photo goes in Blob Storage and the message carries its name. But the exam likes the number."),
            leo("Then what do I remember for the exam?"),
            maya("Work that must survive a crash: PeekLock. Every service needs its own copy: a topic. One restaurant's orders in order: sessions. A message that keeps failing: the dead-letter queue, after ten tries."),
            leo("Keep going."),
            maya("Jobs of three minutes, some run twice: the lock duration ran out, so renew it. A worker on IIS: the idle timeout kills it. A private endpoint, or a hundred megabyte message: Premium."),
            leo("And something to try today?"),
            maya("Create a namespace and an orders queue. Send three messages with DefaultAzureCredential, so no connection string goes anywhere. Receive them in PeekLock, complete two and dead-letter one. Then read the dead-letter queue and print the reason."),
            leo("No kitchen left waiting, and no worker on IIS. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-servicebus",
        listOf(
            maya("Welcome to Case Files. Tonight's case: The Order That Never Reached The Kitchen. Eight on a Friday, a diner pays, the Zaiqa app says ordered, and in the restaurant the tablet stays dark for forty minutes."),
            leo("Detective Leo, reporting. The money went through, though?"),
            maya("Perfectly. Our API, the .NET service the phone talks to, took the payment and wrote the order row in Azure SQL. That part was never in doubt."),
            leo("Then the API failed to call the tablet."),
            maya("It never calls the tablet, and that is the right design. The tablet is in a kitchen: the wifi drops, the printer jams, the app gets closed mid-rush."),
            leo("So what does the API do instead?"),
            maya("It drops a small message, order four four seven one is paid, into a queue, and answers the phone at once. A queue is one line of messages, each handled by exactly one receiver."),
            leo("And the queue lives where? Something we maintain?"),
            maya("In a namespace: your own post office building, with its own address and your queues inside it. Microsoft runs the machines. You deploy nothing for the queue itself."),
            leo("Then the suspect is the queue. It lost the message."),
            maya("That is the junior's answer, and the logs say otherwise. The message sat there all evening. [pause] Clue one: nothing came to collect it."),
            leo("Something has to collect it?"),
            maya("A worker: your own program, whose only job is to read the queue and tell the restaurant. Zaiqa's is the order worker, a small .NET service. Service Bus never calls your code; your code pulls."),
            leo("And where was ours running?"),
            maya("Clue two. Somebody had hosted it on IIS, in an application pool, next to an old web app."),
            leo("That sounds reasonable. It is a .NET program."),
            maya("An IIS application pool wakes up when a request arrives. With no request for twenty minutes it is idle-timed out and shut down. A worker never gets a request."),
            leo("[sigh] So at twenty past seven it went to sleep."),
            maya("And took a half-finished order with it. IIS hosts things that answer calls. A worker answers nothing."),
            leo("Then where should it live? For the file."),
            maya("On a plain server, a Windows Service: starts with the machine, runs forever, no web server. In Azure there are four homes."),
            leo("List them."),
            maya("An Azure Function with a Service Bus trigger, run once per message. Container Apps with no ingress, which means no public door, scaling on how many messages wait. An App Service WebJob. Or a Kubernetes deployment."),
            leo("I want the difference written down. API against worker."),
            maya("An API is called, and answers while the caller waits. A worker is driven by messages already in the queue, and nobody waits for it."),
            leo("Doors?"),
            maya("An API needs a public door on the internet. A worker has no door at all. It dials out and pulls."),
            leo("Scaling?"),
            maya("An API scales on requests a second. A worker scales on how deep the queue is."),
            leo("And failure, which is our case."),
            maya("If the API is down the diner sees an error, now. If the worker is down nothing looks broken: the messages pile up quietly. Which is exactly what we had for forty minutes."),
            leo("Case nearly closed. But there is a second file: a kitchen that cooked the same order twice."),
            maya("A different illness. The worker receives in PeekLock, the default mode. Think of a library hold: the message is put aside for you and hidden from everyone else, but it is not yet yours."),
            leo("And it becomes mine when?"),
            maya("When you settle it. Complete deletes it, abandon hands the lock back, dead-letter moves it aside with a reason, defer sets it aside to fetch later."),
            leo("So where did the second copy come from?"),
            maya("Clue three: the lock duration. The default is one minute, and that tablet handshake takes three. The lock ran out, the message came back, a second worker took it."),
            leo("And the cure?"),
            maya("Renew the lock while the work runs, or raise the lock duration; five minutes is the ceiling. Not ReceiveAndDelete, which removes the message on handover and would have lost it instead."),
            leo("Third file. A pile of orders nobody ever processed."),
            maya("Not lost either. Every failed try raises a message's delivery count. Past the max delivery count, ten by default, Service Bus moves it to the dead-letter queue."),
            leo("Which somebody empties."),
            maya("Nobody empties it for you. That was the quiet failure: a year of poison messages sitting there. Zaiqa reads it every morning now, prints the reason on each one, and resends what a fix has cured."),
            leo("Any other open complaints?"),
            maya("Two. A kitchen that got its nine o'clock before its eight. Several workers pull one line, so orders can cross; turn on sessions, a group of messages with one session id delivered in order to one receiver."),
            leo("And the other?"),
            maya("A payment retry that sent the same order twice. Duplicate detection, on Standard and Premium, drops a repeat with the same message id inside a window you set. A pre-order goes in as a scheduled message, invisible until its time."),
            leo("One last thing. Finance asked whether every order could also reach the loyalty service."),
            maya("Then it is not a queue. A topic gives every subscription its own copy of every message: ratings prompt, loyalty points, finance report, one subscription each, with a filter rule if one wants only Mumbai."),
            leo("And the tier?"),
            maya("Basic has queues only. Standard adds topics and duplicate detection. Premium adds a private endpoint, a door onto your own private network, and raises the biggest message to a hundred megabytes."),
            leo("Case closed. What goes in the summary for the exam?"),
            maya("Must not be lost in a crash: PeekLock. Every service needs a copy: a topic with subscriptions. One kitchen in order: sessions. Keeps failing: the dead-letter queue, after ten tries."),
            leo("And our two culprits?"),
            maya("A three-minute job run twice means the lock duration ran out, so renew it. And a worker on IIS means the idle timeout: use a Windows Service, a Function, Container Apps or a WebJob."),
            leo("Anything to try today?"),
            maya("Create a namespace and an orders queue. Send three messages with DefaultAzureCredential. Receive them in PeekLock, complete two and dead-letter one. Then read the dead-letter queue and print the reason."),
            leo("No sleeping application pools. Case closed. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-servicebus",
        listOf(
            maya("Okay, did you hear what happened to the Zaiqa orders queue this week? Sit down. Four separate people broke it, one a day, and Friday was the best one."),
            leo("I heard a restaurant got the same order three times. Start at Monday."),
            maya("Monday was Tom. Until Monday our API, the .NET service the phone talks to, was calling the restaurant tablet directly and waiting for it to answer."),
            leo("Waiting for a tablet in a kitchen."),
            maya("At eight on a Friday, with the wifi dropping. The diner watched a spinner for ninety seconds. Tom finally did the right thing and put a queue in between."),
            leo("Which does what, exactly?"),
            maya("A queue is one line of messages, each handled by exactly one receiver. The API drops a message saying order four four seven one is paid, answers the phone, and something else delivers it later."),
            leo("And Tom built this queue himself?"),
            maya("He created a namespace, which is your own post office building with your queues inside it, and a queue called orders. The queue is a managed service; Microsoft runs the machines. Nothing of ours on a box."),
            leo("So Monday was a success. [chuckle] Tuesday?"),
            maya("Tuesday was Dana, and Dana asked the question everybody asks. Where does the thing that reads the queue actually live?"),
            leo("The worker. Which is just a program, no?"),
            maya("Your program, and you host it. Zaiqa's is the order worker, a small .NET service whose only job is to read that queue. Service Bus never calls your code. Your code pulls."),
            leo("And Dana put it where?"),
            maya("On IIS. In an application pool, next to an old web app, because, quote, it is .NET, that is where .NET goes."),
            leo("And by Wednesday morning?"),
            maya("It was dead. An IIS application pool starts when a request arrives, and with no request for twenty minutes it is idle-timed out and shut down. A worker never gets a request."),
            leo("[sigh] So it kept quietly shutting itself off."),
            maya("Mid-job, overnight, and nothing looked broken. IIS hosts things that answer calls. A worker answers nothing."),
            leo("Where did it end up?"),
            maya("Container Apps with no ingress, which means no public door, scaling on how many messages are waiting. Dana's other options were a Windows Service on a plain server, an Azure Function with a Service Bus trigger, an App Service WebJob, or a Kubernetes deployment."),
            leo("And Priya made everyone a poster, I hear."),
            maya("She did. An API is called and answers while the caller waits; a worker is driven by messages already in the queue, and nobody waits for it."),
            leo("What else was on it?"),
            maya("An API needs a public door; a worker has no door and dials out. An API scales on requests a second; a worker scales on how deep the queue is."),
            leo("And the last line?"),
            maya("If the API is down, the diner sees an error now. If the worker is down, nothing visibly breaks and the messages pile up. [pause] Which is why Dana's Tuesday went unnoticed until Wednesday."),
            leo("Wednesday. The triple order."),
            maya("Marcus. The worker receives in PeekLock, the default mode: the message is put aside and hidden from everyone else, like a library hold, but it is not yours until you settle it."),
            leo("Settle how?"),
            maya("Complete deletes it. Abandon hands the lock back for another try. Dead-letter moves it aside with a reason. Defer sets it aside to fetch later."),
            leo("And Marcus settled nothing?"),
            maya("Marcus settled fine. Marcus left the lock duration at its default of one minute, and the tablet handshake takes three. The lock ran out twice, two more workers grabbed it, and the kitchen printed three."),
            leo("What should he have done?"),
            maya("Renewed the lock while the work ran, or raised the lock duration; five minutes is the ceiling. And nobody suggest ReceiveAndDelete, which removes the message on handover and loses it in a crash."),
            leo("Thursday."),
            maya("The intern found a dead-letter queue with four thousand messages in it, from a restaurant that closed in March."),
            leo("What put them there?"),
            maya("Service Bus did. Every failed try raises a message's delivery count, and past the max delivery count, ten by default, it moves the message to the dead-letter queue, a side queue every queue comes with."),
            leo("And nothing was reading it."),
            maya("Nothing reads it for you. [sigh] There is a morning script now: open it, print the reason on each message, resend the ones a fix has cured."),
            leo("Friday. You promised me Friday."),
            maya("Priya again, and it was a good one. Finance wanted every delivered order, and the ratings prompt wanted every delivered order, and Priya gave them both the same queue."),
            leo("So they stole each other's messages."),
            maya("One each, at random. A queue gives each message to one receiver. A topic gives every subscription its own copy. It is a topic now, with three subscriptions and a filter rule on the Mumbai one."),
            leo("Anything else the week taught us?"),
            maya("Three small ones. Sessions, a group of messages with one session id delivered in order to one receiver, so a kitchen's orders cannot cross. Duplicate detection, which drops a repeat with the same message id."),
            leo("And the third?"),
            maya("Scheduled messages for pre-orders: hand it over now with a time on it, invisible until then. Oh, and Tom wants Premium, for a private endpoint, a door onto our own private network, and hundred megabyte messages."),
            leo("Do we really need messages of a hundred megabytes?"),
            maya("No. Dish photos go in Blob Storage. But it is on the exam."),
            leo("Speaking of which, what is the gossip I should remember for the exam?"),
            maya("Must survive a crash: PeekLock. Every service needs a copy: a topic with subscriptions. One kitchen in order: sessions. Keeps failing: the dead-letter queue, after ten tries."),
            leo("And the two that made the week?"),
            maya("Three-minute jobs running twice means the lock duration ran out, so renew it. A worker on IIS means the idle timeout: a Windows Service, a Function, Container Apps or a WebJob instead."),
            leo("And something to try today, so I am not next week's story?"),
            maya("Create a namespace and an orders queue. Send three messages with DefaultAzureCredential. Receive them in PeekLock, complete two and dead-letter one. Then read the dead-letter queue and print the reason."),
            leo("No workers on IIS. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-servicebus",
        listOf(
            nova("Hey, you. It is late, the kitchen is closed, and it is just the three of us. Me, Ivy, and you, still awake. [pause] Tonight: the queue Zaiqa has been pretending to have since the first episode."),
            ivy("Pretending. Go on."),
            nova("Our API, the .NET service the phone talks to, takes your payment, writes the order into Azure SQL, and then sends the order to the restaurant. [pause] It has been sending it nowhere."),
            ivy("And nobody noticed?"),
            nova("Somebody noticed on a Friday. Tonight we build the orders queue properly."),
            ivy("Start with why. Why not just ring the kitchen?"),
            nova("Because the kitchen is a kitchen. The wifi drops, the printer jams, the tablet is face down under a menu. [pause] You do not make a hungry person wait for that."),
            ivy("So the API writes a note and walks away."),
            nova("A message: order four four seven one is paid. It goes into a queue, which is one line of messages, each handled by exactly one receiver. The API answers your phone at once."),
            ivy("And where does the queue live?"),
            nova("In a namespace: your own post office building, with its own address, your queues inside. [pause] It is a managed service. You deploy nothing for it. Microsoft keeps the lights on."),
            ivy("Then who comes and collects?"),
            nova("A worker. Your code, your problem, nobody else's. Zaiqa's is the order worker, a small .NET service whose only job is to read that queue. Service Bus never calls you; you pull."),
            ivy("And where does a little worker sleep at night?"),
            nova("[pause] Not on IIS. That is the one everybody reaches for, and it is wrong."),
            ivy("Why? It is .NET on a Windows box."),
            nova("An IIS application pool wakes when a request arrives, and if none arrives for twenty minutes it is idle-timed out and shut down. Your worker never gets a request."),
            ivy("So it gets tidied away mid-job."),
            nova("At three in the morning, with nothing looking broken. [pause] IIS is for things that answer calls. A worker answers nothing."),
            ivy("Then give me the good homes."),
            nova("A Windows Service on a plain server: starts with the machine, runs forever, no web server. In Azure, an Azure Function with a Service Bus trigger, run once per message."),
            ivy("More."),
            nova("Container Apps with no ingress, which means no public door, scaling on how many messages are waiting. An App Service WebJob. Or a Kubernetes deployment, if you keep one anyway."),
            ivy("And say it slowly: what makes a worker not an API?"),
            nova("An API is called, and answers while the caller waits. A worker is driven by messages already in the queue, and nobody is waiting for it. [pause] One is a doorbell. The other is a to-do pile."),
            ivy("Doors, then."),
            nova("An API needs a public door on the internet. A worker has no door at all. It dials out and pulls."),
            ivy("And when the rush comes?"),
            nova("An API scales on requests a second. A worker scales on how deep the queue is. A thousand orders waiting, start more copies."),
            ivy("And when one of them dies?"),
            nova("That is the loveliest part. If the API is down, you see an error now. If the worker is down, nothing visibly breaks. [pause] The messages wait, and get handled later."),
            ivy("Mm. Now the worker has the message. Is it gone from the queue?"),
            nova("Not yet. The default receive mode is PeekLock. Think of a library hold: put aside for you, hidden from everyone else, and not yours until you check it out."),
            ivy("And checking out is?"),
            nova("Settling it. Complete deletes it. Abandon hands the lock back for another try. Dead-letter moves it aside with a reason. Defer sets it aside to fetch later."),
            ivy("And the mode you do not want?"),
            nova("ReceiveAndDelete. Gone the moment it is handed over. [pause] Fine for something you can lose. Never for a paid dinner."),
            ivy("How long is the hold good for?"),
            nova("That is the lock duration. One minute by default, five minutes at most."),
            ivy("And our tablet takes three."),
            nova("So the lock runs out mid-handshake, the message comes back, another worker takes it, and the kitchen cooks twice. Renew the lock while the work runs, or raise the lock duration."),
            ivy("And the message that can never work?"),
            nova("Every try raises its delivery count. Past the max delivery count, ten by default, it moves to the dead-letter queue: a side queue every queue comes with, for mail that could not be delivered."),
            ivy("Who goes and looks?"),
            nova("You do, or nobody does. [sigh] Zaiqa reads it every morning, prints the reason on each message, and resends the ones a fix has cured."),
            ivy("And one kitchen's orders, in order?"),
            nova("Turn on sessions: a group of messages sharing one session id, delivered in order to one receiver at a time. That kitchen gets a single file of its own, while everyone else carries on."),
            ivy("And the same order sent twice by a nervous payment service?"),
            nova("Duplicate detection, on Standard and Premium. Give every message an id of your own, and a repeat inside your window is simply dropped. [pause] A pre-order goes in as a scheduled message, invisible until eight."),
            ivy("And when several of you want the same message?"),
            nova("Then not a queue. A topic gives every subscription its own copy: the ratings prompt, the loyalty points, the finance report. A subscription can filter too, and a new one accepts everything until you remove its default rule."),
            ivy("Tiers, before I drift."),
            nova("Basic, queues only. Standard adds topics and duplicate detection. Premium adds a private endpoint, a door onto your own private network, and a hundred megabyte message instead of two hundred and fifty-six kilobytes."),
            ivy("Mm. Before I sleep. What do I remember for the exam?"),
            nova("Must survive a crash: PeekLock. Everyone needs a copy: a topic with subscriptions. One kitchen in order: sessions. Keeps failing: the dead-letter queue, after ten tries."),
            ivy("And the two that bite?"),
            nova("A three-minute job running twice is the lock duration, so renew it. And a worker on IIS is the idle timeout: a Windows Service, a Function, Container Apps or a WebJob instead."),
            ivy("And something to try today, before bed?"),
            nova("Create a namespace and an orders queue. Send three messages with DefaultAzureCredential. [pause] Receive them in PeekLock, complete two and dead-letter one. Then read the dead-letter queue and print the reason."),
            ivy("Nobody waiting, nothing lost. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-servicebus",
        listOf(
            nova("Tonight is date night, darling. Candles, something with far too much chilli in it, and you and me finally building the queue Zaiqa has been pretending to have."),
            rafe("You know how to show a man a good time. Pretending how?"),
            nova("Our API, the .NET service the phone talks to, takes the payment, writes the order into Azure SQL, and then sends it to the restaurant. Into nothing. There was never a queue."),
            rafe("And the restaurant tablet? Why not call it straight?"),
            nova("Quiz me properly and I will tell you."),
            rafe("Fine. Exam style. The API must answer the diner in under a second, but telling the kitchen can take a minute and sometimes fails. What goes in between?"),
            nova("A queue. One line of messages, each handled by exactly one receiver. The API drops order four four seven one is paid, and answers the phone at once."),
            rafe("Right answer. [kiss]"),
            nova("Mm. And it lives in a namespace: your own post office building, with its own address and your queues inside it. Which brings me to my question, love. Who collects?"),
            rafe("A worker. And before you ask, no, it does not go on IIS."),
            nova("[pause] Go on then. Why not?"),
            rafe("An IIS application pool wakes when a request arrives. With no request for twenty minutes it is idle-timed out and shut down, and a worker never gets a request. It would die mid-job, at three in the morning."),
            nova("Right answer, and you were waiting to say that. [kiss]"),
            rafe("I was. So where does it sleep?"),
            nova("A Windows Service on a plain server: starts with the machine, runs forever, no web server. In Azure: a Function with a Service Bus trigger, Container Apps with no ingress, which means no public door, an App Service WebJob, or a Kubernetes deployment."),
            rafe("And the queue itself? Do we host that too?"),
            nova("Never. It is a managed service. You create the namespace and the queue, Microsoft runs the machines. The only thing you deploy is the worker. [pause] Now, your turn to be difficult."),
            rafe("Then tell me what a worker is, if it is not an API."),
            nova("An API is called and answers while the caller waits. A worker is driven by messages already in the queue, and nobody is waiting for it. One has a public door; the other has no door and dials out."),
            rafe("And on a Friday night?"),
            nova("An API scales on requests a second. A worker scales on how deep the queue is. And if the API is down you see an error now, while a worker being down just lets the messages pile up."),
            rafe("Right answer, all four. [kiss]"),
            nova("Mm. My turn, and this one is real. The worker crashes halfway through an order. Which receive mode means the order is not lost?"),
            rafe("PeekLock. The message is locked, not removed. Like a library hold: put aside for you, hidden from everyone else, not yours until you check it out."),
            nova("Right answer, darling. [kiss] And checking out is settling it: complete deletes it, abandon hands the lock back, dead-letter moves it aside with a reason, defer sets it aside for later."),
            rafe("And the other mode?"),
            nova("ReceiveAndDelete. Gone on handover. Fine for a statistic, never for a paid dinner. Now: the tablet handshake takes three minutes, the queue is on its defaults, and the kitchen cooks everything twice. Why?"),
            rafe("Somebody set the max delivery count too high."),
            nova("[sigh] No kiss for that one. It is the lock duration: one minute by default, and five at most. The lock runs out mid-job, the message becomes visible again, and a second worker takes it."),
            rafe("And the cure is to renew the lock while the work runs."),
            nova("Or raise the lock duration. Now use the thing you reached for. A malformed order fails every single time. Where does it end up, and after how many tries?"),
            rafe("In the dead-letter queue, after ten, because that is the max delivery count by default. Every failed try raises the message's delivery count."),
            nova("Right answer, and you earned that one back. [kiss]"),
            rafe("And somebody empties it?"),
            nova("Nobody but you. We read it every morning, print the reason on each message, and resend what a fix has cured. [pause] My last easy one: one kitchen's orders must arrive in the order they were placed."),
            rafe("Sessions. A group of messages sharing one session id, delivered in order to one receiver at a time. Put the restaurant id in it."),
            nova("Right answer. Everything in its proper order. [kiss]"),
            rafe("Now mine. The payment service gets nervous and sends the same order twice."),
            nova("Duplicate detection, on Standard and Premium. Give every message an id of your own, and a repeat with that id inside your window is dropped before anyone sees it."),
            rafe("And a diner who books dinner at eight, over breakfast?"),
            nova("A scheduled message. Handed over now with a time on it, invisible until then. No timer in our worker."),
            rafe("Right answer, twice over. [kiss] One more, and the exam loves it. The ratings prompt, the loyalty points and the finance report each need every delivered order."),
            nova("Then not a queue, which gives each message to one receiver. A topic, with a subscription each, every subscription getting its own copy. And a filter rule if one of them only wants Mumbai."),
            rafe("And the catch everybody misses?"),
            nova("A new subscription comes with a default rule that accepts everything. Add your filter, remove the default, or it still hears the lot."),
            rafe("And tiers, since we are being thorough."),
            nova("Basic, queues only. Standard adds topics and duplicate detection. Premium adds a private endpoint, a door onto your own private network, and raises the biggest message to a hundred megabytes."),
            rafe("Which we will never need."),
            nova("Never. The dish photo goes in Blob Storage and the message carries its name. But it is on the exam, love, so say the number."),
            rafe("A hundred megabytes. So what do we remember for the exam?"),
            nova("Must survive a crash: PeekLock. Everyone needs a copy: a topic with subscriptions. One kitchen in order: sessions. Keeps failing: the dead-letter queue, after ten tries."),
            rafe("And the two that caught us tonight?"),
            nova("A three-minute job running twice is the lock duration, so renew it. And a worker on IIS is the idle timeout: a Windows Service, a Function, Container Apps or a WebJob instead."),
            rafe("Six for six, near enough. And something to try today, darling?"),
            nova("Create a namespace and an orders queue. Send three messages with DefaultAzureCredential. Receive them in PeekLock, complete two and dead-letter one. Then read the dead-letter queue and print the reason."),
            rafe("Nobody waiting, nothing lost. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun flow() = DiagramSpec(
        "The API drops a paid order into the queue and answers the phone; the worker collects it and tells the kitchen.",
        nodes = listOf(
            DNode("api", "Zaiqa API", 0.25f, 0.18f, w = 0.4f, h = 0.2f, tone = 1, sub = "answers at once"),
            DNode("q", "Orders queue", 0.75f, 0.18f, w = 0.42f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "the message waits"),
            DNode("worker", "Order worker", 0.5f, 0.52f, w = 0.5f, h = 0.22f, tone = 1, sub = "reads the queue"),
            DNode("kitchen", "Restaurant", 0.5f, 0.86f, w = 0.44f, h = 0.2f, sub = "the kitchen tablet"),
        ),
        edges = listOf(
            DEdge("api", "q", "sends"),
            DEdge("q", "worker", "collects"),
            DEdge("worker", "kitchen", "delivers"),
        ),
        aspect = 0.8f,
    )

    private fun hosting() = DiagramSpec(
        "The worker is your own code and needs a home; an IIS app pool with no inbound traffic is idle-timed out.",
        nodes = listOf(
            DNode("worker", "Order worker", 0.5f, 0.13f, w = 0.6f, h = 0.2f, tone = 1, sub = "your code to host"),
            DNode("iis", "IIS app pool", 0.25f, 0.45f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "idle timeout"),
            DNode("svc", "Windows Service", 0.75f, 0.45f, w = 0.44f, h = 0.2f, sub = "starts with the box"),
            DNode("fn", "Function trigger", 0.25f, 0.8f, w = 0.44f, h = 0.2f, sub = "one run a message"),
            DNode("ca", "Container Apps", 0.75f, 0.8f, w = 0.44f, h = 0.2f, sub = "no ingress"),
        ),
        edges = listOf(
            DEdge("worker", "iis", "wrong", dashed = true),
            DEdge("worker", "svc"),
            DEdge("worker", "fn"),
            DEdge("worker", "ca"),
        ),
        aspect = 0.8f,
    )

    private fun sides() = DiagramSpec(
        "An API is called and answers while the caller waits; a worker is driven by the messages already queued.",
        nodes = listOf(
            DNode("api", "The API", 0.25f, 0.15f, w = 0.4f, h = 0.2f, tone = 1, sub = "called, you wait"),
            DNode("worker", "The worker", 0.75f, 0.15f, w = 0.4f, h = 0.2f, tone = 1, sub = "pulls messages"),
            DNode("door", "Public door", 0.25f, 0.48f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "needs one"),
            DNode("nodoor", "No door", 0.75f, 0.48f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, sub = "dials out"),
            DNode("rps", "Error now", 0.25f, 0.82f, w = 0.4f, h = 0.2f, kind = NodeKind.PILL, sub = "if it is down"),
            DNode("depth", "Pile up", 0.75f, 0.82f, w = 0.4f, h = 0.2f, kind = NodeKind.PILL, sub = "if it is down"),
        ),
        edges = listOf(
            DEdge("api", "door"),
            DEdge("worker", "nodoor"),
            DEdge("door", "rps"),
            DEdge("nodoor", "depth"),
        ),
        aspect = 0.8f,
    )

    private fun settle() = DiagramSpec(
        "PeekLock locks the message instead of removing it; the worker then settles it one of four ways.",
        nodes = listOf(
            DNode("msg", "Locked message", 0.5f, 0.13f, w = 0.6f, h = 0.2f, tone = 1, sub = "PeekLock: a library hold"),
            DNode("complete", "Complete", 0.25f, 0.45f, w = 0.44f, h = 0.2f, sub = "done: delete it"),
            DNode("abandon", "Abandon", 0.75f, 0.45f, w = 0.44f, h = 0.2f, sub = "lock back, retry"),
            DNode("defer", "Defer", 0.25f, 0.8f, w = 0.44f, h = 0.2f, sub = "fetch later"),
            DNode("dl", "Dead-letter", 0.75f, 0.8f, w = 0.44f, h = 0.2f, tone = 2, sub = "aside, with a reason"),
        ),
        edges = listOf(
            DEdge("msg", "complete"),
            DEdge("msg", "abandon"),
            DEdge("msg", "defer"),
            DEdge("msg", "dl"),
        ),
        aspect = 0.8f,
    )

    private fun lockRun() = DiagramSpec(
        "A three-minute tablet call outlives the one-minute lock: the message reappears and the kitchen cooks twice.",
        nodes = listOf(
            DNode("w1", "Worker 1", 0.25f, 0.15f, w = 0.4f, h = 0.2f, tone = 1, sub = "3-minute call"),
            DNode("lockN", "Lock duration", 0.75f, 0.15f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "1 minute default"),
            DNode("w2", "Worker 2", 0.25f, 0.5f, w = 0.4f, h = 0.2f, tone = 2, sub = "takes it too"),
            DNode("again", "Visible again", 0.75f, 0.5f, w = 0.44f, h = 0.2f, tone = 2, sub = "delivery count up"),
            DNode("fix", "Renew or raise", 0.5f, 0.85f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "five minutes at most"),
        ),
        edges = listOf(
            DEdge("w1", "lockN", "holds"),
            DEdge("lockN", "again", "expires"),
            DEdge("again", "w2"),
            DEdge("lockN", "fix", "the fix", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun dlq() = DiagramSpec(
        "Each failed try raises the delivery count; past the max it moves to the dead-letter queue, which you must read.",
        nodes = listOf(
            DNode("poison", "Closed kitchen", 0.25f, 0.15f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "fails every time"),
            DNode("count", "Delivery count", 0.75f, 0.15f, w = 0.44f, h = 0.2f, sub = "one more each try"),
            DNode("shelf", "Dead-letter queue", 0.25f, 0.55f, w = 0.44f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "returned-mail shelf"),
            DNode("max", "Max delivery count", 0.75f, 0.5f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "10 by default"),
            DNode("repair", "Morning script", 0.5f, 0.87f, w = 0.5f, h = 0.2f, tone = 1, sub = "read, fix, resend"),
        ),
        edges = listOf(
            DEdge("poison", "count", "each try"),
            DEdge("count", "max"),
            DEdge("max", "shelf", "moved"),
            DEdge("shelf", "repair", "every morning"),
        ),
        aspect = 0.8f,
    )

    private fun fanOut() = DiagramSpec(
        "A topic gives every subscription its own copy of each delivered order; a rule can filter what one of them hears.",
        nodes = listOf(
            DNode("pub", "Zaiqa API", 0.5f, 0.12f, w = 0.44f, h = 0.18f, tone = 1, sub = "publishes once"),
            DNode("topic", "order-events", 0.5f, 0.42f, w = 0.44f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "topic"),
            DNode("s1", "ratings", 0.17f, 0.82f, w = 0.3f, h = 0.22f, sub = "own copy"),
            DNode("s2", "loyalty", 0.5f, 0.82f, w = 0.3f, h = 0.22f, sub = "own copy"),
            DNode("s3", "finance", 0.83f, 0.82f, w = 0.3f, h = 0.22f, kind = NodeKind.NOTE, sub = "city filter"),
        ),
        edges = listOf(
            DEdge("pub", "topic"),
            DEdge("topic", "s1"),
            DEdge("topic", "s2"),
            DEdge("topic", "s3"),
        ),
        aspect = 0.8f,
    )

    private fun extras() = DiagramSpec(
        "Sessions keep one kitchen in order, duplicate detection drops a repeat, and a pre-order is sent on a timer.",
        nodes = listOf(
            DNode("sess", "Sessions", 0.25f, 0.15f, w = 0.44f, h = 0.2f, sub = "one kitchen, in order"),
            DNode("dup", "Duplicates", 0.75f, 0.15f, w = 0.44f, h = 0.2f, sub = "same id, dropped"),
            DNode("sched", "Scheduled", 0.25f, 0.5f, w = 0.44f, h = 0.2f, sub = "hidden until eight"),
            DNode("prem", "Premium tier", 0.75f, 0.5f, w = 0.44f, h = 0.2f, tone = 1, sub = "private endpoint"),
            DNode("size", "100 MB message", 0.5f, 0.85f, w = 0.5f, h = 0.2f, kind = NodeKind.PILL, sub = "Standard: 256 KB"),
        ),
        edges = listOf(
            DEdge("sess", "sched"),
            DEdge("dup", "prem"),
            DEdge("prem", "size"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Service Bus questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("crash", "Survives a crash", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "PeekLock"),
            DNode("copy", "Each needs a copy", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a topic"),
            DNode("order", "One kitchen, in order", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "sessions"),
            DNode("fails", "Keeps failing", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "dead letters"),
            DNode("twice", "Done twice", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "lock duration"),
            DNode("iis", "A worker on IIS", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "idle timeout"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("crash", "copy", "order", "fails", "twice", "iis")
    private val today = BoardCard("Today", "A namespace, an orders queue, three messages, PeekLock, then the dead letters.")
    private val lockCard = BoardCard("Lock duration", "One minute by default, five at most. Renew it while the work runs.")
    private val peekCard = BoardCard("PeekLock", "A library hold: put aside for you, not yours until you check it out.")
    private val settleCard = BoardCard("Settling", "Complete, abandon, dead-letter or defer. Four ways to finish with it.")
    private val iisCard = BoardCard("Why not IIS", "No inbound requests, so the app pool is idle-timed out mid-job.")
    private val managedCard = BoardCard("The queue is managed", "You deploy nothing for it. The only thing you deploy is the worker.")
    private val dlqCard = BoardCard("Dead-letter queue", "Past the max delivery count, ten by default. Nothing drains it for you.")
    private val phraseCard = BoardCard("Listen for the phrase", "Four phrases, four answers: PeekLock, a topic, sessions, dead letters.")
    private val queueCard = BoardCard("Queue", "One line of messages, each handled by exactly one receiver.")
    private val sessionCard = BoardCard("Sessions", "One session id, delivered in order to one receiver at a time.")
    private val driveCard = BoardCard("Who drives it", "An API is called; a worker is driven by what is already queued.")
    private val doorCard = BoardCard("Doors", "An API needs a public one. A worker has none and dials out.")
    private val scaleCard = BoardCard("Scaling", "An API on requests a second; a worker on how deep the queue is.")
    private val svcCard = BoardCard("Windows Service", "Starts with the machine, runs forever, no web server involved.")
    private val morningCard = BoardCard("Read it every morning", "Open it, print the reason on each message, resend what is fixed.")
    private val tiersCard = BoardCard("The tiers", "Basic: queues. Standard: topics. Premium: a private endpoint, 100 MB.")
    private val biteCard = BoardCard("And the two that bite", "A job run twice is the lock; a worker on IIS is the idle timeout.")

    private fun classicBoard() = PodcastBoard(
        "ai-servicebus",
        listOf(
            // 0: "Hi, and welcome back. Zaiqa is in good shape..."
            Scene(
                0, "The queue we never built", flow(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Today", "The orders queue the API has been writing into nothing since episode one.")),
                    Beat(4, show = setOf("kitchen"), light = setOf("kitchen"), card = BoardCard("Why not call the kitchen", "The wifi drops, the printer jams, the app is closed mid-rush.")),
                    Beat(6, show = setOf("q"), light = setOf("api", "q"), pulse = "api" to "q", card = BoardCard("The API answers at once", "Take the payment, write the SQL row, drop a message, reply.")),
                    Beat(8, light = setOf("q"), card = queueCard),
                    Beat(10, light = setOf("q"), card = BoardCard("One line, three cashiers", "Add a cashier and it moves faster; nobody is served twice.")),
                    Beat(12, light = setOf("q"), card = BoardCard("Namespace", "Your own post office building, with your queues inside it.")),
                ),
            ),
            // 13: "Right. Now the question I actually want answered..."
            Scene(
                13, "Where the worker lives", hosting(),
                beats = listOf(
                    Beat(14, show = setOf("worker"), light = setOf("worker"), card = BoardCard("The order worker", "A small .NET program that reads the queue. Your code, your host.")),
                    Beat(16, show = setOf("iis"), light = setOf("iis"), pulse = "worker" to "iis", card = iisCard),
                    Beat(18, light = setOf("iis"), card = BoardCard("IIS answers calls", "A worker answers nothing, so it is killed mid-job in the night.")),
                    Beat(20, show = setOf("svc"), light = setOf("svc"), pulse = "worker" to "svc", card = svcCard),
                    Beat(22, show = setOf("fn", "ca"), light = setOf("fn", "ca"), pulse = "worker" to "ca", card = BoardCard("In Azure", "A Function with a Service Bus trigger, or Container Apps, no ingress.")),
                    Beat(24, light = setOf("fn"), card = BoardCard("And two more", "An App Service WebJob, or a Kubernetes deployment.")),
                    Beat(26, light = setOf("worker"), card = managedCard),
                ),
            ),
            // 27: "Then say it plainly. What is the difference..."
            Scene(
                27, "API against worker", sides(),
                beats = listOf(
                    Beat(28, light = setOf("api", "worker"), card = driveCard),
                    Beat(30, light = setOf("door", "nodoor"), pulse = "worker" to "nodoor", card = doorCard),
                    Beat(32, light = setOf("api", "worker"), card = scaleCard),
                    Beat(34, light = setOf("rps", "depth"), pulse = "nodoor" to "depth", card = BoardCard("When it is down", "The diner sees an error now; or the messages quietly pile up.")),
                ),
            ),
            // 35: "That is the clearest thing anyone has told me..."
            Scene(
                35, "Locked, not removed", settle(),
                beats = listOf(
                    Beat(36, show = setOf("msg"), light = setOf("msg"), card = peekCard),
                    Beat(38, show = setOf("complete", "abandon", "defer", "dl"), light = setOf("complete", "abandon", "defer", "dl"), pulse = "msg" to "complete", card = settleCard),
                    Beat(40, light = setOf("msg"), card = BoardCard("ReceiveAndDelete", "Gone on handover. Fine for a statistic, never for a paid order.")),
                ),
            ),
            // 41: "How long does the lock last?"
            Scene(
                41, "When the lock runs out", lockRun(),
                beats = listOf(
                    Beat(42, light = setOf("lockN"), card = lockCard),
                    Beat(44, light = setOf("again", "w2"), pulse = "lockN" to "again", card = BoardCard("The kitchen cooks twice", "The lock expires mid-job and a second worker takes the message.")),
                ),
            ),
            // 45: "And a message nobody can ever handle?..."
            Scene(
                45, "The returned-mail shelf", dlq(),
                beats = listOf(
                    Beat(46, light = setOf("count", "max"), pulse = "max" to "shelf", card = dlqCard),
                    Beat(48, light = setOf("repair"), pulse = "shelf" to "repair", card = morningCard),
                ),
            ),
            // 49: "Now, one restaurant's orders. Do they arrive in order?"
            Scene(
                49, "Order, repeats and timers", extras(),
                beats = listOf(
                    Beat(50, light = setOf("sess"), card = sessionCard),
                    Beat(52, light = setOf("sess"), card = BoardCard("The restaurant id", "That kitchen gets a single file; the others carry on in parallel.")),
                    Beat(54, light = setOf("dup"), pulse = "dup" to "prem", card = BoardCard("Duplicate detection", "Your own message id; a repeat inside your window is dropped.")),
                    Beat(56, light = setOf("sched"), pulse = "sess" to "sched", card = BoardCard("Scheduled message", "Handed over now with a time on it, invisible until then.")),
                ),
            ),
            // 57: "And the topics you keep promising?"
            Scene(
                57, "When a queue is not enough", fanOut(),
                beats = listOf(
                    Beat(58, light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Topic", "Every subscription gets its own copy of every message.")),
                    Beat(60, light = setOf("s1", "s2", "s3"), pulse = "topic" to "s3", card = BoardCard("The default rule", "A new subscription accepts everything. Remove it when you add yours.")),
                ),
            ),
            // 61: "Last one. We are on Standard. When would Zaiqa go Premium?"
            Scene(
                61, "Standard against Premium", extras(),
                beats = listOf(
                    Beat(62, light = setOf("prem", "size"), pulse = "prem" to "size", card = BoardCard("Premium", "A private endpoint, and a hundred megabyte message instead of 256 KB.")),
                    Beat(64, light = setOf("size"), card = BoardCard("Not that we need it", "A dish photo goes in Blob Storage; the message carries its name.")),
                ),
            ),
            // 65: "Then what do I remember for the exam?"
            Scene(
                65, "For the exam", exam(),
                beats = listOf(
                    Beat(66, show = all6, light = setOf("crash", "copy", "order", "fails"), card = phraseCard),
                    Beat(68, light = setOf("twice", "iis"), card = biteCard),
                    Beat(70, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-servicebus--case-files",
        listOf(
            // 0: "Welcome to Case Files. Tonight's case..."
            Scene(
                0, "The dark tablet", flow(),
                beats = listOf(
                    Beat(0, show = setOf("api", "kitchen"), light = setOf("kitchen"), card = BoardCard("The case", "The diner paid, the app said ordered, and the tablet stayed dark.")),
                    Beat(2, light = setOf("api"), card = BoardCard("The API was fine", "It took the payment and wrote the order row in Azure SQL.")),
                    Beat(6, show = setOf("q"), light = setOf("q"), pulse = "api" to "q", card = queueCard),
                    Beat(8, light = setOf("q"), card = BoardCard("Namespace", "Your own post office building. Microsoft runs the machines.")),
                    Beat(10, light = setOf("q"), card = BoardCard("Clue one", "The message sat there all evening. Nothing came to collect it.")),
                    Beat(12, show = setOf("worker"), light = setOf("worker"), pulse = "q" to "worker", card = BoardCard("The order worker", "Your code pulls from the queue. Service Bus never calls you.")),
                ),
            ),
            // 13: "And where was ours running?"
            Scene(
                13, "Clue two: the app pool", hosting(),
                beats = listOf(
                    Beat(14, show = setOf("worker", "iis"), light = setOf("iis"), pulse = "worker" to "iis", card = BoardCard("Clue two", "Somebody had hosted the worker on IIS, in an application pool.")),
                    Beat(16, light = setOf("iis"), card = iisCard),
                    Beat(18, light = setOf("iis"), card = BoardCard("Asleep at twenty past", "It took a half-finished order with it. IIS answers calls.")),
                    Beat(20, show = setOf("svc"), light = setOf("svc"), pulse = "worker" to "svc", card = svcCard),
                    Beat(22, show = setOf("fn", "ca"), light = setOf("fn", "ca"), pulse = "worker" to "fn", card = BoardCard("Or in Azure", "A Function trigger, Container Apps with no ingress, a WebJob, Kubernetes.")),
                ),
            ),
            // 23: "I want the difference written down..."
            Scene(
                23, "For the file", sides(),
                beats = listOf(
                    Beat(24, light = setOf("api", "worker"), card = driveCard),
                    Beat(26, light = setOf("door", "nodoor"), pulse = "api" to "door", card = doorCard),
                    Beat(28, light = setOf("api", "worker"), card = scaleCard),
                    Beat(30, light = setOf("rps", "depth"), pulse = "nodoor" to "depth", card = BoardCard("Why nobody saw it", "A dead worker breaks nothing visibly. The messages pile up.")),
                ),
            ),
            // 31: "Case nearly closed. But there is a second file..."
            Scene(
                31, "The second file", settle(),
                beats = listOf(
                    Beat(32, show = setOf("msg"), light = setOf("msg"), card = peekCard),
                    Beat(34, show = setOf("complete", "abandon", "defer", "dl"), light = setOf("complete", "abandon", "dl"), pulse = "msg" to "abandon", card = settleCard),
                ),
            ),
            // 35: "So where did the second copy come from?"
            Scene(
                35, "Clue three: the lock", lockRun(),
                beats = listOf(
                    Beat(36, light = setOf("lockN", "w1"), pulse = "lockN" to "again", card = lockCard),
                    Beat(38, light = setOf("fix"), pulse = "lockN" to "fix", card = BoardCard("The cure", "Renew it, or raise it. Not ReceiveAndDelete, which loses the order.")),
                ),
            ),
            // 39: "Third file. A pile of orders nobody ever processed."
            Scene(
                39, "The third file", dlq(),
                beats = listOf(
                    Beat(40, light = setOf("count", "max"), pulse = "max" to "shelf", card = dlqCard),
                    Beat(42, light = setOf("repair"), pulse = "shelf" to "repair", card = BoardCard("The quiet failure", "A year of poison messages. Read it, print the reason, resend.")),
                    Beat(44, light = setOf("shelf"), card = sessionCard),
                    Beat(46, light = setOf("poison"), card = BoardCard("Duplicate detection", "A repeat with the same message id, inside the window, is dropped.")),
                ),
            ),
            // 47: "One last thing. Finance asked..."
            Scene(
                47, "Finance wants a copy", fanOut(),
                beats = listOf(
                    Beat(48, light = setOf("topic", "s1", "s2", "s3"), pulse = "topic" to "s2", card = BoardCard("Topic", "Every subscription gets its own copy. A rule filters one of them.")),
                    Beat(50, light = setOf("pub"), card = tiersCard),
                ),
            ),
            // 51: "Case closed. What goes in the summary for the exam?"
            Scene(
                51, "Case closed", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("crash", "copy", "order", "fails"), card = phraseCard),
                    Beat(54, light = setOf("twice", "iis"), card = BoardCard("Our two culprits", "The lock duration ran out, and a worker was living on IIS.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-servicebus--gossip",
        listOf(
            // 0: "Okay, did you hear what happened to the Zaiqa orders queue..."
            Scene(
                0, "Monday: Tom", flow(),
                beats = listOf(
                    Beat(0, show = setOf("api", "kitchen"), light = setOf("api"), card = BoardCard("Four people, four days", "They broke the orders queue one a day, and Friday was the best.")),
                    Beat(2, light = setOf("api", "kitchen"), card = BoardCard("Monday", "The API was calling the kitchen tablet and waiting for it.")),
                    Beat(4, light = setOf("kitchen"), card = BoardCard("Ninety seconds", "A spinner at eight on a Friday, with the wifi dropping.")),
                    Beat(6, show = setOf("q"), light = setOf("q"), pulse = "api" to "q", card = queueCard),
                    Beat(8, light = setOf("q"), card = BoardCard("Namespace", "Your own post office building. The queue is a managed service.")),
                ),
            ),
            // 10: "Tuesday was Dana..."
            Scene(
                10, "Tuesday: Dana", hosting(),
                beats = listOf(
                    Beat(10, show = setOf("worker"), light = setOf("worker"), card = BoardCard("Dana's question", "Where does the thing that reads the queue actually live?")),
                    Beat(12, light = setOf("worker"), card = BoardCard("The order worker", "Your program. Service Bus never calls your code; your code pulls.")),
                    Beat(14, show = setOf("iis"), light = setOf("iis"), pulse = "worker" to "iis", card = BoardCard("On IIS", "In an application pool, because that is where .NET goes.")),
                    Beat(16, light = setOf("iis"), card = iisCard),
                    Beat(18, light = setOf("iis"), card = BoardCard("Dead by Wednesday", "Mid-job, overnight, and nothing looked broken.")),
                    Beat(20, show = setOf("ca", "svc", "fn"), light = setOf("ca"), pulse = "worker" to "ca", card = BoardCard("Where it went", "Container Apps, no ingress, scaling on how many messages wait.")),
                ),
            ),
            // 21: "And Priya made everyone a poster, I hear."
            Scene(
                21, "Priya's poster", sides(),
                beats = listOf(
                    Beat(22, light = setOf("api", "worker"), card = driveCard),
                    Beat(24, light = setOf("door", "nodoor"), pulse = "api" to "door", card = BoardCard("Doors and scaling", "A public door against none; requests a second against queue depth.")),
                    Beat(26, light = setOf("rps", "depth"), pulse = "nodoor" to "depth", card = BoardCard("The last line", "A dead worker breaks nothing visibly. That is why Tuesday hid.")),
                ),
            ),
            // 27: "Wednesday. The triple order."
            Scene(
                27, "Wednesday: Marcus", settle(),
                beats = listOf(
                    Beat(28, show = setOf("msg"), light = setOf("msg"), card = peekCard),
                    Beat(30, show = setOf("complete", "abandon", "defer", "dl"), light = setOf("complete", "abandon", "defer", "dl"), pulse = "msg" to "defer", card = settleCard),
                ),
            ),
            // 31: "And Marcus settled nothing?"
            Scene(
                31, "The triple order", lockRun(),
                beats = listOf(
                    Beat(32, light = setOf("lockN", "w2"), pulse = "lockN" to "again", card = lockCard),
                    Beat(34, light = setOf("fix"), pulse = "lockN" to "fix", card = BoardCard("What Marcus missed", "Renew the lock, or raise it. Never ReceiveAndDelete for an order.")),
                ),
            ),
            // 35: "Thursday."
            Scene(
                35, "Thursday: the intern", dlq(),
                beats = listOf(
                    Beat(36, light = setOf("shelf"), card = BoardCard("Four thousand messages", "From a restaurant that closed in March, and nobody looking.")),
                    Beat(38, light = setOf("count", "max"), pulse = "max" to "shelf", card = dlqCard),
                    Beat(40, light = setOf("repair"), pulse = "shelf" to "repair", card = morningCard),
                ),
            ),
            // 41: "Friday. You promised me Friday."
            Scene(
                41, "Friday: Priya again", fanOut(),
                beats = listOf(
                    Beat(42, light = setOf("s1", "s3"), card = BoardCard("One queue, two readers", "Finance and ratings both wanted every delivered order.")),
                    Beat(44, light = setOf("topic", "s1", "s2", "s3"), pulse = "pub" to "topic", card = BoardCard("Topic", "Every subscription gets its own copy. A filter rule on the Mumbai one.")),
                ),
            ),
            // 45: "Anything else the week taught us?"
            Scene(
                45, "What else the week taught", extras(),
                beats = listOf(
                    Beat(46, light = setOf("sess", "dup"), pulse = "dup" to "prem", card = BoardCard("Sessions and duplicates", "One session id in order; a repeat with the same id dropped.")),
                    Beat(48, light = setOf("sched", "prem"), pulse = "prem" to "size", card = BoardCard("Scheduled, and Premium", "Invisible until eight; a private endpoint and 100 MB messages.")),
                    Beat(50, light = setOf("size"), card = BoardCard("Not that we need it", "Dish photos go in Blob Storage. But it is on the exam.")),
                ),
            ),
            // 51: "Speaking of which, what is the gossip..."
            Scene(
                51, "For the exam", exam(),
                beats = listOf(
                    Beat(52, show = all6, light = setOf("crash", "copy", "order", "fails"), card = phraseCard),
                    Beat(54, light = setOf("twice", "iis"), card = BoardCard("The two that made the week", "Marcus and the lock duration, Dana and the IIS idle timeout.")),
                    Beat(56, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-servicebus--late-night",
        listOf(
            // 0: "Hey, you. It is late, the kitchen is closed..."
            Scene(
                0, "Sending it nowhere", flow(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Tonight", "The queue Zaiqa has been pretending to have since the first episode.")),
                    Beat(2, light = setOf("api"), card = BoardCard("Pretending", "It takes your payment, writes the SQL row, and sends it nowhere.")),
                    Beat(6, show = setOf("kitchen"), light = setOf("kitchen"), card = BoardCard("Why not ring the kitchen", "The wifi drops, the printer jams, the tablet is under a menu.")),
                    Beat(8, show = setOf("q"), light = setOf("q"), pulse = "api" to "q", card = queueCard),
                    Beat(10, light = setOf("q"), card = BoardCard("Namespace", "Your own post office building. You deploy nothing for the queue.")),
                ),
            ),
            // 11: "Then who comes and collects?"
            Scene(
                11, "Where a worker sleeps", hosting(),
                beats = listOf(
                    Beat(12, show = setOf("worker"), light = setOf("worker"), card = BoardCard("The order worker", "Your code, your problem. Service Bus never calls you; you pull.")),
                    Beat(14, show = setOf("iis"), light = setOf("iis"), pulse = "worker" to "iis", card = BoardCard("Not on IIS", "The one everybody reaches for, and it is the wrong one.")),
                    Beat(16, light = setOf("iis"), card = iisCard),
                    Beat(18, light = setOf("iis"), card = BoardCard("Tidied away mid-job", "At three in the morning, with nothing looking broken.")),
                    Beat(20, show = setOf("svc", "fn"), light = setOf("svc", "fn"), pulse = "worker" to "svc", card = BoardCard("The good homes", "A Windows Service, or a Function with a Service Bus trigger.")),
                    Beat(22, show = setOf("ca"), light = setOf("ca"), pulse = "worker" to "ca", card = BoardCard("And two more", "Container Apps with no ingress, a WebJob, or Kubernetes.")),
                ),
            ),
            // 23: "And say it slowly: what makes a worker not an API?"
            Scene(
                23, "Doorbell and to-do pile", sides(),
                beats = listOf(
                    Beat(24, light = setOf("api", "worker"), card = driveCard),
                    Beat(26, light = setOf("door", "nodoor"), pulse = "worker" to "nodoor", card = doorCard),
                    Beat(28, light = setOf("api", "worker"), card = scaleCard),
                    Beat(30, light = setOf("rps", "depth"), pulse = "door" to "rps", card = BoardCard("When it is down", "An error now, or messages that simply wait and go later.")),
                ),
            ),
            // 31: "Mm. Now the worker has the message..."
            Scene(
                31, "Locked, not removed", settle(),
                beats = listOf(
                    Beat(32, show = setOf("msg"), light = setOf("msg"), card = peekCard),
                    Beat(34, show = setOf("complete", "abandon", "defer", "dl"), light = setOf("complete", "abandon", "defer", "dl"), pulse = "msg" to "complete", card = settleCard),
                    Beat(36, light = setOf("msg"), card = BoardCard("ReceiveAndDelete", "Gone on handover. Fine for a statistic, never for a paid dinner.")),
                ),
            ),
            // 37: "How long is the hold good for?"
            Scene(
                37, "How long the hold lasts", lockRun(),
                beats = listOf(
                    Beat(38, light = setOf("lockN"), card = lockCard),
                    Beat(40, light = setOf("again", "w2"), pulse = "lockN" to "again", card = BoardCard("The kitchen cooks twice", "The lock runs out mid-handshake and another worker takes it.")),
                ),
            ),
            // 41: "And the message that can never work?"
            Scene(
                41, "The shelf nobody reads", dlq(),
                beats = listOf(
                    Beat(42, light = setOf("count", "max"), pulse = "max" to "shelf", card = dlqCard),
                    Beat(44, light = setOf("repair"), pulse = "shelf" to "repair", card = morningCard),
                ),
            ),
            // 45: "And one kitchen's orders, in order?"
            Scene(
                45, "Order, repeats and timers", extras(),
                beats = listOf(
                    Beat(46, light = setOf("sess"), pulse = "sess" to "sched", card = sessionCard),
                    Beat(48, light = setOf("dup", "sched"), pulse = "dup" to "prem", card = BoardCard("Duplicates and timers", "A repeat with the same id dropped; a pre-order hidden until eight.")),
                ),
            ),
            // 49: "And when several of you want the same message?"
            Scene(
                49, "When several want it", fanOut(),
                beats = listOf(
                    Beat(50, light = setOf("topic", "s1", "s2", "s3"), pulse = "pub" to "topic", card = BoardCard("Topic", "Every subscription gets its own copy, and can filter what it hears.")),
                    Beat(52, light = setOf("pub"), card = tiersCard),
                ),
            ),
            // 53: "Mm. Before I sleep. What do I remember for the exam?"
            Scene(
                53, "For the exam", exam(),
                beats = listOf(
                    Beat(54, show = all6, light = setOf("crash", "copy", "order", "fails"), card = phraseCard),
                    Beat(56, light = setOf("twice", "iis"), card = biteCard),
                    Beat(58, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-servicebus--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night, and no queue", flow(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Tonight", "Building the queue Zaiqa has been pretending to have all along.")),
                    Beat(2, light = setOf("api"), card = BoardCard("Into nothing", "It takes the payment, writes the SQL row, and sends it nowhere.")),
                    Beat(5, show = setOf("kitchen"), light = setOf("kitchen"), card = BoardCard("Quiz: what goes between", "The API answers in a second; telling the kitchen can take a minute.")),
                    Beat(6, show = setOf("q"), light = setOf("q"), pulse = "api" to "q", card = queueCard),
                    Beat(8, light = setOf("q"), card = BoardCard("Namespace", "Your own post office building, with your queues inside it.")),
                ),
            ),
            // 9: "A worker. And before you ask, no, it does not go on IIS."
            Scene(
                9, "Not on IIS", hosting(),
                beats = listOf(
                    Beat(10, show = setOf("worker", "iis"), light = setOf("iis"), pulse = "worker" to "iis", card = BoardCard("Quiz: why not IIS", "It is .NET on a Windows box. What is wrong with an app pool?")),
                    Beat(12, light = setOf("iis"), card = iisCard),
                    Beat(14, show = setOf("svc", "fn", "ca"), light = setOf("svc", "fn", "ca"), pulse = "worker" to "svc", card = BoardCard("The good homes", "Windows Service, Function trigger, Container Apps, WebJob, Kubernetes.")),
                    Beat(16, light = setOf("worker"), card = managedCard),
                ),
            ),
            // 17: "Then tell me what a worker is, if it is not an API."
            Scene(
                17, "What a worker is not", sides(),
                beats = listOf(
                    Beat(18, light = setOf("api", "worker", "door", "nodoor"), pulse = "worker" to "nodoor", card = BoardCard("Called, or driven", "An API answers while you wait. A worker has no door at all.")),
                    Beat(20, light = setOf("rps", "depth"), pulse = "nodoor" to "depth", card = BoardCard("Scaling and failure", "Requests a second against queue depth; an error now against a pile.")),
                ),
            ),
            // 22: "Mm. My turn, and this one is real..."
            Scene(
                22, "Quiz: surviving a crash", settle(),
                beats = listOf(
                    Beat(22, show = setOf("msg"), light = setOf("msg"), card = BoardCard("Quiz: which receive mode", "The worker crashes halfway through. The order must not be lost.")),
                    Beat(24, show = setOf("complete", "abandon", "defer", "dl"), light = setOf("msg"), pulse = "msg" to "complete", card = peekCard),
                    Beat(26, light = setOf("complete", "abandon", "defer", "dl"), card = settleCard),
                ),
            ),
            // 27: "Somebody set the max delivery count too high."
            Scene(
                27, "No kiss for that one", lockRun(),
                beats = listOf(
                    Beat(28, light = setOf("lockN", "again"), pulse = "lockN" to "again", card = lockCard),
                    Beat(30, light = setOf("fix"), pulse = "lockN" to "fix", card = BoardCard("The cure", "Renew the lock while the work runs, or raise the lock duration.")),
                ),
            ),
            // 31: "In the dead-letter queue, after ten..."
            Scene(
                31, "Quiz: it fails every time", dlq(),
                beats = listOf(
                    Beat(32, light = setOf("count", "max"), pulse = "max" to "shelf", card = dlqCard),
                    Beat(34, light = setOf("repair"), pulse = "shelf" to "repair", card = BoardCard("Nobody but you", "Read it every morning, print the reason, resend what is fixed.")),
                ),
            ),
            // 35: "Sessions. A group of messages sharing one session id..."
            Scene(
                35, "Order, repeats and timers", extras(),
                beats = listOf(
                    Beat(36, light = setOf("sess"), pulse = "sess" to "sched", card = sessionCard),
                    Beat(38, light = setOf("dup"), pulse = "dup" to "prem", card = BoardCard("Duplicate detection", "Your own message id; a repeat inside your window is dropped.")),
                    Beat(40, light = setOf("sched"), card = BoardCard("Scheduled message", "Handed over now with a time on it, invisible until then.")),
                ),
            ),
            // 41: "Right answer, twice over. One more..."
            Scene(
                41, "Quiz: three want a copy", fanOut(),
                beats = listOf(
                    Beat(42, light = setOf("topic", "s1", "s2", "s3"), pulse = "pub" to "topic", card = BoardCard("Topic", "Every subscription gets its own copy. A rule filters one of them.")),
                    Beat(44, light = setOf("s3"), card = BoardCard("The default rule", "A new subscription accepts everything until you remove it.")),
                ),
            ),
            // 45: "And tiers, since we are being thorough."
            Scene(
                45, "Standard against Premium", extras(),
                beats = listOf(
                    Beat(46, light = setOf("prem", "size"), pulse = "prem" to "size", card = BoardCard("Premium", "A private endpoint, and a hundred megabyte message instead of 256 KB.")),
                    Beat(48, light = setOf("size"), card = BoardCard("Say the number", "Photos go in Blob Storage, but the exam asks for a hundred megabytes.")),
                ),
            ),
            // 49: "A hundred megabytes. So what do we remember for the exam?"
            Scene(
                49, "For the exam", exam(),
                beats = listOf(
                    Beat(50, show = all6, light = setOf("crash", "copy", "order", "fails"), card = phraseCard),
                    Beat(52, light = setOf("twice", "iis"), card = BoardCard("The two that caught us", "The lock duration ran out, and a worker was living on IIS.")),
                    Beat(54, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
