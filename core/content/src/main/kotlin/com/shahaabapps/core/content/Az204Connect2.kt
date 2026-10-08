package com.shahaabapps.core.content

/** AZ-204 module 5, Connect and consume: Event Hubs, Service Bus and Queue Storage, plus the module test. */
internal object Az204Connect2 {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val eventHubs = Topic(
        id = "az-eventhubs", title = "Event Hubs: ingesting streams at scale", minutes = 24,
        references = listOf(
            B.ref("What is Azure Event Hubs?", LEARN + "azure/event-hubs/event-hubs-about"),
            B.ref("Features and terminology in Azure Event Hubs", LEARN + "azure/event-hubs/event-hubs-features"),
            B.ref("Balance partition load across multiple instances", LEARN + "azure/event-hubs/event-processor-balance-partition-load"),
            B.ref("Capture events through Azure Event Hubs", LEARN + "azure/event-hubs/event-hubs-capture-overview"),
            B.ref("Learn module: Explore Azure Event Hubs", LEARN + "training/modules/azure-event-hubs/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-eventhubs-w1", "az-eventhubs",
                "Two separate applications both need to read every event in a stream. What do you think each one needs of its own?",
                listOf("A consumer group", "A partition", "An event hub"), 0,
                "One of these is an independent view of the whole stream; the others are slices or copies.",
            ),
            B.plain("""
                Event Hubs takes in millions of small events a second from many producers, keeps them for a retention
                period, and lets consumers read them in order per partition, at their own pace, more than once. It
                is the Azure equivalent of Kafka, and it speaks the Kafka protocol too.
            """),
            B.diagram(
                caption = "Producers write to partitions; each consumer group reads every partition independently and keeps its own offset. Capture writes batches to storage.",
                nodes = listOf(
                    DNode("p", "producers", 0.1f, 0.5f, w = 0.16f, h = 0.3f, kind = NodeKind.USER, tone = 1, sub = "apps, devices"),
                    DNode("hub", "event hub", 0.42f, 0.5f, w = 0.3f, h = 0.5f, kind = NodeKind.CYLINDER, sub = "partitions 0..3, 7-day retention"),
                    DNode("cg1", "consumer group: realtime", 0.8f, 0.25f, w = 0.3f, h = 0.16f, tone = 2),
                    DNode("cg2", "consumer group: reports", 0.8f, 0.55f, w = 0.3f, h = 0.16f, tone = 2),
                    DNode("cap", "Capture → Blob (Avro)", 0.8f, 0.85f, w = 0.3f, h = 0.14f, kind = NodeKind.NOTE),
                ),
                edges = listOf(DEdge("p", "hub", label = "partition key"), DEdge("hub", "cg1"), DEdge("hub", "cg2"), DEdge("hub", "cap")),
                aspect = 0.7f,
            ),
            B.h("Vocabulary"),
            B.table(
                listOf("Term", "Meaning", "Note"),
                listOf("Namespace", "The container for hubs; has the FQDN and tier", "Basic, Standard, Premium, Dedicated"),
                listOf("Event hub", "One stream (a Kafka topic)", "1 to 32 partitions on Standard; fixed after creation except Premium and Dedicated"),
                listOf("Partition", "An ordered sequence of events", "Order is guaranteed only within a partition"),
                listOf("Partition key", "Hashes events with the same key to the same partition", "Keeps one device's events in order"),
                listOf("Consumer group", "A view of the hub with its own position", "Up to 20 on Standard; one per downstream app"),
                listOf("Offset / checkpoint", "A reader's position in a partition, saved to blob storage", "Resume after restart"),
                listOf("Throughput unit (TU)", "1 MB/s in, 2 MB/s out per unit", "Auto-inflate raises TUs under load"),
                listOf("Retention", "1 to 7 days Standard, up to 90 Premium", "Events are not deleted when read"),
                listOf("Capture", "Automatic batch export to Blob or Data Lake in Avro", "Set by time or size window"),
            ),
            B.remember("Reading does not remove events. Many consumer groups can read the same stream, and a consumer can rewind. That is the key difference from a queue."),
            B.check("az-eventhubs-k1", "az-eventhubs", "Two independent applications must each process every event in the stream. What do you create?",
                listOf("Two event hubs", "Two consumer groups", "Two partitions"), 1, "Each consumer group has its own offsets and sees all events."),
            B.h("Producing and consuming"),
            B.code("csharp", """
                // Producer
                var producer = new EventHubProducerClient("tt-events.servicebus.windows.net", "sessions", new DefaultAzureCredential());
                using var batch = await producer.CreateBatchAsync(new CreateBatchOptions { PartitionKey = "usaid" });
                batch.TryAdd(new EventData(JsonSerializer.SerializeToUtf8Bytes(new { owner = "usaid", minutes = 25 })));
                await producer.SendAsync(batch);

                // Consumer with checkpoints in blob storage (EventProcessorClient balances partitions across instances)
                var store = new BlobContainerClient(new Uri("https://ttstorage.blob.core.windows.net/checkpoints"), new DefaultAzureCredential());
                var processor = new EventProcessorClient(store, "reports", "tt-events.servicebus.windows.net", "sessions", new DefaultAzureCredential());
                processor.ProcessEventAsync += async args => {
                    Handle(args.Data);
                    await args.UpdateCheckpointAsync();
                };
                processor.ProcessErrorAsync += args => { _log.LogError(args.Exception, "partition {p}", args.PartitionId); return Task.CompletedTask; };
                await processor.StartProcessingAsync();
            """),
            B.bullets(
                "EventProcessorClient spreads partitions across running instances and stores ownership and checkpoints in a blob container.",
                "Checkpoint after a batch, not every event; checkpointing is a storage write.",
                "Azure Functions Event Hub trigger does all of this with a Storage connection and a cardinality of many.",
            ),
            B.check(
                "az-eventhubs-k2", "az-eventhubs",
                "Two applications must each read the whole stream, independently of the other. What do you give them?",
                listOf("A consumer group each", "A partition each", "A throughput unit each"), 0,
                "A consumer group is an independent view of the stream, with its own checkpoints.",
                whyWrong = mapOf(
                    1 to "A partition is a slice of the stream, so each application would see only part of it.",
                    2 to "Throughput units buy capacity. They do not separate one reader's position from another's.",
                ),
            ),
            B.h("Scaling and capture"),
            B.bullets(
                "Throughput: add throughput units (Standard) or processing units (Premium); enable auto-inflate to grow automatically (it never shrinks).",
                "Partitions: more partitions mean more parallel readers; one reader per partition per consumer group at most.",
                "Capture: a time window (1 to 15 minutes) or size window (10 to 500 MB) writes Avro files to storage with no code; pairs with Data Lake for analytics.",
            ),
            B.tip("\"Archive every event to storage with no code\" is Capture. \"A single device's events must stay ordered\" is a partition key. \"Replay yesterday's events\" is retention plus a new consumer group starting at an earlier offset."),
            B.compare("Event Hubs versus Event Grid versus Service Bus",
                B.card("Event Hubs", "Streams: telemetry, logs, clickstreams.", "Millions per second", "Retained, replayable, ordered per partition", "Consumer pulls"),
                B.card("Event Grid", "Discrete events: something happened.", "Push to handlers", "Filtered subscriptions", "Reactive, serverless"),
                B.card("Service Bus", "Messages and commands that must be processed.", "Queues and topics", "Sessions, transactions, dead-letter, ordering", "Enterprise messaging"),
            ),
            B.worked("""
                Worked example. Both partners' phones send a heartbeat every minute while a timer runs: owner, topic,
                seconds. These are low value individually and high volume. Event Hubs with partition key = owner keeps
                each person's stream ordered. The realtime consumer group feeds a live "studying now" badge; the
                reports group aggregates minutes per day; Capture archives everything to the data lake for later analysis.
            """),
            B.faded(
                "az-eventhubs-f1",
                "Same stream, a third reader. A fraud service must read every heartbeat without disturbing the realtime badge or the reports job.",
                listOf(
                    "The events are already partitioned by owner, and that stays as it is.",
                    "Give the new service its own ___, so it has its own position in the stream.",
                    "Its checkpoints go in its own blob container, so it can be reset without touching the others.",
                ),
                blank = 1,
                answers = listOf("consumer group"),
                note = "Readers are separated by consumer group, never by partition. A partition is only ever part of the stream.",
            ),
            B.steps("Create and send",
                "az eventhubs namespace create --name tt-events --resource-group tt-rg --sku Standard --enable-auto-inflate --maximum-throughput-units 4" to "A Standard namespace that grows to 4 TUs.",
                "az eventhubs eventhub create --name sessions --namespace-name tt-events --resource-group tt-rg --partition-count 4 --retention-time 168" to "Four partitions, 7-day retention.",
                "az eventhubs eventhub consumer-group create --eventhub-name sessions --namespace-name tt-events --resource-group tt-rg --name reports" to "A second consumer group beside \$Default.",
                "Grant the app Azure Event Hubs Data Sender or Data Receiver." to "Identity-based access instead of SAS keys.",
            ),
            B.mistakes(
                "Sharing one consumer group between two applications; they steal each other's partitions.",
                "Expecting global ordering; order holds only inside a partition.",
                "Checkpointing on every event and paying for a storage write each time.",
                "Choosing Event Hubs when each message needs individual handling with dead-lettering; that is Service Bus.",
            ),
            B.example("Two Tracks: Standard namespace, sessions hub with 4 partitions keyed by owner, two consumer groups, Capture to Data Lake every 5 minutes."),
            B.check(
                "az-eventhubs-k3", "az-eventhubs",
                "Events must also land in storage as Avro files, with no code written to do it. What do you enable?",
                listOf("Capture", "A checkpoint store", "Dead-lettering"), 0,
                "Capture batches events to Blob Storage or Data Lake in Avro automatically.",
                whyWrong = mapOf(
                    1 to "A checkpoint store records how far a reader has got. It archives nothing.",
                    2 to "Dead-lettering is a Service Bus and Event Grid idea. Event Hubs has no such sub-queue.",
                ),
            ),
            B.h("Check yourself"),
            B.gap(
                "az-n-hubs-gap1", "Archive every event to storage without writing any code.",
                "Turn on ___.", "Capture", "capture",
            ),
            B.gap(
                "az-n-hubs-gap2", "A single device's events must arrive in the order they were sent.",
                "Send them with a consistent ___ key.", "partition", "Partition",
            ),
            B.match(
                "az-n-hubs-match", "Pair each piece with what it does.",
                "Consumer group" to "An independent reader of the same stream",
                "Checkpoint" to "A stored position, written to a blob container",
                "Capture" to "Automatic archiving to storage, no code",
                "Retention" to "How far back a new reader can rewind",
            ),
            B.tryIt("Try it in 15 minutes", "A Basic namespace costs about a cent an hour.",
                "Create a Basic namespace and a hub with 2 partitions", "Send 10 events with the az eventhubs CLI or a console app", "Read them with the Functions Event Hub trigger or the processor sample", "Check Metrics for incoming and outgoing messages",
            ),
            B.explain(
                "az-eventhubs-x1",
                "Explain what a partition and a consumer group are, and how a reader resumes where it left off.",
                "A partition is an ordered log inside the hub and the unit of parallelism: order is guaranteed within one, not across them. A consumer group is an independent view of the whole stream, so two applications each get their own position rather than competing for one. That position is a checkpoint, written to blob storage, which is what lets a restarted reader carry on instead of starting again.",
                "A partition is an ordered log" to listOf("partition", "ordered", "order", "log"),
                "A consumer group is an independent view" to listOf("consumer group", "independent", "own view", "separately"),
                "Checkpoints in blob storage resume reading" to listOf("checkpoint", "checkpoints", "blob", "resume", "where it left"),
            ),
            B.glossary(
                "Partition" to "An ordered log inside a hub; the unit of parallelism.",
                "Consumer group" to "An independent view of the stream with its own checkpoints.",
                "Checkpoint" to "A saved reader position, stored in blob storage.",
                "Throughput unit" to "Capacity: 1 MB/s in, 2 MB/s out.",
                "Capture" to "Automatic batch export of events to storage in Avro.",
                "EventProcessorClient" to "The SDK class that reads all partitions and balances across instances.",
            ),
            B.recap(
                "Event Hubs ingests streams; events are retained and replayable, ordered per partition.",
                "Consumer groups give each app its own view; checkpoints in blob storage resume reading.",
                "Scale with throughput units and partitions; Capture archives with no code.",
            ),
        ),
    )

