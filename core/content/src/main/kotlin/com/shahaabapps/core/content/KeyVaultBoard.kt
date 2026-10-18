package com.shahaabapps.core.content

/**
 * The board for the Key Vault episode. Line numbers are the episode's own
 * (Az204Podcasts' keyVault, from 0); each scene and beat names the line that
 * says what it shows.
 */
internal object KeyVaultBoard {
    private const val ASPECT = 0.8f

    val board = PodcastBoard(
        "az-keyvault",
        listOf(
            // 0 "Your app needs a password to open a locked box. Where should the app keep that password?"
            Scene(
                0, "The question",
                DiagramSpec(
                    "An app reads a secret from the vault, but to get in it seems to need a secret of its own.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.2f, 0.42f, w = 0.3f, h = 0.2f),
                        DNode("vault", "Key Vault", 0.78f, 0.42f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("cred", "Its own password?", 0.2f, 0.8f, w = 0.34f, h = 0.2f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(DEdge("app", "vault", "read a secret"), DEdge("cred", "app", dashed = true)),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(0, show = setOf("app", "vault"), pulse = "app" to "vault"),
                    Beat(1, show = setOf("cred"), light = setOf("cred"), card = BoardCard("In a settings file?", "Then the file is what you must protect. It only moves the problem.")),
                ),
            ),
            // 2 "the box itself... Azure Key Vault." 4 a safe in the cloud. 6 secrets, keys. 8 keys never leave. 10 certificates. 12 tiers.
            Scene(
                2, "What the vault holds",
                DiagramSpec(
                    "Key Vault holds secrets, keys and certificates; keys never leave it, so data goes in to be encrypted.",
                    nodes = listOf(
                        DNode("vault", "Key Vault", 0.5f, 0.2f, w = 0.34f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("secrets", "Secrets", 0.17f, 0.62f, w = 0.3f, h = 0.16f, tone = 1, sub = "readable, versioned"),
                        DNode("keys", "Keys", 0.5f, 0.62f, w = 0.3f, h = 0.16f, tone = 1, sub = "never leave"),
                        DNode("certs", "Certificates", 0.83f, 0.62f, w = 0.3f, h = 0.16f, tone = 1, sub = "the padlock"),
                        DNode("data", "Your data", 0.2f, 0.9f, w = 0.3f, h = 0.14f, kind = NodeKind.PILL),
                        DNode("tier", "Premium: HSM-backed", 0.72f, 0.9f, w = 0.46f, h = 0.14f, kind = NodeKind.NOTE),
                    ),
                    edges = listOf(
                        DEdge("vault", "secrets"), DEdge("vault", "keys"), DEdge("vault", "certs"),
                        DEdge("data", "keys", "encrypt"),
                    ),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(2, show = setOf("vault")),
                    Beat(4, light = setOf("vault"), card = BoardCard("Key Vault", "A safe in the cloud. Only the people and apps you allow can open it.")),
                    Beat(6, show = setOf("secrets", "keys"), light = setOf("secrets", "keys"), card = BoardCard("Secrets and keys", "A secret is hidden text you can read back. A key scrambles data.")),
                    Beat(8, show = setOf("data"), light = setOf("keys", "data"), pulse = "data" to "keys", card = BoardCard("Keys never leave", "You send data in; the scrambling happens inside the vault.")),
                    Beat(10, show = setOf("certs"), light = setOf("certs"), card = BoardCard("Certificates", "The files behind the browser padlock. Some renew themselves.")),
                    Beat(12, show = setOf("tier"), light = setOf("keys", "tier"), card = BoardCard("Hardware-protected keys?", "Premium tier: HSM-backed. Standard: software-protected.")),
                ),
            ),
            // 13 "So who's allowed to open the safe?" 14 roles, RBAC, least privilege.
            Scene(
                13, "Who may open it",
                DiagramSpec(
                    "Give the app the smallest role it needs on the vault: Secrets User to read, not Administrator.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("vault", "Key Vault", 0.8f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                        DNode("user", "Secrets User", 0.3f, 0.78f, w = 0.36f, h = 0.16f, kind = NodeKind.PILL),
                        DNode("admin", "Administrator", 0.74f, 0.78f, w = 0.36f, h = 0.16f, kind = NodeKind.PILL, tone = 2),
                    ),
                    edges = listOf(DEdge("app", "vault", "a role")),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(13, show = setOf("app", "vault")),
                    Beat(14, show = setOf("user", "admin"), light = setOf("user", "app"), pulse = "app" to "vault", card = BoardCard("A role is a named permission", "Only reads secrets? Key Vault Secrets User, not Administrator.")),
                ),
            ),
            // 15 "And if someone deletes a secret by mistake?" 16 soft delete, purge protection.
            Scene(
                15, "Deleted by mistake",
                DiagramSpec(
                    "Soft delete keeps a deleted secret recoverable; purge protection stops anyone erasing it for good in that window.",
                    nodes = listOf(
                        DNode("secret", "A secret", 0.2f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("soft", "Soft delete", 0.78f, 0.3f, w = 0.36f, h = 0.2f, tone = 1, sub = "a recycle bin"),
                        DNode("purge", "Purge protection", 0.78f, 0.78f, w = 0.4f, h = 0.2f, sub = "the bin is locked"),
                    ),
                    edges = listOf(DEdge("secret", "soft", "deleted"), DEdge("soft", "purge", dashed = true)),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(15, show = setOf("secret")),
                    Beat(16, show = setOf("soft", "purge"), light = setOf("purge"), pulse = "secret" to "soft", card = BoardCard("\"Prevent permanent deletion\"", "That exam phrase means purge protection.")),
                ),
            ),
            // 17 "How does the app open the safe with no password?" 18 managed identity, a badge.
            Scene(
                17, "A badge, not a password",
                DiagramSpec(
                    "Azure makes the app a badge, its managed identity; the badge holds a role on the vault, and no password is stored.",
                    nodes = listOf(
                        DNode("app", "Your app", 0.18f, 0.3f, w = 0.3f, h = 0.2f),
                        DNode("id", "Managed identity", 0.5f, 0.78f, w = 0.42f, h = 0.18f, kind = NodeKind.PILL, sub = "the app's badge"),
                        DNode("vault", "Key Vault", 0.82f, 0.3f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(DEdge("app", "id", "wears"), DEdge("id", "vault", "Secrets User")),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(17, show = setOf("app", "vault")),
                    Beat(18, show = setOf("id"), light = setOf("id"), pulse = "id" to "vault", card = BoardCard("Managed identity", "A badge Azure makes for your app. No password to store, leak or change.")),
                ),
            ),
            // 20 system-assigned. 22 user-assigned. 24 shared and ready first means user-assigned.
            Scene(
                20, "Two kinds of badge",
                DiagramSpec(
                    "A system-assigned identity belongs to one app; a user-assigned one is made on its own and shared by many.",
                    nodes = listOf(
                        DNode("sys", "System-assigned", 0.24f, 0.2f, w = 0.42f, h = 0.16f, kind = NodeKind.PILL),
                        DNode("one", "One app", 0.24f, 0.62f, w = 0.3f, h = 0.18f, tone = 1),
                        DNode("usr", "User-assigned", 0.76f, 0.2f, w = 0.42f, h = 0.16f, kind = NodeKind.PILL),
                        DNode("a1", "App", 0.58f, 0.62f, w = 0.18f, h = 0.16f, tone = 1),
                        DNode("a2", "Function", 0.88f, 0.62f, w = 0.22f, h = 0.16f, tone = 1),
                        DNode("a3", "VM", 0.74f, 0.9f, w = 0.18f, h = 0.14f, tone = 1),
                    ),
                    edges = listOf(
                        DEdge("sys", "one"),
                        DEdge("usr", "a1"), DEdge("usr", "a2"), DEdge("usr", "a3"),
                    ),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(20, show = setOf("sys", "one"), light = setOf("sys", "one"), card = BoardCard("System-assigned", "One click. It belongs to one app and is deleted with it.")),
                    Beat(22, show = setOf("usr", "a1", "a2", "a3"), light = setOf("usr", "a1", "a2", "a3"), card = BoardCard("User-assigned", "Made on its own, first. Give it a role once, hand it to many apps.")),
                    Beat(24, light = setOf("usr"), card = BoardCard("Shared, and ready before the apps?", "User-assigned. A system-assigned badge cannot be shared.")),
                ),
            ),
            // 25 "What does the code look like?" 26 DefaultAzureCredential.
            Scene(
                25, "The same code everywhere",
                DiagramSpec(
                    "DefaultAzureCredential uses your az login on your laptop and the managed identity in Azure.",
                    nodes = listOf(
                        DNode("dev", "Your laptop", 0.18f, 0.18f, w = 0.34f, h = 0.18f, tone = 1, sub = "after az login"),
                        DNode("azure", "In Azure", 0.18f, 0.76f, w = 0.34f, h = 0.18f, tone = 1, sub = "managed identity"),
                        // Split at the word, so it wraps as two words rather than mid-name.
                        DNode("dac", "DefaultAzure Credential", 0.48f, 0.47f, w = 0.32f, h = 0.22f),
                        DNode("vault", "Key Vault", 0.87f, 0.47f, w = 0.26f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(DEdge("dev", "dac"), DEdge("azure", "dac"), DEdge("dac", "vault")),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(25, show = setOf("dac", "vault")),
                    Beat(26, show = setOf("dev", "azure"), light = setOf("dac"), pulse = "dac" to "vault", card = BoardCard("C#", "new SecretClient(vaultUri, new DefaultAzureCredential())", code = true)),
                ),
            ),
            // 27 "And if I don't want to write any code at all?" 28 Key Vault reference. 30 still needs the role.
            Scene(
                27, "No code at all",
                DiagramSpec(
                    "A Key Vault reference in an app setting is fetched when the app starts, with the app's managed identity, which still needs its role.",
                    nodes = listOf(
                        DNode("setting", "App setting", 0.22f, 0.22f, w = 0.36f, h = 0.2f, kind = NodeKind.NOTE, sub = "a Key Vault reference"),
                        DNode("app", "Your app", 0.22f, 0.72f, w = 0.34f, h = 0.2f),
                        DNode("id", "Managed identity", 0.6f, 0.72f, w = 0.36f, h = 0.16f, kind = NodeKind.PILL),
                        DNode("vault", "Key Vault", 0.82f, 0.24f, w = 0.3f, h = 0.3f, kind = NodeKind.CYLINDER),
                    ),
                    edges = listOf(DEdge("setting", "app", "start-up"), DEdge("id", "vault", "Secrets User"), DEdge("vault", "setting", "fetched", dashed = true)),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(27, show = setOf("app")),
                    Beat(28, show = setOf("setting", "vault"), light = setOf("setting"), pulse = "vault" to "setting", card = BoardCard("App setting", "@Microsoft.KeyVault(VaultName=v;SecretName=s)", code = true)),
                    Beat(30, show = setOf("id"), light = setOf("id"), pulse = "id" to "vault", card = BoardCard("It still needs the role", "The reference saves you the code, not the permission.")),
                ),
            ),
            // 31 "What do people usually get wrong?" 32 the four mistakes.
            Scene(
                31, "Mistakes to avoid",
                DiagramSpec(
                    "Four ways to get Key Vault access wrong, as the topic lists them.",
                    nodes = listOf(
                        DNode("admin", "Administrator for a reader", 0.26f, 0.22f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("role", "A badge with no role: 403", 0.76f, 0.22f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("pin", "A pinned old version", 0.26f, 0.72f, w = 0.46f, h = 0.24f, kind = NodeKind.NOTE),
                        DNode("gone", "Deleting a shared badge", 0.76f, 0.72f, w = 0.44f, h = 0.24f, kind = NodeKind.NOTE),
                    ),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(32, show = setOf("admin", "role", "pin", "gone"), card = BoardCard("Updates never arriving?", "Leave the version off the reference to get the latest.")),
                ),
            ),
            // 33 "Can you sum it up in three lines?" 34 the summary; 36 today's task.
            Scene(
                33, "In three lines",
                DiagramSpec(
                    "The episode in three lines: what the vault holds, which badge, and how the app reads with no password.",
                    nodes = listOf(
                        DNode("one", "Secrets, keys, certificates", 0.5f, 0.16f, w = 0.8f, h = 0.2f, tone = 1, sub = "keys never leave"),
                        DNode("two", "A badge, not a password", 0.5f, 0.5f, w = 0.8f, h = 0.2f, tone = 1, sub = "system or user-assigned"),
                        DNode("three", "DefaultAzureCredential", 0.5f, 0.84f, w = 0.8f, h = 0.2f, tone = 1, sub = "or a Key Vault reference"),
                    ),
                    aspect = ASPECT,
                ),
                beats = listOf(
                    Beat(34, show = setOf("one", "two", "three")),
                    Beat(36, card = BoardCard("Today", "A vault with roles, a secret, Secrets User; then a reference with a green tick.")),
                ),
            ),
        ),
    )
}
