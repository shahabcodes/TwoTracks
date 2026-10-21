package com.shahaabapps.core.content

/** Event Grid: custom events, filters and retries: its five AI-200 episodes and their boards (Ai200Podcasts). */
internal object Ai200EpisodesEventGrid {
    private val classic = Podcast(
        "ai-eventgrid",
        listOf(
            maya("Hi, and welcome back. Today is Event Grid, the part of Azure that tells other parts when something has happened. Picture a notice board that doesn't wait for you to walk past. The moment a notice goes up, it runs over and hands a copy to everyone who asked for that kind of news."),
            leo("A pushy notice board. I've worked in offices like that. What counts as a notice?"),
            maya("An event. A small message that says something happened. A note was updated. A file was uploaded. It's news, not a job. It doesn't tell anyone what to do. It just says what happened."),
            leo("News, not a job. Why does that difference matter?"),
            maya("Because Azure has another service for jobs, called Service Bus. Think of its queue as a to-do list, where each job is picked up by one worker and finished. Event Grid is more like a town crier. It calls out the news to everyone who signed up, and moves on."),
            leo("Okay. So who puts the notice up?"),
            maya("The publisher. Say you're building the Two Tracks Coach, a study assistant for an Android app, with a Python backend on Azure. When a learner edits a note, the backend sends out an event called NoteUpdated. That makes the backend the publisher."),
            leo("And where does it send it? Straight onto the board?"),
            maya("Onto a topic. A topic is one notice board, with its own web address. The Coach's board is a custom topic: a board your own code posts to, with news you invent, like NoteUpdated."),
            leo("Is there any other kind?"),
            maya("A system topic. That's a board where an Azure service posts news about itself. For example, a storage account, Azure's place for keeping files. When a PDF is uploaded there, Storage posts a BlobCreated event by itself. A blob is just a file in storage. You don't publish that one. You only sign up to hear it."),
            leo("Sign up how? I can't exactly follow a notice board."),
            maya("[chuckle] You can, with a subscription. A subscription is a standing order: send me these events, at this address. Whatever sits at that address is the handler, the code that receives the event."),
            leo("What kind of code can be a handler?"),
            maya("A small function, which is Azure Functions: a piece of code that runs only when something happens. Or a webhook, which is just a web address that accepts the event. Or a Service Bus queue. For the Coach, a re-embed function listens for NoteUpdated and refreshes the note's search data. One topic can have many subscriptions, and each gets its own copy."),
            leo("What does an event actually look like?"),
            maya("Like a letter in a standard envelope. The layout of the envelope is called the schema. The one to prefer is CloudEvents 1.0, an open standard that many systems understand. You choose it when you create the topic."),
            leo("And what's written on the envelope?"),
            maya("A type, which says what happened, like NoteUpdated. A source, which says who sent it. And a subject, which says what it happened to, written like a file path: slash notes, slash german, slash 42."),
            leo("And the details? The actual news?"),
            maya("That's the data, inside the envelope, such as the note's word count. And write subjects as paths on purpose, because subscribers can filter on them."),
            leo("Filter. So a subscriber doesn't have to take everything?"),
            maya("No. A filter is a rule on the subscription that says which events to let through. There are three kinds. The first is an event type filter. Only NoteUpdated events, say, and nothing else."),
            leo("And the second?"),
            maya("A subject filter. The subject begins with, or ends with, some text. Picture a pronunciation service that only cares about German notes. Subject begins with slash notes, slash german. Or a service that only wants PDFs: subject ends with dot pdf."),
            leo("And a third? There's always a third."),
            maya("An advanced filter. It looks inside the data and compares a field using an operator, which is a test like greater than, or is one of. For example, only re-embed notes longer than 20 words: data dot wordCount, NumberGreaterThan, 20. A subject filter can't do that. It only reads the start and end of the subject."),
            leo("Got it. Now the scary part. What if my handler is down?"),
            maya("Then Event Grid is patient. Delivery is at least once. The handler has to answer with a success code, which means OK, within 30 seconds. Anything else counts as a failure, and Event Grid tries again later, waiting a little longer each time."),
            leo("How long does it keep trying?"),
            maya("By default, 30 attempts or 24 hours, whichever comes first. You can lower both on the subscription. Some errors aren't retried at all, like a 400, which means the event itself was bad, so trying again won't help."),
            leo("And after 24 hours, it keeps the event somewhere safe?"),
            maya("[pause] No, and this is the big trap. By default, it drops it. Gone. To keep it, you turn on dead-lettering. You point the subscription at a blob container, which is a folder in a storage account, and undelivered events are written there instead."),
            leo("Dead letters. Like the post office's pile of letters nobody could deliver."),
            maya("Exactly that. Now, back to at least once. It means an event can arrive twice. Say the handler did the work, but its answer got lost on the way back. Event Grid can't tell, so it sends the event again."),
            leo("So my function refreshes the same note twice. Is that bad?"),
            maya("Only if the second time changes something. That's why handlers must be idempotent. It's a long word for safe to run twice: the same event again leaves the same result. Writing the note's search data by its id, replacing what's there, is safe. Adding one to a counter is not, so keep the ids of the events you've done."),
            leo("Anything else that can catch me out?"),
            maya("Two things. First, order. Event Grid doesn't promise events arrive in the order they were sent. Second, a webhook must prove it wants the events before any arrive. That's endpoint validation, a handshake: Event Grid knocks, and the webhook must answer the right way. A Functions trigger does it for you."),
            leo("And when would I not use Event Grid at all?"),
            maya("When there's work that one worker must finish, job by job. Say your app marks essays. Each essay is a job to finish, not news. That's a Service Bus queue. Event Grid tells everyone something happened; a queue makes sure one worker does each job."),
            leo("So what do I remember for the exam?"),
            maya("Five things. If undelivered events must be kept, a dead-letter blob container. If only events for one folder or one track, a subject begins-with filter. If a number inside the data decides, an advanced filter. If the handler sees the same event twice, make it idempotent. And if it's an Azure service's own events, like an upload, a system topic."),
            leo("And anything to try today?"),
            maya("About fifteen minutes. Create a custom topic with the CloudEvents input schema. Subscribe a small function or webhook with an event type filter. Publish two events from Python, one matching and one not. Then add a dead-letter container, and check the subscription's delivery metrics, the counts of what arrived and what failed."),
            leo("News that pushes itself, and never gets lost. See you next time."),
        ),
        series = PodcastSeries.AI200,
    )

