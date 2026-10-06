package com.shahaabapps.core.content

/**
 * AZ-204 Developing Solutions for Microsoft Azure, written for this app in
 * plain English. The running example throughout is the Two Tracks backend:
 * an API that stores study logs and sends reminders.
 *
 * Module order and weights follow the exam skills outline.
 */
object Az204 {
    val course: Course
        get() = Course(
            id = "az-204",
            title = "AZ-204 Azure Developer",
            subtitle = "Five modules, one test each, then a mock exam.",
            goalId = "az-204",
            modules = listOf(
                Module(
                    id = "az-compute", number = 1, title = "Compute",
                    summary = "Where your code runs: App Service, Functions and containers.",
                    examWeight = "25 to 30 percent", componentId = "compute",
                    topics = Az204Compute.topics + Az204Compute2.topics, quiz = Az204Compute2.quiz,
                ),
                Module(
                    id = "az-storage", number = 2, title = "Storage",
                    summary = "Blob storage and Cosmos DB for the data your code keeps.",
                    examWeight = "15 to 20 percent", componentId = "storage", topics = emptyList(), quiz = null,
                ),
                Module(
                    id = "az-security", number = 3, title = "Security",
                    summary = "Signing users in, keeping secrets, and letting services talk safely.",
                    examWeight = "15 to 20 percent", componentId = "security", topics = emptyList(), quiz = null,
                ),
                Module(
                    id = "az-monitor", number = 4, title = "Monitor and optimise",
                    summary = "Application Insights, caching and content delivery.",
                    examWeight = "10 to 15 percent", componentId = "monitoring", topics = emptyList(), quiz = null,
                ),
                Module(
                    id = "az-connect", number = 5, title = "Connect and consume",
                    summary = "API Management, events and messages between services.",
                    examWeight = "20 to 25 percent", componentId = "connect", topics = emptyList(), quiz = null,
                ),
            ),
        )
}

