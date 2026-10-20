package com.shahaabapps.core.content

/** AI-200 topics, written from the official study guide and Microsoft Learn (Ai200). */
internal object Ai200Connect {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val serviceBus = Topic(
        id = "ai-servicebus", title = "Service Bus for AI back ends: queues, topics and dead letters", minutes = 28,
        references = listOf(
            B.ref("What is Azure Service Bus?", LEARN + "azure/service-bus-messaging/service-bus-messaging-overview"),
            B.ref("Service Bus queues, topics and subscriptions", LEARN + "azure/service-bus-messaging/service-bus-queues-topics-subscriptions"),
            B.ref("Message transfers, locks and settlement", LEARN + "azure/service-bus-messaging/message-transfers-locks-settlement"),
            B.ref("Overview of Service Bus dead-letter queues", LEARN + "azure/service-bus-messaging/service-bus-dead-letter-queues"),
            B.ref("Send and receive messages from Service Bus queues (Python)", LEARN + "azure/service-bus-messaging/service-bus-python-how-to-use-queues"),
            B.ref("Topic filters and actions", LEARN + "azure/service-bus-messaging/topic-filters"),
            B.ref("Learn module: Discover Azure message queues", LEARN + "training/modules/discover-azure-message-queue/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-servicebus-w1", "ai-servicebus",
                "The Coach worker takes an embedding job off a queue and then crashes halfway through. What do you think happens to the job?",
                listOf("It reappears on the queue once a lock runs out", "It is gone for good", "Service Bus restarts the worker"), 0,
                "Think about what peeking at something and locking it means, as opposed to taking it away.",
            ),
            B.plain("""
                Service Bus is a message broker: a post office between the parts of your app. One part drops a message
                (a small piece of data, such as "embed note 42") into a queue, and another part picks it up when it is
                ready. The sender does not wait and does not even need the receiver to be running. That is exactly
                what slow AI work needs: the API answers the user at once and a worker creates the embeddings later.
            """),
            B.diagram(
                caption = "The API sends a job, the worker locks and processes it, and a message that keeps failing goes to the dead-letter queue to be inspected and resent.",
                nodes = listOf(
                    DNode("api", "Coach API", 0.15f, 0.3f, w = 0.26f, h = 0.22f, tone = 1, sub = "sender"),
                    DNode("queue", "embed-jobs queue", 0.5f, 0.3f, w = 0.32f, h = 0.26f, kind = NodeKind.CYLINDER),
                    DNode("worker", "Embedding worker", 0.85f, 0.3f, w = 0.28f, h = 0.22f, tone = 1, sub = "PeekLock receiver"),
                    DNode("dlq", "Dead-letter queue", 0.5f, 0.78f, w = 0.36f, h = 0.26f, kind = NodeKind.CYLINDER, tone = 2),
                    DNode("fix", "Repair script", 0.15f, 0.78f, w = 0.26f, h = 0.22f, tone = 2, sub = "fix and resend"),
                ),
                edges = listOf(
                    DEdge("api", "queue", label = "1 send"),
                    DEdge("queue", "worker", label = "2 lock"),
                    DEdge("worker", "dlq", label = "poison"),
                    DEdge("dlq", "fix", label = "inspect"),
                    DEdge("fix", "queue", label = "resend", dashed = true),
                ),
                aspect = 0.75f,
            ),
            B.h("Queues, topics and subscriptions"),
            B.p("""
                A queue is one line of messages with competing consumers: each message is handled by exactly one
                receiver, so adding workers shares the load. A topic is a queue with copies: every subscription on
                the topic gets its own copy of each message, so one "note saved" message can feed the embedding worker,
                the search indexer and the progress tracker separately. Topics need the Standard or Premium tier;
                Basic has queues only.
            """),
            B.table(
                listOf("Thing", "Plain words", "Coach example"),
                listOf("Queue", "One line, each message handled once", "embed-jobs: one worker embeds each note"),
                listOf("Topic", "A publish point that copies messages out", "note-events, published when a note changes"),
                listOf("Subscription", "A private queue on a topic, with its own filter rules", "search-index, progress, german-only"),
                listOf("Dead-letter queue", "A built-in sub-queue for messages that cannot be processed", "embed-jobs/${'$'}deadletterqueue"),
            ),
            B.check(
                "ai-servicebus-k1", "ai-servicebus",
                "Every saved note must reach the embedding worker and, separately, the progress tracker. What do you use?",
                listOf("A topic with two subscriptions", "One queue read by both services", "Two senders writing to the same queue"), 0,
                "A topic gives each subscription its own copy of every message, so both services see every note.",
                whyWrong = mapOf(
                    1 to "On a queue the two services compete: each message goes to one of them, never both.",
                    2 to "More senders put more messages in one line; each one is still read by only one receiver.",
                ),
            ),
            B.h("Sending and receiving in Python"),
            B.p("""
                The package is azure-servicebus. You create a ServiceBusClient with the namespace name and a
                credential; with DefaultAzureCredential the app needs the Azure Service Bus Data Sender or Data
                Receiver role instead of a connection string.
            """),
            B.code("python", """
                import json
                from azure.identity import DefaultAzureCredential
                from azure.servicebus import ServiceBusClient, ServiceBusMessage

                NS = "tt-coach-bus.servicebus.windows.net"

                with ServiceBusClient(NS, DefaultAzureCredential()) as client:
                    with client.get_queue_sender("embed-jobs") as sender:
                        sender.send_messages(ServiceBusMessage(
                            json.dumps({"noteId": "42"}),
                            content_type="application/json",
                            message_id="note-42-v3",
                            application_properties={"track": "german"},
                        ))

                    # PeekLock is the default receive mode
                    with client.get_queue_receiver("embed-jobs", max_wait_time=5) as receiver:
                        for msg in receiver:
                            try:
                                embed_note(json.loads(str(msg)))
                                receiver.complete_message(msg)       # done: delete it
                            except RateLimited:
                                receiver.abandon_message(msg)        # unlock: try again soon
                            except BadNote as e:
                                receiver.dead_letter_message(msg, reason="BadNote",
                                                             error_description=str(e))
            """),
            B.h("PeekLock, settlement and the lock"),
            B.compare("Which receive mode?",
                B.card("PeekLock (default)", "The work must not be lost if the worker dies.", "The message is locked, not removed", "You settle it: complete, abandon, dead-letter or defer", "If the lock expires it becomes visible again"),
                B.card("ReceiveAndDelete", "Losing the odd message is acceptable and speed matters.", "Removed the moment it is handed over", "No settlement calls", "A crash mid-work loses the message"),
            ),
            B.bullets(
                "complete_message: finished; the message is deleted.",
                "abandon_message: give the lock back so it can be delivered again; the delivery count goes up.",
                "dead_letter_message: move it to the dead-letter queue with a reason.",
                "defer_message: set it aside and fetch it later by sequence number with receive_deferred_messages.",
                "Lock duration is one minute by default (five at most). A long embedding call can renew the lock, for example with AutoLockRenewer, or the message reappears and is processed twice.",
            ),
            B.check(
                "ai-servicebus-k2", "ai-servicebus",
                "A worker processes each job in about three minutes and jobs are sometimes handled twice. Lock duration is the default. Why?",
                listOf("The lock expires before the work ends, so the message is delivered again", "ReceiveAndDelete is turned on", "The queue has no dead-letter queue"), 0,
                "The default lock is one minute. Renew the lock during long work, or raise the lock duration (up to five minutes).",
                whyWrong = mapOf(
                    1 to "ReceiveAndDelete removes the message at once, so it could be lost, never handled twice.",
                    2 to "Every queue has a dead-letter queue built in, and it has nothing to do with redelivery while the work is running.",
                ),
            ),
            B.h("Dead letters and reprocessing"),
            B.p("""
                Each queue and subscription has a dead-letter queue (DLQ). Service Bus moves a message there itself
                when its delivery count passes the max delivery count (10 by default), and optionally when it
                expires; your code can also dead-letter a message it knows it cannot handle, a "poison message". Nothing
                reads the DLQ for you: you open it with sub_queue=ServiceBusSubQueue.DEAD_LETTER, read the reason,
                fix the cause and send a fresh copy to the main queue.
            """),
            B.code("python", """
                from azure.servicebus import ServiceBusSubQueue

                with client.get_queue_receiver("embed-jobs", sub_queue=ServiceBusSubQueue.DEAD_LETTER) as dlq:
                    for msg in dlq.receive_messages(max_message_count=20, max_wait_time=5):
                        print(msg.dead_letter_reason, msg.delivery_count, str(msg))
            """),
            B.h("Filters on subscriptions, and sessions"),
            B.bullets(
                "Each subscription has rules. A SQL filter tests message properties: track = 'german'. A correlation filter matches exact values such as subject or correlation id and is cheaper.",
                "A new subscription has a default rule that accepts everything; replace it when you add your own filter.",
                "Sessions group messages by session_id and deliver each group in order to one receiver at a time: first in, first out per user.",
                "Duplicate detection (Standard and Premium) drops a resent message with the same message_id inside a time window.",
            ),
            B.tip("\"Each consumer needs every message\" means a topic. \"Must not lose the message if the worker crashes\" means PeekLock. \"Messages for one user in order\" means sessions. \"Keeps failing\" means the dead-letter queue."),
            B.check(
                "ai-servicebus-k3", "ai-servicebus",
                "A malformed job fails every time. With default settings, where does it end up?",
                listOf("In the dead-letter queue after 10 deliveries", "Deleted after the first failure", "Back at the front of the queue forever"), 0,
                "Each abandon or lost lock raises the delivery count; past the max delivery count (10 by default) Service Bus dead-letters it.",
                whyWrong = mapOf(
                    1 to "Service Bus never silently deletes a failed PeekLock message; it retries it first.",
                    2 to "The max delivery count is there precisely so a poison message cannot loop forever.",
                ),
            ),
            B.worked("""
                Worked example. A Coach user saves a German note. The FastAPI endpoint stores it and sends an
                embed-jobs message with message_id note-42-v3, then returns 202 at once. The worker receives it in
                PeekLock, calls the embedding model, writes the vector and completes the message. When the model
                answers 429, the worker abandons the message so it is tried again; when the note text is empty, it
                dead-letters it with reason EmptyNote, because no retry will ever fix that.
            """),
            B.faded(
                "ai-servicebus-f1",
                "Same worker, new problem. The embedding call times out now and then, but works on the next try.",
                listOf(
                    "The failure is temporary, so the message should not go to the dead-letter queue yet.",
                    "The worker should ___ the message, which releases the lock and lets it be delivered again.",
                    "If it keeps failing, the max delivery count moves it to the dead-letter queue anyway.",
                ),
                blank = 1,
                answers = listOf("abandon"),
                note = "Abandon for a temporary failure, dead-letter for one no retry can fix, complete when done.",
            ),
            B.steps("Build it with the az CLI",
                "az servicebus namespace create --name tt-coach-bus --resource-group tt-rg --sku Standard" to "Standard, so topics are available.",
                "az servicebus queue create --namespace-name tt-coach-bus --resource-group tt-rg --name embed-jobs --lock-duration PT2M --max-delivery-count 5" to "A queue with a two-minute lock and five tries before dead-lettering.",
                "az role assignment create --assignee <principalId> --role \"Azure Service Bus Data Sender\" --scope <queue-id>" to "The API may send, with no connection string.",
                "az servicebus topic create --namespace-name tt-coach-bus --resource-group tt-rg --name note-events" to "A topic for note changes.",
                "az servicebus topic subscription rule create --namespace-name tt-coach-bus --resource-group tt-rg --topic-name note-events --subscription-name german-only --name track-german --filter-sql-expression \"track = 'german'\"" to "After creating the subscription, a SQL rule on it; remove the default rule too.",
            ),
            B.mistakes(
                "Using ReceiveAndDelete for jobs that must not be lost, then losing one in a crash.",
                "Leaving the default lock on long embedding calls, so each job runs twice.",
                "Never reading the dead-letter queue; failed jobs pile up silently.",
                "Adding a SQL rule but leaving the ${'$'}Default rule, so the subscription still gets everything.",
                "Using one queue when two services each need every message; that needs a topic.",
            ),
            B.example("Two Tracks Coach: an embed-jobs queue with a two-minute lock, a note-events topic with search-index and progress subscriptions, and a small repair script that reads the dead-letter queue every morning."),
            B.h("Check yourself"),
            B.match(
                "ai-x-bus-match", "Pair each settlement call with what it does.",
                "complete_message" to "Finished; the message is deleted",
                "abandon_message" to "Releases the lock for another try",
                "dead_letter_message" to "Moves it to the dead-letter queue",
                "defer_message" to "Sets it aside to fetch later by sequence number",
            ),
            B.gap(
                "ai-x-bus-gap1", "The receive mode that locks a message instead of removing it.",
                "The default receive mode is ___.", "PeekLock", "peeklock", "peek lock",
            ),
            B.gap(
                "ai-x-bus-gap2", "Several services each need a copy of every message.",
                "Send to a ___ and give each service its own subscription.", "topic", "Topic",
            ),
            B.tryIt("Try it in 15 minutes", "A Standard namespace costs a few cents for an afternoon.",
                "Create a namespace and an embed-jobs queue", "Send three messages from Python with DefaultAzureCredential", "Receive them in PeekLock, complete two and dead-letter one", "Read the dead-letter queue and print the reason",
            ),
            B.explain(
                "ai-servicebus-x1",
                "Explain how a worker processes queued AI jobs without losing them, and what happens to a job that keeps failing.",
                "The worker receives in PeekLock mode, so the message is locked rather than removed. When the work succeeds it calls complete and the message is deleted; on a temporary failure it calls abandon so the message is delivered again. Each delivery raises the delivery count, and past the max delivery count Service Bus moves the message to the dead-letter queue, where someone reads the reason, fixes it and resends. When several services each need every message, a topic with one subscription each is used instead of a queue.",
                "PeekLock locks rather than deletes" to listOf("peeklock", "peek lock", "peek-lock", "locked"),
                "Complete when done, abandon to retry" to listOf("complete", "abandon"),
                "Dead-letter after the max delivery count" to listOf("dead-letter", "dead letter", "deadletter", "max delivery count"),
                "Topics with subscriptions for fan-out" to listOf("topic", "subscription"),
            ),
            B.glossary(
                "Queue" to "A line of messages where each one is handled by a single receiver.",
                "Topic" to "A publish point that copies each message to every subscription.",
                "PeekLock" to "Receive mode that locks a message until it is settled.",
                "Lock duration" to "How long a received message stays hidden; one minute by default.",
                "Max delivery count" to "Tries before a message is dead-lettered; 10 by default.",
                "Dead-letter queue" to "The sub-queue holding messages that could not be processed.",
                "Session" to "A group of messages with one session id, delivered in order.",
            ),
            B.recap(
                "Queues for one consumer per message, topics with subscriptions and filter rules for fan-out.",
                "PeekLock plus complete, abandon, dead-letter or defer; renew the lock for long work.",
                "Poison messages land in the dead-letter queue after the max delivery count; read it and resend.",
            ),
        ),
    )

