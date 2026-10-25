package com.shahaabapps.core.content

/** AI-200 topics, written from the official study guide and Microsoft Learn (Ai200). */
internal object Ai200Secure {
    private const val LEARN = "https://learn.microsoft.com/en-us/"

    private val keyVault = Topic(
        id = "ai-keyvault", title = "Key Vault for AI apps: secrets, rotation and retrieval", minutes = 30,
        references = listOf(
            B.ref("About Azure Key Vault", LEARN + "azure/key-vault/general/overview"),
            B.ref("Quickstart: Key Vault secret client library for Python", LEARN + "azure/key-vault/secrets/quick-create-python"),
            B.ref("What are managed identities for Azure resources?", LEARN + "entra/identity/managed-identities-azure-resources/overview"),
            B.ref("Automate secret rotation with one set of credentials", LEARN + "azure/key-vault/secrets/tutorial-rotation"),
            B.ref("Configure cryptographic key auto-rotation", LEARN + "azure/key-vault/keys/how-to-configure-key-rotation"),
            B.ref("Azure Key Vault soft-delete overview", LEARN + "azure/key-vault/general/soft-delete-overview"),
            B.ref("Use Key Vault references as app settings", LEARN + "azure/app-service/app-service-key-vault-references"),
            B.ref("Manage secrets in Azure Container Apps", LEARN + "azure/container-apps/manage-secrets"),
            B.ref("Learn module: Manage application secrets with Azure Key Vault", LEARN + "training/modules/manage-app-secrets-key-vault/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-keyvault-w1", "ai-keyvault",
                "Two Tracks Coach calls an AI model that needs an API key. Where should the Android app keep that key?",
                listOf("Nowhere; the phone calls the backend, which reads the key from Key Vault", "In the app's resources, obfuscated", "In encrypted shared preferences"), 0,
                "Anything shipped inside an app can be pulled out of it. Think about who should hold the key at all.",
            ),
            B.plain("""
                Key Vault is a locked safe in Azure for secrets: API keys, connection strings, passwords. Your
                backend opens it with a managed identity, which is a login Azure creates and looks after for the
                app, so no password to the safe is stored anywhere. The phone never sees a secret: it calls the
                backend, and only the backend talks to the vault.
            """),
            B.diagram(
                caption = "The phone calls the Coach API; the API reads the key with its managed identity. Near expiry, Event Grid wakes a function that writes a new version.",
                nodes = listOf(
                    DNode("phone", "Android app", 0.12f, 0.2f, w = 0.2f, h = 0.2f, kind = NodeKind.USER),
                    DNode("api", "Coach API", 0.45f, 0.2f, w = 0.3f, h = 0.2f, tone = 1, sub = "managed identity"),
                    DNode("kv", "Key Vault", 0.82f, 0.2f, w = 0.3f, h = 0.2f, kind = NodeKind.CYLINDER, sub = "secret: OpenAiKey"),
                    DNode("eg", "Event Grid", 0.82f, 0.55f, w = 0.3f, h = 0.18f, tone = 2, sub = "SecretNearExpiry"),
                    DNode("fn", "Rotation function", 0.45f, 0.55f, w = 0.3f, h = 0.18f, tone = 1, sub = "new secret version"),
                    DNode("note", "The phone never holds a secret", 0.5f, 0.86f, w = 0.8f, h = 0.14f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("phone", "api", label = "HTTPS"),
                    DEdge("api", "kv", label = "get_secret"),
                    DEdge("kv", "eg", label = "near expiry"),
                    DEdge("eg", "fn", label = "trigger"),
                    DEdge("fn", "kv", label = "set_secret", dashed = true),
                ),
                aspect = 0.8f,
            ),
            B.h("A vault with RBAC"),
            B.p("""
                Create the vault with Azure RBAC, role-based access control, so access is granted with the same
                role assignments you use everywhere else. Give each identity the smallest role that works.
            """),
            B.table(
                listOf("Role", "Can do", "Give it to"),
                listOf("Key Vault Secrets User", "Read secret values", "The Coach API and anything that only reads"),
                listOf("Key Vault Secrets Officer", "Create, update and delete secrets", "The rotation function, the pipeline"),
                listOf("Key Vault Administrator", "Everything on the data plane", "A person, rarely"),
            ),
            B.h("Managed identities: system or user assigned"),
            B.compare("Which identity?",
                B.card("System-assigned", "One app, one identity, same lifetime.", "Switched on inside the resource", "Deleted when the resource is deleted", "Cannot be shared"),
                B.card("User-assigned", "Several apps share one identity, or it must exist first.", "A resource of its own", "Grant roles once, before deploying", "Attach to many apps"),
            ),
            B.p("""
                A shared example: the Coach API container app and the nightly embedding function both read
                OpenAiKey. Create one user-assigned identity, tt-coach-id, grant it Key Vault Secrets User once,
                and attach it to both. In Python, set the environment variable AZURE_CLIENT_ID to that identity's
                client ID so DefaultAzureCredential knows which identity to use.
            """),
            B.h("Reading a secret in Python"),
            B.code("python", """
                import time
                from azure.identity import DefaultAzureCredential
                from azure.keyvault.secrets import SecretClient

                # az login on your laptop, managed identity in Azure: same code
                client = SecretClient(vault_url="https://tt-coach-kv.vault.azure.net",
                                      credential=DefaultAzureCredential())

                _cache = {}          # name -> (value, fetched_at)
                TTL_SECONDS = 300

                def get_secret(name: str) -> str:
                    hit = _cache.get(name)
                    if hit and time.time() - hit[1] < TTL_SECONDS:
                        return hit[0]
                    value = client.get_secret(name).value   # no version: the latest one
                    _cache[name] = (value, time.time())
                    return value

                def forget(name: str) -> None:
                    _cache.pop(name, None)   # call on a 401 from the model, then retry once
            """),
            B.bullets(
                "Every set_secret creates a new version with its own id; the old versions stay.",
                "Ask without a version and you get the latest. Pinning a version means rotation never reaches you.",
                "Cache values in memory: calling the vault on every request is slow and can hit throttling limits.",
                "Refresh the cache on a timer and also when the old value is rejected, which is the rotation signal.",
            ),
            B.check(
                "ai-keyvault-k1", "ai-keyvault",
                "The Coach API and a nightly function must read the same vault, and the identity must have its role before either is deployed. Which identity?",
                listOf("One user-assigned identity on both", "A system-assigned identity on each", "A service principal with a client secret"), 0,
                "A user-assigned identity is its own resource, so you create it, grant the role and attach it to both apps.",
                whyWrong = mapOf(
                    1 to "A system-assigned identity only exists once its app does, so the role cannot be granted in advance.",
                    2 to "A client secret is a new secret to store and rotate, which is what managed identities remove.",
                ),
            ),
            B.h("Rotation"),
            B.p("""
                Rotation means replacing a secret with a new one on a schedule, so a leaked value stops working.
                Keys (for encryption) have a built-in rotation policy: Key Vault makes a new key version by itself
                after a set time, and can raise a near-expiry notification. Secrets such as an API key have no
                built-in rotation, because only the provider can issue a new key. Instead you give the secret an
                expiry date; about 30 days before it, Key Vault sends a SecretNearExpiry event to Event Grid, and
                a function you write gets a new credential and stores it as a new version.
            """),
            B.code("python", """
                import azure.functions as func
                from azure.identity import DefaultAzureCredential
                from azure.keyvault.secrets import SecretClient

                app = func.FunctionApp()

                @app.event_grid_trigger(arg_name="event")
                def rotate(event: func.EventGridEvent):
                    if event.event_type != "Microsoft.KeyVault.SecretNearExpiry":
                        return
                    data = event.get_json()       # VaultName, ObjectName, Version, EXP...
                    client = SecretClient(f"https://{data['VaultName']}.vault.azure.net",
                                          DefaultAzureCredential())
                    new_key = issue_new_key()      # your call to the provider
                    client.set_secret(data["ObjectName"], new_key, expires_on=in_days(90))
            """),
            B.tip("\"Rotate a key automatically\" points to a key rotation policy. \"Do something when a secret is about to expire\" points to the SecretNearExpiry event through Event Grid."),
            B.check(
                "ai-keyvault-k2", "ai-keyvault",
                "OpenAiKey must be replaced every 90 days with no person involved. What do you build?",
                listOf("An expiry date, an Event Grid subscription to SecretNearExpiry and a function that writes a new version", "A key rotation policy on the secret", "A daily restart of the Coach API"), 0,
                "Secrets have no rotation policy; the near-expiry event triggers your own code, which asks the provider for a new key.",
                whyWrong = mapOf(
                    1 to "Rotation policies are for Key Vault keys, which the vault can generate itself; an API key comes from the provider.",
                    2 to "A restart rereads the same value. Nothing new exists until something creates it.",
                ),
            ),
            B.h("No code: Key Vault references"),
            B.p("""
                App Service and Functions resolve a setting written as @Microsoft.KeyVault(VaultName=tt-coach-kv;SecretName=OpenAiKey)
                using the app's identity, and refetch unversioned references within 24 hours. In Container Apps
                a secret can be a keyvaultref, and an environment variable points at it with secretref; the app
                picks up a new version within about 30 minutes and restarts the revisions that use it.
            """),
            B.h("Soft delete and purge protection"),
            B.diagram(
                caption = "A deleted secret waits in the soft-deleted state for the retention period, where it can be recovered or, without purge protection, purged.",
                nodes = listOf(
                    DNode("active", "Active secret", 0.15f, 0.35f, w = 0.24f, h = 0.3f, tone = 1),
                    DNode("deleted", "Soft-deleted", 0.5f, 0.35f, w = 0.3f, h = 0.3f, sub = "kept 7 to 90 days"),
                    DNode("gone", "Purged for good", 0.85f, 0.35f, w = 0.26f, h = 0.3f, tone = 2),
                    DNode("note", "Purge protection: no purge until the window ends", 0.5f, 0.82f, w = 0.9f, h = 0.22f, kind = NodeKind.NOTE, tone = 2),
                ),
                edges = listOf(
                    DEdge("active", "deleted", label = "delete / recover", bidirectional = true),
                    DEdge("deleted", "gone", label = "purge"),
                ),
                aspect = 0.5f,
            ),
            B.bullets(
                "Soft delete is a recycle bin. A deleted vault or secret is kept for a retention period of 7 to 90 days (90 by default), set when the vault is created and fixed after that.",
                "Soft delete is on for every new vault and cannot be turned off.",
                "Purge means deleting from the recycle bin for good, and it needs its own permission. To purge a deleted secret, you need a data role that includes purge, such as Key Vault Secrets Officer or Key Vault Administrator (keys and certificates use their own Officer roles). To purge a whole deleted vault, you need a role such as Key Vault Purge Operator, which works only on vaults.",
                "Purge protection, off by default, blocks purging until the retention period ends, even for administrators. Once on, it cannot be turned off.",
                "While a secret is soft-deleted its name is taken: recover it or wait, you cannot create a new one with the same name.",
            ),
            B.worked("""
                Worked example. The Coach API needs OpenAiKey. Usaid creates tt-coach-kv with RBAC and purge
                protection, and a user-assigned identity tt-coach-id with Key Vault Secrets User. He stores the key
                with a 90-day expiry. The container app has a keyvaultref secret without a version and an
                environment variable that points to it. The rotation function has Key Vault Secrets Officer and
                listens for SecretNearExpiry. When it writes a new version, the app picks it up within 30 minutes.
            """),
            B.faded(
                "ai-keyvault-f1",
                "Same set-up for the database password used by the nightly embedding function. It must rotate itself before it runs out.",
                listOf(
                    "Store the password in tt-coach-kv and give the function's identity Key Vault Secrets User.",
                    "Set an ___ date on the secret, so Key Vault raises SecretNearExpiry about 30 days before it.",
                    "Subscribe a rotation function to that event; it sets a new password and writes a new version.",
                ),
                blank = 1,
                answers = listOf("expiry", "expiration"),
                note = "No expiry date, no near-expiry event, so nothing ever triggers the rotation.",
            ),
            B.steps("Do it for real",
                "az keyvault create --name tt-coach-kv --resource-group tt-rg --enable-rbac-authorization true --retention-days 90 --enable-purge-protection true" to "A vault using RBAC, with a 90-day recycle bin that cannot be emptied early.",
                "az identity create --name tt-coach-id --resource-group tt-rg" to "Returns principalId and clientId of the shared identity.",
                "az role assignment create --assignee-object-id <principalId> --assignee-principal-type ServicePrincipal --role \"Key Vault Secrets User\" --scope <vault-id>" to "Read-only access to secret values.",
                "az keyvault secret set --vault-name tt-coach-kv --name OpenAiKey --value <key> --expires 2027-01-31T00:00:00Z" to "Version 1, with an expiry date.",
                "az containerapp secret set --name tt-coach-api --resource-group tt-rg --secrets \"openai-key=keyvaultref:https://tt-coach-kv.vault.azure.net/secrets/OpenAiKey,identityref:<identity-id>\"" to "A secret resolved from the vault, without a version.",
                "az containerapp update --name tt-coach-api --resource-group tt-rg --set-env-vars \"OPENAI_KEY=secretref:openai-key\"" to "The code reads OPENAI_KEY like any environment variable.",
            ),
            B.mistakes(
                "Putting the model's API key inside the Android app; anyone can extract it from the package.",
                "Pinning a secret version in a reference, then wondering why rotation changed nothing.",
                "Giving the API Key Vault Administrator when Key Vault Secrets User is all it needs.",
                "Calling the vault on every request instead of caching, and getting throttled at busy times.",
                "Expecting to turn purge protection off later; once on, it stays on.",
            ),
            B.example("Two Tracks Coach: one RBAC vault with purge protection, a shared user-assigned identity for the API and the nightly function, versionless references, and a rotation function on SecretNearExpiry. The phone only ever holds the user's own sign-in token."),
            B.check(
                "ai-keyvault-k3", "ai-keyvault",
                "Someone deletes OpenAiKey by mistake. The vault has soft delete and purge protection, with 90-day retention. What is true?",
                listOf("It can be recovered for 90 days, and nobody can purge it before then", "It is gone; soft delete only covers the vault itself", "An administrator can purge it at once"), 0,
                "Soft delete keeps secrets as well as vaults, and purge protection forbids purging until the retention period ends.",
                whyWrong = mapOf(
                    1 to "Soft delete covers secrets, keys and certificates as well as whole vaults.",
                    2 to "That is exactly what purge protection blocks, even for administrators.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-keyvault-match", "Pair each term with what it does.",
                "Key Vault Secrets User" to "Reads secret values, nothing more",
                "SecretNearExpiry" to "Event that can trigger a rotation function",
                "Soft delete" to "Keeps deleted secrets for 7 to 90 days",
                "Purge protection" to "Blocks permanent deletion until retention ends",
            ),
            B.gap(
                "ai-x-keyvault-gap1", "An identity that is its own resource, shared by several apps.",
                "That is a ___-assigned managed identity.", "user", "User",
            ),
            B.gap(
                "ai-x-keyvault-gap2", "Retention can be set when the vault is created.",
                "Soft delete keeps deleted objects for 7 to ___ days.", "90", "ninety",
            ),
            B.tryIt("Try it in 15 minutes", "A few cents of vault operations; delete the resource group afterwards.",
                "Create an RBAC vault and give yourself Key Vault Secrets Officer",
                "Store a secret twice and list its versions with az keyvault secret list-versions",
                "Read it in Python with DefaultAzureCredential after az login",
                "Delete it, find it with az keyvault secret list-deleted, and recover it",
            ),
            B.explain(
                "ai-keyvault-x1",
                "Explain how Two Tracks Coach keeps its AI API key safe, rotates it, and survives an accidental deletion.",
                "The Android app never holds the key; it calls the backend, which reads it from Key Vault with a managed identity through DefaultAzureCredential, so no credential is stored. The secret has an expiry date, and Event Grid delivers SecretNearExpiry to a function that writes a new version. The app asks for the latest version rather than pinning one, so rotation reaches it. Soft delete keeps a deleted secret for up to 90 days and purge protection stops anyone purging it early.",
                "Managed identity, no secret on the phone" to listOf("managed identity", "defaultazurecredential", "entra"),
                "Near-expiry event drives rotation" to listOf("secretnearexpiry", "near expiry", "event grid", "rotation", "rotates"),
                "Read the latest version, do not pin" to listOf("latest version", "versionless", "pinning", "pinned"),
                "Soft delete and purge protection" to listOf("soft delete", "purge"),
            ),
            B.glossary(
                "Managed identity" to "A login Azure creates and maintains for a resource; nothing to store.",
                "User-assigned identity" to "A standalone identity you can share across apps and create in advance.",
                "Secret version" to "Each value written to a secret; reading without a version gets the latest.",
                "SecretNearExpiry" to "Event Grid event Key Vault sends about 30 days before a secret expires.",
                "Key rotation policy" to "A schedule on a key that makes Key Vault create new key versions itself.",
                "Soft delete" to "A recycle bin keeping deleted vault objects for 7 to 90 days.",
                "Purge protection" to "Stops permanent deletion until retention ends; cannot be switched off.",
            ),
            B.recap(
                "Only the backend reads secrets, with a managed identity and a read-only role; the phone holds none.",
                "Read versionless and cache; rotate keys with a policy and secrets with SecretNearExpiry and a function.",
                "Soft delete keeps deletions 7 to 90 days; purge protection blocks purging and cannot be turned off.",
            ),
        ),
    )

    private val appConfig = Topic(
        id = "ai-appconfig", title = "App Configuration: settings and feature flags in one place", minutes = 25,
        references = listOf(
            B.ref("What is Azure App Configuration?", LEARN + "azure/azure-app-configuration/overview"),
            B.ref("Keys and values in App Configuration", LEARN + "azure/azure-app-configuration/concept-key-value"),
            B.ref("Python configuration provider", LEARN + "azure/azure-app-configuration/reference-python-provider"),
            B.ref("Snapshots in App Configuration", LEARN + "azure/azure-app-configuration/concept-snapshots"),
            B.ref("Feature management overview", LEARN + "azure/azure-app-configuration/concept-feature-management"),
            B.ref("Authorize access with Microsoft Entra ID", LEARN + "azure/azure-app-configuration/concept-enable-rbac"),
            B.ref("Learn module: Manage application settings with Azure App Configuration", LEARN + "training/modules/manage-app-settings-app-config/"),
        ),
        blocks = listOf(
            B.warmup(
                "ai-appconfig-w1", "ai-appconfig",
                "The Coach API must notice changed settings without a restart. How many keys should it watch for changes?",
                listOf("One sentinel key", "Every key it loaded", "None; App Configuration restarts the app"), 0,
                "Checking hundreds of keys on every refresh is wasteful. There is a pattern with a name for this.",
            ),
            B.plain("""
                App Configuration is one shared place for settings that are not secrets: the model name, the
                answer length, feature switches. Every copy of the Coach API reads from it, so you change a value
                once and they all follow. Secrets stay in Key Vault; App Configuration only points to them.
            """),
            B.diagram(
                caption = "Plain settings live in App Configuration, secrets stay in Key Vault, and the API watches one sentinel key to know when to reload.",
                nodes = listOf(
                    DNode("store", "App Configuration", 0.5f, 0.12f, w = 0.5f, h = 0.16f, kind = NodeKind.CYLINDER, tone = 1),
                    DNode("k1", "Coach:MaxTokens = 800 (prod)", 0.27f, 0.38f, w = 0.46f, h = 0.13f, kind = NodeKind.PILL),
                    DNode("k2", "Coach:OpenAiKey (KV ref)", 0.75f, 0.38f, w = 0.44f, h = 0.13f, kind = NodeKind.PILL, tone = 2),
                    DNode("k3", "Coach:Sentinel = 12", 0.27f, 0.58f, w = 0.46f, h = 0.13f, kind = NodeKind.PILL, tone = 2),
                    DNode("kv", "Key Vault", 0.75f, 0.58f, w = 0.36f, h = 0.13f, kind = NodeKind.CYLINDER),
                    DNode("app", "Coach API: load() and refresh()", 0.5f, 0.86f, w = 0.7f, h = 0.16f, tone = 1),
                ),
                edges = listOf(
                    DEdge("store", "k1"),
                    DEdge("store", "k2"),
                    DEdge("k2", "kv", label = "points to"),
                    DEdge("k3", "app", label = "watched"),
                    DEdge("app", "kv", label = "own identity", dashed = true),
                ),
                aspect = 0.8f,
            ),
            B.h("Keys, values and labels"),
            B.table(
                listOf("Concept", "Meaning", "Coach example"),
                listOf("Key", "A name, with : or / to group", "Coach:MaxTokens"),
                listOf("Value", "A string; JSON with a content type", "800"),
                listOf("Label", "A variant of the same key", "prod, dev, or no label"),
                listOf("Key Vault reference", "A key whose value points to a secret", "Coach:OpenAiKey"),
                listOf("Feature flag", "An on/off switch, optionally with filters", "VoiceAnswers"),
                listOf("Snapshot", "A named, frozen set of key-values", "release-1-4"),
            ),
            B.p("""
                Labels give one key a value per environment. Load the shared defaults with no label first, then
                the environment's label, and the later value wins for keys that appear in both.
            """),
            B.h("Loading in Python"),
            B.code("python", """
                from azure.appconfiguration.provider import load, SettingSelector, WatchKey
                from azure.identity import DefaultAzureCredential

                cred = DefaultAzureCredential()
                config = load(
                    endpoint="https://tt-coach-config.azconfig.io",
                    credential=cred,
                    selects=[
                        SettingSelector(key_filter="Coach:*", label_filter="\0"),   # no label
                        SettingSelector(key_filter="Coach:*", label_filter="prod"),  # overrides
                    ],
                    trim_prefixes=["Coach:"],
                    refresh_on=[WatchKey("Coach:Sentinel")],
                    refresh_interval=30,
                    keyvault_credential=cred,        # resolves Key Vault references
                    feature_flag_enabled=True,
                    feature_flag_refresh_enabled=True,
                )

                @api.middleware("http")              # api is the FastAPI app
                async def refresh_config(request, call_next):
                    config.refresh()                 # does nothing until 30 s have passed
                    return await call_next(request)

                max_tokens = int(config["MaxTokens"])
            """),
            B.remember("Refresh is driven by activity: you call refresh(), and it only checks the store once the interval has passed. With refresh_on, the sentinel is the only settings key checked, and when it changes, every setting reloads. Feature flags with feature_flag_refresh_enabled=True are watched separately: each loaded flag is checked on its own."),
            B.check(
                "ai-appconfig-k1", "ai-appconfig",
                "You change three settings and want every Coach API instance to take all three together. What do you do last?",
                listOf("Change the sentinel key's value", "Restart each instance", "Lower refresh_interval to one second"), 0,
                "Instances watch only the sentinel. Changing it last means they reload once, with all three new values in place.",
                whyWrong = mapOf(
                    1 to "Dynamic refresh exists so you do not have to restart anything.",
                    2 to "Refreshing more often does not help: they still only reload when the sentinel changes, and you add load.",
                ),
            ),
            B.h("Secrets through App Configuration"),
            B.p("""
                A Key Vault reference is a key whose value is the address of a secret. App Configuration never
                reads the secret: the provider does, with keyvault_credential. So the app's identity needs Key
                Vault Secrets User on the vault as well as App Configuration Data Reader on the store.
            """),
            B.check(
                "ai-appconfig-k2", "ai-appconfig",
                "The Coach API loads Coach:OpenAiKey, a Key Vault reference, and gets a 403 from Key Vault. It can read every plain key. What is missing?",
                listOf("Key Vault Secrets User for the app's identity on the vault", "App Configuration Data Owner", "A label on the reference"), 0,
                "The provider fetches the secret with the app's own identity, so that identity needs a read role on the vault.",
                whyWrong = mapOf(
                    1 to "Writing to the store has nothing to do with reading a secret from the vault.",
                    2 to "Labels pick which value loads; they do not grant access to Key Vault.",
                ),
            ),
            B.h("Feature flags and snapshots"),
            B.bullets(
                "A feature flag is a switch stored under the .appconfig.featureflag/ prefix; load it with feature_flag_enabled=True.",
                "Filters make a flag conditional: a percentage of users, a time window, or targeting named users and groups.",
                "In Python the featuremanagement package reads the loaded flags: FeatureManager(config).is_enabled(\"VoiceAnswers\").",
                "A snapshot is a named, unchangeable copy of selected key-values. Load it with SettingSelector(snapshot_name=\"release-1-4\") to roll out or roll back a known set.",
            ),
            B.tip("\"Read settings only\" means App Configuration Data Reader; \"create snapshots or change values\" means Data Owner. \"Reload when one key changes\" means a sentinel with refresh_on."),
            B.worked("""
                Worked example. The Coach API in production should use 800 tokens; dev uses 2,000. Usaid sets
                Coach:MaxTokens to 800 with label prod and 2,000 with label dev. The API loads no-label keys, then
                its own label, watches Coach:Sentinel and calls refresh() in a middleware. To change the limit he
                edits the prod value, then bumps the sentinel; within 30 seconds of the next request each instance
                reloads.
            """),
            B.faded(
                "ai-appconfig-f1",
                "Same API. Product wants a new prompt template and a lower temperature live together, with no restart.",
                listOf(
                    "Update Coach:PromptTemplate and Coach:Temperature under the prod label.",
                    "Then change the ___ key, the one settings key each instance watches before reloading its settings.",
                    "On the next request after the interval, refresh() sees it and reloads every key.",
                ),
                blank = 1,
                answers = listOf("sentinel"),
                note = "Change the sentinel last, so instances never reload half the change.",
            ),
            B.steps("Do it for real",
                "az appconfig create --name tt-coach-config --resource-group tt-rg --location westeurope --sku standard" to "The store, with endpoint https://tt-coach-config.azconfig.io.",
                "az role assignment create --assignee-object-id <principalId> --assignee-principal-type ServicePrincipal --role \"App Configuration Data Reader\" --scope <store-id>" to "The API's identity can read keys and snapshots.",
                "az appconfig kv set --name tt-coach-config --key Coach:MaxTokens --value 800 --label prod --auth-mode login" to "A labelled value for production.",
                "az appconfig kv set-keyvault --name tt-coach-config --key Coach:OpenAiKey --secret-identifier https://tt-coach-kv.vault.azure.net/secrets/OpenAiKey --auth-mode login" to "A Key Vault reference, no secret in the store.",
                "az appconfig feature set --name tt-coach-config --feature VoiceAnswers --auth-mode login" to "A flag, off until you enable it.",
                "az appconfig kv set --name tt-coach-config --key Coach:Sentinel --value 13 --auth-mode login" to "Running instances reload on their next refresh.",
            ),
            B.mistakes(
                "Storing the API key as a plain value in App Configuration instead of a Key Vault reference.",
                "Watching every key rather than one sentinel, so each refresh costs many requests.",
                "Never calling refresh(), then wondering why changes never arrive.",
                "Granting Data Reader on the store but no Key Vault role, so every Key Vault reference fails.",
            ),
            B.example("Two Tracks Coach: prompt settings and limits per label, one sentinel key, a VoiceAnswers flag rolled out to 10 percent of users, and a snapshot taken before each release so a bad change can be rolled back by name."),
            B.check(
                "ai-appconfig-k3", "ai-appconfig",
                "A release must use exactly the settings that were tested, even if someone edits the store afterwards. What do you use?",
                listOf("A snapshot loaded by name", "A new label per release", "A longer refresh interval"), 0,
                "A snapshot cannot be changed once created, so the release always gets the tested set.",
                whyWrong = mapOf(
                    1 to "Values under a label can still be edited at any time.",
                    2 to "The values still change; you just notice later.",
                ),
            ),
            B.h("Check yourself"),
            B.match(
                "ai-x-appconfig-match", "Pair each piece with its job.",
                "Label" to "A per-environment variant of one key",
                "Sentinel key" to "The one key watched to trigger a full reload",
                "Snapshot" to "A frozen, named set of key-values",
                "Feature flag" to "A switch that can be on for some users only",
            ),
            B.gap(
                "ai-x-appconfig-gap1", "The read-only role for an app's identity.",
                "Grant App Configuration Data ___ on the store.", "Reader", "reader",
            ),
            B.tryIt("Try it in 15 minutes", "The Free tier is enough.",
                "Create a store and give yourself App Configuration Data Owner",
                "Add one key with a dev label and one with a prod label",
                "Load it in Python with azure-appconfiguration-provider and print both",
                "Watch a sentinel, change a value and the sentinel, and see refresh() pick it up",
            ),
            B.explain(
                "ai-appconfig-x1",
                "Explain how the Coach API gets per-environment settings, reloads them without restarting, and reads its secret.",
                "Each key has a value per environment through labels, and the app loads the no-label defaults then its own label. It loads with the Python provider and calls refresh in a middleware; only the sentinel key is watched, so changing it reloads everything. The API key is a Key Vault reference resolved with keyvault_credential. The managed identity needs App Configuration Data Reader on the store and a read role on the vault.",
                "Labels per environment" to listOf("label", "labels"),
                "A sentinel triggers refresh" to listOf("sentinel", "refresh_on", "watchkey"),
                "Secrets through a Key Vault reference" to listOf("key vault reference", "keyvault_credential", "key vault"),
                "Identity with a data reader role" to listOf("data reader", "managed identity"),
            ),
            B.glossary(
                "Label" to "A tag that gives one key different values, often per environment.",
                "Sentinel key" to "A key watched for changes; when it moves, all settings reload.",
                "Configuration provider" to "The azure-appconfiguration-provider library that loads settings into a dictionary.",
                "Key Vault reference" to "A key whose value is a secret's address, resolved by the app.",
                "Feature flag" to "A stored switch, optionally with filters, read by featuremanagement.",
                "Snapshot" to "A named, unchangeable set of key-values for safe rollout and rollback.",
            ),
            B.recap(
                "Keys, values and labels hold non-secret settings; secrets stay in Key Vault behind references.",
                "Load with the Python provider, watch one sentinel and call refresh() as requests come in.",
                "Feature flags switch behaviour live, snapshots freeze a release, Data Reader is the app's role.",
            ),
        ),
    )

    val topics: List<Topic> = listOf(keyVault, appConfig)
}
