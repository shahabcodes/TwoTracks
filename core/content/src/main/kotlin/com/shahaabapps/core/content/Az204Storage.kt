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
            B.tryIt("Try it in 15 minutes", "Free tier covers a few GB.",
                "Create a StorageV2 account with LRS", "Upload a file to a private container", "Change its tier to Cool in the portal", "Add a lifecycle rule that deletes after 1 day", "Delete the resource group",
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
            B.tryIt("Try it in 15 minutes", "Needs the Azure CLI and Storage Blob Data Contributor on your account.",
                "Upload a text blob with az storage blob upload --auth-mode login", "Set metadata on it with az storage blob metadata update", "Generate a 10-minute read SAS with az storage blob generate-sas --as-user", "Open the URL in a browser, then again after it expires",
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