    private val serviceBus = Topic(
        id = "az-servicebus", title = "Service Bus and Queue Storage: messages that must be handled", minutes = 30,
        references = listOf(
            B.ref("What is Azure Service Bus?", LEARN + "azure/service-bus-messaging/service-bus-messaging-overview"),
            B.ref("Service Bus queues, topics, and subscriptions", LEARN + "azure/service-bus-messaging/service-bus-queues-topics-subscriptions"),
            B.ref("Message sessions", LEARN + "azure/service-bus-messaging/message-sessions"),
            B.ref("Dead-letter queues", LEARN + "azure/service-bus-messaging/service-bus-dead-letter-queues"),
            B.ref("Storage queues and Service Bus queues compared", LEARN + "azure/service-bus-messaging/service-bus-azure-and-service-bus-queues-compared-contrasted"),
            B.ref("Learn module: Discover Azure message queues", LEARN + "training/modules/discover-azure-message-queue/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-servicebus-w1", "az-servicebus",
                "A receiver takes a Service Bus message in PeekLock mode and cannot process it at all. What do you think it should do?",
                listOf("Dead-letter it", "Abandon it", "Complete it"), 0,
                "There are four ways to settle a locked message, and three of them are wrong here.",
            ),
            B.plain("""
                A message is a command or a piece of work: "send this email", "generate this certificate". It must
                be processed, usually once, and the sender wants to carry on without waiting. Azure has two queue
                services: Service Bus for enterprise features, Queue Storage for huge, simple queues.
            """),
            B.diagram(
                caption = "A queue is one-to-one. A topic fans out: every subscription gets its own copy, filtered by rules. Failed messages land in the dead-letter sub-queue.",
                nodes = listOf(
                    DNode("s", "sender", 0.1f, 0.5f, w = 0.16f, h = 0.24f, kind = NodeKind.USER, tone = 1),
                    DNode("t", "topic: lesson-events", 0.4f, 0.5f, w = 0.28f, h = 0.3f, kind = NodeKind.CYLINDER),
                    DNode("s1", "sub: certificates (filter: passed = true)", 0.8f, 0.2f, w = 0.36f, h = 0.16f, tone = 2),
                    DNode("s2", "sub: analytics (all)", 0.8f, 0.5f, w = 0.3f, h = 0.16f, tone = 2),
                    DNode("dlq", "\$DeadLetterQueue", 0.8f, 0.82f, w = 0.3f, h = 0.14f, kind = NodeKind.NOTE),
                ),
                edges = listOf(DEdge("s", "t", label = "send"), DEdge("t", "s1"), DEdge("t", "s2"), DEdge("s1", "dlq", label = "after max deliveries")),
                aspect = 0.9f,
            ),
            B.h("Service Bus concepts"),
            B.table(
                listOf("Concept", "Meaning", "Note"),
                listOf("Namespace", "Container with tier Basic, Standard or Premium", "Topics need Standard or above; Premium has dedicated capacity and larger messages"),
                listOf("Queue", "FIFO-ish store; one receiver takes each message", "Competing consumers"),
                listOf("Topic + subscription", "Publish once, each subscription gets a copy", "Rules filter: SQL filter, correlation filter, boolean"),
                listOf("Receive mode", "PeekLock (default) or ReceiveAndDelete", "PeekLock lets you complete, abandon, defer or dead-letter"),
                listOf("Lock duration", "How long a peeked message is invisible", "30 s default, up to 5 minutes; renew if needed"),
                listOf("Max delivery count", "Attempts before dead-lettering", "Default 10"),
                listOf("Dead-letter queue", "Sub-queue of every queue and subscription", "Read it at <entity>/\$DeadLetterQueue"),
                listOf("Sessions", "Group messages by SessionId for strict order and state", "Session-enabled queue; one receiver per session"),
                listOf("Duplicate detection", "Drops repeat MessageIds in a window", "Up to 7 days"),
                listOf("Scheduled messages", "Deliver at ScheduledEnqueueTime", "Delayed work"),
                listOf("Auto-forwarding", "Chain entities", "Topic subscription into a queue"),
                listOf("Transactions", "Group operations atomically", "Complete one and send another"),
            ),
            B.remember("PeekLock is the safe default: receive, process, then Complete. If you crash, the lock expires and the message reappears. ReceiveAndDelete is faster and loses messages on failure."),
            B.check("az-servicebus-k1", "az-servicebus", "Messages for the same order must be processed strictly in order by one consumer at a time. Which feature?",
                listOf("Duplicate detection", "Sessions with SessionId = order id", "Scheduled messages"), 1, "Sessions give per-key ordering and a single active receiver per session."),
            B.code("csharp", """
                await using var client = new ServiceBusClient("tt-bus.servicebus.windows.net", new DefaultAzureCredential());

                // Send
                var sender = client.CreateSender("lesson-events");
                await sender.SendMessageAsync(new ServiceBusMessage(BinaryData.FromObjectAsJson(new { owner = "usaid", topic = "az-apim" })) {
                    Subject = "QuizPassed", MessageId = attemptId, SessionId = "usaid",
                    ApplicationProperties = { ["passed"] = true }
                });

                // Receive with PeekLock
                var processor = client.CreateProcessor("lesson-events", "certificates", new ServiceBusProcessorOptions { MaxConcurrentCalls = 4, AutoCompleteMessages = false });
                processor.ProcessMessageAsync += async args => {
                    try { await _certs.Issue(args.Message.Body.ToObjectFromJson<Passed>()); await args.CompleteMessageAsync(args.Message); }
                    catch (TransientException) { await args.AbandonMessageAsync(args.Message); }
                    catch (Exception e) { await args.DeadLetterMessageAsync(args.Message, "unprocessable", e.Message); }
                };
                processor.ProcessErrorAsync += args => Task.CompletedTask;
                await processor.StartProcessingAsync();
            """),
            B.table(
                listOf("Settlement", "Effect"),
                listOf("Complete", "Remove the message; done"),
                listOf("Abandon", "Release the lock; redelivered, delivery count +1"),
                listOf("Dead-letter", "Move to the DLQ with a reason"),
                listOf("Defer", "Keep but skip; fetch later by sequence number"),
                listOf("Renew lock", "Extend the lock for long work"),
            ),
            B.check(
                "az-servicebus-k2", "az-servicebus",
                "A receiver holds a message in PeekLock and finds it can never be processed. Which settlement?",
                listOf("Dead-letter it", "Abandon it", "Complete it"), 0,
                "Dead-lettering moves it to the sub-queue for messages that could not be handled, where it can be looked at later.",
                whyWrong = mapOf(
                    1 to "Abandon releases the lock and the message comes round again, so one that can never be processed just circles until its delivery count runs out.",
                    2 to "Complete says it was handled successfully, which loses it silently.",
                ),
            ),
            B.h("Topic filters"),
            B.code("text", """
                SQL filter:          passed = true AND owner LIKE 'u%'
                Correlation filter:  Subject = 'QuizPassed'      (cheaper; matches system and user properties exactly)
                Boolean filter:      TrueFilter / FalseFilter
            """),
            B.h("Queue Storage: the simple one"),
            B.compare("Service Bus queue versus Storage queue",
                B.card("Service Bus queue", "Order, sessions, transactions, dead-letter, duplicate detection, topics.", "Message up to 256 KB (100 MB Premium)", "Queue up to 80 GB", "Long polling, AMQP, PeekLock settlement"),
                B.card("Storage queue", "Massive, cheap, simple.", "Message up to 64 KB", "Up to 500 TB in the account", "Visibility timeout, dequeue count, HTTP polling; audit log of all transactions"),
            ),
            B.code("csharp", """
                var queue = new QueueClient(new Uri("https://ttstorage.queue.core.windows.net/thumbnails"), new DefaultAzureCredential());
                await queue.CreateIfNotExistsAsync();
                await queue.SendMessageAsync(Convert.ToBase64String(Encoding.UTF8.GetBytes("usaid.jpg")));

                QueueMessage[] msgs = await queue.ReceiveMessagesAsync(maxMessages: 10, visibilityTimeout: TimeSpan.FromMinutes(2));
                foreach (var m in msgs) {
                    Process(m.Body.ToString());
                    await queue.DeleteMessageAsync(m.MessageId, m.PopReceipt);     // the pop receipt proves you hold it
                }
                var peek = await queue.PeekMessagesAsync(5);                       // look without hiding
            """),
            B.tip("\"Queue must exceed 80 GB\" or \"server-side audit of every operation\" is Storage queues. \"At-most-once / duplicate detection, FIFO guarantee, transactions, or publish-subscribe\" is Service Bus."),
            B.worked("""
                Worked example. When a quiz is passed, the API publishes one QuizPassed message to the lesson-events
                topic. The certificates subscription (correlation filter on Subject) generates a PDF; analytics takes
                everything. The certificate worker uses PeekLock, completes on success, abandons on a transient
                storage error, and dead-letters a malformed message. An operator inspects the DLQ in Service Bus Explorer.
                Thumbnails for profile photos go through a Storage queue because the work is trivial and volume is high.
            """),
            B.faded(
                "az-servicebus-f1",
                "Same worker, a worse message. It holds a message in PeekLock and finds the payload is malformed in a way no retry will fix.",
                listOf(
                    "Completing it would say the work succeeded, and lose it quietly.",
                    "It ___-letters the message instead, moving it to the sub-queue where an operator can look at it.",
                    "Abandoning it would release the lock and deliver it again until the delivery count ran out.",
                ),
                blank = 1,
                answers = listOf("dead"),
                note = "Complete, abandon, dead-letter, defer. Only one of the four is right for something that can never succeed.",
            ),
            B.steps("Create a topic with a filtered subscription",
                "az servicebus namespace create --name tt-bus --resource-group tt-rg --sku Standard" to "Topics need Standard.",
                "az servicebus topic create --name lesson-events --namespace-name tt-bus --resource-group tt-rg --enable-duplicate-detection true" to "Repeat MessageIds are dropped.",
                "az servicebus topic subscription create --name certificates --topic-name lesson-events --namespace-name tt-bus --resource-group tt-rg --max-delivery-count 5" to "Five tries then DLQ.",
                "az servicebus topic subscription rule create --name passed --subscription-name certificates --topic-name lesson-events --namespace-name tt-bus --resource-group tt-rg --filter-sql-expression \"passed = true\"" to "Replace the default TrueFilter.",
            ),
            B.mistakes(
                "Leaving the default TrueFilter rule in place beside your new rule; every message still matches.",
                "Using ReceiveAndDelete for work that can fail.",
                "Forgetting to Base64-encode Storage queue messages when the receiver expects it.",
                "Processing longer than the lock duration without renewing; the message is redelivered to another worker.",
                "Never reading the dead-letter queue; failures pile up silently.",
            ),
            B.example("Two Tracks: Standard Service Bus, lesson-events topic with certificates and analytics subscriptions, sessions keyed by owner, Storage queue for thumbnails."),
            B.check(
                "az-servicebus-k3", "az-servicebus",
                "A queue has to hold well over 80 GB of simple messages. Which service?",
                listOf("Queue Storage", "A Service Bus queue", "A Service Bus topic"), 0,
                "Storage queues are the choice for very large, simple queues. Service Bus caps a queue far lower, and earns it with sessions, transactions and dead-lettering.",
                whyWrong = mapOf(
                    1 to "A Service Bus queue gives ordering, sessions and dead-lettering, but not that capacity.",
                    2 to "A topic is for fan-out to several subscribers, and has the same size limits as the queue.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-n-sb-match", "Pair each way of settling a message with its effect.",
                "Complete" to "Remove the message: done",
                "Abandon" to "Release the lock; it comes back, delivery count up one",
                "Dead-letter" to "Move it to the DLQ with a reason",
                "Defer" to "Keep it but skip it, fetched later by sequence number",
            ),
            B.gap(
                "az-n-sb-gap1", "Receive, process, then settle, so a crash cannot lose the message.",
                "The safe default mode is ___.", "PeekLock", "peeklock", "Peek Lock",
            ),
            B.gap(
                "az-n-sb-gap2", "The question says the queue must be able to exceed 80 GB.",
                "That points at ___ queues, not Service Bus.", "Storage", "storage", "Queue Storage",
            ),
            B.tryIt("Try it in 15 minutes", "A Basic Service Bus namespace is about a cent per million operations; Standard has a base fee.",
                "Create a Standard namespace, a topic and two subscriptions", "Add a SQL filter rule to one and delete its default rule", "Send two messages with different properties from Service Bus Explorer in the portal", "Peek each subscription and the dead-letter queue",
            ),
            B.explain(
                "az-servicebus-x1",
                "Explain PeekLock and the four ways to settle a message, and when you would choose Queue Storage instead.",
                "In PeekLock the message is locked rather than removed, so the receiver decides what happened to it. Complete says it was handled and removes it. Abandon releases the lock so it comes round again. Dead-letter moves it aside for something that can never be processed. Defer sets it apart to be fetched later by sequence number. Queue Storage is the choice when the queue is simply enormous, over 80 GB, and none of Service Bus's sessions, transactions or dead-lettering are needed.",
                "The message is locked, not removed" to listOf("lock", "locked", "peeklock", "not removed"),
                "Complete, abandon, dead-letter, defer" to listOf("complete", "abandon", "dead-letter", "dead letter", "defer"),
                "Queue Storage for very large simple queues" to listOf("queue storage", "storage queue", "80 gb", "large", "huge", "simple"),
            ),
            B.glossary(
                "PeekLock" to "Receive mode where the message is locked until you settle it.",
                "Complete / Abandon / Dead-letter / Defer" to "The four ways to settle a locked message.",
                "Dead-letter queue" to "The sub-queue holding messages that could not be processed.",
                "Session" to "A group of messages with the same SessionId handled in order by one receiver.",
                "Correlation filter" to "A cheap exact-match subscription rule on properties.",
                "Pop receipt" to "The Storage queue token proving you hold a dequeued message.",
                "Visibility timeout" to "How long a dequeued Storage queue message stays hidden.",
            ),
            B.recap(
                "Service Bus queues for one-to-one work; topics and filtered subscriptions for fan-out.",
                "PeekLock then Complete, Abandon, Dead-letter or Defer; sessions for ordering; duplicate detection and scheduling built in.",
                "Storage queues for huge simple queues over 80 GB: send, receive with a visibility timeout, delete with the pop receipt.",
            ),
        ),
    )

    val topics = listOf(eventHubs, serviceBus)

    val quiz = Quiz(
        id = "az-connect-test", title = "Connect and consume module test",
        questions = B.spread(listOf(
            B.q("n1", "az-apim", "Callers may make 100 calls per minute; excess calls must get HTTP 429. Which policy?", listOf("quota", "rate-limit", "ip-filter", "check-header"), setOf(1), "rate-limit controls short windows and returns 429; quota is long-term and returns 403.",
                whyWrong = mapOf(
                    0 to "A quota is a long allowance and answers 403. Per minute is a burst, and the question asks for 429.",
                ),
            ),
            B.q("n2", "az-apim", "Each subscription is allowed 50,000 calls per month. Which policy?", listOf("rate-limit-by-key", "quota-by-key", "cache-lookup", "set-backend-service"), setOf(1), "A monthly allowance is a quota.",
                whyWrong = mapOf(
                    0 to "rate-limit-by-key is the same per-key idea over a short window. A month is an allowance, not a burst.",
                ),
            ),
            B.q("n3", "az-apim", "An API must only accept requests carrying a valid Entra ID token for audience api://tt. Which policy?", listOf("check-header", "validate-jwt", "authentication-basic", "cors"), setOf(1), "validate-jwt verifies the token signature, issuer and audience.",
                whyWrong = mapOf(
                    0 to "check-header only proves a header is present. It cannot check a signature.",
                    2 to "authentication-basic is how APIM authenticates to the back end, not how it checks the caller.",
                ),
            ),
            B.q("n4", "az-apim", "You added an operation-level policy and global policies stopped applying. What is missing?", listOf("<retry>", "<base />", "<cors>", "<return-response>"), setOf(1), "<base /> in each section runs the parent scope's policies.",
                whyWrong = mapOf(
                    3 to "return-response would stop the request there, which is the opposite of letting the parent scope run.",
                ),
            ),
            B.q("n5", "az-apim", "The gateway must run in your own datacenter while the control plane stays in Azure. Which feature?", listOf("Consumption tier", "Self-hosted gateway", "Developer portal", "Backend entity"), setOf(1), "The self-hosted gateway container runs anywhere; Premium (or Developer) tier.",
                whyWrong = mapOf(
                    0 to "Consumption is serverless APIM running in Azure, which is the part being moved out.",
                    3 to "A backend entity describes where APIM forwards to. The gateway is still in Azure.",
                ),
            ),
            B.q("n6", "az-apim", "A legacy back end returns XML but clients need JSON. Which outbound policy?", listOf("json-to-xml", "xml-to-json", "set-body only", "rewrite-uri"), setOf(1), "xml-to-json converts the response body on the way out.",
                whyWrong = mapOf(
                    0 to "json-to-xml goes the other way, and would be the inbound half of the opposite problem.",
                    2 to "set-body could do it with an expression, and you would be writing the converter by hand.",
                ),
            ),
            B.q("n7", "az-eventgrid", "A Function must run when a .jpg is uploaded to the photos container only. How do you filter?", listOf("In the Function code", "Subject begins-with the container path and ends-with .jpg", "Event type Microsoft.Storage.BlobDeleted", "A consumer group"), setOf(1), "Subject prefix and suffix filters on the subscription stop other events being delivered.",
                whyWrong = mapOf(
                    0 to "Filtering in code works, and you have already paid for the delivery and the invocation.",
                    2 to "BlobDeleted is the wrong event entirely. Nothing is uploaded by deleting.",
                    3 to "Consumer groups are an Event Hubs idea. Event Grid has no such thing.",
                ),
            ),
            B.q("n8", "az-eventgrid", "Your webhook never receives events after creating the subscription. What did it most likely fail to do?", listOf("Return 404", "Echo the validation code in the handshake", "Use HTTP instead of HTTPS", "Set a dead-letter destination"), setOf(1), "Event Grid requires the SubscriptionValidationEvent handshake before delivering.",
                whyWrong = mapOf(
                    2 to "HTTP is refused when the subscription is created, so it would not exist to deliver to.",
                    3 to "Dead-lettering decides where undelivered events are kept. It does not start delivery.",
                ),
            ),
            B.q("n9", "az-eventgrid", "Events that cannot be delivered after all retries must be kept. What do you configure?", listOf("Longer lock duration", "Dead-lettering to a storage container", "Capture", "Sessions"), setOf(1), "Event Grid dead-letters undeliverable events to blob storage.",
                whyWrong = mapOf(
                    0 to "Lock duration is a Service Bus idea. Event Grid pushes rather than locks.",
                    2 to "Capture is Event Hubs, and archives everything rather than the failures.",
                ),
            ),
            B.q("n10", "az-eventgrid", "Which delivery guarantee does Event Grid provide?", listOf("Exactly once", "At least once", "At most once", "None"), setOf(1), "Handlers should be idempotent because events may be delivered more than once.",
                whyWrong = mapOf(
                    0 to "Exactly once is what everyone wants and almost nothing offers, which is why idempotency is the handler's job.",
                    2 to "At most once would mean an event is dropped rather than retried.",
                ),
            ),
            B.q("n11", "az-eventhubs", "Millions of telemetry events per second must be ingested and replayed later by several teams. Which service?", listOf("Service Bus", "Event Grid", "Event Hubs", "Storage queue"), setOf(2), "Event Hubs is the streaming ingestion service with retention and consumer groups.",
                whyWrong = mapOf(
                    0 to "Service Bus is for messages that must each be handled, and removes them once they are. There is nothing left to replay.",
                    1 to "Event Grid pushes discrete notifications and keeps nothing to replay.",
                ),
            ),
            B.q("n12", "az-eventhubs", "Events from one device must stay in order. What do you set when sending?", listOf("MessageId", "A partition key equal to the device id", "A consumer group", "Capture"), setOf(1), "Same partition key means same partition, and order holds within a partition.",
                whyWrong = mapOf(
                    0 to "MessageId identifies one event. It does not decide which partition the event lands in.",
                    2 to "A consumer group is a reader's view. Ordering is decided when sending.",
                ),
            ),
            B.q("n13", "az-eventhubs", "Where does EventProcessorClient store checkpoints and ownership?", listOf("Cosmos DB", "A blob container", "Redis", "The event hub itself"), setOf(1), "Checkpoints live in Azure Blob Storage.",
                whyWrong = mapOf(
                    3 to "The hub holds the events, not the readers' positions. Keeping those apart is what lets each consumer group have its own.",
                ),
            ),
            B.q("n14", "az-eventhubs", "Every event must be archived to a data lake with no code. Which feature?", listOf("Consumer groups", "Capture", "Auto-inflate", "Sessions"), setOf(1), "Capture writes Avro batches to Blob or Data Lake automatically.",
                whyWrong = mapOf(
                    2 to "Auto-inflate raises throughput units when load climbs. It archives nothing.",
                ),
            ),
            B.q("n15", "az-eventhubs", "Two separate applications must read the full stream independently. What do you create for each?", listOf("A partition", "A consumer group", "A namespace", "A throughput unit"), setOf(1), "Consumer groups keep independent offsets.",
                whyWrong = mapOf(
                    0 to "A partition is a slice of the stream, so each application would see only part of it.",
                    2 to "A namespace is a separate hub, which would mean publishing every event twice.",
                ),
            ),
            B.q("n16", "az-servicebus", "A message must be processed exactly in order per customer by a single receiver at a time. Which Service Bus feature?", listOf("Duplicate detection", "Sessions", "Auto-forwarding", "Scheduled enqueue"), setOf(1), "Sessions group by SessionId with one active receiver per session.",
                whyWrong = mapOf(
                    0 to "Duplicate detection drops repeats of the same message. It says nothing about order.",
                ),
            ),
            B.q("n17", "az-servicebus", "A worker crashes while processing a message in PeekLock mode. What happens?", listOf("The message is lost", "The lock expires and the message is redelivered", "It is dead-lettered at once", "It is deferred"), setOf(1), "PeekLock keeps the message until Complete; an expired lock makes it visible again.",
                whyWrong = mapOf(
                    0 to "Losing it is what ReceiveAndDelete would do. PeekLock exists to prevent exactly that.",
                    2 to "Dead-lettering happens once the delivery count is exhausted, not on the first crash.",
                ),
            ),
            B.q("n18", "az-servicebus", "After five failed deliveries a message must be set aside for an operator. What do you set?", listOf("Lock duration 5", "Max delivery count 5 (then dead-letter)", "TTL 5 minutes", "Duplicate detection window 5"), setOf(1), "Exceeding max delivery count moves the message to the dead-letter queue.",
                whyWrong = mapOf(
                    0 to "Lock duration is how long one receiver holds it, measured in time, not how many attempts are allowed.",
                    2 to "A TTL expires the message by age, whether it was attempted once or never.",
                ),
            ),
            B.q("n19", "az-servicebus", "Several services must each receive a copy of every order message, with one of them only getting large orders. Which design?", listOf("One queue with competing consumers", "A topic with subscriptions and a SQL filter", "Event Hubs with Capture", "Storage queue with peek"), setOf(1), "Topics fan out; subscription rules filter.",
                whyWrong = mapOf(
                    0 to "Competing consumers share the work: each message goes to one of them, not to all of them.",
                ),
            ),
            B.q("n20", "az-servicebus", "The queue must hold more than 80 GB and a server-side log of every operation is required. Which service?", listOf("Service Bus Standard", "Service Bus Premium", "Azure Queue Storage", "Event Grid"), setOf(2), "Storage queues scale to the account limit and have storage analytics logging.",
                whyWrong = mapOf(
                    1 to "Premium raises the limits and adds VNet, and is still far short of 80 GB.",
                ),
            ),
            B.q("n21", "az-servicebus", "Which Storage queue token must you present to delete a message you received?", listOf("MessageId only", "The pop receipt", "A SAS", "The visibility timeout"), setOf(1), "DeleteMessage takes the message id and the pop receipt from the receive.",
                whyWrong = mapOf(
                    0 to "The id alone is not enough. The pop receipt is what proves you are the receiver currently holding it.",
                    3 to "The visibility timeout is how long it stays hidden, not the token that proves you hold it.",
                ),
            ),
            B.q("n22", "az-eventgrid", "Your application publishes its own LessonFinished events for other services to react to. Which Event Grid resource do you create?", listOf("A system topic", "A custom topic", "A consumer group", "A partner topic"), setOf(1), "Custom topics are for events your application publishes.",
                whyWrong = mapOf(
                    0 to "System topics are created by Azure for its own services' events.",
                    3 to "Partner topics are for a third-party SaaS publishing into Azure.",
                ),
            ),
        )),
    )
}
