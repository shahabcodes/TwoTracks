package com.shahaabapps.core.content

/** Notes sheets for AI-200's messaging, functions, and secure-and-monitor topics. See [Ai200Notes]. */
internal object Ai200Notes3 {

    private val serviceBus = notes(
        "ai-servicebus",
        """
            Service Bus carries Zaiqa's paid orders: a queue for one handler, a topic for several, peek-lock so a
            crash does not lose the message, and a dead-letter queue for what can never be handled.
        """,
        B.glossary(
            "Queue" to "One message, one handler. Several readers compete for messages and each is handled once.",
            "Topic and subscription" to "One message, several independent copies. Each subscription gets its own, with its own filter.",
            "Peek-lock" to "The message is hidden rather than removed while you work, and comes back if you crash. The default, and the safe one.",
            "Dead-letter queue" to "Where a message goes after too many delivery attempts, or when it expires, so a poison message stops blocking the queue.",
            "Session" to "A key that forces messages to be handled in order by one reader, which is how per-order ordering is achieved.",
        ),
        B.diagram(
            caption = "A paid order posted once, read with peek-lock, and dead-lettered if it can never be handled.",
            nodes = listOf(
                DNode("api", "Zaiqa API", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "posts the paid order", hue = DHue.COMPUTE),
                DNode("queue", "orders queue", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "peek-lock", hue = DHue.FLOW),
                DNode("worker", "Order worker", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "completes, or abandons", hue = DHue.COMPUTE),
                DNode("dlq", "Dead letter", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "after max deliveries", hue = DHue.CHOICE),
                DNode("human", "Someone looks", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "fix and resubmit"),
            ),
            edges = listOf(
                DEdge("api", "queue", label = "send"), DEdge("queue", "worker", label = "lock"),
                DEdge("worker", "dlq", label = "gave up"), DEdge("dlq", "human"),
            ),
            aspect = 1.1f,
        ),
        B.compare(
            "Queue, topic, or Event Grid?",
            B.card("Queue", "One thing must handle each message.", "Competing readers", "Dead letters and retries", "Sessions for ordering"),
            B.card("Topic", "Several things each need their own copy.", "Independent subscriptions", "Filters per subscription", "Same guarantees as a queue"),
            B.card("Event Grid", "You are announcing that something happened.", "Many subscribers, push delivery", "Cheap at huge volume", "No ordering, no sessions"),
        ),
        B.commands(
            "Queues, topics and the dead letter",
            B.cmd(
                "az servicebus queue create -g zaiqa-rg --namespace-name zaiqa-bus -n orders --max-delivery-count 5 --enable-dead-lettering-on-message-expiration true",
                "Creating the queue the API posts to.",
                "Sets how many attempts a message gets before it is dead-lettered, which is the dial between retrying a transient failure and blocking the queue forever.",
            ),
            B.cmd(
                "az servicebus topic subscription create -g zaiqa-rg --namespace-name zaiqa-bus --topic-name order-events -n analytics",
                "When a second consumer needs its own copy of every message.",
                "A subscription is where the copies come from: adding one does not change the publisher, which is the whole reason to use a topic instead of a queue.",
            ),
            B.cmd(
                "az servicebus queue show -g zaiqa-rg --namespace-name zaiqa-bus -n orders --query countDetails",
                "When orders are not reaching the kitchen.",
                "Shows the active and the dead-letter counts side by side, which immediately tells you whether the worker is behind or whether the messages are being rejected.",
            ),
            B.cmd(
                "az role assignment create --assignee <identity-id> --role \"Azure Service Bus Data Sender\" --scope <queue-id>",
                "Instead of putting a connection string in configuration.",
                "Gives the API's managed identity the right to send and nothing else, which is the least-privilege answer the exam looks for.",
            ),
        ),
        B.mistakes(
            "Using receive-and-delete for work that matters. A crash mid-handling loses the message.",
            "Expecting a topic to replay. Service Bus delivers; it does not keep a history to read again.",
            "Leaving max delivery count at the default for a poison message, so the same failure repeats ten times before anyone notices.",
        ),
        B.recap(
            "Queue for one handler, topic for several, Event Grid to announce.",
            "Peek-lock, complete on success, abandon on a transient failure.",
            "Watch the dead-letter count; it is where the real failures collect.",
        ),
    )

