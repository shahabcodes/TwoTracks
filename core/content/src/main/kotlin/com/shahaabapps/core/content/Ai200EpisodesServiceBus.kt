package com.shahaabapps.core.content

/** Service Bus for AI back ends: queues, topics and dead letters: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesServiceBus {
    private val classic = Podcast(
        "ai-servicebus",
        listOf(
            maya("Hi, and welcome back. Today is about letting slow work wait its turn. Say you're building the Two Tracks Coach, a study helper in an Android app, with a small Python server on Azure, Microsoft's cloud. When a learner saves a note, the server turns it into numbers an AI search can use. That takes seconds, and sometimes the AI service is busy."),
            leo("And the learner just stares at a spinner? [pause] Not great."),
            maya("So the server writes a small job ticket instead: embed note 42. Those numbers are called an embedding: a list the AI writes for the note, like a spot on a map, so notes that mean similar things land close together. The ticket is called a message. The server drops it off and answers the learner at once."),
            leo("And who does the slow part?"),
            maya("A separate program, called a worker. It picks the ticket up and does the slow part later, when it's ready. The server and the worker never have to wait for each other."),
            leo("And the server drops the ticket off where?"),
            maya("At Azure Service Bus. Think of it as a post office between the parts of your app. The sender doesn't wait, and the worker doesn't even need to be running yet. The message waits safely until someone collects it."),
            leo("And the post office has, what, pigeonholes?"),
            maya("Queues. A queue is one line of messages, and each message is handled by exactly one receiver. Like a bakery queue with three cashiers: add a cashier and the line moves faster, but nobody is served twice. The Coach's queue is called embed-jobs."),
            leo("Where do I create a queue?"),
            maya("Inside a namespace. That's your own post office building, with its own address, holding your queues. It comes in tiers, like sizes. Basic has queues only. Standard and Premium add topics, which we'll get to."),
            leo("And how does the server get in? A password?"),
            maya("Better than that. The Python package has a helper called DefaultAzureCredential. It finds a sign-in by itself: your own login on your laptop, the app's own identity in the cloud, a badge Azure gives the app. No secret to copy anywhere."),
            leo("So anyone with an identity can send?"),
            maya("Only if you allow it. You give the app's identity a role, a named permission. Azure Service Bus Data Sender lets the server send. Azure Service Bus Data Receiver lets the worker receive."),
            leo("Okay. The worker takes the message. Is it gone from the queue?"),
            maya("Not yet, and that's the clever part. The default way to receive is PeekLock. Think of a library hold: the book is put aside for you and hidden from everyone else, but it's not yours until you check it out. The message is locked, not removed."),
            leo("Why not just take it?"),
            maya("Because workers crash. If the worker dies halfway, the lock runs out, and the message shows up in the queue again for another try. Nothing lost. The other mode, ReceiveAndDelete, removes it the moment it's handed over. Faster, but a crash loses it."),
            leo("So with PeekLock, how does the worker say it's done?"),
            maya("It settles the message, which just means it says what happened. Complete means finished: delete it. Abandon means give the lock back, so the message can be tried again soon. Those two you'll use most."),
            leo("There are more?"),
            maya("Two more. Defer sets a message aside, to fetch later by its number, when the worker is ready for it."),
            leo("And the last one?"),
            maya("Dead-letter. It moves the message to a special side queue, with a reason attached. We'll come back to that one."),
            leo("So when do I abandon, and when do I dead-letter?"),
            maya("For example, the AI service answers 429, which means too many requests, slow down. That's temporary, so the worker abandons, and the message comes back for another try. But if the note is empty, no retry will ever fix it. Dead-letter it, with a reason like EmptyNote."),
            leo("And the lock. How long does it last?"),
            maya("That's the lock duration: one minute by default, five at most. Here's a classic trap. A worker takes three minutes per job, and jobs get done twice. Why? The lock ran out after one minute, the message reappeared, and a second worker started it too."),
            leo("[sigh] So the fix is a longer lock?"),
            maya("Raise the lock duration, up to five minutes, or renew the lock while the work runs. The Python package has a helper for that, AutoLockRenewer, which keeps topping it up. The Coach set its queue to two minutes."),
            leo("What about a message that fails every single time?"),
            maya("That's called a poison message. Each time it comes back, its delivery count, the number of tries so far, goes up by one."),
            leo("And it just keeps coming back, forever?"),
            maya("No. Once it passes the max delivery count, ten by default, Service Bus moves it to the dead-letter queue by itself. So a poison message can't loop forever."),
            leo("Dead-letter queue. Sounds grim."),
            maya("[chuckle] It's the returned-mail shelf. Every queue has one built in. But nothing reads it for you, so failed jobs pile up quietly. The Coach has a small repair script that reads it every morning, checks each reason, fixes the cause, and sends a fresh copy back."),
            leo("Okay. Now, what if two services both need every saved note?"),
            maya("Picture a second service, a progress tracker, that also wants to know when a note is saved. On a queue, the worker and the tracker would compete: each message goes to one of them, never both. You need a topic."),
            leo("What's a topic, in plain words?"),
            maya("A newsletter. You publish once, and every subscriber gets their own copy. Each subscriber is a subscription, which is really a private queue on the topic. The Coach publishes to a topic called note-events, with one subscription for search and one for progress."),
            leo("Can a subscription pick only some messages?"),
            maya("Yes, with filter rules. A SQL filter is a short test on a message's labels, written like a database query: track equals german. One catch: a new subscription starts with a default rule that accepts everything. Add your filter but leave that rule, and you still get everything."),
            leo("And if one learner's messages must stay in order?"),
            maya("That's a session. Each message carries a session id, say the learner's id, and Service Bus hands each session to one receiver at a time, in order. First in, first out, per learner."),
            leo("So what do I remember for the exam?"),
            maya("If each consumer needs every message, it's a topic with a subscription each. If the work must not be lost when a worker crashes, PeekLock. If one user's messages must stay in order, sessions. If a message keeps failing, it lands in the dead-letter queue after the max delivery count."),
            leo("And the jobs done twice?"),
            maya("Long work with the default lock means the lock expires first. Renew the lock, or raise the lock duration. And for a temporary failure, abandon."),
            leo("Got it. Anything to try today?"),
            maya("Fifteen minutes, a few cents. Create a namespace and an embed-jobs queue. Send three messages from Python with DefaultAzureCredential. Receive them in PeekLock, complete two and dead-letter one. Then read the dead-letter queue and print the reason."),
            leo("A post office that never loses my mail. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-servicebus",
        listOf(
            maya("Welcome to Case Files. Today's case: The Notes Done Twice. A small Android app called Lingo Lark helps people learn languages, with a Python server on Azure, Microsoft's cloud. Every saved note goes to an AI service that turns it into numbers for search. This month, the AI bill doubled. And some notes were never processed at all."),
            leo("[pause] Detective Leo is on the case. How was the work meant to flow, in plain words?"),
            maya("When a note is saved, the server writes a job ticket: embed note 42. Those numbers are called an embedding: a list the AI writes for the note, like a spot on a map, so notes that mean similar things land close together. The ticket is called a message."),
            leo("And it hands the ticket to whom?"),
            maya("To Azure Service Bus, a post office between the parts of the app. The server answers the user at once. A separate program, called a worker, collects the ticket later and does the slow AI call."),
            leo("And the ticket waits where?"),
            maya("In a queue: one line of messages, where each message is handled by exactly one receiver. Like a bakery queue with several cashiers. More cashiers, faster line, nobody served twice."),
            leo("And where does the queue live?"),
            maya("In a namespace, Lingo Lark's own post office building, with its own address. Theirs is on the Standard tier, a mid-sized plan."),
            leo("What is a queue really, under the picture?"),
            maya("A store that Service Bus keeps on Microsoft's servers. Messages are written to disk and stay there until a receiver settles them, or they expire. So the sender and the worker never need to be running at the same time."),
            leo("Then my first suspect for the double bill: the server sends every message twice."),
            maya("Good instinct. Service Bus even has duplicate detection for that, on Standard and Premium: give each message an id, and a resend with the same id inside a time window is dropped. But the logs show one send per note. Wrong suspect."),
            leo("Then the worker is buggy and calls the AI twice."),
            maya("Closer. Here's the clue. Each job takes about three minutes. The worker receives in PeekLock mode, the default. Think of a library hold: the book is put aside and hidden from everyone else, but it isn't yours until you check it out. The message is locked, not removed."),
            leo("And the lock lasts how long?"),
            maya("That's the lock duration. One minute by default. Under the hood, Service Bus gives the worker a lock with a timer. When the timer runs out, the message becomes visible again, and its delivery count, the number of tries, goes up by one."),
            leo("[pause] So after one minute, a second worker grabs the same note. Three-minute job, two workers. Double bill."),
            maya("Detective Leo, finally. The fix: raise the lock duration, five minutes at most, or renew the lock while the work runs. The Python package has a helper, AutoLockRenewer, that keeps topping it up."),
            leo("And the notes that were never processed? Same thing?"),
            maya("Second witness. Last month, someone switched the receive mode to ReceiveAndDelete to make it faster. That mode removes the message the moment it's handed over. Then a worker crashed mid-job, and those messages were simply gone."),
            leo("So PeekLock is there exactly so a crash loses nothing."),
            maya("Exactly. With PeekLock, the worker settles each message, saying what happened. Complete when done: delete it. Abandon on a temporary failure: give the lock back, so it's tried again. Like when the AI service sends back 429, the code that means too many requests, slow down."),
            leo("And when it fails for good?"),
            maya("Third clue, and it's in a place nobody looked. Every queue has a built-in dead-letter queue, the returned-mail shelf, for messages that can't be processed."),
            leo("And how does a message end up on the shelf?"),
            maya("Each failed try raises its delivery count. Once it passes the max delivery count, ten by default, Service Bus moves it there by itself. Lingo Lark's shelf had three thousand messages on it."),
            leo("Three thousand! Suspect: Service Bus is broken."),
            maya("[sigh] Service Bus did its job. Most were poison messages: empty notes that fail every time. Each one was tried ten times, paid for ten times, then dead-lettered. The worker should dead-letter those itself, at once, with a reason like EmptyNote. No retry will ever fix an empty note."),
            leo("And who reads the dead-letter queue?"),
            maya("Nobody, unless you do. Nothing reads it for you. Lingo Lark now runs a small repair script every morning: it reads each dead letter, checks the reason, fixes the cause, and sends a fresh copy to the main queue."),
            leo("Any loose ends in the file?"),
            maya("One. The progress tracker never saw new notes. It read from the same queue as the worker. On a queue, they compete: each message goes to one of them, never both. When two services each need every message, you need a topic."),
            leo("A topic being?"),
            maya("A newsletter. You publish once, and every subscription gets its own copy. A subscription is really a private queue on the topic. Topics need the Standard or Premium tier; Basic has queues only."),
            leo("And the german-only subscription in the file? It still got everything."),
            maya("Last clue. A subscription can have a filter rule, a test on a message's labels, like track equals german. But a new subscription starts with a default rule that accepts everything. They added their filter and left the default in place. Remove it, and only German notes arrive."),
            leo("So where does Service Bus stop helping?"),
            maya("It delivers at least once, so now and then a message can arrive twice. Make the work safe to repeat: embedding the same note twice should just overwrite the same numbers."),
            leo("And if one learner's messages must stay in order?"),
            maya("Use a session: messages share a session id, say the learner's id, and go, in order, to one receiver at a time. And if you only need to announce that something happened, the Event Grid episode covers that."),
            leo("So, the case summary for the exam?"),
            maya("Jobs done twice with long work: the lock expired, so renew it or raise the lock duration. Must not lose a job if the worker crashes: PeekLock. Keeps failing: the dead-letter queue, after the max delivery count. Each consumer needs every message: a topic. One user's messages in order: sessions."),
            leo("Case closed. Anything to try today?"),
            maya("Create a namespace and an embed-jobs queue. Send three messages from Python with DefaultAzureCredential, a helper that signs in for you, with no secret. Receive them in PeekLock, complete two and dead-letter one. Then read the dead-letter queue and print the reason."),
            leo("One note, one bill. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-servicebus",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? They moved the slow AI work in the Two Tracks Coach, the study helper in the Android app, onto Azure Service Bus. And the post office had quite a week."),
            leo("[laugh] I heard someone found three thousand letters on a shelf. What's the post office?"),
            maya("Service Bus. A post office between the parts of your app. When a learner saves a note, the Python server writes a job ticket: embed note 42, meaning turn it into numbers for AI search, like a spot on a map for its meaning. That ticket is called a message."),
            leo("And then?"),
            maya("The server drops it off and answers the learner at once. A worker, a separate program, collects it later and calls the AI service. Nobody waits on anybody."),
            leo("And the message waits in what?"),
            maya("A queue. One line of messages, and each message is handled by exactly one receiver. Like a bakery queue with three cashiers. More cashiers, faster line, nobody served twice."),
            leo("And the queue lives where?"),
            maya("In a namespace, the team's own post office building, with its own address. It holds all their queues."),
            leo("What is a queue really, under the picture?"),
            maya("A store Service Bus keeps on Microsoft's servers. Messages are written to disk and wait there until a receiver settles them. So the worker can be down for an hour, and nothing is lost. Okay. Monday. Omar."),
            leo("What did Omar do?"),
            maya("Omar wanted the worker faster. So he switched the receive mode to ReceiveAndDelete. That mode removes the message the moment it's handed over. Then the worker crashed mid-job. Those notes? Gone."),
            leo("[sigh] What should it have been?"),
            maya("PeekLock, the default. Think of a library hold: the book is put aside and hidden from everyone else, but it isn't yours until you check it out. The message is locked, not removed. If the worker dies, the lock runs out, and the message comes back."),
            leo("And how does the worker check it out?"),
            maya("It settles the message, saying what happened. Complete means done: delete it. Abandon means give the lock back, so it's tried again, like when the AI service sends back 429, the code that means too many requests, slow down. Okay. Tuesday. Dana."),
            leo("Go."),
            maya("Dana's jobs took three minutes each. And the AI bill showed every note embedded twice. Because the lock duration, how long the hold lasts, is one minute by default."),
            leo("So after a minute, the hold expired."),
            maya("Exactly. Under the hood, Service Bus gives the worker a lock with a timer. When the timer runs out, the message is visible again, and its delivery count, the number of tries, goes up. A second worker grabbed it."),
            leo("How did Dana fix it?"),
            maya("She raised the lock duration to two minutes, five is the most, and renews the lock during long calls with a helper called AutoLockRenewer. One note, one bill."),
            leo("Wednesday. Let me guess, someone else's turn."),
            maya("Marcus. Marcus added a progress tracker that also needed every saved note. He pointed it at the same queue as the worker."),
            leo("And on a queue, they compete."),
            maya("Each message went to one of them, never both. Half the notes never reached search, the other half never reached progress. When each service needs every message, you need a topic."),
            leo("A topic, in plain words?"),
            maya("A newsletter. Publish once, and every subscription gets its own copy. A subscription is a private queue on the topic. Topics need the Standard or Premium tier; Basic has queues only. Marcus made a topic called note-events, with one subscription each."),
            leo("Thursday. The intern?"),
            maya("The intern. Bless them. They made a subscription that should only get German notes. They added a SQL filter rule, a short test on the message's labels: track equals german. And the subscription still got everything."),
            leo("Why?"),
            maya("Because a new subscription starts with a default rule that accepts everything. The intern added their rule, and left the default in place. Remove it, and only German notes arrive."),
            leo("And Friday? There's always a Friday."),
            maya("Friday was Priya, and the shelf. Every queue has a built-in dead-letter queue, the returned-mail shelf, for messages that can't be processed."),
            leo("How does a message get there?"),
            maya("Each failed try raises its delivery count. Once it passes the max delivery count, ten by default, Service Bus moves it to the shelf by itself. Priya opened the shelf for the first time. Three thousand messages."),
            leo("What were they?"),
            maya("Poison messages, mostly empty notes that fail every time. Each tried ten times, paid for ten times. Priya changed the worker to dead-letter an empty note at once, with a reason. And she wrote a repair script that reads the shelf every morning, fixes the cause, and resends a fresh copy."),
            leo("So nothing reads the dead-letter queue by itself?"),
            maya("Nothing. And that's where Service Bus stops helping. It delivers at least once, so a message can now and then arrive twice. Make the work safe to repeat."),
            leo("Anything else nobody did this week?"),
            maya("If one learner's messages must stay in order, use a session, which hands each learner's messages, in order, to one receiver at a time. Nobody needed it yet. Give it time. And the Event Grid episode covers simple announcements."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Must not lose a job if the worker crashes: PeekLock. Jobs done twice with long work: renew the lock or raise the lock duration. Each service needs every message: a topic. Keeps failing: the dead-letter queue, after the max delivery count. One user's messages in order: sessions."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a namespace and an embed-jobs queue. Send three messages from Python with DefaultAzureCredential, a helper that signs in for you, no secret. Receive them in PeekLock, complete two and dead-letter one. Then read the dead-letter queue and print the reason."),
            leo("Read your shelf, people. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-servicebus",
        listOf(
            nova("Hey, you. It's late, the streets are quiet, and you're still learning. [pause] Tonight, Ivy and I are talking about letters. The kind that wait patiently for you."),
            ivy("Letters, at this hour. How old-fashioned of you, Nova."),
            nova("Picture the Two Tracks Coach, the study helper in the Android app, with a Python server on Azure, Microsoft's cloud. A learner saves a note. Turning it into numbers for AI search takes seconds. [pause] Nobody should wait that long for anything."),
            ivy("So what does the server do instead?"),
            nova("It writes a small job ticket, embed note 42. Those numbers are called an embedding: a list the AI writes for the note, like a spot on a map, so notes that mean similar things land close together. The ticket is a message. It drops the message off and answers the learner at once."),
            ivy("And who does the slow part, while I sleep?"),
            nova("A separate program, called a worker. It picks the message up later, when it's ready. [pause] The server and the worker never have to wait for each other."),
            ivy("Drops it off where, though?"),
            nova("At Azure Service Bus. A post office between the parts of your app. The sender doesn't wait. The worker doesn't even have to be awake yet. The message waits for it, safely."),
            ivy("Waiting for someone who isn't awake yet. I know the feeling."),
            nova("Mm. It waits in a queue. One line of messages, and each one handled by exactly one receiver. Like a bakery queue with a few cashiers. More cashiers, faster line, nobody served twice."),
            ivy("And the queue lives where?"),
            nova("In a namespace, your own post office building, with its own address. It holds your queues, [pause] like a building holds its letterboxes."),
            ivy("Tell me what a queue really is. Under the picture."),
            nova("A store that Service Bus keeps on Microsoft's servers. Each message is written to disk and stays there until a receiver says it's done with it. [pause] So nothing is lost when the lights go out."),
            ivy("And when the worker finally comes for it?"),
            nova("By default, it receives in PeekLock mode. Like a library hold: the book is put aside for you, hidden from everyone else, but not yet yours. The message is locked, not removed."),
            ivy("Why not just take it home?"),
            nova("Because workers fall over, at the worst moments. If the worker dies halfway, the lock runs out, and the message comes back for another try. ReceiveAndDelete would hand it over and forget it at once. Faster. [pause] But a crash loses it forever."),
            ivy("So how does the worker say it's finished?"),
            nova("It settles the message, saying what happened. Complete: done, delete it. Abandon: give the lock back, so it's tried again, say when the AI service sends back 429, the code that means too many requests, slow down. Dead-letter: set it aside for good, with a reason."),
            ivy("And how long does the hold last?"),
            nova("That's the lock duration, one minute by default, five at most. Under the hood, Service Bus gives the worker a lock with a timer. When it runs out, the message is visible again, and its delivery count, the number of tries, goes up by one."),
            ivy("So if my job takes three minutes?"),
            nova("Then after one minute, the message reappears, and a second worker starts it too. Done twice, paid twice. [pause] So you raise the lock duration, or renew the lock while you work. A Python helper called AutoLockRenewer does that for you."),
            ivy("And a message that fails, again and again, all night?"),
            nova("That's a poison message. Each failure raises its delivery count. It can't go on forever, though."),
            ivy("What stops it?"),
            nova("The max delivery count, ten by default. Past it, Service Bus moves the message to the dead-letter queue. The returned-mail shelf. [pause] Every queue has one, built in."),
            ivy("And who reads the returned mail?"),
            nova("No one, unless you do. That's where it stops helping. Read the shelf, check each reason, fix the cause, send a fresh copy back. And it delivers at least once, so now and then a message arrives twice. Make the work safe to repeat."),
            ivy("Now, what if two services both want every note?"),
            nova("Then a queue would make them compete; each message goes to only one. You want a topic. A newsletter: publish once, and every subscription gets its own copy. A subscription is really a private queue on the topic."),
            ivy("Any catch?"),
            nova("Topics need the Standard or Premium tier, the bigger plans. Basic has queues only."),
            ivy("And can a subscription be choosy?"),
            nova("With a filter rule, like track equals german. But a new subscription starts with a default rule that accepts everything. [pause] Add your filter, and remove that default, or you still get it all."),
            ivy("And if my messages must arrive in order? I like things in order."),
            nova("Then a session. Messages share a session id, say your learner id, and go to one receiver at a time, in order. First in, first out."),
            ivy("And if I only want to whisper that something happened?"),
            nova("Then the Event Grid episode is your next late night. Announcements, not job tickets."),
            ivy("[chuckle] Noted. Before I sleep. What do I remember for the exam?"),
            nova("Must not lose a job if the worker crashes: PeekLock. Each service needs every message: a topic. One user's messages in order: sessions. Keeps failing: the dead-letter queue, after the max delivery count. Long jobs done twice: the lock expired, so renew it or raise the lock duration."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a namespace and an embed-jobs queue. Send three messages from Python with DefaultAzureCredential, a helper that signs in for you, no secret. [pause] Receive them in PeekLock, complete two and dead-letter one. Then read the dead-letter queue and print the reason."),
            ivy("Letters that wait for me. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-servicebus",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and a topic about patience. Azure Service Bus."),
            rafe("Patience. You've always been good at waiting for me."),
            nova("[laugh] Someone has to be. Picture the Two Tracks Coach, the study helper in the Android app, with a Python server on Azure, Microsoft's cloud. A learner saves a note, and the AI work behind it, turning the note into numbers for search, like a spot on a map for its meaning, takes seconds."),
            rafe("Nobody likes to be kept waiting. So what happens?"),
            nova("The server writes a job ticket, embed note 42. That ticket is called a message. It drops the message off and answers the learner at once."),
            rafe("Drops it off with whom?"),
            nova("With Service Bus, a post office between the parts of your app. A worker, a separate program, picks the message up later and does the slow part."),
            rafe("A note left on the pillow, to be read in the morning."),
            nova("Exactly. It waits in a queue, one line of messages, each handled by exactly one receiver. Under the picture, a queue is a store Service Bus keeps on disk, on Microsoft's servers. The message stays until a receiver says it's done with it."),
            rafe("And the queue lives in a namespace, our own little post office, with its own address. Then let me test you, darling. Exam style. The worker must not lose a job, even if it crashes halfway. Which receive mode?"),
            nova("PeekLock, the default. Like a library hold: the message is locked and hidden, not removed. If the worker crashes, the lock runs out, and the message comes back. ReceiveAndDelete would hand it over and forget it."),
            rafe("Right answer. Hold on, but don't let go too soon. [kiss]"),
            nova("Mm. And when the worker's done, it settles the message, saying what happened. Complete: delete it. Abandon: give the lock back, so it's tried again, say when the AI service sends back 429, the code that means too many requests."),
            rafe("And the hold lasts how long?"),
            nova("That's the lock duration, one minute by default. Under the hood, a lock with a timer. When it runs out, the message is visible again, and its delivery count, the number of tries, goes up."),
            rafe("One minute. I'd want longer."),
            nova("Then here's my question. A job takes three minutes, the lock is the default, and jobs get done twice. Why?"),
            rafe("Because the lock expires after one minute, the message reappears, and a second worker starts it too. Raise the lock duration, five minutes at most, or renew the lock while you work. AutoLockRenewer, in Python, keeps topping it up."),
            nova("Right answer. Some things shouldn't be rushed. [kiss]"),
            rafe("My question, then. A malformed job fails every single time. With default settings, where does it end up?"),
            nova("Somewhere quiet. It's gone for good, I suppose?"),
            rafe("No kiss for that one. Every failure raises its delivery count. Past the max delivery count, ten by default, Service Bus moves it to the dead-letter queue, the returned-mail shelf every queue has built in. Nothing is silently deleted."),
            nova("Then I'll make it up to you. And nothing reads that shelf for you. You read each reason, fix the cause, and send a fresh copy back."),
            rafe("And a job that can never succeed, like an empty note?"),
            nova("That's a poison message. The worker dead-letters it at once, with a reason. No retry will ever fix an empty note. Now my question. Every saved note must reach the search worker and, separately, the progress tracker. What do you use?"),
            rafe("A topic with two subscriptions. On one queue, they'd compete, and each message would go to only one of them. A topic is a newsletter: every subscription, a private queue on the topic, gets its own copy."),
            nova("Right answer. You never share me, either. [kiss]"),
            rafe("Never. But topics need the Standard or Premium tier, the bigger plans; Basic has queues only. And a subscription can filter, like track equals german."),
            nova("Then here's a tricky one. You add that filter rule, and the subscription still gets every message. Why?"),
            rafe("Because a new subscription starts with a default rule that accepts everything. Add your filter and remove the default, or it's still open to all."),
            nova("Right answer. Some doors should only open for one. [kiss]"),
            rafe("Last quiz. One learner's messages must arrive in order. What do you use?"),
            nova("A session. Messages share a session id, the learner's id, and go to one receiver at a time, in order. First in, first out."),
            rafe("Right answer. One at a time, and in the right order. [kiss]"),
            nova("And where does it all stop working, Rafe? Every romance has its limits."),
            rafe("It delivers at least once, so a message can now and then arrive twice: make the work safe to repeat. It doesn't fix poison messages; you do. And if you only want to announce that something happened, the Event Grid episode covers that."),
            nova("Then the last question, for the exam. What do we remember?"),
            rafe("Must not lose a job: PeekLock. Each service needs every message: a topic. In order per user: sessions. Keeps failing: the dead-letter queue, after the max delivery count. Long jobs done twice: renew the lock or raise the lock duration."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Create a namespace and an embed-jobs queue. Send three messages from Python with DefaultAzureCredential, a helper that signs in for you, no secret. Receive them in PeekLock, complete two and dead-letter one. Then read the dead-letter queue and print the reason."),
            rafe("And every message from me, delivered at least once. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun post() = DiagramSpec(
        "The API writes a message and answers at once; Service Bus holds it until a worker collects it.",
        nodes = listOf(
            DNode("api", "Coach API", 0.25f, 0.18f, w = 0.4f, h = 0.2f, tone = 1, sub = "answers at once"),
            DNode("msg", "Message", 0.75f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.PILL, sub = "embed note 42"),
            DNode("bus", "Service Bus", 0.5f, 0.52f, w = 0.5f, h = 0.22f, sub = "the post office"),
            DNode("worker", "Worker", 0.5f, 0.85f, w = 0.44f, h = 0.2f, tone = 1, sub = "does the slow part"),
        ),
        edges = listOf(
            DEdge("api", "msg", "writes"),
            DEdge("msg", "bus", "drop off"),
            DEdge("bus", "worker", "collects"),
        ),
        aspect = 0.8f,
    )

    private fun ns() = DiagramSpec(
        "A namespace is your own post office: queues on every tier, topics only on Standard and Premium.",
        nodes = listOf(
            DNode("ns", "Namespace", 0.5f, 0.15f, w = 0.6f, h = 0.2f, tone = 1, sub = "your post office"),
            DNode("q", "Queue", 0.25f, 0.5f, w = 0.44f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "one receiver each"),
            DNode("t", "Topic", 0.75f, 0.5f, w = 0.44f, h = 0.22f, sub = "Standard or Premium"),
            DNode("basic", "Basic tier", 0.25f, 0.85f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "queues only"),
        ),
        edges = listOf(DEdge("ns", "q"), DEdge("ns", "t")),
        aspect = 0.8f,
    )

    private fun settle() = DiagramSpec(
        "PeekLock locks the message instead of removing it; the worker then settles it one of four ways.",
        nodes = listOf(
            DNode("msg", "Locked message", 0.5f, 0.13f, w = 0.6f, h = 0.2f, tone = 1, sub = "PeekLock: a library hold"),
            DNode("complete", "Complete", 0.25f, 0.45f, w = 0.44f, h = 0.2f, sub = "done: delete it"),
            DNode("abandon", "Abandon", 0.75f, 0.45f, w = 0.44f, h = 0.2f, sub = "try again soon"),
            DNode("defer", "Defer", 0.25f, 0.8f, w = 0.44f, h = 0.2f, sub = "fetch later"),
            DNode("dl", "Dead-letter", 0.75f, 0.8f, w = 0.44f, h = 0.2f, tone = 2, sub = "side queue, reason"),
        ),
        edges = listOf(
            DEdge("msg", "complete"),
            DEdge("msg", "abandon"),
            DEdge("msg", "defer"),
            DEdge("msg", "dl"),
        ),
        aspect = 0.8f,
    )

    private fun lock() = DiagramSpec(
        "A three-minute job outlives the one-minute lock: the message reappears and a second worker starts it.",
        nodes = listOf(
            DNode("w1", "Worker 1", 0.25f, 0.15f, w = 0.4f, h = 0.2f, tone = 1, sub = "3-minute job"),
            DNode("lockN", "Lock", 0.75f, 0.15f, w = 0.4f, h = 0.2f, kind = NodeKind.PILL, sub = "1 minute default"),
            DNode("w2", "Worker 2", 0.25f, 0.5f, w = 0.4f, h = 0.2f, tone = 2, sub = "starts it too"),
            DNode("again", "Visible again", 0.75f, 0.5f, w = 0.44f, h = 0.2f, tone = 2, sub = "delivery count + 1"),
            DNode("fix", "Renew or raise", 0.5f, 0.85f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "AutoLockRenewer, up to 5 min"),
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
            DNode("poison", "Poison message", 0.25f, 0.15f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "fails every time"),
            DNode("count", "Delivery count", 0.75f, 0.15f, w = 0.44f, h = 0.2f, sub = "one more each try"),
            DNode("shelf", "Dead-letter queue", 0.25f, 0.55f, w = 0.44f, h = 0.28f, kind = NodeKind.CYLINDER, sub = "returned-mail shelf"),
            DNode("max", "Max delivery count", 0.75f, 0.5f, w = 0.44f, h = 0.2f, kind = NodeKind.PILL, sub = "10 by default"),
            DNode("repair", "Repair script", 0.5f, 0.87f, w = 0.5f, h = 0.2f, tone = 1, sub = "read, fix, resend"),
        ),
        edges = listOf(
            DEdge("poison", "count", "each try"),
            DEdge("count", "max"),
            DEdge("max", "shelf", "moved"),
            DEdge("shelf", "repair", "every morning"),
        ),
        aspect = 0.8f,
    )

    private fun topic() = DiagramSpec(
        "A topic gives every subscription its own copy; a subscription can filter on the message's labels.",
        nodes = listOf(
            DNode("pub", "Coach API", 0.5f, 0.12f, w = 0.44f, h = 0.18f, tone = 1, sub = "publishes once"),
            DNode("topic", "note-events", 0.5f, 0.42f, w = 0.44f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "topic"),
            DNode("s1", "search-index", 0.17f, 0.82f, w = 0.3f, h = 0.22f, sub = "own copy"),
            DNode("s2", "progress", 0.5f, 0.82f, w = 0.3f, h = 0.22f, sub = "own copy"),
            DNode("s3", "german-only", 0.83f, 0.82f, w = 0.3f, h = 0.22f, kind = NodeKind.NOTE, sub = "track = german"),
        ),
        edges = listOf(
            DEdge("pub", "topic"),
            DEdge("topic", "s1"),
            DEdge("topic", "s2"),
            DEdge("topic", "s3"),
        ),
        aspect = 0.8f,
    )

    private fun rules() = DiagramSpec(
        "A new subscription's default rule accepts everything: add the SQL filter and remove the default.",
        nodes = listOf(
            DNode("sub", "german-only", 0.5f, 0.15f, w = 0.5f, h = 0.2f, tone = 1, sub = "a subscription"),
            DNode("def", "Default rule", 0.25f, 0.55f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, tone = 2, sub = "accepts everything"),
            DNode("sql", "SQL filter", 0.75f, 0.55f, w = 0.44f, h = 0.22f, kind = NodeKind.NOTE, sub = "track = german"),
            DNode("only", "Only German notes", 0.5f, 0.87f, w = 0.6f, h = 0.2f, kind = NodeKind.PILL, sub = "remove the default"),
        ),
        edges = listOf(
            DEdge("sub", "def"),
            DEdge("sub", "sql"),
            DEdge("sql", "only"),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where Service Bus stops: delivery is at least once, order needs sessions, Basic has no topics, events go to Event Grid.",
        nodes = listOf(
            DNode("once", "At least once", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "safe to repeat"),
            DNode("sess", "Session", 0.76f, 0.2f, w = 0.42f, h = 0.22f, sub = "in order per learner"),
            DNode("basic", "Basic tier", 0.26f, 0.76f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "no topics"),
            DNode("link", "Event Grid episode", 0.75f, 0.76f, w = 0.46f, h = 0.2f, tone = 1, sub = "announcements"),
        ),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Service Bus questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("every", "Every consumer", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a topic"),
            DNode("crash", "Worker crashes", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "PeekLock"),
            DNode("order", "In order per user", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "sessions"),
            DNode("fails", "Keeps failing", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "dead-letter queue"),
            DNode("twice", "Done twice", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "renew or raise lock"),
            DNode("temp", "Temporary failure", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "abandon"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("every", "crash", "order", "fails", "twice", "temp")
    private val settleCode = BoardCard("Python", "receiver.complete_message(msg)  # or abandon_message, dead_letter_message", code = true)
    private val dlqCode = BoardCard("Python", "client.get_queue_receiver(\"embed-jobs\", sub_queue=ServiceBusSubQueue.DEAD_LETTER)", code = true)
    private val today = BoardCard("Today", "A namespace, embed-jobs, three messages, PeekLock, then read the dead letters.")

    private fun classicBoard() = PodcastBoard(
        "ai-servicebus",
        listOf(
            // 0: "Hi, and welcome back. Today is about letting slow work wait its turn..."
            Scene(
                0, "Slow work waits its turn", post(),
                beats = listOf(
                    Beat(0, show = setOf("api"), light = setOf("api"), card = BoardCard("Slow AI work", "Turning a note into numbers for search takes seconds.")),
                    Beat(2, show = setOf("msg"), light = setOf("msg"), pulse = "api" to "msg", card = BoardCard("Message", "A job ticket: embed note 42. Embedding: numbers like a spot on a map for meaning.")),
                    Beat(4, show = setOf("worker"), light = setOf("worker"), card = BoardCard("Worker", "A separate program that does the slow part later.")),
                    Beat(6, show = setOf("bus"), light = setOf("bus"), pulse = "msg" to "bus", card = BoardCard("Azure Service Bus", "A post office between the parts of your app. Nobody waits.")),
                ),
            ),
            // 7: "And the post office has, what, pigeonholes?"
            Scene(
                7, "Queues and namespaces", ns(),
                beats = listOf(
                    Beat(8, show = setOf("ns", "q"), light = setOf("q"), pulse = "ns" to "q", card = BoardCard("Queue", "One line of messages; each handled by exactly one receiver.")),
                    Beat(10, show = setOf("t", "basic"), light = setOf("ns", "basic"), card = BoardCard("Namespace", "Your own post office, in tiers. Basic has queues only.")),
                    Beat(12, light = setOf("ns"), card = BoardCard("DefaultAzureCredential", "Finds a sign-in by itself: your login, or the app's identity.")),
                    Beat(14, card = BoardCard("Roles, not secrets", "Data Sender for the server, Data Receiver for the worker.")),
                ),
            ),
            // 15: "Okay. The worker takes the message. Is it gone from the queue?"
            Scene(
                15, "The library hold", settle(),
                beats = listOf(
                    Beat(16, show = setOf("msg"), light = setOf("msg"), card = BoardCard("PeekLock", "Locked and hidden, not removed. A crash means another try.")),
                    Beat(18, card = BoardCard("ReceiveAndDelete", "Removed the moment it's handed over. A crash loses it.")),
                    Beat(20, show = setOf("complete", "abandon"), light = setOf("complete", "abandon"), pulse = "msg" to "complete", card = settleCode),
                    Beat(22, show = setOf("defer"), light = setOf("defer"), pulse = "msg" to "defer", card = BoardCard("Defer", "Set aside, fetched later by its number.")),
                    Beat(24, show = setOf("dl"), light = setOf("dl"), pulse = "msg" to "dl", card = BoardCard("Dead-letter", "Moved to a side queue, with a reason attached.")),
                    Beat(26, light = setOf("abandon", "dl"), card = BoardCard("429 or empty?", "429, slow down: abandon. An empty note: dead-letter it.")),
                ),
            ),
            // 27: "And the lock. How long does it last?"
            Scene(
                27, "The lock duration", lock(),
                beats = listOf(
                    Beat(28, show = setOf("w1", "lockN", "again", "w2"), light = setOf("again", "w2"), pulse = "lockN" to "again", card = BoardCard("One minute by default", "A three-minute job: the lock expires, a second worker starts.")),
                    Beat(30, show = setOf("fix"), light = setOf("fix"), pulse = "lockN" to "fix", card = BoardCard("Renew or raise", "AutoLockRenewer, or up to five minutes. The Coach uses two.")),
                ),
            ),
            // 31: "What about a message that fails every single time?"
            Scene(
                31, "Poison messages", dlq(),
                beats = listOf(
                    Beat(32, show = setOf("poison", "count"), light = setOf("count"), pulse = "poison" to "count", card = BoardCard("Delivery count", "The number of tries so far: one more each time it comes back.")),
                    Beat(34, show = setOf("max", "shelf"), light = setOf("shelf"), pulse = "max" to "shelf", card = BoardCard("Max delivery count", "Ten by default; then it moves to the dead-letter queue.")),
                    Beat(36, show = setOf("repair"), light = setOf("repair"), pulse = "shelf" to "repair", card = dlqCode),
                ),
            ),
            // 37: "Okay. Now, what if two services both need every saved note?"
            Scene(
                37, "Every service, every message", topic(),
                beats = listOf(
                    Beat(38, show = setOf("pub"), light = setOf("pub"), card = BoardCard("Two services, one queue?", "They'd compete: each message goes to one of them, never both.")),
                    Beat(40, show = setOf("topic", "s1", "s2"), light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Topic", "A newsletter: each subscription gets its own copy.")),
                    Beat(42, show = setOf("s3"), light = setOf("s3"), pulse = "topic" to "s3", card = BoardCard("Filter rules", "A SQL filter on labels. Remove the default rule, which takes all.")),
                    Beat(44, light = setOf("s1"), card = BoardCard("Session", "One session id per learner: in order, one receiver at a time.")),
                ),
            ),
            // 45: "So what do I remember for the exam?"
            Scene(
                45, "For the exam", exam(),
                beats = listOf(
                    Beat(46, show = all6, light = setOf("every", "crash", "order", "fails"), card = BoardCard("Listen for the phrase", "Every consumer, a crash, in order, keeps failing: one answer each.")),
                    Beat(48, light = setOf("twice", "temp"), card = BoardCard("Done twice, or temporary", "Renew or raise the lock. A temporary failure: abandon.")),
                    Beat(50, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-servicebus--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Notes Done Twice..."
            Scene(
                0, "The Notes Done Twice",
                DiagramSpec(
                    "Lingo Lark's AI bill doubled while some notes were never processed at all.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.22f, 0.18f, w = 0.38f, h = 0.2f, sub = "Lingo Lark"),
                        DNode("api", "Python server", 0.22f, 0.72f, w = 0.38f, h = 0.2f, sub = "on Azure"),
                        DNode("bill", "AI bill doubled", 0.76f, 0.18f, w = 0.4f, h = 0.2f, kind = NodeKind.NOTE, tone = 2, sub = "notes missing"),
                        DNode("ai", "AI service", 0.76f, 0.72f, w = 0.4f, h = 0.2f, tone = 2, sub = "numbers for search"),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "save note"),
                        DEdge("api", "ai", "embed"),
                        DEdge("ai", "bill", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "bill"), light = setOf("bill"), pulse = "phone" to "api", card = BoardCard("The case", "The AI bill doubled, and some notes were never processed.")),
                    Beat(1, show = setOf("ai"), light = setOf("ai"), pulse = "api" to "ai"),
                ),
            ),
            // 2: "When a note is saved, the server writes a job ticket..."
            Scene(
                2, "How it should flow", post(),
                beats = listOf(
                    Beat(2, show = setOf("api", "msg"), light = setOf("msg"), pulse = "api" to "msg", card = BoardCard("Message", "A job ticket: embed note 42. Embedding: numbers like a spot on a map for meaning.")),
                    Beat(4, show = setOf("bus", "worker"), light = setOf("bus"), pulse = "msg" to "bus", card = BoardCard("Service Bus", "A post office; a worker collects the ticket later.")),
                ),
            ),
            // 5: "And the ticket waits where?"
            Scene(
                5, "Under the picture", ns(),
                beats = listOf(
                    Beat(6, show = setOf("ns", "q"), light = setOf("q"), pulse = "ns" to "q", card = BoardCard("Queue", "One line of messages; each handled by exactly one receiver.")),
                    Beat(8, show = setOf("t", "basic"), light = setOf("ns"), card = BoardCard("Namespace", "Lingo Lark's own post office, on the Standard tier.")),
                    Beat(10, light = setOf("q"), card = BoardCard("Under the picture", "A store on Microsoft's servers: messages on disk until settled.")),
                ),
            ),
            // 11: "Then my first suspect for the double bill..."
            Scene(
                11, "The double bill", lock(),
                beats = listOf(
                    Beat(12, show = setOf("w1"), light = setOf("w1"), card = BoardCard("Suspect: sent twice", "Duplicate detection drops a resent id. But one send per note.")),
                    Beat(14, show = setOf("lockN"), light = setOf("lockN"), pulse = "w1" to "lockN", card = BoardCard("PeekLock", "A library hold: locked and hidden, not removed.")),
                    Beat(16, show = setOf("again"), light = setOf("again"), pulse = "lockN" to "again", card = BoardCard("Lock duration", "One minute by default; then visible again, delivery count + 1.")),
                    Beat(17, show = setOf("w2"), light = setOf("w2"), pulse = "again" to "w2", card = BoardCard("Two workers", "A three-minute job, a second worker after one minute.")),
                    Beat(18, show = setOf("fix"), light = setOf("fix"), pulse = "lockN" to "fix", card = BoardCard("Renew or raise", "AutoLockRenewer, or a lock duration of up to five minutes.")),
                ),
            ),
            // 19: "And the notes that were never processed? Same thing?"
            Scene(
                19, "The missing notes", settle(),
                beats = listOf(
                    Beat(20, show = setOf("msg"), light = setOf("msg"), card = BoardCard("ReceiveAndDelete", "Removed on hand-over. A worker crashed; the notes were gone.")),
                    Beat(22, show = setOf("complete", "abandon", "defer", "dl"), light = setOf("complete", "abandon"), pulse = "msg" to "abandon", card = settleCode),
                ),
            ),
            // 23: "And when it fails for good?"
            Scene(
                23, "Three thousand letters", dlq(),
                beats = listOf(
                    Beat(24, show = setOf("shelf"), light = setOf("shelf"), card = BoardCard("Dead-letter queue", "Built into every queue: the returned-mail shelf.")),
                    Beat(26, show = setOf("poison", "count", "max"), light = setOf("max"), pulse = "max" to "shelf", card = BoardCard("Max delivery count", "Ten by default. Three thousand messages on the shelf.")),
                    Beat(28, light = setOf("poison"), pulse = "poison" to "count", card = BoardCard("Poison messages", "Empty notes, tried ten times. Dead-letter them at once.")),
                    Beat(30, show = setOf("repair"), light = setOf("repair"), pulse = "shelf" to "repair", card = dlqCode),
                ),
            ),
            // 31: "Any loose ends in the file?"
            Scene(
                31, "Loose ends", topic(),
                beats = listOf(
                    Beat(32, show = setOf("pub"), light = setOf("pub"), card = BoardCard("Competing on one queue", "The tracker and the worker each got only some notes.")),
                    Beat(34, show = setOf("topic", "s1", "s2"), light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Topic", "A newsletter: each subscription gets its own copy.")),
                    Beat(36, show = setOf("s3"), light = setOf("s3"), pulse = "topic" to "s3", card = BoardCard("The default rule", "It accepts everything. Remove it, and only German notes arrive.")),
                ),
            ),
            // 37: "So where does Service Bus stop helping?"
            Scene(
                37, "Where it stops", limits(),
                beats = listOf(
                    Beat(38, show = setOf("once", "basic"), light = setOf("once"), card = BoardCard("At least once", "A message can arrive twice. Make the work safe to repeat.")),
                    Beat(40, show = setOf("sess", "link"), light = setOf("sess", "link"), card = BoardCard("Sessions, and Event Grid", "In order per learner; announcements go to Event Grid.")),
                ),
            ),
            // 41: "So, the case summary for the exam?"
            Scene(
                41, "Case closed", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("twice", "crash", "fails", "every", "order"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: lock, PeekLock, shelf, topic.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-servicebus--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "A week at the post office", post(),
                beats = listOf(
                    Beat(0, show = setOf("api"), card = BoardCard("A week at the post office", "The Coach's slow AI work moved onto Azure Service Bus.")),
                    Beat(2, show = setOf("msg", "bus"), light = setOf("msg"), pulse = "api" to "msg", card = BoardCard("Message", "A job ticket: embed note 42, dropped off at the post office.")),
                    Beat(4, show = setOf("worker"), light = setOf("worker"), pulse = "bus" to "worker", card = BoardCard("Worker", "A separate program collects it later and calls the AI.")),
                ),
            ),
            // 5: "And the message waits in what?"
            Scene(
                5, "Queues", ns(),
                beats = listOf(
                    Beat(6, show = setOf("ns", "q"), light = setOf("q"), pulse = "ns" to "q", card = BoardCard("Queue", "One line of messages; each handled by exactly one receiver.")),
                    Beat(8, light = setOf("ns"), card = BoardCard("Namespace", "The team's own post office building, with its own address.")),
                    Beat(10, light = setOf("q"), card = BoardCard("Under the picture", "Messages on disk until settled. A worker can be down an hour.")),
                ),
            ),
            // 11: "What did Omar do?"
            Scene(
                11, "Monday: Omar", settle(),
                beats = listOf(
                    Beat(12, show = setOf("msg"), light = setOf("msg"), card = BoardCard("ReceiveAndDelete", "Removed on hand-over. The worker crashed; the notes were gone.")),
                    Beat(14, card = BoardCard("PeekLock", "A library hold: locked and hidden, not removed.")),
                    Beat(16, show = setOf("complete", "abandon"), light = setOf("complete", "abandon"), pulse = "msg" to "complete", card = settleCode),
                ),
            ),
            // 17: "Go."
            Scene(
                17, "Tuesday: Dana", lock(),
                beats = listOf(
                    Beat(18, show = setOf("w1", "lockN"), light = setOf("lockN"), pulse = "w1" to "lockN", card = BoardCard("Lock duration", "How long the hold lasts: one minute by default.")),
                    Beat(20, show = setOf("again", "w2"), light = setOf("again", "w2"), pulse = "lockN" to "again", card = BoardCard("Under the hood", "A lock with a timer. It runs out; the message is visible again.")),
                    Beat(22, show = setOf("fix"), light = setOf("fix"), pulse = "lockN" to "fix", card = BoardCard("Renew or raise", "Two minutes, five at most, and AutoLockRenewer for long calls.")),
                ),
            ),
            // 23: "Wednesday. Let me guess, someone else's turn."
            Scene(
                23, "Wednesday: Marcus", topic(),
                beats = listOf(
                    Beat(24, show = setOf("pub"), light = setOf("pub"), card = BoardCard("One queue, two services", "The progress tracker read the same queue as the worker.")),
                    Beat(26, card = BoardCard("They compete", "Each message went to one of them, never both.")),
                    Beat(28, show = setOf("topic", "s1", "s2", "s3"), light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Topic", "A newsletter: each subscription gets its own copy.")),
                ),
            ),
            // 29: "Thursday. The intern?"
            Scene(
                29, "Thursday: the intern", rules(),
                beats = listOf(
                    Beat(30, show = setOf("sub", "sql"), light = setOf("sql"), pulse = "sub" to "sql", card = BoardCard("SQL filter", "A short test on the message's labels: track equals german.")),
                    Beat(32, show = setOf("def", "only"), light = setOf("def"), pulse = "sub" to "def", card = BoardCard("The default rule", "Accepts everything. Remove it, and only German notes arrive.")),
                ),
            ),
            // 33: "And Friday? There's always a Friday."
            Scene(
                33, "Friday: Priya", dlq(),
                beats = listOf(
                    Beat(34, show = setOf("shelf"), light = setOf("shelf"), card = BoardCard("Dead-letter queue", "Built into every queue: the returned-mail shelf.")),
                    Beat(36, show = setOf("poison", "count", "max"), light = setOf("max"), pulse = "max" to "shelf", card = BoardCard("Max delivery count", "Ten by default. Three thousand messages on the shelf.")),
                    Beat(38, show = setOf("repair"), light = setOf("repair"), pulse = "shelf" to "repair", card = BoardCard("Poison messages", "Dead-letter empty notes at once; a script reads the shelf daily.")),
                ),
            ),
            // 39: "So nothing reads the dead-letter queue by itself?"
            Scene(
                39, "Where it stops", limits(),
                beats = listOf(
                    Beat(40, show = setOf("once", "basic"), light = setOf("once"), card = BoardCard("At least once", "A message can arrive twice. Make the work safe to repeat.")),
                    Beat(42, show = setOf("sess", "link"), light = setOf("sess", "link"), card = BoardCard("Sessions, and Event Grid", "In order per learner; announcements go to Event Grid.")),
                ),
            ),
            // 43: "So, the gossip I should remember for the exam?"
            Scene(
                43, "For the exam", exam(),
                beats = listOf(
                    Beat(44, show = all6, light = setOf("crash", "twice", "every", "fails"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Nobody wants to be next week's story.")),
                    Beat(46, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-servicebus--late-night",
        listOf(
            // 0: "Hey, you. It's late, the streets are quiet..."
            Scene(
                0, "Letters that wait", post(),
                beats = listOf(
                    Beat(0, show = setOf("api"), card = BoardCard("Tonight", "Letters: the kind that wait patiently for you.")),
                    Beat(4, show = setOf("msg"), light = setOf("msg"), pulse = "api" to "msg", card = BoardCard("Message", "A job ticket: embed note 42. Embedding: numbers like a spot on a map for meaning.")),
                    Beat(6, show = setOf("worker"), light = setOf("worker"), card = BoardCard("Worker", "A separate program that picks it up later, when it's ready.")),
                    Beat(8, show = setOf("bus"), light = setOf("bus"), pulse = "msg" to "bus", card = BoardCard("Azure Service Bus", "A post office between the parts of your app.")),
                ),
            ),
            // 9: "Waiting for someone who isn't awake yet..."
            Scene(
                9, "A queue", ns(),
                beats = listOf(
                    Beat(10, show = setOf("ns", "q"), light = setOf("q"), pulse = "ns" to "q", card = BoardCard("Queue", "One line of messages; each handled by exactly one receiver.")),
                    Beat(12, light = setOf("ns"), card = BoardCard("Namespace", "Your own post office building, with its own address.")),
                    Beat(14, light = setOf("q"), card = BoardCard("Under the picture", "Messages on disk until a receiver is done with them.")),
                ),
            ),
            // 15: "And when the worker finally comes for it?"
            Scene(
                15, "The library hold", settle(),
                beats = listOf(
                    Beat(16, show = setOf("msg"), light = setOf("msg"), card = BoardCard("PeekLock", "A library hold: locked and hidden, not yet yours.")),
                    Beat(18, card = BoardCard("ReceiveAndDelete", "Handed over and forgotten. A crash loses it forever.")),
                    Beat(20, show = setOf("complete", "abandon", "dl"), light = setOf("complete", "abandon", "dl"), pulse = "msg" to "abandon", card = settleCode),
                ),
            ),
            // 21: "And how long does the hold last?"
            Scene(
                21, "How long the hold lasts", lock(),
                beats = listOf(
                    Beat(22, show = setOf("w1", "lockN", "again"), light = setOf("again"), pulse = "lockN" to "again", card = BoardCard("Lock duration", "One minute by default; then visible again, delivery count + 1.")),
                    Beat(24, show = setOf("w2", "fix"), light = setOf("w2", "fix"), pulse = "lockN" to "fix", card = BoardCard("Done twice, paid twice", "Raise the lock duration, or renew it with AutoLockRenewer.")),
                ),
            ),
            // 25: "And a message that fails, again and again, all night?"
            Scene(
                25, "All night", dlq(),
                beats = listOf(
                    Beat(26, show = setOf("poison", "count"), light = setOf("count"), pulse = "poison" to "count", card = BoardCard("Poison message", "Each failure raises its delivery count, the number of tries.")),
                    Beat(28, show = setOf("max", "shelf"), light = setOf("shelf"), pulse = "max" to "shelf", card = BoardCard("Max delivery count", "Ten by default; then the dead-letter queue, the returned mail.")),
                    Beat(30, show = setOf("repair"), light = setOf("repair"), pulse = "shelf" to "repair", card = BoardCard("Where it stops", "Nobody reads the shelf for you. At least once: safe to repeat.")),
                ),
            ),
            // 31: "Now, what if two services both want every note?"
            Scene(
                31, "Every note, for both", topic(),
                beats = listOf(
                    Beat(32, show = setOf("pub", "topic", "s1", "s2"), light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Topic", "A newsletter: each subscription gets its own copy.")),
                    Beat(34, card = BoardCard("Standard or Premium", "Topics need the bigger tiers. Basic has queues only.")),
                    Beat(36, show = setOf("s3"), light = setOf("s3"), pulse = "topic" to "s3", card = BoardCard("Filter rule", "Remove the default rule, or you still get it all.")),
                ),
            ),
            // 37: "And if my messages must arrive in order?..."
            Scene(
                37, "In order", limits(),
                beats = listOf(
                    Beat(38, show = setOf("sess"), light = setOf("sess"), card = BoardCard("Session", "One session id per learner: in order, one receiver at a time.")),
                    Beat(40, show = setOf("link"), light = setOf("link"), card = BoardCard("Your next late night", "The Event Grid episode: announcements, not job tickets.")),
                ),
            ),
            // 41: "Noted. Before I sleep. What do I remember for the exam?"
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("crash", "every", "order", "fails", "twice"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: PeekLock, topic, sessions.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-servicebus--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", post(),
                beats = listOf(
                    Beat(0, show = setOf("api"), card = BoardCard("Date night", "A topic about patience: Azure Service Bus.")),
                    Beat(4, show = setOf("msg"), light = setOf("msg"), pulse = "api" to "msg", card = BoardCard("Message", "A job ticket: embed note 42. The server answers at once.")),
                    Beat(6, show = setOf("bus", "worker"), light = setOf("bus"), pulse = "msg" to "bus", card = BoardCard("Service Bus", "A post office; a worker picks the message up later.")),
                ),
            ),
            // 8: "Exactly. It waits in a queue..."
            Scene(
                8, "A queue", ns(),
                beats = listOf(
                    Beat(8, show = setOf("ns", "q"), light = setOf("q"), pulse = "ns" to "q", card = BoardCard("Queue", "One line, one receiver each; kept on disk until settled.")),
                    Beat(9, light = setOf("ns"), card = BoardCard("Quiz: never lose a job", "The worker may crash halfway. Which receive mode?")),
                ),
            ),
            // 10: "PeekLock, the default..."
            Scene(
                10, "Hold on", settle(),
                beats = listOf(
                    Beat(10, show = setOf("msg"), light = setOf("msg"), card = BoardCard("PeekLock", "A library hold: locked and hidden. ReceiveAndDelete forgets it.")),
                    Beat(11, card = BoardCard("Right answer", "PeekLock: a crash loses nothing.")),
                    Beat(12, show = setOf("complete", "abandon"), light = setOf("complete", "abandon"), pulse = "msg" to "complete", card = settleCode),
                ),
            ),
            // 13: "And the hold lasts how long?"
            Scene(
                13, "Don't rush", lock(),
                beats = listOf(
                    Beat(14, show = setOf("w1", "lockN"), light = setOf("lockN"), pulse = "w1" to "lockN", card = BoardCard("Lock duration", "One minute by default: a lock with a timer.")),
                    Beat(16, show = setOf("again", "w2"), light = setOf("again", "w2"), pulse = "lockN" to "again", card = BoardCard("Quiz: done twice", "A three-minute job, the default lock. Why twice?")),
                    Beat(17, show = setOf("fix"), light = setOf("fix"), pulse = "lockN" to "fix", card = BoardCard("Right answer", "The lock expired. Renew it, or raise it to five minutes at most.")),
                ),
            ),
            // 19: "My question, then. A malformed job fails every single time..."
            Scene(
                19, "Somewhere quiet", dlq(),
                beats = listOf(
                    Beat(19, show = setOf("poison"), light = setOf("poison"), card = BoardCard("Quiz: fails every time", "With default settings, where does it end up?")),
                    Beat(21, show = setOf("count", "max", "shelf"), light = setOf("shelf"), pulse = "max" to "shelf", card = BoardCard("No kiss: the shelf", "Past the max delivery count, ten: the dead-letter queue.")),
                    Beat(22, show = setOf("repair"), light = setOf("repair"), pulse = "shelf" to "repair", card = dlqCode),
                    Beat(24, light = setOf("poison"), card = BoardCard("Poison message", "An empty note: dead-letter it at once, with a reason.")),
                ),
            ),
            // 25: "A topic with two subscriptions..."
            Scene(
                25, "Never shared", topic(),
                beats = listOf(
                    Beat(25, show = setOf("pub", "topic", "s1", "s2"), light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Right answer: a topic", "A newsletter: every subscription gets its own copy.")),
                    Beat(27, show = setOf("s3"), light = setOf("s3"), pulse = "topic" to "s3", card = BoardCard("Standard or Premium", "Basic has queues only. A subscription can filter.")),
                    Beat(28, card = BoardCard("Quiz: the filter ignored", "The filter is added, yet every message arrives. Why?")),
                    Beat(29, card = BoardCard("Right answer", "A default rule accepts everything. Remove it.")),
                ),
            ),
            // 31: "Last quiz. One learner's messages must arrive in order..."
            Scene(
                31, "In order", limits(),
                beats = listOf(
                    Beat(31, show = setOf("sess"), light = setOf("sess"), card = BoardCard("Quiz: in order", "One learner's messages must arrive in order. What then?")),
                    Beat(32, card = BoardCard("Right answer: a session", "A shared session id: in order, one receiver at a time.")),
                    Beat(35, show = setOf("once", "basic", "link"), light = setOf("once", "link"), card = BoardCard("Where it stops", "At least once: safe to repeat. Event Grid for announcements.")),
                ),
            ),
            // 36: "Then the last question, for the exam. What do we remember?"
            Scene(
                36, "For the exam", exam(),
                beats = listOf(
                    Beat(37, show = all6, light = setOf("crash", "every", "order", "fails", "twice"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
