package com.shahaabapps.core.content

/** AZ-204 module 3, Security: identity platform, MSAL, Microsoft Graph. */
internal object Az204Security {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val identity = Topic(
        id = "az-identity", title = "Microsoft identity platform: app registrations, tokens and permissions", minutes = 30,
        references = listOf(
            B.ref("What is the Microsoft identity platform?", LEARN + "entra/identity-platform/v2-overview"),
            B.ref("Application and service principal objects", LEARN + "entra/identity-platform/app-objects-and-service-principals"),
            B.ref("Permissions and consent", LEARN + "entra/identity-platform/permissions-consent-overview"),
            B.ref("OAuth 2.0 authorization code flow", LEARN + "entra/identity-platform/v2-oauth2-auth-code-flow"),
            B.ref("Learn module: Explore the Microsoft identity platform", LEARN + "training/modules/explore-microsoft-identity-platform/"),
        ),
        blocks = listOf(
            B.plain("""
                The identity platform is the sign-in service behind Microsoft accounts and Entra ID (the new name for
                Azure AD). You register your app once, it gets an id, and from then on users sign in with Microsoft
                and your app receives tokens that prove who they are and what they may do.
            """),
            B.h("Two tokens, two jobs"),
            B.table(
                listOf("Token", "Proves", "Sent to", "Format"),
                listOf("ID token", "Who the user is", "Your client app", "JWT with name, email, oid claims"),
                listOf("Access token", "What the caller may do on an API", "The API, in the Authorization header", "JWT with scp or roles claims and an aud"),
                listOf("Refresh token", "Lets the client get new tokens silently", "The identity platform", "Opaque"),
            ),
            B.remember("Access tokens are for the API, never for your client's own decisions. Read the ID token for the user's name; send the access token to the API; validate the audience (aud) on the API side."),
            B.h("Registration: application object and service principal"),
            B.diagram(
                caption = "One application object in the home tenant; one service principal per tenant that uses the app. The principal is what gets roles and permissions.",
                nodes = listOf(
                    DNode("app", "Application object", 0.5f, 0.15f, w = 0.5f, h = 0.18f, tone = 1, sub = "client id, redirect URIs, secrets"),
                    DNode("sp1", "Service principal", 0.25f, 0.6f, w = 0.4f, h = 0.2f, sub = "home tenant"),
                    DNode("sp2", "Service principal", 0.75f, 0.6f, w = 0.4f, h = 0.2f, tone = 2, sub = "customer tenant"),
                    DNode("n", "roles and consent attach here", 0.5f, 0.9f, w = 0.6f, h = 0.12f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("app", "sp1", label = "defines"), DEdge("app", "sp2", label = "defines")),
                aspect = 0.62f,
            ),
            B.table(
                listOf("Setting", "Meaning", "Example"),
                listOf("Supported account types", "Who can sign in", "Single tenant, any tenant (multitenant), plus personal accounts"),
                listOf("Redirect URI", "Where tokens are returned", "https://twotracks.app/signin-oidc, msal{clientId}://auth for mobile"),
                listOf("Client secret or certificate", "Proves the app's own identity (confidential clients only)", "Certificate preferred"),
                listOf("Expose an API", "Scopes your API offers", "api://tt-api/Logs.ReadWrite"),
                listOf("API permissions", "Scopes this app requests from other APIs", "User.Read on Microsoft Graph"),
            ),
            B.check("az-identity-k1", "az-identity", "A customer in another tenant signs into your multitenant app. What gets created in their tenant?",
                listOf("A copy of the application object", "A service principal", "Nothing"), 1, "Each tenant that consents gets its own service principal pointing at your single application object."),
            B.h("Permissions and consent"),
            B.table(
                listOf("Kind", "Who is acting", "Consent", "Claim in token"),
                listOf("Delegated", "A signed-in user, through the app", "The user (or an admin for everyone)", "scp"),
                listOf("Application", "The app itself, no user", "An admin always", "roles"),
            ),
            B.tip("A nightly job with no user reading all mailboxes needs application permissions and admin consent. A user reading their own files needs delegated permissions. The exam tests this split constantly."),
            B.worked("""
                Worked example. Two Tracks registers two apps: the mobile client (public, redirect msal{id}://auth) and
                the API (exposes scope Logs.ReadWrite). The client requests Logs.ReadWrite and Graph User.Read. When
                Usaid signs in he consents once; the client gets an ID token for his name, an access token with
                aud = api://tt-api and scp = Logs.ReadWrite to call the API, and another with aud = Graph for his
                profile photo. A nightly report function uses a client credential with application permission
                Logs.Read.All, consented by the admin.
            """),
            B.check(
                "az-identity-k2", "az-identity",
                "A nightly job with nobody signed in must read every user's calendar. Which permission type?",
                listOf("An application permission, with admin consent", "A delegated permission", "Either will do"), 0,
                "With no user present the app acts as itself: an application permission, consented by an admin, arriving in the roles claim.",
                whyWrong = mapOf(
                    1 to "A delegated permission needs a signed-in user to act on behalf of, and at two in the morning there is none.",
                    2 to "They are not interchangeable. The absence of a user rules delegated out.",
                ),
            ),
            B.h("Public versus confidential clients"),
            B.compare("Client types",
                B.card("Public client", "Mobile and desktop apps, single-page apps.", "Cannot keep a secret", "Uses auth code + PKCE", "Redirect URI is a custom scheme or localhost"),
                B.card("Confidential client", "Web apps and daemons on a server.", "Holds a secret or certificate", "Auth code, or client credentials for no user", "Can request application permissions"),
            ),
            B.h("The flows you must recognise"),
            B.table(
                listOf("Flow", "Scenario", "Note"),
                listOf("Authorization code (+ PKCE)", "User signs in through a browser", "The default for web, mobile and SPA"),
                listOf("Client credentials", "Daemon or job, no user", "Application permissions; secret or certificate"),
                listOf("On-behalf-of", "An API calling another API as the user", "Exchanges the incoming token for a new one"),
                listOf("Device code", "No browser on the device (CLI, TV)", "User enters a code on another device"),
                listOf("Implicit", "Legacy SPA", "Deprecated; choose auth code + PKCE"),
            ),
            B.steps("Register the API and the client with the CLI",
                "az ad app create --display-name tt-api --identifier-uris api://tt-api" to "The API's application object; add a scope under Expose an API.",
                "az ad app create --display-name tt-mobile --public-client-redirect-uris msal{id}://auth --is-fallback-public-client true" to "A public client with no secret.",
                "az ad app permission add --id <mobile-id> --api <api-id> --api-permissions <scope-id>=Scope" to "The mobile app requests the API's delegated scope.",
                "az ad app permission admin-consent --id <mobile-id>" to "Optional: an admin consents once for everyone.",
            ),
            B.mistakes(
                "Checking the access token's claims in the client to decide what to show. Use the ID token or call the API.",
                "Putting a client secret in a mobile app. Public clients use PKCE, not secrets.",
                "Requesting application permissions for a user-facing flow. They need admin consent and run without a user.",
                "Forgetting to validate aud on the API; a token for Graph is not a token for you.",
            ),
            B.example("Two Tracks: mobile app = public client with PKCE; API = exposes Logs.ReadWrite; nightly function = client credentials with a certificate from Key Vault."),
            B.check(
                "az-identity-k3", "az-identity",
                "A mobile app signs users in. Which flow?",
                listOf("Authorization code with PKCE", "Client credentials", "Implicit"), 0,
                "A mobile app is a public client and cannot keep a secret, so PKCE protects the code exchange in its place.",
                whyWrong = mapOf(
                    1 to "Client credentials is for daemons with no user, and needs a secret a mobile app has nowhere safe to put.",
                    2 to "Implicit is deprecated, precisely because it returns tokens in the URL.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-x-identity-match", "Three tokens, three jobs. Pair them.",
                "ID token" to "Proves who the user is, to your client app",
                "Access token" to "Proves what the caller may do, sent to the API",
                "Refresh token" to "Opaque, and only the identity platform reads it",
                "Service principal" to "The app's instance inside one tenant",
            ),
            B.gap(
                "az-x-identity-gap1", "A daemon with no signed-in user needs application permissions.",
                "Those arrive in the token as ___ claims, and always need admin consent.", "roles", "role",
            ),
            B.gap(
                "az-x-identity-gap2", "A delegated permission acts on behalf of the person who signed in.",
                "Delegated permissions arrive as ___ claims.", "scp", "scope", "scopes",
            ),
            B.tryIt("Try it in 15 minutes", "Any free Entra tenant works.",
                "Register an app with a localhost redirect URI", "Note the client id and tenant id", "Add Microsoft Graph User.Read under API permissions", "Open Authentication and compare the public-client and web platform settings",
            ),
            B.glossary(
                "Entra ID" to "The identity service formerly called Azure Active Directory.",
                "Application object" to "The single registration in the home tenant.",
                "Service principal" to "The per-tenant identity that gets permissions and roles.",
                "Delegated permission" to "Acting as a signed-in user; scp claim.",
                "Application permission" to "Acting as the app itself; roles claim; admin consent.",
                "PKCE" to "Proof Key for Code Exchange: protects the auth code for public clients.",
            ),
            B.recap(
                "ID token says who; access token says what the API may be asked; validate aud on the API.",
                "One application object, a service principal per tenant; permissions attach to the principal.",
                "Delegated = user present (scp); application = no user, admin consent (roles). Auth code + PKCE for users, client credentials for daemons.",
            ),
        ),
    )

    private val msal = Topic(
        id = "az-msal", title = "MSAL: acquiring tokens in code", minutes = 24,
        references = listOf(
            B.ref("Overview of the Microsoft Authentication Library (MSAL)", LEARN + "entra/identity-platform/msal-overview"),
            B.ref("Acquire and cache tokens with MSAL", LEARN + "entra/identity-platform/msal-acquire-cache-tokens"),
            B.ref("Scopes and permissions in the identity platform", LEARN + "entra/identity-platform/scopes-oidc"),
            B.ref("Learn module: Implement authentication by using MSAL", LEARN + "training/modules/implement-authentication-by-using-microsoft-authentication-library/"),
        ),
        blocks = listOf(
            B.plain("MSAL is the library that talks to the identity platform for you. You tell it which app you are and which scopes you need; it handles the browser, the cache and the refresh."),
            B.diagram(
                caption = "Always try silent first. Only fall back to an interactive prompt when the cache cannot help.",
                nodes = listOf(
                    DNode("s", "AcquireTokenSilent", 0.2f, 0.5f, w = 0.32f, h = 0.24f, tone = 1, sub = "from cache or refresh token"),
                    DNode("ok", "token", 0.55f, 0.22f, w = 0.2f, h = 0.16f, tone = 2),
                    DNode("ui", "MsalUiRequiredException", 0.55f, 0.78f, w = 0.36f, h = 0.16f, kind = NodeKind.NOTE),
                    DNode("i", "AcquireTokenInteractive", 0.86f, 0.5f, w = 0.26f, h = 0.24f, sub = "browser prompt"),
                ),
                edges = listOf(DEdge("s", "ok", label = "hit"), DEdge("s", "ui", label = "miss"), DEdge("ui", "i")),
                aspect = 0.45f,
            ),
            B.code("csharp", """
                var app = PublicClientApplicationBuilder.Create(clientId)
                    .WithAuthority(AzureCloudInstance.AzurePublic, tenantId)
                    .WithRedirectUri("http://localhost")
                    .Build();

                string[] scopes = { "api://tt-api/Logs.ReadWrite" };
                var accounts = await app.GetAccountsAsync();
                AuthenticationResult result;
                try {
                    result = await app.AcquireTokenSilent(scopes, accounts.FirstOrDefault()).ExecuteAsync();
                } catch (MsalUiRequiredException) {
                    result = await app.AcquireTokenInteractive(scopes).ExecuteAsync();
                }
                http.DefaultRequestHeaders.Authorization = new AuthenticationHeaderValue("Bearer", result.AccessToken);
            """),
            B.remember("Silent first, interactive on MsalUiRequiredException. The exam shows code that calls interactive every time and asks what is wrong."),
            B.h("Confidential clients: web apps and daemons"),
            B.code("csharp", """
                var daemon = ConfidentialClientApplicationBuilder.Create(clientId)
                    .WithCertificate(cert)                          // or .WithClientSecret(secret)
                    .WithAuthority(new Uri($"https://login.microsoftonline.com/{tenantId}"))
                    .Build();

                // No user: the scope is the API's /.default
                var token = await daemon.AcquireTokenForClient(new[] { "api://tt-api/.default" }).ExecuteAsync();
            """),
            B.table(
                listOf("Builder", "Client", "Main methods"),
                listOf("PublicClientApplicationBuilder", "Mobile, desktop, CLI", "AcquireTokenSilent, AcquireTokenInteractive, AcquireTokenWithDeviceCode"),
                listOf("ConfidentialClientApplicationBuilder", "Web app, API, daemon", "AcquireTokenByAuthorizationCode, AcquireTokenForClient, AcquireTokenOnBehalfOf"),
            ),
            B.check("az-msal-k1", "az-msal", "A background service with no user must call Microsoft Graph. Which MSAL method?",
                listOf("AcquireTokenInteractive", "AcquireTokenForClient", "AcquireTokenSilent"), 1, "Client credentials flow is AcquireTokenForClient with the /.default scope."),
            B.check(
                "az-msal-k2", "az-msal",
                "AcquireTokenSilent throws MsalUiRequiredException. What should the code do?",
                listOf("Fall back to an interactive prompt", "Retry silently, with a back-off", "Build a new client application"), 0,
                "That exception is the library saying the user has to be asked: consent, MFA, or a session that has expired.",
                whyWrong = mapOf(
                    1 to "Retrying silently throws the same exception. Nothing in the cache changes without the user.",
                    2 to "A new client object shares the same token cache, and so the same problem.",
                ),
            ),
            B.h("Scopes"),
            B.bullets(
                "Request scopes as resource/permission, for example https://graph.microsoft.com/User.Read or api://tt-api/Logs.ReadWrite.",
                "/.default asks for everything already consented on the app registration; required for client credentials.",
                "openid, profile, offline_access are OpenID Connect scopes; offline_access gets you a refresh token.",
                "One access token is for one resource. Ask for Graph scopes and API scopes in separate calls.",
            ),
            B.tip("If a question mixes scopes from two APIs in one request and asks why it fails: an access token has a single audience."),
            B.worked("""
                Worked example. The Two Tracks Android app uses MSAL for Android with a redirect of
                msauth://com.shahaabapps.twotracks/<signature-hash>. On launch it calls acquireTokenSilent; on first
                run that throws, so it shows the Microsoft sign-in, caches the account, and from then on silent works
                for months thanks to the refresh token. Every API call sends Bearer <access token>.
            """),
            B.steps("Token cache in a web app",
                "Use the auth code flow via Microsoft.Identity.Web." to "It wraps MSAL for ASP.NET Core.",
                "Add a distributed cache (Redis or SQL) for tokens." to "In-memory caches vanish on scale-out.",
                "Call ITokenAcquisition.GetAccessTokenForUserAsync(scopes)." to "Silent refresh is handled for you.",
            ),
            B.mistakes(
                "Calling AcquireTokenInteractive on every request; users see prompts constantly.",
                "Using a secret in a public client application.",
                "Mixing Graph and your API scopes in one call.",
                "Caching tokens in memory on a web farm; use a distributed cache.",
            ),
            B.example("Two Tracks mobile: PublicClientApplication, silent then interactive, one token for the API and one for Graph."),
            B.check(
                "az-msal-k3", "az-msal",
                "A daemon asks for a Graph token with client credentials. Which scope does it request?",
                listOf("https://graph.microsoft.com/.default", "User.Read", "offline_access"), 0,
                "/.default asks for everything already consented for that resource, which is how client credentials has to work: there is no user to consent to anything narrower.",
                whyWrong = mapOf(
                    1 to "A specific delegated scope needs a user. Client credentials cannot request one.",
                    2 to "offline_access asks for a refresh token, which a daemon has no use for: it simply asks for another token.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-x-msal-match", "Pair each situation with the MSAL call that fits.",
                "A daemon with no user at all" to "AcquireTokenForClient",
                "An API calling a second API as the user" to "AcquireTokenOnBehalfOf",
                "A CLI on a machine with no browser" to "AcquireTokenWithDeviceCode",
                "A token you already hold, still valid" to "AcquireTokenSilent",
            ),
            B.gap(
                "az-x-msal-gap1", "A mobile or desktop app cannot keep a secret, so it uses the other builder.",
                "Build it with ___ApplicationBuilder.", "PublicClient", "publicclient", "Public Client",
            ),
            B.tryIt("Try it in 15 minutes", "Use the app registration from the previous topic.",
                "Create a .NET console app and add Microsoft.Identity.Client", "Acquire a token for User.Read interactively", "Paste the access token into jwt.ms and read aud and scp", "Run again and confirm the silent path succeeds",
            ),
            B.glossary(
                "MSAL" to "Microsoft Authentication Library; gets and caches tokens.",
                "AcquireTokenSilent" to "Returns a cached or refreshed token with no prompt.",
                "MsalUiRequiredException" to "The signal to fall back to an interactive prompt.",
                "/.default" to "All consented permissions for a resource; used for client credentials.",
                "Audience (aud)" to "The resource an access token is valid for.",
            ),
            B.recap(
                "Public client builder for apps with users; confidential for servers and daemons.",
                "Silent first, interactive only on MsalUiRequiredException; AcquireTokenForClient for no-user jobs.",
                "One resource per token; /.default for client credentials; offline_access for refresh tokens.",
            ),
        ),
    )

    private val graph = Topic(
        id = "az-graph", title = "Microsoft Graph: one API for Microsoft 365 data", minutes = 20,
        references = listOf(
            B.ref("Overview of Microsoft Graph", LEARN + "graph/overview"),
            B.ref("Use query parameters to customize responses", LEARN + "graph/query-parameters"),
            B.ref("Microsoft Graph SDKs overview", LEARN + "graph/sdks/sdks-overview"),
            B.ref("Learn module: Explore Microsoft Graph", LEARN + "training/modules/microsoft-graph/"),
        ),
        blocks = listOf(
            B.plain("Microsoft Graph is a single REST endpoint, https://graph.microsoft.com, for users, mail, calendar, files, Teams and more. You call it with an access token whose audience is Graph."),
            B.diagram(
                caption = "Your app gets a Graph token from the identity platform and calls one endpoint for many services.",
                nodes = listOf(
                    DNode("app", "Two Tracks", 0.14f, 0.5f, w = 0.22f, h = 0.24f, kind = NodeKind.USER, tone = 1),
                    DNode("id", "identity platform", 0.45f, 0.18f, w = 0.3f, h = 0.16f, kind = NodeKind.CLOUD, tone = 2),
                    DNode("g", "graph.microsoft.com/v1.0", 0.5f, 0.6f, w = 0.4f, h = 0.2f, kind = NodeKind.PILL),
                    DNode("u", "users", 0.84f, 0.3f, w = 0.18f, h = 0.14f, tone = 2), DNode("m", "mail", 0.84f, 0.6f, w = 0.18f, h = 0.14f, tone = 2), DNode("c", "calendar", 0.84f, 0.9f, w = 0.2f, h = 0.14f, tone = 2),
                ),
                edges = listOf(DEdge("app", "id", label = "token"), DEdge("app", "g", label = "Bearer"), DEdge("g", "u"), DEdge("g", "m"), DEdge("g", "c")),
                aspect = 0.5f,
            ),
            B.h("The shape of a call"),
            B.code("http", """
                GET https://graph.microsoft.com/v1.0/me/messages?${'$'}select=subject,from&${'$'}filter=isRead eq false&${'$'}top=10&${'$'}orderby=receivedDateTime desc
                Authorization: Bearer eyJ0eXAi...
            """),
            B.table(
                listOf("Parameter", "Does", "Example"),
                listOf("\$select", "Return only these properties", "\$select=displayName,mail"),
                listOf("\$filter", "Where clause", "\$filter=startswith(displayName,'Sa')"),
                listOf("\$orderby", "Sort", "\$orderby=receivedDateTime desc"),
                listOf("\$top / \$skip", "Page size and offset", "\$top=25"),
                listOf("\$expand", "Include a related resource", "\$expand=members"),
                listOf("\$count", "Include a total", "\$count=true with ConsistencyLevel: eventual"),
                listOf("\$search", "Full text", "\$search=\"displayName:usaid\""),
            ),
            B.tip("\$count, \$search and some \$filter queries on directory objects need the header ConsistencyLevel: eventual. That header is a favourite exam detail."),
            B.check(
                "az-graph-k2", "az-graph",
                "A Graph response contains @odata.nextLink. What does the client do with it?",
                listOf("Call that URL for the next page", "Retry the original call", "Raise \$top and start again"), 0,
                "nextLink is the complete URL of the following page. Follow it until a response arrives without one.",
                whyWrong = mapOf(
                    1 to "Retrying returns the same first page.",
                    2 to "\$top is a hint rather than a guarantee, and Graph will page anyway. Following nextLink is the only reliable way through.",
                ),
            ),
            B.h("Paging and batching"),
            B.bullets(
                "Large results come in pages with an @odata.nextLink; follow it until it is absent.",
                "POST /\$batch sends up to 20 requests in one call, each with its own id and status.",
                "429 means throttled; honour the Retry-After header. The SDKs retry for you.",
            ),
            B.code("csharp", """
                var graph = new GraphServiceClient(new DefaultAzureCredential(), new[] { "https://graph.microsoft.com/.default" });
                var me = await graph.Me.GetAsync(c => c.QueryParameters.Select = new[] { "displayName", "mail" });
                var unread = await graph.Me.Messages.GetAsync(c => {
                    c.QueryParameters.Filter = "isRead eq false";
                    c.QueryParameters.Top = 10;
                });
                foreach (var m in unread.Value) Console.WriteLine(m.Subject);
            """),
            B.check("az-graph-k1", "az-graph", "Your Graph call returns only the first 100 items. How do you get the rest?",
                listOf("Add \$all=true", "Follow @odata.nextLink", "Increase \$skip until empty"), 1, "Graph pages results and returns a nextLink URL to call for the next page."),
            B.worked("""
                Worked example. Two Tracks shows the partner's profile photo and next free calendar slot for a joint
                study session. With delegated User.Read and Calendars.Read it calls /me/photo/${'$'}value and
                /me/calendar/getSchedule. The access token aud is Graph, separate from the token for the Two Tracks API.
            """),
            B.steps("Explore before coding",
                "Open Graph Explorer at developer.microsoft.com/graph/graph-explorer." to "Sign in with a test account.",
                "Run GET /me and then GET /me?\$select=displayName." to "See how \$select shrinks the response.",
                "Open Modify permissions to see which scope each call needs." to "Copy those into your app registration.",
            ),
            B.mistakes(
                "Sending your own API's token to Graph. The audience must be Graph.",
                "Using the beta endpoint in production.",
                "Ignoring nextLink and reporting partial results.",
                "Forgetting ConsistencyLevel: eventual with \$count or \$search.",
            ),
            B.example("Two Tracks: Graph for photos and calendar availability; Graph SDK with DefaultAzureCredential in the API; Graph Explorer for discovering scopes."),
            B.check(
                "az-graph-k3", "az-graph",
                "You must make thirty Graph calls in as few round trips as possible. What do you do?",
                listOf("Two \$batch requests, since one holds twenty", "One \$batch request with all thirty", "Thirty calls, handling Retry-After"), 0,
                "\$batch carries up to twenty requests in one HTTP call, so thirty needs two of them.",
                whyWrong = mapOf(
                    1 to "A single batch is capped at twenty requests, so the rest are rejected.",
                    2 to "That works, and is exactly the thirty round trips batching exists to avoid.",
                ),
            ),
            B.h("Check yourself"),
            B.gap(
                "az-x-graph-gap1", "A large result arrives in pages, and the response tells you there is more.",
                "Follow the @odata.___ until it is absent.", "nextLink", "nextlink",
            ),
            B.gap(
                "az-x-graph-gap2", "Graph throttled you.",
                "A ___ response means slow down, and honour its Retry-After header.", "429",
            ),
            B.gap(
                "az-x-graph-gap3", "Send many requests without many round trips.",
                "POST to /\$batch, up to ___ requests in one call.", "20", "twenty",
            ),
            B.tryIt("Try it in 10 minutes", "Graph Explorer needs no setup.",
                "GET /me", "GET /me/messages?\$top=3&\$select=subject", "GET /users?\$count=true with the ConsistencyLevel header", "Look at the permissions tab for each",
            ),
            B.glossary(
                "Microsoft Graph" to "The unified REST API for Microsoft 365 and Entra data.",
                "OData query parameters" to "\$select, \$filter, \$orderby and friends.",
                "@odata.nextLink" to "The URL of the next page of results.",
                "\$batch" to "Up to 20 requests in one HTTP call.",
                "ConsistencyLevel: eventual" to "Header required for advanced directory queries.",
            ),
            B.recap(
                "One endpoint, v1.0 for production; token audience must be Graph.",
                "Shape results with \$select, \$filter, \$top; follow nextLink for more; batch up to 20.",
                "Advanced queries need ConsistencyLevel: eventual; 429 means wait for Retry-After.",
            ),
        ),
    )

    val topics = listOf(identity, msal, graph)
}
