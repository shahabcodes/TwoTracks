package com.shahaabapps.core.content

/** The Connect and consume module's Event Hubs and Service Bus topics as Case Files and Gossip, with their boards (Az204StyledPodcasts). */
internal object Az204StyledConnect2 {
    private val eventHubsCase = Podcast(
        "az-eventhubs",
        listOf(
            maya("Welcome to Case Files. Today's case: The Teleporting Bikes. A small bike rental company has an Android app. While you ride, the app sends the bike's position every few seconds to a backend on Azure, through Event Hubs. On Monday, bikes on the live map start jumping backwards along the street. And the billing job, which charges by the minute, bills only half the rides."),
            leo("[chuckle] Bikes that travel in time. Detective Leo reporting. Before I guess, what is Event Hubs, in plain words?"),
            maya("Picture a tape recorder that never stops. Millions of small events a second pour in from many senders. It keeps them for days, and readers play them back at their own speed, even more than once. Reading doesn't remove anything. That's the big difference from a queue."),
            leo("And under the picture? What is it really?"),
            maya("A log. An event hub is a set of append-only logs, called partitions. Each new event is written at the end of one partition and gets a number, its offset. Nothing is changed or removed when someone reads. Events simply age out when the retention period ends, one to seven days on Standard."),
            leo("Got it. So, suspect one: the phones. Their clocks are wrong, so the positions go out in the wrong order."),
            maya("The phones are fine. Each phone sends its pings in the right order. They come out wrong at the reading end. Look at how the hub was made. Four partitions. Picture four lanes on a motorway: cars in one lane stay in order, but the lanes don't keep pace with each other."),
            leo("So if one bike's pings land in different lanes..."),
            maya("Exactly. The app sent each ping with no partition key, so the hub spread them across all four lanes. Order holds only inside one partition, never across the whole hub. A reader takes the lanes at different speeds, and a ping from ten seconds ago turns up after a newer one. The bike jumps backwards."),
            leo("And the fix is a partition key. How does that actually work?"),
            maya("The sender puts a key on each event, here the bike's id. Event Hubs hashes that key, turning it into a number, and the same number always picks the same partition. So every ping from one bike goes down one lane, in order. Different bikes still spread across all four."),
            leo("One mystery down. Now billing. Suspect two: the billing job is too slow and drops events."),
            maya("[pause] Look at the setup first. Two readers: the live map, and billing. And both connect with the same consumer group, the Default one that every hub comes with."),
            leo("What's a consumer group, in plain words?"),
            maya("One reader's own view of the whole stream, with its own bookmark. Inside one consumer group, each partition is read by one reader at a time. So two apps sharing a group don't each get a copy. The partitions are shared out between them. The map took two lanes, billing took the other two, and each saw half the rides."),
            leo("So billing gets its own consumer group, and the map keeps Default."),
            maya("Yes. Readers are separated by consumer group, never by partition. A partition is only ever a slice of the stream. And on Standard you can have up to twenty consumer groups on one hub, so a new reader costs you nothing to set up."),
            leo("Case closed? [pause] No, wait, you've got that look."),
            maya("One more clue. The storage bill doubled this month. The billing job saves its reading position, its checkpoint, to blob storage. And it saves it after every single event."),
            leo("Is a checkpoint expensive?"),
            maya("Each one is a write to storage, and you pay for every write. Thousands of bikes, a ping every few seconds, a write each time. Checkpoint after a batch instead. If the job restarts, it rereads the few events since the last checkpoint, so make handling a repeat harmless."),
            leo("Do they have to write all that checkpoint code themselves?"),
            maya("No. The EventProcessorClient in the SDK reads every partition, shares the partitions out across the running copies of the job, and keeps ownership and checkpoints in a blob container. Or an Azure Function with the Event Hub trigger does the lot, given a storage connection. The Azure Functions episode covers triggers."),
            leo("And when the summer rush comes, and every bike is out?"),
            maya("Two dials. Throughput units are slices of capacity, one megabyte a second in and two out each. Auto-inflate adds units under load, but never takes them back. And partitions give you more readers side by side, but you choose the count when you create the hub, and on Standard it stays fixed."),
            leo("Last thing. The owner wants every ride kept for years, for the insurance company."),
            maya("That's past the edge of retention, which stops at seven days on Standard and ninety on Premium. So turn on Capture. It writes the events to Blob Storage or a data lake, in batches, as Avro files, with no code at all. The Blob Storage episode covers what happens to them there."),
            leo("Then the new developer wants refunds on the same hub. Each refund handled once, with failures set aside to look at."),
            maya("Wrong tool. Event Hubs is a stream, read in bulk. It has no way to settle one message and no dead-letter queue. Jobs that must each be handled, with a pile for failures, belong in Service Bus. And a single this-happened note, pushed to a handler, is Event Grid. Both have their own episodes."),
            leo("So, the case summary for the exam?"),
            maya("Five rules. If two apps must each read every event, give each its own consumer group. If one device's events must stay in order, use a partition key. If events must be archived with no code, pick Capture. If it says millions of events a second, telemetry or clickstreams, pick Event Hubs. And to replay yesterday, use retention and a new consumer group."),
            leo("Case closed. Anything to try today?"),
            maya("Create a Basic namespace and a hub with two partitions. It costs about a cent an hour. Send ten events with the az eventhubs command line, or a small console app. Read them with the Functions Event Hub trigger, or the processor sample. Then check Metrics for incoming and outgoing messages."),
            leo("Ten little bikes, each in its own lane. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val eventHubsGossip = Podcast(
        "az-eventhubs",
        listOf(
            maya("Okay, did you hear what Sam did with the plant app? Grab a tea. This one is about a stream."),
            leo("[laugh] I heard the chart went half empty and a fern nearly died. What happened?"),
            maya("So, the team builds an Android app for houseplants. Little soil sensors in the pots send a moisture reading every minute, thousands of pots, all day, and it all pours into Azure Event Hubs, behind a small backend. The app shows a live chart of each plant, and an alert job tells you when to water."),
            leo("Remind me what Event Hubs is, in plain words, before the drama."),
            maya("A tape recorder that never stops. Millions of small events can pour in from many senders. It keeps them for days, and readers play them back at their own pace, even more than once. Reading doesn't remove anything, which is the big difference from a queue."),
            leo("Okay. And Sam?"),
            maya("Sam added the alert job, and pointed it at the same consumer group as the live chart. The Default one. Suddenly the chart showed half the plants, and the alerts missed the other half. One fern went three days without water."),
            leo("[sigh] Poor fern. What's a consumer group?"),
            maya("One reader's own view of the whole stream, with its own bookmark. The hub is split into partitions, and inside one consumer group each partition is read by one reader at a time. So two apps in one group don't both get everything. They split the partitions between them. Aisha fixed it in five minutes: a consumer group each."),
            leo("So readers are separated by consumer group."),
            maya("Never by partition. A partition is only a slice of the stream. And on Standard you can have up to twenty consumer groups on a hub, so there's no reason to share. Each group keeps its own checkpoints, so one can be reset without touching the others."),
            leo("Okay, so what's a partition, really?"),
            maya("An append-only log. Picture one lane on a motorway. Each new event joins the end of one lane and gets a number, its offset. Cars in one lane stay in order, and more lanes means more readers working side by side."),
            leo("Which brings us to week two?"),
            maya("Week two was Ben. Ben's chart drew each plant's moisture as a line, and the lines zigzagged. Wet, dry, wet. Ben was sure the sensors were broken, and ordered new ones."),
            leo("And they weren't broken."),
            maya("No. Ben expected the whole hub to be in order. Order holds only inside one partition. The sensors sent with no partition key, so one plant's readings landed in different lanes and came out shuffled. Aisha again: send with the sensor's id as the partition key."),
            leo("How does a key keep them in order?"),
            maya("Event Hubs hashes the key into a number, and the same key always picks the same partition. One sensor, one lane, in order. Different sensors still spread across all the lanes. Ben has cancelled the new sensors. Mostly."),
            leo("[chuckle] Mostly. Week three?"),
            maya("Dana. Dana wrote the alert job's reading loop by hand, and saved a checkpoint after every single reading."),
            leo("What's a checkpoint?"),
            maya("The bookmark. A reader's saved position in a partition, stored in blob storage, so a restarted reader carries on where it left off. But each checkpoint is a write to storage, and writes cost money. Thousands of pots, a reading a minute each, a write for every one. The storage bill was the talk of the kitchen."),
            leo("So what should Dana have done?"),
            maya("Checkpoint after a batch. And skip the hand-written loop. The EventProcessorClient reads all the partitions, shares them across the running copies of the job, and keeps ownership and checkpoints in a blob container. Or an Azure Function with the Event Hub trigger does the lot. The Azure Functions episode has the trigger side."),
            leo("And the intern? There's always the intern."),
            maya("Bless them. The intern wanted to put the shop's orders for new pots on the same hub. Each order handled once, and failed ones set aside to look at."),
            leo("Sounds sensible?"),
            maya("Wrong tool. Event Hubs is a stream, read in bulk, with no way to settle one message and no dead-letter pile for failures. Work that must each be handled is Service Bus, and the Service Bus episode covers it. And a single this-happened note pushed to a handler, like a new photo uploaded, is Event Grid."),
            leo("So where else does Event Hubs stop?"),
            maya("At retention. Events are kept one to seven days on Standard, up to ninety on Premium, then they age out. The team wanted a year of readings for growth charts, so Priya turned on Capture. It writes the events in batches to Blob Storage or a data lake, as Avro files, with no code."),
            leo("And when spring comes and everyone buys a sensor?"),
            maya("Throughput units, slices of capacity, one megabyte a second in and two out each. Auto-inflate adds units when it's busy, but never shrinks back. And more partitions mean more readers side by side, though on Standard you choose the count when you create the hub."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. Two apps that must each read every event get a consumer group each. One device's events in order means a partition key. Archive every event with no code means Capture. And millions of events a second, telemetry or clickstreams, means Event Hubs. To replay yesterday, use retention and a new consumer group."),
            leo("And something to try today, so the ferns survive?"),
            maya("Create a Basic namespace and a hub with two partitions. It costs about a cent an hour. Send ten events with the az eventhubs command line, or a small console app. Read them with the Functions Event Hub trigger, or the processor sample. Then check Metrics for incoming and outgoing messages."),
            leo("Water the ferns, and give each app its own group. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    private val serviceBusCase = Podcast(
        "az-servicebus",
        listOf(
            maya("Welcome to Case Files. Today's case: The Ghost Bookings. A small gym has an Android app for booking classes. The app calls a backend on Azure, the backend drops a booking message onto Service Bus, and a worker confirms the spot and sends an email. On Saturday morning, some members get two confirmations for one spin class. And some bookings vanish completely."),
            leo("Two for some, none for others. Detective Leo on the case. First, what is Service Bus, in plain words?"),
            maya("Picture order tickets on a kitchen rail. The till puts tickets on, the cooks take them off, and each ticket goes to one cook. A message is a job to be done, like confirm this booking. Under the picture, Service Bus is a message broker: a service that stores each message safely on its side until a receiver says it has been dealt with."),
            leo("Okay. The vanished bookings first. Suspect one: Service Bus lost them."),
            maya("Service Bus doesn't lose messages it's holding. But look at the worker's settings. The receive mode is ReceiveAndDelete. Somebody picked it because it was faster in testing."),
            leo("[sigh] That sounds like it does exactly what it says."),
            maya("It does. The message is deleted the moment the worker takes it. On Saturday the worker crashed halfway through a few bookings, during a deploy. Those messages were already gone, with nothing left to retry. For work that can fail, use PeekLock, the default."),
            leo("How is PeekLock different, actually?"),
            maya("The broker hands the message over with a lock on it, and hides it from every other receiver while the lock lasts. It's still stored. The worker does the job and then settles it. If the worker crashes and never settles, the lock runs out, and the message appears again for the next try."),
            leo("Settles it how?"),
            maya("Four ways. Complete means done, remove it. Abandon means let go, so it's delivered again and its delivery count goes up by one. Dead-letter means move it aside, with a reason. And defer means keep it but skip it, to fetch later by its sequence number."),
            leo("Right. Now the double bookings. Suspect two: the member tapped Book twice."),
            maya("Good thought, but the logs show one message per booking. And even a repeat could be dropped by duplicate detection, which ignores a repeated message id within a time window. Look at the worker's timings instead. The confirmation step calls a slow payment service. On a busy Saturday it took forty seconds."),
            leo("And the lock lasts..."),
            maya("Thirty seconds by default, up to five minutes. After thirty seconds the lock ran out, while the first worker was still busy. So Service Bus handed the same message to a second worker. Two workers, one booking, two emails. Renew the lock during long work, or set a longer lock duration."),
            leo("[laugh] Two cooks, one spin class. Case closed?"),
            maya("Not yet. The waitlist. The booking messages go to a topic, not a queue. A topic is publish once, many get a copy. Each subscription is its own mailbox: one for confirmations, and one for the waitlist, which should only get bookings for full classes. Topics need the Standard tier or above."),
            leo("Let me guess. The waitlist got everything."),
            maya("Every message. The developer added a SQL filter rule, class full equals true, but left the default rule beside it. A new subscription starts with a rule that matches everything, and a message gets in if any rule matches. Delete the default rule. And for an exact match on a property, a correlation filter is cheaper."),
            leo("Any more clues in the file?"),
            maya("The dead-letter queue. Every queue and every subscription has one, a side pile for messages that could not be handled. The gym's had three hundred messages in it. Bookings for classes that had been cancelled. Nobody had ever looked."),
            leo("How did they get there?"),
            maya("The worker dead-lettered them, which was right: a booking for a cancelled class can never succeed. Abandoning would just bring it back until the max delivery count, ten by default, ran out. Completing would hide the failure. But somebody has to read that pile, or failures pile up silently."),
            leo("And strict order? A member books, then cancels a second later."),
            maya("Sessions. Give each message a session id, here the member's id, on a queue with sessions turned on. Messages with the same session id go to one receiver, strictly in order. So the cancel never overtakes the booking."),
            leo("What about the profile photos? I heard there was a thumbnail problem too."),
            maya("Different service. Thumbnails go through Queue Storage, a huge, cheap, simple queue in a storage account. The new worker read every message as nonsense. It expected Base64 encoded text, and the sender had sent plain text. Encode it when the receiver expects it."),
            leo("Does Queue Storage have PeekLock?"),
            maya("No. It has a visibility timeout: a message you take is hidden for that long. When you're done, you delete it with the pop receipt, a token proving you're the one holding it. And this is where Service Bus stops: a Service Bus queue holds up to eighty gigabytes, a storage queue far more. A stream of readings belongs in Event Hubs, which has its own episode."),
            leo("So, the case summary for the exam?"),
            maya("Four rules. If a message can never be processed, dead-letter it. If messages for one customer must stay in order, use sessions. If the queue must grow past eighty gigabytes, or you need an audit log of every operation, pick Storage queues. And if one message must reach several subscribers, use a topic with filtered subscriptions. PeekLock is always the safe default."),
            leo("Case closed. Anything to try today?"),
            maya("Create a Standard namespace, a topic and two subscriptions. Add a SQL filter rule to one, and delete its default rule. Send two messages with different properties from Service Bus Explorer in the portal. Then peek each subscription and the dead-letter queue, and see who got what."),
            leo("No more ghosts on my rail. See you next time."),
        ),
        style = PodcastStyle.CASE_FILES,
    )

    private val serviceBusGossip = Podcast(
        "az-servicebus",
        listOf(
            maya("Did you hear what Dana did to the dog walking app? Sit down, this one has a lot of barking."),
            leo("[chuckle] I heard one walker got every booking in town. What happened?"),
            maya("So, the team runs an Android app for booking dog walks. When an owner books, the backend on Azure sends one message to Service Bus. Under the picture, Service Bus is a message broker: it keeps each message safely on its side until a receiver says it has been handled. So the app carries on, even if a worker is down."),
            leo("And the message goes to a topic, I heard. What's a topic, in plain words?"),
            maya("Publish once, and every subscription gets its own copy. Each subscription is like its own mailbox. There was one for the walker in each part of town, and one for analytics. Topics need the Standard tier or above, and Premium adds dedicated capacity and much bigger messages."),
            leo("So what did Dana do?"),
            maya("Dana gave the north side subscription a SQL filter rule: area equals north. Perfect rule. But she left the default rule beside it. A new subscription starts with a rule that matches everything, and a message gets in if any rule matches. So the north side walker got every booking in town. Forty dogs, one walker."),
            leo("[laugh] Forty dogs. How did it get fixed?"),
            maya("Priya deleted the default rule. And she pointed out that for an exact match on a property, like the area, a correlation filter is cheaper than SQL. A SQL filter is flexible, with things like AND and greater than, but the broker works harder to check it."),
            leo("Okay, week two?"),
            maya("Omar. Omar's worker used ReceiveAndDelete, because it was quicker. The message is deleted the moment it's taken. Then the worker crashed during a deploy, and twelve bookings disappeared. It's faster, but there's no second chance. Twelve dogs, waiting by the door."),
            leo("[sigh] Oh no. So what should Omar have used?"),
            maya("PeekLock, the default. The broker hands the message over locked, not removed, and hides it from other receivers while the lock lasts. If the worker crashes and never answers, the lock runs out and the message comes back. Priya made Omar write the four settlements on a sticky note."),
            leo("Which are?"),
            maya("Complete: done, remove it. Abandon: let go, so it's delivered again and its delivery count goes up by one. Dead-letter: move it aside with a reason. And defer: keep it but skip it, to fetch later by its sequence number."),
            leo("Week three?"),
            maya("Lena. Lena's worker matched each booking to a walker, and on busy mornings that took a minute. The lock lasts thirty seconds by default, up to five minutes. So halfway through, the lock ran out, and the same booking went to a second worker. Two walkers turned up for one poodle."),
            leo("The poodle had a great day, though."),
            maya("It did. The fix is to renew the lock during long work, or set a longer lock duration. And for an owner who books and then changes the time a second later, Priya added sessions. Messages with the same session id, here the owner's id, go to one receiver, strictly in order. And duplicate detection drops a repeated message id within a time window, so a double send is harmless."),
            leo("And Marcus? I heard Marcus has a pile."),
            maya("Marcus has a pile. The dead-letter queue. Every queue and subscription has one, a side pile for messages that could not be handled. Bookings for walkers who had left were dead-lettered, which was right, since they could never succeed. But nobody read the pile. Two hundred owners never heard back."),
            leo("Why not just abandon them?"),
            maya("Abandon brings a message back, again and again, until the max delivery count, ten by default. Then it's dead-lettered anyway. Complete hides the failure. So dead-letter is right, and then you read the pile. Marcus now checks it every morning with his coffee, and the team set a lower max delivery count, so a hopeless message stops circling sooner."),
            leo("And the intern? There's always the intern."),
            maya("The intern built the photo thumbnails, on Queue Storage. That's a huge, cheap, simple queue inside a storage account. The receiver expected Base64 encoded text, the intern sent plain text, and every thumbnail job failed. Encode it when the receiver expects it."),
            leo("And Queue Storage? Does that work like Service Bus?"),
            maya("Simpler. A message you take is hidden for the visibility timeout. When you're done, you delete it with the pop receipt, the token that proves you're holding it. Messages are small, up to sixty-four kilobytes, but the queue can grow far past eighty gigabytes, which is where a Service Bus queue stops."),
            leo("Where else does Service Bus stop?"),
            maya("It's for jobs that must each be handled. A flood of readings, kept and replayed, is Event Hubs. A single this-happened note pushed to a handler is Event Grid. Both have their own episodes. And to run the worker without a server, the Azure Functions episode has the Service Bus trigger."),
            leo("So, the gossip I should remember for the exam?"),
            maya("Four things. A message that can never be processed gets dead-lettered. Messages for one customer that must stay in order mean sessions. A queue past eighty gigabytes, or an audit log of every operation, means Storage queues. One message to several subscribers means a topic. And duplicate detection, transactions or publish and subscribe all point to Service Bus."),
            leo("And something to try today, so I'm not next week's story?"),
            maya("Create a Standard namespace, a topic and two subscriptions. Add a SQL filter rule to one, and delete its default rule. Send two messages with different properties from Service Bus Explorer in the portal. Then peek each subscription and the dead-letter queue."),
            leo("One dog, one walker. See you next time."),
        ),
        style = PodcastStyle.GOSSIP,
    )

    val podcasts: List<Podcast> = listOf(eventHubsCase, eventHubsGossip, serviceBusCase, serviceBusGossip)

    private fun eventHubsCaseBoard() = PodcastBoard(
        "az-eventhubs--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Teleporting Bikes..."
            Scene(
                0, "The Teleporting Bikes",
                DiagramSpec(
                    "The bike app sends positions into Event Hubs; a live map and a billing job read them back.",
                    nodes = listOf(
                        DNode("phones", "Bike apps", 0.14f, 0.45f, w = 0.22f, h = 0.4f, kind = NodeKind.USER, tone = 1),
                        DNode("hub", "Event hub", 0.5f, 0.45f, w = 0.3f, h = 0.42f, kind = NodeKind.CYLINDER, sub = "kept for days"),
                        DNode("map", "Live map", 0.86f, 0.22f, w = 0.26f, h = 0.18f, tone = 1),
                        DNode("bill", "Billing", 0.86f, 0.68f, w = 0.26f, h = 0.18f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("phones", "hub", "pings"),
                        DEdge("hub", "map"),
                        DEdge("hub", "bill"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phones", "hub", "map", "bill"), light = setOf("map", "bill"), pulse = "phones" to "hub", card = BoardCard("The case", "Bikes jump backwards on the map. Billing charges half the rides.")),
                    Beat(2, light = setOf("hub"), card = BoardCard("Event Hubs", "A tape recorder that never stops. Reading doesn't remove anything.")),
                    Beat(4, light = setOf("hub"), card = BoardCard("Under the picture", "Append-only logs called partitions. Each event gets an offset.")),
                ),
            ),
            // 5: "Got it. So, suspect one: the phones..."
            Scene(
                5, "Suspect one: the phones",
                DiagramSpec(
                    "A hub is split into partitions; order holds only inside one, and the same partition key always lands in the same one.",
                    nodes = listOf(
                        DNode("hub", "Event hub", 0.2f, 0.5f, w = 0.32f, h = 0.42f, kind = NodeKind.CYLINDER),
                        DNode("p0", "Partition 0", 0.74f, 0.18f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                        DNode("p1", "Partition 1", 0.74f, 0.5f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                        DNode("p2", "Partition 2", 0.74f, 0.82f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("hub", "p0"),
                        DEdge("hub", "p1", "bike id"),
                        DEdge("hub", "p2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(5, show = setOf("hub"), card = BoardCard("Suspect one: wrong clocks", "Cleared. Each phone sends its pings in the right order.")),
                    Beat(6, show = setOf("p0", "p1", "p2"), light = setOf("p0", "p1", "p2"), card = BoardCard("Partition", "One lane: in order inside it, never across the whole hub.")),
                    Beat(8, light = setOf("p0", "p2"), card = BoardCard("No partition key", "One bike's pings spread over every lane and came out shuffled.")),
                    Beat(10, light = setOf("p1"), pulse = "hub" to "p1", card = BoardCard("Partition key", "The key is hashed: same key, same partition. One bike, one lane.")),
                ),
            ),
            // 11: "One mystery down. Now billing. Suspect two..."
            Scene(
                11, "Half the rides",
                DiagramSpec(
                    "The live map and billing each read the whole stream once each has a consumer group of its own.",
                    nodes = listOf(
                        DNode("hub", "Event hub", 0.2f, 0.4f, w = 0.32f, h = 0.42f, kind = NodeKind.CYLINDER),
                        DNode("map", "Live map", 0.78f, 0.18f, w = 0.4f, h = 0.22f, tone = 1, sub = "Default group"),
                        DNode("bill", "Billing", 0.78f, 0.62f, w = 0.4f, h = 0.22f, tone = 1, sub = "its own group"),
                    ),
                    edges = listOf(
                        DEdge("hub", "map"),
                        DEdge("hub", "bill"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("hub", "map", "bill"), light = setOf("bill"), card = BoardCard("Suspect two: slow billing", "Cleared. Both readers shared the Default consumer group.")),
                    Beat(14, light = setOf("map", "bill"), card = BoardCard("Consumer group", "Each app's own view of the stream. Sharing one splits the partitions.")),
                    Beat(16, light = setOf("bill"), pulse = "hub" to "bill", card = BoardCard("A consumer group each", "Readers are separated by consumer group, never by partition.")),
                ),
            ),
            // 17: "Case closed? No, wait, you've got that look."
            Scene(
                17, "The storage bill",
                DiagramSpec(
                    "A reader saves checkpoints in blob storage; EventProcessorClient or a Function trigger does it for you.",
                    nodes = listOf(
                        DNode("reader", "Billing job", 0.2f, 0.3f, w = 0.32f, h = 0.2f),
                        DNode("blob", "Blob storage", 0.78f, 0.3f, w = 0.36f, h = 0.34f, kind = NodeKind.CYLINDER, sub = "checkpoints"),
                        DNode("epc", "EventProcessorClient", 0.5f, 0.8f, w = 0.64f, h = 0.18f, kind = NodeKind.PILL),
                    ),
                    edges = listOf(
                        DEdge("reader", "blob", "checkpoint"),
                        DEdge("epc", "blob", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(18, show = setOf("reader", "blob"), light = setOf("blob"), card = BoardCard("A checkpoint every event", "Each one is a paid storage write. Thousands of bikes, thousands of writes.")),
                    Beat(20, pulse = "reader" to "blob", card = BoardCard("Checkpoint after a batch", "A restart rereads a few events, so make a repeat harmless.")),
                    Beat(22, show = setOf("epc"), light = setOf("epc"), card = BoardCard("EventProcessorClient", "Shares partitions across copies; keeps checkpoints in a blob container.")),
                ),
            ),
            // 23: "And when the summer rush comes, and every bike is out?"
            Scene(
                23, "The summer rush",
                DiagramSpec(
                    "Throughput units buy capacity and auto-inflate adds them; more partitions mean more readers at once.",
                    nodes = listOf(
                        DNode("tu", "Throughput unit", 0.26f, 0.3f, w = 0.46f, h = 0.26f, sub = "1 MB/s in, 2 out"),
                        DNode("parts", "Partitions", 0.76f, 0.3f, w = 0.44f, h = 0.26f, tone = 1, sub = "fixed on Standard"),
                        DNode("auto", "Auto-inflate: grows, never shrinks", 0.5f, 0.78f, w = 0.72f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("tu", "parts")),
                    Beat(24, show = setOf("auto"), light = setOf("tu", "auto"), card = BoardCard("Throughput unit", "One slice of capacity: 1 MB a second in, 2 out. Auto-inflate adds more.")),
                ),
            ),
            // 25: "Last thing. The owner wants every ride kept for years..."
            Scene(
                25, "Kept for years",
                DiagramSpec(
                    "Retention ends after days; Capture writes events in batches to Blob Storage or a data lake as Avro files.",
                    nodes = listOf(
                        DNode("hub", "Event hub", 0.2f, 0.4f, w = 0.32f, h = 0.42f, kind = NodeKind.CYLINDER, sub = "7 days, 90 Premium"),
                        DNode("store", "Blob or data lake", 0.76f, 0.4f, w = 0.4f, h = 0.42f, kind = NodeKind.CYLINDER, sub = "Avro files"),
                    ),
                    edges = listOf(
                        DEdge("hub", "store", "Capture"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(25, show = setOf("hub"), card = BoardCard("Retention", "1 to 7 days on Standard, up to 90 on Premium. Not years.")),
                    Beat(26, show = setOf("store"), light = setOf("store"), pulse = "hub" to "store", card = BoardCard("Capture", "Archive every event to storage as Avro files, with no code.")),
                ),
            ),
            // 27: "Then the new developer wants refunds on the same hub..."
            Scene(
                27, "Refunds on the hub?",
                DiagramSpec(
                    "Refunds that must each be handled go to Service Bus; Event Hubs streams, Event Grid pushes a note.",
                    nodes = listOf(
                        DNode("refund", "Refunds, one by one", 0.5f, 0.14f, w = 0.52f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("eh", "Event Hubs", 0.18f, 0.62f, w = 0.32f, h = 0.28f, tone = 1, sub = "a stream"),
                        DNode("eg", "Event Grid", 0.5f, 0.62f, w = 0.3f, h = 0.28f, tone = 1, sub = "a pushed note"),
                        DNode("sb", "Service Bus", 0.82f, 0.62f, w = 0.32f, h = 0.28f, sub = "each one handled"),
                    ),
                    edges = listOf(
                        DEdge("refund", "sb"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("refund"), light = setOf("refund"), card = BoardCard("Each refund handled once", "And failures set aside to look at later.")),
                    Beat(28, show = setOf("eh", "eg", "sb"), light = setOf("sb"), pulse = "refund" to "sb", card = BoardCard("That's Service Bus", "Event Hubs has no settling and no dead-letter queue.")),
                ),
            ),
            // 29: "So, the case summary for the exam?"
            Scene(
                29, "Case closed",
                DiagramSpec(
                    "The exam's Event Hubs questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("two", "Two apps, every event", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a consumer group each"),
                        DNode("order", "One device in order", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "partition key"),
                        DNode("arch", "Archive, no code", 0.26f, 0.45f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Capture"),
                        DNode("mill", "Millions a second", 0.76f, 0.45f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Event Hubs"),
                        DNode("replay", "Replay yesterday", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "retention and a new consumer group"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("two", "order", "arch", "mill", "replay"), light = setOf("two", "order"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(32, card = BoardCard("Today", "Basic namespace, 2 partitions, send 10 events, read them, check Metrics.")),
                ),
            ),
        ),
    )

    private fun eventHubsGossipBoard() = PodcastBoard(
        "az-eventhubs--gossip",
        listOf(
            // 0: "Okay, did you hear what Sam did with the plant app?..."
            Scene(
                0, "The plant app",
                DiagramSpec(
                    "Soil sensors send a reading a minute into Event Hubs; a live chart and an alert job read them.",
                    nodes = listOf(
                        DNode("sensors", "Soil sensors", 0.14f, 0.45f, w = 0.24f, h = 0.4f, kind = NodeKind.USER, tone = 1),
                        DNode("hub", "Event hub", 0.5f, 0.45f, w = 0.3f, h = 0.42f, kind = NodeKind.CYLINDER, sub = "kept for days"),
                        DNode("chart", "Live chart", 0.86f, 0.22f, w = 0.26f, h = 0.18f, tone = 1),
                        DNode("alerts", "Alerts", 0.86f, 0.68f, w = 0.26f, h = 0.18f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("sensors", "hub", "readings"),
                        DEdge("hub", "chart"),
                        DEdge("hub", "alerts"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("sensors", "hub")),
                    Beat(2, show = setOf("chart", "alerts"), pulse = "sensors" to "hub", card = BoardCard("The plant app", "Thousands of pots, a moisture reading a minute, into Event Hubs.")),
                    Beat(4, light = setOf("hub"), card = BoardCard("Event Hubs", "A tape recorder that never stops. Reading doesn't remove anything.")),
                ),
            ),
            // 5: "Okay. And Sam?"
            Scene(
                5, "What Sam did",
                DiagramSpec(
                    "The chart and the alerts each read the whole stream once each has a consumer group of its own.",
                    nodes = listOf(
                        DNode("hub", "Event hub", 0.2f, 0.4f, w = 0.32f, h = 0.42f, kind = NodeKind.CYLINDER),
                        DNode("chart", "Live chart", 0.78f, 0.18f, w = 0.4f, h = 0.22f, tone = 1, sub = "consumer group 1"),
                        DNode("alerts", "Alerts", 0.78f, 0.62f, w = 0.4f, h = 0.22f, tone = 1, sub = "consumer group 2"),
                    ),
                    edges = listOf(
                        DEdge("hub", "chart"),
                        DEdge("hub", "alerts"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(6, show = setOf("hub", "chart", "alerts"), light = setOf("chart", "alerts"), card = BoardCard("Sam's shared group", "Chart and alerts on Default: each saw half the plants.")),
                    Beat(8, light = setOf("alerts"), pulse = "hub" to "alerts", card = BoardCard("A consumer group each", "Inside one group, one reader per partition. Aisha's fix: a group each.")),
                    Beat(10, card = BoardCard("Up to twenty", "On Standard, up to 20 consumer groups per hub. No reason to share.")),
                ),
            ),
            // 11: "Okay, so what's a partition, really?"
            Scene(
                11, "Ben's zigzags",
                DiagramSpec(
                    "A hub is split into partitions; order holds only inside one, and the same partition key always lands in the same one.",
                    nodes = listOf(
                        DNode("hub", "Event hub", 0.2f, 0.5f, w = 0.32f, h = 0.42f, kind = NodeKind.CYLINDER),
                        DNode("p0", "Partition 0", 0.74f, 0.18f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                        DNode("p1", "Partition 1", 0.74f, 0.5f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                        DNode("p2", "Partition 2", 0.74f, 0.82f, w = 0.44f, kind = NodeKind.PILL, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("hub", "p0"),
                        DEdge("hub", "p1", "sensor id"),
                        DEdge("hub", "p2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("hub")),
                    Beat(12, show = setOf("p0", "p1", "p2"), light = setOf("p0", "p1", "p2"), card = BoardCard("Partition", "An append-only log, one lane. Each event gets an offset.")),
                    Beat(16, light = setOf("p0", "p2"), card = BoardCard("Ben's zigzags", "Order holds only inside a partition. No key, so readings shuffled.")),
                    Beat(18, light = setOf("p1"), pulse = "hub" to "p1", card = BoardCard("Partition key", "Hashed to a number: same key, same partition. One sensor, one lane.")),
                ),
            ),
            // 20: "Dana. Dana wrote the alert job's reading loop by hand..."
            Scene(
                20, "Dana's storage bill",
                DiagramSpec(
                    "A reader saves checkpoints in blob storage; EventProcessorClient or a Function trigger does it for you.",
                    nodes = listOf(
                        DNode("reader", "Alert job", 0.2f, 0.3f, w = 0.32f, h = 0.2f),
                        DNode("blob", "Blob storage", 0.78f, 0.3f, w = 0.36f, h = 0.34f, kind = NodeKind.CYLINDER, sub = "checkpoints"),
                        DNode("epc", "EventProcessorClient", 0.5f, 0.8f, w = 0.64f, h = 0.18f, kind = NodeKind.PILL),
                    ),
                    edges = listOf(
                        DEdge("reader", "blob", "checkpoint"),
                        DEdge("epc", "blob", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("reader", "blob")),
                    Beat(22, light = setOf("blob"), pulse = "reader" to "blob", card = BoardCard("Checkpoint", "A saved position in blob storage. Every reading meant a paid write.")),
                    Beat(24, show = setOf("epc"), light = setOf("epc"), card = BoardCard("After a batch", "Or let EventProcessorClient or a Function trigger keep them for you.")),
                ),
            ),
            // 25: "And the intern? There's always the intern."
            Scene(
                25, "The intern's orders",
                DiagramSpec(
                    "Orders that must each be handled go to Service Bus; Event Hubs streams, Event Grid pushes a note.",
                    nodes = listOf(
                        DNode("order", "Orders for new pots", 0.5f, 0.14f, w = 0.52f, h = 0.18f, kind = NodeKind.NOTE),
                        DNode("eh", "Event Hubs", 0.18f, 0.62f, w = 0.32f, h = 0.28f, tone = 1, sub = "a stream"),
                        DNode("eg", "Event Grid", 0.5f, 0.62f, w = 0.3f, h = 0.28f, tone = 1, sub = "a pushed note"),
                        DNode("sb", "Service Bus", 0.82f, 0.62f, w = 0.32f, h = 0.28f, sub = "each one handled"),
                    ),
                    edges = listOf(
                        DEdge("order", "sb"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("order"), light = setOf("order"), card = BoardCard("Orders on the hub?", "Each handled once, failed ones set aside: that's not a stream.")),
                    Beat(28, show = setOf("eh", "eg", "sb"), light = setOf("sb"), pulse = "order" to "sb", card = BoardCard("Service Bus", "Settle each message, dead-letter failures. Event Grid: a pushed note.")),
                ),
            ),
            // 29: "So where else does Event Hubs stop?"
            Scene(
                29, "A year of readings",
                DiagramSpec(
                    "Retention ends after days; Capture writes events in batches to Blob Storage or a data lake as Avro files.",
                    nodes = listOf(
                        DNode("hub", "Event hub", 0.2f, 0.4f, w = 0.32f, h = 0.42f, kind = NodeKind.CYLINDER, sub = "7 days, 90 Premium"),
                        DNode("store", "Blob or data lake", 0.76f, 0.4f, w = 0.4f, h = 0.42f, kind = NodeKind.CYLINDER, sub = "Avro files"),
                    ),
                    edges = listOf(
                        DEdge("hub", "store", "Capture"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(29, show = setOf("hub")),
                    Beat(30, show = setOf("store"), light = setOf("store"), pulse = "hub" to "store", card = BoardCard("Capture", "Retention ends after days. Capture keeps Avro files, with no code.")),
                ),
            ),
            // 31: "And when spring comes and everyone buys a sensor?"
            Scene(
                31, "The spring rush",
                DiagramSpec(
                    "Throughput units buy capacity and auto-inflate adds them; more partitions mean more readers at once.",
                    nodes = listOf(
                        DNode("tu", "Throughput unit", 0.26f, 0.3f, w = 0.46f, h = 0.26f, sub = "1 MB/s in, 2 out"),
                        DNode("parts", "Partitions", 0.76f, 0.3f, w = 0.44f, h = 0.26f, tone = 1, sub = "more readers"),
                        DNode("auto", "Auto-inflate: grows, never shrinks", 0.5f, 0.78f, w = 0.72f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(31, show = setOf("tu", "parts")),
                    Beat(32, show = setOf("auto"), light = setOf("tu", "auto"), card = BoardCard("Throughput unit", "1 MB a second in, 2 out. Auto-inflate grows, never shrinks.")),
                ),
            ),
            // 33: "So, the gossip I should remember for the exam?"
            Scene(
                33, "For the exam",
                DiagramSpec(
                    "The exam's Event Hubs questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("two", "Two apps, every event", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "a consumer group each"),
                        DNode("order", "One device in order", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "partition key"),
                        DNode("arch", "Archive, no code", 0.26f, 0.45f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Capture"),
                        DNode("mill", "Millions a second", 0.76f, 0.45f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "Event Hubs"),
                        DNode("replay", "Replay yesterday", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "retention and a new consumer group"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(34, show = setOf("two", "order", "arch", "mill", "replay"), light = setOf("two", "order"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(36, card = BoardCard("Today", "Basic namespace, 2 partitions, send 10 events, read them, check Metrics.")),
                ),
            ),
        ),
    )

    private fun serviceBusCaseBoard() = PodcastBoard(
        "az-servicebus--case-files",
        listOf(
            // 0: "Welcome to Case Files. Today's case: The Ghost Bookings..."
            Scene(
                0, "The Ghost Bookings",
                DiagramSpec(
                    "The gym app calls its backend, which puts a booking message on Service Bus for a worker to confirm.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.2f, 0.18f, w = 0.36f, h = 0.18f),
                        DNode("worker", "Worker", 0.78f, 0.18f, w = 0.32f, h = 0.2f, sub = "confirms, emails"),
                        DNode("api", "Backend", 0.2f, 0.66f, w = 0.32f, h = 0.2f),
                        DNode("bus", "Service Bus", 0.78f, 0.66f, w = 0.32f, h = 0.32f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "book"),
                        DEdge("api", "bus", "message"),
                        DEdge("bus", "worker"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api", "bus", "worker"), pulse = "api" to "bus", card = BoardCard("The case", "Saturday: two confirmations for some, none at all for others.")),
                    Beat(2, light = setOf("bus"), pulse = "bus" to "worker", card = BoardCard("A message broker", "Keeps each message safe until a receiver says it's handled.")),
                ),
            ),
            // 3: "Okay. The vanished bookings first. Suspect one..."
            Scene(
                3, "The vanished bookings",
                DiagramSpec(
                    "PeekLock locks a message instead of removing it; ReceiveAndDelete removes it at once and loses it on a crash.",
                    nodes = listOf(
                        DNode("q", "Queue", 0.2f, 0.32f, w = 0.3f, h = 0.34f, kind = NodeKind.CYLINDER),
                        DNode("w", "Worker", 0.78f, 0.32f, w = 0.32f, h = 0.2f),
                        DNode("pl", "PeekLock", 0.26f, 0.82f, w = 0.46f, h = 0.22f, kind = NodeKind.PILL, sub = "locked, not removed"),
                        DNode("rad", "ReceiveAndDelete", 0.75f, 0.82f, w = 0.46f, h = 0.22f, kind = NodeKind.PILL, tone = 2, sub = "gone if you crash"),
                    ),
                    edges = listOf(
                        DEdge("q", "w", "take"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(3, show = setOf("q", "w"), card = BoardCard("Suspect one: Service Bus", "Cleared. It doesn't lose the messages it's holding.")),
                    Beat(4, show = setOf("rad"), light = setOf("rad"), card = BoardCard("ReceiveAndDelete", "Deleted when taken. The worker crashed, and the bookings were gone.")),
                    Beat(6, show = setOf("pl"), light = setOf("pl"), pulse = "q" to "w", card = BoardCard("PeekLock", "The default: locked, not removed. A crash brings the message back.")),
                    Beat(8, light = setOf("pl", "w"), card = BoardCard("How the lock works", "Hidden from other receivers while locked; never settled, it comes back.")),
                ),
            ),
            // 9: "Settles it how?"
            Scene(
                9, "Settling a message",
                DiagramSpec(
                    "A locked message is settled in one of four ways: complete, abandon, dead-letter or defer.",
                    nodes = listOf(
                        DNode("c", "Complete", 0.26f, 0.25f, w = 0.44f, h = 0.26f, sub = "done, removed"),
                        DNode("a", "Abandon", 0.75f, 0.25f, w = 0.44f, h = 0.26f, tone = 1, sub = "comes back, count +1"),
                        DNode("d", "Dead-letter", 0.26f, 0.72f, w = 0.44f, h = 0.26f, tone = 1, sub = "moved aside"),
                        DNode("f", "Defer", 0.75f, 0.72f, w = 0.44f, h = 0.26f, tone = 1, sub = "skip, fetch later"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("c", "a", "d", "f"), light = setOf("c"), card = BoardCard("Four ways to settle", "Complete, abandon, dead-letter or defer.")),
                ),
            ),
            // 11: "Right. Now the double bookings. Suspect two..."
            Scene(
                11, "The double bookings",
                DiagramSpec(
                    "Work that outlasts the lock lets the same message reach a second worker; renew the lock or lengthen it.",
                    nodes = listOf(
                        DNode("q", "Queue", 0.2f, 0.4f, w = 0.3f, h = 0.36f, kind = NodeKind.CYLINDER),
                        DNode("w1", "Worker 1", 0.78f, 0.2f, w = 0.36f, h = 0.22f, sub = "still busy"),
                        DNode("w2", "Worker 2", 0.78f, 0.6f, w = 0.36f, h = 0.22f, tone = 2, sub = "same message"),
                        DNode("lock", "Lock: 30 s, up to 5 min", 0.5f, 0.88f, w = 0.6f, h = 0.16f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("q", "w1"),
                        DEdge("q", "w2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(11, show = setOf("q", "w1"), card = BoardCard("Suspect two: a double tap", "Cleared. One message per booking, and duplicate detection drops repeats.")),
                    Beat(12, light = setOf("w1"), pulse = "q" to "w1", card = BoardCard("Forty seconds", "A slow payment call made the work longer than the lock.")),
                    Beat(14, show = setOf("w2", "lock"), light = setOf("w2", "lock"), pulse = "q" to "w2", card = BoardCard("The lock ran out", "Same message, second worker. Renew the lock, or make it longer.")),
                ),
            ),
            // 16: "Not yet. The waitlist..."
            Scene(
                16, "The waitlist",
                DiagramSpec(
                    "Each subscription filters with rules; delete the default rule or it still lets every message in.",
                    nodes = listOf(
                        DNode("t", "Topic", 0.18f, 0.48f, w = 0.3f, h = 0.34f, kind = NodeKind.CYLINDER),
                        DNode("conf", "Confirmations", 0.74f, 0.16f, w = 0.46f, h = 0.24f, tone = 1, sub = "subscription"),
                        DNode("wait", "Waitlist", 0.74f, 0.5f, w = 0.46f, h = 0.24f, tone = 1, sub = "SQL: class full"),
                        DNode("def", "Default rule: matches all", 0.74f, 0.84f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("t", "conf"),
                        DEdge("t", "wait"),
                        DEdge("t", "def", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("t", "conf", "wait"), pulse = "t" to "conf", card = BoardCard("Topic", "Publish once; each subscription gets its own copy.")),
                    Beat(18, show = setOf("def"), light = setOf("def", "wait"), pulse = "t" to "wait", card = BoardCard("Delete the default rule", "Any matching rule lets a message in. Exact match? A correlation filter.")),
                ),
            ),
            // 19: "Any more clues in the file?"
            Scene(
                19, "The pile nobody read",
                DiagramSpec(
                    "A message that can never work is dead-lettered; one that fails too often lands there after ten tries.",
                    nodes = listOf(
                        DNode("q", "Queue", 0.2f, 0.28f, w = 0.3f, h = 0.34f, kind = NodeKind.CYLINDER),
                        DNode("w", "Worker", 0.78f, 0.28f, w = 0.32f, h = 0.2f),
                        DNode("dlq", "Dead-letter queue", 0.5f, 0.78f, w = 0.46f, h = 0.36f, kind = NodeKind.CYLINDER, sub = "a side pile"),
                    ),
                    edges = listOf(
                        DEdge("q", "w"),
                        DEdge("w", "dlq", "can never work"),
                        DEdge("q", "dlq", "after 10 tries", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("q", "w", "dlq"), light = setOf("dlq"), card = BoardCard("Dead-letter queue", "A side pile on every queue and subscription. 300 unread.")),
                    Beat(22, pulse = "w" to "dlq", card = BoardCard("Dead-letter the hopeless", "Abandon circles to the max delivery count; complete hides it. Read it!")),
                ),
            ),
            // 23: "And strict order? A member books, then cancels a second later."
            Scene(
                23, "Book, then cancel",
                DiagramSpec(
                    "Sessions keep messages with the same session id in order for one receiver.",
                    nodes = listOf(
                        DNode("q", "Session queue", 0.22f, 0.4f, w = 0.36f, h = 0.36f, kind = NodeKind.CYLINDER),
                        DNode("w", "One receiver", 0.78f, 0.4f, w = 0.36f, h = 0.24f, sub = "strictly in order"),
                    ),
                    edges = listOf(
                        DEdge("q", "w", "member id"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(23, show = setOf("q", "w")),
                    Beat(24, light = setOf("w"), pulse = "q" to "w", card = BoardCard("Session", "Same session id, one receiver, strict order. No cancel overtakes.")),
                ),
            ),
            // 25: "What about the profile photos?..."
            Scene(
                25, "The thumbnail problem",
                DiagramSpec(
                    "Storage queues grow far past Service Bus's 80 GB, with a visibility timeout and a pop receipt instead of PeekLock.",
                    nodes = listOf(
                        DNode("sbq", "Service Bus queue", 0.25f, 0.3f, w = 0.44f, h = 0.38f, kind = NodeKind.CYLINDER, tone = 1, sub = "up to 80 GB"),
                        DNode("stq", "Storage queue", 0.75f, 0.3f, w = 0.44f, h = 0.38f, kind = NodeKind.CYLINDER, sub = "far past 80 GB"),
                        DNode("vt", "Visibility timeout", 0.26f, 0.82f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "hidden while you work"),
                        DNode("pr", "Pop receipt", 0.76f, 0.82f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "proves you hold it"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(26, show = setOf("stq"), light = setOf("stq"), card = BoardCard("Base64", "The receiver expected Base64; the sender sent plain text.")),
                    Beat(28, show = setOf("sbq", "vt", "pr"), light = setOf("vt", "pr"), card = BoardCard("No PeekLock here", "Hidden for the visibility timeout; delete it with the pop receipt.")),
                ),
            ),
            // 29: "So, the case summary for the exam?"
            Scene(
                29, "Case closed",
                DiagramSpec(
                    "The exam's messaging questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("never", "Can never be processed", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "dead-letter"),
                        DNode("order", "One customer, in order", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "sessions"),
                        DNode("big", "Over 80 GB or audit log", 0.26f, 0.45f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Storage queue"),
                        DNode("many", "Several subscribers", 0.76f, 0.45f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a topic"),
                        DNode("safe", "PeekLock", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "always the safe default"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("never", "order", "big", "many", "safe"), light = setOf("never"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(32, card = BoardCard("Today", "A topic, two subscriptions, a SQL rule; send two messages, then peek them.")),
                ),
            ),
        ),
    )

    private fun serviceBusGossipBoard() = PodcastBoard(
        "az-servicebus--gossip",
        listOf(
            // 0: "Did you hear what Dana did to the dog walking app?..."
            Scene(
                0, "The dog walking app",
                DiagramSpec(
                    "The dog walking app calls its backend, which sends one booking message to Service Bus.",
                    nodes = listOf(
                        DNode("phone", "Android app", 0.2f, 0.2f, w = 0.36f, h = 0.18f),
                        DNode("api", "Backend", 0.2f, 0.7f, w = 0.32f, h = 0.2f),
                        DNode("bus", "Service Bus", 0.78f, 0.7f, w = 0.32f, h = 0.32f, kind = NodeKind.CYLINDER, sub = "a broker"),
                    ),
                    edges = listOf(
                        DEdge("phone", "api", "book"),
                        DEdge("api", "bus", "message"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(0, show = setOf("phone", "api"), pulse = "phone" to "api"),
                    Beat(2, show = setOf("bus"), light = setOf("bus"), pulse = "api" to "bus", card = BoardCard("A message broker", "Service Bus keeps each message until a receiver says it's handled.")),
                ),
            ),
            // 3: "And the message goes to a topic, I heard..."
            Scene(
                3, "Dana's forty dogs",
                DiagramSpec(
                    "Each subscription filters with rules; delete the default rule or it still lets every message in.",
                    nodes = listOf(
                        DNode("t", "Topic", 0.18f, 0.48f, w = 0.3f, h = 0.34f, kind = NodeKind.CYLINDER),
                        DNode("north", "North walker", 0.74f, 0.16f, w = 0.46f, h = 0.24f, tone = 1, sub = "SQL: area = north"),
                        DNode("stats", "Analytics", 0.74f, 0.5f, w = 0.46f, h = 0.24f, tone = 1, sub = "subscription"),
                        DNode("def", "Default rule: matches all", 0.74f, 0.84f, w = 0.46f, h = 0.22f, kind = NodeKind.NOTE, tone = 2),
                    ),
                    edges = listOf(
                        DEdge("t", "north"),
                        DEdge("t", "stats"),
                        DEdge("t", "def", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(4, show = setOf("t", "north", "stats"), pulse = "t" to "stats", card = BoardCard("Topic", "Publish once; every subscription gets its own copy. Standard or above.")),
                    Beat(6, show = setOf("def"), light = setOf("def", "north"), pulse = "t" to "north", card = BoardCard("The default rule stayed", "Any matching rule lets it in. North got every booking in town.")),
                    Beat(8, light = setOf("north"), card = BoardCard("Priya's fix", "Delete the default rule. Exact match on a property? Correlation filter.")),
                ),
            ),
            // 9: "Okay, week two?"
            Scene(
                9, "Omar's twelve dogs",
                DiagramSpec(
                    "PeekLock locks a message instead of removing it; ReceiveAndDelete removes it at once and loses it on a crash.",
                    nodes = listOf(
                        DNode("q", "Queue", 0.2f, 0.32f, w = 0.3f, h = 0.34f, kind = NodeKind.CYLINDER),
                        DNode("w", "Worker", 0.78f, 0.32f, w = 0.32f, h = 0.2f),
                        DNode("pl", "PeekLock", 0.26f, 0.82f, w = 0.46f, h = 0.22f, kind = NodeKind.PILL, sub = "locked, not removed"),
                        DNode("rad", "ReceiveAndDelete", 0.75f, 0.82f, w = 0.46f, h = 0.22f, kind = NodeKind.PILL, tone = 2, sub = "gone if you crash"),
                    ),
                    edges = listOf(
                        DEdge("q", "w", "take"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(10, show = setOf("q", "w", "rad"), light = setOf("rad"), card = BoardCard("ReceiveAndDelete", "Deleted when taken. A crash mid-deploy lost twelve bookings.")),
                    Beat(12, show = setOf("pl"), light = setOf("pl"), pulse = "q" to "w", card = BoardCard("PeekLock", "Locked, not removed, hidden from others. A crash brings it back.")),
                ),
            ),
            // 13: "Which are?"
            Scene(
                13, "The sticky note",
                DiagramSpec(
                    "A locked message is settled in one of four ways: complete, abandon, dead-letter or defer.",
                    nodes = listOf(
                        DNode("c", "Complete", 0.26f, 0.25f, w = 0.44f, h = 0.26f, sub = "done, removed"),
                        DNode("a", "Abandon", 0.75f, 0.25f, w = 0.44f, h = 0.26f, tone = 1, sub = "comes back, count +1"),
                        DNode("d", "Dead-letter", 0.26f, 0.72f, w = 0.44f, h = 0.26f, tone = 1, sub = "moved aside"),
                        DNode("f", "Defer", 0.75f, 0.72f, w = 0.44f, h = 0.26f, tone = 1, sub = "skip, fetch later"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(14, show = setOf("c", "a", "d", "f"), light = setOf("c"), card = BoardCard("Four ways to settle", "Complete, abandon, dead-letter or defer.")),
                ),
            ),
            // 15: "Week three?"
            Scene(
                15, "Lena's poodle",
                DiagramSpec(
                    "Work that outlasts the lock lets the same message reach a second worker; renew the lock or lengthen it.",
                    nodes = listOf(
                        DNode("q", "Queue", 0.2f, 0.4f, w = 0.3f, h = 0.36f, kind = NodeKind.CYLINDER),
                        DNode("w1", "Worker 1", 0.78f, 0.2f, w = 0.36f, h = 0.22f, sub = "still busy"),
                        DNode("w2", "Worker 2", 0.78f, 0.6f, w = 0.36f, h = 0.22f, tone = 2, sub = "same booking"),
                        DNode("lock", "Lock: 30 s, up to 5 min", 0.5f, 0.88f, w = 0.6f, h = 0.16f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("q", "w1"),
                        DEdge("q", "w2"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(16, show = setOf("q", "w1", "w2", "lock"), light = setOf("w2", "lock"), pulse = "q" to "w2", card = BoardCard("The lock ran out", "A minute of work, a 30 second lock: a second worker got it.")),
                    Beat(18, light = setOf("w1"), card = BoardCard("Renew the lock", "Or set a longer lock duration. Sessions keep one owner in order.")),
                ),
            ),
            // 19: "And Marcus? I heard Marcus has a pile."
            Scene(
                19, "Marcus's pile",
                DiagramSpec(
                    "A message that can never work is dead-lettered; one that fails too often lands there after ten tries.",
                    nodes = listOf(
                        DNode("q", "Queue", 0.2f, 0.28f, w = 0.3f, h = 0.34f, kind = NodeKind.CYLINDER),
                        DNode("w", "Worker", 0.78f, 0.28f, w = 0.32f, h = 0.2f),
                        DNode("dlq", "Dead-letter queue", 0.5f, 0.78f, w = 0.46f, h = 0.36f, kind = NodeKind.CYLINDER, sub = "a side pile"),
                    ),
                    edges = listOf(
                        DEdge("q", "w"),
                        DEdge("w", "dlq", "can never work"),
                        DEdge("q", "dlq", "after 10 tries", dashed = true),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(20, show = setOf("q", "w", "dlq"), light = setOf("dlq"), pulse = "w" to "dlq", card = BoardCard("Dead-letter queue", "Right for messages that can never work. But nobody read the pile.")),
                    Beat(22, pulse = "q" to "dlq", card = BoardCard("Max delivery count", "Ten tries by default, then dead-lettered anyway. Read the pile!")),
                ),
            ),
            // 23: "And the intern? There's always the intern."
            Scene(
                23, "The intern's thumbnails",
                DiagramSpec(
                    "Storage queues grow far past Service Bus's 80 GB, with a visibility timeout and a pop receipt instead of PeekLock.",
                    nodes = listOf(
                        DNode("sbq", "Service Bus queue", 0.25f, 0.3f, w = 0.44f, h = 0.38f, kind = NodeKind.CYLINDER, tone = 1, sub = "up to 80 GB"),
                        DNode("stq", "Storage queue", 0.75f, 0.3f, w = 0.44f, h = 0.38f, kind = NodeKind.CYLINDER, sub = "far past 80 GB"),
                        DNode("vt", "Visibility timeout", 0.26f, 0.82f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE, sub = "hidden while you work"),
                        DNode("pr", "Pop receipt", 0.76f, 0.82f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE, sub = "proves you hold it"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(24, show = setOf("stq"), light = setOf("stq"), card = BoardCard("Base64", "The receiver expected Base64; the intern sent plain text.")),
                    Beat(26, show = setOf("sbq", "vt", "pr"), light = setOf("vt", "pr"), card = BoardCard("No PeekLock here", "Hidden for the visibility timeout; delete it with the pop receipt.")),
                ),
            ),
            // 27: "Where else does Service Bus stop?"
            Scene(
                27, "Where it stops",
                DiagramSpec(
                    "Service Bus carries jobs that must be handled; a stream is Event Hubs, a pushed note is Event Grid.",
                    nodes = listOf(
                        DNode("sb", "Service Bus", 0.5f, 0.18f, w = 0.4f, h = 0.24f, sub = "jobs to handle"),
                        DNode("eh", "Event Hubs", 0.18f, 0.7f, w = 0.3f, h = 0.26f, tone = 1, sub = "a stream"),
                        DNode("eg", "Event Grid", 0.5f, 0.7f, w = 0.3f, h = 0.26f, tone = 1, sub = "a pushed note"),
                        DNode("fn", "Functions", 0.82f, 0.7f, w = 0.3f, h = 0.26f, tone = 1, sub = "a trigger"),
                    ),
                    edges = listOf(
                        DEdge("sb", "fn", "trigger"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(27, show = setOf("sb")),
                    Beat(28, show = setOf("eh", "eg", "fn"), light = setOf("eh", "eg"), pulse = "sb" to "fn", card = BoardCard("Not every job", "Readings in a flood: Event Hubs. A this-happened note: Event Grid.")),
                ),
            ),
            // 29: "So, the gossip I should remember for the exam?"
            Scene(
                29, "For the exam",
                DiagramSpec(
                    "The exam's messaging questions, each as the phrase to listen for and the answer it points to.",
                    nodes = listOf(
                        DNode("never", "Can never be processed", 0.26f, 0.13f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "dead-letter"),
                        DNode("order", "One customer, in order", 0.76f, 0.13f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "sessions"),
                        DNode("big", "Over 80 GB or audit log", 0.26f, 0.45f, w = 0.46f, h = 0.2f, kind = NodeKind.NOTE, sub = "Storage queue"),
                        DNode("many", "Several subscribers", 0.76f, 0.45f, w = 0.44f, h = 0.2f, kind = NodeKind.NOTE, sub = "a topic"),
                        DNode("sb", "Duplicates, FIFO, transactions", 0.5f, 0.82f, w = 0.7f, h = 0.2f, sub = "Service Bus"),
                    ),
                    aspect = 0.8f,
                ),
                beats = listOf(
                    Beat(30, show = setOf("never", "order", "big", "many", "sb"), light = setOf("sb"), card = BoardCard("Listen for the phrase", "Each exam phrase points straight to one answer.")),
                    Beat(32, card = BoardCard("Today", "A topic, two subscriptions, a SQL rule; send two messages, then peek them.")),
                ),
            ),
        ),
    )

    val boards: List<PodcastBoard> by lazy {
        listOf(eventHubsCaseBoard(), eventHubsGossipBoard(), serviceBusCaseBoard(), serviceBusGossipBoard())
    }
}