    private val eventGrid = Topic(
        id = "ai-eventgrid", title = "Event Grid: custom events, filters and retries", minutes = 25,
        references = listOf(
            B.ref("What is Azure Event Grid?", LEARN + "azure/event-grid/overview"),
            B.ref("CloudEvents v1.0 schema with Event Grid", LEARN + "azure/event-grid/cloud-event-schema"),
            B.ref("Understand event filtering for Event Grid subscriptions", LEARN + "azure/event-grid/event-filtering"),
            B.ref("Event Grid message delivery and retry", LEARN + "azure/event-grid/delivery-and-retry"),
            B.ref("Webhook event delivery and endpoint validation", LEARN + "azure/event-grid/webhook-event-delivery"),
            B.ref("Route custom events to a web endpoint with the Azure CLI", LEARN + "azure/event-grid/custom-event-quickstart"),
            B.ref("Learn module: Explore Azure Event Grid", LEARN + "training/modules/azure-event-grid/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-eventgrid-w1", "ai-eventgrid",
                "Event Grid delivers an event, but your handler is down for an hour. What do you think happens?",
                listOf("Event Grid keeps retrying for up to a day", "The event is lost at once", "The publisher gets an error"), 0,
                "Event Grid promises delivery at least once, and that takes patience.",
            ),
            B.plain("""
                Event Grid is a notice board that pushes. Something happens, such as "a note was updated", and the
                source publishes a small event saying so. Event Grid looks at every subscription on that topic, keeps
                the ones whose filters match, and pushes the event to each handler: a function, a webhook, a Service
                Bus queue. A message in Service Bus is a job to do; an event is news that something happened.
            """),
            B.diagram(
                caption = "The API publishes CloudEvents to a custom topic; each subscription filters and pushes them on, and events that run out of retries are dead-lettered to blob storage.",
                nodes = listOf(
                    DNode("pub", "Coach API", 0.15f, 0.5f, w = 0.26f, h = 0.2f, tone = 1, sub = "publisher"),
                    DNode("topic", "Custom topic", 0.47f, 0.5f, w = 0.3f, h = 0.3f, kind = NodeKind.CLOUD),
                    DNode("fn", "Re-embed function", 0.83f, 0.18f, w = 0.32f, h = 0.2f, sub = "event type filter"),
                    DNode("q", "Service Bus queue", 0.83f, 0.5f, w = 0.32f, h = 0.2f, sub = "subject filter"),
                    DNode("dl", "Dead-letter blobs", 0.83f, 0.82f, w = 0.32f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2),
                ),
                edges = listOf(
                    DEdge("pub", "topic", label = "publish"),
                    DEdge("topic", "fn", label = "push"),
                    DEdge("topic", "q", label = "push"),
                    DEdge("topic", "dl", label = "retries out", dashed = true),
                ),
                aspect = 0.8f,
            ),
            B.h("Topics and the event schema"),
            B.table(
                listOf("Kind", "Who publishes", "Example"),
                listOf("System topic", "An Azure service, about its own resources", "Blob created in a storage account"),
                listOf("Custom topic", "Your own code, over HTTPS", "TwoTracks.Coach.NoteUpdated from the API"),
                listOf("Partner topic", "A third-party SaaS provider", "Events from an outside system"),
            ),
            B.p("""
                Events can use the Event Grid schema or CloudEvents 1.0, an open standard and the one to prefer for new
                work. A CloudEvent has an id, a source (who sent it), a type (what happened), an optional subject (what
                it happened to, written like a path), a time and the data. Choose the schema when you create the topic
                with --input-schema cloudeventschemav1_0. Event Grid also has namespaces with pull delivery and MQTT;
                here we stay with custom topics, which push.
            """),
            B.h("Publishing a custom event from Python"),
            B.code("python", """
                from azure.identity import DefaultAzureCredential
                from azure.eventgrid import EventGridPublisherClient
                from azure.core.messaging import CloudEvent

                client = EventGridPublisherClient(
                    "https://tt-coach-events.westeurope-1.eventgrid.azure.net/api/events",
                    DefaultAzureCredential(),          # needs the EventGrid Data Sender role
                )
                client.send([
                    CloudEvent(
                        source="/twotracks/coach",
                        type="TwoTracks.Coach.NoteUpdated",
                        subject="/notes/german/42",
                        data={"noteId": "42", "wordCount": 180},
                    )
                ])
            """),
            B.check(
                "ai-eventgrid-k1", "ai-eventgrid",
                "The Coach must react when a PDF is uploaded to its storage account. Which kind of topic carries that event?",
                listOf("A system topic for the storage account", "A custom topic the API publishes to", "A Service Bus topic"), 0,
                "Azure services publish their own events to system topics; you only subscribe.",
                whyWrong = mapOf(
                    1 to "A custom topic is for events your code invents. Storage already publishes BlobCreated itself.",
                    2 to "A Service Bus topic holds messages you send; Storage does not post there on its own.",
                ),
            ),
            B.h("Filters on a subscription"),
            B.bullets(
                "Event type filter: only the listed types, such as TwoTracks.Coach.NoteUpdated.",
                "Subject filter: subject begins with /notes/german/ or ends with .pdf. Design subjects as paths so subscribers can filter narrowly or broadly.",
                "Advanced filters: operators on fields, such as data.wordCount NumberGreaterThan 20 or data.track StringIn german, ielts. Values in one filter are OR; separate filters are AND. Up to 25 per subscription.",
            ),
            B.check(
                "ai-eventgrid-k2", "ai-eventgrid",
                "Only re-embed notes longer than 20 words. Which filter tests the word count in the event data?",
                listOf("An advanced filter, data.wordCount NumberGreaterThan 20", "A subject ends-with filter", "An event type filter"), 0,
                "Advanced filters compare fields inside the event, including the data, with operators.",
                whyWrong = mapOf(
                    1 to "A subject filter only looks at the start or end of the subject string, not at a number in the data.",
                    2 to "An event type filter picks which kinds of event, not which values they carry.",
                ),
            ),
            B.h("Delivery, retries and dead letters"),
            B.p("""
                Delivery is at least once, and order is not guaranteed. A handler acknowledges with a 200 to 204 status;
                anything else, or no answer in 30 seconds, is a failure. Event Grid then retries with a growing delay
                (10 seconds, 30 seconds, a minute, and on up to hours) until it reaches the retry policy limits: by
                default 30 attempts or 1,440 minutes (24 hours) of time to live, whichever comes first. Some errors,
                such as 400 Bad Request, are not retried at all.
            """),
            B.remember("Dead-lettering is off by default: when retries run out, the event is dropped. Point the subscription at a blob container and undelivered events are written there instead."),
            B.p("""
                Because an event can arrive twice, write idempotent handlers: doing the same event twice leaves the
                same result. Keep the ids of processed events, or make the work naturally repeatable, such as an
                upsert of the note's vector.
            """),
            B.h("Endpoint validation"),
            B.p("""
                Event Grid will not push to a webhook until the endpoint proves it wants the events. With the Event
                Grid schema it sends a SubscriptionValidationEvent and expects the validation code echoed back; with
                CloudEvents it sends an HTTP OPTIONS request with a WebHook-Request-Origin header and expects a
                WebHook-Allowed-Origin header in reply. Azure Functions with the Event Grid trigger, Service Bus and
                Event Hubs handlers do this for you.
            """),
            B.tip("\"Undelivered events must be kept\" means a dead-letter blob container. \"Only events for one container\" means a subject begins-with filter. \"Handler sees the same event twice\" means make it idempotent."),
            B.check(
                "ai-eventgrid-k3", "ai-eventgrid",
                "A handler sometimes receives the same NoteUpdated event twice. What is the right response?",
                listOf("Make the handler idempotent, for example by tracking event ids", "Open a support case; Event Grid lost an acknowledgement", "Switch to the Event Grid schema"), 0,
                "Delivery is at least once by design, so duplicates are normal and the handler must cope.",
                whyWrong = mapOf(
                    1 to "Duplicates are part of at-least-once delivery, not a fault in the service.",
                    2 to "The schema changes the shape of the event, not how many times it can arrive.",
                ),
            ),
            B.worked("""
                Worked example. The Coach API publishes TwoTracks.Coach.NoteUpdated CloudEvents with subjects like
                /notes/german/42. One subscription sends only NoteUpdated events to the re-embed function; a second,
                with subject begins with /notes/ielts/, sends IELTS notes to a Service Bus queue for the essay
                marker. Both have max delivery attempts of 10, a 2-hour time to live and a dead-letter container
                eg-deadletter. The function upserts the vector by note id, so a duplicate event changes nothing.
            """),
            B.faded(
                "ai-eventgrid-f1",
                "Same topic, a new subscriber. The pronunciation service wants only events about German notes.",
                listOf(
                    "Keep the event type filter so only NoteUpdated events arrive.",
                    "Add a ___ filter that begins with /notes/german/.",
                    "Set a dead-letter container so nothing is silently dropped.",
                ),
                blank = 1,
                answers = listOf("subject"),
                note = "Subjects written as paths let a subscriber pick a branch with begins-with, without an advanced filter.",
            ),
            B.steps("Create a topic and a filtered subscription",
                "az eventgrid topic create --name tt-coach-events --resource-group tt-rg --location westeurope --input-schema cloudeventschemav1_0" to "A custom topic that accepts CloudEvents.",
                "az role assignment create --assignee <principalId> --role \"EventGrid Data Sender\" --scope <topic-id>" to "The API may publish with its identity.",
                "az eventgrid event-subscription create --name reembed --source-resource-id <topic-id> --endpoint-type azurefunction --endpoint <function-resource-id> --event-delivery-schema cloudeventschemav1_0 --included-event-types TwoTracks.Coach.NoteUpdated --advanced-filter data.wordCount NumberGreaterThan 20" to "Only long NoteUpdated notes reach the function.",
                "az eventgrid event-subscription update --name reembed --source-resource-id <topic-id> --max-delivery-attempts 10 --event-ttl 120 --deadletter-endpoint <storage-id>/blobServices/default/containers/eg-deadletter" to "Custom retry policy and a dead-letter container.",
                "Run the Python publisher and open the function's logs" to "One invocation per matching event.",
            ),
            B.mistakes(
                "Assuming failed events are kept; without a dead-letter container they are dropped.",
                "Writing a handler that double-counts when the same event is delivered twice.",
                "Pointing a subscription at a webhook that never answers the validation handshake.",
                "Using Event Grid for jobs that must be worked through one by one; that is a Service Bus queue.",
                "Expecting events in the order they were published; Event Grid does not promise order.",
            ),
            B.example("Two Tracks Coach: a CloudEvents custom topic for note changes, a system topic on the uploads storage account, subject filters per track, and dead letters kept in eg-deadletter."),
            B.h("Check yourself"),
            B.match(
                "ai-x-egrid-match", "Pair each requirement with the Event Grid feature.",
                "Only NoteUpdated events" to "Event type filter",
                "Only notes under /notes/german/" to "Subject begins-with filter",
                "Only notes over 20 words" to "Advanced filter on data",
                "Keep events that could not be delivered" to "Dead-letter blob container",
            ),
            B.gap(
                "ai-x-egrid-gap1", "The open event format to prefer for new custom topics.",
                "Publish events in the ___ 1.0 schema.", "CloudEvents", "cloudevents", "CloudEvent",
            ),
            B.gap(
                "ai-x-egrid-gap2", "Delivery can repeat, so the handler must cope with duplicates.",
                "Event Grid delivers at least ___.", "once", "Once",
            ),
            B.tryIt("Try it in 15 minutes", "The first 100,000 operations a month are free.",
                "Create a custom topic with the CloudEvents input schema", "Subscribe a small function or webhook with an event type filter", "Publish two events from Python, one matching and one not", "Add a dead-letter container and check the subscription's delivery metrics",
            ),
            B.explain(
                "ai-eventgrid-x1",
                "Explain how an Event Grid subscription decides which events to deliver, and what happens when delivery fails.",
                "A subscription filters events by event type, by subject begins-with or ends-with, and by advanced filters on fields in the data. When the handler fails or does not answer, Event Grid retries with exponential backoff until the retry policy runs out, 30 attempts or 24 hours by default, and then dead-letters the event to a blob container if one is configured, or drops it. Delivery is at least once, so the handler must be idempotent and treat a duplicate as harmless.",
                "Filters by type, subject or advanced" to listOf("event type", "subject", "advanced filter"),
                "Retries then dead-letter to storage" to listOf("retry", "retries", "backoff", "dead-letter", "dead letter"),
                "At least once, so idempotent" to listOf("at least once", "at-least-once", "idempotent", "duplicate"),
            ),
            B.glossary(
                "System topic" to "A topic where an Azure service publishes events about its resources.",
                "Custom topic" to "A topic your own code publishes events to.",
                "CloudEvents" to "An open standard event format with id, source, type, subject and data.",
                "Advanced filter" to "A subscription filter that compares event fields with operators.",
                "Dead-lettering" to "Writing undelivered events to a blob container instead of dropping them.",
                "Idempotent" to "Safe to run twice: a repeat leaves the same result.",
            ),
            B.recap(
                "System topics for Azure's own events, custom topics for yours; prefer CloudEvents 1.0.",
                "Filter with event types, subject begins or ends with, and advanced filters on data.",
                "Retries up to 30 attempts or 24 hours, dead-letter to blob if set, at-least-once so be idempotent.",
            ),
        ),
    )

