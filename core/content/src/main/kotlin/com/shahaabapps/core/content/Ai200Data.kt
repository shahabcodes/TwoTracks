package com.shahaabapps.core.content

/** AI-200 topics, written from the official study guide and Microsoft Learn (Ai200). */
internal object Ai200Data {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val cosmosSdk = Topic(
        id = "ai-cosmos-sdk", title = "Cosmos DB for NoSQL in Python: connect and query", minutes = 28,
        references = listOf(
            B.ref("Quickstart: Azure Cosmos DB for NoSQL with the Python SDK", LEARN + "azure/cosmos-db/quickstart-python"),
            B.ref("Get started with Azure Cosmos DB for NoSQL using Python", LEARN + "azure/cosmos-db/how-to-python-get-started"),
            B.ref("Azure Cosmos DB Python SDK for API for NoSQL", LEARN + "azure/cosmos-db/sdk-python"),
            B.ref("Partitioning and horizontal scaling in Azure Cosmos DB", LEARN + "azure/cosmos-db/partitioning"),
            B.ref("Connect with role-based access control and Microsoft Entra ID", LEARN + "azure/cosmos-db/how-to-connect-role-based-access-control"),
            B.ref("Pagination in Azure Cosmos DB queries", LEARN + "cosmos-db/query/pagination"),
            B.ref("Learn module: Use the Azure Cosmos DB for NoSQL SDK", LEARN + "training/modules/use-azure-cosmos-db-sql-api-sdk/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-cosmos-sdk-w1", "ai-cosmos-sdk",
                "You know a note's id and the user it belongs to. What is the cheapest way to fetch it from Cosmos DB?",
                listOf("A point read with the id and partition key", "A SQL query filtering on the id", "Read the whole container and filter in Python"), 0,
                "Think about what the database needs to know to go straight to one item.",
            ),
            B.plain("""
                Cosmos DB for NoSQL is a database that stores JSON documents, called items. An account holds
                databases, a database holds containers, and a container holds items. Every container has a
                partition key: one property, such as userId, that decides which slice of storage an item lives in.
                Your Python code talks to it through the azure-cosmos package, signing in with Entra ID instead of a key.
            """),
            B.diagram(
                caption = "The Coach API signs in with Entra ID, then works down from account to database to container. A point read uses the id plus the partition key.",
                nodes = listOf(
                    DNode("app", "Coach API", 0.18f, 0.2f, w = 0.3f, h = 0.22f, tone = 1, sub = "CosmosClient"),
                    DNode("acct", "Cosmos DB account", 0.62f, 0.2f, w = 0.4f, h = 0.22f, tone = 2, sub = "tt-cosmos"),
                    DNode("db", "Database: coach", 0.62f, 0.5f, w = 0.4f, h = 0.18f),
                    DNode("cont", "Container: notes", 0.62f, 0.8f, w = 0.4f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "partition key /userId"),
                    DNode("pr", "Point read", 0.18f, 0.65f, w = 0.3f, h = 0.3f, kind = NodeKind.NOTE, tone = 2, sub = "id + key, about 1 RU"),
                ),
                edges = listOf(DEdge("app", "acct", label = "Entra token"), DEdge("acct", "db"), DEdge("db", "cont"), DEdge("pr", "cont", dashed = true)),
                aspect = 0.9f,
            ),
            B.h("The resource model"),
            B.table(
                listOf("Level", "Two Tracks Coach", "Python client"),
                listOf("Account", "tt-cosmos, the endpoint URL", "CosmosClient"),
                listOf("Database", "coach", "DatabaseProxy from get_database_client"),
                listOf("Container", "notes, partition key /userId", "ContainerProxy from get_container_client"),
                listOf("Item", "One study note as JSON with an id", "A Python dict"),
            ),
            B.bullets(
                "An item's identity is the pair id plus partition key value; the same id can exist in two partitions.",
                "Pick a partition key with many distinct values that most queries filter on. For the Coach, userId: each learner reads their own notes.",
                "Every operation costs request units (RUs), the currency of Cosmos DB; the next topic covers them.",
            ),
            B.h("Connect with DefaultAzureCredential"),
            B.p("""
                DefaultAzureCredential uses your az login on a laptop and the app's managed identity in Azure, so no
                account key sits in code. The identity needs a Cosmos DB data-plane role, such as Cosmos DB Built-in
                Data Contributor. Data-plane roles read and write items; they do not create databases or containers,
                so create those with az CLI or a template.
            """),
            B.code("python", """
                from azure.identity import DefaultAzureCredential
                from azure.cosmos import CosmosClient, exceptions

                client = CosmosClient("https://tt-cosmos.documents.azure.com:443/", credential=DefaultAzureCredential())
                container = client.get_database_client("coach").get_container_client("notes")

                note = {"id": "n-101", "userId": "u-42", "topic": "cosmos", "text": "Session is the default level."}
                container.upsert_item(note)          # insert, or replace if id + key exist

                try:
                    item = container.read_item(item="n-101", partition_key="u-42")   # point read
                except exceptions.CosmosResourceNotFoundError:
                    item = None
            """),
            B.check(
                "ai-cosmos-sdk-k1", "ai-cosmos-sdk",
                "The Coach API runs with a managed identity that has Cosmos DB Built-in Data Contributor. Its start-up code calls create_database_if_not_exists and fails with 403. Why?",
                listOf("Data-plane roles cannot create databases or containers", "The identity needs an account key as well", "Managed identities cannot read Cosmos DB"), 0,
                "Data-plane roles cover items and queries. Create databases and containers with az CLI, Bicep or a control-plane role.",
                whyWrong = mapOf(
                    1 to "Entra sign-in replaces the key entirely; adding a key puts back the secret you removed.",
                    2 to "They can; the same identity reads and writes items fine. Only management operations are refused.",
                ),
            ),
            B.h("Point reads versus queries"),
            B.compare("How do you fetch it?",
                B.card("Point read", "You know the id and the partition key value.", "read_item(item, partition_key)", "About 1 RU for a 1 KB item", "No query engine involved"),
                B.card("Query", "You filter, sort or do not know the id.", "query_items with SQL", "Costs more than a point read", "Cheapest when it stays in one partition"),
            ),
            B.code("python", """
                # Parameterized: values travel separately, never pasted into the SQL text
                items = container.query_items(
                    query="SELECT c.id, c.text FROM c WHERE c.userId = @user AND c.topic = @topic",
                    parameters=[{"name": "@user", "value": "u-42"}, {"name": "@topic", "value": "cosmos"}],
                    partition_key="u-42",          # single-partition query
                )
                for it in items:
                    print(it["text"])

                # Across all learners: a cross-partition query fans out to every partition
                pages = container.query_items(
                    query="SELECT c.id FROM c WHERE c.topic = @topic",
                    parameters=[{"name": "@topic", "value": "cosmos"}],
                    enable_cross_partition_query=True, max_item_count=50,
                ).by_page()
                first_page = list(next(pages))
                token = pages.continuation_token     # hand back to the client, resume later with by_page(token)

                # Change two fields without sending the whole item
                container.patch_item(item="n-101", partition_key="u-42", patch_operations=[
                    {"op": "replace", "path": "/text", "value": "Session is the default consistency."},
                    {"op": "incr", "path": "/views", "value": 1},
                ])
            """),
            B.tip("\"Cheapest way to get one item whose id and key you know\" points to a point read. \"Query filters on something other than the partition key\" means cross-partition, which costs more."),
            B.check(
                "ai-cosmos-sdk-k2", "ai-cosmos-sdk",
                "A search box passes the learner's text into a Cosmos DB query. How should the code include it?",
                listOf("As a parameter such as @term in the parameters list", "With an f-string inside the SQL text", "Base64-encoded inside the SQL text"), 0,
                "Parameters keep values apart from the SQL, which blocks injection and lets the query text be reused.",
                whyWrong = mapOf(
                    1 to "Pasting user input into SQL text invites injection and breaks on quotes.",
                    2 to "Encoding hides the text but the query still treats it as SQL, and nothing can filter on it.",
                ),
            ),
            B.check(
                "ai-cosmos-sdk-k3", "ai-cosmos-sdk",
                "A page of results must be fetched now and the next page later, in a new request. What does the API hand back to the client?",
                listOf("The continuation token", "The last item's id", "The request charge"), 0,
                "by_page gives a continuation_token; passing it to by_page later resumes exactly where the last page ended.",
                whyWrong = mapOf(
                    1 to "An id alone does not tell the query engine where it was across partitions.",
                    2 to "The charge tells you the cost, not the position in the results.",
                ),
            ),
            B.worked("""
                Worked example. The Coach shows a learner one saved note when they tap it. The app already has
                the note id n-101 and the signed-in learner u-42, which is the partition key value. So it skips SQL
                entirely and calls read_item(item="n-101", partition_key="u-42"), a point read that costs about one RU
                for a small note. If the note was deleted, CosmosResourceNotFoundError is caught and the app shows
                "note not found".
            """),
            B.faded(
                "ai-cosmos-sdk-f1",
                "A learner opens their flashcard f-7. The app knows the card id and the learner id u-9, and the container is partitioned on /userId.",
                listOf(
                    "Both halves of the item's identity are known: the id and the partition key value.",
                    "So skip the query engine and do a point ___ with read_item, passing f-7 and u-9.",
                    "Catch CosmosResourceNotFoundError in case the card was deleted.",
                ),
                blank = 1,
                answers = listOf("read"),
                note = "Id plus partition key means a point read, the cheapest operation Cosmos DB offers.",
            ),
            B.steps("Set it up with az CLI",
                "az cosmosdb create --name tt-cosmos --resource-group tt-rg --capabilities EnableServerless" to "A serverless NoSQL account; you pay per RU used.",
                "az cosmosdb sql database create --account-name tt-cosmos --resource-group tt-rg --name coach" to "The database exists.",
                "az cosmosdb sql container create --account-name tt-cosmos --resource-group tt-rg --database-name coach --name notes --partition-key-path /userId" to "A container partitioned on userId.",
                "az cosmosdb sql role assignment create --account-name tt-cosmos --resource-group tt-rg --role-definition-id 00000000-0000-0000-0000-000000000002 --principal-id <your-object-id> --scope /" to "You get Built-in Data Contributor on the data plane.",
                "pip install azure-cosmos azure-identity, then run the script" to "upsert_item and read_item succeed with no key anywhere.",
            ),
            B.mistakes(
                "Using a query with WHERE c.id = @id when a point read with the partition key would cost a fraction.",
                "Building SQL with f-strings from user input instead of the parameters list.",
                "Granting a data-plane role and expecting create_container to work; it returns 403.",
                "Choosing a partition key with few values, such as topic, so a few partitions take all the load.",
                "Leaving the partition key off a query that could have been scoped to one learner.",
            ),
            B.example("Two Tracks Coach: notes live in coach/notes keyed by userId. Opening a note is a point read, the note list is a single-partition query with a continuation token for paging, and the nightly topic report is the one cross-partition query."),
            B.h("Check yourself"),
            B.match(
                "ai-x-cosmossdk-match", "Pair each call with what it does.",
                "read_item" to "Fetches one item by id and partition key",
                "query_items" to "Runs SQL with optional parameters",
                "upsert_item" to "Inserts the item or replaces it if it exists",
                "patch_item" to "Changes selected fields without sending the whole item",
            ),
            B.gap(
                "ai-x-cosmossdk-gap1", "The property that decides where an item is stored.",
                "Every container has a ___ key, such as /userId.", "partition", "Partition",
            ),
            B.gap(
                "ai-x-cosmossdk-gap2", "What the API returns so the next page can be fetched later.",
                "Resume a paged query by passing its ___ token to by_page.", "continuation", "Continuation",
            ),
            B.tryIt("Try it in 15 minutes", "A serverless account costs pennies for a few hundred operations.",
                "Create a serverless account, the coach database and a notes container on /userId",
                "Give yourself Built-in Data Contributor and sign in with az login",
                "Upsert three notes for two users, then point-read one",
                "Run a parameterized single-partition query and a cross-partition one, and compare the results",
            ),
            B.explain(
                "ai-cosmos-sdk-x1",
                "Explain how a Python app reads from Cosmos DB without a key, and when it uses a point read rather than a query.",
                "The app creates a CosmosClient with DefaultAzureCredential, so locally it uses az login and in Azure the managed identity, and that identity holds a Cosmos DB data plane role. When the app knows the id and the partition key value it does a point read with read_item, the cheapest call. Otherwise it runs a parameterized SQL query, ideally scoped to one partition; a query on another property fans out to every partition and pages back with a continuation token.",
                "Entra sign-in instead of a key" to listOf("defaultazurecredential", "managed identity", "entra", "az login"),
                "Point read needs id and partition key" to listOf("point read", "partition key", "readitem"),
                "Queries are parameterized" to listOf("parameter", "parameterized", "injection"),
                "Cross-partition queries fan out and page" to listOf("every partition", "all partitions", "fans out", "fan out", "continuation"),
            ),
            B.glossary(
                "Item" to "One JSON document in a container, identified by id plus partition key.",
                "Container" to "The unit that holds items, with its own partition key and indexing policy.",
                "Partition key" to "The property whose value decides which partition stores an item.",
                "Point read" to "Fetching one item by id and partition key; the cheapest operation.",
                "Cross-partition query" to "A query not scoped to one key value, which fans out to every partition.",
                "Continuation token" to "An opaque marker that lets a paged query resume where it stopped.",
                "Data-plane role" to "An RBAC role for reading and writing items, not for creating resources.",
            ),
            B.recap(
                "Account, database, container, item; an item is id plus partition key value.",
                "CosmosClient with DefaultAzureCredential and a data-plane role; create containers with az CLI.",
                "Point read when you know id and key; otherwise a parameterized query, single-partition when possible.",
            ),
        ),
    )

