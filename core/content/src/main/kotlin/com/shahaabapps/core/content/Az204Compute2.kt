package com.shahaabapps.core.content

internal object Az204Compute2 {
    private val durable = Topic(
        id = "az-durable", title = "Durable Functions: when one step has to wait for another", minutes = 16,
        blocks = listOf(
            B.plain("""
                Plain functions are stateless and short. Durable Functions let you write a workflow that runs for hours
                or days, remembers where it is, and survives restarts. You write it as ordinary code that looks
                synchronous.
            """),
            B.h("The three kinds of function"),
            B.bullets(
                "Client (starter): any normal function that kicks off an orchestration and gets back an instance id.",
                "Orchestrator: the workflow. It calls activities in order, waits for them, and keeps state. It must be deterministic: no random numbers, no current time, no network calls directly.",
                "Activity: one unit of real work. Call an API, write a row, send an email. This is where the side effects live.",
            ),
            B.diagram(
                caption = "A client starts the orchestrator; the orchestrator calls activities one by one and keeps its place in storage.",
                nodes = listOf(
                    DNode("client", "Client function", 0.14f, 0.3f, w = 0.24f, h = 0.2f, tone = 1, sub = "HTTP trigger"),
                    DNode("orch", "Orchestrator", 0.5f, 0.3f, w = 0.26f, h = 0.22f, sub = "the workflow"),
                    DNode("a1", "Activity: charge card", 0.5f, 0.78f, w = 0.3f, h = 0.16f, tone = 2),
                    DNode("a2", "Activity: send email", 0.86f, 0.78f, w = 0.26f, h = 0.16f, tone = 2),
                    DNode("store", "Task hub (storage)", 0.86f, 0.3f, w = 0.24f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2),
                ),
                edges = listOf(DEdge("client", "orch", label = "start"), DEdge("orch", "a1"), DEdge("orch", "a2"), DEdge("orch", "store", dashed = true, label = "checkpoints")),
                aspect = 0.62f,
            ),
            B.h("Why the orchestrator must be deterministic"),
            B.p("""
                The orchestrator replays from the start every time it wakes up, reading the results of finished
                activities from history instead of running them again. If the code made a different decision on replay,
                the history would not match. So use context.CurrentUtcDateTime instead of DateTime.Now, and put any
                real work inside activities.
            """),
            B.h("Patterns the exam names"),
            B.table(
                listOf("Pattern", "Shape", "Example"),
                listOf("Function chaining", "A then B then C, each using the last result", "Validate, charge, email"),
                listOf("Fan-out/fan-in", "Start many activities in parallel, wait for all", "Resize 100 images"),
                listOf("Async HTTP API", "Start long work, poll a status URL", "Report generation"),
                listOf("Monitor", "Loop with a timer until a condition", "Poll a job until done"),
                listOf("Human interaction", "Wait for an external event with a time-out", "Manager approval"),
            ),
            B.code("csharp", """
                [Function("ApproveLeave")]
                public async Task Run([OrchestrationTrigger] TaskOrchestrationContext ctx)
                {
                    await ctx.CallActivityAsync("RequestApproval", ctx.GetInput<Leave>());
                    using var cts = new CancellationTokenSource();
                    var timeout = ctx.CreateTimer(ctx.CurrentUtcDateTime.AddDays(3), cts.Token);
                    var approval = ctx.WaitForExternalEvent<bool>("Approved");
                    if (await Task.WhenAny(approval, timeout) == approval) { cts.Cancel(); await ctx.CallActivityAsync("Grant", true); }
                    else await ctx.CallActivityAsync("Escalate", true);
                }
            """),
            B.tip("""
                "Wait for a manager to approve within 72 hours" is the human interaction pattern: WaitForExternalEvent
                raced against a durable timer. "Process many items in parallel and combine" is fan-out/fan-in with
                Task.WhenAll.
            """),
            B.remember("""
                Durable timers (ctx.CreateTimer) are the only safe way to wait in an orchestrator. Thread.Sleep or
                Task.Delay would break replay.
            """),
            B.example("""
                Two Tracks could use a monitor pattern to nudge a partner: start when a session is planned, check every
                hour whether it was logged, and after the slot ends send one gentle reminder, then stop.
            """),
            B.recap(
                "Client starts, orchestrator coordinates, activities do the work.",
                "Orchestrators replay, so they must be deterministic; use context time and durable timers.",
                "Know the five patterns by shape: chaining, fan-out/fan-in, async HTTP, monitor, human interaction.",
            ),
        ),
    )

