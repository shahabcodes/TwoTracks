package com.shahaabapps.core.content

/** AI-200 topics, written from the official study guide and Microsoft Learn (Ai200). */
internal object Ai200Containers {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val app = Topic(
        id = "ai-app", title = "Zaiqa: the app we build all course", minutes = 24,
        references = listOf(
            B.ref("What is Azure SQL Database?", LEARN + "azure/azure-sql/database/sql-database-paas-overview"),
            B.ref("Welcome to Azure Cosmos DB", LEARN + "azure/cosmos-db/introduction"),
            B.ref("Criteria for choosing a data store", LEARN + "azure/architecture/guide/technology-choices/data-store-overview"),
            B.ref("Azure Container Apps overview", LEARN + "azure/container-apps/overview"),
            B.ref("Introduction to Azure Blob Storage", LEARN + "azure/storage/blobs/storage-blobs-introduction"),
            B.ref("Service Bus queues, topics and subscriptions", LEARN + "azure/service-bus-messaging/service-bus-queues-topics-subscriptions"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-app-w1", "ai-app",
                "Zaiqa takes an order and captures the payment. Which store must hold the order, its lines and the payment?",
                listOf("Azure SQL Database", "Cosmos DB for NoSQL", "Azure Managed Redis"), 0,
                "Think about which one changes several tables in one go that either all happens or none of it does.",
            ),
            B.plain("""
                Zaiqa is the app we build across this whole course. It is our own version of the food-delivery app you
                already use every week: Zomato's real internals are not public, so we are not describing their system,
                we are building ours. Every later topic changes one part of Zaiqa, and this page is the map it changes.
            """),
            B.diagram(
                caption = "The map Zaiqa changes all course: the app and the dashboard call the API, the API owns Azure SQL, and the queue, the worker and the photo function fill Cosmos, Redis and Blob.",
                nodes = listOf(
                    DNode("app", "Zaiqa app", 0.23f, 0.08f, w = 0.42f, h = 0.14f, sub = "Kotlin on your phone", hue = DHue.NEUTRAL),
                    DNode("dash", "Partner dash", 0.77f, 0.08f, w = 0.42f, h = 0.14f, sub = "Angular dashboard", hue = DHue.NEUTRAL),
                    DNode("api", "Zaiqa API", 0.5f, 0.29f, w = 0.46f, h = 0.14f, sub = "dot NET 9, owns SQL", hue = DHue.COMPUTE),
                    DNode("sql", "Azure SQL", 0.17f, 0.50f, w = 0.31f, h = 0.15f, sub = "source of truth", hue = DHue.DATA),
                    DNode("redis", "Redis", 0.5f, 0.50f, w = 0.31f, h = 0.15f, tone = 1, sub = "true right now", hue = DHue.DATA),
                    DNode("blob", "Blob Storage", 0.83f, 0.50f, w = 0.31f, h = 0.15f, tone = 1, sub = "dish photos", hue = DHue.DATA),
                    DNode("queue", "Orders queue", 0.17f, 0.71f, w = 0.31f, h = 0.15f, sub = "Service Bus", hue = DHue.FLOW),
                    DNode("worker", "Order worker", 0.5f, 0.71f, w = 0.31f, h = 0.15f, tone = 1, sub = "to the kitchen", hue = DHue.COMPUTE),
                    DNode("photo", "Photo function", 0.83f, 0.71f, w = 0.31f, h = 0.15f, tone = 1, sub = "Python", hue = DHue.COMPUTE),
                    DNode("cosmos", "Cosmos DB", 0.5f, 0.92f, w = 0.40f, h = 0.15f, sub = "events and vectors", hue = DHue.DATA),
                ),
                edges = listOf(
                    DEdge("app", "api", label = "order"), DEdge("dash", "api", label = "menus"),
                    DEdge("api", "sql", label = "read write"), DEdge("api", "redis", label = "hot list"),
                    DEdge("api", "blob", label = "photo url"), DEdge("api", "queue", label = "paid"),
                    DEdge("queue", "worker", label = "message"), DEdge("worker", "cosmos", label = "events"),
                    DEdge("photo", "blob", dashed = true), DEdge("photo", "cosmos", label = "vectors"),
                ),
                aspect = 1.3f,
            ),
            B.h("The parts, in the order an order meets them"),
            B.p("""
                You order dal makhani at eight on a Friday. The Kotlin app sends that to the Zaiqa API. The API is the
                only thing allowed to touch the database, so every rule about money lives in one place. Everything
                after that is work the API hands on to something else, so the person waiting is not kept waiting.
            """),
            B.table(
                listOf("Part", "What it does for Zaiqa", "Built with"),
                listOf("Zaiqa Android app", "Browse, search, order, track the rider", "Kotlin and Jetpack Compose"),
                listOf("Partner dashboard", "A restaurant's menus and live orders", "Angular on Static Web Apps"),
                listOf("Zaiqa API", "Everything the app asks for, and the only writer to SQL", "dot NET 9 on Container Apps"),
                listOf("Order worker", "Sends a paid order on to the restaurant", "A dot NET worker"),
                listOf("Photo function", "Dish thumbnails, and making them searchable", "Python on Azure Functions"),
            ),
            B.check(
                "ai-app-k1", "ai-app",
                "The Zaiqa app draws the rider creeping along the map, refreshed every few seconds. Where does that position come from?",
                listOf("Azure Managed Redis", "Azure SQL Database", "Blob Storage"), 0,
                "It is one short value, read thousands of times a second, and worthless a minute later. That is exactly what a cache is for.",
                whyWrong = mapOf(
                    1 to "Writing every rider's position to SQL thousands of times a second would cost a lot and buy nothing: nobody queries last Tuesday's dot on the map.",
                    2 to "Blob Storage holds files. A pair of coordinates is not a file.",
                ),
            ),
            B.h("Which store holds what, and why"),
            B.p("""
                This is the choice the exam asks about more than any other, so learn it as four sentences rather than
                four product names. SQL holds what must be correct. Cosmos holds what must be fast and huge. Redis
                holds what is true right now. Blob holds files.
            """),
            B.table(
                listOf("Store", "What it holds for Zaiqa", "Why this one"),
                listOf("Azure SQL Database", "Restaurants, menus, users, orders, order lines, payments", "Money needs one transaction across several tables, foreign keys, and a report finance can query"),
                listOf("Cosmos DB for NoSQL", "The order event feed, rider ping history, dish vectors", "Huge write rates, global spread, and a change feed that starts later work"),
                listOf("PostgreSQL with pgvector", "The payouts ledger, and the other vector store", "The comparison: when you keep vectors in Postgres instead"),
                listOf("Azure Managed Redis", "Hot restaurant list, rider position, rate limiting", "What is true right now, read thousands of times a second"),
                listOf("Blob Storage", "Dish photos", "Big files are not database rows"),
            ),
            B.code("sql", """
                -- The spine of Zaiqa, in Azure SQL. One transaction writes all three.
                CREATE TABLE Orders (
                    OrderId      INT IDENTITY PRIMARY KEY,
                    UserId       INT NOT NULL REFERENCES Users(UserId),
                    RestaurantId INT NOT NULL REFERENCES Restaurants(RestaurantId),
                    PlacedAtUtc  DATETIME2 NOT NULL,
                    TotalMinor   INT NOT NULL
                );
                CREATE TABLE OrderLines (
                    OrderLineId INT IDENTITY PRIMARY KEY,
                    OrderId     INT NOT NULL REFERENCES Orders(OrderId),
                    DishId      INT NOT NULL REFERENCES Dishes(DishId),
                    Quantity    INT NOT NULL
                );
            """),
            B.compare("Where would you put it?",
                B.card("Azure SQL", "It has to be correct, and several rows change together.", "Orders, order lines, payments", "Transactions and foreign keys", "Reports the finance team writes"),
                B.card("Cosmos DB", "There is a flood of it and you need it fast everywhere.", "Order events and rider pings", "A change feed other work listens to", "Dish vectors for search"),
                B.card("Redis", "It is only interesting right now.", "The hot restaurant list", "The rider's current position", "Rate limiting"),
                B.card("Blob Storage", "It is a file.", "Dish photos and thumbnails", "Cheap per gigabyte", "SQL keeps only the address"),
            ),
            B.tip("A question that says \"several tables must change together\" or \"the finance team runs a monthly report\" is pointing at a relational database. One that says \"millions of writes a second\", \"global\" or \"a feed of changes\" is pointing at Cosmos DB."),
            B.check(
                "ai-app-k2", "ai-app",
                "Zaiqa takes about thirty thousand rider pings a minute and wants later work to react to each one. Where do the pings go?",
                listOf("Cosmos DB for NoSQL, and its change feed starts the later work", "Azure SQL, with a trigger on the pings table", "Azure Managed Redis, with a key per rider"), 0,
                "Huge write rates with a feed of what changed is the Cosmos DB shape, and the change feed is the part SQL has no answer for.",
                whyWrong = mapOf(
                    1 to "A table and a trigger at that rate would be expensive and would slow the same database the orders depend on.",
                    2 to "Redis holds the latest ping well, but it keeps no history and gives nothing to replay.",
                ),
            ),
            B.h("The queue, the worker and the photo function"),
            B.bullets(
                "Service Bus carries the paid order from the API to the kitchen, so a slow restaurant never makes the app spin.",
                "The order worker reads that queue and can be retried; the API has already answered the phone.",
                "Event Grid tells the photo function a dish photo landed in Blob Storage; the function makes a thumbnail and writes a dish vector to Cosmos.",
                "Microsoft Entra signs people in, managed identities mean no passwords between services, Key Vault holds the payment provider key, and App Configuration holds the surge fee.",
            ),
            B.remember("The API owns Azure SQL. Nothing else writes to it. Everything that wants to react to an order reads a queue or a change feed instead, which is why one slow part of Zaiqa never stops the rest."),
            B.worked("""
                Worked example. It is eight on Friday and you order dal makhani. The app calls the Zaiqa API, which
                writes the order, its two lines and the payment into Azure SQL in one transaction, so either all of it
                is there or none of it is. The API then puts one message on the Service Bus orders queue and answers
                the app. The order worker picks the message up and sends it to the restaurant, the order event goes
                into Cosmos DB, the rider's position starts arriving in Redis, and the dish photo you are looking at
                came from Blob Storage.
            """),
            B.faded(
                "ai-app-f1",
                "Zaiqa adds loyalty points: earned on every order, spent at the checkout, and the balance must never go wrong when two orders land at the same moment.",
                listOf(
                    "Points are money under another name, so the balance and the rows that moved it have to change together.",
                    "Keep the balance and its ledger in ___, beside the orders the points came from.",
                    "Show a copy of the balance from Redis on the home screen, and treat that copy as a hint rather than the truth.",
                ),
                blank = 1,
                answers = listOf("azure sql", "sql"),
                note = "Anything that must still add up after a crash belongs with the orders and payments, in the relational store that can change several tables in one transaction.",
            ),
            B.steps("Walk one Zaiqa order yourself",
                "Open the order screen in the app and place an order" to "One row in Orders and two in OrderLines, written together.",
                "Watch the API answer before the restaurant has seen anything" to "The message is on the orders queue; the worker has it next.",
                "Refresh the rider's position twice" to "Two different values from Redis, neither of them stored for long.",
                "Open a dish photo and look at what SQL keeps" to "A URL, not the file; the file is a blob.",
                "Ask what started the ratings prompt after delivery" to "The Cosmos change feed, not the API.",
            ),
            B.mistakes(
                "Putting rider pings in Azure SQL because the order is already there, and paying for a table nobody ever queries.",
                "Keeping the order total only in Redis, where a restart quietly loses money nobody can reconstruct.",
                "Storing a four megabyte dish photo as a binary column instead of a blob with its URL in the row.",
                "Letting the worker and the photo function write to Azure SQL directly, so the rules about money live in three places.",
                "Making the app wait for the restaurant to answer instead of putting the paid order on a queue.",
            ),
            B.example("Zaiqa today: a Kotlin app and an Angular dashboard calling a dot NET 9 API on Container Apps, Azure SQL as the source of truth, Service Bus to the order worker, Cosmos DB for events and vectors, Redis for what is true right now, and Blob Storage for the photos."),
            B.check(
                "ai-app-k3", "ai-app",
                "A restaurant uploads a four megabyte photo of a new dish. Where does the file live, and what does Azure SQL keep?",
                listOf("Blob Storage keeps the file; SQL keeps the dish row and the photo's URL", "SQL keeps the file in a binary column so it stays with the dish", "Redis keeps the file so the menu loads quickly"), 0,
                "Files go in Blob Storage and the row keeps the address. The photo function then makes the thumbnail and the dish vector.",
                whyWrong = mapOf(
                    1 to "A binary column makes every backup, query plan and restore of the orders database drag a photo album behind it.",
                    2 to "Redis is memory, priced like memory, and loses everything it holds on a restart.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-app-match", "Pair each part of Zaiqa with what it is for.",
                "Zaiqa API" to "The only thing that writes to Azure SQL",
                "Azure SQL" to "Orders, payments and anything that must be correct",
                "Cosmos DB" to "Order events, rider pings and dish vectors",
                "Redis" to "The hot restaurant list and the rider's position",
                "Blob Storage" to "The dish photo files themselves",
            ),
            B.gap(
                "ai-x-app-gap1", "The store that must be right, even after a crash halfway through.",
                "Orders, order lines and payments live in Azure ___.", "SQL", "sql",
            ),
            B.gap(
                "ai-x-app-gap2", "What carries the paid order from the API to the kitchen.",
                "The API puts the paid order on a Service ___ queue.", "Bus", "bus",
            ),
            B.tryIt("Try it in 15 minutes", "Nothing here needs an Azure subscription; it is the map you will refer back to in every later topic.",
                "Draw Zaiqa's parts from memory and name what each one is built with",
                "Write the four stores and one sentence each for why Zaiqa uses them",
                "Pick three things Zaiqa stores and say which store each belongs in, and why",
                "Trace one order from the app to the restaurant and name every part it passes",
            ),
            B.explain(
                "ai-app-x1",
                "Explain which Zaiqa store holds what, and why each one was chosen.",
                "Azure SQL Database is the source of truth: restaurants, menus, users, orders, order lines and payments, because money needs one transaction across several tables, foreign keys, and a report the finance team can query. Cosmos DB for NoSQL takes the order event feed, the rider ping history and the dish vectors, where the write rate is huge and the change feed starts later work. Azure Managed Redis holds what is true right now, a cache of the hot restaurant list and the rider's current position. Blob Storage holds the dish photo files, and the row in SQL keeps only the address.",
                "SQL for what must be correct" to listOf("azure sql", "transaction", "source of truth"),
                "Cosmos for what is fast and huge" to listOf("cosmos", "change feed", "rider ping"),
                "Redis for what is true right now" to listOf("redis", "cache"),
                "Blob for files" to listOf("blob", "dish photo"),
            ),
            B.glossary(
                "Zaiqa" to "The food-delivery app we build across this course, our own version of the one you already use.",
                "Source of truth" to "The one store whose copy of a fact is the one that counts.",
                "Transaction" to "Several changes that either all happen or none of them do.",
                "Change feed" to "An ordered list of what changed in Cosmos DB, which other work can read.",
                "Queue" to "A line of messages one service leaves for another to pick up later.",
                "Cache" to "A fast copy of something that is cheap to lose and quick to rebuild.",
                "Blob" to "A file kept in storage, found by a URL rather than a row.",
            ),
            B.recap(
                "Zaiqa is a Kotlin app and an Angular dashboard over a dot NET 9 API on Container Apps, with a worker and a photo function behind it.",
                "SQL holds what must be correct, Cosmos what must be fast and huge, Redis what is true right now, Blob the files.",
                "The API owns Azure SQL; everything else reacts through the Service Bus queue or the Cosmos change feed.",
            ),
        ),
    )

    private val acr = Topic(
        id = "ai-acr", title = "Azure Container Registry: build, store and version images", minutes = 26,
        references = listOf(
            B.ref("Introduction to Azure Container Registry", LEARN + "azure/container-registry/container-registry-intro"),
            B.ref("Azure Container Registry service tiers", LEARN + "azure/container-registry/container-registry-skus"),
            B.ref("Automate container builds with ACR Tasks", LEARN + "azure/container-registry/container-registry-tasks-overview"),
            B.ref("Run multi-step build, test and patch tasks", LEARN + "azure/container-registry/container-registry-tasks-multi-step"),
            B.ref("Registry authentication options", LEARN + "azure/container-registry/container-registry-authentication"),
            B.ref("Geo-replication in Azure Container Registry", LEARN + "azure/container-registry/container-registry-geo-replication"),
            B.ref("Learn module: Store and manage containers in Azure Container Registry", LEARN + "training/modules/store-manage-containers-azure-container-registry/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-acr-w1", "ai-acr",
                "You push coach-api:latest twice in one day. Which name always points at exactly the first image you pushed?",
                listOf("Its digest, the sha256 value", "The tag latest", "The repository name coach-api"), 0,
                "One of these names can be moved to another image at any time. One can never move.",
            ),
            B.plain("""
                A container image is a packed-up copy of your app and everything it needs to run. Azure Container
                Registry (ACR) is a private, managed place in Azure to keep those images, like a private Docker Hub.
                It can also build the images for you in the cloud, so you do not need Docker on your laptop.
            """),
            B.diagram(
                caption = "The order it happens in: source code, a build in Azure, the registry, and only then a host that pulls with AcrPull.",
                nodes = listOf(
                    DNode("src", "1. Source code", 0.5f, 0.07f, w = 0.86f, h = 0.13f, sub = "your app and its Dockerfile", hue = DHue.NEUTRAL),
                    DNode("task", "2. ACR Tasks build", 0.5f, 0.25f, w = 0.86f, h = 0.13f, sub = "az acr build, or a trigger on a commit", hue = DHue.FLOW),
                    DNode("acr", "3. Registry", 0.5f, 0.43f, w = 0.86f, h = 0.13f, sub = "ttcoach.azurecr.io, tag coach-api:1.4.0", hue = DHue.DATA),
                    DNode("role", "4. AcrPull on the identity", 0.5f, 0.61f, w = 0.86f, h = 0.12f, kind = NodeKind.NOTE, tone = 2, sub = "no password, no admin user", hue = DHue.NEUTRAL),
                    DNode("app", "App Service", 0.25f, 0.85f, w = 0.44f, h = 0.16f, tone = 1, sub = "pulls the tag", hue = DHue.COMPUTE),
                    DNode("aca", "Container Apps", 0.75f, 0.85f, w = 0.44f, h = 0.16f, tone = 1, sub = "pulls the tag", hue = DHue.COMPUTE),
                ),
                edges = listOf(
                    DEdge("src", "task", label = "az acr build"), DEdge("task", "acr", label = "push"),
                    DEdge("acr", "app", label = "pull"), DEdge("acr", "aca", label = "pull"), DEdge("role", "acr", dashed = true),
                ),
                aspect = 1.25f,
            ),
            B.h("Registries, repositories, tags and digests"),
            B.p("""
                A registry has a login server name such as ttcoach.azurecr.io. Inside it, a repository is a folder of
                versions of one image, for example coach-api. Each version is found by a tag (a friendly, movable label
                like 1.4.0 or latest) and by a digest (a sha256 hash of the image's content that never changes).
            """),
            B.table(
                listOf("Name", "Example", "Can it move?"),
                listOf("Repository", "coach-api", "It is just the folder"),
                listOf("Tag", "coach-api:1.4.0", "Yes; a new push can reuse it"),
                listOf("Digest", "coach-api@sha256:4f9a...", "Never; it is the content's fingerprint"),
            ),
            B.bullets(
                "Tag each build with something unique (a version or the build id) and deploy that; latest is fine for a laptop, risky in production.",
                "To make one version unchangeable, lock it: az acr repository update with --write-enabled false.",
                "Old untagged images still cost storage. On any tier, delete them yourself or schedule an ACR task that runs acr purge; only a Premium registry can also turn on a retention policy (in preview) that removes untagged images automatically.",
            ),
            B.h("Tiers"),
            B.compare("Which tier?",
                B.card("Basic", "Learning and small projects.", "Lowest price and storage", "Same API as the others", "No geo-replication or private link"),
                B.card("Standard", "Most production apps.", "More storage and throughput", "Still a single region"),
                B.card("Premium", "Many regions or private networking.", "Geo-replication to other regions", "Private endpoints", "Highest throughput"),
            ),
            B.tip("\"Pull from a registry close to users in two regions\" means geo-replication, and geo-replication means Premium. One registry name, copies in each region, and pulls go to the nearest one."),
            B.check(
                "ai-acr-k1", "ai-acr",
                "Two Tracks Coach runs in West Europe and East US and both must pull images locally from one registry name. What do you need?",
                listOf("A Premium registry with geo-replication", "Two Basic registries with the same name", "A Standard registry and a CDN"), 0,
                "Geo-replication keeps one login server and replicas in each region, and it is a Premium feature.",
                whyWrong = mapOf(
                    1 to "Registry names are unique across Azure, and two registries are two names to manage.",
                    2 to "Standard has no geo-replication, and a CDN does not serve container pulls.",
                ),
            ),
            B.h("ACR Tasks: build in the cloud"),
            B.table(
                listOf("Kind", "How it starts", "Command"),
                listOf("Quick task", "You run it once, like docker build and push in Azure", "az acr build"),
                listOf("Triggered task", "A Git commit, a base image update, or a timer", "az acr task create"),
                listOf("Multi-step task", "A YAML file of build, run and push steps", "az acr run or a task with --file"),
            ),
            B.p("""
                The base image trigger is the one exams like: when the python:3.12-slim image your Dockerfile starts
                FROM is patched, the task rebuilds your app image automatically, so security fixes reach you without
                anyone remembering to rebuild.
            """),
            B.code("bash", """
                # Quick task: send the folder to Azure, build there, push to the registry
                az acr build --registry ttcoach --image coach-api:{{.Run.ID}} .

                # Triggered task: rebuild on every commit to main (needs a GitHub token)
                az acr task create --registry ttcoach --name coach-ci \
                  --image coach-api:{{.Run.ID}} --context https://github.com/twotracks/coach.git#main \
                  --file Dockerfile --git-access-token <token>
            """),
            B.code("yaml", """
                # acr-task.yaml: build, test, then push only if tests pass
                version: v1.1.0
                steps:
                  - build: -t {{.Run.Registry}}/coach-api:{{.Run.ID}} .
                  - cmd: {{.Run.Registry}}/coach-api:{{.Run.ID}} python -m pytest
                  - push:
                    - {{.Run.Registry}}/coach-api:{{.Run.ID}}
            """),
            B.check(
                "ai-acr-k2", "ai-acr",
                "The team wants images rebuilt whenever the Python base image gets a security patch. What do you set up?",
                listOf("An ACR task with a base image update trigger", "A quick task run by hand each week", "Geo-replication"), 0,
                "A task tracks the base image in the Dockerfile's FROM line and rebuilds when it changes.",
                whyWrong = mapOf(
                    1 to "A quick task only runs when someone remembers to run it.",
                    2 to "Geo-replication copies images between regions; it never rebuilds anything.",
                ),
            ),
            B.h("Signing in without passwords"),
            B.bullets(
                "People: az acr login uses your Entra sign-in and gives Docker a short-lived token.",
                "Apps and hosts: a managed identity with the AcrPull role on the registry pulls images; AcrPush is for pipelines that push.",
                "The admin user is one shared username and password for the whole registry. It is off by default; keep it off outside quick tests.",
            ),
            B.code("python", """
                # List the tags of coach-api with the Python SDK and Entra sign-in
                from azure.identity import DefaultAzureCredential
                from azure.containerregistry import ContainerRegistryClient

                client = ContainerRegistryClient(
                    "https://ttcoach.azurecr.io", DefaultAzureCredential(),
                    audience="https://management.azure.com",
                )
                for tag in client.list_tag_properties("coach-api"):
                    print(tag.name, tag.digest)
            """),
            B.worked("""
                Worked example. Usaid builds coach-api with az acr build and tags it with the run id, ca7. The App
                Service for Two Tracks Coach has a system-assigned identity, and he grants it AcrPull on ttcoach. He
                deploys coach-api:ca7, not latest, so a later push cannot change what is running. The admin user stays
                disabled.
            """),
            B.faded(
                "ai-acr-f1",
                "A new Container App for the coach's worker must pull from ttcoach, with no password stored anywhere.",
                listOf(
                    "Give the Container App a managed identity.",
                    "On the registry, grant that identity the ___ role, and nothing stronger.",
                    "Point the app at the image by a unique tag, and leave the admin user off.",
                ),
                blank = 1,
                answers = listOf("acrpull", "acr pull"),
                note = "Pulling needs AcrPull only. AcrPush is for whatever builds and pushes images.",
            ),
            B.steps("Create a registry and build in the cloud",
                "az acr create --name ttcoach --resource-group tt-rg --sku Basic" to "Admin user is disabled by default.",
                "az acr build --registry ttcoach --image coach-api:v1 ." to "The build log streams to your terminal; no local Docker needed.",
                "az acr repository show-tags --name ttcoach --repository coach-api" to "Shows v1.",
                "az acr manifest list-metadata --registry ttcoach --name coach-api" to "Shows the sha256 digest for each image (the az acr manifest group is still marked preview).",
                "az role assignment create --assignee <principalId> --role AcrPull --scope <registry-id>" to "The host's identity can now pull.",
            ),
            B.mistakes(
                "Deploying coach-api:latest and finding a later push quietly changed production.",
                "Turning on the admin user for an app because it is quicker than a managed identity.",
                "Granting AcrPush or Owner to an app that only ever pulls images.",
                "Expecting geo-replication on a Basic or Standard registry; it needs Premium.",
            ),
            B.example("Two Tracks Coach: one Premium registry ttcoach geo-replicated to two regions, images built by an ACR task on every commit and on base image updates, tagged with the run id, pulled with AcrPull by managed identities."),
            B.check(
                "ai-acr-k3", "ai-acr",
                "Why is the admin user a poor choice for App Service pulling from ACR?",
                listOf("It is one shared password with push and pull rights to the whole registry", "It only works with Premium", "It cannot pull images"), 0,
                "A managed identity with AcrPull is scoped, auditable and has no secret to leak or rotate.",
                whyWrong = mapOf(
                    1 to "The admin user exists on every tier.",
                    2 to "It can pull and push; that is part of the problem.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-acr-match", "Pair each ACR term with what it means.",
                "Tag" to "A movable, friendly label such as 1.4.0",
                "Digest" to "A sha256 fingerprint that never changes",
                "Quick task" to "az acr build: build and push once in Azure",
                "Geo-replication" to "Copies of one registry in several regions",
                "AcrPull" to "The role an app needs to download images",
            ),
            B.gap(
                "ai-x-acr-gap1", "The tier you need for geo-replication and private endpoints.",
                "Geo-replication needs the ___ tier.", "Premium", "premium",
            ),
            B.gap(
                "ai-x-acr-gap2", "The command that builds an image in Azure without local Docker.",
                "Run az acr ___ with a registry, an image name and a folder.", "build",
            ),
            B.tryIt("Try it in 15 minutes", "A Basic registry costs a few cents a day; delete the resource group afterwards. ACR Tasks may not run on free-credit subscriptions.",
                "Create a Basic registry and check the admin user is off",
                "Build a small Python image with az acr build and a unique tag",
                "List its tags and its digest",
                "Push the same tag again and see the digest change while the tag stays",
            ),
            B.explain(
                "ai-acr-x1",
                "Explain how Two Tracks Coach gets a trustworthy image from source code into a running host without any password.",
                "An ACR task builds the image in Azure, either as a quick task with az acr build or triggered by a commit or a base image update, and pushes it to the registry. Each build gets a unique tag, and the digest identifies the exact content, so deploying that tag or digest means a later push cannot change what runs. The host has a managed identity with the AcrPull role, so it signs in with Entra and the admin user stays disabled.",
                "ACR Tasks build the image" to listOf("acr task", "az acr build", "quick task", "triggered", "base image"),
                "Unique tags or digests, not latest" to listOf("digest", "unique tag", "sha256", "immutable"),
                "Managed identity with AcrPull, no admin user" to listOf("acrpull", "managed identity", "admin user", "entra"),
            ),
            B.glossary(
                "Container registry" to "A private store for container images, such as ttcoach.azurecr.io.",
                "Repository" to "All the versions of one image inside a registry.",
                "Tag" to "A movable name for one image version.",
                "Digest" to "The sha256 hash that identifies an image's exact content.",
                "ACR Tasks" to "Cloud builds: quick, triggered or multi-step.",
                "AcrPull" to "The built-in role that allows pulling images and nothing more.",
            ),
            B.recap(
                "Registry, repository, tag and digest; deploy unique tags, because tags move and digests do not.",
                "ACR Tasks build in Azure: az acr build once, triggers for commits, base images and timers, YAML for multi-step.",
                "Pull with a managed identity holding AcrPull; keep the admin user off; Premium for geo-replication.",
            ),
        ),
    )

    private val appService = Topic(
        id = "ai-appservice-containers", title = "Containers on App Service: settings, secrets and slots", minutes = 25,
        references = listOf(
            B.ref("Configure a custom container for App Service", LEARN + "azure/app-service/configure-custom-container"),
            B.ref("Tutorial: Build and run a custom image in App Service", LEARN + "azure/app-service/tutorial-custom-container"),
            B.ref("Configure an App Service app (app settings)", LEARN + "azure/app-service/configure-common"),
            B.ref("Use Key Vault references in App Service", LEARN + "azure/app-service/app-service-key-vault-references"),
            B.ref("Set up staging environments (deployment slots)", LEARN + "azure/app-service/deploy-staging-slots"),
            B.ref("Learn module: Deploy containers to Azure App Service", LEARN + "training/modules/deploy-containers-azure-app-service/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-appservice-containers-w1", "ai-appservice-containers",
                "Coach's FastAPI container listens on port 8000. How do you think App Service learns that?",
                listOf("An app setting called WEBSITES_PORT", "It reads the Dockerfile from GitHub", "You must change the app to port 80"), 0,
                "The answer is the same tool App Service uses to hand your app any setting at all.",
            ),
            B.plain("""
                App Service is Azure's managed web hosting. Besides code, it can run your own container image: it pulls
                the image from a registry, starts it, and sends web traffic to it. Settings you type into App Service
                arrive inside the container as ordinary environment variables, and secrets can come straight from
                Key Vault.
            """),
            B.diagram(
                caption = "In order: the image comes from the registry, the secret from Key Vault, App Service turns both into environment variables inside the container, and the staging slot swaps in the next release.",
                nodes = listOf(
                    DNode("acr", "1. Registry", 0.5f, 0.07f, w = 0.86f, h = 0.14f, tone = 2, sub = "coach-api:ca7, pulled with AcrPull", hue = DHue.DATA),
                    DNode("kv", "2. Key Vault", 0.5f, 0.25f, w = 0.86f, h = 0.14f, tone = 2, sub = "the secret behind the reference", hue = DHue.DATA),
                    DNode("app", "3. App Service, production", 0.5f, 0.45f, w = 0.86f, h = 0.14f, sub = "pulls the image with its identity", hue = DHue.COMPUTE),
                    DNode("env", "4. Env vars in the container", 0.5f, 0.64f, w = 0.86f, h = 0.14f, kind = NodeKind.NOTE, tone = 1, sub = "WEBSITES_PORT, COSMOS_ENDPOINT, OPENAI_KEY", hue = DHue.NEUTRAL),
                    DNode("stage", "5. Staging slot", 0.5f, 0.87f, w = 0.86f, h = 0.14f, tone = 1, sub = "swap to release, swap back to roll back", hue = DHue.COMPUTE),
                ),
                edges = listOf(
                    DEdge("acr", "app", label = "pull"), DEdge("kv", "app", label = "reference"),
                    DEdge("app", "env"), DEdge("stage", "app", label = "swap", bidirectional = true),
                ),
                aspect = 1.3f,
            ),
            B.h("Pulling the image with a managed identity"),
            B.p("""
                App Service needs permission to pull from your registry. Give the app a managed identity (an Entra
                identity Azure creates for it), grant that identity AcrPull on the registry, and tell App Service to
                use it for pulls with the site setting acrUseManagedIdentityCreds. For a user-assigned identity you
                also give its client id. No registry password is stored.
            """),
            B.code("bash", """
                az webapp create --resource-group tt-rg --plan tt-plan --name tt-coach \
                  --container-image-name ttcoach.azurecr.io/coach-api:ca7
                az webapp identity assign --resource-group tt-rg --name tt-coach
                az role assignment create --assignee <principalId> --role AcrPull --scope <registry-id>
                az resource update --ids <web-config-id> --set properties.acrUseManagedIdentityCreds=True
            """),
            B.h("Settings become environment variables"),
            B.p("""
                Every app setting you add in App Service is injected into the container as an environment variable
                when it starts. Changing a setting restarts the app. Python reads them with os.environ, so the same
                image runs anywhere with different settings.
            """),
            B.code("python", """
                import os
                from fastapi import FastAPI

                app = FastAPI()
                COSMOS_ENDPOINT = os.environ["COSMOS_ENDPOINT"]      # a plain app setting
                OPENAI_KEY = os.environ["OPENAI_KEY"]                # a Key Vault reference, already resolved

                @app.get("/health")
                def health():
                    return {"ok": True, "region": os.environ.get("REGION_NAME", "unknown")}
            """),
            B.table(
                listOf("Setting", "What it does"),
                listOf("WEBSITES_PORT", "The port your container listens on, when it is not 80 (Coach uses 8000)"),
                listOf("WEBSITES_ENABLE_APP_SERVICE_STORAGE", "Whether /home is shared, persistent storage for the container"),
                listOf("Any name you choose", "Arrives as an environment variable, such as COSMOS_ENDPOINT"),
            ),
            B.check(
                "ai-appservice-containers-k1", "ai-appservice-containers",
                "The Coach container starts, but every request times out. It listens on 8000. What is missing?",
                listOf("The WEBSITES_PORT app setting set to 8000", "The AcrPull role", "A deployment slot"), 0,
                "App Service sends traffic to port 80 unless WEBSITES_PORT says otherwise.",
                whyWrong = mapOf(
                    1 to "Without AcrPull the image would never be pulled, so the container would not start at all.",
                    2 to "Slots help with releases; they do not change which port gets traffic.",
                ),
            ),
            B.h("Secrets: Key Vault references"),
            B.p("""
                Never put a key in the image or as a plain setting. Store it in Key Vault and make the app setting a
                Key Vault reference. App Service reads the secret with the app's managed identity, which needs the Key
                Vault Secrets User role, and the container just sees the value in an environment variable.
            """),
            B.code("text", """
                OPENAI_KEY = @Microsoft.KeyVault(SecretUri=https://tt-vault.vault.azure.net/secrets/OpenAiKey/)
                OPENAI_KEY = @Microsoft.KeyVault(VaultName=tt-vault;SecretName=OpenAiKey)
            """),
            B.remember("Leave the version off the secret URI so the app picks up a rotated secret. If the reference cannot be resolved, the app sees the reference text itself, so check the green tick in the portal."),
            B.h("Slots and continuous deployment"),
            B.bullets(
                "A deployment slot is a second live copy of the app, such as staging, with its own URL. Slots need Standard tier or higher.",
                "Deploy the new image to staging, warm it up, then swap: the slots trade places with no downtime, and swapping back is a rollback.",
                "Most settings move with the swap. Tick deployment slot setting on a setting to keep it stuck to its slot, such as a staging database.",
                "Continuous deployment: turn it on for the app to get a webhook URL, then create an ACR webhook for push events. Each push makes the app pull the new image.",
                "Logs: turn on container logging with az webapp log config --docker-container-logging filesystem, then watch with az webapp log tail.",
            ),
            B.check(
                "ai-appservice-containers-k2", "ai-appservice-containers",
                "Staging must use its own test database even after a swap. What do you do with DB_NAME?",
                listOf("Mark it as a deployment slot setting", "Put it in the Dockerfile", "Make it a Key Vault reference"), 0,
                "Slot settings stay with the slot during a swap, so each slot keeps its own value.",
                whyWrong = mapOf(
                    1 to "A value baked into the image would travel with the image into production.",
                    2 to "A reference changes where the value comes from, not whether it swaps.",
                ),
            ),
            B.worked("""
                Worked example. Usaid deploys coach-api:ca8 to the staging slot of tt-coach. COSMOS_ENDPOINT is a normal
                setting, OPENAI_KEY is a Key Vault reference, and DB_NAME is marked as a slot setting so staging keeps
                coach-test. He checks /health on the staging URL, then swaps. Production now runs ca8 with production's
                DB_NAME, and swapping back would restore ca7.
            """),
            B.faded(
                "ai-appservice-containers-f1",
                "The Coach's Redis host differs between staging and production, and a swap must not mix them up.",
                listOf(
                    "Add REDIS_HOST as an app setting in each slot with its own value.",
                    "Mark REDIS_HOST as a deployment ___ setting so it stays where it is during a swap.",
                    "Swap; the new image moves to production but each slot keeps its own Redis host.",
                ),
                blank = 1,
                answers = listOf("slot"),
                note = "Settings follow the swap unless they are slot settings. Connection details per environment are the usual slot settings.",
            ),
            B.steps("Deploy Coach from ACR to App Service",
                "az appservice plan create --name tt-plan --resource-group tt-rg --is-linux --sku S1" to "A Linux plan; S1 allows slots.",
                "az webapp create ... --container-image-name ttcoach.azurecr.io/coach-api:ca7" to "The app exists and points at the image.",
                "az webapp identity assign, then grant AcrPull and set acrUseManagedIdentityCreds" to "Pulls use the identity, no password.",
                "az webapp config appsettings set --name tt-coach --resource-group tt-rg --settings WEBSITES_PORT=8000" to "Traffic reaches the container.",
                "az webapp log config --name tt-coach --resource-group tt-rg --docker-container-logging filesystem; az webapp log tail ..." to "Container start-up lines and uvicorn output stream in.",
            ),
            B.mistakes(
                "Forgetting WEBSITES_PORT when the container does not listen on port 80.",
                "Storing the OpenAI key as a plain app setting instead of a Key Vault reference.",
                "Giving the app's identity no Key Vault role, so the reference never resolves.",
                "Not marking per-environment settings as slot settings, so a swap points production at test data.",
                "Pushing a new image and expecting App Service to notice without continuous deployment or a restart.",
            ),
            B.example("Two Tracks Coach: Linux S1 plan, image pulled with a system identity holding AcrPull, WEBSITES_PORT=8000, secrets as Key Vault references, staging slot with slot settings, ACR webhook for continuous deployment."),
            B.check(
                "ai-appservice-containers-k3", "ai-appservice-containers",
                "After pushing coach-api:latest, production still runs the old image. Continuous deployment is meant to be on. What do you check?",
                listOf("The ACR webhook pointing at the app's deployment URL", "The WEBSITES_PORT setting", "The Key Vault firewall"), 0,
                "The registry notifies App Service through a webhook on push; without it the app only pulls on restart.",
                whyWrong = mapOf(
                    1 to "The port decides where traffic goes, not when a new image is pulled.",
                    2 to "Key Vault plays no part in pulling images.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-appsvc-match", "Pair each piece of configuration with its job.",
                "WEBSITES_PORT" to "Tells App Service which port the container listens on",
                "Key Vault reference" to "Fills a setting from a secret at start-up",
                "Slot setting" to "Stays with its slot during a swap",
                "acrUseManagedIdentityCreds" to "Pulls images with the app's identity",
            ),
            B.gap(
                "ai-x-appsvc-gap1", "How the container sees an app setting.",
                "Every app setting arrives as an ___ variable.", "environment", "env",
            ),
            B.gap(
                "ai-x-appsvc-gap2", "What a registry calls on every push to trigger a pull.",
                "Continuous deployment uses an ACR ___ for push events.", "webhook", "webhooks",
            ),
            B.tryIt("Try it in 15 minutes", "A B1 plan costs a little per hour; delete the resource group when done.",
                "Create a Linux web app from a public sample image",
                "Add an app setting and print it from the container's log",
                "Set WEBSITES_PORT to match the image and browse to the site",
                "Turn on container logging and watch az webapp log tail while it restarts",
            ),
            B.explain(
                "ai-appservice-containers-x1",
                "Explain how the Coach container on App Service gets its image, its settings and its secrets without any password in code.",
                "App Service pulls the image from ACR using the app's managed identity, which holds the AcrPull role, after acrUseManagedIdentityCreds is turned on. Every app setting, including WEBSITES_PORT for port 8000, arrives inside the container as an environment variable. Secrets such as the OpenAI key are Key Vault references, which the platform resolves with the same identity, so the code only reads os.environ.",
                "Managed identity with AcrPull pulls the image" to listOf("acrpull", "managed identity", "acrusemanagedidentitycreds"),
                "Settings arrive as environment variables" to listOf("environment variable", "os.environ", "app setting", "websites_port"),
                "Secrets via Key Vault references" to listOf("key vault reference", "key vault", "keyvault"),
            ),
            B.glossary(
                "Custom container" to "Your own image that App Service pulls and runs instead of a built-in stack.",
                "App setting" to "A name and value App Service passes to the app as an environment variable.",
                "WEBSITES_PORT" to "The setting that names the port the container listens on.",
                "Key Vault reference" to "A setting value that App Service resolves from Key Vault at start-up.",
                "Deployment slot" to "A live copy of the app, such as staging, that can be swapped with production.",
                "Slot setting" to "A setting that stays with its slot during a swap.",
            ),
            B.recap(
                "Pull from ACR with a managed identity holding AcrPull; WEBSITES_PORT when the port is not 80.",
                "App settings become environment variables; secrets are Key Vault references resolved with the identity.",
                "Deploy to a staging slot and swap; slot settings stay put; an ACR webhook gives continuous deployment.",
            ),
        ),
    )

    private val containerApps = Topic(
        id = "ai-container-apps", title = "Azure Container Apps: environments, revisions and KEDA scaling", minutes = 28,
        references = listOf(
            B.ref("Azure Container Apps overview", LEARN + "azure/container-apps/overview"),
            B.ref("Azure Container Apps environments", LEARN + "azure/container-apps/environment"),
            B.ref("Update and deploy changes (revisions)", LEARN + "azure/container-apps/revisions"),
            B.ref("Traffic splitting in Azure Container Apps", LEARN + "azure/container-apps/traffic-splitting"),
            B.ref("Scaling in Azure Container Apps", LEARN + "azure/container-apps/scale-app"),
            B.ref("Manage secrets in Azure Container Apps", LEARN + "azure/container-apps/manage-secrets"),
            B.ref("Jobs in Azure Container Apps", LEARN + "azure/container-apps/jobs"),
            B.ref("Learn module: Deploy containers to Azure Container Apps", LEARN + "training/modules/deploy-containers-azure-container-apps/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-container-apps-w1", "ai-container-apps",
                "Coach's worker reads a Service Bus queue. At night the queue is empty. How many copies do you think should run?",
                listOf("Zero, and start again when messages arrive", "Always at least one", "Ten, to be ready"), 0,
                "You pay per running copy here, and something can watch the queue for you.",
            ),
            B.plain("""
                Azure Container Apps runs your containers without you managing servers or a Kubernetes cluster. You
                give it an image; it runs copies (replicas), scales them up and down, even to zero, and keeps each
                version of your app as a revision you can send traffic to. Under the hood it uses Kubernetes and
                KEDA, but you never touch them directly.
            """),
            B.diagram(
                caption = "Inside one environment: ingress splits traffic between two revisions, and a KEDA rule watching the Service Bus queue decides how many replicas run.",
                nodes = listOf(
                    DNode("envb", "Container Apps environment", 0.5f, 0.075f, w = 0.92f, h = 0.13f, tone = 2, sub = "shared network and logs", hue = DHue.NEUTRAL),
                    DNode("ing", "Ingress", 0.5f, 0.27f, w = 0.46f, h = 0.14f, kind = NodeKind.PILL, tone = 1, sub = "external, port 8000", hue = DHue.FLOW),
                    DNode("r1", "Revision v1", 0.25f, 0.53f, w = 0.46f, h = 0.17f, sub = "80% of traffic", hue = DHue.COMPUTE),
                    DNode("r2", "Revision v2", 0.75f, 0.53f, w = 0.46f, h = 0.17f, sub = "20%, label beta", hue = DHue.COMPUTE),
                    DNode("sb", "Service Bus queue", 0.25f, 0.85f, w = 0.46f, h = 0.17f, tone = 1, sub = "the waiting backlog", hue = DHue.FLOW),
                    DNode("keda", "KEDA scale rule", 0.75f, 0.85f, w = 0.46f, h = 0.17f, kind = NodeKind.NOTE, sub = "a replica per 20", hue = DHue.NEUTRAL),
                ),
                edges = listOf(
                    DEdge("ing", "r1", label = "80%"), DEdge("ing", "r2", label = "20%"),
                    DEdge("keda", "sb", label = "polls"), DEdge("keda", "r2", label = "replicas"),
                ),
                aspect = 1.25f,
            ),
            B.h("Environments, ingress, secrets and environment variables"),
            B.bullets(
                "An environment is the boundary around a group of container apps: they share a virtual network and a Log Analytics workspace, and can call each other by name.",
                "Ingress makes an app reachable over HTTP: external for the internet, internal for other apps in the environment only. You set the target port, such as 8000.",
                "Secrets are stored on the app; an environment variable points at one with secretref:. A secret can also be a Key Vault reference read with a managed identity.",
                "Changing a secret does not create a revision; restart the revision so containers see the new value.",
            ),
            B.code("bash", """
                az containerapp env create --name tt-env --resource-group tt-rg --location westeurope

                az containerapp create --name coach-api --resource-group tt-rg --environment tt-env \
                  --image ttcoach.azurecr.io/coach-api:ca8 --registry-server ttcoach.azurecr.io \
                  --registry-identity system --ingress external --target-port 8000 \
                  --secrets "openai-key=keyvaultref:https://tt-vault.vault.azure.net/secrets/OpenAiKey,identityref:<identity-id>" \
                  --env-vars "OPENAI_KEY=secretref:openai-key" "COSMOS_ENDPOINT=https://tt-cosmos.documents.azure.com"
            """),
            B.h("Revisions"),
            B.p("""
                A revision is a frozen snapshot of the app's template: image, environment variables, scale rules.
                Changing any of those creates a new revision. Changing configuration such as secrets, ingress or the
                revision mode applies to every revision and creates none.
            """),
            B.compare("Which revision mode?",
                B.card("Single (default)", "Most apps: always run the newest version.", "One active revision at a time", "New revision gets traffic once it is ready, so no downtime", "Old revision is deactivated for you"),
                B.card("Multiple", "Blue-green, canary and A/B tests.", "Several revisions active together", "Split traffic by percentage", "Labels give one revision its own URL"),
            ),
            B.tip("\"Send 20 percent of users to the new version\" means multiple revision mode with traffic splitting. \"Give testers a stable URL for the new version\" means a label."),
            B.code("bash", """
                az containerapp revision set-mode --name coach-api --resource-group tt-rg --mode multiple
                az containerapp ingress traffic set --name coach-api --resource-group tt-rg \
                  --revision-weight coach-api--v1=80 coach-api--v2=20
                az containerapp revision label add --name coach-api --resource-group tt-rg \
                  --label beta --revision coach-api--v2
            """),
            B.check(
                "ai-container-apps-k1", "ai-container-apps",
                "You rotate the Service Bus secret on coach-worker. Running containers still use the old value. Why?",
                listOf("Secret changes create no revision; the revision must be restarted", "Secrets only apply in multiple revision mode", "Secrets need a label"), 0,
                "Secrets are application-scope configuration; containers read them at start, so restart the revision.",
                whyWrong = mapOf(
                    1 to "Secrets work the same in both revision modes.",
                    2 to "Labels route traffic; they have nothing to do with secrets.",
                ),
            ),
            B.h("Scaling with KEDA"),
            B.p("""
                Each revision scales between a minimum and a maximum number of replicas: by default 0 and 10, up to
                1,000. Scale rules say when to add replicas. Container Apps uses KEDA (Kubernetes Event-driven
                Autoscaling), so a rule can watch HTTP traffic, TCP connections, CPU or memory, or an event source such
                as a Service Bus queue. If any rule's threshold is passed, the app scales out.
            """),
            B.table(
                listOf("Rule", "Scales on", "Example"),
                listOf("HTTP", "Concurrent requests per replica (default 10)", "coach-api: add a replica per 50 requests"),
                listOf("Custom: azure-servicebus", "Messages waiting in a queue or subscription", "coach-worker: one replica per 20 messages"),
                listOf("Custom: CPU or memory", "Resource use; cannot scale to zero", "Heavy embedding jobs"),
            ),
            B.code("bash", """
                az containerapp create --name coach-worker --resource-group tt-rg --environment tt-env \
                  --image ttcoach.azurecr.io/coach-worker:ca8 --user-assigned <identity-id> \
                  --min-replicas 0 --max-replicas 20 \
                  --scale-rule-name embed-queue --scale-rule-type azure-servicebus \
                  --scale-rule-metadata "queueName=embed-jobs" "namespace=tt-bus" "messageCount=20" \
                  --scale-rule-identity <identity-id>
            """),
            B.remember("KEDA polls event sources every 30 seconds by default, and waits a 300-second cool-down after the last event before going from one replica to zero. An app with no ingress and no scale rule scales to zero and never wakes, so give it a rule or min-replicas of 1."),
            B.check(
                "ai-container-apps-k2", "ai-container-apps",
                "coach-worker should cost nothing when the embed-jobs queue is empty and grow with the backlog. What do you configure?",
                listOf("min-replicas 0 and an azure-servicebus scale rule", "min-replicas 1 and a CPU rule", "An HTTP scale rule with ingress"), 0,
                "A queue-based KEDA rule wakes the app from zero when messages wait, and scales out with the queue length.",
                whyWrong = mapOf(
                    1 to "A CPU rule cannot scale to zero, and min-replicas 1 keeps paying all night.",
                    2 to "The worker gets work from a queue, not from HTTP requests.",
                ),
            ),
            B.p("""
                Container Apps jobs are for work that runs and then ends: manual jobs you start, scheduled jobs on a
                cron timer, and event-driven jobs that start an execution per batch of queue messages. A nightly
                re-embedding of all notes is a scheduled job, not an app.
            """),
            B.worked("""
                Worked example. Usaid ships coach-api v2 with a new retrieval prompt. The app is in multiple revision
                mode, so v2 comes up beside v1. He labels v2 as beta and gives the URL to Sanya, then sets traffic to
                v1=80 and v2=20. The numbers look good, so he moves to 0 and 100, and deactivates v1 later.
            """),
            B.faded(
                "ai-container-apps-f1",
                "A new summariser revision should get 10 percent of real users before everyone gets it.",
                listOf(
                    "Single mode would give the new revision all traffic as soon as it is ready.",
                    "Switch the app to ___ revision mode so both revisions stay active.",
                    "Set traffic weights to 90 and 10, then move to 100 when the metrics look right.",
                ),
                blank = 1,
                answers = listOf("multiple"),
                note = "Traffic splitting needs multiple revision mode (a separate labels mode is still in preview); single mode always moves everything to the newest ready revision.",
            ),
            B.steps("Run Coach on Container Apps",
                "az extension add --name containerapp --upgrade" to "The CLI commands for Container Apps.",
                "az containerapp env create --name tt-env --resource-group tt-rg --location westeurope" to "An environment with a Log Analytics workspace.",
                "az containerapp create ... --ingress external --target-port 8000" to "Prints the app's FQDN; browse to /health.",
                "az containerapp revision list --name coach-api --resource-group tt-rg -o table" to "One revision, active, with 100 percent traffic.",
                "az containerapp update --name coach-api --resource-group tt-rg --set-env-vars MODEL=gpt-4o-mini" to "An env var change creates a new revision.",
            ),
            B.mistakes(
                "Expecting traffic splitting while the app is still in single revision mode.",
                "Rotating a secret and not restarting the revision, so containers keep the old value.",
                "Scaling a queue worker on CPU, which can never scale it to zero.",
                "Leaving an app with no ingress and no scale rule at min-replicas 0, so it never starts.",
                "Storing the Service Bus connection string in plain env vars instead of a secret or managed identity.",
            ),
            B.example("Two Tracks Coach: coach-api with external ingress and an HTTP rule, coach-worker scaled from zero by an azure-servicebus rule using a managed identity, secrets as Key Vault references, and a scheduled job for nightly re-embedding."),
            B.check(
                "ai-container-apps-k3", "ai-container-apps",
                "Which change to coach-api creates a new revision?",
                listOf("A new container image tag", "A new secret value", "New traffic weights"), 0,
                "The image is part of the revision template; secrets and traffic are app configuration and create none.",
                whyWrong = mapOf(
                    1 to "Secrets are application-scope; they need a restart, not a new revision.",
                    2 to "Traffic weights are ingress configuration and apply across revisions.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-aca-match", "Pair each Container Apps term with its meaning.",
                "Environment" to "Shared network and logs for a group of apps",
                "Revision" to "An immutable snapshot of the app's template",
                "Label" to "A stable URL for one specific revision",
                "KEDA" to "The engine that scales replicas on events",
                "Job" to "Container work that runs to completion",
            ),
            B.gap(
                "ai-x-aca-gap1", "How an environment variable points at a stored secret.",
                "Set OPENAI_KEY=___:openai-key.", "secretref",
            ),
            B.gap(
                "ai-x-aca-gap2", "The lowest replica count that costs nothing while idle.",
                "Set min-replicas to ___ for scale to zero.", "0", "zero",
            ),
            B.tryIt("Try it in 15 minutes", "Consumption pricing has a monthly free grant; delete the resource group afterwards.",
                "Create an environment and a container app from a sample image with external ingress",
                "Change an environment variable and list the revisions",
                "Switch to multiple revision mode and split traffic 50/50",
                "Set min-replicas to 0, wait, and watch the replica count drop",
            ),
            B.explain(
                "ai-container-apps-x1",
                "Explain how Container Apps lets Two Tracks Coach release a new version safely and keep the queue worker cheap.",
                "Each change to the image or template creates a new revision. In multiple revision mode the old and new revisions run together, traffic splitting sends a small percentage to the new one, and a label gives testers its own URL; rolling back is moving the traffic. The worker uses a KEDA scale rule on the Service Bus queue with min-replicas 0, so it scales to zero when the queue is empty and adds replicas as messages wait.",
                "Revisions and multiple mode" to listOf("revision", "multiple revision", "multiple mode"),
                "Traffic splitting or labels" to listOf("traffic splitting", "percentage", "label", "weights"),
                "KEDA queue rule and scale to zero" to listOf("keda", "scale rule", "scale to zero", "min-replicas", "service bus"),
            ),
            B.glossary(
                "Environment" to "The boundary that groups container apps with a shared network and log workspace.",
                "Replica" to "One running copy of a revision.",
                "Revision" to "An immutable snapshot of an app's image, settings and scale rules.",
                "Traffic splitting" to "Sending set percentages of requests to different revisions.",
                "KEDA" to "Kubernetes Event-driven Autoscaling, which scales replicas on HTTP or event sources.",
                "secretref" to "The prefix that makes an environment variable read a stored secret.",
                "Container Apps job" to "A container that runs to completion: manual, scheduled or event-driven.",
            ),
            B.recap(
                "An environment groups apps; ingress exposes them; secrets are referenced with secretref and can come from Key Vault.",
                "Template changes create revisions; multiple mode allows traffic splitting and labels; secret changes need a restart.",
                "KEDA rules scale on HTTP or queues such as Service Bus, between min and max replicas, down to zero.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(app, acr, appService, containerApps)
}
