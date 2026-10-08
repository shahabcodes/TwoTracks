package com.shahaabapps.core.content

/** AZ-204 module 1, Compute, topics 4 and 5, plus the module test. */
internal object Az204Compute2 {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val durable = Topic(
        id = "az-durable", title = "Durable Functions: when one step has to wait for another", minutes = 26,
        references = listOf(
            B.ref("Durable Functions overview and patterns", LEARN + "azure/azure-functions/durable/durable-functions-overview"),
            B.ref("Orchestrator function code constraints", LEARN + "azure/azure-functions/durable/durable-functions-code-constraints"),
            B.ref("Timers in Durable Functions", LEARN + "azure/azure-functions/durable/durable-functions-timers"),
            B.ref("Handling external events", LEARN + "azure/azure-functions/durable/durable-functions-external-events"),
            B.ref("Learn module: Implement Durable Functions", LEARN + "training/modules/implement-durable-functions/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-durable-w1", "az-durable",
                "A workflow waits three days for a human to approve something. What do you think the waiting code is doing for those three days?",
                listOf("Not running at all; it replays from the start when it wakes", "Sitting in memory, sleeping", "Polling the approval store on a timer"), 0,
                "This is why one ordinary line of C# is forbidden inside that code.",
            ),
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
            ),
            B.h("Why the orchestrator must be deterministic"),
            B.p("""
                The orchestrator replays from the start every time it wakes up, reading the results of finished
                activities from history instead of running them again. If the code made a different decision on replay,
                the history would not match. So use context.CurrentUtcDateTime instead of DateTime.Now, and put any
                real work inside activities.
            """),
            B.worked("""
                Worked example. An orchestrator calls Activity A, then sleeps 3 days, then calls B. After A finishes the
                host may unload. Three days later the timer fires, the orchestrator replays from line one: A is "called"
                again but its result comes from history in a millisecond, the timer is already satisfied, and the code
                reaches B for the first time. If line one had used Guid.NewGuid() to name the order, replay would produce
                a different id and the history check would fail with a non-determinism error.
            """),
            B.faded(
                "az-durable-f1",
                "Same trap, a different line. An orchestrator needs to wait five minutes between two activities.",
                listOf(
                    "Thread.Sleep would block the host, and Task.Delay would not survive the orchestrator being unloaded.",
                    "Neither is recorded in the ___, so on replay the wait would happen all over again.",
                    "The replay-safe version is context.CreateTimer, a durable timer.",
                ),
                blank = 1,
                answers = listOf("history"),
                note = "Anything that is not in the history gives a different answer on replay, and a different answer is what a non-determinism error is.",
            ),
            B.check("az-durable-k1", "az-durable", "Where should an HTTP call to a payment API live?",
                listOf("In the orchestrator", "In an activity", "In the client function"), 1,
                "Activities hold side effects. Orchestrators replay, so a payment call there would run again on every replay."),
            B.check(
                "az-durable-k2", "az-durable",
                "Which line is safe inside an orchestrator function?",
                listOf("context.CurrentUtcDateTime", "DateTime.UtcNow", "Task.Delay(1000)"), 0,
                "An orchestrator replays its history, so every value has to come back the same. The context supplies a replay-safe clock and timers.",
                whyWrong = mapOf(
                    1 to "DateTime.UtcNow returns something new on every replay, so the orchestration takes a different path each time it runs through.",
                    2 to "Task.Delay is not recorded in the history. A durable timer created from the context is.",
                ),
            ),
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
            B.code("csharp", """
                // Fan-out/fan-in: resize every photo in parallel, then build the gallery once
                var tasks = photos.Select(p => ctx.CallActivityAsync<string>("Resize", p)).ToList();
                var thumbs = await Task.WhenAll(tasks);
                await ctx.CallActivityAsync("BuildGallery", thumbs);
            """),
            B.steps("Raise the external event from outside",
                "Client function starts the orchestration and returns the management URLs." to "The response includes statusQueryGetUri and sendEventPostUri.",
                "The manager's app POSTs to sendEventPostUri with eventName=Approved and body true." to "The orchestrator's WaitForExternalEvent completes.",
                "GET statusQueryGetUri." to "runtimeStatus shows Running, then Completed with the output.",
            ),
            B.compare("Which pattern?",
                B.card("Monitor", "You are polling something until a condition holds.", "Loop", "Durable timer between checks", "Exit when done or after a deadline"),
                B.card("Human interaction", "A person must act, and you need a time-out.", "WaitForExternalEvent", "Raced against a durable timer", "Escalate on time-out"),
                B.card("Async HTTP API", "A caller needs an immediate 202 and a place to poll.", "Client returns status URLs", "Work runs in the orchestration", "Caller polls or uses a webhook"),
            ),
            B.tip("""
                "Wait for a manager to approve within 72 hours" is the human interaction pattern: WaitForExternalEvent
                raced against a durable timer. "Process many items in parallel and combine" is fan-out/fan-in with
                Task.WhenAll.
            """),
            B.mistakes(
                "Using DateTime.Now or Guid.NewGuid() in an orchestrator. Use context time and NewGuid from the context.",
                "Thread.Sleep or Task.Delay to wait. Only durable timers are replay-safe.",
                "Doing I/O in the orchestrator because it is quicker. It runs again on every replay.",
                "Forgetting that activities can run twice (at-least-once). Make them idempotent.",
            ),
            B.remember("""
                Durable timers (ctx.CreateTimer) are the only safe way to wait in an orchestrator. Thread.Sleep or
                Task.Delay would break replay.
            """),
            B.example("""
                Two Tracks could use a monitor pattern to nudge a partner: start when a session is planned, check every
                hour whether it was logged, and after the slot ends send one gentle reminder, then stop.
            """),
            B.check(
                "az-durable-k3", "az-durable",
                "A workflow sends a document for approval and waits up to three days for a person to answer. Which pattern?",
                listOf("Human interaction: an external event raced against a durable timer", "Fan-out/fan-in", "Function chaining"), 0,
                "A named external event carries the answer, and a durable timer races it so the wait cannot last for ever.",
                whyWrong = mapOf(
                    1 to "Fan-out/fan-in runs many activities at once and aggregates them. Nothing here is parallel.",
                    2 to "Chaining runs steps one after another, with nothing from outside to wait for.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-c-durable-match", "Pair each piece of a durable workflow with its job.",
                "Client (starter)" to "Kicks off an orchestration and gets back an instance id",
                "Orchestrator" to "Calls activities in order and keeps the state, deterministically",
                "Activity" to "The one step that does the actual work",
                "Durable timer" to "The only safe way for an orchestration to wait",
            ),
            B.gap(
                "az-c-durable-gap1", "An orchestrator replays from the start, so the clock cannot come from the system.",
                "Read the time with context.___ instead of DateTime.Now.", "CurrentUtcDateTime", "currentutcdatetime",
            ),
            B.gap(
                "az-c-durable-gap2", "Name the pattern: resize every photo in parallel, then build one gallery from the results.",
                "That is ___, with Task.WhenAll.", "fan-out/fan-in", "fan out/fan in", "fan-out fan-in", "fanout/fanin",
            ),
            B.tryIt("Try it in 15 minutes", "Works locally with Azurite; no Azure resources needed.",
                "func new with the Durable Functions orchestration template (dotnet-isolated)", "func start and POST to the HttpStart endpoint", "Open the statusQueryGetUri from the response", "Add a 30-second durable timer and watch runtimeStatus stay Running then Completed",
            ),
            B.explain(
                "az-durable-x1",
                "Explain why an orchestrator function must be deterministic, and what to use instead of DateTime.UtcNow and Task.Delay.",
                "An orchestrator does not stay in memory while it waits. It replays its history from the start each time it wakes, so any value that changes between replays would send it down a different path. The context supplies replay-safe versions: CurrentUtcDateTime for the clock and durable timers for waiting, both recorded in the history so they return the same answer every time.",
                "The orchestrator replays its history" to listOf("replay", "replays", "replayed", "history"),
                "A changing value breaks the replay" to listOf("different", "changes", "new value", "diverge", "different path"),
                "Use the context clock and durable timers" to listOf("context", "currentutcdatetime", "durable timer", "createtimer"),
            ),
            B.glossary(
                "Orchestrator" to "The function that defines the workflow and replays from history.",
                "Activity" to "A function that does one unit of real work; may run more than once.",
                "Task hub" to "The storage tables and queues where orchestration state lives.",
                "Durable timer" to "A replay-safe wait created from the orchestration context.",
                "External event" to "A named signal raised from outside that an orchestrator can wait for.",
            ),
            B.recap(
                "Client starts, orchestrator coordinates, activities do the work.",
                "Orchestrators replay, so they must be deterministic; use context time and durable timers.",
                "Know the five patterns by shape: chaining, fan-out/fan-in, async HTTP, monitor, human interaction.",
            ),
        ),
    )