internal object Az204Compute {
    private val appService = Topic(
        id = "az-app-service", title = "App Service: hosting a web app without a server to manage", minutes = 18,
        blocks = listOf(
            B.plain("""
                App Service is Azure's "just run my web app" service. You hand it your code (or a container) and it
                gives you a URL, HTTPS, scaling and patching. You never log into a virtual machine.
            """),
            B.p("""
                Think of it as renting a shelf in a very well-run bakery. You bring the cake (your app). The bakery
                owns the ovens, the cleaning, the electricity and the front door. You decide how big a shelf you
                pay for and how many copies of the cake sit on it.
            """),
            B.h("The two things you create"),
            B.p("""
                Every App Service app has two parts, and the exam loves to ask which setting belongs to which.
            """),
            B.bullets(
                "An App Service plan: the shelf. It sets the region, the operating system (Windows or Linux), the pricing tier and how many worker instances you have. Several apps can share one plan.",
                "A web app: the cake. It holds your code, your app settings, your custom domain and certificates, and your deployment slots.",
            ),
            B.diagram(
                caption = "One plan can host several apps; the plan decides the size and count of the workers underneath.",
                nodes = listOf(
                    DNode("plan", "App Service plan", 0.5f, 0.78f, w = 0.9f, h = 0.2f, kind = NodeKind.PILL, tone = 1, sub = "region, OS, tier, instance count"),
                    DNode("w1", "Worker", 0.2f, 0.5f, w = 0.22f, h = 0.14f, tone = 2),
                    DNode("w2", "Worker", 0.5f, 0.5f, w = 0.22f, h = 0.14f, tone = 2),
                    DNode("w3", "Worker", 0.8f, 0.5f, w = 0.22f, h = 0.14f, tone = 2),
                    DNode("api", "Two Tracks API", 0.3f, 0.16f, w = 0.34f, h = 0.16f),
                    DNode("admin", "Admin site", 0.72f, 0.16f, w = 0.3f, h = 0.16f),
                ),
                edges = listOf(DEdge("api", "w1"), DEdge("api", "w2"), DEdge("admin", "w3"), DEdge("plan", "w2", dashed = true)),
                aspect = 0.62f,
            ),
            B.h("Pricing tiers, in one breath"),
            B.table(
                listOf("Tier", "What you get", "When"),
                listOf("Free, Shared", "Shared hardware, no custom domain SSL, no scaling", "Trying things out"),
                listOf("Basic", "Dedicated workers, custom domains, manual scale", "Small internal apps"),
                listOf("Standard", "Auto scale, deployment slots, daily backups", "Most production apps"),
                listOf("Premium", "More power, more slots, VNet integration", "Busy apps, private networking"),
                listOf("Isolated", "Your own environment (App Service Environment)", "Compliance and isolation"),
            ),
            B.tip("""
                Deployment slots start at Standard. Auto scale starts at Standard. If a question says "the cheapest tier
                that supports staging slots", the answer is Standard.
            """),
            B.h("App settings and connection strings"),
            B.p("""
                Your app needs values like a database connection string or an API key. Do not put them in code.
                App Service injects them as environment variables at start-up. In .NET they appear in
                configuration automatically; in other stacks you read them from the environment.
            """),
            B.code("bash", """
                # Set a setting with the CLI; the app reads it as an environment variable
                az webapp config appsettings set \
                  --name twotracks-api --resource-group tt-rg \
                  --settings Firestore__ProjectId=two-tracks Reminder__LeadMinutes=15
            """),
            B.remember("""
                Changing an app setting restarts the app. Settings are per slot unless you mark them as
                "deployment slot setting", in which case they stick to the slot.
            """),
            B.h("Logging and diagnostics"),
            B.p("""
                App Service can write application logs (what your code prints), web server logs, detailed error pages
                and failed request traces. Application logging to the file system switches itself off after 12 hours
                so it never fills the disk by accident. For anything long-term, send logs to a storage account or to
                Application Insights, which the Monitor module covers.
            """),
            B.code("bash", """
                az webapp log tail --name twotracks-api --resource-group tt-rg
            """),
            B.h("Where the code comes from"),
            B.bullets(
                "Push a zip, a Git repo, or a GitHub Actions workflow. Continuous deployment watches a branch and redeploys on every push.",
                "Run a container from Azure Container Registry or Docker Hub. App Service pulls the image and runs it.",
                "Deployment Center in the portal wires all of this up with a few clicks.",
            ),
            B.example("""
                For Two Tracks, the API lives in one Standard Linux plan with two instances. The admin site shares
                the plan because it is tiny. Both read their Firebase settings from app settings, never from code.
            """),
            B.recap(
                "A plan is the hardware and tier; a web app is your code and its settings. Several apps can share a plan.",
                "Slots and auto scale need at least the Standard tier.",
                "App settings become environment variables and changing them restarts the app.",
            ),
        ),
    )

