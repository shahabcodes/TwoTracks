package com.shahaabapps.core.content

/** AZ-305 topics, written from the official study guide and Microsoft Learn (Az305). */
internal object Az305Identity {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val monitoring = Topic(
        id = "sa-monitoring", title = "Logging and monitoring: where logs go and who watches", minutes = 30,
        references = listOf(
            B.ref("Azure Monitor overview", LEARN + "azure/azure-monitor/overview"),
            B.ref("Diagnostic settings in Azure Monitor", LEARN + "azure/azure-monitor/essentials/diagnostic-settings"),
            B.ref("Design a Log Analytics workspace architecture", LEARN + "azure/azure-monitor/logs/workspace-design"),
            B.ref("Azure Monitor activity log", LEARN + "azure/azure-monitor/essentials/activity-log"),
            B.ref("What are Azure Monitor alerts?", LEARN + "azure/azure-monitor/alerts/alerts-overview"),
            B.ref("Application Insights overview", LEARN + "azure/azure-monitor/app/app-insights-overview"),
            B.ref("What is Microsoft Sentinel?", LEARN + "azure/sentinel/overview"),
            B.ref("Learn module: Design a solution to log and monitor Azure resources", LEARN + "training/modules/design-solution-to-log-monitor-azure-resources/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-monitoring-w1", "sa-monitoring",
                "Two Tracks stores secrets in Key Vault. Where do the records of who read each secret appear by default?",
                listOf("Nowhere, until a diagnostic setting sends them somewhere", "In the Activity log", "In Application Insights"), 0,
                "Some logs Azure keeps on its own; others it only collects when you ask.",
            ),
            B.plain("""
                Azure Monitor is the control room for everything you run in Azure: it collects numbers and
                records from your resources and your code, keeps them, and raises the alarm when something looks
                wrong. Picture a building's security desk: cameras feed one wall of screens, a guard watches, and a
                phone list says who to ring at 3 a.m. Your design decides three things: which records to collect,
                where to keep them, and who gets told.
            """),
            B.diagram(
                caption = "Diagnostic settings route resource logs and the Activity log to a workspace, a storage account or Event Hubs; Application Insights writes to the workspace.",
                nodes = listOf(
                    DNode("res", "Key Vault", 0.15f, 0.2f, sub = "resource logs"),
                    DNode("sub", "Subscription", 0.15f, 0.5f, sub = "Activity log"),
                    DNode("app", "App Insights", 0.15f, 0.8f, tone = 1, sub = "app telemetry"),
                    DNode("ds", "Diagnostic settings", 0.5f, 0.35f, tone = 2),
                    DNode("law", "Log Analytics", 0.84f, 0.2f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, tone = 1, sub = "query and alert"),
                    DNode("st", "Storage account", 0.84f, 0.5f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "cheap archive"),
                    DNode("eh", "Event Hubs", 0.85f, 0.8f, sub = "stream to a SIEM"),
                ),
                edges = listOf(
                    DEdge("res", "ds"),
                    DEdge("sub", "ds"),
                    DEdge("ds", "law"),
                    DEdge("ds", "st"),
                    DEdge("ds", "eh"),
                    DEdge("app", "law", label = "workspace-based", dashed = true),
                ),
                aspect = 0.7f,
            ),
            B.h("Four kinds of data"),
            B.table(
                listOf("Data", "In plain words", "Collected?"),
                listOf("Metrics", "Numbers over time, like CPU percent or requests per second", "Automatically, kept 93 days"),
                listOf("Activity log", "Who created, changed or deleted a resource: the management record", "Automatically, kept 90 days"),
                listOf("Resource logs", "What happened inside a resource, like each secret read", "Only once you add a diagnostic setting"),
                listOf("App telemetry", "Requests, errors and slow calls from your own code", "Through Application Insights"),
            ),
            B.p("""
                Logs from inside a virtual machine, such as Windows events or Linux syslog, are different again:
                you install the Azure Monitor Agent on the machine and write a data collection rule, a small
                recipe saying which logs to pick up and where to send them.
            """),
            B.h("Routing logs: diagnostic settings"),
            B.p("""
                A diagnostic setting is a forwarding rule you add to a resource, like a post office redirect: it
                says which log categories to send and to which destinations. One resource can have up to five,
                so audit logs can go one way and noisy logs another. The subscription's Activity log is routed the
                same way. There are three main destinations, and the requirement words point to one.
            """),
            B.compare("Where should the logs go?",
                B.card("Log Analytics workspace", "The logs must be searched, charted or alerted on.", "A database for logs, queried with KQL, the Kusto Query Language", "Alerts, workbooks and Sentinel all read from it", "You pay mostly per GB taken in"),
                B.card("Storage account", "Keep logs for years at the lowest cost, rarely read.", "Cheapest place to park large volumes", "Fits audit and compliance archives", "Hard to search; it is a filing cabinet"),
                B.card("Event Hubs", "Stream logs to another tool, such as an outside SIEM.", "A pipe, not a store", "Near real time", "Another system does the reading"),
            ),
            B.check(
                "sa-monitoring-k1", "sa-monitoring",
                "The security team uses a third-party security tool and wants Key Vault audit logs delivered to it in near real time. What do you recommend?",
                listOf("A diagnostic setting that streams the logs to Event Hubs", "A diagnostic setting that writes them to a storage account", "Application Insights on the vault"), 0,
                "Event Hubs is the streaming destination: another tool reads the logs from it as they arrive.",
                whyWrong = mapOf(
                    1 to "Storage is an archive. The outside tool would have to poll files, which is not near real time.",
                    2 to "Application Insights watches your application code, not a vault's audit records.",
                ),
            ),
            B.h("How many workspaces?"),
            B.p("""
                Microsoft's advice is to start with one workspace and add another only when a requirement forces
                it. One workspace is simpler to query, and more data in one place can reach a commitment tier,
                a discount for promising a daily volume. Access can still be split inside one workspace: by
                default people see the logs of resources they already have rights to, called resource-context
                access, and table-level access can hide whole tables.
            """),
            B.table(
                listOf("Requirement", "Design"),
                listOf("One company, no data residency rules", "One central workspace"),
                listOf("EU logs must stay in the EU", "One workspace per geography with that rule"),
                listOf("Each team sees only its own resources' logs", "Still one workspace, with resource-context access"),
                listOf("Separate tenants, or billing that must be split", "A workspace per tenant or per cost owner"),
            ),
            B.tip("\"Minimise administrative effort\" or \"single pane\" points to fewer workspaces. Only data residency, separate tenants, split billing or different retention for the same table justify more."),
            B.h("Watching: alerts, action groups, workbooks"),
            B.bullets(
                "Application Insights is application performance monitoring: it records requests, failures and dependency calls from your code, and its availability tests ping your site from around the world. New resources are workspace-based, storing data in Log Analytics.",
                "An alert rule watches a condition: a metric over a threshold, a log query that returns rows, or an Activity log event such as a resource being deleted.",
                "An action group is the phone list: email, SMS, push or voice, plus automatic actions like a webhook, a function or a Logic App. Many alert rules share one.",
                "A workbook is an interactive report mixing text, queries and charts, for people to explore.",
                "Microsoft Sentinel is a SIEM, a security information and event management service: it sits on a workspace, hunts for attacks across security logs and can respond automatically.",
            ),
            B.check(
                "sa-monitoring-k2", "sa-monitoring",
                "Twenty alert rules must all text the on-call engineer and call the same webhook. How do you keep this easy to change?",
                listOf("One action group used by all twenty alert rules", "A workbook pinned to a dashboard", "A diagnostic setting on each resource"), 0,
                "An action group is defined once and reused, so changing the phone number is one edit.",
                whyWrong = mapOf(
                    1 to "A workbook shows data to someone who looks; it notifies nobody.",
                    2 to "Diagnostic settings move logs; they do not tell anyone anything.",
                ),
            ),
            B.h("Retention and cost"),
            B.p("""
                A workspace keeps data for about a month by default; you can extend the searchable period, and
                long-term retention keeps it for up to 12 years at a lower price. Cost comes mainly from the GB you
                take in, so send only the categories you need, use cheaper table plans for noisy logs you rarely
                search, and archive old audit logs to storage.
            """),
            B.code("bash", """
                az monitor log-analytics workspace create --resource-group tt-rg --workspace-name tt-logs-eu --location westeurope
                az monitor diagnostic-settings create --name to-logs --resource <keyvault-id> \
                  --workspace <workspace-id> --logs '[{"categoryGroup":"audit","enabled":true}]'
            """),
            B.check(
                "sa-monitoring-k3", "sa-monitoring",
                "Auditors want every Activity log entry kept for seven years at the lowest cost; it will rarely be read. What do you recommend?",
                listOf("A diagnostic setting on the subscription that sends the Activity log to a storage account", "Nothing; the Activity log keeps entries for ever", "Application Insights with a seven-year setting"), 0,
                "The Activity log keeps 90 days on its own; export it, and storage is the cheapest place for rarely read archives.",
                whyWrong = mapOf(
                    1 to "The Activity log keeps only 90 days unless you route it somewhere.",
                    2 to "Application Insights holds app telemetry, not the subscription's management record.",
                ),
            ),
            B.worked("""
                Two Tracks goes global. Requirements: on-call staff must be told within minutes when the API fails;
                logs about EU users must stay in Europe, and Australian ones in Australia; audit logs must be kept
                seven years cheaply. Options: one global workspace, one per team, or one per geography. Choice: two
                workspaces, West Europe and Australia East, because residency forces the split and nothing else
                does. Each regional API has workspace-based Application Insights. Diagnostic settings send audit
                logs to the regional workspace and to a storage account for seven years. Every alert rule uses one
                action group.
            """),
            B.faded(
                "sa-monitoring-f1",
                "Two Tracks adds a payment service in Australia. Apply the same design.",
                listOf(
                    "Connect its Application Insights to the Australia workspace, so its data stays in Australia.",
                    "Add a diagnostic setting that sends its audit logs to that workspace and to a storage account for seven years.",
                    "Create a failure-rate alert rule and point it at the existing ___ group, so the same on-call people are told.",
                ),
                blank = 2,
                answers = listOf("action"),
                note = "An action group is reusable: one list of people and actions shared by every alert rule.",
            ),
            B.steps("Do it for real",
                "az monitor log-analytics workspace create --resource-group tt-rg --workspace-name tt-logs-eu --location westeurope" to "A workspace in Europe; note its id.",
                "az monitor diagnostic-settings create --name to-logs --resource <keyvault-id> --workspace <workspace-id> --logs '[{\"categoryGroup\":\"audit\",\"enabled\":true}]'" to "The vault's audit logs now flow into the workspace.",
                "az monitor action-group create --name tt-oncall --resource-group tt-rg --short-name ttoncall --action email oncall oncall@example.com" to "One reusable list of who to tell.",
                "In the portal, open the workspace, choose Logs and run: AzureDiagnostics | take 10" to "Rows appear a few minutes after someone reads a secret.",
            ),
            B.mistakes(
                "Assuming resource logs are collected on their own; without a diagnostic setting they are lost.",
                "Creating a workspace per team when resource-context access in one workspace would do.",
                "Sending audit logs only to storage when the requirement says they must be queried or alerted on.",
                "Relying on the Activity log for long-term audit; it keeps only 90 days unless exported.",
                "Copying the same phone list into every alert instead of sharing one action group.",
            ),
            B.example("Two Tracks goes global: two regional workspaces for residency, workspace-based Application Insights on each API, diagnostic settings sending audit logs to the workspace and to storage for seven years, and one on-call action group behind every alert."),
            B.h("Check yourself"),
            B.match(
                "sa-x-monitoring-match", "Pair each requirement with the destination or feature.",
                "Query and alert on logs" to "Log Analytics workspace",
                "Archive logs cheaply for years" to "Storage account",
                "Stream logs to an outside SIEM" to "Event Hubs",
                "Notify the same people from many alerts" to "Action group",
            ),
            B.gap(
                "sa-x-monitoring-gap1", "The forwarding rule on a resource.",
                "A ___ setting sends a resource's logs to a workspace, storage or Event Hubs.", "diagnostic", "Diagnostic",
            ),
            B.gap(
                "sa-x-monitoring-gap2", "How long Azure keeps management events on its own.",
                "The Activity log keeps entries for ___ days unless you export them.", "90", "ninety",
            ),
            B.tryIt("Try it in 15 minutes", "Pennies of log ingestion; delete the resource group afterwards.",
                "Create a Log Analytics workspace in your nearest region",
                "Add a diagnostic setting on a Key Vault sending the audit category to it",
                "Read a secret, then find the row with a short KQL query",
                "Create an action group with your email and an alert rule that uses it",
            ),
            B.explain(
                "sa-monitoring-x1",
                "Explain how Two Tracks should collect, keep and watch its logs.",
                "Diagnostic settings route resource logs and the Activity log, because resource logs are not collected otherwise. Logs that must be queried go to a Log Analytics workspace, one per geography only because of data residency. Audit logs are also archived to a storage account for seven years at low cost. Alert rules watch for failures and share one action group, so the on-call engineer gets a text.",
                "Diagnostic settings route the logs" to listOf("diagnostic setting", "diagnostic settings", "routing", "route"),
                "Few workspaces, split only when required" to listOf("log analytics", "workspace"),
                "Cheap long-term archive" to listOf("storage account", "archive", "archiving"),
                "Alerts and action groups" to listOf("action group", "alert rule", "alerts"),
            ),
            B.glossary(
                "Azure Monitor" to "The service that collects, stores and alerts on data from everything in Azure.",
                "Log Analytics workspace" to "A store for logs that you search with KQL and alert on.",
                "Diagnostic setting" to "A forwarding rule that sends a resource's logs to a workspace, storage or Event Hubs.",
                "Activity log" to "The subscription's record of who changed which resource; kept 90 days.",
                "Application Insights" to "Monitoring for your own code: requests, failures, slow calls.",
                "Action group" to "A reusable list of who to notify and what to run when an alert fires.",
                "Microsoft Sentinel" to "A security service on top of a workspace that detects and responds to attacks.",
            ),
            B.recap(
                "Diagnostic settings route logs: workspace to query and alert, storage to archive, Event Hubs to stream.",
                "Start with one workspace; split only for residency, tenants, billing or different retention.",
                "Alert rules decide when, action groups decide who; Application Insights watches your code.",
            ),
        ),
    )

