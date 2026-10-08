package com.shahaabapps.core.content

/** AZ-204 module 4, Monitor and optimise: Application Insights, availability and alerts, Redis, CDN, plus the module test. */
internal object Az204Monitor {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val appInsights = Topic(
        id = "az-appinsights", title = "Application Insights: seeing what your code does in production", minutes = 28,
        references = listOf(
            B.ref("Application Insights overview", LEARN + "azure/azure-monitor/app/app-insights-overview"),
            B.ref("Application Insights API for custom events and metrics", LEARN + "azure/azure-monitor/app/api-custom-events-metrics"),
            B.ref("Sampling in Application Insights", LEARN + "azure/azure-monitor/app/sampling"),
            B.ref("Kusto Query Language overview", LEARN + "azure/data-explorer/kusto/query/"),
            B.ref("Learn module: Monitor app performance", LEARN + "training/modules/monitor-app-performance/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-appinsights-w1", "az-appinsights",
                "One web request made four database calls and threw an exception. What do you think ties those six telemetry items together?",
                listOf("A shared operation id", "The timestamp", "The role name of the app"), 0,
                "Correlation has a specific field behind it, and it is the one the traces are built on.",
            ),
            B.plain("""
                Application Insights is the part of Azure Monitor that watches your application: every request,
                every dependency call, every exception, plus anything you choose to log. It stores all of it in a
                Log Analytics workspace that you query with KQL.
            """),
            B.diagram(
                caption = "The SDK or agent sends telemetry to a workspace-based Application Insights resource. Dashboards, alerts and queries read from the workspace.",
                nodes = listOf(
                    DNode("app", "Two Tracks API", 0.15f, 0.5f, w = 0.24f, h = 0.26f, tone = 1, sub = "SDK or auto-instrumentation"),
                    DNode("ai", "Application Insights", 0.47f, 0.5f, w = 0.3f, h = 0.26f, kind = NodeKind.CLOUD, sub = "connection string"),
                    DNode("la", "Log Analytics workspace", 0.8f, 0.22f, w = 0.3f, h = 0.18f, kind = NodeKind.CYLINDER, tone = 2),
                    DNode("alert", "alerts, workbooks, Live Metrics", 0.8f, 0.78f, w = 0.3f, h = 0.18f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("app", "ai", label = "telemetry"), DEdge("ai", "la"), DEdge("la", "alert", label = "KQL")),
                aspect = 0.5f,
            ),
            B.h("What gets collected"),
            B.table(
                listOf("Telemetry", "Table", "Example"),
                listOf("Request", "requests", "GET /api/logs took 120 ms, 200"),
                listOf("Dependency", "dependencies", "Cosmos query, 3 RU, 8 ms"),
                listOf("Exception", "exceptions", "NullReferenceException with stack"),
                listOf("Trace", "traces", "ILogger lines"),
                listOf("Custom event", "customEvents", "LessonFinished with topic id"),
                listOf("Custom metric", "customMetrics", "DeckSize = 42"),
                listOf("Page view", "pageViews", "From the JavaScript SDK"),
                listOf("Availability", "availabilityResults", "Ping from 5 regions"),
            ),
            B.h("Two ways in"),
            B.compare("Instrumentation",
                B.card("Auto-instrumentation", "App Service, Functions, AKS with no code change.", "Toggle in the portal", "Requests, dependencies, exceptions", "No custom events"),
                B.card("SDK (Azure Monitor OpenTelemetry)", "You want custom telemetry and control.", "Add a package and the connection string", "TrackEvent, TrackMetric, custom properties", "Sampling and processors configurable"),
            ),
            B.code("csharp", """
                builder.Services.AddApplicationInsightsTelemetry(); // reads APPLICATIONINSIGHTS_CONNECTION_STRING

                public class LessonService(TelemetryClient telemetry)
                {
                    public void Finished(string topicId, int minutes)
                    {
                        telemetry.TrackEvent("LessonFinished", new Dictionary<string, string> { ["topic"] = topicId }, new Dictionary<string, double> { ["minutes"] = minutes });
                        telemetry.GetMetric("DeckSize").TrackValue(42);
                    }
                }
            """),
            B.remember("Use the connection string, not the old instrumentation key. Custom events take string properties (for filtering) and numeric measurements (for charts)."),
            B.check("az-appinsights-k1", "az-appinsights", "You need to chart how many lessons were finished per hour, split by topic. What do you call?",
                listOf("TrackTrace", "TrackEvent with a topic property", "TrackException"), 1, "Custom events with properties are made for counting business actions and splitting by a dimension."),
            B.h("Querying with KQL"),
            B.code("kusto", """
                requests
                | where timestamp > ago(1h) and success == false
                | summarize failures = count() by name, resultCode
                | order by failures desc

                customEvents
                | where name == "LessonFinished"
                | extend topic = tostring(customDimensions.topic)
                | summarize count() by topic, bin(timestamp, 1h)
                | render timechart
            """),
            B.table(
                listOf("Operator", "Does"),
                listOf("where", "Filter rows"),
                listOf("summarize … by", "Group and aggregate (count, avg, percentile)"),
                listOf("extend / project", "Add a column / pick columns"),
                listOf("join", "Combine tables on a key, for example operation_Id"),
                listOf("render", "Chart the result"),
            ),
            B.check(
                "az-appinsights-k2", "az-appinsights",
                "Which field ties a request to the dependency calls it made?",
                listOf("operation_Id", "session_Id", "cloud_RoleName"), 0,
                "Every telemetry item from one request shares its operation_Id, which is what makes an end-to-end trace possible.",
                whyWrong = mapOf(
                    1 to "session_Id groups one user's whole visit, which is far broader than a single request.",
                    2 to "cloud_RoleName says which component sent the item, not which request it belonged to.",
                ),
            ),
            B.h("Correlation, sampling and Live Metrics"),
            B.bullets(
                "Every request and the dependencies it caused share an operation_Id. The Application Map and end-to-end transaction view are built on it.",
                "Adaptive sampling (default in the .NET SDK) drops a fraction of items under load but keeps related items together and corrects counts.",
                "Fixed-rate sampling keeps a set percentage; ingestion sampling happens in the portal after the data arrives.",
                "Live Metrics shows requests, failures and CPU with one-second latency while you deploy.",
            ),
            B.tip("\"Reduce ingestion cost while keeping counts accurate\" is sampling. \"Follow a slow request through three services\" is operation_Id and the end-to-end transaction view. \"Watch a deployment live\" is Live Metrics."),
            B.worked("""
                Worked example. The Two Tracks API saw a spike in 500s one evening. In Failures, the top exception was a
                Cosmos 429 inside POST /api/logs. The end-to-end view showed the dependency call taking 2 s before the
                retry gave up. A KQL summarize by hour matched the nightly export job. The fix was autoscale on the
                container and a TrackEvent on each export so the two can be correlated next time.
            """),
            B.steps("Add Application Insights to a web app",
                "az monitor app-insights component create --app tt-ai --location westeurope --resource-group tt-rg --workspace <workspace-id>" to "A workspace-based resource.",
                "Copy the connectionString from the output into APPLICATIONINSIGHTS_CONNECTION_STRING." to "The SDK reads it at start-up.",
                "Add the Microsoft.ApplicationInsights.AspNetCore package and AddApplicationInsightsTelemetry()." to "Requests, dependencies and exceptions flow automatically.",
                "Open Live Metrics while you deploy." to "Confirm traffic and zero failures.",
            ),
            B.mistakes(
                "Logging personal data in custom properties. Telemetry is kept for 90 days by default and anyone with reader access can query it.",
                "Turning off sampling under heavy load and paying for every item.",
                "Using the instrumentation key alone; new regions require the connection string.",
                "Querying the wrong table: exceptions you logged through ILogger are in traces unless you pass the exception object.",
            ),
            B.example("Two Tracks: auto-instrumentation on App Service plus TrackEvent for LessonFinished and QuizPassed; a workbook of lessons per day per partner."),
            B.check(
                "az-appinsights-k3", "az-appinsights",
                "Telemetry is costing too much, so you turn on adaptive sampling. What happens to your request counts?",
                listOf("They stay roughly right, because sampling corrects the numbers", "They fall in proportion to the sampling rate", "They stop being collected"), 0,
                "Adaptive sampling keeps related items together and scales the counts back up, so the metrics stay usable while the volume drops.",
                whyWrong = mapOf(
                    1 to "That would make sampling useless for metrics. The SDK multiplies the counts back up by the sampling rate.",
                    2 to "Sampling reduces how much is stored. It does not switch collection off.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-m-ai-match", "Pair each KQL operator with what it does.",
                "where" to "Filter rows",
                "summarize … by" to "Group and aggregate",
                "project" to "Pick the columns to keep",
                "join" to "Combine tables on a key such as operation_Id",
            ),
            B.gap(
                "az-m-ai-gap1", "One request crossing several services is stitched together by a shared value.",
                "Traces are correlated by ___.", "operation_Id", "operation_id", "operationId",
            ),
            B.tryIt("Try it in 15 minutes", "Workspace-based Application Insights has a 5 GB free monthly allowance.",
                "Create an Application Insights resource", "Run any sample web app with the connection string", "Make a few requests and one that fails", "Run requests | summarize count() by resultCode in Logs",
            ),
            B.explain(
                "az-appinsights-x1",
                "Explain what Application Insights collects on its own, and how one request is tied to the calls it made.",
                "Requests, dependency calls, exceptions and traces are collected without any code, and TrackEvent and TrackMetric add your own on top. It all lands in a Log Analytics workspace and is queried with KQL. Every item from a single request carries the same operation_Id, which is what makes an end-to-end trace possible. Sampling cuts the volume while keeping related items together and scaling the counts back up, so the metrics stay usable.",
                "Requests, dependencies, exceptions come free" to listOf("request", "dependency", "dependencies", "exception", "trace"),
                "operation_Id correlates one request" to listOf("operation_id", "operation id", "correlate", "correlation", "ties"),
                "It is queried with KQL in Log Analytics" to listOf("kql", "kusto", "log analytics", "workspace", "logs"),
            ),
            B.glossary(
                "Telemetry" to "Any item sent to Application Insights: request, dependency, exception, trace, event, metric.",
                "Connection string" to "The setting that tells the SDK where to send telemetry.",
                "KQL" to "Kusto Query Language, used in Logs.",
                "operation_Id" to "The id shared by all telemetry of one request; enables correlation.",
                "Adaptive sampling" to "Automatic reduction of telemetry volume that keeps related items and corrects counts.",
                "Live Metrics" to "A one-second-latency view of the app's traffic and health.",
            ),
            B.recap(
                "Requests, dependencies, exceptions and traces come free; TrackEvent and TrackMetric add your own.",
                "Everything lands in a Log Analytics workspace; query with KQL and chart with render.",
                "operation_Id correlates; sampling cuts cost; Live Metrics watches deployments.",
            ),
        ),
    )

    private val availability = Topic(
        id = "az-availability", title = "Availability tests, alerts and autoscale rules", minutes = 22,
        references = listOf(
            B.ref("Application Insights availability tests", LEARN + "azure/azure-monitor/app/availability"),
            B.ref("Azure Monitor alerts overview", LEARN + "azure/azure-monitor/alerts/alerts-overview"),
            B.ref("Action groups", LEARN + "azure/azure-monitor/alerts/action-groups"),
            B.ref("Autoscale in Azure Monitor", LEARN + "azure/azure-monitor/autoscale/autoscale-overview"),
            B.ref("Learn module: Monitor app performance (alerts)", LEARN + "training/modules/monitor-app-performance/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-availability-w1", "az-availability",
                "An autoscale rule adds an instance above 70% CPU and removes one below 70%. What do you think happens?",
                listOf("The instance count flaps up and down", "It works as intended", "It scales out but never back in"), 0,
                "Two rules that meet at the same number have a predictable argument.",
            ),
            B.plain("Monitoring is only useful if somebody is told. Availability tests probe the site from outside, alerts turn a condition into a notification, and action groups say who gets it and how."),
            B.diagram(
                caption = "Signal to action: a metric, log query or availability result crosses a threshold, the alert rule fires, the action group notifies and optionally runs automation.",
                nodes = listOf(
                    DNode("sig", "Signal", 0.14f, 0.5f, w = 0.22f, h = 0.3f, tone = 1, sub = "metric, log, availability"),
                    DNode("rule", "Alert rule", 0.42f, 0.5f, w = 0.24f, h = 0.3f, sub = "condition + window"),
                    DNode("ag", "Action group", 0.7f, 0.5f, w = 0.24f, h = 0.3f, tone = 2, sub = "email, SMS, push, voice"),
                    DNode("act", "webhook, Function, Logic App, runbook", 0.9f, 0.9f, w = 0.3f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("sig", "rule"), DEdge("rule", "ag", label = "fires"), DEdge("ag", "act")),
                aspect = 0.45f,
            ),
            B.h("Availability tests"),
            B.table(
                listOf("Test", "What it does", "Use"),
                listOf("Standard test", "HTTP request from up to 16 regions, checks status, content and TLS validity", "The default for any public endpoint"),
                listOf("Custom TrackAvailability", "Your code (often an Azure Function on a timer) reports a result", "Private endpoints or multi-step logic"),
                listOf("URL ping (classic)", "Simple GET", "Retired; migrate to standard"),
            ),
            B.tip("\"The endpoint is only reachable inside a VNet\" rules out standard tests. Run a Function inside the network and call TrackAvailability."),
            B.code("csharp", """
                [Function("CheckApi")]
                public async Task Run([TimerTrigger("0 */5 * * * *")] TimerInfo t)
                {
                    var sw = Stopwatch.StartNew(); var ok = false;
                    try { ok = (await _http.GetAsync("https://tt-api.internal/health")).IsSuccessStatusCode; }
                    finally { _telemetry.TrackAvailability("api-health", DateTimeOffset.UtcNow, sw.Elapsed, "westeurope-vnet", ok); }
                }
            """),
            B.check(
                "az-availability-k2", "az-availability",
                "The endpoint you need to probe sits inside a private network. What do you use?",
                listOf("TrackAvailability, from code running inside that network", "A standard availability test", "A metric alert"), 0,
                "Standard tests run from Microsoft's public regions, so a private endpoint needs your own probe reporting results in.",
                whyWrong = mapOf(
                    1 to "A standard test probes from public regions, and cannot reach a private endpoint at all.",
                    2 to "A metric alert fires on a metric you already have. It probes nothing.",
                ),
            ),
            B.h("Alert rules"),
            B.table(
                listOf("Kind", "Evaluates", "Example"),
                listOf("Metric alert", "A platform or custom metric against a threshold", "Server response time > 2 s for 5 minutes"),
                listOf("Log search alert", "A KQL query on a schedule", "More than 10 exceptions of type X in 15 minutes"),
                listOf("Activity log alert", "Management operations", "Someone deleted a resource"),
                listOf("Smart detection", "Automatic anomaly detection", "Failure rate rose unusually"),
                listOf("Availability alert", "Failed locations in a window", "3 of 5 regions failed"),
            ),
            B.bullets(
                "Severity 0 (critical) to 4 (verbose). Dynamic thresholds learn the normal range instead of a fixed number.",
                "An alert has a state: fired, acknowledged, closed. Auto-resolve closes metric alerts when the condition clears.",
                "Alert processing rules can suppress notifications during maintenance or add action groups at scale.",
            ),
            B.check("az-availability-k1", "az-availability", "Ops must be paged when a specific KQL query returns more than 10 rows in 15 minutes. Which alert type?",
                listOf("Metric alert", "Log search alert", "Activity log alert"), 1, "Only log search alerts evaluate a KQL query on a schedule."),
            B.h("Autoscale rules"),
            B.p("Autoscale lives in Azure Monitor and applies to App Service plans, VM scale sets and more. A profile has a minimum, maximum and default instance count, plus rules that scale out and in on a metric or schedule."),
            B.table(
                listOf("Rule", "Example", "Note"),
                listOf("Scale out", "CPU > 70 percent for 10 minutes, add 1", "Cooldown 5 minutes"),
                listOf("Scale in", "CPU < 30 percent for 10 minutes, remove 1", "Always pair with a scale-out rule"),
                listOf("Schedule", "Weekdays 8 to 18, minimum 3", "Predictable load"),
                listOf("Queue-based", "Service Bus active messages > 500, add 2", "Metric from another resource"),
            ),
            B.remember("Autoscale scales the number of instances (out and in). Scaling up means a bigger tier and is manual. The exam loves \"flapping\": fix it with a cooldown and a gap between the out and in thresholds."),
            B.worked("""
                Worked example. Two Tracks has a standard availability test from five regions every five minutes and an
                alert when three fail. A metric alert on HTTP 5xx > 5 in 5 minutes pages Usaid through the action
                group (push via the Azure mobile app, plus email). Autoscale keeps one instance at night and adds up to
                three when CPU passes 70 percent, with a 10-minute cooldown.
            """),
            B.steps("Create a metric alert from the CLI",
                "az monitor action-group create --name tt-ops --resource-group tt-rg --short-name ttops --action email usaid shahab@example.com" to "Who gets told.",
                "az monitor metrics alert create --name api-5xx --resource-group tt-rg --scopes <webapp-id> --condition \"total Http5xx > 5\" --window-size 5m --evaluation-frequency 1m --action tt-ops --severity 1" to "The rule.",
                "Trigger an error and watch the alert fire, then auto-resolve." to "State goes fired, then resolved.",
            ),
            B.mistakes(
                "Creating only a scale-out rule; the plan never shrinks and the bill grows.",
                "Setting scale-out and scale-in at the same threshold; instances flap.",
                "Using a standard availability test for a private endpoint; it cannot reach it.",
                "Sending alerts to one person's inbox; use an action group so the list can change.",
            ),
            B.example("Two Tracks: five-region standard test, 5xx metric alert with push notification, log alert on QuizPassed dropping to zero for a day, autoscale 1 to 3 instances."),
            B.check(
                "az-availability-k3", "az-availability",
                "An autoscale profile adds an instance above 70% CPU and removes one below 70%. What goes wrong?",
                listOf("It flaps: the two rules fight, so the thresholds need a gap between them", "Nothing; this is the shape to aim for", "It never scales back in"), 0,
                "Scale-out and scale-in thresholds that meet cause repeated opposite actions. A gap and a cooldown are what keep it steady.",
                whyWrong = mapOf(
                    1 to "A matched pair of rules is right. It is the thresholds touching that causes the trouble.",
                    2 to "It scales in perfectly well. That is the problem: it scales in and straight back out.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-m-avail-match", "Pair each rule with what it is for.",
                "Scale out" to "CPU above 70 percent for ten minutes, add an instance",
                "Scale in" to "CPU below 30 percent for ten minutes, remove one",
                "Schedule rule" to "Weekdays 8 to 18, keep a minimum of three",
                "Queue-based rule" to "Service Bus messages above 500, add two",
            ),
            B.gap(
                "az-m-avail-gap1", "A scale-out rule on its own only ever grows the app.",
                "Always pair it with a scale-___ rule.", "in", "In",
            ),
            B.gap(
                "az-m-avail-gap2", "The default availability test hits a public endpoint from many places at once.",
                "A standard test runs from up to ___ regions.", "16", "sixteen",
            ),
            B.tryIt("Try it in 15 minutes", "Alerts cost cents; availability tests are free for a few.",
                "Add a standard availability test to any public URL", "Create an action group with your email", "Create a metric alert on availability results < 100", "Stop the app and wait for the email",
            ),
            B.explain(
                "az-availability-x1",
                "Explain how you would watch an endpoint and be told when it breaks, and what an autoscale rule needs besides a threshold.",
                "A standard availability test probes a public endpoint on a schedule from several regions; an endpoint inside a private network needs your own code reporting with TrackAvailability instead. Alerts come in kinds — metric, log search, activity log, availability — and all of them send to an action group, which is the reusable set of notifications and automations. An autoscale rule needs a scale-in rule to match the scale-out one, a gap between their thresholds and a cooldown, or the two rules fight and the instance count flaps.",
                "Tests probe from regions, or from your own code" to listOf("availability test", "probe", "probes", "trackavailability", "regions"),
                "Alerts send to an action group" to listOf("action group", "alert", "alerts", "notification"),
                "Autoscale needs a gap and a cooldown" to listOf("cooldown", "cool-down", "gap", "threshold", "flap"),
            ),
            B.glossary(
                "Availability test" to "A scheduled probe of your endpoint from several regions.",
                "Action group" to "A reusable set of notification and automation actions.",
                "Metric alert" to "Fires when a metric crosses a threshold over a window.",
                "Log search alert" to "Fires on the result of a scheduled KQL query.",
                "Dynamic threshold" to "A learned normal range instead of a fixed number.",
                "Cooldown" to "The pause after a scale action before the next evaluation.",
            ),
            B.recap(
                "Standard tests probe public endpoints; TrackAvailability covers private ones.",
                "Metric, log search, activity log and availability alerts send to action groups.",
                "Autoscale pairs scale-out and scale-in rules with a cooldown and a threshold gap.",
            ),
        ),
    )

    private val redis = Topic(
        id = "az-redis", title = "Azure Cache for Redis: fast reads and cache patterns", minutes = 24,
        references = listOf(
            B.ref("About Azure Cache for Redis", LEARN + "azure/azure-cache-for-redis/cache-overview"),
            B.ref("Quickstart: Use Azure Cache for Redis in .NET", LEARN + "azure/azure-cache-for-redis/cache-dotnet-core-quickstart"),
            B.ref("Caching guidance (cache-aside)", LEARN + "azure/architecture/best-practices/caching"),
            B.ref("Learn module: Develop for Azure Cache for Redis", LEARN + "training/modules/develop-for-azure-cache-for-redis/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-redis-w1", "az-redis",
                "How many Redis connection objects do you think a web app should create?",
                listOf("One, shared for the life of the app", "One per request", "One per cache key"), 0,
                "The object in question is expensive, and getting this wrong exhausts the cache.",
            ),
            B.plain("""
                Redis is an in-memory key-value store. Reads take under a millisecond, which makes it the place to
                keep anything you compute often and change rarely: a user's plan for today, a session, a
                leaderboard, a rate-limit counter.
            """),
            B.diagram(
                caption = "Cache-aside: check the cache, miss, read the database, write the cache with a TTL, return. Next time it is a hit.",
                nodes = listOf(
                    DNode("api", "API", 0.14f, 0.5f, w = 0.2f, h = 0.26f, tone = 1),
                    DNode("redis", "Redis", 0.5f, 0.22f, w = 0.24f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 2, sub = "plan:usaid, TTL 10 min"),
                    DNode("db", "Cosmos DB", 0.5f, 0.78f, w = 0.24f, h = 0.2f, kind = NodeKind.CYLINDER),
                    DNode("n", "1 GET → miss, 2 read DB, 3 SET with expiry", 0.84f, 0.5f, w = 0.28f, h = 0.3f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("api", "redis", label = "1 GET"), DEdge("api", "db", label = "2 miss"), DEdge("db", "redis", label = "3 SET")),
                aspect = 0.5f,
            ),
            B.h("Tiers"),
            B.table(
                listOf("Tier", "What you get", "Pick when"),
                listOf("Basic", "Single node, no SLA", "Dev and test only"),
                listOf("Standard", "Two nodes, replicated, 99.9 percent SLA", "Production without large scale"),
                listOf("Premium", "Clustering, persistence, VNet, geo-replication, larger sizes", "Need VNet injection or data persistence"),
                listOf("Enterprise", "Redis Enterprise modules (RediSearch, RedisJSON, time series), active geo-replication", "Need modules or multi-region writes"),
                listOf("Enterprise Flash", "Enterprise on NVMe flash for big data at lower cost", "Very large caches"),
            ),
            B.tip("\"Must persist to disk\" or \"inside a VNet\" means Premium at least. \"RediSearch\" or \"active-active across regions\" means Enterprise."),
            B.h("Core commands and the .NET client"),
            B.code("csharp", """
                var mux = await ConnectionMultiplexer.ConnectAsync("tt-redis.redis.cache.windows.net:6380,ssl=True,abortConnect=False,password=...");
                var db = mux.GetDatabase();

                var key = "plan:usaid:2026-10-06";
                var cached = await db.StringGetAsync(key);
                if (cached.HasValue) return JsonSerializer.Deserialize<Plan>(cached!);

                var plan = await _planner.Build("usaid");                       // slow path
                await db.StringSetAsync(key, JsonSerializer.Serialize(plan), TimeSpan.FromMinutes(10));
                return plan;
            """),
            B.table(
                listOf("Command", "StackExchange.Redis", "Use"),
                listOf("GET / SET with EX", "StringGet, StringSet(key, value, expiry)", "Cache a value with a TTL"),
                listOf("INCR / EXPIRE", "StringIncrement, KeyExpire", "Rate limiting, counters"),
                listOf("HSET / HGETALL", "HashSet, HashGetAll", "An object with fields"),
                listOf("ZADD / ZRANGE", "SortedSetAdd, SortedSetRangeByRank", "Leaderboards"),
                listOf("LPUSH / RPOP", "ListLeftPush, ListRightPop", "Simple queues"),
                listOf("DEL", "KeyDelete", "Invalidate on write"),
                listOf("PING", "db.Ping()", "Health check"),
            ),
            B.remember("Create one ConnectionMultiplexer for the whole app and reuse it. Creating one per request exhausts connections and is the classic exam mistake."),
            B.check("az-redis-k1", "az-redis", "A study-streak leaderboard must return the top 10 users quickly and update on every log. Which Redis structure?",
                listOf("A string per user", "A sorted set", "A list"), 1, "Sorted sets keep members ordered by score; ZRANGE gives the top N in O(log n)."),
            B.check(
                "az-redis-k2", "az-redis",
                "How many ConnectionMultiplexer instances should an application create?",
                listOf("One, shared for the lifetime of the app", "One per request", "One per key"), 0,
                "The multiplexer is expensive to build and designed to be shared.",
                whyWrong = mapOf(
                    1 to "Per request is the classic mistake. It is costly to create and will run the cache out of connections.",
                    2 to "Keys are just arguments to commands on the shared connection.",
                ),
            ),
            B.h("Patterns"),
            B.compare("Cache patterns",
                B.card("Cache-aside", "The app reads the cache first and fills it on a miss.", "Simple; the default", "Stale until TTL or invalidation", "Set an expiry on every key"),
                B.card("Write-through", "Writes go to cache and database together.", "Cache always current", "Extra write latency", "Good for read-heavy hot data"),
                B.card("Session store", "Keep user session state out of the web server.", "Scale out freely", "Needs a Redis instance", "ASP.NET Core AddStackExchangeRedisCache"),
            ),
            B.bullets(
                "Eviction policy: volatile-lru by default (evict keys with a TTL first). Set maxmemory-reserved for headroom.",
                "Redis is not a database: assume anything can disappear; always have the slow path.",
                "Use managed identity (Entra ID authentication) instead of access keys where supported.",
            ),
            B.worked("""
                Worked example. Building Usaid's Today page costs about 30 Cosmos RU and 200 ms of planning. With
                cache-aside and a 10-minute TTL, repeated opens cost 1 ms. When he logs a session the API deletes
                plan:usaid:<date> so the next read rebuilds. The streak leaderboard for both partners is a sorted set
                updated by the change-feed function.
            """),
            B.steps("Create a cache and connect",
                "az redis create --name tt-redis --resource-group tt-rg --location westeurope --sku Standard --vm-size c0" to "Standard C0: 250 MB, replicated.",
                "az redis list-keys --name tt-redis --resource-group tt-rg" to "Or enable Entra ID auth and skip keys.",
                "Store the connection string in Key Vault and read it with a Key Vault reference." to "No key in code.",
                "Add StackExchange.Redis and register one ConnectionMultiplexer as a singleton." to "Reuse across requests.",
            ),
            B.mistakes(
                "A new ConnectionMultiplexer per request.",
                "Keys without an expiry: memory fills and eviction picks for you.",
                "Caching per-user data under a shared key.",
                "Choosing Basic for production; there is no replica and no SLA.",
            ),
            B.example("Two Tracks: Standard C0, cache-aside for Today plans with 10-minute TTL, delete-on-write invalidation, sorted set for streaks."),
            B.check(
                "az-redis-k3", "az-redis",
                "Which Redis structure fits a leaderboard?",
                listOf("A sorted set", "A hash", "A list"), 0,
                "A sorted set orders its members by a numeric score, which is what a leaderboard is.",
                whyWrong = mapOf(
                    1 to "A hash stores the fields of one object. Good for a cached record, but it orders nothing.",
                    2 to "A list keeps insertion order, which suits a queue rather than a ranking.",
                ),
            ),
            B.h("Check yourself"),
            B.gap(
                "az-m-redis-gap1", "Redis evicts keys that carry an expiry before it touches the rest.",
                "The default policy is volatile-___.", "lru", "LRU",
            ),
            B.gap(
                "az-m-redis-gap2", "Anything in the cache can vanish, so the application must cope.",
                "Always keep the ___ path that reads from the real store.", "slow", "Slow",
            ),
            B.tryIt("Try it in 15 minutes", "A Basic C0 cache costs a few cents an hour; delete it after.",
                "Create a Basic C0 cache", "Open the portal Console and run SET greeting hello EX 60, then GET greeting", "Wait a minute and GET again", "ZADD streaks 7 usaid 9 sanya then ZREVRANGE streaks 0 1 WITHSCORES",
            ),
            B.explain(
                "az-redis-x1",
                "Explain the cache-aside pattern, and the one rule about the connection you must not get wrong.",
                "Cache-aside means reading the cache first, and on a miss reading the real source and putting the value in the cache with a time to live. A write invalidates or updates the key so the next read is not stale. The connection rule is that ConnectionMultiplexer is expensive and meant to be shared: create one for the lifetime of the application, not one per request, or the cache runs out of connections.",
                "Read cache, on miss read source and populate" to listOf("cache-aside", "cache aside", "miss", "populate", "read the cache"),
                "A TTL, and invalidate on write" to listOf("ttl", "time to live", "expiry", "invalidate", "expire"),
                "One shared ConnectionMultiplexer" to listOf("connectionmultiplexer", "multiplexer", "one connection", "shared", "singleton"),
            ),
            B.glossary(
                "Cache-aside" to "Read cache, on miss read source and populate cache.",
                "TTL" to "Time to live; the expiry on a key.",
                "ConnectionMultiplexer" to "The StackExchange.Redis connection object; create once.",
                "Sorted set" to "Members ordered by a numeric score; leaderboards.",
                "Eviction policy" to "Which keys Redis drops when memory is full.",
            ),
            B.recap(
                "Redis is in-memory and fast; Standard for production, Premium for VNet and persistence, Enterprise for modules.",
                "Cache-aside with a TTL and invalidate on write; one multiplexer per app.",
                "Strings, hashes, sorted sets and lists each fit a pattern: values, objects, leaderboards, queues.",
            ),
        ),
    )

    private val cdn = Topic(
        id = "az-cdn", title = "Azure Front Door and CDN: caching at the edge", minutes = 20,
        references = listOf(
            B.ref("What is Azure Front Door?", LEARN + "azure/frontdoor/front-door-overview"),
            B.ref("Caching with Azure Front Door", LEARN + "azure/frontdoor/front-door-caching"),
            B.ref("Control CDN caching behavior with caching rules", LEARN + "azure/cdn/cdn-caching-rules"),
            B.ref("Purge cached assets", LEARN + "azure/frontdoor/front-door-caching?pivots=front-door-standard-premium#cache-purge"),
            B.ref("Learn module: Develop for storage on CDNs", LEARN + "training/modules/develop-for-storage-cdns/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-cdn-w1", "az-cdn",
                "A file is cached at the edge and you need it changed for everyone now. What do you think is the more dependable fix?",
                listOf("Change the file name so the URL is new", "Purge the path from every edge", "Set the cache lifetime to one second"), 0,
                "One of these cannot fail to work, because no cached copy can match it.",
            ),
            B.plain("""
                A content delivery network keeps copies of your static files on servers close to users. The first
                visitor in Doha fetches the file from your origin; everyone after them in the region gets the edge
                copy. Azure Front Door is the current product; the classic Azure CDN profiles still appear on the exam.
            """),
            B.diagram(
                caption = "Users hit the nearest edge. A miss goes to the origin once; the edge then serves hits until the TTL ends or you purge.",
                nodes = listOf(
                    DNode("u1", "Doha", 0.12f, 0.25f, w = 0.18f, h = 0.16f, kind = NodeKind.USER, tone = 1),
                    DNode("u2", "Lahore", 0.12f, 0.75f, w = 0.18f, h = 0.16f, kind = NodeKind.USER, tone = 1),
                    DNode("e1", "edge: Middle East", 0.45f, 0.25f, w = 0.3f, h = 0.18f, kind = NodeKind.CLOUD, tone = 2),
                    DNode("e2", "edge: South Asia", 0.45f, 0.75f, w = 0.3f, h = 0.18f, kind = NodeKind.CLOUD, tone = 2),
                    DNode("o", "origin: blob static website", 0.84f, 0.5f, w = 0.28f, h = 0.22f, kind = NodeKind.CYLINDER),
                ),
                edges = listOf(DEdge("u1", "e1"), DEdge("u2", "e2"), DEdge("e1", "o", label = "miss"), DEdge("e2", "o", label = "miss")),
                aspect = 0.5f,
            ),
            B.h("Products"),
            B.table(
                listOf("Product", "What it is", "Note"),
                listOf("Front Door Standard", "Global entry point: CDN, TLS, routing, WAF basics", "Replaces Azure CDN from Microsoft"),
                listOf("Front Door Premium", "Standard plus managed WAF rules, Private Link to origins", "Security-sensitive apps"),
                listOf("Azure CDN (classic, Edgio or Microsoft)", "Legacy CDN profiles", "Still in exam questions; same caching ideas"),
            ),
            B.check(
                "az-cdn-k2", "az-cdn",
                "What decides how long an edge keeps its copy of a file?",
                listOf("Cache-Control from the origin, unless a caching rule overrides it", "The CDN tier", "The size of the file"), 0,
                "TTL comes from the origin's Cache-Control header, or from a rule that overrides it.",
                whyWrong = mapOf(
                    1 to "The tier changes which features and how much you pay, not the lifetime of a cached copy.",
                    2 to "Size has no bearing on TTL.",
                ),
            ),
            B.h("How caching decides"),
            B.bullets(
                "The origin's Cache-Control header sets the TTL. If missing, Front Door uses a default (for example 1 to 3 days depending on the product) and you can override it with a rule.",
                "Query strings: ignore (one cached copy), use (a copy per unique URL), or include or exclude specific parameters.",
                "Compression can be enabled at the edge for text types.",
                "A purge removes cached content before its TTL; do it by path or wildcard. Versioned file names (app.3f2a.js) avoid purges altogether.",
            ),
            B.table(
                listOf("Caching rule", "Behaviour", "Use"),
                listOf("Honor origin", "Use Cache-Control from the origin", "Default"),
                listOf("Override", "Set your own TTL regardless", "Static assets with no headers"),
                listOf("Bypass cache", "Never cache", "Personalised or dynamic responses"),
            ),
            B.tip("\"A file was updated but users still see the old version\" is a purge or a versioned-filename question. \"Different users need different content under the same URL\" means bypass cache or vary on a query string."),
            B.check("az-cdn-k1", "az-cdn", "You deployed a new logo.png but users still see the old one from the CDN. What is the quickest fix?",
                listOf("Wait for the TTL", "Purge the path on the CDN endpoint", "Delete the storage account"), 1, "A purge evicts the cached copy so the next request fetches the new file."),
            B.code("bash", """
                az afd profile create --profile-name tt-fd --resource-group tt-rg --sku Standard_AzureFrontDoor
                az afd endpoint create --profile-name tt-fd --resource-group tt-rg --endpoint-name tt --enabled-state Enabled
                az afd origin-group create --profile-name tt-fd --resource-group tt-rg --origin-group-name static --probe-request-type GET --probe-protocol Https --probe-path / --probe-interval-in-seconds 60 --sample-size 4 --successful-samples-required 3 --additional-latency-in-milliseconds 50
                az afd origin create --profile-name tt-fd --resource-group tt-rg --origin-group-name static --origin-name blob --host-name ttstorage.z6.web.core.windows.net --origin-host-header ttstorage.z6.web.core.windows.net --https-port 443 --enabled-state Enabled
                az afd route create --profile-name tt-fd --resource-group tt-rg --endpoint-name tt --route-name all --origin-group static --supported-protocols Https --https-redirect Enabled --forwarding-protocol HttpsOnly --link-to-default-domain Enabled --enable-caching true --query-string-caching-behavior IgnoreQueryString
                az afd endpoint purge --profile-name tt-fd --resource-group tt-rg --endpoint-name tt --content-paths "/images/*"
            """),
            B.worked("""
                Worked example. Two Tracks serves its help pages and lesson diagrams from a blob static website behind
                Front Door. Diagrams are named with a content hash so a new version has a new URL and no purge is
                needed. The help index.html is set to Cache-Control: max-age=300 so edits show within five minutes.
                The API itself is routed with caching bypassed.
            """),
            B.steps("Serve a static site through Front Door",
                "Enable the static website on the storage account and upload index.html." to "Origin is the web endpoint, not the blob endpoint.",
                "Create a Front Door profile, endpoint, origin group and origin." to "Health probes watch the origin.",
                "Add a route with caching on and query strings ignored." to "Static assets become cached.",
                "Set Cache-Control on blobs with az storage blob update --content-cache-control." to "Control TTL from the origin.",
            ),
            B.mistakes(
                "Caching the API responses by accident and serving one user's data to another.",
                "Forgetting that a purge takes a few minutes and is per path; use versioned names for assets.",
                "Pointing the origin at the blob endpoint instead of the static website endpoint.",
                "Choosing \"use query string\" for assets with cache-busting parameters you never change; one copy per URL wastes cache.",
            ),
            B.example("Two Tracks: Front Door Standard in front of a static website, hashed asset names, 5-minute TTL on HTML, API route with cache bypass."),
            B.check(
                "az-cdn-k3", "az-cdn",
                "A stylesheet must change immediately for every user. What is the more reliable approach?",
                listOf("Give the file a versioned name, so the URL itself changes", "Purge the path and wait", "Drop the TTL to one second"), 0,
                "A new URL cannot be served from an old cached copy, which is why versioned names remove the need to purge at all.",
                whyWrong = mapOf(
                    1 to "Purging is the right tool for an urgent fix, but it is a request to every edge and not instant.",
                    2 to "A one-second TTL means re-fetching on almost every request, which gives up the point of a CDN.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-m-cdn-match", "Pair each caching rule with when to use it.",
                "Honor origin" to "Obey the origin's Cache-Control: the default",
                "Override" to "Set your own TTL for static assets with no headers",
                "Bypass cache" to "Never cache a personalised response",
                "Front Door Premium" to "Managed WAF rules and Private Link to origins",
            ),
            B.gap(
                "az-m-cdn-gap1", "The product that now replaces Azure CDN from Microsoft.",
                "Use Azure ___ Door Standard as the global entry point.", "Front", "front",
            ),
            B.tryIt("Try it in 15 minutes", "Front Door Standard has an hourly base fee; delete the profile after.",
                "Enable a static website on a storage account", "Create a Front Door Standard profile pointing at it", "Load the page twice and check the X-Cache header (TCP_MISS then TCP_HIT)", "Change the file, see the old one, purge, see the new one",
            ),
            B.explain(
                "az-cdn-x1",
                "Explain what decides how long an edge keeps a file, and the two ways to change a file that is already cached.",
                "An edge keeps a copy for the time to live, which comes from the origin's Cache-Control header unless a caching rule overrides it. Query string behaviour decides whether URL parameters produce separate cached copies. To change something already out there you either purge the path, which is a request to every edge and not instant, or give the file a versioned name so the URL itself changes and no old copy can match it.",
                "Cache-Control or a rule sets the TTL" to listOf("cache-control", "cache control", "ttl", "rule", "origin"),
                "Purge removes it before its TTL" to listOf("purge", "purging", "invalidate"),
                "A versioned name avoids the problem" to listOf("version", "versioned", "file name", "filename", "new url"),
            ),
            B.glossary(
                "Edge / point of presence" to "A CDN server near users that holds cached copies.",
                "Origin" to "Where the CDN fetches content from on a miss.",
                "TTL" to "How long an edge keeps a copy; set by Cache-Control or a rule.",
                "Purge" to "Remove cached content before its TTL.",
                "Query string caching" to "Whether URL parameters create separate cached copies.",
            ),
            B.recap(
                "Front Door is the current CDN and global entry point; classic CDN profiles share the concepts.",
                "Cache-Control from the origin or an override rule sets TTL; query string behaviour decides copies.",
                "Purge by path for urgent changes; versioned file names avoid the need.",
            ),
        ),
    )

    val topics = listOf(appInsights, availability, redis, cdn)

    val quiz = Quiz(
        id = "az-monitor-test", title = "Monitor and optimise module test",
        questions = B.spread(listOf(
            B.q("m1", "az-appinsights", "You want to count a business action (lesson finished) and split it by topic in a chart. What do you call?", listOf("TrackTrace", "TrackEvent with a topic property", "TrackException", "TrackRequest"), setOf(1), "Custom events carry string properties for splitting and measurements for charting."),
            B.q("m2", "az-appinsights", "Which setting should a modern SDK use to send telemetry?", listOf("Instrumentation key", "Connection string", "Workspace id", "Subscription id"), setOf(1), "Connection strings replaced instrumentation keys and include the regional endpoint."),
            B.q("m3", "az-appinsights", "Telemetry volume is too costly but counts must stay statistically correct. What do you enable?", listOf("Live Metrics", "Sampling", "A second workspace", "Smart detection"), setOf(1), "Adaptive or fixed-rate sampling reduces volume and corrects counts."),
            B.q("m4", "az-appinsights", "Which field lets you follow one request through its dependency calls across services?", listOf("session_Id", "operation_Id", "user_Id", "itemId"), setOf(1), "All telemetry of one operation shares operation_Id."),
            B.q("m5", "az-appinsights", "Which KQL operator groups rows and counts them?", listOf("where", "project", "summarize", "extend"), setOf(2), "summarize count() by column performs the grouping and aggregation."),
            B.q("m6", "az-availability", "A health endpoint is reachable only inside a VNet. How do you monitor availability?", listOf("A standard availability test", "A Function in the VNet calling TrackAvailability", "A URL ping test", "An activity log alert"), setOf(1), "Standard tests run from public locations; custom TrackAvailability from inside the network works."),
            B.q("m7", "az-availability", "You must be notified when a KQL query returns more than 10 exceptions in 15 minutes. Which alert?", listOf("Metric alert", "Log search alert", "Activity log alert", "Service health alert"), setOf(1), "Log search alerts evaluate KQL on a schedule."),
            B.q("m8", "az-availability", "Where do you define who receives alert notifications?", listOf("In the alert rule's name", "In an action group", "In the workspace", "In the App Service plan"), setOf(1), "Action groups hold the email, SMS, push, webhook and automation actions."),
            B.q("m9", "az-availability", "Instances scale out and back in every few minutes. What fixes the flapping? Select all that apply.", listOf("A cooldown period", "A gap between scale-out and scale-in thresholds", "Removing the scale-in rule", "Scaling up the tier"), setOf(0, 1), "Cooldown and a threshold gap stop oscillation; removing scale-in just wastes money."),
            B.q("m10", "az-availability", "Autoscale changes which of these?", listOf("The pricing tier", "The number of instances", "The region", "The runtime version"), setOf(1), "Autoscale is horizontal: it adds or removes instances."),
            B.q("m11", "az-redis", "Which Redis tier is the minimum for VNet injection and data persistence?", listOf("Basic", "Standard", "Premium", "Any"), setOf(2), "Premium adds persistence, clustering and VNet support."),
            B.q("m12", "az-redis", "Your app opens a new Redis connection on every request and runs out of connections. What should it do?", listOf("Use Basic tier", "Create one ConnectionMultiplexer and reuse it", "Disable TLS", "Use HSET instead of SET"), setOf(1), "The multiplexer is designed to be shared for the application lifetime."),
            B.q("m13", "az-redis", "Which pattern reads the cache first and populates it from the database on a miss?", listOf("Write-through", "Cache-aside", "Write-behind", "Read-replica"), setOf(1), "Cache-aside is the lazy-loading pattern."),
            B.q("m14", "az-redis", "Which data structure suits a top-10 leaderboard?", listOf("String", "List", "Sorted set", "Hash"), setOf(2), "Sorted sets order members by score for cheap range queries."),
            B.q("m15", "az-redis", "Which Redis command sets a value with an expiry in one call?", listOf("SET key value EX 60", "GET key", "INCR key", "DEL key"), setOf(0), "SET with EX (or StringSet with a TimeSpan) writes the value and TTL together."),
            B.q("m16", "az-cdn", "Users still see an old image after you replaced it in storage. What is the quickest fix?", listOf("Restart the storage account", "Purge the path on the CDN or Front Door endpoint", "Lower the TTL and wait", "Delete the endpoint"), setOf(1), "Purge evicts the cached copy immediately."),
            B.q("m17", "az-cdn", "Which origin header controls how long the edge caches a file by default?", listOf("Content-Type", "Cache-Control", "ETag", "Authorization"), setOf(1), "Cache-Control max-age sets the TTL when the rule is to honour the origin."),
            B.q("m18", "az-cdn", "Different users must never receive each other's responses under the same API URL through Front Door. What do you set on that route?", listOf("Ignore query string", "Bypass caching", "Compression on", "A longer TTL"), setOf(1), "Dynamic, personalised responses must not be cached at the edge."),
            B.q("m19", "az-cdn", "How can you avoid purges when deploying new static assets?", listOf("Use versioned or hashed file names", "Disable caching", "Use HTTP instead of HTTPS", "Set TTL to zero"), setOf(0), "A new file name is a new URL, so the edge fetches it fresh."),
            B.q("m20", "az-appinsights", "Which feature shows requests and failures with about one second of delay during a deployment?", listOf("Workbooks", "Live Metrics", "Application Map", "Usage"), setOf(1), "Live Metrics streams near-real-time telemetry."),
        )),
    )
}