    private val caseFiles = Podcast(
        "ai-eventgrid",
        listOf(
            maya("Welcome to Case Files. Today's case: The Edits That Vanished. WordWise is a small Android app for learning vocabulary, with a Python backend on Azure. When a learner edits a word list, the backend announces it, and a search function and a weekly report both react. On Tuesday, learners noticed that Monday's edits never showed up in search."),
            leo("Detective Leo, reporting. Announces it how, in plain words?"),
            maya("Through Event Grid. Picture a notice board that pushes. The backend posts a notice, an event, saying a list was updated. Event Grid then hands a copy to everyone who signed up for that news. An event is news, not a job. It says what happened, not what to do."),
            leo("And what is Event Grid, really, under the notice board?"),
            maya("A service Azure runs for you. The backend sends each event to a topic, which is one board with its own web address, using a secure web call. Event Grid checks every subscription on that topic, a standing order that says send me these, here. Then it makes a web call to each handler, the code that receives it."),
            leo("Then here's my first suspect. The backend never sent Monday's events."),
            maya("[pause] Wrong suspect. The backend's logs, its own diary of what it did, show every event went out, and the topic accepted them. Here's the first clue. On Monday the search function, a small piece of code that runs when something happens, was down while a new version was installed. Thirty-one hours, start to finish."),
            leo("So Event Grid knocked, and nobody was home."),
            maya("Right. The handler must answer with a success code, which means OK, within 30 seconds. Otherwise Event Grid tries again, waiting longer each time: ten seconds, thirty, a minute, and on up to hours. By default it gives up after 30 attempts or 24 hours, whichever comes first."),
            leo("And when it gives up, it keeps the events somewhere, surely."),
            maya("That's the second clue, and the culprit. Nobody had turned on dead-lettering. Without it, when the retries run out, the event is dropped. Gone. Dead-lettering points the subscription at a blob container, a folder in Azure's file storage, and undelivered events are written there instead."),
            leo("So with a dead-letter container, they could have read Monday's events back and replayed them."),
            maya("Exactly. Read each one from the container, and send it to the function again. They could also tune the retry policy on the subscription: fewer attempts, or a shorter time to live, which is how long an event is worth trying. But never past 24 hours, so for a long outage, dead-lettering is the safety net."),
            leo("Noted. Is that the whole case?"),
            maya("One down. But the case file has a second witness. The weekly report shows some learners editing twice as many words as they did."),
            leo("Suspect two: Event Grid has a bug. It sent the same event twice. Open a support case."),
            maya("No bug. Delivery is at least once, by design. If the report's answer got lost on the way back, Event Grid can't tell it was handled, so it sends it again. Duplicates are normal."),
            leo("Then the report is the problem. It added the words every time an event came in."),
            maya("Now you're detecting. The handler must be idempotent, which is a long word for safe to run twice: the same event again leaves the same result. Keep the ids of the events you've already counted, and skip a repeat. The search function was fine, because it replaces a list's search entry by its id."),
            leo("Third witness?"),
            maya("The German team. They built a pronunciation service and subscribed it to the topic. It received every list in every language, and they only wanted German."),
            leo("Suspect: they need their own topic. One board per language."),
            maya("Close, but no. That's what filters are for. A filter is a rule on the subscription. The subject on each event says what it happened to, written like a path: slash lists, slash german, slash 42. A subject filter, begins with slash lists slash german, lets only German lists through."),
            leo("And if they only wanted big lists? Over 20 words?"),
            maya("An advanced filter. It looks inside the event's data and compares a field with a test, like greater than. Data dot wordCount, NumberGreaterThan, 20. And an event type filter picks the kind of news, like ListUpdated only."),
            leo("Fourth witness. I can feel it."),
            maya("[chuckle] The report team moved to a webhook, which is just a web address that accepts events. Nothing ever arrived. Not one event."),
            leo("A filter typo. They filtered out everything."),
            maya("No. The webhook never answered the handshake. Before Event Grid pushes anything to a webhook, the webhook must prove it wants the events. That's endpoint validation: Event Grid knocks first, and the webhook must answer the right way. An Azure Functions trigger does it for you, which is why the search function never had the problem."),
            leo("What format are these events in, anyway?"),
            maya("CloudEvents 1.0. The envelope's layout is called the schema, and CloudEvents is the open standard one to prefer for new work. You pick it when you create the topic. The envelope carries an id, a source saying who sent it, a type, the subject, a time, and the data inside."),
            leo("Last loose end. The report also had lists out of order. Edited on Monday, counted after Tuesday's edit."),
            maya("That's where Event Grid stops working for you. It doesn't promise order. And the report was really a list of jobs, each one to be done once, by one worker, carefully. That's a Service Bus queue, a to-do list for workers. The Service Bus episode covers queues, and the Functions episode covers the handler side."),
            leo("And the upload of a photo? Does WordWise publish that too?"),
            maya("No need. When a file lands in a storage account, Storage posts its own event, BlobCreated, to a system topic. That's a board where an Azure service posts news about itself. Your own news, like ListUpdated, goes to a custom topic."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. Undelivered events must be kept: a dead-letter blob container. Same event twice: make the handler idempotent. Only one language or folder: a subject begins-with filter. A number in the data decides: an advanced filter. A webhook that gets nothing: the validation handshake."),
            leo("Case closed. Anything to try today?"),
            maya("Recreate the case, safely. Create a custom topic with the CloudEvents input schema. Subscribe a small function with an event type filter. Publish two events from Python, one matching and one not. Then add a dead-letter container and check the subscription's delivery metrics, the counts of what arrived and what failed."),
            leo("No more vanishing edits. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.CASE_FILES,
    )

    private val gossip = Podcast(
        "ai-eventgrid",
        listOf(
            maya("Okay, did you hear what happened on the Coach team this week? Sit down. They wired the Two Tracks Coach, the study assistant in the Android app, with its Python backend on Azure, to Event Grid. And it was a week."),
            leo("[laugh] I heard someone called a post office. What's Event Grid, in plain words?"),
            maya("A notice board that pushes. When something happens, like a learner editing a note, the backend posts an event, a small message saying so. Event Grid hands a copy to everyone who signed up for that news. It's news, not a job: it says what happened, not what to do."),
            leo("And under the notice board?"),
            maya("A service Azure runs. The backend sends the event to a topic, one board with its own web address. Each subscription on it is a standing order: send me these events, here. Event Grid checks each one, and makes a web call to its handler, the code that receives the event."),
            leo("Okay. So who went first?"),
            maya("Tom. Monday. Tom set up the custom topic, a board for the Coach's own news, NoteUpdated. And he chose the schema, the layout of each event's envelope. He picked CloudEvents 1.0, the open standard. Good choice, honestly."),
            leo("What's written on that envelope?"),
            maya("A type, which says what happened, like NoteUpdated. A source, which says who sent it. And a subject, which says what it happened to, written like a path: slash notes, slash german, slash 42. Tom wrote lovely tidy paths. Remember that. It matters on Friday."),
            leo("That's not gossip, that's a compliment."),
            maya("Wait. Then he tried to publish BlobCreated himself, every time a PDF was uploaded to storage. Wrote a whole little routine for it."),
            leo("And that's wrong because?"),
            maya("Because a storage account, Azure's place for files, posts that event by itself, to a system topic. That's a board where an Azure service posts news about itself. You don't publish it. You just subscribe. Tom deleted his routine by lunch."),
            leo("[sigh] Poor Tom. Tuesday?"),
            maya("Tuesday was Dana. Dana's re-embed function, a small piece of code that refreshes a note's search data, went down for a long deployment. Thirty hours. When it came back, a whole day of note edits had never arrived."),
            leo("But Event Grid retries. I know that much."),
            maya("It does. The handler must answer with a success code, which means OK, within 30 seconds. If not, Event Grid tries again, waiting longer each time. By default, 30 attempts or 24 hours, whichever comes first. Dana was down for 30 hours."),
            leo("So the events waited somewhere, right?"),
            maya("No. By default they're dropped. Dana hadn't turned on dead-lettering, which writes undelivered events to a blob container, a folder in a storage account. With it, she'd have read them back and replayed them. Without it, she rebuilt the search data by hand."),
            leo("Couldn't she just tell it to keep trying for longer?"),
            maya("Not past a day. Each subscription has a retry policy: fewer attempts, or a shorter time to live, meaning how long an event is worth trying. But 24 hours is the ceiling. So for a long outage, dead-lettering is the safety net. Dana has it switched on everywhere now. Everywhere."),
            leo("Ouch. Wednesday?"),
            maya("Wednesday was Marcus. Marcus built the streak counter. Every NoteUpdated event, plus one to the learner's daily total. And some learners suddenly had double streaks."),
            leo("Event Grid sent things twice?"),
            maya("Sometimes, yes. Delivery is at least once. If a handler's answer gets lost, Event Grid can't tell it worked, so it sends again. Marcus's handler wasn't idempotent, which means safe to run twice: the same event again leaves the same result. He keeps the ids of events he's counted now."),
            leo("And Thursday, I'm guessing, the intern."),
            maya("[chuckle] The intern. Bless them. They wanted the essay marker to get every English essay, one at a time, each marked exactly once, in order. So they used Event Grid."),
            leo("Which is wrong, because it's news, not jobs."),
            maya("You've been listening. Event Grid doesn't promise order, and it tells everyone. Jobs that one worker must finish, one by one, go on a Service Bus queue, a to-do list for workers. The Service Bus episode has the rest. The intern now loves queues."),
            leo("And Friday? There's always a Friday."),
            maya("Priya. Priya never makes mistakes, which is why everyone's still talking. She subscribed the pronunciation service with a webhook, which is just a web address that takes events. And for a whole day, nothing arrived."),
            leo("A wrong filter?"),
            maya("Her filters were perfect, actually. A filter is a rule on the subscription. An event type filter, NoteUpdated only. A subject filter: the subject is written like a path, so begins with slash notes, slash german. And an advanced filter, which tests a field inside the data, wordCount greater than 20."),
            leo("Then what went wrong?"),
            maya("The handshake. Before Event Grid pushes to a webhook, the webhook must prove it wants the events. That's endpoint validation. Event Grid knocks first, and her webhook didn't answer the right way. A Functions trigger does it for you. The Functions episode shows how."),
            leo("How does it know who to tell, under the hood?"),
            maya("Each time an event arrives, Event Grid checks it against every subscription's filters on that topic, and sends a copy to each one that matches, as a web call. Every copy has its own retries. So Dana's failed copies never stopped Marcus's counter getting its own."),
            leo("So where does Event Grid stop being the answer, really?"),
            maya("Where you need order, or one worker per job. That's a queue. And where you need to keep failures, you must ask: dead-lettering is off by default. Everything else, it's great at. News to many listeners, filtered, retried for a day."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Five things. Undelivered events must be kept: a dead-letter blob container. Only German notes: a subject begins-with filter. A number in the data: an advanced filter. Same event twice: an idempotent handler. And Azure's own events, like an upload: a system topic, nothing to publish."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a custom topic with the CloudEvents input schema. Subscribe a small function or webhook with an event type filter. Publish two events from Python, one matching and one not. Then add a dead-letter container and check the subscription's delivery metrics, the counts of what arrived and what failed."),
            leo("Dead letters on, double streaks off. Got it, Marcus. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.GOSSIP,
    )

    private val lateNight = Podcast(
        "ai-eventgrid",
        listOf(
            nova("Hey, you. It's late, the house is quiet, and it's just the three of us. Me, Ivy, and you, still up, still learning. [pause] Tonight, let's talk about how one part of an app hears the news from another."),
            ivy("Mm. Gossip between machines. You know me so well, Nova."),
            nova("[chuckle] Something like that. Picture a notice board that doesn't wait for anyone to walk past. The moment a notice goes up, it carries a copy to everyone who asked. That's Event Grid."),
            ivy("And the notices are?"),
            nova("Events. Small messages that say something happened. A note was edited. A file was uploaded. [pause] It's news, not a job. An event never tells anyone what to do. Jobs live somewhere else in Azure, and we'll get to that."),
            ivy("Tell me what it really is, under the notice board."),
            nova("A service Azure runs for you. Your code sends each event to a topic, one board with its own web address. Event Grid looks at every subscription on that topic, a standing order that says, send me these, here. Then it makes a web call to each handler, the code that receives it."),
            ivy("Who's posting, in our story?"),
            nova("Take the Two Tracks Coach, the study assistant in the Android app, with a Python backend on Azure. When a learner edits a note, the backend posts NoteUpdated to a custom topic. A custom topic is a board your own code posts to, with news you invent."),
            ivy("And a board you didn't make yourself?"),
            nova("A system topic. That's where an Azure service posts news about itself. When a PDF lands in a storage account, Azure's place for files, Storage posts a BlobCreated event there on its own. You don't publish it. You just listen."),
            ivy("And who's listening, on the Coach?"),
            nova("A small function. That's Azure Functions, a bit of code that wakes only when something happens. This one is the re-embed function, and it refreshes a note's search data whenever the note changes. One topic can have many subscriptions, and each gets its own copy."),
            ivy("What does a single event look like? Up close."),
            nova("Like a letter in a standard envelope. The envelope's layout is the schema, and the one to prefer is CloudEvents 1.0, an open standard. On it, a type says what happened, a source says who sent it, and a subject says what it happened to, written like a path. Slash notes, slash german, slash 42."),
            ivy("Paths. So you can choose which letters you open."),
            nova("Exactly. With a filter, a rule on the subscription. An event type filter takes only certain kinds, like NoteUpdated. A subject filter checks how the subject begins or ends, so a German service takes only slash notes slash german."),
            ivy("And the actual news? The details?"),
            nova("Inside the envelope, in the data. For the Coach, the note's id and its word count. Hold on to that word count, [pause] because a fussy subscriber might care about it."),
            ivy("And if I'm fussier than that?"),
            nova("[pause] Then an advanced filter. It looks inside the data and tests a field. Only notes with a word count greater than 20, say. That's the one for a number in the data."),
            ivy("Now, the part that keeps me up. What if the handler's asleep?"),
            nova("Then Event Grid is patient with it. It wants an answer that means OK, a success code, within 30 seconds. Otherwise it tries again, and waits a little longer each time. Ten seconds, thirty, a minute, then on up to hours. By default, 30 attempts or 24 hours, whichever comes first."),
            ivy("Can I make it give up sooner?"),
            nova("You can. Each subscription has a retry policy: fewer attempts, or a shorter time to live, meaning how long an event is worth trying. And some errors aren't retried at all. A 400 means the event itself was bad, [pause] so knocking again won't help."),
            ivy("And when it finally gives up? It tucks the event away somewhere safe?"),
            nova("[long pause] Not unless you ask. By default, it's dropped. To keep it, you turn on dead-lettering. You give the subscription a blob container, a folder in storage, and the undelivered events wait there, for you to read and replay."),
            ivy("Left at the door until morning. I like that better."),
            nova("Me too. And here's a quiet truth. Delivery is at least once. If the handler's answer gets lost, Event Grid can't tell it worked, so it knocks again. The same event can arrive twice."),
            ivy("Twice. So what does a careful handler do?"),
            nova("It's idempotent. Safe to run twice: the same event again leaves the same result. The Coach's re-embed function replaces a note's search data by its id, so a repeat changes nothing. A counter that adds one would count double, so it keeps the ids it's seen."),
            ivy("How does Event Grid decide who gets what, so quickly?"),
            nova("Each time an event arrives, it checks the event against every subscription's filters, and sends a copy to each one that matches, as a web call. Every copy has its own retries. So one sleepy handler never holds up the others."),
            ivy("Where does all of this stop working, Nova?"),
            nova("Here. [pause] Event Grid doesn't promise order. And it tells everyone. Work that one worker must finish, job by job, in order, belongs on a Service Bus queue, a to-do list for workers. The Service Bus episode is your next late night for that."),
            ivy("And a webhook? Can anyone just receive events?"),
            nova("A webhook is just a web address that accepts events. But first it must prove it wants them. That's endpoint validation, a little handshake. Event Grid knocks, and the webhook must answer the right way. A Functions trigger does it for you; the Functions episode shows the handler side."),
            ivy("[sigh] Mm. Before you send me off to bed. What do I remember for the exam?"),
            nova("Five little things. If undelivered events must be kept, a dead-letter blob container. If only one branch, like German notes, a subject begins-with filter. If a number in the data decides, an advanced filter. If the same event arrives twice, an idempotent handler. And Azure's own events, a system topic."),
            ivy("And something to try today, before I sleep?"),
            nova("Create a custom topic with the CloudEvents input schema. Subscribe a small function or webhook with an event type filter. Publish two events from Python, one matching and one not. [pause] Then add a dead-letter container and check the subscription's delivery metrics, the counts of what arrived and what failed."),
            ivy("News that waits at the door for me. Goodnight, you. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.LATE_NIGHT,
    )

    private val couples = Podcast(
        "ai-eventgrid",
        listOf(
            nova("Tonight is date night, darling. Candles, a little wine, and you and me, Rafe, talking about news that just can't wait."),
            rafe("Let me guess. You've heard something, and you have to tell everyone."),
            nova("[laugh] Event Grid. A notice board that pushes. The moment a notice goes up, it carries a copy to everyone who asked for that kind of news."),
            rafe("Like you, after our first date. So what's a notice, here?"),
            nova("An event. A small message that says something happened. A note was edited, a file was uploaded. It's news, not a job. It never tells anyone what to do."),
            rafe("Nobody tells you what to do. And under the notice board, what is it really?"),
            nova("A service Azure runs for you. Your code sends an event to a topic, one board with its own web address. Each subscription on it is a standing order: send me these, here. Event Grid checks each one and makes a web call to its handler, the code that receives it."),
            rafe("Picture the Two Tracks Coach, then. The study assistant in the Android app, Python backend on Azure. A learner edits a note, and the backend posts NoteUpdated to a custom topic, a board for news our own code invents."),
            nova("And who's listening? A small function. That's Azure Functions, a bit of code that wakes only when something happens. The re-embed function, which refreshes a note's search data. One topic, many subscriptions, and each gets its own copy."),
            rafe("Everyone gets their own copy. How very generous of it."),
            nova("And the envelope it travels in? That's your question, darling."),
            rafe("The schema, the envelope's layout. CloudEvents 1.0, the open standard to prefer. A type for what happened, a source for who sent it, and a subject for what it happened to, written like a path."),
            nova("Right answer. You know how I like a well-addressed envelope. [kiss]"),
            rafe("And why write the subject like a path? Slash notes, slash german, slash 42."),
            nova("So subscribers can choose which letters to open, by how the subject begins or ends. The details ride inside, in the data, like the note's word count. Keep that in mind, darling. It comes up later."),
            rafe("Mm. My turn. Exam style. The Coach must react when a PDF is uploaded to its storage account, Azure's place for files. Which kind of topic carries that event?"),
            nova("A custom topic. The backend publishes it."),
            rafe("[sigh] No kiss for that one. Storage posts its own BlobCreated event to a system topic, a board where an Azure service posts news about itself. You don't publish it, you just subscribe. Sometimes you only have to listen."),
            nova("I'll listen more. Promise. Let's talk filters, then. A filter is a rule on the subscription, deciding which events get through. Who's allowed in, and who isn't."),
            rafe("I like a little exclusivity. Three kinds?"),
            nova("Three. An event type filter takes only some kinds, like NoteUpdated. A subject filter checks how the subject begins or ends, so slash notes slash german means German notes only. And an advanced filter tests a field inside the data. So, darling. Only re-embed notes longer than 20 words. Which filter?"),
            rafe("An advanced filter. Data dot wordCount, NumberGreaterThan, 20. The subject can't see inside the data."),
            nova("Right answer. Size does matter, to that filter. [kiss]"),
            rafe("Behave. Now, what if the handler doesn't answer? Asking for a friend."),
            nova("Event Grid wants a success code, meaning OK, within 30 seconds. If not, it tries again, waiting longer each time. By default, up to 30 attempts or 24 hours, whichever comes first. You can lower both on the subscription, its retry policy. And a 400 isn't retried at all: it means the event itself was bad. Persistent, but not forever."),
            rafe("Persistent. I respect that. And after 24 hours?"),
            nova("That's my question for you. The handler was down for two days. Afterwards, the events must still be there. What do you set up?"),
            rafe("Dead-lettering. Point the subscription at a blob container, a folder in storage. Undelivered events are written there instead of dropped, because by default they're simply dropped."),
            nova("Right answer. Nothing we say gets lost. [kiss]"),
            rafe("My question, then. A handler sometimes gets the same NoteUpdated event twice. What's the right response?"),
            nova("Make it idempotent: safe to run twice, so the same event again leaves the same result. Delivery is at least once, by design. If the handler's answer gets lost, Event Grid can't tell, and it knocks again. So keep the ids of events you've handled."),
            rafe("Right answer. Twice is never too much, as long as nothing changes. [kiss]"),
            nova("Under the hood, each time an event arrives, Event Grid checks it against every subscription's filters, and sends a copy to each match as a web call. Every copy gets its own retries, so one sleepy handler never holds up the rest."),
            rafe("One sleepy handler. I'm not naming names."),
            nova("And where does Event Grid stop working, Rafe? Every romance has its limits."),
            rafe("It doesn't promise order. And it tells everyone. Jobs that one worker must finish, one by one, go on a Service Bus queue, a to-do list for workers. That's in the Service Bus episode."),
            nova("And a webhook is a web address that accepts events. Before any arrive, it has to prove it wants them. The Functions episode has the handler side."),
            rafe("Then my question. A new webhook gets nothing at all, not one event, and the filters are fine. Why?"),
            nova("It never answered the handshake. That's endpoint validation. Event Grid knocks first, and the webhook must answer the right way before anything flows. A Functions trigger does it for you."),
            rafe("Right answer. Always answer when someone knocks. [kiss]"),
            nova("Last question, for the exam. What do we remember?"),
            rafe("Undelivered events must be kept: a dead-letter blob container. Only German notes: a subject begins-with filter. A number in the data: an advanced filter. The same event twice: an idempotent handler. And an upload to storage: a system topic."),
            nova("Five for five. That one earns a long one. [kiss]"),
            rafe("Mm. And something to try today, darling?"),
            nova("Create a custom topic with the CloudEvents input schema. Subscribe a small function or webhook with an event type filter. Publish two events from Python, one matching and one not. Then add a dead-letter container and check the subscription's delivery metrics, the counts of what arrived and what failed."),
            rafe("And you're the only notice I ever want pushed to me. Goodnight, everyone. See you next time."),
        ),
        series = PodcastSeries.AI200,
        style = PodcastStyle.COUPLES,
    )

    val podcasts: List<Podcast> = listOf(classic, caseFiles, gossip, lateNight, couples)

    // The diagrams the five boards draw, each scene keyed to its own episode's lines.

    private fun flow() = DiagramSpec(
        "The Coach API publishes to a custom topic; each subscription pushes its own copy on, and events out of retries go to dead-letter blobs.",
        nodes = listOf(
            DNode("pub", "Coach API", 0.17f, 0.5f, w = 0.3f, h = 0.2f, tone = 1, sub = "publisher"),
            DNode("topic", "Custom topic", 0.5f, 0.5f, w = 0.28f, h = 0.26f, kind = NodeKind.CLOUD),
            DNode("fn", "Re-embed function", 0.83f, 0.18f, w = 0.32f, h = 0.2f, sub = "type filter"),
            DNode("q", "Service Bus queue", 0.83f, 0.5f, w = 0.32f, h = 0.2f, sub = "subject filter"),
            DNode("dl", "Dead-letter blobs", 0.83f, 0.82f, w = 0.32f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2),
        ),
        edges = listOf(
            DEdge("pub", "topic", "publish"),
            DEdge("topic", "fn", "push"),
            DEdge("topic", "q", "push"),
            DEdge("topic", "dl", "retries out", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun topics() = DiagramSpec(
        "Azure services post their own news to system topics; your code publishes to a custom topic, in the CloudEvents 1.0 schema.",
        nodes = listOf(
            DNode("storage", "Storage account", 0.24f, 0.18f, w = 0.42f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "a file uploaded"),
            DNode("sys", "System topic", 0.76f, 0.18f, w = 0.42f, h = 0.2f, tone = 2, sub = "BlobCreated"),
            DNode("api", "Coach API", 0.24f, 0.52f, w = 0.42f, h = 0.2f, tone = 1, sub = "your own code"),
            DNode("cust", "Custom topic", 0.76f, 0.52f, w = 0.42f, h = 0.2f, sub = "NoteUpdated"),
            DNode("ce", "CloudEvents 1.0", 0.5f, 0.85f, w = 0.7f, h = 0.18f, kind = NodeKind.PILL, sub = "type, source, subject, data"),
        ),
        edges = listOf(
            DEdge("storage", "sys", "posts itself"),
            DEdge("api", "cust", "publishes"),
            DEdge("cust", "ce", "schema"),
        ),
        aspect = 0.8f,
    )

    private fun subs() = DiagramSpec(
        "Each subscription on the topic is a standing order, and each handler gets its own copy of the event.",
        nodes = listOf(
            DNode("topic", "Custom topic", 0.5f, 0.15f, w = 0.4f, h = 0.2f, tone = 1, sub = "NoteUpdated"),
            DNode("fn", "Function", 0.18f, 0.55f, w = 0.3f, h = 0.2f, sub = "re-embed"),
            DNode("hook", "Webhook", 0.5f, 0.55f, w = 0.3f, h = 0.2f, sub = "a web address"),
            DNode("q", "Queue", 0.82f, 0.55f, w = 0.3f, h = 0.2f, sub = "Service Bus"),
            DNode("note", "One copy each", 0.5f, 0.88f, w = 0.6f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
        ),
        edges = listOf(
            DEdge("topic", "fn", "subscription"),
            DEdge("topic", "hook"),
            DEdge("topic", "q"),
        ),
        aspect = 0.8f,
    )

    private fun filters() = DiagramSpec(
        "A subscription's filters let through only some events: by event type, by how the subject begins or ends, or by a field in the data.",
        nodes = listOf(
            DNode("topic", "Custom topic", 0.5f, 0.14f, w = 0.4f, h = 0.2f, tone = 1),
            DNode("type", "Event type", 0.18f, 0.5f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "NoteUpdated"),
            DNode("subj", "Subject", 0.5f, 0.5f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "begins with"),
            DNode("adv", "Advanced", 0.82f, 0.5f, w = 0.3f, h = 0.24f, kind = NodeKind.NOTE, sub = "wordCount over 20"),
            DNode("handler", "Handler", 0.5f, 0.86f, w = 0.4f, h = 0.18f, tone = 2),
        ),
        edges = listOf(
            DEdge("topic", "type"),
            DEdge("topic", "subj"),
            DEdge("topic", "adv"),
            DEdge("type", "handler"),
            DEdge("subj", "handler"),
            DEdge("adv", "handler"),
        ),
        aspect = 0.8f,
    )

    private fun delivery() = DiagramSpec(
        "No OK within 30 seconds means a retry; out of retries, an event is dead-lettered or dropped. Repeats are normal, so handlers are idempotent.",
        nodes = listOf(
            DNode("topic", "Topic", 0.2f, 0.16f, w = 0.32f, h = 0.2f, tone = 1),
            DNode("handler", "Handler", 0.75f, 0.16f, w = 0.42f, h = 0.2f, sub = "OK within 30 s"),
            DNode("retry", "Retries", 0.75f, 0.5f, w = 0.42f, h = 0.2f, sub = "30 tries or 24 hours"),
            DNode("dl", "Dead-letter container", 0.75f, 0.84f, w = 0.44f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2, sub = "or else dropped"),
            DNode("idem", "Idempotent", 0.24f, 0.62f, w = 0.4f, h = 0.24f, kind = NodeKind.NOTE, sub = "safe to run twice"),
        ),
        edges = listOf(
            DEdge("topic", "handler", "push"),
            DEdge("handler", "retry", "no OK"),
            DEdge("retry", "dl", "out of tries", dashed = true),
        ),
        aspect = 0.8f,
    )

    private fun limits() = DiagramSpec(
        "Where Event Grid stops: jobs for one worker go on a Service Bus queue, order is not promised, and webhooks must pass the handshake.",
        nodes = listOf(
            DNode("jobs", "Jobs, one by one", 0.26f, 0.2f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, sub = "not news"),
            DNode("sb", "Service Bus queue", 0.76f, 0.2f, w = 0.44f, h = 0.22f, tone = 1, sub = "another episode"),
            DNode("order", "No order promised", 0.26f, 0.76f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE),
            DNode("hs", "Validation handshake", 0.76f, 0.76f, w = 0.44f, h = 0.2f, tone = 2, sub = "webhooks prove it"),
        ),
        edges = listOf(DEdge("jobs", "sb", "use instead")),
        aspect = 0.8f,
    )

    private fun exam() = DiagramSpec(
        "The exam's Event Grid questions, each as the phrase to listen for and the answer it points to.",
        nodes = listOf(
            DNode("kept", "Keep undelivered", 0.26f, 0.14f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "dead-letter container"),
            DNode("branch", "Only German notes", 0.76f, 0.14f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "subject begins-with"),
            DNode("number", "A number in the data", 0.26f, 0.47f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "advanced filter"),
            DNode("twice", "Same event twice", 0.76f, 0.47f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "idempotent handler"),
            DNode("azure", "Azure's own events", 0.26f, 0.8f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "system topic"),
            DNode("jobs", "Jobs, one by one", 0.76f, 0.8f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Service Bus queue"),
        ),
        aspect = 0.8f,
    )

    private val all6 = setOf("kept", "branch", "number", "twice", "azure", "jobs")
    private val cloudEvent = BoardCard("Python", "CloudEvent(type=\"TwoTracks.Coach.NoteUpdated\", subject=\"/notes/german/42\", data=d)", code = true)
    private val today = BoardCard("Today", "A CloudEvents topic, a filtered subscription, two events, dead letters.")

    private fun classicBoard() = PodcastBoard(
        "ai-eventgrid",
        listOf(
            // 0: "Hi, and welcome back. Today is Event Grid..."
            Scene(
                0, "A notice board that pushes", flow(),
                beats = listOf(
                    Beat(0, show = setOf("pub", "topic"), light = setOf("topic"), card = BoardCard("Event Grid", "A notice board that pushes: news goes to everyone who asked for it.")),
                    Beat(2, light = setOf("pub"), card = BoardCard("An event", "A small message that something happened. News, not a job.")),
                    Beat(4, show = setOf("q"), light = setOf("q"), pulse = "topic" to "q", card = BoardCard("News, not a job", "Jobs for one worker go on a Service Bus queue, a to-do list.")),
                    Beat(6, light = setOf("pub"), pulse = "pub" to "topic", card = BoardCard("The publisher", "The Coach backend sends NoteUpdated when a learner edits a note.")),
                ),
            ),
            // 8: "Onto a topic. A topic is one notice board..."
            Scene(
                8, "Two kinds of topic", topics(),
                beats = listOf(
                    Beat(8, show = setOf("api", "cust"), light = setOf("cust"), pulse = "api" to "cust", card = BoardCard("Custom topic", "A board your own code posts to, with news you invent.")),
                    Beat(10, show = setOf("storage", "sys"), light = setOf("sys"), pulse = "storage" to "sys", card = BoardCard("System topic", "An Azure service posts news about itself, like BlobCreated.")),
                ),
            ),
            // 12: "You can, with a subscription..."
            Scene(
                12, "Subscriptions", subs(),
                beats = listOf(
                    Beat(12, show = setOf("topic", "fn"), light = setOf("fn"), pulse = "topic" to "fn", card = BoardCard("Subscription", "A standing order: send me these events, at this address.")),
                    Beat(14, show = setOf("hook", "q", "note"), light = setOf("fn", "hook", "q"), card = BoardCard("Handlers", "A function, a webhook or a Service Bus queue. Each gets its own copy.")),
                ),
            ),
            // 15: "What does an event actually look like?"
            Scene(
                15, "The envelope", topics(),
                beats = listOf(
                    Beat(16, show = setOf("api", "cust", "ce"), light = setOf("ce"), pulse = "cust" to "ce", card = BoardCard("CloudEvents 1.0", "The open standard schema to prefer; chosen when you create the topic.")),
                    Beat(18, light = setOf("ce"), card = cloudEvent),
                    Beat(20, light = setOf("cust"), card = BoardCard("Subjects as paths", "Write them as paths on purpose: subscribers filter on them.")),
                ),
            ),
            // 21: "Filter. So a subscriber doesn't have to take everything?"
            Scene(
                21, "Filters", filters(),
                beats = listOf(
                    Beat(22, show = setOf("topic", "type"), light = setOf("type"), pulse = "topic" to "type", card = BoardCard("Event type filter", "Only the listed kinds of event, such as NoteUpdated.")),
                    Beat(24, show = setOf("subj"), light = setOf("subj"), pulse = "topic" to "subj", card = BoardCard("Subject filter", "Begins with /notes/german/, or ends with .pdf.")),
                    Beat(26, show = setOf("adv", "handler"), light = setOf("adv"), pulse = "topic" to "adv", card = BoardCard("Advanced filter", "data.wordCount NumberGreaterThan 20: a test on a field in the data.")),
                ),
            ),
            // 27: "Got it. Now the scary part. What if my handler is down?"
            Scene(
                27, "Delivery and retries", delivery(),
                beats = listOf(
                    Beat(28, show = setOf("topic", "handler"), light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("At least once", "The handler must answer OK within 30 seconds, or it's a failure.")),
                    Beat(30, show = setOf("retry"), light = setOf("retry"), pulse = "handler" to "retry", card = BoardCard("Retries", "30 attempts or 24 hours by default. A 400 is not retried.")),
                    Beat(32, show = setOf("dl"), light = setOf("dl"), pulse = "retry" to "dl", card = BoardCard("Dead-lettering", "Off by default, so events are dropped. Point it at a blob container.")),
                    Beat(34, light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("Twice is normal", "A lost answer means Event Grid sends the event again.")),
                    Beat(36, show = setOf("idem"), light = setOf("idem"), card = BoardCard("Idempotent", "Safe to run twice: write by id, or keep the ids you've done.")),
                ),
            ),
            // 37: "Anything else that can catch me out?"
            Scene(
                37, "Where it stops", limits(),
                beats = listOf(
                    Beat(38, show = setOf("order", "hs"), light = setOf("order", "hs"), card = BoardCard("Order and handshakes", "No order promised. A webhook must answer the validation handshake.")),
                    Beat(40, show = setOf("jobs", "sb"), light = setOf("sb"), pulse = "jobs" to "sb", card = BoardCard("Jobs: a queue", "Work one worker must finish, job by job, goes on a Service Bus queue.")),
                ),
            ),
            // 41: "So what do I remember for the exam?"
            Scene(
                41, "For the exam", exam(),
                beats = listOf(
                    Beat(42, show = all6, light = setOf("kept", "branch", "number", "twice", "azure"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: dead letters, filters, idempotent.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    private fun caseFilesBoard() = PodcastBoard(
        "ai-eventgrid--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Edits That Vanished..."
            Scene(
                0, "The Edits That Vanished",
                DiagramSpec(
                    "WordWise's backend publishes list edits to a custom topic, but the search function was down, and Monday's edits never arrived.",
                    nodes = listOf(
                        DNode("phone", "WordWise app", 0.22f, 0.18f, w = 0.38f, h = 0.2f, sub = "Android"),
                        DNode("api", "Python backend", 0.22f, 0.72f, w = 0.38f, h = 0.2f, tone = 1, sub = "the publisher"),
                        DNode("fn", "Search function", 0.76f, 0.18f, w = 0.4f, h = 0.2f, tone = 2, sub = "down 31 hours"),
                        DNode("topic", "Custom topic", 0.76f, 0.72f, w = 0.4f, h = 0.22f, sub = "ListUpdated"),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "edits"),
                        DEdge("api", "topic", "publish"),
                        DEdge("topic", "fn", "push", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "fn"), light = setOf("fn"), pulse = "phone" to "api", card = BoardCard("The case", "Monday's word list edits never showed up in search.")),
                    Beat(2, show = setOf("topic"), light = setOf("topic"), pulse = "api" to "topic", card = BoardCard("Event Grid", "A notice board that pushes. An event is news, not a job.")),
                    Beat(4, light = setOf("topic", "fn"), pulse = "topic" to "fn", card = BoardCard("Under the picture", "A topic takes events over HTTPS; each subscription gets a web call.")),
                ),
            ),
            // 5: "Then here's my first suspect..."
            Scene(
                5, "Nobody home", delivery(),
                beats = listOf(
                    Beat(5, show = setOf("topic"), light = setOf("topic"), card = BoardCard("Suspect one", "The backend never sent them? Its logs say every event went out.")),
                    Beat(6, show = setOf("handler"), light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("The first clue", "The search function was down for 31 hours.")),
                    Beat(8, show = setOf("retry"), light = setOf("retry"), pulse = "handler" to "retry", card = BoardCard("Retries", "Growing gaps, up to 30 attempts or 24 hours by default.")),
                    Beat(10, show = setOf("dl"), light = setOf("dl"), pulse = "retry" to "dl", card = BoardCard("The culprit", "No dead-letter container, so the events were dropped.")),
                    Beat(12, light = setOf("retry", "dl"), card = BoardCard("Retry policy", "Fewer attempts or a shorter time to live; never past 24 hours.")),
                ),
            ),
            // 14: "One down. But the case file has a second witness..."
            Scene(
                14, "The second witness", delivery(),
                beats = listOf(
                    Beat(14, show = setOf("topic", "handler"), light = setOf("handler"), card = BoardCard("Double counts", "The weekly report shows some learners editing twice the words.")),
                    Beat(16, light = setOf("topic"), pulse = "topic" to "handler", card = BoardCard("At least once", "A lost answer means a resend. Duplicates are normal, not a bug.")),
                    Beat(18, show = setOf("idem"), light = setOf("idem"), card = BoardCard("Idempotent", "Safe to run twice: keep the ids of events already counted.")),
                ),
            ),
            // 19: "Third witness?"
            Scene(
                19, "The third witness", filters(),
                beats = listOf(
                    Beat(20, show = setOf("topic", "handler"), light = setOf("handler"), card = BoardCard("Every language arrives", "The German service got every list, and wanted German only.")),
                    Beat(22, show = setOf("subj"), light = setOf("subj"), pulse = "topic" to "subj", card = BoardCard("Subject filter", "Begins with /lists/german/: German lists only.")),
                    Beat(24, show = setOf("type", "adv"), light = setOf("adv", "type"), pulse = "topic" to "adv", card = BoardCard("Advanced filter", "data.wordCount NumberGreaterThan 20. Event type: ListUpdated only.")),
                ),
            ),
            // 25: "Fourth witness. I can feel it."
            Scene(
                25, "The fourth witness", limits(),
                beats = listOf(
                    Beat(26, show = setOf("hs"), light = setOf("hs"), card = BoardCard("Nothing arrived", "A new webhook got no events at all.")),
                    Beat(28, light = setOf("hs"), card = BoardCard("Endpoint validation", "The webhook must answer the handshake. Functions do it for you.")),
                ),
            ),
            // 29: "What format are these events in, anyway?"
            Scene(
                29, "The envelope", topics(),
                beats = listOf(
                    Beat(30, show = setOf("api", "cust", "ce"), light = setOf("ce"), pulse = "cust" to "ce", card = cloudEvent),
                ),
            ),
            // 31: "Last loose end. The report also had lists out of order."
            Scene(
                31, "Loose ends", limits(),
                beats = listOf(
                    Beat(32, show = setOf("order", "jobs", "sb"), light = setOf("order", "sb"), pulse = "jobs" to "sb", card = BoardCard("Jobs: a queue", "No order promised. One worker per job is a Service Bus queue.")),
                ),
            ),
            // 33: "And the upload of a photo?"
            Scene(
                33, "Azure's own news", topics(),
                beats = listOf(
                    Beat(34, show = setOf("storage", "sys", "api", "cust"), light = setOf("sys"), pulse = "storage" to "sys", card = BoardCard("System topic", "Storage posts BlobCreated itself. Your own news: a custom topic.")),
                ),
            ),
            // 35: "So, the case summary for the exam?"
            Scene(
                35, "Case closed", exam(),
                beats = listOf(
                    Beat(36, show = all6, light = setOf("kept", "twice", "branch", "number"), card = BoardCard("Listen for the phrase", "Dead letters, idempotent, subject and advanced filters, the handshake.")),
                    Beat(38, card = today),
                ),
            ),
        ),
    )

    private fun gossipBoard() = PodcastBoard(
        "ai-eventgrid--gossip",
        listOf(
            // 0: "Okay, did you hear what happened on the Coach team this week?..."
            Scene(
                0, "The Coach's notice board", flow(),
                beats = listOf(
                    Beat(0, show = setOf("pub", "topic"), light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Event Grid", "A notice board that pushes, for the Coach's Python backend.")),
                    Beat(2, light = setOf("pub"), card = BoardCard("An event", "A small message that something happened. News, not a job.")),
                    Beat(4, show = setOf("fn", "q"), light = setOf("fn", "q"), pulse = "topic" to "fn", card = BoardCard("Subscriptions", "Standing orders: Event Grid makes a web call to each handler.")),
                ),
            ),
            // 5: "Okay. So who went first?"
            Scene(
                5, "Monday: Tom", topics(),
                beats = listOf(
                    Beat(6, show = setOf("api", "cust", "ce"), light = setOf("ce"), pulse = "cust" to "ce", card = BoardCard("CloudEvents 1.0", "Tom's custom topic uses the open standard schema. Good choice.")),
                    Beat(8, light = setOf("ce"), card = cloudEvent),
                    Beat(10, show = setOf("storage"), light = setOf("storage"), card = BoardCard("Publishing BlobCreated?", "Tom wrote a routine to publish upload events himself.")),
                    Beat(12, show = setOf("sys"), light = setOf("sys"), pulse = "storage" to "sys", card = BoardCard("System topic", "Storage posts its own events. You only subscribe.")),
                ),
            ),
            // 13: "Poor Tom. Tuesday?"
            Scene(
                13, "Tuesday: Dana", delivery(),
                beats = listOf(
                    Beat(14, show = setOf("topic", "handler"), light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("Down for 30 hours", "Dana's re-embed function missed a day of note edits.")),
                    Beat(16, show = setOf("retry"), light = setOf("retry"), pulse = "handler" to "retry", card = BoardCard("Retries", "OK within 30 seconds, or retry: 30 attempts or 24 hours.")),
                    Beat(18, show = setOf("dl"), light = setOf("dl"), pulse = "retry" to "dl", card = BoardCard("Dead-lettering", "Off by default, so they were dropped. A blob container keeps them.")),
                    Beat(20, light = setOf("retry"), card = BoardCard("Retry policy", "Fewer attempts or a shorter time to live; 24 hours is the ceiling.")),
                ),
            ),
            // 21: "Ouch. Wednesday?"
            Scene(
                21, "Wednesday: Marcus", delivery(),
                beats = listOf(
                    Beat(22, show = setOf("topic", "handler"), light = setOf("handler"), card = BoardCard("Double streaks", "Marcus added one to a total for every NoteUpdated event.")),
                    Beat(24, show = setOf("idem"), light = setOf("idem"), pulse = "topic" to "handler", card = BoardCard("Idempotent", "At least once means repeats. Keep the ids of events counted.")),
                ),
            ),
            // 25: "And Thursday, I'm guessing, the intern."
            Scene(
                25, "Thursday: the intern", limits(),
                beats = listOf(
                    Beat(26, show = setOf("jobs"), light = setOf("jobs"), card = BoardCard("Essays, one at a time", "Each marked exactly once, in order. That's jobs, not news.")),
                    Beat(28, show = setOf("sb", "order"), light = setOf("sb", "order"), pulse = "jobs" to "sb", card = BoardCard("A Service Bus queue", "Event Grid promises no order; one worker per job is a queue.")),
                ),
            ),
            // 29: "And Friday? There's always a Friday."
            Scene(
                29, "Friday: Priya", filters(),
                beats = listOf(
                    Beat(30, show = setOf("topic", "handler"), light = setOf("handler"), card = BoardCard("Nothing arrived", "Priya's webhook got no events for a whole day.")),
                    Beat(32, show = setOf("type", "subj", "adv"), light = setOf("type", "subj", "adv"), pulse = "topic" to "subj", card = BoardCard("Her filters were fine", "Event type, subject begins-with, and advanced on wordCount.")),
                    Beat(34, light = setOf("handler"), card = BoardCard("The handshake", "Endpoint validation: the webhook must prove it wants the events.")),
                ),
            ),
            // 35: "How does it know who to tell, under the hood?"
            Scene(
                35, "Under the hood", flow(),
                beats = listOf(
                    Beat(36, show = setOf("pub", "topic", "fn", "q"), light = setOf("fn", "q"), pulse = "topic" to "q", card = BoardCard("A copy each", "Every matching subscription gets its own copy, with its own retries.")),
                    Beat(38, show = setOf("dl"), light = setOf("dl"), pulse = "topic" to "dl", card = BoardCard("Where it stops", "Order or one worker per job: a queue. Keeping failures: turn it on.")),
                ),
            ),
            // 39: "So, the gossip I should remember for the exam?"
            Scene(
                39, "For the exam", exam(),
                beats = listOf(
                    Beat(40, show = all6, light = setOf("kept", "branch", "number", "twice", "azure"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. One colleague, one lesson.")),
                    Beat(42, card = today),
                ),
            ),
        ),
    )

    private fun lateNightBoard() = PodcastBoard(
        "ai-eventgrid--late-night",
        listOf(
            // 0: "Hey, you. It's late, the house is quiet..."
            Scene(
                0, "News that pushes", flow(),
                beats = listOf(
                    Beat(0, card = BoardCard("Tonight", "How one part of an app hears the news from another.")),
                    Beat(2, show = setOf("pub", "topic"), light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Event Grid", "A notice board that carries a copy to everyone who asked.")),
                    Beat(4, light = setOf("pub"), card = BoardCard("An event", "A small message that something happened. News, not a job.")),
                    Beat(6, show = setOf("fn"), light = setOf("fn"), pulse = "topic" to "fn", card = BoardCard("Under the picture", "Azure checks each subscription, then makes a web call to its handler.")),
                ),
            ),
            // 7: "Who's posting, in our story?"
            Scene(
                7, "Two kinds of board", topics(),
                beats = listOf(
                    Beat(8, show = setOf("api", "cust"), light = setOf("cust"), pulse = "api" to "cust", card = BoardCard("Custom topic", "A board your own code posts to: the Coach's NoteUpdated.")),
                    Beat(10, show = setOf("storage", "sys"), light = setOf("sys"), pulse = "storage" to "sys", card = BoardCard("System topic", "Storage posts BlobCreated itself. You just listen.")),
                ),
            ),
            // 11: "And who's listening, on the Coach?"
            Scene(
                11, "Who's listening", subs(),
                beats = listOf(
                    Beat(12, show = setOf("topic", "fn", "hook", "q", "note"), light = setOf("fn"), pulse = "topic" to "fn", card = BoardCard("A handler", "The re-embed function. Each subscription gets its own copy.")),
                ),
            ),
            // 13: "What does a single event look like? Up close."
            Scene(
                13, "The envelope", topics(),
                beats = listOf(
                    Beat(14, show = setOf("api", "cust", "ce"), light = setOf("ce"), pulse = "cust" to "ce", card = BoardCard("CloudEvents 1.0", "Type, source, and a subject written like a path.")),
                ),
            ),
            // 15: "Paths. So you can choose which letters you open."
            Scene(
                15, "Choosing your letters", filters(),
                beats = listOf(
                    Beat(16, show = setOf("topic", "type", "subj"), light = setOf("type", "subj"), pulse = "topic" to "subj", card = BoardCard("Type and subject filters", "NoteUpdated only; subject begins with /notes/german/.")),
                    Beat(18, show = setOf("handler"), light = setOf("handler"), card = BoardCard("The data", "The note's id and word count ride inside the envelope.")),
                    Beat(20, show = setOf("adv"), light = setOf("adv"), pulse = "topic" to "adv", card = BoardCard("Advanced filter", "Tests a field in the data: a word count greater than 20.")),
                ),
            ),
            // 21: "Now, the part that keeps me up..."
            Scene(
                21, "If the handler sleeps", delivery(),
                beats = listOf(
                    Beat(22, show = setOf("topic", "handler", "retry"), light = setOf("retry"), pulse = "handler" to "retry", card = BoardCard("Retries", "OK within 30 seconds, or retry: 30 attempts or 24 hours.")),
                    Beat(24, light = setOf("retry"), card = BoardCard("Retry policy", "Fewer attempts or a shorter time to live. A 400 is not retried.")),
                    Beat(26, show = setOf("dl"), light = setOf("dl"), pulse = "retry" to "dl", card = BoardCard("Dead-lettering", "Off by default: dropped. A blob container keeps them for you.")),
                    Beat(28, light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("At least once", "A lost answer means a knock again. Repeats happen.")),
                    Beat(30, show = setOf("idem"), light = setOf("idem"), card = BoardCard("Idempotent", "Safe to run twice: replace by id, or keep the ids you've seen.")),
                ),
            ),
            // 31: "How does Event Grid decide who gets what, so quickly?"
            Scene(
                31, "Where it stops", limits(),
                beats = listOf(
                    Beat(32, card = BoardCard("A copy each", "Every match gets its own copy and its own retries.")),
                    Beat(34, show = setOf("jobs", "sb", "order"), light = setOf("jobs", "sb"), pulse = "jobs" to "sb", card = BoardCard("Jobs: a queue", "No order promised. Work for one worker goes on a Service Bus queue.")),
                    Beat(36, show = setOf("hs"), light = setOf("hs"), card = BoardCard("Endpoint validation", "A webhook must answer the handshake first. Functions do it for you.")),
                ),
            ),
            // 37: "Before you send me off to bed. What do I remember for the exam?"
            Scene(
                37, "For the exam", exam(),
                beats = listOf(
                    Beat(38, show = all6, light = setOf("kept", "branch", "number", "twice", "azure"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer: five little things.")),
                    Beat(40, card = today),
                ),
            ),
        ),
    )

    private fun couplesBoard() = PodcastBoard(
        "ai-eventgrid--couples",
        listOf(
            // 0: "Tonight is date night, darling..."
            Scene(
                0, "Date night", flow(),
                beats = listOf(
                    Beat(0, show = setOf("pub", "topic"), card = BoardCard("Date night", "News that just can't wait: Event Grid.")),
                    Beat(2, light = setOf("topic"), pulse = "pub" to "topic", card = BoardCard("Event Grid", "A notice board that pushes a copy to everyone who asked.")),
                    Beat(4, light = setOf("pub"), card = BoardCard("An event", "A small message that something happened. News, not a job.")),
                    Beat(6, show = setOf("fn", "q"), light = setOf("fn"), pulse = "topic" to "fn", card = BoardCard("Under the picture", "Each subscription is a standing order; each handler gets a web call.")),
                ),
            ),
            // 7: "Picture the Two Tracks Coach, then..."
            Scene(
                7, "A well-addressed envelope", topics(),
                beats = listOf(
                    Beat(7, show = setOf("api", "cust"), light = setOf("cust"), pulse = "api" to "cust", card = BoardCard("Custom topic", "The Coach's backend posts NoteUpdated, news its own code invents.")),
                    Beat(10, show = setOf("ce"), light = setOf("ce"), card = BoardCard("Quiz: the envelope", "What is the envelope's layout called, and which one to prefer?")),
                    Beat(11, pulse = "cust" to "ce", card = BoardCard("Right answer", "The schema: CloudEvents 1.0, with a type, a source and a subject.")),
                    Beat(13, light = setOf("ce"), card = cloudEvent),
                ),
            ),
            // 15: "Mm. My turn. Exam style..."
            Scene(
                15, "An upload", topics(),
                beats = listOf(
                    Beat(15, show = setOf("storage"), light = setOf("storage"), card = BoardCard("Quiz: a PDF upload", "The Coach reacts to a PDF upload. Which kind of topic carries it?")),
                    Beat(17, show = setOf("sys"), light = setOf("sys"), pulse = "storage" to "sys", card = BoardCard("No kiss: a system topic", "Storage posts BlobCreated itself. You only subscribe.")),
                ),
            ),
            // 18: "I'll listen more. Promise. Let's talk filters, then..."
            Scene(
                18, "Who gets in", filters(),
                beats = listOf(
                    Beat(18, show = setOf("topic", "handler"), light = setOf("topic"), card = BoardCard("Filters", "Rules on the subscription that decide which events get through.")),
                    Beat(20, show = setOf("type", "subj", "adv"), light = setOf("type", "subj"), pulse = "topic" to "subj", card = BoardCard("Quiz: over 20 words", "Only re-embed notes longer than 20 words. Which filter?")),
                    Beat(21, light = setOf("adv"), pulse = "topic" to "adv", card = BoardCard("Right answer", "An advanced filter: data.wordCount NumberGreaterThan 20.")),
                ),
            ),
            // 23: "Behave. Now, what if the handler doesn't answer?..."
            Scene(
                23, "Persistent", delivery(),
                beats = listOf(
                    Beat(24, show = setOf("topic", "handler", "retry"), light = setOf("retry"), pulse = "handler" to "retry", card = BoardCard("Retries", "OK within 30 seconds, or retry: 30 attempts or 24 hours.")),
                    Beat(26, light = setOf("handler"), card = BoardCard("Quiz: down for two days", "The events must still be there afterwards. What do you set up?")),
                    Beat(27, show = setOf("dl"), light = setOf("dl"), pulse = "retry" to "dl", card = BoardCard("Right answer", "Dead-lettering to a blob container. By default, events are dropped.")),
                    Beat(29, light = setOf("handler"), pulse = "topic" to "handler", card = BoardCard("Quiz: the same event twice", "A handler gets the same NoteUpdated twice. The right response?")),
                    Beat(30, show = setOf("idem"), light = setOf("idem"), card = BoardCard("Right answer", "Make it idempotent: safe to run twice. Keep the ids you've handled.")),
                ),
            ),
            // 32: "Under the hood, each time an event arrives..."
            Scene(
                32, "Every romance has limits", limits(),
                beats = listOf(
                    Beat(32, card = BoardCard("A copy each", "Every match gets its own copy and its own retries.")),
                    Beat(35, show = setOf("jobs", "sb", "order"), light = setOf("jobs", "sb"), pulse = "jobs" to "sb", card = BoardCard("Jobs: a queue", "No order promised. Work for one worker goes on a Service Bus queue.")),
                    Beat(37, show = setOf("hs"), light = setOf("hs"), card = BoardCard("Quiz: a silent webhook", "A new webhook gets no events, and the filters are fine. Why?")),
                    Beat(38, light = setOf("hs"), card = BoardCard("Right answer", "Endpoint validation: the webhook must answer the handshake first.")),
                ),
            ),
            // 40: "Last question, for the exam. What do we remember?"
            Scene(
                40, "For the exam", exam(),
                beats = listOf(
                    Beat(41, show = all6, light = setOf("kept", "branch", "number", "twice", "azure"), card = BoardCard("Listen for the phrase", "Each exam phrase points to one answer. Five for five.")),
                    Beat(44, card = today),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy { listOf(classicBoard(), caseFilesBoard(), gossipBoard(), lateNightBoard(), couplesBoard()) }
}