    private val containers = Topic(
        id = "az-containers", title = "Containers on Azure: Registry, Instances and Container Apps", minutes = 18,
        blocks = listOf(
            B.plain("""
                A container is your app plus everything it needs, packed into one image that runs the same anywhere.
                Azure gives you a place to store images, a quick way to run one, and a managed way to run many.
            """),
            B.h("Azure Container Registry (ACR)"),
            B.p("""
                ACR is your private Docker registry. You push images to it and Azure services pull from it. It can also
                build images for you with ACR Tasks, so a laptop without Docker can still ship.
            """),
            B.code("bash", """
                az acr create --name twotracksacr --resource-group tt-rg --sku Basic
                az acr build --registry twotracksacr --image api:1.0 .      # build in the cloud, push automatically
                az acr login --name twotracksacr                            # then docker push works too
            """),
            B.table(
                listOf("SKU", "Storage", "Extras"),
                listOf("Basic", "10 GB", "Learning and small teams"),
                listOf("Standard", "100 GB", "Most production use"),
                listOf("Premium", "500 GB", "Geo-replication, private link, content trust"),
            ),
            B.h("Azure Container Instances (ACI)"),
            B.p("""
                ACI runs a container the moment you ask, billed by the second, with no cluster to create. It is the
                simplest way to run a one-off job, a build agent or a small always-on process. A container group is
                several containers that share a host, a network and storage, like a tiny pod.
            """),
            B.code("bash", """
                az container create --resource-group tt-rg --name nightly-export \
                  --image twotracksacr.azurecr.io/export:1.0 \
                  --restart-policy OnFailure --environment-variables BUCKET=exports \
                  --azure-file-volume-account-name ttfiles --azure-file-volume-share-name out --azure-file-volume-mount-path /out
            """),
            B.bullets(
                "Restart policy: Always for services, OnFailure for jobs that should retry, Never for one-shot tasks.",
                "Persistent files: mount an Azure Files share; container disks are thrown away.",
                "Secrets: pass them as secure environment variables or a secret volume, never baked into the image.",
            ),
            B.h("Azure Container Apps"),
            B.p("""
                Container Apps is the managed home for microservices. It runs on Kubernetes underneath but hides it.
                You get scale to zero, HTTPS ingress, revisions, traffic splitting and Dapr building blocks without
                touching a cluster.
            """),
            B.diagram(
                caption = "Images live in ACR. ACI runs one container quickly. Container Apps runs a set of services with scaling and traffic rules.",
                nodes = listOf(
                    DNode("acr", "Container Registry", 0.5f, 0.14f, w = 0.36f, h = 0.18f, kind = NodeKind.CYLINDER, tone = 1, sub = "api:1.0, export:1.0"),
                    DNode("aci", "Container Instances", 0.2f, 0.62f, w = 0.32f, h = 0.26f, tone = 2, sub = "one job, by the second"),
                    DNode("aca", "Container Apps", 0.72f, 0.62f, w = 0.4f, h = 0.3f, sub = "revisions, scale 0 to N, ingress"),
                    DNode("rev", "rev-1  70%  |  rev-2  30%", 0.72f, 0.9f, w = 0.4f, h = 0.1f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("acr", "aci", label = "pull"), DEdge("acr", "aca", label = "pull")),
                aspect = 0.66f,
            ),
            B.h("Revisions and traffic"),
            B.p("""
                Each deployment creates a revision. In single revision mode the new one replaces the old. In multiple
                revision mode you can keep both and send, say, 30 percent of traffic to the new one. Revisions are how
                Container Apps does the blue-green and canary releases that App Service does with slots.
            """),
            B.h("Scaling"),
            B.p("""
                Scale rules use KEDA: HTTP concurrency (the default), CPU, memory, or an event source such as a Service
                Bus queue length. Set minimum replicas to 0 to pay nothing while idle, or to 1 to avoid cold starts.
            """),
            B.code("bash", """
                az containerapp create --name tt-api --resource-group tt-rg --environment tt-env \
                  --image twotracksacr.azurecr.io/api:1.0 --target-port 8080 --ingress external \
                  --min-replicas 0 --max-replicas 5 --scale-rule-name http --scale-rule-type http --scale-rule-http-concurrency 50
            """),
            B.tip("""
                Pick ACI for "run this container now with no orchestration". Pick Container Apps for "microservices,
                scale to zero, revisions, Dapr". Pick AKS only when the question demands full Kubernetes control.
            """),
            B.remember("""
                Container Apps secrets are defined on the app and referenced by environment variables. Managed identity
                lets the app pull from ACR with no password at all.
            """),
            B.example("""
                Two Tracks ships its API image to ACR with an ACR Task on every push. Container Apps runs it with
                min 1 replica (no cold start at 16:15) and max 5. The nightly export is an ACI container group that
                writes to an Azure Files share and exits.
            """),
            B.recap(
                "ACR stores images and can build them in the cloud with ACR Tasks.",
                "ACI runs a container or a group by the second; restart policy and Azure Files mounts matter.",
                "Container Apps adds revisions, traffic splitting, KEDA scaling and scale to zero without managing Kubernetes.",
            ),
        ),
    )

    val topics = listOf(durable, containers)

    val quiz = Quiz(
        id = "az-compute-test", title = "Compute module test",
        questions = listOf(
            B.q("c1", "az-app-service", "Which setting belongs to the App Service plan rather than the web app?",
                listOf("Custom domain", "Pricing tier", "App settings", "Deployment slots"), setOf(1),
                "The plan owns the hardware: region, operating system, pricing tier and instance count. Custom domains, settings and slots belong to the app."),
            B.q("c2", "az-app-service", "You need the cheapest App Service tier that supports deployment slots. Which do you choose?",
                listOf("Free", "Basic", "Standard", "Premium"), setOf(2),
                "Deployment slots and auto scale start at Standard. Basic gives dedicated workers but no slots."),
            B.q("c3", "az-app-service", "What happens when you change an app setting on an App Service web app?",
                listOf("Nothing until the next deployment", "The app restarts and picks up the new value", "Only new requests see it; running requests keep the old value", "The setting is queued for the next swap"), setOf(1),
                "App settings are injected as environment variables at start-up, so a change triggers a restart."),
            B.q("c4", "az-slots-scaling", "After a swap, production must keep using the production database connection string. What do you do before swapping?",
                listOf("Delete the connection string from staging", "Mark the connection string as a deployment slot setting", "Restart the production slot", "Scale out the production slot"), setOf(1),
                "A deployment slot setting is sticky: it stays with the slot instead of moving with the code during a swap."),
            B.q("c5", "az-slots-scaling", "Which items move with the code during a slot swap? Select all that apply.",
                listOf("General app settings", "Custom domain names", "Handler mappings", "Scale settings"), setOf(0, 2),
                "General settings and handler mappings travel with the code. Custom domains and scale settings stay with the slot."),
            B.q("c6", "az-slots-scaling", "Your app spikes every weekday at 09:00. What is the most cost-effective way to be ready?",
                listOf("Scale up to Premium permanently", "A schedule-based auto scale rule that adds workers before 09:00", "A CPU-based rule only", "Enable Always On"), setOf(1),
                "Schedule rules add capacity before a known spike. A CPU rule reacts after the spike has begun. Scaling up permanently costs all day."),
            B.q("c7", "az-slots-scaling", "Auto scale rules for an App Service web app are configured on which resource?",
                listOf("The web app", "The App Service plan", "The resource group", "The deployment slot"), setOf(1),
                "The plan owns the workers, so auto scale is a plan setting that applies to every app on it."),
            B.q("c8", "az-functions", "How many triggers can a single Azure Function have?",
                listOf("Exactly one", "One per binding", "Up to three", "Unlimited"), setOf(0),
                "Each function has exactly one trigger and any number of input and output bindings."),
            B.q("c9", "az-functions", "Which CRON expression runs a timer trigger at 16:00 on Sunday to Thursday?",
                listOf("0 16 * * 0-4", "0 0 16 * * 0-4", "16 0 * * 0-4 *", "0 0 16 * * 1-5"), setOf(1),
                "Azure timer triggers use six fields starting with seconds: 0 0 16 * * 0-4. Weekday 0 is Sunday, 4 is Thursday."),
            B.q("c10", "az-functions", "A function must run for 25 minutes and must not suffer cold starts. Which hosting plan fits?",
                listOf("Consumption", "Premium", "Flex Consumption with the default time-out", "Any plan works"), setOf(1),
                "Consumption caps runs at 10 minutes and has cold starts. Premium keeps instances warm and allows longer runs."),
            B.q("c11", "az-functions", "Which file holds settings used only when running a function app locally and is never deployed?",
                listOf("host.json", "function.json", "local.settings.json", "appsettings.json"), setOf(2),
                "local.settings.json is for local runs. In Azure the same keys are app settings. host.json configures the whole app and is deployed."),
            B.q("c12", "az-durable", "Which rule applies to a Durable Functions orchestrator?",
                listOf("It may call external APIs directly", "It must be deterministic because it replays", "It runs at most five minutes", "It cannot call more than one activity"), setOf(1),
                "Orchestrators replay from history on every wake-up, so the code must take the same path each time. Real work goes in activities."),
            B.q("c13", "az-durable", "A workflow must wait up to 72 hours for a manager to approve, then continue or escalate. Which pattern is this?",
                listOf("Function chaining", "Fan-out/fan-in", "Monitor", "Human interaction"), setOf(3),
                "Human interaction waits for an external event (the approval) raced against a durable timer (the 72-hour time-out).",
                scenario = "Two Tracks wants Sanya to approve any change Usaid makes to the shared level dates."),
            B.q("c14", "az-durable", "Inside an orchestrator, what is the safe way to wait for a period of time?",
                listOf("Thread.Sleep", "Task.Delay", "A durable timer from the orchestration context", "A timer-triggered activity"), setOf(2),
                "Only durable timers (context.CreateTimer) are replay-safe. Sleep and Delay break the replay model."),
            B.q("c15", "az-containers", "You need to run a one-off container job that writes its output to shared storage, with no cluster to manage. Which service?",
                listOf("Azure Kubernetes Service", "Azure Container Instances with an Azure Files mount", "App Service", "Azure Container Registry"), setOf(1),
                "ACI runs a container by the second with no orchestration, and an Azure Files share gives it persistent output."),
            B.q("c16", "az-containers", "Which Container Apps feature sends 30 percent of traffic to a new version while keeping the old one?",
                listOf("Deployment slots", "Multiple revision mode with traffic splitting", "Scale rules", "Dapr"), setOf(1),
                "Revisions are Container Apps' answer to slots. In multiple revision mode you split traffic between them."),
            B.q("c17", "az-containers", "Which ACR tier adds geo-replication?",
                listOf("Basic", "Standard", "Premium", "All tiers"), setOf(2),
                "Geo-replication, private link and content trust are Premium features."),
            B.q("c18", "az-containers", "Which ACI restart policy suits a batch job that should retry only if it crashes?",
                listOf("Always", "OnFailure", "Never", "Manual"), setOf(1),
                "OnFailure restarts the container only when it exits with an error. Always is for services; Never is for strict one-shot tasks."),
        ),
    )
}
