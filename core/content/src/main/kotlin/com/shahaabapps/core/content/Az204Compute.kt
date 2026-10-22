package com.shahaabapps.core.content

/**
 * AZ-204 module 1, Compute, topics 1 to 3. Each topic follows the richer
 * template: plain words, concept, diagram, worked example on Two Tracks,
 * step-by-step walkthrough, compare cards where the exam tests a choice,
 * common mistakes, an inline check, try it, glossary, recap, references.
 */
internal object Az204Compute {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val appService = Topic(
        id = "az-app-service", title = "App Service: hosting a web app without a server to manage", minutes = 28,
        references = listOf(
            B.ref("App Service overview", LEARN + "azure/app-service/overview"),
            B.ref("App Service plans", LEARN + "azure/app-service/overview-hosting-plans"),
            B.ref("Configure an App Service app (app settings)", LEARN + "azure/app-service/configure-common"),
            B.ref("Enable diagnostics logging", LEARN + "azure/app-service/troubleshoot-diagnostic-logs"),
            B.ref("Learn module: Explore Azure App Service", LEARN + "training/modules/introduction-to-azure-app-service/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-app-service-w1", "az-app-service",
                "Two web apps are running in Azure and one needs far more instances than the other. What do you think has to be separate?",
                listOf("The plan underneath them", "Their app settings", "Their deployment slots"), 0,
                "Instance count turns out to belong to the thing underneath the app, not the app.",
            ),
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
            B.p("Every App Service app has two parts, and the exam loves to ask which setting belongs to which."),
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
            ),
            B.check("az-app-service-k1", "az-app-service", "Two apps need different instance counts. What do you need?",
                listOf("Two web apps on one plan", "Two plans", "One app with two slots"), 1,
                "Instance count lives on the plan, so apps that must scale differently need separate plans."),
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
            B.worked("""
                Worked example. The Two Tracks API reads Firestore__ProjectId. In appsettings.json it is "two-tracks-dev".
                In Azure you set the same key as an app setting with the value "two-tracks". Because ASP.NET maps a
                double underscore to a colon, Configuration["Firestore:ProjectId"] now returns "two-tracks" with no code
                change. The app restarts once when you save the setting, then every request sees the new value.
            """),
            B.faded(
                "az-app-service-f1",
                "Same problem, a different key. The API reads Configuration[\"Cosmos:Database\"] and production needs a different value from the one in appsettings.json.",
                listOf(
                    "In appsettings.json the key is written Cosmos:Database.",
                    "In Azure, add an app setting named ___ with the production value.",
                    "Save it. The app restarts once, and from then on every request sees the new value.",
                ),
                blank = 1,
                answers = listOf("Cosmos__Database"),
                note = "A double underscore stands in for the colon, because a colon is not legal in an environment variable name on Linux.",
            ),
            B.code("bash", """
                # Set a setting with the CLI; the app reads it as an environment variable
                az webapp config appsettings set \
                  --name twotracks-api --resource-group tt-rg \
                  --settings Firestore__ProjectId=two-tracks Reminder__LeadMinutes=15

                # Read it back from inside the container (Linux apps)
                az webapp ssh --name twotracks-api --resource-group tt-rg
                $ printenv Firestore__ProjectId
                two-tracks
            """),
            B.remember("""
                Changing an app setting restarts the app. Settings are per slot unless you mark them as
                "deployment slot setting", in which case they stick to the slot.
            """),
            B.steps("Create the API in the portal, step by step",
                "Create a resource, choose Web App." to "The create form opens.",
                "Name twotracks-api, publish Code, runtime .NET 8, OS Linux, region West Europe." to "A URL twotracks-api.azurewebsites.net is reserved.",
                "Under Pricing plan create a new plan tt-plan, size Standard S1." to "S1 gives slots and auto scale.",
                "Review and create, then open the resource." to "Overview shows Running and the URL.",
                "Configuration, Application settings, add Firestore__ProjectId." to "Save warns that the app will restart.",
                "Deployment Center, pick GitHub and the repo branch." to "A workflow file is committed and the first run deploys.",
            ),
            B.check(
                "az-app-service-k3", "az-app-service",
                "You change an app setting in the portal. What happens to the running app?",
                listOf("It restarts and starts using the new value", "Nothing until the next deployment", "The value reaches only new instances"), 0,
                "App settings are injected as environment variables at start-up, so changing one restarts the app.",
                whyWrong = mapOf(
                    1 to "No deployment is needed: settings live outside the code, and the platform restarts the app itself.",
                    2 to "Every instance restarts, not only ones started later.",
                ),
            ),
            B.h("Logging and diagnostics"),
            B.p("""
                App Service can write application logs (what your code prints), web server logs, detailed error pages
                and failed request traces. Application logging to the file system switches itself off after 12 hours
                so it never fills the disk by accident. For anything long-term, send logs to a storage account or to
                Application Insights, which the Monitor module covers.
            """),
            B.code("bash", """
                az webapp log config --name twotracks-api --resource-group tt-rg --application-logging filesystem --level information
                az webapp log tail --name twotracks-api --resource-group tt-rg
            """),
            B.check(
                "az-app-service-k2", "az-app-service",
                "An app sits idle overnight and the first request each morning is slow. What fixes that?",
                listOf("Always On, which the Basic tier and above offer", "A larger instance size", "A second deployment slot"), 0,
                "Always On keeps the app loaded, so the platform is not starting it up on the first request after idle.",
                whyWrong = mapOf(
                    1 to "A bigger worker makes a loaded app faster. It does not stop an unloaded one having to start.",
                    2 to "A slot is a second copy for staging and swapping. It has no effect on a cold start in production.",
                ),
            ),
            B.h("Where the code comes from"),
            B.bullets(
                "Push a zip, a Git repo, or a GitHub Actions workflow. Continuous deployment watches a branch and redeploys on every push.",
                "Run a container from Azure Container Registry or Docker Hub. App Service pulls the image and runs it.",
                "Deployment Center in the portal wires all of this up with a few clicks.",
            ),
            B.mistakes(
                "Putting secrets in appsettings.json and committing them. Use app settings or Key Vault references.",
                "Expecting slots on the Basic tier. Slots start at Standard.",
                "Forgetting that a settings change restarts the app, and doing it at 16:15 when reminders fire.",
                "Reading Windows-style settings on Linux: on Linux the key is exactly as typed, case-sensitive.",
            ),
            B.example("""
                For Two Tracks, the API lives in one Standard Linux plan with two instances. The admin site shares
                the plan because it is tiny. Both read their Firebase settings from app settings, never from code.
            """),
            B.h("Check yourself"),
            B.match(
                "az-c-appsvc-match", "Each symptom has one fix. Pair them.",
                "Secrets sitting in appsettings.json" to "Move them to app settings or a Key Vault reference",
                "The first request after an idle hour is slow" to "Turn on Always On, from Basic upwards",
                "You need a staging copy to swap into production" to "Move the plan to Standard, where slots begin",
                "A setting reads as null on Linux but worked on Windows" to "Match the key's case exactly",
            ),
            B.gap(
                "az-c-appsvc-gap1", "Stream the log live from the shell.",
                "az webapp log ___ --name twotracks-api --resource-group tt-rg", "tail",
            ),
            B.gap(
                "az-c-appsvc-gap2", "Filesystem application logging turns itself off so it cannot fill the disk.",
                "Logging to the file system stops on its own after ___ hours.", "12", "twelve",
            ),
            B.tryIt("Try it in 15 minutes", "On a free Azure account, create a Linux web app on the Free tier, set one app setting, and watch the restart in the log stream.",
                "Create web app, runtime .NET 8, Linux, Free F1", "Add app setting Hello=World and save", "Open Log stream and confirm the restart lines", "Browse the URL and see the default page", "Delete the resource group when done",
            ),
            B.explain(
                "az-app-service-x1",
                "A colleague asks what the difference is between an App Service plan and a web app, and when they would need a second plan. Answer in your own words.",
                "A plan is the compute: the region, the operating system, the pricing tier and how many instances run. A web app is the code and its own settings, domains and slots, running on a plan. Several apps can share one plan, which means they share its instances, so two apps that have to scale differently need two plans.",
                "The plan is the hardware and tier" to listOf("plan", "compute", "hardware", "tier", "instance"),
                "The app is the code and its settings" to listOf("app is", "web app", "code", "settings"),
                "Apps on one plan share its instances" to listOf("share", "shared", "same plan", "together"),
            ),
            B.glossary(
                "App Service plan" to "The compute behind one or more web apps: region, OS, tier, instance count.",
                "Web app" to "Your code and its settings, domains and slots, running on a plan.",
                "App setting" to "A key-value pair injected as an environment variable at start-up.",
                "Deployment slot" to "A second live copy of the app on the same plan, for staging and swapping.",
                "Always On" to "Keeps the app loaded so the first request after idle is not slow; Basic and above.",
            ),
            B.recap(
                "A plan is the hardware and tier; a web app is your code and its settings. Several apps can share a plan.",
                "Slots and auto scale need at least the Standard tier.",
                "App settings become environment variables and changing them restarts the app.",
            ),
        ),
    )

    private val slotsAndScaling = Topic(
        id = "az-slots-scaling", title = "Deployment slots and scaling: ship safely and handle the crowd", minutes = 26,
        references = listOf(
            B.ref("Set up staging environments (deployment slots)", LEARN + "azure/app-service/deploy-staging-slots"),
            B.ref("Scale up an app", LEARN + "azure/app-service/manage-scale-up"),
            B.ref("Get started with autoscale", LEARN + "azure/azure-monitor/autoscale/autoscale-get-started"),
            B.ref("Autoscale best practices", LEARN + "azure/azure-monitor/autoscale/autoscale-best-practices"),
            B.ref("Learn module: Scale apps in Azure App Service", LEARN + "training/modules/scale-apps-app-service/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-slots-scaling-w1", "az-slots-scaling",
                "You deploy to a staging slot and swap it into production. Which do you think moves across with the code?",
                listOf("Everything except settings you deliberately pin to the slot", "Everything, with no exceptions", "Only the code; all settings stay behind"), 0,
                "There is a way to make one setting stay put, and the exam leans on it.",
            ),
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
            B.steps("Ship version 2 with zero downtime",
                "Deployment slots, Add slot named staging, clone settings from production." to "A URL twotracks-api-staging.azurewebsites.net appears.",
                "Deploy v2 to the staging slot (CLI: az webapp deploy --slot staging)." to "Staging shows v2; production still v1.",
                "Open the staging URL and run the smoke test." to "Health check returns 200 and the new endpoint answers.",
                "Mark the production database connection string as a deployment slot setting." to "A lock icon shows beside it.",
                "Swap, source staging, target production, review the settings diff." to "The diff shows only values that will move.",
                "Confirm the swap." to "Production answers with v2 within seconds; staging now holds v1.",
                "If errors rise, swap again." to "Production is back on v1 with no redeploy.",
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
            B.check("az-slots-scaling-k1", "az-slots-scaling", "After a swap, which of these is now answering the production URL?",
                listOf("The workers that were serving staging", "The same workers as before, with new code copied in", "A fresh set of workers"), 0,
                "A swap changes routing, not files. The staging workers, already warmed up, start answering the production hostname."),
            B.check(
                "az-slots-scaling-k2", "az-slots-scaling",
                "A connection string on the staging slot is marked as a deployment slot setting. After a swap, which slot has that value?",
                listOf("Staging still", "Production, having travelled with the code", "Neither; the swap clears it"), 0,
                "A sticky setting stays with its slot. Everything not marked sticky moves with the code.",
                whyWrong = mapOf(
                    1 to "That is what an unmarked setting does. Marking it a slot setting is precisely what stops it moving.",
                    2 to "A swap clears nothing. The value stays on the slot it is stuck to.",
                ),
            ),
            B.h("Testing in production"),
            B.worked("""
                Worked example. Two Tracks wants 10 percent of real users on v2 for an hour. In Deployment slots, set
                Traffic percent for staging to 10. A user who lands on staging gets a cookie x-ms-routing-name=staging
                and stays there. To force yourself onto v2 for testing, open the production URL with
                ?x-ms-routing-name=staging. After the hour, check failed requests per slot in Application Insights, then
                set traffic back to 0 and swap if clean.
            """),
            B.faded(
                "az-slots-scaling-f1",
                "Same problem, a different split. You want a quarter of real users on v3 for an afternoon.",
                listOf(
                    "In Deployment slots, set Traffic percent for staging to 25.",
                    "To put your own browser on staging without moving anyone, open the production URL with ?___=staging",
                    "If the failed requests stay flat, set traffic back to 0 and swap.",
                ),
                blank = 1,
                answers = listOf("x-ms-routing-name"),
                note = "The same name arrives as a cookie for a user who lands on staging, which is what keeps them there rather than bouncing between versions.",
            ),
            B.h("Scaling up versus scaling out"),
            B.compare("Which scale?",
                B.card("Scale up", "Each request needs more CPU or memory, or you need a feature of a higher tier.",
                    "Bigger worker: more cores and RAM", "Changes the pricing tier", "Short restart", "Does not help if the app is single-threaded and already fast"),
                B.card("Scale out", "More users at once, or you want resilience when one worker restarts.",
                    "More workers behind one URL", "Same tier, pay per instance", "App must be stateless or share state (cache, database)", "Can be automatic from Standard"),
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
            B.code("bash", """
                # Schedule rule: 3 workers from 16:00 to 18:00 Qatar time on study days, otherwise 1
                az monitor autoscale create --resource-group tt-rg --resource tt-plan \
                  --resource-type Microsoft.Web/serverfarms --name tt-autoscale --min-count 1 --max-count 3 --count 1
                az monitor autoscale profile create --autoscale-name tt-autoscale --resource-group tt-rg \
                  --name study-hours --count 3 --timezone "Arab Standard Time" --start 16:00 --end 18:00 --recurrence week sun mon tue wed thu
            """),
            B.remember("""
                Auto scale rules are set on the plan, not the app, because the plan owns the workers. A scale-in rule
                should be gentler than the scale-out rule (for example out at 70 percent, in at 30 percent).
            """),
            B.mistakes(
                "Scaling out an app that keeps session state in memory. Users bounce between workers and lose their session.",
                "Using the same threshold to scale out and in. The rule oscillates; leave a gap.",
                "Setting slot settings after the swap. Mark them before, or the first swap leaks the staging value.",
                "Swapping with a cold staging slot. Let the warm-up finish; auto swap waits for it.",
            ),
            B.example("""
                Two Tracks gets a burst every evening at 16:15 Qatar time when reminders fire. A schedule rule adds a
                worker at 16:00 and removes it at 18:00, cheaper than reacting to CPU after the spike has started.
            """),
            B.check(
                "az-slots-scaling-k3", "az-slots-scaling",
                "Traffic has doubled, and each request is the same amount of work as before. Scale up or scale out?",
                listOf("Scale out: more instances", "Scale up: a bigger worker", "Neither; lengthen the cool-down"), 0,
                "More of the same work means more workers. Scaling up is for work that needs a bigger machine, not more of them.",
                whyWrong = mapOf(
                    1 to "A bigger worker helps when one request needs more CPU or memory, not when there are simply more requests.",
                    2 to "Cool-down only limits how often a rule may act. It adds no capacity at all.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-c-slots-match", "Pair each term with what it actually does.",
                "Scale up" to "A bigger worker: more cores and memory, and a new tier",
                "Scale out" to "More workers behind the one URL, same tier",
                "Swap" to "Routing changes so the warmed staging workers answer production",
                "Traffic percent" to "A share of real users pinned to staging by a cookie",
            ),
            B.gap(
                "az-c-slots-gap1", "Force your own browser onto the staging build without moving any users.",
                "Open the production URL with ?x-ms-routing-name=___", "staging",
            ),
            B.gap(
                "az-c-slots-gap2", "The question says production must keep using the production database after the swap.",
                "Mark the connection string as a deployment ___ setting so it stays put.", "slot",
            ),
            B.reorder("az-c-slots-re1", "Build the command that deploys to the staging slot.", "az webapp deploy --slot staging"),
            B.tryIt("Try it in 15 minutes", "Needs a Standard S1 plan, which the free credit covers for an hour. Delete it after.",
                "Add a slot named staging to a web app", "Change the app title in staging only", "Swap and confirm production shows the new title", "Swap back", "Delete the plan",
            ),
            B.explain(
                "az-slots-scaling-x1",
                "Explain what happens during a slot swap, and why a setting might not move with the code.",
                "A swap warms the staging slot first, then switches which slot answers the production hostname, so users never meet a cold instance. Everything travels with the code except settings marked as deployment slot settings: those are sticky and stay with their slot, which is how a staging connection string stays pointed at the staging database.",
                "Staging is warmed before the switch" to listOf("warm", "warms", "warmed", "pre-warm", "loads"),
                "Routing changes, not the code" to listOf("routing", "hostname", "production url", "switch", "swaps which"),
                "Sticky settings stay with the slot" to listOf("sticky", "slot setting", "stays", "stay with"),
            ),
            B.glossary(
                "Swap" to "Exchange which slot answers the production hostname, after warming the source.",
                "Sticky setting" to "A deployment slot setting that stays with its slot during a swap.",
                "Auto swap" to "Swap automatically when a deployment to the slot finishes.",
                "Scale rule" to "A condition on the plan that adds or removes instances.",
                "Cool-down" to "Minimum time between two scale actions so the rule does not flap.",
            ),
            B.recap(
                "A swap warms up staging, then switches routing; swap back if anything is wrong.",
                "Sticky settings stay with the slot; everything else moves with the code.",
                "Scale up is a bigger worker; scale out is more workers. Auto scale rules live on the plan.",
            ),
        ),
    )

    private val functions = Topic(
        id = "az-functions", title = "Azure Functions: run a piece of code when something happens", minutes = 30,
        references = listOf(
            B.ref("Azure Functions overview", LEARN + "azure/azure-functions/functions-overview"),
            B.ref("Triggers and bindings", LEARN + "azure/azure-functions/functions-triggers-bindings"),
            B.ref("Timer trigger (NCRONTAB format)", LEARN + "azure/azure-functions/functions-bindings-timer"),
            B.ref("Hosting options", LEARN + "azure/azure-functions/functions-scale"),
            B.ref("host.json reference", LEARN + "azure/azure-functions/functions-host-json"),
            B.ref("Learn module: Explore Azure Functions", LEARN + "training/modules/explore-azure-functions/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-functions-w1", "az-functions",
                "A function reads a message from a queue and writes a row to a table. How many things do you think start it?",
                listOf("One", "Two", "As many as it talks to"), 0,
                "One of those two connections starts the function. The other is just a connection.",
            ),
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
                aspect = 1.05f,
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
            B.check("az-functions-k1", "az-functions", "A function reads a Cosmos document and writes to a queue. How many triggers does it have?",
                listOf("Two", "One", "Three"), 1,
                "Exactly one trigger starts a function. The Cosmos read is an input binding and the queue write an output binding."),
            B.h("The CRON format the timer uses"),
            B.p("Azure's timer trigger uses six fields, not five: seconds, minutes, hours, day, month, weekday. The exam tests this."),
            B.code("text", """
                0 */5 * * * *     every 5 minutes
                0 0 16 * * 0-4    16:00 on Sunday to Thursday
                0 30 9 * * 1-5    09:30 on weekdays
                0 0 0 1 * *       midnight on the 1st of every month
            """),
            B.check(
                "az-functions-k2", "az-functions",
                "What does the NCRONTAB expression 0 */5 * * * * do?",
                listOf("Runs every five minutes", "Runs every five seconds", "Runs at five past every hour"), 0,
                "Six fields, beginning with seconds: at second zero, every fifth minute.",
                whyWrong = mapOf(
                    1 to "Every five seconds puts the step in the first field: */5 * * * * *.",
                    2 to "Five past the hour is 0 5 * * * * — a fixed minute, with no step.",
                ),
            ),
            B.h("A function in C#"),
            B.worked("""
                Worked example. Two Tracks needs a reminder sweep at 16:00 Sunday to Thursday. The timer is the trigger,
                a Cosmos query is an input binding that hands in today's sessions, and the return value is dropped on a
                queue by an output binding. There is no connection code at all; the names of the connection settings
                come from app settings.
            """),
            B.faded(
                "az-functions-f1",
                "Same shape, a different job. A cleanup must run at 02:00 daily, read yesterday's rows and drop the ones to delete on a queue.",
                listOf(
                    "A timer starts the function at 02:00. That is the trigger, and there is only ever one.",
                    "Yesterday's rows are handed in by an input ___, so no connection code is written.",
                    "The rows to delete are returned, and an output binding of the same kind puts them on the queue.",
                ),
                blank = 1,
                answers = listOf("binding"),
                note = "One trigger, any number of bindings. The connection settings they use are named in app settings, not in code.",
            ),
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
            B.steps("Create and deploy it with the CLI",
                "func init tt-functions --worker-runtime dotnet-isolated" to "A project with host.json and local.settings.json.",
                "func new --template \"Timer trigger\" --name ReminderSweep" to "ReminderSweep.cs with a CRON placeholder.",
                "Set AzureWebJobsStorage in local.settings.json to UseDevelopmentStorage=true and start Azurite." to "The timer can take its lock locally.",
                "func start" to "The console prints the next five run times.",
                "az functionapp create --consumption-plan-location westeurope --runtime dotnet-isolated --functions-version 4 --name tt-functions --storage-account ttfuncsa --resource-group tt-rg" to "A Consumption app with its storage account.",
                "func azure functionapp publish tt-functions" to "The functions list prints with their trigger types.",
            ),
            B.h("Hosting plans"),
            B.compare("Which plan?",
                B.card("Consumption", "Runs rarely or in bursts; cost matters most.",
                    "Scales to zero; pay per execution and GB-seconds", "Cold start after idle", "Max run 10 minutes (default 5)", "No VNet"),
                B.card("Premium", "No cold starts, long runs, or private networking.",
                    "Pre-warmed instances", "Runs up to 30 minutes by default, unbounded if configured", "VNet integration", "Pay per core-second even when idle (minimum one instance)"),
                B.card("Dedicated (App Service)", "A plan already exists and has spare capacity.",
                    "Runs on your App Service plan", "Manual or auto scale like a web app", "Always On needed for timers", "Cost is the plan, not executions"),
            ),
            B.p("Flex Consumption is the newer Linux-only consumption plan with always-ready instances to soften cold starts; treat it as Consumption plus a warm pool."),
            B.tip("""
                "Must not have a cold start" or "needs VNet access" points to Premium. "Cheapest for a function that
                runs a few times a day" points to Consumption. "Runs for 30 minutes" rules out Consumption.
            """),
            B.check(
                "az-functions-k3", "az-functions",
                "Which of these is true of the Consumption plan?",
                listOf("It scales to zero, so the first call after idle may cold start", "It keeps instances pre-warmed, so there is no cold start", "It lets a single run take as long as it needs"), 0,
                "Consumption scales to zero and bills per execution. The price is a cold start and a limit on how long one run may take.",
                whyWrong = mapOf(
                    1 to "Pre-warmed instances are the Premium plan, which exists largely to answer cold starts.",
                    2 to "Consumption caps a single run. Premium and Dedicated are what lift that limit.",
                ),
            ),
            B.h("The host file and settings"),
            B.p("""
                host.json configures the whole function app (logging, time-outs, queue batch sizes). local.settings.json
                holds settings for local runs and is never deployed; in Azure the same keys live in app settings. The
                AzureWebJobsStorage setting is required because the runtime uses a storage account for timers and locks.
            """),
            B.code("json", """
                {
                  "version": "2.0",
                  "functionTimeout": "00:08:00",
                  "extensions": { "queues": { "batchSize": 16, "maxDequeueCount": 5 } },
                  "logging": { "logLevel": { "Function.ReminderSweep": "Information" } }
                }
            """),
            B.mistakes(
                "Writing a five-field CRON. Azure timers need six fields and the first is seconds.",
                "Running a 20-minute job on Consumption. It is killed at 10 minutes; use Premium or Durable Functions.",
                "Committing local.settings.json. It holds secrets and is for local runs only.",
                "Expecting a timer to fire on a Dedicated plan without Always On. The host sleeps when idle.",
            ),
            B.remember("""
                One function app can hold many functions, but they all share the same plan, runtime stack and settings.
                Each function has one trigger and any number of bindings.
            """),
            B.example("""
                Two Tracks uses a timer function at 16:00 Sunday to Thursday to find today's sessions and push a
                reminder message per user onto a queue. A second, queue-triggered function sends the notification.
                Both sit in one Consumption function app and cost cents a month.
            """),
            B.h("Check yourself"),
            B.match(
                "az-c-func-match", "Pair each piece with its job.",
                "Trigger" to "The one event that starts the function",
                "Input binding" to "Hands data in with no connection code",
                "Output binding" to "Writes what you return to another service",
                "host.json" to "Settings shared by every function in the app",
            ),
            B.gap(
                "az-c-func-gap1", "Azure's timer uses six fields where most CRON uses five.",
                "Seconds, minutes, hours, day, month and ___.", "weekday", "day of week", "day-of-week",
            ),
            B.gap(
                "az-c-func-gap2", "However many bindings a function has, this number does not change.",
                "Every function has exactly ___ trigger.", "one", "1",
            ),
            B.reorder("az-c-func-re1", "Build the command that publishes the project to Azure.", "func azure functionapp publish tt-functions"),
            B.tryIt("Try it in 15 minutes", "Free tier is enough; a Consumption app has a free monthly grant.",
                "Create a Consumption function app in the portal", "Add an HTTP trigger function with the template", "Call it from the browser with ?name=you", "Add a timer function with 0 */1 * * * * and watch Monitor show runs every minute", "Delete the resource group",
            ),
            B.explain(
                "az-functions-x1",
                "Explain the difference between a trigger and a binding, and what the Consumption plan trades away.",
                "A trigger is the one event that starts a function, and every function has exactly one. Bindings are declared connections to other services for input or output, so there is no connection code to write. Consumption scales to zero and bills per execution, and pays for that with cold starts and a limit on how long one run may take; Premium keeps instances warm and allows VNet access.",
                "One trigger starts the function" to listOf("one trigger", "single trigger", "starts", "event"),
                "Bindings replace connection code" to listOf("binding", "bindings", "input", "output", "no connection code"),
                "Consumption trades cold starts for cost" to listOf("cold start", "scales to zero", "scale to zero", "per execution"),
            ),
            B.glossary(
                "Trigger" to "The one event that starts a function.",
                "Binding" to "A declared connection to a service for input or output, with no connection code.",
                "NCRONTAB" to "The six-field CRON format Azure timers use, starting with seconds.",
                "Cold start" to "The delay when a Consumption instance has to load before the first run.",
                "Function app" to "The unit of deployment: many functions sharing a plan, runtime and settings.",
            ),
            B.recap(
                "One trigger per function; bindings replace connection code for inputs and outputs.",
                "Timer CRON has six fields, starting with seconds.",
                "Consumption scales to zero but has cold starts and a run limit; Premium pre-warms and allows VNet.",
            ),
        ),
    )

    val topics = listOf(appService, slotsAndScaling, functions)
}
