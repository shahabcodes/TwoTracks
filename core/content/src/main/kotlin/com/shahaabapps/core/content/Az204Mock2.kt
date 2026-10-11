package com.shahaabapps.core.content

/** Second pool of AZ-204 mock questions, so a retake draws a different paper. */
internal object Az204Mock2 {
    val questions = listOf(
        // Compute
        B.q("mk51", "az-app-service", "An App Service app must be reachable only from a private network, with no public endpoint. Which tier is required?", listOf("Free", "Basic", "Standard", "Isolated"), setOf(3), "Only the Isolated tier runs in an App Service Environment inside your virtual network.",
            whyWrong = mapOf(
                0 to "Free shares its hardware with everyone and has no networking features at all.",
                1 to "Basic gives dedicated compute but still a public endpoint.",
                2 to "Standard adds slots and autoscale. Private-only needs an App Service Environment.",
            ),
        ),
        B.q("mk52", "az-app-service", "Which App Service feature lets you run a background job on a schedule alongside a web app?", listOf("WebJobs", "Deployment slots", "Custom domains", "Easy Auth"), setOf(0), "WebJobs run continuous or triggered background tasks in the same plan.",
            whyWrong = mapOf(
                1 to "Slots are copies of the app for staging and swapping.",
                2 to "A custom domain changes the address, not what runs.",
                3 to "Easy Auth signs callers in.",
            ),
        ),
        B.q("mk53", "az-slots-scaling", "After a swap, the production slot is slow for the first few requests. What prevents this?", listOf("Scaling up", "Application Initialization warm-up before the swap", "A longer cooldown"), setOf(1), "Slot warm-up requests the app before the swap so the instances are already started.",
            whyWrong = mapOf(
                0 to "A bigger machine still starts the app cold on the first request.",
                2 to "Cooldown limits how often autoscale may act, which is a different problem.",
            ),
        ),
        B.q("mk54", "az-slots-scaling", "Which setting type is swapped with the code by default?", listOf("Slot settings marked sticky", "Ordinary application settings", "Publishing credentials"), setOf(1), "Ordinary settings travel with the code; only settings marked as slot settings stay put.",
            whyWrong = mapOf(
                0 to "Sticky settings are the exception: they are the ones that stay behind.",
                2 to "Publishing credentials belong to the slot and are not application configuration.",
            ),
        ),
        B.q("mk55", "az-functions", "A function must process messages from a queue and scale to zero when idle, at the lowest cost. Which hosting option?", listOf("Dedicated App Service plan", "Consumption plan", "Premium plan", "Kubernetes"), setOf(1), "Consumption scales to zero and bills per execution.",
            whyWrong = mapOf(
                0 to "A dedicated plan is always running, so it never scales to zero and is billed whether or not it works.",
                2 to "Premium keeps instances warm on purpose, which is the opposite of scaling to zero.",
                3 to "Kubernetes is the most to run and the most to pay for.",
            ),
        ),
        B.q("mk56", "az-functions", "A function on the Premium plan must avoid cold starts entirely. What do you configure?", listOf("A longer timeout", "Pre-warmed instances (always ready)", "A timer trigger"), setOf(1), "Premium keeps a set number of instances warm.",
            whyWrong = mapOf(
                0 to "A timeout governs how long one execution may run, not how quickly it starts.",
                2 to "A timer firing periodically is a way of keeping an app awake by accident. Premium has a setting for it.",
            ),
        ),
        B.q("mk57", "az-durable", "Which Durable Functions pattern runs several activities at once and then aggregates the results?", listOf("Function chaining", "Fan-out/fan-in", "Human interaction", "Monitor"), setOf(1), "Fan-out starts parallel activities; fan-in awaits them all.",
            whyWrong = mapOf(
                0 to "Chaining runs them one after another.",
                2 to "Human interaction waits for something outside the system.",
                3 to "Monitor polls a condition until it changes.",
            ),
        ),
        B.q("mk58", "az-durable", "An orchestrator must wait up to three days for a manager's approval. Which pattern and feature?", listOf("Monitor with a timer only", "Human interaction with an external event and a durable timer", "Async HTTP API"), setOf(1), "WaitForExternalEvent with a timeout is the approval pattern.",
            whyWrong = mapOf(
                0 to "Monitor polls something on a schedule. An approval arrives when it arrives, and polling for it is the wrong shape.",
                2 to "The async HTTP pattern is about how the caller checks on a long job, not about waiting for a person.",
            ),
        ),
        B.q("mk59", "az-containers", "An image in Azure Container Registry must be pulled by a container app without storing a password. What do you use?", listOf("The admin account", "A managed identity with AcrPull", "A SAS token"), setOf(1), "Managed identity plus the AcrPull role avoids credentials entirely.",
            whyWrong = mapOf(
                0 to "The admin account is a username and password, which is the thing being avoided.",
                2 to "A shared access signature is a storage credential. A registry is not storage.",
            ),
        ),
        B.q("mk60", "az-containers", "Which ACI restart policy suits a job that should run once and stop?", listOf("Always", "OnFailure", "Never"), setOf(1), "OnFailure restarts only if the process fails, and stops after success.",
            whyWrong = mapOf(
                0 to "Always restarts it after a clean exit too, so a job that finished would start again for ever.",
                2 to "Never leaves a genuine crash unretried, which is not what “run once” means.",
            ),
        ),
        // Storage
        B.q("mk61", "az-blob-basics", "Blobs must be protected from accidental deletion for 30 days without extra code. What do you enable?", listOf("Soft delete for blobs", "Archive tier", "A lease"), setOf(0), "Soft delete retains deleted blobs for the configured period.",
            whyWrong = mapOf(
                1 to "Archive is about cost, and a deleted blob in Archive is just as gone.",
                2 to "A lease stops writers while it is held. It does not survive a delete.",
            ),
        ),
        B.q("mk62", "az-blob-basics", "Which tier has a 180-day minimum and is offline until rehydrated?", listOf("Cool", "Cold", "Archive"), setOf(2), "Archive is the cheapest, offline tier with the longest minimum.",
            whyWrong = mapOf(
                0 to "Cool has a 30-day minimum and is online.",
                1 to "Cold has a 90-day minimum and is also online.",
            ),
        ),
        B.q("mk63", "az-blob-sdk", "A SAS must stop working if an employee leaves, without rotating the account key. What do you use?", listOf("An account SAS", "A service SAS tied to a stored access policy", "A longer expiry"), setOf(1), "Deleting the stored access policy revokes every SAS tied to it.",
            whyWrong = mapOf(
                0 to "An account SAS is signed by the account key, so revoking it means rotating that key.",
                2 to "A longer expiry is the opposite of what is wanted.",
            ),
        ),
        B.q("mk64", "az-blob-sdk", "Which client object lists the containers in an account?", listOf("BlobClient", "BlobContainerClient", "BlobServiceClient"), setOf(2), "The service client is the account-level object.",
            whyWrong = mapOf(
                0 to "A BlobClient addresses one blob.",
                1 to "A BlobContainerClient addresses one container and lists what is in it.",
            ),
        ),
        B.q("mk65", "az-cosmos-model", "A container needs reads in Europe and writes in Asia with low latency in both. What do you enable?", listOf("Multi-region writes", "Strong consistency", "Serverless"), setOf(0), "Multi-region writes allow each region to accept writes locally.",
            whyWrong = mapOf(
                1 to "Strong consistency does the reverse: it constrains where writes may happen and raises latency.",
                2 to "Serverless is a billing mode and is single-region.",
            ),
        ),
        B.q("mk66", "az-cosmos-model", "Which consistency level guarantees reads lag by at most a set number of versions or seconds?", listOf("Session", "Bounded staleness", "Consistent prefix"), setOf(1), "Bounded staleness names the lag explicitly as K versions or T seconds.",
            whyWrong = mapOf(
                0 to "Session guarantees you see your own writes, with no stated bound for anybody else.",
                2 to "Consistent prefix promises order without gaps, and says nothing about how far behind.",
            ),
        ),
        B.q("mk67", "az-cosmos-sdk", "Items must be removed automatically 24 hours after creation. What do you set?", listOf("A lifecycle rule", "TTL on the container and items", "A stored procedure on a timer"), setOf(1), "Cosmos DB has native time to live in seconds.",
            whyWrong = mapOf(
                0 to "Lifecycle rules are a Blob Storage idea.",
                2 to "A timer sweeping the container costs request units and runs late.",
            ),
        ),
        B.q("mk68", "az-cosmos-sdk", "Which operation changes a few named fields without sending the whole document?", listOf("UpsertItemAsync", "PatchItemAsync", "ReplaceItemAsync"), setOf(1), "Patch sends only the operations for the named paths.",
            whyWrong = mapOf(
                0 to "Upsert sends the whole document, creating it if it is absent.",
                2 to "Replace sends the whole document too, and fails if it is missing.",
            ),
        ),
        // Security
        B.q("mk69", "az-identity", "A web API must call a downstream API as the signed-in user. Which flow?", listOf("Client credentials", "On-behalf-of", "Device code"), setOf(1), "On-behalf-of exchanges the incoming user token for a token to the downstream API.",
            whyWrong = mapOf(
                0 to "Client credentials acts as the application, losing the user entirely.",
                2 to "Device code signs a user in at a terminal. There is already a signed-in user here.",
            ),
        ),
        B.q("mk70", "az-identity", "Which app registration setting decides who can sign in to a multitenant app?", listOf("Redirect URI", "Supported account types", "Client secret"), setOf(1), "Supported account types sets single tenant, multitenant or personal accounts.",
            whyWrong = mapOf(
                0 to "The redirect URI says where tokens are returned.",
                2 to "A client secret is how a confidential client proves who it is.",
            ),
        ),
        B.q("mk71", "az-msal", "A console app on a machine with no browser must sign a user in. Which flow?", listOf("Authorization code", "Device code", "Client credentials"), setOf(1), "The user enters a code on another device.",
            whyWrong = mapOf(
                0 to "The authorization code flow needs a browser to redirect to.",
                2 to "Client credentials signs no user in at all.",
            ),
        ),
        B.q("mk72", "az-graph", "Which Graph call returns the signed-in user's profile photo?", listOf("GET /me/photo/\\u0024value", "GET /users", "GET /me/messages"), setOf(0), "The photo stream is at /me/photo/\\u0024value.",
            whyWrong = mapOf(
                1 to "/users lists the directory.",
                2 to "/me/messages returns mail.",
            ),
        ),
        B.q("mk73", "az-keyvault", "A certificate for a custom domain must renew itself with a supported authority. Which Key Vault object?", listOf("A secret", "A key", "A certificate"), setOf(2), "Key Vault certificates support auto-renewal with an integrated CA.",
            whyWrong = mapOf(
                0 to "A secret is an opaque string. Nothing renews it for you.",
                1 to "A key is for cryptographic operations and has no issuing authority behind it.",
            ),
        ),
        B.q("mk74", "az-keyvault", "Which role allows reading secret values but not managing the vault?", listOf("Key Vault Administrator", "Key Vault Secrets User", "Contributor"), setOf(1), "Secrets User grants read access to secret values only.",
            whyWrong = mapOf(
                0 to "Administrator can manage the vault and everything in it, which is more than asked.",
                2 to "Contributor manages the vault resource and, notably, does not grant data access at all.",
            ),
        ),
        B.q("mk75", "az-appconfig", "Which App Configuration feature freezes a named set of key values for one release?", listOf("Labels", "Snapshots", "Feature flags"), setOf(1), "A snapshot is an immutable, named point-in-time set of keys.",
            whyWrong = mapOf(
                0 to "A label distinguishes values of the same key, but the values behind it can still change.",
                2 to "A feature flag turns something on and off.",
            ),
        ),
        // Monitor and Connect
        B.q("mk76", "az-appinsights", "Which view follows one request across several services end to end?", listOf("Live Metrics", "The transaction diagnostics view using operation_Id", "Usage"), setOf(1), "Correlated telemetry is assembled by operation_Id.",
            whyWrong = mapOf(
                0 to "Live Metrics shows what is happening this second, not one request’s path.",
                2 to "Usage is about which features people reach.",
            ),
        ),
        B.q("mk77", "az-redis", "Which Redis tier is required for active geo-replication across regions?", listOf("Standard", "Premium", "Enterprise"), setOf(2), "Active-active geo-replication is an Enterprise feature.",
            whyWrong = mapOf(
                0 to "Standard is a replicated pair in one region.",
                1 to "Premium adds persistence, clustering and VNet injection, and passive geo-replication only.",
            ),
        ),
        B.q("mk78", "az-apim", "An API must return a cached response for identical GET requests for five minutes. Which policies?", listOf("rate-limit and quota", "cache-lookup inbound and cache-store outbound", "validate-jwt and cors"), setOf(1), "Lookup on the way in, store on the way out with a duration.",
            whyWrong = mapOf(
                0 to "Those limit how often it may be called.",
                2 to "Those check a token and set browser rules.",
            ),
        ),
        B.q("mk79", "az-eventhubs", "An event hub has four partitions and one consumer group. What is the maximum number of concurrent readers for that group?", listOf("One", "Four", "Unlimited"), setOf(1), "One active reader per partition per consumer group.",
            whyWrong = mapOf(
                0 to "One reader would leave three partitions unread.",
                2 to "The partition count is the ceiling. A fifth reader has nothing to attach to.",
            ),
        ),
        B.q("mk80", "az-servicebus", "A message must be made available only after six hours. What do you set?", listOf("A lock duration of six hours", "ScheduledEnqueueTimeUtc", "A TTL of six hours"), setOf(1), "Scheduled messages appear in the queue at the time you set.",
            whyWrong = mapOf(
                0 to "The lock duration is how long a receiver holds a message it has taken.",
                2 to "A TTL says when a message expires, not when it appears.",
            ),
        ),
    )
}
