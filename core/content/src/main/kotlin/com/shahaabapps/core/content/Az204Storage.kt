package com.shahaabapps.core.content

/** AZ-204 module 2, Storage: Blob Storage and Cosmos DB. */
internal object Az204Storage {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val blobBasics = Topic(
        id = "az-blob-basics", title = "Blob Storage: accounts, containers, tiers and lifecycle", minutes = 28,
        references = listOf(
            B.ref("Introduction to Azure Blob Storage", LEARN + "azure/storage/blobs/storage-blobs-introduction"),
            B.ref("Access tiers for blob data", LEARN + "azure/storage/blobs/access-tiers-overview"),
            B.ref("Lifecycle management policies", LEARN + "azure/storage/blobs/lifecycle-management-overview"),
            B.ref("Storage account overview (redundancy)", LEARN + "azure/storage/common/storage-account-overview"),
            B.ref("Learn module: Explore Azure Blob storage", LEARN + "training/modules/explore-azure-blob-storage/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-blob-basics-w1", "az-blob-basics",
                "A blob is in the Archive tier and something asks to read it. What do you think happens?",
                listOf("It cannot be read until it is moved to another tier, which takes hours", "It is read, a bit more slowly", "It is read normally and charged more"), 0,
                "Archive is cheaper than the others for a reason that is not about speed.",
            ),
            B.plain("""
                Blob Storage is a place to keep files of any size: images, backups, logs, exports. A storage account
                holds containers, a container holds blobs. You pay for what you store and for how often you touch it.
            """),
            B.h("The three layers"),
            B.diagram(
                caption = "Account, container, blob. The account sets redundancy and performance; the container sets access; the blob is the file.",
                nodes = listOf(
                    DNode("acct", "Storage account", 0.5f, 0.14f, w = 0.5f, h = 0.18f, kind = NodeKind.CYLINDER, tone = 1, sub = "ttstorage, Standard, LRS"),
                    DNode("c1", "Container: exports", 0.25f, 0.5f, w = 0.4f, h = 0.16f, tone = 2),
                    DNode("c2", "Container: photos", 0.75f, 0.5f, w = 0.4f, h = 0.16f, tone = 2),
                    DNode("b1", "2026-10-06.csv", 0.25f, 0.84f, w = 0.36f, h = 0.14f),
                    DNode("b2", "usaid.jpg", 0.75f, 0.84f, w = 0.3f, h = 0.14f),
                ),
                edges = listOf(DEdge("acct", "c1"), DEdge("acct", "c2"), DEdge("c1", "b1"), DEdge("c2", "b2")),
                aspect = 0.68f,
            ),
            B.table(
                listOf("Blob type", "What it is for", "Note"),
                listOf("Block blob", "Files you upload whole or in blocks", "The default; up to about 190 TB"),
                listOf("Append blob", "Logs you add to the end of", "Only append, no edit"),
                listOf("Page blob", "Random-access disks", "Backs VM disks; 512-byte pages"),
            ),
            B.h("Redundancy: how many copies, how far apart"),
            B.table(
                listOf("Option", "Copies", "Survives"),
                listOf("LRS", "3 in one datacenter", "A disk or rack failure"),
                listOf("ZRS", "3 across zones in one region", "A datacenter outage"),
                listOf("GRS", "LRS plus 3 in a paired region", "A regional disaster (read after failover)"),
                listOf("RA-GRS", "GRS plus read access to the secondary", "Read from the second region any time"),
                listOf("GZRS / RA-GZRS", "ZRS plus geo copies", "Both zone and region loss"),
            ),
            B.tip("\"Must survive a regional outage and be readable from the secondary without failover\" is RA-GRS. \"Cheapest with zone resilience\" is ZRS."),
            B.check("az-blob-basics-k1", "az-blob-basics", "An app writes a log line every second and must never rewrite old lines. Which blob type?",
                listOf("Block blob", "Append blob", "Page blob"), 1, "Append blobs are built for add-only logs."),
            B.check(
                "az-blob-basics-k2", "az-blob-basics",
                "The data must survive the loss of an entire region. Which redundancy?",
                listOf("GRS", "ZRS", "LRS"), 0,
                "Geo-redundant storage keeps copies in a second region. Zone- and locally-redundant both stay inside one.",
                whyWrong = mapOf(
                    1 to "ZRS spreads copies across availability zones, which survives a datacentre but not the region around it.",
                    2 to "LRS keeps every copy in one facility.",
                ),
            ),
            B.h("Access tiers: pay for storage or for access, not both"),
            B.table(
                listOf("Tier", "Storage cost", "Access cost", "Minimum stay", "Use"),
                listOf("Hot", "highest", "lowest", "none", "Files used daily"),
                listOf("Cool", "lower", "higher", "30 days", "Monthly reports"),
                listOf("Cold", "lower still", "higher", "90 days", "Rarely read backups"),
                listOf("Archive", "lowest", "highest, hours to rehydrate", "180 days", "Compliance copies"),
            ),
            B.remember("Archive blobs cannot be read until rehydrated to Hot or Cool, which takes up to 15 hours on the standard priority. The exam asks this."),
            B.worked("""
                Worked example. Two Tracks exports every study log to a CSV each night. The current month is read by the
                dashboard (Hot). Older months are kept for a year in case of a dispute (Cool) and then for five years
                for the record (Archive). A lifecycle rule does the moving; nobody touches it by hand.
            """),
            B.faded(
                "az-blob-basics-f1",
                "Same lifecycle, an awkward request. A dispute needs a CSV from three years ago, which the rule moved to Archive long since.",
                listOf(
                    "The blob is still there, and still costs almost nothing to keep.",
                    "It cannot be read until it is ___, which takes hours rather than seconds.",
                    "Until then a read of it simply fails: Archive is offline storage, not slow storage.",
                ),
                blank = 1,
                answers = listOf("rehydrated", "rehydrate", "rehydration"),
                note = "Hot, Cool and Cold are all readable at once, at different prices. Archive is the one that is not readable at all.",
            ),
            B.code("json", """
                {
                  "rules": [{
                    "name": "exports-aging", "enabled": true, "type": "Lifecycle",
                    "definition": {
                      "filters": { "blobTypes": ["blockBlob"], "prefixMatch": ["exports/"] },
                      "actions": { "baseBlob": {
                        "tierToCool":    { "daysAfterModificationGreaterThan": 30 },
                        "tierToArchive": { "daysAfterModificationGreaterThan": 365 },
                        "delete":        { "daysAfterModificationGreaterThan": 2190 }
                      } }
                    }
                  }]
                }
            """),
            B.steps("Create the account and a lifecycle rule",
                "az storage account create --name ttstorage --resource-group tt-rg --sku Standard_ZRS --kind StorageV2" to "A general-purpose v2 account; tiers need v2.",
                "az storage container create --account-name ttstorage --name exports --auth-mode login" to "A private container (no anonymous access).",
                "az storage blob upload --account-name ttstorage --container-name exports --name 2026-10-06.csv --file today.csv --auth-mode login" to "The blob appears in the Hot tier.",
                "az storage account management-policy create --account-name ttstorage --resource-group tt-rg --policy @policy.json" to "The rule runs once a day.",
            ),
            B.compare("Which tier?",
                B.card("Hot", "Read or written often.", "Highest storage price", "Lowest read price", "No minimum stay"),
                B.card("Cool", "Kept for a month or more, read rarely.", "Lower storage", "Early deletion fee under 30 days", "Reads cost more"),
                B.card("Archive", "Kept for compliance, almost never read.", "Cheapest storage", "Offline: rehydrate first", "180-day minimum"),
            ),
            B.mistakes(
                "Trying to read an Archive blob directly. It must be rehydrated first.",
                "Moving data to Cool and deleting it after a week; the early-deletion fee applies.",
                "Choosing GRS when the requirement says read access to the secondary; that needs RA-GRS.",
                "Creating a classic or v1 account and wondering why tiers are missing; use StorageV2.",
            ),
            B.example("Two Tracks uses one ZRS StorageV2 account: exports (lifecycle-managed), photos for profiles, and a static website container for the help pages."),
            B.check(
                "az-blob-basics-k3", "az-blob-basics",
                "An application requests a blob that is in the Archive tier. What happens?",
                listOf("It must be rehydrated first, which takes hours", "It is served, more slowly than from Hot", "It is served normally, at a higher charge"), 0,
                "Archive is offline storage. The blob has to be moved to an online tier before anything can read it.",
                whyWrong = mapOf(
                    1 to "Cool and Cold are cheaper to store and dearer to read, but still readable. Archive is not readable at all until rehydrated.",
                    2 to "There is no read-at-a-premium option for Archive. The data is simply offline.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-s-blob-match", "Pair each blob type with what it is for.",
                "Block blob" to "Files uploaded whole or in blocks: the default",
                "Append blob" to "Logs you only add to the end of",
                "Page blob" to "Random-access disks, in 512-byte pages",
                "Container" to "The folder inside the account that holds blobs",
            ),
            B.gap(
                "az-s-blob-gap1", "A tier charges you either to keep the file or to read it, never cheaply for both.",
                "Monthly reports suit the ___ tier, with its 30-day minimum stay.", "Cool", "cool",
            ),
            B.gap(
                "az-s-blob-gap2", "Rarely read backups, and you accept a rehydration wait before you can read them.",
                "That is the ___ tier.", "Archive", "archive",
            ),
            B.tryIt("Try it in 15 minutes", "Free tier covers a few GB.",
                "Create a StorageV2 account with LRS", "Upload a file to a private container", "Change its tier to Cool in the portal", "Add a lifecycle rule that deletes after 1 day", "Delete the resource group",
            ),
            B.explain(
                "az-blob-basics-x1",
                "Explain the trade-off between access tiers, and what the redundancy letters tell you.",
                "A tier trades the price of storing against the price of reading. Hot is dearest to keep and cheapest to read; Cool and Cold reverse that; Archive is cheapest of all and offline, so a blob has to be rehydrated over hours before anything can read it. Lifecycle rules move and delete blobs by age so nobody has to do it by hand. The redundancy letters say where the copies are: LRS keeps them in one facility, ZRS spreads them across availability zones, GRS puts copies in a second region.",
                "Tiers trade storage price against access price" to listOf("tier", "tiers", "storage", "access", "trade", "cheaper"),
                "Archive is offline and needs rehydrating" to listOf("archive", "rehydrate", "rehydration", "offline", "hours"),
                "LRS, ZRS and GRS say where the copies are" to listOf("lrs", "zrs", "grs", "region", "zone", "copies"),
            ),
            B.glossary(
                "Container" to "A folder-like grouping of blobs with its own access level.",
                "Access tier" to "Hot, Cool, Cold or Archive: the storage and access price trade-off.",
                "Rehydrate" to "Bring an Archive blob back to a readable tier.",
                "Lifecycle rule" to "A policy that moves or deletes blobs by age.",
                "LRS / ZRS / GRS" to "How many copies are kept and how far apart.",
            ),
            B.recap(
                "Account, container, blob; block blobs for files, append blobs for logs, page blobs for disks.",
                "Tiers trade storage price for access price; Archive is offline and needs rehydration.",
                "Lifecycle rules move and delete blobs by age; redundancy letters say where the copies live.",
            ),
        ),
    )

    private val blobSdk = Topic(
        id = "az-blob-sdk", title = "Working with blobs in code: SDK, metadata, SAS and leases", minutes = 26,
        references = listOf(
            B.ref("Azure Blob Storage client library for .NET", LEARN + "azure/storage/blobs/storage-quickstart-blobs-dotnet"),
            B.ref("Manage blob properties and metadata", LEARN + "azure/storage/blobs/storage-blob-properties-metadata"),
            B.ref("Grant limited access with shared access signatures", LEARN + "azure/storage/common/storage-sas-overview"),
            B.ref("Lease Blob", LEARN + "rest/api/storageservices/lease-blob"),
            B.ref("Learn module: Work with Azure Blob storage", LEARN + "training/modules/work-azure-blob-storage/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-blob-sdk-w1", "az-blob-sdk",
                "You need to find blobs anywhere in a storage account by a key-value pair you set. What do you think you use?",
                listOf("Index tags", "Metadata", "The blob name"), 0,
                "Two of these are key-value pairs on a blob. Only one of them can be searched.",
            ),
            B.plain("The SDK has three objects: a service client for the account, a container client, and a blob client. Everything hangs off those."),
            B.diagram(
                caption = "Client objects mirror the storage layers. Create them cheaply; they hold no connection until you call them.",
                nodes = listOf(
                    DNode("svc", "BlobServiceClient", 0.2f, 0.5f, w = 0.3f, h = 0.26f, tone = 1, sub = "the account"),
                    DNode("cont", "BlobContainerClient", 0.52f, 0.5f, w = 0.3f, h = 0.26f, sub = "one container"),
                    DNode("blob", "BlobClient", 0.84f, 0.5f, w = 0.26f, h = 0.26f, tone = 2, sub = "one blob"),
                ),
                edges = listOf(DEdge("svc", "cont", label = "GetBlobContainerClient"), DEdge("cont", "blob", label = "GetBlobClient")),
                aspect = 0.4f,
            ),
            B.code("csharp", """
                var service = new BlobServiceClient(new Uri("https://ttstorage.blob.core.windows.net"), new DefaultAzureCredential());
                var container = service.GetBlobContainerClient("exports");
                await container.CreateIfNotExistsAsync();

                var blob = container.GetBlobClient("2026-10-06.csv");
                await blob.UploadAsync(stream, overwrite: true);

                await foreach (var item in container.GetBlobsAsync(prefix: "2026-10"))
                    Console.WriteLine(item.Name);
            """),
            B.worked("""
                Worked example. DefaultAzureCredential means no connection string in code: locally it uses your az login,
                in Azure it uses the app's managed identity with the Storage Blob Data Contributor role. The same code
                runs in both places with no settings change.
            """),
            B.faded(
                "az-blob-sdk-f1",
                "Same credential story, a link to hand out. Your API must give a browser ten minutes of access to one private blob.",
                listOf(
                    "The account key must not leave the API, so it cannot be the thing that signs this.",
                    "Issue a ___ delegation SAS, signed with an Entra ID key obtained by the app's own identity.",
                    "Set a ten minute expiry, and it can be revoked before then if it has to be.",
                ),
                blank = 1,
                answers = listOf("user"),
                note = "An account-key SAS cannot be revoked without rotating the key for everything else that uses it.",
            ),
            B.h("Properties and metadata"),
            B.p("System properties are things like Content-Type and Last-Modified. Metadata is your own name-value pairs, up to 8 KB, stored with the blob and returned on every read."),
            B.code("csharp", """
                await blob.SetMetadataAsync(new Dictionary<string, string> { ["owner"] = "usaid", ["week"] = "41" });
                var props = await blob.GetPropertiesAsync();
                var owner = props.Value.Metadata["owner"];
                await blob.SetHttpHeadersAsync(new BlobHttpHeaders { ContentType = "text/csv" });
            """),
            B.tip("Metadata keys are case-insensitive and must be valid C# identifiers. The exam likes \"store the uploader's name with the file\": that is metadata, not a tag or a property."),
            B.check("az-blob-sdk-k1", "az-blob-sdk", "You need to search blobs by a custom key across the whole account. Which feature?",
                listOf("Metadata", "Blob index tags", "HTTP headers"), 1, "Index tags are queryable across the account; metadata is only read per blob."),
            B.check(
                "az-blob-sdk-k2", "az-blob-sdk",
                "You need to find blobs across a whole account by a key-value pair. What do you use?",
                listOf("Index tags", "Metadata", "A naming convention"), 0,
                "Index tags are queryable across the account. Metadata travels with a blob but cannot be searched.",
                whyWrong = mapOf(
                    1 to "Metadata is name-value pairs on the blob, with no query over it. That difference is the whole point of the pair.",
                    2 to "A name prefix only narrows within a container, and encodes nothing you can query on.",
                ),
            ),
            B.h("Shared access signatures: a link with limits"),
            B.p("A SAS is a URL with a signature that grants specific permissions for a specific time. Three kinds:"),
            B.table(
                listOf("SAS", "Scope", "Signed with", "Revoke by"),
                listOf("User delegation", "Blob only", "Entra ID credential", "Revoking the delegation key"),
                listOf("Service", "One service (blob, queue, table, file)", "Account key", "Rotating the key or a stored access policy"),
                listOf("Account", "Several services and account operations", "Account key", "Rotating the key"),
            ),
            B.code("csharp", """
                var sas = new BlobSasBuilder {
                    BlobContainerName = "photos", BlobName = "usaid.jpg",
                    Resource = "b", ExpiresOn = DateTimeOffset.UtcNow.AddMinutes(30)
                };
                sas.SetPermissions(BlobSasPermissions.Read);
                var key = await service.GetUserDelegationKeyAsync(DateTimeOffset.UtcNow, DateTimeOffset.UtcNow.AddHours(1));
                var url = new BlobUriBuilder(blob.Uri) { Sas = sas.ToSasQueryParameters(key, service.AccountName) }.ToUri();
            """),
            B.remember("Prefer a user delegation SAS: it is signed with Entra ID, so no account key leaves the vault. A stored access policy lets you revoke a service SAS without rotating the key."),
            B.check(
                "az-blob-sdk-k3", "az-blob-sdk",
                "An API hands out download links for private blobs. Which signature is the safer one?",
                listOf("A short-lived user delegation SAS", "An account-key SAS with a long expiry", "The account key itself"), 0,
                "A user delegation SAS is signed with an Entra ID key, so it can be revoked and carries no account key with it.",
                whyWrong = mapOf(
                    1 to "An account-key SAS cannot be revoked without rotating the key, and a long expiry widens the damage if it leaks.",
                    2 to "Handing out the account key hands out full control of the account.",
                ),
            ),
            B.h("Leases: one writer at a time"),
            B.p("A lease locks a blob for writes for 15 to 60 seconds (or forever until released). Use it when several instances might update the same blob, such as a shared counter or a config file."),
            B.steps("Give a user a 30-minute download link",
                "The API checks the user may see the photo." to "Authorisation happens in your code, not in storage.",
                "Build a BlobSasBuilder with Read and a 30-minute expiry." to "No account key needed with a user delegation key.",
                "Return the full URL with the SAS query string." to "The client downloads straight from storage.",
                "After 30 minutes the link stops working." to "A 403 from storage; the API can issue a new one.",
            ),
            B.mistakes(
                "Embedding the account key in a mobile app to upload. Issue a short SAS from your API instead.",
                "Giving a SAS write permission when only read is needed.",
                "Using metadata for cross-blob search; that is blob index tags.",
                "Forgetting that a lease blocks your own writes too until you pass the lease id.",
            ),
            B.example("Two Tracks profile photos: the app asks the API for a 30-minute read SAS. Uploads go through the API with managed identity. Nobody ever sees the account key."),
            B.h("Check yourself"),
            B.match(
                "az-s-blobsdk-match", "Three kinds of SAS, three ways to take one back. Pair them.",
                "User delegation SAS" to "Signed with an Entra ID credential; revoke the delegation key",
                "Service SAS" to "Signed with the account key, scoped to one service",
                "Account SAS" to "Signed with the account key, spanning several services",
                "Lease" to "Not a link at all: one writer at a time on a blob",
            ),
            B.gap(
                "az-s-blobsdk-gap1", "The safest SAS to hand out, because no account key is involved.",
                "Prefer a ___ delegation SAS.", "user", "User",
            ),
            B.tryIt("Try it in 15 minutes", "Needs the Azure CLI and Storage Blob Data Contributor on your account.",
                "Upload a text blob with az storage blob upload --auth-mode login", "Set metadata on it with az storage blob metadata update", "Generate a 10-minute read SAS with az storage blob generate-sas --as-user", "Open the URL in a browser, then again after it expires",
            ),
            B.explain(
                "az-blob-sdk-x1",
                "Explain the difference between metadata and index tags, and how you would hand someone a safe download link.",
                "Metadata is your own name-value pairs stored with a blob: it travels with it, but there is no query across it. Index tags are also key-value, and are searchable across the whole account, which is the difference that matters. For a download link, issue a shared access signature from your API rather than sharing the account key. A user delegation SAS is signed with an Entra ID key, so it can be revoked and carries no account key, and a short expiry keeps the damage small if it leaks.",
                "Metadata travels but cannot be queried" to listOf("metadata", "travels", "not searchable", "cannot query", "no query"),
                "Index tags are searchable across the account" to listOf("index tag", "tags", "searchable", "query", "across the account"),
                "A short user delegation SAS, not the key" to listOf("user delegation", "sas", "short", "expiry", "revoke"),
            ),
            B.glossary(
                "Metadata" to "Your own name-value pairs stored with a blob.",
                "Index tags" to "Key-value tags you can query across the account.",
                "SAS" to "Shared access signature: a signed URL with limited rights and a lifetime.",
                "User delegation key" to "An Entra-ID-based key for signing a SAS without the account key.",
                "Lease" to "A time-limited write lock on a blob or container.",
            ),
            B.recap(
                "Service, container and blob clients; DefaultAzureCredential keeps keys out of code.",
                "Metadata travels with the blob; index tags are searchable across the account.",
                "Issue short user-delegation SAS links from your API; leases serialise writers.",
            ),
        ),
    )

    val topics = listOf(blobBasics, blobSdk)
}
