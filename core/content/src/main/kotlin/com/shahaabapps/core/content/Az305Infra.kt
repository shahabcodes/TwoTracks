package com.shahaabapps.core.content

/** AZ-305 topics, written from the official study guide and Microsoft Learn (Az305). */
internal object Az305Infra {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val compute = Topic(
        id = "sa-compute", title = "Choosing compute: VMs, containers, serverless and batch", minutes = 30,
        references = listOf(
            B.ref("Choose an Azure compute service", LEARN + "azure/architecture/guide/technology-choices/compute-decision-tree"),
            B.ref("Virtual machine sizes overview", LEARN + "azure/virtual-machines/sizes/overview"),
            B.ref("What are Virtual Machine Scale Sets?", LEARN + "azure/virtual-machine-scale-sets/overview"),
            B.ref("Comparing Container Apps with other Azure container options", LEARN + "azure/container-apps/compare-options"),
            B.ref("Azure Batch runs large parallel jobs in the cloud", LEARN + "azure/batch/batch-technical-overview"),
            B.ref("About Azure Spot Virtual Machines", LEARN + "azure/virtual-machines/spot-vms"),
            B.ref("What is Azure Virtual Desktop?", LEARN + "azure/virtual-desktop/overview"),
            B.ref("Learn module: Design an Azure compute solution", LEARN + "training/modules/design-compute-solution/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-compute-w1", "sa-compute",
                "Two Tracks must turn 200,000 uploaded workout videos into thumbnails overnight, as cheaply as possible. Which service fits best?",
                listOf("Azure Batch with spot nodes", "One very large virtual machine", "App Service on the Premium tier"), 0,
                "Thousands of small jobs that do not talk to each other, with a deadline but no hurry. Which service is built for that?",
            ),
            B.plain("""
                Compute means the place your code runs. Azure offers a ladder of choices. At the bottom you rent a
                whole virtual machine and look after it, like an empty flat. At the top you hand Azure a small
                function and it runs it when something happens, like ordering a meal. The higher you climb, the less
                you manage and control. The exam wants the lowest-effort rung that meets every requirement.
            """),
            B.diagram(
                caption = "Start from the workload, ask what it needs, and the answer points to a family of services. Most designs end up with two or three of them.",
                nodes = listOf(
                    DNode("wl", "Your workload", 0.5f, 0.1f, w = 0.3f, h = 0.12f, kind = NodeKind.USER),
                    DNode("q1", "Need full OS control?", 0.17f, 0.36f, w = 0.3f, h = 0.16f, tone = 2),
                    DNode("q2", "Short code on events?", 0.5f, 0.36f, w = 0.3f, h = 0.16f, tone = 2),
                    DNode("q3", "Packaged as containers?", 0.83f, 0.36f, w = 0.3f, h = 0.16f, tone = 2),
                    DNode("a1", "Virtual Machines", 0.17f, 0.65f, w = 0.3f, h = 0.18f, tone = 1, sub = "or scale sets"),
                    DNode("a2", "Functions", 0.5f, 0.65f, w = 0.3f, h = 0.18f, tone = 1, sub = "or Logic Apps"),
                    DNode("a3", "Container Apps", 0.83f, 0.65f, w = 0.3f, h = 0.18f, tone = 1, sub = "or AKS, ACI"),
                    DNode("note", "A web app: App Service. Huge parallel jobs: Batch.", 0.5f, 0.9f, w = 0.9f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("wl", "q1"), DEdge("wl", "q2"), DEdge("wl", "q3"),
                    DEdge("q1", "a1", label = "yes"),
                    DEdge("q2", "a2", label = "yes"),
                    DEdge("q3", "a3", label = "yes"),
                ),
                aspect = 0.9f,
            ),
            B.h("The ladder: IaaS, PaaS and serverless"),
            B.p("""
                Three short names describe the rungs. IaaS, infrastructure as a service, is the empty flat: you get
                virtual machines and patch the operating system yourself. PaaS, platform as a service, is a serviced
                flat: you bring your app, and Azure looks after the machines underneath. Serverless is the meal you
                order: your code runs only when an event arrives, and you pay only for that time. Requirement words
                tell you the rung.
            """),
            B.table(
                listOf("Requirement words", "Points to", "Why"),
                listOf("Custom software on the OS, admin rights, move an old server unchanged", "Virtual Machines", "Only IaaS gives full control of the machine"),
                listOf("Many identical VMs that grow and shrink with load", "VM Scale Sets", "A group of VMs from one image"),
                listOf("Web site or web API, least management", "App Service", "Managed web hosting"),
                listOf("Small code started by a queue, timer or web call", "Functions", "Runs on events, pay per run"),
                listOf("Thousands of independent tasks, rendering, simulation", "Batch", "Schedules tasks across many VMs"),
            ),
            B.h("Virtual machines: sizes, scale sets and desktops"),
            B.p("""
                A VM size is the shape of the machine: how many processors and how much memory. Sizes come in
                families named by letters, like clothing cut for different body types: pick the family from the kind
                of work, then the size from the load.
            """),
            B.table(
                listOf("Family", "Series", "Good for"),
                listOf("General purpose", "A, B, D", "Web servers, small databases; B saves up spare power for bursts"),
                listOf("Compute optimised", "F", "Busy app servers, processor-heavy batch work"),
                listOf("Memory optimised", "E, M", "Large databases, in-memory analytics"),
                listOf("Storage optimised", "L", "Big data stores on fast local disks"),
                listOf("GPU accelerated", "N", "AI training, graphics and video"),
                listOf("High performance compute", "H", "Simulations on very fast networks"),
            ),
            B.p("""
                A Virtual Machine Scale Set is a group of identical VMs that Azure grows or shrinks on a rule, for
                example when average processor use passes 70 percent. Azure Virtual Desktop streams Windows desktops
                from VMs in Azure to staff laptops, like watching a film instead of owning the disc.
            """),
            B.check(
                "sa-compute-k1", "sa-compute",
                "An old reporting server needs a vendor agent installed on the operating system, and must move to Azure without code changes. Which compute?",
                listOf("Azure Virtual Machines", "Azure Functions", "Azure Container Apps"), 0,
                "Installing software on the OS and moving without changes is lift and shift, which needs the control that only a VM gives.",
                whyWrong = mapOf(
                    1 to "Functions runs your own small pieces of code; you cannot install an agent on the machine underneath.",
                    2 to "Container Apps needs the app packaged as a container first, which is a change, and gives no OS access.",
                ),
            ),
            B.h("Containers: App Service, Container Apps, AKS or ACI"),
            B.p("""
                A container is an app packed with everything it needs, like a ready meal in a sealed box: it runs
                the same anywhere. Kubernetes is the open-source system that runs many containers across many
                machines, like a kitchen manager deciding which oven heats which box. Microservices are an app split
                into small services that each do one job. Four Azure services can run containers, and the exam
                loves to compare them.
            """),
            B.compare("Which container host?",
                B.card("Container Apps", "Microservices without running Kubernetes yourself.", "Built on Kubernetes, but hides it", "Grows with demand, down to zero"),
                B.card("AKS", "You need Kubernetes itself, with all its controls.", "Azure Kubernetes Service: Azure runs the manager, you run the machines", "Most control, most effort"),
                B.card("Container Instances", "One container, started fast, for a short task.", "ACI: no cluster, billed per second", "Does not grow by itself"),
                B.card("App Service", "A web app or API, as code or a container.", "Staging slots and autoscale built in", "Simplest for websites"),
            ),
            B.tip("\"Without managing Kubernetes\" or \"scale to zero\" points to Container Apps. \"Access to the Kubernetes API\" points to AKS. \"Simplest way to run a single container\" points to Container Instances."),
            B.h("Serverless: Functions and Logic Apps"),
            B.p("""
                Azure Functions runs small pieces of code. A trigger is the event that starts one, such as a queue
                message or a timer, like a doorbell that wakes the cook. Logic Apps is for workflows drawn in a
                designer, joined to products like Outlook through ready-made connectors. The hosting plan decides how
                a function app runs and is billed. Flex Consumption, the serverless plan for new apps, scales to zero
                and bills per run; the older Consumption plan is legacy. Premium keeps instances warm, so there is no
                cold start, the pause while a sleeping app wakes.
            """),
            B.h("Batch, and paying less"),
            B.diagram(
                caption = "Batch reads inputs from Storage, splits a job into many tasks across a pool of nodes, and writes the results back.",
                nodes = listOf(
                    DNode("in", "Videos in Storage", 0.15f, 0.35f, w = 0.26f, h = 0.3f, kind = NodeKind.CYLINDER),
                    DNode("pool", "Batch pool", 0.5f, 0.35f, w = 0.34f, h = 0.3f, sub = "spot + dedicated"),
                    DNode("out", "Thumbnails", 0.85f, 0.35f, w = 0.26f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 1),
                    DNode("note", "One job, thousands of independent tasks", 0.5f, 0.82f, w = 0.9f, h = 0.22f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("in", "pool", label = "tasks"),
                    DEdge("pool", "out", label = "results"),
                ),
                aspect = 0.5f,
            ),
            B.p("""
                Azure Batch runs huge piles of work that split into pieces, like a print shop sharing one big order
                across many printers. You give it a pool, a set of VMs it creates and grows. A job is the whole
                order, and tasks are the single pages. Batch costs nothing itself; you pay for the VMs. Rendering,
                risk simulations and video conversion, where each piece runs on its own, suit it best.
            """),
            B.bullets(
                "Spot VMs are Azure's spare machines at a big discount, like standby airline seats. Azure can take them back with 30 seconds' notice, and they have no SLA, so use them only for work that can restart.",
                "A reservation commits you to a VM size in a region for one or three years, for a big discount. Use it for machines that run all the time.",
                "Azure Hybrid Benefit lets you bring Windows Server and SQL Server licences you already own.",
            ),
            B.check(
                "sa-compute-k2", "sa-compute",
                "A nightly simulation splits into 10,000 independent tasks. It may finish late but must be cheap. What do you recommend?",
                listOf("Azure Batch with a pool that uses spot nodes", "A reserved D-series VM running all night", "Container Instances started by hand"), 0,
                "Batch schedules many independent tasks on a pool, and spot nodes are cheap; an evicted task simply runs again.",
                whyWrong = mapOf(
                    1 to "A reservation pays for a machine all day, every day for years; one VM is also slow for 10,000 tasks.",
                    2 to "ACI has no scheduler; you would be building what Batch already does.",
                ),
            ),
            B.check(
                "sa-compute-k3", "sa-compute",
                "The Two Tracks API runs as containers, must scale to zero at night, and the team has no Kubernetes skills. Which host?",
                listOf("Azure Container Apps", "Azure Kubernetes Service", "A Virtual Machine Scale Set"), 0,
                "Container Apps is serverless for containers: built on Kubernetes, hidden from you, and able to scale to zero.",
                whyWrong = mapOf(
                    1 to "AKS needs Kubernetes skills to run node pools and upgrades, and the cluster keeps nodes running.",
                    2 to "A scale set runs whole VMs that you patch; it does not run containers for you.",
                ),
            ),
            B.worked("""
                Worked example. Requirements: the Two Tracks API is three container microservices, busy in Europe's
                evening and idle at 4 a.m., and nobody on the small team knows Kubernetes. Options: AKS, Container
                Apps, App Service. AKS needs cluster skills and always-on machines; App Service suits one web app.
                Choice: Container Apps, scaling down to zero copies. Why: least work, nothing to pay when idle, and
                standard containers can still move to AKS later.
            """),
            B.faded(
                "sa-compute-f1",
                "Same kind of decision for the Two Tracks marketing team: every night 50,000 photos must be resized and watermarked. Each photo is processed on its own, and cost matters more than speed.",
                listOf(
                    "Requirements: many independent tasks, a nightly deadline, the lowest cost.",
                    "Choice: run the work on Azure ___, which schedules tasks across a pool of VMs.",
                    "Cost: add spot nodes to the pool; an evicted task is simply retried.",
                ),
                blank = 1,
                answers = listOf("batch"),
                note = "Independent tasks at scale on a pool of nodes is exactly what Batch was built for.",
            ),
            B.steps("Do it for real",
                "az group create --name tt-compute-rg --location westeurope" to "A resource group to hold the test.",
                "az containerapp up --name tt-api --resource-group tt-compute-rg --image mcr.microsoft.com/k8se/quickstart:latest --ingress external --target-port 80" to "A public URL for a sample container app.",
                "az containerapp update --name tt-api --resource-group tt-compute-rg --min-replicas 0 --max-replicas 5" to "The app scales between zero and five replicas.",
                "az vm list-sizes --location westeurope --output table" to "The sizes on offer in the region; look for D, E and F names.",
                "az group delete --name tt-compute-rg --yes" to "Everything removed, so nothing keeps billing.",
            ),
            B.mistakes(
                "Choosing VMs for a simple web API, then spending every month patching operating systems.",
                "Picking AKS when nobody needs the Kubernetes API; Container Apps would run the same containers with less work.",
                "Putting a production database on spot VMs, which Azure can evict with 30 seconds' notice.",
                "Buying a reservation for a machine that only runs a few hours a week.",
                "Building your own task scheduler on VMs when Azure Batch already does it at no extra charge.",
            ),
            B.example("Two Tracks goes global: the public API runs on Container Apps and scales to zero at night, image and audio processing runs as Batch jobs on spot nodes, a queue-triggered Function sends weekly emails, and the finance team's old reporting server moves to a reserved D-series VM."),
            B.h("Check yourself"),
            B.match(
                "sa-x-compute-match", "Pair each requirement with the compute that fits.",
                "Install a vendor agent on the OS" to "Virtual Machines",
                "Microservices that scale to zero" to "Container Apps",
                "Need the Kubernetes API" to "Azure Kubernetes Service",
                "Workflow joining SaaS apps with connectors" to "Logic Apps",
                "Thousands of independent rendering tasks" to "Azure Batch",
            ),
            B.gap(
                "sa-x-compute-gap1", "Cheap capacity that Azure can take back.",
                "Interruptible work runs cheaply on ___ VMs.", "spot", "Spot",
            ),
            B.gap(
                "sa-x-compute-gap2", "A one- or three-year commitment to a VM size.",
                "A steady VM that runs all the time is cheaper with a ___.", "reservation", "reserved instance",
            ),
            B.tryIt("Try it in 15 minutes", "Free or a few cents; delete the resource group at the end.",
                "Open the compute decision tree in the Architecture Center and follow it for the Two Tracks API",
                "Deploy the sample container app and set its minimum replicas to zero",
                "List VM sizes in your region and find one D, one E and one F size",
                "In the portal VM create page, tick the spot option and read the eviction settings",
            ),
            B.explain(
                "sa-compute-x1",
                "Explain how you would choose compute for the Two Tracks API, a nightly photo job and an old reporting server.",
                "The containerised API has quiet nights and no Kubernetes experts, so Container Apps fits: it hides Kubernetes and can scale to zero. The nightly photo job is many independent tasks, so Azure Batch runs them on spot nodes, since evicted tasks just retry. The reporting server needs software on the operating system, so it moves to a virtual machine with a reservation.",
                "Container Apps for the containerised API" to listOf("container apps", "containerapp", "scale to zero"),
                "Batch for many independent tasks" to listOf("azure batch", "batch pool", "batch job", "batch"),
                "Spot or reservations to cut cost" to listOf("spot", "reservation", "reserved"),
                "VM for lift and shift with OS control" to listOf("virtual machine", "lift and shift", "operating system", "rehost"),
            ),
            B.glossary(
                "IaaS" to "Infrastructure as a service: you rent VMs and manage the OS on them.",
                "PaaS" to "Platform as a service: you deploy the app, Azure manages the machines.",
                "VM Scale Set" to "A group of identical VMs that grows and shrinks on rules.",
                "Container Apps" to "Serverless containers on hidden Kubernetes, able to scale to zero.",
                "Azure Batch" to "Runs large parallel jobs as tasks on a pool of VMs it manages.",
                "Spot VM" to "Spare capacity at a discount that Azure can evict with 30 seconds' notice.",
                "Reservation" to "A one- or three-year commitment to a VM size for a lower price.",
            ),
            B.recap(
                "Pick the lowest-effort service that meets every requirement: VMs for OS control, App Service for web apps, Functions for event code.",
                "Container Apps for containers without cluster work, AKS when you need the Kubernetes API, ACI for one quick container.",
                "Batch for huge parallel jobs; spot VMs for interruptible work, reservations for machines that always run.",
            ),
        ),
    )

    private val appArchitecture = Topic(
        id = "sa-app-architecture", title = "Application architecture: messaging, events, APIs, caching, config and deployment", minutes = 30,
        references = listOf(
            B.ref("Compare Azure messaging services", LEARN + "azure/service-bus-messaging/compare-messaging-services"),
            B.ref("What is Azure Queue Storage?", LEARN + "azure/storage/queues/storage-queues-introduction"),
            B.ref("Azure API Management overview and key concepts", LEARN + "azure/api-management/api-management-key-concepts"),
            B.ref("What is Azure Managed Redis?", LEARN + "azure/redis/overview"),
            B.ref("Cache-Aside pattern", LEARN + "azure/architecture/patterns/cache-aside"),
            B.ref("What is Azure App Configuration?", LEARN + "azure/azure-app-configuration/overview"),
            B.ref("What is Bicep?", LEARN + "azure/azure-resource-manager/bicep/overview"),
            B.ref("Set up staging environments in Azure App Service", LEARN + "azure/app-service/deploy-staging-slots"),
            B.ref("Learn module: Design an application architecture", LEARN + "training/modules/design-application-architecture/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-app-architecture-w1", "sa-app-architecture",
                "Each paid Two Tracks subscription must be processed exactly once, in order, and never lost. Which service carries it?",
                listOf("Azure Service Bus", "Azure Event Grid", "Azure Event Hubs"), 0,
                "This is a valuable message with a contract, not a notice that something happened. Which one is built for orders?",
            ),
            B.plain("""
                A modern app is several parts that talk to each other. Instead of calling each other directly and
                waiting, they pass notes through a go-between, so one part can be busy or down without breaking the
                rest. Around that sit four other choices the exam asks about: a front door for your APIs, a fast
                memory to avoid repeat work, one place for settings, and an automatic way to ship new versions.
            """),
            B.diagram(
                caption = "The phone calls through API Management; the API reads a cache and settings, queues orders on Service Bus, and streams telemetry into Event Hubs.",
                nodes = listOf(
                    DNode("phone", "Android app", 0.12f, 0.15f, w = 0.2f, h = 0.18f, kind = NodeKind.USER),
                    DNode("apim", "API Management", 0.42f, 0.15f, w = 0.3f, h = 0.18f, sub = "keys, rate limits"),
                    DNode("api", "Tracks API", 0.78f, 0.15f, w = 0.3f, h = 0.18f, tone = 1, sub = "Container Apps"),
                    DNode("eh", "Event Hubs", 0.12f, 0.45f, w = 0.2f, h = 0.18f, tone = 2, sub = "telemetry"),
                    DNode("redis", "Managed Redis", 0.42f, 0.45f, w = 0.3f, h = 0.18f, kind = NodeKind.CYLINDER, sub = "cache-aside"),
                    DNode("sb", "Service Bus", 0.78f, 0.45f, w = 0.3f, h = 0.18f, sub = "orders queue"),
                    DNode("cfg", "App Configuration", 0.42f, 0.75f, w = 0.34f, h = 0.18f, tone = 2, sub = "+ Key Vault refs"),
                    DNode("fn", "Billing worker", 0.78f, 0.75f, w = 0.3f, h = 0.18f, tone = 1, sub = "Functions"),
                ),
                edges = listOf(
                    DEdge("phone", "apim", label = "HTTPS"),
                    DEdge("apim", "api"),
                    DEdge("phone", "eh", label = "events"),
                    DEdge("api", "redis", label = "read"),
                    DEdge("api", "sb", label = "order"),
                    DEdge("sb", "fn", label = "trigger"),
                    DEdge("cfg", "api", label = "settings", dashed = true),
                ),
                aspect = 0.85f,
            ),
            B.h("Messages or events?"),
            B.p("""
                A message is a parcel with a delivery note: the sender cares that it is handled. An event is a
                doorbell: a short notice that something happened, whoever hears it. A stream is a long run of events
                in time order, like a till roll. Azure has one service for each, plus a very simple queue.
            """),
            B.table(
                listOf("Service", "Carries", "Strengths", "Pick it when you hear"),
                listOf("Service Bus", "Messages", "Keeps order, drops duplicates, parks failing messages", "Orders, payments, in order, exactly once"),
                listOf("Event Grid", "Single events", "Pushes each event to whoever subscribed, with filters", "React when a file arrives or a resource changes"),
                listOf("Event Hubs", "Event streams", "Millions of events a second, kept so they can be read again", "Telemetry, clicks, sensors, live analytics"),
                listOf("Storage queues", "Simple messages", "Cheap; millions of messages up to 64 KB each", "Simple work queue, huge backlog, lowest cost"),
            ),
            B.p("""
                A few ideas behind that table. FIFO, first in, first out, is a queue at the post office: served in
                the order you arrived. Dead-lettering moves a message that keeps failing to a side shelf, like the
                lost-property office, so a person can look at it. A topic is a queue with several subscribers, so one
                message reaches several readers. A partition is one lane of an event hub; order is kept only inside
                a lane.
            """),
            B.tip("\"Ordered\", \"transaction\" or \"duplicate detection\" points to Service Bus. \"React to a change in an Azure resource\" points to Event Grid. \"Millions of telemetry events\" points to Event Hubs. \"Huge backlog, simplest and cheapest\" points to Storage queues."),
            B.check(
                "sa-app-architecture-k1", "sa-app-architecture",
                "Every phone sends heart-rate readings each second; analysts need to replay the last day of data. Which service ingests it?",
                listOf("Azure Event Hubs", "Azure Service Bus", "Azure Event Grid"), 0,
                "A high-volume time-ordered stream that can be read again is the Event Hubs case, with partitions and retention.",
                whyWrong = mapOf(
                    1 to "Service Bus is for valuable business messages; it removes them once handled, so there is no replay.",
                    2 to "Event Grid pushes single notices to subscribers; it is not built to store a stream for replay.",
                ),
            ),
            B.h("Event-driven designs"),
            B.p("""
                In an event-driven design, parts react to events instead of being called. When a user uploads a
                track, Blob Storage tells Event Grid, which pushes the news to a function that makes a preview.
                Nobody polls, asking again and again whether anything is new, like a child on a car trip.
            """),
            B.h("API integration: API Management"),
            B.p("""
                API Management, often called APIM, is a gateway: one reception desk in front of all your APIs. Every
                caller checks in there first. Policies are the desk's rules, set without code, and they run on every
                call. A developer portal is the brochure rack, where partners read the docs and get their keys.
            """),
            B.bullets(
                "Rate limits stop one caller using everything, like a limit of two cakes per customer.",
                "It can check each caller's sign-in token from Microsoft Entra ID before the request goes further.",
                "Callers only know the desk, so the APIs behind it can move or change without breaking them.",
                "Tiers: Consumption is serverless and billed per call; Developer is for testing, with no SLA; Basic v2 is also for development and testing, but it has an SLA; classic Basic, Standard and Premium, plus Standard v2 and Premium v2, serve production; classic Premium is the one that spans several regions.",
            ),
            B.h("Caching with Redis"),
            B.p("""
                A cache is a small, very fast memory for answers you will need again, like today's timetable on the
                fridge instead of phoning the station. Redis is a popular store that keeps data in memory. Azure
                Managed Redis is Azure's current Redis service; the older Azure Cache for Redis is being retired, so
                new designs use Managed Redis. The usual pattern is cache-aside: look on the fridge first; on a miss,
                phone the station, then pin the answer up with an expiry time.
            """),
            B.check(
                "sa-app-architecture-k2", "sa-app-architecture",
                "The leaderboard query hits the database 2,000 times a minute with the same result. What cuts the load with least change?",
                listOf("Azure Managed Redis with the cache-aside pattern", "A bigger database tier", "A Service Bus queue in front of the database"), 0,
                "Cache-aside answers repeat reads from memory and only goes to the database on a miss, so the load drops sharply.",
                whyWrong = mapOf(
                    1 to "A bigger tier pays more to do the same repeated work; it does not remove it.",
                    2 to "A queue delays work for later; a user waiting for a leaderboard needs the answer now.",
                ),
            ),
            B.h("Configuration: App Configuration and Key Vault"),
            B.compare("Where does a setting live?",
                B.card("App Configuration", "Ordinary settings and feature flags for many apps.", "Key-values with labels such as dev and prod", "Feature flags turn features on without a deploy", "Can hold a pointer to a Key Vault secret"),
                B.card("Key Vault", "Anything secret: keys, passwords, certificates.", "A safe that apps open with their own identity", "Versions, expiry and soft delete", "Never store a secret's value in App Configuration"),
            ),
            B.h("Automated deployment"),
            B.p("""
                Infrastructure as code means your Azure resources are written down in files kept with the app, like a
                recipe card, so every environment is cooked the same way. Bicep is the short language for those
                files; Azure turns it into ARM templates, its own JSON format. A pipeline is the automatic line that
                tests and deploys each change. GitHub Actions runs pipelines from a GitHub repository, and Azure
                Pipelines does the same for teams on Azure DevOps.
            """),
            B.code("bicep", """
                resource plan 'Microsoft.Web/serverfarms@2023-12-01' = {
                  name: 'tt-plan'
                  location: resourceGroup().location
                  sku: { name: 'S1' }   // Standard: deployment slots available
                }
            """),
            B.diagram(
                caption = "The pipeline deploys to the staging slot, the slot warms up, then a swap makes it production. Swap again to roll back.",
                nodes = listOf(
                    DNode("pipe", "Pipeline", 0.15f, 0.35f, w = 0.26f, h = 0.3f, tone = 2, sub = "Actions or DevOps"),
                    DNode("stage", "Staging slot", 0.5f, 0.35f, w = 0.3f, h = 0.3f, sub = "new version"),
                    DNode("prod", "Production slot", 0.85f, 0.35f, w = 0.26f, h = 0.3f, tone = 1),
                    DNode("note", "Swap is instant and reversible: blue-green", 0.5f, 0.82f, w = 0.9f, h = 0.22f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("pipe", "stage", label = "deploy"),
                    DEdge("stage", "prod", label = "swap", bidirectional = true),
                ),
                aspect = 0.5f,
            ),
            B.p("""
                Blue-green deployment keeps two copies, like two stages in a theatre: the live show and the next one.
                You test the new copy, swap the audience over, and can swap back in seconds. In App Service,
                deployment slots do this on the Standard tier and above; in Container Apps, revisions do it.
            """),
            B.check(
                "sa-app-architecture-k3", "sa-app-architecture",
                "Releases of the Two Tracks web app must have no downtime and a rollback in seconds. What do you recommend?",
                listOf("A staging deployment slot, then swap", "Deploy straight to production at night", "Delete and recreate the app from Bicep"), 0,
                "The new version warms up in a staging slot, the swap is instant, and swapping back is the rollback.",
                whyWrong = mapOf(
                    1 to "Night-time still means some downtime, and rolling back means redeploying the old version.",
                    2 to "Recreating the app takes it offline; Bicep describes resources, it is not a release strategy.",
                ),
            ),
            B.worked("""
                Worked example. Requirement: each paid subscription must create one invoice, in order per user, and
                nothing may be lost if billing is down for an hour. Options: Event Grid, Event Hubs, Service Bus,
                Storage queues. Event Grid and Storage queues neither keep order nor drop duplicates; Event Hubs is
                for streams. Choice: a Service Bus queue with sessions, groups that keep one user's messages in
                order, and duplicate detection on. Why: messages wait safely, order holds, and failures go to the
                dead-letter queue.
            """),
            B.faded(
                "sa-app-architecture-f1",
                "A new case: when a user uploads a profile photo to Blob Storage, a thumbnail must be made. Nobody should poll the storage account.",
                listOf(
                    "Requirement: react to a change in an Azure resource, with no polling.",
                    "Choice: subscribe to the BlobCreated event through Event ___.",
                    "Handler: the subscription pushes the event to a function that makes the thumbnail.",
                ),
                blank = 1,
                answers = listOf("grid", "event grid"),
                note = "Azure services publish their own events to Event Grid, which pushes them to subscribers, so nothing polls.",
            ),
            B.steps("Do it for real",
                "az servicebus namespace create --name tt-bus --resource-group tt-app-rg --sku Standard" to "A Service Bus namespace on the Standard tier.",
                "az servicebus queue create --name orders --namespace-name tt-bus --resource-group tt-app-rg --enable-session true --enable-duplicate-detection true" to "A queue that keeps order per session and drops duplicates.",
                "az appconfig create --name tt-config --resource-group tt-app-rg --location westeurope --sku Standard" to "A central store for settings.",
                "az appconfig feature set --name tt-config --feature coach-v2 --label prod" to "A feature flag, off until you enable it.",
                "az webapp deployment slot swap --name tt-web --resource-group tt-app-rg --slot staging --target-slot production" to "Staging becomes production; run it again to roll back.",
            ),
            B.mistakes(
                "Using Event Grid for payment orders, which need ordering and duplicate detection that only Service Bus offers.",
                "Sending phone telemetry through Service Bus queues instead of Event Hubs, and paying for features a stream never uses.",
                "Storing database passwords as plain values in App Configuration instead of Key Vault references.",
                "Caching data with no expiry time, so users keep seeing old results after the database changes.",
                "Deploying straight to production with no staging slot, so a bad release means downtime and a slow rollback.",
            ),
            B.example("Two Tracks goes global: phones call one API Management gateway with rate limits; the Tracks API caches leaderboards in Azure Managed Redis, reads settings and feature flags from App Configuration with Key Vault references, queues paid orders on Service Bus, and streams telemetry into Event Hubs. Bicep and GitHub Actions deploy every change to a staging slot first."),
            B.h("Check yourself"),
            B.match(
                "sa-x-apparch-match", "Pair each requirement with the service.",
                "Orders processed in order, exactly once" to "Service Bus",
                "React when a blob is created" to "Event Grid",
                "Millions of telemetry events per second" to "Event Hubs",
                "Rate limits and keys in front of APIs" to "API Management",
                "Feature flags for many apps" to "App Configuration",
            ),
            B.gap(
                "sa-x-apparch-gap1", "Read the cache first, the database only on a miss.",
                "This pattern is called cache-___.", "aside", "Aside",
            ),
            B.gap(
                "sa-x-apparch-gap2", "Messages that keep failing go to a side queue.",
                "Service Bus moves them to the ___ queue.", "dead-letter", "dead letter", "deadletter",
            ),
            B.tryIt("Try it in 15 minutes", "A few cents; delete the resource group afterwards.",
                "Create a Service Bus namespace and a queue with sessions on",
                "Create an App Configuration store and add a feature flag with a prod label",
                "Read the Compare messaging services page and note one requirement word for each service",
                "Write a three-line Bicep file and run az deployment group what-if against it",
            ),
            B.explain(
                "sa-app-architecture-x1",
                "Explain which services Two Tracks uses for orders, telemetry, repeat reads, settings and releases, and why.",
                "Paid orders go on a Service Bus queue, because they must be processed in order, once, and wait safely if billing is down. Phone telemetry goes to Event Hubs, which takes millions of events and keeps them for replay. Repeated leaderboard reads come from Azure Managed Redis using cache-aside. Settings and feature flags live in App Configuration, with secrets kept in Key Vault. Bicep and a GitHub Actions pipeline deploy each release to a staging slot, then a swap puts it live.",
                "Service Bus for ordered, reliable orders" to listOf("service bus", "dead letter", "sessions"),
                "Event Hubs for telemetry streams" to listOf("event hubs", "event hub", "telemetry"),
                "Redis cache-aside for repeat reads" to listOf("redis", "cache-aside", "cacheaside", "cache aside", "caching"),
                "App Configuration and Key Vault for settings" to listOf("app configuration", "key vault", "feature flag"),
                "Automated deployment with slots" to listOf("bicep", "github actions", "pipeline", "staging slot", "swap"),
            ),
            B.glossary(
                "Service Bus" to "Enterprise message broker with queues, topics, sessions and dead-lettering.",
                "Event Grid" to "Pushes discrete events, such as BlobCreated, to subscribers.",
                "Event Hubs" to "Ingests huge event streams into partitions that can be replayed.",
                "API Management" to "A gateway in front of APIs that applies policies such as rate limits.",
                "Cache-aside" to "Read the cache first; on a miss, read the database and fill the cache.",
                "App Configuration" to "Central store for settings and feature flags, with Key Vault references.",
                "Deployment slot" to "A live copy of an App Service app that can be swapped into production.",
            ),
            B.recap(
                "Service Bus for business messages, Event Grid for reacting to events, Event Hubs for streams, Storage queues for simple cheap queues.",
                "API Management fronts APIs with policies; Managed Redis with cache-aside removes repeat reads; App Configuration holds settings, Key Vault holds secrets.",
                "Bicep plus GitHub Actions or Azure Pipelines deploy every change; slots or revisions give blue-green releases and fast rollback.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(compute, appArchitecture)
}
