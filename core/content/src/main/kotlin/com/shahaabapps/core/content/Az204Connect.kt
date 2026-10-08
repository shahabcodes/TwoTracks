package com.shahaabapps.core.content

/** AZ-204 module 5, Connect and consume: API Management and Event Grid. */
internal object Az204Connect {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val apim = Topic(
        id = "az-apim", title = "API Management: one front door for your APIs", minutes = 30,
        references = listOf(
            B.ref("About API Management", LEARN + "azure/api-management/api-management-key-concepts"),
            B.ref("Policies in Azure API Management", LEARN + "azure/api-management/api-management-howto-policies"),
            B.ref("API Management policy reference", LEARN + "azure/api-management/api-management-policies"),
            B.ref("Subscriptions in Azure API Management", LEARN + "azure/api-management/api-management-subscriptions"),
            B.ref("Learn module: Explore API Management", LEARN + "training/modules/explore-api-management/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-apim-w1", "az-apim",
                "A caller is making too many requests. Which answer do you think API Management gives when you have told it to smooth out bursts?",
                listOf("429 Too Many Requests", "403 Forbidden", "503 Service Unavailable"), 0,
                "There are two ways to limit a caller, and they answer with different codes on purpose.",
            ),
            B.plain("""
                API Management (APIM) sits between callers and your back-end APIs. It gives every API one address,
                checks keys and tokens, limits how often a caller may hit you, rewrites requests and responses, and
                publishes a developer portal with documentation and a try-it page.
            """),
            B.diagram(
                caption = "The gateway runs the policies. The management plane configures; the developer portal documents. Products group APIs and hand out subscription keys.",
                nodes = listOf(
                    DNode("dev", "App or partner", 0.12f, 0.5f, w = 0.2f, h = 0.26f, kind = NodeKind.USER, tone = 1),
                    DNode("gw", "Gateway", 0.42f, 0.5f, w = 0.26f, h = 0.34f, tone = 1, sub = "inbound → backend → outbound → on-error"),
                    DNode("be1", "Two Tracks API", 0.8f, 0.3f, w = 0.26f, h = 0.16f),
                    DNode("be2", "Functions", 0.8f, 0.7f, w = 0.26f, h = 0.16f, tone = 2),
                    DNode("portal", "developer portal: docs, keys, try it", 0.42f, 0.92f, w = 0.5f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("dev", "gw", label = "Ocp-Apim-Subscription-Key"), DEdge("gw", "be1"), DEdge("gw", "be2")),
                aspect = 0.55f,
            ),
            B.h("The building blocks"),
            B.table(
                listOf("Term", "Meaning", "Example"),
                listOf("API", "A set of operations mapped to a back end", "Logs API with GET /logs, POST /logs"),
                listOf("Operation", "One method and URL template", "GET /logs/{id}"),
                listOf("Product", "A bundle of APIs offered to developers, with terms and quotas", "Partner (needs approval), Free (open)"),
                listOf("Subscription", "A pair of keys granting access to a product or API", "Header Ocp-Apim-Subscription-Key"),
                listOf("Group", "Developers with the same visibility", "Administrators, Developers, Guests"),
                listOf("Policy", "XML statements run at a scope", "rate-limit, validate-jwt, set-header"),
                listOf("Backend", "A named back-end service with its own credentials", "https://tt-api.azurewebsites.net"),
            ),
            B.h("Tiers"),
            B.table(
                listOf("Tier", "Use", "Note"),
                listOf("Consumption", "Serverless, pay per call", "No developer portal, no VNet, fast start"),
                listOf("Developer", "Non-production", "No SLA"),
                listOf("Basic / Standard", "Production, growing", "Standard v2 adds VNet integration"),
                listOf("Premium", "Multi-region, VNet injection, self-hosted gateway", "Enterprise"),
            ),
            B.tip("\"Deploy the gateway on-premises or in another cloud\" is the self-hosted gateway, Premium (or Developer for testing). \"Multi-region active deployment\" is Premium."),
            B.h("Policies: where the exam lives"),
            B.p("Policies are XML applied at a scope: global, product, API, or operation. Four sections run in order: inbound (before the back end), backend, outbound (after the back end), on-error. The <base /> element pulls in the policies of the wider scope."),
            B.code("xml", """
                <policies>
                  <inbound>
                    <base />
                    <validate-jwt header-name="Authorization" failed-validation-httpcode="401">
                      <openid-config url="https://login.microsoftonline.com/{tenant}/v2.0/.well-known/openid-configuration" />
                      <audiences><audience>api://tt-api</audience></audiences>
                    </validate-jwt>
                    <rate-limit-by-key calls="60" renewal-period="60" counter-key="@(context.Request.IpAddress)" />
                    <quota-by-key calls="10000" renewal-period="86400" counter-key="@(context.Subscription.Id)" />
                    <set-header name="x-correlation-id" exists-action="skip"><value>@(Guid.NewGuid().ToString())</value></set-header>
                    <cache-lookup vary-by-developer="false" vary-by-developer-groups="false" downstream-caching-type="none" />
                    <set-backend-service base-url="https://tt-api.azurewebsites.net" />
                  </inbound>
                  <backend><base /></backend>
                  <outbound>
                    <base />
                    <cache-store duration="300" />
                    <set-header name="x-powered-by" exists-action="delete" />
                    <xml-to-json kind="direct" apply="always" consider-accept-header="false" />
                  </outbound>
                  <on-error><base /></on-error>
                </policies>
            """),
            B.table(
                listOf("Policy", "Does", "Typical question"),
                listOf("validate-jwt", "Checks an Entra ID (or other) token", "Require a token with a given audience"),
                listOf("rate-limit / rate-limit-by-key", "Limits calls per short window", "60 calls a minute per IP"),
                listOf("quota / quota-by-key", "Limits calls or bandwidth per long window", "10,000 a day per subscription"),
                listOf("ip-filter", "Allow or deny IP ranges", "Only the office network"),
                listOf("check-header", "Requires a header with allowed values", "A version header"),
                listOf("set-header / set-query-parameter", "Add, change or delete", "Remove x-powered-by"),
                listOf("rewrite-uri", "Change the back-end path", "Expose /v2/logs as /logs"),
                listOf("set-backend-service", "Route to a different back end", "A/B or canary"),
                listOf("cache-lookup / cache-store", "Response caching", "Cache GET for 5 minutes"),
                listOf("mock-response", "Return a fake response", "Front-end work before the back end exists"),
                listOf("cors", "Add CORS headers", "Browser callers"),
                listOf("xml-to-json / json-to-xml", "Convert bodies", "Legacy back end"),
                listOf("return-response", "Short-circuit with a response", "Maintenance message"),
                listOf("retry", "Retry the back end on a condition", "Transient 5xx"),
            ),
            B.remember("rate-limit is a short burst control (per minute, 429 Too Many Requests). quota is a long-term allowance (per day or month, 403 Forbidden). Both can be keyed by any expression with the -by-key variants."),
            B.check("az-apim-k1", "az-apim", "Partners may call at most 1,000 times a month each; bursts are fine. Which policy?",
                listOf("rate-limit-by-key", "quota-by-key", "ip-filter"), 1, "A monthly allowance is a quota; rate-limit controls short windows."),
            B.check(
                "az-apim-k2", "az-apim",
                "Callers must be held to ten calls a minute, and told to slow down rather than refused outright. Which policy?",
                listOf("rate-limit, which answers 429", "quota, which answers 403", "validate-jwt, which answers 401"), 0,
                "rate-limit smooths bursts and answers 429 Too Many Requests. A quota is a longer allowance, and going over it is a refusal.",
                whyWrong = mapOf(
                    1 to "A quota is an allowance over a longer period, and a caller past it gets 403 — a refusal, not an invitation to slow down.",
                    2 to "validate-jwt checks the token. It has nothing to say about how often a caller calls.",
                ),
            ),
            B.h("Policy expressions"),
            B.p("Anything in @(...) is a C# expression with the context object: context.Request, context.Response, context.Subscription, context.User, context.Variables. Multi-statement blocks use @{ ... }."),
            B.code("xml", """
                <choose>
                  <when condition="@(context.Request.Headers.GetValueOrDefault("x-client","") == "android")">
                    <set-backend-service base-url="https://tt-api-mobile.azurewebsites.net" />
                  </when>
                  <otherwise><set-backend-service base-url="https://tt-api.azurewebsites.net" /></otherwise>
                </choose>
            """),
            B.check(
                "az-apim-k3", "az-apim",
                "An operation-level policy leaves <base /> out of its inbound section. What happens?",
                listOf("The product and global inbound policies do not run", "The policy is rejected when you save it", "Those policies run twice"), 0,
                "<base /> is where the parent scope's policies execute. Leave it out and that scope is skipped entirely.",
                whyWrong = mapOf(
                    1 to "It saves perfectly happily, which is what makes this easy to miss until something is unprotected.",
                    2 to "Running twice is what happens if <base /> appears more than once, not if it is missing.",
                ),
            ),
            B.h("Securing the back end"),
            B.bullets(
                "Subscription keys authenticate the caller to APIM, not APIM to the back end.",
                "Back-end authentication options: client certificate (authentication-certificate), managed identity token (authentication-managed-identity), or Basic (authentication-basic).",
                "Restrict the back end to accept only APIM: IP restriction, VNet, or require the certificate.",
            ),
            B.worked("""
                Worked example. Two Tracks exposes a Logs API to a partner tutoring service. APIM validates the
                partner's Entra token, enforces 60 calls a minute per subscription, caches GET /levels for five
                minutes, and strips internal headers on the way out. The back end on App Service only accepts
                traffic from APIM's IP and a managed-identity token, so nobody can skip the gateway.
            """),
            B.steps("Publish an API",
                "az apim create --name tt-apim --resource-group tt-rg --publisher-name TwoTracks --publisher-email ops@example.com --sku-name Consumption" to "Consumption deploys in minutes.",
                "Import the back end's OpenAPI document as an API." to "Operations are created from the spec.",
                "Add the API to a product and set Requires subscription." to "Developers get keys through the portal.",
                "Edit the inbound policy to add rate-limit and validate-jwt." to "Test with the built-in test console; the trace shows each policy.",
                "Call with the Ocp-Apim-Subscription-Key header." to "Or api-key in the query string.",
            ),
            B.mistakes(
                "Putting rate-limit at the operation level but forgetting <base /> so global policies stop running.",
                "Confusing quota (long window, 403) with rate-limit (short window, 429).",
                "Leaving the back end publicly reachable so callers bypass APIM.",
                "Choosing Consumption and then needing the developer portal or a VNet.",
            ),
            B.example("Two Tracks: Consumption tier, validate-jwt plus rate-limit-by-key per subscription, cache-store on GET levels, managed-identity authentication to the back end."),
            B.h("Check yourself"),
            B.match(
                "az-n-apim-match", "A policy has four sections. Pair each with when it runs.",
                "inbound" to "Before the request reaches the back end",
                "backend" to "The call to the back end itself",
                "outbound" to "After the back end has replied",
                "on-error" to "When something in the pipeline throws",
            ),
            B.gap(
                "az-n-apim-gap1", "A short burst control, counted per minute.",
                "rate-limit rejects with ___ Too Many Requests.", "429",
            ),
            B.gap(
                "az-n-apim-gap2", "A long-term allowance, counted per day or month.",
                "quota rejects with ___ Forbidden.", "403",
            ),
            B.gap(
                "az-n-apim-gap3", "The question says the gateway must run on-premises or in another cloud.",
                "That is the self-hosted gateway, which needs the ___ tier.", "Premium", "premium",
            ),
            B.tryIt("Try it in 20 minutes", "Consumption tier is free for the first million calls each month.",
                "Create a Consumption APIM", "Import https://conferenceapi.azurewebsites.net?format=json (the sample API)", "Add a rate-limit of 3 calls per 60 seconds", "Call four times from the test console and read the 429 and the trace",
            ),
            B.explain(
                "az-apim-x1",
                "Explain what a policy is in API Management, and the difference between rate-limit and quota.",
                "A policy is an XML statement the gateway runs against a call, in four sections: inbound, backend, outbound and on-error. rate-limit smooths bursts over a short window and answers 429 Too Many Requests, telling a caller to slow down. A quota is an allowance over a longer period, and a caller past it gets 403: a refusal rather than a delay. The base element is where the parent scope's policies run, so leaving it out silently skips them.",
                "A policy runs in the gateway, in sections" to listOf("policy", "gateway", "inbound", "outbound", "xml"),
                "rate-limit answers 429" to listOf("rate-limit", "rate limit", "429", "too many requests"),
                "A quota is an allowance and answers 403" to listOf("quota", "403", "allowance", "forbidden"),
            ),
            B.glossary(
                "Gateway" to "The APIM component that receives calls and runs policies.",
                "Product" to "A bundle of APIs with terms, quotas and subscription rules.",
                "Subscription key" to "The key a caller sends in Ocp-Apim-Subscription-Key.",
                "Policy" to "An XML statement applied inbound, backend, outbound or on-error.",
                "Policy expression" to "C# inside @( ) using the context object.",
                "Self-hosted gateway" to "The APIM gateway container run on your own infrastructure.",
            ),
            B.recap(
                "APIs, operations, products, subscriptions and groups; the gateway runs policies in four sections.",
                "rate-limit for bursts (429), quota for allowances (403), validate-jwt for tokens, cache for speed; <base /> keeps parents running.",
                "Secure the back end so only APIM can reach it; Consumption for serverless, Premium for VNet and multi-region.",
            ),
        ),
    )

    private val eventGrid = Topic(
        id = "az-eventgrid", title = "Event Grid: reacting to things that happened", minutes = 26,
        references = listOf(
            B.ref("What is Azure Event Grid?", LEARN + "azure/event-grid/overview"),
            B.ref("Event Grid event schema and CloudEvents", LEARN + "azure/event-grid/event-schema"),
            B.ref("Event filtering for Event Grid subscriptions", LEARN + "azure/event-grid/event-filtering"),
            B.ref("Webhook event delivery and validation", LEARN + "azure/event-grid/webhook-event-delivery"),
            B.ref("Delivery and retry", LEARN + "azure/event-grid/delivery-and-retry"),
            B.ref("Learn module: Explore Azure Event Grid", LEARN + "training/modules/azure-event-grid/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-eventgrid-w1", "az-eventgrid",
                "Event Grid delivers an event to your webhook. How many times do you think it may arrive?",
                listOf("At least once, so possibly more", "Exactly once, guaranteed", "At most once, and it may be lost"), 0,
                "Whatever it is, it decides something about how your handler has to be written.",
            ),
            B.plain("""
                An event says "this happened": a blob was created, a resource was deleted, a user finished a lesson.
                Event Grid routes events from sources to handlers with filters, retries and no polling. It is push,
                near real time, and you pay per million operations.
            """),
            B.diagram(
                caption = "Sources publish to a topic. Each subscription filters and pushes to one handler. Dead-lettered events go to a storage container.",
                nodes = listOf(
                    DNode("src", "Blob Storage", 0.12f, 0.3f, w = 0.2f, h = 0.18f, tone = 1, sub = "system topic"),
                    DNode("src2", "Two Tracks API", 0.12f, 0.7f, w = 0.2f, h = 0.18f, tone = 1, sub = "custom topic"),
                    DNode("topic", "Event Grid topic", 0.45f, 0.5f, w = 0.26f, h = 0.3f, kind = NodeKind.CLOUD),
                    DNode("h1", "Function", 0.8f, 0.2f, w = 0.22f, h = 0.16f, tone = 2),
                    DNode("h2", "Webhook (API)", 0.8f, 0.5f, w = 0.22f, h = 0.16f, tone = 2),
                    DNode("h3", "Service Bus queue", 0.8f, 0.8f, w = 0.24f, h = 0.16f, tone = 2),
                ),
                edges = listOf(DEdge("src", "topic"), DEdge("src2", "topic"), DEdge("topic", "h1", label = "filter: .csv"), DEdge("topic", "h2"), DEdge("topic", "h3")),
                aspect = 0.5f,
            ),
            B.h("Vocabulary"),
            B.table(
                listOf("Term", "Meaning"),
                listOf("Event", "The smallest unit: what happened, where, when, plus a small data payload (64 KB free, up to 1 MB)"),
                listOf("Event source", "Where it came from: Storage, Resource Groups, IoT Hub, Key Vault, your app"),
                listOf("Topic", "The endpoint events are sent to. System topics for Azure services; custom topics for your app; partner topics for SaaS"),
                listOf("Event subscription", "Which events from a topic go to which handler, with filters"),
                listOf("Event handler", "The destination: Functions, Logic Apps, webhooks, Service Bus, Event Hubs, Storage Queues, Automation"),
                listOf("Domain", "Thousands of topics under one endpoint for multi-tenant publishing"),
                listOf("Namespace topics", "The newer resource supporting pull delivery and MQTT"),
            ),
            B.h("The event schema"),
            B.code("json", """
                [{
                  "id": "9b2f...", "topic": "/subscriptions/.../storageAccounts/ttstorage",
                  "subject": "/blobServices/default/containers/exports/blobs/2026-10-06.csv",
                  "eventType": "Microsoft.Storage.BlobCreated",
                  "eventTime": "2026-10-06T22:00:01Z",
                  "data": { "api": "PutBlob", "contentType": "text/csv", "contentLength": 4096, "url": "https://ttstorage.blob.core.windows.net/exports/2026-10-06.csv" },
                  "dataVersion": "1", "metadataVersion": "1"
                }]
            """),
            B.bullets(
                "Event Grid schema (above) or CloudEvents 1.0 (type, source, id, time, data); pick per topic and per subscription.",
                "Filters: by eventType, by subject prefix or suffix, or advanced filters on data fields (NumberGreaterThan, StringContains, up to 25 conditions).",
                "Delivery: at least once, with retries over 24 hours using exponential backoff. Configure max attempts and event time to live. Dead-letter to a blob container after that.",
            ),
            B.tip("\"Only CSV files uploaded to the exports container\" is a subject filter: begins with /blobServices/default/containers/exports and ends with .csv."),
            B.check("az-eventgrid-k1", "az-eventgrid", "Events that could not be delivered after 24 hours must be kept for inspection. What do you configure?",
                listOf("A longer retry", "Dead-lettering to a storage container", "A second subscription"), 1, "Dead-letter destinations keep undeliverable events as blobs."),
            B.check(
                "az-eventgrid-k2", "az-eventgrid",
                "You want only the blob events under /containers/images/. What do you configure?",
                listOf("A subject begins-with filter on the event subscription", "A policy on the topic", "A check at the top of the handler"), 0,
                "Subscriptions filter by event type, by subject prefix and suffix, or on the data itself, so unwanted events are never delivered.",
                whyWrong = mapOf(
                    1 to "Topics do not filter. The subscription is where a route is narrowed.",
                    2 to "That works, but you have already paid for the delivery. Not receiving them is the point of a subject filter.",
                ),
            ),
            B.h("Webhook validation"),
            B.p("""
                Before Event Grid trusts a webhook it sends a SubscriptionValidationEvent with a validationCode. Your
                endpoint must echo it back in a validationResponse (synchronous handshake), or you visit the
                validationUrl within 5 minutes (manual handshake). Functions with an Event Grid trigger handle this for you.
            """),
            B.code("csharp", """
                [HttpPost("events")]
                public IActionResult Receive([FromBody] EventGridEvent[] events)
                {
                    foreach (var e in events)
                    {
                        if (e.TryGetSystemEventData(out var sys) && sys is SubscriptionValidationEventData v)
                            return Ok(new SubscriptionValidationResponse { ValidationResponse = v.ValidationCode });
                        if (e.EventType == "Microsoft.Storage.BlobCreated")
                            _imports.Enqueue(e.Data.ToObjectFromJson<StorageBlobCreatedEventData>().Url);
                    }
                    return Ok();
                }
            """),
            B.check(
                "az-eventgrid-k3", "az-eventgrid",
                "Your handler occasionally receives the same event twice. What should it do about it?",
                listOf("Be idempotent, so the same event twice has the same effect as once", "Reject the duplicate with a 409", "Turn retries off on the subscription"), 0,
                "Event Grid delivers at least once and retries for 24 hours, so a handler has to tolerate a repeat.",
                whyWrong = mapOf(
                    1 to "A 409 is a failure, so Event Grid retries it. That makes the duplicate worse rather than better.",
                    2 to "Retries are what get an event delivered when the handler was briefly down. Without them, an outage loses events.",
                ),
            ),
            B.h("Publishing your own events"),
            B.code("csharp", """
                var client = new EventGridPublisherClient(new Uri("https://tt-events.westeurope-1.eventgrid.azure.net/api/events"), new DefaultAzureCredential());
                await client.SendEventAsync(new EventGridEvent(
                    subject: "lessons/az-apim", eventType: "TwoTracks.LessonFinished", dataVersion: "1.0",
                    data: new { owner = "usaid", topicId = "az-apim", minutes = 30 }));
            """),
            B.remember("Event Grid is for discrete events and reacting; Event Hubs is for streams of telemetry; Service Bus is for commands and messages that must be processed. The next topic drills this."),
            B.worked("""
                Worked example. When the nightly export lands in the exports container, Blob Storage raises
                BlobCreated. A subscription filtered to .csv pushes it to the import Function, which parses the file.
                A second subscription sends every event to a Service Bus queue for the audit service. A corrupted file
                that makes the Function throw is retried, then dead-lettered to the deadletter container after 24 hours.
            """),
            B.steps("Subscribe a Function to blob events",
                "az eventgrid system-topic create --name ttstorage-events --resource-group tt-rg --source <storage-id> --topic-type Microsoft.Storage.StorageAccounts --location westeurope" to "A system topic for the account.",
                "az eventgrid system-topic event-subscription create --name csv-import --resource-group tt-rg --system-topic-name ttstorage-events --endpoint-type azurefunction --endpoint <function-id> --included-event-types Microsoft.Storage.BlobCreated --subject-begins-with /blobServices/default/containers/exports --subject-ends-with .csv --deadletter-endpoint <container-id>" to "Filtered and dead-lettered.",
                "Upload a CSV and watch the Function log." to "Delivery within seconds.",
            ),
            B.mistakes(
                "Returning 200 before the handshake; the subscription stays unvalidated.",
                "Expecting exactly-once delivery; make handlers idempotent using the event id.",
                "Using Event Grid for high-volume telemetry; that is Event Hubs.",
                "Filtering in the handler when a subject filter would stop the delivery altogether.",
            ),
            B.example("Two Tracks: system topic on the storage account, custom topic for LessonFinished, Function and Service Bus handlers, dead-letter container."),
            B.h("Check yourself"),
            B.match(
                "az-n-grid-match", "The exam's favourite confusion. Pair each service with its job.",
                "Event Grid" to "Discrete events you react to",
                "Event Hubs" to "Streams of telemetry at scale",
                "Service Bus" to "Commands and messages that must be handled",
                "Queue Storage" to "A simple queue that can grow past 80 GB",
            ),
            B.gap(
                "az-n-grid-gap1", "Only CSV files uploaded to the exports container should fire the handler.",
                "Set subject ends with ___.", ".csv", "csv",
            ),
            B.gap(
                "az-n-grid-gap2", "The other half of the same filter.",
                "Subject begins with /blobServices/default/containers/___", "exports",
            ),
            B.tryIt("Try it in 15 minutes", "Event Grid's first 100,000 operations a month are free.",
                "Create a storage account and an Event Grid trigger Function", "Add a system topic subscription for BlobCreated", "Upload a file and read the event JSON in the Function log", "Add a subject suffix filter for .png and upload a .txt to see it skipped",
            ),
            B.explain(
                "az-eventgrid-x1",
                "Explain how an event gets from an Azure service to your handler, and why your handler has to be idempotent.",
                "A source publishes to a topic, either a system topic Azure creates for it or a custom topic your app writes to. An event subscription is a filtered route from that topic to one handler, filtering by event type, by subject prefix or suffix, or on the data. Delivery is at least once, with retries for 24 hours and dead-lettering for what never lands, so the same event can arrive twice and the handler has to give the same result both times.",
                "Sources publish to topics" to listOf("topic", "topics", "system topic", "custom topic", "publish"),
                "Subscriptions filter and route" to listOf("subscription", "filter", "filters", "route", "subject"),
                "At-least-once delivery means duplicates" to listOf("at least once", "at-least-once", "twice", "duplicate", "retry", "retries"),
            ),
            B.glossary(
                "System topic" to "A topic Azure creates for events from an Azure service.",
                "Custom topic" to "A topic your application publishes to.",
                "Event subscription" to "A filtered route from a topic to one handler.",
                "Dead-letter" to "Storage of events that could not be delivered.",
                "Subject filter" to "Begins-with and ends-with filtering on the subject field.",
                "Validation handshake" to "The proof that a webhook endpoint wants to receive events.",
            ),
            B.recap(
                "Sources publish to system or custom topics; subscriptions filter and push to handlers.",
                "Filter by type, subject or data; at-least-once delivery with 24-hour retries and dead-lettering.",
                "Webhooks must answer the validation handshake; handlers should be idempotent.",
            ),
        ),
    )

    val topics = listOf(apim, eventGrid)
}