    private val eventGrid = notes(
        "ai-eventgrid",
        """
            Event Grid announces that something happened — a dish photo landed in Blob Storage — and pushes it to
            subscribers with filters, retries and a dead-letter destination.
        """,
        B.glossary(
            "Event" to "A small notification that something happened, with an id, a type, a subject and a time. Not a command and not the data itself.",
            "System topic" to "Events an Azure service publishes about itself, such as Blob Created, with nothing to deploy.",
            "Custom topic" to "A topic your own code publishes to, for events Azure knows nothing about.",
            "Subscription filter" to "Which events a subscriber wants, by subject prefix or by event type, so the handler is not woken for everything.",
            "Dead-letter destination" to "A blob container Event Grid writes an event to after it has given up delivering it.",
        ),
        B.diagram(
            caption = "A photo lands in Blob Storage, Event Grid tells the function, and the thumbnail and vector follow.",
            nodes = listOf(
                DNode("blob", "Photo uploaded", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "Blob Storage", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("grid", "Event Grid", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "Blob Created", hue = DHue.FLOW),
                DNode("filter", "Filter", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "subject starts dishes", hue = DHue.CHOICE),
                DNode("fn", "Photo function", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "Python", hue = DHue.COMPUTE),
                DNode("out", "Thumbnail, vector", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "Blob and Cosmos", hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("blob", "grid"), DEdge("grid", "filter"), DEdge("filter", "fn", label = "push"), DEdge("fn", "out"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("Service", "Carries", "Delivery"),
            listOf("Event Grid", "Something happened", "Push, with retries, no ordering"),
            listOf("Service Bus", "Something must be done", "Pull, with locks and dead letters"),
            listOf("Event Hubs", "A stream to be processed in batches", "Pull by offset, replayable"),
        ),
        B.commands(
            "Subscribing, filtering and not losing events",
            B.cmd(
                "az eventgrid event-subscription create --name photos --source-resource-id <storage-id> --endpoint <function-url> --subject-begins-with dishes/",
                "Wiring a handler to a storage account's events.",
                "The filter is the important half: without it the function is woken for every blob in the account, including the ones it has no business reading.",
            ),
            B.cmd(
                "az eventgrid event-subscription update --name photos --source-resource-id <storage-id> --deadletter-endpoint <container-id>",
                "Before you rely on the events for anything that matters.",
                "Without a dead-letter destination an event that cannot be delivered is simply dropped after the retry window, with nothing left to look at.",
            ),
            B.cmd(
                "az eventgrid topic create -g zaiqa-rg -n zaiqa-events -l centralindia",
                "When your own code needs to publish events Azure knows nothing about.",
                "A custom topic gives you an endpoint and keys to post to, and subscribers attach to it exactly as they do to a system topic.",
            ),
        ),
        B.mistakes(
            "Putting the data in the event. An event says what happened and where; the handler fetches the rest.",
            "Expecting order. Event Grid gives none; if order matters, use sessions on Service Bus.",
            "Subscribing without a filter, then rate-limiting the handler to cope.",
        ),
        B.recap(
            "Announce with Event Grid, instruct with Service Bus, stream with Event Hubs.",
            "Always filter, always set a dead-letter destination.",
            "The event is a pointer, not the payload.",
        ),
    )

    private val functions = notes(
        "ai-functions",
        """
            Azure Functions in Python: a trigger starts it, bindings hand it data and take data away, and the
            plan you pick decides cold starts, timeouts and whether it can reach a private network.
        """,
        B.glossary(
            "Trigger" to "What starts the function: an HTTP request, a timer, a queue message, a blob, an event.",
            "Binding" to "Declared input or output the platform wires up for you, so the code does not build a client.",
            "Consumption plan" to "Pay per execution, scale to zero, cold starts, and a hard timeout. The cheapest and the most limited.",
            "Premium plan" to "Pre-warmed instances, no cold start, virtual-network access, longer timeouts.",
            "Durable Functions" to "An extension for work with steps and waits, where the orchestration itself is code that can be replayed.",
        ),
        B.diagram(
            caption = "A queue message triggers the function, and an output binding writes the result without any client code.",
            nodes = listOf(
                DNode("msg", "Queue message", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "the trigger", hue = DHue.FLOW),
                DNode("fn", "Python function", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "your code only", hue = DHue.COMPUTE),
                DNode("bind", "Output binding", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "declared, not coded", hue = DHue.CHOICE),
                DNode("store", "Cosmos DB", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "the result", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(DEdge("msg", "fn"), DEdge("fn", "bind"), DEdge("bind", "store")),
            aspect = 1.0f,
        ),
        B.table(
            listOf("Plan", "Cold start", "Timeout", "Pick it when"),
            listOf("Consumption", "Yes", "Up to 10 minutes", "Spiky, cheap, and latency does not matter"),
            listOf("Flex Consumption", "Reduced", "Configurable", "You want scale to zero and network access"),
            listOf("Premium", "No", "Up to 60 minutes", "Latency matters, or you need a virtual network"),
            listOf("App Service plan", "No", "Unbounded", "You already pay for the plan"),
        ),
        B.commands(
            "Creating, publishing and configuring",
            B.cmd(
                "func init zaiqa-photo --python",
                "Starting a new function app locally.",
                "Writes the project layout and the host configuration the tooling expects, which is what the exam means by the Core Tools.",
            ),
            B.cmd(
                "func azure functionapp publish zaiqa-photo",
                "Deploying what you have run locally.",
                "Packages and uploads it, and for Python it builds the dependencies remotely, which is why a local virtual environment does not have to match the cloud.",
            ),
            B.cmd(
                "az functionapp create -g zaiqa-rg -n zaiqa-photo --storage-account zaiqastore --runtime python --functions-version 4 --consumption-plan-location centralindia",
                "Creating the app before the first publish.",
                "The storage account is not optional: Functions keeps its triggers, leases and state there, and the app will not start without one.",
            ),
            B.cmd(
                "az functionapp config appsettings set -g zaiqa-rg -n zaiqa-photo --settings AzureWebJobsStorage__accountName=zaiqastore",
                "When moving off a connection string to an identity.",
                "The identity-based form of the setting lets the runtime use the app's managed identity for its own storage, which removes the last connection string in the app.",
            ),
        ),
        B.mistakes(
            "Expecting a consumption-plan function to answer quickly after a quiet hour. That is the cold start, and Premium is the fix.",
            "Writing client code where an output binding would do, which is the thing the exam is testing.",
            "Deleting the function app's storage account. The triggers live there.",
        ),
        B.recap(
            "Trigger starts it; bindings carry data in and out.",
            "Consumption for cheap and spiky, Premium for no cold start and a network.",
            "The storage account is part of the runtime, not an extra.",
        ),
    )

    private val keyVault = notes(
        "ai-keyvault",
        """
            Key Vault holds Zaiqa's payment provider key: read by managed identity with no password anywhere,
            rotated on a schedule, cached in the client, and recoverable because purge protection is on.
        """,
        B.glossary(
            "Secret, key, certificate" to "Three kinds of thing a vault holds: a string, a cryptographic key that never leaves, and a certificate with its private key.",
            "Managed identity" to "The identity the app signs in with. Nothing to store, nothing to rotate, nothing to leak.",
            "RBAC versus access policy" to "Two permission models on a vault. RBAC is the current one and the exam's answer; access policies are the older per-vault list.",
            "Soft delete" to "A deleted secret is recoverable for a retention period. Always on, and cannot be turned off.",
            "Purge protection" to "Stops anyone destroying a soft-deleted secret before the retention period ends, which is what makes the vault resistant to a malicious admin.",
        ),
        B.diagram(
            caption = "The API asks Entra for a token, then the vault for the secret, and caches it rather than asking again.",
            nodes = listOf(
                DNode("api", "Zaiqa API", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "managed identity", hue = DHue.COMPUTE),
                DNode("entra", "Microsoft Entra", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "issues a token", hue = DHue.CHOICE),
                DNode("kv", "Key Vault", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "payment key", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("cache", "In-process cache", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "with an expiry", hue = DHue.DATA),
                DNode("pay", "Payment provider", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "the actual call"),
            ),
            edges = listOf(
                DEdge("api", "entra", label = "who am I"), DEdge("entra", "kv", label = "token"),
                DEdge("kv", "cache"), DEdge("cache", "pay"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("Where the secret is", "Verdict", "Why"),
            listOf("In the repository", "Wrong", "It is in every clone and every build log forever"),
            listOf("In an app setting, plain", "Wrong", "Anyone with read access to the app can see it"),
            listOf("In Key Vault, read with a key", "Half right", "You have moved the problem to the vault's key"),
            listOf("In Key Vault, read by identity", "Right", "There is no credential to leak"),
        ),
        B.commands(
            "Vault, permission and secret",
            B.cmd(
                "az keyvault create -g zaiqa-rg -n zaiqa-kv --enable-rbac-authorization true --enable-purge-protection true",
                "Creating the vault for anything that matters.",
                "RBAC is the current permission model and purge protection cannot be added as an afterthought in every case, so both belong at creation.",
            ),
            B.cmd(
                "az role assignment create --assignee <identity-id> --role \"Key Vault Secrets User\" --scope <vault-id>",
                "Giving the API read access and nothing more.",
                "Secrets User can read a secret's value but not list, set or delete, which is the least privilege an application needs.",
            ),
            B.cmd(
                "az keyvault secret set --vault-name zaiqa-kv -n payment-key --value <value>",
                "When rotating the provider key.",
                "Each set creates a new version while the old one keeps working, which is what lets a rotation happen without a deployment or an outage.",
            ),
            B.cmd(
                "az keyvault secret show --vault-name zaiqa-kv -n payment-key --query value -o tsv",
                "When checking that the right value is in there.",
                "Reads one secret's current version. Worth knowing it leaves a trace: every read is in the vault's audit log, which is the point of the vault.",
            ),
        ),
        B.mistakes(
            "Fetching the secret on every request. The vault is rate-limited; cache it with an expiry.",
            "Using an access policy because an older tutorial did. RBAC is the answer now.",
            "Leaving purge protection off on a production vault.",
        ),
        B.recap(
            "Identity in, secret out, nothing stored.",
            "RBAC and purge protection at creation.",
            "Cache the value; rotate by adding a version.",
        ),
    )

    private val appConfig = notes(
        "ai-appconfig",
        """
            App Configuration keeps Zaiqa's settings and feature flags in one place, labelled per environment,
            refreshed without a restart, with Key Vault references for anything secret.
        """,
        B.glossary(
            "Key-value" to "One setting. Keys are often namespaced with a colon, as Zaiqa:SurgeFee.",
            "Label" to "A variant of the same key, which is how one store holds development, staging and production values.",
            "Feature flag" to "A setting the app reads as on or off, optionally for a percentage of users, so a feature ships dark and is turned on later.",
            "Dynamic refresh" to "The client polling a sentinel key and reloading when it changes, so a setting takes effect without a restart.",
            "Sentinel key" to "One key the app watches instead of all of them, so a batch of changes is picked up once, when it is complete.",
        ),
        B.diagram(
            caption = "The API reads settings by label and watches a sentinel key, so a change lands without a restart.",
            nodes = listOf(
                DNode("store", "App Configuration", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "labelled per environment", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("sent", "Sentinel key", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "polled, not everything", hue = DHue.CHOICE),
                DNode("api", "Zaiqa API", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "refreshes in place", hue = DHue.COMPUTE),
                DNode("kv", "Key Vault", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "for the secret ones", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("store", "sent"), DEdge("sent", "api", label = "changed"),
                DEdge("kv", "api", label = "reference", dashed = true),
            ),
            aspect = 1.0f,
        ),
        B.compare(
            "App Configuration, or Key Vault?",
            B.card("App Configuration", "It is a setting, and you may want to change it live.", "Labels per environment", "Feature flags", "Refresh without a restart"),
            B.card("Key Vault", "It is a secret.", "Audit log on every read", "Versions and rotation", "Soft delete and purge protection"),
        ),
        B.commands(
            "Values, labels and flags",
            B.cmd(
                "az appconfig kv set -n zaiqa-config --key Zaiqa:SurgeFee --value 15 --label production",
                "Changing a setting for one environment only.",
                "The label is what keeps staging and production apart in one store; without it the same key means the same thing everywhere, which is rarely what you want.",
            ),
            B.cmd(
                "az appconfig kv set -n zaiqa-config --key Sentinel --value v12 --label production",
                "Last, after a batch of related changes.",
                "The app watches only this key, so bumping it at the end makes a group of changes take effect together instead of half-applied.",
            ),
            B.cmd(
                "az appconfig feature set -n zaiqa-config --feature vector-search --label production",
                "Shipping a feature dark and turning it on later.",
                "A flag separates deploying from releasing, which is what lets the code go out on Tuesday and the feature appear on Thursday with no new build.",
            ),
            B.cmd(
                "az appconfig kv set-keyvault -n zaiqa-config --key Zaiqa:PaymentKey --secret-identifier <secret-uri> --label production",
                "When a setting's value must stay secret.",
                "Stores a reference rather than the value, so the store holds no secret and the app resolves it from the vault with its own identity.",
            ),
        ),
        B.mistakes(
            "Putting a secret in App Configuration. Store a Key Vault reference instead.",
            "Polling every key for changes rather than one sentinel, which is both slower and applies half a change.",
            "Forgetting the label, so a staging value reaches production.",
        ),
        B.recap(
            "Settings and flags here; secrets in the vault, by reference.",
            "Label per environment, sentinel for the refresh.",
            "A flag is how deploying stops meaning releasing.",
        ),
    )

    private val openTelemetry = notes(
        "ai-opentelemetry",
        """
            OpenTelemetry follows one Zaiqa order across the app, the API, the queue and the worker as a single
            trace, because the context is carried in the message rather than lost at the boundary.
        """,
        B.glossary(
            "Trace" to "One request's whole journey, across every service it touched.",
            "Span" to "One unit of work inside a trace, with a parent, a duration and attributes.",
            "Context propagation" to "Passing the trace id across a boundary — an HTTP header, or a message property — which is what joins two services into one trace.",
            "Sampling" to "Keeping a fraction of traces, because keeping all of them at production volume costs more than it tells you.",
            "Exporter" to "Where the telemetry goes. For Azure that is the Application Insights distribution, writing into Log Analytics.",
        ),
        B.diagram(
            caption = "One trace id carried from the phone to the worker, so four services appear as one journey.",
            nodes = listOf(
                DNode("app", "Zaiqa app", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "starts the trace"),
                DNode("api", "Zaiqa API", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "same trace id", hue = DHue.COMPUTE),
                DNode("msg", "Queue message", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "carries the context", hue = DHue.FLOW),
                DNode("worker", "Order worker", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "child spans", hue = DHue.COMPUTE),
                DNode("ai", "Application Insights", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "one end-to-end view", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("app", "api"), DEdge("api", "msg"), DEdge("msg", "worker"), DEdge("worker", "ai"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("Signal", "Answers", "Where it lands"),
            listOf("Traces", "Where did this request spend its time?", "requests and dependencies"),
            listOf("Metrics", "How many, how fast, how often, over time?", "customMetrics"),
            listOf("Logs", "What did the code actually say?", "traces"),
        ),
        B.commands(
            "Wiring it up",
            B.cmd(
                "az monitor app-insights component create -g zaiqa-rg -a zaiqa-ai -l centralindia --workspace <workspace-id>",
                "Before any service can export telemetry.",
                "A workspace-based component is the only kind now, and naming the workspace at creation is what lets KQL query the application data beside everything else.",
            ),
            B.cmd(
                "az containerapp update -n zaiqa-api -g zaiqa-rg --set-env-vars APPLICATIONINSIGHTS_CONNECTION_STRING=secretref:ai-conn OTEL_SERVICE_NAME=zaiqa-api",
                "Turning on the exporter for a service.",
                "The service name is what separates the API's spans from the worker's in one trace, so leaving it unset produces a trace nobody can read.",
            ),
            B.cmd(
                "az monitor app-insights component update -g zaiqa-rg -a zaiqa-ai --sampling-percentage 20",
                "When telemetry costs more than the compute does.",
                "Sampling keeps a fraction of traces and scales the counts, which keeps the shape of the data while cutting the bill.",
            ),
        ),
        B.mistakes(
            "Reading two services as unrelated because the context was not propagated across the queue. Set the trace properties on the message.",
            "Leaving the service name unset, so every span claims to be the same service.",
            "Sampling at 100 per cent in production, then being surprised by the Log Analytics bill.",
        ),
        B.recap(
            "One trace id, carried across every boundary including the queue.",
            "Name the service, or the trace is unreadable.",
            "Sample, and let the counts be scaled for you.",
        ),
    )

    private val kql = notes(
        "ai-kql",
        """
            KQL asks your logs questions: start at a table, filter early, summarize into the shape you want, and
            render it — the same language for exceptions, latency and cost.
        """,
        B.glossary(
            "Table" to "Where a kind of telemetry lands: requests, dependencies, exceptions, traces, customMetrics.",
            "Pipe" to "The vertical bar. Each step takes the previous result, which is why filtering first makes everything after it cheaper.",
            "summarize" to "Aggregate into groups: count, average, percentile, by something and by time bin.",
            "bin" to "Rounding a timestamp into buckets, which is how a query becomes a time chart.",
            "Percentile" to "The value a share of requests are under. p95 says what the slow tail is; an average hides it.",
        ),
        B.diagram(
            caption = "The shape of nearly every query: table, filter, summarize, render, in that order.",
            nodes = listOf(
                DNode("table", "requests", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "pick the table", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("where", "where", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "filter early", hue = DHue.CHOICE),
                DNode("sum", "summarize", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "by bin and name", hue = DHue.COMPUTE),
                DNode("render", "render", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "timechart", hue = DHue.FLOW),
            ),
            edges = listOf(DEdge("table", "where"), DEdge("where", "sum"), DEdge("sum", "render")),
            aspect = 1.0f,
        ),
        B.code(
            "kql",
            """
                // The slow tail of Zaiqa's API, by endpoint, over the last day.
                requests
                | where timestamp > ago(1d) and cloud_RoleName == "zaiqa-api"
                | summarize p95 = percentile(duration, 95), count() by name, bin(timestamp, 15m)
                | render timechart
            """,
        ),
        B.table(
            listOf("Question", "Table", "The operator that answers it"),
            listOf("What is failing?", "exceptions", "summarize count() by type, outerMessage"),
            listOf("What is slow?", "requests", "percentile(duration, 95) by name"),
            listOf("Which dependency is slow?", "dependencies", "summarize avg(duration) by target"),
            listOf("Did the release change it?", "requests", "bin(timestamp, 5m) around the deployment"),
        ),
        B.commands(
            "Running a query from the command line",
            B.cmd(
                "az monitor log-analytics query -w <workspace-id> --analytics-query \"requests | summarize count() by resultCode\"",
                "When you want a number without opening the portal.",
                "Runs the query against the workspace and prints the rows, which is what makes a KQL query usable in a script or a pipeline gate.",
            ),
            B.cmd(
                "az monitor log-analytics workspace show -g zaiqa-rg -n zaiqa-logs --query customerId -o tsv",
                "Before the query command will run.",
                "The workspace id the query needs is this customerId, not the resource name, which is the detail that stops the first attempt working.",
            ),
            B.cmd(
                "az monitor scheduled-query create -g zaiqa-rg -n api-5xx --scopes <workspace-id> --condition \"count 'requests | where resultCode startswith \\\"5\\\"' > 10\" --evaluation-frequency 5m",
                "Turning a query you keep running into an alert.",
                "A query you check by hand only catches things while you are looking; as a scheduled rule it pages someone at three in the morning instead.",
            ),
        ),
        B.mistakes(
            "Filtering after summarizing. The work is already done; filter first.",
            "Reporting the average duration. It hides the tail the users actually feel; use p95.",
            "Querying with the workspace name instead of its customerId.",
        ),
        B.recap(
            "Table, filter, summarize, render.",
            "p95, not the average.",
            "A query worth re-running is an alert.",
        ),
    )

    val all: List<TopicNotes> = listOf(serviceBus, eventGrid, functions, keyVault, appConfig, openTelemetry, kql)
}