    private val cosmosPerf = Topic(
        id = "ai-cosmos-perf", title = "Cosmos DB performance: RUs, indexing policies and consistency", minutes = 28,
        references = listOf(
            B.ref("Request Units in Azure Cosmos DB", LEARN + "azure/cosmos-db/request-units"),
            B.ref("Indexing policies in Azure Cosmos DB", LEARN + "azure/cosmos-db/index-policy"),
            B.ref("Consistency levels in Azure Cosmos DB", LEARN + "azure/cosmos-db/consistency-levels"),
            B.ref("Create containers and databases with autoscale throughput", LEARN + "azure/cosmos-db/provision-throughput-autoscale"),
            B.ref("Diagnose and troubleshoot request rate too large (429) exceptions", LEARN + "azure/cosmos-db/troubleshoot-request-rate-too-large"),
            B.ref("Optimize request cost in Azure Cosmos DB", LEARN + "azure/cosmos-db/optimize-cost-reads-writes"),
            B.ref("Learn module: Customize an indexing policy in Azure Cosmos DB for NoSQL", LEARN + "training/modules/customize-indexes-azure-cosmos-db-sql-api/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-cosmos-perf-w1", "ai-cosmos-perf",
                "A container is given 1,000 RU/s and the app suddenly asks for 1,500 RU in one second. What happens?",
                listOf("Some requests get 429 and should be retried", "Azure bills the extra 500 RU", "The requests queue forever"), 0,
                "Throughput is a budget per second. Think about what a budget does when it runs out.",
            ),
            B.plain("""
                A request unit (RU) is the price tag Cosmos DB puts on every operation: reading a 1 KB item by id costs
                about 1 RU, and writes and queries cost more. You buy throughput as RUs per second. Two settings change
                the price: the indexing policy (what gets indexed, which makes writes dearer and queries cheaper) and the
                consistency level (how fresh a read must be, which affects read cost and latency).
            """),
            B.diagram(
                caption = "Each request spends RUs from the container's per-second budget. Within budget it succeeds and reports its charge; over budget it gets 429.",
                nodes = listOf(
                    DNode("app", "Coach API", 0.18f, 0.5f, w = 0.3f, h = 0.22f, tone = 1, sub = "query costs 12 RU"),
                    DNode("cont", "notes container", 0.55f, 0.5f, w = 0.3f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "1,000 RU/s"),
                    DNode("ok", "200 OK", 0.85f, 0.2f, w = 0.28f, h = 0.2f, kind = NodeKind.PILL, sub = "x-ms-request-charge"),
                    DNode("busy", "429 throttled", 0.85f, 0.8f, w = 0.28f, h = 0.2f, kind = NodeKind.PILL, tone = 2, sub = "retry after ms"),
                ),
                edges = listOf(DEdge("app", "cont", label = "request"), DEdge("cont", "ok", label = "in budget"), DEdge("cont", "busy", label = "over budget")),
                aspect = 0.8f,
            ),
            B.h("Request units and the charge header"),
            B.bullets(
                "Every response carries x-ms-request-charge: the RUs it used. Measure it rather than guess.",
                "Cost grows with item size, number of indexed properties, query complexity and stronger consistency.",
                "Point reads are cheapest; single-partition queries next; cross-partition queries cost the most.",
            ),
            B.code("python", """
                container.read_item(item="n-101", partition_key="u-42")
                charge = container.client_connection.last_response_headers["x-ms-request-charge"]
                print("point read cost", charge, "RU")
            """),
            B.h("Throughput: manual, autoscale or serverless"),
            B.compare("How do you buy RUs?",
                B.card("Manual", "Load is steady and predictable.", "A fixed RU/s, from 400", "Billed for what you set", "You change it yourself"),
                B.card("Autoscale", "Load has peaks and quiet hours.", "You set a maximum RU/s", "Scales between 10% of max and max", "Billed for the highest RU/s reached each hour"),
                B.card("Serverless", "Dev, test or small spiky apps.", "No RU/s to set", "Pay per RU consumed", "Not for sustained heavy load"),
            ),
            B.h("429 and retries"),
            B.p("""
                When requests in a second need more RUs than the budget, Cosmos DB answers 429 Too Many Requests with a
                header saying how many milliseconds to wait. The SDK retries these for you a few times before raising
                an error. A few 429s are normal under load; many of them mean you need more RU/s, autoscale, a cheaper
                query or a better partition key, because one hot partition can be throttled while others sit idle.
            """),
            B.check(
                "ai-cosmos-perf-k1", "ai-cosmos-perf",
                "Monitoring shows occasional 429s at 9 a.m. when learners log in, and the rest of the day is quiet. What fits best?",
                listOf("Autoscale throughput with a suitable maximum", "Strong consistency", "Turn indexing off"), 0,
                "Autoscale rises to its maximum for the peak and drops to 10% of it when quiet, so you are not paying peak rates all day.",
                whyWrong = mapOf(
                    1 to "Stronger consistency makes reads cost more, which adds to the throttling.",
                    2 to "With indexing off, every query has to scan, which is far worse than a few 429s.",
                ),
            ),
            B.h("Indexing policies"),
            B.p("""
                By default every property of every item is indexed, which makes any filter fast but makes each write
                pay to update the index. The indexing policy lets you choose. indexingMode is consistent (the index is
                updated with each write) or none (no index, for a pure key-value container). includedPaths and
                excludedPaths choose which properties are indexed, and compositeIndexes serve ORDER BY on several properties.
            """),
            B.code("python", """
                policy = {
                    "indexingMode": "consistent",
                    "includedPaths": [{"path": "/userId/?"}, {"path": "/topic/?"}, {"path": "/createdAt/?"}],
                    "excludedPaths": [{"path": "/*"}],            # skip the long note text and everything else
                    "compositeIndexes": [[
                        {"path": "/topic", "order": "ascending"},
                        {"path": "/createdAt", "order": "descending"},
                    ]],
                }
                database.replace_container(container, partition_key=PartitionKey(path="/userId"), indexing_policy=policy)

                # Now served by the composite index
                container.query_items(
                    query="SELECT * FROM c WHERE c.userId = @u ORDER BY c.topic ASC, c.createdAt DESC",
                    parameters=[{"name": "@u", "value": "u-42"}], partition_key="u-42")
            """),
            B.tip("\"ORDER BY on two properties fails\" means add a composite index with matching paths and orders. \"Writes cost too much and most properties are never filtered\" means exclude paths. Path /? is the value itself, /* is everything under it."),
            B.check(
                "ai-cosmos-perf-k2", "ai-cosmos-perf",
                "A query with ORDER BY c.topic ASC, c.createdAt DESC returns an error about a missing index. What do you add?",
                listOf("A composite index on topic ascending and createdAt descending", "Two separate range indexes", "A higher consistency level"), 0,
                "An ORDER BY across several properties needs a composite index whose paths and orders match the query.",
                whyWrong = mapOf(
                    1 to "Single-property indexes are already there by default; multi-property ORDER BY needs a composite one.",
                    2 to "Consistency affects how fresh reads are, not which indexes exist.",
                ),
            ),
            B.h("The five consistency levels"),
            B.table(
                listOf("Level", "What a reader sees", "Read cost"),
                listOf("Strong", "Always the latest committed write", "High; about twice a Session read (reads two copies)"),
                listOf("Bounded staleness", "At most K versions or T time behind", "High; same as Strong (reads two copies)"),
                listOf("Session (default)", "Your own writes, in order, within your session", "Low (reads one copy)"),
                listOf("Consistent prefix", "Never out of order, but may lag", "Low; same as Session"),
                listOf("Eventual", "Possibly out of order; catches up eventually", "Low; same as Session"),
            ),
            B.p("""
                Set the default on the account. A client can ask for a weaker level than the default, never a stronger
                one. Session suits most apps: a learner who saves a note sees it at once, while other learners may see it a
                moment later. The SDK carries the session token for you.
            """),
            B.code("python", """
                # This reporting job tolerates lag and does not need to read its own writes, so Eventual is enough
                # (it costs the same RUs per read as Session; only Strong and Bounded staleness cost more)
                report_client = CosmosClient(url, credential=DefaultAzureCredential(), consistency_level="Eventual")
            """),
            B.check(
                "ai-cosmos-perf-k3", "ai-cosmos-perf",
                "A learner must always see the note they just saved, and nothing stricter is needed. Which consistency level?",
                listOf("Session", "Strong", "Eventual"), 0,
                "Session guarantees read-your-own-writes for that client at low cost, which is why it is the default.",
                whyWrong = mapOf(
                    1 to "Strong works but costs about twice as much per read and adds latency across regions for no gain here.",
                    2 to "Eventual might show the old list straight after the save.",
                ),
            ),
            B.worked("""
                Worked example. The notes list sorts by topic, then newest first, and fails with a missing-index error.
                The query has two ORDER BY properties, so the team adds a composite index on /topic ascending and
                /createdAt descending, matching the query exactly. They also exclude /text, the long note body nobody
                filters on, and the write charge for a note drops from about 14 RU to 9 RU, measured with
                x-ms-request-charge.
            """),
            B.faded(
                "ai-cosmos-perf-f1",
                "The flashcard list sorts by deck ascending, then dueAt ascending, and the query fails with a missing-index error.",
                listOf(
                    "The query sorts on two properties at once.",
                    "Single-property indexes cannot serve that sort.",
                    "Add a ___ index on /deck ascending and /dueAt ascending to the indexing policy.",
                ),
                blank = 2,
                answers = listOf("composite"),
                note = "ORDER BY on more than one property needs a composite index whose paths and orders match the query.",
            ),
            B.steps("Tune it with az CLI",
                "az cosmosdb update --name tt-cosmos --resource-group tt-rg --default-consistency-level Session" to "The account default is Session.",
                "az cosmosdb sql container update --account-name tt-cosmos --resource-group tt-rg --database-name coach --name notes --idx @policy.json" to "The new indexing policy applies; the index rebuilds in the background.",
                "az cosmosdb sql container throughput migrate --account-name tt-cosmos --resource-group tt-rg --database-name coach --name notes --throughput-type autoscale" to "A provisioned container moves to autoscale.",
                "az cosmosdb sql container throughput update --account-name tt-cosmos --resource-group tt-rg --database-name coach --name notes --max-throughput 4000" to "It now scales between 400 and 4,000 RU/s.",
                "Print x-ms-request-charge before and after in Python" to "The charge for the same write or query is lower.",
            ),
            B.mistakes(
                "Guessing RU costs instead of reading x-ms-request-charge on real requests.",
                "Keeping the default index on long text fields nobody filters on, so every write pays for it.",
                "Choosing Strong consistency by habit and paying double for every read.",
                "Treating every 429 as an outage when the SDK already retried and a hot partition is the real cause.",
                "Expecting a client to request a stronger level than the account default.",
            ),
            B.example("Two Tracks Coach: Session consistency on the account, autoscale to 4,000 RU/s for the morning rush, /text excluded from the index, a composite index for the sorted notes list, and an Eventual client for the nightly report."),
            B.h("Check yourself"),
            B.match(
                "ai-x-cosmosperf-match", "Pair each consistency level with what a reader gets.",
                "Strong" to "Always the latest committed write",
                "Bounded staleness" to "At most a set number of versions or time behind",
                "Session" to "Its own writes, in order",
                "Eventual" to "Possibly out of order until it catches up",
            ),
            B.gap(
                "ai-x-cosmosperf-gap1", "The status code when a second's requests need more RUs than you bought.",
                "Cosmos DB answers ___ Too Many Requests, and the SDK retries.", "429",
            ),
            B.gap(
                "ai-x-cosmosperf-gap2", "Throughput that moves between 10% of a maximum and the maximum.",
                "With ___ throughput you set only the maximum RU/s.", "autoscale", "Autoscale",
            ),
            B.tryIt("Try it in 15 minutes", "Use the notes container from the last topic.",
                "Print x-ms-request-charge for a point read, a single-partition query and a cross-partition query",
                "Exclude /text from the index and compare the charge of an upsert before and after",
                "Run a two-property ORDER BY, see it fail, then add the composite index and run it again",
                "Check the account's default consistency in the portal under Default consistency",
            ),
            B.explain(
                "ai-cosmos-perf-x1",
                "Explain how you would make a Cosmos DB container cheaper and faster to use, covering request units, indexing and consistency.",
                "Every operation costs request units, so first measure the request charge header on real calls. Then trim the indexing policy: exclude long properties nobody filters on so writes cost less, and add a composite index for any ORDER BY on two properties. Keep the session consistency default, which gives read-your-own-writes cheaply, instead of strong. If peaks cause 429 throttling, move to autoscale throughput rather than paying for the peak all day.",
                "Measure the request charge in RUs" to listOf("request unit", "request charge", "x-ms-request-charge", "rus"),
                "Tune the indexing policy" to listOf("indexing policy", "exclude", "excluded", "composite"),
                "Pick session consistency over strong" to listOf("session", "consistency"),
                "Handle peaks with autoscale" to listOf("autoscale", "429", "throttl"),
            ),
            B.glossary(
                "Request unit (RU)" to "The cost of an operation; about 1 for a 1 KB point read.",
                "x-ms-request-charge" to "The response header that reports the RUs a request used.",
                "Indexing policy" to "Which paths are indexed, the mode, and any composite or vector indexes.",
                "Composite index" to "An index over several properties, needed for multi-property ORDER BY.",
                "Session consistency" to "The default: a client reads its own writes in order.",
                "429" to "Too Many Requests: the RU budget for that second is spent.",
                "Autoscale" to "Throughput that scales between 10% of a set maximum and the maximum.",
            ),
            B.recap(
                "Everything costs RUs; read x-ms-request-charge; 429 means over budget and the SDK retries.",
                "Exclude unused paths to cut write cost; composite indexes for ORDER BY on several properties.",
                "Five consistency levels, Session by default; clients may only weaken it; autoscale for peaks.",
            ),
        ),
    )