    private val slotsAndScaling = Topic(
        id = "az-slots-scaling", title = "Deployment slots and scaling: ship safely and handle the crowd", minutes = 16,
        blocks = listOf(
            B.plain("""
                A deployment slot is a second copy of your app with its own URL. You deploy the new version there,
                test it, then swap it with production. Users never see a restart.
            """),
            B.h("How a swap works"),
            B.p("""
                Before the swap, Azure warms up the staging slot by sending it requests, then switches the routing so
                the staging workers answer the production URL. The old production code moves to the staging slot,
                so if something is wrong you swap back in seconds.
            """),
            B.diagram(
                caption = "A swap exchanges which slot answers the production URL. Code moves; sticky settings stay where they are.",
                nodes = listOf(
                    DNode("users", "Users", 0.12f, 0.5f, w = 0.18f, h = 0.2f, kind = NodeKind.USER),
                    DNode("prod", "Production slot", 0.5f, 0.24f, w = 0.36f, h = 0.18f, sub = "v1 before, v2 after"),
                    DNode("stage", "Staging slot", 0.5f, 0.76f, w = 0.36f, h = 0.18f, tone = 1, sub = "v2 before, v1 after"),
                    DNode("note", "Swap", 0.86f, 0.5f, w = 0.2f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("users", "prod", label = "twotracks.azurewebsites.net"), DEdge("prod", "stage", bidirectional = true, dashed = true)),
                aspect = 0.6f,
            ),
            B.h("What swaps and what sticks"),
            B.table(
                listOf("Moves with the code", "Stays with the slot"),
                listOf("General app settings and connection strings", "Settings marked as deployment slot settings"),
                listOf("Handler mappings, public certificates", "Custom domain names, managed certificates"),
                listOf("WebJobs content", "Scale settings, IP restrictions, Always On"),
            ),
            B.tip("""
                If a question says production must keep using the production database after a swap, the answer is to
                mark the connection string as a deployment slot setting (sticky). If it says the new version must
                keep its new setting, leave it unsticky so it moves with the code.
            """),
            B.h("Testing in production"),
            B.p("""
                Slots can also split live traffic. Route 10 percent of users to the staging slot and watch the error
                rate before you commit. A visitor who lands on a slot stays on it for the session thanks to a cookie,
                and a client can pin itself with the x-ms-routing-name header.
            """),
            B.h("Scaling up versus scaling out"),
            B.bullets(
                "Scale up: a bigger worker (more CPU and memory). Changes the pricing tier. Short restart.",
                "Scale out: more workers running the same app. Needs the app to be stateless or to share state elsewhere.",
            ),
            B.p("""
                Auto scale, available from Standard, adds or removes workers on a rule: CPU above 70 percent for 10
                minutes, queue length over 500, or a schedule such as every weekday at 08:00. Always set a minimum, a
                maximum and a cool-down so the rule does not flap.
            """),
            B.diagram(
                caption = "Scale up makes each worker bigger. Scale out adds workers behind the same URL.",
                nodes = listOf(
                    DNode("s1", "Small worker", 0.2f, 0.3f, w = 0.24f, h = 0.16f, tone = 2),
                    DNode("s2", "Big worker", 0.2f, 0.72f, w = 0.3f, h = 0.22f),
                    DNode("o1", "Worker", 0.6f, 0.72f, w = 0.18f, h = 0.16f),
                    DNode("o2", "Worker", 0.8f, 0.72f, w = 0.18f, h = 0.16f),
                    DNode("o3", "Worker", 0.7f, 0.3f, w = 0.18f, h = 0.16f, tone = 2),
                    DNode("up", "scale up", 0.2f, 0.51f, w = 0.18f, h = 0.1f, kind = NodeKind.NOTE, tone = 2),
                    DNode("out", "scale out", 0.7f, 0.51f, w = 0.18f, h = 0.1f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("s1", "s2", dashed = true), DEdge("o3", "o1", dashed = true), DEdge("o3", "o2", dashed = true)),
                aspect = 0.6f,
            ),
            B.remember("""
                Auto scale rules are set on the plan, not the app, because the plan owns the workers. A scale-in rule
                should be gentler than the scale-out rule (for example out at 70 percent, in at 30 percent).
            """),
            B.example("""
                Two Tracks gets a burst every evening at 16:15 Qatar time when reminders fire. A schedule rule adds a
                worker at 16:00 and removes it at 18:00, cheaper than reacting to CPU after the spike has started.
            """),
            B.recap(
                "A swap warms up staging, then switches routing; swap back if anything is wrong.",
                "Sticky settings stay with the slot; everything else moves with the code.",
                "Scale up is a bigger worker; scale out is more workers. Auto scale rules live on the plan.",
            ),
        ),
    )

    private val functions = Topic(
        id = "az-functions", title = "Azure Functions: run a piece of code when something happens", minutes = 20,
        blocks = listOf(
            B.plain("""
                A Function is a small piece of code that runs in response to an event: an HTTP request, a timer, a
                new message in a queue, a new blob. You pay per run and do not manage a server.
            """),
            B.h("Triggers and bindings"),
            B.p("""
                Every function has exactly one trigger: the event that starts it. It can also have bindings, which are
                shortcuts to other services so you do not write connection code. An input binding hands you data, an
                output binding takes what you return and writes it somewhere.
            """),
            B.diagram(
                caption = "One trigger starts the function. Input bindings bring data in, output bindings send results out.",
                nodes = listOf(
                    DNode("trig", "Trigger", 0.14f, 0.5f, w = 0.22f, h = 0.2f, tone = 1, sub = "HTTP, timer, queue, blob"),
                    DNode("fn", "Your function", 0.5f, 0.5f, w = 0.28f, h = 0.26f, sub = "the code you write"),
                    DNode("in", "Input binding", 0.5f, 0.12f, w = 0.3f, h = 0.14f, tone = 2, sub = "read a Cosmos doc"),
                    DNode("out", "Output binding", 0.86f, 0.5f, w = 0.24f, h = 0.2f, tone = 2, sub = "write to a queue"),
                ),
                edges = listOf(DEdge("trig", "fn"), DEdge("in", "fn"), DEdge("fn", "out")),
                aspect = 0.6f,
            ),
            B.table(
                listOf("Trigger", "Fires when", "Typical use"),
                listOf("HTTP", "A request arrives", "APIs and webhooks"),
                listOf("Timer", "A CRON schedule ticks", "Nightly clean-up, reminder sweeps"),
                listOf("Queue Storage", "A message lands in a queue", "Background work"),
                listOf("Blob", "A blob is created or changed", "Process an upload"),
                listOf("Cosmos DB", "A document changes (change feed)", "React to data"),
                listOf("Event Grid, Event Hubs, Service Bus", "An event or message arrives", "Integration"),
            ),
            B.h("The CRON format the timer uses"),
            B.p("""
                Azure's timer trigger uses six fields, not five: seconds, minutes, hours, day, month, weekday.
                The exam tests this.
            """),
            B.code("text", """
                0 */5 * * * *     every 5 minutes
                0 0 16 * * 0-4    16:00 on Sunday to Thursday
                0 30 9 * * 1-5    09:30 on weekdays
            """),
            B.h("A function in C#"),
            B.code("csharp", """
                [Function("ReminderSweep")]
                [QueueOutput("reminders")]
                public string Run(
                    [TimerTrigger("0 0 16 * * 0-4")] TimerInfo timer,
                    [CosmosDBInput("twotracks", "sessions",
                        SqlQuery = "SELECT * FROM s WHERE s.date = GetCurrentDateTime()")] IEnumerable<Session> today)
                {
                    // returning a string drops a message on the "reminders" queue
                    return JsonSerializer.Serialize(today);
                }
            """),
            B.p("""
                The attributes are the bindings. The timer is the trigger, the Cosmos query is an input binding, and the
                return value goes to the queue through the output binding. No connection code anywhere.
            """),
            B.h("Hosting plans"),
            B.table(
                listOf("Plan", "Scaling", "Cost", "Watch out"),
                listOf("Consumption", "Automatic, to zero", "Per execution", "Cold starts; 10 minute max run (default 5)"),
                listOf("Flex Consumption", "Automatic, always-ready instances", "Per execution", "Linux only; newer"),
                listOf("Premium", "Automatic, pre-warmed", "Per core-second", "No cold start; VNet; longer runs"),
                listOf("Dedicated (App Service)", "Manual or auto scale", "Plan price", "Use when a plan already exists"),
            ),
            B.tip("""
                "Must not have a cold start" or "needs VNet access" points to Premium. "Cheapest for a function that
                runs a few times a day" points to Consumption. "Runs for 30 minutes" rules out Consumption.
            """),
            B.h("The host file and settings"),
            B.p("""
                host.json configures the whole function app (logging, time-outs, queue batch sizes). local.settings.json
                holds settings for local runs and is never deployed; in Azure the same keys live in app settings. The
                AzureWebJobsStorage setting is required because the runtime uses a storage account for timers and locks.
            """),
            B.remember("""
                One function app can hold many functions, but they all share the same plan, runtime stack and settings.
                Each function has one trigger and any number of bindings.
            """),
            B.example("""
                Two Tracks uses a timer function at 16:00 Sunday to Thursday to find today's sessions and push a
                reminder message per user onto a queue. A second, queue-triggered function sends the notification.
                Both sit in one Consumption function app and cost cents a month.
            """),
            B.recap(
                "One trigger per function; bindings replace connection code for inputs and outputs.",
                "Timer CRON has six fields, starting with seconds.",
                "Consumption scales to zero but has cold starts and a run limit; Premium pre-warms and allows VNet.",
            ),
        ),
    )

    val topics = listOf(appService, slotsAndScaling, functions)
}
