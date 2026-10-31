package com.shahaabapps.core.content

/** Notes sheets for AZ-204's Monitor, Security and Storage modules. See [Ai200Notes]. */
internal object Az204Notes2 {

    private val appInsights = notes(
        "az-appinsights",
        """
            Application Insights records what your code did in production: requests, dependencies, exceptions and
            custom events, correlated by an operation id and queried with KQL.
        """,
        B.glossary(
            "Instrumentation" to "The SDK or the auto-instrumentation collecting telemetry. Auto covers requests and dependencies; custom events need code.",
            "Operation id" to "The id shared by everything one request caused, which is what makes an end-to-end transaction view possible.",
            "Dependency" to "An outbound call your code made: a database, an HTTP service, a queue. Where most latency actually is.",
            "Live Metrics" to "A real-time view with a second or two of delay, used while a deployment is going out.",
            "Sampling" to "Keeping a fraction of telemetry and scaling the counts, which is how the bill stays sane at volume.",
        ),
        B.diagram(
            caption = "One request's telemetry, correlated by operation id and queried from the workspace.",
            nodes = listOf(
                DNode("req", "Request", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "operation id starts"),
                DNode("dep", "Dependencies", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "SQL, HTTP, queue", hue = DHue.FLOW),
                DNode("ai", "Application Insights", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "same id throughout", hue = DHue.COMPUTE),
                DNode("ws", "Log Analytics", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "where it is stored", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("kql", "KQL", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "questions and alerts", hue = DHue.CHOICE),
            ),
            edges = listOf(
                DEdge("req", "dep"), DEdge("dep", "ai"), DEdge("ai", "ws"), DEdge("ws", "kql"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("Question", "Table", "Operator"),
            listOf("What is failing?", "exceptions", "summarize count() by type"),
            listOf("What is slow?", "requests", "percentile(duration, 95) by name"),
            listOf("Which dependency?", "dependencies", "summarize avg(duration) by target"),
            listOf("Did users see it?", "pageViews, customEvents", "count() by name"),
        ),
        B.commands(
            "Creating it and wiring an app to it",
            B.cmd(
                "az monitor app-insights component create -g tt-rg -a tt-ai -l westeurope --workspace <workspace-id>",
                "Before any application can send telemetry.",
                "Workspace-based is the only kind now, and naming the workspace is what lets KQL query application data beside platform logs.",
            ),
            B.cmd(
                "az webapp config appsettings set -g tt-rg -n tt-web --settings APPLICATIONINSIGHTS_CONNECTION_STRING=<connection>",
                "Turning telemetry on for a web app.",
                "The connection string replaced the instrumentation key, and an app configured with only a key is the outdated answer on the exam.",
            ),
            B.cmd(
                "az monitor app-insights component update -g tt-rg -a tt-ai --sampling-percentage 25",
                "When telemetry costs more than the compute it watches.",
                "Sampling keeps a quarter of the traces and scales the counts, which preserves the shape of the data while cutting the bill.",
            ),
            B.cmd(
                "az monitor log-analytics query -w <workspace-id> --analytics-query \"requests | summarize count() by resultCode\"",
                "When you want a number without opening the portal.",
                "Runs KQL from the command line, which is how a query becomes a check in a script or a release gate.",
            ),
        ),
        B.mistakes(
            "Reporting the average duration. Use p95; the average hides the tail users feel.",
            "Configuring only an instrumentation key, which is the retired mechanism.",
            "Sampling at 100 per cent in production and being surprised by the bill.",
        ),
        B.recap(
            "Requests, dependencies and exceptions, correlated by operation id.",
            "Connection string, not instrumentation key.",
            "Sample, and ask with KQL.",
        ),
    )

    private val availability = notes(
        "az-availability",
        """
            An availability test calls your app from outside on a schedule, an alert rule fires when a signal
            crosses a threshold, and an action group decides who hears about it.
        """,
        B.glossary(
            "Availability test" to "A scheduled request from Microsoft's locations, so an outage is noticed by the platform rather than by a user.",
            "Alert rule" to "A signal, a condition, a window and a frequency. It evaluates on its own schedule, not continuously.",
            "Action group" to "Who and what is notified: email, SMS, a webhook, a function, an ITSM ticket.",
            "Dynamic threshold" to "A threshold the platform learns from history, for metrics with no sensible fixed number.",
            "Autoscale rule" to "Not an alert: it changes instance count instead of telling anyone.",
        ),
        B.diagram(
            caption = "A test calls the app, a rule evaluates the signal, and an action group tells someone.",
            nodes = listOf(
                DNode("test", "Availability test", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "from five regions", hue = DHue.COMPUTE),
                DNode("sig", "Signal", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "metric or query", hue = DHue.FLOW),
                DNode("rule", "Alert rule", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "threshold and window", hue = DHue.CHOICE),
                DNode("act", "Action group", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "email, webhook, function"),
            ),
            edges = listOf(DEdge("test", "sig"), DEdge("sig", "rule"), DEdge("rule", "act", label = "fires")),
            aspect = 1.0f,
        ),
        B.table(
            listOf("Want", "Use", "Not"),
            listOf("Know the site is down", "Availability test and an alert", "A metric on CPU"),
            listOf("Handle load that varies", "Autoscale rules", "An alert to a human"),
            listOf("Catch a spike in 500s", "Log alert on a KQL query", "A metric alert on requests"),
            listOf("Page someone at night", "Action group with SMS or a webhook", "Email only"),
        ),
        B.commands(
            "Rules and who hears them",
            B.cmd(
                "az monitor action-group create -g tt-rg -n tt-oncall --action email dev you@example.com",
                "Before any alert rule is useful.",
                "A rule with no action group fires into nothing, which is how a monitored outage still goes unnoticed for an hour.",
            ),
            B.cmd(
                "az monitor metrics alert create -g tt-rg -n cpu-high --scopes <resource-id> --condition \"avg Percentage CPU > 80\" --window-size 5m --action tt-oncall",
                "For a signal that is already a metric.",
                "Metric alerts are cheap and quick to fire; the window size is what stops a one-minute spike waking somebody for nothing.",
            ),
            B.cmd(
                "az monitor scheduled-query create -g tt-rg -n api-5xx --scopes <workspace-id> --condition \"count 'requests | where resultCode startswith \\\"5\\\"' > 10\" --evaluation-frequency 5m --action-groups <group-id>",
                "When the condition can only be expressed as a query.",
                "Log alerts can say things metrics cannot, at the cost of running on a schedule rather than near real time.",
            ),
        ),
        B.mistakes(
            "Creating an alert rule with no action group.",
            "Using an alert where autoscale is wanted, so a human is woken to press a button the platform could press.",
            "A window so short that normal variation pages someone nightly.",
        ),
        B.recap(
            "Test from outside, alert on a signal, notify through an action group.",
            "Autoscale acts; alerts tell.",
            "Metric alerts are fast; log alerts are expressive.",
        ),
    )

    private val redis = notes(
        "az-redis",
        """
            Azure Cache for Redis makes repeated reads cheap: cache-aside on a miss, a time to live on
            everything, an eviction policy for when it is full, and atomic operations for counters.
        """,
        B.glossary(
            "Cache-aside" to "Read the cache; on a miss read the database, put it in the cache, return it. The pattern most questions mean.",
            "Time to live" to "The expiry on a key. Without one a cache becomes a second database that slowly tells lies.",
            "Eviction policy" to "What is discarded when memory is full. allkeys-lru is the usual answer for a cache.",
            "Cache stampede" to "A popular key expiring and every caller reaching the database at the same moment.",
            "Data persistence" to "Writing the cache to storage so a restart does not lose it. Premium only, and rarely what a cache needs.",
        ),
        B.diagram(
            caption = "Cache-aside: a hit answers immediately, and only a miss reaches the database.",
            nodes = listOf(
                DNode("req", "Read", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "the same data again"),
                DNode("cache", "Redis", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "with a TTL", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("hit", "Hit", 0.27f, 0.58f, w = 0.42f, h = 0.15f, sub = "answer now", hue = DHue.CHOICE),
                DNode("miss", "Miss", 0.73f, 0.58f, w = 0.42f, h = 0.15f, sub = "read and fill", hue = DHue.CHOICE),
                DNode("db", "Database", 0.5f, 0.88f, w = 0.62f, h = 0.15f, sub = "the truth", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("req", "cache"), DEdge("cache", "hit"), DEdge("cache", "miss"), DEdge("miss", "db"),
            ),
            aspect = 1.05f,
        ),
        B.table(
            listOf("Tier", "Gives you", "Pick it when"),
            listOf("Basic", "One node, no replica, no SLA", "Development only"),
            listOf("Standard", "Two nodes with replication", "Most production caching"),
            listOf("Premium", "Clustering, persistence, virtual network", "Large caches or private networking"),
        ),
        B.commands(
            "Creating it and keeping it healthy",
            B.cmd(
                "az redis create -g tt-rg -n tt-cache --sku Standard --vm-size c1",
                "Standing up a cache for anything beyond a demo.",
                "Standard is the cheapest tier with a replica, which is the difference between a restart being invisible and being an outage.",
            ),
            B.cmd(
                "az redis update -g tt-rg -n tt-cache --set redisConfiguration.maxmemory-policy=allkeys-lru",
                "Before the cache can fill up.",
                "The default refuses new writes when memory runs out rather than evicting, which turns a full cache into failing requests.",
            ),
            B.cmd(
                "az redis list-keys -g tt-rg -n tt-cache",
                "When the application needs its connection details.",
                "Prints the access keys, and is worth knowing alongside the better answer of keeping them in Key Vault rather than in configuration.",
            ),
            B.cmd(
                "az redis force-reboot -g tt-rg -n tt-cache --reboot-type PrimaryNode",
                "When testing how the application copes with a failover.",
                "Deliberately restarts a node so you find out whether the client reconnects, which is far better learnt on purpose than at midnight.",
            ),
        ),
        B.mistakes(
            "Caching with no expiry.",
            "Reading a counter, adding one and writing it back. Two callers lose a count; the atomic increment does not.",
            "Choosing Basic for production and discovering there is no replica.",
        ),
        B.recap(
            "Cache-aside with a TTL on everything.",
            "allkeys-lru, so a full cache evicts rather than fails.",
            "Standard at minimum in production.",
        ),
    )

    private val cdn = notes(
        "az-cdn",
        """
            Front Door and CDN cache your content at the edge: the first request fills the edge, later ones are
            served near the user, and a purge is how a bad file stops being served.
        """,
        B.glossary(
            "Point of presence" to "An edge location serving users near it, each with its own cache.",
            "Origin" to "Where the content really lives: a web app, a storage account, anything addressable.",
            "Cache-Control" to "The header deciding how long the edge keeps a response. The origin's instruction to the edge.",
            "Purge" to "Removing cached content from the edge now, rather than waiting for it to expire.",
            "Front Door" to "A global entry point with routing, rules, web application firewall and caching; a CDN profile only caches.",
        ),
        B.diagram(
            caption = "The first request reaches the origin and fills the edge; later ones are served from it.",
            nodes = listOf(
                DNode("user", "User", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "near an edge"),
                DNode("edge", "Edge cache", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "hit, or fill", hue = DHue.DATA),
                DNode("fd", "Front Door", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "routing and rules", hue = DHue.CHOICE),
                DNode("origin", "Origin", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "app or storage", hue = DHue.COMPUTE),
            ),
            edges = listOf(
                DEdge("user", "edge"), DEdge("edge", "fd", label = "on a miss"), DEdge("fd", "origin"),
            ),
            aspect = 1.0f,
        ),
        B.compare(
            "Front Door, or a CDN profile?",
            B.card("Front Door", "You want one global entry point.", "Routing and failover between origins", "Web application firewall", "Caching as well"),
            B.card("CDN profile", "You only want files served nearer.", "Simple caching rules", "Cheap at volume", "No routing or firewall"),
        ),
        B.commands(
            "Profile, endpoint and purge",
            B.cmd(
                "az afd profile create -g tt-rg --profile-name tt-fd --sku Standard_AzureFrontDoor",
                "Before any route or origin exists.",
                "The profile is the global resource; Standard includes caching and rules, while Premium adds the managed firewall rule sets.",
            ),
            B.cmd(
                "az afd origin-group create -g tt-rg --profile-name tt-fd --origin-group-name app --probe-path /healthz --probe-protocol Https",
                "When the traffic should fail over between origins.",
                "The health probe is what makes failover automatic, so an origin group with no probe path is a single point of failure with extra steps.",
            ),
            B.cmd(
                "az afd endpoint purge -g tt-rg --profile-name tt-fd --endpoint-name tt --content-paths \"/*\"",
                "When something wrong has been cached.",
                "Clears the edge immediately rather than waiting for the time to live, which is the only fast fix once a bad file is out.",
            ),
        ),
        B.mistakes(
            "Caching a response that varies per user. Vary the cache key, or do not cache it.",
            "Expecting a code change to appear at the edge before the cache expires. Purge it.",
            "Setting a long time to live on something that changes hourly.",
        ),
        B.recap(
            "The origin's Cache-Control is what the edge obeys.",
            "Purge is the only immediate fix.",
            "Front Door routes and protects; a CDN profile only caches.",
        ),
    )

    private val identity = notes(
        "az-identity",
        """
            The Microsoft identity platform: an app registration describes your application, a scope or app role
            describes what it may do, and a token is what proves it at the time of the call.
        """,
        B.glossary(
            "App registration" to "The application's identity in a tenant: its client id, its redirect URIs and the permissions it asks for.",
            "Service principal" to "The local instance of that application in a tenant, which is what role assignments actually attach to.",
            "Delegated permission" to "Acting as the signed-in user, limited to what that user may do.",
            "Application permission" to "Acting as itself, with no user present, which needs admin consent.",
            "Consent" to "Someone agreeing to the permissions. A user for their own data; an administrator for the tenant's.",
        ),
        B.diagram(
            caption = "The app asks Entra for a token with the scopes it needs, then calls the API with it.",
            nodes = listOf(
                DNode("app", "Your app", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "client id", hue = DHue.COMPUTE),
                DNode("entra", "Microsoft Entra", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "checks consent", hue = DHue.CHOICE),
                DNode("tok", "Access token", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "scopes and roles", hue = DHue.FLOW),
                DNode("api", "Protected API", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "validates it", hue = DHue.COMPUTE),
                DNode("data", "The data", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "only what is allowed", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("app", "entra", label = "sign in"), DEdge("entra", "tok"),
                DEdge("tok", "api", label = "bearer"), DEdge("api", "data"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("Situation", "Flow", "Why"),
            listOf("Web app with a signed-in user", "Authorization code with PKCE", "The user is present and consent is theirs"),
            listOf("Daemon or background job", "Client credentials", "No user, so application permissions and admin consent"),
            listOf("Service calling another service as the user", "On-behalf-of", "Carries the user's identity forward"),
            listOf("Azure resource calling Azure", "Managed identity", "Nothing to store and nothing to rotate"),
        ),
        B.commands(
            "Registration, secret and role",
            B.cmd(
                "az ad app create --display-name tt-api --sign-in-audience AzureADMyOrg",
                "Creating the application's identity.",
                "The audience decides who can sign in at all, and widening it later is a change with security consequences rather than a setting tweak.",
            ),
            B.cmd(
                "az ad sp create --id <app-id>",
                "After creating the registration, before any role assignment.",
                "Roles attach to the service principal rather than the application object, which is why an assignment fails on a registration that has none.",
            ),
            B.cmd(
                "az ad app credential reset --id <app-id> --years 1",
                "Only when a certificate or a managed identity is not possible.",
                "Creates a client secret with an expiry. A secret that never expires is the finding every review raises, so the expiry is the point.",
            ),
            B.cmd(
                "az role assignment create --assignee <principal-id> --role \"Storage Blob Data Reader\" --scope <resource-id>",
                "Giving the identity access to one resource.",
                "Scoping to the resource rather than the subscription is what keeps a compromised application from reaching everything.",
            ),
        ),
        B.mistakes(
            "Asking for application permissions where delegated would do, which needs admin consent and grants far more.",
            "Assigning a role to the app registration instead of its service principal.",
            "Shipping a client secret where a managed identity is available.",
        ),
        B.recap(
            "Registration describes it, service principal is what gets roles.",
            "Delegated for a user present, application for none.",
            "Managed identity whenever the caller is an Azure resource.",
        ),
    )

    private val msal = notes(
        "az-msal",
        """
            MSAL acquires tokens: silently from its cache when it can, interactively when it must, and it is the
            library's job to cache and refresh rather than yours.
        """,
        B.glossary(
            "Public client" to "An app that cannot keep a secret: a phone app, a desktop app, a single-page app.",
            "Confidential client" to "An app that can: a web app or a service with a secret or a certificate.",
            "AcquireTokenSilent" to "Get a token from the cache, refreshing it if needed. The call your code makes first, every time.",
            "Interactive fallback" to "Prompting the user when the silent call fails, which is the only correct response to that failure.",
            "PKCE" to "A proof the same client that started the sign-in is finishing it, required for public clients.",
        ),
        B.diagram(
            caption = "Silent first, interactive only when the silent call cannot do it, then call the API.",
            nodes = listOf(
                DNode("silent", "Silent call", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "from the cache", hue = DHue.COMPUTE),
                DNode("fail", "No token?", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "it may fail", hue = DHue.CHOICE),
                DNode("inter", "Interactive", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "prompt the user", hue = DHue.CHOICE),
                DNode("call", "Call the API", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "bearer token", hue = DHue.FLOW),
            ),
            edges = listOf(
                DEdge("silent", "fail"), DEdge("fail", "inter", label = "yes"),
                DEdge("fail", "call", label = "no"), DEdge("inter", "call"),
            ),
            aspect = 1.0f,
        ),
        B.table(
            listOf("App", "Client type", "Credential"),
            listOf("Android or iOS app", "Public", "None; PKCE and the system browser"),
            listOf("Single-page app", "Public", "None; authorization code with PKCE"),
            listOf("Web app on a server", "Confidential", "Secret or certificate"),
            listOf("Daemon service", "Confidential", "Certificate, or a managed identity"),
        ),
        B.commands(
            "Packages and the registration it needs",
            B.cmd(
                "dotnet add package Microsoft.Identity.Client",
                "Before any token acquisition in a dot NET app.",
                "MSAL is the supported library, and the exam treats hand-rolled token requests against the endpoints as the wrong answer.",
            ),
            B.cmd(
                "az ad app update --id <app-id> --public-client-redirect-uris http://localhost",
                "When a desktop or mobile client cannot complete sign-in.",
                "A missing or mistyped redirect URI is the usual cause, and it has to be registered as a public-client one rather than a web one.",
            ),
            B.cmd(
                "az ad app permission add --id <app-id> --api 00000003-0000-0000-c000-000000000000 --api-permissions <scope-id>=Scope",
                "When the app needs to call Microsoft Graph.",
                "Adds the delegated permission to the registration, which still has to be consented to before a token will carry it.",
            ),
        ),
        B.mistakes(
            "Caching tokens yourself. MSAL does it, including the refresh, and a second cache goes stale.",
            "Keeping a secret in a mobile or single-page app. They are public clients and cannot hold one.",
            "Treating a failed silent call as an error rather than as the signal to prompt.",
        ),
        B.recap(
            "Silent first, interactive on failure.",
            "Public clients hold no secret; use PKCE.",
            "Let the library cache and refresh.",
        ),
    )

    private val graph = notes(
        "az-graph",
        """
            Microsoft Graph is one endpoint for Microsoft 365 data: users, groups, mail and files, with
            permissions per resource, paging on collections, and a consistency header for queries.
        """,
        B.glossary(
            "Graph endpoint" to "One base address, graph.microsoft.com, with a version segment, behind which every service sits.",
            "Permission scope" to "What the token allows on Graph, as User.Read or Mail.Send, delegated or application.",
            "Paging" to "A collection comes back a page at a time with a next link, and code that ignores it silently reads only the first page.",
            "Delta query" to "Asking only what changed since last time, which is how a sync avoids reading everything.",
            "ConsistencyLevel eventual" to "The header that enables counting and advanced filtering on directory objects.",
        ),
        B.diagram(
            caption = "One token, one endpoint, and a collection that arrives a page at a time.",
            nodes = listOf(
                DNode("app", "Your app", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "with a Graph token", hue = DHue.COMPUTE),
                DNode("graph", "Microsoft Graph", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "one endpoint", hue = DHue.FLOW),
                DNode("page", "First page", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "and a next link", hue = DHue.CHOICE),
                DNode("all", "Everything", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "follow every link", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(DEdge("app", "graph"), DEdge("graph", "page"), DEdge("page", "all", label = "follow next")),
            aspect = 1.0f,
        ),
        B.table(
            listOf("Want", "Call", "Permission"),
            listOf("The signed-in user", "/me", "User.Read, delegated"),
            listOf("Everyone in the tenant", "/users", "User.Read.All"),
            listOf("Send mail as the user", "/me/sendMail", "Mail.Send, delegated"),
            listOf("Only what changed", "/users/delta", "The same as the full read"),
        ),
        B.commands(
            "Permissions and a quick call",
            B.cmd(
                "az ad app permission add --id <app-id> --api 00000003-0000-0000-c000-000000000000 --api-permissions <scope-id>=Scope",
                "Before the app can request a Graph scope.",
                "That API id is Microsoft Graph's, and a permission the registration does not declare will simply not appear in any token.",
            ),
            B.cmd(
                "az ad app permission admin-consent --id <app-id>",
                "When the permission is an application one, or tenant-wide.",
                "Application permissions have no user to consent, so without this step every call comes back forbidden with a valid token.",
            ),
            B.cmd(
                "az rest --method get --url https://graph.microsoft.com/v1.0/me",
                "When checking what a token can actually see.",
                "Calls Graph with your own signed-in context, which is the quickest way to separate a permissions problem from a code problem.",
            ),
        ),
        B.mistakes(
            "Reading the first page and reporting a total. Follow the next link.",
            "Using the beta version in production because a property is only there.",
            "Filtering or counting directory objects without the eventual consistency header.",
        ),
        B.recap(
            "One endpoint, permissions per resource.",
            "Always page; use delta for a sync.",
            "Application permissions need admin consent.",
        ),
    )

    private val keyVault = notes(
        "az-keyvault",
        """
            Key Vault with a managed identity removes the last credential from your code: the app proves who it
            is to Entra, the vault checks a role, and no secret is stored anywhere you deploy.
        """,
        B.glossary(
            "Secret, key, certificate" to "A string, a cryptographic key that never leaves the vault, and a certificate with its private key.",
            "System-assigned identity" to "An identity tied to one resource's lifetime. Deleted with it, and the simplest choice.",
            "User-assigned identity" to "A standalone identity several resources can share, which survives any one of them.",
            "RBAC versus access policy" to "Two permission models on a vault. RBAC is current and the exam's answer; access policies are the older list.",
            "Purge protection" to "Stops a soft-deleted secret being destroyed before its retention ends, even by an administrator.",
        ),
        B.diagram(
            caption = "No credential in the deployment: the identity proves itself and the role decides.",
            nodes = listOf(
                DNode("app", "Web app", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "system identity", hue = DHue.COMPUTE),
                DNode("entra", "Entra", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "issues a token", hue = DHue.CHOICE),
                DNode("role", "Role assignment", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "Secrets User", hue = DHue.CHOICE),
                DNode("kv", "Key Vault", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "the secret", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("use", "Used, then cached", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "with an expiry"),
            ),
            edges = listOf(
                DEdge("app", "entra"), DEdge("entra", "role"), DEdge("role", "kv"), DEdge("kv", "use"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("Where the secret is", "Verdict", "Why"),
            listOf("In source control", "Wrong", "It is in every clone and build log forever"),
            listOf("In an app setting, plain", "Wrong", "Anyone who can read the app can read it"),
            listOf("In the vault, read with a secret", "Half right", "You still have a credential to protect"),
            listOf("In the vault, read by identity", "Right", "There is nothing left to leak"),
        ),
        B.commands(
            "Vault, identity, role, secret",
            B.cmd(
                "az keyvault create -g tt-rg -n tt-kv --enable-rbac-authorization true --enable-purge-protection true",
                "Creating a vault for anything that matters.",
                "RBAC is the current permission model, and purge protection cannot always be added later, so both belong at creation time.",
            ),
            B.cmd(
                "az webapp identity assign -g tt-rg -n tt-web --query principalId -o tsv",
                "Before the app can be given any role.",
                "Prints the principal id the role assignment needs, which is the value people go looking for in the portal instead.",
            ),
            B.cmd(
                "az role assignment create --assignee <principal-id> --role \"Key Vault Secrets User\" --scope <vault-id>",
                "Giving the app read access and nothing more.",
                "Secrets User can read a value but not list, set or delete it, which is the least privilege an application actually needs.",
            ),
            B.cmd(
                "az keyvault secret set --vault-name tt-kv -n db-password --value <value>",
                "When rotating a credential.",
                "Each set adds a version while the previous one keeps working, which is what makes a rotation possible without a deployment.",
            ),
        ),
        B.mistakes(
            "Fetching the secret on every request. The vault is rate-limited; cache it with an expiry.",
            "Using an access policy because an older guide did.",
            "A user-assigned identity where system-assigned would do, leaving an orphan identity behind.",
        ),
        B.recap(
            "Identity in, secret out, nothing deployed.",
            "RBAC and purge protection at creation.",
            "Cache the value; rotate by adding a version.",
        ),
    )

    private val appConfig = notes(
        "az-appconfig",
        """
            App Configuration centralises settings and feature flags: labels keep environments apart, a sentinel
            key makes a refresh atomic, and secrets stay in Key Vault behind a reference.
        """,
        B.glossary(
            "Key-value" to "One setting, usually namespaced with a colon, as TwoTracks:PageSize.",
            "Label" to "A variant of the same key, which is how one store holds development, staging and production values.",
            "Feature flag" to "A setting read as on or off, optionally for a percentage of users, so a feature ships dark.",
            "Sentinel key" to "One key the app watches, so a batch of changes is picked up once, when it is complete.",
            "Key Vault reference" to "A key-value whose content is a vault secret's URI, resolved by the client with its own identity.",
        ),
        B.diagram(
            caption = "The app reads by label, watches one sentinel key, and resolves secrets from the vault.",
            nodes = listOf(
                DNode("store", "App Configuration", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "keys with labels", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("sent", "Sentinel key", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "the only one polled", hue = DHue.CHOICE),
                DNode("app", "Your app", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "refreshes in place", hue = DHue.COMPUTE),
                DNode("kv", "Key Vault", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "the secret values", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("store", "sent"), DEdge("sent", "app", label = "changed"),
                DEdge("kv", "app", label = "reference", dashed = true),
            ),
            aspect = 1.0f,
        ),
        B.compare(
            "App Configuration, or Key Vault?",
            B.card("App Configuration", "It is a setting, and may change while running.", "Labels per environment", "Feature flags", "Refresh with no restart"),
            B.card("Key Vault", "It is a secret.", "Audit log on every read", "Versions and rotation", "Soft delete and purge protection"),
        ),
        B.commands(
            "Values, flags and references",
            B.cmd(
                "az appconfig create -g tt-rg -n tt-config -l westeurope --sku standard",
                "Before any key exists.",
                "Standard is the tier with labels, snapshots and a usable request quota; the free tier runs out quickly under a real refresh interval.",
            ),
            B.cmd(
                "az appconfig kv set -n tt-config --key TwoTracks:PageSize --value 50 --label production",
                "Changing a setting for one environment only.",
                "Without a label the same key means the same thing everywhere, which is how a staging value reaches production.",
            ),
            B.cmd(
                "az appconfig kv set -n tt-config --key Sentinel --value 7 --label production",
                "Last, after a batch of related changes.",
                "Bumping the watched key at the end is what makes a group of changes take effect together instead of half-applied.",
            ),
            B.cmd(
                "az appconfig feature set -n tt-config --feature dark-mode --label production",
                "Shipping a feature dark and turning it on later.",
                "A flag separates deploying from releasing, so the code can go out on Tuesday and the feature appear on Thursday with no new build.",
            ),
        ),
        B.mistakes(
            "Putting a secret in the store rather than a reference to one.",
            "Polling every key instead of a sentinel, which applies half a change.",
            "Forgetting the label.",
        ),
        B.recap(
            "Settings and flags here; secrets in the vault by reference.",
            "Label per environment, sentinel for the refresh.",
            "A flag is how deploying stops meaning releasing.",
        ),
    )

    private val blobBasics = notes(
        "az-blob-basics",
        """
            Blob Storage holds files in containers inside an account: the access tier decides what a read costs,
            and a lifecycle rule moves or deletes them without anyone remembering to.
        """,
        B.glossary(
            "Account, container, blob" to "The three levels. The account is the namespace and the billing boundary; a container is a folder one level deep; a blob is the file.",
            "Block blob" to "The ordinary kind, for files you upload and read. Append blobs are for logs, page blobs for disks.",
            "Access tier" to "Hot, Cool, Cold or Archive. Each is cheaper to store and dearer to read than the one before.",
            "Rehydration" to "Bringing an archived blob back, which takes hours and cannot be hurried past its priority.",
            "Lifecycle rule" to "A policy that moves or deletes blobs on age, so cost control is not a human habit.",
        ),
        B.diagram(
            caption = "A blob's life: written hot, cooled as it ages, archived, then deleted by policy.",
            nodes = listOf(
                DNode("hot", "Hot", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "read often", hue = DHue.DATA),
                DNode("cool", "Cool", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "after 30 days", hue = DHue.DATA),
                DNode("arch", "Archive", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "after a year", hue = DHue.DATA),
                DNode("rule", "Lifecycle rule", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "does all of this", hue = DHue.CHOICE),
                DNode("del", "Deleted", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "on schedule"),
            ),
            edges = listOf(
                DEdge("hot", "cool"), DEdge("cool", "arch"), DEdge("rule", "arch", dashed = true), DEdge("arch", "del"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("Tier", "Storage cost", "Read cost", "Minimum stay"),
            listOf("Hot", "Highest", "Lowest", "None"),
            listOf("Cool", "Lower", "Higher", "30 days"),
            listOf("Cold", "Lower still", "Higher still", "90 days"),
            listOf("Archive", "Lowest", "Rehydrate, hours", "180 days"),
        ),
        B.commands(
            "Account, container, tier and policy",
            B.cmd(
                "az storage account create -g tt-rg -n ttstore --sku Standard_LRS --kind StorageV2 --access-tier Hot",
                "Creating the account.",
                "StorageV2 is the kind that has tiers and lifecycle at all, and the redundancy chosen here cannot be changed to every other option later.",
            ),
            B.cmd(
                "az storage container create --account-name ttstore -n uploads --auth-mode login",
                "Creating a container without using an account key.",
                "The login auth mode uses your own identity, which is how the course avoids account keys from the very first command.",
            ),
            B.cmd(
                "az storage blob set-tier --account-name ttstore -c uploads -n old.zip --tier Archive",
                "When one specific blob should stop costing hot-tier money.",
                "Archive is dramatically cheaper to keep and takes hours to read back, so it suits things you must keep and expect never to want quickly.",
            ),
            B.cmd(
                "az storage account management-policy create --account-name ttstore -g tt-rg --policy @lifecycle.json",
                "Instead of tiering blobs by hand.",
                "A policy applies to everything matching a prefix forever, which is the difference between cost control and a reminder nobody acts on.",
            ),
        ),
        B.mistakes(
            "Archiving something a user might open this week. Rehydration is hours.",
            "Moving a blob to Cool a day after writing it and paying the early-deletion charge.",
            "Treating a container as a tree. The slashes in a name are part of the name.",
        ),
        B.recap(
            "Account, container, blob — and the tier is per blob.",
            "Lifecycle rules, not human habits.",
            "Archive is for keeping, not for reading.",
        ),
    )

    private val blobSdk = notes(
        "az-blob-sdk",
        """
            In code: a client per container, metadata and properties on the blob itself, a shared access
            signature when someone else needs temporary access, and a lease when only one writer may win.
        """,
        B.glossary(
            "Metadata" to "Your own name-value pairs on a blob, read and written separately from the content.",
            "Shared access signature" to "A signed url granting limited access for a limited time, without handing over a key.",
            "User delegation SAS" to "A signature signed with an Entra identity rather than the account key. The preferred kind.",
            "Stored access policy" to "A named policy on the container a signature refers to, which is the only way to revoke one before it expires.",
            "Lease" to "An exclusive lock on a blob for a period, which is how two writers are stopped from both winning.",
        ),
        B.diagram(
            caption = "A signed url issued for one blob, for a short time, revocable through its stored policy.",
            nodes = listOf(
                DNode("app", "Your app", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "has the identity", hue = DHue.COMPUTE),
                DNode("sas", "User delegation SAS", 0.5f, 0.37f, w = 0.66f, h = 0.16f, sub = "read, 15 minutes", hue = DHue.CHOICE),
                DNode("client", "The caller", 0.5f, 0.63f, w = 0.66f, h = 0.16f, sub = "no key, no account", hue = DHue.NEUTRAL),
                DNode("blob", "One blob", 0.5f, 0.89f, w = 0.66f, h = 0.16f, sub = "and nothing else", kind = NodeKind.CYLINDER, hue = DHue.DATA),
            ),
            edges = listOf(
                DEdge("app", "sas", label = "issues"), DEdge("sas", "client"), DEdge("client", "blob", label = "reads"),
            ),
            aspect = 1.0f,
        ),
        B.table(
            listOf("Need", "Use", "Not"),
            listOf("Let a user download one file", "User delegation SAS, short expiry", "The account key"),
            listOf("Revoke access early", "A stored access policy", "Waiting for the expiry"),
            listOf("Only one writer wins", "A lease on the blob", "Checking then writing"),
            listOf("Tag a blob for later search", "Blob index tags", "Metadata, which is not searchable"),
        ),
        B.commands(
            "Signatures, leases and metadata",
            B.cmd(
                "az storage blob generate-sas --account-name ttstore -c uploads -n report.pdf --permissions r --expiry 2026-01-01T00:00Z --auth-mode login --as-user",
                "When one person needs one file for a short while.",
                "With --as-user this is a user delegation signature, signed by an identity rather than the account key, so it can be revoked with the identity.",
            ),
            B.cmd(
                "az storage container policy create --account-name ttstore -c uploads -n readonly --permissions r --expiry 2026-01-01T00:00Z",
                "When a signature may need revoking before it expires.",
                "A signature that refers to a stored policy can be killed by changing or deleting the policy, which is otherwise impossible.",
            ),
            B.cmd(
                "az storage blob lease acquire --account-name ttstore -c uploads -b report.pdf --lease-duration 60",
                "When two processes might write the same blob.",
                "The lease is the lock: without it both writers succeed and the last one silently wins, which is the hardest kind of data loss to notice.",
            ),
            B.cmd(
                "az storage blob metadata update --account-name ttstore -c uploads -n report.pdf --metadata owner=finance",
                "When something about the blob must travel with it.",
                "Metadata is written without rewriting the content, which matters when the blob is large and the annotation is small.",
            ),
        ),
        B.mistakes(
            "Handing out the account key instead of a signature.",
            "A signature with a long expiry and no stored policy, which cannot be revoked at all.",
            "Expecting metadata to be searchable. Index tags are what you can query.",
        ),
        B.recap(
            "User delegation signatures, short expiry, stored policy to revoke.",
            "A lease is the only real lock.",
            "Metadata travels; index tags are searchable.",
        ),
    )

    private val cosmosModel = notes(
        "az-cosmos-model",
        """
            Cosmos DB modelling: pick the API at account creation, pick a partition key that spreads and is
            queried, and pick the consistency level that is cheap enough for what the app actually needs.
        """,
        B.glossary(
            "API choice" to "NoSQL, MongoDB, Cassandra, Gremlin or Table, chosen at account creation and not changeable afterwards.",
            "Logical partition" to "Everything sharing one partition key value, capped at 20 GB.",
            "Physical partition" to "What Cosmos actually spreads logical partitions across, and what request units are divided between.",
            "Hot partition" to "One partition taking most of the traffic, which throttles while the rest idles.",
            "Request unit" to "The currency: every read, write and query costs RUs, and you provision them per second.",
        ),
        B.diagram(
            caption = "A key with many values spreads across partitions; a key with few makes one of them hot.",
            nodes = listOf(
                DNode("key", "Partition key", 0.5f, 0.11f, w = 0.66f, h = 0.16f, sub = "chosen once", hue = DHue.CHOICE),
                DNode("good", "Many values", 0.27f, 0.45f, w = 0.44f, h = 0.16f, sub = "spreads evenly", hue = DHue.DATA),
                DNode("bad", "Few values", 0.73f, 0.45f, w = 0.44f, h = 0.16f, sub = "one gets hot", hue = DHue.DATA),
                DNode("ru", "Throughput", 0.5f, 0.82f, w = 0.66f, h = 0.16f, sub = "RUs per second", hue = DHue.FLOW),
            ),
            edges = listOf(
                DEdge("key", "good"), DEdge("key", "bad"),
                DEdge("good", "ru"), DEdge("bad", "ru", label = "throttled", dashed = true),
            ),
            aspect = 1.0f,
        ),
        B.table(
            listOf("Level", "Guarantee", "Cost"),
            listOf("Strong", "Always the latest write", "Most RUs, single-region reads"),
            listOf("Bounded staleness", "Behind by at most K writes or T seconds", "Between the two"),
            listOf("Session", "Your own writes back", "The default and usual answer"),
            listOf("Consistent prefix", "In order, possibly behind", "Cheap"),
            listOf("Eventual", "Eventually", "Cheapest"),
        ),
        B.commands(
            "Account, container and throughput",
            B.cmd(
                "az cosmosdb create -g tt-rg -n tt-cosmos --locations regionName=westeurope --default-consistency-level Session",
                "Creating the account, which is where the API is fixed.",
                "The API cannot be changed afterwards, so an account created for NoSQL can never serve the MongoDB wire protocol.",
            ),
            B.cmd(
                "az cosmosdb sql container create -g tt-rg -a tt-cosmos -d tt -n items --partition-key-path /tenantId --throughput 400",
                "Creating a container, which fixes the partition key.",
                "This one argument decides cost and scale for the life of the container, and changing it later means copying every item.",
            ),
            B.cmd(
                "az cosmosdb sql database throughput migrate -g tt-rg -a tt-cosmos -n tt --throughput-type autoscale",
                "When load is spiky rather than steady.",
                "Autoscale bills for the peak reached instead of a flat provision, which is cheaper than provisioning for the busiest hour of the week.",
            ),
            B.cmd(
                "az cosmosdb failover-priority-change -g tt-rg -n tt-cosmos --failover-policies westeurope=0 northeurope=1",
                "After adding a second region.",
                "The priority list is what decides where a failover goes, and leaving it at the default means the order is not the one you assumed.",
            ),
        ),
        B.mistakes(
            "Choosing a partition key with few values, such as a country or a status.",
            "Raising throughput to fix a hot partition. The hot one cannot go faster.",
            "Picking strong consistency by default and losing multi-region reads.",
        ),
        B.recap(
            "API at the account, partition key at the container, both permanent.",
            "A good key has many values and appears in your queries.",
            "Session unless something genuinely needs more.",
        ),
    )

    private val cosmosSdk = notes(
        "az-cosmos-sdk",
        """
            Cosmos in code: one client reused for the process, a point read when you know the id and key,
            queries scoped to a partition, and the change feed to start work when data changes.
        """,
        B.glossary(
            "Point read" to "Reading one item by id and partition key. About one request unit, and the cheapest operation.",
            "Cross-partition query" to "A query without a partition key, which asks every physical partition.",
            "Stored procedure" to "JavaScript running inside one logical partition, which is the only place a transaction across items exists.",
            "Change feed" to "Every create and update in order, which other work reads to react. No deletes.",
            "429 Too Many Requests" to "Throttling: you asked for more RUs than were provisioned. The SDK retries it for you.",
        ),
        B.diagram(
            caption = "One client for the process, a point read for a known item, and the change feed for reactions.",
            nodes = listOf(
                DNode("client", "One client", 0.5f, 0.10f, w = 0.66f, h = 0.15f, sub = "reused, long-lived", hue = DHue.COMPUTE),
                DNode("read", "Point read", 0.5f, 0.32f, w = 0.66f, h = 0.15f, sub = "id and key", hue = DHue.CHOICE),
                DNode("item", "The item", 0.5f, 0.54f, w = 0.66f, h = 0.15f, sub = "one partition", kind = NodeKind.CYLINDER, hue = DHue.DATA),
                DNode("feed", "Change feed", 0.5f, 0.76f, w = 0.66f, h = 0.15f, sub = "creates and updates", hue = DHue.FLOW),
                DNode("work", "Later work", 0.5f, 0.94f, w = 0.66f, h = 0.12f, sub = "idempotent", hue = DHue.COMPUTE),
            ),
            edges = listOf(
                DEdge("client", "read"), DEdge("read", "item"), DEdge("item", "feed"), DEdge("feed", "work"),
            ),
            aspect = 1.1f,
        ),
        B.table(
            listOf("You know", "Use", "Cost"),
            listOf("id and partition key", "ReadItem", "About one RU"),
            listOf("Partition key only", "Query with the key set", "That partition's work"),
            listOf("Neither", "Cross-partition query", "Work on every partition"),
            listOf("Several items, one partition, atomically", "Stored procedure or transactional batch", "The items' own cost"),
        ),
        B.commands(
            "What you run around the code",
            B.cmd(
                "az cosmosdb keys list -g tt-rg -n tt-cosmos --type connection-strings",
                "Only when a managed identity is not an option.",
                "Prints the connection strings, and is worth knowing next to the better answer of granting the app's identity a data-plane role instead.",
            ),
            B.cmd(
                "az cosmosdb sql role assignment create -g tt-rg -a tt-cosmos --role-definition-name \"Cosmos DB Built-in Data Contributor\" --principal-id <principal-id> --scope \"/\"",
                "When the app should read and write without a key.",
                "Data-plane roles are separate from control-plane RBAC, which is why an owner on the account still cannot read an item.",
            ),
            B.cmd(
                "az cosmosdb sql container create -g tt-rg -a tt-cosmos -d tt -n leases --partition-key-path /id --throughput 400",
                "Before a change feed processor starts.",
                "The processor records its position in a lease container, and without one it cannot resume or share work between instances.",
            ),
        ),
        B.mistakes(
            "Creating a client per request. It caches connections and routing, and rebuilding it is most of the latency people blame on Cosmos.",
            "Querying by id with SELECT when a point read would do.",
            "Assuming the change feed carries deletes. Soft-delete with a flag.",
        ),
        B.recap(
            "One client, reused; point reads when you can.",
            "429 means throttled, and the SDK already retried.",
            "Leases for the change feed, idempotent handlers.",
        ),
    )

    val all: List<TopicNotes> = listOf(
        appInsights, availability, redis, cdn,
        identity, msal, graph, keyVault, appConfig,
        blobBasics, blobSdk, cosmosModel, cosmosSdk,
    )
}