    private val cosmosVector = Topic(
        id = "ai-cosmos-vector", title = "Vector search in Cosmos DB: embeddings and semantic retrieval", minutes = 30,
        references = listOf(
            B.ref("Integrated vector store in Azure Cosmos DB for NoSQL", LEARN + "azure/cosmos-db/vector-search"),
            B.ref("Index and query vector data in Python", LEARN + "azure/cosmos-db/how-to-python-vector-index-query"),
            B.ref("VectorDistance system function", LEARN + "cosmos-db/query/vectordistance"),
            B.ref("Generate embeddings with Azure OpenAI", LEARN + "azure/foundry/openai/how-to/embeddings"),
            B.ref("Retrieval augmented generation in Azure Cosmos DB", LEARN + "azure/cosmos-db/gen-ai/rag"),
            B.ref("What is a vector database?", LEARN + "azure/cosmos-db/vector-database"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-cosmos-vector-w1", "ai-cosmos-vector",
                "A learner searches their notes for \"how do I stop throttling\". The best note says \"handle 429 with autoscale\". How can a database find it?",
                listOf("Compare the meaning of the texts as vectors", "A LIKE query on the word throttling", "An exact match on the title"), 0,
                "The words barely overlap, yet the meaning is close. Think about how meaning could become numbers.",
            ),
            B.plain("""
                An embedding is a list of numbers, often 1,536 of them, that an AI model produces from a piece of text.
                Texts with similar meaning get lists that point in similar directions. Store each note's embedding next
                to the note in Cosmos DB, turn the learner's question into an embedding too, and ask for the notes whose
                vectors are closest. That is vector search, and it finds notes by meaning, not by matching words.
            """),
            B.diagram(
                caption = "Retrieval for the Coach: embed the question, find the closest notes with VectorDistance, then give them to a chat model to answer from.",
                nodes = listOf(
                    DNode("user", "Learner", 0.1f, 0.2f, w = 0.16f, h = 0.2f, kind = NodeKind.USER),
                    DNode("api", "Coach API", 0.4f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "embed the question"),
                    DNode("emb", "Embedding model", 0.8f, 0.2f, w = 0.34f, h = 0.2f, tone = 2, sub = "Azure OpenAI"),
                    DNode("db", "Cosmos DB notes", 0.4f, 0.7f, w = 0.34f, h = 0.26f, kind = NodeKind.CYLINDER, sub = "VectorDistance TOP 5"),
                    DNode("chat", "Chat model", 0.8f, 0.7f, w = 0.34f, h = 0.2f, tone = 2, sub = "answers from notes"),
                ),
                edges = listOf(
                    DEdge("user", "api", label = "question"),
                    DEdge("api", "emb", label = "text", bidirectional = true),
                    DEdge("api", "db", label = "query vector"),
                    DEdge("db", "chat", label = "top notes"),
                ),
                aspect = 0.75f,
            ),
            B.h("Turn on vector search and describe the vectors"),
            B.bullets(
                "Enable the account feature once: az cosmosdb update --capabilities EnableNoSQLVectorSearch (it can take about 15 minutes).",
                "Vector policies go on a new container when you create it; you cannot switch them on for an existing container.",
                "The container vector embedding policy says, per path: the data type (float32 usually), the dimensions (must match the model, 1,536 for text-embedding-3-small) and the distance function.",
                "Exclude the vector path from the normal indexing policy, or every insert pays to index 1,536 numbers one by one.",
                "Vector search is not supported on databases with shared throughput.",
            ),
            B.table(
                listOf("Distance function", "Range", "Most similar is"),
                listOf("cosine (default)", "-1 to +1", "Higher, toward +1"),
                listOf("dotproduct", "-infinity to +infinity", "Higher"),
                listOf("euclidean", "0 to +infinity", "Lower, toward 0"),
            ),
            B.h("Vector indexes"),
            B.compare("Which vector index?",
                B.card("flat", "Small vectors and you need exact results.", "Brute force, 100% recall", "At most 505 dimensions"),
                B.card("quantizedFlat", "Up to about 50,000 vectors in scope, often after a filter.", "Compressed vectors, brute force", "Up to 4,096 dimensions; slight accuracy loss"),
                B.card("diskANN", "More than about 50,000 vectors in scope.", "Approximate nearest neighbour", "Up to 4,096 dimensions; lowest latency and RU at scale"),
            ),
            B.tip("\"1,536-dimension embeddings with a flat index\" is a trap: flat stops at 505 dimensions. quantizedFlat and diskANN need about 1,000 vectors before they help; below that a full scan runs."),
            B.check(
                "ai-cosmos-vector-k1", "ai-cosmos-vector",
                "Notes are embedded with a 1,536-dimension model and the Coach will hold two million of them. Which vector index?",
                listOf("diskANN", "flat", "No vector index; exclude the path"), 0,
                "diskANN handles up to 4,096 dimensions and is the most efficient when far more than 50,000 vectors are in scope.",
                whyWrong = mapOf(
                    1 to "flat supports at most 505 dimensions, so 1,536 does not fit.",
                    2 to "Excluding the path from the normal index is right, but without a vector index every search scans everything.",
                ),
            ),
            B.code("python", """
                from azure.cosmos import PartitionKey

                vector_embedding_policy = {"vectorEmbeddings": [
                    {"path": "/embedding", "dataType": "float32", "distanceFunction": "cosine", "dimensions": 1536},
                ]}
                indexing_policy = {
                    "includedPaths": [{"path": "/*"}],
                    "excludedPaths": [{"path": "/\"_etag\"/?"}, {"path": "/embedding/*"}],
                    "vectorIndexes": [{"path": "/embedding", "type": "diskANN"}],
                }
                notes = database.create_container_if_not_exists(
                    id="notes-v", partition_key=PartitionKey(path="/userId"),
                    indexing_policy=indexing_policy, vector_embedding_policy=vector_embedding_policy)
            """),
            B.h("Make embeddings, then search"),
            B.p("""
                An Azure OpenAI embedding model, such as text-embedding-3-small deployed in your resource, turns text into
                the vector. Sign in with Entra ID as everywhere else. Use the same model for the stored notes and for
                the question, or the numbers are not comparable.
            """),
            B.code("python", """
                from openai import AzureOpenAI
                from azure.identity import DefaultAzureCredential, get_bearer_token_provider

                token = get_bearer_token_provider(DefaultAzureCredential(), "https://cognitiveservices.azure.com/.default")
                aoai = AzureOpenAI(azure_endpoint="https://tt-openai.openai.azure.com/",
                                   azure_ad_token_provider=token, api_version="2024-10-21")
                # 2024-10-21 is a GA version; newer samples use OpenAI(base_url=".../openai/v1/") with no version

                def embed(text):
                    return aoai.embeddings.create(model="text-embedding-3-small", input=text).data[0].embedding

                notes.upsert_item({"id": "n-7", "userId": "u-42", "text": "Handle 429 with autoscale.",
                                   "embedding": embed("Handle 429 with autoscale.")})

                q = embed("how do I stop throttling")
                results = notes.query_items(
                    query="SELECT TOP 5 c.id, c.text, VectorDistance(c.embedding, @q) AS score "
                          "FROM c WHERE c.userId = @u ORDER BY VectorDistance(c.embedding, @q)",
                    parameters=[{"name": "@q", "value": q}, {"name": "@u", "value": "u-42"}],
                    partition_key="u-42")
            """),
            B.p("""
                Always put TOP N on a vector query; without it the engine works out a score for far more items and the
                RU charge climbs. The WHERE filter on userId keeps the search inside one learner's notes, which is both a
                metadata filter and a single-partition query.
            """),
            B.check(
                "ai-cosmos-vector-k2", "ai-cosmos-vector",
                "A vector query returns good matches but costs far more RUs than expected. Its SQL is SELECT c.id FROM c ORDER BY VectorDistance(c.embedding, @q). What is missing?",
                listOf("TOP N in the SELECT", "A composite index on embedding", "Strong consistency"), 0,
                "Without TOP the engine tries to rank many more results than you need; TOP 5 or TOP 10 keeps cost and latency low.",
                whyWrong = mapOf(
                    1 to "Vectors use a vector index, not a composite index, and the path should be excluded from the normal index.",
                    2 to "Stronger consistency would only raise the cost of reads.",
                ),
            ),
            B.h("From retrieval to RAG"),
            B.p("""
                Retrieval augmented generation (RAG) means: retrieve the most relevant notes with vector search, put
                their text into the prompt, and ask a chat model to answer only from them. The answer is grounded in the
                learner's own notes, and the app can show which notes it used.
            """),
            B.check(
                "ai-cosmos-vector-k3", "ai-cosmos-vector",
                "Notes were embedded with text-embedding-3-small, but the question is embedded with a different model. Results are nonsense. Why?",
                listOf("Vectors from different models are not comparable", "The distance function must be euclidean", "diskANN needs strong consistency"), 0,
                "Each model places meaning in its own space, often with different dimensions. Stored vectors and query vectors must come from the same model.",
                whyWrong = mapOf(
                    1 to "Any of the three functions works when both vectors come from one model; changing it does not fix mixed models.",
                    2 to "Consistency has nothing to do with how vectors are compared.",
                ),
            ),
            B.worked("""
                Worked example. The Coach stores 300,000 notes, each with a 1,536-dimension embedding from
                text-embedding-3-small. flat is ruled out because it stops at 505 dimensions. Searches are filtered to
                one learner, so a few hundred vectors are in scope each time, well under 50,000, so the team picks
                quantizedFlat with cosine distance and excludes /embedding/* from the normal index.
            """),
            B.faded(
                "ai-cosmos-vector-f1",
                "A shared library of two million public study cards, each with a 1,536-dimension embedding, is searched across all cards with no filter.",
                listOf(
                    "flat is out: it allows at most 505 dimensions.",
                    "Far more than 50,000 vectors are in scope on every search.",
                    "So choose a ___ vector index, with /embedding/* excluded from the normal index.",
                ),
                blank = 2,
                answers = listOf("diskann"),
                note = "Many vectors in scope means diskANN; a filtered search over a small set suits quantizedFlat; flat only for small, exact cases.",
            ),
            B.steps("Build it for real",
                "az cosmosdb update --name tt-cosmos --resource-group tt-rg --capabilities EnableNoSQLVectorSearch" to "The account allows vector search, after up to about 15 minutes.",
                "az cognitiveservices account deployment create --name tt-openai --resource-group tt-rg --deployment-name text-embedding-3-small --model-name text-embedding-3-small --model-version 1 --model-format OpenAI --sku-name Standard --sku-capacity 10" to "An embedding model deployment exists.",
                "Create notes-v in Python with the vector embedding policy and indexing policy" to "The container shows a vector policy in Data Explorer settings.",
                "Upsert a handful of notes with embed(text) in the embedding property" to "Items hold a 1,536-number array.",
                "Run the TOP 5 VectorDistance query for a question" to "The closest notes come back with scores near 1 for cosine.",
            ),
            B.mistakes(
                "Trying to add a vector policy to an existing container instead of creating a new one.",
                "Leaving the vector path in the normal indexing policy, so every insert costs far more.",
                "Choosing flat for 1,536-dimension embeddings, beyond its 505-dimension limit.",
                "Running a vector query without TOP N and paying for many more scored results.",
                "Embedding stored notes and questions with different models or dimensions.",
            ),
            B.example("Two Tracks Coach: each note carries a cosine embedding from text-embedding-3-small; questions are embedded the same way, filtered to the learner, and the TOP 5 notes are passed to the chat model, which answers and cites them."),
            B.h("Check yourself"),
            B.match(
                "ai-x-cosmosvec-match", "Pair each vector index with when it fits.",
                "flat" to "Exact results for vectors up to 505 dimensions",
                "quantizedFlat" to "Up to about 50,000 vectors in scope, often after a filter",
                "diskANN" to "Large searches with more than about 50,000 vectors in scope",
            ),
            B.gap(
                "ai-x-cosmosvec-gap1", "The default distance function, where values nearer +1 mean more similar.",
                "The vector policy uses ___ distance unless you choose otherwise.", "cosine", "Cosine",
            ),
            B.gap(
                "ai-x-cosmosvec-gap2", "The query function that scores how close two vectors are.",
                "Rank results with ORDER BY ___(c.embedding, @q).", "VectorDistance", "vectordistance",
            ),
            B.tryIt("Try it in 15 minutes", "Needs an Azure OpenAI resource with an embedding deployment; a few cents of tokens.",
                "Enable vector search on the account",
                "Create a container with a vector embedding policy and a quantizedFlat index",
                "Embed and upsert five notes on different topics",
                "Ask a question in different words from any note and see which note ranks first",
            ),
            B.explain(
                "ai-cosmos-vector-x1",
                "Explain how the Coach finds notes by meaning in Cosmos DB, from the container setup to the query.",
                "Each note gets an embedding, a list of numbers from an Azure OpenAI embedding model, stored on the item. The container is created with a vector embedding policy giving the path, dimensions and cosine distance, a diskANN or quantizedFlat vector index, and the vector path excluded from normal indexing. At query time the question is embedded with the same model and a SELECT TOP 5 query orders by VectorDistance, filtered to the learner. The top notes then go to a chat model for RAG.",
                "Embeddings from a model" to listOf("embedding", "azure openai", "vector"),
                "Vector policy and index set at creation" to listOf("vector embedding policy", "vector index", "diskann", "quantizedflat"),
                "Query with VectorDistance and TOP" to listOf("vectordistance", "top 5", "top n"),
                "Results feed RAG" to listOf("rag", "chat model", "retrieval augmented", "prompt"),
            ),
            B.glossary(
                "Embedding" to "A list of numbers a model makes from text; similar meaning gives nearby vectors.",
                "Vector embedding policy" to "The container setting giving each vector path's type, dimensions and distance.",
                "Distance function" to "How closeness is measured: cosine, dot product or euclidean.",
                "quantizedFlat" to "A vector index of compressed vectors searched by brute force.",
                "diskANN" to "An approximate nearest-neighbour vector index for large collections.",
                "VectorDistance" to "The query function that scores the similarity of two vectors.",
                "RAG" to "Retrieval augmented generation: retrieve relevant data, then answer from it.",
            ),
            B.recap(
                "Embeddings turn meaning into numbers; store them on the item and use the same model for questions.",
                "New container with a vector embedding policy and vector index; exclude the vector path from normal indexing.",
                "SELECT TOP N with ORDER BY VectorDistance, filtered by metadata; feed the results to a chat model for RAG.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(cosmosSdk, cosmosPerf, cosmosVector)
}
