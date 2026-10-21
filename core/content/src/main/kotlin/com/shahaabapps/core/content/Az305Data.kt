package com.shahaabapps.core.content

/** AZ-305 topics, written from the official study guide and Microsoft Learn (Az305). */
internal object Az305Data {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val relational = Topic(
        id = "sa-relational", title = "Relational data: choosing Azure SQL and its tiers", minutes = 30,
        references = listOf(
            B.ref("What is Azure SQL? (IaaS versus PaaS)", LEARN + "azure/azure-sql/azure-sql-iaas-vs-paas-what-is-overview"),
            B.ref("Compare vCore and DTU purchasing models", LEARN + "azure/azure-sql/database/purchasing-models"),
            B.ref("vCore purchasing model: service tiers", LEARN + "azure/azure-sql/database/service-tiers-sql-database-vcore"),
            B.ref("What is the Hyperscale service tier?", LEARN + "azure/azure-sql/database/service-tier-hyperscale"),
            B.ref("Serverless compute tier for Azure SQL Database", LEARN + "azure/azure-sql/database/serverless-tier-overview"),
            B.ref("Elastic pools help you manage and scale multiple databases", LEARN + "azure/azure-sql/database/elastic-pool-overview"),
            B.ref("Use read-only replicas to offload read-only query workloads", LEARN + "azure/azure-sql/database/read-scale-out"),
            B.ref("An overview of Azure SQL Database and Managed Instance security", LEARN + "azure/azure-sql/database/security-overview"),
            B.ref("Learn module: Design a data storage solution for relational data", LEARN + "training/modules/design-data-storage-solution-for-relational-data/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-relational-w1", "sa-relational",
                "The Two Tracks office database uses SQL Agent jobs and queries across several databases. It must move to Azure with few changes and no OS patching. Which service?",
                listOf("Azure SQL Managed Instance", "Azure SQL Database, one database each", "SQL Server on an Azure virtual machine"), 0,
                "Look for the option that keeps almost all of SQL Server, yet still has Azure do the patching.",
            ),
            B.plain("""
                Relational data lives in tables of rows and columns that point at each other through keys, like a
                tidy spreadsheet with strict rules. Azure offers three homes for the SQL Server engine, from fully
                managed to fully yours, plus managed PostgreSQL and MySQL. The exam gives you requirement words; your
                job is to hear which home, which tier and which protection they point to.
            """),
            B.diagram(
                caption = "The same API could talk to any of the three homes; moving right buys control and costs you more work.",
                nodes = listOf(
                    DNode("app", "Two Tracks API", 0.5f, 0.12f, w = 0.3f, h = 0.18f, kind = NodeKind.USER),
                    DNode("db", "Azure SQL Database", 0.17f, 0.5f, w = 0.3f, h = 0.26f, sub = "PaaS, one database"),
                    DNode("mi", "SQL Managed Instance", 0.5f, 0.5f, w = 0.3f, h = 0.26f, tone = 1, sub = "near-full SQL Server"),
                    DNode("vm", "SQL Server on a VM", 0.83f, 0.5f, w = 0.3f, h = 0.26f, tone = 2, sub = "IaaS, you run the OS"),
                    DNode("note", "Left: least control, least work. Right: most control, most work.", 0.5f, 0.86f, w = 0.9f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("app", "db"),
                    DEdge("app", "mi"),
                    DEdge("app", "vm"),
                ),
                aspect = 0.7f,
            ),
            B.h("Three homes for SQL Server"),
            B.p("""
                PaaS, platform as a service, means Azure runs the server and you only run the database: patches,
                backups and high availability are done for you. IaaS, infrastructure as a service, means you rent a
                virtual machine and look after everything on it yourself.
            """),
            B.table(
                listOf("Option", "What you manage", "Pick when you hear"),
                listOf("Azure SQL Database", "Only the database", "New cloud app, one database per app or tenant, least admin"),
                listOf("SQL Managed Instance", "Databases on a managed instance in your virtual network", "Lift and shift with SQL Agent, cross-database queries, CLR, near 100% compatible"),
                listOf("SQL Server on a VM", "The OS, SQL Server, patches", "OS access, a feature PaaS lacks (such as Reporting Services), a pinned SQL version"),
                listOf("Azure Database for PostgreSQL or MySQL", "Only the database", "The app already speaks PostgreSQL or MySQL"),
            ),
            B.p("""
                PostgreSQL and MySQL come as flexible server: a managed server with Burstable, General Purpose and
                Memory Optimized tiers, and read replicas. Burstable is like a phone plan that saves up unused minutes:
                cheap for a quiet database that needs a short burst now and then.
            """),
            B.check(
                "sa-relational-k1", "sa-relational",
                "A vendor app needs sysadmin access to the operating system and a SQL Server version Azure no longer offers as PaaS. Where does it go?",
                listOf("SQL Server on an Azure virtual machine", "Azure SQL Managed Instance", "Azure SQL Database Hyperscale"), 0,
                "Only IaaS gives you the operating system and lets you pick an exact SQL Server version.",
                whyWrong = mapOf(
                    1 to "Managed Instance is very compatible, but Azure still owns the OS and keeps the engine current.",
                    2 to "Hyperscale is about size and scale, and it is PaaS: no OS access at all.",
                ),
            ),
            B.h("Buying compute: DTU or vCore"),
            B.p("""
                Azure SQL Database has two purchasing models, meaning two ways to pay for power. A DTU, a database
                transaction unit, is a fixed bundle of CPU, memory and disk speed, like a set menu: simple, with Basic,
                Standard and Premium sizes. A vCore, a virtual processor core, lets you pick processors and storage
                separately, like ordering à la carte. vCore is the one the exam favours: it has the three modern tiers,
                serverless, and Azure Hybrid Benefit, which lets you reuse SQL Server licences you already own.
            """),
            B.compare("Which vCore service tier?",
                B.card("General Purpose", "Most business apps on a budget.", "Remote premium storage", "Up to 4 TB", "One replica, no readable copy"),
                B.card("Business Critical", "Lowest latency and fastest failover.", "Local SSD storage", "Built-in readable secondary", "In-memory tables"),
                B.card("Hyperscale", "Big or fast-growing databases and heavy reads.", "Storage grows to 128 TB", "Snapshot backups, fast restores", "Up to 30 named replicas"),
            ),
            B.p("""
                Two more choices sit beside the tier. Serverless is a compute tier for single databases in General
                Purpose or Hyperscale: it scales itself, bills per second, and can pause when idle so you pay only for
                storage. An elastic pool is a shared budget of compute for many databases whose busy hours differ, like
                a family data plan where each phone uses the allowance at a different time.
            """),
            B.tip("\"Unpredictable use, idle at night, lowest cost\" points to serverless. \"Hundreds of small tenant databases with different peaks\" points to an elastic pool. \"Low latency, in-memory, readable replica included\" points to Business Critical."),
            B.check(
                "sa-relational-k2", "sa-relational",
                "Two Tracks hosts one small database per partner school, 200 of them, each busy at a different hour. Cost matters most. What do you recommend?",
                listOf("An elastic pool of Azure SQL databases", "200 Business Critical databases", "One SQL Server VM with 200 databases"), 0,
                "A pool shares one budget of compute across databases whose peaks do not line up, which is cheaper than sizing each one for its peak.",
                whyWrong = mapOf(
                    1 to "Business Critical is the most expensive tier, and nothing here needs its latency.",
                    2 to "A VM brings patching and high availability back to you, which PaaS removes.",
                ),
            ),
            B.h("Scaling up and out"),
            B.bullets(
                "Scale up: a bigger tier or more vCores. Simple, but one machine always has a ceiling.",
                "Read scale-out: send read-only queries to a replica. Business Critical includes one; Hyperscale adds named replicas.",
                "Read replicas in PostgreSQL and MySQL flexible server do the same job for those engines.",
                "Sharding: split the data across many databases by a key, such as region, when one database can never be enough.",
                "Elastic pools: scale many small databases together rather than one at a time.",
            ),
            B.h("Protecting the data"),
            B.table(
                listOf("Feature", "What it does", "Requirement words"),
                listOf("Transparent data encryption (TDE)", "Encrypts the files on disk; on by default", "Encrypt data at rest"),
                listOf("Always Encrypted", "The client encrypts columns; the server and its admins never see plain text", "Even DBAs must not read card or health data"),
                listOf("Dynamic data masking", "Shows XXXX instead of the value to users without rights", "Support staff see only the last four digits"),
                listOf("Auditing", "Records who did what, to storage or Log Analytics", "Track access for compliance"),
                listOf("Private endpoint", "Gives the server a private address inside your network", "No traffic over the public internet"),
            ),
            B.check(
                "sa-relational-k3", "sa-relational",
                "Learner health notes must stay unreadable to the database administrators who run the server. Which feature?",
                listOf("Always Encrypted", "Transparent data encryption", "Dynamic data masking"), 0,
                "Always Encrypted keeps the keys with the client, so the server only ever holds cipher text.",
                whyWrong = mapOf(
                    1 to "TDE protects the files on disk, but anyone querying the database sees plain values.",
                    2 to "Masking hides values in results, yet an administrator can still read or unmask them.",
                ),
            ),
            B.worked("""
                Worked design. Requirement: the Two Tracks progress database, about 200 GB, quiet at night, busy after
                school in Europe; the budget is tight; no one wants to patch servers. Options: SQL Server on a VM,
                Managed Instance, Azure SQL Database. No OS or instance-level feature is needed, so Azure SQL Database.
                Usage swings and idles, so vCore General Purpose with serverless compute. Choice: General Purpose
                serverless, TDE on by default, a private endpoint for the API. Why: least admin, pay per second, paused
                at night.
            """),
            B.faded(
                "sa-relational-f1",
                "The Australian leaderboard database will reach 30 TB in a year, serves many readers and needs restores in minutes.",
                listOf(
                    "Relational data, no OS access needed: Azure SQL Database.",
                    "Over 4 TB with fast snapshot restores points to the ___ service tier.",
                    "Add named replicas so leaderboard pages read from copies, not the primary.",
                ),
                blank = 1,
                answers = listOf("hyperscale"),
                note = "General Purpose and Business Critical stop at 4 TB; Hyperscale grows to 128 TB.",
            ),
            B.steps("Do it for real",
                "az group create --name tt-data-rg --location westeurope" to "A resource group to delete afterwards.",
                "az sql server create --name tt-sql-eu --resource-group tt-data-rg --location westeurope --enable-ad-only-auth --external-admin-principal-type User --external-admin-name <you> --external-admin-sid <your-object-id>" to "A logical server that only accepts Microsoft Entra sign-ins.",
                "az sql db create --resource-group tt-data-rg --server tt-sql-eu --name tt-progress --edition GeneralPurpose --compute-model Serverless --family Gen5 --capacity 2 --auto-pause-delay 60" to "A serverless database that pauses after an hour of quiet.",
                "In the portal, open the database, then Dynamic Data Masking, and add a mask on an email column" to "Users without the unmask right see a masked address.",
            ),
            B.mistakes(
                "Picking SQL Database for an app that needs SQL Agent and cross-database queries; Managed Instance keeps them.",
                "Reaching for a VM when nothing needs the OS, and inheriting all the patching.",
                "Thinking TDE hides data from administrators; it only protects files at rest.",
                "Choosing Business Critical for size; Hyperscale is the tier that grows past 4 TB.",
                "Using serverless for a busy database that never idles; provisioned compute is cheaper there.",
            ),
            B.example("Two Tracks goes global: the progress database runs on Azure SQL Database General Purpose serverless in West Europe, the big leaderboard on Hyperscale with named replicas, and the old office database moves to Managed Instance so its SQL Agent jobs keep working."),
            B.h("Check yourself"),
            B.match(
                "sa-x-relational-match", "Pair each requirement with the feature it points to.",
                "Admins must never see the values" to "Always Encrypted",
                "Many databases, different peaks" to "Elastic pool",
                "Idle at night, pay per second" to "Serverless",
                "Grows past 4 TB, fast restores" to "Hyperscale",
            ),
            B.gap(
                "sa-x-relational-gap1", "The feature that shows XXXX to support staff.",
                "Dynamic data ___ hides values in query results.", "masking", "Masking",
            ),
            B.tryIt("Try it in 15 minutes", "Serverless with auto-pause costs very little; delete the resource group afterwards.",
                "Create a serverless General Purpose database with the az sql commands above",
                "Run a query in the portal query editor and watch the vCore use in Metrics",
                "Add a mask on a column and query it as a user without unmask rights",
                "Leave it idle past the auto-pause delay and see its status change to Paused",
            ),
            B.explain(
                "sa-relational-x1",
                "Explain how you would choose a home and a tier for the Two Tracks databases, and how you would protect them.",
                "The old office database needs SQL Agent, so it goes to Managed Instance with few changes. New app databases go to Azure SQL Database on the vCore model: General Purpose for normal work, Business Critical for low latency, Hyperscale for very large data. Quiet databases use serverless, and many small tenant databases share an elastic pool. TDE encrypts files at rest, Always Encrypted hides sensitive columns from administrators, and a private endpoint keeps traffic off the internet.",
                "Managed Instance for compatibility" to listOf("managed instance", "sql agent"),
                "Tier from the requirement" to listOf("general purpose", "business critical", "hyperscale"),
                "Serverless or pool for cost" to listOf("serverless", "elastic pool"),
                "Protection features" to listOf("always encrypted", "transparent data encryption", "tde", "private endpoint", "masking"),
            ),
            B.glossary(
                "DTU" to "Database transaction unit: a fixed bundle of CPU, memory and disk speed.",
                "vCore" to "Virtual processor core: buy compute and storage separately; holds the modern tiers.",
                "Hyperscale" to "Tier with storage up to 128 TB, snapshot backups and named read replicas.",
                "Serverless" to "Compute that scales itself, bills per second and can pause when idle.",
                "Elastic pool" to "Shared compute for many databases whose busy times differ.",
                "Always Encrypted" to "Client-side column encryption; the server never sees plain text.",
            ),
            B.recap(
                "SQL Database for least admin, Managed Instance for compatibility, a VM when you need the OS.",
                "vCore tiers: General Purpose for most, Business Critical for latency, Hyperscale for size; serverless and pools cut cost.",
                "TDE for files at rest, Always Encrypted against admins, masking for staff, auditing and private endpoints.",
            ),
        ),
    )

    private val nonRelational = Topic(
        id = "sa-nonrelational", title = "Semi-structured and unstructured data: Cosmos DB and Storage", minutes = 30,
        references = listOf(
            B.ref("Azure Cosmos DB documentation and APIs", LEARN + "azure/cosmos-db/"),
            B.ref("Consistency levels in Azure Cosmos DB", LEARN + "azure/cosmos-db/consistency-levels"),
            B.ref("Access tiers for blob data", LEARN + "azure/storage/blobs/access-tiers-overview"),
            B.ref("Azure Blob Storage lifecycle management overview", LEARN + "azure/storage/blobs/lifecycle-management-overview"),
            B.ref("Azure Storage redundancy", LEARN + "azure/storage/common/storage-redundancy"),
            B.ref("Introduction to Azure Data Lake Storage", LEARN + "azure/storage/blobs/data-lake-storage-introduction"),
            B.ref("What is Azure Files?", LEARN + "azure/storage/files/storage-files-introduction"),
            B.ref("Store business-critical blob data with immutable storage", LEARN + "azure/storage/blobs/immutable-storage-overview"),
            B.ref("Learn module: Design a data storage solution for non-relational data", LEARN + "training/modules/design-data-storage-solution-for-non-relational-data/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-nonrelational-w1", "sa-nonrelational",
                "Old lesson recordings are almost never opened but must be kept for years at the lowest price. Where do they go?",
                listOf("Blob Storage in the archive tier", "Cosmos DB with autoscale", "Azure Files premium shares"), 0,
                "Think about what the cheapest place to keep a file costs you when you finally want it back.",
            ),
            B.plain("""
                Semi-structured data is data with a loose shape, such as JSON documents where each item may carry
                different fields. Unstructured data has no shape the database understands: photos, videos, PDFs, logs.
                Cosmos DB is Azure's home for the first, and a storage account holds the second. The design work is
                trading features, speed and price, then keeping the data safe.
            """),
            B.diagram(
                caption = "A lifecycle rule moves blobs to cheaper tiers as they age; the cheaper the tier, the dearer each read.",
                nodes = listOf(
                    DNode("hot", "Hot", 0.12f, 0.32f, w = 0.21f, h = 0.34f, sub = "daily use"),
                    DNode("cool", "Cool", 0.37f, 0.32f, w = 0.21f, h = 0.34f, tone = 1, sub = "30 days min"),
                    DNode("cold", "Cold", 0.62f, 0.32f, w = 0.21f, h = 0.34f, tone = 1, sub = "90 days min"),
                    DNode("archive", "Archive", 0.87f, 0.32f, w = 0.21f, h = 0.34f, tone = 2, sub = "180 d, offline"),
                    DNode("note", "Archive is offline: rehydrate to an online tier first, which can take up to 15 hours", 0.5f, 0.8f, w = 0.9f, h = 0.26f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("hot", "cool", label = "age"),
                    DEdge("cool", "cold", label = "age"),
                    DEdge("cold", "archive", label = "age"),
                ),
                aspect = 0.5f,
            ),
            B.h("Cosmos DB: semi-structured data at any scale"),
            B.p("""
                Cosmos DB is a NoSQL database, meaning it stores items rather than rows in fixed tables, and it can
                copy them to any Azure region in a few clicks. You pay in RUs, request units: the price tag on every
                read and write. It speaks several APIs, which are the languages your code uses to talk to it.
            """),
            B.table(
                listOf("API", "Pick when"),
                listOf("NoSQL", "New app storing JSON documents; the richest features"),
                listOf("MongoDB", "The app or team already uses MongoDB drivers and tools"),
                listOf("Apache Cassandra", "Existing Cassandra workload with wide rows and CQL queries"),
                listOf("Apache Gremlin", "Graph data: people, friends, follows, recommendations"),
                listOf("Table", "Key-value data from Azure Table storage that now needs global reach"),
            ),
            B.p("""
                Azure Table storage, inside a storage account, is the cheap key-value option: one region and a simple
                lookup by keys. Move to Cosmos DB for Table when you need low latency worldwide, more regions or
                indexes on every property. Consistency, meaning how fresh a read is after a write in another region,
                runs from strong to eventual; session, the default, means each user always sees their own writes.
            """),
            B.check(
                "sa-nonrelational-k1", "sa-nonrelational",
                "The Coach feature stores lesson notes as JSON with different fields per lesson, read in Europe and Australia with low latency. Which store?",
                listOf("Cosmos DB for NoSQL with both regions added", "Azure Table storage", "Azure SQL Database General Purpose"), 0,
                "Flexible JSON plus low-latency reads in two continents is what Cosmos DB with global distribution is for.",
                whyWrong = mapOf(
                    1 to "Table storage is key-value in one region; it neither indexes rich documents nor serves Australia locally.",
                    2 to "A relational table wants a fixed shape, and one SQL database sits in one region.",
                ),
            ),
            B.h("Storage accounts: blobs, lakes and file shares"),
            B.compare("Which kind of unstructured storage?",
                B.card("Blob Storage", "Files reached over HTTP by apps.", "Images, video, backups", "Access tiers and lifecycle rules", "Cheapest at scale"),
                B.card("Data Lake Storage Gen2", "Analytics over big files in folders.", "Blob with hierarchical namespace", "Real folders, fast renames", "Folder and file permissions"),
                B.card("Azure Files", "A shared drive that servers mount.", "SMB or NFS file shares", "Lift and shift without code changes", "File Sync caches it in the office"),
            ),
            B.p("""
                A hierarchical namespace is a switch on the account that makes folders real, like a filing cabinet
                rather than one huge drawer of labelled papers. Renaming a folder becomes one quick step, and you can
                set POSIX-style permissions on folders and files, which analytics tools need.
            """),
            B.h("Balancing features, performance and cost"),
            B.bullets(
                "Hot: highest storage price, cheapest reads. For data in daily use.",
                "Cool (30 days minimum) and cold (90 days minimum): cheaper to keep, dearer to read, still instant.",
                "Archive (180 days minimum): cheapest of all, offline; a read needs rehydration of up to 15 hours.",
                "Leave a cooler tier early and you pay an early deletion charge for the remaining days.",
                "Lifecycle management rules move or delete blobs by age, so nobody has to remember.",
                "Premium accounts use SSDs for low latency, at a higher price per gigabyte.",
            ),
            B.h("Durability: how many copies, and where"),
            B.table(
                listOf("Redundancy", "Copies", "Survives"),
                listOf("LRS", "Three in one datacenter", "A failed disk or rack"),
                listOf("ZRS", "Three across availability zones", "A whole datacenter going down"),
                listOf("GRS", "LRS here plus LRS in the paired region", "A regional disaster"),
                listOf("GZRS", "ZRS here plus LRS in the paired region", "A zone and a region"),
                listOf("RA-GRS, RA-GZRS", "As above", "Plus you can read the secondary at any time"),
            ),
            B.tip("Archive works only with LRS, GRS or RA-GRS, not with ZRS or GZRS. \"Read from the second region even before failover\" points to the RA- versions."),
            B.check(
                "sa-nonrelational-k2", "sa-nonrelational",
                "Profile photos must survive a zone failure and a regional disaster, and the app must read them from the second region during an outage without waiting for failover. Which redundancy?",
                listOf("RA-GZRS", "GRS", "ZRS"), 0,
                "GZRS covers zones and regions, and the RA- prefix gives read access to the secondary at any time.",
                whyWrong = mapOf(
                    1 to "GRS keeps one datacenter copy at home and gives no secondary reads until failover.",
                    2 to "ZRS stays in one region, so a regional disaster takes every copy.",
                ),
            ),
            B.h("Protection"),
            B.bullets(
                "Soft delete for blobs and containers: a recycle bin, kept 1 to 365 days.",
                "Versioning keeps every earlier version when a blob is overwritten.",
                "Point-in-time restore rolls block blobs back to an earlier moment.",
                "Immutable storage, WORM (write once, read many): a time-based retention policy or a legal hold blocks change and deletion, even by admins.",
                "A delete lock on the account stops someone removing the whole account.",
            ),
            B.check(
                "sa-nonrelational-k3", "sa-nonrelational",
                "Exam results must be kept unchangeable for seven years for a regulator. What do you use?",
                listOf("A time-based retention immutability policy", "Blob soft delete for 365 days", "Blob versioning"), 0,
                "Only an immutability policy stops anyone, administrators included, changing or deleting the data for the period.",
                whyWrong = mapOf(
                    1 to "Soft delete is a recycle bin for mistakes, capped at 365 days, and it can be purged.",
                    2 to "Versioning keeps older copies, but the blobs and versions can still be deleted.",
                ),
            ),
            B.worked("""
                Worked design. Requirement: learners upload practice audio, played often in the first month and
                rarely after; kept two years; must survive a zone failure; cost matters. Options: Azure Files, Cosmos
                DB, Blob Storage. Audio is unstructured and fetched by the app, so Blob Storage. A lifecycle rule moves
                it from hot to cool after 30 days and deletes it after two years. Zones matter, so ZRS. Why not
                archive: ZRS does not support it, and replays would wait hours.
            """),
            B.faded(
                "sa-nonrelational-f1",
                "Recordings of IELTS speaking mock tests: written once, kept for seven years, almost never opened, cheapest possible.",
                listOf(
                    "The files are unstructured: Blob Storage.",
                    "A lifecycle rule moves them to the ___ tier after 30 days.",
                    "Use GRS for a copy in the paired region, since that tier needs LRS, GRS or RA-GRS.",
                ),
                blank = 1,
                answers = listOf("archive"),
                note = "Rarely read and kept for years at the lowest price is the archive tier's job.",
            ),
            B.steps("Do it for real",
                "az storage account create --name ttmedia<unique> --resource-group tt-data-rg --location westeurope --sku Standard_ZRS --kind StorageV2" to "A general-purpose v2 account with three copies across zones.",
                "az storage account blob-service-properties update --account-name ttmedia<unique> --resource-group tt-data-rg --enable-delete-retention true --delete-retention-days 14 --enable-versioning true" to "Soft delete for 14 days and versioning switched on.",
                "In the portal, open the account, then Lifecycle management, and add a rule: move to cool after 30 days, delete after 730" to "The rule appears in the list; it runs once a day.",
                "az cosmosdb create --name tt-coach-db --resource-group tt-data-rg --locations regionName=westeurope --locations regionName=australiaeast" to "A Cosmos DB account in two regions, session consistency by default.",
            ),
            B.mistakes(
                "Expecting archive blobs to open at once; they must be rehydrated first, for hours.",
                "Choosing archive on a ZRS or GZRS account, which does not support that tier.",
                "Using soft delete where a regulator demands immutable storage for years.",
                "Picking GRS and promising reads from the second region before any failover.",
                "Using Azure Files for app media, when Blob Storage is cheaper and built for HTTP access.",
            ),
            B.example("Two Tracks goes global: lesson notes live in Cosmos DB for NoSQL in West Europe and Australia East; audio sits in ZRS Blob Storage with a lifecycle rule; compliance records sit in an archive-tier GRS account with an immutability policy; the old office share moves to Azure Files."),
            B.h("Check yourself"),
            B.match(
                "sa-x-nonrel-match", "Pair each need with the service or setting.",
                "Graph of followers" to "Cosmos DB for Apache Gremlin",
                "Real folders for analytics" to "Hierarchical namespace",
                "Office share without code changes" to "Azure Files",
                "Unchangeable for seven years" to "Immutability policy",
            ),
            B.gap(
                "sa-x-nonrel-gap1", "The redundancy that keeps three copies across zones in one region.",
                "That is ___, zone-redundant storage.", "ZRS", "zrs",
            ),
            B.gap(
                "sa-x-nonrel-gap2", "The minimum stay before leaving a tier is free.",
                "The cool tier has a minimum of ___ days.", "30", "thirty",
            ),
            B.tryIt("Try it in 15 minutes", "A storage account with a few files costs cents; delete the resource group afterwards.",
                "Create a general-purpose v2 account and upload a file to a container",
                "Change the file's tier to cool, then to archive, and try to download it",
                "Delete a blob and bring it back from soft delete",
                "Add a lifecycle rule and read its JSON in the portal's code view",
            ),
            B.explain(
                "sa-nonrelational-x1",
                "Explain where Two Tracks keeps lesson notes, audio and compliance records, and how each is kept safe.",
                "Lesson notes are JSON documents with varying fields, so they go into Cosmos DB for NoSQL with both regions added. Audio is unstructured, so it goes into Blob Storage, where a lifecycle rule moves files from hot to cool as they age. The account uses ZRS so a zone failure loses nothing, with soft delete and versioning for mistakes. Compliance records use the archive tier on GRS with an immutability policy so nobody can change them.",
                "Cosmos DB for documents" to listOf("cosmos db", "cosmos"),
                "Tiers and lifecycle for cost" to listOf("lifecycle", "archive tier", "cool tier", "hot to cool"),
                "Redundancy for durability" to listOf("zrs", "grs", "gzrs", "redundan"),
                "Protection against change" to listOf("immutab", "soft delete", "versioning", "legal hold"),
            ),
            B.glossary(
                "Request unit (RU)" to "Cosmos DB's price tag on each read, write and query.",
                "Hierarchical namespace" to "Account setting that makes folders real; turns Blob Storage into Data Lake Gen2.",
                "Access tier" to "Hot, cool, cold or archive: trading storage price against read price.",
                "Lifecycle management" to "Rules that move or delete blobs by age.",
                "GZRS" to "Copies across zones at home plus copies in the paired region.",
                "Immutable storage" to "WORM: data that cannot be changed or deleted for a set time.",
            ),
            B.recap(
                "Cosmos DB for semi-structured data: pick the API the app speaks; Table storage for cheap key-value in one region.",
                "Blob for app files, Data Lake Gen2 for analytics folders, Azure Files for shares; tiers and lifecycle rules cut cost.",
                "LRS, ZRS, GRS, GZRS for copies, RA- for secondary reads; soft delete, versioning and immutability for protection.",
            ),
        ),
    )

    private val integration = Topic(
        id = "sa-data-integration", title = "Data integration and analysis: moving and making sense of data", minutes = 30,
        references = listOf(
            B.ref("What is Azure Data Factory?", LEARN + "azure/data-factory/introduction"),
            B.ref("Integration runtime in Azure Data Factory", LEARN + "azure/data-factory/concepts-integration-runtime"),
            B.ref("Choose a data pipeline orchestration technology", LEARN + "azure/architecture/data-guide/technology-choices/pipeline-orchestration-data-movement"),
            B.ref("What is Microsoft Fabric?", LEARN + "fabric/fundamentals/microsoft-fabric-overview"),
            B.ref("What is Azure Synapse Analytics?", LEARN + "azure/synapse-analytics/overview-what-is"),
            B.ref("What is Azure Databricks?", LEARN + "azure/databricks/introduction/"),
            B.ref("Welcome to Azure Stream Analytics", LEARN + "azure/stream-analytics/stream-analytics-introduction"),
            B.ref("Learn module: Design data integration", LEARN + "training/modules/design-data-integration/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-data-integration-w1", "sa-data-integration",
                "Data Factory must copy a database that sits behind the office firewall, with no inbound ports opened. What makes that possible?",
                listOf("A self-hosted integration runtime on an office machine", "The Azure integration runtime", "A Stream Analytics job"), 0,
                "Something has to sit inside the office and reach out to Azure, rather than Azure reaching in.",
            ),
            B.plain("""
                Data integration means gathering data from many places into one, on a schedule, cleaning it on the
                way. Data analysis means asking big questions of that gathered data, or of data still streaming in.
                Azure has a mover (Data Factory), places to analyse (Fabric, Synapse, Databricks) and a real-time
                engine (Stream Analytics). The exam asks which fits a requirement.
            """),
            B.diagram(
                caption = "Nightly, Data Factory copies office data into the lake for Databricks; live app events flow through Stream Analytics.",
                nodes = listOf(
                    DNode("onprem", "Office SQL Server", 0.15f, 0.2f, w = 0.26f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2),
                    DNode("shir", "Self-hosted IR", 0.5f, 0.2f, w = 0.26f, h = 0.2f, tone = 1, sub = "on an office PC"),
                    DNode("adf", "Data Factory", 0.85f, 0.2f, w = 0.26f, h = 0.2f, sub = "pipeline, nightly"),
                    DNode("lake", "Data Lake Gen2", 0.85f, 0.55f, w = 0.26f, h = 0.2f, kind = NodeKind.CYLINDER),
                    DNode("dbx", "Databricks", 0.5f, 0.55f, w = 0.26f, h = 0.2f, tone = 1, sub = "clean and join"),
                    DNode("bi", "Power BI report", 0.15f, 0.55f, w = 0.26f, h = 0.2f, tone = 2),
                    DNode("app", "App events", 0.15f, 0.87f, w = 0.2f, h = 0.18f, kind = NodeKind.USER),
                    DNode("eh", "Event Hubs", 0.5f, 0.87f, w = 0.26f, h = 0.18f, tone = 2),
                    DNode("asa", "Stream Analytics", 0.85f, 0.87f, w = 0.26f, h = 0.18f, tone = 1),
                ),
                edges = listOf(
                    DEdge("onprem", "shir"),
                    DEdge("shir", "adf", label = "outbound"),
                    DEdge("adf", "lake", label = "copy"),
                    DEdge("lake", "dbx"),
                    DEdge("dbx", "bi"),
                    DEdge("app", "eh"),
                    DEdge("eh", "asa"),
                    DEdge("asa", "bi", label = "live", dashed = true),
                ),
                aspect = 0.8f,
            ),
            B.h("Azure Data Factory: the mover"),
            B.p("""
                Data Factory is a managed service for ETL, extract, transform and load: take data out of a source,
                reshape it, and put it somewhere useful, like a removal company with a timetable. A pipeline is the
                job list; each activity is one step, such as a copy. A linked service holds the connection to a store,
                and a trigger starts the pipeline on a schedule or when a file arrives. Mapping data flows let you
                design transforms visually, and Data Factory runs them on Spark for you.
            """),
            B.h("Integration runtimes: where the work runs"),
            B.table(
                listOf("Integration runtime", "Runs where", "Pick when"),
                listOf("Azure", "Managed compute in Azure", "Cloud to cloud copies and data flows"),
                listOf("Self-hosted", "Software you install on a Windows machine in your network", "Sources behind a firewall or in a private network; it only connects outbound"),
                listOf("Azure-SSIS", "Managed cluster of VMs", "Lift and shift existing SSIS packages"),
            ),
            B.p("""
                SSIS, SQL Server Integration Services, is the older on-premises tool for the same job. If a company
                already has hundreds of SSIS packages, the Azure-SSIS runtime runs them with little change. For high
                availability, register the self-hosted runtime on up to several machines in the office.
            """),
            B.check(
                "sa-data-integration-k1", "sa-data-integration",
                "The finance team has 150 existing SSIS packages and wants them running in Azure with minimal rework. What do you recommend?",
                listOf("Data Factory with an Azure-SSIS integration runtime", "Rewrite them as Databricks notebooks", "Stream Analytics jobs"), 0,
                "The Azure-SSIS runtime runs SSIS packages natively, which is the lift-and-shift path.",
                whyWrong = mapOf(
                    1 to "Rewriting 150 packages is the opposite of minimal rework.",
                    2 to "Stream Analytics processes live streams; it cannot run SSIS packages.",
                ),
            ),
            B.h("Analysis: Fabric, Synapse or Databricks"),
            B.compare("Where do you analyse?",
                B.card("Microsoft Fabric", "One SaaS platform for the whole analytics team.", "Data Factory, Spark, warehouse, real-time and Power BI together", "OneLake: one lake for the organisation", "Pay for a shared capacity"),
                B.card("Azure Synapse Analytics", "A PaaS warehouse with SQL and Spark in one workspace.", "Dedicated SQL pools for a data warehouse", "Serverless SQL to query files in the lake", "Built-in pipelines"),
                B.card("Azure Databricks", "Spark-heavy data engineering and machine learning.", "Notebooks shared by data teams", "Delta Lake tables on your lake", "Strong for Python and ML"),
            ),
            B.p("""
                Spark is an engine that splits a big job across many machines, like a hundred people each counting one
                box of votes. A data warehouse is a database shaped for big read-only questions rather than many small
                writes. Microsoft's docs point new analytics projects towards Fabric, which is SaaS, software as a
                service: nothing to deploy. Synapse stays supported for existing warehouses. At this exam's depth,
                listen for the team: SQL analysts and a warehouse suggest Synapse or Fabric; data scientists in Python
                notebooks suggest Databricks.
            """),
            B.check(
                "sa-data-integration-k2", "sa-data-integration",
                "Analysts want to run occasional T-SQL queries straight over Parquet files in the data lake, without loading them anywhere or keeping servers running. Which fits?",
                listOf("Synapse serverless SQL pool", "A dedicated SQL pool sized for peak", "Azure SQL Database Business Critical"), 0,
                "Serverless SQL reads files where they lie and charges per data processed, so occasional queries cost little.",
                whyWrong = mapOf(
                    1 to "A dedicated pool is a provisioned warehouse you pay for while it runs, and data is loaded into it.",
                    2 to "A transactional database would need the files loaded first and is not built for scans over a lake.",
                ),
            ),
            B.h("Real time: Stream Analytics"),
            B.p("""
                Stream Analytics runs a SQL-like query over events as they arrive, from Event Hubs or IoT Hub, and
                writes results every few seconds to Power BI, SQL, Cosmos DB or storage. Event Hubs is a front door
                that can take in millions of events a second. Windowing groups events into slices of time, such as
                every five minutes, like counting cars per minute at a junction.
            """),
            B.tip("\"Nightly\", \"copy\", \"on-premises source\" point to Data Factory. \"Within seconds\", \"live dashboard\", \"every five minutes\" point to Stream Analytics. \"Data scientists, notebooks, Spark\" point to Databricks."),
            B.check(
                "sa-data-integration-k3", "sa-data-integration",
                "The Two Tracks status page must show how many lessons were completed per minute, updated within seconds. Which service processes the events?",
                listOf("Stream Analytics reading from Event Hubs", "A nightly Data Factory pipeline", "A Synapse dedicated SQL pool loaded daily"), 0,
                "Continuous windowed queries over a live stream are what Stream Analytics does.",
                whyWrong = mapOf(
                    1 to "Nightly is the opposite of within seconds.",
                    2 to "A daily load into a warehouse answers yesterday's question, not this minute's.",
                ),
            ),
            B.worked("""
                Worked design. Requirement: every night, copy bookings from the London office SQL Server into Azure
                for reports; the office allows no inbound connections; the reports team writes SQL. Options: Stream
                Analytics, Data Factory, a custom script on a VM. A scheduled copy is Data Factory's job. The firewall
                rules out the Azure runtime reaching in, so a self-hosted integration runtime goes on two office
                machines. Data lands in Data Lake Gen2 and the team queries it with SQL. Why: managed, scheduled,
                outbound-only, no code to babysit.
            """),
            B.faded(
                "sa-data-integration-f1",
                "Same need for the Australian office: a nightly copy of its file server and MySQL database into the lake.",
                listOf(
                    "A Data Factory pipeline with copy activities and a schedule trigger.",
                    "The sources sit behind the office firewall, so install a ___ integration runtime there.",
                    "Land the files in Data Lake Gen2 for the analysts.",
                ),
                blank = 1,
                answers = listOf("self-hosted", "selfhosted", "self hosted"),
                note = "Only the self-hosted runtime lives inside the private network and connects outbound to Azure.",
            ),
            B.steps("Do it for real",
                "az extension add --name datafactory" to "The Data Factory commands are added to the CLI.",
                "az datafactory create --resource-group tt-data-rg --factory-name tt-adf-<unique> --location westeurope" to "An empty data factory.",
                "In Data Factory Studio, open Manage, then Integration runtimes, then New, Self-Hosted" to "A key to register the runtime appears.",
                "Install the runtime on a Windows machine on your network and paste the key" to "The runtime shows Running in the studio.",
                "Create a pipeline with a Copy data activity from a local folder to Blob Storage, then Debug" to "The run succeeds and the file appears in the container.",
            ),
            B.mistakes(
                "Using the Azure integration runtime for a source behind a firewall; it cannot reach in.",
                "Picking Data Factory for a dashboard that must update within seconds; that is Stream Analytics.",
                "Rewriting existing SSIS packages when the Azure-SSIS runtime can run them as they are.",
                "Keeping a dedicated SQL pool running for a few queries a month; serverless SQL is cheaper.",
            ),
            B.example("Two Tracks goes global: a Data Factory pipeline with a self-hosted runtime copies the legacy office system into Data Lake Gen2 each night; Databricks cleans and joins it; app events flow through Event Hubs into Stream Analytics for the live status page; Power BI shows both."),
            B.h("Check yourself"),
            B.match(
                "sa-x-integration-match", "Pair each requirement with the service.",
                "Run existing SSIS packages" to "Azure-SSIS integration runtime",
                "Counts per minute, within seconds" to "Stream Analytics",
                "Python notebooks and Spark for ML" to "Azure Databricks",
                "One SaaS platform with OneLake" to "Microsoft Fabric",
            ),
            B.gap(
                "sa-x-integration-gap1", "The Data Factory object that holds the list of steps.",
                "Activities are grouped into a ___.", "pipeline", "Pipeline",
            ),
            B.tryIt("Try it in 15 minutes", "A data factory costs nothing idle; a few debug runs cost cents. Delete it afterwards.",
                "Create a data factory with az datafactory create",
                "Make a pipeline that copies a blob from one container to another, then Debug it",
                "Add a schedule trigger and look at the next run time",
                "Open Monitor and read the copy activity's rows and duration",
            ),
            B.explain(
                "sa-data-integration-x1",
                "Explain how Two Tracks gets office data and live app events into reports, and why each service fits.",
                "A Data Factory pipeline copies the office database each night. Because the office firewall blocks inbound traffic, a self-hosted integration runtime on an office machine connects outbound to Azure. The data lands in Data Lake Gen2, where Databricks or Fabric clean and join it for analysis. Live app events go through Event Hubs into Stream Analytics, which counts lessons per minute for the dashboard within seconds.",
                "Data Factory pipeline for scheduled copies" to listOf("data factory", "pipeline"),
                "Self-hosted runtime for private sources" to listOf("self-hosted", "selfhosted", "integration runtime"),
                "An analytics platform" to listOf("databricks", "fabric", "synapse"),
                "Stream Analytics for real time" to listOf("stream analytics", "event hubs"),
            ),
            B.glossary(
                "ETL" to "Extract, transform, load: take data out, reshape it, put it somewhere useful.",
                "Pipeline" to "A Data Factory job: a list of activities, started by a trigger.",
                "Integration runtime" to "The compute Data Factory runs on: Azure, self-hosted or Azure-SSIS.",
                "Spark" to "An engine that splits big data jobs across many machines.",
                "OneLake" to "Fabric's single data lake shared across an organisation.",
                "Windowing" to "Grouping streamed events into time slices, such as every five minutes.",
            ),
            B.recap(
                "Data Factory moves data on a schedule; the self-hosted runtime reaches private networks, Azure-SSIS runs SSIS packages.",
                "Fabric is the SaaS analytics platform, Synapse a SQL and Spark workspace, Databricks the Spark and ML choice.",
                "Stream Analytics answers in seconds over events from Event Hubs; nightly batch is Data Factory's job.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(relational, nonRelational, integration)

    val quiz: Quiz = Quiz(
        id = "sa-data-test", title = "Data storage module test",
        questions = B.spread(listOf(
            B.q(
                "sa-d1", "sa-relational",
                "An on-premises app uses SQL Agent jobs, cross-database queries and CLR. It must move to a managed service with the fewest code changes. Which service?",
                listOf("Azure SQL Managed Instance", "Azure SQL Database single database", "Azure Database for PostgreSQL flexible server"), setOf(0),
                "Managed Instance is nearly fully compatible with SQL Server, including SQL Agent and cross-database queries, while Azure still patches it.",
                whyWrong = mapOf(
                    1 to "A single database lacks SQL Agent and cross-database queries, so the app would need changes.",
                    2 to "PostgreSQL is a different engine; every query and job would need rewriting.",
                ),
            ),
            B.q(
                "sa-d2", "sa-relational",
                "A new database has unpredictable use, sits idle most nights and weekends, and cost must be as low as possible. Which compute option?",
                listOf("Serverless in the General Purpose tier", "Provisioned Business Critical", "DTU Premium"), setOf(0),
                "Serverless scales with demand, bills per second and can auto-pause when idle, so quiet hours cost only storage.",
                whyWrong = mapOf(
                    1 to "Business Critical is the priciest tier and bills every hour whether or not anyone uses it.",
                    2 to "DTU models have no serverless option and are paid for continuously.",
                ),
            ),
            B.q(
                "sa-d3", "sa-relational",
                "A database is expected to reach 40 TB and needs backups and restores that do not slow down as it grows. Which service tier?",
                listOf("Hyperscale", "Business Critical", "General Purpose"), setOf(0),
                "Hyperscale grows to 128 TB and uses snapshot-based backups, so backup and restore time does not depend on size.",
                whyWrong = mapOf(
                    1 to "Business Critical stops at 4 TB of storage.",
                    2 to "General Purpose also stops at 4 TB.",
                ),
            ),
            B.q(
                "sa-d4", "sa-relational",
                "Card numbers must be encrypted so that even database administrators and the cloud operator can never see them in plain text. What do you recommend?",
                listOf("Always Encrypted", "Transparent data encryption with a customer-managed key", "Dynamic data masking"), setOf(0),
                "Always Encrypted encrypts in the client driver and keeps the keys outside the database, so the server never sees plain values.",
                whyWrong = mapOf(
                    1 to "TDE with your own key still decrypts data for anyone allowed to query it, administrators included.",
                    2 to "Masking only changes what some users see in results; the stored value is plain.",
                ),
            ),
            B.q(
                "sa-d5", "sa-relational",
                "A SaaS company gives each of 500 customers its own small database. Usage per customer is low and peaks at different times. What keeps cost down?",
                listOf("Place the databases in elastic pools", "Give each database its own Hyperscale compute", "Move all customers into one SQL Server VM"), setOf(0),
                "Elastic pools share one compute budget among databases with uneven, non-overlapping peaks.",
                whyWrong = mapOf(
                    1 to "500 separate Hyperscale databases each pay for their own compute, the opposite of sharing.",
                    2 to "A VM adds patching and a single point of failure, and isolation between tenants gets harder.",
                ),
            ),
            B.q(
                "sa-d6", "sa-relational",
                "Reporting queries are slowing down the main order database. The tier must also give the lowest write latency, and a readable copy should come included. Which option?",
                listOf("Business Critical and send reports to its read-only replica", "General Purpose with a larger vCore count", "DTU Basic with more storage"), setOf(0),
                "Business Critical has local SSD storage for low latency and a built-in readable secondary for read scale-out.",
                whyWrong = mapOf(
                    1 to "General Purpose has no readable replica, so reports still hit the primary.",
                    2 to "Basic is the smallest DTU size and offers neither low latency nor read replicas.",
                ),
            ),
            B.q(
                "sa-d7", "sa-relational",
                "Security requires that the Azure SQL server is reachable only from the company virtual network, never over a public address. What do you add?",
                listOf("A private endpoint and disable public network access", "Auditing to Log Analytics", "Dynamic data masking"), setOf(0),
                "A private endpoint gives the server a private IP in the virtual network; turning off public access closes the internet route.",
                whyWrong = mapOf(
                    1 to "Auditing records access; it does not block any network path.",
                    2 to "Masking changes query results, not who can connect.",
                ),
            ),
            B.q(
                "sa-d8", "sa-nonrelational",
                "A team is moving an existing MongoDB app to Azure and wants to keep its drivers and tools. Which service?",
                listOf("Azure Cosmos DB for MongoDB", "Azure Cosmos DB for Apache Cassandra", "Azure Table storage"), setOf(0),
                "Cosmos DB for MongoDB is wire-compatible with MongoDB drivers and tools, so the app changes little.",
                whyWrong = mapOf(
                    1 to "The Cassandra API speaks CQL; MongoDB drivers cannot talk to it.",
                    2 to "Table storage is a simple key-value store with its own API.",
                ),
            ),
            B.q(
                "sa-d9", "sa-nonrelational",
                "Logs must be kept for one year and are almost never read, but when needed an auditor wants them within minutes. Which tier is cheapest that still meets this?",
                listOf("Cold", "Archive", "Hot"), setOf(0),
                "Cold is an online tier with millisecond access and low storage cost, for data kept at least 90 days.",
                whyWrong = mapOf(
                    1 to "Archive is offline; rehydration can take up to 15 hours, far longer than minutes.",
                    2 to "Hot meets the speed need but has the highest storage price for data rarely read.",
                ),
            ),
            B.q(
                "sa-d10", "sa-nonrelational",
                "A storage account must keep data through the loss of a whole availability zone, but there is no need for a copy in another region. Which redundancy is cheapest?",
                listOf("ZRS", "LRS", "GZRS"), setOf(0),
                "ZRS keeps three copies across availability zones in one region, which covers a zone failure.",
                whyWrong = mapOf(
                    1 to "LRS keeps all three copies in one datacenter, so a zone failure can take them all.",
                    2 to "GZRS adds a paired-region copy that was not asked for, at a higher price.",
                ),
            ),
            B.q(
                "sa-d11", "sa-nonrelational",
                "Big data jobs need folders that can be renamed in one step and permissions on each folder and file. Which storage do you recommend?",
                listOf("Data Lake Storage Gen2 with hierarchical namespace", "Blob Storage with a flat namespace", "Azure Files standard share"), setOf(0),
                "The hierarchical namespace makes directories real, with atomic renames and POSIX-style access lists that analytics engines expect.",
                whyWrong = mapOf(
                    1 to "In a flat namespace a folder is only a name prefix, so a rename copies every blob.",
                    2 to "Azure Files is an SMB or NFS share for servers, not the store analytics engines are built around.",
                ),
            ),
            B.q(
                "sa-d12", "sa-nonrelational",
                "A legal team needs certain documents to be impossible to delete until a court case ends, with no fixed end date. What do you set?",
                listOf("A legal hold on the data", "A time-based retention policy of seven years", "Blob soft delete with 365 days"), setOf(0),
                "A legal hold is an immutability setting with no expiry; it stays until someone with rights clears it.",
                whyWrong = mapOf(
                    1 to "A time-based policy needs a known period, and the case end date is unknown.",
                    2 to "Soft delete only delays deletion and caps at 365 days.",
                ),
            ),
            B.q(
                "sa-d13", "sa-nonrelational",
                "Files shared by Windows servers in the office must move to Azure with no app changes, and be cached on an office server for fast access. What do you recommend?",
                listOf("Azure Files with Azure File Sync", "Blob Storage with a lifecycle rule", "Cosmos DB for NoSQL"), setOf(0),
                "Azure Files offers SMB shares that servers mount as before, and File Sync caches hot files on a local Windows Server.",
                whyWrong = mapOf(
                    1 to "Blob Storage is reached over HTTP, so the apps would need changing to use it.",
                    2 to "Cosmos DB stores JSON items, not files on a shared drive.",
                ),
            ),
            B.q(
                "sa-d14", "sa-nonrelational",
                "Compliance data must sit in the archive tier and survive a regional disaster. Which redundancy can the account use?",
                listOf("GRS", "GZRS", "ZRS"), setOf(0),
                "The archive tier works only on LRS, GRS or RA-GRS accounts, and GRS adds the paired-region copy.",
                whyWrong = mapOf(
                    1 to "GZRS accounts do not support the archive tier.",
                    2 to "ZRS does not support archive, and it stays in one region.",
                ),
            ),
            B.q(
                "sa-d15", "sa-nonrelational",
                "A key-value store holds a few gigabytes of settings, read in one region, and the lowest price matters more than global reach. Which service?",
                listOf("Azure Table storage", "Cosmos DB for Table with multi-region writes", "Azure SQL Database Hyperscale"), setOf(0),
                "Table storage is the low-cost key-value option inside a storage account, ideal for a single region.",
                whyWrong = mapOf(
                    1 to "Cosmos DB for Table adds global distribution and indexing that this need does not pay for.",
                    2 to "A relational Hyperscale database is far more than simple key-value lookups require.",
                ),
            ),
            B.q(
                "sa-d16", "sa-data-integration",
                "A nightly job must copy data from an Oracle database inside the company network, which allows no inbound connections, to Azure Data Lake Storage. What do you recommend?",
                listOf("Data Factory with a self-hosted integration runtime", "Data Factory with only the Azure integration runtime", "Stream Analytics with an Oracle input"), setOf(0),
                "A self-hosted runtime installed inside the network makes outbound connections only, so it can reach Oracle and send data to Azure.",
                whyWrong = mapOf(
                    1 to "The Azure runtime runs in Azure and cannot reach into a network that blocks inbound connections.",
                    2 to "Stream Analytics reads live streams, not nightly database copies.",
                ),
            ),
            B.q(
                "sa-d17", "sa-data-integration",
                "Sensor readings from 10,000 devices arrive through IoT Hub. An alert must fire when the five-minute average temperature passes a limit. Which service analyses the stream?",
                listOf("Azure Stream Analytics", "Azure Data Factory", "Azure Synapse dedicated SQL pool"), setOf(0),
                "Stream Analytics runs continuous queries with time windows, such as a five-minute average, over IoT Hub or Event Hubs input.",
                whyWrong = mapOf(
                    1 to "Data Factory moves data in batches; it does not watch a live stream.",
                    2 to "A dedicated pool is a warehouse for loaded data, not a real-time stream processor.",
                ),
            ),
            B.q(
                "sa-d18", "sa-data-integration",
                "Data scientists want to build machine learning features in Python notebooks using Spark over Delta Lake tables. Which service fits best?",
                listOf("Azure Databricks", "Azure Stream Analytics", "Azure SQL Database"), setOf(0),
                "Databricks is the Spark-based platform built around collaborative notebooks, Delta Lake and machine learning.",
                whyWrong = mapOf(
                    1 to "Stream Analytics uses a SQL-like language over streams, not Python notebooks.",
                    2 to "A transactional SQL database is not a Spark platform for data science.",
                ),
            ),
            B.q(
                "sa-d19", "sa-data-integration",
                "A company wants one SaaS analytics platform where data engineering, a warehouse, real-time analytics and Power BI share a single lake, with nothing to deploy. Which do you recommend?",
                listOf("Microsoft Fabric", "Azure Data Factory on its own", "SQL Server on an Azure virtual machine"), setOf(0),
                "Fabric is software as a service that brings these workloads together on OneLake, one lake for the whole organisation.",
                whyWrong = mapOf(
                    1 to "Data Factory moves and transforms data but has no warehouse or reporting of its own.",
                    2 to "A VM is infrastructure you deploy and run yourself, the opposite of SaaS.",
                ),
            ),
            B.q(
                "sa-d20", "sa-data-integration",
                "An organisation has hundreds of SQL Server Integration Services packages and wants them running in Azure with the least rework. What do you recommend?",
                listOf("An Azure-SSIS integration runtime in Data Factory", "Rebuild them as mapping data flows", "Run them in Stream Analytics"), setOf(0),
                "The Azure-SSIS runtime is a managed cluster that runs existing SSIS packages natively, the lift-and-shift path.",
                whyWrong = mapOf(
                    1 to "Rebuilding hundreds of packages is the most rework, not the least.",
                    2 to "Stream Analytics cannot run SSIS packages at all.",
                ),
            ),
            B.q(
                "sa-d21", "sa-nonrelational",
                "A Cosmos DB app runs in two regions. Each user must always see their own latest changes, while keeping latency low. Which consistency level?",
                listOf("Session", "Strong", "Eventual"), setOf(0),
                "Session consistency guarantees read-your-writes within a user's session at nearly the latency of eventual.",
                whyWrong = mapOf(
                    1 to "Strong across regions raises write latency, since every region must commit first.",
                    2 to "Eventual can show a user an older value than the one they just wrote.",
                ),
            ),
        )),
    )
}
