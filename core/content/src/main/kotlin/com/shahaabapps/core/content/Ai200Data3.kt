package com.shahaabapps.core.content

/**
 * AI-200 module 2, AI data services, part 3: pgvector on PostgreSQL, Azure
 * Managed Redis, and the module test. Written from the official study guide
 * and Microsoft Learn (Ai200).
 */
internal object Ai200Data3 {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val pgvector = Topic(
        id = "ai-pgvector", title = "pgvector on PostgreSQL: vector search and RAG", minutes = 28,
        references = listOf(
            B.ref("Vector search with pgvector in Azure Database for PostgreSQL", LEARN + "azure/postgresql/extensions/how-to-use-pgvector"),
            B.ref("Optimize performance when using pgvector", LEARN + "azure/postgresql/extensions/how-to-optimize-performance-pgvector"),
            B.ref("Enable and use DiskANN (pg_diskann)", LEARN + "azure/postgresql/extensions/how-to-use-pgdiskann"),
            B.ref("Allow extensions in Azure Database for PostgreSQL", LEARN + "azure/postgresql/extensions/how-to-allow-extensions"),
            B.ref("Compute options in Azure Database for PostgreSQL", LEARN + "azure/postgresql/compute-storage/concepts-compute"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-pgvector-w1", "ai-pgvector",
                "You run CREATE EXTENSION vector on a brand-new Azure PostgreSQL flexible server and it is refused. Why, do you think?",
                listOf("The extension is not on the server's allow-list yet", "pgvector only runs on Cosmos DB", "You must restart the database first"), 0,
                "Azure keeps a list of extensions a server may install, and it starts empty.",
            ),
            B.plain("""
                pgvector is a PostgreSQL extension that adds a vector column type and distance operators, so the
                same database that holds your rows can also find the rows whose embeddings are closest to a
                question. An embedding is a list of numbers a model produces for a piece of text; texts with
                similar meaning get numbers that sit close together. Retrieval-augmented generation (RAG) means:
                find the closest notes, then hand them to a chat model as the facts to answer from.
            """),
            B.diagram(
                caption = "A RAG request: embed the question, fetch the closest notes for this course from pgvector, then ask the chat model with those notes.",
                nodes = listOf(
                    DNode("user", "Learner", 0.1f, 0.5f, w = 0.16f, h = 0.2f, kind = NodeKind.USER, tone = 2),
                    DNode("api", "Coach API", 0.38f, 0.5f, w = 0.3f, h = 0.26f, tone = 1, sub = "FastAPI container"),
                    DNode("emb", "Embedding model", 0.38f, 0.15f, w = 0.4f, h = 0.24f, kind = NodeKind.CLOUD, tone = 2),
                    DNode("pg", "PostgreSQL + pgvector", 0.78f, 0.5f, w = 0.34f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "notes: vector(1536)"),
                    DNode("llm", "Chat model", 0.62f, 0.85f, w = 0.34f, h = 0.22f, kind = NodeKind.CLOUD, tone = 2),
                ),
                edges = listOf(
                    DEdge("user", "api", label = "question"),
                    DEdge("api", "emb", label = "1 embed"),
                    DEdge("api", "pg", label = "2 top 5 + filter"),
                    DEdge("api", "llm", label = "3 answer"),
                ),
                aspect = 0.7f,
            ),
            B.h("Turn it on and store vectors"),
            B.p("""
                On Azure Database for PostgreSQL flexible server an extension must first be allowed at server
                level through the azure.extensions parameter. The extension's real name is vector, not pgvector.
                Then, in each database that needs it, you run CREATE EXTENSION. A column of type vector(n) holds
                n numbers, and n must match the embedding model: 1536 for text-embedding-3-small, for example.
            """),
            B.code("sql", """
                -- once per database, after azure.extensions includes VECTOR
                CREATE EXTENSION IF NOT EXISTS vector;

                CREATE TABLE notes (
                    id         bigserial PRIMARY KEY,
                    user_id    text NOT NULL,
                    course     text NOT NULL,
                    body       text NOT NULL,
                    embedding  vector(1536)
                );
            """),
            B.h("Three distance operators"),
            B.table(
                listOf("Operator", "Distance", "Index operator class"),
                listOf("<->", "Euclidean (L2)", "vector_l2_ops"),
                listOf("<=>", "Cosine distance", "vector_cosine_ops"),
                listOf("<#>", "Negative inner product", "vector_ip_ops"),
            ),
            B.p("""
                Smaller distance means closer, so a search is ORDER BY the distance, then LIMIT k. The index
                is only used when the query's operator matches the operator class the index was built with: a
                cosine index does nothing for a query that orders by Euclidean distance. For embeddings that are
                already normalised to length 1, as OpenAI's are, inner product gives the same ranking and is the
                cheapest to compute.
            """),
            B.check(
                "ai-pgvector-k1", "ai-pgvector",
                "The index was built with vector_cosine_ops, but EXPLAIN shows a sequential scan. The query orders by embedding <-> the question. What is wrong?",
                listOf("The query uses the Euclidean operator, not the cosine one", "The table needs VACUUM", "Indexes only work on vector(3)"), 0,
                "An index serves only the operator of its operator class. Cosine needs the cosine operator in the ORDER BY.",
                whyWrong = mapOf(
                    1 to "Vacuuming tidies dead rows. It does not make an index match a different operator.",
                    2 to "Any defined dimension up to 2000 can be indexed with HNSW or IVFFlat.",
                ),
            ),
            B.h("Indexes: exact by default, approximate by choice"),
            B.p("""
                With no index pgvector compares the question to every row: perfect recall, a lot of CPU. An
                approximate nearest neighbour (ANN) index looks at far fewer rows and may occasionally miss a true
                neighbour. Recall is the share of the true nearest neighbours a search actually returns.
            """),
            B.compare("HNSW or IVFFlat?",
                B.card("HNSW", "Most RAG apps: the best speed-recall trade-off.",
                    "A layered graph of neighbours", "Slower to build and uses more memory", "No training step, so it can be built on an empty table",
                    "Build options m and ef_construction; query knob hnsw.ef_search (default 40)"),
                B.card("IVFFlat", "Fast builds and less memory, with data already loaded.",
                    "Clusters vectors into lists with k-means", "Build after loading data, since the lists are trained on it",
                    "lists about rows / 1000 up to a million rows", "Query knob ivfflat.probes (default 1)"),
            ),
            B.p("""
                Azure also offers DiskANN through the pg_diskann extension, which is Azure-only. It is a graph
                index built for very large tables, with high recall and fast builds, and can compress vectors with
                product quantization. Treat it as the option to name when the table grows to many millions of rows.
            """),
            B.code("sql", """
                CREATE INDEX notes_embedding_hnsw ON notes
                    USING hnsw (embedding vector_cosine_ops) WITH (m = 16, ef_construction = 64);

                -- more candidates, better recall, slower query; SET LOCAL keeps it to one transaction
                BEGIN;
                SET LOCAL hnsw.ef_search = 100;
                SELECT id, body FROM notes
                WHERE user_id = 'u42' AND course = 'ai-200'
                ORDER BY embedding <=> '[0.01, -0.02, ...]'
                LIMIT 5;
                COMMIT;
            """),
            B.tip("\"Best speed-recall trade-off, can index an empty table\" means HNSW. \"Faster build, less memory, needs data first\" means IVFFlat. Raising ef_search or probes buys recall with time."),
            B.h("Reducing compute and sizing the server"),
            B.bullets(
                "Filter with metadata in the WHERE clause (user, course, date) so a search returns only rows the caller may see. With an HNSW or IVFFlat index the filter is applied after the index scan, so a narrow filter can return fewer rows than LIMIT. Fixes: for a very selective filter (one learner's notes), use an exact search on a B-tree index on the filter column; for a few common values, use a partial index; for many values, partition the table; or raise ef_search or turn on iterative index scans (hnsw.iterative_scan).",
                "Return only the columns you need and keep k small; five good notes beat fifty.",
                "Check the plan with EXPLAIN ANALYZE: is the vector index used, and were partitions pruned?",
                "Load data first, then build the index; give the build more maintenance_work_mem and scale up during it if needed.",
                "The index should fit in memory: pick Memory Optimized compute for large vector sets, General Purpose for most apps, never Burstable for production.",
                "Size storage for the vectors plus the index; storage size sets the IOPS you get.",
            ),
            B.check(
                "ai-pgvector-k2", "ai-pgvector",
                "Recall from an HNSW index is too low and latency has room to spare. What do you change first?",
                listOf("Raise hnsw.ef_search", "Drop the index", "Raise ivfflat.probes"), 0,
                "ef_search is the size of the candidate list at query time: larger finds more true neighbours, at some cost in speed.",
                whyWrong = mapOf(
                    1 to "Dropping the index gives perfect recall by scanning every row, which is the CPU bill you were avoiding.",
                    2 to "probes belongs to IVFFlat. It does nothing for an HNSW index.",
                ),
            ),
            B.h("RAG from Python"),
            B.code("python", """
                import numpy as np
                import psycopg
                from pgvector.psycopg import register_vector
                from azure.identity import DefaultAzureCredential

                token = DefaultAzureCredential().get_token(
                    "https://ossrdbms-aad.database.windows.net/.default").token
                conn = psycopg.connect(host="tt-pg.postgres.database.azure.com", dbname="coach",
                                       user="tt-coach-api", password=token, sslmode="require")
                register_vector(conn)  # teaches psycopg the vector type

                def answer(user_id: str, question: str) -> str:
                    q = np.array(embed(question))            # 1 embed the question
                    rows = conn.execute(
                        "SELECT body FROM notes WHERE user_id = %s AND course = %s "
                        "ORDER BY embedding <=> %s LIMIT 5",
                        (user_id, "ai-200", q)).fetchall()    # 2 top 5, filtered
                    context = "\n".join(r[0] for r in rows)
                    return chat("Answer only from these notes:\n" + context, question)  # 3 ground the model
            """),
            B.worked("""
                Worked example. Two Tracks Coach has 400,000 study notes. Asking "what is a dead-letter queue?"
                scanned every row and took 900 ms. Usaid built an HNSW index with vector_cosine_ops, matched it
                with the cosine operator in the query, and added WHERE user_id and course so only that learner's
                notes come back. The filter is applied after the index scan, so it makes results correct, not
                cheaper, and he turned on hnsw.iterative_scan so a narrow filter still returns five rows. EXPLAIN showed an index scan and the query fell to 15 ms. Recall on a test set of
                fifty questions was 0.93, so he set hnsw.ef_search to 80 for the RAG endpoint and got 0.98.
            """),
            B.faded(
                "ai-pgvector-f1",
                "Same Coach, new table: three million flashcard embeddings, the table is created empty and filled during the night, and recall must be high.",
                listOf(
                    "An exact scan of three million rows on every question burns CPU, so the table needs an approximate index.",
                    "Build an ___ index with vector_cosine_ops: best speed-recall trade-off, and no training step, so the empty table is fine.",
                    "If recall is still short, raise hnsw.ef_search for that session and accept slightly slower queries.",
                ),
                blank = 1,
                answers = listOf("hnsw", "HNSW"),
                note = "IVFFlat trains its lists on the data already there, so it is built after loading. HNSW can be built at any time and gives better recall for the same speed.",
            ),
            B.steps("Do it for real",
                "az postgres flexible-server parameter set --resource-group tt-rg --server-name tt-pg --name azure.extensions --value VECTOR" to "The extension is now allowed on the server.",
                "psql \"host=tt-pg.postgres.database.azure.com dbname=coach sslmode=require\" -c \"CREATE EXTENSION IF NOT EXISTS vector;\"" to "CREATE EXTENSION. Repeat in every database that needs it.",
                "Create the notes table with a vector(1536) column and load embeddings" to "SELECT count(*) shows the rows.",
                "CREATE INDEX ... USING hnsw (embedding vector_cosine_ops);" to "The index builds; pg_stat_progress_create_index shows progress.",
                "EXPLAIN ANALYZE the filtered top-5 query" to "An Index Scan on the HNSW index, not a Seq Scan.",
            ),
            B.mistakes(
                "Running CREATE EXTENSION pgvector: the extension is called vector.",
                "Forgetting the azure.extensions allow-list, so CREATE EXTENSION is refused.",
                "Building a cosine index and querying with the Euclidean operator, so the index is never used.",
                "Building IVFFlat on an empty table, so its lists are trained on nothing.",
                "Retrieving top-k across every user's notes and filtering afterwards in Python.",
            ),
            B.example("Two Tracks Coach: notes with vector(1536), an HNSW cosine index, WHERE user_id and course on every search so results are correct (the filter runs after the index scan, so hnsw.iterative_scan is on to still fill LIMIT), ef_search 80 on the RAG endpoint, Memory Optimized compute so the index stays in RAM."),
            B.check(
                "ai-pgvector-k3", "ai-pgvector",
                "A RAG answer quoted another learner's notes. What should the retrieval query have had?",
                listOf("A metadata filter on user_id in the WHERE clause", "A larger LIMIT", "The inner product operator"), 0,
                "Metadata filters decide which rows may compete at all. Retrieval must be scoped to what this caller may see.",
                whyWrong = mapOf(
                    1 to "A larger LIMIT pulls in more rows, including more of other people's notes.",
                    2 to "Changing the distance changes the ranking, not who the notes belong to.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-pgv-match", "Pair each pgvector piece with what it does.",
                "<=>" to "Cosine distance",
                "<->" to "Euclidean (L2) distance",
                "hnsw.ef_search" to "Query-time candidate list for HNSW",
                "ivfflat.probes" to "How many IVFFlat lists a query searches",
                "azure.extensions" to "The server allow-list for extensions",
            ),
            B.gap(
                "ai-x-pgv-gap1", "The name you allow-list and create is not the project's name.",
                "Run CREATE EXTENSION ___; in each database.", "vector", "VECTOR",
            ),
            B.tryIt("Try it in 15 minutes", "A Burstable B1ms server is enough for practice; delete it afterwards.",
                "Allow VECTOR in azure.extensions and run CREATE EXTENSION vector",
                "Create a table with a vector(3) column and insert five rows",
                "Order by each of the three operators and compare the top three rows",
                "Build an HNSW cosine index and confirm the index scan with EXPLAIN",
            ),
            B.explain(
                "ai-pgvector-x1",
                "Explain how Two Tracks Coach finds the notes for a RAG answer in PostgreSQL, and how you keep that search cheap.",
                "The API turns the question into an embedding and runs one SQL query against the vector column: a WHERE clause with metadata such as user_id and course, ORDER BY cosine distance, LIMIT 5. Those notes go into the prompt so the chat model answers from them, which is retrieval-augmented generation. To keep it cheap, an approximate HNSW index replaces scanning every row, the query uses the same operator as the index, and hnsw.ef_search trades a little speed for better recall. Memory Optimized compute keeps the index in RAM.",
                "Embed, retrieve the closest notes, ground the model" to listOf("embedding", "prompt", "retrieval", "grounding", "rag"),
                "Filter with metadata in WHERE" to listOf("metadata", "where clause", "user_id", "filter"),
                "An approximate index instead of a full scan" to listOf("hnsw", "ivfflat", "diskann", "approximate"),
                "Search-time knobs trade recall for speed" to listOf("recall", "ef_search", "probes", "candidate"),
            ),
            B.glossary(
                "pgvector" to "The PostgreSQL extension, named vector, that adds a vector type and distance operators.",
                "Embedding" to "A list of numbers a model gives a text; close numbers mean similar meaning.",
                "HNSW" to "A graph index with the best speed-recall trade-off; tuned at query time with ef_search.",
                "IVFFlat" to "An index that clusters vectors into lists; built after data is loaded; tuned with probes.",
                "DiskANN" to "An Azure-only graph index (pg_diskann) for very large vector tables.",
                "Recall" to "The share of the true nearest neighbours a search actually returns.",
                "RAG" to "Retrieval-augmented generation: fetch relevant data, then give it to the model with the question.",
            ),
            B.recap(
                "Allow VECTOR in azure.extensions, CREATE EXTENSION vector, store embeddings in vector(n).",
                "Match the operator to the index class; HNSW for recall, IVFFlat for fast builds, DiskANN at huge scale.",
                "Filter with metadata in WHERE, keep k small, tune ef_search or probes, keep the index in memory.",
            ),
        ),
    )

    private val redis = Topic(
        id = "ai-redis", title = "Azure Managed Redis: caching and vector search", minutes = 26,
        references = listOf(
            B.ref("What is Azure Managed Redis?", LEARN + "azure/redis/overview"),
            B.ref("Use Microsoft Entra for cache authentication", LEARN + "azure/redis/entra-for-authentication"),
            B.ref("Quickstart: Python app with Azure Managed Redis", LEARN + "azure/redis/python-get-started"),
            B.ref("Vector embeddings and vector search in Azure Managed Redis", LEARN + "azure/redis/overview-vector-similarity"),
            B.ref("Tutorial: Use Azure Managed Redis as a semantic cache", LEARN + "azure/redis/tutorial-semantic-cache"),
            B.ref("Cache-Aside pattern", LEARN + "azure/architecture/patterns/cache-aside"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-redis-w1", "ai-redis",
                "A learner edits a note. The cached copy in Redis is now wrong. What do you think the app should do?",
                listOf("Update the database, then delete the cached key", "Delete the key, then update the database", "Nothing; it expires one day"), 0,
                "Order matters: one way round, a reader can put the old value back.",
            ),
            B.plain("""
                Redis is an in-memory store: values live in RAM, so reads take well under a millisecond. Azure
                Managed Redis is Microsoft's managed Redis built on Redis Enterprise. You use it to keep copies of
                slow or expensive results close to the app, and, with its search module, to find stored vectors by
                similarity, which lets you reuse an earlier LLM answer for a question that means the same thing.
            """),
            B.diagram(
                caption = "Cache-aside: the API checks Redis first, falls back to PostgreSQL or the chat model on a miss, and writes the result back with a TTL.",
                nodes = listOf(
                    DNode("user", "Learner", 0.08f, 0.5f, w = 0.14f, h = 0.2f, kind = NodeKind.USER, tone = 2),
                    DNode("api", "Coach API", 0.36f, 0.5f, w = 0.3f, h = 0.26f, tone = 1, sub = "FastAPI container"),
                    DNode("redis", "Managed Redis", 0.78f, 0.22f, w = 0.36f, h = 0.3f, kind = NodeKind.CYLINDER, sub = "answers + vectors"),
                    DNode("db", "PostgreSQL", 0.78f, 0.78f, w = 0.36f, h = 0.3f, kind = NodeKind.CYLINDER, tone = 2, sub = "source of truth"),
                    DNode("llm", "Chat model", 0.36f, 0.88f, w = 0.34f, h = 0.2f, kind = NodeKind.CLOUD, tone = 2),
                ),
                edges = listOf(
                    DEdge("user", "api"),
                    DEdge("api", "redis", label = "1 GET / KNN"),
                    DEdge("api", "db", label = "2 on a miss"),
                    DEdge("api", "llm", label = "2 on a miss", dashed = true),
                ),
                aspect = 0.7f,
            ),
            B.h("Azure Managed Redis, and the older service"),
            B.table(
                listOf("", "Azure Managed Redis", "Azure Cache for Redis"),
                listOf("Built on", "Redis Enterprise, Redis 7.4", "Community Redis (Basic, Standard, Premium)"),
                listOf("Tiers", "Memory Optimized, Balanced, Compute Optimized, Flash Optimized", "Basic, Standard, Premium (Enterprise tiers too)"),
                listOf("TLS port", "10000", "6380"),
                listOf("Modules", "RediSearch, Bloom, TimeSeries (not on Flash Optimized); RedisJSON on all tiers", "None on Basic, Standard, Premium"),
                listOf("CLI", "az redisenterprise", "az redis"),
            ),
            B.p("""
                Microsoft points new work, and migrations, to Azure Managed Redis. Entra ID authentication is on by
                default for a new cache, and connections use TLS by default; Entra sign-in works only over TLS. Modules
                are chosen when the cache is created and cannot be added later, so if you will ever want vector search, tick RediSearch at creation, with the Enterprise
                clustering policy and the NoEviction eviction policy that RediSearch requires.
            """),
            B.h("Cache-aside, TTL and invalidation"),
            B.bullets(
                "Read: try the cache; on a miss, read the database, store the value with an expiry, return it.",
                "Expiry (TTL, time to live): every cached key gets one, so a missed invalidation heals itself.",
                "Write: update the database first, then delete the cached key; the next read reloads it.",
                "Short TTLs for values that change often, longer ones for stable content; never cache secrets.",
            ),
            B.code("python", """
                import json
                import redis
                from redis_entraid.cred_provider import create_from_default_azure_credential

                # Entra ID: no access key; the library fetches and refreshes the token
                provider = create_from_default_azure_credential(("https://redis.azure.com/.default",))
                r = redis.Redis(host="tt-cache.westeurope.redis.azure.net", port=10000, ssl=True,
                                credential_provider=provider, decode_responses=True)

                def get_summary(note_id: int) -> dict:
                    key = f"summary:{note_id}"
                    cached = r.get(key)
                    if cached:                                  # hit
                        return json.loads(cached)
                    value = load_summary_from_postgres(note_id)  # miss
                    r.set(key, json.dumps(value), ex=3600)       # TTL one hour
                    return value

                def update_note(note_id: int, body: str) -> None:
                    save_note_to_postgres(note_id, body)         # database first
                    r.delete(f"summary:{note_id}")               # then invalidate
            """),
            B.remember("The identity running the code must be added as a Redis user on the cache's Authentication page; a role on the resource alone does not let it run commands."),
            B.check(
                "ai-redis-k1", "ai-redis",
                "Why does every cached summary get an expiry, even though the code deletes keys on write?",
                listOf("So a missed invalidation cannot leave stale data forever", "Redis refuses keys without one", "Expiry makes reads faster"), 0,
                "Invalidation can fail or be forgotten in one code path. A TTL is the safety net that bounds how long a stale value lives.",
                whyWrong = mapOf(
                    1 to "Redis stores keys with no expiry happily; that is exactly the risk.",
                    2 to "A TTL changes how long a key lives, not how fast it is read.",
                ),
            ),
            B.h("Vector search with RediSearch"),
            B.p("""
                RediSearch indexes hashes or JSON documents under a key prefix. A VECTOR field takes an algorithm
                (FLAT for exact brute force on small sets, HNSW for approximate search on larger ones), a type, the
                dimension and a distance metric (COSINE, L2 or IP). A KNN query returns the k nearest vectors and can
                be combined with ordinary filters, such as a TAG for the course.
            """),
            B.code("text", """
                FT.CREATE idx:qa ON HASH PREFIX 1 qa:
                  SCHEMA course TAG answer TEXT
                         embedding VECTOR HNSW 6 TYPE FLOAT32 DIM 1536 DISTANCE_METRIC COSINE

                FT.SEARCH idx:qa "(@course:{ai200})=>[KNN 3 @embedding ${'$'}vec AS score]"
                  PARAMS 2 vec <1536 float32 bytes> SORTBY score RETURN 2 answer score DIALECT 2
            """),
            B.h("Semantic caching of LLM answers"),
            B.p("""
                An exact-key cache misses "what is a DLQ?" when "what is a dead-letter queue?" is already stored.
                A semantic cache embeds the prompt, runs a KNN search, and reuses the stored answer when the
                closest one is within a distance threshold. Start strict and loosen only after testing, keep entries
                on a TTL, and partition by user or tenant and by model version so one person's answer never reaches
                another.
            """),
            B.code("python", """
                import numpy as np
                from redis.commands.search.query import Query

                def cached_answer(course: str, question: str) -> str | None:
                    vec = np.array(embed(question), dtype=np.float32).tobytes()
                    q = (Query("(@course:{" + course + "})=>[KNN 1 @embedding ${'$'}vec AS score]")
                         .sort_by("score").return_fields("answer", "score").dialect(2))
                    hits = r.ft("idx:qa").search(q, query_params={"vec": vec}).docs
                    if hits and float(hits[0].score) < 0.1:   # cosine distance: lower is closer
                        return hits[0].answer
                    return None   # miss: call the model, then HSET qa:<id> with an EXPIRE
            """),
            B.check(
                "ai-redis-k2", "ai-redis",
                "You created a Balanced cache last month without modules, and now need vector search. What do you do?",
                listOf("Create a new cache with RediSearch enabled and migrate", "Enable RediSearch on the existing cache", "Switch the TLS port to 6380"), 0,
                "Modules are chosen at creation in Azure Managed Redis and cannot be added afterwards.",
                whyWrong = mapOf(
                    1 to "There is no switch for that: modules cannot be added to an existing Managed Redis instance.",
                    2 to "6380 is the older Azure Cache for Redis port and has nothing to do with modules.",
                ),
            ),
            B.worked("""
                Worked example. Two Tracks Coach paid for the same chat answers again and again. Usaid created
                tt-cache, Balanced B1, with RediSearch, Enterprise clustering and NoEviction, and added the API's
                managed identity as a Redis user. Note summaries use cache-aside with a one-hour TTL and are deleted
                after each edit. RAG answers go into idx:qa with a course tag and a 24-hour TTL; a new question
                reuses an answer when the cosine distance is under 0.1. A third of questions became cache hits.
            """),
            B.faded(
                "ai-redis-f1",
                "Same cache, a new key: the weekly leaderboard, which is slow to compute in PostgreSQL and changes whenever a score is saved.",
                listOf(
                    "Read leaderboard:week with GET; on a miss compute it in PostgreSQL.",
                    "Store it with SET and an ___ of 600 seconds, so a missed delete cannot leave it stale for long.",
                    "When a score is saved, update PostgreSQL first, then delete leaderboard:week.",
                ),
                blank = 1,
                answers = listOf("expiry", "expiration", "ttl", "expire", "ex", "time to live", "time-to-live"),
                note = "The expiry is the safety net; deleting after the database write is the real invalidation. Deleting first lets a reader cache the old value again.",
            ),
            B.steps("Do it for real",
                "az redisenterprise create --name tt-cache --resource-group tt-rg --location westeurope --sku Balanced_B1 --modules name=RediSearch --clustering-policy EnterpriseCluster --eviction-policy NoEviction" to "A Managed Redis cache with RediSearch; Entra ID is on by default.",
                "Portal: tt-cache > Authentication > Microsoft Entra Authentication > Select member, add yourself and the API's identity" to "Both appear as Redis users.",
                "pip install redis redis-entraid azure-identity numpy" to "The client and the Entra credential provider.",
                "Run the cache-aside code twice for the same note" to "The first call is a miss and loads PostgreSQL; the second is a hit.",
                "Run FT.CREATE, store two answers with HSET, then a KNN query" to "The closer answer comes back with the smaller score.",
            ),
            B.mistakes(
                "Deleting the cached key before updating the database, so a reader caches the old value again.",
                "Caching values with no TTL, so one missed invalidation is stale forever.",
                "Creating the cache without RediSearch and discovering modules cannot be added later.",
                "Copying an access key into app settings when Entra ID with a managed identity is the default.",
                "A semantic cache shared across users with no filter, so one learner sees another's answer.",
            ),
            B.example("Two Tracks Coach: tt-cache with RediSearch, Entra ID through redis-entraid, summaries cached aside for an hour, RAG answers in a semantic cache partitioned by course and user, each on a TTL."),
            B.check(
                "ai-redis-k3", "ai-redis",
                "Which RediSearch vector algorithm gives exact results by comparing every stored vector?",
                listOf("FLAT", "HNSW", "IVFFlat"), 0,
                "FLAT is brute force: exact, fine for small sets. HNSW is approximate and faster on large ones.",
                whyWrong = mapOf(
                    1 to "HNSW is the approximate graph index, chosen for speed on larger sets.",
                    2 to "IVFFlat is a pgvector index type, not a RediSearch one.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-redis-match", "Pair each piece with its job.",
                "SET with ex" to "Store a value with a time to live",
                "DEL after the write" to "Invalidate a cached copy",
                "redis-entraid" to "Entra ID tokens for the Python client",
                "FT.CREATE" to "Define a search index with a VECTOR field",
                "KNN" to "Return the k nearest stored vectors",
            ),
            B.gap(
                "ai-x-redis-gap1", "The pattern where the app, not the cache, loads data on a miss.",
                "This is the cache-___ pattern.", "aside", "Aside",
            ),
            B.tryIt("Try it in 15 minutes", "A Balanced B0 or B1 cache costs a little per hour; delete it afterwards.",
                "Create a Managed Redis cache with RediSearch and add yourself as a Redis user",
                "Connect from Python with redis-entraid and run PING",
                "Write a key with a 60-second TTL and watch TTL count down",
                "Create a small vector index and run a KNN query with two stored answers",
            ),
            B.explain(
                "ai-redis-x1",
                "Explain how Two Tracks Coach uses Azure Managed Redis to save time and money, and how it keeps cached data correct and private.",
                "Summaries use cache-aside: the API reads Redis first and on a cache miss loads PostgreSQL and stores the value with a TTL. When a note changes, the database is updated first and then the key is deleted, so the next read reloads it, and the expiry bounds any staleness. The API connects with Entra ID through a managed identity and redis-entraid, with no access key. For chat answers, RediSearch holds embeddings in an HNSW vector index, and a KNN search reuses an earlier answer for a semantically similar question, filtered by course and user.",
                "Cache-aside on a miss" to listOf("cache-aside", "cache aside", "cacheaside", "cache miss"),
                "TTL and invalidation on write" to listOf("ttl", "expiry", "expire", "invalidate", "deleted"),
                "Entra ID instead of keys" to listOf("entra", "managed identity", "redis-entraid", "token"),
                "Vector search for semantic caching" to listOf("semantic", "knn", "embedding", "redisearch", "vector"),
            ),
            B.glossary(
                "Azure Managed Redis" to "Microsoft's managed Redis built on Redis Enterprise; port 10000, Entra ID by default.",
                "Cache-aside" to "The app checks the cache, loads the database on a miss, and writes the value back.",
                "TTL" to "Time to live: how long a key exists before Redis removes it.",
                "Invalidation" to "Deleting a cached copy after the source data changes.",
                "RediSearch" to "The module that indexes Redis data, including VECTOR fields; chosen at creation.",
                "KNN query" to "A search that returns the k stored vectors nearest to a query vector.",
                "Semantic cache" to "Reusing a stored LLM answer when a new prompt is close in meaning.",
            ),
            B.recap(
                "Azure Managed Redis runs on Redis Enterprise: port 10000, TLS, Entra ID by default, modules picked at creation.",
                "Cache-aside with a TTL on every key; on writes update the database first, then delete the key.",
                "RediSearch VECTOR fields (FLAT or HNSW) and KNN queries power vector search and semantic caching.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(pgvector, redis)

    val quiz: Quiz = Quiz(
        id = "ai-data-test", title = "AI data services module test",
        questions = B.spread(listOf(
            B.q("ai-d1", "ai-cosmos-sdk", "A Python service must read one note when it knows the note's id and partition key value. Which call costs the fewest RUs?",
                listOf("container.read_item(item=id, partition_key=pk)", "A SELECT * query filtered on the id", "A query with enable_cross_partition_query", "Reading the whole partition and filtering in Python"), setOf(0),
                "A point read by id and partition key is the cheapest operation in Cosmos DB, about one RU for a small item.",
                whyWrong = mapOf(
                    1 to "A query works, but the query engine costs more RUs than a point read for the same item.",
                    2 to "A cross-partition query fans out to every partition, the most expensive way to find one item.",
                ),
            ),
            B.q("ai-d2", "ai-cosmos-sdk", "The Coach API connects to Cosmos DB with DefaultAzureCredential and gets 403 on every read. What is missing?",
                listOf("A Cosmos DB data-plane role assignment, such as Built-in Data Reader", "The Contributor role on the resource group", "A firewall rule for port 443", "The account's primary key in app settings"), setOf(0),
                "Entra access to data in Cosmos DB for NoSQL uses its own data-plane roles, assigned with az cosmosdb sql role assignment create.",
                whyWrong = mapOf(
                    1 to "Contributor is a control-plane role: it can manage the account but not read items with Entra ID.",
                    3 to "That brings back the shared key the managed identity was meant to remove.",
                ),
            ),
            B.q("ai-d3", "ai-cosmos-sdk", "Why pass user input to query_items as parameters such as @course rather than building the SQL string?",
                listOf("It prevents injection and lets the query be reused", "Parameters are cheaper in RUs than literals", "Cosmos DB rejects literals in WHERE", "It makes the query cross-partition"), setOf(0),
                "Parameterised queries keep input as data, not query text, which blocks injection.",
                whyWrong = mapOf(
                    1 to "Parameters are about safety, not price; the RU charge depends on the work the query does.",
                    2 to "Literals are allowed; they are just unsafe when they come from user input.",
                ),
            ),
            B.q("ai-d4", "ai-cosmos-perf", "Writes to a container with large documents cost too many RUs, and only two properties are ever queried. What helps most?",
                listOf("Exclude all paths from the indexing policy except the two queried ones", "Switch to Strong consistency", "Add a composite index on every property", "Turn off the partition key"), setOf(0),
                "By default every property is indexed. Indexing only what is queried cuts the RU cost of every write.",
                whyWrong = mapOf(
                    1 to "Strong consistency raises read cost and latency; it does nothing for indexing on write.",
                    2 to "More indexes make writes more expensive, not less.",
                ),
            ),
            B.q("ai-d5", "ai-cosmos-perf", "A query uses ORDER BY c.course ASC, c.createdAt DESC and fails. What does the indexing policy need?",
                listOf("A composite index on course ascending and createdAt descending", "A spatial index", "A range index on id only", "A vector index"), setOf(0),
                "ORDER BY on more than one property needs a composite index with matching paths and orders.",
                whyWrong = mapOf(
                    1 to "Spatial indexes are for geography types.",
                    3 to "A vector index serves VectorDistance, not ordering by two properties.",
                ),
            ),
            B.q("ai-d6", "ai-cosmos-perf", "A learner must always read their own writes, and the app should stay as cheap and fast as possible. Which consistency level?",
                listOf("Session", "Strong", "Eventual", "Bounded staleness"), setOf(0),
                "Session, the default, guarantees read-your-own-writes within a session at low cost.",
                whyWrong = mapOf(
                    1 to "Strong gives the guarantee too, but at the highest latency and RU cost.",
                    2 to "Eventual is cheapest but can return a value older than the learner's own write.",
                ),
            ),
            B.q("ai-d7", "ai-cosmos-vector", "What must a Cosmos DB for NoSQL container define to store and search embeddings?",
                listOf("A vector embedding policy with path, data type, dimensions and distance function; a vector index is optional but recommended", "Only a composite index on the embedding path", "A TTL on the embedding property", "A second container for vectors"), setOf(0),
                "The vector embedding policy is the required part: it describes the vectors so VectorDistance can compare them. A vector index (flat, quantizedFlat or diskANN) is optional but recommended, because it makes those queries faster and cheaper in RUs. Without one, the search does a full scan of every vector, which costs more RUs.",
                whyWrong = mapOf(
                    1 to "Composite indexes are for multi-property ORDER BY, not similarity search.",
                    2 to "TTL only expires items. It does nothing for similarity search.",
                    3 to "Embeddings sit beside the data they describe, in the same item.",
                ),
            ),
            B.q("ai-d8", "ai-cosmos-vector", "Which query returns the five notes closest in meaning to a question embedding in Cosmos DB?",
                listOf("SELECT TOP 5 c.body FROM c ORDER BY VectorDistance(c.embedding, @q)", "SELECT TOP 5 c.body FROM c WHERE c.embedding = @q", "SELECT TOP 5 c.body FROM c ORDER BY c.embedding", "SELECT c.body FROM c WHERE CONTAINS(c.body, @q)"), setOf(0),
                "VectorDistance computes similarity using the container's policy; ordering by it with TOP gives the nearest k.",
                whyWrong = mapOf(
                    1 to "Equality only matches an identical vector, which never happens for a new question.",
                    3 to "CONTAINS is a text match on words, not a similarity search on meaning.",
                ),
            ),
            B.q("ai-d9", "ai-cosmos-vector", "Why exclude the embedding path from the container's regular indexing policy?",
                listOf("Range-indexing thousands of numbers per item wastes RUs and storage on every write", "Vector search fails if the path is range-indexed", "It turns on the vector index", "It is needed for the change feed"), setOf(0),
                "The vector index handles similarity; also range-indexing each number in the array only adds write cost.",
                whyWrong = mapOf(
                    2 to "The vector index comes from the vector index definition, not from excluding the path.",
                    3 to "The change feed works regardless of the indexing policy.",
                ),
            ),
            B.q("ai-d10", "ai-cosmos-changefeed", "Each new note must be embedded once, as soon as it is written. What does the change feed processor need besides the monitored container?",
                listOf("A lease container to track progress", "A Service Bus queue", "A stored procedure", "Strong consistency"), setOf(0),
                "The processor stores its checkpoints and partition ownership in a lease container, which also lets several instances share the work.",
                whyWrong = mapOf(
                    1 to "The change feed is read from Cosmos DB itself; no queue is involved.",
                    3 to "The change feed works with any consistency level.",
                ),
            ),
            B.q("ai-d11", "ai-cosmos-changefeed", "In latest version mode, deleted notes never reach the processor. How do you handle deletions?",
                listOf("Soft delete: set a deleted flag, then let a TTL remove the item", "Raise the RU limit", "Add a second lease container", "Switch to Session consistency"), setOf(0),
                "Latest version mode shows creates and updates only. An update marking the item deleted is seen; TTL removes it later.",
                whyWrong = mapOf(
                    1 to "Throughput has nothing to do with which operations appear in the feed.",
                    2 to "Leases track progress; they do not change what the feed contains.",
                ),
            ),
            B.q("ai-d12", "ai-cosmos-changefeed", "The processor falls behind. Three instances with the same processor name and lease container are started. What happens?",
                listOf("The leases are shared out, so the instances split the work", "Every instance processes every change", "Only the first instance runs", "The feed restarts from the beginning"), setOf(0),
                "Instances with the same name and lease container balance the leases between them automatically.",
                whyWrong = mapOf(
                    1 to "That would happen with different processor names, each keeping its own leases.",
                    3 to "Checkpoints in the lease container let the work continue from where it stopped.",
                ),
            ),
            B.q("ai-d13", "ai-postgres", "A Python app connects to Azure Database for PostgreSQL with Entra ID. What does it pass as the password?",
                listOf("An access token for https://ossrdbms-aad.database.windows.net/.default", "The managed identity's client id", "The server admin password", "Nothing; sslmode=require is enough"), setOf(0),
                "With Entra ID, the token from azure-identity is the password, and the user is the Entra role name in PostgreSQL.",
                whyWrong = mapOf(
                    1 to "The client id names the identity; it proves nothing on its own.",
                    2 to "The admin password is the stored secret Entra ID removes.",
                ),
            ),
            B.q("ai-d14", "ai-postgres", "Many short-lived container replicas open too many PostgreSQL connections. What is the built-in fix on flexible server?",
                listOf("Enable PgBouncer and connect on port 6432", "Switch to Burstable compute", "Raise max_connections to its maximum", "Open a new connection per query"), setOf(0),
                "The built-in PgBouncer pools connections, so many clients share a few server connections.",
                whyWrong = mapOf(
                    1 to "Burstable gives less capacity, not fewer connections.",
                    3 to "A connection per query is exactly the overhead pooling removes.",
                ),
            ),
            B.q("ai-d15", "ai-postgres", "Note metadata is stored as jsonb and queried with the containment operator @>. Which index helps?",
                listOf("GIN", "B-tree", "Hash", "HNSW"), setOf(0),
                "GIN indexes the keys and values inside jsonb and serves containment queries.",
                whyWrong = mapOf(
                    1 to "B-tree suits equality and ranges on scalar columns, not containment inside jsonb.",
                    3 to "HNSW is a vector index for similarity search.",
                ),
            ),
            B.q("ai-d16", "ai-pgvector", "CREATE EXTENSION pgvector fails on a new flexible server. Which two changes fix it? Select all that apply.",
                listOf("Add VECTOR to the azure.extensions parameter", "Run CREATE EXTENSION vector instead", "Restart the server in Burstable tier", "Install pgvector with pip on the server"), setOf(0, 1),
                "Extensions must be allow-listed in azure.extensions, and the extension's name is vector.",
                whyWrong = mapOf(
                    2 to "No tier change or restart is involved in allowing pgvector.",
                    3 to "pip installs the Python client, not a server extension; the managed service provides that.",
                ),
            ),
            B.q("ai-d17", "ai-pgvector", "The table is empty today and fills over time, and high recall matters. Which index type?",
                listOf("HNSW", "IVFFlat", "B-tree", "GIN"), setOf(0),
                "HNSW has no training step, so it can be built on an empty table, and it has the better speed-recall trade-off.",
                whyWrong = mapOf(
                    1 to "IVFFlat trains its lists on existing data, so building it on an empty table gives poor lists.",
                    2 to "B-tree cannot order rows by vector distance.",
                ),
            ),
            B.q("ai-d18", "ai-pgvector", "A RAG search must only return the current learner's notes for one course, and should be cheap. What do you add?",
                listOf("WHERE user_id = %s AND course = %s on the vector query", "A larger LIMIT and filtering in Python", "ivfflat.probes equal to lists", "The inner product operator"), setOf(0),
                "A metadata filter in WHERE keeps other learners' notes out of the results, inside the database. With an approximate index it is applied after the index scan, so it makes results correct rather than cheaper; for one learner's notes, an exact search on a B-tree index on user_id is both correct and cheap.",
                whyWrong = mapOf(
                    1 to "Filtering afterwards wastes work and can leave too few results.",
                    2 to "Probes equal to lists searches every list, which is the full scan you were avoiding.",
                ),
            ),
            B.q("ai-d19", "ai-redis", "Which steps does the cache-aside pattern take on a read miss?",
                listOf("Load from the database, store in the cache with a TTL, return the value", "Return an error and wait for the cache to load itself", "Write to the cache first, then the database", "Read the database every time and skip the cache"), setOf(0),
                "In cache-aside the application loads the data on a miss and puts it in the cache for the next reader.",
                whyWrong = mapOf(
                    1 to "In cache-aside the cache never loads itself; that is the application's job.",
                    2 to "That is a write path, and write-through, not what happens on a read miss.",
                ),
            ),
            B.q("ai-d20", "ai-redis", "A Python app must connect to Azure Managed Redis without an access key. What does it use?",
                listOf("redis-entraid's credential provider with DefaultAzureCredential, on port 10000 with TLS", "The primary access key from az redisenterprise database list-keys", "A SAS token in the connection string", "Port 6379 without TLS"), setOf(0),
                "redis-entraid gets and refreshes Entra tokens for redis-py; the identity must also be added as a Redis user on the cache.",
                whyWrong = mapOf(
                    1 to "An access key is the shared secret Entra ID is meant to replace.",
                    3 to "Entra authentication works only over TLS, and Managed Redis listens on 10000.",
                ),
            ),
            B.q("ai-d21", "ai-redis", "The Coach wants to reuse a chat answer when a new question means the same as an old one. What does Managed Redis need?",
                listOf("The RediSearch module, enabled at creation, with a VECTOR field index and KNN queries", "A longer TTL on exact keys", "The Flash Optimized tier", "Active geo-replication"), setOf(0),
                "Semantic caching embeds prompts and searches them by similarity, which needs RediSearch vector indexing.",
                whyWrong = mapOf(
                    1 to "Exact keys only match identical text, however long they live.",
                    2 to "Flash Optimized does not support RediSearch.",
                ),
            ),
        )),
    )
}