    private val functions = Topic(
        id = "ai-functions", title = "Azure Functions in Python: triggers, bindings and serverless APIs", minutes = 28,
        references = listOf(
            B.ref("Azure Functions Python developer guide", LEARN + "azure/azure-functions/functions-reference-python"),
            B.ref("Azure Functions triggers and bindings concepts", LEARN + "azure/azure-functions/functions-triggers-bindings"),
            B.ref("Azure Functions hosting options", LEARN + "azure/azure-functions/functions-scale"),
            B.ref("Azure Functions Flex Consumption plan hosting", LEARN + "azure/azure-functions/flex-consumption-plan"),
            B.ref("Develop Azure Functions locally using Core Tools", LEARN + "azure/azure-functions/functions-run-local"),
            B.ref("Azure Service Bus trigger for Azure Functions", LEARN + "azure/azure-functions/functions-bindings-service-bus-trigger"),
            B.ref("Learn module: Explore Azure Functions", LEARN + "training/modules/explore-azure-functions/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-functions-w1", "ai-functions",
                "A function must put a job on a Service Bus queue. How much Service Bus SDK code do you think it needs?",
                listOf("None; an output binding sends it", "A full ServiceBusClient with sender", "A REST call with a SAS token"), 0,
                "Functions has a word for connecting to other services declaratively.",
            ),
            B.plain("""
                Azure Functions runs small pieces of code when something happens, and you pay for the time they run.
                A trigger is the thing that starts a function: an HTTP request, a queue message, an event, a changed
                database item. Bindings are declared connections to other services, so data arrives as a parameter or
                leaves by setting one, with no SDK code. For the Coach, functions are the small jobs around the main API.
            """),
            B.diagram(
                caption = "Triggers start the functions declared in function_app.py; input and output bindings move data in and out without SDK code.",
                nodes = listOf(
                    DNode("http", "HTTP request", 0.17f, 0.15f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                    DNode("sb", "Service Bus msg", 0.5f, 0.15f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                    DNode("eg", "Event Grid event", 0.83f, 0.15f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL, tone = 1),
                    DNode("app", "function_app.py", 0.5f, 0.48f, w = 0.5f, h = 0.22f, sub = "app = func.FunctionApp()"),
                    DNode("out", "Queue output", 0.2f, 0.82f, w = 0.34f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2),
                    DNode("cosmos", "Cosmos DB input", 0.8f, 0.82f, w = 0.34f, h = 0.22f, kind = NodeKind.CYLINDER, tone = 2),
                ),
                edges = listOf(
                    DEdge("http", "app", label = "trigger"),
                    DEdge("sb", "app"),
                    DEdge("eg", "app"),
                    DEdge("app", "out", label = "output"),
                    DEdge("cosmos", "app", label = "input"),
                ),
                aspect = 0.85f,
            ),
            B.h("The Python v2 programming model"),
            B.p("""
                In the v2 model there is no function.json: you write functions in function_app.py and decorate them.
                app = func.FunctionApp() holds them all, a trigger decorator such as @app.route or
                @app.service_bus_queue_trigger says what starts each one, and binding decorators add inputs and outputs.
                Bigger apps split functions into blueprints and register them on the app.
            """),
            B.code("python", """
                import json, logging
                import azure.functions as func

                app = func.FunctionApp(http_auth_level=func.AuthLevel.FUNCTION)

                # A serverless API: POST /api/notes/42/embed queues a job and returns 202
                @app.route(route="notes/{noteId}/embed", methods=["POST"])
                @app.service_bus_queue_output(arg_name="jobs", queue_name="embed-jobs",
                                              connection="ServiceBusConnection")
                def queue_embed(req: func.HttpRequest, jobs: func.Out[str]) -> func.HttpResponse:
                    jobs.set(json.dumps({"noteId": req.route_params.get("noteId")}))
                    return func.HttpResponse(status_code=202)

                @app.service_bus_queue_trigger(arg_name="msg", queue_name="embed-jobs",
                                               connection="ServiceBusConnection")
                def embed_job(msg: func.ServiceBusMessage):
                    job = json.loads(msg.get_body().decode("utf-8"))
                    logging.info("embedding note %s", job["noteId"])

                @app.event_grid_trigger(arg_name="event")
                def on_upload(event: func.EventGridEvent):
                    logging.info("%s %s", event.event_type, event.subject)

                @app.cosmos_db_trigger(arg_name="docs", connection="CosmosConnection",
                                       database_name="coach", container_name="notes",
                                       lease_container_name="leases",
                                       create_lease_container_if_not_exists=True)
                def notes_changed(docs: func.DocumentList):
                    logging.info("%d notes changed", len(docs))
            """),
            B.table(
                listOf("Trigger", "Starts when", "Note"),
                listOf("HTTP", "A request arrives", "auth_level FUNCTION needs a key; ANONYMOUS does not"),
                listOf("Service Bus", "A message is on a queue or subscription", "Completed on success, abandoned on an exception"),
                listOf("Event Grid", "An event is pushed by a subscription", "The validation handshake is handled for you"),
                listOf("Cosmos DB", "Items are created or updated", "Uses the change feed and a lease container"),
            ),
            B.check(
                "ai-functions-k1", "ai-functions",
                "In the Python v2 model, where do you say that a function starts on a Service Bus message?",
                listOf("A decorator on the function in function_app.py", "A function.json file beside the code", "host.json"), 0,
                "The v2 model declares triggers and bindings with decorators in code; there is no function.json.",
                whyWrong = mapOf(
                    1 to "function.json belongs to the older v1 model.",
                    2 to "host.json holds settings for the whole app, such as extension options, not each function's trigger.",
                ),
            ),
            B.h("Connections and app settings"),
            B.p("""
                connection="ServiceBusConnection" is not a connection string: it names an app setting. With managed
                identity you set ServiceBusConnection__fullyQualifiedNamespace to the namespace name and grant the
                app's identity a Service Bus data role, and no secret is stored. Locally the same settings live in
                local.settings.json under Values; that file is for your machine only and is not deployed, so every
                setting has to exist in Azure as an app setting too.
            """),
            B.check(
                "ai-functions-k2", "ai-functions",
                "The function app works with func start but fails in Azure: the ServiceBusConnection setting is missing. Why?",
                listOf("local.settings.json is not published; the setting must be added as an app setting", "The decorator is ignored in Azure", "Functions cannot use Service Bus in Azure"), 0,
                "Core Tools publishes the code, not local.settings.json; set the value with az functionapp config appsettings set.",
                whyWrong = mapOf(
                    1 to "Decorators work the same locally and in Azure; the missing piece is the setting they point to.",
                    2 to "The Service Bus trigger is fully supported; it only needs its connection setting.",
                ),
            ),
            B.h("Hosting plans"),
            B.compare("Which plan?",
                B.card("Flex Consumption", "The default choice for new serverless apps.", "Linux, scales to zero and out fast per function", "Pay for executions; optional always-ready instances cut cold starts", "Virtual network support and a choice of instance memory"),
                B.card("Premium", "Always warm, with long runs.", "Pre-warmed instances, so no cold start", "Virtual network support", "Billed per instance, even when idle"),
                B.card("Dedicated", "You already pay for an App Service plan.", "Runs on App Service plan instances", "Predictable cost, no scale to zero", "Scale manually or with autoscale"),
            ),
            B.tip("\"Serverless, scale to zero, but inside a virtual network\" means Flex Consumption. \"No cold start at all, always warm\" means Premium. \"Reuse an existing App Service plan\" means Dedicated."),
            B.check(
                "ai-functions-k3", "ai-functions",
                "A new function app should scale to zero when idle but reach a database inside a virtual network. Which plan?",
                listOf("Flex Consumption", "Dedicated", "The legacy Consumption plan"), 0,
                "Flex Consumption is the recommended serverless plan and adds virtual network integration.",
                whyWrong = mapOf(
                    1 to "A Dedicated plan keeps instances running and never scales to zero.",
                    2 to "The older Consumption plan has no virtual network integration.",
                ),
            ),
            B.worked("""
                Worked example. The Coach has a function app on Flex Consumption. POST /api/notes/{noteId}/embed is an
                HTTP-triggered function that puts a job on embed-jobs through a Service Bus output binding and returns
                202. A Service Bus-triggered function does the embedding; if it raises, the message is abandoned and
                retried until it is dead-lettered. The connection is identity-based: the app setting
                ServiceBusConnection__fullyQualifiedNamespace names the namespace, and the app's identity holds the
                Azure Service Bus Data Sender and Data Receiver roles, nothing broader.
            """),
            B.faded(
                "ai-functions-f1",
                "Same app, a new job. After embedding, the worker must save a summary document to Cosmos DB, again with no SDK code.",
                listOf(
                    "Keep the Service Bus trigger that starts the function.",
                    "Add a Cosmos DB ___ binding with @app.cosmos_db_output and a func.Out parameter.",
                    "Call set on that parameter with the document; the runtime writes it when the function ends.",
                ),
                blank = 1,
                answers = listOf("output"),
                note = "Data leaving a function is an output binding; data read into it is an input binding; what starts it is the trigger.",
            ),
            B.steps("Build, run and deploy",
                "func init coach-fn --worker-runtime python" to "A v2 project with function_app.py, host.json and local.settings.json.",
                "func start" to "Runs locally and lists each function and its URL.",
                "az functionapp create --resource-group tt-rg --name tt-coach-fn --storage-account ttcoachfn --flexconsumption-location westeurope --runtime python --runtime-version 3.12" to "A Flex Consumption function app.",
                "az functionapp config appsettings set --name tt-coach-fn --resource-group tt-rg --settings ServiceBusConnection__fullyQualifiedNamespace=tt-coach-bus.servicebus.windows.net" to "The setting the decorators point to.",
                "func azure functionapp publish tt-coach-fn" to "Builds and deploys the code; local.settings.json stays behind.",
            ),
            B.mistakes(
                "Expecting local.settings.json to be deployed and then missing every setting in Azure.",
                "Putting a connection string in connection= instead of the name of an app setting.",
                "Writing SDK code to send a message when an output binding would do it in one line.",
                "Catching every exception in a Service Bus function, so failed messages are completed and lost.",
            ),
            B.example("Two Tracks Coach: a Flex Consumption app with an HTTP embed endpoint, a Service Bus worker, an Event Grid function for uploads and a Cosmos DB trigger that refreshes progress, all with identity-based connections."),
            B.h("Check yourself"),
            B.match(
                "ai-x-func-match", "Pair each decorator with what it declares.",
                "@app.route" to "An HTTP-triggered serverless API",
                "@app.service_bus_queue_trigger" to "Start on a queue message",
                "@app.service_bus_queue_output" to "Send a message without SDK code",
                "@app.cosmos_db_trigger" to "Start on new or changed items",
            ),
            B.gap(
                "ai-x-func-gap1", "Deploy a function project from your machine with Core Tools.",
                "Run func azure functionapp ___ tt-coach-fn.", "publish", "Publish",
            ),
            B.gap(
                "ai-x-func-gap2", "The recommended serverless hosting plan.",
                "Host new serverless apps on the ___ Consumption plan.", "Flex", "flex",
            ),
            B.tryIt("Try it in 15 minutes", "Flex Consumption has a monthly free grant, so this costs nothing.",
                "Run func init and add an HTTP function with @app.route", "Call it locally after func start", "Create a Flex Consumption app and publish to it", "Add an app setting in Azure and read it with os.environ",
            ),
            B.explain(
                "ai-functions-x1",
                "Explain how a Python function app declares what starts its functions and what they connect to, and how its settings get to Azure.",
                "In the Python v2 model each function in function_app.py has a decorator for its trigger, such as an HTTP route or a Service Bus queue trigger, and further decorators for input and output bindings, so data arrives as a parameter and leaves through func.Out with no SDK code. A binding's connection names an app setting, which can be identity-based. local.settings.json only serves local runs and is not published, so each value is set as an app setting on the function app, usually on the Flex Consumption plan.",
                "Decorators declare the trigger" to listOf("decorator", "function_app", "trigger"),
                "Bindings move data without SDK code" to listOf("binding", "func out"),
                "local.settings.json is not deployed; use app settings" to listOf("local settings json", "app setting", "not published"),
                "A hosting plan such as Flex Consumption" to listOf("flex consumption", "premium", "dedicated", "hosting plan"),
            ),
            B.glossary(
                "Trigger" to "What starts a function: a request, a message, an event or a change.",
                "Binding" to "A declared connection that moves data in or out without SDK code.",
                "v2 programming model" to "Python functions declared with decorators in function_app.py.",
                "Flex Consumption" to "The recommended serverless plan: scale to zero, VNet, per-function scaling.",
                "local.settings.json" to "Local-only settings file; it is not deployed.",
                "Lease container" to "A Cosmos DB container where the trigger tracks change feed progress.",
            ),
            B.recap(
                "Decorators in function_app.py declare triggers (HTTP, Service Bus, Event Grid, Cosmos DB) and bindings.",
                "Connections name app settings, ideally identity-based; local.settings.json is never deployed.",
                "Flex Consumption for serverless, Premium for always warm, Dedicated on an App Service plan; publish with func.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(serviceBus, eventGrid, functions)

    val quiz: Quiz = Quiz(
        id = "ai-connect-test", title = "Connect and consume module test",
        questions = B.spread(listOf(
            B.q("ai-n1", "ai-servicebus", "Every note change must reach three independent services, each processing all of them. What do you use?",
                listOf("One queue with three receivers", "A Service Bus topic with three subscriptions", "Three senders to one queue", "Sessions on one queue"), setOf(1),
                "A topic copies each message to every subscription, so each service gets every change.",
                whyWrong = mapOf(
                    0 to "Receivers on one queue compete, so each message reaches only one of the three.",
                    3 to "Sessions keep a group of messages in order for one receiver; they do not copy messages.",
                ),
            ),
            B.q("ai-n2", "ai-servicebus", "Embedding jobs must not be lost if the worker crashes mid-job. Which receive mode?",
                listOf("ReceiveAndDelete", "PeekLock", "Deferred", "Batch"), setOf(1),
                "PeekLock locks the message until it is settled; if the worker dies, the lock expires and the job reappears.",
                whyWrong = mapOf(
                    0 to "ReceiveAndDelete removes the message as soon as it is handed over, so a crash loses it.",
                    2 to "Deferring is a settlement choice after receiving, not a receive mode.",
                ),
            ),
            B.q("ai-n3", "ai-servicebus", "A message has failed delivery 10 times on a queue with default settings. Where is it now?",
                listOf("Deleted", "In the queue's dead-letter queue", "In a new queue Service Bus creates", "Back with the sender"), setOf(1),
                "Past the max delivery count, 10 by default, Service Bus moves the message to the dead-letter queue.",
                whyWrong = mapOf(
                    0 to "Service Bus keeps failed messages in the dead-letter queue rather than deleting them.",
                    3 to "Messages never travel back to a sender; the sender has already moved on.",
                ),
            ),
            B.q("ai-n4", "ai-servicebus", "Jobs take three minutes and some are processed twice. The queue uses the default lock. What fixes it?",
                listOf("Switch to ReceiveAndDelete", "Renew the lock during processing or raise the lock duration", "Lower the max delivery count", "Enable sessions"), setOf(1),
                "The one-minute lock runs out mid-job and the message is delivered again; keep the lock alive or lengthen it (up to five minutes).",
                whyWrong = mapOf(
                    0 to "That stops the duplicates by risking losing jobs instead, which is worse.",
                    2 to "A lower count dead-letters jobs sooner; it does not stop the lock expiring.",
                ),
            ),
            B.q("ai-n5", "ai-servicebus", "How does Python code read the dead-letter queue of embed-jobs?",
                listOf("get_queue_receiver(\"embed-jobs\", sub_queue=ServiceBusSubQueue.DEAD_LETTER)", "get_queue_receiver(\"embed-jobs-dlq\")", "receive_deferred_messages()", "peek_messages() on the sender"), setOf(0),
                "The dead-letter queue is a sub-queue of the main queue, opened with the sub_queue argument.",
                whyWrong = mapOf(
                    1 to "There is no separate queue named with a suffix; the dead-letter queue lives under the main queue.",
                    2 to "Deferred messages are set aside in the main queue and fetched by sequence number, not dead-lettered.",
                ),
            ),
            B.q("ai-n6", "ai-servicebus", "A subscription should receive only messages whose track property is german. What do you add?",
                listOf("A SQL filter rule on the subscription, replacing the default rule", "A second topic", "Duplicate detection", "A longer lock duration"), setOf(0),
                "Subscription rules filter on message properties; the default rule accepts everything, so replace it.",
                whyWrong = mapOf(
                    1 to "Another topic still sends everything to its subscriptions unless they filter.",
                    2 to "Duplicate detection drops repeats by message id; it does not filter by property.",
                ),
            ),
            B.q("ai-n7", "ai-servicebus", "Each user's messages must be processed in the order they were sent. Which feature?",
                listOf("Topics", "Sessions", "Dead-lettering", "Auto-forwarding"), setOf(1),
                "Sessions group messages by session id and deliver each group in order to one receiver at a time.",
                whyWrong = mapOf(
                    0 to "Topics copy messages to subscriptions; they do not order them per user.",
                    2 to "Dead-lettering is for messages that cannot be processed, not for ordering.",
                ),
            ),
            B.q("ai-n8", "ai-eventgrid", "The Coach must react when a file is uploaded to its storage account. What do you subscribe to?",
                listOf("A custom topic", "A system topic for the storage account", "A Service Bus queue", "An Event Grid namespace with MQTT"), setOf(1),
                "Azure services publish their own events, such as BlobCreated, to system topics.",
                whyWrong = mapOf(
                    0 to "Custom topics carry events your code publishes; Storage publishes its own.",
                    2 to "Storage does not post to a Service Bus queue on its own; Event Grid is what carries the event.",
                ),
            ),
            B.q("ai-n9", "ai-eventgrid", "Which Python code publishes a custom CloudEvent to an Event Grid topic?",
                listOf("EventGridPublisherClient(endpoint, credential).send([CloudEvent(...)])", "ServiceBusClient(...).get_topic_sender()", "requests.get(topic_endpoint)", "EventHubProducerClient.send_batch()"), setOf(0),
                "azure-eventgrid's EventGridPublisherClient sends CloudEvent objects from azure.core.messaging.",
                whyWrong = mapOf(
                    1 to "That sends to a Service Bus topic, a different service.",
                    3 to "That is Event Hubs, for streams of telemetry, not Event Grid.",
                ),
            ),
            B.q("ai-n10", "ai-eventgrid", "A subscriber wants only NoteUpdated events for notes under /notes/german/. What do you configure?",
                listOf("An event type filter and a subject begins-with filter", "A SQL filter rule", "A dead-letter container", "Batching"), setOf(0),
                "Event type filters pick the kind of event; subject filters pick a branch of the subject path.",
                whyWrong = mapOf(
                    1 to "SQL filter rules belong to Service Bus subscriptions, not Event Grid.",
                    3 to "Batching changes how many events arrive per request, not which.",
                ),
            ),
            B.q("ai-n11", "ai-eventgrid", "Only events where data.wordCount is above 20 should be delivered. Which filter?",
                listOf("Subject ends-with", "Advanced filter with NumberGreaterThan", "Event type", "Retry policy"), setOf(1),
                "Advanced filters compare fields in the event, including data, with operators such as NumberGreaterThan.",
                whyWrong = mapOf(
                    0 to "Subject filters test only the subject string's start or end.",
                    3 to "The retry policy controls redelivery, not which events match.",
                ),
            ),
            B.q("ai-n12", "ai-eventgrid", "Events that cannot be delivered must not be lost. What do you configure on the subscription?",
                listOf("Nothing; Event Grid keeps them by default", "A dead-letter location in a blob container", "A Service Bus dead-letter queue", "A longer subject"), setOf(1),
                "With a dead-letter container set, events are written there when retries run out.",
                whyWrong = mapOf(
                    0 to "By default dead-lettering is off, and undelivered events are dropped.",
                    2 to "Event Grid dead-letters to blob storage, not to a Service Bus queue.",
                ),
            ),
            B.q("ai-n13", "ai-eventgrid", "A handler occasionally processes the same event twice. What is the correct design?",
                listOf("Make the handler idempotent, for example by tracking event ids", "Ask Event Grid for exactly-once delivery", "Return 400 for duplicates so they are retried", "Turn off retries"), setOf(0),
                "Event Grid delivers at least once, so duplicates are normal and the handler must tolerate them.",
                whyWrong = mapOf(
                    1 to "There is no exactly-once option; delivery is at least once.",
                    3 to "Without retries you lose events whenever the handler has a brief failure.",
                ),
            ),
            B.q("ai-n14", "ai-eventgrid", "A webhook subscription using the CloudEvents schema is being validated. What must the endpoint do?",
                listOf("Answer an HTTP OPTIONS request with a WebHook-Allowed-Origin header", "Return the event id in the body", "Call the Event Grid management API", "Nothing"), setOf(0),
                "CloudEvents webhooks use the OPTIONS handshake: request with WebHook-Request-Origin, reply with WebHook-Allowed-Origin.",
                whyWrong = mapOf(
                    1 to "Echoing a validation code belongs to the Event Grid schema handshake, and it is the code, not an event id.",
                    3 to "Nothing is needed only for handlers like Functions or Service Bus; a plain webhook must answer the handshake.",
                ),
            ),
            B.q("ai-n15", "ai-eventgrid", "With the default retry policy, how long does Event Grid keep trying to deliver an event?",
                listOf("Up to 30 attempts or 24 hours, whichever comes first", "Once only", "Forever", "Exactly 10 attempts"), setOf(0),
                "The defaults are 30 delivery attempts and 1,440 minutes time to live; both can be lowered.",
                whyWrong = mapOf(
                    1 to "Event Grid retries with backoff; a single try would make at-least-once meaningless.",
                    3 to "Ten is Service Bus's default max delivery count, not Event Grid's.",
                ),
            ),
            B.q("ai-n16", "ai-functions", "In the Python v2 programming model, how is a function's trigger declared?",
                listOf("In function.json next to the code", "With a decorator in function_app.py", "In host.json", "In local.settings.json"), setOf(1),
                "The v2 model uses decorators such as @app.route in code; there is no function.json.",
                whyWrong = mapOf(
                    0 to "function.json is the v1 model.",
                    2 to "host.json holds app-wide settings, not per-function triggers.",
                ),
            ),
            B.q("ai-n17", "ai-functions", "An HTTP function must also put a message on a Service Bus queue, with no SDK code. What do you add?",
                listOf("A Service Bus output binding", "A Service Bus trigger", "An Event Grid subscription", "A timer trigger"), setOf(0),
                "An output binding sends data when the function sets its func.Out parameter.",
                whyWrong = mapOf(
                    1 to "A trigger starts a function from a message; it does not send one.",
                    2 to "Event Grid does not put messages on a queue for this function's code.",
                ),
            ),
            B.q("ai-n18", "ai-functions", "A new serverless function app must scale to zero and reach resources in a virtual network. Which plan?",
                listOf("Dedicated", "Flex Consumption", "Legacy Consumption", "Premium with zero instances"), setOf(1),
                "Flex Consumption is the recommended serverless plan, with virtual network support and scale to zero.",
                whyWrong = mapOf(
                    0 to "A Dedicated plan keeps paid instances running and does not scale to zero.",
                    2 to "The older Consumption plan has no virtual network integration.",
                ),
            ),
            B.q("ai-n19", "ai-functions", "After func azure functionapp publish, the app cannot find a setting that works locally. Why?",
                listOf("local.settings.json is not deployed", "Python functions cannot read settings", "The setting must be in host.json", "publish strips environment variables"), setOf(0),
                "local.settings.json is for local runs only; add the value as an app setting in Azure.",
                whyWrong = mapOf(
                    1 to "Settings reach Python as environment variables, read with os.environ.",
                    2 to "host.json is not where app settings and connection values belong.",
                ),
            ),
            B.q("ai-n20", "ai-functions", "A Service Bus trigger uses connection=\"ServiceBusConnection\" with managed identity. Which app setting do you create?",
                listOf("ServiceBusConnection__fullyQualifiedNamespace", "ServiceBusConnection set to the shared access key", "AzureWebJobsServiceBus", "FUNCTIONS_WORKER_RUNTIME"), setOf(0),
                "Identity-based connections use the setting name plus __fullyQualifiedNamespace, and the identity needs a data role.",
                whyWrong = mapOf(
                    1 to "A shared access key is a secret, the very thing an identity-based connection avoids.",
                    3 to "That setting says which language runs, not how to reach Service Bus.",
                ),
            ),
            B.q("ai-n21", "ai-functions", "A function must run whenever study notes are created or updated in Cosmos DB. What do you use?",
                listOf("A Cosmos DB trigger with a lease container", "A timer trigger that queries every minute", "A Cosmos DB output binding", "An HTTP trigger"), setOf(0),
                "The Cosmos DB trigger reads the change feed and records its progress in a lease container.",
                whyWrong = mapOf(
                    1 to "Polling on a timer misses the point and costs RUs; the change feed tells you what changed.",
                    2 to "An output binding writes items; it does not react to changes.",
                ),
            ),
            B.q("ai-n22", "ai-functions", "A Service Bus-triggered Python function raises an exception. What happens to the message?",
                listOf("It is completed anyway", "It is abandoned and retried, then dead-lettered past the max delivery count", "It is deleted", "It is sent to Event Grid"), setOf(1),
                "The trigger completes on success and abandons on an exception, so the normal Service Bus retry and dead-letter rules apply.",
                whyWrong = mapOf(
                    0 to "Completing a failed message would lose it; the runtime abandons it instead.",
                    2 to "Nothing is deleted on failure; it is redelivered.",
                ),
            ),
        )),
    )
}
