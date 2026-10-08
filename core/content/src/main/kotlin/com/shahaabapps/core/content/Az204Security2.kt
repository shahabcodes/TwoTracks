package com.shahaabapps.core.content

/** AZ-204 module 3, Security: Key Vault, managed identities, App Configuration, and the module test. */
internal object Az204Security2 {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val keyVault = Topic(
        id = "az-keyvault", title = "Key Vault and managed identities: secrets without secrets in code", minutes = 28,
        references = listOf(
            B.ref("About Azure Key Vault", LEARN + "azure/key-vault/general/overview"),
            B.ref("Azure Key Vault security features", LEARN + "azure/key-vault/general/security-features"),
            B.ref("What are managed identities for Azure resources?", LEARN + "entra/identity/managed-identities-azure-resources/overview"),
            B.ref("Key Vault references for App Service and Functions", LEARN + "azure/app-service/app-service-key-vault-references"),
            B.ref("Learn module: Implement Azure Key Vault", LEARN + "training/modules/implement-azure-key-vault/"),
            B.ref("Learn module: Implement managed identities", LEARN + "training/modules/implement-managed-identities/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-keyvault-w1", "az-keyvault",
                "An app needs to read a secret from Key Vault. Where do you think its own credential is stored?",
                listOf("Nowhere; Azure gives the app an identity it manages", "In an app setting", "In the vault itself"), 0,
                "The best answer to storing a credential turns out to be not having one.",
            ),
            B.plain("""
                Key Vault holds three kinds of things: secrets (connection strings, passwords), keys (for encryption
                and signing, which never leave the vault), and certificates. A managed identity is an Entra identity
                that Azure gives to your app so it can read the vault without storing any credential at all.
            """),
            B.diagram(
                caption = "The app's managed identity gets a token from the platform and reads the secret. No credential is stored anywhere in the app.",
                nodes = listOf(
                    DNode("app", "App Service", 0.16f, 0.5f, w = 0.26f, h = 0.26f, tone = 1, sub = "system-assigned identity"),
                    DNode("imds", "Identity endpoint", 0.5f, 0.18f, w = 0.3f, h = 0.16f, kind = NodeKind.CLOUD, tone = 2, sub = "token for vault.azure.net"),
                    DNode("kv", "Key Vault", 0.5f, 0.72f, w = 0.3f, h = 0.22f, kind = NodeKind.CYLINDER, sub = "secret: CosmosConn"),
                    DNode("rbac", "Key Vault Secrets User role", 0.84f, 0.72f, w = 0.28f, h = 0.16f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(DEdge("app", "imds", label = "1 get token"), DEdge("app", "kv", label = "2 GET secret"), DEdge("rbac", "kv")),
                aspect = 0.55f,
            ),
            B.h("Vault basics"),
            B.table(
                listOf("Object", "Example", "Note"),
                listOf("Secret", "A Cosmos connection string", "Versioned; you can read the value"),
                listOf("Key", "An RSA key for encrypting data", "Operations happen inside the vault; the private key never leaves"),
                listOf("Certificate", "The TLS cert for twotracks.app", "Can auto-renew with a supported CA"),
            ),
            B.bullets(
                "Two tiers: Standard (software-protected keys) and Premium (HSM-backed keys).",
                "Two permission models: Azure RBAC (recommended; roles like Key Vault Secrets User) and legacy access policies.",
                "Soft delete is on by default; purge protection prevents permanent deletion during the retention window.",
                "Every version of a secret has its own URL; omit the version to get the latest.",
            ),
            B.tip("\"Keys must never leave the vault and must be hardware protected\" means Premium tier. \"Prevent even administrators from permanently deleting\" means purge protection."),
            B.h("Managed identities: system versus user assigned"),
            B.compare("Which identity?",
                B.card("System-assigned", "One resource, one identity.", "Created and deleted with the resource", "No sharing between resources", "Simplest; enable with one toggle"),
                B.card("User-assigned", "Several resources share one identity.", "Standalone resource with its own lifecycle", "Assign to many apps, functions, VMs", "Grant roles once, pre-provision before deploy"),
            ),
            B.check("az-keyvault-k1", "az-keyvault", "Ten function apps must all read the same vault with the same roles, and roles must be granted before the apps exist. Which identity?",
                listOf("System-assigned on each", "One user-assigned identity", "A service principal with a secret"), 1, "A user-assigned identity exists on its own, can be granted roles first and attached to all ten."),
            B.code("csharp", """
                // Same code locally (az login) and in Azure (managed identity)
                var client = new SecretClient(new Uri("https://tt-vault.vault.azure.net/"), new DefaultAzureCredential());
                KeyVaultSecret s = await client.GetSecretAsync("CosmosConn");
                var conn = s.Value;

                // Encrypt with a key that never leaves the vault
                var crypto = new CryptographyClient(new Uri("https://tt-vault.vault.azure.net/keys/data-key"), new DefaultAzureCredential());
                var enc = await crypto.EncryptAsync(EncryptionAlgorithm.RsaOaep, plaintextBytes);
            """),
            B.check(
                "az-keyvault-k2", "az-keyvault",
                "Three apps must share one identity, created before any of them exists. Which kind?",
                listOf("User-assigned", "System-assigned", "A service principal with a secret"), 0,
                "A user-assigned identity is a resource of its own, so it can be created in advance and attached to several resources.",
                whyWrong = mapOf(
                    1 to "A system-assigned identity is tied to one resource's lifetime and dies with it, so it cannot be shared.",
                    2 to "That puts back the credential a managed identity exists to remove.",
                ),
            ),
            B.h("No code at all: Key Vault references"),
            B.p("App Service and Functions can resolve an app setting from the vault at start-up. The app reads a normal environment variable; the platform fetches the secret with the managed identity."),
            B.code("text", """
                CosmosConn = @Microsoft.KeyVault(SecretUri=https://tt-vault.vault.azure.net/secrets/CosmosConn/)
                CosmosConn = @Microsoft.KeyVault(VaultName=tt-vault;SecretName=CosmosConn)
            """),
            B.worked("""
                Worked example. Two Tracks API runs on App Service with a system-assigned identity holding the Key
                Vault Secrets User role on tt-vault. The app setting CosmosConn is a Key Vault reference. Locally
                Usaid runs az login and DefaultAzureCredential picks up his own account, which also has the role. The
                connection string appears in no file, no pipeline variable and no portal setting.
            """),
            B.steps("Wire it up",
                "az webapp identity assign --name tt-api --resource-group tt-rg" to "Returns the principalId of the new system identity.",
                "az keyvault create --name tt-vault --resource-group tt-rg --enable-rbac-authorization true" to "A vault using RBAC.",
                "az role assignment create --assignee <principalId> --role \"Key Vault Secrets User\" --scope <vault-id>" to "Read-only on secrets.",
                "az keyvault secret set --vault-name tt-vault --name CosmosConn --value <value>" to "Store the secret.",
                "az webapp config appsettings set --name tt-api --resource-group tt-rg --settings CosmosConn=\"@Microsoft.KeyVault(VaultName=tt-vault;SecretName=CosmosConn)\"" to "The app sees a plain setting.",
            ),
            B.mistakes(
                "Granting Key Vault Administrator to the app when it only needs to read secrets.",
                "Pinning a secret version in a reference and wondering why rotation does nothing.",
                "Enabling system-assigned identity and forgetting the role assignment; you get 403.",
                "Deleting a user-assigned identity still attached to apps; they lose access at once.",
            ),
            B.example("Two Tracks: RBAC vault, system identity on the API, user-assigned identity shared by the nightly functions, certificate auto-renewed for the custom domain."),
            B.check(
                "az-keyvault-k3", "az-keyvault",
                "What does a Key Vault reference in an app setting save you from?",
                listOf("Writing any secret-fetching code in the app", "Granting the app access to the vault", "Soft delete"), 0,
                "The platform resolves the reference at start-up and the value arrives as an ordinary environment variable.",
                whyWrong = mapOf(
                    1 to "The app's managed identity still needs permission on the vault. The reference only removes the code.",
                    2 to "Soft delete is a setting on the vault, and has nothing to do with how a value is read.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "az-x-kv-match", "Pair each vault object with what is true of it.",
                "Secret" to "Versioned, and you can read the value back",
                "Key" to "Operations happen inside the vault; the private key never leaves",
                "Certificate" to "Can auto-renew with a supported authority",
                "Managed identity" to "An identity Azure manages, with no password to store",
            ),
            B.gap(
                "az-x-kv-gap1", "An identity that lives and dies with the resource it belongs to.",
                "That is a ___-assigned managed identity.", "system", "System",
            ),
            B.gap(
                "az-x-kv-gap2", "Deletion is reversible during a retention window unless you go further.",
                "Soft delete is on by default; ___ protection blocks permanent deletion.", "purge", "Purge",
            ),
            B.tryIt("Try it in 15 minutes", "Free apart from a few cents of vault operations.",
                "Create an RBAC vault and a secret", "Grant yourself Key Vault Secrets User", "Read the secret with az keyvault secret show", "Create a Function App, enable system identity, add a Key Vault reference setting and check it resolves (green tick in the portal)",
            ),
            B.explain(
                "az-keyvault-x1",
                "Explain how an app can read a secret with no credential of its own, and when a user-assigned identity is the right choice.",
                "The app gets a managed identity, which is an Entra identity Azure creates and rotates for it, and is granted a role on the vault. In code DefaultAzureCredential picks up that identity in Azure and the developer login locally, so nothing is stored. With no code at all, a Key Vault reference in an app setting is resolved at start-up and arrives as an ordinary environment variable. A system-assigned identity lives and dies with one resource; a user-assigned one is a resource of its own, so use it to share an identity across several apps or to create it before they exist.",
                "A managed identity replaces the credential" to listOf("managed identity", "no credential", "no secret", "entra"),
                "DefaultAzureCredential or a Key Vault reference" to listOf("defaultazurecredential", "key vault reference", "app setting", "reference"),
                "User-assigned is shared or pre-created" to listOf("user-assigned", "user assigned", "share", "shared", "several", "before"),
            ),
            B.glossary(
                "Managed identity" to "An Entra identity Azure manages for a resource; no credential to store.",
                "System-assigned" to "An identity tied to one resource's lifetime.",
                "User-assigned" to "A standalone identity shared across resources.",
                "Key Vault reference" to "An app setting syntax that resolves a secret at start-up.",
                "Purge protection" to "Blocks permanent deletion during the soft-delete retention window.",
                "DefaultAzureCredential" to "Tries developer login locally and managed identity in Azure.",
            ),
            B.recap(
                "Secrets, keys and certificates; keys never leave the vault; Premium for HSM.",
                "System-assigned for one resource, user-assigned to share or pre-provision; grant roles with RBAC.",
                "DefaultAzureCredential in code or Key Vault references in settings; no credential in the app.",
            ),
        ),
    )

    private val appConfig = Topic(
        id = "az-appconfig", title = "App Configuration: central settings and feature flags", minutes = 22,
        references = listOf(
            B.ref("What is Azure App Configuration?", LEARN + "azure/azure-app-configuration/overview"),
            B.ref("Keys and values", LEARN + "azure/azure-app-configuration/concept-key-value"),
            B.ref("Feature management overview", LEARN + "azure/azure-app-configuration/concept-feature-management"),
            B.ref("Use Key Vault references in App Configuration", LEARN + "azure/azure-app-configuration/use-key-vault-references-dotnet-core"),
            B.ref("Learn module: Implement Azure App Configuration", LEARN + "training/modules/implement-azure-app-configuration/"),
        ),
        blocks = listOf(
            B.warmup(
                "az-appconfig-w1", "az-appconfig",
                "Settings change in App Configuration and the app must pick them up without restarting. How many keys do you think it watches?",
                listOf("One", "All of them", "None; it waits for a webhook"), 0,
                "There is a pattern for this with a name, and the number is the point of it.",
            ),
            B.plain("""
                App Configuration is a central store for settings that are not secrets: feature flags, limits, URLs,
                colours. All your apps read from one place, change a value once and every instance picks it up,
                and labels let the same key differ per environment.
            """),
            B.diagram(
                caption = "Keys with labels and a sentinel key. Apps watch the sentinel and reload everything when it changes.",
                nodes = listOf(
                    DNode("store", "App Configuration", 0.5f, 0.16f, w = 0.5f, h = 0.18f, kind = NodeKind.CYLINDER, tone = 1),
                    DNode("k1", "TwoTracks:DailyLimit = 15 (label: prod)", 0.5f, 0.42f, w = 0.76f, h = 0.12f, kind = NodeKind.PILL),
                    DNode("k2", "TwoTracks:DailyLimit = 50 (label: dev)", 0.5f, 0.58f, w = 0.76f, h = 0.12f, kind = NodeKind.PILL, tone = 2),
                    DNode("k3", "TwoTracks:Sentinel = 7", 0.5f, 0.74f, w = 0.5f, h = 0.12f, kind = NodeKind.PILL, tone = 2),
                    DNode("app", "API instances refresh on sentinel change", 0.5f, 0.92f, w = 0.7f, h = 0.12f, kind = NodeKind.NOTE),
                ),
                edges = listOf(DEdge("store", "k1"), DEdge("k3", "app")),
                aspect = 0.68f,
            ),
            B.table(
                listOf("Concept", "Meaning", "Example"),
                listOf("Key", "A name; use : or / as separators", "TwoTracks:Srs:DailyLimit"),
                listOf("Label", "A variant of the same key", "prod, dev, or none"),
                listOf("Content type", "How to interpret the value", "application/json for structured values"),
                listOf("Key Vault reference", "A pointer to a secret", "The app resolves it with its identity"),
                listOf("Feature flag", "A special key under .appconfig.featureflag/", "Beta, with filters"),
                listOf("Snapshot", "A frozen, named set of keys", "release-0.25"),
            ),
            B.code("csharp", """
                builder.Configuration.AddAzureAppConfiguration(o => {
                    o.Connect(new Uri("https://tt-config.azconfig.io"), new DefaultAzureCredential())
                     .Select("TwoTracks:*", labelFilter: builder.Environment.EnvironmentName)
                     .ConfigureRefresh(r => r.Register("TwoTracks:Sentinel", refreshAll: true).SetRefreshInterval(TimeSpan.FromSeconds(30)))
                     .ConfigureKeyVault(kv => kv.SetCredential(new DefaultAzureCredential()))
                     .UseFeatureFlags();
                });
                builder.Services.AddAzureAppConfiguration().AddFeatureManagement();
                app.UseAzureAppConfiguration();   // middleware triggers the refresh check
            """),
            B.remember("Register one sentinel key with refreshAll instead of every key. The refresh happens on the next request after the interval, driven by the middleware, not on a timer in the background."),
            B.check(
                "az-appconfig-k3", "az-appconfig",
                "How does an app pick up changed settings without being restarted?",
                listOf("Watch one sentinel key and refresh the rest when it moves", "Poll every key on a timer", "Restart the app from a webhook"), 0,
                "One sentinel key is checked, and when it changes every other key is refreshed: one request instead of hundreds.",
                whyWrong = mapOf(
                    1 to "Polling every key is exactly what the sentinel pattern exists to avoid.",
                    2 to "A restart is what dynamic refresh is for not having to do.",
                ),
            ),
            B.h("Feature flags"),
            B.bullets(
                "A flag is on, off, or conditional with filters: percentage, time window, targeting (users and groups), or custom.",
                "In code: IFeatureManager.IsEnabledAsync(\"Beta\"), or the <feature name=\"Beta\"> tag helper in Razor, or [FeatureGate(\"Beta\")] on a controller.",
                "Variants let a flag return a value, not just on/off, for A/B tests.",
            ),
            B.check("az-appconfig-k1", "az-appconfig", "Show a new Learn feature to 10 percent of users first. Which filter?",
                listOf("TimeWindow", "Percentage", "Targeting with a group"), 1, "The percentage filter enables the flag for a stable share of users."),
            B.worked("""
                Worked example. Two Tracks moved DailyLimit and the mock-exam flag into App Configuration. The dev
                label sets DailyLimit to 50 for testing; prod keeps 15. Turning on MockExam for the Beta group lets
                Sanya try it first. The API reads the store with its managed identity (App Configuration Data Reader)
                and the Cosmos connection is a Key Vault reference inside the store.
            """),
            B.steps("Create and seed a store",
                "az appconfig create --name tt-config --resource-group tt-rg --sku Standard" to "Free tier has one store per subscription and 1000 requests a day.",
                "az appconfig kv set --name tt-config --key TwoTracks:Srs:DailyLimit --value 15 --label prod --yes" to "A labelled value.",
                "az appconfig feature set --name tt-config --feature MockExam --label prod --yes" to "A flag, off by default.",
                "az appconfig kv set-keyvault --name tt-config --key TwoTracks:CosmosConn --secret-identifier https://tt-vault.vault.azure.net/secrets/CosmosConn --yes" to "A Key Vault reference.",
            ),
            B.mistakes(
                "Storing secrets as plain values in App Configuration. Use Key Vault references.",
                "Registering every key for refresh instead of a sentinel.",
                "Forgetting UseAzureAppConfiguration middleware, so refresh never triggers.",
                "Using connection strings when the app has a managed identity; grant App Configuration Data Reader instead.",
            ),
            B.example("Two Tracks: labels per environment, a sentinel key for refresh, MockExam flag with targeting, secrets via Key Vault references."),
            B.check(
                "az-appconfig-k2", "az-appconfig",
                "A feature should be on for a fifth of requests. What do you configure?",
                listOf("A percentage feature filter", "A label", "A sentinel key"), 0,
                "Feature filters — percentage, time window, targeting — are what decide a flag's answer at runtime.",
                whyWrong = mapOf(
                    1 to "A label is a per-environment variant of a key, not a rollout rule.",
                    2 to "A sentinel key is how a refresh is triggered, not how a flag is decided.",
                ),
            ),
            B.h("Check yourself"),
            B.gap(
                "az-x-appcfg-gap1", "Check a flag from code in ASP.NET.",
                "Call IFeatureManager.___Async(\"Beta\").", "IsEnabled", "isenabled",
            ),
            B.gap(
                "az-x-appcfg-gap2", "Turn a feature on for a slice of users rather than everyone.",
                "Use a ___ filter on the flag.", "percentage", "Percentage",
            ),
            B.match(
                "az-x-appcfg-match", "Pair each way of gating a feature with where it goes.",
                "IFeatureManager.IsEnabledAsync" to "Anywhere in C# code",
                "The feature tag helper" to "A Razor view",
                "FeatureGate attribute" to "A controller or action",
                "Variants" to "Returning a value, not just on or off",
            ),
            B.tryIt("Try it in 15 minutes", "The Free tier is enough.",
                "Create a free store and two labelled values for one key", "Add a feature flag and toggle it", "In a console app, load with Select and labelFilter and print the value", "Change the value in the portal and add a sentinel refresh",
            ),
            B.explain(
                "az-appconfig-x1",
                "Explain how an app picks up a changed setting without restarting, and what a feature filter does.",
                "App Configuration holds non-secret settings centrally, with labels for the per-environment variants and Key Vault references where a value is actually a secret. For dynamic refresh the app watches one sentinel key: when that key changes, every other key is refreshed, which is one request rather than hundreds. A feature flag is a switch read through IFeatureManager, and a feature filter decides its answer at runtime — a percentage of requests, a time window, or a targeted set of users.",
                "Labels for environments, references for secrets" to listOf("label", "labels", "environment", "key vault reference"),
                "A sentinel key triggers the refresh" to listOf("sentinel", "refresh", "one key", "watch"),
                "Filters decide a flag at runtime" to listOf("filter", "filters", "percentage", "time window", "targeting"),
            ),
            B.glossary(
                "Label" to "A variant of a key, usually per environment.",
                "Sentinel key" to "One key whose change triggers refreshing all others.",
                "Feature flag" to "A switch with optional filters evaluated at runtime.",
                "Feature filter" to "Percentage, time window, targeting or custom rule for a flag.",
                "Snapshot" to "An immutable named set of key-values.",
            ),
            B.recap(
                "Central non-secret settings; labels for environments; Key Vault references for secrets.",
                "Dynamic refresh with a sentinel key and the middleware; identity-based access with Data Reader.",
                "Feature flags with filters (percentage, time window, targeting) via IFeatureManager.",
            ),
        ),
    )

    val topics = listOf(keyVault, appConfig)

    val quiz = Quiz(
        id = "az-security-test", title = "Security module test",
        questions = B.spread(listOf(
            B.q("x1", "az-identity", "Which token should your API validate before serving a request?", listOf("The ID token", "The access token's aud and scp or roles", "The refresh token", "Any JWT"), setOf(1), "The access token carries the audience and permissions. ID tokens are for the client."),
            B.q("x2", "az-identity", "A nightly job with no signed-in user must read all users' calendars. Which permission type and consent?", listOf("Delegated, user consent", "Delegated, admin consent", "Application, admin consent", "Application, user consent"), setOf(2), "No user means application permissions, which always require admin consent."),
            B.q("x3", "az-identity", "Which flow should a single-page or mobile app use?", listOf("Implicit", "Authorization code with PKCE", "Client credentials", "Resource owner password"), setOf(1), "Auth code with PKCE is the recommended flow for public clients; implicit is deprecated."),
            B.q("x4", "az-identity", "Your multitenant app is used by a customer tenant. Where do its permissions and roles attach?", listOf("The application object", "The service principal in the customer tenant", "The redirect URI", "The client secret"), setOf(1), "Each tenant has its own service principal; consent and roles attach to it."),
            B.q("x5", "az-msal", "Users see a sign-in prompt on every API call. What is the most likely cause?", listOf("The token cache is full", "The code calls AcquireTokenInteractive without trying AcquireTokenSilent", "The redirect URI is wrong", "Scopes are missing"), setOf(1), "Call silent first; only fall back to interactive on MsalUiRequiredException."),
            B.q("x6", "az-msal", "Which MSAL method does a daemon use to get a token with its own identity?", listOf("AcquireTokenInteractive", "AcquireTokenSilent", "AcquireTokenForClient", "AcquireTokenByAuthorizationCode"), setOf(2), "Client credentials = AcquireTokenForClient with a /.default scope."),
            B.q("x7", "az-msal", "A single token request asks for a Graph scope and a custom API scope and fails. Why?", listOf("MSAL supports one scope only", "An access token has one audience", "PKCE is missing", "The tenant is single-tenant"), setOf(1), "Each access token is for one resource; request the two resources separately."),
            B.q("x8", "az-graph", "Which header is required with \$count=true on /users?", listOf("Prefer: odata.maxpagesize", "ConsistencyLevel: eventual", "Accept: application/json", "If-Match"), setOf(1), "Advanced directory queries need ConsistencyLevel: eventual."),
            B.q("x9", "az-graph", "How do you reduce the size of a Graph response to two properties?", listOf("\$filter", "\$select", "\$top", "\$expand"), setOf(1), "\$select limits the returned properties."),
            B.q("x10", "az-graph", "Graph returns 429. What should the client do?", listOf("Retry at once", "Wait for Retry-After seconds then retry", "Switch to beta", "Re-authenticate"), setOf(1), "Throttling responses carry a Retry-After header to honour."),
            B.q("x11", "az-keyvault", "An App Service must read a secret with no credential stored anywhere. What do you combine? Select all that apply.", listOf("A managed identity", "A client secret in app settings", "The Key Vault Secrets User role", "An access policy granting purge"), setOf(0, 2), "Managed identity plus a read role on the vault; no secret is stored."),
            B.q("x12", "az-keyvault", "Keys must be hardware protected. Which tier?", listOf("Standard", "Premium", "Basic", "Free"), setOf(1), "Premium gives HSM-backed keys."),
            B.q("x13", "az-keyvault", "Several apps must share one identity with roles granted before the apps are deployed. Which?", listOf("System-assigned identity", "User-assigned identity", "A certificate", "A SAS"), setOf(1), "User-assigned identities are standalone and shareable."),
            B.q("x14", "az-keyvault", "What does @Microsoft.KeyVault(VaultName=v;SecretName=s) in an app setting do?", listOf("Encrypts the setting", "Resolves the secret at start-up using the app's identity", "Creates the secret", "Rotates the secret"), setOf(1), "Key Vault references let the app read a plain setting that the platform resolves from the vault."),
            B.q("x15", "az-keyvault", "Even administrators must not be able to permanently delete a vault's secrets for 90 days. What do you enable?", listOf("Soft delete only", "Purge protection", "RBAC", "A lock on the resource group"), setOf(1), "Purge protection blocks purge during the retention window."),
            B.q("x16", "az-appconfig", "Settings must refresh across all instances when one value changes, with minimal requests. What do you register?", listOf("Every key individually", "A sentinel key with refreshAll", "A timer", "A webhook"), setOf(1), "One sentinel key triggers a full refresh when it changes."),
            B.q("x17", "az-appconfig", "The same key needs different values for dev and prod. Which feature?", listOf("Snapshots", "Labels", "Content type", "Feature filters"), setOf(1), "Labels are variants of the same key."),
            B.q("x18", "az-appconfig", "A feature must be visible only to the Beta group of users. Which filter?", listOf("Percentage", "TimeWindow", "Targeting", "Custom"), setOf(2), "Targeting enables a flag for named users and groups."),
            B.q("x19", "az-appconfig", "Where should a database password live when using App Configuration?", listOf("As a plain key-value", "As a Key Vault reference in App Configuration", "In the label", "In a feature flag"), setOf(1), "App Configuration is not a secret store; point to Key Vault."),
            B.q("x20", "az-identity", "Which claim tells an API that a token was issued for it?", listOf("iss", "aud", "scp", "oid"), setOf(1), "aud is the audience: the resource the token is for."),
        )),
    )
}
