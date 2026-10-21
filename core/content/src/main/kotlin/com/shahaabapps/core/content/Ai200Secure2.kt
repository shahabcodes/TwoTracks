package com.shahaabapps.core.content

/** AI-200 topics, written from the official study guide and Microsoft Learn (Ai200). */
internal object Ai200Secure2 {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val openTelemetry = Topic(
        id = "ai-opentelemetry", title = "Tracing with OpenTelemetry: following a request across services", minutes = 28,
        references = listOf(
            B.ref("Enable OpenTelemetry in Application Insights", LEARN + "azure/azure-monitor/app/opentelemetry-enable"),
            B.ref("Azure Monitor OpenTelemetry Distro client library for Python", LEARN + "python/api/overview/azure/monitor-opentelemetry-readme"),
            B.ref("Add and modify OpenTelemetry in Application Insights", LEARN + "azure/azure-monitor/app/opentelemetry-add-modify"),
            B.ref("Configure Azure Monitor OpenTelemetry (cloud role name, sampling)", LEARN + "azure/azure-monitor/app/opentelemetry-configuration"),
            B.ref("Application Insights telemetry data model", LEARN + "azure/azure-monitor/app/data-model-complete"),
            B.ref("Use OpenTelemetry with Azure Functions", LEARN + "azure/azure-functions/opentelemetry-howto"),
            B.ref("Application Map: triage distributed applications", LEARN + "azure/azure-monitor/app/app-map"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-opentelemetry-w1", "ai-opentelemetry",
                "A question passes through the Coach API, a cache and a function. How do you think the three logs know they belong to the same question?",
                listOf("A shared trace id travels with every call", "They are matched by timestamp afterwards", "Each service logs the user's email"), 0,
                "Something has to travel with the request from service to service. Timestamps alone cannot tell two users apart.",
            ),
            B.plain("""
                Distributed tracing follows one request through every service it touches. OpenTelemetry is the open
                standard for it: a trace is the whole journey of one request, and a span is one step of that journey
                (handling the HTTP call, a Redis lookup, a database query). Every span carries the same trace id, so
                Application Insights can put the pieces back together as one story.
            """),
            B.diagram(
                caption = "One question to Two Tracks Coach becomes one trace. The trace id travels in the traceparent header, and every service exports its spans to Application Insights.",
                nodes = listOf(
                    DNode("note", "One trace id for all", 0.5f, 0.08f, w = 0.4f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                    DNode("api", "Coach API", 0.18f, 0.3f, w = 0.3f, h = 0.24f, tone = 1, sub = "Container App"),
                    DNode("func", "Summarize function", 0.82f, 0.3f, w = 0.3f, h = 0.24f, sub = "Function app"),
                    DNode("redis", "Managed Redis", 0.18f, 0.75f, w = 0.3f, h = 0.24f, tone = 2, sub = "client span"),
                    DNode("appi", "Application Insights", 0.5f, 0.75f, w = 0.3f, h = 0.24f, tone = 1),
                    DNode("cosmos", "Cosmos DB", 0.82f, 0.75f, w = 0.3f, h = 0.24f, kind = NodeKind.CYLINDER, tone = 2, sub = "dependency"),
                ),
                edges = listOf(
                    DEdge("api", "func", label = "traceparent"),
                    DEdge("api", "redis", label = "child span"),
                    DEdge("func", "cosmos"),
                    DEdge("api", "appi", dashed = true),
                    DEdge("func", "appi", dashed = true),
                ),
                aspect = 0.7f,
            ),
            B.h("Traces, spans and attributes"),
            B.table(
                listOf("Word", "Plain meaning", "In Two Tracks Coach"),
                listOf("Trace", "The whole journey of one request", "One student question, end to end"),
                listOf("Span", "One timed step, with a start, an end and a parent", "POST /ask, a Redis GET, a Cosmos query"),
                listOf("Attribute", "A key and value stuck on a span", "coach.topic = \"cosmos\""),
                listOf("Span kind", "Server, client, internal, producer or consumer", "Server for /ask, client for the call out"),
                listOf("Resource", "Who sent the span", "service.name = coach-api"),
            ),
            B.p("""
                In Application Insights, server and consumer spans land as requests, and client, producer and internal
                spans land as dependencies. The trace id becomes the operation id, which is what joins them later.
            """),
            B.h("Context propagation: the traceparent header"),
            B.p("""
                For a trace to cross from one service to the next, its context has to travel with the call. The W3C
                Trace Context standard puts it in an HTTP header called traceparent. When an instrumented client makes
                a call it adds the header; when an instrumented server receives one it continues the same trace
                instead of starting a new one.
            """),
            B.code("text", """
                traceparent: 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01
                             |  |                                |                |
                          version  trace id (32 hex)       parent span id     flags (01 = sampled)
            """),
            B.check(
                "ai-opentelemetry-k1", "ai-opentelemetry",
                "The Coach API calls the Summarize function, but the function's work shows up as a separate trace. What has gone missing?",
                listOf("The traceparent header on the outgoing call", "The function's managed identity", "A label in App Configuration"), 0,
                "Without traceparent the receiving side cannot know it is part of an existing trace, so it starts a new one.",
                whyWrong = mapOf(
                    1 to "An identity decides what the function may access. It carries no trace context.",
                    2 to "Labels vary settings per environment; they play no part in joining spans.",
                ),
            ),
            B.h("The Azure Monitor OpenTelemetry Distro for Python"),
            B.p("""
                The distro is one package, azure-monitor-opentelemetry, that bundles the OpenTelemetry SDK, the
                Azure Monitor exporter and a set of instrumentations. One call to configure_azure_monitor sets it all
                up. It reads the connection string from APPLICATIONINSIGHTS_CONNECTION_STRING if you do not pass one,
                which is the recommended way in production.
            """),
            B.code("python", """
                # pip install azure-monitor-opentelemetry fastapi requests
                from azure.monitor.opentelemetry import configure_azure_monitor
                configure_azure_monitor(logger_name="coach")   # before the app starts serving

                from fastapi import FastAPI
                from opentelemetry import trace
                import requests

                app = FastAPI()
                tracer = trace.get_tracer(__name__)

                @app.post("/ask")
                def ask(q: dict):                      # server span: automatic (FastAPI)
                    with tracer.start_as_current_span("rank-notes") as span:   # custom span
                        span.set_attribute("coach.topic", q["topic"])
                        notes = rank(q)                # your own code
                    # client span plus traceparent header: automatic (requests)
                    r = requests.post(SUMMARIZE_URL, json={"notes": notes})
                    return r.json()
            """),
            B.bullets(
                "Bundled and on by default: FastAPI, Flask, Django, requests, httpx, urllib, urllib3, psycopg2 and the Azure SDK (azure_sdk), so calls through azure-cosmos or azure-servicebus become dependencies too.",
                "Custom spans: trace.get_tracer(__name__), then start_as_current_span; set_attribute adds detail you can query later.",
                "Cloud role name: set OTEL_SERVICE_NAME (coach-api, summarize). It is the node name on the application map.",
                "Sampling: recent versions default to a rate-limited sampler of about five traces a second; sampling_ratio=0.1 keeps a fixed 10 percent and traces_per_second sets your own limit. Whole traces are kept or dropped together.",
            ),
            B.tip("\"One line to send traces, metrics and logs from Python to Application Insights\" points to configure_azure_monitor. Microsoft has also released a newer Microsoft OpenTelemetry Distro (microsoft-opentelemetry), which Learn now suggests for new apps; existing Azure Monitor distro apps need no change, and the two should not be initialised together."),
            B.check(
                "ai-opentelemetry-k2", "ai-opentelemetry",
                "Two containers send telemetry to the same Application Insights resource and appear as one blob on the application map. What fixes it?",
                listOf("Set OTEL_SERVICE_NAME differently in each", "Give each its own connection string", "Raise sampling_ratio to 1.0"), 0,
                "The service name becomes the cloud role name, and the application map draws one node per role.",
                whyWrong = mapOf(
                    1 to "Separate resources would split the trace across two places, which is the opposite of what you want.",
                    2 to "Sampling decides how many traces are kept, not how services are named.",
                ),
            ),
            B.h("Across a container and a function"),
            B.p("""
                Azure Functions takes part too. Set "telemetryMode": "OpenTelemetry" in host.json, keep
                APPLICATIONINSIGHTS_CONNECTION_STRING, add azure-monitor-opentelemetry to requirements.txt and set
                PYTHON_APPLICATIONINSIGHTS_ENABLE_TELEMETRY to true. The host then takes the operation id straight from
                the incoming traceparent, so the API's call and the function's work share one trace.
            """),
            B.h("Seeing it in Application Insights"),
            B.compare("Where to look",
                B.card("Transaction search", "You have one slow or failed request to explain.", "Find a request by time, name or result", "Open end-to-end transaction details", "See every span of that trace as a timeline"),
                B.card("Application map", "You want the shape of the whole system.", "One node per cloud role", "Arrows for calls, with counts and failure rates", "Spot which hop is slow or failing"),
            ),
            B.worked("""
                Worked example. Two Tracks Coach's /ask felt slow, and the Summarize function's spans appeared as a
                separate trace. Usaid found the API called the function with a raw socket helper the distro does not
                instrument. He switched the call to requests, which adds traceparent, set telemetryMode to
                OpenTelemetry in the function's host.json and gave each service its own OTEL_SERVICE_NAME. End-to-end
                transaction details now show one trace: 40 ms in the API, 1.8 s in the function, most of it a Cosmos
                query. The application map shows coach-api pointing at summarize.
            """),
            B.faded(
                "ai-opentelemetry-f1",
                "Same idea, two containers. The Coach API now calls a grader Container App over HTTP, and both must appear in one trace.",
                listOf(
                    "configure_azure_monitor runs in both containers, with OTEL_SERVICE_NAME set to coach-api and grader.",
                    "The requests instrumentation adds a ___ header to the outgoing call, carrying the trace id and the parent span id.",
                    "The grader's FastAPI instrumentation reads it and makes its server span a child, so the map shows coach-api joined to grader.",
                ),
                blank = 1,
                answers = listOf("traceparent", "trace-parent"),
                note = "Context propagation is the header. Without it each service starts a fresh trace and the map shows two islands.",
            ),
            B.steps("Trace the Coach API for real",
                "az monitor app-insights component create --app coach-ai --location westeurope --resource-group tt-rg --workspace <workspace-id>" to "A workspace-based Application Insights resource; copy its connectionString.",
                "az containerapp update --name coach-api --resource-group tt-rg --set-env-vars APPLICATIONINSIGHTS_CONNECTION_STRING=secretref:appi-conn OTEL_SERVICE_NAME=coach-api" to "The app reads the connection string from a secret and names itself.",
                "Deploy the image with configure_azure_monitor called at start-up, then send a few questions to /ask" to "Telemetry appears after a minute or two.",
                "Portal: Application Insights > Transaction search > pick a POST /ask > open the end-to-end view" to "A timeline with the server span, the custom rank-notes span and the client call.",
                "Portal: Application Insights > Application map" to "Nodes for coach-api and summarize with an arrow between them.",
            ),
            B.mistakes(
                "Calling configure_azure_monitor after the web app has been created, so its first requests go untraced.",
                "Leaving every service with the default name, so the application map draws one confused node.",
                "Hard-coding the connection string in source instead of reading APPLICATIONINSIGHTS_CONNECTION_STRING.",
                "Making outgoing calls with a library the distro does not instrument, which drops the traceparent header.",
                "Expecting every request to be stored when the default rate-limited sampler is keeping only some traces.",
            ),
            B.example("Two Tracks Coach: coach-api (Container App), summarize (Function app) and grader all call configure_azure_monitor or use telemetryMode OpenTelemetry, each with its own OTEL_SERVICE_NAME; a custom rank-notes span carries coach.topic so slow topics can be found later with KQL."),
            B.check(
                "ai-opentelemetry-k3", "ai-opentelemetry",
                "You want to time only the note-ranking part of /ask and tag it with the topic. What do you write?",
                listOf("A custom span with start_as_current_span and set_attribute", "A second call to configure_azure_monitor", "A new Application Insights resource"), 0,
                "A custom span from the tracer times exactly the code inside its with block, and attributes add searchable detail.",
                whyWrong = mapOf(
                    1 to "configure_azure_monitor sets up the pipeline once. Calling it again adds nothing and can double up exporters.",
                    2 to "Another resource splits the data. The span belongs in the same trace as the request.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-otel-match", "Pair each OpenTelemetry idea with what it is.",
                "Trace" to "The whole journey of one request",
                "Span" to "One timed step inside that journey",
                "traceparent" to "The header that carries the trace id to the next service",
                "OTEL_SERVICE_NAME" to "The cloud role name on the application map",
            ),
            B.gap(
                "ai-x-otel-gap1", "One call sets up the Azure Monitor distro in Python.",
                "Call ___ once, before the app starts serving.", "configure_azure_monitor", "configure_azure_monitor()",
            ),
            B.tryIt("Try it in 15 minutes", "A free Application Insights allowance covers this.",
                "Create a workspace-based Application Insights resource and copy its connection string",
                "pip install azure-monitor-opentelemetry fastapi uvicorn requests and call configure_azure_monitor in a tiny FastAPI app",
                "Add one custom span with an attribute, run the app and hit it a few times",
                "Open Transaction search, then the end-to-end view of one request",
            ),
            B.explain(
                "ai-opentelemetry-x1",
                "Explain how one request is followed across the Coach API and the Summarize function, and what you add to the Python code.",
                "Every request becomes a trace, and each step inside it is a span that carries the same trace id, which Application Insights stores as the operation id. When the API calls the function, the instrumented requests library adds a traceparent header, and the receiving side continues the trace through that propagation. In Python you install the Azure Monitor distro and call configure_azure_monitor once at start-up; it brings automatic instrumentation for FastAPI, requests and the Azure SDK, and you add custom spans with the tracer where you need more detail.",
                "Spans share one trace id" to listOf("trace id", "span", "operation id", "operationid"),
                "The traceparent header carries the context" to listOf("traceparent", "propagation", "propagated", "header"),
                "The distro instruments the app" to listOf("distro", "instrumentation", "configure", "application insights"),
            ),
            B.glossary(
                "Trace" to "All the spans of one request, joined by a shared trace id.",
                "Span" to "One timed operation in a trace, with a parent, a kind and attributes.",
                "traceparent" to "The W3C header that carries the trace id and parent span id between services.",
                "configure_azure_monitor" to "The one call that sets up the Azure Monitor OpenTelemetry Distro in Python.",
                "Cloud role name" to "The service's name in Application Insights, from OTEL_SERVICE_NAME.",
                "Sampling" to "Keeping only some traces, whole, to control cost and volume.",
            ),
            B.recap(
                "A trace is one request; spans are its steps; the trace id becomes the operation id.",
                "traceparent carries the context between services; instrumented clients and servers handle it for you.",
                "configure_azure_monitor plus OTEL_SERVICE_NAME; custom spans with the tracer; read it in transaction search and the application map.",
            ),
        ),
    )

    private val kql = Topic(
        id = "ai-kql", title = "KQL: asking your logs and metrics questions", minutes = 28,
        references = listOf(
            B.ref("Log queries in Azure Monitor", LEARN + "azure/azure-monitor/logs/log-query-overview"),
            B.ref("Get started with log queries in Azure Monitor", LEARN + "azure/azure-monitor/logs/get-started-queries"),
            B.ref("Create and configure Application Insights resources", LEARN + "azure/azure-monitor/app/create-workspace-resource"),
            B.ref("Monitor logs in Azure Container Apps with Log Analytics", LEARN + "azure/container-apps/log-monitoring"),
            B.ref("summarize operator", LEARN + "kusto/query/summarize-operator"),
            B.ref("Create a log search alert rule", LEARN + "azure/azure-monitor/alerts/alerts-create-log-alert-rule"),
            B.ref("Learn module: Write your first query with Kusto Query Language", LEARN + "training/modules/write-first-query-kusto-query-language/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-kql-w1", "ai-kql",
                "You want the slowest five percent of /ask requests over the last day. What do you think a log query starts with?",
                listOf("The name of a table", "SELECT * FROM", "A time range in a URL"), 0,
                "This query language reads like a pipeline. Think about where the rows come from first.",
            ),
            B.plain("""
                Your telemetry lands in a Log Analytics workspace, a store of tables. KQL, the Kusto Query Language, is
                how you ask it questions. A query starts with a table and passes its rows through a pipeline of steps,
                each after a | (pipe): filter, pick columns, group and count, then draw a chart.
            """),
            B.diagram(
                caption = "A KQL query is a pipeline: rows flow from a table through each step, left to right, and come out as a table or a chart.",
                nodes = listOf(
                    DNode("t", "AppRequests", 0.13f, 0.35f, w = 0.22f, h = 0.3f, kind = NodeKind.PILL, tone = 1, sub = "rows in"),
                    DNode("w", "where", 0.38f, 0.35f, w = 0.22f, h = 0.3f, kind = NodeKind.PILL, sub = "keep last 1h"),
                    DNode("s", "summarize", 0.63f, 0.35f, w = 0.22f, h = 0.3f, kind = NodeKind.PILL, sub = "p95 per 5 min"),
                    DNode("r", "render", 0.88f, 0.35f, w = 0.22f, h = 0.3f, kind = NodeKind.PILL, tone = 2, sub = "timechart"),
                    DNode("n", "Each | passes its rows to the next step", 0.5f, 0.8f, w = 0.6f, h = 0.25f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("t", "w"), DEdge("w", "s"), DEdge("s", "r")),
                aspect = 0.4f,
            ),
            B.h("Where your data lives"),
            B.table(
                listOf("Table (workspace)", "Classic name", "What is in it"),
                listOf("AppRequests", "requests", "Incoming calls: Name, Url, DurationMs, Success, ResultCode"),
                listOf("AppDependencies", "dependencies", "Outgoing calls: Target, DependencyType, DurationMs, Success"),
                listOf("AppExceptions", "exceptions", "Errors: ExceptionType, OuterMessage, ProblemId"),
                listOf("AppTraces", "traces", "Log lines: Message, SeverityLevel"),
                listOf("ContainerAppConsoleLogs_CL", "(none)", "Container stdout: ContainerAppName_s, RevisionName_s, Log_s"),
            ),
            B.p("""
                Workspace-based Application Insights writes to the App tables in the workspace, with TimeGenerated,
                OperationId and AppRoleName. The Logs page of the Application Insights resource still accepts the
                classic names (requests, timestamp, operation_Id, cloud_RoleName). Same data, two spellings.
            """),
            B.tip("Container Apps console output goes to ContainerAppConsoleLogs_CL and system events to ContainerAppSystemLogs_CL. The _CL tables use suffixed columns: ContainerAppName_s, Log_s."),
            B.h("The operators you will use every day"),
            B.table(
                listOf("Operator", "What it does", "Example"),
                listOf("where", "Keep rows that match", "where TimeGenerated > ago(1h)"),
                listOf("project", "Choose and rename columns", "project TimeGenerated, Name, DurationMs"),
                listOf("extend", "Add a calculated column", "extend slow = DurationMs > 2000"),
                listOf("summarize", "Group and aggregate", "summarize count() by ResultCode"),
                listOf("bin", "Round time into buckets", "by bin(TimeGenerated, 5m)"),
                listOf("order by / take", "Sort, then limit rows", "order by DurationMs desc | take 10"),
                listOf("join", "Match rows from two tables", "join kind=inner (AppExceptions) on OperationId"),
                listOf("render", "Draw a chart", "render timechart"),
            ),
            B.check(
                "ai-kql-k1", "ai-kql",
                "You want a count of requests for every 5-minute slice of the last hour. Which part does the slicing?",
                listOf("bin(TimeGenerated, 5m) in the summarize by", "take 5", "project TimeGenerated"), 0,
                "bin rounds each timestamp down to its bucket, and summarize then counts per bucket.",
                whyWrong = mapOf(
                    1 to "take 5 returns any five rows. It does no grouping at all.",
                    2 to "project only chooses columns; every row still stands alone.",
                ),
            ),
            B.h("Latency, failures and logs"),
            B.code("kusto", """
                // p50, p95 and p99 latency of the Coach API, per 5 minutes, last 24 hours
                AppRequests
                | where TimeGenerated > ago(24h) and AppRoleName == "coach-api"
                | summarize percentiles(DurationMs, 50, 95, 99), calls = count() by bin(TimeGenerated, 5m)
                | render timechart

                // failed requests with the exception that caused them
                AppRequests
                | where TimeGenerated > ago(1d) and Success == false
                | join kind=inner (AppExceptions | where TimeGenerated > ago(1d)) on OperationId
                | project TimeGenerated, Name, ResultCode, ExceptionType, OuterMessage
                | take 20

                // what the container printed
                ContainerAppConsoleLogs_CL
                | where ContainerAppName_s == "coach-api" and Log_s contains "timeout"
                | project TimeGenerated, RevisionName_s, Log_s
                | order by TimeGenerated desc
            """),
            B.remember("Use percentiles, not averages, for latency. An average hides the slow tail; p95 says what the slowest one request in twenty felt like."),
            B.check(
                "ai-kql-k2", "ai-kql",
                "You need the exception details for every failed request. Which column joins AppRequests to AppExceptions?",
                listOf("OperationId", "ResultCode", "AppRoleName"), 0,
                "OperationId is the trace id, shared by every item of one request, so it is the key that joins them.",
                whyWrong = mapOf(
                    1 to "ResultCode is the HTTP status. Thousands of unrelated requests share a 500.",
                    2 to "AppRoleName names the service, which every request of that service shares.",
                ),
            ),
            B.h("From a query to an alert"),
            B.p("""
                A log search alert rule runs a query on a schedule and fires when the result crosses a threshold. You
                choose how often it runs (evaluation frequency), how much data it looks back over (window), the
                threshold and an action group that emails or calls a webhook.
            """),
            B.code("kusto", """
                // alert query: failed /ask requests; fire when the count over 5 minutes is above 10
                AppRequests
                | where AppRoleName == "coach-api" and Name has "/ask" and Success == false
                | summarize failures = count() by bin(TimeGenerated, 5m)
            """),
            B.worked("""
                Worked example. Students said Two Tracks Coach was slow in the evenings. Usaid opened the workspace and
                wrote AppRequests, where TimeGenerated > ago(7d) and Name has "/ask", then summarize
                percentile(DurationMs, 95) by bin(TimeGenerated, 1h), render timechart. The chart showed p95 jumping
                from 0.9 s to 6 s every evening at eight. A join to AppDependencies on OperationId showed the time was
                spent in Cosmos DB, which pointed at an under-provisioned container, not the API.
            """),
            B.faded(
                "ai-kql-f1",
                "Same workspace, a new question: how many /ask requests failed in each 5-minute slice of the last hour?",
                listOf(
                    "Start from AppRequests and keep the last hour with where TimeGenerated > ago(1h).",
                    "Keep only failures: where Name has \"/ask\" and Success == false.",
                    "Group them: ___ failures = count() by bin(TimeGenerated, 5m), then render timechart.",
                ),
                blank = 2,
                answers = listOf("summarize", "summarise"),
                note = "where narrows, summarize groups and counts, bin makes the time buckets, render draws them.",
            ),
            B.steps("Ask the workspace yourself",
                "Portal: Application Insights coach-ai > Logs (or the Log Analytics workspace > Logs)" to "A query editor with the tables listed on the left.",
                "Run: AppRequests | where TimeGenerated > ago(1h) | take 10" to "Ten rows. Look at Name, DurationMs, Success and OperationId.",
                "Run the p95 query with render timechart" to "A line per percentile across the last day.",
                "Select New alert rule from the query, set the threshold to 10, frequency and window to 5 minutes, and pick an action group" to "A log search alert rule that emails you when failures spike.",
                "az monitor log-analytics query --workspace <workspace-guid> --analytics-query \"AppRequests | take 5\" --out table" to "The same query from the command line.",
            ),
            B.mistakes(
                "Forgetting a time filter, so the query scans weeks of data and runs slowly.",
                "Averaging DurationMs and missing the slow tail that users actually complain about.",
                "Querying requests and timestamp in the workspace, where the tables are AppRequests and TimeGenerated.",
                "Writing ContainerAppName instead of ContainerAppName_s in the ContainerAppConsoleLogs_CL table.",
                "Joining on ResultCode or AppRoleName instead of OperationId and getting a huge cross product.",
            ),
            B.example("Two Tracks Coach: a workbook with p95 latency per endpoint, failures joined to exceptions by OperationId, a ContainerAppConsoleLogs_CL query per revision after each deploy, and a log search alert on failed /ask requests."),
            B.check(
                "ai-kql-k3", "ai-kql",
                "Which query keeps only the last hour of requests?",
                listOf("AppRequests | where TimeGenerated > ago(1h)", "AppRequests | take 1h", "AppRequests | project ago(1h)"), 0,
                "ago(1h) is one hour before now, and where keeps the rows newer than it.",
                whyWrong = mapOf(
                    1 to "take limits the number of rows; it has no idea of time.",
                    2 to "project chooses columns, so this adds a column instead of filtering rows.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-kql-match", "Pair each operator with its job.",
                "where" to "Keep only the rows that match",
                "summarize" to "Group rows and aggregate them",
                "extend" to "Add a calculated column",
                "render" to "Draw the result as a chart",
            ),
            B.gap(
                "ai-x-kql-gap1", "Round timestamps into 5-minute buckets.",
                "summarize count() by ___(TimeGenerated, 5m)", "bin", "Bin",
            ),
            B.gap(
                "ai-x-kql-gap2", "Workspace-based Application Insights stores incoming requests here.",
                "The table is called ___.", "AppRequests", "apprequests",
            ),
            B.tryIt("Try it in 15 minutes", "Any workspace with a little data will do, or the free demo workspace in the portal.",
                "Open Logs and run AppRequests | where TimeGenerated > ago(1h) | take 10",
                "Count requests by ResultCode with summarize",
                "Chart p95 DurationMs by bin(TimeGenerated, 5m) with render timechart",
                "Turn a failures query into a log search alert rule with a threshold",
            ),
            B.explain(
                "ai-kql-x1",
                "Explain how you would find when the Coach API got slow, and how you would be told next time.",
                "I would start from the AppRequests table in the workspace and filter with a where on TimeGenerated and the coach-api role. Then I summarize the 95th percentile of DurationMs by bin of five minutes and render a timechart, because a percentile shows the slow tail an average hides. To find the cause I join AppDependencies or AppExceptions on OperationId. To be told next time, I save a failures or latency query as a log search alert rule with a threshold, an evaluation frequency and an action group.",
                "Start from the right table with a time filter" to listOf("apprequests", "timegenerated", "time filter"),
                "Summarize a percentile per time bucket" to listOf("summarize", "percentile", "timechart"),
                "Turn the query into an alert" to listOf("alert", "threshold", "action group"),
            ),
            B.glossary(
                "Log Analytics workspace" to "The store of tables your logs and Application Insights data land in.",
                "KQL" to "Kusto Query Language: a table, then a pipeline of steps separated by |.",
                "summarize" to "Groups rows and aggregates them: count, avg, percentile.",
                "bin" to "Rounds values, usually timestamps, into equal buckets.",
                "ago()" to "A time relative to now, such as ago(1h).",
                "Log search alert rule" to "A query run on a schedule that fires above a threshold.",
            ),
            B.recap(
                "AppRequests, AppDependencies, AppExceptions, AppTraces in the workspace; ContainerAppConsoleLogs_CL for container output.",
                "where with ago() first, then project, extend, summarize with bin, order by, take, join on OperationId, render.",
                "Percentiles for latency; save a query as a log search alert rule with a threshold and an action group.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(openTelemetry, kql)

    val quiz: Quiz = Quiz(
        id = "ai-secure-test", title = "Secure and monitor module test",
        questions = B.spread(listOf(
            B.q("ai-s1", "ai-keyvault", "Which Python code reads the current value of the secret CosmosKey with no credential stored in the app?",
                listOf(
                    "SecretClient(vault_url=url, credential=DefaultAzureCredential()).get_secret(\"CosmosKey\").value",
                    "SecretClient(connection_string=conn).get_secret(\"CosmosKey\")",
                    "SecretClient(vault_url=url, credential=key).list_properties_of_secrets()",
                ), setOf(0),
                "SecretClient takes the vault URL and an Entra credential; get_secret returns a KeyVaultSecret whose value is the secret text.",
                whyWrong = mapOf(
                    1 to "Key Vault has no connection string. Access is with an Entra credential such as a managed identity.",
                    2 to "list_properties_of_secrets lists names and metadata, never the values.",
                ),
            ),
            B.q("ai-s2", "ai-keyvault", "The Cosmos key is rotated with set_secret under the same name. What happens to apps that read it without a version?",
                listOf("They get the new version on their next read", "They keep the old value forever", "They fail until redeployed"), setOf(0),
                "set_secret creates a new version; reading without a version always returns the latest one.",
                whyWrong = mapOf(
                    1 to "Only a reference pinned to a specific version stays on the old value.",
                    2 to "Nothing breaks; the old version is still there and the latest is served next time.",
                ),
            ),
            B.q("ai-s3", "ai-keyvault", "A secret must be rotated automatically shortly before it expires. What starts the rotation?",
                listOf("A near-expiry event from Key Vault through Event Grid, handled by a function", "Soft delete", "Purge protection"), setOf(0),
                "Key Vault publishes a near-expiry event to Event Grid; a function subscribed to it creates the new secret value.",
                whyWrong = mapOf(
                    1 to "Soft delete keeps deleted secrets recoverable. It does not create new ones.",
                    2 to "Purge protection stops permanent deletion; it has nothing to do with expiry.",
                ),
            ),
            B.q("ai-s4", "ai-keyvault", "The Coach API only reads secrets; a rotation function writes them. Which roles?",
                listOf(
                    "Key Vault Secrets User for the API, Key Vault Secrets Officer for the function",
                    "Key Vault Administrator for both",
                    "Key Vault Reader for both",
                ), setOf(0),
                "Least privilege: Secrets User can read secret values, Secrets Officer can also create and update them.",
                whyWrong = mapOf(
                    1 to "Administrator is far more than either needs.",
                    2 to "Key Vault Reader sees the vault's metadata but cannot read secret values.",
                ),
            ),
            B.q("ai-s5", "ai-keyvault", "The API calls get_secret on every request and starts getting throttled by Key Vault. What should it do?",
                listOf("Cache the secret in memory and refresh it now and then", "Move to the Premium tier", "Store the secret in an environment file instead"), setOf(0),
                "Key Vault has request limits; read a secret once, keep it in memory and refresh it periodically or after rotation.",
                whyWrong = mapOf(
                    1 to "Premium is about HSM-protected keys, not about request limits for secrets.",
                    2 to "That puts the secret back on disk, which is what the vault is for avoiding.",
                ),
            ),
            B.q("ai-s6", "ai-appconfig", "The Coach API must load only the Coach:* keys with the prod label. What do you pass to load()?",
                listOf(
                    "selects=[SettingSelector(key_filter=\"Coach:*\", label_filter=\"prod\")]",
                    "refresh_on=[WatchKey(\"prod\")]",
                    "trim_prefixes=[\"prod\"]",
                ), setOf(0),
                "SettingSelector filters by key and label; without one the provider loads keys with no label.",
                whyWrong = mapOf(
                    1 to "WatchKey names a sentinel to watch for changes. It does not filter what is loaded.",
                    2 to "trim_prefixes shortens key names after loading; it does not select a label.",
                ),
            ),
            B.q("ai-s7", "ai-appconfig", "load() was called with refresh_on=[WatchKey(\"Coach:Sentinel\")] but changed values never appear. What is missing?",
                listOf("Calling config.refresh(), for example before each request", "A webhook from App Configuration", "A restart of the container"), setOf(0),
                "The Python provider does not refresh in the background; refresh() checks the sentinel once the interval has passed.",
                whyWrong = mapOf(
                    1 to "No webhook is involved; the provider checks the sentinel when refresh() is called.",
                    2 to "A restart works but defeats the point of dynamic refresh.",
                ),
            ),
            B.q("ai-s8", "ai-appconfig", "A key in App Configuration is a Key Vault reference. What must the Python app provide so load() can resolve it?",
                listOf("keyvault_credential, with access to the vault", "Nothing; App Configuration reads the vault for you", "The secret's value in an environment variable"), setOf(0),
                "The provider resolves the reference itself, so it needs a credential that the vault accepts.",
                whyWrong = mapOf(
                    1 to "App Configuration stores only the pointer. The app's own identity reads the secret.",
                    2 to "That puts the secret back in plain configuration.",
                ),
            ),
            B.q("ai-s9", "ai-appconfig", "The app's managed identity must read settings from the store. Which role?",
                listOf("App Configuration Data Reader", "Reader", "App Configuration Data Owner"), setOf(0),
                "Data Reader allows reading key-values through Entra ID, with no access key in the app.",
                whyWrong = mapOf(
                    1 to "Reader is a control-plane role; it does not grant access to the key-values.",
                    2 to "Data Owner can also write, which a reading app does not need.",
                ),
            ),
            B.q("ai-s10", "ai-appconfig", "A new answer-reranker should reach 10 percent of students without a redeploy. What do you use?",
                listOf("A feature flag with a percentage filter", "A label called reranker", "A snapshot"), setOf(0),
                "Feature flags switch behaviour at runtime, and a filter decides who gets it, such as a percentage of users.",
                whyWrong = mapOf(
                    1 to "Labels vary values by environment; they do not split users.",
                    2 to "A snapshot freezes a set of keys; it does not roll out to a share of users.",
                ),
            ),
            B.q("ai-s11", "ai-opentelemetry", "Which call sets up the Azure Monitor OpenTelemetry Distro in a Python app?",
                listOf("configure_azure_monitor()", "trace.get_tracer(__name__)", "logging.basicConfig()"), setOf(0),
                "configure_azure_monitor wires up the SDK, the exporter and the bundled instrumentations in one call.",
                whyWrong = mapOf(
                    1 to "get_tracer gives you a tracer for custom spans, but nothing is exported until the distro is configured.",
                    2 to "basicConfig sets up Python logging locally; it sends nothing to Application Insights.",
                ),
            ),
            B.q("ai-s12", "ai-opentelemetry", "Which HTTP header carries the trace id from the Coach API to the Summarize function?",
                listOf("traceparent", "Authorization", "x-ms-client-request-id"), setOf(0),
                "W3C Trace Context puts the version, trace id, parent span id and flags in traceparent.",
                whyWrong = mapOf(
                    1 to "Authorization carries a token for access, not tracing context.",
                    2 to "That is a per-request id for an Azure SDK call, not the trace context standard.",
                ),
            ),
            B.q("ai-s13", "ai-opentelemetry", "Where does the connection string for configure_azure_monitor come from in production?",
                listOf("The APPLICATIONINSIGHTS_CONNECTION_STRING environment variable", "A constant in the source code", "The traceparent header"), setOf(0),
                "The distro reads that variable automatically, so the value can come from a Container Apps secret or app setting.",
                whyWrong = mapOf(
                    1 to "Hard-coding it ties the code to one resource and puts it in source control.",
                    2 to "traceparent carries trace context between services, not configuration.",
                ),
            ),
            B.q("ai-s14", "ai-opentelemetry", "Ingestion costs are too high and the team accepts keeping one trace in ten. What do you set?",
                listOf("sampling_ratio=0.1", "enable_live_metrics=False", "OTEL_SERVICE_NAME=sample"), setOf(0),
                "A fixed-percentage sampler keeps whole traces at the given ratio.",
                whyWrong = mapOf(
                    1 to "Live metrics is a real-time view; turning it off does not reduce stored traces.",
                    2 to "The service name only labels the telemetry.",
                ),
            ),
            B.q("ai-s15", "ai-opentelemetry", "You need to see which service in the chain is failing most, at a glance. Where do you look?",
                listOf("The application map", "One request in transaction search", "The Key Vault activity log"), setOf(0),
                "The application map draws each cloud role as a node, with call counts and failure rates on the arrows.",
                whyWrong = mapOf(
                    1 to "Transaction search explains one request in depth, not the whole system at once.",
                    2 to "The vault's activity log records management operations on the vault.",
                ),
            ),
            B.q("ai-s16", "ai-kql", "Which query gives the 95th percentile duration of requests per 5 minutes?",
                listOf(
                    "AppRequests | summarize percentile(DurationMs, 95) by bin(TimeGenerated, 5m)",
                    "AppRequests | where percentile(DurationMs, 95) | take 5",
                    "AppRequests | project DurationMs, 95, 5m",
                ), setOf(0),
                "summarize aggregates, percentile picks the tail value, and bin makes the 5-minute buckets.",
                whyWrong = mapOf(
                    1 to "where filters rows by a condition; an aggregate cannot go there.",
                    2 to "project only chooses columns, so nothing is calculated.",
                ),
            ),
            B.q("ai-s17", "ai-kql", "In a workspace-based Application Insights setup, which table holds incoming HTTP requests?",
                listOf("AppRequests", "AppDependencies", "ContainerAppSystemLogs_CL"), setOf(0),
                "Workspace-based resources write requests to AppRequests; the classic name is requests.",
                whyWrong = mapOf(
                    1 to "AppDependencies holds the calls your app makes out, not the ones it receives.",
                    2 to "That table holds Container Apps platform events such as revision provisioning.",
                ),
            ),
            B.q("ai-s18", "ai-kql", "You want what the coach-api container printed to stdout in the last hour. Which query?",
                listOf(
                    "ContainerAppConsoleLogs_CL | where TimeGenerated > ago(1h) and ContainerAppName_s == \"coach-api\" | project TimeGenerated, Log_s",
                    "AppRequests | where Name == \"coach-api\"",
                    "ContainerAppSystemLogs_CL | take 1h",
                ), setOf(0),
                "Console output lands in ContainerAppConsoleLogs_CL, whose columns carry suffixes such as _s.",
                whyWrong = mapOf(
                    1 to "AppRequests holds request telemetry, not the container's own printed lines.",
                    2 to "System logs are platform events, and take counts rows rather than time.",
                ),
            ),
            B.q("ai-s19", "ai-kql", "How do you add a column marking requests slower than two seconds, keeping all the other columns?",
                listOf("extend slow = DurationMs > 2000", "project slow = DurationMs > 2000", "summarize slow = DurationMs > 2000"), setOf(0),
                "extend adds a calculated column and keeps every existing one.",
                whyWrong = mapOf(
                    1 to "project keeps only the columns you list, so everything else disappears.",
                    2 to "summarize needs an aggregate and collapses the rows into groups.",
                ),
            ),
            B.q("ai-s20", "ai-kql", "The team wants an email when more than ten /ask requests fail in five minutes. What do you create?",
                listOf("A log search alert rule on a failures query with a threshold of 10 and an action group", "A workbook with a timechart", "A daily export of AppRequests to storage"), setOf(0),
                "A log search alert rule runs the query on a schedule, compares the result with the threshold and triggers the action group.",
                whyWrong = mapOf(
                    1 to "A workbook shows the chart to whoever opens it; it notifies nobody.",
                    2 to "An export keeps the data elsewhere but never evaluates it.",
                ),
            ),
        )),
    )
}
