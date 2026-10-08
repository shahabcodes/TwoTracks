package com.shahaabapps.core.content

/** AZ-204 module 2, Storage: Cosmos DB topics and the module test. */
internal object Az204Storage2 {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val cosmosModel = Topic(
        id = "az-cosmos-model", title = "Cosmos DB: accounts, APIs, consistency and partitions", minutes = 30,
        references = listOf(
            B.ref("Azure Cosmos DB overview", LEARN + "azure/cosmos-db/introduction"),
            B.ref("Consistency levels", LEARN + "azure/cosmos-db/consistency-levels"),
            B.ref("Partitioning and horizontal scaling", LEARN + "azure/cosmos-db/partitioning-overview"),
            B.ref("Request units", LEARN + "azure/cosmos-db/request-units"),
            B.ref("Learn module: Explore Azure Cosmos DB", LEARN + "training/modules/explore-azure-cosmos-db/"),
        ),
        blocks = listOf(
            B.plain("""
                Cosmos DB is a database that stores JSON documents, replicates them to any region you pick, and
                answers in single-digit milliseconds. You pay in request units, a currency for work done.
            """),
            B.h("The hierarchy"),
            B.diagram(
                caption = "Account, database, container, item. Throughput and the partition key are set on the container.",
                nodes = listOf(
                    DNode("acct", "Account", 0.5f, 0.12f, w = 0.4f, h = 0.16f, kind = NodeKind.CYLINDER, tone = 1, sub = "API, regions, consistency"),
                    DNode("db", "Database: twotracks", 0.5f, 0.4f, w = 0.46f, h = 0.16f, tone = 2),
                    DNode("c1", "Container: logs", 0.28f, 0.68f, w = 0.36f, h = 0.16f, sub = "partition key /owner"),
                    DNode("c2", "Container: sessions", 0.72f, 0.68f, w = 0.4f, h = 0.16f, sub = "partition key /date"),
                    DNode("i", "{ id, owner, minutes }", 0.28f, 0.92f, w = 0.4f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("acct", "db"), DEdge("db", "c1"), DEdge("db", "c2"), DEdge("c1", "i")),
                aspect = 0.72f,
            ),
            B.h("Pick the API when you create the account"),
            B.table(
                listOf("API", "Use when", "Note"),
                listOf("NoSQL (Core)", "New apps, JSON documents, SQL-like queries", "Richest features; the exam default"),
                listOf("MongoDB", "Existing Mongo code or drivers", "Wire-compatible"),
                listOf("PostgreSQL", "Relational with Citus scale-out", "Distributed Postgres"),
                listOf("Cassandra, Gremlin, Table", "Existing CQL, graph, or Table Storage apps", "Migration paths"),
            ),
            B.tip("The API cannot be changed after creation. If the question says \"minimal code changes from MongoDB\", choose the MongoDB API, not NoSQL."),
            B.h("Consistency: how fresh must a read be?"),
            B.table(
                listOf("Level", "Promise", "Cost"),
                listOf("Strong", "Always the latest committed write", "Highest latency, most RU"),
                listOf("Bounded staleness", "Lag at most K versions or T seconds", "Predictable staleness"),
                listOf("Session", "Your own writes are always visible to you", "The default; best balance"),
                listOf("Consistent prefix", "Never out of order, may be stale", "Cheap"),
                listOf("Eventual", "Will converge, order not guaranteed", "Cheapest, fastest"),
            ),
            B.remember("Session is the default and what most apps want: a user sees their own update immediately, and other users soon after."),
            B.check("az-cosmos-model-k1", "az-cosmos-model", "A leaderboard may show scores slightly late but never out of order. Which level?",
                listOf("Strong", "Consistent prefix", "Eventual"), 1, "Consistent prefix guarantees order without the cost of Strong."),
            B.check(
                "az-cosmos-model-k2", "az-cosmos-model",
                "Which consistency level does a new Cosmos account default to?",
                listOf("Session", "Strong", "Eventual"), 0,
                "Session is the default: a client reads its own writes, at far less cost than Strong.",
                whyWrong = mapOf(
                    1 to "Strong is the most expensive and the slowest, and is not what you get unless you ask.",
                    2 to "Eventual is the cheapest, and is not the default either.",
                ),
            ),
            B.h("Partition keys: the exam's favourite"),
            B.p("""
                Every container is split into logical partitions by the value of one property, the partition key.
                Items with the same key live together, and 20 GB is the limit per logical partition. Queries that
                include the key are cheap; queries that fan out across all partitions are expensive.
            """),
            B.diagram(
                caption = "A good key spreads writes evenly and keeps the common query inside one partition.",
                nodes = listOf(
                    DNode("q", "Query: logs for owner = usaid", 0.5f, 0.12f, w = 0.7f, h = 0.14f, tone = 1),
                    DNode("p1", "/owner = usaid", 0.2f, 0.55f, w = 0.3f, h = 0.3f, sub = "all his logs"),
                    DNode("p2", "/owner = sanya", 0.5f, 0.55f, w = 0.3f, h = 0.3f, tone = 2, sub = "untouched"),
                    DNode("p3", "/owner = …", 0.8f, 0.55f, w = 0.3f, h = 0.3f, tone = 2, sub = "untouched"),
                    DNode("n", "in-partition query: cheap", 0.5f, 0.9f, w = 0.6f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("q", "p1")),
                aspect = 0.7f,
            ),
            B.compare("Which partition key?",
                B.card("/owner (user id)", "Most queries are per user and users are many.", "Even spread across users", "Per-user queries stay in one partition", "A single very heavy user can hit 20 GB"),
                B.card("/date", "You query by day and days are many.", "Even over time", "Today's partition is hot for writes", "Per-user queries fan out"),
                B.card("/country", "Never.", "Only a few values", "One huge partition", "Writes pile onto one value"),
            ),
            B.worked("""
                Worked example. Two Tracks stores study logs with partition key /owner. Usaid's Today page asks for
                his logs this week: one partition, about 3 RU. A report across both partners fans out to two
                partitions, still fine. If the key had been /country, both users would share one partition and every
                write would queue on it.
            """),
            B.h("Request units and throughput"),
            B.bullets(
                "1 RU is roughly a 1 KB point read. A query costs more; a write costs about 5 RU per KB.",
                "Provisioned throughput: set RU/s on the container (or database); you pay for it whether used or not. Autoscale sets a max and scales down to 10 percent.",
                "Serverless: pay per RU consumed; good for spiky or small workloads with no steady load.",
            ),
            B.tip("\"Unpredictable traffic, cost must follow usage\" is serverless or autoscale. \"Steady high load\" is provisioned. A 429 response means you ran out of RU; retry with backoff or raise the limit."),
            B.steps("Create the account and container",
                "az cosmosdb create --name tt-cosmos --resource-group tt-rg --default-consistency-level Session --locations regionName=westeurope" to "A NoSQL API account in one region.",
                "az cosmosdb sql database create --account-name tt-cosmos --resource-group tt-rg --name twotracks" to "The database exists.",
                "az cosmosdb sql container create --account-name tt-cosmos --resource-group tt-rg --database-name twotracks --name logs --partition-key-path /owner --throughput 400" to "A container with 400 RU/s provisioned.",
            ),
            B.mistakes(
                "Choosing a partition key with few distinct values. Writes and storage pile up.",
                "Changing the partition key later. You cannot; you migrate to a new container.",
                "Using Strong consistency everywhere. It doubles the RU of reads and adds latency across regions.",
                "Ignoring 429s. Handle throttling with retries or autoscale.",
            ),
            B.example("Two Tracks: NoSQL API, Session consistency, serverless while the user base is two people, partition key /owner on logs and /date on sessions."),
            B.check(
                "az-cosmos-model-k3", "az-cosmos-model",
                "A container is partitioned by country, and most of the traffic is one country. What is wrong with that?",
                listOf("A hot partition — and the key cannot be changed later", "Nothing, as long as the RU/s is high enough", "Reads will come back stale"), 0,
                "A key needs many values, which spread the load and which the common query includes. It is also fixed when the container is created.",
                whyWrong = mapOf(
                    1 to "Raising RU/s does not help: one logical partition has its own throughput limit and caps at 20 GB.",
                    2 to "Staleness is a consistency question. This is a partitioning one.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-s-cosmos-match", "Pair each consistency level with its promise.",
                "Strong" to "Always the latest committed write",
                "Bounded staleness" to "Behind by at most K versions or T seconds",
                "Session" to "Your own writes are always visible to you",
                "Eventual" to "Will converge, with no order guaranteed",
            ),
            B.gap(
                "az-s-cosmos-gap1", "The level Cosmos gives you unless you choose otherwise.",
                "The default consistency is ___.", "Session", "session",
            ),
            B.gap(
                "az-s-cosmos-gap2", "A rough figure the exam expects you to know.",
                "A 1 KB point read costs about ___ request unit.", "1", "one",
            ),
            B.tryIt("Try it in 15 minutes", "The free tier gives 1000 RU/s and 25 GB on one account per subscription.",
                "Create a free-tier NoSQL account", "Create a container with partition key /owner", "Insert two items with different owners in Data Explorer", "Run SELECT * FROM c WHERE c.owner = 'usaid' and read the RU charge in the query stats",
            ),
            B.explain(
                "az-cosmos-model-x1",
                "Explain what makes a good partition key, and what consistency level you would start with.",
                "A partition key decides which logical partition an item lives in, and all the items sharing a value live together with a 20 GB ceiling and their own throughput. So a good key has many values, spreads the writes evenly, and appears in the queries you run most, letting them hit one partition instead of fanning out. It cannot be changed after the container is created, which is what makes the choice matter. Session is the default consistency and the sensible start: a client reads its own writes, where Strong costs the most and Eventual promises least.",
                "Many values, spread evenly" to listOf("many values", "spread", "even", "distribute", "hot partition"),
                "The common query should include it" to listOf("query", "queries", "include", "single partition", "fan out"),
                "It cannot be changed later" to listOf("cannot change", "cannot be changed", "fixed", "immutable", "creation"),
            ),
            B.glossary(
                "Request unit (RU)" to "The unit of work; about a 1 KB point read.",
                "Partition key" to "The item property that decides which logical partition an item lives in.",
                "Logical partition" to "All items sharing one partition key value; 20 GB maximum.",
                "Consistency level" to "The freshness promise for reads across replicas.",
                "429" to "The status code for throttling when RU/s is exhausted.",
            ),
            B.recap(
                "Account, database, container, item; the API is fixed at creation.",
                "Session consistency is the default; Strong costs most, Eventual least.",
                "Pick a partition key with many values that your common query includes; you cannot change it later.",
            ),
        ),
    )

    private val cosmosSdk = Topic(
        id = "az-cosmos-sdk", title = "Cosmos DB in code: SDK, queries, stored procedures and the change feed", minutes = 28,
        references = listOf(
            B.ref("Quickstart: Azure Cosmos DB for NoSQL with .NET", LEARN + "azure/cosmos-db/nosql/quickstart-dotnet"),
            B.ref("Query items in Azure Cosmos DB for NoSQL", LEARN + "azure/cosmos-db/nosql/query/getting-started"),
            B.ref("Stored procedures, triggers and UDFs", LEARN + "azure/cosmos-db/nosql/stored-procedures-triggers-udfs"),
            B.ref("Change feed in Azure Cosmos DB", LEARN + "azure/cosmos-db/change-feed"),
            B.ref("Learn module: Work with Azure Cosmos DB", LEARN + "training/modules/work-with-cosmos-db/"),
        ),
        blocks = listOf(
            B.plain("The .NET SDK mirrors the hierarchy: CosmosClient, Database, Container. Reads by id plus partition key are the cheapest thing you can do."),
            B.code("csharp", """
                var client = new CosmosClient("https://tt-cosmos.documents.azure.com:443/", new DefaultAzureCredential());
                var container = client.GetContainer("twotracks", "logs");

                var log = new StudyLog { id = Guid.NewGuid().ToString(), owner = "usaid", minutes = 75, date = "2026-10-06" };
                await container.CreateItemAsync(log, new PartitionKey(log.owner));

                // Point read: id + partition key, about 1 RU
                var read = await container.ReadItemAsync<StudyLog>(log.id, new PartitionKey("usaid"));

                // Query within one partition
                var q = new QueryDefinition("SELECT * FROM c WHERE c.owner = @o AND c.date >= @d").WithParameter("@o", "usaid").WithParameter("@d", "2026-10-01");
                using var it = container.GetItemQueryIterator<StudyLog>(q, requestOptions: new QueryRequestOptions { PartitionKey = new PartitionKey("usaid") });
                while (it.HasMoreResults) foreach (var l in await it.ReadNextAsync()) Console.WriteLine(l.minutes);
            """),
            B.diagram(
                caption = "Point read versus query: the point read goes straight to one item; a query without the key asks every partition.",
                nodes = listOf(
                    DNode("pr", "ReadItem(id, pk)", 0.25f, 0.2f, w = 0.4f, h = 0.16f, tone = 1, sub = "~1 RU"),
                    DNode("one", "one partition", 0.25f, 0.6f, w = 0.3f, h = 0.2f),
                    DNode("qx", "Query without pk", 0.75f, 0.2f, w = 0.4f, h = 0.16f, tone = 1, sub = "fan-out"),
                    DNode("a", "p1", 0.6f, 0.6f, w = 0.12f, h = 0.16f, tone = 2), DNode("b", "p2", 0.75f, 0.6f, w = 0.12f, h = 0.16f, tone = 2), DNode("c", "p3", 0.9f, 0.6f, w = 0.12f, h = 0.16f, tone = 2),
                ),
                edges = listOf(DEdge("pr", "one"), DEdge("qx", "a"), DEdge("qx", "b"), DEdge("qx", "c")),
                aspect = 0.5f,
            ),
            B.h("Upsert, patch and optimistic concurrency"),
            B.bullets(
                "UpsertItemAsync writes or replaces. PatchItemAsync changes named fields without sending the whole document.",
                "Every item has an _etag. Pass IfMatchEtag on a replace to fail if someone else changed it first (HTTP 412).",
                "TTL: set DefaultTimeToLive on the container and ttl on items to expire them automatically.",
            ),
            B.check("az-cosmos-sdk-k1", "az-cosmos-sdk", "Two app instances may update the same document at once; the second must not overwrite the first blindly. What do you use?",
                listOf("A stored procedure", "IfMatchEtag on the replace", "Strong consistency"), 1, "The ETag check makes the second write fail with 412 so it can re-read and retry."),
            B.check(
                "az-cosmos-sdk-k2", "az-cosmos-sdk",
                "Two writers must not overwrite each other's change. What do you use?",
                listOf("The ETag, with an if-match condition", "A stored procedure", "A stronger consistency level"), 0,
                "The ETag is a version stamp: send it back with the write and it fails if the item has moved on.",
                whyWrong = mapOf(
                    1 to "A stored procedure gives a transaction inside one partition, which is a different problem from two clients racing.",
                    2 to "Consistency governs how fresh a read is, not whether a write lands on top of someone else's.",
                ),
            ),
            B.h("Server-side code"),
            B.table(
                listOf("Kind", "Runs", "Use"),
                listOf("Stored procedure", "In the database, inside one partition, as a transaction", "Insert several items atomically"),
                listOf("Pre-trigger", "Before a write", "Validate or add a timestamp"),
                listOf("Post-trigger", "After a write, same transaction", "Update a counter document"),
                listOf("User-defined function", "Inside a query", "Format or compute a value"),
            ),
            B.code("javascript", """
                // Stored procedure: create two items or none, in one partition
                function createPair(a, b) {
                  var ctx = getContext(), coll = ctx.getCollection();
                  coll.createDocument(coll.getSelfLink(), a, function (err) {
                    if (err) throw err;
                    coll.createDocument(coll.getSelfLink(), b, function (err2) { if (err2) throw err2; ctx.getResponse().setBody("ok"); });
                  });
                }
            """),
            B.remember("Stored procedures and triggers are JavaScript, scoped to one partition key, and transactional. Triggers must be named on the request to run; they are not automatic."),
            B.h("Change feed: react to every write"),
            B.p("""
                The change feed is an ordered log of inserts and updates per partition. An Azure Function with a
                Cosmos DB trigger, or the change feed processor in the SDK, reads it and does work: update a
                materialised view, send a notification, copy to another store. Deletes are not in the feed unless you
                enable the all-versions-and-deletes mode (preview).
            """),
            B.code("csharp", """
                [Function("OnLogWritten")]
                public void Run([CosmosDBTrigger("twotracks", "logs", Connection = "Cosmos", LeaseContainerName = "leases", CreateLeaseContainerIfNotExists = true)] IReadOnlyList<StudyLog> changes)
                {
                    foreach (var l in changes) _streaks.Recompute(l.owner);
                }
            """),
            B.worked("""
                Worked example. Two Tracks keeps a streaks document per user. Instead of recomputing it on every page
                load, a change-feed function updates it whenever a log is written. The lease container tracks how
                far each function instance has read, so two instances never process the same change.
            """),
            B.steps("Wire a change-feed function",
                "Create a leases container with partition key /id." to "Small; stores checkpoints.",
                "Add the Cosmos DB trigger with LeaseContainerName = leases." to "The function starts reading from now.",
                "Write an item to logs." to "The function fires within a second with a batch of one.",
                "Set StartFromBeginning = true to replay history." to "Every existing item is delivered once.",
            ),
            B.mistakes(
                "Running a cross-partition query where a point read would do.",
                "Expecting a trigger to run without naming it in the request options.",
                "Expecting deletes in the change feed. Use a soft-delete flag or TTL with the all-versions mode.",
                "Sharing one lease container between two different functions without a LeaseContainerPrefix.",
            ),
            B.example("Two Tracks: point reads for goal pages, in-partition queries for weekly logs, a change-feed function for streaks, TTL of 90 days on planned sessions."),
            B.check(
                "az-cosmos-sdk-k3", "az-cosmos-sdk",
                "Which of these does the change feed NOT give you?",
                listOf("Deletes", "Inserts", "Updates"), 0,
                "The change feed carries inserts and updates. To react to a removal you model it as a soft delete, usually with a TTL.",
                whyWrong = mapOf(
                    1 to "Inserts are the obvious case, and are included.",
                    2 to "Updates are included too, in order within a partition.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-s-cosmossdk-match", "Pair each piece of server-side code with where it runs.",
                "Stored procedure" to "Inside one partition, as a transaction",
                "Pre-trigger" to "Before a write, to validate or stamp it",
                "Post-trigger" to "After a write, in the same transaction",
                "User-defined function" to "Inside a query, to format or compute",
            ),
            B.gap(
                "az-s-cosmossdk-gap1", "Someone else changed the document first, so the replace must fail rather than overwrite.",
                "Pass IfMatchEtag and expect HTTP ___ when it clashes.", "412",
            ),
            B.gap(
                "az-s-cosmossdk-gap2", "Change named fields without sending the whole document back.",
                "Call ___ItemAsync.", "Patch", "patch",
            ),
            B.tryIt("Try it in 15 minutes", "Uses the free-tier account from the previous topic.",
                "Insert an item from .NET with CreateItemAsync", "Read it back with ReadItemAsync and print RequestCharge", "Run the same as a query without the partition key and compare the charge", "Add ttl = 60 to an item and watch it vanish",
            ),
            B.explain(
                "az-cosmos-sdk-x1",
                "Explain the cheapest way to read an item, how to stop two writers overwriting each other, and what the change feed is for.",
                "The cheapest read is a point read: ReadItem by id and partition key, which goes straight to one item rather than running a query. For concurrent writers, send the item's ETag back with an if-match condition, and the write fails if the item has moved on since you read it. The change feed is the ordered stream of inserts and updates per partition, so a processor or a Cosmos trigger can react to every write; a lease container keeps its place. Deletes are not in the feed, so a removal has to be modelled as a soft delete.",
                "A point read by id and partition key" to listOf("point read", "readitem", "id and partition", "partition key"),
                "The ETag gives optimistic concurrency" to listOf("etag", "if-match", "concurrency", "optimistic"),
                "The change feed carries inserts and updates" to listOf("change feed", "inserts", "updates", "stream", "reacts"),
            ),
            B.glossary(
                "Point read" to "ReadItem by id and partition key; the cheapest read.",
                "ETag" to "A version stamp used for optimistic concurrency.",
                "TTL" to "Time to live; automatic expiry in seconds.",
                "Change feed" to "The ordered stream of inserts and updates per partition.",
                "Lease container" to "Where the change-feed processor stores its checkpoints.",
            ),
            B.recap(
                "Point reads by id and partition key; include the key in queries.",
                "ETags give optimistic concurrency; stored procedures give transactions inside a partition.",
                "The change feed plus a Cosmos trigger reacts to writes; a lease container keeps the place.",
            ),
        ),
    )

    val topics = listOf(cosmosModel, cosmosSdk)

    val quiz = Quiz(
        id = "az-storage-test", title = "Storage module test",
        questions = B.spread(listOf(
            B.q("s1", "az-blob-basics", "Which redundancy option lets you read from the secondary region without a failover?", listOf("LRS", "GRS", "RA-GRS", "ZRS"), setOf(2), "RA-GRS adds read access to the geo-replicated secondary. GRS copies there but reads need a failover."),
            B.q("s2", "az-blob-basics", "A blob in the Archive tier must be read. What must happen first?", listOf("Nothing, read it", "Rehydrate it to Hot or Cool", "Delete the lifecycle rule", "Change the account to premium"), setOf(1), "Archive is offline. Rehydration takes hours on standard priority."),
            B.q("s3", "az-blob-basics", "Which blob type suits an application log that only grows?", listOf("Block", "Append", "Page", "Any"), setOf(1), "Append blobs are optimised for add-only writes."),
            B.q("s4", "az-blob-basics", "A lifecycle rule should move exports to Cool after 30 days and delete after 6 years. Which actions? Select all that apply.", listOf("tierToCool", "tierToArchive", "delete", "enableAutoTierToHotFromCool"), setOf(0, 2), "tierToCool at 30 days and delete at 2190 days. Archive was not asked for."),
            B.q("s5", "az-blob-sdk", "An uploader's name must be stored with each blob and returned on every read. Which feature?", listOf("Blob index tags", "Metadata", "A lease", "Content-Type"), setOf(1), "Metadata is name-value data stored with the blob and returned with its properties."),
            B.q("s6", "az-blob-sdk", "A mobile app needs to download one private blob for ten minutes without the account key. What do you issue?", listOf("The connection string", "A user delegation SAS with Read", "An account SAS with all permissions", "A lease"), setOf(1), "A short user delegation SAS is signed with Entra ID and limited to that blob and time."),
            B.q("s7", "az-blob-sdk", "How do you revoke a service SAS without rotating the account key?", listOf("Delete the blob", "Use a stored access policy and remove it", "Set the blob to Archive", "You cannot"), setOf(1), "A SAS tied to a stored access policy dies when the policy is removed."),
            B.q("s8", "az-blob-sdk", "Which class represents one file in the .NET SDK?", listOf("BlobServiceClient", "BlobContainerClient", "BlobClient", "StorageAccount"), setOf(2), "BlobClient is a single blob; the container and service clients sit above it."),
            B.q("s9", "az-cosmos-model", "Which consistency level is the Cosmos DB default?", listOf("Strong", "Bounded staleness", "Session", "Eventual"), setOf(2), "Session: your own writes are always visible to you."),
            B.q("s10", "az-cosmos-model", "A container's partition key is /country with 5 values and millions of items. What is the problem?", listOf("Queries are slow to parse", "Few, huge logical partitions and hot writes", "Consistency drops to Eventual", "RU cost is zero"), setOf(1), "Few distinct values means huge partitions, the 20 GB limit, and write contention."),
            B.q("s11", "az-cosmos-model", "Traffic is tiny and spiky and cost must follow usage. Which throughput mode?", listOf("Provisioned 10,000 RU/s", "Serverless", "Strong consistency", "Dedicated gateway"), setOf(1), "Serverless bills per RU consumed with no standing charge."),
            B.q("s12", "az-cosmos-model", "Your app gets HTTP 429 from Cosmos DB. What does it mean?", listOf("Item not found", "Throttled: RU/s exhausted", "Partition key missing", "Consistency violation"), setOf(1), "429 is throttling. Retry with backoff or increase throughput."),
            B.q("s13", "az-cosmos-model", "Which API should an existing MongoDB app use to move to Cosmos DB with minimal changes?", listOf("NoSQL", "MongoDB", "Table", "Gremlin"), setOf(1), "The MongoDB API is wire-compatible with Mongo drivers."),
            B.q("s14", "az-cosmos-sdk", "What is the cheapest way to fetch one document when you know its id and partition key?", listOf("A SQL query by id", "ReadItemAsync (point read)", "A stored procedure", "The change feed"), setOf(1), "A point read costs about 1 RU per KB."),
            B.q("s15", "az-cosmos-sdk", "Which server-side feature writes several items atomically within one partition?", listOf("A user-defined function", "A stored procedure", "A post-trigger alone", "The change feed"), setOf(1), "Stored procedures run as a transaction scoped to one partition key."),
            B.q("s16", "az-cosmos-sdk", "An Azure Function must run whenever a document is inserted or updated. Which trigger?", listOf("Timer", "Cosmos DB trigger reading the change feed", "HTTP", "Queue"), setOf(1), "The Cosmos DB trigger consumes the change feed and needs a lease container."),
            B.q("s17", "az-cosmos-sdk", "Items must disappear 90 days after they are written with no code. What do you set?", listOf("A lifecycle rule", "DefaultTimeToLive on the container", "Eventual consistency", "A pre-trigger"), setOf(1), "TTL on the container (and optionally per item) expires items automatically."),
            B.q("s18", "az-cosmos-sdk", "Which events are not delivered by the standard change feed?", listOf("Inserts", "Updates", "Deletes", "All are delivered"), setOf(2), "Deletes are absent in the latest-version mode; use a soft-delete flag or the all-versions-and-deletes mode."),
            B.q("s19", "az-blob-basics", "Which storage account kind supports access tiers?", listOf("Classic", "StorageV1", "StorageV2 (general-purpose v2)", "Any"), setOf(2), "Tiering is a general-purpose v2 (and blob storage) feature."),
            B.q("s20", "az-cosmos-model", "Which statement about partition keys is true?", listOf("It can be changed after creation", "It is set per database", "It cannot be changed; migrate to a new container", "It must be the id"), setOf(2), "The partition key is fixed per container at creation."),
        )),
    )
}
