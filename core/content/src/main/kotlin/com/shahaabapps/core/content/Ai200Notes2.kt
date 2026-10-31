package com.shahaabapps.core.content

/** Notes sheets for AI-200's troubleshooting topic and its data stores. See [Ai200Notes]. */
internal object Ai200Notes2 {

    private val troubleshoot = notes(
        "ai-container-troubleshoot",
        """
            When a container will not start, the reason is in the events and the probes, not the logs: read the
            status, then the events, then the previous container's output, and only then the network.
        """,
        B.glossary(
            "CrashLoopBackOff" to "The container started, exited, and Kubernetes is waiting longer each time before trying again. Your process died; the logs of the previous attempt say why.",
            "ImagePullBackOff" to "The image could not be fetched at all. Wrong name, wrong tag, or no permission on the registry.",
            "Liveness probe" to "Is it still alive? A failure restarts the container.",
            "Readiness probe" to "Can it take traffic yet? A failure takes it out of the Service, without restarting it.",
            "Startup probe" to "Has it finished booting? While it runs, the liveness probe is held off, which is how a slow start stops being a restart loop.",
        ),
        B.diagram(
            caption = "The order to read a broken container in, because each step rules out the one below it.",
            nodes = listOf(
                DNode("status", "Read the status", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "get pods, get revision"),
                DNode("events", "Read the events", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "describe pod", hue = DHue.CHOICE),
                DNode("logs", "Read the logs", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "and the previous ones", hue = DHue.CHOICE),
                DNode("probe", "Check the probes", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "ready, alive, started", hue = DHue.CHOICE),
                DNode("net", "Then the network", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "dns, ports, ingress", hue = DHue.FLOW),
            ),
            edges = listOf(
                DEdge("status", "events"), DEdge("events", "logs"), DEdge("logs", "probe"), DEdge("probe", "net"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("What you see", "Where the reason is", "Usual cause"),
            listOf("ImagePullBackOff", "describe pod, Events", "Registry permission, or a tag that does not exist"),
            listOf("CrashLoopBackOff", "logs --previous", "The process exits: bad configuration, missing secret"),
            listOf("Running but no traffic", "Readiness probe", "The probe path is wrong, or the port is"),
            listOf("Restarts every minute", "Liveness probe", "Boot takes longer than the probe allows; add a startup probe"),
            listOf("Revision unhealthy", "containerapp logs show", "Same causes; name the revision or you read the healthy one"),
        ),
        B.commands(
            "In the order you run them",
            B.cmd(
                "kubectl describe pod zaiqa-api-7d9f",
                "First, for anything that never started.",
                "The Events list at the bottom is where Kubernetes writes why it could not pull, schedule or start the container. The logs cannot tell you about a container that never ran.",
            ),
            B.cmd(
                "kubectl logs zaiqa-api-7d9f --previous",
                "When the pod is in CrashLoopBackOff.",
                "The current container may be seconds old and empty; the one that died holds the stack trace. This flag is the difference between finding the cause and guessing.",
            ),
            B.cmd(
                "az containerapp revision list -n zaiqa-api -g zaiqa-rg -o table",
                "When a Container Apps release looks wrong.",
                "Shows each revision with its health and its traffic weight, which usually answers whether you are even looking at the revision that is serving.",
            ),
            B.cmd(
                "kubectl exec -it zaiqa-api-7d9f -- curl -s http://zaiqa-worker:8080/healthz",
                "When one service cannot reach another.",
                "Tests the call from inside the cluster, which separates a broken Service or DNS name from a broken application.",
            ),
        ),
        B.mistakes(
            "Reading the logs of a pod that never started. Nothing wrote them; read the events.",
            "Raising the liveness probe's threshold to fix a slow boot instead of adding a startup probe.",
            "Streaming Container Apps logs without naming the revision, then concluding the new one is fine.",
        ),
        B.recap(
            "Status, events, previous logs, probes, network — in that order.",
            "Pull problems are permissions or tags; crash loops are configuration.",
            "A slow start needs a startup probe, not a laxer liveness probe.",
        ),
    )

    private val cosmosSdk = notes(
        "ai-cosmos-sdk",
        """
            Cosmos DB for NoSQL from Python: one client for the life of the process, a point read when you know
            the id and partition key, and a query only when you do not.
        """,
        B.glossary(
            "Container" to "Where items live, and the thing throughput and indexing are set on. Not a table: items in one container need not share a shape.",
            "Partition key" to "The field Cosmos spreads data by. Chosen once, at creation, and it decides everything about cost and scale afterwards.",
            "Point read" to "Fetching one item by its id and partition key. The cheapest operation there is: one request unit for a small item.",
            "Request unit" to "The currency. Every operation costs RUs, and the container's provisioned RUs per second is what you pay for.",
            "Cross-partition query" to "A query with no partition key, which has to ask every partition. Correct, and expensive.",
        ),
        B.diagram(
            caption = "Reading an order event: a point read goes to one partition, a query with no key asks them all.",
            nodes = listOf(
                DNode("client", "One CosmosClient", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "reused, not per request", hue = DHue.COMPUTE),
                DNode("point", "Point read", 0.27f, 0.42f, w = 0.42f, h = 0.16f, sub = "id and key, 1 RU", hue = DHue.CHOICE),
                DNode("query", "Query", 0.73f, 0.42f, w = 0.42f, h = 0.16f, sub = "no key, all partitions", hue = DHue.CHOICE),
                DNode("part", "Partitions", 0.5f, 0.78f, w = 0.66f, h = 0.16f, sub = "by restaurantId", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("client", "point"), DEdge("client", "query"),
                DEdge("point", "part", label = "one"), DEdge("query", "part", label = "every one"),
            ),
            aspect = 0.95f,
        ),
        B.table(
            listOf("You know", "Use", "Costs"),
            listOf("id and partition key", "read_item", "About one RU"),
            listOf("Partition key only", "query with partition_key set", "RUs for that partition's work"),
            listOf("Neither", "query, cross-partition", "Work on every partition, and it grows with them"),
        ),
        B.commands(
            "Creating it, and watching what it costs",
            B.cmd(
                "az cosmosdb sql container create -g zaiqa-rg -a zaiqa-cosmos -d zaiqa -n events --partition-key-path /restaurantId --throughput 400",
                "Creating a container, which is when the partition key is decided.",
                "The partition key cannot be changed afterwards without copying the data, so this one argument is the most consequential in the whole service.",
            ),
            B.cmd(
                "az cosmosdb sql container throughput update -g zaiqa-rg -a zaiqa-cosmos -d zaiqa -n events --throughput 1000",
                "When reads are being throttled with 429s.",
                "Raises provisioned RUs per second. The alternative answer — and usually the better one — is to stop running cross-partition queries.",
            ),
            B.cmd(
                "az cosmosdb sql container throughput migrate -g zaiqa-rg -a zaiqa-cosmos -d zaiqa -n events --throughput-type autoscale",
                "When load is spiky, like a Friday night.",
                "Autoscale charges for the peak it reaches rather than a flat provision, which is cheaper than provisioning for Friday and paying for it on Tuesday.",
            ),
            B.cmd(
                "az cosmosdb show -g zaiqa-rg -n zaiqa-cosmos --query documentEndpoint -o tsv",
                "When wiring the SDK up with a managed identity.",
                "Gives the endpoint the client needs. With an identity there is no key to fetch, which is the configuration the exam prefers.",
            ),
        ),
        B.mistakes(
            "Creating a CosmosClient per request. It holds connections and a routing map, and building it each time is most of the latency people blame on Cosmos.",
            "Querying with SELECT * WHERE id = ... when a point read would do. Same answer, several times the RUs.",
            "Reading a 429 as a Cosmos fault. It is the service saying you asked for more RUs than you bought.",
        ),
        B.recap(
            "One client, reused for the life of the process.",
            "Point read when you know the id and the key.",
            "429 means throttled: either buy more RUs or ask for less.",
        ),
    )

    private val cosmosPerf = notes(
        "ai-cosmos-perf",
        """
            Cosmos performance is three dials: the partition key that spreads the load, the indexing policy that
            decides write cost, and the consistency level that trades freshness for latency.
        """,
        B.glossary(
            "Hot partition" to "One partition taking most of the traffic because the key does not spread it. The container is throttled while most of it idles.",
            "Logical partition" to "All items sharing one partition key value, capped at 20 GB. A key with few values eventually hits that wall.",
            "Indexing policy" to "Which paths are indexed. Everything, by default, which makes writes cost more than they need to.",
            "Consistency level" to "How fresh a read is: strong, bounded staleness, session, consistent prefix, eventual.",
            "Session consistency" to "The default: your own writes are visible to you. Cheaper than strong and right for most apps.",
        ),
        B.diagram(
            caption = "The three dials, and what each one costs you if you turn it the wrong way.",
            nodes = listOf(
                DNode("key", "Partition key", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "spread, or hot", hue = DHue.CHOICE),
                DNode("index", "Indexing policy", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "write cost", hue = DHue.CHOICE),
                DNode("cons", "Consistency", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "fresh, or fast", hue = DHue.CHOICE),
                DNode("ru", "What you pay", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "RUs per second", hue = DHue.DATA),
            ),
            edges = listOf(DEdge("key", "index"), DEdge("index", "cons"), DEdge("cons", "ru")),
            aspect = 1.0f,
        ),
        B.table(
            listOf("Level", "You get", "Costs"),
            listOf("Strong", "The latest write, always", "Most RUs, and single-region reads"),
            listOf("Bounded staleness", "Behind by at most K writes or T seconds", "More than session, less than strong"),
            listOf("Session", "Your own writes back", "The default, and the right answer most often"),
            listOf("Consistent prefix", "Never out of order, possibly behind", "Cheap"),
            listOf("Eventual", "Anything, eventually", "Cheapest reads"),
        ),
        B.commands(
            "Turning the dials",
            B.cmd(
                "az cosmosdb update -g zaiqa-rg -n zaiqa-cosmos --default-consistency-level Session",
                "When a question asks for the cheapest level that still shows a user their own order.",
                "Sets the account default. Session is that answer, and strong would halve the read throughput for a guarantee the app does not need.",
            ),
            B.cmd(
                "az cosmosdb sql container update -g zaiqa-rg -a zaiqa-cosmos -d zaiqa -n events --idx @indexing.json",
                "When writes cost far more RUs than the items would suggest.",
                "Replaces the indexing policy. Excluding paths nobody queries is the cheapest performance win in the service, because every indexed path is paid for on every write.",
            ),
            B.cmd(
                "az cosmosdb sql container show -g zaiqa-rg -a zaiqa-cosmos -d zaiqa -n events --query resource.partitionKey",
                "When diagnosing throttling you cannot explain.",
                "Tells you what the data is actually spread by, which is the first thing to check when one partition is hot and the rest are idle.",
            ),
            B.cmd(
                "az monitor metrics list --resource <cosmos-id> --metric NormalizedRUConsumption --interval PT1M",
                "When you suspect a hot partition rather than a small budget.",
                "This metric is per partition: near 100 per cent on one and low on the others is a hot partition, and buying more RUs will not fix it.",
            ),
        ),
        B.mistakes(
            "Choosing a partition key with few values, such as a country. It cannot spread, and each value is capped at 20 GB.",
            "Raising RUs to fix a hot partition. The hot one still cannot go faster.",
            "Using strong consistency because it sounds safest, then wondering why reads are slow and multi-region reads are gone.",
        ),
        B.recap(
            "A good key has many values and spreads the traffic evenly.",
            "Index only what you query; every indexed path is a write cost.",
            "Session is the default and usually the answer.",
        ),
    )

    private val cosmosVector = notes(
        "ai-cosmos-vector",
        """
            Vector search in Cosmos DB: store an embedding beside each dish, declare a vector index, and order by
            VectorDistance to find the dishes closest in meaning to what someone typed.
        """,
        B.glossary(
            "Embedding" to "A list of numbers that stands for a piece of text's meaning, produced by a model. Similar meanings give nearby vectors.",
            "Vector index" to "An index built for nearest-neighbour search, declared in the container's vector embedding policy before you query.",
            "VectorDistance" to "The function in a query that scores how close two vectors are, used in ORDER BY to rank results.",
            "DiskANN" to "The index type for large vector sets: approximate, fast, and the default choice beyond a few thousand items.",
            "RAG" to "Retrieval-augmented generation: find the relevant text first, then hand it to a model as context.",
        ),
        B.diagram(
            caption = "A search for comfort food: the words become a vector, and the nearest dish vectors come back ranked.",
            nodes = listOf(
                DNode("text", "What they typed", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "something comforting"),
                DNode("embed", "Embedding model", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "text to vector", hue = DHue.COMPUTE),
                DNode("index", "Vector index", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "DiskANN on /vector", hue = DHue.DATA),
                DNode("rank", "Nearest dishes", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "ORDER BY distance", hue = DHue.CHOICE),
                DNode("answer", "Shown, or sent on", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "results, or RAG", hue = DHue.FLOW),
            ),
            edges = listOf(
                DEdge("text", "embed"), DEdge("embed", "index", label = "query vector"),
                DEdge("index", "rank"), DEdge("rank", "answer"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("Index type", "How it searches", "Use it when"),
            listOf("flat", "Exactly, by comparing everything", "A few thousand vectors at most"),
            listOf("quantizedFlat", "Exactly, on compressed vectors", "Middling sets where memory matters"),
            listOf("diskANN", "Approximately, through a graph", "Large sets, and the usual production answer"),
        ),
        B.commands(
            "Enabling it, and creating the container",
            B.cmd(
                "az cosmosdb update -g zaiqa-rg -n zaiqa-cosmos --capabilities EnableNoSQLVectorSearch",
                "Once, before any vector work on the account.",
                "Vector search is a capability, not on by default, and a query against a container created before it will simply not use an index.",
            ),
            B.cmd(
                "az cosmosdb sql container create -g zaiqa-rg -a zaiqa-cosmos -d zaiqa -n dishes --partition-key-path /restaurantId --vector-embedding-policy @vector-policy.json",
                "Creating the container the vectors live in.",
                "The embedding policy names the path, the dimensions and the distance function, and it has to be set at creation rather than added later.",
            ),
            B.cmd(
                "az cosmosdb sql container update -g zaiqa-rg -a zaiqa-cosmos -d zaiqa -n dishes --idx @vector-index.json",
                "When queries work but are slow and expensive.",
                "Adds the vector index. Without one the query still returns the right answer by comparing every item, which is why it is correct and unaffordable.",
            ),
        ),
        B.mistakes(
            "Embedding the query with one model and the items with another. The numbers are then not comparable, and the results look random.",
            "Omitting the vector index and concluding vector search is slow.",
            "Putting the vector in the partition key path. It spreads nothing and breaks every query.",
        ),
        B.recap(
            "Same model for the items and the query, always.",
            "Policy at creation, index before you need the speed.",
            "diskANN for anything large.",
        ),
    )

    private val changeFeed = notes(
        "ai-cosmos-changefeed",
        """
            The Cosmos change feed is an ordered record of every create and update in a container, which other
            work reads at its own pace to react — and it never shows a delete.
        """,
        B.glossary(
            "Change feed" to "Every create and update to a container, in the order they happened per partition, readable from any point.",
            "Lease container" to "Where a processor stores how far it has read, so it can resume and so several instances can share the work.",
            "Change feed processor" to "The SDK component that reads the feed, hands you batches, and keeps the leases.",
            "Soft delete" to "Marking an item deleted instead of removing it, which is how a deleted item reaches the feed at all.",
            "At-least-once" to "A batch may be delivered twice, so the work that reads it must be safe to repeat.",
        ),
        B.diagram(
            caption = "An order event lands in Cosmos, and the feed starts the work that reacts to it.",
            nodes = listOf(
                DNode("write", "Order event written", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "by the worker", hue = DHue.COMPUTE),
                DNode("feed", "Change feed", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "create and update", hue = DHue.FLOW),
                DNode("proc", "Processor", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "keeps the lease", hue = DHue.COMPUTE),
                DNode("lease", "Lease container", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "how far it has read", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("side", "Side effects", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "notify, aggregate", hue = DHue.COMPUTE),
            ),
            edges = listOf(
                DEdge("write", "feed"), DEdge("feed", "proc"),
                DEdge("proc", "lease", label = "checkpoint"), DEdge("proc", "side"),
            ),
            aspect = 1.1f,
        ),
        B.compare(
            "Change feed, or Service Bus?",
            B.card("Change feed", "The trigger is that data changed.", "Replayable from any point", "Ordered per partition", "No delete events"),
            B.card("Service Bus", "The trigger is a decision your code made.", "Dead letters and retries", "Scheduled and deferred messages", "Sessions for ordering"),
        ),
        B.commands(
            "The lease container, and a replay",
            B.cmd(
                "az cosmosdb sql container create -g zaiqa-rg -a zaiqa-cosmos -d zaiqa -n leases --partition-key-path /id --throughput 400",
                "Before the first processor starts.",
                "The processor needs somewhere to record its position; without a lease container it cannot resume or share work, and it will not start.",
            ),
            B.cmd(
                "az cosmosdb sql container delete -g zaiqa-rg -a zaiqa-cosmos -d zaiqa -n leases",
                "When you deliberately want to reprocess the whole feed from the beginning.",
                "Clearing the leases is the replay: the processor comes back with no position and reads from the start, which is why it must never be done casually.",
            ),
            B.cmd(
                "az cosmosdb sql container show -g zaiqa-rg -a zaiqa-cosmos -d zaiqa -n events --query resource.defaultTtl",
                "When old items vanish from the feed before anything read them.",
                "A time-to-live expiry removes items quietly, and an expired item is gone from the feed too, which looks exactly like a processor that skipped work.",
            ),
        ),
        B.mistakes(
            "Expecting deletes. The feed has none; soft-delete with a flag if a consumer must see them.",
            "Writing a processor that is not safe to run twice. Delivery is at least once.",
            "Sharing one lease container between two unrelated consumers, which makes them split the work instead of each seeing all of it.",
        ),
        B.recap(
            "Creates and updates only, in order, per partition.",
            "Leases hold the position; deleting them replays everything.",
            "Make the work idempotent, because a batch can arrive twice.",
        ),
    )

    private val postgres = notes(
        "ai-postgres",
        """
            Azure Database for PostgreSQL flexible server holds Zaiqa's payouts ledger: Entra sign-in, a private
            or firewalled endpoint, connection pooling, and indexes you add deliberately.
        """,
        B.glossary(
            "Flexible server" to "The deployment model to use: you choose the zone, the maintenance window and whether to stop it.",
            "PgBouncer" to "The built-in connection pooler. Postgres connections are expensive, and a serverless caller opens many.",
            "Entra authentication" to "Signing in with an Azure identity instead of a password, which is how a managed identity connects.",
            "B-tree index" to "The ordinary index: equality and ranges on a column. What most slow queries actually want.",
            "EXPLAIN ANALYZE" to "Runs the query and prints the plan with real timings, which is how you find the missing index rather than guessing.",
        ),
        B.diagram(
            caption = "The API reaching Postgres through the pooler, signed in as an identity rather than with a password.",
            nodes = listOf(
                DNode("api", "Zaiqa API", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "managed identity", hue = DHue.COMPUTE),
                DNode("pool", "PgBouncer", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "port 6432", hue = DHue.FLOW),
                DNode("pg", "Flexible server", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "payouts ledger", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("idx", "Indexes", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "added deliberately", hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("api", "pool", label = "pooled"), DEdge("pool", "pg"),
                DEdge("idx", "pg", dashed = true),
            ),
            aspect = 1.0f,
        ),
        B.table(
            listOf("Problem", "The answer here", "Not"),
            listOf("Hundreds of short-lived connections", "PgBouncer on 6432", "A bigger server"),
            listOf("A password in configuration", "Entra authentication with a managed identity", "Key Vault holding the password"),
            listOf("One slow report", "EXPLAIN ANALYZE, then an index", "More vCores"),
            listOf("Reachable from the internet", "Private endpoint, or firewall rules", "Allowing 0.0.0.0"),
        ),
        B.commands(
            "Creating it and letting the API in",
            B.cmd(
                "az postgres flexible-server create -g zaiqa-rg -n zaiqa-pg --tier Burstable --sku-name Standard_B1ms",
                "Creating the server for a practice environment.",
                "Burstable is the cheap tier that is fine for coursework, and naming the tier explicitly avoids provisioning something that bills like production.",
            ),
            B.cmd(
                "az postgres flexible-server parameter set -g zaiqa-rg -s zaiqa-pg --name pgbouncer.enabled --value true",
                "When a serverless or scaled-out caller exhausts connections.",
                "Turns on the built-in pooler, after which the application connects to port 6432 and hundreds of short connections share a few real ones.",
            ),
            B.cmd(
                "az postgres flexible-server ad-admin create -g zaiqa-rg -s zaiqa-pg -u zaiqa-admins -i <object-id>",
                "Before any identity can sign in without a password.",
                "Sets the Entra administrator, which is the prerequisite for creating identity-based roles in the database at all.",
            ),
            B.cmd(
                "az postgres flexible-server firewall-rule create -g zaiqa-rg -n zaiqa-pg -r office --start-ip-address 203.0.113.4 --end-ip-address 203.0.113.4",
                "When you need to reach it from one known address.",
                "One address, not a range, and never 0.0.0.0: the exam treats a wide-open firewall rule as the wrong answer even when it works.",
            ),
        ),
        B.mistakes(
            "Scaling the server because connections are exhausted. Enable the pooler instead.",
            "Keeping the Postgres password in Key Vault and calling that passwordless. Entra authentication is what the exam means.",
            "Adding indexes by guesswork. Read the plan.",
        ),
        B.recap(
            "Pool the connections; do not buy vCores for them.",
            "Entra sign-in, so there is no password at all.",
            "EXPLAIN ANALYZE before any index.",
        ),
    )

    private val pgvector = notes(
        "ai-pgvector",
        """
            pgvector puts vector search inside Postgres: a vector column, an HNSW or IVFFlat index, and a
            distance operator in ORDER BY, so one database holds both the rows and the embeddings.
        """,
        B.glossary(
            "vector column" to "A column of fixed dimension, declared vector(1536), holding one embedding per row.",
            "HNSW" to "A graph index: slower to build, faster and more accurate to query. The usual production choice.",
            "IVFFlat" to "A clustering index: quick to build, needs a representative sample to exist first, and tuned by how many lists it probes.",
            "Distance operator" to "How closeness is measured: cosine, inner product or Euclidean. It must match the one the index was built for.",
            "Filtered search" to "A vector search with an ordinary WHERE beside it, which is the thing pgvector does more naturally than a separate vector store.",
        ),
        B.diagram(
            caption = "One Postgres row holding both the dish and its embedding, so a filter and a vector search run together.",
            nodes = listOf(
                DNode("row", "dishes table", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "name, price, vector", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("idx", "HNSW index", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "on the vector column", hue = DHue.DATA),
                DNode("q", "One query", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "WHERE and ORDER BY", hue = DHue.COMPUTE),
                DNode("out", "Ranked dishes", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "filtered and nearest", hue = DHue.CHOICE),
            ),
            edges = listOf(DEdge("row", "idx"), DEdge("idx", "q"), DEdge("q", "out")),
            aspect = 1.0f,
        ),
        B.compare(
            "pgvector, or Cosmos DB vectors?",
            B.card("pgvector", "The rows and the vectors belong together.", "One database, one transaction", "Filters and joins beside the search", "Familiar SQL and tooling"),
            B.card("Cosmos DB", "The scale or the spread is the problem.", "Global distribution", "Huge write rates", "A change feed to react to"),
        ),
        B.commands(
            "Enabling the extension",
            B.cmd(
                "az postgres flexible-server parameter set -g zaiqa-rg -s zaiqa-pg --name azure.extensions --value vector",
                "Once, before CREATE EXTENSION will work.",
                "Azure allow-lists extensions at the server level, so the SQL command fails with a permissions error until this has run — which is the usual first stumble.",
            ),
            B.cmd(
                "psql \"host=zaiqa-pg.postgres.database.azure.com dbname=zaiqa\" -c \"CREATE EXTENSION vector\"",
                "In the database that will hold the vectors.",
                "Extensions are per database, not per server, so enabling it once does not cover a second database on the same server.",
            ),
            B.cmd(
                "psql \"host=zaiqa-pg.postgres.database.azure.com dbname=zaiqa\" -c \"CREATE INDEX ON dishes USING hnsw (embedding vector_cosine_ops)\"",
                "When a vector query is correct but scans the table.",
                "The operator class has to match the distance operator the query uses, or the planner ignores the index and you pay for a full scan.",
            ),
        ),
        B.mistakes(
            "Building an IVFFlat index on an empty table. It needs data to cluster, and an index built on nothing stays useless.",
            "Mixing cosine and Euclidean between the index and the query, so the index is never used.",
            "Forgetting the extension is per database.",
        ),
        B.recap(
            "Allow-list the extension on the server, then create it in the database.",
            "HNSW for production; match the operator class to the query.",
            "pgvector wins when you need filters and joins beside the search.",
        ),
    )

    private val redis = notes(
        "ai-redis",
        """
            Azure Managed Redis holds what is true right now: the hot restaurant list, the rider's position and
            rate limits, with a time to live on everything and a cache-aside read.
        """,
        B.glossary(
            "Cache-aside" to "Read the cache; on a miss read the database, put it in the cache, return it. The pattern almost every question means.",
            "Time to live" to "The expiry on a key. A cache without one is a second database that slowly tells lies.",
            "Cache stampede" to "A popular key expiring and every caller hitting the database at once.",
            "Eviction policy" to "What Redis throws away when it is full: allkeys-lru is the usual choice for a cache.",
            "Rate limiting" to "Counting a caller's requests in a short-lived key, which Redis does atomically and cheaply.",
        ),
        B.diagram(
            caption = "Cache-aside: the hit answers immediately, and only a miss reaches the database.",
            nodes = listOf(
                DNode("req", "Request", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "hot restaurants"),
                DNode("redis", "Redis", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "with a TTL", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("hit", "Hit", 0.27f, 0.58f, w = 0.42f, h = 0.15f, sub = "answer now", hue = DHue.CHOICE),
                DNode("miss", "Miss", 0.73f, 0.58f, w = 0.42f, h = 0.15f, sub = "read SQL, fill", hue = DHue.CHOICE),
                DNode("sql", "Azure SQL", 0.5f, 0.88f, w = 0.62f, h = 0.15f, sub = "the truth", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("req", "redis"), DEdge("redis", "hit"), DEdge("redis", "miss"), DEdge("miss", "sql"),
            ),
            aspect = 1.05f,
        ),
        B.table(
            listOf("Pattern", "What it is", "Watch out for"),
            listOf("Cache-aside", "Miss reads the database and fills the cache", "The stampede when a hot key expires"),
            listOf("Write-through", "Write the cache and the database together", "Slower writes, and two things to fail"),
            listOf("Session store", "Keep a user's session in Redis", "Needs persistence, or sessions die with the node"),
            listOf("Rate limiter", "A counter per caller with a short TTL", "Must be atomic; use INCR, not read-then-write"),
        ),
        B.commands(
            "Creating it, and looking inside",
            B.cmd(
                "az redis create -g zaiqa-rg -n zaiqa-cache --sku Basic --vm-size c0",
                "Standing up a cache for coursework.",
                "Basic is one node with no replica, which is right for practice and wrong for production, where Standard gives you the failover.",
            ),
            B.cmd(
                "az redis update -g zaiqa-rg -n zaiqa-cache --set redisConfiguration.maxmemory-policy=allkeys-lru",
                "Before the cache is big enough to fill up.",
                "Decides what is thrown away when memory runs out. The default refuses new writes instead of evicting, which turns a full cache into an outage.",
            ),
            B.cmd(
                "az redis list-keys -g zaiqa-rg -n zaiqa-cache",
                "Only when a managed identity is not available.",
                "Prints the access keys. Worth knowing, but the exam's preferred answer for Managed Redis is Entra authentication rather than a key in configuration.",
            ),
            B.cmd(
                "redis-cli -h zaiqa-cache.redis.cache.windows.net --tls INFO keyspace",
                "When you need to know whether anything is actually being cached.",
                "Shows the key count and expiry statistics per database, which answers the usual question of whether a miss rate is the cache's fault or the code's.",
            ),
        ),
        B.mistakes(
            "Caching without a TTL. The cache then holds stale truth indefinitely.",
            "Reading a counter, adding one and writing it back. Two callers lose a count; INCR does not.",
            "Leaving the eviction policy at noeviction, so a full cache starts failing writes.",
        ),
        B.recap(
            "Cache-aside, with a TTL on everything.",
            "allkeys-lru, so a full cache evicts instead of failing.",
            "Atomic operations for counters, always.",
        ),
    )

    val all: List<TopicNotes> = listOf(troubleshoot, cosmosSdk, cosmosPerf, cosmosVector, changeFeed, postgres, pgvector, redis)
}
