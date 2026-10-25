package com.shahaabapps.core.content

/** AI-200 topics, written from the official study guide and Microsoft Learn (Ai200). */
internal object Ai200Data2 {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val changeFeed = Topic(
        id = "ai-cosmos-changefeed", title = "The Cosmos DB change feed: reacting to new and updated items", minutes = 26,
        references = listOf(
            B.ref("Change feed in Azure Cosmos DB", LEARN + "azure/cosmos-db/change-feed"),
            B.ref("Change feed modes", LEARN + "azure/cosmos-db/change-feed-modes"),
            B.ref("Change feed processor", LEARN + "azure/cosmos-db/change-feed-processor"),
            B.ref("Change feed pull model", LEARN + "azure/cosmos-db/change-feed-pull-model"),
            B.ref("Serverless event-based architectures with Azure Cosmos DB and Azure Functions", LEARN + "azure/cosmos-db/change-feed-functions"),
            B.ref("Azure Cosmos DB trigger for Azure Functions", LEARN + "azure/azure-functions/functions-bindings-cosmosdb-v2-trigger"),
            B.ref("Learn module: Consume an Azure Cosmos DB for NoSQL change feed using the SDK", LEARN + "training/modules/consume-azure-cosmos-db-sql-api-change-feed-use-sdk/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-cosmos-changefeed-w1", "ai-cosmos-changefeed",
                "A study note is edited, and its embedding must be rebuilt. How do you think the code finds out the note changed?",
                listOf("Cosmos DB keeps an ordered log of changes it can read", "It queries every note every minute", "The app must send itself an email"), 0,
                "Cosmos DB already records every write in order. Something just has to read that record.",
            ),
            B.plain("""
                The change feed is a running list of the items in a container that were created or changed, in the
                order they happened. Your code reads that list and reacts, for example by rebuilding an embedding,
                instead of searching the whole container for what is new. A lease is a small saved bookmark that
                remembers how far the reader got, so it carries on from there after a restart.
            """),
            B.diagram(
                caption = "One saved note, step by step: it lands in the container, appears in the feed, the Function re-embeds it and checkpoints the lease.",
                nodes = listOf(
                    DNode("api", "1. Coach API", 0.5f, 0.065f, w = 0.86f, h = 0.11f, tone = 1, sub = "a note is saved", hue = DHue.COMPUTE),
                    DNode("notes", "2. notes container", 0.5f, 0.21f, w = 0.86f, h = 0.11f, sub = "the monitored container", hue = DHue.DATA),
                    DNode("feed", "3. Change feed", 0.5f, 0.355f, w = 0.86f, h = 0.11f, sub = "creates and updates, in order", hue = DHue.FLOW),
                    DNode("fn", "4. Embed function", 0.5f, 0.5f, w = 0.86f, h = 0.11f, sub = "Cosmos DB trigger", hue = DHue.COMPUTE),
                    DNode("aoai", "5. Azure OpenAI", 0.5f, 0.645f, w = 0.86f, h = 0.11f, tone = 1, sub = "builds the new embedding", hue = DHue.COMPUTE),
                    DNode("leases", "6. leases container", 0.5f, 0.79f, w = 0.86f, h = 0.11f, tone = 1, sub = "the bookmark, one per range", hue = DHue.DATA),
                    DNode("note", "Deletes never appear here: set a flag, let TTL remove it", 0.5f, 0.935f, w = 0.86f, h = 0.11f, kind = NodeKind.NOTE, tone = 2, hue = DHue.NEUTRAL),
                ),
                edges = listOf(
                    DEdge("api", "notes", label = "upsert"),
                    DEdge("notes", "feed", label = "recorded in order"),
                    DEdge("feed", "fn", label = "a batch of changes"),
                    DEdge("fn", "aoai", label = "embed the body"),
                    DEdge("aoai", "leases", label = "then checkpoint", dashed = true),
                ),
                aspect = 1.3f,
            ),
            B.h("Two modes"),
            B.p("""
                Every container has a change feed; you do not switch it on. What you choose is the mode a reader
                uses. Each reading application picks one mode, and different applications can read the same
                container in different modes.
            """),
            B.table(
                listOf("", "Latest version mode", "All versions and deletes mode"),
                listOf("What appears", "Creates and updates, newest version of each item only", "Every create, update and delete, in order"),
                listOf("Deletes", "Not shown: a deleted item simply vanishes from the feed", "Shown, with metadata saying it was a delete (or a TTL expiry)"),
                listOf("Start from", "Beginning, a point in time, now, or a saved bookmark", "Now or a saved bookmark only"),
                listOf("Needs", "Nothing; on by default", "Continuous backup on the account; changes kept only for that window"),
                listOf("Status", "General availability", "Newer, with narrower SDK support; check its status in the docs"),
            ),
            B.tip("""
                "The processor must see deletes" in latest version mode points to a soft delete: set a deleted flag
                (an update the feed does show), then let a TTL (time to live) remove the item later. The other answer
                is all versions and deletes mode, which needs continuous backup.
            """),
            B.check(
                "ai-cosmos-changefeed-k1", "ai-cosmos-changefeed",
                "A note is created and then edited twice before the reader runs. In latest version mode, what does the reader get?",
                listOf("One entry: the note as it is now", "Three entries, one per write", "Nothing, because it changed more than once"), 0,
                "Latest version mode hands over the newest version of each changed item, so the in-between edits are not seen.",
                whyWrong = mapOf(
                    1 to "Every intermediate version is what all versions and deletes mode gives, not latest version mode.",
                    2 to "The item still changed, so it does appear; only its earlier versions are skipped.",
                ),
            ),
            B.h("The change feed processor"),
            B.p("""
                Reading the feed by hand means tracking where you got to and splitting the work between machines
                yourself. The change feed processor does both for you. It is a library in the .NET and Java SDKs, and
                it is also what runs underneath the Azure Functions Cosmos DB trigger.
            """),
            B.table(
                listOf("Part", "What it is", "In Two Tracks Coach"),
                listOf("Monitored container", "The container whose changes you read", "notes"),
                listOf("Lease container", "Stores one lease (bookmark) per range of partitions; partition key /id", "leases"),
                listOf("Host or instance name", "A unique name per running copy, so leases can be shared out", "the Function host, or pod-1 and pod-2"),
                listOf("Delegate", "Your code that handles each batch of changes", "rebuild the embedding"),
                listOf("Start time", "Where a brand-new processor begins: now (default), a date, or the beginning", "the beginning, once, to embed old notes"),
            ),
            B.bullets(
                "In the change feed processor library, the bookmark moves only after your code finishes a batch without error, so delivery is at least once: the same change can arrive twice, and your handler must cope. The Azure Functions trigger is different: by default it does not retry a batch your code failed on, so that batch is skipped unless you catch errors yourself or turn on a Functions retry policy. Duplicates can still reach a Function, for example from retries or when leases move between instances, so keep the handler safe to run twice.",
                "Several instances with the same processor name and lease container share the work: each lease belongs to one instance at a time.",
                "The start time only matters the first time. Once leases exist, the processor resumes from them and ignores the setting.",
                "Python has no change feed processor library. A Python app either uses the Functions trigger or the pull model, where it reads the feed itself and saves a continuation token as its bookmark.",
            ),
            B.compare("How will Python read the feed?",
                B.card("Azure Functions trigger", "You want changes handled with almost no code.", "Leases and scaling managed for you", "Bound with app settings and an identity", "Your function gets a list of changed items"),
                B.card("Pull model", "You need full control, one partition key, or a one-off backfill.", "query_items_change_feed in azure-cosmos", "You save the continuation token", "You decide when and how often to poll"),
            ),
            B.code("python", """
                # function_app.py: Python v2 programming model
                import logging
                import azure.functions as func

                app = func.FunctionApp()

                @app.cosmos_db_trigger(arg_name="notes",
                                       database_name="coach",
                                       container_name="notes",
                                       connection="COSMOS",          # COSMOS__accountEndpoint in app settings
                                       lease_container_name="leases")
                def embed_changed_notes(notes: func.DocumentList):
                    for note in notes:
                        if note.get("deleted"):
                            remove_from_search(note["id"])        # soft delete seen as an update
                        elif note.get("bodyHash") != note.get("embeddedHash"):
                            reembed(note)                         # only when the text really changed
                        logging.info("handled note %s", note["id"])
            """),
            B.check(
                "ai-cosmos-changefeed-k2", "ai-cosmos-changefeed",
                "Two copies of a change feed processor should split the work for the same container. What must differ between them?",
                listOf("The instance (host) name", "The lease container", "The processor name"), 0,
                "Same processor name and lease container make them one deployment unit; a unique instance name lets the leases be shared out.",
                whyWrong = mapOf(
                    1 to "With different lease containers they keep separate bookmarks and both read every change.",
                    2 to "A different processor name makes a separate deployment unit that also gets every change.",
                ),
            ),
            B.code("python", """
                # Pull model: read the feed yourself and keep the bookmark
                changes = container.query_items_change_feed(start_time="Beginning")
                for item in changes:
                    reembed(item)
                token = container.client_connection.last_response_headers["etag"]
                save_bookmark(token)

                # Later: carry on from where you stopped
                for item in container.query_items_change_feed(continuation=load_bookmark()):
                    reembed(item)
            """),
            B.worked("""
                Worked example. Two Tracks Coach stores notes in the notes container. When a note's body changes, its
                embedding is stale. Usaid creates a leases container with partition key /id and adds a Function with a
                Cosmos DB trigger on notes. The function re-embeds a note only when a hash of the body differs from
                the hash saved with the embedding, because saving the new vector is itself an update and would
                otherwise trigger the function again, forever.
            """),
            B.faded(
                "ai-cosmos-changefeed-f1",
                "A second job must read the same notes container to keep a word count per user, in its own Function.",
                listOf(
                    "Add a new function with a Cosmos DB trigger on the notes container.",
                    "Give it its own ___ container, or a different prefix, so its bookmarks do not clash with the embed function's.",
                    "Make the count safe to repeat, because a batch can be delivered more than once.",
                ),
                blank = 1,
                answers = listOf("lease", "leases"),
                note = "Two triggers sharing one lease container and prefix fight over the same bookmarks, and only one of them fires.",
            ),
            B.steps("Do it for real",
                "az cosmosdb sql container create --account-name tt-cosmos --resource-group tt-rg --database-name coach --name leases --partition-key-path /id" to "The lease container exists. Create it yourself when the Function uses an identity, which cannot create containers.",
                "func init coach-jobs --python, then add the trigger function above to function_app.py" to "A v2 Python function app with one Cosmos DB trigger.",
                "az functionapp config appsettings set --name tt-jobs --resource-group tt-rg --settings COSMOS__accountEndpoint=https://tt-cosmos.documents.azure.com:443/" to "An identity-based connection: no key in the settings.",
                "Grant the function app's managed identity a Cosmos DB data role (for example Cosmos DB Built-in Data Contributor) on the account" to "It can read the feed and write leases.",
                "Edit a note in Data Explorer and watch the function's log stream" to "Within seconds the function logs the note id.",
            ),
            B.mistakes(
                "Expecting deletes in latest version mode; use a soft-delete flag with a TTL instead.",
                "Writing the new embedding back to the same item with no check, so every save triggers the function again.",
                "Pointing two functions at the same lease container with the same prefix; only one of them gets the changes.",
                "Assuming each change arrives exactly once; handlers must be safe to run twice.",
                "Changing the start time on a processor that already has leases and wondering why nothing replays.",
            ),
            B.example("Two Tracks Coach: a Cosmos DB trigger re-embeds edited notes, deleted notes are flagged and expire by TTL, and a one-off pull-model script backfilled embeddings for old notes from the beginning."),
            B.check(
                "ai-cosmos-changefeed-k3", "ai-cosmos-changefeed",
                "Deleted notes must be removed from the vector search index, and the reader uses latest version mode. What do you do?",
                listOf("Set a deleted flag on the note and give it a TTL", "Read the lease container for deletes", "Start the processor from the beginning"), 0,
                "The flag is an update, so it appears in the feed; the TTL then removes the item for real.",
                whyWrong = mapOf(
                    1 to "The lease container holds bookmarks, not the items or their deletes.",
                    2 to "Starting earlier replays creates and updates; deletes still never appear in this mode.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-cfeed-match", "Pair each part with its job.",
                "Monitored container" to "Where the changes come from",
                "Lease container" to "Keeps the bookmarks and shares out the work",
                "Instance name" to "Tells running copies apart",
                "Continuation token" to "The pull model's bookmark, saved by you",
            ),
            B.gap(
                "ai-x-cfeed-gap1", "Which mode shows every write, including deletes?",
                "All versions and ___ mode, which needs continuous backup.", "deletes", "Deletes",
            ),
            B.gap(
                "ai-x-cfeed-gap2", "How a removed item can still be noticed in latest version mode.",
                "Use a soft delete: set a flag, then let a ___ remove the item.", "TTL", "ttl", "time to live",
            ),
            B.tryIt("Try it in 15 minutes", "A free-tier or serverless Cosmos DB account is enough.",
                "Create a notes container and a leases container with partition key /id",
                "Write a Python v2 function with a Cosmos DB trigger that logs each changed id",
                "Run it locally with func start and edit an item in Data Explorer",
                "Delete an item and note that nothing is logged, then try a deleted flag instead",
            ),
            B.explain(
                "ai-cosmos-changefeed-x1",
                "Explain how Two Tracks Coach can rebuild embeddings when notes change, and what happens to deleted notes.",
                "Cosmos DB's change feed lists created and updated items in order. A Function with a Cosmos DB trigger reads the feed for the notes container and re-embeds each changed note; it uses the change feed processor underneath, which keeps a bookmark per partition range in a lease container and delivers each change at least once. Latest version mode never shows deletes, so the app sets a soft-delete flag and a TTL, or reads in all versions and deletes mode with continuous backup.",
                "The change feed lists new and updated items" to listOf("change feed", "updated", "creates", "inserts"),
                "Leases or a trigger do the tracking" to listOf("lease", "trigger", "processor", "bookmark", "continuation"),
                "Deletes need a soft delete with TTL" to listOf("soft delete", "soft-delete", "ttl", "time to live", "all versions"),
            ),
            B.glossary(
                "Change feed" to "An ordered list of created and updated items in a container.",
                "Latest version mode" to "The default mode: newest version of each changed item, no deletes.",
                "All versions and deletes mode" to "Every change including deletes; needs continuous backup.",
                "Lease container" to "Holds the processor's bookmarks and shares work between instances.",
                "Change feed processor" to "The .NET and Java library that reads the feed with leases; under the Functions trigger.",
                "Continuation token" to "The pull model's bookmark, saved by your code.",
                "Soft delete" to "Marking an item deleted with a flag, then removing it later with TTL.",
            ),
            B.recap(
                "The change feed lists creates and updates; latest version mode has no deletes, so use a soft delete with TTL.",
                "The processor needs a monitored container, a lease container and a unique instance name; delivery is at least once.",
                "In Python use the Functions Cosmos DB trigger, or the pull model with a saved continuation token.",
            ),
        ),
    )

    private val postgres = Topic(
        id = "ai-postgres", title = "Azure Database for PostgreSQL: connect, model and index", minutes = 28,
        references = listOf(
            B.ref("What is Azure Database for PostgreSQL?", LEARN + "azure/postgresql/overview"),
            B.ref("Quickstart: Use Python to connect and query data", LEARN + "azure/postgresql/connectivity/connect-python"),
            B.ref("Microsoft Entra authentication with Azure Database for PostgreSQL", LEARN + "azure/postgresql/security/security-entra-concepts"),
            B.ref("PgBouncer in Azure Database for PostgreSQL", LEARN + "azure/postgresql/connectivity/concepts-pgbouncer"),
            B.ref("Compute options in Azure Database for PostgreSQL", LEARN + "azure/postgresql/compute-storage/concepts-compute"),
            B.ref("Learn path: Azure Database for PostgreSQL", LEARN + "training/paths/microsoft-learn-azure-database-for-postgresql/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-postgres-w1", "ai-postgres",
                "Two hundred container replicas each open their own database connections. What do you think helps the server most?",
                listOf("A connection pooler that shares a few real connections", "A bigger disk", "More indexes"), 0,
                "Each PostgreSQL connection is a whole process on the server. Having fewer of them matters.",
            ),
            B.plain("""
                Azure Database for PostgreSQL flexible server is ordinary PostgreSQL that Azure runs for you:
                patches, backups and high availability are handled, and you pick the size. Your Python code talks to
                it with psycopg, the standard PostgreSQL driver, and you design tables, data types and indexes the
                way you would on any PostgreSQL server.
            """),
            B.diagram(
                caption = "One connection, step by step: the API asks Entra for a token, sends it as the password to PgBouncer on 6432, and PgBouncer reuses a server connection.",
                nodes = listOf(
                    DNode("api", "1. Coach API", 0.5f, 0.1f, w = 0.86f, h = 0.15f, tone = 1, sub = "psycopg, with a small client pool", hue = DHue.COMPUTE),
                    DNode("entra", "2. Entra token", 0.5f, 0.3f, w = 0.86f, h = 0.15f, sub = "asked for by the managed identity", hue = DHue.FLOW),
                    DNode("pgb", "3. PgBouncer", 0.5f, 0.5f, w = 0.86f, h = 0.15f, sub = "same host, port 6432", hue = DHue.COMPUTE),
                    DNode("pg", "4. PostgreSQL", 0.5f, 0.7f, w = 0.86f, h = 0.15f, sub = "port 5432: a process per connection", hue = DHue.DATA),
                    DNode("note", "Many app connections share a few warm server ones", 0.5f, 0.9f, w = 0.86f, h = 0.15f, kind = NodeKind.NOTE, tone = 2, hue = DHue.NEUTRAL),
                ),
                edges = listOf(
                    DEdge("api", "entra", label = "1 get a token"),
                    DEdge("entra", "pgb", label = "2 sent as the password"),
                    DEdge("pgb", "pg", label = "3 a pooled connection"),
                ),
                aspect = 1.15f,
            ),
            B.h("Connecting from Python"),
            B.p("""
                There are two ways to sign in. A connection string with a PostgreSQL user name and password works,
                but the password then has to live somewhere, ideally Key Vault. With Microsoft Entra authentication the
                app's managed identity asks Entra ID for a token and sends that token as the password, so nothing
                secret is stored. Tokens expire, so get a fresh one when you open new connections.
            """),
            B.code("python", """
                import os
                import psycopg
                from azure.identity import DefaultAzureCredential

                cred = DefaultAzureCredential()                # managed identity in Azure, az login locally
                SCOPE = "https://ossrdbms-aad.database.windows.net/.default"

                def connect():
                    return psycopg.connect(
                        host=os.environ["PGHOST"],             # tt-pg.postgres.database.azure.com
                        port=6432,                             # PgBouncer; 5432 goes straight to PostgreSQL
                        dbname="coach",
                        user=os.environ["PGUSER"],             # the Entra name of the app's identity
                        password=cred.get_token(SCOPE).token,
                        sslmode="require",
                    )

                with connect() as conn:                        # commits when the block ends
                    rows = conn.execute(
                        "SELECT id, title FROM notes WHERE user_id = %s ORDER BY created_at DESC LIMIT 20",
                        (user_id,),                            # parameters, never string formatting
                    ).fetchall()
            """),
            B.check(
                "ai-postgres-k1", "ai-postgres",
                "The Coach API runs with a managed identity. What does it send as the password when it connects with Entra authentication?",
                listOf("An access token from Entra ID", "The server admin password from an app setting", "The managed identity's client id"), 0,
                "DefaultAzureCredential gets a token for the PostgreSQL scope, and psycopg sends it in the password field.",
                whyWrong = mapOf(
                    1 to "That is password authentication, which puts back the secret Entra sign-in removes.",
                    2 to "A client id names the identity but proves nothing; the token is the proof.",
                ),
            ),
            B.h("Modelling the data"),
            B.table(
                listOf("Type", "Use it for", "Two Tracks Coach column"),
                listOf("uuid", "Ids made anywhere without clashes", "id, with DEFAULT gen_random_uuid()"),
                listOf("text", "Strings of any length; no need to guess a limit", "title, body"),
                listOf("timestamptz", "A moment in time, stored as UTC", "created_at"),
                listOf("jsonb", "Flexible, queryable JSON stored in a binary form", "meta: module, source, tags"),
                listOf("integer, bigint", "Counts and numeric keys", "word_count"),
            ),
            B.code("sql", """
                CREATE TABLE notes (
                    id          uuid        PRIMARY KEY DEFAULT gen_random_uuid(),
                    user_id     text        NOT NULL,
                    title       text        NOT NULL,
                    body        text        NOT NULL,
                    meta        jsonb       NOT NULL DEFAULT '{}',
                    created_at  timestamptz NOT NULL DEFAULT now()
                );

                -- B-tree (the default): equality, ranges and ORDER BY
                CREATE INDEX notes_user_created ON notes (user_id, created_at DESC);

                -- GIN: what is inside a jsonb value (also arrays and full-text search)
                CREATE INDEX notes_meta ON notes USING GIN (meta);
                SELECT id, title FROM notes WHERE meta @> '{"module": "containers"}';
            """),
            B.tip("""
                "Filter or sort on a plain column" points to a B-tree index. "Search inside jsonb, an array or text
                with full-text search" points to GIN. Put the equality column first in a composite B-tree, then the
                column you sort or range over.
            """),
            B.h("Is the index used? EXPLAIN ANALYZE"),
            B.p("""
                EXPLAIN shows the plan PostgreSQL chose; EXPLAIN ANALYZE also runs the query and reports the real
                times and row counts. A Seq Scan on a big table for a selective filter usually means a missing or
                unusable index. Remember that ANALYZE really executes the statement, so wrap an UPDATE or DELETE in a
                transaction you roll back.
            """),
            B.code("sql", """
                EXPLAIN ANALYZE
                SELECT id, title FROM notes WHERE user_id = 'usaid' ORDER BY created_at DESC LIMIT 20;

                -- before:  Seq Scan on notes ... rows removed by filter: 498120 ... Execution Time: 212 ms
                -- after:   Index Scan using notes_user_created on notes ...    Execution Time: 0.4 ms
            """),
            B.check(
                "ai-postgres-k2", "ai-postgres",
                "Queries filter notes with meta @> '{\"module\": \"secure\"}' and are slow. Which index helps?",
                listOf("A GIN index on meta", "A B-tree index on meta", "A B-tree index on created_at"), 0,
                "GIN indexes the keys and values inside jsonb, which is what the containment operator @> searches.",
                whyWrong = mapOf(
                    1 to "A B-tree compares whole values, so it cannot answer whether a jsonb value contains a key.",
                    2 to "created_at is not in the filter, so that index does not narrow the search.",
                ),
            ),
            B.h("Connections, pooling and compute"),
            B.bullets(
                "Every PostgreSQL connection is a separate server process. Opening one costs a handshake, TLS and sign-in; thousands of idle ones use memory the queries need.",
                "Built-in PgBouncer runs on the server itself. Turn on the server parameter pgbouncer.enabled, then connect to the same host on port 6432 instead of 5432. The default pool mode is transaction.",
                "Pooling raises throughput and cuts latency because requests reuse warm server connections instead of paying the connection cost each time, and the server keeps its memory for real work.",
                "Inside the app, keep a client-side pool too (psycopg_pool) rather than connecting per request.",
                "Compute tiers: Burstable for dev and light loads, General Purpose for most production, Memory Optimized for large caches and heavy queries. Built-in PgBouncer is not available on Burstable.",
            ),
            B.remember("Same host, port 6432, and pgbouncer.enabled = true. No restart is needed to turn it on."),
            B.worked("""
                Worked example. During a study rush, Two Tracks Coach scales to 40 replicas and the server hits its
                connection limit, with slow queries everywhere. Usaid moves the server from Burstable to General
                Purpose, sets pgbouncer.enabled to true, changes PGPORT from 5432 to 6432 in the Container App, and
                gives each replica a small psycopg_pool. EXPLAIN ANALYZE then shows the notes list query doing a Seq
                Scan, so he adds a B-tree index on (user_id, created_at). Latency drops and connections stay flat.
            """),
            B.faded(
                "ai-postgres-f1",
                "Notes now carry tags in meta, and the filter page searching inside meta is slow.",
                listOf(
                    "Run EXPLAIN ANALYZE on the filter query and see a Seq Scan on notes.",
                    "The filter uses @> to look inside a jsonb column, so create a ___ index on meta.",
                    "Run EXPLAIN ANALYZE again and check the plan now uses the new index.",
                ),
                blank = 1,
                answers = listOf("gin"),
                note = "B-tree for plain values and sorting; GIN for what is inside jsonb, arrays and full-text documents.",
            ),
            B.steps("Do it for real",
                "az postgres flexible-server create --resource-group tt-rg --name tt-pg --tier GeneralPurpose --sku-name Standard_D2ds_v5" to "A General Purpose server; Burstable would rule out built-in PgBouncer.",
                "In the portal, under Security, Authentication, enable Microsoft Entra authentication and add yourself as Entra admin" to "You can sign in with az login and a token.",
                "az postgres flexible-server parameter set --resource-group tt-rg --server-name tt-pg --name pgbouncer.enabled --value true" to "PgBouncer now listens on port 6432; no restart.",
                "Run the CREATE TABLE and CREATE INDEX statements above in psql or the portal's query editor" to "The notes table with a B-tree and a GIN index.",
                "Run the Python connect() code with PGHOST and PGUSER set, then EXPLAIN ANALYZE the list query" to "Rows come back and the plan shows an Index Scan.",
            ),
            B.mistakes(
                "Opening a new database connection for every request instead of reusing a pool.",
                "Turning on PgBouncer but leaving the app on port 5432, so nothing goes through it.",
                "Using a B-tree index for jsonb containment searches; GIN is the one that looks inside.",
                "Building SQL with f-strings instead of %s parameters, inviting SQL injection.",
                "Fetching one Entra token at start-up and reusing it for new connections after it expires.",
            ),
            B.example("Two Tracks Coach: notes in a uuid-keyed table with jsonb meta, a B-tree on (user_id, created_at), a GIN on meta, Entra sign-in with the API's managed identity, and PgBouncer on port 6432 in General Purpose."),
            B.check(
                "ai-postgres-k3", "ai-postgres",
                "Built-in PgBouncer is enabled. What must the application change?",
                listOf("Connect to port 6432 on the same host", "Use a new host name ending in pgbouncer", "Switch the server to Burstable"), 0,
                "PgBouncer runs on the same server and listens on port 6432; the host name stays the same.",
                whyWrong = mapOf(
                    1 to "There is no separate host; only the port changes.",
                    2 to "Burstable does not support built-in PgBouncer at all.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-pgconn-match", "Pair each PostgreSQL type with what it stores best.",
                "uuid" to "Ids created anywhere without clashes",
                "timestamptz" to "A moment in time, kept as UTC",
                "jsonb" to "Flexible JSON you can index and query",
                "text" to "Strings with no fixed length",
            ),
            B.gap(
                "ai-x-pgconn-gap1", "See the real plan and timings of a query.",
                "Run EXPLAIN ___ before the SELECT.", "ANALYZE", "analyze", "analyse",
            ),
            B.gap(
                "ai-x-pgconn-gap2", "Where to connect once built-in PgBouncer is on.",
                "Use the same host on port ___.", "6432",
            ),
            B.tryIt("Try it in 15 minutes", "A small Burstable server is fine for the first three steps; PgBouncer needs General Purpose.",
                "Create a flexible server with Microsoft Entra authentication and add yourself as admin",
                "Connect from Python with psycopg and a token from DefaultAzureCredential",
                "Create the notes table, insert a few thousand rows and EXPLAIN ANALYZE a filter before and after a B-tree index",
                "Add a GIN index on a jsonb column and check a @> query uses it",
            ),
            B.explain(
                "ai-postgres-x1",
                "Explain how the Coach API should connect to Azure Database for PostgreSQL and how you make its queries fast.",
                "The API connects with psycopg using Microsoft Entra authentication: DefaultAzureCredential gets a token for its managed identity and sends it as the password, so no secret is stored. It goes through the built-in PgBouncer on port 6432 and keeps a small client pool, because reusing connections improves throughput and latency. Tables use uuid, text, timestamptz and jsonb; a B-tree index serves filters and sorting, a GIN index searches inside jsonb, and EXPLAIN ANALYZE shows whether the index is used.",
                "Entra token instead of a password" to listOf("entra", "token", "managed identity", "defaultazurecredential"),
                "Pooling through PgBouncer" to listOf("pgbouncer", "pooling", "pool", "6432"),
                "Indexes checked with EXPLAIN ANALYZE" to listOf("b-tree", "btree", "gin", "explain analyze", "index"),
            ),
            B.glossary(
                "Flexible server" to "The managed PostgreSQL offering in Azure, with choice of compute tier and size.",
                "psycopg" to "The standard PostgreSQL driver for Python.",
                "jsonb" to "PostgreSQL's binary JSON type, which can be indexed and queried.",
                "B-tree index" to "The default index, for equality, ranges and sorting.",
                "GIN index" to "An index on the parts inside a value: jsonb keys, array items, words.",
                "EXPLAIN ANALYZE" to "Runs a query and shows its plan with real times and row counts.",
                "PgBouncer" to "A connection pooler built into flexible server, on port 6432.",
            ),
            B.recap(
                "Connect with psycopg and an Entra token from DefaultAzureCredential, or a connection string kept in Key Vault.",
                "Model with uuid, text, timestamptz and jsonb; B-tree for filters and sorts, GIN inside jsonb; prove it with EXPLAIN ANALYZE.",
                "Pool connections: built-in PgBouncer on port 6432 (not Burstable) plus a client pool, for higher throughput and lower latency.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(changeFeed, postgres)
}
