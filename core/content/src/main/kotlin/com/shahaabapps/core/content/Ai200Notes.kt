package com.shahaabapps.core.content

/**
 * Notes sheets for AI-200, in the order the course teaches them.
 *
 * Every sheet is the same five things, so a reader who has opened one knows
 * where to look in all of them: the words the exam leans on, one picture of
 * where this piece sits in Zaiqa, the table that settles the choice the exam
 * asks about, the commands with the moment each is the right one, and the
 * traps.
 *
 * The pictures are deliberately plainer than the lesson's: a ladder of wide
 * boxes, one per step, read top to bottom. A sheet is glanced at, and a wide
 * box with one short line in it is readable at a glance in a way a dense
 * architecture drawing is not. The lesson keeps the detailed one.
 */
internal object Ai200Notes {

    private val app = notes(
        "ai-app",
        """
            Zaiqa is a food-delivery app: a Kotlin phone app and an Angular partner dashboard talking to one
            dot NET API, which owns Azure SQL and hands everything slow to a queue.
        """,
        B.glossary(
            "Source of truth" to "The one store that decides what is true. In Zaiqa that is Azure SQL: orders, lines and payments.",
            "Transaction" to "Several writes that either all happen or none do. Why money lives in a relational database.",
            "Change feed" to "An ordered list of what changed in a container, which other work reads to react. Cosmos DB has one; SQL does not.",
            "Queue" to "A message waiting to be handled by something else, so the caller is not kept waiting.",
            "Managed identity" to "An identity Azure gives a service so it can sign in to other Azure services with no password in your code.",
        ),
        B.diagram(
            caption = "An order's path through Zaiqa: the app calls the API, the API writes SQL and posts to the queue, and the worker tells the kitchen.",
            nodes = listOf(
                DNode("app", "Zaiqa app", 0.5f, 0.10f, w = 0.62f, h = 0.16f, sub = "Kotlin, on the phone"),
                DNode("api", "Zaiqa API", 0.5f, 0.32f, w = 0.62f, h = 0.16f, sub = "dot NET 9, Container Apps", hue = DHue.COMPUTE),
                DNode("sql", "Azure SQL", 0.5f, 0.54f, w = 0.62f, h = 0.16f, sub = "order, lines, payment", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("queue", "Orders queue", 0.5f, 0.76f, w = 0.62f, h = 0.16f, sub = "Service Bus", hue = DHue.FLOW),
                DNode("worker", "Order worker", 0.5f, 0.94f, w = 0.62f, h = 0.12f, sub = "tells the kitchen", hue = DHue.COMPUTE),
            ),
            edges = listOf(
                DEdge("app", "api", label = "order"),
                DEdge("api", "sql", label = "one transaction"),
                DEdge("api", "queue", label = "paid"),
                DEdge("queue", "worker", label = "message"),
            ),
            aspect = 1.15f,
        ),
        B.table(
            listOf("Store", "Holds", "Because"),
            listOf("Azure SQL", "Orders, lines, payments", "Several tables change together, and finance queries it"),
            listOf("Cosmos DB", "Order events, rider pings, dish vectors", "Huge write rates, global, and a change feed"),
            listOf("PostgreSQL", "Payouts ledger, pgvector store", "The comparison: vectors kept in Postgres instead"),
            listOf("Redis", "Hot list, rider position, rate limits", "True right now, read thousands of times a second"),
            listOf("Blob Storage", "Dish photos", "Files are not rows"),
        ),
        B.commands(
            "Standing up Zaiqa",
            B.cmd(
                "az group create --name zaiqa-rg --location centralindia",
                "First, before anything else exists.",
                "A resource group is the bin everything else goes in, and deleting it deletes the lot — which is how a practice environment stops costing money.",
            ),
            B.cmd(
                "az sql db create -g zaiqa-rg -s zaiqa-sql -n zaiqa --service-objective S0",
                "Creating the source of truth.",
                "Makes the database on an existing logical server. The service objective is the size, and it is the one thing that decides the bill.",
            ),
            B.cmd(
                "az cosmosdb create -g zaiqa-rg -n zaiqa-cosmos --locations regionName=centralindia",
                "Creating the event and vector store.",
                "Creates the account. Databases and containers come after, because throughput is set on them, not here.",
            ),
            B.cmd(
                "az servicebus namespace create -g zaiqa-rg -n zaiqa-bus --sku Standard",
                "Before the API can post a paid order anywhere.",
                "Standard is the cheapest tier with topics and subscriptions; Basic has queues only, and the course uses both.",
            ),
        ),
        B.mistakes(
            "Putting orders in Cosmos DB because it is newer. The question is whether several rows change together, and for a payment they do.",
            "Letting the worker write to SQL as well as the API. Two writers means two places the money rules can disagree.",
            "Reading a question's \"must be fast\" as Redis when it says \"and we query last month's\". A cache keeps nothing.",
        ),
        B.recap(
            "The API is the only writer to Azure SQL.",
            "Anything slow happens after the queue, not before the answer.",
            "SQL for correct, Cosmos for huge and fast, Redis for right now, Blob for files.",
        ),
    )

    private val acr = notes(
        "ai-acr",
        """
            Azure Container Registry stores the images Zaiqa deploys, addressed by repository and tag, pinned by
            digest, built in the cloud by ACR Tasks and pulled with a managed identity rather than a password.
        """,
        B.glossary(
            "Registry" to "The server that holds images: zaiqa.azurecr.io.",
            "Repository" to "One image's name inside the registry: zaiqa/api.",
            "Tag" to "A movable label on a version: :v12, :latest. It can be re-pointed at different bytes.",
            "Digest" to "The sha256 of the exact bytes. It never moves, which is why production pins a digest.",
            "ACR Task" to "A build that runs in Azure, so no developer machine has to produce the image that ships.",
        ),
        B.diagram(
            caption = "From source to a running revision: the task builds in Azure, the registry holds it, and Container Apps pulls it with an identity.",
            nodes = listOf(
                DNode("src", "Source and Dockerfile", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "your repository"),
                DNode("task", "ACR Task", 0.5f, 0.35f, w = 0.66f, h = 0.16f, sub = "builds in Azure", hue = DHue.COMPUTE),
                DNode("acr", "zaiqa.azurecr.io", 0.5f, 0.59f, w = 0.66f, h = 0.16f, sub = "zaiqa/api:v12", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("run", "Container Apps", 0.5f, 0.86f, w = 0.66f, h = 0.16f, sub = "pulls by digest", hue = DHue.COMPUTE),
            ),
            edges = listOf(
                DEdge("src", "task", label = "az acr build"),
                DEdge("task", "acr", label = "push"),
                DEdge("acr", "run", label = "managed identity"),
            ),
            aspect = 1.0f,
        ),
        B.table(
            listOf("Tier", "Gives you", "Pick it when"),
            listOf("Basic", "Small storage, no geo-replication", "Learning, or one small app in one region"),
            listOf("Standard", "More storage and throughput", "Most production work in one region"),
            listOf("Premium", "Geo-replication, private link, content trust, tokens", "More than one region, or a private network, or signed images"),
        ),
        B.commands(
            "The four you will be asked about",
            B.cmd(
                "az acr build -r zaiqa -t zaiqa/api:v12 .",
                "Building an image without Docker on your machine.",
                "Uploads the context, builds it in Azure and pushes the result in one step. This is the command the exam means by ACR Tasks.",
            ),
            B.cmd(
                "az acr login -n zaiqa",
                "Before a plain docker push or pull from your own machine.",
                "Gets a token for your Azure sign-in and hands it to the Docker client, so no registry password exists anywhere.",
            ),
            B.cmd(
                "az acr repository show-manifests -n zaiqa --repository zaiqa/api",
                "When you need the digest a tag currently points at.",
                "Tags move, digests do not. Pinning production to a digest is how you stop someone re-pushing :v12 under you.",
            ),
            B.cmd(
                "az acr import -n zaiqa --source docker.io/library/redis:7 -t cache/redis:7",
                "Bringing a public image in-house.",
                "Copies it registry to registry without pulling it locally, which is how an air-gapped or rate-limited build gets its base images.",
            ),
        ),
        B.mistakes(
            "Deploying :latest and wondering why two replicas run different code. Two pulls at different moments can get different bytes.",
            "Giving a service the registry's admin user. The exam's answer is AcrPull on a managed identity, and admin user off.",
            "Expecting geo-replication on Standard. That is Premium.",
        ),
        B.recap(
            "Build with az acr build, so the image that ships was never on a laptop.",
            "Tag for humans, digest for production.",
            "AcrPull on a managed identity, never the admin user.",
        ),
    )

    private val appServiceContainers = notes(
        "ai-appservice-containers",
        """
            App Service can run your container instead of your code: settings arrive as environment variables,
            secrets as Key Vault references, and a slot lets you warm the new one before it takes traffic.
        """,
        B.glossary(
            "App setting" to "A name and value App Service sets as an environment variable in your container.",
            "Key Vault reference" to "An app setting whose value is @Microsoft.KeyVault(...), resolved by the platform at start-up, so the secret is never in configuration.",
            "Deployment slot" to "A second copy of the app on the same plan, with its own hostname, that can be swapped into production.",
            "Swap" to "Making a slot the production one. Warms the new instance first, so the first real request does not pay the cold start.",
            "Sticky setting" to "A setting marked to stay with its slot through a swap, which is how staging keeps pointing at the staging database.",
        ),
        B.diagram(
            caption = "A container on App Service, with staging warmed and swapped into production rather than deployed over it.",
            nodes = listOf(
                DNode("acr", "zaiqa.azurecr.io", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "zaiqa/api:v12", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("stage", "Staging slot", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "warmed, no traffic", hue = DHue.COMPUTE),
                DNode("prod", "Production", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "takes the traffic", hue = DHue.COMPUTE),
                DNode("kv", "Key Vault", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "payment key", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("acr", "stage", label = "pull"),
                DEdge("stage", "prod", label = "swap"),
                DEdge("kv", "prod", label = "reference", dashed = true),
            ),
            aspect = 1.0f,
        ),
        B.compare(
            "Container on App Service, or Container Apps?",
            B.card("App Service", "One web app, and you want slots and a simple plan.", "Slots and swap built in", "Easy custom domains and certificates", "Scales on CPU and memory"),
            B.card("Container Apps", "Several services, or scaling on a queue.", "Revisions and traffic splitting", "Scales on queue length, to zero", "Dapr and internal ingress"),
        ),
        B.commands(
            "Settings, secrets and the swap",
            B.cmd(
                "az webapp config container set -g zaiqa-rg -n zaiqa-api --container-image-name zaiqa.azurecr.io/zaiqa/api:v12",
                "Pointing the app at a new image.",
                "Changes which image the app runs. With a managed identity and AcrPull there is no registry password to pass.",
            ),
            B.cmd(
                "az webapp config appsettings set -g zaiqa-rg -n zaiqa-api --settings SurgeFee=15",
                "Changing a setting the container reads as an environment variable.",
                "Restarts the app. Anything read once at start-up needs that restart to take effect, which is why a feature flag belongs in App Configuration instead.",
            ),
            B.cmd(
                "az webapp deployment slot create -g zaiqa-rg -n zaiqa-api --slot staging",
                "Before the first safe release.",
                "Makes the second copy. Without a slot every deployment is straight over production, cold start and all.",
            ),
            B.cmd(
                "az webapp deployment slot swap -g zaiqa-rg -n zaiqa-api --slot staging",
                "Releasing what you have already warmed and checked.",
                "Swaps staging into production after warming it, so the release is a hostname change rather than a restart, and swapping back is the rollback.",
            ),
        ),
        B.mistakes(
            "Putting a connection string in an app setting as plain text. The exam wants a Key Vault reference and a managed identity.",
            "Forgetting to mark the staging database setting sticky, so a swap points production at the staging database.",
            "Expecting scale-to-zero. App Service does not; Container Apps does.",
        ),
        B.recap(
            "Settings become environment variables, and changing one restarts the container.",
            "Secrets by Key Vault reference, never in configuration.",
            "Warm the slot, swap it, and swap back to roll back.",
        ),
    )

    private val containerApps = notes(
        "ai-container-apps",
        """
            Container Apps runs Zaiqa's API in an environment, as revisions you can split traffic between, scaled
            by KEDA on HTTP load or queue length, down to zero.
        """,
        B.glossary(
            "Environment" to "The boundary several container apps share: one virtual network, one Log Analytics workspace, and they can call each other by name.",
            "Revision" to "An immutable snapshot of one app: its image and its configuration. A new image means a new revision.",
            "Ingress" to "How traffic reaches the app. External means from the internet; internal means from inside the environment only.",
            "KEDA" to "The scaler. It reads something — HTTP requests, queue length, a custom metric — and decides the replica count.",
            "Replica" to "One running copy of the revision. Zero is allowed, which is what makes a quiet night free.",
        ),
        B.diagram(
            caption = "One environment, two revisions sharing traffic, and KEDA adding replicas to the worker as the queue grows.",
            nodes = listOf(
                DNode("in", "External ingress", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "https, port 443"),
                DNode("r1", "Revision v11", 0.27f, 0.34f, w = 0.42f, h = 0.15f, sub = "80 per cent", hue = DHue.COMPUTE),
                DNode("r2", "Revision v12", 0.73f, 0.34f, w = 0.42f, h = 0.15f, sub = "20 per cent", hue = DHue.COMPUTE),
                DNode("queue", "Orders queue", 0.5f, 0.60f, w = 0.62f, h = 0.15f, sub = "Service Bus", hue = DHue.FLOW),
                DNode("keda", "KEDA scaler", 0.5f, 0.86f, w = 0.62f, h = 0.15f, sub = "0 to 20 replicas", hue = DHue.CHOICE),
            ),
            edges = listOf(
                DEdge("in", "r1"), DEdge("in", "r2"),
                DEdge("r1", "queue", label = "paid order"),
                DEdge("queue", "keda", label = "length", dashed = true),
            ),
            aspect = 1.05f,
        ),
        B.table(
            listOf("Mode", "What a new image does", "Use it for"),
            listOf("Single revision", "Replaces the active one", "Simple services where a rollback is a redeploy"),
            listOf("Multiple revisions", "Runs beside the old one, traffic split by weight", "Canary releases, and holding the old one ready"),
        ),
        B.commands(
            "Revisions, traffic and scale",
            B.cmd(
                "az containerapp update -n zaiqa-api -g zaiqa-rg --image zaiqa.azurecr.io/zaiqa/api:v12",
                "Shipping a new image.",
                "Creates a new revision. In multiple-revision mode it takes no traffic until you give it some, which is the whole point.",
            ),
            B.cmd(
                "az containerapp ingress traffic set -n zaiqa-api -g zaiqa-rg --revision-weight zaiqa-api--v11=80 zaiqa-api--v12=20",
                "Letting a fifth of real traffic try the new revision.",
                "Weights are how a canary works here, and setting the old one back to 100 is the fastest rollback in the service.",
            ),
            B.cmd(
                "az containerapp update -n zaiqa-worker -g zaiqa-rg --min-replicas 0 --max-replicas 20 --scale-rule-name orders --scale-rule-type azure-servicebus --scale-rule-metadata queueName=orders messageCount=20",
                "Scaling the worker on the queue instead of on HTTP.",
                "A worker gets no HTTP requests, so the default scaler never wakes it. This is the KEDA rule the exam asks for: one replica per twenty waiting messages.",
            ),
            B.cmd(
                "az containerapp logs show -n zaiqa-api -g zaiqa-rg --revision zaiqa-api--v12 --follow",
                "When the new revision is unhealthy and you need to see why.",
                "Streams that revision's console output. Naming the revision matters: without it you may be reading the healthy one.",
            ),
        ),
        B.mistakes(
            "Expecting a new revision to take traffic in multiple-revision mode. It takes none until a weight says so.",
            "Leaving min-replicas at zero on a service that must answer the first request quickly. Zero means a cold start.",
            "Using internal ingress for the public API, or external for a service only the API calls.",
        ),
        B.recap(
            "Image or configuration changes make a revision; revisions are immutable.",
            "Traffic weights release and roll back.",
            "KEDA scales on the queue for a worker, on HTTP for an API, and to zero when nothing is happening.",
        ),
    )

    private val aks = notes(
        "ai-aks",
        """
            AKS runs the same image from manifest files: a Deployment keeps the replicas, a Service gives them one
            address, and a rollout can be watched and undone.
        """,
        B.glossary(
            "Pod" to "The smallest thing Kubernetes runs: one or more containers sharing a network address.",
            "Deployment" to "The object that says how many pods of this image, and replaces them in order when the image changes.",
            "Service" to "One stable address in front of the pods. ClusterIP inside the cluster, LoadBalancer for an external one.",
            "Namespace" to "A partition of the cluster, so staging and production objects do not collide.",
            "Workload identity" to "A Kubernetes service account federated with Entra, so a pod can reach Azure with no secret.",
        ),
        B.diagram(
            caption = "A manifest applied to AKS: the Deployment keeps three pods, and the Service gives them one address.",
            nodes = listOf(
                DNode("yaml", "Manifest file", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "Deployment and Service"),
                DNode("dep", "Deployment", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "replicas: 3", hue = DHue.COMPUTE),
                DNode("pods", "Three pods", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "zaiqa/api:v12", hue = DHue.COMPUTE),
                DNode("svc", "Service", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "one address", hue = DHue.FLOW),
            ),
            edges = listOf(
                DEdge("yaml", "dep", label = "kubectl apply"),
                DEdge("dep", "pods", label = "keeps three"),
                DEdge("svc", "pods", label = "routes to", dashed = true),
            ),
            aspect = 1.0f,
        ),
        B.compare(
            "AKS or Container Apps?",
            B.card("AKS", "You need Kubernetes itself.", "Operators, CRDs, service meshes", "Full control of nodes and networking", "A team that already runs Kubernetes"),
            B.card("Container Apps", "You want containers run for you.", "No nodes, no kubectl", "Revisions and KEDA out of the box", "Scale to zero"),
        ),
        B.commands(
            "Getting on, and getting a rollout back",
            B.cmd(
                "az aks get-credentials -g zaiqa-rg -n zaiqa-aks",
                "Before any kubectl command.",
                "Writes the cluster into your kubeconfig. Nothing else here works until it has run, and it is the step people forget in a lab question.",
            ),
            B.cmd(
                "kubectl apply -f api-deployment.yaml",
                "Creating or changing what runs.",
                "Applies the file's desired state. Running it twice does nothing the second time, which is why a manifest in source control is the record of what is deployed.",
            ),
            B.cmd(
                "kubectl rollout status deploy/zaiqa-api",
                "Straight after a change, to see whether it actually landed.",
                "Waits for the new pods to become ready and fails if they do not, which turns a silent bad release into a failed command.",
            ),
            B.cmd(
                "kubectl rollout undo deploy/zaiqa-api",
                "When the new image is bad and you want the old one now.",
                "Steps the Deployment back to its previous revision. Faster and surer than finding and re-applying the old manifest.",
            ),
            B.cmd(
                "az aks update -g zaiqa-rg -n zaiqa-aks --attach-acr zaiqa",
                "When pods fail with ImagePullBackOff from your own registry.",
                "Gives the cluster's identity AcrPull on that registry. It is the usual cause, and the fix is one command rather than a pull secret.",
            ),
        ),
        B.mistakes(
            "Debugging ImagePullBackOff in the pod. The reason is in kubectl describe pod, under Events, not in the logs.",
            "Using a LoadBalancer Service for something only the cluster calls. ClusterIP is the answer, and it costs nothing.",
            "Storing an Azure credential in a Secret. Workload identity is the exam's answer.",
        ),
        B.recap(
            "Deployment keeps the pods, Service gives them an address.",
            "kubectl apply changes it, rollout status checks it, rollout undo takes it back.",
            "Attach the registry to the cluster rather than carrying a pull secret.",
        ),
    )

    val all: List<TopicNotes> = listOf(app, acr, appServiceContainers, containerApps, aks)
}