    private val identity = Topic(
        id = "sa-identity", title = "Authentication and identity management: one sign-in for everyone", minutes = 30,
        references = listOf(
            B.ref("Choose the right authentication method for your hybrid identity solution", LEARN + "entra/identity/hybrid/connect/choose-ad-authn"),
            B.ref("What is Microsoft Entra Cloud Sync?", LEARN + "entra/identity/hybrid/cloud-sync/what-is-cloud-sync"),
            B.ref("What is Conditional Access?", LEARN + "entra/identity/conditional-access/overview"),
            B.ref("Passwordless authentication options for Microsoft Entra ID", LEARN + "entra/identity/authentication/concept-authentication-passwordless"),
            B.ref("B2B collaboration overview", LEARN + "entra/external-id/what-is-b2b"),
            B.ref("External ID overview", LEARN + "entra/external-id/external-identities-overview"),
            B.ref("What are managed identities for Azure resources?", LEARN + "entra/identity/managed-identities-azure-resources/overview"),
            B.ref("Learn module: Design authentication and authorization solutions", LEARN + "training/modules/design-authentication-authorization-solutions/"),
        ),
        blocks = listOf(
            B.warmup(
                "sa-identity-w1", "sa-identity",
                "Two Tracks office staff already sign in with on-premises Active Directory. What is Microsoft's default advice for using the same password in the cloud?",
                listOf("Password hash sync", "Federation with AD FS", "A second set of cloud-only accounts"), 0,
                "Pick the option with the least to build and keep running.",
            ),
            B.plain("""
                Microsoft Entra ID is the company's ID-card office in the cloud: it holds every person and app,
                checks who they are when they sign in, and hands out a token, a short-lived pass that apps and
                Azure accept. One identity means one sign-in for email, internal apps and the Azure portal.
                Your design decides where identities come from, how strongly sign-in is checked, how outsiders
                get in, and how apps sign in without passwords.
            """),
            B.diagram(
                caption = "Office users are synced into Entra ID; every sign-in passes Conditional Access before a token reaches the apps.",
                nodes = listOf(
                    DNode("ad", "On-prem AD", 0.16f, 0.25f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "office users"),
                    DNode("sync", "Entra Connect", 0.5f, 0.25f, tone = 2, sub = "or Cloud Sync"),
                    DNode("entra", "Entra ID", 0.85f, 0.25f, tone = 1, sub = "the tenant"),
                    DNode("user", "Staff and guests", 0.15f, 0.62f, w = 0.24f, h = 0.18f, kind = NodeKind.USER),
                    DNode("ca", "Conditional Access", 0.5f, 0.62f, w = 0.3f, tone = 2),
                    DNode("apps", "Apps and Azure", 0.85f, 0.62f, sub = "one sign-in"),
                    DNode("note", "Managed identities: apps sign in with no password", 0.5f, 0.88f, w = 0.9f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("ad", "sync", label = "sync"),
                    DEdge("sync", "entra"),
                    DEdge("user", "ca", label = "sign in"),
                    DEdge("entra", "ca", label = "policy", dashed = true),
                    DEdge("ca", "apps", label = "token"),
                ),
                aspect = 0.7f,
            ),
            B.h("Tenants, users and groups"),
            B.p("""
                A tenant is one organisation's own copy of Entra ID, its directory; most companies need just one.
                Inside it live users and groups. Give access to groups, never to single people: when someone joins
                or leaves the team, you change the group, not twenty role assignments. A dynamic group fills itself
                from a rule, such as everyone whose department is Finance.
            """),
            B.h("Proving it is you: MFA, Conditional Access, passwordless"),
            B.p("""
                MFA, multifactor authentication, asks for a second proof, such as an app prompt on your phone,
                so a stolen password alone is not enough. Conditional Access decides when to ask: it is a set of
                if-then rules. If the user is an admin, or signs in from an unknown country, then require MFA, a
                managed device, or block. It needs an Entra ID P1 licence; risk-based rules use ID Protection,
                which needs P2.
            """),
            B.table(
                listOf("Requirement", "Recommend"),
                listOf("Every admin must use a second factor", "Conditional Access policy requiring MFA for admin roles"),
                listOf("Block sign-ins from countries we do not work in", "Conditional Access with named locations"),
                listOf("Ask for MFA only when a sign-in looks risky", "Risk-based Conditional Access with ID Protection (P2)"),
                listOf("Tiny company, free licences, wants basic MFA", "Security defaults, switched on for the whole tenant"),
                listOf("Staff should not type passwords at all", "Passwordless: Windows Hello for Business, passkeys or Microsoft Authenticator"),
            ),
            B.check(
                "sa-identity-k1", "sa-identity",
                "Two Tracks wants MFA for anyone opening the Azure portal from outside the office, but not inside it. What do you recommend?",
                listOf("A Conditional Access policy with the office as a named location", "Security defaults", "Turning on MFA for each user one by one"), 0,
                "Conditional Access can take the location into account; the office network becomes a trusted named location.",
                whyWrong = mapOf(
                    1 to "Security defaults are all or nothing; they cannot make exceptions for a location.",
                    2 to "Per-user MFA prompts everywhere and is hard to manage; it has no location condition.",
                ),
            ),
            B.h("Hybrid identity: one password for office and cloud"),
            B.p("""
                Hybrid identity means the same people exist on-premises and in the cloud. A sync tool copies users
                and groups from Active Directory into Entra ID: Entra Connect Sync, a server you install, or Cloud
                Sync, lightweight agents with the settings kept in the cloud. Cloud Sync is the simpler choice for
                new set-ups and handles several disconnected forests, such as after a takeover. Then you choose
                where the password is checked.
            """),
            B.compare("How is the password checked?",
                B.card("Password hash sync", "The default: least to run, works even if the office is down.", "A hash of the hash is copied to the cloud", "Entra ID checks the password itself", "Enables leaked-credential detection"),
                B.card("Pass-through authentication", "On-premises rules like sign-in hours or lockout must apply at once.", "Agents in the office check with AD", "No password data in the cloud", "Run three agents for availability"),
                B.card("Federation (AD FS)", "Only when Entra cannot do it: third-party MFA, smart card systems you already run.", "Sign-in handed to your own servers", "Server farm and perimeter proxies", "Most effort and most to fail"),
            ),
            B.tip("Microsoft recommends turning on password hash sync whatever you choose: it is the backup if the office servers fail, and ID Protection needs it to spot leaked passwords."),
            B.check(
                "sa-identity-k2", "sa-identity",
                "Policy says a disabled or locked-out office account must be refused in the cloud immediately, and no servers may be exposed to the internet. Which sign-in method?",
                listOf("Pass-through authentication", "Password hash sync only", "Federation with AD FS"), 0,
                "Pass-through agents check each sign-in against AD, using outbound connections only.",
                whyWrong = mapOf(
                    1 to "Hash sync learns about a disabled account only at the next sync, and lockout state is not synced.",
                    2 to "Federation also enforces it, but needs proxy servers reachable from the internet.",
                ),
            ),
            B.h("People from outside: guests and customers"),
            B.table(
                listOf("Who", "Recommend"),
                listOf("A partner or contractor with their own work account", "B2B collaboration: invite them as a guest user"),
                listOf("Customers signing up to your app with email or a social account", "Microsoft Entra External ID in an external tenant"),
            ),
            B.p("""
                B2B means business to business: the guest signs in with their own company's account, so you never
                manage their password, and you can still apply Conditional Access to them. External ID is for the
                public: your app's own sign-up page, branded, built to hold millions of customers apart from staff.
                Azure AD B2C is the older product for this; new designs use External ID.
            """),
            B.h("Apps signing in: managed identities and service principals"),
            B.bullets(
                "A service principal is an app's identity in a tenant. Signing in as it needs a secret or certificate that someone must store and renew.",
                "A managed identity is a service principal that Azure looks after: no secret exists, so none can leak. System-assigned lives and dies with one resource; user-assigned is its own resource, shareable.",
                "A pipeline outside Azure, such as GitHub Actions, can use workload identity federation: it trusts the pipeline's own token, so again no stored secret.",
            ),
            B.code("bash", """
                az webapp identity assign --name tt-api --resource-group tt-rg
                az role assignment create --assignee <principalId> --role "Storage Blob Data Reader" --scope <storage-id>
            """),
            B.check(
                "sa-identity-k3", "sa-identity",
                "The Two Tracks API on App Service must read blobs. Requirement: no credentials to store or rotate. What do you recommend?",
                listOf("Turn on its managed identity and grant it a storage data role", "A service principal with a client secret in app settings", "The storage account key in a configuration file"), 0,
                "A managed identity has no secret at all; Azure handles the token.",
                whyWrong = mapOf(
                    1 to "A client secret is exactly a credential to store and rotate.",
                    2 to "An account key is a master password to the whole account; it must be stored and rotated.",
                ),
            ),
            B.worked("""
                Two Tracks goes global. Requirements: office staff keep one password; any admin uses MFA; a
                translation agency in Australia edits content; app users sign up with email or Google; the backend
                reads storage without secrets. Choice: Cloud Sync with password hash sync, because it is simplest
                and survives an office outage. Conditional Access requires MFA for admins and outside the office.
                Translators come in as B2B guests. App users live in an External ID tenant. The API uses a managed
                identity with a narrow data role.
            """),
            B.faded(
                "sa-identity-f1",
                "Two Tracks buys a small studio with its own Active Directory forest. Bring its staff in the same way.",
                listOf(
                    "Install Cloud Sync agents in the studio's network, so its users appear in the Two Tracks tenant.",
                    "Turn on password ___ sync, so they keep their passwords and leaked ones can be detected.",
                    "Add the new users to the groups the MFA Conditional Access policy already targets.",
                ),
                blank = 1,
                answers = listOf("hash"),
                note = "Password hash sync is the recommended default and the one ID Protection needs for leaked credentials.",
            ),
            B.steps("Do it for real",
                "az ad group create --display-name tt-backend-admins --mail-nickname tt-backend-admins" to "A security group; give roles to it, not to people.",
                "az role assignment create --assignee <group-object-id> --role Contributor --scope /subscriptions/<sub-id>/resourceGroups/tt-rg" to "Everyone in the group can manage the resource group.",
                "az webapp identity assign --name tt-api --resource-group tt-rg" to "Returns the principalId of a system-assigned managed identity.",
                "az role assignment create --assignee <principalId> --role \"Storage Blob Data Reader\" --scope <storage-id>" to "The API reads blobs with no secret anywhere.",
                "In the Entra admin center, create a Conditional Access policy requiring MFA for admin roles, in report-only mode" to "Sign-in logs show who it would affect before you switch it on.",
            ),
            B.mistakes(
                "Choosing federation because it sounds enterprise, when nothing requires it and hash sync is simpler.",
                "Skipping password hash sync with pass-through, losing the backup and leaked-credential detection.",
                "Creating cloud accounts and passwords for partners instead of inviting them as B2B guests.",
                "Storing a service principal secret in app settings when a managed identity would need none.",
                "Assigning roles to individual users instead of to groups, so every change is many edits.",
            ),
            B.example("Two Tracks goes global: one tenant, Cloud Sync with password hash sync, Conditional Access for MFA, B2B guests for the translation agency, External ID for app users, and managed identities for every backend service."),
            B.h("Check yourself"),
            B.match(
                "sa-x-identity-match", "Pair each requirement with the recommendation.",
                "Partner signs in with their own work account" to "B2B guest",
                "Customers sign up with a social account" to "External ID",
                "MFA only from outside the office" to "Conditional Access",
                "App reads storage with no secret" to "Managed identity",
            ),
            B.gap(
                "sa-x-identity-gap1", "The sign-in method that checks each password against AD through agents.",
                "That is ___ authentication.", "pass-through", "Pass-through", "passthrough",
            ),
            B.tryIt("Try it in 15 minutes", "Free with any Entra tenant; delete what you create afterwards.",
                "Create a security group and add yourself to it",
                "Invite a second email address of yours as a B2B guest and accept the invitation",
                "Turn on the managed identity of an App Service or Function app",
                "Draft a Conditional Access policy in report-only mode, if your tenant has P1",
            ),
            B.explain(
                "sa-identity-x1",
                "Explain how Two Tracks gives staff, partners, customers and apps one secure way to sign in.",
                "Office users are synced with Cloud Sync and password hash sync, the simplest method, which also backs up sign-in and detects leaked passwords. Conditional Access requires MFA for admins and for sign-ins outside the office. Partners come in as B2B guests with their own accounts, and customers sign up through External ID. Backend apps use managed identities, so no service principal secret is stored.",
                "Hybrid sign-in with hash sync" to listOf("password hash", "hash sync"),
                "Conditional Access and MFA" to listOf("conditional access", "multifactor", "mfa"),
                "Guests and customers" to listOf("b2b", "external id", "guest"),
                "No secrets for apps" to listOf("managed identit", "service principal"),
            ),
            B.glossary(
                "Microsoft Entra ID" to "Microsoft's cloud directory that signs people and apps in and issues tokens.",
                "Tenant" to "One organisation's own copy of Entra ID, holding its users, groups and apps.",
                "Conditional Access" to "If-then sign-in rules: who, where, which device, then allow, MFA or block.",
                "Password hash sync" to "Copies a hash of each on-premises password hash so Entra ID checks sign-ins itself.",
                "Pass-through authentication" to "Office agents check each cloud sign-in against Active Directory.",
                "B2B guest" to "An outside person who signs in with their own organisation's account.",
                "Managed identity" to "An app identity Azure looks after, with no secret to store.",
            ),
            B.recap(
                "Sync users with Cloud Sync or Entra Connect; prefer password hash sync, pass-through for instant AD rules, federation only when forced.",
                "Conditional Access decides when to ask for MFA or block; passwordless removes the password altogether.",
                "Partners are B2B guests, customers use External ID, and apps use managed identities.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(monitoring, identity)
}