    private val containers = Topic(
        id = "az-containers", title = "Containers on Azure: Registry, Instances and Container Apps", minutes = 30,
        references = listOf(
            B.ref("Azure Container Registry introduction", LEARN + "azure/container-registry/container-registry-intro"),
            B.ref("ACR Tasks overview", LEARN + "azure/container-registry/container-registry-tasks-overview"),
            B.ref("Azure Container Instances overview", LEARN + "azure/container-instances/container-instances-overview"),
            B.ref("Container groups in ACI", LEARN + "azure/container-instances/container-instances-container-groups"),
            B.ref("Azure Container Apps overview", LEARN + "azure/container-apps/overview"),
            B.ref("Revisions in Container Apps", LEARN + "azure/container-apps/revisions"),
            B.ref("Scaling in Container Apps", LEARN + "azure/container-apps/scale-app"),
        ),
        blocks = listOf(
            B.warmup(
                "az-containers-w1", "az-containers",
                "You want 10% of traffic on a new container version and 90% on the old. Which Azure container service do you think can do that on its own?",
                listOf("Container Apps", "Container Instances", "Container Registry"), 0,
                "One of the three has a concept of versions that traffic can be divided between.",
            ),
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
                az acr repository show-tags --name twotracksacr --repository api
            """),
            B.table(
                listOf("SKU", "Storage", "Extras"),
                listOf("Basic", "10 GB", "Learning and small teams"),
                listOf("Standard", "100 GB", "Most production use"),
                listOf("Premium", "500 GB", "Geo-replication, private link, content trust"),
            ),
            B.worked("""
                Worked example. Two Tracks keeps one ACR task that watches the main branch on GitHub. On every push it
                builds api:{{.Run.ID}} and tags it latest. The Container App pulls by digest, so a rollback is "point the
                revision at the previous tag", never a rebuild.
            """),
            B.faded(
                "az-containers-f1",
                "Same setup, going backwards. A bad image has gone out and you need the previous version serving traffic within the minute.",
                listOf(
                    "The image is already in the registry; nothing needs building.",
                    "In Container Apps, point traffic at the previous ___ instead of the latest one.",
                    "Each one is immutable, so the old version is exactly what it was.",
                ),
                blank = 1,
                answers = listOf("revision"),
                note = "A rollback is a routing change, not a build. That is the whole reason these are immutable and versioned.",
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
                  --secure-environment-variables API_KEY=... \
                  --azure-file-volume-account-name ttfiles --azure-file-volume-share-name out --azure-file-volume-mount-path /out
                az container logs --resource-group tt-rg --name nightly-export
            """),
            B.bullets(
                "Restart policy: Always for services, OnFailure for jobs that should retry, Never for one-shot tasks.",
                "Persistent files: mount an Azure Files share; container disks are thrown away.",
                "Secrets: pass them as secure environment variables or a secret volume, never baked into the image.",
            ),
            B.check("az-containers-k1", "az-containers", "A nightly export must retry if it crashes but not loop forever on success. Which restart policy?",
                listOf("Always", "OnFailure", "Never"), 1,
                "OnFailure restarts only on a non-zero exit. Always would restart after success too; Never would not retry."),
            B.check(
                "az-containers-k2", "az-containers",
                "You need to build an image in Azure without running a Docker daemon of your own. What do you use?",
                listOf("An ACR Task", "A container group", "A Container Apps revision"), 0,
                "ACR Tasks build images in the cloud, on demand or when source or a base image changes.",
                whyWrong = mapOf(
                    1 to "A container group is several ACI containers sharing a host. That runs images; it does not build them.",
                    2 to "A revision is an immutable version of a deployed Container App, which is the result of a build rather than the build.",
                ),
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
            B.steps("Deploy the API to Container Apps",
                "az containerapp env create --name tt-env --resource-group tt-rg --location westeurope" to "An environment with its Log Analytics workspace.",
                "az containerapp create --name tt-api --environment tt-env --image twotracksacr.azurecr.io/api:1.0 --target-port 8080 --ingress external --registry-server twotracksacr.azurecr.io --registry-identity system" to "The app pulls with managed identity; a public URL prints.",
                "az containerapp update --name tt-api --min-replicas 1 --max-replicas 5" to "No cold start at 16:15; up to five replicas.",
                "az containerapp revision set-mode --name tt-api --mode multiple" to "New deployments keep the old revision alive.",
                "az containerapp ingress traffic set --name tt-api --revision-weight tt-api--v2=30 tt-api--v1=70" to "30 percent of traffic reaches v2.",
            ),
            B.h("Revisions and traffic"),
            B.p("""
                Each deployment creates a revision. In single revision mode the new one replaces the old. In multiple
                revision mode you can keep both and send, say, 30 percent of traffic to the new one. Revisions are how
                Container Apps does the blue-green and canary releases that App Service does with slots.
            """),
            B.check(
                "az-containers-k3", "az-containers",
                "You want a tenth of traffic on a new version and the rest on the old one, without managing Kubernetes. Which service?",
                listOf("Container Apps, splitting traffic across revisions", "ACI, using a restart policy", "ACR, using image tags"), 0,
                "Revisions are immutable versions, and traffic can be split across them by percentage.",
                whyWrong = mapOf(
                    1 to "A restart policy says what happens when a container exits. It cannot route anything.",
                    2 to "Tags name images in the registry. They say nothing about how traffic reaches them.",
                ),
            ),
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
            B.compare("Which container service?",
                B.card("Container Instances", "One container or group, now, no orchestration.", "Billed by the second", "Container groups share network and volumes", "No scaling or ingress rules", "Good for jobs and agents"),
                B.card("Container Apps", "Microservices with scaling, revisions, Dapr.", "Scale 0 to N with KEDA", "Revisions and traffic split", "Managed HTTPS ingress", "Kubernetes hidden"),
                B.card("Kubernetes Service (AKS)", "You need full Kubernetes control.", "Your cluster, your YAML", "Custom controllers and operators", "Most work to run", "Only when the question demands it"),
            ),
            B.tip("""
                Pick ACI for "run this container now with no orchestration". Pick Container Apps for "microservices,
                scale to zero, revisions, Dapr". Pick AKS only when the question demands full Kubernetes control.
            """),
            B.mistakes(
                "Storing the registry password in the container app. Use a managed identity to pull.",
                "Leaving min replicas at 0 for a latency-sensitive API. The first request after idle pays the start-up.",
                "Expecting ACI to keep files between runs. Mount Azure Files.",
                "Using Basic ACR and then needing geo-replication. That is Premium only.",
            ),
            B.remember("""
                Container Apps secrets are defined on the app and referenced by environment variables. Managed identity
                lets the app pull from ACR with no password at all.
            """),
            B.example("""
                Two Tracks ships its API image to ACR with an ACR Task on every push. Container Apps runs it with
                min 1 replica (no cold start at 16:15) and max 5. The nightly export is an ACI container group that
                writes to an Azure Files share and exits.
            """),
            B.h("Check yourself"),
            B.match(
                "az-c-containers-match", "Each requirement points at one service. Pair them.",
                "Run this container now, no orchestration, billed by the second" to "Container Instances",
                "Microservices with scale to zero, revisions and Dapr" to "Container Apps",
                "A private registry that can also build the image for you" to "Container Registry",
                "The question insists on full Kubernetes control" to "AKS",
            ),
            B.gap(
                "az-c-containers-gap1", "Pay nothing while the app is idle, at the cost of a cold start.",
                "Set Container Apps minimum replicas to ___.", "0", "zero",
            ),
            B.gap(
                "az-c-containers-gap2", "Container disks are thrown away when the container goes.",
                "Mount an Azure ___ share to keep files between runs.", "Files", "files",
            ),
            B.reorder("az-c-containers-re1", "Build the command that creates the registry.", "az acr create --name twotracksacr --sku Basic"),
            B.tryIt("Try it in 15 minutes", "ACR Basic and one ACI container cost a few cents; delete after.",
                "Create a Basic ACR", "az acr build a hello-world Dockerfile into it", "az container create from that image with --restart-policy Never", "az container logs shows the output", "Delete the resource group",
            ),
            B.explain(
                "az-containers-x1",
                "A team wants to ship a container and move a tenth of traffic to a new version. Explain which Azure services do what.",
                "The registry, ACR, stores the image and can build it in the cloud with an ACR Task. ACI runs a container or a group of them by the second, which suits a job or a sidecar. Container Apps is what gives you revisions: each deployment is an immutable version and traffic can be split across them by percentage, with KEDA scaling and scale to zero, without running Kubernetes yourself.",
                "ACR stores and can build images" to listOf("acr", "registry", "image", "build"),
                "ACI runs containers by the second" to listOf("aci", "container instances", "by the second", "per second"),
                "Container Apps splits traffic across revisions" to listOf("revision", "revisions", "traffic", "percentage", "split"),
            ),
            B.glossary(
                "Image" to "A packaged app with its runtime, stored in a registry and run as a container.",
                "ACR Task" to "A cloud build that produces an image, on demand or on a trigger.",
                "Container group" to "Several ACI containers sharing a host, network and storage.",
                "Revision" to "An immutable version of a Container App; traffic can be split between revisions.",
                "KEDA" to "The scaler Container Apps uses to add replicas from HTTP load or event sources.",
            ),
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
                "The plan owns the hardware: region, operating system, pricing tier and instance count. Custom domains, settings and slots belong to the app.",
                whyWrong = mapOf(
                    0 to "A custom domain is bound to the app, not to the hardware under it.",
                    2 to "App settings are the app's own environment, and they differ between slots of the same plan.",
                    3 to "Slots belong to the app. The plan only decides whether the tier allows any.",
                ),
            ),
            B.q("c2", "az-app-service", "You need the cheapest App Service tier that supports deployment slots. Which do you choose?",
                listOf("Free", "Basic", "Standard", "Premium"), setOf(2),
                "Deployment slots and auto scale start at Standard. Basic gives dedicated workers but no slots.",
                whyWrong = mapOf(
                    0 to "Free has no slots at all, and no Always On either.",
                    1 to "The near miss. Basic gives you dedicated workers, but slots start one tier above it.",
                    3 to "Premium does have slots. It is simply not the cheapest tier that does.",
                ),
            ),
            B.q("c3", "az-app-service", "What happens when you change an app setting on an App Service web app?",
                listOf("Nothing until the next deployment", "The app restarts and picks up the new value", "Only new requests see it; running requests keep the old value", "The setting is queued for the next swap"), setOf(1),
                "App settings are injected as environment variables at start-up, so a change triggers a restart.",
                whyWrong = mapOf(
                    0 to "Settings are read at start-up, not at deploy time, so the change does not wait for one.",
                    2 to "This describes a graceful config reload, which App Service does not do: it restarts.",
                    3 to "Nothing is queued. A swap moves settings between slots; it is not how a change takes effect.",
                ),
            ),
            B.q("c4", "az-slots-scaling", "After a swap, production must keep using the production database connection string. What do you do before swapping?",
                listOf("Delete the connection string from staging", "Mark the connection string as a deployment slot setting", "Restart the production slot", "Scale out the production slot"), setOf(1),
                "A deployment slot setting is sticky: it stays with the slot instead of moving with the code during a swap.",
                whyWrong = mapOf(
                    0 to "Staging needs a connection string of its own to be testable at all.",
                    2 to "A restart changes nothing about which settings travel during a swap.",
                    3 to "Scaling adds workers. It has no bearing on which settings are sticky.",
                ),
            ),
            B.q("c5", "az-slots-scaling", "Which items move with the code during a slot swap? Select all that apply.",
                listOf("General app settings", "Custom domain names", "Handler mappings", "Scale settings"), setOf(0, 2),
                "General settings and handler mappings travel with the code. Custom domains and scale settings stay with the slot.",
                whyWrong = mapOf(
                    1 to "A custom domain is bound to the slot that answers it, so it stays where it is.",
                    3 to "Scale settings belong to the plan and the slot, not to the code being swapped.",
                ),
            ),
            B.q("c6", "az-slots-scaling", "Your app spikes every weekday at 09:00. What is the most cost-effective way to be ready?",
                listOf("Scale up to Premium permanently", "A schedule-based auto scale rule that adds workers before 09:00", "A CPU-based rule only", "Enable Always On"), setOf(1),
                "Schedule rules add capacity before a known spike. A CPU rule reacts after the spike has begun. Scaling up permanently costs all day.",
                whyWrong = mapOf(
                    0 to "This works, and is the most expensive answer on the list: you pay for the peak all day.",
                    2 to "A CPU rule only reacts once the spike has begun, so the first users still wait.",
                    3 to "Always On keeps the app loaded; it adds no capacity for a crowd.",
                ),
            ),
            B.q("c7", "az-slots-scaling", "Auto scale rules for an App Service web app are configured on which resource?",
                listOf("The web app", "The App Service plan", "The resource group", "The deployment slot"), setOf(1),
                "The plan owns the workers, so auto scale is a plan setting that applies to every app on it.",
                whyWrong = mapOf(
                    0 to "Tempting, because you scale 'the app' in conversation. The workers belong to the plan.",
                    2 to "A resource group is only a container for billing and lifecycle.",
                    3 to "A slot shares the plan's workers; it has no scale settings of its own.",
                ),
            ),
            B.q("c8", "az-functions", "How many triggers can a single Azure Function have?",
                listOf("Exactly one", "One per binding", "Up to three", "Unlimited"), setOf(0),
                "Each function has exactly one trigger and any number of input and output bindings.",
                whyWrong = mapOf(
                    1 to "This confuses triggers with bindings. Bindings are unlimited; the trigger is not.",
                    2 to "There is no limit of three anywhere in Functions.",
                    3 to "Unlimited is true of bindings, which is exactly why the question asks about triggers.",
                ),
            ),
            B.q("c9", "az-functions", "Which CRON expression runs a timer trigger at 16:00 on Sunday to Thursday?",
                listOf("0 16 * * 0-4", "0 0 16 * * 0-4", "16 0 * * 0-4 *", "0 0 16 * * 1-5"), setOf(1),
                "Azure timer triggers use six fields starting with seconds: 0 0 16 * * 0-4. Weekday 0 is Sunday, 4 is Thursday.",
                whyWrong = mapOf(
                    0 to "Five fields: this is standard CRON. Azure's timer wants six, starting with seconds.",
                    2 to "Six fields but in the wrong order; this reads as second 16 of minute 0.",
                    3 to "The shape is right and the days are wrong: 1-5 is Monday to Friday, not Sunday to Thursday.",
                ),
            ),
            B.q("c10", "az-functions", "A function must run for 25 minutes and must not suffer cold starts. Which hosting plan fits?",
                listOf("Consumption", "Premium", "Flex Consumption with the default time-out", "Any plan works"), setOf(1),
                "Consumption caps runs at 10 minutes and has cold starts. Premium keeps instances warm and allows longer runs.",
                whyWrong = mapOf(
                    0 to "Consumption caps a run at ten minutes, so 25 is impossible before cold starts even matter.",
                    2 to "Flex Consumption raises the ceiling, but the default time-out still will not reach 25 minutes.",
                    3 to "Two constraints are given precisely so that not every plan works.",
                ),
            ),
            B.q("c11", "az-functions", "Which file holds settings used only when running a function app locally and is never deployed?",
                listOf("host.json", "function.json", "local.settings.json", "appsettings.json"), setOf(2),
                "local.settings.json is for local runs. In Azure the same keys are app settings. host.json configures the whole app and is deployed.",
                whyWrong = mapOf(
                    0 to "host.json configures the whole app and is deployed with it.",
                    1 to "function.json describes one function's bindings and is deployed too.",
                    3 to "appsettings.json is the ASP.NET convention, not the Functions one.",
                ),
            ),
            B.q("c12", "az-durable", "Which rule applies to a Durable Functions orchestrator?",
                listOf("It may call external APIs directly", "It must be deterministic because it replays", "It runs at most five minutes", "It cannot call more than one activity"), setOf(1),
                "Orchestrators replay from history on every wake-up, so the code must take the same path each time. Real work goes in activities.",
                whyWrong = mapOf(
                    0 to "An activity may call an API. An orchestrator must not, because replay would call it again.",
                    2 to "Orchestrations can run for days; it is the activities that have time limits.",
                    3 to "Calling many activities is the entire point of an orchestrator.",
                ),
            ),
            B.q("c13", "az-durable", "A workflow must wait up to 72 hours for a manager to approve, then continue or escalate. Which pattern is this?",
                listOf("Function chaining", "Fan-out/fan-in", "Monitor", "Human interaction"), setOf(3),
                "Human interaction waits for an external event (the approval) raced against a durable timer (the 72-hour time-out).",
                scenario = "Two Tracks wants Sanya to approve any change Usaid makes to the shared level dates.",
                whyWrong = mapOf(
                    0 to "Chaining runs steps one after another with nothing from outside to wait for.",
                    1 to "Fan-out/fan-in runs many activities at once and aggregates them. Nothing here is parallel.",
                    2 to "The monitor pattern polls something on a schedule until it changes. Here the answer arrives on its own.",
                ),
            ),
            B.q("c14", "az-durable", "Inside an orchestrator, what is the safe way to wait for a period of time?",
                listOf("Thread.Sleep", "Task.Delay", "A durable timer from the orchestration context", "A timer-triggered activity"), setOf(2),
                "Only durable timers (context.CreateTimer) are replay-safe. Sleep and Delay break the replay model.",
                whyWrong = mapOf(
                    0 to "Thread.Sleep blocks the host's thread and is lost the moment the orchestrator is unloaded.",
                    1 to "Task.Delay is the right instinct and is not recorded in the history, so a replay waits all over again.",
                    3 to "A timer-triggered activity is a separate function on its own schedule; it cannot resume this orchestration.",
                ),
            ),
            B.q("c15", "az-containers", "You need to run a one-off container job that writes its output to shared storage, with no cluster to manage. Which service?",
                listOf("Azure Kubernetes Service", "Azure Container Instances with an Azure Files mount", "App Service", "Azure Container Registry"), setOf(1),
                "ACI runs a container by the second with no orchestration, and an Azure Files share gives it persistent output.",
                whyWrong = mapOf(
                    0 to "AKS would run it, and is the cluster the question asks you not to manage.",
                    2 to "App Service is for a service that keeps answering, billed while it runs rather than by the second.",
                ),
            ),
            B.q("c16", "az-containers", "Which Container Apps feature sends 30 percent of traffic to a new version while keeping the old one?",
                listOf("Deployment slots", "Multiple revision mode with traffic splitting", "Scale rules", "Dapr"), setOf(1),
                "Revisions are Container Apps' answer to slots. In multiple revision mode you split traffic between them.",
                whyWrong = mapOf(
                    0 to "Deployment slots are App Service. Container Apps does the same job with revisions.",
                    2 to "Scale rules decide how many replicas run, not which version they are.",
                ),
            ),
            B.q("c17", "az-containers", "Which ACR tier adds geo-replication?",
                listOf("Basic", "Standard", "Premium", "All tiers"), setOf(2),
                "Geo-replication, private link and content trust are Premium features.",
                whyWrong = mapOf(
                    1 to "Standard raises the storage and throughput, and geo-replication starts one tier above it.",
                ),
            ),
            B.q("c18", "az-containers", "Which ACI restart policy suits a batch job that should retry only if it crashes?",
                listOf("Always", "OnFailure", "Never", "Manual"), setOf(1),
                "OnFailure restarts the container only when it exits with an error. Always is for services; Never is for strict one-shot tasks.",
                whyWrong = mapOf(
                    0 to "Always restarts it after a clean exit too, so a job that finished would start again for ever.",
                    2 to "Never leaves a crashed job dead, which is the retry the question asks for.",
                ),
            ),
            B.q("c19", "az-app-service", "An ASP.NET app reads Configuration[\"Firestore:ProjectId\"]. Which app setting key provides it on Linux App Service?",
                listOf("Firestore:ProjectId", "Firestore__ProjectId", "FIRESTORE_PROJECTID", "Firestore.ProjectId"), setOf(1),
                "A colon is not valid in Linux environment variable names, so ASP.NET maps a double underscore to the colon.",
                whyWrong = mapOf(
                    0 to "A colon is not legal in an environment variable name on Linux, so the setting cannot be created.",
                    2 to "An underscore-and-upper-case name is a convention from elsewhere. ASP.NET maps the double underscore, not this.",
                ),
            ),
            B.q("c20", "az-containers", "How should a Container App authenticate to pull images from ACR with no stored password?",
                listOf("Admin user credentials in a secret", "A managed identity granted AcrPull", "Anonymous pull", "A personal access token"), setOf(1),
                "A system or user-assigned managed identity with the AcrPull role lets the app pull with no credential to store or rotate.",
                whyWrong = mapOf(
                    0 to "The admin user is a password in a secret, which is what was to be avoided.",
                    2 to "Anonymous pull would make the images public.",
                    3 to "A personal access token is a person's credential, and expires with them.",
                ),
            ),
        ),
    )
}
