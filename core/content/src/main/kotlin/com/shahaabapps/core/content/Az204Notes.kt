package com.shahaabapps.core.content

/**
 * Notes sheets for AZ-204's Compute and Connect modules. The same five things
 * per sheet as [Ai200Notes], and the same plain ladder pictures.
 */
internal object Az204Notes {

    private val appService = notes(
        "az-app-service",
        """
            App Service hosts a web app on a plan you share between apps: the plan is what you pay for and what
            scales, and the app brings its settings, its identity and its custom domain.
        """,
        B.glossary(
            "App Service plan" to "The machines your apps run on. The tier decides the features and the bill, and several apps can share one plan.",
            "App setting" to "A name and value the platform sets as an environment variable, which overrides the same key in the configuration file.",
            "Managed identity" to "An Entra identity for the app, so it can reach Key Vault, Storage or SQL with no credential in configuration.",
            "Always On" to "Stops the app being unloaded when idle, which is what keeps a timer or a background job alive.",
            "Scale up and scale out" to "Up is a bigger tier; out is more instances of the same one. Different problems, different answers.",
        ),
        B.diagram(
            caption = "One plan, two apps, and the settings and secrets arriving from outside the deployment.",
            nodes = listOf(
                DNode("plan", "App Service plan", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "the thing you pay for", hue = DHue.COMPUTE),
                DNode("web", "Web app", 0.27f, 0.42f, w = 0.42f, h = 0.16f, sub = "takes traffic", hue = DHue.COMPUTE),
                DNode("api", "API app", 0.73f, 0.42f, w = 0.42f, h = 0.16f, sub = "same plan", hue = DHue.COMPUTE),
                DNode("cfg", "Settings and secrets", 0.5f, 0.78f, w = 0.66f, h = 0.16f, sub = "app settings, Key Vault", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("plan", "web"), DEdge("plan", "api"),
                DEdge("cfg", "web", dashed = true), DEdge("cfg", "api", dashed = true),
            ),
            aspect = 0.95f,
        ),
        B.table(
            listOf("Tier", "Gives you", "Pick it when"),
            listOf("Free and Shared", "No custom domain SSL, no scale out", "Trying something"),
            listOf("Basic", "Custom domains, manual scale out", "A small production app"),
            listOf("Standard", "Slots, autoscale, daily backups", "Most production work"),
            listOf("Premium v3", "More memory, faster, virtual network", "Heavier apps, or private networking"),
        ),
        B.commands(
            "Plan, app and settings",
            B.cmd(
                "az appservice plan create -g tt-rg -n tt-plan --sku S1",
                "Before any app can exist.",
                "The plan is the compute, so the tier chosen here decides whether slots, autoscale and custom certificates are available at all.",
            ),
            B.cmd(
                "az webapp create -g tt-rg -p tt-plan -n tt-web --runtime \"DOTNETCORE:9.0\"",
                "Creating the app itself.",
                "The runtime pins the stack the platform provides, which is what lets you deploy code rather than a container image.",
            ),
            B.cmd(
                "az webapp identity assign -g tt-rg -n tt-web",
                "Before the app reads anything from Key Vault or Storage.",
                "Turns on the system-assigned identity and prints its principal id, which is the id every later role assignment needs.",
            ),
            B.cmd(
                "az webapp log tail -g tt-rg -n tt-web",
                "When a deployment starts and nothing answers.",
                "Streams the platform and application logs live, which is usually the fastest way to see a start-up exception the portal shows minutes later.",
            ),
        ),
        B.mistakes(
            "Scaling up when the problem is the number of concurrent requests. That is scale out.",
            "Leaving Always On off on an app with a background timer, then wondering why it stops overnight.",
            "Putting a connection string in the configuration file instead of an app setting, so every environment ships the wrong one.",
        ),
        B.recap(
            "The plan is the compute and the bill; the app is the deployment.",
            "Settings override the file, and secrets come from the vault by identity.",
            "Up for a bigger machine, out for more of them.",
        ),
    )

    private val slots = notes(
        "az-slots-scaling",
        """
            A deployment slot is a second copy of the app you can warm and then swap into production, and
            autoscale adds instances on a rule rather than on someone noticing.
        """,
        B.glossary(
            "Slot" to "A copy of the app on the same plan with its own hostname, used to stage a release.",
            "Swap" to "Making the staging slot the production one. The platform warms the new instances first, so the switch has no cold start.",
            "Sticky setting" to "A setting marked to stay with its slot across a swap, which is how staging keeps its own database.",
            "Autoscale rule" to "A metric, a threshold and an action: add or remove instances when CPU, queue length or a custom metric crosses it.",
            "Cool-down" to "The wait after a scale action before another is allowed, which stops the app flapping in and out.",
        ),
        B.diagram(
            caption = "Deploy to staging, warm it, swap it into production, and swap back if it is wrong.",
            nodes = listOf(
                DNode("dep", "Deploy", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "to staging only"),
                DNode("warm", "Warm up", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "platform pings it", hue = DHue.COMPUTE),
                DNode("swap", "Swap", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "hostnames exchange", hue = DHue.CHOICE),
                DNode("prod", "Production", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "no cold start", hue = DHue.COMPUTE),
                DNode("back", "Swap back", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "the rollback", hue = DHue.CHOICE),
            ),
            edges = listOf(
                DEdge("dep", "warm"), DEdge("warm", "swap"), DEdge("swap", "prod"), DEdge("prod", "back", dashed = true),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("Problem", "Answer", "Not"),
            listOf("Cold start on every release", "Warm a slot and swap", "Deploying straight to production"),
            listOf("Friday evening load", "Autoscale on a metric", "A bigger instance all week"),
            listOf("Staging hitting the live database", "Mark the setting sticky", "Changing it after each swap"),
            listOf("Instances flapping", "A longer cool-down", "A higher threshold"),
        ),
        B.commands(
            "Slots, swaps and rules",
            B.cmd(
                "az webapp deployment slot create -g tt-rg -n tt-web --slot staging",
                "Once, before the first safe release.",
                "Slots need Standard or above, so this command failing is usually the tier rather than the syntax.",
            ),
            B.cmd(
                "az webapp deployment slot swap -g tt-rg -n tt-web --slot staging --target-slot production",
                "Releasing something already warmed and checked.",
                "Warms the staging instances and then exchanges hostnames, which is why the first real request does not pay for the start-up.",
            ),
            B.cmd(
                "az monitor autoscale create -g tt-rg --resource <plan-id> --min-count 2 --max-count 10 --count 2",
                "Before any autoscale rule can be added.",
                "Creates the autoscale setting with its bounds. A minimum of two is also the cheapest way to survive one instance restarting.",
            ),
            B.cmd(
                "az monitor autoscale rule create -g tt-rg --autoscale-name tt-plan-autoscale --condition \"CpuPercentage > 70 avg 5m\" --scale out 1",
                "When load varies through the day.",
                "One rule out needs a matching rule in, or the app scales up on Friday and stays there, which is the mistake the exam likes to show you.",
            ),
        ),
        B.mistakes(
            "Writing a scale-out rule with no scale-in rule.",
            "Swapping without sticky settings, so production picks up the staging connection string.",
            "Expecting slots on a Basic plan.",
        ),
        B.recap(
            "Warm the slot, swap it, swap back to roll back.",
            "Every scale-out rule needs its scale-in twin.",
            "Sticky settings are what keep staging staging.",
        ),
    )

    private val functions = notes(
        "az-functions",
        """
            A function is a piece of code with a trigger: something happens, the platform runs your handler, and
            bindings carry the data in and out without a client in your code.
        """,
        B.glossary(
            "Trigger" to "What starts the function, and there is exactly one: HTTP, timer, queue, blob, event, Cosmos change feed.",
            "Input and output binding" to "Declared data the platform fetches or writes for you, so the handler is only the logic.",
            "Consumption plan" to "Pay per execution, scale to zero, cold starts, and a timeout that defaults to five minutes.",
            "Premium plan" to "Pre-warmed instances, no cold start, virtual network access, longer runs.",
            "host.json" to "Settings for the whole function app: batch sizes, retries, timeouts, logging.",
        ),
        B.diagram(
            caption = "One trigger starts the handler, and an output binding writes the result for it.",
            nodes = listOf(
                DNode("trig", "Trigger", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "queue, timer, HTTP", hue = DHue.FLOW),
                DNode("fn", "Your handler", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "the logic only", hue = DHue.COMPUTE),
                DNode("out", "Output binding", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "declared", hue = DHue.CHOICE),
                DNode("dest", "Queue, table, blob", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "no client code", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(DEdge("trig", "fn"), DEdge("fn", "out"), DEdge("out", "dest")),
            aspect = 1.0f,
        ),
        B.table(
            listOf("Plan", "Cold start", "Timeout", "Network"),
            listOf("Consumption", "Yes", "5 min, up to 10", "No"),
            listOf("Flex Consumption", "Reduced", "Configurable", "Yes"),
            listOf("Premium", "No", "30 min, up to 60", "Yes"),
            listOf("App Service plan", "No", "Unbounded", "Yes"),
        ),
        B.commands(
            "Creating and configuring a function app",
            B.cmd(
                "az functionapp create -g tt-rg -n tt-fn --storage-account ttstore --runtime dotnet-isolated --functions-version 4 --consumption-plan-location westeurope",
                "Creating the app before the first deployment.",
                "The storage account holds the triggers, the leases and the app's own state, so the app does not start without one and must not share it carelessly.",
            ),
            B.cmd(
                "func azure functionapp publish tt-fn",
                "Deploying from the project you have been running locally.",
                "Packages and uploads in one step, and it is the command the exam means when it says deploy with the Core Tools.",
            ),
            B.cmd(
                "az functionapp config appsettings set -g tt-rg -n tt-fn --settings FUNCTIONS_WORKER_RUNTIME=dotnet-isolated",
                "When the host starts but finds no functions.",
                "A mismatch between this setting and what was deployed is the usual cause of an app that runs with nothing in it.",
            ),
            B.cmd(
                "az functionapp plan create -g tt-rg -n tt-premium --sku EP1 --location westeurope",
                "When cold starts or a virtual network rule it out of Consumption.",
                "Premium keeps instances warm and can join a network, which are the two things Consumption cannot do at any price.",
            ),
        ),
        B.mistakes(
            "Two triggers on one function. There is one trigger; the rest are bindings.",
            "Expecting a long job to finish on Consumption. Check the timeout before blaming the code.",
            "Sharing the function app's storage account with application data, then clearing it.",
        ),
        B.recap(
            "One trigger, as many bindings as you like.",
            "Consumption for cheap and spiky; Premium for warm and networked.",
            "The storage account is part of the runtime.",
        ),
    )

    private val durable = notes(
        "az-durable",
        """
            Durable Functions write a workflow as code: an orchestrator calls activities, awaits them, and is
            replayed from its history after every await, which is why it must be deterministic.
        """,
        B.glossary(
            "Orchestrator" to "The function that describes the workflow. Replayed from its event history, so it must be deterministic.",
            "Activity" to "One step that does real work. The only place a side effect belongs.",
            "Replay" to "The runtime re-running the orchestrator from the start after each await, using the recorded results instead of calling again.",
            "External event" to "A signal from outside that an orchestration waits for, which is how a human approval fits in.",
            "Fan-out fan-in" to "Starting many activities at once and waiting for all of them, which is the pattern most questions describe.",
        ),
        B.diagram(
            caption = "Fan-out and fan-in: the orchestrator starts the activities together and waits for all of them.",
            nodes = listOf(
                DNode("client", "Client function", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "starts it", hue = DHue.COMPUTE),
                DNode("orch", "Orchestrator", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "deterministic code", hue = DHue.CHOICE),
                DNode("acts", "Activities", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "run in parallel", hue = DHue.COMPUTE),
                DNode("hist", "Event history", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "in Table Storage", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("done", "Result", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "after fan-in"),
            ),
            edges = listOf(
                DEdge("client", "orch"), DEdge("orch", "acts"), DEdge("acts", "hist", label = "recorded"),
                DEdge("hist", "orch", label = "replay", dashed = true), DEdge("orch", "done"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("Pattern", "Shape", "Typical question"),
            listOf("Function chaining", "One after another, each using the last result", "A pipeline of steps"),
            listOf("Fan-out fan-in", "Many at once, then aggregate", "Process every file, then total them"),
            listOf("Async HTTP", "Start, return a status url, poll", "A long job behind an API"),
            listOf("Human interaction", "Wait for an external event with a timeout", "An approval that may never come"),
            listOf("Monitor", "A loop with a durable timer", "Poll until a condition or a deadline"),
        ),
        B.commands(
            "Adding it to a function app",
            B.cmd(
                "dotnet add package Microsoft.Azure.Functions.Worker.Extensions.DurableTask",
                "Before any orchestrator will compile.",
                "Durable is an extension rather than part of the runtime, so a project without the package has triggers it cannot bind.",
            ),
            B.cmd(
                "az functionapp config appsettings set -g tt-rg -n tt-fn --settings AzureWebJobsStorage=<connection>",
                "Before the first orchestration runs.",
                "The event history and the control queues live in that storage account, so Durable depends on it far more than an ordinary function does.",
            ),
            B.cmd(
                "func start --verbose",
                "When an orchestration behaves differently locally and in Azure.",
                "The verbose host log shows the replay boundaries, which is how non-deterministic code in an orchestrator is actually found.",
            ),
        ),
        B.mistakes(
            "Calling the clock or a random number in an orchestrator. Replay then takes a different path; use the context's own versions.",
            "Doing the work in the orchestrator rather than in an activity.",
            "Awaiting an external event with no timeout, so an unapproved orchestration waits forever.",
        ),
        B.recap(
            "Orchestrators decide; activities do.",
            "Replay means deterministic: no clock, no randomness, no direct IO.",
            "Fan-out fan-in is the pattern most questions are describing.",
        ),
    )

    private val containers = notes(
        "az-containers",
        """
            Azure's container hosts: a registry to hold the image, Container Instances for a single short-lived
            container, and Container Apps for a service with revisions and scaling.
        """,
        B.glossary(
            "Container Registry" to "Where images live, addressed as registry/repository:tag, with a digest for the exact bytes.",
            "Container Instances" to "One container, started quickly, billed per second. No orchestration, no scaling, no slots.",
            "Container Apps" to "A managed service for containers that keeps revisions, splits traffic and scales on a metric, including to zero.",
            "Revision" to "An immutable snapshot of a Container App's image and configuration.",
            "AcrPull" to "The role a compute identity needs to fetch an image, which is the alternative to the registry admin user.",
        ),
        B.diagram(
            caption = "One image in the registry, pulled by whichever host suits the work at hand.",
            nodes = listOf(
                DNode("acr", "Container Registry", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "the image", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("aci", "Container Instances", 0.27f, 0.45f, w = 0.44f, h = 0.16f, sub = "one short job", hue = DHue.COMPUTE),
                DNode("aca", "Container Apps", 0.73f, 0.45f, w = 0.44f, h = 0.16f, sub = "a service", hue = DHue.COMPUTE),
                DNode("id", "Managed identity", 0.5f, 0.82f, w = 0.66f, h = 0.16f, sub = "AcrPull, no password", hue = DHue.CHOICE),
            ),
            edges = listOf(
                DEdge("acr", "aci"), DEdge("acr", "aca"),
                DEdge("id", "acr", label = "pulls as", dashed = true),
            ),
            aspect = 1.0f,
        ),
        B.compare(
            "Which host?",
            B.card("Container Instances", "One container, for a while, then gone.", "Starts in seconds", "Billed per second", "No scaling or revisions"),
            B.card("Container Apps", "A service that must keep answering.", "Revisions and traffic splits", "Scales on HTTP or a queue, to zero", "Internal and external ingress"),
            B.card("App Service", "A web app that happens to be a container.", "Slots and swap", "Easy domains and certificates", "No scale to zero"),
        ),
        B.commands(
            "Build, run, ship",
            B.cmd(
                "az acr build -r ttreg -t tt/api:v3 .",
                "Building the image without Docker locally.",
                "Builds in Azure and pushes in the same step, which keeps the shipped image independent of whatever is installed on a developer machine.",
            ),
            B.cmd(
                "az container create -g tt-rg -n tt-job --image ttreg.azurecr.io/tt/api:v3 --restart-policy Never",
                "Running a one-off job in a container.",
                "Never is what makes it a job rather than a service: it runs, it exits, and it stops costing money.",
            ),
            B.cmd(
                "az containerapp up -n tt-api -g tt-rg --image ttreg.azurecr.io/tt/api:v3 --ingress external --target-port 8080",
                "Getting a container running as a service quickly.",
                "Creates the environment, the app and the ingress in one command, which is the fastest path from an image to a public url.",
            ),
            B.cmd(
                "az role assignment create --assignee <identity-id> --role AcrPull --scope <registry-id>",
                "Instead of enabling the registry admin user.",
                "Lets the host pull with its own identity, so there is no registry password in any deployment or pipeline.",
            ),
        ),
        B.mistakes(
            "Reaching for Container Instances for a web service. It has no scaling and no revisions.",
            "Enabling the admin user because the pull failed. The answer is AcrPull on an identity.",
            "Deploying the latest tag to several replicas and expecting them to match.",
        ),
        B.recap(
            "Registry holds it, Instances runs it once, Container Apps keeps it running.",
            "Identity and AcrPull, never the admin user.",
            "Tag for people, digest for production.",
        ),
    )

    private val apim = notes(
        "az-apim",
        """
            API Management is one front door for several APIs: products and subscriptions decide who may call,
            and policies rewrite, authenticate, cache and throttle on the way through.
        """,
        B.glossary(
            "API and operation" to "The front-door shape you publish, which need not match the back end's own shape.",
            "Product" to "A bundle of APIs a caller subscribes to, carrying its own limits and terms.",
            "Subscription key" to "The key a caller sends, which identifies them for limits and for reporting.",
            "Policy" to "XML in the inbound, backend, outbound or on-error section that transforms, validates, caches or throttles a call.",
            "Named value" to "A reusable setting or secret for policies, which can be backed by Key Vault.",
        ),
        B.diagram(
            caption = "A call arriving at the gateway, passing the inbound policies, and reaching the back end.",
            nodes = listOf(
                DNode("caller", "Caller", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "with a subscription key"),
                DNode("in", "Inbound policies", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "validate, throttle", hue = DHue.CHOICE),
                DNode("gw", "Gateway", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "cache lookup", hue = DHue.COMPUTE),
                DNode("back", "Back-end API", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "your service", hue = DHue.COMPUTE),
                DNode("out", "Outbound policies", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "reshape, cache store", hue = DHue.CHOICE),
            ),
            edges = listOf(
                DEdge("caller", "in"), DEdge("in", "gw"), DEdge("gw", "back"), DEdge("back", "out"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("Need", "Policy", "Section"),
            listOf("Reject an unauthenticated call", "validate-jwt", "inbound"),
            listOf("Limit a caller", "rate-limit-by-key", "inbound"),
            listOf("Hide a back-end header", "set-header with delete", "outbound"),
            listOf("Serve a repeat read cheaply", "cache-lookup and cache-store", "inbound and outbound"),
            listOf("Send some traffic elsewhere", "set-backend-service", "backend"),
        ),
        B.commands(
            "Instance, API and policy",
            B.cmd(
                "az apim create -g tt-rg -n tt-apim --publisher-email you@example.com --publisher-name TwoTracks --sku-name Developer",
                "Creating the instance for coursework.",
                "Developer is the cheap tier with no service level agreement, and creation takes tens of minutes, which is worth knowing before a timed exercise.",
            ),
            B.cmd(
                "az apim api import -g tt-rg -n tt-apim --api-id tt --path tt --specification-format OpenApi --specification-url <swagger-url>",
                "Publishing an existing API through the gateway.",
                "Importing the specification creates every operation at once, which is both faster and less error-prone than defining them by hand.",
            ),
            B.cmd(
                "az apim api policy create -g tt-rg --service-name tt-apim --api-id tt --xml-policy @policy.xml",
                "Adding authentication, caching or throttling.",
                "Policies are where almost all of API Management's behaviour lives, so this is the command behind most exam scenarios about it.",
            ),
            B.cmd(
                "az apim product create -g tt-rg --service-name tt-apim --product-id partner --subscription-required true",
                "When different callers need different limits.",
                "A product is how one API is offered on several sets of terms without being deployed twice.",
            ),
        ),
        B.mistakes(
            "Putting validate-jwt in the outbound section. The call has already reached the back end by then.",
            "Rate limiting by subscription when the requirement is per user. rate-limit-by-key with the right key is the answer.",
            "Expecting the Consumption tier to have a developer portal and a virtual network.",
        ),
        B.recap(
            "Products and subscriptions decide who; policies decide what happens.",
            "Inbound to reject, backend to route, outbound to reshape.",
            "Import the specification rather than hand-building operations.",
        ),
    )

    private val eventGrid = notes(
        "az-eventgrid",
        """
            Event Grid delivers notifications that something happened, pushed to subscribers with filters and
            retries, and dead-lettered to a blob container when it finally gives up.
        """,
        B.glossary(
            "Event" to "A small record that something happened: id, type, subject, time. Not the data, and not an instruction.",
            "System topic" to "Events an Azure resource publishes about itself, such as Blob Created.",
            "Custom topic" to "An endpoint your own code publishes events to.",
            "Event handler" to "What the event is delivered to: a function, a webhook, a queue, Event Hubs.",
            "Retry and dead letter" to "Delivery is retried with backoff for up to a day, then written to a blob container if one is configured.",
        ),
        B.diagram(
            caption = "A resource publishes, the filter narrows, and the handler is pushed the event.",
            nodes = listOf(
                DNode("src", "Blob Storage", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "Blob Created", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("topic", "System topic", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "nothing to deploy", hue = DHue.FLOW),
                DNode("sub", "Subscription", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "filter and handler", hue = DHue.CHOICE),
                DNode("fn", "Function", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "pushed, not polling", hue = DHue.COMPUTE),
            ),
            edges = listOf(DEdge("src", "topic"), DEdge("topic", "sub"), DEdge("sub", "fn", label = "push")),
            aspect = 1.0f,
        ),
        B.table(
            listOf("Service", "Means", "Shape"),
            listOf("Event Grid", "This happened", "Push, many subscribers, no order"),
            listOf("Service Bus", "Do this", "Pull, locks, dead letters, sessions"),
            listOf("Event Hubs", "Here is a stream", "Pull by offset, replayable, partitioned"),
            listOf("Queue Storage", "Do this, simply", "Pull, huge, few features"),
        ),
        B.commands(
            "Subscribing and surviving failures",
            B.cmd(
                "az eventgrid event-subscription create --name thumbs --source-resource-id <storage-id> --endpoint <function-url> --included-event-types Microsoft.Storage.BlobCreated",
                "Wiring a handler to a resource's events.",
                "Naming the event types keeps the handler from being woken for deletes and metadata changes it has no interest in.",
            ),
            B.cmd(
                "az eventgrid event-subscription update --name thumbs --source-resource-id <storage-id> --deadletter-endpoint <container-id> --max-delivery-attempts 10",
                "Before relying on the events for anything important.",
                "With no dead-letter destination an undeliverable event is dropped silently after the retry window, leaving nothing to investigate.",
            ),
            B.cmd(
                "az eventgrid topic create -g tt-rg -n tt-events -l westeurope",
                "When your own code is the thing publishing events.",
                "A custom topic gives you an endpoint and keys, and subscribers attach to it the same way they do to a system topic.",
            ),
        ),
        B.mistakes(
            "Sending the payload in the event. Send a pointer; the handler fetches it.",
            "Relying on order. There is none here.",
            "Forgetting the webhook validation handshake on a custom endpoint, so the subscription never activates.",
        ),
        B.recap(
            "Announce with Event Grid; instruct with Service Bus.",
            "Filter by type or subject, always.",
            "No dead-letter destination means lost events.",
        ),
    )

    private val eventHubs = notes(
        "az-eventhubs",
        """
            Event Hubs takes a stream at scale: events land in partitions, consumer groups read them
            independently by offset, and a checkpoint is what makes restarting cheap.
        """,
        B.glossary(
            "Partition" to "An ordered log inside the hub. Order is per partition, never across the hub.",
            "Partition key" to "What decides which partition an event lands in, so events sharing a key stay in order.",
            "Consumer group" to "An independent view of the whole stream, with its own position, so two readers do not steal each other's events.",
            "Offset and checkpoint" to "Where a reader is, and where it has recorded being, so a restart resumes rather than replays everything.",
            "Capture" to "Automatically writing the stream to Blob Storage or Data Lake, with no code at all.",
        ),
        B.diagram(
            caption = "Events partitioned by key, read by two consumer groups that do not affect each other.",
            nodes = listOf(
                DNode("prod", "Producers", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "with a partition key"),
                DNode("parts", "Partitions", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "ordered logs", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("g1", "Group: live", 0.27f, 0.60f, w = 0.42f, h = 0.15f, sub = "own offset", hue = DHue.COMPUTE),
                DNode("g2", "Group: archive", 0.73f, 0.60f, w = 0.42f, h = 0.15f, sub = "own offset", hue = DHue.COMPUTE),
                DNode("ckpt", "Checkpoints", 0.5f, 0.88f, w = 0.62f, h = 0.15f, sub = "in Blob Storage", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("prod", "parts"), DEdge("parts", "g1"), DEdge("parts", "g2"),
                DEdge("g1", "ckpt"), DEdge("g2", "ckpt"),
            ),
            aspect = 1.05f,
        ),
        B.table(
            listOf("Need", "Event Hubs", "Service Bus"),
            listOf("Millions of events an hour", "Yes, this is the point", "Expensive and wrong"),
            listOf("Read the same events twice", "Yes, by offset or a second group", "No"),
            listOf("Dead letter a bad message", "No", "Yes"),
            listOf("Strict per-key order", "Per partition, by key", "Per session"),
        ),
        B.commands(
            "Namespace, hub and capture",
            B.cmd(
                "az eventhubs namespace create -g tt-rg -n tt-hubs --sku Standard",
                "Before any hub can exist.",
                "Standard is the tier with consumer groups beyond the default and with capture available, both of which the course relies on.",
            ),
            B.cmd(
                "az eventhubs eventhub create -g tt-rg --namespace-name tt-hubs -n telemetry --partition-count 4 --retention-time-in-hours 24",
                "Creating the stream itself.",
                "Partition count cannot be lowered later and sets the ceiling on parallel reading, so it is the one number worth thinking about here.",
            ),
            B.cmd(
                "az eventhubs eventhub consumer-group create -g tt-rg --namespace-name tt-hubs --eventhub-name telemetry -n archive",
                "When a second reader needs its own position in the stream.",
                "Without its own group the new reader competes with the existing one, and each sees only part of the stream.",
            ),
            B.cmd(
                "az eventhubs eventhub update -g tt-rg --namespace-name tt-hubs -n telemetry --enable-capture true --storage-account <id> --blob-container raw",
                "When the stream must also be kept as files.",
                "Capture writes the stream to storage with no consumer to write or operate, which is the no-code answer the exam wants for archiving.",
            ),
        ),
        B.mistakes(
            "Expecting order across the hub. Order is per partition only.",
            "Sharing one consumer group between two unrelated applications.",
            "Never checkpointing, so every restart reprocesses the whole retention window.",
        ),
        B.recap(
            "Partitions give order and parallelism; keys choose the partition.",
            "A consumer group per reader, a checkpoint per group.",
            "Capture archives the stream with no code.",
        ),
    )

    private val serviceBus = notes(
        "az-servicebus",
        """
            Service Bus and Queue Storage both carry work that must be handled: Queue Storage is simple and
            enormous, Service Bus adds topics, sessions, transactions and dead letters.
        """,
        B.glossary(
            "Peek-lock" to "The message is hidden while you work and returns if you crash. The default, and the safe receive mode.",
            "Receive-and-delete" to "The message is gone the moment you take it. One round trip, and a crash loses the work.",
            "Dead-letter queue" to "Where a message goes after too many attempts or on expiry, so a poison message stops blocking the rest.",
            "Session" to "A key forcing related messages to one reader, in order, which is how per-entity ordering is achieved.",
            "Topic and subscription" to "One publish, several independent copies, each with its own filter.",
        ),
        B.diagram(
            caption = "A message locked while it is handled, completed on success, dead-lettered once it has failed enough.",
            nodes = listOf(
                DNode("send", "Sender", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "one message"),
                DNode("q", "Queue", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "peek-lock", hue = DHue.FLOW),
                DNode("work", "Handler", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "complete or abandon", hue = DHue.COMPUTE),
                DNode("dlq", "Dead letter", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "max deliveries", hue = DHue.CHOICE),
                DNode("look", "Investigated", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "fixed, resubmitted"),
            ),
            edges = listOf(
                DEdge("send", "q"), DEdge("q", "work"), DEdge("work", "dlq"), DEdge("dlq", "look"),
            ),
            aspect = 1.1f,
        ),
        B.compare(
            "Queue Storage, or Service Bus?",
            B.card("Queue Storage", "Simple work, and a great deal of it.", "Up to 80 TB of messages", "A simple API and logged access", "No topics, sessions or transactions"),
            B.card("Service Bus", "The work has rules.", "Topics and subscriptions", "Sessions, transactions, scheduling", "Dead letters and duplicate detection"),
        ),
        B.commands(
            "Namespace, queue, and the dead letter",
            B.cmd(
                "az servicebus namespace create -g tt-rg -n tt-bus --sku Standard",
                "Before any queue or topic exists.",
                "Basic has queues only; topics start at Standard, which is why almost every exercise names Standard explicitly.",
            ),
            B.cmd(
                "az servicebus queue create -g tt-rg --namespace-name tt-bus -n jobs --max-delivery-count 5 --lock-duration PT1M",
                "Creating the queue the sender posts to.",
                "The lock duration has to exceed how long handling takes, or the message reappears while the first handler is still working on it.",
            ),
            B.cmd(
                "az servicebus queue show -g tt-rg --namespace-name tt-bus -n jobs --query countDetails",
                "When work is not getting done.",
                "Active and dead-letter counts side by side tell you immediately whether the handler is behind or the messages are being rejected.",
            ),
            B.cmd(
                "az servicebus queue create -g tt-rg --namespace-name tt-bus -n orders --enable-session true",
                "When messages for one entity must be handled in order.",
                "Sessions are the only ordering guarantee here, and they have to be enabled at creation rather than turned on later.",
            ),
        ),
        B.mistakes(
            "Choosing receive-and-delete for work that matters.",
            "A lock duration shorter than the handling time, which produces duplicate work that looks like a bug in the handler.",
            "Expecting a topic to replay history. Use Event Hubs for that.",
        ),
        B.recap(
            "Peek-lock, complete on success, abandon on a transient failure.",
            "Sessions for order, topics for several consumers, dead letters for poison.",
            "Queue Storage when none of that is needed.",
        ),
    )

    val all: List<TopicNotes> = listOf(appService, slots, functions, durable, containers, apim, eventGrid, eventHubs, serviceBus)
}
